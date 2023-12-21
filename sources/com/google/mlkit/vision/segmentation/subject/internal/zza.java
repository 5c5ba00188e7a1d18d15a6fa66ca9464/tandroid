package com.google.mlkit.vision.segmentation.subject.internal;

import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.mlkit.common.sdkinternal.MlKitContext;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
public final /* synthetic */ class zza implements ComponentFactory {
    public static final /* synthetic */ zza zza = new zza();

    private /* synthetic */ zza() {
    }

    @Override // com.google.firebase.components.ComponentFactory
    public final Object create(ComponentContainer componentContainer) {
        return new zze((MlKitContext) componentContainer.get(MlKitContext.class));
    }
}
