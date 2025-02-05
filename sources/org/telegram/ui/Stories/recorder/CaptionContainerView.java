package org.telegram.ui.Stories.recorder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.google.zxing.common.detector.MathUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.BlurringShader;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.CaptionPhotoViewer;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MentionsContainerView;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.Text;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.Stories.DarkThemeResourceProvider;
import org.telegram.ui.Stories.recorder.CaptionContainerView;

/* loaded from: classes5.dex */
public abstract class CaptionContainerView extends FrameLayout {
    public ImageView applyButton;
    private Drawable applyButtonCheck;
    private CombinedDrawable applyButtonDrawable;
    protected final BlurringShader.StoryBlurDrawer backgroundBlur;
    protected final Paint backgroundPaint;
    int beforeScrollY;
    private Bitmap blurBitmap;
    private Matrix blurBitmapMatrix;
    private BitmapShader blurBitmapShader;
    private final BlurringShader.BlurManager blurManager;
    private Paint blurPaint;
    private final ButtonBounce bounce;
    public final RectF bounds;
    protected final BlurringShader.StoryBlurDrawer captionBlur;
    private final RectF clickBounds;
    private int codePointCount;
    private RadialGradient collapseGradient;
    private Matrix collapseGradientMatrix;
    private RadialGradient collapseOutGradient;
    private Paint collapseOutPaint;
    private Paint collapsePaint;
    public boolean collapsed;
    public int collapsedFromX;
    public final AnimatedFloat collapsedT;
    private final FrameLayout containerView;
    protected int currentAccount;
    private long dialogId;
    public final EditTextEmoji editText;
    private final LinearGradient fadeGradient;
    private final Paint fadePaint;
    private Utilities.CallbackVoidReturn getUiBlurBitmap;
    int goingToScrollY;
    private boolean hasReply;
    private final AnimatedFloat heightAnimated;
    private Bitmap hintTextBitmap;
    private final Paint hintTextBitmapPaint;
    private final TextPaint hintTextPaint;
    private boolean ignoreDraw;
    private boolean ignoreTextChange;
    public boolean ignoreTouches;
    private ValueAnimator keyboardAnimator;
    public final KeyboardNotifier keyboardNotifier;
    public boolean keyboardShown;
    public float keyboardT;
    private int lastHeight;
    private float lastHeightTranslation;
    public FrameLayout limitTextContainer;
    public AnimatedTextView limitTextView;
    private final Matrix matrix;
    private BlurringShader.StoryBlurDrawer mentionBackgroundBlur;
    public MentionsContainerView mentionContainer;
    private Utilities.Callback onHeightUpdate;
    private Utilities.Callback onKeyboardOpen;
    ObjectAnimator parentKeyboardAnimator;
    private final RectF rectF;
    protected final BlurringShader.StoryBlurDrawer replyBackgroundBlur;
    private Path replyClipPath;
    private Paint replyLinePaint;
    private Path replyLinePath;
    private float[] replyLinePathRadii;
    private Text replyText;
    protected final BlurringShader.StoryBlurDrawer replyTextBlur;
    private Text replyTitle;
    protected Theme.ResourcesProvider resourcesProvider;
    private final FrameLayout rootView;
    private ObjectAnimator scrollAnimator;
    private int shiftDp;
    private final SizeNotifierFrameLayout sizeNotifierFrameLayout;
    private final Runnable textChangeRunnable;
    public boolean toKeyboardShow;
    private Runnable updateShowKeyboard;
    boolean waitingForScrollYChange;

    class 2 implements TextWatcher {
        private int lastLength;
        private boolean lastOverLimit;

