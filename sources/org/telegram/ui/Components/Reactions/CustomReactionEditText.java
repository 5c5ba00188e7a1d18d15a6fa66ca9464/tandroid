package org.telegram.ui.Components.Reactions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.GestureDetectorCompat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.EditTextCaption;
@SuppressLint({"ViewConstructor"})
/* loaded from: classes4.dex */
public class CustomReactionEditText extends EditTextCaption {
    private final GestureDetectorCompat gestureDetector;
    private Runnable onFocused;
    private final Theme.ResourcesProvider resourcesProvider;

    @Override // org.telegram.ui.Components.EditTextCaption, org.telegram.ui.Components.EditTextBoldCursor, android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override // org.telegram.ui.Components.EditTextCaption, org.telegram.ui.Components.EditTextBoldCursor, android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback, int i) {
        return null;
    }

    public CustomReactionEditText(Context context, Theme.ResourcesProvider resourcesProvider, int i) {
        super(context, resourcesProvider);
        this.resourcesProvider = resourcesProvider;
        this.gestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener(this) { // from class: org.telegram.ui.Components.Reactions.CustomReactionEditText.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent motionEvent) {
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                return true;
            }
        });
        setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        setIncludeFontPadding(true);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 21) {
            setShowSoftInputOnFocus(false);
        }
        setSingleLine(false);
        setMaxLines(50);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(i)});
        setTextSize(1, 22.0f);
        setGravity(80);
        setPadding(AndroidUtilities.dp(18.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(12.0f));
        setTextColor(getThemedColor(Theme.key_chat_messagePanelText));
        setLinkTextColor(getThemedColor(Theme.key_chat_messageLinkOut));
        setHighlightColor(getThemedColor(Theme.key_chat_inTextSelectionHighlight));
        int i3 = Theme.key_chat_messagePanelHint;
        setHintColor(getThemedColor(i3));
        setHintTextColor(getThemedColor(i3));
        setCursorColor(getThemedColor(Theme.key_chat_messagePanelCursor));
        setHandlesColor(getThemedColor(Theme.key_chat_TextSelectionCursor));
        if (i2 >= 28) {
            setFallbackLineSpacing(false);
        }
        setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.Components.Reactions.CustomReactionEditText$$ExternalSyntheticLambda0
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z) {
                CustomReactionEditText.this.lambda$new$0(view, z);
            }
        });
        setLongClickable(false);
        setTextIsSelectable(false);
        setFocusableInTouchMode(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, boolean z) {
        if (z) {
            removeReactionsSpan(true);
            Runnable runnable = this.onFocused;
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        addReactionsSpan();
    }

    @Override // org.telegram.ui.Components.EditTextEffects, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.gestureDetector.onTouchEvent(motionEvent)) {
            return false;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void setOnFocused(Runnable runnable) {
        this.onFocused = runnable;
    }

    public void addReactionsSpan() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getText());
        if (((AddReactionsSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), AddReactionsSpan.class)).length == 0) {
            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder("x");
            AddReactionsSpan addReactionsSpan = new AddReactionsSpan(15.0f, this.resourcesProvider);
            addReactionsSpan.show(this);
            spannableStringBuilder2.setSpan(addReactionsSpan, 0, spannableStringBuilder2.length(), 33);
            setText(getText().append((CharSequence) spannableStringBuilder2));
        }
    }

    public void removeReactionsSpan(boolean z) {
        AddReactionsSpan[] addReactionsSpanArr;
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getText());
        for (final AddReactionsSpan addReactionsSpan : (AddReactionsSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), AddReactionsSpan.class)) {
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.Reactions.CustomReactionEditText$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    CustomReactionEditText.this.lambda$removeReactionsSpan$1(spannableStringBuilder, addReactionsSpan);
                }
            };
            if (z) {
                setCursorVisible(false);
                addReactionsSpan.hide(this, runnable);
            } else {
                runnable.run();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeReactionsSpan$1(SpannableStringBuilder spannableStringBuilder, AddReactionsSpan addReactionsSpan) {
        getText().delete(spannableStringBuilder.getSpanStart(addReactionsSpan), spannableStringBuilder.getSpanEnd(addReactionsSpan));
        setCursorVisible(true);
    }

    public int getEditTextSelectionEnd() {
        int selectionEnd = getSelectionEnd();
        if (selectionEnd < 0) {
            return 0;
        }
        return selectionEnd;
    }

    public int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    public Paint.FontMetricsInt getFontMetricsInt() {
        return getPaint().getFontMetricsInt();
    }
}
