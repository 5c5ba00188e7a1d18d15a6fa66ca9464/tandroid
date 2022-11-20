package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
class F2 extends T2 implements S2, j3 {
    final /* synthetic */ j$.util.function.y b;
    final /* synthetic */ j$.util.function.u c;
    final /* synthetic */ j$.util.function.b d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public F2(j$.util.function.y yVar, j$.util.function.u uVar, j$.util.function.b bVar) {
        this.b = yVar;
        this.c = uVar;
        this.d = bVar;
    }

    @Override // j$.util.stream.m3
    public void accept(double d) {
        this.c.accept(this.a, d);
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(int i) {
        o1.d(this);
        throw null;
    }

    @Override // j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        o1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Objects.requireNonNull(consumer);
    }

    @Override // j$.util.function.Consumer
    /* renamed from: b */
    public /* synthetic */ void accept(Double d) {
        o1.a(this, d);
    }

    @Override // j$.util.stream.S2
    public void h(S2 s2) {
        this.a = this.d.apply(this.a, ((F2) s2).a);
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        this.a = this.b.get();
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ boolean o() {
        return false;
    }
}
