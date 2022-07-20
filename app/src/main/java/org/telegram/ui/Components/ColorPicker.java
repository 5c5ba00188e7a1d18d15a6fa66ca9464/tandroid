package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Keep;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public class ColorPicker extends FrameLayout {
    private ImageView addButton;
    private Drawable circleDrawable;
    private boolean circlePressed;
    private ImageView clearButton;
    private LinearGradient colorGradient;
    private boolean colorPressed;
    private Bitmap colorWheelBitmap;
    private int colorWheelWidth;
    private AnimatorSet colorsAnimator;
    private int currentResetType;
    private final ColorPickerDelegate delegate;
    boolean ignoreTextChange;
    private long lastUpdateTime;
    private Paint linePaint;
    private LinearLayout linearLayout;
    private ActionBarMenuItem menuItem;
    private boolean myMessagesColor;
    private FrameLayout radioContainer;
    private TextView resetButton;
    private int selectedColor;
    private RectF sliderRect = new RectF();
    private RadioButton[] radioButton = new RadioButton[4];
    private int colorsCount = 1;
    private int maxColorsCount = 1;
    private float[] colorHSV = {0.0f, 0.0f, 1.0f};
    private float[] hsvTemp = new float[3];
    private float pressedMoveProgress = 1.0f;
    private float minBrightness = 0.0f;
    private float maxBrightness = 1.0f;
    private float minHsvBrightness = 0.0f;
    private float maxHsvBrightness = 1.0f;
    private EditTextBoldCursor[] colorEditText = new EditTextBoldCursor[2];
    private Paint circlePaint = new Paint(1);
    private Paint colorWheelPaint = new Paint(5);
    private Paint valueSliderPaint = new Paint(5);

    /* loaded from: classes3.dex */
    public interface ColorPickerDelegate {

        /* renamed from: org.telegram.ui.Components.ColorPicker$ColorPickerDelegate$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
            public static void $default$deleteTheme(ColorPickerDelegate colorPickerDelegate) {
            }

            public static int $default$getDefaultColor(ColorPickerDelegate colorPickerDelegate, int i) {
                return 0;
            }

            public static void $default$openThemeCreate(ColorPickerDelegate colorPickerDelegate, boolean z) {
            }
        }

        void deleteTheme();

        int getDefaultColor(int i);

        void openThemeCreate(boolean z);

        void setColor(int i, int i2, boolean z);
    }

    public static /* synthetic */ void lambda$new$4(View view) {
    }

    /* loaded from: classes3.dex */
    public static class RadioButton extends View {
        private ObjectAnimator checkAnimator;
        private boolean checked;
        private float checkedState;
        private int currentColor;
        private final Paint paint = new Paint(1);

        public RadioButton(Context context) {
            super(context);
        }

        void updateCheckedState(boolean z) {
            ObjectAnimator objectAnimator = this.checkAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            float f = 1.0f;
            if (z) {
                float[] fArr = new float[1];
                if (!this.checked) {
                    f = 0.0f;
                }
                fArr[0] = f;
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "checkedState", fArr);
                this.checkAnimator = ofFloat;
                ofFloat.setDuration(200L);
                this.checkAnimator.start();
                return;
            }
            if (!this.checked) {
                f = 0.0f;
            }
            setCheckedState(f);
        }

        public void setChecked(boolean z, boolean z2) {
            this.checked = z;
            updateCheckedState(z2);
        }

        public void setColor(int i) {
            this.currentColor = i;
            invalidate();
        }

        public int getColor() {
            return this.currentColor;
        }

        @Keep
        public void setCheckedState(float f) {
            this.checkedState = f;
            invalidate();
        }

        @Keep
        public float getCheckedState() {
            return this.checkedState;
        }

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            updateCheckedState(false);
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30.0f), 1073741824));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float dp = AndroidUtilities.dp(15.0f);
            float measuredWidth = getMeasuredWidth() * 0.5f;
            float measuredHeight = getMeasuredHeight() * 0.5f;
            this.paint.setColor(this.currentColor);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(AndroidUtilities.dp(3.0f));
            this.paint.setAlpha(Math.round(this.checkedState * 255.0f));
            canvas.drawCircle(measuredWidth, measuredHeight, dp - (this.paint.getStrokeWidth() * 0.5f), this.paint);
            this.paint.setAlpha(255);
            this.paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(measuredWidth, measuredHeight, dp - (AndroidUtilities.dp(5.0f) * this.checkedState), this.paint);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setText(LocaleController.getString("ColorPickerMainColor", 2131625178));
            accessibilityNodeInfo.setClassName(Button.class.getName());
            accessibilityNodeInfo.setChecked(this.checked);
            accessibilityNodeInfo.setCheckable(true);
            accessibilityNodeInfo.setEnabled(true);
        }
    }

    public ColorPicker(Context context, boolean z, ColorPickerDelegate colorPickerDelegate) {
        super(context);
        this.delegate = colorPickerDelegate;
        setWillNotDraw(false);
        this.circleDrawable = context.getResources().getDrawable(2131165570).mutate();
        Paint paint = new Paint();
        this.linePaint = paint;
        paint.setColor(301989888);
        setClipChildren(false);
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(context);
        this.linearLayout = anonymousClass1;
        anonymousClass1.setOrientation(0);
        addView(this.linearLayout, LayoutHelper.createFrame(-1, 54.0f, 51, 27.0f, -6.0f, 17.0f, 0.0f));
        this.linearLayout.setWillNotDraw(false);
        FrameLayout frameLayout = new FrameLayout(context);
        this.radioContainer = frameLayout;
        frameLayout.setClipChildren(false);
        addView(this.radioContainer, LayoutHelper.createFrame(174, 30.0f, 49, 72.0f, 1.0f, 0.0f, 0.0f));
        int i = 0;
        while (i < 4) {
            this.radioButton[i] = new RadioButton(context);
            this.radioButton[i].setChecked(this.selectedColor == i, false);
            this.radioContainer.addView(this.radioButton[i], LayoutHelper.createFrame(30, 30.0f, 48, 0.0f, 0.0f, 0.0f, 0.0f));
            this.radioButton[i].setOnClickListener(new ColorPicker$$ExternalSyntheticLambda0(this));
            i++;
        }
        int i2 = 0;
        while (true) {
            EditTextBoldCursor[] editTextBoldCursorArr = this.colorEditText;
            if (i2 >= editTextBoldCursorArr.length) {
                break;
            }
            if (i2 % 2 == 0) {
                editTextBoldCursorArr[i2] = new AnonymousClass2(context, i2);
                this.colorEditText[i2].setBackgroundDrawable(null);
                this.colorEditText[i2].setText("#");
                this.colorEditText[i2].setEnabled(false);
                this.colorEditText[i2].setFocusable(false);
                this.colorEditText[i2].setPadding(0, AndroidUtilities.dp(5.0f), 0, AndroidUtilities.dp(16.0f));
                this.linearLayout.addView(this.colorEditText[i2], LayoutHelper.createLinear(-2, -1, 0.0f, 0.0f, 0.0f, 0.0f));
            } else {
                editTextBoldCursorArr[i2] = new AnonymousClass3(context, i2);
                this.colorEditText[i2].setBackgroundDrawable(null);
                this.colorEditText[i2].setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                this.colorEditText[i2].setHint("8BC6ED");
                this.colorEditText[i2].setPadding(0, AndroidUtilities.dp(5.0f), 0, AndroidUtilities.dp(16.0f));
                this.linearLayout.addView(this.colorEditText[i2], LayoutHelper.createLinear(71, -1, 0.0f, 0.0f, 0.0f, 0.0f));
                this.colorEditText[i2].addTextChangedListener(new AnonymousClass4(i2));
                this.colorEditText[i2].setOnEditorActionListener(ColorPicker$$ExternalSyntheticLambda5.INSTANCE);
            }
            this.colorEditText[i2].setTextSize(1, 16.0f);
            this.colorEditText[i2].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.colorEditText[i2].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.colorEditText[i2].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.colorEditText[i2].setCursorSize(AndroidUtilities.dp(18.0f));
            this.colorEditText[i2].setCursorWidth(1.5f);
            this.colorEditText[i2].setSingleLine(true);
            this.colorEditText[i2].setGravity(19);
            this.colorEditText[i2].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
            this.colorEditText[i2].setTransformHintToHeader(true);
            this.colorEditText[i2].setInputType(524416);
            this.colorEditText[i2].setImeOptions(268435462);
            if (i2 == 1) {
                this.colorEditText[i2].requestFocus();
            } else if (i2 == 2 || i2 == 3) {
                this.colorEditText[i2].setVisibility(8);
            }
            i2++;
        }
        ImageView imageView = new ImageView(getContext());
        this.addButton = imageView;
        imageView.setBackground(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 1));
        this.addButton.setImageResource(2131165625);
        this.addButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"), PorterDuff.Mode.MULTIPLY));
        this.addButton.setScaleType(ImageView.ScaleType.CENTER);
        this.addButton.setOnClickListener(new ColorPicker$$ExternalSyntheticLambda3(this));
        this.addButton.setContentDescription(LocaleController.getString("Add", 2131624237));
        addView(this.addButton, LayoutHelper.createFrame(30, 30.0f, 49, 36.0f, 1.0f, 0.0f, 0.0f));
        AnonymousClass6 anonymousClass6 = new AnonymousClass6(getContext());
        this.clearButton = anonymousClass6;
        anonymousClass6.setBackground(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 1));
        this.clearButton.setImageResource(2131165687);
        this.clearButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"), PorterDuff.Mode.MULTIPLY));
        this.clearButton.setAlpha(0.0f);
        this.clearButton.setScaleX(0.0f);
        this.clearButton.setScaleY(0.0f);
        this.clearButton.setScaleType(ImageView.ScaleType.CENTER);
        this.clearButton.setVisibility(4);
        this.clearButton.setOnClickListener(new ColorPicker$$ExternalSyntheticLambda2(this));
        this.clearButton.setContentDescription(LocaleController.getString("ClearButton", 2131625132));
        addView(this.clearButton, LayoutHelper.createFrame(30, 30.0f, 51, 97.0f, 1.0f, 0.0f, 0.0f));
        TextView textView = new TextView(context);
        this.resetButton = textView;
        textView.setTextSize(1, 15.0f);
        this.resetButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.resetButton.setGravity(17);
        this.resetButton.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
        this.resetButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        addView(this.resetButton, LayoutHelper.createFrame(-2, 36.0f, 53, 0.0f, 3.0f, 14.0f, 0.0f));
        this.resetButton.setOnClickListener(ColorPicker$$ExternalSyntheticLambda4.INSTANCE);
        if (z) {
            ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, null, 0, Theme.getColor("windowBackgroundWhiteBlackText"));
            this.menuItem = actionBarMenuItem;
            actionBarMenuItem.setLongClickEnabled(false);
            this.menuItem.setIcon(2131165453);
            this.menuItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131624003));
            this.menuItem.addSubItem(1, 2131165714, LocaleController.getString("OpenInEditor", 2131627100));
            this.menuItem.addSubItem(2, 2131165937, LocaleController.getString("ShareTheme", 2131628286));
            this.menuItem.addSubItem(3, 2131165702, LocaleController.getString("DeleteTheme", 2131625434));
            this.menuItem.setMenuYOffset(-AndroidUtilities.dp(80.0f));
            this.menuItem.setSubMenuOpenSide(2);
            this.menuItem.setDelegate(new ColorPicker$$ExternalSyntheticLambda6(this));
            this.menuItem.setAdditionalYOffset(AndroidUtilities.dp(72.0f));
            this.menuItem.setTranslationX(AndroidUtilities.dp(6.0f));
            this.menuItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 1));
            addView(this.menuItem, LayoutHelper.createFrame(30, 30.0f, 53, 0.0f, 2.0f, 10.0f, 0.0f));
            this.menuItem.setOnClickListener(new ColorPicker$$ExternalSyntheticLambda1(this));
        }
        updateColorsPosition(null, 0, false, getMeasuredWidth());
    }

    /* renamed from: org.telegram.ui.Components.ColorPicker$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends LinearLayout {
        private Paint paint;
        private RectF rect = new RectF();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            ColorPicker.this = r1;
            Paint paint = new Paint(1);
            this.paint = paint;
            paint.setColor(Theme.getColor("dialogBackgroundGray"));
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onDraw(Canvas canvas) {
            int left = ColorPicker.this.colorEditText[0].getLeft() - AndroidUtilities.dp(13.0f);
            this.rect.set(left, AndroidUtilities.dp(5.0f), left + ((int) (AndroidUtilities.dp(91.0f) + (ColorPicker.this.clearButton.getVisibility() == 0 ? AndroidUtilities.dp(25.0f) * ColorPicker.this.clearButton.getAlpha() : 0.0f))), AndroidUtilities.dp(37.0f));
            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), this.paint);
        }
    }

    public /* synthetic */ void lambda$new$0(View view) {
        RadioButton radioButton = (RadioButton) view;
        int i = 0;
        while (true) {
            RadioButton[] radioButtonArr = this.radioButton;
            if (i < radioButtonArr.length) {
                boolean z = radioButtonArr[i] == radioButton;
                radioButtonArr[i].setChecked(z, true);
                if (z) {
                    this.selectedColor = i;
                }
                i++;
            } else {
                int color = radioButton.getColor();
                setColorInner(color);
                this.colorEditText[1].setText(String.format("%02x%02x%02x", Byte.valueOf((byte) Color.red(color)), Byte.valueOf((byte) Color.green(color)), Byte.valueOf((byte) Color.blue(color))).toUpperCase());
                return;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ColorPicker$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends EditTextBoldCursor {
        final /* synthetic */ int val$num;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context, int i) {
            super(context);
            ColorPicker.this = r1;
            this.val$num = i;
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (getAlpha() == 1.0f && motionEvent.getAction() == 0) {
                if (ColorPicker.this.colorEditText[this.val$num + 1].isFocused()) {
                    AndroidUtilities.showKeyboard(ColorPicker.this.colorEditText[this.val$num + 1]);
                } else {
                    ColorPicker.this.colorEditText[this.val$num + 1].requestFocus();
                }
            }
            return false;
        }
    }

    /* renamed from: org.telegram.ui.Components.ColorPicker$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends EditTextBoldCursor {
        final /* synthetic */ int val$num;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context, int i) {
            super(context);
            ColorPicker.this = r1;
            this.val$num = i;
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (getAlpha() != 1.0f) {
                return false;
            }
            if (!isFocused()) {
                requestFocus();
                return false;
            }
            AndroidUtilities.showKeyboard(this);
            return super.onTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public boolean getGlobalVisibleRect(android.graphics.Rect rect, android.graphics.Point point) {
            boolean globalVisibleRect = super.getGlobalVisibleRect(rect, point);
            rect.bottom += AndroidUtilities.dp(40.0f);
            return globalVisibleRect;
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            ColorPicker.this.colorEditText[this.val$num - 1].invalidate();
        }
    }

    /* renamed from: org.telegram.ui.Components.ColorPicker$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 implements TextWatcher {
        final /* synthetic */ int val$num;

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass4(int i) {
            ColorPicker.this = r1;
            this.val$num = i;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            ColorPicker colorPicker = ColorPicker.this;
            if (colorPicker.ignoreTextChange) {
                return;
            }
            colorPicker.ignoreTextChange = true;
            int i = 0;
            while (i < editable.length()) {
                char charAt = editable.charAt(i);
                if ((charAt < '0' || charAt > '9') && ((charAt < 'a' || charAt > 'f') && (charAt < 'A' || charAt > 'F'))) {
                    editable.replace(i, i + 1, "");
                    i--;
                }
                i++;
            }
            if (editable.length() == 0) {
                ColorPicker.this.ignoreTextChange = false;
                return;
            }
            ColorPicker colorPicker2 = ColorPicker.this;
            colorPicker2.setColorInner(colorPicker2.getFieldColor(this.val$num, -1));
            int color = ColorPicker.this.getColor();
            if (editable.length() == 6) {
                editable.replace(0, editable.length(), String.format("%02x%02x%02x", Byte.valueOf((byte) Color.red(color)), Byte.valueOf((byte) Color.green(color)), Byte.valueOf((byte) Color.blue(color))).toUpperCase());
                ColorPicker.this.colorEditText[this.val$num].setSelection(editable.length());
            }
            ColorPicker.this.radioButton[ColorPicker.this.selectedColor].setColor(color);
            ColorPicker.this.delegate.setColor(color, ColorPicker.this.selectedColor, true);
            ColorPicker.this.ignoreTextChange = false;
        }
    }

    public static /* synthetic */ boolean lambda$new$1(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            AndroidUtilities.hideKeyboard(textView);
            return true;
        }
        return false;
    }

    public /* synthetic */ void lambda$new$2(View view) {
        if (this.colorsAnimator != null) {
            return;
        }
        int i = this.colorsCount;
        if (i == 1) {
            if (this.radioButton[1].getColor() == 0) {
                RadioButton[] radioButtonArr = this.radioButton;
                radioButtonArr[1].setColor(generateGradientColors(radioButtonArr[0].getColor()));
            }
            if (this.myMessagesColor) {
                this.delegate.setColor(this.radioButton[0].getColor(), 0, true);
            }
            this.delegate.setColor(this.radioButton[1].getColor(), 1, true);
            this.colorsCount = 2;
        } else if (i == 2) {
            this.colorsCount = 3;
            if (this.radioButton[2].getColor() == 0) {
                float[] fArr = new float[3];
                Color.colorToHSV(this.radioButton[0].getColor(), fArr);
                if (fArr[0] > 180.0f) {
                    fArr[0] = fArr[0] - 60.0f;
                } else {
                    fArr[0] = fArr[0] + 60.0f;
                }
                this.radioButton[2].setColor(Color.HSVToColor(255, fArr));
            }
            this.delegate.setColor(this.radioButton[2].getColor(), 2, true);
        } else if (i != 3) {
            return;
        } else {
            this.colorsCount = 4;
            if (this.radioButton[3].getColor() == 0) {
                RadioButton[] radioButtonArr2 = this.radioButton;
                radioButtonArr2[3].setColor(generateGradientColors(radioButtonArr2[2].getColor()));
            }
            this.delegate.setColor(this.radioButton[3].getColor(), 3, true);
        }
        ArrayList<Animator> arrayList = new ArrayList<>();
        if (this.colorsCount < this.maxColorsCount) {
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.ALPHA, 1.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.SCALE_X, 1.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.SCALE_Y, 1.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.TRANSLATION_X, (AndroidUtilities.dp(30.0f) * (this.colorsCount - 1)) + (AndroidUtilities.dp(13.0f) * (this.colorsCount - 1))));
        } else {
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.TRANSLATION_X, (AndroidUtilities.dp(30.0f) * (this.colorsCount - 1)) + (AndroidUtilities.dp(13.0f) * (this.colorsCount - 1))));
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.ALPHA, 0.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.SCALE_X, 0.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.SCALE_Y, 0.0f));
        }
        if (this.colorsCount > 1) {
            if (this.clearButton.getVisibility() != 0) {
                this.clearButton.setScaleX(0.0f);
                this.clearButton.setScaleY(0.0f);
            }
            this.clearButton.setVisibility(0);
            arrayList.add(ObjectAnimator.ofFloat(this.clearButton, View.ALPHA, 1.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.clearButton, View.SCALE_X, 1.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.clearButton, View.SCALE_Y, 1.0f));
        }
        this.radioButton[this.colorsCount - 1].callOnClick();
        this.colorsAnimator = new AnimatorSet();
        updateColorsPosition(arrayList, 0, false, getMeasuredWidth());
        this.colorsAnimator.playTogether(arrayList);
        this.colorsAnimator.setDuration(180L);
        this.colorsAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        this.colorsAnimator.addListener(new AnonymousClass5());
        this.colorsAnimator.start();
    }

    /* renamed from: org.telegram.ui.Components.ColorPicker$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends AnimatorListenerAdapter {
        AnonymousClass5() {
            ColorPicker.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ColorPicker.this.colorsCount == ColorPicker.this.maxColorsCount) {
                ColorPicker.this.addButton.setVisibility(4);
            }
            ColorPicker.this.colorsAnimator = null;
        }
    }

    /* renamed from: org.telegram.ui.Components.ColorPicker$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends ImageView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass6(Context context) {
            super(context);
            ColorPicker.this = r1;
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            super.setAlpha(f);
            ColorPicker.this.linearLayout.invalidate();
        }
    }

    public /* synthetic */ void lambda$new$3(View view) {
        RadioButton[] radioButtonArr;
        if (this.colorsAnimator != null) {
            return;
        }
        ArrayList<Animator> arrayList = new ArrayList<>();
        int i = this.colorsCount;
        if (i == 2) {
            this.colorsCount = 1;
            arrayList.add(ObjectAnimator.ofFloat(this.clearButton, View.ALPHA, 0.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.clearButton, View.SCALE_X, 0.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.clearButton, View.SCALE_Y, 0.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.TRANSLATION_X, 0.0f));
        } else if (i == 3) {
            this.colorsCount = 2;
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.TRANSLATION_X, AndroidUtilities.dp(30.0f) + AndroidUtilities.dp(13.0f)));
        } else if (i != 4) {
            return;
        } else {
            this.colorsCount = 3;
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.TRANSLATION_X, (AndroidUtilities.dp(30.0f) * 2) + (AndroidUtilities.dp(13.0f) * 2)));
        }
        if (this.colorsCount < this.maxColorsCount) {
            this.addButton.setVisibility(0);
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.ALPHA, 1.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.SCALE_X, 1.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.SCALE_Y, 1.0f));
        } else {
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.ALPHA, 0.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.SCALE_X, 0.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.addButton, View.SCALE_Y, 0.0f));
        }
        int i2 = this.selectedColor;
        if (i2 != 3) {
            RadioButton radioButton = this.radioButton[i2];
            int i3 = i2 + 1;
            while (true) {
                radioButtonArr = this.radioButton;
                if (i3 >= radioButtonArr.length) {
                    break;
                }
                radioButtonArr[i3 - 1] = radioButtonArr[i3];
                i3++;
            }
            radioButtonArr[3] = radioButton;
        }
        this.radioButton[0].callOnClick();
        int i4 = 0;
        while (true) {
            RadioButton[] radioButtonArr2 = this.radioButton;
            if (i4 < radioButtonArr2.length) {
                if (i4 < this.colorsCount) {
                    this.delegate.setColor(radioButtonArr2[i4].getColor(), i4, i4 == this.radioButton.length - 1);
                } else {
                    this.delegate.setColor(0, i4, i4 == radioButtonArr2.length - 1);
                }
                i4++;
            } else {
                this.colorsAnimator = new AnimatorSet();
                updateColorsPosition(arrayList, this.selectedColor, true, getMeasuredWidth());
                this.colorsAnimator.playTogether(arrayList);
                this.colorsAnimator.setDuration(180L);
                this.colorsAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                this.colorsAnimator.addListener(new AnonymousClass7());
                this.colorsAnimator.start();
                return;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ColorPicker$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends AnimatorListenerAdapter {
        AnonymousClass7() {
            ColorPicker.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ColorPicker.this.colorsCount == 1) {
                ColorPicker.this.clearButton.setVisibility(4);
            }
            for (int i = 0; i < ColorPicker.this.radioButton.length; i++) {
                if (ColorPicker.this.radioButton[i].getTag(2131230836) == null) {
                    ColorPicker.this.radioButton[i].setVisibility(4);
                }
            }
            ColorPicker.this.colorsAnimator = null;
        }
    }

    public /* synthetic */ void lambda$new$5(int i) {
        boolean z = true;
        if (i != 1 && i != 2) {
            if (i != 3) {
                return;
            }
            this.delegate.deleteTheme();
            return;
        }
        ColorPickerDelegate colorPickerDelegate = this.delegate;
        if (i != 2) {
            z = false;
        }
        colorPickerDelegate.openThemeCreate(z);
    }

    public /* synthetic */ void lambda$new$6(View view) {
        this.menuItem.toggleSubMenu();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateColorsPosition(null, 0, false, getMeasuredWidth());
    }

    private void updateColorsPosition(ArrayList<Animator> arrayList, int i, boolean z, int i2) {
        int i3 = this.colorsCount;
        int left = this.radioContainer.getLeft() + (AndroidUtilities.dp(30.0f) * i3) + ((i3 - 1) * AndroidUtilities.dp(13.0f));
        int dp = i2 - AndroidUtilities.dp(this.currentResetType == 1 ? 50.0f : 0.0f);
        float f = left > dp ? left - dp : 0.0f;
        if (arrayList != null) {
            arrayList.add(ObjectAnimator.ofFloat(this.radioContainer, View.TRANSLATION_X, -f));
        } else {
            this.radioContainer.setTranslationX(-f);
        }
        int i4 = 0;
        int i5 = 0;
        while (true) {
            RadioButton[] radioButtonArr = this.radioButton;
            if (i4 < radioButtonArr.length) {
                boolean z2 = radioButtonArr[i4].getTag(2131230836) != null;
                if (i4 < this.colorsCount) {
                    this.radioButton[i4].setVisibility(0);
                    if (arrayList != null) {
                        if (!z2) {
                            arrayList.add(ObjectAnimator.ofFloat(this.radioButton[i4], View.ALPHA, 1.0f));
                            arrayList.add(ObjectAnimator.ofFloat(this.radioButton[i4], View.SCALE_X, 1.0f));
                            arrayList.add(ObjectAnimator.ofFloat(this.radioButton[i4], View.SCALE_Y, 1.0f));
                        }
                        if (z || (!z && i4 != this.colorsCount - 1)) {
                            arrayList.add(ObjectAnimator.ofFloat(this.radioButton[i4], View.TRANSLATION_X, i5));
                        } else {
                            this.radioButton[i4].setTranslationX(i5);
                        }
                    } else {
                        this.radioButton[i4].setVisibility(0);
                        if (this.colorsAnimator == null) {
                            this.radioButton[i4].setAlpha(1.0f);
                            this.radioButton[i4].setScaleX(1.0f);
                            this.radioButton[i4].setScaleY(1.0f);
                        }
                        this.radioButton[i4].setTranslationX(i5);
                    }
                    this.radioButton[i4].setTag(2131230836, 1);
                } else {
                    if (arrayList == null) {
                        this.radioButton[i4].setVisibility(4);
                        if (this.colorsAnimator == null) {
                            this.radioButton[i4].setAlpha(0.0f);
                            this.radioButton[i4].setScaleX(0.0f);
                            this.radioButton[i4].setScaleY(0.0f);
                        }
                    } else if (z2) {
                        arrayList.add(ObjectAnimator.ofFloat(this.radioButton[i4], View.ALPHA, 0.0f));
                        arrayList.add(ObjectAnimator.ofFloat(this.radioButton[i4], View.SCALE_X, 0.0f));
                        arrayList.add(ObjectAnimator.ofFloat(this.radioButton[i4], View.SCALE_Y, 0.0f));
                    }
                    if (!z) {
                        this.radioButton[i4].setTranslationX(i5);
                    }
                    this.radioButton[i4].setTag(2131230836, null);
                }
                i5 += AndroidUtilities.dp(30.0f) + AndroidUtilities.dp(13.0f);
                i4++;
            } else {
                return;
            }
        }
    }

    public void hideKeyboard() {
        AndroidUtilities.hideKeyboard(this.colorEditText[1]);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x009f  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x00a7  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float f;
        int i;
        float measuredWidth;
        int dp = AndroidUtilities.dp(45.0f);
        float f2 = dp;
        canvas.drawBitmap(this.colorWheelBitmap, 0.0f, f2, (Paint) null);
        int height = dp + this.colorWheelBitmap.getHeight();
        canvas.drawRect(0.0f, f2, getMeasuredWidth(), dp + 1, this.linePaint);
        canvas.drawRect(0.0f, height - 1, getMeasuredWidth(), height, this.linePaint);
        float[] fArr = this.hsvTemp;
        float[] fArr2 = this.colorHSV;
        fArr[0] = fArr2[0];
        fArr[1] = fArr2[1];
        fArr[2] = 1.0f;
        int measuredWidth2 = (int) ((fArr2[0] * getMeasuredWidth()) / 360.0f);
        int height2 = (int) (f2 + (this.colorWheelBitmap.getHeight() * (1.0f - this.colorHSV[1])));
        if (!this.circlePressed) {
            int dp2 = AndroidUtilities.dp(16.0f);
            float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.pressedMoveProgress);
            if (measuredWidth2 < dp2) {
                measuredWidth = measuredWidth2 + ((dp2 - measuredWidth2) * interpolation);
            } else {
                if (measuredWidth2 > getMeasuredWidth() - dp2) {
                    measuredWidth = measuredWidth2 - ((measuredWidth2 - (getMeasuredWidth() - dp2)) * interpolation);
                }
                if (height2 >= dp + dp2) {
                    height2 = (int) (height2 + (interpolation * (i - height2)));
                } else if (height2 > (this.colorWheelBitmap.getHeight() + dp) - dp2) {
                    height2 = (int) (height2 - (interpolation * (height2 - ((dp + this.colorWheelBitmap.getHeight()) - dp2))));
                }
            }
            measuredWidth2 = (int) measuredWidth;
            if (height2 >= dp + dp2) {
            }
        }
        drawPointerArrow(canvas, measuredWidth2, height2, Color.HSVToColor(this.hsvTemp), false);
        this.sliderRect.set(AndroidUtilities.dp(22.0f), AndroidUtilities.dp(26.0f) + height, getMeasuredWidth() - AndroidUtilities.dp(22.0f), height + AndroidUtilities.dp(34.0f));
        if (this.colorGradient == null) {
            float[] fArr3 = this.hsvTemp;
            fArr3[2] = this.minHsvBrightness;
            int HSVToColor = Color.HSVToColor(fArr3);
            float[] fArr4 = this.hsvTemp;
            fArr4[2] = this.maxHsvBrightness;
            int HSVToColor2 = Color.HSVToColor(fArr4);
            RectF rectF = this.sliderRect;
            float f3 = rectF.left;
            float f4 = rectF.top;
            LinearGradient linearGradient = new LinearGradient(f3, f4, rectF.right, f4, new int[]{HSVToColor2, HSVToColor}, (float[]) null, Shader.TileMode.CLAMP);
            this.colorGradient = linearGradient;
            this.valueSliderPaint.setShader(linearGradient);
        }
        canvas.drawRoundRect(this.sliderRect, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.valueSliderPaint);
        if (this.minHsvBrightness == this.maxHsvBrightness) {
            f = 0.5f;
        } else {
            float brightness = getBrightness();
            float f5 = this.minHsvBrightness;
            f = (brightness - f5) / (this.maxHsvBrightness - f5);
        }
        RectF rectF2 = this.sliderRect;
        drawPointerArrow(canvas, (int) (rectF2.left + ((1.0f - f) * rectF2.width())), (int) this.sliderRect.centerY(), getColor(), true);
        if (this.circlePressed || this.pressedMoveProgress >= 1.0f) {
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = elapsedRealtime - this.lastUpdateTime;
        this.lastUpdateTime = elapsedRealtime;
        float f6 = this.pressedMoveProgress + (((float) j) / 180.0f);
        this.pressedMoveProgress = f6;
        if (f6 > 1.0f) {
            this.pressedMoveProgress = 1.0f;
        }
        invalidate();
    }

    public int getFieldColor(int i, int i2) {
        try {
            return Integer.parseInt(this.colorEditText[i].getText().toString(), 16) | (-16777216);
        } catch (Exception unused) {
            return i2;
        }
    }

    private void drawPointerArrow(Canvas canvas, int i, int i2, int i3, boolean z) {
        int dp = AndroidUtilities.dp(z ? 12.0f : 16.0f);
        this.circleDrawable.setBounds(i - dp, i2 - dp, i + dp, dp + i2);
        this.circleDrawable.draw(canvas);
        this.circlePaint.setColor(-1);
        float f = i;
        float f2 = i2;
        canvas.drawCircle(f, f2, AndroidUtilities.dp(z ? 11.0f : 15.0f), this.circlePaint);
        this.circlePaint.setColor(i3);
        canvas.drawCircle(f, f2, AndroidUtilities.dp(z ? 9.0f : 13.0f), this.circlePaint);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        if (this.colorWheelWidth != i) {
            this.colorWheelWidth = i;
            this.colorWheelBitmap = createColorWheelBitmap(i, AndroidUtilities.dp(180.0f));
            this.colorGradient = null;
        }
    }

    private Bitmap createColorWheelBitmap(int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        float f = i;
        float f2 = i2;
        this.colorWheelPaint.setShader(new ComposeShader(new LinearGradient(0.0f, i2 / 3, 0.0f, f2, new int[]{-1, 0}, (float[]) null, Shader.TileMode.CLAMP), new LinearGradient(0.0f, 0.0f, f, 0.0f, new int[]{-65536, -256, -16711936, -16711681, -16776961, -65281, -65536}, (float[]) null, Shader.TileMode.CLAMP), PorterDuff.Mode.MULTIPLY));
        new Canvas(createBitmap).drawRect(0.0f, 0.0f, f, f2, this.colorWheelPaint);
        return createBitmap;
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00fa, code lost:
        if (r12 <= (r11.sliderRect.bottom + org.telegram.messenger.AndroidUtilities.dp(7.0f))) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x000b, code lost:
        if (r0 != 2) goto L7;
     */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0141  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float f;
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                this.colorPressed = false;
                this.circlePressed = false;
                this.lastUpdateTime = SystemClock.elapsedRealtime();
                invalidate();
            }
            return super.onTouchEvent(motionEvent);
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int dp = AndroidUtilities.dp(45.0f);
        float f2 = 0.0f;
        if (this.circlePressed || (!this.colorPressed && y >= dp && y <= this.colorWheelBitmap.getHeight() + dp)) {
            if (!this.circlePressed) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            this.circlePressed = true;
            this.pressedMoveProgress = 0.0f;
            this.lastUpdateTime = SystemClock.elapsedRealtime();
            x = Math.max(0, Math.min(x, this.colorWheelBitmap.getWidth()));
            y = Math.max(dp, Math.min(y, this.colorWheelBitmap.getHeight() + dp));
            if (this.minHsvBrightness == this.maxHsvBrightness) {
                f = 0.5f;
            } else {
                float brightness = getBrightness();
                float f3 = this.minHsvBrightness;
                f = (brightness - f3) / (this.maxHsvBrightness - f3);
            }
            this.colorHSV[0] = (x * 360.0f) / this.colorWheelBitmap.getWidth();
            this.colorHSV[1] = 1.0f - ((1.0f / this.colorWheelBitmap.getHeight()) * (y - dp));
            updateHsvMinMaxBrightness();
            this.colorHSV[2] = (this.minHsvBrightness * (1.0f - f)) + (this.maxHsvBrightness * f);
            this.colorGradient = null;
        }
        if (!this.colorPressed) {
            if (!this.circlePressed) {
                float f4 = x;
                RectF rectF = this.sliderRect;
                if (f4 >= rectF.left && f4 <= rectF.right) {
                    float f5 = y;
                    if (f5 >= rectF.top - AndroidUtilities.dp(7.0f)) {
                    }
                }
            }
            if (!this.colorPressed || this.circlePressed) {
                int color = getColor();
                if (!this.ignoreTextChange) {
                    int red = Color.red(color);
                    int green = Color.green(color);
                    int blue = Color.blue(color);
                    this.ignoreTextChange = true;
                    String upperCase = String.format("%02x%02x%02x", Byte.valueOf((byte) red), Byte.valueOf((byte) green), Byte.valueOf((byte) blue)).toUpperCase();
                    Editable text = this.colorEditText[1].getText();
                    text.replace(0, text.length(), upperCase);
                    this.radioButton[this.selectedColor].setColor(color);
                    this.ignoreTextChange = false;
                }
                this.delegate.setColor(color, this.selectedColor, false);
                invalidate();
            }
            return true;
        }
        float f6 = x;
        RectF rectF2 = this.sliderRect;
        float width = 1.0f - ((f6 - rectF2.left) / rectF2.width());
        if (width >= 0.0f) {
            f2 = width > 1.0f ? 1.0f : width;
        }
        this.colorHSV[2] = (this.minHsvBrightness * (1.0f - f2)) + (this.maxHsvBrightness * f2);
        if (!this.colorPressed) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        this.colorPressed = true;
        if (!this.colorPressed) {
        }
        int color2 = getColor();
        if (!this.ignoreTextChange) {
        }
        this.delegate.setColor(color2, this.selectedColor, false);
        invalidate();
        return true;
    }

    public void setColorInner(int i) {
        Color.colorToHSV(i, this.colorHSV);
        int defaultColor = this.delegate.getDefaultColor(this.selectedColor);
        if (defaultColor == 0 || defaultColor != i) {
            updateHsvMinMaxBrightness();
        }
        this.colorGradient = null;
        invalidate();
    }

    public void setColor(int i, int i2) {
        if (!this.ignoreTextChange) {
            this.ignoreTextChange = true;
            if (this.selectedColor == i2) {
                String upperCase = String.format("%02x%02x%02x", Byte.valueOf((byte) Color.red(i)), Byte.valueOf((byte) Color.green(i)), Byte.valueOf((byte) Color.blue(i))).toUpperCase();
                this.colorEditText[1].setText(upperCase);
                this.colorEditText[1].setSelection(upperCase.length());
            }
            this.radioButton[i2].setColor(i);
            this.ignoreTextChange = false;
        }
        setColorInner(i);
    }

    public void setHasChanges(boolean z) {
        if (!z || this.resetButton.getTag() == null) {
            if ((!z && this.resetButton.getTag() == null) || this.clearButton.getTag() != null) {
                return;
            }
            this.resetButton.setTag(z ? 1 : null);
            AnimatorSet animatorSet = new AnimatorSet();
            ArrayList arrayList = new ArrayList();
            if (z) {
                this.resetButton.setVisibility(0);
            }
            TextView textView = this.resetButton;
            Property property = View.ALPHA;
            float[] fArr = new float[1];
            fArr[0] = z ? 1.0f : 0.0f;
            arrayList.add(ObjectAnimator.ofFloat(textView, property, fArr));
            animatorSet.addListener(new AnonymousClass8(z));
            animatorSet.playTogether(arrayList);
            animatorSet.setDuration(180L);
            animatorSet.start();
        }
    }

    /* renamed from: org.telegram.ui.Components.ColorPicker$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$value;

        AnonymousClass8(boolean z) {
            ColorPicker.this = r1;
            this.val$value = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!this.val$value) {
                ColorPicker.this.resetButton.setVisibility(8);
            }
        }
    }

    public void setType(int i, boolean z, int i2, int i3, boolean z2, int i4, boolean z3) {
        if (i != this.currentResetType) {
            this.selectedColor = 0;
            int i5 = 0;
            while (i5 < 4) {
                this.radioButton[i5].setChecked(i5 == this.selectedColor, true);
                i5++;
            }
        }
        this.maxColorsCount = i2;
        this.currentResetType = i;
        this.myMessagesColor = z2;
        this.colorsCount = i3;
        if (i3 == 1) {
            this.addButton.setTranslationX(0.0f);
        } else if (i3 == 2) {
            this.addButton.setTranslationX(AndroidUtilities.dp(30.0f) + AndroidUtilities.dp(13.0f));
        } else if (i3 == 3) {
            this.addButton.setTranslationX((AndroidUtilities.dp(30.0f) * 2) + (AndroidUtilities.dp(13.0f) * 2));
        } else {
            this.addButton.setTranslationX((AndroidUtilities.dp(30.0f) * 3) + (AndroidUtilities.dp(13.0f) * 3));
        }
        ActionBarMenuItem actionBarMenuItem = this.menuItem;
        if (actionBarMenuItem != null) {
            if (i == 1) {
                actionBarMenuItem.setVisibility(0);
            } else {
                actionBarMenuItem.setVisibility(8);
                this.clearButton.setTranslationX(0.0f);
            }
        }
        if (i2 <= 1) {
            this.addButton.setVisibility(8);
            this.clearButton.setVisibility(8);
        } else {
            if (i3 < i2) {
                this.addButton.setVisibility(0);
                this.addButton.setScaleX(1.0f);
                this.addButton.setScaleY(1.0f);
                this.addButton.setAlpha(1.0f);
            } else {
                this.addButton.setVisibility(8);
            }
            if (i3 > 1) {
                this.clearButton.setVisibility(0);
                this.clearButton.setScaleX(1.0f);
                this.clearButton.setScaleY(1.0f);
                this.clearButton.setAlpha(1.0f);
            } else {
                this.clearButton.setVisibility(8);
            }
        }
        this.linearLayout.invalidate();
        ArrayList arrayList = null;
        updateColorsPosition(null, 0, false, getMeasuredWidth());
        if (z3) {
            arrayList = new ArrayList();
        }
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        animatorSet.setDuration(180L);
        animatorSet.addListener(new AnonymousClass9(i2));
        animatorSet.start();
    }

    /* renamed from: org.telegram.ui.Components.ColorPicker$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$maxColorsCount;

        AnonymousClass9(int i) {
            ColorPicker.this = r1;
            this.val$maxColorsCount = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.val$maxColorsCount <= 1) {
                ColorPicker.this.clearButton.setVisibility(8);
            }
        }
    }

    public int getColor() {
        float[] fArr = this.hsvTemp;
        float[] fArr2 = this.colorHSV;
        fArr[0] = fArr2[0];
        fArr[1] = fArr2[1];
        fArr[2] = getBrightness();
        return (Color.HSVToColor(this.hsvTemp) & 16777215) | (-16777216);
    }

    private float getBrightness() {
        return Math.max(this.minHsvBrightness, Math.min(this.colorHSV[2], this.maxHsvBrightness));
    }

    private void updateHsvMinMaxBrightness() {
        ImageView imageView = this.clearButton;
        if (imageView == null) {
            return;
        }
        float f = imageView.getTag() != null ? 0.0f : this.minBrightness;
        float f2 = this.clearButton.getTag() != null ? 1.0f : this.maxBrightness;
        float[] fArr = this.colorHSV;
        float f3 = fArr[2];
        if (f == 0.0f && f2 == 1.0f) {
            this.minHsvBrightness = 0.0f;
            this.maxHsvBrightness = 1.0f;
            return;
        }
        fArr[2] = 1.0f;
        int HSVToColor = Color.HSVToColor(fArr);
        this.colorHSV[2] = f3;
        float computePerceivedBrightness = AndroidUtilities.computePerceivedBrightness(HSVToColor);
        float max = Math.max(0.0f, Math.min(f / computePerceivedBrightness, 1.0f));
        this.minHsvBrightness = max;
        this.maxHsvBrightness = Math.max(max, Math.min(f2 / computePerceivedBrightness, 1.0f));
    }

    public void setMinBrightness(float f) {
        this.minBrightness = f;
        updateHsvMinMaxBrightness();
    }

    public void setMaxBrightness(float f) {
        this.maxBrightness = f;
        updateHsvMinMaxBrightness();
    }

    public void provideThemeDescriptions(List<ThemeDescription> list) {
        for (int i = 0; i < this.colorEditText.length; i++) {
            list.add(new ThemeDescription(this.colorEditText[i], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
            list.add(new ThemeDescription(this.colorEditText[i], ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
            list.add(new ThemeDescription(this.colorEditText[i], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
            list.add(new ThemeDescription(this.colorEditText[i], ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "windowBackgroundWhiteBlueHeader"));
            list.add(new ThemeDescription(this.colorEditText[i], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
            list.add(new ThemeDescription(this.colorEditText[i], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
        }
        list.add(new ThemeDescription(this.clearButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription(this.clearButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "dialogButtonSelector"));
        if (this.menuItem != null) {
            ColorPicker$$ExternalSyntheticLambda7 colorPicker$$ExternalSyntheticLambda7 = new ColorPicker$$ExternalSyntheticLambda7(this);
            list.add(new ThemeDescription(this.menuItem, 0, null, null, null, colorPicker$$ExternalSyntheticLambda7, "windowBackgroundWhiteBlackText"));
            list.add(new ThemeDescription(this.menuItem, 0, null, null, null, colorPicker$$ExternalSyntheticLambda7, "dialogButtonSelector"));
            list.add(new ThemeDescription(this.menuItem, 0, null, null, null, colorPicker$$ExternalSyntheticLambda7, "actionBarDefaultSubmenuItem"));
            list.add(new ThemeDescription(this.menuItem, 0, null, null, null, colorPicker$$ExternalSyntheticLambda7, "actionBarDefaultSubmenuItemIcon"));
            list.add(new ThemeDescription(this.menuItem, 0, null, null, null, colorPicker$$ExternalSyntheticLambda7, "actionBarDefaultSubmenuBackground"));
        }
    }

    public /* synthetic */ void lambda$provideThemeDescriptions$7() {
        this.menuItem.setIconColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        Theme.setDrawableColor(this.menuItem.getBackground(), Theme.getColor("dialogButtonSelector"));
        this.menuItem.setPopupItemsColor(Theme.getColor("actionBarDefaultSubmenuItem"), false);
        this.menuItem.setPopupItemsColor(Theme.getColor("actionBarDefaultSubmenuItemIcon"), true);
        this.menuItem.redrawPopup(Theme.getColor("actionBarDefaultSubmenuBackground"));
    }

    public static int generateGradientColors(int i) {
        float[] fArr = new float[3];
        Color.colorToHSV(i, fArr);
        if (fArr[1] > 0.5f) {
            fArr[1] = fArr[1] - 0.15f;
        } else {
            fArr[1] = fArr[1] + 0.15f;
        }
        if (fArr[0] > 180.0f) {
            fArr[0] = fArr[0] - 20.0f;
        } else {
            fArr[0] = fArr[0] + 20.0f;
        }
        return Color.HSVToColor(255, fArr);
    }
}
