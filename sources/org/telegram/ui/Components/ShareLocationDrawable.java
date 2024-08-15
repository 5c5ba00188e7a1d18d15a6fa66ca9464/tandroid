package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
/* loaded from: classes3.dex */
public class ShareLocationDrawable extends Drawable {
    private int currentType;
    private Drawable drawable;
    private Drawable drawableLeft;
    private Drawable drawableRight;
    private long lastUpdateTime = 0;
    private float[] progress = {0.0f, -0.5f};

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    public ShareLocationDrawable(Context context, int i) {
        this.currentType = i;
        if (i == 4) {
            this.drawable = context.getResources().getDrawable(R.drawable.filled_extend_location).mutate();
            this.drawableLeft = context.getResources().getDrawable(R.drawable.smallanimationpinleft).mutate();
            this.drawableRight = context.getResources().getDrawable(R.drawable.smallanimationpinright).mutate();
        } else if (i == 5) {
            this.drawable = context.getResources().getDrawable(R.drawable.filled_stop_location).mutate();
            this.drawableLeft = context.getResources().getDrawable(R.drawable.smallanimationpinleft).mutate();
            this.drawableRight = context.getResources().getDrawable(R.drawable.smallanimationpinright).mutate();
        } else if (i == 3) {
            this.drawable = context.getResources().getDrawable(R.drawable.nearby_l).mutate();
            this.drawableLeft = context.getResources().getDrawable(R.drawable.animationpinleft).mutate();
            this.drawableRight = context.getResources().getDrawable(R.drawable.animationpinright).mutate();
        } else if (i == 2) {
            this.drawable = context.getResources().getDrawable(R.drawable.nearby_m).mutate();
            this.drawableLeft = context.getResources().getDrawable(R.drawable.animationpinleft).mutate();
            this.drawableRight = context.getResources().getDrawable(R.drawable.animationpinright).mutate();
        } else if (i == 1) {
            this.drawable = context.getResources().getDrawable(R.drawable.smallanimationpin).mutate();
            this.drawableLeft = context.getResources().getDrawable(R.drawable.smallanimationpinleft).mutate();
            this.drawableRight = context.getResources().getDrawable(R.drawable.smallanimationpinright).mutate();
        } else {
            this.drawable = context.getResources().getDrawable(R.drawable.animationpin).mutate();
            this.drawableLeft = context.getResources().getDrawable(R.drawable.animationpinleft).mutate();
            this.drawableRight = context.getResources().getDrawable(R.drawable.animationpinright).mutate();
        }
    }

