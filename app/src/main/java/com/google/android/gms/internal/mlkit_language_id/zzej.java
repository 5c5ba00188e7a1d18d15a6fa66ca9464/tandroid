package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
final class zzej<T extends zzel<T>> {
    private static final zzej zzd = new zzej(true);
    final zzgq<T, Object> zza;
    private boolean zzb;
    private boolean zzc;

    private zzej() {
        this.zza = zzgq.zza(16);
    }

    private zzej(boolean z) {
        this(zzgq.zza(0));
        zzb();
    }

    private zzej(zzgq<T, Object> zzgqVar) {
        this.zza = zzgqVar;
        zzb();
    }

    public static <T extends zzel<T>> zzej<T> zza() {
        return zzd;
    }

    public final void zzb() {
        if (this.zzb) {
            return;
        }
        this.zza.zza();
        this.zzb = true;
    }

    public final boolean zzc() {
        return this.zzb;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzej) {
            return this.zza.equals(((zzej) obj).zza);
        }
        return false;
    }

    public final int hashCode() {
        return this.zza.hashCode();
    }

    public final Iterator<Map.Entry<T, Object>> zzd() {
        if (this.zzc) {
            return new zzff(this.zza.entrySet().iterator());
        }
        return this.zza.entrySet().iterator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Iterator<Map.Entry<T, Object>> zze() {
        if (this.zzc) {
            return new zzff(this.zza.zze().iterator());
        }
        return this.zza.zze().iterator();
    }

    private final Object zza(T t) {
        Object obj = this.zza.get(t);
        if (obj instanceof zzfa) {
            zzfa zzfaVar = (zzfa) obj;
            return zzfa.zza();
        }
        return obj;
    }

    private final void zzb(T t, Object obj) {
        if (t.zzd()) {
            if (!(obj instanceof List)) {
                throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
            }
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List) obj);
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj2 = arrayList.get(i);
                i++;
                zza(t.zzb(), obj2);
            }
            obj = arrayList;
        } else {
            zza(t.zzb(), obj);
        }
        if (obj instanceof zzfa) {
            this.zzc = true;
        }
        this.zza.zza((zzgq<T, Object>) t, (T) obj);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x001c, code lost:
        if ((r3 instanceof com.google.android.gms.internal.mlkit_language_id.zzfa) == false) goto L3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0025, code lost:
        if ((r3 instanceof com.google.android.gms.internal.mlkit_language_id.zzet) == false) goto L3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x002e, code lost:
        if ((r3 instanceof byte[]) == false) goto L3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void zza(zzhv zzhvVar, Object obj) {
        zzeq.zza(obj);
        boolean z = true;
        switch (zzei.zza[zzhvVar.zza().ordinal()]) {
            case 1:
                z = obj instanceof Integer;
                break;
            case 2:
                z = obj instanceof Long;
                break;
            case 3:
                z = obj instanceof Float;
                break;
            case 4:
                z = obj instanceof Double;
                break;
            case 5:
                z = obj instanceof Boolean;
                break;
            case 6:
                z = obj instanceof String;
                break;
            case 7:
                if (!(obj instanceof zzdn)) {
                    break;
                }
                break;
            case 8:
                if (!(obj instanceof Integer)) {
                    break;
                }
                break;
            case 9:
                if (!(obj instanceof zzfz)) {
                    break;
                }
                break;
            default:
                z = false;
                break;
        }
        if (z) {
            return;
        }
        throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
    }

    public final boolean zzf() {
        for (int i = 0; i < this.zza.zzc(); i++) {
            if (!zza((Map.Entry) this.zza.zzb(i))) {
                return false;
            }
        }
        for (Map.Entry<T, Object> entry : this.zza.zzd()) {
            if (!zza((Map.Entry) entry)) {
                return false;
            }
        }
        return true;
    }

    private static <T extends zzel<T>> boolean zza(Map.Entry<T, Object> entry) {
        T key = entry.getKey();
        if (key.zzc() == zzhy.MESSAGE) {
            if (key.zzd()) {
                for (zzfz zzfzVar : (List) entry.getValue()) {
                    if (!zzfzVar.zzi()) {
                        return false;
                    }
                }
            } else {
                Object value = entry.getValue();
                if (value instanceof zzfz) {
                    if (!((zzfz) value).zzi()) {
                        return false;
                    }
                } else if (value instanceof zzfa) {
                    return true;
                } else {
                    throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
                }
            }
        }
        return true;
    }

    public final void zza(zzej<T> zzejVar) {
        for (int i = 0; i < zzejVar.zza.zzc(); i++) {
            zzb(zzejVar.zza.zzb(i));
        }
        for (Map.Entry<T, Object> entry : zzejVar.zza.zzd()) {
            zzb(entry);
        }
    }

    private static Object zza(Object obj) {
        if (obj instanceof zzgf) {
            return ((zzgf) obj).zza();
        }
        if (!(obj instanceof byte[])) {
            return obj;
        }
        byte[] bArr = (byte[]) obj;
        byte[] bArr2 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }

    private final void zzb(Map.Entry<T, Object> entry) {
        zzfz zzg;
        T key = entry.getKey();
        Object value = entry.getValue();
        if (value instanceof zzfa) {
            zzfa zzfaVar = (zzfa) value;
            value = zzfa.zza();
        }
        if (key.zzd()) {
            Object zza = zza((zzej<T>) key);
            if (zza == null) {
                zza = new ArrayList();
            }
            for (Object obj : (List) value) {
                ((List) zza).add(zza(obj));
            }
            this.zza.zza((zzgq<T, Object>) key, (T) zza);
        } else if (key.zzc() == zzhy.MESSAGE) {
            Object zza2 = zza((zzej<T>) key);
            if (zza2 == null) {
                this.zza.zza((zzgq<T, Object>) key, (T) zza(value));
                return;
            }
            if (zza2 instanceof zzgf) {
                zzg = key.zza((zzgf) zza2, (zzgf) value);
            } else {
                zzg = key.zza(((zzfz) zza2).zzm(), (zzfz) value).zzg();
            }
            this.zza.zza((zzgq<T, Object>) key, (T) zzg);
        } else {
            this.zza.zza((zzgq<T, Object>) key, (T) zza(value));
        }
    }

    public final int zzg() {
        int i = 0;
        for (int i2 = 0; i2 < this.zza.zzc(); i2++) {
            i += zzc(this.zza.zzb(i2));
        }
        for (Map.Entry<T, Object> entry : this.zza.zzd()) {
            i += zzc(entry);
        }
        return i;
    }

    private static int zzc(Map.Entry<T, Object> entry) {
        T key = entry.getKey();
        Object value = entry.getValue();
        if (key.zzc() == zzhy.MESSAGE && !key.zzd() && !key.zze()) {
            if (value instanceof zzfa) {
                return zzea.zzb(entry.getKey().zza(), (zzfa) value);
            }
            return zzea.zzb(entry.getKey().zza(), (zzfz) value);
        }
        return zza((zzel<?>) key, value);
    }

    static int zza(zzhv zzhvVar, int i, Object obj) {
        int zze = zzea.zze(i);
        if (zzhvVar == zzhv.GROUP) {
            zzeq.zza((zzfz) obj);
            zze <<= 1;
        }
        return zze + zzb(zzhvVar, obj);
    }

    private static int zzb(zzhv zzhvVar, Object obj) {
        switch (zzei.zzb[zzhvVar.ordinal()]) {
            case 1:
                return zzea.zzb(((Double) obj).doubleValue());
            case 2:
                return zzea.zzb(((Float) obj).floatValue());
            case 3:
                return zzea.zzd(((Long) obj).longValue());
            case 4:
                return zzea.zze(((Long) obj).longValue());
            case 5:
                return zzea.zzf(((Integer) obj).intValue());
            case 6:
                return zzea.zzg(((Long) obj).longValue());
            case 7:
                return zzea.zzi(((Integer) obj).intValue());
            case 8:
                return zzea.zzb(((Boolean) obj).booleanValue());
            case 9:
                return zzea.zzc((zzfz) obj);
            case 10:
                if (obj instanceof zzfa) {
                    return zzea.zza((zzfa) obj);
                }
                return zzea.zzb((zzfz) obj);
            case 11:
                if (obj instanceof zzdn) {
                    return zzea.zzb((zzdn) obj);
                }
                return zzea.zzb((String) obj);
            case 12:
                if (obj instanceof zzdn) {
                    return zzea.zzb((zzdn) obj);
                }
                return zzea.zzb((byte[]) obj);
            case 13:
                return zzea.zzg(((Integer) obj).intValue());
            case 14:
                return zzea.zzj(((Integer) obj).intValue());
            case 15:
                return zzea.zzh(((Long) obj).longValue());
            case 16:
                return zzea.zzh(((Integer) obj).intValue());
            case 17:
                return zzea.zzf(((Long) obj).longValue());
            case 18:
                if (obj instanceof zzet) {
                    return zzea.zzk(((zzet) obj).zza());
                }
                return zzea.zzk(((Integer) obj).intValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    public static int zza(zzel<?> zzelVar, Object obj) {
        zzhv zzb = zzelVar.zzb();
        int zza = zzelVar.zza();
        if (zzelVar.zzd()) {
            int i = 0;
            if (zzelVar.zze()) {
                for (Object obj2 : (List) obj) {
                    i += zzb(zzb, obj2);
                }
                return zzea.zze(zza) + i + zzea.zzl(i);
            }
            for (Object obj3 : (List) obj) {
                i += zza(zzb, zza, obj3);
            }
            return i;
        }
        return zza(zzb, zza, obj);
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzej zzejVar = new zzej();
        for (int i = 0; i < this.zza.zzc(); i++) {
            Map.Entry<T, Object> zzb = this.zza.zzb(i);
            zzejVar.zzb((zzej) zzb.getKey(), zzb.getValue());
        }
        for (Map.Entry<T, Object> entry : this.zza.zzd()) {
            zzejVar.zzb((zzej) entry.getKey(), entry.getValue());
        }
        zzejVar.zzc = this.zzc;
        return zzejVar;
    }
}
