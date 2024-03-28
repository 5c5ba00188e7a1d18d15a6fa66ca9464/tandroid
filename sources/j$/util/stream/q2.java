package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class q2 extends t2 implements l3 {
    private final int[] h;

    q2(q2 q2Var, j$.util.t tVar, long j, long j2) {
        super(q2Var, tVar, j, j2, q2Var.h.length);
        this.h = q2Var.h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public q2(j$.util.t tVar, z2 z2Var, int[] iArr) {
        super(tVar, z2Var, iArr.length);
        this.h = iArr;
    }

    @Override // j$.util.stream.t2, j$.util.stream.n3
    public void accept(int i) {
        int i2 = this.f;
        if (i2 >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        int[] iArr = this.h;
        this.f = i2 + 1;
        iArr[i2] = i;
    }

    @Override // j$.util.stream.t2
    t2 b(j$.util.t tVar, long j, long j2) {
        return new q2(this, tVar, j, j2);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: c */
    public /* synthetic */ void accept(Integer num) {
        p1.b(this, num);
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new j$.util.function.k(this, lVar);
    }
}
