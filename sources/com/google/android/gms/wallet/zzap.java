package com.google.android.gms.wallet;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.wallet.Wallet;

/* loaded from: classes.dex */
final class zzap extends Api.AbstractClientBuilder {
    @Override // com.google.android.gms.common.api.Api.AbstractClientBuilder
    public final /* bridge */ /* synthetic */ Api.Client buildClient(Context context, Looper looper, ClientSettings clientSettings, Object obj, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        Wallet.WalletOptions walletOptions = (Wallet.WalletOptions) obj;
        if (walletOptions == null) {
            walletOptions = new Wallet.WalletOptions();
        }
        return new com.google.android.gms.internal.wallet.zzac(context, looper, clientSettings, connectionCallbacks, onConnectionFailedListener, walletOptions.environment, walletOptions.theme, walletOptions.zzb);
    }
}
