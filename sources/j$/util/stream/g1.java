package j$.util.stream;

import j$.util.function.Consumer;
import java.util.ArrayDeque;
/* loaded from: classes2.dex */
final class g1 extends h1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public g1(D0 d0) {
        super(d0);
    }

    @Override // j$.util.Q
    public final boolean a(Consumer consumer) {
        D0 f;
        if (h()) {
            boolean a = this.d.a(consumer);
            if (!a) {
                if (this.c == null && (f = h1.f(this.e)) != null) {
                    j$.util.Q spliterator = f.spliterator();
                    this.d = spliterator;
                    return spliterator.a(consumer);
                }
                this.a = null;
            }
            return a;
        }
        return false;
    }

    @Override // j$.util.Q
    public final void forEachRemaining(Consumer consumer) {
        if (this.a == null) {
            return;
        }
        if (this.d != null) {
            do {
            } while (a(consumer));
            return;
        }
        j$.util.Q q = this.c;
        if (q != null) {
            q.forEachRemaining(consumer);
            return;
        }
        ArrayDeque g = g();
        while (true) {
            D0 f = h1.f(g);
            if (f == null) {
                this.a = null;
                return;
            }
            f.forEach(consumer);
        }
    }
}
