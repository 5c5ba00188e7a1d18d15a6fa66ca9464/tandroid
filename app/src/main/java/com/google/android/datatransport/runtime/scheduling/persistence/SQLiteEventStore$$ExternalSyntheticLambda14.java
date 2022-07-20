package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.Cursor;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
import java.util.Map;
/* loaded from: classes.dex */
public final /* synthetic */ class SQLiteEventStore$$ExternalSyntheticLambda14 implements SQLiteEventStore.Function {
    public final /* synthetic */ Map f$0;

    public /* synthetic */ SQLiteEventStore$$ExternalSyntheticLambda14(Map map) {
        this.f$0 = map;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        Object lambda$loadMetadata$16;
        lambda$loadMetadata$16 = SQLiteEventStore.lambda$loadMetadata$16(this.f$0, (Cursor) obj);
        return lambda$loadMetadata$16;
    }
}
