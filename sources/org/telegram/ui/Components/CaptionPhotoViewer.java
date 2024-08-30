package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BlurringShader;
import org.telegram.ui.Stories.DarkThemeResourceProvider;
import org.telegram.ui.Stories.recorder.CaptionContainerView;
import org.telegram.ui.Stories.recorder.HintView2;
/* loaded from: classes3.dex */
public abstract class CaptionPhotoViewer extends CaptionContainerView {
    private final int SHOW_ONCE;
    private final ImageView addPhotoButton;
    private boolean addPhotoVisible;
    private final Runnable applyCaption;
    private final HintView2 hint;
    private boolean isVideo;
    private Utilities.Callback onTTLChange;
    private int timer;
    private final ImageView timerButton;
    private final CaptionContainerView.PeriodDrawable timerDrawable;
    private ItemOptions timerPopup;
    private boolean timerVisible;
    private final int[] values;

    public CaptionPhotoViewer(Context context, final FrameLayout frameLayout, SizeNotifierFrameLayout sizeNotifierFrameLayout, FrameLayout frameLayout2, Theme.ResourcesProvider resourcesProvider, BlurringShader.BlurManager blurManager, Runnable runnable) {
        super(context, frameLayout, sizeNotifierFrameLayout, frameLayout2, resourcesProvider, blurManager);
        this.timer = 0;
        this.SHOW_ONCE = ConnectionsManager.DEFAULT_DATACENTER_ID;
        this.values = new int[]{ConnectionsManager.DEFAULT_DATACENTER_ID, 3, 10, 30, 0};
        this.applyCaption = runnable;
        ImageView imageView = new ImageView(context);
        this.addPhotoButton = imageView;
        imageView.setImageResource(R.drawable.filled_add_photo);
        ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER;
        imageView.setScaleType(scaleType);
        imageView.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
        imageView.setBackground(Theme.createSelectorDrawable(1090519039, 1, AndroidUtilities.dp(18.0f)));
        setAddPhotoVisible(false, false);
        addView(imageView, LayoutHelper.createFrame(44, 44.0f, 83, 14.0f, 0.0f, 0.0f, 10.0f));
        ImageView imageView2 = new ImageView(context);
        this.timerButton = imageView2;
        CaptionContainerView.PeriodDrawable periodDrawable = new CaptionContainerView.PeriodDrawable();
        this.timerDrawable = periodDrawable;
        imageView2.setImageDrawable(periodDrawable);
        imageView2.setBackground(Theme.createSelectorDrawable(1090519039, 1, AndroidUtilities.dp(18.0f)));
        imageView2.setScaleType(scaleType);
        setTimerVisible(false, false);
        addView(imageView2, LayoutHelper.createFrame(44, 44.0f, 85, 0.0f, 0.0f, 11.0f, 10.0f));
        HintView2 hintView2 = new HintView2(context, 3);
        this.hint = hintView2;
        hintView2.setRounding(12.0f);
        hintView2.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(8.0f));
        hintView2.setJoint(1.0f, -21.0f);
        hintView2.setMultilineText(true);
        addView(hintView2, LayoutHelper.createFrame(-1, 80, 85));
        imageView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.CaptionPhotoViewer$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CaptionPhotoViewer.this.lambda$new$1(frameLayout, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00c1  */
    /* renamed from: changeTimer */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void lambda$new$0(int i) {
        CharSequence replaceTags;
        int i2;
        if (this.timer == i) {
            return;
        }
        setTimer(i);
        Utilities.Callback callback = this.onTTLChange;
        if (callback != null) {
            callback.run(Integer.valueOf(i));
        }
        if (i == 0) {
            i2 = this.isVideo ? R.string.TimerPeriodVideoKeep : R.string.TimerPeriodPhotoKeep;
        } else if (i != Integer.MAX_VALUE) {
            if (i > 0) {
                replaceTags = AndroidUtilities.replaceTags(LocaleController.formatPluralString(this.isVideo ? "TimerPeriodVideoSetSeconds" : "TimerPeriodPhotoSetSeconds", i, new Object[0]));
                this.hint.setMultilineText(true);
                HintView2 hintView2 = this.hint;
                hintView2.setMaxWidthPx(HintView2.cutInFancyHalf(replaceTags, hintView2.getTextPaint()));
                this.hint.setInnerPadding(12, 7, 11, 7);
                this.hint.setIconMargin(2);
                this.hint.setIconTranslate(0.0f, 0.0f);
                this.hint.setTranslationY((-Math.min(AndroidUtilities.dp(34.0f), getEditTextHeight())) - AndroidUtilities.dp(14.0f));
                this.hint.setText(replaceTags);
                int i3 = i <= 0 ? R.raw.fire_on : R.raw.fire_off;
                RLottieDrawable rLottieDrawable = new RLottieDrawable(i3, "" + i3, AndroidUtilities.dp(34.0f), AndroidUtilities.dp(34.0f));
                rLottieDrawable.start();
                this.hint.setIcon(rLottieDrawable);
                this.hint.show();
            }
            return;
        } else {
            i2 = this.isVideo ? R.string.TimerPeriodVideoSetOnce : R.string.TimerPeriodPhotoSetOnce;
        }
        replaceTags = LocaleController.getString(i2);
        this.hint.setMaxWidthPx(getMeasuredWidth());
        this.hint.setMultilineText(false);
        this.hint.setInnerPadding(13, 4, 10, 4);
        this.hint.setIconMargin(0);
        this.hint.setIconTranslate(0.0f, -AndroidUtilities.dp(1.0f));
        this.hint.setTranslationY((-Math.min(AndroidUtilities.dp(34.0f), getEditTextHeight())) - AndroidUtilities.dp(14.0f));
        this.hint.setText(replaceTags);
        if (i <= 0) {
        }
        RLottieDrawable rLottieDrawable2 = new RLottieDrawable(i3, "" + i3, AndroidUtilities.dp(34.0f), AndroidUtilities.dp(34.0f));
        rLottieDrawable2.start();
        this.hint.setIcon(rLottieDrawable2);
        this.hint.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0075 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$new$1(FrameLayout frameLayout, View view) {
        int[] iArr;
        String formatPluralString;
        int i;
        ItemOptions itemOptions = this.timerPopup;
        if (itemOptions != null && itemOptions.isShown()) {
            this.timerPopup.dismiss();
            this.timerPopup = null;
            return;
        }
        this.hint.hide();
        ItemOptions makeOptions = ItemOptions.makeOptions(frameLayout, new DarkThemeResourceProvider(), this.timerButton);
        this.timerPopup = makeOptions;
        makeOptions.setDimAlpha(0);
        this.timerPopup.addText(LocaleController.getString(R.string.TimerPeriodHint), 13, AndroidUtilities.dp(200.0f));
        this.timerPopup.addGap();
        for (final int i2 : this.values) {
            if (i2 == 0) {
                i = R.string.TimerPeriodDoNotDelete;
            } else if (i2 == Integer.MAX_VALUE) {
                i = R.string.TimerPeriodOnce;
            } else {
                formatPluralString = LocaleController.formatPluralString("Seconds", i2, new Object[0]);
                this.timerPopup.add(0, formatPluralString, new Runnable() { // from class: org.telegram.ui.Components.CaptionPhotoViewer$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        CaptionPhotoViewer.this.lambda$new$0(i2);
                    }
                });
                if (this.timer != i2) {
                    this.timerPopup.putCheck();
                }
            }
            formatPluralString = LocaleController.getString(i);
            this.timerPopup.add(0, formatPluralString, new Runnable() { // from class: org.telegram.ui.Components.CaptionPhotoViewer$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    CaptionPhotoViewer.this.lambda$new$0(i2);
                }
            });
            if (this.timer != i2) {
            }
        }
        this.timerPopup.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAddPhotoVisible$2(boolean z) {
        if (z) {
            return;
        }
        this.timerButton.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setTimerVisible$3(boolean z) {
        if (z) {
            return;
        }
        this.timerButton.setVisibility(8);
    }

    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    protected int additionalKeyboardHeight() {
        return 0;
    }

    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    protected void afterUpdateShownKeyboard(boolean z) {
        int i = 0;
        this.timerButton.setVisibility((z || !this.timerVisible) ? 8 : 0);
        this.addPhotoButton.setVisibility((z || !this.addPhotoVisible) ? 8 : 8);
        if (z) {
            this.timerButton.setVisibility(8);
            this.addPhotoButton.setVisibility(8);
        }
    }

    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    protected void beforeUpdateShownKeyboard(boolean z) {
        if (!z) {
            this.timerButton.setVisibility(this.timerVisible ? 0 : 8);
            this.addPhotoButton.setVisibility(this.addPhotoVisible ? 0 : 8);
        }
        HintView2 hintView2 = this.hint;
        if (hintView2 != null) {
            hintView2.hide();
        }
    }

    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    protected boolean clipChild(View view) {
        return view != this.hint;
    }

    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    protected int getCaptionDefaultLimit() {
        return MessagesController.getInstance(this.currentAccount).captionLengthLimitDefault;
    }

    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    protected int getCaptionLimit() {
        return UserConfig.getInstance(this.currentAccount).isPremium() ? getCaptionPremiumLimit() : getCaptionDefaultLimit();
    }

    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    protected int getCaptionPremiumLimit() {
        return MessagesController.getInstance(this.currentAccount).captionLengthLimitPremium;
    }

    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    protected int getEditTextLeft() {
        if (this.addPhotoVisible) {
            return AndroidUtilities.dp(31.0f);
        }
        return 0;
    }

    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    protected int getEditTextStyle() {
        return 3;
    }

    public boolean hasTimer() {
        return this.timerVisible && this.timer > 0;
    }

    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    protected void onEditHeightChange(int i) {
        this.hint.setTranslationY((-Math.min(AndroidUtilities.dp(34.0f), i)) - AndroidUtilities.dp(10.0f));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    public void onTextChange() {
        Runnable runnable = this.applyCaption;
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    public void onUpdateShowKeyboard(float f) {
        float f2 = 1.0f - f;
        this.timerButton.setAlpha(f2);
        this.addPhotoButton.setAlpha(f2);
    }

    public void setAddPhotoVisible(final boolean z, boolean z2) {
        this.addPhotoVisible = z;
        this.addPhotoButton.animate().cancel();
        int i = 0;
        if (z2) {
            this.addPhotoButton.setVisibility(0);
            this.addPhotoButton.animate().alpha(z ? 1.0f : 0.0f).translationX(z ? 0.0f : AndroidUtilities.dp(-8.0f)).withEndAction(new Runnable() { // from class: org.telegram.ui.Components.CaptionPhotoViewer$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    CaptionPhotoViewer.this.lambda$setAddPhotoVisible$2(z);
                }
            }).start();
        } else {
            this.addPhotoButton.setVisibility(z ? 0 : 8);
            this.addPhotoButton.setAlpha(z ? 1.0f : 0.0f);
            this.addPhotoButton.setTranslationX(z ? 0.0f : AndroidUtilities.dp(-8.0f));
        }
        updateEditTextLeft();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.editText.getLayoutParams();
        if (this.addPhotoVisible && this.timerVisible) {
            i = 33;
        }
        marginLayoutParams.rightMargin = AndroidUtilities.dp(12 + i);
        this.editText.setLayoutParams(marginLayoutParams);
    }

    public void setIsVideo(boolean z) {
        this.isVideo = z;
    }

    public void setOnAddPhotoClick(View.OnClickListener onClickListener) {
        this.addPhotoButton.setOnClickListener(onClickListener);
    }

    public void setOnTimerChange(Utilities.Callback<Integer> callback) {
        this.onTTLChange = callback;
    }

    public void setTimer(int i) {
        this.timer = i;
        this.timerDrawable.setValue(i == Integer.MAX_VALUE ? 1 : Math.max(1, i), this.timer > 0, true);
        HintView2 hintView2 = this.hint;
        if (hintView2 != null) {
            hintView2.hide();
        }
    }

    public void setTimerVisible(final boolean z, boolean z2) {
        this.timerVisible = z;
        this.timerButton.animate().cancel();
        int i = 0;
        if (z2) {
            this.timerButton.setVisibility(0);
            this.timerButton.animate().alpha(z ? 1.0f : 0.0f).translationX(z ? 0.0f : AndroidUtilities.dp(8.0f)).withEndAction(new Runnable() { // from class: org.telegram.ui.Components.CaptionPhotoViewer$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    CaptionPhotoViewer.this.lambda$setTimerVisible$3(z);
                }
            }).start();
        } else {
            this.timerButton.setVisibility(z ? 0 : 8);
            this.timerButton.setAlpha(z ? 1.0f : 0.0f);
            this.timerButton.setTranslationX(z ? 0.0f : AndroidUtilities.dp(8.0f));
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.editText.getLayoutParams();
        if (this.addPhotoVisible && this.timerVisible) {
            i = 33;
        }
        marginLayoutParams.rightMargin = AndroidUtilities.dp(12 + i);
        this.editText.setLayoutParams(marginLayoutParams);
    }

    @Override // org.telegram.ui.Stories.recorder.CaptionContainerView
    public void updateColors(Theme.ResourcesProvider resourcesProvider) {
        super.updateColors(resourcesProvider);
        this.timerDrawable.updateColors(-1, Theme.getColor(Theme.key_chat_editMediaButton, resourcesProvider), -1);
    }
}
