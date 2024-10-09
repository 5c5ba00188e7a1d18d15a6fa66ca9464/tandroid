package j$.util;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
public final /* synthetic */ class w implements j$.util.function.W {
    public final /* synthetic */ Consumer a;

    public /* synthetic */ w(Consumer consumer) {
        this.a = consumer;
    }

    @Override // j$.util.function.W
    public final void accept(long j) {
        this.a.r(Long.valueOf(j));
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        return j$.com.android.tools.r8.a.d(this, w);
    }
}
