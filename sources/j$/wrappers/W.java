package j$.wrappers;

import java.util.function.IntToDoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class W {
    final /* synthetic */ IntToDoubleFunction a;

    private /* synthetic */ W(IntToDoubleFunction intToDoubleFunction) {
        this.a = intToDoubleFunction;
    }

    public static /* synthetic */ W b(IntToDoubleFunction intToDoubleFunction) {
        if (intToDoubleFunction == null) {
            return null;
        }
        return intToDoubleFunction instanceof X ? ((X) intToDoubleFunction).a : new W(intToDoubleFunction);
    }

    public double a(int i) {
        return this.a.applyAsDouble(i);
    }
}
