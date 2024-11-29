package org.telegram.ui.Stories.recorder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.RenderNode;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.common.detector.MathUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotFullscreenButtons$$ExternalSyntheticApiModelOutline2;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.camera.CameraView;
import org.telegram.messenger.video.VideoPlayerHolderBase;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.BlurringShader;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Stories.recorder.CollageLayout;
import org.telegram.ui.Stories.recorder.CollageLayoutView2;

/* loaded from: classes5.dex */
public abstract class CollageLayoutView2 extends FrameLayout implements ItemOptions.ScrimView {
    private final AnimatedFloat[] animatedColumns;
    private final AnimatedFloat animatedReordering;
    private final AnimatedFloat animatedRows;
    private boolean attached;
    private final BlurringShader.BlurManager blurManager;
    public CameraView cameraView;
    private Object cameraViewBlurRenderNode;
    private Runnable cancelGestures;
    private final Path clipPath;
    private final FrameLayout containerView;
    private CollageLayout currentLayout;
    public Part currentPart;
    public float dx;
    public float dy;
    private boolean fastSeek;
    private final LinearGradient gradient;
    private final Matrix gradientMatrix;
    private final int gradientWidth;
    private final Paint highlightPaint;
    private final Path highlightPath;
    public boolean isMuted;
    private long lastPausedPosition;
    public float ldx;
    public float ldy;
    private final float[] lefts;
    public Part longPressedPart;
    private boolean needsBlur;
    public Part nextPart;
    public Runnable onLongPressPart;
    private Runnable onResetState;
    public final ArrayList parts;
    private boolean playing;
    public Part pressedPart;
    private boolean preview;
    private long previewStartTime;
    private final float[] radii;
    private final RectF rect;
    public final ArrayList removingParts;
    public boolean reordering;
    public Part reorderingPart;
    public boolean reorderingTouch;
    private final Runnable resetReordering;
    private final Theme.ResourcesProvider resourcesProvider;
    private boolean restorePositionOnPlaying;
    private final float[] rights;
    private final Runnable syncRunnable;
    private TimelineView timelineView;
    public float tx;
    public float ty;

    public class Part {
        private ValueAnimator animator;
        private StoryEntry content;
        private boolean current;
        private final AnimatedFloat highlightAnimated;
        public final ImageReceiver imageReceiver;
        private int index;
        public CollageLayout.Part part;
        public TextureView textureView;
        public boolean textureViewReady;
        public VideoPlayerHolderBase videoPlayer;
        private volatile long pendingSeek = -1;
        public boolean hasBounds = false;
        public RectF fromBounds = new RectF();
        public RectF bounds = new RectF();
        public float boundsTransition = 1.0f;

        class 3 extends VideoPlayerHolderBase {
            3() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onVideoSizeChanged$0(int i, int i2, int i3) {
                StoryEntry storyEntry = Part.this.content;
                if (storyEntry == null) {
                    return;
                }
                if (storyEntry.width == i && storyEntry.height == i2 && storyEntry.orientation == i3) {
                    return;
                }
                storyEntry.width = i;
                storyEntry.height = i2;
                storyEntry.orientation = i3;
                TextureView textureView = Part.this.textureView;
                if (textureView != null) {
                    textureView.requestLayout();
                }
            }

            @Override // org.telegram.messenger.video.VideoPlayerHolderBase
            public boolean needRepeat() {
                return !CollageLayoutView2.this.preview;
            }

            @Override // org.telegram.messenger.video.VideoPlayerHolderBase
            public void onRenderedFirstFrame() {
                Part part = Part.this;
                part.textureViewReady = true;
                CollageLayoutView2.this.invalidate();
            }

