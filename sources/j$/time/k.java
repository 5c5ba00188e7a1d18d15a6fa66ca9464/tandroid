package j$.time;

import j$.time.zone.ZoneRules;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class k extends ZoneId {
    private final String b;
    private final transient ZoneRules c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k(String str, ZoneRules zoneRules) {
        this.b = str;
        this.c = zoneRules;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static k h(String str, boolean z) {
        int length = str.length();
        if (length >= 2) {
            for (int i = 0; i < length; i++) {
                char charAt = str.charAt(i);
                if ((charAt < 'a' || charAt > 'z') && ((charAt < 'A' || charAt > 'Z') && ((charAt != '/' || i == 0) && ((charAt < '0' || charAt > '9' || i == 0) && ((charAt != '~' || i == 0) && ((charAt != '.' || i == 0) && ((charAt != '_' || i == 0) && ((charAt != '+' || i == 0) && (charAt != '-' || i == 0))))))))) {
                    throw new c("Invalid ID for region-based ZoneId, invalid format: " + str);
                }
            }
            ZoneRules zoneRules = null;
            try {
                zoneRules = j$.time.zone.f.a(str, true);
            } catch (j$.time.zone.c e) {
                if (z) {
                    throw e;
                }
            }
            return new k(str, zoneRules);
        }
        throw new c("Invalid ID for region-based ZoneId, invalid format: " + str);
    }

    @Override // j$.time.ZoneId
    public String getId() {
        return this.b;
    }

    @Override // j$.time.ZoneId
    public ZoneRules getRules() {
        ZoneRules zoneRules = this.c;
        return zoneRules != null ? zoneRules : j$.time.zone.f.a(this.b, false);
    }
}
