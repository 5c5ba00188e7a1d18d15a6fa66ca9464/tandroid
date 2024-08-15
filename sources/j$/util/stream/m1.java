package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class m1 extends p1 implements d2 {
    private final int[] h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m1(j$.util.Q q, u0 u0Var, int[] iArr) {
        super(iArr.length, q, u0Var);
        this.h = iArr;
    }

    m1(m1 m1Var, j$.util.Q q, long j, long j2) {
        super(m1Var, q, j, j2, m1Var.h.length);
        this.h = m1Var.h;
    }

    @Override // j$.util.stream.p1
    final p1 a(j$.util.Q q, long j, long j2) {
        return new m1(this, q, j, j2);
    }

    @Override // j$.util.stream.p1, j$.util.stream.f2
    public final void accept(int i) {
        int i2 = this.f;
        if (i2 >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        int[] iArr = this.h;
        this.f = i2 + 1;
        iArr[i2] = i;
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        g((Integer) obj);
    }

    @Override // j$.util.stream.d2
    public final /* synthetic */ void g(Integer num) {
        u0.l0(this, num);
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        k.getClass();
        return new j$.util.function.H(this, k);
    }
}
