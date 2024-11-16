package j$.util.stream;

import j$.util.function.Predicate;

/* loaded from: classes2.dex */
final class l0 extends p0 {
    final /* synthetic */ q0 c;
    final /* synthetic */ Predicate d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    l0(Predicate predicate, q0 q0Var) {
        super(q0Var);
        this.c = q0Var;
        this.d = predicate;
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final void r(Object obj) {
        boolean z;
        boolean z2;
        if (this.a) {
            return;
        }
        boolean test = this.d.test(obj);
        q0 q0Var = this.c;
        z = q0Var.a;
        if (test == z) {
            this.a = true;
            z2 = q0Var.b;
            this.b = z2;
        }
    }
}
