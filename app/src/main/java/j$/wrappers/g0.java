package j$.wrappers;

import java.util.function.LongConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class g0 implements LongConsumer {
    final /* synthetic */ j$.util.function.q a;

    private /* synthetic */ g0(j$.util.function.q qVar) {
        this.a = qVar;
    }

    public static /* synthetic */ LongConsumer a(j$.util.function.q qVar) {
        if (qVar == null) {
            return null;
        }
        return qVar instanceof f0 ? ((f0) qVar).a : new g0(qVar);
    }

    @Override // java.util.function.LongConsumer
    public /* synthetic */ void accept(long j) {
        this.a.accept(j);
    }

    @Override // java.util.function.LongConsumer
    public /* synthetic */ LongConsumer andThen(LongConsumer longConsumer) {
        return a(this.a.f(f0.b(longConsumer)));
    }
}
