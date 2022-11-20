package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
final class L4 extends f4 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public L4(y2 y2Var, j$.util.function.y yVar, boolean z) {
        super(y2Var, yVar, z);
    }

    L4(y2 y2Var, j$.util.u uVar, boolean z) {
        super(y2Var, uVar, z);
    }

    @Override // j$.util.u
    public boolean b(Consumer consumer) {
        Object obj;
        Objects.requireNonNull(consumer);
        boolean a = a();
        if (a) {
            a4 a4Var = (a4) this.h;
            long j = this.g;
            if (a4Var.c != 0) {
                if (j >= a4Var.count()) {
                    throw new IndexOutOfBoundsException(Long.toString(j));
                }
                for (int i = 0; i <= a4Var.c; i++) {
                    long[] jArr = a4Var.d;
                    long j2 = jArr[i];
                    Object[][] objArr = a4Var.f;
                    if (j < j2 + objArr[i].length) {
                        obj = objArr[i][(int) (j - jArr[i])];
                    }
                }
                throw new IndexOutOfBoundsException(Long.toString(j));
            } else if (j >= a4Var.b) {
                throw new IndexOutOfBoundsException(Long.toString(j));
            } else {
                obj = a4Var.e[(int) j];
            }
            consumer.accept(obj);
        }
        return a;
    }

    @Override // j$.util.u
    public void forEachRemaining(Consumer consumer) {
        if (this.h != null || this.i) {
            do {
            } while (b(consumer));
            return;
        }
        Objects.requireNonNull(consumer);
        h();
        this.b.u0(new K4(consumer), this.d);
        this.i = true;
    }

    @Override // j$.util.stream.f4
    void j() {
        a4 a4Var = new a4();
        this.h = a4Var;
        this.e = this.b.v0(new K4(a4Var));
        this.f = new b(this);
    }

    @Override // j$.util.stream.f4
    f4 l(j$.util.u uVar) {
        return new L4(this.b, uVar, this.a);
    }
}
