package org.telegram.ui;

import android.app.Activity;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.EmojiData;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$TL_dataJSON;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_getStickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_setTyping;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_sendMessageEmojiInteraction;
import org.telegram.tgnet.TLRPC$TL_stickerPack;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickerSetBulletinLayout;
import org.telegram.ui.Components.StickersAlert;
/* loaded from: classes3.dex */
public class EmojiAnimationsOverlay implements NotificationCenter.NotificationCenterDelegate {
    private static final HashSet<String> excludeEmojiFromPack;
    private static final HashSet<String> supportedEmoji = new HashSet<>();
    private boolean attached;
    ChatActivity chatActivity;
    FrameLayout contentLayout;
    int currentAccount;
    long dialogId;
    Runnable hintRunnable;
    String lastTappedEmoji;
    RecyclerListView listView;
    Runnable sentInteractionsRunnable;
    TLRPC$TL_messages_stickerSet set;
    int threadMsgId;
    boolean inited = false;
    HashMap<String, ArrayList<TLRPC$Document>> emojiInteractionsStickersMap = new HashMap<>();
    HashMap<Long, Integer> lastAnimationIndex = new HashMap<>();
    Random random = new Random();
    int lastTappedMsgId = -1;
    long lastTappedTime = 0;
    ArrayList<Long> timeIntervals = new ArrayList<>();
    ArrayList<Integer> animationIndexes = new ArrayList<>();
    ArrayList<DrawingObject> drawingObjects = new ArrayList<>();

    public void onAllEffectsEnd() {
        throw null;
    }

    static {
        HashSet<String> hashSet = new HashSet<>();
        excludeEmojiFromPack = hashSet;
        hashSet.add("0⃣");
        hashSet.add("1⃣");
        hashSet.add("2⃣");
        hashSet.add("3⃣");
        hashSet.add("4⃣");
        hashSet.add("5⃣");
        hashSet.add("6⃣");
        hashSet.add("7⃣");
        hashSet.add("8⃣");
        hashSet.add("9⃣");
    }

    public EmojiAnimationsOverlay(ChatActivity chatActivity, FrameLayout frameLayout, RecyclerListView recyclerListView, int i, long j, int i2) {
        this.chatActivity = chatActivity;
        this.contentLayout = frameLayout;
        this.listView = recyclerListView;
        this.currentAccount = i;
        this.dialogId = j;
        this.threadMsgId = i2;
    }

