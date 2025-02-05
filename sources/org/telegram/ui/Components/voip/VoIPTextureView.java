package org.telegram.ui.Components.voip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.os.Build;
import android.view.TextureView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.webrtc.RendererCommon;
import org.webrtc.TextureViewRenderer;

/* loaded from: classes3.dex */
public class VoIPTextureView extends FrameLayout {
    public static int SCALE_TYPE_ADAPTIVE = 2;
    public static int SCALE_TYPE_FILL = 0;
    public static int SCALE_TYPE_FIT = 1;
    public static int SCALE_TYPE_NONE = 3;
    int animateFromHeight;
    float animateFromRendererH;
    float animateFromRendererW;
    float animateFromThumbScale;
    int animateFromWidth;
    float animateFromX;
    float animateFromY;
    long animateNextDuration;
    boolean animateOnNextLayout;
    ArrayList animateOnNextLayoutAnimations;
    boolean animateWithParent;
    public float animationProgress;
    float aninateFromScale;
    float aninateFromScaleBlur;
    final boolean applyRotation;
    public View backgroundView;
    public TextureView blurRenderer;
    public Bitmap cameraLastBitmap;
    float clipHorizontal;
    boolean clipToTexture;
    float clipVertical;
    ValueAnimator currentAnimation;
    float currentClipHorizontal;
    float currentClipVertical;
    float currentThumbScale;
    boolean ignoreLayout;
    public final ImageView imageView;
    final boolean isCamera;
    public final TextureViewRenderer renderer;
    float roundRadius;
    public float scaleTextureToFill;
    private float scaleTextureToFillBlur;
    private float scaleThumb;
    public int scaleType;
    private boolean screencast;
    private ImageView screencastImage;
    private TextView screencastText;
    private FrameLayout screencastView;
    public float stubVisibleProgress;
    private Bitmap thumb;

