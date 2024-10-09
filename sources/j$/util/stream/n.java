package j$.util.stream;

/* loaded from: classes2.dex */
final class n extends a2 {
    boolean b;
    Object c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(e2 e2Var) {
        super(e2Var);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final void r(Object obj) {
        e2 e2Var = this.a;
        if (obj != null) {
            Object obj2 = this.c;
            if (obj2 != null && obj.equals(obj2)) {
                return;
            }
        } else {
            if (this.b) {
                return;
            }
            this.b = true;
            obj = null;
        }
        this.c = obj;
        e2Var.r((e2) obj);
    }

    @Override // j$.util.stream.a2, j$.util.stream.e2
    public final void m() {
        this.b = false;
        this.c = null;
        this.a.m();
    }

    @Override // j$.util.stream.a2, j$.util.stream.e2
    public final void n(long j) {
        this.b = false;
        this.c = null;
        this.a.n(-1L);
    }
}
