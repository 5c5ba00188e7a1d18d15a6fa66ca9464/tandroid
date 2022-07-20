package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.datatransport.runtime.TransportContext;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* loaded from: classes.dex */
public final /* synthetic */ class SQLiteEventStore$$ExternalSyntheticLambda2 implements SQLiteEventStore.Function {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ TransportContext f$1;

    public /* synthetic */ SQLiteEventStore$$ExternalSyntheticLambda2(long j, TransportContext transportContext) {
        this.f$0 = j;
        this.f$1 = transportContext;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        Object lambda$recordNextCallTime$7;
        lambda$recordNextCallTime$7 = SQLiteEventStore.lambda$recordNextCallTime$7(this.f$0, this.f$1, (SQLiteDatabase) obj);
        return lambda$recordNextCallTime$7;
    }
}
