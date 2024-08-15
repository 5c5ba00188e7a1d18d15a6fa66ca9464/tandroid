package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final class zzy$zzbg extends zzeo<zzy$zzbg, zzb> implements zzgb {
    private static final zzex<Integer, zza> zzf = new zzcc();
    private static final zzex<Integer, zza> zzh = new zzce();
    private static final zzex<Integer, zza> zzj = new zzcd();
    private static final zzy$zzbg zzl;
    private static volatile zzgj<zzy$zzbg> zzm;
    private int zzc;
    private long zzd;
    private zzeu zze = zzeo.zzk();
    private zzeu zzg = zzeo.zzk();
    private zzeu zzi = zzeo.zzk();
    private int zzk;

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public enum zza implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3),
        zze(4),
        zzf(5),
        zzg(10),
        zzh(11),
        zzi(12),
        zzj(13),
        zzk(14),
        zzl(15);
        
        private static final zzes<zza> zzm = new zzcg();
        private final int zzn;

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzn;
        }

        public static zzev zzb() {
            return zzcf.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzn + " name=" + name() + '>';
        }

        zza(int i) {
            this.zzn = i;
        }
    }

    private zzy$zzbg() {
    }

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public static final class zzb extends zzeo.zzb<zzy$zzbg, zzb> implements zzgb {
        private zzb() {
            super(zzy$zzbg.zzl);
        }

        /* synthetic */ zzb(zzx zzxVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj<com.google.android.gms.internal.mlkit_language_id.zzy$zzbg>, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        zzgj<zzy$zzbg> zzgjVar;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzbg();
            case 2:
                return new zzb(null);
            case 3:
                return zzeo.zza(zzl, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0003\u0000\u0001ဃ\u0000\u0002\u001e\u0003\u001e\u0004\u001e\u0005င\u0001", new Object[]{"zzc", "zzd", "zze", zza.zzb(), "zzg", zza.zzb(), "zzi", zza.zzb(), "zzk"});
            case 4:
                return zzl;
            case 5:
                zzgj<zzy$zzbg> zzgjVar2 = zzm;
                zzgj<zzy$zzbg> zzgjVar3 = zzgjVar2;
                if (zzgjVar2 == null) {
                    synchronized (zzy$zzbg.class) {
                        zzgj<zzy$zzbg> zzgjVar4 = zzm;
                        zzgjVar = zzgjVar4;
                        if (zzgjVar4 == null) {
                            ?? zzaVar = new zzeo.zza(zzl);
                            zzm = zzaVar;
                            zzgjVar = zzaVar;
                        }
                    }
                    zzgjVar3 = zzgjVar;
                }
                return zzgjVar3;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.gms.internal.mlkit_language_id.zzcc, com.google.android.gms.internal.mlkit_language_id.zzex<java.lang.Integer, com.google.android.gms.internal.mlkit_language_id.zzy$zzbg$zza>] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.gms.internal.mlkit_language_id.zzce, com.google.android.gms.internal.mlkit_language_id.zzex<java.lang.Integer, com.google.android.gms.internal.mlkit_language_id.zzy$zzbg$zza>] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.google.android.gms.internal.mlkit_language_id.zzcd, com.google.android.gms.internal.mlkit_language_id.zzex<java.lang.Integer, com.google.android.gms.internal.mlkit_language_id.zzy$zzbg$zza>] */
    static {
        zzy$zzbg zzy_zzbg = new zzy$zzbg();
        zzl = zzy_zzbg;
        zzeo.zza(zzy$zzbg.class, zzy_zzbg);
    }
}
