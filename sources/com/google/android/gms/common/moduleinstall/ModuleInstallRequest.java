package com.google.android.gms.common.moduleinstall;

import com.google.android.gms.common.api.OptionalModuleApi;
import com.google.android.gms.common.internal.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public final class ModuleInstallRequest {
    private final List zaa;
    private final Executor zac;
    private final boolean zad;

    public static class Builder {
        private final List zaa = new ArrayList();
        private boolean zab = true;
        private Executor zad;

        public Builder addApi(OptionalModuleApi optionalModuleApi) {
            this.zaa.add(optionalModuleApi);
            return this;
        }

        public ModuleInstallRequest build() {
            return new ModuleInstallRequest(this.zaa, null, this.zad, this.zab, null);
        }
    }

    /* synthetic */ ModuleInstallRequest(List list, InstallStatusListener installStatusListener, Executor executor, boolean z, zac zacVar) {
        Preconditions.checkNotNull(list, "APIs must not be null.");
        Preconditions.checkArgument(!list.isEmpty(), "APIs must not be empty.");
        if (executor != null) {
            Preconditions.checkNotNull(installStatusListener, "Listener must not be null when listener executor is set.");
        }
        this.zaa = list;
        this.zac = executor;
        this.zad = z;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public List getApis() {
        return this.zaa;
    }

    public InstallStatusListener getListener() {
        return null;
    }

    public Executor getListenerExecutor() {
        return this.zac;
    }

    public final boolean zaa() {
        return this.zad;
    }
}
