package j$.util;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.stream.Stream;
import j$.util.stream.u0;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
/* loaded from: classes2.dex */
public abstract /* synthetic */ class a {
    public static void f(E e, Consumer consumer) {
        if (consumer instanceof j$.util.function.m) {
            e.d((j$.util.function.m) consumer);
        } else if (h0.a) {
            h0.a(e.getClass(), "{0} calling Spliterator.OfDouble.forEachRemaining((DoubleConsumer) action::accept)");
            throw null;
        } else {
            consumer.getClass();
            e.d(new o(consumer));
        }
    }

    public static void g(H h, Consumer consumer) {
        if (consumer instanceof j$.util.function.K) {
            h.c((j$.util.function.K) consumer);
        } else if (h0.a) {
            h0.a(h.getClass(), "{0} calling Spliterator.OfInt.forEachRemaining((IntConsumer) action::accept)");
            throw null;
        } else {
            consumer.getClass();
            h.c(new s(consumer));
        }
    }

    public static void h(K k, Consumer consumer) {
        if (consumer instanceof j$.util.function.h0) {
            k.b((j$.util.function.h0) consumer);
        } else if (h0.a) {
            h0.a(k.getClass(), "{0} calling Spliterator.OfLong.forEachRemaining((LongConsumer) action::accept)");
            throw null;
        } else {
            consumer.getClass();
            k.b(new w(consumer));
        }
    }

    public static long i(Q q) {
        if ((q.characteristics() & 64) == 0) {
            return -1L;
        }
        return q.estimateSize();
    }

    public static boolean k(Q q, int i) {
        return (q.characteristics() & i) == i;
    }

    public static boolean l(Collection collection, Predicate predicate) {
        if (DesugarCollections.a.isInstance(collection)) {
            return DesugarCollections.c(collection, predicate);
        }
        predicate.getClass();
        java.util.Iterator it = collection.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (predicate.test(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public static Stream m(Collection collection) {
        return u0.W0(Collection$-EL.b(collection), false);
    }

    public static boolean n(E e, Consumer consumer) {
        if (consumer instanceof j$.util.function.m) {
            return e.o((j$.util.function.m) consumer);
        }
        if (h0.a) {
            h0.a(e.getClass(), "{0} calling Spliterator.OfDouble.tryAdvance((DoubleConsumer) action::accept)");
            throw null;
        }
        consumer.getClass();
        return e.o(new o(consumer));
    }

    public static boolean p(H h, Consumer consumer) {
        if (consumer instanceof j$.util.function.K) {
            return h.j((j$.util.function.K) consumer);
        }
        if (h0.a) {
            h0.a(h.getClass(), "{0} calling Spliterator.OfInt.tryAdvance((IntConsumer) action::accept)");
            throw null;
        }
        consumer.getClass();
        return h.j(new s(consumer));
    }

    public static boolean q(K k, Consumer consumer) {
        if (consumer instanceof j$.util.function.h0) {
            return k.e((j$.util.function.h0) consumer);
        }
        if (h0.a) {
            h0.a(k.getClass(), "{0} calling Spliterator.OfLong.tryAdvance((LongConsumer) action::accept)");
            throw null;
        }
        consumer.getClass();
        return k.e(new w(consumer));
    }

    public static boolean r(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static void s(List list, Comparator comparator) {
        if (DesugarCollections.b.isInstance(list)) {
            DesugarCollections.d(list, comparator);
            return;
        }
        Object[] array = list.toArray();
        Arrays.sort(array, comparator);
        ListIterator listIterator = list.listIterator();
        for (Object obj : array) {
            listIterator.next();
            listIterator.set(obj);
        }
    }

    public int characteristics() {
        return 16448;
    }

    public long estimateSize() {
        return 0L;
    }

    public void forEachRemaining(Object obj) {
        obj.getClass();
    }

    public boolean tryAdvance(Object obj) {
        obj.getClass();
        return false;
    }

    public Q trySplit() {
        return null;
    }
}
