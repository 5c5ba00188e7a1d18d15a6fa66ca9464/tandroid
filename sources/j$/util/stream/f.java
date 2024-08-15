package j$.util.stream;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class f extends CountedCompleter {
    static final int g = ForkJoinPool.getCommonPoolParallelism() << 2;
    protected final u0 a;
    protected j$.util.Q b;
    protected long c;
    protected f d;
    protected f e;
    private Object f;

    /* JADX INFO: Access modifiers changed from: protected */
    public f(f fVar, j$.util.Q q) {
        super(fVar);
        this.b = q;
        this.a = fVar.a;
        this.c = fVar.c;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public f(u0 u0Var, j$.util.Q q) {
        super(null);
        this.a = u0Var;
        this.b = q;
        this.c = 0L;
    }

    public static long f(long j) {
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
    public final f c() {
        return (f) getCompleter();
    }

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        j$.util.Q trySplit;
        j$.util.Q q = this.b;
        long estimateSize = q.estimateSize();
        long j = this.c;
        if (j == 0) {
            j = f(estimateSize);
            this.c = j;
        }
        boolean z = false;
        f fVar = this;
        while (estimateSize > j && (trySplit = q.trySplit()) != null) {
            f d = fVar.d(trySplit);
            fVar.d = d;
            f d2 = fVar.d(q);
            fVar.e = d2;
            fVar.setPendingCount(1);
            if (z) {
                q = trySplit;
                fVar = d;
                d = d2;
            } else {
                fVar = d2;
            }
            z = !z;
            d.fork();
            estimateSize = q.estimateSize();
        }
        fVar.e(fVar.a());
        fVar.tryComplete();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract f d(j$.util.Q q);

    /* JADX INFO: Access modifiers changed from: protected */
    public void e(Object obj) {
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
    protected final void setRawResult(Object obj) {
        if (obj != null) {
            throw new IllegalStateException();
        }
    }
}
