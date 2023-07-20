package j$.wrappers;

import java.util.function.ToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class F0 implements ToLongFunction {
    final /* synthetic */ j$.util.function.ToLongFunction a;

    private /* synthetic */ F0(j$.util.function.ToLongFunction toLongFunction) {
        this.a = toLongFunction;
    }

    public static /* synthetic */ ToLongFunction a(j$.util.function.ToLongFunction toLongFunction) {
        if (toLongFunction == null) {
            return null;
        }
        return toLongFunction instanceof E0 ? ((E0) toLongFunction).a : new F0(toLongFunction);
    }

    @Override // java.util.function.ToLongFunction
    public /* synthetic */ long applyAsLong(Object obj) {
        return this.a.applyAsLong(obj);
    }
}
