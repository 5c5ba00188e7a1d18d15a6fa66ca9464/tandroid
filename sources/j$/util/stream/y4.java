package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.s;
import java.util.Objects;
/* loaded from: classes2.dex */
final class y4 extends z4 implements s.c {
    /* JADX INFO: Access modifiers changed from: package-private */
    public y4(s.c cVar, long j, long j2) {
        super(cVar, j, j2);
    }

    y4(s.c cVar, long j, long j2, long j3, long j4) {
        super(cVar, j, j2, j3, j4, null);
    }

    @Override // j$.util.stream.D4
    protected j$.util.s a(j$.util.s sVar, long j, long j2, long j3, long j4) {
        return new y4((s.c) sVar, j, j2, j3, j4);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.l(this, consumer);
    }

    @Override // j$.util.stream.z4
    protected /* bridge */ /* synthetic */ Object f() {
        return new j$.util.function.q() { // from class: j$.util.stream.x4
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

    @Override // j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.d(this, consumer);
    }
}
