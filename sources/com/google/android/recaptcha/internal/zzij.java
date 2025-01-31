package com.google.android.recaptcha.internal;

import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
final class zzij {
    private static final zzij zzb = new zzij(true);
    final zzle zza = new zzku(16);
    private boolean zzc;
    private boolean zzd;

    private zzij() {
    }

    private zzij(boolean z) {
        zzg();
        zzg();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int zza(zzii zziiVar, Object obj) {
        long longValue;
        int zza;
        int intValue;
        int intValue2;
        zzmb zzd = zziiVar.zzd();
        int zza2 = zziiVar.zza();
        zziiVar.zzg();
        int zzy = zzhh.zzy(zza2 << 3);
        if (zzd == zzmb.zzj) {
            byte[] bArr = zzjc.zzd;
            if (((zzke) obj) instanceof zzgg) {
                throw null;
            }
            zzy += zzy;
        }
        zzmc zzmcVar = zzmc.zza;
        int i = 4;
        switch (zzd.ordinal()) {
            case 0:
                ((Double) obj).doubleValue();
                i = 8;
                return zzy + i;
            case 1:
                ((Float) obj).floatValue();
                return zzy + i;
            case 2:
            case 3:
                longValue = ((Long) obj).longValue();
                i = zzhh.zzz(longValue);
                return zzy + i;
            case 4:
                intValue2 = ((Integer) obj).intValue();
                i = zzhh.zzu(intValue2);
                return zzy + i;
            case 5:
            case 15:
                ((Long) obj).longValue();
                i = 8;
                return zzy + i;
            case 6:
            case 14:
                ((Integer) obj).intValue();
                return zzy + i;
            case 7:
                ((Boolean) obj).booleanValue();
                i = 1;
                return zzy + i;
            case 8:
                if (!(obj instanceof zzgw)) {
                    i = zzhh.zzx((String) obj);
                    return zzy + i;
                }
                zza = ((zzgw) obj).zzd();
                i = zzhh.zzy(zza) + zza;
                return zzy + i;
            case 9:
                i = ((zzke) obj).zzn();
                return zzy + i;
            case 10:
                if (!(obj instanceof zzjj)) {
                    i = zzhh.zzv((zzke) obj);
                    return zzy + i;
                }
                zza = ((zzjj) obj).zza();
                i = zzhh.zzy(zza) + zza;
                return zzy + i;
            case 11:
                if (!(obj instanceof zzgw)) {
                    zza = ((byte[]) obj).length;
                    i = zzhh.zzy(zza) + zza;
                    return zzy + i;
                }
                zza = ((zzgw) obj).zzd();
                i = zzhh.zzy(zza) + zza;
                return zzy + i;
            case 12:
                intValue = ((Integer) obj).intValue();
                i = zzhh.zzy(intValue);
                return zzy + i;
            case 13:
                if (obj instanceof zziv) {
                    intValue2 = ((zziv) obj).zza();
                    i = zzhh.zzu(intValue2);
                    return zzy + i;
                }
                intValue2 = ((Integer) obj).intValue();
                i = zzhh.zzu(intValue2);
                return zzy + i;
            case 16:
                int intValue3 = ((Integer) obj).intValue();
                intValue = (intValue3 >> 31) ^ (intValue3 + intValue3);
                i = zzhh.zzy(intValue);
                return zzy + i;
            case 17:
                long longValue2 = ((Long) obj).longValue();
                longValue = (longValue2 >> 63) ^ (longValue2 + longValue2);
                i = zzhh.zzz(longValue);
                return zzy + i;
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    public static zzij zzd() {
        return zzb;
    }

    private static Object zzl(Object obj) {
        if (obj instanceof zzkj) {
            return ((zzkj) obj).zzd();
        }
        if (!(obj instanceof byte[])) {
            return obj;
        }
        byte[] bArr = (byte[]) obj;
        int length = bArr.length;
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, 0, bArr2, 0, length);
        return bArr2;
    }

    private final void zzm(Map.Entry entry) {
        zzke zzj;
        zzii zziiVar = (zzii) entry.getKey();
        Object value = entry.getValue();
        boolean z = value instanceof zzjj;
        zziiVar.zzg();
        if (zziiVar.zze() != zzmc.zzi) {
            if (z) {
                throw new IllegalStateException("Lazy fields must be message-valued");
            }
            this.zza.put(zziiVar, zzl(value));
            return;
        }
        Object zze = zze(zziiVar);
        if (zze == null) {
            this.zza.put(zziiVar, zzl(value));
            if (z) {
                this.zzd = true;
                return;
            }
            return;
        }
        if (z) {
            throw null;
        }
        if (zze instanceof zzkj) {
            zzj = zziiVar.zzc((zzkj) zze, (zzkj) value);
        } else {
            zzkd zzX = ((zzke) zze).zzX();
            zziiVar.zzb(zzX, (zzke) value);
            zzj = zzX.zzj();
        }
        this.zza.put(zziiVar, zzj);
    }

    private static boolean zzn(Map.Entry entry) {
        zzii zziiVar = (zzii) entry.getKey();
        if (zziiVar.zze() != zzmc.zzi) {
            return true;
        }
        zziiVar.zzg();
        Object value = entry.getValue();
        if (value instanceof zzkf) {
            return ((zzkf) value).zzo();
        }
        if (value instanceof zzjj) {
            return true;
        }
        throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
    }

    private static final int zzo(Map.Entry entry) {
        zzii zziiVar = (zzii) entry.getKey();
        Object value = entry.getValue();
        if (zziiVar.zze() != zzmc.zzi) {
            return zza(zziiVar, value);
        }
        zziiVar.zzg();
        zziiVar.zzf();
        boolean z = value instanceof zzjj;
        int zza = ((zzii) entry.getKey()).zza();
        if (!z) {
            int zzy = zzhh.zzy(zza);
            int zzy2 = zzhh.zzy(24) + zzhh.zzv((zzke) value);
            int zzy3 = zzhh.zzy(16);
            int zzy4 = zzhh.zzy(8);
            return zzy4 + zzy4 + zzy3 + zzy + zzy2;
        }
        int zzy5 = zzhh.zzy(zza);
        int zza2 = ((zzjj) value).zza();
        int zzy6 = zzhh.zzy(zza2) + zza2;
        int zzy7 = zzhh.zzy(24);
        int zzy8 = zzhh.zzy(16);
        int zzy9 = zzhh.zzy(8);
        return zzy9 + zzy9 + zzy8 + zzy5 + zzy7 + zzy6;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzij) {
            return this.zza.equals(((zzij) obj).zza);
        }
        return false;
    }

    public final int hashCode() {
        return this.zza.hashCode();
    }

    public final int zzb() {
        int i = 0;
        for (int i2 = 0; i2 < this.zza.zzb(); i2++) {
            i += zzo(this.zza.zzg(i2));
        }
        Iterator it = this.zza.zzc().iterator();
        while (it.hasNext()) {
            i += zzo((Map.Entry) it.next());
        }
        return i;
    }

    /* renamed from: zzc, reason: merged with bridge method [inline-methods] */
    public final zzij clone() {
        zzij zzijVar = new zzij();
        for (int i = 0; i < this.zza.zzb(); i++) {
            Map.Entry zzg = this.zza.zzg(i);
            zzijVar.zzi((zzii) zzg.getKey(), zzg.getValue());
        }
        for (Map.Entry entry : this.zza.zzc()) {
            zzijVar.zzi((zzii) entry.getKey(), entry.getValue());
        }
        zzijVar.zzd = this.zzd;
        return zzijVar;
    }

    public final Object zze(zzii zziiVar) {
        Object obj = this.zza.get(zziiVar);
        if (obj instanceof zzjj) {
            throw null;
        }
        return obj;
    }

    public final Iterator zzf() {
        return this.zzd ? new zzji(this.zza.entrySet().iterator()) : this.zza.entrySet().iterator();
    }

    public final void zzg() {
        if (this.zzc) {
            return;
        }
        for (int i = 0; i < this.zza.zzb(); i++) {
            Map.Entry zzg = this.zza.zzg(i);
            if (zzg.getValue() instanceof zzit) {
                ((zzit) zzg.getValue()).zzB();
            }
        }
        this.zza.zza();
        this.zzc = true;
    }

    public final void zzh(zzij zzijVar) {
        for (int i = 0; i < zzijVar.zza.zzb(); i++) {
            zzm(zzijVar.zza.zzg(i));
        }
        Iterator it = zzijVar.zza.zzc().iterator();
        while (it.hasNext()) {
            zzm((Map.Entry) it.next());
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x002c, code lost:
    
        if ((r7 instanceof com.google.android.recaptcha.internal.zziv) == false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0035, code lost:
    
        if ((r7 instanceof byte[]) == false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0049, code lost:
    
        if (r1 == false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0023, code lost:
    
        if ((r7 instanceof com.google.android.recaptcha.internal.zzjj) == false) goto L32;
     */
    /* JADX WARN: Removed duplicated region for block: B:9:0x004f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zzi(zzii zziiVar, Object obj) {
        boolean z;
        zziiVar.zzg();
        zziiVar.zzd();
        byte[] bArr = zzjc.zzd;
        obj.getClass();
        zzmb zzmbVar = zzmb.zza;
        zzmc zzmcVar = zzmc.zza;
        switch (r1.zza()) {
            case zza:
                z = obj instanceof Integer;
                break;
            case zzb:
                z = obj instanceof Long;
                break;
            case zzc:
                z = obj instanceof Float;
                break;
            case zzd:
                z = obj instanceof Double;
                break;
            case zze:
                z = obj instanceof Boolean;
                break;
            case zzf:
                z = obj instanceof String;
                break;
            case zzg:
                if (!(obj instanceof zzgw)) {
                    break;
                }
                if (obj instanceof zzjj) {
                    this.zzd = true;
                }
                this.zza.put(zziiVar, obj);
                return;
            case zzh:
                if (!(obj instanceof Integer)) {
                    break;
                }
                if (obj instanceof zzjj) {
                }
                this.zza.put(zziiVar, obj);
                return;
            case zzi:
                if (!(obj instanceof zzke)) {
                    break;
                }
                if (obj instanceof zzjj) {
                }
                this.zza.put(zziiVar, obj);
                return;
            default:
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(zziiVar.zza()), zziiVar.zzd().zza(), obj.getClass().getName()));
        }
    }

    public final boolean zzj() {
        return this.zzc;
    }

    public final boolean zzk() {
        for (int i = 0; i < this.zza.zzb(); i++) {
            if (!zzn(this.zza.zzg(i))) {
                return false;
            }
        }
        Iterator it = this.zza.zzc().iterator();
        while (it.hasNext()) {
            if (!zzn((Map.Entry) it.next())) {
                return false;
            }
        }
        return true;
    }
}
