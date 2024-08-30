package com.google.android.gms.internal.wallet;

import android.os.Bundle;
import android.os.IInterface;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.PaymentCardRecognitionIntentResponse;
import com.google.android.gms.wallet.PaymentData;
/* loaded from: classes.dex */
public interface zzu extends IInterface {
    void zzb(int i, FullWallet fullWallet, Bundle bundle);

    void zzc(Status status, boolean z, Bundle bundle);

    void zzd(int i, MaskedWallet maskedWallet, Bundle bundle);

    void zze(Status status, PaymentCardRecognitionIntentResponse paymentCardRecognitionIntentResponse, Bundle bundle);

    void zzf(Status status, PaymentData paymentData, Bundle bundle);

    void zzg(int i, boolean z, Bundle bundle);

    void zzh(int i, Bundle bundle);
}
