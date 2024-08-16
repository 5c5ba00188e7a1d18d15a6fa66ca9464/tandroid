package j$.util.concurrent;

import j$.util.K;
import j$.util.function.Consumer;
import j$.util.function.W;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class z implements K {
    long a;
    final long b;
    final long c;
    final long d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public z(long j, long j2, long j3, long j4) {
        this.a = j;
        this.b = j2;
        this.c = j3;
        this.d = j4;
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        j$.util.a.h(this, consumer);
    }

    @Override // j$.util.Q
    /* renamed from: b */
    public final z trySplit() {
        long j = this.a;
        long j2 = (this.b + j) >>> 1;
        if (j2 <= j) {
            return null;
        }
        this.a = j2;
        return new z(j, j2, this.c, this.d);
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return 17728;
    }

    @Override // j$.util.N
    /* renamed from: d */
    public final void forEachRemaining(W w) {
        w.getClass();
        long j = this.a;
        long j2 = this.b;
        if (j < j2) {
            this.a = j2;
            ThreadLocalRandom current = ThreadLocalRandom.current();
            do {
                w.accept(current.e(this.c, this.d));
                j++;
            } while (j < j2);
        }
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        return this.b - this.a;
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
    /* renamed from: i */
    public final boolean tryAdvance(W w) {
        w.getClass();
        long j = this.a;
        if (j < this.b) {
            w.accept(ThreadLocalRandom.current().e(this.c, this.d));
            this.a = j + 1;
            return true;
        }
        return false;
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return j$.util.a.q(this, consumer);
    }
}
