package com.googlecode.mp4parser;

import java.io.Closeable;
import java.io.IOException;
/* loaded from: classes.dex */
public interface DataSource extends Closeable {
    long position() throws IOException;

    void position(long j) throws IOException;
}
