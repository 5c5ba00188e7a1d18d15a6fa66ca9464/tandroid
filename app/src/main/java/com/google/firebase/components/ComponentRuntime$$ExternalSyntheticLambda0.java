package com.google.firebase.components;

import com.google.firebase.inject.Provider;
/* loaded from: classes.dex */
public final /* synthetic */ class ComponentRuntime$$ExternalSyntheticLambda0 implements Provider {
    public final /* synthetic */ ComponentRegistrar f$0;

    public /* synthetic */ ComponentRuntime$$ExternalSyntheticLambda0(ComponentRegistrar componentRegistrar) {
        this.f$0 = componentRegistrar;
    }

    @Override // com.google.firebase.inject.Provider
    public final Object get() {
        ComponentRegistrar lambda$toProviders$1;
        lambda$toProviders$1 = ComponentRuntime.lambda$toProviders$1(this.f$0);
        return lambda$toProviders$1;
    }
}
