package j$.util.stream;

import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class v4 implements j$.util.function.l {
    public static final /* synthetic */ v4 a = new v4();

    private /* synthetic */ v4() {
    }

    @Override // j$.util.function.l
    public final void accept(int i) {
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new j$.util.function.k(this, lVar);
    }
}
