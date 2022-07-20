package com.google.firebase.components;

import com.google.firebase.inject.Provider;
/* loaded from: classes.dex */
public final /* synthetic */ class ComponentRuntime$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ OptionalProvider f$0;
    public final /* synthetic */ Provider f$1;

    public /* synthetic */ ComponentRuntime$$ExternalSyntheticLambda4(OptionalProvider optionalProvider, Provider provider) {
        this.f$0 = optionalProvider;
        this.f$1 = provider;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.set(this.f$1);
    }
}
