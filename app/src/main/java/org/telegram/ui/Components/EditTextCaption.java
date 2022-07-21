package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.utils.CopyUtilities;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.TextStyleSpan;
/* loaded from: classes3.dex */
public class EditTextCaption extends EditTextBoldCursor {
    private boolean allowTextEntitiesIntersection;
    private String caption;
    private StaticLayout captionLayout;
    private boolean copyPasteShowed;
    private EditTextCaptionDelegate delegate;
    private int hintColor;
    private boolean isInitLineCount;
    private int lineCount;
    private float offsetY;
    private final Theme.ResourcesProvider resourcesProvider;
    private int userNameLength;
    private int xOffset;
    private int yOffset;
    private int selectionStart = -1;
    private int selectionEnd = -1;

    /* loaded from: classes3.dex */
    public interface EditTextCaptionDelegate {
        void onSpansChanged();
    }

    protected void onLineCountChanged(int i, int i2) {
    }

    public EditTextCaption(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.resourcesProvider = resourcesProvider;
        addTextChangedListener(new AnonymousClass1());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Components.EditTextCaption$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass1() {
            EditTextCaption.this = r1;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (EditTextCaption.this.lineCount != EditTextCaption.this.getLineCount()) {
                if (!EditTextCaption.this.isInitLineCount && EditTextCaption.this.getMeasuredWidth() > 0) {
                    EditTextCaption editTextCaption = EditTextCaption.this;
                    editTextCaption.onLineCountChanged(editTextCaption.lineCount, EditTextCaption.this.getLineCount());
                }
                EditTextCaption editTextCaption2 = EditTextCaption.this;
                editTextCaption2.lineCount = editTextCaption2.getLineCount();
            }
        }
    }

    public void setCaption(String str) {
        String str2 = this.caption;
        if ((str2 == null || str2.length() == 0) && (str == null || str.length() == 0)) {
            return;
        }
        String str3 = this.caption;
        if (str3 != null && str3.equals(str)) {
            return;
        }
        this.caption = str;
        if (str != null) {
            this.caption = str.replace('\n', ' ');
        }
        requestLayout();
    }

    public void setDelegate(EditTextCaptionDelegate editTextCaptionDelegate) {
        this.delegate = editTextCaptionDelegate;
    }

    public void setAllowTextEntitiesIntersection(boolean z) {
        this.allowTextEntitiesIntersection = z;
    }

    public void makeSelectedBold() {
        TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
        textStyleRun.flags |= 1;
        applyTextStyleToSelection(new TextStyleSpan(textStyleRun));
    }

    public void makeSelectedSpoiler() {
        TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
        textStyleRun.flags |= 256;
        applyTextStyleToSelection(new TextStyleSpan(textStyleRun));
    }

    public void makeSelectedItalic() {
        TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
        textStyleRun.flags |= 2;
        applyTextStyleToSelection(new TextStyleSpan(textStyleRun));
    }

    public void makeSelectedMono() {
        TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
        textStyleRun.flags |= 4;
        applyTextStyleToSelection(new TextStyleSpan(textStyleRun));
    }

    public void makeSelectedStrike() {
        TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
        textStyleRun.flags |= 8;
        applyTextStyleToSelection(new TextStyleSpan(textStyleRun));
    }

    public void makeSelectedUnderline() {
        TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
        textStyleRun.flags |= 16;
        applyTextStyleToSelection(new TextStyleSpan(textStyleRun));
    }

