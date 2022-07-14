package com.google.android.gms.internal.firebase_messaging;

import java.io.PrintStream;
/* compiled from: com.google.firebase:firebase-messaging@@22.0.0 */
/* loaded from: classes3.dex */
public final class zzt {
    static final zzn zza;

    /* JADX WARN: Removed duplicated region for block: B:20:0x0083 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0084  */
    static {
        zzn zznVar;
        Throwable th;
        Integer num = null;
        try {
            try {
                num = (Integer) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
            } catch (Throwable th2) {
                th = th2;
                PrintStream printStream = System.err;
                String name = zzr.class.getName();
                StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 133);
                sb.append("An error has occurred when initializing the try-with-resources desuguring strategy. The default strategy ");
                sb.append(name);
                sb.append("will be used. The error is: ");
                printStream.println(sb.toString());
                th.printStackTrace(System.err);
                zznVar = new zzr();
                zza = zznVar;
                if (num != null) {
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to retrieve value from android.os.Build$VERSION.SDK_INT due to the following exception.");
            e.printStackTrace(System.err);
        }
        if (num != null) {
            try {
            } catch (Throwable th3) {
                th = th3;
                PrintStream printStream2 = System.err;
                String name2 = zzr.class.getName();
                StringBuilder sb2 = new StringBuilder(String.valueOf(name2).length() + 133);
                sb2.append("An error has occurred when initializing the try-with-resources desuguring strategy. The default strategy ");
                sb2.append(name2);
                sb2.append("will be used. The error is: ");
                printStream2.println(sb2.toString());
                th.printStackTrace(System.err);
                zznVar = new zzr();
                zza = zznVar;
                if (num != null) {
                }
            }
            if (num.intValue() >= 19) {
                zznVar = new zzs();
                zza = zznVar;
                if (num != null) {
                    return;
                }
                num.intValue();
                return;
            }
        }
        zznVar = !Boolean.getBoolean("com.google.devtools.build.android.desugar.runtime.twr_disable_mimic") ? new zzq() : new zzr();
        zza = zznVar;
        if (num != null) {
        }
    }

    public static void zza(Throwable th, Throwable th2) {
        zza.zza(th, th2);
    }
}
