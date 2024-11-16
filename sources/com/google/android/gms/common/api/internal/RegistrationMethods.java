package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.internal.Preconditions;

/* loaded from: classes.dex */
public class RegistrationMethods {
    public final RegisterListenerMethod register;
    public final UnregisterListenerMethod zaa;
    public final Runnable zab;

    public static class Builder {
        private RemoteCall zaa;
        private RemoteCall zab;
        private ListenerHolder zad;
        private Feature[] zae;
        private int zag;
        private Runnable zac = new Runnable() { // from class: com.google.android.gms.common.api.internal.zacj
            @Override // java.lang.Runnable
            public final void run() {
            }
        };
        private boolean zaf = true;

        /* synthetic */ Builder(zacm zacmVar) {
        }

        public RegistrationMethods build() {
            Preconditions.checkArgument(this.zaa != null, "Must set register function");
            Preconditions.checkArgument(this.zab != null, "Must set unregister function");
            Preconditions.checkArgument(this.zad != null, "Must set holder");
            return new RegistrationMethods(new zack(this, this.zad, this.zae, this.zaf, this.zag), new zacl(this, (ListenerHolder.ListenerKey) Preconditions.checkNotNull(this.zad.getListenerKey(), "Key must not be null")), this.zac, null);
        }

        public Builder register(RemoteCall remoteCall) {
            this.zaa = remoteCall;
            return this;
        }

        public Builder setMethodKey(int i) {
            this.zag = i;
            return this;
        }

        public Builder unregister(RemoteCall remoteCall) {
            this.zab = remoteCall;
            return this;
        }

        public Builder withHolder(ListenerHolder listenerHolder) {
            this.zad = listenerHolder;
            return this;
        }
    }

    /* synthetic */ RegistrationMethods(RegisterListenerMethod registerListenerMethod, UnregisterListenerMethod unregisterListenerMethod, Runnable runnable, zacn zacnVar) {
        this.register = registerListenerMethod;
        this.zaa = unregisterListenerMethod;
        this.zab = runnable;
    }

    public static Builder builder() {
        return new Builder(null);
    }
}
