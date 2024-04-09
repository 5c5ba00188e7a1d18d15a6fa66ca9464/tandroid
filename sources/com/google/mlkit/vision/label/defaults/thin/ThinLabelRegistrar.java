package com.google.mlkit.vision.label.defaults.thin;

import com.google.android.gms.internal.mlkit_vision_label.zzbe;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import com.google.mlkit.common.sdkinternal.ExecutorSelector;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.vision.common.internal.MultiFlavorDetectorCreator;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public class ThinLabelRegistrar implements ComponentRegistrar {
    @Override // com.google.firebase.components.ComponentRegistrar
    public final List getComponents() {
        return zzbe.zzj(Component.builder(zze.class).add(Dependency.required(MlKitContext.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.vision.label.defaults.thin.zzi
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new zze((MlKitContext) componentContainer.get(MlKitContext.class));
            }
        }).build(), Component.builder(zzd.class).add(Dependency.required(zze.class)).add(Dependency.required(ExecutorSelector.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.vision.label.defaults.thin.zzj
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new zzd((zze) componentContainer.get(zze.class), (ExecutorSelector) componentContainer.get(ExecutorSelector.class));
            }
        }).build(), Component.intoSetBuilder(MultiFlavorDetectorCreator.Registration.class).add(Dependency.requiredProvider(zzd.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.vision.label.defaults.thin.zzk
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return new MultiFlavorDetectorCreator.Registration(ImageLabelerOptions.class, componentContainer.getProvider(zzd.class), 0);
            }
        }).build());
    }
}
