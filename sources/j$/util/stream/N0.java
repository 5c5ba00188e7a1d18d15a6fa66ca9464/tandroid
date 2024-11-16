package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
final class N0 extends P0 implements B0 {
    N0(B0 b0, B0 b02) {
        super(b0, b02);
    }

    @Override // j$.util.stream.F0
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public final /* synthetic */ void i(Integer[] numArr, int i) {
        t0.o(this, numArr, i);
    }

    @Override // j$.util.stream.E0
    public final Object c(int i) {
        return new int[i];
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ void forEach(Consumer consumer) {
        t0.r(this, consumer);
    }

    @Override // j$.util.stream.F0
    public final j$.util.N spliterator() {
        return new e1(this);
    }

    @Override // j$.util.stream.F0
    public final j$.util.Q spliterator() {
        return new e1(this);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.u(this, j, j2);
    }
}
