package j$.util.stream;

import java.util.Comparator;
import org.telegram.tgnet.ConnectionsManager;
/* loaded from: classes2.dex */
public abstract class H4 extends J4 implements j$.util.w {
    public H4(j$.util.w wVar, long j, long j2) {
        super(wVar, j, j2);
    }

    public H4(j$.util.w wVar, H4 h4) {
        super(wVar, h4);
    }

    @Override // j$.util.w
    /* renamed from: forEachRemaining */
    public void e(Object obj) {
        obj.getClass();
        j4 j4Var = null;
        while (true) {
            int r = r();
            if (r != 1) {
                if (r != 2) {
                    ((j$.util.w) this.a).forEachRemaining(obj);
                    return;
                }
                if (j4Var == null) {
                    j4Var = t(ConnectionsManager.RequestFlagNeedQuickAck);
                } else {
                    j4Var.b = 0;
                }
                long j = 0;
                while (((j$.util.w) this.a).tryAdvance(j4Var)) {
                    j++;
                    if (j >= 128) {
                        break;
                    }
                }
                if (j == 0) {
                    return;
                }
                j4Var.b(obj, p(j));
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

    protected abstract void s(Object obj);

    protected abstract j4 t(int i);

    @Override // j$.util.w
    /* renamed from: tryAdvance */
    public boolean k(Object obj) {
        obj.getClass();
        while (r() != 1 && ((j$.util.w) this.a).tryAdvance(this)) {
            if (p(1L) == 1) {
                s(obj);
                return true;
            }
        }
        return false;
    }
}
