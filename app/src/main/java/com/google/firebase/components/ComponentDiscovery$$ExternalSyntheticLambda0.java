package com.google.firebase.components;

import com.google.firebase.inject.Provider;
/* loaded from: classes.dex */
public final /* synthetic */ class ComponentDiscovery$$ExternalSyntheticLambda0 implements Provider {
    public final /* synthetic */ String f$0;

    public /* synthetic */ ComponentDiscovery$$ExternalSyntheticLambda0(String str) {
        this.f$0 = str;
    }

    @Override // com.google.firebase.inject.Provider
    public final Object get() {
        ComponentRegistrar instantiate;
        instantiate = ComponentDiscovery.instantiate(this.f$0);
        return instantiate;
    }
}
