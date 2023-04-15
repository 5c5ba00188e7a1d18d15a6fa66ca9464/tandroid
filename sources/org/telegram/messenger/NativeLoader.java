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
    private static final String LIB_NAME = "tmessages.45";
    private static final String LIB_SO_NAME = "libtmessages.45.so";
    private static final int LIB_VERSION = 45;
    private static final String LOCALE_LIB_SO_NAME = "libtmessages.45loc.so";
    public static StringBuilder log = new StringBuilder();
    private static volatile boolean nativeLoaded = false;

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
                    byte[] bArr = new byte[LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM];
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

    /* JADX WARN: Removed duplicated region for block: B:29:0x007b A[Catch: all -> 0x001d, TryCatch #1 {, blocks: (B:4:0x0003, B:35:0x00c4, B:38:0x00ce, B:9:0x000a, B:11:0x0016, B:17:0x0021, B:19:0x0052, B:21:0x0056, B:22:0x005b, B:27:0x0077, B:29:0x007b, B:30:0x00aa, B:26:0x0067), top: B:44:0x0003, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00b1 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00c4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @SuppressLint({"UnsafeDynamicallyLoadedCode"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static synchronized void initNativeLibs(Context context) {
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
                StringBuilder sb = log;
                sb.append("129: ");
                sb.append(e);
                sb.append("\n");
                String abiFolder = getAbiFolder();
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
                    } catch (Error e2) {
                        StringBuilder sb2 = log;
                        sb2.append(e2);
                        sb2.append("\n");
                        FileLog.e(e2);
                        file2.delete();
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.e("Library not found, arch = " + abiFolder);
                            StringBuilder sb3 = log;
                            sb3.append("Library not found, arch = " + abiFolder);
                            sb3.append("\n");
                        }
                        if (loadFromZip(context, file, file2, abiFolder)) {
                            try {
                                System.loadLibrary(LIB_NAME);
                                nativeLoaded = true;
                            } catch (Error e3) {
                                FileLog.e(e3);
                                StringBuilder sb4 = log;
                                sb4.append("185: ");
                                sb4.append(e3);
                                sb4.append("\n");
                            }
                            return;
                        }
                        return;
                    }
                }
                if (BuildVars.LOGS_ENABLED) {
                }
                if (loadFromZip(context, file, file2, abiFolder)) {
                }
            }
        }
    }

    public static String getAbiFolder() {
        String str = "x86_64";
        String str2 = "armeabi";
        try {
            String str3 = Build.CPU_ABI;
            if (!str3.equalsIgnoreCase("x86_64")) {
                if (str3.equalsIgnoreCase("arm64-v8a")) {
                    str = "arm64-v8a";
                } else if (str3.equalsIgnoreCase("armeabi-v7a")) {
                    str = "armeabi-v7a";
                } else {
                    if (!str3.equalsIgnoreCase("armeabi")) {
                        if (str3.equalsIgnoreCase("x86")) {
                            str = "x86";
                        } else if (str3.equalsIgnoreCase("mips")) {
                            str = "mips";
                        } else if (BuildVars.LOGS_ENABLED) {
                            FileLog.e("Unsupported arch: " + str3);
                        }
                    }
                    str = "armeabi";
                }
            }
            str2 = str;
        } catch (Exception e) {
            FileLog.e(e);
        }
        String property = System.getProperty("os.arch");
        return (property == null || !property.contains("686")) ? str2 : "x86";
    }

    public static boolean loaded() {
        return nativeLoaded;
    }
}
