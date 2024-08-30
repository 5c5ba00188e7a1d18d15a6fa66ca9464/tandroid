package com.microsoft.appcenter.analytics;

import android.content.Context;
import com.microsoft.appcenter.channel.AbstractChannelListener;
import com.microsoft.appcenter.channel.Channel;
import com.microsoft.appcenter.ingestion.models.Log;
import com.microsoft.appcenter.ingestion.models.one.PartAUtils;
import com.microsoft.appcenter.utils.storage.SharedPreferencesManager;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class AnalyticsTransmissionTarget {
    private Channel mChannel;
    Context mContext;
    final AnalyticsTransmissionTarget mParentTarget;
    private final String mTransmissionTargetToken;
    private final Map mChildrenTargets = new HashMap();
    private final PropertyConfigurator mPropertyConfigurator = new PropertyConfigurator(this);

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnalyticsTransmissionTarget(String str, AnalyticsTransmissionTarget analyticsTransmissionTarget) {
        this.mTransmissionTargetToken = str;
        this.mParentTarget = analyticsTransmissionTarget;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addTicketToLog(Log log) {
    }

    private boolean areAncestorsEnabled() {
        for (AnalyticsTransmissionTarget analyticsTransmissionTarget = this.mParentTarget; analyticsTransmissionTarget != null; analyticsTransmissionTarget = analyticsTransmissionTarget.mParentTarget) {
            if (!analyticsTransmissionTarget.isEnabledInStorage()) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Channel.Listener getChannelListener() {
        return new AbstractChannelListener() { // from class: com.microsoft.appcenter.analytics.AnalyticsTransmissionTarget.7
            @Override // com.microsoft.appcenter.channel.AbstractChannelListener, com.microsoft.appcenter.channel.Channel.Listener
            public void onPreparingLog(Log log, String str) {
                AnalyticsTransmissionTarget.addTicketToLog(log);
            }
        };
    }

    private String getEnabledPreferenceKey() {
        return Analytics.getInstance().getEnabledPreferenceKeyPrefix() + PartAUtils.getTargetKey(this.mTransmissionTargetToken);
    }

    private boolean isEnabledInStorage() {
        return SharedPreferencesManager.getBoolean(getEnabledPreferenceKey(), true);
    }

    public PropertyConfigurator getPropertyConfigurator() {
        return this.mPropertyConfigurator;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getTransmissionTargetToken() {
        return this.mTransmissionTargetToken;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initInBackground(Context context, Channel channel) {
        this.mContext = context;
        this.mChannel = channel;
        channel.addListener(this.mPropertyConfigurator);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEnabled() {
        return areAncestorsEnabled() && isEnabledInStorage();
    }
}
