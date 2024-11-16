package androidx.biometric;

import java.util.Arrays;

/* loaded from: classes.dex */
class BiometricErrorData {
    private final int mErrorCode;
    private final CharSequence mErrorMessage;

    BiometricErrorData(int i, CharSequence charSequence) {
        this.mErrorCode = i;
        this.mErrorMessage = charSequence;
    }

    private static String convertToString(CharSequence charSequence) {
        if (charSequence != null) {
            return charSequence.toString();
        }
        return null;
    }

    private boolean isErrorMessageEqualTo(CharSequence charSequence) {
        String convertToString = convertToString(this.mErrorMessage);
        String convertToString2 = convertToString(charSequence);
        return (convertToString == null && convertToString2 == null) || (convertToString != null && convertToString.equals(convertToString2));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BiometricErrorData)) {
            return false;
        }
        BiometricErrorData biometricErrorData = (BiometricErrorData) obj;
        return this.mErrorCode == biometricErrorData.mErrorCode && isErrorMessageEqualTo(biometricErrorData.mErrorMessage);
    }

    int getErrorCode() {
        return this.mErrorCode;
    }

    CharSequence getErrorMessage() {
        return this.mErrorMessage;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.mErrorCode), convertToString(this.mErrorMessage)});
    }
}
