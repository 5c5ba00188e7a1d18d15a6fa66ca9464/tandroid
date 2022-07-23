package j$.util.stream;
/* loaded from: classes2.dex */
public final class q2 extends s2 implements l3 {
    private final long[] h;

    q2(q2 q2Var, j$.util.u uVar, long j, long j2) {
        super(q2Var, uVar, j, j2, q2Var.h.length);
        this.h = q2Var.h;
    }

    public q2(j$.util.u uVar, y2 y2Var, long[] jArr) {
        super(uVar, y2Var, jArr.length);
        this.h = jArr;
    }

    @Override // j$.util.stream.s2, j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public void accept(long j) {
        int i = this.f;
        if (i < this.g) {
            long[] jArr = this.h;
            this.f = i + 1;
            jArr[i] = j;
            return;
        }
        throw new IndexOutOfBoundsException(Integer.toString(this.f));
    }

    @Override // j$.util.stream.s2
    s2 b(j$.util.u uVar, long j, long j2) {
        return new q2(this, uVar, j, j2);
    }

    /* renamed from: c */
    public /* synthetic */ void accept(Long l) {
        o1.c(this, l);
    }

    @Override // j$.util.function.q
    public j$.util.function.q f(j$.util.function.q qVar) {
        qVar.getClass();
        return new j$.util.function.p(this, qVar);
    }
}
