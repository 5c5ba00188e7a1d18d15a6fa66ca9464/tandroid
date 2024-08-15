package j$.util.function;

import java.util.function.DoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class n implements p {
    public final /* synthetic */ DoubleFunction a;

    private /* synthetic */ n(DoubleFunction doubleFunction) {
        this.a = doubleFunction;
    }

    public static /* synthetic */ p a(DoubleFunction doubleFunction) {
        if (doubleFunction == null) {
            return null;
        }
        return doubleFunction instanceof o ? ((o) doubleFunction).a : new n(doubleFunction);
    }

    @Override // j$.util.function.p
    public final /* synthetic */ Object apply(double d) {
        return this.a.apply(d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof n) {
            obj = ((n) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
