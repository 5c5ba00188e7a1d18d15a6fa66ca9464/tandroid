package com.google.android.datatransport.runtime.scheduling.persistence;

import com.google.android.datatransport.runtime.dagger.internal.Factory;
import com.google.android.datatransport.runtime.dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class EventStoreModule_DbNameFactory implements Factory<String> {
    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public String mo70get() {
        return dbName();
    }

    public static EventStoreModule_DbNameFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static String dbName() {
        return (String) Preconditions.checkNotNull(EventStoreModule.dbName(), "Cannot return null from a non-@Nullable @Provides method");
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final EventStoreModule_DbNameFactory INSTANCE = new EventStoreModule_DbNameFactory();
    }
}
