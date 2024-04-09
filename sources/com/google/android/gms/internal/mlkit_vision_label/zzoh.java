package com.google.android.gms.internal.mlkit_vision_label;

import android.os.IBinder;
import android.os.IInterface;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public abstract class zzoh extends zzb implements zzoi {
    public static zzoi zza(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.mlkit.vision.label.aidls.IImageLabelerCreator");
        return queryLocalInterface instanceof zzoi ? (zzoi) queryLocalInterface : new zzog(iBinder);
    }
}
