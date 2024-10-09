package androidx.biometric;

import android.content.Context;
import android.util.Log;

/* loaded from: classes.dex */
abstract class ErrorUtils {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getFingerprintErrorString(Context context, int i) {
        int i2;
        if (context == null) {
            return "";
        }
        if (i != 1) {
            if (i != 7) {
                switch (i) {
                    case 9:
                        break;
                    case 10:
                        i2 = R$string.fingerprint_error_user_canceled;
                        break;
                    case 11:
                        i2 = R$string.fingerprint_error_no_fingerprints;
                        break;
                    case 12:
                        i2 = R$string.fingerprint_error_hw_not_present;
                        break;
                    default:
                        Log.e("BiometricUtils", "Unknown error code: " + i);
                        i2 = R$string.default_error_msg;
                        break;
                }
            }
            i2 = R$string.fingerprint_error_lockout;
        } else {
            i2 = R$string.fingerprint_error_hw_not_available;
        }
        return context.getString(i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isKnownError(int i) {
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return true;
            case 6:
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isLockoutError(int i) {
        return i == 7 || i == 9;
    }
}
