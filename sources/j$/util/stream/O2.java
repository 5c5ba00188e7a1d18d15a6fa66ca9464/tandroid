package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;
import java.util.Objects;
/* loaded from: classes2.dex */
class O2 extends T2 implements S2, k3 {
    final /* synthetic */ Supplier b;
    final /* synthetic */ j$.util.function.u c;
    final /* synthetic */ j$.util.function.b d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public O2(Supplier supplier, j$.util.function.u uVar, j$.util.function.b bVar) {
        this.b = supplier;
        this.c = uVar;
        this.d = bVar;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(double d) {
        o1.f(this);
        throw null;
    }

    @Override // j$.util.stream.m3
    public void accept(int i) {
        this.c.accept(this.a, i);
    }

    @Override // j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        o1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: b */
    public /* synthetic */ void accept(Integer num) {
        o1.b(this, num);
    }

    @Override // j$.util.stream.S2
    public void h(S2 s2) {
        this.a = this.d.apply(this.a, ((O2) s2).a);
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new j$.util.function.k(this, lVar);
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        this.a = this.b.get();
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ boolean o() {
        return false;
    }
}
