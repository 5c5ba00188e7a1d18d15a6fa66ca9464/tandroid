package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class P0 extends F0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public P0(D0 d0, D0 d02) {
        super(d0, d02);
    }

    @Override // j$.util.stream.D0
    public final void e(Object[] objArr, int i) {
        objArr.getClass();
        D0 d0 = this.a;
        d0.e(objArr, i);
        this.b.e(objArr, i + ((int) d0.count()));
    }

    @Override // j$.util.stream.D0
    public final void forEach(Consumer consumer) {
        this.a.forEach(consumer);
        this.b.forEach(consumer);
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
    public final D0 q(long j, long j2, j$.util.function.N n) {
        if (j == 0 && j2 == count()) {
            return this;
        }
        long count = this.a.count();
        return j >= count ? this.b.q(j - count, j2 - count, n) : j2 <= count ? this.a.q(j, j2, n) : u1.l(U2.REFERENCE, this.a.q(j, count, n), this.b.q(0L, j2 - count, n));
    }

    @Override // j$.util.stream.D0
    public final j$.util.Q spliterator() {
        return new g1(this);
    }

    public final String toString() {
        return count() < 32 ? String.format("ConcNode[%s.%s]", this.a, this.b) : String.format("ConcNode[size=%d]", Long.valueOf(count()));
    }
}
