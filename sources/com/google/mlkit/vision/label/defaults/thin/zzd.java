package com.google.mlkit.vision.label.defaults.thin;

import com.google.android.gms.common.Feature;
import com.google.android.gms.internal.mlkit_vision_label.zzkd;
import com.google.android.gms.internal.mlkit_vision_label.zzkf;
import com.google.android.gms.internal.mlkit_vision_label.zzkg;
import com.google.android.gms.internal.mlkit_vision_label.zznp;
import com.google.android.gms.internal.mlkit_vision_label.zzns;
import com.google.android.gms.internal.mlkit_vision_label.zzoa;
import com.google.mlkit.common.sdkinternal.ExecutorSelector;
import com.google.mlkit.common.sdkinternal.MLTask;
import com.google.mlkit.vision.common.internal.MultiFlavorDetectorCreator;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.label.internal.ImageLabelerImpl;

/* loaded from: classes.dex */
final class zzd implements MultiFlavorDetectorCreator.DetectorCreator {
    private final zze zza;
    private final ExecutorSelector zzb;
    private final zznp zzc;

    zzd(zze zzeVar, ExecutorSelector executorSelector) {
        zznp zzb = zzoa.zzb("play-services-mlkit-image-labeling");
        this.zza = zzeVar;
        this.zzb = executorSelector;
        this.zzc = zzb;
    }

    @Override // com.google.mlkit.vision.common.internal.MultiFlavorDetectorCreator.DetectorCreator
    public final /* bridge */ /* synthetic */ MultiFlavorDetectorCreator.MultiFlavorDetector create(MultiFlavorDetectorCreator.DetectorOptions detectorOptions) {
        ImageLabelerOptions imageLabelerOptions = (ImageLabelerOptions) detectorOptions;
        zznp zznpVar = this.zzc;
        zzkg zzkgVar = new zzkg();
        zzkgVar.zze(zzkd.zzb);
        zznpVar.zzd(zzns.zzg(zzkgVar, 1), zzkf.zzo);
        return ImageLabelerImpl.newInstance((MLTask) this.zza.get(imageLabelerOptions), this.zzb.getExecutorToUse(imageLabelerOptions.getExecutor()), new Feature("vision.ica", 1L));
    }
}
