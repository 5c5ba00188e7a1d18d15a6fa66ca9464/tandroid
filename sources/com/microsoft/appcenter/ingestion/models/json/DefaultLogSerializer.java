package com.microsoft.appcenter.ingestion.models.json;

import com.microsoft.appcenter.ingestion.models.Log;
import com.microsoft.appcenter.ingestion.models.LogContainer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/* loaded from: classes.dex */
public class DefaultLogSerializer implements LogSerializer {
    private final Map mLogFactories = new HashMap();

    private Log readLog(JSONObject jSONObject, String str) {
        if (str == null) {
            str = jSONObject.getString("type");
        }
        LogFactory logFactory = (LogFactory) this.mLogFactories.get(str);
        if (logFactory != null) {
            Log create = logFactory.create();
            create.read(jSONObject);
            return create;
        }
        throw new JSONException("Unknown log type: " + str);
    }

    private JSONStringer writeLog(JSONStringer jSONStringer, Log log) {
        jSONStringer.object();
        log.write(jSONStringer);
        jSONStringer.endObject();
        return jSONStringer;
    }

    @Override // com.microsoft.appcenter.ingestion.models.json.LogSerializer
    public void addLogFactory(String str, LogFactory logFactory) {
        this.mLogFactories.put(str, logFactory);
    }

    @Override // com.microsoft.appcenter.ingestion.models.json.LogSerializer
    public Log deserializeLog(String str, String str2) {
        return readLog(new JSONObject(str), str2);
    }

    @Override // com.microsoft.appcenter.ingestion.models.json.LogSerializer
    public String serializeContainer(LogContainer logContainer) {
        JSONStringer jSONStringer = new JSONStringer();
        jSONStringer.object();
        jSONStringer.key("logs").array();
        Iterator it = logContainer.getLogs().iterator();
        while (it.hasNext()) {
            writeLog(jSONStringer, (Log) it.next());
        }
        jSONStringer.endArray();
        jSONStringer.endObject();
        return jSONStringer.toString();
    }

    @Override // com.microsoft.appcenter.ingestion.models.json.LogSerializer
    public String serializeLog(Log log) {
        return writeLog(new JSONStringer(), log).toString();
    }

    @Override // com.microsoft.appcenter.ingestion.models.json.LogSerializer
    public Collection toCommonSchemaLog(Log log) {
        return ((LogFactory) this.mLogFactories.get(log.getType())).toCommonSchemaLogs(log);
    }
}
