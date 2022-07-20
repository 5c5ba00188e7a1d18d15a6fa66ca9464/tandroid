package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.datatransport.runtime.firebase.transport.LogEventDropped;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* loaded from: classes.dex */
public final /* synthetic */ class SQLiteEventStore$$ExternalSyntheticLambda13 implements SQLiteEventStore.Function {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ LogEventDropped.Reason f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ SQLiteEventStore$$ExternalSyntheticLambda13(String str, LogEventDropped.Reason reason, long j) {
        this.f$0 = str;
        this.f$1 = reason;
        this.f$2 = j;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        Object lambda$recordLogEventDropped$18;
        lambda$recordLogEventDropped$18 = SQLiteEventStore.lambda$recordLogEventDropped$18(this.f$0, this.f$1, this.f$2, (SQLiteDatabase) obj);
        return lambda$recordLogEventDropped$18;
    }
}
