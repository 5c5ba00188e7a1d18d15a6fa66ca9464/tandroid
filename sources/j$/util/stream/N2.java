package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
class N2 implements T2, l3 {
    private int a;
    final /* synthetic */ int b;
    final /* synthetic */ j$.util.function.j c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public N2(int i, j$.util.function.j jVar) {
        this.b = i;
        this.c = jVar;
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void accept(double d) {
        p1.f(this);
        throw null;
    }

    @Override // j$.util.stream.n3
    public void accept(int i) {
        this.a = this.c.applyAsInt(this.a, i);
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

    @Override // j$.util.function.Supplier
    public Object get() {
        return Integer.valueOf(this.a);
    }

    @Override // j$.util.stream.T2
    public void h(T2 t2) {
        accept(((N2) t2).a);
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
        this.a = this.b;
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ boolean o() {
        return false;
    }
}
