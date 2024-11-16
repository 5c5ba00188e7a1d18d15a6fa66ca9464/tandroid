package com.microsoft.appcenter.utils.crypto;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import com.microsoft.appcenter.utils.crypto.CryptoUtils;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import javax.security.auth.x500.X500Principal;

/* loaded from: classes.dex */
class CryptoRsaHandler implements CryptoHandler {
    CryptoRsaHandler() {
    }

    private CryptoUtils.ICipher getCipher(CryptoUtils.ICryptoFactory iCryptoFactory, int i) {
        return iCryptoFactory.getCipher("RSA/ECB/PKCS1Padding", i >= 23 ? "AndroidKeyStoreBCWorkaround" : "AndroidOpenSSL");
    }

    @Override // com.microsoft.appcenter.utils.crypto.CryptoHandler
    public byte[] decrypt(CryptoUtils.ICryptoFactory iCryptoFactory, int i, KeyStore.Entry entry, byte[] bArr) {
        CryptoUtils.ICipher cipher = getCipher(iCryptoFactory, i);
        cipher.init(2, ((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
        return cipher.doFinal(bArr);
    }

    @Override // com.microsoft.appcenter.utils.crypto.CryptoHandler
    public byte[] encrypt(CryptoUtils.ICryptoFactory iCryptoFactory, int i, KeyStore.Entry entry, byte[] bArr) {
        CryptoUtils.ICipher cipher = getCipher(iCryptoFactory, i);
        X509Certificate x509Certificate = (X509Certificate) ((KeyStore.PrivateKeyEntry) entry).getCertificate();
        try {
            x509Certificate.checkValidity();
            cipher.init(1, x509Certificate.getPublicKey());
            return cipher.doFinal(bArr);
        } catch (CertificateExpiredException e) {
            throw new InvalidKeyException(e);
        }
    }

    @Override // com.microsoft.appcenter.utils.crypto.CryptoHandler
    public void generateKey(CryptoUtils.ICryptoFactory iCryptoFactory, String str, Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(1, 1);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
        keyPairGenerator.initialize(new KeyPairGeneratorSpec.Builder(context).setAlias(str).setSubject(new X500Principal("CN=" + str)).setStartDate(new Date()).setEndDate(calendar.getTime()).setSerialNumber(BigInteger.TEN).setKeySize(2048).build());
        keyPairGenerator.generateKeyPair();
    }

    @Override // com.microsoft.appcenter.utils.crypto.CryptoHandler
    public String getAlgorithm() {
        return "RSA/ECB/PKCS1Padding/2048";
    }
}
