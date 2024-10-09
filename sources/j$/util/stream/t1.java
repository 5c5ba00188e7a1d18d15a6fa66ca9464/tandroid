package j$.util.stream;

import java.util.concurrent.CountedCompleter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class t1 extends CountedCompleter {
    protected final F0 a;
    protected final int b;
    public final /* synthetic */ int c;
    private final Object d;

    public t1(F0 f0, Object obj, int i) {
        this.c = i;
        this.a = f0;
        this.b = 0;
        this.d = obj;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public t1(t1 t1Var, E0 e0, int i) {
        this(t1Var, e0, i, (byte) 0);
        this.c = 0;
        this.d = t1Var.d;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public t1(t1 t1Var, F0 f0, int i) {
        this(t1Var, f0, i, (byte) 0);
        this.c = 1;
        this.d = (Object[]) t1Var.d;
    }

    t1(t1 t1Var, F0 f0, int i, byte b) {
        super(t1Var);
        this.a = f0;
        this.b = i;
    }

    final void a() {
        switch (this.c) {
            case 0:
                ((E0) this.a).d(this.d, this.b);
                return;
            default:
                this.a.i((Object[]) this.d, this.b);
                return;
        }
    }

    final t1 b(int i, int i2) {
        switch (this.c) {
            case 0:
                return new t1(this, ((E0) this.a).a(i), i2);
            default:
                return new t1(this, this.a.a(i), i2);
        }
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void compute() {
        t1 t1Var = this;
        while (t1Var.a.p() != 0) {
            t1Var.setPendingCount(t1Var.a.p() - 1);
            int i = 0;
            int i2 = 0;
            while (i < t1Var.a.p() - 1) {
                t1 b = t1Var.b(i, t1Var.b + i2);
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
