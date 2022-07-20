package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* loaded from: classes.dex */
public final /* synthetic */ class SQLiteEventStore$$ExternalSyntheticLambda5 implements SQLiteEventStore.Function {
    public final /* synthetic */ SQLiteEventStore f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ SQLiteEventStore$$ExternalSyntheticLambda5(SQLiteEventStore sQLiteEventStore, long j) {
        this.f$0 = sQLiteEventStore;
        this.f$1 = j;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        Integer lambda$cleanUp$12;
        lambda$cleanUp$12 = this.f$0.lambda$cleanUp$12(this.f$1, (SQLiteDatabase) obj);
        return lambda$cleanUp$12;
    }
}
