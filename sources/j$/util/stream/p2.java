package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class p2 extends t2 implements k3 {
    private final double[] h;

    p2(p2 p2Var, j$.util.t tVar, long j, long j2) {
        super(p2Var, tVar, j, j2, p2Var.h.length);
        this.h = p2Var.h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p2(j$.util.t tVar, z2 z2Var, double[] dArr) {
        super(tVar, z2Var, dArr.length);
        this.h = dArr;
    }

    @Override // j$.util.stream.t2, j$.util.stream.n3
    public void accept(double d) {
        int i = this.f;
        if (i >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        double[] dArr = this.h;
        this.f = i + 1;
        dArr[i] = d;
    }

    @Override // j$.util.stream.t2
    t2 b(j$.util.t tVar, long j, long j2) {
        return new p2(this, tVar, j, j2);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: c */
    public /* synthetic */ void accept(Double d) {
        p1.a(this, d);
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
    }
}