        2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$afterTextChanged$0() {
            CaptionContainerView.this.waitingForScrollYChange = false;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            String str;
            CaptionContainerView.this.codePointCount = Character.codePointCount(editable, 0, editable.length());
            int captionLimit = CaptionContainerView.this.getCaptionLimit();
            if (CaptionContainerView.this.codePointCount + 25 > captionLimit) {
                str = "" + (captionLimit - CaptionContainerView.this.codePointCount);
            } else {
                str = null;
            }
            CaptionContainerView.this.limitTextView.cancelAnimation();
            CaptionContainerView.this.limitTextView.setText(str);
            CaptionContainerView captionContainerView = CaptionContainerView.this;
            captionContainerView.limitTextView.setTextColor(captionContainerView.codePointCount >= captionLimit ? -1280137 : -1);
            if (CaptionContainerView.this.codePointCount > captionLimit && !UserConfig.getInstance(CaptionContainerView.this.currentAccount).isPremium() && CaptionContainerView.this.codePointCount < CaptionContainerView.this.getCaptionPremiumLimit() && CaptionContainerView.this.codePointCount > this.lastLength && (CaptionContainerView.this.captionLimitToast() || MessagesController.getInstance(CaptionContainerView.this.currentAccount).premiumFeaturesBlocked())) {
                AndroidUtilities.shakeViewSpring(CaptionContainerView.this.limitTextView, r0.shiftDp = -r0.shiftDp);
                BotWebViewVibrationEffect.APP_ERROR.vibrate();
            }
            this.lastLength = CaptionContainerView.this.codePointCount;
            boolean z = CaptionContainerView.this.codePointCount > captionLimit;
            if (z != this.lastOverLimit) {
                CaptionContainerView.this.onCaptionLimitUpdate(z);
            }
            this.lastOverLimit = z;
            if (!CaptionContainerView.this.ignoreTextChange) {
                AndroidUtilities.cancelRunOnUIThread(CaptionContainerView.this.textChangeRunnable);
                AndroidUtilities.runOnUIThread(CaptionContainerView.this.textChangeRunnable, 1500L);
            }
            CaptionContainerView.this.ignoreTextChange = false;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CaptionContainerView.2.this.lambda$afterTextChanged$0();
                }
            });
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (CaptionContainerView.this.scrollAnimator == null || !CaptionContainerView.this.scrollAnimator.isRunning()) {
                CaptionContainerView captionContainerView = CaptionContainerView.this;
                captionContainerView.beforeScrollY = captionContainerView.editText.getEditText().getScrollY();
                CaptionContainerView.this.waitingForScrollYChange = true;
            }
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (CaptionContainerView.this.editText.getEditText().suppressOnTextChanged) {
                return;
            }
            CaptionContainerView captionContainerView = CaptionContainerView.this;
            if (captionContainerView.mentionContainer == null) {
                captionContainerView.createMentionsContainer();
            }
            if (CaptionContainerView.this.mentionContainer.getAdapter() != null) {
                CaptionContainerView.this.mentionContainer.getAdapter().setUserOrChat(MessagesController.getInstance(CaptionContainerView.this.currentAccount).getUser(Long.valueOf(CaptionContainerView.this.dialogId)), MessagesController.getInstance(CaptionContainerView.this.currentAccount).getChat(Long.valueOf(-CaptionContainerView.this.dialogId)));
                CaptionContainerView.this.mentionContainer.getAdapter().lambda$searchUsernameOrHashtag$7(charSequence, CaptionContainerView.this.editText.getEditText().getSelectionStart(), null, false, false);
            }
        }
    }

    public static class BounceableImageView extends ImageView {
        private final ButtonBounce bounce;
        private final float scale;

        public BounceableImageView(Context context) {
            this(context, 0.2f);
        }

        public BounceableImageView(Context context, float f) {
            super(context);
            this.bounce = new ButtonBounce(this);
            this.scale = f;
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            canvas.save();
            float scale = this.bounce.getScale(this.scale);
            canvas.scale(scale, scale, getWidth() / 2.0f, getHeight() / 2.0f);
            super.draw(canvas);
            canvas.restore();
        }

        @Override // android.view.View
        public void setPressed(boolean z) {
            super.setPressed(z);
            this.bounce.setPressed(z);
        }
    }

    public static class PeriodDrawable extends Drawable {
        private final Path activePath;
        public final AnimatedTextView.AnimatedTextDrawable activeTextDrawable;
        private boolean clear;
        private float cx;
        private float cy;
        private final int dashes;
        public float diameterDp;
        private final Paint fillPaint;
        private final AnimatedFloat fillT;
        private boolean filled;
        public final Paint strokePaint;
        public final AnimatedTextView.AnimatedTextDrawable textDrawable;
        public float textOffsetX;
        public float textOffsetY;

        public PeriodDrawable() {
            this(5);
        }

        public PeriodDrawable(int i) {
            boolean z = true;
            Paint paint = new Paint(1);
            this.strokePaint = paint;
            this.fillPaint = new Paint(1);
            boolean z2 = false;
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(z, z2, z2) { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView.PeriodDrawable.1
                @Override // android.graphics.drawable.Drawable
                public void invalidateSelf() {
                    PeriodDrawable.this.invalidateSelf();
                }
            };
            this.textDrawable = animatedTextDrawable;
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = new AnimatedTextView.AnimatedTextDrawable(z, z2, z2) { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView.PeriodDrawable.2
                @Override // android.graphics.drawable.Drawable
                public void invalidateSelf() {
                    PeriodDrawable.this.invalidateSelf();
                }
            };
            this.activeTextDrawable = animatedTextDrawable2;
            this.filled = false;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView$PeriodDrawable$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CaptionContainerView.PeriodDrawable.this.invalidateSelf();
                }
            };
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.fillT = new AnimatedFloat(runnable, 0L, 350L, cubicBezierInterpolator);
            this.activePath = new Path();
            this.diameterDp = 21.0f;
            this.dashes = i;
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(AndroidUtilities.dpf2(1.66f));
            paint.setStrokeCap(Paint.Cap.ROUND);
            animatedTextDrawable.setAnimationProperties(0.3f, 0L, 250L, cubicBezierInterpolator);
            animatedTextDrawable.setTypeface(AndroidUtilities.getTypeface("fonts/num.otf"));
            animatedTextDrawable.setTextSize(AndroidUtilities.dpf2(12.0f));
            animatedTextDrawable.setGravity(17);
            animatedTextDrawable2.setAnimationProperties(0.3f, 0L, 250L, cubicBezierInterpolator);
            animatedTextDrawable2.setTypeface(AndroidUtilities.getTypeface("fonts/num.otf"));
            animatedTextDrawable2.setTextSize(AndroidUtilities.dpf2(12.0f));
            animatedTextDrawable2.setGravity(17);
            updateColors(-1, -15033089, -1);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            draw(canvas, 1.0f);
        }

        public void draw(Canvas canvas, float f) {
            float dpf2 = AndroidUtilities.dpf2(this.diameterDp) / 2.0f;
            float f2 = this.fillT.set(this.filled);
            if (f2 > 0.0f) {
                this.fillPaint.setAlpha((int) (f * 255.0f * f2));
                canvas.drawCircle(this.cx, this.cy, AndroidUtilities.dpf2(11.33f) * f2, this.fillPaint);
            }
            float f3 = f * 255.0f;
            this.strokePaint.setAlpha((int) ((1.0f - f2) * f3));
            RectF rectF = AndroidUtilities.rectTmp;
            float f4 = this.cx;
            float f5 = this.cy;
            rectF.set(f4 - dpf2, f5 - dpf2, f4 + dpf2, f5 + dpf2);
            canvas.drawArc(rectF, 90.0f, 180.0f, false, this.strokePaint);
            float f6 = (this.dashes * 1.0f) + ((r1 + 1) * 1.5f);
            float f7 = (1.0f / f6) * 180.0f;
            float f8 = (1.5f / f6) * 180.0f;
            float f9 = f8;
            for (int i = 0; i < this.dashes; i++) {
                canvas.drawArc(AndroidUtilities.rectTmp, f9 + 270.0f, f7, false, this.strokePaint);
                f9 += f7 + f8;
            }
            canvas.save();
            canvas.translate(this.textOffsetX + 0.0f, this.textOffsetY);
            Rect rect = AndroidUtilities.rectTmp2;
            rect.set((int) (this.cx - AndroidUtilities.dp(20.0f)), (int) (this.cy - AndroidUtilities.dp(20.0f)), (int) (this.cx + AndroidUtilities.dp(20.0f)), (int) (this.cy + AndroidUtilities.dp(20.0f)));
            this.textDrawable.setBounds(rect);
            int i2 = (int) f3;
            this.textDrawable.setAlpha(i2);
            this.textDrawable.draw(canvas);
            if (f2 > 0.0f) {
                this.activePath.rewind();
                this.activePath.addCircle(this.cx, this.cy + AndroidUtilities.dp(1.0f), AndroidUtilities.dpf2(11.33f) * f2, Path.Direction.CW);
                canvas.clipPath(this.activePath);
                this.activeTextDrawable.setBounds(rect);
                this.activeTextDrawable.setAlpha(i2);
                this.activeTextDrawable.draw(canvas);
            }
            canvas.restore();
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(24.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp(24.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setBounds(int i, int i2, int i3, int i4) {
            super.setBounds(i, i2, i3, i4);
            this.cx = getBounds().centerX();
            this.cy = getBounds().centerY();
        }

        @Override // android.graphics.drawable.Drawable
        public void setBounds(Rect rect) {
            super.setBounds(rect);
            this.cx = getBounds().centerX();
            this.cy = getBounds().centerY();
        }

        public void setCenterXY(float f, float f2) {
            this.cx = f;
            this.cy = f2;
        }

        public void setClear(boolean z) {
            if (this.clear != z) {
                this.clear = z;
                this.strokePaint.setXfermode(z ? new PorterDuffXfermode(PorterDuff.Mode.CLEAR) : null);
                this.textDrawable.getPaint().setXfermode(z ? new PorterDuffXfermode(PorterDuff.Mode.CLEAR) : null);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public void setTextSize(float f) {
            this.activeTextDrawable.setTextSize(AndroidUtilities.dpf2(f));
            this.textDrawable.setTextSize(AndroidUtilities.dpf2(f));
        }

        public void setValue(int i, boolean z, boolean z2) {
            this.textDrawable.setText("" + i, z2);
            this.activeTextDrawable.setText("" + i, z2);
            this.filled = z;
            if (!z2) {
                this.fillT.set(z, true);
            }
            invalidateSelf();
        }

        public void updateColors(int i, int i2, int i3) {
            this.strokePaint.setColor(i);
            this.textDrawable.setTextColor(i);
            this.activeTextDrawable.setTextColor(i3);
            this.fillPaint.setColor(i2);
        }
    }

    public CaptionContainerView(Context context, FrameLayout frameLayout, SizeNotifierFrameLayout sizeNotifierFrameLayout, FrameLayout frameLayout2, Theme.ResourcesProvider resourcesProvider, final BlurringShader.BlurManager blurManager) {
        super(context);
        Paint paint = new Paint(1);
        this.backgroundPaint = paint;
        Paint paint2 = new Paint(1);
        this.fadePaint = paint2;
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(10.0f), new int[]{-65536, 0}, new float[]{0.05f, 1.0f}, Shader.TileMode.CLAMP);
        this.fadeGradient = linearGradient;
        this.matrix = new Matrix();
        this.hintTextPaint = new TextPaint(3);
        Paint paint3 = new Paint(3);
        this.hintTextBitmapPaint = paint3;
        this.shiftDp = -4;
        this.currentAccount = UserConfig.selectedAccount;
        this.textChangeRunnable = new Runnable() { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CaptionContainerView.this.lambda$new$1();
            }
        };
        this.bounce = new ButtonBounce(this, 1.0f, 3.0f);
        this.updateShowKeyboard = new Runnable() { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CaptionContainerView.this.lambda$new$2();
            }
        };
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.heightAnimated = new AnimatedFloat(this, 0L, 300L, cubicBezierInterpolator);
        this.ignoreDraw = false;
        this.rectF = new RectF();
        this.bounds = new RectF();
        this.clickBounds = new RectF();
        this.collapsedT = new AnimatedFloat(this, 500L, cubicBezierInterpolator);
        this.resourcesProvider = resourcesProvider;
        this.rootView = frameLayout;
        this.sizeNotifierFrameLayout = sizeNotifierFrameLayout;
        this.containerView = frameLayout2;
        this.blurManager = blurManager;
        this.backgroundBlur = new BlurringShader.StoryBlurDrawer(blurManager, this, 0, !customBlur());
        this.replyBackgroundBlur = new BlurringShader.StoryBlurDrawer(blurManager, this, 8);
        this.replyTextBlur = new BlurringShader.StoryBlurDrawer(blurManager, this, 9);
        paint.setColor(Integer.MIN_VALUE);
        this.keyboardNotifier = new KeyboardNotifier(frameLayout, new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView$$ExternalSyntheticLambda2
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                CaptionContainerView.this.updateKeyboard(((Integer) obj).intValue());
            }
        });
        EditTextEmoji editTextEmoji = new EditTextEmoji(context, sizeNotifierFrameLayout, null, getEditTextStyle(), true, new DarkThemeResourceProvider()) { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView.1
            private BlurringShader.StoryBlurDrawer blurDrawer;

            @Override // org.telegram.ui.Components.EditTextEmoji
            protected boolean allowSearch() {
                return true;
            }

            @Override // org.telegram.ui.Components.EditTextEmoji
            protected void createEmojiView() {
                super.createEmojiView();
                EmojiView emojiView = getEmojiView();
                if (emojiView != null) {
                    if (CaptionContainerView.this.getEditTextStyle() == 2 || CaptionContainerView.this.getEditTextStyle() == 3) {
                        emojiView.shouldLightenBackground = false;
                        emojiView.fixBottomTabContainerTranslation = false;
                        emojiView.setShouldDrawBackground(false);
                        if (CaptionContainerView.this instanceof CaptionPhotoViewer) {
                            emojiView.setPadding(0, 0, 0, AndroidUtilities.navigationBarHeight);
                            emojiView.emojiCacheType = 3;
                        }
                    }
                }
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                CaptionContainerView captionContainerView = CaptionContainerView.this;
                if ((captionContainerView instanceof CaptionStory) && ((CaptionStory) captionContainerView).isRecording()) {
                    return false;
                }
                return super.dispatchTouchEvent(motionEvent);
            }

            @Override // org.telegram.ui.Components.EditTextEmoji
            protected void drawEmojiBackground(Canvas canvas, View view) {
                CaptionContainerView.this.rectF.set(0.0f, 0.0f, view.getWidth(), view.getHeight());
                if (!CaptionContainerView.this.customBlur()) {
                    CaptionContainerView captionContainerView = CaptionContainerView.this;
                    captionContainerView.drawBackground(canvas, captionContainerView.rectF, 0.0f, 0.95f, view);
                } else {
                    if (this.blurDrawer == null) {
                        this.blurDrawer = new BlurringShader.StoryBlurDrawer(blurManager, view, 7);
                    }
                    CaptionContainerView captionContainerView2 = CaptionContainerView.this;
                    captionContainerView2.drawBlur(this.blurDrawer, canvas, captionContainerView2.rectF, 0.0f, false, 0.0f, -view.getY(), false, 1.0f);
                }
            }

            @Override // org.telegram.ui.Components.EditTextEmoji
            protected void onEmojiKeyboardUpdate() {
                CaptionContainerView.this.keyboardNotifier.fire();
            }

            @Override // org.telegram.ui.Components.EditTextEmoji
            protected boolean onScrollYChange(int i) {
                if (CaptionContainerView.this.scrollAnimator != null && CaptionContainerView.this.scrollAnimator.isRunning() && i == CaptionContainerView.this.goingToScrollY) {
                    return false;
                }
                CaptionContainerView.this.invalidate();
                CaptionContainerView captionContainerView = CaptionContainerView.this;
                if (!captionContainerView.waitingForScrollYChange) {
                    return true;
                }
                captionContainerView.waitingForScrollYChange = false;
                if (captionContainerView.beforeScrollY == i) {
                    return true;
                }
                if (captionContainerView.scrollAnimator != null && CaptionContainerView.this.scrollAnimator.isRunning() && i == CaptionContainerView.this.goingToScrollY) {
                    return true;
                }
                if (CaptionContainerView.this.scrollAnimator != null) {
                    CaptionContainerView.this.scrollAnimator.cancel();
                }
                CaptionContainerView.this.editText.getEditText().setScrollY(CaptionContainerView.this.beforeScrollY);
                CaptionContainerView captionContainerView2 = CaptionContainerView.this;
                EditTextCaption editText = captionContainerView2.editText.getEditText();
                CaptionContainerView captionContainerView3 = CaptionContainerView.this;
                int i2 = captionContainerView3.beforeScrollY;
                captionContainerView3.goingToScrollY = i;
                captionContainerView2.scrollAnimator = ObjectAnimator.ofInt(editText, "scrollY", i2, i);
                CaptionContainerView.this.scrollAnimator.setDuration(240L);
                CaptionContainerView.this.scrollAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                CaptionContainerView.this.scrollAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView.1.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (CaptionContainerView.this.scrollAnimator != animator) {
                            return;
                        }
                        CaptionContainerView.this.scrollAnimator = null;
                        CaptionContainerView.this.editText.getEditText().setScrollY(CaptionContainerView.this.goingToScrollY);
                    }
                });
                CaptionContainerView.this.scrollAnimator.start();
                return false;
            }

            @Override // org.telegram.ui.Components.EditTextEmoji
            protected void onWaitingForKeyboard() {
                CaptionContainerView.this.keyboardNotifier.awaitKeyboard();
            }

            @Override // org.telegram.ui.Components.EditTextEmoji
            protected void updatedEmojiExpanded() {
                CaptionContainerView.this.keyboardNotifier.fire();
            }
        };
        this.editText = editTextEmoji;
        editTextEmoji.setFocusable(true);
        editTextEmoji.setFocusableInTouchMode(true);
        editTextEmoji.getEditText().hintLayoutYFix = true;
        editTextEmoji.getEditText().drawHint = new Utilities.Callback2() { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView$$ExternalSyntheticLambda3
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                CaptionContainerView.this.drawHint((Canvas) obj, (Runnable) obj2);
            }
        };
        editTextEmoji.getEditText().setSupportRtlHint(true);
        this.captionBlur = new BlurringShader.StoryBlurDrawer(blurManager, editTextEmoji.getEditText(), customBlur() ? 1 : 2);
        editTextEmoji.getEditText().setHintColor(-1);
        editTextEmoji.getEditText().setHintText(LocaleController.getString(R.string.AddCaption), false);
        paint3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        editTextEmoji.getEditText().setTranslationX(AndroidUtilities.dp(-22.0f));
        if (isAtTop()) {
            editTextEmoji.getEditText().setGravity(48);
        }
        editTextEmoji.getEmojiButton().setAlpha(0.0f);
        editTextEmoji.getEditText().addTextChangedListener(new 2());
        editTextEmoji.getEditText().setLinkTextColor(-1);
        addView(editTextEmoji, LayoutHelper.createFrame(-1, -2.0f, (isAtTop() ? 48 : 80) | 7, 12.0f, 12.0f, additionalRightMargin() + 12, 12.0f));
        BounceableImageView bounceableImageView = new BounceableImageView(context);
        this.applyButton = bounceableImageView;
        ScaleStateListAnimator.apply(bounceableImageView, 0.05f, 1.25f);
        Drawable mutate = context.getResources().getDrawable(R.drawable.input_done).mutate();
        this.applyButtonCheck = mutate;
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogFloatingIcon), PorterDuff.Mode.SRC_IN));
        CombinedDrawable combinedDrawable = new CombinedDrawable(Theme.createCircleDrawable(AndroidUtilities.dp(16.0f), Theme.getColor(Theme.key_chat_editMediaButton, resourcesProvider)), this.applyButtonCheck, 0, AndroidUtilities.dp(1.0f));
        this.applyButtonDrawable = combinedDrawable;
        combinedDrawable.setCustomSize(AndroidUtilities.dp(32.0f), AndroidUtilities.dp(32.0f));
        this.applyButton.setImageDrawable(this.applyButtonDrawable);
        this.applyButton.setScaleType(ImageView.ScaleType.CENTER);
        this.applyButton.setAlpha(0.0f);
        this.applyButton.setVisibility(8);
        this.applyButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CaptionContainerView.this.lambda$new$0(view);
            }
        });
        this.applyButton.setTranslationY(-AndroidUtilities.dp(1.0f));
        addView(this.applyButton, LayoutHelper.createFrame(44, 44, (isAtTop() ? 48 : 80) | 5));
        AnimatedTextView animatedTextView = new AnimatedTextView(context, false, true, true);
        this.limitTextView = animatedTextView;
        animatedTextView.setGravity(17);
        this.limitTextView.setTextSize(AndroidUtilities.dp(15.0f));
        this.limitTextView.setTextColor(-1);
        this.limitTextView.setAnimationProperties(0.4f, 0L, 320L, cubicBezierInterpolator);
        this.limitTextView.setTypeface(AndroidUtilities.bold());
        FrameLayout frameLayout3 = new FrameLayout(context);
        this.limitTextContainer = frameLayout3;
        frameLayout3.setTranslationX(AndroidUtilities.dp(2.0f));
        this.limitTextContainer.addView(this.limitTextView, LayoutHelper.createFrame(52, 16, (isAtTop() ? 48 : 80) | 5));
        addView(this.limitTextContainer, LayoutHelper.createFrame(52, 16.0f, (isAtTop() ? 48 : 80) | 5, 0.0f, isAtTop() ? 50 : 0, 0.0f, isAtTop() ? 0 : 50));
        paint2.setShader(linearGradient);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    private void animateScrollTo(boolean z) {
        EditTextCaption editText = this.editText.getEditText();
        if (editText == null || editText.getLayout() == null) {
            return;
        }
        ObjectAnimator objectAnimator = this.scrollAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        int scrollY = editText.getScrollY();
        EditTextEmoji editTextEmoji = this.editText;
        editTextEmoji.setSelection(z ? editTextEmoji.length() : 0);
        this.editText.getEditText().setForceCursorEnd(false);
        ObjectAnimator ofInt = ObjectAnimator.ofInt(editText, "scrollY", scrollY, z ? editText.getLayout().getLineTop(editText.getLineCount()) - ((editText.getHeight() - editText.getPaddingTop()) - editText.getPaddingBottom()) : 0);
        this.scrollAnimator = ofInt;
        ofInt.setDuration(360L);
        this.scrollAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.scrollAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createMentionsContainer() {
        MentionsContainerView mentionsContainerView = new MentionsContainerView(getContext(), UserConfig.getInstance(this.currentAccount).getClientUserId(), 0L, LaunchActivity.getLastFragment(), null, new DarkThemeResourceProvider()) { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView.3
            @Override // org.telegram.ui.Components.MentionsContainerView
            public void drawRoundRect(Canvas canvas, Rect rect, float f) {
                Paint paint;
                int i;
                CaptionContainerView.this.rectF.set(rect);
                if (CaptionContainerView.this.customBlur()) {
                    CaptionContainerView captionContainerView = CaptionContainerView.this;
                    captionContainerView.drawBlur(captionContainerView.mentionBackgroundBlur, canvas, CaptionContainerView.this.rectF, f, false, -CaptionContainerView.this.mentionContainer.getX(), -CaptionContainerView.this.mentionContainer.getY(), false, 1.0f);
                    return;
                }
                Paint paint2 = CaptionContainerView.this.mentionBackgroundBlur.getPaint(1.0f);
                if (paint2 == null) {
                    paint = CaptionContainerView.this.backgroundPaint;
                    i = 128;
                } else {
                    canvas.drawRoundRect(CaptionContainerView.this.rectF, f, f, paint2);
                    paint = CaptionContainerView.this.backgroundPaint;
                    i = 80;
                }
                paint.setAlpha(i);
                canvas.drawRoundRect(CaptionContainerView.this.rectF, f, f, CaptionContainerView.this.backgroundPaint);
            }

            @Override // org.telegram.ui.Components.MentionsContainerView
            protected boolean isStories() {
                return true;
            }
        };
        this.mentionContainer = mentionsContainerView;
        this.mentionBackgroundBlur = new BlurringShader.StoryBlurDrawer(this.blurManager, mentionsContainerView, 0);
        this.mentionContainer.withDelegate(new MentionsContainerView.Delegate() { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView.4
            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public /* synthetic */ void addEmojiToRecent(String str) {
                MentionsContainerView.Delegate.-CC.$default$addEmojiToRecent(this, str);
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public Paint.FontMetricsInt getFontMetrics() {
                return CaptionContainerView.this.editText.getEditText().getPaint().getFontMetricsInt();
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public /* synthetic */ void onStickerSelected(TLRPC.TL_document tL_document, String str, Object obj) {
                MentionsContainerView.Delegate.-CC.$default$onStickerSelected(this, tL_document, str, obj);
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public void replaceText(int i, int i2, CharSequence charSequence, boolean z) {
                CaptionContainerView.this.replaceWithText(i, i2, charSequence, z);
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public /* synthetic */ void sendBotInlineResult(TLRPC.BotInlineResult botInlineResult, boolean z, int i) {
                MentionsContainerView.Delegate.-CC.$default$sendBotInlineResult(this, botInlineResult, z, i);
            }
        });
        this.containerView.addView(this.mentionContainer, LayoutHelper.createFrame(-1, -1, 83));
        setupMentionContainer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawBackground(Canvas canvas, RectF rectF, float f, float f2, View view) {
        Bitmap bitmap;
        float f3 = 0.0f;
        if (this.keyboardT > 0.0f && this.blurPaint != null && this.blurBitmapShader != null && (bitmap = this.blurBitmap) != null && !bitmap.isRecycled()) {
            this.blurBitmapMatrix.reset();
            this.blurBitmapMatrix.postScale(this.rootView.getWidth() / this.blurBitmap.getWidth(), this.rootView.getHeight() / this.blurBitmap.getHeight());
            float f4 = 0.0f;
            for (int i = 0; i < 8 && view != null; i++) {
                f3 += view.getX();
                f4 += view.getY();
                Object parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
            this.blurBitmapMatrix.postTranslate(-f3, -f4);
            this.blurBitmapShader.setLocalMatrix(this.blurBitmapMatrix);
            this.blurPaint.setAlpha((int) (this.keyboardT * 255.0f * f2));
            canvas.drawRoundRect(rectF, f, f, this.blurPaint);
        }
        this.backgroundPaint.setAlpha((int) (this.blurPaint == null ? 128.0f : f2 * AndroidUtilities.lerp(128, NotificationCenter.recordStopped, this.keyboardT)));
        canvas.drawRoundRect(rectF, f, f, this.backgroundPaint);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawHint(Canvas canvas, Runnable runnable) {
        if (!customBlur()) {
            Paint paint = this.captionBlur.getPaint(1.0f);
            this.editText.getEditText().setHintColor(paint != null ? -1 : -2130706433);
            if (paint == null) {
                runnable.run();
                return;
            }
            EditTextCaption editText = this.editText.getEditText();
            canvas.saveLayerAlpha(0.0f, 0.0f, editText.getWidth(), editText.getHeight(), NotificationCenter.liveLocationsChanged, 31);
            runnable.run();
            canvas.drawRect(0.0f, 0.0f, editText.getWidth(), editText.getHeight(), paint);
            canvas.restore();
            return;
        }
        if (this.hintTextBitmap == null) {
            runnable.run();
            return;
        }
        canvas.translate(-this.editText.getEditText().hintLayoutX, 0.0f);
        canvas.saveLayerAlpha(0.0f, 0.0f, this.hintTextBitmap.getWidth(), this.hintTextBitmap.getHeight(), NotificationCenter.liveLocationsChanged, 31);
        this.rectF.set(0.0f, 1.0f, this.hintTextBitmap.getWidth(), this.hintTextBitmap.getHeight() - 1);
        drawBlur(this.captionBlur, canvas, this.rectF, 0.0f, true, (-this.editText.getX()) - r8.getPaddingLeft(), ((-this.editText.getY()) - r8.getPaddingTop()) - r8.getExtendedPaddingTop(), true, 1.0f);
        canvas.save();
        this.hintTextBitmapPaint.setAlpha(NotificationCenter.activeGroupCallsUpdated);
        canvas.drawBitmap(this.hintTextBitmap, 0.0f, 0.0f, this.hintTextBitmapPaint);
        canvas.restore();
        canvas.restore();
    }

    private void drawReply(Canvas canvas) {
        float f;
        float f2;
        float f3;
        int min;
        if (!this.hasReply || this.replyBackgroundBlur == null || this.replyTextBlur == null) {
            return;
        }
        if (this.collapsed) {
            if (this.keyboardShown) {
                f3 = this.bounds.bottom;
                min = Math.max(AndroidUtilities.dp(46.0f), this.editText.getHeight());
            } else {
                f3 = this.bounds.bottom;
                min = Math.min(AndroidUtilities.dp(82.0f), this.editText.getHeight());
            }
            float f4 = f3 - min;
            f = f4 - AndroidUtilities.dp(50.0f);
            f2 = 1.0f - this.collapsedT.get();
        } else {
            f = this.bounds.top;
            f2 = 1.0f;
        }
        Paint paint = this.replyBackgroundBlur.getPaint(f2);
        Paint paint2 = this.replyTextBlur.getPaint(f2);
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(this.bounds.left + AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f) + f, this.bounds.right - AndroidUtilities.dp(10.0f), AndroidUtilities.dp(52.0f) + f);
        if (paint != null) {
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f), paint);
        }
        if (paint2 != null) {
            RectF rectF2 = this.bounds;
            canvas.saveLayerAlpha(rectF2.left, rectF2.top, rectF2.right, rectF2.bottom, NotificationCenter.liveLocationsChanged, 31);
        }
        Path path = this.replyClipPath;
        if (path == null) {
            this.replyClipPath = new Path();
        } else {
            path.rewind();
        }
        float lerp = AndroidUtilities.lerp(AndroidUtilities.dp(21.0f), 0, this.keyboardT);
        Path path2 = this.replyClipPath;
        RectF rectF3 = this.bounds;
        Path.Direction direction = Path.Direction.CW;
        path2.addRoundRect(rectF3, lerp, lerp, direction);
        canvas.clipPath(this.replyClipPath);
        Text text = this.replyTitle;
        if (text != null) {
            text.ellipsize((int) (this.bounds.width() - AndroidUtilities.dp(40.0f))).draw(canvas, AndroidUtilities.dp(20.0f) + this.bounds.left, f + AndroidUtilities.dp(22.0f), -1, 1.0f);
        }
        Path path3 = this.replyLinePath;
        if (path3 == null) {
            this.replyLinePath = new Path();
            float[] fArr = new float[8];
            this.replyLinePathRadii = fArr;
            float dp = AndroidUtilities.dp(5.0f);
            fArr[1] = dp;
            fArr[0] = dp;
            float[] fArr2 = this.replyLinePathRadii;
            fArr2[3] = 0.0f;
            fArr2[2] = 0.0f;
            fArr2[5] = 0.0f;
            fArr2[4] = 0.0f;
            float dp2 = AndroidUtilities.dp(5.0f);
            fArr2[7] = dp2;
            fArr2[6] = dp2;
        } else {
            path3.rewind();
        }
        float f5 = rectF.left;
        rectF.set(f5, rectF.top, AndroidUtilities.dp(3.0f) + f5, rectF.bottom);
        this.replyLinePath.addRoundRect(rectF, this.replyLinePathRadii, direction);
        if (this.replyLinePaint == null) {
            Paint paint3 = new Paint();
            this.replyLinePaint = paint3;
            paint3.setColor(-1);
        }
        this.replyLinePaint.setAlpha((int) (f2 * 255.0f));
        canvas.drawPath(this.replyLinePath, this.replyLinePaint);
        if (paint2 != null) {
            canvas.save();
            canvas.drawRect(this.bounds, paint2);
            canvas.restore();
            canvas.restore();
        }
        Text text2 = this.replyText;
        if (text2 != null) {
            text2.ellipsize((int) (this.bounds.width() - AndroidUtilities.dp(40.0f))).draw(canvas, AndroidUtilities.dp(20.0f) + this.bounds.left, f + AndroidUtilities.dp(40.0f), -1, 1.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        closeKeyboard();
        AndroidUtilities.cancelRunOnUIThread(this.textChangeRunnable);
        this.textChangeRunnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        updateShowKeyboard(this.toKeyboardShow, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateShowKeyboard$3(ValueAnimator valueAnimator) {
        this.keyboardT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.editText.getEditText().setTranslationX(AndroidUtilities.lerp(AndroidUtilities.dp(-22.0f) + getEditTextLeft(), AndroidUtilities.dp(2.0f), this.keyboardT));
        this.editText.setTranslationX(AndroidUtilities.lerp(0, AndroidUtilities.dp(-8.0f), this.keyboardT));
        this.editText.setTranslationY(AndroidUtilities.lerp(0, AndroidUtilities.dp(isAtTop() ? -10.0f : 10.0f), this.keyboardT));
        this.limitTextContainer.setTranslationX(AndroidUtilities.lerp(-AndroidUtilities.dp(8.0f), AndroidUtilities.dp(2.0f), this.keyboardT));
        this.limitTextContainer.setTranslationY(AndroidUtilities.lerp(-AndroidUtilities.dp(8.0f), 0, this.keyboardT));
        this.editText.getEmojiButton().setAlpha(this.keyboardT);
        this.applyButton.setAlpha((float) Math.pow(this.keyboardT, 16.0d));
        onUpdateShowKeyboard(this.keyboardT);
        MentionsContainerView mentionsContainerView = this.mentionContainer;
        if (mentionsContainerView != null) {
            mentionsContainerView.setAlpha((float) Math.pow(this.keyboardT, 4.0d));
        }
        this.editText.getEditText().invalidate();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void replaceWithText(int i, int i2, CharSequence charSequence, boolean z) {
        if (this.editText == null) {
            return;
        }
        try {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.editText.getText());
            spannableStringBuilder.replace(i, i2 + i, charSequence);
            if (z) {
                Emoji.replaceEmoji(spannableStringBuilder, this.editText.getEditText().getPaint().getFontMetricsInt(), false);
            }
            this.editText.setText(spannableStringBuilder);
            this.editText.setSelection(i + charSequence.length());
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private void updateShowKeyboard(final boolean z, boolean z2) {
        ValueAnimator valueAnimator;
        long j;
        if (this.keyboardShown == z) {
            return;
        }
        this.keyboardShown = z;
        ValueAnimator valueAnimator2 = this.keyboardAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.cancel();
            this.keyboardAnimator = null;
        }
        Utilities.Callback callback = this.onKeyboardOpen;
        if (callback != null) {
            callback.run(Boolean.valueOf(z));
        }
        beforeUpdateShownKeyboard(z);
        if (z2) {
            if (z) {
                MentionsContainerView mentionsContainerView = this.mentionContainer;
                if (mentionsContainerView != null) {
                    mentionsContainerView.setVisibility(0);
                }
                this.applyButton.setVisibility(0);
            } else {
                this.editText.getEditText().scrollBy(0, -this.editText.getEditText().getScrollY());
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.keyboardT, z ? 1.0f : 0.0f);
            this.keyboardAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView$$ExternalSyntheticLambda5
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                    CaptionContainerView.this.lambda$updateShowKeyboard$3(valueAnimator3);
                }
            });
            if (!z) {
                this.editText.getEditText().setAllowDrawCursor(false);
            }
            this.keyboardAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.recorder.CaptionContainerView.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (!z) {
                        CaptionContainerView.this.applyButton.setVisibility(8);
                        MentionsContainerView mentionsContainerView2 = CaptionContainerView.this.mentionContainer;
                        if (mentionsContainerView2 != null) {
                            mentionsContainerView2.setVisibility(8);
                        }
                    }
                    if (z) {
                        CaptionContainerView.this.editText.getEditText().setAllowDrawCursor(true);
                    }
                    CaptionContainerView.this.afterUpdateShownKeyboard(z);
                }
            });
            ValueAnimator valueAnimator3 = this.keyboardAnimator;
            if (z) {
                valueAnimator3.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                valueAnimator = this.keyboardAnimator;
                j = 250;
            } else {
                valueAnimator3.setInterpolator(new FastOutSlowInInterpolator());
                valueAnimator = this.keyboardAnimator;
                j = 420;
            }
            valueAnimator.setDuration(j);
            this.keyboardAnimator.start();
        } else {
            this.keyboardT = z ? 1.0f : 0.0f;
            this.editText.getEditText().setTranslationX(AndroidUtilities.lerp(AndroidUtilities.dp(-22.0f) + getEditTextLeft(), AndroidUtilities.dp(2.0f), this.keyboardT));
            this.editText.setTranslationX(AndroidUtilities.lerp(0, AndroidUtilities.dp(-8.0f), this.keyboardT));
            this.editText.setTranslationY(AndroidUtilities.lerp(0, AndroidUtilities.dp(isAtTop() ? -10.0f : 10.0f), this.keyboardT));
            this.limitTextContainer.setTranslationX(AndroidUtilities.lerp(-AndroidUtilities.dp(8.0f), AndroidUtilities.dp(2.0f), this.keyboardT));
            this.limitTextContainer.setTranslationY(AndroidUtilities.lerp(-AndroidUtilities.dp(8.0f), 0, this.keyboardT));
            this.editText.getEmojiButton().setAlpha(this.keyboardT);
            this.applyButton.setVisibility(z ? 0 : 8);
            this.applyButton.setAlpha(z ? 1.0f : 0.0f);
            onUpdateShowKeyboard(this.keyboardT);
            this.editText.getEditText().setAllowDrawCursor(z);
            afterUpdateShownKeyboard(z);
            invalidate();
        }
        animateScrollTo(z);
        this.editText.setSuggestionsEnabled(z);
        if (!z) {
            this.editText.getEditText().setSpoilersRevealed(false, true);
        }
        if (!z || SharedConfig.getDevicePerformanceClass() < 1 || LiteMode.isPowerSaverApplied()) {
            return;
        }
        if (this.blurBitmap == null) {
            this.blurBitmap = Bitmap.createBitmap((int) (this.rootView.getWidth() / 12.0f), (int) (this.rootView.getHeight() / 12.0f), Bitmap.Config.ARGB_8888);
        }
        this.ignoreDraw = true;
        drawBlurBitmap(this.blurBitmap, 12.0f);
        this.ignoreDraw = false;
        Bitmap bitmap = this.blurBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            this.blurBitmap = null;
            return;
        }
        Bitmap bitmap2 = this.blurBitmap;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        this.blurBitmapShader = new BitmapShader(bitmap2, tileMode, tileMode);
        Matrix matrix = this.blurBitmapMatrix;
        if (matrix == null) {
            this.blurBitmapMatrix = new Matrix();
        } else {
            matrix.reset();
        }
        this.blurBitmapShader.setLocalMatrix(this.blurBitmapMatrix);
        if (this.blurPaint == null) {
            Paint paint = new Paint(3);
            this.blurPaint = paint;
            paint.setColor(-1);
        }
        this.blurPaint.setShader(this.blurBitmapShader);
    }

    protected int additionalKeyboardHeight() {
        return AndroidUtilities.navigationBarHeight;
    }

    public int additionalRightMargin() {
        return 0;
    }

    protected abstract void afterUpdateShownKeyboard(boolean z);

    protected abstract void beforeUpdateShownKeyboard(boolean z);

    protected abstract boolean captionLimitToast();

    public void clear() {
        this.ignoreTextChange = true;
        this.editText.setText("");
    }

    @Override // android.view.ViewGroup, android.view.View
    public void clearFocus() {
        this.editText.clearFocus();
    }

    protected boolean clipChild(View view) {
        return true;
    }

    public void closeKeyboard() {
        this.editText.closeKeyboard();
        this.editText.hidePopup(true);
    }

    protected abstract boolean customBlur();

    /* JADX WARN: Code restructure failed: missing block: B:34:0x01b6, code lost:
    
        if ((r0 <= 0.0f) != (r1 <= 0.0f)) goto L60;
     */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchDraw(Canvas canvas) {
        float f;
        int i;
        Paint paint;
        int i2;
        if (this.ignoreDraw) {
            return;
        }
        int height = this.editText.getHeight();
        int dp = this.collapsed ? AndroidUtilities.dp(40.0f) : this.keyboardShown ? Math.max(AndroidUtilities.dp(46.0f), height) : Math.min(AndroidUtilities.dp(82.0f), height);
        if (!this.collapsed && this.hasReply) {
            dp += AndroidUtilities.dp(50.0f);
        }
        float f2 = dp;
        int i3 = (int) this.heightAnimated.set(f2);
        if (i3 != this.lastHeight) {
            onEditHeightChange(i3);
            Utilities.Callback callback = this.onHeightUpdate;
            if (callback != null) {
                callback.run(Integer.valueOf(i3));
            }
            this.lastHeight = dp;
        }
        updateMentionsLayoutPosition();
        float lerp = AndroidUtilities.lerp(AndroidUtilities.dp(12.0f), 0, this.keyboardT * (1.0f - forceRound()));
        if (isAtTop()) {
            float f3 = i3 + lerp;
            this.bounds.set(lerp, lerp, getWidth() - lerp, f3);
            this.clickBounds.set(lerp, lerp, getWidth() - lerp, f3 + AndroidUtilities.dp(24.0f));
        } else {
            float dpf2 = (AndroidUtilities.dpf2(-1.0f) * this.keyboardT) + f2;
            float f4 = i3;
            float f5 = dpf2 - f4;
            if (Math.abs(this.lastHeightTranslation - f5) >= 1.0f && !this.collapsed) {
                EditTextCaption editText = this.editText.getEditText();
                this.lastHeightTranslation = f5;
                editText.setTranslationY(f5);
            }
            this.bounds.set(lerp, (getHeight() - lerp) - f4, getWidth() - lerp, getHeight() - lerp);
            this.clickBounds.set(0.0f, (getHeight() - i3) - AndroidUtilities.dp(24.0f), getWidth(), getHeight());
        }
        canvas.save();
        float scale = this.bounce.getScale(0.018f);
        canvas.scale(scale, scale, this.bounds.centerX(), this.bounds.centerY());
        float lerp2 = AndroidUtilities.lerp(AndroidUtilities.dp(21.0f), 0, this.keyboardT * (1.0f - forceRound()));
        if (customBlur()) {
            i = 1;
            f = lerp2;
            drawBlur(this.backgroundBlur, canvas, this.bounds, lerp2, false, 0.0f, 0.0f, true, 1.0f);
            paint = this.backgroundPaint;
            i2 = AndroidUtilities.lerp(38, 64, this.keyboardT);
        } else {
            f = lerp2;
            i = 1;
            Paint[] paints = this.backgroundBlur.getPaints(1.0f, 0.0f, 0.0f);
            if (paints == null || paints[1] == null) {
                paint = this.backgroundPaint;
                i2 = 128;
            } else {
                Paint paint2 = paints[0];
                if (paint2 != null) {
                    canvas.drawRoundRect(this.bounds, f, f, paint2);
                }
                Paint paint3 = paints[1];
                if (paint3 != null) {
                    canvas.drawRoundRect(this.bounds, f, f, paint3);
                }
                paint = this.backgroundPaint;
                i2 = 51;
            }
        }
        paint.setAlpha(i2);
        canvas.drawRoundRect(this.bounds, f, f, this.backgroundPaint);
        float f6 = this.collapsedT.get();
        float f7 = this.collapsedT.set(this.collapsed);
        if (Math.abs(f6 - f7) <= 0.001f) {
        }
        invalidateDrawOver2();
        if (f7 > 0.0f) {
            canvas.saveLayerAlpha(this.bounds, NotificationCenter.liveLocationsChanged, 31);
        }
        drawReply(canvas);
        super.dispatchDraw(canvas);
        if (f7 > 0.0f) {
            int i4 = this.collapsedFromX;
            float dp2 = i4 == Integer.MAX_VALUE ? this.bounds.right - AndroidUtilities.dp(20.0f) : i4 == Integer.MIN_VALUE ? this.bounds.left + AndroidUtilities.dp(20.0f) : i4;
            float dp3 = this.bounds.bottom - AndroidUtilities.dp(20.0f);
            RectF rectF = this.bounds;
            float distance = MathUtils.distance(rectF.left, rectF.top, dp2, dp3);
            RectF rectF2 = this.bounds;
            float max = Math.max(distance, MathUtils.distance(rectF2.left, rectF2.bottom, dp2, dp3));
            RectF rectF3 = this.bounds;
            float distance2 = MathUtils.distance(rectF3.right, rectF3.top, dp2, dp3);
            RectF rectF4 = this.bounds;
            float max2 = Math.max(max, Math.max(distance2, MathUtils.distance(rectF4.right, rectF4.bottom, dp2, dp3))) * f7;
            if (this.collapsePaint == null) {
                Paint paint4 = new Paint(i);
                this.collapsePaint = paint4;
                PorterDuff.Mode mode = PorterDuff.Mode.DST_OUT;
                paint4.setXfermode(new PorterDuffXfermode(mode));
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                RadialGradient radialGradient = new RadialGradient(0.0f, 0.0f, 32.0f, new int[]{-1, -1, 0}, new float[]{0.0f, 0.6f, 1.0f}, tileMode);
                this.collapseGradient = radialGradient;
                this.collapsePaint.setShader(radialGradient);
                this.collapseGradientMatrix = new Matrix();
                Paint paint5 = new Paint(i);
                this.collapseOutPaint = paint5;
                paint5.setXfermode(new PorterDuffXfermode(mode));
                RadialGradient radialGradient2 = new RadialGradient(0.0f, 0.0f, 32.0f, new int[]{0, 0, -1}, new float[]{0.0f, 0.5f, 1.0f}, tileMode);
                this.collapseOutGradient = radialGradient2;
                this.collapseOutPaint.setShader(radialGradient2);
            }
            this.collapseGradientMatrix.reset();
            this.collapseGradientMatrix.postTranslate(dp2, dp3);
            this.collapseGradientMatrix.preScale(Math.max(1.0f, max2) / 16.0f, Math.max(1.0f, max2) / 16.0f);
            this.collapseGradient.setLocalMatrix(this.collapseGradientMatrix);
            canvas.save();
            canvas.drawRoundRect(this.bounds, f, f, this.collapsePaint);
            canvas.restore();
            canvas.restore();
            canvas.saveLayerAlpha(this.bounds, NotificationCenter.liveLocationsChanged, 31);
            drawOver(canvas, this.bounds);
            this.collapseGradientMatrix.reset();
            this.collapseGradientMatrix.postTranslate(dp2, dp3);
            this.collapseGradientMatrix.preScale(Math.max(1.0f, max2) / 16.0f, Math.max(1.0f, max2) / 16.0f);
            this.collapseOutGradient.setLocalMatrix(this.collapseGradientMatrix);
            canvas.save();
            canvas.drawRoundRect(this.bounds, f, f, this.collapseOutPaint);
            canvas.restore();
            canvas.restore();
            if (!drawOver2FromParent()) {
                drawOver2(canvas, this.bounds, f7);
            }
        }
        canvas.restore();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.ignoreTouches || ((motionEvent.getAction() == 0 && ignoreTouches(motionEvent.getX(), motionEvent.getY())) || !(this.clickBounds.contains(motionEvent.getX(), motionEvent.getY()) || this.keyboardShown))) {
            return false;
        }
        if (motionEvent.getAction() != 0 || this.keyboardShown) {
            if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                this.bounce.setPressed(false);
            }
            return super.dispatchTouchEvent(motionEvent);
        }
        if ((this instanceof CaptionStory) && ((CaptionStory) this).isRecording()) {
            return super.dispatchTouchEvent(motionEvent);
        }
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt != null && childAt.isClickable() && childAt.getVisibility() == 0 && childAt.getAlpha() >= 0.5f && this.editText != childAt) {
                this.rectF.set(childAt.getX(), childAt.getY(), childAt.getX() + childAt.getWidth(), childAt.getY() + childAt.getHeight());
                if (this.rectF.contains(motionEvent.getX(), motionEvent.getY())) {
                    return super.dispatchTouchEvent(motionEvent);
                }
            }
        }
        this.keyboardNotifier.ignore(false);
        this.editText.getEditText().setForceCursorEnd(true);
        this.editText.getEditText().requestFocus();
        this.editText.openKeyboard();
        this.editText.getEditText().setScrollY(0);
        this.bounce.setPressed(true);
        return true;
    }

    protected abstract void drawBlur(BlurringShader.StoryBlurDrawer storyBlurDrawer, Canvas canvas, RectF rectF, float f, boolean z, float f2, float f3, boolean z2, float f4);

    protected void drawBlurBitmap(Bitmap bitmap, float f) {
        Utilities.stackBlurBitmap(bitmap, (int) f);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        boolean drawChild;
        if (view == this.editText) {
            float max = isAtTop() ? 0.0f : Math.max(0, (this.editText.getHeight() - AndroidUtilities.dp(82.0f)) - this.editText.getScrollY()) * (1.0f - this.keyboardT);
            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), NotificationCenter.liveLocationsChanged, 31);
            canvas.save();
            canvas.clipRect(this.bounds);
            canvas.translate(0.0f, max);
            drawChild = super.drawChild(canvas, view, j);
            canvas.restore();
            canvas.save();
            this.matrix.reset();
            this.matrix.postTranslate(0.0f, this.bounds.top - 1.0f);
            this.fadeGradient.setLocalMatrix(this.matrix);
            RectF rectF = this.bounds;
            float f = rectF.left;
            float f2 = rectF.top;
            canvas.drawRect(f, f2, rectF.right, f2 + AndroidUtilities.dp(10.0f), this.fadePaint);
            this.matrix.reset();
            this.matrix.postRotate(180.0f);
            this.matrix.postTranslate(0.0f, this.bounds.bottom);
            this.fadeGradient.setLocalMatrix(this.matrix);
            RectF rectF2 = this.bounds;
            float f3 = rectF2.left;
            float dp = rectF2.bottom - AndroidUtilities.dp(10.0f);
            RectF rectF3 = this.bounds;
            canvas.drawRect(f3, dp, rectF3.right, rectF3.bottom, this.fadePaint);
            canvas.restore();
        } else {
            if (!clipChild(view)) {
                return super.drawChild(canvas, view, j);
            }
            canvas.save();
            canvas.clipRect(this.bounds);
            drawChild = super.drawChild(canvas, view, j);
        }
        canvas.restore();
        return drawChild;
    }

    public void drawOver(Canvas canvas, RectF rectF) {
    }

    public void drawOver2(Canvas canvas, RectF rectF, float f) {
    }

    public boolean drawOver2FromParent() {
        return false;
    }

    protected float forceRound() {
        return 0.0f;
    }

    public RectF getBounds() {
        return this.bounds;
    }

    protected int getCaptionDefaultLimit() {
        return 0;
    }

    protected int getCaptionLimit() {
        return UserConfig.getInstance(this.currentAccount).isPremium() ? getCaptionPremiumLimit() : getCaptionDefaultLimit();
    }

    protected int getCaptionPremiumLimit() {
        return 0;
    }

    public int getCodePointCount() {
        return this.codePointCount;
    }

    public int getEditTextHeight() {
        return (int) this.heightAnimated.get();
    }

    public int getEditTextHeightClosedKeyboard() {
        return Math.min(AndroidUtilities.dp(82.0f), this.editText.getHeight());
    }

    protected int getEditTextLeft() {
        return 0;
    }

    protected int getEditTextStyle() {
        return 2;
    }

    public float getOver2Alpha() {
        return this.collapsedT.get();
    }

    public int getSelectionLength() {
        EditTextEmoji editTextEmoji = this.editText;
        if (editTextEmoji != null && editTextEmoji.getEditText() != null) {
            try {
                return this.editText.getEditText().getSelectionEnd() - this.editText.getEditText().getSelectionStart();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return 0;
    }

    public CharSequence getText() {
        return this.editText.getText();
    }

    protected abstract boolean ignoreTouches(float f, float f2);

    public void invalidateBlur() {
        invalidate();
        this.editText.getEditText().invalidate();
        this.editText.getEmojiButton().invalidate();
        MentionsContainerView mentionsContainerView = this.mentionContainer;
        if (mentionsContainerView != null) {
            mentionsContainerView.invalidate();
        }
        if (this.editText.getEmojiView() == null || !customBlur()) {
            return;
        }
        this.editText.getEmojiView().invalidate();
    }

    public void invalidateDrawOver2() {
    }

    protected boolean isAtTop() {
        return false;
    }

    public boolean isCaptionOverLimit() {
        return getCodePointCount() > getCaptionLimit();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (customBlur()) {
            Bitmap bitmap = this.hintTextBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.hintTextBitmap = null;
            }
            this.hintTextPaint.setColor(-16777216);
            this.hintTextPaint.setTextSize(AndroidUtilities.dp(16.0f));
            String string = LocaleController.getString(R.string.AddCaption);
            this.hintTextBitmap = Bitmap.createBitmap((int) Math.ceil(this.hintTextPaint.measureText(string)), (int) Math.ceil(this.hintTextPaint.getFontMetrics().descent - this.hintTextPaint.getFontMetrics().ascent), Bitmap.Config.ARGB_8888);
            new Canvas(this.hintTextBitmap).drawText(string, 0.0f, -((int) this.hintTextPaint.getFontMetrics().ascent), this.hintTextPaint);
        }
    }

    public boolean onBackPressed() {
        EditTextEmoji editTextEmoji = this.editText;
        if (editTextEmoji.emojiExpanded && editTextEmoji.getEmojiView() != null) {
            if (this.keyboardNotifier.keyboardVisible()) {
                this.editText.getEmojiView().hideSearchKeyboard();
            } else {
                this.editText.collapseEmojiView();
            }
            return true;
        }
        if (this.editText.isPopupShowing()) {
            this.editText.hidePopup(true);
            return true;
        }
        if ((!this.editText.isKeyboardVisible() && !this.keyboardNotifier.keyboardVisible()) || this.keyboardNotifier.ignoring) {
            return false;
        }
        closeKeyboard();
        return true;
    }

    protected void onCaptionLimitUpdate(boolean z) {
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Bitmap bitmap = this.blurBitmap;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.blurBitmapShader = null;
        this.blurPaint = null;
        Bitmap bitmap2 = this.hintTextBitmap;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.hintTextBitmap = null;
        }
    }

    protected void onEditHeightChange(int i) {
    }

    public void onPause() {
        this.editText.onPause();
    }

    public void onResume() {
        this.editText.onResume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onTextChange, reason: merged with bridge method [inline-methods] */
    public void lambda$new$1() {
    }

    protected abstract void onUpdateShowKeyboard(float f);

    public void setAccount(int i) {
        this.currentAccount = i;
    }

    public void setCollapsed(boolean z, int i) {
        this.collapsed = z;
        this.collapsedFromX = i;
        invalidate();
    }

    public void setDialogId(long j) {
        this.dialogId = j;
    }

    public void setOnHeightUpdate(Utilities.Callback<Integer> callback) {
        this.onHeightUpdate = callback;
    }

    public void setOnKeyboardOpen(Utilities.Callback<Boolean> callback) {
        this.onKeyboardOpen = callback;
    }

    @Override // android.view.View
    public void setPressed(boolean z) {
        super.setPressed(z);
        this.bounce.setPressed(z && !this.keyboardShown);
    }

    public void setReply(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == null && charSequence2 == null) {
            this.hasReply = false;
            invalidate();
            return;
        }
        this.hasReply = true;
        if (charSequence == null) {
            charSequence = "";
        }
        this.replyTitle = new Text(charSequence, 14.0f, AndroidUtilities.bold());
        if (charSequence2 == null) {
            charSequence2 = "";
        }
        this.replyText = new Text(charSequence2, 14.0f);
    }

    public void setText(CharSequence charSequence) {
        this.ignoreTextChange = true;
        this.editText.setText(charSequence);
    }

    public void setUiBlurBitmap(Utilities.CallbackVoidReturn<Bitmap> callbackVoidReturn) {
        this.getUiBlurBitmap = callbackVoidReturn;
    }

    protected void setupMentionContainer() {
        this.mentionContainer.getAdapter().setAllowStickers(false);
        this.mentionContainer.getAdapter().setAllowBots(false);
        this.mentionContainer.getAdapter().setAllowChats(false);
        this.mentionContainer.getAdapter().setSearchInDailogs(true);
    }

    public void updateColors(Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
        this.applyButtonCheck.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogFloatingIcon), PorterDuff.Mode.SRC_IN));
        this.applyButtonDrawable.setBackgroundDrawable(Theme.createCircleDrawable(AndroidUtilities.dp(16.0f), Theme.getColor(Theme.key_chat_editMediaButton, resourcesProvider)));
    }

    protected void updateEditTextLeft() {
        this.editText.getEditText().setTranslationX(AndroidUtilities.lerp(AndroidUtilities.dp(-22.0f) + getEditTextLeft(), AndroidUtilities.dp(2.0f), this.keyboardT));
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x003a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateKeyboard(int i) {
        int additionalKeyboardHeight;
        int keyboardHeight;
        int max;
        ObjectAnimator objectAnimator;
        long j;
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierFrameLayout;
        if (sizeNotifierFrameLayout != null) {
            sizeNotifierFrameLayout.notifyHeightChanged();
        }
        if (!this.editText.isPopupShowing()) {
            if (this.editText.isWaitingForKeyboardOpen()) {
                additionalKeyboardHeight = additionalKeyboardHeight();
                keyboardHeight = this.editText.getKeyboardHeight();
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout2 = this.sizeNotifierFrameLayout;
            max = Math.max(0, i - (sizeNotifierFrameLayout2 != null ? 0 : sizeNotifierFrameLayout2.getBottomPadding()));
            View view = (View) getParent();
            view.clearAnimation();
            if (!isAtTop()) {
                ObjectAnimator objectAnimator2 = this.parentKeyboardAnimator;
                if (objectAnimator2 != null) {
                    objectAnimator2.removeAllListeners();
                    this.parentKeyboardAnimator.cancel();
                    this.parentKeyboardAnimator = null;
                }
                this.parentKeyboardAnimator = ObjectAnimator.ofFloat(view, (Property<View, Float>) FrameLayout.TRANSLATION_Y, view.getTranslationY(), -max);
                if (max > AndroidUtilities.dp(20.0f)) {
                    this.parentKeyboardAnimator.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                    objectAnimator = this.parentKeyboardAnimator;
                    j = 250;
                } else {
                    this.parentKeyboardAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                    objectAnimator = this.parentKeyboardAnimator;
                    j = 640;
                }
                objectAnimator.setDuration(j);
                this.parentKeyboardAnimator.start();
            }
            this.toKeyboardShow = max > AndroidUtilities.dp(20.0f);
            AndroidUtilities.cancelRunOnUIThread(this.updateShowKeyboard);
            AndroidUtilities.runOnUIThread(this.updateShowKeyboard);
            if (max >= AndroidUtilities.dp(20.0f)) {
                this.editText.getEditText().clearFocus();
                this.editText.hidePopup(true);
                return;
            }
            return;
        }
        additionalKeyboardHeight = additionalKeyboardHeight();
        keyboardHeight = this.editText.getEmojiPadding();
        i = Math.max(0, additionalKeyboardHeight + keyboardHeight);
        SizeNotifierFrameLayout sizeNotifierFrameLayout22 = this.sizeNotifierFrameLayout;
        max = Math.max(0, i - (sizeNotifierFrameLayout22 != null ? 0 : sizeNotifierFrameLayout22.getBottomPadding()));
        View view2 = (View) getParent();
        view2.clearAnimation();
        if (!isAtTop()) {
        }
        this.toKeyboardShow = max > AndroidUtilities.dp(20.0f);
        AndroidUtilities.cancelRunOnUIThread(this.updateShowKeyboard);
        AndroidUtilities.runOnUIThread(this.updateShowKeyboard);
        if (max >= AndroidUtilities.dp(20.0f)) {
        }
    }

    public void updateMentionsLayoutPosition() {
        if (this.mentionContainer != null) {
            float translationY = ((View) getParent()).getTranslationY() - this.heightAnimated.get();
            if (this.mentionContainer.getY() != translationY) {
                this.mentionContainer.setTranslationY(translationY);
                this.mentionContainer.invalidate();
            }
        }
    }
}
