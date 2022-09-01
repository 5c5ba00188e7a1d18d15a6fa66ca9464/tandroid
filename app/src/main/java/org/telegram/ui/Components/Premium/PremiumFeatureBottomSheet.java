package org.telegram.ui.Components.Premium;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.BottomPagesView;
import org.telegram.ui.Components.ChatAttachAlert;
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
    ArrayList<PremiumPreviewFragment.PremiumFeatureData> premiumFeatures;
    private PremiumPreviewFragment.SubscriptionTier selectedTier;
    private final int startType;
    SvgHelper.SvgDrawable svgIcon;
    ViewPager viewPager;

    public PremiumFeatureBottomSheet(BaseFragment baseFragment, int i, boolean z) {
        this(baseFragment, i, z, null);
    }

    public PremiumFeatureBottomSheet(BaseFragment baseFragment, int i, boolean z, PremiumPreviewFragment.SubscriptionTier subscriptionTier) {
        this(baseFragment, baseFragment.getContext(), baseFragment.getCurrentAccount(), i, z, subscriptionTier);
    }

    public PremiumFeatureBottomSheet(BaseFragment baseFragment, Context context, int i, int i2, boolean z) {
        this(baseFragment, context, i, i2, z, null);
    }

    public PremiumFeatureBottomSheet(final BaseFragment baseFragment, Context context, int i, int i2, final boolean z, PremiumPreviewFragment.SubscriptionTier subscriptionTier) {
        super(context, false);
        this.premiumFeatures = new ArrayList<>();
        this.selectedTier = subscriptionTier;
        fixNavigationBar();
        this.startType = i2;
        this.onlySelectedType = z;
        this.svgIcon = SvgHelper.getDrawable(RLottieDrawable.readRes(null, R.raw.star_loader));
        FrameLayout frameLayout = new FrameLayout(getContext()) { // from class: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet.1
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i3, int i4) {
                if (((BottomSheet) PremiumFeatureBottomSheet.this).isPortrait) {
                    PremiumFeatureBottomSheet.this.contentHeight = View.MeasureSpec.getSize(i3);
                } else {
                    PremiumFeatureBottomSheet.this.contentHeight = (int) (View.MeasureSpec.getSize(i4) * 0.65f);
                }
                super.onMeasure(i3, i4);
            }
        };
        PremiumPreviewFragment.fillPremiumFeaturesList(this.premiumFeatures, i);
        int i3 = 0;
        while (true) {
            if (i3 >= this.premiumFeatures.size()) {
                i3 = 0;
                break;
            }
            if (this.premiumFeatures.get(i3).type == 0) {
                this.premiumFeatures.remove(i3);
                i3--;
            } else if (this.premiumFeatures.get(i3).type == i2) {
                break;
            }
            i3++;
        }
        if (z) {
            this.premiumFeatures.clear();
            this.premiumFeatures.add(this.premiumFeatures.get(i3));
            i3 = 0;
        }
        final PremiumPreviewFragment.PremiumFeatureData premiumFeatureData = this.premiumFeatures.get(i3);
        setApplyBottomPadding(false);
        this.useBackgroundTopPadding = false;
        final PremiumGradient.GradientTools gradientTools = new PremiumGradient.GradientTools("premiumGradientBottomSheet1", "premiumGradientBottomSheet2", "premiumGradientBottomSheet3", null);
        gradientTools.x1 = 0.0f;
        gradientTools.y1 = 1.1f;
        gradientTools.x2 = 1.5f;
        gradientTools.y2 = -0.2f;
        gradientTools.exactly = true;
        this.content = new FrameLayout(getContext()) { // from class: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet.2
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i4, int i5) {
                super.onMeasure(i4, View.MeasureSpec.makeMeasureSpec(PremiumFeatureBottomSheet.this.contentHeight + AndroidUtilities.dp(2.0f), 1073741824));
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                gradientTools.gradientMatrix(0, 0, getMeasuredWidth(), getMeasuredHeight(), 0.0f, 0.0f);
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, AndroidUtilities.dp(2.0f), getMeasuredWidth(), getMeasuredHeight() + AndroidUtilities.dp(18.0f));
                canvas.save();
                canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(12.0f) - 1, AndroidUtilities.dp(12.0f) - 1, gradientTools.paint);
                canvas.restore();
                super.dispatchDraw(canvas);
            }
        };
        FrameLayout frameLayout2 = new FrameLayout(getContext());
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.msg_close);
        imageView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(12.0f), ColorUtils.setAlphaComponent(-1, 40), ColorUtils.setAlphaComponent(-1, 100)));
        frameLayout2.addView(imageView, LayoutHelper.createFrame(24, 24, 17));
        frameLayout2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PremiumFeatureBottomSheet.this.lambda$new$0(view);
            }
        });
        frameLayout.addView(this.content, LayoutHelper.createLinear(-1, -2, 1, 0, 16, 0, 0));
        ViewPager viewPager = new ViewPager(getContext()) { // from class: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.viewpager.widget.ViewPager, android.view.View
            public void onMeasure(int i4, int i5) {
                int dp = AndroidUtilities.dp(100.0f);
                if (getChildCount() > 0) {
                    getChildAt(0).measure(i4, View.MeasureSpec.makeMeasureSpec(0, 0));
                    dp = getChildAt(0).getMeasuredHeight();
                }
                super.onMeasure(i4, View.MeasureSpec.makeMeasureSpec(dp, 1073741824));
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
        };
        this.viewPager = viewPager;
        viewPager.setOffscreenPageLimit(0);
        this.viewPager.setAdapter(new PagerAdapter() { // from class: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet.4
            @Override // androidx.viewpager.widget.PagerAdapter
            public boolean isViewFromObject(View view, Object obj) {
                return view == obj;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getCount() {
                return PremiumFeatureBottomSheet.this.premiumFeatures.size();
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public Object instantiateItem(ViewGroup viewGroup, int i4) {
                PremiumFeatureBottomSheet premiumFeatureBottomSheet = PremiumFeatureBottomSheet.this;
                ViewPage viewPage = new ViewPage(premiumFeatureBottomSheet.getContext(), i4);
                viewGroup.addView(viewPage);
                viewPage.position = i4;
                viewPage.setFeatureDate(PremiumFeatureBottomSheet.this.premiumFeatures.get(i4));
                return viewPage;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public void destroyItem(ViewGroup viewGroup, int i4, Object obj) {
                viewGroup.removeView((View) obj);
            }
        });
        this.viewPager.setCurrentItem(i3);
        frameLayout.addView(this.viewPager, LayoutHelper.createFrame(-1, 100.0f, 0, 0.0f, 18.0f, 0.0f, 0.0f));
        frameLayout.addView(frameLayout2, LayoutHelper.createFrame(52, 52.0f, 53, 0.0f, 16.0f, 0.0f, 0.0f));
        final BottomPagesView bottomPagesView = new BottomPagesView(getContext(), this.viewPager, this.premiumFeatures.size());
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet.5
            float progress;
            int selectedPosition;
            int toPosition;

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i4) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i4, float f, int i5) {
                bottomPagesView.setPageOffset(i4, f);
                this.selectedPosition = i4;
                this.toPosition = i5 > 0 ? i4 + 1 : i4 - 1;
                this.progress = f;
                checkPage();
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i4) {
                checkPage();
            }

            private void checkPage() {
                float measuredWidth;
                boolean z2 = false;
                for (int i4 = 0; i4 < PremiumFeatureBottomSheet.this.viewPager.getChildCount(); i4++) {
                    ViewPage viewPage = (ViewPage) PremiumFeatureBottomSheet.this.viewPager.getChildAt(i4);
                    float f = 0.0f;
                    if (!PremiumFeatureBottomSheet.this.enterAnimationIsRunning || !(viewPage.topView instanceof PremiumAppIconsPreviewView)) {
                        int i5 = viewPage.position;
                        if (i5 == this.selectedPosition) {
                            PagerHeaderView pagerHeaderView = viewPage.topHeader;
                            measuredWidth = (-viewPage.getMeasuredWidth()) * this.progress;
                            pagerHeaderView.setOffset(measuredWidth);
                        } else if (i5 == this.toPosition) {
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
                    z2 = true;
                }
                premiumFeatureBottomSheet.containerViewsForward = z2;
            }
        });
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.addView(frameLayout);
        linearLayout.setOrientation(1);
        bottomPagesView.setColor("chats_unreadCounterMuted", "chats_actionBackground");
        if (!z) {
            linearLayout.addView(bottomPagesView, LayoutHelper.createLinear(this.premiumFeatures.size() * 11, 5, 1, 0, 0, 0, 10));
        }
        PremiumButtonView premiumButtonView = new PremiumButtonView(getContext(), true);
        this.premiumButtonView = premiumButtonView;
        premiumButtonView.buttonLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PremiumFeatureBottomSheet.this.lambda$new$1(baseFragment, z, premiumFeatureData, view);
            }
        });
        this.premiumButtonView.overlayTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PremiumFeatureBottomSheet.this.lambda$new$2(view);
            }
        });
        FrameLayout frameLayout3 = new FrameLayout(getContext());
        this.buttonContainer = frameLayout3;
        frameLayout3.addView(this.premiumButtonView, LayoutHelper.createFrame(-1, 48.0f, 16, 16.0f, 0.0f, 16.0f, 0.0f));
        this.buttonContainer.setBackgroundColor(getThemedColor("dialogBackground"));
        linearLayout.addView(this.buttonContainer, LayoutHelper.createLinear(-1, 68, 80));
        if (UserConfig.getInstance(i).isPremium()) {
            this.premiumButtonView.setOverlayText(LocaleController.getString("OK", R.string.OK), false, false);
        }
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(linearLayout);
        setCustomView(scrollView);
        MediaDataController.getInstance(i).preloadPremiumPreviewStickers();
        setButtonText();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(BaseFragment baseFragment, boolean z, PremiumPreviewFragment.PremiumFeatureData premiumFeatureData, View view) {
        if (baseFragment instanceof ChatActivity) {
            ChatActivity chatActivity = (ChatActivity) baseFragment;
            chatActivity.closeMenu();
            ChatAttachAlert chatAttachAlert = chatActivity.chatAttachAlert;
            if (chatAttachAlert != null) {
                chatAttachAlert.dismiss(true);
            }
        }
        if (baseFragment.getVisibleDialog() != null) {
            baseFragment.getVisibleDialog().dismiss();
        }
        if (z) {
            baseFragment.presentFragment(new PremiumPreviewFragment(PremiumPreviewFragment.featureTypeToServerString(premiumFeatureData.type)));
        } else {
            PremiumPreviewFragment.buyPremium(baseFragment, this.selectedTier, PremiumPreviewFragment.featureTypeToServerString(premiumFeatureData.type));
        }
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        dismiss();
    }

    private void setButtonText() {
        if (this.onlySelectedType) {
            int i = this.startType;
            if (i == 4) {
                this.premiumButtonView.buttonTextView.setText(LocaleController.getString(R.string.UnlockPremiumReactions));
                this.premiumButtonView.setIcon(R.raw.unlock_icon);
                return;
            } else if (i == 3) {
                this.premiumButtonView.buttonTextView.setText(LocaleController.getString(R.string.AboutTelegramPremium));
                return;
            } else if (i != 10) {
                return;
            } else {
                this.premiumButtonView.buttonTextView.setText(LocaleController.getString(R.string.UnlockPremiumIcons));
                this.premiumButtonView.setIcon(R.raw.unlock_icon);
                return;
            }
        }
        this.premiumButtonView.buttonTextView.setText(PremiumPreviewFragment.getPremiumButtonText(this.currentAccount, this.selectedTier));
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void show() {
        super.show();
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopAllHeavyOperations, 16);
    }

    /* JADX INFO: Access modifiers changed from: protected */
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
                this.premiumButtonView.setOverlayText(LocaleController.getString("OK", R.string.OK), false, true);
            } else {
                this.premiumButtonView.clearOverlayText();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ViewPage extends LinearLayout {
        TextView description;
        public int position;
        TextView title;
        PagerHeaderView topHeader;
        View topView;

        public ViewPage(Context context, int i) {
            super(context);
            setOrientation(1);
            View viewForPosition = PremiumFeatureBottomSheet.this.getViewForPosition(context, i);
            this.topView = viewForPosition;
            addView(viewForPosition);
            this.topHeader = (PagerHeaderView) this.topView;
            TextView textView = new TextView(context);
            this.title = textView;
            textView.setGravity(1);
            this.title.setTextColor(Theme.getColor("dialogTextBlack"));
            this.title.setTextSize(1, 20.0f);
            this.title.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            addView(this.title, LayoutHelper.createFrame(-1, -2.0f, 0, 21.0f, 20.0f, 21.0f, 0.0f));
            TextView textView2 = new TextView(context);
            this.description = textView2;
            textView2.setGravity(1);
            this.description.setTextSize(1, 15.0f);
            this.description.setTextColor(Theme.getColor("dialogTextBlack"));
            if (!PremiumFeatureBottomSheet.this.onlySelectedType) {
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
                        this.title.setText(LocaleController.getString("PremiumPreviewAppIcon", R.string.PremiumPreviewAppIcon));
                        this.description.setText(LocaleController.getString("PremiumPreviewAppIconDescription2", R.string.PremiumPreviewAppIconDescription2));
                        return;
                    }
                    this.title.setText(LocaleController.getString("PremiumPreviewNoAds", R.string.PremiumPreviewNoAds));
                    this.description.setText(LocaleController.getString("PremiumPreviewNoAdsDescription2", R.string.PremiumPreviewNoAdsDescription2));
                    return;
                }
                this.title.setText(LocaleController.getString("AdditionalReactions", R.string.AdditionalReactions));
                this.description.setText(LocaleController.getString("AdditionalReactionsDescription", R.string.AdditionalReactionsDescription));
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
                return new PremiumStickersPreviewRecycler(this, context, this.currentAccount) { // from class: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet.6
                    @Override // org.telegram.ui.Components.Premium.PremiumStickersPreviewRecycler, org.telegram.ui.Components.Premium.PagerHeaderView
                    public void setOffset(float f) {
                        setAutoPlayEnabled(f == 0.0f);
                        super.setOffset(f);
                    }
                };
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
        final HashMap hashMap = new HashMap();
        hashMap.put("👌", 1);
        hashMap.put("😍", 2);
        hashMap.put("🤡", 3);
        hashMap.put("🕊", 4);
        hashMap.put("\u1f971", 5);
        hashMap.put("\u1f974", 6);
        hashMap.put("🐳", 7);
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet$$ExternalSyntheticLambda3
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$getViewForPosition$3;
                lambda$getViewForPosition$3 = PremiumFeatureBottomSheet.lambda$getViewForPosition$3(hashMap, (ReactionDrawingObject) obj, (ReactionDrawingObject) obj2);
                return lambda$getViewForPosition$3;
            }
        });
        return new CarouselView(context, arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$getViewForPosition$3(HashMap hashMap, ReactionDrawingObject reactionDrawingObject, ReactionDrawingObject reactionDrawingObject2) {
        boolean containsKey = hashMap.containsKey(reactionDrawingObject.reaction.reaction);
        int i = ConnectionsManager.DEFAULT_DATACENTER_ID;
        int intValue = containsKey ? ((Integer) hashMap.get(reactionDrawingObject.reaction.reaction)).intValue() : ConnectionsManager.DEFAULT_DATACENTER_ID;
        if (hashMap.containsKey(reactionDrawingObject2.reaction.reaction)) {
            i = ((Integer) hashMap.get(reactionDrawingObject2.reaction.reaction)).intValue();
        }
        return i - intValue;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BottomSheet
    public boolean onCustomOpenAnimation() {
        if (this.viewPager.getChildCount() > 0) {
            ViewPage viewPage = (ViewPage) this.viewPager.getChildAt(0);
            View view = viewPage.topView;
            if (view instanceof PremiumAppIconsPreviewView) {
                final PremiumAppIconsPreviewView premiumAppIconsPreviewView = (PremiumAppIconsPreviewView) view;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(viewPage.getMeasuredWidth(), 0.0f);
                premiumAppIconsPreviewView.setOffset(viewPage.getMeasuredWidth());
                this.enterAnimationIsRunning = true;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet.7
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        premiumAppIconsPreviewView.setOffset(((Float) valueAnimator.getAnimatedValue()).floatValue());
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet.8
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        PremiumFeatureBottomSheet.this.enterAnimationIsRunning = false;
                        premiumAppIconsPreviewView.setOffset(0.0f);
                        super.onAnimationEnd(animator);
                    }
                });
                ofFloat.setDuration(500L);
                ofFloat.setStartDelay(100L);
                ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                ofFloat.start();
            }
        }
        return super.onCustomOpenAnimation();
    }
}
