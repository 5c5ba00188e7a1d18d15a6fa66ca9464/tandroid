package com.google.android.gms.internal.mlkit_language_id;

import java.util.Comparator;
/* loaded from: classes.dex */
final class zzdp implements Comparator {
    @Override // java.util.Comparator
    public final /* synthetic */ int compare(Object obj, Object obj2) {
        int zzb;
        int zzb2;
        zzdn zzdnVar = (zzdn) obj;
        zzdn zzdnVar2 = (zzdn) obj2;
        zzds zzdsVar = (zzds) zzdnVar.iterator();
        zzds zzdsVar2 = (zzds) zzdnVar2.iterator();
        while (zzdsVar.hasNext() && zzdsVar2.hasNext()) {
            zzb = zzdn.zzb(zzdsVar.zza());
            zzb2 = zzdn.zzb(zzdsVar2.zza());
            int compare = Integer.compare(zzb, zzb2);
            if (compare != 0) {
                return compare;
            }
        }
        return Integer.compare(zzdnVar.zza(), zzdnVar2.zza());
    }
}
