package j$.util.function;

import java.util.function.DoubleBinaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class h implements DoubleBinaryOperator {
    public final /* synthetic */ i a;

    private /* synthetic */ h(i iVar) {
        this.a = iVar;
    }

    public static /* synthetic */ DoubleBinaryOperator a(i iVar) {
        if (iVar == null) {
            return null;
        }
        return iVar instanceof g ? ((g) iVar).a : new h(iVar);
    }

    @Override // java.util.function.DoubleBinaryOperator
    public final /* synthetic */ double applyAsDouble(double d, double d2) {
        return this.a.applyAsDouble(d, d2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        i iVar = this.a;
        if (obj instanceof h) {
            obj = ((h) obj).a;
        }
        return iVar.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
