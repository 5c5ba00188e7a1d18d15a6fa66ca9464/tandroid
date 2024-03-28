package androidx.biometric;

import android.content.Context;
import android.os.Build;
/* loaded from: classes.dex */
class DeviceUtils {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean shouldUseFingerprintForCrypto(Context context, String str, String str2) {
        if (Build.VERSION.SDK_INT != 28) {
            return false;
        }
        return isVendorInList(context, str, R$array.crypto_fingerprint_fallback_vendors) || isModelInPrefixList(context, str2, R$array.crypto_fingerprint_fallback_prefixes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean shouldHideFingerprintDialog(Context context, String str) {
        if (Build.VERSION.SDK_INT != 28) {
            return false;
        }
        return isModelInPrefixList(context, str, R$array.hide_fingerprint_instantly_prefixes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean shouldDelayShowingPrompt(Context context, String str) {
        if (Build.VERSION.SDK_INT != 29) {
            return false;
        }
        return isModelInList(context, str, R$array.delay_showing_prompt_models);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean canAssumeStrongBiometrics(Context context, String str) {
        if (Build.VERSION.SDK_INT >= 30) {
            return false;
        }
        return isModelInList(context, str, R$array.assume_strong_biometrics_models);
    }

    private static boolean isVendorInList(Context context, String str, int i) {
        if (str == null) {
            return false;
        }
        for (String str2 : context.getResources().getStringArray(i)) {
            if (str.equalsIgnoreCase(str2)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isModelInPrefixList(Context context, String str, int i) {
        if (str == null) {
            return false;
        }
        for (String str2 : context.getResources().getStringArray(i)) {
            if (str.startsWith(str2)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isModelInList(Context context, String str, int i) {
        if (str == null) {
            return false;
        }
        for (String str2 : context.getResources().getStringArray(i)) {
            if (str.equals(str2)) {
                return true;
            }
        }
        return false;
    }
}
