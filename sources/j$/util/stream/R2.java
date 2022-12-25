package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
class R2 implements S2, l3 {
    private boolean a;
    private long b;
    final /* synthetic */ j$.util.function.o c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public R2(j$.util.function.o oVar) {
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
        if (this.a) {
            this.a = false;
        } else {
            j = this.c.applyAsLong(this.b, j);
        }
        this.b = j;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Objects.requireNonNull(consumer);
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

    @Override // j$.util.function.y
    public Object get() {
        return this.a ? j$.util.l.a() : j$.util.l.d(this.b);
    }

    @Override // j$.util.stream.S2
    public void h(S2 s2) {
        R2 r2 = (R2) s2;
        if (r2.a) {
            return;
        }
        accept(r2.b);
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        this.a = true;
        this.b = 0L;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ boolean o() {
        return false;
    }
}
