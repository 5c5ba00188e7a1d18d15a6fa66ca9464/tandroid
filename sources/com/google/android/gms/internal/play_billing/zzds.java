package com.google.android.gms.internal.play_billing;

import java.util.Collections;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzds extends zzec {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzds(int i) {
        super(i, null);
    }

    @Override // com.google.android.gms.internal.play_billing.zzec
    public final void zza() {
        if (!zzj()) {
            for (int i = 0; i < zzb(); i++) {
                Map.Entry zzg = zzg(i);
                if (((zzbr) zzg.getKey()).zzc()) {
                    zzg.setValue(Collections.unmodifiableList((List) zzg.getValue()));
                }
            }
            for (Map.Entry entry : zzc()) {
                if (((zzbr) entry.getKey()).zzc()) {
                    entry.setValue(Collections.unmodifiableList((List) entry.getValue()));
                }
            }
        }
        super.zza();
    }
}
