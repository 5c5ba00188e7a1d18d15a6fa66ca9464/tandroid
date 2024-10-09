package j$.util.stream;

import j$.util.function.BiFunction;
import j$.util.function.Function;
import java.util.Collection;
import java.util.Set;

/* loaded from: classes2.dex */
public final /* synthetic */ class k implements j$.util.function.f {
    public final /* synthetic */ int a;

    public /* synthetic */ k(int i) {
        this.a = i;
    }

    @Override // j$.util.function.BiFunction
    public final /* synthetic */ BiFunction andThen(Function function) {
        switch (this.a) {
            case 0:
                return j$.com.android.tools.r8.a.a(this, function);
            case 1:
                return j$.com.android.tools.r8.a.a(this, function);
            case 2:
                return j$.com.android.tools.r8.a.a(this, function);
            case 3:
                return j$.com.android.tools.r8.a.a(this, function);
            default:
                return j$.com.android.tools.r8.a.a(this, function);
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
                return new M0((z0) obj, (z0) obj2);
            case 2:
                return new N0((B0) obj, (B0) obj2);
            case 3:
                return new O0((D0) obj, (D0) obj2);
            default:
                return new Q0((F0) obj, (F0) obj2);
        }
    }
}
