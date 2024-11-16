package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
public abstract class TaskApiCall {
    private final Feature[] zaa;
    private final boolean zab;
    private final int zac;

    public static class Builder {
        private RemoteCall zaa;
        private Feature[] zac;
        private boolean zab = true;
        private int zad = 0;

        /* synthetic */ Builder(zacw zacwVar) {
        }

        public TaskApiCall build() {
            Preconditions.checkArgument(this.zaa != null, "execute parameter required");
            return new zacv(this, this.zac, this.zab, this.zad);
        }

        public Builder run(RemoteCall remoteCall) {
            this.zaa = remoteCall;
            return this;
        }

        public Builder setAutoResolveMissingFeatures(boolean z) {
            this.zab = z;
            return this;
        }

        public Builder setFeatures(Feature... featureArr) {
            this.zac = featureArr;
            return this;
        }

        public Builder setMethodKey(int i) {
            this.zad = i;
            return this;
        }
    }

    protected TaskApiCall(Feature[] featureArr, boolean z, int i) {
        this.zaa = featureArr;
        boolean z2 = false;
        if (featureArr != null && z) {
            z2 = true;
        }
        this.zab = z2;
        this.zac = i;
    }

    public static Builder builder() {
        return new Builder(null);
    }

    protected abstract void doExecute(Api.AnyClient anyClient, TaskCompletionSource taskCompletionSource);

    public boolean shouldAutoResolveMissingFeatures() {
        return this.zab;
    }

    public final int zaa() {
        return this.zac;
    }

    public final Feature[] zab() {
        return this.zaa;
    }
}
