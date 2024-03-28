package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class g3 implements k3 {
    protected final n3 a;

    public g3(n3 n3Var) {
        Objects.requireNonNull(n3Var);
        this.a = n3Var;
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

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
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
