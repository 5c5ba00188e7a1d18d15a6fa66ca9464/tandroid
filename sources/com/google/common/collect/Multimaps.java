package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.AbstractCollection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public abstract class Multimaps {

    private static class CustomListMultimap extends AbstractListMultimap {
        transient Supplier factory;

        CustomListMultimap(Map map, Supplier supplier) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(supplier);
        }

        @Override // com.google.common.collect.AbstractMultimap
        Map createAsMap() {
            return createMaybeNavigableAsMap();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.AbstractMapBasedMultimap
        public List createCollection() {
            return (List) this.factory.get();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Set createKeySet() {
            return createMaybeNavigableKeySet();
        }
    }

    static abstract class Entries extends AbstractCollection {
        Entries() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            multimap().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return multimap().containsEntry(entry.getKey(), entry.getValue());
        }

        abstract Multimap multimap();

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return multimap().remove(entry.getKey(), entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return multimap().size();
        }
    }

    static boolean equalsImpl(Multimap multimap, Object obj) {
        if (obj == multimap) {
            return true;
        }
        if (obj instanceof Multimap) {
            return multimap.asMap().equals(((Multimap) obj).asMap());
        }
        return false;
    }

    public static ListMultimap newListMultimap(Map map, Supplier supplier) {
        return new CustomListMultimap(map, supplier);
    }
}