    public VoIPTextureView(Context context, boolean z, boolean z2) {
        this(context, z, z2, true, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0191 A[Catch: all -> 0x01a7, TryCatch #0 {all -> 0x01a7, blocks: (B:21:0x0179, B:23:0x0191, B:24:0x01a9), top: B:20:0x0179 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01b5  */
    /* JADX WARN: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public VoIPTextureView(Context context, boolean z, boolean z2, boolean z3, boolean z4) {
        super(context);
        TextureView textureView;
        Bitmap decodeFile;
        this.stubVisibleProgress = 1.0f;
        this.animateOnNextLayoutAnimations = new ArrayList();
        this.aninateFromScale = 1.0f;
        this.aninateFromScaleBlur = 1.0f;
        this.animateFromThumbScale = 1.0f;
        this.isCamera = z;
        this.applyRotation = z2;
        ImageView imageView = new ImageView(context);
        this.imageView = imageView;
        TextureViewRenderer textureViewRenderer = new TextureViewRenderer(context) { // from class: org.telegram.ui.Components.voip.VoIPTextureView.1
            @Override // org.webrtc.TextureViewRenderer, org.webrtc.RendererCommon.RendererEvents
            public void onFirstFrameRendered() {
                super.onFirstFrameRendered();
                VoIPTextureView.this.onFirstFrameRendered();
            }

            @Override // android.view.TextureView, android.view.View
            protected void onSizeChanged(int i, int i2, int i3, int i4) {
                super.onSizeChanged(i, i2, i3, i4);
            }
        };
        this.renderer = textureViewRenderer;
        textureViewRenderer.setFpsReduction(30.0f);
        textureViewRenderer.setOpaque(false);
        textureViewRenderer.setEnableHardwareScaler(true);
        textureViewRenderer.setIsCamera(!z2);
        if (!z && z2) {
            View view = new View(context);
            this.backgroundView = view;
            view.setBackgroundColor(-14999773);
            addView(this.backgroundView, LayoutHelper.createFrame(-1, -1.0f));
            if (z4) {
                TextureView textureView2 = new TextureView(context);
                this.blurRenderer = textureView2;
                addView(textureView2, LayoutHelper.createFrame(-1, -2, 17));
            }
            textureViewRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        } else {
            if (z) {
                if (z4) {
                    TextureView textureView3 = new TextureView(context);
                    this.blurRenderer = textureView3;
                    addView(textureView3, LayoutHelper.createFrame(-1, -2, 17));
                }
                addView(textureViewRenderer);
                addView(imageView);
                textureView = this.blurRenderer;
                if (textureView != null) {
                    textureView.setOpaque(false);
                }
                FrameLayout frameLayout = new FrameLayout(getContext());
                this.screencastView = frameLayout;
                frameLayout.setBackground(new MotionBackgroundDrawable(-14602694, -13935795, -14395293, -14203560, true));
                addView(this.screencastView, LayoutHelper.createFrame(-1, -1.0f));
                this.screencastView.setVisibility(8);
                ImageView imageView2 = new ImageView(getContext());
                this.screencastImage = imageView2;
                imageView2.setScaleType(ImageView.ScaleType.CENTER);
                this.screencastImage.setImageResource(R.drawable.screencast_big);
                this.screencastView.addView(this.screencastImage, LayoutHelper.createFrame(82, 82.0f, 17, 0.0f, 0.0f, 0.0f, 60.0f));
                TextView textView = new TextView(getContext());
                this.screencastText = textView;
                textView.setText(LocaleController.getString(R.string.VoipVideoScreenSharing));
                this.screencastText.setGravity(17);
                this.screencastText.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                this.screencastText.setTextColor(-1);
                this.screencastText.setTextSize(1, 15.0f);
                this.screencastText.setTypeface(AndroidUtilities.bold());
                this.screencastView.addView(this.screencastText, LayoutHelper.createFrame(-1, -2.0f, 17, 21.0f, 28.0f, 21.0f, 0.0f));
                if (z3 && Build.VERSION.SDK_INT >= 21) {
                    setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.Components.voip.VoIPTextureView.2
                        @Override // android.view.ViewOutlineProvider
                        public void getOutline(View view2, Outline outline) {
                            VoIPTextureView voIPTextureView = VoIPTextureView.this;
                            if (voIPTextureView.roundRadius < 1.0f) {
                                outline.setRect((int) voIPTextureView.currentClipHorizontal, (int) voIPTextureView.currentClipVertical, (int) (view2.getMeasuredWidth() - VoIPTextureView.this.currentClipHorizontal), (int) (view2.getMeasuredHeight() - VoIPTextureView.this.currentClipVertical));
                                return;
                            }
                            int i = (int) voIPTextureView.currentClipHorizontal;
                            int i2 = (int) voIPTextureView.currentClipVertical;
                            int measuredWidth = (int) (view2.getMeasuredWidth() - VoIPTextureView.this.currentClipHorizontal);
                            float measuredHeight = view2.getMeasuredHeight();
                            VoIPTextureView voIPTextureView2 = VoIPTextureView.this;
                            outline.setRoundRect(i, i2, measuredWidth, (int) (measuredHeight - voIPTextureView2.currentClipVertical), voIPTextureView2.roundRadius);
                        }
                    });
                    setClipToOutline(true);
                }
                if (z && this.cameraLastBitmap == null) {
                    try {
                        decodeFile = BitmapFactory.decodeFile(new File(ApplicationLoader.getFilesDirFixed(), "voip_icthumb.jpg").getAbsolutePath());
                        this.cameraLastBitmap = decodeFile;
                        if (decodeFile == null) {
                            this.cameraLastBitmap = BitmapFactory.decodeFile(new File(ApplicationLoader.getFilesDirFixed(), "icthumb.jpg").getAbsolutePath());
                        }
                        imageView.setImageBitmap(this.cameraLastBitmap);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    } catch (Throwable unused) {
                    }
                }
                if (z2) {
                    this.renderer.setScreenRotation(((WindowManager) getContext().getSystemService("window")).getDefaultDisplay().getRotation());
                    return;
                }
                return;
            }
            if (z4) {
                TextureView textureView4 = new TextureView(context);
                this.blurRenderer = textureView4;
                addView(textureView4, LayoutHelper.createFrame(-1, -2, 17));
            }
        }
        addView(textureViewRenderer, LayoutHelper.createFrame(-1, -2, 17));
        addView(imageView);
        textureView = this.blurRenderer;
        if (textureView != null) {
        }
        FrameLayout frameLayout2 = new FrameLayout(getContext());
        this.screencastView = frameLayout2;
        frameLayout2.setBackground(new MotionBackgroundDrawable(-14602694, -13935795, -14395293, -14203560, true));
        addView(this.screencastView, LayoutHelper.createFrame(-1, -1.0f));
        this.screencastView.setVisibility(8);
        ImageView imageView22 = new ImageView(getContext());
        this.screencastImage = imageView22;
        imageView22.setScaleType(ImageView.ScaleType.CENTER);
        this.screencastImage.setImageResource(R.drawable.screencast_big);
        this.screencastView.addView(this.screencastImage, LayoutHelper.createFrame(82, 82.0f, 17, 0.0f, 0.0f, 0.0f, 60.0f));
        TextView textView2 = new TextView(getContext());
        this.screencastText = textView2;
        textView2.setText(LocaleController.getString(R.string.VoipVideoScreenSharing));
        this.screencastText.setGravity(17);
        this.screencastText.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        this.screencastText.setTextColor(-1);
        this.screencastText.setTextSize(1, 15.0f);
        this.screencastText.setTypeface(AndroidUtilities.bold());
        this.screencastView.addView(this.screencastText, LayoutHelper.createFrame(-1, -2.0f, 17, 21.0f, 28.0f, 21.0f, 0.0f));
        if (z3) {
            setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.Components.voip.VoIPTextureView.2
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view2, Outline outline) {
                    VoIPTextureView voIPTextureView = VoIPTextureView.this;
                    if (voIPTextureView.roundRadius < 1.0f) {
                        outline.setRect((int) voIPTextureView.currentClipHorizontal, (int) voIPTextureView.currentClipVertical, (int) (view2.getMeasuredWidth() - VoIPTextureView.this.currentClipHorizontal), (int) (view2.getMeasuredHeight() - VoIPTextureView.this.currentClipVertical));
                        return;
                    }
                    int i = (int) voIPTextureView.currentClipHorizontal;
                    int i2 = (int) voIPTextureView.currentClipVertical;
                    int measuredWidth = (int) (view2.getMeasuredWidth() - VoIPTextureView.this.currentClipHorizontal);
                    float measuredHeight = view2.getMeasuredHeight();
                    VoIPTextureView voIPTextureView2 = VoIPTextureView.this;
                    outline.setRoundRect(i, i2, measuredWidth, (int) (measuredHeight - voIPTextureView2.currentClipVertical), voIPTextureView2.roundRadius);
                }
            });
            setClipToOutline(true);
        }
        if (z) {
            decodeFile = BitmapFactory.decodeFile(new File(ApplicationLoader.getFilesDirFixed(), "voip_icthumb.jpg").getAbsolutePath());
            this.cameraLastBitmap = decodeFile;
            if (decodeFile == null) {
            }
            imageView.setImageBitmap(this.cameraLastBitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        if (z2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLayout$0(float f, float f2, float f3, float f4, float f5, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        float f6 = 1.0f - floatValue;
        this.animationProgress = f6;
        this.currentClipVertical = this.clipVertical * floatValue;
        this.currentClipHorizontal = this.clipHorizontal * floatValue;
        if (Build.VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
        invalidate();
        float f7 = (f * floatValue) + (this.scaleTextureToFill * f6);
        this.renderer.setScaleX(f7);
        this.renderer.setScaleY(f7);
        float f8 = (f2 * floatValue) + (this.scaleTextureToFillBlur * f6);
        TextureView textureView = this.blurRenderer;
        if (textureView != null) {
            textureView.setScaleX(f8);
            this.blurRenderer.setScaleY(f8);
        }
        setTranslationX(f3 * floatValue);
        setTranslationY(f4 * floatValue);
        this.currentThumbScale = (f5 * floatValue) + (this.scaleThumb * f6);
    }

    public void animateToLayout() {
        float x;
        if (this.animateOnNextLayout || getMeasuredHeight() == 0 || getMeasuredWidth() == 0) {
            return;
        }
        this.animateFromHeight = getMeasuredHeight();
        this.animateFromWidth = getMeasuredWidth();
        if (!this.animateWithParent || getParent() == null) {
            this.animateFromY = getY();
            x = getX();
        } else {
            View view = (View) getParent();
            this.animateFromY = view.getY();
            x = view.getX();
        }
        this.animateFromX = x;
        this.aninateFromScale = this.scaleTextureToFill;
        this.aninateFromScaleBlur = this.scaleTextureToFillBlur;
        this.animateFromThumbScale = this.scaleThumb;
        this.animateFromRendererW = this.renderer.getMeasuredWidth();
        this.animateFromRendererH = this.renderer.getMeasuredHeight();
        this.animateOnNextLayout = true;
        requestLayout();
    }

    public void attachBackgroundRenderer() {
        TextureView textureView = this.blurRenderer;
        if (textureView != null) {
            this.renderer.setBackgroundRenderer(textureView);
            if (this.renderer.isFirstFrameRendered()) {
                return;
            }
            this.blurRenderer.setAlpha(0.0f);
        }
    }

    public void cancelAnimation() {
        this.animateOnNextLayout = false;
        this.animateNextDuration = 0L;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.imageView.getVisibility() == 0 && this.renderer.isFirstFrameRendered()) {
            float f = this.stubVisibleProgress - 0.10666667f;
            this.stubVisibleProgress = f;
            if (f <= 0.0f) {
                this.stubVisibleProgress = 0.0f;
                this.imageView.setVisibility(8);
            } else {
                invalidate();
                this.imageView.setAlpha(this.stubVisibleProgress);
            }
        }
    }

    public boolean isInAnimation() {
        return this.currentAnimation != null;
    }

    protected void onFirstFrameRendered() {
        invalidate();
        if (this.renderer.getAlpha() != 1.0f) {
            this.renderer.animate().setDuration(300L).alpha(1.0f);
        }
        TextureView textureView = this.blurRenderer;
        if (textureView == null || textureView.getAlpha() == 1.0f) {
            return;
        }
        this.blurRenderer.animate().setDuration(300L).alpha(1.0f);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float top;
        float left;
        float measuredHeight;
        float measuredWidth;
        float f;
        super.onLayout(z, i, i2, i3, i4);
        if (this.blurRenderer != null) {
            this.scaleTextureToFillBlur = Math.max(getMeasuredHeight() / this.blurRenderer.getMeasuredHeight(), getMeasuredWidth() / this.blurRenderer.getMeasuredWidth());
        }
        if (!this.applyRotation) {
            this.renderer.updateRotation();
        }
        if (this.scaleType == SCALE_TYPE_NONE) {
            TextureView textureView = this.blurRenderer;
            if (textureView != null) {
                textureView.setScaleX(this.scaleTextureToFillBlur);
                this.blurRenderer.setScaleY(this.scaleTextureToFillBlur);
                return;
            }
            return;
        }
        if (this.renderer.getMeasuredHeight() == 0 || this.renderer.getMeasuredWidth() == 0 || getMeasuredHeight() == 0 || getMeasuredWidth() == 0) {
            this.scaleTextureToFill = 1.0f;
            if (this.currentAnimation == null && !this.animateOnNextLayout) {
                this.currentClipHorizontal = 0.0f;
                this.currentClipVertical = 0.0f;
            }
        } else {
            int i5 = this.scaleType;
            if (i5 != SCALE_TYPE_FILL) {
                if (i5 == SCALE_TYPE_ADAPTIVE) {
                    if (Math.abs((getMeasuredHeight() / getMeasuredWidth()) - 1.0f) >= 0.02f) {
                        if (getMeasuredWidth() <= getMeasuredHeight() || this.renderer.getMeasuredHeight() <= this.renderer.getMeasuredWidth()) {
                            f = Math.min(getMeasuredHeight() / this.renderer.getMeasuredHeight(), getMeasuredWidth() / this.renderer.getMeasuredWidth());
                            this.scaleTextureToFill = f;
                        } else {
                            measuredHeight = getMeasuredHeight() / this.renderer.getMeasuredHeight();
                            measuredWidth = getMeasuredWidth() / 2.0f;
                            f = Math.max(measuredHeight, measuredWidth / this.renderer.getMeasuredWidth());
                            this.scaleTextureToFill = f;
                        }
                    }
                } else if (i5 == SCALE_TYPE_FIT) {
                    this.scaleTextureToFill = Math.min(getMeasuredHeight() / this.renderer.getMeasuredHeight(), getMeasuredWidth() / this.renderer.getMeasuredWidth());
                    if (this.clipToTexture && !this.animateWithParent && this.currentAnimation == null && !this.animateOnNextLayout) {
                        this.currentClipHorizontal = (getMeasuredWidth() - this.renderer.getMeasuredWidth()) / 2.0f;
                        this.currentClipVertical = (getMeasuredHeight() - this.renderer.getMeasuredHeight()) / 2.0f;
                        if (Build.VERSION.SDK_INT >= 21) {
                            invalidateOutline();
                        }
                    }
                }
            }
            measuredHeight = getMeasuredHeight() / this.renderer.getMeasuredHeight();
            measuredWidth = getMeasuredWidth();
            f = Math.max(measuredHeight, measuredWidth / this.renderer.getMeasuredWidth());
            this.scaleTextureToFill = f;
        }
        if (this.thumb != null) {
            this.scaleThumb = Math.max(getMeasuredWidth() / this.thumb.getWidth(), getMeasuredHeight() / this.thumb.getHeight());
        }
        if (!this.animateOnNextLayout) {
            if (this.currentAnimation == null) {
                this.renderer.setScaleX(this.scaleTextureToFill);
                this.renderer.setScaleY(this.scaleTextureToFill);
                TextureView textureView2 = this.blurRenderer;
                if (textureView2 != null) {
                    textureView2.setScaleX(this.scaleTextureToFillBlur);
                    this.blurRenderer.setScaleY(this.scaleTextureToFillBlur);
                }
                this.currentThumbScale = this.scaleThumb;
                return;
            }
            return;
        }
        this.aninateFromScale /= this.renderer.getMeasuredWidth() / this.animateFromRendererW;
        this.aninateFromScaleBlur /= this.renderer.getMeasuredWidth() / this.animateFromRendererW;
        this.animateOnNextLayout = false;
        if (!this.animateWithParent || getParent() == null) {
            top = this.animateFromY - getTop();
            left = this.animateFromX - getLeft();
        } else {
            View view = (View) getParent();
            top = this.animateFromY - view.getTop();
            left = this.animateFromX - view.getLeft();
        }
        this.clipVertical = 0.0f;
        this.clipHorizontal = 0.0f;
        if (this.animateFromHeight != getMeasuredHeight()) {
            float measuredHeight2 = (getMeasuredHeight() - this.animateFromHeight) / 2.0f;
            this.clipVertical = measuredHeight2;
            top -= measuredHeight2;
        }
        final float f2 = top;
        if (this.animateFromWidth != getMeasuredWidth()) {
            float measuredWidth2 = (getMeasuredWidth() - this.animateFromWidth) / 2.0f;
            this.clipHorizontal = measuredWidth2;
            left -= measuredWidth2;
        }
        final float f3 = left;
        setTranslationY(f2);
        setTranslationX(f3);
        ValueAnimator valueAnimator = this.currentAnimation;
        if (valueAnimator != null) {
            valueAnimator.removeAllListeners();
            this.currentAnimation.cancel();
        }
        this.renderer.setScaleX(this.aninateFromScale);
        this.renderer.setScaleY(this.aninateFromScale);
        TextureView textureView3 = this.blurRenderer;
        if (textureView3 != null) {
            textureView3.setScaleX(this.aninateFromScaleBlur);
            this.blurRenderer.setScaleY(this.aninateFromScaleBlur);
        }
        this.currentClipVertical = this.clipVertical;
        this.currentClipHorizontal = this.clipHorizontal;
        if (Build.VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
        invalidate();
        final float f4 = this.aninateFromScale;
        final float f5 = this.aninateFromScaleBlur;
        final float f6 = this.animateFromThumbScale;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        this.currentAnimation = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.voip.VoIPTextureView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                VoIPTextureView.this.lambda$onLayout$0(f4, f5, f3, f2, f6, valueAnimator2);
            }
        });
        long j = this.animateNextDuration;
        if (j != 0) {
            this.currentAnimation.setDuration(j);
        } else {
            this.currentAnimation.setDuration(350L);
        }
        this.currentAnimation.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.VoIPTextureView.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                VoIPTextureView voIPTextureView = VoIPTextureView.this;
                voIPTextureView.currentClipVertical = 0.0f;
                voIPTextureView.currentClipHorizontal = 0.0f;
                voIPTextureView.renderer.setScaleX(voIPTextureView.scaleTextureToFill);
                VoIPTextureView voIPTextureView2 = VoIPTextureView.this;
                voIPTextureView2.renderer.setScaleY(voIPTextureView2.scaleTextureToFill);
                VoIPTextureView voIPTextureView3 = VoIPTextureView.this;
                TextureView textureView4 = voIPTextureView3.blurRenderer;
                if (textureView4 != null) {
                    textureView4.setScaleX(voIPTextureView3.scaleTextureToFillBlur);
                    VoIPTextureView voIPTextureView4 = VoIPTextureView.this;
                    voIPTextureView4.blurRenderer.setScaleY(voIPTextureView4.scaleTextureToFillBlur);
                }
                VoIPTextureView.this.setTranslationY(0.0f);
                VoIPTextureView.this.setTranslationX(0.0f);
                VoIPTextureView voIPTextureView5 = VoIPTextureView.this;
                voIPTextureView5.currentThumbScale = voIPTextureView5.scaleThumb;
                VoIPTextureView.this.currentAnimation = null;
            }
        });
        this.currentAnimation.start();
        if (!this.animateOnNextLayoutAnimations.isEmpty()) {
            for (int i6 = 0; i6 < this.animateOnNextLayoutAnimations.size(); i6++) {
                ((Animator) this.animateOnNextLayoutAnimations.get(i6)).start();
            }
        }
        this.animateOnNextLayoutAnimations.clear();
        this.animateNextDuration = 0L;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (!this.applyRotation) {
            this.ignoreLayout = true;
            this.renderer.setScreenRotation(((WindowManager) getContext().getSystemService("window")).getDefaultDisplay().getRotation());
            this.ignoreLayout = false;
        }
        super.onMeasure(i, i2);
        updateRendererSize();
        super.onMeasure(i, i2);
        this.renderer.updateRotation();
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    public void saveCameraLastBitmap() {
        Bitmap bitmap = this.renderer.getBitmap(150, 150);
        if (bitmap == null || bitmap.getPixel(0, 0) == 0) {
            return;
        }
        Utilities.blurBitmap(bitmap, 3, 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(ApplicationLoader.getFilesDirFixed(), "voip_icthumb.jpg"));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Throwable unused) {
        }
    }

    public void setAnimateNextDuration(long j) {
        this.animateNextDuration = j;
    }

    public void setAnimateWithParent(boolean z) {
        this.animateWithParent = z;
    }

    public void setIsScreencast(boolean z) {
        this.screencast = z;
        this.screencastView.setVisibility(z ? 0 : 8);
        if (this.screencast) {
            this.renderer.setVisibility(8);
            TextureView textureView = this.blurRenderer;
            if (textureView != null) {
                textureView.setVisibility(8);
            }
            this.imageView.setVisibility(8);
            return;
        }
        this.renderer.setVisibility(0);
        TextureView textureView2 = this.blurRenderer;
        if (textureView2 != null) {
            textureView2.setVisibility(0);
        }
    }

    public void setRoundCorners(float f) {
        if (this.roundRadius != f) {
            this.roundRadius = f;
            if (Build.VERSION.SDK_INT >= 21) {
                invalidateOutline();
            } else {
                invalidate();
            }
        }
    }

    public void setScreenshareMiniProgress(float f, boolean z) {
        float f2;
        if (this.screencast) {
            float scaleX = ((View) getParent()).getScaleX();
            float f3 = 1.0f;
            this.screencastText.setAlpha(1.0f - f);
            if (z) {
                f2 = 0.4f * f;
            } else {
                f3 = 1.0f / scaleX;
                f2 = (0.4f / scaleX) * f;
            }
            float f4 = f3 - f2;
            this.screencastImage.setScaleX(f4);
            this.screencastImage.setScaleY(f4);
            this.screencastImage.setTranslationY(AndroidUtilities.dp(60.0f) * f);
        }
    }

    public void setStub(VoIPTextureView voIPTextureView) {
        if (this.screencast) {
            return;
        }
        Bitmap bitmap = voIPTextureView.renderer.getBitmap();
        if (bitmap == null || bitmap.getPixel(0, 0) == 0) {
            this.imageView.setImageDrawable(voIPTextureView.imageView.getDrawable());
        } else {
            this.imageView.setImageBitmap(bitmap);
            this.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        this.stubVisibleProgress = 1.0f;
        this.imageView.setVisibility(0);
        this.imageView.setAlpha(1.0f);
    }

    public void setThumb(Bitmap bitmap) {
        this.thumb = bitmap;
    }

    public void synchOrRunAnimation(Animator animator) {
        if (this.animateOnNextLayout) {
            this.animateOnNextLayoutAnimations.add(animator);
        } else {
            animator.start();
        }
    }

    protected void updateRendererSize() {
        TextureView textureView = this.blurRenderer;
        if (textureView != null) {
            textureView.getLayoutParams().width = this.renderer.getMeasuredWidth();
            this.blurRenderer.getLayoutParams().height = this.renderer.getMeasuredHeight();
        }
    }

    public void updateRotation() {
        if (this.applyRotation) {
            return;
        }
        ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay();
    }
}
