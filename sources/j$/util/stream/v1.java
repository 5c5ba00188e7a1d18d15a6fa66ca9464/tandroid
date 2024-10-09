package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;

/* loaded from: classes2.dex */
final class v1 extends O1 implements N1, d2 {
    final /* synthetic */ Supplier b;
    final /* synthetic */ j$.util.function.o0 c;
    final /* synthetic */ j$.util.function.f d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v1(Supplier supplier, j$.util.function.o0 o0Var, j$.util.function.f fVar) {
        this.b = supplier;
        this.c = o0Var;
        this.d = fVar;
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final /* synthetic */ void accept(double d) {
        t0.b();
        throw null;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void accept(int i) {
        t0.k();
        throw null;
    }

    @Override // j$.util.stream.e2
    public final void accept(long j) {
        this.c.accept(this.a, j);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final /* bridge */ /* synthetic */ void r(Object obj) {
        j((Long) obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        return j$.com.android.tools.r8.a.d(this, w);
    }

    @Override // j$.util.stream.N1
    public final void h(N1 n1) {
        this.a = this.d.apply(this.a, ((v1) n1).a);
    }

    @Override // j$.util.stream.d2
    public final /* synthetic */ void j(Long l) {
        t0.i(this, l);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void m() {
    }

    @Override // j$.util.stream.e2
    public final void n(long j) {
        this.a = this.b.get();
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        return false;
    }
}
