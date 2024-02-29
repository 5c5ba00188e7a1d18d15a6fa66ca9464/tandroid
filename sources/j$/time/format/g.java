package j$.time.format;

import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class g implements h {
    private final h[] a;
    private final boolean b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(List list, boolean z) {
        this.a = (h[]) list.toArray(new h[list.size()]);
        this.b = z;
    }

    g(h[] hVarArr, boolean z) {
        this.a = hVarArr;
        this.b = z;
    }

    @Override // j$.time.format.h
    public boolean a(t tVar, StringBuilder sb) {
        int length = sb.length();
        if (this.b) {
            tVar.g();
        }
        try {
            for (h hVar : this.a) {
                if (!hVar.a(tVar, sb)) {
                    sb.setLength(length);
                    return true;
                }
            }
            if (this.b) {
                tVar.a();
            }
            return true;
        } finally {
            if (this.b) {
                tVar.a();
            }
        }
    }

    public g b(boolean z) {
        return z == this.b ? this : new g(this.a, z);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.a != null) {
            sb.append(this.b ? "[" : "(");
            for (h hVar : this.a) {
                sb.append(hVar);
            }
            sb.append(this.b ? "]" : ")");
        }
        return sb.toString();
    }
}
