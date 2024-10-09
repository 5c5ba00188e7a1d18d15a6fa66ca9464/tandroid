package j$.time.format;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public enum l implements g {
    SENSITIVE,
    INSENSITIVE,
    STRICT,
    LENIENT;

    @Override // j$.time.format.g
    public final boolean a(s sVar, StringBuilder sb) {
        return true;
    }

    @Override // java.lang.Enum
    public final String toString() {
        int ordinal = ordinal();
        if (ordinal == 0) {
            return "ParseCaseSensitive(true)";
        }
        if (ordinal == 1) {
            return "ParseCaseSensitive(false)";
        }
        if (ordinal == 2) {
            return "ParseStrict(true)";
        }
        if (ordinal == 3) {
            return "ParseStrict(false)";
        }
        throw new IllegalStateException("Unreachable");
    }
}
