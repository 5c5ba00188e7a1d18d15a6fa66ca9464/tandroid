package com.googlecode.mp4parser;

import java.io.Closeable;
/* loaded from: classes.dex */
public interface DataSource extends Closeable {
    long position();

    void position(long j);
}
