package j$.util.stream;

import java.util.ArrayDeque;
import java.util.Comparator;
/* loaded from: classes2.dex */
abstract class h1 implements j$.util.Q {
    D0 a;
    int b;
    j$.util.Q c;
    j$.util.Q d;
    ArrayDeque e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h1(D0 d0) {
        this.a = d0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static D0 f(ArrayDeque arrayDeque) {
        while (true) {
            D0 d0 = (D0) arrayDeque.pollFirst();
            if (d0 == null) {
                return null;
            }
            if (d0.j() != 0) {
                int j = d0.j();
                while (true) {
                    j--;
                    if (j >= 0) {
                        arrayDeque.addFirst(d0.a(j));
                    }
                }
            } else if (d0.count() > 0) {
                return d0;
            }
        }
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return 64;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        long j = 0;
        if (this.a == null) {
            return 0L;
        }
        j$.util.Q q = this.c;
        if (q != null) {
            return q.estimateSize();
        }
        for (int i = this.b; i < this.a.j(); i++) {
            j += this.a.a(i).count();
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ArrayDeque g() {
        ArrayDeque arrayDeque = new ArrayDeque(8);
        int j = this.a.j();
        while (true) {
            j--;
            if (j < this.b) {
                return arrayDeque;
            }
            arrayDeque.addFirst(this.a.a(j));
        }
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.i(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean h() {
        if (this.a == null) {
            return false;
        }
        if (this.d == null) {
            j$.util.Q q = this.c;
            if (q == null) {
                ArrayDeque g = g();
                this.e = g;
                D0 f = f(g);
                if (f == null) {
                    this.a = null;
                    return false;
                }
                q = f.spliterator();
            }
            this.d = q;
            return true;
        }
        return true;
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.k(this, i);
    }

    @Override // j$.util.Q
    public /* bridge */ /* synthetic */ j$.util.E trySplit() {
        return (j$.util.E) trySplit();
    }

    @Override // j$.util.Q
    public /* bridge */ /* synthetic */ j$.util.H trySplit() {
        return (j$.util.H) trySplit();
    }

    @Override // j$.util.Q
    public /* bridge */ /* synthetic */ j$.util.K trySplit() {
        return (j$.util.K) trySplit();
    }

    @Override // j$.util.Q
    public /* bridge */ /* synthetic */ j$.util.N trySplit() {
        return (j$.util.N) trySplit();
    }

    @Override // j$.util.Q
    public final j$.util.Q trySplit() {
        D0 d0 = this.a;
        if (d0 == null || this.d != null) {
            return null;
        }
        j$.util.Q q = this.c;
        if (q != null) {
            return q.trySplit();
        }
        if (this.b < d0.j() - 1) {
            D0 d02 = this.a;
            int i = this.b;
            this.b = i + 1;
            return d02.a(i).spliterator();
        }
        D0 a = this.a.a(this.b);
        this.a = a;
        if (a.j() == 0) {
            j$.util.Q spliterator = this.a.spliterator();
            this.c = spliterator;
            return spliterator.trySplit();
        }
        D0 d03 = this.a;
        this.b = 0 + 1;
        return d03.a(0).spliterator();
    }
}
