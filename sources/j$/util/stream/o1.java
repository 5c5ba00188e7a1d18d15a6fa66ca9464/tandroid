package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class o1 extends p1 {
    private final Object[] h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o1(j$.util.Q q, u0 u0Var, Object[] objArr) {
        super(objArr.length, q, u0Var);
        this.h = objArr;
    }

    o1(o1 o1Var, j$.util.Q q, long j, long j2) {
        super(o1Var, q, j, j2, o1Var.h.length);
        this.h = o1Var.h;
    }

    @Override // j$.util.stream.p1
    final p1 a(j$.util.Q q, long j, long j2) {
        return new o1(this, q, j, j2);
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        int i = this.f;
        if (i >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        Object[] objArr = this.h;
        this.f = i + 1;
        objArr[i] = obj;
    }
}
