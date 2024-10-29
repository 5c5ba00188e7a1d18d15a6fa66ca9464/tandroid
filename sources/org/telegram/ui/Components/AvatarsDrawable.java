package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.core.graphics.ColorUtils;
import java.util.Random;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.GroupCallUserCell;
import org.telegram.ui.Stories.StoriesGradientTools;

/* loaded from: classes3.dex */
public class AvatarsDrawable {
    private boolean attached;
    boolean centered;
    public int count;
    int currentStyle;
    public boolean drawStoriesCircle;
    public int height;
    private boolean isInCall;
    private int overrideSize;
    View parent;
    private boolean showSavedMessages;
    StoriesGradientTools storiesTools;
    private boolean transitionInProgress;
    ValueAnimator transitionProgressAnimator;
    boolean updateAfterTransition;
    Runnable updateDelegate;
    boolean wasDraw;
    public int width;
    DrawingState[] currentStates = new DrawingState[3];
    DrawingState[] animatingStates = new DrawingState[3];
    float transitionProgress = 1.0f;
    private Paint paint = new Paint(1);
    private Paint xRefP = new Paint(1);
    public int strokeWidth = AndroidUtilities.dp(1.67f);
    private float overrideSizeStepFactor = 0.8f;
    private float overrideAlpha = 1.0f;
    public long transitionDuration = 220;
    public Interpolator transitionInterpolator = CubicBezierInterpolator.DEFAULT;
    Random random = new Random();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class DrawingState {
        private int animationType;
        private AvatarDrawable avatarDrawable;
        private long id;
        private ImageReceiver imageReceiver;
        private long lastSpeakTime;
        private long lastUpdateTime;
        private int moveFromIndex;
        private TLObject object;
        TLRPC.TL_groupCallParticipant participant;
        private GroupCallUserCell.AvatarWavesDrawable wavesDrawable;

        private DrawingState() {
        }
    }

