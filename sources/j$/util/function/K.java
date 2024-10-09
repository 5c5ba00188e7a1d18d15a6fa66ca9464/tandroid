package j$.util.function;

import java.util.function.IntToDoubleFunction;

/* loaded from: classes2.dex */
public final /* synthetic */ class K {
    public final /* synthetic */ IntToDoubleFunction a;

    private /* synthetic */ K(IntToDoubleFunction intToDoubleFunction) {
        this.a = intToDoubleFunction;
    }

    public static /* synthetic */ K a(IntToDoubleFunction intToDoubleFunction) {
        if (intToDoubleFunction == null) {
            return null;
        }
        return new K(intToDoubleFunction);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        IntToDoubleFunction intToDoubleFunction = this.a;
        if (obj instanceof K) {
            obj = ((K) obj).a;
        }
        return intToDoubleFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
