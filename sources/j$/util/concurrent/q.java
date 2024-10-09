package j$.util.concurrent;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class q extends k {
    q e;
    q f;
    q g;
    q h;
    boolean i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(int i, Object obj, Object obj2, q qVar, q qVar2) {
        super(i, obj, obj2, qVar);
        this.e = qVar2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.concurrent.k
    public final k a(Object obj, int i) {
        return b(i, obj, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final q b(int i, Object obj, Class cls) {
        if (obj == null) {
            return null;
        }
        q qVar = this;
        do {
            q qVar2 = qVar.f;
            q qVar3 = qVar.g;
            int i2 = qVar.a;
            if (i2 <= i) {
                if (i2 >= i) {
                    Object obj2 = qVar.b;
                    if (obj2 == obj || (obj2 != null && obj.equals(obj2))) {
                        return qVar;
                    }
                    if (qVar2 != null) {
                        if (qVar3 != null) {
                            if (cls != null || (cls = ConcurrentHashMap.c(obj)) != null) {
                                int i3 = ConcurrentHashMap.i;
                                int compareTo = (obj2 == null || obj2.getClass() != cls) ? 0 : ((Comparable) obj).compareTo(obj2);
                                if (compareTo != 0) {
                                    if (compareTo >= 0) {
                                        qVar2 = qVar3;
                                    }
                                }
                            }
                            q b = qVar3.b(i, obj, cls);
                            if (b != null) {
                                return b;
                            }
                        }
                    }
                }
                qVar = qVar3;
            }
            qVar = qVar2;
        } while (qVar != null);
        return null;
    }
}
