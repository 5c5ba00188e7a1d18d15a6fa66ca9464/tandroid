package j$.wrappers;

import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class P implements j$.util.function.l {
    final /* synthetic */ IntConsumer a;

    private /* synthetic */ P(IntConsumer intConsumer) {
        this.a = intConsumer;
    }

    public static /* synthetic */ j$.util.function.l b(IntConsumer intConsumer) {
        if (intConsumer == null) {
            return null;
        }
        return intConsumer instanceof Q ? ((Q) intConsumer).a : new P(intConsumer);
    }

    @Override // j$.util.function.l
    public /* synthetic */ void accept(int i) {
        this.a.accept(i);
    }

    @Override // j$.util.function.l
    public /* synthetic */ j$.util.function.l l(j$.util.function.l lVar) {
        return b(this.a.andThen(Q.a(lVar)));
    }
}
