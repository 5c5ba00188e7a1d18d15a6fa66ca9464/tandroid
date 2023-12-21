package com.google.android.gms.internal.mlkit_common;

import java.util.Arrays;
import java.util.Objects;
/* compiled from: com.google.mlkit:common@@18.10.0 */
/* loaded from: classes.dex */
public final class zzy {
    private final String zza;
    private final zzw zzb;
    private zzw zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzy(String str, zzx zzxVar) {
        zzw zzwVar = new zzw();
        this.zzb = zzwVar;
        this.zzc = zzwVar;
        Objects.requireNonNull(str);
        this.zza = str;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append(this.zza);
        sb.append('{');
        zzw zzwVar = this.zzb.zzc;
        String str = "";
        while (zzwVar != null) {
            Object obj = zzwVar.zzb;
            sb.append(str);
            String str2 = zzwVar.zza;
            if (str2 != null) {
                sb.append(str2);
                sb.append('=');
            }
            if (obj == null || !obj.getClass().isArray()) {
                sb.append(obj);
            } else {
                String deepToString = Arrays.deepToString(new Object[]{obj});
                sb.append((CharSequence) deepToString, 1, deepToString.length() - 1);
            }
            zzwVar = zzwVar.zzc;
            str = ", ";
        }
        sb.append('}');
        return sb.toString();
    }

    public final zzy zza(String str, Object obj) {
        zzw zzwVar = new zzw();
        this.zzc.zzc = zzwVar;
        this.zzc = zzwVar;
        zzwVar.zzb = obj;
        zzwVar.zza = str;
        return this;
    }
}
