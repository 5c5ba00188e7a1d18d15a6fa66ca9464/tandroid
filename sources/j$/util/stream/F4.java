package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.t;
import java.util.Objects;
/* loaded from: classes2.dex */
final class F4 extends H4 implements t.b, j$.util.function.l {
    int e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public F4(t.b bVar, long j, long j2) {
        super(bVar, j, j2);
    }

    F4(t.b bVar, F4 f4) {
        super(bVar, f4);
    }

    @Override // j$.util.function.l
    public void accept(int i) {
        this.e = i;
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.k(this, consumer);
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.c(this, consumer);
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new j$.util.function.k(this, lVar);
    }

    @Override // j$.util.stream.J4
    protected j$.util.t q(j$.util.t tVar) {
        return new F4((t.b) tVar, this);
    }

    @Override // j$.util.stream.H4
    protected void s(Object obj) {
        ((j$.util.function.l) obj).accept(this.e);
    }

    @Override // j$.util.stream.H4
    protected j4 t(int i) {
        return new h4(i);
    }
}
