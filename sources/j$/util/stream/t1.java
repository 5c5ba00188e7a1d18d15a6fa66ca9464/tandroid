package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class t1 extends CountedCompleter {
    protected final D0 a;
    protected final int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t1(D0 d0) {
        this.a = d0;
        this.b = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public t1(t1 t1Var, D0 d0, int i) {
        super(t1Var);
        this.a = d0;
        this.b = i;
    }

    abstract void a();

    abstract s1 b(int i, int i2);

    @Override // java.util.concurrent.CountedCompleter
    public final void compute() {
        t1 t1Var = this;
        while (t1Var.a.j() != 0) {
            t1Var.setPendingCount(t1Var.a.j() - 1);
            int i = 0;
            int i2 = 0;
            while (i < t1Var.a.j() - 1) {
                s1 b = t1Var.b(i, t1Var.b + i2);
                i2 = (int) (i2 + b.a.count());
                b.fork();
                i++;
            }
            t1Var = t1Var.b(i, t1Var.b + i2);
        }
        t1Var.a();
        t1Var.propagateCompletion();
    }
}
