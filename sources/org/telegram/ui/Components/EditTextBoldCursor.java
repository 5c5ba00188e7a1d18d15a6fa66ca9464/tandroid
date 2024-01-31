package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import androidx.annotation.Keep;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLoaderPriorityQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.XiaomiUtilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.FloatingActionMode;
import org.telegram.ui.ActionBar.FloatingToolbar;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.QuoteSpan;
/* loaded from: classes4.dex */
public class EditTextBoldCursor extends EditTextEffects {
    private static Class editorClass;
    private static Method getVerticalOffsetMethod;
    private static Field mCursorDrawableResField;
    private static Field mEditor;
    private static Method mEditorInvalidateDisplayList;
    private static Field mScrollYField;
    private static boolean mScrollYGet;
    private static Field mShowCursorField;
    private int activeLineColor;
    private Paint activeLinePaint;
    private float activeLineWidth;
    private boolean allowDrawCursor;
    private View attachedToWindow;
    private boolean currentDrawHintAsHeader;
    ShapeDrawable cursorDrawable;
    private boolean cursorDrawn;
    private int cursorSize;
    private float cursorWidth;
    public Utilities.Callback2<Canvas, Runnable> drawHint;
    boolean drawInMaim;
    private Object editor;
    private int errorLineColor;
    private TextPaint errorPaint;
    private CharSequence errorText;
    public FloatingActionMode floatingActionMode;
    private FloatingToolbar floatingToolbar;
    private ViewTreeObserver.OnPreDrawListener floatingToolbarPreDrawListener;
    private boolean forceCursorEnd;
    private GradientDrawable gradientDrawable;
    private float headerAnimationProgress;
    private int headerHintColor;
    private AnimatorSet headerTransformAnimation;
    private CharSequence hint;
    private float hintAlpha;
    private AnimatedTextView.AnimatedTextDrawable hintAnimatedDrawable;
    private AnimatedTextView.AnimatedTextDrawable hintAnimatedDrawable2;
    private SubstringLayoutAnimator hintAnimator;
    private int hintColor;
    private long hintLastUpdateTime;
    private StaticLayout hintLayout;
    public float hintLayoutX;
    public boolean hintLayoutYFix;
    private boolean hintVisible;
    private int ignoreBottomCount;
    private int ignoreTopCount;
    private Runnable invalidateRunnable;
    private boolean isTextWatchersSuppressed;
    private float lastLineActiveness;
    private int lastSize;
    private int lastTouchX;
    private boolean lineActive;
    private float lineActiveness;
    private int lineColor;
    private long lineLastUpdateTime;
    private Paint linePaint;
    private float lineSpacingExtra;
    private boolean lineVisible;
    private float lineY;
    private android.graphics.Rect mTempRect;
    private boolean nextSetTextAnimated;
    private Runnable onPremiumMenuLockClickListener;
    private android.graphics.Rect padding;
    private android.graphics.Rect rect;
    private List<TextWatcher> registeredTextWatchers;
    float rightHintOffset;
    private int scrollY;
    private boolean supportRtlHint;
    private boolean transformHintToHeader;
    private View windowView;

    /* JADX INFO: Access modifiers changed from: protected */
    public void extendActionMode(ActionMode actionMode, Menu menu) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getActionModeStyle() {
        return 1;
    }

    @Override // android.widget.TextView, android.view.View
    @TargetApi(26)
    public int getAutofillType() {
        return 0;
    }

    protected Theme.ResourcesProvider getResourcesProvider() {
        return null;
    }

    public void setHintText2(CharSequence charSequence, boolean z) {
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.hintAnimatedDrawable2;
        if (animatedTextDrawable != null) {
            animatedTextDrawable.setText(charSequence, !LocaleController.isRTL && z);
        }
    }

