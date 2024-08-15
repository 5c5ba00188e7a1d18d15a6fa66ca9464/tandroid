package j$.util.function;

import java.util.function.IntToDoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class S implements U {
    public final /* synthetic */ IntToDoubleFunction a;

    private /* synthetic */ S(IntToDoubleFunction intToDoubleFunction) {
        this.a = intToDoubleFunction;
    }

    public static /* synthetic */ U b(IntToDoubleFunction intToDoubleFunction) {
        if (intToDoubleFunction == null) {
            return null;
        }
        return intToDoubleFunction instanceof T ? ((T) intToDoubleFunction).a : new S(intToDoubleFunction);
    }

    public final /* synthetic */ double a(int i) {
        return this.a.applyAsDouble(i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof S) {
            obj = ((S) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
