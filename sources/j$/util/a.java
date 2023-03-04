package j$.util;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.stream.Stream;
import j$.util.stream.o1;
import j$.util.u;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
/* loaded from: classes2.dex */
public abstract /* synthetic */ class a {
    public static void a(Collection collection, Consumer consumer) {
        Objects.requireNonNull(consumer);
        for (Object obj : collection) {
            consumer.accept(obj);
        }
    }

    public static void b(t tVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.f) {
            tVar.e((j$.util.function.f) consumer);
        } else if (N.a) {
            N.a(tVar.getClass(), "{0} calling Spliterator.OfDouble.forEachRemaining((DoubleConsumer) action::accept)");
            throw null;
        } else {
            Objects.requireNonNull(consumer);
            tVar.e(new m(consumer));
        }
    }

    public static void c(u.a aVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.l) {
            aVar.c((j$.util.function.l) consumer);
        } else if (N.a) {
            N.a(aVar.getClass(), "{0} calling Spliterator.OfInt.forEachRemaining((IntConsumer) action::accept)");
            throw null;
        } else {
            Objects.requireNonNull(consumer);
            aVar.c(new o(consumer));
        }
    }

    public static void d(v vVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.q) {
            vVar.d((j$.util.function.q) consumer);
        } else if (N.a) {
            N.a(vVar.getClass(), "{0} calling Spliterator.OfLong.forEachRemaining((LongConsumer) action::accept)");
            throw null;
        } else {
            Objects.requireNonNull(consumer);
            vVar.d(new q(consumer));
        }
    }

    public static long e(u uVar) {
        if ((uVar.characteristics() & 64) == 0) {
            return -1L;
        }
        return uVar.estimateSize();
    }

    public static boolean f(u uVar, int i) {
        return (uVar.characteristics() & i) == i;
    }

    public static Stream g(Collection collection) {
        return o1.y(Collection$-EL.b(collection), true);
    }

    public static boolean h(Collection collection, Predicate predicate) {
        if (DesugarCollections.a.isInstance(collection)) {
            return DesugarCollections.c(collection, predicate);
        }
        Objects.requireNonNull(predicate);
        boolean z = false;
        java.util.Iterator it = collection.iterator();
        while (it.hasNext()) {
            if (predicate.test(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public static Stream i(Collection collection) {
        return o1.y(Collection$-EL.b(collection), false);
    }

    public static boolean j(t tVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.f) {
            return tVar.k((j$.util.function.f) consumer);
        }
        if (N.a) {
            N.a(tVar.getClass(), "{0} calling Spliterator.OfDouble.tryAdvance((DoubleConsumer) action::accept)");
            throw null;
        }
        Objects.requireNonNull(consumer);
        return tVar.k(new m(consumer));
    }

    public static boolean k(u.a aVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.l) {
            return aVar.g((j$.util.function.l) consumer);
        }
        if (N.a) {
            N.a(aVar.getClass(), "{0} calling Spliterator.OfInt.tryAdvance((IntConsumer) action::accept)");
            throw null;
        }
        Objects.requireNonNull(consumer);
        return aVar.g(new o(consumer));
    }

    public static boolean l(v vVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.q) {
            return vVar.i((j$.util.function.q) consumer);
        }
        if (N.a) {
            N.a(vVar.getClass(), "{0} calling Spliterator.OfLong.tryAdvance((LongConsumer) action::accept)");
            throw null;
        }
        Objects.requireNonNull(consumer);
        return vVar.i(new q(consumer));
    }

    public static Optional m(java.util.Optional optional) {
        if (optional == null) {
            return null;
        }
        return optional.isPresent() ? Optional.of(optional.get()) : Optional.empty();
    }

    public static j n(OptionalDouble optionalDouble) {
        if (optionalDouble == null) {
            return null;
        }
        return optionalDouble.isPresent() ? j.d(optionalDouble.getAsDouble()) : j.a();
    }

    public static k o(OptionalInt optionalInt) {
        if (optionalInt == null) {
            return null;
        }
        return optionalInt.isPresent() ? k.d(optionalInt.getAsInt()) : k.a();
    }

    public static l p(OptionalLong optionalLong) {
        if (optionalLong == null) {
            return null;
        }
        return optionalLong.isPresent() ? l.d(optionalLong.getAsLong()) : l.a();
    }

    public static java.util.Optional q(Optional optional) {
        if (optional == null) {
            return null;
        }
        return optional.isPresent() ? java.util.Optional.of(optional.get()) : java.util.Optional.empty();
    }

    public static OptionalDouble r(j jVar) {
        if (jVar == null) {
            return null;
        }
        return jVar.c() ? OptionalDouble.of(jVar.b()) : OptionalDouble.empty();
    }

    public static OptionalInt s(k kVar) {
        if (kVar == null) {
            return null;
        }
        return kVar.c() ? OptionalInt.of(kVar.b()) : OptionalInt.empty();
    }

    public static OptionalLong t(l lVar) {
        if (lVar == null) {
            return null;
        }
        return lVar.c() ? OptionalLong.of(lVar.b()) : OptionalLong.empty();
    }

    public static boolean u(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static void v(List list, Comparator comparator) {
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

    public static Comparator w(Comparator comparator, Comparator comparator2) {
        if (comparator instanceof e) {
            return ((f) ((e) comparator)).thenComparing(comparator2);
        }
        Objects.requireNonNull(comparator2);
        return new c(comparator, comparator2);
    }
}
