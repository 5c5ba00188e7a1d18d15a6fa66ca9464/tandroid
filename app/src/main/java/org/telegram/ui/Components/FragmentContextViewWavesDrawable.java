package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.SystemClock;
import android.view.View;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.TLRPC$TL_groupCallParticipant;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class FragmentContextViewWavesDrawable {
    private float amplitude;
    private float amplitude2;
    private float animateAmplitudeDiff;
    private float animateAmplitudeDiff2;
    private float animateToAmplitude;
    WeavingState currentState;
    private long lastUpdateTime;
    WeavingState pausedState;
    WeavingState previousState;
    WeavingState[] states = new WeavingState[4];
    float progressToState = 1.0f;
    ArrayList<View> parents = new ArrayList<>();
    Paint paint = new Paint(1);
    LineBlobDrawable lineBlobDrawable = new LineBlobDrawable(5);
    LineBlobDrawable lineBlobDrawable1 = new LineBlobDrawable(7);
    LineBlobDrawable lineBlobDrawable2 = new LineBlobDrawable(8);
    Path path = new Path();

    public FragmentContextViewWavesDrawable() {
        new RectF();
        new Paint(1);
        for (int i = 0; i < 4; i++) {
            this.states[i] = new WeavingState(i);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00d8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(float f, float f2, float f3, float f4, Canvas canvas, FragmentContextView fragmentContextView, float f5) {
        boolean z;
        long j;
        float f6;
        int i;
        checkColors();
        int i2 = 0;
        boolean z2 = fragmentContextView != null && this.parents.size() > 0 && fragmentContextView == this.parents.get(0);
        if (f2 > f4) {
            return;
        }
        long j2 = 0;
        WeavingState weavingState = this.currentState;
        boolean z3 = (weavingState == null || this.previousState == null || ((weavingState.currentState != 1 || this.previousState.currentState != 0) && (this.previousState.currentState != 1 || this.currentState.currentState != 0))) ? false : true;
        if (z2) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j3 = elapsedRealtime - this.lastUpdateTime;
            this.lastUpdateTime = elapsedRealtime;
            j2 = j3 > 20 ? 17L : j3;
            if (j2 < 3) {
                j = j2;
                z = false;
                float f7 = 1.0f;
                float f8 = 0.0f;
                if (z) {
                    float f9 = this.animateToAmplitude;
                    float f10 = this.amplitude;
                    if (f9 != f10) {
                        float f11 = this.animateAmplitudeDiff;
                        float f12 = f10 + (((float) j) * f11);
                        this.amplitude = f12;
                        if (f11 > 0.0f) {
                            if (f12 > f9) {
                                this.amplitude = f9;
                            }
                        } else if (f12 < f9) {
                            this.amplitude = f9;
                        }
                        fragmentContextView.invalidate();
                    }
                    float f13 = this.animateToAmplitude;
                    float f14 = this.amplitude2;
                    if (f13 != f14) {
                        float f15 = this.animateAmplitudeDiff2;
                        float f16 = f14 + (((float) j) * f15);
                        this.amplitude2 = f16;
                        if (f15 > 0.0f) {
                            if (f16 > f13) {
                                this.amplitude2 = f13;
                            }
                        } else if (f16 < f13) {
                            this.amplitude2 = f13;
                        }
                        fragmentContextView.invalidate();
                    }
                    if (this.previousState != null) {
                        float f17 = this.progressToState + (((float) j) / 250.0f);
                        this.progressToState = f17;
                        if (f17 > 1.0f) {
                            this.progressToState = 1.0f;
                            this.previousState = null;
                        }
                        fragmentContextView.invalidate();
                    }
                }
                while (i2 < 2) {
                    if (i2 == 0 && this.previousState == null) {
                        i = i2;
                    } else {
                        if (i2 == 0) {
                            f6 = f7 - this.progressToState;
                            this.previousState.setToPaint(this.paint);
                        } else {
                            WeavingState weavingState2 = this.currentState;
                            if (weavingState2 == null) {
                                return;
                            }
                            f6 = this.previousState != null ? this.progressToState : 1.0f;
                            if (z) {
                                weavingState2.update((int) (f4 - f2), (int) (f3 - f), j, this.amplitude);
                            }
                            this.currentState.setToPaint(this.paint);
                        }
                        float f18 = f6;
                        LineBlobDrawable lineBlobDrawable = this.lineBlobDrawable;
                        lineBlobDrawable.minRadius = f8;
                        lineBlobDrawable.maxRadius = AndroidUtilities.dp(2.0f) + (AndroidUtilities.dp(2.0f) * this.amplitude);
                        this.lineBlobDrawable1.minRadius = AndroidUtilities.dp(f8);
                        this.lineBlobDrawable1.maxRadius = AndroidUtilities.dp(3.0f) + (AndroidUtilities.dp(9.0f) * this.amplitude);
                        this.lineBlobDrawable2.minRadius = AndroidUtilities.dp(f8);
                        LineBlobDrawable lineBlobDrawable2 = this.lineBlobDrawable2;
                        float f19 = this.amplitude;
                        lineBlobDrawable2.maxRadius = AndroidUtilities.dp(3.0f) + (AndroidUtilities.dp(9.0f) * f19);
                        if (i2 == 1 && z) {
                            this.lineBlobDrawable.update(f19, 0.3f);
                            this.lineBlobDrawable1.update(this.amplitude, 0.7f);
                            this.lineBlobDrawable2.update(this.amplitude, 0.7f);
                        }
                        this.paint.setAlpha((int) (76.0f * f18));
                        float dp = AndroidUtilities.dp(6.0f) * this.amplitude2;
                        float dp2 = AndroidUtilities.dp(6.0f) * this.amplitude2;
                        i = i2;
                        this.lineBlobDrawable1.draw(f, f2 - dp, f3, f4, canvas, this.paint, f2, f5);
                        this.lineBlobDrawable2.draw(f, f2 - dp2, f3, f4, canvas, this.paint, f2, f5);
                        if (i == 1 && z3) {
                            this.paint.setAlpha(255);
                        } else if (i == 1) {
                            this.paint.setAlpha((int) (255.0f * f18));
                        } else {
                            this.paint.setAlpha(255);
                        }
                        if (i == 1 && z3) {
                            this.path.reset();
                            this.path.addCircle(f3 - AndroidUtilities.dp(18.0f), f2 + ((f4 - f2) / 2.0f), (f3 - f) * 1.1f * f18, Path.Direction.CW);
                            canvas.save();
                            canvas.clipPath(this.path);
                            this.lineBlobDrawable.draw(f, f2, f3, f4, canvas, this.paint, f2, f5);
                            canvas.restore();
                        } else {
                            this.lineBlobDrawable.draw(f, f2, f3, f4, canvas, this.paint, f2, f5);
                        }
                    }
                    i2 = i + 1;
                    f8 = 0.0f;
                    f7 = 1.0f;
                }
            }
        }
        z = z2;
        j = j2;
        float f72 = 1.0f;
        float f82 = 0.0f;
        if (z) {
        }
        while (i2 < 2) {
        }
    }

    private void checkColors() {
        int i = 0;
        while (true) {
            WeavingState[] weavingStateArr = this.states;
            if (i < weavingStateArr.length) {
                weavingStateArr[i].checkColor();
                i++;
            } else {
                return;
            }
        }
    }

    private void setState(int i, boolean z) {
        WeavingState weavingState = this.currentState;
        if (weavingState == null || weavingState.currentState != i) {
            if (VoIPService.getSharedInstance() == null && this.currentState == null) {
                this.currentState = this.pausedState;
                return;
            }
            WeavingState weavingState2 = z ? this.currentState : null;
            this.previousState = weavingState2;
            this.currentState = this.states[i];
            if (weavingState2 != null) {
                this.progressToState = 0.0f;
            } else {
                this.progressToState = 1.0f;
            }
        }
    }

    public void setAmplitude(float f) {
        this.animateToAmplitude = f;
        float f2 = this.amplitude;
        this.animateAmplitudeDiff = (f - f2) / 250.0f;
        this.animateAmplitudeDiff2 = (f - f2) / 120.0f;
    }

    public void addParent(View view) {
        if (!this.parents.contains(view)) {
            this.parents.add(view);
        }
    }

    public void removeParent(View view) {
        this.parents.remove(view);
        if (this.parents.isEmpty()) {
            this.pausedState = this.currentState;
            this.currentState = null;
            this.previousState = null;
        }
    }

    public void updateState(boolean z) {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance != null) {
            int callState = sharedInstance.getCallState();
            if (!sharedInstance.isSwitchingStream() && (callState == 1 || callState == 2 || callState == 6 || callState == 5)) {
                setState(2, z);
                return;
            }
            ChatObject.Call call = sharedInstance.groupCall;
            if (call != null) {
                TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant = call.participants.get(sharedInstance.getSelfId());
                if ((tLRPC$TL_groupCallParticipant != null && !tLRPC$TL_groupCallParticipant.can_self_unmute && tLRPC$TL_groupCallParticipant.muted && !ChatObject.canManageCalls(sharedInstance.getChat())) || sharedInstance.groupCall.call.rtmp_stream) {
                    sharedInstance.setMicMute(true, false, false);
                    setState(3, z);
                    return;
                }
                setState(sharedInstance.isMicMute() ? 1 : 0, z);
                return;
            }
            setState(sharedInstance.isMicMute() ? 1 : 0, z);
        }
    }

    public int getState() {
        WeavingState weavingState = this.currentState;
        if (weavingState != null) {
            return weavingState.currentState;
        }
        return 0;
    }

    /* loaded from: classes3.dex */
    public static class WeavingState {
        int color1;
        int color2;
        private final int currentState;
        private float duration;
        public Shader shader;
        private float startX;
        private float startY;
        private float time;
        private float targetX = -1.0f;
        private float targetY = -1.0f;
        private final Matrix matrix = new Matrix();
        String greenKey1 = "voipgroup_topPanelGreen1";
        String greenKey2 = "voipgroup_topPanelGreen2";
        String blueKey1 = "voipgroup_topPanelBlue1";
        String blueKey2 = "voipgroup_topPanelBlue2";
        String mutedByAdmin = "voipgroup_mutedByAdminGradient";
        String mutedByAdmin2 = "voipgroup_mutedByAdminGradient2";
        String mutedByAdmin3 = "voipgroup_mutedByAdminGradient3";

        public WeavingState(int i) {
            this.currentState = i;
            createGradients();
        }

        private void createGradients() {
            int i = this.currentState;
            if (i == 0) {
                int color = Theme.getColor(this.greenKey1);
                this.color1 = color;
                int color2 = Theme.getColor(this.greenKey2);
                this.color2 = color2;
                this.shader = new RadialGradient(200.0f, 200.0f, 200.0f, new int[]{color, color2}, (float[]) null, Shader.TileMode.CLAMP);
            } else if (i == 1) {
                int color3 = Theme.getColor(this.blueKey1);
                this.color1 = color3;
                int color4 = Theme.getColor(this.blueKey2);
                this.color2 = color4;
                this.shader = new RadialGradient(200.0f, 200.0f, 200.0f, new int[]{color3, color4}, (float[]) null, Shader.TileMode.CLAMP);
            } else if (i != 3) {
            } else {
                int color5 = Theme.getColor(this.mutedByAdmin);
                this.color1 = color5;
                int color6 = Theme.getColor(this.mutedByAdmin2);
                this.color2 = color6;
                this.shader = new RadialGradient(200.0f, 200.0f, 200.0f, new int[]{color5, Theme.getColor(this.mutedByAdmin3), color6}, new float[]{0.0f, 0.6f, 1.0f}, Shader.TileMode.CLAMP);
            }
        }

        public void update(int i, int i2, long j, float f) {
            if (this.currentState == 2) {
                return;
            }
            float f2 = this.duration;
            if (f2 == 0.0f || this.time >= f2) {
                this.duration = Utilities.random.nextInt(700) + 500;
                this.time = 0.0f;
                if (this.targetX == -1.0f) {
                    int i3 = this.currentState;
                    if (i3 == 3) {
                        this.targetX = ((Utilities.random.nextInt(100) * 0.05f) / 100.0f) - 0.3f;
                        this.targetY = ((Utilities.random.nextInt(100) * 0.05f) / 100.0f) + 0.7f;
                    } else if (i3 == 0) {
                        this.targetX = ((Utilities.random.nextInt(100) * 0.2f) / 100.0f) - 0.3f;
                        this.targetY = ((Utilities.random.nextInt(100) * 0.3f) / 100.0f) + 0.7f;
                    } else {
                        this.targetX = ((Utilities.random.nextInt(100) / 100.0f) * 0.2f) + 1.1f;
                        this.targetY = (Utilities.random.nextInt(100) * 4.0f) / 100.0f;
                    }
                }
                this.startX = this.targetX;
                this.startY = this.targetY;
                int i4 = this.currentState;
                if (i4 == 3) {
                    this.targetX = ((Utilities.random.nextInt(100) * 0.05f) / 100.0f) - 0.3f;
                    this.targetY = ((Utilities.random.nextInt(100) * 0.05f) / 100.0f) + 0.7f;
                } else if (i4 == 0) {
                    this.targetX = ((Utilities.random.nextInt(100) * 0.2f) / 100.0f) - 0.3f;
                    this.targetY = ((Utilities.random.nextInt(100) * 0.3f) / 100.0f) + 0.7f;
                } else {
                    this.targetX = ((Utilities.random.nextInt(100) / 100.0f) * 0.2f) + 1.1f;
                    this.targetY = (Utilities.random.nextInt(100) * 4.0f) / 100.0f;
                }
            }
            float f3 = (float) j;
            float f4 = this.time + ((BlobDrawable.GRADIENT_SPEED_MIN + 0.5f) * f3) + (f3 * BlobDrawable.GRADIENT_SPEED_MAX * 2.0f * f);
            this.time = f4;
            float f5 = this.duration;
            if (f4 > f5) {
                this.time = f5;
            }
            float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.time / f5);
            float f6 = i2;
            float f7 = this.startX;
            float f8 = ((f7 + ((this.targetX - f7) * interpolation)) * f6) - 200.0f;
            float f9 = this.startY;
            float f10 = (i * (f9 + ((this.targetY - f9) * interpolation))) - 200.0f;
            float f11 = f6 / 400.0f;
            int i5 = this.currentState;
            float f12 = f11 * ((i5 == 0 || i5 == 3) ? 3.0f : 1.5f);
            this.matrix.reset();
            this.matrix.postTranslate(f8, f10);
            this.matrix.postScale(f12, f12, f8 + 200.0f, f10 + 200.0f);
            this.shader.setLocalMatrix(this.matrix);
        }

        public void checkColor() {
            int i = this.currentState;
            if (i == 0) {
                if (this.color1 == Theme.getColor(this.greenKey1) && this.color2 == Theme.getColor(this.greenKey2)) {
                    return;
                }
                createGradients();
            } else if (i == 1) {
                if (this.color1 == Theme.getColor(this.blueKey1) && this.color2 == Theme.getColor(this.blueKey2)) {
                    return;
                }
                createGradients();
            } else if (i != 3) {
            } else {
                if (this.color1 == Theme.getColor(this.mutedByAdmin) && this.color2 == Theme.getColor(this.mutedByAdmin2)) {
                    return;
                }
                createGradients();
            }
        }

        public void setToPaint(Paint paint) {
            int i = this.currentState;
            if (i == 0 || i == 1 || i == 3) {
                paint.setShader(this.shader);
                return;
            }
            paint.setShader(null);
            paint.setColor(Theme.getColor("voipgroup_topPanelGray"));
        }
    }
}
