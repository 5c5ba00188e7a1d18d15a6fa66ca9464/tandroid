package j$.util.function;

import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class D implements F {
    public final /* synthetic */ IntConsumer a;

    private /* synthetic */ D(IntConsumer intConsumer) {
        this.a = intConsumer;
    }

    public static /* synthetic */ F a(IntConsumer intConsumer) {
        if (intConsumer == null) {
            return null;
        }
        return intConsumer instanceof E ? ((E) intConsumer).a : new D(intConsumer);
    }

    @Override // j$.util.function.F
    public final /* synthetic */ void accept(int i) {
        this.a.accept(i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        IntConsumer intConsumer = this.a;
        if (obj instanceof D) {
            obj = ((D) obj).a;
        }
        return intConsumer.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.function.F
    public final /* synthetic */ F l(F f) {
        return a(this.a.andThen(E.a(f)));
    }
}
