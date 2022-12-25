package j$.wrappers;

import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class M implements Function {
    final /* synthetic */ j$.util.function.Function a;

    private /* synthetic */ M(j$.util.function.Function function) {
        this.a = function;
    }

    public static /* synthetic */ Function a(j$.util.function.Function function) {
        if (function == null) {
            return null;
        }
        return function instanceof L ? ((L) function).a : new M(function);
    }

    @Override // java.util.function.Function
    public /* synthetic */ Function andThen(Function function) {
        return a(this.a.andThen(L.a(function)));
    }

    @Override // java.util.function.Function
    public /* synthetic */ Object apply(Object obj) {
        return this.a.apply(obj);
    }

    @Override // java.util.function.Function
    public /* synthetic */ Function compose(Function function) {
        return a(this.a.compose(L.a(function)));
    }
}
