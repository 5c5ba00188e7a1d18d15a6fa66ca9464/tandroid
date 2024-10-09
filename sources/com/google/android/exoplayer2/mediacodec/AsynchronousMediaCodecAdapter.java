package com.google.android.exoplayer2.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import com.google.android.exoplayer2.decoder.CryptoInfo;
import com.google.android.exoplayer2.mediacodec.AsynchronousMediaCodecAdapter;
import com.google.android.exoplayer2.mediacodec.MediaCodecAdapter;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.common.base.Supplier;
import java.nio.ByteBuffer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AsynchronousMediaCodecAdapter implements MediaCodecAdapter {
    private final AsynchronousMediaCodecCallback asynchronousMediaCodecCallback;
    private final AsynchronousMediaCodecBufferEnqueuer bufferEnqueuer;
    private final MediaCodec codec;
    private boolean codecReleased;
    private int state;
    private final boolean synchronizeCodecInteractionsWithQueueing;

    /* loaded from: classes.dex */
    public static final class Factory implements MediaCodecAdapter.Factory {
        private final Supplier callbackThreadSupplier;
        private final Supplier queueingThreadSupplier;
        private final boolean synchronizeCodecInteractionsWithQueueing;

        public Factory(final int i, boolean z) {
            this(new Supplier() { // from class: com.google.android.exoplayer2.mediacodec.AsynchronousMediaCodecAdapter$Factory$$ExternalSyntheticLambda0
                @Override // com.google.common.base.Supplier
                public final Object get() {
                    HandlerThread lambda$new$0;
                    lambda$new$0 = AsynchronousMediaCodecAdapter.Factory.lambda$new$0(i);
                    return lambda$new$0;
                }
            }, new Supplier() { // from class: com.google.android.exoplayer2.mediacodec.AsynchronousMediaCodecAdapter$Factory$$ExternalSyntheticLambda1
                @Override // com.google.common.base.Supplier
                public final Object get() {
                    HandlerThread lambda$new$1;
                    lambda$new$1 = AsynchronousMediaCodecAdapter.Factory.lambda$new$1(i);
                    return lambda$new$1;
                }
            }, z);
        }

        Factory(Supplier supplier, Supplier supplier2, boolean z) {
            this.callbackThreadSupplier = supplier;
            this.queueingThreadSupplier = supplier2;
            this.synchronizeCodecInteractionsWithQueueing = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ HandlerThread lambda$new$0(int i) {
            return new HandlerThread(AsynchronousMediaCodecAdapter.createCallbackThreadLabel(i));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ HandlerThread lambda$new$1(int i) {
            return new HandlerThread(AsynchronousMediaCodecAdapter.createQueueingThreadLabel(i));
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter.Factory
        public AsynchronousMediaCodecAdapter createAdapter(MediaCodecAdapter.Configuration configuration) {
            MediaCodec mediaCodec;
            AsynchronousMediaCodecAdapter asynchronousMediaCodecAdapter;
            String str = configuration.codecInfo.name;
            AsynchronousMediaCodecAdapter asynchronousMediaCodecAdapter2 = null;
            try {
                TraceUtil.beginSection("createCodec:" + str);
                mediaCodec = MediaCodec.createByCodecName(str);
                try {
                    asynchronousMediaCodecAdapter = new AsynchronousMediaCodecAdapter(mediaCodec, (HandlerThread) this.callbackThreadSupplier.get(), (HandlerThread) this.queueingThreadSupplier.get(), this.synchronizeCodecInteractionsWithQueueing);
                } catch (Exception e) {
                    e = e;
                }
            } catch (Exception e2) {
                e = e2;
                mediaCodec = null;
            }
            try {
                TraceUtil.endSection();
                asynchronousMediaCodecAdapter.initialize(configuration.mediaFormat, configuration.surface, configuration.crypto, configuration.flags);
                return asynchronousMediaCodecAdapter;
            } catch (Exception e3) {
                e = e3;
                asynchronousMediaCodecAdapter2 = asynchronousMediaCodecAdapter;
                if (asynchronousMediaCodecAdapter2 != null) {
                    asynchronousMediaCodecAdapter2.release();
                } else if (mediaCodec != null) {
                    mediaCodec.release();
                }
                throw e;
            }
        }
    }

    private AsynchronousMediaCodecAdapter(MediaCodec mediaCodec, HandlerThread handlerThread, HandlerThread handlerThread2, boolean z) {
        this.codec = mediaCodec;
        this.asynchronousMediaCodecCallback = new AsynchronousMediaCodecCallback(handlerThread);
        this.bufferEnqueuer = new AsynchronousMediaCodecBufferEnqueuer(mediaCodec, handlerThread2);
        this.synchronizeCodecInteractionsWithQueueing = z;
        this.state = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String createCallbackThreadLabel(int i) {
        return createThreadLabel(i, "ExoPlayer:MediaCodecAsyncAdapter:");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String createQueueingThreadLabel(int i) {
        return createThreadLabel(i, "ExoPlayer:MediaCodecQueueingThread:");
    }

    private static String createThreadLabel(int i, String str) {
        String str2;
        StringBuilder sb = new StringBuilder(str);
        if (i == 1) {
            str2 = "Audio";
        } else if (i == 2) {
            str2 = "Video";
        } else {
            sb.append("Unknown(");
            sb.append(i);
            str2 = ")";
        }
        sb.append(str2);
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initialize(MediaFormat mediaFormat, Surface surface, MediaCrypto mediaCrypto, int i) {
        this.asynchronousMediaCodecCallback.initialize(this.codec);
        TraceUtil.beginSection("configureCodec");
        this.codec.configure(mediaFormat, surface, mediaCrypto, i);
        TraceUtil.endSection();
        this.bufferEnqueuer.start();
        TraceUtil.beginSection("startCodec");
        this.codec.start();
        TraceUtil.endSection();
        this.state = 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnFrameRenderedListener$0(MediaCodecAdapter.OnFrameRenderedListener onFrameRenderedListener, MediaCodec mediaCodec, long j, long j2) {
        onFrameRenderedListener.onFrameRendered(this, j, j2);
    }

    private void maybeBlockOnQueueing() {
        if (this.synchronizeCodecInteractionsWithQueueing) {
            try {
                this.bufferEnqueuer.waitUntilQueueingComplete();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public int dequeueInputBufferIndex() {
        return this.asynchronousMediaCodecCallback.dequeueInputBufferIndex();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public int dequeueOutputBufferIndex(MediaCodec.BufferInfo bufferInfo) {
        return this.asynchronousMediaCodecCallback.dequeueOutputBufferIndex(bufferInfo);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public void flush() {
        this.bufferEnqueuer.flush();
        this.codec.flush();
        this.asynchronousMediaCodecCallback.flush();
        this.codec.start();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public ByteBuffer getInputBuffer(int i) {
        ByteBuffer inputBuffer;
        inputBuffer = this.codec.getInputBuffer(i);
        return inputBuffer;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public ByteBuffer getOutputBuffer(int i) {
        ByteBuffer outputBuffer;
        outputBuffer = this.codec.getOutputBuffer(i);
        return outputBuffer;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public MediaFormat getOutputFormat() {
        return this.asynchronousMediaCodecCallback.getOutputFormat();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public boolean needsReconfiguration() {
        return false;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public void queueInputBuffer(int i, int i2, int i3, long j, int i4) {
        this.bufferEnqueuer.queueInputBuffer(i, i2, i3, j, i4);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public void queueSecureInputBuffer(int i, int i2, CryptoInfo cryptoInfo, long j, int i3) {
        this.bufferEnqueuer.queueSecureInputBuffer(i, i2, cryptoInfo, j, i3);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public void release() {
        try {
            if (this.state == 1) {
                this.bufferEnqueuer.shutdown();
                this.asynchronousMediaCodecCallback.shutdown();
            }
            this.state = 2;
            if (this.codecReleased) {
                return;
            }
            this.codec.release();
            this.codecReleased = true;
        } catch (Throwable th) {
            if (!this.codecReleased) {
                this.codec.release();
                this.codecReleased = true;
            }
            throw th;
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public void releaseOutputBuffer(int i, long j) {
        this.codec.releaseOutputBuffer(i, j);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public void releaseOutputBuffer(int i, boolean z) {
        this.codec.releaseOutputBuffer(i, z);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public void setOnFrameRenderedListener(final MediaCodecAdapter.OnFrameRenderedListener onFrameRenderedListener, Handler handler) {
        maybeBlockOnQueueing();
        this.codec.setOnFrameRenderedListener(new MediaCodec.OnFrameRenderedListener() { // from class: com.google.android.exoplayer2.mediacodec.AsynchronousMediaCodecAdapter$$ExternalSyntheticLambda5
            @Override // android.media.MediaCodec.OnFrameRenderedListener
            public final void onFrameRendered(MediaCodec mediaCodec, long j, long j2) {
                AsynchronousMediaCodecAdapter.this.lambda$setOnFrameRenderedListener$0(onFrameRenderedListener, mediaCodec, j, j2);
            }
        }, handler);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public void setOutputSurface(Surface surface) {
        maybeBlockOnQueueing();
        this.codec.setOutputSurface(surface);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public void setParameters(Bundle bundle) {
        maybeBlockOnQueueing();
        this.codec.setParameters(bundle);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
    public void setVideoScalingMode(int i) {
        maybeBlockOnQueueing();
        this.codec.setVideoScalingMode(i);
    }
}
