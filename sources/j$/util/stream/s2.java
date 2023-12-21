package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
abstract class s2 extends CountedCompleter implements m3 {
    protected final j$.util.t a;
    protected final y2 b;
    protected final long c;
    protected long d;
    protected long e;
    protected int f;
    protected int g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s2(s2 s2Var, j$.util.t tVar, long j, long j2, int i) {
        super(s2Var);
        this.a = tVar;
        this.b = s2Var.b;
        this.c = s2Var.c;
        this.d = j;
        this.e = j2;
        if (j < 0 || j2 < 0 || (j + j2) - 1 >= i) {
            throw new IllegalArgumentException(String.format("offset and length interval [%d, %d + %d) is not within array size interval [0, %d)", Long.valueOf(j), Long.valueOf(j), Long.valueOf(j2), Integer.valueOf(i)));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public s2(j$.util.t tVar, y2 y2Var, int i) {
        this.a = tVar;
        this.b = y2Var;
        this.c = f.h(tVar.estimateSize());
        this.d = 0L;
        this.e = i;
    }

    public /* synthetic */ void accept(double d) {
        o1.f(this);
        throw null;
    }

    public /* synthetic */ void accept(int i) {
        o1.d(this);
        throw null;
    }

    public /* synthetic */ void accept(long j) {
        o1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Objects.requireNonNull(consumer);
    }

    abstract s2 b(j$.util.t tVar, long j, long j2);

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        j$.util.t trySplit;
        j$.util.t tVar = this.a;
        s2 s2Var = this;
        while (tVar.estimateSize() > s2Var.c && (trySplit = tVar.trySplit()) != null) {
            s2Var.setPendingCount(1);
            long estimateSize = trySplit.estimateSize();
            s2Var.b(trySplit, s2Var.d, estimateSize).fork();
            s2Var = s2Var.b(tVar, s2Var.d + estimateSize, s2Var.e - estimateSize);
        }
        c cVar = (c) s2Var.b;
        Objects.requireNonNull(cVar);
        cVar.n0(cVar.v0(s2Var), tVar);
        s2Var.propagateCompletion();
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        long j2 = this.e;
        if (j > j2) {
            throw new IllegalStateException("size passed to Sink.begin exceeds array length");
        }
        int i = (int) this.d;
        this.f = i;
        this.g = i + ((int) j2);
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ boolean o() {
        return false;
    }
}
