package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
abstract class k1 implements n3 {
    boolean a;
    boolean b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k1(l1 l1Var) {
        boolean z;
        z = l1Var.b;
        this.b = !z;
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void accept(double d) {
        p1.f(this);
        throw null;
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void accept(int i) {
        p1.d(this);
        throw null;
    }

    @Override // j$.util.stream.n3, j$.util.stream.m3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        p1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void n(long j) {
    }

    @Override // j$.util.stream.n3
    public boolean o() {
        return this.a;
    }
}
