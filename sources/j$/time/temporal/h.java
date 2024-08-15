package j$.time.temporal;
/* loaded from: classes2.dex */
enum h implements o {
    WEEK_BASED_YEARS("WeekBasedYears"),
    QUARTER_YEARS("QuarterYears");
    
    private final String a;

    static {
        j$.time.e eVar = j$.time.e.c;
    }

    h(String str) {
        this.a = str;
    }

    @Override // java.lang.Enum
    public final String toString() {
        return this.a;
    }
}
