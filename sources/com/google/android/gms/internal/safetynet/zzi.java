package com.google.android.gms.internal.safetynet;

import android.text.TextUtils;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

/* loaded from: classes.dex */
final class zzi extends zzr {
    final /* synthetic */ byte[] zza;
    final /* synthetic */ String zzb;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzi(GoogleApiClient googleApiClient, byte[] bArr, String str) {
        super(googleApiClient);
        this.zza = bArr;
        this.zzb = str;
    }

    @Override // com.google.android.gms.common.api.internal.BaseImplementation$ApiMethodImpl
    protected final /* bridge */ /* synthetic */ void doExecute(Api.AnyClient anyClient) {
        zzaf zzafVar = (zzaf) anyClient;
        zzg zzgVar = this.zzc;
        byte[] bArr = this.zza;
        String str = this.zzb;
        if (TextUtils.isEmpty(str)) {
            str = zzafVar.zzp("com.google.android.safetynet.ATTEST_API_KEY");
        }
        ((zzh) zzafVar.getService()).zzc(zzgVar, bArr, str);
    }
}
