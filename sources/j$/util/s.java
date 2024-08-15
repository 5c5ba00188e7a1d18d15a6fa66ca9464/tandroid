package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class s implements j$.util.function.K {
    public final /* synthetic */ Consumer a;

    public /* synthetic */ s(Consumer consumer) {
        this.a = consumer;
    }

    @Override // j$.util.function.K
    public final void accept(int i) {
        this.a.accept(Integer.valueOf(i));
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        k.getClass();
        return new j$.util.function.H(this, k);
    }
}
