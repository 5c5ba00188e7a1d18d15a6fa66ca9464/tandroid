package j$.wrappers;

import java.util.function.ToDoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class B0 implements ToDoubleFunction {
    final /* synthetic */ j$.util.function.z a;

    private /* synthetic */ B0(j$.util.function.z zVar) {
        this.a = zVar;
    }

    public static /* synthetic */ ToDoubleFunction a(j$.util.function.z zVar) {
        if (zVar == null) {
            return null;
        }
        return zVar instanceof A0 ? ((A0) zVar).a : new B0(zVar);
    }

    @Override // java.util.function.ToDoubleFunction
    public /* synthetic */ double applyAsDouble(Object obj) {
        return this.a.applyAsDouble(obj);
    }
}
