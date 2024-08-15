package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class L1 extends u1 {
    final /* synthetic */ j$.util.function.d0 h;
    final /* synthetic */ long i;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L1(U2 u2, j$.util.function.d0 d0Var, long j) {
        super(u2);
        this.h = d0Var;
        this.i = j;
    }

    @Override // j$.util.stream.u1
    public final O1 u() {
        return new M1(this.i, this.h);
    }
}
