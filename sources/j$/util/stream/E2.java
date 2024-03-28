package j$.util.stream;
/* loaded from: classes2.dex */
class E2 extends V2 {
    public final /* synthetic */ int b = 1;
    final /* synthetic */ Object c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public E2(f4 f4Var, j$.util.function.b bVar) {
        super(f4Var);
        this.c = bVar;
    }

    @Override // j$.util.stream.V2
    public T2 a() {
        switch (this.b) {
            case 0:
                return new F2((j$.util.function.d) this.c);
            case 1:
                return new I2((j$.util.function.b) this.c);
            case 2:
                return new O2((j$.util.function.j) this.c);
            default:
                return new S2((j$.util.function.o) this.c);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public E2(f4 f4Var, j$.util.function.d dVar) {
        super(f4Var);
        this.c = dVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public E2(f4 f4Var, j$.util.function.j jVar) {
        super(f4Var);
        this.c = jVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public E2(f4 f4Var, j$.util.function.o oVar) {
        super(f4Var);
        this.c = oVar;
    }
}
