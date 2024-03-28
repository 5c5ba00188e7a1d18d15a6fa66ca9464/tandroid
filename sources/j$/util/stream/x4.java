package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.t;
import java.util.Objects;
/* loaded from: classes2.dex */
final class x4 extends A4 implements t.b {
    /* JADX INFO: Access modifiers changed from: package-private */
    public x4(t.b bVar, long j, long j2) {
        super(bVar, j, j2);
    }

    x4(t.b bVar, long j, long j2, long j3, long j4) {
        super(bVar, j, j2, j3, j4, null);
    }

    @Override // j$.util.stream.E4
    protected j$.util.t a(j$.util.t tVar, long j, long j2, long j3, long j4) {
        return new x4((t.b) tVar, j, j2, j3, j4);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.k(this, consumer);
    }

    @Override // j$.util.stream.A4
    protected /* bridge */ /* synthetic */ Object f() {
        return new j$.util.function.l() { // from class: j$.util.stream.w4
            @Override // j$.util.function.l
            public final void accept(int i) {
            }

            @Override // j$.util.function.l
            public j$.util.function.l l(j$.util.function.l lVar) {
                Objects.requireNonNull(lVar);
                return new j$.util.function.k(this, lVar);
            }
        };
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.c(this, consumer);
    }
}
