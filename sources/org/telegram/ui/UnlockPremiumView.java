package org.telegram.ui;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.PremiumButtonView;

/* loaded from: classes4.dex */
public class UnlockPremiumView extends FrameLayout {
    public final PremiumButtonView premiumButtonView;

    /* JADX WARN: Removed duplicated region for block: B:12:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public UnlockPremiumView(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        int i2;
        LinearLayout linearLayout = new LinearLayout(context);
        addView(linearLayout, LayoutHelper.createFrame(-1, -2, 80));
        linearLayout.setOrientation(1);
        TextView textView = new TextView(context);
        textView.setTextColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider), 100));
        textView.setTextSize(1, 13.0f);
        textView.setGravity(17);
        if (i != 0) {
            i2 = i == 1 ? R.string.UnlockPremiumReactionsDescription : i2;
            linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 0, 16, 17, 17, 16));
            PremiumButtonView premiumButtonView = new PremiumButtonView(context, false, resourcesProvider);
            this.premiumButtonView = premiumButtonView;
            String string = LocaleController.getString(i != 0 ? R.string.UnlockPremiumStickers : R.string.UnlockPremiumReactions);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append((CharSequence) "d ").setSpan(new ColoredImageSpan(ContextCompat.getDrawable(context, R.drawable.msg_premium_normal)), 0, 1, 0);
            spannableStringBuilder.append((CharSequence) string);
            premiumButtonView.buttonTextView.setText(spannableStringBuilder);
            linearLayout.addView(premiumButtonView, LayoutHelper.createLinear(-1, 48, 0, 16, 0, 16, 16));
        }
        i2 = R.string.UnlockPremiumStickersDescription;
        textView.setText(LocaleController.getString(i2));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 0, 16, 17, 17, 16));
        PremiumButtonView premiumButtonView2 = new PremiumButtonView(context, false, resourcesProvider);
        this.premiumButtonView = premiumButtonView2;
        String string2 = LocaleController.getString(i != 0 ? R.string.UnlockPremiumStickers : R.string.UnlockPremiumReactions);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
        spannableStringBuilder2.append((CharSequence) "d ").setSpan(new ColoredImageSpan(ContextCompat.getDrawable(context, R.drawable.msg_premium_normal)), 0, 1, 0);
        spannableStringBuilder2.append((CharSequence) string2);
        premiumButtonView2.buttonTextView.setText(spannableStringBuilder2);
        linearLayout.addView(premiumButtonView2, LayoutHelper.createLinear(-1, 48, 0, 16, 0, 16, 16));
    }
}
