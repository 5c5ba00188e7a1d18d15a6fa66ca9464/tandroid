package j$.util.stream;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class e extends CountedCompleter {
    static final int g = ForkJoinPool.getCommonPoolParallelism() << 2;
    protected final b a;
    protected j$.util.Q b;
    protected long c;
    protected e d;
    protected e e;
    private Object f;

    /* JADX INFO: Access modifiers changed from: protected */
    public e(b bVar, j$.util.Q q) {
        super(null);
        this.a = bVar;
        this.b = q;
        this.c = 0L;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public e(e eVar, j$.util.Q q) {
        super(eVar);
        this.b = q;
        this.a = eVar.a;
        this.c = eVar.c;
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
    public final boolean c() {
        return ((e) getCompleter()) == null;
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
        e eVar = this;
        while (estimateSize > j && (trySplit = q.trySplit()) != null) {
            e d = eVar.d(trySplit);
            eVar.d = d;
            e d2 = eVar.d(q);
            eVar.e = d2;
            eVar.setPendingCount(1);
            if (z) {
                q = trySplit;
                eVar = d;
                d = d2;
            } else {
                eVar = d2;
            }
            z = !z;
            d.fork();
            estimateSize = q.estimateSize();
        }
        eVar.e(eVar.a());
        eVar.tryComplete();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract e d(j$.util.Q q);

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
