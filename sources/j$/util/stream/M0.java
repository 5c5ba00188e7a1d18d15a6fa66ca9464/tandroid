package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class M0 extends O0 implements A0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public M0(A0 a0, A0 a02) {
        super(a0, a02);
    }

    @Override // j$.util.stream.D0
    /* renamed from: f */
    public final /* synthetic */ void e(Integer[] numArr, int i) {
        u0.t0(this, numArr, i);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ void forEach(Consumer consumer) {
        u0.w0(this, consumer);
    }

    @Override // j$.util.stream.C0
    public final Object newArray(int i) {
        return new int[i];
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ D0 q(long j, long j2, j$.util.function.N n) {
        return u0.z0(this, j, j2);
    }

    @Override // j$.util.stream.D0
    public final j$.util.N spliterator() {
        return new d1(this);
    }

    @Override // j$.util.stream.D0
    public final j$.util.Q spliterator() {
        return new d1(this);
    }
}
