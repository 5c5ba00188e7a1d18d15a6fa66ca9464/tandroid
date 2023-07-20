package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.function.ToDoubleFunction;
import j$.util.function.ToIntFunction;
import j$.util.function.ToLongFunction;
/* loaded from: classes2.dex */
class Y2 extends i3 {
    public final /* synthetic */ int b = 5;
    final /* synthetic */ Object c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Y2(K k, m3 m3Var) {
        super(m3Var);
        this.c = k;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        switch (this.b) {
            case 0:
                ((Consumer) ((L) this.c).m).accept(obj);
                this.a.accept((m3) obj);
                return;
            case 1:
                if (((Predicate) ((L) this.c).m).test(obj)) {
                    this.a.accept((m3) obj);
                    return;
                }
                return;
            case 2:
                this.a.accept((m3) ((a3) this.c).m.apply(obj));
                return;
            case 3:
                this.a.accept(((ToIntFunction) ((M) this.c).m).applyAsInt(obj));
                return;
            case 4:
                this.a.accept(((ToLongFunction) ((N) this.c).m).applyAsLong(obj));
                return;
            case 5:
                this.a.accept(((ToDoubleFunction) ((K) this.c).m).applyAsDouble(obj));
                return;
            default:
                Stream stream = (Stream) ((a3) this.c).m.apply(obj);
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

    @Override // j$.util.stream.m3
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
    public Y2(L l, m3 m3Var) {
        super(m3Var);
        this.c = l;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Y2(L l, m3 m3Var, j$.lang.a aVar) {
        super(m3Var);
        this.c = l;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Y2(M m, m3 m3Var) {
        super(m3Var);
        this.c = m;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Y2(N n, m3 m3Var) {
        super(m3Var);
        this.c = n;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Y2(a3 a3Var, m3 m3Var) {
        super(m3Var);
        this.c = a3Var;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Y2(a3 a3Var, m3 m3Var, j$.lang.a aVar) {
        super(m3Var);
        this.c = a3Var;
    }
}
