package com.google.android.gms.common.internal;

import android.app.Activity;
import android.content.Intent;

/* loaded from: classes.dex */
final class zad extends zag {
    final /* synthetic */ Intent zaa;
    final /* synthetic */ Activity zab;
    final /* synthetic */ int zac;

    zad(Intent intent, Activity activity, int i) {
        this.zaa = intent;
        this.zab = activity;
        this.zac = i;
    }

    @Override // com.google.android.gms.common.internal.zag
    public final void zaa() {
        Intent intent = this.zaa;
        if (intent != null) {
            this.zab.startActivityForResult(intent, this.zac);
        }
    }
}
