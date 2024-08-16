package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
abstract class Q implements x3, y3 {
    private final boolean a;

    /* JADX INFO: Access modifiers changed from: protected */
    public Q(boolean z) {
        this.a = z;
    }

    @Override // j$.util.stream.x3
    public final Object a(b bVar, j$.util.Q q) {
        bVar.getClass();
        bVar.i0(q, bVar.E0(this));
        return null;
    }

    public /* synthetic */ void accept(double d) {
        t0.b();
        throw null;
    }

    public /* synthetic */ void accept(int i) {
        t0.k();
        throw null;
    }

    public /* synthetic */ void accept(long j) {
        t0.l();
        throw null;
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.x3
    public final Object c(b bVar, j$.util.Q q) {
        (this.a ? new S(bVar, q, this) : new T(bVar, q, bVar.E0(this))).invoke();
        return null;
    }

    @Override // j$.util.stream.x3
    public final int d() {
        if (this.a) {
            return 0;
        }
        return S2.r;
    }

    @Override // j$.util.function.Supplier
    public final /* bridge */ /* synthetic */ Object get() {
        return null;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void m() {
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void n(long j) {
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        return false;
    }
}
