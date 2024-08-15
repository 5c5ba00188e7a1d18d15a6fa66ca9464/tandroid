package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class n1 extends p1 implements e2 {
    private final long[] h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n1(j$.util.Q q, u0 u0Var, long[] jArr) {
        super(jArr.length, q, u0Var);
        this.h = jArr;
    }

    n1(n1 n1Var, j$.util.Q q, long j, long j2) {
        super(n1Var, q, j, j2, n1Var.h.length);
        this.h = n1Var.h;
    }

    @Override // j$.util.stream.p1
    final p1 a(j$.util.Q q, long j, long j2) {
        return new n1(this, q, j, j2);
    }

    @Override // j$.util.stream.p1, j$.util.stream.f2
    public final void accept(long j) {
        int i = this.f;
        if (i >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        long[] jArr = this.h;
        this.f = i + 1;
        jArr[i] = j;
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        l((Long) obj);
    }

    @Override // j$.util.function.h0
    public final j$.util.function.h0 i(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        return new j$.util.function.e0(this, h0Var);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void l(Long l) {
        u0.n0(this, l);
    }
}
