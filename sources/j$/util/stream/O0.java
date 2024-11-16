package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
final class O0 extends P0 implements D0 {
    O0(D0 d0, D0 d02) {
        super(d0, d02);
    }

    @Override // j$.util.stream.F0
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public final /* synthetic */ void i(Long[] lArr, int i) {
        t0.p(this, lArr, i);
    }

    @Override // j$.util.stream.E0
    public final Object c(int i) {
        return new long[i];
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ void forEach(Consumer consumer) {
        t0.s(this, consumer);
    }

    @Override // j$.util.stream.F0
    public final j$.util.N spliterator() {
        return new f1(this);
    }

    @Override // j$.util.stream.F0
    public final j$.util.Q spliterator() {
        return new f1(this);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.v(this, j, j2);
    }
}
