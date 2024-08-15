package j$.util.concurrent;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class s extends m {
    s e;
    s f;
    s g;
    s h;
    boolean i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s(int i, Object obj, Object obj2, s sVar, s sVar2) {
        super(i, obj, obj2, sVar);
        this.e = sVar2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.concurrent.m
    public final m a(Object obj, int i) {
        return b(i, obj, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final s b(int i, Object obj, Class cls) {
        int d;
        if (obj != null) {
            s sVar = this;
            do {
                s sVar2 = sVar.f;
                s sVar3 = sVar.g;
                int i2 = sVar.a;
                if (i2 <= i) {
                    if (i2 >= i) {
                        Object obj2 = sVar.b;
                        if (obj2 == obj || (obj2 != null && obj.equals(obj2))) {
                            return sVar;
                        }
                        if (sVar2 != null) {
                            if (sVar3 != null) {
                                if ((cls == null && (cls = ConcurrentHashMap.c(obj)) == null) || (d = ConcurrentHashMap.d(cls, obj, obj2)) == 0) {
                                    s b = sVar3.b(i, obj, cls);
                                    if (b != null) {
                                        return b;
                                    }
                                } else if (d >= 0) {
                                    sVar2 = sVar3;
                                }
                            }
                        }
                    }
                    sVar = sVar3;
                    continue;
                }
                sVar = sVar2;
                continue;
            } while (sVar != null);
            return null;
        }
        return null;
    }
}
