package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzaw extends zzeo implements zzgb {
    private static final zzy$zzaw zzj;
    private static volatile zzgj zzk;
    private int zzc;
    private int zzd;
    private boolean zze;
    private boolean zzf;
    private int zzg;
    private float zzh;
    private zzy$zzam zzi;

    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzaw.zzj);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    public enum zzb implements zzet {
        zza(0),
        zzb(1),
        zzc(2);

        private static final zzes zzd = new zzbq();
        private final int zze;

        zzb(int i) {
            this.zze = i;
        }

        public static zzev zzb() {
            return zzbr.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzb.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zze + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zze;
        }
    }

    static {
        zzy$zzaw zzy_zzaw = new zzy$zzaw();
        zzj = zzy_zzaw;
        zzeo.zza(zzy$zzaw.class, zzy_zzaw);
    }

    private zzy$zzaw() {
    }

    /* JADX WARN: Type inference failed for: r3v19, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzaw();
            case 2:
                return new zza(zzxVar);
            case 3:
                return zzeo.zza(zzj, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0000\u0000\u0001ဌ\u0000\u0002ဇ\u0001\u0003ဇ\u0002\u0004ဋ\u0003\u0005ခ\u0004\u0006ဉ\u0005", new Object[]{"zzc", "zzd", zzb.zzb(), "zze", "zzf", "zzg", "zzh", "zzi"});
            case 4:
                return zzj;
            case 5:
                zzgj zzgjVar = zzk;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzaw.class) {
                        try {
                            zzgj zzgjVar3 = zzk;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzj);
                                zzk = zzaVar;
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
