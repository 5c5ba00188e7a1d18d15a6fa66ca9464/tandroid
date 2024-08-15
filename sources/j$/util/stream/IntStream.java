package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.stream.Stream;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Spliterator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.stream.DoubleStream;
/* loaded from: classes2.dex */
public interface IntStream extends BaseStream<Integer, IntStream> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class VivifiedWrapper implements IntStream {
        public final /* synthetic */ java.util.stream.IntStream a;

        private /* synthetic */ VivifiedWrapper(java.util.stream.IntStream intStream) {
            this.a = intStream;
        }

        public static /* synthetic */ IntStream convert(java.util.stream.IntStream intStream) {
            if (intStream == null) {
                return null;
            }
            return intStream instanceof Wrapper ? IntStream.this : new VivifiedWrapper(intStream);
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ Object A(Supplier supplier, j$.util.function.C0 c0, BiConsumer biConsumer) {
            return this.a.collect(j$.util.function.L0.a(supplier), j$.util.function.B0.a(c0), j$.util.function.a.a(biConsumer));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ boolean D(j$.util.function.Q q) {
            return this.a.anyMatch(j$.util.function.P.a(q));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ void O(j$.util.function.K k) {
            this.a.forEachOrdered(j$.util.function.J.a(k));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ Stream P(j$.util.function.N n) {
            return Stream.VivifiedWrapper.convert(this.a.mapToObj(j$.util.function.M.a(n)));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream S(j$.util.function.N n) {
            return convert(this.a.flatMap(j$.util.function.M.a(n)));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ void V(j$.util.function.K k) {
            this.a.forEach(j$.util.function.J.a(k));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ F W(j$.util.function.U u) {
            return D.i0(this.a.mapToDouble(j$.util.function.T.a(u)));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream Z(j$.util.function.Q q) {
            return convert(this.a.filter(j$.util.function.P.a(q)));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ j$.util.m a0(j$.util.function.G g) {
            return j$.util.k.c(this.a.reduce(j$.util.function.F.a(g)));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ F asDoubleStream() {
            return D.i0(this.a.asDoubleStream());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ LongStream asLongStream() {
            return k0.i0(this.a.asLongStream());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ j$.util.l average() {
            return j$.util.k.b(this.a.average());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream b0(j$.util.function.K k) {
            return convert(this.a.peek(j$.util.function.J.a(k)));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ Stream boxed() {
            return Stream.VivifiedWrapper.convert(this.a.boxed());
        }

        @Override // j$.util.stream.BaseStream, java.lang.AutoCloseable
        public final /* synthetic */ void close() {
            this.a.close();
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ long count() {
            return this.a.count();
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ LongStream d(j$.util.function.X x) {
            return k0.i0(this.a.mapToLong(j$.util.function.W.a(x)));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream distinct() {
            return convert(this.a.distinct());
        }

        public final /* synthetic */ boolean equals(Object obj) {
            if (obj instanceof VivifiedWrapper) {
                obj = ((VivifiedWrapper) obj).a;
            }
            return this.a.equals(obj);
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ j$.util.m findAny() {
            return j$.util.k.c(this.a.findAny());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ j$.util.m findFirst() {
            return j$.util.k.c(this.a.findFirst());
        }

        public final /* synthetic */ int hashCode() {
            return this.a.hashCode();
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ boolean isParallel() {
            return this.a.isParallel();
        }

        @Override // j$.util.stream.IntStream, j$.util.stream.BaseStream, j$.util.stream.F
        public final /* synthetic */ j$.util.v iterator() {
            return j$.util.t.a(this.a.iterator());
        }

        @Override // j$.util.stream.BaseStream, j$.util.stream.F
        public final /* synthetic */ Iterator iterator() {
            return this.a.iterator();
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream l(j$.util.function.a0 a0Var) {
            return convert(this.a.map(j$.util.function.Z.a(a0Var)));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream limit(long j) {
            return convert(this.a.limit(j));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ j$.util.m max() {
            return j$.util.k.c(this.a.max());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ j$.util.m min() {
            return j$.util.k.c(this.a.min());
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ BaseStream onClose(Runnable runnable) {
            return g.i0(this.a.onClose(runnable));
        }

        @Override // j$.util.stream.BaseStream, j$.util.stream.F
        public final /* synthetic */ BaseStream parallel() {
            return g.i0(this.a.parallel());
        }

        @Override // j$.util.stream.IntStream, j$.util.stream.BaseStream, j$.util.stream.F
        public final /* synthetic */ IntStream parallel() {
            return convert(this.a.parallel());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ int s(int i, j$.util.function.G g) {
            return this.a.reduce(i, j$.util.function.F.a(g));
        }

        @Override // j$.util.stream.BaseStream, j$.util.stream.F
        public final /* synthetic */ BaseStream sequential() {
            return g.i0(this.a.sequential());
        }

        @Override // j$.util.stream.IntStream, j$.util.stream.BaseStream, j$.util.stream.F
        public final /* synthetic */ IntStream sequential() {
            return convert(this.a.sequential());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream skip(long j) {
            return convert(this.a.skip(j));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream sorted() {
            return convert(this.a.sorted());
        }

        @Override // j$.util.stream.IntStream, j$.util.stream.BaseStream, j$.util.stream.F
        public final /* synthetic */ j$.util.H spliterator() {
            return j$.util.F.f(this.a.spliterator());
        }

        @Override // j$.util.stream.BaseStream, j$.util.stream.F
        public final /* synthetic */ j$.util.Q spliterator() {
            return j$.util.O.f(this.a.spliterator());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ int sum() {
            return this.a.sum();
        }

        @Override // j$.util.stream.IntStream
        public final j$.util.i summaryStatistics() {
            this.a.summaryStatistics();
            throw new Error("Java 8+ API desugaring (library desugaring) cannot convert from java.util.IntSummaryStatistics");
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ boolean t(j$.util.function.Q q) {
            return this.a.allMatch(j$.util.function.P.a(q));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ int[] toArray() {
            return this.a.toArray();
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ boolean u(j$.util.function.Q q) {
            return this.a.noneMatch(j$.util.function.P.a(q));
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ BaseStream unordered() {
            return g.i0(this.a.unordered());
        }
    }

    /* loaded from: classes2.dex */
    public final /* synthetic */ class Wrapper implements java.util.stream.IntStream {
        private /* synthetic */ Wrapper() {
            IntStream.this = r1;
        }

        public static /* synthetic */ java.util.stream.IntStream convert(IntStream intStream) {
            if (intStream == null) {
                return null;
            }
            return intStream instanceof VivifiedWrapper ? ((VivifiedWrapper) intStream).a : new Wrapper();
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ boolean allMatch(IntPredicate intPredicate) {
            return IntStream.this.t(j$.util.function.O.b(intPredicate));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ boolean anyMatch(IntPredicate intPredicate) {
            return IntStream.this.D(j$.util.function.O.b(intPredicate));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ DoubleStream asDoubleStream() {
            return E.i0(IntStream.this.asDoubleStream());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.LongStream asLongStream() {
            return l0.i0(IntStream.this.asLongStream());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ OptionalDouble average() {
            return j$.util.k.f(IntStream.this.average());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.Stream boxed() {
            return Q2.i0(IntStream.this.boxed());
        }

        @Override // java.util.stream.BaseStream, java.lang.AutoCloseable
        public final /* synthetic */ void close() {
            IntStream.this.close();
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ Object collect(java.util.function.Supplier supplier, ObjIntConsumer objIntConsumer, java.util.function.BiConsumer biConsumer) {
            return IntStream.this.A(j$.util.function.K0.a(supplier), j$.util.function.A0.a(objIntConsumer), BiConsumer.VivifiedWrapper.convert(biConsumer));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ long count() {
            return IntStream.this.count();
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.IntStream distinct() {
            return convert(IntStream.this.distinct());
        }

        public final /* synthetic */ boolean equals(Object obj) {
            IntStream intStream = IntStream.this;
            if (obj instanceof Wrapper) {
                obj = IntStream.this;
            }
            return intStream.equals(obj);
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.IntStream filter(IntPredicate intPredicate) {
            return convert(IntStream.this.Z(j$.util.function.O.b(intPredicate)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ OptionalInt findAny() {
            return j$.util.k.g(IntStream.this.findAny());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ OptionalInt findFirst() {
            return j$.util.k.g(IntStream.this.findFirst());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.IntStream flatMap(IntFunction intFunction) {
            return convert(IntStream.this.S(j$.util.function.L.a(intFunction)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ void forEach(IntConsumer intConsumer) {
            IntStream.this.V(j$.util.function.I.a(intConsumer));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ void forEachOrdered(IntConsumer intConsumer) {
            IntStream.this.O(j$.util.function.I.a(intConsumer));
        }

        public final /* synthetic */ int hashCode() {
            return IntStream.this.hashCode();
        }

        @Override // java.util.stream.BaseStream
        public final /* synthetic */ boolean isParallel() {
            return IntStream.this.isParallel();
        }

        @Override // java.util.stream.IntStream, java.util.stream.BaseStream
        public final /* synthetic */ Iterator<Integer> iterator() {
            return IntStream.this.iterator();
        }

        @Override // java.util.stream.IntStream, java.util.stream.BaseStream
        public final /* synthetic */ Iterator<Integer> iterator() {
            return j$.util.u.a(IntStream.this.iterator());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.IntStream limit(long j) {
            return convert(IntStream.this.limit(j));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.IntStream map(IntUnaryOperator intUnaryOperator) {
            return convert(IntStream.this.l(j$.util.function.Y.d(intUnaryOperator)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ DoubleStream mapToDouble(IntToDoubleFunction intToDoubleFunction) {
            return E.i0(IntStream.this.W(j$.util.function.S.b(intToDoubleFunction)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.LongStream mapToLong(IntToLongFunction intToLongFunction) {
            return l0.i0(IntStream.this.d(j$.util.function.V.a(intToLongFunction)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.Stream mapToObj(IntFunction intFunction) {
            return Q2.i0(IntStream.this.P(j$.util.function.L.a(intFunction)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ OptionalInt max() {
            return j$.util.k.g(IntStream.this.max());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ OptionalInt min() {
            return j$.util.k.g(IntStream.this.min());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ boolean noneMatch(IntPredicate intPredicate) {
            return IntStream.this.u(j$.util.function.O.b(intPredicate));
        }

        /* JADX WARN: Type inference failed for: r2v2, types: [java.util.stream.IntStream, java.util.stream.BaseStream] */
        @Override // java.util.stream.BaseStream
        public final /* synthetic */ java.util.stream.IntStream onClose(Runnable runnable) {
            return h.i0(IntStream.this.onClose(runnable));
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.IntStream, java.util.stream.BaseStream] */
        @Override // java.util.stream.IntStream, java.util.stream.BaseStream
        public final /* synthetic */ java.util.stream.IntStream parallel() {
            return h.i0(IntStream.this.parallel());
        }

        @Override // java.util.stream.IntStream, java.util.stream.BaseStream
        public final /* synthetic */ java.util.stream.IntStream parallel() {
            return convert(IntStream.this.parallel());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.IntStream peek(IntConsumer intConsumer) {
            return convert(IntStream.this.b0(j$.util.function.I.a(intConsumer)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ int reduce(int i, IntBinaryOperator intBinaryOperator) {
            return IntStream.this.s(i, j$.util.function.E.a(intBinaryOperator));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ OptionalInt reduce(IntBinaryOperator intBinaryOperator) {
            return j$.util.k.g(IntStream.this.a0(j$.util.function.E.a(intBinaryOperator)));
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.IntStream, java.util.stream.BaseStream] */
        @Override // java.util.stream.IntStream, java.util.stream.BaseStream
        public final /* synthetic */ java.util.stream.IntStream sequential() {
            return h.i0(IntStream.this.sequential());
        }

        @Override // java.util.stream.IntStream, java.util.stream.BaseStream
        public final /* synthetic */ java.util.stream.IntStream sequential() {
            return convert(IntStream.this.sequential());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.IntStream skip(long j) {
            return convert(IntStream.this.skip(j));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.IntStream sorted() {
            return convert(IntStream.this.sorted());
        }

        @Override // java.util.stream.IntStream, java.util.stream.BaseStream
        public final /* synthetic */ Spliterator<Integer> spliterator() {
            return j$.util.G.a(IntStream.this.spliterator());
        }

        @Override // java.util.stream.IntStream, java.util.stream.BaseStream
        public final /* synthetic */ Spliterator<Integer> spliterator() {
            return j$.util.P.a(IntStream.this.spliterator());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ int sum() {
            return IntStream.this.sum();
        }

        @Override // java.util.stream.IntStream
        public final IntSummaryStatistics summaryStatistics() {
            IntStream.this.summaryStatistics();
            throw new Error("Java 8+ API desugaring (library desugaring) cannot convert to java.util.IntSummaryStatistics");
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ int[] toArray() {
            return IntStream.this.toArray();
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.IntStream, java.util.stream.BaseStream] */
        @Override // java.util.stream.BaseStream
        public final /* synthetic */ java.util.stream.IntStream unordered() {
            return h.i0(IntStream.this.unordered());
        }
    }

    Object A(Supplier supplier, j$.util.function.C0 c0, BiConsumer biConsumer);

    boolean D(j$.util.function.Q q);

    void O(j$.util.function.K k);

    Stream P(j$.util.function.N n);

    IntStream S(j$.util.function.N n);

    void V(j$.util.function.K k);

    F W(j$.util.function.U u);

    IntStream Z(j$.util.function.Q q);

    j$.util.m a0(j$.util.function.G g);

    F asDoubleStream();

    LongStream asLongStream();

    j$.util.l average();

    IntStream b0(j$.util.function.K k);

    Stream boxed();

    long count();

    LongStream d(j$.util.function.X x);

    IntStream distinct();

    j$.util.m findAny();

    j$.util.m findFirst();

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    j$.util.v iterator();

    IntStream l(j$.util.function.a0 a0Var);

    IntStream limit(long j);

    j$.util.m max();

    j$.util.m min();

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    IntStream parallel();

    int s(int i, j$.util.function.G g);

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    IntStream sequential();

    IntStream skip(long j);

    IntStream sorted();

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    j$.util.H spliterator();

    int sum();

    j$.util.i summaryStatistics();

    boolean t(j$.util.function.Q q);

    int[] toArray();

    boolean u(j$.util.function.Q q);
}
