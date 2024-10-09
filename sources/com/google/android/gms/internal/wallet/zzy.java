package com.google.android.gms.internal.wallet;

import android.os.Bundle;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.TaskUtil;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
final class zzy extends zzx {
    private final TaskCompletionSource zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzy(TaskCompletionSource taskCompletionSource) {
        this.zza = taskCompletionSource;
    }

    @Override // com.google.android.gms.internal.wallet.zzx, com.google.android.gms.internal.wallet.zzu
    public final void zzc(Status status, boolean z, Bundle bundle) {
        TaskUtil.setResultOrApiException(status, Boolean.valueOf(z), this.zza);
    }

    @Override // com.google.android.gms.internal.wallet.zzx, com.google.android.gms.internal.wallet.zzu
    public final void zzg(int i, boolean z, Bundle bundle) {
        TaskUtil.setResultOrApiException(new Status(i), Boolean.valueOf(z), this.zza);
    }
}
