package com.google.firebase.heartbeatinfo;

import android.content.Context;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.Dependency;
import com.google.firebase.components.Lazy;
import com.google.firebase.heartbeatinfo.HeartBeatInfo;
import com.google.firebase.inject.Provider;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class DefaultHeartBeatInfo implements HeartBeatInfo {
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() { // from class: com.google.firebase.heartbeatinfo.DefaultHeartBeatInfo$$ExternalSyntheticLambda0
        @Override // java.util.concurrent.ThreadFactory
        public final Thread newThread(Runnable runnable) {
            Thread lambda$static$0;
            lambda$static$0 = DefaultHeartBeatInfo.lambda$static$0(runnable);
            return lambda$static$0;
        }
    };
    private final Executor backgroundExecutor;
    private final Set consumers;
    private Provider storageProvider;

    private DefaultHeartBeatInfo(final Context context, Set set) {
        this(new Lazy(new Provider() { // from class: com.google.firebase.heartbeatinfo.DefaultHeartBeatInfo$$ExternalSyntheticLambda2
            @Override // com.google.firebase.inject.Provider
            public final Object get() {
                HeartBeatInfoStorage heartBeatInfoStorage;
                heartBeatInfoStorage = HeartBeatInfoStorage.getInstance(context);
                return heartBeatInfoStorage;
            }
        }), set, new ThreadPoolExecutor(0, 1, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue(), THREAD_FACTORY));
    }

    DefaultHeartBeatInfo(Provider provider, Set set, Executor executor) {
        this.storageProvider = provider;
        this.consumers = set;
        this.backgroundExecutor = executor;
    }

    public static Component component() {
        return Component.builder(HeartBeatInfo.class).add(Dependency.required(Context.class)).add(Dependency.setOf(HeartBeatConsumer.class)).factory(new ComponentFactory() { // from class: com.google.firebase.heartbeatinfo.DefaultHeartBeatInfo$$ExternalSyntheticLambda1
            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                HeartBeatInfo lambda$component$4;
                lambda$component$4 = DefaultHeartBeatInfo.lambda$component$4(componentContainer);
                return lambda$component$4;
            }
        }).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ HeartBeatInfo lambda$component$4(ComponentContainer componentContainer) {
        return new DefaultHeartBeatInfo((Context) componentContainer.get(Context.class), componentContainer.setOf(HeartBeatConsumer.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Thread lambda$static$0(Runnable runnable) {
        return new Thread(runnable, "heartbeat-information-executor");
    }

    @Override // com.google.firebase.heartbeatinfo.HeartBeatInfo
    public HeartBeatInfo.HeartBeat getHeartBeatCode(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        boolean shouldSendSdkHeartBeat = ((HeartBeatInfoStorage) this.storageProvider.get()).shouldSendSdkHeartBeat(str, currentTimeMillis);
        boolean shouldSendGlobalHeartBeat = ((HeartBeatInfoStorage) this.storageProvider.get()).shouldSendGlobalHeartBeat(currentTimeMillis);
        return (shouldSendSdkHeartBeat && shouldSendGlobalHeartBeat) ? HeartBeatInfo.HeartBeat.COMBINED : shouldSendGlobalHeartBeat ? HeartBeatInfo.HeartBeat.GLOBAL : shouldSendSdkHeartBeat ? HeartBeatInfo.HeartBeat.SDK : HeartBeatInfo.HeartBeat.NONE;
    }
}
