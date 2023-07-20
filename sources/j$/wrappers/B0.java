package j$.wrappers;

import java.util.function.ToDoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class B0 implements ToDoubleFunction {
    final /* synthetic */ j$.util.function.ToDoubleFunction a;

    private /* synthetic */ B0(j$.util.function.ToDoubleFunction toDoubleFunction) {
        this.a = toDoubleFunction;
    }

    public static /* synthetic */ ToDoubleFunction a(j$.util.function.ToDoubleFunction toDoubleFunction) {
        if (toDoubleFunction == null) {
            return null;
        }
        return toDoubleFunction instanceof A0 ? ((A0) toDoubleFunction).a : new B0(toDoubleFunction);
    }

    @Override // java.util.function.ToDoubleFunction
    public /* synthetic */ double applyAsDouble(Object obj) {
        return this.a.applyAsDouble(obj);
    }
}
