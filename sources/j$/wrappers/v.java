package j$.wrappers;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class v implements BinaryOperator {
    final /* synthetic */ j$.util.function.b a;

    private /* synthetic */ v(j$.util.function.b bVar) {
        this.a = bVar;
    }

    public static /* synthetic */ BinaryOperator a(j$.util.function.b bVar) {
        if (bVar == null) {
            return null;
        }
        return bVar instanceof u ? ((u) bVar).a : new v(bVar);
    }

    @Override // java.util.function.BiFunction
    public /* synthetic */ BiFunction andThen(Function function) {
        return t.a(this.a.andThen(L.a(function)));
    }

    @Override // java.util.function.BiFunction
    public /* synthetic */ Object apply(Object obj, Object obj2) {
        return this.a.apply(obj, obj2);
    }
}
