package org.telegram.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
/* loaded from: classes3.dex */
public class SponsoredMessageInfoView extends FrameLayout {
    public SponsoredMessageInfoView(Activity activity, Theme.ResourcesProvider resourcesProvider) {
        super(activity);
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        TextView textView = new TextView(activity);
        textView.setText(LocaleController.getString("SponsoredMessageInfo", 2131628471));
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText", resourcesProvider));
        textView.setTextSize(1, 20.0f);
        TextView textView2 = new TextView(activity);
        textView2.setText(LocaleController.getString("SponsoredMessageInfoDescription1", 2131628472));
        textView2.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText", resourcesProvider));
        textView2.setTextSize(1, 14.0f);
        textView2.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        TextView textView3 = new TextView(activity);
        textView3.setText(LocaleController.getString("SponsoredMessageInfoDescription2", 2131628473));
        textView3.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText", resourcesProvider));
        textView3.setTextSize(1, 14.0f);
        textView3.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        TextView textView4 = new TextView(activity);
        textView4.setText(LocaleController.getString("SponsoredMessageInfoDescription3", 2131628474));
        textView4.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText", resourcesProvider));
        textView4.setTextSize(1, 14.0f);
        textView4.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Theme.getColor("featuredStickers_addButton", resourcesProvider));
        paint.setStrokeWidth(AndroidUtilities.dp(1.0f));
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, activity, paint);
        anonymousClass1.setOnClickListener(new AnonymousClass2(this, activity));
        anonymousClass1.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
        anonymousClass1.setText(LocaleController.getString("SponsoredMessageAlertLearnMoreUrl", 2131628468));
        anonymousClass1.setTextColor(Theme.getColor("featuredStickers_addButton", resourcesProvider));
        anonymousClass1.setBackground(Theme.AdaptiveRipple.filledRect(Theme.getColor("dialogBackground", resourcesProvider), 4.0f));
        anonymousClass1.setTextSize(1, 14.0f);
        anonymousClass1.setGravity(16);
        TextView textView5 = new TextView(activity);
        textView5.setText(LocaleController.getString("SponsoredMessageInfoDescription4", 2131628475));
        textView5.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        textView5.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText", resourcesProvider));
        textView5.setTextSize(1, 14.0f);
        linearLayout.addView(textView);
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 0, 0, 18, 0, 0));
        linearLayout.addView(textView3, LayoutHelper.createLinear(-1, -2, 0, 0, 24, 0, 0));
        linearLayout.addView(textView4, LayoutHelper.createLinear(-1, -2, 0, 0, 24, 0, 0));
        linearLayout.addView(anonymousClass1, LayoutHelper.createLinear(-2, 34, 1, 0, 14, 0, 0));
        linearLayout.addView(textView5, LayoutHelper.createLinear(-1, -2, 0, 0, 14, 0, 0));
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(linearLayout);
        addView(scrollView, LayoutHelper.createFrame(-1, -2.0f, 0, 22.0f, 12.0f, 22.0f, 22.0f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.SponsoredMessageInfoView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends TextView {
        final /* synthetic */ Paint val$buttonPaint;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(SponsoredMessageInfoView sponsoredMessageInfoView, Context context, Paint paint) {
            super(context);
            this.val$buttonPaint = paint;
        }

        @Override // android.widget.TextView, android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), getMeasuredWidth() - AndroidUtilities.dp(1.0f), getMeasuredHeight() - AndroidUtilities.dp(1.0f));
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.val$buttonPaint);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.SponsoredMessageInfoView$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        final /* synthetic */ Activity val$context;

        AnonymousClass2(SponsoredMessageInfoView sponsoredMessageInfoView, Activity activity) {
            this.val$context = activity;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Browser.openUrl(this.val$context, LocaleController.getString("SponsoredMessageAlertLearnMoreUrl", 2131628468));
        }
    }
}
