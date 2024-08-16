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
public final class S2 {
    public static final S2 DISTINCT;
    public static final S2 ORDERED;
    public static final S2 SHORT_CIRCUIT;
    public static final S2 SIZED;
    public static final S2 SORTED;
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
    private static final /* synthetic */ S2[] v;
    private final Map a;
    private final int b;
    private final int c;
    private final int d;
    private final int e;

    static {
        R2 r2 = R2.SPLITERATOR;
        Q2 f2 = f(r2);
        R2 r22 = R2.STREAM;
        f2.a(r22);
        R2 r23 = R2.OP;
        f2.a.put(r23, 3);
        S2 s2 = new S2("DISTINCT", 0, 0, f2);
        DISTINCT = s2;
        Q2 f3 = f(r2);
        f3.a(r22);
        f3.a.put(r23, 3);
        S2 s22 = new S2("SORTED", 1, 1, f3);
        SORTED = s22;
        Q2 f4 = f(r2);
        f4.a(r22);
        Map map = f4.a;
        map.put(r23, 3);
        R2 r24 = R2.TERMINAL_OP;
        map.put(r24, 2);
        R2 r25 = R2.UPSTREAM_TERMINAL_OP;
        map.put(r25, 2);
        S2 s23 = new S2("ORDERED", 2, 2, f4);
        ORDERED = s23;
        Q2 f5 = f(r2);
        f5.a(r22);
        f5.a.put(r23, 2);
        S2 s24 = new S2("SIZED", 3, 3, f5);
        SIZED = s24;
        Q2 f6 = f(r23);
        f6.a(r24);
        S2 s25 = new S2("SHORT_CIRCUIT", 4, 12, f6);
        SHORT_CIRCUIT = s25;
        v = new S2[]{s2, s22, s23, s24, s25};
        f = b(r2);
        g = b(r22);
        h = b(r23);
        b(r24);
        b(r25);
        int i2 = 0;
        for (S2 s26 : values()) {
            i2 |= s26.e;
        }
        i = i2;
        int i3 = g;
        j = i3;
        int i4 = i3 << 1;
        k = i4;
        l = i3 | i4;
        S2 s27 = DISTINCT;
        m = s27.c;
        n = s27.d;
        S2 s28 = SORTED;
        o = s28.c;
        p = s28.d;
        S2 s29 = ORDERED;
        q = s29.c;
        r = s29.d;
        S2 s210 = SIZED;
        s = s210.c;
        t = s210.d;
        u = SHORT_CIRCUIT.c;
    }

    private S2(String str, int i2, int i3, Q2 q2) {
        R2[] values = R2.values();
        int length = values.length;
        int i4 = 0;
        while (true) {
            Map map = q2.a;
            if (i4 >= length) {
                this.a = map;
                int i5 = i3 * 2;
                this.b = i5;
                this.c = 1 << i5;
                this.d = 2 << i5;
                this.e = 3 << i5;
                return;
            }
            Map.-EL.b(map, values[i4], 0);
            i4++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(int i2, int i3) {
        return i2 | (i3 & (i2 == 0 ? i : ((((j & i2) << 1) | i2) | ((k & i2) >> 1)) ^ (-1)));
    }

    private static int b(R2 r2) {
        S2[] values;
        int i2 = 0;
        for (S2 s2 : values()) {
            i2 |= ((Integer) s2.a.get(r2)).intValue() << s2.b;
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

    private static Q2 f(R2 r2) {
        Q2 q2 = new Q2(new EnumMap(R2.class));
        q2.a(r2);
        return q2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int g(int i2) {
        return i2 & ((i2 ^ (-1)) >> 1) & j;
    }

    public static S2 valueOf(String str) {
        return (S2) Enum.valueOf(S2.class, str);
    }

    public static S2[] values() {
        return (S2[]) v.clone();
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
