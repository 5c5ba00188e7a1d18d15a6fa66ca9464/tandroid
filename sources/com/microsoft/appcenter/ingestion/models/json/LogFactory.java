package com.microsoft.appcenter.ingestion.models.json;

import com.microsoft.appcenter.ingestion.models.Log;
import java.util.Collection;

/* loaded from: classes.dex */
public interface LogFactory {
    Log create();

    Collection toCommonSchemaLogs(Log log);
}
