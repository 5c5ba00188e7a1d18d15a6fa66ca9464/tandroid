package j$.time.format;

import j$.util.concurrent.ConcurrentHashMap;
import org.telegram.messenger.NotificationCenter;

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
        if (!(obj instanceof w)) {
            return false;
        }
        ((w) obj).getClass();
        return true;
    }

    public final int hashCode() {
        return NotificationCenter.didStartedMultiGiftsSelector;
    }

    public final String toString() {
        return "DecimalStyle[0+-.]";
    }
}
