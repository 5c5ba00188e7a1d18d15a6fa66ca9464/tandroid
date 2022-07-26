package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class r0 extends CountedCompleter {
    private j$.util.u a;
    private final m3 b;
    private final y2 c;
    private long d;

    r0(r0 r0Var, j$.util.u uVar) {
        super(r0Var);
        this.a = uVar;
        this.b = r0Var.b;
        this.d = r0Var.d;
        this.c = r0Var.c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public r0(y2 y2Var, j$.util.u uVar, m3 m3Var) {
        super(null);
        this.b = m3Var;
        this.c = y2Var;
        this.a = uVar;
        this.d = 0L;
    }

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        j$.util.u trySplit;
        j$.util.u uVar = this.a;
        long estimateSize = uVar.estimateSize();
        long j = this.d;
        if (j == 0) {
            j = f.h(estimateSize);
            this.d = j;
        }
        boolean d = d4.SHORT_CIRCUIT.d(this.c.s0());
        boolean z = false;
        m3 m3Var = this.b;
        r0 r0Var = this;
        while (true) {
            if (d && m3Var.o()) {
                break;
            } else if (estimateSize <= j || (trySplit = uVar.trySplit()) == null) {
                break;
            } else {
                r0 r0Var2 = new r0(r0Var, trySplit);
                r0Var.addToPendingCount(1);
                if (z) {
                    uVar = trySplit;
                } else {
                    r0 r0Var3 = r0Var;
                    r0Var = r0Var2;
                    r0Var2 = r0Var3;
                }
                z = !z;
                r0Var.fork();
                r0Var = r0Var2;
                estimateSize = uVar.estimateSize();
            }
        }
        r0Var.c.n0(m3Var, uVar);
        r0Var.a = null;
        r0Var.propagateCompletion();
    }
}
