package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class h3 implements l3 {
    protected final n3 a;

    public h3(n3 n3Var) {
        Objects.requireNonNull(n3Var);
        this.a = n3Var;
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void accept(double d) {
        p1.f(this);
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
    public /* synthetic */ void accept(Integer num) {
        p1.b(this, num);
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new j$.util.function.k(this, lVar);
    }

    @Override // j$.util.stream.n3
    public void m() {
        this.a.m();
    }

    @Override // j$.util.stream.n3
    public boolean o() {
        return this.a.o();
    }
}
