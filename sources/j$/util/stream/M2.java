package j$.util.stream;

import java.util.Comparator;

/* loaded from: classes2.dex */
abstract class M2 implements j$.util.N {
    int a;
    final int b;
    int c;
    final int d;
    Object e;
    final /* synthetic */ N2 f;

    M2(N2 n2, int i, int i2, int i3, int i4) {
        this.f = n2;
        this.a = i;
        this.b = i2;
        this.c = i3;
        this.d = i4;
        Object[] objArr = n2.f;
        this.e = objArr == null ? n2.e : objArr[i];
    }

    abstract void b(int i, Object obj, Object obj2);

    @Override // j$.util.Q
    public final int characteristics() {
        return 16464;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        int i = this.a;
        int i2 = this.d;
        int i3 = this.b;
        if (i == i3) {
            return i2 - this.c;
        }
        long[] jArr = this.f.d;
        return ((jArr[i3] + i2) - jArr[i]) - this.c;
    }

    abstract j$.util.N f(Object obj, int i, int i2);

    @Override // j$.util.N
    /* renamed from: forEachRemaining, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public final void e(Object obj) {
        N2 n2;
        obj.getClass();
        int i = this.a;
        int i2 = this.d;
        int i3 = this.b;
        if (i < i3 || (i == i3 && this.c < i2)) {
            int i4 = this.c;
            while (true) {
                n2 = this.f;
                if (i >= i3) {
                    break;
                }
                Object obj2 = n2.f[i];
                n2.u(obj2, i4, n2.v(obj2), obj);
                i++;
                i4 = 0;
            }
            n2.u(this.a == i3 ? this.e : n2.f[i3], i4, i2, obj);
            this.a = i3;
            this.c = i2;
        }
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.j(this);
    }

    abstract j$.util.N h(int i, int i2, int i3, int i4);

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.k(this, i);
    }

    @Override // j$.util.N
    /* renamed from: tryAdvance, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public final boolean p(Object obj) {
        obj.getClass();
        int i = this.a;
        int i2 = this.b;
        if (i >= i2 && (i != i2 || this.c >= this.d)) {
            return false;
        }
        Object obj2 = this.e;
        int i3 = this.c;
        this.c = i3 + 1;
        b(i3, obj2, obj);
        int i4 = this.c;
        Object obj3 = this.e;
        N2 n2 = this.f;
        if (i4 == n2.v(obj3)) {
            this.c = 0;
            int i5 = this.a + 1;
            this.a = i5;
            Object[] objArr = n2.f;
            if (objArr != null && i5 <= i2) {
                this.e = objArr[i5];
            }
        }
        return true;
    }

    @Override // j$.util.N, j$.util.Q
    public /* bridge */ /* synthetic */ j$.util.E trySplit() {
        return (j$.util.E) trySplit();
    }

    @Override // j$.util.N, j$.util.Q
    public /* bridge */ /* synthetic */ j$.util.H trySplit() {
        return (j$.util.H) trySplit();
    }

    @Override // j$.util.N, j$.util.Q
    public /* bridge */ /* synthetic */ j$.util.K trySplit() {
        return (j$.util.K) trySplit();
    }

    @Override // j$.util.Q
    public final j$.util.N trySplit() {
        int i = this.a;
        int i2 = this.b;
        if (i < i2) {
            int i3 = i2 - 1;
            int i4 = this.c;
            N2 n2 = this.f;
            j$.util.N h = h(i, i3, i4, n2.v(n2.f[i3]));
            this.a = i2;
            this.c = 0;
            this.e = n2.f[i2];
            return h;
        }
        if (i != i2) {
            return null;
        }
        int i5 = this.c;
        int i6 = (this.d - i5) / 2;
        if (i6 == 0) {
            return null;
        }
        j$.util.N f = f(this.e, i5, i6);
        this.c += i6;
        return f;
    }
}
