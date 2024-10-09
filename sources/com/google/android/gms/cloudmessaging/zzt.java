package com.google.android.gms.cloudmessaging;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final /* synthetic */ class zzt implements Continuation {
    static final Continuation zza = new zzt();

    private zzt() {
    }

    @Override // com.google.android.gms.tasks.Continuation
    public final Object then(Task task) {
        return Rpc.zza(task);
    }
}
