package j$.time.temporal;
/* loaded from: classes2.dex */
public abstract class h extends Enum implements k {
    public static final h DAY_OF_QUARTER;
    public static final h QUARTER_OF_YEAR;
    public static final h WEEK_BASED_YEAR;
    public static final h WEEK_OF_WEEK_BASED_YEAR;
    private static final /* synthetic */ h[] a;

    static {
        d dVar = new d("DAY_OF_QUARTER", 0);
        DAY_OF_QUARTER = dVar;
        e eVar = new e("QUARTER_OF_YEAR", 1);
        QUARTER_OF_YEAR = eVar;
        f fVar = new f("WEEK_OF_WEEK_BASED_YEAR", 2);
        WEEK_OF_WEEK_BASED_YEAR = fVar;
        g gVar = new g("WEEK_BASED_YEAR", 3);
        WEEK_BASED_YEAR = gVar;
        a = new h[]{dVar, eVar, fVar, gVar};
    }

    public h(String str, int i, c cVar) {
        super(str, i);
    }

    public static h valueOf(String str) {
        return (h) Enum.valueOf(h.class, str);
    }

    public static h[] values() {
        return (h[]) a.clone();
    }
}
