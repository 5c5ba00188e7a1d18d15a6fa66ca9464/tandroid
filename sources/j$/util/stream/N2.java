package j$.util.stream;

import java.util.Arrays;
import java.util.Spliterator;

/* loaded from: classes2.dex */
abstract class N2 extends d implements Iterable {
    Object e;
    Object[] f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public N2() {
        this.e = c(16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public N2(int i) {
        super(i);
        this.e = c(1 << this.a);
    }

    public abstract Object c(int i);

    @Override // j$.util.stream.d
    public final void clear() {
        Object[] objArr = this.f;
        if (objArr != null) {
            this.e = objArr[0];
            this.f = null;
            this.d = null;
        }
        this.b = 0;
        this.c = 0;
    }

    public void d(Object obj, int i) {
        long j = i;
        long count = count() + j;
        if (count > v(obj) || count < j) {
            throw new IndexOutOfBoundsException("does not fit");
        }
        if (this.c == 0) {
            System.arraycopy(this.e, 0, obj, i, this.b);
            return;
        }
        for (int i2 = 0; i2 < this.c; i2++) {
            Object obj2 = this.f[i2];
            System.arraycopy(obj2, 0, obj, i, v(obj2));
            i += v(this.f[i2]);
        }
        int i3 = this.b;
        if (i3 > 0) {
            System.arraycopy(this.e, 0, obj, i, i3);
        }
    }

    public Object e() {
        long count = count();
        if (count >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        Object c = c((int) count);
        d(c, 0);
        return c;
    }

    public void g(Object obj) {
        for (int i = 0; i < this.c; i++) {
            Object obj2 = this.f[i];
            u(obj2, 0, v(obj2), obj);
        }
        u(this.e, 0, this.b, obj);
    }

    public abstract j$.util.Q spliterator();

    @Override // java.lang.Iterable
    public final /* synthetic */ Spliterator spliterator() {
        return j$.util.P.a(spliterator());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void u(Object obj, int i, int i2, Object obj2);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int v(Object obj);

    /* JADX INFO: Access modifiers changed from: protected */
    public final int w(long j) {
        if (this.c == 0) {
            if (j < this.b) {
                return 0;
            }
            throw new IndexOutOfBoundsException(Long.toString(j));
        }
        if (j >= count()) {
            throw new IndexOutOfBoundsException(Long.toString(j));
        }
        for (int i = 0; i <= this.c; i++) {
            if (j < this.d[i] + v(this.f[i])) {
                return i;
            }
        }
        throw new IndexOutOfBoundsException(Long.toString(j));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void x(long j) {
        long v;
        int i = this.c;
        if (i == 0) {
            v = v(this.e);
        } else {
            v = v(this.f[i]) + this.d[i];
        }
        if (j > v) {
            if (this.f == null) {
                Object[] y = y();
                this.f = y;
                this.d = new long[8];
                y[0] = this.e;
            }
            int i2 = this.c + 1;
            while (j > v) {
                Object[] objArr = this.f;
                if (i2 >= objArr.length) {
                    int length = objArr.length * 2;
                    this.f = Arrays.copyOf(objArr, length);
                    this.d = Arrays.copyOf(this.d, length);
                }
                int i3 = this.a;
                if (i2 != 0 && i2 != 1) {
                    i3 = Math.min((i3 + i2) - 1, 30);
                }
                int i4 = 1 << i3;
                this.f[i2] = c(i4);
                long[] jArr = this.d;
                jArr[i2] = jArr[i2 - 1] + v(this.f[r6]);
                v += i4;
                i2++;
            }
        }
    }

    protected abstract Object[] y();

    /* JADX INFO: Access modifiers changed from: protected */
    public final void z() {
        long v;
        if (this.b == v(this.e)) {
            if (this.f == null) {
                Object[] y = y();
                this.f = y;
                this.d = new long[8];
                y[0] = this.e;
            }
            int i = this.c;
            int i2 = i + 1;
            Object[] objArr = this.f;
            if (i2 >= objArr.length || objArr[i2] == null) {
                if (i == 0) {
                    v = v(this.e);
                } else {
                    v = v(objArr[i]) + this.d[i];
                }
                x(v + 1);
            }
            this.b = 0;
            int i3 = this.c + 1;
            this.c = i3;
            this.e = this.f[i3];
        }
    }
}
