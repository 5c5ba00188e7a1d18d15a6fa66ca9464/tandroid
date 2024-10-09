package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.Set;

/* loaded from: classes.dex */
public abstract class zzay extends zzaq implements Set {
    private transient zzav zza;

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
        return zzbs.zza(this);
    }

    public final zzav zzf() {
        zzav zzavVar = this.zza;
        if (zzavVar != null) {
            return zzavVar;
        }
        zzav zzg = zzg();
        this.zza = zzg;
        return zzg;
    }

    zzav zzg() {
        Object[] array = toArray();
        int i = zzav.$r8$clinit;
        return zzav.zzg(array, array.length);
    }
}
