package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.s;
import java.util.Objects;
/* loaded from: classes2.dex */
final class w4 extends z4 implements s.b {
    /* JADX INFO: Access modifiers changed from: package-private */
    public w4(s.b bVar, long j, long j2) {
        super(bVar, j, j2);
    }

    w4(s.b bVar, long j, long j2, long j3, long j4) {
        super(bVar, j, j2, j3, j4, null);
    }

    @Override // j$.util.stream.D4
    protected j$.util.s a(j$.util.s sVar, long j, long j2, long j3, long j4) {
        return new w4((s.b) sVar, j, j2, j3, j4);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.k(this, consumer);
    }

    @Override // j$.util.stream.z4
    protected /* bridge */ /* synthetic */ Object f() {
        return new j$.util.function.l() { // from class: j$.util.stream.v4
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

    @Override // j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.c(this, consumer);
    }
}
