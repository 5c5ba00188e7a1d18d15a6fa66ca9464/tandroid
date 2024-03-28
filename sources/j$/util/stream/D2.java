package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
class D2 implements T2, k3 {
    private double a;
    final /* synthetic */ double b;
    final /* synthetic */ j$.util.function.d c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public D2(double d, j$.util.function.d dVar) {
        this.b = d;
        this.c = dVar;
    }

    @Override // j$.util.stream.n3
    public void accept(double d) {
        this.a = this.c.applyAsDouble(this.a, d);
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

    @Override // j$.util.function.Consumer
    /* renamed from: b */
    public /* synthetic */ void accept(Double d) {
        p1.a(this, d);
    }

    @Override // j$.util.function.Supplier
    public Object get() {
        return Double.valueOf(this.a);
    }

    @Override // j$.util.stream.T2
    public void h(T2 t2) {
        accept(((D2) t2).a);
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
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
