package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class q4 implements l3 {
    public final /* synthetic */ int a = 0;
    public final /* synthetic */ Object b;

    public /* synthetic */ q4(j$.util.function.l lVar) {
        this.b = lVar;
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

    public /* synthetic */ void b(Integer num) {
        switch (this.a) {
            case 0:
                p1.b(this, num);
                return;
            default:
                p1.b(this, num);
                return;
        }
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        switch (this.a) {
            case 0:
                Objects.requireNonNull(lVar);
                return new j$.util.function.k(this, lVar);
            default:
                Objects.requireNonNull(lVar);
                return new j$.util.function.k(this, lVar);
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

    public /* synthetic */ q4(X3 x3) {
        this.b = x3;
    }

    @Override // j$.util.stream.l3, j$.util.stream.n3
    public final void accept(int i) {
        switch (this.a) {
            case 0:
                ((j$.util.function.l) this.b).accept(i);
                return;
            default:
                ((X3) this.b).accept(i);
                return;
        }
    }

    @Override // j$.util.stream.n3, j$.util.stream.m3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        switch (this.a) {
            case 0:
                p1.e(this);
                throw null;
            default:
                p1.e(this);
                throw null;
        }
    }

    @Override // j$.util.function.Consumer
    public /* bridge */ /* synthetic */ void accept(Object obj) {
        switch (this.a) {
            case 0:
                b((Integer) obj);
                return;
            default:
                b((Integer) obj);
                return;
        }
    }
}
