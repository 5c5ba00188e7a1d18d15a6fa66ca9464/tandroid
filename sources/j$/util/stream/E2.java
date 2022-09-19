package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
class E2 implements S2, j3 {
    private boolean a;
    private double b;
    final /* synthetic */ j$.util.function.d c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public E2(j$.util.function.d dVar) {
        this.c = dVar;
    }

    @Override // j$.util.stream.m3
    public void accept(double d) {
        if (this.a) {
            this.a = false;
        } else {
            d = this.c.applyAsDouble(this.b, d);
        }
        this.b = d;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(int i) {
        o1.d(this);
        throw null;
    }

    @Override // j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        o1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return consumer.getClass();
    }

    @Override // j$.util.function.Consumer
    /* renamed from: b */
    public /* synthetic */ void accept(Double d) {
        o1.a(this, d);
    }

    @Override // j$.util.function.y
    public Object get() {
        return this.a ? j$.util.j.a() : j$.util.j.d(this.b);
    }

    @Override // j$.util.stream.S2
    public void h(S2 s2) {
        E2 e2 = (E2) s2;
        if (!e2.a) {
            accept(e2.b);
        }
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        fVar.getClass();
        return new j$.util.function.e(this, fVar);
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        this.a = true;
        this.b = 0.0d;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ boolean o() {
        return false;
    }
}
