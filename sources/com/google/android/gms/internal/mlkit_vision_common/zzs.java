package com.google.android.gms.internal.mlkit_vision_common;

import java.util.Set;
/* compiled from: com.google.mlkit:vision-common@@17.3.0 */
/* loaded from: classes.dex */
public abstract class zzs extends zzl implements Set {
    private transient zzp zza;

    @Override // java.util.Collection, java.util.Set
    public final boolean equals(Object obj) {
        if (obj == this || obj == this) {
            return true;
        }
        if (obj instanceof Set) {
            Set set = (Set) obj;
            try {
                if (size() == set.size()) {
                    if (containsAll(set)) {
                        return true;
                    }
                }
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    @Override // java.util.Collection, java.util.Set
    public final int hashCode() {
        return zzaa.zza(this);
    }

    public final zzp zzf() {
        zzp zzpVar = this.zza;
        if (zzpVar == null) {
            zzp zzg = zzg();
            this.zza = zzg;
            return zzg;
        }
        return zzpVar;
    }

    zzp zzg() {
        return zzp.zzg(toArray());
    }
}
