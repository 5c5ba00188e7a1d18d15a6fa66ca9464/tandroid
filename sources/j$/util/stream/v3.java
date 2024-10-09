package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class v3 extends U2 {
    v3(b bVar, j$.util.Q q, boolean z) {
        super(bVar, q, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public v3(b bVar, Supplier supplier, boolean z) {
        super(bVar, supplier, z);
    }

    @Override // j$.util.Q
    public final void a(Consumer consumer) {
        if (this.h != null || this.i) {
            do {
            } while (s(consumer));
            return;
        }
        consumer.getClass();
        h();
        a aVar = new a(consumer, 8);
        this.b.D0(this.d, aVar);
        this.i = true;
    }

    @Override // j$.util.stream.U2
    final void j() {
        O2 o2 = new O2();
        this.h = o2;
        this.e = this.b.E0(new a(o2, 6));
        this.f = new a(this, 7);
    }

    @Override // j$.util.stream.U2
    final U2 k(j$.util.Q q) {
        return new v3(this.b, q, this.a);
    }

    @Override // j$.util.Q
    public final boolean s(Consumer consumer) {
        Object obj;
        consumer.getClass();
        boolean b = b();
        if (b) {
            O2 o2 = (O2) this.h;
            long j = this.g;
            if (o2.c != 0) {
                if (j >= o2.count()) {
                    throw new IndexOutOfBoundsException(Long.toString(j));
                }
                for (int i = 0; i <= o2.c; i++) {
                    long j2 = o2.d[i];
                    Object[] objArr = o2.f[i];
                    if (j < objArr.length + j2) {
                        obj = objArr[(int) (j - j2)];
                    }
                }
                throw new IndexOutOfBoundsException(Long.toString(j));
            }
            if (j >= o2.b) {
                throw new IndexOutOfBoundsException(Long.toString(j));
            }
            obj = o2.e[(int) j];
            consumer.r(obj);
        }
        return b;
    }
}
