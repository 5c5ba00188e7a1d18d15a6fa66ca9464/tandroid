package com.google.android.recaptcha.internal;

import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
final class zzki implements zzkr {
    private final zzke zza;
    private final zzll zzb;
    private final boolean zzc;
    private final zzif zzd;

    private zzki(zzll zzllVar, zzif zzifVar, zzke zzkeVar) {
        this.zzb = zzllVar;
        this.zzc = zzifVar.zzj(zzkeVar);
        this.zzd = zzifVar;
        this.zza = zzkeVar;
    }

    static zzki zzc(zzll zzllVar, zzif zzifVar, zzke zzkeVar) {
        return new zzki(zzllVar, zzifVar, zzkeVar);
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final int zza(Object obj) {
        zzll zzllVar = this.zzb;
        int zzb = zzllVar.zzb(zzllVar.zzd(obj));
        return this.zzc ? zzb + this.zzd.zzb(obj).zzb() : zzb;
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final int zzb(Object obj) {
        int hashCode = this.zzb.zzd(obj).hashCode();
        return this.zzc ? (hashCode * 53) + this.zzd.zzb(obj).zza.hashCode() : hashCode;
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final Object zze() {
        zzke zzkeVar = this.zza;
        return zzkeVar instanceof zzit ? ((zzit) zzkeVar).zzs() : zzkeVar.zzW().zzk();
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final void zzf(Object obj) {
        this.zzb.zzm(obj);
        this.zzd.zzf(obj);
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final void zzg(Object obj, Object obj2) {
        zzkt.zzr(this.zzb, obj, obj2);
        if (this.zzc) {
            zzkt.zzq(this.zzd, obj, obj2);
        }
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final void zzh(Object obj, zzkq zzkqVar, zzie zzieVar) {
        boolean zzO;
        zzll zzllVar = this.zzb;
        Object zzc = zzllVar.zzc(obj);
        zzif zzifVar = this.zzd;
        zzij zzc2 = zzifVar.zzc(obj);
        while (zzkqVar.zzc() != Integer.MAX_VALUE) {
            try {
                int zzd = zzkqVar.zzd();
                if (zzd != 11) {
                    if ((zzd & 7) == 2) {
                        Object zzd2 = zzifVar.zzd(zzieVar, this.zza, zzd >>> 3);
                        if (zzd2 != null) {
                            zzifVar.zzg(zzkqVar, zzd2, zzieVar, zzc2);
                        } else {
                            zzO = zzllVar.zzr(zzc, zzkqVar);
                        }
                    } else {
                        zzO = zzkqVar.zzO();
                    }
                    if (!zzO) {
                        break;
                    }
                } else {
                    Object obj2 = null;
                    zzgw zzgwVar = null;
                    int i = 0;
                    while (zzkqVar.zzc() != Integer.MAX_VALUE) {
                        int zzd3 = zzkqVar.zzd();
                        if (zzd3 == 16) {
                            i = zzkqVar.zzj();
                            obj2 = zzifVar.zzd(zzieVar, this.zza, i);
                        } else if (zzd3 == 26) {
                            if (obj2 != null) {
                                zzifVar.zzg(zzkqVar, obj2, zzieVar, zzc2);
                            } else {
                                zzgwVar = zzkqVar.zzp();
                            }
                        } else if (!zzkqVar.zzO()) {
                            break;
                        }
                    }
                    if (zzkqVar.zzd() != 12) {
                        throw zzje.zzb();
                    }
                    if (zzgwVar != null) {
                        if (obj2 != null) {
                            zzifVar.zzh(zzgwVar, obj2, zzieVar, zzc2);
                        } else {
                            zzllVar.zzk(zzc, i, zzgwVar);
                        }
                    }
                }
            } finally {
                zzllVar.zzn(obj, zzc);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0088 A[EDGE_INSN: B:24:0x0088->B:25:0x0088 BREAK  A[LOOP:1: B:10:0x004b->B:18:0x004b], SYNTHETIC] */
    @Override // com.google.android.recaptcha.internal.zzkr
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zzi(Object obj, byte[] bArr, int i, int i2, zzgj zzgjVar) {
        zzit zzitVar = (zzit) obj;
        zzlm zzlmVar = zzitVar.zzc;
        if (zzlmVar == zzlm.zzc()) {
            zzlmVar = zzlm.zzf();
            zzitVar.zzc = zzlmVar;
        }
        ((zzip) obj).zzi();
        Object obj2 = null;
        while (i < i2) {
            int zzi = zzgk.zzi(bArr, i, zzgjVar);
            int i3 = zzgjVar.zza;
            if (i3 == 11) {
                int i4 = 0;
                zzgw zzgwVar = null;
                while (zzi < i2) {
                    zzi = zzgk.zzi(bArr, zzi, zzgjVar);
                    int i5 = zzgjVar.zza;
                    int i6 = i5 >>> 3;
                    int i7 = i5 & 7;
                    if (i6 != 2) {
                        if (i6 == 3) {
                            if (obj2 != null) {
                                int i8 = zzkn.zza;
                                throw null;
                            }
                            if (i7 == 2) {
                                zzi = zzgk.zza(bArr, zzi, zzgjVar);
                                zzgwVar = (zzgw) zzgjVar.zzc;
                            }
                        }
                        if (i5 != 12) {
                            break;
                        } else {
                            zzi = zzgk.zzo(i5, bArr, zzi, i2, zzgjVar);
                        }
                    } else if (i7 == 0) {
                        zzi = zzgk.zzi(bArr, zzi, zzgjVar);
                        i4 = zzgjVar.zza;
                        obj2 = this.zzd.zzd(zzgjVar.zzd, this.zza, i4);
                    } else if (i5 != 12) {
                    }
                }
                if (zzgwVar != null) {
                    zzlmVar.zzj((i4 << 3) | 2, zzgwVar);
                }
                i = zzi;
            } else if ((i3 & 7) == 2) {
                obj2 = this.zzd.zzd(zzgjVar.zzd, this.zza, i3 >>> 3);
                if (obj2 != null) {
                    int i9 = zzkn.zza;
                    throw null;
                }
                i = zzgk.zzh(i3, bArr, zzi, i2, zzlmVar, zzgjVar);
            } else {
                i = zzgk.zzo(i3, bArr, zzi, i2, zzgjVar);
            }
        }
        if (i != i2) {
            throw zzje.zzg();
        }
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final void zzj(Object obj, zzmd zzmdVar) {
        Iterator zzf = this.zzd.zzb(obj).zzf();
        while (zzf.hasNext()) {
            Map.Entry entry = (Map.Entry) zzf.next();
            zzii zziiVar = (zzii) entry.getKey();
            if (zziiVar.zze() != zzmc.zzi) {
                throw new IllegalStateException("Found invalid MessageSet item.");
            }
            zziiVar.zzg();
            zziiVar.zzf();
            zzmdVar.zzw(zziiVar.zza(), entry instanceof zzjh ? ((zzjh) entry).zza().zzb() : entry.getValue());
        }
        zzll zzllVar = this.zzb;
        zzllVar.zzp(zzllVar.zzd(obj), zzmdVar);
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final boolean zzk(Object obj, Object obj2) {
        zzll zzllVar = this.zzb;
        if (!zzllVar.zzd(obj).equals(zzllVar.zzd(obj2))) {
            return false;
        }
        if (this.zzc) {
            return this.zzd.zzb(obj).equals(this.zzd.zzb(obj2));
        }
        return true;
    }

    @Override // com.google.android.recaptcha.internal.zzkr
    public final boolean zzl(Object obj) {
        return this.zzd.zzb(obj).zzk();
    }
}
