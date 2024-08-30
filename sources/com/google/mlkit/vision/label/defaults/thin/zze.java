package com.google.mlkit.vision.label.defaults.thin;

import android.content.Context;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.internal.mlkit_vision_label.zzoa;
import com.google.mlkit.common.sdkinternal.LazyInstanceMap;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
/* loaded from: classes.dex */
public final class zze extends LazyInstanceMap {
    private final MlKitContext zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zze(MlKitContext mlKitContext) {
        this.zza = mlKitContext;
    }

    @Override // com.google.mlkit.common.sdkinternal.LazyInstanceMap
    protected final /* bridge */ /* synthetic */ Object create(Object obj) {
        ImageLabelerOptions imageLabelerOptions = (ImageLabelerOptions) obj;
        Context applicationContext = this.zza.getApplicationContext();
        return new zzh(imageLabelerOptions, GoogleApiAvailabilityLight.getInstance().getApkVersion(applicationContext) >= 204700000 ? new zza(applicationContext, imageLabelerOptions) : new zzc(applicationContext, imageLabelerOptions), zzoa.zzb("play-services-mlkit-image-labeling"));
    }
}
