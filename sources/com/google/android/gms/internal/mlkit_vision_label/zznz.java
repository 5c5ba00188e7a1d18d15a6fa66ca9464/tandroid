package com.google.android.gms.internal.mlkit_vision_label;

import com.google.mlkit.common.sdkinternal.LazyInstanceMap;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;

/* loaded from: classes.dex */
final class zznz extends LazyInstanceMap {
    /* synthetic */ zznz(zzny zznyVar) {
    }

    @Override // com.google.mlkit.common.sdkinternal.LazyInstanceMap
    protected final /* bridge */ /* synthetic */ Object create(Object obj) {
        zznh zznhVar = (zznh) obj;
        MlKitContext mlKitContext = MlKitContext.getInstance();
        return new zznp(mlKitContext.getApplicationContext(), (SharedPrefManager) mlKitContext.get(SharedPrefManager.class), new zzni(MlKitContext.getInstance().getApplicationContext(), zznhVar), zznhVar.zzb());
    }
}
