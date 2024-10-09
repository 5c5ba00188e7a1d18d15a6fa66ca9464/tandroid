package j$.time.temporal;

/* loaded from: classes2.dex */
enum h implements o {
    WEEK_BASED_YEARS("WeekBasedYears"),
    QUARTER_YEARS("QuarterYears");

    private final String a;

    static {
        j$.time.d dVar = j$.time.d.c;
    }

    h(String str) {
        this.a = str;
    }

    @Override // java.lang.Enum
    public final String toString() {
        return this.a;
    }
}
