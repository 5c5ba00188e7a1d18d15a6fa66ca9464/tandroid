package j$.time.zone;

import java.security.PrivilegedAction;
import java.util.List;
/* loaded from: classes2.dex */
class d implements PrivilegedAction {
    final /* synthetic */ List a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(List list) {
        this.a = list;
    }

    @Override // java.security.PrivilegedAction
    public Object run() {
        String property = System.getProperty("java.time.zone.DefaultZoneRulesProvider");
        if (property == null) {
            f.d(new e());
            return null;
        }
        try {
            f fVar = (f) f.class.cast(Class.forName(property, true, f.class.getClassLoader()).newInstance());
            f.d(fVar);
            this.a.add(fVar);
            return null;
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
