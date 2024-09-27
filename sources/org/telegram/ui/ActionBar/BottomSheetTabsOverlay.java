package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.RenderNode;
import android.graphics.Shader;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.Surface;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import com.google.zxing.common.detector.MathUtils;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.BottomSheetTabs;
import org.telegram.ui.ActionBar.BottomSheetTabsOverlay;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Text;
import org.telegram.ui.GradientClip;
/* loaded from: classes4.dex */
public class BottomSheetTabsOverlay extends FrameLayout {
    private View actionBarLayout;
    private final AnimatedFloat animatedCount;
    private ValueAnimator animator;
    private Bitmap blurBitmap;
    private Paint blurBitmapPaint;
    private BitmapShader blurBitmapShader;
    private Matrix blurMatrix;
    private final Path clipPath;
    private final RectF clipRect;
    private Drawable closeAllButtonBackground;
    private boolean closeAllButtonBackgroundDark;
    private Text closeAllButtonText;
    private float dismissProgress;
    private Sheet dismissingSheet;
    private BottomSheetTabs.TabDrawable dismissingTab;
    private GradientClip gradientClip;
    private boolean hitCloseAllButton;
    private boolean horizontallySwiping;
    private boolean isOpen;
    private float lastY;
    private final int maximumVelocity;
    private final int minimumVelocity;
    public float offset;
    private ValueAnimator openAnimator;
    private float openProgress;
    private float openingProgress;
    private Sheet openingSheet;
    private BottomSheetTabs.TabDrawable openingTab;
    private float openingTabScroll;
    private final int[] pos;
    private final int[] pos2;
    private final int[] pos3;
    private TabPreview pressTab;
    private boolean pressTabClose;
    private final RectF rect;
    private final RectF rect2;
    private ValueAnimator scrollAnimator;
    private final OverScroller scroller;
    private boolean slowerDismiss;
    private long startTime;
    private float startX;
    private float startY;
    private final ArrayList tabs;
    private BottomSheetTabs tabsView;
    private final RectF tabsViewBounds;
    private VelocityTracker velocityTracker;
    private boolean verticallyScrolling;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 2 extends AnimatorListenerAdapter {
        final /* synthetic */ Sheet val$sheet;
        final /* synthetic */ BottomSheetTabs.WebTabData val$tab;

        2(BottomSheetTabs.WebTabData webTabData, Sheet sheet) {
            this.val$tab = webTabData;
            this.val$sheet = sheet;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onAnimationEnd$0(BottomSheetTabs.WebTabData webTabData, Sheet sheet, Bitmap bitmap) {
            webTabData.previewBitmap = bitmap;
            sheet.getWindowView().setDrawingFromOverlay(false);
            sheet.release();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            int i;
            int i2;
            final BottomSheetTabs.WebTabData webTabData = this.val$tab;
            View view = webTabData.webView;
            if (view == null) {
                view = webTabData.view2;
            }
            if (view != null && webTabData.previewBitmap == null && (i = webTabData.viewWidth) > 0 && (i2 = webTabData.viewHeight) > 0) {
                if (Build.VERSION.SDK_INT >= 26) {
                    final Sheet sheet = this.val$sheet;
                    BottomSheetTabsOverlay.renderHardwareViewToBitmap(view, -webTabData.viewScroll, new Utilities.Callback() { // from class: org.telegram.ui.ActionBar.BottomSheetTabsOverlay$2$$ExternalSyntheticLambda0
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            BottomSheetTabsOverlay.2.lambda$onAnimationEnd$0(BottomSheetTabs.WebTabData.this, sheet, (Bitmap) obj);
                        }
                    });
                    BottomSheetTabsOverlay.this.dismissingSheet = null;
                    BottomSheetTabsOverlay.this.invalidate();
                }
                webTabData.previewBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(this.val$tab.previewBitmap);
                canvas.translate(0.0f, -this.val$tab.viewScroll);
                view.draw(canvas);
            }
            this.val$sheet.getWindowView().setDrawingFromOverlay(false);
            this.val$sheet.release();
            BottomSheetTabsOverlay.this.dismissingSheet = null;
            BottomSheetTabsOverlay.this.invalidate();
        }
    }

    /* loaded from: classes4.dex */
    public interface Sheet {

        /* loaded from: classes4.dex */
        public abstract /* synthetic */ class -CC {
            public static void $default$setLastVisible(Sheet sheet, boolean z) {
            }
        }

        void dismiss(boolean z);

        int getNavigationBarColor(int i);

        SheetView getWindowView();

        boolean isFullSize();

        void release();

        BottomSheetTabs.WebTabData saveState();

        boolean setDialog(BottomSheetTabDialog bottomSheetTabDialog);

        void setLastVisible(boolean z);
    }

    /* loaded from: classes4.dex */
    public interface SheetView {
        float drawInto(Canvas canvas, RectF rectF, float f, RectF rectF2, float f2, boolean z);

        Context getContext();

        RectF getRect();

