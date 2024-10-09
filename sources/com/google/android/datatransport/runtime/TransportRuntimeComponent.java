package com.google.android.datatransport.runtime;

import android.content.Context;
import com.google.android.datatransport.runtime.scheduling.persistence.EventStore;
import java.io.Closeable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class TransportRuntimeComponent implements Closeable {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Builder {
        TransportRuntimeComponent build();

        Builder setApplicationContext(Context context);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        getEventStore().close();
    }

    abstract EventStore getEventStore();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract TransportRuntime getTransportRuntime();
}
