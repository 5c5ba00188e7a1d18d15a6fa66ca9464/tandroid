package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
abstract class t2 extends CountedCompleter implements n3 {
    protected final j$.util.t a;
    protected final z2 b;
    protected final long c;
    protected long d;
    protected long e;
    protected int f;
    protected int g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t2(t2 t2Var, j$.util.t tVar, long j, long j2, int i) {
        super(t2Var);
        this.a = tVar;
        this.b = t2Var.b;
        this.c = t2Var.c;
        this.d = j;
        this.e = j2;
        if (j < 0 || j2 < 0 || (j + j2) - 1 >= i) {
            throw new IllegalArgumentException(String.format("offset and length interval [%d, %d + %d) is not within array size interval [0, %d)", Long.valueOf(j), Long.valueOf(j), Long.valueOf(j2), Integer.valueOf(i)));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public t2(j$.util.t tVar, z2 z2Var, int i) {
        this.a = tVar;
        this.b = z2Var;
        this.c = f.h(tVar.estimateSize());
        this.d = 0L;
        this.e = i;
    }

    public /* synthetic */ void accept(double d) {
        p1.f(this);
        throw null;
    }

    public /* synthetic */ void accept(int i) {
        p1.d(this);
        throw null;
    }

    public /* synthetic */ void accept(long j) {
        p1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    abstract t2 b(j$.util.t tVar, long j, long j2);

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        j$.util.t trySplit;
        j$.util.t tVar = this.a;
        t2 t2Var = this;
        while (tVar.estimateSize() > t2Var.c && (trySplit = tVar.trySplit()) != null) {
            t2Var.setPendingCount(1);
            long estimateSize = trySplit.estimateSize();
            t2Var.b(trySplit, t2Var.d, estimateSize).fork();
            t2Var = t2Var.b(tVar, t2Var.d + estimateSize, t2Var.e - estimateSize);
        }
        c cVar = (c) t2Var.b;
        Objects.requireNonNull(cVar);
        cVar.l0(cVar.t0(t2Var), tVar);
        t2Var.propagateCompletion();
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.n3
    public void n(long j) {
        long j2 = this.e;
        if (j > j2) {
            throw new IllegalStateException("size passed to Sink.begin exceeds array length");
        }
        int i = (int) this.d;
        this.f = i;
        this.g = i + ((int) j2);
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ boolean o() {
        return false;
    }
}
