package j$.util.stream;

import j$.util.Map;
import java.util.EnumMap;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum DISTINCT uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:444)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByRegister(EnumVisitor.java:391)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:320)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:258)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes2.dex */
public final class d4 {
    public static final d4 DISTINCT;
    public static final d4 ORDERED;
    public static final d4 SHORT_CIRCUIT;
    public static final d4 SIZED;
    public static final d4 SORTED;
    static final int f;
    static final int g;
    static final int h;
    private static final int i;
    private static final int j;
    private static final int k;
    static final int l;
    static final int m;
    static final int n;
    static final int o;
    static final int p;
    static final int q;
    static final int r;
    static final int s;
    static final int t;
    static final int u;
    private static final /* synthetic */ d4[] v;
    private final Map a;
    private final int b;
    private final int c;
    private final int d;
    private final int e;

    static {
        c4 c4Var = c4.SPLITERATOR;
        b4 f2 = f(c4Var);
        c4 c4Var2 = c4.STREAM;
        f2.b(c4Var2);
        c4 c4Var3 = c4.OP;
        f2.c(c4Var3);
        d4 d4Var = new d4("DISTINCT", 0, 0, f2);
        DISTINCT = d4Var;
        b4 f3 = f(c4Var);
        f3.b(c4Var2);
        f3.c(c4Var3);
        d4 d4Var2 = new d4("SORTED", 1, 1, f3);
        SORTED = d4Var2;
        b4 f4 = f(c4Var);
        f4.b(c4Var2);
        f4.c(c4Var3);
        c4 c4Var4 = c4.TERMINAL_OP;
        f4.a(c4Var4);
        c4 c4Var5 = c4.UPSTREAM_TERMINAL_OP;
        f4.a(c4Var5);
        d4 d4Var3 = new d4("ORDERED", 2, 2, f4);
        ORDERED = d4Var3;
        b4 f5 = f(c4Var);
        f5.b(c4Var2);
        f5.a(c4Var3);
        d4 d4Var4 = new d4("SIZED", 3, 3, f5);
        SIZED = d4Var4;
        b4 f6 = f(c4Var3);
        f6.b(c4Var4);
        d4 d4Var5 = new d4("SHORT_CIRCUIT", 4, 12, f6);
        SHORT_CIRCUIT = d4Var5;
        v = new d4[]{d4Var, d4Var2, d4Var3, d4Var4, d4Var5};
        f = b(c4Var);
        int b = b(c4Var2);
        g = b;
        h = b(c4Var3);
        b(c4Var4);
        b(c4Var5);
        int i2 = 0;
        for (d4 d4Var6 : values()) {
            i2 |= d4Var6.e;
        }
        i = i2;
        j = b;
        int i3 = b << 1;
        k = i3;
        l = b | i3;
        m = d4Var.c;
        n = d4Var.d;
        o = d4Var2.c;
        p = d4Var2.d;
        q = d4Var3.c;
        r = d4Var3.d;
        s = d4Var4.c;
        t = d4Var4.d;
        u = d4Var5.c;
    }

    private d4(String str, int i2, int i3, b4 b4Var) {
        c4[] values;
        for (c4 c4Var : c4.values()) {
            Map map = b4Var.a;
            if (map instanceof j$.util.Map) {
                ((j$.util.Map) map).putIfAbsent(c4Var, 0);
            } else {
                Map.-CC.$default$putIfAbsent(map, c4Var, 0);
            }
        }
        this.a = b4Var.a;
        int i4 = i3 * 2;
        this.b = i4;
        this.c = 1 << i4;
        this.d = 2 << i4;
        this.e = 3 << i4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(int i2, int i3) {
        return i2 | (i3 & (i2 == 0 ? i : ((((j & i2) << 1) | i2) | ((k & i2) >> 1)) ^ (-1)));
    }

    private static int b(c4 c4Var) {
        d4[] values;
        int i2 = 0;
        for (d4 d4Var : values()) {
            i2 |= ((Integer) d4Var.a.get(c4Var)).intValue() << d4Var.b;
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(j$.util.s sVar) {
        int characteristics = sVar.characteristics();
        return ((characteristics & 4) == 0 || sVar.getComparator() == null) ? f & characteristics : f & characteristics & (-5);
    }

    private static b4 f(c4 c4Var) {
        EnumMap enumMap = new EnumMap(c4.class);
        b4 b4Var = new b4(enumMap);
        enumMap.put((EnumMap) c4Var, (c4) 1);
        return b4Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int g(int i2) {
        return i2 & ((i2 ^ (-1)) >> 1) & j;
    }

    public static d4 valueOf(String str) {
        return (d4) Enum.valueOf(d4.class, str);
    }

    public static d4[] values() {
        return (d4[]) v.clone();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d(int i2) {
        return (i2 & this.e) == this.c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean e(int i2) {
        int i3 = this.e;
        return (i2 & i3) == i3;
    }
}
