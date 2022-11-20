package j$.util.stream;

import j$.util.function.BiFunction;
import j$.util.function.Function;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class I1 implements j$.util.function.b {
    public static final /* synthetic */ I1 a = new I1();

    private /* synthetic */ I1() {
    }

    @Override // j$.util.function.BiFunction
    public BiFunction andThen(Function function) {
        Objects.requireNonNull(function);
        return new j$.util.concurrent.a(this, function);
    }

    @Override // j$.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        return new P1((w1) obj, (w1) obj2);
    }
}
