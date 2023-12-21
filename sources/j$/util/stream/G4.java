package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.t;
import java.util.Objects;
/* loaded from: classes2.dex */
final class G4 extends H4 implements t.c, j$.util.function.q {
    long e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public G4(t.c cVar, long j, long j2) {
        super(cVar, j, j2);
    }

    G4(t.c cVar, G4 g4) {
        super(cVar, g4);
    }

    @Override // j$.util.function.q
    public void accept(long j) {
        this.e = j;
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.l(this, consumer);
    }

    @Override // j$.util.function.q
    public j$.util.function.q f(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        return new j$.util.function.p(this, qVar);
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.d(this, consumer);
    }

    @Override // j$.util.stream.J4
    protected j$.util.t q(j$.util.t tVar) {
        return new G4((t.c) tVar, this);
    }

    @Override // j$.util.stream.H4
    protected void s(Object obj) {
        ((j$.util.function.q) obj).accept(this.e);
    }

    @Override // j$.util.stream.H4
    protected j4 t(int i) {
        return new i4(i);
    }
}
