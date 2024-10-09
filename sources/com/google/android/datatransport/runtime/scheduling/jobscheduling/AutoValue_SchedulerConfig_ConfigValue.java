package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig;
import java.util.Set;

/* loaded from: classes.dex */
final class AutoValue_SchedulerConfig_ConfigValue extends SchedulerConfig.ConfigValue {
    private final long delta;
    private final Set flags;
    private final long maxAllowedDelay;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class Builder extends SchedulerConfig.ConfigValue.Builder {
        private Long delta;
        private Set flags;
        private Long maxAllowedDelay;

        @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.ConfigValue.Builder
        public SchedulerConfig.ConfigValue build() {
            String str = "";
            if (this.delta == null) {
                str = " delta";
            }
            if (this.maxAllowedDelay == null) {
                str = str + " maxAllowedDelay";
            }
            if (this.flags == null) {
                str = str + " flags";
            }
            if (str.isEmpty()) {
                return new AutoValue_SchedulerConfig_ConfigValue(this.delta.longValue(), this.maxAllowedDelay.longValue(), this.flags);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.ConfigValue.Builder
        public SchedulerConfig.ConfigValue.Builder setDelta(long j) {
            this.delta = Long.valueOf(j);
            return this;
        }

        @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.ConfigValue.Builder
        public SchedulerConfig.ConfigValue.Builder setFlags(Set set) {
            if (set == null) {
                throw new NullPointerException("Null flags");
            }
            this.flags = set;
            return this;
        }

        @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.ConfigValue.Builder
        public SchedulerConfig.ConfigValue.Builder setMaxAllowedDelay(long j) {
            this.maxAllowedDelay = Long.valueOf(j);
            return this;
        }
    }

    private AutoValue_SchedulerConfig_ConfigValue(long j, long j2, Set set) {
        this.delta = j;
        this.maxAllowedDelay = j2;
        this.flags = set;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SchedulerConfig.ConfigValue)) {
            return false;
        }
        SchedulerConfig.ConfigValue configValue = (SchedulerConfig.ConfigValue) obj;
        return this.delta == configValue.getDelta() && this.maxAllowedDelay == configValue.getMaxAllowedDelay() && this.flags.equals(configValue.getFlags());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.ConfigValue
    public long getDelta() {
        return this.delta;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.ConfigValue
    public Set getFlags() {
        return this.flags;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.ConfigValue
    public long getMaxAllowedDelay() {
        return this.maxAllowedDelay;
    }

    public int hashCode() {
        long j = this.delta;
        int i = (((int) (j ^ (j >>> 32))) ^ 1000003) * 1000003;
        long j2 = this.maxAllowedDelay;
        return this.flags.hashCode() ^ ((i ^ ((int) (j2 ^ (j2 >>> 32)))) * 1000003);
    }

    public String toString() {
        return "ConfigValue{delta=" + this.delta + ", maxAllowedDelay=" + this.maxAllowedDelay + ", flags=" + this.flags + "}";
    }
}
