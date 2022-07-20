package com.google.android.gms.wallet;

import android.content.Context;
import androidx.annotation.RecentlyNonNull;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.Wallet;
/* compiled from: com.google.android.gms:play-services-wallet@@18.1.3 */
/* loaded from: classes.dex */
public class PaymentsClient extends GoogleApi<Wallet.WalletOptions> {
    @RecentlyNonNull
    public Task<Boolean> isReadyToPay(@RecentlyNonNull IsReadyToPayRequest isReadyToPayRequest) {
        return doRead(TaskApiCall.builder().setMethodKey(23705).run(new zzab(isReadyToPayRequest)).build());
    }

    @RecentlyNonNull
    public Task<PaymentData> loadPaymentData(@RecentlyNonNull PaymentDataRequest paymentDataRequest) {
        return doWrite(TaskApiCall.builder().run(new zzac(paymentDataRequest)).setFeatures(zzj.zzc).setAutoResolveMissingFeatures(true).setMethodKey(23707).build());
    }

    public PaymentsClient(Context context, Wallet.WalletOptions walletOptions) {
        super(context, Wallet.API, walletOptions, GoogleApi.Settings.DEFAULT_SETTINGS);
    }
}
