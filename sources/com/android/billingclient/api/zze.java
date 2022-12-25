package com.android.billingclient.api;

import java.util.Arrays;
import org.json.JSONObject;
/* compiled from: com.android.billingclient:billing@@5.1.0 */
/* loaded from: classes.dex */
public final class zze {
    private final String zza;
    private final String zzb;
    private final String zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zze(JSONObject jSONObject, zzd zzdVar) {
        this.zza = jSONObject.optString("productId");
        this.zzb = jSONObject.optString("productType");
        String optString = jSONObject.optString("offerToken");
        this.zzc = true == optString.isEmpty() ? null : optString;
    }

    public final boolean equals(Object obj) {
        String str;
        String str2;
        if (this == obj) {
            return true;
        }
        if (obj instanceof zze) {
            zze zzeVar = (zze) obj;
            return this.zza.equals(zzeVar.zza) && this.zzb.equals(zzeVar.zzb) && ((str = this.zzc) == (str2 = zzeVar.zzc) || (str != null && str.equals(str2)));
        }
        return false;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.zza, this.zzb, this.zzc});
    }

    public final String toString() {
        return String.format("{id: %s, type: %s, offer token: %s}", this.zza, this.zzb, this.zzc);
    }
}
