package j$.util.function;

import java.util.function.IntToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class V implements X {
    public final /* synthetic */ IntToLongFunction a;

    private /* synthetic */ V(IntToLongFunction intToLongFunction) {
        this.a = intToLongFunction;
    }

    public static /* synthetic */ X a(IntToLongFunction intToLongFunction) {
        if (intToLongFunction == null) {
            return null;
        }
        return intToLongFunction instanceof W ? ((W) intToLongFunction).a : new V(intToLongFunction);
    }

    @Override // j$.util.function.X
    public final /* synthetic */ long applyAsLong(int i) {
        return this.a.applyAsLong(i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof V) {
            obj = ((V) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
