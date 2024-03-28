package j$.util.stream;
/* loaded from: classes2.dex */
class Q2 extends V2 {
    final /* synthetic */ j$.util.function.o b;
    final /* synthetic */ long c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Q2(f4 f4Var, j$.util.function.o oVar, long j) {
        super(f4Var);
        this.b = oVar;
        this.c = j;
    }

    @Override // j$.util.stream.V2
    public T2 a() {
        return new R2(this.c, this.b);
    }
}
