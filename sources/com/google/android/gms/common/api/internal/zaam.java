package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
final class zaam extends zabg {
    final /* synthetic */ ConnectionResult zaa;
    final /* synthetic */ zaao zab;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zaam(zaao zaaoVar, zabf zabfVar, ConnectionResult connectionResult) {
        super(zabfVar);
        this.zab = zaaoVar;
        this.zaa = connectionResult;
    }

    @Override // com.google.android.gms.common.api.internal.zabg
    public final void zaa() {
        this.zab.zaa.zaD(this.zaa);
    }
}
