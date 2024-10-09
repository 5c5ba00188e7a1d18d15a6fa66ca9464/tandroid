package com.microsoft.appcenter.utils.crypto;

import android.content.Context;
import android.os.Build;
import android.util.Base64;
import com.microsoft.appcenter.utils.AppCenterLog;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.CertificateExpiredException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

/* loaded from: classes.dex */
public class CryptoUtils {
    static final ICryptoFactory DEFAULT_CRYPTO_FACTORY = new ICryptoFactory() { // from class: com.microsoft.appcenter.utils.crypto.CryptoUtils.1
        @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICryptoFactory
        public ICipher getCipher(String str, String str2) {
            final Cipher cipher = Cipher.getInstance(str, str2);
            return new ICipher() { // from class: com.microsoft.appcenter.utils.crypto.CryptoUtils.1.2
                @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICipher
                public byte[] doFinal(byte[] bArr) {
                    return cipher.doFinal(bArr);
                }

                @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICipher
                public byte[] doFinal(byte[] bArr, int i, int i2) {
                    return cipher.doFinal(bArr, i, i2);
                }

                @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICipher
                public int getBlockSize() {
                    return cipher.getBlockSize();
                }

                @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICipher
                public byte[] getIV() {
                    return cipher.getIV();
                }

                @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICipher
                public void init(int i, Key key) {
                    cipher.init(i, key);
                }

                @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICipher
                public void init(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec) {
                    cipher.init(i, key, algorithmParameterSpec);
                }
            };
        }

        @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICryptoFactory
        public IKeyGenerator getKeyGenerator(String str, String str2) {
            final KeyGenerator keyGenerator = KeyGenerator.getInstance(str, str2);
            return new IKeyGenerator() { // from class: com.microsoft.appcenter.utils.crypto.CryptoUtils.1.1
                @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.IKeyGenerator
                public void generateKey() {
                    keyGenerator.generateKey();
                }

                @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.IKeyGenerator
                public void init(AlgorithmParameterSpec algorithmParameterSpec) {
                    keyGenerator.init(algorithmParameterSpec);
                }
            };
        }
    };
    private static CryptoUtils sInstance;
    private final int mApiLevel;
    private final Context mContext;
    private final ICryptoFactory mCryptoFactory;
    private final Map mCryptoHandlers;
    private final KeyStore mKeyStore;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class CryptoHandlerEntry {
        int mAliasIndex;
        final CryptoHandler mCryptoHandler;

        CryptoHandlerEntry(int i, CryptoHandler cryptoHandler) {
            this.mAliasIndex = i;
            this.mCryptoHandler = cryptoHandler;
        }
    }

    /* loaded from: classes.dex */
    public static class DecryptedData {
        final String mDecryptedData;
        final String mNewEncryptedData;

        public DecryptedData(String str, String str2) {
            this.mDecryptedData = str;
            this.mNewEncryptedData = str2;
        }

        public String getDecryptedData() {
            return this.mDecryptedData;
        }

