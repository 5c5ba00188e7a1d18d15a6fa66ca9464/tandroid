package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final class zzy$zzbf extends zzeo<zzy$zzbf, zzb> implements zzgb {
    private static final zzy$zzbf zzf;
    private static volatile zzgj<zzy$zzbf> zzg;
    private int zzc;
    private int zzd;
    private int zze;

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public enum zza implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3);
        
        private static final zzes<zza> zze = new zzcb();
        private final int zzf;

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzf;
        }

        public static zzev zzb() {
            return zzca.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzf + " name=" + name() + '>';
        }

        zza(int i) {
            this.zzf = i;
        }
    }

    private zzy$zzbf() {
    }

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public static final class zzb extends zzeo.zzb<zzy$zzbf, zzb> implements zzgb {
        private zzb() {
            super(zzy$zzbf.zzf);
        }

        /* synthetic */ zzb(zzx zzxVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v15, types: [com.google.android.gms.internal.mlkit_language_id.zzgj<com.google.android.gms.internal.mlkit_language_id.zzy$zzbf>, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzbf();
            case 2:
                return new zzb(null);
            case 3:
                return zzeo.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဌ\u0000\u0002င\u0001", new Object[]{"zzc", "zzd", zza.zzb(), "zze"});
            case 4:
                return zzf;
            case 5:
                zzgj<zzy$zzbf> zzgjVar = zzg;
                zzgj<zzy$zzbf> zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzbf.class) {
                        try {
                            zzgj<zzy$zzbf> zzgjVar3 = zzg;
                            zzgj<zzy$zzbf> zzgjVar4 = zzgjVar3;
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
        zzy$zzbf zzy_zzbf = new zzy$zzbf();
        zzf = zzy_zzbf;
        zzeo.zza(zzy$zzbf.class, zzy_zzbf);
    }
}
