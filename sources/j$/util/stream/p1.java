package j$.util.stream;

import j$.util.function.Consumer;
import java.util.concurrent.CountedCompleter;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class p1 extends CountedCompleter implements f2 {
    protected final j$.util.Q a;
    protected final u0 b;
    protected final long c;
    protected long d;
    protected long e;
    protected int f;
    protected int g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p1(int i, j$.util.Q q, u0 u0Var) {
        this.a = q;
        this.b = u0Var;
        this.c = f.f(q.estimateSize());
        this.d = 0L;
        this.e = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p1(p1 p1Var, j$.util.Q q, long j, long j2, int i) {
        super(p1Var);
        this.a = q;
        this.b = p1Var.b;
        this.c = p1Var.c;
        this.d = j;
        this.e = j2;
        if (j < 0 || j2 < 0 || (j + j2) - 1 >= i) {
            throw new IllegalArgumentException(String.format("offset and length interval [%d, %d + %d) is not within array size interval [0, %d)", Long.valueOf(j), Long.valueOf(j), Long.valueOf(j2), Integer.valueOf(i)));
        }
    }

    abstract p1 a(j$.util.Q q, long j, long j2);

    public /* synthetic */ void accept(double d) {
        u0.i0();
        throw null;
    }

    public /* synthetic */ void accept(int i) {
        u0.p0();
        throw null;
    }

    public /* synthetic */ void accept(long j) {
        u0.q0();
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
        p1 p1Var = this;
        while (q.estimateSize() > p1Var.c && (trySplit = q.trySplit()) != null) {
            p1Var.setPendingCount(1);
            long estimateSize = trySplit.estimateSize();
            p1Var.a(trySplit, p1Var.d, estimateSize).fork();
            p1Var = p1Var.a(q, p1Var.d + estimateSize, p1Var.e - estimateSize);
        }
        p1Var.b.X0(q, p1Var);
        p1Var.propagateCompletion();
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void end() {
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        long j2 = this.e;
        if (j > j2) {
            throw new IllegalStateException("size passed to Sink.begin exceeds array length");
        }
        int i = (int) this.d;
        this.f = i;
        this.g = i + ((int) j2);
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ boolean h() {
        return false;
    }
}
