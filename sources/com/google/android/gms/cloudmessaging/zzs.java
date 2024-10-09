package com.google.android.gms.cloudmessaging;

import android.os.Bundle;

/* loaded from: classes.dex */
final class zzs extends zzq {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzs(int i, int i2, Bundle bundle) {
        super(i, 1, bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.cloudmessaging.zzq
    public final void zza(Bundle bundle) {
        Bundle bundle2 = bundle.getBundle("data");
        if (bundle2 == null) {
            bundle2 = Bundle.EMPTY;
        }
        zza((Object) bundle2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.cloudmessaging.zzq
    public final boolean zza() {
        return false;
    }
}
