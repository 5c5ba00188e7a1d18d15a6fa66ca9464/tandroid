package j$.wrappers;

import java.util.function.ToIntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class D0 implements ToIntFunction {
    final /* synthetic */ j$.util.function.ToIntFunction a;

    private /* synthetic */ D0(j$.util.function.ToIntFunction toIntFunction) {
        this.a = toIntFunction;
    }

    public static /* synthetic */ ToIntFunction a(j$.util.function.ToIntFunction toIntFunction) {
        if (toIntFunction == null) {
            return null;
        }
        return toIntFunction instanceof C0 ? ((C0) toIntFunction).a : new D0(toIntFunction);
    }

    @Override // java.util.function.ToIntFunction
    public /* synthetic */ int applyAsInt(Object obj) {
        return this.a.applyAsInt(obj);
    }
}
