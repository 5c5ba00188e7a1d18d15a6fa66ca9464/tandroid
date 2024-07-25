package com.google.android.gms.internal.play_billing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
final class zzbs {
    private static final zzbs zzb = new zzbs(true);
    final zzec zza = new zzds(16);
    private boolean zzc;
    private boolean zzd;

    private zzbs() {
    }

    public static zzbs zza() {
        throw null;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static final void zzd(zzbr zzbrVar, Object obj) {
        boolean z;
        zzew zzb2 = zzbrVar.zzb();
        byte[] bArr = zzcg.zzd;
        Objects.requireNonNull(obj);
        zzew zzewVar = zzew.zza;
        zzex zzexVar = zzex.zza;
        switch (zzb2.zza().ordinal()) {
            case 0:
                z = obj instanceof Integer;
                if (z) {
                    return;
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(zzbrVar.zza()), zzbrVar.zzb().zza(), obj.getClass().getName()));
            case 1:
                z = obj instanceof Long;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(zzbrVar.zza()), zzbrVar.zzb().zza(), obj.getClass().getName()));
            case 2:
                z = obj instanceof Float;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(zzbrVar.zza()), zzbrVar.zzb().zza(), obj.getClass().getName()));
            case 3:
                z = obj instanceof Double;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(zzbrVar.zza()), zzbrVar.zzb().zza(), obj.getClass().getName()));
            case 4:
                z = obj instanceof Boolean;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(zzbrVar.zza()), zzbrVar.zzb().zza(), obj.getClass().getName()));
            case 5:
                z = obj instanceof String;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(zzbrVar.zza()), zzbrVar.zzb().zza(), obj.getClass().getName()));
            case 6:
                if ((obj instanceof zzba) || (obj instanceof byte[])) {
                    return;
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(zzbrVar.zza()), zzbrVar.zzb().zza(), obj.getClass().getName()));
            case 7:
                if (obj instanceof Integer) {
                    return;
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(zzbrVar.zza()), zzbrVar.zzb().zza(), obj.getClass().getName()));
            case 8:
                if (obj instanceof zzdf) {
                    return;
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(zzbrVar.zza()), zzbrVar.zzb().zza(), obj.getClass().getName()));
            default:
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(zzbrVar.zza()), zzbrVar.zzb().zza(), obj.getClass().getName()));
        }
    }

    public final /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzbs zzbsVar = new zzbs();
        for (int i = 0; i < this.zza.zzb(); i++) {
            Map.Entry zzg = this.zza.zzg(i);
            zzbsVar.zzc((zzbr) zzg.getKey(), zzg.getValue());
        }
        for (Map.Entry entry : this.zza.zzc()) {
            zzbsVar.zzc((zzbr) entry.getKey(), entry.getValue());
        }
        zzbsVar.zzd = this.zzd;
        return zzbsVar;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzbs) {
            return this.zza.equals(((zzbs) obj).zza);
        }
        return false;
    }

    public final int hashCode() {
        return this.zza.hashCode();
    }

    public final void zzb() {
        if (this.zzc) {
            return;
        }
        for (int i = 0; i < this.zza.zzb(); i++) {
            Map.Entry zzg = this.zza.zzg(i);
            if (zzg.getValue() instanceof zzcb) {
                ((zzcb) zzg.getValue()).zzn();
            }
        }
        this.zza.zza();
        this.zzc = true;
    }

    public final void zzc(zzbr zzbrVar, Object obj) {
        if (zzbrVar.zzc()) {
            if (!(obj instanceof List)) {
                throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
            }
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List) obj);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                zzd(zzbrVar, arrayList.get(i));
            }
            obj = arrayList;
        } else {
            zzd(zzbrVar, obj);
        }
        this.zza.put(zzbrVar, obj);
    }

    private zzbs(boolean z) {
        zzb();
        zzb();
    }
}
