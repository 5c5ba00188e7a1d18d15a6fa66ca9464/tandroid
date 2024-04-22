package j$.util.stream;

import j$.util.s;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: classes2.dex */
abstract class J4 {
    protected final j$.util.s a;
    protected final boolean b;
    private final long c;
    private final AtomicLong d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public J4(j$.util.s sVar, long j, long j2) {
        this.a = sVar;
        int i = (j2 > 0L ? 1 : (j2 == 0L ? 0 : -1));
        this.b = i < 0;
        this.c = i >= 0 ? j2 : 0L;
        this.d = new AtomicLong(i >= 0 ? j + j2 : j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public J4(j$.util.s sVar, J4 j4) {
        this.a = sVar;
        this.b = j4.b;
        this.d = j4.d;
        this.c = j4.c;
    }

    public final int characteristics() {
        return this.a.characteristics() & (-16465);
    }

    public final long estimateSize() {
        return this.a.estimateSize();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final long p(long j) {
        long j2;
        long min;
        do {
            j2 = this.d.get();
            if (j2 != 0) {
                min = Math.min(j2, j);
                if (min <= 0) {
                    break;
                }
            } else if (this.b) {
                return j;
            } else {
                return 0L;
            }
        } while (!this.d.compareAndSet(j2, j2 - min));
        if (this.b) {
            return Math.max(j - min, 0L);
        }
        long j3 = this.c;
        return j2 > j3 ? Math.max(min - (j2 - j3), 0L) : min;
    }

    protected abstract j$.util.s q(j$.util.s sVar);

    /* JADX INFO: Access modifiers changed from: protected */
    public final int r() {
        if (this.d.get() > 0) {
            return 2;
        }
        return this.b ? 3 : 1;
    }

    public /* bridge */ /* synthetic */ s.a trySplit() {
        return (s.a) trySplit();
    }

    public /* bridge */ /* synthetic */ s.b trySplit() {
        return (s.b) trySplit();
    }

    public /* bridge */ /* synthetic */ s.c trySplit() {
        return (s.c) trySplit();
    }

    public final j$.util.s trySplit() {
        j$.util.s trySplit;
        if (this.d.get() == 0 || (trySplit = this.a.trySplit()) == null) {
            return null;
        }
        return q(trySplit);
    }

    public /* bridge */ /* synthetic */ j$.util.t trySplit() {
        return (j$.util.t) trySplit();
    }
}
