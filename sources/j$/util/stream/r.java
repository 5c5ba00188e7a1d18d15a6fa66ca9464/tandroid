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
    public final /* synthetic */ BiFunction andThen(Function function) {
        switch (this.a) {
            case 0:
                return j$.com.android.tools.r8.a.a(this, function);
            case 1:
                return j$.com.android.tools.r8.a.a(this, function);
            default:
                return j$.com.android.tools.r8.a.a(this, function);
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
