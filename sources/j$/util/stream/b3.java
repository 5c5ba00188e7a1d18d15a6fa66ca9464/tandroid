package j$.util.stream;

import j$.util.function.Function;
/* loaded from: classes2.dex */
class b3 extends e3 {
    public final /* synthetic */ int l;
    final /* synthetic */ Function m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public b3(f3 f3Var, c cVar, f4 f4Var, int i, Function function, int i2) {
        super(cVar, f4Var, i);
        this.l = i2;
        if (i2 != 1) {
            this.m = function;
            return;
        }
        this.m = function;
        super(cVar, f4Var, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public n3 F0(int i, n3 n3Var) {
        switch (this.l) {
            case 0:
                return new Z2(this, n3Var);
            default:
                return new Z2(this, n3Var, (j$.lang.a) null);
        }
    }
}
