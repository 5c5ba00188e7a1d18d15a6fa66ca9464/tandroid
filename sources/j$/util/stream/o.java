package j$.util.stream;
/* loaded from: classes2.dex */
final class o extends b2 {
    boolean b;
    Object c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(f2 f2Var) {
        super(f2Var);
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        f2 f2Var = this.a;
        if (obj != null) {
            Object obj2 = this.c;
            if (obj2 != null && obj.equals(obj2)) {
                return;
            }
        } else if (this.b) {
            return;
        } else {
            this.b = true;
            obj = null;
        }
        this.c = obj;
        f2Var.accept((f2) obj);
    }

    @Override // j$.util.stream.b2, j$.util.stream.f2
    public final void end() {
        this.b = false;
        this.c = null;
        this.a.end();
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        this.b = false;
        this.c = null;
        this.a.f(-1L);
    }
}
