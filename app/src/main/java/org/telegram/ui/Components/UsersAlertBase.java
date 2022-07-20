package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.GroupCallTextCell;
import org.telegram.ui.Cells.GroupCallUserCell;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class UsersAlertBase extends BottomSheet {
    private int backgroundColor;
    private float colorProgress;
    protected StickerEmptyView emptyView;
    protected FlickerLoadingView flickerLoadingView;
    protected FrameLayout frameLayout;
    protected final FillLastLinearLayoutManager layoutManager;
    protected RecyclerListView listView;
    protected RecyclerView.Adapter listViewAdapter;
    protected int scrollOffsetY;
    protected RecyclerView.Adapter searchListViewAdapter;
    protected SearchField searchView;
    protected View shadow;
    protected AnimatorSet shadowAnimation;
    protected Drawable shadowDrawable;
    private RectF rect = new RectF();
    protected boolean needSnapToTop = true;
    protected boolean isEmptyViewVisible = true;
    protected String keyScrollUp = "key_sheet_scrollUp";
    protected String keyListSelector = "listSelectorSDK21";
    protected String keySearchBackground = "dialogSearchBackground";
    protected String keyInviteMembersBackground = "windowBackgroundWhite";
    protected String keyListViewBackground = "windowBackgroundWhite";
    protected String keyActionBarUnscrolled = "windowBackgroundWhite";
    protected String keyNameText = "windowBackgroundWhiteBlackText";
    protected String keyLastSeenText = "windowBackgroundWhiteGrayText";
    protected String keyLastSeenTextUnscrolled = "windowBackgroundWhiteGrayText";
    protected String keySearchPlaceholder = "dialogSearchHint";
    protected String keySearchText = "dialogSearchText";
    protected String keySearchIcon = "dialogSearchIcon";
    protected String keySearchIconUnscrolled = "dialogSearchIcon";

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

    protected void onSearchViewTouched(MotionEvent motionEvent, EditTextBoldCursor editTextBoldCursor) {
    }

    public void search(String str) {
    }

    protected void updateColorKeys() {
    }

    public UsersAlertBase(Context context, boolean z, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context, z, resourcesProvider);
        updateColorKeys();
        setDimBehindAlpha(75);
        this.currentAccount = i;
        this.shadowDrawable = context.getResources().getDrawable(2131166140).mutate();
        ContainerView createContainerView = createContainerView(context);
        this.containerView = createContainerView;
        createContainerView.setWillNotDraw(false);
        this.containerView.setClipChildren(false);
        ViewGroup viewGroup = this.containerView;
        int i2 = this.backgroundPaddingLeft;
        viewGroup.setPadding(i2, 0, i2, 0);
        this.frameLayout = new FrameLayout(context);
        SearchField searchField = new SearchField(context);
        this.searchView = searchField;
        this.frameLayout.addView(searchField, LayoutHelper.createFrame(-1, -1, 51));
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context);
        this.flickerLoadingView = flickerLoadingView;
        flickerLoadingView.setViewType(6);
        this.flickerLoadingView.showDate(false);
        this.flickerLoadingView.setUseHeaderOffset(true);
        this.flickerLoadingView.setColors(this.keyInviteMembersBackground, this.keySearchBackground, this.keyActionBarUnscrolled);
        StickerEmptyView stickerEmptyView = new StickerEmptyView(context, this.flickerLoadingView, 1);
        this.emptyView = stickerEmptyView;
        stickerEmptyView.addView(this.flickerLoadingView, 0, LayoutHelper.createFrame(-1, -1.0f, 0, 0.0f, 2.0f, 0.0f, 0.0f));
        this.emptyView.title.setText(LocaleController.getString("NoResult", 2131626910));
        this.emptyView.subtitle.setText(LocaleController.getString("SearchEmptyViewFilteredSubtitle2", 2131628160));
        this.emptyView.setVisibility(8);
        this.emptyView.setAnimateLayoutChange(true);
        this.emptyView.showProgress(true, false);
        this.emptyView.setColors(this.keyNameText, this.keyLastSeenText, this.keyInviteMembersBackground, this.keySearchBackground);
        this.containerView.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 62.0f, 0.0f, 0.0f));
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(context);
        this.listView = anonymousClass1;
        anonymousClass1.setTag(13);
        this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
        this.listView.setClipToPadding(false);
        this.listView.setHideIfEmpty(false);
        this.listView.setSelectorDrawableColor(Theme.getColor(this.keyListSelector));
        FillLastLinearLayoutManager fillLastLinearLayoutManager = new FillLastLinearLayoutManager(getContext(), 1, false, AndroidUtilities.dp(8.0f), this.listView);
        this.layoutManager = fillLastLinearLayoutManager;
        fillLastLinearLayoutManager.setBind(false);
        this.listView.setLayoutManager(fillLastLinearLayoutManager);
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        this.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        this.listView.setOnScrollListener(new AnonymousClass2());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
        layoutParams.topMargin = AndroidUtilities.dp(58.0f);
        View view = new View(context);
        this.shadow = view;
        view.setBackgroundColor(Theme.getColor("dialogShadowLine"));
        this.shadow.setAlpha(0.0f);
        this.shadow.setTag(1);
        this.containerView.addView(this.shadow, layoutParams);
        this.containerView.addView(this.frameLayout, LayoutHelper.createFrame(-1, 58, 51));
        setColorProgress(0.0f);
        this.listView.setEmptyView(this.emptyView);
        this.listView.setAnimateEmptyView(true, 0);
    }

    /* renamed from: org.telegram.ui.Components.UsersAlertBase$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends RecyclerListView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            UsersAlertBase.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView
        protected boolean allowSelectChildAtPosition(float f, float f2) {
            return UsersAlertBase.this.isAllowSelectChildAtPosition(f, f2);
        }

        @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
        public void setTranslationY(float f) {
            super.setTranslationY(f);
            getLocationInWindow(new int[2]);
        }

        @Override // org.telegram.ui.Components.RecyclerListView
        public boolean emptyViewIsVisible() {
            return getAdapter() != null && UsersAlertBase.this.isEmptyViewVisible && getAdapter().getItemCount() <= 2;
        }
    }

    /* renamed from: org.telegram.ui.Components.UsersAlertBase$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends RecyclerView.OnScrollListener {
        AnonymousClass2() {
            UsersAlertBase.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            UsersAlertBase.this.updateLayout();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            RecyclerListView.Holder holder;
            if (i == 0) {
                UsersAlertBase usersAlertBase = UsersAlertBase.this;
                if (!usersAlertBase.needSnapToTop || usersAlertBase.scrollOffsetY + ((BottomSheet) usersAlertBase).backgroundPaddingTop + AndroidUtilities.dp(13.0f) >= AndroidUtilities.statusBarHeight * 2 || !UsersAlertBase.this.listView.canScrollVertically(1) || (holder = (RecyclerListView.Holder) UsersAlertBase.this.listView.findViewHolderForAdapterPosition(0)) == null || holder.itemView.getTop() <= 0) {
                    return;
                }
                UsersAlertBase.this.listView.smoothScrollBy(0, holder.itemView.getTop());
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        AndroidUtilities.statusBarHeight = AndroidUtilities.getStatusBarHeight(getContext());
    }

    protected ContainerView createContainerView(Context context) {
        return new ContainerView(context);
    }

    protected boolean isAllowSelectChildAtPosition(float f, float f2) {
        return f2 >= ((float) (AndroidUtilities.dp(58.0f) + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)));
    }

    /* loaded from: classes3.dex */
    public class SearchField extends FrameLayout {
        private final ImageView clearSearchImageView;
        private final CloseProgressDrawable2 progressDrawable;
        private final View searchBackground;
        protected EditTextBoldCursor searchEditText;
        private final ImageView searchIconImageView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SearchField(Context context) {
            super(context);
            UsersAlertBase.this = r12;
            View view = new View(context);
            this.searchBackground = view;
            view.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), Theme.getColor(r12.keySearchBackground)));
            addView(view, LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 11.0f, 14.0f, 0.0f));
            ImageView imageView = new ImageView(context);
            this.searchIconImageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageResource(2131166150);
            imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(r12.keySearchPlaceholder), PorterDuff.Mode.MULTIPLY));
            addView(imageView, LayoutHelper.createFrame(36, 36.0f, 51, 16.0f, 11.0f, 0.0f, 0.0f));
            ImageView imageView2 = new ImageView(context);
            this.clearSearchImageView = imageView2;
            imageView2.setScaleType(ImageView.ScaleType.CENTER);
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(r12);
            this.progressDrawable = anonymousClass1;
            imageView2.setImageDrawable(anonymousClass1);
            anonymousClass1.setSide(AndroidUtilities.dp(7.0f));
            imageView2.setScaleX(0.1f);
            imageView2.setScaleY(0.1f);
            imageView2.setAlpha(0.0f);
            addView(imageView2, LayoutHelper.createFrame(36, 36.0f, 53, 14.0f, 11.0f, 14.0f, 0.0f));
            imageView2.setOnClickListener(new UsersAlertBase$SearchField$$ExternalSyntheticLambda0(this));
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(context, r12);
            this.searchEditText = anonymousClass2;
            anonymousClass2.setTextSize(1, 16.0f);
            this.searchEditText.setHintTextColor(Theme.getColor(r12.keySearchPlaceholder));
            this.searchEditText.setTextColor(Theme.getColor(r12.keySearchText));
            this.searchEditText.setBackgroundDrawable(null);
            this.searchEditText.setPadding(0, 0, 0, 0);
            this.searchEditText.setMaxLines(1);
            this.searchEditText.setLines(1);
            this.searchEditText.setSingleLine(true);
            this.searchEditText.setImeOptions(268435459);
            this.searchEditText.setHint(LocaleController.getString("VoipGroupSearchMembers", 2131629171));
            this.searchEditText.setCursorColor(Theme.getColor(r12.keySearchText));
            this.searchEditText.setCursorSize(AndroidUtilities.dp(20.0f));
            this.searchEditText.setCursorWidth(1.5f);
            addView(this.searchEditText, LayoutHelper.createFrame(-1, 40.0f, 51, 54.0f, 9.0f, 46.0f, 0.0f));
            this.searchEditText.addTextChangedListener(new AnonymousClass3(r12));
            this.searchEditText.setOnEditorActionListener(new UsersAlertBase$SearchField$$ExternalSyntheticLambda1(this));
        }

        /* renamed from: org.telegram.ui.Components.UsersAlertBase$SearchField$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends CloseProgressDrawable2 {
            AnonymousClass1(UsersAlertBase usersAlertBase) {
                SearchField.this = r1;
            }

            @Override // org.telegram.ui.Components.CloseProgressDrawable2
            protected int getCurrentColor() {
                return Theme.getColor(UsersAlertBase.this.keySearchPlaceholder);
            }
        }

        public /* synthetic */ void lambda$new$0(View view) {
            this.searchEditText.setText("");
            AndroidUtilities.showKeyboard(this.searchEditText);
        }

        /* renamed from: org.telegram.ui.Components.UsersAlertBase$SearchField$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends EditTextBoldCursor {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass2(Context context, UsersAlertBase usersAlertBase) {
                super(context);
                SearchField.this = r1;
            }

            @Override // org.telegram.ui.Components.EditTextEffects, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                obtain.setLocation(obtain.getRawX(), obtain.getRawY() - ((BottomSheet) UsersAlertBase.this).containerView.getTranslationY());
                if (obtain.getAction() == 1) {
                    obtain.setAction(3);
                }
                UsersAlertBase.this.listView.dispatchTouchEvent(obtain);
                obtain.recycle();
                return super.dispatchTouchEvent(motionEvent);
            }
        }

        /* renamed from: org.telegram.ui.Components.UsersAlertBase$SearchField$3 */
        /* loaded from: classes3.dex */
        public class AnonymousClass3 implements TextWatcher {
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass3(UsersAlertBase usersAlertBase) {
                SearchField.this = r1;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                RecyclerListView recyclerListView;
                boolean z = SearchField.this.searchEditText.length() > 0;
                float f = 0.0f;
                if (z != (SearchField.this.clearSearchImageView.getAlpha() != 0.0f)) {
                    ViewPropertyAnimator animate = SearchField.this.clearSearchImageView.animate();
                    float f2 = 1.0f;
                    if (z) {
                        f = 1.0f;
                    }
                    ViewPropertyAnimator scaleX = animate.alpha(f).setDuration(150L).scaleX(z ? 1.0f : 0.1f);
                    if (!z) {
                        f2 = 0.1f;
                    }
                    scaleX.scaleY(f2).start();
                }
                String obj = SearchField.this.searchEditText.getText().toString();
                int itemCount = UsersAlertBase.this.listView.getAdapter() == null ? 0 : UsersAlertBase.this.listView.getAdapter().getItemCount();
                UsersAlertBase.this.search(obj);
                if (TextUtils.isEmpty(obj) && (recyclerListView = UsersAlertBase.this.listView) != null) {
                    RecyclerView.Adapter adapter = recyclerListView.getAdapter();
                    UsersAlertBase usersAlertBase = UsersAlertBase.this;
                    if (adapter != usersAlertBase.listViewAdapter) {
                        usersAlertBase.listView.setAnimateEmptyView(false, 0);
                        UsersAlertBase usersAlertBase2 = UsersAlertBase.this;
                        usersAlertBase2.listView.setAdapter(usersAlertBase2.listViewAdapter);
                        UsersAlertBase.this.listView.setAnimateEmptyView(true, 0);
                        if (itemCount == 0) {
                            UsersAlertBase.this.showItemsAnimated(0);
                        }
                    }
                }
                UsersAlertBase.this.flickerLoadingView.setVisibility(0);
            }
        }

        public /* synthetic */ boolean lambda$new$1(TextView textView, int i, KeyEvent keyEvent) {
            if (keyEvent != null) {
                if ((keyEvent.getAction() != 1 || keyEvent.getKeyCode() != 84) && (keyEvent.getAction() != 0 || keyEvent.getKeyCode() != 66)) {
                    return false;
                }
                AndroidUtilities.hideKeyboard(this.searchEditText);
                return false;
            }
            return false;
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            UsersAlertBase.this.onSearchViewTouched(motionEvent, this.searchEditText);
            return super.onInterceptTouchEvent(motionEvent);
        }

        public void closeSearch() {
            this.clearSearchImageView.callOnClick();
            AndroidUtilities.hideKeyboard(this.searchEditText);
        }
    }

    /* renamed from: org.telegram.ui.Components.UsersAlertBase$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends AnimationProperties.FloatProperty<UsersAlertBase> {
        AnonymousClass3(String str) {
            super(str);
        }

        public void setValue(UsersAlertBase usersAlertBase, float f) {
            usersAlertBase.setColorProgress(f);
        }

        public Float get(UsersAlertBase usersAlertBase) {
            return Float.valueOf(usersAlertBase.getColorProgress());
        }
    }

    static {
        new AnonymousClass3("colorProgress");
    }

    public float getColorProgress() {
        return this.colorProgress;
    }

    public void setColorProgress(float f) {
        this.colorProgress = f;
        this.backgroundColor = AndroidUtilities.getOffsetColor(Theme.getColor(this.keyInviteMembersBackground), Theme.getColor(this.keyListViewBackground), f, 1.0f);
        this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(this.backgroundColor, PorterDuff.Mode.MULTIPLY));
        this.frameLayout.setBackgroundColor(this.backgroundColor);
        int i = this.backgroundColor;
        this.navBarColor = i;
        this.listView.setGlowColor(i);
        int offsetColor = AndroidUtilities.getOffsetColor(Theme.getColor(this.keyLastSeenTextUnscrolled), Theme.getColor(this.keyLastSeenText), f, 1.0f);
        int offsetColor2 = AndroidUtilities.getOffsetColor(Theme.getColor(this.keySearchIconUnscrolled), Theme.getColor(this.keySearchIcon), f, 1.0f);
        int childCount = this.listView.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = this.listView.getChildAt(i2);
            if (childAt instanceof GroupCallTextCell) {
                ((GroupCallTextCell) childAt).setColors(offsetColor, offsetColor);
            } else if (childAt instanceof GroupCallUserCell) {
                ((GroupCallUserCell) childAt).setGrayIconColor(this.shadow.getTag() != null ? this.keySearchIcon : this.keySearchIconUnscrolled, offsetColor2);
            }
        }
        this.containerView.invalidate();
        this.listView.invalidate();
        this.container.invalidate();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        AndroidUtilities.hideKeyboard(this.searchView.searchEditText);
        super.dismiss();
    }

    @SuppressLint({"NewApi"})
    public void updateLayout() {
        if (this.listView.getChildCount() <= 0) {
            return;
        }
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(0);
        int top = findViewHolderForAdapterPosition != null ? findViewHolderForAdapterPosition.itemView.getTop() - AndroidUtilities.dp(8.0f) : 0;
        int i = (top <= 0 || findViewHolderForAdapterPosition == null || findViewHolderForAdapterPosition.getAdapterPosition() != 0) ? 0 : top;
        if (top >= 0 && findViewHolderForAdapterPosition != null && findViewHolderForAdapterPosition.getAdapterPosition() == 0) {
            runShadowAnimation(false);
        } else {
            runShadowAnimation(true);
            top = i;
        }
        if (this.scrollOffsetY == top) {
            return;
        }
        this.scrollOffsetY = top;
        setTranslationY(top);
    }

    public void setTranslationY(int i) {
        this.listView.setTopGlowOffset(i);
        float f = i;
        this.frameLayout.setTranslationY(f);
        this.emptyView.setTranslationY(f);
        this.containerView.invalidate();
    }

    private void runShadowAnimation(boolean z) {
        if ((!z || this.shadow.getTag() == null) && (z || this.shadow.getTag() != null)) {
            return;
        }
        this.shadow.setTag(z ? null : 1);
        if (z) {
            this.shadow.setVisibility(0);
        }
        AnimatorSet animatorSet = this.shadowAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.shadowAnimation = animatorSet2;
        Animator[] animatorArr = new Animator[1];
        View view = this.shadow;
        Property property = View.ALPHA;
        float[] fArr = new float[1];
        fArr[0] = z ? 1.0f : 0.0f;
        animatorArr[0] = ObjectAnimator.ofFloat(view, property, fArr);
        animatorSet2.playTogether(animatorArr);
        this.shadowAnimation.setDuration(150L);
        this.shadowAnimation.addListener(new AnonymousClass4(z));
        this.shadowAnimation.start();
    }

    /* renamed from: org.telegram.ui.Components.UsersAlertBase$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$show;

        AnonymousClass4(boolean z) {
            UsersAlertBase.this = r1;
            this.val$show = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            AnimatorSet animatorSet = UsersAlertBase.this.shadowAnimation;
            if (animatorSet == null || !animatorSet.equals(animator)) {
                return;
            }
            if (!this.val$show) {
                UsersAlertBase.this.shadow.setVisibility(4);
            }
            UsersAlertBase.this.shadowAnimation = null;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            AnimatorSet animatorSet = UsersAlertBase.this.shadowAnimation;
            if (animatorSet == null || !animatorSet.equals(animator)) {
                return;
            }
            UsersAlertBase.this.shadowAnimation = null;
        }
    }

    public void showItemsAnimated(int i) {
        if (!isShowing()) {
            return;
        }
        this.listView.getViewTreeObserver().addOnPreDrawListener(new AnonymousClass5(i));
    }

    /* renamed from: org.telegram.ui.Components.UsersAlertBase$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ int val$from;

        AnonymousClass5(int i) {
            UsersAlertBase.this = r1;
            this.val$from = i;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            UsersAlertBase.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
            int childCount = UsersAlertBase.this.listView.getChildCount();
            AnimatorSet animatorSet = new AnimatorSet();
            for (int i = 0; i < childCount; i++) {
                View childAt = UsersAlertBase.this.listView.getChildAt(i);
                int childAdapterPosition = UsersAlertBase.this.listView.getChildAdapterPosition(childAt);
                if (childAdapterPosition >= this.val$from) {
                    if (childAdapterPosition == 1 && UsersAlertBase.this.listView.getAdapter() == UsersAlertBase.this.searchListViewAdapter && (childAt instanceof GraySectionCell)) {
                        childAt = ((GraySectionCell) childAt).getTextView();
                    }
                    childAt.setAlpha(0.0f);
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(childAt, View.ALPHA, 0.0f, 1.0f);
                    ofFloat.setStartDelay((int) ((Math.min(UsersAlertBase.this.listView.getMeasuredHeight(), Math.max(0, childAt.getTop())) / UsersAlertBase.this.listView.getMeasuredHeight()) * 100.0f));
                    ofFloat.setDuration(200L);
                    animatorSet.playTogether(ofFloat);
                }
            }
            animatorSet.start();
            return true;
        }
    }

    /* loaded from: classes3.dex */
    public class ContainerView extends FrameLayout {
        private boolean ignoreLayout = false;
        float snapToTopOffset;
        ValueAnimator valueAnimator;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ContainerView(Context context) {
            super(context);
            UsersAlertBase.this = r1;
        }

        @Override // android.view.View
        public void setTranslationY(float f) {
            super.setTranslationY(f);
            invalidate();
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int i3;
            int size = View.MeasureSpec.getSize(i2);
            if (Build.VERSION.SDK_INT >= 21) {
                this.ignoreLayout = true;
                setPadding(((BottomSheet) UsersAlertBase.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ((BottomSheet) UsersAlertBase.this).backgroundPaddingLeft, 0);
                this.ignoreLayout = false;
            }
            int paddingTop = size - getPaddingTop();
            if (((BottomSheet) UsersAlertBase.this).keyboardVisible) {
                i3 = AndroidUtilities.dp(8.0f);
                UsersAlertBase.this.setAllowNestedScroll(false);
                int i4 = UsersAlertBase.this.scrollOffsetY;
                if (i4 != 0) {
                    float f = i4;
                    this.snapToTopOffset = f;
                    setTranslationY(f);
                    ValueAnimator valueAnimator = this.valueAnimator;
                    if (valueAnimator != null) {
                        valueAnimator.removeAllListeners();
                        this.valueAnimator.cancel();
                    }
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(this.snapToTopOffset, 0.0f);
                    this.valueAnimator = ofFloat;
                    ofFloat.addUpdateListener(new UsersAlertBase$ContainerView$$ExternalSyntheticLambda0(this));
                    this.valueAnimator.setDuration(250L);
                    this.valueAnimator.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                    this.valueAnimator.addListener(new AnonymousClass1());
                    this.valueAnimator.start();
                } else if (this.valueAnimator != null) {
                    setTranslationY(this.snapToTopOffset);
                }
            } else {
                i3 = (paddingTop - ((paddingTop / 5) * 3)) + AndroidUtilities.dp(8.0f);
                UsersAlertBase.this.setAllowNestedScroll(true);
            }
            if (UsersAlertBase.this.listView.getPaddingTop() != i3) {
                this.ignoreLayout = true;
                UsersAlertBase.this.listView.setPadding(0, i3, 0, 0);
                this.ignoreLayout = false;
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
        }

        public /* synthetic */ void lambda$onMeasure$0(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.snapToTopOffset = floatValue;
            setTranslationY(floatValue);
        }

        /* renamed from: org.telegram.ui.Components.UsersAlertBase$ContainerView$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends AnimatorListenerAdapter {
            AnonymousClass1() {
                ContainerView.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                ContainerView containerView = ContainerView.this;
                containerView.snapToTopOffset = 0.0f;
                containerView.setTranslationY(0.0f);
                ContainerView.this.valueAnimator = null;
            }
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            UsersAlertBase.this.updateLayout();
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                float y = motionEvent.getY();
                UsersAlertBase usersAlertBase = UsersAlertBase.this;
                if (y < usersAlertBase.scrollOffsetY) {
                    usersAlertBase.dismiss();
                    return true;
                }
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return !UsersAlertBase.this.isDismissed() && super.onTouchEvent(motionEvent);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x00c3  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x016a  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            int i;
            float f;
            canvas.save();
            UsersAlertBase usersAlertBase = UsersAlertBase.this;
            int dp = (usersAlertBase.scrollOffsetY - ((BottomSheet) usersAlertBase).backgroundPaddingTop) + AndroidUtilities.dp(6.0f);
            UsersAlertBase usersAlertBase2 = UsersAlertBase.this;
            int dp2 = (usersAlertBase2.scrollOffsetY - ((BottomSheet) usersAlertBase2).backgroundPaddingTop) - AndroidUtilities.dp(13.0f);
            int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(50.0f) + ((BottomSheet) UsersAlertBase.this).backgroundPaddingTop;
            if (Build.VERSION.SDK_INT >= 21) {
                int i2 = AndroidUtilities.statusBarHeight;
                dp2 += i2;
                dp += i2;
                measuredHeight -= i2;
                float translationY = ((BottomSheet) UsersAlertBase.this).backgroundPaddingTop + dp2 + getTranslationY();
                int i3 = AndroidUtilities.statusBarHeight;
                if (translationY < i3 * 2) {
                    int min = (int) Math.min(i3, (((i3 * 2) - dp2) - ((BottomSheet) UsersAlertBase.this).backgroundPaddingTop) - getTranslationY());
                    dp2 -= min;
                    measuredHeight += min;
                    f = 1.0f - Math.min(1.0f, (min * 2) / AndroidUtilities.statusBarHeight);
                } else {
                    f = 1.0f;
                }
                float translationY2 = ((BottomSheet) UsersAlertBase.this).backgroundPaddingTop + dp2 + getTranslationY();
                int i4 = AndroidUtilities.statusBarHeight;
                if (translationY2 < i4) {
                    i = (int) Math.min(i4, ((i4 - dp2) - ((BottomSheet) UsersAlertBase.this).backgroundPaddingTop) - getTranslationY());
                    UsersAlertBase.this.shadowDrawable.setBounds(0, dp2, getMeasuredWidth(), measuredHeight);
                    UsersAlertBase.this.shadowDrawable.draw(canvas);
                    if (f != 1.0f) {
                        Theme.dialogs_onlineCirclePaint.setColor(UsersAlertBase.this.backgroundColor);
                        UsersAlertBase.this.rect.set(((BottomSheet) UsersAlertBase.this).backgroundPaddingLeft, ((BottomSheet) UsersAlertBase.this).backgroundPaddingTop + dp2, getMeasuredWidth() - ((BottomSheet) UsersAlertBase.this).backgroundPaddingLeft, ((BottomSheet) UsersAlertBase.this).backgroundPaddingTop + dp2 + AndroidUtilities.dp(24.0f));
                        canvas.drawRoundRect(UsersAlertBase.this.rect, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
                    }
                    int dp3 = AndroidUtilities.dp(36.0f);
                    UsersAlertBase.this.rect.set((getMeasuredWidth() - dp3) / 2, dp, (getMeasuredWidth() + dp3) / 2, dp + AndroidUtilities.dp(4.0f));
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(UsersAlertBase.this.keyScrollUp));
                    canvas.drawRoundRect(UsersAlertBase.this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                    if (i > 0) {
                        Theme.dialogs_onlineCirclePaint.setColor(Color.argb(255, (int) (Color.red(UsersAlertBase.this.backgroundColor) * 0.8f), (int) (Color.green(UsersAlertBase.this.backgroundColor) * 0.8f), (int) (Color.blue(UsersAlertBase.this.backgroundColor) * 0.8f)));
                        canvas.drawRect(((BottomSheet) UsersAlertBase.this).backgroundPaddingLeft, (AndroidUtilities.statusBarHeight - i) - getTranslationY(), getMeasuredWidth() - ((BottomSheet) UsersAlertBase.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight - getTranslationY(), Theme.dialogs_onlineCirclePaint);
                    }
                    canvas.restore();
                }
            } else {
                f = 1.0f;
            }
            i = 0;
            UsersAlertBase.this.shadowDrawable.setBounds(0, dp2, getMeasuredWidth(), measuredHeight);
            UsersAlertBase.this.shadowDrawable.draw(canvas);
            if (f != 1.0f) {
            }
            int dp32 = AndroidUtilities.dp(36.0f);
            UsersAlertBase.this.rect.set((getMeasuredWidth() - dp32) / 2, dp, (getMeasuredWidth() + dp32) / 2, dp + AndroidUtilities.dp(4.0f));
            Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(UsersAlertBase.this.keyScrollUp));
            canvas.drawRoundRect(UsersAlertBase.this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
            if (i > 0) {
            }
            canvas.restore();
        }

        @Override // android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            canvas.save();
            canvas.clipRect(0, getPaddingTop(), getMeasuredWidth(), getMeasuredHeight());
            super.dispatchDraw(canvas);
            canvas.restore();
        }
    }
}
