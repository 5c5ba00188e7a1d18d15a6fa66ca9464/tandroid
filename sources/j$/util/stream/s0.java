package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class s0 extends CountedCompleter {
    private j$.util.s a;
    private final m3 b;
    private final y2 c;
    private long d;

    s0(s0 s0Var, j$.util.s sVar) {
        super(s0Var);
        this.a = sVar;
        this.b = s0Var.b;
        this.d = s0Var.d;
        this.c = s0Var.c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public s0(y2 y2Var, j$.util.s sVar, m3 m3Var) {
        super(null);
        this.b = m3Var;
        this.c = y2Var;
        this.a = sVar;
        this.d = 0L;
    }

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        j$.util.s trySplit;
        j$.util.s sVar = this.a;
        long estimateSize = sVar.estimateSize();
        long j = this.d;
        if (j == 0) {
            j = f.h(estimateSize);
            this.d = j;
        }
        boolean d = d4.SHORT_CIRCUIT.d(this.c.n0());
        boolean z = false;
        m3 m3Var = this.b;
        s0 s0Var = this;
        while (true) {
            if (d && m3Var.o()) {
                break;
            } else if (estimateSize <= j || (trySplit = sVar.trySplit()) == null) {
                break;
            } else {
                s0 s0Var2 = new s0(s0Var, trySplit);
                s0Var.addToPendingCount(1);
                if (z) {
                    sVar = trySplit;
                } else {
                    s0 s0Var3 = s0Var;
                    s0Var = s0Var2;
                    s0Var2 = s0Var3;
                }
                z = !z;
                s0Var.fork();
                s0Var = s0Var2;
                estimateSize = sVar.estimateSize();
            }
        }
        s0Var.c.i0(m3Var, sVar);
        s0Var.a = null;
        s0Var.propagateCompletion();
    }
}
