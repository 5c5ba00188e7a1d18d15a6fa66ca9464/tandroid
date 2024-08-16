package j$.util.function;

import java.util.function.DoubleConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class m implements DoubleConsumer {
    public final /* synthetic */ n a;

    private /* synthetic */ m(n nVar) {
        this.a = nVar;
    }

    public static /* synthetic */ DoubleConsumer a(n nVar) {
        if (nVar == null) {
            return null;
        }
        return nVar instanceof l ? ((l) nVar).a : new m(nVar);
    }

    @Override // java.util.function.DoubleConsumer
    public final /* synthetic */ void accept(double d) {
        this.a.accept(d);
    }

    @Override // java.util.function.DoubleConsumer
    public final /* synthetic */ DoubleConsumer andThen(DoubleConsumer doubleConsumer) {
        return a(this.a.k(l.a(doubleConsumer)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        n nVar = this.a;
        if (obj instanceof m) {
            obj = ((m) obj).a;
        }
        return nVar.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
