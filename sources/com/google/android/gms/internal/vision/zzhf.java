package com.google.android.gms.internal.vision;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: classes.dex */
public abstract class zzhf implements zzkk {
    protected int zza = 0;

    /* JADX INFO: Access modifiers changed from: protected */
    public static void zza(Iterable iterable, List list) {
        zzjf.zza(iterable);
        if (iterable instanceof zzjv) {
            List zzd = ((zzjv) iterable).zzd();
            zzjv zzjvVar = (zzjv) list;
            int size = list.size();
            for (Object obj : zzd) {
                if (obj == null) {
                    int size2 = zzjvVar.size() - size;
                    StringBuilder sb = new StringBuilder(37);
                    sb.append("Element at index ");
                    sb.append(size2);
                    sb.append(" is null.");
                    String sb2 = sb.toString();
                    for (int size3 = zzjvVar.size() - 1; size3 >= size; size3--) {
                        zzjvVar.remove(size3);
                    }
                    throw new NullPointerException(sb2);
                }
                if (obj instanceof zzht) {
                    zzjvVar.zza((zzht) obj);
                } else {
                    zzjvVar.add((String) obj);
                }
            }
            return;
        }
        if (iterable instanceof zzkw) {
            list.addAll((Collection) iterable);
            return;
        }
        if ((list instanceof ArrayList) && (iterable instanceof Collection)) {
            ((ArrayList) list).ensureCapacity(list.size() + ((Collection) iterable).size());
        }
        int size4 = list.size();
        for (Object obj2 : iterable) {
            if (obj2 == null) {
                int size5 = list.size() - size4;
                StringBuilder sb3 = new StringBuilder(37);
                sb3.append("Element at index ");
                sb3.append(size5);
                sb3.append(" is null.");
                String sb4 = sb3.toString();
                for (int size6 = list.size() - 1; size6 >= size4; size6--) {
                    list.remove(size6);
                }
                throw new NullPointerException(sb4);
            }
            list.add(obj2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void zzb(int i);

    public final byte[] zzh() {
        try {
            byte[] bArr = new byte[zzm()];
            zzii zza = zzii.zza(bArr);
            zza(zza);
            zza.zzb();
            return bArr;
        } catch (IOException e) {
            String name = getClass().getName();
            StringBuilder sb = new StringBuilder(name.length() + 62 + "byte array".length());
            sb.append("Serializing ");
            sb.append(name);
            sb.append(" to a ");
            sb.append("byte array");
            sb.append(" threw an IOException (should never happen).");
            throw new RuntimeException(sb.toString(), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int zzi();
}
