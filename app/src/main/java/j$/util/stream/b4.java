package j$.util.stream;

import java.util.Map;
/* loaded from: classes2.dex */
class b4 {
    final Map a;

    public b4(Map map) {
        this.a = map;
    }

    public b4 a(c4 c4Var) {
        this.a.put(c4Var, 2);
        return this;
    }

    public b4 b(c4 c4Var) {
        this.a.put(c4Var, 1);
        return this;
    }

    public b4 c(c4 c4Var) {
        this.a.put(c4Var, 3);
        return this;
    }
}
