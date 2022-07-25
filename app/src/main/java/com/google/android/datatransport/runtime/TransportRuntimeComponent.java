package com.google.android.datatransport.runtime;

import android.content.Context;
import com.google.android.datatransport.runtime.scheduling.persistence.EventStore;
import java.io.Closeable;
import java.io.IOException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class TransportRuntimeComponent implements Closeable {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Builder {
        TransportRuntimeComponent build();

        /* renamed from: setApplicationContext */
        Builder mo42setApplicationContext(Context context);
    }

    abstract EventStore getEventStore();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract TransportRuntime getTransportRuntime();

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        getEventStore().close();
    }
}
