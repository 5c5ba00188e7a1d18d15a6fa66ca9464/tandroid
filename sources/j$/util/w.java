package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class w implements j$.util.function.h0 {
    public final /* synthetic */ Consumer a;

    public /* synthetic */ w(Consumer consumer) {
        this.a = consumer;
    }

    @Override // j$.util.function.h0
    public final void accept(long j) {
        this.a.accept(Long.valueOf(j));
    }

    @Override // j$.util.function.h0
    public final j$.util.function.h0 i(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        return new j$.util.function.e0(this, h0Var);
    }
}
