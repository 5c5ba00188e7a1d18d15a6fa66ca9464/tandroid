package j$.wrappers;

import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class Q implements IntConsumer {
    final /* synthetic */ j$.util.function.l a;

    private /* synthetic */ Q(j$.util.function.l lVar) {
        this.a = lVar;
    }

    public static /* synthetic */ IntConsumer a(j$.util.function.l lVar) {
        if (lVar == null) {
            return null;
        }
        return lVar instanceof P ? ((P) lVar).a : new Q(lVar);
    }

    @Override // java.util.function.IntConsumer
    public /* synthetic */ void accept(int i) {
        this.a.accept(i);
    }

    @Override // java.util.function.IntConsumer
    public /* synthetic */ IntConsumer andThen(IntConsumer intConsumer) {
        return a(this.a.l(P.b(intConsumer)));
    }
}
