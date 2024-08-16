package j$.time.format;

import j$.time.Instant;
import j$.time.LocalDate;
import j$.time.ZoneId;
import j$.time.ZoneOffset;
import j$.time.zone.ZoneRules;
import java.util.Locale;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class s {
    private j$.time.temporal.k a;
    private a b;
    private int c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0074  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public s(j$.time.temporal.k kVar, a aVar) {
        j$.time.temporal.a[] values;
        ZoneOffset zoneOffset;
        ZoneRules rules;
        j$.time.chrono.f b = aVar.b();
        ZoneId e = aVar.e();
        if (b != null || e != null) {
            j$.time.chrono.f fVar = (j$.time.chrono.f) kVar.c(j$.time.temporal.j.d());
            ZoneId zoneId = (ZoneId) kVar.c(j$.time.temporal.j.j());
            LocalDate localDate = null;
            b = j$.util.a.A(b, fVar) ? null : b;
            e = j$.util.a.A(e, zoneId) ? null : e;
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
                            zoneOffset = rules.getOffset(Instant.c);
                            if (zoneOffset instanceof ZoneOffset) {
                                j$.time.temporal.a aVar2 = j$.time.temporal.a.OFFSET_SECONDS;
                                if (kVar.e(aVar2) && kVar.d(aVar2) != e.getRules().getOffset(Instant.c).getTotalSeconds()) {
                                    throw new j$.time.c("Unable to apply override zone '" + e + "' because the temporal object being formatted has a different offset but does not represent an instant: " + kVar);
                                }
                            }
                        }
                        zoneOffset = e;
                        if (zoneOffset instanceof ZoneOffset) {
                        }
                    }
                }
                zoneId = e != null ? e : zoneId;
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
                kVar = new r(localDate, kVar, fVar2, zoneId);
            }
        }
        this.a = kVar;
        this.b = aVar;
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
            return Long.valueOf(this.a.b(lVar));
        } catch (j$.time.c e) {
            if (this.c > 0) {
                return null;
            }
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object f(j$.time.temporal.n nVar) {
        j$.time.temporal.k kVar = this.a;
        Object c = kVar.c(nVar);
        if (c == null && this.c == 0) {
            throw new j$.time.c("Unable to extract value: " + kVar.getClass());
        }
        return c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void g() {
        this.c++;
    }

    public final String toString() {
        return this.a.toString();
    }
}
