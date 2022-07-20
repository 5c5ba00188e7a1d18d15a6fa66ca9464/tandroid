package org.webrtc;

import android.graphics.Matrix;
import android.os.Handler;
import java.nio.ByteBuffer;
import org.telegram.messenger.FileLog;
import org.webrtc.VideoFrame;
/* loaded from: classes3.dex */
public class TextureBufferImpl implements VideoFrame.TextureBuffer {
    private final int height;
    private final int id;
    private final RefCountDelegate refCountDelegate;
    private final RefCountMonitor refCountMonitor;
    private final Handler toI420Handler;
    private final Matrix transformMatrix;
    private final VideoFrame.TextureBuffer.Type type;
    private final int unscaledHeight;
    private final int unscaledWidth;
    private final int width;
    private final YuvConverter yuvConverter;

    /* loaded from: classes3.dex */
    public interface RefCountMonitor {
        void onDestroy(TextureBufferImpl textureBufferImpl);

        void onRelease(TextureBufferImpl textureBufferImpl);

        void onRetain(TextureBufferImpl textureBufferImpl);
    }

    @Override // org.webrtc.VideoFrame.Buffer
    public /* synthetic */ int getBufferType() {
        return VideoFrame.Buffer.CC.$default$getBufferType(this);
    }

    /* renamed from: org.webrtc.TextureBufferImpl$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 implements RefCountMonitor {
        final /* synthetic */ Runnable val$releaseCallback;

        @Override // org.webrtc.TextureBufferImpl.RefCountMonitor
        public void onRelease(TextureBufferImpl textureBufferImpl) {
        }

        @Override // org.webrtc.TextureBufferImpl.RefCountMonitor
        public void onRetain(TextureBufferImpl textureBufferImpl) {
        }

        AnonymousClass1(Runnable runnable) {
            this.val$releaseCallback = runnable;
        }

