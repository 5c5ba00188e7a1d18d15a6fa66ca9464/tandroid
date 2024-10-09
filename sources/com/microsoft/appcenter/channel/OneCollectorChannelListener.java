package com.microsoft.appcenter.channel;

import com.microsoft.appcenter.channel.Channel;
import com.microsoft.appcenter.http.HttpClient;
import com.microsoft.appcenter.ingestion.Ingestion;
import com.microsoft.appcenter.ingestion.OneCollectorIngestion;
import com.microsoft.appcenter.ingestion.models.Log;
import com.microsoft.appcenter.ingestion.models.json.LogSerializer;
import com.microsoft.appcenter.ingestion.models.one.CommonSchemaLog;
import com.microsoft.appcenter.ingestion.models.one.SdkExtension;
import com.microsoft.appcenter.utils.AppCenterLog;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/* loaded from: classes.dex */
public class OneCollectorChannelListener extends AbstractChannelListener {
    private final Channel mChannel;
    private final Map mEpochsAndSeqsByIKey;
    private final Ingestion mIngestion;
    private final UUID mInstallId;
    private final LogSerializer mLogSerializer;

    /* loaded from: classes.dex */
    private static class EpochAndSeq {
        final String epoch;
        long seq;

        EpochAndSeq(String str) {
            this.epoch = str;
        }
    }

    public OneCollectorChannelListener(Channel channel, LogSerializer logSerializer, HttpClient httpClient, UUID uuid) {
        this(new OneCollectorIngestion(httpClient, logSerializer), channel, logSerializer, uuid);
    }

    OneCollectorChannelListener(OneCollectorIngestion oneCollectorIngestion, Channel channel, LogSerializer logSerializer, UUID uuid) {
        this.mEpochsAndSeqsByIKey = new HashMap();
        this.mChannel = channel;
        this.mLogSerializer = logSerializer;
        this.mInstallId = uuid;
        this.mIngestion = oneCollectorIngestion;
    }

    private static String getOneCollectorGroupName(String str) {
        return str + "/one";
    }

    private static boolean isOneCollectorCompatible(Log log) {
        return ((log instanceof CommonSchemaLog) || log.getTransmissionTargetTokens().isEmpty()) ? false : true;
    }

    private static boolean isOneCollectorGroup(String str) {
        return str.endsWith("/one");
    }

    @Override // com.microsoft.appcenter.channel.AbstractChannelListener, com.microsoft.appcenter.channel.Channel.Listener
    public void onClear(String str) {
        if (isOneCollectorGroup(str)) {
            return;
        }
        this.mChannel.clear(getOneCollectorGroupName(str));
    }

    @Override // com.microsoft.appcenter.channel.AbstractChannelListener, com.microsoft.appcenter.channel.Channel.Listener
    public void onGloballyEnabled(boolean z) {
        if (z) {
            return;
        }
        this.mEpochsAndSeqsByIKey.clear();
    }

    @Override // com.microsoft.appcenter.channel.AbstractChannelListener, com.microsoft.appcenter.channel.Channel.Listener
    public void onGroupAdded(String str, Channel.GroupListener groupListener, long j) {
        if (isOneCollectorGroup(str)) {
            return;
        }
        this.mChannel.addGroup(getOneCollectorGroupName(str), 50, j, 2, this.mIngestion, groupListener);
    }

    @Override // com.microsoft.appcenter.channel.AbstractChannelListener, com.microsoft.appcenter.channel.Channel.Listener
    public void onGroupRemoved(String str) {
        if (isOneCollectorGroup(str)) {
            return;
        }
        this.mChannel.removeGroup(getOneCollectorGroupName(str));
    }

    @Override // com.microsoft.appcenter.channel.AbstractChannelListener, com.microsoft.appcenter.channel.Channel.Listener
    public void onPreparedLog(Log log, String str, int i) {
        if (isOneCollectorCompatible(log)) {
            try {
                Collection<CommonSchemaLog> commonSchemaLog = this.mLogSerializer.toCommonSchemaLog(log);
                for (CommonSchemaLog commonSchemaLog2 : commonSchemaLog) {
                    commonSchemaLog2.setFlags(Long.valueOf(i));
                    EpochAndSeq epochAndSeq = (EpochAndSeq) this.mEpochsAndSeqsByIKey.get(commonSchemaLog2.getIKey());
                    if (epochAndSeq == null) {
                        epochAndSeq = new EpochAndSeq(UUID.randomUUID().toString());
                        this.mEpochsAndSeqsByIKey.put(commonSchemaLog2.getIKey(), epochAndSeq);
                    }
                    SdkExtension sdk = commonSchemaLog2.getExt().getSdk();
                    sdk.setEpoch(epochAndSeq.epoch);
                    long j = epochAndSeq.seq + 1;
                    epochAndSeq.seq = j;
                    sdk.setSeq(Long.valueOf(j));
                    sdk.setInstallId(this.mInstallId);
                }
                String oneCollectorGroupName = getOneCollectorGroupName(str);
                Iterator it = commonSchemaLog.iterator();
                while (it.hasNext()) {
                    this.mChannel.enqueue((CommonSchemaLog) it.next(), oneCollectorGroupName, i);
                }
            } catch (IllegalArgumentException e) {
                AppCenterLog.error("AppCenter", "Cannot send a log to one collector: " + e.getMessage());
            }
        }
    }

    public void setLogUrl(String str) {
        this.mIngestion.setLogUrl(str);
    }

    @Override // com.microsoft.appcenter.channel.AbstractChannelListener, com.microsoft.appcenter.channel.Channel.Listener
    public boolean shouldFilter(Log log) {
        return isOneCollectorCompatible(log);
    }
}
