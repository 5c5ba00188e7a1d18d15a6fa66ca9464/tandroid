package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class o4 implements k3 {
    public final /* synthetic */ int a = 0;
    public final /* synthetic */ Object b;

    public /* synthetic */ o4(j$.util.function.f fVar) {
        this.b = fVar;
    }

    @Override // j$.util.stream.k3, j$.util.stream.n3
    public final void accept(double d) {
        switch (this.a) {
            case 0:
                ((j$.util.function.f) this.b).accept(d);
                return;
            default:
                ((V3) this.b).accept(d);
                return;
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

    public /* synthetic */ void b(Double d) {
        switch (this.a) {
            case 0:
                p1.a(this, d);
                return;
            default:
                p1.a(this, d);
                return;
        }
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        switch (this.a) {
            case 0:
                Objects.requireNonNull(fVar);
                return new j$.util.function.e(this, fVar);
            default:
                Objects.requireNonNull(fVar);
                return new j$.util.function.e(this, fVar);
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

    public /* synthetic */ o4(V3 v3) {
        this.b = v3;
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
                b((Double) obj);
                return;
            default:
                b((Double) obj);
                return;
        }
    }
}
