package com.google.android.gms.internal.vision;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.Iterator;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzlg extends zzlh {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzlg(int i) {
        super(i, null);
    }

    @Override // com.google.android.gms.internal.vision.zzlh
    public final void zza() {
        if (!zzb()) {
            if (zzc() > 0) {
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzb(0).getKey());
                throw null;
            }
            Iterator it = zzd().iterator();
            if (it.hasNext()) {
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(((Map.Entry) it.next()).getKey());
                throw null;
            }
        }
        super.zza();
    }
}
