package androidx.biometric;

import android.os.Build;
import androidx.biometric.BiometricPrompt;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.NotificationCenter;
/* loaded from: classes.dex */
class AuthenticatorUtils {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isDeviceCredentialAllowed(int i) {
        return (i & LiteMode.FLAG_CHAT_SCALE) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSomeBiometricAllowed(int i) {
        return (i & 32767) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isWeakBiometricAllowed(int i) {
        return (i & NotificationCenter.voipServiceCreated) == 255;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String convertToString(int i) {
        if (i != 15) {
            if (i != 255) {
                if (i != 32768) {
                    if (i != 32783) {
                        if (i == 33023) {
                            return "BIOMETRIC_WEAK | DEVICE_CREDENTIAL";
                        }
                        return String.valueOf(i);
                    }
                    return "BIOMETRIC_STRONG | DEVICE_CREDENTIAL";
                }
                return "DEVICE_CREDENTIAL";
            }
            return "BIOMETRIC_WEAK";
        }
        return "BIOMETRIC_STRONG";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getConsolidatedAuthenticators(BiometricPrompt.PromptInfo promptInfo, BiometricPrompt.CryptoObject cryptoObject) {
        if (promptInfo.getAllowedAuthenticators() != 0) {
            return promptInfo.getAllowedAuthenticators();
        }
        int i = cryptoObject != null ? 15 : NotificationCenter.voipServiceCreated;
        return promptInfo.isDeviceCredentialAllowed() ? 32768 | i : i;
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
}
