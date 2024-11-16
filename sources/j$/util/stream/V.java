package j$.util.stream;

/* loaded from: classes2.dex */
final class V extends Y1 {
    public final /* synthetic */ int b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ V(int i, e2 e2Var) {
        super(e2Var);
        this.b = i;
    }

    @Override // j$.util.stream.e2
    public final void accept(int i) {
        switch (this.b) {
            case 0:
                this.a.accept(i);
                break;
            default:
                this.a.accept(i);
                break;
        }
    }
}
