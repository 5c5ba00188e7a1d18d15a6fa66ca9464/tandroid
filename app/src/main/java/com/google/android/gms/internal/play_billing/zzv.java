package com.google.android.gms.internal.play_billing;

import com.huawei.hms.framework.common.ContainerUtils;
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
final class zzv {
    private final Object zza;
    private final Object zzb;
    private final Object zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzv(Object obj, Object obj2, Object obj3) {
        this.zza = obj;
        this.zzb = obj2;
        this.zzc = obj3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final IllegalArgumentException zza() {
        return new IllegalArgumentException("Multiple entries with same key: " + this.zza + ContainerUtils.KEY_VALUE_DELIMITER + this.zzb + " and " + this.zza + ContainerUtils.KEY_VALUE_DELIMITER + this.zzc);
    }
}
