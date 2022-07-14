package org.webrtc;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.telegram.messenger.FileLog;
import org.webrtc.EglBase;
import org.webrtc.GlGenericDrawer;
import org.webrtc.GlUtil;
import org.webrtc.RendererCommon;
import org.webrtc.VideoSink;
/* loaded from: classes5.dex */
public class EglRenderer implements VideoSink {
    private static final long LOG_INTERVAL_SEC = 4;
    private static final String TAG = "EglRenderer";
    private final GlTextureFrameBuffer bitmapTextureFramebuffer;
    private final Matrix drawMatrix;
    private RendererCommon.GlDrawer drawer;
    private EglBase eglBase;
    private final EglSurfaceCreation eglSurfaceBackgroundCreationRunnable;
    private final EglSurfaceCreation eglSurfaceCreationRunnable;
    private volatile ErrorCallback errorCallback;
    public boolean firstFrameRendered;
    private final Object fpsReductionLock;
    private final VideoFrameDrawer frameDrawer;
    private final ArrayList<FrameListenerAndParams> frameListeners;
    private final Object frameLock;
    private int framesDropped;
    private int framesReceived;
    private int framesRendered;
    private final Object handlerLock;
    private float layoutAspectRatio;
    private final Object layoutLock;
    private long minRenderPeriodNs;
    private boolean mirrorHorizontally;
    private boolean mirrorVertically;
    protected final String name;
    private long nextFrameTimeNs;
    private VideoFrame pendingFrame;
    private long renderSwapBufferTimeNs;
    private Handler renderThreadHandler;
    private long renderTimeNs;
    private int rotation;
    private boolean usePresentationTimeStamp;

    /* loaded from: classes5.dex */
    public interface ErrorCallback {
        void onGlOutOfMemory();
    }

    /* loaded from: classes5.dex */
    public interface FrameListener {
        void onFrame(Bitmap bitmap);
    }

    @Override // org.webrtc.VideoSink
    public /* synthetic */ void setParentSink(VideoSink videoSink) {
        VideoSink.CC.$default$setParentSink(this, videoSink);
    }

    /* loaded from: classes5.dex */
    public static class FrameListenerAndParams {
        public final boolean applyFpsReduction;
        public final RendererCommon.GlDrawer drawer;
        public final FrameListener listener;
        public final float scale;

        public FrameListenerAndParams(FrameListener listener, float scale, RendererCommon.GlDrawer drawer, boolean applyFpsReduction) {
            this.listener = listener;
            this.scale = scale;
            this.drawer = drawer;
            this.applyFpsReduction = applyFpsReduction;
        }
    }

    /* loaded from: classes5.dex */
    public class EglSurfaceCreation implements Runnable {
        private final boolean background;
        private Object surface;

        public EglSurfaceCreation(boolean background) {
            EglRenderer.this = r1;
            this.background = background;
        }

        public synchronized void setSurface(Object surface) {
            this.surface = surface;
        }

        /* JADX WARN: Code restructure failed: missing block: B:13:0x0028, code lost:
            if (org.webrtc.EglRenderer.this.eglBase.hasSurface() == false) goto L14;
         */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public synchronized void run() {
            if (this.surface != null && EglRenderer.this.eglBase != null) {
                if (this.background) {
                    if (!EglRenderer.this.eglBase.hasBackgroundSurface()) {
                        Object obj = this.surface;
                        if (obj instanceof Surface) {
                            EglRenderer.this.eglBase.createSurface((Surface) this.surface);
                        } else if (obj instanceof SurfaceTexture) {
                            if (this.background) {
                                EglRenderer.this.eglBase.createBackgroundSurface((SurfaceTexture) this.surface);
                            } else {
                                EglRenderer.this.eglBase.createSurface((SurfaceTexture) this.surface);
                            }
                        } else {
                            throw new IllegalStateException("Invalid surface: " + this.surface);
                        }
                        if (!this.background) {
                            EglRenderer.this.eglBase.makeCurrent();
                            GLES20.glPixelStorei(3317, 1);
                        } else {
                            EglRenderer.this.eglBase.makeBackgroundCurrent();
                            GLES20.glPixelStorei(3317, 1);
                            if (EglRenderer.this.eglBase.hasSurface()) {
                                EglRenderer.this.eglBase.makeCurrent();
                            }
                        }
                    }
                }
            }
        }
    }

