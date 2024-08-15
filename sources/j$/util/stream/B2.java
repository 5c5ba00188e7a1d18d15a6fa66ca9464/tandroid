package j$.util.stream;

import j$.util.Collection$-EL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
/* loaded from: classes2.dex */
final class B2 extends t2 {
    private ArrayList d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public B2(f2 f2Var, Comparator comparator) {
        super(f2Var, comparator);
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        this.d.add(obj);
    }

    @Override // j$.util.stream.b2, j$.util.stream.f2
    public final void end() {
        j$.util.a.s(this.d, this.b);
        f2 f2Var = this.a;
        f2Var.f(this.d.size());
        if (this.c) {
            Iterator it = this.d.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (f2Var.h()) {
                    break;
                }
                f2Var.accept((f2) next);
            }
        } else {
            ArrayList arrayList = this.d;
            f2Var.getClass();
            Collection$-EL.a(arrayList, new a(f2Var, 3));
        }
        f2Var.end();
        this.d = null;
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.d = j >= 0 ? new ArrayList((int) j) : new ArrayList();
    }
}
