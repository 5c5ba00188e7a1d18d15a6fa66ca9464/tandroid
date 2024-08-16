package j$.util.function;

import java.util.function.DoubleBinaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class i implements DoubleBinaryOperator {
    public final /* synthetic */ j a;

    private /* synthetic */ i(j jVar) {
        this.a = jVar;
    }

    public static /* synthetic */ DoubleBinaryOperator a(j jVar) {
        if (jVar == null) {
            return null;
        }
        return jVar instanceof h ? ((h) jVar).a : new i(jVar);
    }

    @Override // java.util.function.DoubleBinaryOperator
    public final /* synthetic */ double applyAsDouble(double d, double d2) {
        return this.a.applyAsDouble(d, d2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        j jVar = this.a;
        if (obj instanceof i) {
            obj = ((i) obj).a;
        }
        return jVar.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
