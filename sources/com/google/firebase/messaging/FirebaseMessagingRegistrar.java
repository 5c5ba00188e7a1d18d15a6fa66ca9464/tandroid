package com.google.firebase.messaging;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.android.datatransport.TransportFactory;
import com.google.firebase.FirebaseApp;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import com.google.firebase.events.Subscriber;
import com.google.firebase.heartbeatinfo.HeartBeatInfo;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.installations.FirebaseInstallationsApi;
import com.google.firebase.platforminfo.LibraryVersionComponent;
import com.google.firebase.platforminfo.UserAgentPublisher;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class FirebaseMessagingRegistrar implements ComponentRegistrar {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static final /* synthetic */ FirebaseMessaging lambda$getComponents$0$FirebaseMessagingRegistrar(ComponentContainer componentContainer) {
        FirebaseApp firebaseApp = (FirebaseApp) componentContainer.get(FirebaseApp.class);
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(componentContainer.get(FirebaseInstanceIdInternal.class));
        return new FirebaseMessaging(firebaseApp, null, componentContainer.getProvider(UserAgentPublisher.class), componentContainer.getProvider(HeartBeatInfo.class), (FirebaseInstallationsApi) componentContainer.get(FirebaseInstallationsApi.class), (TransportFactory) componentContainer.get(TransportFactory.class), (Subscriber) componentContainer.get(Subscriber.class));
    }

    @Override // com.google.firebase.components.ComponentRegistrar
    public List<Component> getComponents() {
        return Arrays.asList(Component.builder(FirebaseMessaging.class).add(Dependency.required(FirebaseApp.class)).add(Dependency.optional(FirebaseInstanceIdInternal.class)).add(Dependency.optionalProvider(UserAgentPublisher.class)).add(Dependency.optionalProvider(HeartBeatInfo.class)).add(Dependency.optional(TransportFactory.class)).add(Dependency.required(FirebaseInstallationsApi.class)).add(Dependency.required(Subscriber.class)).factory(FirebaseMessagingRegistrar$$Lambda$0.$instance).alwaysEager().build(), LibraryVersionComponent.create("fire-fcm", "22.0.0"));
    }
}
