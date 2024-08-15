package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class U extends CountedCompleter {
    private j$.util.Q a;
    private final f2 b;
    private final u0 c;
    private long d;

    U(U u, j$.util.Q q) {
        super(u);
        this.a = q;
        this.b = u.b;
        this.d = u.d;
        this.c = u.c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public U(u0 u0Var, j$.util.Q q, f2 f2Var) {
        super(null);
        this.b = f2Var;
        this.c = u0Var;
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
            j = f.f(estimateSize);
            this.d = j;
        }
        boolean d = T2.SHORT_CIRCUIT.d(this.c.K0());
        f2 f2Var = this.b;
        boolean z = false;
        U u = this;
        while (true) {
            if (d && f2Var.h()) {
                break;
            } else if (estimateSize <= j || (trySplit = q.trySplit()) == null) {
                break;
            } else {
                U u2 = new U(u, trySplit);
                u.addToPendingCount(1);
                if (z) {
                    q = trySplit;
                } else {
                    U u3 = u;
                    u = u2;
                    u2 = u3;
                }
                z = !z;
                u.fork();
                u = u2;
                estimateSize = q.estimateSize();
            }
        }
        u.c.F0(q, f2Var);
        u.a = null;
        u.propagateCompletion();
    }
}
