package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.function.ToDoubleFunction;
import j$.util.function.ToIntFunction;
import j$.util.function.ToLongFunction;
/* loaded from: classes2.dex */
class Z2 extends j3 {
    public final /* synthetic */ int b = 5;
    final /* synthetic */ Object c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z2(L l, n3 n3Var) {
        super(n3Var);
        this.c = l;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        switch (this.b) {
            case 0:
                ((Consumer) ((M) this.c).m).accept(obj);
                this.a.accept((n3) obj);
                return;
            case 1:
                if (((Predicate) ((M) this.c).m).test(obj)) {
                    this.a.accept((n3) obj);
                    return;
                }
                return;
            case 2:
                this.a.accept((n3) ((b3) this.c).m.apply(obj));
                return;
            case 3:
                this.a.accept(((ToIntFunction) ((N) this.c).m).applyAsInt(obj));
                return;
            case 4:
                this.a.accept(((ToLongFunction) ((O) this.c).m).applyAsLong(obj));
                return;
            case 5:
                this.a.accept(((ToDoubleFunction) ((L) this.c).m).applyAsDouble(obj));
                return;
            default:
                Stream stream = (Stream) ((b3) this.c).m.apply(obj);
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

    @Override // j$.util.stream.n3
    public void n(long j) {
        switch (this.b) {
            case 1:
                this.a.n(-1L);
                return;
            case 6:
                this.a.n(-1L);
                return;
            default:
                this.a.n(j);
                return;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z2(M m, n3 n3Var) {
        super(n3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z2(M m, n3 n3Var, j$.lang.a aVar) {
        super(n3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z2(N n, n3 n3Var) {
        super(n3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z2(O o, n3 n3Var) {
        super(n3Var);
        this.c = o;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z2(b3 b3Var, n3 n3Var) {
        super(n3Var);
        this.c = b3Var;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Z2(b3 b3Var, n3 n3Var, j$.lang.a aVar) {
        super(n3Var);
        this.c = b3Var;
    }
}