        @Override // org.webrtc.TextureBufferImpl.RefCountMonitor
        public void onDestroy(TextureBufferImpl textureBufferImpl) {
            Runnable runnable = this.val$releaseCallback;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public TextureBufferImpl(int i, int i2, VideoFrame.TextureBuffer.Type type, int i3, Matrix matrix, Handler handler, YuvConverter yuvConverter, Runnable runnable) {
        this(i, i2, i, i2, type, i3, matrix, handler, yuvConverter, new AnonymousClass1(runnable));
    }

    public TextureBufferImpl(int i, int i2, VideoFrame.TextureBuffer.Type type, int i3, Matrix matrix, Handler handler, YuvConverter yuvConverter, RefCountMonitor refCountMonitor) {
        this(i, i2, i, i2, type, i3, matrix, handler, yuvConverter, refCountMonitor);
    }

    private TextureBufferImpl(int i, int i2, int i3, int i4, VideoFrame.TextureBuffer.Type type, int i5, Matrix matrix, Handler handler, YuvConverter yuvConverter, RefCountMonitor refCountMonitor) {
        this.unscaledWidth = i;
        this.unscaledHeight = i2;
        this.width = i3;
        this.height = i4;
        this.type = type;
        this.id = i5;
        this.transformMatrix = matrix;
        this.toI420Handler = handler;
        this.yuvConverter = yuvConverter;
        this.refCountDelegate = new RefCountDelegate(new TextureBufferImpl$$ExternalSyntheticLambda1(this, refCountMonitor));
        this.refCountMonitor = refCountMonitor;
    }

    public /* synthetic */ void lambda$new$0(RefCountMonitor refCountMonitor) {
        refCountMonitor.onDestroy(this);
    }

    @Override // org.webrtc.VideoFrame.TextureBuffer
    public VideoFrame.TextureBuffer.Type getType() {
        return this.type;
    }

    @Override // org.webrtc.VideoFrame.TextureBuffer
    public int getTextureId() {
        return this.id;
    }

    @Override // org.webrtc.VideoFrame.TextureBuffer
    public Matrix getTransformMatrix() {
        return this.transformMatrix;
    }

    @Override // org.webrtc.VideoFrame.Buffer
    public int getWidth() {
        return this.width;
    }

    @Override // org.webrtc.VideoFrame.Buffer
    public int getHeight() {
        return this.height;
    }

    @Override // org.webrtc.VideoFrame.Buffer
    public VideoFrame.I420Buffer toI420() {
        try {
            return (VideoFrame.I420Buffer) ThreadUtils.invokeAtFrontUninterruptibly(this.toI420Handler, new TextureBufferImpl$$ExternalSyntheticLambda2(this));
        } catch (Throwable th) {
            FileLog.e(th);
            int width = getWidth();
            int height = getHeight();
            int i = ((width + 7) / 8) * 8;
            int i2 = (height + 1) / 2;
            ByteBuffer nativeAllocateByteBuffer = JniCommon.nativeAllocateByteBuffer((height + i2) * i);
            while (nativeAllocateByteBuffer.hasRemaining()) {
                nativeAllocateByteBuffer.put((byte) 0);
            }
            int i3 = i / 4;
            int i4 = (i * height) + 0;
            int i5 = i / 2;
            int i6 = i4 + i5;
            nativeAllocateByteBuffer.position(0);
            nativeAllocateByteBuffer.limit(i4);
            ByteBuffer slice = nativeAllocateByteBuffer.slice();
            nativeAllocateByteBuffer.position(i4);
            int i7 = ((i2 - 1) * i) + i5;
            nativeAllocateByteBuffer.limit(i4 + i7);
            ByteBuffer slice2 = nativeAllocateByteBuffer.slice();
            nativeAllocateByteBuffer.position(i6);
            nativeAllocateByteBuffer.limit(i6 + i7);
            return JavaI420Buffer.wrap(width, height, slice, i, slice2, i, nativeAllocateByteBuffer.slice(), i, new TextureBufferImpl$$ExternalSyntheticLambda0(nativeAllocateByteBuffer));
        }
    }

    public /* synthetic */ VideoFrame.I420Buffer lambda$toI420$1() throws Exception {
        return this.yuvConverter.convert(this);
    }

    @Override // org.webrtc.VideoFrame.Buffer, org.webrtc.RefCounted
    public void retain() {
        this.refCountMonitor.onRetain(this);
        this.refCountDelegate.retain();
    }

    @Override // org.webrtc.VideoFrame.Buffer, org.webrtc.RefCounted
    public void release() {
        this.refCountMonitor.onRelease(this);
        this.refCountDelegate.release();
    }

    @Override // org.webrtc.VideoFrame.Buffer
    public VideoFrame.Buffer cropAndScale(int i, int i2, int i3, int i4, int i5, int i6) {
        Matrix matrix = new Matrix();
        int i7 = this.height;
        matrix.preTranslate(i / this.width, (i7 - (i2 + i4)) / i7);
        matrix.preScale(i3 / this.width, i4 / this.height);
        return applyTransformMatrix(matrix, Math.round((this.unscaledWidth * i3) / this.width), Math.round((this.unscaledHeight * i4) / this.height), i5, i6);
    }

    public int getUnscaledWidth() {
        return this.unscaledWidth;
    }

    public int getUnscaledHeight() {
        return this.unscaledHeight;
    }

    public Handler getToI420Handler() {
        return this.toI420Handler;
    }

    public YuvConverter getYuvConverter() {
        return this.yuvConverter;
    }

    public TextureBufferImpl applyTransformMatrix(Matrix matrix, int i, int i2) {
        return applyTransformMatrix(matrix, i, i2, i, i2);
    }

    private TextureBufferImpl applyTransformMatrix(Matrix matrix, int i, int i2, int i3, int i4) {
        Matrix matrix2 = new Matrix(this.transformMatrix);
        matrix2.preConcat(matrix);
        retain();
        return new TextureBufferImpl(i, i2, i3, i4, this.type, this.id, matrix2, this.toI420Handler, this.yuvConverter, new AnonymousClass2());
    }

    /* renamed from: org.webrtc.TextureBufferImpl$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements RefCountMonitor {
        AnonymousClass2() {
            TextureBufferImpl.this = r1;
        }

        @Override // org.webrtc.TextureBufferImpl.RefCountMonitor
        public void onRetain(TextureBufferImpl textureBufferImpl) {
            TextureBufferImpl.this.refCountMonitor.onRetain(TextureBufferImpl.this);
        }

        @Override // org.webrtc.TextureBufferImpl.RefCountMonitor
        public void onRelease(TextureBufferImpl textureBufferImpl) {
            TextureBufferImpl.this.refCountMonitor.onRelease(TextureBufferImpl.this);
        }

        @Override // org.webrtc.TextureBufferImpl.RefCountMonitor
        public void onDestroy(TextureBufferImpl textureBufferImpl) {
            TextureBufferImpl.this.release();
        }
    }
}
