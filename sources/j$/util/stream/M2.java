package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
class M2 implements S2, k3 {
    private int a;
    final /* synthetic */ int b;
    final /* synthetic */ j$.util.function.j c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public M2(int i, j$.util.function.j jVar) {
        this.b = i;
        this.c = jVar;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(double d) {
        o1.f(this);
        throw null;
    }

    @Override // j$.util.stream.m3
    public void accept(int i) {
        this.a = this.c.applyAsInt(this.a, i);
    }

    @Override // j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        o1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: b */
    public /* synthetic */ void accept(Integer num) {
        o1.b(this, num);
    }

    @Override // j$.util.function.Supplier
    public Object get() {
        return Integer.valueOf(this.a);
    }

    @Override // j$.util.stream.S2
    public void h(S2 s2) {
        accept(((M2) s2).a);
    }

    @Override // j$.util.function.l
    public j$.util.function.l l(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new j$.util.function.k(this, lVar);
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
