package com.google.android.exoplayer2.decoder;

import com.google.android.exoplayer2.util.Assertions;
import java.util.ArrayDeque;

/* loaded from: classes.dex */
public abstract class SimpleDecoder implements Decoder {
    private int availableInputBufferCount;
    private final DecoderInputBuffer[] availableInputBuffers;
    private int availableOutputBufferCount;
    private final DecoderOutputBuffer[] availableOutputBuffers;
    private final Thread decodeThread;
    private DecoderInputBuffer dequeuedInputBuffer;
    private DecoderException exception;
    private boolean flushed;
    private final Object lock = new Object();
    private final ArrayDeque<DecoderInputBuffer> queuedInputBuffers = new ArrayDeque<>();
    private final ArrayDeque<DecoderOutputBuffer> queuedOutputBuffers = new ArrayDeque<>();
    private boolean released;
    private int skippedOutputBufferCount;

    /* JADX INFO: Access modifiers changed from: protected */
    public SimpleDecoder(DecoderInputBuffer[] decoderInputBufferArr, DecoderOutputBuffer[] decoderOutputBufferArr) {
        this.availableInputBuffers = decoderInputBufferArr;
        this.availableInputBufferCount = decoderInputBufferArr.length;
        for (int i = 0; i < this.availableInputBufferCount; i++) {
            this.availableInputBuffers[i] = createInputBuffer();
        }
        this.availableOutputBuffers = decoderOutputBufferArr;
        this.availableOutputBufferCount = decoderOutputBufferArr.length;
        for (int i2 = 0; i2 < this.availableOutputBufferCount; i2++) {
            this.availableOutputBuffers[i2] = createOutputBuffer();
        }
        Thread thread = new Thread("ExoPlayer:SimpleDecoder") { // from class: com.google.android.exoplayer2.decoder.SimpleDecoder.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                SimpleDecoder.this.run();
            }
        };
        this.decodeThread = thread;
        thread.start();
    }

    private boolean canDecodeBuffer() {
        return !this.queuedInputBuffers.isEmpty() && this.availableOutputBufferCount > 0;
    }

    private boolean decode() {
        DecoderException createUnexpectedDecodeException;
        synchronized (this.lock) {
            while (!this.released && !canDecodeBuffer()) {
                try {
                    this.lock.wait();
                } finally {
                }
            }
            if (this.released) {
                return false;
            }
            DecoderInputBuffer removeFirst = this.queuedInputBuffers.removeFirst();
            DecoderOutputBuffer[] decoderOutputBufferArr = this.availableOutputBuffers;
            int i = this.availableOutputBufferCount - 1;
            this.availableOutputBufferCount = i;
            DecoderOutputBuffer decoderOutputBuffer = decoderOutputBufferArr[i];
            boolean z = this.flushed;
            this.flushed = false;
            if (removeFirst.isEndOfStream()) {
                decoderOutputBuffer.addFlag(4);
            } else {
                if (removeFirst.isDecodeOnly()) {
                    decoderOutputBuffer.addFlag(Integer.MIN_VALUE);
                }
                if (removeFirst.isFirstSample()) {
                    decoderOutputBuffer.addFlag(134217728);
                }
                try {
                    createUnexpectedDecodeException = decode(removeFirst, decoderOutputBuffer, z);
                } catch (OutOfMemoryError | RuntimeException e) {
                    createUnexpectedDecodeException = createUnexpectedDecodeException(e);
                }
                if (createUnexpectedDecodeException != null) {
                    synchronized (this.lock) {
                        this.exception = createUnexpectedDecodeException;
                    }
                    return false;
                }
            }
            synchronized (this.lock) {
                try {
                    if (!this.flushed) {
                        if (decoderOutputBuffer.isDecodeOnly()) {
                            this.skippedOutputBufferCount++;
                        } else {
                            decoderOutputBuffer.skippedOutputBufferCount = this.skippedOutputBufferCount;
                            this.skippedOutputBufferCount = 0;
                            this.queuedOutputBuffers.addLast(decoderOutputBuffer);
                            releaseInputBufferInternal(removeFirst);
                        }
                    }
                    decoderOutputBuffer.release();
                    releaseInputBufferInternal(removeFirst);
                } finally {
                }
            }
            return true;
        }
    }

    private void maybeNotifyDecodeLoop() {
        if (canDecodeBuffer()) {
            this.lock.notify();
        }
    }

    private void maybeThrowException() {
        DecoderException decoderException = this.exception;
        if (decoderException != null) {
            throw decoderException;
        }
    }

    private void releaseInputBufferInternal(DecoderInputBuffer decoderInputBuffer) {
        decoderInputBuffer.clear();
        DecoderInputBuffer[] decoderInputBufferArr = this.availableInputBuffers;
        int i = this.availableInputBufferCount;
        this.availableInputBufferCount = i + 1;
        decoderInputBufferArr[i] = decoderInputBuffer;
    }

    private void releaseOutputBufferInternal(DecoderOutputBuffer decoderOutputBuffer) {
        decoderOutputBuffer.clear();
        DecoderOutputBuffer[] decoderOutputBufferArr = this.availableOutputBuffers;
        int i = this.availableOutputBufferCount;
        this.availableOutputBufferCount = i + 1;
        decoderOutputBufferArr[i] = decoderOutputBuffer;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void run() {
        do {
            try {
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        } while (decode());
    }

    protected abstract DecoderInputBuffer createInputBuffer();

    protected abstract DecoderOutputBuffer createOutputBuffer();

    protected abstract DecoderException createUnexpectedDecodeException(Throwable th);

    protected abstract DecoderException decode(DecoderInputBuffer decoderInputBuffer, DecoderOutputBuffer decoderOutputBuffer, boolean z);

    @Override // com.google.android.exoplayer2.decoder.Decoder
    public final DecoderInputBuffer dequeueInputBuffer() {
        DecoderInputBuffer decoderInputBuffer;
        synchronized (this.lock) {
            maybeThrowException();
            Assertions.checkState(this.dequeuedInputBuffer == null);
            int i = this.availableInputBufferCount;
            if (i == 0) {
                decoderInputBuffer = null;
            } else {
                DecoderInputBuffer[] decoderInputBufferArr = this.availableInputBuffers;
                int i2 = i - 1;
                this.availableInputBufferCount = i2;
                decoderInputBuffer = decoderInputBufferArr[i2];
            }
            this.dequeuedInputBuffer = decoderInputBuffer;
        }
        return decoderInputBuffer;
    }

    @Override // com.google.android.exoplayer2.decoder.Decoder
    public final DecoderOutputBuffer dequeueOutputBuffer() {
        synchronized (this.lock) {
            try {
                maybeThrowException();
                if (this.queuedOutputBuffers.isEmpty()) {
                    return null;
                }
                return this.queuedOutputBuffers.removeFirst();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.google.android.exoplayer2.decoder.Decoder
    public final void flush() {
        synchronized (this.lock) {
            try {
                this.flushed = true;
                this.skippedOutputBufferCount = 0;
                DecoderInputBuffer decoderInputBuffer = this.dequeuedInputBuffer;
                if (decoderInputBuffer != null) {
                    releaseInputBufferInternal(decoderInputBuffer);
                    this.dequeuedInputBuffer = null;
                }
                while (!this.queuedInputBuffers.isEmpty()) {
                    releaseInputBufferInternal(this.queuedInputBuffers.removeFirst());
                }
                while (!this.queuedOutputBuffers.isEmpty()) {
                    this.queuedOutputBuffers.removeFirst().release();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.google.android.exoplayer2.decoder.Decoder
    public final void queueInputBuffer(DecoderInputBuffer decoderInputBuffer) {
        synchronized (this.lock) {
            maybeThrowException();
            Assertions.checkArgument(decoderInputBuffer == this.dequeuedInputBuffer);
            this.queuedInputBuffers.addLast(decoderInputBuffer);
            maybeNotifyDecodeLoop();
            this.dequeuedInputBuffer = null;
        }
    }

    @Override // com.google.android.exoplayer2.decoder.Decoder
    public void release() {
        synchronized (this.lock) {
            this.released = true;
            this.lock.notify();
        }
        try {
            this.decodeThread.join();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void releaseOutputBuffer(DecoderOutputBuffer decoderOutputBuffer) {
        synchronized (this.lock) {
            releaseOutputBufferInternal(decoderOutputBuffer);
            maybeNotifyDecodeLoop();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setInitialInputBufferSize(int i) {
        Assertions.checkState(this.availableInputBufferCount == this.availableInputBuffers.length);
        for (DecoderInputBuffer decoderInputBuffer : this.availableInputBuffers) {
            decoderInputBuffer.ensureSpaceForWrite(i);
        }
    }
}
