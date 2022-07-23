package j$.util.stream;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
/* loaded from: classes2.dex */
abstract class f extends CountedCompleter {
    static final int g = ForkJoinPool.getCommonPoolParallelism() << 2;
    protected final y2 a;
    protected j$.util.u b;
    protected long c;
    protected f d;
    protected f e;
    private Object f;

    public f(f fVar, j$.util.u uVar) {
        super(fVar);
        this.b = uVar;
        this.a = fVar.a;
        this.c = fVar.c;
    }

    public f(y2 y2Var, j$.util.u uVar) {
        super(null);
        this.a = y2Var;
        this.b = uVar;
        this.c = 0L;
    }

    public static long h(long j) {
        long j2 = j / g;
        if (j2 > 0) {
            return j2;
        }
        return 1L;
    }

    public abstract Object a();

    public Object b() {
        return this.f;
    }

    public f c() {
        return (f) getCompleter();
    }

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        j$.util.u trySplit;
        j$.util.u uVar = this.b;
        long estimateSize = uVar.estimateSize();
        long j = this.c;
        if (j == 0) {
            j = h(estimateSize);
            this.c = j;
        }
        boolean z = false;
        f fVar = this;
        while (estimateSize > j && (trySplit = uVar.trySplit()) != null) {
            f f = fVar.f(trySplit);
            fVar.d = f;
            f f2 = fVar.f(uVar);
            fVar.e = f2;
            fVar.setPendingCount(1);
            if (z) {
                uVar = trySplit;
                fVar = f;
                f = f2;
            } else {
                fVar = f2;
            }
            z = !z;
            f.fork();
            estimateSize = uVar.estimateSize();
        }
        fVar.g(fVar.a());
        fVar.tryComplete();
    }

    public boolean d() {
        return this.d == null;
    }

    public boolean e() {
        return c() == null;
    }

    public abstract f f(j$.util.u uVar);

    public void g(Object obj) {
        this.f = obj;
    }

    @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public Object getRawResult() {
        return this.f;
    }

    @Override // java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter countedCompleter) {
        this.b = null;
        this.e = null;
        this.d = null;
    }

    @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    protected void setRawResult(Object obj) {
        if (obj == null) {
            return;
        }
        throw new IllegalStateException();
    }
}
