package com.google.android.gms.internal.location;

import com.google.android.gms.common.api.internal.ListenerHolder;

/* loaded from: classes.dex */
final class zzcv implements ListenerHolder.Notifier {
    final /* synthetic */ zzcw zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzcv(zzcw zzcwVar) {
        this.zza = zzcwVar;
    }

    @Override // com.google.android.gms.common.api.internal.ListenerHolder.Notifier
    public final /* bridge */ /* synthetic */ void notifyListener(Object obj) {
        zzcs zzcsVar;
        zzcsVar = this.zza.zza;
        zzcsVar.zzb();
    }

    @Override // com.google.android.gms.common.api.internal.ListenerHolder.Notifier
    public final void onNotifyListenerFailed() {
    }
}
