package j$.util.stream;
/* loaded from: classes2.dex */
class r extends j3 {
    boolean b;
    Object c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public r(t tVar, n3 n3Var) {
        super(n3Var);
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        if (obj == null) {
            if (this.b) {
                return;
            }
            this.b = true;
            n3 n3Var = this.a;
            this.c = null;
            n3Var.accept((n3) null);
            return;
        }
        Object obj2 = this.c;
        if (obj2 == null || !obj.equals(obj2)) {
            n3 n3Var2 = this.a;
            this.c = obj;
            n3Var2.accept((n3) obj);
        }
    }

    @Override // j$.util.stream.j3, j$.util.stream.n3
    public void m() {
        this.b = false;
        this.c = null;
        this.a.m();
    }

    @Override // j$.util.stream.n3
    public void n(long j) {
        this.b = false;
        this.c = null;
        this.a.n(-1L);
    }
}
