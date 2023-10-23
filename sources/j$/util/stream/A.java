package j$.util.stream;

import j$.util.function.BiConsumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class A implements BiConsumer {
    public static final /* synthetic */ A a = new A();

    private /* synthetic */ A() {
    }

    @Override // j$.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        double[] dArr = (double[]) obj;
        double[] dArr2 = (double[]) obj2;
        l.b(dArr, dArr2[0]);
        l.b(dArr, dArr2[1]);
        dArr[2] = dArr[2] + dArr2[2];
        dArr[3] = dArr[3] + dArr2[3];
    }

    @Override // j$.util.function.BiConsumer
    public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
        return Objects.requireNonNull(biConsumer);
    }
}
