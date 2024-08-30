package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;
/* loaded from: classes.dex */
public final class zzfi$zzf extends zzjb implements zzkm {
    private static final zzfi$zzf zzl;
    private static volatile zzkx zzm;
    private int zzc;
    private int zzg;
    private long zzi;
    private long zzj;
    private String zzd = "";
    private String zze = "";
    private zzjl zzf = zzjb.zzo();
    private String zzh = "";
    private zzjl zzk = zzjb.zzo();

    /* loaded from: classes.dex */
    public enum zza implements zzje {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3);
        
        private static final zzjh zze = new zzfp();
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
            return zzfo.zza;
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
            super(zzfi$zzf.zzl);
        }

        /* synthetic */ zzb(zzfk zzfkVar) {
            this();
        }

        public final zzb zza(long j) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zzf) this.zza).zza(j);
            return this;
        }

        public final zzb zza(Iterable iterable) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zzf) this.zza).zza(iterable);
            return this;
        }

        public final zzb zza(String str) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zzf) this.zza).zza(str);
            return this;
        }

        public final zzb zzb(long j) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            ((zzfi$zzf) this.zza).zzb(j);
            return this;
        }
    }

    static {
        zzfi$zzf zzfi_zzf = new zzfi$zzf();
        zzl = zzfi_zzf;
        zzjb.zza(zzfi$zzf.class, zzfi_zzf);
    }

    private zzfi$zzf() {
    }

    public static zzb zza() {
        return (zzb) zzl.zzj();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(long j) {
        this.zzc |= 16;
        this.zzi = j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(Iterable iterable) {
        zzjl zzjlVar = this.zzk;
        if (!zzjlVar.zza()) {
            this.zzk = zzjb.zza(zzjlVar);
        }
        zzhf.zza(iterable, this.zzk);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(String str) {
        str.getClass();
        this.zzc |= 1;
        this.zzd = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzb(long j) {
        this.zzc |= 32;
        this.zzj = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v19, types: [com.google.android.gms.internal.vision.zzkx, com.google.android.gms.internal.vision.zzjb$zza] */
    @Override // com.google.android.gms.internal.vision.zzjb
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zzf();
            case 2:
                return new zzb(null);
            case 3:
                return zzjb.zza(zzl, "\u0001\b\u0000\u0001\u0001\b\b\u0000\u0002\u0000\u0001ဈ\u0000\u0002ဈ\u0001\u0003\u001a\u0004ဌ\u0002\u0005ဈ\u0003\u0006ဂ\u0004\u0007ဂ\u0005\b\u001b", new Object[]{"zzc", "zzd", "zze", "zzf", "zzg", zza.zzb(), "zzh", "zzi", "zzj", "zzk", zzfi$zzn.class});
            case 4:
                return zzl;
            case 5:
                zzkx zzkxVar = zzm;
                zzkx zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zzf.class) {
                        try {
                            zzkx zzkxVar3 = zzm;
                            zzkx zzkxVar4 = zzkxVar3;
                            if (zzkxVar3 == null) {
                                ?? zzaVar = new zzjb.zza(zzl);
                                zzm = zzaVar;
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
