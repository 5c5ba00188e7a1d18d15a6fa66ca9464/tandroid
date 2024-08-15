package j$.util.concurrent;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class h extends m {
    final m[] e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(m[] mVarArr) {
        super(-1, null, null, null);
        this.e = mVarArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0029, code lost:
        if ((r0 instanceof j$.util.concurrent.h) == false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x002b, code lost:
        r0 = ((j$.util.concurrent.h) r0).e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0034, code lost:
        return r0.a(r5, r6);
     */
    @Override // j$.util.concurrent.m
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final m a(Object obj, int i) {
        int length;
        m m;
        Object obj2;
        m[] mVarArr = this.e;
        loop0: while (obj != null && mVarArr != null && (length = mVarArr.length) != 0 && (m = ConcurrentHashMap.m(mVarArr, (length - 1) & i)) != null) {
            while (true) {
                int i2 = m.a;
                if (i2 != i || ((obj2 = m.b) != obj && (obj2 == null || !obj.equals(obj2)))) {
                    if (i2 >= 0) {
                        m = m.d;
                        if (m == null) {
                            break loop0;
                        }
                    } else {
                        break;
                    }
                }
            }
            return m;
        }
        return null;
    }
}
