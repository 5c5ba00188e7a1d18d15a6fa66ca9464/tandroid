package com.microsoft.appcenter.utils.crypto;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
    static final ICryptoFactory DEFAULT_CRYPTO_FACTORY = new AnonymousClass1();
    @SuppressLint({"StaticFieldLeak"})
    private static CryptoUtils sInstance;
    private final int mApiLevel;
    private final Context mContext;
    private final ICryptoFactory mCryptoFactory;
    private final Map<String, CryptoHandlerEntry> mCryptoHandlers;
    private final KeyStore mKeyStore;

    /* loaded from: classes.dex */
    public interface ICipher {
        byte[] doFinal(byte[] bArr) throws Exception;

        byte[] doFinal(byte[] bArr, int i, int i2) throws Exception;

        int getBlockSize();

        byte[] getIV();

        void init(int i, Key key) throws Exception;

        void init(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec) throws Exception;
    }

    /* loaded from: classes.dex */
    public interface ICryptoFactory {
        ICipher getCipher(String str, String str2) throws Exception;

        IKeyGenerator getKeyGenerator(String str, String str2) throws Exception;
    }

    /* loaded from: classes.dex */
    interface IKeyGenerator {
        void generateKey();

        void init(AlgorithmParameterSpec algorithmParameterSpec) throws Exception;
    }

    /* renamed from: com.microsoft.appcenter.utils.crypto.CryptoUtils$1 */
    /* loaded from: classes.dex */
    static class AnonymousClass1 implements ICryptoFactory {
        AnonymousClass1() {
        }

        /* renamed from: com.microsoft.appcenter.utils.crypto.CryptoUtils$1$1 */
        /* loaded from: classes.dex */
        class C00081 implements IKeyGenerator {
            final /* synthetic */ KeyGenerator val$keyGenerator;

            C00081(AnonymousClass1 anonymousClass1, KeyGenerator keyGenerator) {
                this.val$keyGenerator = keyGenerator;
            }

            @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.IKeyGenerator
            public void init(AlgorithmParameterSpec algorithmParameterSpec) throws Exception {
                this.val$keyGenerator.init(algorithmParameterSpec);
            }

            @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.IKeyGenerator
            public void generateKey() {
                this.val$keyGenerator.generateKey();
            }
        }

        @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICryptoFactory
        public IKeyGenerator getKeyGenerator(String str, String str2) throws Exception {
            return new C00081(this, KeyGenerator.getInstance(str, str2));
        }

        /* renamed from: com.microsoft.appcenter.utils.crypto.CryptoUtils$1$2 */
        /* loaded from: classes.dex */
        class AnonymousClass2 implements ICipher {
            final /* synthetic */ Cipher val$cipher;

            AnonymousClass2(AnonymousClass1 anonymousClass1, Cipher cipher) {
                this.val$cipher = cipher;
            }

            @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICipher
            public void init(int i, Key key) throws Exception {
                this.val$cipher.init(i, key);
            }

            @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICipher
            public void init(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec) throws Exception {
                this.val$cipher.init(i, key, algorithmParameterSpec);
            }

            @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICipher
            public byte[] doFinal(byte[] bArr) throws Exception {
                return this.val$cipher.doFinal(bArr);
            }

            @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICipher
            public byte[] doFinal(byte[] bArr, int i, int i2) throws Exception {
                return this.val$cipher.doFinal(bArr, i, i2);
            }

            @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICipher
            public byte[] getIV() {
                return this.val$cipher.getIV();
            }

            @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICipher
            public int getBlockSize() {
                return this.val$cipher.getBlockSize();
            }
        }

        @Override // com.microsoft.appcenter.utils.crypto.CryptoUtils.ICryptoFactory
        public ICipher getCipher(String str, String str2) throws Exception {
            return new AnonymousClass2(this, Cipher.getInstance(str, str2));
        }
    }

    private CryptoUtils(Context context) {
        this(context, DEFAULT_CRYPTO_FACTORY, Build.VERSION.SDK_INT);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0044 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @TargetApi(23)
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

    public static CryptoUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CryptoUtils(context);
        }
        return sInstance;
    }

    private void registerHandler(CryptoHandler cryptoHandler) throws Exception {
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

    private String getAlias(CryptoHandler cryptoHandler, int i) {
        return "appcenter." + i + "." + cryptoHandler.getAlgorithm();
    }

    private KeyStore.Entry getKeyStoreEntry(CryptoHandlerEntry cryptoHandlerEntry) throws Exception {
        return getKeyStoreEntry(cryptoHandlerEntry.mCryptoHandler, cryptoHandlerEntry.mAliasIndex);
    }

    private KeyStore.Entry getKeyStoreEntry(CryptoHandler cryptoHandler, int i) throws Exception {
        if (this.mKeyStore == null) {
            return null;
        }
        return this.mKeyStore.getEntry(getAlias(cryptoHandler, i), null);
    }

    public String encrypt(String str) {
        if (str == null) {
            return null;
        }
        try {
            CryptoHandlerEntry next = this.mCryptoHandlers.values().iterator().next();
            CryptoHandler cryptoHandler = next.mCryptoHandler;
            try {
                String encodeToString = Base64.encodeToString(cryptoHandler.encrypt(this.mCryptoFactory, this.mApiLevel, getKeyStoreEntry(next), str.getBytes("UTF-8")), 0);
                return cryptoHandler.getAlgorithm() + ":" + encodeToString;
            } catch (InvalidKeyException e) {
                if (!(e.getCause() instanceof CertificateExpiredException) && !"android.security.keystore.KeyExpiredException".equals(e.getClass().getName())) {
                    throw e;
                }
                AppCenterLog.debug("AppCenter", "Alias expired: " + next.mAliasIndex);
                int i = next.mAliasIndex ^ 1;
                next.mAliasIndex = i;
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

    public DecryptedData decrypt(String str) {
        if (str == null) {
            return new DecryptedData(null, null);
        }
        String[] split = str.split(":");
        CryptoHandlerEntry cryptoHandlerEntry = split.length == 2 ? this.mCryptoHandlers.get(split[0]) : null;
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

    private DecryptedData getDecryptedData(CryptoHandler cryptoHandler, int i, String str) throws Exception {
        String str2 = new String(cryptoHandler.decrypt(this.mCryptoFactory, this.mApiLevel, getKeyStoreEntry(cryptoHandler, i), Base64.decode(str, 0)), "UTF-8");
        return new DecryptedData(str2, cryptoHandler != this.mCryptoHandlers.values().iterator().next().mCryptoHandler ? encrypt(str2) : null);
    }

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
}
