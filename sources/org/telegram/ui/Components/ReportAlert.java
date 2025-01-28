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
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;

/* loaded from: classes3.dex */
public abstract class ReportAlert extends BottomSheet {
    private BottomSheetCell clearButton;
    private EditTextBoldCursor editText;

    public static class BottomSheetCell extends FrameLayout {
        private View background;
        private TextView textView;

        public BottomSheetCell(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            View view = new View(context);
            this.background = view;
            view.setBackground(Theme.AdaptiveRipple.filledRectByKey(Theme.key_featuredStickers_addButton, 8.0f));
            addView(this.background, LayoutHelper.createFrame(-1, -1.0f, 0, 16.0f, 16.0f, 16.0f, 16.0f));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setGravity(1);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setGravity(17);
            this.textView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText, resourcesProvider));
            this.textView.setTextSize(1, 14.0f);
            this.textView.setTypeface(AndroidUtilities.bold());
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

    /* JADX WARN: Removed duplicated region for block: B:7:0x0132  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ReportAlert(Context context, final int i, Theme.ResourcesProvider resourcesProvider) {
        super(context, true, resourcesProvider);
        int i2;
        setApplyBottomPadding(false);
        setApplyTopPadding(false);
        ScrollView scrollView = new ScrollView(context);
        scrollView.setFillViewport(true);
        setCustomView(scrollView);
        FrameLayout frameLayout = new FrameLayout(context);
        scrollView.addView(frameLayout, LayoutHelper.createScroll(-1, -2, 51));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        rLottieImageView.setAnimation(R.raw.report_police, 120, 120);
        rLottieImageView.playAnimation();
        frameLayout.addView(rLottieImageView, LayoutHelper.createFrame(NotificationCenter.audioRouteChanged, 160.0f, 49, 17.0f, 14.0f, 17.0f, 0.0f));
        TextView textView = new TextView(context);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setTextSize(1, 24.0f);
        textView.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
        if (i == 0) {
            i2 = R.string.ReportTitleSpam;
        } else if (i == 6) {
            i2 = R.string.ReportTitleFake;
        } else if (i == 1) {
            i2 = R.string.ReportTitleViolence;
        } else if (i == 2) {
            i2 = R.string.ReportTitleChild;
        } else {
            if (i != 5) {
                if (i == 100) {
                    i2 = R.string.ReportChat;
                }
                frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 49, 17.0f, 197.0f, 17.0f, 0.0f));
                TextView textView2 = new TextView(context);
                textView2.setTextSize(1, 14.0f);
                textView2.setTextColor(getThemedColor(Theme.key_dialogTextGray3));
                textView2.setGravity(1);
                textView2.setText(LocaleController.getString(R.string.ReportInfo));
                frameLayout.addView(textView2, LayoutHelper.createFrame(-2, -2.0f, 49, 30.0f, 235.0f, 30.0f, 44.0f));
                EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
                this.editText = editTextBoldCursor;
                editTextBoldCursor.setTextSize(1, 18.0f);
                this.editText.setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                EditTextBoldCursor editTextBoldCursor2 = this.editText;
                int i3 = Theme.key_windowBackgroundWhiteBlackText;
                editTextBoldCursor2.setTextColor(getThemedColor(i3));
                this.editText.setBackgroundDrawable(null);
                this.editText.setLineColors(getThemedColor(Theme.key_windowBackgroundWhiteInputField), getThemedColor(Theme.key_windowBackgroundWhiteInputFieldActivated), getThemedColor(Theme.key_text_RedRegular));
                this.editText.setMaxLines(1);
                this.editText.setLines(1);
                this.editText.setPadding(0, 0, 0, 0);
                this.editText.setSingleLine(true);
                this.editText.setGravity(LocaleController.isRTL ? 5 : 3);
                this.editText.setInputType(180224);
                this.editText.setImeOptions(6);
                this.editText.setHint(LocaleController.getString(R.string.ReportHint));
                this.editText.setCursorColor(getThemedColor(i3));
                this.editText.setCursorSize(AndroidUtilities.dp(20.0f));
                this.editText.setCursorWidth(1.5f);
                this.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Components.ReportAlert$$ExternalSyntheticLambda0
                    @Override // android.widget.TextView.OnEditorActionListener
                    public final boolean onEditorAction(TextView textView3, int i4, KeyEvent keyEvent) {
                        boolean lambda$new$0;
                        lambda$new$0 = ReportAlert.this.lambda$new$0(textView3, i4, keyEvent);
                        return lambda$new$0;
                    }
                });
                frameLayout.addView(this.editText, LayoutHelper.createFrame(-1, 36.0f, 51, 17.0f, 305.0f, 17.0f, 0.0f));
                BottomSheetCell bottomSheetCell = new BottomSheetCell(context, resourcesProvider);
                this.clearButton = bottomSheetCell;
                bottomSheetCell.setBackground(null);
                this.clearButton.setText(LocaleController.getString(R.string.ReportSend));
                ScaleStateListAnimator.apply(this.clearButton);
                this.clearButton.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ReportAlert$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ReportAlert.this.lambda$new$1(i, view);
                    }
                });
                frameLayout.addView(this.clearButton, LayoutHelper.createFrame(-1, 50.0f, 51, 0.0f, 357.0f, 0.0f, 0.0f));
                this.smoothKeyboardAnimationEnabled = true;
            }
            i2 = R.string.ReportTitlePornography;
        }
        textView.setText(LocaleController.getString(i2));
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 49, 17.0f, 197.0f, 17.0f, 0.0f));
        TextView textView22 = new TextView(context);
        textView22.setTextSize(1, 14.0f);
        textView22.setTextColor(getThemedColor(Theme.key_dialogTextGray3));
        textView22.setGravity(1);
        textView22.setText(LocaleController.getString(R.string.ReportInfo));
        frameLayout.addView(textView22, LayoutHelper.createFrame(-2, -2.0f, 49, 30.0f, 235.0f, 30.0f, 44.0f));
        EditTextBoldCursor editTextBoldCursor3 = new EditTextBoldCursor(context);
        this.editText = editTextBoldCursor3;
        editTextBoldCursor3.setTextSize(1, 18.0f);
        this.editText.setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
        EditTextBoldCursor editTextBoldCursor22 = this.editText;
        int i32 = Theme.key_windowBackgroundWhiteBlackText;
        editTextBoldCursor22.setTextColor(getThemedColor(i32));
        this.editText.setBackgroundDrawable(null);
        this.editText.setLineColors(getThemedColor(Theme.key_windowBackgroundWhiteInputField), getThemedColor(Theme.key_windowBackgroundWhiteInputFieldActivated), getThemedColor(Theme.key_text_RedRegular));
        this.editText.setMaxLines(1);
        this.editText.setLines(1);
        this.editText.setPadding(0, 0, 0, 0);
        this.editText.setSingleLine(true);
        this.editText.setGravity(LocaleController.isRTL ? 5 : 3);
        this.editText.setInputType(180224);
        this.editText.setImeOptions(6);
        this.editText.setHint(LocaleController.getString(R.string.ReportHint));
        this.editText.setCursorColor(getThemedColor(i32));
        this.editText.setCursorSize(AndroidUtilities.dp(20.0f));
        this.editText.setCursorWidth(1.5f);
        this.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Components.ReportAlert$$ExternalSyntheticLambda0
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView3, int i4, KeyEvent keyEvent) {
                boolean lambda$new$0;
                lambda$new$0 = ReportAlert.this.lambda$new$0(textView3, i4, keyEvent);
                return lambda$new$0;
            }
        });
        frameLayout.addView(this.editText, LayoutHelper.createFrame(-1, 36.0f, 51, 17.0f, 305.0f, 17.0f, 0.0f));
        BottomSheetCell bottomSheetCell2 = new BottomSheetCell(context, resourcesProvider);
        this.clearButton = bottomSheetCell2;
        bottomSheetCell2.setBackground(null);
        this.clearButton.setText(LocaleController.getString(R.string.ReportSend));
        ScaleStateListAnimator.apply(this.clearButton);
        this.clearButton.background.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ReportAlert$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ReportAlert.this.lambda$new$1(i, view);
            }
        });
        frameLayout.addView(this.clearButton, LayoutHelper.createFrame(-1, 50.0f, 51, 0.0f, 357.0f, 0.0f, 0.0f));
        this.smoothKeyboardAnimationEnabled = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6) {
            return false;
        }
        this.clearButton.background.callOnClick();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i, View view) {
        AndroidUtilities.hideKeyboard(this.editText);
        onSend(i, this.editText.getText().toString());
        dismiss();
    }

    protected abstract void onSend(int i, String str);
}
