package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class s0 extends CountedCompleter {
    private j$.util.t a;
    private final n3 b;
    private final z2 c;
    private long d;

    s0(s0 s0Var, j$.util.t tVar) {
        super(s0Var);
        this.a = tVar;
        this.b = s0Var.b;
        this.d = s0Var.d;
        this.c = s0Var.c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public s0(z2 z2Var, j$.util.t tVar, n3 n3Var) {
        super(null);
        this.b = n3Var;
        this.c = z2Var;
        this.a = tVar;
        this.d = 0L;
    }

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        j$.util.t trySplit;
        j$.util.t tVar = this.a;
        long estimateSize = tVar.estimateSize();
        long j = this.d;
        if (j == 0) {
            j = f.h(estimateSize);
            this.d = j;
        }
        boolean d = e4.SHORT_CIRCUIT.d(this.c.q0());
        boolean z = false;
        n3 n3Var = this.b;
        s0 s0Var = this;
        while (true) {
            if (d && n3Var.o()) {
                break;
            } else if (estimateSize <= j || (trySplit = tVar.trySplit()) == null) {
                break;
            } else {
                s0 s0Var2 = new s0(s0Var, trySplit);
                s0Var.addToPendingCount(1);
                if (z) {
                    tVar = trySplit;
                } else {
                    s0 s0Var3 = s0Var;
                    s0Var = s0Var2;
                    s0Var2 = s0Var3;
                }
                z = !z;
                s0Var.fork();
                s0Var = s0Var2;
                estimateSize = tVar.estimateSize();
            }
        }
        s0Var.c.l0(n3Var, tVar);
        s0Var.a = null;
        s0Var.propagateCompletion();
    }
}
