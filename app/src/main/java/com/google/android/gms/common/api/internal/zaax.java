package com.google.android.gms.common.api.internal;

import java.lang.ref.WeakReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zaax extends zabm {
    private WeakReference<zaar> zaa;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zaax(zaar zaarVar) {
        this.zaa = new WeakReference<>(zaarVar);
    }

    @Override // com.google.android.gms.common.api.internal.zabm
    public final void zaa() {
        zaar zaarVar = this.zaa.get();
        if (zaarVar == null) {
            return;
        }
        zaarVar.zae();
    }
}
