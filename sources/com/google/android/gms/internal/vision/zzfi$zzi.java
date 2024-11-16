package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;

/* loaded from: classes.dex */
public final class zzfi$zzi extends zzjb implements zzkm {
    private static final zzfi$zzi zzg;
    private static volatile zzkx zzh;
    private int zzc;
    private zzfi$zzj zzd;
    private zzfi$zzl zze;
    private zzjl zzf = zzjb.zzo();

    public static final class zza extends zzjb.zzb implements zzkm {
        private zza() {
            super(zzfi$zzi.zzg);
        }

        /* synthetic */ zza(zzfk zzfkVar) {
            this();
        }

        public final zza zza(zzfi$zzj zzfi_zzj) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zzi) this.zza).zza(zzfi_zzj);
            return this;
        }

        public final zza zza(Iterable iterable) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zzi) this.zza).zza(iterable);
            return this;
        }
    }

    static {
        zzfi$zzi zzfi_zzi = new zzfi$zzi();
        zzg = zzfi_zzi;
        zzjb.zza(zzfi$zzi.class, zzfi_zzi);
    }

    private zzfi$zzi() {
    }

    public static zza zza() {
        return (zza) zzg.zzj();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(zzfi$zzj zzfi_zzj) {
        zzfi_zzj.getClass();
        this.zzd = zzfi_zzj;
        this.zzc |= 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(Iterable iterable) {
        zzc();
        zzhf.zza(iterable, this.zzf);
    }

    private final void zzc() {
        zzjl zzjlVar = this.zzf;
        if (zzjlVar.zza()) {
            return;
        }
        this.zzf = zzjb.zza(zzjlVar);
    }

    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.vision.zzjb$zza, com.google.android.gms.internal.vision.zzkx] */
    @Override // com.google.android.gms.internal.vision.zzjb
    protected final Object zza(int i, Object obj, Object obj2) {
        zzfk zzfkVar = null;
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zzi();
            case 2:
                return new zza(zzfkVar);
            case 3:
                return zzjb.zza(zzg, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0001\u0000\u0001ဉ\u0000\u0002ဉ\u0001\u0003\u001b", new Object[]{"zzc", "zzd", "zze", "zzf", zzfi$zzf.class});
            case 4:
                return zzg;
            case 5:
                zzkx zzkxVar = zzh;
                zzkx zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zzi.class) {
                        try {
                            zzkx zzkxVar3 = zzh;
                            zzkx zzkxVar4 = zzkxVar3;
                            if (zzkxVar3 == null) {
                                ?? zzaVar = new zzjb.zza(zzg);
                                zzh = zzaVar;
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
