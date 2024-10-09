package com.microsoft.appcenter.analytics.ingestion.models;

import com.microsoft.appcenter.ingestion.models.AbstractLog;

/* loaded from: classes.dex */
public class StartSessionLog extends AbstractLog {
    @Override // com.microsoft.appcenter.ingestion.models.Log
    public String getType() {
        return "startSession";
    }
}
