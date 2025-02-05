package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.SystemClock;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.Utilities;

/* loaded from: classes3.dex */
public class VideoPlayerSeekBar {
    private static Paint paint;
    private static Paint strokePaint;
    private static int thumbWidth;
    private static Path tmpPath;
    private static float[] tmpRadii;
    private float animateFromBufferedProgress;
    private boolean animateResetBuffering;
    private AnimatedFloat animateThumbLoopBackProgress;
    private int backgroundColor;
    private int backgroundSelectedColor;
    private float bufferedProgress;
    private int cacheColor;
    private int circleColor;
    private float currentRadius;
    private SeekBarDelegate delegate;
    private int height;
    private int horizontalPadding;
    private CharSequence lastCaption;
    private long lastTimestampUpdate;
    private long lastTimestampsAppearingUpdate;
    private long lastUpdateTime;
    private long lastVideoDuration;
    private float loopBackWasThumbX;
    private View parentView;
    private float progress;
    private int progressColor;
    private boolean selected;
    private int smallLineColor;
    private int timestampChangeDirection;
    private StaticLayout[] timestampLabel;
    private TextPaint timestampLabelPaint;
    private ArrayList timestamps;
    private float transitionProgress;
    private int width;
    private int thumbX = 0;
    private float animatedThumbX = 0.0f;
    private int draggingThumbX = 0;
    private int thumbDX = 0;
    private boolean pressed = false;
    private boolean pressedDelayed = false;
    private RectF rect = new RectF();
    private float bufferedAnimationValue = 1.0f;
    private int lineHeight = AndroidUtilities.dp(4.0f);
    private int smallLineHeight = AndroidUtilities.dp(2.0f);
    private int fromThumbX = 0;
    private float animateThumbProgress = 1.0f;
    private float timestampsAppearing = 0.0f;
    private final float TIMESTAMP_GAP = 1.0f;
    private int currentTimestamp = -1;
    private int lastTimestamp = -1;
    private float timestampChangeT = 1.0f;
    private float lastWidth = -1.0f;

    public interface SeekBarDelegate {
        void onSeekBarContinuousDrag(float f);

        void onSeekBarDrag(float f);
    }

    public VideoPlayerSeekBar(View view) {
        if (paint == null) {
            paint = new Paint(1);
            Paint paint2 = new Paint(1);
            strokePaint = paint2;
            paint2.setStyle(Paint.Style.STROKE);
            strokePaint.setColor(-16777216);
            strokePaint.setStrokeWidth(1.0f);
        }
        this.parentView = view;
        thumbWidth = AndroidUtilities.dp(24.0f);
        this.currentRadius = AndroidUtilities.dp(6.0f);
        this.animateThumbLoopBackProgress = new AnimatedFloat(0.0f, view, 0L, 300L, CubicBezierInterpolator.EASE_OUT_QUINT);
    }

