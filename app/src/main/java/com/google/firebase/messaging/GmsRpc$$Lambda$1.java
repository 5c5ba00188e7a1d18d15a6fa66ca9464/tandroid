package com.google.firebase.messaging;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
/* compiled from: com.google.firebase:firebase-messaging@@22.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class GmsRpc$$Lambda$1 implements Continuation {
    private final GmsRpc arg$1;

    public GmsRpc$$Lambda$1(GmsRpc gmsRpc) {
        this.arg$1 = gmsRpc;
    }

    @Override // com.google.android.gms.tasks.Continuation
    public Object then(Task task) {
        return this.arg$1.lambda$extractResponseWhenComplete$0$GmsRpc(task);
    }
}
