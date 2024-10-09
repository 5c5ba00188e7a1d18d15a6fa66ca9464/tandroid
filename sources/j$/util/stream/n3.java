package j$.util.stream;

/* loaded from: classes2.dex */
abstract class n3 {
    final long a;
    final long b;
    j$.util.Q c;
    long d;
    long e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n3(j$.util.Q q, long j, long j2, long j3, long j4) {
        this.c = q;
        this.a = j;
        this.b = j2;
        this.d = j3;
        this.e = j4;
    }

    protected abstract j$.util.Q b(j$.util.Q q, long j, long j2, long j3, long j4);

    public final int characteristics() {
        return this.c.characteristics();
    }

    public final long estimateSize() {
        long j = this.e;
        long j2 = this.a;
        if (j2 < j) {
            return j - Math.max(j2, this.d);
        }
        return 0L;
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
        long j = this.e;
        if (this.a >= j || this.d >= j) {
            return null;
        }
        while (true) {
            j$.util.Q trySplit = this.c.trySplit();
            if (trySplit == null) {
                return null;
            }
            long estimateSize = trySplit.estimateSize() + this.d;
            long min = Math.min(estimateSize, this.b);
            long j2 = this.a;
            if (j2 >= min) {
                this.d = min;
            } else {
                long j3 = this.b;
                if (min < j3) {
                    long j4 = this.d;
                    if (j4 < j2 || estimateSize > j3) {
                        this.d = min;
                        return b(trySplit, j2, j3, j4, min);
                    }
                    this.d = min;
                    return trySplit;
                }
                this.c = trySplit;
                this.e = min;
            }
        }
    }
}
