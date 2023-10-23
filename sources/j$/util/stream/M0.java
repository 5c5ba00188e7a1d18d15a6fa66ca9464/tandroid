package j$.util.stream;

import j$.util.function.BiConsumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class M0 implements BiConsumer {
    public static final /* synthetic */ M0 a = new M0();

    private /* synthetic */ M0() {
    }

    @Override // j$.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        ((j$.util.i) obj).b((j$.util.i) obj2);
    }

    @Override // j$.util.function.BiConsumer
    public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
        return Objects.requireNonNull(biConsumer);
    }
}
