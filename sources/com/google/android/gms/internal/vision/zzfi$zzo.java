package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;

/* loaded from: classes.dex */
public final class zzfi$zzo extends zzjb implements zzkm {
    private static final zzfi$zzo zzi;
    private static volatile zzkx zzj;
    private int zzc;
    private zzfi$zze zzd;
    private zzfi$zzk zze;
    private zzfi$zzi zzf;
    private int zzg;
    private boolean zzh;

    /* loaded from: classes.dex */
    public static final class zza extends zzjb.zzb implements zzkm {
        private zza() {
            super(zzfi$zzo.zzi);
        }

        /* synthetic */ zza(zzfk zzfkVar) {
            this();
        }

        public final zza zza(zzfi$zzi zzfi_zzi) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zzo) this.zza).zza(zzfi_zzi);
            return this;
        }
    }

    static {
        zzfi$zzo zzfi_zzo = new zzfi$zzo();
        zzi = zzfi_zzo;
        zzjb.zza(zzfi$zzo.class, zzfi_zzo);
    }

    private zzfi$zzo() {
    }

    public static zza zza() {
        return (zza) zzi.zzj();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(zzfi$zzi zzfi_zzi) {
        zzfi_zzi.getClass();
        this.zzf = zzfi_zzi;
        this.zzc |= 4;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.vision.zzkx, com.google.android.gms.internal.vision.zzjb$zza] */
    @Override // com.google.android.gms.internal.vision.zzjb
    public final Object zza(int i, Object obj, Object obj2) {
        zzfk zzfkVar = null;
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zzo();
            case 2:
                return new zza(zzfkVar);
            case 3:
                return zzjb.zza(zzi, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဉ\u0001\u0003ဉ\u0002\u0004င\u0003\u0005ဇ\u0004", new Object[]{"zzc", "zzd", "zze", "zzf", "zzg", "zzh"});
            case 4:
                return zzi;
            case 5:
                zzkx zzkxVar = zzj;
                zzkx zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zzo.class) {
                        try {
                            zzkx zzkxVar3 = zzj;
                            zzkx zzkxVar4 = zzkxVar3;
                            if (zzkxVar3 == null) {
                                ?? zzaVar = new zzjb.zza(zzi);
                                zzj = zzaVar;
                                zzkxVar4 = zzaVar;
                            }
                        } finally {
                        }
                    }
                }
                return zzkxVar2;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
