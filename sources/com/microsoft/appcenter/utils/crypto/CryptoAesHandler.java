package com.microsoft.appcenter.utils.crypto;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import com.microsoft.appcenter.utils.crypto.CryptoUtils;
import java.security.KeyStore;
import java.util.Calendar;
import javax.crypto.spec.IvParameterSpec;

/* loaded from: classes.dex */
class CryptoAesHandler implements CryptoHandler {
    CryptoAesHandler() {
    }

    @Override // com.microsoft.appcenter.utils.crypto.CryptoHandler
    public byte[] decrypt(CryptoUtils.ICryptoFactory iCryptoFactory, int i, KeyStore.Entry entry, byte[] bArr) {
        CryptoUtils.ICipher cipher = iCryptoFactory.getCipher("AES/CBC/PKCS7Padding", "AndroidKeyStoreBCWorkaround");
        int blockSize = cipher.getBlockSize();
        cipher.init(2, ((KeyStore.SecretKeyEntry) entry).getSecretKey(), new IvParameterSpec(bArr, 0, blockSize));
        return cipher.doFinal(bArr, blockSize, bArr.length - blockSize);
    }

    @Override // com.microsoft.appcenter.utils.crypto.CryptoHandler
    public byte[] encrypt(CryptoUtils.ICryptoFactory iCryptoFactory, int i, KeyStore.Entry entry, byte[] bArr) {
        CryptoUtils.ICipher cipher = iCryptoFactory.getCipher("AES/CBC/PKCS7Padding", "AndroidKeyStoreBCWorkaround");
        cipher.init(1, ((KeyStore.SecretKeyEntry) entry).getSecretKey());
        byte[] iv = cipher.getIV();
        byte[] doFinal = cipher.doFinal(bArr);
        byte[] bArr2 = new byte[iv.length + doFinal.length];
        System.arraycopy(iv, 0, bArr2, 0, iv.length);
        System.arraycopy(doFinal, 0, bArr2, iv.length, doFinal.length);
        return bArr2;
    }

    @Override // com.microsoft.appcenter.utils.crypto.CryptoHandler
    public void generateKey(CryptoUtils.ICryptoFactory iCryptoFactory, String str, Context context) {
        KeyGenParameterSpec.Builder blockModes;
        KeyGenParameterSpec.Builder encryptionPaddings;
        KeyGenParameterSpec.Builder keySize;
        KeyGenParameterSpec.Builder keyValidityForOriginationEnd;
        KeyGenParameterSpec build;
        Calendar calendar = Calendar.getInstance();
        calendar.add(1, 1);
        CryptoUtils.IKeyGenerator keyGenerator = iCryptoFactory.getKeyGenerator("AES", "AndroidKeyStore");
        blockModes = new KeyGenParameterSpec.Builder(str, 3).setBlockModes("CBC");
        encryptionPaddings = blockModes.setEncryptionPaddings("PKCS7Padding");
        keySize = encryptionPaddings.setKeySize(256);
        keyValidityForOriginationEnd = keySize.setKeyValidityForOriginationEnd(calendar.getTime());
        build = keyValidityForOriginationEnd.build();
        keyGenerator.init(build);
        keyGenerator.generateKey();
    }

    @Override // com.microsoft.appcenter.utils.crypto.CryptoHandler
    public String getAlgorithm() {
        return "AES/CBC/PKCS7Padding/256";
    }
}
