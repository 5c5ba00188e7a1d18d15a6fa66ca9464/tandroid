package j$.time.chrono;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.Locale;
/* loaded from: classes2.dex */
public abstract class a implements d {
    static {
        new ConcurrentHashMap();
        new ConcurrentHashMap();
        new Locale("ja", "JP", "JP");
    }

    @Override // java.lang.Comparable
    public final int compareTo(Object obj) {
        ((d) obj).getClass();
        return 0;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof a) {
            ((a) obj).getClass();
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return getClass().hashCode() ^ 72805;
    }

    public final String toString() {
        return "ISO";
    }
}
