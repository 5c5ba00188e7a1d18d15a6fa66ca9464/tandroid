package com.google.android.gms.internal.mlkit_common;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes3.dex */
public final class zzd {
    private static final String[] zza = {"com.android.", "com.google.", "com.chrome.", "com.nest.", "com.waymo.", "com.waze"};
    private static final String[] zzb;

    public static AssetFileDescriptor zza(Context context, Uri uri, String str) throws FileNotFoundException {
        return zza(context, uri, str, zzh.zza);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static AssetFileDescriptor zza(Context context, Uri uri, String str, zzh zzhVar) throws FileNotFoundException {
        ContentResolver contentResolver = context.getContentResolver();
        Uri parse = Uri.parse(uri.toString());
        String scheme = parse.getScheme();
        if ("android.resource".equals(scheme)) {
            return contentResolver.openAssetFileDescriptor(parse, str);
        }
        if ("content".equals(scheme)) {
            char c = 65535;
            int i = 2;
            switch (str.hashCode()) {
                case 114:
                    if (str.equals("r")) {
                        c = 0;
                        break;
                    }
                    break;
                case 119:
                    if (str.equals("w")) {
                        c = 1;
                        break;
                    }
                    break;
                case 3653:
                    if (str.equals("rw")) {
                        c = 3;
                        break;
                    }
                    break;
                case 3805:
                    if (str.equals("wt")) {
                        c = 2;
                        break;
                    }
                    break;
                case 113359:
                    if (str.equals("rwt")) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    i = 1;
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            if (!zza(context, parse, i, zzhVar)) {
                throw new FileNotFoundException("Can't open content uri.");
            }
            return (AssetFileDescriptor) zza(contentResolver.openAssetFileDescriptor(parse, str));
        } else if ("file".equals(scheme)) {
            AssetFileDescriptor assetFileDescriptor = (AssetFileDescriptor) zza(contentResolver.openAssetFileDescriptor(parse, str));
            try {
                zza(context, assetFileDescriptor.getParcelFileDescriptor(), parse, zzhVar);
                return assetFileDescriptor;
            } catch (FileNotFoundException e) {
                zza(assetFileDescriptor, e);
                throw e;
            } catch (IOException e2) {
                FileNotFoundException fileNotFoundException = new FileNotFoundException("Validation failed.");
                fileNotFoundException.initCause(e2);
                zza(assetFileDescriptor, fileNotFoundException);
                throw fileNotFoundException;
            }
        } else {
            throw new FileNotFoundException("Not implemented. Contact tiktok-users@");
        }
    }

    public static InputStream zza(Context context, Uri uri) throws FileNotFoundException {
        return zza(context, uri, zzh.zza);
    }

    private static InputStream zza(Context context, Uri uri, zzh zzhVar) throws FileNotFoundException {
        ContentResolver contentResolver = context.getContentResolver();
        Uri parse = Uri.parse(uri.toString());
        String scheme = parse.getScheme();
        if ("android.resource".equals(scheme)) {
            return contentResolver.openInputStream(parse);
        }
        if ("content".equals(scheme)) {
            if (!zza(context, parse, 1, zzhVar)) {
                throw new FileNotFoundException("Can't open content uri.");
            }
            return (InputStream) zza(contentResolver.openInputStream(parse));
        } else if ("file".equals(scheme)) {
            try {
                ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(Uri.fromFile(new File(parse.getPath()).getCanonicalFile()), "r");
                try {
                    zza(context, openFileDescriptor, parse, zzhVar);
                    return new ParcelFileDescriptor.AutoCloseInputStream(openFileDescriptor);
                } catch (FileNotFoundException e) {
                    zza(openFileDescriptor, e);
                    throw e;
                } catch (IOException e2) {
                    FileNotFoundException fileNotFoundException = new FileNotFoundException("Validation failed.");
                    fileNotFoundException.initCause(e2);
                    zza(openFileDescriptor, fileNotFoundException);
                    throw fileNotFoundException;
                }
            } catch (IOException e3) {
                FileNotFoundException fileNotFoundException2 = new FileNotFoundException("Canonicalization failed.");
                fileNotFoundException2.initCause(e3);
                throw fileNotFoundException2;
            }
        } else {
            throw new FileNotFoundException("Not implemented. Contact tiktok-users@");
        }
    }

    private static boolean zza(Context context, Uri uri, int i, zzh zzhVar) {
        boolean z;
        String[] strArr;
        boolean z2;
        boolean z3;
        String authority = uri.getAuthority();
        ProviderInfo resolveContentProvider = context.getPackageManager().resolveContentProvider(authority, 0);
        if (resolveContentProvider == null) {
            int lastIndexOf = authority.lastIndexOf(64);
            if (lastIndexOf >= 0) {
                authority = authority.substring(lastIndexOf + 1);
                resolveContentProvider = context.getPackageManager().resolveContentProvider(authority, 0);
            }
            if (resolveContentProvider == null) {
                z3 = zzhVar.zzc;
                return !z3;
            }
        }
        switch (zze.zza[zzh.zza(zzhVar, context, new zzr(uri, resolveContentProvider, authority)) - 1]) {
            case 1:
                return true;
            case 2:
                return false;
            default:
                if (context.getPackageName().equals(resolveContentProvider.packageName)) {
                    z2 = zzhVar.zzc;
                    return z2;
                }
                z = zzhVar.zzc;
                if (z) {
                    return false;
                }
                if (context.checkUriPermission(uri, Process.myPid(), Process.myUid(), i) == 0) {
                    return true;
                }
                for (String str : zzb) {
                    if (str.equals(authority)) {
                        return true;
                    }
                }
                if (resolveContentProvider.exported) {
                    for (String str2 : zza) {
                        if (str2.charAt(str2.length() - 1) == '.') {
                            if (resolveContentProvider.packageName.startsWith(str2)) {
                                return false;
                            }
                        } else if (resolveContentProvider.packageName.equals(str2)) {
                            return false;
                        }
                    }
                }
                return true;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x00ca  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x011e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void zza(Context context, ParcelFileDescriptor parcelFileDescriptor, Uri uri, zzh zzhVar) throws IOException {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        Context createDeviceProtectedStorageContext;
        File dataDir;
        String canonicalPath = new File(uri.getPath()).getCanonicalPath();
        zzj zza2 = zzj.zza(parcelFileDescriptor.getFileDescriptor());
        zzj zza3 = zzj.zza(canonicalPath);
        if (zza3.zzc) {
            String valueOf = String.valueOf(canonicalPath);
            throw new FileNotFoundException(valueOf.length() != 0 ? "Can't open file: ".concat(valueOf) : new String("Can't open file: "));
        }
        boolean z6 = false;
        if (!(zza2.zza == zza3.zza && zza2.zzb == zza3.zzb)) {
            String valueOf2 = String.valueOf(canonicalPath);
            throw new FileNotFoundException(valueOf2.length() != 0 ? "Can't open file: ".concat(valueOf2) : new String("Can't open file: "));
        }
        if (canonicalPath.startsWith("/proc/")) {
            z6 = true;
        } else if (!canonicalPath.startsWith("/data/misc/")) {
            z = zzhVar.zzd;
            if (z) {
                z6 = true;
            } else {
                File dataDir2 = ContextCompat.getDataDir(context);
                if (dataDir2 != null) {
                    if (canonicalPath.startsWith(dataDir2.getCanonicalPath())) {
                        z2 = true;
                        if (!z2) {
                            z3 = true;
                        } else {
                            if (Build.VERSION.SDK_INT >= 19) {
                                File[] zza4 = zza((Callable<File[]>) new Callable(context) { // from class: com.google.android.gms.internal.mlkit_common.zzc
                                    private final Context zza;

                                    /* JADX INFO: Access modifiers changed from: package-private */
                                    {
                                        this.zza = context;
                                    }

                                    @Override // java.util.concurrent.Callable
                                    public final Object call() {
                                        File[] externalFilesDirs;
                                        externalFilesDirs = ContextCompat.getExternalFilesDirs(this.zza, null);
                                        return externalFilesDirs;
                                    }
                                });
                                int length = zza4.length;
                                int i = 0;
                                while (true) {
                                    if (i < length) {
                                        File file = zza4[i];
                                        if (file != null && canonicalPath.startsWith(file.getCanonicalPath())) {
                                            z5 = true;
                                            break;
                                        }
                                        i++;
                                    } else {
                                        File[] zza5 = zza((Callable<File[]>) new Callable(context) { // from class: com.google.android.gms.internal.mlkit_common.zzf
                                            private final Context zza;

                                            /* JADX INFO: Access modifiers changed from: package-private */
                                            {
                                                this.zza = context;
                                            }

                                            @Override // java.util.concurrent.Callable
                                            public final Object call() {
                                                File[] externalCacheDirs;
                                                externalCacheDirs = ContextCompat.getExternalCacheDirs(this.zza);
                                                return externalCacheDirs;
                                            }
                                        });
                                        int length2 = zza5.length;
                                        int i2 = 0;
                                        while (true) {
                                            if (i2 < length2) {
                                                File file2 = zza5[i2];
                                                if (file2 != null && canonicalPath.startsWith(file2.getCanonicalPath())) {
                                                    z5 = true;
                                                    break;
                                                }
                                                i2++;
                                            } else {
                                                z5 = false;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (z5) {
                                    z3 = true;
                                }
                            }
                            z3 = false;
                        }
                        z4 = zzhVar.zzc;
                        if (z3 != z4) {
                            z6 = true;
                        }
                    }
                    createDeviceProtectedStorageContext = ContextCompat.createDeviceProtectedStorageContext(context);
                    if (createDeviceProtectedStorageContext == null && (dataDir = ContextCompat.getDataDir(createDeviceProtectedStorageContext)) != null && canonicalPath.startsWith(dataDir.getCanonicalPath())) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (!z2) {
                    }
                    z4 = zzhVar.zzc;
                    if (z3 != z4) {
                    }
                } else {
                    if (canonicalPath.startsWith(Environment.getDataDirectory().getCanonicalPath())) {
                        z2 = true;
                        if (!z2) {
                        }
                        z4 = zzhVar.zzc;
                        if (z3 != z4) {
                        }
                    }
                    createDeviceProtectedStorageContext = ContextCompat.createDeviceProtectedStorageContext(context);
                    if (createDeviceProtectedStorageContext == null) {
                    }
                    z2 = false;
                    if (!z2) {
                    }
                    z4 = zzhVar.zzc;
                    if (z3 != z4) {
                    }
                }
            }
        } else {
            z6 = true;
        }
        if (z6) {
            String valueOf3 = String.valueOf(canonicalPath);
            throw new FileNotFoundException(valueOf3.length() != 0 ? "Can't open file: ".concat(valueOf3) : new String("Can't open file: "));
        }
    }

    private static File[] zza(Callable<File[]> callable) {
        try {
            return callable.call();
        } catch (NullPointerException e) {
            if (Build.VERSION.SDK_INT >= 22) {
                throw e;
            }
            return new File[0];
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    private static void zza(AssetFileDescriptor assetFileDescriptor, FileNotFoundException fileNotFoundException) {
        try {
            assetFileDescriptor.close();
        } catch (IOException e) {
            zza(fileNotFoundException, e);
        }
    }

    private static void zza(ParcelFileDescriptor parcelFileDescriptor, FileNotFoundException fileNotFoundException) {
        try {
            parcelFileDescriptor.close();
        } catch (IOException e) {
            zza(fileNotFoundException, e);
        }
    }

    private static void zza(Exception exc, Exception exc2) {
        if (Build.VERSION.SDK_INT >= 19) {
            zzan.zza(exc, exc2);
        }
    }

    private static <T> T zza(T t) throws FileNotFoundException {
        if (t == null) {
            throw new FileNotFoundException("Content resolver returned null value.");
        }
        return t;
    }

    static {
        boolean z = false;
        String[] strArr = new String[4];
        strArr[0] = "media";
        String str = "";
        strArr[1] = Build.VERSION.SDK_INT <= 25 ? "com.google.android.inputmethod.latin.inputcontent" : str;
        strArr[2] = Build.VERSION.SDK_INT <= 25 ? "com.google.android.inputmethod.latin.dev.inputcontent" : str;
        if (Build.HARDWARE.equals("goldfish") || Build.HARDWARE.equals("ranchu")) {
            z = true;
        }
        if (z) {
            str = "com.google.android.apps.common.testing.services.storage.runfiles";
        }
        strArr[3] = str;
        zzb = strArr;
    }
}
