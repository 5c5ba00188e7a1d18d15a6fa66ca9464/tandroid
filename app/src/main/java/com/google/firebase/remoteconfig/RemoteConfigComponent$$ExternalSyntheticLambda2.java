package com.google.firebase.remoteconfig;

import java.util.concurrent.Callable;
/* loaded from: classes.dex */
public final /* synthetic */ class RemoteConfigComponent$$ExternalSyntheticLambda2 implements Callable {
    public final /* synthetic */ RemoteConfigComponent f$0;

    public /* synthetic */ RemoteConfigComponent$$ExternalSyntheticLambda2(RemoteConfigComponent remoteConfigComponent) {
        this.f$0 = remoteConfigComponent;
    }

    @Override // java.util.concurrent.Callable
    public final Object call() {
        return this.f$0.getDefault();
    }
}
