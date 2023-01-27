package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
/* loaded from: classes3.dex */
public class DialogsPremiumHintCell extends FrameLayout {
    private ImageView chevronView;
    private TextView messageView;
    private TextView titleView;

    public DialogsPremiumHintCell(Context context) {
        super(context);
        setWillNotDraw(false);
        setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
        TextView textView = new TextView(context);
        this.titleView = textView;
        textView.setTextSize(1, 15.0f);
        this.titleView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.titleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.titleView.setSingleLine();
        TextView textView2 = this.titleView;
        boolean z = LocaleController.isRTL;
        addView(textView2, LayoutHelper.createFrame(-2, -2.0f, z ? 5 : 3, z ? 24.0f : 0.0f, 0.0f, z ? 0.0f : 24.0f, 0.0f));
        TextView textView3 = new TextView(context);
        this.messageView = textView3;
        textView3.setTextSize(1, 14.0f);
        this.messageView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
        this.messageView.setMaxLines(2);
        this.messageView.setEllipsize(TextUtils.TruncateAt.END);
        TextView textView4 = this.messageView;
        boolean z2 = LocaleController.isRTL;
        addView(textView4, LayoutHelper.createFrame(-2, -2.0f, (z2 ? 5 : 3) | 80, z2 ? 24.0f : 0.0f, 0.0f, z2 ? 0.0f : 24.0f, 0.0f));
        ImageView imageView = new ImageView(context);
        this.chevronView = imageView;
        imageView.setImageResource(R.drawable.arrow_newchat);
        this.chevronView.setColorFilter(Theme.getColor("windowBackgroundWhiteGrayText"), PorterDuff.Mode.SRC_IN);
        addView(this.chevronView, LayoutHelper.createFrame(16, 16, (LocaleController.isRTL ? 3 : 5) | 16));
        setBackground(Theme.AdaptiveRipple.filledRect());
    }

    public void setText(CharSequence charSequence, CharSequence charSequence2) {
        this.titleView.setText(charSequence);
        this.messageView.setText(charSequence2);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, getHeight() - 1, getWidth(), getHeight() - 1, Theme.dividerPaint);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(getStaticHeight(), 1073741824));
    }

    public static int getStaticHeight() {
        return AndroidUtilities.dp(72.0f) + 1;
    }
}
