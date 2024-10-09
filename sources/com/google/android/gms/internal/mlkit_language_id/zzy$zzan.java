package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzan extends zzeo implements zzgb {
    private static final zzy$zzan zzh;
    private static volatile zzgj zzi;
    private int zzc;
    private int zzd;
    private float zze;
    private int zzf;
    private int zzg;

    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzan.zzh);
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
        zzg(6);

        private static final zzes zzh = new zzbh();
        private final int zzi;

        zzb(int i) {
            this.zzi = i;
        }

        public static zzev zzb() {
            return zzbi.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzb.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzi + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzi;
        }
    }

    static {
        zzy$zzan zzy_zzan = new zzy$zzan();
        zzh = zzy_zzan;
        zzeo.zza(zzy$zzan.class, zzy_zzan);
    }

    private zzy$zzan() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v17, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzan();
            case 2:
                return new zza(zzxVar);
            case 3:
                return zzeo.zza(zzh, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001ဌ\u0000\u0002ခ\u0001\u0003င\u0002\u0004ဋ\u0003", new Object[]{"zzc", "zzd", zzb.zzb(), "zze", "zzf", "zzg"});
            case 4:
                return zzh;
            case 5:
                zzgj zzgjVar = zzi;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzan.class) {
                        try {
                            zzgj zzgjVar3 = zzi;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzh);
                                zzi = zzaVar;
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
