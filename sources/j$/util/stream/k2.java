package j$.util.stream;

import j$.util.s;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
/* loaded from: classes2.dex */
abstract class k2 implements j$.util.s {
    A1 a;
    int b;
    j$.util.s c;
    j$.util.s d;
    Deque e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k2(A1 a1) {
        this.a = a1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final A1 a(Deque deque) {
        while (true) {
            A1 a1 = (A1) deque.pollFirst();
            if (a1 == null) {
                return null;
            }
            if (a1.p() != 0) {
                for (int p = a1.p() - 1; p >= 0; p--) {
                    deque.addFirst(a1.b(p));
                }
            } else if (a1.count() > 0) {
                return a1;
            }
        }
    }

    @Override // j$.util.s
    public final int characteristics() {
        return 64;
    }

    @Override // j$.util.s
    public final long estimateSize() {
        long j = 0;
        if (this.a == null) {
            return 0L;
        }
        j$.util.s sVar = this.c;
        if (sVar != null) {
            return sVar.estimateSize();
        }
        for (int i = this.b; i < this.a.p(); i++) {
            j += this.a.b(i).count();
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Deque f() {
        ArrayDeque arrayDeque = new ArrayDeque(8);
        int p = this.a.p();
        while (true) {
            p--;
            if (p < this.b) {
                return arrayDeque;
            }
            arrayDeque.addFirst(this.a.b(p));
        }
    }

    @Override // j$.util.s
    public Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.s
    public /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.e(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean h() {
        if (this.a == null) {
            return false;
        }
        if (this.d == null) {
            j$.util.s sVar = this.c;
            if (sVar == null) {
                Deque f = f();
                this.e = f;
                A1 a = a(f);
                if (a == null) {
                    this.a = null;
                    return false;
                }
                sVar = a.spliterator();
            }
            this.d = sVar;
            return true;
        }
        return true;
    }

    @Override // j$.util.s
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.f(this, i);
    }

    @Override // j$.util.s
    public /* bridge */ /* synthetic */ s.a trySplit() {
        return (s.a) trySplit();
    }

    @Override // j$.util.s
    public /* bridge */ /* synthetic */ s.b trySplit() {
        return (s.b) trySplit();
    }

    @Override // j$.util.s
    public /* bridge */ /* synthetic */ s.c trySplit() {
        return (s.c) trySplit();
    }

    @Override // j$.util.s
    public final j$.util.s trySplit() {
        A1 a1 = this.a;
        if (a1 == null || this.d != null) {
            return null;
        }
        j$.util.s sVar = this.c;
        if (sVar != null) {
            return sVar.trySplit();
        }
        if (this.b < a1.p() - 1) {
            A1 a12 = this.a;
            int i = this.b;
            this.b = i + 1;
            return a12.b(i).spliterator();
        }
        A1 b = this.a.b(this.b);
        this.a = b;
        if (b.p() == 0) {
            j$.util.s spliterator = this.a.spliterator();
            this.c = spliterator;
            return spliterator.trySplit();
        }
        this.b = 0;
        A1 a13 = this.a;
        this.b = 1;
        return a13.b(0).spliterator();
    }

    @Override // j$.util.s
    public /* bridge */ /* synthetic */ j$.util.t trySplit() {
        return (j$.util.t) trySplit();
    }
}
