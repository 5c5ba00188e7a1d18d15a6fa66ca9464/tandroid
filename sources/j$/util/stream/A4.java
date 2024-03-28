package j$.util.stream;

import java.util.Comparator;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class A4 extends E4 implements j$.util.u {
    /* JADX INFO: Access modifiers changed from: package-private */
    public A4(j$.util.u uVar, long j, long j2) {
        super(uVar, j, j2, 0L, Math.min(uVar.estimateSize(), j2));
    }

    protected abstract Object f();

    @Override // j$.util.u
    /* renamed from: forEachRemaining */
    public void e(Object obj) {
        Objects.requireNonNull(obj);
        long j = this.a;
        long j2 = this.e;
        if (j >= j2) {
            return;
        }
        long j3 = this.d;
        if (j3 >= j2) {
            return;
        }
        if (j3 >= j && ((j$.util.u) this.c).estimateSize() + j3 <= this.b) {
            ((j$.util.u) this.c).forEachRemaining(obj);
            this.d = this.e;
            return;
        }
        while (this.a > this.d) {
            ((j$.util.u) this.c).tryAdvance(f());
            this.d++;
        }
        while (this.d < this.e) {
            ((j$.util.u) this.c).tryAdvance(obj);
            this.d++;
        }
    }

    @Override // j$.util.t
    public Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.t
    public /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.e(this);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.f(this, i);
    }

    @Override // j$.util.u
    /* renamed from: tryAdvance */
    public boolean k(Object obj) {
        long j;
        Objects.requireNonNull(obj);
        if (this.a >= this.e) {
            return false;
        }
        while (true) {
            long j2 = this.a;
            j = this.d;
            if (j2 <= j) {
                break;
            }
            ((j$.util.u) this.c).tryAdvance(f());
            this.d++;
        }
        if (j >= this.e) {
            return false;
        }
        this.d = j + 1;
        return ((j$.util.u) this.c).tryAdvance(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public A4(j$.util.u uVar, long j, long j2, long j3, long j4, p1 p1Var) {
        super(uVar, j, j2, j3, j4);
    }
}
