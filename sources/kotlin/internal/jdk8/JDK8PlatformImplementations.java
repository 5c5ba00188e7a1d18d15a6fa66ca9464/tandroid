package kotlin.internal.jdk8;

import kotlin.internal.jdk7.JDK7PlatformImplementations;
import kotlin.random.Random;
import kotlin.random.jdk8.PlatformThreadLocalRandom;

/* loaded from: classes.dex */
public class JDK8PlatformImplementations extends JDK7PlatformImplementations {

    private static final class ReflectSdkVersion {
        public static final ReflectSdkVersion INSTANCE = new ReflectSdkVersion();
        public static final Integer sdkVersion;

        static {
            Integer num;
            Object obj;
            Integer num2 = null;
            try {
                obj = Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
            } catch (Throwable unused) {
            }
            if (obj instanceof Integer) {
                num = (Integer) obj;
                if (num != null && num.intValue() > 0) {
                    num2 = num;
                }
                sdkVersion = num2;
            }
            num = null;
            if (num != null) {
                num2 = num;
            }
            sdkVersion = num2;
        }

        private ReflectSdkVersion() {
        }
    }

    private final boolean sdkIsNullOrAtLeast(int i) {
        Integer num = ReflectSdkVersion.sdkVersion;
        return num == null || num.intValue() >= i;
    }

    @Override // kotlin.internal.PlatformImplementations
    public Random defaultPlatformRandom() {
        return sdkIsNullOrAtLeast(34) ? new PlatformThreadLocalRandom() : super.defaultPlatformRandom();
    }
}
