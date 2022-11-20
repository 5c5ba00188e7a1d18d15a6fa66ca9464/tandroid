package j$.time.format;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class n implements g {
    private final j$.time.temporal.k a;
    private final u b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(j$.time.temporal.k kVar, u uVar, c cVar) {
        this.a = kVar;
        this.b = uVar;
    }

    public String toString() {
        StringBuilder sb;
        Object obj;
        if (this.b == u.FULL) {
            sb = new StringBuilder();
            sb.append("Text(");
            obj = this.a;
        } else {
            sb = new StringBuilder();
            sb.append("Text(");
            sb.append(this.a);
            sb.append(",");
            obj = this.b;
        }
        sb.append(obj);
        sb.append(")");
        return sb.toString();
    }
}
