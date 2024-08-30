package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
/* loaded from: classes3.dex */
public class ScrimOptions extends Dialog {
    private Bitmap blurBitmap;
    private Paint blurBitmapPaint;
    private BitmapShader blurBitmapShader;
    private Matrix blurMatrix;
    private final FrameLayout containerView;
    public final Context context;
    public final int currentAccount;
    private boolean dismissing;
    private final android.graphics.Rect insets;
    private boolean isGroup;
    private ValueAnimator openAnimator;
    private float openProgress;
    private ItemOptions options;
    private FrameLayout optionsContainer;
    private View optionsView;
    public final Theme.ResourcesProvider resourcesProvider;
    private ChatMessageCell scrimCell;
    private Drawable scrimDrawable;
    private float scrimDrawableSh;
    private float scrimDrawableSw;
    private float scrimDrawableTx1;
    private float scrimDrawableTx2;
    private float scrimDrawableTy1;
    private float scrimDrawableTy2;
    private final FrameLayout windowView;

    public ScrimOptions(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, R.style.TransparentDialog);
        this.currentAccount = UserConfig.selectedAccount;
        this.insets = new android.graphics.Rect();
        this.scrimDrawableSw = 1.0f;
        this.scrimDrawableSh = 1.0f;
        this.dismissing = false;
        this.context = context;
        this.resourcesProvider = resourcesProvider;
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.ScrimOptions.1
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                if (ScrimOptions.this.openProgress > 0.0f && ScrimOptions.this.blurBitmapPaint != null) {
                    ScrimOptions.this.blurMatrix.reset();
                    float width = getWidth() / ScrimOptions.this.blurBitmap.getWidth();
                    ScrimOptions.this.blurMatrix.postScale(width, width);
                    ScrimOptions.this.blurBitmapShader.setLocalMatrix(ScrimOptions.this.blurMatrix);
                    ScrimOptions.this.blurBitmapPaint.setAlpha((int) (ScrimOptions.this.openProgress * 255.0f));
                    canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), ScrimOptions.this.blurBitmapPaint);
                }
                super.dispatchDraw(canvas);
                if (ScrimOptions.this.scrimDrawable != null) {
                    ScrimOptions.this.scrimDrawable.setAlpha((int) (ScrimOptions.this.openProgress * 255.0f));
                    canvas.save();
                    canvas.translate(ScrimOptions.this.scrimDrawableTx2 + (ScrimOptions.this.scrimDrawableTx1 * ScrimOptions.this.openProgress), ScrimOptions.this.scrimDrawableTy2 + (ScrimOptions.this.scrimDrawableTy1 * ScrimOptions.this.openProgress));
                    float lerp = AndroidUtilities.lerp(AndroidUtilities.lerp(Math.min(ScrimOptions.this.scrimDrawableSw, ScrimOptions.this.scrimDrawableSh), Math.max(ScrimOptions.this.scrimDrawableSw, ScrimOptions.this.scrimDrawableSh), 0.75f), 1.0f, ScrimOptions.this.openProgress);
                    canvas.scale(lerp, lerp, (-ScrimOptions.this.scrimDrawableTx2) + ScrimOptions.this.scrimDrawable.getBounds().left + ((ScrimOptions.this.scrimDrawable.getBounds().width() / 2.0f) * ScrimOptions.this.scrimDrawableSw), (-ScrimOptions.this.scrimDrawableTy2) + ScrimOptions.this.scrimDrawable.getBounds().top + ((ScrimOptions.this.scrimDrawable.getBounds().height() / 2.0f) * ScrimOptions.this.scrimDrawableSh));
                    ScrimOptions.this.scrimDrawable.draw(canvas);
                    canvas.restore();
                }
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == 4 && keyEvent.getAction() == 1) {
                    ScrimOptions.this.onBackPressed();
                    return true;
                }
                return super.dispatchKeyEventPreIme(keyEvent);
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
                super.onLayout(z, i, i2, i3, i4);
                ScrimOptions.this.layout();
            }
        };
        this.windowView = frameLayout;
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScrimOptions.this.lambda$new$0(view);
            }
        });
        SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context);
        this.containerView = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.setClipToPadding(false);
        frameLayout.addView(sizeNotifierFrameLayout, LayoutHelper.createFrame(-1, -1, 119));
        if (Build.VERSION.SDK_INT >= 21) {
            frameLayout.setFitsSystemWindows(true);
            frameLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.Components.ScrimOptions.2
                @Override // android.view.View.OnApplyWindowInsetsListener
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    int systemWindowInsetLeft;
                    int systemWindowInsetTop;
                    int systemWindowInsetRight;
                    int systemWindowInsetBottom;
                    WindowInsets consumeSystemWindowInsets;
                    WindowInsets windowInsets2;
                    Insets insets;
                    int i;
                    int i2;
                    int i3;
                    int i4;
                    int i5 = Build.VERSION.SDK_INT;
                    if (i5 >= 30) {
                        insets = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout() | WindowInsetsCompat.Type.systemBars());
                        android.graphics.Rect rect = ScrimOptions.this.insets;
                        i = insets.left;
                        i2 = insets.top;
                        i3 = insets.right;
                        i4 = insets.bottom;
                        rect.set(i, i2, i3, i4);
                    } else {
                        android.graphics.Rect rect2 = ScrimOptions.this.insets;
                        systemWindowInsetLeft = windowInsets.getSystemWindowInsetLeft();
                        systemWindowInsetTop = windowInsets.getSystemWindowInsetTop();
                        systemWindowInsetRight = windowInsets.getSystemWindowInsetRight();
                        systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
                        rect2.set(systemWindowInsetLeft, systemWindowInsetTop, systemWindowInsetRight, systemWindowInsetBottom);
                    }
                    ScrimOptions.this.containerView.setPadding(ScrimOptions.this.insets.left, ScrimOptions.this.insets.top, ScrimOptions.this.insets.right, ScrimOptions.this.insets.bottom);
                    ScrimOptions.this.windowView.requestLayout();
                    if (i5 >= 30) {
                        windowInsets2 = WindowInsets.CONSUMED;
                        return windowInsets2;
                    }
                    consumeSystemWindowInsets = windowInsets.consumeSystemWindowInsets();
                    return consumeSystemWindowInsets;
                }
            });
        }
    }

    private void animateOpenTo(final boolean z, float f, final Runnable runnable) {
        ValueAnimator valueAnimator = this.openAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.openProgress, z ? 1.0f : 0.0f);
        this.openAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda9
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ScrimOptions.this.lambda$animateOpenTo$5(valueAnimator2);
            }
        });
        this.openAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ScrimOptions.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ScrimOptions.this.openProgress = z ? 1.0f : 0.0f;
                ScrimOptions.this.optionsView.setScaleX(AndroidUtilities.lerp(0.8f, 1.0f, ScrimOptions.this.openProgress));
                ScrimOptions.this.optionsView.setScaleY(AndroidUtilities.lerp(0.8f, 1.0f, ScrimOptions.this.openProgress));
                ScrimOptions.this.optionsView.setAlpha(ScrimOptions.this.openProgress);
                ScrimOptions.this.windowView.invalidate();
                ScrimOptions.this.containerView.invalidate();
                Runnable runnable2 = runnable;
                if (runnable2 != null) {
                    AndroidUtilities.runOnUIThread(runnable2);
                }
            }
        });
        this.openAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.openAnimator.setDuration(350L);
        this.openAnimator.start();
    }

    private void animateOpenTo(boolean z, Runnable runnable) {
        animateOpenTo(z, 1.0f, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateOpenTo$5(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.openProgress = floatValue;
        this.optionsView.setScaleX(AndroidUtilities.lerp(0.8f, 1.0f, floatValue));
        this.optionsView.setScaleY(AndroidUtilities.lerp(0.8f, 1.0f, this.openProgress));
        this.optionsView.setAlpha(this.openProgress);
        this.windowView.invalidate();
        this.containerView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$1() {
        super.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$2() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                ScrimOptions.this.lambda$dismiss$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissFast$3() {
        super.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissFast$4() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                ScrimOptions.this.lambda$dismissFast$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareBlur$6(View view, Bitmap bitmap) {
        if (view != null) {
            view.setVisibility(0);
        }
        this.blurBitmap = bitmap;
        Paint paint = new Paint(1);
        this.blurBitmapPaint = paint;
        Bitmap bitmap2 = this.blurBitmap;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(bitmap2, tileMode, tileMode);
        this.blurBitmapShader = bitmapShader;
        paint.setShader(bitmapShader);
        ColorMatrix colorMatrix = new ColorMatrix();
        AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, Theme.isCurrentThemeDark() ? 0.08f : 0.25f);
        AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, Theme.isCurrentThemeDark() ? -0.02f : -0.07f);
        this.blurBitmapPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        this.blurMatrix = new Matrix();
    }

    private void prepareBlur(final View view) {
        if (view != null) {
            view.setVisibility(4);
        }
        AndroidUtilities.makeGlobalBlurBitmap(new Utilities.Callback() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda7
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                ScrimOptions.this.lambda$prepareBlur$6(view, (Bitmap) obj);
            }
        }, 14.0f);
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (this.dismissing) {
            return;
        }
        this.dismissing = true;
        animateOpenTo(false, new Runnable() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                ScrimOptions.this.lambda$dismiss$2();
            }
        });
        this.windowView.invalidate();
    }

    public void dismissFast() {
        if (this.dismissing) {
            return;
        }
        this.dismissing = true;
        animateOpenTo(false, 2.0f, new Runnable() { // from class: org.telegram.ui.Components.ScrimOptions$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ScrimOptions.this.lambda$dismissFast$4();
            }
        });
        this.windowView.invalidate();
    }

    @Override // android.app.Dialog
    public boolean isShowing() {
        return !this.dismissing;
    }

    public void layout() {
        boolean z;
        Drawable drawable = this.scrimDrawable;
        if (drawable != null) {
            android.graphics.Rect bounds = drawable.getBounds();
            FrameLayout frameLayout = this.optionsContainer;
            if (frameLayout != null) {
                float f = this.scrimDrawableTx2;
                float f2 = bounds.left + f;
                float f3 = bounds.right + f;
                float f4 = this.scrimDrawableTy2;
                float f5 = bounds.top + f4;
                float f6 = bounds.bottom + f4;
                boolean z2 = true;
                if (f3 - frameLayout.getMeasuredWidth() < AndroidUtilities.dp(8.0f)) {
                    this.optionsView.setPivotX(AndroidUtilities.dp(6.0f));
                    this.optionsContainer.setX(Math.min(this.containerView.getWidth() - this.optionsContainer.getWidth(), f2 - AndroidUtilities.dp(10.0f)) - this.containerView.getX());
                    z = false;
                } else {
                    View view = this.optionsView;
                    view.setPivotX(view.getMeasuredWidth() - AndroidUtilities.dp(6.0f));
                    this.optionsContainer.setX(Math.max(AndroidUtilities.dp(8.0f), (AndroidUtilities.dp(4.0f) + f3) - this.optionsContainer.getMeasuredWidth()) - this.containerView.getX());
                    z = true;
                }
                this.scrimDrawableTx1 = z ? ((this.optionsContainer.getX() + this.optionsContainer.getWidth()) - AndroidUtilities.dp(6.0f)) - f3 : (this.optionsContainer.getX() + AndroidUtilities.dp(10.0f)) - f2;
                this.scrimDrawableTy1 = 0.0f;
                if (this.optionsContainer.getMeasuredHeight() + f6 > this.windowView.getMeasuredHeight() - AndroidUtilities.dp(16.0f)) {
                    View view2 = this.optionsView;
                    view2.setPivotY(view2.getMeasuredHeight() - AndroidUtilities.dp(6.0f));
                    this.optionsContainer.setY(((f5 - AndroidUtilities.dp(4.0f)) - this.optionsContainer.getMeasuredHeight()) - this.containerView.getY());
                } else {
                    this.optionsView.setPivotY(AndroidUtilities.dp(6.0f));
                    this.optionsContainer.setY(Math.min((this.windowView.getHeight() - this.optionsContainer.getMeasuredHeight()) - AndroidUtilities.dp(16.0f), f6) - this.containerView.getY());
                    z2 = false;
                }
                this.options.setSwipebackGravity(z, z2);
            }
        }
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogNoAnimation);
        setContentView(this.windowView, new ViewGroup.LayoutParams(-1, -1));
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.height = -1;
        attributes.gravity = 119;
        attributes.dimAmount = 0.0f;
        int i = attributes.flags & (-3);
        attributes.softInputMode = 16;
        attributes.flags = 131072 | i;
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 21) {
            attributes.flags = i | (-1945960192);
        }
        attributes.flags |= 1152;
        if (i2 >= 28) {
            attributes.layoutInDisplayCutoutMode = 1;
        }
        window.setAttributes(attributes);
        this.windowView.setSystemUiVisibility(256);
        AndroidUtilities.setLightNavigationBar(this.windowView, !Theme.isCurrentThemeDark());
    }

    public void setItemOptions(ItemOptions itemOptions) {
        this.options = itemOptions;
        this.optionsView = itemOptions.getLayout();
        FrameLayout frameLayout = new FrameLayout(this.context);
        this.optionsContainer = frameLayout;
        frameLayout.addView(this.optionsView, LayoutHelper.createFrame(-2, -2.0f));
        this.containerView.addView(this.optionsContainer, LayoutHelper.createFrame(-2, -2.0f));
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x035b  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0371  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x03d6  */
    /* JADX WARN: Removed duplicated region for block: B:131:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x02f5  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x031a  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0329  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x034a  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0354  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setScrim(final ChatMessageCell chatMessageCell, CharacterStyle characterStyle, CharSequence charSequence) {
        float f;
        float f2;
        ArrayList<MessageObject.TextLayoutBlock> arrayList;
        float f3;
        int i;
        StaticLayout staticLayout;
        int i2;
        int i3;
        float f4;
        float f5;
        RectF rectF;
        StaticLayout staticLayout2;
        final Bitmap bitmap;
        int i4;
        SpannableStringBuilder spannableStringBuilder;
        int i5;
        int endHyphenEdit;
        String fontVariationSettings;
        float letterSpacing;
        String fontFeatureSettings;
        boolean isElegantTextHeight;
        CharacterStyle[] characterStyleArr;
        int i6;
        if (chatMessageCell == null) {
            return;
        }
        this.scrimCell = chatMessageCell;
        this.isGroup = chatMessageCell.getCurrentMessagesGroup() != null;
        MessageObject messageObject = chatMessageCell.getMessageObject();
        if (chatMessageCell.getCaptionLayout() != null) {
            f = chatMessageCell.getCaptionX();
            f2 = chatMessageCell.getCaptionY();
            arrayList = chatMessageCell.getCaptionLayout().textLayoutBlocks;
            f3 = chatMessageCell.getCaptionLayout().textXOffset;
        } else {
            f = 0.0f;
            f2 = 0.0f;
            arrayList = null;
            f3 = 0.0f;
        }
        if (arrayList == null) {
            f = chatMessageCell.getTextX();
            f2 = chatMessageCell.getTextY() + chatMessageCell.transitionYOffsetForDrawables;
            arrayList = messageObject.textLayoutBlocks;
            f3 = messageObject.textXOffset;
        }
        if (arrayList == null) {
            return;
        }
        int i7 = -1;
        int i8 = 0;
        int i9 = 0;
        StaticLayout staticLayout3 = null;
        int i10 = 0;
        for (int i11 = 0; i11 < arrayList.size(); i11++) {
            MessageObject.TextLayoutBlock textLayoutBlock = arrayList.get(i11);
            StaticLayout staticLayout4 = textLayoutBlock.textLayout;
            if (staticLayout4 != null && (staticLayout4.getText() instanceof Spanned)) {
                i6 = i8;
                CharacterStyle[] characterStyleArr2 = (CharacterStyle[]) ((Spanned) staticLayout4.getText()).getSpans(0, staticLayout4.getText().length(), CharacterStyle.class);
                if (characterStyleArr2 != null) {
                    for (CharacterStyle characterStyle2 : characterStyleArr2) {
                        if (characterStyle2 == characterStyle) {
                            i8 = ((Spanned) staticLayout4.getText()).getSpanStart(characterStyle);
                            i9 = ((Spanned) staticLayout4.getText()).getSpanEnd(characterStyle);
                            f += textLayoutBlock.isRtl() ? (int) Math.ceil(f3) : 0;
                            f2 += textLayoutBlock.padTop + textLayoutBlock.textYOffset(arrayList, chatMessageCell.transitionParams);
                            i10 = textLayoutBlock.originalWidth;
                            i7 = i11;
                            staticLayout3 = staticLayout4;
                        }
                    }
                }
            } else {
                i6 = i8;
            }
            i8 = i6;
        }
        int i12 = i8;
        if (i7 != -1 || chatMessageCell.getDescriptionlayout() == null) {
            i = i9;
            staticLayout = staticLayout3;
            i2 = i10;
            i3 = i12;
        } else {
            StaticLayout descriptionlayout = chatMessageCell.getDescriptionlayout();
            i3 = i12;
            for (int i13 = 0; i13 == 0; i13++) {
                if (descriptionlayout != null && (descriptionlayout.getText() instanceof Spanned) && (characterStyleArr = (CharacterStyle[]) ((Spanned) descriptionlayout.getText()).getSpans(0, descriptionlayout.getText().length(), CharacterStyle.class)) != null) {
                    int i14 = 0;
                    while (true) {
                        if (i14 >= characterStyleArr.length) {
                            break;
                        } else if (characterStyleArr[i14] == characterStyle) {
                            i3 = ((Spanned) descriptionlayout.getText()).getSpanStart(characterStyle);
                            staticLayout3 = descriptionlayout;
                            i9 = ((Spanned) descriptionlayout.getText()).getSpanEnd(characterStyle);
                            f = chatMessageCell.getDescriptionLayoutX();
                            f2 = chatMessageCell.getDescriptionLayoutY();
                            i10 = descriptionlayout.getWidth();
                            break;
                        } else {
                            i14++;
                        }
                    }
                }
            }
            i = i9;
            staticLayout = staticLayout3;
            i2 = i10;
        }
        if (staticLayout == null) {
            return;
        }
        if (charSequence != null) {
            int lineForOffset = staticLayout.getLineForOffset(i3);
            float lineTop = f2 + staticLayout.getLineTop(lineForOffset);
            float primaryHorizontal = staticLayout.getPrimaryHorizontal(i3);
            float lineWidth = staticLayout.getLineWidth(lineForOffset);
            LinkPath linkPath = new LinkPath(true);
            linkPath.setCurrentLayout(staticLayout, i3, 0.0f);
            staticLayout.getSelectionPath(i3, i, linkPath);
            RectF rectF2 = new RectF();
            linkPath.computeBounds(rectF2, true);
            StaticLayout makeStaticLayout = MessageObject.makeStaticLayout(charSequence, staticLayout.getPaint(), staticLayout.getWidth(), 1.0f, 0.0f, false);
            int length = charSequence.length();
            float width = makeStaticLayout.getWidth();
            float f6 = 0.0f;
            for (int i15 = 0; i15 < makeStaticLayout.getLineCount(); i15++) {
                width = Math.min(width, makeStaticLayout.getLineLeft(i15));
                f6 = Math.max(f6, makeStaticLayout.getLineRight(i15));
            }
            f4 = f + Math.max(0.0f, Math.min(primaryHorizontal, lineWidth - Math.max(0.0f, f6 - width)));
            f5 = lineTop;
            i3 = 0;
            rectF = rectF2;
            i = length;
            staticLayout2 = makeStaticLayout;
        } else {
            f4 = f;
            f5 = f2;
            rectF = null;
            staticLayout2 = staticLayout;
        }
        final Paint paint = new Paint(1);
        paint.setColor(Theme.getColor(messageObject.isOutOwner() ? Theme.key_chat_outBubble : Theme.key_chat_inBubble, this.resourcesProvider));
        paint.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(5.0f)));
        final LinkPath linkPath2 = new LinkPath(true);
        linkPath2.setUseCornerPathImplementation(true);
        linkPath2.setCurrentLayout(staticLayout2, i3, 0.0f);
        staticLayout2.getSelectionPath(i3, i, linkPath2);
        linkPath2.closeRects();
        final RectF rectF3 = new RectF();
        linkPath2.computeBounds(rectF3, true);
        int width2 = (int) (rectF3.width() + LinkPath.getRadius());
        if (chatMessageCell.drawBackgroundInParent() && width2 > 0) {
            if (rectF3.height() > 0.0f) {
                Bitmap createBitmap = Bitmap.createBitmap(width2, (int) rectF3.height(), Bitmap.Config.ALPHA_8);
                Canvas canvas = new Canvas(createBitmap);
                Paint paint2 = new Paint(1);
                paint2.setColor(-1);
                canvas.drawRect(0.0f, 0.0f, width2, rectF3.height(), paint2);
                Paint paint3 = new Paint(1);
                paint3.setColor(-1);
                paint3.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(5.0f)));
                paint3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.translate(-rectF3.left, -rectF3.top);
                canvas.drawPath(linkPath2, paint3);
                bitmap = createBitmap;
                final Paint paint4 = new Paint(3);
                paint4.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                chatMessageCell.setupTextColors();
                TextPaint textPaint = new TextPaint(1);
                textPaint.setColor(staticLayout2.getPaint().getColor());
                textPaint.linkColor = staticLayout2.getPaint().linkColor;
                textPaint.setTextSize(staticLayout2.getPaint().getTextSize());
                textPaint.setTextAlign(staticLayout2.getPaint().getTextAlign());
                textPaint.setTypeface(staticLayout2.getPaint().getTypeface());
                textPaint.setLinearText(staticLayout2.getPaint().isLinearText());
                i4 = Build.VERSION.SDK_INT;
                RectF rectF4 = rectF;
                if (i4 >= 21) {
                    letterSpacing = staticLayout2.getPaint().getLetterSpacing();
                    textPaint.setLetterSpacing(letterSpacing);
                    fontFeatureSettings = staticLayout2.getPaint().getFontFeatureSettings();
                    textPaint.setFontFeatureSettings(fontFeatureSettings);
                    isElegantTextHeight = staticLayout2.getPaint().isElegantTextHeight();
                    textPaint.setElegantTextHeight(isElegantTextHeight);
                }
                if (i4 >= 26) {
                    fontVariationSettings = staticLayout2.getPaint().getFontVariationSettings();
                    textPaint.setFontVariationSettings(fontVariationSettings);
                }
                if (i4 >= 29) {
                    endHyphenEdit = staticLayout2.getPaint().getEndHyphenEdit();
                    textPaint.setEndHyphenEdit(endHyphenEdit);
                }
                spannableStringBuilder = new SpannableStringBuilder(AnimatedEmojiSpan.cloneSpans(staticLayout2.getText(), -1, textPaint.getFontMetricsInt()));
                if (i3 <= 0) {
                    i5 = 0;
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(0), 0, i3, 33);
                } else {
                    i5 = 0;
                }
                if (i < spannableStringBuilder.length()) {
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(i5), i, spannableStringBuilder.length(), 33);
                }
                final StaticLayout makeStaticLayout2 = MessageObject.makeStaticLayout(spannableStringBuilder, textPaint, i2, 1.0f, messageObject.totalAnimatedEmojiCount < 4 ? -1.0f : 0.0f, false);
                final int[] iArr = new int[2];
                chatMessageCell.getLocationOnScreen(iArr);
                final int[] iArr2 = {iArr[0] + ((int) f4), iArr[1] + ((int) f5)};
                this.scrimDrawable = new Drawable() { // from class: org.telegram.ui.Components.ScrimOptions.4
                    private int alpha = NotificationCenter.voipServiceCreated;

                    @Override // android.graphics.drawable.Drawable
                    public void draw(Canvas canvas2) {
                        if (this.alpha <= 0) {
                            return;
                        }
                        RectF rectF5 = AndroidUtilities.rectTmp;
                        rectF5.set(getBounds());
                        rectF5.left -= LinkPath.getRadius() / 2.0f;
                        canvas2.save();
                        canvas2.saveLayerAlpha(rectF5, this.alpha, 31);
                        int[] iArr3 = iArr2;
                        canvas2.translate(iArr3[0], iArr3[1]);
                        ChatMessageCell chatMessageCell2 = chatMessageCell;
                        if (chatMessageCell2 == null || !chatMessageCell2.drawBackgroundInParent()) {
                            canvas2.drawPath(linkPath2, paint);
                        } else {
                            Theme.MessageDrawable messageDrawable = chatMessageCell.currentBackgroundDrawable;
                            if (messageDrawable == null || messageDrawable.getPaint() == null) {
                                int[] iArr4 = iArr2;
                                canvas2.translate(-iArr4[0], -iArr4[1]);
                                int[] iArr5 = iArr;
                                canvas2.translate(iArr5[0], iArr5[1]);
                                chatMessageCell.drawBackgroundInternal(canvas2, true);
                                int[] iArr6 = iArr;
                                canvas2.translate(-iArr6[0], -iArr6[1]);
                                int[] iArr7 = iArr2;
                                canvas2.translate(iArr7[0], iArr7[1]);
                            } else {
                                canvas2.save();
                                canvas2.translate(0.0f, -chatMessageCell.currentBackgroundDrawable.getTopY());
                                canvas2.drawPaint(chatMessageCell.currentBackgroundDrawable.getPaint());
                                canvas2.restore();
                            }
                            if (bitmap != null) {
                                canvas2.save();
                                Bitmap bitmap2 = bitmap;
                                RectF rectF6 = rectF3;
                                canvas2.drawBitmap(bitmap2, rectF6.left, rectF6.top, paint4);
                                canvas2.restore();
                            }
                        }
                        canvas2.clipPath(linkPath2);
                        makeStaticLayout2.draw(canvas2);
                        canvas2.restore();
                    }

                    @Override // android.graphics.drawable.Drawable
                    public int getOpacity() {
                        return -2;
                    }

                    @Override // android.graphics.drawable.Drawable
                    public void setAlpha(int i16) {
                        this.alpha = i16;
                    }

                    @Override // android.graphics.drawable.Drawable
                    public void setColorFilter(ColorFilter colorFilter) {
                    }
                };
                int radius = (int) (iArr[0] + f4 + rectF3.left + (LinkPath.getRadius() / 2.0f));
                int i16 = (int) (iArr[1] + f5 + rectF3.top);
                this.scrimDrawable.setBounds(radius, i16, ((int) rectF3.width()) + radius, ((int) rectF3.height()) + i16);
                if (charSequence == null) {
                    float f7 = radius;
                    if (rectF3.width() + f7 > AndroidUtilities.displaySize.x - AndroidUtilities.dp(8.0f)) {
                        this.scrimDrawableTx2 -= (f7 + rectF3.width()) - (AndroidUtilities.displaySize.x - AndroidUtilities.dp(8.0f));
                    }
                    float f8 = i16;
                    if (rectF3.height() + f8 > ((AndroidUtilities.displaySize.y - AndroidUtilities.statusBarHeight) - AndroidUtilities.navigationBarHeight) - AndroidUtilities.dp(8.0f)) {
                        this.scrimDrawableTy2 -= (f8 + rectF3.height()) - (((AndroidUtilities.displaySize.y - AndroidUtilities.statusBarHeight) - AndroidUtilities.navigationBarHeight) - AndroidUtilities.dp(8.0f));
                    }
                    if (rectF4 != null) {
                        this.scrimDrawableSw = rectF4.width() / rectF3.width();
                        this.scrimDrawableSh = rectF4.height() / rectF3.height();
                        return;
                    }
                    return;
                }
                return;
            }
        }
        bitmap = null;
        final Paint paint42 = new Paint(3);
        paint42.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        chatMessageCell.setupTextColors();
        TextPaint textPaint2 = new TextPaint(1);
        textPaint2.setColor(staticLayout2.getPaint().getColor());
        textPaint2.linkColor = staticLayout2.getPaint().linkColor;
        textPaint2.setTextSize(staticLayout2.getPaint().getTextSize());
        textPaint2.setTextAlign(staticLayout2.getPaint().getTextAlign());
        textPaint2.setTypeface(staticLayout2.getPaint().getTypeface());
        textPaint2.setLinearText(staticLayout2.getPaint().isLinearText());
        i4 = Build.VERSION.SDK_INT;
        RectF rectF42 = rectF;
        if (i4 >= 21) {
        }
        if (i4 >= 26) {
        }
        if (i4 >= 29) {
        }
        spannableStringBuilder = new SpannableStringBuilder(AnimatedEmojiSpan.cloneSpans(staticLayout2.getText(), -1, textPaint2.getFontMetricsInt()));
        if (i3 <= 0) {
        }
        if (i < spannableStringBuilder.length()) {
        }
        final StaticLayout makeStaticLayout22 = MessageObject.makeStaticLayout(spannableStringBuilder, textPaint2, i2, 1.0f, messageObject.totalAnimatedEmojiCount < 4 ? -1.0f : 0.0f, false);
        final int[] iArr3 = new int[2];
        chatMessageCell.getLocationOnScreen(iArr3);
        final int[] iArr22 = {iArr3[0] + ((int) f4), iArr3[1] + ((int) f5)};
        this.scrimDrawable = new Drawable() { // from class: org.telegram.ui.Components.ScrimOptions.4
            private int alpha = NotificationCenter.voipServiceCreated;

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas2) {
                if (this.alpha <= 0) {
                    return;
                }
                RectF rectF5 = AndroidUtilities.rectTmp;
                rectF5.set(getBounds());
                rectF5.left -= LinkPath.getRadius() / 2.0f;
                canvas2.save();
                canvas2.saveLayerAlpha(rectF5, this.alpha, 31);
                int[] iArr32 = iArr22;
                canvas2.translate(iArr32[0], iArr32[1]);
                ChatMessageCell chatMessageCell2 = chatMessageCell;
                if (chatMessageCell2 == null || !chatMessageCell2.drawBackgroundInParent()) {
                    canvas2.drawPath(linkPath2, paint);
                } else {
                    Theme.MessageDrawable messageDrawable = chatMessageCell.currentBackgroundDrawable;
                    if (messageDrawable == null || messageDrawable.getPaint() == null) {
                        int[] iArr4 = iArr22;
                        canvas2.translate(-iArr4[0], -iArr4[1]);
                        int[] iArr5 = iArr3;
                        canvas2.translate(iArr5[0], iArr5[1]);
                        chatMessageCell.drawBackgroundInternal(canvas2, true);
                        int[] iArr6 = iArr3;
                        canvas2.translate(-iArr6[0], -iArr6[1]);
                        int[] iArr7 = iArr22;
                        canvas2.translate(iArr7[0], iArr7[1]);
                    } else {
                        canvas2.save();
                        canvas2.translate(0.0f, -chatMessageCell.currentBackgroundDrawable.getTopY());
                        canvas2.drawPaint(chatMessageCell.currentBackgroundDrawable.getPaint());
                        canvas2.restore();
                    }
                    if (bitmap != null) {
                        canvas2.save();
                        Bitmap bitmap2 = bitmap;
                        RectF rectF6 = rectF3;
                        canvas2.drawBitmap(bitmap2, rectF6.left, rectF6.top, paint42);
                        canvas2.restore();
                    }
                }
                canvas2.clipPath(linkPath2);
                makeStaticLayout22.draw(canvas2);
                canvas2.restore();
            }

            @Override // android.graphics.drawable.Drawable
            public int getOpacity() {
                return -2;
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int i162) {
                this.alpha = i162;
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }
        };
        int radius2 = (int) (iArr3[0] + f4 + rectF3.left + (LinkPath.getRadius() / 2.0f));
        int i162 = (int) (iArr3[1] + f5 + rectF3.top);
        this.scrimDrawable.setBounds(radius2, i162, ((int) rectF3.width()) + radius2, ((int) rectF3.height()) + i162);
        if (charSequence == null) {
        }
    }

    @Override // android.app.Dialog
    public void show() {
        if (AndroidUtilities.isSafeToShow(getContext())) {
            super.show();
            prepareBlur(null);
            animateOpenTo(true, null);
        }
    }
}
