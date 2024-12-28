package org.telegram.ui.Components;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
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
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;

/* loaded from: classes3.dex */
public class SeekBarView extends FrameLayout {
    private static Path tmpPath;
    private static float[] tmpRadii;
    private final float TIMESTAMP_GAP;
    private AnimatedFloat animatedThumbX;
    private float bufferedProgress;
    boolean captured;
    private float currentRadius;
    private int currentTimestamp;
    public SeekBarViewDelegate delegate;
    private Drawable hoverDrawable;
    private Paint innerPaint1;
    private CharSequence lastCaption;
    private long lastDuration;
    private int lastTimestamp;
    private long lastTimestampUpdate;
    private long lastTimestampsAppearingUpdate;
    private long lastUpdateTime;
    int lastValue;
    private float lastWidth;
    private int lineWidthDp;
    private float minProgress;
    private Paint outerPaint1;
    private boolean pressed;
    private boolean pressedDelayed;
    private int[] pressedState;
    private float progressToSet;
    private RectF rect;
    private boolean reportChanges;
    private final Theme.ResourcesProvider resourcesProvider;
    private final SeekBarAccessibilityDelegate seekBarAccessibilityDelegate;
    private int selectorWidth;
    private int separatorsCount;
    float sx;
    float sy;
    private int thumbDX;
    private int thumbSize;
    private int thumbX;
    private int timestampChangeDirection;
    private float timestampChangeT;
    private StaticLayout[] timestampLabel;
    private TextPaint timestampLabelPaint;
    private ArrayList timestamps;
    private float timestampsAppearing;
    private float transitionProgress;
    private int transitionThumbX;
    private boolean twoSided;

    public interface SeekBarViewDelegate {

        public abstract /* synthetic */ class -CC {
            public static CharSequence $default$getContentDescription(SeekBarViewDelegate seekBarViewDelegate) {
                return null;
            }

            public static int $default$getStepsCount(SeekBarViewDelegate seekBarViewDelegate) {
                return 0;
            }

            public static void $default$onSeekBarPressed(SeekBarViewDelegate seekBarViewDelegate, boolean z) {
            }
        }

        CharSequence getContentDescription();

        int getStepsCount();

        void onSeekBarDrag(boolean z, float f);

        void onSeekBarPressed(boolean z);
    }

    public SeekBarView(Context context) {
        this(context, null);
    }

    public SeekBarView(Context context, Theme.ResourcesProvider resourcesProvider) {
        this(context, false, resourcesProvider);
    }

