package j$.util.function;

import java.util.function.IntToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class L implements N {
    public final /* synthetic */ IntToLongFunction a;

    private /* synthetic */ L(IntToLongFunction intToLongFunction) {
        this.a = intToLongFunction;
    }

    public static /* synthetic */ N a(IntToLongFunction intToLongFunction) {
        if (intToLongFunction == null) {
            return null;
        }
        return intToLongFunction instanceof M ? ((M) intToLongFunction).a : new L(intToLongFunction);
    }

    @Override // j$.util.function.N
    public final /* synthetic */ long applyAsLong(int i) {
        return this.a.applyAsLong(i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        IntToLongFunction intToLongFunction = this.a;
        if (obj instanceof L) {
            obj = ((L) obj).a;
        }
        return intToLongFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
