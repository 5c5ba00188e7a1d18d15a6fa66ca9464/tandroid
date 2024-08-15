package j$.time.format;

import j$.time.LocalDate;
import j$.time.ZoneId;
import java.util.Locale;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class t {
    private j$.time.temporal.k a;
    private b b;
    private int c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t(j$.time.temporal.k kVar, b bVar) {
        j$.time.temporal.a[] values;
        j$.time.chrono.d b = bVar.b();
        if (b != null) {
            j$.time.chrono.d dVar = (j$.time.chrono.d) kVar.d(j$.time.temporal.j.d());
            ZoneId zoneId = (ZoneId) kVar.d(j$.time.temporal.j.j());
            LocalDate localDate = null;
            b = j$.util.a.r(b, dVar) ? null : b;
            j$.util.a.r(null, zoneId);
            if (b != null) {
                j$.time.chrono.d dVar2 = b != null ? b : dVar;
                if (b != null) {
                    if (kVar.b(j$.time.temporal.a.EPOCH_DAY)) {
                        ((j$.time.chrono.e) dVar2).getClass();
                        localDate = LocalDate.h(kVar);
                    } else if (b != j$.time.chrono.e.a || dVar != null) {
                        for (j$.time.temporal.a aVar : j$.time.temporal.a.values()) {
                            if (aVar.isDateBased() && kVar.b(aVar)) {
                                throw new j$.time.d("Unable to apply override chronology '" + b + "' because the temporal object being formatted contains date fields but does not represent a whole date: " + kVar);
                            }
                        }
                    }
                }
                kVar = new s(localDate, kVar, dVar2, zoneId);
            }
        }
        this.a = kVar;
        this.b = bVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a() {
        this.c--;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final w b() {
        return this.b.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Locale c() {
        return this.b.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final j$.time.temporal.k d() {
        return this.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Long e(j$.time.temporal.l lVar) {
        try {
            return Long.valueOf(this.a.c(lVar));
        } catch (j$.time.d e) {
            if (this.c > 0) {
                return null;
            }
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object f(j$.time.temporal.n nVar) {
        Object d = this.a.d(nVar);
        if (d == null && this.c == 0) {
            throw new j$.time.d("Unable to extract value: " + this.a.getClass());
        }
        return d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void g() {
        this.c++;
    }

    public final String toString() {
        return this.a.toString();
    }
}
