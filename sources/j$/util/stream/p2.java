package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class p2 extends s2 implements k3 {
    private final int[] h;

    p2(p2 p2Var, j$.util.t tVar, long j, long j2) {
        super(p2Var, tVar, j, j2, p2Var.h.length);
        this.h = p2Var.h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p2(j$.util.t tVar, y2 y2Var, int[] iArr) {
        super(tVar, y2Var, iArr.length);
        this.h = iArr;
    }

    @Override // j$.util.stream.s2, j$.util.stream.m3
    public void accept(int i) {
        int i2 = this.f;
        if (i2 >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        int[] iArr = this.h;
        this.f = i2 + 1;
        iArr[i2] = i;
    }

    @Override // j$.util.stream.s2
    s2 b(j$.util.t tVar, long j, long j2) {
        return new p2(this, tVar, j, j2);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: c */
    public /* synthetic */ void accept(Integer num) {
        o1.b(this, num);
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new j$.util.function.k(this, lVar);
    }
}
