package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
/* loaded from: classes.dex */
public class NativeLoader {
    private static final String LIB_NAME = "tmessages.42";
    private static final String LIB_SO_NAME = "libtmessages.42.so";
    private static final int LIB_VERSION = 42;
    private static final String LOCALE_LIB_SO_NAME = "libtmessages.42loc.so";
    private static volatile boolean nativeLoaded = false;
    private String crashPath = "";

    private static native void init(String str, boolean z);

    /* JADX WARN: Removed duplicated region for block: B:10:0x0022  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0035 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0036 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static File getNativeLibraryDir(Context context) {
        File file;
        if (context != null) {
            try {
                file = new File((String) ApplicationInfo.class.getField("nativeLibraryDir").get(context.getApplicationInfo()));
            } catch (Throwable th) {
                th.printStackTrace();
            }
            if (file == null) {
                file = new File(context.getApplicationInfo().dataDir, "lib");
            }
            if (file.isDirectory()) {
                return null;
            }
            return file;
        }
        file = null;
        if (file == null) {
        }
        if (file.isDirectory()) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00ca A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x00d4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.util.zip.ZipFile] */
    /* JADX WARN: Type inference failed for: r2v7, types: [int] */
    @SuppressLint({"UnsafeDynamicallyLoadedCode", "SetWorldReadable"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static boolean loadFromZip(Context context, File file, File file2, String str) {
        ?? r2;
        ZipFile zipFile;
        try {
            for (File file3 : file.listFiles()) {
                file3.delete();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        InputStream inputStream = null;
        try {
            try {
                zipFile = new ZipFile(context.getApplicationInfo().sourceDir);
                try {
                    ZipEntry entry = zipFile.getEntry("lib/" + str + "/" + LIB_SO_NAME);
                    if (entry == null) {
                        throw new Exception("Unable to find file in apk:lib/" + str + "/" + LIB_NAME);
                    }
                    InputStream inputStream2 = zipFile.getInputStream(entry);
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int read = inputStream2.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        Thread.yield();
                        fileOutputStream.write(bArr, 0, read);
                    }
                    fileOutputStream.close();
                    file2.setReadable(true, false);
                    file2.setExecutable(true, false);
                    file2.setWritable(true);
                    try {
                        System.load(file2.getAbsolutePath());
                        nativeLoaded = true;
                    } catch (Error e2) {
                        FileLog.e(e2);
                    }
                    try {
                        inputStream2.close();
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                    try {
                        zipFile.close();
                    } catch (Exception e4) {
                        FileLog.e(e4);
                    }
                    return true;
                } catch (Exception e5) {
                    e = e5;
                    FileLog.e(e);
                    if (0 != 0) {
                        try {
                            inputStream.close();
                        } catch (Exception e6) {
                            FileLog.e(e6);
                        }
                    }
                    if (zipFile != null) {
                        try {
                            zipFile.close();
                        } catch (Exception e7) {
                            FileLog.e(e7);
                        }
                    }
                    return false;
                }
            } catch (Throwable th) {
                th = th;
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (Exception e8) {
                        FileLog.e(e8);
                    }
                }
                if (r2 != 0) {
                    try {
                        r2.close();
                    } catch (Exception e9) {
                        FileLog.e(e9);
                    }
                }
                throw th;
            }
        } catch (Exception e10) {
            e = e10;
            zipFile = null;
        } catch (Throwable th2) {
            th = th2;
            r2 = 0;
            if (0 != 0) {
            }
            if (r2 != 0) {
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x00db A[Catch: all -> 0x001d, TryCatch #3 {, blocks: (B:4:0x0003, B:65:0x00fa, B:68:0x0104, B:9:0x000a, B:11:0x0016, B:17:0x0021, B:18:0x0024, B:42:0x008e, B:44:0x0096, B:47:0x00a1, B:49:0x00bc, B:51:0x00c0, B:52:0x00c5, B:57:0x00d7, B:59:0x00db, B:60:0x00ef, B:56:0x00d1, B:21:0x0033, B:24:0x003e, B:27:0x0049, B:30:0x0054, B:33:0x0061, B:36:0x006c, B:38:0x0072, B:41:0x0089), top: B:78:0x0003, inners: #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00f6 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x00fa A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @SuppressLint({"UnsafeDynamicallyLoadedCode"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static synchronized void initNativeLibs(Context context) {
        String str;
        synchronized (NativeLoader.class) {
            if (nativeLoaded) {
                return;
            }
            try {
                System.loadLibrary(LIB_NAME);
                nativeLoaded = true;
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("loaded normal lib");
                }
            } catch (Error e) {
                FileLog.e(e);
                try {
                    String str2 = Build.CPU_ABI;
                    if (str2.equalsIgnoreCase("x86_64")) {
                        str = "x86_64";
                    } else if (str2.equalsIgnoreCase("arm64-v8a")) {
                        str = "arm64-v8a";
                    } else if (str2.equalsIgnoreCase("armeabi-v7a")) {
                        str = "armeabi-v7a";
                    } else if (str2.equalsIgnoreCase("armeabi")) {
                        str = "armeabi";
                    } else if (str2.equalsIgnoreCase("x86")) {
                        str = "x86";
                    } else if (str2.equalsIgnoreCase("mips")) {
                        str = "mips";
                    } else {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.e("Unsupported arch: " + str2);
                        }
                        str = "armeabi";
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                    str = "armeabi";
                }
                String property = System.getProperty("os.arch");
                if (property != null && property.contains("686")) {
                    str = "x86";
                }
                File file = new File(context.getFilesDir(), "lib");
                file.mkdirs();
                File file2 = new File(file, LOCALE_LIB_SO_NAME);
                if (file2.exists()) {
                    try {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("Load local lib");
                        }
                        System.load(file2.getAbsolutePath());
                        nativeLoaded = true;
                        return;
                    } catch (Error e3) {
                        FileLog.e(e3);
                        file2.delete();
                        if (BuildVars.LOGS_ENABLED) {
                        }
                        if (loadFromZip(context, file, file2, str)) {
                        }
                    }
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("Library not found, arch = " + str);
                }
                if (loadFromZip(context, file, file2, str)) {
                    try {
                        System.loadLibrary(LIB_NAME);
                        nativeLoaded = true;
                    } catch (Error e4) {
                        FileLog.e(e4);
                    }
                }
            }
        }
    }
}
