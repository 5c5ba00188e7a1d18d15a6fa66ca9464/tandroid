package com.google.android.recaptcha.internal;

import java.util.Map;

/* loaded from: classes.dex */
final class zzig extends zzif {
    zzig() {
    }

    @Override // com.google.android.recaptcha.internal.zzif
    final int zza(Map.Entry entry) {
        return ((zziq) entry.getKey()).zza;
    }

    @Override // com.google.android.recaptcha.internal.zzif
    final zzij zzb(Object obj) {
        return ((zzip) obj).zzb;
    }

    @Override // com.google.android.recaptcha.internal.zzif
    final zzij zzc(Object obj) {
        return ((zzip) obj).zzi();
    }

    @Override // com.google.android.recaptcha.internal.zzif
    final Object zzd(zzie zzieVar, zzke zzkeVar, int i) {
        return zzieVar.zza(zzkeVar, i);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00f0  */
    @Override // com.google.android.recaptcha.internal.zzif
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    final Object zze(Object obj, zzkq zzkqVar, Object obj2, zzie zzieVar, zzij zzijVar, Object obj3, zzll zzllVar) {
        int ordinal;
        Object zze;
        long zzl;
        int zzg;
        zzir zzirVar = (zzir) obj2;
        zzmb zzmbVar = zzirVar.zzb.zzb;
        Object obj4 = null;
        if (zzmbVar == zzmb.zzn) {
            zzkqVar.zzg();
            throw null;
        }
        switch (zzmbVar.ordinal()) {
            case 0:
                obj4 = Double.valueOf(zzkqVar.zza());
                ordinal = zzirVar.zzb.zzb.ordinal();
                if ((ordinal != 9 || ordinal == 10) && (zze = zzijVar.zze(zzirVar.zzb)) != null) {
                    byte[] bArr = zzjc.zzd;
                    obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                }
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 1:
                obj4 = Float.valueOf(zzkqVar.zzb());
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                    break;
                }
                byte[] bArr2 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 2:
                zzl = zzkqVar.zzl();
                obj4 = Long.valueOf(zzl);
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr22 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 3:
                zzl = zzkqVar.zzo();
                obj4 = Long.valueOf(zzl);
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 4:
                zzg = zzkqVar.zzg();
                obj4 = Integer.valueOf(zzg);
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr2222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 5:
                zzl = zzkqVar.zzk();
                obj4 = Long.valueOf(zzl);
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr22222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 6:
                zzg = zzkqVar.zzf();
                obj4 = Integer.valueOf(zzg);
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr222222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 7:
                obj4 = Boolean.valueOf(zzkqVar.zzN());
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr2222222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 8:
                obj4 = zzkqVar.zzr();
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr22222222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 9:
                Object zze2 = zzijVar.zze(zzirVar.zzb);
                if (!(zze2 instanceof zzit)) {
                    throw null;
                }
                zzkr zzb = zzkn.zza().zzb(zze2.getClass());
                if (!((zzit) zze2).zzG()) {
                    Object zze3 = zzb.zze();
                    zzb.zzg(zze3, zze2);
                    zzijVar.zzi(zzirVar.zzb, zze3);
                    zze2 = zze3;
                }
                zzkqVar.zzt(zze2, zzb, zzieVar);
                return obj3;
            case 10:
                Object zze4 = zzijVar.zze(zzirVar.zzb);
                if (!(zze4 instanceof zzit)) {
                    throw null;
                }
                zzkr zzb2 = zzkn.zza().zzb(zze4.getClass());
                if (!((zzit) zze4).zzG()) {
                    Object zze5 = zzb2.zze();
                    zzb2.zzg(zze5, zze4);
                    zzijVar.zzi(zzirVar.zzb, zze5);
                    zze4 = zze5;
                }
                zzkqVar.zzu(zze4, zzb2, zzieVar);
                return obj3;
            case 11:
                obj4 = zzkqVar.zzp();
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr222222222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 12:
                zzg = zzkqVar.zzj();
                obj4 = Integer.valueOf(zzg);
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr2222222222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 13:
                throw new IllegalStateException("Shouldn't reach here.");
            case 14:
                zzg = zzkqVar.zzh();
                obj4 = Integer.valueOf(zzg);
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr22222222222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 15:
                zzl = zzkqVar.zzm();
                obj4 = Long.valueOf(zzl);
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr222222222222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 16:
                zzg = zzkqVar.zzi();
                obj4 = Integer.valueOf(zzg);
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr2222222222222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            case 17:
                zzl = zzkqVar.zzn();
                obj4 = Long.valueOf(zzl);
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr22222222222222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
            default:
                ordinal = zzirVar.zzb.zzb.ordinal();
                if (ordinal != 9) {
                }
                byte[] bArr222222222222222 = zzjc.zzd;
                obj4 = ((zzke) zze).zzX().zzc((zzke) obj4).zzk();
                zzijVar.zzi(zzirVar.zzb, obj4);
                return obj3;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzif
    final void zzf(Object obj) {
        ((zzip) obj).zzb.zzg();
    }

    @Override // com.google.android.recaptcha.internal.zzif
    final void zzg(zzkq zzkqVar, Object obj, zzie zzieVar, zzij zzijVar) {
        throw null;
    }

    @Override // com.google.android.recaptcha.internal.zzif
    final void zzh(zzgw zzgwVar, Object obj, zzie zzieVar, zzij zzijVar) {
        throw null;
    }

    @Override // com.google.android.recaptcha.internal.zzif
    final void zzi(zzmd zzmdVar, Map.Entry entry) {
        zziq zziqVar = (zziq) entry.getKey();
        zzmb zzmbVar = zzmb.zza;
        switch (zziqVar.zzb.ordinal()) {
            case 0:
                zzmdVar.zzf(zziqVar.zza, ((Double) entry.getValue()).doubleValue());
                break;
            case 1:
                zzmdVar.zzo(zziqVar.zza, ((Float) entry.getValue()).floatValue());
                break;
            case 2:
                zzmdVar.zzt(zziqVar.zza, ((Long) entry.getValue()).longValue());
                break;
            case 3:
                zzmdVar.zzK(zziqVar.zza, ((Long) entry.getValue()).longValue());
                break;
            case 4:
                zzmdVar.zzr(zziqVar.zza, ((Integer) entry.getValue()).intValue());
                break;
            case 5:
                zzmdVar.zzm(zziqVar.zza, ((Long) entry.getValue()).longValue());
                break;
            case 6:
                zzmdVar.zzk(zziqVar.zza, ((Integer) entry.getValue()).intValue());
                break;
            case 7:
                zzmdVar.zzb(zziqVar.zza, ((Boolean) entry.getValue()).booleanValue());
                break;
            case 8:
                zzmdVar.zzG(zziqVar.zza, (String) entry.getValue());
                break;
            case 9:
                zzmdVar.zzq(zziqVar.zza, entry.getValue(), zzkn.zza().zzb(entry.getValue().getClass()));
                break;
            case 10:
                zzmdVar.zzv(zziqVar.zza, entry.getValue(), zzkn.zza().zzb(entry.getValue().getClass()));
                break;
            case 11:
                zzmdVar.zzd(zziqVar.zza, (zzgw) entry.getValue());
                break;
            case 12:
                zzmdVar.zzI(zziqVar.zza, ((Integer) entry.getValue()).intValue());
                break;
            case 13:
                zzmdVar.zzr(zziqVar.zza, ((Integer) entry.getValue()).intValue());
                break;
            case 14:
                zzmdVar.zzx(zziqVar.zza, ((Integer) entry.getValue()).intValue());
                break;
            case 15:
                zzmdVar.zzz(zziqVar.zza, ((Long) entry.getValue()).longValue());
                break;
            case 16:
                zzmdVar.zzB(zziqVar.zza, ((Integer) entry.getValue()).intValue());
                break;
            case 17:
                zzmdVar.zzD(zziqVar.zza, ((Long) entry.getValue()).longValue());
                break;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzif
    final boolean zzj(zzke zzkeVar) {
        return zzkeVar instanceof zzip;
    }
}
