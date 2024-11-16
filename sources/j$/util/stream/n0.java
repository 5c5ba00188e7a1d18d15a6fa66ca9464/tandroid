package j$.util.stream;

/* loaded from: classes2.dex */
final class n0 extends p0 implements d2 {
    final /* synthetic */ q0 c;
    final /* synthetic */ j$.util.function.Z d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    n0(j$.util.function.Z z, q0 q0Var) {
        super(q0Var);
        this.c = q0Var;
        this.d = z;
    }

    @Override // j$.util.stream.p0, j$.util.stream.e2
    public final void accept(long j) {
        boolean z;
        boolean z2;
        if (this.a) {
            return;
        }
        boolean test = this.d.a.test(j);
        q0 q0Var = this.c;
        z = q0Var.a;
        if (test == z) {
            this.a = true;
            z2 = q0Var.b;
            this.b = z2;
        }
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final /* bridge */ /* synthetic */ void r(Object obj) {
        j((Long) obj);
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        return j$.com.android.tools.r8.a.d(this, w);
    }

    @Override // j$.util.stream.d2
    public final /* synthetic */ void j(Long l) {
        t0.i(this, l);
    }
}
