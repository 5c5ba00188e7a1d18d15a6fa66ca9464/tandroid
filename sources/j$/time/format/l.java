package j$.time.format;
/* loaded from: classes2.dex */
enum l implements g {
    SENSITIVE,
    INSENSITIVE,
    STRICT,
    LENIENT;

    @Override // java.lang.Enum
    public String toString() {
        int ordinal = ordinal();
        if (ordinal != 0) {
            if (ordinal != 1) {
                if (ordinal != 2) {
                    if (ordinal == 3) {
                        return "ParseStrict(false)";
                    }
                    throw new IllegalStateException("Unreachable");
                }
                return "ParseStrict(true)";
            }
            return "ParseCaseSensitive(false)";
        }
        return "ParseCaseSensitive(true)";
    }
}
