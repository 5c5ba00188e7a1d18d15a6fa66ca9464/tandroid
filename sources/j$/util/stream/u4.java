package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.s;
import java.util.Objects;
/* loaded from: classes2.dex */
final class u4 extends z4 implements s.a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public u4(s.a aVar, long j, long j2) {
        super(aVar, j, j2);
    }

    u4(s.a aVar, long j, long j2, long j3, long j4) {
        super(aVar, j, j2, j3, j4, null);
    }

    @Override // j$.util.stream.D4
    protected j$.util.s a(j$.util.s sVar, long j, long j2, long j3, long j4) {
        return new u4((s.a) sVar, j, j2, j3, j4);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.j(this, consumer);
    }

    @Override // j$.util.stream.z4
    protected /* bridge */ /* synthetic */ Object f() {
        return new j$.util.function.f() { // from class: j$.util.stream.t4
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

    @Override // j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.b(this, consumer);
    }
}
