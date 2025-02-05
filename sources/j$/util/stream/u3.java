package j$.util.stream;

import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes2.dex */
abstract class u3 {
    protected final j$.util.Q a;
    protected final boolean b;
    private final long c;
    private final AtomicLong d;

    u3(j$.util.Q q, long j, long j2) {
        this.a = q;
        this.b = j2 < 0;
        this.c = j2 >= 0 ? j2 : 0L;
        this.d = new AtomicLong(j2 >= 0 ? j + j2 : j);
    }

    u3(j$.util.Q q, u3 u3Var) {
        this.a = q;
        this.b = u3Var.b;
        this.d = u3Var.d;
        this.c = u3Var.c;
    }

    public final int characteristics() {
        return this.a.characteristics() & (-16465);
    }

    public final long estimateSize() {
        return this.a.estimateSize();
    }

    protected final long t(long j) {
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
            } else {
                if (z) {
                    return j;
                }
                return 0L;
            }
        } while (!atomicLong.compareAndSet(j2, j2 - min));
        if (z) {
            return Math.max(j - min, 0L);
        }
        long j3 = this.c;
        return j2 > j3 ? Math.max(min - (j2 - j3), 0L) : min;
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
        return u(trySplit);
    }

    protected abstract j$.util.Q u(j$.util.Q q);

    protected final t3 v() {
        return this.d.get() > 0 ? t3.MAYBE_MORE : this.b ? t3.UNLIMITED : t3.NO_MORE;
    }
}
