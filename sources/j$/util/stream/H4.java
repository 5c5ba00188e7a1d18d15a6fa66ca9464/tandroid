package j$.util.stream;

import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
abstract class H4 extends J4 implements j$.util.u {
    /* JADX INFO: Access modifiers changed from: package-private */
    public H4(j$.util.u uVar, long j, long j2) {
        super(uVar, j, j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public H4(j$.util.u uVar, H4 h4) {
        super(uVar, h4);
    }

    @Override // j$.util.u
    /* renamed from: forEachRemaining */
    public void e(Object obj) {
        Objects.requireNonNull(obj);
        j4 j4Var = null;
        while (true) {
            int r = r();
            if (r == 1) {
                return;
            }
            if (r != 2) {
                ((j$.util.u) this.a).forEachRemaining(obj);
                return;
            }
            if (j4Var == null) {
                j4Var = t(128);
            } else {
                j4Var.b = 0;
            }
            long j = 0;
            while (((j$.util.u) this.a).tryAdvance(j4Var)) {
                j++;
                if (j >= 128) {
                    break;
                }
            }
            if (j == 0) {
                return;
            }
            j4Var.b(obj, p(j));
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

    protected abstract void s(Object obj);

    protected abstract j4 t(int i);

    @Override // j$.util.u
    /* renamed from: tryAdvance */
    public boolean k(Object obj) {
        Objects.requireNonNull(obj);
        while (r() != 1 && ((j$.util.u) this.a).tryAdvance(this)) {
            if (p(1L) == 1) {
                s(obj);
                return true;
            }
        }
        return false;
    }
}
