package androidx.core.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import androidx.core.view.GravityCompat;

/* loaded from: classes.dex */
public abstract class RoundedBitmapDrawableFactory {

    /* loaded from: classes.dex */
    private static class DefaultRoundedBitmapDrawable extends RoundedBitmapDrawable {
        DefaultRoundedBitmapDrawable(Resources resources, Bitmap bitmap) {
            super(resources, bitmap);
        }

        @Override // androidx.core.graphics.drawable.RoundedBitmapDrawable
        void gravityCompatApply(int i, int i2, int i3, Rect rect, Rect rect2) {
            GravityCompat.apply(i, i2, i3, rect, rect2, 0);
        }
    }

    public static RoundedBitmapDrawable create(Resources resources, Bitmap bitmap) {
        return Build.VERSION.SDK_INT >= 21 ? new RoundedBitmapDrawable21(resources, bitmap) : new DefaultRoundedBitmapDrawable(resources, bitmap);
    }
}
