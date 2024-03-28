package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Supplier;
/* loaded from: classes2.dex */
class A2 extends V2 {
    public final /* synthetic */ int b = 3;
    final /* synthetic */ Object c;
    final /* synthetic */ Object d;
    final /* synthetic */ Object e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public A2(f4 f4Var, BiConsumer biConsumer, BiConsumer biConsumer2, Supplier supplier) {
        super(f4Var);
        this.c = biConsumer;
        this.d = biConsumer2;
        this.e = supplier;
    }

    @Override // j$.util.stream.V2
    public T2 a() {
        switch (this.b) {
            case 0:
                return new B2((Supplier) this.e, (j$.util.function.w) this.d, (j$.util.function.b) this.c);
            case 1:
                return new G2((Supplier) this.e, (j$.util.function.u) this.d, (j$.util.function.b) this.c);
            case 2:
                return new H2(this.e, (BiFunction) this.d, (j$.util.function.b) this.c);
            case 3:
                return new L2((Supplier) this.e, (BiConsumer) this.d, (BiConsumer) this.c);
            default:
                return new P2((Supplier) this.e, (j$.util.function.v) this.d, (j$.util.function.b) this.c);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public A2(f4 f4Var, j$.util.function.b bVar, BiFunction biFunction, Object obj) {
        super(f4Var);
        this.c = bVar;
        this.d = biFunction;
        this.e = obj;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public A2(f4 f4Var, j$.util.function.b bVar, j$.util.function.u uVar, Supplier supplier) {
        super(f4Var);
        this.c = bVar;
        this.d = uVar;
        this.e = supplier;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public A2(f4 f4Var, j$.util.function.b bVar, j$.util.function.v vVar, Supplier supplier) {
        super(f4Var);
        this.c = bVar;
        this.d = vVar;
        this.e = supplier;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public A2(f4 f4Var, j$.util.function.b bVar, j$.util.function.w wVar, Supplier supplier) {
        super(f4Var);
        this.c = bVar;
        this.d = wVar;
        this.e = supplier;
    }
}
