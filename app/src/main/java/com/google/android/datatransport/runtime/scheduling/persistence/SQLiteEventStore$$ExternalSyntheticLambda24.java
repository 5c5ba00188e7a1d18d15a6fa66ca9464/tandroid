package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* loaded from: classes.dex */
public final /* synthetic */ class SQLiteEventStore$$ExternalSyntheticLambda24 implements SQLiteEventStore.Producer {
    public final /* synthetic */ SQLiteDatabase f$0;

    public /* synthetic */ SQLiteEventStore$$ExternalSyntheticLambda24(SQLiteDatabase sQLiteDatabase) {
        this.f$0 = sQLiteDatabase;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Producer
    public final Object produce() {
        Object beginTransaction;
        beginTransaction = this.f$0.beginTransaction();
        return beginTransaction;
    }
}
