package j$.util.stream;

import java.util.ArrayDeque;
/* loaded from: classes2.dex */
abstract class f1 extends h1 implements j$.util.N {
    /* JADX INFO: Access modifiers changed from: package-private */
    public f1(C0 c0) {
        super(c0);
    }

    @Override // j$.util.N
    /* renamed from: forEachRemaining */
    public final void d(Object obj) {
        if (this.a == null) {
            return;
        }
        if (this.d != null) {
            do {
            } while (o(obj));
            return;
        }
        j$.util.Q q = this.c;
        if (q != null) {
            ((j$.util.N) q).forEachRemaining(obj);
            return;
        }
        ArrayDeque g = g();
        while (true) {
            C0 c0 = (C0) h1.f(g);
            if (c0 == null) {
                this.a = null;
                return;
            }
            c0.d(obj);
        }
    }

    @Override // j$.util.N
    /* renamed from: tryAdvance */
    public final boolean o(Object obj) {
        C0 c0;
        if (h()) {
            boolean tryAdvance = ((j$.util.N) this.d).tryAdvance(obj);
            if (!tryAdvance) {
                if (this.c == null && (c0 = (C0) h1.f(this.e)) != null) {
                    j$.util.N spliterator = c0.spliterator();
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
