package com.google.android.gms.internal.icing;

import com.google.android.gms.internal.icing.zzct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.firebase:firebase-appindexing@@20.0.0 */
/* loaded from: classes3.dex */
public final class zzcu<T extends zzct<T>> {
    private static final zzcu zzd = new zzcu(true);
    final zzez<T, Object> zza = new zzes(16);
    private boolean zzb;
    private boolean zzc;

    private zzcu() {
    }

    public static <T extends zzct<T>> zzcu<T> zza() {
        throw null;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static final void zzd(T t, Object obj) {
        boolean z;
        zzfs zzb = t.zzb();
        zzdh.zza(obj);
        zzfs zzfsVar = zzfs.DOUBLE;
        zzft zzftVar = zzft.INT;
        switch (zzb.zza().ordinal()) {
            case 0:
                z = obj instanceof Integer;
                if (z) {
                    return;
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 1:
                z = obj instanceof Long;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 2:
                z = obj instanceof Float;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 3:
                z = obj instanceof Double;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 4:
                z = obj instanceof Boolean;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 5:
                z = obj instanceof String;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 6:
                if ((obj instanceof zzcf) || (obj instanceof byte[])) {
                    return;
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 7:
                if ((obj instanceof Integer) || (obj instanceof zzde)) {
                    return;
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 8:
                if ((obj instanceof zzee) || (obj instanceof zzdl)) {
                    return;
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            default:
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
        }
    }

    public final /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzcu zzcuVar = new zzcu();
        for (int i = 0; i < this.zza.zzc(); i++) {
            Map.Entry<T, Object> zzd2 = this.zza.zzd(i);
            zzcuVar.zzc(zzd2.getKey(), zzd2.getValue());
        }
        for (Map.Entry<T, Object> entry : this.zza.zze()) {
            zzcuVar.zzc(entry.getKey(), entry.getValue());
        }
        zzcuVar.zzc = this.zzc;
        return zzcuVar;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzcu) {
            return this.zza.equals(((zzcu) obj).zza);
        }
        return false;
    }

    public final int hashCode() {
        return this.zza.hashCode();
    }

    public final void zzb() {
        if (this.zzb) {
            return;
        }
        this.zza.zza();
        this.zzb = true;
    }

    public final void zzc(T t, Object obj) {
        if (t.zzc()) {
            if (!(obj instanceof List)) {
                throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
            }
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List) obj);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                zzd(t, arrayList.get(i));
            }
            obj = arrayList;
        } else {
            zzd(t, obj);
        }
        if (obj instanceof zzdl) {
            this.zzc = true;
        }
        this.zza.put(t, obj);
    }

    private zzcu(boolean z) {
        zzb();
        zzb();
    }
}
