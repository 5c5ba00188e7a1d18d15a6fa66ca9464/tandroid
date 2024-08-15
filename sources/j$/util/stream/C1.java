package j$.util.stream;

import j$.util.function.BiFunction;
import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class C1 extends P1 implements O1 {
    final /* synthetic */ Object b;
    final /* synthetic */ BiFunction c;
    final /* synthetic */ j$.util.function.f d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public C1(Object obj, BiFunction biFunction, j$.util.function.f fVar) {
        this.b = obj;
        this.c = biFunction;
        this.d = fVar;
    }

    @Override // j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public final /* synthetic */ void accept(double d) {
        u0.i0();
        throw null;
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void accept(int i) {
        u0.p0();
        throw null;
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void accept(long j) {
        u0.q0();
        throw null;
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        this.a = this.c.apply(this.a, obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void end() {
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        this.a = this.b;
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ boolean h() {
        return false;
    }

    @Override // j$.util.stream.O1
    public final void k(O1 o1) {
        this.a = this.d.apply(this.a, ((C1) o1).a);
    }
}
