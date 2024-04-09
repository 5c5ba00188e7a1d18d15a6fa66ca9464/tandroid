package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Iterator;
import java.util.Objects;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zzbi {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(Iterator it) {
        Objects.requireNonNull(it);
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }
}
