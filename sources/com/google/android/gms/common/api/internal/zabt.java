package com.google.android.gms.common.api.internal;

import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
final class zabt implements Runnable {
    final /* synthetic */ ConnectionResult zaa;
    final /* synthetic */ zabu zab;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zabt(zabu zabuVar, ConnectionResult connectionResult) {
        this.zab = zabuVar;
        this.zaa = connectionResult;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Map map;
        ApiKey apiKey;
        Api.Client client;
        Api.Client client2;
        Api.Client client3;
        Api.Client client4;
        zabu zabuVar = this.zab;
        map = zabuVar.zaa.zap;
        apiKey = zabuVar.zac;
        zabq zabqVar = (zabq) map.get(apiKey);
        if (zabqVar == null) {
            return;
        }
        if (this.zaa.isSuccess()) {
            this.zab.zaf = true;
            client = this.zab.zab;
            if (client.requiresSignIn()) {
                this.zab.zag();
                return;
            }
            try {
                zabu zabuVar2 = this.zab;
                client3 = zabuVar2.zab;
                client4 = zabuVar2.zab;
                client3.getRemoteService(null, client4.getScopesForConnectionlessNonSignIn());
                return;
            } catch (SecurityException e) {
                Log.e("GoogleApiManager", "Failed to get service from broker. ", e);
                client2 = this.zab.zab;
                client2.disconnect("Failed to get service from broker.");
                zabqVar.zar(new ConnectionResult(10), null);
                return;
            }
        }
        zabqVar.zar(this.zaa, null);
    }
}
