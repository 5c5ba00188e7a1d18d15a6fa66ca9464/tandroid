package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.util.Consumer;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC$RecentMeUrl;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BlurredRecyclerView;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.TextViewSwitcher;
/* loaded from: classes3.dex */
public class DialogsEmptyCell extends LinearLayout {
    private RLottieImageView imageView;
    private Runnable onUtyanAnimationEndListener;
    private Consumer<Float> onUtyanAnimationUpdateListener;
    private int prevIcon;
    private TextViewSwitcher subtitleView;
    private TextView titleView;
    private boolean utyanAnimationTriggered;
    private ValueAnimator utyanAnimator;
    private float utyanCollapseProgress;
    private int currentType = -1;
    private int currentAccount = UserConfig.selectedAccount;

    public static /* synthetic */ boolean lambda$new$0(View view, MotionEvent motionEvent) {
        return true;
    }

    public DialogsEmptyCell(Context context) {
        super(context);
        setGravity(17);
        setOrientation(1);
        setOnTouchListener(DialogsEmptyCell$$ExternalSyntheticLambda3.INSTANCE);
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.imageView = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        addView(this.imageView, LayoutHelper.createFrame(100, 100.0f, 17, 52.0f, 4.0f, 52.0f, 0.0f));
        this.imageView.setOnClickListener(new DialogsEmptyCell$$ExternalSyntheticLambda2(this));
        TextView textView = new TextView(context);
        this.titleView = textView;
        textView.setTextColor(Theme.getColor("chats_nameMessage_threeLines"));
        this.titleView.setTextSize(1, 20.0f);
        this.titleView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleView.setGravity(17);
        addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 51, 52.0f, 10.0f, 52.0f, 0.0f));
        TextViewSwitcher textViewSwitcher = new TextViewSwitcher(context);
        this.subtitleView = textViewSwitcher;
        textViewSwitcher.setFactory(new DialogsEmptyCell$$ExternalSyntheticLambda4(context));
        this.subtitleView.setInAnimation(context, 2130771968);
        this.subtitleView.setOutAnimation(context, 2130771969);
        addView(this.subtitleView, LayoutHelper.createFrame(-1, -2.0f, 51, 52.0f, 7.0f, 52.0f, 0.0f));
    }

    public /* synthetic */ void lambda$new$1(View view) {
        if (!this.imageView.isPlaying()) {
            this.imageView.setProgress(0.0f);
            this.imageView.playAnimation();
        }
    }

    public static /* synthetic */ View lambda$new$2(Context context) {
        TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor("chats_message"));
        textView.setTextSize(1, 14.0f);
        textView.setGravity(17);
        textView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        return textView;
    }

    public void setOnUtyanAnimationEndListener(Runnable runnable) {
        this.onUtyanAnimationEndListener = runnable;
    }

    public void setOnUtyanAnimationUpdateListener(Consumer<Float> consumer) {
        this.onUtyanAnimationUpdateListener = consumer;
    }

    public void setType(int i) {
        int i2;
        String str;
        if (this.currentType == i) {
            return;
        }
        this.currentType = i;
        if (i == 0 || i == 1) {
            i2 = 2131558592;
            str = LocaleController.getString("NoChatsHelp", 2131626865);
            this.titleView.setText(LocaleController.getString("NoChats", 2131626863));
        } else if (i == 2) {
            this.imageView.setAutoRepeat(false);
            i2 = 2131558447;
            str = LocaleController.getString("FilterNoChatsToDisplayInfo", 2131625914);
            this.titleView.setText(LocaleController.getString("FilterNoChatsToDisplay", 2131625913));
        } else {
            this.imageView.setAutoRepeat(true);
            i2 = 2131558446;
            str = LocaleController.getString("FilterAddingChatsInfo", 2131625869);
            this.titleView.setText(LocaleController.getString("FilterAddingChats", 2131625868));
        }
        if (i2 != 0) {
            this.imageView.setVisibility(0);
            if (this.currentType == 1) {
                if (isUtyanAnimationTriggered()) {
                    this.utyanCollapseProgress = 1.0f;
                    String string = LocaleController.getString("NoChatsContactsHelp", 2131626864);
                    if (AndroidUtilities.isTablet() && !AndroidUtilities.isSmallTablet()) {
                        string = string.replace('\n', ' ');
                    }
                    this.subtitleView.setText(string, true);
                    requestLayout();
                } else {
                    startUtyanCollapseAnimation(true);
                }
            }
            if (this.prevIcon != i2) {
                this.imageView.setAnimation(i2, 100, 100);
                this.imageView.playAnimation();
                this.prevIcon = i2;
            }
        } else {
            this.imageView.setVisibility(8);
        }
        if (AndroidUtilities.isTablet() && !AndroidUtilities.isSmallTablet()) {
            str = str.replace('\n', ' ');
        }
        this.subtitleView.setText(str, false);
    }

    public boolean isUtyanAnimationTriggered() {
        return this.utyanAnimationTriggered;
    }

    public void startUtyanExpandAnimation() {
        ValueAnimator valueAnimator = this.utyanAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.utyanAnimationTriggered = false;
        ValueAnimator duration = ValueAnimator.ofFloat(this.utyanCollapseProgress, 0.0f).setDuration(250L);
        this.utyanAnimator = duration;
        duration.setInterpolator(Easings.easeOutQuad);
        this.utyanAnimator.addUpdateListener(new DialogsEmptyCell$$ExternalSyntheticLambda0(this));
        this.utyanAnimator.addListener(new AnonymousClass1());
        this.utyanAnimator.start();
    }

    public /* synthetic */ void lambda$startUtyanExpandAnimation$3(ValueAnimator valueAnimator) {
        this.utyanCollapseProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        requestLayout();
        Consumer<Float> consumer = this.onUtyanAnimationUpdateListener;
        if (consumer != null) {
            consumer.accept(Float.valueOf(this.utyanCollapseProgress));
        }
    }

    /* renamed from: org.telegram.ui.Cells.DialogsEmptyCell$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        AnonymousClass1() {
            DialogsEmptyCell.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (DialogsEmptyCell.this.onUtyanAnimationEndListener != null) {
                DialogsEmptyCell.this.onUtyanAnimationEndListener.run();
            }
            if (animator == DialogsEmptyCell.this.utyanAnimator) {
                DialogsEmptyCell.this.utyanAnimator = null;
            }
        }
    }

    public void startUtyanCollapseAnimation(boolean z) {
        ValueAnimator valueAnimator = this.utyanAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.utyanAnimationTriggered = true;
        if (z) {
            String string = LocaleController.getString("NoChatsContactsHelp", 2131626864);
            if (AndroidUtilities.isTablet() && !AndroidUtilities.isSmallTablet()) {
                string = string.replace('\n', ' ');
            }
            this.subtitleView.setText(string, true);
        }
        ValueAnimator duration = ValueAnimator.ofFloat(this.utyanCollapseProgress, 1.0f).setDuration(250L);
        this.utyanAnimator = duration;
        duration.setInterpolator(Easings.easeOutQuad);
        this.utyanAnimator.addUpdateListener(new DialogsEmptyCell$$ExternalSyntheticLambda1(this));
        this.utyanAnimator.addListener(new AnonymousClass2());
        this.utyanAnimator.start();
    }

    public /* synthetic */ void lambda$startUtyanCollapseAnimation$4(ValueAnimator valueAnimator) {
        this.utyanCollapseProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        requestLayout();
        Consumer<Float> consumer = this.onUtyanAnimationUpdateListener;
        if (consumer != null) {
            consumer.accept(Float.valueOf(this.utyanCollapseProgress));
        }
    }

    /* renamed from: org.telegram.ui.Cells.DialogsEmptyCell$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends AnimatorListenerAdapter {
        AnonymousClass2() {
            DialogsEmptyCell.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (DialogsEmptyCell.this.onUtyanAnimationEndListener != null) {
                DialogsEmptyCell.this.onUtyanAnimationEndListener.run();
            }
            if (animator == DialogsEmptyCell.this.utyanAnimator) {
                DialogsEmptyCell.this.utyanAnimator = null;
            }
        }
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateLayout();
    }

    @Override // android.view.View
    public void offsetTopAndBottom(int i) {
        super.offsetTopAndBottom(i);
        updateLayout();
    }

    public void updateLayout() {
        int i;
        int i2 = 0;
        if ((getParent() instanceof View) && (((i = this.currentType) == 2 || i == 3) && ((View) getParent()).getPaddingTop() != 0)) {
            i2 = 0 - (getTop() / 2);
        }
        int i3 = this.currentType;
        if (i3 == 0 || i3 == 1) {
            i2 = (int) (i2 - (((int) (ActionBar.getCurrentActionBarHeight() / 2.0f)) * (1.0f - this.utyanCollapseProgress)));
        }
        float f = i2;
        this.imageView.setTranslationY(f);
        this.titleView.setTranslationY(f);
        this.subtitleView.setTranslationY(f);
    }

    private int measureUtyanHeight(int i) {
        int i2;
        if (getParent() instanceof View) {
            View view = (View) getParent();
            i2 = view.getMeasuredHeight();
            if (view.getPaddingTop() != 0 && Build.VERSION.SDK_INT >= 21) {
                i2 -= AndroidUtilities.statusBarHeight;
            }
        } else {
            i2 = View.MeasureSpec.getSize(i);
        }
        if (i2 == 0) {
            i2 = (AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) - (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
        }
        if (getParent() instanceof BlurredRecyclerView) {
            i2 -= ((BlurredRecyclerView) getParent()).blurTopPadding;
        }
        return (int) (i2 + ((AndroidUtilities.dp(320.0f) - i2) * this.utyanCollapseProgress));
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        int i4 = this.currentType;
        if (i4 == 0 || i4 == 1) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(measureUtyanHeight(i2), 1073741824));
        } else if (i4 == 2 || i4 == 3) {
            if (getParent() instanceof View) {
                View view = (View) getParent();
                i3 = view.getMeasuredHeight();
                if (view.getPaddingTop() != 0 && Build.VERSION.SDK_INT >= 21) {
                    i3 -= AndroidUtilities.statusBarHeight;
                }
            } else {
                i3 = View.MeasureSpec.getSize(i2);
            }
            if (i3 == 0) {
                i3 = (AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) - (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
            }
            if (getParent() instanceof BlurredRecyclerView) {
                i3 -= ((BlurredRecyclerView) getParent()).blurTopPadding;
            }
            ArrayList<TLRPC$RecentMeUrl> arrayList = MessagesController.getInstance(this.currentAccount).hintDialogs;
            if (!arrayList.isEmpty()) {
                i3 -= (((AndroidUtilities.dp(72.0f) * arrayList.size()) + arrayList.size()) - 1) + AndroidUtilities.dp(50.0f);
            }
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
        } else {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(166.0f), 1073741824));
        }
    }
}
