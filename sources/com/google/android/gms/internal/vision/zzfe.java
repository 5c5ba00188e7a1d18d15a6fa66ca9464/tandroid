package com.google.android.gms.internal.vision;

import java.io.PrintStream;
import org.telegram.messenger.MessagesStorage;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes.dex */
public final class zzfe {
    private static final zzfd zza;

    /* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
    /* loaded from: classes.dex */
    static final class zza extends zzfd {
        zza() {
        }

        @Override // com.google.android.gms.internal.vision.zzfd
        public final void zza(Throwable th) {
            th.printStackTrace();
        }
    }

    public static void zza(Throwable th) {
        zza.zza(th);
    }

    private static Integer zza() {
        try {
            return (Integer) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
        } catch (Exception e) {
            System.err.println("Failed to retrieve value from android.os.Build$VERSION.SDK_INT due to the following exception.");
            e.printStackTrace(System.err);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    static {
        Integer num;
        zzfd zzaVar;
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
                StringBuilder sb = new StringBuilder(name.length() + MessagesStorage.LAST_DB_VERSION);
                sb.append("An error has occurred when initializing the try-with-resources desuguring strategy. The default strategy ");
                sb.append(name);
                sb.append("will be used. The error is: ");
                printStream.println(sb.toString());
                th.printStackTrace(System.err);
                zzaVar = new zza();
                zza = zzaVar;
                if (num == null) {
                }
            }
            if (num.intValue() >= 19) {
                zzaVar = new zzfj();
                zza = zzaVar;
                if (num == null) {
                    return;
                }
                num.intValue();
                return;
            }
        }
        if (!Boolean.getBoolean("com.google.devtools.build.android.desugar.runtime.twr_disable_mimic")) {
            zzaVar = new zzfh();
        } else {
            zzaVar = new zza();
        }
        zza = zzaVar;
        if (num == null) {
        }
    }
}
