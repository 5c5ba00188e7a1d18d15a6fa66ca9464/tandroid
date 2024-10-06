package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RecordingCanvas;
import android.graphics.Region;
import android.graphics.RenderEffect;
import android.graphics.RenderNode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.text.MeasuredText;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.BlurSettingsBottomSheet;
import org.telegram.ui.ChatBackgroundDrawable;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.WallpaperParallaxEffect;
/* loaded from: classes3.dex */
public class SizeNotifierFrameLayout extends FrameLayout {
    private static DispatchQueue blurQueue;
    public static boolean drawingBlur;
    private final float DOWN_SCALE;
    private final int TOP_CLIP_OFFSET;
    public AdjustPanLayoutHelper adjustPanLayoutHelper;
    private boolean animationInProgress;
    boolean attached;
    private Drawable backgroundDrawable;
    private boolean backgroundMotion;
    private int backgroundTranslationY;
    protected View backgroundView;
    private float bgAngle;
    final BlurBackgroundTask blurBackgroundTask;
    public ArrayList blurBehindViews;
    ValueAnimator blurCrossfade;
    public float blurCrossfadeProgress;
    public boolean blurGeneratingTuskIsRunning;
    public boolean blurIsRunning;
    private boolean[] blurNodeInvalidated;
    private boolean[] blurNodeInvalidatedThisFrame;
    private RenderNode[] blurNodes;
    public Paint blurPaintBottom;
    public Paint blurPaintBottom2;
    public Paint blurPaintTop;
    public Paint blurPaintTop2;
    private int bottomClip;
    int count;
    int count2;
    BlurBitmap currentBitmap;
    protected SizeNotifierFrameLayoutDelegate delegate;
    private float drawnBottomOffset;
    private int emojiHeight;
    private float emojiOffset;
    public boolean invalidateBlur;
    protected int keyboardHeight;
    private float lastDrawnBottomBlurOffset;
    private final ArrayList lastViews;
    Matrix matrix;
    Matrix matrix2;
    public boolean needBlur;
    public boolean needBlurBottom;
    private boolean occupyStatusBar;
    private Drawable oldBackgroundDrawable;
    private boolean oldBackgroundMotion;
    private WallpaperParallaxEffect parallaxEffect;
    private float parallaxScale;
    private INavigationLayout parentLayout;
    private boolean paused;
    BlurBitmap prevBitmap;
    private android.graphics.Rect rect;
    private Paint selectedBlurPaint;
    private Paint selectedBlurPaint2;
    private boolean skipBackgroundDrawing;
    SnowflakesEffect snowflakesEffect;
    private float themeAnimationValue;
    int times;
    int times2;
    private float translationX;
    private float translationY;
    public ArrayList unusedBitmaps;
    private final ArrayList views;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class BackgroundView extends View {
        public BackgroundView(Context context) {
            super(context);
        }

        /* JADX WARN: Code restructure failed: missing block: B:117:0x02be, code lost:
            if (r16.this$0.useRootView() != false) goto L76;
         */
        /* JADX WARN: Code restructure failed: missing block: B:118:0x02c0, code lost:
            r12 = getRootView().getMeasuredHeight() - r11;
         */
        /* JADX WARN: Code restructure failed: missing block: B:119:0x02ca, code lost:
            r12 = getHeight();
         */
        /* JADX WARN: Code restructure failed: missing block: B:120:0x02ce, code lost:
            r13 = java.lang.Math.max(getMeasuredWidth() / r7.getIntrinsicWidth(), r12 / r7.getIntrinsicHeight());
            r14 = (int) java.lang.Math.ceil((r7.getIntrinsicWidth() * r13) * r8);
            r4 = (int) java.lang.Math.ceil((r7.getIntrinsicHeight() * r13) * r8);
            r5 = ((getMeasuredWidth() - r14) / 2) + ((int) r9);
            r8 = ((r16.this$0.backgroundTranslationY + ((r12 - r4) / 2)) + r11) + ((int) r10);
            r17.save();
            r17.clipRect(0, r11, r14, getMeasuredHeight() - r16.this$0.bottomClip);
            r7.setBounds(r5, r8, r14 + r5, r4 + r8);
            r7.draw(r17);
            r16.this$0.checkSnowflake(r17);
         */
        /* JADX WARN: Code restructure failed: missing block: B:128:0x03b6, code lost:
            if (r16.this$0.bottomClip != 0) goto L59;
         */
        /* JADX WARN: Code restructure failed: missing block: B:68:0x0141, code lost:
            if (r16.this$0.useRootView() != false) goto L76;
         */
        /* JADX WARN: Code restructure failed: missing block: B:80:0x01b2, code lost:
            if (r16.this$0.bottomClip != 0) goto L59;
         */
        /* JADX WARN: Code restructure failed: missing block: B:88:0x01f4, code lost:
            if (r16.this$0.bottomClip != 0) goto L59;
         */
        /* JADX WARN: Code restructure failed: missing block: B:96:0x0247, code lost:
            if (r16.this$0.bottomClip != 0) goto L59;
         */
        /* JADX WARN: Removed duplicated region for block: B:144:0x0417  */
        /* JADX WARN: Removed duplicated region for block: B:153:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:29:0x00a9  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            int i;
            float f;
            float f2;
            float f3;
            int currentActionBarHeight;
            if (SizeNotifierFrameLayout.this.backgroundDrawable == null || SizeNotifierFrameLayout.this.skipBackgroundDrawing) {
                return;
            }
            Drawable newDrawable = SizeNotifierFrameLayout.this.getNewDrawable();
            boolean newDrawableMotion = SizeNotifierFrameLayout.this.getNewDrawableMotion();
            if (newDrawable == SizeNotifierFrameLayout.this.backgroundDrawable || newDrawable == null) {
                if (SizeNotifierFrameLayout.this.backgroundMotion != newDrawableMotion) {
                    SizeNotifierFrameLayout.this.backgroundMotion = newDrawableMotion;
                }
                SizeNotifierFrameLayout sizeNotifierFrameLayout = SizeNotifierFrameLayout.this;
                sizeNotifierFrameLayout.themeAnimationValue = Utilities.clamp(sizeNotifierFrameLayout.themeAnimationValue + (AndroidUtilities.screenRefreshTime / 200.0f), 1.0f, 0.0f);
                i = 0;
                while (i < 2) {
                    SizeNotifierFrameLayout sizeNotifierFrameLayout2 = SizeNotifierFrameLayout.this;
                    Drawable drawable = i == 0 ? sizeNotifierFrameLayout2.oldBackgroundDrawable : sizeNotifierFrameLayout2.backgroundDrawable;
                    if (drawable != null) {
                        drawable.setAlpha((i != 1 || SizeNotifierFrameLayout.this.oldBackgroundDrawable == null || SizeNotifierFrameLayout.this.parentLayout == null) ? NotificationCenter.closeSearchByActiveAction : (int) (SizeNotifierFrameLayout.this.themeAnimationValue * 255.0f));
                        SizeNotifierFrameLayout sizeNotifierFrameLayout3 = SizeNotifierFrameLayout.this;
                        if (i == 0 ? sizeNotifierFrameLayout3.oldBackgroundMotion : sizeNotifierFrameLayout3.backgroundMotion) {
                            f = SizeNotifierFrameLayout.this.parallaxScale;
                            f2 = SizeNotifierFrameLayout.this.translationX;
                            f3 = SizeNotifierFrameLayout.this.translationY;
                        } else {
                            f = 1.0f;
                            f2 = 0.0f;
                            f3 = 0.0f;
                        }
                        if (drawable instanceof MotionBackgroundDrawable) {
                            MotionBackgroundDrawable motionBackgroundDrawable = (MotionBackgroundDrawable) drawable;
                            if (motionBackgroundDrawable.hasPattern()) {
                                currentActionBarHeight = (SizeNotifierFrameLayout.this.isActionBarVisible() ? ActionBar.getCurrentActionBarHeight() : 0) + ((SizeNotifierFrameLayout.this.isStatusBarVisible() && Build.VERSION.SDK_INT >= 21 && SizeNotifierFrameLayout.this.occupyStatusBar) ? AndroidUtilities.statusBarHeight : 0);
                            } else {
                                if (SizeNotifierFrameLayout.this.bottomClip != 0) {
                                    canvas.save();
                                    canvas.clipRect(0, 0, getMeasuredWidth(), getRootView().getMeasuredHeight() - SizeNotifierFrameLayout.this.bottomClip);
                                }
                                motionBackgroundDrawable.setTranslationY(SizeNotifierFrameLayout.this.backgroundTranslationY);
                                int measuredHeight = (int) ((getRootView().getMeasuredHeight() - SizeNotifierFrameLayout.this.backgroundTranslationY) + f3);
                                if (SizeNotifierFrameLayout.this.animationInProgress) {
                                    measuredHeight = (int) (measuredHeight - SizeNotifierFrameLayout.this.emojiOffset);
                                } else if (SizeNotifierFrameLayout.this.emojiHeight != 0) {
                                    measuredHeight -= SizeNotifierFrameLayout.this.emojiHeight;
                                }
                                drawable.setBounds(0, 0, getMeasuredWidth(), measuredHeight);
                                drawable.draw(canvas);
                            }
                        } else if (drawable instanceof ColorDrawable) {
                            if (SizeNotifierFrameLayout.this.bottomClip != 0) {
                                canvas.save();
                                canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - SizeNotifierFrameLayout.this.bottomClip);
                            }
                            drawable.setBounds(0, 0, getMeasuredWidth(), getRootView().getMeasuredHeight());
                            drawable.draw(canvas);
                            SizeNotifierFrameLayout.this.checkSnowflake(canvas);
                        } else if (drawable instanceof GradientDrawable) {
                            if (SizeNotifierFrameLayout.this.bottomClip != 0) {
                                canvas.save();
                                canvas.clipRect(0, 0, getMeasuredWidth(), getRootView().getMeasuredHeight() - SizeNotifierFrameLayout.this.bottomClip);
                            }
                            drawable.setBounds(0, SizeNotifierFrameLayout.this.backgroundTranslationY, getMeasuredWidth(), SizeNotifierFrameLayout.this.backgroundTranslationY + getRootView().getMeasuredHeight());
                            drawable.draw(canvas);
                            SizeNotifierFrameLayout.this.checkSnowflake(canvas);
                        } else if (!(drawable instanceof BitmapDrawable)) {
                            if (SizeNotifierFrameLayout.this.bottomClip != 0) {
                                canvas.save();
                                canvas.clipRect(0, 0, getMeasuredWidth(), getRootView().getMeasuredHeight() - SizeNotifierFrameLayout.this.bottomClip);
                            }
                            if (drawable instanceof ChatBackgroundDrawable) {
                                ((ChatBackgroundDrawable) drawable).setParent(this);
                            }
                            float f4 = f - 1.0f;
                            float f5 = (((-getMeasuredWidth()) * f4) / 2.0f) + f2;
                            float f6 = (((-getRootView().getMeasuredHeight()) * f4) / 2.0f) + f3;
                            drawable.setBounds((int) f5, (int) (SizeNotifierFrameLayout.this.backgroundTranslationY + f6), (int) ((getMeasuredWidth() * f) + f5), (int) (SizeNotifierFrameLayout.this.backgroundTranslationY + (getRootView().getMeasuredHeight() * f) + f6));
                            drawable.draw(canvas);
                            SizeNotifierFrameLayout.this.checkSnowflake(canvas);
                        } else if (((BitmapDrawable) drawable).getTileModeX() == Shader.TileMode.REPEAT) {
                            canvas.save();
                            float f7 = 2.0f / AndroidUtilities.density;
                            canvas.scale(f7, f7);
                            drawable.setBounds(0, 0, (int) Math.ceil(getMeasuredWidth() / f7), (int) Math.ceil(getRootView().getMeasuredHeight() / f7));
                            drawable.draw(canvas);
                            SizeNotifierFrameLayout.this.checkSnowflake(canvas);
                        } else {
                            currentActionBarHeight = (SizeNotifierFrameLayout.this.isActionBarVisible() ? ActionBar.getCurrentActionBarHeight() : 0) + ((SizeNotifierFrameLayout.this.isStatusBarVisible() && Build.VERSION.SDK_INT >= 21 && SizeNotifierFrameLayout.this.occupyStatusBar) ? AndroidUtilities.statusBarHeight : 0);
                        }
                        canvas.restore();
                        if (i == 0 && SizeNotifierFrameLayout.this.oldBackgroundDrawable != null && SizeNotifierFrameLayout.this.themeAnimationValue >= 1.0f) {
                            SizeNotifierFrameLayout sizeNotifierFrameLayout4 = SizeNotifierFrameLayout.this;
                            if (sizeNotifierFrameLayout4.attached && (sizeNotifierFrameLayout4.oldBackgroundDrawable instanceof ChatBackgroundDrawable)) {
                                ((ChatBackgroundDrawable) SizeNotifierFrameLayout.this.oldBackgroundDrawable).onDetachedFromWindow(SizeNotifierFrameLayout.this.backgroundView);
                            }
                            SizeNotifierFrameLayout.this.oldBackgroundDrawable = null;
                            SizeNotifierFrameLayout.this.oldBackgroundMotion = false;
                            SizeNotifierFrameLayout.this.checkMotion();
                            SizeNotifierFrameLayout.this.backgroundView.invalidate();
                        }
                    }
                    i++;
                }
                if (SizeNotifierFrameLayout.this.themeAnimationValue == 1.0f) {
                    SizeNotifierFrameLayout.this.backgroundView.invalidate();
                    return;
                }
                return;
            }
            if (Theme.isAnimatingColor()) {
                SizeNotifierFrameLayout sizeNotifierFrameLayout5 = SizeNotifierFrameLayout.this;
                sizeNotifierFrameLayout5.oldBackgroundDrawable = sizeNotifierFrameLayout5.backgroundDrawable;
                SizeNotifierFrameLayout sizeNotifierFrameLayout6 = SizeNotifierFrameLayout.this;
                sizeNotifierFrameLayout6.oldBackgroundMotion = sizeNotifierFrameLayout6.backgroundMotion;
            }
            if (newDrawable instanceof MotionBackgroundDrawable) {
                ((MotionBackgroundDrawable) newDrawable).setParentView(SizeNotifierFrameLayout.this.backgroundView);
            }
            SizeNotifierFrameLayout.this.backgroundDrawable = newDrawable;
            SizeNotifierFrameLayout sizeNotifierFrameLayout7 = SizeNotifierFrameLayout.this;
            if (sizeNotifierFrameLayout7.attached && (sizeNotifierFrameLayout7.backgroundDrawable instanceof ChatBackgroundDrawable)) {
                ((ChatBackgroundDrawable) SizeNotifierFrameLayout.this.backgroundDrawable).onAttachedToWindow(this);
            }
            SizeNotifierFrameLayout.this.backgroundMotion = newDrawableMotion;
            SizeNotifierFrameLayout.this.themeAnimationValue = 0.0f;
            SizeNotifierFrameLayout.this.checkMotion();
            SizeNotifierFrameLayout sizeNotifierFrameLayout8 = SizeNotifierFrameLayout.this;
            sizeNotifierFrameLayout8.themeAnimationValue = Utilities.clamp(sizeNotifierFrameLayout8.themeAnimationValue + (AndroidUtilities.screenRefreshTime / 200.0f), 1.0f, 0.0f);
            i = 0;
            while (i < 2) {
            }
            if (SizeNotifierFrameLayout.this.themeAnimationValue == 1.0f) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class BlurBackgroundTask implements Runnable {
        BlurBitmap finalBitmap;
        int radius;

        private BlurBackgroundTask() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(ValueAnimator valueAnimator) {
            SizeNotifierFrameLayout.this.blurCrossfadeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            SizeNotifierFrameLayout.this.invalidateBlurredViews();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1() {
            SizeNotifierFrameLayout sizeNotifierFrameLayout = SizeNotifierFrameLayout.this;
            sizeNotifierFrameLayout.blurGeneratingTuskIsRunning = false;
            sizeNotifierFrameLayout.startBlur();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$2() {
            SizeNotifierFrameLayout sizeNotifierFrameLayout = SizeNotifierFrameLayout.this;
            if (!sizeNotifierFrameLayout.blurIsRunning) {
                BlurBitmap blurBitmap = this.finalBitmap;
                if (blurBitmap != null) {
                    blurBitmap.recycle();
                }
                SizeNotifierFrameLayout.this.blurGeneratingTuskIsRunning = false;
                return;
            }
            final BlurBitmap blurBitmap2 = sizeNotifierFrameLayout.currentBitmap;
            sizeNotifierFrameLayout.prevBitmap = blurBitmap2;
            sizeNotifierFrameLayout.blurPaintTop2.setShader(sizeNotifierFrameLayout.blurPaintTop.getShader());
            SizeNotifierFrameLayout sizeNotifierFrameLayout2 = SizeNotifierFrameLayout.this;
            sizeNotifierFrameLayout2.blurPaintBottom2.setShader(sizeNotifierFrameLayout2.blurPaintBottom.getShader());
            Bitmap bitmap = this.finalBitmap.topBitmap;
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            SizeNotifierFrameLayout.this.blurPaintTop.setShader(new BitmapShader(bitmap, tileMode, tileMode));
            BlurBitmap blurBitmap3 = this.finalBitmap;
            if (blurBitmap3.needBlurBottom && blurBitmap3.bottomBitmap != null) {
                SizeNotifierFrameLayout.this.blurPaintBottom.setShader(new BitmapShader(this.finalBitmap.bottomBitmap, tileMode, tileMode));
            }
            ValueAnimator valueAnimator = SizeNotifierFrameLayout.this.blurCrossfade;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout3 = SizeNotifierFrameLayout.this;
            sizeNotifierFrameLayout3.blurCrossfadeProgress = 0.0f;
            sizeNotifierFrameLayout3.blurCrossfade = ValueAnimator.ofFloat(0.0f, 1.0f);
            SizeNotifierFrameLayout.this.blurCrossfade.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.SizeNotifierFrameLayout$BlurBackgroundTask$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    SizeNotifierFrameLayout.BlurBackgroundTask.this.lambda$run$0(valueAnimator2);
                }
            });
            SizeNotifierFrameLayout.this.blurCrossfade.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.SizeNotifierFrameLayout.BlurBackgroundTask.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SizeNotifierFrameLayout sizeNotifierFrameLayout4 = SizeNotifierFrameLayout.this;
                    sizeNotifierFrameLayout4.blurCrossfadeProgress = 1.0f;
                    sizeNotifierFrameLayout4.unusedBitmaps.add(blurBitmap2);
                    SizeNotifierFrameLayout.this.blurPaintTop2.setShader(null);
                    SizeNotifierFrameLayout.this.blurPaintBottom2.setShader(null);
                    SizeNotifierFrameLayout.this.invalidateBlurredViews();
                    super.onAnimationEnd(animator);
                }
            });
            SizeNotifierFrameLayout.this.blurCrossfade.setDuration(50L);
            SizeNotifierFrameLayout.this.blurCrossfade.start();
            SizeNotifierFrameLayout.this.invalidateBlurredViews();
            SizeNotifierFrameLayout.this.currentBitmap = this.finalBitmap;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.SizeNotifierFrameLayout$BlurBackgroundTask$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    SizeNotifierFrameLayout.BlurBackgroundTask.this.lambda$run$1();
                }
            }, 16L);
        }

        @Override // java.lang.Runnable
        public void run() {
            Bitmap bitmap;
            long currentTimeMillis = System.currentTimeMillis();
            Utilities.stackBlurBitmap(this.finalBitmap.topBitmap, this.radius);
            BlurBitmap blurBitmap = this.finalBitmap;
            if (blurBitmap.needBlurBottom && (bitmap = blurBitmap.bottomBitmap) != null) {
                Utilities.stackBlurBitmap(bitmap, this.radius);
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout = SizeNotifierFrameLayout.this;
            sizeNotifierFrameLayout.times = (int) (sizeNotifierFrameLayout.times + (System.currentTimeMillis() - currentTimeMillis));
            SizeNotifierFrameLayout sizeNotifierFrameLayout2 = SizeNotifierFrameLayout.this;
            int i = sizeNotifierFrameLayout2.count + 1;
            sizeNotifierFrameLayout2.count = i;
            if (i > 1000) {
                StringBuilder sb = new StringBuilder();
                sb.append("chat blur generating average time");
                SizeNotifierFrameLayout sizeNotifierFrameLayout3 = SizeNotifierFrameLayout.this;
                sb.append(sizeNotifierFrameLayout3.times / sizeNotifierFrameLayout3.count);
                FileLog.d(sb.toString());
                SizeNotifierFrameLayout sizeNotifierFrameLayout4 = SizeNotifierFrameLayout.this;
                sizeNotifierFrameLayout4.count = 0;
                sizeNotifierFrameLayout4.times = 0;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.SizeNotifierFrameLayout$BlurBackgroundTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SizeNotifierFrameLayout.BlurBackgroundTask.this.lambda$run$2();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class BlurBitmap {
        Bitmap bottomBitmap;
        Canvas bottomCanvas;
        float bottomOffset;
        float bottomScaleX;
        float bottomScaleY;
        float drawnListTranslationY;
        public boolean needBlurBottom;
        int pixelFixOffset;
        Bitmap topBitmap;
        Canvas topCanvas;
        float topScaleX;
        float topScaleY;

        private BlurBitmap() {
        }

        public void recycle() {
            this.topBitmap.recycle();
            Bitmap bitmap = this.bottomBitmap;
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    /* loaded from: classes3.dex */
    public interface IViewWithInvalidateCallback {
        void listenInvalidate(Runnable runnable);
    }

    /* loaded from: classes3.dex */
    public static class SimplerCanvas extends Canvas {
        public SimplerCanvas(Bitmap bitmap) {
            super(bitmap);
        }

        @Override // android.graphics.Canvas
        public boolean clipPath(Path path) {
            return false;
        }

        @Override // android.graphics.Canvas
        public boolean clipPath(Path path, Region.Op op) {
            return false;
        }

        @Override // android.graphics.Canvas
        public void drawText(CharSequence charSequence, int i, int i2, float f, float f2, Paint paint) {
        }

        @Override // android.graphics.Canvas
        public void drawText(String str, float f, float f2, Paint paint) {
        }

        @Override // android.graphics.Canvas
        public void drawText(String str, int i, int i2, float f, float f2, Paint paint) {
        }

        @Override // android.graphics.Canvas
        public void drawText(char[] cArr, int i, int i2, float f, float f2, Paint paint) {
        }

        @Override // android.graphics.Canvas
        public void drawTextOnPath(String str, Path path, float f, float f2, Paint paint) {
        }

        @Override // android.graphics.Canvas
        public void drawTextOnPath(char[] cArr, int i, int i2, Path path, float f, float f2, Paint paint) {
        }

        @Override // android.graphics.Canvas
        public void drawTextRun(MeasuredText measuredText, int i, int i2, int i3, int i4, float f, float f2, boolean z, Paint paint) {
        }

        @Override // android.graphics.Canvas
        public void drawTextRun(CharSequence charSequence, int i, int i2, int i3, int i4, float f, float f2, boolean z, Paint paint) {
        }

        @Override // android.graphics.Canvas
        public void drawTextRun(char[] cArr, int i, int i2, int i3, int i4, float f, float f2, boolean z, Paint paint) {
        }
    }

    /* loaded from: classes3.dex */
    public interface SizeNotifierFrameLayoutDelegate {
        void onSizeChanged(int i, boolean z);
    }

    public SizeNotifierFrameLayout(Context context) {
        this(context, null);
    }

    public SizeNotifierFrameLayout(Context context, INavigationLayout iNavigationLayout) {
        super(context);
        this.rect = new android.graphics.Rect();
        this.occupyStatusBar = true;
        this.parallaxScale = 1.0f;
        this.paused = true;
        this.unusedBitmaps = new ArrayList(10);
        this.blurBehindViews = new ArrayList();
        this.matrix = new Matrix();
        this.matrix2 = new Matrix();
        this.blurPaintTop = new Paint();
        this.blurPaintTop2 = new Paint();
        this.blurPaintBottom = new Paint();
        this.blurPaintBottom2 = new Paint();
        this.DOWN_SCALE = 12.0f;
        this.TOP_CLIP_OFFSET = 34;
        this.themeAnimationValue = 1.0f;
        this.blurBackgroundTask = new BlurBackgroundTask();
        this.blurNodeInvalidatedThisFrame = new boolean[2];
        this.blurNodeInvalidated = new boolean[2];
        this.lastViews = new ArrayList();
        this.views = new ArrayList();
        setWillNotDraw(false);
        this.parentLayout = iNavigationLayout;
        this.adjustPanLayoutHelper = createAdjustPanLayoutHelper();
    }

    private void checkLayerType() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkMotion() {
        if (!this.oldBackgroundMotion && !this.backgroundMotion) {
            WallpaperParallaxEffect wallpaperParallaxEffect = this.parallaxEffect;
            if (wallpaperParallaxEffect != null) {
                wallpaperParallaxEffect.setEnabled(false);
                this.parallaxEffect = null;
                this.parallaxScale = 1.0f;
                this.translationX = 0.0f;
                this.translationY = 0.0f;
                return;
            }
            return;
        }
        if (this.parallaxEffect == null) {
            WallpaperParallaxEffect wallpaperParallaxEffect2 = new WallpaperParallaxEffect(getContext());
            this.parallaxEffect = wallpaperParallaxEffect2;
            wallpaperParallaxEffect2.setCallback(new WallpaperParallaxEffect.Callback() { // from class: org.telegram.ui.Components.SizeNotifierFrameLayout$$ExternalSyntheticLambda9
                @Override // org.telegram.ui.Components.WallpaperParallaxEffect.Callback
                public final void onOffsetsChanged(int i, int i2, float f) {
                    SizeNotifierFrameLayout.this.lambda$checkMotion$0(i, i2, f);
                }
            });
            if (getMeasuredWidth() != 0 && getMeasuredHeight() != 0) {
                this.parallaxScale = this.parallaxEffect.getScale(getMeasuredWidth(), getMeasuredHeight());
            }
        }
        if (this.paused) {
            return;
        }
        this.parallaxEffect.setEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkSnowflake(Canvas canvas) {
        if (this.backgroundView != null && Theme.canStartHolidayAnimation() && LiteMode.isEnabled(32)) {
            if (this.snowflakesEffect == null) {
                this.snowflakesEffect = new SnowflakesEffect(1);
            }
            this.snowflakesEffect.onDraw(this.backgroundView, canvas);
        }
    }

    private void drawListWithCallbacks(Canvas canvas, boolean z) {
        if (!invalidateOptimized()) {
            drawList(canvas, z, null);
            return;
        }
        this.lastViews.clear();
        this.lastViews.addAll(this.views);
        this.views.clear();
        drawList(canvas, z, this.views);
        Iterator it = this.lastViews.iterator();
        while (it.hasNext()) {
            ((IViewWithInvalidateCallback) it.next()).listenInvalidate(null);
        }
        Iterator it2 = this.views.iterator();
        while (it2.hasNext()) {
            ((IViewWithInvalidateCallback) it2.next()).listenInvalidate(new Runnable() { // from class: org.telegram.ui.Components.SizeNotifierFrameLayout$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    SizeNotifierFrameLayout.this.updateBlurContent();
                }
            });
        }
    }

    private float getBlurRadius() {
        int devicePerformanceClass = SharedConfig.getDevicePerformanceClass();
        if (devicePerformanceClass != 1) {
            return devicePerformanceClass != 2 ? 3.0f : 60.0f;
        }
        return 4.0f;
    }

    private float getRenderNodeScale() {
        float f;
        int devicePerformanceClass = SharedConfig.getDevicePerformanceClass();
        if (devicePerformanceClass == 1) {
            f = 12.0f;
        } else if (devicePerformanceClass == 2) {
            return AndroidUtilities.density;
        } else {
            f = 15.0f;
        }
        return AndroidUtilities.dp(f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkMotion$0(int i, int i2, float f) {
        this.translationX = i;
        this.translationY = i2;
        this.bgAngle = f;
        View view = this.backgroundView;
        if (view != null) {
            view.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyHeightChanged$1(boolean z) {
        SizeNotifierFrameLayoutDelegate sizeNotifierFrameLayoutDelegate = this.delegate;
        if (sizeNotifierFrameLayoutDelegate != null) {
            sizeNotifierFrameLayoutDelegate.onSizeChanged(this.keyboardHeight, z);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateBlurShaderPosition(float f, boolean z) {
        Matrix matrix;
        float f2;
        float f3;
        this.selectedBlurPaint = z ? this.blurPaintTop : this.blurPaintBottom;
        this.selectedBlurPaint2 = z ? this.blurPaintTop2 : this.blurPaintBottom2;
        if (z) {
            f += getTranslationY();
        }
        if (this.selectedBlurPaint.getShader() == null) {
            return;
        }
        this.matrix.reset();
        this.matrix2.reset();
        if (!z) {
            float f4 = -f;
            BlurBitmap blurBitmap = this.currentBitmap;
            this.matrix.setTranslate(0.0f, (((blurBitmap.bottomOffset + f4) - blurBitmap.pixelFixOffset) - 34.0f) - (blurBitmap.drawnListTranslationY - (getBottomOffset() + getListTranslationY())));
            Matrix matrix2 = this.matrix;
            BlurBitmap blurBitmap2 = this.currentBitmap;
            matrix2.preScale(blurBitmap2.bottomScaleX, blurBitmap2.bottomScaleY);
            BlurBitmap blurBitmap3 = this.prevBitmap;
            if (blurBitmap3 != null) {
                this.matrix2.setTranslate(0.0f, (((f4 + blurBitmap3.bottomOffset) - blurBitmap3.pixelFixOffset) - 34.0f) - (blurBitmap3.drawnListTranslationY - (getBottomOffset() + getListTranslationY())));
                matrix = this.matrix2;
                BlurBitmap blurBitmap4 = this.prevBitmap;
                f2 = blurBitmap4.bottomScaleX;
                f3 = blurBitmap4.bottomScaleY;
                matrix.preScale(f2, f3);
            }
            this.selectedBlurPaint.getShader().setLocalMatrix(this.matrix);
            if (this.selectedBlurPaint2.getShader() == null) {
                this.selectedBlurPaint2.getShader().setLocalMatrix(this.matrix);
                return;
            }
            return;
        }
        float f5 = -f;
        this.matrix.setTranslate(0.0f, (f5 - this.currentBitmap.pixelFixOffset) - 34.0f);
        Matrix matrix3 = this.matrix;
        BlurBitmap blurBitmap5 = this.currentBitmap;
        matrix3.preScale(blurBitmap5.topScaleX, blurBitmap5.topScaleY);
        BlurBitmap blurBitmap6 = this.prevBitmap;
        if (blurBitmap6 != null) {
            this.matrix2.setTranslate(0.0f, (f5 - blurBitmap6.pixelFixOffset) - 34.0f);
            matrix = this.matrix2;
            BlurBitmap blurBitmap7 = this.prevBitmap;
            f2 = blurBitmap7.topScaleX;
            f3 = blurBitmap7.topScaleY;
            matrix.preScale(f2, f3);
        }
        this.selectedBlurPaint.getShader().setLocalMatrix(this.matrix);
        if (this.selectedBlurPaint2.getShader() == null) {
        }
    }

    public boolean DRAW_USING_RENDERNODE() {
        return Build.VERSION.SDK_INT >= 31 && SharedConfig.useNewBlur;
    }

    public boolean blurWasDrawn() {
        return SharedConfig.chatBlurEnabled() && (DRAW_USING_RENDERNODE() || this.currentBitmap != null);
    }

    protected AdjustPanLayoutHelper createAdjustPanLayoutHelper() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        boolean[] zArr = this.blurNodeInvalidatedThisFrame;
        zArr[0] = false;
        zArr[1] = false;
        if (this.blurIsRunning) {
            startBlur();
        }
        super.dispatchDraw(canvas);
    }

    public void drawBlurCircle(Canvas canvas, float f, float f2, float f3, float f4, Paint paint, boolean z) {
        int alpha = Color.alpha(Theme.getColor(DRAW_USING_RENDERNODE() ? Theme.key_chat_BlurAlpha : Theme.key_chat_BlurAlphaSlow));
        if (this.currentBitmap == null || !SharedConfig.chatBlurEnabled()) {
            canvas.drawCircle(f2, f3, f4, paint);
            return;
        }
        updateBlurShaderPosition(f, z);
        paint.setAlpha(NotificationCenter.closeSearchByActiveAction);
        if (this.blurCrossfadeProgress == 1.0f || this.selectedBlurPaint2.getShader() == null) {
            canvas.drawCircle(f2, f3, f4, paint);
            canvas.drawCircle(f2, f3, f4, this.selectedBlurPaint);
        } else {
            canvas.drawCircle(f2, f3, f4, paint);
            canvas.drawCircle(f2, f3, f4, this.selectedBlurPaint2);
            canvas.saveLayerAlpha(f2 - f4, f3 - f4, f2 + f4, f3 + f4, (int) (this.blurCrossfadeProgress * 255.0f), 31);
            canvas.drawCircle(f2, f3, f4, paint);
            canvas.drawCircle(f2, f3, f4, this.selectedBlurPaint);
            canvas.restore();
        }
        paint.setAlpha(alpha);
        canvas.drawCircle(f2, f3, f4, paint);
    }

    public void drawBlurRect(Canvas canvas, float f, android.graphics.Rect rect, Paint paint, boolean z) {
        float f2;
        float bottomOffset;
        RecordingCanvas beginRecording;
        Shader.TileMode tileMode;
        RenderEffect createBlurEffect;
        RenderEffect createColorFilterEffect;
        RenderEffect createChainEffect;
        int alpha = Color.alpha(Theme.getColor((DRAW_USING_RENDERNODE() && SharedConfig.getDevicePerformanceClass() == 2) ? Theme.key_chat_BlurAlpha : Theme.key_chat_BlurAlphaSlow));
        if (!SharedConfig.chatBlurEnabled()) {
            canvas.drawRect(rect, paint);
        } else if (!DRAW_USING_RENDERNODE()) {
            if (this.currentBitmap == null) {
                canvas.drawRect(rect, paint);
                return;
            }
            updateBlurShaderPosition(f, z);
            paint.setAlpha(NotificationCenter.closeSearchByActiveAction);
            if (this.blurCrossfadeProgress == 1.0f || this.selectedBlurPaint2.getShader() == null) {
                canvas.drawRect(rect, paint);
                canvas.drawRect(rect, this.selectedBlurPaint);
            } else {
                canvas.drawRect(rect, paint);
                canvas.drawRect(rect, this.selectedBlurPaint2);
                canvas.saveLayerAlpha(rect.left, rect.top, rect.right, rect.bottom, (int) (this.blurCrossfadeProgress * 255.0f), 31);
                canvas.drawRect(rect, paint);
                canvas.drawRect(rect, this.selectedBlurPaint);
                canvas.restore();
            }
            paint.setAlpha(alpha);
            canvas.drawRect(rect, paint);
        } else if (!canvas.isHardwareAccelerated()) {
            canvas.drawRect(rect, paint);
        } else {
            if (this.blurNodes == null) {
                this.blurNodes = new RenderNode[2];
            }
            float renderNodeScale = getRenderNodeScale();
            int i = !z ? 1 : 0;
            if (!z && !this.blurNodeInvalidated[i] && Math.abs(getBottomOffset() - this.lastDrawnBottomBlurOffset) > 0.1f) {
                this.blurNodeInvalidated[i] = true;
            }
            int dp = AndroidUtilities.dp(36.0f);
            if (alpha < 255 && this.blurNodeInvalidated[i] && !this.blurNodeInvalidatedThisFrame[i]) {
                RenderNode[] renderNodeArr = this.blurNodes;
                if (renderNodeArr[i] == null) {
                    renderNodeArr[i] = new RenderNode("blurNode" + i);
                    ColorMatrix colorMatrix = new ColorMatrix();
                    colorMatrix.setSaturation(2.0f);
                    RenderNode renderNode = this.blurNodes[i];
                    float blurRadius = getBlurRadius();
                    float blurRadius2 = getBlurRadius();
                    tileMode = Shader.TileMode.DECAL;
                    createBlurEffect = RenderEffect.createBlurEffect(blurRadius, blurRadius2, tileMode);
                    createColorFilterEffect = RenderEffect.createColorFilterEffect(new ColorMatrixColorFilter(colorMatrix));
                    createChainEffect = RenderEffect.createChainEffect(createBlurEffect, createColorFilterEffect);
                    renderNode.setRenderEffect(createChainEffect);
                }
                int measuredWidth = getMeasuredWidth();
                int currentActionBarHeight = ActionBar.getCurrentActionBarHeight() + AndroidUtilities.statusBarHeight + AndroidUtilities.dp(100.0f);
                this.blurNodes[i].setPosition(0, 0, (int) (measuredWidth / renderNodeScale), (int) (((dp * 2) + currentActionBarHeight) / renderNodeScale));
                beginRecording = this.blurNodes[i].beginRecording();
                drawingBlur = true;
                float f3 = 1.0f / renderNodeScale;
                beginRecording.scale(f3, f3);
                beginRecording.drawPaint(paint);
                beginRecording.translate(0.0f, dp);
                if (!z) {
                    float bottomOffset2 = getBottomOffset();
                    this.lastDrawnBottomBlurOffset = bottomOffset2;
                    float f4 = bottomOffset2 - currentActionBarHeight;
                    this.drawnBottomOffset = f4;
                    beginRecording.translate(0.0f, -f4);
                }
                drawListWithCallbacks(beginRecording, z);
                drawingBlur = false;
                this.blurNodes[i].endRecording();
                this.blurNodeInvalidatedThisFrame[i] = true;
                this.blurNodeInvalidated[i] = false;
            }
            if (!invalidateOptimized()) {
                this.blurNodeInvalidated[i] = true;
                invalidateBlurredViews();
            }
            canvas.save();
            canvas.drawRect(rect, paint);
            canvas.clipRect(rect);
            RenderNode renderNode2 = this.blurNodes[i];
            if (renderNode2 != null && alpha < 255) {
                renderNode2.setAlpha(1.0f - (alpha / 255.0f));
                if (z) {
                    bottomOffset = (-f) - getTranslationY();
                    f2 = 0.0f;
                } else {
                    f2 = 0.0f;
                    bottomOffset = ((-f) + this.drawnBottomOffset) - (this.lastDrawnBottomBlurOffset - (getBottomOffset() + getListTranslationY()));
                }
                canvas.translate(f2, bottomOffset);
                canvas.translate(f2, -dp);
                canvas.scale(renderNodeScale, renderNodeScale);
                canvas.drawRenderNode(this.blurNodes[i]);
            }
            canvas.restore();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void drawList(Canvas canvas, boolean z, ArrayList arrayList) {
    }

    public Drawable getBackgroundImage() {
        return this.backgroundDrawable;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0019, code lost:
        if (r2 != 0) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0027, code lost:
        if ((r0 instanceof org.telegram.ui.ChatBackgroundDrawable) != false) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0029, code lost:
        r2 = r3.backgroundTranslationY;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getBackgroundSizeY() {
        Drawable drawable = this.backgroundDrawable;
        int i = 0;
        if (drawable instanceof MotionBackgroundDrawable) {
            if (!((MotionBackgroundDrawable) drawable).hasPattern()) {
                i = this.animationInProgress ? (int) this.emojiOffset : this.emojiHeight;
            } else if (this.backgroundTranslationY == 0) {
                i = -this.keyboardHeight;
            }
        }
        return getMeasuredHeight() - i;
    }

    public int getBackgroundTranslationY() {
        Drawable drawable = this.backgroundDrawable;
        if (!(drawable instanceof MotionBackgroundDrawable)) {
            if (drawable instanceof ChatBackgroundDrawable) {
                return this.backgroundTranslationY;
            }
            return 0;
        } else if (this.animationInProgress) {
            return (int) this.emojiOffset;
        } else {
            int i = this.emojiHeight;
            return i != 0 ? i : this.backgroundTranslationY;
        }
    }

    protected float getBottomOffset() {
        return getMeasuredHeight();
    }

    public int getBottomPadding() {
        return 0;
    }

    protected float getBottomTranslation() {
        return 0.0f;
    }

    public int getHeightWithKeyboard() {
        return this.keyboardHeight + getMeasuredHeight();
    }

    public int getKeyboardHeight() {
        return this.keyboardHeight;
    }

    protected float getListTranslationY() {
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Drawable getNewDrawable() {
        return Theme.getCachedWallpaperNonBlocking();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean getNewDrawableMotion() {
        return Theme.isWallpaperMotion();
    }

    protected Theme.ResourcesProvider getResourceProvider() {
        return null;
    }

    protected int getScrollOffset() {
        return 0;
    }

    public void invalidateBackground() {
        View view = this.backgroundView;
        if (view != null) {
            view.invalidate();
        }
    }

    public void invalidateBlur() {
        if (SharedConfig.chatBlurEnabled()) {
            this.invalidateBlur = true;
            if (!this.blurIsRunning || this.blurGeneratingTuskIsRunning) {
                return;
            }
            invalidate();
        }
    }

    public void invalidateBlurredViews() {
        boolean[] zArr = this.blurNodeInvalidated;
        zArr[0] = true;
        zArr[1] = true;
        for (int i = 0; i < this.blurBehindViews.size(); i++) {
            ((View) this.blurBehindViews.get(i)).invalidate();
        }
    }

    protected boolean invalidateOptimized() {
        return false;
    }

    protected boolean isActionBarVisible() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isStatusBarVisible() {
        return true;
    }

    public int measureKeyboardHeight() {
        View rootView = getRootView();
        getWindowVisibleDisplayFrame(this.rect);
        android.graphics.Rect rect = this.rect;
        if (rect.bottom == 0 && rect.top == 0) {
            return 0;
        }
        int height = (rootView.getHeight() - (this.rect.top != 0 ? AndroidUtilities.statusBarHeight : 0)) - AndroidUtilities.getViewInset(rootView);
        android.graphics.Rect rect2 = this.rect;
        int max = Math.max(0, height - (rect2.bottom - rect2.top));
        this.keyboardHeight = max;
        return max;
    }

    public void notifyHeightChanged() {
        WallpaperParallaxEffect wallpaperParallaxEffect = this.parallaxEffect;
        if (wallpaperParallaxEffect != null) {
            this.parallaxScale = wallpaperParallaxEffect.getScale(getMeasuredWidth(), getMeasuredHeight());
        }
        if (this.delegate != null) {
            this.keyboardHeight = measureKeyboardHeight();
            android.graphics.Point point = AndroidUtilities.displaySize;
            final boolean z = point.x > point.y;
            post(new Runnable() { // from class: org.telegram.ui.Components.SizeNotifierFrameLayout$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    SizeNotifierFrameLayout.this.lambda$notifyHeightChanged$1(z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attached = true;
        if (this.needBlur && !this.blurIsRunning) {
            this.blurIsRunning = true;
            this.invalidateBlur = true;
        }
        Drawable drawable = this.backgroundDrawable;
        if (drawable instanceof ChatBackgroundDrawable) {
            ((ChatBackgroundDrawable) drawable).onAttachedToWindow(this.backgroundView);
        }
        Drawable drawable2 = this.oldBackgroundDrawable;
        if (drawable2 instanceof ChatBackgroundDrawable) {
            ((ChatBackgroundDrawable) drawable2).onAttachedToWindow(this.backgroundView);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attached = false;
        this.blurPaintTop.setShader(null);
        this.blurPaintTop2.setShader(null);
        this.blurPaintBottom.setShader(null);
        this.blurPaintBottom2.setShader(null);
        ValueAnimator valueAnimator = this.blurCrossfade;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        BlurBitmap blurBitmap = this.currentBitmap;
        if (blurBitmap != null) {
            blurBitmap.recycle();
            this.currentBitmap = null;
        }
        for (int i = 0; i < this.unusedBitmaps.size(); i++) {
            if (this.unusedBitmaps.get(i) != null) {
                ((BlurBitmap) this.unusedBitmaps.get(i)).recycle();
            }
        }
        this.unusedBitmaps.clear();
        this.blurIsRunning = false;
        Drawable drawable = this.backgroundDrawable;
        if (drawable instanceof ChatBackgroundDrawable) {
            ((ChatBackgroundDrawable) drawable).onDetachedFromWindow(this.backgroundView);
        }
        Drawable drawable2 = this.oldBackgroundDrawable;
        if (drawable2 instanceof ChatBackgroundDrawable) {
            ((ChatBackgroundDrawable) drawable2).onDetachedFromWindow(this.backgroundView);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        notifyHeightChanged();
    }

    public void onPause() {
        WallpaperParallaxEffect wallpaperParallaxEffect = this.parallaxEffect;
        if (wallpaperParallaxEffect != null) {
            wallpaperParallaxEffect.setEnabled(false);
        }
        this.paused = true;
    }

    public void onResume() {
        WallpaperParallaxEffect wallpaperParallaxEffect = this.parallaxEffect;
        if (wallpaperParallaxEffect != null) {
            wallpaperParallaxEffect.setEnabled(true);
        }
        this.paused = false;
    }

    public void setBackgroundImage(Drawable drawable, boolean z) {
        if (this.backgroundDrawable == drawable) {
            return;
        }
        if (this.backgroundView == null) {
            BackgroundView backgroundView = new BackgroundView(getContext());
            this.backgroundView = backgroundView;
            addView(backgroundView, 0, LayoutHelper.createFrame(-1, -1.0f));
            checkLayerType();
        }
        if (drawable instanceof MotionBackgroundDrawable) {
            ((MotionBackgroundDrawable) drawable).setParentView(this.backgroundView);
        }
        if (this.attached) {
            Drawable drawable2 = this.backgroundDrawable;
            if (drawable2 instanceof ChatBackgroundDrawable) {
                ((ChatBackgroundDrawable) drawable2).onDetachedFromWindow(this.backgroundView);
            }
        }
        this.backgroundDrawable = drawable;
        if (this.attached && (drawable instanceof ChatBackgroundDrawable)) {
            ((ChatBackgroundDrawable) drawable).onAttachedToWindow(this.backgroundView);
        }
        checkMotion();
        this.backgroundView.invalidate();
        checkLayerType();
    }

    public void setBackgroundTranslation(int i) {
        if (i != this.backgroundTranslationY) {
            this.backgroundTranslationY = i;
            View view = this.backgroundView;
            if (view != null) {
                view.invalidate();
            }
        }
    }

    public void setBottomClip(int i) {
        if (i != this.bottomClip) {
            this.bottomClip = i;
            View view = this.backgroundView;
            if (view != null) {
                view.invalidate();
            }
        }
    }

    public void setDelegate(SizeNotifierFrameLayoutDelegate sizeNotifierFrameLayoutDelegate) {
        this.delegate = sizeNotifierFrameLayoutDelegate;
    }

    public void setEmojiKeyboardHeight(int i) {
        if (this.emojiHeight != i) {
            this.emojiHeight = i;
            View view = this.backgroundView;
            if (view != null) {
                view.invalidate();
            }
        }
    }

    public void setEmojiOffset(boolean z, float f) {
        if (this.emojiOffset == f && this.animationInProgress == z) {
            return;
        }
        this.emojiOffset = f;
        this.animationInProgress = z;
        View view = this.backgroundView;
        if (view != null) {
            view.invalidate();
        }
    }

    public void setOccupyStatusBar(boolean z) {
        this.occupyStatusBar = z;
    }

    public void setSkipBackgroundDrawing(boolean z) {
        if (this.skipBackgroundDrawing != z) {
            this.skipBackgroundDrawing = z;
            View view = this.backgroundView;
            if (view != null) {
                view.invalidate();
            }
        }
    }

    public void startBlur() {
        BlurBitmap blurBitmap;
        if (!this.blurIsRunning || this.blurGeneratingTuskIsRunning || !this.invalidateBlur || !SharedConfig.chatBlurEnabled() || DRAW_USING_RENDERNODE() || Color.alpha(Theme.getColor(Theme.key_chat_BlurAlphaSlow)) == 255) {
            return;
        }
        int measuredWidth = getMeasuredWidth();
        int dp = AndroidUtilities.dp(100.0f) + ActionBar.getCurrentActionBarHeight() + AndroidUtilities.statusBarHeight;
        if (measuredWidth == 0 || dp == 0) {
            return;
        }
        this.invalidateBlur = false;
        this.blurGeneratingTuskIsRunning = true;
        float f = dp;
        int i = ((int) (f / 12.0f)) + 34;
        float f2 = measuredWidth;
        int i2 = (int) (f2 / 12.0f);
        long currentTimeMillis = System.currentTimeMillis();
        if (this.unusedBitmaps.size() > 0) {
            ArrayList arrayList = this.unusedBitmaps;
            blurBitmap = (BlurBitmap) arrayList.remove(arrayList.size() - 1);
        } else {
            blurBitmap = null;
        }
        if (blurBitmap == null) {
            blurBitmap = new BlurBitmap();
            Bitmap.Config config = Bitmap.Config.ARGB_8888;
            blurBitmap.topBitmap = Bitmap.createBitmap(i2, i, config);
            blurBitmap.topCanvas = new SimplerCanvas(blurBitmap.topBitmap);
            if (this.needBlurBottom) {
                blurBitmap.bottomBitmap = Bitmap.createBitmap(i2, i, config);
                blurBitmap.bottomCanvas = new SimplerCanvas(blurBitmap.bottomBitmap);
            }
        } else {
            blurBitmap.topBitmap.eraseColor(0);
            Bitmap bitmap = blurBitmap.bottomBitmap;
            if (bitmap != null) {
                bitmap.eraseColor(0);
            }
        }
        float width = blurBitmap.topBitmap.getWidth() / f2;
        float height = (blurBitmap.topBitmap.getHeight() - 34) / f;
        int save = blurBitmap.topCanvas.save();
        blurBitmap.pixelFixOffset = getScrollOffset() % 24;
        float f3 = height * 10.0f;
        blurBitmap.topCanvas.clipRect(1.0f, f3, blurBitmap.topBitmap.getWidth(), blurBitmap.topBitmap.getHeight() - 1);
        blurBitmap.topCanvas.scale(width, height);
        blurBitmap.topCanvas.translate(0.0f, f3 + blurBitmap.pixelFixOffset);
        blurBitmap.topScaleX = 1.0f / width;
        blurBitmap.topScaleY = 1.0f / height;
        drawList(blurBitmap.topCanvas, true, null);
        try {
            blurBitmap.topCanvas.restoreToCount(save);
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (this.needBlurBottom) {
            float width2 = blurBitmap.bottomBitmap.getWidth() / f2;
            float height2 = (blurBitmap.bottomBitmap.getHeight() - 34) / f;
            blurBitmap.needBlurBottom = true;
            blurBitmap.bottomOffset = getBottomOffset() - f;
            blurBitmap.drawnListTranslationY = getBottomOffset();
            blurBitmap.bottomCanvas.save();
            float f4 = height2 * 10.0f;
            blurBitmap.bottomCanvas.clipRect(1.0f, f4, blurBitmap.bottomBitmap.getWidth(), blurBitmap.bottomBitmap.getHeight() - 1);
            blurBitmap.bottomCanvas.scale(width2, height2);
            blurBitmap.bottomCanvas.translate(0.0f, (f4 - blurBitmap.bottomOffset) + blurBitmap.pixelFixOffset);
            blurBitmap.bottomScaleX = 1.0f / width2;
            blurBitmap.bottomScaleY = 1.0f / height2;
            drawList(blurBitmap.bottomCanvas, false, null);
            blurBitmap.bottomCanvas.restore();
        } else {
            blurBitmap.needBlurBottom = false;
        }
        this.times2 = (int) (this.times2 + (System.currentTimeMillis() - currentTimeMillis));
        int i3 = this.count2 + 1;
        this.count2 = i3;
        if (i3 >= 20) {
            this.count2 = 0;
            this.times2 = 0;
        }
        if (blurQueue == null) {
            blurQueue = new DispatchQueue("BlurQueue");
        }
        this.blurBackgroundTask.radius = (int) (((int) (Math.max(6, Math.max(dp, measuredWidth) / NotificationCenter.updateBotMenuButton) * 2.5f)) * BlurSettingsBottomSheet.blurRadius);
        BlurBackgroundTask blurBackgroundTask = this.blurBackgroundTask;
        blurBackgroundTask.finalBitmap = blurBitmap;
        blurQueue.postRunnable(blurBackgroundTask);
    }

    public void updateBlurContent() {
        if (DRAW_USING_RENDERNODE()) {
            invalidateBlurredViews();
        }
    }

    protected boolean useRootView() {
        return true;
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return drawable == getBackgroundImage() || super.verifyDrawable(drawable);
    }
}
