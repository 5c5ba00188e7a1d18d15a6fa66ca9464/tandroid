package j$.util.stream;

import j$.util.stream.IntStream;
import j$.util.stream.Stream;
import java.util.Iterator;
import java.util.stream.DoubleStream;
/* loaded from: classes2.dex */
public final /* synthetic */ class g implements BaseStream {
    public final /* synthetic */ java.util.stream.BaseStream a;

    private /* synthetic */ g(java.util.stream.BaseStream baseStream) {
        this.a = baseStream;
    }

    public static /* synthetic */ BaseStream i0(java.util.stream.BaseStream baseStream) {
        if (baseStream == null) {
            return null;
        }
        return baseStream instanceof h ? ((h) baseStream).a : baseStream instanceof DoubleStream ? D.i0((DoubleStream) baseStream) : baseStream instanceof java.util.stream.IntStream ? IntStream.VivifiedWrapper.convert((java.util.stream.IntStream) baseStream) : baseStream instanceof java.util.stream.LongStream ? k0.i0((java.util.stream.LongStream) baseStream) : baseStream instanceof java.util.stream.Stream ? Stream.VivifiedWrapper.convert((java.util.stream.Stream) baseStream) : new g(baseStream);
    }

    @Override // j$.util.stream.BaseStream, java.lang.AutoCloseable
    public final /* synthetic */ void close() {
        this.a.close();
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof g) {
            obj = ((g) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream onClose(Runnable runnable) {
        return i0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ BaseStream parallel() {
        return i0(this.a.parallel());
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ BaseStream sequential() {
        return i0(this.a.sequential());
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ j$.util.Q spliterator() {
        return j$.util.O.f(this.a.spliterator());
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream unordered() {
        return i0(this.a.unordered());
    }
}
