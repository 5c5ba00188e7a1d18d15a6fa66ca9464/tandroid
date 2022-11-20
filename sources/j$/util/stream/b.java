package j$.util.stream;

import j$.util.function.Consumer;
import java.util.List;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class b implements j$.util.function.y, j$.util.function.r, Consumer, j$.util.function.c {
    public final /* synthetic */ int a = 2;
    public final /* synthetic */ Object b;

    public /* synthetic */ b(j$.util.u uVar) {
        this.b = uVar;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        switch (this.a) {
            case 3:
                ((m3) this.b).accept((m3) obj);
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
                return Objects.requireNonNull(consumer);
            default:
                return Objects.requireNonNull(consumer);
        }
    }

    @Override // j$.util.function.r
    public Object apply(long j) {
        int i = H1.k;
        return x2.d(j, (j$.util.function.m) this.b);
    }

    @Override // j$.util.function.y
    public Object get() {
        switch (this.a) {
            case 0:
                return (j$.util.u) this.b;
            default:
                return ((c) this.b).D0();
        }
    }

    public /* synthetic */ b(j$.util.function.m mVar) {
        this.b = mVar;
    }

    public /* synthetic */ b(c cVar) {
        this.b = cVar;
    }
}
