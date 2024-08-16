package j$.util.stream;

import j$.util.function.Consumer;
import java.util.ArrayDeque;
/* loaded from: classes2.dex */
final class h1 extends i1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public h1(F0 f0) {
        super(f0);
    }

    @Override // j$.util.Q
    public final void a(Consumer consumer) {
        if (this.a == null) {
            return;
        }
        if (this.d != null) {
            do {
            } while (s(consumer));
            return;
        }
        j$.util.Q q = this.c;
        if (q != null) {
            q.a(consumer);
            return;
        }
        ArrayDeque f = f();
        while (true) {
            F0 b = i1.b(f);
            if (b == null) {
                this.a = null;
                return;
            }
            b.forEach(consumer);
        }
    }

    @Override // j$.util.Q
    public final boolean s(Consumer consumer) {
        F0 b;
        if (h()) {
            boolean s = this.d.s(consumer);
            if (!s) {
                if (this.c == null && (b = i1.b(this.e)) != null) {
                    j$.util.Q spliterator = b.spliterator();
                    this.d = spliterator;
                    return spliterator.s(consumer);
                }
                this.a = null;
            }
            return s;
        }
        return false;
    }
}
