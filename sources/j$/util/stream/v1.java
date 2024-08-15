package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Supplier;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class v1 extends u1 {
    public final /* synthetic */ int h;
    final /* synthetic */ Object i;
    final /* synthetic */ Object j;
    final /* synthetic */ Object k;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ v1(U2 u2, Object obj, Object obj2, Object obj3, int i) {
        super(u2);
        this.h = i;
        this.i = obj;
        this.k = obj2;
        this.j = obj3;
    }

    @Override // j$.util.stream.u1
    public final O1 u() {
        int i = this.h;
        Object obj = this.i;
        Object obj2 = this.k;
        Object obj3 = this.j;
        switch (i) {
            case 0:
                return new w1((Supplier) obj3, (j$.util.function.F0) obj2, (j$.util.function.f) obj);
            case 1:
                return new B1((Supplier) obj3, (j$.util.function.z0) obj2, (j$.util.function.f) obj);
            case 2:
                return new C1(obj3, (BiFunction) obj2, (j$.util.function.f) obj);
            case 3:
                return new G1((Supplier) obj3, (BiConsumer) obj2, (BiConsumer) obj);
            default:
                return new K1((Supplier) obj3, (j$.util.function.C0) obj2, (j$.util.function.f) obj);
        }
    }
}
