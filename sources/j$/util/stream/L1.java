package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
final class L1 implements N1, d2 {
    private long a;
    final /* synthetic */ long b;
    final /* synthetic */ j$.util.function.S c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public L1(long j, j$.util.function.S s) {
        this.b = j;
        this.c = s;
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
        this.a = this.c.applyAsLong(this.a, j);
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

    @Override // j$.util.function.Supplier
    public final Object get() {
        return Long.valueOf(this.a);
    }

    @Override // j$.util.stream.N1
    public final void h(N1 n1) {
        accept(((L1) n1).a);
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
        this.a = this.b;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        return false;
    }
}
