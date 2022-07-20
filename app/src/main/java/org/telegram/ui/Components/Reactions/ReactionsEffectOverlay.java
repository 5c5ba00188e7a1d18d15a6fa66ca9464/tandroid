package org.telegram.ui.Components.Reactions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Random;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$TL_messagePeerReaction;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.ReactionsContainerLayout;
/* loaded from: classes3.dex */
public class ReactionsEffectOverlay {
    @SuppressLint({"StaticFieldLeak"})
    public static ReactionsEffectOverlay currentOverlay;
    @SuppressLint({"StaticFieldLeak"})
    public static ReactionsEffectOverlay currentShortOverlay;
    private static long lastHapticTime;
    private static int uniqPrefix;
    float animateInProgress;
    float animateOutProgress;
    private final int animationType;
    private ChatMessageCell cell;
    private final FrameLayout container;
    private ViewGroup decorView;
    private float dismissProgress;
    private boolean dismissed;
    private final AnimationView effectImageView;
    private final AnimationView emojiImageView;
    private final AnimationView emojiStaticImageView;
    private boolean finished;
    private final long groupId;
    private ReactionsContainerLayout.ReactionHolderView holderView;
    private float lastDrawnToX;
    private float lastDrawnToY;
    private final int messageId;
    private final String reaction;
    private boolean started;
    private boolean useWindow;
    private boolean wasScrolled;
    private WindowManager windowManager;
    FrameLayout windowView;
    int[] loc = new int[2];
    ArrayList<AvatarParticle> avatars = new ArrayList<>();

    static /* synthetic */ float access$216(ReactionsEffectOverlay reactionsEffectOverlay, float f) {
        float f2 = reactionsEffectOverlay.dismissProgress + f;
        reactionsEffectOverlay.dismissProgress = f2;
        return f2;
    }

