package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class l1 extends p1 implements c2 {
    private final double[] h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l1(j$.util.Q q, u0 u0Var, double[] dArr) {
        super(dArr.length, q, u0Var);
        this.h = dArr;
    }

    l1(l1 l1Var, j$.util.Q q, long j, long j2) {
        super(l1Var, q, j, j2, l1Var.h.length);
        this.h = l1Var.h;
    }

    @Override // j$.util.stream.p1
    final p1 a(j$.util.Q q, long j, long j2) {
        return new l1(this, q, j, j2);
    }

    @Override // j$.util.stream.p1, j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public final void accept(double d) {
        int i = this.f;
        if (i >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        double[] dArr = this.h;
        this.f = i + 1;
        dArr[i] = d;
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        p((Double) obj);
    }

    @Override // j$.util.function.m
    public final j$.util.function.m m(j$.util.function.m mVar) {
        mVar.getClass();
        return new j$.util.function.j(this, mVar);
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void p(Double d) {
        u0.j0(this, d);
    }
}
