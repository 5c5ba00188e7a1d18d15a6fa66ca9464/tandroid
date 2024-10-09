package com.google.android.gms.internal.mlkit_vision_label;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzbz extends zzbg {
    static final zzbg zza = new zzbz(null, new Object[0], 0);
    final transient Object[] zzb;
    private final transient int zzc;

    private zzbz(Object obj, Object[] objArr, int i) {
        this.zzb = objArr;
        this.zzc = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzbz zzg(int i, Object[] objArr, zzbf zzbfVar) {
        Object obj = objArr[0];
        obj.getClass();
        Object obj2 = objArr[1];
        obj2.getClass();
        zzal.zzb(obj, obj2);
        return new zzbz(null, objArr, 1);
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x001f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0020 A[RETURN] */
    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbg, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object get(Object obj) {
        Object obj2;
        Object[] objArr = this.zzb;
        int i = this.zzc;
        if (obj != null && i == 1) {
            Object obj3 = objArr[0];
            obj3.getClass();
            if (obj3.equals(obj)) {
                obj2 = objArr[1];
                obj2.getClass();
                if (obj2 != null) {
                    return null;
                }
                return obj2;
            }
        }
        obj2 = null;
        if (obj2 != null) {
        }
    }

    @Override // java.util.Map
    public final int size() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbg
    final zzaz zza() {
        return new zzby(this.zzb, 1, this.zzc);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbg
    final zzbh zzd() {
        return new zzbw(this, this.zzb, 0, this.zzc);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbg
    final zzbh zze() {
        return new zzbx(this, new zzby(this.zzb, 0, this.zzc));
    }
}
