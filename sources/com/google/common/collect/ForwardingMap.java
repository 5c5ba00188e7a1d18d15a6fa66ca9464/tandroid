package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class ForwardingMap<K, V> extends ForwardingObject implements Map<K, V> {
    @Override // com.google.common.collect.ForwardingObject
    protected abstract Map<K, V> delegate();

    public int size() {
        return delegate().size();
    }

    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    @Override // java.util.Map
    public V remove(Object obj) {
        return delegate().remove(obj);
    }

    @Override // java.util.Map
    public void clear() {
        delegate().clear();
    }

    public boolean containsKey(Object obj) {
        return delegate().containsKey(obj);
    }

    public V get(Object obj) {
        return delegate().get(obj);
    }

    @Override // java.util.Map
    public V put(K k, V v) {
        return delegate().put(k, v);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        delegate().putAll(map);
    }

    public Set<K> keySet() {
        return delegate().keySet();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return delegate().values();
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return delegate().entrySet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean standardContainsValue(Object obj) {
        return Maps.containsValueImpl(this, obj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean standardEquals(Object obj) {
        return Maps.equalsImpl(this, obj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int standardHashCode() {
        return Sets.hashCodeImpl(entrySet());
    }
}
