package org.telegram.ui.Components;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class ReportAlert extends BottomSheet {
    private BottomSheetCell clearButton;
    private EditTextBoldCursor editText;

    protected void onSend(int i, String str) {
        throw null;
    }

    /* loaded from: classes3.dex */
    public static class BottomSheetCell extends FrameLayout {
        private View background;
        private TextView textView;

        public BottomSheetCell(Context context) {
            super(context);
            View view = new View(context);
            this.background = view;
            view.setBackground(Theme.AdaptiveRipple.filledRect("featuredStickers_addButton", 4.0f));
            addView(this.background, LayoutHelper.createFrame(-1, -1.0f, 0, 16.0f, 16.0f, 16.0f, 16.0f));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setGravity(1);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setGravity(17);
            this.textView.setTextColor(Theme.getColor("featuredStickers_buttonText"));
            this.textView.setTextSize(1, 14.0f);
            this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            addView(this.textView, LayoutHelper.createFrame(-2, -2, 17));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0f), 1073741824));
        }

        public void setText(CharSequence charSequence) {
            this.textView.setText(charSequence);
        }
    }

    public ReportAlert(Context context, int i) {
        super(context, true);
        setApplyBottomPadding(false);
        setApplyTopPadding(false);
        ScrollView scrollView = new ScrollView(context);
        scrollView.setFillViewport(true);
        setCustomView(scrollView);
        FrameLayout frameLayout = new FrameLayout(context);
        scrollView.addView(frameLayout, LayoutHelper.createScroll(-1, -2, 51));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        rLottieImageView.setAnimation(2131558523, 120, 120);
        rLottieImageView.playAnimation();
        frameLayout.addView(rLottieImageView, LayoutHelper.createFrame(160, 160.0f, 49, 17.0f, 14.0f, 17.0f, 0.0f));
        TextView textView = new TextView(context);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setTextSize(1, 24.0f);
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        int i2 = 5;
        if (i == 0) {
            textView.setText(LocaleController.getString("ReportTitleSpam", 2131627959));
        } else if (i == 6) {
            textView.setText(LocaleController.getString("ReportTitleFake", 2131627957));
        } else if (i == 1) {
            textView.setText(LocaleController.getString("ReportTitleViolence", 2131627960));
        } else if (i == 2) {
            textView.setText(LocaleController.getString("ReportTitleChild", 2131627956));
        } else if (i == 5) {
            textView.setText(LocaleController.getString("ReportTitlePornography", 2131627958));
        } else if (i == 100) {
            textView.setText(LocaleController.getString("ReportChat", 2131627930));
        }
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 49, 17.0f, 197.0f, 17.0f, 0.0f));
        TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 14.0f);
        textView2.setTextColor(Theme.getColor("dialogTextGray3"));
        textView2.setGravity(1);
        textView2.setText(LocaleController.getString("ReportInfo", 2131627942));
        frameLayout.addView(textView2, LayoutHelper.createFrame(-2, -2.0f, 49, 30.0f, 235.0f, 30.0f, 44.0f));
        EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
        this.editText = editTextBoldCursor;
        editTextBoldCursor.setTextSize(1, 18.0f);
        this.editText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.editText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.editText.setBackgroundDrawable(null);
        this.editText.setLineColors(getThemedColor("windowBackgroundWhiteInputField"), getThemedColor("windowBackgroundWhiteInputFieldActivated"), getThemedColor("windowBackgroundWhiteRedText3"));
        this.editText.setMaxLines(1);
        this.editText.setLines(1);
        this.editText.setPadding(0, 0, 0, 0);
        this.editText.setSingleLine(true);
        this.editText.setGravity(!LocaleController.isRTL ? 3 : i2);
        this.editText.setInputType(180224);
        this.editText.setImeOptions(6);
        this.editText.setHint(LocaleController.getString("ReportHint", 2131627941));
        this.editText.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.editText.setCursorSize(AndroidUtilities.dp(20.0f));
        this.editText.setCursorWidth(1.5f);
        this.editText.setOnEditorActionListener(new ReportAlert$$ExternalSyntheticLambda1(this));
        frameLayout.addView(this.editText, LayoutHelper.createFrame(-1, 36.0f, 51, 17.0f, 305.0f, 17.0f, 0.0f));
        BottomSheetCell bottomSheetCell = new BottomSheetCell(context);
        this.clearButton = bottomSheetCell;
        bottomSheetCell.setBackground(null);
        this.clearButton.setText(LocaleController.getString("ReportSend", 2131627946));
        this.clearButton.background.setOnClickListener(new ReportAlert$$ExternalSyntheticLambda0(this, i));
        frameLayout.addView(this.clearButton, LayoutHelper.createFrame(-1, 50.0f, 51, 0.0f, 357.0f, 0.0f, 0.0f));
        this.smoothKeyboardAnimationEnabled = true;
    }

    public /* synthetic */ boolean lambda$new$0(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            this.clearButton.background.callOnClick();
            return true;
        }
        return false;
    }

    public /* synthetic */ void lambda$new$1(int i, View view) {
        AndroidUtilities.hideKeyboard(this.editText);
        onSend(i, this.editText.getText().toString());
        dismiss();
    }
}
