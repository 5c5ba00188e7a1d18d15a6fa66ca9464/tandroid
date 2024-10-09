package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzbg extends zzeo implements zzgb {
    private static final zzex zzf = new zzcc();
    private static final zzex zzh = new zzce();
    private static final zzex zzj = new zzcd();
    private static final zzy$zzbg zzl;
    private static volatile zzgj zzm;
    private int zzc;
    private long zzd;
    private zzeu zze = zzeo.zzk();
    private zzeu zzg = zzeo.zzk();
    private zzeu zzi = zzeo.zzk();
    private int zzk;

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

        private static final zzes zzm = new zzcg();
        private final int zzn;

        zza(int i) {
            this.zzn = i;
        }

        public static zzev zzb() {
            return zzcf.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzn + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzn;
        }
    }

    /* loaded from: classes.dex */
    public static final class zzb extends zzeo.zzb implements zzgb {
        private zzb() {
            super(zzy$zzbg.zzl);
        }

        /* synthetic */ zzb(zzx zzxVar) {
            this();
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.gms.internal.mlkit_language_id.zzex, com.google.android.gms.internal.mlkit_language_id.zzcc] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.gms.internal.mlkit_language_id.zzex, com.google.android.gms.internal.mlkit_language_id.zzce] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.google.android.gms.internal.mlkit_language_id.zzex, com.google.android.gms.internal.mlkit_language_id.zzcd] */
    static {
        zzy$zzbg zzy_zzbg = new zzy$zzbg();
        zzl = zzy_zzbg;
        zzeo.zza(zzy$zzbg.class, zzy_zzbg);
    }

    private zzy$zzbg() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r5v19, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzbg();
            case 2:
                return new zzb(zzxVar);
            case 3:
                return zzeo.zza(zzl, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0003\u0000\u0001ဃ\u0000\u0002\u001e\u0003\u001e\u0004\u001e\u0005င\u0001", new Object[]{"zzc", "zzd", "zze", zza.zzb(), "zzg", zza.zzb(), "zzi", zza.zzb(), "zzk"});
            case 4:
                return zzl;
            case 5:
                zzgj zzgjVar = zzm;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzbg.class) {
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
