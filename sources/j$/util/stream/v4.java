package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.t;
import java.util.Objects;
/* loaded from: classes2.dex */
final class v4 extends A4 implements t.a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public v4(t.a aVar, long j, long j2) {
        super(aVar, j, j2);
    }

    v4(t.a aVar, long j, long j2, long j3, long j4) {
        super(aVar, j, j2, j3, j4, null);
    }

    @Override // j$.util.stream.E4
    protected j$.util.t a(j$.util.t tVar, long j, long j2, long j3, long j4) {
        return new v4((t.a) tVar, j, j2, j3, j4);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.j(this, consumer);
    }

    @Override // j$.util.stream.A4
    protected /* bridge */ /* synthetic */ Object f() {
        return new j$.util.function.f() { // from class: j$.util.stream.u4
            @Override // j$.util.function.f
            public final void accept(double d) {
            }

            @Override // j$.util.function.f
            public j$.util.function.f j(j$.util.function.f fVar) {
                Objects.requireNonNull(fVar);
                return new j$.util.function.e(this, fVar);
            }
        };
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.b(this, consumer);
    }
}
