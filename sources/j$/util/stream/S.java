package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
abstract class S implements C3, D3 {
    private final boolean a;

    /* JADX INFO: Access modifiers changed from: protected */
    public S(boolean z) {
        this.a = z;
    }

    @Override // j$.util.stream.C3
    public final Object a(u0 u0Var, j$.util.Q q) {
        (this.a ? new T(u0Var, q, this) : new U(u0Var, q, u0Var.Y0(this))).invoke();
        return null;
    }

    public /* synthetic */ void accept(double d) {
        u0.i0();
        throw null;
    }

    public /* synthetic */ void accept(int i) {
        u0.p0();
        throw null;
    }

    public /* synthetic */ void accept(long j) {
        u0.q0();
        throw null;
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.C3
    public final int b() {
        if (this.a) {
            return 0;
        }
        return T2.r;
    }

    @Override // j$.util.stream.C3
    public final Object c(u0 u0Var, j$.util.Q q) {
        u0Var.X0(q, this);
        return null;
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void end() {
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void f(long j) {
    }

    @Override // j$.util.function.Supplier
    public final /* bridge */ /* synthetic */ Object get() {
        return null;
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ boolean h() {
        return false;
    }
}
