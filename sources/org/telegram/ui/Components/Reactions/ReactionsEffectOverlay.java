package org.telegram.ui.Components.Reactions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Random;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.Reactions.ReactionsEffectOverlay;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.ReactionsContainerLayout;
import org.telegram.ui.SelectAnimatedEmojiDialog;

/* loaded from: classes3.dex */
public class ReactionsEffectOverlay {
    public static ReactionsEffectOverlay currentOverlay;
    public static ReactionsEffectOverlay currentShortOverlay;
    private static long lastHapticTime;
    private static int uniqPrefix;
    float animateInProgress;
    float animateOutProgress;
    private final int animationType;
    private ChatMessageCell cell;
    private final FrameLayout container;
    private final int currentAccount;
    private ViewGroup decorView;
    private float dismissProgress;
    private boolean dismissed;
    private final AnimationView effectImageView;
    private final AnimationView emojiImageView;
    private final AnimationView emojiStaticImageView;
    private final BaseFragment fragment;
    private final long groupId;
    private ReactionsContainerLayout.ReactionHolderView holderView;
    boolean isFinished;
    public boolean isStories;
    private float lastDrawnToX;
    private float lastDrawnToY;
    private final int messageId;
    private ReactionsEffectOverlay nextReactionOverlay;
    private final ReactionsLayoutInBubble.VisibleReaction reaction;
    public long startTime;
    public boolean started;
    private boolean useWindow;
    private boolean wasScrolled;
    private WindowManager windowManager;
    public FrameLayout windowView;
    int[] loc = new int[2];
    private SelectAnimatedEmojiDialog.ImageViewEmoji holderView2 = null;
    ArrayList avatars = new ArrayList();

