package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.upstream.DataReader;
/* loaded from: classes.dex */
public interface ExtractorInput extends DataReader {
    void advancePeekPosition(int i);

    boolean advancePeekPosition(int i, boolean z);

    long getLength();

    long getPeekPosition();

    long getPosition();

    int peek(byte[] bArr, int i, int i2);

    void peekFully(byte[] bArr, int i, int i2);

    boolean peekFully(byte[] bArr, int i, int i2, boolean z);

    @Override // com.google.android.exoplayer2.upstream.DataReader
    int read(byte[] bArr, int i, int i2);

    void readFully(byte[] bArr, int i, int i2);

    boolean readFully(byte[] bArr, int i, int i2, boolean z);

    void resetPeekPosition();

    void setRetryPosition(long j, Throwable th);

    int skip(int i);

    void skipFully(int i);
}
