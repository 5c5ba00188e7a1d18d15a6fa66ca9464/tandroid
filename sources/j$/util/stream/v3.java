package j$.util.stream;

import java.util.Comparator;
/* loaded from: classes2.dex */
abstract class v3 extends y3 implements j$.util.N {
    /* JADX INFO: Access modifiers changed from: package-private */
    public v3(j$.util.N n, long j, long j2) {
        super(n, j, j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public v3(j$.util.N n, v3 v3Var) {
        super(n, v3Var);
    }

    @Override // j$.util.N
    /* renamed from: forEachRemaining */
    public final void d(Object obj) {
        obj.getClass();
        Z2 z2 = null;
        while (true) {
            x3 s = s();
            if (s == x3.NO_MORE) {
                return;
            }
            x3 x3Var = x3.MAYBE_MORE;
            j$.util.Q q = this.a;
            if (s != x3Var) {
                ((j$.util.N) q).forEachRemaining(obj);
                return;
            }
            if (z2 == null) {
                z2 = u();
            } else {
                z2.b = 0;
            }
            long j = 0;
            while (((j$.util.N) q).tryAdvance(z2)) {
                j++;
                if (j >= 128) {
                    break;
                }
            }
            if (j == 0) {
                return;
            }
            z2.a(obj, q(j));
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

    protected abstract void t(Object obj);

    @Override // j$.util.N
    /* renamed from: tryAdvance */
    public final boolean o(Object obj) {
        obj.getClass();
        while (s() != x3.NO_MORE && ((j$.util.N) this.a).tryAdvance(this)) {
            if (q(1L) == 1) {
                t(obj);
                return true;
            }
        }
        return false;
    }

    protected abstract Z2 u();
}
