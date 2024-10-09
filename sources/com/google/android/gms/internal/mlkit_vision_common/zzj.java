package com.google.android.gms.internal.mlkit_vision_common;

import java.util.Arrays;
import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes.dex */
abstract class zzj extends zzk {
    Object[] zza = new Object[4];
    int zzb = 0;
    boolean zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzj(int i) {
    }

    private final void zzb(int i) {
        Object[] objArr = this.zza;
        int length = objArr.length;
        if (length < i) {
            int i2 = length + (length >> 1) + 1;
            if (i2 < i) {
                int highestOneBit = Integer.highestOneBit(i - 1);
                i2 = highestOneBit + highestOneBit;
            }
            if (i2 < 0) {
                i2 = ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            this.zza = Arrays.copyOf(objArr, i2);
        } else if (!this.zzc) {
            return;
        } else {
            this.zza = (Object[]) objArr.clone();
        }
        this.zzc = false;
    }

    public final zzj zza(Object obj) {
        obj.getClass();
        zzb(this.zzb + 1);
        Object[] objArr = this.zza;
        int i = this.zzb;
        this.zzb = i + 1;
        objArr[i] = obj;
        return this;
    }
}
