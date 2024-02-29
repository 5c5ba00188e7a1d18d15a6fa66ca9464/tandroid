package j$.time.temporal;

import java.util.Objects;
/* loaded from: classes2.dex */
public abstract /* synthetic */ class j {
    public static int a(k kVar, l lVar) {
        x b = kVar.b(lVar);
        if (!b.g()) {
            throw new w("Invalid field " + lVar + " for get() method, use getLong() instead");
        }
        long c = kVar.c(lVar);
        if (b.h(c)) {
            return (int) c;
        }
        throw new j$.time.c("Invalid value for " + lVar + " (valid values " + b + "): " + c);
    }

    public static Object b(k kVar, u uVar) {
        int i = t.a;
        if (uVar == m.a || uVar == n.a || uVar == o.a) {
            return null;
        }
        return uVar.a(kVar);
    }

    public static x c(k kVar, l lVar) {
        if (!(lVar instanceof a)) {
            Objects.requireNonNull(lVar, "field");
            return lVar.e(kVar);
        } else if (kVar.e(lVar)) {
            return lVar.a();
        } else {
            throw new w("Unsupported field: " + lVar);
        }
    }
}
