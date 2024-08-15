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
public final class T2 {
    public static final T2 DISTINCT;
    public static final T2 ORDERED;
    public static final T2 SHORT_CIRCUIT;
    public static final T2 SIZED;
    public static final T2 SORTED;
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
    private static final /* synthetic */ T2[] v;
    private final Map a;
    private final int b;
    private final int c;
    private final int d;
    private final int e;

    static {
        S2 s2 = S2.SPLITERATOR;
        R2 f2 = f(s2);
        S2 s22 = S2.STREAM;
        f2.a(s22);
        S2 s23 = S2.OP;
        f2.a.put(s23, 3);
        T2 t2 = new T2("DISTINCT", 0, 0, f2);
        DISTINCT = t2;
        R2 f3 = f(s2);
        f3.a(s22);
        f3.a.put(s23, 3);
        T2 t22 = new T2("SORTED", 1, 1, f3);
        SORTED = t22;
        R2 f4 = f(s2);
        f4.a(s22);
        Map map = f4.a;
        map.put(s23, 3);
        S2 s24 = S2.TERMINAL_OP;
        map.put(s24, 2);
        S2 s25 = S2.UPSTREAM_TERMINAL_OP;
        map.put(s25, 2);
        T2 t23 = new T2("ORDERED", 2, 2, f4);
        ORDERED = t23;
        R2 f5 = f(s2);
        f5.a(s22);
        f5.a.put(s23, 2);
        T2 t24 = new T2("SIZED", 3, 3, f5);
        SIZED = t24;
        R2 f6 = f(s23);
        f6.a(s24);
        T2 t25 = new T2("SHORT_CIRCUIT", 4, 12, f6);
        SHORT_CIRCUIT = t25;
        v = new T2[]{t2, t22, t23, t24, t25};
        f = b(s2);
        g = b(s22);
        h = b(s23);
        b(s24);
        b(s25);
        int i2 = 0;
        for (T2 t26 : values()) {
            i2 |= t26.e;
        }
        i = i2;
        int i3 = g;
        j = i3;
        int i4 = i3 << 1;
        k = i4;
        l = i3 | i4;
        T2 t27 = DISTINCT;
        m = t27.c;
        n = t27.d;
        T2 t28 = SORTED;
        o = t28.c;
        p = t28.d;
        T2 t29 = ORDERED;
        q = t29.c;
        r = t29.d;
        T2 t210 = SIZED;
        s = t210.c;
        t = t210.d;
        u = SHORT_CIRCUIT.c;
    }

    private T2(String str, int i2, int i3, R2 r2) {
        S2[] values = S2.values();
        int length = values.length;
        int i4 = 0;
        while (true) {
            Map map = r2.a;
            if (i4 >= length) {
                this.a = map;
                int i5 = i3 * 2;
                this.b = i5;
                this.c = 1 << i5;
                this.d = 2 << i5;
                this.e = 3 << i5;
                return;
            }
            Map.-EL.a(map, values[i4], 0);
            i4++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(int i2, int i3) {
        return i2 | (i3 & (i2 == 0 ? i : ((((j & i2) << 1) | i2) | ((k & i2) >> 1)) ^ (-1)));
    }

    private static int b(S2 s2) {
        T2[] values;
        int i2 = 0;
        for (T2 t2 : values()) {
            i2 |= ((Integer) t2.a.get(s2)).intValue() << t2.b;
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(j$.util.Q q2) {
        int characteristics = q2.characteristics();
        int i2 = characteristics & 4;
        int i3 = f;
        return (i2 == 0 || q2.getComparator() == null) ? characteristics & i3 : characteristics & i3 & (-5);
    }

    private static R2 f(S2 s2) {
        R2 r2 = new R2(new EnumMap(S2.class));
        r2.a(s2);
        return r2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int g(int i2) {
        return i2 & ((i2 ^ (-1)) >> 1) & j;
    }

    public static T2 valueOf(String str) {
        return (T2) Enum.valueOf(T2.class, str);
    }

    public static T2[] values() {
        return (T2[]) v.clone();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean d(int i2) {
        return (i2 & this.e) == this.c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean e(int i2) {
        int i3 = this.e;
        return (i2 & i3) == i3;
    }
}
