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
public final class e4 {
    public static final e4 DISTINCT;
    public static final e4 ORDERED;
    public static final e4 SHORT_CIRCUIT;
    public static final e4 SIZED;
    public static final e4 SORTED;
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
    private static final /* synthetic */ e4[] v;
    private final Map a;
    private final int b;
    private final int c;
    private final int d;
    private final int e;

    static {
        d4 d4Var = d4.SPLITERATOR;
        c4 f2 = f(d4Var);
        d4 d4Var2 = d4.STREAM;
        f2.b(d4Var2);
        d4 d4Var3 = d4.OP;
        f2.c(d4Var3);
        e4 e4Var = new e4("DISTINCT", 0, 0, f2);
        DISTINCT = e4Var;
        c4 f3 = f(d4Var);
        f3.b(d4Var2);
        f3.c(d4Var3);
        e4 e4Var2 = new e4("SORTED", 1, 1, f3);
        SORTED = e4Var2;
        c4 f4 = f(d4Var);
        f4.b(d4Var2);
        f4.c(d4Var3);
        d4 d4Var4 = d4.TERMINAL_OP;
        f4.a(d4Var4);
        d4 d4Var5 = d4.UPSTREAM_TERMINAL_OP;
        f4.a(d4Var5);
        e4 e4Var3 = new e4("ORDERED", 2, 2, f4);
        ORDERED = e4Var3;
        c4 f5 = f(d4Var);
        f5.b(d4Var2);
        f5.a(d4Var3);
        e4 e4Var4 = new e4("SIZED", 3, 3, f5);
        SIZED = e4Var4;
        c4 f6 = f(d4Var3);
        f6.b(d4Var4);
        e4 e4Var5 = new e4("SHORT_CIRCUIT", 4, 12, f6);
        SHORT_CIRCUIT = e4Var5;
        v = new e4[]{e4Var, e4Var2, e4Var3, e4Var4, e4Var5};
        f = b(d4Var);
        int b = b(d4Var2);
        g = b;
        h = b(d4Var3);
        b(d4Var4);
        b(d4Var5);
        int i2 = 0;
        for (e4 e4Var6 : values()) {
            i2 |= e4Var6.e;
        }
        i = i2;
        j = b;
        int i3 = b << 1;
        k = i3;
        l = b | i3;
        m = e4Var.c;
        n = e4Var.d;
        o = e4Var2.c;
        p = e4Var2.d;
        q = e4Var3.c;
        r = e4Var3.d;
        s = e4Var4.c;
        t = e4Var4.d;
        u = e4Var5.c;
    }

    private e4(String str, int i2, int i3, c4 c4Var) {
        d4[] values;
        for (d4 d4Var : d4.values()) {
            Map map = c4Var.a;
            if (map instanceof j$.util.Map) {
                ((j$.util.Map) map).putIfAbsent(d4Var, 0);
            } else {
                Map.-CC.$default$putIfAbsent(map, d4Var, 0);
            }
        }
        this.a = c4Var.a;
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

    private static int b(d4 d4Var) {
        e4[] values;
        int i2 = 0;
        for (e4 e4Var : values()) {
            i2 |= ((Integer) e4Var.a.get(d4Var)).intValue() << e4Var.b;
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(j$.util.t tVar) {
        int characteristics = tVar.characteristics();
        return ((characteristics & 4) == 0 || tVar.getComparator() == null) ? f & characteristics : f & characteristics & (-5);
    }

    private static c4 f(d4 d4Var) {
        EnumMap enumMap = new EnumMap(d4.class);
        c4 c4Var = new c4(enumMap);
        enumMap.put((EnumMap) d4Var, (d4) 1);
        return c4Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int g(int i2) {
        return i2 & ((i2 ^ (-1)) >> 1) & j;
    }

    public static e4 valueOf(String str) {
        return (e4) Enum.valueOf(e4.class, str);
    }

    public static e4[] values() {
        return (e4[]) v.clone();
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
