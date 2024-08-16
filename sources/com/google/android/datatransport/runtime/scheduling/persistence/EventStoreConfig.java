package com.google.android.datatransport.runtime.scheduling.persistence;

import com.google.android.datatransport.runtime.scheduling.persistence.AutoValue_EventStoreConfig;
import com.google.auto.value.AutoValue;
import org.telegram.messenger.NotificationCenter;
/* JADX INFO: Access modifiers changed from: package-private */
@AutoValue
/* loaded from: classes.dex */
public abstract class EventStoreConfig {
    static final EventStoreConfig DEFAULT = builder().setMaxStorageSizeInBytes(10485760).setLoadBatchSize(NotificationCenter.storyQualityUpdate).setCriticalSectionEnterTimeoutMs(10000).setEventCleanUpAge(604800000).setMaxBlobByteSizePerRow(81920).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    @AutoValue.Builder
    /* loaded from: classes.dex */
    public static abstract class Builder {
        abstract EventStoreConfig build();

        abstract Builder setCriticalSectionEnterTimeoutMs(int i);

        abstract Builder setEventCleanUpAge(long j);

        abstract Builder setLoadBatchSize(int i);

        abstract Builder setMaxBlobByteSizePerRow(int i);

        abstract Builder setMaxStorageSizeInBytes(long j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int getCriticalSectionEnterTimeoutMs();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract long getEventCleanUpAge();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int getLoadBatchSize();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int getMaxBlobByteSizePerRow();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract long getMaxStorageSizeInBytes();

    static Builder builder() {
        return new AutoValue_EventStoreConfig.Builder();
    }
}
