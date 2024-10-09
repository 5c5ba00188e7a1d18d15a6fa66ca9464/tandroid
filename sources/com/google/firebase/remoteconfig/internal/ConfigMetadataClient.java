package com.google.firebase.remoteconfig.internal;

import android.content.SharedPreferences;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigInfo;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import java.util.Date;

/* loaded from: classes.dex */
public class ConfigMetadataClient {
    static final Date LAST_FETCH_TIME_NO_FETCH_YET = new Date(-1);
    static final Date NO_BACKOFF_TIME = new Date(-1);
    private final SharedPreferences frcMetadata;
    private final Object frcInfoLock = new Object();
    private final Object backoffMetadataLock = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class BackoffMetadata {
        private Date backoffEndTime;
        private int numFailedFetches;

        BackoffMetadata(int i, Date date) {
            this.numFailedFetches = i;
            this.backoffEndTime = date;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Date getBackoffEndTime() {
            return this.backoffEndTime;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getNumFailedFetches() {
            return this.numFailedFetches;
        }
    }

    public ConfigMetadataClient(SharedPreferences sharedPreferences) {
        this.frcMetadata = sharedPreferences;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BackoffMetadata getBackoffMetadata() {
        BackoffMetadata backoffMetadata;
        synchronized (this.backoffMetadataLock) {
            backoffMetadata = new BackoffMetadata(this.frcMetadata.getInt("num_failed_fetches", 0), new Date(this.frcMetadata.getLong("backoff_end_time_in_millis", -1L)));
        }
        return backoffMetadata;
    }

    public long getFetchTimeoutInSeconds() {
        return this.frcMetadata.getLong("fetch_timeout_in_seconds", 60L);
    }

    public FirebaseRemoteConfigInfo getInfo() {
        FirebaseRemoteConfigInfoImpl build;
        synchronized (this.frcInfoLock) {
            long j = this.frcMetadata.getLong("last_fetch_time_in_millis", -1L);
            int i = this.frcMetadata.getInt("last_fetch_status", 0);
            build = FirebaseRemoteConfigInfoImpl.newBuilder().withLastFetchStatus(i).withLastSuccessfulFetchTimeInMillis(j).withConfigSettings(new FirebaseRemoteConfigSettings.Builder().setFetchTimeoutInSeconds(this.frcMetadata.getLong("fetch_timeout_in_seconds", 60L)).setMinimumFetchIntervalInSeconds(this.frcMetadata.getLong("minimum_fetch_interval_in_seconds", ConfigFetchHandler.DEFAULT_MINIMUM_FETCH_INTERVAL_IN_SECONDS)).build()).build();
        }
        return build;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getLastFetchETag() {
        return this.frcMetadata.getString("last_fetch_etag", null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Date getLastSuccessfulFetchTime() {
        return new Date(this.frcMetadata.getLong("last_fetch_time_in_millis", -1L));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetBackoff() {
        setBackoffMetadata(0, NO_BACKOFF_TIME);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBackoffMetadata(int i, Date date) {
        synchronized (this.backoffMetadataLock) {
            this.frcMetadata.edit().putInt("num_failed_fetches", i).putLong("backoff_end_time_in_millis", date.getTime()).apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastFetchETag(String str) {
        synchronized (this.frcInfoLock) {
            this.frcMetadata.edit().putString("last_fetch_etag", str).apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateLastFetchAsFailed() {
        synchronized (this.frcInfoLock) {
            this.frcMetadata.edit().putInt("last_fetch_status", 1).apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateLastFetchAsSuccessfulAt(Date date) {
        synchronized (this.frcInfoLock) {
            this.frcMetadata.edit().putInt("last_fetch_status", -1).putLong("last_fetch_time_in_millis", date.getTime()).apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateLastFetchAsThrottled() {
        synchronized (this.frcInfoLock) {
            this.frcMetadata.edit().putInt("last_fetch_status", 2).apply();
        }
    }
}
