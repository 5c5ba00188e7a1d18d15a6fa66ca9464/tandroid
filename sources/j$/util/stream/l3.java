package j$.util.stream;

import java.util.Comparator;

/* loaded from: classes2.dex */
abstract class l3 extends n3 implements j$.util.N {
    l3(j$.util.N n, long j, long j2) {
        super(n, j, j2, 0L, Math.min(n.estimateSize(), j2));
    }

    l3(j$.util.N n, long j, long j2, long j3, long j4) {
        super(n, j, j2, j3, j4);
    }

    protected abstract Object f();

    @Override // j$.util.N
    /* renamed from: forEachRemaining, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public final void e(Object obj) {
        obj.getClass();
        long j = this.e;
        long j2 = this.a;
        if (j2 >= j) {
            return;
        }
        long j3 = this.d;
        if (j3 >= j) {
            return;
        }
        if (j3 >= j2 && ((j$.util.N) this.c).estimateSize() + j3 <= this.b) {
            ((j$.util.N) this.c).e(obj);
            this.d = this.e;
            return;
        }
        while (j2 > this.d) {
            ((j$.util.N) this.c).p(f());
            this.d++;
        }
        while (this.d < this.e) {
            ((j$.util.N) this.c).p(obj);
            this.d++;
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

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.k(this, i);
    }

    @Override // j$.util.N
    /* renamed from: tryAdvance, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public final boolean p(Object obj) {
        long j;
        obj.getClass();
        long j2 = this.e;
        long j3 = this.a;
        if (j3 >= j2) {
            return false;
        }
        while (true) {
            j = this.d;
            if (j3 <= j) {
                break;
            }
            ((j$.util.N) this.c).p(f());
            this.d++;
        }
        if (j >= this.e) {
            return false;
        }
        this.d = j + 1;
        return ((j$.util.N) this.c).p(obj);
    }
}
