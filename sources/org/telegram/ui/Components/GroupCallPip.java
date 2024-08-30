package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.SystemClock;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.TLRPC$TL_groupCallParticipant;
import org.telegram.ui.Components.GroupCallPip;
import org.telegram.ui.Components.voip.RTMPStreamPipOverlay;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public class GroupCallPip implements NotificationCenter.NotificationCenterDelegate {
    private static boolean forceRemoved = true;
    private static GroupCallPip instance;
    FrameLayout alertContainer;
    boolean animateToPrepareRemove;
    boolean animateToShowRemoveTooltip;
    AvatarsImageView avatarsImageView;
    private final GroupCallPipButton button;
    boolean buttonInAlpha;
    int currentAccount;
    RLottieDrawable deleteIcon;
    private final RLottieImageView iconView;
    int lastScreenX;
    int lastScreenY;
    boolean moving;
    ValueAnimator pinAnimator;
    GroupCallPipAlertView pipAlertView;
    boolean pressedState;
    View removeTooltipView;
    boolean removed;
    boolean showAlert;
    AnimatorSet showRemoveAnimator;
    WindowManager.LayoutParams windowLayoutParams;
    int windowLeft;
    WindowManager windowManager;
    float windowOffsetLeft;
    float windowOffsetTop;
    FrameLayout windowRemoveTooltipOverlayView;
    FrameLayout windowRemoveTooltipView;
    int windowTop;
    FrameLayout windowView;
    float windowX;
    float windowY;
    float prepareToRemoveProgress = 0.0f;
    int[] location = new int[2];
    float[] point = new float[2];
    float xRelative = -1.0f;
    float yRelative = -1.0f;
    private ValueAnimator.AnimatorUpdateListener updateXlistener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.GroupCallPip.1
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            GroupCallPip groupCallPip = GroupCallPip.this;
            groupCallPip.windowLayoutParams.x = (int) floatValue;
            groupCallPip.updateAvatarsPosition();
            if (GroupCallPip.this.windowView.getParent() != null) {
                GroupCallPip groupCallPip2 = GroupCallPip.this;
                groupCallPip2.windowManager.updateViewLayout(groupCallPip2.windowView, groupCallPip2.windowLayoutParams);
            }
        }
    };
    private ValueAnimator.AnimatorUpdateListener updateYlistener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.GroupCallPip.2
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            GroupCallPip groupCallPip = GroupCallPip.this;
            groupCallPip.windowLayoutParams.y = (int) floatValue;
            if (groupCallPip.windowView.getParent() != null) {
                GroupCallPip groupCallPip2 = GroupCallPip.this;
                groupCallPip2.windowManager.updateViewLayout(groupCallPip2.windowView, groupCallPip2.windowLayoutParams);
            }
        }
    };
    boolean animateToPinnedToCenter = false;
    float pinnedProgress = 0.0f;

    /* loaded from: classes3.dex */
    class 3 extends FrameLayout {
        Runnable micRunnable;
        AnimatorSet moveToBoundsAnimator;
        boolean pressed;
        Runnable pressedRunnable;
        long startTime;
        float startX;
        float startY;
        final /* synthetic */ float val$touchSlop;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        3(Context context, float f) {
            super(context);
            this.val$touchSlop = f;
            this.pressedRunnable = new Runnable() { // from class: org.telegram.ui.Components.GroupCallPip.3.1
                @Override // java.lang.Runnable
                public void run() {
                    VoIPService sharedInstance = VoIPService.getSharedInstance();
                    if (sharedInstance == null || !sharedInstance.isMicMute()) {
                        return;
                    }
                    TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = (TLRPC$TL_groupCallParticipant) sharedInstance.groupCall.participants.get(sharedInstance.getSelfId());
                    if (tLRPC$TL_groupCallParticipant == null || tLRPC$TL_groupCallParticipant.can_self_unmute || !tLRPC$TL_groupCallParticipant.muted || ChatObject.canManageCalls(sharedInstance.getChat())) {
                        AndroidUtilities.runOnUIThread(3.this.micRunnable, 90L);
                        3.this.performHapticFeedback(3, 2);
                        3.this.pressed = true;
                    }
                }
            };
            this.micRunnable = new Runnable() { // from class: org.telegram.ui.Components.GroupCallPip$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCallPip.3.lambda$$0();
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$$0() {
            if (VoIPService.getSharedInstance() == null || !VoIPService.getSharedInstance().isMicMute()) {
                return;
            }
            VoIPService.getSharedInstance().setMicMute(false, true, false);
        }

        private void onTap() {
            if (VoIPService.getSharedInstance() != null) {
                GroupCallPip groupCallPip = GroupCallPip.this;
                groupCallPip.showAlert(!groupCallPip.showAlert);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            android.graphics.Point point = AndroidUtilities.displaySize;
            int i3 = point.x;
            GroupCallPip groupCallPip = GroupCallPip.this;
            if (i3 == groupCallPip.lastScreenX && groupCallPip.lastScreenY == point.y) {
                return;
            }
            groupCallPip.lastScreenX = i3;
            groupCallPip.lastScreenY = point.y;
            if (groupCallPip.xRelative < 0.0f) {
                SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("groupcallpipconfig", 0);
                GroupCallPip.this.xRelative = sharedPreferences.getFloat("relativeX", 1.0f);
                GroupCallPip.this.yRelative = sharedPreferences.getFloat("relativeY", 0.4f);
            }
            if (GroupCallPip.instance != null) {
                GroupCallPip groupCallPip2 = GroupCallPip.instance;
                GroupCallPip groupCallPip3 = GroupCallPip.this;
                groupCallPip2.setPosition(groupCallPip3.xRelative, groupCallPip3.yRelative);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0022, code lost:
            if (r6 != 3) goto L11;
         */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0064  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            int[] iArr;
            GroupCallPip groupCallPip;
            GroupCallPip groupCallPip2;
            GroupCallPip groupCallPip3;
            boolean z;
            boolean z2 = false;
            if (GroupCallPip.instance == null) {
                return false;
            }
            float rawX = motionEvent.getRawX();
            float rawY = motionEvent.getRawY();
            ViewParent parent = getParent();
            int action = motionEvent.getAction();
            if (action != 0) {
                float f = 0.0f;
                if (action != 1) {
                    if (action == 2) {
                        float f2 = rawX - this.startX;
                        float f3 = rawY - this.startY;
                        if (!GroupCallPip.this.moving) {
                            float f4 = (f2 * f2) + (f3 * f3);
                            float f5 = this.val$touchSlop;
                            if (f4 > f5 * f5) {
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                                AndroidUtilities.cancelRunOnUIThread(this.pressedRunnable);
                                GroupCallPip groupCallPip4 = GroupCallPip.this;
                                groupCallPip4.moving = true;
                                groupCallPip4.showRemoveTooltip(true);
                                GroupCallPip.this.showAlert(false);
                                this.startX = rawX;
                                this.startY = rawY;
                                f3 = 0.0f;
                                groupCallPip = GroupCallPip.this;
                                if (groupCallPip.moving) {
                                    groupCallPip.windowX += f;
                                    groupCallPip.windowY += f3;
                                    this.startX = rawX;
                                    this.startY = rawY;
                                    groupCallPip.updateButtonPosition();
                                    float measuredWidth = GroupCallPip.this.windowX + (getMeasuredWidth() / 2.0f);
                                    float measuredHeight = GroupCallPip.this.windowY + (getMeasuredHeight() / 2.0f);
                                    float measuredWidth2 = (groupCallPip2.windowLeft - GroupCallPip.this.windowOffsetLeft) + (groupCallPip2.windowRemoveTooltipView.getMeasuredWidth() / 2.0f);
                                    float measuredHeight2 = (groupCallPip3.windowTop - GroupCallPip.this.windowOffsetTop) + (groupCallPip3.windowRemoveTooltipView.getMeasuredHeight() / 2.0f);
                                    float f6 = measuredWidth - measuredWidth2;
                                    float f7 = measuredHeight - measuredHeight2;
                                    float f8 = (f6 * f6) + (f7 * f7);
                                    if (f8 < AndroidUtilities.dp(80.0f) * AndroidUtilities.dp(80.0f)) {
                                        GroupCallPip.this.button.setRemoveAngle((((measuredWidth <= measuredWidth2 || measuredHeight >= measuredHeight2) && (measuredWidth >= measuredWidth2 || measuredHeight >= measuredHeight2)) ? 90.0d : 270.0d) - Math.toDegrees(Math.atan(f6 / f7)));
                                        if (f8 < AndroidUtilities.dp(50.0f) * AndroidUtilities.dp(50.0f)) {
                                            z = true;
                                            z2 = true;
                                        } else {
                                            z = true;
                                        }
                                    } else {
                                        z = false;
                                    }
                                    GroupCallPip.this.pinnedToCenter(z2);
                                    GroupCallPip.this.prepareToRemove(z);
                                }
                            }
                        }
                        f = f2;
                        groupCallPip = GroupCallPip.this;
                        if (groupCallPip.moving) {
                        }
                    }
                }
                AndroidUtilities.cancelRunOnUIThread(this.micRunnable);
                AndroidUtilities.cancelRunOnUIThread(this.pressedRunnable);
                GroupCallPip groupCallPip5 = GroupCallPip.this;
                if (groupCallPip5.animateToPrepareRemove) {
                    if (this.pressed && VoIPService.getSharedInstance() != null) {
                        VoIPService.getSharedInstance().setMicMute(true, false, false);
                    }
                    this.pressed = false;
                    GroupCallPip.this.remove();
                    return false;
                }
                groupCallPip5.pressedState = false;
                groupCallPip5.checkButtonAlpha();
                if (this.pressed) {
                    if (VoIPService.getSharedInstance() != null) {
                        VoIPService.getSharedInstance().setMicMute(true, false, false);
                        performHapticFeedback(3, 2);
                    }
                    this.pressed = false;
                } else if (motionEvent.getAction() == 1 && !GroupCallPip.this.moving) {
                    onTap();
                    return false;
                }
                if (parent != null && GroupCallPip.this.moving) {
                    parent.requestDisallowInterceptTouchEvent(false);
                    android.graphics.Point point = AndroidUtilities.displaySize;
                    int i = point.x;
                    int i2 = point.y;
                    float f9 = GroupCallPip.this.windowLayoutParams.x;
                    float measuredWidth3 = getMeasuredWidth() + f9;
                    float f10 = GroupCallPip.this.windowLayoutParams.y;
                    float measuredHeight3 = getMeasuredHeight() + f10;
                    this.moveToBoundsAnimator = new AnimatorSet();
                    float f11 = -AndroidUtilities.dp(36.0f);
                    if (f9 < f11) {
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(GroupCallPip.this.windowLayoutParams.x, f11);
                        ofFloat.addUpdateListener(GroupCallPip.this.updateXlistener);
                        this.moveToBoundsAnimator.playTogether(ofFloat);
                        f9 = f11;
                    } else if (measuredWidth3 > i - f11) {
                        float measuredWidth4 = (i - getMeasuredWidth()) - f11;
                        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(GroupCallPip.this.windowLayoutParams.x, measuredWidth4);
                        ofFloat2.addUpdateListener(GroupCallPip.this.updateXlistener);
                        this.moveToBoundsAnimator.playTogether(ofFloat2);
                        f9 = measuredWidth4;
                    }
                    int dp = i2 + AndroidUtilities.dp(36.0f);
                    if (f10 < AndroidUtilities.statusBarHeight - AndroidUtilities.dp(36.0f)) {
                        f10 = AndroidUtilities.statusBarHeight - AndroidUtilities.dp(36.0f);
                        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(GroupCallPip.this.windowLayoutParams.y, f10);
                        ofFloat3.addUpdateListener(GroupCallPip.this.updateYlistener);
                        this.moveToBoundsAnimator.playTogether(ofFloat3);
                    } else if (measuredHeight3 > dp) {
                        f10 = dp - getMeasuredHeight();
                        ValueAnimator ofFloat4 = ValueAnimator.ofFloat(GroupCallPip.this.windowLayoutParams.y, f10);
                        ofFloat4.addUpdateListener(GroupCallPip.this.updateYlistener);
                        this.moveToBoundsAnimator.playTogether(ofFloat4);
                    }
                    this.moveToBoundsAnimator.setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT);
                    this.moveToBoundsAnimator.start();
                    GroupCallPip groupCallPip6 = GroupCallPip.this;
                    if (groupCallPip6.xRelative >= 0.0f) {
                        groupCallPip6.getRelativePosition(f9, f10, groupCallPip6.point);
                        SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("groupcallpipconfig", 0).edit();
                        GroupCallPip groupCallPip7 = GroupCallPip.this;
                        float f12 = groupCallPip7.point[0];
                        groupCallPip7.xRelative = f12;
                        SharedPreferences.Editor putFloat = edit.putFloat("relativeX", f12);
                        GroupCallPip groupCallPip8 = GroupCallPip.this;
                        float f13 = groupCallPip8.point[1];
                        groupCallPip8.yRelative = f13;
                        putFloat.putFloat("relativeY", f13).apply();
                    }
                }
                GroupCallPip groupCallPip9 = GroupCallPip.this;
                groupCallPip9.moving = false;
                groupCallPip9.showRemoveTooltip(false);
            } else {
                getLocationOnScreen(GroupCallPip.this.location);
                GroupCallPip groupCallPip10 = GroupCallPip.this;
                int i3 = groupCallPip10.location[0];
                WindowManager.LayoutParams layoutParams = groupCallPip10.windowLayoutParams;
                groupCallPip10.windowOffsetLeft = i3 - layoutParams.x;
                groupCallPip10.windowOffsetTop = iArr[1] - layoutParams.y;
                this.startX = rawX;
                this.startY = rawY;
                this.startTime = System.currentTimeMillis();
                AndroidUtilities.runOnUIThread(this.pressedRunnable, 300L);
                GroupCallPip groupCallPip11 = GroupCallPip.this;
                WindowManager.LayoutParams layoutParams2 = groupCallPip11.windowLayoutParams;
                groupCallPip11.windowX = layoutParams2.x;
                groupCallPip11.windowY = layoutParams2.y;
                groupCallPip11.pressedState = true;
                groupCallPip11.checkButtonAlpha();
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 9 extends AnimatorListenerAdapter {
        final /* synthetic */ View val$alert;
        final /* synthetic */ WindowManager val$windowManager;
        final /* synthetic */ View val$windowRemoveTooltipOverlayView;
        final /* synthetic */ View val$windowRemoveTooltipView;
        final /* synthetic */ View val$windowView;

        9(View view, View view2, WindowManager windowManager, View view3, View view4) {
            this.val$windowView = view;
            this.val$windowRemoveTooltipView = view2;
            this.val$windowManager = windowManager;
            this.val$windowRemoveTooltipOverlayView = view3;
            this.val$alert = view4;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onAnimationEnd$0(View view, View view2, WindowManager windowManager, View view3, View view4) {
            view.setVisibility(8);
            view2.setVisibility(8);
            windowManager.removeView(view);
            windowManager.removeView(view2);
            windowManager.removeView(view3);
            windowManager.removeView(view4);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            NotificationCenter notificationCenter = NotificationCenter.getInstance(GroupCallPip.this.currentAccount);
            final View view = this.val$windowView;
            final View view2 = this.val$windowRemoveTooltipView;
            final WindowManager windowManager = this.val$windowManager;
            final View view3 = this.val$windowRemoveTooltipOverlayView;
            final View view4 = this.val$alert;
            notificationCenter.doOnIdle(new Runnable() { // from class: org.telegram.ui.Components.GroupCallPip$9$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCallPip.9.lambda$onAnimationEnd$0(view, view2, windowManager, view3, view4);
                }
            });
        }
    }

    public GroupCallPip(Context context, int i) {
        this.currentAccount = i;
        3 r0 = new 3(context, ViewConfiguration.get(context).getScaledTouchSlop());
        this.windowView = r0;
        r0.setAlpha(0.7f);
        GroupCallPipButton groupCallPipButton = new GroupCallPipButton(context, this.currentAccount, false);
        this.button = groupCallPipButton;
        this.windowView.addView(groupCallPipButton, LayoutHelper.createFrame(-1, -1, 17));
        AvatarsImageView avatarsImageView = new AvatarsImageView(context, true);
        this.avatarsImageView = avatarsImageView;
        avatarsImageView.setStyle(5);
        this.avatarsImageView.setCentered(true);
        this.avatarsImageView.setVisibility(8);
        this.avatarsImageView.setDelegate(new Runnable() { // from class: org.telegram.ui.Components.GroupCallPip$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallPip.this.lambda$new$0();
            }
        });
        updateAvatars(false);
        this.windowView.addView(this.avatarsImageView, LayoutHelper.createFrame(108, 36, 49));
        this.windowRemoveTooltipView = new FrameLayout(context) { // from class: org.telegram.ui.Components.GroupCallPip.4
            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                super.onLayout(z, i2, i3, i4, i5);
                GroupCallPip groupCallPip = GroupCallPip.this;
                groupCallPip.windowRemoveTooltipView.getLocationOnScreen(groupCallPip.location);
                GroupCallPip groupCallPip2 = GroupCallPip.this;
                int[] iArr = groupCallPip2.location;
                groupCallPip2.windowLeft = iArr[0];
                groupCallPip2.windowTop = iArr[1] - AndroidUtilities.dp(25.0f);
            }

            @Override // android.view.View
            public void setVisibility(int i2) {
                super.setVisibility(i2);
                GroupCallPip.this.windowRemoveTooltipOverlayView.setVisibility(i2);
            }
        };
        View view = new View(context) { // from class: org.telegram.ui.Components.GroupCallPip.5
            Paint paint = new Paint(1);

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                GroupCallPip groupCallPip = GroupCallPip.this;
                boolean z = groupCallPip.animateToPrepareRemove;
                if (z) {
                    float f = groupCallPip.prepareToRemoveProgress;
                    if (f != 1.0f) {
                        float f2 = f + 0.064f;
                        groupCallPip.prepareToRemoveProgress = f2;
                        if (f2 > 1.0f) {
                            groupCallPip.prepareToRemoveProgress = 1.0f;
                        }
                        invalidate();
                        this.paint.setColor(ColorUtils.blendARGB(1711607061, 1714752530, GroupCallPip.this.prepareToRemoveProgress));
                        canvas.drawCircle(getMeasuredWidth() / 2.0f, (getMeasuredHeight() / 2.0f) - AndroidUtilities.dp(25.0f), AndroidUtilities.dp(35.0f) + (AndroidUtilities.dp(5.0f) * GroupCallPip.this.prepareToRemoveProgress), this.paint);
                    }
                }
                if (!z) {
                    float f3 = groupCallPip.prepareToRemoveProgress;
                    if (f3 != 0.0f) {
                        float f4 = f3 - 0.064f;
                        groupCallPip.prepareToRemoveProgress = f4;
                        if (f4 < 0.0f) {
                            groupCallPip.prepareToRemoveProgress = 0.0f;
                        }
                        invalidate();
                    }
                }
                this.paint.setColor(ColorUtils.blendARGB(1711607061, 1714752530, GroupCallPip.this.prepareToRemoveProgress));
                canvas.drawCircle(getMeasuredWidth() / 2.0f, (getMeasuredHeight() / 2.0f) - AndroidUtilities.dp(25.0f), AndroidUtilities.dp(35.0f) + (AndroidUtilities.dp(5.0f) * GroupCallPip.this.prepareToRemoveProgress), this.paint);
            }

            @Override // android.view.View
            public void setAlpha(float f) {
                super.setAlpha(f);
                GroupCallPip.this.windowRemoveTooltipOverlayView.setAlpha(f);
            }

            @Override // android.view.View
            public void setScaleX(float f) {
                super.setScaleX(f);
                GroupCallPip.this.windowRemoveTooltipOverlayView.setScaleX(f);
            }

            @Override // android.view.View
            public void setScaleY(float f) {
                super.setScaleY(f);
                GroupCallPip.this.windowRemoveTooltipOverlayView.setScaleY(f);
            }

            @Override // android.view.View
            public void setTranslationY(float f) {
                super.setTranslationY(f);
                GroupCallPip.this.windowRemoveTooltipOverlayView.setTranslationY(f);
            }
        };
        this.removeTooltipView = view;
        this.windowRemoveTooltipView.addView(view);
        this.windowRemoveTooltipOverlayView = new FrameLayout(context);
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.iconView = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        int i2 = R.raw.group_pip_delete_icon;
        RLottieDrawable rLottieDrawable = new RLottieDrawable(i2, "" + i2, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f), true, null);
        this.deleteIcon = rLottieDrawable;
        rLottieDrawable.setPlayInDirectionOfCustomEndFrame(true);
        rLottieImageView.setAnimation(this.deleteIcon);
        rLottieImageView.setColorFilter(-1);
        this.windowRemoveTooltipOverlayView.addView(rLottieImageView, LayoutHelper.createFrame(40, 40.0f, 17, 0.0f, 0.0f, 0.0f, 25.0f));
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.GroupCallPip.6
            int lastSize = -1;

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i3, int i4, int i5, int i6) {
                super.onLayout(z, i3, i4, i5, i6);
                android.graphics.Point point = AndroidUtilities.displaySize;
                int i7 = point.x + point.y;
                int i8 = this.lastSize;
                if (i8 > 0 && i8 != i7) {
                    setVisibility(8);
                    GroupCallPip groupCallPip = GroupCallPip.this;
                    groupCallPip.showAlert = false;
                    groupCallPip.checkButtonAlpha();
                }
                this.lastSize = i7;
            }

            @Override // android.view.View
            public void setVisibility(int i3) {
                super.setVisibility(i3);
                if (i3 == 8) {
                    this.lastSize = -1;
                }
            }
        };
        this.alertContainer = frameLayout;
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.GroupCallPip$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                GroupCallPip.this.lambda$new$1(view2);
            }
        });
        this.alertContainer.setClipChildren(false);
        FrameLayout frameLayout2 = this.alertContainer;
        GroupCallPipAlertView groupCallPipAlertView = new GroupCallPipAlertView(context, this.currentAccount);
        this.pipAlertView = groupCallPipAlertView;
        frameLayout2.addView(groupCallPipAlertView, LayoutHelper.createFrame(-2, -2.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkButtonAlpha() {
        boolean z = this.pressedState || this.showAlert;
        if (this.buttonInAlpha != z) {
            this.buttonInAlpha = z;
            this.windowView.animate().alpha(z ? 1.0f : 0.7f).start();
            this.button.setPressedState(z);
        }
    }

    public static boolean checkInlinePermissions() {
        return Build.VERSION.SDK_INT < 23 || ApplicationLoader.canDrawOverlays;
    }

    public static void clearForce() {
        forceRemoved = false;
    }

    private static WindowManager.LayoutParams createWindowLayoutParams(Context context) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = AndroidUtilities.dp(105.0f);
        layoutParams.width = AndroidUtilities.dp(105.0f);
        layoutParams.gravity = 51;
        layoutParams.format = -3;
        layoutParams.type = AndroidUtilities.checkInlinePermissions(context) ? Build.VERSION.SDK_INT >= 26 ? 2038 : 2003 : 99;
        layoutParams.flags = 520;
        return layoutParams;
    }

    public static void finish() {
        GroupCallPip groupCallPip = instance;
        if (groupCallPip != null) {
            groupCallPip.showAlert(false);
            GroupCallPip groupCallPip2 = instance;
            final WindowManager windowManager = groupCallPip2.windowManager;
            final FrameLayout frameLayout = groupCallPip2.windowView;
            final FrameLayout frameLayout2 = groupCallPip2.windowRemoveTooltipView;
            final FrameLayout frameLayout3 = groupCallPip2.windowRemoveTooltipOverlayView;
            final FrameLayout frameLayout4 = groupCallPip2.alertContainer;
            frameLayout.animate().scaleX(0.5f).scaleY(0.5f).alpha(0.0f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.GroupCallPip.10
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (frameLayout.getParent() != null) {
                        frameLayout.setVisibility(8);
                        frameLayout2.setVisibility(8);
                        frameLayout3.setVisibility(8);
                        windowManager.removeView(frameLayout);
                        windowManager.removeView(frameLayout2);
                        windowManager.removeView(frameLayout3);
                        windowManager.removeView(frameLayout4);
                    }
                }
            }).start();
            instance.onDestroy();
            instance = null;
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupCallVisibilityChanged, new Object[0]);
        }
    }

    public static GroupCallPip getInstance() {
        return instance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getRelativePosition(float f, float f2, float[] fArr) {
        android.graphics.Point point = AndroidUtilities.displaySize;
        float f3 = -AndroidUtilities.dp(36.0f);
        fArr[0] = (f - f3) / ((point.x - (f3 * 2.0f)) - AndroidUtilities.dp(105.0f));
        fArr[1] = f2 / (point.y - AndroidUtilities.dp(105.0f));
        fArr[0] = Math.min(1.0f, Math.max(0.0f, fArr[0]));
        fArr[1] = Math.min(1.0f, Math.max(0.0f, fArr[1]));
    }

    public static boolean isShowing() {
        VoIPService sharedInstance;
        if (!RTMPStreamPipOverlay.isVisible() && instance == null) {
            return (!checkInlinePermissions() || (sharedInstance = VoIPService.getSharedInstance()) == null || sharedInstance.groupCall == null || sharedInstance.isHangingUp() || forceRemoved || (!ApplicationLoader.mainInterfaceStopped && GroupCallActivity.groupCallUiVisible)) ? false : true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateAvatars(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        showAlert(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pinnedToCenter$3(ValueAnimator valueAnimator) {
        if (this.removed) {
            return;
        }
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.pinnedProgress = floatValue;
        this.button.setPinnedProgress(floatValue);
        this.windowView.setScaleX(1.0f - (this.pinnedProgress * 0.6f));
        this.windowView.setScaleY(1.0f - (this.pinnedProgress * 0.6f));
        if (this.moving) {
            updateButtonPosition();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$remove$2() {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupCallVisibilityChanged, new Object[0]);
    }

    public static boolean onBackPressed() {
        GroupCallPip groupCallPip = instance;
        if (groupCallPip == null || !groupCallPip.showAlert) {
            return false;
        }
        groupCallPip.showAlert(false);
        return true;
    }

    private void onDestroy() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupCallUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.webRtcSpeakerAmplitudeEvent);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.groupCallVisibilityChanged);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didEndCall);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void remove() {
        GroupCallPip groupCallPip = instance;
        if (groupCallPip == null) {
            return;
        }
        this.removed = true;
        forceRemoved = true;
        this.button.removed = true;
        groupCallPip.showAlert(false);
        float measuredWidth = ((this.windowLeft - this.windowOffsetLeft) + (this.windowRemoveTooltipView.getMeasuredWidth() / 2.0f)) - (this.windowLayoutParams.x + (this.windowView.getMeasuredWidth() / 2.0f));
        float measuredHeight = ((this.windowTop - this.windowOffsetTop) + (this.windowRemoveTooltipView.getMeasuredHeight() / 2.0f)) - (this.windowLayoutParams.y + (this.windowView.getMeasuredHeight() / 2.0f));
        GroupCallPip groupCallPip2 = instance;
        WindowManager windowManager = groupCallPip2.windowManager;
        FrameLayout frameLayout = groupCallPip2.windowView;
        FrameLayout frameLayout2 = groupCallPip2.windowRemoveTooltipView;
        FrameLayout frameLayout3 = groupCallPip2.windowRemoveTooltipOverlayView;
        FrameLayout frameLayout4 = groupCallPip2.alertContainer;
        onDestroy();
        instance = null;
        AnimatorSet animatorSet = new AnimatorSet();
        long currentFrame = this.deleteIcon.getCurrentFrame() < 33 ? ((1.0f - (this.deleteIcon.getCurrentFrame() / 33.0f)) * ((float) this.deleteIcon.getDuration())) / 2.0f : 0L;
        float f = this.windowLayoutParams.x;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(f, measuredWidth + f);
        ofFloat.addUpdateListener(this.updateXlistener);
        ValueAnimator duration = ofFloat.setDuration(250L);
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
        duration.setInterpolator(cubicBezierInterpolator);
        animatorSet.playTogether(ofFloat);
        float f2 = this.windowLayoutParams.y;
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(f2, (f2 + measuredHeight) - AndroidUtilities.dp(30.0f), this.windowLayoutParams.y + measuredHeight);
        ofFloat2.addUpdateListener(this.updateYlistener);
        ofFloat2.setDuration(250L).setInterpolator(cubicBezierInterpolator);
        animatorSet.playTogether(ofFloat2);
        Property property = View.SCALE_X;
        animatorSet.playTogether(ObjectAnimator.ofFloat(frameLayout, property, frameLayout.getScaleX(), 0.1f).setDuration(180L));
        Property property2 = View.SCALE_Y;
        animatorSet.playTogether(ObjectAnimator.ofFloat(frameLayout, property2, frameLayout.getScaleY(), 0.1f).setDuration(180L));
        Property property3 = View.ALPHA;
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(frameLayout, property3, 1.0f, 0.0f);
        float f3 = (float) 350;
        ofFloat3.setStartDelay(f3 * 0.7f);
        ofFloat3.setDuration(f3 * 0.3f);
        animatorSet.playTogether(ofFloat3);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.GroupCallPip$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                GroupCallPip.lambda$remove$2();
            }
        }, 370L);
        long j = currentFrame + 530;
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.removeTooltipView, property, 1.0f, 1.05f);
        ofFloat4.setDuration(j);
        CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.EASE_BOTH;
        ofFloat4.setInterpolator(cubicBezierInterpolator2);
        animatorSet.playTogether(ofFloat4);
        ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(this.removeTooltipView, property2, 1.0f, 1.05f);
        ofFloat5.setDuration(j);
        ofFloat5.setInterpolator(cubicBezierInterpolator2);
        animatorSet.playTogether(ofFloat5);
        ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(this.removeTooltipView, property, 1.0f, 0.3f);
        ofFloat6.setStartDelay(j);
        ofFloat6.setDuration(350L);
        CubicBezierInterpolator cubicBezierInterpolator3 = CubicBezierInterpolator.EASE_OUT_QUINT;
        ofFloat6.setInterpolator(cubicBezierInterpolator3);
        animatorSet.playTogether(ofFloat6);
        ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat(this.removeTooltipView, property2, 1.0f, 0.3f);
        ofFloat7.setStartDelay(j);
        ofFloat7.setDuration(350L);
        ofFloat7.setInterpolator(cubicBezierInterpolator3);
        animatorSet.playTogether(ofFloat7);
        ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat(this.removeTooltipView, View.TRANSLATION_Y, 0.0f, AndroidUtilities.dp(60.0f));
        ofFloat8.setStartDelay(j);
        ofFloat8.setDuration(350L);
        ofFloat8.setInterpolator(cubicBezierInterpolator3);
        animatorSet.playTogether(ofFloat8);
        ObjectAnimator ofFloat9 = ObjectAnimator.ofFloat(this.removeTooltipView, property3, 1.0f, 0.0f);
        ofFloat9.setStartDelay(j);
        ofFloat9.setDuration(350L);
        ofFloat9.setInterpolator(cubicBezierInterpolator3);
        animatorSet.playTogether(ofFloat9);
        animatorSet.addListener(new 9(frameLayout, frameLayout2, windowManager, frameLayout3, frameLayout4));
        animatorSet.start();
        this.deleteIcon.setCustomEndFrame(66);
        this.iconView.stopAnimation();
        this.iconView.playAnimation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPosition(float f, float f2) {
        float f3 = -AndroidUtilities.dp(36.0f);
        this.windowLayoutParams.x = (int) (f3 + (((AndroidUtilities.displaySize.x - (2.0f * f3)) - AndroidUtilities.dp(105.0f)) * f));
        this.windowLayoutParams.y = (int) ((AndroidUtilities.displaySize.y - AndroidUtilities.dp(105.0f)) * f2);
        updateAvatarsPosition();
        if (this.windowView.getParent() != null) {
            this.windowManager.updateViewLayout(this.windowView, this.windowLayoutParams);
        }
    }

    public static void show(Context context, int i) {
        if (instance != null) {
            return;
        }
        instance = new GroupCallPip(context, i);
        WindowManager windowManager = (WindowManager) ApplicationLoader.applicationContext.getSystemService("window");
        instance.windowManager = windowManager;
        WindowManager.LayoutParams createWindowLayoutParams = createWindowLayoutParams(context);
        createWindowLayoutParams.width = -1;
        createWindowLayoutParams.height = -1;
        createWindowLayoutParams.dimAmount = 0.25f;
        createWindowLayoutParams.flags = 522;
        windowManager.addView(instance.alertContainer, createWindowLayoutParams);
        instance.alertContainer.setVisibility(8);
        WindowManager.LayoutParams createWindowLayoutParams2 = createWindowLayoutParams(context);
        createWindowLayoutParams2.gravity = 81;
        createWindowLayoutParams2.width = AndroidUtilities.dp(100.0f);
        createWindowLayoutParams2.height = AndroidUtilities.dp(150.0f);
        windowManager.addView(instance.windowRemoveTooltipView, createWindowLayoutParams2);
        WindowManager.LayoutParams createWindowLayoutParams3 = createWindowLayoutParams(context);
        GroupCallPip groupCallPip = instance;
        groupCallPip.windowLayoutParams = createWindowLayoutParams3;
        windowManager.addView(groupCallPip.windowView, createWindowLayoutParams3);
        WindowManager.LayoutParams createWindowLayoutParams4 = createWindowLayoutParams(context);
        createWindowLayoutParams4.gravity = 81;
        createWindowLayoutParams4.width = AndroidUtilities.dp(100.0f);
        createWindowLayoutParams4.height = AndroidUtilities.dp(150.0f);
        windowManager.addView(instance.windowRemoveTooltipOverlayView, createWindowLayoutParams4);
        instance.windowRemoveTooltipView.setVisibility(8);
        instance.windowView.setScaleX(0.5f);
        instance.windowView.setScaleY(0.5f);
        instance.windowView.setAlpha(0.0f);
        instance.windowView.animate().alpha(0.7f).scaleY(1.0f).scaleX(1.0f).setDuration(350L).setInterpolator(new OvershootInterpolator()).start();
        NotificationCenter.getInstance(instance.currentAccount).addObserver(instance, NotificationCenter.groupCallUpdated);
        NotificationCenter.getGlobalInstance().addObserver(instance, NotificationCenter.webRtcSpeakerAmplitudeEvent);
        NotificationCenter.getGlobalInstance().addObserver(instance, NotificationCenter.didEndCall);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAlert(boolean z) {
        ViewPropertyAnimator listener;
        if (z != this.showAlert) {
            this.showAlert = z;
            this.alertContainer.animate().setListener(null).cancel();
            if (this.showAlert) {
                if (this.alertContainer.getVisibility() != 0) {
                    this.alertContainer.setVisibility(0);
                    this.alertContainer.setAlpha(0.0f);
                    this.pipAlertView.setScaleX(0.7f);
                    this.pipAlertView.setScaleY(0.7f);
                }
                this.alertContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.Components.GroupCallPip.7
                    @Override // android.view.ViewTreeObserver.OnPreDrawListener
                    public boolean onPreDraw() {
                        GroupCallPipAlertView groupCallPipAlertView;
                        GroupCallPip.this.alertContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                        GroupCallPip groupCallPip = GroupCallPip.this;
                        groupCallPip.alertContainer.getLocationOnScreen(groupCallPip.location);
                        GroupCallPip groupCallPip2 = GroupCallPip.this;
                        float measuredWidth = groupCallPip2.windowLayoutParams.x + groupCallPip2.windowOffsetLeft + (groupCallPip2.button.getMeasuredWidth() / 2.0f);
                        GroupCallPip groupCallPip3 = GroupCallPip.this;
                        float f = measuredWidth - groupCallPip3.location[0];
                        int i = 1;
                        float measuredWidth2 = ((groupCallPip3.windowLayoutParams.y + groupCallPip3.windowOffsetTop) + (groupCallPip3.button.getMeasuredWidth() / 2.0f)) - GroupCallPip.this.location[1];
                        boolean z2 = measuredWidth2 - ((float) AndroidUtilities.dp(61.0f)) > 0.0f && ((float) AndroidUtilities.dp(61.0f)) + measuredWidth2 < ((float) GroupCallPip.this.alertContainer.getMeasuredHeight());
                        if (AndroidUtilities.dp(61.0f) + f + GroupCallPip.this.pipAlertView.getMeasuredWidth() >= GroupCallPip.this.alertContainer.getMeasuredWidth() - AndroidUtilities.dp(16.0f) || !z2) {
                            if ((f - AndroidUtilities.dp(61.0f)) - GroupCallPip.this.pipAlertView.getMeasuredWidth() > AndroidUtilities.dp(16.0f) && z2) {
                                float dp = AndroidUtilities.dp(40.0f) / GroupCallPip.this.pipAlertView.getMeasuredHeight();
                                float max = Math.max(dp, Math.min(measuredWidth2 / GroupCallPip.this.alertContainer.getMeasuredHeight(), 1.0f - dp));
                                GroupCallPip.this.pipAlertView.setTranslationX((int) ((f - AndroidUtilities.dp(61.0f)) - GroupCallPip.this.pipAlertView.getMeasuredWidth()));
                                GroupCallPipAlertView groupCallPipAlertView2 = GroupCallPip.this.pipAlertView;
                                groupCallPipAlertView2.setTranslationY((int) (measuredWidth2 - (groupCallPipAlertView2.getMeasuredHeight() * max)));
                                groupCallPipAlertView = GroupCallPip.this.pipAlertView;
                            } else if (measuredWidth2 > GroupCallPip.this.alertContainer.getMeasuredHeight() * 0.3f) {
                                float dp2 = AndroidUtilities.dp(40.0f) / GroupCallPip.this.pipAlertView.getMeasuredWidth();
                                float max2 = Math.max(dp2, Math.min(f / GroupCallPip.this.alertContainer.getMeasuredWidth(), 1.0f - dp2));
                                GroupCallPipAlertView groupCallPipAlertView3 = GroupCallPip.this.pipAlertView;
                                groupCallPipAlertView3.setTranslationX((int) (f - (groupCallPipAlertView3.getMeasuredWidth() * max2)));
                                GroupCallPipAlertView groupCallPipAlertView4 = GroupCallPip.this.pipAlertView;
                                groupCallPipAlertView4.setTranslationY((int) ((measuredWidth2 - groupCallPipAlertView4.getMeasuredHeight()) - AndroidUtilities.dp(61.0f)));
                                groupCallPipAlertView = GroupCallPip.this.pipAlertView;
                                i = 3;
                            } else {
                                float dp3 = AndroidUtilities.dp(40.0f) / GroupCallPip.this.pipAlertView.getMeasuredWidth();
                                float max3 = Math.max(dp3, Math.min(f / GroupCallPip.this.alertContainer.getMeasuredWidth(), 1.0f - dp3));
                                GroupCallPipAlertView groupCallPipAlertView5 = GroupCallPip.this.pipAlertView;
                                groupCallPipAlertView5.setTranslationX((int) (f - (groupCallPipAlertView5.getMeasuredWidth() * max3)));
                                GroupCallPip.this.pipAlertView.setTranslationY((int) (AndroidUtilities.dp(61.0f) + measuredWidth2));
                                groupCallPipAlertView = GroupCallPip.this.pipAlertView;
                                i = 2;
                            }
                            groupCallPipAlertView.setPosition(i, f, measuredWidth2);
                        } else {
                            GroupCallPip.this.pipAlertView.setTranslationX(AndroidUtilities.dp(61.0f) + f);
                            float dp4 = AndroidUtilities.dp(40.0f) / GroupCallPip.this.pipAlertView.getMeasuredHeight();
                            float max4 = Math.max(dp4, Math.min(measuredWidth2 / GroupCallPip.this.alertContainer.getMeasuredHeight(), 1.0f - dp4));
                            GroupCallPipAlertView groupCallPipAlertView6 = GroupCallPip.this.pipAlertView;
                            groupCallPipAlertView6.setTranslationY((int) (measuredWidth2 - (groupCallPipAlertView6.getMeasuredHeight() * max4)));
                            GroupCallPip.this.pipAlertView.setPosition(0, f, measuredWidth2);
                        }
                        return false;
                    }
                });
                this.alertContainer.animate().alpha(1.0f).setDuration(150L).start();
                listener = this.pipAlertView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150L);
            } else {
                this.pipAlertView.animate().scaleX(0.7f).scaleY(0.7f).setDuration(150L).start();
                listener = this.alertContainer.animate().alpha(0.0f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.GroupCallPip.8
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        GroupCallPip.this.alertContainer.setVisibility(8);
                    }
                });
            }
            listener.start();
        }
        checkButtonAlpha();
    }

    private void showAvatars(boolean z) {
        ViewPropertyAnimator listener;
        if (z != (this.avatarsImageView.getTag() != null)) {
            this.avatarsImageView.animate().setListener(null).cancel();
            if (z) {
                if (this.avatarsImageView.getVisibility() != 0) {
                    this.avatarsImageView.setVisibility(0);
                    this.avatarsImageView.setAlpha(0.0f);
                    this.avatarsImageView.setScaleX(0.5f);
                    this.avatarsImageView.setScaleY(0.5f);
                }
                listener = this.avatarsImageView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150L);
            } else {
                listener = this.avatarsImageView.animate().alpha(0.0f).scaleX(0.5f).scaleY(0.5f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.GroupCallPip.13
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        GroupCallPip.this.avatarsImageView.setVisibility(8);
                    }
                });
            }
            listener.start();
            this.avatarsImageView.setTag(z ? 1 : null);
        }
    }

    private void updateAvatars(boolean z) {
        AvatarsImageView avatarsImageView = this.avatarsImageView;
        if (avatarsImageView.avatarsDrawable.transitionProgressAnimator != null) {
            avatarsImageView.updateAfterTransitionEnd();
            return;
        }
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        ChatObject.Call call = sharedInstance != null ? sharedInstance.groupCall : null;
        int i = 0;
        if (call != null) {
            long selfId = sharedInstance.getSelfId();
            int size = call.sortedParticipants.size();
            int i2 = 0;
            while (i < 2) {
                if (i2 < size) {
                    TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = call.sortedParticipants.get(i2);
                    if (MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer) != selfId && SystemClock.uptimeMillis() - tLRPC$TL_groupCallParticipant.lastSpeakTime <= 500) {
                        this.avatarsImageView.setObject(i, this.currentAccount, tLRPC$TL_groupCallParticipant);
                    }
                    i2++;
                } else {
                    this.avatarsImageView.setObject(i, this.currentAccount, null);
                }
                i++;
                i2++;
            }
            this.avatarsImageView.setObject(2, this.currentAccount, null);
        } else {
            while (i < 3) {
                this.avatarsImageView.setObject(i, this.currentAccount, null);
                i++;
            }
        }
        this.avatarsImageView.commitTransition(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAvatarsPosition() {
        int i = AndroidUtilities.displaySize.x;
        float min = Math.min(Math.max(this.windowLayoutParams.x, -AndroidUtilities.dp(36.0f)), (i - this.windowView.getMeasuredWidth()) + AndroidUtilities.dp(36.0f));
        if (min < 0.0f) {
            this.avatarsImageView.setTranslationX(Math.abs(min) / 3.0f);
        } else if (min > i - this.windowView.getMeasuredWidth()) {
            this.avatarsImageView.setTranslationX((-Math.abs(min - (i - this.windowView.getMeasuredWidth()))) / 3.0f);
        } else {
            this.avatarsImageView.setTranslationX(0.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateButtonPosition() {
        float measuredWidth = ((this.windowLeft - this.windowOffsetLeft) + (this.windowRemoveTooltipView.getMeasuredWidth() / 2.0f)) - (this.windowView.getMeasuredWidth() / 2.0f);
        float measuredHeight = (((this.windowTop - this.windowOffsetTop) + (this.windowRemoveTooltipView.getMeasuredHeight() / 2.0f)) - (this.windowView.getMeasuredHeight() / 2.0f)) - AndroidUtilities.dp(25.0f);
        WindowManager.LayoutParams layoutParams = this.windowLayoutParams;
        float f = this.windowX;
        float f2 = this.pinnedProgress;
        float f3 = 1.0f - f2;
        layoutParams.x = (int) ((f * f3) + (measuredWidth * f2));
        layoutParams.y = (int) ((this.windowY * f3) + (measuredHeight * f2));
        updateAvatarsPosition();
        if (this.windowView.getParent() != null) {
            this.windowManager.updateViewLayout(this.windowView, this.windowLayoutParams);
        }
    }

    public static void updateVisibility(Context context) {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        boolean z = (sharedInstance == null || sharedInstance.groupCall == null || sharedInstance.isHangingUp()) ? false : true;
        if (!AndroidUtilities.checkInlinePermissions(ApplicationLoader.applicationContext) || !z || forceRemoved || (!ApplicationLoader.mainInterfaceStopped && GroupCallActivity.groupCallUiVisible)) {
            finish();
            return;
        }
        show(context, sharedInstance.getAccount());
        instance.showAvatars(true);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.groupCallUpdated || i == NotificationCenter.webRtcSpeakerAmplitudeEvent) {
            updateAvatars(true);
        } else if (i == NotificationCenter.didEndCall) {
            updateVisibility(ApplicationLoader.applicationContext);
        }
    }

    void pinnedToCenter(final boolean z) {
        if (this.removed || this.animateToPinnedToCenter == z) {
            return;
        }
        this.animateToPinnedToCenter = z;
        ValueAnimator valueAnimator = this.pinAnimator;
        if (valueAnimator != null) {
            valueAnimator.removeAllListeners();
            this.pinAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.pinnedProgress, z ? 1.0f : 0.0f);
        this.pinAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.GroupCallPip$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                GroupCallPip.this.lambda$pinnedToCenter$3(valueAnimator2);
            }
        });
        this.pinAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.GroupCallPip.12
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                GroupCallPip groupCallPip = GroupCallPip.this;
                if (groupCallPip.removed) {
                    return;
                }
                groupCallPip.pinnedProgress = z ? 1.0f : 0.0f;
                groupCallPip.button.setPinnedProgress(GroupCallPip.this.pinnedProgress);
                GroupCallPip groupCallPip2 = GroupCallPip.this;
                groupCallPip2.windowView.setScaleX(1.0f - (groupCallPip2.pinnedProgress * 0.6f));
                GroupCallPip groupCallPip3 = GroupCallPip.this;
                groupCallPip3.windowView.setScaleY(1.0f - (groupCallPip3.pinnedProgress * 0.6f));
                GroupCallPip groupCallPip4 = GroupCallPip.this;
                if (groupCallPip4.moving) {
                    groupCallPip4.updateButtonPosition();
                }
            }
        });
        this.pinAnimator.setDuration(250L);
        this.pinAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.pinAnimator.start();
    }

    void prepareToRemove(boolean z) {
        if (this.animateToPrepareRemove != z) {
            this.animateToPrepareRemove = z;
            this.removeTooltipView.invalidate();
            if (!this.removed) {
                this.deleteIcon.setCustomEndFrame(z ? 33 : 0);
                this.iconView.playAnimation();
            }
            if (z) {
                this.button.performHapticFeedback(3, 2);
            }
        }
        this.button.prepareToRemove(z);
    }

    void showRemoveTooltip(boolean z) {
        AnimatorSet animatorSet;
        if (this.animateToShowRemoveTooltip != z) {
            this.animateToShowRemoveTooltip = z;
            AnimatorSet animatorSet2 = this.showRemoveAnimator;
            if (animatorSet2 != null) {
                animatorSet2.removeAllListeners();
                this.showRemoveAnimator.cancel();
            }
            if (z) {
                if (this.windowRemoveTooltipView.getVisibility() != 0) {
                    this.windowRemoveTooltipView.setVisibility(0);
                    this.removeTooltipView.setAlpha(0.0f);
                    this.removeTooltipView.setScaleX(0.5f);
                    this.removeTooltipView.setScaleY(0.5f);
                    this.deleteIcon.setCurrentFrame(0);
                }
                AnimatorSet animatorSet3 = new AnimatorSet();
                this.showRemoveAnimator = animatorSet3;
                View view = this.removeTooltipView;
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.ALPHA, view.getAlpha(), 1.0f);
                View view2 = this.removeTooltipView;
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view2, View.SCALE_X, view2.getScaleX(), 1.0f);
                View view3 = this.removeTooltipView;
                animatorSet3.playTogether(ofFloat, ofFloat2, ObjectAnimator.ofFloat(view3, View.SCALE_Y, view3.getScaleY(), 1.0f));
                animatorSet = this.showRemoveAnimator.setDuration(150L);
            } else {
                AnimatorSet animatorSet4 = new AnimatorSet();
                this.showRemoveAnimator = animatorSet4;
                View view4 = this.removeTooltipView;
                ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view4, View.ALPHA, view4.getAlpha(), 0.0f);
                View view5 = this.removeTooltipView;
                ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(view5, View.SCALE_X, view5.getScaleX(), 0.5f);
                View view6 = this.removeTooltipView;
                animatorSet4.playTogether(ofFloat3, ofFloat4, ObjectAnimator.ofFloat(view6, View.SCALE_Y, view6.getScaleY(), 0.5f));
                this.showRemoveAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.GroupCallPip.11
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        GroupCallPip.this.windowRemoveTooltipView.setVisibility(8);
                        GroupCallPip groupCallPip = GroupCallPip.this;
                        groupCallPip.animateToPrepareRemove = false;
                        groupCallPip.prepareToRemoveProgress = 0.0f;
                    }
                });
                this.showRemoveAnimator.setDuration(150L);
                animatorSet = this.showRemoveAnimator;
            }
            animatorSet.start();
        }
    }
}
