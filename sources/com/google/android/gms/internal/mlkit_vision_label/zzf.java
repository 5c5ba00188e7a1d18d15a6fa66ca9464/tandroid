package com.google.android.gms.internal.mlkit_vision_label;

import android.os.IBinder;
import android.os.IInterface;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public abstract class zzf extends zzb implements zzg {
    public static zzg zza(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.vision.label.internal.client.INativeImageLabelerCreator");
        return queryLocalInterface instanceof zzg ? (zzg) queryLocalInterface : new zze(iBinder);
    }
}
