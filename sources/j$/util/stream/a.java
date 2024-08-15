package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;
import java.util.List;
/* loaded from: classes2.dex */
public final /* synthetic */ class a implements Supplier, LongFunction, Consumer {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;

    public /* synthetic */ a(Object obj, int i) {
        this.a = i;
        this.b = obj;
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        int i = this.a;
        Object obj2 = this.b;
        switch (i) {
            case 3:
                ((f2) obj2).accept((f2) obj);
                return;
            default:
                ((List) obj2).add(obj);
                return;
        }
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 3:
                return Consumer.-CC.$default$andThen(this, consumer);
            default:
                return Consumer.-CC.$default$andThen(this, consumer);
        }
    }

    @Override // j$.util.function.LongFunction
    public final Object apply(long j) {
        int i = I0.k;
        return u1.g(j, (j$.util.function.N) this.b);
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        int i = this.a;
        Object obj = this.b;
        switch (i) {
            case 0:
                return (j$.util.Q) obj;
            default:
                return ((c) obj).h1();
        }
    }
}
