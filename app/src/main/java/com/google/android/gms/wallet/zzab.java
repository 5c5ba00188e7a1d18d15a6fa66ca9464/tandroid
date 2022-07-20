package com.google.android.gms.wallet;

import com.google.android.gms.common.api.internal.RemoteCall;
import com.google.android.gms.tasks.TaskCompletionSource;
/* compiled from: com.google.android.gms:play-services-wallet@@18.1.3 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzab implements RemoteCall {
    public final /* synthetic */ IsReadyToPayRequest zza;

    @Override // com.google.android.gms.common.api.internal.RemoteCall
    public final void accept(Object obj, Object obj2) {
        ((com.google.android.gms.internal.wallet.zzab) obj).zzr(this.zza, (TaskCompletionSource) obj2);
    }
}
