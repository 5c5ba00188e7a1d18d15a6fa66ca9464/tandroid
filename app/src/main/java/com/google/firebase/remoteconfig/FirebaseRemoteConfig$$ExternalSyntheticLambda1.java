package com.google.firebase.remoteconfig;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
/* loaded from: classes.dex */
public final /* synthetic */ class FirebaseRemoteConfig$$ExternalSyntheticLambda1 implements Continuation {
    public final /* synthetic */ FirebaseRemoteConfig f$0;
    public final /* synthetic */ Task f$1;
    public final /* synthetic */ Task f$2;

    public /* synthetic */ FirebaseRemoteConfig$$ExternalSyntheticLambda1(FirebaseRemoteConfig firebaseRemoteConfig, Task task, Task task2) {
        this.f$0 = firebaseRemoteConfig;
        this.f$1 = task;
        this.f$2 = task2;
    }

    @Override // com.google.android.gms.tasks.Continuation
    public final Object then(Task task) {
        Task lambda$activate$2;
        lambda$activate$2 = this.f$0.lambda$activate$2(this.f$1, this.f$2, task);
        return lambda$activate$2;
    }
}
