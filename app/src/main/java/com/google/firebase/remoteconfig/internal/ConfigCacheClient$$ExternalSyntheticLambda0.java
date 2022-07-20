package com.google.firebase.remoteconfig.internal;

import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
/* loaded from: classes.dex */
public final /* synthetic */ class ConfigCacheClient$$ExternalSyntheticLambda0 implements SuccessContinuation {
    public final /* synthetic */ ConfigCacheClient f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ ConfigContainer f$2;

    public /* synthetic */ ConfigCacheClient$$ExternalSyntheticLambda0(ConfigCacheClient configCacheClient, boolean z, ConfigContainer configContainer) {
        this.f$0 = configCacheClient;
        this.f$1 = z;
        this.f$2 = configContainer;
    }

    @Override // com.google.android.gms.tasks.SuccessContinuation
    public final Task then(Object obj) {
        Task lambda$put$1;
        lambda$put$1 = this.f$0.lambda$put$1(this.f$1, this.f$2, (Void) obj);
        return lambda$put$1;
    }
}
