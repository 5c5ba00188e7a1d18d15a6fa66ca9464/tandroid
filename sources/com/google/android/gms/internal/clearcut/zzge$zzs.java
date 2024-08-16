package com.google.android.gms.internal.clearcut;

import com.google.android.gms.internal.clearcut.zzcg;
/* loaded from: classes.dex */
public final class zzge$zzs extends zzcg<zzge$zzs, zza> implements zzdq {
    private static final zzge$zzs zzbfc;
    private static volatile zzdz<zzge$zzs> zzbg;
    private int zzbb;
    private int zzbfa = -1;
    private int zzbfb;

    /* loaded from: classes.dex */
    public static final class zza extends zzcg.zza<zzge$zzs, zza> implements zzdq {
        private zza() {
            super(zzge$zzs.zzbfc);
        }

        /* synthetic */ zza(zzgf zzgfVar) {
            this();
        }
    }

    /* loaded from: classes.dex */
    public enum zzb implements zzcj {
        zzbfd(0),
        zzbfe(1),
        zzbff(2),
        zzbfg(3),
        zzbfh(4),
        zzbfi(5),
        zzbfj(6),
        zzbfk(7),
        zzbfl(8),
        zzbfm(9),
        zzbfn(10),
        zzbfo(11),
        zzbfp(12),
        zzbfq(13),
        zzbfr(14),
        zzbfs(15),
        zzbft(16),
        zzbfu(17),
        zzbfv(18),
        zzbfw(19),
        zzbfx(100);
        
        private static final zzck<zzb> zzbq = new zzgo();
        private final int value;

        zzb(int i) {
            this.value = i;
        }

        public static zzb zzaz(int i) {
            if (i != 100) {
                switch (i) {
                    case 0:
                        return zzbfd;
                    case 1:
                        return zzbfe;
                    case 2:
                        return zzbff;
                    case 3:
                        return zzbfg;
                    case 4:
                        return zzbfh;
                    case 5:
                        return zzbfi;
                    case 6:
                        return zzbfj;
                    case 7:
                        return zzbfk;
                    case 8:
                        return zzbfl;
                    case 9:
                        return zzbfm;
                    case 10:
                        return zzbfn;
                    case 11:
                        return zzbfo;
                    case 12:
                        return zzbfp;
                    case 13:
                        return zzbfq;
                    case 14:
                        return zzbfr;
                    case 15:
                        return zzbfs;
                    case 16:
                        return zzbft;
                    case 17:
                        return zzbfu;
                    case 18:
                        return zzbfv;
                    case 19:
                        return zzbfw;
                    default:
                        return null;
                }
            }
            return zzbfx;
        }

        public static zzck<zzb> zzd() {
            return zzbq;
        }

        @Override // com.google.android.gms.internal.clearcut.zzcj
        public final int zzc() {
            return this.value;
        }
    }

    /* loaded from: classes.dex */
    public enum zzc implements zzcj {
        zzbfz(-1),
        zzbga(0),
        zzbgb(1),
        zzbgc(2),
        zzbgd(3),
        zzbge(4),
        zzbgf(5),
        zzbgg(6),
        zzbgh(7),
        zzbgi(8),
        zzbgj(9),
        zzbgk(10),
        zzbgl(11),
        zzbgm(12),
        zzbgn(13),
        zzbgo(14),
        zzbgp(15),
        zzbgq(16),
        zzbgr(17);
        
        private static final zzck<zzc> zzbq = new zzgp();
        private final int value;

        zzc(int i) {
            this.value = i;
        }

        public static zzc zzba(int i) {
            switch (i) {
                case -1:
                    return zzbfz;
                case 0:
                    return zzbga;
                case 1:
                    return zzbgb;
                case 2:
                    return zzbgc;
                case 3:
                    return zzbgd;
                case 4:
                    return zzbge;
                case 5:
                    return zzbgf;
                case 6:
                    return zzbgg;
                case 7:
                    return zzbgh;
                case 8:
                    return zzbgi;
                case 9:
                    return zzbgj;
                case 10:
                    return zzbgk;
                case 11:
                    return zzbgl;
                case 12:
                    return zzbgm;
                case 13:
                    return zzbgn;
                case 14:
                    return zzbgo;
                case 15:
                    return zzbgp;
                case 16:
                    return zzbgq;
                case 17:
                    return zzbgr;
                default:
                    return null;
            }
        }

        public static zzck<zzc> zzd() {
            return zzbq;
        }

        @Override // com.google.android.gms.internal.clearcut.zzcj
        public final int zzc() {
            return this.value;
        }
    }

    static {
        zzge$zzs zzge_zzs = new zzge$zzs();
        zzbfc = zzge_zzs;
        zzcg.zza(zzge$zzs.class, zzge_zzs);
    }

    private zzge$zzs() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r4v16, types: [com.google.android.gms.internal.clearcut.zzdz<com.google.android.gms.internal.clearcut.zzge$zzs>, com.google.android.gms.internal.clearcut.zzcg$zzb] */
    @Override // com.google.android.gms.internal.clearcut.zzcg
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzgf.zzba[i - 1]) {
            case 1:
                return new zzge$zzs();
            case 2:
                return new zza(null);
            case 3:
                return zzcg.zza(zzbfc, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0003\u0000\u0000\u0000\u0001\f\u0000\u0002\f\u0001", new Object[]{"zzbb", "zzbfa", zzc.zzd(), "zzbfb", zzb.zzd()});
            case 4:
                return zzbfc;
            case 5:
                zzdz<zzge$zzs> zzdzVar = zzbg;
                zzdz<zzge$zzs> zzdzVar2 = zzdzVar;
                if (zzdzVar == null) {
                    synchronized (zzge$zzs.class) {
                        try {
                            zzdz<zzge$zzs> zzdzVar3 = zzbg;
                            zzdz<zzge$zzs> zzdzVar4 = zzdzVar3;
                            if (zzdzVar3 == null) {
                                ?? zzbVar = new zzcg.zzb(zzbfc);
                                zzbg = zzbVar;
                                zzdzVar4 = zzbVar;
                            }
                        } finally {
                        }
                    }
                }
                return zzdzVar2;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
