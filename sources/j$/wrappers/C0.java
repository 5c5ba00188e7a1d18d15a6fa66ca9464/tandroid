package j$.wrappers;

import j$.util.function.ToIntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class C0 implements ToIntFunction {
    final /* synthetic */ java.util.function.ToIntFunction a;

    private /* synthetic */ C0(java.util.function.ToIntFunction toIntFunction) {
        this.a = toIntFunction;
    }

    public static /* synthetic */ ToIntFunction a(java.util.function.ToIntFunction toIntFunction) {
        if (toIntFunction == null) {
            return null;
        }
        return toIntFunction instanceof D0 ? ((D0) toIntFunction).a : new C0(toIntFunction);
    }

    @Override // j$.util.function.ToIntFunction
    public /* synthetic */ int applyAsInt(Object obj) {
        return this.a.applyAsInt(obj);
    }
}
