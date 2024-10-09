package j$.util.function;

import java.util.function.LongConsumer;

/* loaded from: classes2.dex */
public final /* synthetic */ class V implements LongConsumer {
    public final /* synthetic */ W a;

    private /* synthetic */ V(W w) {
        this.a = w;
    }

    public static /* synthetic */ LongConsumer a(W w) {
        if (w == null) {
            return null;
        }
        return w instanceof U ? ((U) w).a : new V(w);
    }

    @Override // java.util.function.LongConsumer
    public final /* synthetic */ void accept(long j) {
        this.a.accept(j);
    }

    @Override // java.util.function.LongConsumer
    public final /* synthetic */ LongConsumer andThen(LongConsumer longConsumer) {
        return a(this.a.f(U.a(longConsumer)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        W w = this.a;
        if (obj instanceof V) {
            obj = ((V) obj).a;
        }
        return w.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
