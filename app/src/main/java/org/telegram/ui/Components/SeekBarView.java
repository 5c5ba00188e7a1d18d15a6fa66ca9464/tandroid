package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class SeekBarView extends FrameLayout {
    private AnimatedFloat animatedThumbX;
    private float bufferedProgress;
    boolean captured;
    private float currentRadius;
    public SeekBarViewDelegate delegate;
    private Drawable hoverDrawable;
    private Paint innerPaint1;
    private long lastUpdateTime;
    private Paint outerPaint1;
    private boolean pressed;
    private int[] pressedState;
    private float progressToSet;
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
    private float transitionProgress;
    private int transitionThumbX;
    private boolean twoSided;

    /* loaded from: classes3.dex */
    public interface SeekBarViewDelegate {

        /* renamed from: org.telegram.ui.Components.SeekBarView$SeekBarViewDelegate$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
            public static CharSequence $default$getContentDescription(SeekBarViewDelegate seekBarViewDelegate) {
                return null;
            }

            public static int $default$getStepsCount(SeekBarViewDelegate seekBarViewDelegate) {
                return 0;
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
        this.animatedThumbX = new AnimatedFloat(this, 150L, CubicBezierInterpolator.DEFAULT);
        this.progressToSet = -100.0f;
        this.pressedState = new int[]{16842910, 16842919};
        this.transitionProgress = 1.0f;
        this.resourcesProvider = resourcesProvider;
        setWillNotDraw(false);
        this.innerPaint1 = new Paint(1);
        Paint paint = new Paint(1);
        this.outerPaint1 = paint;
        paint.setColor(getThemedColor("player_progress"));
        this.selectorWidth = AndroidUtilities.dp(32.0f);
        this.thumbSize = AndroidUtilities.dp(24.0f);
        this.currentRadius = AndroidUtilities.dp(6.0f);
        if (Build.VERSION.SDK_INT >= 21) {
            Drawable createSelectorDrawable = Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(getThemedColor("player_progress"), 40), 1, AndroidUtilities.dp(16.0f));
            this.hoverDrawable = createSelectorDrawable;
            createSelectorDrawable.setCallback(this);
            this.hoverDrawable.setVisible(true, false);
        }
        setImportantForAccessibility(1);
        FloatSeekBarAccessibilityDelegate floatSeekBarAccessibilityDelegate = new FloatSeekBarAccessibilityDelegate(z) { // from class: org.telegram.ui.Components.SeekBarView.1
            {
                SeekBarView.this = this;
            }

            @Override // org.telegram.ui.Components.FloatSeekBarAccessibilityDelegate
            public float getProgress() {
                return SeekBarView.this.getProgress();
            }

            @Override // org.telegram.ui.Components.FloatSeekBarAccessibilityDelegate
            public void setProgress(float f) {
                SeekBarView.this.pressed = true;
                SeekBarView.this.setProgress(f);
                SeekBarViewDelegate seekBarViewDelegate = SeekBarView.this.delegate;
                if (seekBarViewDelegate != null) {
                    seekBarViewDelegate.onSeekBarDrag(true, f);
                }
                SeekBarView.this.pressed = false;
            }

            @Override // org.telegram.ui.Components.FloatSeekBarAccessibilityDelegate
            public float getDelta() {
                int stepsCount = SeekBarView.this.delegate.getStepsCount();
                return stepsCount > 0 ? 1.0f / stepsCount : super.getDelta();
            }

            @Override // org.telegram.ui.Components.SeekBarAccessibilityDelegate
            public CharSequence getContentDescription(View view) {
                SeekBarViewDelegate seekBarViewDelegate = SeekBarView.this.delegate;
                if (seekBarViewDelegate != null) {
                    return seekBarViewDelegate.getContentDescription();
                }
                return null;
            }
        };
        this.seekBarAccessibilityDelegate = floatSeekBarAccessibilityDelegate;
        setAccessibilityDelegate(floatSeekBarAccessibilityDelegate);
    }

    public void setSeparatorsCount(int i) {
        this.separatorsCount = i;
    }

    public void setTwoSided(boolean z) {
        this.twoSided = z;
    }

    public boolean isTwoSided() {
        return this.twoSided;
    }

    public void setInnerColor(int i) {
        this.innerPaint1.setColor(i);
    }

    public void setOuterColor(int i) {
        this.outerPaint1.setColor(i);
        Drawable drawable = this.hoverDrawable;
        if (drawable != null) {
            Theme.setSelectorDrawableColor(drawable, ColorUtils.setAlphaComponent(i, 40), true);
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return onTouch(motionEvent);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return onTouch(motionEvent);
    }

    public void setReportChanges(boolean z) {
        this.reportChanges = z;
    }

    public void setDelegate(SeekBarViewDelegate seekBarViewDelegate) {
        this.delegate = seekBarViewDelegate;
    }

    public boolean onTouch(MotionEvent motionEvent) {
        Drawable drawable;
        Drawable drawable2;
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
                        if (x < 0) {
                            this.thumbX = 0;
                        } else if (x > getMeasuredWidth() - this.selectorWidth) {
                            this.thumbX = getMeasuredWidth() - this.selectorWidth;
                        }
                    }
                    this.thumbDX = (int) (motionEvent.getX() - this.thumbX);
                    this.pressed = true;
                }
            }
            if (this.pressed) {
                if (motionEvent.getAction() == 1) {
                    if (this.twoSided) {
                        float measuredWidth = (getMeasuredWidth() - this.selectorWidth) / 2;
                        int i = this.thumbX;
                        if (i >= measuredWidth) {
                            this.delegate.onSeekBarDrag(false, (i - measuredWidth) / measuredWidth);
                        } else {
                            this.delegate.onSeekBarDrag(false, -Math.max(0.01f, 1.0f - ((measuredWidth - i) / measuredWidth)));
                        }
                    } else {
                        this.delegate.onSeekBarDrag(true, this.thumbX / (getMeasuredWidth() - this.selectorWidth));
                    }
                }
                if (Build.VERSION.SDK_INT >= 21 && (drawable = this.hoverDrawable) != null) {
                    drawable.setState(StateSet.NOTHING);
                }
                this.delegate.onSeekBarPressed(false);
                this.pressed = false;
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
                            if (x2 < 0) {
                                this.thumbX = 0;
                            } else if (x2 > getMeasuredWidth() - this.selectorWidth) {
                                this.thumbX = getMeasuredWidth() - this.selectorWidth;
                            }
                        }
                        this.thumbDX = (int) (motionEvent.getX() - this.thumbX);
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
                if (x3 < 0) {
                    this.thumbX = 0;
                } else if (x3 > getMeasuredWidth() - this.selectorWidth) {
                    this.thumbX = getMeasuredWidth() - this.selectorWidth;
                }
                if (this.reportChanges) {
                    if (this.twoSided) {
                        float measuredWidth2 = (getMeasuredWidth() - this.selectorWidth) / 2;
                        int i2 = this.thumbX;
                        if (i2 >= measuredWidth2) {
                            this.delegate.onSeekBarDrag(false, (i2 - measuredWidth2) / measuredWidth2);
                        } else {
                            this.delegate.onSeekBarDrag(false, -Math.max(0.01f, 1.0f - ((measuredWidth2 - i2) / measuredWidth2)));
                        }
                    } else {
                        this.delegate.onSeekBarDrag(false, this.thumbX / (getMeasuredWidth() - this.selectorWidth));
                    }
                }
                if (Build.VERSION.SDK_INT >= 21 && (drawable2 = this.hoverDrawable) != null) {
                    drawable2.setHotspot(motionEvent.getX(), motionEvent.getY());
                }
                invalidate();
                return true;
            }
        }
        return false;
    }

    public float getProgress() {
        if (getMeasuredWidth() == 0) {
            return this.progressToSet;
        }
        return this.thumbX / (getMeasuredWidth() - this.selectorWidth);
    }

    public void setProgress(float f) {
        setProgress(f, false);
    }

    public void setProgress(float f, boolean z) {
        double d;
        if (getMeasuredWidth() == 0) {
            this.progressToSet = f;
            return;
        }
        this.progressToSet = -100.0f;
        if (this.twoSided) {
            float measuredWidth = (getMeasuredWidth() - this.selectorWidth) / 2;
            if (f < 0.0f) {
                d = Math.ceil(measuredWidth + ((-(f + 1.0f)) * measuredWidth));
            } else {
                d = Math.ceil(measuredWidth + (f * measuredWidth));
            }
        } else {
            d = Math.ceil((getMeasuredWidth() - this.selectorWidth) * f);
        }
        int i = (int) d;
        int i2 = this.thumbX;
        if (i2 == i) {
            return;
        }
        if (z) {
            this.transitionThumbX = i2;
            this.transitionProgress = 0.0f;
        }
        this.thumbX = i;
        if (i < 0) {
            this.thumbX = 0;
        } else if (i > getMeasuredWidth() - this.selectorWidth) {
            this.thumbX = getMeasuredWidth() - this.selectorWidth;
        }
        invalidate();
    }

    public void setBufferedProgress(float f) {
        this.bufferedProgress = f;
        invalidate();
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

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.hoverDrawable;
    }

    public boolean isDragging() {
        return this.pressed;
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x029a  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x02e2  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x02f7  */
    /* JADX WARN: Removed duplicated region for block: B:71:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float f;
        int i;
        float measuredWidth;
        int i2 = this.thumbX;
        boolean z = true;
        if (!this.twoSided && this.separatorsCount > 1) {
            i2 = (int) this.animatedThumbX.set(Math.round(i2 / measuredWidth) * ((getMeasuredWidth() - this.selectorWidth) / (this.separatorsCount - 1.0f)));
        }
        int i3 = i2;
        int measuredHeight = (getMeasuredHeight() - this.thumbSize) / 2;
        this.innerPaint1.setColor(getThemedColor("player_progressBackground"));
        canvas.drawRect(this.selectorWidth / 2, (getMeasuredHeight() / 2) - AndroidUtilities.dp(1.0f), getMeasuredWidth() - (this.selectorWidth / 2), (getMeasuredHeight() / 2) + AndroidUtilities.dp(1.0f), this.innerPaint1);
        boolean z2 = false;
        if (!this.twoSided && this.separatorsCount > 1) {
            for (int i4 = 0; i4 < this.separatorsCount; i4++) {
                canvas.drawCircle(AndroidUtilities.lerp(this.selectorWidth / 2, getMeasuredWidth() - (this.selectorWidth / 2), i4 / (this.separatorsCount - 1.0f)), getMeasuredHeight() / 2, AndroidUtilities.dp(1.6f), this.innerPaint1);
            }
        }
        if (this.bufferedProgress > 0.0f) {
            this.innerPaint1.setColor(getThemedColor("key_player_progressCachedBackground"));
            canvas.drawRect(this.selectorWidth / 2, (getMeasuredHeight() / 2) - AndroidUtilities.dp(1.0f), (this.bufferedProgress * (getMeasuredWidth() - this.selectorWidth)) + (this.selectorWidth / 2), (getMeasuredHeight() / 2) + AndroidUtilities.dp(1.0f), this.innerPaint1);
        }
        float f2 = 6.0f;
        if (this.twoSided) {
            canvas.drawRect((getMeasuredWidth() / 2) - AndroidUtilities.dp(1.0f), (getMeasuredHeight() / 2) - AndroidUtilities.dp(6.0f), (getMeasuredWidth() / 2) + AndroidUtilities.dp(1.0f), (getMeasuredHeight() / 2) + AndroidUtilities.dp(6.0f), this.outerPaint1);
            if (i3 > (getMeasuredWidth() - this.selectorWidth) / 2) {
                canvas.drawRect(getMeasuredWidth() / 2, (getMeasuredHeight() / 2) - AndroidUtilities.dp(1.0f), (this.selectorWidth / 2) + i3, (getMeasuredHeight() / 2) + AndroidUtilities.dp(1.0f), this.outerPaint1);
            } else {
                canvas.drawRect((i / 2) + i3, (getMeasuredHeight() / 2) - AndroidUtilities.dp(1.0f), getMeasuredWidth() / 2, (getMeasuredHeight() / 2) + AndroidUtilities.dp(1.0f), this.outerPaint1);
            }
        } else {
            canvas.drawRect(this.selectorWidth / 2, (getMeasuredHeight() / 2) - AndroidUtilities.dp(1.0f), (this.selectorWidth / 2) + i3, (getMeasuredHeight() / 2) + AndroidUtilities.dp(1.0f), this.outerPaint1);
            if (this.separatorsCount > 1) {
                for (int i5 = 0; i5 < this.separatorsCount; i5++) {
                    float lerp = AndroidUtilities.lerp(this.selectorWidth / 2, getMeasuredWidth() - (this.selectorWidth / 2), i5 / (this.separatorsCount - 1.0f));
                    if (lerp > (this.selectorWidth / 2) + i3) {
                        break;
                    }
                    canvas.drawCircle(lerp, getMeasuredHeight() / 2, AndroidUtilities.dp(1.4f), this.outerPaint1);
                }
            }
        }
        if (this.hoverDrawable != null) {
            int dp = ((this.selectorWidth / 2) + i3) - AndroidUtilities.dp(16.0f);
            int dp2 = ((this.thumbSize / 2) + measuredHeight) - AndroidUtilities.dp(16.0f);
            this.hoverDrawable.setBounds(dp, dp2, AndroidUtilities.dp(32.0f) + dp, AndroidUtilities.dp(32.0f) + dp2);
            this.hoverDrawable.draw(canvas);
        }
        if (this.pressed) {
            f2 = 8.0f;
        }
        int dp3 = AndroidUtilities.dp(f2);
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.lastUpdateTime;
        if (elapsedRealtime > 18) {
            elapsedRealtime = 16;
        }
        float f3 = this.currentRadius;
        float f4 = dp3;
        if (f3 != f4) {
            if (f3 < f4) {
                float dp4 = f3 + (AndroidUtilities.dp(1.0f) * (((float) elapsedRealtime) / 60.0f));
                this.currentRadius = dp4;
                if (dp4 > f4) {
                    this.currentRadius = f4;
                }
            } else {
                float dp5 = f3 - (AndroidUtilities.dp(1.0f) * (((float) elapsedRealtime) / 60.0f));
                this.currentRadius = dp5;
                if (dp5 < f4) {
                    this.currentRadius = f4;
                }
            }
            z2 = true;
        }
        float f5 = this.transitionProgress;
        if (f5 < 1.0f) {
            float f6 = f5 + (((float) elapsedRealtime) / 225.0f);
            this.transitionProgress = f6;
            if (f6 >= 1.0f) {
                this.transitionProgress = 1.0f;
            }
            f = this.transitionProgress;
            if (f >= 1.0f) {
                float interpolation = 1.0f - Easings.easeInQuad.getInterpolation(Math.min(1.0f, f * 3.0f));
                float interpolation2 = Easings.easeOutQuad.getInterpolation(this.transitionProgress);
                if (interpolation > 0.0f) {
                    canvas.drawCircle(this.transitionThumbX + (this.selectorWidth / 2), (this.thumbSize / 2) + measuredHeight, this.currentRadius * interpolation, this.outerPaint1);
                }
                canvas.drawCircle(i3 + (this.selectorWidth / 2), measuredHeight + (this.thumbSize / 2), this.currentRadius * interpolation2, this.outerPaint1);
            } else {
                canvas.drawCircle(i3 + (this.selectorWidth / 2), measuredHeight + (this.thumbSize / 2), this.currentRadius, this.outerPaint1);
            }
            if (z) {
                return;
            }
            postInvalidateOnAnimation();
            return;
        }
        z = z2;
        f = this.transitionProgress;
        if (f >= 1.0f) {
        }
        if (z) {
        }
    }

    public SeekBarAccessibilityDelegate getSeekBarAccessibilityDelegate() {
        return this.seekBarAccessibilityDelegate;
    }

    private int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }
}
