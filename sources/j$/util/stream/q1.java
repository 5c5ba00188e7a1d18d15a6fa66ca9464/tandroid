package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class q1 extends P2 implements D0, y0 {
    @Override // j$.util.stream.D0
    public final D0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public final /* synthetic */ void accept(double d) {
        u0.i0();
        throw null;
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void accept(int i) {
        u0.p0();
        throw null;
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void accept(long j) {
        u0.q0();
        throw null;
    }

    @Override // j$.util.stream.P2, j$.util.function.Consumer
    public final void accept(Object obj) {
        super.accept(obj);
    }

    @Override // j$.util.stream.y0
    public final D0 build() {
        return this;
    }

    @Override // j$.util.stream.D0
    public final void e(Object[] objArr, int i) {
        long j = i;
        long count = count() + j;
        if (count > objArr.length || count < j) {
            throw new IndexOutOfBoundsException("does not fit");
        }
        if (this.c == 0) {
            System.arraycopy(this.e, 0, objArr, i, this.b);
            return;
        }
        for (int i2 = 0; i2 < this.c; i2++) {
            Object[] objArr2 = this.f[i2];
            System.arraycopy(objArr2, 0, objArr, i, objArr2.length);
            i += this.f[i2].length;
        }
        int i3 = this.b;
        if (i3 > 0) {
            System.arraycopy(this.e, 0, objArr, i, i3);
        }
    }

    @Override // j$.util.stream.f2
    public final void end() {
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        clear();
        r(j);
    }

    @Override // j$.util.stream.P2, j$.util.stream.D0
    public final void forEach(Consumer consumer) {
        super.forEach(consumer);
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ boolean h() {
        return false;
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ int j() {
        return 0;
    }

    @Override // j$.util.stream.D0
    public final Object[] o(j$.util.function.N n) {
        long count = count();
        if (count < 2147483639) {
            Object[] objArr = (Object[]) n.apply((int) count);
            e(objArr, 0);
            return objArr;
        }
        throw new IllegalArgumentException("Stream size exceeds max array size");
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ D0 q(long j, long j2, j$.util.function.N n) {
        return u0.B0(this, j, j2, n);
    }

    @Override // j$.util.stream.P2, java.lang.Iterable
    public final j$.util.Q spliterator() {
        return super.spliterator();
    }
}
