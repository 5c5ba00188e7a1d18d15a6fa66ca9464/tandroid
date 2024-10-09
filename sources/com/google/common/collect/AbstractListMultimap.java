package com.google.common.collect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
abstract class AbstractListMultimap extends AbstractMapBasedMultimap implements ListMultimap {
    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractListMultimap(Map map) {
        super(map);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Map asMap() {
        return super.asMap();
    }

    @Override // com.google.common.collect.AbstractMultimap
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public List get(Object obj) {
        return (List) super.get(obj);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public boolean put(Object obj, Object obj2) {
        return super.put(obj, obj2);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    Collection unmodifiableCollectionSubclass(Collection collection) {
        return Collections.unmodifiableList((List) collection);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    Collection wrapCollection(Object obj, Collection collection) {
        return wrapList(obj, (List) collection, null);
    }
}
