package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.datatransport.runtime.EventInternal;
import com.google.android.datatransport.runtime.TransportContext;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* loaded from: classes.dex */
public final /* synthetic */ class SQLiteEventStore$$ExternalSyntheticLambda6 implements SQLiteEventStore.Function {
    public final /* synthetic */ SQLiteEventStore f$0;
    public final /* synthetic */ EventInternal f$1;
    public final /* synthetic */ TransportContext f$2;

    public /* synthetic */ SQLiteEventStore$$ExternalSyntheticLambda6(SQLiteEventStore sQLiteEventStore, EventInternal eventInternal, TransportContext transportContext) {
        this.f$0 = sQLiteEventStore;
        this.f$1 = eventInternal;
        this.f$2 = transportContext;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        Long lambda$persist$1;
        lambda$persist$1 = this.f$0.lambda$persist$1(this.f$1, this.f$2, (SQLiteDatabase) obj);
        return lambda$persist$1;
    }
}
