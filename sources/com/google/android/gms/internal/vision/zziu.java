package com.google.android.gms.internal.vision;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
final class zziu {
    private static final zziu zzd = new zziu(true);
    final zzlh zza;
    private boolean zzb;
    private boolean zzc;

    private zziu() {
        this.zza = zzlh.zza(16);
    }

    private zziu(zzlh zzlhVar) {
        this.zza = zzlhVar;
        zzb();
    }

    private zziu(boolean z) {
        this(zzlh.zza(0));
        zzb();
    }

    static int zza(zzml zzmlVar, int i, Object obj) {
        int zze = zzii.zze(i);
        if (zzmlVar == zzml.zzj) {
            zzjf.zza((zzkk) obj);
            zze <<= 1;
        }
        return zze + zza(zzmlVar, obj);
    }

    private static int zza(zzml zzmlVar, Object obj) {
        switch (zzit.zzb[zzmlVar.ordinal()]) {
            case 1:
                return zzii.zzb(((Double) obj).doubleValue());
            case 2:
                return zzii.zzb(((Float) obj).floatValue());
            case 3:
                return zzii.zzd(((Long) obj).longValue());
            case 4:
                return zzii.zze(((Long) obj).longValue());
            case 5:
                return zzii.zzf(((Integer) obj).intValue());
            case 6:
                return zzii.zzg(((Long) obj).longValue());
            case 7:
                return zzii.zzi(((Integer) obj).intValue());
            case 8:
                return zzii.zzb(((Boolean) obj).booleanValue());
            case 9:
                return zzii.zzc((zzkk) obj);
            case 10:
                return zzii.zzb((zzkk) obj);
            case 11:
                return obj instanceof zzht ? zzii.zzb((zzht) obj) : zzii.zzb((String) obj);
            case 12:
                return obj instanceof zzht ? zzii.zzb((zzht) obj) : zzii.zzb((byte[]) obj);
            case 13:
                return zzii.zzg(((Integer) obj).intValue());
            case 14:
                return zzii.zzj(((Integer) obj).intValue());
            case 15:
                return zzii.zzh(((Long) obj).longValue());
            case 16:
                return zzii.zzh(((Integer) obj).intValue());
            case 17:
                return zzii.zzf(((Long) obj).longValue());
            case 18:
                return obj instanceof zzje ? zzii.zzk(((zzje) obj).zza()) : zzii.zzk(((Integer) obj).intValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    private static boolean zza(Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        throw null;
    }

    private final void zzb(Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        entry.getValue();
        throw null;
    }

    public static int zzc(zziw zziwVar, Object obj) {
        zzml zzb = zziwVar.zzb();
        int zza = zziwVar.zza();
        if (!zziwVar.zzd()) {
            return zza(zzb, zza, obj);
        }
        int i = 0;
        List list = (List) obj;
        if (zziwVar.zze()) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                i += zza(zzb, it.next());
            }
            return zzii.zze(zza) + i + zzii.zzl(i);
        }
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            i += zza(zzb, zza, it2.next());
        }
        return i;
    }

    private static int zzc(Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        entry.getValue();
        throw null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x001c, code lost:
    
        if ((r6 instanceof com.google.android.gms.internal.vision.zzkk) == false) goto L4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0027, code lost:
    
        if ((r6 instanceof com.google.android.gms.internal.vision.zzje) == false) goto L4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0030, code lost:
    
        if ((r6 instanceof byte[]) == false) goto L4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void zzd(zziw zziwVar, Object obj) {
        boolean z;
        zzml zzb = zziwVar.zzb();
        zzjf.zza(obj);
        switch (zzit.zza[zzb.zza().ordinal()]) {
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
                if (!(obj instanceof zzht)) {
                    break;
                }
                z = true;
                break;
            case 8:
                if (!(obj instanceof Integer)) {
                    break;
                }
                z = true;
                break;
            case 9:
                break;
            default:
                z = false;
                break;
        }
        if (!z) {
            throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(zziwVar.zza()), zziwVar.zzb().zza(), obj.getClass().getName()));
        }
    }

    public final /* synthetic */ Object clone() {
        zziu zziuVar = new zziu();
        for (int i = 0; i < this.zza.zzc(); i++) {
            Map.Entry zzb = this.zza.zzb(i);
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzb.getKey());
            zziuVar.zza((zziw) null, zzb.getValue());
        }
        for (Map.Entry entry : this.zza.zzd()) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
            zziuVar.zza((zziw) null, entry.getValue());
        }
        zziuVar.zzc = this.zzc;
        return zziuVar;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zziu) {
            return this.zza.equals(((zziu) obj).zza);
        }
        return false;
    }

    public final int hashCode() {
        return this.zza.hashCode();
    }

    public final void zza(zziu zziuVar) {
        for (int i = 0; i < zziuVar.zza.zzc(); i++) {
            zzb(zziuVar.zza.zzb(i));
        }
        Iterator it = zziuVar.zza.zzd().iterator();
        while (it.hasNext()) {
            zzb((Map.Entry) it.next());
        }
    }

    public final void zza(zziw zziwVar, Object obj) {
        if (!zziwVar.zzd()) {
            zzd(zziwVar, obj);
        } else {
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
                zzd(zziwVar, obj2);
            }
            obj = arrayList;
        }
        this.zza.put(zziwVar, obj);
    }

    public final void zzb() {
        if (this.zzb) {
            return;
        }
        this.zza.zza();
        this.zzb = true;
    }

    public final Iterator zzd() {
        return this.zzc ? new zzjq(this.zza.entrySet().iterator()) : this.zza.entrySet().iterator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Iterator zze() {
        return this.zzc ? new zzjq(this.zza.zze().iterator()) : this.zza.zze().iterator();
    }

    public final boolean zzf() {
        for (int i = 0; i < this.zza.zzc(); i++) {
            if (!zza(this.zza.zzb(i))) {
                return false;
            }
        }
        Iterator it = this.zza.zzd().iterator();
        while (it.hasNext()) {
            if (!zza((Map.Entry) it.next())) {
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
        Iterator it = this.zza.zzd().iterator();
        while (it.hasNext()) {
            i += zzc((Map.Entry) it.next());
        }
        return i;
    }
}
