package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Set;

/* loaded from: classes.dex */
public abstract class zzbh extends zzaz implements Set {
    private transient zzbe zza;

    zzbh() {
    }

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
        return zzcb.zza(this);
    }

    public final zzbe zzf() {
        zzbe zzbeVar = this.zza;
        if (zzbeVar != null) {
            return zzbeVar;
        }
        zzbe zzg = zzg();
        this.zza = zzg;
        return zzg;
    }

    zzbe zzg() {
        return zzbe.zzg(toArray());
    }
}
