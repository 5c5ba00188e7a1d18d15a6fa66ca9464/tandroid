package j$.util.function;

import java.util.function.DoubleConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class k implements m {
    public final /* synthetic */ DoubleConsumer a;

    private /* synthetic */ k(DoubleConsumer doubleConsumer) {
        this.a = doubleConsumer;
    }

    public static /* synthetic */ m a(DoubleConsumer doubleConsumer) {
        if (doubleConsumer == null) {
            return null;
        }
        return doubleConsumer instanceof l ? ((l) doubleConsumer).a : new k(doubleConsumer);
    }

    @Override // j$.util.function.m
    public final /* synthetic */ void accept(double d) {
        this.a.accept(d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof k) {
            obj = ((k) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.function.m
    public final /* synthetic */ m m(m mVar) {
        return a(this.a.andThen(l.a(mVar)));
    }
}
