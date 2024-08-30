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
                    StringBuilder sb = new StringBuilder(37);
                    sb.append("Element at index ");
                    sb.append(zzfgVar.size() - size);
                    sb.append(" is null.");
                    String sb2 = sb.toString();
                    for (int size2 = zzfgVar.size() - 1; size2 >= size; size2--) {
                        zzfgVar.remove(size2);
                    }
                    throw new NullPointerException(sb2);
                } else if (obj instanceof zzdn) {
                    zzfgVar.zza((zzdn) obj);
                } else {
                    zzfgVar.add((String) obj);
                }
            }
        } else if (iterable instanceof zzgi) {
            list.addAll((Collection) iterable);
        } else {
            if ((list instanceof ArrayList) && (iterable instanceof Collection)) {
                ((ArrayList) list).ensureCapacity(list.size() + ((Collection) iterable).size());
            }
            int size3 = list.size();
            for (Object obj2 : iterable) {
                if (obj2 == null) {
                    StringBuilder sb3 = new StringBuilder(37);
                    sb3.append("Element at index ");
                    sb3.append(list.size() - size3);
                    sb3.append(" is null.");
                    String sb4 = sb3.toString();
                    for (int size4 = list.size() - 1; size4 >= size3; size4--) {
                        list.remove(size4);
                    }
                    throw new NullPointerException(sb4);
                }
                list.add(obj2);
            }
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
