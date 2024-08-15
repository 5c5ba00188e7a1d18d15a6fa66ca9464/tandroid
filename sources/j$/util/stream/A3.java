package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class A3 extends V2 {
    A3(u0 u0Var, j$.util.Q q, boolean z) {
        super(u0Var, q, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public A3(u0 u0Var, a aVar, boolean z) {
        super(u0Var, aVar, z);
    }

    @Override // j$.util.Q
    public final boolean a(Consumer consumer) {
        Object obj;
        consumer.getClass();
        boolean f = f();
        if (f) {
            P2 p2 = (P2) this.h;
            long j = this.g;
            if (p2.c != 0) {
                if (j < p2.count()) {
                    for (int i = 0; i <= p2.c; i++) {
                        long j2 = p2.d[i];
                        Object[] objArr = p2.f[i];
                        if (j < objArr.length + j2) {
                            obj = objArr[(int) (j - j2)];
                        }
                    }
                    throw new IndexOutOfBoundsException(Long.toString(j));
                }
                throw new IndexOutOfBoundsException(Long.toString(j));
            } else if (j >= p2.b) {
                throw new IndexOutOfBoundsException(Long.toString(j));
            } else {
                obj = p2.e[(int) j];
            }
            consumer.accept(obj);
        }
        return f;
    }

    @Override // j$.util.Q
    public final void forEachRemaining(Consumer consumer) {
        if (this.h != null || this.i) {
            do {
            } while (a(consumer));
            return;
        }
        consumer.getClass();
        h();
        z3 z3Var = new z3(consumer, 1);
        this.b.X0(this.d, z3Var);
        this.i = true;
    }

    @Override // j$.util.stream.V2
    final void i() {
        P2 p2 = new P2();
        this.h = p2;
        this.e = this.b.Y0(new z3(p2, 0));
        this.f = new a(this, 7);
    }

    @Override // j$.util.stream.V2
    final V2 k(j$.util.Q q) {
        return new A3(this.b, q, this.a);
    }
}
