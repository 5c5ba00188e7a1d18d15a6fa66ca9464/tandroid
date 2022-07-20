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
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.os.SystemClock;
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
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.XiaomiUtilities;
import org.telegram.ui.ActionBar.FloatingActionMode;
import org.telegram.ui.ActionBar.FloatingToolbar;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class EditTextBoldCursor extends EditTextEffects {
    private static Class editorClass;
    private static Method getVerticalOffsetMethod;
    private static Field mCursorDrawableResField;
    private static Field mEditor;
    private static Field mScrollYField;
    private static boolean mScrollYGet;
    private static Field mShowCursorField;
    private int activeLineColor;
    private Paint activeLinePaint;
    private View attachedToWindow;
    private boolean currentDrawHintAsHeader;
    ShapeDrawable cursorDrawable;
    private boolean cursorDrawn;
    private int cursorSize;
    boolean drawInMaim;
    private Object editor;
    private int errorLineColor;
    private TextPaint errorPaint;
    private CharSequence errorText;
    private boolean fixed;
    public FloatingActionMode floatingActionMode;
    private FloatingToolbar floatingToolbar;
    private ViewTreeObserver.OnPreDrawListener floatingToolbarPreDrawListener;
    private GradientDrawable gradientDrawable;
    private float headerAnimationProgress;
    private int headerHintColor;
    private AnimatorSet headerTransformAnimation;
    private CharSequence hint;
    private SubstringLayoutAnimator hintAnimator;
    private int hintColor;
    private long hintLastUpdateTime;
    private StaticLayout hintLayout;
    private int ignoreBottomCount;
    private int ignoreTopCount;
    private int lastSize;
    private int lineColor;
    private long lineLastUpdateTime;
    private Paint linePaint;
    private float lineSpacingExtra;
    private float lineY;
    private ViewTreeObserver.OnPreDrawListener listenerFixer;
    private android.graphics.Rect mTempRect;
    private boolean nextSetTextAnimated;
    private int scrollY;
    private boolean supportRtlHint;
    private boolean transformHintToHeader;
    private View windowView;
    private Runnable invalidateRunnable = new AnonymousClass1();
    private android.graphics.Rect rect = new android.graphics.Rect();
    private boolean hintVisible = true;
    private float hintAlpha = 1.0f;
    private boolean allowDrawCursor = true;
    private float cursorWidth = 2.0f;
    private boolean lineVisible = false;
    private boolean lineActive = false;
    private float lineActiveness = 0.0f;
    private float lastLineActiveness = 0.0f;
    private float activeLineWidth = 0.0f;
    private List<TextWatcher> registeredTextWatchers = new ArrayList();
    private boolean isTextWatchersSuppressed = false;
    private android.graphics.Rect padding = new android.graphics.Rect();
    private int lastTouchX = -1;

    protected void extendActionMode(ActionMode actionMode, Menu menu) {
    }

    protected int getActionModeStyle() {
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

    /* renamed from: org.telegram.ui.Components.EditTextBoldCursor$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
            EditTextBoldCursor.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            EditTextBoldCursor.this.invalidate();
            if (EditTextBoldCursor.this.attachedToWindow != null) {
                AndroidUtilities.runOnUIThread(this, 500L);
            }
        }
    }

    @TargetApi(23)
    /* loaded from: classes3.dex */
    public class ActionModeCallback2Wrapper extends ActionMode.Callback2 {
        private final ActionMode.Callback mWrapped;

        public ActionModeCallback2Wrapper(ActionMode.Callback callback) {
            EditTextBoldCursor.this = r1;
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
        if (Build.VERSION.SDK_INT >= 26) {
            setImportantForAutofill(2);
        }
        init();
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
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(new RectShape());
        anonymousClass2.getPaint().setColor(0);
        return anonymousClass2;
    }

    /* renamed from: org.telegram.ui.Components.EditTextBoldCursor$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ShapeDrawable {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Shape shape) {
            super(shape);
            EditTextBoldCursor.this = r1;
        }

        @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            super.draw(canvas);
            EditTextBoldCursor.this.cursorDrawn = true;
        }
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
            AnonymousClass3 anonymousClass3 = new AnonymousClass3();
            this.cursorDrawable = anonymousClass3;
            anonymousClass3.setShape(new RectShape());
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
                Method declaredMethod = TextView.class.getDeclaredMethod("getVerticalOffset", Boolean.TYPE);
                getVerticalOffsetMethod = declaredMethod;
                declaredMethod.setAccessible(true);
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
            } catch (Throwable unused3) {
            }
            try {
                if (mCursorDrawableResField == null) {
                    Field declaredField4 = TextView.class.getDeclaredField("mCursorDrawableRes");
                    mCursorDrawableResField = declaredField4;
                    declaredField4.setAccessible(true);
                }
                Field field = mCursorDrawableResField;
                if (field != null) {
                    field.set(this, 2131165398);
                }
            } catch (Throwable unused4) {
            }
        }
        this.cursorSize = AndroidUtilities.dp(24.0f);
    }

    /* renamed from: org.telegram.ui.Components.EditTextBoldCursor$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends ShapeDrawable {
        AnonymousClass3() {
            EditTextBoldCursor.this = r1;
        }

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
    }

    @SuppressLint({"PrivateApi"})
    public void fixHandleView(boolean z) {
        if (z) {
            this.fixed = false;
        } else if (this.fixed) {
        } else {
            try {
                if (editorClass == null) {
                    editorClass = Class.forName("android.widget.Editor");
                    Field declaredField = TextView.class.getDeclaredField("mEditor");
                    mEditor = declaredField;
                    declaredField.setAccessible(true);
                    this.editor = mEditor.get(this);
                }
                if (this.listenerFixer == null) {
                    Method declaredMethod = editorClass.getDeclaredMethod("getPositionListener", new Class[0]);
                    declaredMethod.setAccessible(true);
                    this.listenerFixer = (ViewTreeObserver.OnPreDrawListener) declaredMethod.invoke(this.editor, new Object[0]);
                }
                ViewTreeObserver.OnPreDrawListener onPreDrawListener = this.listenerFixer;
                onPreDrawListener.getClass();
                AndroidUtilities.runOnUIThread(new EditTextBoldCursor$$ExternalSyntheticLambda1(onPreDrawListener), 500L);
            } catch (Throwable unused) {
            }
            this.fixed = true;
        }
    }

    public void setTransformHintToHeader(boolean z) {
        if (this.transformHintToHeader == z) {
            return;
        }
        this.transformHintToHeader = z;
        AnimatorSet animatorSet = this.headerTransformAnimation;
        if (animatorSet == null) {
            return;
        }
        animatorSet.cancel();
        this.headerTransformAnimation = null;
    }

    public void setAllowDrawCursor(boolean z) {
        this.allowDrawCursor = z;
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
        getContext().getResources().getDrawable(2131166122).getPadding(this.padding);
        android.graphics.Rect rect = this.padding;
        setPadding(rect.left, rect.top, rect.right, rect.bottom);
        this.lineColor = i;
        this.activeLineColor = i2;
        this.activeLinePaint.setColor(i2);
        this.errorLineColor = i3;
        this.errorPaint.setColor(i3);
        invalidate();
    }

    public void setHintVisible(boolean z) {
        if (this.hintVisible == z) {
            return;
        }
        this.hintLastUpdateTime = System.currentTimeMillis();
        this.hintVisible = z;
        invalidate();
    }

    public void setHintColor(int i) {
        this.hintColor = i;
        invalidate();
    }

    public void setHeaderHintColor(int i) {
        this.headerHintColor = i;
        invalidate();
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

    @Override // android.widget.TextView, android.view.View
    protected void onScrollChanged(int i, int i2, int i3, int i4) {
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

    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredHeight = getMeasuredHeight() + (getMeasuredWidth() << 16);
        if (this.hintLayout != null) {
            if (this.lastSize != measuredHeight) {
                setHintText(this.hint);
            }
            this.lineY = ((getMeasuredHeight() - this.hintLayout.getHeight()) / 2.0f) + this.hintLayout.getHeight() + AndroidUtilities.dp(6.0f);
        } else {
            this.lineY = getMeasuredHeight() - AndroidUtilities.dp(2.0f);
        }
        this.lastSize = measuredHeight;
    }

    public void setHintText(CharSequence charSequence) {
        setHintText(charSequence, false);
    }

    public void setHintText(CharSequence charSequence, boolean z) {
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
            this.hintAnimator.create(this.hintLayout, this.hint, charSequence, getPaint());
        } else {
            SubstringLayoutAnimator substringLayoutAnimator = this.hintAnimator;
            if (substringLayoutAnimator != null) {
                substringLayoutAnimator.cancel();
            }
        }
        this.hint = charSequence;
        if (getMeasuredWidth() != 0) {
            charSequence = TextUtils.ellipsize(charSequence, getPaint(), getMeasuredWidth(), TextUtils.TruncateAt.END);
            StaticLayout staticLayout = this.hintLayout;
            if (staticLayout != null && TextUtils.equals(staticLayout.getText(), charSequence)) {
                return;
            }
        }
        this.hintLayout = new StaticLayout(charSequence, getPaint(), AndroidUtilities.dp(1000.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }

    public Layout getHintLayoutEx() {
        return this.hintLayout;
    }

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
            float f = 1.0f;
            if (z) {
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.headerTransformAnimation = animatorSet2;
                Animator[] animatorArr = new Animator[1];
                float[] fArr = new float[1];
                if (!z2) {
                    f = 0.0f;
                }
                fArr[0] = f;
                animatorArr[0] = ObjectAnimator.ofFloat(this, "headerAnimationProgress", fArr);
                animatorSet2.playTogether(animatorArr);
                this.headerTransformAnimation.setDuration(200L);
                this.headerTransformAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.headerTransformAnimation.start();
            } else {
                if (!z2) {
                    f = 0.0f;
                }
                this.headerAnimationProgress = f;
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
            if (i2 == Integer.MAX_VALUE) {
                return 0;
            }
            return -i2;
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

    /* JADX WARN: Removed duplicated region for block: B:111:0x027f A[Catch: all -> 0x02a8, TryCatch #3 {all -> 0x02a8, blocks: (B:86:0x01d4, B:88:0x01d8, B:90:0x01dc, B:92:0x01ee, B:96:0x01f8, B:97:0x01fc, B:100:0x0202, B:102:0x0209, B:104:0x0211, B:105:0x0224, B:107:0x022c, B:109:0x0237, B:111:0x027f, B:113:0x0282, B:114:0x0287), top: B:201:0x01d4 }] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x02af A[FINALLY_INSNS] */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0336 A[Catch: all -> 0x0360, TryCatch #0 {all -> 0x0360, blocks: (B:122:0x02b9, B:124:0x02c0, B:126:0x02c8, B:127:0x02db, B:129:0x02e3, B:131:0x02ee, B:133:0x0336, B:135:0x0339, B:136:0x033e), top: B:195:0x02b9 }] */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0366 A[FINALLY_INSNS] */
    @Override // org.telegram.ui.Components.EditTextEffects, android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        boolean z;
        int i;
        android.graphics.Rect rect;
        int i2;
        float f;
        Object obj;
        boolean z2;
        int i3;
        android.graphics.Rect rect2;
        int i4;
        float f2;
        int i5;
        boolean z3;
        boolean z4;
        float f3 = 1.0f;
        if ((length() == 0 || this.transformHintToHeader) && this.hintLayout != null && ((z4 = this.hintVisible) || this.hintAlpha != 0.0f)) {
            if ((z4 && this.hintAlpha != 1.0f) || (!z4 && this.hintAlpha != 0.0f)) {
                long currentTimeMillis = System.currentTimeMillis();
                long j = currentTimeMillis - this.hintLastUpdateTime;
                if (j < 0 || j > 17) {
                    j = 17;
                }
                this.hintLastUpdateTime = currentTimeMillis;
                if (this.hintVisible) {
                    float f4 = this.hintAlpha + (((float) j) / 150.0f);
                    this.hintAlpha = f4;
                    if (f4 > 1.0f) {
                        this.hintAlpha = 1.0f;
                    }
                } else {
                    float f5 = this.hintAlpha - (((float) j) / 150.0f);
                    this.hintAlpha = f5;
                    if (f5 < 0.0f) {
                        this.hintAlpha = 0.0f;
                    }
                }
                invalidate();
            }
            int color = getPaint().getColor();
            canvas.save();
            float lineLeft = this.hintLayout.getLineLeft(0);
            float lineWidth = this.hintLayout.getLineWidth(0);
            int i6 = lineLeft != 0.0f ? (int) (0 - lineLeft) : 0;
            if (this.supportRtlHint && LocaleController.isRTL) {
                canvas.translate(i6 + getScrollX() + (getMeasuredWidth() - lineWidth), (this.lineY - this.hintLayout.getHeight()) - AndroidUtilities.dp(7.0f));
            } else {
                canvas.translate(i6 + getScrollX(), (this.lineY - this.hintLayout.getHeight()) - AndroidUtilities.dp2(7.0f));
            }
            if (this.transformHintToHeader) {
                float f6 = 1.0f - (this.headerAnimationProgress * 0.3f);
                if (this.supportRtlHint && LocaleController.isRTL) {
                    float f7 = lineWidth + lineLeft;
                    canvas.translate(f7 - (f7 * f6), 0.0f);
                } else if (lineLeft != 0.0f) {
                    canvas.translate(lineLeft * (1.0f - f6), 0.0f);
                }
                canvas.scale(f6, f6);
                canvas.translate(0.0f, (-AndroidUtilities.dp(22.0f)) * this.headerAnimationProgress);
                getPaint().setColor(AndroidUtilities.lerpColor(this.hintColor, this.headerHintColor, this.headerAnimationProgress));
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
                this.hintLayout.draw(canvas);
            }
            getPaint().setColor(color);
            canvas.restore();
        }
        int extendedPaddingTop = getExtendedPaddingTop();
        this.scrollY = Integer.MAX_VALUE;
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
        if (field2 != null && (i5 = this.scrollY) != Integer.MAX_VALUE) {
            try {
                field2.set(this, Integer.valueOf(i5));
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
            } finally {
                if (BuildVars.DEBUG_PRIVATE_VERSION) {
                }
            }
            if (this.allowDrawCursor && z) {
                canvas.save();
                if (getVerticalOffsetMethod != null) {
                    if ((getGravity() & 112) != 48) {
                        i = ((Integer) getVerticalOffsetMethod.invoke(this, Boolean.TRUE)).intValue();
                        canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + i);
                        Layout layout = getLayout();
                        int lineForOffset = layout.getLineForOffset(getSelectionStart());
                        int lineCount = layout.getLineCount();
                        updateCursorPosition();
                        android.graphics.Rect bounds = this.gradientDrawable.getBounds();
                        android.graphics.Rect rect3 = this.rect;
                        rect3.left = bounds.left;
                        rect3.right = bounds.left + AndroidUtilities.dp(this.cursorWidth);
                        rect = this.rect;
                        i2 = bounds.bottom;
                        rect.bottom = i2;
                        rect.top = bounds.top;
                        f = this.lineSpacingExtra;
                        if (f != 0.0f && lineForOffset < lineCount - 1) {
                            rect.bottom = (int) (i2 - f);
                        }
                        int centerY = rect.centerY();
                        int i7 = this.cursorSize;
                        rect.top = centerY - (i7 / 2);
                        android.graphics.Rect rect4 = this.rect;
                        rect4.bottom = rect4.top + i7;
                        this.gradientDrawable.setBounds(rect4);
                        this.gradientDrawable.draw(canvas);
                        canvas.restore();
                    }
                    i = 0;
                    canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + i);
                    Layout layout2 = getLayout();
                    int lineForOffset2 = layout2.getLineForOffset(getSelectionStart());
                    int lineCount2 = layout2.getLineCount();
                    updateCursorPosition();
                    android.graphics.Rect bounds2 = this.gradientDrawable.getBounds();
                    android.graphics.Rect rect32 = this.rect;
                    rect32.left = bounds2.left;
                    rect32.right = bounds2.left + AndroidUtilities.dp(this.cursorWidth);
                    rect = this.rect;
                    i2 = bounds2.bottom;
                    rect.bottom = i2;
                    rect.top = bounds2.top;
                    f = this.lineSpacingExtra;
                    if (f != 0.0f) {
                        rect.bottom = (int) (i2 - f);
                    }
                    int centerY2 = rect.centerY();
                    int i72 = this.cursorSize;
                    rect.top = centerY2 - (i72 / 2);
                    android.graphics.Rect rect42 = this.rect;
                    rect42.bottom = rect42.top + i72;
                    this.gradientDrawable.setBounds(rect42);
                    this.gradientDrawable.draw(canvas);
                    canvas.restore();
                } else {
                    if ((getGravity() & 112) != 48) {
                        i = getTotalPaddingTop() - getExtendedPaddingTop();
                        canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + i);
                        Layout layout22 = getLayout();
                        int lineForOffset22 = layout22.getLineForOffset(getSelectionStart());
                        int lineCount22 = layout22.getLineCount();
                        updateCursorPosition();
                        android.graphics.Rect bounds22 = this.gradientDrawable.getBounds();
                        android.graphics.Rect rect322 = this.rect;
                        rect322.left = bounds22.left;
                        rect322.right = bounds22.left + AndroidUtilities.dp(this.cursorWidth);
                        rect = this.rect;
                        i2 = bounds22.bottom;
                        rect.bottom = i2;
                        rect.top = bounds22.top;
                        f = this.lineSpacingExtra;
                        if (f != 0.0f) {
                        }
                        int centerY22 = rect.centerY();
                        int i722 = this.cursorSize;
                        rect.top = centerY22 - (i722 / 2);
                        android.graphics.Rect rect422 = this.rect;
                        rect422.bottom = rect422.top + i722;
                        this.gradientDrawable.setBounds(rect422);
                        this.gradientDrawable.draw(canvas);
                        canvas.restore();
                    }
                    i = 0;
                    canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + i);
                    Layout layout222 = getLayout();
                    int lineForOffset222 = layout222.getLineForOffset(getSelectionStart());
                    int lineCount222 = layout222.getLineCount();
                    updateCursorPosition();
                    android.graphics.Rect bounds222 = this.gradientDrawable.getBounds();
                    android.graphics.Rect rect3222 = this.rect;
                    rect3222.left = bounds222.left;
                    rect3222.right = bounds222.left + AndroidUtilities.dp(this.cursorWidth);
                    rect = this.rect;
                    i2 = bounds222.bottom;
                    rect.bottom = i2;
                    rect.top = bounds222.top;
                    f = this.lineSpacingExtra;
                    if (f != 0.0f) {
                    }
                    int centerY222 = rect.centerY();
                    int i7222 = this.cursorSize;
                    rect.top = centerY222 - (i7222 / 2);
                    android.graphics.Rect rect4222 = this.rect;
                    rect4222.bottom = rect4222.top + i7222;
                    this.gradientDrawable.setBounds(rect4222);
                    this.gradientDrawable.draw(canvas);
                    canvas.restore();
                }
                if (BuildVars.DEBUG_PRIVATE_VERSION) {
                    RuntimeException runtimeException = new RuntimeException(e3);
                }
            }
        } else if (this.cursorDrawn) {
            try {
                canvas.save();
                if (getVerticalOffsetMethod != null) {
                    if ((getGravity() & 112) != 48) {
                        i3 = ((Integer) getVerticalOffsetMethod.invoke(this, Boolean.TRUE)).intValue();
                        canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + i3);
                        Layout layout3 = getLayout();
                        int lineForOffset3 = layout3.getLineForOffset(getSelectionStart());
                        int lineCount3 = layout3.getLineCount();
                        updateCursorPosition();
                        android.graphics.Rect bounds3 = this.gradientDrawable.getBounds();
                        android.graphics.Rect rect5 = this.rect;
                        rect5.left = bounds3.left;
                        rect5.right = bounds3.left + AndroidUtilities.dp(this.cursorWidth);
                        rect2 = this.rect;
                        i4 = bounds3.bottom;
                        rect2.bottom = i4;
                        rect2.top = bounds3.top;
                        f2 = this.lineSpacingExtra;
                        if (f2 != 0.0f && lineForOffset3 < lineCount3 - 1) {
                            rect2.bottom = (int) (i4 - f2);
                        }
                        int centerY3 = rect2.centerY();
                        int i8 = this.cursorSize;
                        rect2.top = centerY3 - (i8 / 2);
                        android.graphics.Rect rect6 = this.rect;
                        rect6.bottom = rect6.top + i8;
                        this.gradientDrawable.setBounds(rect6);
                        this.gradientDrawable.draw(canvas);
                        canvas.restore();
                        this.cursorDrawn = false;
                    }
                    i3 = 0;
                    canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + i3);
                    Layout layout32 = getLayout();
                    int lineForOffset32 = layout32.getLineForOffset(getSelectionStart());
                    int lineCount32 = layout32.getLineCount();
                    updateCursorPosition();
                    android.graphics.Rect bounds32 = this.gradientDrawable.getBounds();
                    android.graphics.Rect rect52 = this.rect;
                    rect52.left = bounds32.left;
                    rect52.right = bounds32.left + AndroidUtilities.dp(this.cursorWidth);
                    rect2 = this.rect;
                    i4 = bounds32.bottom;
                    rect2.bottom = i4;
                    rect2.top = bounds32.top;
                    f2 = this.lineSpacingExtra;
                    if (f2 != 0.0f) {
                        rect2.bottom = (int) (i4 - f2);
                    }
                    int centerY32 = rect2.centerY();
                    int i82 = this.cursorSize;
                    rect2.top = centerY32 - (i82 / 2);
                    android.graphics.Rect rect62 = this.rect;
                    rect62.bottom = rect62.top + i82;
                    this.gradientDrawable.setBounds(rect62);
                    this.gradientDrawable.draw(canvas);
                    canvas.restore();
                    this.cursorDrawn = false;
                } else {
                    if ((getGravity() & 112) != 48) {
                        i3 = getTotalPaddingTop() - getExtendedPaddingTop();
                        canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + i3);
                        Layout layout322 = getLayout();
                        int lineForOffset322 = layout322.getLineForOffset(getSelectionStart());
                        int lineCount322 = layout322.getLineCount();
                        updateCursorPosition();
                        android.graphics.Rect bounds322 = this.gradientDrawable.getBounds();
                        android.graphics.Rect rect522 = this.rect;
                        rect522.left = bounds322.left;
                        rect522.right = bounds322.left + AndroidUtilities.dp(this.cursorWidth);
                        rect2 = this.rect;
                        i4 = bounds322.bottom;
                        rect2.bottom = i4;
                        rect2.top = bounds322.top;
                        f2 = this.lineSpacingExtra;
                        if (f2 != 0.0f) {
                        }
                        int centerY322 = rect2.centerY();
                        int i822 = this.cursorSize;
                        rect2.top = centerY322 - (i822 / 2);
                        android.graphics.Rect rect622 = this.rect;
                        rect622.bottom = rect622.top + i822;
                        this.gradientDrawable.setBounds(rect622);
                        this.gradientDrawable.draw(canvas);
                        canvas.restore();
                        this.cursorDrawn = false;
                    }
                    i3 = 0;
                    canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + i3);
                    Layout layout3222 = getLayout();
                    int lineForOffset3222 = layout3222.getLineForOffset(getSelectionStart());
                    int lineCount3222 = layout3222.getLineCount();
                    updateCursorPosition();
                    android.graphics.Rect bounds3222 = this.gradientDrawable.getBounds();
                    android.graphics.Rect rect5222 = this.rect;
                    rect5222.left = bounds3222.left;
                    rect5222.right = bounds3222.left + AndroidUtilities.dp(this.cursorWidth);
                    rect2 = this.rect;
                    i4 = bounds3222.bottom;
                    rect2.bottom = i4;
                    rect2.top = bounds3222.top;
                    f2 = this.lineSpacingExtra;
                    if (f2 != 0.0f) {
                    }
                    int centerY3222 = rect2.centerY();
                    int i8222 = this.cursorSize;
                    rect2.top = centerY3222 - (i8222 / 2);
                    android.graphics.Rect rect6222 = this.rect;
                    rect6222.bottom = rect6222.top + i8222;
                    this.gradientDrawable.setBounds(rect6222);
                    this.gradientDrawable.draw(canvas);
                    canvas.restore();
                    this.cursorDrawn = false;
                }
            } finally {
                if (BuildVars.DEBUG_PRIVATE_VERSION) {
                }
            }
            if (BuildVars.DEBUG_PRIVATE_VERSION) {
                RuntimeException runtimeException2 = new RuntimeException(th);
            }
        }
        if (!this.lineVisible || this.lineColor == 0) {
            return;
        }
        int dp = AndroidUtilities.dp(1.0f);
        boolean z5 = this.lineActive;
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
        if (this.lineActive != z5) {
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
        int i9 = this.lastTouchX;
        if (i9 < 0) {
            i9 = getMeasuredWidth() / 2;
        }
        int i10 = i9;
        int max = Math.max(i10, getMeasuredWidth() - i10) * 2;
        if (this.lineActiveness < 1.0f) {
            canvas.drawRect(getScrollX(), scrollY - dp, getScrollX() + getMeasuredWidth(), scrollY, this.linePaint);
        }
        float f8 = this.lineActiveness;
        if (f8 <= 0.0f) {
            return;
        }
        float interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f8);
        boolean z6 = this.lineActive;
        if (z6) {
            this.activeLineWidth = max * interpolation;
        }
        if (!z6) {
            f3 = interpolation;
        }
        float f9 = i10;
        canvas.drawRect(Math.max(0.0f, f9 - (this.activeLineWidth / 2.0f)) + getScrollX(), scrollY - ((int) (f3 * AndroidUtilities.dp(2.0f))), getScrollX() + Math.min(f9 + (this.activeLineWidth / 2.0f), getMeasuredWidth()), scrollY, this.activeLinePaint);
    }

    public void setWindowView(View view) {
        this.windowView = view;
    }

    private boolean updateCursorPosition() {
        Layout layout = getLayout();
        int selectionStart = getSelectionStart();
        int lineForOffset = layout.getLineForOffset(selectionStart);
        updateCursorPosition(layout.getLineTop(lineForOffset), layout.getLineTop(lineForOffset + 1), layout.getPrimaryHorizontal(selectionStart));
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
        if (Math.abs(f2) <= 1.0f || (TextUtils.isEmpty(getText()) && 1048576 - scrollX <= f3 + 1.0f && max <= 1.0f)) {
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

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        try {
            super.onAttachedToWindow();
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.attachedToWindow = getRootView();
        AndroidUtilities.runOnUIThread(this.invalidateRunnable);
    }

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
            this.floatingToolbar = new FloatingToolbar(context, view, getActionModeStyle(), getResourcesProvider());
            this.floatingActionMode = new FloatingActionMode(getContext(), new ActionModeCallback2Wrapper(callback), this, this.floatingToolbar);
            this.floatingToolbarPreDrawListener = new EditTextBoldCursor$$ExternalSyntheticLambda0(this);
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

    public /* synthetic */ boolean lambda$startActionMode$0() {
        FloatingActionMode floatingActionMode = this.floatingActionMode;
        if (floatingActionMode != null) {
            floatingActionMode.updateViewLocationInWindow();
            return true;
        }
        return true;
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
}
