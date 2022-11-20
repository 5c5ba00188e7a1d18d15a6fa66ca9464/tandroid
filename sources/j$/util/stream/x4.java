package j$.util.stream;

import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class x4 implements j$.util.function.q {
    public static final /* synthetic */ x4 a = new x4();

    private /* synthetic */ x4() {
    }

    @Override // j$.util.function.q
    public final void accept(long j) {
    }

    @Override // j$.util.function.q
    public j$.util.function.q f(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        return new j$.util.function.p(this, qVar);
    }
}
