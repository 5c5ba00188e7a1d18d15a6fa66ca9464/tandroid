package j$.time.format;

import j$.time.Instant;
import j$.time.LocalDate;
import j$.time.ZoneId;
import j$.time.ZoneOffset;
import j$.time.zone.ZoneRules;
import java.util.Locale;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class t {
    private j$.time.temporal.k a;
    private a b;
    private int c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:32:0x006c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public t(j$.time.temporal.k kVar, a aVar) {
        j$.time.temporal.a[] values;
        ZoneOffset zoneOffset;
        ZoneRules rules;
        j$.time.chrono.g b = aVar.b();
        ZoneId e = aVar.e();
        if (b != null || e != null) {
            int i = j$.time.temporal.t.a;
            j$.time.chrono.g gVar = (j$.time.chrono.g) kVar.d(j$.time.temporal.n.a);
            ZoneId zoneId = (ZoneId) kVar.d(j$.time.temporal.m.a);
            LocalDate localDate = null;
            b = j$.util.a.u(b, gVar) ? null : b;
            e = j$.util.a.u(e, zoneId) ? null : e;
            if (b != null || e != null) {
                j$.time.chrono.g gVar2 = b != null ? b : gVar;
                if (e != null) {
                    if (kVar.e(j$.time.temporal.a.INSTANT_SECONDS)) {
                        if (gVar2 == null) {
                            j$.time.chrono.h hVar = j$.time.chrono.h.a;
                        }
                        kVar = j$.time.m.i(Instant.h(kVar), e);
                    } else {
                        try {
                            rules = e.getRules();
                        } catch (j$.time.zone.c unused) {
                        }
                        if (rules.d()) {
                            zoneOffset = rules.getOffset(Instant.c);
                            if (zoneOffset instanceof ZoneOffset) {
                                j$.time.temporal.a aVar2 = j$.time.temporal.a.OFFSET_SECONDS;
                                if (kVar.e(aVar2) && kVar.a(aVar2) != e.getRules().getOffset(Instant.c).getTotalSeconds()) {
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
                        Objects.requireNonNull((j$.time.chrono.h) gVar2);
                        localDate = LocalDate.h(kVar);
                    } else if (b != j$.time.chrono.h.a || gVar != null) {
                        for (j$.time.temporal.a aVar3 : j$.time.temporal.a.values()) {
                            if (aVar3.f() && kVar.e(aVar3)) {
                                throw new j$.time.c("Unable to apply override chronology '" + b + "' because the temporal object being formatted contains date fields but does not represent a whole date: " + kVar);
                            }
                        }
                    }
                }
                kVar = new s(localDate, kVar, gVar2, zoneId);
            }
        }
        this.a = kVar;
        this.b = aVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        this.c--;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public w b() {
        return this.b.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Locale c() {
        return this.b.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public j$.time.temporal.k d() {
        return this.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Long e(j$.time.temporal.l lVar) {
        try {
            return Long.valueOf(this.a.c(lVar));
        } catch (j$.time.c e) {
            if (this.c > 0) {
                return null;
            }
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object f(j$.time.temporal.u uVar) {
        Object d = this.a.d(uVar);
        if (d == null && this.c == 0) {
            throw new j$.time.c("Unable to extract value: " + this.a.getClass());
        }
        return d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g() {
        this.c++;
    }

    public String toString() {
        return this.a.toString();
    }
}
