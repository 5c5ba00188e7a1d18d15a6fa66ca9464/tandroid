package com.google.android.exoplayer2.decoder;
/* loaded from: classes.dex */
public abstract class DecoderOutputBuffer extends Buffer {
    public int skippedOutputBufferCount;
    public long timeUs;

    /* loaded from: classes.dex */
    public interface Owner<S extends DecoderOutputBuffer> {
        void releaseOutputBuffer(S s);
    }

    public abstract void release();
}
