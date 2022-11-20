package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class n1 implements Consumer {
    public static final /* synthetic */ n1 a = new n1();

    private /* synthetic */ n1() {
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Objects.requireNonNull(consumer);
    }
}
