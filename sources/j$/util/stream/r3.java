package j$.util.stream;

import java.util.Comparator;

/* loaded from: classes2.dex */
abstract class r3 extends u3 implements j$.util.N {
    /* JADX INFO: Access modifiers changed from: package-private */
    public r3(j$.util.N n, long j, long j2) {
        super(n, j, j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public r3(j$.util.N n, r3 r3Var) {
        super(n, r3Var);
    }

    @Override // j$.util.N
    /* renamed from: forEachRemaining, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public final void e(Object obj) {
        obj.getClass();
        Y2 y2 = null;
        while (true) {
            t3 v = v();
            if (v == t3.NO_MORE) {
                return;
            }
            t3 t3Var = t3.MAYBE_MORE;
            j$.util.Q q = this.a;
            if (v != t3Var) {
                ((j$.util.N) q).e(obj);
                return;
            }
            if (y2 == null) {
                y2 = x();
            } else {
                y2.b = 0;
            }
            long j = 0;
            while (((j$.util.N) q).p(y2)) {
                j++;
                if (j >= 128) {
                    break;
                }
            }
            if (j == 0) {
                return;
            } else {
                y2.a(obj, t(j));
            }
        }
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

    @Override // j$.util.N
    /* renamed from: tryAdvance, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public final boolean p(Object obj) {
        obj.getClass();
        while (v() != t3.NO_MORE && ((j$.util.N) this.a).p(this)) {
            if (t(1L) == 1) {
                w(obj);
                return true;
            }
        }
        return false;
    }

    protected abstract void w(Object obj);

    protected abstract Y2 x();
}
