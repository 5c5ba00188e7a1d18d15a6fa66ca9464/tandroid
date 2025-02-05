package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.PowerManager;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.voip.EncryptionKeyEmojifier;
import org.telegram.messenger.voip.Instance;
import org.telegram.messenger.voip.VideoCapturerDevice;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.messenger.voip.VoIPServiceState;
import org.telegram.messenger.voip.VoipAudioManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_phone;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.DarkAlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackgroundGradientDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.HideViewAfterAnimation;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.voip.AcceptDeclineView;
import org.telegram.ui.Components.voip.EmojiRationalLayout;
import org.telegram.ui.Components.voip.EndCloseLayout;
import org.telegram.ui.Components.voip.HideEmojiTextView;
import org.telegram.ui.Components.voip.ImageWithWavesView;
import org.telegram.ui.Components.voip.PrivateVideoPreviewDialogNew;
import org.telegram.ui.Components.voip.RateCallLayout;
import org.telegram.ui.Components.voip.VoIPBackgroundProvider;
import org.telegram.ui.Components.voip.VoIPButtonsLayout;
import org.telegram.ui.Components.voip.VoIPFloatingLayout;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.Components.voip.VoIPNotificationsLayout;
import org.telegram.ui.Components.voip.VoIPPiPView;
import org.telegram.ui.Components.voip.VoIPStatusTextView;
import org.telegram.ui.Components.voip.VoIPTextureView;
import org.telegram.ui.Components.voip.VoIPToggleButton;
import org.telegram.ui.Components.voip.VoIPWindowView;
import org.telegram.ui.Components.voip.VoIpCoverView;
import org.telegram.ui.Components.voip.VoIpGradientLayout;
import org.telegram.ui.Components.voip.VoIpHintView;
import org.telegram.ui.Components.voip.VoIpSnowView;
import org.telegram.ui.Components.voip.VoIpSwitchLayout;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.VoIPFragment;
import org.webrtc.EglBase;
import org.webrtc.GlRectDrawer;
import org.webrtc.MediaStreamTrack;
import org.webrtc.RendererCommon;
import org.webrtc.TextureViewRenderer;

