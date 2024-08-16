package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class s implements j$.util.function.F {
    public final /* synthetic */ Consumer a;

    public /* synthetic */ s(Consumer consumer) {
        this.a = consumer;
    }

    @Override // j$.util.function.F
    public final void accept(int i) {
        this.a.accept(Integer.valueOf(i));
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }
}
