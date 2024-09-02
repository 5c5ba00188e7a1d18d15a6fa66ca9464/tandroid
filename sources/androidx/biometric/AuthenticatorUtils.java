package androidx.biometric;

import android.os.Build;
import androidx.biometric.BiometricPrompt;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.NotificationCenter;
/* loaded from: classes.dex */
abstract class AuthenticatorUtils {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static String convertToString(int i) {
        return i != 15 ? i != 255 ? i != 32768 ? i != 32783 ? i != 33023 ? String.valueOf(i) : "BIOMETRIC_WEAK | DEVICE_CREDENTIAL" : "BIOMETRIC_STRONG | DEVICE_CREDENTIAL" : "DEVICE_CREDENTIAL" : "BIOMETRIC_WEAK" : "BIOMETRIC_STRONG";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getConsolidatedAuthenticators(BiometricPrompt.PromptInfo promptInfo, BiometricPrompt.CryptoObject cryptoObject) {
        if (promptInfo.getAllowedAuthenticators() != 0) {
            return promptInfo.getAllowedAuthenticators();
        }
        int i = cryptoObject != null ? 15 : NotificationCenter.didClearDatabase;
        return promptInfo.isDeviceCredentialAllowed() ? 32768 | i : i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isDeviceCredentialAllowed(int i) {
        return (i & LiteMode.FLAG_CHAT_SCALE) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSomeBiometricAllowed(int i) {
        return (i & 32767) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSupportedCombination(int i) {
        if (i == 15 || i == 255) {
            return true;
        }
        if (i == 32768) {
            return Build.VERSION.SDK_INT >= 30;
        } else if (i != 32783) {
            return i == 33023 || i == 0;
        } else {
            int i2 = Build.VERSION.SDK_INT;
            return i2 < 28 || i2 > 29;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isWeakBiometricAllowed(int i) {
        return (i & NotificationCenter.didClearDatabase) == 255;
    }
}
