package com.google.firebase.remoteconfig.internal;

import com.google.android.gms.common.util.BiConsumer;
/* loaded from: classes.dex */
public final /* synthetic */ class ConfigGetParameterHandler$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BiConsumer f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ConfigContainer f$2;

    public /* synthetic */ ConfigGetParameterHandler$$ExternalSyntheticLambda0(BiConsumer biConsumer, String str, ConfigContainer configContainer) {
        this.f$0 = biConsumer;
        this.f$1 = str;
        this.f$2 = configContainer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.accept(this.f$1, this.f$2);
    }
}
