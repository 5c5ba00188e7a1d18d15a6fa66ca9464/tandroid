package com.google.android.gms.internal.mlkit_common;

import java.io.PrintStream;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
public final class zzan {
    private static final zzaq zza;

    /* compiled from: com.google.mlkit:common@@17.0.0 */
    /* loaded from: classes.dex */
    static final class zza extends zzaq {
        zza() {
        }

        @Override // com.google.android.gms.internal.mlkit_common.zzaq
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
            System.err.println("Failed to retrieve value from android.os.Build$VERSION.SDK_INT due to the following exception.");
            e.printStackTrace(System.err);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    static {
        zzaq zzaqVar;
        Integer num;
        Throwable th;
        try {
            num = zza();
        } catch (Throwable th2) {
            th = th2;
            num = null;
        }
        if (num != null) {
            try {
            } catch (Throwable th3) {
                th = th3;
                PrintStream printStream = System.err;
                String name = zza.class.getName();
                StringBuilder sb = new StringBuilder(name.length() + 133);
                sb.append("An error has occurred when initializing the try-with-resources desuguring strategy. The default strategy ");
                sb.append(name);
                sb.append("will be used. The error is: ");
                printStream.println(sb.toString());
                th.printStackTrace(System.err);
                zzaqVar = new zza();
                zza = zzaqVar;
                if (num == null) {
                }
            }
            if (num.intValue() >= 19) {
                zzaqVar = new zzat();
                zza = zzaqVar;
                if (num == null) {
                    return;
                }
                num.intValue();
                return;
            }
        }
        if (!Boolean.getBoolean("com.google.devtools.build.android.desugar.runtime.twr_disable_mimic")) {
            zzaqVar = new zzar();
        } else {
            zzaqVar = new zza();
        }
        zza = zzaqVar;
        if (num == null) {
        }
    }
}
