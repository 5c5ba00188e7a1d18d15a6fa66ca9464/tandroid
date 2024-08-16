package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.ToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class Q1 implements j$.util.function.I, ToLongFunction, Consumer {
    public final /* synthetic */ int a;

    private final void a(Object obj) {
    }

    private final void c(Object obj) {
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        int i = this.a;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 6:
                return Consumer.-CC.$default$andThen(this, consumer);
            default:
                return Consumer.-CC.$default$andThen(this, consumer);
        }
    }

    @Override // j$.util.function.I
    public Object apply(int i) {
        switch (this.a) {
            case 0:
                return new Object[i];
            case 1:
            default:
                return new Double[i];
            case 2:
                return new Object[i];
            case 3:
                return new Integer[i];
            case 4:
                return new Long[i];
        }
    }

    @Override // j$.util.function.ToLongFunction
    public long applyAsLong(Object obj) {
        return 1L;
    }
}
