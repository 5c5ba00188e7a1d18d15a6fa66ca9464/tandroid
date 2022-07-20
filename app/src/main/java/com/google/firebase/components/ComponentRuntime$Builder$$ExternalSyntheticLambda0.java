package com.google.firebase.components;

import com.google.firebase.components.ComponentRuntime;
import com.google.firebase.inject.Provider;
/* loaded from: classes.dex */
public final /* synthetic */ class ComponentRuntime$Builder$$ExternalSyntheticLambda0 implements Provider {
    public final /* synthetic */ ComponentRegistrar f$0;

    public /* synthetic */ ComponentRuntime$Builder$$ExternalSyntheticLambda0(ComponentRegistrar componentRegistrar) {
        this.f$0 = componentRegistrar;
    }

    @Override // com.google.firebase.inject.Provider
    public final Object get() {
        ComponentRegistrar lambda$addComponentRegistrar$0;
        lambda$addComponentRegistrar$0 = ComponentRuntime.Builder.lambda$addComponentRegistrar$0(this.f$0);
        return lambda$addComponentRegistrar$0;
    }
}
