package com.google.android.gms.internal.mlkit_language_id;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
final class zzej {
    private static final zzej zzd = new zzej(true);
    final zzgq zza;
    private boolean zzb;
    private boolean zzc;

    private zzej() {
        this.zza = zzgq.zza(16);
    }

    private zzej(zzgq zzgqVar) {
        this.zza = zzgqVar;
        zzb();
    }

    private zzej(boolean z) {
        this(zzgq.zza(0));
        zzb();
    }

    public static int zza(zzel zzelVar, Object obj) {
        zzhv zzb = zzelVar.zzb();
        int zza = zzelVar.zza();
        if (zzelVar.zzd()) {
            int i = 0;
            List<Object> list = (List) obj;
            if (zzelVar.zze()) {
                for (Object obj2 : list) {
                    i += zzb(zzb, obj2);
                }
                return zzea.zze(zza) + i + zzea.zzl(i);
            }
            for (Object obj3 : list) {
                i += zza(zzb, zza, obj3);
            }
            return i;
        }
        return zza(zzb, zza, obj);
    }

    static int zza(zzhv zzhvVar, int i, Object obj) {
        int zze = zzea.zze(i);
        if (zzhvVar == zzhv.zzj) {
            zzeq.zza((zzfz) obj);
            zze <<= 1;
        }
        return zze + zzb(zzhvVar, obj);
    }

    public static zzej zza() {
        return zzd;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0021, code lost:
        if ((r3 instanceof com.google.android.gms.internal.mlkit_language_id.zzet) == false) goto L3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x002a, code lost:
        if ((r3 instanceof byte[]) == false) goto L3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0018, code lost:
        if ((r3 instanceof com.google.android.gms.internal.mlkit_language_id.zzfz) == false) goto L3;
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
                break;
            default:
                z = false;
                break;
        }
        if (!z) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
    }

    private static boolean zza(Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        throw null;
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
                return zzea.zzb((zzfz) obj);
            case 11:
                return obj instanceof zzdn ? zzea.zzb((zzdn) obj) : zzea.zzb((String) obj);
            case 12:
                return obj instanceof zzdn ? zzea.zzb((zzdn) obj) : zzea.zzb((byte[]) obj);
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
                return obj instanceof zzet ? zzea.zzk(((zzet) obj).zza()) : zzea.zzk(((Integer) obj).intValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    private final void zzb(zzel zzelVar, Object obj) {
        if (!zzelVar.zzd()) {
            zza(zzelVar.zzb(), obj);
        } else if (!(obj instanceof List)) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List) obj);
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj2 = arrayList.get(i);
                i++;
                zza(zzelVar.zzb(), obj2);
            }
            obj = arrayList;
        }
        this.zza.put(zzelVar, obj);
    }

    private final void zzb(Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        entry.getValue();
        throw null;
    }

    private static int zzc(Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        entry.getValue();
        throw null;
    }

    public final /* synthetic */ Object clone() {
        zzej zzejVar = new zzej();
        for (int i = 0; i < this.zza.zzc(); i++) {
            Map.Entry zzb = this.zza.zzb(i);
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzb.getKey());
            zzejVar.zzb((zzel) null, zzb.getValue());
        }
        for (Map.Entry entry : this.zza.zzd()) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
            zzejVar.zzb((zzel) null, entry.getValue());
        }
        zzejVar.zzc = this.zzc;
        return zzejVar;
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

    public final void zza(zzej zzejVar) {
        for (int i = 0; i < zzejVar.zza.zzc(); i++) {
            zzb(zzejVar.zza.zzb(i));
        }
        for (Map.Entry entry : zzejVar.zza.zzd()) {
            zzb(entry);
        }
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

    public final Iterator zzd() {
        return this.zzc ? new zzff(this.zza.entrySet().iterator()) : this.zza.entrySet().iterator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Iterator zze() {
        return this.zzc ? new zzff(this.zza.zze().iterator()) : this.zza.zze().iterator();
    }

    public final boolean zzf() {
        for (int i = 0; i < this.zza.zzc(); i++) {
            if (!zza(this.zza.zzb(i))) {
                return false;
            }
        }
        for (Map.Entry entry : this.zza.zzd()) {
            if (!zza(entry)) {
                return false;
            }
        }
        return true;
    }

    public final int zzg() {
        int i = 0;
        for (int i2 = 0; i2 < this.zza.zzc(); i2++) {
            i += zzc(this.zza.zzb(i2));
        }
        for (Map.Entry entry : this.zza.zzd()) {
            i += zzc(entry);
        }
        return i;
    }
}