    /* JADX WARN: Removed duplicated region for block: B:113:0x05d0  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x017b  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x01a1  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02e8  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x02f9  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0399  */
    /* JADX WARN: Type inference failed for: r14v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r14v2 */
    /* JADX WARN: Type inference failed for: r14v3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private ReactionsEffectOverlay(Context context, BaseFragment baseFragment, ReactionsContainerLayout reactionsContainerLayout, ChatMessageCell chatMessageCell, float f, float f2, String str, int i, int i2) {
        float f3;
        float f4;
        float f5;
        int i3;
        int i4;
        TLRPC$TL_availableReaction tLRPC$TL_availableReaction;
        ?? r14;
        int i5;
        int i6;
        Random random;
        this.holderView = null;
        this.messageId = chatMessageCell.getMessageObject().getId();
        this.groupId = chatMessageCell.getMessageObject().getGroupId();
        this.reaction = str;
        this.animationType = i2;
        this.cell = chatMessageCell;
        ReactionsLayoutInBubble.ReactionButton reactionButton = chatMessageCell.getReactionButton(str);
        ChatActivity chatActivity = baseFragment instanceof ChatActivity ? (ChatActivity) baseFragment : null;
        if (reactionsContainerLayout != null) {
            int i7 = 0;
            while (true) {
                if (i7 >= reactionsContainerLayout.recyclerListView.getChildCount()) {
                    break;
                } else if (((ReactionsContainerLayout.ReactionHolderView) reactionsContainerLayout.recyclerListView.getChildAt(i7)).currentReaction.reaction.equals(str)) {
                    this.holderView = (ReactionsContainerLayout.ReactionHolderView) reactionsContainerLayout.recyclerListView.getChildAt(i7);
                    break;
                } else {
                    i7++;
                }
            }
        }
        if (i2 == 1) {
            Random random2 = new Random();
            ArrayList<TLRPC$TL_messagePeerReaction> arrayList = chatMessageCell.getMessageObject().messageOwner.reactions != null ? chatMessageCell.getMessageObject().messageOwner.reactions.recent_reactions : null;
            if (arrayList != null && chatActivity != null && chatActivity.getDialogId() < 0) {
                int i8 = 0;
                while (i8 < arrayList.size()) {
                    if (str.equals(arrayList.get(i8).reaction) && arrayList.get(i8).unread) {
                        AvatarDrawable avatarDrawable = new AvatarDrawable();
                        ImageReceiver imageReceiver = new ImageReceiver();
                        long peerId = MessageObject.getPeerId(arrayList.get(i8).peer_id);
                        if (peerId < 0) {
                            TLRPC$Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-peerId));
                            if (chat != null) {
                                avatarDrawable.setInfo(chat);
                                imageReceiver.setForUserOrChat(chat, avatarDrawable);
                                AvatarParticle avatarParticle = new AvatarParticle(this, null);
                                avatarParticle.imageReceiver = imageReceiver;
                                avatarParticle.fromX = 0.5f;
                                avatarParticle.fromY = 0.5f;
                                float f6 = 100.0f;
                                avatarParticle.jumpY = ((Math.abs(random2.nextInt() % 100) / 100.0f) * 0.1f) + 0.3f;
                                avatarParticle.randomScale = ((Math.abs(random2.nextInt() % 100) / 100.0f) * 0.4f) + 0.8f;
                                avatarParticle.randomRotation = (Math.abs(random2.nextInt() % 100) * 60) / 100.0f;
                                avatarParticle.leftTime = (int) (((Math.abs(random2.nextInt() % 100) / 100.0f) * 200.0f) + 400.0f);
                                float f7 = 0.6f;
                                if (!this.avatars.isEmpty()) {
                                    avatarParticle.toX = ((Math.abs(random2.nextInt() % 100) * 0.6f) / 100.0f) + 0.2f;
                                    avatarParticle.toY = (Math.abs(random2.nextInt() % 100) * 0.4f) / 100.0f;
                                    random = random2;
                                } else {
                                    int i9 = 0;
                                    float f8 = 0.0f;
                                    float f9 = 0.0f;
                                    float f10 = 0.0f;
                                    while (i9 < 10) {
                                        float abs = ((Math.abs(random2.nextInt() % 100) * f7) / f6) + 0.2f;
                                        float abs2 = ((Math.abs(random2.nextInt() % 100) * 0.4f) / f6) + 0.2f;
                                        float f11 = 2.14748365E9f;
                                        int i10 = 0;
                                        while (i10 < this.avatars.size()) {
                                            float f12 = this.avatars.get(i10).toX - abs;
                                            Random random3 = random2;
                                            float f13 = this.avatars.get(i10).toY - abs2;
                                            float f14 = (f12 * f12) + (f13 * f13);
                                            if (f14 < f11) {
                                                f11 = f14;
                                            }
                                            i10++;
                                            random2 = random3;
                                        }
                                        Random random4 = random2;
                                        if (f11 > f10) {
                                            f9 = abs2;
                                            f8 = abs;
                                            f10 = f11;
                                        }
                                        i9++;
                                        random2 = random4;
                                        f7 = 0.6f;
                                        f6 = 100.0f;
                                    }
                                    random = random2;
                                    avatarParticle.toX = f8;
                                    avatarParticle.toY = f9;
                                }
                                this.avatars.add(avatarParticle);
                            }
                        } else {
                            TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(peerId));
                            if (user != null) {
                                avatarDrawable.setInfo(user);
                                imageReceiver.setForUserOrChat(user, avatarDrawable);
                                AvatarParticle avatarParticle2 = new AvatarParticle(this, null);
                                avatarParticle2.imageReceiver = imageReceiver;
                                avatarParticle2.fromX = 0.5f;
                                avatarParticle2.fromY = 0.5f;
                                float f62 = 100.0f;
                                avatarParticle2.jumpY = ((Math.abs(random2.nextInt() % 100) / 100.0f) * 0.1f) + 0.3f;
                                avatarParticle2.randomScale = ((Math.abs(random2.nextInt() % 100) / 100.0f) * 0.4f) + 0.8f;
                                avatarParticle2.randomRotation = (Math.abs(random2.nextInt() % 100) * 60) / 100.0f;
                                avatarParticle2.leftTime = (int) (((Math.abs(random2.nextInt() % 100) / 100.0f) * 200.0f) + 400.0f);
                                float f72 = 0.6f;
                                if (!this.avatars.isEmpty()) {
                                }
                                this.avatars.add(avatarParticle2);
                            }
                        }
                        i8++;
                        random2 = random;
                    }
                    random = random2;
                    i8++;
                    random2 = random;
                }
            }
        }
        ReactionsContainerLayout.ReactionHolderView reactionHolderView = this.holderView;
        boolean z = (reactionHolderView == null && (f == 0.0f || f2 == 0.0f)) ? false : true;
        if (reactionHolderView != null) {
            reactionHolderView.getLocationOnScreen(this.loc);
            f4 = this.loc[0] + this.holderView.backupImageView.getX();
            f3 = this.loc[1] + this.holderView.backupImageView.getY();
            f5 = this.holderView.backupImageView.getWidth() * this.holderView.getScaleX();
        } else if (reactionButton != null) {
            chatMessageCell.getLocationInWindow(this.loc);
            float imageX = this.loc[0] + chatMessageCell.reactionsLayoutInBubble.x + reactionButton.x + reactionButton.imageReceiver.getImageX();
            float imageY = this.loc[1] + chatMessageCell.reactionsLayoutInBubble.y + reactionButton.y + reactionButton.imageReceiver.getImageY();
            float imageHeight = reactionButton.imageReceiver.getImageHeight();
            reactionButton.imageReceiver.getImageWidth();
            f4 = imageX;
            f3 = imageY;
            f5 = imageHeight;
        } else {
            ((View) chatMessageCell.getParent()).getLocationInWindow(this.loc);
            int[] iArr = this.loc;
            f3 = iArr[1] + f2;
            f4 = iArr[0] + f;
            f5 = 0.0f;
            if (i2 != 2) {
                int dp = AndroidUtilities.dp(34.0f);
                i3 = (int) ((dp * 2.0f) / AndroidUtilities.density);
                i4 = dp;
            } else {
                if (i2 == 1) {
                    i5 = AndroidUtilities.dp(80.0f);
                    i6 = (int) ((i5 * 2.0f) / AndroidUtilities.density);
                } else {
                    int dp2 = AndroidUtilities.dp(350.0f);
                    Point point = AndroidUtilities.displaySize;
                    i5 = Math.round(Math.min(dp2, Math.min(point.x, point.y)) * 0.8f);
                    i6 = sizeForBigReaction();
                }
                i3 = i6;
                i4 = i5;
            }
            int i11 = i4 >> 1;
            int i12 = i3 >> 1;
            float f15 = f5 / i11;
            this.animateInProgress = 0.0f;
            this.animateOutProgress = 0.0f;
            FrameLayout frameLayout = new FrameLayout(context);
            this.container = frameLayout;
            int i13 = i4;
            int i14 = i3;
            this.windowView = new AnonymousClass1(context, baseFragment, chatMessageCell, str, chatActivity, i11, i2, z, f15, f4, f3);
            AnimationView animationView = new AnimationView(context);
            this.effectImageView = animationView;
            AnimationView animationView2 = new AnimationView(context);
            this.emojiImageView = animationView2;
            AnimationView animationView3 = new AnimationView(context);
            this.emojiStaticImageView = animationView3;
            tLRPC$TL_availableReaction = MediaDataController.getInstance(i).getReactionsMap().get(str);
            if (tLRPC$TL_availableReaction == null) {
                int i15 = 2;
                if (i2 != 2) {
                    TLRPC$Document tLRPC$Document = i2 == 1 ? tLRPC$TL_availableReaction.around_animation : tLRPC$TL_availableReaction.effect_animation;
                    ImageReceiver imageReceiver2 = animationView.getImageReceiver();
                    StringBuilder sb = new StringBuilder();
                    int i16 = uniqPrefix;
                    uniqPrefix = i16 + 1;
                    sb.append(i16);
                    sb.append("_");
                    sb.append(chatMessageCell.getMessageObject().getId());
                    sb.append("_");
                    imageReceiver2.setUniqKeyPrefix(sb.toString());
                    ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$Document);
                    animationView.setImage(forDocument, i14 + "_" + i14 + "_pcache", (ImageLocation) null, (String) null, 0, (Object) null);
                    r14 = 0;
                    animationView.getImageReceiver().setAutoRepeat(0);
                    animationView.getImageReceiver().setAllowStartAnimation(false);
                    if (animationView.getImageReceiver().getLottieAnimation() != null) {
                        animationView.getImageReceiver().getLottieAnimation().setCurrentFrame(0, false);
                        animationView.getImageReceiver().getLottieAnimation().start();
                    }
                    i15 = 2;
                } else {
                    r14 = 0;
                }
                if (i2 == i15) {
                    TLRPC$Document tLRPC$Document2 = tLRPC$TL_availableReaction.appear_animation;
                    ImageReceiver imageReceiver3 = animationView2.getImageReceiver();
                    StringBuilder sb2 = new StringBuilder();
                    int i17 = uniqPrefix;
                    uniqPrefix = i17 + 1;
                    sb2.append(i17);
                    sb2.append("_");
                    sb2.append(chatMessageCell.getMessageObject().getId());
                    sb2.append("_");
                    imageReceiver3.setUniqKeyPrefix(sb2.toString());
                    ImageLocation forDocument2 = ImageLocation.getForDocument(tLRPC$Document2);
                    animationView2.setImage(forDocument2, i12 + "_" + i12, (ImageLocation) null, (String) null, 0, (Object) null);
                } else if (i2 == 0) {
                    TLRPC$Document tLRPC$Document3 = tLRPC$TL_availableReaction.activate_animation;
                    ImageReceiver imageReceiver4 = animationView2.getImageReceiver();
                    StringBuilder sb3 = new StringBuilder();
                    int i18 = uniqPrefix;
                    uniqPrefix = i18 + 1;
                    sb3.append(i18);
                    sb3.append("_");
                    sb3.append(chatMessageCell.getMessageObject().getId());
                    sb3.append("_");
                    imageReceiver4.setUniqKeyPrefix(sb3.toString());
                    ImageLocation forDocument3 = ImageLocation.getForDocument(tLRPC$Document3);
                    animationView2.setImage(forDocument3, i12 + "_" + i12, (ImageLocation) null, (String) null, 0, (Object) null);
                }
                ImageReceiver imageReceiver5 = animationView2.getImageReceiver();
                int i19 = r14 == true ? 1 : 0;
                int i20 = r14 == true ? 1 : 0;
                imageReceiver5.setAutoRepeat(i19);
                animationView2.getImageReceiver().setAllowStartAnimation(r14);
                if (animationView2.getImageReceiver().getLottieAnimation() != null) {
                    if (i2 == 2) {
                        animationView2.getImageReceiver().getLottieAnimation().setCurrentFrame(animationView2.getImageReceiver().getLottieAnimation().getFramesCount() - 1, r14);
                    } else {
                        animationView2.getImageReceiver().getLottieAnimation().setCurrentFrame(r14, r14);
                        animationView2.getImageReceiver().getLottieAnimation().start();
                    }
                }
                int i21 = i13 - i11;
                int i22 = i21 >> 1;
                i21 = i2 == 1 ? i22 : i21;
                frameLayout.addView(animationView2);
                animationView2.getLayoutParams().width = i11;
                animationView2.getLayoutParams().height = i11;
                ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).topMargin = i22;
                ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).leftMargin = i21;
                if (i2 != 1) {
                    animationView3.getImageReceiver().setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.center_icon), "40_40_lastframe", null, "webp", tLRPC$TL_availableReaction, 1);
                }
                frameLayout.addView(animationView3);
                animationView3.getLayoutParams().width = i11;
                animationView3.getLayoutParams().height = i11;
                ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).topMargin = i22;
                ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).leftMargin = i21;
                this.windowView.addView(frameLayout);
                frameLayout.getLayoutParams().width = i13;
                frameLayout.getLayoutParams().height = i13;
                int i23 = -i22;
                ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = i23;
                int i24 = -i21;
                ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).leftMargin = i24;
                this.windowView.addView(animationView);
                animationView.getLayoutParams().width = i13;
                animationView.getLayoutParams().height = i13;
                animationView.getLayoutParams().width = i13;
                animationView.getLayoutParams().height = i13;
                ((FrameLayout.LayoutParams) animationView.getLayoutParams()).topMargin = i23;
                ((FrameLayout.LayoutParams) animationView.getLayoutParams()).leftMargin = i24;
                frameLayout.setPivotX(i21);
                frameLayout.setPivotY(i22);
                return;
            }
            this.dismissed = true;
            return;
        }
        if (i2 != 2) {
        }
        int i112 = i4 >> 1;
        int i122 = i3 >> 1;
        float f152 = f5 / i112;
        this.animateInProgress = 0.0f;
        this.animateOutProgress = 0.0f;
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.container = frameLayout2;
        int i132 = i4;
        int i142 = i3;
        this.windowView = new AnonymousClass1(context, baseFragment, chatMessageCell, str, chatActivity, i112, i2, z, f152, f4, f3);
        AnimationView animationView4 = new AnimationView(context);
        this.effectImageView = animationView4;
        AnimationView animationView22 = new AnimationView(context);
        this.emojiImageView = animationView22;
        AnimationView animationView32 = new AnimationView(context);
        this.emojiStaticImageView = animationView32;
        tLRPC$TL_availableReaction = MediaDataController.getInstance(i).getReactionsMap().get(str);
        if (tLRPC$TL_availableReaction == null) {
        }
    }

    /* renamed from: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends FrameLayout {
        final /* synthetic */ int val$animationType;
        final /* synthetic */ ChatMessageCell val$cell;
        final /* synthetic */ ChatActivity val$chatActivity;
        final /* synthetic */ int val$emojiSize;
        final /* synthetic */ BaseFragment val$fragment;
        final /* synthetic */ boolean val$fromHolder;
        final /* synthetic */ float val$fromScale;
        final /* synthetic */ float val$fromX;
        final /* synthetic */ float val$fromY;
        final /* synthetic */ String val$reaction;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context, BaseFragment baseFragment, ChatMessageCell chatMessageCell, String str, ChatActivity chatActivity, int i, int i2, boolean z, float f, float f2, float f3) {
            super(context);
            ReactionsEffectOverlay.this = r1;
            this.val$fragment = baseFragment;
            this.val$cell = chatMessageCell;
            this.val$reaction = str;
            this.val$chatActivity = chatActivity;
            this.val$emojiSize = i;
            this.val$animationType = i2;
            this.val$fromHolder = z;
            this.val$fromScale = f;
            this.val$fromX = f2;
            this.val$fromY = f3;
        }

        /* JADX WARN: Removed duplicated region for block: B:165:0x0445  */
        /* JADX WARN: Removed duplicated region for block: B:173:0x0474  */
        /* JADX WARN: Removed duplicated region for block: B:174:0x0477  */
        /* JADX WARN: Removed duplicated region for block: B:177:0x053d  */
        /* JADX WARN: Removed duplicated region for block: B:182:0x054a  */
        /* JADX WARN: Removed duplicated region for block: B:183:0x055e  */
        /* JADX WARN: Removed duplicated region for block: B:186:0x0568  */
        /* JADX WARN: Removed duplicated region for block: B:190:0x057b  */
        @Override // android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void dispatchDraw(Canvas canvas) {
            ChatMessageCell chatMessageCell;
            int i;
            float f;
            float f2;
            float f3;
            float f4;
            boolean z;
            int i2;
            float f5;
            if (ReactionsEffectOverlay.this.dismissed) {
                if (ReactionsEffectOverlay.this.dismissProgress != 1.0f) {
                    ReactionsEffectOverlay.access$216(ReactionsEffectOverlay.this, 0.10666667f);
                    if (ReactionsEffectOverlay.this.dismissProgress > 1.0f) {
                        ReactionsEffectOverlay.this.dismissProgress = 1.0f;
                        AndroidUtilities.runOnUIThread(new ReactionsEffectOverlay$1$$ExternalSyntheticLambda1(this));
                    }
                }
                if (ReactionsEffectOverlay.this.dismissProgress != 1.0f) {
                    setAlpha(1.0f - ReactionsEffectOverlay.this.dismissProgress);
                    super.dispatchDraw(canvas);
                }
                invalidate();
            } else if (ReactionsEffectOverlay.this.started) {
                if (ReactionsEffectOverlay.this.holderView != null) {
                    ReactionsEffectOverlay.this.holderView.backupImageView.setAlpha(0.0f);
                    ReactionsEffectOverlay.this.holderView.pressedBackupImageView.setAlpha(0.0f);
                }
                BaseFragment baseFragment = this.val$fragment;
                if (baseFragment instanceof ChatActivity) {
                    chatMessageCell = ((ChatActivity) baseFragment).findMessageCell(ReactionsEffectOverlay.this.messageId, false);
                } else {
                    chatMessageCell = this.val$cell;
                }
                if (this.val$cell.getMessageObject().shouldDrawReactionsInLayout()) {
                    i = AndroidUtilities.dp(20.0f);
                } else {
                    i = AndroidUtilities.dp(14.0f);
                }
                float f6 = i;
                if (chatMessageCell == null) {
                    f = ReactionsEffectOverlay.this.lastDrawnToX;
                    f2 = ReactionsEffectOverlay.this.lastDrawnToY;
                } else {
                    this.val$cell.getLocationInWindow(ReactionsEffectOverlay.this.loc);
                    ReactionsLayoutInBubble.ReactionButton reactionButton = this.val$cell.getReactionButton(this.val$reaction);
                    int[] iArr = ReactionsEffectOverlay.this.loc;
                    int i3 = iArr[0];
                    ReactionsLayoutInBubble reactionsLayoutInBubble = this.val$cell.reactionsLayoutInBubble;
                    f = i3 + reactionsLayoutInBubble.x;
                    f2 = iArr[1] + reactionsLayoutInBubble.y;
                    if (reactionButton != null) {
                        f += reactionButton.x + reactionButton.imageReceiver.getImageX();
                        f2 += reactionButton.y + reactionButton.imageReceiver.getImageY();
                    }
                    ChatActivity chatActivity = this.val$chatActivity;
                    if (chatActivity != null) {
                        f2 += chatActivity.drawingChatLisViewYoffset;
                    }
                    if (chatMessageCell.drawPinnedBottom && !chatMessageCell.shouldDrawTimeOnMedia()) {
                        f2 += AndroidUtilities.dp(2.0f);
                    }
                    ReactionsEffectOverlay.this.lastDrawnToX = f;
                    ReactionsEffectOverlay.this.lastDrawnToY = f2;
                }
                if (this.val$fragment.getParentActivity() == null || this.val$fragment.getFragmentView().getParent() == null || this.val$fragment.getFragmentView().getVisibility() != 0 || this.val$fragment.getFragmentView() == null) {
                    return;
                }
                this.val$fragment.getFragmentView().getLocationOnScreen(ReactionsEffectOverlay.this.loc);
                setAlpha(((View) this.val$fragment.getFragmentView().getParent()).getAlpha());
                int i4 = this.val$emojiSize;
                float f7 = f - ((i4 - f6) / 2.0f);
                float f8 = f2 - ((i4 - f6) / 2.0f);
                if (this.val$animationType != 1) {
                    int[] iArr2 = ReactionsEffectOverlay.this.loc;
                    if (f7 < iArr2[0]) {
                        f7 = iArr2[0];
                    }
                    if (i4 + f7 > iArr2[0] + getMeasuredWidth()) {
                        f7 = (ReactionsEffectOverlay.this.loc[0] + getMeasuredWidth()) - this.val$emojiSize;
                    }
                }
                CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
                float interpolation = cubicBezierInterpolator.getInterpolation(ReactionsEffectOverlay.this.animateOutProgress);
                if (this.val$animationType == 2) {
                    f3 = CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(interpolation);
                    f4 = cubicBezierInterpolator.getInterpolation(interpolation);
                } else if (this.val$fromHolder) {
                    f3 = CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(ReactionsEffectOverlay.this.animateInProgress);
                    f4 = cubicBezierInterpolator.getInterpolation(ReactionsEffectOverlay.this.animateInProgress);
                } else {
                    f3 = ReactionsEffectOverlay.this.animateInProgress;
                    f4 = f3;
                }
                float f9 = 1.0f - f3;
                float f10 = (this.val$fromScale * f9) + f3;
                float f11 = f6 / this.val$emojiSize;
                if (this.val$animationType == 1) {
                    f10 = 1.0f;
                } else {
                    f7 = (f7 * f3) + (this.val$fromX * f9);
                    f8 = (f8 * f4) + (this.val$fromY * (1.0f - f4));
                }
                ReactionsEffectOverlay.this.effectImageView.setTranslationX(f7);
                ReactionsEffectOverlay.this.effectImageView.setTranslationY(f8);
                float f12 = 1.0f - interpolation;
                ReactionsEffectOverlay.this.effectImageView.setAlpha(f12);
                ReactionsEffectOverlay.this.effectImageView.setScaleX(f10);
                ReactionsEffectOverlay.this.effectImageView.setScaleY(f10);
                int i5 = this.val$animationType;
                if (i5 == 2) {
                    f10 = (this.val$fromScale * f9) + (f11 * f3);
                    f7 = (this.val$fromX * f9) + (f * f3);
                    f8 = (this.val$fromY * (1.0f - f4)) + (f2 * f4);
                } else if (interpolation != 0.0f) {
                    f10 = (f10 * f12) + (f11 * interpolation);
                    f7 = (f7 * f12) + (f * interpolation);
                    f8 = (f8 * f12) + (f2 * interpolation);
                }
                if (i5 != 1) {
                    ReactionsEffectOverlay.this.emojiStaticImageView.setAlpha(interpolation > 0.7f ? (interpolation - 0.7f) / 0.3f : 0.0f);
                }
                ReactionsEffectOverlay.this.container.setTranslationX(f7);
                ReactionsEffectOverlay.this.container.setTranslationY(f8);
                ReactionsEffectOverlay.this.container.setScaleX(f10);
                ReactionsEffectOverlay.this.container.setScaleY(f10);
                super.dispatchDraw(canvas);
                if (this.val$animationType == 1 || ReactionsEffectOverlay.this.emojiImageView.wasPlaying) {
                    ReactionsEffectOverlay reactionsEffectOverlay = ReactionsEffectOverlay.this;
                    float f13 = reactionsEffectOverlay.animateInProgress;
                    if (f13 != 1.0f) {
                        if (this.val$fromHolder) {
                            reactionsEffectOverlay.animateInProgress = f13 + 0.045714285f;
                        } else {
                            reactionsEffectOverlay.animateInProgress = f13 + 0.07272727f;
                        }
                        if (reactionsEffectOverlay.animateInProgress > 1.0f) {
                            reactionsEffectOverlay.animateInProgress = 1.0f;
                        }
                    }
                }
                float f14 = 16.0f;
                if (this.val$animationType == 2 || ((ReactionsEffectOverlay.this.wasScrolled && this.val$animationType == 0) || ((this.val$animationType != 1 && ReactionsEffectOverlay.this.emojiImageView.wasPlaying && ReactionsEffectOverlay.this.emojiImageView.getImageReceiver().getLottieAnimation() != null && !ReactionsEffectOverlay.this.emojiImageView.getImageReceiver().getLottieAnimation().isRunning()) || (this.val$animationType == 1 && ReactionsEffectOverlay.this.effectImageView.wasPlaying && ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation() != null && !ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().isRunning())))) {
                    ReactionsEffectOverlay reactionsEffectOverlay2 = ReactionsEffectOverlay.this;
                    float f15 = reactionsEffectOverlay2.animateOutProgress;
                    if (f15 != 1.0f) {
                        int i6 = this.val$animationType;
                        if (i6 == 1) {
                            reactionsEffectOverlay2.animateOutProgress = 1.0f;
                        } else {
                            reactionsEffectOverlay2.animateOutProgress = f15 + (16.0f / (i6 == 2 ? 350.0f : 220.0f));
                        }
                        if (reactionsEffectOverlay2.animateOutProgress > 0.7f && !reactionsEffectOverlay2.finished) {
                            ReactionsEffectOverlay.startShortAnimation();
                        }
                        if (ReactionsEffectOverlay.this.animateOutProgress >= 1.0f) {
                            int i7 = this.val$animationType;
                            if (i7 == 0 || i7 == 2) {
                                this.val$cell.reactionsLayoutInBubble.animateReaction(this.val$reaction);
                            }
                            ReactionsEffectOverlay.this.animateOutProgress = 1.0f;
                            if (this.val$animationType == 1) {
                                ReactionsEffectOverlay.currentShortOverlay = null;
                            } else {
                                ReactionsEffectOverlay.currentOverlay = null;
                            }
                            this.val$cell.invalidate();
                            if (this.val$cell.getCurrentMessagesGroup() != null && this.val$cell.getParent() != null) {
                                ((View) this.val$cell.getParent()).invalidate();
                            }
                            AndroidUtilities.runOnUIThread(new ReactionsEffectOverlay$1$$ExternalSyntheticLambda0(this));
                        }
                    }
                }
                if (!ReactionsEffectOverlay.this.avatars.isEmpty() && ReactionsEffectOverlay.this.effectImageView.wasPlaying) {
                    RLottieDrawable lottieAnimation = ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation();
                    int i8 = 0;
                    while (i8 < ReactionsEffectOverlay.this.avatars.size()) {
                        AvatarParticle avatarParticle = ReactionsEffectOverlay.this.avatars.get(i8);
                        float f16 = avatarParticle.progress;
                        if (lottieAnimation != null && lottieAnimation.isRunning()) {
                            float duration = (float) ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getDuration();
                            if (((int) (duration - ((ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getCurrentFrame() / ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getFramesCount()) * duration))) >= avatarParticle.leftTime) {
                                z = false;
                                if (z) {
                                    float f17 = avatarParticle.outProgress;
                                    if (f17 != 1.0f) {
                                        float f18 = f17 + 0.10666667f;
                                        avatarParticle.outProgress = f18;
                                        if (f18 > 1.0f) {
                                            avatarParticle.outProgress = 1.0f;
                                            ReactionsEffectOverlay.this.avatars.remove(i8);
                                            i8--;
                                            i2 = 1;
                                            i8 += i2;
                                            f14 = 16.0f;
                                        }
                                        float f19 = f16 < 0.5f ? f16 / 0.5f : 1.0f - ((f16 - 0.5f) / 0.5f);
                                        float f20 = 1.0f - f16;
                                        float f21 = (avatarParticle.fromX * f20) + (avatarParticle.toX * f16);
                                        float f22 = ((avatarParticle.fromY * f20) + (avatarParticle.toY * f16)) - (avatarParticle.jumpY * f19);
                                        float f23 = avatarParticle.randomScale * f16 * (1.0f - avatarParticle.outProgress);
                                        float x = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f21);
                                        float y = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f22);
                                        int dp = AndroidUtilities.dp(f14);
                                        float f24 = dp;
                                        float f25 = f24 / 2.0f;
                                        ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.setImageCoords(x - f25, y - f25, f24, f24);
                                        ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.setRoundRadius(dp >> 1);
                                        canvas.save();
                                        canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                        canvas.scale(f23, f23, x, y);
                                        canvas.rotate(avatarParticle.currentRotation, x, y);
                                        ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.draw(canvas);
                                        canvas.restore();
                                        f5 = avatarParticle.progress;
                                        if (f5 < 1.0f) {
                                            float f26 = f5 + 0.045714285f;
                                            avatarParticle.progress = f26;
                                            if (f26 > 1.0f) {
                                                avatarParticle.progress = 1.0f;
                                            }
                                        }
                                        if (f16 >= 1.0f) {
                                            avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * 16.0f) / 500.0f;
                                        }
                                        if (avatarParticle.incrementRotation) {
                                            float f27 = avatarParticle.currentRotation;
                                            float f28 = avatarParticle.randomRotation;
                                            float f29 = f27 + (f28 / 250.0f);
                                            avatarParticle.currentRotation = f29;
                                            if (f29 > f28) {
                                                avatarParticle.incrementRotation = false;
                                            }
                                        } else {
                                            float f30 = avatarParticle.currentRotation;
                                            float f31 = avatarParticle.randomRotation;
                                            float f32 = f30 - (f31 / 250.0f);
                                            avatarParticle.currentRotation = f32;
                                            if (f32 < (-f31)) {
                                                i2 = 1;
                                                avatarParticle.incrementRotation = true;
                                                i8 += i2;
                                                f14 = 16.0f;
                                            }
                                        }
                                        i2 = 1;
                                        i8 += i2;
                                        f14 = 16.0f;
                                    }
                                }
                                if (f16 < 0.5f) {
                                }
                                float f202 = 1.0f - f16;
                                float f212 = (avatarParticle.fromX * f202) + (avatarParticle.toX * f16);
                                float f222 = ((avatarParticle.fromY * f202) + (avatarParticle.toY * f16)) - (avatarParticle.jumpY * f19);
                                float f232 = avatarParticle.randomScale * f16 * (1.0f - avatarParticle.outProgress);
                                float x2 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f212);
                                float y2 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f222);
                                int dp2 = AndroidUtilities.dp(f14);
                                float f242 = dp2;
                                float f252 = f242 / 2.0f;
                                ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.setImageCoords(x2 - f252, y2 - f252, f242, f242);
                                ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.setRoundRadius(dp2 >> 1);
                                canvas.save();
                                canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                canvas.scale(f232, f232, x2, y2);
                                canvas.rotate(avatarParticle.currentRotation, x2, y2);
                                ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.draw(canvas);
                                canvas.restore();
                                f5 = avatarParticle.progress;
                                if (f5 < 1.0f) {
                                }
                                if (f16 >= 1.0f) {
                                }
                                if (avatarParticle.incrementRotation) {
                                }
                                i2 = 1;
                                i8 += i2;
                                f14 = 16.0f;
                            }
                        }
                        z = true;
                        if (z) {
                        }
                        if (f16 < 0.5f) {
                        }
                        float f2022 = 1.0f - f16;
                        float f2122 = (avatarParticle.fromX * f2022) + (avatarParticle.toX * f16);
                        float f2222 = ((avatarParticle.fromY * f2022) + (avatarParticle.toY * f16)) - (avatarParticle.jumpY * f19);
                        float f2322 = avatarParticle.randomScale * f16 * (1.0f - avatarParticle.outProgress);
                        float x22 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f2122);
                        float y22 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f2222);
                        int dp22 = AndroidUtilities.dp(f14);
                        float f2422 = dp22;
                        float f2522 = f2422 / 2.0f;
                        ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.setImageCoords(x22 - f2522, y22 - f2522, f2422, f2422);
                        ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.setRoundRadius(dp22 >> 1);
                        canvas.save();
                        canvas.translate(0.0f, avatarParticle.globalTranslationY);
                        canvas.scale(f2322, f2322, x22, y22);
                        canvas.rotate(avatarParticle.currentRotation, x22, y22);
                        ReactionsEffectOverlay.this.avatars.get(i8).imageReceiver.draw(canvas);
                        canvas.restore();
                        f5 = avatarParticle.progress;
                        if (f5 < 1.0f) {
                        }
                        if (f16 >= 1.0f) {
                        }
                        if (avatarParticle.incrementRotation) {
                        }
                        i2 = 1;
                        i8 += i2;
                        f14 = 16.0f;
                    }
                }
                invalidate();
            } else {
                invalidate();
            }
        }

        public /* synthetic */ void lambda$dispatchDraw$0() {
            ReactionsEffectOverlay.this.removeCurrentView();
        }

        public /* synthetic */ void lambda$dispatchDraw$1() {
            ReactionsEffectOverlay.this.removeCurrentView();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            for (int i = 0; i < ReactionsEffectOverlay.this.avatars.size(); i++) {
                ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.onAttachedToWindow();
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            for (int i = 0; i < ReactionsEffectOverlay.this.avatars.size(); i++) {
                ReactionsEffectOverlay.this.avatars.get(i).imageReceiver.onDetachedFromWindow();
            }
        }
    }

    public void removeCurrentView() {
        try {
            if (this.useWindow) {
                this.windowManager.removeView(this.windowView);
            } else {
                this.decorView.removeView(this.windowView);
            }
        } catch (Exception unused) {
        }
    }

    public static void show(BaseFragment baseFragment, ReactionsContainerLayout reactionsContainerLayout, ChatMessageCell chatMessageCell, float f, float f2, String str, int i, int i2) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (chatMessageCell == null || str == null || baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        boolean z = true;
        if (!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            return;
        }
        if (i2 == 2 || i2 == 0) {
            show(baseFragment, null, chatMessageCell, 0.0f, 0.0f, str, i, 1);
        }
        ReactionsEffectOverlay reactionsEffectOverlay = new ReactionsEffectOverlay(baseFragment.getParentActivity(), baseFragment, reactionsContainerLayout, chatMessageCell, f, f2, str, i, i2);
        if (i2 == 1) {
            currentShortOverlay = reactionsEffectOverlay;
        } else {
            currentOverlay = reactionsEffectOverlay;
        }
        if (!(baseFragment instanceof ChatActivity) || (actionBarPopupWindow = ((ChatActivity) baseFragment).scrimPopupWindow) == null || !actionBarPopupWindow.isShowing()) {
            z = false;
        }
        reactionsEffectOverlay.useWindow = z;
        if (z) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.height = -1;
            layoutParams.width = -1;
            layoutParams.type = 1000;
            layoutParams.flags = 65816;
            layoutParams.format = -3;
            WindowManager windowManager = baseFragment.getParentActivity().getWindowManager();
            reactionsEffectOverlay.windowManager = windowManager;
            windowManager.addView(reactionsEffectOverlay.windowView, layoutParams);
        } else {
            FrameLayout frameLayout = (FrameLayout) baseFragment.getParentActivity().getWindow().getDecorView();
            reactionsEffectOverlay.decorView = frameLayout;
            frameLayout.addView(reactionsEffectOverlay.windowView);
        }
        chatMessageCell.invalidate();
        if (chatMessageCell.getCurrentMessagesGroup() == null || chatMessageCell.getParent() == null) {
            return;
        }
        ((View) chatMessageCell.getParent()).invalidate();
    }

    public static void startAnimation() {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            reactionsEffectOverlay.started = true;
            if (reactionsEffectOverlay.animationType != 0 || System.currentTimeMillis() - lastHapticTime <= 200) {
                return;
            }
            lastHapticTime = System.currentTimeMillis();
            currentOverlay.cell.performHapticFeedback(3);
            return;
        }
        startShortAnimation();
        ReactionsEffectOverlay reactionsEffectOverlay2 = currentShortOverlay;
        if (reactionsEffectOverlay2 == null) {
            return;
        }
        reactionsEffectOverlay2.cell.reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay2.reaction);
    }

    public static void startShortAnimation() {
        ReactionsEffectOverlay reactionsEffectOverlay = currentShortOverlay;
        if (reactionsEffectOverlay == null || reactionsEffectOverlay.started) {
            return;
        }
        reactionsEffectOverlay.started = true;
        if (reactionsEffectOverlay.animationType != 1 || System.currentTimeMillis() - lastHapticTime <= 200) {
            return;
        }
        lastHapticTime = System.currentTimeMillis();
        currentShortOverlay.cell.performHapticFeedback(3);
    }

    public static void removeCurrent(boolean z) {
        int i = 0;
        while (i < 2) {
            ReactionsEffectOverlay reactionsEffectOverlay = i == 0 ? currentOverlay : currentShortOverlay;
            if (reactionsEffectOverlay != null) {
                if (z) {
                    reactionsEffectOverlay.removeCurrentView();
                } else {
                    reactionsEffectOverlay.dismissed = true;
                }
            }
            i++;
        }
        currentShortOverlay = null;
        currentOverlay = null;
    }

    public static boolean isPlaying(int i, long j, String str) {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            int i2 = reactionsEffectOverlay.animationType;
            if (i2 != 2 && i2 != 0) {
                return false;
            }
            long j2 = reactionsEffectOverlay.groupId;
            return ((j2 != 0 && j == j2) || i == reactionsEffectOverlay.messageId) && reactionsEffectOverlay.reaction.equals(str);
        }
        return false;
    }

    /* loaded from: classes3.dex */
    public class AnimationView extends BackupImageView {
        boolean wasPlaying;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnimationView(Context context) {
            super(context);
            ReactionsEffectOverlay.this = r1;
        }

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onDraw(Canvas canvas) {
            if (getImageReceiver().getLottieAnimation() != null && getImageReceiver().getLottieAnimation().isRunning()) {
                this.wasPlaying = true;
            }
            if (!this.wasPlaying && getImageReceiver().getLottieAnimation() != null && !getImageReceiver().getLottieAnimation().isRunning()) {
                if (ReactionsEffectOverlay.this.animationType == 2) {
                    getImageReceiver().getLottieAnimation().setCurrentFrame(getImageReceiver().getLottieAnimation().getFramesCount() - 1, false);
                } else {
                    getImageReceiver().getLottieAnimation().setCurrentFrame(0, false);
                    getImageReceiver().getLottieAnimation().start();
                }
            }
            super.onDraw(canvas);
        }
    }

    public static void onScrolled(int i) {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            reactionsEffectOverlay.lastDrawnToY -= i;
            if (i == 0) {
                return;
            }
            reactionsEffectOverlay.wasScrolled = true;
        }
    }

    public static int sizeForBigReaction() {
        int dp = AndroidUtilities.dp(350.0f);
        Point point = AndroidUtilities.displaySize;
        return (int) (Math.round(Math.min(dp, Math.min(point.x, point.y)) * 0.7f) / AndroidUtilities.density);
    }

    /* loaded from: classes3.dex */
    public class AvatarParticle {
        float currentRotation;
        float fromX;
        float fromY;
        float globalTranslationY;
        ImageReceiver imageReceiver;
        boolean incrementRotation;
        float jumpY;
        public int leftTime;
        float outProgress;
        float progress;
        float randomRotation;
        float randomScale;
        float toX;
        float toY;

        private AvatarParticle(ReactionsEffectOverlay reactionsEffectOverlay) {
        }

        /* synthetic */ AvatarParticle(ReactionsEffectOverlay reactionsEffectOverlay, AnonymousClass1 anonymousClass1) {
            this(reactionsEffectOverlay);
        }
    }
}
