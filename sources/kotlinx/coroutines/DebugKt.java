package kotlinx.coroutines;

import java.util.concurrent.atomic.AtomicLong;
import kotlinx.coroutines.internal.SystemPropsKt;
/* compiled from: Debug.kt */
/* loaded from: classes.dex */
public final class DebugKt {
    private static final boolean ASSERTIONS_ENABLED = false;
    private static final boolean DEBUG;
    private static final boolean RECOVER_STACK_TRACES;

    public static final boolean getASSERTIONS_ENABLED() {
        return ASSERTIONS_ENABLED;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:
        if (r0.equals("auto") != false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0037, code lost:
        if (r0.equals("on") != false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0040, code lost:
        if (r0.equals("") != false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0042, code lost:
        r0 = true;
     */
    static {
        boolean assertions_enabled;
        String systemProp = SystemPropsKt.systemProp("kotlinx.coroutines.debug");
        boolean z = false;
        if (systemProp != null) {
            int hashCode = systemProp.hashCode();
            if (hashCode != 0) {
                if (hashCode != 3551) {
                    if (hashCode != 109935) {
                        if (hashCode == 3005871) {
                        }
                    } else if (systemProp.equals("off")) {
                        assertions_enabled = false;
                    }
                }
                throw new IllegalStateException(("System property 'kotlinx.coroutines.debug' has unrecognized value '" + ((Object) systemProp) + '\'').toString());
            }
            DEBUG = assertions_enabled;
            if (assertions_enabled && SystemPropsKt.systemProp("kotlinx.coroutines.stacktrace.recovery", true)) {
                z = true;
            }
            RECOVER_STACK_TRACES = z;
            new AtomicLong(0L);
        }
        assertions_enabled = getASSERTIONS_ENABLED();
        DEBUG = assertions_enabled;
        if (assertions_enabled) {
            z = true;
        }
        RECOVER_STACK_TRACES = z;
        new AtomicLong(0L);
    }

    public static final boolean getDEBUG() {
        return DEBUG;
    }

    public static final boolean getRECOVER_STACK_TRACES() {
        return RECOVER_STACK_TRACES;
    }
}
