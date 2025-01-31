package com.google.android.recaptcha.internal;

import java.util.List;
import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes.dex */
final class zzhd implements zzkq {
    private final zzhc zza;
    private int zzb;
    private int zzc;
    private int zzd = 0;

    private zzhd(zzhc zzhcVar) {
        byte[] bArr = zzjc.zzd;
        this.zza = zzhcVar;
        zzhcVar.zzc = this;
    }

    private final void zzP(Object obj, zzkr zzkrVar, zzie zzieVar) {
        int i = this.zzc;
        this.zzc = ((this.zzb >>> 3) << 3) | 4;
        try {
            zzkrVar.zzh(obj, this, zzieVar);
            if (this.zzb == this.zzc) {
            } else {
                throw zzje.zzg();
            }
        } finally {
            this.zzc = i;
        }
    }

    private final void zzQ(Object obj, zzkr zzkrVar, zzie zzieVar) {
        zzhc zzhcVar = this.zza;
        int zzn = zzhcVar.zzn();
        if (zzhcVar.zza >= zzhcVar.zzb) {
            throw new zzje("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
        }
        int zze = this.zza.zze(zzn);
        this.zza.zza++;
        zzkrVar.zzh(obj, this, zzieVar);
        this.zza.zzz(0);
        r4.zza--;
        this.zza.zzA(zze);
    }

    private final void zzR(int i) {
        if (this.zza.zzd() != i) {
            throw zzje.zzj();
        }
    }

    private final void zzS(int i) {
        if ((this.zzb & 7) != i) {
            throw zzje.zza();
        }
    }

    private static final void zzT(int i) {
        if ((i & 3) != 0) {
            throw zzje.zzg();
        }
    }

    private static final void zzU(int i) {
        if ((i & 7) != 0) {
            throw zzje.zzg();
        }
    }

    public static zzhd zzq(zzhc zzhcVar) {
        zzhd zzhdVar = zzhcVar.zzc;
        return zzhdVar != null ? zzhdVar : new zzhd(zzhcVar);
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzA(List list) {
        int zzm;
        int i;
        if (list instanceof zzjt) {
            zzjt zzjtVar = (zzjt) list;
            int i2 = this.zzb & 7;
            if (i2 != 1) {
                if (i2 != 2) {
                    throw zzje.zza();
                }
                int zzn = this.zza.zzn();
                zzU(zzn);
                int zzd = this.zza.zzd() + zzn;
                do {
                    zzjtVar.zzg(this.zza.zzo());
                } while (this.zza.zzd() < zzd);
                return;
            }
            do {
                zzjtVar.zzg(this.zza.zzo());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 != 1) {
                if (i3 != 2) {
                    throw zzje.zza();
                }
                int zzn2 = this.zza.zzn();
                zzU(zzn2);
                int zzd2 = this.zza.zzd() + zzn2;
                do {
                    list.add(Long.valueOf(this.zza.zzo()));
                } while (this.zza.zzd() < zzd2);
                return;
            }
            do {
                list.add(Long.valueOf(this.zza.zzo()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzB(List list) {
        int zzm;
        int i;
        if (list instanceof zzil) {
            zzil zzilVar = (zzil) list;
            int i2 = this.zzb & 7;
            if (i2 == 2) {
                int zzn = this.zza.zzn();
                zzT(zzn);
                int zzd = this.zza.zzd() + zzn;
                do {
                    zzilVar.zze(this.zza.zzc());
                } while (this.zza.zzd() < zzd);
                return;
            }
            if (i2 != 5) {
                throw zzje.zza();
            }
            do {
                zzilVar.zze(this.zza.zzc());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 == 2) {
                int zzn2 = this.zza.zzn();
                zzT(zzn2);
                int zzd2 = this.zza.zzd() + zzn2;
                do {
                    list.add(Float.valueOf(this.zza.zzc()));
                } while (this.zza.zzd() < zzd2);
                return;
            }
            if (i3 != 5) {
                throw zzje.zza();
            }
            do {
                list.add(Float.valueOf(this.zza.zzc()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    @Deprecated
    public final void zzC(List list, zzkr zzkrVar, zzie zzieVar) {
        int zzm;
        int i = this.zzb;
        if ((i & 7) != 3) {
            throw zzje.zza();
        }
        do {
            Object zze = zzkrVar.zze();
            zzP(zze, zzkrVar, zzieVar);
            zzkrVar.zzf(zze);
            list.add(zze);
            if (this.zza.zzC() || this.zzd != 0) {
                return;
            } else {
                zzm = this.zza.zzm();
            }
        } while (zzm == i);
        this.zzd = zzm;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzD(List list) {
        int zzm;
        int i;
        if (list instanceof zziu) {
            zziu zziuVar = (zziu) list;
            int i2 = this.zzb & 7;
            if (i2 != 0) {
                if (i2 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar = this.zza;
                int zzd = zzhcVar.zzd() + zzhcVar.zzn();
                do {
                    zziuVar.zzg(this.zza.zzh());
                } while (this.zza.zzd() < zzd);
                zzR(zzd);
                return;
            }
            do {
                zziuVar.zzg(this.zza.zzh());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 != 0) {
                if (i3 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar2 = this.zza;
                int zzd2 = zzhcVar2.zzd() + zzhcVar2.zzn();
                do {
                    list.add(Integer.valueOf(this.zza.zzh()));
                } while (this.zza.zzd() < zzd2);
                zzR(zzd2);
                return;
            }
            do {
                list.add(Integer.valueOf(this.zza.zzh()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzE(List list) {
        int zzm;
        int i;
        if (list instanceof zzjt) {
            zzjt zzjtVar = (zzjt) list;
            int i2 = this.zzb & 7;
            if (i2 != 0) {
                if (i2 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar = this.zza;
                int zzd = zzhcVar.zzd() + zzhcVar.zzn();
                do {
                    zzjtVar.zzg(this.zza.zzp());
                } while (this.zza.zzd() < zzd);
                zzR(zzd);
                return;
            }
            do {
                zzjtVar.zzg(this.zza.zzp());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 != 0) {
                if (i3 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar2 = this.zza;
                int zzd2 = zzhcVar2.zzd() + zzhcVar2.zzn();
                do {
                    list.add(Long.valueOf(this.zza.zzp()));
                } while (this.zza.zzd() < zzd2);
                zzR(zzd2);
                return;
            }
            do {
                list.add(Long.valueOf(this.zza.zzp()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzF(List list, zzkr zzkrVar, zzie zzieVar) {
        int zzm;
        int i = this.zzb;
        if ((i & 7) != 2) {
            throw zzje.zza();
        }
        do {
            Object zze = zzkrVar.zze();
            zzQ(zze, zzkrVar, zzieVar);
            zzkrVar.zzf(zze);
            list.add(zze);
            if (this.zza.zzC() || this.zzd != 0) {
                return;
            } else {
                zzm = this.zza.zzm();
            }
        } while (zzm == i);
        this.zzd = zzm;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzG(List list) {
        int zzm;
        int i;
        if (list instanceof zziu) {
            zziu zziuVar = (zziu) list;
            int i2 = this.zzb & 7;
            if (i2 == 2) {
                int zzn = this.zza.zzn();
                zzT(zzn);
                int zzd = this.zza.zzd() + zzn;
                do {
                    zziuVar.zzg(this.zza.zzk());
                } while (this.zza.zzd() < zzd);
                return;
            }
            if (i2 != 5) {
                throw zzje.zza();
            }
            do {
                zziuVar.zzg(this.zza.zzk());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 == 2) {
                int zzn2 = this.zza.zzn();
                zzT(zzn2);
                int zzd2 = this.zza.zzd() + zzn2;
                do {
                    list.add(Integer.valueOf(this.zza.zzk()));
                } while (this.zza.zzd() < zzd2);
                return;
            }
            if (i3 != 5) {
                throw zzje.zza();
            }
            do {
                list.add(Integer.valueOf(this.zza.zzk()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzH(List list) {
        int zzm;
        int i;
        if (list instanceof zzjt) {
            zzjt zzjtVar = (zzjt) list;
            int i2 = this.zzb & 7;
            if (i2 != 1) {
                if (i2 != 2) {
                    throw zzje.zza();
                }
                int zzn = this.zza.zzn();
                zzU(zzn);
                int zzd = this.zza.zzd() + zzn;
                do {
                    zzjtVar.zzg(this.zza.zzt());
                } while (this.zza.zzd() < zzd);
                return;
            }
            do {
                zzjtVar.zzg(this.zza.zzt());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 != 1) {
                if (i3 != 2) {
                    throw zzje.zza();
                }
                int zzn2 = this.zza.zzn();
                zzU(zzn2);
                int zzd2 = this.zza.zzd() + zzn2;
                do {
                    list.add(Long.valueOf(this.zza.zzt()));
                } while (this.zza.zzd() < zzd2);
                return;
            }
            do {
                list.add(Long.valueOf(this.zza.zzt()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzI(List list) {
        int zzm;
        int i;
        if (list instanceof zziu) {
            zziu zziuVar = (zziu) list;
            int i2 = this.zzb & 7;
            if (i2 != 0) {
                if (i2 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar = this.zza;
                int zzd = zzhcVar.zzd() + zzhcVar.zzn();
                do {
                    zziuVar.zzg(this.zza.zzl());
                } while (this.zza.zzd() < zzd);
                zzR(zzd);
                return;
            }
            do {
                zziuVar.zzg(this.zza.zzl());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 != 0) {
                if (i3 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar2 = this.zza;
                int zzd2 = zzhcVar2.zzd() + zzhcVar2.zzn();
                do {
                    list.add(Integer.valueOf(this.zza.zzl()));
                } while (this.zza.zzd() < zzd2);
                zzR(zzd2);
                return;
            }
            do {
                list.add(Integer.valueOf(this.zza.zzl()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzJ(List list) {
        int zzm;
        int i;
        if (list instanceof zzjt) {
            zzjt zzjtVar = (zzjt) list;
            int i2 = this.zzb & 7;
            if (i2 != 0) {
                if (i2 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar = this.zza;
                int zzd = zzhcVar.zzd() + zzhcVar.zzn();
                do {
                    zzjtVar.zzg(this.zza.zzu());
                } while (this.zza.zzd() < zzd);
                zzR(zzd);
                return;
            }
            do {
                zzjtVar.zzg(this.zza.zzu());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 != 0) {
                if (i3 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar2 = this.zza;
                int zzd2 = zzhcVar2.zzd() + zzhcVar2.zzn();
                do {
                    list.add(Long.valueOf(this.zza.zzu()));
                } while (this.zza.zzd() < zzd2);
                zzR(zzd2);
                return;
            }
            do {
                list.add(Long.valueOf(this.zza.zzu()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    public final void zzK(List list, boolean z) {
        int zzm;
        int i;
        if ((this.zzb & 7) != 2) {
            throw zzje.zza();
        }
        if ((list instanceof zzjm) && !z) {
            zzjm zzjmVar = (zzjm) list;
            do {
                zzjmVar.zzi(zzp());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            do {
                list.add(z ? zzs() : zzr());
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzL(List list) {
        int zzm;
        int i;
        if (list instanceof zziu) {
            zziu zziuVar = (zziu) list;
            int i2 = this.zzb & 7;
            if (i2 != 0) {
                if (i2 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar = this.zza;
                int zzd = zzhcVar.zzd() + zzhcVar.zzn();
                do {
                    zziuVar.zzg(this.zza.zzn());
                } while (this.zza.zzd() < zzd);
                zzR(zzd);
                return;
            }
            do {
                zziuVar.zzg(this.zza.zzn());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 != 0) {
                if (i3 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar2 = this.zza;
                int zzd2 = zzhcVar2.zzd() + zzhcVar2.zzn();
                do {
                    list.add(Integer.valueOf(this.zza.zzn()));
                } while (this.zza.zzd() < zzd2);
                zzR(zzd2);
                return;
            }
            do {
                list.add(Integer.valueOf(this.zza.zzn()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzM(List list) {
        int zzm;
        int i;
        if (list instanceof zzjt) {
            zzjt zzjtVar = (zzjt) list;
            int i2 = this.zzb & 7;
            if (i2 != 0) {
                if (i2 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar = this.zza;
                int zzd = zzhcVar.zzd() + zzhcVar.zzn();
                do {
                    zzjtVar.zzg(this.zza.zzv());
                } while (this.zza.zzd() < zzd);
                zzR(zzd);
                return;
            }
            do {
                zzjtVar.zzg(this.zza.zzv());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 != 0) {
                if (i3 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar2 = this.zza;
                int zzd2 = zzhcVar2.zzd() + zzhcVar2.zzn();
                do {
                    list.add(Long.valueOf(this.zza.zzv()));
                } while (this.zza.zzd() < zzd2);
                zzR(zzd2);
                return;
            }
            do {
                list.add(Long.valueOf(this.zza.zzv()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final boolean zzN() {
        zzS(0);
        return this.zza.zzD();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final boolean zzO() {
        int i;
        if (this.zza.zzC() || (i = this.zzb) == this.zzc) {
            return false;
        }
        return this.zza.zzE(i);
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final double zza() {
        zzS(1);
        return this.zza.zzb();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final float zzb() {
        zzS(5);
        return this.zza.zzc();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final int zzc() {
        int i = this.zzd;
        if (i != 0) {
            this.zzb = i;
            this.zzd = 0;
        } else {
            i = this.zza.zzm();
            this.zzb = i;
        }
        return (i == 0 || i == this.zzc) ? ConnectionsManager.DEFAULT_DATACENTER_ID : i >>> 3;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final int zzd() {
        return this.zzb;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final int zze() {
        zzS(0);
        return this.zza.zzf();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final int zzf() {
        zzS(5);
        return this.zza.zzg();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final int zzg() {
        zzS(0);
        return this.zza.zzh();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final int zzh() {
        zzS(5);
        return this.zza.zzk();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final int zzi() {
        zzS(0);
        return this.zza.zzl();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final int zzj() {
        zzS(0);
        return this.zza.zzn();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final long zzk() {
        zzS(1);
        return this.zza.zzo();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final long zzl() {
        zzS(0);
        return this.zza.zzp();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final long zzm() {
        zzS(1);
        return this.zza.zzt();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final long zzn() {
        zzS(0);
        return this.zza.zzu();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final long zzo() {
        zzS(0);
        return this.zza.zzv();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final zzgw zzp() {
        zzS(2);
        return this.zza.zzw();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final String zzr() {
        zzS(2);
        return this.zza.zzx();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final String zzs() {
        zzS(2);
        return this.zza.zzy();
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzt(Object obj, zzkr zzkrVar, zzie zzieVar) {
        zzS(3);
        zzP(obj, zzkrVar, zzieVar);
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzu(Object obj, zzkr zzkrVar, zzie zzieVar) {
        zzS(2);
        zzQ(obj, zzkrVar, zzieVar);
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzv(List list) {
        int zzm;
        int i;
        if (list instanceof zzgl) {
            zzgl zzglVar = (zzgl) list;
            int i2 = this.zzb & 7;
            if (i2 != 0) {
                if (i2 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar = this.zza;
                int zzd = zzhcVar.zzd() + zzhcVar.zzn();
                do {
                    zzglVar.zze(this.zza.zzD());
                } while (this.zza.zzd() < zzd);
                zzR(zzd);
                return;
            }
            do {
                zzglVar.zze(this.zza.zzD());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 != 0) {
                if (i3 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar2 = this.zza;
                int zzd2 = zzhcVar2.zzd() + zzhcVar2.zzn();
                do {
                    list.add(Boolean.valueOf(this.zza.zzD()));
                } while (this.zza.zzd() < zzd2);
                zzR(zzd2);
                return;
            }
            do {
                list.add(Boolean.valueOf(this.zza.zzD()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzw(List list) {
        int zzm;
        if ((this.zzb & 7) != 2) {
            throw zzje.zza();
        }
        do {
            list.add(zzp());
            if (this.zza.zzC()) {
                return;
            } else {
                zzm = this.zza.zzm();
            }
        } while (zzm == this.zzb);
        this.zzd = zzm;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzx(List list) {
        int zzm;
        int i;
        if (list instanceof zzhy) {
            zzhy zzhyVar = (zzhy) list;
            int i2 = this.zzb & 7;
            if (i2 != 1) {
                if (i2 != 2) {
                    throw zzje.zza();
                }
                int zzn = this.zza.zzn();
                zzU(zzn);
                int zzd = this.zza.zzd() + zzn;
                do {
                    zzhyVar.zze(this.zza.zzb());
                } while (this.zza.zzd() < zzd);
                return;
            }
            do {
                zzhyVar.zze(this.zza.zzb());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 != 1) {
                if (i3 != 2) {
                    throw zzje.zza();
                }
                int zzn2 = this.zza.zzn();
                zzU(zzn2);
                int zzd2 = this.zza.zzd() + zzn2;
                do {
                    list.add(Double.valueOf(this.zza.zzb()));
                } while (this.zza.zzd() < zzd2);
                return;
            }
            do {
                list.add(Double.valueOf(this.zza.zzb()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzy(List list) {
        int zzm;
        int i;
        if (list instanceof zziu) {
            zziu zziuVar = (zziu) list;
            int i2 = this.zzb & 7;
            if (i2 != 0) {
                if (i2 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar = this.zza;
                int zzd = zzhcVar.zzd() + zzhcVar.zzn();
                do {
                    zziuVar.zzg(this.zza.zzf());
                } while (this.zza.zzd() < zzd);
                zzR(zzd);
                return;
            }
            do {
                zziuVar.zzg(this.zza.zzf());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 != 0) {
                if (i3 != 2) {
                    throw zzje.zza();
                }
                zzhc zzhcVar2 = this.zza;
                int zzd2 = zzhcVar2.zzd() + zzhcVar2.zzn();
                do {
                    list.add(Integer.valueOf(this.zza.zzf()));
                } while (this.zza.zzd() < zzd2);
                zzR(zzd2);
                return;
            }
            do {
                list.add(Integer.valueOf(this.zza.zzf()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }

    @Override // com.google.android.recaptcha.internal.zzkq
    public final void zzz(List list) {
        int zzm;
        int i;
        if (list instanceof zziu) {
            zziu zziuVar = (zziu) list;
            int i2 = this.zzb & 7;
            if (i2 == 2) {
                int zzn = this.zza.zzn();
                zzT(zzn);
                int zzd = this.zza.zzd() + zzn;
                do {
                    zziuVar.zzg(this.zza.zzg());
                } while (this.zza.zzd() < zzd);
                return;
            }
            if (i2 != 5) {
                throw zzje.zza();
            }
            do {
                zziuVar.zzg(this.zza.zzg());
                if (this.zza.zzC()) {
                    return;
                } else {
                    i = this.zza.zzm();
                }
            } while (i == this.zzb);
        } else {
            int i3 = this.zzb & 7;
            if (i3 == 2) {
                int zzn2 = this.zza.zzn();
                zzT(zzn2);
                int zzd2 = this.zza.zzd() + zzn2;
                do {
                    list.add(Integer.valueOf(this.zza.zzg()));
                } while (this.zza.zzd() < zzd2);
                return;
            }
            if (i3 != 5) {
                throw zzje.zza();
            }
            do {
                list.add(Integer.valueOf(this.zza.zzg()));
                if (this.zza.zzC()) {
                    return;
                } else {
                    zzm = this.zza.zzm();
                }
            } while (zzm == this.zzb);
            i = zzm;
        }
        this.zzd = i;
    }
}
