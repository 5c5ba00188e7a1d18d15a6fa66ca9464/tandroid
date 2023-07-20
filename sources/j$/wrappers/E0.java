package j$.wrappers;

import j$.util.function.ToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class E0 implements ToLongFunction {
    final /* synthetic */ java.util.function.ToLongFunction a;

    private /* synthetic */ E0(java.util.function.ToLongFunction toLongFunction) {
        this.a = toLongFunction;
    }

    public static /* synthetic */ ToLongFunction a(java.util.function.ToLongFunction toLongFunction) {
        if (toLongFunction == null) {
            return null;
        }
        return toLongFunction instanceof F0 ? ((F0) toLongFunction).a : new E0(toLongFunction);
    }

    @Override // j$.util.function.ToLongFunction
    public /* synthetic */ long applyAsLong(Object obj) {
        return this.a.applyAsLong(obj);
    }
}
