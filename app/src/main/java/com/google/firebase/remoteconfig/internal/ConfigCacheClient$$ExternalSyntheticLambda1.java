package com.google.firebase.remoteconfig.internal;

import java.util.concurrent.Callable;
/* loaded from: classes.dex */
public final /* synthetic */ class ConfigCacheClient$$ExternalSyntheticLambda1 implements Callable {
    public final /* synthetic */ ConfigCacheClient f$0;
    public final /* synthetic */ ConfigContainer f$1;

    public /* synthetic */ ConfigCacheClient$$ExternalSyntheticLambda1(ConfigCacheClient configCacheClient, ConfigContainer configContainer) {
        this.f$0 = configCacheClient;
        this.f$1 = configContainer;
    }

    @Override // java.util.concurrent.Callable
    public final Object call() {
        Void lambda$put$0;
        lambda$put$0 = this.f$0.lambda$put$0(this.f$1);
        return lambda$put$0;
    }
}
