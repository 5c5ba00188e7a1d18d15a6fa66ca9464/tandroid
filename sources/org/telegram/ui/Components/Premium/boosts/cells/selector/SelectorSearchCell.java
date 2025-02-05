package org.telegram.ui.Components.Premium.boosts.cells.selector;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ScrollView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.GroupCreateSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorSearchCell;

/* loaded from: classes3.dex */
public abstract class SelectorSearchCell extends ScrollView {
    public ArrayList allSpans;
    private final LinearGradient bottomGradient;
    private final AnimatedFloat bottomGradientAlpha;
    private final Matrix bottomGradientMatrix;
    private final Paint bottomGradientPaint;
    public float containerHeight;
    private GroupCreateSpan currentDeletingSpan;
    private EditTextBoldCursor editText;
    private int fieldY;
    private int hintTextWidth;
    private boolean ignoreScrollEvent;
    private boolean ignoreTextChange;
    private Utilities.Callback onSearchTextChange;
    private int prevResultContainerHeight;
    private final Theme.ResourcesProvider resourcesProvider;
    public int resultContainerHeight;
    private boolean scroll;
    public SpansContainer spansContainer;
    private final LinearGradient topGradient;
    private final AnimatedFloat topGradientAlpha;
    private final Matrix topGradientMatrix;
    private final Paint topGradientPaint;
    private Runnable updateHeight;

    public class SpansContainer extends ViewGroup {
        private View addingSpan;
        private ArrayList animAddingSpans;
        private ArrayList animRemovingSpans;
        private boolean animationStarted;
        private ArrayList animators;
        private AnimatorSet currentAnimation;
        private final int heightDp;
        private final int padDp;
        private final int padXDp;
        private final int padYDp;
        private final ArrayList removingSpans;

