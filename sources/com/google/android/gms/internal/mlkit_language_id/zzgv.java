package com.google.android.gms.internal.mlkit_language_id;

import java.util.Iterator;

/* loaded from: classes.dex */
final class zzgv extends zzhb {
    private final /* synthetic */ zzgq zza;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    private zzgv(zzgq zzgqVar) {
        super(zzgqVar, null);
        this.zza = zzgqVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzgv(zzgq zzgqVar, zzgt zzgtVar) {
        this(zzgqVar);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzhb, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator iterator() {
        return new zzgs(this.zza, null);
    }
}
