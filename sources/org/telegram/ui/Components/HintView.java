package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.HintView;

/* loaded from: classes3.dex */
public class HintView extends FrameLayout {
    private AnimatorSet animatorSet;
    private ImageView arrowImageView;
    private int backgroundColor;
    Paint backgroundPaint;
    private int bottomOffset;
    private int currentType;
    private View currentView;
    private boolean drawPath;
    private float extraTranslationY;
    private boolean hasCloseButton;
    private Runnable hideRunnable;
    private ImageView imageView;
    private boolean isTopArrow;
    private ChatMessageCell messageCell;
    private String overrideText;
    Path path;
    private final Theme.ResourcesProvider resourcesProvider;
    private long showingDuration;
    private int shownY;
    public TextView textView;
    private float translationY;
    private boolean useScale;

    class 1 extends AnimatorListenerAdapter {
        1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            HintView.this.hide();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            HintView.this.animatorSet = null;
            if (HintView.this.hasCloseButton) {
                return;
            }
            AndroidUtilities.runOnUIThread(HintView.this.hideRunnable = new Runnable() { // from class: org.telegram.ui.Components.HintView$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    HintView.1.this.lambda$onAnimationEnd$0();
                }
            }, HintView.this.currentType == 0 ? 10000L : 2000L);
        }
    }

    class 2 extends AnimatorListenerAdapter {
        2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            HintView.this.hide();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            HintView.this.animatorSet = null;
            if (HintView.this.hasCloseButton) {
                return;
            }
            AndroidUtilities.runOnUIThread(HintView.this.hideRunnable = new Runnable() { // from class: org.telegram.ui.Components.HintView$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    HintView.2.this.lambda$onAnimationEnd$0();
                }
            }, HintView.this.showingDuration);
        }
    }

    public interface VisibilityListener {
    }

    public HintView(Context context, int i) {
        this(context, i, false, null);
    }

    public HintView(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        this(context, i, false, resourcesProvider);
    }

    public HintView(Context context, int i, boolean z) {
        this(context, i, z, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x008e, code lost:
    
        if (r12 != false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x00dd, code lost:
    
        r6 = r4;
        r3 = r5;
        r8 = 6.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00d9, code lost:
    
        r6 = r4;
        r3 = r5;
        r8 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00d7, code lost:
    
        if (r12 != false) goto L34;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public HintView(Context context, int i, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        TextView textView;
        float f;
        TextView textView2;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        this.showingDuration = 2000L;
        this.resourcesProvider = resourcesProvider;
        this.currentType = i;
        this.isTopArrow = z;
        CorrectlyMeasuringTextView correctlyMeasuringTextView = new CorrectlyMeasuringTextView(context);
        this.textView = correctlyMeasuringTextView;
        int i2 = Theme.key_chat_gifSaveHintText;
        correctlyMeasuringTextView.setTextColor(getThemedColor(i2));
        this.textView.setTextSize(1, 14.0f);
        this.textView.setMaxLines(2);
        if (i == 7 || i == 8 || i == 9) {
            textView = this.textView;
            f = 310.0f;
        } else if (i == 4) {
            textView = this.textView;
            f = 280.0f;
        } else {
            textView = this.textView;
            f = 250.0f;
        }
        textView.setMaxWidth(AndroidUtilities.dp(f));
        if (this.currentType == 3) {
            this.textView.setGravity(19);
            this.textView.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(5.0f), getThemedColor(Theme.key_chat_gifSaveHintBackground)));
            this.textView.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
            textView2 = this.textView;
            f2 = z ? 6.0f : 0.0f;
            f3 = 30.0f;
        } else {
            this.textView.setGravity(51);
            this.textView.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), getThemedColor(Theme.key_chat_gifSaveHintBackground)));
            this.textView.setPadding(AndroidUtilities.dp(this.currentType == 0 ? 54.0f : 8.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            textView2 = this.textView;
            f2 = z ? 6.0f : 0.0f;
            f3 = -2.0f;
        }
        addView(textView2, LayoutHelper.createFrame(-2, f5, 51, 0.0f, f4, 0.0f, f6));
        if (i == 0) {
            this.textView.setText(LocaleController.getString(R.string.AutoplayVideoInfo));
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setImageResource(R.drawable.tooltip_sound);
            this.imageView.setScaleType(ImageView.ScaleType.CENTER);
            this.imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(i2), PorterDuff.Mode.MULTIPLY));
            addView(this.imageView, LayoutHelper.createFrame(38, 34.0f, 51, 7.0f, 7.0f, 0.0f, 0.0f));
        }
        ImageView imageView2 = new ImageView(context);
        this.arrowImageView = imageView2;
        imageView2.setImageResource(z ? R.drawable.tooltip_arrow_up : R.drawable.tooltip_arrow);
        this.arrowImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_gifSaveHintBackground), PorterDuff.Mode.MULTIPLY));
        addView(this.arrowImageView, LayoutHelper.createFrame(14, 6.0f, (z ? 48 : 80) | 3, 0.0f, 0.0f, 0.0f, 0.0f));
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createCloseButton$0(View view) {
        hide(true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:54:0x013e, code lost:
    
        if (r1 < 0) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0141, code lost:
    
        r6 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x015e, code lost:
    
        if (r1 >= 0) goto L62;
     */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0177  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0189  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0193  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x012e  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x00b7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updatePosition(View view) {
        int measuredHeight;
        int dp;
        int i;
        int i2;
        int measuredWidth;
        View view2;
        int i3;
        float f;
        int measuredHeight2;
        int i4;
        int i5;
        int measuredWidth2;
        float measuredWidth3;
        int i6;
        measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x, Integer.MIN_VALUE));
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        int dp2 = iArr[1] - AndroidUtilities.dp(4.0f);
        int i7 = this.currentType;
        if (i7 == 4) {
            i = AndroidUtilities.dp(4.0f);
        } else {
            if (i7 == 6 && this.isTopArrow) {
                measuredHeight = view.getMeasuredHeight() + getMeasuredHeight();
                dp = AndroidUtilities.dp(10.0f);
            } else {
                if (i7 != 7 && (i7 != 8 || !this.isTopArrow)) {
                    if (i7 == 8) {
                        dp2 -= AndroidUtilities.dp(10.0f);
                    }
                    i2 = this.currentType;
                    int i8 = 0;
                    if (i2 == 8 || !this.isTopArrow) {
                        measuredWidth = i2 != 3 ? iArr[0] : iArr[0] + (view.getMeasuredWidth() / 2);
                    } else if (view instanceof SimpleTextView) {
                        SimpleTextView simpleTextView = (SimpleTextView) view;
                        Drawable rightDrawable = simpleTextView.getRightDrawable();
                        measuredWidth = (iArr[0] + (rightDrawable != null ? rightDrawable.getBounds().centerX() : simpleTextView.getTextWidth() / 2)) - AndroidUtilities.dp(8.0f);
                    } else {
                        measuredWidth = view instanceof TextView ? (iArr[0] + ((TextView) view).getMeasuredWidth()) - AndroidUtilities.dp(16.5f) : iArr[0];
                    }
                    view2 = (View) getParent();
                    view2.getLocationInWindow(iArr);
                    i3 = measuredWidth - iArr[0];
                    int i9 = (dp2 - iArr[1]) - this.bottomOffset;
                    int measuredWidth4 = view2.getMeasuredWidth();
                    if (this.isTopArrow || (i6 = this.currentType) == 6 || i6 == 7 || i6 == 8) {
                        f = this.extraTranslationY;
                        measuredHeight2 = i9 - getMeasuredHeight();
                    } else {
                        f = this.extraTranslationY;
                        measuredHeight2 = AndroidUtilities.dp(44.0f);
                    }
                    float f2 = measuredHeight2;
                    this.translationY = f2;
                    setTranslationY(f + f2);
                    if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                        i4 = 0;
                        i5 = 0;
                    } else {
                        i4 = ((ViewGroup.MarginLayoutParams) getLayoutParams()).leftMargin;
                        i5 = ((ViewGroup.MarginLayoutParams) getLayoutParams()).rightMargin;
                    }
                    if (this.currentType != 8 && !this.isTopArrow) {
                        i8 = (((measuredWidth4 - i4) - i5) - getMeasuredWidth()) / 2;
                    } else if (i3 <= view2.getMeasuredWidth() / 2) {
                        if (this.currentType == 3) {
                            measuredWidth2 = (int) (measuredWidth4 - (getMeasuredWidth() * 1.5f));
                        } else {
                            i8 = (measuredWidth4 - getMeasuredWidth()) - (i5 + i4);
                        }
                    } else if (this.currentType == 3) {
                        measuredWidth2 = (i3 - (getMeasuredWidth() / 2)) - this.arrowImageView.getMeasuredWidth();
                    }
                    setTranslationX(i8);
                    float measuredWidth5 = (i3 - (i4 + i8)) - (this.arrowImageView.getMeasuredWidth() / 2.0f);
                    if (this.currentType == 7) {
                        measuredWidth5 += AndroidUtilities.dp(2.0f);
                    }
                    this.arrowImageView.setTranslationX(measuredWidth5);
                    if (i3 <= view2.getMeasuredWidth() / 2) {
                        if (measuredWidth5 >= AndroidUtilities.dp(10.0f)) {
                            return;
                        }
                    } else if (measuredWidth5 > getMeasuredWidth() - AndroidUtilities.dp(24.0f)) {
                        measuredWidth3 = (measuredWidth5 - getMeasuredWidth()) + AndroidUtilities.dp(24.0f);
                        setTranslationX(measuredWidth3);
                        this.arrowImageView.setTranslationX(measuredWidth5 - measuredWidth3);
                    } else if (measuredWidth5 >= AndroidUtilities.dp(10.0f)) {
                        return;
                    }
                    measuredWidth3 = measuredWidth5 - AndroidUtilities.dp(10.0f);
                    setTranslationX(getTranslationX() + measuredWidth3);
                    this.arrowImageView.setTranslationX(measuredWidth5 - measuredWidth3);
                }
                measuredHeight = view.getMeasuredHeight() + getMeasuredHeight();
                dp = AndroidUtilities.dp(8.0f);
            }
            i = measuredHeight + dp;
        }
        dp2 += i;
        i2 = this.currentType;
        int i82 = 0;
        if (i2 == 8) {
        }
        if (i2 != 3) {
        }
        view2 = (View) getParent();
        view2.getLocationInWindow(iArr);
        i3 = measuredWidth - iArr[0];
        int i92 = (dp2 - iArr[1]) - this.bottomOffset;
        int measuredWidth42 = view2.getMeasuredWidth();
        if (this.isTopArrow) {
        }
        f = this.extraTranslationY;
        measuredHeight2 = i92 - getMeasuredHeight();
        float f22 = measuredHeight2;
        this.translationY = f22;
        setTranslationY(f + f22);
        if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
        }
        if (this.currentType != 8) {
        }
        if (i3 <= view2.getMeasuredWidth() / 2) {
        }
        setTranslationX(i82);
        float measuredWidth52 = (i3 - (i4 + i82)) - (this.arrowImageView.getMeasuredWidth() / 2.0f);
        if (this.currentType == 7) {
        }
        this.arrowImageView.setTranslationX(measuredWidth52);
        if (i3 <= view2.getMeasuredWidth() / 2) {
        }
        measuredWidth3 = measuredWidth52 - AndroidUtilities.dp(10.0f);
        setTranslationX(getTranslationX() + measuredWidth3);
        this.arrowImageView.setTranslationX(measuredWidth52 - measuredWidth3);
    }

    public void createCloseButton() {
        this.textView.setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(36.0f), AndroidUtilities.dp(8.0f));
        this.hasCloseButton = true;
        ImageView imageView = new ImageView(getContext());
        this.imageView = imageView;
        imageView.setImageResource(R.drawable.msg_mini_close_tooltip);
        this.imageView.setScaleType(ImageView.ScaleType.CENTER);
        this.imageView.setColorFilter(new PorterDuffColorFilter(ColorUtils.setAlphaComponent(getThemedColor(Theme.key_chat_gifSaveHintText), 125), PorterDuff.Mode.MULTIPLY));
        ImageView imageView2 = this.imageView;
        boolean z = this.isTopArrow;
        addView(imageView2, LayoutHelper.createFrame(34, 34.0f, 21, 0.0f, z ? 3.0f : 0.0f, 0.0f, z ? 0.0f : 3.0f));
        setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.HintView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HintView.this.lambda$createCloseButton$0(view);
            }
        });
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.drawPath && this.path != null) {
            if (this.backgroundPaint == null) {
                Paint paint = new Paint(1);
                this.backgroundPaint = paint;
                paint.setPathEffect(new CornerPathEffect(AndroidUtilities.dpf2(6.0f)));
                this.backgroundPaint.setColor(this.backgroundColor);
            }
            canvas.drawPath(this.path, this.backgroundPaint);
        }
        super.dispatchDraw(canvas);
    }

    public float getBaseTranslationY() {
        return this.translationY;
    }

    public ChatMessageCell getMessageCell() {
        return this.messageCell;
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean z) {
        if (getTag() == null) {
            return;
        }
        setTag(null);
        Runnable runnable = this.hideRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.hideRunnable = null;
        }
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        if (!z) {
            setVisibility(4);
            this.currentView = null;
            this.messageCell = null;
            this.animatorSet = null;
            return;
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.animatorSet = animatorSet2;
        if (this.useScale) {
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this, (Property<HintView, Float>) View.ALPHA, 1.0f, 0.0f), ObjectAnimator.ofFloat(this, (Property<HintView, Float>) View.SCALE_Y, 1.0f, 0.5f), ObjectAnimator.ofFloat(this, (Property<HintView, Float>) View.SCALE_X, 1.0f, 0.5f));
            this.animatorSet.setDuration(150L);
            this.animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
        } else {
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this, (Property<HintView, Float>) View.ALPHA, 0.0f));
            this.animatorSet.setDuration(300L);
        }
        this.animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.HintView.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                HintView.this.setVisibility(4);
                HintView.this.getClass();
                HintView.this.currentView = null;
                HintView.this.messageCell = null;
                HintView.this.animatorSet = null;
            }
        });
        this.animatorSet.start();
    }

    public boolean isShowing() {
        return getTag() != null;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.drawPath) {
            int measuredHeight = getMeasuredHeight();
            int measuredWidth = getMeasuredWidth();
            float x = this.arrowImageView.getX() + (this.arrowImageView.getMeasuredWidth() / 2.0f);
            Path path = this.path;
            if (path == null) {
                this.path = new Path();
            } else {
                path.rewind();
            }
            if (this.isTopArrow) {
                this.path.moveTo(0.0f, AndroidUtilities.dp(6.0f));
                float f = measuredHeight;
                this.path.lineTo(0.0f, f);
                float f2 = measuredWidth;
                this.path.lineTo(f2, f);
                this.path.lineTo(f2, AndroidUtilities.dp(6.0f));
                this.path.lineTo(AndroidUtilities.dp(7.0f) + x, AndroidUtilities.dp(6.0f));
                this.path.lineTo(x, -AndroidUtilities.dp(2.0f));
                this.path.lineTo(x - AndroidUtilities.dp(7.0f), AndroidUtilities.dp(6.0f));
            } else {
                this.path.moveTo(0.0f, measuredHeight - AndroidUtilities.dp(6.0f));
                this.path.lineTo(0.0f, 0.0f);
                float f3 = measuredWidth;
                this.path.lineTo(f3, 0.0f);
                this.path.lineTo(f3, measuredHeight - AndroidUtilities.dp(6.0f));
                this.path.lineTo(AndroidUtilities.dp(7.0f) + x, measuredHeight - AndroidUtilities.dp(6.0f));
                this.path.lineTo(x, AndroidUtilities.dp(2.0f) + measuredHeight);
                this.path.lineTo(x - AndroidUtilities.dp(7.0f), measuredHeight - AndroidUtilities.dp(6.0f));
            }
            this.path.close();
        }
    }

    public void setBackgroundColor(int i, int i2) {
        this.textView.setTextColor(i2);
        this.arrowImageView.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
        TextView textView = this.textView;
        int i3 = this.currentType;
        textView.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp((i3 == 7 || i3 == 8) ? 6.0f : 3.0f), i));
    }

    public void setBottomOffset(int i) {
        this.bottomOffset = i;
    }

    public void setExtraTranslationY(float f) {
        this.extraTranslationY = f;
        setTranslationY(f + this.translationY);
    }

    public void setOverrideText(String str) {
        this.overrideText = str;
        this.textView.setText(str);
        ChatMessageCell chatMessageCell = this.messageCell;
        if (chatMessageCell != null) {
            this.messageCell = null;
            showForMessageCell(chatMessageCell, false);
        }
    }

    public void setShowingDuration(long j) {
        this.showingDuration = j;
    }

    public void setText(CharSequence charSequence) {
        this.textView.setText(charSequence);
    }

    public void setUseScale(boolean z) {
        this.useScale = z;
    }

    public void setVisibleListener(VisibilityListener visibilityListener) {
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0219, code lost:
    
        if (r2 < org.telegram.messenger.AndroidUtilities.dp(10.0f)) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x024c, code lost:
    
        r5 = r2 - org.telegram.messenger.AndroidUtilities.dp(10.0f);
        setTranslationX(getTranslationX() + r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x024a, code lost:
    
        if (r2 < org.telegram.messenger.AndroidUtilities.dp(10.0f)) goto L91;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean showForMessageCell(ChatMessageCell chatMessageCell, Object obj, int i, int i2, boolean z) {
        int dp;
        int forwardNameCenterX;
        TextView textView;
        String formatPluralString;
        int i3;
        float measuredWidth;
        int i4 = this.currentType;
        if ((i4 == 5 && i2 == this.shownY && this.messageCell == chatMessageCell) || (i4 != 5 && ((i4 == 0 && getTag() != null) || this.messageCell == chatMessageCell))) {
            return false;
        }
        Runnable runnable = this.hideRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.hideRunnable = null;
        }
        int[] iArr = new int[2];
        chatMessageCell.getLocationInWindow(iArr);
        int i5 = iArr[1];
        ((View) getParent()).getLocationInWindow(iArr);
        int i6 = i5 - iArr[1];
        View view = (View) chatMessageCell.getParent();
        int i7 = this.currentType;
        if (i7 == 0) {
            ImageReceiver photoImage = chatMessageCell.getPhotoImage();
            dp = (int) (i6 + photoImage.getImageY());
            int imageHeight = (int) photoImage.getImageHeight();
            int i8 = dp + imageHeight;
            int measuredHeight = view.getMeasuredHeight();
            if (dp <= getMeasuredHeight() + AndroidUtilities.dp(10.0f) || i8 > measuredHeight + (imageHeight / 4)) {
                return false;
            }
            forwardNameCenterX = chatMessageCell.getNoSoundIconCenterX();
            measure(View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE));
        } else if (i7 == 5) {
            Integer num = (Integer) obj;
            dp = i6 + i2;
            this.shownY = i2;
            if (num.intValue() == -1) {
                textView = this.textView;
                i3 = R.string.PollSelectOption;
            } else if (chatMessageCell.getMessageObject().isQuiz()) {
                if (num.intValue() == 0) {
                    textView = this.textView;
                    i3 = R.string.NoVotesQuiz;
                } else {
                    textView = this.textView;
                    formatPluralString = LocaleController.formatPluralString("Answer", num.intValue(), new Object[0]);
                    textView.setText(formatPluralString);
                    measure(View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE));
                    forwardNameCenterX = i;
                }
            } else if (num.intValue() == 0) {
                textView = this.textView;
                i3 = R.string.NoVotes;
            } else {
                textView = this.textView;
                formatPluralString = LocaleController.formatPluralString("Vote", num.intValue(), new Object[0]);
                textView.setText(formatPluralString);
                measure(View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE));
                forwardNameCenterX = i;
            }
            formatPluralString = LocaleController.getString(i3);
            textView.setText(formatPluralString);
            measure(View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE));
            forwardNameCenterX = i;
        } else {
            MessageObject messageObject = chatMessageCell.getMessageObject();
            String str = this.overrideText;
            if (str == null) {
                this.textView.setText(LocaleController.getString(R.string.HidAccount));
            } else {
                this.textView.setText(str);
            }
            measure(View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE));
            TLRPC.User currentUser = chatMessageCell.getCurrentUser();
            if (currentUser == null || currentUser.id != 0) {
                dp = i6 + AndroidUtilities.dp(22.0f);
                if (!messageObject.isOutOwner() && chatMessageCell.isDrawNameLayout()) {
                    dp += AndroidUtilities.dp(20.0f);
                }
                if (!messageObject.shouldDrawWithoutBackground() && chatMessageCell.isDrawTopic()) {
                    dp = (int) (dp + AndroidUtilities.dp(5.0f) + chatMessageCell.getDrawTopicHeight());
                }
            } else {
                dp = i6 + ((chatMessageCell.getMeasuredHeight() - Math.max(0, chatMessageCell.getBottom() - view.getMeasuredHeight())) - AndroidUtilities.dp(50.0f));
            }
            if (!this.isTopArrow && dp <= getMeasuredHeight() + AndroidUtilities.dp(10.0f)) {
                return false;
            }
            forwardNameCenterX = chatMessageCell.getForwardNameCenterX();
        }
        int measuredWidth2 = view.getMeasuredWidth();
        if (this.isTopArrow) {
            float f = this.extraTranslationY;
            float dp2 = AndroidUtilities.dp(44.0f);
            this.translationY = dp2;
            setTranslationY(f + dp2);
        } else {
            float f2 = this.extraTranslationY;
            float measuredHeight2 = dp - getMeasuredHeight();
            this.translationY = measuredHeight2;
            setTranslationY(f2 + measuredHeight2);
        }
        int left = chatMessageCell.getLeft() + forwardNameCenterX;
        int dp3 = AndroidUtilities.dp(19.0f);
        if (this.currentType == 5) {
            int max = Math.max(0, (forwardNameCenterX - (getMeasuredWidth() / 2)) - AndroidUtilities.dp(19.1f));
            setTranslationX(max);
            dp3 += max;
        } else if (left > view.getMeasuredWidth() / 2) {
            int measuredWidth3 = (measuredWidth2 - getMeasuredWidth()) - AndroidUtilities.dp(38.0f);
            setTranslationX(measuredWidth3);
            dp3 += measuredWidth3;
        } else {
            setTranslationX(0.0f);
        }
        float left2 = ((chatMessageCell.getLeft() + forwardNameCenterX) - dp3) - (this.arrowImageView.getMeasuredWidth() / 2);
        this.arrowImageView.setTranslationX(left2);
        if (left <= view.getMeasuredWidth() / 2) {
            if (left2 > getMeasuredWidth() - AndroidUtilities.dp(24.0f)) {
                measuredWidth = (left2 - getMeasuredWidth()) + AndroidUtilities.dp(24.0f);
                setTranslationX(measuredWidth);
            }
        }
        this.arrowImageView.setTranslationX(left2 - measuredWidth);
        this.messageCell = chatMessageCell;
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        setTag(1);
        setVisibility(0);
        if (z) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animatorSet = animatorSet2;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this, (Property<HintView, Float>) View.ALPHA, 0.0f, 1.0f));
            this.animatorSet.addListener(new 1());
            this.animatorSet.setDuration(300L);
            this.animatorSet.start();
        } else {
            setAlpha(1.0f);
        }
        return true;
    }

    public boolean showForMessageCell(ChatMessageCell chatMessageCell, boolean z) {
        return showForMessageCell(chatMessageCell, null, 0, 0, z);
    }

    public boolean showForView(View view, boolean z) {
        if (this.currentView == view || getTag() != null) {
            if (getTag() != null) {
                updatePosition(view);
            }
            return false;
        }
        Runnable runnable = this.hideRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.hideRunnable = null;
        }
        updatePosition(view);
        this.currentView = view;
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        setTag(1);
        setVisibility(0);
        if (z) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animatorSet = animatorSet2;
            if (this.useScale) {
                setPivotX(this.arrowImageView.getX() + (this.arrowImageView.getMeasuredWidth() / 2.0f));
                setPivotY(this.arrowImageView.getY() + (this.arrowImageView.getMeasuredHeight() / 2.0f));
                this.animatorSet.playTogether(ObjectAnimator.ofFloat(this, (Property<HintView, Float>) View.ALPHA, 0.0f, 1.0f), ObjectAnimator.ofFloat(this, (Property<HintView, Float>) View.SCALE_Y, 0.5f, 1.0f), ObjectAnimator.ofFloat(this, (Property<HintView, Float>) View.SCALE_X, 0.5f, 1.0f));
                this.animatorSet.setDuration(350L);
                this.animatorSet.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            } else {
                animatorSet2.playTogether(ObjectAnimator.ofFloat(this, (Property<HintView, Float>) View.ALPHA, 0.0f, 1.0f));
                this.animatorSet.setDuration(300L);
            }
            this.animatorSet.addListener(new 2());
            this.animatorSet.start();
        } else {
            setAlpha(1.0f);
        }
        return true;
    }

    public void updatePosition() {
        View view = this.currentView;
        if (view == null) {
            return;
        }
        updatePosition(view);
    }
}
