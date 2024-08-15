package j$.time.format;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class n implements h {
    private final String a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(String str) {
        this.a = str;
    }

    @Override // j$.time.format.h
    public final boolean a(t tVar, StringBuilder sb) {
        sb.append(this.a);
        return true;
    }

    public final String toString() {
        String replace = this.a.replace("'", "''");
        return "'" + replace + "'";
    }
}
