package com.google.common.collect;

import java.io.Serializable;
/* loaded from: classes.dex */
class ImmutableEntry extends AbstractMapEntry implements Serializable {
    final Object key;
    final Object value;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableEntry(Object obj, Object obj2) {
        this.key = obj;
        this.value = obj2;
    }

    @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
    public final Object getKey() {
        return this.key;
    }

    @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
    public final Object getValue() {
        return this.value;
    }

    @Override // java.util.Map.Entry
    public final Object setValue(Object obj) {
        throw new UnsupportedOperationException();
    }
}
