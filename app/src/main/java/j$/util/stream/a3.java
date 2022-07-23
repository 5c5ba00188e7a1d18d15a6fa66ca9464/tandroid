package j$.util.stream;

import j$.util.function.Function;
/* loaded from: classes2.dex */
class a3 extends d3 {
    public final /* synthetic */ int l;
    final /* synthetic */ Function m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a3(e3 e3Var, c cVar, e4 e4Var, int i, Function function, int i2) {
        super(cVar, e4Var, i);
        this.l = i2;
        if (i2 != 1) {
            this.m = function;
            return;
        }
        this.m = function;
        super(cVar, e4Var, i);
    }

    @Override // j$.util.stream.c
    public m3 H0(int i, m3 m3Var) {
        switch (this.l) {
            case 0:
                return new Y2(this, m3Var);
            default:
                return new Y2(this, m3Var, (j$.lang.a) null);
        }
    }
}
