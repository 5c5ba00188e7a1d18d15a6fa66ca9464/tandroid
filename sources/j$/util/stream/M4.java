package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;
import java.util.Objects;
/* loaded from: classes2.dex */
final class M4 extends g4 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public M4(z2 z2Var, Supplier supplier, boolean z) {
        super(z2Var, supplier, z);
    }

    M4(z2 z2Var, j$.util.t tVar, boolean z) {
        super(z2Var, tVar, z);
    }

    @Override // j$.util.t
    public boolean b(Consumer consumer) {
        Object obj;
        Objects.requireNonNull(consumer);
        boolean a = a();
        if (a) {
            b4 b4Var = (b4) this.h;
            long j = this.g;
            if (b4Var.c != 0) {
                if (j < b4Var.count()) {
                    for (int i = 0; i <= b4Var.c; i++) {
                        long[] jArr = b4Var.d;
                        long j2 = jArr[i];
                        Object[][] objArr = b4Var.f;
                        if (j < j2 + objArr[i].length) {
                            obj = objArr[i][(int) (j - jArr[i])];
                        }
                    }
                    throw new IndexOutOfBoundsException(Long.toString(j));
                }
                throw new IndexOutOfBoundsException(Long.toString(j));
            } else if (j >= b4Var.b) {
                throw new IndexOutOfBoundsException(Long.toString(j));
            } else {
                obj = b4Var.e[(int) j];
            }
            consumer.accept(obj);
        }
        return a;
    }

    @Override // j$.util.t
    public void forEachRemaining(Consumer consumer) {
        if (this.h != null || this.i) {
            do {
            } while (b(consumer));
            return;
        }
        Objects.requireNonNull(consumer);
        h();
        this.b.s0(new L4(consumer), this.d);
        this.i = true;
    }

    @Override // j$.util.stream.g4
    void j() {
        b4 b4Var = new b4();
        this.h = b4Var;
        this.e = this.b.t0(new L4(b4Var));
        this.f = new b(this);
    }

    @Override // j$.util.stream.g4
    g4 l(j$.util.t tVar) {
        return new M4(this.b, tVar, this.a);
    }
}
