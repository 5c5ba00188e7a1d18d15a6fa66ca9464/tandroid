package j$.time.format;

import j$.util.concurrent.ConcurrentHashMap;
/* loaded from: classes2.dex */
public final class w {
    public static final w a = new w();

    static {
        new ConcurrentHashMap(16, 0.75f, 2);
    }

    private w() {
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof w) {
            ((w) obj).getClass();
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return 182;
    }

    public final String toString() {
        return "DecimalStyle[0+-.]";
    }
}
