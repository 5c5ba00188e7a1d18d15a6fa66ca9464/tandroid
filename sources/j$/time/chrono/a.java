package j$.time.chrono;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class a implements g {
    static {
        new ConcurrentHashMap();
        new ConcurrentHashMap();
        new Locale("ja", "JP", "JP");
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        Objects.requireNonNull((g) obj);
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof a) {
            Objects.requireNonNull((a) obj);
            return true;
        }
        return false;
    }

    public int hashCode() {
        return getClass().hashCode() ^ 72805;
    }

    public String toString() {
        return "ISO";
    }
}
