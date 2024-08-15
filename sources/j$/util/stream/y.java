package j$.util.stream;
/* loaded from: classes2.dex */
final class y extends B {
    public final /* synthetic */ int l;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ y(c cVar, int i, int i2) {
        super(cVar, i);
        this.l = i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final f2 l1(int i, f2 f2Var) {
        switch (this.l) {
            case 0:
                return f2Var;
            case 1:
                return new X(this, f2Var, 2);
            default:
                return new f0(this, f2Var, 0);
        }
    }
}
