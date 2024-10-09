package j$.util.function;

import java.util.function.DoubleConsumer;

/* loaded from: classes2.dex */
public final /* synthetic */ class l implements n {
    public final /* synthetic */ DoubleConsumer a;

    private /* synthetic */ l(DoubleConsumer doubleConsumer) {
        this.a = doubleConsumer;
    }

    public static /* synthetic */ n a(DoubleConsumer doubleConsumer) {
        if (doubleConsumer == null) {
            return null;
        }
        return doubleConsumer instanceof m ? ((m) doubleConsumer).a : new l(doubleConsumer);
    }

    @Override // j$.util.function.n
    public final /* synthetic */ void accept(double d) {
        this.a.accept(d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        DoubleConsumer doubleConsumer = this.a;
        if (obj instanceof l) {
            obj = ((l) obj).a;
        }
        return doubleConsumer.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.function.n
    public final /* synthetic */ n k(n nVar) {
        return a(this.a.andThen(m.a(nVar)));
    }
}
