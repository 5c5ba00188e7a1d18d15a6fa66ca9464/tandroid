package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;
import java.util.Objects;
/* loaded from: classes2.dex */
class P2 extends U2 implements T2, l3 {
    final /* synthetic */ Supplier b;
    final /* synthetic */ j$.util.function.v c;
    final /* synthetic */ j$.util.function.b d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public P2(Supplier supplier, j$.util.function.v vVar, j$.util.function.b bVar) {
        this.b = supplier;
        this.c = vVar;
        this.d = bVar;
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void accept(double d) {
        p1.f(this);
        throw null;
    }

    @Override // j$.util.stream.n3
    public void accept(int i) {
        this.c.accept(this.a, i);
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

    @Override // j$.util.function.Consumer
    /* renamed from: b */
    public /* synthetic */ void accept(Integer num) {
        p1.b(this, num);
    }

    @Override // j$.util.stream.T2
    public void h(T2 t2) {
        this.a = this.d.apply(this.a, ((P2) t2).a);
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new j$.util.function.k(this, lVar);
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.n3
    public void n(long j) {
        this.a = this.b.get();
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ boolean o() {
        return false;
    }
}
