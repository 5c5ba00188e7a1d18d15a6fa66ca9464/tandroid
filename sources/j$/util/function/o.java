package j$.util.function;

import java.util.function.DoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class o implements DoubleFunction {
    public final /* synthetic */ p a;

    private /* synthetic */ o(p pVar) {
        this.a = pVar;
    }

    public static /* synthetic */ DoubleFunction a(p pVar) {
        if (pVar == null) {
            return null;
        }
        return pVar instanceof n ? ((n) pVar).a : new o(pVar);
    }

    @Override // java.util.function.DoubleFunction
    public final /* synthetic */ Object apply(double d) {
        return this.a.apply(d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        p pVar = this.a;
        if (obj instanceof o) {
            obj = ((o) obj).a;
        }
        return pVar.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
