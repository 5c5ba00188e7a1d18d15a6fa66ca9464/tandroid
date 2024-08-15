package j$.util.stream;

import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes2.dex */
final class t0 extends d {
    private final s0 j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t0(s0 s0Var, u0 u0Var, j$.util.Q q) {
        super(u0Var, q);
        this.j = s0Var;
    }

    t0(t0 t0Var, j$.util.Q q) {
        super(t0Var, q);
        this.j = t0Var.j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public final Object a() {
        boolean z;
        Boolean valueOf;
        u0 u0Var = this.a;
        q0 q0Var = (q0) this.j.b.get();
        u0Var.X0(this.b, q0Var);
        boolean z2 = q0Var.b;
        z = this.j.a.b;
        if (z2 == z && (valueOf = Boolean.valueOf(z2)) != null) {
            AtomicReference atomicReference = this.h;
            while (!atomicReference.compareAndSet(null, valueOf) && atomicReference.get() == null) {
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public final f d(j$.util.Q q) {
        return new t0(this, q);
    }

    @Override // j$.util.stream.d
    protected final Object i() {
        boolean z;
        z = this.j.a.b;
        return Boolean.valueOf(!z);
    }
}
