package j$.util.stream;

import j$.util.function.Consumer;
import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
abstract class q1 extends CountedCompleter implements e2 {
    protected final j$.util.Q a;
    protected final b b;
    protected final long c;
    protected long d;
    protected long e;
    protected int f;
    protected int g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q1(int i, j$.util.Q q, b bVar) {
        this.a = q;
        this.b = bVar;
        this.c = e.f(q.estimateSize());
        this.d = 0L;
        this.e = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public q1(q1 q1Var, j$.util.Q q, long j, long j2, int i) {
        super(q1Var);
        this.a = q;
        this.b = q1Var.b;
        this.c = q1Var.c;
        this.d = j;
        this.e = j2;
        if (j < 0 || j2 < 0 || (j + j2) - 1 >= i) {
            throw new IllegalArgumentException(String.format("offset and length interval [%d, %d + %d) is not within array size interval [0, %d)", Long.valueOf(j), Long.valueOf(j), Long.valueOf(j2), Integer.valueOf(i)));
        }
    }

    abstract q1 a(j$.util.Q q, long j, long j2);

    public /* synthetic */ void accept(double d) {
        t0.b();
        throw null;
    }

    public /* synthetic */ void accept(int i) {
        t0.k();
        throw null;
    }

    public /* synthetic */ void accept(long j) {
        t0.l();
        throw null;
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void compute() {
        j$.util.Q trySplit;
        j$.util.Q q = this.a;
        q1 q1Var = this;
        while (q.estimateSize() > q1Var.c && (trySplit = q.trySplit()) != null) {
            q1Var.setPendingCount(1);
            long estimateSize = trySplit.estimateSize();
            q1Var.a(trySplit, q1Var.d, estimateSize).fork();
            q1Var = q1Var.a(q, q1Var.d + estimateSize, q1Var.e - estimateSize);
        }
        q1Var.b.D0(q, q1Var);
        q1Var.propagateCompletion();
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void m() {
    }

    @Override // j$.util.stream.e2
    public final void n(long j) {
        long j2 = this.e;
        if (j > j2) {
            throw new IllegalStateException("size passed to Sink.begin exceeds array length");
        }
        int i = (int) this.d;
        this.f = i;
        this.g = i + ((int) j2);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        return false;
    }
}
