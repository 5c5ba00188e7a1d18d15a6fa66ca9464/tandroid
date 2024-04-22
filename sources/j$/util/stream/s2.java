package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
abstract class s2 extends CountedCompleter implements m3 {
    protected final j$.util.s a;
    protected final y2 b;
    protected final long c;
    protected long d;
    protected long e;
    protected int f;
    protected int g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s2(j$.util.s sVar, y2 y2Var, int i) {
        this.a = sVar;
        this.b = y2Var;
        this.c = f.h(sVar.estimateSize());
        this.d = 0L;
        this.e = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public s2(s2 s2Var, j$.util.s sVar, long j, long j2, int i) {
        super(s2Var);
        this.a = sVar;
        this.b = s2Var.b;
        this.c = s2Var.c;
        this.d = j;
        this.e = j2;
        if (j < 0 || j2 < 0 || (j + j2) - 1 >= i) {
            throw new IllegalArgumentException(String.format("offset and length interval [%d, %d + %d) is not within array size interval [0, %d)", Long.valueOf(j), Long.valueOf(j), Long.valueOf(j2), Integer.valueOf(i)));
        }
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
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    abstract s2 b(j$.util.s sVar, long j, long j2);

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        j$.util.s trySplit;
        j$.util.s sVar = this.a;
        s2 s2Var = this;
        while (sVar.estimateSize() > s2Var.c && (trySplit = sVar.trySplit()) != null) {
            s2Var.setPendingCount(1);
            long estimateSize = trySplit.estimateSize();
            s2Var.b(trySplit, s2Var.d, estimateSize).fork();
            s2Var = s2Var.b(sVar, s2Var.d + estimateSize, s2Var.e - estimateSize);
        }
        c cVar = (c) s2Var.b;
        Objects.requireNonNull(cVar);
        cVar.i0(cVar.q0(s2Var), sVar);
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
