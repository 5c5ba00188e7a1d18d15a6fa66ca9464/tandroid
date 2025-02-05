package kotlin.collections;

import java.util.List;

/* loaded from: classes.dex */
public abstract class AbstractMutableList extends java.util.AbstractList implements List {
    protected AbstractMutableList() {
    }

    public abstract int getSize();

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ Object remove(int i) {
        return removeAt(i);
    }

    public abstract Object removeAt(int i);

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ int size() {
        return getSize();
    }
}
