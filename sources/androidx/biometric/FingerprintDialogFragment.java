package androidx.biometric;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/* loaded from: classes.dex */
public class FingerprintDialogFragment extends DialogFragment {
    private int mErrorTextColor;
    private ImageView mFingerprintIcon;
    TextView mHelpMessageView;
    private int mNormalTextColor;
    BiometricViewModel mViewModel;
    final Handler mHandler = new Handler(Looper.getMainLooper());
    final Runnable mResetDialogRunnable = new Runnable() { // from class: androidx.biometric.FingerprintDialogFragment.1
        @Override // java.lang.Runnable
        public void run() {
            FingerprintDialogFragment.this.resetDialog();
        }
    };

    private static class Api21Impl {
        static void startAnimation(Drawable drawable) {
            if (drawable instanceof AnimatedVectorDrawable) {
                ((AnimatedVectorDrawable) drawable).start();
            }
        }
    }

    private static class Api26Impl {
        static int getColorErrorAttr() {
            return R$attr.colorError;
        }
    }

    private void connectViewModel() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        BiometricViewModel biometricViewModel = (BiometricViewModel) new ViewModelProvider(activity).get(BiometricViewModel.class);
        this.mViewModel = biometricViewModel;
        biometricViewModel.getFingerprintDialogState().observe(this, new Observer() { // from class: androidx.biometric.FingerprintDialogFragment.3
            @Override // androidx.lifecycle.Observer
            public void onChanged(Integer num) {
                FingerprintDialogFragment fingerprintDialogFragment = FingerprintDialogFragment.this;
                fingerprintDialogFragment.mHandler.removeCallbacks(fingerprintDialogFragment.mResetDialogRunnable);
                FingerprintDialogFragment.this.updateFingerprintIcon(num.intValue());
                FingerprintDialogFragment.this.updateHelpMessageColor(num.intValue());
                FingerprintDialogFragment fingerprintDialogFragment2 = FingerprintDialogFragment.this;
                fingerprintDialogFragment2.mHandler.postDelayed(fingerprintDialogFragment2.mResetDialogRunnable, 2000L);
            }
        });
        this.mViewModel.getFingerprintDialogHelpMessage().observe(this, new Observer() { // from class: androidx.biometric.FingerprintDialogFragment.4
            @Override // androidx.lifecycle.Observer
            public void onChanged(CharSequence charSequence) {
                FingerprintDialogFragment fingerprintDialogFragment = FingerprintDialogFragment.this;
                fingerprintDialogFragment.mHandler.removeCallbacks(fingerprintDialogFragment.mResetDialogRunnable);
                FingerprintDialogFragment.this.updateHelpMessageText(charSequence);
                FingerprintDialogFragment fingerprintDialogFragment2 = FingerprintDialogFragment.this;
                fingerprintDialogFragment2.mHandler.postDelayed(fingerprintDialogFragment2.mResetDialogRunnable, 2000L);
            }
        });
    }

    private Drawable getAssetForTransition(int i, int i2) {
        int i3;
        Context context = getContext();
        if (context == null) {
            Log.w("FingerprintFragment", "Unable to get asset. Context is null.");
            return null;
        }
        if (i != 0 || i2 != 1) {
            if (i == 1 && i2 == 2) {
                i3 = R$drawable.fingerprint_dialog_error;
                return ContextCompat.getDrawable(context, i3);
            }
            if ((i != 2 || i2 != 1) && (i != 1 || i2 != 3)) {
                return null;
            }
        }
        i3 = R$drawable.fingerprint_dialog_fp_icon;
        return ContextCompat.getDrawable(context, i3);
    }

    private int getThemedColorFor(int i) {
        Context context = getContext();
        FragmentActivity activity = getActivity();
        if (context == null || activity == null) {
            Log.w("FingerprintFragment", "Unable to get themed color. Context or activity is null.");
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(i, typedValue, true);
        TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(typedValue.data, new int[]{i});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        return color;
    }

    static FingerprintDialogFragment newInstance() {
        return new FingerprintDialogFragment();
    }

    private boolean shouldAnimateForTransition(int i, int i2) {
        if (i == 0 && i2 == 1) {
            return false;
        }
        if (i == 1 && i2 == 2) {
            return true;
        }
        return i == 2 && i2 == 1;
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        this.mViewModel.setFingerprintDialogCancelPending(true);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        int color;
        super.onCreate(bundle);
        connectViewModel();
        if (Build.VERSION.SDK_INT >= 26) {
            color = getThemedColorFor(Api26Impl.getColorErrorAttr());
        } else {
            Context context = getContext();
            color = context != null ? ContextCompat.getColor(context, R$color.biometric_error_color) : 0;
        }
        this.mErrorTextColor = color;
        this.mNormalTextColor = getThemedColorFor(R.attr.textColorSecondary);
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(this.mViewModel.getTitle());
        View inflate = LayoutInflater.from(builder.getContext()).inflate(R$layout.fingerprint_dialog_layout, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R$id.fingerprint_subtitle);
        if (textView != null) {
            CharSequence subtitle = this.mViewModel.getSubtitle();
            if (TextUtils.isEmpty(subtitle)) {
                textView.setVisibility(8);
            } else {
                textView.setVisibility(0);
                textView.setText(subtitle);
            }
        }
        TextView textView2 = (TextView) inflate.findViewById(R$id.fingerprint_description);
        if (textView2 != null) {
            CharSequence description = this.mViewModel.getDescription();
            if (TextUtils.isEmpty(description)) {
                textView2.setVisibility(8);
            } else {
                textView2.setVisibility(0);
                textView2.setText(description);
            }
        }
        this.mFingerprintIcon = (ImageView) inflate.findViewById(R$id.fingerprint_icon);
        this.mHelpMessageView = (TextView) inflate.findViewById(R$id.fingerprint_error);
        builder.setNegativeButton(AuthenticatorUtils.isDeviceCredentialAllowed(this.mViewModel.getAllowedAuthenticators()) ? getString(R$string.confirm_device_credential_password) : this.mViewModel.getNegativeButtonText(), new DialogInterface.OnClickListener() { // from class: androidx.biometric.FingerprintDialogFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                FingerprintDialogFragment.this.mViewModel.setNegativeButtonPressPending(true);
            }
        });
        builder.setView(inflate);
        AlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.mViewModel.setFingerprintDialogPreviousState(0);
        this.mViewModel.setFingerprintDialogState(1);
        this.mViewModel.setFingerprintDialogHelpMessage(getString(R$string.fingerprint_dialog_touch_sensor));
    }

    void resetDialog() {
        Context context = getContext();
        if (context == null) {
            Log.w("FingerprintFragment", "Not resetting the dialog. Context is null.");
        } else {
            this.mViewModel.setFingerprintDialogState(1);
            this.mViewModel.setFingerprintDialogHelpMessage(context.getString(R$string.fingerprint_dialog_touch_sensor));
        }
    }

    void updateFingerprintIcon(int i) {
        int fingerprintDialogPreviousState;
        Drawable assetForTransition;
        if (this.mFingerprintIcon == null || Build.VERSION.SDK_INT < 23 || (assetForTransition = getAssetForTransition((fingerprintDialogPreviousState = this.mViewModel.getFingerprintDialogPreviousState()), i)) == null) {
            return;
        }
        this.mFingerprintIcon.setImageDrawable(assetForTransition);
        if (shouldAnimateForTransition(fingerprintDialogPreviousState, i)) {
            Api21Impl.startAnimation(assetForTransition);
        }
        this.mViewModel.setFingerprintDialogPreviousState(i);
    }

    void updateHelpMessageColor(int i) {
        TextView textView = this.mHelpMessageView;
        if (textView != null) {
            textView.setTextColor(i == 2 ? this.mErrorTextColor : this.mNormalTextColor);
        }
    }

    void updateHelpMessageText(CharSequence charSequence) {
        TextView textView = this.mHelpMessageView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }
}
