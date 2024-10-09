package com.google.mlkit.vision.common.internal;

import com.google.android.gms.internal.mlkit_vision_common.zzp;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import com.google.mlkit.vision.common.internal.MultiFlavorDetectorCreator;
import java.util.List;

/* loaded from: classes.dex */
public class VisionCommonRegistrar implements ComponentRegistrar {
    @Override // com.google.firebase.components.ComponentRegistrar
    public final List getComponents() {
        return zzp.zzi(Component.builder(MultiFlavorDetectorCreator.class).add(Dependency.setOf(MultiFlavorDetectorCreator.Registration.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.vision.common.internal.zzf
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new MultiFlavorDetectorCreator(componentContainer.setOf(MultiFlavorDetectorCreator.Registration.class));
            }
        }).build());
    }
}