    public void setHintRightOffset(int i) {
        float f = i;
        if (this.rightHintOffset == f) {
            return;
        }
        this.rightHintOffset = f;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @TargetApi(23)
    /* loaded from: classes4.dex */
    public class ActionModeCallback2Wrapper extends ActionMode.Callback2 {
        private final ActionMode.Callback mWrapped;

        public ActionModeCallback2Wrapper(ActionMode.Callback callback) {
            this.mWrapped = callback;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            EditTextBoldCursor.this.cleanupFloatingActionModeViews();
            EditTextBoldCursor.this.floatingActionMode = null;
        }

        @Override // android.view.ActionMode.Callback2
        public void onGetContentRect(ActionMode actionMode, View view, android.graphics.Rect rect) {
            ActionMode.Callback callback = this.mWrapped;
            if (callback instanceof ActionMode.Callback2) {
                ((ActionMode.Callback2) callback).onGetContentRect(actionMode, view, rect);
            } else {
                super.onGetContentRect(actionMode, view, rect);
            }
        }
    }

    public EditTextBoldCursor(Context context) {
        super(context);
        this.invalidateRunnable = new Runnable() { // from class: org.telegram.ui.Components.EditTextBoldCursor.1
            @Override // java.lang.Runnable
            public void run() {
                EditTextBoldCursor.this.invalidate();
                if (EditTextBoldCursor.this.attachedToWindow != null) {
                    AndroidUtilities.runOnUIThread(this, 500L);
                }
            }
        };
        this.rect = new android.graphics.Rect();
        this.hintVisible = true;
        this.hintAlpha = 1.0f;
        this.allowDrawCursor = true;
        this.forceCursorEnd = false;
        this.cursorWidth = 2.0f;
        this.lineVisible = false;
        this.lineActive = false;
        this.lineActiveness = 0.0f;
        this.lastLineActiveness = 0.0f;
        this.activeLineWidth = 0.0f;
        this.registeredTextWatchers = new ArrayList();
        this.isTextWatchersSuppressed = false;
        this.padding = new android.graphics.Rect();
        this.lastTouchX = -1;
        if (Build.VERSION.SDK_INT >= 26) {
            setImportantForAutofill(2);
        }
        init();
    }

    public void useAnimatedTextDrawable() {
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable() { // from class: org.telegram.ui.Components.EditTextBoldCursor.2
            @Override // android.graphics.drawable.Drawable
            public void invalidateSelf() {
                EditTextBoldCursor.this.invalidate();
            }
        };
        this.hintAnimatedDrawable = animatedTextDrawable;
        animatedTextDrawable.setEllipsizeByGradient(true);
        this.hintAnimatedDrawable.setTextColor(this.hintColor);
        this.hintAnimatedDrawable.setTextSize(getPaint().getTextSize());
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = new AnimatedTextView.AnimatedTextDrawable() { // from class: org.telegram.ui.Components.EditTextBoldCursor.3
            @Override // android.graphics.drawable.Drawable
            public void invalidateSelf() {
                EditTextBoldCursor.this.invalidate();
            }
        };
        this.hintAnimatedDrawable2 = animatedTextDrawable2;
        animatedTextDrawable2.setGravity(5);
        this.hintAnimatedDrawable2.setTextColor(this.hintColor);
        this.hintAnimatedDrawable2.setTextSize(getPaint().getTextSize());
    }

    @Override // android.widget.TextView
    public void addTextChangedListener(TextWatcher textWatcher) {
        this.registeredTextWatchers.add(textWatcher);
        if (this.isTextWatchersSuppressed) {
            return;
        }
        super.addTextChangedListener(textWatcher);
    }

    @Override // android.widget.TextView
    public void removeTextChangedListener(TextWatcher textWatcher) {
        this.registeredTextWatchers.remove(textWatcher);
        if (this.isTextWatchersSuppressed) {
            return;
        }
        super.removeTextChangedListener(textWatcher);
    }

    public void dispatchTextWatchersTextChanged() {
        for (TextWatcher textWatcher : this.registeredTextWatchers) {
            textWatcher.beforeTextChanged("", 0, length(), length());
            textWatcher.onTextChanged(getText(), 0, length(), length());
            textWatcher.afterTextChanged(getText());
        }
    }

    public void setTextWatchersSuppressed(boolean z, boolean z2) {
        if (this.isTextWatchersSuppressed == z) {
            return;
        }
        this.isTextWatchersSuppressed = z;
        if (z) {
            for (TextWatcher textWatcher : this.registeredTextWatchers) {
                super.removeTextChangedListener(textWatcher);
            }
            return;
        }
        for (TextWatcher textWatcher2 : this.registeredTextWatchers) {
            super.addTextChangedListener(textWatcher2);
            if (z2) {
                textWatcher2.beforeTextChanged("", 0, length(), length());
                textWatcher2.onTextChanged(getText(), 0, length(), length());
                textWatcher2.afterTextChanged(getText());
            }
        }
    }

    @Override // android.widget.TextView
    public Drawable getTextCursorDrawable() {
        if (this.cursorDrawable != null) {
            return super.getTextCursorDrawable();
        }
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape()) { // from class: org.telegram.ui.Components.EditTextBoldCursor.4
            @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                super.draw(canvas);
                EditTextBoldCursor.this.cursorDrawn = true;
            }
        };
        shapeDrawable.getPaint().setColor(0);
        return shapeDrawable;
    }

    @SuppressLint({"PrivateApi"})
    private void init() {
        this.linePaint = new Paint();
        this.activeLinePaint = new Paint();
        TextPaint textPaint = new TextPaint(1);
        this.errorPaint = textPaint;
        textPaint.setTextSize(AndroidUtilities.dp(11.0f));
        int i = Build.VERSION.SDK_INT;
        if (i >= 26) {
            setImportantForAutofill(2);
        }
        if (i >= 29) {
            ShapeDrawable shapeDrawable = new ShapeDrawable() { // from class: org.telegram.ui.Components.EditTextBoldCursor.5
                @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
                public void draw(Canvas canvas) {
                    EditTextBoldCursor editTextBoldCursor = EditTextBoldCursor.this;
                    if (editTextBoldCursor.drawInMaim) {
                        editTextBoldCursor.cursorDrawn = true;
                    } else {
                        super.draw(canvas);
                    }
                }

                @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
                public int getIntrinsicHeight() {
                    return AndroidUtilities.dp(EditTextBoldCursor.this.cursorSize + 20);
                }

                @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
                public int getIntrinsicWidth() {
                    return AndroidUtilities.dp(EditTextBoldCursor.this.cursorWidth);
                }
            };
            this.cursorDrawable = shapeDrawable;
            shapeDrawable.setShape(new RectShape());
            this.gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{-11230757, -11230757});
            setTextCursorDrawable(this.cursorDrawable);
        }
        try {
            if (!mScrollYGet && mScrollYField == null) {
                mScrollYGet = true;
                Field declaredField = View.class.getDeclaredField("mScrollY");
                mScrollYField = declaredField;
                declaredField.setAccessible(true);
            }
        } catch (Throwable unused) {
        }
        try {
            if (editorClass == null) {
                Field declaredField2 = TextView.class.getDeclaredField("mEditor");
                mEditor = declaredField2;
                declaredField2.setAccessible(true);
                Class<?> cls = Class.forName("android.widget.Editor");
                editorClass = cls;
                try {
                    Field declaredField3 = cls.getDeclaredField("mShowCursor");
                    mShowCursorField = declaredField3;
                    declaredField3.setAccessible(true);
                } catch (Exception unused2) {
                }
                try {
                    Method declaredMethod = editorClass.getDeclaredMethod("invalidateTextDisplayList", new Class[0]);
                    mEditorInvalidateDisplayList = declaredMethod;
                    declaredMethod.setAccessible(true);
                } catch (Exception unused3) {
                }
                Method declaredMethod2 = TextView.class.getDeclaredMethod("getVerticalOffset", Boolean.TYPE);
                getVerticalOffsetMethod = declaredMethod2;
                declaredMethod2.setAccessible(true);
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
        if (this.cursorDrawable == null) {
            try {
                GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{-11230757, -11230757});
                this.gradientDrawable = gradientDrawable;
                if (Build.VERSION.SDK_INT >= 29) {
                    setTextCursorDrawable(gradientDrawable);
                }
                this.editor = mEditor.get(this);
            } catch (Throwable unused4) {
            }
            try {
                if (mCursorDrawableResField == null) {
                    Field declaredField4 = TextView.class.getDeclaredField("mCursorDrawableRes");
                    mCursorDrawableResField = declaredField4;
                    declaredField4.setAccessible(true);
                }
                Field field = mCursorDrawableResField;
                if (field != null) {
                    field.set(this, Integer.valueOf(R.drawable.field_carret_empty));
                }
            } catch (Throwable unused5) {
            }
        }
        this.cursorSize = AndroidUtilities.dp(24.0f);
    }

    public void setTransformHintToHeader(boolean z) {
        if (this.transformHintToHeader == z) {
            return;
        }
        this.transformHintToHeader = z;
        AnimatorSet animatorSet = this.headerTransformAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.headerTransformAnimation = null;
        }
    }

    public void setAllowDrawCursor(boolean z) {
        this.allowDrawCursor = z;
        invalidate();
    }

    public void setForceCursorEnd(boolean z) {
        this.forceCursorEnd = z;
        invalidate();
    }

    public void setCursorWidth(float f) {
        this.cursorWidth = f;
    }

    public void setCursorColor(int i) {
        ShapeDrawable shapeDrawable = this.cursorDrawable;
        if (shapeDrawable != null) {
            shapeDrawable.getPaint().setColor(i);
        }
        GradientDrawable gradientDrawable = this.gradientDrawable;
        if (gradientDrawable != null) {
            gradientDrawable.setColor(i);
        }
        invalidate();
    }

    public void setCursorSize(int i) {
        this.cursorSize = i;
    }

    public void setErrorLineColor(int i) {
        this.errorLineColor = i;
        this.errorPaint.setColor(i);
        invalidate();
    }

    public void setLineColors(int i, int i2, int i3) {
        this.lineVisible = true;
        getContext().getResources().getDrawable(R.drawable.search_dark).getPadding(this.padding);
        android.graphics.Rect rect = this.padding;
        setPadding(rect.left, rect.top, rect.right, rect.bottom);
        this.lineColor = i;
        this.activeLineColor = i2;
        this.activeLinePaint.setColor(i2);
        this.errorLineColor = i3;
        this.errorPaint.setColor(i3);
        invalidate();
    }

    public void setHintVisible(boolean z, boolean z2) {
        if (this.hintVisible == z) {
            return;
        }
        this.hintLastUpdateTime = System.currentTimeMillis();
        this.hintVisible = z;
        if (!z2) {
            this.hintAlpha = z ? 1.0f : 0.0f;
        }
        invalidate();
    }

    public void setHintColor(int i) {
        this.hintColor = i;
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.hintAnimatedDrawable;
        if (animatedTextDrawable != null) {
            animatedTextDrawable.setTextColor(i);
        }
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = this.hintAnimatedDrawable2;
        if (animatedTextDrawable2 != null) {
            animatedTextDrawable2.setTextColor(this.hintColor);
        }
        invalidate();
    }

    public void setHeaderHintColor(int i) {
        this.headerHintColor = i;
        invalidate();
    }

    @Override // android.widget.TextView
    public void setTextSize(int i, float f) {
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.hintAnimatedDrawable;
        if (animatedTextDrawable != null) {
            animatedTextDrawable.setTextSize(AndroidUtilities.dp(f));
        }
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = this.hintAnimatedDrawable2;
        if (animatedTextDrawable2 != null) {
            animatedTextDrawable2.setTextSize(AndroidUtilities.dp(f));
        }
        super.setTextSize(i, f);
    }

    public void setNextSetTextAnimated(boolean z) {
        this.nextSetTextAnimated = z;
    }

    public void setErrorText(CharSequence charSequence) {
        if (TextUtils.equals(charSequence, this.errorText)) {
            return;
        }
        this.errorText = charSequence;
        requestLayout();
    }

    public boolean hasErrorText() {
        return !TextUtils.isEmpty(this.errorText);
    }

    public StaticLayout getErrorLayout(int i) {
        if (TextUtils.isEmpty(this.errorText)) {
            return null;
        }
        return new StaticLayout(this.errorText, this.errorPaint, i, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }

    public float getLineY() {
        return this.lineY;
    }

    public void setSupportRtlHint(boolean z) {
        this.supportRtlHint = z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        if (i != i3) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    @Override // org.telegram.ui.Components.EditTextEffects, android.widget.EditText, android.widget.TextView
    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        super.setText(charSequence, bufferType);
        checkHeaderVisibility(this.nextSetTextAnimated);
        this.nextSetTextAnimated = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredHeight = getMeasuredHeight() + (getMeasuredWidth() << 16);
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.hintAnimatedDrawable;
        if (animatedTextDrawable != null) {
            animatedTextDrawable.setBounds(getPaddingLeft(), getPaddingTop(), getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
        }
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = this.hintAnimatedDrawable2;
        if (animatedTextDrawable2 != null) {
            animatedTextDrawable2.setBounds(getPaddingLeft(), getPaddingTop(), getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
        }
        StaticLayout staticLayout = this.hintLayout;
        if (staticLayout != null && this.hintAnimatedDrawable == null) {
            if (this.lastSize != measuredHeight) {
                setHintText(this.hint, false, staticLayout.getPaint());
            }
            if (this.hintLayoutYFix) {
                this.lineY = (((getExtendedPaddingTop() + getPaddingTop()) + ((((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom()) - this.hintLayout.getHeight()) / 2.0f)) + this.hintLayout.getHeight()) - AndroidUtilities.dp(1.0f);
            } else {
                this.lineY = ((getMeasuredHeight() - this.hintLayout.getHeight()) / 2.0f) + this.hintLayout.getHeight() + AndroidUtilities.dp(6.0f);
            }
        } else {
            this.lineY = getMeasuredHeight() - AndroidUtilities.dp(2.0f);
        }
        this.lastSize = measuredHeight;
    }

    public void setHintText(CharSequence charSequence) {
        setHintText(charSequence, false, getPaint());
    }

    public void setHintText(CharSequence charSequence, boolean z) {
        setHintText(charSequence, z, getPaint());
    }

    public void setHintText(CharSequence charSequence, boolean z, TextPaint textPaint) {
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.hintAnimatedDrawable;
        if (animatedTextDrawable != null) {
            animatedTextDrawable.setText(charSequence, !LocaleController.isRTL);
            return;
        }
        if (charSequence == null) {
            charSequence = "";
        }
        if (getMeasuredWidth() == 0) {
            z = false;
        }
        if (z) {
            if (this.hintAnimator == null) {
                this.hintAnimator = new SubstringLayoutAnimator(this);
            }
            this.hintAnimator.create(this.hintLayout, this.hint, charSequence, textPaint);
        } else {
            SubstringLayoutAnimator substringLayoutAnimator = this.hintAnimator;
            if (substringLayoutAnimator != null) {
                substringLayoutAnimator.cancel();
            }
        }
        this.hint = charSequence;
        if (getMeasuredWidth() != 0) {
            charSequence = TextUtils.ellipsize(charSequence, textPaint, getMeasuredWidth(), TextUtils.TruncateAt.END);
            StaticLayout staticLayout = this.hintLayout;
            if (staticLayout != null && TextUtils.equals(staticLayout.getText(), charSequence)) {
                return;
            }
        }
        this.hintLayout = new StaticLayout(charSequence, textPaint, AndroidUtilities.dp(1000.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        invalidate();
    }

    public Layout getHintLayoutEx() {
        return this.hintLayout;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onFocusChanged(boolean z, int i, android.graphics.Rect rect) {
        try {
            super.onFocusChanged(z, i, rect);
        } catch (Exception e) {
            FileLog.e(e);
        }
        checkHeaderVisibility(true);
    }

    private void checkHeaderVisibility(boolean z) {
        boolean z2 = this.transformHintToHeader && (isFocused() || getText().length() > 0);
        if (this.currentDrawHintAsHeader != z2) {
            AnimatorSet animatorSet = this.headerTransformAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.headerTransformAnimation = null;
            }
            this.currentDrawHintAsHeader = z2;
            if (z) {
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.headerTransformAnimation = animatorSet2;
                Animator[] animatorArr = new Animator[1];
                float[] fArr = new float[1];
                fArr[0] = z2 ? 1.0f : 0.0f;
                animatorArr[0] = ObjectAnimator.ofFloat(this, "headerAnimationProgress", fArr);
                animatorSet2.playTogether(animatorArr);
                this.headerTransformAnimation.setDuration(200L);
                this.headerTransformAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.headerTransformAnimation.start();
            } else {
                this.headerAnimationProgress = z2 ? 1.0f : 0.0f;
            }
            invalidate();
        }
    }

    @Keep
    public void setHeaderAnimationProgress(float f) {
        this.headerAnimationProgress = f;
        invalidate();
    }

    @Keep
    public float getHeaderAnimationProgress() {
        return this.headerAnimationProgress;
    }

    @Override // android.widget.TextView
    public void setLineSpacing(float f, float f2) {
        super.setLineSpacing(f, f2);
        this.lineSpacingExtra = f;
    }

    @Override // android.widget.TextView
    public int getExtendedPaddingTop() {
        int i = this.ignoreTopCount;
        if (i != 0) {
            this.ignoreTopCount = i - 1;
            return 0;
        }
        return super.getExtendedPaddingTop();
    }

    @Override // android.widget.TextView
    public int getExtendedPaddingBottom() {
        int i = this.ignoreBottomCount;
        if (i != 0) {
            this.ignoreBottomCount = i - 1;
            int i2 = this.scrollY;
            if (i2 != Integer.MAX_VALUE) {
                return -i2;
            }
            return 0;
        }
        return super.getExtendedPaddingBottom();
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.lastTouchX = (int) motionEvent.getX();
        }
        return super.onTouchEvent(motionEvent);
    }

    public void invalidateForce() {
        invalidate();
        if (isHardwareAccelerated()) {
            try {
                if (mEditorInvalidateDisplayList != null) {
                    if (this.editor == null) {
                        this.editor = mEditor.get(this);
                    }
                    Object obj = this.editor;
                    if (obj != null) {
                        mEditorInvalidateDisplayList.invoke(obj, new Object[0]);
                    }
                }
            } catch (Exception unused) {
            }
        }
    }

    private void drawHint(final Canvas canvas) {
        if (length() == 0 || this.transformHintToHeader) {
            boolean z = this.hintVisible;
            if ((z && this.hintAlpha != 1.0f) || (!z && this.hintAlpha != 0.0f)) {
                long currentTimeMillis = System.currentTimeMillis();
                long j = currentTimeMillis - this.hintLastUpdateTime;
                if (j < 0 || j > 17) {
                    j = 17;
                }
                this.hintLastUpdateTime = currentTimeMillis;
                if (this.hintVisible) {
                    float f = this.hintAlpha + (((float) j) / 150.0f);
                    this.hintAlpha = f;
                    if (f > 1.0f) {
                        this.hintAlpha = 1.0f;
                    }
                } else {
                    float f2 = this.hintAlpha - (((float) j) / 150.0f);
                    this.hintAlpha = f2;
                    if (f2 < 0.0f) {
                        this.hintAlpha = 0.0f;
                    }
                }
                invalidate();
            }
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.hintAnimatedDrawable;
            if (animatedTextDrawable != null && !TextUtils.isEmpty(animatedTextDrawable.getText()) && (this.hintVisible || this.hintAlpha != 0.0f)) {
                if (this.hintAnimatedDrawable2 != null) {
                    if (this.hintAnimatedDrawable.getCurrentWidth() + this.hintAnimatedDrawable2.getCurrentWidth() < getMeasuredWidth()) {
                        canvas.save();
                        canvas.translate((this.hintAnimatedDrawable2.getCurrentWidth() - getMeasuredWidth()) + this.hintAnimatedDrawable.getCurrentWidth(), 0.0f);
                        this.hintAnimatedDrawable2.setAlpha((int) (Color.alpha(this.hintColor) * this.hintAlpha));
                        this.hintAnimatedDrawable2.draw(canvas);
                        canvas.restore();
                        this.hintAnimatedDrawable.setRightPadding(0.0f);
                    } else {
                        canvas.save();
                        canvas.translate(this.rightHintOffset, 0.0f);
                        this.hintAnimatedDrawable2.setAlpha((int) (Color.alpha(this.hintColor) * this.hintAlpha));
                        this.hintAnimatedDrawable2.draw(canvas);
                        canvas.restore();
                        this.hintAnimatedDrawable.setRightPadding((this.hintAnimatedDrawable2.getCurrentWidth() + AndroidUtilities.dp(2.0f)) - this.rightHintOffset);
                    }
                } else {
                    this.hintAnimatedDrawable.setRightPadding(0.0f);
                }
                this.hintAnimatedDrawable.setAlpha((int) (Color.alpha(this.hintColor) * this.hintAlpha));
                this.hintAnimatedDrawable.draw(canvas);
            } else if (this.hintLayout != null) {
                if (this.hintVisible || this.hintAlpha != 0.0f) {
                    int color = getPaint().getColor();
                    canvas.save();
                    float lineLeft = this.hintLayout.getLineLeft(0);
                    float lineWidth = this.hintLayout.getLineWidth(0);
                    int i = lineLeft != 0.0f ? (int) (0 - lineLeft) : 0;
                    if (this.supportRtlHint && LocaleController.isRTL) {
                        float scrollX = i + getScrollX() + (getMeasuredWidth() - lineWidth);
                        this.hintLayoutX = scrollX;
                        canvas.translate(scrollX, (this.lineY - this.hintLayout.getHeight()) - AndroidUtilities.dp(7.0f));
                    } else {
                        float scrollX2 = i + getScrollX();
                        this.hintLayoutX = scrollX2;
                        canvas.translate(scrollX2, (this.lineY - this.hintLayout.getHeight()) - AndroidUtilities.dp2(7.0f));
                    }
                    if (this.transformHintToHeader) {
                        float f3 = 1.0f - (this.headerAnimationProgress * 0.3f);
                        if (this.supportRtlHint && LocaleController.isRTL) {
                            float f4 = lineWidth + lineLeft;
                            canvas.translate(f4 - (f4 * f3), 0.0f);
                        } else if (lineLeft != 0.0f) {
                            canvas.translate(lineLeft * (1.0f - f3), 0.0f);
                        }
                        canvas.scale(f3, f3);
                        canvas.translate(0.0f, (-AndroidUtilities.dp(22.0f)) * this.headerAnimationProgress);
                        getPaint().setColor(ColorUtils.blendARGB(this.hintColor, this.headerHintColor, this.headerAnimationProgress));
                    } else {
                        getPaint().setColor(this.hintColor);
                        getPaint().setAlpha((int) (this.hintAlpha * 255.0f * (Color.alpha(this.hintColor) / 255.0f)));
                    }
                    SubstringLayoutAnimator substringLayoutAnimator = this.hintAnimator;
                    if (substringLayoutAnimator != null && substringLayoutAnimator.animateTextChange) {
                        canvas.save();
                        canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                        this.hintAnimator.draw(canvas, getPaint());
                        canvas.restore();
                    } else {
                        Utilities.Callback2<Canvas, Runnable> callback2 = this.drawHint;
                        if (callback2 != null) {
                            callback2.run(canvas, new Runnable() { // from class: org.telegram.ui.Components.EditTextBoldCursor$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    EditTextBoldCursor.this.lambda$drawHint$0(canvas);
                                }
                            });
                        } else {
                            this.hintLayout.draw(canvas);
                        }
                    }
                    getPaint().setColor(color);
                    canvas.restore();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$drawHint$0(Canvas canvas) {
        this.hintLayout.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0117 A[Catch: all -> 0x0140, TryCatch #0 {all -> 0x0140, blocks: (B:31:0x006c, B:33:0x0070, B:35:0x0074, B:37:0x0086, B:42:0x0094, B:45:0x009a, B:47:0x00a1, B:49:0x00a9, B:54:0x00cf, B:56:0x0117, B:58:0x011a, B:59:0x011f, B:50:0x00bc, B:52:0x00c4, B:41:0x0090), top: B:145:0x006c }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01ce A[Catch: all -> 0x01f8, TryCatch #1 {all -> 0x01f8, blocks: (B:69:0x0151, B:71:0x0158, B:73:0x0160, B:78:0x0186, B:80:0x01ce, B:82:0x01d1, B:83:0x01d6, B:74:0x0173, B:76:0x017b), top: B:147:0x0151 }] */
    @Override // org.telegram.ui.Components.EditTextEffects, android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        boolean z;
        int totalPaddingTop;
        android.graphics.Rect rect;
        int i;
        float f;
        Object obj;
        boolean z2;
        int totalPaddingTop2;
        android.graphics.Rect rect2;
        int i2;
        float f2;
        int i3;
        boolean z3;
        drawHint(canvas);
        int extendedPaddingTop = getExtendedPaddingTop();
        this.scrollY = ConnectionsManager.DEFAULT_DATACENTER_ID;
        try {
            Field field = mScrollYField;
            if (field != null) {
                this.scrollY = field.getInt(this);
                mScrollYField.set(this, 0);
            } else {
                this.scrollY = getScrollX();
            }
        } catch (Exception e) {
            if (BuildVars.DEBUG_PRIVATE_VERSION) {
                throw new RuntimeException(e);
            }
        }
        this.ignoreTopCount = 1;
        this.ignoreBottomCount = 1;
        canvas.save();
        canvas.translate(0.0f, extendedPaddingTop);
        try {
            this.drawInMaim = true;
            super.onDraw(canvas);
            this.drawInMaim = false;
        } catch (Exception e2) {
            if (BuildVars.DEBUG_PRIVATE_VERSION) {
                throw new RuntimeException(e2);
            }
        }
        Field field2 = mScrollYField;
        if (field2 != null && (i3 = this.scrollY) != Integer.MAX_VALUE) {
            try {
                field2.set(this, Integer.valueOf(i3));
            } catch (Exception e3) {
                if (z3) {
                    throw new RuntimeException(e3);
                }
            }
        }
        canvas.restore();
        if (this.cursorDrawable == null) {
            try {
                Field field3 = mShowCursorField;
                if (field3 != null && (obj = this.editor) != null) {
                    z = (SystemClock.uptimeMillis() - field3.getLong(obj)) % 1000 < 500 && isFocused();
                } else {
                    z = this.cursorDrawn;
                    this.cursorDrawn = false;
                }
                if (this.allowDrawCursor && z) {
                    canvas.save();
                    if (getVerticalOffsetMethod != null) {
                        if ((getGravity() & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle) != 48) {
                            totalPaddingTop = ((Integer) getVerticalOffsetMethod.invoke(this, Boolean.TRUE)).intValue();
                            canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + totalPaddingTop);
                            Layout layout = getLayout();
                            int lineForOffset = layout.getLineForOffset(getSelectionStart());
                            int lineCount = layout.getLineCount();
                            updateCursorPosition();
                            android.graphics.Rect bounds = this.gradientDrawable.getBounds();
                            android.graphics.Rect rect3 = this.rect;
                            rect3.left = bounds.left;
                            rect3.right = bounds.left + AndroidUtilities.dp(this.cursorWidth);
                            rect = this.rect;
                            i = bounds.bottom;
                            rect.bottom = i;
                            rect.top = bounds.top;
                            f = this.lineSpacingExtra;
                            if (f != 0.0f && lineForOffset < lineCount - 1) {
                                rect.bottom = (int) (i - f);
                            }
                            int centerY = rect.centerY();
                            int i4 = this.cursorSize;
                            rect.top = centerY - (i4 / 2);
                            android.graphics.Rect rect4 = this.rect;
                            rect4.bottom = rect4.top + i4;
                            this.gradientDrawable.setBounds(rect4);
                            this.gradientDrawable.draw(canvas);
                            canvas.restore();
                        }
                        totalPaddingTop = 0;
                        canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + totalPaddingTop);
                        Layout layout2 = getLayout();
                        int lineForOffset2 = layout2.getLineForOffset(getSelectionStart());
                        int lineCount2 = layout2.getLineCount();
                        updateCursorPosition();
                        android.graphics.Rect bounds2 = this.gradientDrawable.getBounds();
                        android.graphics.Rect rect32 = this.rect;
                        rect32.left = bounds2.left;
                        rect32.right = bounds2.left + AndroidUtilities.dp(this.cursorWidth);
                        rect = this.rect;
                        i = bounds2.bottom;
                        rect.bottom = i;
                        rect.top = bounds2.top;
                        f = this.lineSpacingExtra;
                        if (f != 0.0f) {
                            rect.bottom = (int) (i - f);
                        }
                        int centerY2 = rect.centerY();
                        int i42 = this.cursorSize;
                        rect.top = centerY2 - (i42 / 2);
                        android.graphics.Rect rect42 = this.rect;
                        rect42.bottom = rect42.top + i42;
                        this.gradientDrawable.setBounds(rect42);
                        this.gradientDrawable.draw(canvas);
                        canvas.restore();
                    } else {
                        if ((getGravity() & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle) != 48) {
                            totalPaddingTop = getTotalPaddingTop() - getExtendedPaddingTop();
                            canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + totalPaddingTop);
                            Layout layout22 = getLayout();
                            int lineForOffset22 = layout22.getLineForOffset(getSelectionStart());
                            int lineCount22 = layout22.getLineCount();
                            updateCursorPosition();
                            android.graphics.Rect bounds22 = this.gradientDrawable.getBounds();
                            android.graphics.Rect rect322 = this.rect;
                            rect322.left = bounds22.left;
                            rect322.right = bounds22.left + AndroidUtilities.dp(this.cursorWidth);
                            rect = this.rect;
                            i = bounds22.bottom;
                            rect.bottom = i;
                            rect.top = bounds22.top;
                            f = this.lineSpacingExtra;
                            if (f != 0.0f) {
                            }
                            int centerY22 = rect.centerY();
                            int i422 = this.cursorSize;
                            rect.top = centerY22 - (i422 / 2);
                            android.graphics.Rect rect422 = this.rect;
                            rect422.bottom = rect422.top + i422;
                            this.gradientDrawable.setBounds(rect422);
                            this.gradientDrawable.draw(canvas);
                            canvas.restore();
                        }
                        totalPaddingTop = 0;
                        canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + totalPaddingTop);
                        Layout layout222 = getLayout();
                        int lineForOffset222 = layout222.getLineForOffset(getSelectionStart());
                        int lineCount222 = layout222.getLineCount();
                        updateCursorPosition();
                        android.graphics.Rect bounds222 = this.gradientDrawable.getBounds();
                        android.graphics.Rect rect3222 = this.rect;
                        rect3222.left = bounds222.left;
                        rect3222.right = bounds222.left + AndroidUtilities.dp(this.cursorWidth);
                        rect = this.rect;
                        i = bounds222.bottom;
                        rect.bottom = i;
                        rect.top = bounds222.top;
                        f = this.lineSpacingExtra;
                        if (f != 0.0f) {
                        }
                        int centerY222 = rect.centerY();
                        int i4222 = this.cursorSize;
                        rect.top = centerY222 - (i4222 / 2);
                        android.graphics.Rect rect4222 = this.rect;
                        rect4222.bottom = rect4222.top + i4222;
                        this.gradientDrawable.setBounds(rect4222);
                        this.gradientDrawable.draw(canvas);
                        canvas.restore();
                    }
                }
            } finally {
                if (BuildVars.DEBUG_PRIVATE_VERSION) {
                    RuntimeException runtimeException = new RuntimeException(th);
                }
            }
        } else if (this.cursorDrawn) {
            try {
                canvas.save();
                if (getVerticalOffsetMethod != null) {
                    if ((getGravity() & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle) != 48) {
                        totalPaddingTop2 = ((Integer) getVerticalOffsetMethod.invoke(this, Boolean.TRUE)).intValue();
                        canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + totalPaddingTop2);
                        Layout layout3 = getLayout();
                        int lineForOffset3 = layout3.getLineForOffset(getSelectionStart());
                        int lineCount3 = layout3.getLineCount();
                        updateCursorPosition();
                        android.graphics.Rect bounds3 = this.gradientDrawable.getBounds();
                        android.graphics.Rect rect5 = this.rect;
                        rect5.left = bounds3.left;
                        rect5.right = bounds3.left + AndroidUtilities.dp(this.cursorWidth);
                        rect2 = this.rect;
                        i2 = bounds3.bottom;
                        rect2.bottom = i2;
                        rect2.top = bounds3.top;
                        f2 = this.lineSpacingExtra;
                        if (f2 != 0.0f && lineForOffset3 < lineCount3 - 1) {
                            rect2.bottom = (int) (i2 - f2);
                        }
                        int centerY3 = rect2.centerY();
                        int i5 = this.cursorSize;
                        rect2.top = centerY3 - (i5 / 2);
                        android.graphics.Rect rect6 = this.rect;
                        rect6.bottom = rect6.top + i5;
                        this.gradientDrawable.setBounds(rect6);
                        this.gradientDrawable.draw(canvas);
                        canvas.restore();
                        this.cursorDrawn = false;
                    }
                    totalPaddingTop2 = 0;
                    canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + totalPaddingTop2);
                    Layout layout32 = getLayout();
                    int lineForOffset32 = layout32.getLineForOffset(getSelectionStart());
                    int lineCount32 = layout32.getLineCount();
                    updateCursorPosition();
                    android.graphics.Rect bounds32 = this.gradientDrawable.getBounds();
                    android.graphics.Rect rect52 = this.rect;
                    rect52.left = bounds32.left;
                    rect52.right = bounds32.left + AndroidUtilities.dp(this.cursorWidth);
                    rect2 = this.rect;
                    i2 = bounds32.bottom;
                    rect2.bottom = i2;
                    rect2.top = bounds32.top;
                    f2 = this.lineSpacingExtra;
                    if (f2 != 0.0f) {
                        rect2.bottom = (int) (i2 - f2);
                    }
                    int centerY32 = rect2.centerY();
                    int i52 = this.cursorSize;
                    rect2.top = centerY32 - (i52 / 2);
                    android.graphics.Rect rect62 = this.rect;
                    rect62.bottom = rect62.top + i52;
                    this.gradientDrawable.setBounds(rect62);
                    this.gradientDrawable.draw(canvas);
                    canvas.restore();
                    this.cursorDrawn = false;
                } else {
                    if ((getGravity() & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle) != 48) {
                        totalPaddingTop2 = getTotalPaddingTop() - getExtendedPaddingTop();
                        canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + totalPaddingTop2);
                        Layout layout322 = getLayout();
                        int lineForOffset322 = layout322.getLineForOffset(getSelectionStart());
                        int lineCount322 = layout322.getLineCount();
                        updateCursorPosition();
                        android.graphics.Rect bounds322 = this.gradientDrawable.getBounds();
                        android.graphics.Rect rect522 = this.rect;
                        rect522.left = bounds322.left;
                        rect522.right = bounds322.left + AndroidUtilities.dp(this.cursorWidth);
                        rect2 = this.rect;
                        i2 = bounds322.bottom;
                        rect2.bottom = i2;
                        rect2.top = bounds322.top;
                        f2 = this.lineSpacingExtra;
                        if (f2 != 0.0f) {
                        }
                        int centerY322 = rect2.centerY();
                        int i522 = this.cursorSize;
                        rect2.top = centerY322 - (i522 / 2);
                        android.graphics.Rect rect622 = this.rect;
                        rect622.bottom = rect622.top + i522;
                        this.gradientDrawable.setBounds(rect622);
                        this.gradientDrawable.draw(canvas);
                        canvas.restore();
                        this.cursorDrawn = false;
                    }
                    totalPaddingTop2 = 0;
                    canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + totalPaddingTop2);
                    Layout layout3222 = getLayout();
                    int lineForOffset3222 = layout3222.getLineForOffset(getSelectionStart());
                    int lineCount3222 = layout3222.getLineCount();
                    updateCursorPosition();
                    android.graphics.Rect bounds3222 = this.gradientDrawable.getBounds();
                    android.graphics.Rect rect5222 = this.rect;
                    rect5222.left = bounds3222.left;
                    rect5222.right = bounds3222.left + AndroidUtilities.dp(this.cursorWidth);
                    rect2 = this.rect;
                    i2 = bounds3222.bottom;
                    rect2.bottom = i2;
                    rect2.top = bounds3222.top;
                    f2 = this.lineSpacingExtra;
                    if (f2 != 0.0f) {
                    }
                    int centerY3222 = rect2.centerY();
                    int i5222 = this.cursorSize;
                    rect2.top = centerY3222 - (i5222 / 2);
                    android.graphics.Rect rect6222 = this.rect;
                    rect6222.bottom = rect6222.top + i5222;
                    this.gradientDrawable.setBounds(rect6222);
                    this.gradientDrawable.draw(canvas);
                    canvas.restore();
                    this.cursorDrawn = false;
                }
            } finally {
                if (BuildVars.DEBUG_PRIVATE_VERSION) {
                    RuntimeException runtimeException2 = new RuntimeException(e3);
                }
            }
        }
        if (!this.lineVisible || this.lineColor == 0) {
            return;
        }
        int dp = AndroidUtilities.dp(1.0f);
        boolean z4 = this.lineActive;
        if (!TextUtils.isEmpty(this.errorText)) {
            this.linePaint.setColor(this.errorLineColor);
            dp = AndroidUtilities.dp(2.0f);
            this.lineActive = false;
        } else if (isFocused()) {
            this.lineActive = true;
        } else {
            this.linePaint.setColor(this.lineColor);
            this.lineActive = false;
        }
        if (this.lineActive != z4) {
            this.lineLastUpdateTime = SystemClock.elapsedRealtime();
            this.lastLineActiveness = this.lineActiveness;
        }
        float elapsedRealtime = ((float) (SystemClock.elapsedRealtime() - this.lineLastUpdateTime)) / 150.0f;
        if (elapsedRealtime < 1.0f || (((z2 = this.lineActive) && this.lineActiveness != 1.0f) || (!z2 && this.lineActiveness != 0.0f))) {
            this.lineActiveness = AndroidUtilities.lerp(this.lastLineActiveness, this.lineActive ? 1.0f : 0.0f, Math.max(0.0f, Math.min(1.0f, elapsedRealtime)));
            if (elapsedRealtime < 1.0f) {
                invalidate();
            }
        }
        int scrollY = ((int) this.lineY) + getScrollY() + Math.min(Math.max(0, ((((getLayout() == null ? 0 : getLayout().getHeight()) - getMeasuredHeight()) + getPaddingBottom()) + getPaddingTop()) - getScrollY()), AndroidUtilities.dp(2.0f));
        int i6 = this.lastTouchX;
        if (i6 < 0) {
            i6 = getMeasuredWidth() / 2;
        }
        int max = Math.max(i6, getMeasuredWidth() - i6) * 2;
        if (this.lineActiveness < 1.0f) {
            canvas.drawRect(getScrollX(), scrollY - dp, getScrollX() + getMeasuredWidth(), scrollY, this.linePaint);
        }
        float f3 = this.lineActiveness;
        if (f3 > 0.0f) {
            float interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f3);
            boolean z5 = this.lineActive;
            if (z5) {
                this.activeLineWidth = max * interpolation;
            }
            float f4 = i6;
            canvas.drawRect(getScrollX() + Math.max(0.0f, f4 - (this.activeLineWidth / 2.0f)), scrollY - ((int) ((z5 ? 1.0f : interpolation) * AndroidUtilities.dp(2.0f))), getScrollX() + Math.min(f4 + (this.activeLineWidth / 2.0f), getMeasuredWidth()), scrollY, this.activeLinePaint);
        }
    }

    public void setWindowView(View view) {
        this.windowView = view;
    }

    private boolean updateCursorPosition() {
        Layout layout = getLayout();
        int length = this.forceCursorEnd ? layout.getText().length() : getSelectionStart();
        int lineForOffset = layout.getLineForOffset(length);
        updateCursorPosition(layout.getLineTop(lineForOffset), layout.getLineTop(lineForOffset + 1), layout.getPrimaryHorizontal(length));
        layout.getText();
        return true;
    }

    private int clampHorizontalPosition(Drawable drawable, float f) {
        int i;
        float max = Math.max(0.5f, f - 0.5f);
        if (this.mTempRect == null) {
            this.mTempRect = new android.graphics.Rect();
        }
        int i2 = 0;
        if (drawable != null) {
            drawable.getPadding(this.mTempRect);
            i2 = drawable.getIntrinsicWidth();
        } else {
            this.mTempRect.setEmpty();
        }
        int scrollX = getScrollX();
        float f2 = max - scrollX;
        int width = (getWidth() - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        float f3 = width;
        if (f2 >= f3 - 1.0f) {
            return (width + scrollX) - (i2 - this.mTempRect.right);
        }
        if (Math.abs(f2) <= 1.0f || (TextUtils.isEmpty(getText()) && FileLoaderPriorityQueue.PRIORITY_VALUE_MAX - scrollX <= f3 + 1.0f && max <= 1.0f)) {
            i = this.mTempRect.left;
        } else {
            scrollX = (int) max;
            i = this.mTempRect.left;
        }
        return scrollX - i;
    }

    private void updateCursorPosition(int i, int i2, float f) {
        int clampHorizontalPosition = clampHorizontalPosition(this.gradientDrawable, f);
        int dp = AndroidUtilities.dp(this.cursorWidth);
        GradientDrawable gradientDrawable = this.gradientDrawable;
        android.graphics.Rect rect = this.mTempRect;
        gradientDrawable.setBounds(clampHorizontalPosition, i - rect.top, dp + clampHorizontalPosition, i2 + rect.bottom);
    }

    @Override // android.widget.TextView
    public float getLineSpacingExtra() {
        return super.getLineSpacingExtra();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupFloatingActionModeViews() {
        FloatingToolbar floatingToolbar = this.floatingToolbar;
        if (floatingToolbar != null) {
            floatingToolbar.dismiss();
            this.floatingToolbar = null;
        }
        if (this.floatingToolbarPreDrawListener != null) {
            getViewTreeObserver().removeOnPreDrawListener(this.floatingToolbarPreDrawListener);
            this.floatingToolbarPreDrawListener = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.EditTextEffects, android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        try {
            super.onAttachedToWindow();
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.attachedToWindow = getRootView();
        AndroidUtilities.runOnUIThread(this.invalidateRunnable);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.EditTextEffects, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = null;
        AndroidUtilities.cancelRunOnUIThread(this.invalidateRunnable);
    }

    @Override // android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback) {
        if (Build.VERSION.SDK_INT >= 23 && (this.windowView != null || this.attachedToWindow != null)) {
            FloatingActionMode floatingActionMode = this.floatingActionMode;
            if (floatingActionMode != null) {
                floatingActionMode.finish();
            }
            cleanupFloatingActionModeViews();
            Context context = getContext();
            View view = this.windowView;
            if (view == null) {
                view = this.attachedToWindow;
            }
            FloatingToolbar floatingToolbar = new FloatingToolbar(context, view, getActionModeStyle(), getResourcesProvider());
            this.floatingToolbar = floatingToolbar;
            floatingToolbar.setOnPremiumLockClick(this.onPremiumMenuLockClickListener);
            this.floatingToolbar.setQuoteShowVisible(new Utilities.Callback0Return() { // from class: org.telegram.ui.Components.EditTextBoldCursor$$ExternalSyntheticLambda2
                @Override // org.telegram.messenger.Utilities.Callback0Return
                public final Object run() {
                    boolean shouldShowQuoteButton;
                    shouldShowQuoteButton = EditTextBoldCursor.this.shouldShowQuoteButton();
                    return Boolean.valueOf(shouldShowQuoteButton);
                }
            });
            this.floatingActionMode = new FloatingActionMode(getContext(), new ActionModeCallback2Wrapper(callback), this, this.floatingToolbar);
            this.floatingToolbarPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.Components.EditTextBoldCursor$$ExternalSyntheticLambda0
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public final boolean onPreDraw() {
                    boolean lambda$startActionMode$1;
                    lambda$startActionMode$1 = EditTextBoldCursor.this.lambda$startActionMode$1();
                    return lambda$startActionMode$1;
                }
            };
            FloatingActionMode floatingActionMode2 = this.floatingActionMode;
            callback.onCreateActionMode(floatingActionMode2, floatingActionMode2.getMenu());
            FloatingActionMode floatingActionMode3 = this.floatingActionMode;
            extendActionMode(floatingActionMode3, floatingActionMode3.getMenu());
            this.floatingActionMode.invalidate();
            getViewTreeObserver().addOnPreDrawListener(this.floatingToolbarPreDrawListener);
            invalidate();
            return this.floatingActionMode;
        }
        return super.startActionMode(callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$startActionMode$1() {
        FloatingActionMode floatingActionMode = this.floatingActionMode;
        if (floatingActionMode != null) {
            floatingActionMode.updateViewLocationInWindow();
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldShowQuoteButton() {
        Editable text;
        if (!hasSelection() || getSelectionStart() < 0 || getSelectionEnd() < 0 || getSelectionStart() == getSelectionEnd() || (text = getText()) == null) {
            return false;
        }
        QuoteSpan.QuoteStyleSpan[] quoteStyleSpanArr = (QuoteSpan.QuoteStyleSpan[]) text.getSpans(getSelectionStart(), getSelectionEnd(), QuoteSpan.QuoteStyleSpan.class);
        return quoteStyleSpanArr == null || quoteStyleSpanArr.length == 0;
    }

    @Override // android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback, int i) {
        if (Build.VERSION.SDK_INT >= 23 && (this.windowView != null || this.attachedToWindow != null)) {
            return startActionMode(callback);
        }
        return super.startActionMode(callback, i);
    }

    public void hideActionMode() {
        cleanupFloatingActionModeViews();
    }

    @Override // android.widget.EditText
    public void setSelection(int i, int i2) {
        try {
            super.setSelection(i, i2);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    @Override // android.widget.EditText
    public void setSelection(int i) {
        try {
            super.setSelection(i);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("android.widget.EditText");
        if (this.hintLayout != null) {
            if (getText().length() <= 0) {
                accessibilityNodeInfo.setText(this.hintLayout.getText());
            } else {
                AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo).setHintText(this.hintLayout.getText());
            }
        }
    }

    public void setHandlesColor(int i) {
        if (Build.VERSION.SDK_INT >= 29 && !XiaomiUtilities.isMIUI()) {
            try {
                Drawable textSelectHandleLeft = getTextSelectHandleLeft();
                textSelectHandleLeft.setColorFilter(i, PorterDuff.Mode.SRC_IN);
                setTextSelectHandleLeft(textSelectHandleLeft);
                Drawable textSelectHandle = getTextSelectHandle();
                textSelectHandle.setColorFilter(i, PorterDuff.Mode.SRC_IN);
                setTextSelectHandle(textSelectHandle);
                Drawable textSelectHandleRight = getTextSelectHandleRight();
                textSelectHandleRight.setColorFilter(i, PorterDuff.Mode.SRC_IN);
                setTextSelectHandleRight(textSelectHandleRight);
            } catch (Exception unused) {
            }
        }
    }

    public void setOnPremiumMenuLockClickListener(Runnable runnable) {
        this.onPremiumMenuLockClickListener = runnable;
    }

    public Runnable getOnPremiumMenuLockClickListener() {
        return this.onPremiumMenuLockClickListener;
    }
}
