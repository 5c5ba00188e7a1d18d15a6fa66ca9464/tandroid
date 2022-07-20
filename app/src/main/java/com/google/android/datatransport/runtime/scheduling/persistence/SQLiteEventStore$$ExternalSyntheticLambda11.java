package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.Cursor;
import com.google.android.datatransport.runtime.TransportContext;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
import java.util.List;
/* loaded from: classes.dex */
public final /* synthetic */ class SQLiteEventStore$$ExternalSyntheticLambda11 implements SQLiteEventStore.Function {
    public final /* synthetic */ SQLiteEventStore f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ TransportContext f$2;

    public /* synthetic */ SQLiteEventStore$$ExternalSyntheticLambda11(SQLiteEventStore sQLiteEventStore, List list, TransportContext transportContext) {
        this.f$0 = sQLiteEventStore;
        this.f$1 = list;
        this.f$2 = transportContext;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        Object lambda$loadEvents$14;
        lambda$loadEvents$14 = this.f$0.lambda$loadEvents$14(this.f$1, this.f$2, (Cursor) obj);
        return lambda$loadEvents$14;
    }
}
