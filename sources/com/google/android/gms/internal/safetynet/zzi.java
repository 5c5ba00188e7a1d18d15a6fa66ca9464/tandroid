package com.google.android.gms.internal.safetynet;

import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.common.api.GoogleApiClient;
/* compiled from: com.google.android.gms:play-services-safetynet@@17.0.1 */
/* loaded from: classes.dex */
final class zzi extends zzr {
    final /* synthetic */ byte[] zza;
    final /* synthetic */ String zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzi(GoogleApiClient googleApiClient, byte[] bArr, String str) {
        super(googleApiClient);
        this.zza = bArr;
        this.zzb = str;
    }

    @Override // com.google.android.gms.common.api.internal.BaseImplementation$ApiMethodImpl
    protected final /* bridge */ /* synthetic */ void doExecute(zzaf zzafVar) throws RemoteException {
        zzaf zzafVar2 = zzafVar;
        zzg zzgVar = this.zzc;
        byte[] bArr = this.zza;
        String str = this.zzb;
        if (TextUtils.isEmpty(str)) {
            str = zzafVar2.zzp("com.google.android.safetynet.ATTEST_API_KEY");
        }
        ((zzh) zzafVar2.getService()).zzc(zzgVar, bArr, str);
    }
}
