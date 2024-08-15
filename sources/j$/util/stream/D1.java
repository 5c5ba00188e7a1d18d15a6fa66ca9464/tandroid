package j$.util.stream;

import j$.util.Optional;
import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class D1 implements O1 {
    private boolean a;
    private Object b;
    final /* synthetic */ j$.util.function.f c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public D1(j$.util.function.f fVar) {
        this.c = fVar;
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
        if (this.a) {
            this.a = false;
        } else {
            obj = this.c.apply(this.b, obj);
        }
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
    public final void f(long j) {
        this.a = true;
        this.b = null;
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        return this.a ? Optional.empty() : Optional.of(this.b);
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ boolean h() {
        return false;
    }

    @Override // j$.util.stream.O1
    public final void k(O1 o1) {
        D1 d1 = (D1) o1;
        if (d1.a) {
            return;
        }
        accept(d1.b);
    }
}
