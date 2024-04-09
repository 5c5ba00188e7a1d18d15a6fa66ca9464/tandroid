package com.google.mlkit.vision.label.defaults.thin;

import android.content.Context;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.internal.mlkit_vision_label.zzoa;
import com.google.mlkit.common.sdkinternal.LazyInstanceMap;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zze extends LazyInstanceMap {
    private final MlKitContext zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zze(MlKitContext mlKitContext) {
        this.zza = mlKitContext;
    }

    @Override // com.google.mlkit.common.sdkinternal.LazyInstanceMap
    protected final /* bridge */ /* synthetic */ Object create(Object obj) {
        zzb zzcVar;
        ImageLabelerOptions imageLabelerOptions = (ImageLabelerOptions) obj;
        Context applicationContext = this.zza.getApplicationContext();
        if (GoogleApiAvailabilityLight.getInstance().getApkVersion(applicationContext) >= 204700000) {
            zzcVar = new zza(applicationContext, imageLabelerOptions);
        } else {
            zzcVar = new zzc(applicationContext, imageLabelerOptions);
        }
        return new zzh(imageLabelerOptions, zzcVar, zzoa.zzb("play-services-mlkit-image-labeling"));
    }
}