        public SpansContainer(Context context) {
            super(context);
            this.animAddingSpans = new ArrayList();
            this.animRemovingSpans = new ArrayList();
            this.animators = new ArrayList();
            this.removingSpans = new ArrayList();
            this.padDp = 14;
            this.padYDp = 4;
            this.padXDp = 6;
            this.heightDp = 28;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMeasure$0() {
            SelectorSearchCell.this.fullScroll(NotificationCenter.walletSyncProgressChanged);
        }

        private void setupEndValues() {
            AnimatorSet animatorSet = this.currentAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            for (int i = 0; i < this.animAddingSpans.size(); i++) {
                ((View) this.animAddingSpans.get(i)).setScaleX(1.0f);
                ((View) this.animAddingSpans.get(i)).setScaleY(1.0f);
                ((View) this.animAddingSpans.get(i)).setAlpha(1.0f);
            }
            for (int i2 = 0; i2 < this.animRemovingSpans.size(); i2++) {
                ((View) this.animRemovingSpans.get(i2)).setScaleX(0.0f);
                ((View) this.animRemovingSpans.get(i2)).setScaleY(0.0f);
                ((View) this.animRemovingSpans.get(i2)).setAlpha(0.0f);
            }
            this.animAddingSpans.clear();
            this.animRemovingSpans.clear();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:31:0x00d9  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onMeasure(int i, int i2) {
            int min;
            int i3;
            int childCount = getChildCount();
            int size = View.MeasureSpec.getSize(i);
            float f = 28.0f;
            int dp = size - AndroidUtilities.dp(28.0f);
            int dp2 = AndroidUtilities.dp(10.0f);
            int dp3 = AndroidUtilities.dp(10.0f);
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            while (i4 < childCount) {
                View childAt = getChildAt(i4);
                if (childAt instanceof GroupCreateSpan) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(f), 1073741824));
                    boolean contains = this.removingSpans.contains(childAt);
                    if (!contains && childAt.getMeasuredWidth() + i5 > dp) {
                        dp2 += childAt.getMeasuredHeight() + AndroidUtilities.dp(4.0f);
                        i5 = 0;
                    }
                    if (childAt.getMeasuredWidth() + i6 > dp) {
                        dp3 += childAt.getMeasuredHeight() + AndroidUtilities.dp(4.0f);
                        i6 = 0;
                    }
                    int dp4 = AndroidUtilities.dp(14.0f) + i5;
                    if (!this.animationStarted) {
                        if (contains) {
                            childAt.setTranslationX(AndroidUtilities.dp(14.0f) + i6);
                            childAt.setTranslationY(dp3);
                        } else {
                            if (this.removingSpans.isEmpty()) {
                                i3 = childCount;
                                childAt.setTranslationX(dp4);
                                childAt.setTranslationY(dp2);
                            } else {
                                float f2 = dp4;
                                if (childAt.getTranslationX() != f2) {
                                    i3 = childCount;
                                    this.animators.add(ObjectAnimator.ofFloat(childAt, (Property<View, Float>) View.TRANSLATION_X, f2));
                                } else {
                                    i3 = childCount;
                                }
                                float f3 = dp2;
                                if (childAt.getTranslationY() != f3) {
                                    this.animators.add(ObjectAnimator.ofFloat(childAt, (Property<View, Float>) View.TRANSLATION_Y, f3));
                                }
                            }
                            if (!contains) {
                                i5 += childAt.getMeasuredWidth() + AndroidUtilities.dp(6.0f);
                            }
                            i6 += childAt.getMeasuredWidth() + AndroidUtilities.dp(6.0f);
                        }
                    }
                    i3 = childCount;
                    if (!contains) {
                    }
                    i6 += childAt.getMeasuredWidth() + AndroidUtilities.dp(6.0f);
                } else {
                    i3 = childCount;
                }
                i4++;
                childCount = i3;
                f = 28.0f;
            }
            if (AndroidUtilities.isTablet()) {
                min = AndroidUtilities.dp(376.0f);
            } else {
                Point point = AndroidUtilities.displaySize;
                min = Math.min(point.x, point.y) - AndroidUtilities.dp(154.0f);
            }
            int i7 = min / 3;
            if (dp - i5 < i7) {
                dp2 += AndroidUtilities.dp(36.0f);
                i5 = 0;
            }
            if (dp - i6 < i7) {
                dp3 += AndroidUtilities.dp(36.0f);
            }
            SelectorSearchCell.this.editText.measure(View.MeasureSpec.makeMeasureSpec(dp - i5, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(28.0f), 1073741824));
            SelectorSearchCell.this.editText.setHintVisible(SelectorSearchCell.this.editText.getMeasuredWidth() > SelectorSearchCell.this.hintTextWidth, true);
            if (!this.animationStarted) {
                int dp5 = dp3 + AndroidUtilities.dp(38.0f);
                int dp6 = i5 + AndroidUtilities.dp(16.0f);
                SelectorSearchCell.this.fieldY = dp2;
                if (this.currentAnimation != null) {
                    int dp7 = dp2 + AndroidUtilities.dp(38.0f);
                    SelectorSearchCell selectorSearchCell = SelectorSearchCell.this;
                    selectorSearchCell.resultContainerHeight = dp7;
                    float f4 = dp7;
                    if (selectorSearchCell.containerHeight != f4) {
                        this.animators.add(selectorSearchCell.getContainerHeightAnimator(f4));
                    }
                    float f5 = dp6;
                    if (SelectorSearchCell.this.editText.getTranslationX() != f5) {
                        this.animators.add(ObjectAnimator.ofFloat(SelectorSearchCell.this.editText, (Property<EditTextBoldCursor, Float>) View.TRANSLATION_X, f5));
                    }
                    if (SelectorSearchCell.this.editText.getTranslationY() != SelectorSearchCell.this.fieldY) {
                        this.animators.add(ObjectAnimator.ofFloat(SelectorSearchCell.this.editText, (Property<EditTextBoldCursor, Float>) View.TRANSLATION_Y, SelectorSearchCell.this.fieldY));
                    }
                    SelectorSearchCell.this.editText.setAllowDrawCursor(false);
                    this.currentAnimation.playTogether(this.animators);
                    this.currentAnimation.setDuration(180L);
                    this.currentAnimation.setInterpolator(new LinearInterpolator());
                    this.currentAnimation.start();
                    this.animationStarted = true;
                    if (SelectorSearchCell.this.updateHeight != null) {
                        SelectorSearchCell.this.updateHeight.run();
                    }
                } else {
                    SelectorSearchCell selectorSearchCell2 = SelectorSearchCell.this;
                    selectorSearchCell2.resultContainerHeight = dp5;
                    selectorSearchCell2.containerHeight = dp5;
                    selectorSearchCell2.editText.setTranslationX(dp6);
                    SelectorSearchCell.this.editText.setTranslationY(SelectorSearchCell.this.fieldY);
                    if (SelectorSearchCell.this.updateHeight != null) {
                        SelectorSearchCell.this.updateHeight.run();
                    }
                    if (SelectorSearchCell.this.scroll) {
                        post(new Runnable() { // from class: org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorSearchCell$SpansContainer$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                SelectorSearchCell.SpansContainer.this.lambda$onMeasure$0();
                            }
                        });
                        SelectorSearchCell.this.scroll = false;
                    }
                }
                SelectorSearchCell selectorSearchCell3 = SelectorSearchCell.this;
                selectorSearchCell3.prevResultContainerHeight = selectorSearchCell3.resultContainerHeight;
            } else if (this.currentAnimation != null) {
                if (!SelectorSearchCell.this.ignoreScrollEvent && this.removingSpans.isEmpty()) {
                    SelectorSearchCell.this.editText.bringPointIntoView(SelectorSearchCell.this.editText.getSelectionStart());
                }
                if (SelectorSearchCell.this.scroll) {
                    SelectorSearchCell.this.fullScroll(NotificationCenter.walletSyncProgressChanged);
                    SelectorSearchCell.this.scroll = false;
                }
            }
            setMeasuredDimension(size, (int) SelectorSearchCell.this.containerHeight);
        }

        public void removeAllSpans(boolean z) {
            SelectorSearchCell.this.ignoreScrollEvent = true;
            final ArrayList arrayList = new ArrayList(SelectorSearchCell.this.allSpans);
            this.removingSpans.clear();
            this.removingSpans.addAll(SelectorSearchCell.this.allSpans);
            SelectorSearchCell.this.allSpans.clear();
            for (int i = 0; i < arrayList.size(); i++) {
                ((GroupCreateSpan) arrayList.get(i)).setOnClickListener(null);
            }
            setupEndValues();
            if (z) {
                this.animationStarted = false;
                AnimatorSet animatorSet = new AnimatorSet();
                this.currentAnimation = animatorSet;
                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorSearchCell.SpansContainer.3
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        for (int i2 = 0; i2 < arrayList.size(); i2++) {
                            SpansContainer.this.removeView((View) arrayList.get(i2));
                        }
                        SpansContainer.this.removingSpans.clear();
                        SpansContainer.this.currentAnimation = null;
                        SpansContainer.this.animationStarted = false;
                        SelectorSearchCell.this.editText.setAllowDrawCursor(true);
                        if (SelectorSearchCell.this.updateHeight != null) {
                            SelectorSearchCell.this.updateHeight.run();
                        }
                        if (SelectorSearchCell.this.scroll) {
                            SelectorSearchCell.this.fullScroll(NotificationCenter.walletSyncProgressChanged);
                            SelectorSearchCell.this.scroll = false;
                        }
                    }
                });
                this.animators.clear();
                this.animAddingSpans.clear();
                this.animRemovingSpans.clear();
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    GroupCreateSpan groupCreateSpan = (GroupCreateSpan) arrayList.get(i2);
                    this.animAddingSpans.add(groupCreateSpan);
                    this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.SCALE_X, 1.0f, 0.01f));
                    this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.SCALE_Y, 1.0f, 0.01f));
                    this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.ALPHA, 1.0f, 0.0f));
                }
            } else {
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    removeView((View) arrayList.get(i3));
                }
                this.removingSpans.clear();
                this.currentAnimation = null;
                this.animationStarted = false;
                SelectorSearchCell.this.editText.setAllowDrawCursor(true);
            }
            requestLayout();
        }

        public void removeSpan(final GroupCreateSpan groupCreateSpan) {
            SelectorSearchCell.this.ignoreScrollEvent = true;
            SelectorSearchCell.this.allSpans.remove(groupCreateSpan);
            groupCreateSpan.setOnClickListener(null);
            setupEndValues();
            this.animationStarted = false;
            AnimatorSet animatorSet = new AnimatorSet();
            this.currentAnimation = animatorSet;
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorSearchCell.SpansContainer.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SpansContainer.this.removeView(groupCreateSpan);
                    SpansContainer.this.removingSpans.clear();
                    SpansContainer.this.currentAnimation = null;
                    SpansContainer.this.animationStarted = false;
                    SelectorSearchCell.this.editText.setAllowDrawCursor(true);
                    if (SelectorSearchCell.this.updateHeight != null) {
                        SelectorSearchCell.this.updateHeight.run();
                    }
                    if (SelectorSearchCell.this.scroll) {
                        SelectorSearchCell.this.fullScroll(NotificationCenter.walletSyncProgressChanged);
                        SelectorSearchCell.this.scroll = false;
                    }
                }
            });
            this.removingSpans.clear();
            this.removingSpans.add(groupCreateSpan);
            this.animAddingSpans.clear();
            this.animRemovingSpans.clear();
            this.animAddingSpans.add(groupCreateSpan);
            this.animators.clear();
            this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.SCALE_X, 1.0f, 0.01f));
            this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.SCALE_Y, 1.0f, 0.01f));
            this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.ALPHA, 1.0f, 0.0f));
            requestLayout();
        }

        public void updateSpans(final ArrayList arrayList, ArrayList arrayList2, boolean z) {
            SelectorSearchCell.this.ignoreScrollEvent = true;
            SelectorSearchCell.this.allSpans.removeAll(arrayList);
            SelectorSearchCell.this.allSpans.addAll(arrayList2);
            this.removingSpans.clear();
            this.removingSpans.addAll(arrayList);
            for (int i = 0; i < arrayList.size(); i++) {
                ((GroupCreateSpan) arrayList.get(i)).setOnClickListener(null);
            }
            setupEndValues();
            if (z) {
                this.animationStarted = false;
                AnimatorSet animatorSet = new AnimatorSet();
                this.currentAnimation = animatorSet;
                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorSearchCell.SpansContainer.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        for (int i2 = 0; i2 < arrayList.size(); i2++) {
                            SpansContainer.this.removeView((View) arrayList.get(i2));
                        }
                        SpansContainer.this.addingSpan = null;
                        SpansContainer.this.removingSpans.clear();
                        SpansContainer.this.currentAnimation = null;
                        SpansContainer.this.animationStarted = false;
                        SelectorSearchCell.this.editText.setAllowDrawCursor(true);
                        if (SelectorSearchCell.this.updateHeight != null) {
                            SelectorSearchCell.this.updateHeight.run();
                        }
                        if (SelectorSearchCell.this.scroll) {
                            SelectorSearchCell.this.fullScroll(NotificationCenter.walletSyncProgressChanged);
                            SelectorSearchCell.this.scroll = false;
                        }
                    }
                });
                this.animators.clear();
                this.animAddingSpans.clear();
                this.animRemovingSpans.clear();
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    GroupCreateSpan groupCreateSpan = (GroupCreateSpan) arrayList.get(i2);
                    this.animRemovingSpans.add(groupCreateSpan);
                    this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.SCALE_X, 1.0f, 0.01f));
                    this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.SCALE_Y, 1.0f, 0.01f));
                    this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.ALPHA, 1.0f, 0.0f));
                }
                for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                    GroupCreateSpan groupCreateSpan2 = (GroupCreateSpan) arrayList2.get(i3);
                    this.animAddingSpans.add(groupCreateSpan2);
                    this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan2, (Property<GroupCreateSpan, Float>) View.SCALE_X, 0.01f, 1.0f));
                    this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan2, (Property<GroupCreateSpan, Float>) View.SCALE_Y, 0.01f, 1.0f));
                    this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan2, (Property<GroupCreateSpan, Float>) View.ALPHA, 0.0f, 1.0f));
                }
            } else {
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    removeView((View) arrayList.get(i4));
                }
                this.addingSpan = null;
                this.removingSpans.clear();
                this.currentAnimation = null;
                this.animationStarted = false;
                SelectorSearchCell.this.editText.setAllowDrawCursor(true);
            }
            for (int i5 = 0; i5 < arrayList2.size(); i5++) {
                addView((View) arrayList2.get(i5));
            }
            requestLayout();
        }
    }

    public SelectorSearchCell(Context context, Theme.ResourcesProvider resourcesProvider, Runnable runnable) {
        super(context);
        this.allSpans = new ArrayList();
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.topGradientAlpha = new AnimatedFloat(this, 0L, 300L, cubicBezierInterpolator);
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(8.0f), new int[]{-16777216, 0}, new float[]{0.0f, 1.0f}, tileMode);
        this.topGradient = linearGradient;
        Paint paint = new Paint(1);
        this.topGradientPaint = paint;
        this.topGradientMatrix = new Matrix();
        this.bottomGradientAlpha = new AnimatedFloat(this, 0L, 300L, cubicBezierInterpolator);
        LinearGradient linearGradient2 = new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(8.0f), new int[]{0, -16777216}, new float[]{0.0f, 1.0f}, tileMode);
        this.bottomGradient = linearGradient2;
        Paint paint2 = new Paint(1);
        this.bottomGradientPaint = paint2;
        this.bottomGradientMatrix = new Matrix();
        paint.setShader(linearGradient);
        PorterDuff.Mode mode = PorterDuff.Mode.DST_OUT;
        paint.setXfermode(new PorterDuffXfermode(mode));
        paint2.setShader(linearGradient2);
        paint2.setXfermode(new PorterDuffXfermode(mode));
        this.resourcesProvider = resourcesProvider;
        this.updateHeight = runnable;
        setVerticalScrollBarEnabled(false);
        AndroidUtilities.setScrollViewEdgeEffectColor(this, Theme.getColor(Theme.key_windowBackgroundWhite));
        SpansContainer spansContainer = new SpansContainer(context);
        this.spansContainer = spansContainer;
        addView(spansContainer, LayoutHelper.createFrame(-1, -2.0f));
        EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context) { // from class: org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorSearchCell.1
            @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (SelectorSearchCell.this.currentDeletingSpan != null) {
                    SelectorSearchCell.this.currentDeletingSpan.cancelDeleteAnimation();
                    SelectorSearchCell.this.currentDeletingSpan = null;
                }
                if (motionEvent.getAction() == 0 && !AndroidUtilities.showKeyboard(this)) {
                    SelectorSearchCell.this.fullScroll(NotificationCenter.walletSyncProgressChanged);
                    clearFocus();
                    requestFocus();
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.editText = editTextBoldCursor;
        if (Build.VERSION.SDK_INT >= 25) {
            editTextBoldCursor.setRevealOnFocusHint(false);
        }
        this.editText.setTextSize(1, 16.0f);
        this.editText.setHintColor(Theme.getColor(Theme.key_groupcreate_hintText, resourcesProvider));
        this.editText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
        EditTextBoldCursor editTextBoldCursor2 = this.editText;
        int i = Theme.key_groupcreate_cursor;
        editTextBoldCursor2.setCursorColor(Theme.getColor(i, resourcesProvider));
        this.editText.setHandlesColor(Theme.getColor(i, resourcesProvider));
        this.editText.setCursorWidth(1.5f);
        this.editText.setInputType(655536);
        this.editText.setSingleLine(true);
        this.editText.setBackgroundDrawable(null);
        this.editText.setVerticalScrollBarEnabled(false);
        this.editText.setHorizontalScrollBarEnabled(false);
        this.editText.setTextIsSelectable(false);
        this.editText.setPadding(0, 0, 0, 0);
        this.editText.setImeOptions(268435462);
        this.editText.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        this.spansContainer.addView(this.editText);
        EditTextBoldCursor editTextBoldCursor3 = this.editText;
        int i2 = R.string.Search;
        editTextBoldCursor3.setHintText(LocaleController.getString(i2));
        this.hintTextWidth = (int) this.editText.getPaint().measureText(LocaleController.getString(i2));
        this.editText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorSearchCell.2
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (SelectorSearchCell.this.ignoreTextChange || SelectorSearchCell.this.onSearchTextChange == null || editable == null) {
                    return;
                }
                SelectorSearchCell.this.onSearchTextChange.run(editable.toString());
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getContainerHeightAnimator$1(ValueAnimator valueAnimator) {
        setContainerHeight(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onDeleteSpanClicked, reason: merged with bridge method [inline-methods] */
    public void lambda$updateSpans$0(View view, HashSet hashSet, Runnable runnable) {
        if (this.allSpans.contains(view)) {
            GroupCreateSpan groupCreateSpan = (GroupCreateSpan) view;
            if (groupCreateSpan.isDeleting()) {
                this.currentDeletingSpan = null;
                this.spansContainer.removeSpan(groupCreateSpan);
                hashSet.remove(Long.valueOf(groupCreateSpan.getUid()));
                runnable.run();
                return;
            }
            GroupCreateSpan groupCreateSpan2 = this.currentDeletingSpan;
            if (groupCreateSpan2 != null) {
                groupCreateSpan2.cancelDeleteAnimation();
                this.currentDeletingSpan = null;
            }
            this.currentDeletingSpan = groupCreateSpan;
            groupCreateSpan.startDeleteAnimation();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        float scrollY = getScrollY();
        canvas.saveLayerAlpha(0.0f, scrollY, getWidth(), getHeight() + r0, NotificationCenter.liveLocationsChanged, 31);
        super.dispatchDraw(canvas);
        canvas.save();
        float f = this.topGradientAlpha.set(canScrollVertically(-1));
        this.topGradientMatrix.reset();
        this.topGradientMatrix.postTranslate(0.0f, scrollY);
        this.topGradient.setLocalMatrix(this.topGradientMatrix);
        this.topGradientPaint.setAlpha((int) (f * 255.0f));
        canvas.drawRect(0.0f, scrollY, getWidth(), AndroidUtilities.dp(8.0f) + r0, this.topGradientPaint);
        float f2 = this.bottomGradientAlpha.set(canScrollVertically(1));
        this.bottomGradientMatrix.reset();
        this.bottomGradientMatrix.postTranslate(0.0f, (getHeight() + r0) - AndroidUtilities.dp(8.0f));
        this.bottomGradient.setLocalMatrix(this.bottomGradientMatrix);
        this.bottomGradientPaint.setAlpha((int) (f2 * 255.0f));
        canvas.drawRect(0.0f, (getHeight() + r0) - AndroidUtilities.dp(8.0f), getWidth(), r0 + getHeight(), this.bottomGradientPaint);
        canvas.restore();
        canvas.restore();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return super.dispatchTouchEvent(motionEvent);
    }

    protected Animator getContainerHeightAnimator(float f) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.containerHeight, f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorSearchCell$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SelectorSearchCell.this.lambda$getContainerHeightAnimator$1(valueAnimator);
            }
        });
        return ofFloat;
    }

    public EditTextBoldCursor getEditText() {
        return this.editText;
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(150.0f), Integer.MIN_VALUE));
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
        if (this.ignoreScrollEvent) {
            this.ignoreScrollEvent = false;
            return false;
        }
        rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
        rect.top += this.fieldY + AndroidUtilities.dp(20.0f);
        rect.bottom += this.fieldY + AndroidUtilities.dp(50.0f);
        return super.requestChildRectangleOnScreen(view, rect, z);
    }

    public void setContainerHeight(float f) {
        this.containerHeight = f;
        SpansContainer spansContainer = this.spansContainer;
        if (spansContainer != null) {
            spansContainer.requestLayout();
        }
    }

    public void setHintText(String str, boolean z) {
        this.editText.setHintText(str, z);
    }

    public void setOnSearchTextChange(Utilities.Callback<String> callback) {
        this.onSearchTextChange = callback;
    }

    public void setText(CharSequence charSequence) {
        this.ignoreTextChange = true;
        this.editText.setText(charSequence);
        this.ignoreTextChange = false;
    }

    public void updateSpans(boolean z, final HashSet hashSet, final Runnable runnable, List list) {
        Object obj;
        MessagesController messagesController = MessagesController.getInstance(UserConfig.selectedAccount);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < this.allSpans.size(); i++) {
            GroupCreateSpan groupCreateSpan = (GroupCreateSpan) this.allSpans.get(i);
            if (!hashSet.contains(Long.valueOf(groupCreateSpan.getUid()))) {
                arrayList.add(groupCreateSpan);
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            Long l = (Long) it.next();
            long longValue = l.longValue();
            int i2 = 0;
            while (true) {
                if (i2 >= this.allSpans.size()) {
                    Object user = longValue >= 0 ? messagesController.getUser(l) : messagesController.getChat(Long.valueOf(-longValue));
                    if (list != null) {
                        Iterator it2 = list.iterator();
                        while (it2.hasNext()) {
                            TLRPC.TL_help_country tL_help_country = (TLRPC.TL_help_country) it2.next();
                            if (tL_help_country.default_name.hashCode() == longValue) {
                                obj = tL_help_country;
                                break;
                            }
                        }
                    }
                    obj = user;
                    if (obj != null) {
                        GroupCreateSpan groupCreateSpan2 = new GroupCreateSpan(getContext(), obj, null, true, this.resourcesProvider);
                        groupCreateSpan2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorSearchCell$$ExternalSyntheticLambda1
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                SelectorSearchCell.this.lambda$updateSpans$0(hashSet, runnable, view);
                            }
                        });
                        arrayList2.add(groupCreateSpan2);
                    }
                } else if (((GroupCreateSpan) this.allSpans.get(i2)).getUid() == longValue) {
                    break;
                } else {
                    i2++;
                }
            }
        }
        if (!arrayList.isEmpty() || !arrayList2.isEmpty()) {
            this.spansContainer.updateSpans(arrayList, arrayList2, z);
        }
        this.editText.setOnKeyListener(new View.OnKeyListener() { // from class: org.telegram.ui.Components.Premium.boosts.cells.selector.SelectorSearchCell.3
            private boolean wasEmpty;

            @Override // android.view.View.OnKeyListener
            public boolean onKey(View view, int i3, KeyEvent keyEvent) {
                if (i3 == 67) {
                    if (keyEvent.getAction() == 0) {
                        this.wasEmpty = SelectorSearchCell.this.editText.length() == 0;
                    } else if (keyEvent.getAction() == 1 && this.wasEmpty && !SelectorSearchCell.this.allSpans.isEmpty()) {
                        ArrayList arrayList3 = SelectorSearchCell.this.allSpans;
                        SelectorSearchCell.this.lambda$updateSpans$0((GroupCreateSpan) arrayList3.get(arrayList3.size() - 1), hashSet, runnable);
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