    /* JADX WARN: Code restructure failed: missing block: B:87:0x0104, code lost:
    
        r12 = ((java.lang.Float) ((android.util.Pair) r0.timestamps.get(r15)).first).floatValue();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void drawProgressBar(Canvas canvas, RectF rectF, Paint paint2) {
        int i;
        float floatValue;
        VideoPlayerSeekBar videoPlayerSeekBar = this;
        int i2 = 1;
        float dp = AndroidUtilities.dp(AndroidUtilities.lerp(2, 1, videoPlayerSeekBar.transitionProgress));
        ArrayList arrayList = videoPlayerSeekBar.timestamps;
        if (arrayList == null || arrayList.isEmpty()) {
            canvas.drawRoundRect(rectF, dp, dp, paint2);
            return;
        }
        float f = rectF.bottom;
        float lerp = videoPlayerSeekBar.horizontalPadding + AndroidUtilities.lerp(thumbWidth / 2.0f, 0.0f, videoPlayerSeekBar.transitionProgress);
        float lerp2 = videoPlayerSeekBar.horizontalPadding + AndroidUtilities.lerp(videoPlayerSeekBar.width - (thumbWidth / 2.0f), videoPlayerSeekBar.parentView.getWidth() - (videoPlayerSeekBar.horizontalPadding * 2.0f), videoPlayerSeekBar.transitionProgress);
        AndroidUtilities.rectTmp.set(rectF);
        float dp2 = AndroidUtilities.dp(videoPlayerSeekBar.timestampsAppearing * 1.0f) / 2.0f;
        if (tmpPath == null) {
            tmpPath = new Path();
        }
        tmpPath.reset();
        float dp3 = AndroidUtilities.dp(4.0f) / (lerp2 - lerp);
        int i3 = 0;
        while (true) {
            i = -1;
            if (i3 >= videoPlayerSeekBar.timestamps.size()) {
                i3 = -1;
                break;
            } else if (((Float) ((Pair) videoPlayerSeekBar.timestamps.get(i3)).first).floatValue() >= dp3) {
                break;
            } else {
                i3++;
            }
        }
        if (i3 < 0) {
            i3 = 0;
        }
        int size = videoPlayerSeekBar.timestamps.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            if (1.0f - ((Float) ((Pair) videoPlayerSeekBar.timestamps.get(size)).first).floatValue() >= dp3) {
                i = size + 1;
                break;
            }
            size--;
        }
        if (i < 0) {
            i = videoPlayerSeekBar.timestamps.size();
        }
        int i4 = i;
        int i5 = i3;
        while (i5 <= i4) {
            float floatValue2 = i5 == i3 ? 0.0f : ((Float) ((Pair) videoPlayerSeekBar.timestamps.get(i5 - 1)).first).floatValue();
            if (i5 == i4) {
                floatValue = 1.0f;
                while (i5 != i4 && i5 != 0 && i5 < videoPlayerSeekBar.timestamps.size() - i2 && ((Float) ((Pair) videoPlayerSeekBar.timestamps.get(i5)).first).floatValue() - floatValue2 <= dp3) {
                    i5++;
                }
                RectF rectF2 = AndroidUtilities.rectTmp;
                rectF2.left = AndroidUtilities.lerp(lerp, lerp2, floatValue2) + (i5 > 0 ? dp2 : 0.0f);
                float lerp3 = AndroidUtilities.lerp(lerp, lerp2, floatValue) - (i5 < i4 ? dp2 : 0.0f);
                rectF2.right = lerp3;
                float f2 = rectF.right;
                boolean z = lerp3 > f2;
                if (z) {
                    rectF2.right = f2;
                }
                float f3 = rectF2.right;
                float f4 = rectF.left;
                if (f3 >= f4) {
                    if (rectF2.left < f4) {
                        rectF2.left = f4;
                    }
                    if (tmpRadii == null) {
                        tmpRadii = new float[8];
                    }
                    if (i5 == i3 || (z && rectF2.left >= rectF.left)) {
                        float[] fArr = tmpRadii;
                        fArr[7] = dp;
                        fArr[6] = dp;
                        fArr[1] = dp;
                        fArr[0] = dp;
                        float f5 = 0.7f * dp * videoPlayerSeekBar.timestampsAppearing;
                        fArr[5] = f5;
                        fArr[4] = f5;
                        fArr[3] = f5;
                        fArr[2] = f5;
                    } else if (i5 >= i4) {
                        float[] fArr2 = tmpRadii;
                        float f6 = 0.7f * dp * videoPlayerSeekBar.timestampsAppearing;
                        fArr2[7] = f6;
                        fArr2[6] = f6;
                        fArr2[1] = f6;
                        fArr2[0] = f6;
                        fArr2[5] = dp;
                        fArr2[4] = dp;
                        fArr2[3] = dp;
                        fArr2[2] = dp;
                    } else {
                        float[] fArr3 = tmpRadii;
                        float f7 = 0.7f * dp * videoPlayerSeekBar.timestampsAppearing;
                        fArr3[5] = f7;
                        fArr3[4] = f7;
                        fArr3[3] = f7;
                        fArr3[2] = f7;
                        fArr3[7] = f7;
                        fArr3[6] = f7;
                        fArr3[1] = f7;
                        fArr3[0] = f7;
                    }
                    tmpPath.addRoundRect(rectF2, tmpRadii, Path.Direction.CW);
                    if (z) {
                        break;
                    }
                }
                i5++;
                videoPlayerSeekBar = this;
                i2 = 1;
            }
            floatValue = ((Float) ((Pair) videoPlayerSeekBar.timestamps.get(i5)).first).floatValue();
        }
        canvas.drawPath(tmpPath, paint2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x0127, code lost:
    
        if (r3 > r10) goto L59;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void drawTimestampLabel(Canvas canvas) {
        float f;
        float f2;
        ArrayList arrayList = this.timestamps;
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        float f3 = ((this.pressed || this.pressedDelayed) ? this.draggingThumbX : this.animatedThumbX) / (this.width - thumbWidth);
        int size = this.timestamps.size() - 1;
        while (true) {
            if (size < 0) {
                size = -1;
                break;
            } else if (((Float) ((Pair) this.timestamps.get(size)).first).floatValue() - 0.001f <= f3) {
                break;
            } else {
                size--;
            }
        }
        if (this.timestampLabel == null) {
            this.timestampLabel = new StaticLayout[2];
        }
        float lerp = this.horizontalPadding + AndroidUtilities.lerp(thumbWidth / 2.0f, 0.0f, this.transitionProgress);
        float lerp2 = this.horizontalPadding + AndroidUtilities.lerp(this.width - (thumbWidth / 2.0f), this.parentView.getWidth() - (this.horizontalPadding * 2.0f), this.transitionProgress);
        float f4 = this.horizontalPadding + (this.width - (thumbWidth / 2.0f));
        float abs = Math.abs(lerp - f4) - AndroidUtilities.dp(16.0f);
        float f5 = this.lastWidth;
        if (f5 > 0.0f && Math.abs(f5 - abs) > 0.01f) {
            StaticLayout[] staticLayoutArr = this.timestampLabel;
            StaticLayout staticLayout = staticLayoutArr[0];
            if (staticLayout != null) {
                staticLayoutArr[0] = makeStaticLayout(staticLayout.getText(), (int) abs);
            }
            StaticLayout[] staticLayoutArr2 = this.timestampLabel;
            StaticLayout staticLayout2 = staticLayoutArr2[1];
            if (staticLayout2 != null) {
                staticLayoutArr2[1] = makeStaticLayout(staticLayout2.getText(), (int) abs);
            }
        }
        this.lastWidth = abs;
        if (size != this.currentTimestamp) {
            StaticLayout[] staticLayoutArr3 = this.timestampLabel;
            staticLayoutArr3[1] = staticLayoutArr3[0];
            if (this.pressed) {
                AndroidUtilities.vibrateCursor(this.parentView);
            }
            if (size < 0 || size >= this.timestamps.size()) {
                this.timestampLabel[0] = null;
            } else {
                CharSequence charSequence = (CharSequence) ((Pair) this.timestamps.get(size)).second;
                if (charSequence == null) {
                    this.timestampLabel[0] = null;
                } else {
                    this.timestampLabel[0] = makeStaticLayout(charSequence, (int) abs);
                }
            }
            this.timestampChangeT = 0.0f;
            if (size != -1) {
                int i = this.currentTimestamp;
                if (i != -1) {
                    if (size >= i) {
                    }
                }
                this.timestampChangeDirection = 1;
                this.lastTimestamp = this.currentTimestamp;
                this.currentTimestamp = size;
            }
            this.timestampChangeDirection = -1;
            this.lastTimestamp = this.currentTimestamp;
            this.currentTimestamp = size;
        }
        if (this.timestampChangeT < 1.0f) {
            this.timestampChangeT = Math.min(this.timestampChangeT + (Math.min(17L, Math.abs(SystemClock.elapsedRealtime() - this.lastTimestampUpdate)) / (this.timestamps.size() > 8 ? 160.0f : 220.0f)), 1.0f);
            this.parentView.invalidate();
            this.lastTimestampUpdate = SystemClock.elapsedRealtime();
        }
        if (this.timestampsAppearing < 1.0f) {
            this.timestampsAppearing = Math.min(this.timestampsAppearing + (Math.min(17L, Math.abs(SystemClock.elapsedRealtime() - this.lastTimestampUpdate)) / 200.0f), 1.0f);
            this.parentView.invalidate();
            this.lastTimestampsAppearingUpdate = SystemClock.elapsedRealtime();
        }
        float interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(this.timestampChangeT);
        canvas.save();
        int i2 = this.height;
        canvas.translate(lerp + ((lerp2 - f4) * this.transitionProgress), AndroidUtilities.lerp((this.lineHeight + i2) / 2.0f, i2 - AndroidUtilities.dp(3.0f), this.transitionProgress) + AndroidUtilities.dp(12.0f));
        if (this.timestampLabel[1] != null) {
            canvas.save();
            if (this.timestampChangeDirection != 0) {
                f2 = 0.0f;
                canvas.translate(AndroidUtilities.dp(8.0f) + (AndroidUtilities.dp(16.0f) * (-this.timestampChangeDirection) * interpolation), 0.0f);
            } else {
                f2 = 0.0f;
            }
            canvas.translate(f2, (-this.timestampLabel[1].getHeight()) / 2.0f);
            this.timestampLabelPaint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f * (1.0f - interpolation) * this.timestampsAppearing));
            this.timestampLabel[1].draw(canvas);
            canvas.restore();
        }
        if (this.timestampLabel[0] != null) {
            canvas.save();
            if (this.timestampChangeDirection != 0) {
                f = 0.0f;
                canvas.translate(AndroidUtilities.dp(8.0f) + (AndroidUtilities.dp(16.0f) * this.timestampChangeDirection * (1.0f - interpolation)), 0.0f);
            } else {
                f = 0.0f;
            }
            canvas.translate(f, (-this.timestampLabel[0].getHeight()) / 2.0f);
            this.timestampLabelPaint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f * interpolation * this.timestampsAppearing));
            this.timestampLabel[0].draw(canvas);
            canvas.restore();
        }
        canvas.restore();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTouch$0() {
        this.pressedDelayed = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$updateTimestamps$1(Pair pair, Pair pair2) {
        if (((Float) pair.first).floatValue() > ((Float) pair2.first).floatValue()) {
            return 1;
        }
        return ((Float) pair2.first).floatValue() > ((Float) pair.first).floatValue() ? -1 : 0;
    }

    private StaticLayout makeStaticLayout(CharSequence charSequence, int i) {
        StaticLayout.Builder obtain;
        StaticLayout.Builder maxLines;
        StaticLayout.Builder alignment;
        StaticLayout.Builder ellipsize;
        StaticLayout.Builder ellipsizedWidth;
        StaticLayout build;
        if (this.timestampLabelPaint == null) {
            TextPaint textPaint = new TextPaint(1);
            this.timestampLabelPaint = textPaint;
            textPaint.setTextSize(AndroidUtilities.dp(12.0f));
            this.timestampLabelPaint.setColor(-1);
        }
        CharSequence charSequence2 = charSequence == null ? "" : charSequence;
        if (Build.VERSION.SDK_INT < 23) {
            return new StaticLayout(charSequence2, 0, charSequence2.length(), this.timestampLabelPaint, i, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, Math.min(AndroidUtilities.dp(400.0f), i));
        }
        obtain = StaticLayout.Builder.obtain(charSequence2, 0, charSequence2.length(), this.timestampLabelPaint, i);
        maxLines = obtain.setMaxLines(1);
        alignment = maxLines.setAlignment(Layout.Alignment.ALIGN_CENTER);
        ellipsize = alignment.setEllipsize(TextUtils.TruncateAt.END);
        ellipsizedWidth = ellipsize.setEllipsizedWidth(Math.min(AndroidUtilities.dp(400.0f), i));
        build = ellipsizedWidth.build();
        return build;
    }

    private void setPaintColor(int i, float f) {
        if (f < 1.0f) {
            i = ColorUtils.setAlphaComponent(i, (int) (Color.alpha(i) * f));
        }
        paint.setColor(i);
    }

    public void clearTimestamps() {
        this.timestamps = null;
        this.currentTimestamp = -1;
        this.timestampsAppearing = 0.0f;
        StaticLayout[] staticLayoutArr = this.timestampLabel;
        if (staticLayoutArr != null) {
            staticLayoutArr[1] = null;
            staticLayoutArr[0] = null;
        }
        this.lastCaption = null;
        this.lastVideoDuration = -1L;
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0175, code lost:
    
        if (r16.selected != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x01b6, code lost:
    
        r6 = r16.cacheColor;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x01b3, code lost:
    
        r6 = r16.backgroundSelectedColor;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0202, code lost:
    
        if (r8 > r6) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0217, code lost:
    
        r6 = r16.parentView;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0219, code lost:
    
        if (r6 == null) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x021b, code lost:
    
        r6.invalidate();
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0215, code lost:
    
        r16.currentRadius = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0213, code lost:
    
        if (r8 < r6) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x01b1, code lost:
    
        if (r16.selected != false) goto L42;
     */
    /* JADX WARN: Removed duplicated region for block: B:40:0x01ca  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x022f  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x02fa  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0320  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x035b  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x035d  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01cd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas, View view) {
        float dp;
        int i;
        this.rect.left = this.horizontalPadding + AndroidUtilities.lerp(thumbWidth / 2.0f, 0.0f, this.transitionProgress);
        RectF rectF = this.rect;
        int i2 = this.height;
        rectF.top = AndroidUtilities.lerp((i2 - this.lineHeight) / 2.0f, (i2 - AndroidUtilities.dp(3.0f)) - this.smallLineHeight, this.transitionProgress);
        RectF rectF2 = this.rect;
        int i3 = this.height;
        rectF2.bottom = AndroidUtilities.lerp((this.lineHeight + i3) / 2.0f, i3 - AndroidUtilities.dp(3.0f), this.transitionProgress);
        float f = this.thumbX;
        float min = Math.min(this.animatedThumbX, f);
        this.animatedThumbX = min;
        float lerp = AndroidUtilities.lerp(min, f, 0.5f);
        this.animatedThumbX = lerp;
        if (Math.abs(f - lerp) > 0.005f) {
            this.parentView.invalidate();
        }
        float f2 = this.animatedThumbX;
        float f3 = this.animateThumbProgress;
        if (f3 != 1.0f) {
            float f4 = f3 + 0.07272727f;
            this.animateThumbProgress = f4;
            if (f4 >= 1.0f) {
                this.animateThumbProgress = 1.0f;
            } else {
                view.invalidate();
                float interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(this.animateThumbProgress);
                f2 = (f2 * interpolation) + (this.fromThumbX * (1.0f - interpolation));
            }
        }
        float f5 = this.animateThumbLoopBackProgress.set(0.0f);
        if (this.pressed) {
            f5 = 0.0f;
        }
        this.rect.right = this.horizontalPadding + AndroidUtilities.lerp(this.width - (thumbWidth / 2.0f), this.parentView.getWidth() - (this.horizontalPadding * 2.0f), this.transitionProgress);
        setPaintColor(this.selected ? this.backgroundSelectedColor : this.backgroundColor, 1.0f - this.transitionProgress);
        drawProgressBar(canvas, this.rect, paint);
        float f6 = this.bufferedAnimationValue;
        if (f6 != 1.0f) {
            float f7 = f6 + 0.16f;
            this.bufferedAnimationValue = f7;
            if (f7 > 1.0f) {
                this.bufferedAnimationValue = 1.0f;
            } else {
                this.parentView.invalidate();
            }
        }
        if (!this.animateResetBuffering) {
            float f8 = this.animateFromBufferedProgress;
            float f9 = this.bufferedAnimationValue;
            float f10 = (f8 * (1.0f - f9)) + (this.bufferedProgress * f9);
            if (f10 > 0.0f) {
                this.rect.right = this.horizontalPadding + AndroidUtilities.lerp((thumbWidth / 2.0f) + (f10 * (this.width - r10)), this.parentView.getWidth() - (this.horizontalPadding * 2.0f), this.transitionProgress);
            }
            dp = AndroidUtilities.dp(this.pressed ? 8.0f : 6.0f);
            if (this.currentRadius != dp) {
            }
            float lerp2 = AndroidUtilities.lerp(this.currentRadius, 0.0f, this.transitionProgress);
            if (f5 > 0.0f) {
            }
            RectF rectF3 = this.rect;
            float f11 = this.horizontalPadding;
            float f12 = thumbWidth / 2.0f;
            if (this.pressed) {
            }
            rectF3.right = f11 + AndroidUtilities.lerp(f12 + f2, (this.parentView.getWidth() - (this.horizontalPadding * 2.0f)) * getProgress(), this.transitionProgress);
            if (this.transitionProgress > 0.0f) {
                strokePaint.setAlpha((int) (this.transitionProgress * 255.0f * 0.2f));
                drawProgressBar(canvas, this.rect, strokePaint);
            }
            setPaintColor(ColorUtils.blendARGB(this.progressColor, this.smallLineColor, this.transitionProgress), 1.0f);
            drawProgressBar(canvas, this.rect, paint);
            setPaintColor(ColorUtils.blendARGB(this.circleColor, getProgress() == 0.0f ? 0 : this.smallLineColor, this.transitionProgress), 1.0f - this.transitionProgress);
            RectF rectF4 = this.rect;
            canvas.drawCircle(rectF4.right, rectF4.centerY(), lerp2 * (1.0f - f5), paint);
            drawTimestampLabel(canvas);
        }
        float f13 = this.animateFromBufferedProgress;
        if (f13 > 0.0f) {
            this.rect.right = this.horizontalPadding + AndroidUtilities.lerp((thumbWidth / 2.0f) + (f13 * (this.width - r10)), this.parentView.getWidth() - (this.horizontalPadding * 2.0f), this.transitionProgress);
            setPaintColor(this.selected ? this.backgroundSelectedColor : this.cacheColor, (1.0f - this.transitionProgress) * (1.0f - this.bufferedAnimationValue));
            drawProgressBar(canvas, this.rect, paint);
        }
        float f14 = this.bufferedProgress;
        if (f14 > 0.0f) {
            this.rect.right = this.horizontalPadding + AndroidUtilities.lerp((thumbWidth / 2.0f) + (f14 * (this.width - r10)), this.parentView.getWidth() - (this.horizontalPadding * 2.0f), this.transitionProgress);
        }
        dp = AndroidUtilities.dp(this.pressed ? 8.0f : 6.0f);
        if (this.currentRadius != dp) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j = elapsedRealtime - this.lastUpdateTime;
            this.lastUpdateTime = elapsedRealtime;
            if (j > 18) {
                j = 16;
            }
            float f15 = this.currentRadius;
            if (f15 < dp) {
                float dp2 = f15 + (AndroidUtilities.dp(1.0f) * (j / 60.0f));
                this.currentRadius = dp2;
            } else {
                float dp3 = f15 - (AndroidUtilities.dp(1.0f) * (j / 60.0f));
                this.currentRadius = dp3;
            }
        }
        float lerp22 = AndroidUtilities.lerp(this.currentRadius, 0.0f, this.transitionProgress);
        if (f5 > 0.0f) {
            RectF rectF5 = this.rect;
            float f16 = rectF5.left;
            rectF5.right = this.horizontalPadding + AndroidUtilities.lerp((thumbWidth / 2.0f) + (this.width - r14), this.parentView.getWidth() - (this.horizontalPadding * 2.0f), this.transitionProgress);
            RectF rectF6 = this.rect;
            rectF6.left = AndroidUtilities.lerp(f16, rectF6.right, 1.0f - f5);
            if (this.transitionProgress > 0.0f && this.rect.width() > 0.0f) {
                strokePaint.setAlpha((int) (this.transitionProgress * 255.0f * 0.2f));
                drawProgressBar(canvas, this.rect, strokePaint);
            }
            setPaintColor(ColorUtils.blendARGB(this.progressColor, this.smallLineColor, this.transitionProgress), 1.0f);
            drawProgressBar(canvas, this.rect, paint);
            this.rect.left = f16;
            setPaintColor(ColorUtils.blendARGB(this.circleColor, getProgress() == 0.0f ? 0 : this.smallLineColor, this.transitionProgress), 1.0f - this.transitionProgress);
            canvas.drawCircle(this.horizontalPadding + AndroidUtilities.lerp((thumbWidth / 2.0f) + this.loopBackWasThumbX, (this.parentView.getWidth() - (this.horizontalPadding * 2.0f)) * (this.loopBackWasThumbX / (this.width - thumbWidth)), this.transitionProgress), this.rect.centerY(), lerp22 * f5, paint);
        }
        RectF rectF32 = this.rect;
        float f112 = this.horizontalPadding;
        float f122 = thumbWidth / 2.0f;
        if (this.pressed) {
            f2 = this.draggingThumbX;
        }
        rectF32.right = f112 + AndroidUtilities.lerp(f122 + f2, (this.parentView.getWidth() - (this.horizontalPadding * 2.0f)) * getProgress(), this.transitionProgress);
        if (this.transitionProgress > 0.0f && this.rect.width() > 0.0f) {
            strokePaint.setAlpha((int) (this.transitionProgress * 255.0f * 0.2f));
            drawProgressBar(canvas, this.rect, strokePaint);
        }
        setPaintColor(ColorUtils.blendARGB(this.progressColor, this.smallLineColor, this.transitionProgress), 1.0f);
        drawProgressBar(canvas, this.rect, paint);
        setPaintColor(ColorUtils.blendARGB(this.circleColor, getProgress() == 0.0f ? 0 : this.smallLineColor, this.transitionProgress), 1.0f - this.transitionProgress);
        RectF rectF42 = this.rect;
        canvas.drawCircle(rectF42.right, rectF42.centerY(), lerp22 * (1.0f - f5), paint);
        drawTimestampLabel(canvas);
        setPaintColor(i, 1.0f - this.transitionProgress);
        drawProgressBar(canvas, this.rect, paint);
        dp = AndroidUtilities.dp(this.pressed ? 8.0f : 6.0f);
        if (this.currentRadius != dp) {
        }
        float lerp222 = AndroidUtilities.lerp(this.currentRadius, 0.0f, this.transitionProgress);
        if (f5 > 0.0f) {
        }
        RectF rectF322 = this.rect;
        float f1122 = this.horizontalPadding;
        float f1222 = thumbWidth / 2.0f;
        if (this.pressed) {
        }
        rectF322.right = f1122 + AndroidUtilities.lerp(f1222 + f2, (this.parentView.getWidth() - (this.horizontalPadding * 2.0f)) * getProgress(), this.transitionProgress);
        if (this.transitionProgress > 0.0f) {
        }
        setPaintColor(ColorUtils.blendARGB(this.progressColor, this.smallLineColor, this.transitionProgress), 1.0f);
        drawProgressBar(canvas, this.rect, paint);
        setPaintColor(ColorUtils.blendARGB(this.circleColor, getProgress() == 0.0f ? 0 : this.smallLineColor, this.transitionProgress), 1.0f - this.transitionProgress);
        RectF rectF422 = this.rect;
        canvas.drawCircle(rectF422.right, rectF422.centerY(), lerp222 * (1.0f - f5), paint);
        drawTimestampLabel(canvas);
    }

    public float getProgress() {
        return this.thumbX / (this.width - thumbWidth);
    }

    public int getThumbX() {
        return (this.pressed ? this.draggingThumbX : this.thumbX) + (thumbWidth / 2);
    }

    public int getWidth() {
        return this.width - thumbWidth;
    }

    public boolean isDragging() {
        return this.pressed;
    }

    public boolean onTouch(int i, float f, float f2) {
        SeekBarDelegate seekBarDelegate;
        if (i == 0) {
            if (this.transitionProgress > 0.0f) {
                return false;
            }
            int i2 = this.height;
            int i3 = thumbWidth;
            int i4 = (i2 - i3) / 2;
            if (f >= (-i4)) {
                int i5 = this.width;
                if (f <= i5 + i4 && f2 >= 0.0f && f2 <= i2) {
                    int i6 = this.thumbX;
                    if (i6 - i4 > f || f > i6 + i3 + i4) {
                        int i7 = ((int) f) - (i3 / 2);
                        this.thumbX = i7;
                        if (i7 < 0) {
                            this.thumbX = 0;
                        } else if (i7 > i5 - i3) {
                            this.thumbX = i3 - i5;
                        }
                        this.animatedThumbX = this.thumbX;
                    }
                    this.pressedDelayed = true;
                    this.pressed = true;
                    int i8 = this.thumbX;
                    this.draggingThumbX = i8;
                    this.thumbDX = (int) (f - i8);
                    return true;
                }
            }
        } else if (i == 1 || i == 3) {
            if (this.pressed) {
                int i9 = this.draggingThumbX;
                this.thumbX = i9;
                float f3 = i9;
                this.animatedThumbX = f3;
                if (i == 1 && (seekBarDelegate = this.delegate) != null) {
                    seekBarDelegate.onSeekBarDrag(f3 / (this.width - thumbWidth));
                }
                this.pressed = false;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.VideoPlayerSeekBar$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        VideoPlayerSeekBar.this.lambda$onTouch$0();
                    }
                }, 50L);
                return true;
            }
        } else if (i == 2 && this.pressed) {
            int i10 = (int) (f - this.thumbDX);
            this.draggingThumbX = i10;
            if (i10 < 0) {
                this.draggingThumbX = 0;
            } else {
                int i11 = this.width - thumbWidth;
                if (i10 > i11) {
                    this.draggingThumbX = i11;
                }
            }
            SeekBarDelegate seekBarDelegate2 = this.delegate;
            if (seekBarDelegate2 != null) {
                seekBarDelegate2.onSeekBarContinuousDrag(this.draggingThumbX / (this.width - thumbWidth));
            }
            return true;
        }
        return false;
    }

    public void setBufferedProgress(float f) {
        float f2 = this.bufferedProgress;
        if (f != f2) {
            this.animateFromBufferedProgress = f2;
            this.animateResetBuffering = f < f2;
            this.bufferedProgress = f;
            this.bufferedAnimationValue = 0.0f;
        }
    }

    public void setColors(int i, int i2, int i3, int i4, int i5, int i6) {
        this.backgroundColor = i;
        this.cacheColor = i2;
        this.circleColor = i4;
        this.progressColor = i3;
        this.backgroundSelectedColor = i5;
        this.smallLineColor = i6;
    }

    public void setDelegate(SeekBarDelegate seekBarDelegate) {
        this.delegate = seekBarDelegate;
    }

    public void setHorizontalPadding(int i) {
        this.horizontalPadding = i;
    }

    public void setProgress(float f) {
        setProgress(f, false);
    }

    public void setProgress(float f, boolean z) {
        if (Math.abs(this.progress - 1.0f) < 0.04f && Math.abs(f) < 0.04f) {
            this.animateThumbLoopBackProgress.set(1.0f, true);
            this.loopBackWasThumbX = this.thumbX;
        }
        this.progress = f;
        int ceil = (int) Math.ceil((this.width - thumbWidth) * f);
        if (z) {
            if (Math.abs(ceil - this.thumbX) > AndroidUtilities.dp(10.0f)) {
                float interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(this.animateThumbProgress);
                this.fromThumbX = (int) ((this.thumbX * interpolation) + (this.fromThumbX * (1.0f - interpolation)));
                this.animateThumbProgress = 0.0f;
            } else if (this.animateThumbProgress == 1.0f) {
                this.animateThumbProgress = 0.0f;
                this.fromThumbX = this.thumbX;
            }
        }
        this.thumbX = ceil;
        if (ceil < 0) {
            this.thumbX = 0;
        } else {
            int i = this.width - thumbWidth;
            if (ceil > i) {
                this.thumbX = i;
            }
        }
        if (Math.abs(this.animatedThumbX - this.thumbX) > AndroidUtilities.dp(8.0f)) {
            this.animatedThumbX = this.thumbX;
        }
    }

    public void setSize(int i, int i2) {
        this.width = i;
        this.height = i2;
        View view = this.parentView;
        if (view != null) {
            view.invalidate();
        }
    }

    public void setTransitionProgress(float f) {
        if (this.transitionProgress != f) {
            this.transitionProgress = f;
            this.parentView.invalidate();
        }
    }

    public void updateTimestamps(MessageObject messageObject, long j) {
        Integer parseInt;
        String str;
        if (messageObject == null || j < 0) {
            clearTimestamps();
            return;
        }
        CharSequence charSequence = messageObject.caption;
        if (messageObject.isYouTubeVideo()) {
            if (messageObject.youtubeDescription == null && (str = messageObject.messageOwner.media.webpage.description) != null) {
                messageObject.youtubeDescription = SpannableString.valueOf(str);
                MessageObject.addUrlsByPattern(messageObject.isOut(), messageObject.youtubeDescription, false, 3, (int) j, false);
            }
            charSequence = messageObject.youtubeDescription;
        }
        if (charSequence == this.lastCaption && this.lastVideoDuration == j) {
            return;
        }
        this.lastCaption = charSequence;
        this.lastVideoDuration = j;
        if (!(charSequence instanceof Spanned)) {
            this.timestamps = null;
            this.currentTimestamp = -1;
            this.timestampsAppearing = 0.0f;
            StaticLayout[] staticLayoutArr = this.timestampLabel;
            if (staticLayoutArr != null) {
                staticLayoutArr[1] = null;
                staticLayoutArr[0] = null;
                return;
            }
            return;
        }
        Spanned spanned = (Spanned) charSequence;
        try {
            URLSpanNoUnderline[] uRLSpanNoUnderlineArr = (URLSpanNoUnderline[]) spanned.getSpans(0, spanned.length(), URLSpanNoUnderline.class);
            this.timestamps = new ArrayList();
            this.timestampsAppearing = 0.0f;
            if (this.timestampLabelPaint == null) {
                TextPaint textPaint = new TextPaint(1);
                this.timestampLabelPaint = textPaint;
                textPaint.setTextSize(AndroidUtilities.dp(12.0f));
                this.timestampLabelPaint.setColor(-1);
            }
            for (URLSpanNoUnderline uRLSpanNoUnderline : uRLSpanNoUnderlineArr) {
                if (uRLSpanNoUnderline != null && uRLSpanNoUnderline.getURL() != null && uRLSpanNoUnderline.label != null && uRLSpanNoUnderline.getURL().startsWith("video?") && (parseInt = Utilities.parseInt((CharSequence) uRLSpanNoUnderline.getURL().substring(6))) != null && parseInt.intValue() >= 0) {
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(uRLSpanNoUnderline.label);
                    Emoji.replaceEmoji(spannableStringBuilder, this.timestampLabelPaint.getFontMetricsInt(), false);
                    this.timestamps.add(new Pair(Float.valueOf((parseInt.intValue() * 1000) / j), spannableStringBuilder));
                }
            }
            Collections.sort(this.timestamps, new Comparator() { // from class: org.telegram.ui.Components.VideoPlayerSeekBar$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$updateTimestamps$1;
                    lambda$updateTimestamps$1 = VideoPlayerSeekBar.lambda$updateTimestamps$1((Pair) obj, (Pair) obj2);
                    return lambda$updateTimestamps$1;
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
            this.timestamps = null;
            this.currentTimestamp = -1;
            this.timestampsAppearing = 0.0f;
            StaticLayout[] staticLayoutArr2 = this.timestampLabel;
            if (staticLayoutArr2 != null) {
                staticLayoutArr2[1] = null;
                staticLayoutArr2[0] = null;
            }
        }
    }
}
