package com.google.android.gms.internal.clearcut;

import com.google.android.gms.internal.clearcut.zzca;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.R;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzby<FieldDescriptorType extends zzca<FieldDescriptorType>> {
    private static final zzby zzgw = new zzby(true);
    private boolean zzgu;
    private boolean zzgv = false;
    private final zzei<FieldDescriptorType, Object> zzgt = zzei.zzaj(16);

    private zzby() {
    }

    private zzby(boolean z) {
        zzv();
    }

    static int zza(zzfl zzflVar, int i, Object obj) {
        int zzr = zzbn.zzr(i);
        if (zzflVar == zzfl.GROUP) {
            zzci.zzf((zzdo) obj);
            zzr <<= 1;
        }
        return zzr + zzb(zzflVar, obj);
    }

    private final Object zza(FieldDescriptorType fielddescriptortype) {
        Object obj = this.zzgt.get(fielddescriptortype);
        return obj instanceof zzcr ? zzcr.zzbr() : obj;
    }

    private final void zza(FieldDescriptorType fielddescriptortype, Object obj) {
        if (!fielddescriptortype.zzaw()) {
            zza(fielddescriptortype.zzau(), obj);
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
                zza(fielddescriptortype.zzau(), obj2);
            }
            obj = arrayList;
        }
        if (obj instanceof zzcr) {
            this.zzgv = true;
        }
        this.zzgt.zza((zzei<FieldDescriptorType, Object>) fielddescriptortype, (FieldDescriptorType) obj);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0026, code lost:
        if ((r3 instanceof com.google.android.gms.internal.clearcut.zzcj) == false) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x002f, code lost:
        if ((r3 instanceof byte[]) == false) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x001b, code lost:
        if ((r3 instanceof com.google.android.gms.internal.clearcut.zzcr) == false) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x001e, code lost:
        r0 = false;
     */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0046 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0047  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void zza(zzfl zzflVar, Object obj) {
        zzci.checkNotNull(obj);
        boolean z = true;
        boolean z2 = false;
        switch (zzbz.zzgx[zzflVar.zzek().ordinal()]) {
            case 1:
                z = obj instanceof Integer;
                z2 = z;
                if (z2) {
                    throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
                }
                return;
            case 2:
                z = obj instanceof Long;
                z2 = z;
                if (z2) {
                }
                break;
            case 3:
                z = obj instanceof Float;
                z2 = z;
                if (z2) {
                }
                break;
            case 4:
                z = obj instanceof Double;
                z2 = z;
                if (z2) {
                }
                break;
            case 5:
                z = obj instanceof Boolean;
                z2 = z;
                if (z2) {
                }
                break;
            case 6:
                z = obj instanceof String;
                z2 = z;
                if (z2) {
                }
                break;
            case 7:
                if (!(obj instanceof zzbb)) {
                    break;
                }
                z2 = z;
                if (z2) {
                }
                break;
            case 8:
                if (!(obj instanceof Integer)) {
                    break;
                }
                z2 = z;
                if (z2) {
                }
                break;
            case 9:
                if (!(obj instanceof zzdo)) {
                    break;
                }
                z2 = z;
                if (z2) {
                }
                break;
            default:
                if (z2) {
                }
                break;
        }
    }

    public static <T extends zzca<T>> zzby<T> zzar() {
        return zzgw;
    }

    private static int zzb(zzca<?> zzcaVar, Object obj) {
        zzfl zzau = zzcaVar.zzau();
        int zzc = zzcaVar.zzc();
        if (zzcaVar.zzaw()) {
            int i = 0;
            List<Object> list = (List) obj;
            if (zzcaVar.zzax()) {
                for (Object obj2 : list) {
                    i += zzb(zzau, obj2);
                }
                return zzbn.zzr(zzc) + i + zzbn.zzz(i);
            }
            for (Object obj3 : list) {
                i += zza(zzau, zzc, obj3);
            }
            return i;
        }
        return zza(zzau, zzc, obj);
    }

    private static int zzb(zzfl zzflVar, Object obj) {
        switch (zzbz.zzgq[zzflVar.ordinal()]) {
            case 1:
                return zzbn.zzb(((Double) obj).doubleValue());
            case 2:
                return zzbn.zzb(((Float) obj).floatValue());
            case 3:
                return zzbn.zze(((Long) obj).longValue());
            case 4:
                return zzbn.zzf(((Long) obj).longValue());
            case 5:
                return zzbn.zzs(((Integer) obj).intValue());
            case 6:
                return zzbn.zzh(((Long) obj).longValue());
            case 7:
                return zzbn.zzv(((Integer) obj).intValue());
            case 8:
                return zzbn.zzb(((Boolean) obj).booleanValue());
            case 9:
                return zzbn.zzd((zzdo) obj);
            case 10:
                return obj instanceof zzcr ? zzbn.zza((zzcr) obj) : zzbn.zzc((zzdo) obj);
            case 11:
                return obj instanceof zzbb ? zzbn.zzb((zzbb) obj) : zzbn.zzh((String) obj);
            case 12:
                return obj instanceof zzbb ? zzbn.zzb((zzbb) obj) : zzbn.zzd((byte[]) obj);
            case 13:
                return zzbn.zzt(((Integer) obj).intValue());
            case 14:
                return zzbn.zzw(((Integer) obj).intValue());
            case 15:
                return zzbn.zzi(((Long) obj).longValue());
            case 16:
                return zzbn.zzu(((Integer) obj).intValue());
            case 17:
                return zzbn.zzg(((Long) obj).longValue());
            case R.styleable.MapAttrs_uiScrollGesturesDuringRotateOrZoom /* 18 */:
                return obj instanceof zzcj ? zzbn.zzx(((zzcj) obj).zzc()) : zzbn.zzx(((Integer) obj).intValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    private static boolean zzb(Map.Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorType key = entry.getKey();
        if (key.zzav() == zzfq.MESSAGE) {
            boolean zzaw = key.zzaw();
            Object value = entry.getValue();
            if (zzaw) {
                for (zzdo zzdoVar : (List) value) {
                    if (!zzdoVar.isInitialized()) {
                        return false;
                    }
                }
            } else if (!(value instanceof zzdo)) {
                if (!(value instanceof zzcr)) {
                    throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
                }
                return true;
            } else if (!((zzdo) value).isInitialized()) {
                return false;
            }
        }
        return true;
    }

    private final void zzc(Map.Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorType key = entry.getKey();
        Object value = entry.getValue();
        if (value instanceof zzcr) {
            value = zzcr.zzbr();
        }
        if (key.zzaw()) {
            Object zza = zza((zzby<FieldDescriptorType>) key);
            if (zza == null) {
                zza = new ArrayList();
            }
            for (Object obj : (List) value) {
                ((List) zza).add(zzd(obj));
            }
            this.zzgt.zza((zzei<FieldDescriptorType, Object>) key, (FieldDescriptorType) zza);
        } else if (key.zzav() != zzfq.MESSAGE) {
            this.zzgt.zza((zzei<FieldDescriptorType, Object>) key, (FieldDescriptorType) zzd(value));
        } else {
            Object zza2 = zza((zzby<FieldDescriptorType>) key);
            if (zza2 == null) {
                this.zzgt.zza((zzei<FieldDescriptorType, Object>) key, (FieldDescriptorType) zzd(value));
            } else {
                this.zzgt.zza((zzei<FieldDescriptorType, Object>) key, (FieldDescriptorType) (zza2 instanceof zzdv ? key.zza((zzdv) zza2, (zzdv) value) : key.zza(((zzdo) zza2).zzbc(), (zzdo) value).zzbj()));
            }
        }
    }

    private static int zzd(Map.Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorType key = entry.getKey();
        Object value = entry.getValue();
        if (key.zzav() != zzfq.MESSAGE || key.zzaw() || key.zzax()) {
            return zzb((zzca<?>) key, value);
        }
        boolean z = value instanceof zzcr;
        int zzc = entry.getKey().zzc();
        return z ? zzbn.zzb(zzc, (zzcr) value) : zzbn.zzd(zzc, (zzdo) value);
    }

    private static Object zzd(Object obj) {
        if (obj instanceof zzdv) {
            return ((zzdv) obj).zzci();
        }
        if (!(obj instanceof byte[])) {
            return obj;
        }
        byte[] bArr = (byte[]) obj;
        byte[] bArr2 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzby zzbyVar = new zzby();
        for (int i = 0; i < this.zzgt.zzdr(); i++) {
            Map.Entry<FieldDescriptorType, Object> zzak = this.zzgt.zzak(i);
            zzbyVar.zza((zzby) zzak.getKey(), zzak.getValue());
        }
        for (Map.Entry<FieldDescriptorType, Object> entry : this.zzgt.zzds()) {
            zzbyVar.zza((zzby) entry.getKey(), entry.getValue());
        }
        zzbyVar.zzgv = this.zzgv;
        return zzbyVar;
    }

    public final Iterator<Map.Entry<FieldDescriptorType, Object>> descendingIterator() {
        return this.zzgv ? new zzcu(this.zzgt.zzdt().iterator()) : this.zzgt.zzdt().iterator();
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzby) {
            return this.zzgt.equals(((zzby) obj).zzgt);
        }
        return false;
    }

    public final int hashCode() {
        return this.zzgt.hashCode();
    }

    public final boolean isEmpty() {
        return this.zzgt.isEmpty();
    }

    public final boolean isImmutable() {
        return this.zzgu;
    }

    public final boolean isInitialized() {
        for (int i = 0; i < this.zzgt.zzdr(); i++) {
            if (!zzb(this.zzgt.zzak(i))) {
                return false;
            }
        }
        for (Map.Entry<FieldDescriptorType, Object> entry : this.zzgt.zzds()) {
            if (!zzb(entry)) {
                return false;
            }
        }
        return true;
    }

    public final Iterator<Map.Entry<FieldDescriptorType, Object>> iterator() {
        return this.zzgv ? new zzcu(this.zzgt.entrySet().iterator()) : this.zzgt.entrySet().iterator();
    }

    public final void zza(zzby<FieldDescriptorType> zzbyVar) {
        for (int i = 0; i < zzbyVar.zzgt.zzdr(); i++) {
            zzc(zzbyVar.zzgt.zzak(i));
        }
        for (Map.Entry<FieldDescriptorType, Object> entry : zzbyVar.zzgt.zzds()) {
            zzc(entry);
        }
    }

    public final int zzas() {
        int i = 0;
        for (int i2 = 0; i2 < this.zzgt.zzdr(); i2++) {
            Map.Entry<FieldDescriptorType, Object> zzak = this.zzgt.zzak(i2);
            i += zzb((zzca<?>) zzak.getKey(), zzak.getValue());
        }
        for (Map.Entry<FieldDescriptorType, Object> entry : this.zzgt.zzds()) {
            i += zzb((zzca<?>) entry.getKey(), entry.getValue());
        }
        return i;
    }

    public final int zzat() {
        int i = 0;
        for (int i2 = 0; i2 < this.zzgt.zzdr(); i2++) {
            i += zzd((Map.Entry) this.zzgt.zzak(i2));
        }
        for (Map.Entry<FieldDescriptorType, Object> entry : this.zzgt.zzds()) {
            i += zzd((Map.Entry) entry);
        }
        return i;
    }

    public final void zzv() {
        if (this.zzgu) {
            return;
        }
        this.zzgt.zzv();
        this.zzgu = true;
    }
}
