package j$.util.concurrent;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class f extends k {
    final k[] e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(k[] kVarArr) {
        super(-1, null, null, null);
        this.e = kVarArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0029, code lost:
        if ((r0 instanceof j$.util.concurrent.f) == false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x002b, code lost:
        r0 = ((j$.util.concurrent.f) r0).e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0034, code lost:
        return r0.a(r5, r6);
     */
    @Override // j$.util.concurrent.k
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final k a(Object obj, int i) {
        int length;
        k l;
        Object obj2;
        k[] kVarArr = this.e;
        loop0: while (obj != null && kVarArr != null && (length = kVarArr.length) != 0 && (l = ConcurrentHashMap.l(kVarArr, (length - 1) & i)) != null) {
            while (true) {
                int i2 = l.a;
                if (i2 != i || ((obj2 = l.b) != obj && (obj2 == null || !obj.equals(obj2)))) {
                    if (i2 >= 0) {
                        l = l.d;
                        if (l == null) {
                            break loop0;
                        }
                    } else {
                        break;
                    }
                }
            }
            return l;
        }
        return null;
    }
}
