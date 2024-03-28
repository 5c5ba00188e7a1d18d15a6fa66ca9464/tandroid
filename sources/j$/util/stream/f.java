package j$.util.stream;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
/* loaded from: classes2.dex */
abstract class f extends CountedCompleter {
    static final int g = ForkJoinPool.getCommonPoolParallelism() << 2;
    protected final z2 a;
    protected j$.util.t b;
    protected long c;
    protected f d;
    protected f e;
    private Object f;

    /* JADX INFO: Access modifiers changed from: protected */
    public f(f fVar, j$.util.t tVar) {
        super(fVar);
        this.b = tVar;
        this.a = fVar.a;
        this.c = fVar.c;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public f(z2 z2Var, j$.util.t tVar) {
        super(null);
        this.a = z2Var;
        this.b = tVar;
        this.c = 0L;
    }

    public static long h(long j) {
        long j2 = j / g;
        if (j2 > 0) {
            return j2;
        }
        return 1L;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Object a();

    /* JADX INFO: Access modifiers changed from: protected */
    public Object b() {
        return this.f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public f c() {
        return (f) getCompleter();
    }

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        j$.util.t trySplit;
        j$.util.t tVar = this.b;
        long estimateSize = tVar.estimateSize();
        long j = this.c;
        if (j == 0) {
            j = h(estimateSize);
            this.c = j;
        }
        boolean z = false;
        f fVar = this;
        while (estimateSize > j && (trySplit = tVar.trySplit()) != null) {
            f f = fVar.f(trySplit);
            fVar.d = f;
            f f2 = fVar.f(tVar);
            fVar.e = f2;
            fVar.setPendingCount(1);
            if (z) {
                tVar = trySplit;
                fVar = f;
                f = f2;
            } else {
                fVar = f2;
            }
            z = !z;
            f.fork();
            estimateSize = tVar.estimateSize();
        }
        fVar.g(fVar.a());
        fVar.tryComplete();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean d() {
        return this.d == null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean e() {
        return c() == null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract f f(j$.util.t tVar);

    /* JADX INFO: Access modifiers changed from: protected */
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
        if (obj != null) {
            throw new IllegalStateException();
        }
    }
}
