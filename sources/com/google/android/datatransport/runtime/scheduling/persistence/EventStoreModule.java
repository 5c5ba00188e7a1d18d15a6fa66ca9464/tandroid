package com.google.android.datatransport.runtime.scheduling.persistence;

import android.content.Context;
/* loaded from: classes.dex */
public abstract class EventStoreModule {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static EventStoreConfig storeConfig() {
        return EventStoreConfig.DEFAULT;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int schemaVersion() {
        return SchemaManager.SCHEMA_VERSION;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String dbName() {
        return "com.google.android.datatransport.events";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String packageName(Context context) {
        return context.getPackageName();
    }
}
