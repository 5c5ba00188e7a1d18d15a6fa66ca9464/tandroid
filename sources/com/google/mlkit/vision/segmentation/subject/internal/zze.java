package com.google.mlkit.vision.segmentation.subject.internal;

import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztl;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztn;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztw;
import com.google.mlkit.common.sdkinternal.LazyInstanceMap;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions;
/* loaded from: classes.dex */
public final class zze extends LazyInstanceMap {
    private final MlKitContext zza;

    public zze(MlKitContext mlKitContext) {
        this.zza = mlKitContext;
    }

    @Override // com.google.mlkit.common.sdkinternal.LazyInstanceMap
    protected final /* bridge */ /* synthetic */ Object create(Object obj) {
        MlKitContext mlKitContext = this.zza;
        zztl zzb = zztw.zzb("subject-segmentation");
        zztn zza = zztn.zza(mlKitContext.getApplicationContext());
        return new zzj(this.zza, (SubjectSegmenterOptions) obj, zzb, zza);
    }
}
