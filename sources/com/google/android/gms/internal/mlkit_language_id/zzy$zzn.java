package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final class zzy$zzn extends zzeo<zzy$zzn, zzb> implements zzgb {
    private static final zzy$zzn zzf;
    private static volatile zzgj<zzy$zzn> zzg;
    private int zzc;
    private int zzd;
    private int zze;

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public enum zza implements zzet {
        zza(0),
        zzb(1),
        zzc(2);
        
        private static final zzes<zza> zzd = new zzad();
        private final int zze;

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zze;
        }

        public static zzev zzb() {
            return zzac.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zze + " name=" + name() + '>';
        }

        zza(int i) {
            this.zze = i;
        }
    }

    private zzy$zzn() {
    }

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public static final class zzb extends zzeo.zzb<zzy$zzn, zzb> implements zzgb {
        private zzb() {
            super(zzy$zzn.zzf);
        }

        /* synthetic */ zzb(zzx zzxVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj<com.google.android.gms.internal.mlkit_language_id.zzy$zzn>, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzn();
            case 2:
                return new zzb(null);
            case 3:
                return zzeo.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001င\u0000\u0002ဌ\u0001", new Object[]{"zzc", "zzd", "zze", zza.zzb()});
            case 4:
                return zzf;
            case 5:
                zzgj<zzy$zzn> zzgjVar = zzg;
                zzgj<zzy$zzn> zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzn.class) {
                        try {
                            zzgj<zzy$zzn> zzgjVar3 = zzg;
                            zzgj<zzy$zzn> zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzf);
                                zzg = zzaVar;
                                zzgjVar4 = zzaVar;
                            }
                        } finally {
                        }
                    }
                }
                return zzgjVar2;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }

    static {
        zzy$zzn zzy_zzn = new zzy$zzn();
        zzf = zzy_zzn;
        zzeo.zza(zzy$zzn.class, zzy_zzn);
    }
}
