package j$.time.format;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public enum m implements h {
    SENSITIVE,
    INSENSITIVE,
    STRICT,
    LENIENT;

    @Override // j$.time.format.h
    public boolean a(t tVar, StringBuilder sb) {
        return true;
    }

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
