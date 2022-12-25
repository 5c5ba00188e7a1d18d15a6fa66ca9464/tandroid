package j$.wrappers;

import java.util.function.ToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class F0 implements ToLongFunction {
    final /* synthetic */ j$.util.function.A a;

    private /* synthetic */ F0(j$.util.function.A a) {
        this.a = a;
    }

    public static /* synthetic */ ToLongFunction a(j$.util.function.A a) {
        if (a == null) {
            return null;
        }
        return a instanceof E0 ? ((E0) a).a : new F0(a);
    }

    @Override // java.util.function.ToLongFunction
    public /* synthetic */ long applyAsLong(Object obj) {
        return this.a.applyAsLong(obj);
    }
}
