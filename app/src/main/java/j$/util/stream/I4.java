package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Comparator;
import org.telegram.tgnet.ConnectionsManager;
/* loaded from: classes2.dex */
final class I4 extends J4 implements j$.util.u, Consumer {
    Object e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public I4(j$.util.u uVar, long j, long j2) {
        super(uVar, j, j2);
    }

    I4(j$.util.u uVar, I4 i4) {
        super(uVar, i4);
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        this.e = obj;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return consumer.getClass();
    }

    @Override // j$.util.u
    public boolean b(Consumer consumer) {
        consumer.getClass();
        while (r() != 1 && this.a.b(this)) {
            if (p(1L) == 1) {
                consumer.accept(this.e);
                this.e = null;
                return true;
            }
        }
        return false;
    }

    @Override // j$.util.u
    public void forEachRemaining(Consumer consumer) {
        consumer.getClass();
        k4 k4Var = null;
        while (true) {
            int r = r();
            if (r != 1) {
                if (r == 2) {
                    if (k4Var == null) {
                        k4Var = new k4(ConnectionsManager.RequestFlagNeedQuickAck);
                    } else {
                        k4Var.a = 0;
                    }
                    long j = 0;
                    while (this.a.b(k4Var)) {
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
                        consumer.accept(k4Var.b[i]);
                    }
                } else {
                    this.a.forEachRemaining(consumer);
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override // j$.util.u
    public Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.u
    public /* synthetic */ long getExactSizeIfKnown() {
        return j$.util.a.e(this);
    }

    @Override // j$.util.u
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.f(this, i);
    }

    @Override // j$.util.stream.J4
    protected j$.util.u q(j$.util.u uVar) {
        return new I4(uVar, this);
    }
}