    /* loaded from: classes5.dex */
    public static class HandlerWithExceptionCallback extends Handler {
        private final Runnable exceptionCallback;

        public HandlerWithExceptionCallback(Looper looper, Runnable exceptionCallback) {
            super(looper);
            this.exceptionCallback = exceptionCallback;
        }

        @Override // android.os.Handler
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
                Logging.e(EglRenderer.TAG, "Exception on EglRenderer thread", e);
                this.exceptionCallback.run();
                throw e;
            }
        }
    }

    public EglRenderer(String name) {
        this(name, new VideoFrameDrawer());
    }

    public EglRenderer(String name, VideoFrameDrawer videoFrameDrawer) {
        this.handlerLock = new Object();
        this.frameListeners = new ArrayList<>();
        this.fpsReductionLock = new Object();
        this.drawMatrix = new Matrix();
        this.frameLock = new Object();
        this.layoutLock = new Object();
        this.bitmapTextureFramebuffer = new GlTextureFrameBuffer(6408);
        this.eglSurfaceCreationRunnable = new EglSurfaceCreation(false);
        this.eglSurfaceBackgroundCreationRunnable = new EglSurfaceCreation(true);
        this.name = name;
        this.frameDrawer = videoFrameDrawer;
    }

    public void init(final EglBase.Context sharedContext, final int[] configAttributes, RendererCommon.GlDrawer drawer, boolean usePresentationTimeStamp) {
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler != null) {
                throw new IllegalStateException(this.name + "Already initialized");
            }
            logD("Initializing EglRenderer");
            this.drawer = drawer;
            this.usePresentationTimeStamp = usePresentationTimeStamp;
            this.firstFrameRendered = false;
            HandlerThread renderThread = new HandlerThread(this.name + TAG);
            renderThread.start();
            HandlerWithExceptionCallback handlerWithExceptionCallback = new HandlerWithExceptionCallback(renderThread.getLooper(), new Runnable() { // from class: org.webrtc.EglRenderer.1
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (EglRenderer.this.handlerLock) {
                        EglRenderer.this.renderThreadHandler = null;
                    }
                }
            });
            this.renderThreadHandler = handlerWithExceptionCallback;
            handlerWithExceptionCallback.post(new Runnable() { // from class: org.webrtc.EglRenderer$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    EglRenderer.this.m4834lambda$init$0$orgwebrtcEglRenderer(sharedContext, configAttributes);
                }
            });
            this.renderThreadHandler.post(this.eglSurfaceCreationRunnable);
        }
    }

    /* renamed from: lambda$init$0$org-webrtc-EglRenderer */
    public /* synthetic */ void m4834lambda$init$0$orgwebrtcEglRenderer(EglBase.Context sharedContext, int[] configAttributes) {
        if (sharedContext == null) {
            logD("EglBase10.create context");
            this.eglBase = EglBase.CC.createEgl10(configAttributes);
            return;
        }
        logD("EglBase.create shared context");
        this.eglBase = EglBase.CC.create(sharedContext, configAttributes);
    }

    public void init(EglBase.Context sharedContext, int[] configAttributes, RendererCommon.GlDrawer drawer) {
        init(sharedContext, configAttributes, drawer, false);
    }

    public void createEglSurface(Surface surface) {
        createEglSurfaceInternal(surface, false);
    }

    public void createEglSurface(SurfaceTexture surfaceTexture) {
        createEglSurfaceInternal(surfaceTexture, false);
    }

    public void createBackgroundSurface(SurfaceTexture surface) {
        createEglSurfaceInternal(surface, true);
    }

    private void createEglSurfaceInternal(Object surface, boolean background) {
        if (background) {
            this.eglSurfaceBackgroundCreationRunnable.setSurface(surface);
            synchronized (this.handlerLock) {
                Handler handler = this.renderThreadHandler;
                if (handler != null) {
                    handler.post(this.eglSurfaceBackgroundCreationRunnable);
                } else {
                    FileLog.d("can't create background surface. render thread is null");
                }
            }
            return;
        }
        this.eglSurfaceCreationRunnable.setSurface(surface);
        postToRenderThread(this.eglSurfaceCreationRunnable);
    }

    public void release() {
        logD("Releasing.");
        final CountDownLatch eglCleanupBarrier = new CountDownLatch(1);
        synchronized (this.handlerLock) {
            Handler handler = this.renderThreadHandler;
            if (handler == null) {
                logD("Already released");
                return;
            }
            handler.postAtFrontOfQueue(new Runnable() { // from class: org.webrtc.EglRenderer$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    EglRenderer.this.m4835lambda$release$1$orgwebrtcEglRenderer(eglCleanupBarrier);
                }
            });
            final Looper renderLooper = this.renderThreadHandler.getLooper();
            this.renderThreadHandler.post(new Runnable() { // from class: org.webrtc.EglRenderer$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    EglRenderer.this.m4836lambda$release$2$orgwebrtcEglRenderer(renderLooper);
                }
            });
            this.renderThreadHandler = null;
            ThreadUtils.awaitUninterruptibly(eglCleanupBarrier);
            synchronized (this.frameLock) {
                VideoFrame videoFrame = this.pendingFrame;
                if (videoFrame != null) {
                    videoFrame.release();
                    this.pendingFrame = null;
                }
            }
            logD("Releasing done.");
        }
    }

    /* renamed from: lambda$release$1$org-webrtc-EglRenderer */
    public /* synthetic */ void m4835lambda$release$1$orgwebrtcEglRenderer(CountDownLatch eglCleanupBarrier) {
        synchronized (EglBase.lock) {
            GLES20.glUseProgram(0);
        }
        RendererCommon.GlDrawer glDrawer = this.drawer;
        if (glDrawer != null) {
            glDrawer.release();
            this.drawer = null;
        }
        this.frameDrawer.release();
        this.bitmapTextureFramebuffer.release();
        if (this.eglBase != null) {
            logD("eglBase detach and release.");
            this.eglBase.detachCurrent();
            this.eglBase.release();
            this.eglBase = null;
        }
        this.frameListeners.clear();
        eglCleanupBarrier.countDown();
    }

    /* renamed from: lambda$release$2$org-webrtc-EglRenderer */
    public /* synthetic */ void m4836lambda$release$2$orgwebrtcEglRenderer(Looper renderLooper) {
        logD("Quitting render thread.");
        renderLooper.quit();
    }

    public void printStackTrace() {
        synchronized (this.handlerLock) {
            Handler handler = this.renderThreadHandler;
            Thread renderThread = handler == null ? null : handler.getLooper().getThread();
            if (renderThread != null) {
                StackTraceElement[] renderStackTrace = renderThread.getStackTrace();
                if (renderStackTrace.length > 0) {
                    logW("EglRenderer stack trace:");
                    for (StackTraceElement traceElem : renderStackTrace) {
                        logW(traceElem.toString());
                    }
                }
            }
        }
    }

    public void setMirror(boolean mirror) {
        logD("setMirrorHorizontally: " + mirror);
        synchronized (this.layoutLock) {
            this.mirrorHorizontally = mirror;
        }
    }

    public void setMirrorVertically(boolean mirrorVertically) {
        logD("setMirrorVertically: " + mirrorVertically);
        synchronized (this.layoutLock) {
            this.mirrorVertically = mirrorVertically;
        }
    }

    public void setLayoutAspectRatio(float layoutAspectRatio) {
        if (this.layoutAspectRatio != layoutAspectRatio) {
            synchronized (this.layoutLock) {
                this.layoutAspectRatio = layoutAspectRatio;
            }
        }
    }

    public void setFpsReduction(float fps) {
        logD("setFpsReduction: " + fps);
        synchronized (this.fpsReductionLock) {
            long previousRenderPeriodNs = this.minRenderPeriodNs;
            if (fps <= 0.0f) {
                this.minRenderPeriodNs = Long.MAX_VALUE;
            } else {
                this.minRenderPeriodNs = ((float) TimeUnit.SECONDS.toNanos(1L)) / fps;
            }
            if (this.minRenderPeriodNs != previousRenderPeriodNs) {
                this.nextFrameTimeNs = System.nanoTime();
            }
        }
    }

    public void disableFpsReduction() {
        setFpsReduction(Float.POSITIVE_INFINITY);
    }

    public void pauseVideo() {
        setFpsReduction(0.0f);
    }

    public void addFrameListener(FrameListener listener, float scale) {
        addFrameListener(listener, scale, null, false);
    }

    public void addFrameListener(FrameListener listener, float scale, RendererCommon.GlDrawer drawerParam) {
        addFrameListener(listener, scale, drawerParam, false);
    }

    public void addFrameListener(final FrameListener listener, final float scale, final RendererCommon.GlDrawer drawerParam, final boolean applyFpsReduction) {
        postToRenderThread(new Runnable() { // from class: org.webrtc.EglRenderer$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                EglRenderer.this.m4831lambda$addFrameListener$3$orgwebrtcEglRenderer(drawerParam, listener, scale, applyFpsReduction);
            }
        });
    }

    /* renamed from: lambda$addFrameListener$3$org-webrtc-EglRenderer */
    public /* synthetic */ void m4831lambda$addFrameListener$3$orgwebrtcEglRenderer(RendererCommon.GlDrawer drawerParam, FrameListener listener, float scale, boolean applyFpsReduction) {
        RendererCommon.GlDrawer listenerDrawer = drawerParam == null ? this.drawer : drawerParam;
        this.frameListeners.add(new FrameListenerAndParams(listener, scale, listenerDrawer, applyFpsReduction));
    }

    public void removeFrameListener(final FrameListener listener) {
        final CountDownLatch latch = new CountDownLatch(1);
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler == null) {
                return;
            }
            if (Thread.currentThread() == this.renderThreadHandler.getLooper().getThread()) {
                throw new RuntimeException("removeFrameListener must not be called on the render thread.");
            }
            postToRenderThread(new Runnable() { // from class: org.webrtc.EglRenderer$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    EglRenderer.this.m4838lambda$removeFrameListener$4$orgwebrtcEglRenderer(latch, listener);
                }
            });
            ThreadUtils.awaitUninterruptibly(latch);
        }
    }

    /* renamed from: lambda$removeFrameListener$4$org-webrtc-EglRenderer */
    public /* synthetic */ void m4838lambda$removeFrameListener$4$orgwebrtcEglRenderer(CountDownLatch latch, FrameListener listener) {
        latch.countDown();
        Iterator<FrameListenerAndParams> iter = this.frameListeners.iterator();
        while (iter.hasNext()) {
            if (iter.next().listener == listener) {
                iter.remove();
            }
        }
    }

    public void setErrorCallback(ErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
    }

    @Override // org.webrtc.VideoSink
    public void onFrame(VideoFrame frame) {
        synchronized (this.handlerLock) {
            if (this.renderThreadHandler == null) {
                logD("Dropping frame - Not initialized or already released.");
                return;
            }
            synchronized (this.frameLock) {
                VideoFrame videoFrame = this.pendingFrame;
                boolean dropOldFrame = videoFrame != null;
                if (dropOldFrame) {
                    videoFrame.release();
                }
                this.pendingFrame = frame;
                frame.retain();
                this.renderThreadHandler.post(new Runnable() { // from class: org.webrtc.EglRenderer$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        EglRenderer.this.renderFrameOnRenderThread();
                    }
                });
            }
        }
    }

    public void setRotation(int value) {
        synchronized (this.layoutLock) {
            this.rotation = value;
        }
    }

    public void releaseEglSurface(final Runnable completionCallback, final boolean background) {
        this.eglSurfaceCreationRunnable.setSurface(null);
        synchronized (this.handlerLock) {
            Handler handler = this.renderThreadHandler;
            if (handler != null) {
                handler.removeCallbacks(this.eglSurfaceCreationRunnable);
                this.renderThreadHandler.postAtFrontOfQueue(new Runnable() { // from class: org.webrtc.EglRenderer$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        EglRenderer.this.m4837lambda$releaseEglSurface$5$orgwebrtcEglRenderer(background, completionCallback);
                    }
                });
            } else if (completionCallback != null) {
                completionCallback.run();
            }
        }
    }

    /* renamed from: lambda$releaseEglSurface$5$org-webrtc-EglRenderer */
    public /* synthetic */ void m4837lambda$releaseEglSurface$5$orgwebrtcEglRenderer(boolean background, Runnable completionCallback) {
        EglBase eglBase = this.eglBase;
        if (eglBase != null) {
            eglBase.detachCurrent();
            this.eglBase.releaseSurface(background);
        }
        if (completionCallback != null) {
            completionCallback.run();
        }
    }

    private void postToRenderThread(Runnable runnable) {
        synchronized (this.handlerLock) {
            Handler handler = this.renderThreadHandler;
            if (handler != null) {
                handler.post(runnable);
            }
        }
    }

    /* renamed from: clearSurfaceOnRenderThread */
    public void m4832lambda$clearImage$6$orgwebrtcEglRenderer(float r, float g, float b, float a) {
        EglBase eglBase = this.eglBase;
        if (eglBase != null && eglBase.hasSurface()) {
            logD("clearSurface");
            GLES20.glClearColor(r, g, b, a);
            GLES20.glClear(16384);
            this.eglBase.swapBuffers(false);
        }
    }

    public void clearImage() {
        clearImage(0.0f, 0.0f, 0.0f, 0.0f);
        this.firstFrameRendered = false;
    }

    public void clearImage(final float r, final float g, final float b, final float a) {
        synchronized (this.handlerLock) {
            Handler handler = this.renderThreadHandler;
            if (handler == null) {
                return;
            }
            handler.postAtFrontOfQueue(new Runnable() { // from class: org.webrtc.EglRenderer$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    EglRenderer.this.m4832lambda$clearImage$6$orgwebrtcEglRenderer(r, g, b, a);
                }
            });
        }
    }

    public void getTexture(final GlGenericDrawer.TextureCallback callback) {
        synchronized (this.handlerLock) {
            try {
                Handler handler = this.renderThreadHandler;
                if (handler != null) {
                    handler.post(new Runnable() { // from class: org.webrtc.EglRenderer$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            EglRenderer.this.m4833lambda$getTexture$7$orgwebrtcEglRenderer(callback);
                        }
                    });
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* renamed from: lambda$getTexture$7$org-webrtc-EglRenderer */
    public /* synthetic */ void m4833lambda$getTexture$7$orgwebrtcEglRenderer(GlGenericDrawer.TextureCallback callback) {
        this.frameDrawer.getRenderBufferBitmap(this.drawer, this.rotation, callback);
    }

    /* JADX WARN: Removed duplicated region for block: B:89:0x0192 A[Catch: all -> 0x0186, TryCatch #0 {all -> 0x0186, blocks: (B:64:0x0109, B:66:0x0114, B:68:0x0138, B:69:0x0142, B:70:0x0147, B:71:0x014c, B:73:0x0154, B:74:0x015f, B:75:0x0165, B:77:0x0169, B:83:0x017f, B:87:0x0189, B:89:0x0192, B:90:0x0195), top: B:105:0x00e4 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void renderFrameOnRenderThread() {
        boolean shouldRenderFrame;
        float drawnAspectRatio;
        float scaleX;
        float scaleY;
        Throwable th;
        ErrorCallback errorCallback;
        synchronized (this.frameLock) {
            VideoFrame frame = this.pendingFrame;
            if (frame == null) {
                return;
            }
            this.pendingFrame = null;
            EglBase eglBase = this.eglBase;
            if (eglBase == null || !eglBase.hasSurface()) {
                logD("Dropping frame - No surface");
                frame.release();
                return;
            }
            synchronized (this.fpsReductionLock) {
                long j = this.minRenderPeriodNs;
                if (j == Long.MAX_VALUE) {
                    shouldRenderFrame = false;
                } else if (j <= 0) {
                    shouldRenderFrame = true;
                } else {
                    long currentTimeNs = System.nanoTime();
                    long j2 = this.nextFrameTimeNs;
                    if (currentTimeNs < j2) {
                        shouldRenderFrame = false;
                    } else {
                        long j3 = j2 + this.minRenderPeriodNs;
                        this.nextFrameTimeNs = j3;
                        this.nextFrameTimeNs = Math.max(j3, currentTimeNs);
                        shouldRenderFrame = true;
                    }
                }
            }
            System.nanoTime();
            boolean rotate = Math.abs(this.rotation) == 90 || Math.abs(this.rotation) == 270;
            float frameAspectRatio = (rotate ? frame.getRotatedHeight() : frame.getRotatedWidth()) / (rotate ? frame.getRotatedWidth() : frame.getRotatedHeight());
            synchronized (this.layoutLock) {
                float f = this.layoutAspectRatio;
                if (f == 0.0f) {
                    f = frameAspectRatio;
                }
                drawnAspectRatio = f;
            }
            if (frameAspectRatio > drawnAspectRatio) {
                float scaleX2 = drawnAspectRatio / frameAspectRatio;
                scaleX = scaleX2;
                scaleY = 1.0f;
            } else {
                scaleX = 1.0f;
                scaleY = frameAspectRatio / drawnAspectRatio;
            }
            this.drawMatrix.reset();
            this.drawMatrix.preTranslate(0.5f, 0.5f);
            this.drawMatrix.preRotate(this.rotation);
            Matrix matrix = this.drawMatrix;
            float f2 = -1.0f;
            float f3 = this.mirrorHorizontally ? -1.0f : 1.0f;
            if (!this.mirrorVertically) {
                f2 = 1.0f;
            }
            matrix.preScale(f3, f2);
            this.drawMatrix.preScale(scaleX, scaleY);
            this.drawMatrix.preTranslate(-0.5f, -0.5f);
            try {
                if (shouldRenderFrame) {
                    try {
                        try {
                            this.frameDrawer.drawFrame(frame, this.drawer, this.drawMatrix, 0, 0, this.eglBase.surfaceWidth(), this.eglBase.surfaceHeight(), rotate, false);
                            if (this.eglBase.hasBackgroundSurface()) {
                                this.eglBase.makeBackgroundCurrent();
                                this.frameDrawer.drawFrame(frame, this.drawer, this.drawMatrix, 0, 0, this.eglBase.surfaceWidth(), this.eglBase.surfaceHeight(), rotate, true);
                                if (this.usePresentationTimeStamp) {
                                    this.eglBase.swapBuffers(frame.getTimestampNs(), true);
                                } else {
                                    this.eglBase.swapBuffers(true);
                                }
                                this.eglBase.makeCurrent();
                            }
                            System.nanoTime();
                            if (this.usePresentationTimeStamp) {
                                this.eglBase.swapBuffers(frame.getTimestampNs(), false);
                            } else {
                                this.eglBase.swapBuffers(false);
                            }
                            if (!this.firstFrameRendered) {
                                this.firstFrameRendered = true;
                                onFirstFrameRendered();
                            }
                        } catch (GlUtil.GlOutOfMemoryException e) {
                            e = e;
                            logE("Error while drawing frame", e);
                            errorCallback = this.errorCallback;
                            if (errorCallback != null) {
                                errorCallback.onGlOutOfMemory();
                            }
                            this.drawer.release();
                            this.frameDrawer.release();
                            this.bitmapTextureFramebuffer.release();
                            frame.release();
                        }
                    } catch (GlUtil.GlOutOfMemoryException e2) {
                        e = e2;
                        logE("Error while drawing frame", e);
                        errorCallback = this.errorCallback;
                        if (errorCallback != null) {
                        }
                        this.drawer.release();
                        this.frameDrawer.release();
                        this.bitmapTextureFramebuffer.release();
                        frame.release();
                    } catch (Throwable th2) {
                        th = th2;
                        frame.release();
                        throw th;
                    }
                }
                notifyCallbacks(frame, shouldRenderFrame);
                frame.release();
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    protected void onFirstFrameRendered() {
    }

    private void notifyCallbacks(VideoFrame frame, boolean wasRendered) {
        if (this.frameListeners.isEmpty()) {
            return;
        }
        this.drawMatrix.reset();
        this.drawMatrix.preTranslate(0.5f, 0.5f);
        this.drawMatrix.preRotate(this.rotation);
        this.drawMatrix.preScale(this.mirrorHorizontally ? -1.0f : 1.0f, this.mirrorVertically ? -1.0f : 1.0f);
        this.drawMatrix.preScale(1.0f, -1.0f);
        this.drawMatrix.preTranslate(-0.5f, -0.5f);
        Iterator<FrameListenerAndParams> it = this.frameListeners.iterator();
        while (it.hasNext()) {
            FrameListenerAndParams listenerAndParams = it.next();
            if (wasRendered || !listenerAndParams.applyFpsReduction) {
                it.remove();
                int scaledWidth = (int) (listenerAndParams.scale * frame.getRotatedWidth());
                int scaledHeight = (int) (listenerAndParams.scale * frame.getRotatedHeight());
                if (scaledWidth == 0 || scaledHeight == 0) {
                    listenerAndParams.listener.onFrame(null);
                } else {
                    this.bitmapTextureFramebuffer.setSize(scaledWidth, scaledHeight);
                    GLES20.glBindFramebuffer(36160, this.bitmapTextureFramebuffer.getFrameBufferId());
                    GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.bitmapTextureFramebuffer.getTextureId(), 0);
                    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                    GLES20.glClear(16384);
                    this.frameDrawer.drawFrame(frame, listenerAndParams.drawer, this.drawMatrix, 0, 0, scaledWidth, scaledHeight, false, false);
                    ByteBuffer bitmapBuffer = ByteBuffer.allocateDirect(scaledWidth * scaledHeight * 4);
                    GLES20.glViewport(0, 0, scaledWidth, scaledHeight);
                    GLES20.glReadPixels(0, 0, scaledWidth, scaledHeight, 6408, 5121, bitmapBuffer);
                    GLES20.glBindFramebuffer(36160, 0);
                    GlUtil.checkNoGLES2Error("EglRenderer.notifyCallbacks");
                    Bitmap bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(bitmapBuffer);
                    listenerAndParams.listener.onFrame(bitmap);
                }
            }
        }
    }

    private void logE(String string, Throwable e) {
        Logging.e(TAG, this.name + string, e);
    }

    private void logD(String string) {
        Logging.d(TAG, this.name + string);
    }

    private void logW(String string) {
        Logging.w(TAG, this.name + string);
    }
}
