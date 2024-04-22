package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
class Q2 implements S2, l3 {
    private long a;
    final /* synthetic */ long b;
    final /* synthetic */ j$.util.function.o c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Q2(long j, j$.util.function.o oVar) {
        this.b = j;
        this.c = oVar;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(double d) {
        o1.f(this);
        throw null;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(int i) {
        o1.d(this);
        throw null;
    }

    @Override // j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public void accept(long j) {
        this.a = this.c.applyAsLong(this.a, j);
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: b */
    public /* synthetic */ void accept(Long l) {
        o1.c(this, l);
    }

    @Override // j$.util.function.q
    public j$.util.function.q f(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        return new j$.util.function.p(this, qVar);
    }

    @Override // j$.util.function.Supplier
    public Object get() {
        return Long.valueOf(this.a);
    }

    @Override // j$.util.stream.S2
    public void h(S2 s2) {
        accept(((Q2) s2).a);
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        this.a = this.b;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ boolean o() {
        return false;
    }
}
