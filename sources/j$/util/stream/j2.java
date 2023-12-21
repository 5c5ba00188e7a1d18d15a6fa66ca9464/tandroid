package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Deque;
/* loaded from: classes2.dex */
final class j2 extends k2 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public j2(A1 a1) {
        super(a1);
    }

    @Override // j$.util.t
    public boolean b(Consumer consumer) {
        A1 a;
        if (h()) {
            boolean b = this.d.b(consumer);
            if (!b) {
                if (this.c == null && (a = a(this.e)) != null) {
                    j$.util.t spliterator = a.spliterator();
                    this.d = spliterator;
                    return spliterator.b(consumer);
                }
                this.a = null;
            }
            return b;
        }
        return false;
    }

    @Override // j$.util.t
    public void forEachRemaining(Consumer consumer) {
        if (this.a == null) {
            return;
        }
        if (this.d != null) {
            do {
            } while (b(consumer));
            return;
        }
        j$.util.t tVar = this.c;
        if (tVar != null) {
            tVar.forEachRemaining(consumer);
            return;
        }
        Deque f = f();
        while (true) {
            A1 a = a(f);
            if (a == null) {
                this.a = null;
                return;
            }
            a.forEach(consumer);
        }
    }
}
