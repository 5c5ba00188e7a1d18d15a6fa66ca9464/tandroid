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
    private static final String LIB_NAME = "tmessages.49";
    private static final String LIB_SO_NAME = "libtmessages.49.so";
    private static final int LIB_VERSION = 49;
    private static final String LOCALE_LIB_SO_NAME = "libtmessages.49loc.so";
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
    /* JADX WARN: Removed duplicated region for block: B:71:0x00db A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x00d1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.util.zip.ZipFile] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.util.zip.ZipFile] */
    @SuppressLint({"UnsafeDynamicallyLoadedCode", "SetWorldReadable"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static boolean loadFromZip(Context context, File file, File file2, String str) {
        InputStream inputStream;
        ZipFile zipFile;
        ZipFile zipFile2;
        ZipFile zipFile3;
        try {
            for (File file3 : file.listFiles()) {
                file3.delete();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        InputStream inputStream2 = null;
        try {
            zipFile3 = new ZipFile(context.getApplicationInfo().sourceDir);
        } catch (Exception e2) {
            e = e2;
            zipFile = null;
        } catch (Throwable th) {
            th = th;
            inputStream = null;
            zipFile2 = inputStream2;
            inputStream2 = inputStream;
            if (inputStream2 != null) {
            }
            if (zipFile2 != 0) {
            }
            throw th;
        }
        try {
            ZipEntry entry = zipFile3.getEntry("lib/" + str + "/" + LIB_SO_NAME);
            if (entry == null) {
                throw new Exception("Unable to find file in apk:lib/" + str + "/" + LIB_NAME);
            }
            InputStream inputStream3 = zipFile3.getInputStream(entry);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM];
            while (true) {
                int read = inputStream3.read(bArr);
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
            } catch (Error e3) {
                FileLog.e(e3);
            }
            try {
                inputStream3.close();
            } catch (Exception e4) {
                FileLog.e(e4);
            }
            try {
                zipFile3.close();
            } catch (Exception e5) {
                FileLog.e(e5);
            }
            return true;
        } catch (Exception e6) {
            e = e6;
            zipFile = zipFile3;
            try {
                FileLog.e(e);
                if (0 != 0) {
                    try {
                        inputStream2.close();
                    } catch (Exception e7) {
                        FileLog.e(e7);
                    }
                }
                if (zipFile != 0) {
                    try {
                        zipFile.close();
                    } catch (Exception e8) {
                        FileLog.e(e8);
                    }
                }
                return false;
            } catch (Throwable th2) {
                th = th2;
                inputStream = null;
                inputStream2 = zipFile;
                zipFile2 = inputStream2;
                inputStream2 = inputStream;
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (Exception e9) {
                        FileLog.e(e9);
                    }
                }
                if (zipFile2 != 0) {
                    try {
                        zipFile2.close();
                    } catch (Exception e10) {
                        FileLog.e(e10);
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            zipFile2 = zipFile3;
            if (inputStream2 != null) {
            }
            if (zipFile2 != 0) {
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x007f A[Catch: all -> 0x001c, TryCatch #5 {, blocks: (B:4:0x0003, B:38:0x00c8, B:43:0x00d4, B:9:0x000a, B:11:0x0016, B:19:0x0023, B:21:0x0054, B:23:0x0058, B:26:0x0060, B:30:0x007b, B:32:0x007f, B:33:0x00ae, B:29:0x006b), top: B:54:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00b5 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00c8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
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
                sb.append("128: ");
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
                                sb4.append("184: ");
                                sb4.append(e3);
                                sb4.append("\n");
                                return;
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
        String str;
        String str2 = "mips";
        String str3 = "armeabi";
        try {
            str = Build.CPU_ABI;
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (str.equalsIgnoreCase("x86_64")) {
            str2 = "x86_64";
        } else if (str.equalsIgnoreCase("arm64-v8a")) {
            str2 = "arm64-v8a";
        } else if (str.equalsIgnoreCase("armeabi-v7a")) {
            str2 = "armeabi-v7a";
        } else if (str.equalsIgnoreCase("armeabi")) {
            str2 = "armeabi";
        } else if (str.equalsIgnoreCase("x86")) {
            str2 = "x86";
        } else if (!str.equalsIgnoreCase("mips")) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Unsupported arch: " + str);
            }
            String property = System.getProperty("os.arch");
            return (property == null && property.contains("686")) ? "x86" : str3;
        }
        str3 = str2;
        String property2 = System.getProperty("os.arch");
        if (property2 == null) {
        }
    }

    public static boolean loaded() {
        return nativeLoaded;
    }
}
