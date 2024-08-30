package com.google.android.gms.internal.mlkit_language_id;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.android.gms.common.internal.GmsLogger;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.Dependency;
import com.google.mlkit.common.model.RemoteModel;
import com.google.mlkit.common.sdkinternal.LazyInstanceMap;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;
/* loaded from: classes.dex */
public final class zzcz {
    private final zzcv zzc;
    private final SharedPrefManager zze;
    private static final GmsLogger zzb = new GmsLogger("ModelDownloadLogger", "");
    public static final Component zza = Component.builder(zza.class).add(Dependency.required(zzcv.class)).add(Dependency.required(SharedPrefManager.class)).factory(zzdb.zza).build();

    /* loaded from: classes.dex */
    public static class zza extends LazyInstanceMap {
        private final zzcv zza;
        private final SharedPrefManager zzb;

        private zza(zzcv zzcvVar, SharedPrefManager sharedPrefManager) {
            this.zza = zzcvVar;
            this.zzb = sharedPrefManager;
        }

        @Override // com.google.mlkit.common.sdkinternal.LazyInstanceMap
        protected /* synthetic */ Object create(Object obj) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
            return new zzcz(this.zza, this.zzb, null);
        }
    }

    private zzcz(zzcv zzcvVar, SharedPrefManager sharedPrefManager, RemoteModel remoteModel) {
        this.zzc = zzcvVar;
        this.zze = sharedPrefManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final /* synthetic */ zza zza(ComponentContainer componentContainer) {
        return new zza((zzcv) componentContainer.get(zzcv.class), (SharedPrefManager) componentContainer.get(SharedPrefManager.class));
    }
}
