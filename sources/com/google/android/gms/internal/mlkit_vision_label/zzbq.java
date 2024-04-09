package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Map;
import java.util.Objects;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zzbq {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object zza(Map map, Object obj) {
        Objects.requireNonNull(map);
        try {
            return map.get(obj);
        } catch (ClassCastException | NullPointerException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean zzb(Map map, Object obj) {
        Objects.requireNonNull(map);
        try {
            return map.containsKey(obj);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }
}
