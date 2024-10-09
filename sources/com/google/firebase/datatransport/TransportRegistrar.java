package com.google.firebase.datatransport;

import android.content.Context;
import com.google.android.datatransport.TransportFactory;
import com.google.android.datatransport.cct.CCTDestination;
import com.google.android.datatransport.runtime.TransportRuntime;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class TransportRegistrar implements ComponentRegistrar {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ TransportFactory lambda$getComponents$0(ComponentContainer componentContainer) {
        TransportRuntime.initialize((Context) componentContainer.get(Context.class));
        return TransportRuntime.getInstance().newFactory(CCTDestination.LEGACY_INSTANCE);
    }

    @Override // com.google.firebase.components.ComponentRegistrar
    public List<Component> getComponents() {
        return Collections.singletonList(Component.builder(TransportFactory.class).add(Dependency.required(Context.class)).factory(new ComponentFactory() { // from class: com.google.firebase.datatransport.TransportRegistrar$$ExternalSyntheticLambda0
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                TransportFactory lambda$getComponents$0;
                lambda$getComponents$0 = TransportRegistrar.lambda$getComponents$0(componentContainer);
                return lambda$getComponents$0;
            }
        }).build());
    }
}
