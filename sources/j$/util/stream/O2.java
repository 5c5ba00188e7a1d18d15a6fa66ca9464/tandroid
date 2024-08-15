package j$.util.stream;

import java.util.Arrays;
import java.util.Spliterator;
/* loaded from: classes2.dex */
abstract class O2 extends e implements Iterable {
    Object e;
    Object[] f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public O2() {
        this.e = newArray(16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public O2(int i) {
        super(i);
        this.e = newArray(1 << this.a);
    }

    public Object b() {
        long count = count();
        if (count < 2147483639) {
            Object newArray = newArray((int) count);
            c(newArray, 0);
            return newArray;
        }
        throw new IllegalArgumentException("Stream size exceeds max array size");
    }

    public void c(Object obj, int i) {
        long j = i;
        long count = count() + j;
        if (count > s(obj) || count < j) {
            throw new IndexOutOfBoundsException("does not fit");
        }
        if (this.c == 0) {
            System.arraycopy(this.e, 0, obj, i, this.b);
            return;
        }
        for (int i2 = 0; i2 < this.c; i2++) {
            Object obj2 = this.f[i2];
            System.arraycopy(obj2, 0, obj, i, s(obj2));
            i += s(this.f[i2]);
        }
        int i3 = this.b;
        if (i3 > 0) {
            System.arraycopy(this.e, 0, obj, i, i3);
        }
    }

    @Override // j$.util.stream.e
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

    public void d(Object obj) {
        for (int i = 0; i < this.c; i++) {
            Object obj2 = this.f[i];
            r(obj2, 0, s(obj2), obj);
        }
        r(this.e, 0, this.b, obj);
    }

    public abstract Object newArray(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void r(Object obj, int i, int i2, Object obj2);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int s(Object obj);

    public abstract j$.util.Q spliterator();

    @Override // java.lang.Iterable
    public final /* synthetic */ Spliterator spliterator() {
        return j$.util.P.a(spliterator());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int t(long j) {
        if (this.c == 0) {
            if (j < this.b) {
                return 0;
            }
            throw new IndexOutOfBoundsException(Long.toString(j));
        } else if (j < count()) {
            for (int i = 0; i <= this.c; i++) {
                if (j < this.d[i] + s(this.f[i])) {
                    return i;
                }
            }
            throw new IndexOutOfBoundsException(Long.toString(j));
        } else {
            throw new IndexOutOfBoundsException(Long.toString(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void u(long j) {
        long s;
        int i;
        int i2 = this.c;
        if (i2 == 0) {
            s = s(this.e);
        } else {
            s = s(this.f[i2]) + this.d[i2];
        }
        if (j <= s) {
            return;
        }
        if (this.f == null) {
            Object[] v = v();
            this.f = v;
            this.d = new long[8];
            v[0] = this.e;
        }
        int i3 = this.c;
        while (true) {
            i3++;
            if (j <= s) {
                return;
            }
            Object[] objArr = this.f;
            if (i3 >= objArr.length) {
                int length = objArr.length * 2;
                this.f = Arrays.copyOf(objArr, length);
                this.d = Arrays.copyOf(this.d, length);
            }
            int i4 = this.a;
            if (i3 != 0 && i3 != 1) {
                i4 = Math.min((i4 + i3) - 1, 30);
            }
            int i5 = 1 << i4;
            this.f[i3] = newArray(i5);
            long[] jArr = this.d;
            jArr[i3] = jArr[i3 - 1] + s(this.f[i]);
            s += i5;
        }
    }

    protected abstract Object[] v();

    /* JADX INFO: Access modifiers changed from: protected */
    public final void w() {
        long s;
        if (this.b == s(this.e)) {
            if (this.f == null) {
                Object[] v = v();
                this.f = v;
                this.d = new long[8];
                v[0] = this.e;
            }
            int i = this.c;
            int i2 = i + 1;
            Object[] objArr = this.f;
            if (i2 >= objArr.length || objArr[i2] == null) {
                if (i == 0) {
                    s = s(this.e);
                } else {
                    s = s(objArr[i]) + this.d[i];
                }
                u(s + 1);
            }
            this.b = 0;
            int i3 = this.c + 1;
            this.c = i3;
            this.e = this.f[i3];
        }
    }
}
