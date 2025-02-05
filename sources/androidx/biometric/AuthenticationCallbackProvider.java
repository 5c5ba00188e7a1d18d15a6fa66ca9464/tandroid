package androidx.biometric;

import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import androidx.biometric.BiometricPrompt;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

/* loaded from: classes.dex */
class AuthenticationCallbackProvider {
    private BiometricPrompt.AuthenticationCallback mBiometricCallback;
    private FingerprintManagerCompat.AuthenticationCallback mFingerprintCallback;
    final Listener mListener;

    private static class Api28Impl {
        static BiometricPrompt.AuthenticationCallback createCallback(final Listener listener) {
            return new BiometricPrompt.AuthenticationCallback() { // from class: androidx.biometric.AuthenticationCallbackProvider.Api28Impl.1
                @Override // android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
                public void onAuthenticationError(int i, CharSequence charSequence) {
                    Listener.this.onError(i, charSequence);
                }

                @Override // android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
                public void onAuthenticationFailed() {
                    Listener.this.onFailure();
                }

                @Override // android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
                public void onAuthenticationHelp(int i, CharSequence charSequence) {
                }

                @Override // android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult authenticationResult) {
                    BiometricPrompt.CryptoObject unwrapFromBiometricPrompt = authenticationResult != null ? CryptoObjectUtils.unwrapFromBiometricPrompt(authenticationResult.getCryptoObject()) : null;
                    int i = Build.VERSION.SDK_INT;
                    int i2 = -1;
                    if (i >= 30) {
                        if (authenticationResult != null) {
                            i2 = Api30Impl.getAuthenticationType(authenticationResult);
                        }
                    } else if (i != 29) {
                        i2 = 2;
                    }
                    Listener.this.onSuccess(new BiometricPrompt.AuthenticationResult(unwrapFromBiometricPrompt, i2));
                }
            };
        }
    }

    private static class Api30Impl {
        static int getAuthenticationType(BiometricPrompt.AuthenticationResult authenticationResult) {
            return authenticationResult.getAuthenticationType();
        }
    }

    static class Listener {
        Listener() {
        }

        abstract void onError(int i, CharSequence charSequence);

        abstract void onFailure();

        abstract void onHelp(CharSequence charSequence);

        abstract void onSuccess(BiometricPrompt.AuthenticationResult authenticationResult);
    }

    AuthenticationCallbackProvider(Listener listener) {
        this.mListener = listener;
    }

    BiometricPrompt.AuthenticationCallback getBiometricCallback() {
        if (this.mBiometricCallback == null) {
            this.mBiometricCallback = Api28Impl.createCallback(this.mListener);
        }
        return this.mBiometricCallback;
    }

    FingerprintManagerCompat.AuthenticationCallback getFingerprintCallback() {
        if (this.mFingerprintCallback == null) {
            this.mFingerprintCallback = new FingerprintManagerCompat.AuthenticationCallback() { // from class: androidx.biometric.AuthenticationCallbackProvider.1
                @Override // androidx.core.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback
                public void onAuthenticationError(int i, CharSequence charSequence) {
                    AuthenticationCallbackProvider.this.mListener.onError(i, charSequence);
                }

                @Override // androidx.core.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback
                public void onAuthenticationFailed() {
                    AuthenticationCallbackProvider.this.mListener.onFailure();
                }

                @Override // androidx.core.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback
                public void onAuthenticationHelp(int i, CharSequence charSequence) {
                    AuthenticationCallbackProvider.this.mListener.onHelp(charSequence);
                }

                @Override // androidx.core.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult authenticationResult) {
                    AuthenticationCallbackProvider.this.mListener.onSuccess(new BiometricPrompt.AuthenticationResult(authenticationResult != null ? CryptoObjectUtils.unwrapFromFingerprintManager(authenticationResult.getCryptoObject()) : null, 2));
                }
            };
        }
        return this.mFingerprintCallback;
    }
}
