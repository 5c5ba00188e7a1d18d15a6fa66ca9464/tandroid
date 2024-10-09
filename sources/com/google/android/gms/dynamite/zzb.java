package com.google.android.gms.dynamite;

import android.os.Looper;
import android.util.Log;

/* loaded from: classes.dex */
public abstract class zzb {
    private static volatile ClassLoader zza;
    private static volatile Thread zzb;

    public static synchronized ClassLoader zza() {
        ClassLoader classLoader;
        synchronized (zzb.class) {
            try {
                if (zza == null) {
                    zza = zzb();
                }
                classLoader = zza;
            } catch (Throwable th) {
                throw th;
            }
        }
        return classLoader;
    }

    private static synchronized ClassLoader zzb() {
        synchronized (zzb.class) {
            ClassLoader classLoader = null;
            if (zzb == null) {
                zzb = zzc();
                if (zzb == null) {
                    return null;
                }
            }
            synchronized (zzb) {
                try {
                    classLoader = zzb.getContextClassLoader();
                } catch (SecurityException e) {
                    Log.w("DynamiteLoaderV2CL", "Failed to get thread context classloader " + e.getMessage());
                }
            }
            return classLoader;
        }
    }

    private static synchronized Thread zzc() {
        SecurityException e;
        Thread thread;
        Thread thread2;
        ThreadGroup threadGroup;
        synchronized (zzb.class) {
            ThreadGroup threadGroup2 = Looper.getMainLooper().getThread().getThreadGroup();
            if (threadGroup2 == null) {
                return null;
            }
            synchronized (Void.class) {
                try {
                    try {
                        int activeGroupCount = threadGroup2.activeGroupCount();
                        ThreadGroup[] threadGroupArr = new ThreadGroup[activeGroupCount];
                        threadGroup2.enumerate(threadGroupArr);
                        int i = 0;
                        int i2 = 0;
                        while (true) {
                            if (i2 >= activeGroupCount) {
                                threadGroup = null;
                                break;
                            }
                            threadGroup = threadGroupArr[i2];
                            if ("dynamiteLoader".equals(threadGroup.getName())) {
                                break;
                            }
                            i2++;
                        }
                        if (threadGroup == null) {
                            threadGroup = new ThreadGroup(threadGroup2, "dynamiteLoader");
                        }
                        int activeCount = threadGroup.activeCount();
                        Thread[] threadArr = new Thread[activeCount];
                        threadGroup.enumerate(threadArr);
                        while (true) {
                            if (i >= activeCount) {
                                thread2 = null;
                                break;
                            }
                            thread2 = threadArr[i];
                            if ("GmsDynamite".equals(thread2.getName())) {
                                break;
                            }
                            i++;
                        }
                    } finally {
                    }
                } catch (SecurityException e2) {
                    e = e2;
                    thread = null;
                }
                if (thread2 == null) {
                    try {
                        thread = new zza(threadGroup, "GmsDynamite");
                        try {
                            thread.setContextClassLoader(null);
                            thread.start();
                        } catch (SecurityException e3) {
                            e = e3;
                            Log.w("DynamiteLoaderV2CL", "Failed to enumerate thread/threadgroup " + e.getMessage());
                            thread2 = thread;
                            return thread2;
                        }
                    } catch (SecurityException e4) {
                        e = e4;
                        thread = thread2;
                    }
                    thread2 = thread;
                }
            }
            return thread2;
        }
    }
}
