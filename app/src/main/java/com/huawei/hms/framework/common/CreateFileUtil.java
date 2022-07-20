package com.huawei.hms.framework.common;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.libcore.io.ExternalStorageFile;
import com.huawei.libcore.io.ExternalStorageFileInputStream;
import com.huawei.libcore.io.ExternalStorageFileOutputStream;
import com.huawei.libcore.io.ExternalStorageRandomAccessFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
/* loaded from: classes.dex */
public class CreateFileUtil {
    private static final String EXTERNAL_FILE_NAME = "com.huawei.libcore.io.ExternalStorageFile";
    private static final String EXTERNAL_INPUTSTREAM_NAME = "com.huawei.libcore.io.ExternalStorageFileInputStream";
    private static final String EXTERNAL_OUTPUTSTREAM_NAME = "com.huawei.libcore.io.ExternalStorageFileOutputStream";
    private static final String RANDOM_ACCESS_FILE_NAME = "com.huawei.libcore.io.ExternalStorageRandomAccessFile";
    private static final String TAG = "CreateFileUtil";

    public static String getCacheDirPath(Context context) {
        return context == null ? "" : ContextCompat.getProtectedStorageContext(context).getCacheDir().getPath();
    }

    public static File newFile(String str) {
        if (str == null) {
            return null;
        }
        if (EmuiUtil.isUpPVersion() && ReflectionUtils.checkCompatible("com.huawei.libcore.io.ExternalStorageFile")) {
            return new ExternalStorageFile(str);
        }
        return new File(str);
    }

    public static String getCanonicalPath(String str) {
        try {
            return newFile(str).getCanonicalPath();
        } catch (IOException e) {
            Logger.w("CreateFileUtil", "the canonicalPath has IOException", e);
            return str;
        } catch (SecurityException e2) {
            Logger.w("CreateFileUtil", "the canonicalPath has securityException", e2);
            return str;
        } catch (Exception e3) {
            Logger.w("CreateFileUtil", "the canonicalPath has other Exception", e3);
            return str;
        }
    }

    public static FileInputStream newFileInputStream(String str) throws FileNotFoundException {
        if (str == null) {
            Logger.w("CreateFileUtil", "newFileInputStream  file is null");
            throw new FileNotFoundException("file is null");
        } else if (EmuiUtil.isUpPVersion() && ReflectionUtils.checkCompatible("com.huawei.libcore.io.ExternalStorageFileInputStream")) {
            return new ExternalStorageFileInputStream(str);
        } else {
            return new FileInputStream(str);
        }
    }

    public static FileOutputStream newFileOutputStream(File file) throws FileNotFoundException {
        if (file == null) {
            Logger.e("CreateFileUtil", "newFileOutputStream  file is null");
            throw new FileNotFoundException("file is null");
        } else if (EmuiUtil.isUpPVersion() && ReflectionUtils.checkCompatible("com.huawei.libcore.io.ExternalStorageFileOutputStream")) {
            return new ExternalStorageFileOutputStream(file);
        } else {
            return new FileOutputStream(file);
        }
    }

    public static RandomAccessFile newRandomAccessFile(String str, String str2) throws FileNotFoundException {
        if (str == null) {
            Logger.w("CreateFileUtil", "newFileOutputStream  file is null");
            throw new FileNotFoundException("file is null");
        } else if (EmuiUtil.isUpPVersion() && ReflectionUtils.checkCompatible("com.huawei.libcore.io.ExternalStorageRandomAccessFile")) {
            return new ExternalStorageRandomAccessFile(str, str2);
        } else {
            return new RandomAccessFile(str, str2);
        }
    }

    public static void deleteSecure(File file) {
        if (file == null || !file.exists() || file.delete()) {
            return;
        }
        Logger.w("CreateFileUtil", "deleteSecure exception");
    }

    public static void deleteSecure(String str) {
        if (!TextUtils.isEmpty(str)) {
            deleteSecure(newFile(str));
        }
    }

    @Deprecated
    public static boolean isPVersion() {
        return EmuiUtil.isUpPVersion();
    }
}
