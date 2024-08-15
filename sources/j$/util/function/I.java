package j$.util.function;

import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class I implements K {
    public final /* synthetic */ IntConsumer a;

    private /* synthetic */ I(IntConsumer intConsumer) {
        this.a = intConsumer;
    }

    public static /* synthetic */ K a(IntConsumer intConsumer) {
        if (intConsumer == null) {
            return null;
        }
        return intConsumer instanceof J ? ((J) intConsumer).a : new I(intConsumer);
    }

    @Override // j$.util.function.K
    public final /* synthetic */ void accept(int i) {
        this.a.accept(i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof I) {
            obj = ((I) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.function.K
    public final /* synthetic */ K n(K k) {
        return a(this.a.andThen(J.a(k)));
    }
}
