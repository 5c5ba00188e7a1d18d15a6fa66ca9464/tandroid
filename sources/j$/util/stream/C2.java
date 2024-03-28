package j$.util.stream;
/* loaded from: classes2.dex */
class C2 extends V2 {
    final /* synthetic */ j$.util.function.d b;
    final /* synthetic */ double c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C2(f4 f4Var, j$.util.function.d dVar, double d) {
        super(f4Var);
        this.b = dVar;
        this.c = d;
    }

    @Override // j$.util.stream.V2
    public T2 a() {
        return new D2(this.c, this.b);
    }
}
