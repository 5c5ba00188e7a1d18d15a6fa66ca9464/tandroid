package j$.util.function;

import java.util.function.DoubleConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class l implements DoubleConsumer {
    public final /* synthetic */ m a;

    private /* synthetic */ l(m mVar) {
        this.a = mVar;
    }

    public static /* synthetic */ DoubleConsumer a(m mVar) {
        if (mVar == null) {
            return null;
        }
        return mVar instanceof k ? ((k) mVar).a : new l(mVar);
    }

    @Override // java.util.function.DoubleConsumer
    public final /* synthetic */ void accept(double d) {
        this.a.accept(d);
    }

    @Override // java.util.function.DoubleConsumer
    public final /* synthetic */ DoubleConsumer andThen(DoubleConsumer doubleConsumer) {
        return a(this.a.m(k.a(doubleConsumer)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        m mVar = this.a;
        if (obj instanceof l) {
            obj = ((l) obj).a;
        }
        return mVar.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
