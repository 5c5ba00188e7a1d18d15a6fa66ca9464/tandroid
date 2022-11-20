package j$.util.stream;

import j$.util.Collection$-EL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class N3 extends F3 {
    private ArrayList d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public N3(m3 m3Var, Comparator comparator) {
        super(m3Var, comparator);
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
        this.d.add(obj);
    }

    @Override // j$.util.stream.i3, j$.util.stream.m3
    public void m() {
        j$.util.a.G(this.d, this.b);
        this.a.n(this.d.size());
        if (!this.c) {
            ArrayList arrayList = this.d;
            m3 m3Var = this.a;
            Objects.requireNonNull(m3Var);
            Collection$-EL.a(arrayList, new b(m3Var));
        } else {
            Iterator it = this.d.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (this.a.o()) {
                    break;
                }
                this.a.accept((m3) next);
            }
        }
        this.a.m();
        this.d = null;
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        if (j < 2147483639) {
            this.d = j >= 0 ? new ArrayList((int) j) : new ArrayList();
            return;
        }
        throw new IllegalArgumentException("Stream size exceeds max array size");
    }
}
