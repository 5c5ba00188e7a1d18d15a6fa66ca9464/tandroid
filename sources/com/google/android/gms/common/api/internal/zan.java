package com.google.android.gms.common.api.internal;

import android.app.Dialog;

/* loaded from: classes.dex */
final class zan extends zabw {
    final /* synthetic */ Dialog zaa;
    final /* synthetic */ zao zab;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zan(zao zaoVar, Dialog dialog) {
        this.zab = zaoVar;
        this.zaa = dialog;
    }

    @Override // com.google.android.gms.common.api.internal.zabw
    public final void zaa() {
        this.zab.zaa.zad();
        if (this.zaa.isShowing()) {
            this.zaa.dismiss();
        }
    }
}
