package j$.util.stream;

import j$.util.function.Consumer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;

/* loaded from: classes2.dex */
class O2 extends d implements Consumer, Iterable {
    protected Object[] e = new Object[1 << 4];
    protected Object[][] f;

    O2() {
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public void r(Object obj) {
        long length;
        int i = this.b;
        Object[] objArr = this.e;
        if (i == objArr.length) {
            if (this.f == null) {
                Object[][] objArr2 = new Object[8][];
                this.f = objArr2;
                this.d = new long[8];
                objArr2[0] = objArr;
            }
            int i2 = this.c;
            int i3 = i2 + 1;
            Object[][] objArr3 = this.f;
            if (i3 >= objArr3.length || objArr3[i3] == null) {
                if (i2 == 0) {
                    length = objArr.length;
                } else {
                    length = objArr3[i2].length + this.d[i2];
                }
                u(length + 1);
            }
            this.b = 0;
            int i4 = this.c + 1;
            this.c = i4;
            this.e = this.f[i4];
        }
        Object[] objArr4 = this.e;
        int i5 = this.b;
        this.b = i5 + 1;
        objArr4[i5] = obj;
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.d
    public final void clear() {
        Object[][] objArr = this.f;
        if (objArr != null) {
            this.e = objArr[0];
            int i = 0;
            while (true) {
                Object[] objArr2 = this.e;
                if (i >= objArr2.length) {
                    break;
                }
                objArr2[i] = null;
                i++;
            }
            this.f = null;
            this.d = null;
        } else {
            for (int i2 = 0; i2 < this.b; i2++) {
                this.e[i2] = null;
            }
        }
        this.b = 0;
        this.c = 0;
    }

    public void forEach(Consumer consumer) {
        for (int i = 0; i < this.c; i++) {
            for (Object obj : this.f[i]) {
                consumer.r(obj);
            }
        }
        for (int i2 = 0; i2 < this.b; i2++) {
            consumer.r(this.e[i2]);
        }
    }

    @Override // java.lang.Iterable
    public final /* synthetic */ void forEach(java.util.function.Consumer consumer) {
        forEach(j$.util.function.g.a(consumer));
    }

    @Override // java.lang.Iterable
    public final Iterator iterator() {
        return j$.util.f0.i(spliterator());
    }

    @Override // java.lang.Iterable
    public j$.util.Q spliterator() {
        return new F2(this, 0, this.c, 0, this.b);
    }

    @Override // java.lang.Iterable
    public final /* synthetic */ Spliterator spliterator() {
        return j$.util.P.a(spliterator());
    }

    public final String toString() {
        ArrayList arrayList = new ArrayList();
        forEach(new a(arrayList, 10));
        return "SpinedBuffer:" + arrayList.toString();
    }

    protected final void u(long j) {
        int i = this.c;
        long length = i == 0 ? this.e.length : this.d[i] + this.f[i].length;
        if (j > length) {
            if (this.f == null) {
                Object[][] objArr = new Object[8][];
                this.f = objArr;
                this.d = new long[8];
                objArr[0] = this.e;
            }
            int i2 = i + 1;
            while (j > length) {
                Object[][] objArr2 = this.f;
                if (i2 >= objArr2.length) {
                    int length2 = objArr2.length * 2;
                    this.f = (Object[][]) Arrays.copyOf(objArr2, length2);
                    this.d = Arrays.copyOf(this.d, length2);
                }
                int i3 = this.a;
                if (i2 != 0 && i2 != 1) {
                    i3 = Math.min((i3 + i2) - 1, 30);
                }
                int i4 = 1 << i3;
                this.f[i2] = new Object[i4];
                long[] jArr = this.d;
                jArr[i2] = jArr[i2 - 1] + r5[r7].length;
                length += i4;
                i2++;
            }
        }
    }
}
