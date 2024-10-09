package org.telegram.ui.Components;

import android.content.Context;
import android.content.res.Resources;
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

    public ShareLocationDrawable(Context context, int i) {
        Resources resources;
        int i2;
        Resources resources2;
        int i3;
        this.currentType = i;
        if (i == 4) {
            resources2 = context.getResources();
            i3 = R.drawable.filled_extend_location;
        } else if (i == 5) {
            resources2 = context.getResources();
            i3 = R.drawable.filled_stop_location;
        } else {
            if (i != 1) {
                this.drawable = context.getResources().getDrawable(R.drawable.animationpin).mutate();
                this.drawableLeft = context.getResources().getDrawable(R.drawable.animationpinleft).mutate();
                resources = context.getResources();
                i2 = R.drawable.animationpinright;
                this.drawableRight = resources.getDrawable(i2).mutate();
            }
            resources2 = context.getResources();
            i3 = R.drawable.smallanimationpin;
        }
        this.drawable = resources2.getDrawable(i3).mutate();
        this.drawableLeft = context.getResources().getDrawable(R.drawable.smallanimationpinleft).mutate();
        resources = context.getResources();
        i2 = R.drawable.smallanimationpinright;
        this.drawableRight = resources.getDrawable(i2).mutate();
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

    /* JADX WARN: Removed duplicated region for block: B:20:0x016c  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x016e  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        int dp;
        int dp2;
        int dp3;
        float f;
        int dp4;
        int i;
        int dp5;
        int i2;
        int dp6;
        int i3;
        int dp7;
        int intrinsicWidth = this.drawable.getIntrinsicWidth();
        int intrinsicHeight = this.drawable.getIntrinsicHeight();
        int i4 = this.currentType;
        int i5 = 3;
        int i6 = 5;
        int i7 = 1;
        int dp8 = AndroidUtilities.dp((i4 == 4 || i4 == 5) ? 24.0f : i4 == 3 ? 44.0f : i4 == 2 ? 32.0f : i4 == 1 ? 30.0f : 120.0f);
        int intrinsicHeight2 = getBounds().top + ((getIntrinsicHeight() - dp8) / 2);
        int intrinsicWidth2 = getBounds().left + ((getIntrinsicWidth() - dp8) / 2);
        int i8 = intrinsicWidth + intrinsicWidth2;
        this.drawable.setBounds(intrinsicWidth2, intrinsicHeight2, i8, intrinsicHeight2 + intrinsicHeight);
        this.drawable.draw(canvas);
        int i9 = 0;
        while (i9 < 2) {
            float f2 = this.progress[i9];
            if (f2 >= 0.0f) {
                float f3 = (f2 * 0.5f) + 0.5f;
                int i10 = this.currentType;
                if (i10 == 4 || i10 == i6) {
                    dp = AndroidUtilities.dp(2.5f * f3);
                    dp2 = AndroidUtilities.dp(f3 * 6.5f);
                    dp3 = AndroidUtilities.dp(this.progress[i9] * 6.0f);
                    f = 3.0f;
                    dp4 = (intrinsicWidth2 + AndroidUtilities.dp(3.0f)) - dp3;
                    i = intrinsicHeight2 + (intrinsicHeight / 2);
                    dp5 = AndroidUtilities.dp(2.0f);
                } else {
                    if (i10 == i5) {
                        dp = AndroidUtilities.dp(5.0f * f3);
                        dp2 = AndroidUtilities.dp(f3 * 18.0f);
                        dp7 = AndroidUtilities.dp(this.progress[i9] * 15.0f);
                        dp4 = (intrinsicWidth2 + AndroidUtilities.dp(2.0f)) - dp7;
                        i2 = (intrinsicHeight2 + (intrinsicHeight / 2)) - AndroidUtilities.dp(7.0f);
                    } else if (i10 == 2) {
                        dp = AndroidUtilities.dp(5.0f * f3);
                        dp2 = AndroidUtilities.dp(f3 * 18.0f);
                        dp7 = AndroidUtilities.dp(this.progress[i9] * 15.0f);
                        dp4 = (intrinsicWidth2 + AndroidUtilities.dp(2.0f)) - dp7;
                        i2 = intrinsicHeight2 + (intrinsicHeight / 2);
                    } else if (i10 == i7) {
                        dp = AndroidUtilities.dp(2.5f * f3);
                        dp2 = AndroidUtilities.dp(f3 * 6.5f);
                        dp3 = AndroidUtilities.dp(this.progress[i9] * 6.0f);
                        dp4 = (AndroidUtilities.dp(7.0f) + intrinsicWidth2) - dp3;
                        i2 = intrinsicHeight2 + (intrinsicHeight / 2);
                        dp6 = AndroidUtilities.dp(7.0f);
                        i3 = (i8 - dp6) + dp3;
                        float f4 = this.progress[i9];
                        int i11 = (int) ((f4 < 0.5f ? f4 / 0.5f : 1.0f - ((f4 - 0.5f) / 0.5f)) * 255.0f);
                        this.drawableLeft.setAlpha(i11);
                        int i12 = i2 - dp2;
                        int i13 = i2 + dp2;
                        this.drawableLeft.setBounds(dp4 - dp, i12, dp4 + dp, i13);
                        this.drawableLeft.draw(canvas);
                        this.drawableRight.setAlpha(i11);
                        this.drawableRight.setBounds(i3 - dp, i12, i3 + dp, i13);
                        this.drawableRight.draw(canvas);
                    } else {
                        dp = AndroidUtilities.dp(5.0f * f3);
                        dp2 = AndroidUtilities.dp(f3 * 18.0f);
                        dp3 = AndroidUtilities.dp(this.progress[i9] * 15.0f);
                        f = 42.0f;
                        dp4 = (intrinsicWidth2 + AndroidUtilities.dp(42.0f)) - dp3;
                        i = intrinsicHeight2 + (intrinsicHeight / 2);
                        dp5 = AndroidUtilities.dp(7.0f);
                    }
                    i3 = (i8 - AndroidUtilities.dp(2.0f)) + dp7;
                    float f42 = this.progress[i9];
                    int i112 = (int) ((f42 < 0.5f ? f42 / 0.5f : 1.0f - ((f42 - 0.5f) / 0.5f)) * 255.0f);
                    this.drawableLeft.setAlpha(i112);
                    int i122 = i2 - dp2;
                    int i132 = i2 + dp2;
                    this.drawableLeft.setBounds(dp4 - dp, i122, dp4 + dp, i132);
                    this.drawableLeft.draw(canvas);
                    this.drawableRight.setAlpha(i112);
                    this.drawableRight.setBounds(i3 - dp, i122, i3 + dp, i132);
                    this.drawableRight.draw(canvas);
                }
                i2 = i - dp5;
                dp6 = AndroidUtilities.dp(f);
                i3 = (i8 - dp6) + dp3;
                float f422 = this.progress[i9];
                int i1122 = (int) ((f422 < 0.5f ? f422 / 0.5f : 1.0f - ((f422 - 0.5f) / 0.5f)) * 255.0f);
                this.drawableLeft.setAlpha(i1122);
                int i1222 = i2 - dp2;
                int i1322 = i2 + dp2;
                this.drawableLeft.setBounds(dp4 - dp, i1222, dp4 + dp, i1322);
                this.drawableLeft.draw(canvas);
                this.drawableRight.setAlpha(i1122);
                this.drawableRight.setBounds(i3 - dp, i1222, i3 + dp, i1322);
                this.drawableRight.draw(canvas);
            }
            i9++;
            i5 = 3;
            i6 = 5;
            i7 = 1;
        }
        update();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        int i = this.currentType;
        return AndroidUtilities.dp((i == 4 || i == 5) ? 42.0f : i == 3 ? 100.0f : i == 2 ? 74.0f : i == 1 ? 40.0f : 180.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        int i = this.currentType;
        return AndroidUtilities.dp((i == 4 || i == 5) ? 42.0f : i == 3 ? 100.0f : i == 2 ? 74.0f : i == 1 ? 40.0f : 120.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.drawable.setColorFilter(colorFilter);
        this.drawableLeft.setColorFilter(colorFilter);
        this.drawableRight.setColorFilter(colorFilter);
    }
}
