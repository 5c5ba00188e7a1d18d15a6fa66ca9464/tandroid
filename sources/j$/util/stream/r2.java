package j$.util.stream;
/* loaded from: classes2.dex */
final class r2 extends s2 {
    private final Object[] h;

    r2(r2 r2Var, j$.util.u uVar, long j, long j2) {
        super(r2Var, uVar, j, j2, r2Var.h.length);
        this.h = r2Var.h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public r2(j$.util.u uVar, y2 y2Var, Object[] objArr) {
        super(uVar, y2Var, objArr.length);
        this.h = objArr;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        int i = this.f;
        if (i >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        Object[] objArr = this.h;
        this.f = i + 1;
        objArr[i] = obj;
    }

    @Override // j$.util.stream.s2
    s2 b(j$.util.u uVar, long j, long j2) {
        return new r2(this, uVar, j, j2);
    }
}
