package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class o implements j$.util.function.m {
    public final /* synthetic */ Consumer a;

    public /* synthetic */ o(Consumer consumer) {
        this.a = consumer;
    }

    @Override // j$.util.function.m
    public final void accept(double d) {
        this.a.accept(Double.valueOf(d));
    }

    @Override // j$.util.function.m
    public final j$.util.function.m m(j$.util.function.m mVar) {
        mVar.getClass();
        return new j$.util.function.j(this, mVar);
    }
}
