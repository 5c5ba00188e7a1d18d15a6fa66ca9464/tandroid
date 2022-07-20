package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextPaint;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.GestureDetectorCompat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class CustomPhoneKeyboardView extends ViewGroup {
    private ImageView backButton;
    private boolean dispatchBackWhenEmpty;
    private EditText editText;
    private boolean postedLongClick;
    private boolean runningLongClick;
    private View[] views = new View[12];
    private Runnable onBackButton = new CustomPhoneKeyboardView$$ExternalSyntheticLambda3(this);
    private Runnable detectLongClick = new CustomPhoneKeyboardView$$ExternalSyntheticLambda2(this);

    public static /* synthetic */ void lambda$new$3(View view) {
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int i) {
        return true;
    }

    public /* synthetic */ void lambda$new$0() {
        EditText editText = this.editText;
        if (editText != null) {
            if (editText.length() == 0 && !this.dispatchBackWhenEmpty) {
                return;
            }
            performHapticFeedback(3, 2);
            playSoundEffect(0);
            this.editText.dispatchKeyEvent(new KeyEvent(0, 67));
            this.editText.dispatchKeyEvent(new KeyEvent(1, 67));
            if (!this.runningLongClick) {
                return;
            }
            postDelayed(this.onBackButton, 50L);
        }
    }

    public /* synthetic */ void lambda$new$1() {
        this.postedLongClick = false;
        this.runningLongClick = true;
        this.onBackButton.run();
    }

    public CustomPhoneKeyboardView(Context context) {
        super(context);
        String str;
        int i = 0;
        while (i < 11) {
            if (i != 9) {
                switch (i) {
                    case 1:
                        str = "ABC";
                        break;
                    case 2:
                        str = "DEF";
                        break;
                    case 3:
                        str = "GHI";
                        break;
                    case 4:
                        str = "JKL";
                        break;
                    case 5:
                        str = "MNO";
                        break;
                    case 6:
                        str = "PQRS";
                        break;
                    case 7:
                        str = "TUV";
                        break;
                    case 8:
                        str = "WXYZ";
                        break;
                    case 9:
                    default:
                        str = "";
                        break;
                    case 10:
                        str = "+";
                        break;
                }
                String valueOf = String.valueOf(i != 10 ? i + 1 : 0);
                this.views[i] = new NumberButtonView(context, valueOf, str);
                this.views[i].setOnClickListener(new CustomPhoneKeyboardView$$ExternalSyntheticLambda0(this, valueOf));
                addView(this.views[i]);
            }
            i++;
        }
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(context, setupBackButtonDetector(context));
        this.backButton = anonymousClass1;
        anonymousClass1.setImageResource(2131165683);
        this.backButton.setColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.backButton.setBackground(getButtonDrawable());
        int dp = AndroidUtilities.dp(11.0f);
        this.backButton.setPadding(dp, dp, dp, dp);
        this.backButton.setOnClickListener(CustomPhoneKeyboardView$$ExternalSyntheticLambda1.INSTANCE);
        View[] viewArr = this.views;
        ImageView imageView = this.backButton;
        viewArr[11] = imageView;
        addView(imageView);
    }

    public /* synthetic */ void lambda$new$2(String str, View view) {
        if (this.editText == null) {
            return;
        }
        performHapticFeedback(3, 2);
        EditText editText = this.editText;
        if (editText instanceof EditTextBoldCursor) {
            ((EditTextBoldCursor) editText).setTextWatchersSuppressed(true, false);
        }
        Editable text = this.editText.getText();
        int selectionStart = this.editText.getSelectionEnd() == this.editText.length() ? -1 : this.editText.getSelectionStart() + str.length();
        if (this.editText.getSelectionStart() != -1 && this.editText.getSelectionEnd() != -1) {
            EditText editText2 = this.editText;
            editText2.setText(text.replace(editText2.getSelectionStart(), this.editText.getSelectionEnd(), str));
            EditText editText3 = this.editText;
            if (selectionStart == -1) {
                selectionStart = editText3.length();
            }
            editText3.setSelection(selectionStart);
        } else {
            this.editText.setText(str);
            EditText editText4 = this.editText;
            editText4.setSelection(editText4.length());
        }
        EditText editText5 = this.editText;
        if (!(editText5 instanceof EditTextBoldCursor)) {
            return;
        }
        ((EditTextBoldCursor) editText5).setTextWatchersSuppressed(false, true);
    }

    /* renamed from: org.telegram.ui.Components.CustomPhoneKeyboardView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends ImageView {
        final /* synthetic */ GestureDetectorCompat val$backDetector;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context, GestureDetectorCompat gestureDetectorCompat) {
            super(context);
            CustomPhoneKeyboardView.this = r1;
            this.val$backDetector = gestureDetectorCompat;
        }

        @Override // android.view.View
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if ((motionEvent.getAction() == 1 || motionEvent.getAction() == 3) && (CustomPhoneKeyboardView.this.postedLongClick || CustomPhoneKeyboardView.this.runningLongClick)) {
                CustomPhoneKeyboardView.this.postedLongClick = false;
                CustomPhoneKeyboardView.this.runningLongClick = false;
                removeCallbacks(CustomPhoneKeyboardView.this.detectLongClick);
                removeCallbacks(CustomPhoneKeyboardView.this.onBackButton);
            }
            super.onTouchEvent(motionEvent);
            return this.val$backDetector.onTouchEvent(motionEvent);
        }
    }

    public void setDispatchBackWhenEmpty(boolean z) {
        this.dispatchBackWhenEmpty = z;
    }

    /* renamed from: org.telegram.ui.Components.CustomPhoneKeyboardView$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends GestureDetector.SimpleOnGestureListener {
        final /* synthetic */ int val$touchSlop;

        AnonymousClass2(int i) {
            CustomPhoneKeyboardView.this = r1;
            this.val$touchSlop = i;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            if (CustomPhoneKeyboardView.this.postedLongClick) {
                CustomPhoneKeyboardView customPhoneKeyboardView = CustomPhoneKeyboardView.this;
                customPhoneKeyboardView.removeCallbacks(customPhoneKeyboardView.detectLongClick);
            }
            CustomPhoneKeyboardView.this.postedLongClick = true;
            CustomPhoneKeyboardView customPhoneKeyboardView2 = CustomPhoneKeyboardView.this;
            customPhoneKeyboardView2.postDelayed(customPhoneKeyboardView2.detectLongClick, 200L);
            CustomPhoneKeyboardView.this.onBackButton.run();
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if ((CustomPhoneKeyboardView.this.postedLongClick || CustomPhoneKeyboardView.this.runningLongClick) && (Math.abs(f) >= this.val$touchSlop || Math.abs(f2) >= this.val$touchSlop)) {
                CustomPhoneKeyboardView.this.postedLongClick = false;
                CustomPhoneKeyboardView.this.runningLongClick = false;
                CustomPhoneKeyboardView customPhoneKeyboardView = CustomPhoneKeyboardView.this;
                customPhoneKeyboardView.removeCallbacks(customPhoneKeyboardView.detectLongClick);
                CustomPhoneKeyboardView customPhoneKeyboardView2 = CustomPhoneKeyboardView.this;
                customPhoneKeyboardView2.removeCallbacks(customPhoneKeyboardView2.onBackButton);
            }
            return false;
        }
    }

    private GestureDetectorCompat setupBackButtonDetector(Context context) {
        return new GestureDetectorCompat(context, new AnonymousClass2(ViewConfiguration.get(context).getScaledTouchSlop()));
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
        this.dispatchBackWhenEmpty = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int width = (getWidth() - AndroidUtilities.dp(32.0f)) / 3;
        int height = (getHeight() - AndroidUtilities.dp(42.0f)) / 4;
        for (int i5 = 0; i5 < this.views.length; i5++) {
            int dp = ((i5 % 3) * (AndroidUtilities.dp(6.0f) + width)) + AndroidUtilities.dp(10.0f);
            int dp2 = ((i5 / 3) * (AndroidUtilities.dp(6.0f) + height)) + AndroidUtilities.dp(10.0f);
            View[] viewArr = this.views;
            if (viewArr[i5] != null) {
                viewArr[i5].layout(dp, dp2, dp + width, dp2 + height);
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        View[] viewArr;
        setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
        int width = (getWidth() - AndroidUtilities.dp(32.0f)) / 3;
        int height = (getHeight() - AndroidUtilities.dp(42.0f)) / 4;
        for (View view : this.views) {
            if (view != null) {
                view.measure(View.MeasureSpec.makeMeasureSpec(width, 1073741824), View.MeasureSpec.makeMeasureSpec(height, 1073741824));
            }
        }
    }

    public static Drawable getButtonDrawable() {
        return Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("listSelectorSDK21"), ColorUtils.setAlphaComponent(Theme.getColor("listSelectorSDK21"), 60));
    }

    public void updateColors() {
        View[] viewArr;
        this.backButton.setColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"));
        for (View view : this.views) {
            if (view != null) {
                view.setBackground(getButtonDrawable());
                if (view instanceof NumberButtonView) {
                    ((NumberButtonView) view).updateColors();
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class NumberButtonView extends View {
        private String mNumber;
        private String mSymbols;
        private TextPaint numberTextPaint = new TextPaint(1);
        private TextPaint symbolsTextPaint = new TextPaint(1);
        private android.graphics.Rect rect = new android.graphics.Rect();

        public NumberButtonView(Context context, String str, String str2) {
            super(context);
            this.mNumber = str;
            this.mSymbols = str2;
            this.numberTextPaint.setTextSize(AndroidUtilities.dp(24.0f));
            this.symbolsTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
            setBackground(CustomPhoneKeyboardView.getButtonDrawable());
            updateColors();
        }

        public void updateColors() {
            this.numberTextPaint.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.symbolsTextPaint.setColor(Theme.getColor("windowBackgroundWhiteHintText"));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float measureText = this.symbolsTextPaint.measureText(this.mSymbols);
            float measureText2 = this.numberTextPaint.measureText(this.mNumber);
            TextPaint textPaint = this.numberTextPaint;
            String str = this.mNumber;
            textPaint.getTextBounds(str, 0, str.length(), this.rect);
            TextPaint textPaint2 = this.symbolsTextPaint;
            String str2 = this.mSymbols;
            textPaint2.getTextBounds(str2, 0, str2.length(), this.rect);
            canvas.drawText(this.mNumber, (getWidth() * 0.25f) - (measureText2 / 2.0f), (getHeight() / 2.0f) + (this.rect.height() / 2.0f), this.numberTextPaint);
            canvas.drawText(this.mSymbols, (getWidth() * 0.7f) - (measureText / 2.0f), (getHeight() / 2.0f) + (this.rect.height() / 2.0f), this.symbolsTextPaint);
        }
    }
}
