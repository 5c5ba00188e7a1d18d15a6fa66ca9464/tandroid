package com.microsoft.appcenter.persistence;

import com.microsoft.appcenter.ingestion.models.Log;
import com.microsoft.appcenter.ingestion.models.json.LogSerializer;
import java.io.Closeable;
import java.util.Collection;
import java.util.List;

/* loaded from: classes.dex */
public abstract class Persistence implements Closeable {
    private LogSerializer mLogSerializer;

    /* loaded from: classes.dex */
    public static class PersistenceException extends Exception {
        /* JADX INFO: Access modifiers changed from: package-private */
        public PersistenceException(String str) {
            super(str);
        }

        public PersistenceException(String str, Throwable th) {
            super(str, th);
        }
    }

    public abstract void clearPendingLogState();

    public abstract int countLogs(String str);

    public abstract void deleteLogs(String str);

    public abstract void deleteLogs(String str, String str2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public LogSerializer getLogSerializer() {
        LogSerializer logSerializer = this.mLogSerializer;
        if (logSerializer != null) {
            return logSerializer;
        }
        throw new IllegalStateException("logSerializer not configured");
    }

    public abstract String getLogs(String str, Collection collection, int i, List list);

    public abstract long putLog(Log log, String str, int i);

    public void setLogSerializer(LogSerializer logSerializer) {
        this.mLogSerializer = logSerializer;
    }

    public abstract boolean setMaxStorageSize(long j);
}
