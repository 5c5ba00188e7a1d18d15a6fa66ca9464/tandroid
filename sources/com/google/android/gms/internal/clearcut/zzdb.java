package com.google.android.gms.internal.clearcut;
/* loaded from: classes.dex */
final class zzdb extends zzcy {
    private zzdb() {
        super();
    }

    private static zzcn zzc(Object obj, long j) {
        return (zzcn) zzfd.zzo(obj, j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzcy
    public final void zza(Object obj, long j) {
        zzc(obj, j).zzv();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzcy
    public final void zza(Object obj, Object obj2, long j) {
        zzcn zzc = zzc(obj, j);
        zzcn zzc2 = zzc(obj2, j);
        int size = zzc.size();
        int size2 = zzc2.size();
        if (size > 0 && size2 > 0) {
            if (!zzc.zzu()) {
                zzc = zzc.zzi(size2 + size);
            }
            zzc.addAll(zzc2);
        }
        if (size > 0) {
            zzc2 = zzc;
        }
        zzfd.zza(obj, j, zzc2);
    }
}
