package j$.util.stream;

/* loaded from: classes2.dex */
final class m0 extends p0 implements c2 {
    final /* synthetic */ q0 c;
    final /* synthetic */ j$.util.function.J d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public m0(j$.util.function.J j, q0 q0Var) {
        super(q0Var);
        this.c = q0Var;
        this.d = j;
    }

    @Override // j$.util.stream.p0, j$.util.stream.e2
    public final void accept(int i) {
        boolean z;
        boolean z2;
        if (this.a) {
            return;
        }
        boolean test = this.d.a.test(i);
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
        o((Integer) obj);
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void o(Integer num) {
        t0.g(this, num);
    }
}
