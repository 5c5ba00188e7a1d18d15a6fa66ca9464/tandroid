package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class N0 extends O0 implements B0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public N0(B0 b0, B0 b02) {
        super(b0, b02);
    }

    @Override // j$.util.stream.D0
    /* renamed from: f */
    public final /* synthetic */ void e(Long[] lArr, int i) {
        u0.u0(this, lArr, i);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ void forEach(Consumer consumer) {
        u0.x0(this, consumer);
    }

    @Override // j$.util.stream.C0
    public final Object newArray(int i) {
        return new long[i];
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ D0 q(long j, long j2, j$.util.function.N n) {
        return u0.A0(this, j, j2);
    }

    @Override // j$.util.stream.D0
    public final j$.util.N spliterator() {
        return new e1(this);
    }

    @Override // j$.util.stream.D0
    public final j$.util.Q spliterator() {
        return new e1(this);
    }
}
