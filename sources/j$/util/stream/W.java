package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Consumer;
import j$.util.function.LongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class W implements j$.util.function.G, j$.util.function.d0, LongFunction, j$.util.function.F0, BiConsumer, Consumer {
    public final /* synthetic */ int a;

    public /* synthetic */ W(int i) {
        this.a = i;
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
    }

    @Override // j$.util.function.F0
    public final void accept(Object obj, long j) {
        ((j$.util.j) obj).accept(j);
    }

    @Override // j$.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        ((j$.util.j) obj).a((j$.util.j) obj2);
    }

    @Override // j$.util.function.BiConsumer
    public final /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
        return BiConsumer.-CC.$default$andThen(this, biConsumer);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.LongFunction
    public final Object apply(long j) {
        return Long.valueOf(j);
    }

    @Override // j$.util.function.G
    public final int applyAsInt(int i, int i2) {
        return Math.max(i, i2);
    }

    @Override // j$.util.function.d0
    public final long applyAsLong(long j, long j2) {
        switch (this.a) {
            case 1:
                return Math.min(j, j2);
            case 2:
                return Math.max(j, j2);
            default:
                return j + j2;
        }
    }
}
