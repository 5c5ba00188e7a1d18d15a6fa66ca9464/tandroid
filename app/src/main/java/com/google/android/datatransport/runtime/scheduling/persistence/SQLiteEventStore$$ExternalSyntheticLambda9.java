package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* loaded from: classes.dex */
public final /* synthetic */ class SQLiteEventStore$$ExternalSyntheticLambda9 implements SQLiteEventStore.Function {
    public final /* synthetic */ SQLiteEventStore f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ SQLiteEventStore$$ExternalSyntheticLambda9(SQLiteEventStore sQLiteEventStore, String str, String str2) {
        this.f$0 = sQLiteEventStore;
        this.f$1 = str;
        this.f$2 = str2;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        Object lambda$recordFailure$4;
        lambda$recordFailure$4 = this.f$0.lambda$recordFailure$4(this.f$1, this.f$2, (SQLiteDatabase) obj);
        return lambda$recordFailure$4;
    }
}
