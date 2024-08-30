package com.google.android.gms.internal.clearcut;

import java.util.Arrays;
/* loaded from: classes.dex */
public final class zzha extends zzfu implements Cloneable {
    private byte[] zzbjn;
    private zzge$zzd zzbjo;
    public byte[] zzbjp;
    private String zzbjq;
    private String zzbjr;
    private String zzbjt;
    public long zzbju;
    public byte[] zzbjw;
    private String zzbjx;
    private int zzbjy;
    private int[] zzbjz;
    private long zzbka;
    private zzge$zzs zzbkb;
    public boolean zzbkc;
    public long zzbjf = 0;
    public long zzbjg = 0;
    private long zzbjh = 0;
    private String tag = "";
    public int zzbji = 0;
    private String zzbjj = "";
    private int zzbjk = 0;
    private boolean zzbjl = false;
    private zzhb[] zzbjm = zzhb.zzge();

    public zzha() {
        byte[] bArr = zzgb.zzse;
        this.zzbjn = bArr;
        this.zzbjo = null;
        this.zzbjp = bArr;
        this.zzbjq = "";
        this.zzbjr = "";
        this.zzbjt = "";
        this.zzbju = 180000L;
        this.zzbjw = bArr;
        this.zzbjx = "";
        this.zzbjy = 0;
        this.zzbjz = zzgb.zzrx;
        this.zzbka = 0L;
        this.zzbkb = null;
        this.zzbkc = false;
        this.zzrs = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: zzgd */
    public final zzha clone() {
        try {
            zzha zzhaVar = (zzha) super.zzeo();
            zzhb[] zzhbVarArr = this.zzbjm;
            if (zzhbVarArr != null && zzhbVarArr.length > 0) {
                zzhaVar.zzbjm = new zzhb[zzhbVarArr.length];
                int i = 0;
                while (true) {
                    zzhb[] zzhbVarArr2 = this.zzbjm;
                    if (i >= zzhbVarArr2.length) {
                        break;
                    }
                    zzhb zzhbVar = zzhbVarArr2[i];
                    i++;
                }
            }
            zzge$zzd zzge_zzd = this.zzbjo;
            if (zzge_zzd != null) {
                zzhaVar.zzbjo = zzge_zzd;
            }
            int[] iArr = this.zzbjz;
            if (iArr != null && iArr.length > 0) {
                zzhaVar.zzbjz = (int[]) iArr.clone();
            }
            zzge$zzs zzge_zzs = this.zzbkb;
            if (zzge_zzs != null) {
                zzhaVar.zzbkb = zzge_zzs;
            }
            return zzhaVar;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzha) {
            zzha zzhaVar = (zzha) obj;
            if (this.zzbjf == zzhaVar.zzbjf && this.zzbjg == zzhaVar.zzbjg) {
                String str = this.tag;
                if (str == null) {
                    if (zzhaVar.tag != null) {
                        return false;
                    }
                } else if (!str.equals(zzhaVar.tag)) {
                    return false;
                }
                if (this.zzbji != zzhaVar.zzbji) {
                    return false;
                }
                String str2 = this.zzbjj;
                if (str2 == null) {
                    if (zzhaVar.zzbjj != null) {
                        return false;
                    }
                } else if (!str2.equals(zzhaVar.zzbjj)) {
                    return false;
                }
                if (zzfy.equals(this.zzbjm, zzhaVar.zzbjm) && Arrays.equals(this.zzbjn, zzhaVar.zzbjn)) {
                    zzge$zzd zzge_zzd = this.zzbjo;
                    if (zzge_zzd == null) {
                        if (zzhaVar.zzbjo != null) {
                            return false;
                        }
                    } else if (!zzge_zzd.equals(zzhaVar.zzbjo)) {
                        return false;
                    }
                    if (Arrays.equals(this.zzbjp, zzhaVar.zzbjp)) {
                        String str3 = this.zzbjq;
                        if (str3 == null) {
                            if (zzhaVar.zzbjq != null) {
                                return false;
                            }
                        } else if (!str3.equals(zzhaVar.zzbjq)) {
                            return false;
                        }
                        String str4 = this.zzbjr;
                        if (str4 == null) {
                            if (zzhaVar.zzbjr != null) {
                                return false;
                            }
                        } else if (!str4.equals(zzhaVar.zzbjr)) {
                            return false;
                        }
                        String str5 = this.zzbjt;
                        if (str5 == null) {
                            if (zzhaVar.zzbjt != null) {
                                return false;
                            }
                        } else if (!str5.equals(zzhaVar.zzbjt)) {
                            return false;
                        }
                        if (this.zzbju == zzhaVar.zzbju && Arrays.equals(this.zzbjw, zzhaVar.zzbjw)) {
                            String str6 = this.zzbjx;
                            if (str6 == null) {
                                if (zzhaVar.zzbjx != null) {
                                    return false;
                                }
                            } else if (!str6.equals(zzhaVar.zzbjx)) {
                                return false;
                            }
                            if (zzfy.equals(this.zzbjz, zzhaVar.zzbjz)) {
                                zzge$zzs zzge_zzs = this.zzbkb;
                                if (zzge_zzs == null) {
                                    if (zzhaVar.zzbkb != null) {
                                        return false;
                                    }
                                } else if (!zzge_zzs.equals(zzhaVar.zzbkb)) {
                                    return false;
                                }
                                return this.zzbkc == zzhaVar.zzbkc;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        long j = this.zzbjf;
        long j2 = this.zzbjg;
        int hashCode = (((((zzha.class.getName().hashCode() + 527) * 31) + ((int) (j ^ (j >>> 32)))) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 961;
        String str = this.tag;
        int hashCode2 = (((hashCode + (str == null ? 0 : str.hashCode())) * 31) + this.zzbji) * 31;
        String str2 = this.zzbjj;
        int hashCode3 = ((((((hashCode2 + (str2 == null ? 0 : str2.hashCode())) * 961) + 1237) * 31) + zzfy.hashCode(this.zzbjm)) * 31) + Arrays.hashCode(this.zzbjn);
        zzge$zzd zzge_zzd = this.zzbjo;
        int hashCode4 = ((((hashCode3 * 31) + (zzge_zzd == null ? 0 : zzge_zzd.hashCode())) * 31) + Arrays.hashCode(this.zzbjp)) * 31;
        String str3 = this.zzbjq;
        int hashCode5 = (hashCode4 + (str3 == null ? 0 : str3.hashCode())) * 31;
        String str4 = this.zzbjr;
        int hashCode6 = (hashCode5 + (str4 == null ? 0 : str4.hashCode())) * 961;
        String str5 = this.zzbjt;
        int hashCode7 = str5 == null ? 0 : str5.hashCode();
        long j3 = this.zzbju;
        int hashCode8 = (((((hashCode6 + hashCode7) * 31) + ((int) (j3 ^ (j3 >>> 32)))) * 961) + Arrays.hashCode(this.zzbjw)) * 31;
        String str6 = this.zzbjx;
        int hashCode9 = ((hashCode8 + (str6 == null ? 0 : str6.hashCode())) * 961) + zzfy.hashCode(this.zzbjz);
        zzge$zzs zzge_zzs = this.zzbkb;
        return ((((hashCode9 * 961) + (zzge_zzs != null ? zzge_zzs.hashCode() : 0)) * 31) + (this.zzbkc ? 1231 : 1237)) * 31;
    }

    @Override // com.google.android.gms.internal.clearcut.zzfu, com.google.android.gms.internal.clearcut.zzfz
    public final void zza(zzfs zzfsVar) {
        long j = this.zzbjf;
        if (j != 0) {
            zzfsVar.zzi(1, j);
        }
        String str = this.tag;
        if (str != null && !str.equals("")) {
            zzfsVar.zza(2, this.tag);
        }
        zzhb[] zzhbVarArr = this.zzbjm;
        int i = 0;
        if (zzhbVarArr != null && zzhbVarArr.length > 0) {
            int i2 = 0;
            while (true) {
                zzhb[] zzhbVarArr2 = this.zzbjm;
                if (i2 >= zzhbVarArr2.length) {
                    break;
                }
                zzhb zzhbVar = zzhbVarArr2[i2];
                i2++;
            }
        }
        byte[] bArr = this.zzbjn;
        byte[] bArr2 = zzgb.zzse;
        if (!Arrays.equals(bArr, bArr2)) {
            zzfsVar.zza(4, this.zzbjn);
        }
        if (!Arrays.equals(this.zzbjp, bArr2)) {
            zzfsVar.zza(6, this.zzbjp);
        }
        String str2 = this.zzbjq;
        if (str2 != null && !str2.equals("")) {
            zzfsVar.zza(8, this.zzbjq);
        }
        zzge$zzd zzge_zzd = this.zzbjo;
        if (zzge_zzd != null) {
            zzfsVar.zze(9, zzge_zzd);
        }
        int i3 = this.zzbji;
        if (i3 != 0) {
            zzfsVar.zzc(11, i3);
        }
        String str3 = this.zzbjr;
        if (str3 != null && !str3.equals("")) {
            zzfsVar.zza(13, this.zzbjr);
        }
        String str4 = this.zzbjt;
        if (str4 != null && !str4.equals("")) {
            zzfsVar.zza(14, this.zzbjt);
        }
        long j2 = this.zzbju;
        if (j2 != 180000) {
            zzfsVar.zzb(15, 0);
            zzfsVar.zzn(zzfs.zzj(j2));
        }
        long j3 = this.zzbjg;
        if (j3 != 0) {
            zzfsVar.zzi(17, j3);
        }
        if (!Arrays.equals(this.zzbjw, bArr2)) {
            zzfsVar.zza(18, this.zzbjw);
        }
        int[] iArr = this.zzbjz;
        if (iArr != null && iArr.length > 0) {
            while (true) {
                int[] iArr2 = this.zzbjz;
                if (i >= iArr2.length) {
                    break;
                }
                zzfsVar.zzc(20, iArr2[i]);
                i++;
            }
        }
        zzge$zzs zzge_zzs = this.zzbkb;
        if (zzge_zzs != null) {
            zzfsVar.zze(23, zzge_zzs);
        }
        String str5 = this.zzbjx;
        if (str5 != null && !str5.equals("")) {
            zzfsVar.zza(24, this.zzbjx);
        }
        boolean z = this.zzbkc;
        if (z) {
            zzfsVar.zzb(25, z);
        }
        String str6 = this.zzbjj;
        if (str6 != null && !str6.equals("")) {
            zzfsVar.zza(26, this.zzbjj);
        }
        super.zza(zzfsVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.clearcut.zzfu, com.google.android.gms.internal.clearcut.zzfz
    public final int zzen() {
        int[] iArr;
        int zzen = super.zzen();
        long j = this.zzbjf;
        if (j != 0) {
            zzen += zzfs.zzd(1, j);
        }
        String str = this.tag;
        if (str != null && !str.equals("")) {
            zzen += zzfs.zzb(2, this.tag);
        }
        zzhb[] zzhbVarArr = this.zzbjm;
        int i = 0;
        if (zzhbVarArr != null && zzhbVarArr.length > 0) {
            int i2 = 0;
            while (true) {
                zzhb[] zzhbVarArr2 = this.zzbjm;
                if (i2 >= zzhbVarArr2.length) {
                    break;
                }
                zzhb zzhbVar = zzhbVarArr2[i2];
                i2++;
            }
        }
        byte[] bArr = this.zzbjn;
        byte[] bArr2 = zzgb.zzse;
        if (!Arrays.equals(bArr, bArr2)) {
            zzen += zzfs.zzb(4, this.zzbjn);
        }
        if (!Arrays.equals(this.zzbjp, bArr2)) {
            zzen += zzfs.zzb(6, this.zzbjp);
        }
        String str2 = this.zzbjq;
        if (str2 != null && !str2.equals("")) {
            zzen += zzfs.zzb(8, this.zzbjq);
        }
        zzge$zzd zzge_zzd = this.zzbjo;
        if (zzge_zzd != null) {
            zzen += zzbn.zzc(9, zzge_zzd);
        }
        int i3 = this.zzbji;
        if (i3 != 0) {
            zzen += zzfs.zzr(11) + zzfs.zzs(i3);
        }
        String str3 = this.zzbjr;
        if (str3 != null && !str3.equals("")) {
            zzen += zzfs.zzb(13, this.zzbjr);
        }
        String str4 = this.zzbjt;
        if (str4 != null && !str4.equals("")) {
            zzen += zzfs.zzb(14, this.zzbjt);
        }
        long j2 = this.zzbju;
        if (j2 != 180000) {
            zzen += zzfs.zzr(15) + zzfs.zzo(zzfs.zzj(j2));
        }
        long j3 = this.zzbjg;
        if (j3 != 0) {
            zzen += zzfs.zzd(17, j3);
        }
        if (!Arrays.equals(this.zzbjw, bArr2)) {
            zzen += zzfs.zzb(18, this.zzbjw);
        }
        int[] iArr2 = this.zzbjz;
        if (iArr2 != null && iArr2.length > 0) {
            int i4 = 0;
            while (true) {
                iArr = this.zzbjz;
                if (i >= iArr.length) {
                    break;
                }
                i4 += zzfs.zzs(iArr[i]);
                i++;
            }
            zzen = zzen + i4 + (iArr.length * 2);
        }
        zzge$zzs zzge_zzs = this.zzbkb;
        if (zzge_zzs != null) {
            zzen += zzbn.zzc(23, zzge_zzs);
        }
        String str5 = this.zzbjx;
        if (str5 != null && !str5.equals("")) {
            zzen += zzfs.zzb(24, this.zzbjx);
        }
        if (this.zzbkc) {
            zzen += zzfs.zzr(25) + 1;
        }
        String str6 = this.zzbjj;
        return (str6 == null || str6.equals("")) ? zzen : zzen + zzfs.zzb(26, this.zzbjj);
    }
}
