package j$.util;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.stream.Stream;
import j$.util.stream.t0;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/* loaded from: classes2.dex */
public abstract /* synthetic */ class a {
    a() {
    }

    public static boolean A(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static void B(Object obj, String str) {
        if (obj == null) {
            throw new NullPointerException(str);
        }
    }

    public static void C(List list, Comparator comparator) {
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

    public static void b(E e, Consumer consumer) {
        if (consumer instanceof j$.util.function.n) {
            e.e((j$.util.function.n) consumer);
        } else {
            if (h0.a) {
                h0.a(e.getClass(), "{0} calling Spliterator.OfDouble.forEachRemaining((DoubleConsumer) action::accept)");
                throw null;
            }
            consumer.getClass();
            e.e(new o(consumer));
        }
    }

    public static void f(H h, Consumer consumer) {
        if (consumer instanceof j$.util.function.F) {
            h.e((j$.util.function.F) consumer);
        } else {
            if (h0.a) {
                h0.a(h.getClass(), "{0} calling Spliterator.OfInt.forEachRemaining((IntConsumer) action::accept)");
                throw null;
            }
            consumer.getClass();
            h.e(new s(consumer));
        }
    }

    public static void h(K k, Consumer consumer) {
        if (consumer instanceof j$.util.function.W) {
            k.e((j$.util.function.W) consumer);
        } else {
            if (h0.a) {
                h0.a(k.getClass(), "{0} calling Spliterator.OfLong.forEachRemaining((LongConsumer) action::accept)");
                throw null;
            }
            consumer.getClass();
            k.e(new w(consumer));
        }
    }

    public static long j(Q q) {
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
        Iterator it = collection.iterator();
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
        return t0.e0(Collection$-EL.b(collection), false);
    }

    public static boolean n(E e, Consumer consumer) {
        if (consumer instanceof j$.util.function.n) {
            return e.p((j$.util.function.n) consumer);
        }
        if (h0.a) {
            h0.a(e.getClass(), "{0} calling Spliterator.OfDouble.tryAdvance((DoubleConsumer) action::accept)");
            throw null;
        }
        consumer.getClass();
        return e.p(new o(consumer));
    }

    public static boolean o(H h, Consumer consumer) {
        if (consumer instanceof j$.util.function.F) {
            return h.p((j$.util.function.F) consumer);
        }
        if (h0.a) {
            h0.a(h.getClass(), "{0} calling Spliterator.OfInt.tryAdvance((IntConsumer) action::accept)");
            throw null;
        }
        consumer.getClass();
        return h.p(new s(consumer));
    }

    public static boolean q(K k, Consumer consumer) {
        if (consumer instanceof j$.util.function.W) {
            return k.p((j$.util.function.W) consumer);
        }
        if (h0.a) {
            h0.a(k.getClass(), "{0} calling Spliterator.OfLong.tryAdvance((LongConsumer) action::accept)");
            throw null;
        }
        consumer.getClass();
        return k.p(new w(consumer));
    }

    public static Optional r(java.util.Optional optional) {
        if (optional == null) {
            return null;
        }
        return optional.isPresent() ? Optional.of(optional.get()) : Optional.empty();
    }

    public static l t(OptionalDouble optionalDouble) {
        if (optionalDouble == null) {
            return null;
        }
        return optionalDouble.isPresent() ? l.d(optionalDouble.getAsDouble()) : l.a();
    }

    public static m u(OptionalInt optionalInt) {
        if (optionalInt == null) {
            return null;
        }
        return optionalInt.isPresent() ? m.d(optionalInt.getAsInt()) : m.a();
    }

    public static n v(OptionalLong optionalLong) {
        if (optionalLong == null) {
            return null;
        }
        return optionalLong.isPresent() ? n.d(optionalLong.getAsLong()) : n.a();
    }

    public static java.util.Optional w(Optional optional) {
        if (optional == null) {
            return null;
        }
        return optional.isPresent() ? java.util.Optional.of(optional.get()) : java.util.Optional.empty();
    }

    public static OptionalDouble x(l lVar) {
        if (lVar == null) {
            return null;
        }
        return lVar.c() ? OptionalDouble.of(lVar.b()) : OptionalDouble.empty();
    }

    public static OptionalInt y(m mVar) {
        if (mVar == null) {
            return null;
        }
        return mVar.c() ? OptionalInt.of(mVar.b()) : OptionalInt.empty();
    }

    public static OptionalLong z(n nVar) {
        if (nVar == null) {
            return null;
        }
        return nVar.c() ? OptionalLong.of(nVar.b()) : OptionalLong.empty();
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
