package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.Cursor;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* loaded from: classes.dex */
public final /* synthetic */ class SQLiteEventStore$$ExternalSyntheticLambda3 implements SQLiteEventStore.Function {
    public final /* synthetic */ SQLiteEventStore f$0;

    public /* synthetic */ SQLiteEventStore$$ExternalSyntheticLambda3(SQLiteEventStore sQLiteEventStore) {
        this.f$0 = sQLiteEventStore;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        Object lambda$cleanUp$11;
        lambda$cleanUp$11 = this.f$0.lambda$cleanUp$11((Cursor) obj);
        return lambda$cleanUp$11;
    }
}
