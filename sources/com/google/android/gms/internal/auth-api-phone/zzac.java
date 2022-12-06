package com.google.android.gms.internal.auth-api-phone;

import com.google.android.gms.common.Feature;
/* compiled from: com.google.android.gms:play-services-auth-api-phone@@17.5.1 */
/* loaded from: classes.dex */
public final class zzac {
    public static final Feature zza;
    public static final Feature zzb;
    public static final Feature zzc;
    public static final Feature zzd;
    public static final Feature[] zze;

    static {
        Feature feature = new Feature("sms_code_autofill", 2L);
        zza = feature;
        Feature feature2 = new Feature("sms_code_browser", 2L);
        zzb = feature2;
        Feature feature3 = new Feature("sms_retrieve", 1L);
        zzc = feature3;
        Feature feature4 = new Feature("user_consent", 3L);
        zzd = feature4;
        zze = new Feature[]{feature, feature2, feature3, feature4};
    }
}
