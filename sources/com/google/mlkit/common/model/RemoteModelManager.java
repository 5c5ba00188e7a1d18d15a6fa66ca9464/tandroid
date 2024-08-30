package com.google.mlkit.common.model;

import com.google.firebase.inject.Provider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class RemoteModelManager {
    private final Map zza = new HashMap();

    /* loaded from: classes.dex */
    public static class RemoteModelManagerRegistration {
        private final Class zza;
        private final Provider zzb;

        public RemoteModelManagerRegistration(Class cls, Provider provider) {
            this.zza = cls;
            this.zzb = provider;
        }

        final Provider zza() {
            return this.zzb;
        }

        final Class zzb() {
            return this.zza;
        }
    }

    public RemoteModelManager(Set set) {
        Iterator it = set.iterator();
        while (it.hasNext()) {
            RemoteModelManagerRegistration remoteModelManagerRegistration = (RemoteModelManagerRegistration) it.next();
            this.zza.put(remoteModelManagerRegistration.zzb(), remoteModelManagerRegistration.zza());
        }
    }
}
