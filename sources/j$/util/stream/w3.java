package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class w3 extends y3 implements j$.util.Q, Consumer {
    Object e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w3(j$.util.Q q, long j, long j2) {
        super(q, j, j2);
    }

    w3(j$.util.Q q, w3 w3Var) {
        super(q, w3Var);
    }

    @Override // j$.util.Q
    public final boolean a(Consumer consumer) {
        consumer.getClass();
        while (s() != x3.NO_MORE && this.a.a(this)) {
            if (q(1L) == 1) {
                consumer.accept(this.e);
                this.e = null;
                return true;
            }
        }
        return false;
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        this.e = obj;
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.Q
    public final void forEachRemaining(Consumer consumer) {
        consumer.getClass();
        a3 a3Var = null;
        while (true) {
            x3 s = s();
            if (s == x3.NO_MORE) {
                return;
            }
            x3 x3Var = x3.MAYBE_MORE;
            j$.util.Q q = this.a;
            if (s != x3Var) {
                q.forEachRemaining(consumer);
                return;
            }
            if (a3Var == null) {
                a3Var = new a3();
            } else {
                a3Var.a = 0;
            }
            long j = 0;
            while (q.a(a3Var)) {
                j++;
                if (j >= 128) {
                    break;
                }
            }
            if (j == 0) {
                return;
            }
            long q2 = q(j);
            for (int i = 0; i < q2; i++) {
                consumer.accept(a3Var.b[i]);
            }
        }
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.i(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.k(this, i);
    }

    @Override // j$.util.stream.y3
    protected final j$.util.Q r(j$.util.Q q) {
        return new w3(q, this);
    }
}
