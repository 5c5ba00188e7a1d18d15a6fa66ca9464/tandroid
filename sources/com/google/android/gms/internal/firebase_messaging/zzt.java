package com.google.android.gms.internal.firebase_messaging;

import java.io.PrintStream;
import org.telegram.messenger.NotificationCenter;
/* loaded from: classes.dex */
public abstract class zzt {
    static final zzn zza;

    static {
        zzn zzrVar;
        Integer num = null;
        try {
            try {
                num = (Integer) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
            } catch (Exception e) {
                PrintStream printStream = System.err;
                printStream.println("Failed to retrieve value from android.os.Build$VERSION.SDK_INT due to the following exception.");
                e.printStackTrace(printStream);
            }
            zzrVar = (num == null || num.intValue() < 19) ? !Boolean.getBoolean("com.google.devtools.build.android.desugar.runtime.twr_disable_mimic") ? new zzq() : new zzr() : new zzs();
        } catch (Throwable th) {
            PrintStream printStream2 = System.err;
            String name = zzr.class.getName();
            StringBuilder sb = new StringBuilder(name.length() + NotificationCenter.didUpdateConnectionState);
            sb.append("An error has occurred when initializing the try-with-resources desuguring strategy. The default strategy ");
            sb.append(name);
            sb.append("will be used. The error is: ");
            printStream2.println(sb.toString());
            th.printStackTrace(printStream2);
            zzrVar = new zzr();
        }
        zza = zzrVar;
    }

    public static void zza(Throwable th, Throwable th2) {
        zza.zza(th, th2);
    }
}
