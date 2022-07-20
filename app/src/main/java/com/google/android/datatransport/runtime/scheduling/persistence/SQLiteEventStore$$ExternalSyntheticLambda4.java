package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.Cursor;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* loaded from: classes.dex */
public final /* synthetic */ class SQLiteEventStore$$ExternalSyntheticLambda4 implements SQLiteEventStore.Function {
    public final /* synthetic */ SQLiteEventStore f$0;

    public /* synthetic */ SQLiteEventStore$$ExternalSyntheticLambda4(SQLiteEventStore sQLiteEventStore) {
        this.f$0 = sQLiteEventStore;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        Object lambda$recordFailure$3;
        lambda$recordFailure$3 = this.f$0.lambda$recordFailure$3((Cursor) obj);
        return lambda$recordFailure$3;
    }
}
