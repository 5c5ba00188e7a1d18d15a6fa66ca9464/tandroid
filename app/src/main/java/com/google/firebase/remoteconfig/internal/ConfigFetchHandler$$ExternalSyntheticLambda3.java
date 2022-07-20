package com.google.firebase.remoteconfig.internal;

import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.remoteconfig.internal.ConfigFetchHandler;
/* loaded from: classes.dex */
public final /* synthetic */ class ConfigFetchHandler$$ExternalSyntheticLambda3 implements SuccessContinuation {
    public final /* synthetic */ ConfigFetchHandler.FetchResponse f$0;

    public /* synthetic */ ConfigFetchHandler$$ExternalSyntheticLambda3(ConfigFetchHandler.FetchResponse fetchResponse) {
        this.f$0 = fetchResponse;
    }

    @Override // com.google.android.gms.tasks.SuccessContinuation
    public final Task then(Object obj) {
        Task forResult;
        ConfigContainer configContainer = (ConfigContainer) obj;
        forResult = Tasks.forResult(this.f$0);
        return forResult;
    }
}
