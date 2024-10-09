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
    @Override // j$.util.concurrent.k
    public final k a(Object obj, int i) {
        int length;
        k l;
        Object obj2;
        k[] kVarArr = this.e;
        loop0: while (obj != null && kVarArr != null && (length = kVarArr.length) != 0 && (l = ConcurrentHashMap.l(kVarArr, (length - 1) & i)) != null) {
            do {
                int i2 = l.a;
                if (i2 == i && ((obj2 = l.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                    return l;
                }
                if (i2 >= 0) {
                    l = l.d;
                } else {
                    if (!(l instanceof f)) {
                        return l.a(obj, i);
                    }
                    kVarArr = ((f) l).e;
                }
            } while (l != null);
        }
        return null;
    }
}
