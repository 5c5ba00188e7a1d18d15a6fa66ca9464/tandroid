package j$.time.format;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class w {
    public static final w a = new w('0', '+', '-', '.');

    static {
        new ConcurrentHashMap(16, 0.75f, 2);
    }

    private w(char c, char c2, char c3, char c4) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String a(String str) {
        return str;
    }

    public char b() {
        return '.';
    }

    public char c() {
        return '-';
    }

    public char d() {
        return '+';
    }

    public char e() {
        return '0';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof w) {
            Objects.requireNonNull((w) obj);
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 182;
    }

    public String toString() {
        return "DecimalStyle[0+-.]";
    }
}
