package j$.util.function;

import java.util.function.DoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class p implements DoubleFunction {
    public final /* synthetic */ q a;

    private /* synthetic */ p(q qVar) {
        this.a = qVar;
    }

    public static /* synthetic */ DoubleFunction a(q qVar) {
        if (qVar == null) {
            return null;
        }
        return qVar instanceof o ? ((o) qVar).a : new p(qVar);
    }

    @Override // java.util.function.DoubleFunction
    public final /* synthetic */ Object apply(double d) {
        return this.a.apply(d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        q qVar = this.a;
        if (obj instanceof p) {
            obj = ((p) obj).a;
        }
        return qVar.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
