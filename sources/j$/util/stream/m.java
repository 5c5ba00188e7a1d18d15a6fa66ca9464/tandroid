package j$.util.stream;

import j$.util.function.BiConsumer;
import java.util.LinkedHashSet;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class m implements BiConsumer {
    public static final /* synthetic */ m a = new m();

    private /* synthetic */ m() {
    }

    @Override // j$.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        ((LinkedHashSet) obj).add(obj2);
    }

    @Override // j$.util.function.BiConsumer
    public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
        return Objects.requireNonNull(biConsumer);
    }
}
