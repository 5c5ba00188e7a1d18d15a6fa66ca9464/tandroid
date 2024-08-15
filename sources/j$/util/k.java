package j$.util;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
/* loaded from: classes2.dex */
public abstract class k {
    public static Optional a(java.util.Optional optional) {
        if (optional == null) {
            return null;
        }
        return optional.isPresent() ? Optional.of(optional.get()) : Optional.empty();
    }

    public static l b(OptionalDouble optionalDouble) {
        if (optionalDouble == null) {
            return null;
        }
        return optionalDouble.isPresent() ? l.d(optionalDouble.getAsDouble()) : l.a();
    }

    public static m c(OptionalInt optionalInt) {
        if (optionalInt == null) {
            return null;
        }
        return optionalInt.isPresent() ? m.d(optionalInt.getAsInt()) : m.a();
    }

    public static n d(OptionalLong optionalLong) {
        if (optionalLong == null) {
            return null;
        }
        return optionalLong.isPresent() ? n.d(optionalLong.getAsLong()) : n.a();
    }

    public static java.util.Optional e(Optional optional) {
        if (optional == null) {
            return null;
        }
        return optional.isPresent() ? java.util.Optional.of(optional.get()) : java.util.Optional.empty();
    }

    public static OptionalDouble f(l lVar) {
        if (lVar == null) {
            return null;
        }
        return lVar.c() ? OptionalDouble.of(lVar.b()) : OptionalDouble.empty();
    }

    public static OptionalInt g(m mVar) {
        if (mVar == null) {
            return null;
        }
        return mVar.c() ? OptionalInt.of(mVar.b()) : OptionalInt.empty();
    }

    public static OptionalLong h(n nVar) {
        if (nVar == null) {
            return null;
        }
        return nVar.c() ? OptionalLong.of(nVar.b()) : OptionalLong.empty();
    }
}
