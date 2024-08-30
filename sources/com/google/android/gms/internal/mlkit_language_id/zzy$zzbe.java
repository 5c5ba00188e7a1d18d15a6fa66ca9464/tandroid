package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
/* loaded from: classes.dex */
public final class zzy$zzbe extends zzeo implements zzgb {
    private static final zzy$zzbe zzl;
    private static volatile zzgj zzm;
    private int zzc;
    private zzy$zzaf zzd;
    private zzy$zzbi zze;
    private int zzf;
    private int zzg;
    private int zzh;
    private int zzi;
    private int zzj;
    private int zzk;

    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzbe.zzl);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    /* loaded from: classes.dex */
    public enum zzb implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3),
        zze(4),
        zzf(5),
        zzg(6),
        zzh(400),
        zzi(401),
        zzj(403),
        zzk(404),
        zzl(408),
        zzm(409),
        zzn(429),
        zzo(499),
        zzp(501),
        zzq(500),
        zzr(503),
        zzs(504),
        zzt(511),
        zzu(7),
        zzv(8),
        zzw(9),
        zzx(10),
        zzy(11),
        zzz(12),
        zzaa(13),
        zzab(14),
        zzac(15),
        zzad(16),
        zzae(17),
        zzaf(18),
        zzag(19),
        zzah(20);
        
        private static final zzes zzai = new zzby();
        private final int zzaj;

        zzb(int i) {
            this.zzaj = i;
        }

        public static zzev zzb() {
            return zzbz.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzb.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzaj + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzaj;
        }
    }

    static {
        zzy$zzbe zzy_zzbe = new zzy$zzbe();
        zzl = zzy_zzbe;
        zzeo.zza(zzy$zzbe.class, zzy_zzbe);
    }

    private zzy$zzbe() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v15, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzbe();
            case 2:
                return new zza(null);
            case 3:
                return zzeo.zza(zzl, "\u0001\b\u0000\u0001\u0001\b\b\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဉ\u0001\u0003င\u0002\u0004င\u0003\u0005င\u0004\u0006င\u0005\u0007ဌ\u0006\bင\u0007", new Object[]{"zzc", "zzd", "zze", "zzf", "zzg", "zzh", "zzi", "zzj", zzb.zzb(), "zzk"});
            case 4:
                return zzl;
            case 5:
                zzgj zzgjVar = zzm;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzbe.class) {
                        try {
                            zzgj zzgjVar3 = zzm;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzl);
                                zzm = zzaVar;
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
}
