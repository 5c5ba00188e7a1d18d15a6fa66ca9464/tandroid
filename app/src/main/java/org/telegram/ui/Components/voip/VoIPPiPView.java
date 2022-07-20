package org.telegram.ui.Components.voip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.voip.VideoCapturerDevice;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.VoIPFragment;
import org.webrtc.EglBase;
/* loaded from: classes3.dex */
public class VoIPPiPView implements VoIPService.StateListener, NotificationCenter.NotificationCenterDelegate {
    public static int bottomInset = 0;
    private static VoIPPiPView expandedInstance = null;
    private static VoIPPiPView instance = null;
    public static boolean switchingToPip = false;
    public static int topInset;
    ValueAnimator animatorToCameraMini;
    boolean callingUserIsVideo;
    public final VoIPTextureView callingUserTextureView;
    ImageView closeIcon;
    private int currentAccount;
    boolean currentUserIsVideo;
    public final VoIPTextureView currentUserTextureView;
    ImageView enlargeIcon;
    ValueAnimator expandAnimator;
    public boolean expanded;
    private boolean expandedAnimationInProgress;
    FloatingView floatingView;
    AnimatorSet moveToBoundsAnimator;
    boolean moving;
    public final int parentHeight;
    public final int parentWidth;
    float progressToCameraMini;
    long startTime;
    float startX;
    float startY;
    View topShadow;
    public WindowManager.LayoutParams windowLayoutParams;
    private WindowManager windowManager;
    public FrameLayout windowView;
    public int xOffset;
    public int yOffset;
    ValueAnimator.AnimatorUpdateListener animatorToCameraMiniUpdater = new VoIPPiPView$$ExternalSyntheticLambda0(this);
    float[] point = new float[2];
    Runnable collapseRunnable = new AnonymousClass1(this);
    private ValueAnimator.AnimatorUpdateListener updateXlistener = new AnonymousClass2();
    private ValueAnimator.AnimatorUpdateListener updateYlistener = new AnonymousClass3();

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onAudioSettingsChanged() {
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onCameraFirstFrameAvailable() {
        VoIPService.StateListener.CC.$default$onCameraFirstFrameAvailable(this);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onSignalBarsCountChanged(int i) {
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onVideoAvailableChange(boolean z) {
    }

    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.progressToCameraMini = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.floatingView.invalidate();
    }

    /* renamed from: org.telegram.ui.Components.voip.VoIPPiPView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1(VoIPPiPView voIPPiPView) {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (VoIPPiPView.instance != null) {
                VoIPPiPView.instance.floatingView.expand(false);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.voip.VoIPPiPView$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements ValueAnimator.AnimatorUpdateListener {
        AnonymousClass2() {
            VoIPPiPView.this = r1;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            VoIPPiPView voIPPiPView = VoIPPiPView.this;
            voIPPiPView.windowLayoutParams.x = (int) floatValue;
            if (voIPPiPView.windowView.getParent() != null) {
                WindowManager windowManager = VoIPPiPView.this.windowManager;
                VoIPPiPView voIPPiPView2 = VoIPPiPView.this;
                windowManager.updateViewLayout(voIPPiPView2.windowView, voIPPiPView2.windowLayoutParams);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.voip.VoIPPiPView$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements ValueAnimator.AnimatorUpdateListener {
        AnonymousClass3() {
            VoIPPiPView.this = r1;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            VoIPPiPView voIPPiPView = VoIPPiPView.this;
            voIPPiPView.windowLayoutParams.y = (int) floatValue;
            if (voIPPiPView.windowView.getParent() != null) {
                WindowManager windowManager = VoIPPiPView.this.windowManager;
                VoIPPiPView voIPPiPView2 = VoIPPiPView.this;
                windowManager.updateViewLayout(voIPPiPView2.windowView, voIPPiPView2.windowLayoutParams);
            }
        }
    }

    public static void show(Activity activity, int i, int i2, int i3, int i4) {
        WindowManager windowManager;
        if (instance != null || VideoCapturerDevice.eglBase == null) {
            return;
        }
        WindowManager.LayoutParams createWindowLayoutParams = createWindowLayoutParams(activity, i2, i3, 0.25f);
        instance = new VoIPPiPView(activity, i2, i3, false);
        if (AndroidUtilities.checkInlinePermissions(activity)) {
            windowManager = (WindowManager) ApplicationLoader.applicationContext.getSystemService("window");
        } else {
            windowManager = (WindowManager) activity.getSystemService("window");
        }
        VoIPPiPView voIPPiPView = instance;
        voIPPiPView.currentAccount = i;
        voIPPiPView.windowManager = windowManager;
        voIPPiPView.windowLayoutParams = createWindowLayoutParams;
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("voippipconfig", 0);
        instance.setRelativePosition(sharedPreferences.getFloat("relativeX", 1.0f), sharedPreferences.getFloat("relativeY", 0.0f));
        NotificationCenter.getGlobalInstance().addObserver(instance, NotificationCenter.didEndCall);
        windowManager.addView(instance.windowView, createWindowLayoutParams);
        instance.currentUserTextureView.renderer.init(VideoCapturerDevice.eglBase.getEglBaseContext(), null);
        instance.callingUserTextureView.renderer.init(VideoCapturerDevice.eglBase.getEglBaseContext(), null);
        if (i4 != 0) {
            if (i4 != 1) {
                return;
            }
            instance.windowView.setAlpha(0.0f);
            if (VoIPService.getSharedInstance() == null) {
                return;
            }
            VoIPService sharedInstance = VoIPService.getSharedInstance();
            VoIPPiPView voIPPiPView2 = instance;
            sharedInstance.setBackgroundSinks(voIPPiPView2.currentUserTextureView.renderer, voIPPiPView2.callingUserTextureView.renderer);
            return;
        }
        instance.windowView.setScaleX(0.5f);
        instance.windowView.setScaleY(0.5f);
        instance.windowView.setAlpha(0.0f);
        instance.windowView.animate().alpha(1.0f).scaleY(1.0f).scaleX(1.0f).start();
        if (VoIPService.getSharedInstance() == null) {
            return;
        }
        VoIPService sharedInstance2 = VoIPService.getSharedInstance();
        VoIPPiPView voIPPiPView3 = instance;
        sharedInstance2.setSinks(voIPPiPView3.currentUserTextureView.renderer, voIPPiPView3.callingUserTextureView.renderer);
    }

    public static WindowManager.LayoutParams createWindowLayoutParams(Context context, int i, int i2, float f) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        float f2 = i2;
        float f3 = f2 * 0.4f;
        float f4 = i;
        float f5 = 0.4f * f4;
        layoutParams.height = (int) ((f2 * f) + ((((int) ((f3 * 1.05f) - f3)) / 2) * 2));
        layoutParams.width = (int) ((f4 * f) + ((((int) ((1.05f * f5) - f5)) / 2) * 2));
        layoutParams.gravity = 51;
        layoutParams.format = -3;
        if (AndroidUtilities.checkInlinePermissions(context)) {
            if (Build.VERSION.SDK_INT >= 26) {
                layoutParams.type = 2038;
            } else {
                layoutParams.type = 2003;
            }
        } else {
            layoutParams.type = 99;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            layoutParams.flags |= Integer.MIN_VALUE;
        }
        layoutParams.flags = 16778120;
        return layoutParams;
    }

    public static void prepareForTransition() {
        if (expandedInstance != null) {
            instance.expandAnimator.cancel();
        }
    }

    public static void finish() {
        if (switchingToPip) {
            return;
        }
        VoIPPiPView voIPPiPView = expandedInstance;
        if (voIPPiPView != null) {
            voIPPiPView.finishInternal();
        }
        VoIPPiPView voIPPiPView2 = instance;
        if (voIPPiPView2 != null) {
            voIPPiPView2.finishInternal();
        }
        expandedInstance = null;
        instance = null;
    }

    public static boolean isExpanding() {
        return instance.expanded;
    }

    private void setRelativePosition(float f, float f2) {
        Point point = AndroidUtilities.displaySize;
        float f3 = point.x;
        float f4 = point.y;
        float dp = AndroidUtilities.dp(16.0f);
        float dp2 = AndroidUtilities.dp(16.0f);
        float dp3 = AndroidUtilities.dp(60.0f);
        float dp4 = AndroidUtilities.dp(16.0f);
        float f5 = this.parentWidth * 0.25f;
        float f6 = this.parentHeight * 0.25f;
        if (this.floatingView.getMeasuredWidth() != 0) {
            f5 = this.floatingView.getMeasuredWidth();
        }
        if (this.floatingView.getMeasuredWidth() != 0) {
            f6 = this.floatingView.getMeasuredHeight();
        }
        WindowManager.LayoutParams layoutParams = this.windowLayoutParams;
        layoutParams.x = (int) ((f * (((f3 - dp) - dp2) - f5)) - (this.xOffset - dp));
        layoutParams.y = (int) ((f2 * (((f4 - dp3) - dp4) - f6)) - (this.yOffset - dp3));
        if (this.windowView.getParent() != null) {
            this.windowManager.updateViewLayout(this.windowView, this.windowLayoutParams);
        }
    }

    public static VoIPPiPView getInstance() {
        VoIPPiPView voIPPiPView = expandedInstance;
        return voIPPiPView != null ? voIPPiPView : instance;
    }

    public VoIPPiPView(Context context, int i, int i2, boolean z) {
        this.parentWidth = i;
        this.parentHeight = i2;
        float f = i2 * 0.4f;
        this.yOffset = ((int) ((f * 1.05f) - f)) / 2;
        float f2 = i * 0.4f;
        this.xOffset = ((int) ((1.05f * f2) - f2)) / 2;
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context, ContextCompat.getDrawable(context, 2131165316));
        this.windowView = anonymousClass4;
        anonymousClass4.setWillNotDraw(false);
        FrameLayout frameLayout = this.windowView;
        int i3 = this.xOffset;
        int i4 = this.yOffset;
        frameLayout.setPadding(i3, i4, i3, i4);
        this.floatingView = new FloatingView(context);
        VoIPTextureView voIPTextureView = new VoIPTextureView(context, false, true);
        this.callingUserTextureView = voIPTextureView;
        voIPTextureView.scaleType = VoIPTextureView.SCALE_TYPE_NONE;
        VoIPTextureView voIPTextureView2 = new VoIPTextureView(context, false, true);
        this.currentUserTextureView = voIPTextureView2;
        voIPTextureView2.renderer.setMirror(true);
        this.floatingView.addView(voIPTextureView);
        this.floatingView.addView(voIPTextureView2);
        this.floatingView.setBackgroundColor(-7829368);
        this.windowView.addView(this.floatingView);
        this.windowView.setClipChildren(false);
        this.windowView.setClipToPadding(false);
        if (z) {
            View view = new View(context);
            this.topShadow = view;
            view.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{ColorUtils.setAlphaComponent(-16777216, 76), 0}));
            this.floatingView.addView(this.topShadow, -1, AndroidUtilities.dp(60.0f));
            ImageView imageView = new ImageView(context);
            this.closeIcon = imageView;
            imageView.setImageResource(2131166055);
            this.closeIcon.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            this.closeIcon.setContentDescription(LocaleController.getString("Close", 2131625167));
            this.floatingView.addView(this.closeIcon, LayoutHelper.createFrame(40, 40.0f, 53, 4.0f, 4.0f, 4.0f, 0.0f));
            ImageView imageView2 = new ImageView(context);
            this.enlargeIcon = imageView2;
            imageView2.setImageResource(2131166056);
            this.enlargeIcon.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            this.enlargeIcon.setContentDescription(LocaleController.getString("Open", 2131627090));
            this.floatingView.addView(this.enlargeIcon, LayoutHelper.createFrame(40, 40.0f, 51, 4.0f, 4.0f, 4.0f, 0.0f));
            this.closeIcon.setOnClickListener(VoIPPiPView$$ExternalSyntheticLambda2.INSTANCE);
            this.enlargeIcon.setOnClickListener(new VoIPPiPView$$ExternalSyntheticLambda1(this, context));
        }
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null) {
            sharedInstance.registerStateListener(this);
        }
        updateViewState();
    }

    /* renamed from: org.telegram.ui.Components.voip.VoIPPiPView$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends FrameLayout {
        final /* synthetic */ Drawable val$outerDrawable;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context, Drawable drawable) {
            super(context);
            VoIPPiPView.this = r1;
            this.val$outerDrawable = drawable;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            canvas.save();
            canvas.scale(VoIPPiPView.this.floatingView.getScaleX(), VoIPPiPView.this.floatingView.getScaleY(), VoIPPiPView.this.floatingView.getLeft() + VoIPPiPView.this.floatingView.getPivotX(), VoIPPiPView.this.floatingView.getTop() + VoIPPiPView.this.floatingView.getPivotY());
            this.val$outerDrawable.setBounds(VoIPPiPView.this.floatingView.getLeft() - AndroidUtilities.dp(2.0f), VoIPPiPView.this.floatingView.getTop() - AndroidUtilities.dp(2.0f), VoIPPiPView.this.floatingView.getRight() + AndroidUtilities.dp(2.0f), VoIPPiPView.this.floatingView.getBottom() + AndroidUtilities.dp(2.0f));
            this.val$outerDrawable.draw(canvas);
            canvas.restore();
            super.onDraw(canvas);
        }
    }

