package com.google.firebase.components;

import com.google.firebase.events.Publisher;
import com.google.firebase.inject.Provider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class RestrictedComponentContainer extends AbstractComponentContainer {
    private final Set allowedDeferredInterfaces;
    private final Set allowedDirectInterfaces;
    private final Set allowedProviderInterfaces;
    private final Set allowedPublishedEvents;
    private final Set allowedSetDirectInterfaces;
    private final Set allowedSetProviderInterfaces;
    private final ComponentContainer delegateContainer;

    /* loaded from: classes.dex */
    private static class RestrictedPublisher implements Publisher {
        private final Set allowedPublishedEvents;
        private final Publisher delegate;

        public RestrictedPublisher(Set set, Publisher publisher) {
            this.allowedPublishedEvents = set;
            this.delegate = publisher;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RestrictedComponentContainer(Component component, ComponentContainer componentContainer) {
        HashSet hashSet = new HashSet();
        HashSet hashSet2 = new HashSet();
        HashSet hashSet3 = new HashSet();
        HashSet hashSet4 = new HashSet();
        HashSet hashSet5 = new HashSet();
        for (Dependency dependency : component.getDependencies()) {
            if (dependency.isDirectInjection()) {
                boolean isSet = dependency.isSet();
                Class cls = dependency.getInterface();
                if (isSet) {
                    hashSet4.add(cls);
                } else {
                    hashSet.add(cls);
                }
            } else if (dependency.isDeferred()) {
                hashSet3.add(dependency.getInterface());
            } else {
                boolean isSet2 = dependency.isSet();
                Class cls2 = dependency.getInterface();
                if (isSet2) {
                    hashSet5.add(cls2);
                } else {
                    hashSet2.add(cls2);
                }
            }
        }
        if (!component.getPublishedEvents().isEmpty()) {
            hashSet.add(Publisher.class);
        }
        this.allowedDirectInterfaces = Collections.unmodifiableSet(hashSet);
        this.allowedProviderInterfaces = Collections.unmodifiableSet(hashSet2);
        this.allowedDeferredInterfaces = Collections.unmodifiableSet(hashSet3);
        this.allowedSetDirectInterfaces = Collections.unmodifiableSet(hashSet4);
        this.allowedSetProviderInterfaces = Collections.unmodifiableSet(hashSet5);
        this.allowedPublishedEvents = component.getPublishedEvents();
        this.delegateContainer = componentContainer;
    }

    @Override // com.google.firebase.components.AbstractComponentContainer, com.google.firebase.components.ComponentContainer
    public Object get(Class cls) {
        if (this.allowedDirectInterfaces.contains(cls)) {
            Object obj = this.delegateContainer.get(cls);
            return !cls.equals(Publisher.class) ? obj : new RestrictedPublisher(this.allowedPublishedEvents, (Publisher) obj);
        }
        throw new DependencyException(String.format("Attempting to request an undeclared dependency %s.", cls));
    }

    @Override // com.google.firebase.components.ComponentContainer
    public Provider getProvider(Class cls) {
        if (this.allowedProviderInterfaces.contains(cls)) {
            return this.delegateContainer.getProvider(cls);
        }
        throw new DependencyException(String.format("Attempting to request an undeclared dependency Provider<%s>.", cls));
    }

    @Override // com.google.firebase.components.AbstractComponentContainer, com.google.firebase.components.ComponentContainer
    public Set setOf(Class cls) {
        if (this.allowedSetDirectInterfaces.contains(cls)) {
            return this.delegateContainer.setOf(cls);
        }
        throw new DependencyException(String.format("Attempting to request an undeclared dependency Set<%s>.", cls));
    }

    @Override // com.google.firebase.components.ComponentContainer
    public Provider setOfProvider(Class cls) {
        if (this.allowedSetProviderInterfaces.contains(cls)) {
            return this.delegateContainer.setOfProvider(cls);
        }
        throw new DependencyException(String.format("Attempting to request an undeclared dependency Provider<Set<%s>>.", cls));
    }
}
