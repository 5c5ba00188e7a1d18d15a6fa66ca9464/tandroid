package org.telegram.ui.Business;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Business.QuickRepliesController;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Stories.recorder.HintView2;

/* loaded from: classes4.dex */
public class QuickRepliesEmptyView extends LinearLayout {
    private TextView descriptionView;
    private TextView descriptionView2;
    public RLottieImageView imageView;
    private final Theme.ResourcesProvider resourcesProvider;
    private TextView titleView;

    private class DotTextView extends TextView {
        public DotTextView(Context context) {
            super(context);
        }

        @Override // android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (getPaddingLeft() > 0) {
                canvas.drawCircle((getPaddingLeft() - AndroidUtilities.dp(2.5f)) / 2.0f, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(2.5f), getPaint());
            }
            super.dispatchDraw(canvas);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0192  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x018d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public QuickRepliesEmptyView(Context context, int i, long j, long j2, String str, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        int i2;
        TextView textView;
        int i3;
        TextView textView2;
        setOrientation(1);
        this.resourcesProvider = resourcesProvider;
        TextView textView3 = new TextView(context);
        this.titleView = textView3;
        textView3.setTextSize(1, 14.0f);
        this.titleView.setTypeface(AndroidUtilities.bold());
        this.titleView.setTextAlignment(4);
        this.titleView.setGravity(17);
        DotTextView dotTextView = new DotTextView(context);
        this.descriptionView = dotTextView;
        dotTextView.setTextAlignment(4);
        this.descriptionView.setGravity(17);
        this.descriptionView.setTextSize(1, 13.0f);
        this.descriptionView.setGravity(1);
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.imageView = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.imageView.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
        this.descriptionView.setMaxWidth(AndroidUtilities.dp(160.0f));
        if ("hello".equalsIgnoreCase(str)) {
            this.imageView.setImageResource(R.drawable.large_greeting);
            this.titleView.setText(LocaleController.getString(R.string.BusinessGreetingIntroTitle));
            textView = this.descriptionView;
            i3 = R.string.BusinessGreetingIntro;
        } else {
            if (!"away".equalsIgnoreCase(str)) {
                if (i == 5) {
                    this.imageView.setImageResource(R.drawable.large_quickreplies);
                    QuickRepliesController.QuickReply findReply = QuickRepliesController.getInstance(UserConfig.selectedAccount).findReply(j2);
                    str = findReply != null ? findReply.name : str;
                    this.titleView.setText(LocaleController.getString(R.string.BusinessRepliesIntroTitle));
                    this.descriptionView.setMaxWidth(AndroidUtilities.dp(208.0f));
                    this.descriptionView.setTextAlignment(2);
                    this.descriptionView.setGravity(3);
                    this.descriptionView.setText(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.BusinessRepliesIntro1, str)));
                    this.descriptionView.setPadding(AndroidUtilities.dp(28.0f), 0, 0, 0);
                    DotTextView dotTextView2 = new DotTextView(context);
                    this.descriptionView2 = dotTextView2;
                    dotTextView2.setMaxWidth(AndroidUtilities.dp(208.0f));
                    this.descriptionView2.setTextAlignment(2);
                    this.descriptionView2.setGravity(3);
                    this.descriptionView2.setTextSize(1, 13.0f);
                    this.descriptionView2.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.BusinessRepliesIntro2)));
                    this.descriptionView2.setPadding(AndroidUtilities.dp(28.0f), 0, 0, 0);
                }
                i2 = 12;
                addView(this.imageView, LayoutHelper.createLinear(78, 78, 49, 20, 17, 20, 9));
                addView(this.titleView, LayoutHelper.createLinear(-2, -2, 49, 20, 0, 20, 9));
                addView(this.descriptionView, LayoutHelper.createLinear(-2, -2, 49, i2, 0, i2, this.descriptionView2 == null ? 9 : 19));
                textView2 = this.descriptionView2;
                if (textView2 != null) {
                    addView(textView2, LayoutHelper.createLinear(-2, -2, 49, 12, 0, 12, 19));
                }
                updateColors();
            }
            this.imageView.setImageResource(R.drawable.large_away);
            this.titleView.setText(LocaleController.getString(R.string.BusinessAwayIntroTitle));
            textView = this.descriptionView;
            i3 = R.string.BusinessAwayIntro;
        }
        textView.setText(LocaleController.getString(i3));
        this.descriptionView.setMaxWidth(Math.min(AndroidUtilities.dp(160.0f), HintView2.cutInFancyHalf(this.descriptionView.getText(), this.descriptionView.getPaint())));
        i2 = 22;
        addView(this.imageView, LayoutHelper.createLinear(78, 78, 49, 20, 17, 20, 9));
        addView(this.titleView, LayoutHelper.createLinear(-2, -2, 49, 20, 0, 20, 9));
        addView(this.descriptionView, LayoutHelper.createLinear(-2, -2, 49, i2, 0, i2, this.descriptionView2 == null ? 9 : 19));
        textView2 = this.descriptionView2;
        if (textView2 != null) {
        }
        updateColors();
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    private void updateColors() {
        TextView textView = this.titleView;
        int i = Theme.key_chat_serviceText;
        textView.setTextColor(getThemedColor(i));
        this.descriptionView.setTextColor(getThemedColor(i));
        TextView textView2 = this.descriptionView2;
        if (textView2 != null) {
            textView2.setTextColor(getThemedColor(i));
        }
    }
}