        void setDrawingFromOverlay(boolean z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class TabPreview {
        private final Paint backgroundPaint;
        private final Paint bitmapPaint;
        public final ButtonBounce bounce;
        public final RectF clickBounds = new RectF();
        private final Path clipPath;
        private ValueAnimator dismissAnimator;
        public float dismissProgress;
        private final float[] dst;
        private final RadialGradient gradient;
        private final Matrix gradientMatrix;
        private final Paint gradientPaint;
        private final Matrix matrix;
        public final View parentView;
        private final Paint shadowPaint;
        private final float[] src;
        private final RectF tabBounds;
        public final BottomSheetTabs.WebTabData tabData;
        public final BottomSheetTabs.TabDrawable tabDrawable;
        public WebView webView;

        public TabPreview(View view, BottomSheetTabs.WebTabData webTabData, BottomSheetTabs.TabDrawable tabDrawable) {
            Paint paint = new Paint(1);
            this.backgroundPaint = paint;
            this.matrix = new Matrix();
            this.src = new float[8];
            this.dst = new float[8];
            this.dismissProgress = 0.0f;
            this.shadowPaint = new Paint(1);
            this.tabBounds = new RectF();
            this.clipPath = new Path();
            this.bitmapPaint = new Paint(3);
            this.gradient = new RadialGradient(0.0f, 0.0f, 255.0f, new int[]{0, 805306368}, new float[]{0.5f, 1.0f}, Shader.TileMode.CLAMP);
            this.gradientMatrix = new Matrix();
            this.gradientPaint = new Paint(1);
            this.parentView = view;
            this.tabData = webTabData;
            this.tabDrawable = tabDrawable;
            this.webView = null;
            this.bounce = new ButtonBounce(view);
            paint.setColor(webTabData.backgroundColor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$animateDismiss$0(ValueAnimator valueAnimator) {
            this.dismissProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            View view = this.parentView;
            if (view != null) {
                view.invalidate();
            }
        }

        public void animateDismiss(final float f) {
            cancelDismissAnimator();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.dismissProgress, f);
            this.dismissAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheetTabsOverlay$TabPreview$$ExternalSyntheticLambda5
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BottomSheetTabsOverlay.TabPreview.this.lambda$animateDismiss$0(valueAnimator);
                }
            });
            this.dismissAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.BottomSheetTabsOverlay.TabPreview.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    TabPreview tabPreview = TabPreview.this;
                    tabPreview.dismissProgress = f;
                    View view = tabPreview.parentView;
                    if (view != null) {
                        view.invalidate();
                    }
                }
            });
            if (Math.abs(f) < 0.1f) {
                AndroidUtilities.applySpring(this.dismissAnimator, 285.0d, 20.0d);
            } else {
                this.dismissAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            }
            this.dismissAnimator.start();
        }

        public void cancelDismissAnimator() {
            ValueAnimator valueAnimator = this.dismissAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
        }

        public void draw(Canvas canvas, RectF rectF, boolean z, float f, float f2, float f3, float f4) {
            float f5;
            float f6;
            Object obj;
            boolean hasDisplayList;
            int width;
            float clamp = f * Utilities.clamp(1.0f - ((Math.abs(this.dismissProgress) - 0.3f) / 0.7f), 1.0f, 0.0f);
            if (clamp <= 0.0f) {
                return;
            }
            float f7 = 1.0f - f3;
            float f8 = f2 * f7;
            float lerp = AndroidUtilities.lerp(1.0f, 1.3f, f8);
            float currentActionBarHeight = f3 * ((AndroidUtilities.statusBarHeight + ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.dp(50.0f));
            canvas.save();
            canvas.rotate(this.dismissProgress * 20.0f, rectF.centerX() + (AndroidUtilities.dp(50.0f) * this.dismissProgress), rectF.bottom + AndroidUtilities.dp(350.0f));
            float scale = this.bounce.getScale(0.01f);
            canvas.scale(scale, scale, rectF.centerX(), rectF.centerY());
            float lerp2 = AndroidUtilities.lerp(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(6.0f), f2);
            if (z) {
                this.shadowPaint.setColor(0);
                this.shadowPaint.setShadowLayer(AndroidUtilities.dp(30.0f), 0.0f, AndroidUtilities.dp(10.0f), Theme.multAlpha(536870912, clamp * f2 * f7));
                canvas.drawRoundRect(rectF, lerp2, lerp2, this.shadowPaint);
                this.backgroundPaint.setAlpha((int) (clamp * 255.0f));
                canvas.drawRoundRect(rectF, lerp2, lerp2, this.backgroundPaint);
            } else {
                this.clipPath.rewind();
                this.clipPath.addRoundRect(rectF, lerp2, lerp2, Path.Direction.CW);
                canvas.save();
                this.shadowPaint.setColor(0);
                float f9 = clamp * f2;
                this.shadowPaint.setShadowLayer(AndroidUtilities.dp(30.0f), 0.0f, AndroidUtilities.dp(10.0f), Theme.multAlpha(536870912, f9 * f7));
                canvas.drawPath(this.clipPath, this.shadowPaint);
                canvas.clipPath(this.clipPath);
                float f10 = clamp * 255.0f * f2;
                int i = (int) f10;
                this.backgroundPaint.setAlpha(i);
                canvas.drawRoundRect(rectF, lerp2, lerp2, this.backgroundPaint);
                canvas.save();
                canvas.translate(rectF.left, rectF.top + (AndroidUtilities.dp(50.0f) * lerp) + currentActionBarHeight);
                canvas.scale(1.0f, AndroidUtilities.lerp(1.0f, 1.25f, f8));
                BottomSheetTabs.WebTabData webTabData = this.tabData;
                if (webTabData != null && (obj = webTabData.previewNode) != null && Build.VERSION.SDK_INT >= 29) {
                    hasDisplayList = BottomSheetTabsOverlay$TabPreview$$ExternalSyntheticApiModelOutline0.m(obj).hasDisplayList();
                    if (hasDisplayList) {
                        RenderNode m = BottomSheetTabsOverlay$TabPreview$$ExternalSyntheticApiModelOutline0.m(this.tabData.previewNode);
                        float width2 = rectF.width();
                        width = m.getWidth();
                        float f11 = width2 / width;
                        canvas.scale(f11, f11);
                        m.setAlpha(f9);
                        canvas.drawRenderNode(m);
                        f5 = lerp2;
                        f6 = currentActionBarHeight;
                        canvas.restore();
                        canvas.save();
                        this.gradientPaint.setAlpha((int) (f10 * f7));
                        this.gradientMatrix.reset();
                        float height = rectF.height() / 255.0f;
                        this.gradientMatrix.postScale(height, height);
                        this.gradientMatrix.postTranslate(rectF.centerX(), rectF.top);
                        this.gradient.setLocalMatrix(this.gradientMatrix);
                        this.gradientPaint.setShader(this.gradient);
                        canvas.drawRect(rectF, this.gradientPaint);
                        canvas.restore();
                        this.tabBounds.set(rectF);
                        RectF rectF2 = this.tabBounds;
                        rectF2.bottom = rectF2.top + Math.min(rectF.height(), AndroidUtilities.dp(50.0f));
                        this.tabBounds.offset(0.0f, f6);
                        this.tabDrawable.setExpandProgress(f2);
                        canvas.scale(1.0f, lerp, this.tabBounds.centerX(), this.tabBounds.top);
                        this.tabDrawable.draw(canvas, this.tabBounds, f5, clamp * clamp, f4);
                        canvas.restore();
                    }
                }
                BottomSheetTabs.WebTabData webTabData2 = this.tabData;
                if (webTabData2 != null && webTabData2.previewBitmap != null) {
                    float width3 = rectF.width() / this.tabData.previewBitmap.getWidth();
                    canvas.scale(width3, width3);
                    this.bitmapPaint.setAlpha(i);
                    canvas.drawBitmap(this.tabData.previewBitmap, 0.0f, 0.0f, this.bitmapPaint);
                } else if (this.webView != null) {
                    float width4 = rectF.width() / this.webView.getWidth();
                    canvas.scale(width4, width4);
                    f5 = lerp2;
                    f6 = currentActionBarHeight;
                    canvas.saveLayerAlpha(0.0f, 0.0f, this.webView.getWidth(), this.webView.getHeight(), i, 31);
                    this.webView.draw(canvas);
                    canvas.restore();
                    canvas.restore();
                    canvas.save();
                    this.gradientPaint.setAlpha((int) (f10 * f7));
                    this.gradientMatrix.reset();
                    float height2 = rectF.height() / 255.0f;
                    this.gradientMatrix.postScale(height2, height2);
                    this.gradientMatrix.postTranslate(rectF.centerX(), rectF.top);
                    this.gradient.setLocalMatrix(this.gradientMatrix);
                    this.gradientPaint.setShader(this.gradient);
                    canvas.drawRect(rectF, this.gradientPaint);
                    canvas.restore();
                    this.tabBounds.set(rectF);
                    RectF rectF22 = this.tabBounds;
                    rectF22.bottom = rectF22.top + Math.min(rectF.height(), AndroidUtilities.dp(50.0f));
                    this.tabBounds.offset(0.0f, f6);
                    this.tabDrawable.setExpandProgress(f2);
                    canvas.scale(1.0f, lerp, this.tabBounds.centerX(), this.tabBounds.top);
                    this.tabDrawable.draw(canvas, this.tabBounds, f5, clamp * clamp, f4);
                    canvas.restore();
                }
                f5 = lerp2;
                f6 = currentActionBarHeight;
                canvas.restore();
                canvas.save();
                this.gradientPaint.setAlpha((int) (f10 * f7));
                this.gradientMatrix.reset();
                float height22 = rectF.height() / 255.0f;
                this.gradientMatrix.postScale(height22, height22);
                this.gradientMatrix.postTranslate(rectF.centerX(), rectF.top);
                this.gradient.setLocalMatrix(this.gradientMatrix);
                this.gradientPaint.setShader(this.gradient);
                canvas.drawRect(rectF, this.gradientPaint);
                canvas.restore();
                this.tabBounds.set(rectF);
                RectF rectF222 = this.tabBounds;
                rectF222.bottom = rectF222.top + Math.min(rectF.height(), AndroidUtilities.dp(50.0f));
                this.tabBounds.offset(0.0f, f6);
                this.tabDrawable.setExpandProgress(f2);
                canvas.scale(1.0f, lerp, this.tabBounds.centerX(), this.tabBounds.top);
                this.tabDrawable.draw(canvas, this.tabBounds, f5, clamp * clamp, f4);
                canvas.restore();
            }
            canvas.restore();
        }

        public boolean isPressed() {
            return this.bounce.isPressed();
        }

        public void setPressed(boolean z) {
            this.bounce.setPressed(z);
        }
    }

    public BottomSheetTabsOverlay(Context context) {
        super(context);
        this.animatedCount = new AnimatedFloat(this, 0L, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
        this.tabsViewBounds = new RectF();
        this.tabs = new ArrayList();
        this.pos = new int[2];
        this.pos2 = new int[2];
        this.pos3 = new int[2];
        this.rect = new RectF();
        this.rect2 = new RectF();
        this.clipRect = new RectF();
        this.clipPath = new Path();
        setWillNotDraw(false);
        this.scroller = new OverScroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.maximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.minimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    }

    private void animateOpen(boolean z) {
        if (this.isOpen == z) {
            return;
        }
        ValueAnimator valueAnimator = this.openAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.isOpen = z;
        BottomSheetTabs bottomSheetTabs = this.tabsView;
        if (bottomSheetTabs != null) {
            bottomSheetTabs.drawTabs = false;
            bottomSheetTabs.invalidate();
        }
        invalidate();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.openProgress, z ? 1.0f : 0.0f);
        this.openAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheetTabsOverlay$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                BottomSheetTabsOverlay.this.lambda$animateOpen$5(valueAnimator2);
            }
        });
        this.openAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.BottomSheetTabsOverlay.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (BottomSheetTabsOverlay.this.tabsView != null) {
                    BottomSheetTabsOverlay.this.tabsView.drawTabs = true;
                    BottomSheetTabsOverlay.this.tabsView.invalidate();
                }
                BottomSheetTabsOverlay bottomSheetTabsOverlay = BottomSheetTabsOverlay.this;
                bottomSheetTabsOverlay.openProgress = bottomSheetTabsOverlay.isOpen ? 1.0f : 0.0f;
                BottomSheetTabsOverlay.this.invalidate();
                if (BottomSheetTabsOverlay.this.isOpen || BottomSheetTabsOverlay.this.openingSheet != null) {
                    return;
                }
                BottomSheetTabsOverlay.this.clearTabs();
            }
        });
        this.openAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.openAnimator.setDuration(320L);
        this.openAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearTabs() {
        this.tabs.clear();
    }

    private void drawDismissingTab(Canvas canvas) {
        if (this.dismissingSheet != null) {
            getLocationOnScreen(this.pos2);
            this.tabsView.getLocationOnScreen(this.pos);
            this.tabsView.getTabBounds(this.rect, 0.0f);
            RectF rectF = this.rect;
            int[] iArr = this.pos;
            int i = iArr[0];
            int[] iArr2 = this.pos2;
            rectF.offset(i - iArr2[0], iArr[1] - iArr2[1]);
            SheetView windowView = this.dismissingSheet.getWindowView();
            RectF rectF2 = this.rect;
            float f = this.dismissProgress;
            float drawInto = windowView.drawInto(canvas, rectF2, f, this.clipRect, f, false);
            if (this.dismissingTab != null) {
                this.clipPath.rewind();
                this.clipPath.addRoundRect(this.clipRect, drawInto, drawInto, Path.Direction.CW);
                canvas.save();
                canvas.clipPath(this.clipPath);
                float dp = this.clipRect.top - (AndroidUtilities.dp(50.0f) * (1.0f - this.dismissProgress));
                RectF rectF3 = this.rect;
                RectF rectF4 = this.clipRect;
                rectF3.set(rectF4.left, dp, rectF4.right, AndroidUtilities.dp(50.0f) + dp);
                this.tabsView.setupTab(this.dismissingTab);
                this.dismissingTab.draw(canvas, this.rect, drawInto, this.dismissProgress, 1.0f);
                canvas.restore();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:80:0x02a6  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x02b1  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x044b  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x044e  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0461  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void drawTabsPreview(Canvas canvas) {
        float f;
        int i;
        int i2;
        Object obj;
        float f2;
        int i3;
        int i4;
        float f3;
        int i5;
        int i6;
        float f4;
        boolean z;
        Sheet sheet;
        int i7;
        BottomSheetTabs bottomSheetTabs;
        Sheet sheet2;
        int[] iArr;
        int[] iArr2;
        int i8;
        if (this.openProgress > 0.0f || this.openingProgress > 0.0f) {
            canvas.save();
            View view = this.actionBarLayout;
            if (view != null) {
                view.getLocationOnScreen(this.pos);
                getLocationOnScreen(this.pos2);
                this.tabsViewBounds.set(this.pos[0] - this.pos2[0], iArr[1] - iArr2[1], i8 + this.actionBarLayout.getWidth(), (this.pos[1] - this.pos2[1]) + this.actionBarLayout.getHeight());
            } else {
                int[] iArr3 = this.pos;
                iArr3[1] = 0;
                iArr3[0] = 0;
                this.tabsViewBounds.set(0.0f, 0.0f, 0.0f, 0.0f);
            }
            canvas.clipRect(this.tabsViewBounds);
            RectF rectF = this.tabsViewBounds;
            canvas.translate(rectF.left, rectF.top);
            float width = this.tabsViewBounds.width();
            float height = this.tabsViewBounds.height();
            if (this.blurBitmap != null) {
                this.blurMatrix.reset();
                float width2 = this.tabsViewBounds.width() / this.blurBitmap.getWidth();
                this.blurMatrix.postScale(width2, width2);
                this.blurBitmapShader.setLocalMatrix(this.blurMatrix);
                this.blurBitmapPaint.setAlpha((int) (this.openProgress * 255.0f));
                canvas.drawRect(0.0f, 0.0f, width, height, this.blurBitmapPaint);
            }
            canvas.saveLayerAlpha(0.0f, 0.0f, width, height, NotificationCenter.messagePlayingSpeedChanged, 31);
            float dp = AndroidUtilities.statusBarHeight + AndroidUtilities.dp(40.0f) + AndroidUtilities.dp(55.0f);
            float dp2 = AndroidUtilities.dp(68.0f);
            int min = (int) Math.min(AndroidUtilities.dp(340.0f), 0.95f * width);
            int height2 = (int) (AndroidUtilities.isTablet() ? this.tabsViewBounds.height() * 0.5f : 0.75f * height);
            float f5 = width / 2.0f;
            int i9 = 0;
            float f6 = 0.0f;
            while (true) {
                f = 1.0f;
                if (i9 >= this.tabs.size()) {
                    break;
                }
                if (((TabPreview) this.tabs.get(i9)).tabDrawable.index < 0) {
                    f = 0.0f;
                }
                f6 += f;
                i9++;
            }
            float f7 = this.animatedCount.set(f6);
            float lerp = AndroidUtilities.lerp(0.0f, 1.0f - Utilities.clamp(getScrollWindow() <= 0.0f ? 0.0f : ((getScrollMin() - getScrollOffset()) / (getScrollWindow() * 0.15f)) * 0.2f, 1.0f, 0.0f), this.openProgress);
            int i10 = 0;
            int i11 = -1;
            for (int i12 = 1; i10 < this.tabs.size() + i12; i12 = 1) {
                if (i10 != this.tabs.size()) {
                    obj = this.tabs.get(i10);
                } else if (i11 < 0 || this.openingProgress <= 0.5f) {
                    i6 = i10;
                    i5 = i11;
                    f3 = lerp;
                    i4 = height2;
                    i3 = min;
                    f2 = dp2;
                    f4 = dp;
                    i11 = i5;
                    i10 = i6 + 1;
                    dp = f4;
                    lerp = f3;
                    height2 = i4;
                    min = i3;
                    dp2 = f2;
                    f = 1.0f;
                } else {
                    obj = this.tabs.get(i11);
                }
                TabPreview tabPreview = (TabPreview) obj;
                if (i10 >= this.tabs.size() || tabPreview.tabDrawable != this.openingTab || this.openingProgress <= 0.5f) {
                    BottomSheetTabs.TabDrawable tabDrawable = tabPreview.tabDrawable;
                    BottomSheetTabs.TabDrawable tabDrawable2 = this.openingTab;
                    float f8 = tabDrawable == tabDrawable2 ? 1.0f : lerp;
                    float f9 = tabDrawable == tabDrawable2 ? this.openingProgress : 0.0f;
                    float position = (f7 - f) - tabDrawable.getPosition();
                    float max = tabPreview.tabDrawable == this.openingTab ? this.openingTabScroll : (position - Math.max(getScrollMin(), getScrollOffset())) / getScrollWindow();
                    Math.max(max, 0.0f);
                    int i13 = i10;
                    Math.max(Math.min(max, 1.0f), -4.0f);
                    float dp3 = (AndroidUtilities.dp(6.0f) * Math.min(5.0f, position)) + dp;
                    int i14 = i11;
                    float f10 = height2;
                    float f11 = dp3 + ((((height - dp2) - (0.26f * f10)) - dp3) * max);
                    float f12 = min / 2.0f;
                    i4 = height2;
                    this.rect2.set(f5 - f12, f11, f5 + f12, f10 + f11);
                    BottomSheetTabs.TabDrawable tabDrawable3 = tabPreview.tabDrawable;
                    BottomSheetTabs.TabDrawable tabDrawable4 = this.openingTab;
                    if (tabDrawable3 != tabDrawable4) {
                        RectF rectF2 = this.rect2;
                        if ((rectF2.top > height || rectF2.bottom < 0.0f || lerp < 0.1f) && position < f7 - 3.0f) {
                            z = true;
                            sheet = this.openingSheet;
                            if (sheet == null && tabDrawable3 == tabDrawable4) {
                                this.rect.set(sheet.getWindowView().getRect());
                                RectF rectF3 = this.rect2;
                                AndroidUtilities.lerpCentered(rectF3, this.rect, f9, rectF3);
                                i7 = min;
                            } else {
                                i7 = min;
                                this.tabsView.getTabBounds(this.rect, Utilities.clamp(tabDrawable3.getPosition(), 1.0f, 0.0f));
                                this.rect.offset(this.tabsView.getX(), this.tabsView.getY());
                                RectF rectF4 = this.rect;
                                RectF rectF5 = this.rect2;
                                AndroidUtilities.lerpCentered(rectF4, rectF5, lerp, rectF5);
                            }
                            bottomSheetTabs = this.tabsView;
                            if (bottomSheetTabs != null) {
                                bottomSheetTabs.setupTab(tabPreview.tabDrawable);
                            }
                            if (tabPreview.tabDrawable != this.openingTab) {
                                RectF rectF6 = this.rect2;
                                if (rectF6.top > height || rectF6.bottom < 0.0f) {
                                    f4 = dp;
                                    i6 = i13;
                                    i5 = i14;
                                    i3 = i7;
                                    f3 = lerp;
                                    f2 = dp2;
                                    i11 = i5;
                                    i10 = i6 + 1;
                                    dp = f4;
                                    lerp = f3;
                                    height2 = i4;
                                    min = i3;
                                    dp2 = f2;
                                    f = 1.0f;
                                }
                            }
                            canvas.save();
                            tabPreview.clickBounds.set(this.rect2);
                            tabPreview.matrix.reset();
                            tabPreview.src[0] = this.rect2.left;
                            tabPreview.src[1] = this.rect2.top;
                            tabPreview.src[2] = this.rect2.right;
                            tabPreview.src[3] = this.rect2.top;
                            tabPreview.src[4] = this.rect2.right;
                            float[] fArr = tabPreview.src;
                            RectF rectF7 = this.rect2;
                            fArr[5] = rectF7.top + (rectF7.height() * 1.0f);
                            tabPreview.src[6] = this.rect2.left;
                            float[] fArr2 = tabPreview.src;
                            RectF rectF8 = this.rect2;
                            fArr2[7] = rectF8.top + (rectF8.height() * 1.0f);
                            tabPreview.dst[0] = this.rect2.left;
                            tabPreview.dst[1] = this.rect2.top - AndroidUtilities.dp(0.0f);
                            tabPreview.dst[2] = this.rect2.right;
                            tabPreview.dst[3] = this.rect2.top - AndroidUtilities.dp(0.0f);
                            float f13 = (1.0f - f9) * f8;
                            float f14 = lerp;
                            tabPreview.dst[4] = this.rect2.centerX() + ((this.rect2.width() / 2.0f) * AndroidUtilities.lerp(1.0f, 0.83f, f13));
                            tabPreview.dst[5] = (this.rect2.top - AndroidUtilities.dp(0.0f)) + (((this.rect2.height() * 1.0f) + AndroidUtilities.dp(0.0f)) * AndroidUtilities.lerp(1.0f, 0.6f, f13));
                            tabPreview.dst[6] = this.rect2.centerX() - ((this.rect2.width() / 2.0f) * AndroidUtilities.lerp(1.0f, 0.83f, f13));
                            tabPreview.dst[7] = (this.rect2.top - AndroidUtilities.dp(0.0f)) + (((this.rect2.height() * 1.0f) + AndroidUtilities.dp(0.0f)) * AndroidUtilities.lerp(1.0f, 0.6f, f13));
                            tabPreview.matrix.setPolyToPoly(tabPreview.src, 0, tabPreview.dst, 0, 4);
                            canvas.concat(tabPreview.matrix);
                            RectF rectF9 = this.rect2;
                            BottomSheetTabs.TabDrawable tabDrawable5 = tabPreview.tabDrawable;
                            float lerp2 = tabDrawable5 != this.openingTab ? 1.0f : AndroidUtilities.lerp(tabDrawable5.getAlpha(), 1.0f, this.openProgress);
                            if (tabPreview.tabDrawable == this.openingTab) {
                                f13 = 1.0f;
                            }
                            i6 = i13;
                            i5 = i14;
                            f3 = f14;
                            boolean z2 = z;
                            float f15 = lerp2;
                            i3 = i7;
                            float f16 = f13;
                            f2 = dp2;
                            f4 = dp;
                            tabPreview.draw(canvas, rectF9, z2, f15, f16, f9, AndroidUtilities.lerp(Utilities.clamp01((position - f7) + 2.0f), 1.0f, Utilities.clamp01((f8 - 0.1f) / 0.8f)));
                            sheet2 = this.openingSheet;
                            if (sheet2 != null && tabPreview.tabDrawable == this.openingTab) {
                                SheetView windowView = sheet2.getWindowView();
                                RectF rectF10 = this.rect2;
                                windowView.drawInto(canvas, rectF10, 1.0f, rectF10, f9, true);
                            }
                            canvas.restore();
                            i11 = i5;
                            i10 = i6 + 1;
                            dp = f4;
                            lerp = f3;
                            height2 = i4;
                            min = i3;
                            dp2 = f2;
                            f = 1.0f;
                        }
                    }
                    z = false;
                    sheet = this.openingSheet;
                    if (sheet == null) {
                    }
                    i7 = min;
                    this.tabsView.getTabBounds(this.rect, Utilities.clamp(tabDrawable3.getPosition(), 1.0f, 0.0f));
                    this.rect.offset(this.tabsView.getX(), this.tabsView.getY());
                    RectF rectF42 = this.rect;
                    RectF rectF52 = this.rect2;
                    AndroidUtilities.lerpCentered(rectF42, rectF52, lerp, rectF52);
                    bottomSheetTabs = this.tabsView;
                    if (bottomSheetTabs != null) {
                    }
                    if (tabPreview.tabDrawable != this.openingTab) {
                    }
                    canvas.save();
                    tabPreview.clickBounds.set(this.rect2);
                    tabPreview.matrix.reset();
                    tabPreview.src[0] = this.rect2.left;
                    tabPreview.src[1] = this.rect2.top;
                    tabPreview.src[2] = this.rect2.right;
                    tabPreview.src[3] = this.rect2.top;
                    tabPreview.src[4] = this.rect2.right;
                    float[] fArr3 = tabPreview.src;
                    RectF rectF72 = this.rect2;
                    fArr3[5] = rectF72.top + (rectF72.height() * 1.0f);
                    tabPreview.src[6] = this.rect2.left;
                    float[] fArr22 = tabPreview.src;
                    RectF rectF82 = this.rect2;
                    fArr22[7] = rectF82.top + (rectF82.height() * 1.0f);
                    tabPreview.dst[0] = this.rect2.left;
                    tabPreview.dst[1] = this.rect2.top - AndroidUtilities.dp(0.0f);
                    tabPreview.dst[2] = this.rect2.right;
                    tabPreview.dst[3] = this.rect2.top - AndroidUtilities.dp(0.0f);
                    float f132 = (1.0f - f9) * f8;
                    float f142 = lerp;
                    tabPreview.dst[4] = this.rect2.centerX() + ((this.rect2.width() / 2.0f) * AndroidUtilities.lerp(1.0f, 0.83f, f132));
                    tabPreview.dst[5] = (this.rect2.top - AndroidUtilities.dp(0.0f)) + (((this.rect2.height() * 1.0f) + AndroidUtilities.dp(0.0f)) * AndroidUtilities.lerp(1.0f, 0.6f, f132));
                    tabPreview.dst[6] = this.rect2.centerX() - ((this.rect2.width() / 2.0f) * AndroidUtilities.lerp(1.0f, 0.83f, f132));
                    tabPreview.dst[7] = (this.rect2.top - AndroidUtilities.dp(0.0f)) + (((this.rect2.height() * 1.0f) + AndroidUtilities.dp(0.0f)) * AndroidUtilities.lerp(1.0f, 0.6f, f132));
                    tabPreview.matrix.setPolyToPoly(tabPreview.src, 0, tabPreview.dst, 0, 4);
                    canvas.concat(tabPreview.matrix);
                    RectF rectF92 = this.rect2;
                    BottomSheetTabs.TabDrawable tabDrawable52 = tabPreview.tabDrawable;
                    if (tabDrawable52 != this.openingTab) {
                    }
                    if (tabPreview.tabDrawable == this.openingTab) {
                    }
                    i6 = i13;
                    i5 = i14;
                    f3 = f142;
                    boolean z22 = z;
                    float f152 = lerp2;
                    i3 = i7;
                    float f162 = f132;
                    f2 = dp2;
                    f4 = dp;
                    tabPreview.draw(canvas, rectF92, z22, f152, f162, f9, AndroidUtilities.lerp(Utilities.clamp01((position - f7) + 2.0f), 1.0f, Utilities.clamp01((f8 - 0.1f) / 0.8f)));
                    sheet2 = this.openingSheet;
                    if (sheet2 != null) {
                        SheetView windowView2 = sheet2.getWindowView();
                        RectF rectF102 = this.rect2;
                        windowView2.drawInto(canvas, rectF102, 1.0f, rectF102, f9, true);
                    }
                    canvas.restore();
                    i11 = i5;
                    i10 = i6 + 1;
                    dp = f4;
                    lerp = f3;
                    height2 = i4;
                    min = i3;
                    dp2 = f2;
                    f = 1.0f;
                } else {
                    i11 = i10;
                    i6 = i11;
                    f3 = lerp;
                    i4 = height2;
                    i3 = min;
                    f2 = dp2;
                    f4 = dp;
                    i10 = i6 + 1;
                    dp = f4;
                    lerp = f3;
                    height2 = i4;
                    min = i3;
                    dp2 = f2;
                    f = 1.0f;
                }
            }
            float f17 = dp;
            canvas.save();
            if (this.gradientClip == null) {
                this.gradientClip = new GradientClip();
            }
            RectF rectF11 = AndroidUtilities.rectTmp;
            rectF11.set(0.0f, 0.0f, width, f17);
            this.gradientClip.draw(canvas, rectF11, true, this.openProgress);
            canvas.restore();
            canvas.restore();
            if (this.closeAllButtonText == null) {
                this.closeAllButtonText = new Text(LocaleController.getString(R.string.BotCloseAllTabs), 14.0f, AndroidUtilities.bold());
            }
            if (this.closeAllButtonBackground == null || this.closeAllButtonBackgroundDark != Theme.isCurrentThemeDark()) {
                boolean isCurrentThemeDark = Theme.isCurrentThemeDark();
                this.closeAllButtonBackgroundDark = isCurrentThemeDark;
                if (isCurrentThemeDark) {
                    i = 553648127;
                    i2 = 872415231;
                } else {
                    i = 771751936;
                    i2 = 1140850688;
                }
                this.closeAllButtonBackground = Theme.createSimpleSelectorRoundRectDrawable(64, i, i2);
                this.closeAllButtonBackground.setCallback(this);
            }
            float currentWidth = this.closeAllButtonText.getCurrentWidth() + AndroidUtilities.dp(24.0f);
            float f18 = (width - currentWidth) / 2.0f;
            this.closeAllButtonBackground.setBounds((int) f18, (int) ((f17 - (AndroidUtilities.dp(95.0f) / 2.0f)) - AndroidUtilities.dp(14.0f)), (int) ((width + currentWidth) / 2.0f), (int) ((f17 - (AndroidUtilities.dp(95.0f) / 2.0f)) + AndroidUtilities.dp(14.0f)));
            this.closeAllButtonBackground.setAlpha((int) (this.openProgress * 255.0f));
            this.closeAllButtonBackground.draw(canvas);
            this.closeAllButtonText.draw(canvas, f18 + AndroidUtilities.dp(12.0f), f17 - (AndroidUtilities.dp(95.0f) / 2.0f), -1, this.openProgress);
            canvas.restore();
        }
    }

    private float getScrollStep() {
        return AndroidUtilities.dp(200.0f);
    }

    private TabPreview getTabAt(float f, float f2) {
        if (this.openProgress < 1.0f) {
            return null;
        }
        for (int size = this.tabs.size() - 1; size >= 0; size--) {
            TabPreview tabPreview = (TabPreview) this.tabs.get(size);
            if (Math.abs(tabPreview.dismissProgress) < 0.4f && tabPreview.clickBounds.contains(f, f2)) {
                return tabPreview;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateOpen$5(ValueAnimator valueAnimator) {
        this.openProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissSheet$3(ValueAnimator valueAnimator) {
        this.dismissProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchTouchEvent$0(TabPreview tabPreview, Boolean bool) {
        if (!bool.booleanValue()) {
            tabPreview.animateDismiss(0.0f);
            return;
        }
        tabPreview.animateDismiss(tabPreview.dismissProgress < 0.0f ? -1.0f : 1.0f);
        scrollTo(Utilities.clamp(this.offset, getScrollMax(false), getScrollMin(false)));
        if (this.tabsView.getTabs().isEmpty()) {
            closeTabsView();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchTouchEvent$1(TabPreview tabPreview, Boolean bool) {
        if (!bool.booleanValue()) {
            tabPreview.animateDismiss(0.0f);
            return;
        }
        tabPreview.animateDismiss(1.0f);
        scrollTo(Utilities.clamp(this.offset, getScrollMax(false), getScrollMin(false)));
        if (this.tabsView.getTabs().isEmpty()) {
            closeTabsView();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scrollTo$4(ValueAnimator valueAnimator) {
        this.offset = ((Float) valueAnimator.getAnimatedValue()).floatValue();
    }

    private void prepareBlur(View view) {
        AndroidUtilities.makingGlobalBlurBitmap = true;
        this.blurBitmap = AndroidUtilities.makeBlurBitmap(view, 14.0f, 14);
        AndroidUtilities.makingGlobalBlurBitmap = false;
        Paint paint = new Paint(1);
        this.blurBitmapPaint = paint;
        Bitmap bitmap = this.blurBitmap;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
        this.blurBitmapShader = bitmapShader;
        paint.setShader(bitmapShader);
        ColorMatrix colorMatrix = new ColorMatrix();
        AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, Theme.isCurrentThemeDark() ? 0.08f : 0.25f);
        this.blurBitmapPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        this.blurMatrix = new Matrix();
    }

    private void prepareTabs() {
        BottomSheetTabs.TabDrawable tabDrawable;
        ArrayList<BottomSheetTabs.WebTabData> tabs = this.tabsView.getTabs();
        ArrayList<BottomSheetTabs.TabDrawable> tabDrawables = this.tabsView.getTabDrawables();
        for (int size = tabs.size() - 1; size >= 0; size--) {
            BottomSheetTabs.WebTabData webTabData = tabs.get(size);
            int i = 0;
            while (true) {
                if (i >= tabDrawables.size()) {
                    tabDrawable = null;
                    break;
                }
                tabDrawable = tabDrawables.get(i);
                if (tabDrawable.tab == webTabData) {
                    break;
                }
                i++;
            }
            if (tabDrawable != null) {
                this.tabs.add(new TabPreview(this, webTabData, tabDrawable));
            }
        }
        this.animatedCount.set(this.tabs.size(), true);
        setScrollOffset(getScrollMax());
    }

    public static void renderHardwareViewToBitmap(View view, float f, final Utilities.Callback callback) {
        Canvas lockHardwareCanvas;
        if (view == null || callback == null || view.getWidth() <= 0 || view.getHeight() <= 0) {
            if (callback != null) {
                callback.run(null);
                return;
            }
            return;
        }
        final SurfaceTexture surfaceTexture = new SurfaceTexture(false);
        surfaceTexture.setDefaultBufferSize(view.getWidth(), view.getHeight());
        final Surface surface = new Surface(surfaceTexture);
        final Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        lockHardwareCanvas = surface.lockHardwareCanvas();
        lockHardwareCanvas.translate(0.0f, f);
        view.draw(lockHardwareCanvas);
        surface.unlockCanvasAndPost(lockHardwareCanvas);
        PixelCopy.request(surface, createBitmap, new PixelCopy.OnPixelCopyFinishedListener() { // from class: org.telegram.ui.ActionBar.BottomSheetTabsOverlay.5
            @Override // android.view.PixelCopy.OnPixelCopyFinishedListener
            public void onPixelCopyFinished(int i) {
                Utilities.Callback callback2;
                Bitmap bitmap;
                if (i == 0) {
                    callback2 = Utilities.Callback.this;
                    bitmap = createBitmap;
                } else {
                    createBitmap.recycle();
                    callback2 = Utilities.Callback.this;
                    bitmap = null;
                }
                callback2.run(bitmap);
                surface.release();
                surfaceTexture.release();
            }
        }, new Handler());
    }

    private void scrollTo(float f) {
        ValueAnimator valueAnimator = this.scrollAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.scrollAnimator = null;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.offset, f);
        this.scrollAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheetTabsOverlay$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                BottomSheetTabsOverlay.this.lambda$scrollTo$4(valueAnimator2);
            }
        });
        this.scrollAnimator.setDuration(250L);
        this.scrollAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.scrollAnimator.start();
    }

    public void closeTabsView() {
        animateOpen(false);
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.scroller.computeScrollOffset()) {
            setScrollOffset(this.scroller.getCurrY() / getScrollStep());
            postInvalidateOnAnimation();
        }
    }

    public boolean dismissSheet(Sheet sheet) {
        ValueAnimator valueAnimator;
        if (sheet == null || this.tabsView == null) {
            return false;
        }
        if ((this.dismissingSheet != null || this.openingSheet != null) && (valueAnimator = this.animator) != null) {
            valueAnimator.end();
            this.animator = null;
        }
        this.dismissingSheet = sheet;
        sheet.setLastVisible(false);
        sheet.getWindowView().setDrawingFromOverlay(true);
        invalidate();
        ValueAnimator valueAnimator2 = this.animator;
        if (valueAnimator2 != null) {
            valueAnimator2.cancel();
        }
        BottomSheetTabs.WebTabData saveState = sheet.saveState();
        this.dismissingTab = this.tabsView.pushTab(saveState);
        this.dismissProgress = 0.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.animator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheetTabsOverlay$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                BottomSheetTabsOverlay.this.lambda$dismissSheet$3(valueAnimator3);
            }
        });
        this.animator.addListener(new 2(saveState, sheet));
        if (this.slowerDismiss || sheet.isFullSize()) {
            AndroidUtilities.applySpring(this.animator, 220.0d, 30.0d, 1.0d);
        } else {
            AndroidUtilities.applySpring(this.animator, 350.0d, 30.0d, 1.0d);
        }
        this.animator.start();
        this.slowerDismiss = false;
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawDismissingTab(canvas);
        drawTabsPreview(canvas);
    }

    /* JADX WARN: Code restructure failed: missing block: B:185:0x04b5, code lost:
        if (((float) (java.lang.System.currentTimeMillis() - r25.startTime)) <= (android.view.ViewConfiguration.getTapTimeout() * 1.2f)) goto L184;
     */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!(AndroidUtilities.isTablet() && motionEvent.getAction() == 0 && !this.tabsViewBounds.contains(motionEvent.getX(), motionEvent.getY())) && this.openProgress > 0.0f) {
            if (this.velocityTracker == null) {
                this.velocityTracker = VelocityTracker.obtain();
            }
            this.velocityTracker.addMovement(motionEvent);
            if (motionEvent.getAction() == 0) {
                this.startTime = System.currentTimeMillis();
                this.startX = motionEvent.getX();
                this.startY = motionEvent.getY();
                this.pressTab = getTabAt(motionEvent.getX(), motionEvent.getY());
                Drawable drawable = this.closeAllButtonBackground;
                boolean z = drawable != null && drawable.getBounds().contains((int) motionEvent.getX(), (int) motionEvent.getY());
                this.hitCloseAllButton = z;
                if (z) {
                    this.pressTab = null;
                }
                Drawable drawable2 = this.closeAllButtonBackground;
                if (drawable2 != null) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        drawable2.setHotspot(motionEvent.getX(), motionEvent.getY());
                    }
                    this.closeAllButtonBackground.setState(this.hitCloseAllButton ? new int[]{16842919, 16842910} : new int[0]);
                }
                this.verticallyScrolling = false;
                this.horizontallySwiping = false;
                this.pressTabClose = false;
                TabPreview tabPreview = this.pressTab;
                if (tabPreview != null) {
                    tabPreview.cancelDismissAnimator();
                    boolean contains = this.pressTab.tabDrawable.closeRipple.getBounds().contains((int) (motionEvent.getX() - this.pressTab.clickBounds.left), (int) ((motionEvent.getY() - this.pressTab.clickBounds.top) - AndroidUtilities.dp(24.0f)));
                    this.pressTabClose = contains;
                    if (Build.VERSION.SDK_INT >= 21 && contains) {
                        this.pressTab.tabDrawable.closeRipple.setHotspot((int) (motionEvent.getX() - this.rect.left), (int) (motionEvent.getY() - this.rect.centerY()));
                    }
                    this.pressTab.setPressed(!this.pressTabClose);
                    this.pressTab.tabDrawable.closeRipple.setState(this.pressTabClose ? new int[]{16842919, 16842910} : new int[0]);
                }
                this.lastY = motionEvent.getY();
                if (!this.scroller.isFinished()) {
                    this.scroller.abortAnimation();
                }
                ValueAnimator valueAnimator = this.scrollAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    this.scrollAnimator = null;
                }
            } else if (motionEvent.getAction() == 2) {
                TabPreview tabPreview2 = this.pressTab;
                if (tabPreview2 != null) {
                    if (tabPreview2.isPressed()) {
                        if (!this.horizontallySwiping && !this.verticallyScrolling && MathUtils.distance(this.startX, motionEvent.getY(), motionEvent.getX(), motionEvent.getY()) > AndroidUtilities.touchSlop) {
                            this.horizontallySwiping = true;
                        }
                        if (!this.verticallyScrolling && !this.horizontallySwiping && MathUtils.distance(motionEvent.getX(), this.startY, motionEvent.getX(), motionEvent.getY()) > AndroidUtilities.touchSlop) {
                            if (!this.scroller.isFinished()) {
                                this.scroller.abortAnimation();
                            }
                            ValueAnimator valueAnimator2 = this.scrollAnimator;
                            if (valueAnimator2 != null) {
                                valueAnimator2.cancel();
                                this.scrollAnimator = null;
                            }
                            this.verticallyScrolling = true;
                        }
                        if (this.tabsView != null && (this.verticallyScrolling || this.horizontallySwiping)) {
                            this.pressTab.setPressed(false);
                            this.pressTab.cancelDismissAnimator();
                        }
                    } else {
                        if (!this.pressTabClose && !this.horizontallySwiping && !this.verticallyScrolling && MathUtils.distance(this.startX, motionEvent.getY(), motionEvent.getX(), motionEvent.getY()) > AndroidUtilities.touchSlop) {
                            this.horizontallySwiping = true;
                        }
                        if (!this.pressTabClose && !this.verticallyScrolling && !this.horizontallySwiping && MathUtils.distance(motionEvent.getX(), this.startY, motionEvent.getX(), motionEvent.getY()) > AndroidUtilities.touchSlop) {
                            if (!this.scroller.isFinished()) {
                                this.scroller.abortAnimation();
                            }
                            ValueAnimator valueAnimator3 = this.scrollAnimator;
                            if (valueAnimator3 != null) {
                                valueAnimator3.cancel();
                                this.scrollAnimator = null;
                            }
                            this.verticallyScrolling = true;
                        }
                        if (this.pressTabClose) {
                            boolean contains2 = this.pressTab.tabDrawable.closeRipple.getBounds().contains((int) (motionEvent.getX() - this.pressTab.clickBounds.left), (int) ((motionEvent.getY() - this.pressTab.clickBounds.top) - AndroidUtilities.dp(24.0f)));
                            this.pressTabClose = contains2;
                            if (!contains2) {
                                this.pressTab.tabDrawable.closeRipple.setState(new int[0]);
                            }
                        }
                    }
                    if (!this.pressTab.isPressed()) {
                        if (this.horizontallySwiping) {
                            this.pressTab.dismissProgress = (motionEvent.getX() - this.startX) / AndroidUtilities.dp(300.0f);
                        } else if (this.verticallyScrolling) {
                            float y = motionEvent.getY() - this.lastY;
                            if (this.offset < getScrollMin()) {
                                y *= 1.0f - (Utilities.clamp((getScrollMin() - this.offset) / getScrollStep(), 1.0f, 0.0f) * 0.5f);
                            }
                            setScrollOffset(Utilities.clamp(((getScrollOffset() * getScrollStep()) - y) / getScrollStep(), getScrollMax(), getScrollMin() - (getScrollStep() * 1.4f)));
                            invalidate();
                        }
                    }
                    invalidate();
                }
                Drawable drawable3 = this.closeAllButtonBackground;
                if (drawable3 != null && this.hitCloseAllButton) {
                    boolean z2 = this.pressTab == null && drawable3.getBounds().contains((int) motionEvent.getX(), (int) motionEvent.getY());
                    this.hitCloseAllButton = z2;
                    if (!z2) {
                        this.closeAllButtonBackground.setState(new int[0]);
                    }
                }
                this.lastY = motionEvent.getY();
            } else if (motionEvent.getAction() == 1) {
                TabPreview tabPreview3 = this.pressTab;
                if (tabPreview3 != null) {
                    if (this.tabsView == null || Math.abs(tabPreview3.dismissProgress) <= 0.4f) {
                        this.pressTab.animateDismiss(0.0f);
                        if (this.tabsView != null && this.pressTab.isPressed()) {
                            closeTabsView();
                            TabPreview tabPreview4 = this.pressTab;
                            tabPreview4.webView = null;
                            this.tabsView.openTab(tabPreview4.tabData);
                        } else if (this.verticallyScrolling) {
                            if (this.offset < getScrollMin() - (getScrollWindow() * 0.15f)) {
                                closeTabsView();
                            } else if (this.offset < getScrollMin()) {
                                scrollTo(getScrollMin());
                            } else {
                                this.velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
                                float yVelocity = this.velocityTracker.getYVelocity();
                                if (Math.abs(yVelocity) > this.minimumVelocity) {
                                    this.scroller.fling(0, (int) (getScrollOffset() * getScrollStep()), 0, (int) (-yVelocity), 0, 0, (int) (getScrollMin() * getScrollStep()), (int) (getScrollMax() * getScrollStep()), 0, (int) (getScrollStep() * 0.1f));
                                } else {
                                    this.scroller.startScroll(0, (int) (getScrollOffset() * getScrollStep()), 0, 0, 0);
                                }
                            }
                            this.velocityTracker.recycle();
                            this.velocityTracker = null;
                            postInvalidateOnAnimation();
                        }
                    } else {
                        final TabPreview tabPreview5 = this.pressTab;
                        this.tabsView.removeTab(tabPreview5.tabData, new Utilities.Callback() { // from class: org.telegram.ui.ActionBar.BottomSheetTabsOverlay$$ExternalSyntheticLambda3
                            @Override // org.telegram.messenger.Utilities.Callback
                            public final void run(Object obj) {
                                BottomSheetTabsOverlay.this.lambda$dispatchTouchEvent$0(tabPreview5, (Boolean) obj);
                            }
                        });
                    }
                    this.pressTab.setPressed(false);
                    if (this.pressTabClose) {
                        this.pressTabClose = this.pressTab.tabDrawable.closeRipple.getBounds().contains((int) (motionEvent.getX() - this.pressTab.clickBounds.left), (int) ((motionEvent.getY() - this.pressTab.clickBounds.top) - AndroidUtilities.dp(24.0f)));
                    }
                    if (this.pressTabClose) {
                        final TabPreview tabPreview6 = this.pressTab;
                        this.tabsView.removeTab(tabPreview6.tabData, new Utilities.Callback() { // from class: org.telegram.ui.ActionBar.BottomSheetTabsOverlay$$ExternalSyntheticLambda4
                            @Override // org.telegram.messenger.Utilities.Callback
                            public final void run(Object obj) {
                                BottomSheetTabsOverlay.this.lambda$dispatchTouchEvent$1(tabPreview6, (Boolean) obj);
                            }
                        });
                    }
                    this.pressTab.tabDrawable.closeRipple.setState(new int[0]);
                } else {
                    if (this.hitCloseAllButton) {
                        this.tabsView.removeAll();
                    } else if (MathUtils.distance(this.startX, this.startY, motionEvent.getX(), motionEvent.getY()) <= AndroidUtilities.touchSlop) {
                        if (!this.verticallyScrolling) {
                            if (!this.horizontallySwiping) {
                            }
                        }
                    }
                    closeTabsView();
                }
                this.pressTab = null;
                this.pressTabClose = false;
                VelocityTracker velocityTracker = this.velocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    this.velocityTracker = null;
                }
                this.hitCloseAllButton = false;
                Drawable drawable4 = this.closeAllButtonBackground;
                if (drawable4 != null) {
                    drawable4.setState(new int[0]);
                }
            } else if (motionEvent.getAction() == 3) {
                TabPreview tabPreview7 = this.pressTab;
                if (tabPreview7 != null) {
                    tabPreview7.animateDismiss(0.0f);
                    this.pressTab.setPressed(false);
                    this.pressTab.tabDrawable.closeRipple.setState(new int[0]);
                }
                this.pressTab = null;
                this.pressTabClose = false;
                VelocityTracker velocityTracker2 = this.velocityTracker;
                if (velocityTracker2 != null) {
                    velocityTracker2.recycle();
                    this.velocityTracker = null;
                }
                this.hitCloseAllButton = false;
                Drawable drawable5 = this.closeAllButtonBackground;
                if (drawable5 != null) {
                    drawable5.setState(new int[0]);
                }
            }
            return true;
        }
        return false;
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        return false;
    }

    public float getScrollMax() {
        return getScrollMax(true);
    }

    public float getScrollMax(boolean z) {
        return (getScrollRange(z) - getScrollWindow(z)) - ((getScrollWindow(z) / 3.0f) * Utilities.clamp(4.0f - getScrollRange(z), 0.5f, 0.0f));
    }

    public float getScrollMin() {
        return getScrollMin(true);
    }

    public float getScrollMin(boolean z) {
        return ((-getScrollWindow()) / 3.0f) * Utilities.clamp(getScrollRange(z), 1.0f, 0.0f);
    }

    public float getScrollOffset() {
        return this.offset;
    }

    public float getScrollRange() {
        return getScrollRange(true);
    }

    public float getScrollRange(boolean z) {
        float f = 0.0f;
        for (int i = 0; i < this.tabs.size(); i++) {
            f += ((TabPreview) this.tabs.get(i)).tabDrawable.index >= 0 ? 1.0f : 0.0f;
        }
        return z ? this.animatedCount.set(f) : f;
    }

    public float getScrollWindow() {
        return Math.min(3.0f, getScrollRange());
    }

    public float getScrollWindow(boolean z) {
        return Math.min(3.0f, getScrollRange(z));
    }

    public boolean onBackPressed() {
        if (this.isOpen) {
            closeTabsView();
            return true;
        }
        return false;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2) + AndroidUtilities.navigationBarHeight, 1073741824));
    }

    public void openTabsView() {
        BottomSheetTabs bottomSheetTabs = this.tabsView;
        if (bottomSheetTabs == null || !(bottomSheetTabs.getParent() instanceof View)) {
            return;
        }
        stopAnimations();
        View view = (View) this.tabsView.getParent();
        this.actionBarLayout = view;
        if (view != null) {
            view.getLocationOnScreen(this.pos);
        } else {
            int[] iArr = this.pos;
            iArr[1] = 0;
            iArr[0] = 0;
        }
        getLocationOnScreen(this.pos2);
        RectF rectF = this.tabsViewBounds;
        int[] iArr2 = this.pos;
        int i = iArr2[0];
        int[] iArr3 = this.pos2;
        int i2 = i - iArr3[0];
        rectF.set(i2, iArr2[1] - iArr3[1], i2 + this.actionBarLayout.getWidth(), (this.pos[1] - this.pos2[1]) + this.actionBarLayout.getHeight());
        prepareBlur(this.actionBarLayout);
        clearTabs();
        prepareTabs();
        animateOpen(true);
    }

    public void setScrollOffset(float f) {
        this.offset = f;
    }

    public void setSlowerDismiss(boolean z) {
        this.slowerDismiss = z;
    }

    public void setTabsView(BottomSheetTabs bottomSheetTabs) {
        this.tabsView = bottomSheetTabs;
    }

    public void stopAnimations() {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.animator = null;
        }
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return drawable == this.closeAllButtonBackground || super.verifyDrawable(drawable);
    }
}
