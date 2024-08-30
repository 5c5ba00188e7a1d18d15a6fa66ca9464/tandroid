package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;
/* loaded from: classes.dex */
public final class zzfi$zzj extends zzjb implements zzkm {
    private static final zzfi$zzj zzi;
    private static volatile zzkx zzj;
    private int zzc;
    private int zzd;
    private long zze;
    private long zzf;
    private long zzg;
    private long zzh;

    /* loaded from: classes.dex */
    public enum zza implements zzje {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3);
        
        private static final zzjh zze = new zzfx();
        private final int zzf;

        zza(int i) {
            this.zzf = i;
        }

        public static zza zza(int i) {
            if (i != 0) {
                if (i != 1) {
                    if (i != 2) {
                        if (i != 3) {
                            return null;
                        }
                        return zzd;
                    }
                    return zzc;
                }
                return zzb;
            }
            return zza;
        }

        public static zzjg zzb() {
            return zzfw.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzf + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.vision.zzje
        public final int zza() {
            return this.zzf;
        }
    }

    /* loaded from: classes.dex */
    public static final class zzb extends zzjb.zzb implements zzkm {
        private zzb() {
            super(zzfi$zzj.zzi);
        }

        /* synthetic */ zzb(zzfk zzfkVar) {
            this();
        }

        public final zzb zza(long j) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zzj) this.zza).zza(j);
            return this;
        }

        public final zzb zzb(long j) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zzj) this.zza).zzb(j);
            return this;
        }

        public final zzb zzc(long j) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zzj) this.zza).zzc(j);
            return this;
        }

        public final zzb zzd(long j) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zzj) this.zza).zzd(j);
            return this;
        }
    }

    static {
        zzfi$zzj zzfi_zzj = new zzfi$zzj();
        zzi = zzfi_zzj;
        zzjb.zza(zzfi$zzj.class, zzfi_zzj);
    }

    private zzfi$zzj() {
    }

    public static zzb zza() {
        return (zzb) zzi.zzj();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(long j) {
        this.zzc |= 2;
        this.zze = j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzb(long j) {
        this.zzc |= 4;
        this.zzf = j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzc(long j) {
        this.zzc |= 8;
        this.zzg = j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzd(long j) {
        this.zzc |= 16;
        this.zzh = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v18, types: [com.google.android.gms.internal.vision.zzkx, com.google.android.gms.internal.vision.zzjb$zza] */
    @Override // com.google.android.gms.internal.vision.zzjb
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zzj();
            case 2:
                return new zzb(null);
            case 3:
                return zzjb.zza(zzi, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0000\u0000\u0001ဌ\u0000\u0002ဂ\u0001\u0003ဂ\u0002\u0004ဂ\u0004\u0005ဂ\u0003", new Object[]{"zzc", "zzd", zza.zzb(), "zze", "zzf", "zzh", "zzg"});
            case 4:
                return zzi;
            case 5:
                zzkx zzkxVar = zzj;
                zzkx zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zzj.class) {
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
