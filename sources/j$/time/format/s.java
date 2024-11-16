package j$.time.format;

import j$.time.Instant;
import j$.time.LocalDate;
import j$.time.ZoneId;
import j$.time.ZoneOffset;
import j$.time.zone.ZoneRules;
import java.util.Locale;

/* loaded from: classes2.dex */
final class s {
    private j$.time.temporal.k a;
    private a b;
    private int c;

    /* JADX WARN: Removed duplicated region for block: B:29:0x0074  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    s(j$.time.temporal.k kVar, a aVar) {
        ZoneId zoneId;
        ZoneRules rules;
        j$.time.chrono.f b = aVar.b();
        ZoneId e = aVar.e();
        if (b != null || e != null) {
            j$.time.chrono.f fVar = (j$.time.chrono.f) kVar.c(j$.time.temporal.j.d());
            ZoneId zoneId2 = (ZoneId) kVar.c(j$.time.temporal.j.j());
            LocalDate localDate = null;
            b = j$.util.a.A(b, fVar) ? null : b;
            e = j$.util.a.A(e, zoneId2) ? null : e;
            if (b != null || e != null) {
                j$.time.chrono.f fVar2 = b != null ? b : fVar;
                if (e != null) {
                    if (kVar.e(j$.time.temporal.a.INSTANT_SECONDS)) {
                        fVar2 = fVar2 == null ? j$.time.chrono.g.a : fVar2;
                        Instant h = Instant.h(kVar);
                        ((j$.time.chrono.g) fVar2).getClass();
                        kVar = j$.time.p.g(h, e);
                    } else {
                        try {
                            rules = e.getRules();
                        } catch (j$.time.zone.c unused) {
                        }
                        if (rules.d()) {
                            zoneId = rules.getOffset(Instant.c);
                            if (zoneId instanceof ZoneOffset) {
                                j$.time.temporal.a aVar2 = j$.time.temporal.a.OFFSET_SECONDS;
                                if (kVar.e(aVar2) && kVar.d(aVar2) != e.getRules().getOffset(Instant.c).getTotalSeconds()) {
                                    throw new j$.time.c("Unable to apply override zone '" + e + "' because the temporal object being formatted has a different offset but does not represent an instant: " + kVar);
                                }
                            }
                        }
                        zoneId = e;
                        if (zoneId instanceof ZoneOffset) {
                        }
                    }
                }
                zoneId2 = e != null ? e : zoneId2;
                if (b != null) {
                    if (kVar.e(j$.time.temporal.a.EPOCH_DAY)) {
                        ((j$.time.chrono.g) fVar2).getClass();
                        localDate = LocalDate.h(kVar);
                    } else if (b != j$.time.chrono.g.a || fVar != null) {
                        for (j$.time.temporal.a aVar3 : j$.time.temporal.a.values()) {
                            if (aVar3.e() && kVar.e(aVar3)) {
                                throw new j$.time.c("Unable to apply override chronology '" + b + "' because the temporal object being formatted contains date fields but does not represent a whole date: " + kVar);
                            }
                        }
                    }
                }
                kVar = new r(localDate, kVar, fVar2, zoneId2);
            }
        }
        this.a = kVar;
        this.b = aVar;
    }

    final void a() {
        this.c--;
    }

    final w b() {
        return this.b.c();
    }

    final Locale c() {
        return this.b.d();
    }

    final j$.time.temporal.k d() {
        return this.a;
    }

    final Long e(j$.time.temporal.l lVar) {
        try {
            return Long.valueOf(this.a.b(lVar));
        } catch (j$.time.c e) {
            if (this.c > 0) {
                return null;
            }
            throw e;
        }
    }

    final Object f(j$.time.temporal.n nVar) {
        j$.time.temporal.k kVar = this.a;
        Object c = kVar.c(nVar);
        if (c != null || this.c != 0) {
            return c;
        }
        throw new j$.time.c("Unable to extract value: " + kVar.getClass());
    }

    final void g() {
        this.c++;
    }

    public final String toString() {
        return this.a.toString();
    }
}
