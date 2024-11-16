package j$.time.format;

/* loaded from: classes2.dex */
final class m implements g {
    private final String a;

    m(String str) {
        this.a = str;
    }

    @Override // j$.time.format.g
    public final boolean a(s sVar, StringBuilder sb) {
        sb.append(this.a);
        return true;
    }

    public final String toString() {
        return "'" + this.a.replace("'", "''") + "'";
    }
}