    public SeekBarView(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.animatedThumbX = new AnimatedFloat(this, 0L, 80L, CubicBezierInterpolator.EASE_OUT);
        this.progressToSet = -100.0f;
        this.minProgress = -1.0f;
        this.pressedState = new int[]{R.attr.state_enabled, R.attr.state_pressed};
        this.transitionProgress = 1.0f;
        this.lineWidthDp = 3;
        this.timestampsAppearing = 0.0f;
        this.TIMESTAMP_GAP = 1.0f;
        this.currentTimestamp = -1;
        this.lastTimestamp = -1;
        this.timestampChangeT = 1.0f;
        this.lastWidth = -1.0f;
        this.rect = new RectF();
        this.resourcesProvider = resourcesProvider;
        setWillNotDraw(false);
        this.innerPaint1 = new Paint(1);
        Paint paint = new Paint(1);
        this.outerPaint1 = paint;
        int i = Theme.key_player_progress;
        paint.setColor(getThemedColor(i));
        this.selectorWidth = AndroidUtilities.dp(32.0f);
        this.thumbSize = AndroidUtilities.dp(24.0f);
        this.currentRadius = AndroidUtilities.dp(6.0f);
        if (Build.VERSION.SDK_INT >= 21) {
            Drawable createSelectorDrawable = Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(getThemedColor(i), 40), 1, AndroidUtilities.dp(16.0f));
            this.hoverDrawable = createSelectorDrawable;
            createSelectorDrawable.setCallback(this);
            this.hoverDrawable.setVisible(true, false);
        }
        setImportantForAccessibility(1);
        FloatSeekBarAccessibilityDelegate floatSeekBarAccessibilityDelegate = new FloatSeekBarAccessibilityDelegate(z) { // from class: org.telegram.ui.Components.SeekBarView.1
            @Override // org.telegram.ui.Components.SeekBarAccessibilityDelegate
            public CharSequence getContentDescription(View view) {
                SeekBarViewDelegate seekBarViewDelegate = SeekBarView.this.delegate;
                if (seekBarViewDelegate != null) {
                    return seekBarViewDelegate.getContentDescription();
                }
                return null;
            }

            @Override // org.telegram.ui.Components.FloatSeekBarAccessibilityDelegate
            protected float getDelta() {
                int stepsCount = SeekBarView.this.delegate.getStepsCount();
                return stepsCount > 0 ? 1.0f / stepsCount : super.getDelta();
            }

            @Override // org.telegram.ui.Components.FloatSeekBarAccessibilityDelegate
            public float getProgress() {
                return SeekBarView.this.getProgress();
            }

            @Override // org.telegram.ui.Components.FloatSeekBarAccessibilityDelegate
            public void setProgress(float f) {
                SeekBarView.this.pressed = true;
                SeekBarView.this.setProgress(f);
                SeekBarView.this.setSeekBarDrag(true, f);
                SeekBarView.this.pressed = false;
            }
        };
        this.seekBarAccessibilityDelegate = floatSeekBarAccessibilityDelegate;
        setAccessibilityDelegate(floatSeekBarAccessibilityDelegate);
    }

    private void drawProgressBar(Canvas canvas, RectF rectF, Paint paint) {
        int i;
        SeekBarView seekBarView = this;
        float dp = AndroidUtilities.dp(2.0f);
        ArrayList arrayList = seekBarView.timestamps;
        if (arrayList == null || arrayList.isEmpty()) {
            canvas.drawRoundRect(rectF, dp, dp, paint);
            return;
        }
        float f = rectF.bottom;
        float f2 = seekBarView.selectorWidth / 2.0f;
        float measuredWidth = getMeasuredWidth() - (seekBarView.selectorWidth / 2.0f);
        AndroidUtilities.rectTmp.set(rectF);
        float dp2 = AndroidUtilities.dp(seekBarView.timestampsAppearing * 1.0f) / 2.0f;
        if (tmpPath == null) {
            tmpPath = new Path();
        }
        tmpPath.reset();
        float dp3 = AndroidUtilities.dp(4.0f) / (measuredWidth - f2);
        int i2 = 0;
        while (true) {
            i = -1;
            if (i2 >= seekBarView.timestamps.size()) {
                i2 = -1;
                break;
            } else if (((Float) ((Pair) seekBarView.timestamps.get(i2)).first).floatValue() >= dp3) {
                break;
            } else {
                i2++;
            }
        }
        if (i2 < 0) {
            i2 = 0;
        }
        int i3 = 1;
        int size = seekBarView.timestamps.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            if (1.0f - ((Float) ((Pair) seekBarView.timestamps.get(size)).first).floatValue() >= dp3) {
                i = size + 1;
                break;
            }
            size--;
        }
        if (i < 0) {
            i = seekBarView.timestamps.size();
        }
        int i4 = i2;
        while (i4 <= i) {
            float floatValue = i4 == i2 ? 0.0f : ((Float) ((Pair) seekBarView.timestamps.get(i4 - 1)).first).floatValue();
            float floatValue2 = i4 == i ? 1.0f : ((Float) ((Pair) seekBarView.timestamps.get(i4)).first).floatValue();
            while (i4 != i && i4 != 0 && i4 < seekBarView.timestamps.size() - i3 && ((Float) ((Pair) seekBarView.timestamps.get(i4)).first).floatValue() - floatValue <= dp3) {
                i4++;
                floatValue2 = ((Float) ((Pair) seekBarView.timestamps.get(i4)).first).floatValue();
            }
            RectF rectF2 = AndroidUtilities.rectTmp;
            rectF2.left = AndroidUtilities.lerp(f2, measuredWidth, floatValue) + (i4 > 0 ? dp2 : 0.0f);
            float lerp = AndroidUtilities.lerp(f2, measuredWidth, floatValue2) - (i4 < i ? dp2 : 0.0f);
            rectF2.right = lerp;
            float f3 = rectF.right;
            boolean z = lerp > f3;
            if (z) {
                rectF2.right = f3;
            }
            float f4 = rectF2.right;
            float f5 = rectF.left;
            if (f4 >= f5) {
                if (rectF2.left < f5) {
                    rectF2.left = f5;
                }
                if (tmpRadii == null) {
                    tmpRadii = new float[8];
                }
                if (i4 == i2 || (z && rectF2.left >= rectF.left)) {
                    float[] fArr = tmpRadii;
                    fArr[7] = dp;
                    fArr[6] = dp;
                    fArr[1] = dp;
                    fArr[0] = dp;
                    float f6 = 0.7f * dp * seekBarView.timestampsAppearing;
                    fArr[5] = f6;
                    fArr[4] = f6;
                    fArr[3] = f6;
                    fArr[2] = f6;
                } else if (i4 >= i) {
                    float[] fArr2 = tmpRadii;
                    float f7 = 0.7f * dp * seekBarView.timestampsAppearing;
                    fArr2[7] = f7;
                    fArr2[6] = f7;
                    fArr2[1] = f7;
                    fArr2[0] = f7;
                    fArr2[5] = dp;
                    fArr2[4] = dp;
                    fArr2[3] = dp;
                    fArr2[2] = dp;
                } else {
                    float[] fArr3 = tmpRadii;
                    float f8 = 0.7f * dp * seekBarView.timestampsAppearing;
                    fArr3[5] = f8;
                    fArr3[4] = f8;
                    fArr3[3] = f8;
                    fArr3[2] = f8;
                    fArr3[7] = f8;
                    fArr3[6] = f8;
                    fArr3[1] = f8;
                    fArr3[0] = f8;
                }
                tmpPath.addRoundRect(rectF2, tmpRadii, Path.Direction.CW);
                if (z) {
                    break;
                }
            }
            i4++;
            i3 = 1;
            seekBarView = this;
        }
        canvas.drawPath(tmpPath, paint);
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x0104, code lost:
    
        if (r1 > r5) goto L59;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void drawTimestampLabel(Canvas canvas) {
        ArrayList arrayList = this.timestamps;
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        float progress = getProgress();
        int size = this.timestamps.size() - 1;
        while (true) {
            if (size < 0) {
                size = -1;
                break;
            } else if (((Float) ((Pair) this.timestamps.get(size)).first).floatValue() - 0.001f <= progress) {
                break;
            } else {
                size--;
            }
        }
        if (this.timestampLabel == null) {
            this.timestampLabel = new StaticLayout[2];
        }
        float dp = (this.selectorWidth / 2.0f) + (this.lastDuration > 600000 ? AndroidUtilities.dp(42.0f) : 0);
        float abs = Math.abs(dp - ((getMeasuredWidth() - (this.selectorWidth / 2.0f)) - (this.lastDuration > 600000 ? AndroidUtilities.dp(42.0f) : 0))) - AndroidUtilities.dp(66.0f);
        float f = this.lastWidth;
        if (f > 0.0f && Math.abs(f - abs) > 0.01f) {
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
                AndroidUtilities.vibrateCursor(this);
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
            invalidate();
            this.lastTimestampUpdate = SystemClock.elapsedRealtime();
        }
        if (this.timestampsAppearing < 1.0f) {
            this.timestampsAppearing = Math.min(this.timestampsAppearing + (Math.min(17L, Math.abs(SystemClock.elapsedRealtime() - this.lastTimestampUpdate)) / 200.0f), 1.0f);
            invalidate();
            this.lastTimestampsAppearingUpdate = SystemClock.elapsedRealtime();
        }
        float interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(this.timestampChangeT);
        canvas.save();
        canvas.translate(dp + AndroidUtilities.dp(25.0f), (getMeasuredHeight() / 2.0f) + AndroidUtilities.dp(14.0f));
        this.timestampLabelPaint.setColor(getThemedColor(Theme.key_player_time));
        if (this.timestampLabel[1] != null) {
            canvas.save();
            if (this.timestampChangeDirection != 0) {
                canvas.translate(AndroidUtilities.dp(8.0f) + (AndroidUtilities.dp(16.0f) * (-this.timestampChangeDirection) * interpolation), 0.0f);
            }
            canvas.translate(0.0f, (-this.timestampLabel[1].getHeight()) / 2.0f);
            this.timestampLabelPaint.setAlpha((int) ((1.0f - interpolation) * 255.0f * this.timestampsAppearing));
            this.timestampLabel[1].draw(canvas);
            canvas.restore();
        }
        if (this.timestampLabel[0] != null) {
            canvas.save();
            if (this.timestampChangeDirection != 0) {
                canvas.translate(AndroidUtilities.dp(8.0f) + (AndroidUtilities.dp(16.0f) * this.timestampChangeDirection * (1.0f - interpolation)), 0.0f);
            }
            canvas.translate(0.0f, (-this.timestampLabel[0].getHeight()) / 2.0f);
            this.timestampLabelPaint.setAlpha((int) (interpolation * 255.0f * this.timestampsAppearing));
            this.timestampLabel[0].draw(canvas);
            canvas.restore();
        }
        canvas.restore();
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
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
        }
        this.timestampLabelPaint.setColor(getThemedColor(Theme.key_player_time));
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

    private int minThumbX() {
        return Math.max((int) (this.minProgress * (getMeasuredWidth() - this.selectorWidth)), 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSeekBarDrag(boolean z, float f) {
        SeekBarViewDelegate seekBarViewDelegate = this.delegate;
        if (seekBarViewDelegate != null) {
            seekBarViewDelegate.onSeekBarDrag(z, f);
        }
        if (this.separatorsCount > 1) {
            int round = Math.round((r0 - 1) * f);
            if (!z && round != this.lastValue) {
                AndroidUtilities.vibrateCursor(this);
            }
            this.lastValue = round;
        }
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
        this.lastDuration = -1L;
    }

    public float getProgress() {
        return getMeasuredWidth() == 0 ? this.progressToSet : this.thumbX / (getMeasuredWidth() - this.selectorWidth);
    }

    public SeekBarAccessibilityDelegate getSeekBarAccessibilityDelegate() {
        return this.seekBarAccessibilityDelegate;
    }

    public boolean isDragging() {
        return this.pressed;
    }

    public boolean isTwoSided() {
        return this.twoSided;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x01e7, code lost:
    
        if (r4 > r1) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x01fc, code lost:
    
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x01fa, code lost:
    
        r16.currentRadius = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x01f8, code lost:
    
        if (r4 < r1) goto L39;
     */
    /* JADX WARN: Removed duplicated region for block: B:38:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x027a  */
    /* JADX WARN: Removed duplicated region for block: B:47:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0262  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        boolean z;
        float f;
        float f2;
        float measuredHeight;
        int measuredWidth;
        int i = this.thumbX;
        boolean z2 = true;
        if (!this.twoSided && this.separatorsCount > 1) {
            i = (int) this.animatedThumbX.set(Math.round(i / r2) * ((getMeasuredWidth() - this.selectorWidth) / (this.separatorsCount - 1.0f)));
        }
        int i2 = i;
        int measuredHeight2 = (getMeasuredHeight() - this.thumbSize) / 2;
        this.innerPaint1.setColor(getThemedColor(Theme.key_player_progressBackground));
        float measuredHeight3 = getMeasuredHeight() / 2.0f;
        float f3 = this.selectorWidth / 2.0f;
        float measuredWidth2 = getMeasuredWidth() - (this.selectorWidth / 2);
        float dp = measuredHeight3 - (AndroidUtilities.dp(this.lineWidthDp) / 2.0f);
        float dp2 = measuredHeight3 + (AndroidUtilities.dp(this.lineWidthDp) / 2.0f);
        this.rect.set(f3, dp, measuredWidth2, dp2);
        drawProgressBar(canvas, this.rect, this.innerPaint1);
        if (this.bufferedProgress > 0.0f) {
            this.innerPaint1.setColor(getThemedColor(Theme.key_player_progressCachedBackground));
            this.rect.set(f3, dp, (this.selectorWidth / 2.0f) + (this.bufferedProgress * (getMeasuredWidth() - this.selectorWidth)), dp2);
            drawProgressBar(canvas, this.rect, this.innerPaint1);
        }
        if (this.twoSided) {
            canvas.drawRect((getMeasuredWidth() / 2) - AndroidUtilities.dp(1.0f), (getMeasuredHeight() / 2) - AndroidUtilities.dp(6.0f), (getMeasuredWidth() / 2) + AndroidUtilities.dp(1.0f), (getMeasuredHeight() / 2) + AndroidUtilities.dp(6.0f), this.outerPaint1);
            int measuredWidth3 = getMeasuredWidth();
            int i3 = this.selectorWidth;
            if (i2 > (measuredWidth3 - i3) / 2) {
                f2 = getMeasuredWidth() / 2;
                measuredHeight = (getMeasuredHeight() / 2) - AndroidUtilities.dp(1.0f);
                measuredWidth = (this.selectorWidth / 2) + i2;
            } else {
                f2 = (i3 / 2) + i2;
                measuredHeight = (getMeasuredHeight() / 2) - AndroidUtilities.dp(1.0f);
                measuredWidth = getMeasuredWidth() / 2;
            }
            canvas.drawRect(f2, measuredHeight, measuredWidth, (getMeasuredHeight() / 2) + AndroidUtilities.dp(1.0f), this.outerPaint1);
        } else {
            float f4 = this.minProgress;
            if (f4 >= 0.0f) {
                float f5 = measuredWidth2 - f3;
                this.rect.set((f4 * f5) + f3, dp, i2 + f3, dp2);
                drawProgressBar(canvas, this.rect, this.outerPaint1);
                int alpha = this.outerPaint1.getAlpha();
                this.rect.set(f3, dp, (this.minProgress * f5) + f3, dp2);
                this.outerPaint1.setAlpha((int) (alpha * 0.5f));
                drawProgressBar(canvas, this.rect, this.outerPaint1);
                this.outerPaint1.setAlpha(alpha);
            } else {
                this.rect.set(f3, dp, i2 + f3, dp2);
                drawProgressBar(canvas, this.rect, this.outerPaint1);
            }
        }
        if (this.hoverDrawable != null) {
            int dp3 = ((this.selectorWidth / 2) + i2) - AndroidUtilities.dp(16.0f);
            int dp4 = ((this.thumbSize / 2) + measuredHeight2) - AndroidUtilities.dp(16.0f);
            this.hoverDrawable.setBounds(dp3, dp4, AndroidUtilities.dp(32.0f) + dp3, AndroidUtilities.dp(32.0f) + dp4);
            this.hoverDrawable.draw(canvas);
        }
        int dp5 = AndroidUtilities.dp(this.pressed ? 8.0f : 6.0f);
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.lastUpdateTime;
        if (elapsedRealtime > 18) {
            elapsedRealtime = 16;
        }
        float f6 = this.currentRadius;
        float f7 = dp5;
        if (f6 == f7) {
            z = false;
        } else if (f6 < f7) {
            float dp6 = f6 + (AndroidUtilities.dp(1.0f) * (elapsedRealtime / 60.0f));
            this.currentRadius = dp6;
        } else {
            float dp7 = f6 - (AndroidUtilities.dp(1.0f) * (elapsedRealtime / 60.0f));
            this.currentRadius = dp7;
        }
        float f8 = this.transitionProgress;
        if (f8 < 1.0f) {
            float f9 = f8 + (elapsedRealtime / 225.0f);
            this.transitionProgress = f9;
            if (f9 >= 1.0f) {
                this.transitionProgress = 1.0f;
            }
            f = this.transitionProgress;
            if (f >= 1.0f) {
                float interpolation = 1.0f - Easings.easeInQuad.getInterpolation(Math.min(1.0f, f * 3.0f));
                float interpolation2 = Easings.easeOutQuad.getInterpolation(this.transitionProgress);
                if (interpolation > 0.0f) {
                    canvas.drawCircle(this.transitionThumbX + (this.selectorWidth / 2), (this.thumbSize / 2) + measuredHeight2, this.currentRadius * interpolation, this.outerPaint1);
                }
                canvas.drawCircle(i2 + (this.selectorWidth / 2), measuredHeight2 + (this.thumbSize / 2), this.currentRadius * interpolation2, this.outerPaint1);
            } else {
                canvas.drawCircle(i2 + (this.selectorWidth / 2), measuredHeight2 + (this.thumbSize / 2), this.currentRadius, this.outerPaint1);
            }
            drawTimestampLabel(canvas);
            if (z2) {
                return;
            }
            postInvalidateOnAnimation();
            return;
        }
        z2 = z;
        f = this.transitionProgress;
        if (f >= 1.0f) {
        }
        drawTimestampLabel(canvas);
        if (z2) {
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return onTouch(motionEvent);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.progressToSet == -100.0f || getMeasuredWidth() <= 0) {
            return;
        }
        setProgress(this.progressToSet);
        this.progressToSet = -100.0f;
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0139  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    boolean onTouch(MotionEvent motionEvent) {
        Drawable drawable;
        int measuredWidth;
        int measuredWidth2;
        Drawable drawable2;
        float measuredWidth3;
        int measuredWidth4;
        Drawable drawable3;
        if (motionEvent.getAction() == 0) {
            this.sx = motionEvent.getX();
            this.sy = motionEvent.getY();
            return true;
        }
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.captured = false;
            if (motionEvent.getAction() == 1) {
                if (Math.abs(motionEvent.getY() - this.sy) < ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    int measuredHeight = (getMeasuredHeight() - this.thumbSize) / 2;
                    if (this.thumbX - measuredHeight > motionEvent.getX() || motionEvent.getX() > this.thumbX + this.thumbSize + measuredHeight) {
                        int x = ((int) motionEvent.getX()) - (this.thumbSize / 2);
                        this.thumbX = x;
                        if (x < minThumbX()) {
                            measuredWidth = minThumbX();
                        } else if (this.thumbX > getMeasuredWidth() - this.selectorWidth) {
                            measuredWidth = getMeasuredWidth() - this.selectorWidth;
                        }
                        this.thumbX = measuredWidth;
                    }
                    this.thumbDX = (int) (motionEvent.getX() - this.thumbX);
                    this.pressedDelayed = true;
                    this.pressed = true;
                }
            }
            if (this.pressed) {
                if (motionEvent.getAction() == 1) {
                    if (this.twoSided) {
                        float measuredWidth5 = (getMeasuredWidth() - this.selectorWidth) / 2;
                        float f = this.thumbX;
                        if (f >= measuredWidth5) {
                            setSeekBarDrag(false, (f - measuredWidth5) / measuredWidth5);
                        } else {
                            setSeekBarDrag(false, -Math.max(0.01f, 1.0f - ((measuredWidth5 - f) / measuredWidth5)));
                        }
                    } else {
                        setSeekBarDrag(true, this.thumbX / (getMeasuredWidth() - this.selectorWidth));
                    }
                }
                if (Build.VERSION.SDK_INT >= 21 && (drawable = this.hoverDrawable) != null) {
                    drawable.setState(StateSet.NOTHING);
                }
                this.delegate.onSeekBarPressed(false);
                this.pressed = false;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.SeekBarView$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        SeekBarView.this.lambda$onTouch$0();
                    }
                }, 50L);
                invalidate();
                return true;
            }
        } else if (motionEvent.getAction() == 2) {
            if (!this.captured) {
                ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
                if (Math.abs(motionEvent.getY() - this.sy) <= viewConfiguration.getScaledTouchSlop() && Math.abs(motionEvent.getX() - this.sx) > viewConfiguration.getScaledTouchSlop()) {
                    this.captured = true;
                    getParent().requestDisallowInterceptTouchEvent(true);
                    int measuredHeight2 = (getMeasuredHeight() - this.thumbSize) / 2;
                    if (motionEvent.getY() >= 0.0f && motionEvent.getY() <= getMeasuredHeight()) {
                        if (this.thumbX - measuredHeight2 > motionEvent.getX() || motionEvent.getX() > this.thumbX + this.thumbSize + measuredHeight2) {
                            int x2 = ((int) motionEvent.getX()) - (this.thumbSize / 2);
                            this.thumbX = x2;
                            if (x2 < minThumbX()) {
                                measuredWidth4 = minThumbX();
                            } else if (this.thumbX > getMeasuredWidth() - this.selectorWidth) {
                                measuredWidth4 = getMeasuredWidth() - this.selectorWidth;
                            }
                            this.thumbX = measuredWidth4;
                        }
                        this.thumbDX = (int) (motionEvent.getX() - this.thumbX);
                        this.pressedDelayed = true;
                        this.pressed = true;
                        this.delegate.onSeekBarPressed(true);
                        if (Build.VERSION.SDK_INT >= 21 && (drawable3 = this.hoverDrawable) != null) {
                            drawable3.setState(this.pressedState);
                            this.hoverDrawable.setHotspot(motionEvent.getX(), motionEvent.getY());
                        }
                        invalidate();
                        return true;
                    }
                }
            } else if (this.pressed) {
                int x3 = (int) (motionEvent.getX() - this.thumbDX);
                this.thumbX = x3;
                if (x3 >= minThumbX()) {
                    if (this.thumbX > getMeasuredWidth() - this.selectorWidth) {
                        measuredWidth2 = getMeasuredWidth() - this.selectorWidth;
                    }
                    if (this.reportChanges) {
                        if (this.twoSided) {
                            float measuredWidth6 = (getMeasuredWidth() - this.selectorWidth) / 2;
                            float f2 = this.thumbX;
                            if (f2 >= measuredWidth6) {
                                setSeekBarDrag(false, (f2 - measuredWidth6) / measuredWidth6);
                            } else {
                                measuredWidth3 = -Math.max(0.01f, 1.0f - ((measuredWidth6 - f2) / measuredWidth6));
                            }
                        } else {
                            measuredWidth3 = this.thumbX / (getMeasuredWidth() - this.selectorWidth);
                        }
                        setSeekBarDrag(false, measuredWidth3);
                    }
                    if (Build.VERSION.SDK_INT >= 21 && (drawable2 = this.hoverDrawable) != null) {
                        drawable2.setHotspot(motionEvent.getX(), motionEvent.getY());
                    }
                    invalidate();
                    return true;
                }
                measuredWidth2 = minThumbX();
                this.thumbX = measuredWidth2;
                if (this.reportChanges) {
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    drawable2.setHotspot(motionEvent.getX(), motionEvent.getY());
                }
                invalidate();
                return true;
            }
        }
        return false;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return onTouch(motionEvent);
    }

    public void setBufferedProgress(float f) {
        this.bufferedProgress = f;
        invalidate();
    }

    public void setDelegate(SeekBarViewDelegate seekBarViewDelegate) {
        this.delegate = seekBarViewDelegate;
    }

    public void setInnerColor(int i) {
        this.innerPaint1.setColor(i);
    }

    public void setLineWidth(int i) {
        this.lineWidthDp = i;
    }

    public void setMinProgress(float f) {
        this.minProgress = f;
        float progress = getProgress();
        float f2 = this.minProgress;
        if (progress < f2) {
            setProgress(f2, false);
        }
        invalidate();
    }

    public void setOuterColor(int i) {
        this.outerPaint1.setColor(i);
        Drawable drawable = this.hoverDrawable;
        if (drawable != null) {
            Theme.setSelectorDrawableColor(drawable, ColorUtils.setAlphaComponent(i, 40), true);
        }
    }

    public void setProgress(float f) {
        setProgress(f, false);
    }

    public void setProgress(float f, boolean z) {
        float measuredWidth;
        int measuredWidth2;
        if (getMeasuredWidth() == 0) {
            this.progressToSet = f;
            return;
        }
        this.progressToSet = -100.0f;
        if (this.twoSided) {
            float measuredWidth3 = (getMeasuredWidth() - this.selectorWidth) / 2;
            if (f < 0.0f) {
                f = -(f + 1.0f);
            }
            measuredWidth = measuredWidth3 + (f * measuredWidth3);
        } else {
            measuredWidth = (getMeasuredWidth() - this.selectorWidth) * f;
        }
        int ceil = (int) Math.ceil(measuredWidth);
        int i = this.thumbX;
        if (i != ceil) {
            if (z) {
                this.transitionThumbX = i;
                this.transitionProgress = 0.0f;
            }
            this.thumbX = ceil;
            if (ceil >= minThumbX()) {
                if (this.thumbX > getMeasuredWidth() - this.selectorWidth) {
                    measuredWidth2 = getMeasuredWidth() - this.selectorWidth;
                }
                invalidate();
            }
            measuredWidth2 = minThumbX();
            this.thumbX = measuredWidth2;
            invalidate();
        }
    }

    public void setReportChanges(boolean z) {
        this.reportChanges = z;
    }

    public void setSeparatorsCount(int i) {
        this.separatorsCount = i;
    }

    public void setTwoSided(boolean z) {
        this.twoSided = z;
    }

    public void updateTimestamps(MessageObject messageObject, Long l) {
        Integer parseInt;
        String str;
        if (messageObject == null) {
            clearTimestamps();
            return;
        }
        if (l == null) {
            l = Long.valueOf(((long) messageObject.getDuration()) * 1000);
        }
        if (l.longValue() < 0) {
            clearTimestamps();
            return;
        }
        CharSequence charSequence = messageObject.caption;
        if (messageObject.isYouTubeVideo()) {
            if (messageObject.youtubeDescription == null && (str = messageObject.messageOwner.media.webpage.description) != null) {
                messageObject.youtubeDescription = SpannableString.valueOf(str);
                MessageObject.addUrlsByPattern(messageObject.isOut(), messageObject.youtubeDescription, false, 3, (int) l.longValue(), false);
            }
            charSequence = messageObject.youtubeDescription;
        }
        if (charSequence == this.lastCaption && this.lastDuration == l.longValue()) {
            return;
        }
        this.lastCaption = charSequence;
        this.lastDuration = l.longValue() * 10;
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
                if (uRLSpanNoUnderline != null && uRLSpanNoUnderline.getURL() != null && uRLSpanNoUnderline.label != null && uRLSpanNoUnderline.getURL().startsWith("audio?") && (parseInt = Utilities.parseInt((CharSequence) uRLSpanNoUnderline.getURL().substring(6))) != null && parseInt.intValue() >= 0) {
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(uRLSpanNoUnderline.label);
                    Emoji.replaceEmoji(spannableStringBuilder, this.timestampLabelPaint.getFontMetricsInt(), false);
                    this.timestamps.add(new Pair(Float.valueOf((parseInt.intValue() * 1000) / l.longValue()), spannableStringBuilder));
                }
            }
            Collections.sort(this.timestamps, new Comparator() { // from class: org.telegram.ui.Components.SeekBarView$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$updateTimestamps$1;
                    lambda$updateTimestamps$1 = SeekBarView.lambda$updateTimestamps$1((Pair) obj, (Pair) obj2);
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

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.hoverDrawable;
    }
}
