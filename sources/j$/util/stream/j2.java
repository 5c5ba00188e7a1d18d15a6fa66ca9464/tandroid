package j$.util.stream;

import java.util.Deque;
/* loaded from: classes2.dex */
abstract class j2 extends l2 implements j$.util.u {
    /* JADX INFO: Access modifiers changed from: package-private */
    public j2(A1 a1) {
        super(a1);
    }

    @Override // j$.util.u
    /* renamed from: forEachRemaining */
    public void e(Object obj) {
        if (this.a == null) {
            return;
        }
        if (this.d != null) {
            do {
            } while (k(obj));
            return;
        }
        j$.util.t tVar = this.c;
        if (tVar != null) {
            ((j$.util.u) tVar).forEachRemaining(obj);
            return;
        }
        Deque f = f();
        while (true) {
            A1 a1 = (A1) a(f);
            if (a1 == null) {
                this.a = null;
                return;
            }
            a1.g(obj);
        }
    }

    @Override // j$.util.u
    /* renamed from: tryAdvance */
    public boolean k(Object obj) {
        A1 a1;
        if (h()) {
            boolean tryAdvance = ((j$.util.u) this.d).tryAdvance(obj);
            if (!tryAdvance) {
                if (this.c == null && (a1 = (A1) a(this.e)) != null) {
                    j$.util.u spliterator = a1.spliterator();
                    this.d = spliterator;
                    return spliterator.tryAdvance(obj);
                }
                this.a = null;
            }
            return tryAdvance;
        }
        return false;
    }
}
