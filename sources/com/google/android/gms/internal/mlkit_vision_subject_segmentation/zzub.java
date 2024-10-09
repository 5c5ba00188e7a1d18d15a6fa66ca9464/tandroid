package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.dynamic.IObjectWrapper;

/* loaded from: classes.dex */
public final class zzub extends zza implements IInterface {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzub(IBinder iBinder) {
        super(iBinder, "com.google.mlkit.vision.segmentation.subject.aidls.ISubjectSegmenter");
    }

    public final zzuh zzd(IObjectWrapper iObjectWrapper, zztz zztzVar) {
        Parcel zza = zza();
        zzc.zzb(zza, iObjectWrapper);
        zzc.zza(zza, zztzVar);
        Parcel zzb = zzb(3, zza);
        zzuh createFromParcel = zzb.readInt() == 0 ? null : zzuh.CREATOR.createFromParcel(zzb);
        zzb.recycle();
        return createFromParcel;
    }

    public final void zze() {
        zzc(1, zza());
    }

    public final void zzf() {
        zzc(2, zza());
    }
}
