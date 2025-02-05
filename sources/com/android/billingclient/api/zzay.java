package com.android.billingclient.api;

import android.content.Context;
import com.google.android.datatransport.Encoding;
import com.google.android.datatransport.Event;
import com.google.android.datatransport.Transformer;
import com.google.android.datatransport.Transport;
import com.google.android.datatransport.cct.CCTDestination;
import com.google.android.datatransport.runtime.TransportRuntime;
import com.google.android.gms.internal.play_billing.zzb;
import com.google.android.gms.internal.play_billing.zzfz;

/* loaded from: classes.dex */
final class zzay {
    private boolean zza;
    private Transport zzb;

    zzay(Context context) {
        try {
            TransportRuntime.initialize(context);
            this.zzb = TransportRuntime.getInstance().newFactory(CCTDestination.INSTANCE).getTransport("PLAY_BILLING_LIBRARY", zzfz.class, Encoding.of("proto"), new Transformer() { // from class: com.android.billingclient.api.zzax
                @Override // com.google.android.datatransport.Transformer
                public final Object apply(Object obj) {
                    return ((zzfz) obj).zzc();
                }
            });
        } catch (Throwable unused) {
            this.zza = true;
        }
    }

    public final void zza(zzfz zzfzVar) {
        String str;
        if (this.zza) {
            str = "Skipping logging since initialization failed.";
        } else {
            try {
                this.zzb.send(Event.ofData(zzfzVar));
                return;
            } catch (Throwable unused) {
                str = "logging failed.";
            }
        }
        zzb.zzj("BillingLogger", str);
    }
}
