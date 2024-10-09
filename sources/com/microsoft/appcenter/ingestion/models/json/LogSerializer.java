package com.microsoft.appcenter.ingestion.models.json;

import com.microsoft.appcenter.ingestion.models.Log;
import com.microsoft.appcenter.ingestion.models.LogContainer;
import java.util.Collection;

/* loaded from: classes.dex */
public interface LogSerializer {
    void addLogFactory(String str, LogFactory logFactory);

    Log deserializeLog(String str, String str2);

    String serializeContainer(LogContainer logContainer);

    String serializeLog(Log log);

    Collection toCommonSchemaLog(Log log);
}
