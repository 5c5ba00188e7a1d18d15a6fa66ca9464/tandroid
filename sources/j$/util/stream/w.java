package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class w extends c0 {
    public final /* synthetic */ int l;
    final /* synthetic */ Object m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ w(c cVar, int i, Object obj, int i2) {
        super(cVar, i);
        this.l = i2;
        this.m = obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final f2 l1(int i, f2 f2Var) {
        switch (this.l) {
            case 0:
                return new t(this, f2Var, 2);
            case 1:
                return new X(this, f2Var, 1);
            case 2:
                return new X(this, f2Var, 3);
            case 3:
                return new X(this, f2Var, 7);
            case 4:
                return new X(this, f2Var, 8);
            case 5:
                return new f0(this, f2Var, 3);
            case 6:
                return new R1(this, f2Var, 3);
            default:
                return new p(this, f2Var);
        }
    }
}
