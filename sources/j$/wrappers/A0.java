package j$.wrappers;

import j$.util.function.ToDoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class A0 implements ToDoubleFunction {
    final /* synthetic */ java.util.function.ToDoubleFunction a;

    private /* synthetic */ A0(java.util.function.ToDoubleFunction toDoubleFunction) {
        this.a = toDoubleFunction;
    }

    public static /* synthetic */ ToDoubleFunction a(java.util.function.ToDoubleFunction toDoubleFunction) {
        if (toDoubleFunction == null) {
            return null;
        }
        return toDoubleFunction instanceof B0 ? ((B0) toDoubleFunction).a : new A0(toDoubleFunction);
    }

    @Override // j$.util.function.ToDoubleFunction
    public /* synthetic */ double applyAsDouble(Object obj) {
        return this.a.applyAsDouble(obj);
    }
}
