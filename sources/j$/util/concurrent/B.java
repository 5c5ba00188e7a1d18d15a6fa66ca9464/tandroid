package j$.util.concurrent;

import j$.util.K;
import j$.util.function.Consumer;
import j$.util.function.h0;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class B implements K {
    long a;
    final long b;
    final long c;
    final long d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public B(long j, long j2, long j3, long j4) {
        this.a = j;
        this.b = j2;
        this.c = j3;
        this.d = j4;
    }

    @Override // j$.util.K, j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.q(this, consumer);
    }

    @Override // j$.util.N
    /* renamed from: b */
    public final void forEachRemaining(h0 h0Var) {
        h0Var.getClass();
        long j = this.a;
        long j2 = this.b;
        if (j < j2) {
            this.a = j2;
            ThreadLocalRandom current = ThreadLocalRandom.current();
            do {
                h0Var.accept(current.e(this.c, this.d));
                j++;
            } while (j < j2);
        }
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return 17728;
    }

    @Override // j$.util.N
    /* renamed from: e */
    public final boolean tryAdvance(h0 h0Var) {
        h0Var.getClass();
        long j = this.a;
        if (j < this.b) {
            h0Var.accept(ThreadLocalRandom.current().e(this.c, this.d));
            this.a = j + 1;
            return true;
        }
        return false;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        return this.b - this.a;
    }

    @Override // j$.util.Q
    /* renamed from: f */
    public final B trySplit() {
        long j = this.a;
        long j2 = (this.b + j) >>> 1;
        if (j2 <= j) {
            return null;
        }
        this.a = j2;
        return new B(j, j2, this.c, this.d);
    }

    @Override // j$.util.K, j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.h(this, consumer);
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.i(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.k(this, i);
    }
}
