package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class s4 implements m3 {
    public final /* synthetic */ int a = 0;
    public final /* synthetic */ Object b;

    public /* synthetic */ s4(j$.util.function.q qVar) {
        this.b = qVar;
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void accept(double d) {
        switch (this.a) {
            case 0:
                p1.f(this);
                throw null;
            default:
                p1.f(this);
                throw null;
        }
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 0:
                return Consumer.-CC.$default$andThen(this, consumer);
            default:
                return Consumer.-CC.$default$andThen(this, consumer);
        }
    }

    public /* synthetic */ void b(Long l) {
        switch (this.a) {
            case 0:
                p1.c(this, l);
                return;
            default:
                p1.c(this, l);
                return;
        }
    }

    @Override // j$.util.function.q
    public j$.util.function.q f(j$.util.function.q qVar) {
        switch (this.a) {
            case 0:
                Objects.requireNonNull(qVar);
                return new j$.util.function.p(this, qVar);
            default:
                Objects.requireNonNull(qVar);
                return new j$.util.function.p(this, qVar);
        }
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void n(long j) {
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ boolean o() {
        return false;
    }

    public /* synthetic */ s4(Z3 z3) {
        this.b = z3;
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void accept(int i) {
        switch (this.a) {
            case 0:
                p1.d(this);
                throw null;
            default:
                p1.d(this);
                throw null;
        }
    }

    @Override // j$.util.stream.m3, j$.util.function.q
    public final void accept(long j) {
        switch (this.a) {
            case 0:
                ((j$.util.function.q) this.b).accept(j);
                return;
            default:
                ((Z3) this.b).accept(j);
                return;
        }
    }

    @Override // j$.util.function.Consumer
    public /* bridge */ /* synthetic */ void accept(Object obj) {
        switch (this.a) {
            case 0:
                b((Long) obj);
                return;
            default:
                b((Long) obj);
                return;
        }
    }
}
