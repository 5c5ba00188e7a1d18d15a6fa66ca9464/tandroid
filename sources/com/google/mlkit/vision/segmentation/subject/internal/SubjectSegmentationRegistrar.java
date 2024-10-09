package com.google.mlkit.vision.segmentation.subject.internal;

import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzav;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import com.google.mlkit.common.sdkinternal.ExecutorSelector;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import java.util.List;

/* loaded from: classes.dex */
public class SubjectSegmentationRegistrar implements ComponentRegistrar {
    @Override // com.google.firebase.components.ComponentRegistrar
    public final List getComponents() {
        return zzav.zzi(Component.builder(zze.class).add(Dependency.required(MlKitContext.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.vision.segmentation.subject.internal.zza
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new zze((MlKitContext) componentContainer.get(MlKitContext.class));
            }
        }).build(), Component.builder(zzc.class).add(Dependency.required(zze.class)).add(Dependency.required(ExecutorSelector.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.vision.segmentation.subject.internal.zzb
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new zzc((zze) componentContainer.get(zze.class), (ExecutorSelector) componentContainer.get(ExecutorSelector.class));
            }
        }).build());
    }
}
