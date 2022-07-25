package com.google.android.gms.common.internal;

import android.content.Intent;
import com.google.android.gms.common.api.internal.LifecycleFragment;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final class zaf extends zab {
    private final /* synthetic */ Intent zaa;
    private final /* synthetic */ LifecycleFragment zab;
    private final /* synthetic */ int zac = 2;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zaf(Intent intent, LifecycleFragment lifecycleFragment, int i) {
        this.zaa = intent;
        this.zab = lifecycleFragment;
    }

    @Override // com.google.android.gms.common.internal.zab
    public final void zaa() {
        Intent intent = this.zaa;
        if (intent != null) {
            this.zab.startActivityForResult(intent, this.zac);
        }
    }
}
