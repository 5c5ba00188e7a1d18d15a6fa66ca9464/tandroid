package j$.util.stream;

import j$.util.function.BiFunction;
import j$.util.function.Function;
import java.util.Collection;
import java.util.Set;
/* loaded from: classes2.dex */
public final /* synthetic */ class l implements j$.util.function.f {
    public final /* synthetic */ int a;

    public /* synthetic */ l(int i) {
        this.a = i;
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
            case 2:
                function.getClass();
                return new j$.util.concurrent.u(this, function);
            case 3:
                function.getClass();
                return new j$.util.concurrent.u(this, function);
            default:
                function.getClass();
                return new j$.util.concurrent.u(this, function);
        }
    }

    @Override // j$.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        switch (this.a) {
            case 0:
                Collection collection = (Collection) obj;
                Set set = Collectors.a;
                collection.addAll((Collection) obj2);
                return collection;
            case 1:
                return new L0((z0) obj, (z0) obj2);
            case 2:
                return new M0((A0) obj, (A0) obj2);
            case 3:
                return new N0((B0) obj, (B0) obj2);
            default:
                return new P0((D0) obj, (D0) obj2);
        }
    }
}
