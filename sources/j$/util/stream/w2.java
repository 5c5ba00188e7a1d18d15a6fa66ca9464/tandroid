package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
abstract class w2 extends CountedCompleter {
    protected final A1 a;
    protected final int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w2(A1 a1, int i) {
        this.a = a1;
        this.b = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public w2(w2 w2Var, A1 a1, int i) {
        super(w2Var);
        this.a = a1;
        this.b = i;
    }

    abstract void a();

    abstract w2 b(int i, int i2);

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        w2 w2Var = this;
        while (w2Var.a.p() != 0) {
            w2Var.setPendingCount(w2Var.a.p() - 1);
            int i = 0;
            int i2 = 0;
            while (i < w2Var.a.p() - 1) {
                w2 b = w2Var.b(i, w2Var.b + i2);
                i2 = (int) (i2 + b.a.count());
                b.fork();
                i++;
            }
            w2Var = w2Var.b(i, w2Var.b + i2);
        }
        w2Var.a();
        w2Var.propagateCompletion();
    }
}
