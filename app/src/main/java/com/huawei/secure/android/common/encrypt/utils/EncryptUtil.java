package com.huawei.secure.android.common.encrypt.utils;

import android.os.Build;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.prng.SP800SecureRandomBuilder;
/* loaded from: classes.dex */
public class EncryptUtil {
    private static boolean c = false;
    private static boolean d = true;

    /* JADX WARN: Removed duplicated region for block: B:19:0x001b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static SecureRandom a() {
        SecureRandom secureRandom;
        b.a("EncryptUtil", "generateSecureRandomNew ");
        try {
        } catch (NoSuchAlgorithmException unused) {
            b.b("EncryptUtil", "getSecureRandomBytes: NoSuchAlgorithmException");
        }
        if (Build.VERSION.SDK_INT >= 26) {
            secureRandom = SecureRandom.getInstanceStrong();
            if (secureRandom == null) {
                try {
                    secureRandom = SecureRandom.getInstance("SHA1PRNG");
                } catch (NoSuchAlgorithmException unused2) {
                    b.b("EncryptUtil", "NoSuchAlgorithmException");
                    return secureRandom;
                } catch (Throwable th) {
                    if (d) {
                        b.b("EncryptUtil", "exception : " + th.getMessage() + " , you should implementation bcprov-jdk15on library");
                        d = false;
                    }
                    return secureRandom;
                }
            }
            AESEngine aESEngine = new AESEngine();
            byte[] bArr = new byte[32];
            secureRandom.nextBytes(bArr);
            return new SP800SecureRandomBuilder(secureRandom, true).setEntropyBitsRequired(384).buildCTR(aESEngine, 256, bArr, false);
        }
        secureRandom = null;
        if (secureRandom == null) {
        }
        AESEngine aESEngine2 = new AESEngine();
        byte[] bArr2 = new byte[32];
        secureRandom.nextBytes(bArr2);
        return new SP800SecureRandomBuilder(secureRandom, true).setEntropyBitsRequired(384).buildCTR(aESEngine2, 256, bArr2, false);
    }

    public static byte[] generateSecureRandom(int i) {
        if (!c) {
            byte[] bArr = new byte[i];
            SecureRandom secureRandom = null;
            try {
                if (Build.VERSION.SDK_INT >= 26) {
                    secureRandom = SecureRandom.getInstanceStrong();
                }
            } catch (NoSuchAlgorithmException unused) {
                b.b("EncryptUtil", "getSecureRandomBytes: NoSuchAlgorithmException");
            }
            if (secureRandom == null) {
                try {
                    secureRandom = SecureRandom.getInstance("SHA1PRNG");
                } catch (NoSuchAlgorithmException unused2) {
                    b.b("EncryptUtil", "getSecureRandomBytes getInstance: NoSuchAlgorithmException");
                    return new byte[0];
                } catch (Exception e) {
                    b.b("EncryptUtil", "getSecureRandomBytes getInstance: exception : " + e.getMessage());
                    return new byte[0];
                }
            }
            secureRandom.nextBytes(bArr);
            return bArr;
        }
        return a(i);
    }

    public static String generateSecureRandomStr(int i) {
        return HexUtil.byteArray2HexStr(generateSecureRandom(i));
    }

    private static byte[] a(int i) {
        SecureRandom a = a();
        if (a == null) {
            return new byte[0];
        }
        byte[] bArr = new byte[i];
        a.nextBytes(bArr);
        return bArr;
    }
}
