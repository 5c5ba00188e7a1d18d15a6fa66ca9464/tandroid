package com.google.mlkit.common.model;

import com.google.firebase.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/* compiled from: com.google.mlkit:common@@18.10.0 */
/* loaded from: classes.dex */
public class RemoteModelManager {
    private final Map zza = new HashMap();

    /* compiled from: com.google.mlkit:common@@18.10.0 */
    /* loaded from: classes.dex */
    public static class RemoteModelManagerRegistration {
        private final Class zza;
        private final Provider zzb;

        public <RemoteT extends RemoteModel> RemoteModelManagerRegistration(Class<RemoteT> cls, Provider<Object> provider) {
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

    public RemoteModelManager(Set<RemoteModelManagerRegistration> set) {
        for (RemoteModelManagerRegistration remoteModelManagerRegistration : set) {
            this.zza.put(remoteModelManagerRegistration.zzb(), remoteModelManagerRegistration.zza());
        }
    }
}
