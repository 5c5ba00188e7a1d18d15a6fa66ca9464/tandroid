package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.function.ToDoubleFunction;
import j$.util.function.ToIntFunction;
import j$.util.function.ToLongFunction;
import java.util.HashSet;
/* loaded from: classes2.dex */
final class o extends a2 {
    public final /* synthetic */ int b;
    Object c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ o(b bVar, e2 e2Var, int i) {
        super(e2Var);
        this.b = i;
        this.c = bVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public o(e2 e2Var) {
        super(e2Var);
        this.b = 0;
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.b) {
            case 0:
                if (((HashSet) this.c).contains(obj)) {
                    return;
                }
                ((HashSet) this.c).add(obj);
                this.a.accept((e2) obj);
                return;
            case 1:
                ((Consumer) ((u) this.c).n).accept(obj);
                this.a.accept((e2) obj);
                return;
            case 2:
                if (((Predicate) ((u) this.c).n).test(obj)) {
                    this.a.accept((e2) obj);
                    return;
                }
                return;
            case 3:
                this.a.accept((e2) ((T1) this.c).n.apply(obj));
                return;
            case 4:
                this.a.accept(((ToIntFunction) ((v) this.c).n).applyAsInt(obj));
                return;
            case 5:
                this.a.accept(((ToLongFunction) ((w) this.c).n).applyAsLong(obj));
                return;
            case 6:
                this.a.accept(((ToDoubleFunction) ((t) this.c).n).applyAsDouble(obj));
                return;
            default:
                Stream stream = (Stream) ((T1) this.c).n.apply(obj);
                if (stream != null) {
                    try {
                        ((Stream) stream.sequential()).forEach(this.a);
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

    @Override // j$.util.stream.a2, j$.util.stream.e2
    public void m() {
        switch (this.b) {
            case 0:
                this.c = null;
                this.a.m();
                return;
            default:
                super.m();
                return;
        }
    }

    @Override // j$.util.stream.a2, j$.util.stream.e2
    public void n(long j) {
        switch (this.b) {
            case 0:
                this.c = new HashSet();
                this.a.n(-1L);
                return;
            case 2:
                this.a.n(-1L);
                return;
            case 7:
                this.a.n(-1L);
                return;
            default:
                super.n(j);
                return;
        }
    }
}
