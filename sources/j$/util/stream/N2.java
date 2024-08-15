package j$.util.stream;

import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class N2 implements j$.util.N {
    int a;
    final int b;
    int c;
    final int d;
    Object e;
    final /* synthetic */ O2 f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public N2(O2 o2, int i, int i2, int i3, int i4) {
        this.f = o2;
        this.a = i;
        this.b = i2;
        this.c = i3;
        this.d = i4;
        Object[] objArr = o2.f;
        this.e = objArr == null ? o2.e : objArr[i];
    }

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

    abstract void f(int i, Object obj, Object obj2);

    @Override // j$.util.N
    /* renamed from: forEachRemaining */
    public final void d(Object obj) {
        O2 o2;
        obj.getClass();
        int i = this.a;
        int i2 = this.d;
        int i3 = this.b;
        if (i < i3 || (i == i3 && this.c < i2)) {
            int i4 = this.c;
            while (true) {
                o2 = this.f;
                if (i >= i3) {
                    break;
                }
                Object obj2 = o2.f[i];
                o2.r(obj2, i4, o2.s(obj2), obj);
                i++;
                i4 = 0;
            }
            o2.r(this.a == i3 ? this.e : o2.f[i3], i4, i2, obj);
            this.a = i3;
            this.c = i2;
        }
    }

    abstract j$.util.N g(Object obj, int i, int i2);

    @Override // j$.util.Q
    public final Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.i(this);
    }

    abstract j$.util.N h(int i, int i2, int i3, int i4);

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.k(this, i);
    }

    @Override // j$.util.N
    /* renamed from: tryAdvance */
    public final boolean o(Object obj) {
        obj.getClass();
        int i = this.a;
        int i2 = this.b;
        if (i < i2 || (i == i2 && this.c < this.d)) {
            Object obj2 = this.e;
            int i3 = this.c;
            this.c = i3 + 1;
            f(i3, obj2, obj);
            int i4 = this.c;
            Object obj3 = this.e;
            O2 o2 = this.f;
            if (i4 == o2.s(obj3)) {
                this.c = 0;
                int i5 = this.a + 1;
                this.a = i5;
                Object[] objArr = o2.f;
                if (objArr != null && i5 <= i2) {
                    this.e = objArr[i5];
                }
            }
            return true;
        }
        return false;
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
            int i3 = this.c;
            O2 o2 = this.f;
            j$.util.N h = h(i, i2 - 1, i3, o2.s(o2.f[i2 - 1]));
            this.a = i2;
            this.c = 0;
            this.e = o2.f[i2];
            return h;
        } else if (i == i2) {
            int i4 = this.c;
            int i5 = (this.d - i4) / 2;
            if (i5 == 0) {
                return null;
            }
            j$.util.N g = g(this.e, i4, i5);
            this.c += i5;
            return g;
        } else {
            return null;
        }
    }
}
