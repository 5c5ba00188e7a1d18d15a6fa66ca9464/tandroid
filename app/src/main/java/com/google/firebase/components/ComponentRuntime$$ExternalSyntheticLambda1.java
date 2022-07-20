package com.google.firebase.components;

import com.google.firebase.inject.Provider;
/* loaded from: classes.dex */
public final /* synthetic */ class ComponentRuntime$$ExternalSyntheticLambda1 implements Provider {
    public final /* synthetic */ ComponentRuntime f$0;
    public final /* synthetic */ Component f$1;

    public /* synthetic */ ComponentRuntime$$ExternalSyntheticLambda1(ComponentRuntime componentRuntime, Component component) {
        this.f$0 = componentRuntime;
        this.f$1 = component;
    }

    @Override // com.google.firebase.inject.Provider
    public final Object get() {
        Object lambda$discoverComponents$0;
        lambda$discoverComponents$0 = this.f$0.lambda$discoverComponents$0(this.f$1);
        return lambda$discoverComponents$0;
    }
}
