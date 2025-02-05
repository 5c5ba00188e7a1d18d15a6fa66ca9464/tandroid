package org.telegram.ui.Components.voip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.voip.VoIpSwitchLayout;

/* loaded from: classes3.dex */
public class VoIpSwitchLayout extends FrameLayout {
    public int animationDelay;
    private final VoIPBackgroundProvider backgroundProvider;
    private final TextView currentTextView;
    private final TextView newTextView;
    private Type type;
    private VoIpButtonView voIpButtonView;

    static /* synthetic */ class 4 {
        static final /* synthetic */ int[] $SwitchMap$org$telegram$ui$Components$voip$VoIpSwitchLayout$Type;

        static {
            int[] iArr = new int[Type.values().length];
            $SwitchMap$org$telegram$ui$Components$voip$VoIpSwitchLayout$Type = iArr;
            try {
                iArr[Type.MICRO.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$telegram$ui$Components$voip$VoIpSwitchLayout$Type[Type.CAMERA.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$org$telegram$ui$Components$voip$VoIpSwitchLayout$Type[Type.VIDEO.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$org$telegram$ui$Components$voip$VoIpSwitchLayout$Type[Type.BLUETOOTH.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$org$telegram$ui$Components$voip$VoIpSwitchLayout$Type[Type.SPEAKER.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public enum Type {
        MICRO,
        CAMERA,
        VIDEO,
        BLUETOOTH,
        SPEAKER
    }

    public static class VoIpButtonView extends View {
        private ValueAnimator animator;
        private final VoIPBackgroundProvider backgroundProvider;
        private final Path clipPath;
        private final Paint darkPaint;
        private boolean isSelectedState;
        private final Paint maskPaint;
        private final int maxRadius;
        private OnBtnClickedListener onBtnClickedListener;
        private float pressedScale;
        private ValueAnimator pressedScaleAnimator;
        private RLottieDrawable selectedIcon;
        private int selectedRadius;
        private RLottieDrawable singleIcon;
        private int singleIconBackgroundAlphaPercent;
        private float startX;
        private float startY;
        private RLottieDrawable unSelectedIcon;
        private int unselectedRadius;
        private final Paint whiteCirclePaint;

        public interface OnBtnClickedListener {
            void onClicked(View view);
        }

        public VoIpButtonView(Context context, VoIPBackgroundProvider voIPBackgroundProvider) {
            super(context);
            Paint paint = new Paint(1);
            this.maskPaint = paint;
            Paint paint2 = new Paint(1);
            this.whiteCirclePaint = paint2;
            Paint paint3 = new Paint(1);
            this.darkPaint = paint3;
            this.clipPath = new Path();
            int dp = AndroidUtilities.dp(26.0f);
            this.maxRadius = dp;
            this.unselectedRadius = dp;
            this.selectedRadius = 0;
            this.isSelectedState = false;
            this.singleIconBackgroundAlphaPercent = 0;
            this.pressedScale = 1.0f;
            this.backgroundProvider = voIPBackgroundProvider;
            voIPBackgroundProvider.attach(this);
            setLayerType(1, null);
            paint2.setColor(-1);
            paint.setColor(-16777216);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            paint3.setColor(-16777216);
            paint3.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.SRC_ATOP));
            paint3.setAlpha(35);
        }

        private boolean isAnimating() {
            int i = this.unselectedRadius;
            int i2 = this.maxRadius;
            return ((i == i2 && this.selectedRadius == 0) || (this.selectedRadius == i2 && i == 0)) ? false : true;
        }

        private boolean isClick(float f, float f2, float f3, float f4) {
            return Math.abs(f - f2) <= ((float) AndroidUtilities.dp(48.0f)) && Math.abs(f3 - f4) <= ((float) AndroidUtilities.dp(48.0f));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setPressedBtn$3(ValueAnimator valueAnimator) {
            this.pressedScale = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setSelectedState$0(ValueAnimator valueAnimator) {
            this.singleIconBackgroundAlphaPercent = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setSelectedState$1(ValueAnimator valueAnimator) {
            this.selectedRadius = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setSelectedState$2(ValueAnimator valueAnimator) {
            this.unselectedRadius = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            invalidate();
        }

        private void setPressedBtn(boolean z) {
            ValueAnimator valueAnimator = this.pressedScaleAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.pressedScale, z ? 0.8f : 1.0f);
            this.pressedScaleAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout$VoIpButtonView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    VoIpSwitchLayout.VoIpButtonView.this.lambda$setPressedBtn$3(valueAnimator2);
                }
            });
            this.pressedScaleAnimator.setDuration(150L);
            this.pressedScaleAnimator.start();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            canvas.save();
            float f = this.pressedScale;
            canvas.scale(f, f, getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f);
            float width = getWidth() / 2.0f;
            float height = getHeight() / 2.0f;
            this.backgroundProvider.setLightTranslation(getX() + ((View) getParent()).getX(), getY() + ((View) ((View) getParent()).getParent()).getY());
            if (this.singleIcon != null) {
                if (this.singleIconBackgroundAlphaPercent <= 20) {
                    canvas.drawCircle(width, height, this.maxRadius, this.backgroundProvider.getLightPaint());
                    if (this.backgroundProvider.isReveal()) {
                        canvas.drawCircle(width, height, this.maxRadius, this.backgroundProvider.getRevealPaint());
                    }
                    this.singleIcon.draw(canvas);
                    return;
                }
                this.darkPaint.setAlpha((int) ((r2 * 35) / 100.0f));
                this.whiteCirclePaint.setAlpha((int) ((this.singleIconBackgroundAlphaPercent * NotificationCenter.liveLocationsChanged) / 100.0f));
                canvas.drawCircle(width, height, this.maxRadius, this.whiteCirclePaint);
                this.singleIcon.draw(canvas, this.maskPaint);
                this.singleIcon.draw(canvas, this.darkPaint);
                return;
            }
            if (this.selectedIcon == null || this.unSelectedIcon == null) {
                return;
            }
            int i = this.unselectedRadius;
            int i2 = this.maxRadius;
            boolean z = false;
            boolean z2 = i == i2 && this.selectedRadius == 0;
            int i3 = this.selectedRadius;
            if (i3 == i2 && i == 0) {
                z = true;
            }
            if (i3 == i2 && i > 0 && i != i2) {
                canvas.drawCircle(width, height, i3, this.whiteCirclePaint);
                canvas.drawCircle(width, height, this.unselectedRadius, this.maskPaint);
                this.selectedIcon.setAlpha(NotificationCenter.liveLocationsChanged);
                this.selectedIcon.draw(canvas, this.maskPaint);
                this.selectedIcon.setAlpha(35);
                this.selectedIcon.draw(canvas);
                this.clipPath.reset();
                this.clipPath.addCircle(width, height, this.unselectedRadius, Path.Direction.CW);
                canvas.clipPath(this.clipPath);
                canvas.drawCircle(width, height, this.unselectedRadius, this.maskPaint);
            }
            if (z2 || this.unselectedRadius > 0) {
                canvas.drawCircle(width, height, this.unselectedRadius, this.backgroundProvider.getLightPaint());
                if (this.backgroundProvider.isReveal()) {
                    canvas.drawCircle(width, height, this.unselectedRadius, this.backgroundProvider.getRevealPaint());
                }
                this.unSelectedIcon.draw(canvas);
            }
            if (z || (this.selectedRadius > 0 && this.unselectedRadius == this.maxRadius)) {
                this.clipPath.reset();
                this.clipPath.addCircle(width, height, this.selectedRadius, Path.Direction.CW);
                canvas.clipPath(this.clipPath);
                canvas.drawCircle(width, height, this.selectedRadius, this.whiteCirclePaint);
                this.selectedIcon.setAlpha(NotificationCenter.liveLocationsChanged);
                this.selectedIcon.draw(canvas, this.maskPaint);
                this.selectedIcon.setAlpha(35);
                this.selectedIcon.draw(canvas);
            }
            canvas.restore();
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            OnBtnClickedListener onBtnClickedListener;
            int action = motionEvent.getAction();
            if (action == 0) {
                setPressedBtn(true);
                this.startX = motionEvent.getX();
                this.startY = motionEvent.getY();
            } else if (action == 1) {
                setPressedBtn(false);
                if (isClick(this.startX, motionEvent.getX(), this.startY, motionEvent.getY()) && !isAnimating() && (onBtnClickedListener = this.onBtnClickedListener) != null) {
                    onBtnClickedListener.onClicked(this);
                }
            } else if (action == 3) {
                setPressedBtn(false);
            }
            return true;
        }

        public void setOnBtnClickedListener(OnBtnClickedListener onBtnClickedListener) {
            this.onBtnClickedListener = onBtnClickedListener;
        }

        public void setSelectedState(boolean z, boolean z2, Type type) {
            RLottieDrawable rLottieDrawable;
            ValueAnimator valueAnimator = this.animator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.animator.removeAllUpdateListeners();
                this.animator.cancel();
                z2 = false;
            }
            if (z2) {
                if (this.singleIcon != null) {
                    ValueAnimator valueAnimator2 = this.animator;
                    if (valueAnimator2 != null) {
                        valueAnimator2.removeAllUpdateListeners();
                        this.animator.cancel();
                    }
                    ValueAnimator ofInt = z ? ValueAnimator.ofInt(20, 100) : ValueAnimator.ofInt(100, 20);
                    this.animator = ofInt;
                    ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout$VoIpButtonView$$ExternalSyntheticLambda1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                            VoIpSwitchLayout.VoIpButtonView.this.lambda$setSelectedState$0(valueAnimator3);
                        }
                    });
                    this.animator.setDuration(200L);
                    this.animator.start();
                    if (type == Type.CAMERA) {
                        this.singleIcon.setCurrentFrame(0, false);
                        rLottieDrawable = this.singleIcon;
                        rLottieDrawable.start();
                    }
                } else {
                    ValueAnimator valueAnimator3 = this.animator;
                    if (valueAnimator3 != null) {
                        valueAnimator3.removeAllUpdateListeners();
                        this.animator.cancel();
                    }
                    ValueAnimator ofInt2 = ValueAnimator.ofInt(0, this.maxRadius);
                    this.animator = ofInt2;
                    int i = this.maxRadius;
                    if (z) {
                        this.unselectedRadius = i;
                        ofInt2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout$VoIpButtonView$$ExternalSyntheticLambda2
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator4) {
                                VoIpSwitchLayout.VoIpButtonView.this.lambda$setSelectedState$1(valueAnimator4);
                            }
                        });
                        this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout.VoIpButtonView.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                VoIpButtonView.this.unselectedRadius = 0;
                                VoIpButtonView.this.invalidate();
                            }
                        });
                        this.animator.setDuration(200L);
                        this.animator.start();
                        this.selectedIcon.setCurrentFrame(0, false);
                        rLottieDrawable = this.selectedIcon;
                        rLottieDrawable.start();
                    } else {
                        this.selectedRadius = i;
                        ofInt2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout$VoIpButtonView$$ExternalSyntheticLambda3
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator4) {
                                VoIpSwitchLayout.VoIpButtonView.this.lambda$setSelectedState$2(valueAnimator4);
                            }
                        });
                        this.animator.setDuration(200L);
                        this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout.VoIpButtonView.2
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                VoIpButtonView.this.selectedRadius = 0;
                                VoIpButtonView.this.invalidate();
                            }
                        });
                        this.animator.start();
                    }
                }
            } else if (z) {
                this.selectedRadius = this.maxRadius;
                this.unselectedRadius = 0;
                this.singleIconBackgroundAlphaPercent = 100;
                if (type == Type.VIDEO || type == Type.MICRO) {
                    this.selectedIcon.setCurrentFrame(r7.getFramesCount() - 1, false);
                }
            } else {
                this.selectedRadius = 0;
                this.unselectedRadius = this.maxRadius;
                this.singleIconBackgroundAlphaPercent = 20;
            }
            this.isSelectedState = z;
            invalidate();
        }
    }

    public VoIpSwitchLayout(Context context, VoIPBackgroundProvider voIPBackgroundProvider) {
        super(context);
        this.backgroundProvider = voIPBackgroundProvider;
        setWillNotDraw(true);
        VoIpButtonView voIpButtonView = new VoIpButtonView(context, voIPBackgroundProvider);
        this.voIpButtonView = voIpButtonView;
        addView(voIpButtonView, LayoutHelper.createFrame(53.5f, 53.5f, 1));
        TextView textView = new TextView(context);
        this.currentTextView = textView;
        textView.setGravity(1);
        textView.setTextSize(1, 11.0f);
        textView.setTextColor(-1);
        textView.setImportantForAccessibility(2);
        addView(textView, LayoutHelper.createFrame(-1, -2.0f, 0, 0.0f, 58.0f, 0.0f, 2.0f));
        TextView textView2 = new TextView(context);
        this.newTextView = textView2;
        textView2.setGravity(1);
        textView2.setTextSize(1, 11.0f);
        textView2.setTextColor(-1);
        textView2.setImportantForAccessibility(2);
        addView(textView2, LayoutHelper.createFrame(-1, -2.0f, 0, 0.0f, 58.0f, 0.0f, 2.0f));
        textView.setVisibility(8);
        textView2.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: attachBtToSpeaker, reason: merged with bridge method [inline-methods] */
    public void lambda$setType$2(int i) {
        VoIpButtonView voIpButtonView = this.voIpButtonView;
        int i2 = R.raw.bt_to_speaker;
        voIpButtonView.unSelectedIcon = new RLottieDrawable(i2, "" + i2, i, i, true, null);
        this.voIpButtonView.selectedIcon = new RLottieDrawable(i2, "" + i2, i, i, true, null);
        this.voIpButtonView.selectedIcon.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
    }

    private void attachNewButton(int i, int i2, boolean z, Type type) {
        RLottieDrawable rLottieDrawable;
        VoIpButtonView voIpButtonView = new VoIpButtonView(getContext(), this.backgroundProvider);
        if (i == R.raw.camera_flip2) {
            rLottieDrawable = new RLottieDrawable(i, "" + i, i2, i2, true, null);
            voIpButtonView.singleIcon = rLottieDrawable;
            voIpButtonView.singleIcon.setMasterParent(voIpButtonView);
        } else {
            rLottieDrawable = new RLottieDrawable(i, "" + i, i2, i2, true, null);
            voIpButtonView.unSelectedIcon = rLottieDrawable;
            voIpButtonView.selectedIcon = new RLottieDrawable(i, "" + i, i2, i2, true, null);
            voIpButtonView.selectedIcon.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
        }
        voIpButtonView.setSelectedState(z, false, type);
        voIpButtonView.setAlpha(0.0f);
        voIpButtonView.setOnBtnClickedListener(this.voIpButtonView.onBtnClickedListener);
        addView(voIpButtonView, LayoutHelper.createFrame(53.5f, 53.5f, 1));
        final VoIpButtonView voIpButtonView2 = this.voIpButtonView;
        this.voIpButtonView = voIpButtonView;
        voIpButtonView.animate().alpha(1.0f).setDuration(250L).start();
        voIpButtonView2.animate().alpha(0.0f).setDuration(250L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                VoIpSwitchLayout.this.removeView(voIpButtonView2);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: attachSpeakerToBt, reason: merged with bridge method [inline-methods] */
    public void lambda$setType$0(int i) {
        VoIpButtonView voIpButtonView = this.voIpButtonView;
        int i2 = R.raw.speaker_to_bt;
        voIpButtonView.unSelectedIcon = new RLottieDrawable(i2, "" + i2, i, i, true, null);
        this.voIpButtonView.selectedIcon = new RLottieDrawable(i2, "" + i2, i, i, true, null);
        this.voIpButtonView.selectedIcon.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setType$1(final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                VoIpSwitchLayout.this.lambda$setType$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setType$3(final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                VoIpSwitchLayout.this.lambda$setType$2(i);
            }
        });
    }

    private void setText(Type type, boolean z) {
        int i;
        final String string;
        int i2 = 4.$SwitchMap$org$telegram$ui$Components$voip$VoIpSwitchLayout$Type[type.ordinal()];
        if (i2 == 1) {
            i = z ? R.string.VoipUnmute : R.string.VoipMute;
        } else if (i2 == 2) {
            i = R.string.VoipFlip;
        } else if (i2 == 3) {
            i = z ? R.string.VoipStartVideo : R.string.VoipStopVideo;
        } else if (i2 == 4) {
            i = R.string.VoipAudioRoutingBluetooth;
        } else {
            if (i2 != 5) {
                string = "";
                setContentDescription(string);
                if (this.currentTextView.getVisibility() != 8 && this.newTextView.getVisibility() == 8) {
                    this.currentTextView.setVisibility(0);
                    this.currentTextView.setText(string);
                    this.newTextView.setText(string);
                    return;
                } else {
                    if (this.newTextView.getText().equals(string) || !this.currentTextView.getText().equals(string)) {
                        this.currentTextView.animate().alpha(0.0f).translationY(-AndroidUtilities.dp(4.0f)).setDuration(140L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                VoIpSwitchLayout.this.currentTextView.setText(string);
                                VoIpSwitchLayout.this.currentTextView.setTranslationY(0.0f);
                                VoIpSwitchLayout.this.currentTextView.setAlpha(1.0f);
                            }
                        }).start();
                        this.newTextView.setText(string);
                        this.newTextView.setVisibility(0);
                        this.newTextView.setAlpha(0.0f);
                        this.newTextView.setTranslationY(AndroidUtilities.dp(5.0f));
                        this.newTextView.animate().alpha(1.0f).translationY(0.0f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout.2
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                VoIpSwitchLayout.this.newTextView.setVisibility(8);
                            }
                        }).start();
                    }
                    return;
                }
            }
            i = R.string.VoipSpeaker;
        }
        string = LocaleController.getString(i);
        setContentDescription(string);
        if (this.currentTextView.getVisibility() != 8) {
        }
        if (this.newTextView.getText().equals(string)) {
        }
        this.currentTextView.animate().alpha(0.0f).translationY(-AndroidUtilities.dp(4.0f)).setDuration(140L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                VoIpSwitchLayout.this.currentTextView.setText(string);
                VoIpSwitchLayout.this.currentTextView.setTranslationY(0.0f);
                VoIpSwitchLayout.this.currentTextView.setAlpha(1.0f);
            }
        }).start();
        this.newTextView.setText(string);
        this.newTextView.setVisibility(0);
        this.newTextView.setAlpha(0.0f);
        this.newTextView.setTranslationY(AndroidUtilities.dp(5.0f));
        this.newTextView.animate().alpha(1.0f).translationY(0.0f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                VoIpSwitchLayout.this.newTextView.setVisibility(8);
            }
        }).start();
    }

    public void setOnBtnClickedListener(VoIpButtonView.OnBtnClickedListener onBtnClickedListener) {
        this.voIpButtonView.setOnBtnClickedListener(onBtnClickedListener);
    }

    public void setType(Type type, boolean z) {
        setType(type, z, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x01c0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setType(Type type, boolean z, boolean z2) {
        RLottieDrawable rLottieDrawable;
        PorterDuffColorFilter porterDuffColorFilter;
        RLottieDrawable rLottieDrawable2;
        boolean z3;
        int i;
        RLottieDrawable rLottieDrawable3;
        Runnable runnable;
        if (this.type == type && z == this.voIpButtonView.isSelectedState) {
            if (getVisibility() != 0) {
                setVisibility(0);
                return;
            }
            return;
        }
        if (getVisibility() != 0) {
            setVisibility(0);
        }
        final int dp = AndroidUtilities.dp(53.5f);
        int i2 = 4.$SwitchMap$org$telegram$ui$Components$voip$VoIpSwitchLayout$Type[type.ordinal()];
        if (i2 != 1) {
            if (i2 == 2) {
                Type type2 = this.type;
                if (type2 == Type.SPEAKER || type2 == Type.BLUETOOTH) {
                    attachNewButton(R.raw.camera_flip2, dp, z, type);
                    z3 = true;
                } else if (type2 != Type.CAMERA) {
                    VoIpButtonView voIpButtonView = this.voIpButtonView;
                    int i3 = R.raw.camera_flip2;
                    voIpButtonView.singleIcon = new RLottieDrawable(i3, "" + i3, dp, dp, true, null);
                    rLottieDrawable2 = this.voIpButtonView.singleIcon;
                    rLottieDrawable2.setMasterParent(this.voIpButtonView);
                }
            } else if (i2 != 3) {
                if (i2 == 4) {
                    Type type3 = this.type;
                    if (type3 == Type.SPEAKER) {
                        z3 = z == this.voIpButtonView.isSelectedState;
                        VoIpButtonView voIpButtonView2 = this.voIpButtonView;
                        rLottieDrawable3 = z ? voIpButtonView2.selectedIcon : voIpButtonView2.unSelectedIcon;
                        rLottieDrawable3.setMasterParent(this.voIpButtonView);
                        runnable = new Runnable() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                VoIpSwitchLayout.this.lambda$setType$3(dp);
                            }
                        };
                        rLottieDrawable3.setOnAnimationEndListener(runnable);
                        rLottieDrawable3.start();
                    } else if (type3 == Type.CAMERA) {
                        i = R.raw.bt_to_speaker;
                        attachNewButton(i, dp, z, type);
                        z3 = true;
                    } else if (type3 != Type.BLUETOOTH) {
                        lambda$setType$2(dp);
                    }
                } else if (i2 == 5) {
                    Type type4 = this.type;
                    if (type4 == Type.BLUETOOTH) {
                        z3 = z == this.voIpButtonView.isSelectedState;
                        VoIpButtonView voIpButtonView3 = this.voIpButtonView;
                        rLottieDrawable3 = z ? voIpButtonView3.selectedIcon : voIpButtonView3.unSelectedIcon;
                        rLottieDrawable3.setMasterParent(this.voIpButtonView);
                        runnable = new Runnable() { // from class: org.telegram.ui.Components.voip.VoIpSwitchLayout$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                VoIpSwitchLayout.this.lambda$setType$1(dp);
                            }
                        };
                        rLottieDrawable3.setOnAnimationEndListener(runnable);
                        rLottieDrawable3.start();
                    } else if (type4 == Type.CAMERA) {
                        i = R.raw.speaker_to_bt;
                        attachNewButton(i, dp, z, type);
                        z3 = true;
                    } else if (type4 != Type.SPEAKER) {
                        lambda$setType$0(dp);
                    }
                }
            } else if (this.type != Type.VIDEO) {
                VoIpButtonView voIpButtonView4 = this.voIpButtonView;
                int i4 = R.raw.video_stop;
                voIpButtonView4.unSelectedIcon = new RLottieDrawable(i4, "" + i4, dp, dp, true, null);
                this.voIpButtonView.selectedIcon = new RLottieDrawable(i4, "" + i4, dp, dp, true, null);
                rLottieDrawable = this.voIpButtonView.selectedIcon;
                porterDuffColorFilter = new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY);
                rLottieDrawable.setColorFilter(porterDuffColorFilter);
                rLottieDrawable2 = this.voIpButtonView.selectedIcon;
                rLottieDrawable2.setMasterParent(this.voIpButtonView);
            }
            if (!z3) {
                this.voIpButtonView.setSelectedState(z, (this.type == null || z2) ? false : true, type);
            }
            setText(type, z);
            this.type = type;
        }
        if (this.type != Type.MICRO) {
            VoIpButtonView voIpButtonView5 = this.voIpButtonView;
            int i5 = R.raw.call_mute;
            voIpButtonView5.unSelectedIcon = new RLottieDrawable(i5, "" + i5, dp, dp, true, null);
            this.voIpButtonView.selectedIcon = new RLottieDrawable(i5, "" + i5, dp, dp, true, null);
            rLottieDrawable = this.voIpButtonView.selectedIcon;
            porterDuffColorFilter = new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY);
            rLottieDrawable.setColorFilter(porterDuffColorFilter);
            rLottieDrawable2 = this.voIpButtonView.selectedIcon;
            rLottieDrawable2.setMasterParent(this.voIpButtonView);
        }
        z3 = false;
        if (!z3) {
        }
        setText(type, z);
        this.type = type;
    }
}
