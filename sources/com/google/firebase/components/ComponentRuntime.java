package com.google.firebase.components;

import android.util.Log;
import com.google.android.exoplayer2.mediacodec.AsynchronousMediaCodecBufferEnqueuer$$ExternalSyntheticBackportWithForwarding0;
import com.google.firebase.components.ComponentRuntime;
import com.google.firebase.dynamicloading.ComponentLoader;
import com.google.firebase.events.Publisher;
import com.google.firebase.events.Subscriber;
import com.google.firebase.inject.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public class ComponentRuntime extends AbstractComponentContainer implements ComponentLoader {
    private static final Provider EMPTY_PROVIDER = new Provider() { // from class: com.google.firebase.components.ComponentRuntime$$ExternalSyntheticLambda0
        @Override // com.google.firebase.inject.Provider
        public final Object get() {
            return Collections.emptySet();
        }
    };
    private final Map components;
    private final AtomicReference eagerComponentsInitializedWith;
    private final EventBus eventBus;
    private final Map lazyInstanceMap;
    private final Map lazySetMap;
    private final List unprocessedRegistrarProviders;

    /* loaded from: classes.dex */
    public static final class Builder {
        private final Executor defaultExecutor;
        private final List lazyRegistrars = new ArrayList();
        private final List additionalComponents = new ArrayList();

        Builder(Executor executor) {
            this.defaultExecutor = executor;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ ComponentRegistrar lambda$addComponentRegistrar$0(ComponentRegistrar componentRegistrar) {
            return componentRegistrar;
        }

        public Builder addComponent(Component component) {
            this.additionalComponents.add(component);
            return this;
        }

        public Builder addComponentRegistrar(final ComponentRegistrar componentRegistrar) {
            this.lazyRegistrars.add(new Provider() { // from class: com.google.firebase.components.ComponentRuntime$Builder$$ExternalSyntheticLambda0
                @Override // com.google.firebase.inject.Provider
                public final Object get() {
                    ComponentRegistrar lambda$addComponentRegistrar$0;
                    lambda$addComponentRegistrar$0 = ComponentRuntime.Builder.lambda$addComponentRegistrar$0(ComponentRegistrar.this);
                    return lambda$addComponentRegistrar$0;
                }
            });
            return this;
        }

        public Builder addLazyComponentRegistrars(Collection collection) {
            this.lazyRegistrars.addAll(collection);
            return this;
        }

        public ComponentRuntime build() {
            return new ComponentRuntime(this.defaultExecutor, this.lazyRegistrars, this.additionalComponents);
        }
    }

    private ComponentRuntime(Executor executor, Iterable iterable, Collection collection) {
        this.components = new HashMap();
        this.lazyInstanceMap = new HashMap();
        this.lazySetMap = new HashMap();
        this.eagerComponentsInitializedWith = new AtomicReference();
        EventBus eventBus = new EventBus(executor);
        this.eventBus = eventBus;
        ArrayList arrayList = new ArrayList();
        arrayList.add(Component.of(eventBus, EventBus.class, Subscriber.class, Publisher.class));
        arrayList.add(Component.of(this, ComponentLoader.class, new Class[0]));
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            Component component = (Component) it.next();
            if (component != null) {
                arrayList.add(component);
            }
        }
        this.unprocessedRegistrarProviders = iterableToList(iterable);
        discoverComponents(arrayList);
    }

    public static Builder builder(Executor executor) {
        return new Builder(executor);
    }

    private void discoverComponents(List list) {
        ArrayList arrayList = new ArrayList();
        synchronized (this) {
            Iterator it = this.unprocessedRegistrarProviders.iterator();
            while (it.hasNext()) {
                try {
                    ComponentRegistrar componentRegistrar = (ComponentRegistrar) ((Provider) it.next()).get();
                    if (componentRegistrar != null) {
                        list.addAll(componentRegistrar.getComponents());
                        it.remove();
                    }
                } catch (InvalidRegistrarException e) {
                    it.remove();
                    Log.w("ComponentDiscovery", "Invalid component registrar.", e);
                }
            }
            if (this.components.isEmpty()) {
                CycleDetector.detect(list);
            } else {
                ArrayList arrayList2 = new ArrayList(this.components.keySet());
                arrayList2.addAll(list);
                CycleDetector.detect(arrayList2);
            }
            Iterator it2 = list.iterator();
            while (it2.hasNext()) {
                final Component component = (Component) it2.next();
                this.components.put(component, new Lazy(new Provider() { // from class: com.google.firebase.components.ComponentRuntime$$ExternalSyntheticLambda1
                    @Override // com.google.firebase.inject.Provider
                    public final Object get() {
                        Object lambda$discoverComponents$0;
                        lambda$discoverComponents$0 = ComponentRuntime.this.lambda$discoverComponents$0(component);
                        return lambda$discoverComponents$0;
                    }
                }));
            }
            arrayList.addAll(processInstanceComponents(list));
            arrayList.addAll(processSetComponents());
            processDependencies();
        }
        Iterator it3 = arrayList.iterator();
        while (it3.hasNext()) {
            ((Runnable) it3.next()).run();
        }
        maybeInitializeEagerComponents();
    }

    private void doInitializeEagerComponents(Map map, boolean z) {
        for (Map.Entry entry : map.entrySet()) {
            Component component = (Component) entry.getKey();
            Provider provider = (Provider) entry.getValue();
            if (component.isAlwaysEager() || (component.isEagerInDefaultApp() && z)) {
                provider.get();
            }
        }
        this.eventBus.enablePublishingAndFlushPending();
    }

    private static List iterableToList(Iterable iterable) {
        ArrayList arrayList = new ArrayList();
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object lambda$discoverComponents$0(Component component) {
        return component.getFactory().create(new RestrictedComponentContainer(component, this));
    }

    private void maybeInitializeEagerComponents() {
        Boolean bool = (Boolean) this.eagerComponentsInitializedWith.get();
        if (bool != null) {
            doInitializeEagerComponents(this.components, bool.booleanValue());
        }
    }

    private void processDependencies() {
        Map map;
        Class cls;
        Provider empty;
        for (Component component : this.components.keySet()) {
            for (Dependency dependency : component.getDependencies()) {
                if (dependency.isSet() && !this.lazySetMap.containsKey(dependency.getInterface())) {
                    map = this.lazySetMap;
                    cls = dependency.getInterface();
                    empty = LazySet.fromCollection(Collections.emptySet());
                } else if (this.lazyInstanceMap.containsKey(dependency.getInterface())) {
                    continue;
                } else {
                    if (dependency.isRequired()) {
                        throw new MissingDependencyException(String.format("Unsatisfied dependency for component %s: %s", component, dependency.getInterface()));
                    }
                    if (!dependency.isSet()) {
                        map = this.lazyInstanceMap;
                        cls = dependency.getInterface();
                        empty = OptionalProvider.empty();
                    }
                }
                map.put(cls, empty);
            }
        }
    }

    private List processInstanceComponents(List list) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Component component = (Component) it.next();
            if (component.isValue()) {
                final Provider provider = (Provider) this.components.get(component);
                for (Class cls : component.getProvidedInterfaces()) {
                    if (this.lazyInstanceMap.containsKey(cls)) {
                        final OptionalProvider optionalProvider = (OptionalProvider) ((Provider) this.lazyInstanceMap.get(cls));
                        arrayList.add(new Runnable() { // from class: com.google.firebase.components.ComponentRuntime$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                OptionalProvider.this.set(provider);
                            }
                        });
                    } else {
                        this.lazyInstanceMap.put(cls, provider);
                    }
                }
            }
        }
        return arrayList;
    }

    private List processSetComponents() {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        for (Map.Entry entry : this.components.entrySet()) {
            Component component = (Component) entry.getKey();
            if (!component.isValue()) {
                Provider provider = (Provider) entry.getValue();
                for (Class cls : component.getProvidedInterfaces()) {
                    if (!hashMap.containsKey(cls)) {
                        hashMap.put(cls, new HashSet());
                    }
                    ((Set) hashMap.get(cls)).add(provider);
                }
            }
        }
        for (Map.Entry entry2 : hashMap.entrySet()) {
            if (this.lazySetMap.containsKey(entry2.getKey())) {
                final LazySet lazySet = (LazySet) this.lazySetMap.get(entry2.getKey());
                for (final Provider provider2 : (Set) entry2.getValue()) {
                    arrayList.add(new Runnable() { // from class: com.google.firebase.components.ComponentRuntime$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            LazySet.this.add(provider2);
                        }
                    });
                }
            } else {
                this.lazySetMap.put((Class) entry2.getKey(), LazySet.fromCollection((Collection) entry2.getValue()));
            }
        }
        return arrayList;
    }

    @Override // com.google.firebase.components.AbstractComponentContainer, com.google.firebase.components.ComponentContainer
    public /* bridge */ /* synthetic */ Object get(Class cls) {
        return super.get(cls);
    }

    @Override // com.google.firebase.components.ComponentContainer
    public synchronized Provider getProvider(Class cls) {
        Preconditions.checkNotNull(cls, "Null interface requested.");
        return (Provider) this.lazyInstanceMap.get(cls);
    }

    public void initializeEagerComponents(boolean z) {
        HashMap hashMap;
        if (AsynchronousMediaCodecBufferEnqueuer$$ExternalSyntheticBackportWithForwarding0.m(this.eagerComponentsInitializedWith, null, Boolean.valueOf(z))) {
            synchronized (this) {
                hashMap = new HashMap(this.components);
            }
            doInitializeEagerComponents(hashMap, z);
        }
    }

    @Override // com.google.firebase.components.AbstractComponentContainer, com.google.firebase.components.ComponentContainer
    public /* bridge */ /* synthetic */ Set setOf(Class cls) {
        return super.setOf(cls);
    }

    @Override // com.google.firebase.components.ComponentContainer
    public synchronized Provider setOfProvider(Class cls) {
        LazySet lazySet = (LazySet) this.lazySetMap.get(cls);
        if (lazySet != null) {
            return lazySet;
        }
        return EMPTY_PROVIDER;
    }
}
