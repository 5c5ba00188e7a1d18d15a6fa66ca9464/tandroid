package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
abstract class x2 extends CountedCompleter {
    protected final B1 a;
    protected final int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x2(B1 b1, int i) {
        this.a = b1;
        this.b = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public x2(x2 x2Var, B1 b1, int i) {
        super(x2Var);
        this.a = b1;
        this.b = i;
    }

    abstract void a();

    abstract x2 b(int i, int i2);

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        x2 x2Var = this;
        while (x2Var.a.p() != 0) {
            x2Var.setPendingCount(x2Var.a.p() - 1);
            int i = 0;
            int i2 = 0;
            while (i < x2Var.a.p() - 1) {
                x2 b = x2Var.b(i, x2Var.b + i2);
                i2 = (int) (i2 + b.a.count());
                b.fork();
                i++;
            }
            x2Var = x2Var.b(i, x2Var.b + i2);
        }
        x2Var.a();
        x2Var.propagateCompletion();
    }
}
