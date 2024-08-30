package androidx.appcompat.widget;

import android.os.Build;
import android.view.View;
/* loaded from: classes.dex */
public abstract class TooltipCompat {

    /* loaded from: classes.dex */
    static class Api26Impl {
        static void setTooltipText(View view, CharSequence charSequence) {
            view.setTooltipText(charSequence);
        }
    }

    public static void setTooltipText(View view, CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setTooltipText(view, charSequence);
        } else {
            TooltipCompatHandler.setTooltipText(view, charSequence);
        }
    }
}
