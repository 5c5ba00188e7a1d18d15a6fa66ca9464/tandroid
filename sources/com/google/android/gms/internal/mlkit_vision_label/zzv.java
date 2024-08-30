package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
abstract class zzv extends zzag implements zzbj {
    /* JADX INFO: Access modifiers changed from: protected */
    public zzv(Map map) {
        super(map);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_label.zzag
    public final Collection zzb(Object obj, Collection collection) {
        return zzi(obj, (List) collection, null);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [java.util.List, java.util.Collection] */
    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbj
    public final List zzc(Object obj) {
        return super.zzh(obj);
    }
}
