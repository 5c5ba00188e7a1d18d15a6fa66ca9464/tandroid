package androidx.biometric;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import androidx.biometric.AuthenticationCallbackProvider;
import androidx.biometric.BiometricPrompt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class BiometricViewModel extends ViewModel {
    private AuthenticationCallbackProvider mAuthenticationCallbackProvider;
    private MutableLiveData mAuthenticationError;
    private MutableLiveData mAuthenticationHelpMessage;
    private MutableLiveData mAuthenticationResult;
    private CancellationSignalProvider mCancellationSignalProvider;
    private BiometricPrompt.AuthenticationCallback mClientCallback;
    private Executor mClientExecutor;
    private BiometricPrompt.CryptoObject mCryptoObject;
    private MutableLiveData mFingerprintDialogHelpMessage;
    private MutableLiveData mFingerprintDialogState;
    private MutableLiveData mIsAuthenticationFailurePending;
    private boolean mIsAwaitingResult;
    private boolean mIsConfirmingDeviceCredential;
    private boolean mIsDelayingPrompt;
    private MutableLiveData mIsFingerprintDialogCancelPending;
    private boolean mIsIgnoringCancel;
    private MutableLiveData mIsNegativeButtonPressPending;
    private boolean mIsPromptShowing;
    private DialogInterface.OnClickListener mNegativeButtonListener;
    private CharSequence mNegativeButtonTextOverride;
    private BiometricPrompt.PromptInfo mPromptInfo;
    private int mCanceledFrom = 0;
    private boolean mIsFingerprintDialogDismissedInstantly = true;
    private int mFingerprintDialogPreviousState = 0;

    private static final class CallbackListener extends AuthenticationCallbackProvider.Listener {
        private final WeakReference mViewModelRef;

        CallbackListener(BiometricViewModel biometricViewModel) {
            this.mViewModelRef = new WeakReference(biometricViewModel);
        }

        @Override // androidx.biometric.AuthenticationCallbackProvider.Listener
        void onError(int i, CharSequence charSequence) {
            if (this.mViewModelRef.get() == null || ((BiometricViewModel) this.mViewModelRef.get()).isConfirmingDeviceCredential() || !((BiometricViewModel) this.mViewModelRef.get()).isAwaitingResult()) {
                return;
            }
            ((BiometricViewModel) this.mViewModelRef.get()).setAuthenticationError(new BiometricErrorData(i, charSequence));
        }

        @Override // androidx.biometric.AuthenticationCallbackProvider.Listener
        void onFailure() {
            if (this.mViewModelRef.get() == null || !((BiometricViewModel) this.mViewModelRef.get()).isAwaitingResult()) {
                return;
            }
            ((BiometricViewModel) this.mViewModelRef.get()).setAuthenticationFailurePending(true);
        }

        @Override // androidx.biometric.AuthenticationCallbackProvider.Listener
        void onHelp(CharSequence charSequence) {
            if (this.mViewModelRef.get() != null) {
                ((BiometricViewModel) this.mViewModelRef.get()).setAuthenticationHelpMessage(charSequence);
            }
        }

        @Override // androidx.biometric.AuthenticationCallbackProvider.Listener
        void onSuccess(BiometricPrompt.AuthenticationResult authenticationResult) {
            if (this.mViewModelRef.get() == null || !((BiometricViewModel) this.mViewModelRef.get()).isAwaitingResult()) {
                return;
            }
            if (authenticationResult.getAuthenticationType() == -1) {
                authenticationResult = new BiometricPrompt.AuthenticationResult(authenticationResult.getCryptoObject(), ((BiometricViewModel) this.mViewModelRef.get()).getInferredAuthenticationResultType());
            }
            ((BiometricViewModel) this.mViewModelRef.get()).setAuthenticationResult(authenticationResult);
        }
    }

    private static class DefaultExecutor implements Executor {
        private final Handler mHandler = new Handler(Looper.getMainLooper());

        DefaultExecutor() {
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }

    private static class NegativeButtonListener implements DialogInterface.OnClickListener {
        private final WeakReference mViewModelRef;

        NegativeButtonListener(BiometricViewModel biometricViewModel) {
            this.mViewModelRef = new WeakReference(biometricViewModel);
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (this.mViewModelRef.get() != null) {
                ((BiometricViewModel) this.mViewModelRef.get()).setNegativeButtonPressPending(true);
            }
        }
    }

    private static void updateValue(MutableLiveData mutableLiveData, Object obj) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            mutableLiveData.setValue(obj);
        } else {
            mutableLiveData.postValue(obj);
        }
    }

    int getAllowedAuthenticators() {
        BiometricPrompt.PromptInfo promptInfo = this.mPromptInfo;
        if (promptInfo != null) {
            return AuthenticatorUtils.getConsolidatedAuthenticators(promptInfo, this.mCryptoObject);
        }
        return 0;
    }

    AuthenticationCallbackProvider getAuthenticationCallbackProvider() {
        if (this.mAuthenticationCallbackProvider == null) {
            this.mAuthenticationCallbackProvider = new AuthenticationCallbackProvider(new CallbackListener(this));
        }
        return this.mAuthenticationCallbackProvider;
    }

    MutableLiveData getAuthenticationError() {
        if (this.mAuthenticationError == null) {
            this.mAuthenticationError = new MutableLiveData();
        }
        return this.mAuthenticationError;
    }

    LiveData getAuthenticationHelpMessage() {
        if (this.mAuthenticationHelpMessage == null) {
            this.mAuthenticationHelpMessage = new MutableLiveData();
        }
        return this.mAuthenticationHelpMessage;
    }

    LiveData getAuthenticationResult() {
        if (this.mAuthenticationResult == null) {
            this.mAuthenticationResult = new MutableLiveData();
        }
        return this.mAuthenticationResult;
    }

    int getCanceledFrom() {
        return this.mCanceledFrom;
    }

    CancellationSignalProvider getCancellationSignalProvider() {
        if (this.mCancellationSignalProvider == null) {
            this.mCancellationSignalProvider = new CancellationSignalProvider();
        }
        return this.mCancellationSignalProvider;
    }

    BiometricPrompt.AuthenticationCallback getClientCallback() {
        if (this.mClientCallback == null) {
            this.mClientCallback = new BiometricPrompt.AuthenticationCallback() { // from class: androidx.biometric.BiometricViewModel.1
            };
        }
        return this.mClientCallback;
    }

    Executor getClientExecutor() {
        Executor executor = this.mClientExecutor;
        return executor != null ? executor : new DefaultExecutor();
    }

    BiometricPrompt.CryptoObject getCryptoObject() {
        return this.mCryptoObject;
    }

    CharSequence getDescription() {
        BiometricPrompt.PromptInfo promptInfo = this.mPromptInfo;
        if (promptInfo != null) {
            return promptInfo.getDescription();
        }
        return null;
    }

    LiveData getFingerprintDialogHelpMessage() {
        if (this.mFingerprintDialogHelpMessage == null) {
            this.mFingerprintDialogHelpMessage = new MutableLiveData();
        }
        return this.mFingerprintDialogHelpMessage;
    }

    int getFingerprintDialogPreviousState() {
        return this.mFingerprintDialogPreviousState;
    }

    LiveData getFingerprintDialogState() {
        if (this.mFingerprintDialogState == null) {
            this.mFingerprintDialogState = new MutableLiveData();
        }
        return this.mFingerprintDialogState;
    }

    int getInferredAuthenticationResultType() {
        int allowedAuthenticators = getAllowedAuthenticators();
        return (!AuthenticatorUtils.isSomeBiometricAllowed(allowedAuthenticators) || AuthenticatorUtils.isDeviceCredentialAllowed(allowedAuthenticators)) ? -1 : 2;
    }

    DialogInterface.OnClickListener getNegativeButtonListener() {
        if (this.mNegativeButtonListener == null) {
            this.mNegativeButtonListener = new NegativeButtonListener(this);
        }
        return this.mNegativeButtonListener;
    }

    CharSequence getNegativeButtonText() {
        CharSequence charSequence = this.mNegativeButtonTextOverride;
        if (charSequence != null) {
            return charSequence;
        }
        BiometricPrompt.PromptInfo promptInfo = this.mPromptInfo;
        if (promptInfo != null) {
            return promptInfo.getNegativeButtonText();
        }
        return null;
    }

    CharSequence getSubtitle() {
        BiometricPrompt.PromptInfo promptInfo = this.mPromptInfo;
        if (promptInfo != null) {
            return promptInfo.getSubtitle();
        }
        return null;
    }

    CharSequence getTitle() {
        BiometricPrompt.PromptInfo promptInfo = this.mPromptInfo;
        if (promptInfo != null) {
            return promptInfo.getTitle();
        }
        return null;
    }

    LiveData isAuthenticationFailurePending() {
        if (this.mIsAuthenticationFailurePending == null) {
            this.mIsAuthenticationFailurePending = new MutableLiveData();
        }
        return this.mIsAuthenticationFailurePending;
    }

    boolean isAwaitingResult() {
        return this.mIsAwaitingResult;
    }

    boolean isConfirmationRequired() {
        BiometricPrompt.PromptInfo promptInfo = this.mPromptInfo;
        return promptInfo == null || promptInfo.isConfirmationRequired();
    }

    boolean isConfirmingDeviceCredential() {
        return this.mIsConfirmingDeviceCredential;
    }

    boolean isDelayingPrompt() {
        return this.mIsDelayingPrompt;
    }

    LiveData isFingerprintDialogCancelPending() {
        if (this.mIsFingerprintDialogCancelPending == null) {
            this.mIsFingerprintDialogCancelPending = new MutableLiveData();
        }
        return this.mIsFingerprintDialogCancelPending;
    }

    boolean isFingerprintDialogDismissedInstantly() {
        return this.mIsFingerprintDialogDismissedInstantly;
    }

    boolean isIgnoringCancel() {
        return this.mIsIgnoringCancel;
    }

    LiveData isNegativeButtonPressPending() {
        if (this.mIsNegativeButtonPressPending == null) {
            this.mIsNegativeButtonPressPending = new MutableLiveData();
        }
        return this.mIsNegativeButtonPressPending;
    }

    boolean isPromptShowing() {
        return this.mIsPromptShowing;
    }

    void resetClientCallback() {
        this.mClientCallback = null;
    }

    void setAuthenticationError(BiometricErrorData biometricErrorData) {
        if (this.mAuthenticationError == null) {
            this.mAuthenticationError = new MutableLiveData();
        }
        updateValue(this.mAuthenticationError, biometricErrorData);
    }

    void setAuthenticationFailurePending(boolean z) {
        if (this.mIsAuthenticationFailurePending == null) {
            this.mIsAuthenticationFailurePending = new MutableLiveData();
        }
        updateValue(this.mIsAuthenticationFailurePending, Boolean.valueOf(z));
    }

    void setAuthenticationHelpMessage(CharSequence charSequence) {
        if (this.mAuthenticationHelpMessage == null) {
            this.mAuthenticationHelpMessage = new MutableLiveData();
        }
        updateValue(this.mAuthenticationHelpMessage, charSequence);
    }

    void setAuthenticationResult(BiometricPrompt.AuthenticationResult authenticationResult) {
        if (this.mAuthenticationResult == null) {
            this.mAuthenticationResult = new MutableLiveData();
        }
        updateValue(this.mAuthenticationResult, authenticationResult);
    }

    void setAwaitingResult(boolean z) {
        this.mIsAwaitingResult = z;
    }

    void setCanceledFrom(int i) {
        this.mCanceledFrom = i;
    }

    void setClientCallback(BiometricPrompt.AuthenticationCallback authenticationCallback) {
        this.mClientCallback = authenticationCallback;
    }

    void setClientExecutor(Executor executor) {
        this.mClientExecutor = executor;
    }

    void setConfirmingDeviceCredential(boolean z) {
        this.mIsConfirmingDeviceCredential = z;
    }

    void setCryptoObject(BiometricPrompt.CryptoObject cryptoObject) {
        this.mCryptoObject = cryptoObject;
    }

    void setDelayingPrompt(boolean z) {
        this.mIsDelayingPrompt = z;
    }

    void setFingerprintDialogCancelPending(boolean z) {
        if (this.mIsFingerprintDialogCancelPending == null) {
            this.mIsFingerprintDialogCancelPending = new MutableLiveData();
        }
        updateValue(this.mIsFingerprintDialogCancelPending, Boolean.valueOf(z));
    }

    void setFingerprintDialogDismissedInstantly(boolean z) {
        this.mIsFingerprintDialogDismissedInstantly = z;
    }

    void setFingerprintDialogHelpMessage(CharSequence charSequence) {
        if (this.mFingerprintDialogHelpMessage == null) {
            this.mFingerprintDialogHelpMessage = new MutableLiveData();
        }
        updateValue(this.mFingerprintDialogHelpMessage, charSequence);
    }

    void setFingerprintDialogPreviousState(int i) {
        this.mFingerprintDialogPreviousState = i;
    }

    void setFingerprintDialogState(int i) {
        if (this.mFingerprintDialogState == null) {
            this.mFingerprintDialogState = new MutableLiveData();
        }
        updateValue(this.mFingerprintDialogState, Integer.valueOf(i));
    }

    void setIgnoringCancel(boolean z) {
        this.mIsIgnoringCancel = z;
    }

    void setNegativeButtonPressPending(boolean z) {
        if (this.mIsNegativeButtonPressPending == null) {
            this.mIsNegativeButtonPressPending = new MutableLiveData();
        }
        updateValue(this.mIsNegativeButtonPressPending, Boolean.valueOf(z));
    }

    void setNegativeButtonTextOverride(CharSequence charSequence) {
        this.mNegativeButtonTextOverride = charSequence;
    }

    void setPromptInfo(BiometricPrompt.PromptInfo promptInfo) {
        this.mPromptInfo = promptInfo;
    }

    void setPromptShowing(boolean z) {
        this.mIsPromptShowing = z;
    }
}
