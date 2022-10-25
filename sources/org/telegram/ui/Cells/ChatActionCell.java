package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.util.ObjectsCompat$$ExternalSyntheticBackport0;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_documentEmpty;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_messageActionUserUpdatedPhoto;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.tgnet.TLRPC$TL_photoStrippedSize;
import org.telegram.tgnet.TLRPC$TL_premiumGiftOption;
import org.telegram.tgnet.TLRPC$TL_stickerPack;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.Premium.StarParticlesView;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public class ChatActionCell extends BaseCell implements DownloadController.FileDownloadProgressListener, NotificationCenter.NotificationCenterDelegate {
    private static Map<Integer, String> monthsToEmoticon;
    private int TAG;
    private SpannableStringBuilder accessibilityText;
    private AnimatedEmojiSpan.EmojiGroupedSpans animatedEmojiStack;
    private AvatarDrawable avatarDrawable;
    private int backgroundHeight;
    private Path backgroundPath;
    private boolean canDrawInParent;
    private int currentAccount;
    private MessageObject currentMessageObject;
    private ImageLocation currentVideoLocation;
    private int customDate;
    private CharSequence customText;
    private ChatActionCellDelegate delegate;
    private boolean forceWasUnread;
    private boolean giftButtonPressed;
    private RectF giftButtonRect;
    private TLRPC$VideoSize giftEffectAnimation;
    private StaticLayout giftPremiumButtonLayout;
    private float giftPremiumButtonWidth;
    private StaticLayout giftPremiumSubtitleLayout;
    private StaticLayout giftPremiumTitleLayout;
    private int giftRectSize;
    private TLRPC$Document giftSticker;
    private ImageReceiver.ImageReceiverDelegate giftStickerDelegate;
    private TextPaint giftSubtitlePaint;
    private TextPaint giftTitlePaint;
    private boolean hasReplyMessage;
    private boolean imagePressed;
    private ImageReceiver imageReceiver;
    private boolean invalidateColors;
    private boolean invalidatePath;
    private float lastTouchX;
    private float lastTouchY;
    private ArrayList<Integer> lineHeights;
    private ArrayList<Integer> lineWidths;
    private String overrideBackground;
    private Paint overrideBackgroundPaint;
    private String overrideText;
    private TextPaint overrideTextPaint;
    private URLSpan pressedLink;
    private int previousWidth;
    private RectF rect;
    private View rippleView;
    public List<SpoilerEffect> spoilers;
    private Stack<SpoilerEffect> spoilersPool;
    private StarParticlesView.Drawable starParticlesDrawable;
    private Path starsPath;
    private int starsSize;
    private int stickerSize;
    private int textHeight;
    private StaticLayout textLayout;
    TextPaint textPaint;
    private int textWidth;
    private int textX;
    private int textXLeft;
    private int textY;
    private ThemeDelegate themeDelegate;
    private float viewTop;
    private boolean visiblePartSet;
    private boolean wasLayout;

    /* loaded from: classes3.dex */
    public interface ChatActionCellDelegate {

        /* loaded from: classes3.dex */
        public final /* synthetic */ class -CC {
            public static boolean $default$canDrawOutboundsContent(ChatActionCellDelegate chatActionCellDelegate) {
                return true;
            }

            public static void $default$didClickImage(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell) {
            }

            public static boolean $default$didLongPress(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, float f, float f2) {
                return false;
            }

            public static void $default$didOpenPremiumGift(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, TLRPC$TL_premiumGiftOption tLRPC$TL_premiumGiftOption, boolean z) {
            }

            public static void $default$didPressReplyMessage(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, int i) {
            }

            public static BaseFragment $default$getBaseFragment(ChatActionCellDelegate chatActionCellDelegate) {
                return null;
            }

            public static long $default$getDialogId(ChatActionCellDelegate chatActionCellDelegate) {
                return 0L;
            }

            public static int $default$getTopicId(ChatActionCellDelegate chatActionCellDelegate) {
                return 0;
            }

            public static void $default$needOpenInviteLink(ChatActionCellDelegate chatActionCellDelegate, TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported) {
            }

            public static void $default$needOpenUserProfile(ChatActionCellDelegate chatActionCellDelegate, long j) {
            }

            public static void $default$needShowEffectOverlay(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, TLRPC$Document tLRPC$Document, TLRPC$VideoSize tLRPC$VideoSize) {
            }
        }

        boolean canDrawOutboundsContent();

        void didClickImage(ChatActionCell chatActionCell);

        boolean didLongPress(ChatActionCell chatActionCell, float f, float f2);

        void didOpenPremiumGift(ChatActionCell chatActionCell, TLRPC$TL_premiumGiftOption tLRPC$TL_premiumGiftOption, boolean z);

        void didPressReplyMessage(ChatActionCell chatActionCell, int i);

        BaseFragment getBaseFragment();

        long getDialogId();

        int getTopicId();

        void needOpenInviteLink(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported);

        void needOpenUserProfile(long j);

        void needShowEffectOverlay(ChatActionCell chatActionCell, TLRPC$Document tLRPC$Document, TLRPC$VideoSize tLRPC$VideoSize);
    }

    /* loaded from: classes3.dex */
    public interface ThemeDelegate extends Theme.ResourcesProvider {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
    }

    static {
        HashMap hashMap = new HashMap();
        monthsToEmoticon = hashMap;
        hashMap.put(1, "1⃣");
        monthsToEmoticon.put(3, "2⃣");
        monthsToEmoticon.put(6, "3⃣");
        monthsToEmoticon.put(12, "4⃣");
        monthsToEmoticon.put(24, "5⃣");
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        MessageObject messageObject;
        if (i == NotificationCenter.startSpoilers) {
            setSpoilersSuppressed(false);
        } else if (i == NotificationCenter.stopSpoilers) {
            setSpoilersSuppressed(true);
        } else if (i == NotificationCenter.didUpdatePremiumGiftStickers) {
            MessageObject messageObject2 = this.currentMessageObject;
            if (messageObject2 == null) {
                return;
            }
            setMessageObject(messageObject2, true);
        } else if (i != NotificationCenter.diceStickersDidLoad || !ObjectsCompat$$ExternalSyntheticBackport0.m(objArr[0], UserConfig.getInstance(this.currentAccount).premiumGiftsStickerPack) || (messageObject = this.currentMessageObject) == null) {
        } else {
            setMessageObject(messageObject, true);
        }
    }

    public void setSpoilersSuppressed(boolean z) {
        for (SpoilerEffect spoilerEffect : this.spoilers) {
            spoilerEffect.setSuppressUpdates(z);
        }
    }

    public /* synthetic */ void lambda$new$0(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        RLottieDrawable lottieAnimation;
        ChatActionCellDelegate chatActionCellDelegate;
        if (!z || (lottieAnimation = this.imageReceiver.getLottieAnimation()) == null) {
            return;
        }
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && !messageObject.playedGiftAnimation) {
            messageObject.playedGiftAnimation = true;
            lottieAnimation.setCurrentFrame(0, false);
            AndroidUtilities.runOnUIThread(new ChatActionCell$$ExternalSyntheticLambda2(lottieAnimation));
            if (!messageObject.wasUnread && !this.forceWasUnread) {
                return;
            }
            messageObject.wasUnread = false;
            this.forceWasUnread = false;
            try {
                performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            if (getContext() instanceof LaunchActivity) {
                ((LaunchActivity) getContext()).getFireworksOverlay().start();
            }
            TLRPC$VideoSize tLRPC$VideoSize = this.giftEffectAnimation;
            if (tLRPC$VideoSize == null || (chatActionCellDelegate = this.delegate) == null) {
                return;
            }
            chatActionCellDelegate.needShowEffectOverlay(this, this.giftSticker, tLRPC$VideoSize);
            return;
        }
        lottieAnimation.stop();
        lottieAnimation.setCurrentFrame(lottieAnimation.getFramesCount() - 1, false);
    }

    public ChatActionCell(Context context) {
        this(context, false, null);
    }

    public ChatActionCell(Context context, boolean z, ThemeDelegate themeDelegate) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.giftButtonRect = new RectF();
        this.spoilers = new ArrayList();
        this.spoilersPool = new Stack<>();
        this.lineWidths = new ArrayList<>();
        this.lineHeights = new ArrayList<>();
        this.backgroundPath = new Path();
        this.rect = new RectF();
        this.invalidatePath = true;
        this.invalidateColors = false;
        this.giftTitlePaint = new TextPaint(1);
        this.giftSubtitlePaint = new TextPaint(1);
        this.giftStickerDelegate = new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda3
            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public final void didSetImage(ImageReceiver imageReceiver, boolean z2, boolean z3, boolean z4) {
                ChatActionCell.this.lambda$new$0(imageReceiver, z2, z3, z4);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
                ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver);
            }
        };
        this.starsPath = new Path();
        this.canDrawInParent = z;
        this.themeDelegate = themeDelegate;
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.imageReceiver = imageReceiver;
        imageReceiver.setRoundRadius(AndroidUtilities.roundMessageSize / 2);
        this.avatarDrawable = new AvatarDrawable();
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        this.giftTitlePaint.setTextSize(TypedValue.applyDimension(1, 16.0f, getResources().getDisplayMetrics()));
        this.giftSubtitlePaint.setTextSize(TypedValue.applyDimension(1, 15.0f, getResources().getDisplayMetrics()));
        View view = new View(context);
        this.rippleView = view;
        view.setBackground(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 7, AndroidUtilities.dp(16.0f)));
        this.rippleView.setVisibility(8);
        addView(this.rippleView);
        StarParticlesView.Drawable drawable = new StarParticlesView.Drawable(10);
        this.starParticlesDrawable = drawable;
        drawable.type = 100;
        drawable.isCircle = false;
        drawable.roundEffect = true;
        drawable.useRotate = false;
        drawable.useBlur = true;
        drawable.checkBounds = true;
        drawable.size1 = 1;
        drawable.k3 = 0.98f;
        drawable.k2 = 0.98f;
        drawable.k1 = 0.98f;
        drawable.paused = false;
        drawable.speedScale = 0.0f;
        drawable.minLifeTime = 750L;
        drawable.randLifeTime = 750;
        drawable.init();
    }

    public void setDelegate(ChatActionCellDelegate chatActionCellDelegate) {
        this.delegate = chatActionCellDelegate;
    }

    public void setCustomDate(int i, boolean z, boolean z2) {
        String formatDateChat;
        int i2 = this.customDate;
        if (i2 == i || i2 / 3600 == i / 3600) {
            return;
        }
        if (!z) {
            formatDateChat = LocaleController.formatDateChat(i);
        } else if (i == 2147483646) {
            formatDateChat = LocaleController.getString("MessageScheduledUntilOnline", R.string.MessageScheduledUntilOnline);
        } else {
            formatDateChat = LocaleController.formatString("MessageScheduledOn", R.string.MessageScheduledOn, LocaleController.formatDateChat(i));
        }
        this.customDate = i;
        CharSequence charSequence = this.customText;
        if (charSequence != null && TextUtils.equals(formatDateChat, charSequence)) {
            return;
        }
        this.customText = formatDateChat;
        this.accessibilityText = null;
        updateTextInternal(z2);
    }

    private void updateTextInternal(boolean z) {
        if (getMeasuredWidth() != 0) {
            createLayout(this.customText, getMeasuredWidth());
            invalidate();
        }
        if (this.wasLayout) {
            buildLayout();
        } else if (z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActionCell.this.requestLayout();
                }
            });
        } else {
            requestLayout();
        }
    }

    public void setCustomText(CharSequence charSequence) {
        this.customText = charSequence;
        if (charSequence != null) {
            updateTextInternal(false);
        }
    }

    public void setOverrideColor(String str, String str2) {
        this.overrideBackground = str;
        this.overrideText = str2;
    }

    public void setMessageObject(MessageObject messageObject) {
        setMessageObject(messageObject, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x024f  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x026e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setMessageObject(MessageObject messageObject, boolean z) {
        TLRPC$PhotoSize tLRPC$PhotoSize;
        TLRPC$VideoSize tLRPC$VideoSize;
        String str;
        TLRPC$Document tLRPC$Document;
        StaticLayout staticLayout;
        if (this.currentMessageObject != messageObject || (((staticLayout = this.textLayout) != null && !TextUtils.equals(staticLayout.getText(), messageObject.messageText)) || ((!this.hasReplyMessage && messageObject.replyMessageObject != null) || z))) {
            if (BuildVars.DEBUG_PRIVATE_VERSION && Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
                FileLog.e(new IllegalStateException("Wrong thread!!!"));
            }
            this.accessibilityText = null;
            this.currentMessageObject = messageObject;
            boolean z2 = true;
            int i = 0;
            this.hasReplyMessage = messageObject.replyMessageObject != null;
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            this.previousWidth = 0;
            int i2 = messageObject.type;
            if (i2 == 18) {
                this.imageReceiver.setRoundRadius(0);
                String str2 = UserConfig.getInstance(this.currentAccount).premiumGiftsStickerPack;
                if (str2 == null) {
                    MediaDataController.getInstance(this.currentAccount).checkPremiumGiftStickers();
                    return;
                }
                TLRPC$TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByName(str2);
                if (stickerSetByName == null) {
                    stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByEmojiOrName(str2);
                }
                TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = stickerSetByName;
                if (tLRPC$TL_messages_stickerSet != null) {
                    String str3 = monthsToEmoticon.get(Integer.valueOf(messageObject.messageOwner.action.months));
                    Iterator<TLRPC$TL_stickerPack> it = tLRPC$TL_messages_stickerSet.packs.iterator();
                    TLRPC$Document tLRPC$Document2 = null;
                    while (true) {
                        if (!it.hasNext()) {
                            str = str2;
                            break;
                        }
                        TLRPC$TL_stickerPack next = it.next();
                        if (ObjectsCompat$$ExternalSyntheticBackport0.m(next.emoticon, str3)) {
                            Iterator<Long> it2 = next.documents.iterator();
                            while (it2.hasNext()) {
                                long longValue = it2.next().longValue();
                                Iterator<TLRPC$Document> it3 = tLRPC$TL_messages_stickerSet.documents.iterator();
                                while (true) {
                                    if (!it3.hasNext()) {
                                        str = str2;
                                        break;
                                    }
                                    TLRPC$Document next2 = it3.next();
                                    str = str2;
                                    if (next2.id == longValue) {
                                        tLRPC$Document2 = next2;
                                        break;
                                    }
                                    str2 = str;
                                }
                                if (tLRPC$Document2 != null) {
                                    break;
                                }
                                str2 = str;
                            }
                        }
                        str = str2;
                        if (tLRPC$Document2 != null) {
                            break;
                        }
                        str2 = str;
                    }
                    tLRPC$Document = (tLRPC$Document2 != null || tLRPC$TL_messages_stickerSet.documents.isEmpty()) ? tLRPC$Document2 : tLRPC$TL_messages_stickerSet.documents.get(0);
                } else {
                    str = str2;
                    tLRPC$Document = null;
                }
                this.forceWasUnread = messageObject.wasUnread;
                this.giftSticker = tLRPC$Document;
                if (tLRPC$Document != null) {
                    this.imageReceiver.setAllowStartLottieAnimation(false);
                    this.imageReceiver.setDelegate(this.giftStickerDelegate);
                    this.giftEffectAnimation = null;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= tLRPC$Document.video_thumbs.size()) {
                            break;
                        } else if ("f".equals(tLRPC$Document.video_thumbs.get(i3).type)) {
                            this.giftEffectAnimation = tLRPC$Document.video_thumbs.get(i3);
                            break;
                        } else {
                            i3++;
                        }
                    }
                    SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$Document.thumbs, "emptyListPlaceholder", 0.2f);
                    if (svgThumb != null) {
                        svgThumb.overrideWidthAndHeight(512, 512);
                    }
                    this.imageReceiver.setAutoRepeat(0);
                    this.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$Document), messageObject.getId() + "_130_130", svgThumb, "tgs", tLRPC$TL_messages_stickerSet, 1);
                } else {
                    MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
                    String str4 = str;
                    if (tLRPC$TL_messages_stickerSet != null) {
                        z2 = false;
                    }
                    mediaDataController.loadStickersByEmojiOrName(str4, false, z2);
                }
            } else if (i2 == 11) {
                this.imageReceiver.setAllowStartLottieAnimation(true);
                this.imageReceiver.setDelegate(null);
                this.imageReceiver.setRoundRadius(AndroidUtilities.roundMessageSize / 2);
                this.avatarDrawable.setInfo(messageObject.getDialogId(), null, null);
                if (messageObject.messageOwner.action instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                    this.imageReceiver.setImage(null, null, this.avatarDrawable, null, messageObject, 0);
                } else {
                    int size = messageObject.photoThumbs.size();
                    int i4 = 0;
                    while (true) {
                        if (i4 >= size) {
                            tLRPC$PhotoSize = null;
                            break;
                        }
                        TLRPC$PhotoSize tLRPC$PhotoSize2 = messageObject.photoThumbs.get(i4);
                        if (tLRPC$PhotoSize2 instanceof TLRPC$TL_photoStrippedSize) {
                            tLRPC$PhotoSize = tLRPC$PhotoSize2;
                            break;
                        }
                        i4++;
                    }
                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 640);
                    if (closestPhotoSizeWithSize != null) {
                        TLRPC$Photo tLRPC$Photo = messageObject.messageOwner.action.photo;
                        if (!tLRPC$Photo.video_sizes.isEmpty() && SharedConfig.autoplayGifs) {
                            tLRPC$VideoSize = tLRPC$Photo.video_sizes.get(0);
                            if (!messageObject.mediaExists && !DownloadController.getInstance(this.currentAccount).canDownloadMedia(4, tLRPC$VideoSize.size)) {
                                this.currentVideoLocation = ImageLocation.getForPhoto(tLRPC$VideoSize, tLRPC$Photo);
                                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(FileLoader.getAttachFileName(tLRPC$VideoSize), messageObject, this);
                            }
                            if (tLRPC$VideoSize == null) {
                                this.imageReceiver.setImage(ImageLocation.getForPhoto(tLRPC$VideoSize, tLRPC$Photo), ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForObject(tLRPC$PhotoSize, messageObject.photoThumbsObject), "50_50_b", this.avatarDrawable, 0L, null, messageObject, 1);
                            } else {
                                this.imageReceiver.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize, messageObject.photoThumbsObject), "150_150", ImageLocation.getForObject(tLRPC$PhotoSize, messageObject.photoThumbsObject), "50_50_b", this.avatarDrawable, 0L, null, messageObject, 1);
                            }
                        }
                        tLRPC$VideoSize = null;
                        if (tLRPC$VideoSize == null) {
                        }
                    } else {
                        this.imageReceiver.setImageBitmap(this.avatarDrawable);
                    }
                }
                this.imageReceiver.setVisible(!PhotoViewer.isShowingImage(messageObject), false);
            } else {
                this.imageReceiver.setAllowStartLottieAnimation(true);
                this.imageReceiver.setDelegate(null);
                this.imageReceiver.setImageBitmap((Bitmap) null);
            }
            View view = this.rippleView;
            if (messageObject.type != 18) {
                i = 8;
            }
            view.setVisibility(i);
            requestLayout();
        }
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    public ImageReceiver getPhotoImage() {
        return this.imageReceiver;
    }

    public void setVisiblePart(float f, int i) {
        this.visiblePartSet = true;
        this.backgroundHeight = i;
        this.viewTop = f;
    }

    @Override // org.telegram.ui.Cells.BaseCell
    protected boolean onLongPress() {
        ChatActionCellDelegate chatActionCellDelegate = this.delegate;
        if (chatActionCellDelegate != null) {
            return chatActionCellDelegate.didLongPress(this, this.lastTouchX, this.lastTouchY);
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        View view = this.rippleView;
        RectF rectF = this.giftButtonRect;
        view.layout((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
        this.imageReceiver.onDetachedFromWindow();
        setStarsPaused(true);
        this.wasLayout = false;
        AnimatedEmojiSpan.release(this, this.animatedEmojiStack);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdatePremiumGiftStickers);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.diceStickersDidLoad);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        ChatActionCellDelegate chatActionCellDelegate;
        super.onAttachedToWindow();
        this.imageReceiver.onAttachedToWindow();
        setStarsPaused(false);
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.canDrawInParent && (chatActionCellDelegate = this.delegate) != null && !chatActionCellDelegate.canDrawOutboundsContent(), this.animatedEmojiStack, this.textLayout);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdatePremiumGiftStickers);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.diceStickersDidLoad);
    }

    private void setStarsPaused(boolean z) {
        StarParticlesView.Drawable drawable = this.starParticlesDrawable;
        if (z == drawable.paused) {
            return;
        }
        drawable.paused = z;
        if (z) {
            drawable.pausedTime = System.currentTimeMillis();
            return;
        }
        for (int i = 0; i < this.starParticlesDrawable.particles.size(); i++) {
            this.starParticlesDrawable.particles.get(i).lifeTime += System.currentTimeMillis() - this.starParticlesDrawable.pausedTime;
        }
        invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00e3  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:58:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        int i;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null) {
            return super.onTouchEvent(motionEvent);
        }
        float x = motionEvent.getX();
        this.lastTouchX = x;
        float y = motionEvent.getY();
        this.lastTouchY = y;
        boolean z2 = true;
        if (motionEvent.getAction() == 0) {
            if (this.delegate != null) {
                int i2 = messageObject.type;
                if ((i2 == 11 || i2 == 18) && this.imageReceiver.isInsideImage(x, y)) {
                    this.imagePressed = true;
                    z = true;
                } else {
                    z = false;
                }
                if (messageObject.type == 18 && this.giftButtonRect.contains(x, y)) {
                    View view = this.rippleView;
                    this.giftButtonPressed = true;
                    view.setPressed(true);
                    z = true;
                }
                if (z) {
                    startCheckLongPress();
                }
                if (!z && (motionEvent.getAction() == 0 || (this.pressedLink != null && motionEvent.getAction() == 1))) {
                    i = this.textX;
                    if (x >= i) {
                        int i3 = this.textY;
                        if (y >= i3 && x <= i + this.textWidth && y <= this.textHeight + i3) {
                            float f = x - this.textXLeft;
                            int lineForVertical = this.textLayout.getLineForVertical((int) (y - i3));
                            int offsetForHorizontal = this.textLayout.getOffsetForHorizontal(lineForVertical, f);
                            float lineLeft = this.textLayout.getLineLeft(lineForVertical);
                            if (lineLeft <= f && lineLeft + this.textLayout.getLineWidth(lineForVertical) >= f) {
                                CharSequence charSequence = messageObject.messageText;
                                if (charSequence instanceof Spannable) {
                                    URLSpan[] uRLSpanArr = (URLSpan[]) ((Spannable) charSequence).getSpans(offsetForHorizontal, offsetForHorizontal, URLSpan.class);
                                    if (uRLSpanArr.length != 0) {
                                        if (motionEvent.getAction() == 0) {
                                            this.pressedLink = uRLSpanArr[0];
                                        } else {
                                            URLSpan uRLSpan = uRLSpanArr[0];
                                            URLSpan uRLSpan2 = this.pressedLink;
                                            if (uRLSpan == uRLSpan2) {
                                                openLink(uRLSpan2);
                                            }
                                        }
                                        z = z2;
                                    } else {
                                        this.pressedLink = null;
                                    }
                                    z2 = z;
                                    z = z2;
                                }
                            }
                            this.pressedLink = null;
                        }
                    }
                    this.pressedLink = null;
                }
                return z ? super.onTouchEvent(motionEvent) : z;
            }
        } else {
            if (motionEvent.getAction() != 2) {
                cancelCheckLongPress();
            }
            if (this.imagePressed) {
                int action = motionEvent.getAction();
                if (action == 1) {
                    this.imagePressed = false;
                    if (messageObject.type == 18) {
                        openPremiumGiftPreview();
                    } else {
                        ChatActionCellDelegate chatActionCellDelegate = this.delegate;
                        if (chatActionCellDelegate != null) {
                            chatActionCellDelegate.didClickImage(this);
                            playSoundEffect(0);
                        }
                    }
                } else if (action != 2) {
                    if (action == 3) {
                        this.imagePressed = false;
                    }
                } else if (!this.imageReceiver.isInsideImage(x, y)) {
                    this.imagePressed = false;
                }
            } else if (this.giftButtonPressed) {
                int action2 = motionEvent.getAction();
                if (action2 == 1) {
                    View view2 = this.rippleView;
                    this.giftButtonPressed = false;
                    view2.setPressed(false);
                    if (this.delegate != null) {
                        playSoundEffect(0);
                        openPremiumGiftPreview();
                    }
                } else if (action2 != 2) {
                    if (action2 == 3) {
                        View view3 = this.rippleView;
                        this.giftButtonPressed = false;
                        view3.setPressed(false);
                    }
                } else if (!this.giftButtonRect.contains(x, y)) {
                    View view4 = this.rippleView;
                    this.giftButtonPressed = false;
                    view4.setPressed(false);
                }
            }
        }
        z = false;
        if (!z) {
            i = this.textX;
            if (x >= i) {
            }
            this.pressedLink = null;
        }
        if (z) {
        }
    }

    private void openPremiumGiftPreview() {
        final TLRPC$TL_premiumGiftOption tLRPC$TL_premiumGiftOption = new TLRPC$TL_premiumGiftOption();
        TLRPC$MessageAction tLRPC$MessageAction = this.currentMessageObject.messageOwner.action;
        tLRPC$TL_premiumGiftOption.amount = tLRPC$MessageAction.amount;
        tLRPC$TL_premiumGiftOption.months = tLRPC$MessageAction.months;
        tLRPC$TL_premiumGiftOption.currency = tLRPC$MessageAction.currency;
        if (this.delegate != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActionCell.this.lambda$openPremiumGiftPreview$1(tLRPC$TL_premiumGiftOption);
                }
            });
        }
    }

    public /* synthetic */ void lambda$openPremiumGiftPreview$1(TLRPC$TL_premiumGiftOption tLRPC$TL_premiumGiftOption) {
        this.delegate.didOpenPremiumGift(this, tLRPC$TL_premiumGiftOption, false);
    }

    public void openLink(CharacterStyle characterStyle) {
        if (this.delegate == null || !(characterStyle instanceof URLSpan)) {
            return;
        }
        String url = ((URLSpan) characterStyle).getURL();
        if (url.startsWith("topic")) {
            URLSpan uRLSpan = this.pressedLink;
            if (uRLSpan instanceof URLSpanNoUnderline) {
                TLObject object = ((URLSpanNoUnderline) uRLSpan).getObject();
                if (!(object instanceof TLRPC$TL_forumTopic)) {
                    return;
                }
                ForumUtilities.openTopic(this.delegate.getBaseFragment(), -this.delegate.getDialogId(), (TLRPC$TL_forumTopic) object, 0);
                return;
            }
        }
        if (url.startsWith("invite")) {
            URLSpan uRLSpan2 = this.pressedLink;
            if (uRLSpan2 instanceof URLSpanNoUnderline) {
                TLObject object2 = ((URLSpanNoUnderline) uRLSpan2).getObject();
                if (!(object2 instanceof TLRPC$TL_chatInviteExported)) {
                    return;
                }
                this.delegate.needOpenInviteLink((TLRPC$TL_chatInviteExported) object2);
                return;
            }
        }
        if (url.startsWith("game")) {
            this.delegate.didPressReplyMessage(this, this.currentMessageObject.getReplyMsgId());
        } else if (url.startsWith("http")) {
            Browser.openUrl(getContext(), url);
        } else {
            this.delegate.needOpenUserProfile(Long.parseLong(url));
        }
    }

    private void createLayout(CharSequence charSequence, int i) {
        ChatActionCellDelegate chatActionCellDelegate;
        int dp = i - AndroidUtilities.dp(30.0f);
        this.invalidatePath = true;
        this.textLayout = new StaticLayout(charSequence, (TextPaint) getThemedPaint("paintChatActionText"), dp, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        if (charSequence instanceof Spannable) {
            SpoilerEffect.addSpoilers(this, this.textLayout, (Spannable) charSequence, this.spoilersPool, this.spoilers);
        }
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.canDrawInParent && (chatActionCellDelegate = this.delegate) != null && !chatActionCellDelegate.canDrawOutboundsContent(), this.animatedEmojiStack, this.textLayout);
        this.textHeight = 0;
        this.textWidth = 0;
        try {
            int lineCount = this.textLayout.getLineCount();
            for (int i2 = 0; i2 < lineCount; i2++) {
                try {
                    float lineWidth = this.textLayout.getLineWidth(i2);
                    float f = dp;
                    if (lineWidth > f) {
                        lineWidth = f;
                    }
                    this.textHeight = (int) Math.max(this.textHeight, Math.ceil(this.textLayout.getLineBottom(i2)));
                    this.textWidth = (int) Math.max(this.textWidth, Math.ceil(lineWidth));
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        this.textX = (i - this.textWidth) / 2;
        this.textY = AndroidUtilities.dp(7.0f);
        this.textXLeft = (i - this.textLayout.getWidth()) / 2;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        int dp;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null && this.customText == null) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), this.textHeight + AndroidUtilities.dp(14.0f));
            return;
        }
        if (messageObject != null && messageObject.type == 18) {
            int min = Math.min((int) ((AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : AndroidUtilities.displaySize.x) * 0.6f), ((AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) - AndroidUtilities.dp(64.0f));
            this.giftRectSize = min;
            this.stickerSize = min - AndroidUtilities.dp(106.0f);
        }
        int max = Math.max(AndroidUtilities.dp(30.0f), View.MeasureSpec.getSize(i));
        if (this.previousWidth != max) {
            this.wasLayout = true;
            this.previousWidth = max;
            buildLayout();
        }
        int i4 = 0;
        if (messageObject != null) {
            int i5 = messageObject.type;
            if (i5 == 11) {
                i3 = AndroidUtilities.roundMessageSize;
                dp = AndroidUtilities.dp(10.0f);
            } else if (i5 == 18) {
                i3 = this.giftRectSize;
                dp = AndroidUtilities.dp(12.0f);
            }
            i4 = i3 + dp;
        }
        setMeasuredDimension(max, this.textHeight + i4 + AndroidUtilities.dp(14.0f));
        if (messageObject == null || messageObject.type != 18) {
            return;
        }
        float dp2 = this.textY + this.textHeight + (this.giftRectSize * 0.075f) + this.stickerSize + AndroidUtilities.dp(4.0f) + this.giftPremiumTitleLayout.getHeight() + AndroidUtilities.dp(4.0f) + this.giftPremiumSubtitleLayout.getHeight();
        float measuredHeight = dp2 + ((((getMeasuredHeight() - dp2) - this.giftPremiumButtonLayout.getHeight()) - AndroidUtilities.dp(8.0f)) / 2.0f);
        float f = (this.previousWidth - this.giftPremiumButtonWidth) / 2.0f;
        this.giftButtonRect.set(f - AndroidUtilities.dp(18.0f), measuredHeight - AndroidUtilities.dp(8.0f), f + this.giftPremiumButtonWidth + AndroidUtilities.dp(18.0f), measuredHeight + this.giftPremiumButtonLayout.getHeight() + AndroidUtilities.dp(8.0f));
        int measuredWidth = getMeasuredWidth() << (getMeasuredHeight() + 16);
        this.starParticlesDrawable.rect.set(this.giftButtonRect);
        this.starParticlesDrawable.rect2.set(this.giftButtonRect);
        if (this.starsSize == measuredWidth) {
            return;
        }
        this.starsSize = measuredWidth;
        this.starParticlesDrawable.resetPositions();
    }

    private void buildLayout() {
        CharSequence charSequence;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            charSequence = (this.delegate.getTopicId() != 0 || !MessageObject.isTopicActionMessage(messageObject)) ? null : ForumUtilities.createActionTextWithTopic(MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-messageObject.getDialogId(), MessageObject.getTopicId(messageObject.messageOwner)), messageObject);
            if (charSequence == null) {
                TLRPC$Message tLRPC$Message = messageObject.messageOwner;
                if (tLRPC$Message != null && (tLRPC$MessageMedia = tLRPC$Message.media) != null && tLRPC$MessageMedia.ttl_seconds != 0) {
                    if (tLRPC$MessageMedia.photo instanceof TLRPC$TL_photoEmpty) {
                        charSequence = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                    } else if (tLRPC$MessageMedia.document instanceof TLRPC$TL_documentEmpty) {
                        charSequence = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
                    } else {
                        charSequence = AnimatedEmojiSpan.cloneSpans(messageObject.messageText);
                    }
                } else {
                    charSequence = AnimatedEmojiSpan.cloneSpans(messageObject.messageText);
                }
            }
        } else {
            charSequence = this.customText;
        }
        createLayout(charSequence, this.previousWidth);
        if (messageObject != null) {
            int i = messageObject.type;
            if (i == 11) {
                int i2 = AndroidUtilities.roundMessageSize;
                this.imageReceiver.setImageCoords((this.previousWidth - AndroidUtilities.roundMessageSize) / 2.0f, this.textHeight + AndroidUtilities.dp(19.0f), i2, i2);
            } else if (i != 18) {
            } else {
                createGiftPremiumLayouts(LocaleController.getString(R.string.ActionGiftPremiumTitle), LocaleController.formatString(R.string.ActionGiftPremiumSubtitle, LocaleController.formatPluralString("Months", messageObject.messageOwner.action.months, new Object[0])), LocaleController.getString(R.string.ActionGiftPremiumView), this.giftRectSize);
            }
        }
    }

    private void createGiftPremiumLayouts(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i) {
        SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(charSequence);
        valueOf.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), 0, valueOf.length(), 33);
        this.giftPremiumTitleLayout = new StaticLayout(valueOf, this.giftTitlePaint, i, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        this.giftPremiumSubtitleLayout = new StaticLayout(charSequence2, this.giftSubtitlePaint, i, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(charSequence3);
        valueOf2.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), 0, valueOf2.length(), 33);
        StaticLayout staticLayout = new StaticLayout(valueOf2, (TextPaint) getThemedPaint("paintChatActionText"), i, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        this.giftPremiumButtonLayout = staticLayout;
        this.giftPremiumButtonWidth = measureLayoutWidth(staticLayout);
    }

    private float measureLayoutWidth(Layout layout) {
        float f = 0.0f;
        for (int i = 0; i < layout.getLineCount(); i++) {
            float ceil = (int) Math.ceil(layout.getLineWidth(i));
            if (ceil > f) {
                f = ceil;
            }
        }
        return f;
    }

    public int getCustomDate() {
        return this.customDate;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        int i;
        TextPaint textPaint;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && messageObject.type == 18) {
            int dp = this.giftRectSize - AndroidUtilities.dp(106.0f);
            this.stickerSize = dp;
            this.imageReceiver.setImageCoords((this.previousWidth - dp) / 2.0f, this.textY + this.textHeight + (this.giftRectSize * 0.075f), dp, dp);
            if (this.textPaint != null && (textPaint = this.giftTitlePaint) != null && this.giftSubtitlePaint != null) {
                if (textPaint.getColor() != this.textPaint.getColor()) {
                    this.giftTitlePaint.setColor(this.textPaint.getColor());
                }
                if (this.giftSubtitlePaint.getColor() != this.textPaint.getColor()) {
                    this.giftSubtitlePaint.setColor(this.textPaint.getColor());
                }
            }
        }
        if (messageObject != null && ((i = messageObject.type) == 11 || i == 18)) {
            this.imageReceiver.draw(canvas);
        }
        if (this.textLayout == null) {
            return;
        }
        drawBackground(canvas, false);
        if (this.textPaint != null) {
            canvas.save();
            canvas.translate(this.textXLeft, this.textY);
            if (this.textLayout.getPaint() != this.textPaint) {
                buildLayout();
            }
            canvas.save();
            SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
            this.textLayout.draw(canvas);
            ChatActionCellDelegate chatActionCellDelegate = this.delegate;
            if (chatActionCellDelegate == null || chatActionCellDelegate.canDrawOutboundsContent()) {
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.textLayout, this.animatedEmojiStack, 0.0f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f);
            }
            canvas.restore();
            for (SpoilerEffect spoilerEffect : this.spoilers) {
                spoilerEffect.setColor(this.textLayout.getPaint().getColor());
                spoilerEffect.draw(canvas);
            }
            canvas.restore();
        }
        if (messageObject == null || messageObject.type != 18) {
            return;
        }
        canvas.save();
        int i2 = this.previousWidth;
        int i3 = this.giftRectSize;
        float f = (i2 - i3) / 2.0f;
        float dp2 = this.textY + this.textHeight + (i3 * 0.075f) + this.stickerSize + AndroidUtilities.dp(4.0f);
        canvas.translate(f, dp2);
        this.giftPremiumTitleLayout.draw(canvas);
        canvas.restore();
        float height = dp2 + this.giftPremiumTitleLayout.getHeight() + AndroidUtilities.dp(4.0f);
        canvas.save();
        canvas.translate(f, height);
        this.giftPremiumSubtitleLayout.draw(canvas);
        canvas.restore();
        float height2 = height + this.giftPremiumSubtitleLayout.getHeight();
        float height3 = height2 + ((((getHeight() - height2) - this.giftPremiumButtonLayout.getHeight()) - AndroidUtilities.dp(8.0f)) / 2.0f);
        canvas.drawRoundRect(this.giftButtonRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), getThemedPaint("paintChatActionBackground"));
        if (hasGradientService()) {
            canvas.drawRoundRect(this.giftButtonRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), Theme.chat_actionBackgroundGradientDarkenPaint);
        }
        this.starsPath.rewind();
        this.starsPath.addRoundRect(this.giftButtonRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), Path.Direction.CW);
        canvas.save();
        canvas.clipPath(this.starsPath);
        this.starParticlesDrawable.onDraw(canvas);
        if (!this.starParticlesDrawable.paused) {
            invalidate();
        }
        canvas.restore();
        canvas.save();
        canvas.translate(f, height3);
        this.giftPremiumButtonLayout.draw(canvas);
        canvas.restore();
    }

    public void drawBackground(Canvas canvas, boolean z) {
        Paint paint;
        Paint paint2;
        int i;
        int i2;
        int i3;
        Paint paint3;
        int i4;
        float f;
        float f2;
        int i5;
        int i6;
        int i7;
        int i8;
        float f3;
        int i9;
        int i10;
        int i11;
        if (this.canDrawInParent) {
            if (hasGradientService() && !z) {
                return;
            }
            if (!hasGradientService() && z) {
                return;
            }
        }
        Paint themedPaint = getThemedPaint("paintChatActionBackground");
        this.textPaint = (TextPaint) getThemedPaint("paintChatActionText");
        String str = this.overrideBackground;
        if (str != null) {
            int themedColor = getThemedColor(str);
            if (this.overrideBackgroundPaint == null) {
                Paint paint4 = new Paint(1);
                this.overrideBackgroundPaint = paint4;
                paint4.setColor(themedColor);
                TextPaint textPaint = new TextPaint(1);
                this.overrideTextPaint = textPaint;
                textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                this.overrideTextPaint.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize) - 2));
                this.overrideTextPaint.setColor(getThemedColor(this.overrideText));
            }
            themedPaint = this.overrideBackgroundPaint;
            this.textPaint = this.overrideTextPaint;
        }
        if (this.invalidatePath) {
            this.invalidatePath = false;
            this.lineWidths.clear();
            int lineCount = this.textLayout.getLineCount();
            int dp = AndroidUtilities.dp(11.0f);
            int dp2 = AndroidUtilities.dp(8.0f);
            int i12 = 0;
            for (int i13 = 0; i13 < lineCount; i13++) {
                int ceil = (int) Math.ceil(this.textLayout.getLineWidth(i13));
                if (i13 == 0 || (i11 = i12 - ceil) <= 0 || i11 > dp + dp2) {
                    i12 = ceil;
                }
                this.lineWidths.add(Integer.valueOf(i12));
            }
            for (int i14 = lineCount - 2; i14 >= 0; i14--) {
                int intValue = this.lineWidths.get(i14).intValue();
                int i15 = i12 - intValue;
                if (i15 <= 0 || i15 > dp + dp2) {
                    i12 = intValue;
                }
                this.lineWidths.set(i14, Integer.valueOf(i12));
            }
            int dp3 = AndroidUtilities.dp(4.0f);
            int measuredWidth = getMeasuredWidth() / 2;
            int dp4 = AndroidUtilities.dp(3.0f);
            int dp5 = AndroidUtilities.dp(6.0f);
            int i16 = dp - dp4;
            this.lineHeights.clear();
            this.backgroundPath.reset();
            float f4 = measuredWidth;
            this.backgroundPath.moveTo(f4, dp3);
            int i17 = 0;
            int i18 = 0;
            while (i17 < lineCount) {
                int intValue2 = this.lineWidths.get(i17).intValue();
                int lineBottom = this.textLayout.getLineBottom(i17);
                int i19 = dp2;
                int i20 = lineCount - 1;
                if (i17 < i20) {
                    paint3 = themedPaint;
                    i4 = this.lineWidths.get(i17 + 1).intValue();
                } else {
                    paint3 = themedPaint;
                    i4 = 0;
                }
                int i21 = lineBottom - i18;
                if (i17 == 0 || intValue2 > i12) {
                    f = 3.0f;
                    i21 += AndroidUtilities.dp(3.0f);
                } else {
                    f = 3.0f;
                }
                if (i17 == i20 || intValue2 > i4) {
                    i21 += AndroidUtilities.dp(f);
                }
                float f5 = (intValue2 / 2.0f) + f4;
                int i22 = (i17 == i20 || intValue2 >= i4 || i17 == 0 || intValue2 >= i12) ? i19 : dp5;
                if (i17 == 0 || intValue2 > i12) {
                    f2 = f4;
                    i5 = lineCount;
                    i6 = i12;
                    i7 = measuredWidth;
                    i8 = lineBottom;
                    this.rect.set((f5 - dp4) - dp, dp3, i16 + f5, (dp * 2) + dp3);
                    this.backgroundPath.arcTo(this.rect, -90.0f, 90.0f);
                } else {
                    f2 = f4;
                    if (intValue2 < i12) {
                        i8 = lineBottom;
                        float f6 = i16 + f5;
                        i7 = measuredWidth;
                        i5 = lineCount;
                        i6 = i12;
                        this.rect.set(f6, dp3, (i22 * 2) + f6, i10 + dp3);
                        this.backgroundPath.arcTo(this.rect, -90.0f, -90.0f);
                    } else {
                        i5 = lineCount;
                        i6 = i12;
                        i7 = measuredWidth;
                        i8 = lineBottom;
                    }
                }
                dp3 += i21;
                if (i17 == i20 || intValue2 >= i4) {
                    f3 = 3.0f;
                } else {
                    f3 = 3.0f;
                    dp3 -= AndroidUtilities.dp(3.0f);
                    i21 -= AndroidUtilities.dp(3.0f);
                }
                if (i17 != 0 && intValue2 < i6) {
                    dp3 -= AndroidUtilities.dp(f3);
                    i21 -= AndroidUtilities.dp(f3);
                }
                this.lineHeights.add(Integer.valueOf(i21));
                if (i17 == i20 || intValue2 > i4) {
                    this.rect.set((f5 - dp4) - dp, dp3 - (dp * 2), f5 + i16, dp3);
                    this.backgroundPath.arcTo(this.rect, 0.0f, 90.0f);
                } else if (intValue2 < i4) {
                    float f7 = f5 + i16;
                    this.rect.set(f7, dp3 - i9, (i22 * 2) + f7, dp3);
                    this.backgroundPath.arcTo(this.rect, 180.0f, -90.0f);
                }
                i17++;
                i12 = intValue2;
                dp2 = i19;
                themedPaint = paint3;
                f4 = f2;
                i18 = i8;
                measuredWidth = i7;
                lineCount = i5;
            }
            paint = themedPaint;
            int i23 = dp2;
            int i24 = measuredWidth;
            int i25 = lineCount - 1;
            int i26 = i25;
            while (i26 >= 0) {
                int intValue3 = i26 != 0 ? this.lineWidths.get(i26 - 1).intValue() : 0;
                int intValue4 = this.lineWidths.get(i26).intValue();
                int intValue5 = i26 != i25 ? this.lineWidths.get(i26 + 1).intValue() : 0;
                this.textLayout.getLineBottom(i26);
                float f8 = i24 - (intValue4 / 2);
                int i27 = (i26 == i25 || intValue4 >= intValue5 || i26 == 0 || intValue4 >= intValue3) ? i23 : dp5;
                if (i26 == i25 || intValue4 > intValue5) {
                    this.rect.set(f8 - i16, dp3 - (dp * 2), dp4 + f8 + dp, dp3);
                    this.backgroundPath.arcTo(this.rect, 90.0f, 90.0f);
                } else if (intValue4 < intValue5) {
                    float f9 = f8 - i16;
                    this.rect.set(f9 - (i27 * 2), dp3 - i3, f9, dp3);
                    this.backgroundPath.arcTo(this.rect, 90.0f, -90.0f);
                }
                dp3 -= this.lineHeights.get(i26).intValue();
                if (i26 == 0 || intValue4 > intValue3) {
                    this.rect.set(f8 - i16, dp3, f8 + dp4 + dp, (dp * 2) + dp3);
                    this.backgroundPath.arcTo(this.rect, 180.0f, 90.0f);
                } else if (intValue4 < intValue3) {
                    float f10 = f8 - i16;
                    this.rect.set(f10 - (i27 * 2), dp3, f10, i2 + dp3);
                    this.backgroundPath.arcTo(this.rect, 0.0f, -90.0f);
                }
                i26--;
            }
            this.backgroundPath.close();
        } else {
            paint = themedPaint;
        }
        if (!this.visiblePartSet) {
            this.backgroundHeight = ((ViewGroup) getParent()).getMeasuredHeight();
        }
        ThemeDelegate themeDelegate = this.themeDelegate;
        if (themeDelegate != null) {
            themeDelegate.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, 0.0f, this.viewTop + AndroidUtilities.dp(4.0f));
        } else {
            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, 0.0f, this.viewTop + AndroidUtilities.dp(4.0f));
        }
        int i28 = -1;
        if (!z || getAlpha() == 1.0f) {
            paint2 = paint;
            i = -1;
        } else {
            i28 = paint.getAlpha();
            i = Theme.chat_actionBackgroundGradientDarkenPaint.getAlpha();
            paint2 = paint;
            paint2.setAlpha((int) (i28 * getAlpha()));
            Theme.chat_actionBackgroundGradientDarkenPaint.setAlpha((int) (i * getAlpha()));
        }
        canvas.drawPath(this.backgroundPath, paint2);
        if (hasGradientService()) {
            canvas.drawPath(this.backgroundPath, Theme.chat_actionBackgroundGradientDarkenPaint);
        }
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && messageObject.type == 18) {
            float width = (getWidth() - this.giftRectSize) / 2.0f;
            float dp6 = this.textY + this.textHeight + AndroidUtilities.dp(12.0f);
            RectF rectF = AndroidUtilities.rectTmp;
            int i29 = this.giftRectSize;
            rectF.set(width, dp6, i29 + width, i29 + dp6);
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), paint2);
            if (hasGradientService()) {
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), Theme.chat_actionBackgroundGradientDarkenPaint);
            }
        }
        if (i28 >= 0) {
            paint2.setAlpha(i28);
            Theme.chat_actionBackgroundGradientDarkenPaint.setAlpha(i);
        }
    }

    public boolean hasGradientService() {
        ThemeDelegate themeDelegate;
        return this.overrideBackgroundPaint == null && ((themeDelegate = this.themeDelegate) == null ? Theme.hasGradientService() : themeDelegate.hasGradientService());
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || messageObject.type != 11) {
            return;
        }
        TLRPC$PhotoSize tLRPC$PhotoSize = null;
        int i = 0;
        int size = messageObject.photoThumbs.size();
        while (true) {
            if (i >= size) {
                break;
            }
            TLRPC$PhotoSize tLRPC$PhotoSize2 = messageObject.photoThumbs.get(i);
            if (tLRPC$PhotoSize2 instanceof TLRPC$TL_photoStrippedSize) {
                tLRPC$PhotoSize = tLRPC$PhotoSize2;
                break;
            }
            i++;
        }
        this.imageReceiver.setImage(this.currentVideoLocation, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForObject(tLRPC$PhotoSize, messageObject.photoThumbsObject), "50_50_b", this.avatarDrawable, 0L, null, messageObject, 1);
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        CharacterStyle[] characterStyleArr;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        MessageObject messageObject = this.currentMessageObject;
        if (!TextUtils.isEmpty(this.customText) || messageObject != null) {
            if (this.accessibilityText == null) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(!TextUtils.isEmpty(this.customText) ? this.customText : messageObject.messageText);
                for (final CharacterStyle characterStyle : (CharacterStyle[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ClickableSpan.class)) {
                    int spanStart = spannableStringBuilder.getSpanStart(characterStyle);
                    int spanEnd = spannableStringBuilder.getSpanEnd(characterStyle);
                    spannableStringBuilder.removeSpan(characterStyle);
                    spannableStringBuilder.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Cells.ChatActionCell.1
                        {
                            ChatActionCell.this = this;
                        }

                        @Override // android.text.style.ClickableSpan
                        public void onClick(View view) {
                            if (ChatActionCell.this.delegate != null) {
                                ChatActionCell.this.openLink(characterStyle);
                            }
                        }
                    }, spanStart, spanEnd, 33);
                }
                this.accessibilityText = spannableStringBuilder;
            }
            if (Build.VERSION.SDK_INT < 24) {
                accessibilityNodeInfo.setContentDescription(this.accessibilityText.toString());
            } else {
                accessibilityNodeInfo.setText(this.accessibilityText);
            }
            accessibilityNodeInfo.setEnabled(true);
        }
    }

    public void setInvalidateColors(boolean z) {
        if (this.invalidateColors == z) {
            return;
        }
        this.invalidateColors = z;
        invalidate();
    }

    private int getThemedColor(String str) {
        ThemeDelegate themeDelegate = this.themeDelegate;
        Integer color = themeDelegate != null ? themeDelegate.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }

    private Paint getThemedPaint(String str) {
        ThemeDelegate themeDelegate = this.themeDelegate;
        Paint paint = themeDelegate != null ? themeDelegate.getPaint(str) : null;
        return paint != null ? paint : Theme.getThemePaint(str);
    }

    public void drawOutboundsContent(Canvas canvas) {
        canvas.save();
        canvas.translate(this.textXLeft, this.textY);
        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.textLayout, this.animatedEmojiStack, 0.0f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f);
        canvas.restore();
    }
}
