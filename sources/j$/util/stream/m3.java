package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class m3 extends n3 implements j$.util.Q {
    /* JADX INFO: Access modifiers changed from: package-private */
    public m3(j$.util.Q q, long j, long j2) {
        super(q, j, j2, 0L, Math.min(q.estimateSize(), j2));
    }

    private m3(j$.util.Q q, long j, long j2, long j3, long j4) {
        super(q, j, j2, j3, j4);
    }

    @Override // j$.util.Q
    public final void a(Consumer consumer) {
        consumer.getClass();
        long j = this.e;
        long j2 = this.a;
        if (j2 >= j) {
            return;
        }
        long j3 = this.d;
        if (j3 >= j) {
            return;
        }
        if (j3 >= j2 && this.c.estimateSize() + j3 <= this.b) {
            this.c.a(consumer);
            this.d = this.e;
            return;
        }
        while (j2 > this.d) {
            this.c.s(new Q1(7));
            this.d++;
        }
        while (this.d < this.e) {
            this.c.s(consumer);
            this.d++;
        }
    }

    @Override // j$.util.stream.n3
    protected final j$.util.Q b(j$.util.Q q, long j, long j2, long j3, long j4) {
        return new m3(q, j, j2, j3, j4);
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

    @Override // j$.util.Q
    public final boolean s(Consumer consumer) {
        long j;
        consumer.getClass();
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
            this.c.s(new Q1(6));
            this.d++;
        }
        if (j >= this.e) {
            return false;
        }
        this.d = j + 1;
        return this.c.s(consumer);
    }
}
