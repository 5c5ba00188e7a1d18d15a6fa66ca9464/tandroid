package com.microsoft.appcenter.ingestion.models.json;

import com.microsoft.appcenter.ingestion.models.Log;
import com.microsoft.appcenter.ingestion.models.one.CommonSchemaLog;
import java.util.Collection;
/* loaded from: classes.dex */
public interface LogFactory {
    /* renamed from: create */
    Log mo247create();

    Collection<CommonSchemaLog> toCommonSchemaLogs(Log log);
}
