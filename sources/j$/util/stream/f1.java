package j$.util.stream;

import j$.util.function.Predicate;
/* loaded from: classes2.dex */
class f1 extends j1 {
    final /* synthetic */ k1 c;
    final /* synthetic */ Predicate d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public f1(k1 k1Var, Predicate predicate) {
        super(k1Var);
        this.c = k1Var;
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
