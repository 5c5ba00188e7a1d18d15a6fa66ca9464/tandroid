package j$.time.format;

/* loaded from: classes2.dex */
final class e implements g {
    private final char a;

    e(char c) {
        this.a = c;
    }

    @Override // j$.time.format.g
    public final boolean a(s sVar, StringBuilder sb) {
        sb.append(this.a);
        return true;
    }

    public final String toString() {
        char c = this.a;
        if (c == '\'') {
            return "''";
        }
        return "'" + c + "'";
    }
}
