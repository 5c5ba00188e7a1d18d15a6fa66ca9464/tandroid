package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class T extends CountedCompleter {
    private j$.util.Q a;
    private final e2 b;
    private final b c;
    private long d;

    T(T t, j$.util.Q q) {
        super(t);
        this.a = q;
        this.b = t.b;
        this.d = t.d;
        this.c = t.c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public T(b bVar, j$.util.Q q, e2 e2Var) {
        super(null);
        this.b = e2Var;
        this.c = bVar;
        this.a = q;
        this.d = 0L;
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void compute() {
        j$.util.Q trySplit;
        j$.util.Q q = this.a;
        long estimateSize = q.estimateSize();
        long j = this.d;
        if (j == 0) {
            j = e.f(estimateSize);
            this.d = j;
        }
        boolean d = S2.SHORT_CIRCUIT.d(this.c.s0());
        e2 e2Var = this.b;
        boolean z = false;
        T t = this;
        while (true) {
            if (d && e2Var.q()) {
                break;
            } else if (estimateSize <= j || (trySplit = q.trySplit()) == null) {
                break;
            } else {
                T t2 = new T(t, trySplit);
                t.addToPendingCount(1);
                if (z) {
                    q = trySplit;
                } else {
                    T t3 = t;
                    t = t2;
                    t2 = t3;
                }
                z = !z;
                t.fork();
                t = t2;
                estimateSize = q.estimateSize();
            }
        }
        t.c.i0(q, e2Var);
        t.a = null;
        t.propagateCompletion();
    }
}
