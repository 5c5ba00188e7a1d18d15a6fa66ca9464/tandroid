package j$.wrappers;

import java.util.Iterator;
import java.util.stream.BaseStream;
/* loaded from: classes2.dex */
public final /* synthetic */ class H0 implements j$.util.stream.g {
    final /* synthetic */ BaseStream a;

    private /* synthetic */ H0(BaseStream baseStream) {
        this.a = baseStream;
    }

    public static /* synthetic */ j$.util.stream.g n0(BaseStream baseStream) {
        if (baseStream == null) {
            return null;
        }
        return baseStream instanceof I0 ? ((I0) baseStream).a : new H0(baseStream);
    }

    @Override // j$.util.stream.g, java.lang.AutoCloseable
    public /* synthetic */ void close() {
        this.a.close();
    }

    @Override // j$.util.stream.g
    public /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.g
    /* renamed from: iterator */
    public /* synthetic */ Iterator mo331iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g onClose(Runnable runnable) {
        return n0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.g, j$.util.stream.IntStream
    /* renamed from: parallel */
    public /* synthetic */ j$.util.stream.g mo332parallel() {
        return n0(this.a.parallel());
    }

    @Override // j$.util.stream.g, j$.util.stream.IntStream
    /* renamed from: sequential */
    public /* synthetic */ j$.util.stream.g mo333sequential() {
        return n0(this.a.sequential());
    }

    @Override // j$.util.stream.g
    /* renamed from: spliterator */
    public /* synthetic */ j$.util.u mo334spliterator() {
        return g.a(this.a.spliterator());
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g unordered() {
        return n0(this.a.unordered());
    }
}
