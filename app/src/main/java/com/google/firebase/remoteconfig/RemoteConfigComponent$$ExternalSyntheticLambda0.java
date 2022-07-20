package com.google.firebase.remoteconfig;

import com.google.android.gms.common.util.BiConsumer;
import com.google.firebase.remoteconfig.internal.ConfigContainer;
import com.google.firebase.remoteconfig.internal.Personalization;
/* loaded from: classes.dex */
public final /* synthetic */ class RemoteConfigComponent$$ExternalSyntheticLambda0 implements BiConsumer {
    public final /* synthetic */ Personalization f$0;

    @Override // com.google.android.gms.common.util.BiConsumer
    public final void accept(Object obj, Object obj2) {
        this.f$0.logArmActive((String) obj, (ConfigContainer) obj2);
    }
}
