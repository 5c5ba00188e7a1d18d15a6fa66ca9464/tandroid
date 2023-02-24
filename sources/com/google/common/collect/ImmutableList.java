package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
/* loaded from: classes.dex */
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess {
    private static final UnmodifiableListIterator<Object> EMPTY_ITR = new Itr(RegularImmutableList.EMPTY, 0);

    @Override // com.google.common.collect.ImmutableCollection
    @Deprecated
    public final ImmutableList<E> asList() {
        return this;
    }

    public static <E> ImmutableList<E> of() {
        return (ImmutableList<E>) RegularImmutableList.EMPTY;
    }

    public static <E> ImmutableList<E> of(E e) {
        return construct(e);
    }

    public static <E> ImmutableList<E> of(E e, E e2) {
        return construct(e, e2);
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3) {
        return construct(e, e2, e3);
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5) {
        return construct(e, e2, e3, e4, e5);
    }

    public static <E> ImmutableList<E> copyOf(Collection<? extends E> collection) {
        if (collection instanceof ImmutableCollection) {
            ImmutableList<E> asList = ((ImmutableCollection) collection).asList();
            return asList.isPartialView() ? asImmutableList(asList.toArray()) : asList;
        }
        return construct(collection.toArray());
    }

    public static <E> ImmutableList<E> copyOf(E[] eArr) {
        if (eArr.length == 0) {
            return of();
        }
        return construct((Object[]) eArr.clone());
    }

    public static <E> ImmutableList<E> sortedCopyOf(Comparator<? super E> comparator, Iterable<? extends E> iterable) {
        Preconditions.checkNotNull(comparator);
        Object[] array = Iterables.toArray(iterable);
        ObjectArrays.checkElementsNotNull(array);
        Arrays.sort(array, comparator);
        return asImmutableList(array);
    }

    private static <E> ImmutableList<E> construct(Object... objArr) {
        return asImmutableList(ObjectArrays.checkElementsNotNull(objArr));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> ImmutableList<E> asImmutableList(Object[] objArr) {
        return asImmutableList(objArr, objArr.length);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> ImmutableList<E> asImmutableList(Object[] objArr, int i) {
        if (i == 0) {
            return of();
        }
        return new RegularImmutableList(objArr, i);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public UnmodifiableIterator<E> iterator() {
        return listIterator();
    }

    @Override // java.util.List
    public UnmodifiableListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override // java.util.List
    public UnmodifiableListIterator<E> listIterator(int i) {
        Preconditions.checkPositionIndex(i, size());
        if (isEmpty()) {
            return (UnmodifiableListIterator<E>) EMPTY_ITR;
        }
        return new Itr(this, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Itr<E> extends AbstractIndexedListIterator<E> {
        private final ImmutableList<E> list;

        Itr(ImmutableList<E> immutableList, int i) {
            super(immutableList.size(), i);
            this.list = immutableList;
        }

        @Override // com.google.common.collect.AbstractIndexedListIterator
        protected E get(int i) {
            return this.list.get(i);
        }
    }

    @Override // java.util.List
    public int indexOf(Object obj) {
        if (obj == null) {
            return -1;
        }
        return Lists.indexOfImpl(this, obj);
    }

    @Override // java.util.List
    public int lastIndexOf(Object obj) {
        if (obj == null) {
            return -1;
        }
        return Lists.lastIndexOfImpl(this, obj);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    @Override // java.util.List
    public ImmutableList<E> subList(int i, int i2) {
        Preconditions.checkPositionIndexes(i, i2, size());
        int i3 = i2 - i;
        if (i3 == size()) {
            return this;
        }
        if (i3 == 0) {
            return of();
        }
        return subListUnchecked(i, i2);
    }

    ImmutableList<E> subListUnchecked(int i, int i2) {
        return new SubList(i, i2 - i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class SubList extends ImmutableList<E> {
        final transient int length;
        final transient int offset;

        @Override // com.google.common.collect.ImmutableCollection
        boolean isPartialView() {
            return true;
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator listIterator() {
            return super.listIterator();
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator listIterator(int i) {
            return super.listIterator(i);
        }

        SubList(int i, int i2) {
            this.offset = i;
            this.length = i2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.length;
        }

        @Override // com.google.common.collect.ImmutableCollection
        Object[] internalArray() {
            return ImmutableList.this.internalArray();
        }

        @Override // com.google.common.collect.ImmutableCollection
        int internalArrayStart() {
            return ImmutableList.this.internalArrayStart() + this.offset;
        }

        @Override // com.google.common.collect.ImmutableCollection
        int internalArrayEnd() {
            return ImmutableList.this.internalArrayStart() + this.offset + this.length;
        }

        @Override // java.util.List
        public E get(int i) {
            Preconditions.checkElementIndex(i, this.length);
            return ImmutableList.this.get(i + this.offset);
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public ImmutableList<E> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, this.length);
            ImmutableList immutableList = ImmutableList.this;
            int i3 = this.offset;
            return immutableList.subList(i + i3, i2 + i3);
        }
    }

    @Override // java.util.List
    @Deprecated
    public final boolean addAll(int i, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    @Deprecated
    public final E set(int i, E e) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    @Deprecated
    public final void add(int i, E e) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    @Deprecated
    public final E remove(int i) {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int copyIntoArray(Object[] objArr, int i) {
        int size = size();
        for (int i2 = 0; i2 < size; i2++) {
            objArr[i + i2] = get(i2);
        }
        return i + size;
    }

    @Override // java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        return Lists.equalsImpl(this, obj);
    }

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        int size = size();
        int i = 1;
        for (int i2 = 0; i2 < size; i2++) {
            i = (((i * 31) + get(i2).hashCode()) ^ (-1)) ^ (-1);
        }
        return i;
    }

    public static <E> Builder<E> builder() {
        return new Builder<>();
    }

    /* loaded from: classes.dex */
    public static final class Builder<E> extends ImmutableCollection.ArrayBasedBuilder<E> {
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object obj) {
            return add((Builder<E>) obj);
        }

        public Builder() {
            this(4);
        }

        Builder(int i) {
            super(i);
        }

        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E e) {
            super.add((Builder<E>) e);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterable<? extends E> iterable) {
            super.addAll((Iterable) iterable);
            return this;
        }

        public ImmutableList<E> build() {
            this.forceCopy = true;
            return ImmutableList.asImmutableList(this.contents, this.size);
        }
    }
}