            @Override // org.telegram.messenger.video.VideoPlayerHolderBase
            protected void onVideoSizeChanged(final int i, final int i2, final int i3, float f) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2$Part$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        CollageLayoutView2.Part.3.this.lambda$onVideoSizeChanged$0(i, i2, i3);
                    }
                });
            }
        }

        public Part() {
            this.highlightAnimated = new AnimatedFloat(CollageLayoutView2.this, 0L, 1200L, CubicBezierInterpolator.EASE_OUT);
            this.imageReceiver = new ImageReceiver(CollageLayoutView2.this);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ long access$802(Part part, long j) {
            part.pendingSeek = j;
            return j;
        }

        public boolean hasContent() {
            return this.content != null;
        }

        public void setContent(StoryEntry storyEntry) {
            VideoPlayerHolderBase videoPlayerHolderBase = this.videoPlayer;
            if (videoPlayerHolderBase != null) {
                videoPlayerHolderBase.release(null);
                this.videoPlayer = null;
            }
            TextureView textureView = this.textureView;
            if (textureView != null) {
                CollageLayoutView2.this.removeView(textureView);
                this.textureView = null;
            }
            this.textureViewReady = false;
            this.content = storyEntry;
            StringBuilder sb = new StringBuilder();
            sb.append((int) Math.ceil(AndroidUtilities.displaySize.x / AndroidUtilities.density));
            sb.append("_");
            sb.append((int) Math.ceil(AndroidUtilities.displaySize.y / AndroidUtilities.density));
            sb.append((storyEntry == null || !storyEntry.isVideo) ? "" : "_g");
            sb.append("_exif");
            String sb2 = sb.toString();
            StoryEntry storyEntry2 = this.content;
            if (storyEntry2 == null) {
                this.imageReceiver.clearImage();
            } else if (storyEntry2.isVideo) {
                Bitmap bitmap = storyEntry2.thumbBitmap;
                if (bitmap != null) {
                    this.imageReceiver.setImageBitmap(bitmap);
                } else {
                    String str = storyEntry2.thumbPath;
                    if (str != null) {
                        this.imageReceiver.setImage(str, sb2, null, null, 0L);
                    } else {
                        this.imageReceiver.clearImage();
                    }
                }
                TextureView textureView2 = new TextureView(CollageLayoutView2.this.getContext());
                this.textureView = textureView2;
                CollageLayoutView2.this.addView(textureView2);
                3 r9 = new 3();
                this.videoPlayer = r9;
                r9.allowMultipleInstances(true);
                this.videoPlayer.with(this.textureView);
                this.videoPlayer.preparePlayer(Uri.fromFile(this.content.file), true, 1.0f);
                if (!CollageLayoutView2.this.preview || CollageLayoutView2.this.playing) {
                    this.videoPlayer.play();
                } else {
                    this.videoPlayer.pause();
                }
            } else {
                this.imageReceiver.setImage(storyEntry2.file.getAbsolutePath(), sb2, null, null, 0L);
            }
            CollageLayoutView2.this.invalidate();
        }

        public void setCurrent(boolean z) {
            this.current = z;
        }

        public void setPart(CollageLayout.Part part, boolean z) {
            CollageLayout.Part part2 = this.part;
            if (part != null) {
                this.part = part;
            }
            ValueAnimator valueAnimator = this.animator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.animator = null;
            }
            if (z) {
                if (this.hasBounds) {
                    RectF rectF = this.fromBounds;
                    AndroidUtilities.lerp(rectF, this.bounds, this.boundsTransition, rectF);
                } else {
                    CollageLayoutView2.this.layoutOut(this.fromBounds, part);
                }
                if (part == null) {
                    CollageLayoutView2.this.layoutOut(this.bounds, part2);
                } else {
                    CollageLayoutView2.this.layout(this.bounds, part);
                }
                this.boundsTransition = 0.0f;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.animator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2.Part.1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        Part.this.boundsTransition = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                        CollageLayoutView2.this.invalidate();
                    }
                });
                this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2.Part.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        Part part3 = Part.this;
                        part3.boundsTransition = 1.0f;
                        if (CollageLayoutView2.this.removingParts.contains(part3)) {
                            Part.this.imageReceiver.onDetachedFromWindow();
                            VideoPlayerHolderBase videoPlayerHolderBase = Part.this.videoPlayer;
                            if (videoPlayerHolderBase != null) {
                                videoPlayerHolderBase.release(null);
                                Part.this.videoPlayer = null;
                            }
                            TextureView textureView = Part.this.textureView;
                            if (textureView != null) {
                                AndroidUtilities.removeFromParent(textureView);
                                Part.this.textureView = null;
                            }
                            Part part4 = Part.this;
                            CollageLayoutView2.this.removingParts.remove(part4);
                        }
                        CollageLayoutView2.this.invalidate();
                    }
                });
                this.animator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.animator.setDuration(360L);
                this.animator.start();
            } else {
                CollageLayoutView2.this.layout(this.bounds, part);
                this.boundsTransition = 1.0f;
                if (part == null) {
                    this.imageReceiver.onDetachedFromWindow();
                    VideoPlayerHolderBase videoPlayerHolderBase = this.videoPlayer;
                    if (videoPlayerHolderBase != null) {
                        videoPlayerHolderBase.release(null);
                        this.videoPlayer = null;
                    }
                    TextureView textureView = this.textureView;
                    if (textureView != null) {
                        AndroidUtilities.removeFromParent(textureView);
                        this.textureView = null;
                    }
                    CollageLayoutView2.this.removingParts.remove(this);
                }
            }
            CollageLayoutView2.this.invalidate();
            this.hasBounds = true;
        }
    }

    public CollageLayoutView2(Context context, BlurringShader.BlurManager blurManager, FrameLayout frameLayout, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentLayout = new CollageLayout(".");
        ArrayList arrayList = new ArrayList();
        this.parts = arrayList;
        this.removingParts = new ArrayList();
        Paint paint = new Paint(1);
        this.highlightPaint = paint;
        this.highlightPath = new Path();
        this.radii = new float[8];
        this.resetReordering = new Runnable() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CollageLayoutView2.this.lambda$new$0();
            }
        };
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.animatedRows = new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator);
        this.animatedColumns = new AnimatedFloat[]{new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator), new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator), new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator), new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator), new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator)};
        this.animatedReordering = new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator);
        this.lefts = new float[5];
        this.rights = new float[5];
        this.rect = new RectF();
        this.clipPath = new Path();
        this.playing = true;
        this.restorePositionOnPlaying = true;
        this.syncRunnable = new Runnable() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CollageLayoutView2.this.lambda$new$6();
            }
        };
        this.blurManager = blurManager;
        this.containerView = frameLayout;
        this.resourcesProvider = resourcesProvider;
        setBackgroundColor(-14737633);
        Part part = new Part();
        part.setPart((CollageLayout.Part) this.currentLayout.parts.get(0), false);
        part.setCurrent(true);
        if (this.attached) {
            part.imageReceiver.onAttachedToWindow();
        }
        arrayList.add(part);
        this.currentPart = part;
        this.nextPart = null;
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(-1);
        paint.setStrokeWidth(AndroidUtilities.dp(8.0f));
        int dp = AndroidUtilities.dp(300.0f);
        this.gradientWidth = dp;
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, dp, 0.0f, new int[]{0, -1, -1, 0}, new float[]{0.0f, 0.2f, 0.8f, 1.0f}, Shader.TileMode.CLAMP);
        this.gradient = linearGradient;
        this.gradientMatrix = new Matrix();
        paint.setShader(linearGradient);
        setWillNotDraw(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void drawPart(Canvas canvas, RectF rectF, Part part) {
        boolean z;
        ImageView imageView;
        int width;
        int height;
        int width2;
        int height2;
        View view;
        if (AndroidUtilities.makingGlobalBlurBitmap && part == this.longPressedPart) {
            return;
        }
        float f = 0.0f;
        if (part != this.reorderingPart || this.animatedReordering.get() <= 0.0f) {
            z = false;
        } else {
            canvas.save();
            this.clipPath.rewind();
            RectF rectF2 = AndroidUtilities.rectTmp;
            rectF2.set(rectF);
            rectF2.inset(AndroidUtilities.dp(10.0f) * this.animatedReordering.get(), AndroidUtilities.dp(10.0f) * this.animatedReordering.get());
            float dp = AndroidUtilities.dp(12.0f) * this.animatedReordering.get();
            this.clipPath.addRoundRect(rectF2, dp, dp, Path.Direction.CW);
            canvas.clipPath(this.clipPath);
            z = true;
        }
        if (part != null && part.content != null) {
            view = part.textureView;
            if (view == null || !part.textureViewReady) {
                part.imageReceiver.setImageCoords(rectF.left, rectF.top, rectF.width(), rectF.height());
                if (!part.imageReceiver.draw(canvas)) {
                    drawView(canvas, this.cameraView, rectF, 0.0f);
                }
                if (z) {
                }
            }
            drawView(canvas, view, rectF, f);
            if (z) {
            }
        } else {
            if ((part == null || !part.current) && !AndroidUtilities.makingGlobalBlurBitmap) {
                setCameraNeedsBlur(!this.preview);
                if (this.cameraViewBlurRenderNode == null || Build.VERSION.SDK_INT < 29 || !canvas.isHardwareAccelerated()) {
                    drawView(canvas, this.cameraView, rectF, 0.75f);
                } else {
                    RenderNode m = BotFullscreenButtons$$ExternalSyntheticApiModelOutline2.m(this.cameraViewBlurRenderNode);
                    float width3 = rectF.width();
                    width = m.getWidth();
                    float f2 = width3 / width;
                    float height3 = rectF.height();
                    height = m.getHeight();
                    float max = Math.max(f2, height3 / height);
                    canvas.save();
                    canvas.translate(rectF.centerX(), rectF.centerY());
                    canvas.clipRect((-rectF.width()) / 2.0f, (-rectF.height()) / 2.0f, rectF.width() / 2.0f, rectF.height() / 2.0f);
                    canvas.scale(max, max);
                    width2 = m.getWidth();
                    height2 = m.getHeight();
                    canvas.translate((-width2) / 2.0f, (-height2) / 2.0f);
                    canvas.drawRenderNode(m);
                    canvas.drawColor(1677721600);
                    canvas.restore();
                }
                CameraView cameraView = this.cameraView;
                if (cameraView != null && (imageView = cameraView.blurredStubView) != null && imageView.getVisibility() == 0 && this.cameraView.blurredStubView.getAlpha() > 0.0f) {
                    drawView(canvas, this.cameraView.blurredStubView, rectF, 0.4f);
                }
                if (z) {
                    return;
                }
                canvas.restore();
                return;
            }
            view = this.cameraView;
            if (part == null || !part.current) {
                f = 0.4f;
            }
            drawView(canvas, view, rectF, f);
            if (z) {
            }
        }
    }

    private void drawView(Canvas canvas, View view, RectF rectF, float f) {
        Bitmap bitmap;
        if (view == null) {
            return;
        }
        float max = Math.max(rectF.width() / view.getWidth(), rectF.height() / view.getHeight());
        canvas.save();
        canvas.translate(rectF.centerX(), rectF.centerY());
        canvas.clipRect((-rectF.width()) / 2.0f, (-rectF.height()) / 2.0f, rectF.width() / 2.0f, rectF.height() / 2.0f);
        canvas.scale(max, max);
        canvas.translate((-view.getWidth()) / 2.0f, (-view.getHeight()) / 2.0f);
        if (AndroidUtilities.makingGlobalBlurBitmap) {
            TextureView textureView = view instanceof TextureView ? (TextureView) view : view instanceof CameraView ? ((CameraView) view).getTextureView() : null;
            if (textureView != null && (bitmap = textureView.getBitmap()) != null) {
                canvas.scale(view.getWidth() / bitmap.getWidth(), view.getHeight() / bitmap.getHeight());
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            }
        } else {
            view.draw(canvas);
        }
        if (f > 0.0f) {
            canvas.drawColor(Theme.multAlpha(-16777216, view.getAlpha() * f));
        }
        canvas.restore();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.reordering) {
            this.reordering = false;
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00d5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$new$6() {
        VideoPlayerHolderBase videoPlayerHolderBase;
        boolean z;
        long clamp;
        long position = getPosition();
        Part mainPart = getMainPart();
        long j = mainPart == null ? 0L : mainPart.content.videoOffset + ((long) (mainPart.content.videoLeft * mainPart.content.duration));
        for (int i = 0; i < this.parts.size(); i++) {
            final Part part = (Part) this.parts.get(i);
            if (part.content != null && (videoPlayerHolderBase = part.videoPlayer) != null) {
                long duration = videoPlayerHolderBase.getDuration();
                long clamp2 = Utilities.clamp((position + j) - part.content.videoOffset, duration, 0L);
                if (!this.preview || this.playing) {
                    float f = clamp2;
                    float f2 = duration;
                    if (f > part.content.videoLeft * f2 && f < part.content.videoRight * f2) {
                        z = true;
                        float f3 = duration;
                        clamp = Utilities.clamp(clamp2, (long) (part.content.videoRight * f3), (long) (part.content.videoLeft * f3));
                        if (part.videoPlayer.isPlaying() != z) {
                            VideoPlayerHolderBase videoPlayerHolderBase2 = part.videoPlayer;
                            if (z) {
                                videoPlayerHolderBase2.play();
                            } else {
                                videoPlayerHolderBase2.pause();
                            }
                        }
                        part.videoPlayer.setVolume((!this.isMuted || part.content.muted) ? 0.0f : part.content.videoVolume);
                        if (Math.abs((part.pendingSeek < 0 ? part.pendingSeek : part.videoPlayer.getCurrentPosition()) - clamp) > 450) {
                            if (part.pendingSeek < 0) {
                                part.videoPlayer.seekTo(part.pendingSeek = clamp, this.fastSeek, new Runnable() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2$$ExternalSyntheticLambda8
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        CollageLayoutView2.Part.access$802(CollageLayoutView2.Part.this, -1L);
                                    }
                                });
                            }
                        }
                    }
                }
                z = false;
                float f32 = duration;
                clamp = Utilities.clamp(clamp2, (long) (part.content.videoRight * f32), (long) (part.content.videoLeft * f32));
                if (part.videoPlayer.isPlaying() != z) {
                }
                part.videoPlayer.setVolume((!this.isMuted || part.content.muted) ? 0.0f : part.content.videoVolume);
                if (Math.abs((part.pendingSeek < 0 ? part.pendingSeek : part.videoPlayer.getCurrentPosition()) - clamp) > 450) {
                }
            }
        }
        TimelineView timelineView = this.timelineView;
        if (timelineView != null) {
            timelineView.setProgress(position);
        }
        if (this.preview && this.playing) {
            AndroidUtilities.runOnUIThread(this.syncRunnable, (long) (1000.0f / AndroidUtilities.screenRefreshRate));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLongPress$1(Float f) {
        this.longPressedPart.content.videoVolume = f.floatValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLongPress$2() {
        retake(this.longPressedPart);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLongPress$3() {
        delete(this.longPressedPart);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onLongPress$4() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void layout(RectF rectF, CollageLayout.Part part) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth <= 0 || measuredHeight <= 0) {
            Point point = AndroidUtilities.displaySize;
            int i = point.x;
            measuredHeight = point.y;
            measuredWidth = i;
        }
        int[] iArr = part.layout.columns;
        int i2 = part.y;
        float f = measuredWidth / iArr[i2];
        float f2 = measuredHeight / r2.h;
        rectF.set(part.x * f, i2 * f2, f * (r8 + 1), f2 * (i2 + 1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void layoutOut(RectF rectF, CollageLayout.Part part) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth <= 0 || measuredHeight <= 0) {
            Point point = AndroidUtilities.displaySize;
            int i = point.x;
            measuredHeight = point.y;
            measuredWidth = i;
        }
        layout(rectF, part);
        float f = rectF.left;
        boolean z = f <= 0.0f;
        float f2 = rectF.top;
        boolean z2 = f2 <= 0.0f;
        float f3 = measuredWidth;
        boolean z3 = rectF.right >= f3;
        float f4 = measuredHeight;
        boolean z4 = rectF.bottom >= f4;
        if (z && z3 && !z2 && !z4) {
            rectF.offset(0.0f, f4 - f2);
            return;
        }
        if (z2 && z4 && !z && !z3) {
            rectF.offset(0.0f, f3 - f);
            return;
        }
        if (z3 && !z) {
            rectF.offset(rectF.width(), 0.0f);
        }
        if (!z4 || z2) {
            return;
        }
        rectF.offset(0.0f, rectF.height());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLongPress() {
        if (this.reorderingTouch || this.preview) {
            return;
        }
        Part part = this.pressedPart;
        this.longPressedPart = part;
        if (part == null || part.content == null) {
            return;
        }
        Runnable runnable = this.cancelGestures;
        if (runnable != null) {
            runnable.run();
        }
        FrameLayout frameLayout = new FrameLayout(getContext());
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.menu_lightbulb);
        imageView.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        frameLayout.addView(imageView, LayoutHelper.createFrame(24, 24.0f, 19, 12.0f, 12.0f, 12.0f, 12.0f));
        TextView textView = new TextView(getContext());
        textView.setText(LocaleController.getString(R.string.StoryCollageMenuHint));
        textView.setTextSize(1, 13.0f);
        textView.setTextColor(-1);
        frameLayout.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 23, 47.0f, 8.0f, 24.0f, 8.0f));
        ItemOptions makeOptions = ItemOptions.makeOptions(this.containerView, this.resourcesProvider, this);
        if (this.longPressedPart.content.isVideo) {
            SliderView onValueChange = new SliderView(getContext(), 0).setMinMax(0.0f, 1.5f).setValue(this.longPressedPart.content.videoVolume).setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2$$ExternalSyntheticLambda4
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    CollageLayoutView2.this.lambda$onLongPress$1((Float) obj);
                }
            });
            onValueChange.fixWidth = AndroidUtilities.dp(220.0f);
            makeOptions.addView(onValueChange).addSpaceGap();
        }
        makeOptions.setFixedWidth(NotificationCenter.updateAllMessages).add(R.drawable.menu_camera_retake, LocaleController.getString(R.string.StoreCollageRetake), new Runnable() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                CollageLayoutView2.this.lambda$onLongPress$2();
            }
        }).add(R.drawable.msg_delete, (CharSequence) LocaleController.getString(R.string.Delete), true, new Runnable() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                CollageLayoutView2.this.lambda$onLongPress$3();
            }
        }).addSpaceGap().addView(frameLayout, LayoutHelper.createLinear(NotificationCenter.updateAllMessages, -2)).setOnDismiss(new Runnable() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                CollageLayoutView2.lambda$onLongPress$4();
            }
        }).setGravity(1).allowCenter(true).setBlur(true).setRoundRadius(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(10.0f)).show();
        performHapticFeedback(0, 1);
    }

    public boolean cancelTouch() {
        if (this.pressedPart == null) {
            return false;
        }
        this.pressedPart = null;
        this.reorderingTouch = false;
        invalidate();
        Runnable runnable = this.onLongPressPart;
        if (runnable == null) {
            return true;
        }
        AndroidUtilities.cancelRunOnUIThread(runnable);
        this.onLongPressPart = null;
        return true;
    }

    public void clear(boolean z) {
        Iterator it = this.parts.iterator();
        while (it.hasNext()) {
            ((Part) it.next()).setContent(null);
        }
        updatePartsState();
    }

    public void delete(Part part) {
        if (part != null && this.parts.indexOf(part) >= 0) {
            CollageLayout collageLayout = this.currentLayout;
            CollageLayout delete = collageLayout.delete(collageLayout.parts.indexOf(part.part));
            if (delete.parts.size() <= 1) {
                clear(true);
                invalidate();
            }
            setLayout(delete, true);
            this.reordering = true;
            updatePartsState();
            invalidate();
            Runnable runnable = this.onResetState;
            if (runnable != null) {
                runnable.run();
            }
            onLayoutUpdate(delete);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x0312  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x02a3  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x02af  */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchDraw(Canvas canvas) {
        double d;
        float f;
        int i;
        BlurringShader.BlurManager blurManager;
        float f2;
        Part part;
        boolean z;
        super.dispatchDraw(canvas);
        if (!hasLayout() && !this.reordering && !this.reorderingTouch && this.animatedRows.get() == this.currentLayout.h && this.animatedColumns[0].get() == this.currentLayout.columns[0]) {
            setCameraNeedsBlur(false);
            return;
        }
        if (this.preview) {
            setCameraNeedsBlur(false);
        }
        canvas.drawColor(-14737633);
        float f3 = this.animatedReordering.set(this.reorderingTouch);
        float f4 = this.animatedRows.set(this.currentLayout.h);
        int i2 = 0;
        while (true) {
            d = f4;
            if (i2 >= Math.ceil(d)) {
                break;
            }
            this.lefts[i2] = getMeasuredWidth();
            this.rights[i2] = 0.0f;
            i2++;
        }
        int i3 = this.currentLayout.h;
        while (true) {
            AnimatedFloat[] animatedFloatArr = this.animatedColumns;
            if (i3 >= animatedFloatArr.length) {
                break;
            }
            animatedFloatArr[i3].set(1.0f);
            i3++;
        }
        float f5 = 0.0f;
        boolean z2 = false;
        for (int i4 = 0; i4 < this.parts.size(); i4++) {
            Part part2 = (Part) this.parts.get(i4);
            CollageLayout.Part part3 = part2.part;
            float f6 = this.animatedColumns[part3.y].set(part3.layout.columns[r3]);
            if (this.reordering || this.reorderingTouch) {
                z = z2;
                AndroidUtilities.lerp(part2.fromBounds, part2.bounds, part2.boundsTransition, this.rect);
            } else {
                z = z2;
                this.rect.set((getMeasuredWidth() / f6) * part3.x, (getMeasuredHeight() / f4) * part3.y, (getMeasuredWidth() / f6) * (part3.x + 1), (getMeasuredHeight() / f4) * (part3.y + 1));
            }
            float[] fArr = this.lefts;
            int i5 = part3.y;
            fArr[i5] = Math.min(fArr[i5], this.rect.left);
            float[] fArr2 = this.rights;
            int i6 = part3.y;
            fArr2[i6] = Math.max(fArr2[i6], this.rect.right);
            f5 = Math.max(f5, this.rect.bottom);
            if (f3 <= 0.0f || part2 != this.reorderingPart) {
                z2 = (!this.preview || part2.videoPlayer == null) ? z : true;
                drawPart(canvas, this.rect, part2);
            } else {
                z2 = z;
            }
        }
        for (int i7 = 0; i7 < this.removingParts.size(); i7++) {
            Part part4 = (Part) this.removingParts.get(i7);
            CollageLayout.Part part5 = part4.part;
            AnimatedFloat[] animatedFloatArr2 = this.animatedColumns;
            int i8 = part5.y;
            float f7 = animatedFloatArr2[i8].set(i8 >= this.currentLayout.columns.length ? 1.0f : r13[i8]);
            boolean z3 = z2;
            this.rect.set((getMeasuredWidth() / f7) * part5.x, (getMeasuredHeight() / f4) * part5.y, (getMeasuredWidth() / f7) * (part5.x + 1), (getMeasuredHeight() / f4) * (part5.y + 1));
            float[] fArr3 = this.lefts;
            int i9 = part5.y;
            fArr3[i9] = Math.min(fArr3[i9], this.rect.left);
            float[] fArr4 = this.rights;
            int i10 = part5.y;
            fArr4[i10] = Math.max(fArr4[i10], this.rect.right);
            f5 = Math.max(f5, this.rect.bottom);
            z2 = (!this.preview || part4.videoPlayer == null) ? z3 : true;
            drawPart(canvas, this.rect, part4);
        }
        boolean z4 = z2;
        if (!this.reorderingTouch) {
            for (int i11 = 0; i11 < Math.ceil(d); i11++) {
                if (this.lefts[i11] >= 0.0f) {
                    this.rect.set(0.0f, (getMeasuredHeight() / f4) * i11, this.lefts[i11], (getMeasuredHeight() / f4) * (i11 + 1));
                    drawPart(canvas, this.rect, null);
                }
                if (this.rights[i11] < getMeasuredWidth()) {
                    this.rect.set(this.rights[i11], (getMeasuredHeight() / f4) * i11, getMeasuredWidth(), (getMeasuredHeight() / f4) * (i11 + 1));
                    drawPart(canvas, this.rect, null);
                }
            }
            if (f5 < getMeasuredHeight()) {
                f = 0.0f;
                this.rect.set(0.0f, f5, getMeasuredWidth(), getMeasuredHeight());
                drawPart(canvas, this.rect, null);
                if (f3 > f && (part = this.reorderingPart) != null) {
                    CollageLayout.Part part6 = part.part;
                    float f8 = this.animatedColumns[part6.y].set(this.currentLayout.columns[r7]);
                    if (this.reorderingTouch) {
                        this.rect.set((getMeasuredWidth() / f8) * part6.x, (getMeasuredHeight() / f4) * part6.y, (getMeasuredWidth() / f8) * (part6.x + 1), (getMeasuredHeight() / f4) * (part6.y + 1));
                    } else {
                        AndroidUtilities.lerp(part.fromBounds, part.bounds, part.boundsTransition, this.rect);
                    }
                    canvas.save();
                    canvas.translate(AndroidUtilities.lerp(this.ldx, this.dx, part.boundsTransition) * f3, AndroidUtilities.lerp(this.ldy, this.dy, part.boundsTransition) * f3);
                    drawPart(canvas, this.rect, part);
                    canvas.restore();
                }
                for (i = 0; i < this.parts.size(); i++) {
                    Part part7 = (Part) this.parts.get(i);
                    CollageLayout.Part part8 = part7.part;
                    float f9 = part7.highlightAnimated.set(0.0f);
                    if (f9 > 0.0f) {
                        float f10 = this.animatedColumns[part8.y].set(part8.layout.columns[r8]);
                        if (this.reordering || this.reorderingTouch) {
                            AndroidUtilities.lerp(part7.fromBounds, part7.bounds, part7.boundsTransition, this.rect);
                        } else {
                            this.rect.set((getMeasuredWidth() / f10) * part8.x, (getMeasuredHeight() / f4) * part8.y, (getMeasuredWidth() / f10) * (part8.x + 1), (getMeasuredHeight() / f4) * (part8.y + 1));
                        }
                        RectF rectF = AndroidUtilities.rectTmp;
                        rectF.set(this.rect);
                        rectF.inset(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f));
                        this.gradientMatrix.reset();
                        Matrix matrix = this.gradientMatrix;
                        float f11 = this.rect.left;
                        int i12 = this.gradientWidth;
                        int i13 = i12 * i12;
                        matrix.postTranslate(f11 + AndroidUtilities.lerp(((float) Math.sqrt(i13 + i13)) * (-1.4f), (float) Math.sqrt((this.rect.width() * this.rect.width()) + (this.rect.height() * this.rect.height())), 1.0f - f9), 0.0f);
                        this.gradientMatrix.postRotate(-25.0f);
                        this.gradient.setLocalMatrix(this.gradientMatrix);
                        this.highlightPaint.setAlpha(NotificationCenter.newLocationAvailable);
                        this.highlightPath.rewind();
                        float[] fArr5 = this.radii;
                        CollageLayout.Part part9 = part7.part;
                        float dp = (part9.x == 0 && part9.y == 0) ? AndroidUtilities.dp(8.0f) : 0.0f;
                        fArr5[1] = dp;
                        fArr5[0] = dp;
                        float[] fArr6 = this.radii;
                        CollageLayout.Part part10 = part7.part;
                        float dp2 = (part10.x == part10.layout.w - 1 && part10.y == 0) ? AndroidUtilities.dp(8.0f) : 0.0f;
                        fArr6[2] = dp2;
                        fArr6[1] = dp2;
                        float[] fArr7 = this.radii;
                        CollageLayout.Part part11 = part7.part;
                        int i14 = part11.x;
                        CollageLayout collageLayout = part11.layout;
                        float dp3 = (i14 == collageLayout.w - 1 && part11.y == collageLayout.h - 1) ? AndroidUtilities.dp(8.0f) : 0.0f;
                        fArr7[4] = dp3;
                        fArr7[3] = dp3;
                        float[] fArr8 = this.radii;
                        CollageLayout.Part part12 = part7.part;
                        if (part12.x == 0 && part12.y == part12.layout.h - 1) {
                            f2 = AndroidUtilities.dp(8.0f);
                            fArr8[6] = f2;
                            fArr8[5] = f2;
                            this.highlightPath.addRoundRect(rectF, this.radii, Path.Direction.CW);
                            canvas.drawPath(this.highlightPath, this.highlightPaint);
                        }
                        f2 = 0.0f;
                        fArr8[6] = f2;
                        fArr8[5] = f2;
                        this.highlightPath.addRoundRect(rectF, this.radii, Path.Direction.CW);
                        canvas.drawPath(this.highlightPath, this.highlightPaint);
                    }
                }
                if (z4 || (blurManager = this.blurManager) == null) {
                }
                blurManager.invalidate();
                return;
            }
        }
        f = 0.0f;
        if (f3 > f) {
            CollageLayout.Part part62 = part.part;
            float f82 = this.animatedColumns[part62.y].set(this.currentLayout.columns[r7]);
            if (this.reorderingTouch) {
            }
            canvas.save();
            canvas.translate(AndroidUtilities.lerp(this.ldx, this.dx, part.boundsTransition) * f3, AndroidUtilities.lerp(this.ldy, this.dy, part.boundsTransition) * f3);
            drawPart(canvas, this.rect, part);
            canvas.restore();
        }
        while (i < this.parts.size()) {
        }
        if (z4) {
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        Runnable runnable;
        if (!hasLayout() || this.preview) {
            return super.dispatchTouchEvent(motionEvent);
        }
        Part partAt = getPartAt(motionEvent.getX(), motionEvent.getY());
        if (motionEvent.getAction() == 0) {
            this.tx = motionEvent.getX();
            this.ty = motionEvent.getY();
            this.reorderingTouch = false;
            this.dx = 0.0f;
            this.ldx = 0.0f;
            this.dy = 0.0f;
            this.ldy = 0.0f;
            this.pressedPart = partAt;
            if (partAt != null) {
                Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        CollageLayoutView2.this.onLongPress();
                    }
                };
                this.onLongPressPart = runnable2;
                AndroidUtilities.runOnUIThread(runnable2, ViewConfiguration.getLongPressTimeout());
            }
        } else if (motionEvent.getAction() == 2) {
            if (MathUtils.distance(motionEvent.getX(), motionEvent.getY(), this.tx, this.ty) > AndroidUtilities.touchSlop * 1.2f && (runnable = this.onLongPressPart) != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.onLongPressPart = null;
            }
            if (!this.reorderingTouch && getFilledProgress() >= 1.0f && this.pressedPart != null && partAt != null && MathUtils.distance(motionEvent.getX(), motionEvent.getY(), this.tx, this.ty) > AndroidUtilities.touchSlop * 1.2f) {
                this.reorderingTouch = true;
                this.reorderingPart = this.pressedPart;
                this.dx = 0.0f;
                this.ldx = 0.0f;
                this.dy = 0.0f;
                this.ldy = 0.0f;
                invalidate();
                Runnable runnable3 = this.onLongPressPart;
                if (runnable3 != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable3);
                    this.onLongPressPart = null;
                }
            } else if (this.reorderingTouch && this.reorderingPart != null) {
                int partIndexAt = getPartIndexAt(motionEvent.getX(), motionEvent.getY());
                int indexOf = this.parts.indexOf(this.reorderingPart);
                if (partIndexAt >= 0 && indexOf >= 0 && partIndexAt != indexOf) {
                    swap(indexOf, partIndexAt);
                    float f = this.currentLayout.h;
                    float f2 = this.animatedColumns[this.reorderingPart.part.y].get();
                    this.rect.set((getMeasuredWidth() / f2) * r1.x, (getMeasuredHeight() / f) * r1.y, (getMeasuredWidth() / f2) * (r1.x + 1), (getMeasuredHeight() / f) * (r1.y + 1));
                    this.ldx = this.dx;
                    this.ldy = this.dy;
                    this.tx = this.rect.centerX();
                    this.ty = this.rect.centerY();
                }
                this.dx = motionEvent.getX() - this.tx;
                this.dy = motionEvent.getY() - this.ty;
                invalidate();
            } else if (this.pressedPart != partAt) {
                this.pressedPart = null;
                Runnable runnable4 = this.onLongPressPart;
                if (runnable4 != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable4);
                    this.onLongPressPart = null;
                }
                return true;
            }
        } else if (motionEvent.getAction() == 1) {
            if (this.pressedPart != null) {
                this.pressedPart = null;
                this.reorderingTouch = false;
                invalidate();
                Runnable runnable5 = this.onLongPressPart;
                if (runnable5 != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable5);
                    this.onLongPressPart = null;
                }
                return true;
            }
        } else if (motionEvent.getAction() == 3 && cancelTouch()) {
            return true;
        }
        return this.pressedPart != null || super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (view == this.cameraView && AndroidUtilities.makingGlobalBlurBitmap) {
            return false;
        }
        return super.drawChild(canvas, view, j);
    }

    @Override // org.telegram.ui.Components.ItemOptions.ScrimView
    public void drawScrim(Canvas canvas, float f) {
        Part part = this.longPressedPart;
        if (part != null) {
            CollageLayout.Part part2 = part.part;
            float f2 = part2.layout.h;
            float f3 = this.animatedColumns[part2.y].set(r0.columns[r3]);
            this.rect.set((getMeasuredWidth() / f3) * part2.x, (getMeasuredHeight() / f2) * part2.y, (getMeasuredWidth() / f3) * (part2.x + 1), (getMeasuredHeight() / f2) * (part2.y + 1));
            drawPart(canvas, this.rect, this.longPressedPart);
        }
    }

    public void forceNotRestorePosition() {
    }

    @Override // org.telegram.ui.Components.ItemOptions.ScrimView
    public void getBounds(RectF rectF) {
        Part part = this.longPressedPart;
        if (part == null) {
            rectF.set(0.0f, 0.0f, getWidth(), getHeight());
            return;
        }
        CollageLayout.Part part2 = part.part;
        float f = part2.layout.h;
        float f2 = this.animatedColumns[part2.y].set(r1.columns[r4]);
        rectF.set((getMeasuredWidth() / f2) * part2.x, (getMeasuredHeight() / f) * part2.y, (getMeasuredWidth() / f2) * (part2.x + 1), (getMeasuredHeight() / f) * (part2.y + 1));
    }

    public ArrayList<StoryEntry> getContent() {
        ArrayList<StoryEntry> arrayList = new ArrayList<>();
        Iterator it = this.parts.iterator();
        while (it.hasNext()) {
            Part part = (Part) it.next();
            if (part.hasContent()) {
                arrayList.add(part.content);
            }
        }
        return arrayList;
    }

    public Part getCurrent() {
        return this.currentPart;
    }

    public long getDuration() {
        Part mainPart;
        if (!this.preview || (mainPart = getMainPart()) == null || mainPart.content == null) {
            return 1L;
        }
        return Math.max(Math.min((long) (mainPart.content.duration * (mainPart.content.videoRight - mainPart.content.videoLeft)), 59500L), 1L);
    }

    public float getFilledProgress() {
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < this.parts.size(); i3++) {
            if (((Part) this.parts.get(i3)).hasContent()) {
                i++;
            }
            i2++;
        }
        return i / i2;
    }

    public CollageLayout getLayout() {
        return this.currentLayout;
    }

    public Part getMainPart() {
        Part part = null;
        if (!this.preview) {
            return null;
        }
        Iterator it = this.parts.iterator();
        long j = 0;
        while (it.hasNext()) {
            Part part2 = (Part) it.next();
            if (part2.content != null && part2.content.isVideo) {
                long j2 = part2.content.duration;
                VideoPlayerHolderBase videoPlayerHolderBase = part2.videoPlayer;
                if (videoPlayerHolderBase != null && videoPlayerHolderBase.getDuration() > 0) {
                    j2 = part2.videoPlayer.getDuration();
                }
                if (j2 > j) {
                    part = part2;
                    j = j2;
                }
            }
        }
        return part;
    }

    public Part getNext() {
        return this.nextPart;
    }

    public ArrayList<Integer> getOrder() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < this.parts.size(); i++) {
            arrayList.add(Integer.valueOf(((Part) this.parts.get(i)).index));
        }
        return arrayList;
    }

    public Part getPartAt(float f, float f2) {
        float f3 = this.animatedRows.get();
        for (int i = 0; i < this.parts.size(); i++) {
            Part part = (Part) this.parts.get(i);
            float f4 = this.animatedColumns[part.part.y].get();
            this.rect.set((getMeasuredWidth() / f4) * r3.x, (getMeasuredHeight() / f3) * r3.y, (getMeasuredWidth() / f4) * (r3.x + 1), (getMeasuredHeight() / f3) * (r3.y + 1));
            if (this.rect.contains(f, f2)) {
                return part;
            }
        }
        return null;
    }

    public int getPartIndexAt(float f, float f2) {
        float f3 = this.animatedRows.get();
        for (int i = 0; i < this.parts.size(); i++) {
            float f4 = this.animatedColumns[((Part) this.parts.get(i)).part.y].get();
            this.rect.set((getMeasuredWidth() / f4) * r2.x, (getMeasuredHeight() / f3) * r2.y, (getMeasuredWidth() / f4) * (r2.x + 1), (getMeasuredHeight() / f3) * (r2.y + 1));
            if (this.rect.contains(f, f2)) {
                return i;
            }
        }
        return -1;
    }

    public long getPosition() {
        if (!this.preview) {
            return 0L;
        }
        if (!this.playing) {
            return this.lastPausedPosition;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - this.previewStartTime;
        if (j > getDuration()) {
            this.previewStartTime = currentTimeMillis - (j % getDuration());
        }
        return j;
    }

    public boolean hasContent() {
        Iterator it = this.parts.iterator();
        while (it.hasNext()) {
            if (((Part) it.next()).hasContent()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasLayout() {
        return this.currentLayout.parts.size() > 1;
    }

    public boolean hasVideo() {
        Iterator it = this.parts.iterator();
        while (it.hasNext()) {
            Part part = (Part) it.next();
            if (part.content != null && part.content.isVideo) {
                return true;
            }
        }
        return false;
    }

    public void highlight(int i) {
        Iterator it = this.parts.iterator();
        while (it.hasNext()) {
            Part part = (Part) it.next();
            if (part.index == i) {
                part.highlightAnimated.set(1.0f, true);
                invalidate();
                return;
            }
        }
    }

    public boolean isPlaying() {
        return this.playing;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < this.parts.size(); i++) {
            ((Part) this.parts.get(i)).imageReceiver.onAttachedToWindow();
        }
        this.attached = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (int i = 0; i < this.parts.size(); i++) {
            ((Part) this.parts.get(i)).imageReceiver.onDetachedFromWindow();
        }
        this.attached = false;
        AndroidUtilities.cancelRunOnUIThread(this.syncRunnable);
    }

    protected abstract void onLayoutUpdate(CollageLayout collageLayout);

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int makeMeasureSpec;
        int makeMeasureSpec2;
        Part part;
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        setMeasuredDimension(size, size2);
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            if (childAt != this.cameraView) {
                int i4 = 0;
                while (true) {
                    if (i4 >= this.parts.size()) {
                        part = null;
                        break;
                    } else {
                        if (childAt == ((Part) this.parts.get(i4)).textureView) {
                            part = (Part) this.parts.get(i4);
                            break;
                        }
                        i4++;
                    }
                }
                if (part != null && part.content != null && part.content.width > 0 && part.content.height > 0) {
                    int i5 = part.content.width;
                    int i6 = part.content.height;
                    if (part.content.orientation % 90 != 1) {
                        i6 = i5;
                        i5 = i6;
                    }
                    float f = i6;
                    float f2 = i5;
                    float min = Math.min(1.0f, Math.max(f / size, f2 / size2));
                    makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) (f * min), 1073741824);
                    makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec((int) (f2 * min), 1073741824);
                    childAt.measure(makeMeasureSpec, makeMeasureSpec2);
                }
            }
            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
            makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(size2, 1073741824);
            childAt.measure(makeMeasureSpec, makeMeasureSpec2);
        }
    }

    public boolean push(StoryEntry storyEntry) {
        Part part = this.currentPart;
        if (part != null) {
            part.setContent(storyEntry);
        }
        updatePartsState();
        requestLayout();
        return this.currentPart == null;
    }

    public void retake(Part part) {
        if (part == null) {
            return;
        }
        part.setContent(null);
        updatePartsState();
        invalidate();
        Runnable runnable = this.onResetState;
        if (runnable != null) {
            runnable.run();
        }
    }

    public void seekTo(long j, boolean z) {
        if (this.preview) {
            long clamp = Utilities.clamp(j, getDuration(), 0L);
            if (!this.playing) {
                this.lastPausedPosition = clamp;
            }
            this.previewStartTime = System.currentTimeMillis() - clamp;
            this.fastSeek = z;
            AndroidUtilities.cancelRunOnUIThread(this.syncRunnable);
            this.syncRunnable.run();
        }
    }

    public void set(StoryEntry storyEntry, boolean z) {
        if (storyEntry == null || storyEntry.collageContent == null) {
            clear(true);
            return;
        }
        setLayout(storyEntry.collage, z);
        for (int i = 0; i < this.parts.size(); i++) {
            ((Part) this.parts.get(i)).setContent((StoryEntry) storyEntry.collageContent.get(i));
        }
    }

    public void setCameraNeedsBlur(boolean z) {
        if (this.needsBlur == z) {
            return;
        }
        this.needsBlur = z;
        updateCameraNeedsBlur();
    }

    public void setCameraView(CameraView cameraView) {
        CameraView cameraView2 = this.cameraView;
        if (cameraView2 != cameraView && cameraView2 != null) {
            cameraView2.unlistenDraw(new Runnable() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    CollageLayoutView2.this.invalidate();
                }
            });
            AndroidUtilities.removeFromParent(this.cameraView);
            this.cameraView = null;
            updateCameraNeedsBlur();
        }
        this.cameraView = cameraView;
        if (cameraView != null) {
            addView(cameraView, LayoutHelper.createFrame(-1, -1, 119));
        }
        CameraView cameraView3 = this.cameraView;
        if (cameraView3 != null) {
            cameraView3.unlistenDraw(new Runnable() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    CollageLayoutView2.this.invalidate();
                }
            });
        }
        this.cameraView = cameraView;
        if (cameraView != null) {
            cameraView.listenDraw(new Runnable() { // from class: org.telegram.ui.Stories.recorder.CollageLayoutView2$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    CollageLayoutView2.this.invalidate();
                }
            });
        }
        updateCameraNeedsBlur();
        invalidate();
    }

    public void setCancelGestures(Runnable runnable) {
        this.cancelGestures = runnable;
    }

    public void setLayout(CollageLayout collageLayout, boolean z) {
        if (collageLayout == null) {
            collageLayout = new CollageLayout(".");
        }
        this.currentLayout = collageLayout;
        AndroidUtilities.cancelRunOnUIThread(this.resetReordering);
        int i = 0;
        while (i < Math.max(collageLayout.parts.size(), this.parts.size())) {
            CollageLayout.Part part = i < collageLayout.parts.size() ? (CollageLayout.Part) collageLayout.parts.get(i) : null;
            Part part2 = i < this.parts.size() ? (Part) this.parts.get(i) : null;
            if (part2 == null && part != null) {
                Part part3 = new Part();
                if (this.attached) {
                    part3.imageReceiver.onAttachedToWindow();
                }
                part3.setPart(part, z);
                this.parts.add(part3);
            } else if (part != null) {
                part2.setPart(part, z);
            } else if (part2 != null) {
                this.removingParts.add(part2);
                this.parts.remove(part2);
                part2.setPart(null, z);
                i--;
            }
            i++;
        }
        updatePartsState();
        invalidate();
        if (z) {
            AndroidUtilities.runOnUIThread(this.resetReordering, 360L);
        }
    }

    public void setMuted(boolean z) {
        if (this.isMuted == z) {
            return;
        }
        this.isMuted = z;
    }

    public void setPlaying(boolean z) {
        boolean z2 = this.restorePositionOnPlaying;
        this.restorePositionOnPlaying = true;
        if (this.playing == z) {
            return;
        }
        this.playing = z;
        if (!z) {
            this.lastPausedPosition = getPosition();
        } else if (z2) {
            seekTo(this.lastPausedPosition, false);
        } else {
            this.fastSeek = false;
        }
        AndroidUtilities.cancelRunOnUIThread(this.syncRunnable);
        this.syncRunnable.run();
    }

    public void setPreview(boolean z) {
        if (this.preview == z) {
            return;
        }
        this.preview = z;
        if (z) {
            BlurringShader.BlurManager blurManager = this.blurManager;
            if (blurManager != null) {
                blurManager.invalidate();
            }
            for (int i = 0; i < this.parts.size(); i++) {
                ((Part) this.parts.get(i)).index = i;
            }
        }
        this.fastSeek = false;
        this.lastPausedPosition = 0L;
        Iterator it = this.parts.iterator();
        while (it.hasNext()) {
            Part part = (Part) it.next();
            VideoPlayerHolderBase videoPlayerHolderBase = part.videoPlayer;
            if (videoPlayerHolderBase != null) {
                videoPlayerHolderBase.setAudioEnabled(z, true);
                if (!z || this.playing) {
                    part.videoPlayer.play();
                } else {
                    part.videoPlayer.pause();
                }
            }
        }
        AndroidUtilities.cancelRunOnUIThread(this.syncRunnable);
        if (z) {
            this.previewStartTime = System.currentTimeMillis();
            AndroidUtilities.runOnUIThread(this.syncRunnable, (long) (1000.0f / AndroidUtilities.screenRefreshRate));
        }
    }

    public void setResetState(Runnable runnable) {
        this.onResetState = runnable;
    }

    public void setTimelineView(TimelineView timelineView) {
        this.timelineView = timelineView;
    }

    public void swap(int i, int i2) {
        Collections.swap(this.parts, i, i2);
        setLayout(this.currentLayout, true);
        this.reordering = true;
        invalidate();
    }

    public void updateCameraNeedsBlur() {
        CameraView cameraView = this.cameraView;
        boolean z = cameraView != null && this.needsBlur;
        if (z == (this.cameraViewBlurRenderNode != null)) {
            return;
        }
        this.cameraViewBlurRenderNode = z ? cameraView.getBlurRenderNode() : null;
    }

    public void updatePartsState() {
        this.currentPart = null;
        this.nextPart = null;
        int i = 0;
        while (true) {
            if (i >= this.parts.size()) {
                break;
            }
            Part part = (Part) this.parts.get(i);
            if (!part.hasContent()) {
                if (this.currentPart != null) {
                    this.nextPart = part;
                    break;
                }
                this.currentPart = part;
            }
            i++;
        }
        for (int i2 = 0; i2 < this.parts.size(); i2++) {
            Part part2 = (Part) this.parts.get(i2);
            part2.setCurrent(part2 == this.currentPart);
        }
    }
}
