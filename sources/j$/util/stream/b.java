package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;
import java.util.List;
/* loaded from: classes2.dex */
public final /* synthetic */ class b implements Supplier, j$.util.function.r, Consumer, j$.util.function.c {
    public final /* synthetic */ int a = 2;
    public final /* synthetic */ Object b;

    public /* synthetic */ b(j$.util.t tVar) {
        this.b = tVar;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        switch (this.a) {
            case 3:
                ((n3) this.b).accept((n3) obj);
                return;
            default:
                ((List) this.b).add(obj);
                return;
        }
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 3:
                return Consumer.-CC.$default$andThen(this, consumer);
            default:
                return Consumer.-CC.$default$andThen(this, consumer);
        }
    }

    @Override // j$.util.function.r
    public Object apply(long j) {
        int i = I1.k;
        return y2.d(j, (j$.util.function.m) this.b);
    }

    @Override // j$.util.function.Supplier
    public Object get() {
        switch (this.a) {
            case 0:
                return (j$.util.t) this.b;
            default:
                return ((c) this.b).B0();
        }
    }

    public /* synthetic */ b(c cVar) {
        this.b = cVar;
    }
}
