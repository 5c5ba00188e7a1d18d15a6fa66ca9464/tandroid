package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class r2 extends t2 implements m3 {
    private final long[] h;

    r2(r2 r2Var, j$.util.t tVar, long j, long j2) {
        super(r2Var, tVar, j, j2, r2Var.h.length);
        this.h = r2Var.h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public r2(j$.util.t tVar, z2 z2Var, long[] jArr) {
        super(tVar, z2Var, jArr.length);
        this.h = jArr;
    }

    @Override // j$.util.stream.t2, j$.util.stream.n3, j$.util.stream.m3, j$.util.function.q
    public void accept(long j) {
        int i = this.f;
        if (i >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        long[] jArr = this.h;
        this.f = i + 1;
        jArr[i] = j;
    }

    @Override // j$.util.stream.t2
    t2 b(j$.util.t tVar, long j, long j2) {
        return new r2(this, tVar, j, j2);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: c */
    public /* synthetic */ void accept(Long l) {
        p1.c(this, l);
    }

    @Override // j$.util.function.q
    public j$.util.function.q f(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        return new j$.util.function.p(this, qVar);
    }
}
