package j$.util.stream;
/* loaded from: classes2.dex */
final class s2 extends t2 {
    private final Object[] h;

    s2(s2 s2Var, j$.util.t tVar, long j, long j2) {
        super(s2Var, tVar, j, j2, s2Var.h.length);
        this.h = s2Var.h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public s2(j$.util.t tVar, z2 z2Var, Object[] objArr) {
        super(tVar, z2Var, objArr.length);
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

    @Override // j$.util.stream.t2
    t2 b(j$.util.t tVar, long j, long j2) {
        return new s2(this, tVar, j, j2);
    }
}
