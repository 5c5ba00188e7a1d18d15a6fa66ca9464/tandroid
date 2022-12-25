package j$.wrappers;

import java.util.function.IntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class S implements j$.util.function.m {
    final /* synthetic */ IntFunction a;

    private /* synthetic */ S(IntFunction intFunction) {
        this.a = intFunction;
    }

    public static /* synthetic */ j$.util.function.m a(IntFunction intFunction) {
        if (intFunction == null) {
            return null;
        }
        return intFunction instanceof T ? ((T) intFunction).a : new S(intFunction);
    }

    @Override // j$.util.function.m
    public /* synthetic */ Object apply(int i) {
        return this.a.apply(i);
    }
}
