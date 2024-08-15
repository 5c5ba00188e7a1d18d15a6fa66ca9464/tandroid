package j$.util.stream;

import j$.util.function.Function;
/* loaded from: classes2.dex */
final class T1 extends W1 {
    public final /* synthetic */ int l;
    final /* synthetic */ Function m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ T1(c cVar, int i, Function function, int i2) {
        super(cVar, i);
        this.l = i2;
        this.m = function;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final f2 l1(int i, f2 f2Var) {
        switch (this.l) {
            case 0:
                return new R1(this, f2Var, 2);
            default:
                return new R1(this, f2Var, 6);
        }
    }
}
