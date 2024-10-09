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
        public final /* synthetic */ void C(j$.util.function.F f) {
            this.a.forEachOrdered(j$.util.function.E.a(f));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ Stream D(j$.util.function.I i) {
            return Stream.VivifiedWrapper.convert(this.a.mapToObj(j$.util.function.H.a(i)));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream H(j$.util.function.O o) {
            return convert(this.a.map(o == null ? null : o.a));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ int J(int i, j$.util.function.B b) {
            return this.a.reduce(i, j$.util.function.A.a(b));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream K(j$.util.function.I i) {
            return convert(this.a.flatMap(j$.util.function.H.a(i)));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ void L(j$.util.function.F f) {
            this.a.forEach(j$.util.function.E.a(f));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream Q(j$.util.function.J j) {
            return convert(this.a.filter(j == null ? null : j.a));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ boolean T(j$.util.function.J j) {
            return this.a.allMatch(j == null ? null : j.a);
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ j$.util.m W(j$.util.function.B b) {
            return j$.util.a.u(this.a.reduce(j$.util.function.A.a(b)));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream X(j$.util.function.F f) {
            return convert(this.a.peek(j$.util.function.E.a(f)));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ boolean a0(j$.util.function.J j) {
            return this.a.anyMatch(j == null ? null : j.a);
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ D asDoubleStream() {
            return B.i0(this.a.asDoubleStream());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ LongStream asLongStream() {
            return i0.i0(this.a.asLongStream());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ j$.util.l average() {
            return j$.util.a.t(this.a.average());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ boolean b0(j$.util.function.J j) {
            return this.a.noneMatch(j == null ? null : j.a);
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
        public final /* synthetic */ IntStream distinct() {
            return convert(this.a.distinct());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ D e(j$.util.function.K k) {
            return B.i0(this.a.mapToDouble(k == null ? null : k.a));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ Object e0(Supplier supplier, j$.util.function.l0 l0Var, BiConsumer biConsumer) {
            return this.a.collect(j$.util.function.u0.a(supplier), j$.util.function.k0.a(l0Var), j$.util.function.a.a(biConsumer));
        }

        public final /* synthetic */ boolean equals(Object obj) {
            java.util.stream.IntStream intStream = this.a;
            if (obj instanceof VivifiedWrapper) {
                obj = ((VivifiedWrapper) obj).a;
            }
            return intStream.equals(obj);
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ j$.util.m findAny() {
            return j$.util.a.u(this.a.findAny());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ j$.util.m findFirst() {
            return j$.util.a.u(this.a.findFirst());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ LongStream g(j$.util.function.N n) {
            return i0.i0(this.a.mapToLong(j$.util.function.M.a(n)));
        }

        public final /* synthetic */ int hashCode() {
            return this.a.hashCode();
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ boolean isParallel() {
            return this.a.isParallel();
        }

        /* JADX WARN: Type inference failed for: r0v1, types: [java.util.PrimitiveIterator$OfInt] */
        @Override // j$.util.stream.IntStream, j$.util.stream.BaseStream, j$.util.stream.D
        public final /* synthetic */ j$.util.v iterator() {
            return j$.util.t.b(this.a.iterator());
        }

        @Override // j$.util.stream.BaseStream, j$.util.stream.D
        public final /* synthetic */ Iterator iterator() {
            return this.a.iterator();
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ IntStream limit(long j) {
            return convert(this.a.limit(j));
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ j$.util.m max() {
            return j$.util.a.u(this.a.max());
        }

        @Override // j$.util.stream.IntStream
        public final /* synthetic */ j$.util.m min() {
            return j$.util.a.u(this.a.min());
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ BaseStream onClose(Runnable runnable) {
            return f.i0(this.a.onClose(runnable));
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ BaseStream parallel() {
            return f.i0(this.a.parallel());
        }

        @Override // j$.util.stream.IntStream, j$.util.stream.BaseStream
        public final /* synthetic */ IntStream parallel() {
            return convert(this.a.parallel());
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ BaseStream sequential() {
            return f.i0(this.a.sequential());
        }

        @Override // j$.util.stream.IntStream, j$.util.stream.BaseStream
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

        /* JADX WARN: Type inference failed for: r0v1, types: [java.util.Spliterator$OfInt] */
        @Override // j$.util.stream.IntStream, j$.util.stream.BaseStream, j$.util.stream.D
        public final /* synthetic */ j$.util.H spliterator() {
            return j$.util.F.b(this.a.spliterator());
        }

        @Override // j$.util.stream.BaseStream, j$.util.stream.D
        public final /* synthetic */ j$.util.Q spliterator() {
            return j$.util.O.b(this.a.spliterator());
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
        public final /* synthetic */ int[] toArray() {
            return this.a.toArray();
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ BaseStream unordered() {
            return f.i0(this.a.unordered());
        }
    }

    /* loaded from: classes2.dex */
    public final /* synthetic */ class Wrapper implements java.util.stream.IntStream {
        private /* synthetic */ Wrapper() {
        }

        public static /* synthetic */ java.util.stream.IntStream convert(IntStream intStream) {
            if (intStream == null) {
                return null;
            }
            return intStream instanceof VivifiedWrapper ? ((VivifiedWrapper) intStream).a : new Wrapper();
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ boolean allMatch(IntPredicate intPredicate) {
            return IntStream.this.T(j$.util.function.J.a(intPredicate));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ boolean anyMatch(IntPredicate intPredicate) {
            return IntStream.this.a0(j$.util.function.J.a(intPredicate));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ DoubleStream asDoubleStream() {
            return C.i0(IntStream.this.asDoubleStream());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.LongStream asLongStream() {
            return j0.i0(IntStream.this.asLongStream());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ OptionalDouble average() {
            return j$.util.a.x(IntStream.this.average());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.Stream boxed() {
            return P2.i0(IntStream.this.boxed());
        }

        @Override // java.util.stream.BaseStream, java.lang.AutoCloseable
        public final /* synthetic */ void close() {
            IntStream.this.close();
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ Object collect(java.util.function.Supplier supplier, ObjIntConsumer objIntConsumer, java.util.function.BiConsumer biConsumer) {
            return IntStream.this.e0(j$.util.function.t0.a(supplier), j$.util.function.j0.a(objIntConsumer), BiConsumer.VivifiedWrapper.convert(biConsumer));
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
            return convert(IntStream.this.Q(j$.util.function.J.a(intPredicate)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ OptionalInt findAny() {
            return j$.util.a.y(IntStream.this.findAny());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ OptionalInt findFirst() {
            return j$.util.a.y(IntStream.this.findFirst());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.IntStream flatMap(IntFunction intFunction) {
            return convert(IntStream.this.K(j$.util.function.G.a(intFunction)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ void forEach(IntConsumer intConsumer) {
            IntStream.this.L(j$.util.function.D.a(intConsumer));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ void forEachOrdered(IntConsumer intConsumer) {
            IntStream.this.C(j$.util.function.D.a(intConsumer));
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
            return j$.util.u.b(IntStream.this.iterator());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.IntStream limit(long j) {
            return convert(IntStream.this.limit(j));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.IntStream map(IntUnaryOperator intUnaryOperator) {
            return convert(IntStream.this.H(j$.util.function.O.a(intUnaryOperator)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ DoubleStream mapToDouble(IntToDoubleFunction intToDoubleFunction) {
            return C.i0(IntStream.this.e(j$.util.function.K.a(intToDoubleFunction)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.LongStream mapToLong(IntToLongFunction intToLongFunction) {
            return j0.i0(IntStream.this.g(j$.util.function.L.a(intToLongFunction)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.Stream mapToObj(IntFunction intFunction) {
            return P2.i0(IntStream.this.D(j$.util.function.G.a(intFunction)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ OptionalInt max() {
            return j$.util.a.y(IntStream.this.max());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ OptionalInt min() {
            return j$.util.a.y(IntStream.this.min());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ boolean noneMatch(IntPredicate intPredicate) {
            return IntStream.this.b0(j$.util.function.J.a(intPredicate));
        }

        /* JADX WARN: Type inference failed for: r2v2, types: [java.util.stream.IntStream, java.util.stream.BaseStream] */
        @Override // java.util.stream.BaseStream
        public final /* synthetic */ java.util.stream.IntStream onClose(Runnable runnable) {
            return g.i0(IntStream.this.onClose(runnable));
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.IntStream, java.util.stream.BaseStream] */
        @Override // java.util.stream.IntStream, java.util.stream.BaseStream
        public final /* synthetic */ java.util.stream.IntStream parallel() {
            return g.i0(IntStream.this.parallel());
        }

        @Override // java.util.stream.IntStream, java.util.stream.BaseStream
        public final /* synthetic */ java.util.stream.IntStream parallel() {
            return convert(IntStream.this.parallel());
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ java.util.stream.IntStream peek(IntConsumer intConsumer) {
            return convert(IntStream.this.X(j$.util.function.D.a(intConsumer)));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ int reduce(int i, IntBinaryOperator intBinaryOperator) {
            return IntStream.this.J(i, j$.util.function.z.a(intBinaryOperator));
        }

        @Override // java.util.stream.IntStream
        public final /* synthetic */ OptionalInt reduce(IntBinaryOperator intBinaryOperator) {
            return j$.util.a.y(IntStream.this.W(j$.util.function.z.a(intBinaryOperator)));
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.IntStream, java.util.stream.BaseStream] */
        @Override // java.util.stream.IntStream, java.util.stream.BaseStream
        public final /* synthetic */ java.util.stream.IntStream sequential() {
            return g.i0(IntStream.this.sequential());
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
            return g.i0(IntStream.this.unordered());
        }
    }

    void C(j$.util.function.F f);

    Stream D(j$.util.function.I i);

    IntStream H(j$.util.function.O o);

    int J(int i, j$.util.function.B b);

    IntStream K(j$.util.function.I i);

    void L(j$.util.function.F f);

    IntStream Q(j$.util.function.J j);

    boolean T(j$.util.function.J j);

    j$.util.m W(j$.util.function.B b);

    IntStream X(j$.util.function.F f);

    boolean a0(j$.util.function.J j);

    D asDoubleStream();

    LongStream asLongStream();

    j$.util.l average();

    boolean b0(j$.util.function.J j);

    Stream boxed();

    long count();

    IntStream distinct();

    D e(j$.util.function.K k);

    Object e0(Supplier supplier, j$.util.function.l0 l0Var, BiConsumer biConsumer);

    j$.util.m findAny();

    j$.util.m findFirst();

    LongStream g(j$.util.function.N n);

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    j$.util.v iterator();

    IntStream limit(long j);

    j$.util.m max();

    j$.util.m min();

    @Override // j$.util.stream.BaseStream
    IntStream parallel();

    @Override // j$.util.stream.BaseStream
    IntStream sequential();

    IntStream skip(long j);

    IntStream sorted();

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    j$.util.H spliterator();

    int sum();

    j$.util.i summaryStatistics();

    int[] toArray();
}
