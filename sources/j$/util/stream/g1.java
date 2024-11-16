package j$.util.stream;

import java.util.ArrayDeque;

/* loaded from: classes2.dex */
abstract class g1 extends i1 implements j$.util.N {
    g1(E0 e0) {
        super(e0);
    }

    @Override // j$.util.N
    /* renamed from: forEachRemaining, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public final void e(Object obj) {
        if (this.a == null) {
            return;
        }
        if (this.d != null) {
            while (p(obj)) {
            }
            return;
        }
        j$.util.Q q = this.c;
        if (q != null) {
            ((j$.util.N) q).e(obj);
            return;
        }
        ArrayDeque f = f();
        while (true) {
            E0 e0 = (E0) i1.b(f);
            if (e0 == null) {
                this.a = null;
                return;
            }
            e0.g(obj);
        }
    }

    @Override // j$.util.N
    /* renamed from: tryAdvance, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public final boolean p(Object obj) {
        E0 e0;
        if (!h()) {
            return false;
        }
        boolean p = ((j$.util.N) this.d).p(obj);
        if (!p) {
            if (this.c == null && (e0 = (E0) i1.b(this.e)) != null) {
                j$.util.N spliterator = e0.spliterator();
                this.d = spliterator;
                return spliterator.p(obj);
            }
            this.a = null;
        }
        return p;
    }
}
