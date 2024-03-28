package j$.util.stream;

import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
abstract class I4 extends K4 implements j$.util.u {
    /* JADX INFO: Access modifiers changed from: package-private */
    public I4(j$.util.u uVar, long j, long j2) {
        super(uVar, j, j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public I4(j$.util.u uVar, I4 i4) {
        super(uVar, i4);
    }

    @Override // j$.util.u
    /* renamed from: forEachRemaining */
    public void e(Object obj) {
        Objects.requireNonNull(obj);
        k4 k4Var = null;
        while (true) {
            int r = r();
            if (r == 1) {
                return;
            }
            if (r != 2) {
                ((j$.util.u) this.a).forEachRemaining(obj);
                return;
            }
            if (k4Var == null) {
                k4Var = t(128);
            } else {
                k4Var.b = 0;
            }
            long j = 0;
            while (((j$.util.u) this.a).tryAdvance(k4Var)) {
                j++;
                if (j >= 128) {
                    break;
                }
            }
            if (j == 0) {
                return;
            }
            k4Var.b(obj, p(j));
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

    protected abstract k4 t(int i);

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
