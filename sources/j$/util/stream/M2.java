package j$.util.stream;
/* loaded from: classes2.dex */
class M2 extends V2 {
    final /* synthetic */ j$.util.function.j b;
    final /* synthetic */ int c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public M2(f4 f4Var, j$.util.function.j jVar, int i) {
        super(f4Var);
        this.b = jVar;
        this.c = i;
    }

    @Override // j$.util.stream.V2
    public T2 a() {
        return new N2(this.c, this.b);
    }
}
