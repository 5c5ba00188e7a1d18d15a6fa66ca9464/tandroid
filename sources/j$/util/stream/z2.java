package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Supplier;
/* loaded from: classes2.dex */
class z2 extends U2 {
    public final /* synthetic */ int b = 3;
    final /* synthetic */ Object c;
    final /* synthetic */ Object d;
    final /* synthetic */ Object e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public z2(e4 e4Var, BiConsumer biConsumer, BiConsumer biConsumer2, Supplier supplier) {
        super(e4Var);
        this.c = biConsumer;
        this.d = biConsumer2;
        this.e = supplier;
    }

    @Override // j$.util.stream.U2
    public S2 a() {
        switch (this.b) {
            case 0:
                return new A2((Supplier) this.e, (j$.util.function.v) this.d, (j$.util.function.b) this.c);
            case 1:
                return new F2((Supplier) this.e, (j$.util.function.t) this.d, (j$.util.function.b) this.c);
            case 2:
                return new G2(this.e, (BiFunction) this.d, (j$.util.function.b) this.c);
            case 3:
                return new K2((Supplier) this.e, (BiConsumer) this.d, (BiConsumer) this.c);
            default:
                return new O2((Supplier) this.e, (j$.util.function.u) this.d, (j$.util.function.b) this.c);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public z2(e4 e4Var, j$.util.function.b bVar, BiFunction biFunction, Object obj) {
        super(e4Var);
        this.c = bVar;
        this.d = biFunction;
        this.e = obj;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public z2(e4 e4Var, j$.util.function.b bVar, j$.util.function.t tVar, Supplier supplier) {
        super(e4Var);
        this.c = bVar;
        this.d = tVar;
        this.e = supplier;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public z2(e4 e4Var, j$.util.function.b bVar, j$.util.function.u uVar, Supplier supplier) {
        super(e4Var);
        this.c = bVar;
        this.d = uVar;
        this.e = supplier;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public z2(e4 e4Var, j$.util.function.b bVar, j$.util.function.v vVar, Supplier supplier) {
        super(e4Var);
        this.c = bVar;
        this.d = vVar;
        this.e = supplier;
    }
}
