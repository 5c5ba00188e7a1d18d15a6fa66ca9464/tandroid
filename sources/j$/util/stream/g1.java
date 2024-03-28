package j$.util.stream;

import j$.util.function.Predicate;
/* loaded from: classes2.dex */
class g1 extends k1 {
    final /* synthetic */ l1 c;
    final /* synthetic */ Predicate d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g1(l1 l1Var, Predicate predicate) {
        super(l1Var);
        this.c = l1Var;
        this.d = predicate;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        boolean z;
        boolean z2;
        if (this.a) {
            return;
        }
        boolean test = this.d.test(obj);
        z = this.c.a;
        if (test == z) {
            this.a = true;
            z2 = this.c.b;
            this.b = z2;
        }
    }
}
