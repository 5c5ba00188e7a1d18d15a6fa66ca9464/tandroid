package com.google.mlkit.common.internal;

import com.google.android.gms.internal.mlkit_common.zzaq;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import com.google.mlkit.common.model.CustomRemoteModel;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.common.sdkinternal.Cleaner;
import com.google.mlkit.common.sdkinternal.CloseGuard$Factory;
import com.google.mlkit.common.sdkinternal.ExecutorSelector;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.common.sdkinternal.MlKitThreadPool;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import com.google.mlkit.common.sdkinternal.model.ModelFileHelper;
import java.util.List;

/* loaded from: classes.dex */
public class CommonComponentRegistrar implements ComponentRegistrar {
    @Override // com.google.firebase.components.ComponentRegistrar
    public final List getComponents() {
        return zzaq.zzi(SharedPrefManager.COMPONENT, Component.builder(ModelFileHelper.class).add(Dependency.required(MlKitContext.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.common.internal.zza
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new ModelFileHelper((MlKitContext) componentContainer.get(MlKitContext.class));
            }
        }).build(), Component.builder(MlKitThreadPool.class).factory(new ComponentFactory() { // from class: com.google.mlkit.common.internal.zzb
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new MlKitThreadPool();
            }
        }).build(), Component.builder(RemoteModelManager.class).add(Dependency.setOf(RemoteModelManager.RemoteModelManagerRegistration.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.common.internal.zzc
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new RemoteModelManager(componentContainer.setOf(RemoteModelManager.RemoteModelManagerRegistration.class));
            }
        }).build(), Component.builder(ExecutorSelector.class).add(Dependency.requiredProvider(MlKitThreadPool.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.common.internal.zzd
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new ExecutorSelector(componentContainer.getProvider(MlKitThreadPool.class));
            }
        }).build(), Component.builder(Cleaner.class).factory(new ComponentFactory() { // from class: com.google.mlkit.common.internal.zze
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return Cleaner.create();
            }
        }).build(), Component.builder(CloseGuard$Factory.class).add(Dependency.required(Cleaner.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.common.internal.zzf
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new Object((Cleaner) componentContainer.get(Cleaner.class)) { // from class: com.google.mlkit.common.sdkinternal.CloseGuard$Factory
                    private final Cleaner zza;

                    {
                        this.zza = r1;
                    }
                };
            }
        }).build(), Component.builder(com.google.mlkit.common.internal.model.zzg.class).add(Dependency.required(MlKitContext.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.common.internal.zzg
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new com.google.mlkit.common.internal.model.zzg((MlKitContext) componentContainer.get(MlKitContext.class));
            }
        }).build(), Component.intoSetBuilder(RemoteModelManager.RemoteModelManagerRegistration.class).add(Dependency.requiredProvider(com.google.mlkit.common.internal.model.zzg.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.common.internal.zzh
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new RemoteModelManager.RemoteModelManagerRegistration(CustomRemoteModel.class, componentContainer.getProvider(com.google.mlkit.common.internal.model.zzg.class));
            }
        }).build());
    }
}