/* loaded from: classes4.dex */
public class VoIPFragment implements VoIPService.StateListener, NotificationCenter.NotificationCenterDelegate {
    private static VoIPFragment instance;
    private AcceptDeclineView acceptDeclineView;
    private AccessibilityManager accessibilityManager;
    Activity activity;
    private ImageView backIcon;
    private VoIPToggleButton bottomEndCallBtn;
    private VoIpSwitchLayout bottomMuteBtn;
    View bottomShadow;
    private VoIpSwitchLayout bottomSpeakerBtn;
    private VoIpSwitchLayout bottomVideoBtn;
    private VoIPButtonsLayout buttonsLayout;
    TLRPC.User callingUser;
    boolean callingUserIsVideo;
    private VoIPFloatingLayout callingUserMiniFloatingLayout;
    private TextureViewRenderer callingUserMiniTextureRenderer;
    private ImageWithWavesView callingUserPhotoViewMini;
    private VoIPTextureView callingUserTextureView;
    private TextView callingUserTitle;
    boolean cameraForceExpanded;
    private Animator cameraShowingAnimator;
    private boolean canHideUI;
    private boolean canSwitchToPip;
    private boolean canZoomGesture;
    private final int currentAccount;
    private int currentState;
    TLRPC.User currentUser;
    private VoIPFloatingLayout currentUserCameraFloatingLayout;
    private boolean currentUserCameraIsFullscreen;
    boolean currentUserIsVideo;
    private VoIPTextureView currentUserTextureView;
    private boolean deviceIsLocked;
    private boolean emojiExpanded;
    LinearLayout emojiLayout;
    private boolean emojiLoaded;
    LinearLayout emojiRationalLayout;
    TextView emojiRationalTextView;
    TextView emojiRationalTopTextView;
    HintView2 encryptionTooltip;
    EndCloseLayout endCloseLayout;
    boolean enterFromPiP;
    private float enterTransitionProgress;
    float fillNaviagtionBarValue;
    private ViewGroup fragmentView;
    private VoIpGradientLayout gradientLayout;
    FrameLayout hideEmojiLayout;
    TextView hideEmojiTextView;
    boolean hideUiRunnableWaiting;
    private boolean isFinished;
    private boolean isInPinchToZoomTouchMode;
    private boolean isNearEar;
    boolean isOutgoing;
    private boolean isVideoCall;
    long lastContentTapTime;
    private WindowInsets lastInsets;
    private boolean lockOnScreen;
    VoIPNotificationsLayout notificationsLayout;
    private float pinchCenterX;
    private float pinchCenterY;
    private float pinchStartCenterX;
    private float pinchStartCenterY;
    private float pinchStartDistance;
    private float pinchTranslationX;
    private float pinchTranslationY;
    private int pointerId1;
    private int pointerId2;
    private PrivateVideoPreviewDialogNew previewDialog;
    private int previousState;
    RateCallLayout rateCallLayout;
    private boolean screenWasWakeup;
    private int selectedRating;
    private boolean signalBarWasReceived;
    private ImageView speakerPhoneIcon;
    LinearLayout statusLayout;
    private int statusLayoutAnimateToOffset;
    private VoIPStatusTextView statusTextView;
    private boolean switchingToPip;
    HintView2 tapToVideoTooltip;
    View topShadow;
    float touchSlop;
    ValueAnimator uiVisibilityAnimator;
    private VoIpCoverView voIpCoverView;
    private VoIpSnowView voIpSnowView;
    private boolean wasEstablished;
    private VoIPWindowView windowView;
    ValueAnimator zoomBackAnimator;
    private boolean zoomStarted;
    private final VoIPBackgroundProvider backgroundProvider = new VoIPBackgroundProvider();
    BackupImageView[] emojiViews = new BackupImageView[4];
    Emoji.EmojiDrawable[] emojiDrawables = new Emoji.EmojiDrawable[4];
    Paint overlayPaint = new Paint();
    Paint overlayBottomPaint = new Paint();
    private boolean uiVisible = true;
    float uiVisibilityAlpha = 1.0f;
    AnimationNotificationsLocker notificationsLocker = new AnimationNotificationsLocker();
    ValueAnimator.AnimatorUpdateListener statusbarAnimatorListener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda8
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            VoIPFragment.this.lambda$new$0(valueAnimator);
        }
    };
    Runnable hideUIRunnable = new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda9
        @Override // java.lang.Runnable
        public final void run() {
            VoIPFragment.this.lambda$new$1();
        }
    };
    Runnable stopAnimatingBgRunnable = new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda10
        @Override // java.lang.Runnable
        public final void run() {
            VoIPFragment.this.lambda$new$2();
        }
    };
    float pinchScale = 1.0f;

    class 12 implements RendererCommon.RendererEvents {
        12() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFirstFrameRendered$0() {
            VoIPFragment.this.updateViewState();
        }

        @Override // org.webrtc.RendererCommon.RendererEvents
        public void onFirstFrameRendered() {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.VoIPFragment$12$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPFragment.12.this.lambda$onFirstFrameRendered$0();
                }
            });
        }

        @Override // org.webrtc.RendererCommon.RendererEvents
        public void onFrameResolutionChanged(int i, int i2, int i3) {
        }
    }

    class 13 implements RendererCommon.RendererEvents {
        13() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFirstFrameRendered$0() {
            VoIPFragment.this.updateViewState();
        }

        @Override // org.webrtc.RendererCommon.RendererEvents
        public void onFirstFrameRendered() {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.VoIPFragment$13$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPFragment.13.this.lambda$onFirstFrameRendered$0();
                }
            });
        }

        @Override // org.webrtc.RendererCommon.RendererEvents
        public void onFrameResolutionChanged(int i, int i2, int i3) {
        }
    }

    class 14 extends AnimatorListenerAdapter {
        14() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            VoIPFragment.this.notificationsLocker.unlock();
            VoIPPiPView.getInstance().onTransitionEnd();
            VoIPFragment.this.currentUserCameraFloatingLayout.setCornerRadius(-1.0f);
            VoIPFragment.this.callingUserTextureView.renderer.release();
            VoIPFragment.this.currentUserTextureView.renderer.release();
            VoIPFragment.this.callingUserMiniTextureRenderer.release();
            VoIPFragment.this.destroy();
            VoIPFragment.this.windowView.finishImmediate();
            VoIPPiPView.switchingToPip = false;
            VoIPFragment.this.switchingToPip = false;
            VoIPFragment unused = VoIPFragment.instance = null;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            VoIPPiPView.getInstance().windowView.setAlpha(1.0f);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.VoIPFragment$14$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPFragment.14.this.lambda$onAnimationEnd$0();
                }
            }, 200L);
        }
    }

    class 8 implements AcceptDeclineView.Listener {
        8() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAccept$0() {
            if (VoIPService.getSharedState() != null) {
                VoIPService.getSharedState().acceptIncomingCall();
                if (!VoIPFragment.this.currentUserIsVideo || VoIPService.getSharedInstance() == null) {
                    return;
                }
                VoIPService.getSharedInstance().requestVideoCall(false);
            }
        }

        @Override // org.telegram.ui.Components.voip.AcceptDeclineView.Listener
        public void onAccept() {
            int checkSelfPermission;
            if (VoIPFragment.this.currentState != 17) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkSelfPermission = VoIPFragment.this.activity.checkSelfPermission("android.permission.RECORD_AUDIO");
                    if (checkSelfPermission != 0) {
                        VoIPFragment.this.activity.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 101);
                        return;
                    }
                }
                if (VoIPService.getSharedState() != null) {
                    VoIPFragment.this.runAcceptCallAnimation(new Runnable() { // from class: org.telegram.ui.VoIPFragment$8$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            VoIPFragment.8.this.lambda$onAccept$0();
                        }
                    });
                    return;
                }
                return;
            }
            Intent intent = new Intent(VoIPFragment.this.activity, (Class<?>) VoIPService.class);
            intent.putExtra("user_id", VoIPFragment.this.callingUser.id);
            intent.putExtra("is_outgoing", true);
            intent.putExtra("start_incall_activity", false);
            intent.putExtra("video_call", VoIPFragment.this.isVideoCall);
            intent.putExtra("can_video_call", VoIPFragment.this.isVideoCall);
            intent.putExtra("account", VoIPFragment.this.currentAccount);
            try {
                VoIPFragment.this.activity.startService(intent);
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }

        @Override // org.telegram.ui.Components.voip.AcceptDeclineView.Listener
        public void onDecline() {
            if (VoIPFragment.this.currentState == 17 || VoIPService.getSharedState() == null) {
                VoIPFragment.this.windowView.finish();
            } else {
                VoIPService.getSharedState().declineIncomingCall();
            }
        }
    }

    public VoIPFragment(int i) {
        this.currentAccount = i;
        this.currentUser = MessagesController.getInstance(i).getUser(Long.valueOf(UserConfig.getInstance(i).getClientUserId()));
        VoIPServiceState sharedState = VoIPService.getSharedState();
        if (sharedState == null) {
            return;
        }
        this.callingUser = sharedState.getUser();
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().registerStateListener(this);
        }
        this.isOutgoing = sharedState.isOutgoing();
        this.previousState = -1;
        this.currentState = sharedState.getCallState();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.webRtcSpeakerAmplitudeEvent);
        NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.voipServiceCreated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.closeInCallActivity);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.nearEarEvent);
    }

    private void checkEmojiLoaded(boolean z) {
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            Emoji.EmojiDrawable emojiDrawable = this.emojiDrawables[i2];
            if (emojiDrawable != null && emojiDrawable.isLoaded()) {
                i++;
            }
        }
        if (i == 4) {
            this.emojiLoaded = true;
            for (int i3 = 0; i3 < 4; i3++) {
                if (this.emojiViews[i3].getVisibility() != 0) {
                    this.emojiViews[i3].setVisibility(0);
                    if (z) {
                        this.emojiViews[i3].setAlpha(0.0f);
                        this.emojiViews[i3].setScaleX(0.0f);
                        this.emojiViews[i3].setScaleY(0.0f);
                        this.emojiViews[i3].animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT_BACK).setDuration(250L).start();
                    }
                }
            }
            this.encryptionTooltip.postDelayed(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPFragment.this.lambda$checkEmojiLoaded$32();
                }
            }, 1000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkPointerIds(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() < 2) {
            return false;
        }
        if (this.pointerId1 == motionEvent.getPointerId(0) && this.pointerId2 == motionEvent.getPointerId(1)) {
            return true;
        }
        return this.pointerId1 == motionEvent.getPointerId(1) && this.pointerId2 == motionEvent.getPointerId(0);
    }

    public static void clearInstance() {
        WindowInsets windowInsets;
        int systemWindowInsetTop;
        int systemWindowInsetBottom;
        if (instance != null) {
            if (VoIPService.getSharedInstance() != null) {
                int measuredHeight = instance.windowView.getMeasuredHeight();
                VoIPFragment voIPFragment = instance;
                if (voIPFragment.canSwitchToPip) {
                    VoIPPiPView.show(voIPFragment.activity, voIPFragment.currentAccount, voIPFragment.windowView.getMeasuredWidth(), measuredHeight, 0);
                    if (Build.VERSION.SDK_INT >= 20 && (windowInsets = instance.lastInsets) != null) {
                        systemWindowInsetTop = windowInsets.getSystemWindowInsetTop();
                        VoIPPiPView.topInset = systemWindowInsetTop;
                        systemWindowInsetBottom = instance.lastInsets.getSystemWindowInsetBottom();
                        VoIPPiPView.bottomInset = systemWindowInsetBottom;
                    }
                }
            }
            instance.callingUserTextureView.renderer.release();
            instance.currentUserTextureView.renderer.release();
            instance.callingUserMiniTextureRenderer.release();
            instance.destroy();
        }
        instance = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroy() {
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().unregisterStateListener(this);
        }
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.webRtcSpeakerAmplitudeEvent);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.voipServiceCreated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeInCallActivity);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.nearEarEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void expandEmoji(boolean z) {
        ViewPropertyAnimator interpolator;
        if (this.emojiLoaded && this.emojiExpanded != z && this.uiVisible) {
            this.emojiExpanded = z;
            this.voIpCoverView.onEmojiExpanded(z);
            if (z) {
                if (SharedConfig.callEncryptionHintDisplayedCount < 2) {
                    SharedConfig.incrementCallEncryptionHintDisplayed(2);
                }
                this.encryptionTooltip.hide();
                AndroidUtilities.cancelRunOnUIThread(this.hideUIRunnable);
                this.hideUiRunnableWaiting = false;
                if (this.callingUserPhotoViewMini.getVisibility() == 0) {
                    this.callingUserPhotoViewMini.animate().setStartDelay(0L).translationY(AndroidUtilities.dp(48.0f)).scaleY(0.1f).scaleX(0.1f).alpha(0.0f).setDuration(200L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                }
                this.hideEmojiLayout.animate().setListener(null).cancel();
                this.hideEmojiLayout.setVisibility(0);
                this.hideEmojiLayout.setAlpha(0.0f);
                this.hideEmojiLayout.setScaleX(0.3f);
                this.hideEmojiLayout.setScaleY(0.3f);
                this.hideEmojiLayout.animate().alpha(1.0f).scaleY(1.0f).scaleX(1.0f).setDuration(340L).setInterpolator(CubicBezierInterpolator.EASE_OUT).start();
                ViewPropertyAnimator translationY = this.emojiLayout.animate().scaleX(1.72f).scaleY(1.72f).translationY(AndroidUtilities.dp(140.0f));
                CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
                translationY.setInterpolator(cubicBezierInterpolator).setDuration(400L).start();
                this.emojiRationalLayout.animate().setListener(null).cancel();
                this.emojiRationalLayout.setVisibility(0);
                this.emojiRationalLayout.setTranslationY(-AndroidUtilities.dp(120.0f));
                this.emojiRationalLayout.setScaleX(0.7f);
                this.emojiRationalLayout.setScaleY(0.7f);
                this.emojiRationalLayout.setAlpha(0.0f);
                interpolator = this.emojiRationalLayout.animate().alpha(1.0f).translationY(0.0f).scaleX(1.0f).scaleY(1.0f).setDuration(400L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.VoIPFragment.16
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        for (BackupImageView backupImageView : VoIPFragment.this.emojiViews) {
                            AnimatedEmojiDrawable animatedEmojiDrawable = backupImageView.animatedEmojiDrawable;
                            if (animatedEmojiDrawable != null && animatedEmojiDrawable.getImageReceiver() != null) {
                                backupImageView.animatedEmojiDrawable.getImageReceiver().setAllowStartAnimation(true);
                                backupImageView.animatedEmojiDrawable.getImageReceiver().startAnimation();
                            }
                        }
                    }
                }).setInterpolator(cubicBezierInterpolator);
            } else {
                if (this.callingUserPhotoViewMini.getVisibility() == 0) {
                    this.callingUserPhotoViewMini.animate().setStartDelay(50L).translationY(0.0f).scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(250L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                }
                this.hideEmojiLayout.animate().setListener(null).cancel();
                ViewPropertyAnimator duration = this.hideEmojiLayout.animate().alpha(0.0f).scaleY(0.3f).scaleX(0.3f).setDuration(230L);
                CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.DEFAULT;
                duration.setInterpolator(cubicBezierInterpolator2).setListener(new HideViewAfterAnimation(this.hideEmojiLayout)).start();
                this.emojiLayout.animate().scaleX(1.0f).scaleY(1.0f).translationY(0.0f).setInterpolator(cubicBezierInterpolator2).setDuration(280L).start();
                this.emojiRationalLayout.animate().setListener(null).cancel();
                interpolator = this.emojiRationalLayout.animate().alpha(0.0f).scaleY(0.7f).scaleX(0.7f).translationY(-AndroidUtilities.dp(120.0f)).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.VoIPFragment.17
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        VoIPFragment.this.startWaitingFoHideUi();
                        for (BackupImageView backupImageView : VoIPFragment.this.emojiViews) {
                            AnimatedEmojiDrawable animatedEmojiDrawable = backupImageView.animatedEmojiDrawable;
                            if (animatedEmojiDrawable != null && animatedEmojiDrawable.getImageReceiver() != null) {
                                backupImageView.animatedEmojiDrawable.getImageReceiver().setAllowStartAnimation(false);
                                backupImageView.animatedEmojiDrawable.getImageReceiver().stopAnimation();
                            }
                        }
                        VoIPFragment.this.emojiRationalLayout.setVisibility(8);
                    }
                }).setDuration(250L).setInterpolator(cubicBezierInterpolator2);
            }
            interpolator.start();
            this.previousState = this.currentState;
            updateViewState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishZoom() {
        if (this.zoomStarted) {
            this.zoomStarted = false;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            this.zoomBackAnimator = ofFloat;
            final float f = this.pinchScale;
            final float f2 = this.pinchTranslationX;
            final float f3 = this.pinchTranslationY;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda39
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    VoIPFragment.this.lambda$finishZoom$15(f, f2, f3, valueAnimator);
                }
            });
            this.zoomBackAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.VoIPFragment.11
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    VoIPFragment voIPFragment = VoIPFragment.this;
                    voIPFragment.zoomBackAnimator = null;
                    voIPFragment.pinchScale = 1.0f;
                    voIPFragment.pinchTranslationX = 0.0f;
                    VoIPFragment.this.pinchTranslationY = 0.0f;
                    VoIPFragment.this.fragmentView.invalidate();
                }
            });
            this.zoomBackAnimator.setDuration(350L);
            this.zoomBackAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.zoomBackAnimator.start();
        }
        this.canZoomGesture = false;
        this.isInPinchToZoomTouchMode = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public VoIPTextureView getFullscreenTextureView() {
        return this.callingUserIsVideo ? this.callingUserTextureView : this.currentUserTextureView;
    }

    public static VoIPFragment getInstance() {
        return instance;
    }

    private void initRenderers() {
        this.currentUserTextureView.renderer.init(VideoCapturerDevice.getEglBase().getEglBaseContext(), new 12());
        this.callingUserTextureView.renderer.init(VideoCapturerDevice.getEglBase().getEglBaseContext(), new 13(), EglBase.CONFIG_PLAIN, new GlRectDrawer());
        this.callingUserMiniTextureRenderer.init(VideoCapturerDevice.getEglBase().getEglBaseContext(), null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkEmojiLoaded$32() {
        if (SharedConfig.callEncryptionHintDisplayedCount < 2) {
            SharedConfig.incrementCallEncryptionHintDisplayed(1);
            this.encryptionTooltip.setTranslationY(this.emojiLayout.getY() + AndroidUtilities.dp(36.0f));
            this.encryptionTooltip.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPiPTransition$18(boolean z, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        float f17 = 1.0f - floatValue;
        this.enterTransitionProgress = f17;
        updateSystemBarColors();
        if (z) {
            float f18 = (f * f17) + (f2 * floatValue);
            this.currentUserCameraFloatingLayout.setScaleX(f18);
            this.currentUserCameraFloatingLayout.setScaleY(f18);
            this.currentUserCameraFloatingLayout.setTranslationX((f3 * f17) + (f4 * floatValue));
            this.currentUserCameraFloatingLayout.setTranslationY((f5 * f17) + (f6 * floatValue));
            this.currentUserCameraFloatingLayout.setCornerRadius((f7 * f17) + (f8 * floatValue));
            this.currentUserCameraFloatingLayout.setAlpha((f9 * f17) + (f10 * floatValue));
        }
        float f19 = (f11 * f17) + (f12 * floatValue);
        this.callingUserTextureView.setScaleX(f19);
        this.callingUserTextureView.setScaleY(f19);
        this.callingUserTextureView.setTranslationX((f13 * f17) + (f14 * floatValue));
        this.callingUserTextureView.setTranslationY((f17 * f15) + (f16 * floatValue));
        this.callingUserTextureView.setRoundCorners(((AndroidUtilities.dp(4.0f) * floatValue) * 1.0f) / f19);
        if (!this.currentUserCameraFloatingLayout.measuredAsFloatingMode) {
            this.currentUserTextureView.setScreenshareMiniProgress(floatValue, false);
        }
        this.windowView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(View view) {
        VoIPService sharedInstance;
        if (this.speakerPhoneIcon.getTag() == null || (sharedInstance = VoIPService.getSharedInstance()) == null) {
            return;
        }
        startWaitingFoHideUi();
        sharedInstance.toggleSpeakerphoneOrShowRouteSheet(this.activity, false, Integer.valueOf(sharedInstance.isBluetoothOn() ? 2 : sharedInstance.isSpeakerphoneOn() ? 0 : 1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$11(View view) {
        if (this.lockOnScreen) {
            return;
        }
        onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$12() {
        this.previousState = this.currentState;
        updateViewState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(float f, boolean z) {
        this.currentUserTextureView.setScreenshareMiniProgress(f, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(View view) {
        if (this.currentUserIsVideo && this.callingUserIsVideo && System.currentTimeMillis() - this.lastContentTapTime > 500) {
            AndroidUtilities.cancelRunOnUIThread(this.hideUIRunnable);
            this.hideUiRunnableWaiting = false;
            this.lastContentTapTime = System.currentTimeMillis();
            this.callingUserMiniFloatingLayout.setRelativePosition(this.currentUserCameraFloatingLayout);
            this.currentUserCameraIsFullscreen = true;
            this.cameraForceExpanded = true;
            this.previousState = this.currentState;
            updateViewState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(View view) {
        if (!this.cameraForceExpanded || System.currentTimeMillis() - this.lastContentTapTime <= 500) {
            return;
        }
        AndroidUtilities.cancelRunOnUIThread(this.hideUIRunnable);
        this.hideUiRunnableWaiting = false;
        this.lastContentTapTime = System.currentTimeMillis();
        this.currentUserCameraFloatingLayout.setRelativePosition(this.callingUserMiniFloatingLayout);
        this.currentUserCameraIsFullscreen = false;
        this.cameraForceExpanded = false;
        this.previousState = this.currentState;
        updateViewState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(View view) {
        if (System.currentTimeMillis() - this.lastContentTapTime < 500) {
            return;
        }
        this.lastContentTapTime = System.currentTimeMillis();
        boolean z = this.emojiExpanded;
        if (!z && this.emojiLoaded) {
            expandEmoji(!z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(View view) {
        if (System.currentTimeMillis() - this.lastContentTapTime < 500) {
            return;
        }
        this.lastContentTapTime = System.currentTimeMillis();
        if (this.emojiLoaded) {
            expandEmoji(!this.emojiExpanded);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishZoom$15(float f, float f2, float f3, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.pinchScale = (f * floatValue) + ((1.0f - floatValue) * 1.0f);
        this.pinchTranslationX = f2 * floatValue;
        this.pinchTranslationY = f3 * floatValue;
        this.fragmentView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.uiVisibilityAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateSystemBarColors();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.hideUiRunnableWaiting = false;
        HintView2 hintView2 = this.tapToVideoTooltip;
        boolean z = hintView2 != null && hintView2.shown();
        if (!this.canHideUI || !this.uiVisible || this.emojiExpanded || z) {
            return;
        }
        this.lastContentTapTime = System.currentTimeMillis();
        showUi(false);
        this.previousState = this.currentState;
        updateViewState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        if (this.currentState == 3) {
            this.callingUserPhotoViewMini.setMute(true, false);
            this.gradientLayout.pause();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onRequestPermissionsResultInternal$39() {
        if (VoIPService.getSharedState() != null) {
            VoIPService.getSharedState().acceptIncomingCall();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestPermissionsResultInternal$40() {
        this.windowView.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSignalBarsCountChanged$4(int i) {
        this.statusTextView.setSignalBarCount(i);
        if (i <= 1) {
            this.gradientLayout.showToBadConnection();
            this.statusTextView.showBadConnection(true, true);
        } else {
            this.gradientLayout.hideBadConnection();
            this.statusTextView.showBadConnection(false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestInlinePermissions$42(AlertDialog alertDialog, int i) {
        VoIPWindowView voIPWindowView = this.windowView;
        if (voIPWindowView != null) {
            voIPWindowView.finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runAcceptCallAnimation$13(int i, int i2, int i3, ViewGroup.MarginLayoutParams marginLayoutParams, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.acceptDeclineView.setTranslationY(i * floatValue);
        int i4 = (int) (i2 - ((i2 + i3) * floatValue));
        marginLayoutParams.leftMargin = i4;
        marginLayoutParams.rightMargin = i4;
        this.acceptDeclineView.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runAcceptCallAnimation$14() {
        int[] iArr = new int[2];
        this.acceptDeclineView.getLocationOnScreen(iArr);
        int i = iArr[0];
        int i2 = iArr[1];
        this.bottomSpeakerBtn.getLocationOnScreen(iArr);
        this.bottomSpeakerBtn.setTranslationX((i - iArr[0]) + AndroidUtilities.dp(42.0f));
        this.bottomSpeakerBtn.setTranslationY((i2 - iArr[1]) + AndroidUtilities.dp(44.0f));
        this.bottomMuteBtn.getLocationOnScreen(iArr);
        this.bottomMuteBtn.setTranslationX((i - iArr[0]) + AndroidUtilities.dp(42.0f));
        this.bottomMuteBtn.setTranslationY((i2 - iArr[1]) + AndroidUtilities.dp(44.0f));
        this.bottomVideoBtn.getLocationOnScreen(iArr);
        this.bottomVideoBtn.setTranslationX((i - iArr[0]) + AndroidUtilities.dp(42.0f));
        this.bottomVideoBtn.setTranslationY((i2 - iArr[1]) + AndroidUtilities.dp(44.0f));
        this.bottomEndCallBtn.getLocationOnScreen(iArr);
        this.bottomEndCallBtn.setTranslationX((((i + this.acceptDeclineView.getWidth()) - iArr[0]) - AndroidUtilities.dp(49.0f)) - AndroidUtilities.dp(60.0f));
        this.bottomEndCallBtn.setTranslationY((i2 - iArr[1]) + AndroidUtilities.dp(44.0f));
        this.bottomEndCallBtn.setAlpha(1.0f);
        this.bottomSpeakerBtn.setAlpha(1.0f);
        this.bottomMuteBtn.setAlpha(1.0f);
        this.bottomVideoBtn.setAlpha(1.0f);
        ViewPropertyAnimator translationX = this.bottomEndCallBtn.animate().setStartDelay(0L).translationY(0.0f).setInterpolator(new LinearInterpolator()).translationX(0.0f);
        long j = NotificationCenter.storyQualityUpdate;
        translationX.setDuration(j).start();
        this.bottomSpeakerBtn.animate().setStartDelay(0L).translationY(0.0f).setInterpolator(new LinearInterpolator()).translationX(0.0f).setDuration(j).start();
        this.bottomMuteBtn.animate().setStartDelay(0L).translationY(0.0f).setInterpolator(new LinearInterpolator()).translationX(0.0f).setDuration(j).start();
        this.bottomVideoBtn.animate().setStartDelay(0L).translationY(0.0f).setInterpolator(new LinearInterpolator()).translationX(0.0f).setDuration(j).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setFrontalCameraAction$38(VoIPService voIPService, VoIpSwitchLayout voIpSwitchLayout, View view) {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null) {
            AndroidUtilities.cancelRunOnUIThread(this.hideUIRunnable);
            this.hideUiRunnableWaiting = false;
            if (this.accessibilityManager.isTouchExplorationEnabled()) {
                view.announceForAccessibility(LocaleController.getString(voIPService.isFrontFaceCamera() ? R.string.AccDescrVoipCamSwitchedToBack : R.string.AccDescrVoipCamSwitchedToFront));
            }
            voIpSwitchLayout.setType(VoIpSwitchLayout.Type.CAMERA, !voIPService.isFrontFaceCamera());
            sharedInstance.switchCamera();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setMicrohoneAction$34(View view) {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null) {
            AndroidUtilities.cancelRunOnUIThread(this.hideUIRunnable);
            this.hideUiRunnableWaiting = false;
            boolean z = !sharedInstance.isMicMute();
            if (this.accessibilityManager.isTouchExplorationEnabled()) {
                view.announceForAccessibility(LocaleController.getString(z ? R.string.AccDescrVoipMicOff : R.string.AccDescrVoipMicOn));
            }
            sharedInstance.setMicMute(z, false, true);
            this.previousState = this.currentState;
            updateViewState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setSpeakerPhoneAction$37(int i, VoIpSwitchLayout voIpSwitchLayout, VoIPService voIPService, View view) {
        if (VoIPService.getSharedInstance() != null) {
            AndroidUtilities.cancelRunOnUIThread(this.hideUIRunnable);
            this.hideUiRunnableWaiting = false;
            VoIPService.getSharedInstance().toggleSpeakerphoneOrShowRouteSheet(this.activity, false, Integer.valueOf(i));
            setSpeakerPhoneAction(voIpSwitchLayout, voIPService, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setVideoAction$35(VoIPService voIPService, AlertDialog alertDialog, int i) {
        voIPService.sharedUIParams.cameraAlertWasShowed = true;
        toggleCameraInput();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setVideoAction$36(final VoIPService voIPService, View view) {
        TL_phone.PhoneCall phoneCall;
        int checkSelfPermission;
        AndroidUtilities.cancelRunOnUIThread(this.hideUIRunnable);
        this.hideUiRunnableWaiting = false;
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            checkSelfPermission = this.activity.checkSelfPermission("android.permission.CAMERA");
            if (checkSelfPermission != 0) {
                this.activity.requestPermissions(new String[]{"android.permission.CAMERA"}, 102);
                return;
            }
        }
        if (i >= 21 || (phoneCall = voIPService.privateCall) == null || phoneCall.video || this.callingUserIsVideo || voIPService.sharedUIParams.cameraAlertWasShowed) {
            toggleCameraInput();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setMessage(LocaleController.getString(R.string.VoipSwitchToVideoCall));
        builder.setPositiveButton(LocaleController.getString(R.string.VoipSwitch), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda44
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i2) {
                VoIPFragment.this.lambda$setVideoAction$35(voIPService, alertDialog, i2);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ WindowInsets lambda$show$3(VoIPFragment voIPFragment, View view, WindowInsets windowInsets) {
        WindowInsets consumeSystemWindowInsets;
        WindowInsets windowInsets2;
        int i = Build.VERSION.SDK_INT;
        if (i >= 21) {
            voIPFragment.setInsets(windowInsets);
        }
        if (i >= 30) {
            windowInsets2 = WindowInsets.CONSUMED;
            return windowInsets2;
        }
        consumeSystemWindowInsets = windowInsets.consumeSystemWindowInsets();
        return consumeSystemWindowInsets;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showErrorDialog$41(DialogInterface dialogInterface) {
        this.windowView.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startTransitionFromPiP$16(Animator animator) {
        VoIPPiPView.switchingToPip = false;
        VoIPPiPView.finish();
        ViewPropertyAnimator duration = this.speakerPhoneIcon.animate().setDuration(150L);
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
        duration.setInterpolator(cubicBezierInterpolator).start();
        this.backIcon.animate().alpha(1.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
        this.emojiLayout.animate().alpha(1.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
        this.statusLayout.animate().alpha(1.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
        this.buttonsLayout.animate().alpha(1.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
        this.bottomShadow.animate().alpha(1.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).start();
        this.topShadow.animate().alpha(1.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).start();
        this.notificationsLayout.animate().alpha(1.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).start();
        animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.VoIPFragment.15
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator2) {
                VoIPFragment.this.notificationsLocker.unlock();
                VoIPFragment.this.currentUserCameraFloatingLayout.setCornerRadius(-1.0f);
                VoIPFragment.this.switchingToPip = false;
                VoIPFragment.this.currentUserCameraFloatingLayout.switchingToPip = false;
                VoIPFragment voIPFragment = VoIPFragment.this;
                voIPFragment.previousState = voIPFragment.currentState;
                VoIPFragment.this.updateViewState();
            }
        });
        animator.setDuration(350L);
        animator.setInterpolator(cubicBezierInterpolator);
        animator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startTransitionFromPiP$17() {
        this.windowView.setAlpha(1.0f);
        this.windowView.invalidate();
        final Animator createPiPTransition = createPiPTransition(true);
        this.backIcon.setAlpha(0.0f);
        this.emojiLayout.setAlpha(0.0f);
        this.statusLayout.setAlpha(0.0f);
        this.buttonsLayout.setAlpha(0.0f);
        this.bottomShadow.setAlpha(0.0f);
        this.topShadow.setAlpha(0.0f);
        this.speakerPhoneIcon.setAlpha(0.0f);
        this.notificationsLayout.setAlpha(0.0f);
        this.currentUserCameraFloatingLayout.switchingToPip = true;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                VoIPFragment.this.lambda$startTransitionFromPiP$16(createPiPTransition);
            }
        }, 32L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateButtons$33(View view) {
        if (VoIPService.getSharedInstance() != null) {
            AndroidUtilities.cancelRunOnUIThread(this.hideUIRunnable);
            this.hideUiRunnableWaiting = false;
            VoIPService.getSharedInstance().hangUp();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$19() {
        this.windowView.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$20(VoIPService voIPService, View view) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                VoIPFragment.this.lambda$updateViewState$19();
            }
        });
        int i = this.selectedRating;
        if (i > 0) {
            voIPService.sendCallRating(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$21(final VoIPService voIPService) {
        this.endCloseLayout.switchToClose(new View.OnClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda41
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VoIPFragment.this.lambda$updateViewState$20(voIPService, view);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$22() {
        this.windowView.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$23(VoIPService voIPService, View view) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                VoIPFragment.this.lambda$updateViewState$22();
            }
        });
        int i = this.selectedRating;
        if (i > 0) {
            voIPService.sendCallRating(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$24(int i) {
        this.selectedRating = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$25() {
        this.windowView.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$26(AlertDialog alertDialog, int i) {
        this.windowView.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$27(boolean[] zArr, AlertDialog alertDialog, int i) {
        zArr[0] = true;
        this.currentState = 17;
        Intent intent = new Intent(this.activity, (Class<?>) VoIPService.class);
        intent.putExtra("user_id", this.callingUser.id);
        intent.putExtra("is_outgoing", true);
        intent.putExtra("start_incall_activity", false);
        intent.putExtra("video_call", false);
        intent.putExtra("can_video_call", false);
        intent.putExtra("account", this.currentAccount);
        try {
            this.activity.startService(intent);
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$28(boolean[] zArr, DialogInterface dialogInterface) {
        if (zArr[0]) {
            return;
        }
        this.windowView.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$29() {
        this.windowView.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$30() {
        this.windowView.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateViewState$31() {
        VoIPFloatingLayout voIPFloatingLayout = this.callingUserMiniFloatingLayout;
        voIPFloatingLayout.isAppearing = false;
        voIPFloatingLayout.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBackPressed() {
        if (this.isFinished || this.switchingToPip) {
            return;
        }
        PrivateVideoPreviewDialogNew privateVideoPreviewDialogNew = this.previewDialog;
        if (privateVideoPreviewDialogNew != null) {
            privateVideoPreviewDialogNew.dismiss(false, false);
            return;
        }
        if (this.callingUserIsVideo && this.currentUserIsVideo && this.cameraForceExpanded) {
            this.cameraForceExpanded = false;
            this.currentUserCameraFloatingLayout.setRelativePosition(this.callingUserMiniFloatingLayout);
            this.currentUserCameraIsFullscreen = false;
            this.previousState = this.currentState;
            updateViewState();
            return;
        }
        if (this.emojiExpanded) {
            expandEmoji(false);
            return;
        }
        if (this.emojiRationalLayout.getVisibility() != 8) {
            return;
        }
        if (!this.canSwitchToPip || this.lockOnScreen) {
            this.windowView.finish();
        } else if (AndroidUtilities.checkInlinePermissions(this.activity)) {
            switchToPip();
        } else {
            requestInlinePermissions();
        }
    }

    public static void onPause() {
        VoIPFragment voIPFragment = instance;
        if (voIPFragment != null) {
            voIPFragment.onPauseInternal();
        }
        if (VoIPPiPView.getInstance() != null) {
            VoIPPiPView.getInstance().onPause();
        }
    }

    public static void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        VoIPFragment voIPFragment = instance;
        if (voIPFragment != null) {
            voIPFragment.onRequestPermissionsResultInternal(i, strArr, iArr);
        }
    }

    private void onRequestPermissionsResultInternal(int i, String[] strArr, int[] iArr) {
        boolean shouldShowRequestPermissionRationale;
        if (i == 101) {
            if (VoIPService.getSharedState() == null) {
                this.windowView.finish();
                return;
            }
            if (iArr.length <= 0 || iArr[0] != 0) {
                shouldShowRequestPermissionRationale = this.activity.shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO");
                if (!shouldShowRequestPermissionRationale) {
                    if (VoIPService.getSharedState() != null) {
                        VoIPService.getSharedState().declineIncomingCall();
                    }
                    VoIPHelper.permissionDenied(this.activity, new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            VoIPFragment.this.lambda$onRequestPermissionsResultInternal$40();
                        }
                    }, i);
                    return;
                }
            } else {
                runAcceptCallAnimation(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        VoIPFragment.lambda$onRequestPermissionsResultInternal$39();
                    }
                });
            }
        }
        if (i == 102) {
            if (VoIPService.getSharedState() == null) {
                this.windowView.finish();
            } else {
                if (iArr.length <= 0 || iArr[0] != 0) {
                    return;
                }
                toggleCameraInput();
            }
        }
    }

    public static void onResume() {
        VoIPFragment voIPFragment = instance;
        if (voIPFragment != null) {
            voIPFragment.onResumeInternal();
        }
        if (VoIPPiPView.getInstance() != null) {
            VoIPPiPView.getInstance().onResume();
        }
    }

    private void requestInlinePermissions() {
        if (Build.VERSION.SDK_INT >= 21) {
            AlertsCreator.createDrawOverlayPermissionDialog(this.activity, new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda11
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i) {
                    VoIPFragment.this.lambda$requestInlinePermissions$42(alertDialog, i);
                }
            }).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runAcceptCallAnimation(final Runnable runnable) {
        if (this.bottomVideoBtn.getVisibility() == 0) {
            this.acceptDeclineView.getLocationOnScreen(new int[2]);
            this.acceptDeclineView.stopAnimations();
            runnable.run();
            return;
        }
        this.bottomEndCallBtn.animate().cancel();
        this.bottomSpeakerBtn.animate().cancel();
        this.bottomMuteBtn.animate().cancel();
        this.bottomVideoBtn.animate().cancel();
        this.acceptDeclineView.getLocationOnScreen(new int[2]);
        this.acceptDeclineView.stopAnimations();
        this.bottomEndCallBtn.setData(R.drawable.calls_decline, -1, -1041108, LocaleController.getString(R.string.VoipEndCall2), false, false);
        this.bottomSpeakerBtn.setType(VoIpSwitchLayout.Type.SPEAKER, false);
        this.bottomMuteBtn.setType(VoIpSwitchLayout.Type.MICRO, false);
        this.bottomVideoBtn.setType(VoIpSwitchLayout.Type.VIDEO, true);
        this.bottomEndCallBtn.setVisibility(0);
        this.bottomSpeakerBtn.setVisibility(0);
        this.bottomMuteBtn.setVisibility(0);
        this.bottomVideoBtn.setVisibility(0);
        this.bottomEndCallBtn.setAlpha(0.0f);
        this.bottomSpeakerBtn.setAlpha(0.0f);
        this.bottomMuteBtn.setAlpha(0.0f);
        this.bottomVideoBtn.setAlpha(0.0f);
        final ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.acceptDeclineView.getLayoutParams();
        final int marginEnd = marginLayoutParams.getMarginEnd();
        AndroidUtilities.dp(52.0f);
        final int dp = AndroidUtilities.dp(24.0f);
        final int dp2 = AndroidUtilities.dp(62.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                VoIPFragment.this.lambda$runAcceptCallAnimation$13(dp2, marginEnd, dp, marginLayoutParams, valueAnimator);
            }
        });
        AcceptDeclineView acceptDeclineView = this.acceptDeclineView;
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(acceptDeclineView, (Property<AcceptDeclineView, Float>) View.SCALE_X, acceptDeclineView.getScaleX(), 1.0f, 1.0f, 1.0f);
        AcceptDeclineView acceptDeclineView2 = this.acceptDeclineView;
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(acceptDeclineView2, (Property<AcceptDeclineView, Float>) View.SCALE_Y, acceptDeclineView2.getScaleY(), 1.0f, 1.0f, 1.0f);
        AcceptDeclineView acceptDeclineView3 = this.acceptDeclineView;
        animatorSet.playTogether(ofFloat, ofFloat2, ofFloat3, ObjectAnimator.ofFloat(acceptDeclineView3, (Property<AcceptDeclineView, Float>) View.ALPHA, acceptDeclineView3.getAlpha(), this.acceptDeclineView.getAlpha(), 0.0f, 0.0f));
        animatorSet.setDuration(400L);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.VoIPFragment.10
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                runnable.run();
                VoIPFragment.this.acceptDeclineView.setScaleX(1.15f);
                VoIPFragment.this.acceptDeclineView.setScaleY(1.15f);
                ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) VoIPFragment.this.acceptDeclineView.getLayoutParams();
                marginLayoutParams2.leftMargin = AndroidUtilities.dp(10.0f);
                marginLayoutParams2.rightMargin = AndroidUtilities.dp(10.0f);
                VoIPFragment.this.acceptDeclineView.setVisibility(8);
            }
        });
        animatorSet.start();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                VoIPFragment.this.lambda$runAcceptCallAnimation$14();
            }
        }, 133L);
    }

    private void setFrontalCameraAction(final VoIpSwitchLayout voIpSwitchLayout, final VoIPService voIPService, boolean z) {
        if (!this.currentUserIsVideo) {
            voIpSwitchLayout.setType(VoIpSwitchLayout.Type.CAMERA, false);
            voIpSwitchLayout.setOnBtnClickedListener(null);
            voIpSwitchLayout.setEnabled(false);
        } else {
            voIpSwitchLayout.setEnabled(true);
            if (voIPService.isFrontFaceCamera()) {
                voIpSwitchLayout.setType(VoIpSwitchLayout.Type.CAMERA, true ^ voIPService.isSwitchingCamera());
            } else {
                voIpSwitchLayout.setType(VoIpSwitchLayout.Type.CAMERA, voIPService.isSwitchingCamera());
            }
            voIpSwitchLayout.setOnBtnClickedListener(new VoIpSwitchLayout.VoIpButtonView.OnBtnClickedListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda40
                @Override // org.telegram.ui.Components.voip.VoIpSwitchLayout.VoIpButtonView.OnBtnClickedListener
                public final void onClicked(View view) {
                    VoIPFragment.this.lambda$setFrontalCameraAction$38(voIPService, voIpSwitchLayout, view);
                }
            });
        }
    }

    private void setInsets(WindowInsets windowInsets) {
        int systemWindowInsetBottom;
        int systemWindowInsetBottom2;
        int systemWindowInsetTop;
        int systemWindowInsetTop2;
        int systemWindowInsetTop3;
        int systemWindowInsetTop4;
        int systemWindowInsetTop5;
        int systemWindowInsetTop6;
        int systemWindowInsetTop7;
        int systemWindowInsetTop8;
        int systemWindowInsetBottom3;
        int systemWindowInsetBottom4;
        int systemWindowInsetBottom5;
        this.lastInsets = windowInsets;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.buttonsLayout.getLayoutParams();
        systemWindowInsetBottom = this.lastInsets.getSystemWindowInsetBottom();
        layoutParams.bottomMargin = systemWindowInsetBottom;
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.acceptDeclineView.getLayoutParams();
        systemWindowInsetBottom2 = this.lastInsets.getSystemWindowInsetBottom();
        layoutParams2.bottomMargin = systemWindowInsetBottom2;
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.backIcon.getLayoutParams();
        systemWindowInsetTop = this.lastInsets.getSystemWindowInsetTop();
        layoutParams3.topMargin = systemWindowInsetTop;
        FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) this.speakerPhoneIcon.getLayoutParams();
        systemWindowInsetTop2 = this.lastInsets.getSystemWindowInsetTop();
        layoutParams4.topMargin = systemWindowInsetTop2;
        FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) this.statusLayout.getLayoutParams();
        int dp = AndroidUtilities.dp(135.0f);
        systemWindowInsetTop3 = this.lastInsets.getSystemWindowInsetTop();
        layoutParams5.topMargin = dp + systemWindowInsetTop3;
        FrameLayout.LayoutParams layoutParams6 = (FrameLayout.LayoutParams) this.emojiLayout.getLayoutParams();
        int dp2 = AndroidUtilities.dp(17.0f);
        systemWindowInsetTop4 = this.lastInsets.getSystemWindowInsetTop();
        layoutParams6.topMargin = dp2 + systemWindowInsetTop4;
        FrameLayout.LayoutParams layoutParams7 = (FrameLayout.LayoutParams) this.callingUserPhotoViewMini.getLayoutParams();
        int dp3 = AndroidUtilities.dp(93.0f);
        systemWindowInsetTop5 = this.lastInsets.getSystemWindowInsetTop();
        layoutParams7.topMargin = dp3 + systemWindowInsetTop5;
        FrameLayout.LayoutParams layoutParams8 = (FrameLayout.LayoutParams) this.hideEmojiLayout.getLayoutParams();
        systemWindowInsetTop6 = this.lastInsets.getSystemWindowInsetTop();
        layoutParams8.topMargin = systemWindowInsetTop6;
        FrameLayout.LayoutParams layoutParams9 = (FrameLayout.LayoutParams) this.emojiRationalLayout.getLayoutParams();
        int dp4 = AndroidUtilities.dp(118.0f);
        systemWindowInsetTop7 = this.lastInsets.getSystemWindowInsetTop();
        layoutParams9.topMargin = dp4 + systemWindowInsetTop7;
        FrameLayout.LayoutParams layoutParams10 = (FrameLayout.LayoutParams) this.rateCallLayout.getLayoutParams();
        int dp5 = AndroidUtilities.dp(380.0f);
        systemWindowInsetTop8 = this.lastInsets.getSystemWindowInsetTop();
        layoutParams10.topMargin = dp5 + systemWindowInsetTop8;
        FrameLayout.LayoutParams layoutParams11 = (FrameLayout.LayoutParams) this.callingUserMiniFloatingLayout.getLayoutParams();
        systemWindowInsetBottom3 = this.lastInsets.getSystemWindowInsetBottom();
        layoutParams11.bottomMargin = systemWindowInsetBottom3;
        FrameLayout.LayoutParams layoutParams12 = (FrameLayout.LayoutParams) this.notificationsLayout.getLayoutParams();
        systemWindowInsetBottom4 = this.lastInsets.getSystemWindowInsetBottom();
        layoutParams12.bottomMargin = systemWindowInsetBottom4;
        this.currentUserCameraFloatingLayout.setInsets(this.lastInsets);
        this.callingUserMiniFloatingLayout.setInsets(this.lastInsets);
        this.fragmentView.requestLayout();
        PrivateVideoPreviewDialogNew privateVideoPreviewDialogNew = this.previewDialog;
        if (privateVideoPreviewDialogNew != null) {
            systemWindowInsetBottom5 = this.lastInsets.getSystemWindowInsetBottom();
            privateVideoPreviewDialogNew.setBottomPadding(systemWindowInsetBottom5);
        }
    }

    private void setMicrohoneAction(VoIpSwitchLayout voIpSwitchLayout, VoIPService voIPService, boolean z) {
        voIpSwitchLayout.setType(VoIpSwitchLayout.Type.MICRO, voIPService.isMicMute());
        this.currentUserCameraFloatingLayout.setMuted(voIPService.isMicMute(), z);
        voIpSwitchLayout.setOnBtnClickedListener(new VoIpSwitchLayout.VoIpButtonView.OnBtnClickedListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda37
            @Override // org.telegram.ui.Components.voip.VoIpSwitchLayout.VoIpButtonView.OnBtnClickedListener
            public final void onClicked(View view) {
                VoIPFragment.this.lambda$setMicrohoneAction$34(view);
            }
        });
    }

    private void setSpeakerPhoneAction(final VoIpSwitchLayout voIpSwitchLayout, final VoIPService voIPService, boolean z) {
        VoipAudioManager voipAudioManager = VoipAudioManager.get();
        final int i = 0;
        if (voIPService.isBluetoothOn()) {
            voIpSwitchLayout.setType(VoIpSwitchLayout.Type.BLUETOOTH, false);
            i = 2;
        } else if (voipAudioManager.isSpeakerphoneOn()) {
            voIpSwitchLayout.setType(VoIpSwitchLayout.Type.SPEAKER, true);
        } else {
            voIpSwitchLayout.setType(VoIpSwitchLayout.Type.SPEAKER, false);
            i = 1;
        }
        voIpSwitchLayout.setEnabled(true);
        voIpSwitchLayout.setOnBtnClickedListener(new VoIpSwitchLayout.VoIpButtonView.OnBtnClickedListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda33
            @Override // org.telegram.ui.Components.voip.VoIpSwitchLayout.VoIpButtonView.OnBtnClickedListener
            public final void onClicked(View view) {
                VoIPFragment.this.lambda$setSpeakerPhoneAction$37(i, voIpSwitchLayout, voIPService, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVideoAction(VoIpSwitchLayout voIpSwitchLayout, final VoIPService voIPService, boolean z) {
        if (!((this.currentUserIsVideo || this.callingUserIsVideo) ? true : voIPService.isVideoAvailable())) {
            voIpSwitchLayout.setType(VoIpSwitchLayout.Type.VIDEO, true);
            voIpSwitchLayout.setOnClickListener(null);
            voIpSwitchLayout.setEnabled(false);
        } else {
            if (this.currentUserIsVideo) {
                voIPService.isScreencast();
                voIpSwitchLayout.setType(VoIpSwitchLayout.Type.VIDEO, false, z);
            } else {
                voIpSwitchLayout.setType(VoIpSwitchLayout.Type.VIDEO, true, z);
            }
            voIpSwitchLayout.setOnBtnClickedListener(new VoIpSwitchLayout.VoIpButtonView.OnBtnClickedListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda42
                @Override // org.telegram.ui.Components.voip.VoIpSwitchLayout.VoIpButtonView.OnBtnClickedListener
                public final void onClicked(View view) {
                    VoIPFragment.this.lambda$setVideoAction$36(voIPService, view);
                }
            });
            voIpSwitchLayout.setEnabled(true);
        }
    }

    public static void show(Activity activity, int i) {
        show(activity, false, i);
    }

    public static void show(Activity activity, boolean z, int i) {
        VoIPFragment voIPFragment = instance;
        if (voIPFragment != null && voIPFragment.windowView.getParent() == null) {
            VoIPFragment voIPFragment2 = instance;
            if (voIPFragment2 != null) {
                voIPFragment2.callingUserTextureView.renderer.release();
                instance.currentUserTextureView.renderer.release();
                instance.callingUserMiniTextureRenderer.release();
                VoIPWindowView voIPWindowView = instance.windowView;
                if (voIPWindowView != null) {
                    voIPWindowView.finishImmediate();
                }
                instance.destroy();
            }
            instance = null;
        }
        if (instance != null || activity.isFinishing()) {
            return;
        }
        boolean z2 = VoIPPiPView.getInstance() != null;
        if (VoIPService.getSharedState() == null || VoIPService.getSharedState().getUser() == null) {
            return;
        }
        final VoIPFragment voIPFragment3 = new VoIPFragment(i);
        voIPFragment3.activity = activity;
        instance = voIPFragment3;
        VoIPWindowView voIPWindowView2 = new VoIPWindowView(activity, !z2) { // from class: org.telegram.ui.VoIPFragment.1
            private final Path clipPath = new Path();
            private final RectF rectF = new RectF();

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                if (!voIPFragment3.switchingToPip || getAlpha() == 0.0f) {
                    super.dispatchDraw(canvas);
                    return;
                }
                float width = voIPFragment3.callingUserTextureView.getWidth() * voIPFragment3.callingUserTextureView.getScaleX();
                float height = voIPFragment3.callingUserTextureView.getHeight() * voIPFragment3.callingUserTextureView.getScaleY();
                float x = voIPFragment3.callingUserTextureView.getX() + ((voIPFragment3.callingUserTextureView.getWidth() - width) / 2.0f);
                float y = voIPFragment3.callingUserTextureView.getY() + ((voIPFragment3.callingUserTextureView.getHeight() - height) / 2.0f);
                canvas.save();
                this.clipPath.rewind();
                this.rectF.set(x, y, width + x, height + y);
                float dp = AndroidUtilities.dp(4.0f);
                this.clipPath.addRoundRect(this.rectF, dp, dp, Path.Direction.CW);
                this.clipPath.close();
                canvas.clipPath(this.clipPath);
                super.dispatchDraw(canvas);
                canvas.restore();
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                VoIPServiceState sharedState;
                if (voIPFragment3.isFinished || voIPFragment3.switchingToPip) {
                    return false;
                }
                int keyCode = keyEvent.getKeyCode();
                if (keyCode == 4 && keyEvent.getAction() == 1 && !voIPFragment3.lockOnScreen) {
                    voIPFragment3.onBackPressed();
                    return true;
                }
                if ((keyCode != 25 && keyCode != 24) || voIPFragment3.currentState != 15 || (sharedState = VoIPService.getSharedState()) == null) {
                    return super.dispatchKeyEvent(keyEvent);
                }
                sharedState.stopRinging();
                return true;
            }
        };
        instance.deviceIsLocked = ((KeyguardManager) activity.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
        PowerManager powerManager = (PowerManager) activity.getSystemService("power");
        int i2 = Build.VERSION.SDK_INT;
        boolean isInteractive = i2 >= 20 ? powerManager.isInteractive() : powerManager.isScreenOn();
        VoIPFragment voIPFragment4 = instance;
        voIPFragment4.screenWasWakeup = true ^ isInteractive;
        voIPWindowView2.setLockOnScreen(voIPFragment4.deviceIsLocked);
        voIPFragment3.windowView = voIPWindowView2;
        if (i2 >= 20) {
            voIPWindowView2.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda3
                @Override // android.view.View.OnApplyWindowInsetsListener
                public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    WindowInsets lambda$show$3;
                    lambda$show$3 = VoIPFragment.lambda$show$3(VoIPFragment.this, view, windowInsets);
                    return lambda$show$3;
                }
            });
        }
        WindowManager windowManager = (WindowManager) activity.getSystemService("window");
        WindowManager.LayoutParams createWindowLayoutParams = voIPWindowView2.createWindowLayoutParams();
        if (z) {
            createWindowLayoutParams.type = i2 >= 26 ? 2038 : 2003;
        }
        windowManager.addView(voIPWindowView2, createWindowLayoutParams);
        voIPWindowView2.addView(voIPFragment3.createView(activity));
        if (z2) {
            voIPFragment3.enterTransitionProgress = 0.0f;
            voIPFragment3.startTransitionFromPiP();
        } else {
            voIPFragment3.enterTransitionProgress = 1.0f;
            voIPFragment3.updateSystemBarColors();
        }
    }

    private void showAcceptDeclineView(boolean z, boolean z2) {
        if (z2) {
            if (z && this.acceptDeclineView.getTag() == null) {
                this.acceptDeclineView.animate().setListener(null).cancel();
                if (this.acceptDeclineView.getVisibility() == 8) {
                    this.acceptDeclineView.setVisibility(0);
                    this.acceptDeclineView.setAlpha(0.0f);
                }
                this.acceptDeclineView.animate().alpha(1.0f);
            }
            if (!z && this.acceptDeclineView.getTag() != null) {
                this.acceptDeclineView.animate().setListener(null).cancel();
                this.acceptDeclineView.animate().setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.VoIPFragment.22
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        VoIPFragment.this.acceptDeclineView.setVisibility(8);
                    }
                }).alpha(0.0f);
            }
        } else {
            this.acceptDeclineView.setVisibility(z ? 0 : 8);
        }
        this.acceptDeclineView.setEnabled(z);
        this.acceptDeclineView.setTag(z ? 1 : null);
    }

    private void showCallingUserAvatarMini(boolean z, boolean z2) {
        ViewPropertyAnimator listener;
        boolean z3 = (this.currentUserIsVideo || this.callingUserIsVideo) ? false : true;
        if (!z) {
            this.callingUserPhotoViewMini.animate().setListener(null).cancel();
            this.callingUserPhotoViewMini.setTranslationY(0.0f);
            this.callingUserPhotoViewMini.setAlpha(1.0f);
            this.callingUserPhotoViewMini.setScaleX(1.0f);
            this.callingUserPhotoViewMini.setScaleY(1.0f);
            this.callingUserPhotoViewMini.setVisibility(z3 ? 0 : 8);
        } else if (z3 && this.callingUserPhotoViewMini.getTag() == null) {
            this.callingUserPhotoViewMini.animate().setListener(null).cancel();
            this.callingUserPhotoViewMini.setVisibility(0);
            if (!this.emojiExpanded) {
                this.callingUserPhotoViewMini.setAlpha(0.0f);
                ImageWithWavesView imageWithWavesView = this.callingUserPhotoViewMini;
                if (!z2) {
                    imageWithWavesView.setTranslationY(-AndroidUtilities.dp(135.0f));
                    imageWithWavesView = this.callingUserPhotoViewMini;
                }
                listener = imageWithWavesView.animate().alpha(1.0f).translationY(0.0f).scaleY(1.0f).scaleX(1.0f).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT);
                listener.start();
            } else if (z2) {
                this.callingUserPhotoViewMini.setAlpha(0.0f);
                this.callingUserPhotoViewMini.setTranslationY(AndroidUtilities.dp(48.0f));
                this.callingUserPhotoViewMini.setScaleX(0.1f);
                this.callingUserPhotoViewMini.setScaleY(0.1f);
            }
        } else if (!z3 && this.callingUserPhotoViewMini.getTag() != null) {
            this.callingUserPhotoViewMini.animate().setListener(null).cancel();
            this.callingUserPhotoViewMini.setTranslationY(0.0f);
            listener = this.callingUserPhotoViewMini.animate().alpha(0.0f).setDuration(150L).scaleX(0.1f).scaleY(0.1f).setInterpolator(CubicBezierInterpolator.DEFAULT).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.VoIPFragment.21
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    VoIPFragment.this.callingUserPhotoViewMini.setVisibility(8);
                }
            });
            listener.start();
        }
        this.callingUserPhotoViewMini.setTag(z3 ? 1 : null);
    }

    private void showErrorDialog(CharSequence charSequence) {
        if (this.activity.isFinishing()) {
            return;
        }
        AlertDialog show = new DarkAlertDialog.Builder(this.activity).setTitle(LocaleController.getString(R.string.VoipFailed)).setMessage(charSequence).setPositiveButton(LocaleController.getString(R.string.OK), null).show();
        show.setCanceledOnTouchOutside(true);
        show.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda38
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                VoIPFragment.this.lambda$showErrorDialog$41(dialogInterface);
            }
        });
    }

    private void showFloatingLayout(int i, boolean z) {
        Animator animator;
        if (this.currentUserCameraFloatingLayout.getTag() == null || ((Integer) this.currentUserCameraFloatingLayout.getTag()).intValue() != 2) {
            this.currentUserCameraFloatingLayout.setUiVisible(this.uiVisible);
        }
        if (!z && (animator = this.cameraShowingAnimator) != null) {
            animator.removeAllListeners();
            this.cameraShowingAnimator.cancel();
        }
        if (i != 0) {
            boolean z2 = (this.currentUserCameraFloatingLayout.getTag() == null || ((Integer) this.currentUserCameraFloatingLayout.getTag()).intValue() == 0) ? false : z;
            if (!z) {
                this.currentUserCameraFloatingLayout.setVisibility(0);
            } else if (this.currentUserCameraFloatingLayout.getTag() != null && ((Integer) this.currentUserCameraFloatingLayout.getTag()).intValue() == 0) {
                if (this.currentUserCameraFloatingLayout.getVisibility() == 8) {
                    this.currentUserCameraFloatingLayout.setAlpha(0.0f);
                    this.currentUserCameraFloatingLayout.setScaleX(0.7f);
                    this.currentUserCameraFloatingLayout.setScaleY(0.7f);
                    this.currentUserCameraFloatingLayout.setVisibility(0);
                }
                Animator animator2 = this.cameraShowingAnimator;
                if (animator2 != null) {
                    animator2.removeAllListeners();
                    this.cameraShowingAnimator.cancel();
                }
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ObjectAnimator.ofFloat(this.currentUserCameraFloatingLayout, (Property<VoIPFloatingLayout, Float>) View.ALPHA, 0.0f, 1.0f), ObjectAnimator.ofFloat(this.currentUserCameraFloatingLayout, (Property<VoIPFloatingLayout, Float>) View.SCALE_X, 0.7f, 1.0f), ObjectAnimator.ofFloat(this.currentUserCameraFloatingLayout, (Property<VoIPFloatingLayout, Float>) View.SCALE_Y, 0.7f, 1.0f));
                this.cameraShowingAnimator = animatorSet;
                animatorSet.setDuration(150L).start();
            }
            if (this.currentUserCameraFloatingLayout.getTag() == null || ((Integer) this.currentUserCameraFloatingLayout.getTag()).intValue() != 2) {
                VoIPFloatingLayout voIPFloatingLayout = this.currentUserCameraFloatingLayout;
                if (voIPFloatingLayout.relativePositionToSetX < 0.0f) {
                    voIPFloatingLayout.setRelativePosition(1.0f, 1.0f);
                    this.currentUserCameraIsFullscreen = true;
                }
            }
            this.currentUserCameraFloatingLayout.setFloatingMode(i == 2, z2);
            this.currentUserCameraIsFullscreen = i != 2;
        } else if (!z) {
            this.currentUserCameraFloatingLayout.setVisibility(8);
        } else if (this.currentUserCameraFloatingLayout.getTag() != null && ((Integer) this.currentUserCameraFloatingLayout.getTag()).intValue() != 0) {
            Animator animator3 = this.cameraShowingAnimator;
            if (animator3 != null) {
                animator3.removeAllListeners();
                this.cameraShowingAnimator.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            VoIPFloatingLayout voIPFloatingLayout2 = this.currentUserCameraFloatingLayout;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(voIPFloatingLayout2, (Property<VoIPFloatingLayout, Float>) View.ALPHA, voIPFloatingLayout2.getAlpha(), 0.0f));
            if (this.currentUserCameraFloatingLayout.getTag() != null && ((Integer) this.currentUserCameraFloatingLayout.getTag()).intValue() == 2) {
                VoIPFloatingLayout voIPFloatingLayout3 = this.currentUserCameraFloatingLayout;
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(voIPFloatingLayout3, (Property<VoIPFloatingLayout, Float>) View.SCALE_X, voIPFloatingLayout3.getScaleX(), 0.7f);
                VoIPFloatingLayout voIPFloatingLayout4 = this.currentUserCameraFloatingLayout;
                animatorSet2.playTogether(ofFloat, ObjectAnimator.ofFloat(voIPFloatingLayout4, (Property<VoIPFloatingLayout, Float>) View.SCALE_Y, voIPFloatingLayout4.getScaleX(), 0.7f));
            }
            this.cameraShowingAnimator = animatorSet2;
            animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.VoIPFragment.20
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator4) {
                    VoIPFragment.this.currentUserCameraFloatingLayout.setTranslationX(0.0f);
                    VoIPFragment.this.currentUserCameraFloatingLayout.setTranslationY(0.0f);
                    VoIPFragment.this.currentUserCameraFloatingLayout.setScaleY(1.0f);
                    VoIPFragment.this.currentUserCameraFloatingLayout.setScaleX(1.0f);
                    VoIPFragment.this.currentUserCameraFloatingLayout.setVisibility(8);
                }
            });
            this.cameraShowingAnimator.setDuration(250L).setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.cameraShowingAnimator.setStartDelay(50L);
            this.cameraShowingAnimator.start();
        }
        this.currentUserCameraFloatingLayout.setTag(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUi(boolean z) {
        int i;
        ValueAnimator valueAnimator = this.uiVisibilityAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (z || !this.uiVisible) {
            if (z && !this.uiVisible) {
                this.tapToVideoTooltip.hide();
                this.encryptionTooltip.hide();
                ViewPropertyAnimator translationY = this.callingUserTitle.animate().alpha(1.0f).setDuration(150L).translationY(0.0f);
                CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
                translationY.setInterpolator(cubicBezierInterpolator).start();
                this.statusTextView.animate().alpha(1.0f).setDuration(150L).translationY(0.0f).setInterpolator(cubicBezierInterpolator).start();
                this.speakerPhoneIcon.animate().alpha(1.0f).translationY(0.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
                this.backIcon.animate().alpha(1.0f).translationY(0.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
                this.emojiLayout.animate().alpha(1.0f).translationY(0.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
                this.buttonsLayout.animate().alpha(1.0f).translationY(0.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
                this.bottomShadow.animate().alpha(1.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
                this.topShadow.animate().alpha(1.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.uiVisibilityAlpha, 1.0f);
                this.uiVisibilityAnimator = ofFloat;
                ofFloat.addUpdateListener(this.statusbarAnimatorListener);
                this.uiVisibilityAnimator.setDuration(150L).setInterpolator(cubicBezierInterpolator);
                this.uiVisibilityAnimator.start();
                this.buttonsLayout.setEnabled(true);
            }
            i = 0;
        } else {
            ViewPropertyAnimator duration = this.speakerPhoneIcon.animate().alpha(0.0f).translationY(-AndroidUtilities.dp(10.0f)).setDuration(150L);
            CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.DEFAULT;
            duration.setInterpolator(cubicBezierInterpolator2).start();
            this.backIcon.animate().alpha(0.0f).translationY(-AndroidUtilities.dp(10.0f)).setDuration(150L).setInterpolator(cubicBezierInterpolator2).start();
            this.emojiLayout.animate().alpha(0.0f).translationY(-AndroidUtilities.dp(10.0f)).setDuration(150L).setInterpolator(cubicBezierInterpolator2).start();
            this.callingUserTitle.animate().alpha(0.0f).setDuration(150L).translationY(-AndroidUtilities.dp(10.0f)).setInterpolator(cubicBezierInterpolator2).start();
            this.statusTextView.animate().alpha(0.0f).setDuration(150L).translationY(-AndroidUtilities.dp(10.0f)).setInterpolator(cubicBezierInterpolator2).start();
            this.buttonsLayout.animate().alpha(0.0f).translationY(AndroidUtilities.dp(10.0f)).setDuration(150L).setInterpolator(cubicBezierInterpolator2).start();
            this.bottomShadow.animate().alpha(0.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator2).start();
            this.topShadow.animate().alpha(0.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator2).start();
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.uiVisibilityAlpha, 0.0f);
            this.uiVisibilityAnimator = ofFloat2;
            ofFloat2.addUpdateListener(this.statusbarAnimatorListener);
            this.uiVisibilityAnimator.setDuration(150L).setInterpolator(cubicBezierInterpolator2);
            this.uiVisibilityAnimator.start();
            AndroidUtilities.cancelRunOnUIThread(this.hideUIRunnable);
            this.hideUiRunnableWaiting = false;
            this.buttonsLayout.setEnabled(false);
            this.encryptionTooltip.hide();
            i = 150;
        }
        this.uiVisible = z;
        this.windowView.requestFullscreen(!z);
        this.notificationsLayout.animate().translationY((-AndroidUtilities.dp(16.0f)) - (this.uiVisible ? AndroidUtilities.dp(80.0f) : 0)).setDuration(150L).setStartDelay(i).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startWaitingFoHideUi() {
        if (VoIPService.getSharedInstance() != null) {
            AndroidUtilities.cancelRunOnUIThread(this.hideUIRunnable);
            this.hideUiRunnableWaiting = false;
            if (this.canHideUI && this.uiVisible) {
                AndroidUtilities.runOnUIThread(this.hideUIRunnable, 3000L);
                this.hideUiRunnableWaiting = true;
            }
        }
    }

    private void toggleCameraInput() {
        int systemWindowInsetBottom;
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null) {
            if (this.accessibilityManager.isTouchExplorationEnabled()) {
                this.fragmentView.announceForAccessibility(LocaleController.getString(!this.currentUserIsVideo ? R.string.AccDescrVoipCamOn : R.string.AccDescrVoipCamOff));
            }
            if (this.currentUserIsVideo) {
                this.currentUserTextureView.saveCameraLastBitmap();
                sharedInstance.setVideoState(false, 0);
                if (Build.VERSION.SDK_INT >= 21) {
                    sharedInstance.clearCamera();
                }
            } else {
                if (Build.VERSION.SDK_INT >= 21) {
                    if (this.previewDialog == null) {
                        sharedInstance.createCaptureDevice(false);
                        if (!sharedInstance.isFrontFaceCamera()) {
                            sharedInstance.switchCamera();
                        }
                        this.windowView.setLockOnScreen(true);
                        this.bottomVideoBtn.getLocationOnScreen(new int[2]);
                        PrivateVideoPreviewDialogNew privateVideoPreviewDialogNew = new PrivateVideoPreviewDialogNew(this.fragmentView.getContext(), r0[0], r0[1]) { // from class: org.telegram.ui.VoIPFragment.24
                            @Override // org.telegram.ui.Components.voip.PrivateVideoPreviewDialogNew
                            protected void afterOpened() {
                                VoIPFragment.this.gradientLayout.lockDrawing = true;
                                VoIPFragment.this.gradientLayout.invalidate();
                            }

                            @Override // org.telegram.ui.Components.voip.PrivateVideoPreviewDialogNew
                            protected void beforeClosed() {
                                VoIPFragment.this.gradientLayout.lockDrawing = false;
                                VoIPFragment.this.gradientLayout.invalidate();
                            }

                            @Override // org.telegram.ui.Components.voip.PrivateVideoPreviewDialogNew
                            protected int[] getFloatingViewLocation() {
                                int[] iArr = new int[2];
                                VoIPFragment.this.currentUserCameraFloatingLayout.getLocationOnScreen(iArr);
                                return new int[]{iArr[0], iArr[1], VoIPFragment.this.currentUserCameraFloatingLayout.getMeasuredWidth()};
                            }

                            @Override // org.telegram.ui.Components.voip.PrivateVideoPreviewDialogNew
                            protected boolean isHasVideoOnMainScreen() {
                                return VoIPFragment.this.callingUserIsVideo;
                            }

                            @Override // org.telegram.ui.Components.voip.PrivateVideoPreviewDialogNew
                            public void onDismiss(boolean z, boolean z2) {
                                VoIPFragment.this.previewDialog = null;
                                VoIPService sharedInstance2 = VoIPService.getSharedInstance();
                                VoIPFragment.this.windowView.setLockOnScreen(false);
                                if (z2) {
                                    VoIPFragment.this.currentUserIsVideo = true;
                                    if (sharedInstance2 != null && !z) {
                                        sharedInstance2.requestVideoCall(false);
                                        sharedInstance2.setVideoState(false, 2);
                                        sharedInstance2.switchToSpeaker();
                                    }
                                    if (sharedInstance2 != null) {
                                        VoIPFragment voIPFragment = VoIPFragment.this;
                                        voIPFragment.setVideoAction(voIPFragment.bottomVideoBtn, sharedInstance2, true);
                                    }
                                } else if (sharedInstance2 != null) {
                                    sharedInstance2.setVideoState(false, 0);
                                }
                                VoIPFragment voIPFragment2 = VoIPFragment.this;
                                voIPFragment2.previousState = voIPFragment2.currentState;
                                VoIPFragment.this.updateViewState();
                            }
                        };
                        this.previewDialog = privateVideoPreviewDialogNew;
                        WindowInsets windowInsets = this.lastInsets;
                        if (windowInsets != null) {
                            systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
                            privateVideoPreviewDialogNew.setBottomPadding(systemWindowInsetBottom);
                        }
                        this.fragmentView.addView(this.previewDialog);
                        return;
                    }
                    return;
                }
                this.currentUserIsVideo = true;
                if (!sharedInstance.isSpeakerphoneOn()) {
                    VoIPService.getSharedInstance().toggleSpeakerphoneOrShowRouteSheet(this.activity, false);
                }
                sharedInstance.requestVideoCall(false);
                sharedInstance.setVideoState(false, 2);
            }
            this.previousState = this.currentState;
            updateViewState();
        }
    }

    private void updateButtons(boolean z) {
        ViewPropertyAnimator alpha;
        ViewPropertyAnimator alpha2;
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null) {
            return;
        }
        if (z) {
            TransitionSet transitionSet = new TransitionSet();
            Transition duration = new Visibility() { // from class: org.telegram.ui.VoIPFragment.23
                @Override // android.transition.Visibility
                public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
                    ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat((Property<?, Float>) View.TRANSLATION_Y, AndroidUtilities.dp(100.0f), 0.0f), PropertyValuesHolder.ofFloat((Property<?, Float>) View.SCALE_Y, 0.0f, 1.0f), PropertyValuesHolder.ofFloat((Property<?, Float>) View.SCALE_X, 0.0f, 1.0f));
                    if (view instanceof VoIPToggleButton) {
                        view.setTranslationY(AndroidUtilities.dp(100.0f));
                        view.setScaleX(0.0f);
                        view.setScaleY(0.0f);
                        ofPropertyValuesHolder.setStartDelay(((VoIPToggleButton) view).animationDelay);
                    }
                    if (view instanceof VoIpSwitchLayout) {
                        view.setTranslationY(AndroidUtilities.dp(100.0f));
                        view.setScaleX(0.0f);
                        view.setScaleY(0.0f);
                        ofPropertyValuesHolder.setStartDelay(((VoIpSwitchLayout) view).animationDelay);
                    }
                    return ofPropertyValuesHolder;
                }

                @Override // android.transition.Visibility
                public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
                    return ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat((Property<?, Float>) View.TRANSLATION_Y, view.getTranslationY(), AndroidUtilities.dp(100.0f)), PropertyValuesHolder.ofFloat((Property<?, Float>) View.SCALE_Y, view.getScaleY(), 0.0f), PropertyValuesHolder.ofFloat((Property<?, Float>) View.SCALE_X, view.getScaleX(), 0.0f));
                }
            }.setDuration(250L);
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            transitionSet.addTransition(duration.setInterpolator(cubicBezierInterpolator)).addTransition(new ChangeBounds().setDuration(250L).setInterpolator(cubicBezierInterpolator));
            transitionSet.excludeChildren(VoIPToggleButton.class, true);
            transitionSet.excludeChildren(VoIpSwitchLayout.class, true);
            TransitionManager.beginDelayedTransition(this.buttonsLayout, transitionSet);
        }
        int i = this.currentState;
        if (i == 11) {
            this.bottomSpeakerBtn.setVisibility(8);
            this.bottomVideoBtn.setVisibility(8);
            this.bottomMuteBtn.setVisibility(8);
            this.bottomEndCallBtn.setVisibility(8);
            return;
        }
        int i2 = 0;
        if (i == 15 || i == 17) {
            TL_phone.PhoneCall phoneCall = sharedInstance.privateCall;
            if (phoneCall != null && phoneCall.video && i == 15) {
                if (sharedInstance.isScreencast() || !(this.currentUserIsVideo || this.callingUserIsVideo)) {
                    setSpeakerPhoneAction(this.bottomSpeakerBtn, sharedInstance, z);
                    alpha = this.speakerPhoneIcon.animate().alpha(0.0f);
                } else {
                    setFrontalCameraAction(this.bottomSpeakerBtn, sharedInstance, z);
                    if (this.uiVisible) {
                        alpha = this.speakerPhoneIcon.animate().alpha(1.0f);
                    }
                    setVideoAction(this.bottomVideoBtn, sharedInstance, false);
                    setMicrohoneAction(this.bottomMuteBtn, sharedInstance, z);
                }
                alpha.start();
                setVideoAction(this.bottomVideoBtn, sharedInstance, false);
                setMicrohoneAction(this.bottomMuteBtn, sharedInstance, z);
            } else {
                this.bottomSpeakerBtn.setVisibility(8);
                this.bottomVideoBtn.setVisibility(8);
                this.bottomMuteBtn.setVisibility(8);
            }
            this.bottomEndCallBtn.setVisibility(8);
        } else {
            if (instance == null) {
                return;
            }
            if (sharedInstance.isScreencast() || !(this.currentUserIsVideo || this.callingUserIsVideo)) {
                setSpeakerPhoneAction(this.bottomSpeakerBtn, sharedInstance, z);
                this.speakerPhoneIcon.setTag(null);
                alpha2 = this.speakerPhoneIcon.animate().alpha(0.0f);
            } else {
                setFrontalCameraAction(this.bottomSpeakerBtn, sharedInstance, z);
                if (this.uiVisible) {
                    this.speakerPhoneIcon.setTag(1);
                    alpha2 = this.speakerPhoneIcon.animate().alpha(1.0f);
                }
                setVideoAction(this.bottomVideoBtn, sharedInstance, false);
                setMicrohoneAction(this.bottomMuteBtn, sharedInstance, z);
                this.bottomEndCallBtn.setData(R.drawable.calls_decline, -1, -1041108, LocaleController.getString(R.string.VoipEndCall2), false, z);
                this.bottomEndCallBtn.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda23
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        VoIPFragment.this.lambda$updateButtons$33(view);
                    }
                });
            }
            alpha2.start();
            setVideoAction(this.bottomVideoBtn, sharedInstance, false);
            setMicrohoneAction(this.bottomMuteBtn, sharedInstance, z);
            this.bottomEndCallBtn.setData(R.drawable.calls_decline, -1, -1041108, LocaleController.getString(R.string.VoipEndCall2), false, z);
            this.bottomEndCallBtn.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda23
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    VoIPFragment.this.lambda$updateButtons$33(view);
                }
            });
        }
        if (this.bottomSpeakerBtn.getVisibility() == 0) {
            this.bottomSpeakerBtn.animationDelay = 0;
            i2 = 16;
        }
        if (this.bottomVideoBtn.getVisibility() == 0) {
            this.bottomVideoBtn.animationDelay = i2;
            i2 += 16;
        }
        if (this.bottomMuteBtn.getVisibility() == 0) {
            this.bottomMuteBtn.animationDelay = i2;
            i2 += 16;
        }
        if (this.bottomEndCallBtn.getVisibility() == 0) {
            this.bottomEndCallBtn.animationDelay = i2;
        }
        updateSpeakerPhoneIcon();
    }

    private void updateKeyView(boolean z) {
        VoIPService sharedInstance;
        byte[] bArr;
        if (this.emojiLoaded || (sharedInstance = VoIPService.getSharedInstance()) == null) {
            return;
        }
        int i = 0;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(sharedInstance.getEncryptionKey());
            byteArrayOutputStream.write(sharedInstance.getGA());
            bArr = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            FileLog.e((Throwable) e, false);
            bArr = null;
        }
        if (bArr == null) {
            return;
        }
        String[] emojifyForCall = EncryptionKeyEmojifier.emojifyForCall(Utilities.computeSHA256(bArr, 0, bArr.length));
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < 4; i2++) {
            Emoji.preloadEmoji(emojifyForCall[i2]);
            Emoji.EmojiDrawable emojiDrawable = Emoji.getEmojiDrawable(emojifyForCall[i2]);
            if (emojiDrawable != null) {
                emojiDrawable.setBounds(0, 0, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
                emojiDrawable.preload();
                int[] iArr = new int[1];
                TextPaint textPaint = new TextPaint(1);
                textPaint.setTextSize(AndroidUtilities.dp(28.0f));
                TLRPC.Document replaceEmojiToLottieFrame = replaceEmojiToLottieFrame(Emoji.replaceEmoji((CharSequence) emojifyForCall[i2], textPaint.getFontMetricsInt(), false, iArr), iArr);
                arrayList2.add(emojiDrawable);
                if (replaceEmojiToLottieFrame != null) {
                    arrayList.add(replaceEmojiToLottieFrame);
                }
                this.emojiViews[i2].setVisibility(8);
            }
            this.emojiDrawables[i2] = emojiDrawable;
        }
        if (arrayList.size() == 4) {
            while (i < arrayList.size()) {
                this.emojiViews[i].setAnimatedEmojiDrawable(new AnimatedEmojiDrawable(17, this.currentAccount, (TLRPC.Document) arrayList.get(i)));
                this.emojiViews[i].getImageReceiver().clearImage();
                i++;
            }
        } else {
            while (i < arrayList2.size()) {
                this.emojiViews[i].setImageDrawable((Drawable) arrayList2.get(i));
                i++;
            }
        }
        checkEmojiLoaded(z);
    }

    private void updateSpeakerPhoneIcon() {
        ImageView imageView;
        int i;
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null) {
            return;
        }
        VoipAudioManager voipAudioManager = VoipAudioManager.get();
        if (sharedInstance.isBluetoothOn()) {
            imageView = this.speakerPhoneIcon;
            i = R.drawable.calls_bluetooth;
        } else if (voipAudioManager.isSpeakerphoneOn()) {
            imageView = this.speakerPhoneIcon;
            i = R.drawable.calls_speaker;
        } else if (sharedInstance.isHeadsetPlugged()) {
            imageView = this.speakerPhoneIcon;
            i = R.drawable.calls_menu_headset;
        } else {
            imageView = this.speakerPhoneIcon;
            i = R.drawable.calls_menu_phone;
        }
        imageView.setImageResource(i);
    }

    private void updateSystemBarColors() {
        this.overlayPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (this.uiVisibilityAlpha * 102.0f * this.enterTransitionProgress)));
        this.overlayBottomPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (((this.fillNaviagtionBarValue * 0.5f) + 0.5f) * 255.0f * this.enterTransitionProgress)));
        ViewGroup viewGroup = this.fragmentView;
        if (viewGroup != null) {
            viewGroup.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x043a, code lost:
    
        if (r20.previousState != 5) goto L112;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x058c  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x064b  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x06b0  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x075a  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0458 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:200:0x0868  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x08b7  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x08b0  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0459  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x08de  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x09c1  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0a00  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0a1b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0a26  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x0a31  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0a4f  */
    /* JADX WARN: Removed duplicated region for block: B:269:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:270:0x0a3f  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x08f2  */
    /* JADX WARN: Removed duplicated region for block: B:288:0x072b  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x060b  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x052d  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x052a  */
    /* JADX WARN: Type inference failed for: r9v21 */
    /* JADX WARN: Type inference failed for: r9v23, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r9v39 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateViewState() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        int i;
        boolean z5;
        boolean z6;
        int dp;
        boolean z7;
        boolean z8;
        int i2;
        boolean z9;
        boolean z10;
        View view;
        int i3;
        int i4;
        TL_phone.PhoneCall phoneCall;
        VoIPService.SharedUIParams sharedUIParams;
        long j;
        Layout layout;
        int lineCount;
        ViewPropertyAnimator alpha;
        Runnable runnable;
        CharSequence string;
        String formatString;
        ?? r9;
        WindowInsets windowInsets;
        int systemWindowInsetBottom;
        VoIPStatusTextView voIPStatusTextView;
        int i5;
        if (this.isFinished || this.switchingToPip) {
            return;
        }
        this.lockOnScreen = false;
        boolean z11 = this.previousState != -1;
        VoIPServiceState sharedState = VoIPService.getSharedState();
        final VoIPService sharedInstance = VoIPService.getSharedInstance();
        int i6 = this.currentState;
        if (i6 == 1 || i6 == 2) {
            z = false;
        } else {
            if (i6 != 3) {
                if (i6 == 4) {
                    VoIPStatusTextView voIPStatusTextView2 = this.statusTextView;
                    int i7 = R.string.VoipFailed;
                    voIPStatusTextView2.setText(LocaleController.getString(i7), false, z11);
                    VoIPService sharedInstance2 = VoIPService.getSharedInstance();
                    String lastError = sharedInstance2 != null ? sharedInstance2.getLastError() : Instance.ERROR_UNKNOWN;
                    if (TextUtils.equals(lastError, Instance.ERROR_UNKNOWN)) {
                        runnable = new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda22
                            @Override // java.lang.Runnable
                            public final void run() {
                                VoIPFragment.this.lambda$updateViewState$30();
                            }
                        };
                    } else {
                        if (TextUtils.equals(lastError, Instance.ERROR_INCOMPATIBLE)) {
                            TLRPC.User user = this.callingUser;
                            formatString = LocaleController.formatString("VoipPeerIncompatible", R.string.VoipPeerIncompatible, ContactsController.formatName(user.first_name, user.last_name));
                        } else {
                            if (TextUtils.equals(lastError, Instance.ERROR_PEER_OUTDATED)) {
                                if (this.isVideoCall) {
                                    final boolean[] zArr = new boolean[1];
                                    AlertDialog show = new DarkAlertDialog.Builder(this.activity).setTitle(LocaleController.getString(i7)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("VoipPeerVideoOutdated", R.string.VoipPeerVideoOutdated, UserObject.getFirstName(this.callingUser)))).setNegativeButton(LocaleController.getString(R.string.Cancel), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda18
                                        @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                                        public final void onClick(AlertDialog alertDialog, int i8) {
                                            VoIPFragment.this.lambda$updateViewState$26(alertDialog, i8);
                                        }
                                    }).setPositiveButton(LocaleController.getString(R.string.VoipPeerVideoOutdatedMakeVoice), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda19
                                        @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                                        public final void onClick(AlertDialog alertDialog, int i8) {
                                            VoIPFragment.this.lambda$updateViewState$27(zArr, alertDialog, i8);
                                        }
                                    }).show();
                                    show.setCanceledOnTouchOutside(true);
                                    show.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda20
                                        @Override // android.content.DialogInterface.OnDismissListener
                                        public final void onDismiss(DialogInterface dialogInterface) {
                                            VoIPFragment.this.lambda$updateViewState$28(zArr, dialogInterface);
                                        }
                                    });
                                } else {
                                    formatString = LocaleController.formatString("VoipPeerOutdated", R.string.VoipPeerOutdated, UserObject.getFirstName(this.callingUser));
                                }
                            } else if (TextUtils.equals(lastError, Instance.ERROR_PRIVACY)) {
                                TLRPC.User user2 = this.callingUser;
                                formatString = LocaleController.formatString("CallNotAvailable", R.string.CallNotAvailable, ContactsController.formatName(user2.first_name, user2.last_name));
                            } else {
                                if (TextUtils.equals(lastError, Instance.ERROR_AUDIO_IO)) {
                                    string = "Error initializing audio hardware";
                                } else if (TextUtils.equals(lastError, Instance.ERROR_LOCALIZED)) {
                                    this.windowView.finish();
                                } else if (TextUtils.equals(lastError, Instance.ERROR_CONNECTION_SERVICE)) {
                                    string = LocaleController.getString(R.string.VoipErrorUnknown);
                                } else {
                                    runnable = new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda21
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            VoIPFragment.this.lambda$updateViewState$29();
                                        }
                                    };
                                }
                                showErrorDialog(string);
                            }
                            z4 = false;
                            if (this.previewDialog == null) {
                            }
                        }
                        string = AndroidUtilities.replaceTags(formatString);
                        showErrorDialog(string);
                    }
                    AndroidUtilities.runOnUIThread(runnable, 1000L);
                } else if (i6 != 5) {
                    switch (i6) {
                        case 11:
                            boolean z12 = sharedInstance != null && sharedInstance.hasRate();
                            this.currentUserTextureView.saveCameraLastBitmap();
                            if (!z12 || this.isFinished) {
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda17
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        VoIPFragment.this.lambda$updateViewState$25();
                                    }
                                }, 200L);
                                break;
                            } else {
                                if (this.uiVisible) {
                                    int[] iArr = new int[2];
                                    int i8 = AndroidUtilities.displaySize.x;
                                    this.bottomEndCallBtn.getLocationOnScreen(iArr);
                                    int measuredWidth = ((i8 - iArr[0]) - ((this.bottomEndCallBtn.getMeasuredWidth() - AndroidUtilities.dp(52.0f)) / 2)) - AndroidUtilities.dp(52.0f);
                                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.endCloseLayout.getLayoutParams();
                                    marginLayoutParams.rightMargin = measuredWidth;
                                    marginLayoutParams.leftMargin = measuredWidth;
                                    this.endCloseLayout.setTranslationY(iArr[1]);
                                    this.endCloseLayout.setAlpha(1.0f);
                                    this.endCloseLayout.setLayoutParams(marginLayoutParams);
                                    this.buttonsLayout.animate().alpha(0.0f).setDuration(80L).start();
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda13
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            VoIPFragment.this.lambda$updateViewState$21(sharedInstance);
                                        }
                                    }, 2L);
                                    r9 = 0;
                                } else {
                                    this.buttonsLayout.setVisibility(8);
                                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.endCloseLayout.getLayoutParams();
                                    layoutParams.rightMargin = AndroidUtilities.dp(18.0f);
                                    layoutParams.leftMargin = AndroidUtilities.dp(18.0f);
                                    int dp2 = AndroidUtilities.dp(36.0f);
                                    layoutParams.bottomMargin = dp2;
                                    if (Build.VERSION.SDK_INT >= 20 && (windowInsets = this.lastInsets) != null) {
                                        systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
                                        layoutParams.bottomMargin = dp2 + systemWindowInsetBottom;
                                    }
                                    layoutParams.gravity = 80;
                                    this.endCloseLayout.setLayoutParams(layoutParams);
                                    this.endCloseLayout.animate().alpha(1.0f).setDuration(250L).start();
                                    r9 = 0;
                                    this.endCloseLayout.switchToClose(new View.OnClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda15
                                        @Override // android.view.View.OnClickListener
                                        public final void onClick(View view2) {
                                            VoIPFragment.this.lambda$updateViewState$23(sharedInstance, view2);
                                        }
                                    }, false);
                                }
                                this.rateCallLayout.setVisibility(r9);
                                this.rateCallLayout.show(new RateCallLayout.OnRateSelected() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda16
                                    @Override // org.telegram.ui.Components.voip.RateCallLayout.OnRateSelected
                                    public final void onRateSelected(int i9) {
                                        VoIPFragment.this.lambda$updateViewState$24(i9);
                                    }
                                });
                                if (this.emojiExpanded) {
                                    this.emojiExpanded = r9;
                                    ViewPropertyAnimator duration = this.hideEmojiLayout.animate().alpha(0.0f).scaleY(0.3f).scaleX(0.3f).setDuration(250L);
                                    CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
                                    duration.setInterpolator(cubicBezierInterpolator).setListener(new HideViewAfterAnimation(this.hideEmojiLayout)).start();
                                    this.emojiLayout.animate().scaleX(1.0f).scaleY(1.0f).translationY(0.0f).setInterpolator(cubicBezierInterpolator).setDuration(250L).start();
                                    this.emojiRationalLayout.animate().alpha(0.0f).scaleY(0.7f).scaleX(0.7f).translationY(-AndroidUtilities.dp(120.0f)).setListener(new HideViewAfterAnimation(this.hideEmojiLayout)).setDuration(250L).setInterpolator(cubicBezierInterpolator).start();
                                }
                                for (BackupImageView backupImageView : this.emojiViews) {
                                    backupImageView.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setInterpolator(CubicBezierInterpolator.DEFAULT).setDuration(250L).start();
                                }
                                this.callingUserTitle.animate().alpha(0.0f).setDuration(70L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.VoIPFragment.18
                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public void onAnimationEnd(Animator animator) {
                                        VoIPFragment.this.callingUserTitle.setText(LocaleController.getString(R.string.VoipCallEnded));
                                        VoIPFragment.this.callingUserTitle.animate().alpha(1.0f).setDuration(70L).setListener(null).start();
                                    }
                                }).start();
                                this.speakerPhoneIcon.animate().alpha(0.0f).setInterpolator(CubicBezierInterpolator.DEFAULT).setDuration(250L).start();
                                this.speakerPhoneIcon.setVisibility(8);
                                this.statusTextView.showReconnect(false, true);
                                this.statusTextView.showBadConnection(false, true);
                                this.statusTextView.setDrawCallIcon();
                                this.callingUserPhotoViewMini.onNeedRating();
                                updateButtons(true);
                                this.bottomEndCallBtn.setVisibility(4);
                                this.callingUserMiniFloatingLayout.setAlpha(0.0f);
                                this.callingUserMiniFloatingLayout.setVisibility(8);
                                this.currentUserCameraFloatingLayout.setAlpha(0.0f);
                                this.currentUserCameraFloatingLayout.setVisibility(8);
                                PrivateVideoPreviewDialogNew privateVideoPreviewDialogNew = this.previewDialog;
                                if (privateVideoPreviewDialogNew != null) {
                                    privateVideoPreviewDialogNew.dismiss(false, false);
                                }
                                this.notificationsLayout.animate().alpha(0.0f).setDuration(250L).start();
                                break;
                            }
                            break;
                        case 12:
                            if (this.previousState != 12) {
                                voIPStatusTextView = this.statusTextView;
                                i5 = R.string.VoipExchangingKeys;
                                voIPStatusTextView.setText(LocaleController.getString(i5), true, z11);
                                break;
                            }
                            break;
                        case 13:
                            voIPStatusTextView = this.statusTextView;
                            i5 = R.string.VoipWaiting;
                            voIPStatusTextView.setText(LocaleController.getString(i5), true, z11);
                            break;
                        case 14:
                            voIPStatusTextView = this.statusTextView;
                            i5 = R.string.VoipRequesting;
                            voIPStatusTextView.setText(LocaleController.getString(i5), true, z11);
                            break;
                        case 15:
                            this.lockOnScreen = false;
                            this.acceptDeclineView.setRetryMod(false);
                            if (sharedState == null || sharedState.getPrivateCall() == null || !sharedState.getPrivateCall().video) {
                                this.statusTextView.setText(LocaleController.getString(R.string.VoipInCallBranding), false, z11);
                                this.acceptDeclineView.setTranslationY(0.0f);
                            } else {
                                this.statusTextView.setText(LocaleController.getString(R.string.VoipInVideoCallBranding), false, z11);
                                this.acceptDeclineView.setTranslationY(-AndroidUtilities.dp(60.0f));
                            }
                            z3 = true;
                            z2 = false;
                            z4 = false;
                            break;
                        case 16:
                            if (this.previousState != 16) {
                                this.statusTextView.setText(LocaleController.getString(R.string.VoipRinging), true, z11);
                                break;
                            }
                            break;
                        case 17:
                            z4 = false;
                            this.statusTextView.setText(LocaleController.getString(R.string.VoipBusy), false, z11);
                            this.acceptDeclineView.setRetryMod(true);
                            this.currentUserIsVideo = false;
                            this.callingUserIsVideo = false;
                            z3 = true;
                            z2 = false;
                            break;
                    }
                    if (this.previewDialog == null) {
                        return;
                    }
                    boolean z13 = this.callingUserIsVideo || this.currentUserIsVideo;
                    if (sharedInstance != null) {
                        this.callingUserIsVideo = sharedInstance.getRemoteVideoState() == 2;
                        boolean z14 = sharedInstance.getVideoState(false) == 2 || sharedInstance.getVideoState(false) == 1;
                        this.currentUserIsVideo = z14;
                        if (z14 && !this.isVideoCall) {
                            this.isVideoCall = true;
                        }
                    }
                    if (z11) {
                        this.currentUserCameraFloatingLayout.saveRelativePosition();
                        this.callingUserMiniFloatingLayout.saveRelativePosition();
                    }
                    if (this.callingUserIsVideo) {
                        if (!this.switchingToPip) {
                            this.gradientLayout.setAlpha(1.0f);
                        }
                        ViewPropertyAnimator animate = this.callingUserTextureView.animate();
                        if (z11) {
                            animate.alpha(1.0f).setDuration(250L).start();
                        } else {
                            animate.cancel();
                            this.callingUserTextureView.setAlpha(1.0f);
                        }
                        if (!this.callingUserTextureView.renderer.isFirstFrameRendered() && !this.enterFromPiP) {
                            i = 0;
                            this.callingUserIsVideo = false;
                            if (!this.currentUserIsVideo || this.callingUserIsVideo) {
                                this.gradientLayout.setVisibility(4);
                            } else {
                                this.gradientLayout.setVisibility(i);
                                ViewPropertyAnimator animate2 = this.callingUserTextureView.animate();
                                if (z11) {
                                    animate2.alpha(0.0f).setDuration(250L).start();
                                } else {
                                    animate2.cancel();
                                    this.callingUserTextureView.setAlpha(0.0f);
                                }
                            }
                            z5 = this.currentUserIsVideo;
                            if (z5 || !this.callingUserIsVideo) {
                                this.cameraForceExpanded = false;
                            }
                            z6 = !z5 && this.cameraForceExpanded;
                            showCallingUserAvatarMini(z11, z13);
                            dp = this.callingUserPhotoViewMini.getTag() != null ? 0 : AndroidUtilities.dp(135.0f) + AndroidUtilities.dp(12.0f);
                            showAcceptDeclineView(z3, z11);
                            this.windowView.setLockOnScreen(!this.lockOnScreen || this.deviceIsLocked);
                            z7 = this.currentState != 3 && (this.currentUserIsVideo || this.callingUserIsVideo);
                            this.canHideUI = z7;
                            if (!z7 && !this.uiVisible) {
                                showUi(true);
                            }
                            if (this.uiVisible || !this.canHideUI || this.hideUiRunnableWaiting || sharedInstance == null) {
                                z8 = z2;
                            } else {
                                z8 = z2;
                                AndroidUtilities.runOnUIThread(this.hideUIRunnable, 3000L);
                                this.hideUiRunnableWaiting = true;
                            }
                            if (z11) {
                                if (!this.lockOnScreen) {
                                    this.backIcon.setVisibility(0);
                                }
                                this.backIcon.setAlpha(this.lockOnScreen ? 0.0f : 1.0f);
                                this.notificationsLayout.setTranslationY((-AndroidUtilities.dp(16.0f)) - (this.uiVisible ? AndroidUtilities.dp(80.0f) : 0));
                            } else {
                                if (this.currentState == 11) {
                                    alpha = this.backIcon.animate().alpha(0.0f).setInterpolator(CubicBezierInterpolator.DEFAULT).setDuration(250L);
                                } else if (this.lockOnScreen || !this.uiVisible) {
                                    if (this.backIcon.getVisibility() != 0) {
                                        this.backIcon.setVisibility(0);
                                        this.backIcon.setAlpha(0.0f);
                                    }
                                    alpha = this.backIcon.animate().alpha(0.0f);
                                } else {
                                    alpha = this.backIcon.animate().alpha(1.0f);
                                }
                                alpha.start();
                                this.notificationsLayout.animate().translationY((-AndroidUtilities.dp(16.0f)) - (this.uiVisible ? AndroidUtilities.dp(80.0f) : 0)).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                            }
                            i2 = this.currentState;
                            if (i2 != 10 && i2 != 11) {
                                updateButtons(z11);
                            }
                            if (z4) {
                                this.statusTextView.showTimer(z11);
                            }
                            this.statusTextView.showReconnect(z8, z11);
                            if (this.callingUserPhotoViewMini.getVisibility() == 0 && this.emojiExpanded) {
                                dp += AndroidUtilities.dp(24.0f);
                                layout = this.emojiRationalTextView.getLayout();
                                if (layout != null && (lineCount = layout.getLineCount()) > 2) {
                                    dp += AndroidUtilities.dp(20.0f) * (lineCount - 2);
                                }
                            }
                            if (this.currentState == 11 && !this.currentUserIsVideo && !this.callingUserIsVideo) {
                                dp -= AndroidUtilities.dp(24.0f);
                            }
                            if (!this.currentUserIsVideo || this.callingUserIsVideo) {
                                dp -= AndroidUtilities.dp(60.0f);
                            }
                            int i9 = dp;
                            if (z11) {
                                this.statusLayout.setTranslationY(i9);
                            } else {
                                if (this.emojiExpanded && (this.currentUserIsVideo || this.callingUserIsVideo)) {
                                    this.statusLayout.animate().setStartDelay(0L).alpha(0.0f).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                                    j = 250;
                                } else {
                                    j = 250;
                                    this.statusLayout.animate().setStartDelay(250L).alpha(1.0f).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                                }
                                if (i9 != this.statusLayoutAnimateToOffset) {
                                    ViewPropertyAnimator animate3 = this.statusLayout.animate();
                                    if (this.currentState != 11) {
                                        j = 0;
                                    }
                                    animate3.setStartDelay(j).translationY(i9).setDuration(200L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                                }
                            }
                            this.statusLayoutAnimateToOffset = i9;
                            boolean z15 = sharedInstance == null && sharedInstance.isScreencast();
                            int i10 = this.currentState;
                            this.canSwitchToPip = i10 == 11 && i10 != 17 && ((this.currentUserIsVideo && !z15) || this.callingUserIsVideo);
                            if (sharedInstance != null) {
                                if (this.currentUserIsVideo) {
                                    sharedInstance.sharedUIParams.tapToVideoTooltipWasShowed = true;
                                }
                                this.currentUserTextureView.setIsScreencast(sharedInstance.isScreencast());
                                this.currentUserTextureView.renderer.setMirror(sharedInstance.isFrontFaceCamera());
                                sharedInstance.setSinks((!this.currentUserIsVideo || sharedInstance.isScreencast()) ? null : this.currentUserTextureView.renderer, z6 ? this.callingUserMiniTextureRenderer : this.callingUserTextureView.renderer);
                                if (z11) {
                                    this.notificationsLayout.beforeLayoutChanges();
                                }
                                if (sharedInstance.isMicMute()) {
                                    this.notificationsLayout.addNotification(R.drawable.calls_mute_mini, LocaleController.getString(R.string.VoipMyMicrophoneState), "self-muted", z11);
                                } else {
                                    this.notificationsLayout.removeNotification("self-muted");
                                }
                                if ((this.currentUserIsVideo || this.callingUserIsVideo) && (((i4 = this.currentState) == 3 || i4 == 5) && sharedInstance.getCallDuration() > 500)) {
                                    if (sharedInstance.getRemoteAudioState() == 0) {
                                        VoIPNotificationsLayout voIPNotificationsLayout = this.notificationsLayout;
                                        voIPNotificationsLayout.addNotification(R.drawable.calls_mute_mini, LocaleController.formatString("VoipUserMicrophoneIsOff", R.string.VoipUserMicrophoneIsOff, voIPNotificationsLayout.ellipsize(UserObject.getFirstName(this.callingUser))), "muted", z11);
                                    } else {
                                        this.notificationsLayout.removeNotification("muted");
                                    }
                                    if (sharedInstance.getRemoteVideoState() == 0) {
                                        VoIPNotificationsLayout voIPNotificationsLayout2 = this.notificationsLayout;
                                        voIPNotificationsLayout2.addNotification(R.drawable.calls_camera_mini, LocaleController.formatString("VoipUserCameraIsOff", R.string.VoipUserCameraIsOff, voIPNotificationsLayout2.ellipsize(UserObject.getFirstName(this.callingUser))), MediaStreamTrack.VIDEO_TRACK_KIND, z11);
                                        if (this.notificationsLayout.getChildCount() == 0 && this.callingUserIsVideo && (phoneCall = sharedInstance.privateCall) != null && !phoneCall.video) {
                                            sharedUIParams = sharedInstance.sharedUIParams;
                                            if (!sharedUIParams.tapToVideoTooltipWasShowed) {
                                                sharedUIParams.tapToVideoTooltipWasShowed = true;
                                                this.tapToVideoTooltip.setTranslationY(-((this.fragmentView.getMeasuredHeight() - this.buttonsLayout.getY()) + AndroidUtilities.dp(6.0f)));
                                                this.tapToVideoTooltip.setJointPx(0.0f, this.buttonsLayout.getX() + this.bottomVideoBtn.getX() + AndroidUtilities.dp(14.0f));
                                                this.tapToVideoTooltip.show();
                                                if (z11) {
                                                    this.notificationsLayout.animateLayoutChanges();
                                                }
                                            }
                                        }
                                        if (this.notificationsLayout.getChildCount() != 0) {
                                            this.tapToVideoTooltip.hide();
                                        }
                                        if (z11) {
                                        }
                                    }
                                } else if (sharedInstance.getRemoteAudioState() == 0) {
                                    VoIPNotificationsLayout voIPNotificationsLayout3 = this.notificationsLayout;
                                    voIPNotificationsLayout3.addNotification(R.drawable.calls_mute_mini, LocaleController.formatString("VoipUserMicrophoneIsOff", R.string.VoipUserMicrophoneIsOff, voIPNotificationsLayout3.ellipsize(UserObject.getFirstName(this.callingUser))), "muted", z11);
                                } else {
                                    this.notificationsLayout.removeNotification("muted");
                                }
                                this.notificationsLayout.removeNotification(MediaStreamTrack.VIDEO_TRACK_KIND);
                                if (this.notificationsLayout.getChildCount() == 0) {
                                    sharedUIParams = sharedInstance.sharedUIParams;
                                    if (!sharedUIParams.tapToVideoTooltipWasShowed) {
                                    }
                                }
                                if (this.notificationsLayout.getChildCount() != 0) {
                                }
                                if (z11) {
                                }
                            }
                            int childsHight = this.notificationsLayout.getChildsHight();
                            this.callingUserMiniFloatingLayout.setBottomOffset(childsHight, z11);
                            this.currentUserCameraFloatingLayout.setBottomOffset(childsHight, z11);
                            this.currentUserCameraFloatingLayout.setUiVisible(this.uiVisible);
                            this.callingUserMiniFloatingLayout.setUiVisible(this.uiVisible);
                            if (this.currentUserIsVideo) {
                                z9 = true;
                                showFloatingLayout(0, z11);
                            } else if (!this.callingUserIsVideo || this.cameraForceExpanded) {
                                z9 = true;
                                showFloatingLayout(1, z11);
                            } else {
                                showFloatingLayout(2, z11);
                                z9 = true;
                            }
                            if (!z6 && this.callingUserMiniFloatingLayout.getTag() == null) {
                                this.callingUserMiniFloatingLayout.setIsActive(z9);
                                if (this.callingUserMiniFloatingLayout.getVisibility() != 0) {
                                    this.callingUserMiniFloatingLayout.setVisibility(0);
                                    this.callingUserMiniFloatingLayout.setAlpha(0.0f);
                                    this.callingUserMiniFloatingLayout.setScaleX(0.5f);
                                    this.callingUserMiniFloatingLayout.setScaleY(0.5f);
                                }
                                this.callingUserMiniFloatingLayout.animate().setListener(null).cancel();
                                VoIPFloatingLayout voIPFloatingLayout = this.callingUserMiniFloatingLayout;
                                voIPFloatingLayout.isAppearing = true;
                                voIPFloatingLayout.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).setStartDelay(150L).withEndAction(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda14
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        VoIPFragment.this.lambda$updateViewState$31();
                                    }
                                }).start();
                                this.callingUserMiniFloatingLayout.setTag(1);
                            } else if (!z6 && this.callingUserMiniFloatingLayout.getTag() != null) {
                                this.callingUserMiniFloatingLayout.setIsActive(false);
                                this.callingUserMiniFloatingLayout.animate().alpha(0.0f).scaleX(0.5f).scaleY(0.5f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.VoIPFragment.19
                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public void onAnimationEnd(Animator animator) {
                                        if (VoIPFragment.this.callingUserMiniFloatingLayout.getTag() == null) {
                                            VoIPFragment.this.callingUserMiniFloatingLayout.setVisibility(8);
                                        }
                                    }
                                }).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                                this.callingUserMiniFloatingLayout.setTag(null);
                            }
                            this.currentUserCameraFloatingLayout.restoreRelativePosition();
                            this.callingUserMiniFloatingLayout.restoreRelativePosition();
                            updateSpeakerPhoneIcon();
                            if (this.currentState == 3) {
                                this.voIpCoverView.onConnected();
                                this.callingUserPhotoViewMini.onConnected();
                                if (!this.gradientLayout.isConnectedCalled()) {
                                    int[] iArr2 = new int[2];
                                    this.callingUserPhotoViewMini.getLocationOnScreen(iArr2);
                                    this.gradientLayout.switchToCallConnected(iArr2[0] + AndroidUtilities.dp(106.0f), iArr2[1] + AndroidUtilities.dp(106.0f), this.previousState != -1);
                                    z10 = !this.currentUserIsVideo || this.callingUserIsVideo;
                                    this.voIpSnowView.setState(z10);
                                    this.voIpCoverView.setState(z10);
                                    this.backgroundProvider.setHasVideo(z10);
                                    if (this.callingUserIsVideo && !z13 && this.isNearEar) {
                                        this.isNearEar = false;
                                        if (sharedInstance != null) {
                                            sharedInstance.playStartRecordSound();
                                        }
                                    }
                                    int visibility = this.topShadow.getVisibility();
                                    if (!z10) {
                                        i3 = 4;
                                        if (visibility != 4) {
                                            view = this.topShadow;
                                            view.setVisibility(i3);
                                            this.bottomShadow.setVisibility(i3);
                                        }
                                        AndroidUtilities.cancelRunOnUIThread(this.stopAnimatingBgRunnable);
                                        if (this.currentState != 3) {
                                            AndroidUtilities.runOnUIThread(this.stopAnimatingBgRunnable, 10000L);
                                            return;
                                        }
                                        return;
                                    }
                                    if (visibility != 0) {
                                        view = this.topShadow;
                                        i3 = 0;
                                        view.setVisibility(i3);
                                        this.bottomShadow.setVisibility(i3);
                                    }
                                    AndroidUtilities.cancelRunOnUIThread(this.stopAnimatingBgRunnable);
                                    if (this.currentState != 3) {
                                    }
                                }
                            }
                            if (this.currentUserIsVideo) {
                            }
                            this.voIpSnowView.setState(z10);
                            this.voIpCoverView.setState(z10);
                            this.backgroundProvider.setHasVideo(z10);
                            if (this.callingUserIsVideo) {
                                this.isNearEar = false;
                                if (sharedInstance != null) {
                                }
                            }
                            int visibility2 = this.topShadow.getVisibility();
                            if (!z10) {
                            }
                        }
                    }
                    i = 0;
                    if (this.currentUserIsVideo) {
                    }
                    this.gradientLayout.setVisibility(4);
                    z5 = this.currentUserIsVideo;
                    if (z5) {
                    }
                    this.cameraForceExpanded = false;
                    if (z5) {
                    }
                    showCallingUserAvatarMini(z11, z13);
                    if (this.callingUserPhotoViewMini.getTag() != null) {
                    }
                    showAcceptDeclineView(z3, z11);
                    this.windowView.setLockOnScreen(!this.lockOnScreen || this.deviceIsLocked);
                    if (this.currentState != 3) {
                    }
                    this.canHideUI = z7;
                    if (!z7) {
                        showUi(true);
                    }
                    if (this.uiVisible) {
                    }
                    z8 = z2;
                    if (z11) {
                    }
                    i2 = this.currentState;
                    if (i2 != 10) {
                        updateButtons(z11);
                    }
                    if (z4) {
                    }
                    this.statusTextView.showReconnect(z8, z11);
                    if (this.callingUserPhotoViewMini.getVisibility() == 0) {
                        dp += AndroidUtilities.dp(24.0f);
                        layout = this.emojiRationalTextView.getLayout();
                        if (layout != null) {
                            dp += AndroidUtilities.dp(20.0f) * (lineCount - 2);
                        }
                    }
                    if (this.currentState == 11) {
                        dp -= AndroidUtilities.dp(24.0f);
                    }
                    if (!this.currentUserIsVideo) {
                    }
                    dp -= AndroidUtilities.dp(60.0f);
                    int i92 = dp;
                    if (z11) {
                    }
                    this.statusLayoutAnimateToOffset = i92;
                    if (sharedInstance == null) {
                    }
                    int i102 = this.currentState;
                    this.canSwitchToPip = i102 == 11 && i102 != 17 && ((this.currentUserIsVideo && !z15) || this.callingUserIsVideo);
                    if (sharedInstance != null) {
                    }
                    int childsHight2 = this.notificationsLayout.getChildsHight();
                    this.callingUserMiniFloatingLayout.setBottomOffset(childsHight2, z11);
                    this.currentUserCameraFloatingLayout.setBottomOffset(childsHight2, z11);
                    this.currentUserCameraFloatingLayout.setUiVisible(this.uiVisible);
                    this.callingUserMiniFloatingLayout.setUiVisible(this.uiVisible);
                    if (this.currentUserIsVideo) {
                    }
                    if (!z6) {
                    }
                    if (!z6) {
                        this.callingUserMiniFloatingLayout.setIsActive(false);
                        this.callingUserMiniFloatingLayout.animate().alpha(0.0f).scaleX(0.5f).scaleY(0.5f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.VoIPFragment.19
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                if (VoIPFragment.this.callingUserMiniFloatingLayout.getTag() == null) {
                                    VoIPFragment.this.callingUserMiniFloatingLayout.setVisibility(8);
                                }
                            }
                        }).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                        this.callingUserMiniFloatingLayout.setTag(null);
                    }
                    this.currentUserCameraFloatingLayout.restoreRelativePosition();
                    this.callingUserMiniFloatingLayout.restoreRelativePosition();
                    updateSpeakerPhoneIcon();
                    if (this.currentState == 3) {
                    }
                    if (this.currentUserIsVideo) {
                    }
                    this.voIpSnowView.setState(z10);
                    this.voIpCoverView.setState(z10);
                    this.backgroundProvider.setHasVideo(z10);
                    if (this.callingUserIsVideo) {
                    }
                    int visibility22 = this.topShadow.getVisibility();
                    if (!z10) {
                    }
                }
                z3 = false;
                z2 = false;
                z4 = false;
                if (this.previewDialog == null) {
                }
            }
            updateKeyView(z11);
            if (this.currentState == 5) {
                z = this.wasEstablished;
                if (!z) {
                }
                z2 = z;
                z3 = false;
                z4 = false;
                if (this.previewDialog == null) {
                }
            } else {
                this.wasEstablished = true;
                z3 = false;
                z2 = false;
                z4 = true;
                if (this.previewDialog == null) {
                }
            }
        }
        this.statusTextView.setText(LocaleController.getString(R.string.VoipConnecting), true, z11);
        z2 = z;
        z3 = false;
        z4 = false;
        if (this.previewDialog == null) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0152  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x019f  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x01b1  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01a1  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x019a  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0138  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Animator createPiPTransition(boolean z) {
        float measuredHeight;
        float measuredWidth;
        float f;
        final boolean z2;
        this.currentUserCameraFloatingLayout.animate().cancel();
        float f2 = VoIPPiPView.getInstance().windowLayoutParams.x + VoIPPiPView.getInstance().xOffset;
        float f3 = VoIPPiPView.getInstance().windowLayoutParams.y + VoIPPiPView.getInstance().yOffset;
        final float x = this.currentUserCameraFloatingLayout.getX();
        final float y = this.currentUserCameraFloatingLayout.getY();
        final float scaleX = this.currentUserCameraFloatingLayout.getScaleX();
        float f4 = VoIPPiPView.isExpanding() ? 0.4f : 0.25f;
        final float measuredWidth2 = f2 - ((this.callingUserTextureView.getMeasuredWidth() - (this.callingUserTextureView.getMeasuredWidth() * f4)) / 2.0f);
        final float measuredHeight2 = f3 - ((this.callingUserTextureView.getMeasuredHeight() - (this.callingUserTextureView.getMeasuredHeight() * f4)) / 2.0f);
        if (this.callingUserIsVideo) {
            int measuredWidth3 = this.currentUserCameraFloatingLayout.getMeasuredWidth();
            if (!this.currentUserIsVideo || measuredWidth3 == 0) {
                z2 = false;
                measuredHeight = 1.0f;
                f = 0.0f;
                measuredWidth = 1.0f;
                float dp = !this.callingUserIsVideo ? AndroidUtilities.dp(4.0f) : 0.0f;
                final float dp2 = (AndroidUtilities.dp(4.0f) * 1.0f) / f;
                float f5 = (this.callingUserIsVideo || VoIPPiPView.isExpanding()) ? 1.0f : 0.0f;
                if (z) {
                    if (z2) {
                        this.currentUserCameraFloatingLayout.setScaleX(f);
                        this.currentUserCameraFloatingLayout.setScaleY(f);
                        this.currentUserCameraFloatingLayout.setTranslationX(measuredWidth);
                        this.currentUserCameraFloatingLayout.setTranslationY(measuredHeight);
                        this.currentUserCameraFloatingLayout.setCornerRadius(dp2);
                        this.currentUserCameraFloatingLayout.setAlpha(f5);
                    }
                    this.callingUserTextureView.setScaleX(f4);
                    this.callingUserTextureView.setScaleY(f4);
                    this.callingUserTextureView.setTranslationX(measuredWidth2);
                    this.callingUserTextureView.setTranslationY(measuredHeight2);
                    this.callingUserTextureView.setRoundCorners((AndroidUtilities.dp(6.0f) * 1.0f) / f4);
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(!z ? 1.0f : 0.0f, !z ? 0.0f : 1.0f);
                this.enterTransitionProgress = !z ? 0.0f : 1.0f;
                updateSystemBarColors();
                final float f6 = 1.0f;
                final float f7 = 1.0f;
                final float f8 = 0.0f;
                final float f9 = f5;
                final float f10 = 0.0f;
                final float f11 = measuredHeight;
                final float f12 = f;
                final float f13 = measuredWidth;
                final float f14 = dp;
                final float f15 = f4;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda35
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        VoIPFragment.this.lambda$createPiPTransition$18(z2, scaleX, f12, x, f13, y, f11, f14, dp2, f6, f9, f7, f15, f8, measuredWidth2, f10, measuredHeight2, valueAnimator);
                    }
                });
                return ofFloat;
            }
            float measuredWidth4 = (this.windowView.getMeasuredWidth() / measuredWidth3) * f4 * 0.4f;
            measuredHeight = (((f3 - ((this.currentUserCameraFloatingLayout.getMeasuredHeight() - (this.currentUserCameraFloatingLayout.getMeasuredHeight() * measuredWidth4)) / 2.0f)) + (VoIPPiPView.getInstance().parentHeight * f4)) - ((VoIPPiPView.getInstance().parentHeight * f4) * 0.4f)) - AndroidUtilities.dp(4.0f);
            measuredWidth = (((f2 - ((this.currentUserCameraFloatingLayout.getMeasuredWidth() - (this.currentUserCameraFloatingLayout.getMeasuredWidth() * measuredWidth4)) / 2.0f)) + (VoIPPiPView.getInstance().parentWidth * f4)) - ((VoIPPiPView.getInstance().parentWidth * f4) * 0.4f)) - AndroidUtilities.dp(4.0f);
            f = measuredWidth4;
        } else {
            measuredHeight = f3 - ((this.currentUserCameraFloatingLayout.getMeasuredHeight() - (this.currentUserCameraFloatingLayout.getMeasuredHeight() * f4)) / 2.0f);
            measuredWidth = f2 - ((this.currentUserCameraFloatingLayout.getMeasuredWidth() - (this.currentUserCameraFloatingLayout.getMeasuredWidth() * f4)) / 2.0f);
            f = f4;
        }
        z2 = true;
        if (!this.callingUserIsVideo) {
        }
        final float dp22 = (AndroidUtilities.dp(4.0f) * 1.0f) / f;
        if (this.callingUserIsVideo) {
        }
        if (z) {
        }
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(!z ? 1.0f : 0.0f, !z ? 0.0f : 1.0f);
        this.enterTransitionProgress = !z ? 0.0f : 1.0f;
        updateSystemBarColors();
        final float f62 = 1.0f;
        final float f72 = 1.0f;
        final float f82 = 0.0f;
        final float f92 = f5;
        final float f102 = 0.0f;
        final float f112 = measuredHeight;
        final float f122 = f;
        final float f132 = measuredWidth;
        final float f142 = dp;
        final float f152 = f4;
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda35
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                VoIPFragment.this.lambda$createPiPTransition$18(z2, scaleX, f122, x, f132, y, f112, f142, dp22, f62, f92, f72, f152, f82, measuredWidth2, f102, measuredHeight2, valueAnimator);
            }
        });
        return ofFloat2;
    }

    public View createView(Context context) {
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.accessibilityManager = (AccessibilityManager) ContextCompat.getSystemService(context, AccessibilityManager.class);
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.VoIPFragment.2
            boolean check;
            long pressedTime;
            float pressedX;
            float pressedY;

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                if (view == VoIPFragment.this.gradientLayout) {
                    VoIPFragment voIPFragment = VoIPFragment.this;
                    if (voIPFragment.currentUserIsVideo || voIPFragment.callingUserIsVideo) {
                        return false;
                    }
                }
                if ((view != VoIPFragment.this.gradientLayout && view != VoIPFragment.this.callingUserTextureView && (view != VoIPFragment.this.currentUserCameraFloatingLayout || !VoIPFragment.this.currentUserCameraIsFullscreen)) || (!VoIPFragment.this.zoomStarted && VoIPFragment.this.zoomBackAnimator == null)) {
                    return super.drawChild(canvas, view, j);
                }
                canvas.save();
                VoIPFragment voIPFragment2 = VoIPFragment.this;
                float f = voIPFragment2.pinchScale;
                canvas.scale(f, f, voIPFragment2.pinchCenterX, VoIPFragment.this.pinchCenterY);
                canvas.translate(VoIPFragment.this.pinchTranslationX, VoIPFragment.this.pinchTranslationY);
                boolean drawChild = super.drawChild(canvas, view, j);
                canvas.restore();
                return drawChild;
            }

            @Override // android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == 1) {
                    VoIPFragment.this.callingUserPhotoViewMini.setMute(false, false);
                    VoIPFragment.this.gradientLayout.resume();
                    AndroidUtilities.cancelRunOnUIThread(VoIPFragment.this.stopAnimatingBgRunnable);
                    if (VoIPFragment.this.currentState == 3) {
                        AndroidUtilities.runOnUIThread(VoIPFragment.this.stopAnimatingBgRunnable, 10000L);
                    }
                }
                return super.onInterceptTouchEvent(motionEvent);
            }

            /* JADX WARN: Code restructure failed: missing block: B:52:0x02eb, code lost:
            
                if (r0 != 3) goto L101;
             */
            /* JADX WARN: Code restructure failed: missing block: B:88:0x01a4, code lost:
            
                if (r13.this$0.checkPointerIds(r14) == false) goto L54;
             */
            /* JADX WARN: Code restructure failed: missing block: B:90:0x01aa, code lost:
            
                if (r14.getActionMasked() != 3) goto L73;
             */
            @Override // android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean onTouchEvent(MotionEvent motionEvent) {
                HintView2 hintView2;
                if (motionEvent.getActionMasked() == 1) {
                    VoIPFragment.this.callingUserPhotoViewMini.setMute(false, false);
                    VoIPFragment.this.gradientLayout.resume();
                    AndroidUtilities.cancelRunOnUIThread(VoIPFragment.this.stopAnimatingBgRunnable);
                    if (VoIPFragment.this.currentState == 3) {
                        AndroidUtilities.runOnUIThread(VoIPFragment.this.stopAnimatingBgRunnable, 10000L);
                    }
                }
                if (!VoIPFragment.this.canZoomGesture && !VoIPFragment.this.isInPinchToZoomTouchMode && !VoIPFragment.this.zoomStarted && motionEvent.getActionMasked() != 0) {
                    VoIPFragment.this.finishZoom();
                    return false;
                }
                if (motionEvent.getActionMasked() == 0) {
                    VoIPFragment.this.canZoomGesture = false;
                    VoIPFragment.this.isInPinchToZoomTouchMode = false;
                    VoIPFragment.this.zoomStarted = false;
                }
                VoIPTextureView fullscreenTextureView = VoIPFragment.this.getFullscreenTextureView();
                if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5) {
                    if (motionEvent.getActionMasked() == 0) {
                        RectF rectF = AndroidUtilities.rectTmp;
                        rectF.set(fullscreenTextureView.getX(), fullscreenTextureView.getY(), fullscreenTextureView.getX() + fullscreenTextureView.getMeasuredWidth(), fullscreenTextureView.getY() + fullscreenTextureView.getMeasuredHeight());
                        rectF.inset(((fullscreenTextureView.getMeasuredHeight() * fullscreenTextureView.scaleTextureToFill) - fullscreenTextureView.getMeasuredHeight()) / 2.0f, ((fullscreenTextureView.getMeasuredWidth() * fullscreenTextureView.scaleTextureToFill) - fullscreenTextureView.getMeasuredWidth()) / 2.0f);
                        if (GroupCallActivity.isLandscapeMode) {
                            rectF.top = Math.max(rectF.top, ActionBar.getCurrentActionBarHeight());
                            rectF.right = Math.min(rectF.right, fullscreenTextureView.getMeasuredWidth() - AndroidUtilities.dp(90.0f));
                        } else {
                            rectF.top = Math.max(rectF.top, ActionBar.getCurrentActionBarHeight());
                            rectF.bottom = Math.min(rectF.bottom, fullscreenTextureView.getMeasuredHeight() - AndroidUtilities.dp(90.0f));
                        }
                        VoIPFragment.this.canZoomGesture = rectF.contains(motionEvent.getX(), motionEvent.getY());
                        if (!VoIPFragment.this.canZoomGesture) {
                            VoIPFragment.this.finishZoom();
                        }
                    }
                    if (VoIPFragment.this.canZoomGesture && !VoIPFragment.this.isInPinchToZoomTouchMode && motionEvent.getPointerCount() == 2) {
                        VoIPFragment.this.pinchStartDistance = (float) Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0));
                        VoIPFragment voIPFragment = VoIPFragment.this;
                        voIPFragment.pinchStartCenterX = voIPFragment.pinchCenterX = (motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f;
                        VoIPFragment voIPFragment2 = VoIPFragment.this;
                        voIPFragment2.pinchStartCenterY = voIPFragment2.pinchCenterY = (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f;
                        VoIPFragment voIPFragment3 = VoIPFragment.this;
                        voIPFragment3.pinchScale = 1.0f;
                        voIPFragment3.pointerId1 = motionEvent.getPointerId(0);
                        VoIPFragment.this.pointerId2 = motionEvent.getPointerId(1);
                        VoIPFragment.this.isInPinchToZoomTouchMode = true;
                    }
                } else if (motionEvent.getActionMasked() == 2 && VoIPFragment.this.isInPinchToZoomTouchMode) {
                    int i = -1;
                    int i2 = -1;
                    for (int i3 = 0; i3 < motionEvent.getPointerCount(); i3++) {
                        if (VoIPFragment.this.pointerId1 == motionEvent.getPointerId(i3)) {
                            i = i3;
                        }
                        if (VoIPFragment.this.pointerId2 == motionEvent.getPointerId(i3)) {
                            i2 = i3;
                        }
                    }
                    if (i != -1 && i2 != -1) {
                        VoIPFragment.this.pinchScale = ((float) Math.hypot(motionEvent.getX(i2) - motionEvent.getX(i), motionEvent.getY(i2) - motionEvent.getY(i))) / VoIPFragment.this.pinchStartDistance;
                        VoIPFragment voIPFragment4 = VoIPFragment.this;
                        if (voIPFragment4.pinchScale > 1.005f && !voIPFragment4.zoomStarted) {
                            VoIPFragment.this.pinchStartDistance = (float) Math.hypot(motionEvent.getX(i2) - motionEvent.getX(i), motionEvent.getY(i2) - motionEvent.getY(i));
                            VoIPFragment voIPFragment5 = VoIPFragment.this;
                            voIPFragment5.pinchStartCenterX = voIPFragment5.pinchCenterX = (motionEvent.getX(i) + motionEvent.getX(i2)) / 2.0f;
                            VoIPFragment voIPFragment6 = VoIPFragment.this;
                            voIPFragment6.pinchStartCenterY = voIPFragment6.pinchCenterY = (motionEvent.getY(i) + motionEvent.getY(i2)) / 2.0f;
                            VoIPFragment voIPFragment7 = VoIPFragment.this;
                            voIPFragment7.pinchScale = 1.0f;
                            voIPFragment7.pinchTranslationX = 0.0f;
                            VoIPFragment.this.pinchTranslationY = 0.0f;
                            getParent().requestDisallowInterceptTouchEvent(true);
                            VoIPFragment.this.zoomStarted = true;
                            VoIPFragment.this.isInPinchToZoomTouchMode = true;
                        }
                        float x = (motionEvent.getX(i) + motionEvent.getX(i2)) / 2.0f;
                        float y = (motionEvent.getY(i) + motionEvent.getY(i2)) / 2.0f;
                        float f = VoIPFragment.this.pinchStartCenterX - x;
                        float f2 = VoIPFragment.this.pinchStartCenterY - y;
                        VoIPFragment voIPFragment8 = VoIPFragment.this;
                        voIPFragment8.pinchTranslationX = (-f) / voIPFragment8.pinchScale;
                        VoIPFragment voIPFragment9 = VoIPFragment.this;
                        voIPFragment9.pinchTranslationY = (-f2) / voIPFragment9.pinchScale;
                        invalidate();
                    }
                    getParent().requestDisallowInterceptTouchEvent(false);
                    VoIPFragment.this.finishZoom();
                } else {
                    if (motionEvent.getActionMasked() != 1) {
                        if (motionEvent.getActionMasked() == 6) {
                        }
                    }
                    getParent().requestDisallowInterceptTouchEvent(false);
                    VoIPFragment.this.finishZoom();
                }
                VoIPFragment.this.fragmentView.invalidate();
                int action = motionEvent.getAction();
                if (action == 0) {
                    this.pressedX = motionEvent.getX();
                    this.pressedY = motionEvent.getY();
                    this.check = true;
                    this.pressedTime = System.currentTimeMillis();
                } else if (action == 1) {
                    if (this.check) {
                        float x2 = motionEvent.getX() - this.pressedX;
                        float y2 = motionEvent.getY() - this.pressedY;
                        long currentTimeMillis = System.currentTimeMillis();
                        float f3 = (x2 * x2) + (y2 * y2);
                        VoIPFragment voIPFragment10 = VoIPFragment.this;
                        float f4 = voIPFragment10.touchSlop;
                        if (f3 < f4 * f4 && currentTimeMillis - this.pressedTime < 300 && currentTimeMillis - voIPFragment10.lastContentTapTime > 300) {
                            voIPFragment10.lastContentTapTime = System.currentTimeMillis();
                            if (VoIPFragment.this.emojiExpanded) {
                                VoIPFragment.this.expandEmoji(false);
                            } else if (VoIPFragment.this.canHideUI) {
                                VoIPFragment.this.showUi(!r14.uiVisible);
                                VoIPFragment voIPFragment11 = VoIPFragment.this;
                                voIPFragment11.previousState = voIPFragment11.currentState;
                                if (!VoIPFragment.this.uiVisible && (hintView2 = VoIPFragment.this.tapToVideoTooltip) != null && hintView2.shown()) {
                                    VoIPFragment.this.tapToVideoTooltip.hide();
                                }
                                VoIPFragment.this.updateViewState();
                            }
                        }
                        this.check = false;
                    }
                }
                return VoIPFragment.this.canZoomGesture || this.check;
            }
        };
        frameLayout.setClipToPadding(false);
        frameLayout.setClipChildren(false);
        frameLayout.setBackgroundColor(-16777216);
        updateSystemBarColors();
        this.fragmentView = frameLayout;
        frameLayout.setFitsSystemWindows(true);
        this.gradientLayout = new VoIpGradientLayout(context, this.backgroundProvider);
        VoIPTextureView voIPTextureView = new VoIPTextureView(context, false, true, false, false);
        this.callingUserTextureView = voIPTextureView;
        TextureViewRenderer textureViewRenderer = voIPTextureView.renderer;
        RendererCommon.ScalingType scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FIT;
        textureViewRenderer.setScalingType(scalingType);
        this.callingUserTextureView.renderer.setEnableHardwareScaler(true);
        this.callingUserTextureView.renderer.setRotateTextureWithScreen(true);
        this.callingUserTextureView.scaleType = VoIPTextureView.SCALE_TYPE_FIT;
        frameLayout.addView(this.gradientLayout, LayoutHelper.createFrame(-1, -1.0f));
        VoIpCoverView voIpCoverView = new VoIpCoverView(context, this.callingUser, this.backgroundProvider);
        this.voIpCoverView = voIpCoverView;
        frameLayout.addView(voIpCoverView, LayoutHelper.createFrame(-1, -1.0f));
        VoIpSnowView voIpSnowView = new VoIpSnowView(context);
        this.voIpSnowView = voIpSnowView;
        frameLayout.addView(voIpSnowView, LayoutHelper.createFrame(-1, 220.0f));
        frameLayout.addView(this.callingUserTextureView);
        GradientDrawable.Orientation orientation = GradientDrawable.Orientation.TOP_BOTTOM;
        new BackgroundGradientDrawable(orientation, new int[]{-14994098, -14328963}).startDithering(BackgroundGradientDrawable.Sizes.ofDeviceScreen(BackgroundGradientDrawable.Sizes.Orientation.PORTRAIT), new BackgroundGradientDrawable.ListenerAdapter() { // from class: org.telegram.ui.VoIPFragment.3
            @Override // org.telegram.ui.Components.BackgroundGradientDrawable.ListenerAdapter, org.telegram.ui.Components.BackgroundGradientDrawable.Listener
            public void onAllSizesReady() {
                VoIPFragment.this.gradientLayout.invalidate();
            }
        });
        VoIPFloatingLayout voIPFloatingLayout = new VoIPFloatingLayout(context);
        this.currentUserCameraFloatingLayout = voIPFloatingLayout;
        voIPFloatingLayout.setDelegate(new VoIPFloatingLayout.VoIPFloatingLayoutDelegate() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda24
            @Override // org.telegram.ui.Components.voip.VoIPFloatingLayout.VoIPFloatingLayoutDelegate
            public final void onChange(float f, boolean z) {
                VoIPFragment.this.lambda$createView$5(f, z);
            }
        });
        this.currentUserCameraFloatingLayout.setRelativePosition(1.0f, 1.0f);
        this.currentUserCameraIsFullscreen = true;
        VoIPTextureView voIPTextureView2 = new VoIPTextureView(context, true, false);
        this.currentUserTextureView = voIPTextureView2;
        voIPTextureView2.renderer.setIsCamera(true);
        this.currentUserTextureView.renderer.setUseCameraRotation(true);
        this.currentUserCameraFloatingLayout.setOnTapListener(new View.OnClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda25
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VoIPFragment.this.lambda$createView$6(view);
            }
        });
        this.currentUserTextureView.renderer.setMirror(true);
        this.currentUserCameraFloatingLayout.addView(this.currentUserTextureView);
        VoIPFloatingLayout voIPFloatingLayout2 = new VoIPFloatingLayout(context);
        this.callingUserMiniFloatingLayout = voIPFloatingLayout2;
        voIPFloatingLayout2.alwaysFloating = true;
        voIPFloatingLayout2.setFloatingMode(true, false);
        TextureViewRenderer textureViewRenderer2 = new TextureViewRenderer(context);
        this.callingUserMiniTextureRenderer = textureViewRenderer2;
        textureViewRenderer2.setEnableHardwareScaler(true);
        this.callingUserMiniTextureRenderer.setIsCamera(false);
        this.callingUserMiniTextureRenderer.setFpsReduction(30.0f);
        this.callingUserMiniTextureRenderer.setScalingType(scalingType);
        this.callingUserMiniFloatingLayout.addView(this.callingUserMiniTextureRenderer, LayoutHelper.createFrame(-1, -2, 17));
        this.callingUserMiniFloatingLayout.setOnTapListener(new View.OnClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda26
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VoIPFragment.this.lambda$createView$7(view);
            }
        });
        this.callingUserMiniFloatingLayout.setVisibility(8);
        frameLayout.addView(this.currentUserCameraFloatingLayout, LayoutHelper.createFrame(-2, -2.0f));
        frameLayout.addView(this.callingUserMiniFloatingLayout);
        View view = new View(context);
        this.bottomShadow = view;
        view.setBackground(new GradientDrawable(orientation, new int[]{0, ColorUtils.setAlphaComponent(-16777216, NotificationCenter.dialogTranslate)}));
        frameLayout.addView(this.bottomShadow, LayoutHelper.createFrame(-1, NotificationCenter.audioRouteChanged, 80));
        View view2 = new View(context);
        this.topShadow = view2;
        view2.setBackground(new GradientDrawable(orientation, new int[]{ColorUtils.setAlphaComponent(-16777216, 102), 0}));
        frameLayout.addView(this.topShadow, LayoutHelper.createFrame(-1, NotificationCenter.audioRouteChanged, 48));
        LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.VoIPFragment.4
            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                accessibilityNodeInfo.setVisibleToUser(VoIPFragment.this.emojiLoaded);
            }
        };
        this.emojiLayout = linearLayout;
        linearLayout.setOrientation(0);
        this.emojiLayout.setPadding(0, 0, 0, AndroidUtilities.dp(30.0f));
        this.emojiLayout.setClipToPadding(false);
        this.emojiLayout.setContentDescription(LocaleController.getString(R.string.VoipHintEncryptionKey));
        this.emojiLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda27
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                VoIPFragment.this.lambda$createView$8(view3);
            }
        });
        this.hideEmojiTextView = new HideEmojiTextView(context, this.backgroundProvider);
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.hideEmojiLayout = frameLayout2;
        frameLayout2.addView(this.hideEmojiTextView, LayoutHelper.createFrame(-2, -2.0f, 3, 0.0f, 16.0f, 0.0f, 0.0f));
        this.hideEmojiLayout.setVisibility(8);
        this.hideEmojiLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda28
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                VoIPFragment.this.lambda$createView$9(view3);
            }
        });
        EmojiRationalLayout emojiRationalLayout = new EmojiRationalLayout(context, this.backgroundProvider);
        this.emojiRationalLayout = emojiRationalLayout;
        emojiRationalLayout.setOrientation(1);
        TextView textView = new TextView(context);
        this.emojiRationalTopTextView = textView;
        textView.setText(LocaleController.getString(R.string.VoipCallEncryptionEndToEnd));
        this.emojiRationalTopTextView.setTextSize(1, 15.0f);
        this.emojiRationalTopTextView.setTypeface(AndroidUtilities.bold());
        this.emojiRationalTopTextView.setTextColor(-1);
        this.emojiRationalTopTextView.setGravity(17);
        TextView textView2 = new TextView(context) { // from class: org.telegram.ui.VoIPFragment.5
            @Override // android.widget.TextView, android.view.View
            protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
                super.onLayout(z, i, i2, i3, i4);
                if (z) {
                    VoIPFragment.this.updateViewState();
                }
            }
        };
        this.emojiRationalTextView = textView2;
        textView2.setTextSize(1, 15.0f);
        this.emojiRationalTextView.setTextColor(-1);
        this.emojiRationalTextView.setGravity(17);
        this.emojiRationalTextView.setText(LocaleController.formatString("CallEmojiKeyTooltip", R.string.CallEmojiKeyTooltip, TextUtils.ellipsize(UserObject.getFirstName(this.callingUser), this.emojiRationalTextView.getPaint(), AndroidUtilities.dp(300.0f), TextUtils.TruncateAt.END)));
        this.emojiRationalLayout.setVisibility(8);
        this.emojiRationalLayout.addView(this.emojiRationalTopTextView);
        this.emojiRationalLayout.addView(this.emojiRationalTextView, LayoutHelper.createLinear(-2, -2, 0.0f, 8.0f, 0.0f, 0.0f));
        this.emojiRationalLayout.setPadding(AndroidUtilities.dp(18.0f), AndroidUtilities.dp(80.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
        int i = 0;
        while (i < 4) {
            this.emojiViews[i] = new BackupImageView(context);
            this.emojiViews[i].getImageReceiver().setAspectFit(true);
            this.emojiLayout.addView(this.emojiViews[i], LayoutHelper.createLinear(25, 25, i == 0 ? 0.0f : 6.0f, 0.0f, 0.0f, 0.0f));
            i++;
        }
        LinearLayout linearLayout2 = new LinearLayout(context) { // from class: org.telegram.ui.VoIPFragment.6
            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                VoIPServiceState sharedState = VoIPService.getSharedState();
                CharSequence text = VoIPFragment.this.callingUserTitle.getText();
                if (sharedState == null || TextUtils.isEmpty(text)) {
                    return;
                }
                StringBuilder sb = new StringBuilder(text);
                sb.append(", ");
                sb.append(LocaleController.getString((sharedState.getPrivateCall() == null || !sharedState.getPrivateCall().video) ? R.string.VoipInCallBranding : R.string.VoipInVideoCallBranding));
                long callDuration = sharedState.getCallDuration();
                if (callDuration > 0) {
                    sb.append(", ");
                    sb.append(LocaleController.formatDuration((int) (callDuration / 1000)));
                }
                accessibilityNodeInfo.setText(sb);
            }
        };
        this.statusLayout = linearLayout2;
        linearLayout2.setOrientation(1);
        this.statusLayout.setFocusable(true);
        this.statusLayout.setFocusableInTouchMode(true);
        this.callingUserPhotoViewMini = new ImageWithWavesView(context);
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setInfo(this.callingUser);
        this.callingUserPhotoViewMini.setImage(ImageLocation.getForUserOrChat(this.callingUser, 0), null, avatarDrawable, this.callingUser);
        this.callingUserPhotoViewMini.setRoundRadius(AndroidUtilities.dp(135.0f) / 2);
        TextView textView3 = new TextView(context);
        this.callingUserTitle = textView3;
        textView3.setTextSize(1, 28.0f);
        TLRPC.User user = this.callingUser;
        this.callingUserTitle.setText(Emoji.replaceEmoji(ContactsController.formatName(user.first_name, user.last_name), this.callingUserTitle.getPaint().getFontMetricsInt(), false));
        this.callingUserTitle.setMaxLines(2);
        this.callingUserTitle.setEllipsize(TextUtils.TruncateAt.END);
        this.callingUserTitle.setTextColor(-1);
        this.callingUserTitle.setGravity(1);
        this.callingUserTitle.setImportantForAccessibility(2);
        this.statusLayout.addView(this.callingUserTitle, LayoutHelper.createLinear(-2, -2, 1, 8, 0, 8, 6));
        VoIPStatusTextView voIPStatusTextView = new VoIPStatusTextView(context, this.backgroundProvider);
        this.statusTextView = voIPStatusTextView;
        ViewCompat.setImportantForAccessibility(voIPStatusTextView, 4);
        this.statusLayout.addView(this.statusTextView, LayoutHelper.createLinear(-2, -2, 1, 0, 0, 0, 6));
        this.statusLayout.setClipChildren(false);
        this.statusLayout.setClipToPadding(false);
        this.statusLayout.setPadding(0, 0, 0, AndroidUtilities.dp(15.0f));
        this.endCloseLayout = new EndCloseLayout(context);
        this.rateCallLayout = new RateCallLayout(context, this.backgroundProvider);
        this.endCloseLayout.setAlpha(0.0f);
        this.rateCallLayout.setVisibility(8);
        frameLayout.addView(this.callingUserPhotoViewMini, LayoutHelper.createFrame(NotificationCenter.groupPackUpdated, 204.0f, 1, 0.0f, 93.0f, 0.0f, 0.0f));
        frameLayout.addView(this.statusLayout, LayoutHelper.createFrame(-1, -2.0f, 0, 0.0f, 135.0f, 0.0f, 0.0f));
        frameLayout.addView(this.hideEmojiLayout, LayoutHelper.createFrame(-2, -2.0f, 1, 0.0f, 0.0f, 0.0f, 0.0f));
        frameLayout.addView(this.emojiRationalLayout, LayoutHelper.createFrame(NotificationCenter.stealthModeChanged, -2.0f, 49, 0.0f, 118.0f, 0.0f, 0.0f));
        frameLayout.addView(this.emojiLayout, LayoutHelper.createFrame(-2, -2.0f, 1, 0.0f, 0.0f, 0.0f, 0.0f));
        frameLayout.addView(this.endCloseLayout, LayoutHelper.createFrame(-1, 52.0f, 5, 0.0f, 0.0f, 0.0f, 0.0f));
        frameLayout.addView(this.rateCallLayout, LayoutHelper.createFrame(-1, -2.0f, 3, 0.0f, 380.0f, 0.0f, 0.0f));
        this.buttonsLayout = new VoIPButtonsLayout(context);
        this.bottomSpeakerBtn = new VoIpSwitchLayout(context, this.backgroundProvider);
        this.bottomVideoBtn = new VoIpSwitchLayout(context, this.backgroundProvider);
        this.bottomMuteBtn = new VoIpSwitchLayout(context, this.backgroundProvider);
        this.bottomEndCallBtn = new VoIPToggleButton(context) { // from class: org.telegram.ui.VoIPFragment.7
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchSetPressed(boolean z) {
                super.dispatchSetPressed(z);
                setPressedBtn(z);
            }
        };
        this.bottomSpeakerBtn.setTranslationY(AndroidUtilities.dp(100.0f));
        this.bottomSpeakerBtn.setScaleX(0.0f);
        this.bottomSpeakerBtn.setScaleY(0.0f);
        this.bottomSpeakerBtn.animate().setStartDelay(150).translationY(0.0f).scaleY(1.0f).scaleX(1.0f).setDuration(250L).start();
        this.bottomVideoBtn.setTranslationY(AndroidUtilities.dp(100.0f));
        this.bottomVideoBtn.setScaleX(0.0f);
        this.bottomVideoBtn.setScaleY(0.0f);
        this.bottomVideoBtn.animate().setStartDelay(NotificationCenter.applyGroupCallVisibleParticipants).translationY(0.0f).scaleY(1.0f).scaleX(1.0f).setDuration(250L).start();
        this.bottomMuteBtn.setTranslationY(AndroidUtilities.dp(100.0f));
        this.bottomMuteBtn.setScaleX(0.0f);
        this.bottomMuteBtn.setScaleY(0.0f);
        this.bottomMuteBtn.animate().setStartDelay(NotificationCenter.didStartedMultiGiftsSelector).translationY(0.0f).scaleY(1.0f).scaleX(1.0f).setDuration(250L).start();
        this.bottomEndCallBtn.setTranslationY(AndroidUtilities.dp(100.0f));
        this.bottomEndCallBtn.setScaleX(0.0f);
        this.bottomEndCallBtn.setScaleY(0.0f);
        this.bottomEndCallBtn.animate().setStartDelay(NotificationCenter.emojiKeywordsLoaded).translationY(0.0f).scaleY(1.0f).scaleX(1.0f).setDuration(250L).start();
        this.buttonsLayout.addView(this.bottomSpeakerBtn);
        this.buttonsLayout.addView(this.bottomVideoBtn);
        this.buttonsLayout.addView(this.bottomMuteBtn);
        this.buttonsLayout.addView(this.bottomEndCallBtn);
        AcceptDeclineView acceptDeclineView = new AcceptDeclineView(context);
        this.acceptDeclineView = acceptDeclineView;
        acceptDeclineView.setListener(new 8());
        this.acceptDeclineView.setScaleX(1.15f);
        this.acceptDeclineView.setScaleY(1.15f);
        frameLayout.addView(this.buttonsLayout, LayoutHelper.createFrame(-1, -2, 80));
        float f = AndroidUtilities.isTablet() ? 100 : 27;
        frameLayout.addView(this.acceptDeclineView, LayoutHelper.createFrame(-1, 186.0f, 80, f, 0.0f, f, 0.0f));
        ImageView imageView = new ImageView(context);
        this.backIcon = imageView;
        imageView.setBackground(Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(-1, 76)));
        this.backIcon.setImageResource(R.drawable.msg_call_minimize_shadow);
        this.backIcon.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
        this.backIcon.setContentDescription(LocaleController.getString(R.string.Back));
        frameLayout.addView(this.backIcon, LayoutHelper.createFrame(56, 56, 51));
        ImageView imageView2 = new ImageView(context) { // from class: org.telegram.ui.VoIPFragment.9
            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                accessibilityNodeInfo.setClassName(ToggleButton.class.getName());
                accessibilityNodeInfo.setCheckable(true);
                VoIPService sharedInstance = VoIPService.getSharedInstance();
                if (sharedInstance != null) {
                    accessibilityNodeInfo.setChecked(sharedInstance.isSpeakerphoneOn());
                }
            }
        };
        this.speakerPhoneIcon = imageView2;
        imageView2.setContentDescription(LocaleController.getString(R.string.VoipSpeaker));
        this.speakerPhoneIcon.setBackground(Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(-1, 76)));
        this.speakerPhoneIcon.setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f));
        frameLayout.addView(this.speakerPhoneIcon, LayoutHelper.createFrame(56, 56, 53));
        this.speakerPhoneIcon.setAlpha(0.0f);
        this.speakerPhoneIcon.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda29
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                VoIPFragment.this.lambda$createView$10(view3);
            }
        });
        this.backIcon.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda30
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                VoIPFragment.this.lambda$createView$11(view3);
            }
        });
        if (this.windowView.isLockOnScreen()) {
            this.backIcon.setVisibility(8);
        }
        VoIPNotificationsLayout voIPNotificationsLayout = new VoIPNotificationsLayout(context, this.backgroundProvider);
        this.notificationsLayout = voIPNotificationsLayout;
        voIPNotificationsLayout.setGravity(80);
        this.notificationsLayout.setOnViewsUpdated(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                VoIPFragment.this.lambda$createView$12();
            }
        });
        frameLayout.addView(this.notificationsLayout, LayoutHelper.createFrame(-1, 200.0f, 80, 16.0f, 0.0f, 16.0f, 0.0f));
        HintView2 multilineText = new VoIpHintView(context, 3, this.backgroundProvider, true).setMultilineText(true);
        Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
        HintView2 rounding = multilineText.setTextAlign(alignment).setDuration(-1L).setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                VoIPFragment.this.startWaitingFoHideUi();
            }
        }).setHideByTouch(true).setMaxWidth(320.0f).useScale(true).setInnerPadding(10.0f, 6.0f, 10.0f, 6.0f).setRounding(8.0f);
        this.tapToVideoTooltip = rounding;
        rounding.setText(LocaleController.getString(R.string.TapToTurnCamera));
        frameLayout.addView(this.tapToVideoTooltip, LayoutHelper.createFrame(-2, -2.0f, 80, 19.0f, 0.0f, 19.0f, 0.0f));
        HintView2 rounding2 = new VoIpHintView(context, 1, this.backgroundProvider, false).setMultilineText(true).setTextAlign(alignment).setDuration(4000L).setHideByTouch(true).setMaxWidth(320.0f).useScale(true).setInnerPadding(10.0f, 6.0f, 10.0f, 6.0f).setRounding(8.0f);
        this.encryptionTooltip = rounding2;
        rounding2.setText(LocaleController.getString(R.string.VoipHintEncryptionKey));
        frameLayout.addView(this.encryptionTooltip, LayoutHelper.createFrame(-2, -2.0f, 1, 0.0f, 0.0f, 0.0f, 0.0f));
        updateViewState();
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null) {
            if (!this.isVideoCall) {
                TL_phone.PhoneCall phoneCall = sharedInstance.privateCall;
                this.isVideoCall = phoneCall != null && phoneCall.video;
            }
            initRenderers();
        }
        return frameLayout;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.voipServiceCreated) {
            if (this.currentState != 17 || VoIPService.getSharedInstance() == null) {
                return;
            }
            this.currentUserTextureView.renderer.release();
            this.callingUserTextureView.renderer.release();
            this.callingUserMiniTextureRenderer.release();
            initRenderers();
            VoIPService.getSharedInstance().registerStateListener(this);
            return;
        }
        if (i == NotificationCenter.emojiLoaded) {
            updateKeyView(true);
            return;
        }
        if (i == NotificationCenter.closeInCallActivity) {
            this.windowView.finish();
            return;
        }
        if (i == NotificationCenter.webRtcSpeakerAmplitudeEvent) {
            this.callingUserPhotoViewMini.setAmplitude(((Float) objArr[0]).floatValue() * 15.0f);
            return;
        }
        if (i == NotificationCenter.nearEarEvent) {
            boolean booleanValue = ((Boolean) objArr[0]).booleanValue();
            this.isNearEar = booleanValue;
            if (booleanValue) {
                this.callingUserPhotoViewMini.setMute(true, true);
            }
        }
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onAudioSettingsChanged() {
        updateButtons(true);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onCameraFirstFrameAvailable() {
        VoIPService.StateListener.-CC.$default$onCameraFirstFrameAvailable(this);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onCameraSwitch(boolean z) {
        this.previousState = this.currentState;
        updateViewState();
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onMediaStateUpdated(int i, int i2) {
        this.previousState = this.currentState;
        if (i2 == 2 && !this.isVideoCall) {
            this.isVideoCall = true;
        }
        updateViewState();
    }

    public void onPauseInternal() {
        VoIPService sharedInstance;
        WindowInsets windowInsets;
        int systemWindowInsetTop;
        int systemWindowInsetBottom;
        PowerManager powerManager = (PowerManager) this.activity.getSystemService("power");
        int i = Build.VERSION.SDK_INT;
        boolean isInteractive = i >= 20 ? powerManager.isInteractive() : powerManager.isScreenOn();
        boolean checkInlinePermissions = AndroidUtilities.checkInlinePermissions(this.activity);
        if (this.canSwitchToPip && checkInlinePermissions) {
            int measuredHeight = instance.windowView.getMeasuredHeight();
            VoIPFragment voIPFragment = instance;
            VoIPPiPView.show(voIPFragment.activity, voIPFragment.currentAccount, voIPFragment.windowView.getMeasuredWidth(), measuredHeight, 0);
            if (i >= 20 && (windowInsets = instance.lastInsets) != null) {
                systemWindowInsetTop = windowInsets.getSystemWindowInsetTop();
                VoIPPiPView.topInset = systemWindowInsetTop;
                systemWindowInsetBottom = instance.lastInsets.getSystemWindowInsetBottom();
                VoIPPiPView.bottomInset = systemWindowInsetBottom;
            }
        }
        if (this.currentUserIsVideo) {
            if ((checkInlinePermissions && isInteractive) || (sharedInstance = VoIPService.getSharedInstance()) == null) {
                return;
            }
            sharedInstance.setVideoState(false, 1);
        }
    }

    public void onResumeInternal() {
        if (VoIPPiPView.getInstance() != null) {
            VoIPPiPView.finish();
        }
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null) {
            if (sharedInstance.getVideoState(false) == 1) {
                sharedInstance.setVideoState(false, 2);
            }
            updateViewState();
        } else if (VoIPService.getSharedState() == null) {
            this.windowView.finish();
        }
        this.deviceIsLocked = ((KeyguardManager) this.activity.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }

    public void onScreenCastStart() {
        PrivateVideoPreviewDialogNew privateVideoPreviewDialogNew = this.previewDialog;
        if (privateVideoPreviewDialogNew == null) {
            return;
        }
        privateVideoPreviewDialogNew.dismiss(true, true);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onScreenOnChange(boolean z) {
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onSignalBarsCountChanged(final int i) {
        VoIpGradientLayout voIpGradientLayout;
        if (i > 0) {
            this.signalBarWasReceived = true;
        }
        if (this.statusTextView == null || (voIpGradientLayout = this.gradientLayout) == null || !voIpGradientLayout.isConnectedCalled() || !this.signalBarWasReceived) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                VoIPFragment.this.lambda$onSignalBarsCountChanged$4(i);
            }
        }, 400L);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onStateChanged(int i) {
        int i2 = this.currentState;
        if (i2 != i) {
            this.previousState = i2;
            this.currentState = i;
            if (this.windowView != null) {
                updateViewState();
            }
        }
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onVideoAvailableChange(boolean z) {
        this.previousState = this.currentState;
        if (z && !this.isVideoCall) {
            this.isVideoCall = true;
        }
        updateViewState();
    }

    public TLRPC.Document replaceEmojiToLottieFrame(CharSequence charSequence, int[] iArr) {
        if (!(charSequence instanceof Spannable)) {
            return null;
        }
        Spannable spannable = (Spannable) charSequence;
        Emoji.EmojiSpan[] emojiSpanArr = (Emoji.EmojiSpan[]) spannable.getSpans(0, spannable.length(), Emoji.EmojiSpan.class);
        AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) spannable.getSpans(0, spannable.length(), AnimatedEmojiSpan.class);
        if (emojiSpanArr != null) {
            if (((iArr == null ? 0 : iArr[0]) - emojiSpanArr.length) - (animatedEmojiSpanArr == null ? 0 : animatedEmojiSpanArr.length) <= 0 && emojiSpanArr.length > 0) {
                return MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(emojiSpanArr[0].emoji);
            }
        }
        return null;
    }

    public void startTransitionFromPiP() {
        this.enterFromPiP = true;
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null && sharedInstance.getVideoState(false) == 2) {
            this.callingUserTextureView.setStub(VoIPPiPView.getInstance().callingUserTextureView);
            this.currentUserTextureView.setStub(VoIPPiPView.getInstance().currentUserTextureView);
        }
        this.windowView.setAlpha(0.0f);
        updateViewState();
        this.switchingToPip = true;
        VoIPPiPView.switchingToPip = true;
        VoIPPiPView.prepareForTransition();
        this.notificationsLocker.lock();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.VoIPFragment$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                VoIPFragment.this.lambda$startTransitionFromPiP$17();
            }
        }, 32L);
    }

    public void switchToPip() {
        WindowInsets windowInsets;
        int systemWindowInsetTop;
        int systemWindowInsetBottom;
        if (this.isFinished || !AndroidUtilities.checkInlinePermissions(this.activity) || instance == null) {
            return;
        }
        this.isFinished = true;
        if (VoIPService.getSharedInstance() != null) {
            int measuredHeight = instance.windowView.getMeasuredHeight();
            VoIPFragment voIPFragment = instance;
            VoIPPiPView.show(voIPFragment.activity, voIPFragment.currentAccount, voIPFragment.windowView.getMeasuredWidth(), measuredHeight, 1);
            if (Build.VERSION.SDK_INT >= 20 && (windowInsets = instance.lastInsets) != null) {
                systemWindowInsetTop = windowInsets.getSystemWindowInsetTop();
                VoIPPiPView.topInset = systemWindowInsetTop;
                systemWindowInsetBottom = instance.lastInsets.getSystemWindowInsetBottom();
                VoIPPiPView.bottomInset = systemWindowInsetBottom;
            }
        }
        if (VoIPPiPView.getInstance() == null) {
            return;
        }
        ViewPropertyAnimator duration = this.speakerPhoneIcon.animate().alpha(0.0f).setDuration(150L);
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
        duration.setInterpolator(cubicBezierInterpolator).start();
        this.backIcon.animate().alpha(0.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
        this.emojiLayout.animate().alpha(0.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
        this.statusLayout.animate().alpha(0.0f).setDuration(150L).setInterpolator(cubicBezierInterpolator).start();
        this.buttonsLayout.animate().alpha(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).start();
        this.bottomShadow.animate().alpha(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).start();
        this.topShadow.animate().alpha(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).start();
        this.callingUserMiniFloatingLayout.animate().alpha(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).start();
        this.notificationsLayout.animate().alpha(0.0f).setDuration(350L).setInterpolator(cubicBezierInterpolator).start();
        VoIPPiPView.switchingToPip = true;
        this.switchingToPip = true;
        Animator createPiPTransition = createPiPTransition(false);
        this.notificationsLocker.lock();
        createPiPTransition.addListener(new 14());
        createPiPTransition.setDuration(350L);
        createPiPTransition.setInterpolator(cubicBezierInterpolator);
        createPiPTransition.start();
    }
}
