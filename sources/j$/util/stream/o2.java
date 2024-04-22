package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class o2 extends s2 implements j3 {
    private final double[] h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o2(j$.util.s sVar, y2 y2Var, double[] dArr) {
        super(sVar, y2Var, dArr.length);
        this.h = dArr;
    }

    o2(o2 o2Var, j$.util.s sVar, long j, long j2) {
        super(o2Var, sVar, j, j2, o2Var.h.length);
        this.h = o2Var.h;
    }

    @Override // j$.util.stream.s2, j$.util.stream.m3
    public void accept(double d) {
        int i = this.f;
        if (i >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        double[] dArr = this.h;
        this.f = i + 1;
        dArr[i] = d;
    }

    @Override // j$.util.stream.s2
    s2 b(j$.util.s sVar, long j, long j2) {
        return new o2(this, sVar, j, j2);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: c */
    public /* synthetic */ void accept(Double d) {
        o1.a(this, d);
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
    }
}
