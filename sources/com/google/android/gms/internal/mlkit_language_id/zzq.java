package com.google.android.gms.internal.mlkit_language_id;

import java.io.PrintStream;
import org.telegram.messenger.NotificationCenter;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final class zzq {
    private static final zzt zza;
    private static final int zzb;

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    static final class zza extends zzt {
        zza() {
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzt
        public final void zza(Throwable th, Throwable th2) {
        }
    }

    public static void zza(Throwable th, Throwable th2) {
        zza.zza(th, th2);
    }

    private static Integer zza() {
        try {
            return (Integer) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
        } catch (Exception e) {
            PrintStream printStream = System.err;
            printStream.println("Failed to retrieve value from android.os.Build$VERSION.SDK_INT due to the following exception.");
            e.printStackTrace(printStream);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0063  */
    static {
        Integer num;
        zzt zzaVar;
        try {
            num = zza();
        } catch (Throwable th) {
            th = th;
            num = null;
        }
        if (num != null) {
            try {
            } catch (Throwable th2) {
                th = th2;
                PrintStream printStream = System.err;
                String name = zza.class.getName();
                StringBuilder sb = new StringBuilder(name.length() + NotificationCenter.didUpdateConnectionState);
                sb.append("An error has occurred when initializing the try-with-resources desuguring strategy. The default strategy ");
                sb.append(name);
                sb.append("will be used. The error is: ");
                printStream.println(sb.toString());
                th.printStackTrace(printStream);
                zzaVar = new zza();
                zza = zzaVar;
                zzb = num != null ? num.intValue() : 1;
            }
            if (num.intValue() >= 19) {
                zzaVar = new zzw();
                zza = zzaVar;
                zzb = num != null ? num.intValue() : 1;
            }
        }
        if (!Boolean.getBoolean("com.google.devtools.build.android.desugar.runtime.twr_disable_mimic")) {
            zzaVar = new zzu();
        } else {
            zzaVar = new zza();
        }
        zza = zzaVar;
        zzb = num != null ? num.intValue() : 1;
    }
}
