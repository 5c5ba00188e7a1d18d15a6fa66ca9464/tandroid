package com.google.android.gms.internal.mlkit_language_id;
/* loaded from: classes.dex */
final class zzfk extends zzfj {
    private zzfk() {
        super();
    }

    private static zzew zzb(Object obj, long j) {
        return (zzew) zzhn.zzf(obj, j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzfj
    public final void zza(Object obj, long j) {
        zzb(obj, j).b_();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzfj
    public final void zza(Object obj, Object obj2, long j) {
        zzew zzb = zzb(obj, j);
        zzew zzb2 = zzb(obj2, j);
        int size = zzb.size();
        int size2 = zzb2.size();
        if (size > 0 && size2 > 0) {
            if (!zzb.zza()) {
                zzb = zzb.zzb(size2 + size);
            }
            zzb.addAll(zzb2);
        }
        if (size > 0) {
            zzb2 = zzb;
        }
        zzhn.zza(obj, j, zzb2);
    }
}
