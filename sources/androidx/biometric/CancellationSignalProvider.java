package androidx.biometric;

import android.os.CancellationSignal;
import android.util.Log;

/* loaded from: classes.dex */
class CancellationSignalProvider {
    private CancellationSignal mBiometricCancellationSignal;
    private androidx.core.os.CancellationSignal mFingerprintCancellationSignal;
    private final Injector mInjector = new Injector() { // from class: androidx.biometric.CancellationSignalProvider.1
        @Override // androidx.biometric.CancellationSignalProvider.Injector
        public CancellationSignal getBiometricCancellationSignal() {
            return Api16Impl.create();
        }

        @Override // androidx.biometric.CancellationSignalProvider.Injector
        public androidx.core.os.CancellationSignal getFingerprintCancellationSignal() {
            return new androidx.core.os.CancellationSignal();
        }
    };

    /* loaded from: classes.dex */
    private static class Api16Impl {
        static void cancel(CancellationSignal cancellationSignal) {
            cancellationSignal.cancel();
        }

        static CancellationSignal create() {
            return new CancellationSignal();
        }
    }

    /* loaded from: classes.dex */
    interface Injector {
        CancellationSignal getBiometricCancellationSignal();

        androidx.core.os.CancellationSignal getFingerprintCancellationSignal();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancel() {
        CancellationSignal cancellationSignal = this.mBiometricCancellationSignal;
        if (cancellationSignal != null) {
            try {
                Api16Impl.cancel(cancellationSignal);
            } catch (NullPointerException e) {
                Log.e("CancelSignalProvider", "Got NPE while canceling biometric authentication.", e);
            }
            this.mBiometricCancellationSignal = null;
        }
        androidx.core.os.CancellationSignal cancellationSignal2 = this.mFingerprintCancellationSignal;
        if (cancellationSignal2 != null) {
            try {
                cancellationSignal2.cancel();
            } catch (NullPointerException e2) {
                Log.e("CancelSignalProvider", "Got NPE while canceling fingerprint authentication.", e2);
            }
            this.mFingerprintCancellationSignal = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CancellationSignal getBiometricCancellationSignal() {
        if (this.mBiometricCancellationSignal == null) {
            this.mBiometricCancellationSignal = this.mInjector.getBiometricCancellationSignal();
        }
        return this.mBiometricCancellationSignal;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.core.os.CancellationSignal getFingerprintCancellationSignal() {
        if (this.mFingerprintCancellationSignal == null) {
            this.mFingerprintCancellationSignal = this.mInjector.getFingerprintCancellationSignal();
        }
        return this.mFingerprintCancellationSignal;
    }
}
