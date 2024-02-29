package j$.time.temporal;
/* loaded from: classes2.dex */
enum h implements v {
    WEEK_BASED_YEARS("WeekBasedYears", j$.time.d.c(31556952)),
    QUARTER_YEARS("QuarterYears", j$.time.d.c(7889238));
    
    private final String a;

    h(String str, j$.time.d dVar) {
        this.a = str;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.a;
    }
}
