package j$.util.stream;

import j$.util.function.BiConsumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class s0 implements BiConsumer {
    public static final /* synthetic */ s0 a = new s0();

    private /* synthetic */ s0() {
    }

    @Override // j$.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        ((j$.util.h) obj).b((j$.util.h) obj2);
    }

    @Override // j$.util.function.BiConsumer
    public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
        return Objects.requireNonNull(biConsumer);
    }
}
