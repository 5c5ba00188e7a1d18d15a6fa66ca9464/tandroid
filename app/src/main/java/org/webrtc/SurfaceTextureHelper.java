package org.webrtc;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import java.util.concurrent.Callable;
import org.webrtc.EglBase;
import org.webrtc.TextureBufferImpl;
import org.webrtc.VideoFrame;
/* loaded from: classes3.dex */
public class SurfaceTextureHelper {
    private static final String TAG = "SurfaceTextureHelper";
    private final EglBase eglBase;
    private final FrameRefMonitor frameRefMonitor;
    private int frameRotation;
    private final Handler handler;
    private boolean hasPendingTexture;
    private boolean isQuitting;
    private volatile boolean isTextureInUse;
    private VideoSink listener;
    private final int oesTextureId;
    private VideoSink pendingListener;
    final Runnable setListenerRunnable;
    private final SurfaceTexture surfaceTexture;
    private int textureHeight;
    private final TextureBufferImpl.RefCountMonitor textureRefCountMonitor;
    private int textureWidth;
    private final TimestampAligner timestampAligner;
    private final YuvConverter yuvConverter;

    /* loaded from: classes3.dex */
    public interface FrameRefMonitor {
        void onDestroyBuffer(VideoFrame.TextureBuffer textureBuffer);

        void onNewBuffer(VideoFrame.TextureBuffer textureBuffer);

        void onReleaseBuffer(VideoFrame.TextureBuffer textureBuffer);

        void onRetainBuffer(VideoFrame.TextureBuffer textureBuffer);
    }

    /* synthetic */ SurfaceTextureHelper(EglBase.Context context, Handler handler, boolean z, YuvConverter yuvConverter, FrameRefMonitor frameRefMonitor, AnonymousClass1 anonymousClass1) {
        this(context, handler, z, yuvConverter, frameRefMonitor);
    }

    public static SurfaceTextureHelper create(String str, EglBase.Context context, boolean z, YuvConverter yuvConverter, FrameRefMonitor frameRefMonitor) {
        HandlerThread handlerThread = new HandlerThread(str);
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        return (SurfaceTextureHelper) ThreadUtils.invokeAtFrontUninterruptibly(handler, new AnonymousClass1(context, handler, z, yuvConverter, frameRefMonitor, str));
    }

    /* renamed from: org.webrtc.SurfaceTextureHelper$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Callable<SurfaceTextureHelper> {
        final /* synthetic */ boolean val$alignTimestamps;
        final /* synthetic */ FrameRefMonitor val$frameRefMonitor;
        final /* synthetic */ Handler val$handler;
        final /* synthetic */ EglBase.Context val$sharedContext;
        final /* synthetic */ String val$threadName;
        final /* synthetic */ YuvConverter val$yuvConverter;

        AnonymousClass1(EglBase.Context context, Handler handler, boolean z, YuvConverter yuvConverter, FrameRefMonitor frameRefMonitor, String str) {
            this.val$sharedContext = context;
            this.val$handler = handler;
            this.val$alignTimestamps = z;
            this.val$yuvConverter = yuvConverter;
            this.val$frameRefMonitor = frameRefMonitor;
            this.val$threadName = str;
        }

