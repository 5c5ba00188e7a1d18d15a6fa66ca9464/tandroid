package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.function.ToDoubleFunction;
import j$.util.function.ToIntFunction;
import j$.util.function.ToLongFunction;
/* loaded from: classes2.dex */
final class R1 extends b2 {
    public final /* synthetic */ int b;
    final /* synthetic */ c c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ R1(c cVar, f2 f2Var, int i) {
        super(f2Var);
        this.b = i;
        this.c = cVar;
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        int i = this.b;
        f2 f2Var = this.a;
        c cVar = this.c;
        switch (i) {
            case 0:
                ((Consumer) ((v) cVar).m).accept(obj);
                f2Var.accept((f2) obj);
                return;
            case 1:
                if (((Predicate) ((v) cVar).m).test(obj)) {
                    f2Var.accept((f2) obj);
                    return;
                }
                return;
            case 2:
                f2Var.accept((f2) ((T1) cVar).m.apply(obj));
                return;
            case 3:
                f2Var.accept(((ToIntFunction) ((w) cVar).m).applyAsInt(obj));
                return;
            case 4:
                f2Var.accept(((ToLongFunction) ((x) cVar).m).applyAsLong(obj));
                return;
            case 5:
                f2Var.accept(((ToDoubleFunction) ((u) cVar).m).applyAsDouble(obj));
                return;
            default:
                Stream stream = (Stream) ((T1) cVar).m.apply(obj);
                if (stream != null) {
                    try {
                        ((Stream) stream.sequential()).forEach(f2Var);
                    } catch (Throwable th) {
                        try {
                            stream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (stream != null) {
                    stream.close();
                    return;
                }
                return;
        }
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        int i = this.b;
        f2 f2Var = this.a;
        switch (i) {
            case 1:
                f2Var.f(-1L);
                return;
            case 6:
                f2Var.f(-1L);
                return;
            default:
                f2Var.f(j);
                return;
        }
    }
}