    private void update() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        if (j > 16) {
            j = 16;
        }
        for (int i = 0; i < 2; i++) {
            float[] fArr = this.progress;
            if (fArr[i] >= 1.0f) {
                fArr[i] = 0.0f;
            }
            float f = fArr[i] + (((float) j) / 1300.0f);
            fArr[i] = f;
            if (f > 1.0f) {
                fArr[i] = 1.0f;
            }
        }
        invalidateSelf();
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0186  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        int dp;
        int dp2;
        int dp3;
        int dp4;
        int dp5;
        int dp6;
        int dp7;
        int i;
        int dp8;
        int dp9;
        int intrinsicWidth = this.drawable.getIntrinsicWidth();
        int intrinsicHeight = this.drawable.getIntrinsicHeight();
        int i2 = this.currentType;
        int i3 = 3;
        int i4 = 5;
        int i5 = 1;
        if (i2 == 4 || i2 == 5) {
            dp = AndroidUtilities.dp(24.0f);
        } else if (i2 == 3) {
            dp = AndroidUtilities.dp(44.0f);
        } else if (i2 == 2) {
            dp = AndroidUtilities.dp(32.0f);
        } else if (i2 == 1) {
            dp = AndroidUtilities.dp(30.0f);
        } else {
            dp = AndroidUtilities.dp(120.0f);
        }
        int intrinsicHeight2 = getBounds().top + ((getIntrinsicHeight() - dp) / 2);
        int intrinsicWidth2 = getBounds().left + ((getIntrinsicWidth() - dp) / 2);
        int i6 = intrinsicWidth + intrinsicWidth2;
        this.drawable.setBounds(intrinsicWidth2, intrinsicHeight2, i6, intrinsicHeight2 + intrinsicHeight);
        this.drawable.draw(canvas);
        int i7 = 0;
        while (i7 < 2) {
            float f = this.progress[i7];
            if (f >= 0.0f) {
                float f2 = (f * 0.5f) + 0.5f;
                int i8 = this.currentType;
                if (i8 == 4 || i8 == i4) {
                    dp2 = AndroidUtilities.dp(2.5f * f2);
                    dp3 = AndroidUtilities.dp(f2 * 6.5f);
                    dp4 = AndroidUtilities.dp(this.progress[i7] * 6.0f);
                    dp5 = (intrinsicWidth2 + AndroidUtilities.dp(3.0f)) - dp4;
                    dp6 = (intrinsicHeight2 + (intrinsicHeight / 2)) - AndroidUtilities.dp(2.0f);
                    dp7 = AndroidUtilities.dp(3.0f);
                } else {
                    if (i8 == i3) {
                        dp2 = AndroidUtilities.dp(5.0f * f2);
                        dp3 = AndroidUtilities.dp(f2 * 18.0f);
                        dp8 = AndroidUtilities.dp(this.progress[i7] * 15.0f);
                        dp5 = (intrinsicWidth2 + AndroidUtilities.dp(2.0f)) - dp8;
                        dp6 = (intrinsicHeight2 + (intrinsicHeight / 2)) - AndroidUtilities.dp(7.0f);
                        dp9 = AndroidUtilities.dp(2.0f);
                    } else if (i8 == 2) {
                        dp2 = AndroidUtilities.dp(5.0f * f2);
                        dp3 = AndroidUtilities.dp(f2 * 18.0f);
                        dp8 = AndroidUtilities.dp(this.progress[i7] * 15.0f);
                        dp5 = (intrinsicWidth2 + AndroidUtilities.dp(2.0f)) - dp8;
                        dp6 = intrinsicHeight2 + (intrinsicHeight / 2);
                        dp9 = AndroidUtilities.dp(2.0f);
                    } else if (i8 == i5) {
                        dp2 = AndroidUtilities.dp(2.5f * f2);
                        dp3 = AndroidUtilities.dp(f2 * 6.5f);
                        dp4 = AndroidUtilities.dp(this.progress[i7] * 6.0f);
                        dp5 = (AndroidUtilities.dp(7.0f) + intrinsicWidth2) - dp4;
                        dp6 = intrinsicHeight2 + (intrinsicHeight / 2);
                        dp7 = AndroidUtilities.dp(7.0f);
                    } else {
                        dp2 = AndroidUtilities.dp(5.0f * f2);
                        dp3 = AndroidUtilities.dp(f2 * 18.0f);
                        dp4 = AndroidUtilities.dp(this.progress[i7] * 15.0f);
                        dp5 = (intrinsicWidth2 + AndroidUtilities.dp(42.0f)) - dp4;
                        dp6 = (intrinsicHeight2 + (intrinsicHeight / 2)) - AndroidUtilities.dp(7.0f);
                        dp7 = AndroidUtilities.dp(42.0f);
                    }
                    i = (i6 - dp9) + dp8;
                    float f3 = this.progress[i7];
                    int i9 = (int) ((f3 >= 0.5f ? f3 / 0.5f : 1.0f - ((f3 - 0.5f) / 0.5f)) * 255.0f);
                    this.drawableLeft.setAlpha(i9);
                    int i10 = dp6 - dp3;
                    int i11 = dp6 + dp3;
                    this.drawableLeft.setBounds(dp5 - dp2, i10, dp5 + dp2, i11);
                    this.drawableLeft.draw(canvas);
                    this.drawableRight.setAlpha(i9);
                    this.drawableRight.setBounds(i - dp2, i10, i + dp2, i11);
                    this.drawableRight.draw(canvas);
                }
                i = (i6 - dp7) + dp4;
                float f32 = this.progress[i7];
                int i92 = (int) ((f32 >= 0.5f ? f32 / 0.5f : 1.0f - ((f32 - 0.5f) / 0.5f)) * 255.0f);
                this.drawableLeft.setAlpha(i92);
                int i102 = dp6 - dp3;
                int i112 = dp6 + dp3;
                this.drawableLeft.setBounds(dp5 - dp2, i102, dp5 + dp2, i112);
                this.drawableLeft.draw(canvas);
                this.drawableRight.setAlpha(i92);
                this.drawableRight.setBounds(i - dp2, i102, i + dp2, i112);
                this.drawableRight.draw(canvas);
            }
            i7++;
            i3 = 3;
            i4 = 5;
            i5 = 1;
        }
        update();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.drawable.setColorFilter(colorFilter);
        this.drawableLeft.setColorFilter(colorFilter);
        this.drawableRight.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        int i = this.currentType;
        if (i == 4 || i == 5) {
            return AndroidUtilities.dp(42.0f);
        }
        if (i == 3) {
            return AndroidUtilities.dp(100.0f);
        }
        if (i == 2) {
            return AndroidUtilities.dp(74.0f);
        }
        if (i == 1) {
            return AndroidUtilities.dp(40.0f);
        }
        return AndroidUtilities.dp(120.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        int i = this.currentType;
        if (i == 4 || i == 5) {
            return AndroidUtilities.dp(42.0f);
        }
        if (i == 3) {
            return AndroidUtilities.dp(100.0f);
        }
        if (i == 2) {
            return AndroidUtilities.dp(74.0f);
        }
        if (i == 1) {
            return AndroidUtilities.dp(40.0f);
        }
        return AndroidUtilities.dp(180.0f);
    }
}
