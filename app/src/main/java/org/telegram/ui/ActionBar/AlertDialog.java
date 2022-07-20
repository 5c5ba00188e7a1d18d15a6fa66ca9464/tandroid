package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LineProgressView;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.spoilers.SpoilersTextView;
/* loaded from: classes3.dex */
public class AlertDialog extends Dialog implements Drawable.Callback {
    private float aspectRatio;
    private Rect backgroundPaddings;
    protected ViewGroup buttonsLayout;
    private boolean canCacnel;
    private AlertDialog cancelDialog;
    private boolean checkFocusable;
    private ScrollView contentScrollView;
    private int currentProgress;
    private View customView;
    private int customViewHeight;
    private int customViewOffset;
    private String dialogButtonColorKey;
    private float dimAlpha;
    private boolean dimEnabled;
    private boolean dismissDialogByButtons;
    private Runnable dismissRunnable;
    private boolean drawBackground;
    private boolean focusable;
    private int[] itemIcons;
    private ArrayList<AlertDialogCell> itemViews;
    private CharSequence[] items;
    private int lastScreenWidth;
    private LineProgressView lineProgressView;
    private TextView lineProgressViewPercent;
    private CharSequence message;
    private TextView messageTextView;
    private boolean messageTextViewClickable;
    private DialogInterface.OnClickListener negativeButtonListener;
    private CharSequence negativeButtonText;
    private DialogInterface.OnClickListener neutralButtonListener;
    private CharSequence neutralButtonText;
    private boolean notDrawBackgroundOnTopView;
    private DialogInterface.OnClickListener onBackButtonListener;
    private DialogInterface.OnCancelListener onCancelListener;
    private DialogInterface.OnClickListener onClickListener;
    private DialogInterface.OnDismissListener onDismissListener;
    private ViewTreeObserver.OnScrollChangedListener onScrollChangedListener;
    private DialogInterface.OnClickListener positiveButtonListener;
    private CharSequence positiveButtonText;
    private FrameLayout progressViewContainer;
    private int progressViewStyle;
    private final Theme.ResourcesProvider resourcesProvider;
    private LinearLayout scrollContainer;
    private CharSequence secondTitle;
    private TextView secondTitleTextView;
    private BitmapDrawable[] shadow;
    private AnimatorSet[] shadowAnimation;
    private Drawable shadowDrawable;
    private boolean[] shadowVisibility;
    private Runnable showRunnable;
    private CharSequence subtitle;
    private TextView subtitleTextView;
    private CharSequence title;
    private FrameLayout titleContainer;
    private TextView titleTextView;
    private boolean topAnimationAutoRepeat;
    private int topAnimationId;
    private int topAnimationSize;
    private int topBackgroundColor;
    private Drawable topDrawable;
    private int topHeight;
    private RLottieImageView topImageView;
    private int topResId;
    private View topView;
    private boolean verticalButtons;

    public ArrayList<ThemeDescription> getThemeDescriptions() {
        return null;
    }

    public /* synthetic */ void lambda$new$0() {
        if (isShowing()) {
            return;
        }
        try {
            show();
        } catch (Exception unused) {
        }
    }

    /* loaded from: classes3.dex */
    public static class AlertDialogCell extends FrameLayout {
        private ImageView imageView;
        private final Theme.ResourcesProvider resourcesProvider;
        private TextView textView;

