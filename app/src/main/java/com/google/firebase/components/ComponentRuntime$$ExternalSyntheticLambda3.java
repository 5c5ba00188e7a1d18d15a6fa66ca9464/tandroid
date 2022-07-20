package com.google.firebase.components;

import com.google.firebase.inject.Provider;
/* loaded from: classes.dex */
public final /* synthetic */ class ComponentRuntime$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ LazySet f$0;
    public final /* synthetic */ Provider f$1;

    public /* synthetic */ ComponentRuntime$$ExternalSyntheticLambda3(LazySet lazySet, Provider provider) {
        this.f$0 = lazySet;
        this.f$1 = provider;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.add(this.f$1);
    }
}
