package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class J4 extends K4 implements j$.util.t, Consumer {
    Object e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public J4(j$.util.t tVar, long j, long j2) {
        super(tVar, j, j2);
    }

    J4(j$.util.t tVar, J4 j4) {
        super(tVar, j4);
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        this.e = obj;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.t
    public boolean b(Consumer consumer) {
        Objects.requireNonNull(consumer);
        while (r() != 1 && this.a.b(this)) {
            if (p(1L) == 1) {
                consumer.accept(this.e);
                this.e = null;
                return true;
            }
        }
        return false;
    }

    @Override // j$.util.t
    public void forEachRemaining(Consumer consumer) {
        Objects.requireNonNull(consumer);
        l4 l4Var = null;
        while (true) {
            int r = r();
            if (r == 1) {
                return;
            }
            if (r == 2) {
                if (l4Var == null) {
                    l4Var = new l4(128);
                } else {
                    l4Var.a = 0;
                }
                long j = 0;
                while (this.a.b(l4Var)) {
                    j++;
                    if (j >= 128) {
                        break;
                    }
                }
                if (j == 0) {
                    return;
                }
                long p = p(j);
                for (int i = 0; i < p; i++) {
                    consumer.accept(l4Var.b[i]);
                }
            } else {
                this.a.forEachRemaining(consumer);
                return;
            }
        }
    }

    @Override // j$.util.t
    public Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.t
    public /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.e(this);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.f(this, i);
    }

    @Override // j$.util.stream.K4
    protected j$.util.t q(j$.util.t tVar) {
        return new J4(tVar, this);
    }
}
