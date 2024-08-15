package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class r implements j$.util.function.f {
    public final /* synthetic */ int a;
    public final /* synthetic */ BiConsumer b;

    public /* synthetic */ r(BiConsumer biConsumer, int i) {
        this.a = i;
        this.b = biConsumer;
    }

    @Override // j$.util.function.BiFunction
    public final BiFunction andThen(Function function) {
        switch (this.a) {
            case 0:
                function.getClass();
                return new j$.util.concurrent.u(this, function);
            case 1:
                function.getClass();
                return new j$.util.concurrent.u(this, function);
            default:
                function.getClass();
                return new j$.util.concurrent.u(this, function);
        }
    }

    @Override // j$.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        int i = this.a;
        BiConsumer biConsumer = this.b;
        switch (i) {
            case 0:
                biConsumer.accept(obj, obj2);
                return obj;
            case 1:
                biConsumer.accept(obj, obj2);
                return obj;
            default:
                biConsumer.accept(obj, obj2);
                return obj;
        }
    }
}
