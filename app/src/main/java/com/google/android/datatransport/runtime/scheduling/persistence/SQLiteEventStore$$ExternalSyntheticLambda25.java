package com.google.android.datatransport.runtime.scheduling.persistence;

import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* loaded from: classes.dex */
public final /* synthetic */ class SQLiteEventStore$$ExternalSyntheticLambda25 implements SQLiteEventStore.Producer {
    public final /* synthetic */ SchemaManager f$0;

    public /* synthetic */ SQLiteEventStore$$ExternalSyntheticLambda25(SchemaManager schemaManager) {
        this.f$0 = schemaManager;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Producer
    public final Object produce() {
        return this.f$0.getWritableDatabase();
    }
}
