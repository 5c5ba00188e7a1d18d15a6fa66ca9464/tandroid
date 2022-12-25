package j$.wrappers;

import java.util.function.ToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class E0 implements j$.util.function.A {
    final /* synthetic */ ToLongFunction a;

    private /* synthetic */ E0(ToLongFunction toLongFunction) {
        this.a = toLongFunction;
    }

    public static /* synthetic */ j$.util.function.A a(ToLongFunction toLongFunction) {
        if (toLongFunction == null) {
            return null;
        }
        return toLongFunction instanceof F0 ? ((F0) toLongFunction).a : new E0(toLongFunction);
    }

    @Override // j$.util.function.A
    public /* synthetic */ long applyAsLong(Object obj) {
        return this.a.applyAsLong(obj);
    }
}
