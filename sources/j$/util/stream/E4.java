package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.t;
import java.util.Objects;
/* loaded from: classes2.dex */
final class E4 extends H4 implements t.a, j$.util.function.f {
    double e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public E4(t.a aVar, long j, long j2) {
        super(aVar, j, j2);
    }

    E4(t.a aVar, E4 e4) {
        super(aVar, e4);
    }

    @Override // j$.util.function.f
    public void accept(double d) {
        this.e = d;
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.j(this, consumer);
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.b(this, consumer);
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
    }

    @Override // j$.util.stream.J4
    protected j$.util.t q(j$.util.t tVar) {
        return new E4((t.a) tVar, this);
    }

    @Override // j$.util.stream.H4
    protected void s(Object obj) {
        ((j$.util.function.f) obj).accept(this.e);
    }

    @Override // j$.util.stream.H4
    protected j4 t(int i) {
        return new g4(i);
    }
}
