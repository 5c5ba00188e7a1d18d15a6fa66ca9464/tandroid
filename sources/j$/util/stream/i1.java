package j$.util.stream;

import java.util.ArrayDeque;
import java.util.Comparator;
/* loaded from: classes2.dex */
abstract class i1 implements j$.util.Q {
    F0 a;
    int b;
    j$.util.Q c;
    j$.util.Q d;
    ArrayDeque e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i1(F0 f0) {
        this.a = f0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static F0 b(ArrayDeque arrayDeque) {
        while (true) {
            F0 f0 = (F0) arrayDeque.pollFirst();
            if (f0 == null) {
                return null;
            }
            if (f0.p() != 0) {
                for (int p = f0.p() - 1; p >= 0; p--) {
                    arrayDeque.addFirst(f0.a(p));
                }
            } else if (f0.count() > 0) {
                return f0;
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
        for (int i = this.b; i < this.a.p(); i++) {
            j += this.a.a(i).count();
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ArrayDeque f() {
        ArrayDeque arrayDeque = new ArrayDeque(8);
        int p = this.a.p();
        while (true) {
            p--;
            if (p < this.b) {
                return arrayDeque;
            }
            arrayDeque.addFirst(this.a.a(p));
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

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean h() {
        if (this.a == null) {
            return false;
        }
        if (this.d == null) {
            j$.util.Q q = this.c;
            if (q == null) {
                ArrayDeque f = f();
                this.e = f;
                F0 b = b(f);
                if (b == null) {
                    this.a = null;
                    return false;
                }
                q = b.spliterator();
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
        F0 f0 = this.a;
        if (f0 == null || this.d != null) {
            return null;
        }
        j$.util.Q q = this.c;
        if (q != null) {
            return q.trySplit();
        }
        if (this.b < f0.p() - 1) {
            F0 f02 = this.a;
            int i = this.b;
            this.b = i + 1;
            return f02.a(i).spliterator();
        }
        F0 a = this.a.a(this.b);
        this.a = a;
        if (a.p() == 0) {
            j$.util.Q spliterator = this.a.spliterator();
            this.c = spliterator;
            return spliterator.trySplit();
        }
        F0 f03 = this.a;
        this.b = 1;
        return f03.a(0).spliterator();
    }
}
