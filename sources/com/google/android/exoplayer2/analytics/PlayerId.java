package com.google.android.exoplayer2.analytics;

import android.media.metrics.LogSessionId;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
/* loaded from: classes.dex */
public final class PlayerId {
    private final LogSessionIdApi31 logSessionIdApi31;

    static {
        if (Util.SDK_INT < 31) {
            new PlayerId();
        } else {
            new PlayerId(LogSessionIdApi31.UNSET);
        }
    }

    public PlayerId() {
        this((LogSessionIdApi31) null);
        Assertions.checkState(Util.SDK_INT < 31);
    }

    public PlayerId(LogSessionId logSessionId) {
        this(new LogSessionIdApi31(logSessionId));
    }

    private PlayerId(LogSessionIdApi31 logSessionIdApi31) {
        this.logSessionIdApi31 = logSessionIdApi31;
    }

    public LogSessionId getLogSessionId() {
        return ((LogSessionIdApi31) Assertions.checkNotNull(this.logSessionIdApi31)).logSessionId;
    }

    /* loaded from: classes.dex */
    private static final class LogSessionIdApi31 {
        public static final LogSessionIdApi31 UNSET = new LogSessionIdApi31(LogSessionId.LOG_SESSION_ID_NONE);
        public final LogSessionId logSessionId;

        public LogSessionIdApi31(LogSessionId logSessionId) {
            this.logSessionId = logSessionId;
        }
    }
}
