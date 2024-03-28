package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.t;
import java.util.Objects;
/* loaded from: classes2.dex */
final class z4 extends A4 implements t.c {
    /* JADX INFO: Access modifiers changed from: package-private */
    public z4(t.c cVar, long j, long j2) {
        super(cVar, j, j2);
    }

    z4(t.c cVar, long j, long j2, long j3, long j4) {
        super(cVar, j, j2, j3, j4, null);
    }

    @Override // j$.util.stream.E4
    protected j$.util.t a(j$.util.t tVar, long j, long j2, long j3, long j4) {
        return new z4((t.c) tVar, j, j2, j3, j4);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.l(this, consumer);
    }

    @Override // j$.util.stream.A4
    protected /* bridge */ /* synthetic */ Object f() {
        return new j$.util.function.q() { // from class: j$.util.stream.y4
            @Override // j$.util.function.q
            public final void accept(long j) {
            }

            @Override // j$.util.function.q
            public j$.util.function.q f(j$.util.function.q qVar) {
                Objects.requireNonNull(qVar);
                return new j$.util.function.p(this, qVar);
            }
        };
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.d(this, consumer);
    }
}