    public void onAttachedToWindow() {
        this.attached = true;
        checkStickerPack();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.diceStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.onEmojiInteractionsReceived);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
    }

    public void onDetachedFromWindow() {
        this.attached = false;
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.diceStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.onEmojiInteractionsReceived);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
    }

    public void checkStickerPack() {
        if (this.inited) {
            return;
        }
        TLRPC$TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByName("EmojiAnimations");
        this.set = stickerSetByName;
        if (stickerSetByName == null) {
            this.set = MediaDataController.getInstance(this.currentAccount).getStickerSetByEmojiOrName("EmojiAnimations");
        }
        if (this.set == null) {
            MediaDataController.getInstance(this.currentAccount).loadStickersByEmojiOrName("EmojiAnimations", false, true);
        }
        if (this.set == null) {
            return;
        }
        HashMap hashMap = new HashMap();
        for (int i = 0; i < this.set.documents.size(); i++) {
            hashMap.put(Long.valueOf(this.set.documents.get(i).id), this.set.documents.get(i));
        }
        for (int i2 = 0; i2 < this.set.packs.size(); i2++) {
            TLRPC$TL_stickerPack tLRPC$TL_stickerPack = this.set.packs.get(i2);
            if (!excludeEmojiFromPack.contains(tLRPC$TL_stickerPack.emoticon) && tLRPC$TL_stickerPack.documents.size() > 0) {
                supportedEmoji.add(tLRPC$TL_stickerPack.emoticon);
                ArrayList<TLRPC$Document> arrayList = new ArrayList<>();
                this.emojiInteractionsStickersMap.put(tLRPC$TL_stickerPack.emoticon, arrayList);
                for (int i3 = 0; i3 < tLRPC$TL_stickerPack.documents.size(); i3++) {
                    arrayList.add((TLRPC$Document) hashMap.get(tLRPC$TL_stickerPack.documents.get(i3)));
                }
                if (tLRPC$TL_stickerPack.emoticon.equals("❤")) {
                    String[] strArr = {"🧡", "💛", "💚", "💙", "💜", "🖤", "\u1f90d", "\u1f90e"};
                    for (int i4 = 0; i4 < 8; i4++) {
                        String str = strArr[i4];
                        supportedEmoji.add(str);
                        this.emojiInteractionsStickersMap.put(str, arrayList);
                    }
                }
            }
        }
        this.inited = true;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        Integer printingStringType;
        if (i == NotificationCenter.diceStickersDidLoad) {
            if (!"EmojiAnimations".equals((String) objArr[0])) {
                return;
            }
            checkStickerPack();
        } else if (i == NotificationCenter.onEmojiInteractionsReceived) {
            long longValue = ((Long) objArr[0]).longValue();
            TLRPC$TL_sendMessageEmojiInteraction tLRPC$TL_sendMessageEmojiInteraction = (TLRPC$TL_sendMessageEmojiInteraction) objArr[1];
            if (longValue != this.dialogId || !supportedEmoji.contains(tLRPC$TL_sendMessageEmojiInteraction.emoticon)) {
                return;
            }
            int i3 = tLRPC$TL_sendMessageEmojiInteraction.msg_id;
            if (tLRPC$TL_sendMessageEmojiInteraction.interaction.data == null) {
                return;
            }
            try {
                JSONArray jSONArray = new JSONObject(tLRPC$TL_sendMessageEmojiInteraction.interaction.data).getJSONArray("a");
                for (int i4 = 0; i4 < jSONArray.length(); i4++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i4);
                    AndroidUtilities.runOnUIThread(new AnonymousClass1(i3, jSONObject.optInt("i", 1) - 1), (long) (jSONObject.optDouble("t", 0.0d) * 1000.0d));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (i == NotificationCenter.updateInterfaces && (printingStringType = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.dialogId, this.threadMsgId)) != null && printingStringType.intValue() == 5) {
            cancelHintRunnable();
        }
    }

    /* renamed from: org.telegram.ui.EmojiAnimationsOverlay$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ int val$animation;
        final /* synthetic */ int val$messageId;

        AnonymousClass1(int i, int i2) {
            EmojiAnimationsOverlay.this = r1;
            this.val$messageId = i;
            this.val$animation = i2;
        }

        @Override // java.lang.Runnable
        public void run() {
            EmojiAnimationsOverlay.this.findViewAndShowAnimation(this.val$messageId, this.val$animation);
        }
    }

    public void findViewAndShowAnimation(int i, int i2) {
        if (!this.attached) {
            return;
        }
        ChatMessageCell chatMessageCell = null;
        int i3 = 0;
        while (true) {
            if (i3 >= this.listView.getChildCount()) {
                break;
            }
            View childAt = this.listView.getChildAt(i3);
            if (childAt instanceof ChatMessageCell) {
                ChatMessageCell chatMessageCell2 = (ChatMessageCell) childAt;
                if (chatMessageCell2.getPhotoImage().hasNotThumb() && chatMessageCell2.getMessageObject().getStickerEmoji() != null && chatMessageCell2.getMessageObject().getId() == i) {
                    chatMessageCell = chatMessageCell2;
                    break;
                }
            }
            i3++;
        }
        if (chatMessageCell == null) {
            return;
        }
        this.chatActivity.restartSticker(chatMessageCell);
        if (!EmojiData.hasEmojiSupportVibration(chatMessageCell.getMessageObject().getStickerEmoji())) {
            chatMessageCell.performHapticFeedback(3);
        }
        showAnimationForCell(chatMessageCell, i2, false, true);
    }

    public void draw(Canvas canvas) {
        float f;
        float f2;
        if (!this.drawingObjects.isEmpty()) {
            int i = 0;
            while (i < this.drawingObjects.size()) {
                DrawingObject drawingObject = this.drawingObjects.get(i);
                drawingObject.viewFound = false;
                int i2 = 0;
                while (true) {
                    if (i2 >= this.listView.getChildCount()) {
                        f = 0.0f;
                        break;
                    }
                    View childAt = this.listView.getChildAt(i2);
                    if (childAt instanceof ChatMessageCell) {
                        ChatMessageCell chatMessageCell = (ChatMessageCell) childAt;
                        if (chatMessageCell.getMessageObject().getId() == drawingObject.messageId) {
                            drawingObject.viewFound = true;
                            float x = this.listView.getX() + childAt.getX();
                            float y = this.listView.getY() + childAt.getY();
                            f = childAt.getY();
                            if (drawingObject.isPremiumSticker) {
                                drawingObject.lastX = x + chatMessageCell.getPhotoImage().getImageX();
                                drawingObject.lastY = y + chatMessageCell.getPhotoImage().getImageY();
                            } else {
                                float imageX = x + chatMessageCell.getPhotoImage().getImageX();
                                float imageY = y + chatMessageCell.getPhotoImage().getImageY();
                                if (drawingObject.isOut) {
                                    f2 = ((-chatMessageCell.getPhotoImage().getImageWidth()) * 2.0f) + AndroidUtilities.dp(24.0f);
                                } else {
                                    f2 = -AndroidUtilities.dp(24.0f);
                                }
                                drawingObject.lastX = imageX + f2;
                                drawingObject.lastY = imageY - chatMessageCell.getPhotoImage().getImageWidth();
                            }
                            drawingObject.lastW = chatMessageCell.getPhotoImage().getImageWidth();
                            drawingObject.lastH = chatMessageCell.getPhotoImage().getImageHeight();
                        }
                    }
                    i2++;
                }
                if (!drawingObject.viewFound || drawingObject.lastH + f < this.chatActivity.getChatListViewPadding() || f > this.listView.getMeasuredHeight() - this.chatActivity.blurredViewBottomOffset) {
                    drawingObject.removing = true;
                }
                if (drawingObject.removing) {
                    float f3 = drawingObject.removeProgress;
                    if (f3 != 1.0f) {
                        float clamp = Utilities.clamp(f3 + 0.10666667f, 1.0f, 0.0f);
                        drawingObject.removeProgress = clamp;
                        drawingObject.imageReceiver.setAlpha(1.0f - clamp);
                        this.chatActivity.contentView.invalidate();
                    }
                }
                if (drawingObject.isPremiumSticker) {
                    float f4 = drawingObject.lastH;
                    float f5 = 1.49926f * f4;
                    float f6 = 0.0546875f * f5;
                    float f7 = ((drawingObject.lastY + (f4 / 2.0f)) - (f5 / 2.0f)) - (0.00279f * f5);
                    if (!drawingObject.isOut) {
                        drawingObject.imageReceiver.setImageCoords(drawingObject.lastX - f6, f7, f5, f5);
                    } else {
                        drawingObject.imageReceiver.setImageCoords(((drawingObject.lastX + drawingObject.lastW) - f5) + f6, f7, f5, f5);
                    }
                    if (!drawingObject.isOut) {
                        canvas.save();
                        canvas.scale(-1.0f, 1.0f, drawingObject.imageReceiver.getCenterX(), drawingObject.imageReceiver.getCenterY());
                        drawingObject.imageReceiver.draw(canvas);
                        canvas.restore();
                    } else {
                        drawingObject.imageReceiver.draw(canvas);
                    }
                } else {
                    ImageReceiver imageReceiver = drawingObject.imageReceiver;
                    float f8 = drawingObject.lastX + drawingObject.randomOffsetX;
                    float f9 = drawingObject.lastY + drawingObject.randomOffsetY;
                    float f10 = drawingObject.lastW;
                    imageReceiver.setImageCoords(f8, f9, f10 * 3.0f, f10 * 3.0f);
                    if (!drawingObject.isOut) {
                        canvas.save();
                        canvas.scale(-1.0f, 1.0f, drawingObject.imageReceiver.getCenterX(), drawingObject.imageReceiver.getCenterY());
                        drawingObject.imageReceiver.draw(canvas);
                        canvas.restore();
                    } else {
                        drawingObject.imageReceiver.draw(canvas);
                    }
                }
                if (drawingObject.removeProgress == 1.0f || (drawingObject.wasPlayed && drawingObject.imageReceiver.getLottieAnimation() != null && drawingObject.imageReceiver.getLottieAnimation().getCurrentFrame() == drawingObject.imageReceiver.getLottieAnimation().getFramesCount() - 2)) {
                    this.drawingObjects.remove(i);
                    i--;
                } else if (drawingObject.imageReceiver.getLottieAnimation() != null && drawingObject.imageReceiver.getLottieAnimation().isRunning()) {
                    drawingObject.wasPlayed = true;
                } else if (drawingObject.imageReceiver.getLottieAnimation() != null && !drawingObject.imageReceiver.getLottieAnimation().isRunning()) {
                    drawingObject.imageReceiver.getLottieAnimation().setCurrentFrame(0, true);
                    drawingObject.imageReceiver.getLottieAnimation().start();
                }
                i++;
            }
            if (this.drawingObjects.isEmpty()) {
                onAllEffectsEnd();
            }
            this.contentLayout.invalidate();
        }
    }

    public boolean onTapItem(ChatMessageCell chatMessageCell, ChatActivity chatActivity) {
        if (chatActivity.isSecretChat() || chatMessageCell.getMessageObject() == null || chatMessageCell.getMessageObject().getId() < 0) {
            return false;
        }
        if (!chatMessageCell.getMessageObject().isPremiumSticker() && chatActivity.currentUser == null) {
            return false;
        }
        boolean showAnimationForCell = showAnimationForCell(chatMessageCell, -1, true, false);
        if (showAnimationForCell && (!EmojiData.hasEmojiSupportVibration(chatMessageCell.getMessageObject().getStickerEmoji()) || chatMessageCell.getMessageObject().isPremiumSticker())) {
            chatMessageCell.performHapticFeedback(3);
        }
        if (chatMessageCell.getMessageObject().isPremiumSticker()) {
            chatMessageCell.getMessageObject().forcePlayEffect = false;
            chatMessageCell.getMessageObject().messageOwner.premiumEffectWasPlayed = true;
            chatActivity.getMessagesStorage().updateMessageCustomParams(this.dialogId, chatMessageCell.getMessageObject().messageOwner);
            return showAnimationForCell;
        }
        Integer printingStringType = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.dialogId, this.threadMsgId);
        if ((printingStringType == null || printingStringType.intValue() != 5) && this.hintRunnable == null && showAnimationForCell && ((Bulletin.getVisibleBulletin() == null || !Bulletin.getVisibleBulletin().isShowing()) && SharedConfig.emojiInteractionsHintCount > 0 && UserConfig.getInstance(this.currentAccount).getClientUserId() != chatActivity.currentUser.id)) {
            SharedConfig.updateEmojiInteractionsHintCount(SharedConfig.emojiInteractionsHintCount - 1);
            StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(chatActivity.getParentActivity(), null, -1, MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(chatMessageCell.getMessageObject().getStickerEmoji()), chatActivity.getResourceProvider());
            stickerSetBulletinLayout.subtitleTextView.setVisibility(8);
            stickerSetBulletinLayout.titleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("EmojiInteractionTapHint", 2131625609, chatActivity.currentUser.first_name)));
            stickerSetBulletinLayout.titleTextView.setTypeface(null);
            stickerSetBulletinLayout.titleTextView.setMaxLines(3);
            stickerSetBulletinLayout.titleTextView.setSingleLine(false);
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(Bulletin.make(chatActivity, stickerSetBulletinLayout, 2750));
            this.hintRunnable = anonymousClass2;
            AndroidUtilities.runOnUIThread(anonymousClass2, 1500L);
        }
        return showAnimationForCell;
    }

    /* renamed from: org.telegram.ui.EmojiAnimationsOverlay$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements Runnable {
        final /* synthetic */ Bulletin val$bulletin;

        AnonymousClass2(Bulletin bulletin) {
            EmojiAnimationsOverlay.this = r1;
            this.val$bulletin = bulletin;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.val$bulletin.show();
            EmojiAnimationsOverlay.this.hintRunnable = null;
        }
    }

    public void cancelHintRunnable() {
        Runnable runnable = this.hintRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        this.hintRunnable = null;
    }

    private boolean showAnimationForCell(ChatMessageCell chatMessageCell, int i, boolean z, boolean z2) {
        MessageObject messageObject;
        String stickerEmoji;
        TLRPC$VideoSize tLRPC$VideoSize;
        TLRPC$Document tLRPC$Document;
        boolean z3;
        Runnable runnable;
        int i2 = i;
        if (this.drawingObjects.size() <= 12 && chatMessageCell.getPhotoImage().hasNotThumb() && (stickerEmoji = (messageObject = chatMessageCell.getMessageObject()).getStickerEmoji()) != null) {
            float imageHeight = chatMessageCell.getPhotoImage().getImageHeight();
            float imageWidth = chatMessageCell.getPhotoImage().getImageWidth();
            if (imageHeight <= 0.0f || imageWidth <= 0.0f) {
                return false;
            }
            String unwrapEmoji = unwrapEmoji(stickerEmoji);
            boolean isPremiumSticker = messageObject.isPremiumSticker();
            if (!supportedEmoji.contains(unwrapEmoji) && !isPremiumSticker) {
                return false;
            }
            ArrayList<TLRPC$Document> arrayList = this.emojiInteractionsStickersMap.get(unwrapEmoji);
            if ((arrayList == null || arrayList.isEmpty()) && !isPremiumSticker) {
                return false;
            }
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < this.drawingObjects.size(); i5++) {
                if (this.drawingObjects.get(i5).messageId == chatMessageCell.getMessageObject().getId()) {
                    i3++;
                    if (this.drawingObjects.get(i5).imageReceiver.getLottieAnimation() == null || this.drawingObjects.get(i5).imageReceiver.getLottieAnimation().isGeneratingCache()) {
                        return false;
                    }
                }
                if (this.drawingObjects.get(i5).document != null && chatMessageCell.getMessageObject().getDocument() != null && this.drawingObjects.get(i5).document.id == chatMessageCell.getMessageObject().getDocument().id) {
                    i4++;
                }
            }
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = null;
            if (z && isPremiumSticker && i3 > 0) {
                if (Bulletin.getVisibleBulletin() != null && Bulletin.getVisibleBulletin().hash == messageObject.getId()) {
                    return false;
                }
                TLRPC$InputStickerSet inputStickerSet = messageObject.getInputStickerSet();
                if (inputStickerSet.short_name != null) {
                    tLRPC$TL_messages_stickerSet = MediaDataController.getInstance(this.currentAccount).getStickerSetByName(inputStickerSet.short_name);
                }
                if (tLRPC$TL_messages_stickerSet == null) {
                    tLRPC$TL_messages_stickerSet = MediaDataController.getInstance(this.currentAccount).getStickerSetById(inputStickerSet.id);
                }
                if (tLRPC$TL_messages_stickerSet == null) {
                    TLRPC$TL_messages_getStickerSet tLRPC$TL_messages_getStickerSet = new TLRPC$TL_messages_getStickerSet();
                    tLRPC$TL_messages_getStickerSet.stickerset = inputStickerSet;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getStickerSet, new EmojiAnimationsOverlay$$ExternalSyntheticLambda3(this, messageObject));
                } else {
                    lambda$showAnimationForCell$0(tLRPC$TL_messages_stickerSet, messageObject);
                }
                return false;
            } else if (i3 >= 4) {
                return false;
            } else {
                if (isPremiumSticker) {
                    tLRPC$VideoSize = messageObject.getPremiumStickerAnimation();
                    tLRPC$Document = null;
                } else {
                    if (i2 < 0 || i2 > arrayList.size() - 1) {
                        i2 = Math.abs(this.random.nextInt()) % arrayList.size();
                    }
                    tLRPC$Document = arrayList.get(i2);
                    tLRPC$VideoSize = null;
                }
                if (tLRPC$Document == null && tLRPC$VideoSize == null) {
                    return false;
                }
                DrawingObject drawingObject = new DrawingObject(this, null);
                drawingObject.isPremiumSticker = messageObject.isPremiumSticker();
                drawingObject.randomOffsetX = ((this.random.nextInt() % 101) / 100.0f) * (imageWidth / 4.0f);
                drawingObject.randomOffsetY = (imageHeight / 4.0f) * ((this.random.nextInt() % 101) / 100.0f);
                drawingObject.messageId = chatMessageCell.getMessageObject().getId();
                drawingObject.document = tLRPC$Document;
                drawingObject.isOut = chatMessageCell.getMessageObject().isOutOwner();
                drawingObject.imageReceiver.setAllowStartAnimation(true);
                if (tLRPC$Document != null) {
                    int i6 = (int) ((imageWidth * 2.0f) / AndroidUtilities.density);
                    Integer num = this.lastAnimationIndex.get(Long.valueOf(tLRPC$Document.id));
                    int intValue = num == null ? 0 : num.intValue();
                    this.lastAnimationIndex.put(Long.valueOf(tLRPC$Document.id), Integer.valueOf((intValue + 1) % 4));
                    ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$Document);
                    drawingObject.imageReceiver.setUniqKeyPrefix(intValue + "_" + drawingObject.messageId + "_");
                    drawingObject.imageReceiver.setImage(forDocument, i6 + "_" + i6 + "_pcache", null, "tgs", this.set, 1);
                    z3 = isPremiumSticker;
                } else {
                    int i7 = (int) ((imageWidth * 1.5f) / AndroidUtilities.density);
                    if (i4 > 0) {
                        z3 = isPremiumSticker;
                        Integer num2 = this.lastAnimationIndex.get(Long.valueOf(messageObject.getDocument().id));
                        int intValue2 = num2 == null ? 0 : num2.intValue();
                        this.lastAnimationIndex.put(Long.valueOf(messageObject.getDocument().id), Integer.valueOf((intValue2 + 1) % 4));
                        drawingObject.imageReceiver.setUniqKeyPrefix(intValue2 + "_" + drawingObject.messageId + "_");
                    } else {
                        z3 = isPremiumSticker;
                    }
                    drawingObject.document = messageObject.getDocument();
                    drawingObject.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$VideoSize, messageObject.getDocument()), i7 + "_" + i7, null, "tgs", this.set, 1);
                }
                drawingObject.imageReceiver.setLayerNum(Integer.MAX_VALUE);
                drawingObject.imageReceiver.setAutoRepeat(0);
                if (drawingObject.imageReceiver.getLottieAnimation() != null) {
                    if (drawingObject.isPremiumSticker) {
                        drawingObject.imageReceiver.getLottieAnimation().setCurrentFrame(0, false, true);
                    }
                    drawingObject.imageReceiver.getLottieAnimation().start();
                }
                this.drawingObjects.add(drawingObject);
                drawingObject.imageReceiver.onAttachedToWindow();
                drawingObject.imageReceiver.setParentView(this.contentLayout);
                this.contentLayout.invalidate();
                if (z && !z3) {
                    int i8 = this.lastTappedMsgId;
                    if (i8 != 0 && i8 != chatMessageCell.getMessageObject().getId() && (runnable = this.sentInteractionsRunnable) != null) {
                        AndroidUtilities.cancelRunOnUIThread(runnable);
                        this.sentInteractionsRunnable.run();
                    }
                    this.lastTappedMsgId = chatMessageCell.getMessageObject().getId();
                    this.lastTappedEmoji = unwrapEmoji;
                    if (this.lastTappedTime == 0) {
                        this.lastTappedTime = System.currentTimeMillis();
                        this.timeIntervals.clear();
                        this.animationIndexes.clear();
                        this.timeIntervals.add(0L);
                        this.animationIndexes.add(Integer.valueOf(i2));
                    } else {
                        this.timeIntervals.add(Long.valueOf(System.currentTimeMillis() - this.lastTappedTime));
                        this.animationIndexes.add(Integer.valueOf(i2));
                    }
                    Runnable runnable2 = this.sentInteractionsRunnable;
                    if (runnable2 != null) {
                        AndroidUtilities.cancelRunOnUIThread(runnable2);
                        this.sentInteractionsRunnable = null;
                    }
                    EmojiAnimationsOverlay$$ExternalSyntheticLambda0 emojiAnimationsOverlay$$ExternalSyntheticLambda0 = new EmojiAnimationsOverlay$$ExternalSyntheticLambda0(this);
                    this.sentInteractionsRunnable = emojiAnimationsOverlay$$ExternalSyntheticLambda0;
                    AndroidUtilities.runOnUIThread(emojiAnimationsOverlay$$ExternalSyntheticLambda0, 500L);
                }
                if (z2) {
                    MessagesController.getInstance(this.currentAccount).sendTyping(this.dialogId, this.threadMsgId, 11, unwrapEmoji, 0);
                }
                return true;
            }
        }
        return false;
    }

    public /* synthetic */ void lambda$showAnimationForCell$1(MessageObject messageObject, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new EmojiAnimationsOverlay$$ExternalSyntheticLambda2(this, tLObject, messageObject));
    }

    public /* synthetic */ void lambda$showAnimationForCell$2() {
        sendCurrentTaps();
        this.sentInteractionsRunnable = null;
    }

    /* renamed from: showStickerSetBulletin */
    public void lambda$showAnimationForCell$0(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet, MessageObject messageObject) {
        if (MessagesController.getInstance(this.currentAccount).premiumLocked || this.chatActivity.getParentActivity() == null) {
            return;
        }
        StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(this.contentLayout.getContext(), null, -1, messageObject.getDocument(), this.chatActivity.getResourceProvider());
        stickerSetBulletinLayout.titleTextView.setText(tLRPC$TL_messages_stickerSet.set.title);
        stickerSetBulletinLayout.subtitleTextView.setText(LocaleController.getString("PremiumStickerTooltip", 2131627619));
        Bulletin.UndoButton undoButton = new Bulletin.UndoButton(this.chatActivity.getParentActivity(), true, this.chatActivity.getResourceProvider());
        stickerSetBulletinLayout.setButton(undoButton);
        undoButton.setUndoAction(new EmojiAnimationsOverlay$$ExternalSyntheticLambda1(this, messageObject));
        undoButton.setText(LocaleController.getString("ViewAction", 2131628911));
        Bulletin make = Bulletin.make(this.chatActivity, stickerSetBulletinLayout, 2750);
        make.hash = messageObject.getId();
        make.show();
    }

    public /* synthetic */ void lambda$showStickerSetBulletin$3(MessageObject messageObject) {
        Activity parentActivity = this.chatActivity.getParentActivity();
        ChatActivity chatActivity = this.chatActivity;
        TLRPC$InputStickerSet inputStickerSet = messageObject.getInputStickerSet();
        ChatActivity chatActivity2 = this.chatActivity;
        StickersAlert stickersAlert = new StickersAlert(parentActivity, chatActivity, inputStickerSet, null, chatActivity2.chatActivityEnterView, chatActivity2.getResourceProvider());
        stickersAlert.setCalcMandatoryInsets(this.chatActivity.isKeyboardVisible());
        this.chatActivity.showDialog(stickersAlert);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0029, code lost:
        if (r9.charAt(r3) <= 57343) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0043, code lost:
        if (r9.charAt(r3) != 9794) goto L19;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v6, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r9v8, types: [java.lang.CharSequence] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String unwrapEmoji(String str) {
        int length = str.length();
        int i = 0;
        String str2 = str;
        while (i < length) {
            if (i < length - 1) {
                if (str2.charAt(i) == 55356) {
                    int i2 = i + 1;
                    if (str2.charAt(i2) >= 57339) {
                    }
                }
                if (str2.charAt(i) == 8205) {
                    int i3 = i + 1;
                    if (str2.charAt(i3) != 9792) {
                    }
                    length -= 2;
                    str2 = TextUtils.concat(str2.subSequence(0, i), str2.subSequence(i + 2, str2.length()));
                    i--;
                    i++;
                    str2 = str2;
                }
            }
            if (str2.charAt(i) == 65039) {
                length--;
                str2 = TextUtils.concat(str2.subSequence(0, i), str2.subSequence(i + 1, str2.length()));
                i--;
                i++;
                str2 = str2;
            } else {
                i++;
                str2 = str2;
            }
        }
        return str2.toString();
    }

    private void sendCurrentTaps() {
        if (this.lastTappedMsgId == 0) {
            return;
        }
        TLRPC$TL_sendMessageEmojiInteraction tLRPC$TL_sendMessageEmojiInteraction = new TLRPC$TL_sendMessageEmojiInteraction();
        tLRPC$TL_sendMessageEmojiInteraction.msg_id = this.lastTappedMsgId;
        tLRPC$TL_sendMessageEmojiInteraction.emoticon = this.lastTappedEmoji;
        tLRPC$TL_sendMessageEmojiInteraction.interaction = new TLRPC$TL_dataJSON();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("v", 1);
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < this.timeIntervals.size(); i++) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("i", this.animationIndexes.get(i).intValue() + 1);
                jSONObject2.put("t", ((float) this.timeIntervals.get(i).longValue()) / 1000.0f);
                jSONArray.put(i, jSONObject2);
            }
            jSONObject.put("a", jSONArray);
            tLRPC$TL_sendMessageEmojiInteraction.interaction.data = jSONObject.toString();
            TLRPC$TL_messages_setTyping tLRPC$TL_messages_setTyping = new TLRPC$TL_messages_setTyping();
            int i2 = this.threadMsgId;
            if (i2 != 0) {
                tLRPC$TL_messages_setTyping.top_msg_id = i2;
                tLRPC$TL_messages_setTyping.flags |= 1;
            }
            tLRPC$TL_messages_setTyping.action = tLRPC$TL_sendMessageEmojiInteraction;
            tLRPC$TL_messages_setTyping.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialogId);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_setTyping, null);
            clearSendingInfo();
        } catch (JSONException e) {
            clearSendingInfo();
            FileLog.e(e);
        }
    }

    private void clearSendingInfo() {
        this.lastTappedMsgId = 0;
        this.lastTappedEmoji = null;
        this.lastTappedTime = 0L;
        this.timeIntervals.clear();
        this.animationIndexes.clear();
    }

    public void onScrolled(int i) {
        for (int i2 = 0; i2 < this.drawingObjects.size(); i2++) {
            if (!this.drawingObjects.get(i2).viewFound) {
                this.drawingObjects.get(i2).lastY -= i;
            }
        }
    }

    public boolean isIdle() {
        return this.drawingObjects.isEmpty();
    }

    public boolean checkPosition(ChatMessageCell chatMessageCell, float f, int i) {
        float y = chatMessageCell.getY() + chatMessageCell.getPhotoImage().getCenterY();
        return y > f && y < ((float) i);
    }

    /* loaded from: classes3.dex */
    public class DrawingObject {
        TLRPC$Document document;
        ImageReceiver imageReceiver;
        boolean isOut;
        public boolean isPremiumSticker;
        public float lastH;
        public float lastW;
        public float lastX;
        public float lastY;
        int messageId;
        public float randomOffsetX;
        public float randomOffsetY;
        float removeProgress;
        boolean removing;
        public boolean viewFound;
        boolean wasPlayed;

        private DrawingObject(EmojiAnimationsOverlay emojiAnimationsOverlay) {
            this.imageReceiver = new ImageReceiver();
        }

        /* synthetic */ DrawingObject(EmojiAnimationsOverlay emojiAnimationsOverlay, AnonymousClass1 anonymousClass1) {
            this(emojiAnimationsOverlay);
        }
    }
}
