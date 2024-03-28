package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Function;
import j$.util.function.Supplier;
import j$.util.stream.Collector;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes2.dex */
public final class Collectors {
    static final Set a;

    /* loaded from: classes2.dex */
    static class a implements Collector {
        private final Supplier a;
        private final BiConsumer b;
        private final j$.util.function.b c;
        private final Function d;
        private final Set e;

        a(Supplier supplier, BiConsumer biConsumer, j$.util.function.b bVar, Set set) {
            Set set2 = Collectors.a;
            j jVar = new Function() { // from class: j$.util.stream.j
                @Override // j$.util.function.Function
                public /* synthetic */ Function andThen(Function function) {
                    return Function.-CC.$default$andThen(this, function);
                }

                @Override // j$.util.function.Function
                public final Object apply(Object obj) {
                    Set set3 = Collectors.a;
                    return obj;
                }

                @Override // j$.util.function.Function
                public /* synthetic */ Function compose(Function function) {
                    return Function.-CC.$default$compose(this, function);
                }
            };
            this.a = supplier;
            this.b = biConsumer;
            this.c = bVar;
            this.d = jVar;
            this.e = set;
        }

        @Override // j$.util.stream.Collector
        public BiConsumer accumulator() {
            return this.b;
        }

        @Override // j$.util.stream.Collector
        public Set characteristics() {
            return this.e;
        }

        @Override // j$.util.stream.Collector
        public j$.util.function.b combiner() {
            return this.c;
        }

        @Override // j$.util.stream.Collector
        public Function finisher() {
            return this.d;
        }

        @Override // j$.util.stream.Collector
        public Supplier supplier() {
            return this.a;
        }
    }

    static {
        Collector.a aVar = Collector.a.CONCURRENT;
        Collector.a aVar2 = Collector.a.UNORDERED;
        Collector.a aVar3 = Collector.a.IDENTITY_FINISH;
        Collections.unmodifiableSet(EnumSet.of(aVar, aVar2, aVar3));
        Collections.unmodifiableSet(EnumSet.of(aVar, aVar2));
        a = Collections.unmodifiableSet(EnumSet.of(aVar3));
        Collections.unmodifiableSet(EnumSet.of(aVar2, aVar3));
        Collections.emptySet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static double a(double[] dArr) {
        double d = dArr[0] + dArr[1];
        double d2 = dArr[dArr.length - 1];
        return (Double.isNaN(d) && Double.isInfinite(d2)) ? d2 : d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static double[] b(double[] dArr, double d) {
        double d2 = d - dArr[1];
        double d3 = dArr[0];
        double d4 = d3 + d2;
        dArr[1] = (d4 - d3) - d2;
        dArr[0] = d4;
        return dArr;
    }

    public static <T, C extends Collection<T>> Collector<T, ?, C> toCollection(Supplier<C> supplier) {
        return new a(supplier, new BiConsumer() { // from class: j$.util.stream.h
            @Override // j$.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((Collection) obj).add(obj2);
            }

            @Override // j$.util.function.BiConsumer
            public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            }
        }, new j$.util.function.b() { // from class: j$.util.stream.i
            @Override // j$.util.function.BiFunction
            public BiFunction andThen(Function function) {
                Objects.requireNonNull(function);
                return new j$.util.concurrent.a(this, function);
            }

            @Override // j$.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                Collection collection = (Collection) obj;
                Set set = Collectors.a;
                collection.addAll((Collection) obj2);
                return collection;
            }
        }, a);
    }
}
