package org.telegram.ui.Components.Premium;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.BottomPagesView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.PremiumPreviewFragment;
/* loaded from: classes3.dex */
public class PremiumFeatureBottomSheet extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
    private FrameLayout buttonContainer;
    boolean containerViewsForward;
    float containerViewsProgress;
    FrameLayout content;
    int contentHeight;
    boolean enterAnimationIsRunning;
    private final boolean onlySelectedType;
    private PremiumButtonView premiumButtonView;
    private final int startType;
    ViewPager viewPager;
    ArrayList<PremiumPreviewFragment.PremiumFeatureData> premiumFeatures = new ArrayList<>();
    SvgHelper.SvgDrawable svgIcon = SvgHelper.getDrawable(RLottieDrawable.readRes(null, 2131558556));

    public PremiumFeatureBottomSheet(BaseFragment baseFragment, int i, boolean z) {
        super(baseFragment.getParentActivity(), false);
        fixNavigationBar();
        this.startType = i;
        this.onlySelectedType = z;
        Activity parentActivity = baseFragment.getParentActivity();
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(parentActivity);
        PremiumPreviewFragment.fillPremiumFeaturesList(this.premiumFeatures, baseFragment.getCurrentAccount());
        int i2 = 0;
        while (true) {
            if (i2 >= this.premiumFeatures.size()) {
                i2 = 0;
                break;
            }
            if (this.premiumFeatures.get(i2).type == 0) {
                this.premiumFeatures.remove(i2);
                i2--;
            } else if (this.premiumFeatures.get(i2).type == i) {
                break;
            }
            i2++;
        }
        if (z) {
            this.premiumFeatures.clear();
            this.premiumFeatures.add(this.premiumFeatures.get(i2));
            i2 = 0;
        }
        PremiumPreviewFragment.PremiumFeatureData premiumFeatureData = this.premiumFeatures.get(i2);
        setApplyBottomPadding(false);
        this.useBackgroundTopPadding = false;
        PremiumGradient.GradientTools gradientTools = new PremiumGradient.GradientTools("premiumGradientBottomSheet1", "premiumGradientBottomSheet2", "premiumGradientBottomSheet3", null);
        gradientTools.x1 = 0.0f;
        gradientTools.y1 = 1.1f;
        gradientTools.x2 = 1.5f;
        gradientTools.y2 = -0.2f;
        gradientTools.exactly = true;
        this.content = new AnonymousClass2(parentActivity, gradientTools);
        FrameLayout frameLayout = new FrameLayout(parentActivity);
        ImageView imageView = new ImageView(parentActivity);
        imageView.setImageResource(2131165687);
        imageView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(12.0f), ColorUtils.setAlphaComponent(-1, 40), ColorUtils.setAlphaComponent(-1, 100)));
        frameLayout.addView(imageView, LayoutHelper.createFrame(24, 24, 17));
        frameLayout.setOnClickListener(new PremiumFeatureBottomSheet$$ExternalSyntheticLambda1(this));
        anonymousClass1.addView(this.content, LayoutHelper.createLinear(-1, -2, 1, 0, 16, 0, 0));
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(parentActivity);
        this.viewPager = anonymousClass3;
        anonymousClass3.setOffscreenPageLimit(0);
        this.viewPager.setAdapter(new AnonymousClass4(parentActivity));
        this.viewPager.setCurrentItem(i2);
        anonymousClass1.addView(this.viewPager, LayoutHelper.createFrame(-1, 100.0f, 0, 0.0f, 18.0f, 0.0f, 0.0f));
        anonymousClass1.addView(frameLayout, LayoutHelper.createFrame(52, 52.0f, 53, 0.0f, 16.0f, 0.0f, 0.0f));
        BottomPagesView bottomPagesView = new BottomPagesView(parentActivity, this.viewPager, this.premiumFeatures.size());
        this.viewPager.addOnPageChangeListener(new AnonymousClass5(bottomPagesView));
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.addView(anonymousClass1);
        linearLayout.setOrientation(1);
        bottomPagesView.setColor("chats_unreadCounterMuted", "chats_actionBackground");
        if (!z) {
            linearLayout.addView(bottomPagesView, LayoutHelper.createLinear(this.premiumFeatures.size() * 11, 5, 1, 0, 0, 0, 10));
        }
        PremiumButtonView premiumButtonView = new PremiumButtonView(parentActivity, true);
        this.premiumButtonView = premiumButtonView;
        premiumButtonView.buttonLayout.setOnClickListener(new PremiumFeatureBottomSheet$$ExternalSyntheticLambda2(this, baseFragment, z, premiumFeatureData));
        this.premiumButtonView.overlayTextView.setOnClickListener(new PremiumFeatureBottomSheet$$ExternalSyntheticLambda0(this));
        FrameLayout frameLayout2 = new FrameLayout(parentActivity);
        this.buttonContainer = frameLayout2;
        frameLayout2.addView(this.premiumButtonView, LayoutHelper.createFrame(-1, 48.0f, 16, 16.0f, 0.0f, 16.0f, 0.0f));
        this.buttonContainer.setBackgroundColor(getThemedColor("dialogBackground"));
        linearLayout.addView(this.buttonContainer, LayoutHelper.createLinear(-1, 68, 80));
        if (UserConfig.getInstance(this.currentAccount).isPremium()) {
            this.premiumButtonView.setOverlayText(LocaleController.getString("OK", 2131627127), false, false);
        }
        ScrollView scrollView = new ScrollView(parentActivity);
        scrollView.addView(linearLayout);
        setCustomView(scrollView);
        MediaDataController.getInstance(this.currentAccount).preloadPremiumPreviewStickers();
        setButtonText();
    }

    /* renamed from: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            PremiumFeatureBottomSheet.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            if (((BottomSheet) PremiumFeatureBottomSheet.this).isPortrait) {
                PremiumFeatureBottomSheet.this.contentHeight = View.MeasureSpec.getSize(i);
            } else {
                PremiumFeatureBottomSheet.this.contentHeight = (int) (View.MeasureSpec.getSize(i2) * 0.65f);
            }
            super.onMeasure(i, i2);
        }
    }

    /* renamed from: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends FrameLayout {
        final /* synthetic */ PremiumGradient.GradientTools val$gradientTools;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context, PremiumGradient.GradientTools gradientTools) {
            super(context);
            PremiumFeatureBottomSheet.this = r1;
            this.val$gradientTools = gradientTools;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(PremiumFeatureBottomSheet.this.contentHeight + AndroidUtilities.dp(2.0f), 1073741824));
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            this.val$gradientTools.gradientMatrix(0, 0, getMeasuredWidth(), getMeasuredHeight(), 0.0f, 0.0f);
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, AndroidUtilities.dp(2.0f), getMeasuredWidth(), getMeasuredHeight() + AndroidUtilities.dp(18.0f));
            canvas.save();
            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(12.0f) - 1, AndroidUtilities.dp(12.0f) - 1, this.val$gradientTools.paint);
            canvas.restore();
            super.dispatchDraw(canvas);
        }
    }

    public /* synthetic */ void lambda$new$0(View view) {
        dismiss();
    }

    /* renamed from: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends ViewPager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            PremiumFeatureBottomSheet.this = r1;
        }

        @Override // androidx.viewpager.widget.ViewPager, android.view.View
        public void onMeasure(int i, int i2) {
            int dp = AndroidUtilities.dp(100.0f);
            if (getChildCount() > 0) {
                getChildAt(0).measure(i, View.MeasureSpec.makeMeasureSpec(0, 0));
                dp = getChildAt(0).getMeasuredHeight();
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(dp, 1073741824));
        }

        @Override // androidx.viewpager.widget.ViewPager, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            try {
                return super.onInterceptTouchEvent(motionEvent);
            } catch (Exception unused) {
                return false;
            }
        }

        @Override // androidx.viewpager.widget.ViewPager, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (PremiumFeatureBottomSheet.this.enterAnimationIsRunning) {
                return false;
            }
            return super.onTouchEvent(motionEvent);
        }
    }

    /* renamed from: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends PagerAdapter {
        final /* synthetic */ Context val$context;

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        AnonymousClass4(Context context) {
            PremiumFeatureBottomSheet.this = r1;
            this.val$context = context;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return PremiumFeatureBottomSheet.this.premiumFeatures.size();
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            ViewPage viewPage = new ViewPage(this.val$context, i);
            viewGroup.addView(viewPage);
            viewPage.position = i;
            viewPage.setFeatureDate(PremiumFeatureBottomSheet.this.premiumFeatures.get(i));
            return viewPage;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }
    }

    /* renamed from: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 implements ViewPager.OnPageChangeListener {
        float progress;
        int selectedPosition;
        int toPosition;
        final /* synthetic */ BottomPagesView val$bottomPages;

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
        }

        AnonymousClass5(BottomPagesView bottomPagesView) {
            PremiumFeatureBottomSheet.this = r1;
            this.val$bottomPages = bottomPagesView;
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
            this.val$bottomPages.setPageOffset(i, f);
            this.selectedPosition = i;
            this.toPosition = i2 > 0 ? i + 1 : i - 1;
            this.progress = f;
            checkPage();
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
            checkPage();
        }

        private void checkPage() {
            float measuredWidth;
            boolean z = false;
            for (int i = 0; i < PremiumFeatureBottomSheet.this.viewPager.getChildCount(); i++) {
                ViewPage viewPage = (ViewPage) PremiumFeatureBottomSheet.this.viewPager.getChildAt(i);
                float f = 0.0f;
                if (!PremiumFeatureBottomSheet.this.enterAnimationIsRunning || !(viewPage.topView instanceof PremiumAppIconsPreviewView)) {
                    int i2 = viewPage.position;
                    if (i2 == this.selectedPosition) {
                        PagerHeaderView pagerHeaderView = viewPage.topHeader;
                        measuredWidth = (-viewPage.getMeasuredWidth()) * this.progress;
                        pagerHeaderView.setOffset(measuredWidth);
                    } else if (i2 == this.toPosition) {
                        PagerHeaderView pagerHeaderView2 = viewPage.topHeader;
                        measuredWidth = ((-viewPage.getMeasuredWidth()) * this.progress) + viewPage.getMeasuredWidth();
                        pagerHeaderView2.setOffset(measuredWidth);
                    } else {
                        viewPage.topHeader.setOffset(viewPage.getMeasuredWidth());
                    }
                    f = measuredWidth;
                }
                if (viewPage.topView instanceof PremiumAppIconsPreviewView) {
                    viewPage.setTranslationX(-f);
                    viewPage.title.setTranslationX(f);
                    viewPage.description.setTranslationX(f);
                }
            }
            PremiumFeatureBottomSheet premiumFeatureBottomSheet = PremiumFeatureBottomSheet.this;
            premiumFeatureBottomSheet.containerViewsProgress = this.progress;
            if (this.toPosition > this.selectedPosition) {
                z = true;
            }
            premiumFeatureBottomSheet.containerViewsForward = z;
        }
    }

    public /* synthetic */ void lambda$new$1(BaseFragment baseFragment, boolean z, PremiumPreviewFragment.PremiumFeatureData premiumFeatureData, View view) {
        if (baseFragment.getVisibleDialog() != null) {
            baseFragment.getVisibleDialog().dismiss();
        }
        if (baseFragment instanceof ChatActivity) {
            ((ChatActivity) baseFragment).closeMenu();
        }
        if (z) {
            baseFragment.presentFragment(new PremiumPreviewFragment(PremiumPreviewFragment.featureTypeToServerString(premiumFeatureData.type)));
        } else {
            PremiumPreviewFragment.buyPremium(baseFragment, PremiumPreviewFragment.featureTypeToServerString(premiumFeatureData.type));
        }
        dismiss();
    }

    public /* synthetic */ void lambda$new$2(View view) {
        dismiss();
    }

    private void setButtonText() {
        if (this.onlySelectedType) {
            int i = this.startType;
            if (i == 4) {
                this.premiumButtonView.buttonTextView.setText(LocaleController.getString(2131628806));
                this.premiumButtonView.setIcon(2131558588);
                return;
            } else if (i == 3) {
                this.premiumButtonView.buttonTextView.setText(LocaleController.getString(2131623944));
                return;
            } else if (i != 10) {
                return;
            } else {
                this.premiumButtonView.buttonTextView.setText(LocaleController.getString(2131628805));
                this.premiumButtonView.setIcon(2131558588);
                return;
            }
        }
        this.premiumButtonView.buttonTextView.setText(PremiumPreviewFragment.getPremiumButtonText(this.currentAccount));
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void show() {
        super.show();
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopAllHeavyOperations, 16);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.billingProductDetailsUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.premiumPromoUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.billingProductDetailsUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.premiumPromoUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.startAllHeavyOperations, 16);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.billingProductDetailsUpdated || i == NotificationCenter.premiumPromoUpdated) {
            setButtonText();
        } else if (i != NotificationCenter.currentUserPremiumStatusChanged) {
        } else {
            if (UserConfig.getInstance(this.currentAccount).isPremium()) {
                this.premiumButtonView.setOverlayText(LocaleController.getString("OK", 2131627127), false, true);
            } else {
                this.premiumButtonView.clearOverlayText();
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ViewPage extends LinearLayout {
        TextView description;
        public int position;
        TextView title;
        PagerHeaderView topHeader;
        View topView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ViewPage(Context context, int i) {
            super(context);
            PremiumFeatureBottomSheet.this = r10;
            setOrientation(1);
            View viewForPosition = r10.getViewForPosition(context, i);
            this.topView = viewForPosition;
            addView(viewForPosition);
            this.topHeader = (PagerHeaderView) this.topView;
            TextView textView = new TextView(context);
            this.title = textView;
            textView.setGravity(1);
            this.title.setTextColor(Theme.getColor("dialogTextBlack"));
            this.title.setTextSize(1, 20.0f);
            this.title.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            addView(this.title, LayoutHelper.createFrame(-1, -2.0f, 0, 21.0f, 20.0f, 21.0f, 0.0f));
            TextView textView2 = new TextView(context);
            this.description = textView2;
            textView2.setGravity(1);
            this.description.setTextSize(1, 15.0f);
            this.description.setTextColor(Theme.getColor("dialogTextBlack"));
            if (!r10.onlySelectedType) {
                this.description.setLines(2);
            }
            addView(this.description, LayoutHelper.createFrame(-1, -2.0f, 0, 21.0f, 10.0f, 21.0f, 16.0f));
            setClipChildren(false);
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            ViewGroup.LayoutParams layoutParams = this.topView.getLayoutParams();
            PremiumFeatureBottomSheet premiumFeatureBottomSheet = PremiumFeatureBottomSheet.this;
            layoutParams.height = premiumFeatureBottomSheet.contentHeight;
            this.description.setVisibility(((BottomSheet) premiumFeatureBottomSheet).isPortrait ? 0 : 8);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.title.getLayoutParams();
            if (((BottomSheet) PremiumFeatureBottomSheet.this).isPortrait) {
                marginLayoutParams.topMargin = AndroidUtilities.dp(20.0f);
                marginLayoutParams.bottomMargin = 0;
            } else {
                marginLayoutParams.topMargin = AndroidUtilities.dp(10.0f);
                marginLayoutParams.bottomMargin = AndroidUtilities.dp(10.0f);
            }
            super.onMeasure(i, i2);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (view == this.topView) {
                if (view instanceof CarouselView) {
                    return super.drawChild(canvas, view, j);
                }
                canvas.save();
                canvas.clipRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                boolean drawChild = super.drawChild(canvas, view, j);
                canvas.restore();
                return drawChild;
            }
            return super.drawChild(canvas, view, j);
        }

        void setFeatureDate(PremiumPreviewFragment.PremiumFeatureData premiumFeatureData) {
            if (PremiumFeatureBottomSheet.this.onlySelectedType) {
                if (PremiumFeatureBottomSheet.this.startType != 4) {
                    if (PremiumFeatureBottomSheet.this.startType != 3) {
                        if (PremiumFeatureBottomSheet.this.startType != 10) {
                            return;
                        }
                        this.title.setText(LocaleController.getString("PremiumPreviewAppIcon", 2131627651));
                        this.description.setText(LocaleController.getString("PremiumPreviewAppIconDescription2", 2131627654));
                        return;
                    }
                    this.title.setText(LocaleController.getString("PremiumPreviewNoAds", 2131627662));
                    this.description.setText(LocaleController.getString("PremiumPreviewNoAdsDescription2", 2131627664));
                    return;
                }
                this.title.setText(LocaleController.getString("AdditionalReactions", 2131624313));
                this.description.setText(LocaleController.getString("AdditionalReactionsDescription", 2131624314));
                return;
            }
            this.title.setText(premiumFeatureData.title);
            this.description.setText(premiumFeatureData.description);
        }
    }

    View getViewForPosition(Context context, int i) {
        PremiumPreviewFragment.PremiumFeatureData premiumFeatureData = this.premiumFeatures.get(i);
        int i2 = premiumFeatureData.type;
        if (i2 != 4) {
            if (i2 == 5) {
                return new AnonymousClass6(this, context, this.currentAccount);
            }
            if (i2 == 10) {
                return new PremiumAppIconsPreviewView(context);
            }
            return new VideoScreenPreview(context, this.svgIcon, this.currentAccount, premiumFeatureData.type);
        }
        ArrayList arrayList = new ArrayList();
        List<TLRPC$TL_availableReaction> enabledReactionsList = MediaDataController.getInstance(this.currentAccount).getEnabledReactionsList();
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < enabledReactionsList.size(); i3++) {
            if (enabledReactionsList.get(i3).premium) {
                arrayList2.add(enabledReactionsList.get(i3));
            }
        }
        for (int i4 = 0; i4 < arrayList2.size(); i4++) {
            ReactionDrawingObject reactionDrawingObject = new ReactionDrawingObject(i4);
            reactionDrawingObject.set((TLRPC$TL_availableReaction) arrayList2.get(i4));
            arrayList.add(reactionDrawingObject);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("👌", 1);
        hashMap.put("😍", 2);
        hashMap.put("🤡", 3);
        hashMap.put("🕊", 4);
        hashMap.put("\u1f971", 5);
        hashMap.put("\u1f974", 6);
        hashMap.put("🐳", 7);
        Collections.sort(arrayList, new PremiumFeatureBottomSheet$$ExternalSyntheticLambda3(hashMap));
        return new CarouselView(context, arrayList);
    }

    public static /* synthetic */ int lambda$getViewForPosition$3(HashMap hashMap, ReactionDrawingObject reactionDrawingObject, ReactionDrawingObject reactionDrawingObject2) {
        int i = Integer.MAX_VALUE;
        int intValue = hashMap.containsKey(reactionDrawingObject.reaction.reaction) ? ((Integer) hashMap.get(reactionDrawingObject.reaction.reaction)).intValue() : Integer.MAX_VALUE;
        if (hashMap.containsKey(reactionDrawingObject2.reaction.reaction)) {
            i = ((Integer) hashMap.get(reactionDrawingObject2.reaction.reaction)).intValue();
        }
        return i - intValue;
    }

    /* renamed from: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends PremiumStickersPreviewRecycler {
        AnonymousClass6(PremiumFeatureBottomSheet premiumFeatureBottomSheet, Context context, int i) {
            super(context, i);
        }

        @Override // org.telegram.ui.Components.Premium.PremiumStickersPreviewRecycler, org.telegram.ui.Components.Premium.PagerHeaderView
        public void setOffset(float f) {
            setAutoPlayEnabled(f == 0.0f);
            super.setOffset(f);
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public boolean onCustomOpenAnimation() {
        if (this.viewPager.getChildCount() > 0) {
            ViewPage viewPage = (ViewPage) this.viewPager.getChildAt(0);
            View view = viewPage.topView;
            if (view instanceof PremiumAppIconsPreviewView) {
                PremiumAppIconsPreviewView premiumAppIconsPreviewView = (PremiumAppIconsPreviewView) view;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(viewPage.getMeasuredWidth(), 0.0f);
                premiumAppIconsPreviewView.setOffset(viewPage.getMeasuredWidth());
                this.enterAnimationIsRunning = true;
                ofFloat.addUpdateListener(new AnonymousClass7(this, premiumAppIconsPreviewView));
                ofFloat.addListener(new AnonymousClass8(premiumAppIconsPreviewView));
                ofFloat.setDuration(500L);
                ofFloat.setStartDelay(100L);
                ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                ofFloat.start();
            }
        }
        return super.onCustomOpenAnimation();
    }

    /* renamed from: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet$7 */
    /* loaded from: classes3.dex */
    class AnonymousClass7 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ PremiumAppIconsPreviewView val$premiumAppIconsPreviewView;

        AnonymousClass7(PremiumFeatureBottomSheet premiumFeatureBottomSheet, PremiumAppIconsPreviewView premiumAppIconsPreviewView) {
            this.val$premiumAppIconsPreviewView = premiumAppIconsPreviewView;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.val$premiumAppIconsPreviewView.setOffset(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* renamed from: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet$8 */
    /* loaded from: classes3.dex */
    class AnonymousClass8 extends AnimatorListenerAdapter {
        final /* synthetic */ PremiumAppIconsPreviewView val$premiumAppIconsPreviewView;

        AnonymousClass8(PremiumAppIconsPreviewView premiumAppIconsPreviewView) {
            PremiumFeatureBottomSheet.this = r1;
            this.val$premiumAppIconsPreviewView = premiumAppIconsPreviewView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            PremiumFeatureBottomSheet.this.enterAnimationIsRunning = false;
            this.val$premiumAppIconsPreviewView.setOffset(0.0f);
            super.onAnimationEnd(animator);
        }
    }
}
