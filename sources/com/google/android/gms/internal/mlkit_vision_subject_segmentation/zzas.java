package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.Arrays;
import org.telegram.tgnet.ConnectionsManager;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
public final class zzas extends zzao {
    public zzas() {
        super(4);
    }

    public final zzav zzb() {
        this.zzc = true;
        return zzav.zzg(this.zza, this.zzb);
    }

    public final zzas zza(Object obj) {
        obj.getClass();
        int i = this.zzb;
        int i2 = i + 1;
        Object[] objArr = this.zza;
        int length = objArr.length;
        if (length < i2) {
            int i3 = length + (length >> 1) + 1;
            if (i3 < i2) {
                int highestOneBit = Integer.highestOneBit(i);
                i3 = highestOneBit + highestOneBit;
            }
            if (i3 < 0) {
                i3 = ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            this.zza = Arrays.copyOf(objArr, i3);
            this.zzc = false;
        } else if (this.zzc) {
            this.zza = (Object[]) objArr.clone();
            this.zzc = false;
        }
        Object[] objArr2 = this.zza;
        int i4 = this.zzb;
        this.zzb = i4 + 1;
        objArr2[i4] = obj;
        return this;
    }
}
