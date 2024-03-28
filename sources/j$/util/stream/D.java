package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Function;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class D implements j$.util.function.b {
    public final /* synthetic */ int a;
    public final /* synthetic */ BiConsumer b;

    public /* synthetic */ D(BiConsumer biConsumer, int i) {
        this.a = i;
        if (i == 1) {
            this.b = biConsumer;
        } else if (i != 2) {
            this.b = biConsumer;
        } else {
            this.b = biConsumer;
        }
    }

    @Override // j$.util.function.BiFunction
    public BiFunction andThen(Function function) {
        switch (this.a) {
            case 0:
                Objects.requireNonNull(function);
                return new j$.util.concurrent.a(this, function);
            case 1:
                Objects.requireNonNull(function);
                return new j$.util.concurrent.a(this, function);
            default:
                Objects.requireNonNull(function);
                return new j$.util.concurrent.a(this, function);
        }
    }

    @Override // j$.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        switch (this.a) {
            case 0:
                this.b.accept(obj, obj2);
                return obj;
            case 1:
                this.b.accept(obj, obj2);
                return obj;
            default:
                this.b.accept(obj, obj2);
                return obj;
        }
    }
}
