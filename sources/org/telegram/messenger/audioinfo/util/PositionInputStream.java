package org.telegram.messenger.audioinfo.util;

import java.io.FilterInputStream;
import java.io.InputStream;

/* loaded from: classes3.dex */
public class PositionInputStream extends FilterInputStream {
    private long position;
    private long positionMark;

    public PositionInputStream(InputStream inputStream) {
        this(inputStream, 0L);
    }

    public PositionInputStream(InputStream inputStream, long j) {
        super(inputStream);
        this.position = j;
    }

    public long getPosition() {
        return this.position;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(int i) {
        this.positionMark = this.position;
        super.mark(i);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() {
        int read = super.read();
        if (read >= 0) {
            this.position++;
        }
        return read;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public final int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) {
        long j = this.position;
        int read = super.read(bArr, i, i2);
        if (read > 0) {
            this.position = j + read;
        }
        return read;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() {
        super.reset();
        this.position = this.positionMark;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j) {
        long j2 = this.position;
        long skip = super.skip(j);
        this.position = j2 + skip;
        return skip;
    }
}
