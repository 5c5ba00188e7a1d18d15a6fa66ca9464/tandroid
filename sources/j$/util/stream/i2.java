package j$.util.stream;

import java.util.Deque;
/* loaded from: classes2.dex */
abstract class i2 extends k2 implements j$.util.w {
    /* JADX INFO: Access modifiers changed from: package-private */
    public i2(z1 z1Var) {
        super(z1Var);
    }

    @Override // j$.util.w
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
        j$.util.u uVar = this.c;
        if (uVar != null) {
            ((j$.util.w) uVar).forEachRemaining(obj);
            return;
        }
        Deque f = f();
        while (true) {
            z1 z1Var = (z1) a(f);
            if (z1Var == null) {
                this.a = null;
                return;
            }
            z1Var.g(obj);
        }
    }

    @Override // j$.util.w
    /* renamed from: tryAdvance */
    public boolean k(Object obj) {
        z1 z1Var;
        if (!h()) {
            return false;
        }
        boolean tryAdvance = ((j$.util.w) this.d).tryAdvance(obj);
        if (!tryAdvance) {
            if (this.c == null && (z1Var = (z1) a(this.e)) != null) {
                j$.util.w spliterator = z1Var.spliterator();
                this.d = spliterator;
                return spliterator.tryAdvance(obj);
            }
            this.a = null;
        }
        return tryAdvance;
    }
}
