package com.microsoft.appcenter.analytics.channel;

import android.os.SystemClock;
import com.microsoft.appcenter.analytics.ingestion.models.StartSessionLog;
import com.microsoft.appcenter.channel.AbstractChannelListener;
import com.microsoft.appcenter.channel.Channel;
import com.microsoft.appcenter.ingestion.models.Log;
import com.microsoft.appcenter.ingestion.models.StartServiceLog;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.context.SessionContext;
import java.util.Date;
import java.util.UUID;
/* loaded from: classes.dex */
public class SessionTracker extends AbstractChannelListener {
    private final Channel mChannel;
    private final String mGroupName;
    private Long mLastPausedTime;
    private long mLastQueuedLogTime;
    private Long mLastResumedTime;
    private UUID mSid;

    public SessionTracker(Channel channel, String str) {
        this.mChannel = channel;
        this.mGroupName = str;
    }

    private boolean hasSessionTimedOut() {
        if (this.mLastPausedTime == null) {
            return false;
        }
        boolean z = SystemClock.elapsedRealtime() - this.mLastQueuedLogTime >= 20000;
        boolean z2 = this.mLastResumedTime.longValue() - Math.max(this.mLastPausedTime.longValue(), this.mLastQueuedLogTime) >= 20000;
        AppCenterLog.debug("AppCenterAnalytics", "noLogSentForLong=" + z + " wasBackgroundForLong=" + z2);
        return z && z2;
    }

    private void sendStartSessionIfNeeded() {
        if (this.mSid == null || hasSessionTimedOut()) {
            this.mSid = UUID.randomUUID();
            SessionContext.getInstance().addSession(this.mSid);
            this.mLastQueuedLogTime = SystemClock.elapsedRealtime();
            StartSessionLog startSessionLog = new StartSessionLog();
            startSessionLog.setSid(this.mSid);
            this.mChannel.enqueue(startSessionLog, this.mGroupName, 1);
        }
    }

    public void clearSessions() {
        SessionContext.getInstance().clearSessions();
    }

    public void onActivityPaused() {
        AppCenterLog.debug("AppCenterAnalytics", "onActivityPaused");
        this.mLastPausedTime = Long.valueOf(SystemClock.elapsedRealtime());
    }

    public void onActivityResumed() {
        AppCenterLog.debug("AppCenterAnalytics", "onActivityResumed");
        this.mLastResumedTime = Long.valueOf(SystemClock.elapsedRealtime());
        sendStartSessionIfNeeded();
    }

    @Override // com.microsoft.appcenter.channel.AbstractChannelListener, com.microsoft.appcenter.channel.Channel.Listener
    public void onPreparingLog(Log log, String str) {
        if ((log instanceof StartSessionLog) || (log instanceof StartServiceLog)) {
            return;
        }
        Date timestamp = log.getTimestamp();
        if (timestamp == null) {
            log.setSid(this.mSid);
            this.mLastQueuedLogTime = SystemClock.elapsedRealtime();
            return;
        }
        SessionContext.SessionInfo sessionAt = SessionContext.getInstance().getSessionAt(timestamp.getTime());
        if (sessionAt != null) {
            log.setSid(sessionAt.getSessionId());
        }
    }
}
