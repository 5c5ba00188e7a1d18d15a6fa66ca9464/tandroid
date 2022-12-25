package j$.wrappers;

import java.util.function.IntToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class Y implements j$.util.function.n {
    final /* synthetic */ IntToLongFunction a;

    private /* synthetic */ Y(IntToLongFunction intToLongFunction) {
        this.a = intToLongFunction;
    }

    public static /* synthetic */ j$.util.function.n a(IntToLongFunction intToLongFunction) {
        if (intToLongFunction == null) {
            return null;
        }
        return intToLongFunction instanceof Z ? ((Z) intToLongFunction).a : new Y(intToLongFunction);
    }

    @Override // j$.util.function.n
    public /* synthetic */ long applyAsLong(int i) {
        return this.a.applyAsLong(i);
    }
}