    public AvatarsDrawable(View view, boolean z) {
        this.parent = view;
        for (int i = 0; i < 3; i++) {
            this.currentStates[i] = new DrawingState();
            this.currentStates[i].imageReceiver = new ImageReceiver(view);
            this.currentStates[i].imageReceiver.setInvalidateAll(true);
            this.currentStates[i].imageReceiver.setRoundRadius(AndroidUtilities.dp(12.0f));
            this.currentStates[i].avatarDrawable = new AvatarDrawable();
            this.currentStates[i].avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
            this.animatingStates[i] = new DrawingState();
            this.animatingStates[i].imageReceiver = new ImageReceiver(view);
            this.animatingStates[i].imageReceiver.setInvalidateAll(true);
            this.animatingStates[i].imageReceiver.setRoundRadius(AndroidUtilities.dp(12.0f));
            this.animatingStates[i].avatarDrawable = new AvatarDrawable();
            this.animatingStates[i].avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        }
        this.isInCall = z;
        this.xRefP.setColor(0);
        this.xRefP.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidate() {
        View view = this.parent;
        if (view != null) {
            view.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$commitTransition$0(ValueAnimator valueAnimator) {
        this.transitionProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void swapStates() {
        for (int i = 0; i < 3; i++) {
            DrawingState[] drawingStateArr = this.currentStates;
            DrawingState drawingState = drawingStateArr[i];
            DrawingState[] drawingStateArr2 = this.animatingStates;
            drawingStateArr[i] = drawingStateArr2[i];
            drawingStateArr2[i] = drawingState;
        }
    }

    public void animateFromState(AvatarsDrawable avatarsDrawable, int i, boolean z) {
        if (avatarsDrawable == null) {
            return;
        }
        ValueAnimator valueAnimator = avatarsDrawable.transitionProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            if (this.transitionInProgress) {
                this.transitionInProgress = false;
                swapStates();
            }
        }
        TLObject[] tLObjectArr = new TLObject[3];
        for (int i2 = 0; i2 < 3; i2++) {
            tLObjectArr[i2] = this.currentStates[i2].object;
            setObject(i2, i, avatarsDrawable.currentStates[i2].object);
        }
        commitTransition(false);
        for (int i3 = 0; i3 < 3; i3++) {
            setObject(i3, i, tLObjectArr[i3]);
        }
        this.wasDraw = true;
        commitTransition(true, z);
    }

    public void commitTransition(boolean z) {
        commitTransition(z, true);
    }

    public void commitTransition(boolean z, boolean z2) {
        if (!this.wasDraw || !z) {
            this.transitionProgress = 1.0f;
            swapStates();
            return;
        }
        DrawingState[] drawingStateArr = new DrawingState[3];
        boolean z3 = false;
        for (int i = 0; i < 3; i++) {
            DrawingState[] drawingStateArr2 = this.currentStates;
            drawingStateArr[i] = drawingStateArr2[i];
            if (drawingStateArr2[i].id != this.animatingStates[i].id) {
                z3 = true;
            } else {
                this.currentStates[i].lastSpeakTime = this.animatingStates[i].lastSpeakTime;
            }
        }
        if (!z3) {
            this.transitionProgress = 1.0f;
            return;
        }
        for (int i2 = 0; i2 < 3; i2++) {
            int i3 = 0;
            while (true) {
                if (i3 >= 3) {
                    this.animatingStates[i2].animationType = 0;
                    break;
                }
                if (this.currentStates[i3].id == this.animatingStates[i2].id) {
                    drawingStateArr[i3] = null;
                    if (i2 == i3) {
                        this.animatingStates[i2].animationType = -1;
                        GroupCallUserCell.AvatarWavesDrawable avatarWavesDrawable = this.animatingStates[i2].wavesDrawable;
                        this.animatingStates[i2].wavesDrawable = this.currentStates[i2].wavesDrawable;
                        this.currentStates[i2].wavesDrawable = avatarWavesDrawable;
                    } else {
                        this.animatingStates[i2].animationType = 2;
                        this.animatingStates[i2].moveFromIndex = i3;
                    }
                } else {
                    i3++;
                }
            }
        }
        for (int i4 = 0; i4 < 3; i4++) {
            DrawingState drawingState = drawingStateArr[i4];
            if (drawingState != null) {
                drawingState.animationType = 1;
            }
        }
        ValueAnimator valueAnimator = this.transitionProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.removeAllListeners();
            this.transitionProgressAnimator.cancel();
            if (this.transitionInProgress) {
                swapStates();
                this.transitionInProgress = false;
            }
        }
        this.transitionProgress = 0.0f;
        if (z2) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.transitionProgressAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.AvatarsDrawable$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    AvatarsDrawable.this.lambda$commitTransition$0(valueAnimator2);
                }
            });
            this.transitionProgressAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AvatarsDrawable.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    AvatarsDrawable avatarsDrawable = AvatarsDrawable.this;
                    if (avatarsDrawable.transitionProgressAnimator != null) {
                        avatarsDrawable.transitionProgress = 1.0f;
                        avatarsDrawable.swapStates();
                        AvatarsDrawable avatarsDrawable2 = AvatarsDrawable.this;
                        if (avatarsDrawable2.updateAfterTransition) {
                            avatarsDrawable2.updateAfterTransition = false;
                            Runnable runnable = avatarsDrawable2.updateDelegate;
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                        AvatarsDrawable.this.invalidate();
                    }
                    AvatarsDrawable.this.transitionProgressAnimator = null;
                }
            });
            this.transitionProgressAnimator.setDuration(this.transitionDuration);
            this.transitionProgressAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.transitionProgressAnimator.start();
        } else {
            this.transitionInProgress = true;
        }
        invalidate();
    }

    public int getSize() {
        int i = this.overrideSize;
        if (i != 0) {
            return i;
        }
        int i2 = this.currentStyle;
        return AndroidUtilities.dp((i2 == 4 || i2 == 10) ? 32.0f : 24.0f);
    }

    public void onAttachedToWindow() {
        if (this.attached) {
            return;
        }
        this.attached = true;
        for (int i = 0; i < 3; i++) {
            this.currentStates[i].imageReceiver.onAttachedToWindow();
            this.animatingStates[i].imageReceiver.onAttachedToWindow();
        }
    }

    public void onDetachedFromWindow() {
        if (this.attached) {
            this.attached = false;
            this.wasDraw = false;
            for (int i = 0; i < 3; i++) {
                this.currentStates[i].imageReceiver.onDetachedFromWindow();
                this.animatingStates[i].imageReceiver.onDetachedFromWindow();
            }
            if (this.currentStyle == 3) {
                Theme.getFragmentContextViewWavesDrawable().setAmplitude(0.0f);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:111:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x029f  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x02a2 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x02bd  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0348  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x0407  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x066c  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0686  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0689 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:211:0x0681  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x041b  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x05ab  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x05d8  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x05f4  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0625  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0649  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x0696  */
    /* JADX WARN: Removed duplicated region for block: B:323:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:325:0x0122  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x009f  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x012b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        float f;
        int i;
        int i2;
        int i3;
        int i4;
        boolean z;
        int i5;
        int i6;
        int i7;
        float f2;
        boolean z2;
        float f3;
        int i8;
        int i9;
        DrawingState drawingState;
        float avatarScale;
        DrawingState drawingState2;
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant;
        DrawingState drawingState3;
        GroupCallUserCell.AvatarWavesDrawable avatarWavesDrawable;
        GroupCallUserCell.AvatarWavesDrawable avatarWavesDrawable2;
        int i10;
        GroupCallUserCell.AvatarWavesDrawable avatarWavesDrawable3;
        double d;
        float f4;
        int i11;
        float f5;
        boolean z3;
        int i12;
        int dp;
        int i13 = 1;
        this.wasDraw = true;
        int i14 = this.currentStyle;
        int i15 = 10;
        int i16 = 4;
        boolean z4 = i14 == 4 || i14 == 10;
        int size = getSize();
        int i17 = 11;
        if (this.currentStyle == 11) {
            f = 12.0f;
        } else {
            int i18 = this.overrideSize;
            if (i18 != 0) {
                i = (int) (i18 * this.overrideSizeStepFactor);
                int i19 = i;
                int i20 = 0;
                for (i2 = 0; i2 < 3; i2++) {
                    if (this.currentStates[i2].id != 0) {
                        i20++;
                    }
                }
                int i21 = this.currentStyle;
                int dp2 = (i21 != 0 || i21 == 10 || i21 == 11) ? 0 : AndroidUtilities.dp(10.0f);
                int dp3 = this.centered ? dp2 : ((this.width - (i20 * i19)) - AndroidUtilities.dp(z4 ? 8.0f : 4.0f)) / 2;
                boolean z5 = VoIPService.getSharedInstance() == null && VoIPService.getSharedInstance().isMicMute();
                i3 = this.currentStyle;
                if (i3 != 4) {
                    this.paint.setColor(Theme.getColor(Theme.key_inappPlayerBackground));
                } else if (i3 != 3) {
                    this.paint.setColor(Theme.getColor(z5 ? Theme.key_returnToCallMutedBackground : Theme.key_returnToCallBackground));
                }
                int i22 = 0;
                for (i4 = 0; i4 < 3; i4++) {
                    if (this.animatingStates[i4].id != 0) {
                        i22++;
                    }
                }
                int i23 = this.currentStyle;
                z = i23 != 0 || i23 == 1 || i23 == 3 || i23 == 4 || i23 == 5 || i23 == 10 || i23 == 11;
                if (z) {
                    i5 = 2;
                } else {
                    float dp4 = i23 == 10 ? AndroidUtilities.dp(16.0f) : 0.0f;
                    if (this.drawStoriesCircle) {
                        dp4 += AndroidUtilities.dp(20.0f);
                    }
                    float f6 = -dp4;
                    i5 = 2;
                    canvas.saveLayerAlpha(f6, f6, this.width + dp4, this.height + dp4, NotificationCenter.playerDidStartPlaying, 31);
                }
                float f7 = 2.0f;
                float f8 = 1.0f;
                if (this.drawStoriesCircle) {
                    int i24 = 2;
                    while (i24 >= 0) {
                        int i25 = 0;
                        while (i25 < i5) {
                            if (i25 != 0 || this.transitionProgress != 1.0f) {
                                DrawingState[] drawingStateArr = i25 == 0 ? this.animatingStates : this.currentStates;
                                if (i25 != i13 || this.transitionProgress == 1.0f || drawingStateArr[i24].animationType == i13) {
                                    ImageReceiver imageReceiver = drawingStateArr[i24].imageReceiver;
                                    if (imageReceiver.hasImageSet()) {
                                        if (i25 == 0) {
                                            i11 = (this.centered ? ((this.width - (i22 * i19)) - AndroidUtilities.dp(z4 ? 8.0f : 4.0f)) / i5 : dp2) + (i19 * i24);
                                        } else {
                                            i11 = dp3 + (i19 * i24);
                                        }
                                        imageReceiver.setImageX(i11);
                                        int i26 = this.currentStyle;
                                        imageReceiver.setImageY((i26 == 0 || i26 == i15 || i26 == i17) ? (this.height - size) / f7 : AndroidUtilities.dp(i26 == i16 ? 8.0f : 6.0f));
                                        if (this.transitionProgress != 1.0f) {
                                            if (drawingStateArr[i24].animationType == i13) {
                                                canvas.save();
                                                float f9 = 1.0f - this.transitionProgress;
                                                canvas.scale(f9, f9, imageReceiver.getCenterX(), imageReceiver.getCenterY());
                                                f5 = 1.0f - this.transitionProgress;
                                            } else if (drawingStateArr[i24].animationType == 0) {
                                                canvas.save();
                                                float f10 = this.transitionProgress;
                                                canvas.scale(f10, f10, imageReceiver.getCenterX(), imageReceiver.getCenterY());
                                                f5 = this.transitionProgress;
                                            } else {
                                                if (drawingStateArr[i24].animationType == i5) {
                                                    dp = (this.centered ? ((this.width - (i22 * i19)) - AndroidUtilities.dp(z4 ? 8.0f : 4.0f)) / i5 : dp2) + (i19 * i24);
                                                    i12 = drawingStateArr[i24].moveFromIndex * i19;
                                                } else if (drawingStateArr[i24].animationType == -1 && this.centered) {
                                                    i12 = i19 * i24;
                                                    dp = (((this.width - (i22 * i19)) - AndroidUtilities.dp(z4 ? 8.0f : 4.0f)) / i5) + i12;
                                                }
                                                float f11 = this.transitionProgress;
                                                imageReceiver.setImageX((int) ((dp * f11) + ((dp3 + i12) * (1.0f - f11))));
                                            }
                                            z3 = true;
                                            float f12 = f5 * this.overrideAlpha;
                                            float size2 = (getSize() / f7) + AndroidUtilities.dp(4.0f);
                                            if (this.storiesTools == null) {
                                                this.storiesTools = new StoriesGradientTools();
                                            }
                                            this.storiesTools.setBounds(0.0f, 0.0f, this.parent.getMeasuredHeight(), AndroidUtilities.dp(40.0f));
                                            this.storiesTools.paint.setAlpha((int) (f12 * 255.0f));
                                            canvas.drawCircle(imageReceiver.getCenterX(), imageReceiver.getCenterY(), size2, this.storiesTools.paint);
                                            if (!z3) {
                                                canvas.restore();
                                            }
                                        }
                                        f5 = 1.0f;
                                        z3 = false;
                                        float f122 = f5 * this.overrideAlpha;
                                        float size22 = (getSize() / f7) + AndroidUtilities.dp(4.0f);
                                        if (this.storiesTools == null) {
                                        }
                                        this.storiesTools.setBounds(0.0f, 0.0f, this.parent.getMeasuredHeight(), AndroidUtilities.dp(40.0f));
                                        this.storiesTools.paint.setAlpha((int) (f122 * 255.0f));
                                        canvas.drawCircle(imageReceiver.getCenterX(), imageReceiver.getCenterY(), size22, this.storiesTools.paint);
                                        if (!z3) {
                                        }
                                    }
                                }
                            }
                            i25++;
                            f7 = 2.0f;
                            i13 = 1;
                            i15 = 10;
                            i16 = 4;
                            i17 = 11;
                        }
                        i24--;
                        f7 = 2.0f;
                        i13 = 1;
                        i15 = 10;
                        i16 = 4;
                        i17 = 11;
                    }
                }
                i6 = 2;
                while (i6 >= 0) {
                    int i27 = 0;
                    while (i27 < i5) {
                        if (i27 != 0 || this.transitionProgress != f8) {
                            DrawingState[] drawingStateArr2 = i27 == 0 ? this.animatingStates : this.currentStates;
                            if (i27 != 1 || this.transitionProgress == f8 || drawingStateArr2[i6].animationType == 1) {
                                ImageReceiver imageReceiver2 = drawingStateArr2[i6].imageReceiver;
                                if (imageReceiver2.hasImageSet()) {
                                    if (i27 == 0) {
                                        i7 = (this.centered ? ((this.width - (i22 * i19)) - AndroidUtilities.dp(z4 ? 8.0f : 4.0f)) / i5 : dp2) + (i19 * i6);
                                    } else {
                                        i7 = dp3 + (i19 * i6);
                                    }
                                    imageReceiver2.setImageX(i7);
                                    int i28 = this.currentStyle;
                                    if (i28 != 0 && i28 != 10) {
                                        if (i28 != 11) {
                                            f2 = AndroidUtilities.dp(i28 == 4 ? 8.0f : 6.0f);
                                            imageReceiver2.setImageY(f2);
                                            if (this.transitionProgress != f8) {
                                                if (drawingStateArr2[i6].animationType == 1) {
                                                    canvas.save();
                                                    float f13 = f8 - this.transitionProgress;
                                                    canvas.scale(f13, f13, imageReceiver2.getCenterX(), imageReceiver2.getCenterY());
                                                    f4 = f8 - this.transitionProgress;
                                                } else if (drawingStateArr2[i6].animationType == 0) {
                                                    canvas.save();
                                                    float f14 = this.transitionProgress;
                                                    canvas.scale(f14, f14, imageReceiver2.getCenterX(), imageReceiver2.getCenterY());
                                                    f4 = this.transitionProgress;
                                                } else if (drawingStateArr2[i6].animationType == i5) {
                                                    int dp5 = this.centered ? ((this.width - (i22 * i19)) - AndroidUtilities.dp(z4 ? 8.0f : 4.0f)) / i5 : dp2;
                                                    int i29 = dp3 + (drawingStateArr2[i6].moveFromIndex * i19);
                                                    float f15 = this.transitionProgress;
                                                    imageReceiver2.setImageX((int) (((dp5 + (i19 * i6)) * f15) + (i29 * (f8 - f15))));
                                                } else {
                                                    if (drawingStateArr2[i6].animationType == -1 && this.centered) {
                                                        int dp6 = ((this.width - (i22 * i19)) - AndroidUtilities.dp(z4 ? 8.0f : 4.0f)) / i5;
                                                        int i30 = i19 * i6;
                                                        float f16 = this.transitionProgress;
                                                        imageReceiver2.setImageX((int) (((dp6 + i30) * f16) + ((dp3 + i30) * (f8 - f16))));
                                                    }
                                                    z2 = false;
                                                    f3 = 1.0f;
                                                    float f17 = f3 * this.overrideAlpha;
                                                    if (i6 == drawingStateArr2.length - 1 || this.drawStoriesCircle) {
                                                        i8 = this.currentStyle;
                                                        if (i8 != 1 || i8 == 3) {
                                                            i9 = 5;
                                                        } else {
                                                            i9 = 5;
                                                            if (i8 != 5) {
                                                                if (i8 == 4 || i8 == 10) {
                                                                    canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), AndroidUtilities.dp(17.0f), this.xRefP);
                                                                    if (drawingStateArr2[i6].wavesDrawable == null) {
                                                                        drawingStateArr2[i6].wavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(17.0f), AndroidUtilities.dp(21.0f));
                                                                    }
                                                                    if (this.currentStyle == 10) {
                                                                        avatarWavesDrawable2 = drawingStateArr2[i6].wavesDrawable;
                                                                        i10 = Theme.key_voipgroup_speakingText;
                                                                    } else {
                                                                        avatarWavesDrawable2 = drawingStateArr2[i6].wavesDrawable;
                                                                        i10 = Theme.key_voipgroup_listeningText;
                                                                    }
                                                                    avatarWavesDrawable2.setColor(ColorUtils.setAlphaComponent(Theme.getColor(i10), (int) (f17 * 76.5f)));
                                                                    long currentTimeMillis = System.currentTimeMillis();
                                                                    if (currentTimeMillis - drawingStateArr2[i6].lastUpdateTime > 100) {
                                                                        drawingStateArr2[i6].lastUpdateTime = currentTimeMillis;
                                                                        if (this.currentStyle == 10) {
                                                                            DrawingState drawingState4 = drawingStateArr2[i6];
                                                                            TLRPC.TL_groupCallParticipant tL_groupCallParticipant2 = drawingState4.participant;
                                                                            if (tL_groupCallParticipant2 == null || tL_groupCallParticipant2.amplitude <= 0.0f) {
                                                                                drawingState4.wavesDrawable.setShowWaves(false, this.parent);
                                                                            } else {
                                                                                drawingState4.wavesDrawable.setShowWaves(true, this.parent);
                                                                                DrawingState drawingState5 = drawingStateArr2[i6];
                                                                                float f18 = drawingState5.participant.amplitude * 15.0f;
                                                                                avatarWavesDrawable3 = drawingState5.wavesDrawable;
                                                                                d = f18;
                                                                            }
                                                                        } else if (ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime() - drawingStateArr2[i6].lastSpeakTime <= 5) {
                                                                            drawingStateArr2[i6].wavesDrawable.setShowWaves(true, this.parent);
                                                                            avatarWavesDrawable3 = drawingStateArr2[i6].wavesDrawable;
                                                                            d = this.random.nextInt() % 100;
                                                                        } else {
                                                                            drawingStateArr2[i6].wavesDrawable.setShowWaves(false, this.parent);
                                                                            avatarWavesDrawable3 = drawingStateArr2[i6].wavesDrawable;
                                                                            d = 0.0d;
                                                                        }
                                                                        avatarWavesDrawable3.setAmplitude(d);
                                                                    }
                                                                    drawingStateArr2[i6].wavesDrawable.update();
                                                                    drawingStateArr2[i6].wavesDrawable.draw(canvas, imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), this.parent);
                                                                    drawingState = drawingStateArr2[i6];
                                                                    avatarScale = drawingState.wavesDrawable.getAvatarScale();
                                                                    imageReceiver2.setAlpha(f17);
                                                                    f8 = 1.0f;
                                                                    if (avatarScale == 1.0f) {
                                                                        canvas.save();
                                                                        canvas.scale(avatarScale, avatarScale, imageReceiver2.getCenterX(), imageReceiver2.getCenterY());
                                                                        imageReceiver2.draw(canvas);
                                                                        canvas.restore();
                                                                    } else {
                                                                        imageReceiver2.draw(canvas);
                                                                    }
                                                                    if (!z2) {
                                                                        canvas.restore();
                                                                    }
                                                                    i27++;
                                                                    i5 = 2;
                                                                } else {
                                                                    float size3 = (getSize() / 2.0f) + this.strokeWidth;
                                                                    if (z) {
                                                                        canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), size3, this.xRefP);
                                                                    } else {
                                                                        int alpha = this.paint.getAlpha();
                                                                        if (f17 != f8) {
                                                                            this.paint.setAlpha((int) (alpha * f17));
                                                                        }
                                                                        canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), size3, this.paint);
                                                                        if (f17 != f8) {
                                                                            this.paint.setAlpha(alpha);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), AndroidUtilities.dp(13.0f), this.xRefP);
                                                        if (drawingStateArr2[i6].wavesDrawable == null) {
                                                            if (this.currentStyle == i9) {
                                                                drawingState3 = drawingStateArr2[i6];
                                                                avatarWavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(16.0f));
                                                            } else {
                                                                drawingState3 = drawingStateArr2[i6];
                                                                avatarWavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(17.0f), AndroidUtilities.dp(21.0f));
                                                            }
                                                            drawingState3.wavesDrawable = avatarWavesDrawable;
                                                        }
                                                        if (this.currentStyle == i9) {
                                                            drawingStateArr2[i6].wavesDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_voipgroup_speakingText), (int) (f17 * 76.5f)));
                                                        }
                                                        drawingState2 = drawingStateArr2[i6];
                                                        tL_groupCallParticipant = drawingState2.participant;
                                                        if (tL_groupCallParticipant != null || tL_groupCallParticipant.amplitude <= 0.0f) {
                                                            drawingState2.wavesDrawable.setShowWaves(false, this.parent);
                                                        } else {
                                                            drawingState2.wavesDrawable.setShowWaves(true, this.parent);
                                                            drawingStateArr2[i6].wavesDrawable.setAmplitude(r5.participant.amplitude * 15.0f);
                                                        }
                                                        if (this.currentStyle == i9 && SystemClock.uptimeMillis() - drawingStateArr2[i6].participant.lastSpeakTime > 500) {
                                                            this.updateDelegate.run();
                                                        }
                                                        drawingStateArr2[i6].wavesDrawable.update();
                                                        if (this.currentStyle == i9) {
                                                            drawingStateArr2[i6].wavesDrawable.draw(canvas, imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), this.parent);
                                                            invalidate();
                                                        }
                                                        drawingState = drawingStateArr2[i6];
                                                        avatarScale = drawingState.wavesDrawable.getAvatarScale();
                                                        imageReceiver2.setAlpha(f17);
                                                        f8 = 1.0f;
                                                        if (avatarScale == 1.0f) {
                                                        }
                                                        if (!z2) {
                                                        }
                                                        i27++;
                                                        i5 = 2;
                                                    }
                                                    avatarScale = 1.0f;
                                                    imageReceiver2.setAlpha(f17);
                                                    f8 = 1.0f;
                                                    if (avatarScale == 1.0f) {
                                                    }
                                                    if (!z2) {
                                                    }
                                                    i27++;
                                                    i5 = 2;
                                                }
                                                f3 = f4;
                                                z2 = true;
                                                float f172 = f3 * this.overrideAlpha;
                                                if (i6 == drawingStateArr2.length - 1) {
                                                }
                                                i8 = this.currentStyle;
                                                if (i8 != 1) {
                                                }
                                                i9 = 5;
                                                canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), AndroidUtilities.dp(13.0f), this.xRefP);
                                                if (drawingStateArr2[i6].wavesDrawable == null) {
                                                }
                                                if (this.currentStyle == i9) {
                                                }
                                                drawingState2 = drawingStateArr2[i6];
                                                tL_groupCallParticipant = drawingState2.participant;
                                                if (tL_groupCallParticipant != null) {
                                                }
                                                drawingState2.wavesDrawable.setShowWaves(false, this.parent);
                                                if (this.currentStyle == i9) {
                                                    this.updateDelegate.run();
                                                }
                                                drawingStateArr2[i6].wavesDrawable.update();
                                                if (this.currentStyle == i9) {
                                                }
                                                drawingState = drawingStateArr2[i6];
                                                avatarScale = drawingState.wavesDrawable.getAvatarScale();
                                                imageReceiver2.setAlpha(f172);
                                                f8 = 1.0f;
                                                if (avatarScale == 1.0f) {
                                                }
                                                if (!z2) {
                                                }
                                                i27++;
                                                i5 = 2;
                                            }
                                            z2 = false;
                                            f3 = 1.0f;
                                            float f1722 = f3 * this.overrideAlpha;
                                            if (i6 == drawingStateArr2.length - 1) {
                                            }
                                            i8 = this.currentStyle;
                                            if (i8 != 1) {
                                            }
                                            i9 = 5;
                                            canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), AndroidUtilities.dp(13.0f), this.xRefP);
                                            if (drawingStateArr2[i6].wavesDrawable == null) {
                                            }
                                            if (this.currentStyle == i9) {
                                            }
                                            drawingState2 = drawingStateArr2[i6];
                                            tL_groupCallParticipant = drawingState2.participant;
                                            if (tL_groupCallParticipant != null) {
                                            }
                                            drawingState2.wavesDrawable.setShowWaves(false, this.parent);
                                            if (this.currentStyle == i9) {
                                            }
                                            drawingStateArr2[i6].wavesDrawable.update();
                                            if (this.currentStyle == i9) {
                                            }
                                            drawingState = drawingStateArr2[i6];
                                            avatarScale = drawingState.wavesDrawable.getAvatarScale();
                                            imageReceiver2.setAlpha(f1722);
                                            f8 = 1.0f;
                                            if (avatarScale == 1.0f) {
                                            }
                                            if (!z2) {
                                            }
                                            i27++;
                                            i5 = 2;
                                        }
                                    }
                                    f2 = (this.height - size) / 2.0f;
                                    imageReceiver2.setImageY(f2);
                                    if (this.transitionProgress != f8) {
                                    }
                                    z2 = false;
                                    f3 = 1.0f;
                                    float f17222 = f3 * this.overrideAlpha;
                                    if (i6 == drawingStateArr2.length - 1) {
                                    }
                                    i8 = this.currentStyle;
                                    if (i8 != 1) {
                                    }
                                    i9 = 5;
                                    canvas.drawCircle(imageReceiver2.getCenterX(), imageReceiver2.getCenterY(), AndroidUtilities.dp(13.0f), this.xRefP);
                                    if (drawingStateArr2[i6].wavesDrawable == null) {
                                    }
                                    if (this.currentStyle == i9) {
                                    }
                                    drawingState2 = drawingStateArr2[i6];
                                    tL_groupCallParticipant = drawingState2.participant;
                                    if (tL_groupCallParticipant != null) {
                                    }
                                    drawingState2.wavesDrawable.setShowWaves(false, this.parent);
                                    if (this.currentStyle == i9) {
                                    }
                                    drawingStateArr2[i6].wavesDrawable.update();
                                    if (this.currentStyle == i9) {
                                    }
                                    drawingState = drawingStateArr2[i6];
                                    avatarScale = drawingState.wavesDrawable.getAvatarScale();
                                    imageReceiver2.setAlpha(f17222);
                                    f8 = 1.0f;
                                    if (avatarScale == 1.0f) {
                                    }
                                    if (!z2) {
                                    }
                                    i27++;
                                    i5 = 2;
                                }
                            }
                        }
                        i27++;
                        i5 = 2;
                    }
                    i6--;
                    i5 = 2;
                }
                if (z) {
                    return;
                }
                canvas.restore();
                return;
            }
            f = z4 ? 24.0f : 20.0f;
        }
        i = AndroidUtilities.dp(f);
        int i192 = i;
        int i202 = 0;
        while (i2 < 3) {
        }
        int i212 = this.currentStyle;
        if (i212 != 0) {
        }
        if (this.centered) {
        }
        if (VoIPService.getSharedInstance() == null) {
        }
        i3 = this.currentStyle;
        if (i3 != 4) {
        }
        int i222 = 0;
        while (i4 < 3) {
        }
        int i232 = this.currentStyle;
        if (i232 != 0) {
        }
        if (z) {
        }
        float f72 = 2.0f;
        float f82 = 1.0f;
        if (this.drawStoriesCircle) {
        }
        i6 = 2;
        while (i6 >= 0) {
        }
        if (z) {
        }
    }

    public void reset() {
        for (int i = 0; i < this.animatingStates.length; i++) {
            setObject(0, 0, null);
        }
    }

    public void setAlpha(float f) {
        this.overrideAlpha = f;
    }

    public void setAvatarsTextSize(int i) {
        for (int i2 = 0; i2 < 3; i2++) {
            DrawingState drawingState = this.currentStates[i2];
            if (drawingState != null && drawingState.avatarDrawable != null) {
                this.currentStates[i2].avatarDrawable.setTextSize(i);
            }
            DrawingState drawingState2 = this.animatingStates[i2];
            if (drawingState2 != null && drawingState2.avatarDrawable != null) {
                this.animatingStates[i2].avatarDrawable.setTextSize(i);
            }
        }
    }

    public void setCentered(boolean z) {
        this.centered = z;
    }

    public void setCount(int i) {
        this.count = i;
        View view = this.parent;
        if (view != null) {
            view.requestLayout();
        }
    }

    public void setDelegate(Runnable runnable) {
        this.updateDelegate = runnable;
    }

    public void setObject(int i, int i2, TLObject tLObject) {
        TLRPC.User user;
        TLRPC.Chat chat;
        ImageReceiver imageReceiver;
        ImageLocation forPhoto;
        String str;
        ImageLocation forPhoto2;
        StringBuilder sb;
        DrawingState drawingState;
        long j;
        this.animatingStates[i].id = 0L;
        DrawingState drawingState2 = this.animatingStates[i];
        drawingState2.participant = null;
        if (tLObject == null) {
            drawingState2.imageReceiver.setImageBitmap((Drawable) null);
        } else {
            drawingState2.lastSpeakTime = -1L;
            this.animatingStates[i].object = tLObject;
            if (tLObject instanceof TLRPC.TL_groupCallParticipant) {
                TLRPC.TL_groupCallParticipant tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) tLObject;
                this.animatingStates[i].participant = tL_groupCallParticipant;
                long peerId = MessageObject.getPeerId(tL_groupCallParticipant.peer);
                if (DialogObject.isUserDialog(peerId)) {
                    user = MessagesController.getInstance(i2).getUser(Long.valueOf(peerId));
                    this.animatingStates[i].avatarDrawable.setInfo(i2, user);
                    chat = null;
                } else {
                    TLRPC.Chat chat2 = MessagesController.getInstance(i2).getChat(Long.valueOf(-peerId));
                    this.animatingStates[i].avatarDrawable.setInfo(i2, chat2);
                    chat = chat2;
                    user = null;
                }
                if (this.currentStyle != 4) {
                    drawingState = this.animatingStates[i];
                } else if (peerId == AccountInstance.getInstance(i2).getUserConfig().getClientUserId()) {
                    this.animatingStates[i].lastSpeakTime = 0L;
                    this.animatingStates[i].id = peerId;
                } else if (this.isInCall) {
                    drawingState = this.animatingStates[i];
                    j = tL_groupCallParticipant.lastActiveDate;
                    drawingState.lastSpeakTime = j;
                    this.animatingStates[i].id = peerId;
                } else {
                    drawingState = this.animatingStates[i];
                }
                j = tL_groupCallParticipant.active_date;
                drawingState.lastSpeakTime = j;
                this.animatingStates[i].id = peerId;
            } else if (tLObject instanceof TLRPC.User) {
                user = (TLRPC.User) tLObject;
                if (user.self && this.showSavedMessages) {
                    this.animatingStates[i].avatarDrawable.setAvatarType(1);
                    this.animatingStates[i].avatarDrawable.setScaleSize(0.6f);
                } else {
                    this.animatingStates[i].avatarDrawable.setAvatarType(0);
                    this.animatingStates[i].avatarDrawable.setScaleSize(1.0f);
                    this.animatingStates[i].avatarDrawable.setInfo(i2, user);
                }
                this.animatingStates[i].id = user.id;
                chat = null;
            } else if (tLObject instanceof TLRPC.Chat) {
                chat = (TLRPC.Chat) tLObject;
                this.animatingStates[i].avatarDrawable.setAvatarType(0);
                this.animatingStates[i].avatarDrawable.setScaleSize(1.0f);
                this.animatingStates[i].avatarDrawable.setInfo(i2, chat);
                this.animatingStates[i].id = -chat.id;
                user = null;
            } else {
                user = null;
                chat = null;
            }
            int size = getSize();
            if (tLObject instanceof TL_stories.StoryItem) {
                TL_stories.StoryItem storyItem = (TL_stories.StoryItem) tLObject;
                this.animatingStates[i].id = storyItem.id;
                TLRPC.MessageMedia messageMedia = storyItem.media;
                TLRPC.Document document = messageMedia.document;
                if (document != null) {
                    TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50, true, null, false);
                    TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(storyItem.media.document.thumbs, 50, true, closestPhotoSizeWithSize, true);
                    imageReceiver = this.animatingStates[i].imageReceiver;
                    forPhoto = ImageLocation.getForDocument(closestPhotoSizeWithSize2, storyItem.media.document);
                    str = size + "_" + size;
                    forPhoto2 = ImageLocation.getForDocument(closestPhotoSizeWithSize, storyItem.media.document);
                    sb = new StringBuilder();
                } else {
                    TLRPC.Photo photo = messageMedia.photo;
                    if (photo != null) {
                        TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 50, true, null, false);
                        TLRPC.PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(storyItem.media.photo.sizes, 50, true, closestPhotoSizeWithSize3, true);
                        imageReceiver = this.animatingStates[i].imageReceiver;
                        forPhoto = ImageLocation.getForPhoto(closestPhotoSizeWithSize4, storyItem.media.photo);
                        str = size + "_" + size;
                        forPhoto2 = ImageLocation.getForPhoto(closestPhotoSizeWithSize3, storyItem.media.photo);
                        sb = new StringBuilder();
                    }
                }
                sb.append(size);
                sb.append("_");
                sb.append(size);
                imageReceiver.setImage(forPhoto, str, forPhoto2, sb.toString(), 0L, null, storyItem, 0);
            } else if (user == null) {
                this.animatingStates[i].imageReceiver.setForUserOrChat(chat, this.animatingStates[i].avatarDrawable);
            } else if (user.self && this.showSavedMessages) {
                this.animatingStates[i].imageReceiver.setImageBitmap(this.animatingStates[i].avatarDrawable);
            } else {
                this.animatingStates[i].imageReceiver.setForUserOrChat(user, this.animatingStates[i].avatarDrawable);
            }
            this.animatingStates[i].imageReceiver.setRoundRadius(size / 2);
            float f = size;
            this.animatingStates[i].imageReceiver.setImageCoords(0.0f, 0.0f, f, f);
        }
        invalidate();
    }

    public void setShowSavedMessages(boolean z) {
        this.showSavedMessages = z;
    }

    public void setSize(int i) {
        this.overrideSize = i;
    }

    public void setStepFactor(float f) {
        this.overrideSizeStepFactor = f;
    }

    public void setStyle(int i) {
        this.currentStyle = i;
        invalidate();
    }

    public void setTransitionProgress(float f) {
        if (!this.transitionInProgress || this.transitionProgress == f) {
            return;
        }
        this.transitionProgress = f;
        if (f == 1.0f) {
            swapStates();
            this.transitionInProgress = false;
        }
    }

    public void updateAfterTransitionEnd() {
        this.updateAfterTransition = true;
    }
}
