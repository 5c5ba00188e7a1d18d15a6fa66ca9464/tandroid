package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class o implements j$.util.function.n {
    public final /* synthetic */ Consumer a;

    public /* synthetic */ o(Consumer consumer) {
        this.a = consumer;
    }

    @Override // j$.util.function.n
    public final void accept(double d) {
        this.a.accept(Double.valueOf(d));
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        return j$.com.android.tools.r8.a.b(this, nVar);
    }
}
