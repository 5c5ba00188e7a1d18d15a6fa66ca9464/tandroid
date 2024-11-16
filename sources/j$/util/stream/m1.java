package j$.util.stream;

/* loaded from: classes2.dex */
final class m1 extends q1 implements b2 {
    private final double[] h;

    m1(j$.util.Q q, b bVar, double[] dArr) {
        super(dArr.length, q, bVar);
        this.h = dArr;
    }

    m1(m1 m1Var, j$.util.Q q, long j, long j2) {
        super(m1Var, q, j, j2, m1Var.h.length);
        this.h = m1Var.h;
    }

    @Override // j$.util.stream.q1
    final q1 a(j$.util.Q q, long j, long j2) {
        return new m1(this, q, j, j2);
    }

    @Override // j$.util.stream.q1, j$.util.stream.e2, j$.util.function.n
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
    /* renamed from: accept */
    public final /* bridge */ /* synthetic */ void r(Object obj) {
        r((Double) obj);
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        return j$.com.android.tools.r8.a.b(this, nVar);
    }

    @Override // j$.util.stream.b2
    public final /* synthetic */ void r(Double d) {
        t0.e(this, d);
    }
}
