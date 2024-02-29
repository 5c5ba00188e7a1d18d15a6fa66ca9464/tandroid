package j$.time.format;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class f implements h {
    private final char a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(char c) {
        this.a = c;
    }

    @Override // j$.time.format.h
    public boolean a(t tVar, StringBuilder sb) {
        sb.append(this.a);
        return true;
    }

    public String toString() {
        if (this.a == '\'') {
            return "''";
        }
        return "'" + this.a + "'";
    }
}
