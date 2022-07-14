package com.google.mlkit.common.internal.model;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import com.google.android.gms.common.internal.GmsLogger;
import com.google.android.gms.internal.mlkit_common.zzd;
import com.google.android.gms.internal.mlkit_common.zzx;
import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes3.dex */
public class ModelUtils {
    private static final GmsLogger zza = new GmsLogger("ModelUtils", "");

    /* compiled from: com.google.mlkit:common@@17.0.0 */
    /* loaded from: classes3.dex */
    public static abstract class ModelLoggingInfo {
        public abstract String getHash();

        public abstract long getSize();

        static ModelLoggingInfo zza(long j, String str) {
            return new AutoValue_ModelUtils_ModelLoggingInfo(j, zzx.zza(str));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x00c9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ModelLoggingInfo getModelLoggingInfo(Context context, LocalModel localModel) {
        long length;
        InputStream inputStream;
        IOException e;
        InputStream inputStream2;
        String assetFilePath = localModel.getAssetFilePath();
        String absoluteFilePath = localModel.getAbsoluteFilePath();
        Uri uri = localModel.getUri();
        InputStream inputStream3 = null;
        if (assetFilePath != null) {
            try {
                AssetFileDescriptor openFd = context.getAssets().openFd(assetFilePath);
                length = openFd.getLength();
                if (openFd != null) {
                    openFd.close();
                }
            } catch (IOException e2) {
                zza.e("ModelUtils", "Failed to open model file", e2);
                return null;
            }
        } else if (absoluteFilePath != null) {
            length = new File(absoluteFilePath).length();
        } else {
            try {
                AssetFileDescriptor zza2 = zzd.zza(context, uri, "r");
                length = zza2.getLength();
                if (zza2 != null) {
                    zza2.close();
                }
            } catch (IOException e3) {
                zza.e("ModelUtils", "Failed to open model file", e3);
                return null;
            }
        }
        SharedPrefManager sharedPrefManager = (SharedPrefManager) MlKitContext.getInstance().get(SharedPrefManager.class);
        String cachedLocalModelHash = sharedPrefManager.getCachedLocalModelHash(localModel, length);
        if (cachedLocalModelHash != null) {
            return ModelLoggingInfo.zza(length, cachedLocalModelHash);
        }
        try {
            if (assetFilePath != null) {
                inputStream2 = context.getAssets().open(assetFilePath);
            } else if (absoluteFilePath != null) {
                inputStream2 = new FileInputStream(new File(absoluteFilePath));
            } else {
                inputStream2 = zzd.zza(context, uri);
            }
            try {
                String zza3 = zza(inputStream2);
                if (zza3 != null) {
                    sharedPrefManager.setCachedLocalModelHash(localModel, length, zza3);
                }
                ModelLoggingInfo zza4 = ModelLoggingInfo.zza(length, zza3);
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (IOException e4) {
                        zza.e("ModelUtils", "Failed to close model file", e4);
                    }
                }
                return zza4;
            } catch (IOException e5) {
                inputStream = inputStream2;
                e = e5;
                try {
                    zza.e("ModelUtils", "Failed to open model file", e);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e6) {
                            zza.e("ModelUtils", "Failed to close model file", e6);
                        }
                    }
                    return null;
                } catch (Throwable th) {
                    th = th;
                    inputStream3 = inputStream;
                    if (inputStream3 != null) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                inputStream3 = inputStream2;
                th = th2;
                if (inputStream3 != null) {
                    try {
                        inputStream3.close();
                    } catch (IOException e7) {
                        zza.e("ModelUtils", "Failed to close model file", e7);
                    }
                }
                throw th;
            }
        } catch (IOException e8) {
            e = e8;
            inputStream = null;
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public static boolean zza(File file, String str) {
        String sha256 = getSHA256(file);
        GmsLogger gmsLogger = zza;
        String valueOf = String.valueOf(sha256);
        gmsLogger.d("ModelUtils", valueOf.length() != 0 ? "Calculated hash value is: ".concat(valueOf) : new String("Calculated hash value is: "));
        return str.equals(sha256);
    }

    public static String getSHA256(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            String zza2 = zza(fileInputStream);
            fileInputStream.close();
            return zza2;
        } catch (IOException e) {
            GmsLogger gmsLogger = zza;
            String valueOf = String.valueOf(e);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 44);
            sb.append("Failed to create FileInputStream for model: ");
            sb.append(valueOf);
            gmsLogger.e("ModelUtils", sb.toString());
            return null;
        }
    }

    private static String zza(InputStream inputStream) {
        int i;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] bArr = new byte[1048576];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                messageDigest.update(bArr, 0, read);
            }
            byte[] digest = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & 255);
                if (hexString.length() == 1) {
                    sb.append('0');
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (IOException e) {
            zza.e("ModelUtils", "Failed to read model file");
            return null;
        } catch (NoSuchAlgorithmException e2) {
            zza.e("ModelUtils", "Do not have SHA-256 algorithm");
            return null;
        }
    }

    private ModelUtils() {
    }
}
