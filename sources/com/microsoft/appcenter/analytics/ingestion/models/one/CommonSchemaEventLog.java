package com.microsoft.appcenter.analytics.ingestion.models.one;

import com.microsoft.appcenter.ingestion.models.one.CommonSchemaLog;
/* loaded from: classes.dex */
public class CommonSchemaEventLog extends CommonSchemaLog {
    @Override // com.microsoft.appcenter.ingestion.models.Log
    public String getType() {
        return "commonSchemaEvent";
    }
}
