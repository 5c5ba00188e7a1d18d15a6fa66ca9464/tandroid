package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/* loaded from: classes.dex */
public abstract class Lists {

    /* loaded from: classes.dex */
    private static class TransformingRandomAccessList extends AbstractList implements RandomAccess, Serializable {
        final List fromList;
        final Function function;

        TransformingRandomAccessList(List list, Function function) {
            this.fromList = (List) Preconditions.checkNotNull(list);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.fromList.clear();
        }

        @Override // java.util.AbstractList, java.util.List
        public Object get(int i) {
            return this.function.apply(this.fromList.get(i));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return this.fromList.isEmpty();
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator iterator() {
            return listIterator();
        }

        @Override // java.util.AbstractList, java.util.List
        public ListIterator listIterator(int i) {
            return new TransformedListIterator(this.fromList.listIterator(i)) { // from class: com.google.common.collect.Lists.TransformingRandomAccessList.1
                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.google.common.collect.TransformedIterator
                public Object transform(Object obj) {
                    return TransformingRandomAccessList.this.function.apply(obj);
                }
            };
        }

        @Override // java.util.AbstractList, java.util.List
        public Object remove(int i) {
            return this.function.apply(this.fromList.remove(i));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.fromList.size();
        }
    }

    /* loaded from: classes.dex */
    private static class TransformingSequentialList extends AbstractSequentialList implements Serializable {
        final List fromList;
        final Function function;

        TransformingSequentialList(List list, Function function) {
            this.fromList = (List) Preconditions.checkNotNull(list);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.fromList.clear();
        }

        @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public ListIterator listIterator(int i) {
            return new TransformedListIterator(this.fromList.listIterator(i)) { // from class: com.google.common.collect.Lists.TransformingSequentialList.1
                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.google.common.collect.TransformedIterator
                public Object transform(Object obj) {
                    return TransformingSequentialList.this.function.apply(obj);
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.fromList.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List cast(Iterable iterable) {
        return (List) iterable;
    }

    static int computeArrayListCapacity(int i) {
        CollectPreconditions.checkNonnegative(i, "arraySize");
        return Ints.saturatedCast(i + 5 + (i / 10));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean equalsImpl(List list, Object obj) {
        if (obj == Preconditions.checkNotNull(list)) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        List list2 = (List) obj;
        int size = list.size();
        if (size != list2.size()) {
            return false;
        }
        if (!(list instanceof RandomAccess) || !(list2 instanceof RandomAccess)) {
            return Iterators.elementsEqual(list.iterator(), list2.iterator());
        }
        for (int i = 0; i < size; i++) {
            if (!Objects.equal(list.get(i), list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int indexOfImpl(List list, Object obj) {
        if (list instanceof RandomAccess) {
            return indexOfRandomAccess(list, obj);
        }
        ListIterator listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            if (Objects.equal(obj, listIterator.next())) {
                return listIterator.previousIndex();
            }
        }
        return -1;
    }

    private static int indexOfRandomAccess(List list, Object obj) {
        int size = list.size();
        int i = 0;
        if (obj == null) {
            while (i < size) {
                if (list.get(i) == null) {
                    return i;
                }
                i++;
            }
            return -1;
        }
        while (i < size) {
            if (obj.equals(list.get(i))) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int lastIndexOfImpl(List list, Object obj) {
        if (list instanceof RandomAccess) {
            return lastIndexOfRandomAccess(list, obj);
        }
        ListIterator listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            if (Objects.equal(obj, listIterator.previous())) {
                return listIterator.nextIndex();
            }
        }
        return -1;
    }

    private static int lastIndexOfRandomAccess(List list, Object obj) {
        if (obj == null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                if (list.get(size) == null) {
                    return size;
                }
            }
            return -1;
        }
        for (int size2 = list.size() - 1; size2 >= 0; size2--) {
            if (obj.equals(list.get(size2))) {
                return size2;
            }
        }
        return -1;
    }

    public static ArrayList newArrayList() {
        return new ArrayList();
    }

    public static ArrayList newArrayList(Iterator it) {
        ArrayList newArrayList = newArrayList();
        Iterators.addAll(newArrayList, it);
        return newArrayList;
    }

    public static ArrayList newArrayList(Object... objArr) {
        Preconditions.checkNotNull(objArr);
        ArrayList arrayList = new ArrayList(computeArrayListCapacity(objArr.length));
        Collections.addAll(arrayList, objArr);
        return arrayList;
    }

    public static List transform(List list, Function function) {
        return list instanceof RandomAccess ? new TransformingRandomAccessList(list, function) : new TransformingSequentialList(list, function);
    }
}
