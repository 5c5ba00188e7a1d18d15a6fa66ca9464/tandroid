package j$.util.function;

import java.util.function.LongToDoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class n0 implements p0 {
    public final /* synthetic */ LongToDoubleFunction a;

    private /* synthetic */ n0(LongToDoubleFunction longToDoubleFunction) {
        this.a = longToDoubleFunction;
    }

    public static /* synthetic */ p0 b(LongToDoubleFunction longToDoubleFunction) {
        if (longToDoubleFunction == null) {
            return null;
        }
        return longToDoubleFunction instanceof o0 ? ((o0) longToDoubleFunction).a : new n0(longToDoubleFunction);
    }

    public final /* synthetic */ double a(long j) {
        return this.a.applyAsDouble(j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof n0) {
            obj = ((n0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
