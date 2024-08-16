package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class Q0 extends H0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Q0(F0 f0, F0 f02) {
        super(f0, f02);
    }

    @Override // j$.util.stream.F0
    public final void forEach(Consumer consumer) {
        this.a.forEach(consumer);
        this.b.forEach(consumer);
    }

    @Override // j$.util.stream.F0
    public final void i(Object[] objArr, int i) {
        objArr.getClass();
        F0 f0 = this.a;
        f0.i(objArr, i);
        this.b.i(objArr, i + ((int) f0.count()));
    }

    @Override // j$.util.stream.F0
    public final Object[] s(j$.util.function.I i) {
        long count = count();
        if (count < 2147483639) {
            Object[] objArr = (Object[]) i.apply((int) count);
            i(objArr, 0);
            return objArr;
        }
        throw new IllegalArgumentException("Stream size exceeds max array size");
    }

    @Override // j$.util.stream.F0
    public final j$.util.Q spliterator() {
        return new h1(this);
    }

    @Override // j$.util.stream.F0
    public final F0 t(long j, long j2, j$.util.function.I i) {
        if (j == 0 && j2 == count()) {
            return this;
        }
        long count = this.a.count();
        if (j >= count) {
            return this.b.t(j - count, j2 - count, i);
        } else if (j2 <= count) {
            return this.a.t(j, j2, i);
        } else {
            return t0.I(T2.REFERENCE, this.a.t(j, count, i), this.b.t(0L, j2 - count, i));
        }
    }

    public final String toString() {
        return count() < 32 ? String.format("ConcNode[%s.%s]", this.a, this.b) : String.format("ConcNode[size=%d]", Long.valueOf(count()));
    }
}
