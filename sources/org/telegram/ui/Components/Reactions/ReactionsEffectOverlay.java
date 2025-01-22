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
import org.telegram.ui.Cells.ChatActionCell;
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
    private View cell;
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
        final /* synthetic */ View val$cell;
        final /* synthetic */ ChatActivity val$chatActivity;
        final /* synthetic */ int val$emojiSize;
        final /* synthetic */ BaseFragment val$fragment;
        final /* synthetic */ boolean val$fromHolder;
        final /* synthetic */ float val$fromScale;
        final /* synthetic */ float val$fromX;
        final /* synthetic */ float val$fromY;
        final /* synthetic */ boolean val$isStories;
        final /* synthetic */ MessageObject val$messageObject;
        final /* synthetic */ ReactionsLayoutInBubble.VisibleReaction val$visibleReaction;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        1(Context context, BaseFragment baseFragment, View view, boolean z, MessageObject messageObject, ChatActivity chatActivity, int i, int i2, boolean z2, float f, float f2, float f3, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
            super(context);
            this.val$fragment = baseFragment;
            this.val$cell = view;
            this.val$isStories = z;
            this.val$messageObject = messageObject;
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

        /* JADX WARN: Code restructure failed: missing block: B:145:0x055a, code lost:
        
            if (((int) (r9 - ((r19.this$0.effectImageView.getImageReceiver().getLottieAnimation().getCurrentFrame() / r19.this$0.effectImageView.getImageReceiver().getLottieAnimation().getFramesCount()) * r9))) < r6.leftTime) goto L236;
         */
        /* JADX WARN: Removed duplicated region for block: B:101:0x0303  */
        /* JADX WARN: Removed duplicated region for block: B:105:0x0330  */
        /* JADX WARN: Removed duplicated region for block: B:135:0x04e3  */
        /* JADX WARN: Removed duplicated region for block: B:140:0x0506  */
        /* JADX WARN: Removed duplicated region for block: B:149:0x058d  */
        /* JADX WARN: Removed duplicated region for block: B:152:0x0656  */
        /* JADX WARN: Removed duplicated region for block: B:157:0x0663  */
        /* JADX WARN: Removed duplicated region for block: B:160:0x0681  */
        /* JADX WARN: Removed duplicated region for block: B:167:0x0695  */
        /* JADX WARN: Removed duplicated region for block: B:171:0x0677  */
        /* JADX WARN: Removed duplicated region for block: B:172:0x0590  */
        /* JADX WARN: Removed duplicated region for block: B:184:0x03d8  */
        /* JADX WARN: Removed duplicated region for block: B:237:0x0315  */
        /* JADX WARN: Removed duplicated region for block: B:245:0x0286  */
        /* JADX WARN: Removed duplicated region for block: B:248:0x0229  */
        /* JADX WARN: Removed duplicated region for block: B:249:0x01fb  */
        /* JADX WARN: Removed duplicated region for block: B:255:0x0195 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:260:0x011b  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x00bd  */
        /* JADX WARN: Removed duplicated region for block: B:70:0x01c2  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x01d2  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x01f0  */
        /* JADX WARN: Removed duplicated region for block: B:79:0x0226  */
        /* JADX WARN: Removed duplicated region for block: B:82:0x026c  */
        /* JADX WARN: Removed duplicated region for block: B:86:0x02a0  */
        /* JADX WARN: Removed duplicated region for block: B:96:0x02c7  */
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
            ReactionsLayoutInBubble reactionsLayoutInBubble;
            int i2;
            int i3;
            float f11;
            float f12;
            ReactionsLayoutInBubble.ReactionButton reactionButton;
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
            View findCell = baseFragment2 instanceof ChatActivity ? ((ChatActivity) baseFragment2).findCell(ReactionsEffectOverlay.this.messageId, false) : this.val$cell;
            if (this.val$isStories) {
                f = SharedConfig.deviceIsHigh() ? 120.0f : 50.0f;
            } else {
                MessageObject messageObject = this.val$messageObject;
                if (messageObject != null && messageObject.shouldDrawReactionsInLayout()) {
                    dp = AndroidUtilities.dp(20.0f);
                    float f13 = dp;
                    if (findCell == null) {
                        findCell.getLocationInWindow(ReactionsEffectOverlay.this.loc);
                        ReactionsEffectOverlay reactionsEffectOverlay2 = ReactionsEffectOverlay.this;
                        int[] iArr = reactionsEffectOverlay2.loc;
                        f2 = iArr[0];
                        f3 = iArr[1];
                        if (findCell instanceof ChatMessageCell) {
                            ChatMessageCell chatMessageCell = (ChatMessageCell) findCell;
                            reactionButton = chatMessageCell.getReactionButton(reactionsEffectOverlay2.reaction);
                            if (chatMessageCell.drawPinnedBottom && !chatMessageCell.shouldDrawTimeOnMedia()) {
                                f3 += AndroidUtilities.dp(2.0f);
                            }
                        } else {
                            reactionButton = findCell instanceof ChatActionCell ? ((ChatActionCell) findCell).getReactionButton(reactionsEffectOverlay2.reaction) : null;
                        }
                        if (reactionButton != null) {
                            Rect rect = reactionButton.drawingImageRect;
                            f2 += rect.left;
                            f3 += rect.top;
                        }
                        ChatActivity chatActivity = this.val$chatActivity;
                        if (chatActivity != null) {
                            f3 += chatActivity.drawingChatListViewYoffset;
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
                        if (this.val$emojiSize + f4 > r15 + getMeasuredWidth()) {
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
                            ReactionsEffectOverlay reactionsEffectOverlay3 = ReactionsEffectOverlay.this;
                            f9 = reactionsEffectOverlay3.animateInProgress;
                            if (f9 != 1.0f) {
                                reactionsEffectOverlay3.animateInProgress = this.val$fromHolder ? f9 + 0.045714285f : f9 + 0.07272727f;
                                if (reactionsEffectOverlay3.animateInProgress > 1.0f) {
                                    reactionsEffectOverlay3.animateInProgress = 1.0f;
                                }
                            }
                        }
                        float f21 = 16.0f;
                        if (this.val$animationType != 2 || ((ReactionsEffectOverlay.this.wasScrolled && this.val$animationType == 0) || ((this.val$animationType != 1 && ReactionsEffectOverlay.this.emojiImageView.wasPlaying && ReactionsEffectOverlay.this.emojiImageView.getImageReceiver().getLottieAnimation() != null && !ReactionsEffectOverlay.this.emojiImageView.getImageReceiver().getLottieAnimation().isRunning()) || ((this.val$visibleReaction.documentId != 0 && System.currentTimeMillis() - ReactionsEffectOverlay.this.startTime > 2000) || ((this.val$animationType == 1 && ReactionsEffectOverlay.this.effectImageView.wasPlaying && ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation() != null && !ReactionsEffectOverlay.this.effectImageView.getImageReceiver().getLottieAnimation().isRunning()) || (this.val$visibleReaction.documentId != 0 && System.currentTimeMillis() - ReactionsEffectOverlay.this.startTime > 2000)))))) {
                            ReactionsEffectOverlay reactionsEffectOverlay4 = ReactionsEffectOverlay.this;
                            f10 = reactionsEffectOverlay4.animateOutProgress;
                            if (f10 != 1.0f) {
                                int i4 = this.val$animationType;
                                if (i4 == 1) {
                                    reactionsEffectOverlay4.animateOutProgress = 1.0f;
                                } else {
                                    reactionsEffectOverlay4.animateOutProgress = f10 + (16.0f / (i4 == 2 ? 350.0f : 220.0f));
                                }
                                if (reactionsEffectOverlay4.animateOutProgress > 0.7f) {
                                    if (!this.val$isStories || i4 != 2) {
                                        ReactionsEffectOverlay.startShortAnimation();
                                    } else if (!reactionsEffectOverlay4.isFinished) {
                                        reactionsEffectOverlay4.isFinished = true;
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
                                ReactionsEffectOverlay reactionsEffectOverlay5 = ReactionsEffectOverlay.this;
                                if (reactionsEffectOverlay5.animateOutProgress >= 1.0f) {
                                    int i5 = this.val$animationType;
                                    if (i5 == 0 || i5 == 2) {
                                        View view = this.val$cell;
                                        if (view instanceof ChatMessageCell) {
                                            reactionsLayoutInBubble = ((ChatMessageCell) view).reactionsLayoutInBubble;
                                        } else if (view instanceof ChatActionCell) {
                                            reactionsLayoutInBubble = ((ChatActionCell) view).reactionsLayoutInBubble;
                                        }
                                        reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay5.reaction);
                                    }
                                    ReactionsEffectOverlay.this.animateOutProgress = 1.0f;
                                    if (this.val$animationType == 1) {
                                        ReactionsEffectOverlay.currentShortOverlay = null;
                                    } else {
                                        ReactionsEffectOverlay.currentOverlay = null;
                                    }
                                    View view2 = this.val$cell;
                                    if (view2 != null) {
                                        view2.invalidate();
                                        View view3 = this.val$cell;
                                        if ((view3 instanceof ChatMessageCell) && ((ChatMessageCell) view3).getCurrentMessagesGroup() != null && this.val$cell.getParent() != null) {
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
                    ReactionsEffectOverlay reactionsEffectOverlay32 = ReactionsEffectOverlay.this;
                    f9 = reactionsEffectOverlay32.animateInProgress;
                    if (f9 != 1.0f) {
                    }
                    float f212 = 16.0f;
                    if (this.val$animationType != 2) {
                    }
                    ReactionsEffectOverlay reactionsEffectOverlay42 = ReactionsEffectOverlay.this;
                    f10 = reactionsEffectOverlay42.animateOutProgress;
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
            if (findCell == null) {
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
                if (this.val$emojiSize + f4 > r15 + getMeasuredWidth()) {
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
            ReactionsEffectOverlay reactionsEffectOverlay322 = ReactionsEffectOverlay.this;
            f9 = reactionsEffectOverlay322.animateInProgress;
            if (f9 != 1.0f) {
            }
            float f2122 = 16.0f;
            if (this.val$animationType != 2) {
            }
            ReactionsEffectOverlay reactionsEffectOverlay422 = ReactionsEffectOverlay.this;
            f10 = reactionsEffectOverlay422.animateOutProgress;
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
    /* JADX WARN: Removed duplicated region for block: B:105:0x03cf  */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x04b4  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x04cd  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x04dc  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x068e  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x06cd  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x06f2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:157:0x06f6  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x05fb  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00ca  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x04c9  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x03ec  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x032c  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x02c4  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x02cc  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x02df  */
    /* JADX WARN: Type inference failed for: r14v13 */
    /* JADX WARN: Type inference failed for: r14v14, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r14v15 */
    /* JADX WARN: Type inference failed for: r3v32 */
    /* JADX WARN: Type inference failed for: r3v38, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r3v56 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ReactionsEffectOverlay(Context context, BaseFragment baseFragment, ReactionsContainerLayout reactionsContainerLayout, View view, View view2, float f, float f2, ReactionsLayoutInBubble.VisibleReaction visibleReaction, int i, int i2, boolean z) {
        MessageObject messageObject;
        MessageObject messageObject2;
        ReactionsLayoutInBubble.ReactionButton reactionButton;
        ReactionsLayoutInBubble.ReactionButton reactionButton2;
        long j;
        ReactionsLayoutInBubble.ReactionButton reactionButton3;
        MessageObject messageObject3;
        View view3;
        MessageObject messageObject4;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        int i3;
        int round;
        int sizeForBigReaction;
        int i4;
        int i5;
        int i6;
        FrameLayout frameLayout;
        AnimationView animationView;
        AnimationView animationView2;
        TLRPC.TL_availableReaction tL_availableReaction;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        ?? r3;
        int i12;
        int i13;
        int i14;
        ImageLocation forDocument;
        StringBuilder sb;
        ?? r14;
        String str;
        Random random;
        ArrayList<TLRPC.MessagePeerReaction> arrayList;
        TLRPC.TL_messageReactions tL_messageReactions;
        this.holderView = null;
        this.fragment = baseFragment;
        this.isStories = z;
        boolean z2 = view instanceof ChatMessageCell;
        if (z2) {
            messageObject2 = ((ChatMessageCell) view).getMessageObject();
            this.messageId = messageObject2.getId();
            this.groupId = messageObject2.getGroupId();
        } else {
            if (!(view instanceof ChatActionCell)) {
                this.messageId = 0;
                this.groupId = 0L;
                messageObject = null;
                this.reaction = visibleReaction;
                this.animationType = i2;
                this.currentAccount = i;
                this.cell = view;
                if (!z2) {
                    reactionButton2 = ((ChatMessageCell) view).getReactionButton(visibleReaction);
                } else {
                    if (!(view instanceof ChatActionCell)) {
                        reactionButton = null;
                        if (z || i2 != 2) {
                            j = 0;
                            reactionButton3 = reactionButton;
                            messageObject3 = messageObject;
                            view3 = view2;
                        } else {
                            j = 0;
                            reactionButton3 = reactionButton;
                            messageObject3 = messageObject;
                            view3 = view2;
                            ReactionsEffectOverlay reactionsEffectOverlay = new ReactionsEffectOverlay(context, baseFragment, reactionsContainerLayout, view, view2, f, f2, visibleReaction, i, 1, true);
                            this.nextReactionOverlay = reactionsEffectOverlay;
                            currentShortOverlay = reactionsEffectOverlay;
                        }
                        ChatActivity chatActivity = baseFragment instanceof ChatActivity ? (ChatActivity) baseFragment : null;
                        if (reactionsContainerLayout != null) {
                            int i15 = 0;
                            while (true) {
                                if (i15 < reactionsContainerLayout.recyclerListView.getChildCount()) {
                                    if ((reactionsContainerLayout.recyclerListView.getChildAt(i15) instanceof ReactionsContainerLayout.ReactionHolderView) && ((ReactionsContainerLayout.ReactionHolderView) reactionsContainerLayout.recyclerListView.getChildAt(i15)).currentReaction.equals(this.reaction)) {
                                        this.holderView = (ReactionsContainerLayout.ReactionHolderView) reactionsContainerLayout.recyclerListView.getChildAt(i15);
                                        break;
                                    }
                                    i15++;
                                } else {
                                    break;
                                }
                            }
                        }
                        float f8 = 0.8f;
                        if (i2 == 1) {
                            Random random2 = new Random();
                            messageObject4 = messageObject3;
                            ArrayList<TLRPC.MessagePeerReaction> arrayList2 = (messageObject4 == null || (tL_messageReactions = messageObject4.messageOwner.reactions) == null) ? null : tL_messageReactions.recent_reactions;
                            if (arrayList2 != null && chatActivity != null && chatActivity.getDialogId() < j) {
                                int i16 = 0;
                                while (i16 < arrayList2.size()) {
                                    if (this.reaction.equals(arrayList2.get(i16).reaction) && arrayList2.get(i16).unread) {
                                        AvatarDrawable avatarDrawable = new AvatarDrawable();
                                        ImageReceiver imageReceiver = new ImageReceiver();
                                        long peerId = MessageObject.getPeerId(arrayList2.get(i16).peer_id);
                                        if (peerId < j) {
                                            TLRPC.Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-peerId));
                                            if (chat != null) {
                                                avatarDrawable.setInfo(i, chat);
                                                imageReceiver.setForUserOrChat(chat, avatarDrawable);
                                            }
                                        } else {
                                            TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(peerId));
                                            if (user != null) {
                                                avatarDrawable.setInfo(i, user);
                                                imageReceiver.setForUserOrChat(user, avatarDrawable);
                                            }
                                            random = random2;
                                            arrayList = arrayList2;
                                            i16++;
                                            random2 = random;
                                            arrayList2 = arrayList;
                                            f8 = 0.8f;
                                        }
                                        AvatarParticle avatarParticle = new AvatarParticle(this, null);
                                        avatarParticle.imageReceiver = imageReceiver;
                                        avatarParticle.fromX = 0.5f;
                                        avatarParticle.fromY = 0.5f;
                                        float f9 = 100.0f;
                                        avatarParticle.jumpY = ((Math.abs(random2.nextInt() % 100) / 100.0f) * 0.1f) + 0.3f;
                                        avatarParticle.randomScale = ((Math.abs(random2.nextInt() % 100) / 100.0f) * 0.4f) + f8;
                                        avatarParticle.randomRotation = (Math.abs(random2.nextInt() % 100) * 60) / 100.0f;
                                        avatarParticle.leftTime = (int) (((Math.abs(random2.nextInt() % 100) / 100.0f) * 200.0f) + 400.0f);
                                        if (this.avatars.isEmpty()) {
                                            avatarParticle.toX = ((Math.abs(random2.nextInt() % 100) * 0.6f) / 100.0f) + 0.2f;
                                            avatarParticle.toY = (Math.abs(random2.nextInt() % 100) * 0.4f) / 100.0f;
                                            random = random2;
                                            arrayList = arrayList2;
                                        } else {
                                            float f10 = 0.0f;
                                            int i17 = 0;
                                            float f11 = 0.0f;
                                            float f12 = 0.0f;
                                            while (i17 < 10) {
                                                float abs = ((Math.abs(random2.nextInt() % 100) * 0.6f) / f9) + 0.2f;
                                                float abs2 = ((Math.abs(random2.nextInt() % 100) * 0.4f) / 100.0f) + 0.2f;
                                                float f13 = 2.14748365E9f;
                                                Random random3 = random2;
                                                ArrayList<TLRPC.MessagePeerReaction> arrayList3 = arrayList2;
                                                int i18 = 0;
                                                while (i18 < this.avatars.size()) {
                                                    float f14 = ((AvatarParticle) this.avatars.get(i18)).toX - abs;
                                                    float f15 = abs;
                                                    float f16 = ((AvatarParticle) this.avatars.get(i18)).toY - abs2;
                                                    float f17 = (f14 * f14) + (f16 * f16);
                                                    if (f17 < f13) {
                                                        f13 = f17;
                                                    }
                                                    i18++;
                                                    abs = f15;
                                                }
                                                float f18 = abs;
                                                if (f13 > f12) {
                                                    f10 = abs2;
                                                    f12 = f13;
                                                    f11 = f18;
                                                }
                                                i17++;
                                                random2 = random3;
                                                arrayList2 = arrayList3;
                                                f9 = 100.0f;
                                            }
                                            random = random2;
                                            arrayList = arrayList2;
                                            avatarParticle.toX = f11;
                                            avatarParticle.toY = f10;
                                        }
                                        this.avatars.add(avatarParticle);
                                        i16++;
                                        random2 = random;
                                        arrayList2 = arrayList;
                                        f8 = 0.8f;
                                    }
                                    random = random2;
                                    arrayList = arrayList2;
                                    i16++;
                                    random2 = random;
                                    arrayList2 = arrayList;
                                    f8 = 0.8f;
                                }
                            }
                        } else {
                            messageObject4 = messageObject3;
                        }
                        ReactionsContainerLayout.ReactionHolderView reactionHolderView = this.holderView;
                        boolean z3 = reactionHolderView == null || !(f == 0.0f || f2 == 0.0f);
                        if (view3 != null) {
                            view3.getLocationOnScreen(this.loc);
                            int[] iArr = this.loc;
                            float f19 = iArr[0];
                            float f20 = iArr[1];
                            f7 = view2.getWidth() * view2.getScaleX();
                            if (view3 instanceof SelectAnimatedEmojiDialog.ImageViewEmoji) {
                                float f21 = ((SelectAnimatedEmojiDialog.ImageViewEmoji) view3).bigReactionSelectedProgress;
                                if (f21 > 0.0f) {
                                    f7 = view2.getWidth() * ((f21 * 2.0f) + 1.0f);
                                    f19 -= (f7 - view2.getWidth()) / 2.0f;
                                    f20 -= f7 - view2.getWidth();
                                }
                            }
                            f5 = f20;
                            f6 = f19;
                        } else if (reactionHolderView != null) {
                            reactionHolderView.getLocationOnScreen(this.loc);
                            float x = this.loc[0] + this.holderView.loopImageView.getX();
                            float y = this.loc[1] + this.holderView.loopImageView.getY();
                            f7 = this.holderView.loopImageView.getWidth() * this.holderView.getScaleX();
                            f6 = x;
                            f5 = y;
                        } else {
                            ReactionsLayoutInBubble.ReactionButton reactionButton4 = reactionButton3;
                            if (reactionButton4 != null) {
                                view.getLocationInWindow(this.loc);
                                float f22 = this.loc[0];
                                ImageReceiver imageReceiver2 = reactionButton4.imageReceiver;
                                f3 = f22 + (imageReceiver2 == null ? 0.0f : imageReceiver2.getImageX());
                                float f23 = this.loc[1];
                                ImageReceiver imageReceiver3 = reactionButton4.imageReceiver;
                                f4 = f23 + (imageReceiver3 == null ? 0.0f : imageReceiver3.getImageY());
                                ImageReceiver imageReceiver4 = reactionButton4.imageReceiver;
                                if (imageReceiver4 != null) {
                                    f6 = f3;
                                    f5 = f4;
                                    i3 = 2;
                                    f7 = imageReceiver4.getImageHeight();
                                    if (i2 != i3) {
                                        int dp = AndroidUtilities.dp((z && SharedConfig.deviceIsHigh()) ? 60.0f : 34.0f);
                                        i4 = dp;
                                        i5 = (int) ((dp * 2.0f) / AndroidUtilities.density);
                                    } else {
                                        if (i2 != 1) {
                                            int dp2 = AndroidUtilities.dp(350.0f);
                                            Point point = AndroidUtilities.displaySize;
                                            round = Math.round(Math.min(dp2, Math.min(point.x, point.y)) * 0.8f);
                                            sizeForBigReaction = sizeForBigReaction();
                                        } else if (z) {
                                            int dp3 = AndroidUtilities.dp(SharedConfig.deviceIsHigh() ? 240.0f : 140.0f);
                                            sizeForBigReaction = SharedConfig.deviceIsHigh() ? (int) ((AndroidUtilities.dp(80.0f) * 2.0f) / AndroidUtilities.density) : sizeForAroundReaction();
                                            i4 = dp3;
                                            i5 = sizeForBigReaction;
                                        } else {
                                            round = AndroidUtilities.dp(80.0f);
                                            sizeForBigReaction = sizeForAroundReaction();
                                        }
                                        i4 = round;
                                        i5 = sizeForBigReaction;
                                    }
                                    i6 = i4 >> 1;
                                    int i19 = i5 >> 1;
                                    this.animateInProgress = 0.0f;
                                    this.animateOutProgress = 0.0f;
                                    frameLayout = new FrameLayout(context);
                                    this.container = frameLayout;
                                    int i20 = i5;
                                    int i21 = i4;
                                    MessageObject messageObject5 = messageObject4;
                                    this.windowView = new 1(context, baseFragment, view, z, messageObject4, chatActivity, i6, i2, z3, f7 / i6, f6, f5, visibleReaction);
                                    animationView = new AnimationView(context);
                                    this.effectImageView = animationView;
                                    animationView2 = new AnimationView(context);
                                    this.emojiImageView = animationView2;
                                    AnimationView animationView3 = new AnimationView(context);
                                    this.emojiStaticImageView = animationView3;
                                    tL_availableReaction = visibleReaction.emojicon == null ? MediaDataController.getInstance(i).getReactionsMap().get(this.reaction.emojicon) : null;
                                    if (tL_availableReaction != null && visibleReaction.documentId == j) {
                                        this.dismissed = true;
                                        return;
                                    }
                                    if (tL_availableReaction == null) {
                                        i7 = i2;
                                        int i22 = 2;
                                        if (i7 != 2) {
                                            if ((i7 == 1 && LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_CHAT)) || i7 == 0) {
                                                TLRPC.Document document = i7 == 1 ? tL_availableReaction.around_animation : tL_availableReaction.effect_animation;
                                                if (i7 == 1) {
                                                    str = getFilterForAroundAnimation();
                                                } else {
                                                    str = i20 + "_" + i20;
                                                }
                                                String str2 = str;
                                                ImageReceiver imageReceiver5 = animationView.getImageReceiver();
                                                StringBuilder sb2 = new StringBuilder();
                                                int i23 = uniqPrefix;
                                                uniqPrefix = i23 + 1;
                                                sb2.append(i23);
                                                sb2.append("_");
                                                sb2.append(this.messageId);
                                                sb2.append("_");
                                                imageReceiver5.setUniqKeyPrefix(sb2.toString());
                                                animationView.setImage(ImageLocation.getForDocument(document), str2, (ImageLocation) null, (String) null, 0, (Object) null);
                                                r14 = 0;
                                                animationView.getImageReceiver().setAutoRepeat(0);
                                                animationView.getImageReceiver().setAllowStartAnimation(false);
                                            } else {
                                                r14 = 0;
                                            }
                                            if (animationView.getImageReceiver().getLottieAnimation() != null) {
                                                animationView.getImageReceiver().getLottieAnimation().setCurrentFrame(r14, r14);
                                                animationView.getImageReceiver().getLottieAnimation().start();
                                            }
                                            i22 = 2;
                                        }
                                        if (i7 == i22) {
                                            TLRPC.Document document2 = z ? tL_availableReaction.select_animation : tL_availableReaction.appear_animation;
                                            ImageReceiver imageReceiver6 = animationView2.getImageReceiver();
                                            StringBuilder sb3 = new StringBuilder();
                                            int i24 = uniqPrefix;
                                            uniqPrefix = i24 + 1;
                                            sb3.append(i24);
                                            sb3.append("_");
                                            sb3.append(this.messageId);
                                            sb3.append("_");
                                            imageReceiver6.setUniqKeyPrefix(sb3.toString());
                                            forDocument = ImageLocation.getForDocument(document2);
                                            sb = new StringBuilder();
                                            i14 = i19;
                                        } else {
                                            i14 = i19;
                                            if (i7 == 0) {
                                                TLRPC.Document document3 = tL_availableReaction.activate_animation;
                                                ImageReceiver imageReceiver7 = animationView2.getImageReceiver();
                                                StringBuilder sb4 = new StringBuilder();
                                                int i25 = uniqPrefix;
                                                uniqPrefix = i25 + 1;
                                                sb4.append(i25);
                                                sb4.append("_");
                                                sb4.append(this.messageId);
                                                sb4.append("_");
                                                imageReceiver7.setUniqKeyPrefix(sb4.toString());
                                                forDocument = ImageLocation.getForDocument(document3);
                                                sb = new StringBuilder();
                                            }
                                        }
                                        sb.append(i14);
                                        sb.append("_");
                                        sb.append(i14);
                                        animationView2.setImage(forDocument, sb.toString(), (ImageLocation) null, (String) null, 0, (Object) null);
                                    } else {
                                        i7 = i2;
                                        if (i7 == 0) {
                                            i8 = i;
                                            i10 = 1;
                                            animationView2.setAnimatedReactionDrawable(new AnimatedEmojiDrawable(1, i8, visibleReaction.documentId));
                                            i9 = 2;
                                        } else {
                                            i8 = i;
                                            i9 = 2;
                                            i10 = 1;
                                            if (i7 == 2) {
                                                animationView2.setAnimatedReactionDrawable(new AnimatedEmojiDrawable(2, i8, visibleReaction.documentId));
                                            }
                                        }
                                        if (i7 == 0 || i7 == i10) {
                                            AnimatedEmojiDrawable animatedEmojiDrawable = new AnimatedEmojiDrawable(i9, i8, visibleReaction.documentId);
                                            if (messageObject5 != null) {
                                                i11 = Theme.getColor(messageObject5.shouldDrawWithoutBackground() ? messageObject5.isOutOwner() ? Theme.key_chat_outReactionButtonBackground : Theme.key_chat_inReactionButtonBackground : messageObject5.isOutOwner() ? Theme.key_chat_outReactionButtonTextSelected : Theme.key_chat_inReactionButtonTextSelected, baseFragment != null ? baseFragment.getResourceProvider() : null);
                                            } else {
                                                i11 = -1;
                                            }
                                            animatedEmojiDrawable.setColorFilter(new PorterDuffColorFilter(i11, PorterDuff.Mode.SRC_IN));
                                            boolean z4 = i7 == 0;
                                            animationView.setAnimatedEmojiEffect(AnimatedEmojiEffect.createFrom(animatedEmojiDrawable, z4, !z4));
                                            r3 = 0;
                                            this.windowView.setClipChildren(false);
                                            animationView2.getImageReceiver().setAutoRepeat(r3);
                                            animationView2.getImageReceiver().setAllowStartAnimation(r3);
                                            if (animationView2.getImageReceiver().getLottieAnimation() != null) {
                                                if (i7 == 2) {
                                                    animationView2.getImageReceiver().getLottieAnimation().setCurrentFrame(animationView2.getImageReceiver().getLottieAnimation().getFramesCount() - 1, r3);
                                                } else {
                                                    animationView2.getImageReceiver().getLottieAnimation().setCurrentFrame(r3, r3);
                                                    animationView2.getImageReceiver().getLottieAnimation().start();
                                                }
                                            }
                                            i12 = i21 - i6;
                                            i13 = i12 >> 1;
                                            i12 = i7 == 1 ? i13 : i12;
                                            frameLayout.addView(animationView2);
                                            animationView2.getLayoutParams().width = i6;
                                            animationView2.getLayoutParams().height = i6;
                                            ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).topMargin = i13;
                                            ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).leftMargin = i12;
                                            if (i7 != 1 && !z) {
                                                if (tL_availableReaction != null) {
                                                    animationView3.getImageReceiver().setImage(ImageLocation.getForDocument(tL_availableReaction.center_icon), "40_40_lastreactframe", null, "webp", tL_availableReaction, 1);
                                                }
                                                frameLayout.addView(animationView3);
                                                animationView3.getLayoutParams().width = i6;
                                                animationView3.getLayoutParams().height = i6;
                                                ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).topMargin = i13;
                                                ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).leftMargin = i12;
                                            }
                                            this.windowView.addView(frameLayout);
                                            frameLayout.getLayoutParams().width = i21;
                                            frameLayout.getLayoutParams().height = i21;
                                            int i26 = -i13;
                                            ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = i26;
                                            int i27 = -i12;
                                            ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).leftMargin = i27;
                                            this.windowView.addView(animationView);
                                            animationView.getLayoutParams().width = i21;
                                            animationView.getLayoutParams().height = i21;
                                            animationView.getLayoutParams().width = i21;
                                            animationView.getLayoutParams().height = i21;
                                            ((FrameLayout.LayoutParams) animationView.getLayoutParams()).topMargin = i26;
                                            ((FrameLayout.LayoutParams) animationView.getLayoutParams()).leftMargin = i27;
                                            frameLayout.setPivotX(i12);
                                            frameLayout.setPivotY(i13);
                                        }
                                    }
                                    r3 = 0;
                                    animationView2.getImageReceiver().setAutoRepeat(r3);
                                    animationView2.getImageReceiver().setAllowStartAnimation(r3);
                                    if (animationView2.getImageReceiver().getLottieAnimation() != null) {
                                    }
                                    i12 = i21 - i6;
                                    i13 = i12 >> 1;
                                    if (i7 == 1) {
                                    }
                                    frameLayout.addView(animationView2);
                                    animationView2.getLayoutParams().width = i6;
                                    animationView2.getLayoutParams().height = i6;
                                    ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).topMargin = i13;
                                    ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).leftMargin = i12;
                                    if (i7 != 1) {
                                        if (tL_availableReaction != null) {
                                        }
                                        frameLayout.addView(animationView3);
                                        animationView3.getLayoutParams().width = i6;
                                        animationView3.getLayoutParams().height = i6;
                                        ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).topMargin = i13;
                                        ((FrameLayout.LayoutParams) animationView3.getLayoutParams()).leftMargin = i12;
                                    }
                                    this.windowView.addView(frameLayout);
                                    frameLayout.getLayoutParams().width = i21;
                                    frameLayout.getLayoutParams().height = i21;
                                    int i262 = -i13;
                                    ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = i262;
                                    int i272 = -i12;
                                    ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).leftMargin = i272;
                                    this.windowView.addView(animationView);
                                    animationView.getLayoutParams().width = i21;
                                    animationView.getLayoutParams().height = i21;
                                    animationView.getLayoutParams().width = i21;
                                    animationView.getLayoutParams().height = i21;
                                    ((FrameLayout.LayoutParams) animationView.getLayoutParams()).topMargin = i262;
                                    ((FrameLayout.LayoutParams) animationView.getLayoutParams()).leftMargin = i272;
                                    frameLayout.setPivotX(i12);
                                    frameLayout.setPivotY(i13);
                                }
                            } else if (view != null) {
                                ((View) view.getParent()).getLocationInWindow(this.loc);
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
                        i3 = 2;
                        if (i2 != i3) {
                        }
                        i6 = i4 >> 1;
                        int i192 = i5 >> 1;
                        this.animateInProgress = 0.0f;
                        this.animateOutProgress = 0.0f;
                        frameLayout = new FrameLayout(context);
                        this.container = frameLayout;
                        int i202 = i5;
                        int i212 = i4;
                        MessageObject messageObject52 = messageObject4;
                        this.windowView = new 1(context, baseFragment, view, z, messageObject4, chatActivity, i6, i2, z3, f7 / i6, f6, f5, visibleReaction);
                        animationView = new AnimationView(context);
                        this.effectImageView = animationView;
                        animationView2 = new AnimationView(context);
                        this.emojiImageView = animationView2;
                        AnimationView animationView32 = new AnimationView(context);
                        this.emojiStaticImageView = animationView32;
                        if (visibleReaction.emojicon == null) {
                        }
                        if (tL_availableReaction != null) {
                        }
                        if (tL_availableReaction == null) {
                        }
                        r3 = 0;
                        animationView2.getImageReceiver().setAutoRepeat(r3);
                        animationView2.getImageReceiver().setAllowStartAnimation(r3);
                        if (animationView2.getImageReceiver().getLottieAnimation() != null) {
                        }
                        i12 = i212 - i6;
                        i13 = i12 >> 1;
                        if (i7 == 1) {
                        }
                        frameLayout.addView(animationView2);
                        animationView2.getLayoutParams().width = i6;
                        animationView2.getLayoutParams().height = i6;
                        ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).topMargin = i13;
                        ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).leftMargin = i12;
                        if (i7 != 1) {
                        }
                        this.windowView.addView(frameLayout);
                        frameLayout.getLayoutParams().width = i212;
                        frameLayout.getLayoutParams().height = i212;
                        int i2622 = -i13;
                        ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = i2622;
                        int i2722 = -i12;
                        ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).leftMargin = i2722;
                        this.windowView.addView(animationView);
                        animationView.getLayoutParams().width = i212;
                        animationView.getLayoutParams().height = i212;
                        animationView.getLayoutParams().width = i212;
                        animationView.getLayoutParams().height = i212;
                        ((FrameLayout.LayoutParams) animationView.getLayoutParams()).topMargin = i2622;
                        ((FrameLayout.LayoutParams) animationView.getLayoutParams()).leftMargin = i2722;
                        frameLayout.setPivotX(i12);
                        frameLayout.setPivotY(i13);
                    }
                    reactionButton2 = ((ChatActionCell) view).getReactionButton(visibleReaction);
                }
                reactionButton = reactionButton2;
                if (z) {
                }
                j = 0;
                reactionButton3 = reactionButton;
                messageObject3 = messageObject;
                view3 = view2;
                if (baseFragment instanceof ChatActivity) {
                }
                if (reactionsContainerLayout != null) {
                }
                float f82 = 0.8f;
                if (i2 == 1) {
                }
                ReactionsContainerLayout.ReactionHolderView reactionHolderView2 = this.holderView;
                if (reactionHolderView2 == null) {
                }
                if (view3 != null) {
                }
                i3 = 2;
                if (i2 != i3) {
                }
                i6 = i4 >> 1;
                int i1922 = i5 >> 1;
                this.animateInProgress = 0.0f;
                this.animateOutProgress = 0.0f;
                frameLayout = new FrameLayout(context);
                this.container = frameLayout;
                int i2022 = i5;
                int i2122 = i4;
                MessageObject messageObject522 = messageObject4;
                this.windowView = new 1(context, baseFragment, view, z, messageObject4, chatActivity, i6, i2, z3, f7 / i6, f6, f5, visibleReaction);
                animationView = new AnimationView(context);
                this.effectImageView = animationView;
                animationView2 = new AnimationView(context);
                this.emojiImageView = animationView2;
                AnimationView animationView322 = new AnimationView(context);
                this.emojiStaticImageView = animationView322;
                if (visibleReaction.emojicon == null) {
                }
                if (tL_availableReaction != null) {
                }
                if (tL_availableReaction == null) {
                }
                r3 = 0;
                animationView2.getImageReceiver().setAutoRepeat(r3);
                animationView2.getImageReceiver().setAllowStartAnimation(r3);
                if (animationView2.getImageReceiver().getLottieAnimation() != null) {
                }
                i12 = i2122 - i6;
                i13 = i12 >> 1;
                if (i7 == 1) {
                }
                frameLayout.addView(animationView2);
                animationView2.getLayoutParams().width = i6;
                animationView2.getLayoutParams().height = i6;
                ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).topMargin = i13;
                ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).leftMargin = i12;
                if (i7 != 1) {
                }
                this.windowView.addView(frameLayout);
                frameLayout.getLayoutParams().width = i2122;
                frameLayout.getLayoutParams().height = i2122;
                int i26222 = -i13;
                ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = i26222;
                int i27222 = -i12;
                ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).leftMargin = i27222;
                this.windowView.addView(animationView);
                animationView.getLayoutParams().width = i2122;
                animationView.getLayoutParams().height = i2122;
                animationView.getLayoutParams().width = i2122;
                animationView.getLayoutParams().height = i2122;
                ((FrameLayout.LayoutParams) animationView.getLayoutParams()).topMargin = i26222;
                ((FrameLayout.LayoutParams) animationView.getLayoutParams()).leftMargin = i27222;
                frameLayout.setPivotX(i12);
                frameLayout.setPivotY(i13);
            }
            messageObject2 = ((ChatActionCell) view).getMessageObject();
            this.messageId = messageObject2.getId();
            this.groupId = 0L;
        }
        messageObject = messageObject2;
        this.reaction = visibleReaction;
        this.animationType = i2;
        this.currentAccount = i;
        this.cell = view;
        if (!z2) {
        }
        reactionButton = reactionButton2;
        if (z) {
        }
        j = 0;
        reactionButton3 = reactionButton;
        messageObject3 = messageObject;
        view3 = view2;
        if (baseFragment instanceof ChatActivity) {
        }
        if (reactionsContainerLayout != null) {
        }
        float f822 = 0.8f;
        if (i2 == 1) {
        }
        ReactionsContainerLayout.ReactionHolderView reactionHolderView22 = this.holderView;
        if (reactionHolderView22 == null) {
        }
        if (view3 != null) {
        }
        i3 = 2;
        if (i2 != i3) {
        }
        i6 = i4 >> 1;
        int i19222 = i5 >> 1;
        this.animateInProgress = 0.0f;
        this.animateOutProgress = 0.0f;
        frameLayout = new FrameLayout(context);
        this.container = frameLayout;
        int i20222 = i5;
        int i21222 = i4;
        MessageObject messageObject5222 = messageObject4;
        this.windowView = new 1(context, baseFragment, view, z, messageObject4, chatActivity, i6, i2, z3, f7 / i6, f6, f5, visibleReaction);
        animationView = new AnimationView(context);
        this.effectImageView = animationView;
        animationView2 = new AnimationView(context);
        this.emojiImageView = animationView2;
        AnimationView animationView3222 = new AnimationView(context);
        this.emojiStaticImageView = animationView3222;
        if (visibleReaction.emojicon == null) {
        }
        if (tL_availableReaction != null) {
        }
        if (tL_availableReaction == null) {
        }
        r3 = 0;
        animationView2.getImageReceiver().setAutoRepeat(r3);
        animationView2.getImageReceiver().setAllowStartAnimation(r3);
        if (animationView2.getImageReceiver().getLottieAnimation() != null) {
        }
        i12 = i21222 - i6;
        i13 = i12 >> 1;
        if (i7 == 1) {
        }
        frameLayout.addView(animationView2);
        animationView2.getLayoutParams().width = i6;
        animationView2.getLayoutParams().height = i6;
        ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).topMargin = i13;
        ((FrameLayout.LayoutParams) animationView2.getLayoutParams()).leftMargin = i12;
        if (i7 != 1) {
        }
        this.windowView.addView(frameLayout);
        frameLayout.getLayoutParams().width = i21222;
        frameLayout.getLayoutParams().height = i21222;
        int i262222 = -i13;
        ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = i262222;
        int i272222 = -i12;
        ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).leftMargin = i272222;
        this.windowView.addView(animationView);
        animationView.getLayoutParams().width = i21222;
        animationView.getLayoutParams().height = i21222;
        animationView.getLayoutParams().width = i21222;
        animationView.getLayoutParams().height = i21222;
        ((FrameLayout.LayoutParams) animationView.getLayoutParams()).topMargin = i262222;
        ((FrameLayout.LayoutParams) animationView.getLayoutParams()).leftMargin = i272222;
        frameLayout.setPivotX(i12);
        frameLayout.setPivotY(i13);
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

    /* JADX WARN: Code restructure failed: missing block: B:22:0x006e, code lost:
    
        if (r26 != 2) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0078, code lost:
    
        if (r1.isShowing() == false) goto L31;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void show(BaseFragment baseFragment, ReactionsContainerLayout reactionsContainerLayout, View view, View view2, float f, float f2, ReactionsLayoutInBubble.VisibleReaction visibleReaction, int i, int i2) {
        if (view == null || visibleReaction == null || baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        boolean z = true;
        if (MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            if (i2 == 2 || i2 == 0) {
                show(baseFragment, null, view, view2, 0.0f, 0.0f, visibleReaction, i, 1);
            }
            ReactionsEffectOverlay reactionsEffectOverlay = new ReactionsEffectOverlay(baseFragment.getParentActivity(), baseFragment, reactionsContainerLayout, view, view2, f, f2, visibleReaction, i, i2, false);
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
                layoutParams.type = MediaDataController.MAX_STYLE_RUNS_COUNT;
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
            view.invalidate();
            if (!(view instanceof ChatMessageCell) || ((ChatMessageCell) view).getCurrentMessagesGroup() == null || view.getParent() == null) {
                return;
            }
            ((View) view.getParent()).invalidate();
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
        ReactionsLayoutInBubble reactionsLayoutInBubble;
        ReactionsEffectOverlay reactionsEffectOverlay = currentOverlay;
        if (reactionsEffectOverlay != null) {
            reactionsEffectOverlay.started = true;
            reactionsEffectOverlay.startTime = System.currentTimeMillis();
            if (currentOverlay.animationType != 0 || System.currentTimeMillis() - lastHapticTime <= 200) {
                return;
            }
            lastHapticTime = System.currentTimeMillis();
            currentOverlay.cell.performHapticFeedback(3);
            return;
        }
        startShortAnimation();
        ReactionsEffectOverlay reactionsEffectOverlay2 = currentShortOverlay;
        if (reactionsEffectOverlay2 != null) {
            View view = reactionsEffectOverlay2.cell;
            if (view instanceof ChatMessageCell) {
                reactionsLayoutInBubble = ((ChatMessageCell) view).reactionsLayoutInBubble;
            } else if (!(view instanceof ChatActionCell)) {
                return;
            } else {
                reactionsLayoutInBubble = ((ChatActionCell) view).reactionsLayoutInBubble;
            }
            reactionsLayoutInBubble.animateReaction(reactionsEffectOverlay2.reaction);
        }
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
