package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.DialogCell;
/* loaded from: classes3.dex */
public class CheckDrawable {
    private static int color;
    private static Drawable drawable;
    private static float lastDensity;
    private static CharSequence spanned;

    public static CharSequence getSpanned(Context context) {
        if (spanned != null && drawable != null && lastDensity == AndroidUtilities.density) {
            int color2 = Theme.getColor("windowBackgroundWhiteGrayText");
            if (color2 != color) {
                Drawable drawable2 = drawable;
                color = color2;
                drawable2.setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.SRC_IN));
            }
            return spanned;
        } else if (context == null) {
            return null;
        } else {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("v ");
            lastDensity = AndroidUtilities.density;
            Drawable mutate = context.getResources().getDrawable(R.drawable.msg_mini_checks).mutate();
            drawable = mutate;
            int color3 = Theme.getColor("windowBackgroundWhiteGrayText");
            color = color3;
            mutate.setColorFilter(new PorterDuffColorFilter(color3, PorterDuff.Mode.SRC_IN));
            drawable.setBounds(0, AndroidUtilities.dp(4.66f), drawable.getIntrinsicWidth(), AndroidUtilities.dp(4.66f) + drawable.getIntrinsicHeight());
            spannableStringBuilder.setSpan(new ImageSpan(drawable, 2), 0, 1, 33);
            spannableStringBuilder.setSpan(new DialogCell.FixedWidthSpan(AndroidUtilities.dp(2.0f)), 1, 2, 33);
            spanned = spannableStringBuilder;
            return spannableStringBuilder;
        }
    }
}
