package j$.wrappers;

import j$.util.Optional;
import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Consumer;
import j$.util.function.Function;
import j$.util.function.Predicate;
import j$.util.function.Supplier;
import j$.util.function.ToDoubleFunction;
import j$.util.function.ToIntFunction;
import j$.util.function.ToLongFunction;
import j$.util.stream.Collector;
import j$.util.stream.IntStream;
import j$.util.stream.LongStream;
import j$.util.stream.Stream;
import java.util.Comparator;
import java.util.Iterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class $r8$wrapper$java$util$stream$Stream$-V-WRP implements Stream {
    final /* synthetic */ java.util.stream.Stream a;

    private /* synthetic */ $r8$wrapper$java$util$stream$Stream$-V-WRP(java.util.stream.Stream stream) {
        this.a = stream;
    }

    public static /* synthetic */ Stream convert(java.util.stream.Stream stream) {
        if (stream == null) {
            return null;
        }
        return stream instanceof O0 ? ((O0) stream).a : new $r8$wrapper$java$util$stream$Stream$-V-WRP(stream);
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Object A(Object obj, BiFunction biFunction, j$.util.function.b bVar) {
        return this.a.reduce(obj, t.a(biFunction), v.a(bVar));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ j$.util.stream.V D(Function function) {
        return K0.i0(this.a.flatMapToDouble(M.a(function)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream S(Consumer consumer) {
        return convert(this.a.peek($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ boolean T(Predicate predicate) {
        return this.a.allMatch(x0.a(predicate));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ LongStream U(Function function) {
        return M0.i0(this.a.flatMapToLong(M.a(function)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ boolean Z(Predicate predicate) {
        return this.a.noneMatch(x0.a(predicate));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ boolean a(Predicate predicate) {
        return this.a.anyMatch(x0.a(predicate));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ IntStream c(Function function) {
        return $r8$wrapper$java$util$stream$IntStream$-V-WRP.convert(this.a.flatMapToInt(M.a(function)));
    }

    @Override // j$.util.stream.Stream, j$.util.stream.g, java.lang.AutoCloseable
    public /* synthetic */ void close() {
        this.a.close();
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Object collect(Collector collector) {
        return this.a.collect(J0.a(collector));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream distinct() {
        return convert(this.a.distinct());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ void e(Consumer consumer) {
        this.a.forEachOrdered($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ j$.util.stream.V e0(ToDoubleFunction toDoubleFunction) {
        return K0.i0(this.a.mapToDouble(B0.a(toDoubleFunction)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream filter(Predicate predicate) {
        return convert(this.a.filter(x0.a(predicate)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Optional findAny() {
        return j$.util.a.m(this.a.findAny());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Optional findFirst() {
        return j$.util.a.m(this.a.findFirst());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ void forEach(Consumer consumer) {
        this.a.forEach($r8$wrapper$java$util$function$Consumer$-WRP.convert(consumer));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Object h0(Object obj, j$.util.function.b bVar) {
        return this.a.reduce(obj, v.a(bVar));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Object i(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2) {
        return this.a.collect(z0.a(supplier), r.a(biConsumer), r.a(biConsumer2));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.g
    public /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Object[] l(j$.util.function.m mVar) {
        return this.a.toArray(T.a(mVar));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream limit(long j) {
        return convert(this.a.limit(j));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ IntStream m(ToIntFunction toIntFunction) {
        return $r8$wrapper$java$util$stream$IntStream$-V-WRP.convert(this.a.mapToInt(D0.a(toIntFunction)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream map(Function function) {
        return convert(this.a.map(M.a(function)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ LongStream mapToLong(ToLongFunction toLongFunction) {
        return M0.i0(this.a.mapToLong(F0.a(toLongFunction)));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Optional max(Comparator comparator) {
        return j$.util.a.m(this.a.max(comparator));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Optional min(Comparator comparator) {
        return j$.util.a.m(this.a.min(comparator));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream n(Function function) {
        return convert(this.a.flatMap(M.a(function)));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g onClose(Runnable runnable) {
        return G0.i0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g parallel() {
        return G0.i0(this.a.parallel());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Optional s(j$.util.function.b bVar) {
        return j$.util.a.m(this.a.reduce(v.a(bVar)));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g sequential() {
        return G0.i0(this.a.sequential());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream skip(long j) {
        return convert(this.a.skip(j));
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream sorted() {
        return convert(this.a.sorted());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Stream sorted(Comparator comparator) {
        return convert(this.a.sorted(comparator));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.s spliterator() {
        return g.a(this.a.spliterator());
    }

    @Override // j$.util.stream.Stream
    public /* synthetic */ Object[] toArray() {
        return this.a.toArray();
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g unordered() {
        return G0.i0(this.a.unordered());
    }
}
