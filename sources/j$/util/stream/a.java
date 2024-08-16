package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;
import java.util.List;
/* loaded from: classes2.dex */
public final /* synthetic */ class a implements Supplier, LongFunction, Consumer, e2 {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;

    public /* synthetic */ a(Object obj, int i) {
        this.a = i;
        this.b = obj;
    }

    private final /* synthetic */ void a(long j) {
    }

    private final /* synthetic */ void c(long j) {
    }

    private final /* synthetic */ void d() {
    }

    private final /* synthetic */ void e() {
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public /* synthetic */ void accept(double d) {
        switch (this.a) {
            case 6:
                t0.b();
                throw null;
            default:
                t0.b();
                throw null;
        }
    }

    @Override // j$.util.stream.e2
    public /* synthetic */ void accept(int i) {
        switch (this.a) {
            case 6:
                t0.k();
                throw null;
            default:
                t0.k();
                throw null;
        }
    }

    @Override // j$.util.stream.e2
    public /* synthetic */ void accept(long j) {
        switch (this.a) {
            case 6:
                t0.l();
                throw null;
            default:
                t0.l();
                throw null;
        }
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        switch (this.a) {
            case 2:
                ((e2) this.b).accept((e2) obj);
                return;
            case 6:
                ((O2) this.b).accept(obj);
                return;
            case 8:
                ((Consumer) this.b).accept(obj);
                return;
            default:
                ((List) this.b).add(obj);
                return;
        }
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 2:
                return Consumer.-CC.$default$andThen(this, consumer);
            case 6:
                return Consumer.-CC.$default$andThen(this, consumer);
            case 8:
                return Consumer.-CC.$default$andThen(this, consumer);
            default:
                return Consumer.-CC.$default$andThen(this, consumer);
        }
    }

    @Override // j$.util.function.LongFunction
    public Object apply(long j) {
        return t0.D(j, (j$.util.function.I) this.b);
    }

    public boolean f() {
        switch (this.a) {
            case 3:
                d3 d3Var = (d3) this.b;
                return d3Var.d.s(d3Var.e);
            case 4:
                f3 f3Var = (f3) this.b;
                return f3Var.d.s(f3Var.e);
            case 5:
                h3 h3Var = (h3) this.b;
                return h3Var.d.s(h3Var.e);
            default:
                v3 v3Var = (v3) this.b;
                return v3Var.d.s(v3Var.e);
        }
    }

    @Override // j$.util.function.Supplier
    public Object get() {
        switch (this.a) {
            case 0:
                return ((b) this.b).u0();
            default:
                return (j$.util.Q) this.b;
        }
    }

    @Override // j$.util.stream.e2
    public /* synthetic */ void m() {
        int i = this.a;
    }

    @Override // j$.util.stream.e2
    public /* synthetic */ void n(long j) {
        int i = this.a;
    }

    @Override // j$.util.stream.e2
    public /* synthetic */ boolean q() {
        switch (this.a) {
            case 6:
                return false;
            default:
                return false;
        }
    }
}
