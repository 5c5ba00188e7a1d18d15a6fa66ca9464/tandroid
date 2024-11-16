package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzid$zzf extends zzeo implements zzgb {
    private static final zzid$zzf zzo;
    private static volatile zzgj zzp;
    private int zzc;
    private zzid$zzb zzd;
    private zzid$zzi zze;
    private zzid$zzd zzf;
    private int zzg;
    private zzid$zzc zzh;
    private zzid$zzl zzi;
    private long zzj;
    private long zzk;
    private boolean zzl;
    private int zzm;
    private byte zzn = 2;

    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzid$zzf.zzo);
        }

        /* synthetic */ zza(zzic zzicVar) {
            this();
        }
    }

    static {
        zzid$zzf zzid_zzf = new zzid$zzf();
        zzo = zzid_zzf;
        zzeo.zza(zzid$zzf.class, zzid_zzf);
    }

    private zzid$zzf() {
    }

    /* JADX WARN: Type inference failed for: r3v21, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzic zzicVar = null;
        switch (zzic.zza[i - 1]) {
            case 1:
                return new zzid$zzf();
            case 2:
                return new zza(zzicVar);
            case 3:
                return zzeo.zza(zzo, "\u0001\n\u0000\u0001\u0001\n\n\u0000\u0000\u0001\u0001ဉ\u0000\u0002ဉ\u0001\u0003ဌ\u0003\u0004ဉ\u0004\u0005ᐉ\u0005\u0006ဂ\u0006\u0007ဂ\u0007\bဇ\b\tင\t\nဉ\u0002", new Object[]{"zzc", "zzd", "zze", "zzg", zzij.zzb(), "zzh", "zzi", "zzj", "zzk", "zzl", "zzm", "zzf"});
            case 4:
                return zzo;
            case 5:
                zzgj zzgjVar = zzp;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzid$zzf.class) {
                        try {
                            zzgj zzgjVar3 = zzp;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzo);
                                zzp = zzaVar;
                                zzgjVar4 = zzaVar;
                            }
                        } finally {
                        }
                    }
                }
                return zzgjVar2;
            case 6:
                return Byte.valueOf(this.zzn);
            case 7:
                this.zzn = (byte) (obj != null ? 1 : 0);
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
