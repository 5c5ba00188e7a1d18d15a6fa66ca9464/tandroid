package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.datatransport.runtime.TransportContext;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
import java.util.List;
/* loaded from: classes.dex */
public final /* synthetic */ class SQLiteEventStore$$ExternalSyntheticLambda7 implements SQLiteEventStore.Function {
    public final /* synthetic */ SQLiteEventStore f$0;
    public final /* synthetic */ TransportContext f$1;

    public /* synthetic */ SQLiteEventStore$$ExternalSyntheticLambda7(SQLiteEventStore sQLiteEventStore, TransportContext transportContext) {
        this.f$0 = sQLiteEventStore;
        this.f$1 = transportContext;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        List lambda$loadBatch$8;
        lambda$loadBatch$8 = this.f$0.lambda$loadBatch$8(this.f$1, (SQLiteDatabase) obj);
        return lambda$loadBatch$8;
    }
}
