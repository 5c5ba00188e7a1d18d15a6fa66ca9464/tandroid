package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.util.Random;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;

/* loaded from: classes3.dex */
public class GroupCallPipButton extends FrameLayout implements NotificationCenter.NotificationCenterDelegate, VoIPService.StateListener {
    float amplitude;
    float animateAmplitudeDiff;
    float animateToAmplitude;
    private RLottieDrawable bigMicDrawable;
    BlobDrawable blobDrawable;
    BlobDrawable blobDrawable2;
    private final int currentAccount;
    WeavingState currentState;
    long lastStubUpdateAmplitude;
    Matrix matrix;
    private RLottieImageView muteButton;
    OvershootInterpolator overshootInterpolator;
    Paint paint;
    float pinnedProgress;
    boolean prepareToRemove;
    private final LinearGradient prepareToRemoveShader;
    float pressedProgress;
    boolean pressedState;
    WeavingState previousState;
    float progressToPrepareRemove;
    float progressToState;
    Random random;
    float removeAngle;
    public boolean removed;
    WeavingState[] states;
    private boolean stub;
    float wavesEnter;

    public static class WeavingState {
        int color1;
        int color2;
        int color3;
        private final int currentState;
        private float duration;
        public Shader shader;
        private float startX;
        private float startY;
        private float time;
        private float targetX = -1.0f;
        private float targetY = -1.0f;
        private final Matrix matrix = new Matrix();

        public WeavingState(int i) {
            this.currentState = i;
        }

        private void updateTargets() {
            float nextInt;
            float f;
            int i = this.currentState;
            if (i == 0) {
                this.targetX = ((Utilities.random.nextInt(100) * 0.1f) / 100.0f) + 0.2f;
                f = ((Utilities.random.nextInt(100) * 0.1f) / 100.0f) + 0.7f;
            } else {
                if (i == 3) {
                    this.targetX = ((Utilities.random.nextInt(100) * 0.1f) / 100.0f) + 0.6f;
                    nextInt = Utilities.random.nextInt(100) * 0.1f;
                } else {
                    this.targetX = ((Utilities.random.nextInt(100) / 100.0f) * 0.2f) + 0.8f;
                    nextInt = Utilities.random.nextInt(100);
                }
                f = nextInt / 100.0f;
            }
            this.targetY = f;
        }

        public void setToPaint(Paint paint) {
            if (this.currentState != 2) {
                paint.setShader(this.shader);
            } else {
                paint.setShader(null);
                paint.setColor(Theme.getColor(Theme.key_voipgroup_topPanelGray));
            }
        }

