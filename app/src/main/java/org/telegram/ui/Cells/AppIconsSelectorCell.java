package org.telegram.ui.Cells;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.LauncherIconController;
/* loaded from: classes3.dex */
public class AppIconsSelectorCell extends RecyclerListView implements NotificationCenter.NotificationCenterDelegate {
    private List<LauncherIconController.LauncherIcon> availableIcons = new ArrayList();
    private int currentAccount;
    private LinearLayoutManager linearLayoutManager;

    public AppIconsSelectorCell(Context context, BaseFragment baseFragment, int i) {
        super(context);
        this.currentAccount = i;
        setPadding(0, AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f));
        setFocusable(false);
        setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        setItemAnimator(null);
        setLayoutAnimation(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 0, false);
        this.linearLayoutManager = linearLayoutManager;
        setLayoutManager(linearLayoutManager);
        setAdapter(new AnonymousClass1());
        addItemDecoration(new AnonymousClass2());
        setOnItemClickListener(new AppIconsSelectorCell$$ExternalSyntheticLambda0(this, baseFragment, context));
        updateIconsVisibility();
    }

    /* renamed from: org.telegram.ui.Cells.AppIconsSelectorCell$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends RecyclerView.Adapter {
        AnonymousClass1() {
            AppIconsSelectorCell.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RecyclerListView.Holder(new IconHolderView(viewGroup.getContext(), null));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            IconHolderView iconHolderView = (IconHolderView) viewHolder.itemView;
            LauncherIconController.LauncherIcon launcherIcon = (LauncherIconController.LauncherIcon) AppIconsSelectorCell.this.availableIcons.get(i);
            iconHolderView.bind(launcherIcon);
            iconHolderView.iconView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(18.0f), 0, Theme.getColor("listSelectorSDK21"), -16777216));
            iconHolderView.iconView.setForeground(launcherIcon.foreground);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return AppIconsSelectorCell.this.availableIcons.size();
        }
    }

    /* renamed from: org.telegram.ui.Cells.AppIconsSelectorCell$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends RecyclerView.ItemDecoration {
        AnonymousClass2() {
            AppIconsSelectorCell.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            int adapterPosition = recyclerView.getChildViewHolder(view).getAdapterPosition();
            if (adapterPosition == 0) {
                rect.left = AndroidUtilities.dp(18.0f);
            }
            if (adapterPosition == AppIconsSelectorCell.this.getAdapter().getItemCount() - 1) {
                rect.right = AndroidUtilities.dp(18.0f);
                return;
            }
            int itemCount = AppIconsSelectorCell.this.getAdapter().getItemCount();
            if (itemCount == 4) {
                rect.right = ((AppIconsSelectorCell.this.getWidth() - AndroidUtilities.dp(36.0f)) - (AndroidUtilities.dp(58.0f) * itemCount)) / (itemCount - 1);
            } else {
                rect.right = AndroidUtilities.dp(24.0f);
            }
        }
    }

    public /* synthetic */ void lambda$new$0(BaseFragment baseFragment, Context context, View view, int i) {
        IconHolderView iconHolderView = (IconHolderView) view;
        LauncherIconController.LauncherIcon launcherIcon = this.availableIcons.get(i);
        if (launcherIcon.premium && !UserConfig.hasPremiumOnAccounts()) {
            baseFragment.showDialog(new PremiumFeatureBottomSheet(baseFragment, 10, true));
        } else if (!LauncherIconController.isEnabled(launcherIcon)) {
            AnonymousClass3 anonymousClass3 = new AnonymousClass3(this, context);
            anonymousClass3.setTargetPosition(i);
            this.linearLayoutManager.startSmoothScroll(anonymousClass3);
            LauncherIconController.setIcon(launcherIcon);
            iconHolderView.setSelected(true, true);
            for (int i2 = 0; i2 < getChildCount(); i2++) {
                IconHolderView iconHolderView2 = (IconHolderView) getChildAt(i2);
                if (iconHolderView2 != iconHolderView) {
                    iconHolderView2.setSelected(false, true);
                }
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.showBulletin, 5, launcherIcon);
        }
    }

    /* renamed from: org.telegram.ui.Cells.AppIconsSelectorCell$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends LinearSmoothScroller {
        AnonymousClass3(AppIconsSelectorCell appIconsSelectorCell, Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller
        public int calculateDtToFit(int i, int i2, int i3, int i4, int i5) {
            return (i3 - i) + AndroidUtilities.dp(16.0f);
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller
        public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return super.calculateSpeedPerPixel(displayMetrics) * 3.0f;
        }
    }

    @SuppressLint({"NotifyDataSetChanged"})
    private void updateIconsVisibility() {
        this.availableIcons.clear();
        this.availableIcons.addAll(Arrays.asList(LauncherIconController.LauncherIcon.values()));
        if (MessagesController.getInstance(this.currentAccount).premiumLocked) {
            int i = 0;
            while (i < this.availableIcons.size()) {
                if (this.availableIcons.get(i).premium) {
                    this.availableIcons.remove(i);
                    i--;
                }
                i++;
            }
        }
        getAdapter().notifyDataSetChanged();
        invalidateItemDecorations();
        for (int i2 = 0; i2 < this.availableIcons.size(); i2++) {
            if (LauncherIconController.isEnabled(this.availableIcons.get(i2))) {
                this.linearLayoutManager.scrollToPositionWithOffset(i2, AndroidUtilities.dp(16.0f));
                return;
            }
        }
    }

    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        invalidateItemDecorations();
    }

    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), i2);
    }

    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.premiumStatusChangedGlobal);
    }

    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.premiumStatusChangedGlobal);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.premiumStatusChangedGlobal) {
            updateIconsVisibility();
        }
    }

    /* loaded from: classes3.dex */
    public static final class IconHolderView extends LinearLayout {
        private AdaptiveIconImageView iconView;
        private Paint outlinePaint;
        private float progress;
        private TextView titleView;

        /* synthetic */ IconHolderView(Context context, AnonymousClass1 anonymousClass1) {
            this(context);
        }

        private IconHolderView(Context context) {
            super(context);
            this.outlinePaint = new Paint(1);
            setOrientation(1);
            setWillNotDraw(false);
            AdaptiveIconImageView adaptiveIconImageView = new AdaptiveIconImageView(context);
            this.iconView = adaptiveIconImageView;
            adaptiveIconImageView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            addView(this.iconView, LayoutHelper.createLinear(58, 58, 1));
            TextView textView = new TextView(context);
            this.titleView = textView;
            textView.setSingleLine();
            this.titleView.setTextSize(1, 13.0f);
            this.titleView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            addView(this.titleView, LayoutHelper.createLinear(-2, -2, 1, 0, 4, 0, 0));
            this.outlinePaint.setStyle(Paint.Style.STROKE);
            this.outlinePaint.setStrokeWidth(Math.max(2, AndroidUtilities.dp(0.5f)));
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            super.draw(canvas);
            float strokeWidth = this.outlinePaint.getStrokeWidth();
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(this.iconView.getLeft() + strokeWidth, this.iconView.getTop() + strokeWidth, this.iconView.getRight() - strokeWidth, this.iconView.getBottom() - strokeWidth);
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f), this.outlinePaint);
        }

        private void setProgress(float f) {
            this.progress = f;
            this.titleView.setTextColor(ColorUtils.blendARGB(Theme.getColor("windowBackgroundWhiteBlackText"), Theme.getColor("windowBackgroundWhiteValueText"), f));
            this.outlinePaint.setColor(ColorUtils.blendARGB(ColorUtils.setAlphaComponent(Theme.getColor("switchTrack"), 63), Theme.getColor("windowBackgroundWhiteValueText"), f));
            this.outlinePaint.setStrokeWidth(Math.max(2, AndroidUtilities.dp(AndroidUtilities.lerp(0.5f, 2.0f, f))));
            invalidate();
        }

        public void setSelected(boolean z, boolean z2) {
            float f = z ? 1.0f : 0.0f;
            float f2 = this.progress;
            if (f != f2 || !z2) {
                if (z2) {
                    ValueAnimator duration = ValueAnimator.ofFloat(f2, f).setDuration(250L);
                    duration.setInterpolator(Easings.easeInOutQuad);
                    duration.addUpdateListener(new AppIconsSelectorCell$IconHolderView$$ExternalSyntheticLambda0(this));
                    duration.start();
                    return;
                }
                setProgress(f);
            }
        }

        public /* synthetic */ void lambda$setSelected$0(ValueAnimator valueAnimator) {
            setProgress(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        public void bind(LauncherIconController.LauncherIcon launcherIcon) {
            this.iconView.setImageResource(launcherIcon.background);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.titleView.getLayoutParams();
            if (launcherIcon.premium && !UserConfig.hasPremiumOnAccounts()) {
                SpannableString spannableString = new SpannableString("d " + LocaleController.getString(launcherIcon.title));
                ColoredImageSpan coloredImageSpan = new ColoredImageSpan(2131165805);
                coloredImageSpan.setTopOffset(1);
                coloredImageSpan.setSize(AndroidUtilities.dp(13.0f));
                spannableString.setSpan(coloredImageSpan, 0, 1, 33);
                marginLayoutParams.rightMargin = AndroidUtilities.dp(4.0f);
                this.titleView.setText(spannableString);
            } else {
                marginLayoutParams.rightMargin = 0;
                this.titleView.setText(LocaleController.getString(launcherIcon.title));
            }
            setSelected(LauncherIconController.isEnabled(launcherIcon), false);
        }
    }

    /* loaded from: classes3.dex */
    public static class AdaptiveIconImageView extends ImageView {
        private Drawable foreground;
        private Path path = new Path();
        private int outerPadding = AndroidUtilities.dp(5.0f);
        private int backgroundOuterPadding = AndroidUtilities.dp(42.0f);

        public AdaptiveIconImageView(Context context) {
            super(context);
        }

        public void setForeground(int i) {
            this.foreground = ContextCompat.getDrawable(getContext(), i);
            invalidate();
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            super.onSizeChanged(i, i2, i3, i4);
            updatePath();
        }

        public void setPadding(int i) {
            setPadding(i, i, i, i);
        }

        public void setOuterPadding(int i) {
            this.outerPadding = i;
        }

        public void setBackgroundOuterPadding(int i) {
            this.backgroundOuterPadding = i;
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.clipPath(this.path);
            canvas.scale((this.backgroundOuterPadding / getWidth()) + 1.0f, (this.backgroundOuterPadding / getHeight()) + 1.0f, getWidth() / 2.0f, getHeight() / 2.0f);
            super.draw(canvas);
            canvas.restore();
            Drawable drawable = this.foreground;
            if (drawable != null) {
                int i = this.outerPadding;
                drawable.setBounds(-i, -i, getWidth() + this.outerPadding, getHeight() + this.outerPadding);
                this.foreground.draw(canvas);
            }
        }

        private void updatePath() {
            this.path.rewind();
            this.path.addCircle(getWidth() / 2.0f, getHeight() / 2.0f, Math.min((getWidth() - getPaddingLeft()) - getPaddingRight(), (getHeight() - getPaddingTop()) - getPaddingBottom()) / 2.0f, Path.Direction.CW);
        }
    }
}
