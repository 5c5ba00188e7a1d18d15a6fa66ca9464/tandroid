package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class r4 implements l3 {
    public final /* synthetic */ int a = 0;
    public final /* synthetic */ Object b;

    public /* synthetic */ r4(j$.util.function.q qVar) {
        this.b = qVar;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(double d) {
        switch (this.a) {
            case 0:
                o1.f(this);
                throw null;
            default:
                o1.f(this);
                throw null;
        }
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 0:
                return Objects.requireNonNull(consumer);
            default:
                return Objects.requireNonNull(consumer);
        }
    }

    public /* synthetic */ void b(Long l) {
        switch (this.a) {
            case 0:
                o1.c(this, l);
                return;
            default:
                o1.c(this, l);
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

    @Override // j$.util.stream.m3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void n(long j) {
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ boolean o() {
        return false;
    }

    public /* synthetic */ r4(Y3 y3) {
        this.b = y3;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(int i) {
        switch (this.a) {
            case 0:
                o1.d(this);
                throw null;
            default:
                o1.d(this);
                throw null;
        }
    }

    @Override // j$.util.stream.l3, j$.util.function.q
    public final void accept(long j) {
        switch (this.a) {
            case 0:
                ((j$.util.function.q) this.b).accept(j);
                return;
            default:
                ((Y3) this.b).accept(j);
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
