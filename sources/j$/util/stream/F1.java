package j$.util.stream;

import j$.util.function.BiFunction;
import j$.util.function.Function;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class F1 implements j$.util.function.b {
    public static final /* synthetic */ F1 a = new F1();

    private /* synthetic */ F1() {
    }

    @Override // j$.util.function.BiFunction
    public BiFunction andThen(Function function) {
        Objects.requireNonNull(function);
        return new j$.util.concurrent.a(this, function);
    }

    @Override // j$.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        return new O1((u1) obj, (u1) obj2);
    }
}
