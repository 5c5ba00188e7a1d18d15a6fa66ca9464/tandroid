package com.google.android.gms.common.util;

import android.os.Process;
import android.os.StrictMode;
import com.google.android.gms.common.internal.Preconditions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class ProcessUtils {
    private static String zza;
    private static int zzb;

    public static String getMyProcessName() {
        BufferedReader bufferedReader;
        if (zza == null) {
            int i = zzb;
            if (i == 0) {
                i = Process.myPid();
                zzb = i;
            }
            String str = null;
            str = null;
            str = null;
            BufferedReader bufferedReader2 = null;
            if (i > 0) {
                try {
                    String str2 = "/proc/" + i + "/cmdline";
                    StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
                    try {
                        bufferedReader = new BufferedReader(new FileReader(str2));
                    } finally {
                        StrictMode.setThreadPolicy(allowThreadDiskReads);
                    }
                } catch (IOException unused) {
                    bufferedReader = null;
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    String readLine = bufferedReader.readLine();
                    Preconditions.checkNotNull(readLine);
                    str = readLine.trim();
                } catch (IOException unused2) {
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader2 = bufferedReader;
                    IOUtils.closeQuietly(bufferedReader2);
                    throw th;
                }
                IOUtils.closeQuietly(bufferedReader);
            }
            zza = str;
        }
        return zza;
    }
}
