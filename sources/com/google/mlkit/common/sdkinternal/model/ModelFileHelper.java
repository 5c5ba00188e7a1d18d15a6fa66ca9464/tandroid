package com.google.mlkit.common.sdkinternal.model;

import com.google.android.gms.common.internal.GmsLogger;
import com.google.mlkit.common.sdkinternal.MlKitContext;
/* loaded from: classes.dex */
public class ModelFileHelper {
    private final MlKitContext zze;
    private static final GmsLogger zzd = new GmsLogger("ModelFileHelper", "");
    public static final String zza = String.format("com.google.mlkit.%s.models", "translate");
    public static final String zzb = String.format("com.google.mlkit.%s.models", "custom");
    static final String zzc = String.format("com.google.mlkit.%s.models", "base");

    public ModelFileHelper(MlKitContext mlKitContext) {
        this.zze = mlKitContext;
    }
}
