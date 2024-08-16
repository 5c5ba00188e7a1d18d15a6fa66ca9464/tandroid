package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class s3 extends u3 implements j$.util.Q, Consumer {
    Object e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s3(j$.util.Q q, long j, long j2) {
        super(q, j, j2);
    }

    s3(j$.util.Q q, s3 s3Var) {
        super(q, s3Var);
    }

    @Override // j$.util.Q
    public final void a(Consumer consumer) {
        consumer.getClass();
        Z2 z2 = null;
        while (true) {
            t3 v = v();
            if (v == t3.NO_MORE) {
                return;
            }
            t3 t3Var = t3.MAYBE_MORE;
            j$.util.Q q = this.a;
            if (v != t3Var) {
                q.a(consumer);
                return;
            }
            if (z2 == null) {
                z2 = new Z2();
            } else {
                z2.a = 0;
            }
            long j = 0;
            while (q.s(z2)) {
                j++;
                if (j >= 128) {
                    break;
                }
            }
            if (j == 0) {
                return;
            }
            long t = t(j);
            for (int i = 0; i < t; i++) {
                consumer.accept(z2.b[i]);
            }
        }
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
    public final Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.j(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.k(this, i);
    }

    @Override // j$.util.Q
    public final boolean s(Consumer consumer) {
        consumer.getClass();
        while (v() != t3.NO_MORE && this.a.s(this)) {
            if (t(1L) == 1) {
                consumer.accept(this.e);
                this.e = null;
                return true;
            }
        }
        return false;
    }

    @Override // j$.util.stream.u3
    protected final j$.util.Q u(j$.util.Q q) {
        return new s3(q, this);
    }
}