        @Override // java.util.concurrent.Callable
        public SurfaceTextureHelper call() {
            try {
                return new SurfaceTextureHelper(this.val$sharedContext, this.val$handler, this.val$alignTimestamps, this.val$yuvConverter, this.val$frameRefMonitor, null);
            } catch (RuntimeException e) {
                Logging.e("SurfaceTextureHelper", this.val$threadName + " create failure", e);
                return null;
            }
        }
    }

    public static SurfaceTextureHelper create(String str, EglBase.Context context) {
        return create(str, context, false, new YuvConverter(), null);
    }

    public static SurfaceTextureHelper create(String str, EglBase.Context context, boolean z) {
        return create(str, context, z, new YuvConverter(), null);
    }

    public static SurfaceTextureHelper create(String str, EglBase.Context context, boolean z, YuvConverter yuvConverter) {
        return create(str, context, z, yuvConverter, null);
    }

    /* renamed from: org.webrtc.SurfaceTextureHelper$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements TextureBufferImpl.RefCountMonitor {
        AnonymousClass2() {
            SurfaceTextureHelper.this = r1;
        }

        @Override // org.webrtc.TextureBufferImpl.RefCountMonitor
        public void onRetain(TextureBufferImpl textureBufferImpl) {
            if (SurfaceTextureHelper.this.frameRefMonitor != null) {
                SurfaceTextureHelper.this.frameRefMonitor.onRetainBuffer(textureBufferImpl);
            }
        }

        @Override // org.webrtc.TextureBufferImpl.RefCountMonitor
        public void onRelease(TextureBufferImpl textureBufferImpl) {
            if (SurfaceTextureHelper.this.frameRefMonitor != null) {
                SurfaceTextureHelper.this.frameRefMonitor.onReleaseBuffer(textureBufferImpl);
            }
        }

        @Override // org.webrtc.TextureBufferImpl.RefCountMonitor
        public void onDestroy(TextureBufferImpl textureBufferImpl) {
            SurfaceTextureHelper.this.returnTextureFrame();
            if (SurfaceTextureHelper.this.frameRefMonitor != null) {
                SurfaceTextureHelper.this.frameRefMonitor.onDestroyBuffer(textureBufferImpl);
            }
        }
    }

    /* renamed from: org.webrtc.SurfaceTextureHelper$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
            SurfaceTextureHelper.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            Logging.d("SurfaceTextureHelper", "Setting listener to " + SurfaceTextureHelper.this.pendingListener);
            SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.this;
            surfaceTextureHelper.listener = surfaceTextureHelper.pendingListener;
            SurfaceTextureHelper.this.pendingListener = null;
            if (SurfaceTextureHelper.this.hasPendingTexture) {
                SurfaceTextureHelper.this.updateTexImage();
                SurfaceTextureHelper.this.hasPendingTexture = false;
            }
        }
    }

    private SurfaceTextureHelper(EglBase.Context context, Handler handler, boolean z, YuvConverter yuvConverter, FrameRefMonitor frameRefMonitor) {
        this.textureRefCountMonitor = new AnonymousClass2();
        this.setListenerRunnable = new AnonymousClass3();
        if (handler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("SurfaceTextureHelper must be created on the handler thread");
        }
        this.handler = handler;
        this.timestampAligner = z ? new TimestampAligner() : null;
        this.yuvConverter = yuvConverter;
        this.frameRefMonitor = frameRefMonitor;
        EglBase create = EglBase.CC.create(context, EglBase.CONFIG_PIXEL_BUFFER);
        this.eglBase = create;
        try {
            create.createDummyPbufferSurface();
            create.makeCurrent();
            int generateTexture = GlUtil.generateTexture(36197);
            this.oesTextureId = generateTexture;
            SurfaceTexture surfaceTexture = new SurfaceTexture(generateTexture);
            this.surfaceTexture = surfaceTexture;
            setOnFrameAvailableListener(surfaceTexture, new SurfaceTextureHelper$$ExternalSyntheticLambda0(this), handler);
        } catch (RuntimeException e) {
            this.eglBase.release();
            handler.getLooper().quit();
            throw e;
        }
    }

    public /* synthetic */ void lambda$new$0(SurfaceTexture surfaceTexture) {
        if (this.hasPendingTexture) {
            Logging.d("SurfaceTextureHelper", "A frame is already pending, dropping frame.");
        }
        this.hasPendingTexture = true;
        tryDeliverTextureFrame();
    }

    @TargetApi(21)
    private static void setOnFrameAvailableListener(SurfaceTexture surfaceTexture, SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener, Handler handler) {
        if (Build.VERSION.SDK_INT >= 21) {
            surfaceTexture.setOnFrameAvailableListener(onFrameAvailableListener, handler);
        } else {
            surfaceTexture.setOnFrameAvailableListener(onFrameAvailableListener);
        }
    }

    public void startListening(VideoSink videoSink) {
        if (this.listener != null || this.pendingListener != null) {
            throw new IllegalStateException("SurfaceTextureHelper listener has already been set.");
        }
        this.pendingListener = videoSink;
        this.handler.post(this.setListenerRunnable);
    }

    public void stopListening() {
        Logging.d("SurfaceTextureHelper", "stopListening()");
        this.handler.removeCallbacks(this.setListenerRunnable);
        ThreadUtils.invokeAtFrontUninterruptibly(this.handler, new SurfaceTextureHelper$$ExternalSyntheticLambda3(this));
    }

    public /* synthetic */ void lambda$stopListening$1() {
        this.listener = null;
        this.pendingListener = null;
    }

    public void setTextureSize(int i, int i2) {
        if (i <= 0) {
            throw new IllegalArgumentException("Texture width must be positive, but was " + i);
        } else if (i2 <= 0) {
            throw new IllegalArgumentException("Texture height must be positive, but was " + i2);
        } else {
            this.surfaceTexture.setDefaultBufferSize(i, i2);
            this.handler.post(new SurfaceTextureHelper$$ExternalSyntheticLambda6(this, i, i2));
        }
    }

    public /* synthetic */ void lambda$setTextureSize$2(int i, int i2) {
        this.textureWidth = i;
        this.textureHeight = i2;
        tryDeliverTextureFrame();
    }

    public void forceFrame() {
        this.handler.post(new SurfaceTextureHelper$$ExternalSyntheticLambda2(this));
    }

    public /* synthetic */ void lambda$forceFrame$3() {
        this.hasPendingTexture = true;
        tryDeliverTextureFrame();
    }

    public /* synthetic */ void lambda$setFrameRotation$4(int i) {
        this.frameRotation = i;
    }

    public void setFrameRotation(int i) {
        this.handler.post(new SurfaceTextureHelper$$ExternalSyntheticLambda5(this, i));
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.surfaceTexture;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void returnTextureFrame() {
        this.handler.post(new SurfaceTextureHelper$$ExternalSyntheticLambda4(this));
    }

    public /* synthetic */ void lambda$returnTextureFrame$5() {
        this.isTextureInUse = false;
        if (this.isQuitting) {
            release();
        } else {
            tryDeliverTextureFrame();
        }
    }

    public boolean isTextureInUse() {
        return this.isTextureInUse;
    }

    public void dispose() {
        Logging.d("SurfaceTextureHelper", "dispose()");
        ThreadUtils.invokeAtFrontUninterruptibly(this.handler, new SurfaceTextureHelper$$ExternalSyntheticLambda1(this));
    }

    public /* synthetic */ void lambda$dispose$6() {
        this.isQuitting = true;
        if (!this.isTextureInUse) {
            release();
        }
    }

    @Deprecated
    public VideoFrame.I420Buffer textureToYuv(VideoFrame.TextureBuffer textureBuffer) {
        return textureBuffer.toI420();
    }

    public void updateTexImage() {
        synchronized (EglBase.lock) {
            try {
                this.surfaceTexture.updateTexImage();
            } catch (Throwable unused) {
            }
        }
    }

    private void tryDeliverTextureFrame() {
        if (this.handler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Wrong thread.");
        }
        if (this.isQuitting || !this.hasPendingTexture || this.isTextureInUse || this.listener == null) {
            return;
        }
        if (this.textureWidth == 0 || this.textureHeight == 0) {
            Logging.w("SurfaceTextureHelper", "Texture size has not been set.");
            return;
        }
        this.isTextureInUse = true;
        this.hasPendingTexture = false;
        updateTexImage();
        float[] fArr = new float[16];
        this.surfaceTexture.getTransformMatrix(fArr);
        long timestamp = this.surfaceTexture.getTimestamp();
        TimestampAligner timestampAligner = this.timestampAligner;
        if (timestampAligner != null) {
            timestamp = timestampAligner.translateTimestamp(timestamp);
        }
        TextureBufferImpl textureBufferImpl = new TextureBufferImpl(this.textureWidth, this.textureHeight, VideoFrame.TextureBuffer.Type.OES, this.oesTextureId, RendererCommon.convertMatrixToAndroidGraphicsMatrix(fArr), this.handler, this.yuvConverter, this.textureRefCountMonitor);
        FrameRefMonitor frameRefMonitor = this.frameRefMonitor;
        if (frameRefMonitor != null) {
            frameRefMonitor.onNewBuffer(textureBufferImpl);
        }
        VideoFrame videoFrame = new VideoFrame(textureBufferImpl, this.frameRotation, timestamp);
        this.listener.onFrame(videoFrame);
        videoFrame.release();
    }

    private void release() {
        if (this.handler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Wrong thread.");
        }
        if (this.isTextureInUse || !this.isQuitting) {
            throw new IllegalStateException("Unexpected release.");
        }
        this.yuvConverter.release();
        GLES20.glDeleteTextures(1, new int[]{this.oesTextureId}, 0);
        this.surfaceTexture.release();
        this.eglBase.release();
        this.handler.getLooper().quit();
        TimestampAligner timestampAligner = this.timestampAligner;
        if (timestampAligner == null) {
            return;
        }
        timestampAligner.dispose();
    }
}
