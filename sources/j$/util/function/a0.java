package j$.util.function;

import java.util.function.LongToDoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class a0 {
    public final /* synthetic */ LongToDoubleFunction a;

    private /* synthetic */ a0(LongToDoubleFunction longToDoubleFunction) {
        this.a = longToDoubleFunction;
    }

    public static /* synthetic */ a0 a(LongToDoubleFunction longToDoubleFunction) {
        if (longToDoubleFunction == null) {
            return null;
        }
        return new a0(longToDoubleFunction);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        LongToDoubleFunction longToDoubleFunction = this.a;
        if (obj instanceof a0) {
            obj = ((a0) obj).a;
        }
        return longToDoubleFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
