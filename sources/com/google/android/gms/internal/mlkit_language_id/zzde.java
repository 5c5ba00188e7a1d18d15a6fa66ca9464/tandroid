package com.google.android.gms.internal.mlkit_language_id;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: classes.dex */
public abstract class zzde implements zzfz {
    protected int zza = 0;

    /* JADX INFO: Access modifiers changed from: protected */
    public static void zza(Iterable iterable, List list) {
        zzeq.zza(iterable);
        if (iterable instanceof zzfg) {
            List zzb = ((zzfg) iterable).zzb();
            zzfg zzfgVar = (zzfg) list;
            int size = list.size();
            for (Object obj : zzb) {
                if (obj == null) {
                    int size2 = zzfgVar.size() - size;
                    StringBuilder sb = new StringBuilder(37);
                    sb.append("Element at index ");
                    sb.append(size2);
                    sb.append(" is null.");
                    String sb2 = sb.toString();
                    for (int size3 = zzfgVar.size() - 1; size3 >= size; size3--) {
                        zzfgVar.remove(size3);
                    }
                    throw new NullPointerException(sb2);
                }
                if (obj instanceof zzdn) {
                    zzfgVar.zza((zzdn) obj);
                } else {
                    zzfgVar.add((String) obj);
                }
            }
            return;
        }
        if (iterable instanceof zzgi) {
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
    public abstract void zza(int i);

    public final byte[] zzf() {
        try {
            byte[] bArr = new byte[zzj()];
            zzea zza = zzea.zza(bArr);
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
    public abstract int zzg();
}
