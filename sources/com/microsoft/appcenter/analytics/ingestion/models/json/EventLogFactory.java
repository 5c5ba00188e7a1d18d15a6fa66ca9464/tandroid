package com.microsoft.appcenter.analytics.ingestion.models.json;

import com.microsoft.appcenter.analytics.ingestion.models.EventLog;
import com.microsoft.appcenter.analytics.ingestion.models.one.CommonSchemaEventLog;
import com.microsoft.appcenter.ingestion.models.Log;
import com.microsoft.appcenter.ingestion.models.json.AbstractLogFactory;
import com.microsoft.appcenter.ingestion.models.one.CommonSchemaDataUtils;
import com.microsoft.appcenter.ingestion.models.one.PartAUtils;
import java.util.Collection;
import java.util.LinkedList;
/* loaded from: classes.dex */
public class EventLogFactory extends AbstractLogFactory {
    @Override // com.microsoft.appcenter.ingestion.models.json.LogFactory
    public EventLog create() {
        return new EventLog();
    }

    @Override // com.microsoft.appcenter.ingestion.models.json.AbstractLogFactory, com.microsoft.appcenter.ingestion.models.json.LogFactory
    public Collection toCommonSchemaLogs(Log log) {
        LinkedList linkedList = new LinkedList();
        for (String str : log.getTransmissionTargetTokens()) {
            CommonSchemaEventLog commonSchemaEventLog = new CommonSchemaEventLog();
            EventLog eventLog = (EventLog) log;
            PartAUtils.setName(commonSchemaEventLog, eventLog.getName());
            PartAUtils.addPartAFromLog(log, commonSchemaEventLog, str);
            CommonSchemaDataUtils.addCommonSchemaData(eventLog.getTypedProperties(), commonSchemaEventLog);
            linkedList.add(commonSchemaEventLog);
            commonSchemaEventLog.setTag(log.getTag());
        }
        return linkedList;
    }
}
