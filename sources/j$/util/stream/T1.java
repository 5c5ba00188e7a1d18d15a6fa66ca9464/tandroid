package j$.util.stream;

import j$.util.function.Function;
/* loaded from: classes2.dex */
final class T1 extends V1 {
    public final /* synthetic */ int m;
    final /* synthetic */ Function n;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ T1(b bVar, int i, Function function, int i2) {
        super(bVar, i, 1);
        this.m = i2;
        this.n = function;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.b
    public final e2 z0(int i, e2 e2Var) {
        switch (this.m) {
            case 0:
                return new o(this, e2Var, 3);
            default:
                return new o(this, e2Var, 7);
        }
    }
}
