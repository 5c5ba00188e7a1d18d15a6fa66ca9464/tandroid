package j$.util.stream;

import java.util.concurrent.atomic.AtomicLong;
/* loaded from: classes2.dex */
abstract class y3 {
    protected final j$.util.Q a;
    protected final boolean b;
    private final long c;
    private final AtomicLong d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y3(j$.util.Q q, long j, long j2) {
        this.a = q;
        this.b = j2 < 0;
        this.c = j2 >= 0 ? j2 : 0L;
        this.d = new AtomicLong(j2 >= 0 ? j + j2 : j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public y3(j$.util.Q q, y3 y3Var) {
        this.a = q;
        this.b = y3Var.b;
        this.d = y3Var.d;
        this.c = y3Var.c;
    }

    public final int characteristics() {
        return this.a.characteristics() & (-16465);
    }

    public final long estimateSize() {
        return this.a.estimateSize();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final long q(long j) {
        AtomicLong atomicLong;
        long j2;
        boolean z;
        long min;
        do {
            atomicLong = this.d;
            j2 = atomicLong.get();
            z = this.b;
            if (j2 != 0) {
                min = Math.min(j2, j);
                if (min <= 0) {
                    break;
                }
            } else if (z) {
                return j;
            } else {
                return 0L;
            }
        } while (!atomicLong.compareAndSet(j2, j2 - min));
        if (z) {
            return Math.max(j - min, 0L);
        }
        long j3 = this.c;
        return j2 > j3 ? Math.max(min - (j2 - j3), 0L) : min;
    }

    protected abstract j$.util.Q r(j$.util.Q q);

    /* JADX INFO: Access modifiers changed from: protected */
    public final x3 s() {
        return this.d.get() > 0 ? x3.MAYBE_MORE : this.b ? x3.UNLIMITED : x3.NO_MORE;
    }

    public /* bridge */ /* synthetic */ j$.util.E trySplit() {
        return (j$.util.E) trySplit();
    }

    public /* bridge */ /* synthetic */ j$.util.H trySplit() {
        return (j$.util.H) trySplit();
    }

    public /* bridge */ /* synthetic */ j$.util.K trySplit() {
        return (j$.util.K) trySplit();
    }

    public /* bridge */ /* synthetic */ j$.util.N trySplit() {
        return (j$.util.N) trySplit();
    }

    public final j$.util.Q trySplit() {
        j$.util.Q trySplit;
        if (this.d.get() == 0 || (trySplit = this.a.trySplit()) == null) {
            return null;
        }
        return r(trySplit);
    }
}