    public void makeSelectedUrl() {
        int i;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        builder.setTitle(LocaleController.getString("CreateLink", 2131625288));
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(this, getContext());
        anonymousClass2.setTextSize(1, 18.0f);
        anonymousClass2.setText("http://");
        anonymousClass2.setTextColor(getThemedColor("dialogTextBlack"));
        anonymousClass2.setHintText(LocaleController.getString("URL", 2131628788));
        anonymousClass2.setHeaderHintColor(getThemedColor("windowBackgroundWhiteBlueHeader"));
        anonymousClass2.setSingleLine(true);
        anonymousClass2.setFocusable(true);
        anonymousClass2.setTransformHintToHeader(true);
        anonymousClass2.setLineColors(getThemedColor("windowBackgroundWhiteInputField"), getThemedColor("windowBackgroundWhiteInputFieldActivated"), getThemedColor("windowBackgroundWhiteRedText3"));
        anonymousClass2.setImeOptions(6);
        anonymousClass2.setBackgroundDrawable(null);
        anonymousClass2.requestFocus();
        anonymousClass2.setPadding(0, 0, 0, 0);
        builder.setView(anonymousClass2);
        int i2 = this.selectionStart;
        if (i2 >= 0 && (i = this.selectionEnd) >= 0) {
            this.selectionEnd = -1;
            this.selectionStart = -1;
        } else {
            i2 = getSelectionStart();
            i = getSelectionEnd();
        }
        builder.setPositiveButton(LocaleController.getString("OK", 2131627127), new EditTextCaption$$ExternalSyntheticLambda0(this, i2, i, anonymousClass2));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        builder.show().setOnShowListener(new EditTextCaption$$ExternalSyntheticLambda1(anonymousClass2));
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) anonymousClass2.getLayoutParams();
        if (marginLayoutParams != null) {
            if (marginLayoutParams instanceof FrameLayout.LayoutParams) {
                ((FrameLayout.LayoutParams) marginLayoutParams).gravity = 1;
            }
            int dp = AndroidUtilities.dp(24.0f);
            marginLayoutParams.leftMargin = dp;
            marginLayoutParams.rightMargin = dp;
            marginLayoutParams.height = AndroidUtilities.dp(36.0f);
            anonymousClass2.setLayoutParams(marginLayoutParams);
        }
        anonymousClass2.setSelection(0, anonymousClass2.getText().length());
    }

    /* renamed from: org.telegram.ui.Components.EditTextCaption$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends EditTextBoldCursor {
        AnonymousClass2(EditTextCaption editTextCaption, Context context) {
            super(context);
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f), 1073741824));
        }
    }

    public /* synthetic */ void lambda$makeSelectedUrl$0(int i, int i2, EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface, int i3) {
        Editable text = getText();
        CharacterStyle[] characterStyleArr = (CharacterStyle[]) text.getSpans(i, i2, CharacterStyle.class);
        if (characterStyleArr != null && characterStyleArr.length > 0) {
            for (CharacterStyle characterStyle : characterStyleArr) {
                if (!(characterStyle instanceof AnimatedEmojiSpan)) {
                    int spanStart = text.getSpanStart(characterStyle);
                    int spanEnd = text.getSpanEnd(characterStyle);
                    text.removeSpan(characterStyle);
                    if (spanStart < i) {
                        text.setSpan(characterStyle, spanStart, i, 33);
                    }
                    if (spanEnd > i2) {
                        text.setSpan(characterStyle, i2, spanEnd, 33);
                    }
                }
            }
        }
        try {
            text.setSpan(new URLSpanReplacement(editTextBoldCursor.getText().toString()), i, i2, 33);
        } catch (Exception unused) {
        }
        EditTextCaptionDelegate editTextCaptionDelegate = this.delegate;
        if (editTextCaptionDelegate != null) {
            editTextCaptionDelegate.onSpansChanged();
        }
    }

    public static /* synthetic */ void lambda$makeSelectedUrl$1(EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface) {
        editTextBoldCursor.requestFocus();
        AndroidUtilities.showKeyboard(editTextBoldCursor);
    }

    public void makeSelectedRegular() {
        applyTextStyleToSelection(null);
    }

    public void setSelectionOverride(int i, int i2) {
        this.selectionStart = i;
        this.selectionEnd = i2;
    }

    private void applyTextStyleToSelection(TextStyleSpan textStyleSpan) {
        int i;
        int i2 = this.selectionStart;
        if (i2 >= 0 && (i = this.selectionEnd) >= 0) {
            this.selectionEnd = -1;
            this.selectionStart = -1;
        } else {
            i2 = getSelectionStart();
            i = getSelectionEnd();
        }
        MediaDataController.addStyleToText(textStyleSpan, i2, i, getText(), this.allowTextEntitiesIntersection);
        EditTextCaptionDelegate editTextCaptionDelegate = this.delegate;
        if (editTextCaptionDelegate != null) {
            editTextCaptionDelegate.onSpansChanged();
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onWindowFocusChanged(boolean z) {
        if (Build.VERSION.SDK_INT >= 23 || z || !this.copyPasteShowed) {
            try {
                super.onWindowFocusChanged(z);
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.EditTextCaption$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements ActionMode.Callback {
        final /* synthetic */ ActionMode.Callback val$callback;

        AnonymousClass3(ActionMode.Callback callback) {
            EditTextCaption.this = r1;
            this.val$callback = callback;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            EditTextCaption.this.copyPasteShowed = true;
            return this.val$callback.onCreateActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return this.val$callback.onPrepareActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (EditTextCaption.this.performMenuAction(menuItem.getItemId())) {
                actionMode.finish();
                return true;
            }
            try {
                return this.val$callback.onActionItemClicked(actionMode, menuItem);
            } catch (Exception unused) {
                return true;
            }
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            EditTextCaption.this.copyPasteShowed = false;
            this.val$callback.onDestroyActionMode(actionMode);
        }
    }

    private ActionMode.Callback overrideCallback(ActionMode.Callback callback) {
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(callback);
        return Build.VERSION.SDK_INT >= 23 ? new AnonymousClass4(this, anonymousClass3, callback) : anonymousClass3;
    }

    /* renamed from: org.telegram.ui.Components.EditTextCaption$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends ActionMode.Callback2 {
        final /* synthetic */ ActionMode.Callback val$callback;
        final /* synthetic */ ActionMode.Callback val$wrap;

        AnonymousClass4(EditTextCaption editTextCaption, ActionMode.Callback callback, ActionMode.Callback callback2) {
            this.val$wrap = callback;
            this.val$callback = callback2;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.val$wrap.onCreateActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return this.val$wrap.onPrepareActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.val$wrap.onActionItemClicked(actionMode, menuItem);
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            this.val$wrap.onDestroyActionMode(actionMode);
        }

        @Override // android.view.ActionMode.Callback2
        public void onGetContentRect(ActionMode actionMode, View view, android.graphics.Rect rect) {
            ActionMode.Callback callback = this.val$callback;
            if (callback instanceof ActionMode.Callback2) {
                ((ActionMode.Callback2) callback).onGetContentRect(actionMode, view, rect);
            } else {
                super.onGetContentRect(actionMode, view, rect);
            }
        }
    }

    public boolean performMenuAction(int i) {
        if (i == 2131230863) {
            makeSelectedRegular();
            return true;
        } else if (i == 2131230856) {
            makeSelectedBold();
            return true;
        } else if (i == 2131230859) {
            makeSelectedItalic();
            return true;
        } else if (i == 2131230861) {
            makeSelectedMono();
            return true;
        } else if (i == 2131230860) {
            makeSelectedUrl();
            return true;
        } else if (i == 2131230867) {
            makeSelectedStrike();
            return true;
        } else if (i == 2131230868) {
            makeSelectedUnderline();
            return true;
        } else if (i != 2131230866) {
            return false;
        } else {
            makeSelectedSpoiler();
            return true;
        }
    }

    @Override // org.telegram.ui.Components.EditTextBoldCursor, android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback, int i) {
        return super.startActionMode(overrideCallback(callback), i);
    }

    @Override // org.telegram.ui.Components.EditTextBoldCursor, android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return super.startActionMode(overrideCallback(callback));
    }

    @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
    @SuppressLint({"DrawAllocation"})
    public void onMeasure(int i, int i2) {
        int indexOf;
        try {
            this.isInitLineCount = getMeasuredWidth() == 0 && getMeasuredHeight() == 0;
            super.onMeasure(i, i2);
            if (this.isInitLineCount) {
                this.lineCount = getLineCount();
            }
            this.isInitLineCount = false;
        } catch (Exception e) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp(51.0f));
            FileLog.e(e);
        }
        this.captionLayout = null;
        String str = this.caption;
        if (str == null || str.length() <= 0) {
            return;
        }
        Editable text = getText();
        if (text.length() <= 1 || text.charAt(0) != '@' || (indexOf = TextUtils.indexOf((CharSequence) text, ' ')) == -1) {
            return;
        }
        TextPaint paint = getPaint();
        int i3 = indexOf + 1;
        CharSequence subSequence = text.subSequence(0, i3);
        int ceil = (int) Math.ceil(paint.measureText(text, 0, i3));
        this.userNameLength = subSequence.length();
        int measuredWidth = ((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight()) - ceil;
        CharSequence ellipsize = TextUtils.ellipsize(this.caption, paint, measuredWidth, TextUtils.TruncateAt.END);
        this.xOffset = ceil;
        try {
            StaticLayout staticLayout = new StaticLayout(ellipsize, getPaint(), measuredWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.captionLayout = staticLayout;
            if (staticLayout.getLineCount() > 0) {
                this.xOffset = (int) (this.xOffset + (-this.captionLayout.getLineLeft(0)));
            }
            this.yOffset = ((getMeasuredHeight() - this.captionLayout.getLineBottom(0)) / 2) + AndroidUtilities.dp(0.5f);
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    public String getCaption() {
        return this.caption;
    }

    @Override // org.telegram.ui.Components.EditTextBoldCursor
    public void setHintColor(int i) {
        super.setHintColor(i);
        this.hintColor = i;
        invalidate();
    }

    public void setOffsetY(float f) {
        this.offsetY = f;
        invalidate();
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    @Override // org.telegram.ui.Components.EditTextBoldCursor, org.telegram.ui.Components.EditTextEffects, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(0.0f, this.offsetY);
        super.onDraw(canvas);
        try {
            if (this.captionLayout != null && this.userNameLength == length()) {
                TextPaint paint = getPaint();
                int color = getPaint().getColor();
                paint.setColor(this.hintColor);
                canvas.save();
                canvas.translate(this.xOffset, this.yOffset);
                this.captionLayout.draw(canvas);
                canvas.restore();
                paint.setColor(color);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        canvas.restore();
    }

    @Override // org.telegram.ui.Components.EditTextBoldCursor, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        AccessibilityNodeInfoCompat wrap = AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo);
        if (!TextUtils.isEmpty(this.caption)) {
            wrap.setHintText(this.caption);
        }
        List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> actionList = wrap.getActionList();
        int i = 0;
        int size = actionList.size();
        while (true) {
            if (i >= size) {
                break;
            }
            AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat = actionList.get(i);
            if (accessibilityActionCompat.getId() == 268435456) {
                wrap.removeAction(accessibilityActionCompat);
                break;
            }
            i++;
        }
        if (hasSelection()) {
            wrap.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131230866, LocaleController.getString("Spoiler", 2131628466)));
            wrap.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131230856, LocaleController.getString("Bold", 2131624714)));
            wrap.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131230859, LocaleController.getString("Italic", 2131626357)));
            wrap.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131230861, LocaleController.getString("Mono", 2131626782)));
            wrap.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131230867, LocaleController.getString("Strike", 2131628547)));
            wrap.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131230868, LocaleController.getString("Underline", 2131628796)));
            wrap.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131230860, LocaleController.getString("CreateLink", 2131625288)));
            wrap.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2131230863, LocaleController.getString("Regular", 2131627943)));
        }
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i, Bundle bundle) {
        return performMenuAction(i) || super.performAccessibilityAction(i, bundle);
    }

    private int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }

    @Override // android.widget.TextView
    public boolean onTextContextMenuItem(int i) {
        if (i == 16908322) {
            ClipData primaryClip = ((ClipboardManager) getContext().getSystemService("clipboard")).getPrimaryClip();
            if (primaryClip != null && primaryClip.getItemCount() == 1 && primaryClip.getDescription().hasMimeType("text/html")) {
                try {
                    Spannable fromHTML = CopyUtilities.fromHTML(primaryClip.getItemAt(0).getHtmlText());
                    Emoji.replaceEmoji(fromHTML, getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) fromHTML.getSpans(0, fromHTML.length(), AnimatedEmojiSpan.class);
                    if (animatedEmojiSpanArr != null) {
                        for (AnimatedEmojiSpan animatedEmojiSpan : animatedEmojiSpanArr) {
                            animatedEmojiSpan.applyFontMetrics(getPaint().getFontMetricsInt(), AnimatedEmojiDrawable.getCacheTypeForEnterView());
                        }
                    }
                    int max = Math.max(0, getSelectionStart());
                    setText(getText().replace(max, Math.min(getText().length(), getSelectionEnd()), fromHTML));
                    setSelection(fromHTML.length() + max, max + fromHTML.length());
                    return true;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        } else {
            try {
                if (i == 16908321) {
                    AndroidUtilities.addToClipboard(getText().subSequence(Math.max(0, getSelectionStart()), Math.min(getText().length(), getSelectionEnd())));
                    return true;
                } else if (i == 16908320) {
                    int max2 = Math.max(0, getSelectionStart());
                    int min = Math.min(getText().length(), getSelectionEnd());
                    AndroidUtilities.addToClipboard(getText().subSequence(max2, min));
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    if (max2 != 0) {
                        spannableStringBuilder.append(getText().subSequence(0, max2));
                    }
                    if (min != getText().length()) {
                        spannableStringBuilder.append(getText().subSequence(min, getText().length()));
                    }
                    setText(spannableStringBuilder);
                    return true;
                }
            } catch (Exception unused) {
            }
        }
        return super.onTextContextMenuItem(i);
    }
}
