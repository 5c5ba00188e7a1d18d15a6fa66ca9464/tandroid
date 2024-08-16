package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
abstract class K implements y3 {
    boolean a;
    Object b;

    @Override // j$.util.stream.e2, j$.util.function.n
    public /* synthetic */ void accept(double d) {
        t0.b();
        throw null;
    }

    @Override // j$.util.stream.e2
    public /* synthetic */ void accept(int i) {
        t0.k();
        throw null;
    }

    @Override // j$.util.stream.e2
    public /* synthetic */ void accept(long j) {
        t0.l();
        throw null;
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final void r(Object obj) {
        if (this.a) {
            return;
        }
        this.a = true;
        this.b = obj;
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void m() {
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void n(long j) {
    }

    @Override // j$.util.stream.e2
    public final boolean q() {
        return this.a;
    }
}
