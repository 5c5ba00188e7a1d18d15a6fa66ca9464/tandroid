package com.google.android.play.core.integrity;

import com.google.android.gms.tasks.Task;
/* loaded from: classes.dex */
final class aa implements IntegrityManager {
    private final aj a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public aa(aj ajVar) {
        this.a = ajVar;
    }

    @Override // com.google.android.play.core.integrity.IntegrityManager
    public final Task requestIntegrityToken(IntegrityTokenRequest integrityTokenRequest) {
        return this.a.c(integrityTokenRequest);
    }
}