    class 1 extends FrameLayout {
        final /* synthetic */ int val$animationType;
        final /* synthetic */ ChatMessageCell val$cell;
        final /* synthetic */ ChatActivity val$chatActivity;
        final /* synthetic */ int val$emojiSize;
        final /* synthetic */ BaseFragment val$fragment;
        final /* synthetic */ boolean val$fromHolder;
        final /* synthetic */ float val$fromScale;
        final /* synthetic */ float val$fromX;
        final /* synthetic */ float val$fromY;
        final /* synthetic */ boolean val$isStories;
        final /* synthetic */ ReactionsLayoutInBubble.VisibleReaction val$visibleReaction;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        1(Context context, BaseFragment baseFragment, ChatMessageCell chatMessageCell, boolean z, ChatActivity chatActivity, int i, int i2, boolean z2, float f, float f2, float f3, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
            super(context);
            this.val$fragment = baseFragment;
            this.val$cell = chatMessageCell;
            this.val$isStories = z;
            this.val$chatActivity = chatActivity;
            this.val$emojiSize = i;
            this.val$animationType = i2;
            this.val$fromHolder = z2;
            this.val$fromScale = f;
            this.val$fromX = f2;
            this.val$fromY = f3;
            this.val$visibleReaction = visibleReaction;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dispatchDraw$0() {
            ReactionsEffectOverlay.this.removeCurrentView();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dispatchDraw$1() {
            ReactionsEffectOverlay.this.removeCurrentView();
        }

        /* JADX WARN: Code restructure failed: missing block: B:144:0x0538, code lost:
        
            if (((int) (r9 - ((r20.this$0.effectImageView.getImageReceiver().getLottieAnimation().getCurrentFrame() / r20.this$0.effectImageView.getImageReceiver().getLottieAnimation().getFramesCount()) * r9))) < r7.leftTime) goto L225;
         */
        /* JADX WARN: Removed duplicated region for block: B:100:0x02f6  */
        /* JADX WARN: Removed duplicated region for block: B:104:0x0323  */
        /* JADX WARN: Removed duplicated region for block: B:134:0x04c1  */
        /* JADX WARN: Removed duplicated region for block: B:139:0x04e4  */
        /* JADX WARN: Removed duplicated region for block: B:148:0x056b  */
        /* JADX WARN: Removed duplicated region for block: B:151:0x0634  */
        /* JADX WARN: Removed duplicated region for block: B:156:0x0641  */
        /* JADX WARN: Removed duplicated region for block: B:159:0x065f  */
        /* JADX WARN: Removed duplicated region for block: B:166:0x0673  */
        /* JADX WARN: Removed duplicated region for block: B:170:0x0655  */
        /* JADX WARN: Removed duplicated region for block: B:171:0x056e  */
        /* JADX WARN: Removed duplicated region for block: B:183:0x03cb  */
        /* JADX WARN: Removed duplicated region for block: B:230:0x0308  */
        /* JADX WARN: Removed duplicated region for block: B:238:0x0279  */
        /* JADX WARN: Removed duplicated region for block: B:241:0x021c  */
        /* JADX WARN: Removed duplicated region for block: B:242:0x01ee  */
        /* JADX WARN: Removed duplicated region for block: B:248:0x0188 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:249:0x010e  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x00c1  */
        /* JADX WARN: Removed duplicated region for block: B:69:0x01b5  */
        /* JADX WARN: Removed duplicated region for block: B:72:0x01c5  */
        /* JADX WARN: Removed duplicated region for block: B:75:0x01e3  */
        /* JADX WARN: Removed duplicated region for block: B:78:0x0219  */
        /* JADX WARN: Removed duplicated region for block: B:81:0x025f  */
        /* JADX WARN: Removed duplicated region for block: B:85:0x0293  */
        /* JADX WARN: Removed duplicated region for block: B:95:0x02ba  */
        @Override // android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void dispatchDraw(Canvas canvas) {
            float f;
            int dp;
            float f2;
            float f3;
            BaseFragment baseFragment;
            float f4;
            float f5;
            float f6;
            int i;
            float f7;
            float f8;
            float f9;
            float f10;
            ChatMessageCell chatMessageCell;
            int i2;
            int i3;
            float f11;
            float f12;
            if (ReactionsEffectOverlay.this.dismissed) {
                if (ReactionsEffectOverlay.this.dismissProgress != 1.0f) {
                    ReactionsEffectOverlay.access$216(ReactionsEffectOverlay.this, 0.10666667f);
                    if (ReactionsEffectOverlay.this.dismissProgress > 1.0f) {
                        ReactionsEffectOverlay.this.dismissProgress = 1.0f;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                ReactionsEffectOverlay.1.this.lambda$dispatchDraw$0();
                            }
                        });
                    }
                }
                if (ReactionsEffectOverlay.this.dismissProgress != 1.0f) {
                    setAlpha(1.0f - ReactionsEffectOverlay.this.dismissProgress);
                    super.dispatchDraw(canvas);
                }
                invalidate();
                return;
            }
            ReactionsEffectOverlay reactionsEffectOverlay = ReactionsEffectOverlay.this;
            if (!reactionsEffectOverlay.started) {
                invalidate();
                return;
            }
            if (reactionsEffectOverlay.holderView != null) {
                ReactionsEffectOverlay.this.holderView.enterImageView.setAlpha(0.0f);
                ReactionsEffectOverlay.this.holderView.pressedBackupImageView.setAlpha(0.0f);
            }
            BaseFragment baseFragment2 = this.val$fragment;
            ChatMessageCell findMessageCell = baseFragment2 instanceof ChatActivity ? ((ChatActivity) baseFragment2).findMessageCell(ReactionsEffectOverlay.this.messageId, false) : this.val$cell;
            if (this.val$isStories) {
                f = SharedConfig.deviceIsHigh() ? 120.0f : 50.0f;
            } else {
                ChatMessageCell chatMessageCell2 = this.val$cell;
                if (chatMessageCell2 != null && chatMessageCell2.getMessageObject().shouldDrawReactionsInLayout()) {
                    dp = AndroidUtilities.dp(20.0f);
                    float f13 = dp;
                    if (findMessageCell == null) {
                        this.val$cell.getLocationInWindow(ReactionsEffectOverlay.this.loc);
                        ReactionsLayoutInBubble.ReactionButton reactionButton = this.val$cell.getReactionButton(ReactionsEffectOverlay.this.reaction);
                        int[] iArr = ReactionsEffectOverlay.this.loc;
                        f2 = iArr[0];
                        f3 = iArr[1];
                        if (reactionButton != null) {
                            Rect rect = reactionButton.drawingImageRect;
                            f2 += rect.left;
                            f3 += rect.top;
                        }
                        ChatActivity chatActivity = this.val$chatActivity;
                        if (chatActivity != null) {
                            f3 += chatActivity.drawingChatListViewYoffset;
                        }
                        if (findMessageCell.drawPinnedBottom && !findMessageCell.shouldDrawTimeOnMedia()) {
                            f3 += AndroidUtilities.dp(2.0f);
                        }
                        ReactionsEffectOverlay.this.lastDrawnToX = f2;
                        ReactionsEffectOverlay.this.lastDrawnToY = f3;
                    } else if (this.val$isStories) {
                        float f14 = f13 / 2.0f;
                        f2 = (getMeasuredWidth() / 2.0f) - f14;
                        f3 = (getMeasuredHeight() / 2.0f) - f14;
                    } else {
                        f2 = ReactionsEffectOverlay.this.lastDrawnToX;
                        f3 = ReactionsEffectOverlay.this.lastDrawnToY;
                    }
                    baseFragment = this.val$fragment;
                    if (baseFragment == null && baseFragment.getParentActivity() != null && this.val$fragment.getFragmentView() != null && this.val$fragment.getFragmentView().getParent() != null && this.val$fragment.getFragmentView().getVisibility() == 0 && this.val$fragment.getFragmentView() != null) {
                        this.val$fragment.getFragmentView().getLocationOnScreen(ReactionsEffectOverlay.this.loc);
                        setAlpha(((View) this.val$fragment.getFragmentView().getParent()).getAlpha());
                    } else if (!this.val$isStories) {
                        return;
                    }
                    float f15 = (this.val$emojiSize - f13) / 2.0f;
                    f4 = f2 - f15;
                    float f16 = f3 - f15;
                    if (this.val$isStories && this.val$animationType == 0) {
                        f4 += AndroidUtilities.dp(40.0f);
                    }
                    if (this.val$animationType != 1 && !this.val$isStories) {
                        f12 = ReactionsEffectOverlay.this.loc[0];
                        if (f4 < f12) {
                            f4 = f12;
                        }
                        if (this.val$emojiSize + f4 > r14 + getMeasuredWidth()) {
                            f4 = (ReactionsEffectOverlay.this.loc[0] + getMeasuredWidth()) - this.val$emojiSize;
                        }
                    }
                    CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
                    float interpolation = cubicBezierInterpolator.getInterpolation(ReactionsEffectOverlay.this.animateOutProgress);
                    if (this.val$animationType != 2) {
                        f5 = CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(interpolation);
                        f6 = cubicBezierInterpolator.getInterpolation(interpolation);
                    } else if (this.val$fromHolder) {
                        f5 = CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(ReactionsEffectOverlay.this.animateInProgress);
                        f6 = cubicBezierInterpolator.getInterpolation(ReactionsEffectOverlay.this.animateInProgress);
                    } else {
                        f5 = ReactionsEffectOverlay.this.animateInProgress;
                        f6 = f5;
                    }
                    float f17 = 1.0f - f5;
                    float f18 = (this.val$fromScale * f17) + f5;
                    float f19 = f13 / this.val$emojiSize;
                    if (this.val$animationType != 1) {
                        f18 = 1.0f;
                    } else {
                        f4 = (f4 * f5) + (this.val$fromX * f17);
                        f16 = (f16 * f6) + (this.val$fromY * (1.0f - f6));
                    }
                    ReactionsEffectOverlay.this.effectImageView.setTranslationX(f4);
                    ReactionsEffectOverlay.this.effectImageView.setTranslationY(f16);
                    float f20 = 1.0f - interpolation;
                    ReactionsEffectOverlay.this.effectImageView.setAlpha(f20);
                    ReactionsEffectOverlay.this.effectImageView.setScaleX(f18);
                    ReactionsEffectOverlay.this.effectImageView.setScaleY(f18);
                    i = this.val$animationType;
                    if (i == 2) {
                        if (interpolation != 0.0f) {
                            f18 = (f18 * f20) + (f19 * interpolation);
                            f4 = (f4 * f20) + (f2 * interpolation);
                            f7 = f16 * f20;
                            f8 = f3 * interpolation;
                        }
                        if (i != 1) {
                            if (this.val$isStories) {
                                ReactionsEffectOverlay.this.emojiStaticImageView.setAlpha(1.0f);
                            } else {
                                ReactionsEffectOverlay.this.emojiStaticImageView.setAlpha(interpolation > 0.7f ? (interpolation - 0.7f) / 0.3f : 0.0f);
                            }
                        }
                        if (this.val$animationType == 0 && this.val$isStories) {
                            ReactionsEffectOverlay.this.emojiImageView.setAlpha(f20);
                        }
                        ReactionsEffectOverlay.this.container.setTranslationX(f4);
                        ReactionsEffectOverlay.this.container.setTranslationY(f16);
                        ReactionsEffectOverlay.this.container.setScaleX(f18);
                        ReactionsEffectOverlay.this.container.setScaleY(f18);
                        super.dispatchDraw(canvas);
                        if (this.val$animationType != 1 || ReactionsEffectOverlay.this.emojiImageView.wasPlaying) {
                            ReactionsEffectOverlay reactionsEffectOverlay2 = ReactionsEffectOverlay.this;
                            f9 = reactionsEffectOverlay2.animateInProgress;
                            if (f9 != 1.0f) {
                                reactionsEffectOverlay2.animateInProgress = this.val$fromHolder ? f9 + 0.045714285f : f9 + 0.07272727f;
                                if (reactionsEffectOverlay2.animateInProgress > 1.0f) {
                                    reactionsEffectOverlay2.animateInProgress = 1.0f;
                                }
                            }
                        }
                        float f21 = 16.0f;
                        if (this.val$animationType != 2 || ((ReactionsEffectOverlay.this.wasScrolled && this.val$animationType == 0) || ((this.val$animationType != 1 && ReactionsEffectOverlay.this.emojiImageView.wasPlaying && ReactionsEffectOverlay.this.emojiImageView.getImageReceiver().getLottieAnimation() != null && !ReactionsEffectOverlay.this.emojiImageView.getImageReceiver().getLottieAnimation().isRunning()) || ((this.val$visibleReaction.documentId != 0 && System.currentTimeMillis() - ReactionsEffectOverlay.this.startTime > 2000) || ((this.val$animationType == 1 && ReactionsEffectOverlay.this.effectImageView.wasPlaying && ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation() != null && !ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().isRunning()) || (this.val$visibleReaction.documentId != 0 && System.currentTimeMillis() - ReactionsEffectOverlay.this.startTime > 2000)))))) {
                            ReactionsEffectOverlay reactionsEffectOverlay3 = ReactionsEffectOverlay.this;
                            f10 = reactionsEffectOverlay3.animateOutProgress;
                            if (f10 != 1.0f) {
                                int i4 = this.val$animationType;
                                if (i4 == 1) {
                                    reactionsEffectOverlay3.animateOutProgress = 1.0f;
                                } else {
                                    reactionsEffectOverlay3.animateOutProgress = f10 + (16.0f / (i4 == 2 ? 350.0f : 220.0f));
                                }
                                if (reactionsEffectOverlay3.animateOutProgress > 0.7f) {
                                    if (!this.val$isStories || i4 != 2) {
                                        ReactionsEffectOverlay.startShortAnimation();
                                    } else if (!reactionsEffectOverlay3.isFinished) {
                                        reactionsEffectOverlay3.isFinished = true;
                                        try {
                                            performHapticFeedback(0);
                                        } catch (Exception unused) {
                                        }
                                        ((ViewGroup) getParent()).addView(ReactionsEffectOverlay.this.nextReactionOverlay.windowView);
                                        ReactionsEffectOverlay.this.nextReactionOverlay.isStories = true;
                                        ReactionsEffectOverlay.this.nextReactionOverlay.started = true;
                                        ReactionsEffectOverlay.this.nextReactionOverlay.startTime = System.currentTimeMillis();
                                        ReactionsEffectOverlay.this.nextReactionOverlay.windowView.setTag(R.id.parent_tag, 1);
                                        animate().scaleX(0.0f).scaleY(0.0f).setStartDelay(1000L).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay.1.1
                                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                            public void onAnimationEnd(Animator animator) {
                                                ReactionsEffectOverlay.this.removeCurrentView();
                                            }
                                        });
                                    }
                                }
                                ReactionsEffectOverlay reactionsEffectOverlay4 = ReactionsEffectOverlay.this;
                                if (reactionsEffectOverlay4.animateOutProgress >= 1.0f) {
                                    int i5 = this.val$animationType;
                                    if ((i5 == 0 || i5 == 2) && (chatMessageCell = this.val$cell) != null) {
                                        chatMessageCell.reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay4.reaction);
                                    }
                                    ReactionsEffectOverlay.this.animateOutProgress = 1.0f;
                                    if (this.val$animationType == 1) {
                                        ReactionsEffectOverlay.currentShortOverlay = null;
                                    } else {
                                        ReactionsEffectOverlay.currentOverlay = null;
                                    }
                                    ChatMessageCell chatMessageCell3 = this.val$cell;
                                    if (chatMessageCell3 != null) {
                                        chatMessageCell3.invalidate();
                                        if (this.val$cell.getCurrentMessagesGroup() != null && this.val$cell.getParent() != null) {
                                            ((View) this.val$cell.getParent()).invalidate();
                                        }
                                    }
                                    if (!this.val$isStories || this.val$animationType != 2) {
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.ReactionsEffectOverlay$1$$ExternalSyntheticLambda1
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                ReactionsEffectOverlay.1.this.lambda$dispatchDraw$1();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                        if (!ReactionsEffectOverlay.this.avatars.isEmpty() && ReactionsEffectOverlay.this.effectImageView.wasPlaying) {
                            RLottieDrawable lottieAnimation = ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation();
                            i2 = 0;
                            while (i2 < ReactionsEffectOverlay.this.avatars.size()) {
                                AvatarParticle avatarParticle = (AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i2);
                                float f22 = avatarParticle.progress;
                                if (lottieAnimation != null && lottieAnimation.isRunning()) {
                                    float duration = ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().getDuration();
                                }
                                float f23 = avatarParticle.outProgress;
                                if (f23 != 1.0f) {
                                    float f24 = f23 + 0.10666667f;
                                    avatarParticle.outProgress = f24;
                                    if (f24 > 1.0f) {
                                        avatarParticle.outProgress = 1.0f;
                                        ReactionsEffectOverlay.this.avatars.remove(i2);
                                        i2--;
                                        i3 = 1;
                                        i2 += i3;
                                        f21 = 16.0f;
                                    }
                                    float f25 = f22 >= 0.5f ? f22 / 0.5f : 1.0f - ((f22 - 0.5f) / 0.5f);
                                    float f26 = 1.0f - f22;
                                    float f27 = (avatarParticle.fromX * f26) + (avatarParticle.toX * f22);
                                    float f28 = ((avatarParticle.fromY * f26) + (avatarParticle.toY * f22)) - (avatarParticle.jumpY * f25);
                                    float f29 = avatarParticle.randomScale * f22 * (1.0f - avatarParticle.outProgress);
                                    float x = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f27);
                                    float y = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f28);
                                    int dp2 = AndroidUtilities.dp(f21);
                                    float f30 = dp2;
                                    float f31 = f30 / 2.0f;
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i2)).imageReceiver.setImageCoords(x - f31, y - f31, f30, f30);
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i2)).imageReceiver.setRoundRadius(dp2 >> 1);
                                    canvas.save();
                                    canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                    canvas.scale(f29, f29, x, y);
                                    canvas.rotate(avatarParticle.currentRotation, x, y);
                                    ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i2)).imageReceiver.draw(canvas);
                                    canvas.restore();
                                    f11 = avatarParticle.progress;
                                    if (f11 < 1.0f) {
                                        float f32 = f11 + 0.045714285f;
                                        avatarParticle.progress = f32;
                                        if (f32 > 1.0f) {
                                            avatarParticle.progress = 1.0f;
                                        }
                                    }
                                    if (f22 < 1.0f) {
                                        avatarParticle.globalTranslationY += (AndroidUtilities.dp(20.0f) * 16.0f) / 500.0f;
                                    }
                                    if (avatarParticle.incrementRotation) {
                                        float f33 = avatarParticle.currentRotation;
                                        float f34 = avatarParticle.randomRotation;
                                        float f35 = f33 - (f34 / 250.0f);
                                        avatarParticle.currentRotation = f35;
                                        if (f35 < (-f34)) {
                                            i3 = 1;
                                            avatarParticle.incrementRotation = true;
                                            i2 += i3;
                                            f21 = 16.0f;
                                        }
                                    } else {
                                        float f36 = avatarParticle.currentRotation;
                                        float f37 = avatarParticle.randomRotation;
                                        float f38 = f36 + (f37 / 250.0f);
                                        avatarParticle.currentRotation = f38;
                                        if (f38 > f37) {
                                            avatarParticle.incrementRotation = false;
                                        }
                                    }
                                    i3 = 1;
                                    i2 += i3;
                                    f21 = 16.0f;
                                }
                                if (f22 >= 0.5f) {
                                }
                                float f262 = 1.0f - f22;
                                float f272 = (avatarParticle.fromX * f262) + (avatarParticle.toX * f22);
                                float f282 = ((avatarParticle.fromY * f262) + (avatarParticle.toY * f22)) - (avatarParticle.jumpY * f25);
                                float f292 = avatarParticle.randomScale * f22 * (1.0f - avatarParticle.outProgress);
                                float x2 = ReactionsEffectOverlay.this.effectImageView.getX() + (ReactionsEffectOverlay.this.effectImageView.getWidth() * ReactionsEffectOverlay.this.effectImageView.getScaleX() * f272);
                                float y2 = ReactionsEffectOverlay.this.effectImageView.getY() + (ReactionsEffectOverlay.this.effectImageView.getHeight() * ReactionsEffectOverlay.this.effectImageView.getScaleY() * f282);
                                int dp22 = AndroidUtilities.dp(f21);
                                float f302 = dp22;
                                float f312 = f302 / 2.0f;
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i2)).imageReceiver.setImageCoords(x2 - f312, y2 - f312, f302, f302);
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i2)).imageReceiver.setRoundRadius(dp22 >> 1);
                                canvas.save();
                                canvas.translate(0.0f, avatarParticle.globalTranslationY);
                                canvas.scale(f292, f292, x2, y2);
                                canvas.rotate(avatarParticle.currentRotation, x2, y2);
                                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i2)).imageReceiver.draw(canvas);
                                canvas.restore();
                                f11 = avatarParticle.progress;
                                if (f11 < 1.0f) {
                                }
                                if (f22 < 1.0f) {
                                }
                                if (avatarParticle.incrementRotation) {
                                }
                                i3 = 1;
                                i2 += i3;
                                f21 = 16.0f;
                            }
                        }
                        invalidate();
                    }
                    f18 = (this.val$fromScale * f17) + (f19 * f5);
                    f4 = (this.val$fromX * f17) + (f2 * f5);
                    f7 = this.val$fromY * (1.0f - f6);
                    f8 = f3 * f6;
                    f16 = f7 + f8;
                    if (i != 1) {
                    }
                    if (this.val$animationType == 0) {
                        ReactionsEffectOverlay.this.emojiImageView.setAlpha(f20);
                    }
                    ReactionsEffectOverlay.this.container.setTranslationX(f4);
                    ReactionsEffectOverlay.this.container.setTranslationY(f16);
                    ReactionsEffectOverlay.this.container.setScaleX(f18);
                    ReactionsEffectOverlay.this.container.setScaleY(f18);
                    super.dispatchDraw(canvas);
                    if (this.val$animationType != 1) {
                    }
                    ReactionsEffectOverlay reactionsEffectOverlay22 = ReactionsEffectOverlay.this;
                    f9 = reactionsEffectOverlay22.animateInProgress;
                    if (f9 != 1.0f) {
                    }
                    float f212 = 16.0f;
                    if (this.val$animationType != 2) {
                    }
                    ReactionsEffectOverlay reactionsEffectOverlay32 = ReactionsEffectOverlay.this;
                    f10 = reactionsEffectOverlay32.animateOutProgress;
                    if (f10 != 1.0f) {
                    }
                    if (!ReactionsEffectOverlay.this.avatars.isEmpty()) {
                        RLottieDrawable lottieAnimation2 = ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation();
                        i2 = 0;
                        while (i2 < ReactionsEffectOverlay.this.avatars.size()) {
                        }
                    }
                    invalidate();
                }
                f = 14.0f;
            }
            dp = AndroidUtilities.dp(f);
            float f132 = dp;
            if (findMessageCell == null) {
            }
            baseFragment = this.val$fragment;
            if (baseFragment == null) {
            }
            if (!this.val$isStories) {
            }
            float f152 = (this.val$emojiSize - f132) / 2.0f;
            f4 = f2 - f152;
            float f162 = f3 - f152;
            if (this.val$isStories) {
                f4 += AndroidUtilities.dp(40.0f);
            }
            if (this.val$animationType != 1) {
                f12 = ReactionsEffectOverlay.this.loc[0];
                if (f4 < f12) {
                }
                if (this.val$emojiSize + f4 > r14 + getMeasuredWidth()) {
                }
            }
            CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.DEFAULT;
            float interpolation2 = cubicBezierInterpolator2.getInterpolation(ReactionsEffectOverlay.this.animateOutProgress);
            if (this.val$animationType != 2) {
            }
            float f172 = 1.0f - f5;
            float f182 = (this.val$fromScale * f172) + f5;
            float f192 = f132 / this.val$emojiSize;
            if (this.val$animationType != 1) {
            }
            ReactionsEffectOverlay.this.effectImageView.setTranslationX(f4);
            ReactionsEffectOverlay.this.effectImageView.setTranslationY(f162);
            float f202 = 1.0f - interpolation2;
            ReactionsEffectOverlay.this.effectImageView.setAlpha(f202);
            ReactionsEffectOverlay.this.effectImageView.setScaleX(f182);
            ReactionsEffectOverlay.this.effectImageView.setScaleY(f182);
            i = this.val$animationType;
            if (i == 2) {
            }
            f162 = f7 + f8;
            if (i != 1) {
            }
            if (this.val$animationType == 0) {
            }
            ReactionsEffectOverlay.this.container.setTranslationX(f4);
            ReactionsEffectOverlay.this.container.setTranslationY(f162);
            ReactionsEffectOverlay.this.container.setScaleX(f182);
            ReactionsEffectOverlay.this.container.setScaleY(f182);
            super.dispatchDraw(canvas);
            if (this.val$animationType != 1) {
            }
            ReactionsEffectOverlay reactionsEffectOverlay222 = ReactionsEffectOverlay.this;
            f9 = reactionsEffectOverlay222.animateInProgress;
            if (f9 != 1.0f) {
            }
            float f2122 = 16.0f;
            if (this.val$animationType != 2) {
            }
            ReactionsEffectOverlay reactionsEffectOverlay322 = ReactionsEffectOverlay.this;
            f10 = reactionsEffectOverlay322.animateOutProgress;
            if (f10 != 1.0f) {
            }
            if (!ReactionsEffectOverlay.this.avatars.isEmpty()) {
            }
            invalidate();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            for (int i = 0; i < ReactionsEffectOverlay.this.avatars.size(); i++) {
                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.onAttachedToWindow();
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            for (int i = 0; i < ReactionsEffectOverlay.this.avatars.size(); i++) {
                ((AvatarParticle) ReactionsEffectOverlay.this.avatars.get(i)).imageReceiver.onDetachedFromWindow();
            }
        }
    }

    private class AnimationView extends BackupImageView {
        AnimatedEmojiDrawable animatedEmojiDrawable;
        boolean attached;
        AnimatedEmojiEffect emojiEffect;
        boolean wasPlaying;

        public AnimationView(Context context) {
            super(context);
            getImageReceiver().setFileLoadingPriority(3);
        }

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.attached = true;
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.addView(this);
            }
            AnimatedEmojiEffect animatedEmojiEffect = this.emojiEffect;
            if (animatedEmojiEffect != null) {
                animatedEmojiEffect.setView(this);
            }
        }

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.attached = false;
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.removeView(this);
            }
            AnimatedEmojiEffect animatedEmojiEffect = this.emojiEffect;
            if (animatedEmojiEffect != null) {
                animatedEmojiEffect.removeView(this);
            }
        }

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        protected void onDraw(Canvas canvas) {
            AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                this.animatedEmojiDrawable.setAlpha(NotificationCenter.newLocationAvailable);
                this.animatedEmojiDrawable.draw(canvas);
            } else {
                AnimatedEmojiEffect animatedEmojiEffect = this.emojiEffect;
                if (animatedEmojiEffect == null) {
                    if (getImageReceiver().getLottieAnimation() != null && getImageReceiver().getLottieAnimation().isRunning()) {
                        this.wasPlaying = true;
                    }
                    if (!this.wasPlaying && getImageReceiver().getLottieAnimation() != null && !getImageReceiver().getLottieAnimation().isRunning()) {
                        if (ReactionsEffectOverlay.this.animationType != 2 || ReactionsEffectOverlay.this.isStories) {
                            getImageReceiver().getLottieAnimation().setCurrentFrame(0, false);
                            getImageReceiver().getLottieAnimation().start();
                        } else {
                            getImageReceiver().getLottieAnimation().setCurrentFrame(getImageReceiver().getLottieAnimation().getFramesCount() - 1, false);
                        }
                    }
                    super.onDraw(canvas);
                    return;
                }
                animatedEmojiEffect.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                this.emojiEffect.draw(canvas);
            }
            this.wasPlaying = true;
        }

        public void setAnimatedEmojiEffect(AnimatedEmojiEffect animatedEmojiEffect) {
            this.emojiEffect = animatedEmojiEffect;
        }

        public void setAnimatedReactionDrawable(AnimatedEmojiDrawable animatedEmojiDrawable) {
            if (animatedEmojiDrawable != null) {
                animatedEmojiDrawable.removeView(this);
            }
            this.animatedEmojiDrawable = animatedEmojiDrawable;
            if (!this.attached || animatedEmojiDrawable == null) {
                return;
            }
            animatedEmojiDrawable.addView(this);
        }
    }

    private class AvatarParticle {
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

        private AvatarParticle() {
        }

        /* synthetic */ AvatarParticle(ReactionsEffectOverlay reactionsEffectOverlay, 1 r2) {
            this();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:175:0x05e8  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x062c  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x062e  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x061f  */
    /* JADX WARN: Type inference failed for: r15v13 */
    /* JADX WARN: Type inference failed for: r15v15 */
    /* JADX WARN: Type inference failed for: r15v16 */
    /* JADX WARN: Type inference failed for: r15v4 */
    /* JADX WARN: Type inference failed for: r15v5, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r15v6 */
    /* JADX WARN: Type inference failed for: r15v7 */
    /* JADX WARN: Type inference failed for: r1v36, types: [org.telegram.messenger.ImageReceiver] */
    /* JADX WARN: Type inference failed for: r1v37, types: [org.telegram.messenger.ImageReceiver] */
    /* JADX WARN: Type inference failed for: r1v60, types: [org.telegram.ui.Components.RLottieDrawable] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ReactionsEffectOverlay(Context context, BaseFragment baseFragment, ReactionsContainerLayout reactionsContainerLayout, ChatMessageCell chatMessageCell, View view, float f, float f2, ReactionsLayoutInBubble.VisibleReaction visibleReaction, int i, int i2, boolean z) {
        long j;
        ReactionsLayoutInBubble.ReactionButton reactionButton;
        ChatMessageCell chatMessageCell2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        int round;
        int sizeForBigReaction;
        int i3;
        int i4;
        int i5;
        int i6;
        ?? r15;
        int i7;
        int i8;
        AnimatedEmojiDrawable animatedEmojiDrawable;
        int i9;
        ImageLocation forDocument;
        StringBuilder sb;
        boolean z2;
        String str;
        Random random;
        TLRPC.User user;
        this.holderView = null;
        this.fragment = baseFragment;
        this.isStories = z;
        if (chatMessageCell != null) {
            this.messageId = chatMessageCell.getMessageObject().getId();
            this.groupId = chatMessageCell.getMessageObject().getGroupId();
        } else {
            this.messageId = 0;
            this.groupId = 0L;
        }
        this.reaction = visibleReaction;
        this.animationType = i2;
        this.currentAccount = i;
        this.cell = chatMessageCell;
        ReactionsLayoutInBubble.ReactionButton reactionButton2 = chatMessageCell != null ? chatMessageCell.getReactionButton(visibleReaction) : null;
        if (z && i2 == 2) {
            j = 0;
            reactionButton = reactionButton2;
            chatMessageCell2 = chatMessageCell;
            ReactionsEffectOverlay reactionsEffectOverlay = new ReactionsEffectOverlay(context, baseFragment, reactionsContainerLayout, chatMessageCell, view, f, f2, visibleReaction, i, 1, true);
            this.nextReactionOverlay = reactionsEffectOverlay;
            currentShortOverlay = reactionsEffectOverlay;
        } else {
            j = 0;
            reactionButton = reactionButton2;
            chatMessageCell2 = chatMessageCell;
        }
        ChatActivity chatActivity = baseFragment instanceof ChatActivity ? (ChatActivity) baseFragment : null;
        if (reactionsContainerLayout != null) {
            int i10 = 0;
            while (true) {
                if (i10 < reactionsContainerLayout.recyclerListView.getChildCount()) {
                    if ((reactionsContainerLayout.recyclerListView.getChildAt(i10) instanceof ReactionsContainerLayout.ReactionHolderView) && ((ReactionsContainerLayout.ReactionHolderView) reactionsContainerLayout.recyclerListView.getChildAt(i10)).currentReaction.equals(this.reaction)) {
                        this.holderView = (ReactionsContainerLayout.ReactionHolderView) reactionsContainerLayout.recyclerListView.getChildAt(i10);
                        break;
                    }
                    i10++;
                } else {
                    break;
                }
            }
        }
        float f8 = 0.8f;
        if (i2 == 1) {
            Random random2 = new Random();
            ArrayList<TLRPC.MessagePeerReaction> arrayList = (chatMessageCell2 == null || chatMessageCell.getMessageObject().messageOwner.reactions == null) ? null : chatMessageCell.getMessageObject().messageOwner.reactions.recent_reactions;
            if (arrayList != null && chatActivity != null && chatActivity.getDialogId() < j) {
                int i11 = 0;
                while (i11 < arrayList.size()) {
                    if (this.reaction.equals(arrayList.get(i11).reaction) && arrayList.get(i11).unread) {
                        AvatarDrawable avatarDrawable = new AvatarDrawable();
                        ImageReceiver imageReceiver = new ImageReceiver();
                        long peerId = MessageObject.getPeerId(arrayList.get(i11).peer_id);
                        if (peerId < j) {
                            TLRPC.Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-peerId));
                            if (chat != null) {
                                avatarDrawable.setInfo(i, chat);
                                user = chat;
                            }
                        } else {
                            TLRPC.User user2 = MessagesController.getInstance(i).getUser(Long.valueOf(peerId));
                            if (user2 != null) {
                                avatarDrawable.setInfo(i, user2);
                                user = user2;
                            }
                            random = random2;
                            i11++;
                            random2 = random;
                            f8 = 0.8f;
                        }
                        imageReceiver.setForUserOrChat(user, avatarDrawable);
                        AvatarParticle avatarParticle = new AvatarParticle(this, null);
                        avatarParticle.imageReceiver = imageReceiver;
                        avatarParticle.fromX = 0.5f;
                        avatarParticle.fromY = 0.5f;
                        float f9 = 100.0f;
                        avatarParticle.jumpY = ((Math.abs(random2.nextInt() % 100) / 100.0f) * 0.1f) + 0.3f;
                        float f10 = 0.4f;
                        avatarParticle.randomScale = ((Math.abs(random2.nextInt() % 100) / 100.0f) * 0.4f) + f8;
                        avatarParticle.randomRotation = (Math.abs(random2.nextInt() % 100) * 60) / 100.0f;
                        avatarParticle.leftTime = (int) (((Math.abs(random2.nextInt() % 100) / 100.0f) * 200.0f) + 400.0f);
                        if (this.avatars.isEmpty()) {
                            avatarParticle.toX = ((Math.abs(random2.nextInt() % 100) * 0.6f) / 100.0f) + 0.2f;
                            avatarParticle.toY = (Math.abs(random2.nextInt() % 100) * 0.4f) / 100.0f;
                            random = random2;
                        } else {
                            float f11 = 0.0f;
                            int i12 = 0;
                            float f12 = 0.0f;
                            float f13 = 0.0f;
                            while (i12 < 10) {
                                float abs = ((Math.abs(random2.nextInt() % 100) * 0.6f) / f9) + 0.2f;
                                float abs2 = ((Math.abs(random2.nextInt() % 100) * f10) / 100.0f) + 0.2f;
                                float f14 = 2.14748365E9f;
                                Random random3 = random2;
                                int i13 = 0;
                                while (i13 < this.avatars.size()) {
                                    float f15 = ((AvatarParticle) this.avatars.get(i13)).toX - abs;
                                    float f16 = abs;
                                    float f17 = ((AvatarParticle) this.avatars.get(i13)).toY - abs2;
                                    float f18 = (f15 * f15) + (f17 * f17);
                                    if (f18 < f14) {
                                        f14 = f18;
                                    }
                                    i13++;
                                    abs = f16;
                                }
                                float f19 = abs;
                                if (f14 > f13) {
                                    f11 = abs2;
                                    f13 = f14;
                                    f12 = f19;
                                }
                                i12++;
                                random2 = random3;
                                f9 = 100.0f;
                                f10 = 0.4f;
                            }
                            random = random2;
                            avatarParticle.toX = f12;
                            avatarParticle.toY = f11;
                        }
                        this.avatars.add(avatarParticle);
                        i11++;
                        random2 = random;
                        f8 = 0.8f;
                    }
                    random = random2;
                    i11++;
                    random2 = random;
                    f8 = 0.8f;
                }
            }
        }
        ReactionsContainerLayout.ReactionHolderView reactionHolderView = this.holderView;
        boolean z3 = (reactionHolderView == null && (f == 0.0f || f2 == 0.0f)) ? false : true;
        if (view != null) {
            view.getLocationOnScreen(this.loc);
            int[] iArr = this.loc;
            float f20 = iArr[0];
            float f21 = iArr[1];
            f7 = view.getWidth() * view.getScaleX();
            if (view instanceof SelectAnimatedEmojiDialog.ImageViewEmoji) {
                float f22 = ((SelectAnimatedEmojiDialog.ImageViewEmoji) view).bigReactionSelectedProgress;
                if (f22 > 0.0f) {
                    f7 = view.getWidth() * ((f22 * 2.0f) + 1.0f);
                    f20 -= (f7 - view.getWidth()) / 2.0f;
                    f21 -= f7 - view.getWidth();
                }
            }
            f5 = f21;
            f6 = f20;
        } else if (reactionHolderView != null) {
            reactionHolderView.getLocationOnScreen(this.loc);
            f6 = this.loc[0] + this.holderView.loopImageView.getX();
            f5 = this.loc[1] + this.holderView.loopImageView.getY();
            f7 = this.holderView.loopImageView.getWidth() * this.holderView.getScaleX();
        } else {
            ReactionsLayoutInBubble.ReactionButton reactionButton3 = reactionButton;
            if (reactionButton3 != null) {
                chatMessageCell2.getLocationInWindow(this.loc);
                float f23 = this.loc[0];
                ImageReceiver imageReceiver2 = reactionButton3.imageReceiver;
                f3 = f23 + (imageReceiver2 == null ? 0.0f : imageReceiver2.getImageX());
                float f24 = this.loc[1];
                ImageReceiver imageReceiver3 = reactionButton3.imageReceiver;
                f4 = f24 + (imageReceiver3 == null ? 0.0f : imageReceiver3.getImageY());
                ImageReceiver imageReceiver4 = reactionButton3.imageReceiver;
                if (imageReceiver4 != null) {
                    f7 = imageReceiver4.getImageHeight();
                    f6 = f3;
                    f5 = f4;
                }
            } else if (chatMessageCell2 != null) {
                ((View) chatMessageCell.getParent()).getLocationInWindow(this.loc);
                int[] iArr2 = this.loc;
                f5 = iArr2[1] + f2;
                f6 = iArr2[0] + f;
                f7 = 0.0f;
            } else {
                f3 = f;
                f4 = f2;
            }
            f6 = f3;
            f5 = f4;
            f7 = 0.0f;
        }
        if (i2 == 2) {
            int dp = AndroidUtilities.dp((z && SharedConfig.deviceIsHigh()) ? 60.0f : 34.0f);
            i3 = dp;
            i4 = (int) ((dp * 2.0f) / AndroidUtilities.density);
        } else {
            if (i2 != 1) {
                int dp2 = AndroidUtilities.dp(350.0f);
                Point point = AndroidUtilities.displaySize;
                round = Math.round(Math.min(dp2, Math.min(point.x, point.y)) * 0.8f);
                sizeForBigReaction = sizeForBigReaction();
            } else if (z) {
                int dp3 = AndroidUtilities.dp(SharedConfig.deviceIsHigh() ? 240.0f : 140.0f);
                i4 = SharedConfig.deviceIsHigh() ? (int) ((AndroidUtilities.dp(80.0f) * 2.0f) / AndroidUtilities.density) : sizeForAroundReaction();
                i3 = dp3;
            } else {
                round = AndroidUtilities.dp(80.0f);
                sizeForBigReaction = sizeForAroundReaction();
            }
            i3 = round;
            i4 = sizeForBigReaction;
        }
        int i14 = i3 >> 1;
        int i15 = i4 >> 1;
        float f25 = f7 / i14;
        this.animateInProgress = 0.0f;
        this.animateOutProgress = 0.0f;
        FrameLayout frameLayout = new FrameLayout(context);
        this.container = frameLayout;
        int i16 = i4;
        int i17 = i3;
        this.windowView = new 1(context, baseFragment, chatMessageCell, z, chatActivity, i14, i2, z3, f25, f6, f5, visibleReaction);
        AnimationView animationView = new AnimationView(context);
        this.effectImageView = animationView;
        AnimationView animationView2 = new AnimationView(context);
        this.emojiImageView = animationView2;
        AnimationView animationView3 = new AnimationView(context);
        this.emojiStaticImageView = animationView3;
        TLRPC.TL_availableReaction tL_availableReaction = visibleReaction.emojicon != null ? MediaDataController.getInstance(i).getReactionsMap().get(this.reaction.emojicon) : null;
        if (tL_availableReaction == null && visibleReaction.documentId == j) {
            this.dismissed = true;
            return;
        }
        if (tL_availableReaction != null) {
            i5 = i2;
            i6 = 2;
            if (i5 != 2) {
                if ((i5 == 1 && LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_CHAT)) || i5 == 0) {
                    TLRPC.Document document = i5 == 1 ? tL_availableReaction.around_animation : tL_availableReaction.effect_animation;
                    if (i5 == 1) {
                        str = getFilterForAroundAnimation();
                    } else {
                        str = i16 + "_" + i16;
                    }
                    String str2 = str;
                    ImageReceiver imageReceiver5 = animationView.getImageReceiver();
                    StringBuilder sb2 = new StringBuilder();
                    int i18 = uniqPrefix;
                    uniqPrefix = i18 + 1;
                    sb2.append(i18);
                    sb2.append("_");
                    sb2.append(this.messageId);
                    sb2.append("_");
                    imageReceiver5.setUniqKeyPrefix(sb2.toString());
                    animationView.setImage(ImageLocation.getForDocument(document), str2, (ImageLocation) null, (String) null, 0, (Object) null);
                    z2 = false;
                    animationView.getImageReceiver().setAutoRepeat(0);
                    animationView.getImageReceiver().setAllowStartAnimation(false);
                } else {
                    z2 = false;
                }
                r15 = z2;
                if (animationView.getImageReceiver().getLottieAnimation() != null) {
                    animationView.getImageReceiver().getLottieAnimation().setCurrentFrame(z2 ? 1 : 0, z2);
                    animationView.getImageReceiver().getLottieAnimation().start();
                    r15 = z2;
                }
            } else {
                r15 = 0;
            }
            if (i5 == 2) {
                TLRPC.Document document2 = z ? tL_availableReaction.select_animation : tL_availableReaction.appear_animation;
                ImageReceiver imageReceiver6 = animationView2.getImageReceiver();
                StringBuilder sb3 = new StringBuilder();
                int i19 = uniqPrefix;
                uniqPrefix = i19 + 1;
                sb3.append(i19);
                sb3.append("_");
                sb3.append(this.messageId);
                sb3.append("_");
                imageReceiver6.setUniqKeyPrefix(sb3.toString());
                forDocument = ImageLocation.getForDocument(document2);
                sb = new StringBuilder();
                i9 = i15;
            } else {
                i9 = i15;
                if (i5 == 0) {
                    TLRPC.Document document3 = tL_availableReaction.activate_animation;
                    ImageReceiver imageReceiver7 = animationView2.getImageReceiver();
                    StringBuilder sb4 = new StringBuilder();
                    int i20 = uniqPrefix;
                    uniqPrefix = i20 + 1;
                    sb4.append(i20);
                    sb4.append("_");
                    sb4.append(this.messageId);
                    sb4.append("_");
                    imageReceiver7.setUniqKeyPrefix(sb4.toString());
                    forDocument = ImageLocation.getForDocument(document3);
                    sb = new StringBuilder();
                }
            }
            sb.append(i9);
            sb.append("_");
            sb.append(i9);
            animationView2.setImage(forDocument, sb.toString(), (ImageLocation) null, (String) null, 0, (Object) null);
        } else {
            i5 = i2;
            i6 = 2;
            r15 = 0;
            r15 = 0;
            if (i5 == 0) {
                i7 = i;
                animatedEmojiDrawable = new AnimatedEmojiDrawable(1, i7, visibleReaction.documentId);
            } else {
                i7 = i;
                animatedEmojiDrawable = i5 == 2 ? new AnimatedEmojiDrawable(2, i7, visibleReaction.documentId) : animatedEmojiDrawable;
                if (i5 != 0 || i5 == 1) {
                    AnimatedEmojiDrawable animatedEmojiDrawable2 = new AnimatedEmojiDrawable(2, i7, visibleReaction.documentId);
                    if (chatMessageCell == null) {
                        i8 = Theme.getColor(chatMessageCell.getMessageObject().shouldDrawWithoutBackground() ? chatMessageCell.getMessageObject().isOutOwner() ? Theme.key_chat_outReactionButtonBackground : Theme.key_chat_inReactionButtonBackground : chatMessageCell.getMessageObject().isOutOwner() ? Theme.key_chat_outReactionButtonTextSelected : Theme.key_chat_inReactionButtonTextSelected, baseFragment != null ? baseFragment.getResourceProvider() : null);
                    } else {
                        i8 = -1;
                    }
                    animatedEmojiDrawable2.setColorFilter(new PorterDuffColorFilter(i8, PorterDuff.Mode.SRC_IN));
                    boolean z4 = i5 != 0;
                    animationView.setAnimatedEmojiEffect(AnimatedEmojiEffect.createFrom(animatedEmojiDrawable2, z4, !z4));
                    this.windowView.setClipChildren(false);
                }
            }
            animationView2.setAnimatedReactionDrawable(animatedEmojiDrawable);
            if (i5 != 0) {
            }
            AnimatedEmojiDrawable animatedEmojiDrawable22 = new AnimatedEmojiDrawable(2, i7, visibleReaction.documentId);
            if (chatMessageCell == null) {
            }
            animatedEmojiDrawable22.setColorFilter(new PorterDuffColorFilter(i8, PorterDuff.Mode.SRC_IN));
            if (i5 != 0) {
            }
            animationView.setAnimatedEmojiEffect(AnimatedEmojiEffect.createFrom(animatedEmojiDrawable22, z4, !z4));
            this.windowView.setClipChildren(false);
        }
        animationView2.getImageReceiver().setAutoRepeat(r15);
        animationView2.getImageReceiver().setAllowStartAnimation(r15);
        if (animationView2.getImageReceiver().getLottieAnimation() != null) {
            ?? lottieAnimation = animationView2.getImageReceiver().getLottieAnimation();
            if (i5 == i6) {
                lottieAnimation.setCurrentFrame(animationView2.getImageReceiver().getLottieAnimation().getFramesCount() - 1, r15);
            } else {
                lottieAnimation.setCurrentFrame(r15, r15);
                animationView2.getImageReceiver().getLottieAnimation().start();
            }
        }
        int i21 = i17 - i14;
        int i22 = i21 >> 1;
        i21 = i5 == 1 ? i22 : i21;
        frameLayout.addView(animationView2);
        animationView2.getLayoutParams().width = i14;
        animationView2.getLayoutParams().height = i14;
        ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).topMargin = i22;
        ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).leftMargin = i21;
        if (i5 != 1 && !z) {
            if (tL_availableReaction != null) {
                animationView3.getImageReceiver().setImage(ImageLocation.getForDocument(tL_availableReaction.center_icon), "40_40_lastreactframe", null, "webp", tL_availableReaction, 1);
            }
            frameLayout.addView(animationView3);
            animationView3.getLayoutParams().width = i14;
            animationView3.getLayoutParams().height = i14;
            ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).topMargin = i22;
            ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).leftMargin = i21;
        }
        this.windowView.addView(frameLayout);
        frameLayout.getLayoutParams().width = i17;
        frameLayout.getLayoutParams().height = i17;
        int i23 = -i22;
        ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = i23;
        int i24 = -i21;
        ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).leftMargin = i24;
        this.windowView.addView(animationView);
        animationView.getLayoutParams().width = i17;
        animationView.getLayoutParams().height = i17;
        animationView.getLayoutParams().width = i17;
        animationView.getLayoutParams().height = i17;
        ((FrameLayout.LayoutParams) animationView.getLayoutParams()).topMargin = i23;
        ((FrameLayout.LayoutParams) animationView.getLayoutParams()).leftMargin = i24;
        frameLayout.setPivotX(i21);
        frameLayout.setPivotY(i22);
    }

    static /* synthetic */ float access$216(ReactionsEffectOverlay reactionsEffectOverlay, float f) {
        float f2 = reactionsEffectOverlay.dismissProgress + f;
        reactionsEffectOverlay.dismissProgress = f2;
        return f2;
    }

    public static void dismissAll() {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            reactionsEffectOverlay.dismissed = true;
        }
        ReactionsEffectOverlay reactionsEffectOverlay2 = currentShortOverlay;
        if (reactionsEffectOverlay2 != null) {
            reactionsEffectOverlay2.dismissed = true;
        }
    }

    public static String getFilterForAroundAnimation() {
        return sizeForAroundReaction() + "_" + sizeForAroundReaction() + "_nolimit_pcache";
    }

    public static boolean isPlaying(int i, long j, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay == null) {
            return false;
        }
        int i2 = reactionsEffectOverlay.animationType;
        if (i2 != 2 && i2 != 0) {
            return false;
        }
        long j2 = reactionsEffectOverlay.groupId;
        return ((j2 != 0 && j == j2) || i == reactionsEffectOverlay.messageId) && reactionsEffectOverlay.reaction.equals(visibleReaction);
    }

    public static void onScrolled(int i) {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            reactionsEffectOverlay.lastDrawnToY -= i;
            if (i != 0) {
                reactionsEffectOverlay.wasScrolled = true;
            }
        }
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

    /* JADX INFO: Access modifiers changed from: private */
    public void removeCurrentView() {
        try {
            if (this.useWindow) {
                this.windowManager.removeView(this.windowView);
            } else {
                AndroidUtilities.removeFromParent(this.windowView);
            }
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0069, code lost:
    
        if (r25 != 2) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0073, code lost:
    
        if (r0.isShowing() == false) goto L31;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void show(BaseFragment baseFragment, ReactionsContainerLayout reactionsContainerLayout, ChatMessageCell chatMessageCell, View view, float f, float f2, ReactionsLayoutInBubble.VisibleReaction visibleReaction, int i, int i2) {
        if (chatMessageCell == null || visibleReaction == null || baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        boolean z = true;
        if (MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            if (i2 == 2 || i2 == 0) {
                show(baseFragment, null, chatMessageCell, view, 0.0f, 0.0f, visibleReaction, i, 1);
            }
            ReactionsEffectOverlay reactionsEffectOverlay = new ReactionsEffectOverlay(baseFragment.getParentActivity(), baseFragment, reactionsContainerLayout, chatMessageCell, view, f, f2, visibleReaction, i, i2, false);
            if (i2 == 1) {
                currentShortOverlay = reactionsEffectOverlay;
            } else {
                currentOverlay = reactionsEffectOverlay;
            }
            if (baseFragment instanceof ChatActivity) {
                ChatActivity chatActivity = (ChatActivity) baseFragment;
                if (i2 != 0) {
                }
                ActionBarPopupWindow actionBarPopupWindow = chatActivity.scrimPopupWindow;
                if (actionBarPopupWindow != null) {
                }
            }
            z = false;
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
                AndroidUtilities.setPreferredMaxRefreshRate(windowManager, reactionsEffectOverlay.windowView, layoutParams);
                reactionsEffectOverlay.windowManager.addView(reactionsEffectOverlay.windowView, layoutParams);
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
    }

    public static int sizeForAroundReaction() {
        return (int) ((AndroidUtilities.dp(40.0f) * 2.0f) / AndroidUtilities.density);
    }

    public static int sizeForBigReaction() {
        int dp = AndroidUtilities.dp(350.0f);
        Point point = AndroidUtilities.displaySize;
        return (int) (Math.round(Math.min(dp, Math.min(point.x, point.y)) * 0.7f) / AndroidUtilities.density);
    }

    public static void startAnimation() {
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay == null) {
            startShortAnimation();
            ReactionsEffectOverlay reactionsEffectOverlay2 = currentShortOverlay;
            if (reactionsEffectOverlay2 != null) {
                reactionsEffectOverlay2.cell.reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay2.reaction);
                return;
            }
            return;
        }
        reactionsEffectOverlay.started = true;
        reactionsEffectOverlay.startTime = System.currentTimeMillis();
        if (currentOverlay.animationType != 0 || System.currentTimeMillis() - lastHapticTime <= 200) {
            return;
        }
        lastHapticTime = System.currentTimeMillis();
        currentOverlay.cell.performHapticFeedback(3);
    }

    public static void startShortAnimation() {
        ReactionsEffectOverlay reactionsEffectOverlay = currentShortOverlay;
        if (reactionsEffectOverlay == null || reactionsEffectOverlay.started) {
            return;
        }
        reactionsEffectOverlay.started = true;
        reactionsEffectOverlay.startTime = System.currentTimeMillis();
        if (currentShortOverlay.animationType != 1 || System.currentTimeMillis() - lastHapticTime <= 200) {
            return;
        }
        lastHapticTime = System.currentTimeMillis();
        currentShortOverlay.cell.performHapticFeedback(3);
    }
}
