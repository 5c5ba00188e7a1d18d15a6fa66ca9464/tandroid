package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: classes.dex */
public abstract class Iterators {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum EmptyModifiableIterator implements Iterator {
        INSTANCE;

        @Override // java.util.Iterator
        public boolean hasNext() {
            return false;
        }

        @Override // java.util.Iterator
        public Object next() {
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            CollectPreconditions.checkRemove(false);
        }
    }

    public static boolean addAll(Collection collection, Iterator it) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(it);
        boolean z = false;
        while (it.hasNext()) {
            z |= collection.add(it.next());
        }
        return z;
    }

    public static boolean any(Iterator it, Predicate predicate) {
        return indexOf(it, predicate) != -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void clear(Iterator it) {
        Preconditions.checkNotNull(it);
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0021, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0014, code lost:
    
        if (r2.hasNext() == false) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x001e, code lost:
    
        if (r3.equals(r2.next()) == false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0020, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:?, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:2:0x0001, code lost:
    
        if (r3 == null) goto L4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x0007, code lost:
    
        if (r2.hasNext() == false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x000d, code lost:
    
        if (r2.next() != null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x000f, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean contains(Iterator it, Object obj) {
    }

    public static boolean elementsEqual(Iterator it, Iterator it2) {
        while (it.hasNext()) {
            if (!it2.hasNext() || !Objects.equal(it.next(), it2.next())) {
                return false;
            }
        }
        return !it2.hasNext();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Iterator emptyModifiableIterator() {
        return EmptyModifiableIterator.INSTANCE;
    }

    public static UnmodifiableIterator filter(final Iterator it, final Predicate predicate) {
        Preconditions.checkNotNull(it);
        Preconditions.checkNotNull(predicate);
        return new AbstractIterator() { // from class: com.google.common.collect.Iterators.5
            @Override // com.google.common.collect.AbstractIterator
            protected Object computeNext() {
                while (it.hasNext()) {
                    Object next = it.next();
                    if (predicate.apply(next)) {
                        return next;
                    }
                }
                return endOfData();
            }
        };
    }

    public static Object find(Iterator it, Predicate predicate) {
        Preconditions.checkNotNull(it);
        Preconditions.checkNotNull(predicate);
        while (it.hasNext()) {
            Object next = it.next();
            if (predicate.apply(next)) {
                return next;
            }
        }
        throw new NoSuchElementException();
    }

    public static Object getLast(Iterator it) {
        Object next;
        do {
            next = it.next();
        } while (it.hasNext());
        return next;
    }

    public static Object getLast(Iterator it, Object obj) {
        return it.hasNext() ? getLast(it) : obj;
    }

    public static Object getNext(Iterator it, Object obj) {
        return it.hasNext() ? it.next() : obj;
    }

    public static int indexOf(Iterator it, Predicate predicate) {
        Preconditions.checkNotNull(predicate, "predicate");
        int i = 0;
        while (it.hasNext()) {
            if (predicate.apply(it.next())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object pollNext(Iterator it) {
        if (!it.hasNext()) {
            return null;
        }
        Object next = it.next();
        it.remove();
        return next;
    }

    public static boolean removeAll(Iterator it, Collection collection) {
        Preconditions.checkNotNull(collection);
        boolean z = false;
        while (it.hasNext()) {
            if (collection.contains(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public static boolean removeIf(Iterator it, Predicate predicate) {
        Preconditions.checkNotNull(predicate);
        boolean z = false;
        while (it.hasNext()) {
            if (predicate.apply(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public static UnmodifiableIterator singletonIterator(final Object obj) {
        return new UnmodifiableIterator() { // from class: com.google.common.collect.Iterators.9
            boolean done;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return !this.done;
            }

            @Override // java.util.Iterator
            public Object next() {
                if (this.done) {
                    throw new NoSuchElementException();
                }
                this.done = true;
                return obj;
            }
        };
    }
}