    public static /* synthetic */ void lambda$new$1(View view) {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null) {
            sharedInstance.hangUp();
        } else {
            finish();
        }
    }

    public /* synthetic */ void lambda$new$2(Context context, View view) {
        boolean z = context instanceof LaunchActivity;
        if (z && !ApplicationLoader.mainInterfacePaused) {
            VoIPFragment.show((Activity) context, this.currentAccount);
        } else if (!z) {
        } else {
            Intent intent = new Intent(context, LaunchActivity.class);
            intent.setAction("voip");
            context.startActivity(intent);
        }
    }

    public void finishInternal() {
        this.currentUserTextureView.renderer.release();
        this.callingUserTextureView.renderer.release();
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null) {
            sharedInstance.unregisterStateListener(this);
        }
        this.windowView.setVisibility(8);
        if (this.windowView.getParent() != null) {
            this.floatingView.getRelativePosition(this.point);
            float min = Math.min(1.0f, Math.max(0.0f, this.point[0]));
            ApplicationLoader.applicationContext.getSharedPreferences("voippipconfig", 0).edit().putFloat("relativeX", min).putFloat("relativeY", Math.min(1.0f, Math.max(0.0f, this.point[1]))).apply();
            try {
                this.windowManager.removeView(this.windowView);
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didEndCall);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onStateChanged(int i) {
        if (i == 11 || i == 17 || i == 4 || i == 10) {
            AndroidUtilities.runOnUIThread(VoIPPiPView$$ExternalSyntheticLambda3.INSTANCE, 200L);
        }
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null) {
            finish();
        } else if (i == 3 && !sharedInstance.isVideoAvailable()) {
            finish();
        } else {
            updateViewState();
        }
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onMediaStateUpdated(int i, int i2) {
        updateViewState();
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onCameraSwitch(boolean z) {
        updateViewState();
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onScreenOnChange(boolean z) {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null) {
            return;
        }
        if (!z && this.currentUserIsVideo) {
            sharedInstance.setVideoState(false, 1);
        } else if (!z || sharedInstance.getVideoState(false) != 1) {
        } else {
            sharedInstance.setVideoState(false, 2);
        }
    }

    private void updateViewState() {
        boolean z = this.floatingView.getMeasuredWidth() != 0;
        boolean z2 = this.callingUserIsVideo;
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        float f = 1.0f;
        if (sharedInstance != null) {
            this.callingUserIsVideo = sharedInstance.getRemoteVideoState() == 2;
            this.currentUserIsVideo = sharedInstance.getVideoState(false) == 2 || sharedInstance.getVideoState(false) == 1;
            this.currentUserTextureView.renderer.setMirror(sharedInstance.isFrontFaceCamera());
            this.currentUserTextureView.setIsScreencast(sharedInstance.isScreencast());
            this.currentUserTextureView.setScreenshareMiniProgress(1.0f, false);
        }
        if (!z) {
            if (!this.callingUserIsVideo) {
                f = 0.0f;
            }
            this.progressToCameraMini = f;
        } else if (z2 == this.callingUserIsVideo) {
        } else {
            ValueAnimator valueAnimator = this.animatorToCameraMini;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            float[] fArr = new float[2];
            fArr[0] = this.progressToCameraMini;
            if (!this.callingUserIsVideo) {
                f = 0.0f;
            }
            fArr[1] = f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.animatorToCameraMini = ofFloat;
            ofFloat.addUpdateListener(this.animatorToCameraMiniUpdater);
            this.animatorToCameraMini.setDuration(300L).setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.animatorToCameraMini.start();
        }
    }

    public void onTransitionEnd() {
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().swapSinks();
        }
    }

    public void onPause() {
        if (this.windowLayoutParams.type == 99) {
            VoIPService sharedInstance = VoIPService.getSharedInstance();
            if (!this.currentUserIsVideo) {
                return;
            }
            sharedInstance.setVideoState(false, 1);
        }
    }

    public void onResume() {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null || sharedInstance.getVideoState(false) != 1) {
            return;
        }
        sharedInstance.setVideoState(false, 2);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.didEndCall) {
            finish();
        }
    }

    /* loaded from: classes3.dex */
    public class FloatingView extends FrameLayout {
        float bottomPadding;
        float leftPadding;
        float rightPadding;
        float topPadding;
        float touchSlop;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public FloatingView(Context context) {
            super(context);
            VoIPPiPView.this = r2;
            this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            if (Build.VERSION.SDK_INT >= 21) {
                setOutlineProvider(new AnonymousClass1(this, r2));
                setClipToOutline(true);
            }
        }

        /* renamed from: org.telegram.ui.Components.voip.VoIPPiPView$FloatingView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends ViewOutlineProvider {
            AnonymousClass1(FloatingView floatingView, VoIPPiPView voIPPiPView) {
            }

            @Override // android.view.ViewOutlineProvider
            @TargetApi(21)
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight(), (1.0f / view.getScaleX()) * AndroidUtilities.dp(4.0f));
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            this.leftPadding = AndroidUtilities.dp(16.0f);
            this.rightPadding = AndroidUtilities.dp(16.0f);
            this.topPadding = AndroidUtilities.dp(60.0f);
            this.bottomPadding = AndroidUtilities.dp(16.0f);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            VoIPPiPView voIPPiPView = VoIPPiPView.this;
            voIPPiPView.currentUserTextureView.setPivotX(voIPPiPView.callingUserTextureView.getMeasuredWidth());
            VoIPPiPView voIPPiPView2 = VoIPPiPView.this;
            voIPPiPView2.currentUserTextureView.setPivotY(voIPPiPView2.callingUserTextureView.getMeasuredHeight());
            VoIPPiPView.this.currentUserTextureView.setTranslationX((-AndroidUtilities.dp(4.0f)) * (1.0f / getScaleX()) * VoIPPiPView.this.progressToCameraMini);
            VoIPPiPView.this.currentUserTextureView.setTranslationY((-AndroidUtilities.dp(4.0f)) * (1.0f / getScaleY()) * VoIPPiPView.this.progressToCameraMini);
            VoIPPiPView.this.currentUserTextureView.setRoundCorners(AndroidUtilities.dp(8.0f) * (1.0f / getScaleY()) * VoIPPiPView.this.progressToCameraMini);
            VoIPPiPView voIPPiPView3 = VoIPPiPView.this;
            voIPPiPView3.currentUserTextureView.setScaleX(((1.0f - voIPPiPView3.progressToCameraMini) * 0.6f) + 0.4f);
            VoIPPiPView voIPPiPView4 = VoIPPiPView.this;
            voIPPiPView4.currentUserTextureView.setScaleY(((1.0f - voIPPiPView4.progressToCameraMini) * 0.6f) + 0.4f);
            VoIPPiPView voIPPiPView5 = VoIPPiPView.this;
            voIPPiPView5.currentUserTextureView.setAlpha(Math.min(1.0f, 1.0f - voIPPiPView5.progressToCameraMini));
            super.dispatchDraw(canvas);
        }

        /* JADX WARN: Code restructure failed: missing block: B:14:0x0035, code lost:
            if (r4 != 3) goto L65;
         */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (VoIPPiPView.this.expandedAnimationInProgress || VoIPPiPView.switchingToPip || VoIPPiPView.instance == null) {
                return false;
            }
            AndroidUtilities.cancelRunOnUIThread(VoIPPiPView.this.collapseRunnable);
            float rawX = motionEvent.getRawX();
            float rawY = motionEvent.getRawY();
            ViewParent parent = getParent();
            int action = motionEvent.getAction();
            if (action == 0) {
                VoIPPiPView voIPPiPView = VoIPPiPView.this;
                voIPPiPView.startX = rawX;
                voIPPiPView.startY = rawY;
                voIPPiPView.startTime = System.currentTimeMillis();
                AnimatorSet animatorSet = VoIPPiPView.this.moveToBoundsAnimator;
                if (animatorSet != null) {
                    animatorSet.cancel();
                }
            } else {
                if (action != 1) {
                    if (action == 2) {
                        VoIPPiPView voIPPiPView2 = VoIPPiPView.this;
                        float f = rawX - voIPPiPView2.startX;
                        float f2 = rawY - voIPPiPView2.startY;
                        if (!voIPPiPView2.moving) {
                            float f3 = (f * f) + (f2 * f2);
                            float f4 = this.touchSlop;
                            if (f3 > f4 * f4) {
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                                VoIPPiPView voIPPiPView3 = VoIPPiPView.this;
                                voIPPiPView3.moving = true;
                                voIPPiPView3.startX = rawX;
                                voIPPiPView3.startY = rawY;
                                f = 0.0f;
                                f2 = 0.0f;
                            }
                        }
                        VoIPPiPView voIPPiPView4 = VoIPPiPView.this;
                        if (voIPPiPView4.moving) {
                            WindowManager.LayoutParams layoutParams = voIPPiPView4.windowLayoutParams;
                            layoutParams.x = (int) (layoutParams.x + f);
                            layoutParams.y = (int) (layoutParams.y + f2);
                            voIPPiPView4.startX = rawX;
                            voIPPiPView4.startY = rawY;
                            if (voIPPiPView4.windowView.getParent() != null) {
                                WindowManager windowManager = VoIPPiPView.this.windowManager;
                                VoIPPiPView voIPPiPView5 = VoIPPiPView.this;
                                windowManager.updateViewLayout(voIPPiPView5.windowView, voIPPiPView5.windowLayoutParams);
                            }
                        }
                    }
                }
                AnimatorSet animatorSet2 = VoIPPiPView.this.moveToBoundsAnimator;
                if (animatorSet2 != null) {
                    animatorSet2.cancel();
                }
                if (motionEvent.getAction() == 1 && !VoIPPiPView.this.moving && System.currentTimeMillis() - VoIPPiPView.this.startTime < 150) {
                    Context context = getContext();
                    boolean z = context instanceof LaunchActivity;
                    if (z && !ApplicationLoader.mainInterfacePaused) {
                        VoIPFragment.show((Activity) context, VoIPPiPView.this.currentAccount);
                    } else if (z) {
                        Intent intent = new Intent(context, LaunchActivity.class);
                        intent.setAction("voip");
                        context.startActivity(intent);
                    }
                    VoIPPiPView.this.moving = false;
                    return false;
                }
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                    Point point = AndroidUtilities.displaySize;
                    int i = point.x;
                    int i2 = point.y + VoIPPiPView.topInset;
                    float f5 = this.topPadding;
                    float f6 = this.bottomPadding;
                    VoIPPiPView voIPPiPView6 = VoIPPiPView.this;
                    float left = voIPPiPView6.windowLayoutParams.x + voIPPiPView6.floatingView.getLeft();
                    float measuredWidth = VoIPPiPView.this.floatingView.getMeasuredWidth() + left;
                    VoIPPiPView voIPPiPView7 = VoIPPiPView.this;
                    float top = voIPPiPView7.windowLayoutParams.y + voIPPiPView7.floatingView.getTop();
                    float measuredHeight = VoIPPiPView.this.floatingView.getMeasuredHeight() + top;
                    VoIPPiPView.this.moveToBoundsAnimator = new AnimatorSet();
                    float f7 = this.leftPadding;
                    if (left < f7) {
                        VoIPPiPView voIPPiPView8 = VoIPPiPView.this;
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(voIPPiPView8.windowLayoutParams.x, f7 - voIPPiPView8.floatingView.getLeft());
                        ofFloat.addUpdateListener(VoIPPiPView.this.updateXlistener);
                        VoIPPiPView.this.moveToBoundsAnimator.playTogether(ofFloat);
                    } else if (measuredWidth > i - this.rightPadding) {
                        VoIPPiPView voIPPiPView9 = VoIPPiPView.this;
                        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(voIPPiPView9.windowLayoutParams.x, (i - voIPPiPView9.floatingView.getRight()) - this.rightPadding);
                        ofFloat2.addUpdateListener(VoIPPiPView.this.updateXlistener);
                        VoIPPiPView.this.moveToBoundsAnimator.playTogether(ofFloat2);
                    }
                    if (top < f5) {
                        VoIPPiPView voIPPiPView10 = VoIPPiPView.this;
                        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(voIPPiPView10.windowLayoutParams.y, f5 - voIPPiPView10.floatingView.getTop());
                        ofFloat3.addUpdateListener(VoIPPiPView.this.updateYlistener);
                        VoIPPiPView.this.moveToBoundsAnimator.playTogether(ofFloat3);
                    } else if (measuredHeight > i2 - f6) {
                        VoIPPiPView voIPPiPView11 = VoIPPiPView.this;
                        ValueAnimator ofFloat4 = ValueAnimator.ofFloat(voIPPiPView11.windowLayoutParams.y, (i2 - voIPPiPView11.floatingView.getMeasuredHeight()) - f6);
                        ofFloat4.addUpdateListener(VoIPPiPView.this.updateYlistener);
                        VoIPPiPView.this.moveToBoundsAnimator.playTogether(ofFloat4);
                    }
                    VoIPPiPView.this.moveToBoundsAnimator.setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT);
                    VoIPPiPView.this.moveToBoundsAnimator.start();
                }
                VoIPPiPView.this.moving = false;
                if (VoIPPiPView.instance.expanded) {
                    AndroidUtilities.runOnUIThread(VoIPPiPView.this.collapseRunnable, 3000L);
                }
            }
            return true;
        }

        public void getRelativePosition(float[] fArr) {
            Point point = AndroidUtilities.displaySize;
            VoIPPiPView voIPPiPView = VoIPPiPView.this;
            float left = voIPPiPView.windowLayoutParams.x + voIPPiPView.floatingView.getLeft();
            float f = this.leftPadding;
            fArr[0] = (left - f) / (((point.x - f) - this.rightPadding) - VoIPPiPView.this.floatingView.getMeasuredWidth());
            VoIPPiPView voIPPiPView2 = VoIPPiPView.this;
            float top = voIPPiPView2.windowLayoutParams.y + voIPPiPView2.floatingView.getTop();
            float f2 = this.topPadding;
            fArr[1] = (top - f2) / (((point.y - f2) - this.bottomPadding) - VoIPPiPView.this.floatingView.getMeasuredHeight());
            fArr[0] = Math.min(1.0f, Math.max(0.0f, fArr[0]));
            fArr[1] = Math.min(1.0f, Math.max(0.0f, fArr[1]));
        }

        public void expand(boolean z) {
            AndroidUtilities.cancelRunOnUIThread(VoIPPiPView.this.collapseRunnable);
            if (VoIPPiPView.instance == null || VoIPPiPView.this.expandedAnimationInProgress || VoIPPiPView.instance.expanded == z) {
                return;
            }
            VoIPPiPView.instance.expanded = z;
            VoIPPiPView voIPPiPView = VoIPPiPView.this;
            int i = voIPPiPView.parentWidth;
            int i2 = voIPPiPView.xOffset;
            float f = (i * 0.25f) + (i2 * 2);
            int i3 = voIPPiPView.parentHeight;
            int i4 = voIPPiPView.yOffset;
            float f2 = (i3 * 0.25f) + (i4 * 2);
            float f3 = (i * 0.4f) + (i2 * 2);
            float f4 = (i3 * 0.4f) + (i4 * 2);
            voIPPiPView.expandedAnimationInProgress = true;
            if (z) {
                Context context = VoIPPiPView.instance.windowView.getContext();
                VoIPPiPView voIPPiPView2 = VoIPPiPView.this;
                WindowManager.LayoutParams createWindowLayoutParams = VoIPPiPView.createWindowLayoutParams(context, voIPPiPView2.parentWidth, voIPPiPView2.parentHeight, 0.4f);
                Context context2 = getContext();
                VoIPPiPView voIPPiPView3 = VoIPPiPView.this;
                VoIPPiPView voIPPiPView4 = new VoIPPiPView(context2, voIPPiPView3.parentWidth, voIPPiPView3.parentHeight, true);
                getRelativePosition(VoIPPiPView.this.point);
                VoIPPiPView voIPPiPView5 = VoIPPiPView.this;
                float[] fArr = voIPPiPView5.point;
                float f5 = fArr[0];
                float f6 = fArr[1];
                WindowManager.LayoutParams layoutParams = voIPPiPView5.windowLayoutParams;
                createWindowLayoutParams.x = (int) (layoutParams.x - ((f3 - f) * f5));
                createWindowLayoutParams.y = (int) (layoutParams.y - ((f4 - f2) * f6));
                voIPPiPView5.windowManager.addView(voIPPiPView4.windowView, createWindowLayoutParams);
                voIPPiPView4.windowView.setAlpha(1.0f);
                voIPPiPView4.windowLayoutParams = createWindowLayoutParams;
                voIPPiPView4.windowManager = VoIPPiPView.this.windowManager;
                VoIPPiPView unused = VoIPPiPView.expandedInstance = voIPPiPView4;
                swapRender(VoIPPiPView.instance, VoIPPiPView.expandedInstance);
                float scaleX = VoIPPiPView.this.floatingView.getScaleX() * 0.625f;
                voIPPiPView4.floatingView.setPivotX(f5 * VoIPPiPView.this.parentWidth * 0.4f);
                voIPPiPView4.floatingView.setPivotY(f6 * VoIPPiPView.this.parentHeight * 0.4f);
                voIPPiPView4.floatingView.setScaleX(scaleX);
                voIPPiPView4.floatingView.setScaleY(scaleX);
                VoIPPiPView.expandedInstance.topShadow.setAlpha(0.0f);
                VoIPPiPView.expandedInstance.closeIcon.setAlpha(0.0f);
                VoIPPiPView.expandedInstance.enlargeIcon.setAlpha(0.0f);
                AndroidUtilities.runOnUIThread(new VoIPPiPView$FloatingView$$ExternalSyntheticLambda2(this, scaleX, voIPPiPView4), 64L);
            } else if (VoIPPiPView.expandedInstance == null) {
            } else {
                VoIPPiPView.expandedInstance.floatingView.getRelativePosition(VoIPPiPView.this.point);
                float[] fArr2 = VoIPPiPView.this.point;
                float f7 = fArr2[0];
                float f8 = fArr2[1];
                VoIPPiPView.instance.windowLayoutParams.x = (int) (VoIPPiPView.expandedInstance.windowLayoutParams.x + ((f3 - f) * f7));
                VoIPPiPView.instance.windowLayoutParams.y = (int) (VoIPPiPView.expandedInstance.windowLayoutParams.y + ((f4 - f2) * f8));
                VoIPPiPView.expandedInstance.floatingView.setPivotX(f7 * VoIPPiPView.this.parentWidth * 0.4f);
                VoIPPiPView.expandedInstance.floatingView.setPivotY(f8 * VoIPPiPView.this.parentHeight * 0.4f);
                showUi(false);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.addUpdateListener(new VoIPPiPView$FloatingView$$ExternalSyntheticLambda0(VoIPPiPView.this.floatingView.getScaleX() * 0.625f));
                ofFloat.setDuration(300L).setInterpolator(CubicBezierInterpolator.DEFAULT);
                ofFloat.addListener(new AnonymousClass3(z));
                ofFloat.start();
                VoIPPiPView.this.expandAnimator = ofFloat;
            }
        }

        public /* synthetic */ void lambda$expand$1(float f, VoIPPiPView voIPPiPView) {
            if (VoIPPiPView.expandedInstance == null) {
                return;
            }
            VoIPPiPView.this.windowView.setAlpha(0.0f);
            try {
                VoIPPiPView.this.windowManager.removeView(VoIPPiPView.this.windowView);
            } catch (Throwable th) {
                FileLog.e(th);
            }
            animate().cancel();
            showUi(true);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addUpdateListener(new VoIPPiPView$FloatingView$$ExternalSyntheticLambda1(f, 1.0f, voIPPiPView));
            ofFloat.addListener(new AnonymousClass2());
            ofFloat.setDuration(300L).setInterpolator(CubicBezierInterpolator.DEFAULT);
            ofFloat.start();
            VoIPPiPView.this.expandAnimator = ofFloat;
        }

        public static /* synthetic */ void lambda$expand$0(float f, float f2, VoIPPiPView voIPPiPView, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            float f3 = (f * (1.0f - floatValue)) + (f2 * floatValue);
            voIPPiPView.floatingView.setScaleX(f3);
            voIPPiPView.floatingView.setScaleY(f3);
            voIPPiPView.floatingView.invalidate();
            voIPPiPView.windowView.invalidate();
            if (Build.VERSION.SDK_INT >= 21) {
                voIPPiPView.floatingView.invalidateOutline();
            }
        }

        /* renamed from: org.telegram.ui.Components.voip.VoIPPiPView$FloatingView$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends AnimatorListenerAdapter {
            AnonymousClass2() {
                FloatingView.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                VoIPPiPView.this.expandedAnimationInProgress = false;
            }
        }

        public static /* synthetic */ void lambda$expand$2(float f, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            float f2 = (1.0f - floatValue) + (f * floatValue);
            if (VoIPPiPView.expandedInstance != null) {
                VoIPPiPView.expandedInstance.floatingView.setScaleX(f2);
                VoIPPiPView.expandedInstance.floatingView.setScaleY(f2);
                VoIPPiPView.expandedInstance.floatingView.invalidate();
                if (Build.VERSION.SDK_INT >= 21) {
                    VoIPPiPView.expandedInstance.floatingView.invalidateOutline();
                }
                VoIPPiPView.expandedInstance.windowView.invalidate();
            }
        }

        /* renamed from: org.telegram.ui.Components.voip.VoIPPiPView$FloatingView$3 */
        /* loaded from: classes3.dex */
        public class AnonymousClass3 extends AnimatorListenerAdapter {
            final /* synthetic */ boolean val$expanded;

            AnonymousClass3(boolean z) {
                FloatingView.this = r1;
                this.val$expanded = z;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (VoIPPiPView.expandedInstance == null) {
                    return;
                }
                FloatingView.this.swapRender(VoIPPiPView.expandedInstance, VoIPPiPView.instance);
                VoIPPiPView.instance.windowView.setAlpha(1.0f);
                VoIPPiPView.this.windowManager.addView(VoIPPiPView.instance.windowView, VoIPPiPView.instance.windowLayoutParams);
                AndroidUtilities.runOnUIThread(new VoIPPiPView$FloatingView$3$$ExternalSyntheticLambda0(this, this.val$expanded), 64L);
            }

            public /* synthetic */ void lambda$onAnimationEnd$0(boolean z) {
                if (VoIPPiPView.instance == null || VoIPPiPView.expandedInstance == null) {
                    return;
                }
                VoIPPiPView.expandedInstance.windowView.setAlpha(0.0f);
                VoIPPiPView.expandedInstance.finishInternal();
                VoIPPiPView.this.expandedAnimationInProgress = false;
                if (!z) {
                    return;
                }
                AndroidUtilities.runOnUIThread(VoIPPiPView.this.collapseRunnable, 3000L);
            }
        }

        private void showUi(boolean z) {
            if (VoIPPiPView.expandedInstance == null) {
                return;
            }
            float f = 0.0f;
            if (z) {
                VoIPPiPView.expandedInstance.topShadow.setAlpha(0.0f);
                VoIPPiPView.expandedInstance.closeIcon.setAlpha(0.0f);
                VoIPPiPView.expandedInstance.enlargeIcon.setAlpha(0.0f);
            }
            ViewPropertyAnimator duration = VoIPPiPView.expandedInstance.topShadow.animate().alpha(z ? 1.0f : 0.0f).setDuration(300L);
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            duration.setInterpolator(cubicBezierInterpolator).start();
            VoIPPiPView.expandedInstance.closeIcon.animate().alpha(z ? 1.0f : 0.0f).setDuration(300L).setInterpolator(cubicBezierInterpolator).start();
            ViewPropertyAnimator animate = VoIPPiPView.expandedInstance.enlargeIcon.animate();
            if (z) {
                f = 1.0f;
            }
            animate.alpha(f).setDuration(300L).setInterpolator(cubicBezierInterpolator).start();
        }

        public void swapRender(VoIPPiPView voIPPiPView, VoIPPiPView voIPPiPView2) {
            voIPPiPView2.currentUserTextureView.setStub(voIPPiPView.currentUserTextureView);
            voIPPiPView2.callingUserTextureView.setStub(voIPPiPView.callingUserTextureView);
            voIPPiPView.currentUserTextureView.renderer.release();
            voIPPiPView.callingUserTextureView.renderer.release();
            EglBase eglBase = VideoCapturerDevice.eglBase;
            if (eglBase == null) {
                return;
            }
            voIPPiPView2.currentUserTextureView.renderer.init(eglBase.getEglBaseContext(), null);
            voIPPiPView2.callingUserTextureView.renderer.init(VideoCapturerDevice.eglBase.getEglBaseContext(), null);
            if (VoIPService.getSharedInstance() == null) {
                return;
            }
            VoIPService.getSharedInstance().setSinks(voIPPiPView2.currentUserTextureView.renderer, voIPPiPView2.callingUserTextureView.renderer);
        }
    }
}
