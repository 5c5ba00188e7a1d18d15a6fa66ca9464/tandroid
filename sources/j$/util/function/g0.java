package j$.util.function;

import java.util.function.LongConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class g0 implements LongConsumer {
    public final /* synthetic */ h0 a;

    private /* synthetic */ g0(h0 h0Var) {
        this.a = h0Var;
    }

    public static /* synthetic */ LongConsumer a(h0 h0Var) {
        if (h0Var == null) {
            return null;
        }
        return h0Var instanceof f0 ? ((f0) h0Var).a : new g0(h0Var);
    }

    @Override // java.util.function.LongConsumer
    public final /* synthetic */ void accept(long j) {
        this.a.accept(j);
    }

    @Override // java.util.function.LongConsumer
    public final /* synthetic */ LongConsumer andThen(LongConsumer longConsumer) {
        return a(this.a.i(f0.a(longConsumer)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        h0 h0Var = this.a;
        if (obj instanceof g0) {
            obj = ((g0) obj).a;
        }
        return h0Var.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