        public String getNewEncryptedData() {
            return this.mNewEncryptedData;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface ICipher {
        byte[] doFinal(byte[] bArr);

        byte[] doFinal(byte[] bArr, int i, int i2);

        int getBlockSize();

        byte[] getIV();

        void init(int i, Key key);

        void init(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface ICryptoFactory {
        ICipher getCipher(String str, String str2);

        IKeyGenerator getKeyGenerator(String str, String str2);
    }

    /* loaded from: classes.dex */
    interface IKeyGenerator {
        void generateKey();

        void init(AlgorithmParameterSpec algorithmParameterSpec);
    }

    private CryptoUtils(Context context) {
        this(context, DEFAULT_CRYPTO_FACTORY, Build.VERSION.SDK_INT);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0044 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    CryptoUtils(Context context, ICryptoFactory iCryptoFactory, int i) {
        KeyStore keyStore;
        this.mCryptoHandlers = new LinkedHashMap();
        this.mContext = context.getApplicationContext();
        this.mCryptoFactory = iCryptoFactory;
        this.mApiLevel = i;
        KeyStore keyStore2 = null;
        if (i >= 19) {
            try {
                keyStore = KeyStore.getInstance("AndroidKeyStore");
            } catch (Exception unused) {
            }
            try {
                keyStore.load(null);
                keyStore2 = keyStore;
            } catch (Exception unused2) {
                keyStore2 = keyStore;
                AppCenterLog.error("AppCenter", "Cannot use secure keystore on this device.");
                this.mKeyStore = keyStore2;
                if (keyStore2 != null) {
                    try {
                        registerHandler(new CryptoAesHandler());
                    } catch (Exception unused3) {
                        AppCenterLog.error("AppCenter", "Cannot use modern encryption on this device.");
                    }
                }
                if (keyStore2 != null) {
                }
                CryptoNoOpHandler cryptoNoOpHandler = new CryptoNoOpHandler();
                this.mCryptoHandlers.put(cryptoNoOpHandler.getAlgorithm(), new CryptoHandlerEntry(0, cryptoNoOpHandler));
            }
        }
        this.mKeyStore = keyStore2;
        if (keyStore2 != null && i >= 23) {
            registerHandler(new CryptoAesHandler());
        }
        if (keyStore2 != null) {
            try {
                registerHandler(new CryptoRsaHandler());
            } catch (Exception unused4) {
                AppCenterLog.error("AppCenter", "Cannot use old encryption on this device.");
            }
        }
        CryptoNoOpHandler cryptoNoOpHandler2 = new CryptoNoOpHandler();
        this.mCryptoHandlers.put(cryptoNoOpHandler2.getAlgorithm(), new CryptoHandlerEntry(0, cryptoNoOpHandler2));
    }

    private String getAlias(CryptoHandler cryptoHandler, int i) {
        return "appcenter." + i + "." + cryptoHandler.getAlgorithm();
    }

    private DecryptedData getDecryptedData(CryptoHandler cryptoHandler, int i, String str) {
        String str2 = new String(cryptoHandler.decrypt(this.mCryptoFactory, this.mApiLevel, getKeyStoreEntry(cryptoHandler, i), Base64.decode(str, 0)), "UTF-8");
        return new DecryptedData(str2, cryptoHandler != ((CryptoHandlerEntry) this.mCryptoHandlers.values().iterator().next()).mCryptoHandler ? encrypt(str2) : null);
    }

    public static CryptoUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CryptoUtils(context);
        }
        return sInstance;
    }

    private KeyStore.Entry getKeyStoreEntry(CryptoHandler cryptoHandler, int i) {
        if (this.mKeyStore == null) {
            return null;
        }
        return this.mKeyStore.getEntry(getAlias(cryptoHandler, i), null);
    }

    private KeyStore.Entry getKeyStoreEntry(CryptoHandlerEntry cryptoHandlerEntry) {
        return getKeyStoreEntry(cryptoHandlerEntry.mCryptoHandler, cryptoHandlerEntry.mAliasIndex);
    }

    private void registerHandler(CryptoHandler cryptoHandler) {
        int i = 0;
        String alias = getAlias(cryptoHandler, 0);
        String alias2 = getAlias(cryptoHandler, 1);
        Date creationDate = this.mKeyStore.getCreationDate(alias);
        Date creationDate2 = this.mKeyStore.getCreationDate(alias2);
        if (creationDate2 != null && creationDate2.after(creationDate)) {
            alias = alias2;
            i = 1;
        }
        if (this.mCryptoHandlers.isEmpty() && !this.mKeyStore.containsAlias(alias)) {
            AppCenterLog.debug("AppCenter", "Creating alias: " + alias);
            cryptoHandler.generateKey(this.mCryptoFactory, alias, this.mContext);
        }
        AppCenterLog.debug("AppCenter", "Using " + alias);
        this.mCryptoHandlers.put(cryptoHandler.getAlgorithm(), new CryptoHandlerEntry(i, cryptoHandler));
    }

    public DecryptedData decrypt(String str) {
        if (str == null) {
            return new DecryptedData(null, null);
        }
        String[] split = str.split(":");
        CryptoHandlerEntry cryptoHandlerEntry = split.length == 2 ? (CryptoHandlerEntry) this.mCryptoHandlers.get(split[0]) : null;
        CryptoHandler cryptoHandler = cryptoHandlerEntry == null ? null : cryptoHandlerEntry.mCryptoHandler;
        if (cryptoHandler == null) {
            AppCenterLog.error("AppCenter", "Failed to decrypt data.");
            return new DecryptedData(str, null);
        }
        try {
            try {
                return getDecryptedData(cryptoHandler, cryptoHandlerEntry.mAliasIndex, split[1]);
            } catch (Exception unused) {
                return getDecryptedData(cryptoHandler, cryptoHandlerEntry.mAliasIndex ^ 1, split[1]);
            }
        } catch (Exception unused2) {
            AppCenterLog.error("AppCenter", "Failed to decrypt data.");
            return new DecryptedData(str, null);
        }
    }

    public String encrypt(String str) {
        if (str == null) {
            return null;
        }
        try {
            CryptoHandlerEntry cryptoHandlerEntry = (CryptoHandlerEntry) this.mCryptoHandlers.values().iterator().next();
            CryptoHandler cryptoHandler = cryptoHandlerEntry.mCryptoHandler;
            try {
                return cryptoHandler.getAlgorithm() + ":" + Base64.encodeToString(cryptoHandler.encrypt(this.mCryptoFactory, this.mApiLevel, getKeyStoreEntry(cryptoHandlerEntry), str.getBytes("UTF-8")), 0);
            } catch (InvalidKeyException e) {
                if (!(e.getCause() instanceof CertificateExpiredException) && !"android.security.keystore.KeyExpiredException".equals(e.getClass().getName())) {
                    throw e;
                }
                AppCenterLog.debug("AppCenter", "Alias expired: " + cryptoHandlerEntry.mAliasIndex);
                int i = cryptoHandlerEntry.mAliasIndex ^ 1;
                cryptoHandlerEntry.mAliasIndex = i;
                String alias = getAlias(cryptoHandler, i);
                if (this.mKeyStore.containsAlias(alias)) {
                    AppCenterLog.debug("AppCenter", "Deleting alias: " + alias);
                    this.mKeyStore.deleteEntry(alias);
                }
                AppCenterLog.debug("AppCenter", "Creating alias: " + alias);
                cryptoHandler.generateKey(this.mCryptoFactory, alias, this.mContext);
                return encrypt(str);
            }
        } catch (Exception unused) {
            AppCenterLog.error("AppCenter", "Failed to encrypt data.");
            return str;
        }
    }
}
