package com.microsoft.appcenter.crashes.ingestion.models.json;

import com.microsoft.appcenter.crashes.ingestion.models.ManagedErrorLog;
import com.microsoft.appcenter.ingestion.models.json.AbstractLogFactory;
/* loaded from: classes.dex */
public class ManagedErrorLogFactory extends AbstractLogFactory {
    private static final ManagedErrorLogFactory sInstance = new ManagedErrorLogFactory();

    private ManagedErrorLogFactory() {
    }

    public static ManagedErrorLogFactory getInstance() {
        return sInstance;
    }

    @Override // com.microsoft.appcenter.ingestion.models.json.LogFactory
    /* renamed from: create */
    public ManagedErrorLog mo247create() {
        return new ManagedErrorLog();
    }
}
