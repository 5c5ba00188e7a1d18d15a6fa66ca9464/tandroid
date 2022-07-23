package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public abstract class f3 implements j3 {
    protected final m3 a;

    public f3(m3 m3Var) {
        m3Var.getClass();
        this.a = m3Var;
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

    /* renamed from: b */
    public /* synthetic */ void accept(Double d) {
        o1.a(this, d);
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        fVar.getClass();
        return new j$.util.function.e(this, fVar);
    }

    @Override // j$.util.stream.m3
    public void m() {
        this.a.m();
    }

    @Override // j$.util.stream.m3
    public boolean o() {
        return this.a.o();
    }
}
