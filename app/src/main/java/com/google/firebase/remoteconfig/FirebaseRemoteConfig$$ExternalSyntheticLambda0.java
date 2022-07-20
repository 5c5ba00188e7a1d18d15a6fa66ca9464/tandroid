package com.google.firebase.remoteconfig;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
/* loaded from: classes.dex */
public final /* synthetic */ class FirebaseRemoteConfig$$ExternalSyntheticLambda0 implements Continuation {
    public final /* synthetic */ FirebaseRemoteConfig f$0;

    public /* synthetic */ FirebaseRemoteConfig$$ExternalSyntheticLambda0(FirebaseRemoteConfig firebaseRemoteConfig) {
        this.f$0 = firebaseRemoteConfig;
    }

    @Override // com.google.android.gms.tasks.Continuation
    public final Object then(Task task) {
        boolean processActivatePutTask;
        processActivatePutTask = this.f$0.processActivatePutTask(task);
        return Boolean.valueOf(processActivatePutTask);
    }
}
