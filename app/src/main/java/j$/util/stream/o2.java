package j$.util.stream;
/* loaded from: classes2.dex */
public final class o2 extends s2 implements j3 {
    private final double[] h;

    o2(o2 o2Var, j$.util.u uVar, long j, long j2) {
        super(o2Var, uVar, j, j2, o2Var.h.length);
        this.h = o2Var.h;
    }

    public o2(j$.util.u uVar, y2 y2Var, double[] dArr) {
        super(uVar, y2Var, dArr.length);
        this.h = dArr;
    }

    @Override // j$.util.stream.s2, j$.util.stream.m3
    public void accept(double d) {
        int i = this.f;
        if (i < this.g) {
            double[] dArr = this.h;
            this.f = i + 1;
            dArr[i] = d;
            return;
        }
        throw new IndexOutOfBoundsException(Integer.toString(this.f));
    }

    @Override // j$.util.stream.s2
    s2 b(j$.util.u uVar, long j, long j2) {
        return new o2(this, uVar, j, j2);
    }

    /* renamed from: c */
    public /* synthetic */ void accept(Double d) {
        o1.a(this, d);
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        fVar.getClass();
        return new j$.util.function.e(this, fVar);
    }
}
