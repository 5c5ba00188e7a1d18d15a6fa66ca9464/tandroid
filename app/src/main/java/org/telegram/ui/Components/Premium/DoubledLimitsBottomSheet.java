package org.telegram.ui.Components.Premium;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.FixedHeightEmptyCell;
import org.telegram.ui.Components.BottomSheetWithRecyclerListView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.PremiumPreviewFragment;
/* loaded from: classes3.dex */
public class DoubledLimitsBottomSheet extends BottomSheetWithRecyclerListView implements NotificationCenter.NotificationCenterDelegate {
    private View divider;
    PremiumGradient.GradientTools gradientTools;
    int lastViewRow;
    final ArrayList<Limit> limits;
    int limitsStartRow;
    PremiumButtonView premiumButtonView;
    int rowCount;
    ImageView titleImage;
    float titleProgress;
    TextView titleView;
    private int totalGradientHeight;
    int headerRow = 0;
    FrameLayout titleLayout = new FrameLayout(getContext());

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    protected CharSequence getTitle() {
        return null;
    }

    public void setParentFragment(PremiumPreviewFragment premiumPreviewFragment) {
    }

    public DoubledLimitsBottomSheet(BaseFragment baseFragment, int i) {
        super(baseFragment, false, false);
        ArrayList<Limit> arrayList = new ArrayList<>();
        this.limits = arrayList;
        PremiumGradient.GradientTools gradientTools = new PremiumGradient.GradientTools("premiumGradient1", "premiumGradient2", "premiumGradient3", "premiumGradient4");
        this.gradientTools = gradientTools;
        gradientTools.x1 = 0.0f;
        gradientTools.y1 = 0.0f;
        gradientTools.x2 = 0.0f;
        gradientTools.y2 = 1.0f;
        this.clipToActionBar = true;
        MessagesController messagesController = MessagesController.getInstance(i);
        arrayList.add(new Limit(LocaleController.getString("GroupsAndChannelsLimitTitle", 2131626163), LocaleController.formatString("GroupsAndChannelsLimitSubtitle", 2131626162, Integer.valueOf(messagesController.channelsLimitPremium)), messagesController.channelsLimitDefault, messagesController.channelsLimitPremium, null));
        arrayList.add(new Limit(LocaleController.getString("PinChatsLimitTitle", 2131627579), LocaleController.formatString("PinChatsLimitSubtitle", 2131627578, Integer.valueOf(messagesController.dialogFiltersPinnedLimitPremium)), messagesController.dialogFiltersPinnedLimitDefault, messagesController.dialogFiltersPinnedLimitPremium, null));
        arrayList.add(new Limit(LocaleController.getString("PublicLinksLimitTitle", 2131627829), LocaleController.formatString("PublicLinksLimitSubtitle", 2131627828, Integer.valueOf(messagesController.publicLinksLimitPremium)), messagesController.publicLinksLimitDefault, messagesController.publicLinksLimitPremium, null));
        arrayList.add(new Limit(LocaleController.getString("SavedGifsLimitTitle", 2131628139), LocaleController.formatString("SavedGifsLimitSubtitle", 2131628138, Integer.valueOf(messagesController.savedGifsLimitPremium)), messagesController.savedGifsLimitDefault, messagesController.savedGifsLimitPremium, null));
        arrayList.add(new Limit(LocaleController.getString("FavoriteStickersLimitTitle", 2131625838), LocaleController.formatString("FavoriteStickersLimitSubtitle", 2131625837, Integer.valueOf(messagesController.stickersFavedLimitPremium)), messagesController.stickersFavedLimitDefault, messagesController.stickersFavedLimitPremium, null));
        arrayList.add(new Limit(LocaleController.getString("BioLimitTitle", 2131624685), LocaleController.formatString("BioLimitSubtitle", 2131624684, Integer.valueOf(messagesController.stickersFavedLimitPremium)), messagesController.aboutLengthLimitDefault, messagesController.aboutLengthLimitPremium, null));
        arrayList.add(new Limit(LocaleController.getString("CaptionsLimitTitle", 2131624858), LocaleController.formatString("CaptionsLimitSubtitle", 2131624857, Integer.valueOf(messagesController.stickersFavedLimitPremium)), messagesController.captionLengthLimitDefault, messagesController.captionLengthLimitPremium, null));
        arrayList.add(new Limit(LocaleController.getString("FoldersLimitTitle", 2131625953), LocaleController.formatString("FoldersLimitSubtitle", 2131625952, Integer.valueOf(messagesController.dialogFiltersLimitPremium)), messagesController.dialogFiltersLimitDefault, messagesController.dialogFiltersLimitPremium, null));
        arrayList.add(new Limit(LocaleController.getString("ChatPerFolderLimitTitle", 2131625038), LocaleController.formatString("ChatPerFolderLimitSubtitle", 2131625037, Integer.valueOf(messagesController.dialogFiltersChatsLimitPremium)), messagesController.dialogFiltersChatsLimitDefault, messagesController.dialogFiltersChatsLimitPremium, null));
        arrayList.add(new Limit(LocaleController.getString("ConnectedAccountsLimitTitle", 2131625240), LocaleController.formatString("ConnectedAccountsLimitSubtitle", 2131625239, 4), 3, 4, null));
        this.rowCount = 0;
        int i2 = 0 + 1;
        this.rowCount = i2;
        this.limitsStartRow = i2;
        this.rowCount = i2 + arrayList.size();
        TextView textView = new TextView(getContext());
        this.titleView = textView;
        textView.setText(LocaleController.getString("DoubledLimits", 2131625547));
        this.titleView.setGravity(17);
        this.titleView.setTextSize(1, 20.0f);
        this.titleView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.titleView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleLayout.addView(this.titleView, LayoutHelper.createFrame(-2, -2, 16));
        ImageView imageView = new ImageView(getContext());
        this.titleImage = imageView;
        imageView.setImageDrawable(PremiumGradient.getInstance().createGradientDrawable(ContextCompat.getDrawable(getContext(), 2131166028)));
        this.titleLayout.addView(this.titleImage, LayoutHelper.createFrame(40, 28, 16));
        this.containerView.addView(this.titleLayout, LayoutHelper.createFrame(-1, 40.0f));
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, getContext());
        this.divider = anonymousClass1;
        anonymousClass1.setBackgroundColor(Theme.getColor("dialogBackground"));
        this.containerView.addView(this.divider, LayoutHelper.createFrame(-1, 72.0f, 80, 0.0f, 0.0f, 0.0f, 0.0f));
        PremiumButtonView premiumButtonView = new PremiumButtonView(getContext(), true);
        this.premiumButtonView = premiumButtonView;
        premiumButtonView.buttonTextView.setText(PremiumPreviewFragment.getPremiumButtonText(i));
        this.containerView.addView(this.premiumButtonView, LayoutHelper.createFrame(-1, 48.0f, 80, 16.0f, 0.0f, 16.0f, 12.0f));
        this.premiumButtonView.buttonLayout.setOnClickListener(new DoubledLimitsBottomSheet$$ExternalSyntheticLambda1(this, i, baseFragment));
        this.premiumButtonView.overlayTextView.setOnClickListener(new DoubledLimitsBottomSheet$$ExternalSyntheticLambda0(this));
        this.recyclerListView.setPadding(0, 0, 0, AndroidUtilities.dp(72.0f));
        bindPremium(UserConfig.getInstance(getCurrentAccount()).isPremium());
    }

    /* renamed from: org.telegram.ui.Components.Premium.DoubledLimitsBottomSheet$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends View {
        AnonymousClass1(DoubledLimitsBottomSheet doubledLimitsBottomSheet, Context context) {
            super(context);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), 1.0f, Theme.dividerPaint);
        }
    }

    public /* synthetic */ void lambda$new$0(int i, BaseFragment baseFragment, View view) {
        if (!UserConfig.getInstance(i).isPremium()) {
            PremiumPreviewFragment.buyPremium(baseFragment, "double_limits");
        }
        dismiss();
    }

    public /* synthetic */ void lambda$new$1(View view) {
        dismiss();
    }

    private void bindPremium(boolean z) {
        if (z) {
            this.premiumButtonView.setOverlayText(LocaleController.getString("OK", 2131627127), false, false);
        }
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    public void onPreMeasure(int i, int i2) {
        super.onPreMeasure(i, i2);
        measureGradient(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    protected void onPreDraw(Canvas canvas, int i, float f) {
        float measuredHeight = AndroidUtilities.statusBarHeight + (((this.actionBar.getMeasuredHeight() - AndroidUtilities.statusBarHeight) - AndroidUtilities.dp(40.0f)) / 2.0f);
        float measuredWidth = (((this.titleLayout.getMeasuredWidth() - this.titleView.getMeasuredWidth()) - this.titleImage.getMeasuredWidth()) - AndroidUtilities.dp(6.0f)) / 2.0f;
        float dp = (AndroidUtilities.dp(72.0f) - this.titleImage.getMeasuredWidth()) - AndroidUtilities.dp(6.0f);
        float measuredWidth2 = this.titleImage.getMeasuredWidth() + measuredWidth + AndroidUtilities.dp(6.0f);
        float dp2 = AndroidUtilities.dp(72.0f);
        float max = Math.max(i + AndroidUtilities.dp(24.0f), measuredHeight);
        if (f > 0.0f) {
            float f2 = this.titleProgress;
            if (f2 != 1.0f) {
                float f3 = f2 + 0.10666667f;
                this.titleProgress = f3;
                if (f3 > 1.0f) {
                    this.titleProgress = 1.0f;
                }
                this.containerView.invalidate();
                FrameLayout frameLayout = this.titleLayout;
                float f4 = this.titleProgress;
                frameLayout.setTranslationY((max * (1.0f - f4)) + (measuredHeight * f4));
                TextView textView = this.titleView;
                float f5 = this.titleProgress;
                textView.setTranslationX((measuredWidth2 * (1.0f - f5)) + (dp2 * f5));
                ImageView imageView = this.titleImage;
                float f6 = this.titleProgress;
                imageView.setTranslationX((measuredWidth * (1.0f - f6)) + (dp * f6));
                this.titleImage.setAlpha(1.0f - this.titleProgress);
                float f7 = ((1.0f - this.titleProgress) * 0.4f) + 0.6f;
                this.titleImage.setScaleX(f7);
                this.titleImage.setScaleY(f7);
            }
        }
        if (f == 0.0f) {
            float f8 = this.titleProgress;
            if (f8 != 0.0f) {
                float f9 = f8 - 0.10666667f;
                this.titleProgress = f9;
                if (f9 < 0.0f) {
                    this.titleProgress = 0.0f;
                }
                this.containerView.invalidate();
            }
        }
        FrameLayout frameLayout2 = this.titleLayout;
        float f42 = this.titleProgress;
        frameLayout2.setTranslationY((max * (1.0f - f42)) + (measuredHeight * f42));
        TextView textView2 = this.titleView;
        float f52 = this.titleProgress;
        textView2.setTranslationX((measuredWidth2 * (1.0f - f52)) + (dp2 * f52));
        ImageView imageView2 = this.titleImage;
        float f62 = this.titleProgress;
        imageView2.setTranslationX((measuredWidth * (1.0f - f62)) + (dp * f62));
        this.titleImage.setAlpha(1.0f - this.titleProgress);
        float f72 = ((1.0f - this.titleProgress) * 0.4f) + 0.6f;
        this.titleImage.setScaleX(f72);
        this.titleImage.setScaleY(f72);
    }

    /* renamed from: org.telegram.ui.Components.Premium.DoubledLimitsBottomSheet$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends RecyclerListView.SelectionAdapter {
        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        AnonymousClass2() {
            DoubledLimitsBottomSheet.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LimitCell limitCell;
            Context context = viewGroup.getContext();
            if (i == 1) {
                limitCell = new FixedHeightEmptyCell(context, 64);
            } else if (i != 2) {
                LimitCell limitCell2 = new LimitCell(DoubledLimitsBottomSheet.this, context);
                limitCell2.previewView.setParentViewForGradien(((BottomSheet) DoubledLimitsBottomSheet.this).containerView);
                limitCell2.previewView.setStaticGradinet(DoubledLimitsBottomSheet.this.gradientTools);
                limitCell = limitCell2;
            } else {
                limitCell = new FixedHeightEmptyCell(context, 16);
            }
            limitCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(limitCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0) {
                LimitCell limitCell = (LimitCell) viewHolder.itemView;
                DoubledLimitsBottomSheet doubledLimitsBottomSheet = DoubledLimitsBottomSheet.this;
                limitCell.setData(doubledLimitsBottomSheet.limits.get(i - doubledLimitsBottomSheet.limitsStartRow));
                LimitPreviewView limitPreviewView = limitCell.previewView;
                DoubledLimitsBottomSheet doubledLimitsBottomSheet2 = DoubledLimitsBottomSheet.this;
                limitPreviewView.gradientYOffset = doubledLimitsBottomSheet2.limits.get(i - doubledLimitsBottomSheet2.limitsStartRow).yOffset;
                limitCell.previewView.gradientTotalHeight = DoubledLimitsBottomSheet.this.totalGradientHeight;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return DoubledLimitsBottomSheet.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            DoubledLimitsBottomSheet doubledLimitsBottomSheet = DoubledLimitsBottomSheet.this;
            if (i == doubledLimitsBottomSheet.headerRow) {
                return 1;
            }
            return i == doubledLimitsBottomSheet.lastViewRow ? 2 : 0;
        }
    }

    @Override // org.telegram.ui.Components.BottomSheetWithRecyclerListView
    protected RecyclerListView.SelectionAdapter createAdapter() {
        return new AnonymousClass2();
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
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.billingProductDetailsUpdated || i == NotificationCenter.premiumPromoUpdated) {
            this.premiumButtonView.buttonTextView.setText(PremiumPreviewFragment.getPremiumButtonText(this.currentAccount));
        } else if (i != NotificationCenter.currentUserPremiumStatusChanged) {
        } else {
            bindPremium(UserConfig.getInstance(this.currentAccount).isPremium());
        }
    }

    /* loaded from: classes3.dex */
    public class LimitCell extends LinearLayout {
        LimitPreviewView previewView;
        TextView subtitle;
        TextView title;

        public LimitCell(DoubledLimitsBottomSheet doubledLimitsBottomSheet, Context context) {
            super(context);
            setOrientation(1);
            setPadding(AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f), 0);
            TextView textView = new TextView(context);
            this.title = textView;
            textView.setTextSize(1, 15.0f);
            this.title.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.title.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            addView(this.title, LayoutHelper.createLinear(-1, -2, 0.0f, 0, 16, 0, 16, 0));
            TextView textView2 = new TextView(context);
            this.subtitle = textView2;
            textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
            this.subtitle.setTextSize(1, 14.0f);
            addView(this.subtitle, LayoutHelper.createLinear(-1, -2, 0.0f, 0, 16, 1, 16, 0));
            LimitPreviewView limitPreviewView = new LimitPreviewView(context, 0, 10, 20);
            this.previewView = limitPreviewView;
            addView(limitPreviewView, LayoutHelper.createLinear(-1, -2, 0.0f, 0, 0, 8, 0, 21));
        }

        @SuppressLint({"SetTextI18n"})
        public void setData(Limit limit) {
            this.title.setText(limit.title);
            this.subtitle.setText(limit.subtitle);
            this.previewView.premiumCount.setText(Integer.toString(limit.premiumLimit));
            this.previewView.defaultCount.setText(Integer.toString(limit.defaultLimit));
        }
    }

    private void measureGradient(int i, int i2) {
        LimitCell limitCell = new LimitCell(this, getContext());
        int i3 = 0;
        for (int i4 = 0; i4 < this.limits.size(); i4++) {
            limitCell.setData(this.limits.get(i4));
            limitCell.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
            this.limits.get(i4).yOffset = i3;
            i3 += limitCell.getMeasuredHeight();
        }
        this.totalGradientHeight = i3;
    }

    /* loaded from: classes3.dex */
    public static class Limit {
        final int defaultLimit;
        final int premiumLimit;
        final String subtitle;
        final String title;
        public int yOffset;

        /* synthetic */ Limit(String str, String str2, int i, int i2, AnonymousClass1 anonymousClass1) {
            this(str, str2, i, i2);
        }

        private Limit(String str, String str2, int i, int i2) {
            this.title = str;
            this.subtitle = str2;
            this.defaultLimit = i;
            this.premiumLimit = i2;
        }
    }
}
