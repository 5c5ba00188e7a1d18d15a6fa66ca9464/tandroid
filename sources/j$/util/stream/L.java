package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
abstract class L implements D3 {
    boolean a;
    Object b;

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
    /* renamed from: accept */
    public final void p(Object obj) {
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
