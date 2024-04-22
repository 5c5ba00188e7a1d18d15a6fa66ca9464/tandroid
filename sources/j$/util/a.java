package j$.util;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.s;
import j$.util.stream.Stream;
import j$.util.stream.o1;
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

    public static void b(s.a aVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.f) {
            aVar.e((j$.util.function.f) consumer);
        } else if (K.a) {
            K.a(aVar.getClass(), "{0} calling Spliterator.OfDouble.forEachRemaining((DoubleConsumer) action::accept)");
            throw null;
        } else {
            Objects.requireNonNull(consumer);
            aVar.e(new m(consumer));
        }
    }

    public static void c(s.b bVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.l) {
            bVar.c((j$.util.function.l) consumer);
        } else if (K.a) {
            K.a(bVar.getClass(), "{0} calling Spliterator.OfInt.forEachRemaining((IntConsumer) action::accept)");
            throw null;
        } else {
            Objects.requireNonNull(consumer);
            bVar.c(new o(consumer));
        }
    }

    public static void d(s.c cVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.q) {
            cVar.d((j$.util.function.q) consumer);
        } else if (K.a) {
            K.a(cVar.getClass(), "{0} calling Spliterator.OfLong.forEachRemaining((LongConsumer) action::accept)");
            throw null;
        } else {
            Objects.requireNonNull(consumer);
            cVar.d(new q(consumer));
        }
    }

    public static long e(s sVar) {
        if ((sVar.characteristics() & 64) == 0) {
            return -1L;
        }
        return sVar.estimateSize();
    }

    public static boolean f(s sVar, int i) {
        return (sVar.characteristics() & i) == i;
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

    public static boolean j(s.a aVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.f) {
            return aVar.k((j$.util.function.f) consumer);
        }
        if (K.a) {
            K.a(aVar.getClass(), "{0} calling Spliterator.OfDouble.tryAdvance((DoubleConsumer) action::accept)");
            throw null;
        }
        Objects.requireNonNull(consumer);
        return aVar.k(new m(consumer));
    }

    public static boolean k(s.b bVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.l) {
            return bVar.g((j$.util.function.l) consumer);
        }
        if (K.a) {
            K.a(bVar.getClass(), "{0} calling Spliterator.OfInt.tryAdvance((IntConsumer) action::accept)");
            throw null;
        }
        Objects.requireNonNull(consumer);
        return bVar.g(new o(consumer));
    }

    public static boolean l(s.c cVar, Consumer consumer) {
        if (consumer instanceof j$.util.function.q) {
            return cVar.i((j$.util.function.q) consumer);
        }
        if (K.a) {
            K.a(cVar.getClass(), "{0} calling Spliterator.OfLong.tryAdvance((LongConsumer) action::accept)");
            throw null;
        }
        Objects.requireNonNull(consumer);
        return cVar.i(new q(consumer));
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
}
