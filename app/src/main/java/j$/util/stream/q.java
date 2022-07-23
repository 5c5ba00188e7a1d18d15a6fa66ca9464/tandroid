package j$.util.stream;
/* loaded from: classes2.dex */
class q extends i3 {
    boolean b;
    Object c;

    public q(s sVar, m3 m3Var) {
        super(m3Var);
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        if (obj == null) {
            if (this.b) {
                return;
            }
            this.b = true;
            m3 m3Var = this.a;
            this.c = null;
            m3Var.accept((m3) null);
            return;
        }
        Object obj2 = this.c;
        if (obj2 != null && obj.equals(obj2)) {
            return;
        }
        m3 m3Var2 = this.a;
        this.c = obj;
        m3Var2.accept((m3) obj);
    }

    @Override // j$.util.stream.i3, j$.util.stream.m3
    public void m() {
        this.b = false;
        this.c = null;
        this.a.m();
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        this.b = false;
        this.c = null;
        this.a.n(-1L);
    }
}
