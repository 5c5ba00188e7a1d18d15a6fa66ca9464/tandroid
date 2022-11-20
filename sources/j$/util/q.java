package j$.util;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class q implements j$.util.function.q {
    public final /* synthetic */ Consumer a;

    @Override // j$.util.function.q
    public final void accept(long j) {
        this.a.accept(Long.valueOf(j));
    }

    @Override // j$.util.function.q
    public j$.util.function.q f(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        return new j$.util.function.p(this, qVar);
    }
}
