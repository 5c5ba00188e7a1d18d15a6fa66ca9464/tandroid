package j$.util.stream;

import j$.util.Collection$-EL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class O3 extends G3 {
    private ArrayList d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public O3(n3 n3Var, Comparator comparator) {
        super(n3Var, comparator);
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        this.d.add(obj);
    }

    @Override // j$.util.stream.j3, j$.util.stream.n3
    public void m() {
        j$.util.a.v(this.d, this.b);
        this.a.n(this.d.size());
        if (this.c) {
            Iterator it = this.d.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (this.a.o()) {
                    break;
                }
                this.a.accept((n3) next);
            }
        } else {
            ArrayList arrayList = this.d;
            n3 n3Var = this.a;
            Objects.requireNonNull(n3Var);
            Collection$-EL.a(arrayList, new b(n3Var));
        }
        this.a.m();
        this.d = null;
    }

    @Override // j$.util.stream.n3
    public void n(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.d = j >= 0 ? new ArrayList((int) j) : new ArrayList();
    }
}
