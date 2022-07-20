package com.google.android.gms.cloudmessaging;

import android.os.Handler;
import android.os.Message;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-cloud-messaging@@16.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzi implements Handler.Callback {
    private final zzf zza;

    public zzi(zzf zzfVar) {
        this.zza = zzfVar;
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        return this.zza.zza(message);
    }
}
