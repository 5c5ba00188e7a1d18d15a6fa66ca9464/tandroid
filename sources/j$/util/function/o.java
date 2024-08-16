package j$.util.function;

import java.util.function.DoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class o implements q {
    public final /* synthetic */ DoubleFunction a;

    private /* synthetic */ o(DoubleFunction doubleFunction) {
        this.a = doubleFunction;
    }

    public static /* synthetic */ q a(DoubleFunction doubleFunction) {
        if (doubleFunction == null) {
            return null;
        }
        return doubleFunction instanceof p ? ((p) doubleFunction).a : new o(doubleFunction);
    }

    @Override // j$.util.function.q
    public final /* synthetic */ Object apply(double d) {
        return this.a.apply(d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        DoubleFunction doubleFunction = this.a;
        if (obj instanceof o) {
            obj = ((o) obj).a;
        }
        return doubleFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
