package com.google.android.exoplayer2.decoder;

/* loaded from: classes.dex */
public interface Decoder {
    Object dequeueInputBuffer();

    Object dequeueOutputBuffer();

    void flush();

    String getName();

    void queueInputBuffer(Object obj);

    void release();
}
