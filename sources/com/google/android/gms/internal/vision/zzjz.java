package com.google.android.gms.internal.vision;
/* loaded from: classes.dex */
final class zzjz extends zzju {
    private zzjz() {
        super();
    }

    private static zzjl zzc(Object obj, long j) {
        return (zzjl) zzma.zzf(obj, j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.vision.zzju
    public final void zza(Object obj, Object obj2, long j) {
        zzjl zzc = zzc(obj, j);
        zzjl zzc2 = zzc(obj2, j);
        int size = zzc.size();
        int size2 = zzc2.size();
        if (size > 0 && size2 > 0) {
            if (!zzc.zza()) {
                zzc = zzc.zza(size2 + size);
            }
            zzc.addAll(zzc2);
        }
        if (size > 0) {
            zzc2 = zzc;
        }
        zzma.zza(obj, j, zzc2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.vision.zzju
    public final void zzb(Object obj, long j) {
        zzc(obj, j).zzb();
    }
}
