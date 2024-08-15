package j$.util.function;

import java.util.function.LongConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class f0 implements h0 {
    public final /* synthetic */ LongConsumer a;

    private /* synthetic */ f0(LongConsumer longConsumer) {
        this.a = longConsumer;
    }

    public static /* synthetic */ h0 a(LongConsumer longConsumer) {
        if (longConsumer == null) {
            return null;
        }
        return longConsumer instanceof g0 ? ((g0) longConsumer).a : new f0(longConsumer);
    }

    @Override // j$.util.function.h0
    public final /* synthetic */ void accept(long j) {
        this.a.accept(j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof f0) {
            obj = ((f0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.function.h0
    public final /* synthetic */ h0 i(h0 h0Var) {
        return a(this.a.andThen(g0.a(h0Var)));
    }
}
