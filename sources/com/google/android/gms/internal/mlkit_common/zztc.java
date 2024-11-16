package com.google.android.gms.internal.mlkit_common;

import com.google.mlkit.common.sdkinternal.LazyInstanceMap;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;

/* loaded from: classes.dex */
final class zztc extends LazyInstanceMap {
    /* synthetic */ zztc(zztb zztbVar) {
    }

    @Override // com.google.mlkit.common.sdkinternal.LazyInstanceMap
    protected final /* bridge */ /* synthetic */ Object create(Object obj) {
        zzsm zzsmVar = (zzsm) obj;
        MlKitContext mlKitContext = MlKitContext.getInstance();
        return new zzss(mlKitContext.getApplicationContext(), (SharedPrefManager) mlKitContext.get(SharedPrefManager.class), new zzsn(MlKitContext.getInstance().getApplicationContext(), zzsmVar), zzsmVar.zzb());
    }
}