        public void update(long j, float f) {
            RadialGradient radialGradient;
            int i = this.currentState;
            if (i == 0) {
                int i2 = this.color1;
                int i3 = Theme.key_voipgroup_overlayGreen1;
                if (i2 != Theme.getColor(i3) || this.color2 != Theme.getColor(Theme.key_voipgroup_overlayGreen2)) {
                    int color = Theme.getColor(i3);
                    this.color1 = color;
                    int color2 = Theme.getColor(Theme.key_voipgroup_overlayGreen2);
                    this.color2 = color2;
                    radialGradient = new RadialGradient(200.0f, 200.0f, 200.0f, new int[]{color, color2}, (float[]) null, Shader.TileMode.CLAMP);
                    this.shader = radialGradient;
                }
            } else if (i == 1) {
                int i4 = this.color1;
                int i5 = Theme.key_voipgroup_overlayBlue1;
                if (i4 != Theme.getColor(i5) || this.color2 != Theme.getColor(Theme.key_voipgroup_overlayBlue2)) {
                    int color3 = Theme.getColor(i5);
                    this.color1 = color3;
                    int color4 = Theme.getColor(Theme.key_voipgroup_overlayBlue2);
                    this.color2 = color4;
                    radialGradient = new RadialGradient(200.0f, 200.0f, 200.0f, new int[]{color3, color4}, (float[]) null, Shader.TileMode.CLAMP);
                    this.shader = radialGradient;
                }
            } else {
                if (i != 3) {
                    return;
                }
                int i6 = this.color1;
                int i7 = Theme.key_voipgroup_mutedByAdminGradient;
                if (i6 != Theme.getColor(i7) || this.color2 != Theme.getColor(Theme.key_voipgroup_mutedByAdminGradient2) || this.color3 != Theme.getColor(Theme.key_voipgroup_mutedByAdminGradient3)) {
                    int color5 = Theme.getColor(Theme.key_voipgroup_mutedByAdminGradient2);
                    this.color2 = color5;
                    int color6 = Theme.getColor(Theme.key_voipgroup_mutedByAdminGradient3);
                    this.color3 = color6;
                    int color7 = Theme.getColor(i7);
                    this.color1 = color7;
                    radialGradient = new RadialGradient(200.0f, 200.0f, 200.0f, new int[]{color5, color6, color7}, (float[]) null, Shader.TileMode.CLAMP);
                    this.shader = radialGradient;
                }
            }
            int dp = AndroidUtilities.dp(130.0f);
            float f2 = this.duration;
            if (f2 == 0.0f || this.time >= f2) {
                this.duration = Utilities.random.nextInt(700) + 500;
                this.time = 0.0f;
                if (this.targetX == -1.0f) {
                    updateTargets();
                }
                this.startX = this.targetX;
                this.startY = this.targetY;
                updateTargets();
            }
            float f3 = j;
            float f4 = this.time + ((BlobDrawable.GRADIENT_SPEED_MIN + 0.5f) * f3) + (f3 * BlobDrawable.GRADIENT_SPEED_MAX * 2.0f * f);
            this.time = f4;
            float f5 = this.duration;
            if (f4 > f5) {
                this.time = f5;
            }
            float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.time / f5);
            float f6 = dp;
            float f7 = this.startX;
            float f8 = ((f7 + ((this.targetX - f7) * interpolation)) * f6) - 200.0f;
            float f9 = this.startY;
            float f10 = ((f9 + ((this.targetY - f9) * interpolation)) * f6) - 200.0f;
            float f11 = (f6 / 400.0f) * (this.currentState != 3 ? 1.5f : 2.0f);
            this.matrix.reset();
            this.matrix.postTranslate(f8, f10);
            this.matrix.postScale(f11, f11, f8 + 200.0f, f10 + 200.0f);
            this.shader.setLocalMatrix(this.matrix);
        }
    }

    public GroupCallPipButton(Context context, int i, boolean z) {
        super(context);
        this.paint = new Paint(1);
        this.blobDrawable = new BlobDrawable(8);
        this.blobDrawable2 = new BlobDrawable(9);
        this.progressToState = 1.0f;
        this.matrix = new Matrix();
        this.wavesEnter = 0.0f;
        this.random = new Random();
        this.states = new WeavingState[4];
        this.overshootInterpolator = new OvershootInterpolator();
        this.stub = z;
        this.currentAccount = i;
        for (int i2 = 0; i2 < 4; i2++) {
            this.states[i2] = new WeavingState(i2);
        }
        this.blobDrawable.maxRadius = AndroidUtilities.dp(37.0f);
        this.blobDrawable.minRadius = AndroidUtilities.dp(32.0f);
        this.blobDrawable2.maxRadius = AndroidUtilities.dp(37.0f);
        this.blobDrawable2.minRadius = AndroidUtilities.dp(32.0f);
        this.blobDrawable.generateBlob();
        this.blobDrawable2.generateBlob();
        int i3 = R.raw.voice_outlined;
        this.bigMicDrawable = new RLottieDrawable(i3, "" + i3, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(30.0f), true, null);
        setWillNotDraw(false);
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.muteButton = rLottieImageView;
        rLottieImageView.setAnimation(this.bigMicDrawable);
        this.muteButton.setScaleType(ImageView.ScaleType.CENTER);
        addView(this.muteButton);
        this.prepareToRemoveShader = new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(350.0f), 0.0f, new int[]{-2801343, -561538, 0}, new float[]{0.0f, 0.4f, 1.0f}, Shader.TileMode.CLAMP);
        if (z) {
            setState(0);
        }
    }

    private void setAmplitude(double d) {
        float min = (float) (Math.min(8500.0d, d) / 8500.0d);
        this.animateToAmplitude = min;
        this.animateAmplitudeDiff = (min - this.amplitude) / ((BlobDrawable.AMPLITUDE_SPEED * 500.0f) + 100.0f);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateButtonState() {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null || sharedInstance.groupCall == null) {
            return;
        }
        int callState = sharedInstance.getCallState();
        if (callState == 1 || callState == 2 || callState == 6 || callState == 5) {
            setState(2);
            return;
        }
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) sharedInstance.groupCall.participants.get(sharedInstance.getSelfId());
        if (tL_groupCallParticipant == null || tL_groupCallParticipant.can_self_unmute || !tL_groupCallParticipant.muted || ChatObject.canManageCalls(sharedInstance.getChat())) {
            setState(sharedInstance.isMicMute() ? 1 : 0);
            return;
        }
        if (!sharedInstance.isMicMute()) {
            sharedInstance.setMicMute(true, false, false);
        }
        setState(3);
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
        if (getParent() != null) {
            ((View) getParent()).dispatchTouchEvent(obtain);
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.webRtcMicAmplitudeEvent) {
            setAmplitude(((Float) objArr[0]).floatValue() * 4000.0f);
        } else if (i == NotificationCenter.groupCallUpdated) {
            updateButtonState();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.stub) {
            return;
        }
        setAmplitude(0.0d);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.webRtcMicAmplitudeEvent);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.groupCallUpdated);
        boolean z = VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().isMicMute();
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().registerStateListener(this);
        }
        this.bigMicDrawable.setCustomEndFrame(z ? 13 : 24);
        RLottieDrawable rLottieDrawable = this.bigMicDrawable;
        rLottieDrawable.setCurrentFrame(rLottieDrawable.getCustomEndFrame() - 1, false, true);
        updateButtonState();
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onAudioSettingsChanged() {
        RLottieDrawable rLottieDrawable;
        int i = 0;
        boolean z = VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().isMicMute();
        if (this.bigMicDrawable.setCustomEndFrame(z ? 13 : 24)) {
            if (z) {
                rLottieDrawable = this.bigMicDrawable;
            } else {
                rLottieDrawable = this.bigMicDrawable;
                i = 12;
            }
            rLottieDrawable.setCurrentFrame(i);
        }
        this.muteButton.playAnimation();
        updateButtonState();
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onCameraFirstFrameAvailable() {
        VoIPService.StateListener.-CC.$default$onCameraFirstFrameAvailable(this);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onCameraSwitch(boolean z) {
        VoIPService.StateListener.-CC.$default$onCameraSwitch(this, z);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.stub) {
            return;
        }
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.webRtcMicAmplitudeEvent);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupCallUpdated);
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().unregisterStateListener(this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:106:0x02ce  */
    /* JADX WARN: Removed duplicated region for block: B:111:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x016a  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0158  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0129  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0109  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0117  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0154  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0227  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x029a  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x02a5  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0236  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float f;
        float f2;
        boolean z;
        int i;
        int i2;
        boolean z2;
        float f3;
        WeavingState weavingState;
        Paint paint;
        super.onDraw(canvas);
        if (getAlpha() == 0.0f) {
            return;
        }
        float measuredWidth = getMeasuredWidth() >> 1;
        float measuredHeight = getMeasuredHeight() >> 1;
        boolean z3 = this.pressedState;
        if (z3) {
            float f4 = this.pressedProgress;
            if (f4 != 1.0f) {
                float f5 = f4 + 0.10666667f;
                this.pressedProgress = f5;
                if (f5 > 1.0f) {
                    this.pressedProgress = 1.0f;
                }
                float interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(this.pressedProgress) * 0.1f;
                float f6 = interpolation + 1.0f;
                this.muteButton.setScaleY(f6);
                this.muteButton.setScaleX(f6);
                if (this.stub) {
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastStubUpdateAmplitude > 1000) {
                        this.lastStubUpdateAmplitude = currentTimeMillis;
                        float abs = ((Math.abs(this.random.nextInt() % 100) * 0.5f) / 100.0f) + 0.5f;
                        this.animateToAmplitude = abs;
                        this.animateAmplitudeDiff = (abs - this.amplitude) / ((BlobDrawable.AMPLITUDE_SPEED * 1500.0f) + 100.0f);
                    }
                }
                f = this.animateToAmplitude;
                f2 = this.amplitude;
                if (f != f2) {
                    float f7 = this.animateAmplitudeDiff;
                    float f8 = f2 + (16.0f * f7);
                    this.amplitude = f8;
                    if (f7 <= 0.0f ? f8 < f : f8 > f) {
                        this.amplitude = f;
                    }
                }
                if (this.previousState != null) {
                    float f9 = this.progressToState + 0.064f;
                    this.progressToState = f9;
                    if (f9 > 1.0f) {
                        this.progressToState = 1.0f;
                        this.previousState = null;
                    }
                }
                z = this.prepareToRemove;
                if (z) {
                    float f10 = this.progressToPrepareRemove;
                    if (f10 != 1.0f) {
                        float f11 = f10 + 0.045714285f;
                        this.progressToPrepareRemove = f11;
                        if (f11 > 1.0f) {
                            this.progressToPrepareRemove = 1.0f;
                        }
                        if (this.removed) {
                            invalidate();
                        }
                        i = 0;
                        z2 = this.currentState.currentState == 3 && this.currentState.currentState != 2;
                        if (z2) {
                            float f12 = this.wavesEnter;
                            if (f12 != 1.0f) {
                                float f13 = f12 + 0.045714285f;
                                this.wavesEnter = f13;
                                if (f13 > 1.0f) {
                                    this.wavesEnter = 1.0f;
                                }
                                float interpolation2 = (this.overshootInterpolator.getInterpolation(this.wavesEnter) * 0.35f) + 0.65f;
                                this.blobDrawable.update(this.amplitude, !this.stub ? 0.1f : 0.8f);
                                this.blobDrawable2.update(this.amplitude, !this.stub ? 0.1f : 0.8f);
                                for (i2 = 3; i < i2; i2 = 3) {
                                    if (i != 0 || this.previousState != null) {
                                        if (i == 0) {
                                            if (this.progressToPrepareRemove != 1.0f) {
                                                f3 = 1.0f - this.progressToState;
                                                this.previousState.update(16L, this.amplitude);
                                                weavingState = this.previousState;
                                                weavingState.setToPaint(this.paint);
                                                this.blobDrawable.maxRadius = AndroidUtilities.dp(40.0f);
                                                this.blobDrawable.minRadius = AndroidUtilities.dp(32.0f);
                                                this.blobDrawable2.maxRadius = AndroidUtilities.dp(38.0f);
                                                this.blobDrawable2.minRadius = AndroidUtilities.dp(33.0f);
                                                if (i != 2) {
                                                    this.paint.setAlpha((int) (76.0f * f3 * (1.0f - this.progressToPrepareRemove)));
                                                } else {
                                                    this.paint.setAlpha((int) (76.0f * f3 * this.progressToPrepareRemove));
                                                }
                                                if (this.wavesEnter != 0.0f) {
                                                    float min = Math.min(((this.amplitude * 0.3f) + 1.0f + interpolation) * (1.0f - this.pinnedProgress), 1.3f) * interpolation2;
                                                    canvas.save();
                                                    canvas.scale(min, min, measuredWidth, measuredHeight);
                                                    this.blobDrawable.draw(measuredWidth, measuredHeight, canvas, this.paint);
                                                    canvas.restore();
                                                    float min2 = Math.min(((this.amplitude * 0.26f) + 1.0f + interpolation) * (1.0f - this.pinnedProgress), 1.3f) * interpolation2;
                                                    canvas.save();
                                                    canvas.scale(min2, min2, measuredWidth, measuredHeight);
                                                    this.blobDrawable2.draw(measuredWidth, measuredHeight, canvas, this.paint);
                                                    canvas.restore();
                                                }
                                                if (i == 2) {
                                                    paint = this.paint;
                                                    f3 = this.progressToPrepareRemove;
                                                } else if (i == 1) {
                                                    paint = this.paint;
                                                } else {
                                                    this.paint.setAlpha(NotificationCenter.liveLocationsChanged);
                                                    canvas.save();
                                                    canvas.scale(f6, f6, measuredWidth, measuredHeight);
                                                    canvas.drawCircle(measuredWidth, measuredHeight, AndroidUtilities.dp(32.0f), this.paint);
                                                    canvas.restore();
                                                }
                                                paint.setAlpha((int) (f3 * 255.0f));
                                                canvas.save();
                                                canvas.scale(f6, f6, measuredWidth, measuredHeight);
                                                canvas.drawCircle(measuredWidth, measuredHeight, AndroidUtilities.dp(32.0f), this.paint);
                                                canvas.restore();
                                            }
                                        } else if (i == 1) {
                                            WeavingState weavingState2 = this.currentState;
                                            if (weavingState2 == null) {
                                                return;
                                            }
                                            if (this.progressToPrepareRemove != 1.0f) {
                                                f3 = this.previousState != null ? this.progressToState : 1.0f;
                                                weavingState2.update(16L, this.amplitude);
                                                weavingState = this.currentState;
                                                weavingState.setToPaint(this.paint);
                                                this.blobDrawable.maxRadius = AndroidUtilities.dp(40.0f);
                                                this.blobDrawable.minRadius = AndroidUtilities.dp(32.0f);
                                                this.blobDrawable2.maxRadius = AndroidUtilities.dp(38.0f);
                                                this.blobDrawable2.minRadius = AndroidUtilities.dp(33.0f);
                                                if (i != 2) {
                                                }
                                                if (this.wavesEnter != 0.0f) {
                                                }
                                                if (i == 2) {
                                                }
                                                paint.setAlpha((int) (f3 * 255.0f));
                                                canvas.save();
                                                canvas.scale(f6, f6, measuredWidth, measuredHeight);
                                                canvas.drawCircle(measuredWidth, measuredHeight, AndroidUtilities.dp(32.0f), this.paint);
                                                canvas.restore();
                                            }
                                        } else if (this.progressToPrepareRemove != 0.0f) {
                                            this.paint.setColor(-65536);
                                            this.matrix.reset();
                                            this.matrix.postTranslate((-AndroidUtilities.dp(250.0f)) * (1.0f - this.progressToPrepareRemove), 0.0f);
                                            this.matrix.postRotate(this.removeAngle, measuredWidth, measuredHeight);
                                            this.prepareToRemoveShader.setLocalMatrix(this.matrix);
                                            this.paint.setShader(this.prepareToRemoveShader);
                                            f3 = 1.0f;
                                            this.blobDrawable.maxRadius = AndroidUtilities.dp(40.0f);
                                            this.blobDrawable.minRadius = AndroidUtilities.dp(32.0f);
                                            this.blobDrawable2.maxRadius = AndroidUtilities.dp(38.0f);
                                            this.blobDrawable2.minRadius = AndroidUtilities.dp(33.0f);
                                            if (i != 2) {
                                            }
                                            if (this.wavesEnter != 0.0f) {
                                            }
                                            if (i == 2) {
                                            }
                                            paint.setAlpha((int) (f3 * 255.0f));
                                            canvas.save();
                                            canvas.scale(f6, f6, measuredWidth, measuredHeight);
                                            canvas.drawCircle(measuredWidth, measuredHeight, AndroidUtilities.dp(32.0f), this.paint);
                                            canvas.restore();
                                        }
                                        i++;
                                    }
                                    i++;
                                }
                                if (!this.removed || this.wavesEnter <= 0.0f) {
                                    return;
                                }
                                invalidate();
                                return;
                            }
                        }
                        if (!z2) {
                            float f14 = this.wavesEnter;
                            if (f14 != 0.0f) {
                                float f15 = f14 - 0.045714285f;
                                this.wavesEnter = f15;
                                if (f15 < 0.0f) {
                                    this.wavesEnter = 0.0f;
                                }
                            }
                        }
                        float interpolation22 = (this.overshootInterpolator.getInterpolation(this.wavesEnter) * 0.35f) + 0.65f;
                        this.blobDrawable.update(this.amplitude, !this.stub ? 0.1f : 0.8f);
                        this.blobDrawable2.update(this.amplitude, !this.stub ? 0.1f : 0.8f);
                        while (i < i2) {
                        }
                        if (this.removed) {
                            return;
                        } else {
                            return;
                        }
                    }
                }
                if (!z) {
                    float f16 = this.progressToPrepareRemove;
                    if (f16 != 0.0f) {
                        float f17 = f16 - 0.045714285f;
                        this.progressToPrepareRemove = f17;
                        if (f17 < 0.0f) {
                            this.progressToPrepareRemove = 0.0f;
                        }
                    }
                }
                i = 0;
                if (this.currentState.currentState == 3) {
                }
                if (z2) {
                }
                if (!z2) {
                }
                float interpolation222 = (this.overshootInterpolator.getInterpolation(this.wavesEnter) * 0.35f) + 0.65f;
                this.blobDrawable.update(this.amplitude, !this.stub ? 0.1f : 0.8f);
                this.blobDrawable2.update(this.amplitude, !this.stub ? 0.1f : 0.8f);
                while (i < i2) {
                }
                if (this.removed) {
                }
            }
        }
        if (!z3) {
            float f18 = this.pressedProgress;
            if (f18 != 0.0f) {
                float f19 = f18 - 0.10666667f;
                this.pressedProgress = f19;
                if (f19 < 0.0f) {
                    this.pressedProgress = 0.0f;
                }
            }
        }
        float interpolation3 = CubicBezierInterpolator.DEFAULT.getInterpolation(this.pressedProgress) * 0.1f;
        float f62 = interpolation3 + 1.0f;
        this.muteButton.setScaleY(f62);
        this.muteButton.setScaleX(f62);
        if (this.stub) {
        }
        f = this.animateToAmplitude;
        f2 = this.amplitude;
        if (f != f2) {
        }
        if (this.previousState != null) {
        }
        z = this.prepareToRemove;
        if (z) {
        }
        if (!z) {
        }
        i = 0;
        if (this.currentState.currentState == 3) {
        }
        if (z2) {
        }
        if (!z2) {
        }
        float interpolation2222 = (this.overshootInterpolator.getInterpolation(this.wavesEnter) * 0.35f) + 0.65f;
        this.blobDrawable.update(this.amplitude, !this.stub ? 0.1f : 0.8f);
        this.blobDrawable2.update(this.amplitude, !this.stub ? 0.1f : 0.8f);
        while (i < i2) {
        }
        if (this.removed) {
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (Build.VERSION.SDK_INT < 21 || GroupCallPip.getInstance() == null) {
            return;
        }
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, LocaleController.getString(GroupCallPip.getInstance().showAlert ? R.string.AccDescrCloseMenu : R.string.AccDescrOpenMenu2)));
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onMediaStateUpdated(int i, int i2) {
        VoIPService.StateListener.-CC.$default$onMediaStateUpdated(this, i, i2);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onScreenOnChange(boolean z) {
        VoIPService.StateListener.-CC.$default$onScreenOnChange(this, z);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onSignalBarsCountChanged(int i) {
        VoIPService.StateListener.-CC.$default$onSignalBarsCountChanged(this, i);
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public void onStateChanged(int i) {
        updateButtonState();
    }

    @Override // org.telegram.messenger.voip.VoIPService.StateListener
    public /* synthetic */ void onVideoAvailableChange(boolean z) {
        VoIPService.StateListener.-CC.$default$onVideoAvailableChange(this, z);
    }

    public void prepareToRemove(boolean z) {
        if (this.prepareToRemove != z) {
            invalidate();
        }
        this.prepareToRemove = z;
    }

    public void setPinnedProgress(float f) {
        this.pinnedProgress = f;
    }

    public void setPressedState(boolean z) {
        this.pressedState = z;
    }

    public void setRemoveAngle(double d) {
        this.removeAngle = (float) d;
    }

    public void setState(int i) {
        StringBuilder sb;
        int i2;
        WeavingState weavingState = this.currentState;
        if (weavingState == null || weavingState.currentState != i) {
            WeavingState weavingState2 = this.currentState;
            this.previousState = weavingState2;
            WeavingState weavingState3 = this.states[i];
            this.currentState = weavingState3;
            float f = 0.0f;
            if (weavingState2 != null) {
                this.progressToState = 0.0f;
            } else {
                this.progressToState = 1.0f;
                if (weavingState3.currentState != 3 && this.currentState.currentState != 2) {
                    f = 1.0f;
                }
                this.wavesEnter = f;
            }
            VoIPService sharedInstance = VoIPService.getSharedInstance();
            String string = LocaleController.getString((sharedInstance == null || !ChatObject.isChannelOrGiga(sharedInstance.getChat())) ? R.string.VoipGroupVoiceChat : R.string.VoipChannelVoiceChat);
            if (i == 0) {
                sb = new StringBuilder();
                sb.append(string);
                sb.append(", ");
                i2 = R.string.VoipTapToMute;
            } else {
                if (i != 2) {
                    if (i == 3) {
                        sb = new StringBuilder();
                        sb.append(string);
                        sb.append(", ");
                        i2 = R.string.VoipMutedByAdmin;
                    }
                    setContentDescription(string);
                    invalidate();
                }
                sb = new StringBuilder();
                sb.append(string);
                sb.append(", ");
                i2 = R.string.Connecting;
            }
            sb.append(LocaleController.getString(i2));
            string = sb.toString();
            setContentDescription(string);
            invalidate();
        }
    }
}
