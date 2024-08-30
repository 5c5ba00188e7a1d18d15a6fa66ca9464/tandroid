package com.google.android.gms.internal.clearcut;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
/* loaded from: classes.dex */
final class zzi extends zzg {
    private final /* synthetic */ zzh zzap;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzi(zzh zzhVar) {
        super(null);
        this.zzap = zzhVar;
    }

    @Override // com.google.android.gms.internal.clearcut.zzl
    public final void zza(Status status) {
        this.zzap.setResult((Result) status);
    }
}