        public AlertDialogCell(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor("dialogButtonSelector"), 2));
            setPadding(AndroidUtilities.dp(23.0f), 0, AndroidUtilities.dp(23.0f), 0);
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            this.imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor("dialogIcon"), PorterDuff.Mode.MULTIPLY));
            int i = 5;
            addView(this.imageView, LayoutHelper.createFrame(-2, 40, (LocaleController.isRTL ? 5 : 3) | 16));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setGravity(1);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setTextColor(getThemedColor("dialogTextBlack"));
            this.textView.setTextSize(1, 16.0f);
            addView(this.textView, LayoutHelper.createFrame(-2, -2, (!LocaleController.isRTL ? 3 : i) | 16));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }

        public void setTextColor(int i) {
            this.textView.setTextColor(i);
        }

        public void setGravity(int i) {
            this.textView.setGravity(i);
        }

        public void setTextAndIcon(CharSequence charSequence, int i) {
            this.textView.setText(charSequence);
            if (i != 0) {
                this.imageView.setImageResource(i);
                this.imageView.setVisibility(0);
                this.textView.setPadding(LocaleController.isRTL ? 0 : AndroidUtilities.dp(56.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(56.0f) : 0, 0);
                return;
            }
            this.imageView.setVisibility(4);
            this.textView.setPadding(0, 0, 0, 0);
        }

        private int getThemedColor(String str) {
            Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
            Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
            return color != null ? color.intValue() : Theme.getColor(str);
        }
    }

    public AlertDialog(Context context, int i) {
        this(context, i, null);
    }

    public AlertDialog(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context, 2131689509);
        this.customViewHeight = -2;
        this.shadow = new BitmapDrawable[2];
        this.shadowVisibility = new boolean[2];
        this.shadowAnimation = new AnimatorSet[2];
        this.customViewOffset = 20;
        this.dialogButtonColorKey = "dialogButton";
        this.topHeight = 132;
        this.messageTextViewClickable = true;
        this.canCacnel = true;
        this.dismissDialogByButtons = true;
        this.checkFocusable = true;
        this.dismissRunnable = new AlertDialog$$ExternalSyntheticLambda6(this);
        this.showRunnable = new AlertDialog$$ExternalSyntheticLambda7(this);
        this.itemViews = new ArrayList<>();
        this.dimEnabled = true;
        this.dimAlpha = 0.6f;
        this.topAnimationAutoRepeat = true;
        this.resourcesProvider = resourcesProvider;
        this.backgroundPaddings = new Rect();
        if (i != 3) {
            Drawable mutate = context.getResources().getDrawable(2131166085).mutate();
            this.shadowDrawable = mutate;
            mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor("dialogBackground"), PorterDuff.Mode.MULTIPLY));
            this.shadowDrawable.getPadding(this.backgroundPaddings);
        }
        this.progressViewStyle = i;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        int i;
        int i2;
        int i3;
        super.onCreate(bundle);
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(getContext());
        anonymousClass1.setOrientation(1);
        if (this.progressViewStyle == 3) {
            anonymousClass1.setBackgroundDrawable(null);
            anonymousClass1.setPadding(0, 0, 0, 0);
            this.drawBackground = false;
        } else if (this.notDrawBackgroundOnTopView) {
            Rect rect = new Rect();
            this.shadowDrawable.getPadding(rect);
            anonymousClass1.setPadding(rect.left, rect.top, rect.right, rect.bottom);
            this.drawBackground = true;
        } else {
            anonymousClass1.setBackgroundDrawable(null);
            anonymousClass1.setPadding(0, 0, 0, 0);
            anonymousClass1.setBackgroundDrawable(this.shadowDrawable);
            this.drawBackground = false;
        }
        anonymousClass1.setFitsSystemWindows(Build.VERSION.SDK_INT >= 21);
        setContentView(anonymousClass1);
        boolean z = (this.positiveButtonText == null && this.negativeButtonText == null && this.neutralButtonText == null) ? false : true;
        if (this.topResId != 0 || this.topAnimationId != 0 || this.topDrawable != null) {
            RLottieImageView rLottieImageView = new RLottieImageView(getContext());
            this.topImageView = rLottieImageView;
            Drawable drawable = this.topDrawable;
            if (drawable != null) {
                rLottieImageView.setImageDrawable(drawable);
            } else {
                int i4 = this.topResId;
                if (i4 != 0) {
                    rLottieImageView.setImageResource(i4);
                } else {
                    rLottieImageView.setAutoRepeat(this.topAnimationAutoRepeat);
                    RLottieImageView rLottieImageView2 = this.topImageView;
                    int i5 = this.topAnimationId;
                    int i6 = this.topAnimationSize;
                    rLottieImageView2.setAnimation(i5, i6, i6);
                    this.topImageView.playAnimation();
                }
            }
            this.topImageView.setScaleType(ImageView.ScaleType.CENTER);
            this.topImageView.setBackgroundDrawable(getContext().getResources().getDrawable(2131166087));
            this.topImageView.getBackground().setColorFilter(new PorterDuffColorFilter(this.topBackgroundColor, PorterDuff.Mode.MULTIPLY));
            this.topImageView.setPadding(0, 0, 0, 0);
            anonymousClass1.addView(this.topImageView, LayoutHelper.createLinear(-1, this.topHeight, 51, -8, -8, 0, 0));
        } else {
            View view = this.topView;
            if (view != null) {
                view.setPadding(0, 0, 0, 0);
                anonymousClass1.addView(this.topView, LayoutHelper.createLinear(-1, this.topHeight, 51, 0, 0, 0, 0));
            }
        }
        if (this.title != null) {
            FrameLayout frameLayout = new FrameLayout(getContext());
            this.titleContainer = frameLayout;
            anonymousClass1.addView(frameLayout, LayoutHelper.createLinear(-2, -2, 24.0f, 0.0f, 24.0f, 0.0f));
            TextView textView = new TextView(getContext());
            this.titleTextView = textView;
            textView.setText(this.title);
            this.titleTextView.setTextColor(getThemedColor("dialogTextBlack"));
            this.titleTextView.setTextSize(1, 20.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.titleTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            this.titleContainer.addView(this.titleTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 0.0f, 19.0f, 0.0f, this.subtitle != null ? 2 : this.items != null ? 14 : 10));
        }
        if (this.secondTitle != null && this.title != null) {
            TextView textView2 = new TextView(getContext());
            this.secondTitleTextView = textView2;
            textView2.setText(this.secondTitle);
            this.secondTitleTextView.setTextColor(getThemedColor("dialogTextGray3"));
            this.secondTitleTextView.setTextSize(1, 18.0f);
            this.secondTitleTextView.setGravity((LocaleController.isRTL ? 3 : 5) | 48);
            this.titleContainer.addView(this.secondTitleTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 3 : 5) | 48, 0.0f, 21.0f, 0.0f, 0.0f));
        }
        if (this.subtitle != null) {
            TextView textView3 = new TextView(getContext());
            this.subtitleTextView = textView3;
            textView3.setText(this.subtitle);
            this.subtitleTextView.setTextColor(getThemedColor("dialogIcon"));
            this.subtitleTextView.setTextSize(1, 14.0f);
            this.subtitleTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            anonymousClass1.addView(this.subtitleTextView, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 24, 0, 24, this.items != null ? 14 : 10));
        }
        if (this.progressViewStyle == 0) {
            this.shadow[0] = (BitmapDrawable) getContext().getResources().getDrawable(2131165446).mutate();
            this.shadow[1] = (BitmapDrawable) getContext().getResources().getDrawable(2131165447).mutate();
            this.shadow[0].setAlpha(0);
            this.shadow[1].setAlpha(0);
            this.shadow[0].setCallback(this);
            this.shadow[1].setCallback(this);
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(getContext());
            this.contentScrollView = anonymousClass2;
            anonymousClass2.setVerticalScrollBarEnabled(false);
            AndroidUtilities.setScrollViewEdgeEffectColor(this.contentScrollView, getThemedColor("dialogScrollGlow"));
            anonymousClass1.addView(this.contentScrollView, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 0.0f));
            LinearLayout linearLayout = new LinearLayout(getContext());
            this.scrollContainer = linearLayout;
            linearLayout.setOrientation(1);
            this.contentScrollView.addView(this.scrollContainer, new FrameLayout.LayoutParams(-1, -2));
        }
        SpoilersTextView spoilersTextView = new SpoilersTextView(getContext());
        this.messageTextView = spoilersTextView;
        spoilersTextView.setTextColor(getThemedColor("dialogTextBlack"));
        this.messageTextView.setTextSize(1, 16.0f);
        this.messageTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        this.messageTextView.setLinkTextColor(getThemedColor("dialogTextLink"));
        if (!this.messageTextViewClickable) {
            this.messageTextView.setClickable(false);
            this.messageTextView.setEnabled(false);
        }
        this.messageTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        int i7 = this.progressViewStyle;
        if (i7 == 1) {
            FrameLayout frameLayout2 = new FrameLayout(getContext());
            this.progressViewContainer = frameLayout2;
            anonymousClass1.addView(frameLayout2, LayoutHelper.createLinear(-1, 44, 51, 23, this.title == null ? 24 : 0, 23, 24));
            RadialProgressView radialProgressView = new RadialProgressView(getContext(), this.resourcesProvider);
            radialProgressView.setProgressColor(getThemedColor("dialogProgressCircle"));
            this.progressViewContainer.addView(radialProgressView, LayoutHelper.createFrame(44, 44, (LocaleController.isRTL ? 5 : 3) | 48));
            this.messageTextView.setLines(1);
            this.messageTextView.setEllipsize(TextUtils.TruncateAt.END);
            FrameLayout frameLayout3 = this.progressViewContainer;
            TextView textView4 = this.messageTextView;
            boolean z2 = LocaleController.isRTL;
            frameLayout3.addView(textView4, LayoutHelper.createFrame(-2, -2.0f, (z2 ? 5 : 3) | 16, z2 ? 0 : 62, 0.0f, z2 ? 62 : 0, 0.0f));
        } else if (i7 == 2) {
            anonymousClass1.addView(this.messageTextView, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 24, this.title == null ? 19 : 0, 24, 20));
            LineProgressView lineProgressView = new LineProgressView(getContext());
            this.lineProgressView = lineProgressView;
            lineProgressView.setProgress(this.currentProgress / 100.0f, false);
            this.lineProgressView.setProgressColor(getThemedColor("dialogLineProgress"));
            this.lineProgressView.setBackColor(getThemedColor("dialogLineProgressBackground"));
            anonymousClass1.addView(this.lineProgressView, LayoutHelper.createLinear(-1, 4, 19, 24, 0, 24, 0));
            TextView textView5 = new TextView(getContext());
            this.lineProgressViewPercent = textView5;
            textView5.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.lineProgressViewPercent.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            this.lineProgressViewPercent.setTextColor(getThemedColor("dialogTextGray2"));
            this.lineProgressViewPercent.setTextSize(1, 14.0f);
            anonymousClass1.addView(this.lineProgressViewPercent, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 23, 4, 23, 24));
            updateLineProgressTextView();
        } else if (i7 == 3) {
            setCanceledOnTouchOutside(false);
            setCancelable(false);
            FrameLayout frameLayout4 = new FrameLayout(getContext());
            this.progressViewContainer = frameLayout4;
            frameLayout4.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), getThemedColor("dialog_inlineProgressBackground")));
            anonymousClass1.addView(this.progressViewContainer, LayoutHelper.createLinear(86, 86, 17));
            RadialProgressView radialProgressView2 = new RadialProgressView(getContext(), this.resourcesProvider);
            radialProgressView2.setProgressColor(getThemedColor("dialog_inlineProgress"));
            this.progressViewContainer.addView(radialProgressView2, LayoutHelper.createLinear(86, 86));
        } else {
            this.scrollContainer.addView(this.messageTextView, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 24, 0, 24, (this.customView == null && this.items == null) ? 0 : this.customViewOffset));
        }
        if (!TextUtils.isEmpty(this.message)) {
            this.messageTextView.setText(this.message);
            this.messageTextView.setVisibility(0);
        } else {
            this.messageTextView.setVisibility(8);
        }
        if (this.items != null) {
            int i8 = 0;
            while (true) {
                CharSequence[] charSequenceArr = this.items;
                if (i8 >= charSequenceArr.length) {
                    break;
                }
                if (charSequenceArr[i8] != null) {
                    AlertDialogCell alertDialogCell = new AlertDialogCell(getContext(), this.resourcesProvider);
                    CharSequence charSequence = this.items[i8];
                    int[] iArr = this.itemIcons;
                    alertDialogCell.setTextAndIcon(charSequence, iArr != null ? iArr[i8] : 0);
                    alertDialogCell.setTag(Integer.valueOf(i8));
                    this.itemViews.add(alertDialogCell);
                    this.scrollContainer.addView(alertDialogCell, LayoutHelper.createLinear(-1, 50));
                    alertDialogCell.setOnClickListener(new AlertDialog$$ExternalSyntheticLambda2(this));
                }
                i8++;
            }
        }
        View view2 = this.customView;
        if (view2 != null) {
            if (view2.getParent() != null) {
                ((ViewGroup) this.customView.getParent()).removeView(this.customView);
            }
            this.scrollContainer.addView(this.customView, LayoutHelper.createLinear(-1, this.customViewHeight));
        }
        if (z) {
            if (!this.verticalButtons) {
                TextPaint textPaint = new TextPaint();
                textPaint.setTextSize(AndroidUtilities.dp(14.0f));
                CharSequence charSequence2 = this.positiveButtonText;
                int measureText = charSequence2 != null ? (int) (0 + textPaint.measureText(charSequence2, 0, charSequence2.length()) + AndroidUtilities.dp(10.0f)) : 0;
                CharSequence charSequence3 = this.negativeButtonText;
                if (charSequence3 != null) {
                    measureText = (int) (measureText + textPaint.measureText(charSequence3, 0, charSequence3.length()) + AndroidUtilities.dp(10.0f));
                }
                CharSequence charSequence4 = this.neutralButtonText;
                if (charSequence4 != null) {
                    measureText = (int) (measureText + textPaint.measureText(charSequence4, 0, charSequence4.length()) + AndroidUtilities.dp(10.0f));
                }
                if (measureText > AndroidUtilities.displaySize.x - AndroidUtilities.dp(110.0f)) {
                    this.verticalButtons = true;
                }
            }
            if (this.verticalButtons) {
                LinearLayout linearLayout2 = new LinearLayout(getContext());
                linearLayout2.setOrientation(1);
                this.buttonsLayout = linearLayout2;
            } else {
                this.buttonsLayout = new AnonymousClass3(this, getContext());
            }
            this.buttonsLayout.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            anonymousClass1.addView(this.buttonsLayout, LayoutHelper.createLinear(-1, 52));
            if (this.positiveButtonText != null) {
                AnonymousClass4 anonymousClass4 = new AnonymousClass4(this, getContext());
                anonymousClass4.setMinWidth(AndroidUtilities.dp(64.0f));
                anonymousClass4.setTag(-1);
                anonymousClass4.setTextSize(1, 14.0f);
                anonymousClass4.setTextColor(getThemedColor(this.dialogButtonColorKey));
                anonymousClass4.setGravity(17);
                anonymousClass4.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                anonymousClass4.setText(this.positiveButtonText.toString().toUpperCase());
                anonymousClass4.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(getThemedColor(this.dialogButtonColorKey)));
                anonymousClass4.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
                if (this.verticalButtons) {
                    this.buttonsLayout.addView(anonymousClass4, LayoutHelper.createLinear(-2, 36, LocaleController.isRTL ? 3 : 5));
                } else {
                    this.buttonsLayout.addView(anonymousClass4, LayoutHelper.createFrame(-2, 36, 53));
                }
                anonymousClass4.setOnClickListener(new AlertDialog$$ExternalSyntheticLambda5(this));
            }
            if (this.negativeButtonText != null) {
                AnonymousClass5 anonymousClass5 = new AnonymousClass5(this, getContext());
                anonymousClass5.setMinWidth(AndroidUtilities.dp(64.0f));
                anonymousClass5.setTag(-2);
                anonymousClass5.setTextSize(1, 14.0f);
                anonymousClass5.setTextColor(getThemedColor(this.dialogButtonColorKey));
                anonymousClass5.setGravity(17);
                anonymousClass5.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                anonymousClass5.setEllipsize(TextUtils.TruncateAt.END);
                anonymousClass5.setSingleLine(true);
                anonymousClass5.setText(this.negativeButtonText.toString().toUpperCase());
                anonymousClass5.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(getThemedColor(this.dialogButtonColorKey)));
                anonymousClass5.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
                if (this.verticalButtons) {
                    this.buttonsLayout.addView(anonymousClass5, 0, LayoutHelper.createLinear(-2, 36, LocaleController.isRTL ? 3 : 5));
                } else {
                    this.buttonsLayout.addView(anonymousClass5, LayoutHelper.createFrame(-2, 36, 53));
                }
                anonymousClass5.setOnClickListener(new AlertDialog$$ExternalSyntheticLambda4(this));
            }
            if (this.neutralButtonText != null) {
                AnonymousClass6 anonymousClass6 = new AnonymousClass6(this, getContext());
                anonymousClass6.setMinWidth(AndroidUtilities.dp(64.0f));
                anonymousClass6.setTag(-3);
                anonymousClass6.setTextSize(1, 14.0f);
                anonymousClass6.setTextColor(getThemedColor(this.dialogButtonColorKey));
                anonymousClass6.setGravity(17);
                anonymousClass6.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                anonymousClass6.setEllipsize(TextUtils.TruncateAt.END);
                anonymousClass6.setSingleLine(true);
                anonymousClass6.setText(this.neutralButtonText.toString().toUpperCase());
                anonymousClass6.setBackground(Theme.getRoundRectSelectorDrawable(getThemedColor(this.dialogButtonColorKey)));
                anonymousClass6.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
                if (this.verticalButtons) {
                    ViewGroup viewGroup = this.buttonsLayout;
                    if (LocaleController.isRTL) {
                        i3 = -2;
                        i2 = 3;
                    } else {
                        i3 = -2;
                        i2 = 5;
                    }
                    viewGroup.addView(anonymousClass6, 1, LayoutHelper.createLinear(i3, 36, i2));
                } else {
                    this.buttonsLayout.addView(anonymousClass6, LayoutHelper.createFrame(-2, 36, 51));
                }
                anonymousClass6.setOnClickListener(new AlertDialog$$ExternalSyntheticLambda3(this));
            }
            if (this.verticalButtons) {
                for (int i9 = 1; i9 < this.buttonsLayout.getChildCount(); i9++) {
                    ((ViewGroup.MarginLayoutParams) this.buttonsLayout.getChildAt(i9).getLayoutParams()).topMargin = AndroidUtilities.dp(6.0f);
                }
            }
        }
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(window.getAttributes());
        if (this.progressViewStyle == 3) {
            layoutParams.width = -1;
        } else {
            if (this.dimEnabled) {
                layoutParams.dimAmount = this.dimAlpha;
                layoutParams.flags |= 2;
            } else {
                layoutParams.dimAmount = 0.0f;
                layoutParams.flags ^= 2;
            }
            int i10 = AndroidUtilities.displaySize.x;
            this.lastScreenWidth = i10;
            int dp = i10 - AndroidUtilities.dp(48.0f);
            if (AndroidUtilities.isTablet()) {
                if (AndroidUtilities.isSmallTablet()) {
                    i = AndroidUtilities.dp(446.0f);
                } else {
                    i = AndroidUtilities.dp(496.0f);
                }
            } else {
                i = AndroidUtilities.dp(356.0f);
            }
            int min = Math.min(i, dp);
            Rect rect2 = this.backgroundPaddings;
            layoutParams.width = min + rect2.left + rect2.right;
        }
        View view3 = this.customView;
        if (view3 == null || !this.checkFocusable || !canTextInput(view3)) {
            layoutParams.flags |= 131072;
        } else {
            layoutParams.softInputMode = 4;
        }
        if (Build.VERSION.SDK_INT >= 28) {
            layoutParams.layoutInDisplayCutoutMode = 0;
        }
        window.setAttributes(layoutParams);
    }

    /* renamed from: org.telegram.ui.ActionBar.AlertDialog$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends LinearLayout {
        private boolean inLayout;

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context) {
            super(context);
            AlertDialog.this = r1;
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (AlertDialog.this.progressViewStyle == 3) {
                AlertDialog.this.showCancelAlert();
                return false;
            }
            return super.onTouchEvent(motionEvent);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (AlertDialog.this.progressViewStyle == 3) {
                AlertDialog.this.showCancelAlert();
                return false;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        /* JADX WARN: Removed duplicated region for block: B:85:0x032d  */
        @Override // android.widget.LinearLayout, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onMeasure(int i, int i2) {
            int i3;
            int measuredHeight;
            int i4;
            float f;
            float f2;
            if (AlertDialog.this.progressViewStyle == 3) {
                AlertDialog.this.progressViewContainer.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0f), 1073741824));
                setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
                return;
            }
            this.inLayout = true;
            int size = View.MeasureSpec.getSize(i);
            int size2 = (View.MeasureSpec.getSize(i2) - getPaddingTop()) - getPaddingBottom();
            int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(paddingLeft - AndroidUtilities.dp(48.0f), 1073741824);
            int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(paddingLeft, 1073741824);
            ViewGroup viewGroup = AlertDialog.this.buttonsLayout;
            if (viewGroup != null) {
                int childCount = viewGroup.getChildCount();
                for (int i5 = 0; i5 < childCount; i5++) {
                    View childAt = AlertDialog.this.buttonsLayout.getChildAt(i5);
                    if (childAt instanceof TextView) {
                        ((TextView) childAt).setMaxWidth(AndroidUtilities.dp((paddingLeft - AndroidUtilities.dp(24.0f)) / 2));
                    }
                }
                AlertDialog.this.buttonsLayout.measure(makeMeasureSpec2, i2);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) AlertDialog.this.buttonsLayout.getLayoutParams();
                i3 = size2 - ((AlertDialog.this.buttonsLayout.getMeasuredHeight() + layoutParams.bottomMargin) + layoutParams.topMargin);
            } else {
                i3 = size2;
            }
            if (AlertDialog.this.secondTitleTextView != null) {
                AlertDialog.this.secondTitleTextView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(makeMeasureSpec), Integer.MIN_VALUE), i2);
            }
            if (AlertDialog.this.titleTextView != null) {
                if (AlertDialog.this.secondTitleTextView != null) {
                    AlertDialog.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec((View.MeasureSpec.getSize(makeMeasureSpec) - AlertDialog.this.secondTitleTextView.getMeasuredWidth()) - AndroidUtilities.dp(8.0f), 1073741824), i2);
                } else {
                    AlertDialog.this.titleTextView.measure(makeMeasureSpec, i2);
                }
            }
            if (AlertDialog.this.titleContainer != null) {
                AlertDialog.this.titleContainer.measure(makeMeasureSpec, i2);
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) AlertDialog.this.titleContainer.getLayoutParams();
                i3 -= (AlertDialog.this.titleContainer.getMeasuredHeight() + layoutParams2.bottomMargin) + layoutParams2.topMargin;
            }
            if (AlertDialog.this.subtitleTextView != null) {
                AlertDialog.this.subtitleTextView.measure(makeMeasureSpec, i2);
                LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) AlertDialog.this.subtitleTextView.getLayoutParams();
                i3 -= (AlertDialog.this.subtitleTextView.getMeasuredHeight() + layoutParams3.bottomMargin) + layoutParams3.topMargin;
            }
            if (AlertDialog.this.topImageView != null) {
                AlertDialog.this.topImageView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(AlertDialog.this.topHeight), 1073741824));
                i3 -= AlertDialog.this.topImageView.getMeasuredHeight() - AndroidUtilities.dp(8.0f);
            }
            if (AlertDialog.this.topView != null) {
                int dp = size - AndroidUtilities.dp(16.0f);
                if (AlertDialog.this.aspectRatio == 0.0f) {
                    f2 = dp / 936.0f;
                    f = 354.0f;
                } else {
                    f2 = dp;
                    f = AlertDialog.this.aspectRatio;
                }
                int i6 = (int) (f2 * f);
                AlertDialog.this.topView.measure(View.MeasureSpec.makeMeasureSpec(dp, 1073741824), View.MeasureSpec.makeMeasureSpec(i6, 1073741824));
                AlertDialog.this.topView.getLayoutParams().height = i6;
                i3 -= AlertDialog.this.topView.getMeasuredHeight();
            }
            if (AlertDialog.this.progressViewStyle == 0) {
                LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) AlertDialog.this.contentScrollView.getLayoutParams();
                if (AlertDialog.this.customView != null) {
                    layoutParams4.topMargin = (AlertDialog.this.titleTextView == null && AlertDialog.this.messageTextView.getVisibility() == 8 && AlertDialog.this.items == null) ? AndroidUtilities.dp(16.0f) : 0;
                    layoutParams4.bottomMargin = AlertDialog.this.buttonsLayout == null ? AndroidUtilities.dp(8.0f) : 0;
                } else if (AlertDialog.this.items != null) {
                    layoutParams4.topMargin = (AlertDialog.this.titleTextView == null && AlertDialog.this.messageTextView.getVisibility() == 8) ? AndroidUtilities.dp(8.0f) : 0;
                    layoutParams4.bottomMargin = AndroidUtilities.dp(8.0f);
                } else if (AlertDialog.this.messageTextView.getVisibility() == 0) {
                    layoutParams4.topMargin = AlertDialog.this.titleTextView == null ? AndroidUtilities.dp(19.0f) : 0;
                    layoutParams4.bottomMargin = AndroidUtilities.dp(20.0f);
                }
                int i7 = i3 - (layoutParams4.bottomMargin + layoutParams4.topMargin);
                AlertDialog.this.contentScrollView.measure(makeMeasureSpec2, View.MeasureSpec.makeMeasureSpec(i7, Integer.MIN_VALUE));
                i3 = i7 - AlertDialog.this.contentScrollView.getMeasuredHeight();
            } else {
                if (AlertDialog.this.progressViewContainer != null) {
                    AlertDialog.this.progressViewContainer.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE));
                    LinearLayout.LayoutParams layoutParams5 = (LinearLayout.LayoutParams) AlertDialog.this.progressViewContainer.getLayoutParams();
                    measuredHeight = AlertDialog.this.progressViewContainer.getMeasuredHeight() + layoutParams5.bottomMargin;
                    i4 = layoutParams5.topMargin;
                } else {
                    if (AlertDialog.this.messageTextView != null) {
                        AlertDialog.this.messageTextView.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE));
                        if (AlertDialog.this.messageTextView.getVisibility() != 8) {
                            LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) AlertDialog.this.messageTextView.getLayoutParams();
                            measuredHeight = AlertDialog.this.messageTextView.getMeasuredHeight() + layoutParams6.bottomMargin;
                            i4 = layoutParams6.topMargin;
                        }
                    }
                    if (AlertDialog.this.lineProgressView != null) {
                        AlertDialog.this.lineProgressView.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(4.0f), 1073741824));
                        LinearLayout.LayoutParams layoutParams7 = (LinearLayout.LayoutParams) AlertDialog.this.lineProgressView.getLayoutParams();
                        int measuredHeight2 = i3 - ((AlertDialog.this.lineProgressView.getMeasuredHeight() + layoutParams7.bottomMargin) + layoutParams7.topMargin);
                        AlertDialog.this.lineProgressViewPercent.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(measuredHeight2, Integer.MIN_VALUE));
                        LinearLayout.LayoutParams layoutParams8 = (LinearLayout.LayoutParams) AlertDialog.this.lineProgressViewPercent.getLayoutParams();
                        i3 = measuredHeight2 - ((AlertDialog.this.lineProgressViewPercent.getMeasuredHeight() + layoutParams8.bottomMargin) + layoutParams8.topMargin);
                    }
                }
                i3 -= measuredHeight + i4;
                if (AlertDialog.this.lineProgressView != null) {
                }
            }
            setMeasuredDimension(size, (size2 - i3) + getPaddingTop() + getPaddingBottom());
            this.inLayout = false;
            if (AlertDialog.this.lastScreenWidth == AndroidUtilities.displaySize.x) {
                return;
            }
            AndroidUtilities.runOnUIThread(new AlertDialog$1$$ExternalSyntheticLambda1(this));
        }

        public /* synthetic */ void lambda$onMeasure$0() {
            int i;
            AlertDialog.this.lastScreenWidth = AndroidUtilities.displaySize.x;
            int dp = AndroidUtilities.displaySize.x - AndroidUtilities.dp(56.0f);
            if (AndroidUtilities.isTablet()) {
                if (AndroidUtilities.isSmallTablet()) {
                    i = AndroidUtilities.dp(446.0f);
                } else {
                    i = AndroidUtilities.dp(496.0f);
                }
            } else {
                i = AndroidUtilities.dp(356.0f);
            }
            Window window = AlertDialog.this.getWindow();
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = Math.min(i, dp) + AlertDialog.this.backgroundPaddings.left + AlertDialog.this.backgroundPaddings.right;
            try {
                window.setAttributes(layoutParams);
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }

        @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (AlertDialog.this.progressViewStyle == 3) {
                int measuredWidth = ((i3 - i) - AlertDialog.this.progressViewContainer.getMeasuredWidth()) / 2;
                int measuredHeight = ((i4 - i2) - AlertDialog.this.progressViewContainer.getMeasuredHeight()) / 2;
                AlertDialog.this.progressViewContainer.layout(measuredWidth, measuredHeight, AlertDialog.this.progressViewContainer.getMeasuredWidth() + measuredWidth, AlertDialog.this.progressViewContainer.getMeasuredHeight() + measuredHeight);
            } else if (AlertDialog.this.contentScrollView == null) {
            } else {
                if (AlertDialog.this.onScrollChangedListener == null) {
                    AlertDialog.this.onScrollChangedListener = new AlertDialog$1$$ExternalSyntheticLambda0(this);
                    AlertDialog.this.contentScrollView.getViewTreeObserver().addOnScrollChangedListener(AlertDialog.this.onScrollChangedListener);
                }
                AlertDialog.this.onScrollChangedListener.onScrollChanged();
            }
        }

        public /* synthetic */ void lambda$onLayout$1() {
            AlertDialog alertDialog = AlertDialog.this;
            boolean z = false;
            alertDialog.runShadowAnimation(0, alertDialog.titleTextView != null && AlertDialog.this.contentScrollView.getScrollY() > AlertDialog.this.scrollContainer.getTop());
            AlertDialog alertDialog2 = AlertDialog.this;
            if (alertDialog2.buttonsLayout != null && alertDialog2.contentScrollView.getScrollY() + AlertDialog.this.contentScrollView.getHeight() < AlertDialog.this.scrollContainer.getBottom()) {
                z = true;
            }
            alertDialog2.runShadowAnimation(1, z);
            AlertDialog.this.contentScrollView.invalidate();
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.inLayout) {
                return;
            }
            super.requestLayout();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (AlertDialog.this.drawBackground) {
                AlertDialog.this.shadowDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                if (AlertDialog.this.topView == null || !AlertDialog.this.notDrawBackgroundOnTopView) {
                    AlertDialog.this.shadowDrawable.draw(canvas);
                } else {
                    int bottom = AlertDialog.this.topView.getBottom();
                    canvas.save();
                    canvas.clipRect(0, bottom, getMeasuredWidth(), getMeasuredHeight());
                    AlertDialog.this.shadowDrawable.draw(canvas);
                    canvas.restore();
                }
            }
            super.dispatchDraw(canvas);
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.AlertDialog$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ScrollView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            AlertDialog.this = r1;
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            boolean drawChild = super.drawChild(canvas, view, j);
            if (AlertDialog.this.shadow[0].getPaint().getAlpha() != 0) {
                AlertDialog.this.shadow[0].setBounds(0, getScrollY(), getMeasuredWidth(), getScrollY() + AndroidUtilities.dp(3.0f));
                AlertDialog.this.shadow[0].draw(canvas);
            }
            if (AlertDialog.this.shadow[1].getPaint().getAlpha() != 0) {
                AlertDialog.this.shadow[1].setBounds(0, (getScrollY() + getMeasuredHeight()) - AndroidUtilities.dp(3.0f), getMeasuredWidth(), getScrollY() + getMeasuredHeight());
                AlertDialog.this.shadow[1].draw(canvas);
            }
            return drawChild;
        }
    }

    public /* synthetic */ void lambda$onCreate$1(View view) {
        DialogInterface.OnClickListener onClickListener = this.onClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, ((Integer) view.getTag()).intValue());
        }
        dismiss();
    }

    /* renamed from: org.telegram.ui.ActionBar.AlertDialog$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends FrameLayout {
        AnonymousClass3(AlertDialog alertDialog, Context context) {
            super(context);
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int childCount = getChildCount();
            int i7 = i3 - i;
            View view = null;
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = getChildAt(i8);
                Integer num = (Integer) childAt.getTag();
                if (num != null) {
                    if (num.intValue() == -1) {
                        if (LocaleController.isRTL) {
                            childAt.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + childAt.getMeasuredWidth(), getPaddingTop() + childAt.getMeasuredHeight());
                        } else {
                            childAt.layout((i7 - getPaddingRight()) - childAt.getMeasuredWidth(), getPaddingTop(), i7 - getPaddingRight(), getPaddingTop() + childAt.getMeasuredHeight());
                        }
                        view = childAt;
                    } else if (num.intValue() == -2) {
                        if (LocaleController.isRTL) {
                            int paddingLeft = getPaddingLeft();
                            if (view != null) {
                                paddingLeft += view.getMeasuredWidth() + AndroidUtilities.dp(8.0f);
                            }
                            childAt.layout(paddingLeft, getPaddingTop(), childAt.getMeasuredWidth() + paddingLeft, getPaddingTop() + childAt.getMeasuredHeight());
                        } else {
                            int paddingRight = (i7 - getPaddingRight()) - childAt.getMeasuredWidth();
                            if (view != null) {
                                paddingRight -= view.getMeasuredWidth() + AndroidUtilities.dp(8.0f);
                            }
                            childAt.layout(paddingRight, getPaddingTop(), childAt.getMeasuredWidth() + paddingRight, getPaddingTop() + childAt.getMeasuredHeight());
                        }
                    } else if (num.intValue() == -3) {
                        if (LocaleController.isRTL) {
                            childAt.layout((i7 - getPaddingRight()) - childAt.getMeasuredWidth(), getPaddingTop(), i7 - getPaddingRight(), getPaddingTop() + childAt.getMeasuredHeight());
                        } else {
                            childAt.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + childAt.getMeasuredWidth(), getPaddingTop() + childAt.getMeasuredHeight());
                        }
                    }
                } else {
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    if (view != null) {
                        i6 = view.getLeft() + ((view.getMeasuredWidth() - measuredWidth) / 2);
                        i5 = view.getTop() + ((view.getMeasuredHeight() - measuredHeight) / 2);
                    } else {
                        i6 = 0;
                        i5 = 0;
                    }
                    childAt.layout(i6, i5, measuredWidth + i6, measuredHeight + i5);
                }
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            int measuredWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
            int childCount = getChildCount();
            int i3 = 0;
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = getChildAt(i4);
                if ((childAt instanceof TextView) && childAt.getTag() != null) {
                    i3 += childAt.getMeasuredWidth();
                }
            }
            if (i3 > measuredWidth) {
                View findViewWithTag = findViewWithTag(-2);
                View findViewWithTag2 = findViewWithTag(-3);
                if (findViewWithTag == null || findViewWithTag2 == null) {
                    return;
                }
                if (findViewWithTag.getMeasuredWidth() < findViewWithTag2.getMeasuredWidth()) {
                    findViewWithTag2.measure(View.MeasureSpec.makeMeasureSpec(findViewWithTag2.getMeasuredWidth() - (i3 - measuredWidth), 1073741824), View.MeasureSpec.makeMeasureSpec(findViewWithTag2.getMeasuredHeight(), 1073741824));
                } else {
                    findViewWithTag.measure(View.MeasureSpec.makeMeasureSpec(findViewWithTag.getMeasuredWidth() - (i3 - measuredWidth), 1073741824), View.MeasureSpec.makeMeasureSpec(findViewWithTag.getMeasuredHeight(), 1073741824));
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.AlertDialog$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends TextView {
        AnonymousClass4(AlertDialog alertDialog, Context context) {
            super(context);
        }

        @Override // android.widget.TextView, android.view.View
        public void setEnabled(boolean z) {
            super.setEnabled(z);
            setAlpha(z ? 1.0f : 0.5f);
        }

        @Override // android.widget.TextView
        public void setTextColor(int i) {
            super.setTextColor(i);
            setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(i));
        }
    }

    public /* synthetic */ void lambda$onCreate$2(View view) {
        DialogInterface.OnClickListener onClickListener = this.positiveButtonListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, -1);
        }
        if (this.dismissDialogByButtons) {
            dismiss();
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.AlertDialog$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends TextView {
        AnonymousClass5(AlertDialog alertDialog, Context context) {
            super(context);
        }

        @Override // android.widget.TextView, android.view.View
        public void setEnabled(boolean z) {
            super.setEnabled(z);
            setAlpha(z ? 1.0f : 0.5f);
        }

        @Override // android.widget.TextView
        public void setTextColor(int i) {
            super.setTextColor(i);
            setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(i));
        }
    }

    public /* synthetic */ void lambda$onCreate$3(View view) {
        DialogInterface.OnClickListener onClickListener = this.negativeButtonListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, -2);
        }
        if (this.dismissDialogByButtons) {
            cancel();
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.AlertDialog$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 extends TextView {
        AnonymousClass6(AlertDialog alertDialog, Context context) {
            super(context);
        }

        @Override // android.widget.TextView, android.view.View
        public void setEnabled(boolean z) {
            super.setEnabled(z);
            setAlpha(z ? 1.0f : 0.5f);
        }

        @Override // android.widget.TextView
        public void setTextColor(int i) {
            super.setTextColor(i);
            setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(i));
        }
    }

    public /* synthetic */ void lambda$onCreate$4(View view) {
        DialogInterface.OnClickListener onClickListener = this.neutralButtonListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, -2);
        }
        if (this.dismissDialogByButtons) {
            dismiss();
        }
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        super.onBackPressed();
        DialogInterface.OnClickListener onClickListener = this.onBackButtonListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, -2);
        }
    }

    public void setFocusable(boolean z) {
        if (this.focusable == z) {
            return;
        }
        this.focusable = z;
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (this.focusable) {
            attributes.softInputMode = 16;
            attributes.flags &= -131073;
        } else {
            attributes.softInputMode = 48;
            attributes.flags |= 131072;
        }
        window.setAttributes(attributes);
    }

    public void setBackgroundColor(int i) {
        this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
    }

    public void setTextColor(int i) {
        TextView textView = this.titleTextView;
        if (textView != null) {
            textView.setTextColor(i);
        }
        TextView textView2 = this.messageTextView;
        if (textView2 != null) {
            textView2.setTextColor(i);
        }
    }

    public void showCancelAlert() {
        if (!this.canCacnel || this.cancelDialog != null) {
            return;
        }
        Builder builder = new Builder(getContext());
        builder.setTitle(LocaleController.getString("AppName", 2131624375));
        builder.setMessage(LocaleController.getString("StopLoading", 2131628471));
        builder.setPositiveButton(LocaleController.getString("WaitMore", 2131629220), null);
        builder.setNegativeButton(LocaleController.getString("Stop", 2131628463), new AlertDialog$$ExternalSyntheticLambda0(this));
        builder.setOnDismissListener(new AlertDialog$$ExternalSyntheticLambda1(this));
        try {
            this.cancelDialog = builder.show();
        } catch (Exception unused) {
        }
    }

    public /* synthetic */ void lambda$showCancelAlert$5(DialogInterface dialogInterface, int i) {
        DialogInterface.OnCancelListener onCancelListener = this.onCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(this);
        }
        dismiss();
    }

    public /* synthetic */ void lambda$showCancelAlert$6(DialogInterface dialogInterface) {
        this.cancelDialog = null;
    }

    public void runShadowAnimation(int i, boolean z) {
        if ((!z || this.shadowVisibility[i]) && (z || !this.shadowVisibility[i])) {
            return;
        }
        this.shadowVisibility[i] = z;
        AnimatorSet[] animatorSetArr = this.shadowAnimation;
        if (animatorSetArr[i] != null) {
            animatorSetArr[i].cancel();
        }
        this.shadowAnimation[i] = new AnimatorSet();
        BitmapDrawable[] bitmapDrawableArr = this.shadow;
        if (bitmapDrawableArr[i] != null) {
            AnimatorSet animatorSet = this.shadowAnimation[i];
            Animator[] animatorArr = new Animator[1];
            BitmapDrawable bitmapDrawable = bitmapDrawableArr[i];
            int[] iArr = new int[1];
            iArr[0] = z ? 255 : 0;
            animatorArr[0] = ObjectAnimator.ofInt(bitmapDrawable, "alpha", iArr);
            animatorSet.playTogether(animatorArr);
        }
        this.shadowAnimation[i].setDuration(150L);
        this.shadowAnimation[i].addListener(new AnonymousClass7(i));
        try {
            this.shadowAnimation[i].start();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.AlertDialog$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$num;

        AnonymousClass7(int i) {
            AlertDialog.this = r1;
            this.val$num = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (AlertDialog.this.shadowAnimation[this.val$num] == null || !AlertDialog.this.shadowAnimation[this.val$num].equals(animator)) {
                return;
            }
            AlertDialog.this.shadowAnimation[this.val$num] = null;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (AlertDialog.this.shadowAnimation[this.val$num] == null || !AlertDialog.this.shadowAnimation[this.val$num].equals(animator)) {
                return;
            }
            AlertDialog.this.shadowAnimation[this.val$num] = null;
        }
    }

    public void setProgress(int i) {
        this.currentProgress = i;
        LineProgressView lineProgressView = this.lineProgressView;
        if (lineProgressView != null) {
            lineProgressView.setProgress(i / 100.0f, true);
            updateLineProgressTextView();
        }
    }

    private void updateLineProgressTextView() {
        this.lineProgressViewPercent.setText(String.format("%d%%", Integer.valueOf(this.currentProgress)));
    }

    public void setCanCancel(boolean z) {
        this.canCacnel = z;
    }

    private boolean canTextInput(View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        while (childCount > 0) {
            childCount--;
            if (canTextInput(viewGroup.getChildAt(childCount))) {
                return true;
            }
        }
        return false;
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        DialogInterface.OnDismissListener onDismissListener = this.onDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(this);
        }
        AlertDialog alertDialog = this.cancelDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        try {
            super.dismiss();
        } catch (Throwable unused) {
        }
        AndroidUtilities.cancelRunOnUIThread(this.showRunnable);
    }

    @Override // android.app.Dialog
    public void setCanceledOnTouchOutside(boolean z) {
        super.setCanceledOnTouchOutside(z);
    }

    @Override // android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        this.title = charSequence;
        TextView textView = this.titleTextView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void setNeutralButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        this.neutralButtonText = charSequence;
        this.neutralButtonListener = onClickListener;
    }

    public void setItemColor(int i, int i2, int i3) {
        if (i < 0 || i >= this.itemViews.size()) {
            return;
        }
        AlertDialogCell alertDialogCell = this.itemViews.get(i);
        alertDialogCell.textView.setTextColor(i2);
        alertDialogCell.imageView.setColorFilter(new PorterDuffColorFilter(i3, PorterDuff.Mode.MULTIPLY));
    }

    public int getItemsCount() {
        return this.itemViews.size();
    }

    public void setMessage(CharSequence charSequence) {
        this.message = charSequence;
        if (this.messageTextView != null) {
            if (!TextUtils.isEmpty(charSequence)) {
                this.messageTextView.setText(this.message);
                this.messageTextView.setVisibility(0);
                return;
            }
            this.messageTextView.setVisibility(8);
        }
    }

    public View getButton(int i) {
        ViewGroup viewGroup = this.buttonsLayout;
        if (viewGroup != null) {
            return viewGroup.findViewWithTag(Integer.valueOf(i));
        }
        return null;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        this.contentScrollView.invalidate();
        this.scrollContainer.invalidate();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        ScrollView scrollView = this.contentScrollView;
        if (scrollView != null) {
            scrollView.postDelayed(runnable, j);
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        ScrollView scrollView = this.contentScrollView;
        if (scrollView != null) {
            scrollView.removeCallbacks(runnable);
        }
    }

    @Override // android.app.Dialog
    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
        super.setOnCancelListener(onCancelListener);
    }

    public void setPositiveButtonListener(DialogInterface.OnClickListener onClickListener) {
        this.positiveButtonListener = onClickListener;
    }

    public int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }

    public void showDelayed(long j) {
        AndroidUtilities.cancelRunOnUIThread(this.showRunnable);
        AndroidUtilities.runOnUIThread(this.showRunnable, j);
    }

    public ViewGroup getButtonsLayout() {
        return this.buttonsLayout;
    }

    /* loaded from: classes3.dex */
    public static class Builder {
        private AlertDialog alertDialog;

        public Builder(AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
        }

        public Builder(Context context) {
            this(context, null);
        }

        public Builder(Context context, Theme.ResourcesProvider resourcesProvider) {
            this(context, 0, resourcesProvider);
        }

        public Builder(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
            this.alertDialog = new AlertDialog(context, i, resourcesProvider);
        }

        public Context getContext() {
            return this.alertDialog.getContext();
        }

        public Builder setItems(CharSequence[] charSequenceArr, DialogInterface.OnClickListener onClickListener) {
            this.alertDialog.items = charSequenceArr;
            this.alertDialog.onClickListener = onClickListener;
            return this;
        }

        public Builder setCheckFocusable(boolean z) {
            this.alertDialog.checkFocusable = z;
            return this;
        }

        public Builder setItems(CharSequence[] charSequenceArr, int[] iArr, DialogInterface.OnClickListener onClickListener) {
            this.alertDialog.items = charSequenceArr;
            this.alertDialog.itemIcons = iArr;
            this.alertDialog.onClickListener = onClickListener;
            return this;
        }

        public Builder setView(View view) {
            return setView(view, -2);
        }

        public Builder setView(View view, int i) {
            this.alertDialog.customView = view;
            this.alertDialog.customViewHeight = i;
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.alertDialog.title = charSequence;
            return this;
        }

        public Builder setSubtitle(CharSequence charSequence) {
            this.alertDialog.subtitle = charSequence;
            return this;
        }

        public Builder setTopView(View view) {
            this.alertDialog.topView = view;
            return this;
        }

        public Builder setDialogButtonColorKey(String str) {
            this.alertDialog.dialogButtonColorKey = str;
            return this;
        }

        public Builder setTopAnimation(int i, int i2, boolean z, int i3) {
            this.alertDialog.topAnimationId = i;
            this.alertDialog.topAnimationSize = i2;
            this.alertDialog.topAnimationAutoRepeat = z;
            this.alertDialog.topBackgroundColor = i3;
            return this;
        }

        public Builder setTopImage(Drawable drawable, int i) {
            this.alertDialog.topDrawable = drawable;
            this.alertDialog.topBackgroundColor = i;
            return this;
        }

        public Builder setMessage(CharSequence charSequence) {
            this.alertDialog.message = charSequence;
            return this;
        }

        public Builder setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            this.alertDialog.positiveButtonText = charSequence;
            this.alertDialog.positiveButtonListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            this.alertDialog.negativeButtonText = charSequence;
            this.alertDialog.negativeButtonListener = onClickListener;
            return this;
        }

        public Builder setNeutralButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            this.alertDialog.neutralButtonText = charSequence;
            this.alertDialog.neutralButtonListener = onClickListener;
            return this;
        }

        public Builder setOnBackButtonListener(DialogInterface.OnClickListener onClickListener) {
            this.alertDialog.onBackButtonListener = onClickListener;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.alertDialog.setOnCancelListener(onCancelListener);
            return this;
        }

        public Builder setCustomViewOffset(int i) {
            this.alertDialog.customViewOffset = i;
            return this;
        }

        public Builder setMessageTextViewClickable(boolean z) {
            this.alertDialog.messageTextViewClickable = z;
            return this;
        }

        public AlertDialog create() {
            return this.alertDialog;
        }

        public AlertDialog show() {
            this.alertDialog.show();
            return this.alertDialog;
        }

        public Runnable getDismissRunnable() {
            return this.alertDialog.dismissRunnable;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.alertDialog.setOnDismissListener(onDismissListener);
            return this;
        }

        public void setTopViewAspectRatio(float f) {
            this.alertDialog.aspectRatio = f;
        }

        public Builder setDimEnabled(boolean z) {
            this.alertDialog.dimEnabled = z;
            return this;
        }

        public Builder setDimAlpha(float f) {
            this.alertDialog.dimAlpha = f;
            return this;
        }

        public void notDrawBackgroundOnTopView(boolean z) {
            this.alertDialog.notDrawBackgroundOnTopView = z;
        }

        public void setButtonsVertical(boolean z) {
            this.alertDialog.verticalButtons = z;
        }

        public Builder setOnPreDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.alertDialog.onDismissListener = onDismissListener;
            return this;
        }
    }
}
