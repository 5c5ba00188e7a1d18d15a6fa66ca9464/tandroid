package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class i3 implements m3 {
    protected final m3 a;

    public i3(m3 m3Var) {
        Objects.requireNonNull(m3Var);
        this.a = m3Var;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(double d) {
        o1.f(this);
        throw null;
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
        return Consumer.-CC.$default$andThen(this, consumer);
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
