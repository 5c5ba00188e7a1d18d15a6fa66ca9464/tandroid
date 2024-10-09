package j$.util;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.stream.Stream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/* loaded from: classes2.dex */
public final /* synthetic */ class Collection$-EL {
    public static void a(Collection collection, Consumer consumer) {
        if (collection instanceof b) {
            ((b) collection).forEach(consumer);
            return;
        }
        consumer.getClass();
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            consumer.r(it.next());
        }
    }

    public static Q b(Collection collection) {
        if (collection instanceof b) {
            return ((b) collection).spliterator();
        }
        if (collection instanceof LinkedHashSet) {
            LinkedHashSet linkedHashSet = (LinkedHashSet) collection;
            linkedHashSet.getClass();
            return new d0(linkedHashSet, 17);
        }
        if (collection instanceof SortedSet) {
            SortedSet sortedSet = (SortedSet) collection;
            return new B(sortedSet, sortedSet);
        }
        if (collection instanceof Set) {
            Set set = (Set) collection;
            set.getClass();
            return new d0(set, 1);
        }
        if (!(collection instanceof List)) {
            collection.getClass();
            return new d0(collection, 0);
        }
        List list = (List) collection;
        list.getClass();
        return new d0(list, 16);
    }

    public static /* synthetic */ boolean removeIf(Collection collection, Predicate predicate) {
        return collection instanceof b ? ((b) collection).a(predicate) : a.l(collection, predicate);
    }

    public static /* synthetic */ Stream stream(Collection collection) {
        return collection instanceof b ? ((b) collection).stream() : a.m(collection);
    }
}
