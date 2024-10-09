package com.google.android.gms.internal.mlkit_language_id;

/* loaded from: classes.dex */
public enum zzij implements zzet {
    zza(0),
    zzb(1),
    zzc(2);

    private static final zzes zzd = new zzes() { // from class: com.google.android.gms.internal.mlkit_language_id.zzim
    };
    private final int zze;

    zzij(int i) {
        this.zze = i;
    }

    public static zzev zzb() {
        return zzil.zza;
    }

    @Override // java.lang.Enum
    public final String toString() {
        return "<" + zzij.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zze + " name=" + name() + '>';
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzet
    public final int zza() {
        return this.zze;
    }
}
