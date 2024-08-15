package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
abstract class q0 implements f2 {
    boolean a;
    boolean b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q0(r0 r0Var) {
        boolean z;
        z = r0Var.b;
        this.b = !z;
    }

    @Override // j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public /* synthetic */ void accept(double d) {
        u0.i0();
        throw null;
    }

    @Override // j$.util.stream.f2
    public /* synthetic */ void accept(int i) {
        u0.p0();
        throw null;
    }

    @Override // j$.util.stream.f2
    public /* synthetic */ void accept(long j) {
        u0.q0();
        throw null;
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void end() {
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void f(long j) {
    }

    @Override // j$.util.stream.f2
    public final boolean h() {
        return this.a;
    }
}
