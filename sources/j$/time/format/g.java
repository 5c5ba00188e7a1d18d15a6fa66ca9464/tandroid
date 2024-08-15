package j$.time.format;

import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class g implements h {
    private final h[] a;
    private final boolean b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(ArrayList arrayList, boolean z) {
        this((h[]) arrayList.toArray(new h[arrayList.size()]), z);
    }

    g(h[] hVarArr, boolean z) {
        this.a = hVarArr;
        this.b = z;
    }

    @Override // j$.time.format.h
    public final boolean a(t tVar, StringBuilder sb) {
        int length = sb.length();
        boolean z = this.b;
        if (z) {
            tVar.g();
        }
        try {
            for (h hVar : this.a) {
                if (!hVar.a(tVar, sb)) {
                    sb.setLength(length);
                    return true;
                }
            }
            if (z) {
                tVar.a();
            }
            return true;
        } finally {
            if (z) {
                tVar.a();
            }
        }
    }

    public final g b() {
        return !this.b ? this : new g(this.a, false);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        h[] hVarArr = this.a;
        if (hVarArr != null) {
            boolean z = this.b;
            sb.append(z ? "[" : "(");
            for (h hVar : hVarArr) {
                sb.append(hVar);
            }
            sb.append(z ? "]" : ")");
        }
        return sb.toString();
    }
}
