package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import androidx.biometric.BiometricManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.CustomPhoneKeyboardView;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberPicker;
import org.telegram.ui.Components.OutlineTextContainerView;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.TextViewSwitcher;
import org.telegram.ui.Components.TransformableLoginButtonView;
import org.telegram.ui.Components.VerticalPositionAutoAnimator;
import org.telegram.ui.PasscodeActivity;

/* loaded from: classes4.dex */
public class PasscodeActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int autoLockDetailRow;
    private int autoLockRow;
    private int captureDetailRow;
    private int captureHeaderRow;
    private int captureRow;
    private int changePasscodeRow;
    private CodeFieldContainer codeFieldContainer;
    private TextViewSwitcher descriptionTextSwitcher;
    private int disablePasscodeRow;
    private int fingerprintRow;
    private String firstPassword;
    private VerticalPositionAutoAnimator floatingAutoAnimator;
    private Animator floatingButtonAnimator;
    private FrameLayout floatingButtonContainer;
    private TransformableLoginButtonView floatingButtonIcon;
    private int hintRow;
    private CustomPhoneKeyboardView keyboardView;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private RLottieImageView lockImageView;
    private Runnable onShowKeyboardCallback;
    private ActionBarMenuItem otherItem;
    private OutlineTextContainerView outlinePasswordView;
    private TextView passcodesDoNotMatchTextView;
    private ImageView passwordButton;
    private EditTextBoldCursor passwordEditText;
    private boolean postedHidePasscodesDoNotMatch;
    private int rowCount;
    private TextView titleTextView;
    private int type;
    private int utyanRow;
    private int currentPasswordType = 0;
    private int passcodeSetStep = 0;
    private Runnable hidePasscodesDoNotMatch = new Runnable() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda11
        @Override // java.lang.Runnable
        public final void run() {
            PasscodeActivity.this.lambda$new$0();
        }
    };

    class 4 extends ActionBar.ActionBarMenuOnItemClick {
        final /* synthetic */ ActionBarMenuSubItem val$switchItem;

        4(ActionBarMenuSubItem actionBarMenuSubItem) {
            this.val$switchItem = actionBarMenuSubItem;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(ActionBarMenuSubItem actionBarMenuSubItem) {
            actionBarMenuSubItem.setText(LocaleController.getString(PasscodeActivity.this.currentPasswordType == 0 ? R.string.PasscodeSwitchToPassword : R.string.PasscodeSwitchToPIN));
            actionBarMenuSubItem.setIcon(PasscodeActivity.this.currentPasswordType == 0 ? R.drawable.msg_permissions : R.drawable.msg_pin_code);
            PasscodeActivity.this.showKeyboard();
            if (PasscodeActivity.this.isPinCode()) {
                PasscodeActivity.this.passwordEditText.setInputType(524417);
                AndroidUtilities.updateViewVisibilityAnimated(PasscodeActivity.this.passwordButton, true, 0.1f, false);
            }
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                PasscodeActivity.this.lambda$onBackPressed$321();
                return;
            }
            if (i == 1) {
                PasscodeActivity passcodeActivity = PasscodeActivity.this;
                passcodeActivity.currentPasswordType = passcodeActivity.currentPasswordType != 0 ? 0 : 1;
                final ActionBarMenuSubItem actionBarMenuSubItem = this.val$switchItem;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PasscodeActivity$4$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PasscodeActivity.4.this.lambda$onItemClick$0(actionBarMenuSubItem);
                    }
                }, 150L);
                PasscodeActivity.this.passwordEditText.setText("");
                for (CodeNumberField codeNumberField : PasscodeActivity.this.codeFieldContainer.codeField) {
                    codeNumberField.setText("");
                }
                PasscodeActivity.this.updateFields();
            }
        }
    }

    class 8 extends CodeFieldContainer {
        8(Context context) {
            super(context);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processNextPressed$0() {
            PasscodeActivity.this.processNext();
        }

        @Override // org.telegram.ui.CodeFieldContainer
        protected void processNextPressed() {
            if (PasscodeActivity.this.passcodeSetStep == 0) {
                postDelayed(new Runnable() { // from class: org.telegram.ui.PasscodeActivity$8$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PasscodeActivity.8.this.lambda$processNextPressed$0();
                    }
                }, 260L);
            } else {
                PasscodeActivity.this.processDone();
            }
        }
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return PasscodeActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == PasscodeActivity.this.fingerprintRow || i == PasscodeActivity.this.captureRow) {
                return 0;
            }
            if (i == PasscodeActivity.this.changePasscodeRow || i == PasscodeActivity.this.autoLockRow || i == PasscodeActivity.this.disablePasscodeRow) {
                return 1;
            }
            if (i == PasscodeActivity.this.autoLockDetailRow || i == PasscodeActivity.this.captureDetailRow || i == PasscodeActivity.this.hintRow) {
                return 2;
            }
            if (i == PasscodeActivity.this.captureHeaderRow) {
                return 3;
            }
            return i == PasscodeActivity.this.utyanRow ? 4 : 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == PasscodeActivity.this.fingerprintRow || adapterPosition == PasscodeActivity.this.autoLockRow || adapterPosition == PasscodeActivity.this.captureRow || adapterPosition == PasscodeActivity.this.changePasscodeRow || adapterPosition == PasscodeActivity.this.disablePasscodeRow;
        }

        /* JADX WARN: Code restructure failed: missing block: B:25:0x008f, code lost:
        
            if (org.telegram.messenger.LocaleController.isRTL != false) goto L27;
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x00b8, code lost:
        
            r3 = 5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x00b6, code lost:
        
            if (org.telegram.messenger.LocaleController.isRTL != false) goto L27;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String string;
            boolean z;
            int i2;
            TextView textView;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                if (i == PasscodeActivity.this.fingerprintRow) {
                    string = LocaleController.getString(R.string.UnlockFingerprint);
                    z = SharedConfig.useFingerprintLock;
                } else {
                    if (i != PasscodeActivity.this.captureRow) {
                        return;
                    }
                    string = LocaleController.getString(R.string.ScreenCaptureShowContent);
                    z = SharedConfig.allowScreenCapture;
                }
                textCheckCell.setTextAndCheck(string, z, false);
                return;
            }
            if (itemViewType == 1) {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                if (i == PasscodeActivity.this.changePasscodeRow) {
                    textSettingsCell.setText(LocaleController.getString(R.string.ChangePasscode), true);
                    if (SharedConfig.passcodeHash.length() == 0) {
                        i2 = Theme.key_windowBackgroundWhiteGrayText7;
                    }
                    i2 = Theme.key_windowBackgroundWhiteBlackText;
                } else if (i == PasscodeActivity.this.autoLockRow) {
                    int i3 = SharedConfig.autoLockIn;
                    textSettingsCell.setTextAndValue(LocaleController.getString(R.string.AutoLock), i3 == 0 ? LocaleController.formatString("AutoLockDisabled", R.string.AutoLockDisabled, new Object[0]) : i3 < 3600 ? LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Minutes", i3 / 60, new Object[0])) : i3 < 86400 ? LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Hours", (int) Math.ceil((i3 / 60.0f) / 60.0f), new Object[0])) : LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Days", (int) Math.ceil(((i3 / 60.0f) / 60.0f) / 24.0f), new Object[0])), true);
                    i2 = Theme.key_windowBackgroundWhiteBlackText;
                } else {
                    if (i != PasscodeActivity.this.disablePasscodeRow) {
                        return;
                    }
                    textSettingsCell.setText(LocaleController.getString(R.string.DisablePasscode), false);
                    i2 = Theme.key_text_RedBold;
                }
                textSettingsCell.setTag(Integer.valueOf(i2));
                textSettingsCell.setTextColor(Theme.getColor(i2));
                return;
            }
            int i4 = 3;
            if (itemViewType != 2) {
                if (itemViewType != 3) {
                    if (itemViewType != 4) {
                        return;
                    }
                    RLottieImageHolderView rLottieImageHolderView = (RLottieImageHolderView) viewHolder.itemView;
                    rLottieImageHolderView.imageView.setAnimation(R.raw.utyan_passcode, 100, 100);
                    rLottieImageHolderView.imageView.playAnimation();
                    return;
                }
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                headerCell.setHeight(46);
                if (i == PasscodeActivity.this.captureHeaderRow) {
                    headerCell.setText(LocaleController.getString(R.string.ScreenCaptureHeader));
                    return;
                }
                return;
            }
            TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
            if (i == PasscodeActivity.this.hintRow) {
                textInfoPrivacyCell.setText(LocaleController.getString(R.string.PasscodeScreenHint));
                textInfoPrivacyCell.setBackground(null);
                textInfoPrivacyCell.getTextView().setGravity(1);
                return;
            }
            if (i == PasscodeActivity.this.autoLockDetailRow) {
                textInfoPrivacyCell.setText(LocaleController.getString(R.string.AutoLockInfo));
                textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                textView = textInfoPrivacyCell.getTextView();
            } else {
                if (i != PasscodeActivity.this.captureDetailRow) {
                    return;
                }
                textInfoPrivacyCell.setText(LocaleController.getString(R.string.ScreenCaptureInfo));
                textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                textView = textInfoPrivacyCell.getTextView();
            }
            textView.setGravity(i4);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View textCheckCell;
            if (i == 0) {
                textCheckCell = new TextCheckCell(this.mContext);
            } else if (i == 1) {
                textCheckCell = new TextSettingsCell(this.mContext);
            } else {
                if (i != 3) {
                    textCheckCell = i != 4 ? new TextInfoPrivacyCell(this.mContext) : new RLottieImageHolderView(this.mContext);
                    return new RecyclerListView.Holder(textCheckCell);
                }
                textCheckCell = new HeaderCell(this.mContext);
            }
            textCheckCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            return new RecyclerListView.Holder(textCheckCell);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class RLottieImageHolderView extends FrameLayout {
        private RLottieImageView imageView;

        private RLottieImageHolderView(Context context) {
            super(context);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.imageView = rLottieImageView;
            rLottieImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PasscodeActivity$RLottieImageHolderView$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PasscodeActivity.RLottieImageHolderView.this.lambda$new$0(view);
                }
            });
            int dp = AndroidUtilities.dp(120.0f);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dp, dp);
            layoutParams.gravity = 1;
            addView(this.imageView, layoutParams);
            setPadding(0, AndroidUtilities.dp(32.0f), 0, 0);
            setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            if (this.imageView.getAnimatedDrawable().isRunning()) {
                return;
            }
            this.imageView.getAnimatedDrawable().setCurrentFrame(0, false);
            this.imageView.playAnimation();
        }
    }

    public PasscodeActivity(int i) {
        this.type = i;
    }

    private void animateSuccessAnimation(final Runnable runnable) {
        if (!isPinCode()) {
            runnable.run();
            return;
        }
        int i = 0;
        while (true) {
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            CodeNumberField[] codeNumberFieldArr = codeFieldContainer.codeField;
            if (i >= codeNumberFieldArr.length) {
                codeFieldContainer.postDelayed(new Runnable() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda23
                    @Override // java.lang.Runnable
                    public final void run() {
                        PasscodeActivity.this.lambda$animateSuccessAnimation$16(runnable);
                    }
                }, (this.codeFieldContainer.codeField.length * 75) + 350);
                return;
            } else {
                final CodeNumberField codeNumberField = codeNumberFieldArr[i];
                codeNumberField.postDelayed(new Runnable() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda22
                    @Override // java.lang.Runnable
                    public final void run() {
                        CodeNumberField.this.animateSuccessProgress(1.0f);
                    }
                }, i * 75);
                i++;
            }
        }
    }

    public static BaseFragment determineOpenFragment() {
        return SharedConfig.passcodeHash.length() != 0 ? new PasscodeActivity(2) : new ActionIntroActivity(6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCustomKeyboardVisible() {
        if (isPinCode() && this.type != 0 && !AndroidUtilities.isTablet()) {
            Point point = AndroidUtilities.displaySize;
            if (point.x < point.y && !AndroidUtilities.isAccessibilityTouchExplorationEnabled()) {
                return true;
            }
        }
        return false;
    }

    private boolean isPassword() {
        int i = this.type;
        if (i == 1 && this.currentPasswordType == 1) {
            return true;
        }
        return i == 2 && SharedConfig.passcodeType == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPinCode() {
        int i = this.type;
        if (i == 1 && this.currentPasswordType == 0) {
            return true;
        }
        return i == 2 && SharedConfig.passcodeType == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateSuccessAnimation$16(Runnable runnable) {
        for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
            codeNumberField.animateSuccessProgress(0.0f);
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(int i, boolean z) {
        Runnable runnable;
        if (i < AndroidUtilities.dp(20.0f) || (runnable = this.onShowKeyboardCallback) == null) {
            return;
        }
        runnable.run();
        this.onShowKeyboardCallback = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$10(TextView textView, int i, KeyEvent keyEvent) {
        int i2 = this.passcodeSetStep;
        if (i2 == 0) {
            processNext();
            return true;
        }
        if (i2 != 1) {
            return false;
        }
        processDone();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$11(CodeNumberField codeNumberField, View view, boolean z) {
        this.keyboardView.setEditText(codeNumberField);
        this.keyboardView.setDispatchBackWhenEmpty(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$12(View view) {
        int i = this.type;
        if (i == 1) {
            if (this.passcodeSetStep == 0) {
                processNext();
                return;
            }
        } else if (i != 2) {
            return;
        }
        processDone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(DialogInterface dialogInterface, int i) {
        SharedConfig.passcodeHash = "";
        SharedConfig.appLocked = false;
        SharedConfig.saveConfig();
        getMediaDataController().buildShortcuts();
        int childCount = this.listView.getChildCount();
        int i2 = 0;
        while (true) {
            if (i2 >= childCount) {
                break;
            }
            View childAt = this.listView.getChildAt(i2);
            if (childAt instanceof TextSettingsCell) {
                ((TextSettingsCell) childAt).setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText7));
                break;
            }
            i2++;
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetPasscode, new Object[0]);
        lambda$onBackPressed$321();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createView$3(int i) {
        return i == 0 ? LocaleController.getString(R.string.AutoLockDisabled) : i == 1 ? LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Minutes", 1, new Object[0])) : i == 2 ? LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Minutes", 5, new Object[0])) : i == 3 ? LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Hours", 1, new Object[0])) : i == 4 ? LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Hours", 5, new Object[0])) : "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(NumberPicker numberPicker, int i, DialogInterface dialogInterface, int i2) {
        int i3;
        int value = numberPicker.getValue();
        if (value == 0) {
            SharedConfig.autoLockIn = 0;
        } else {
            if (value == 1) {
                i3 = 60;
            } else if (value == 2) {
                i3 = NotificationCenter.customTypefacesLoaded;
            } else if (value == 3) {
                i3 = 3600;
            } else if (value == 4) {
                i3 = 18000;
            }
            SharedConfig.autoLockIn = i3;
        }
        this.listAdapter.notifyItemChanged(i);
        UserConfig.getInstance(this.currentAccount).saveConfig(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(View view, final int i) {
        int i2 = 0;
        if (view.isEnabled()) {
            if (i == this.disablePasscodeRow) {
                AlertDialog create = new AlertDialog.Builder(getParentActivity()).setTitle(LocaleController.getString(R.string.DisablePasscode)).setMessage(LocaleController.getString(R.string.DisablePasscodeConfirmMessage)).setNegativeButton(LocaleController.getString(R.string.Cancel), null).setPositiveButton(LocaleController.getString(R.string.DisablePasscodeTurnOff), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda16
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i3) {
                        PasscodeActivity.this.lambda$createView$2(dialogInterface, i3);
                    }
                }).create();
                create.show();
                ((TextView) create.getButton(-1)).setTextColor(Theme.getColor(Theme.key_text_RedBold));
                return;
            }
            if (i == this.changePasscodeRow) {
                presentFragment(new PasscodeActivity(1));
                return;
            }
            if (i != this.autoLockRow) {
                if (i == this.fingerprintRow) {
                    SharedConfig.useFingerprintLock = !SharedConfig.useFingerprintLock;
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                    ((TextCheckCell) view).setChecked(SharedConfig.useFingerprintLock);
                    return;
                } else {
                    if (i == this.captureRow) {
                        SharedConfig.allowScreenCapture = !SharedConfig.allowScreenCapture;
                        UserConfig.getInstance(this.currentAccount).saveConfig(false);
                        ((TextCheckCell) view).setChecked(SharedConfig.allowScreenCapture);
                        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetPasscode, Boolean.FALSE);
                        if (SharedConfig.allowScreenCapture) {
                            return;
                        }
                        AlertsCreator.showSimpleAlert(this, LocaleController.getString(R.string.ScreenCaptureAlert));
                        return;
                    }
                    return;
                }
            }
            if (getParentActivity() == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString(R.string.AutoLock));
            final NumberPicker numberPicker = new NumberPicker(getParentActivity());
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(4);
            int i3 = SharedConfig.autoLockIn;
            if (i3 != 0) {
                if (i3 == 60) {
                    numberPicker.setValue(1);
                } else if (i3 == 300) {
                    i2 = 2;
                } else if (i3 == 3600) {
                    i2 = 3;
                } else if (i3 == 18000) {
                    numberPicker.setValue(4);
                }
                numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda17
                    @Override // org.telegram.ui.Components.NumberPicker.Formatter
                    public final String format(int i4) {
                        String lambda$createView$3;
                        lambda$createView$3 = PasscodeActivity.lambda$createView$3(i4);
                        return lambda$createView$3;
                    }
                });
                builder.setView(numberPicker);
                builder.setNegativeButton(LocaleController.getString(R.string.Done), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda18
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        PasscodeActivity.this.lambda$createView$4(numberPicker, i, dialogInterface, i4);
                    }
                });
                showDialog(builder.create());
            }
            numberPicker.setValue(i2);
            numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda17
                @Override // org.telegram.ui.Components.NumberPicker.Formatter
                public final String format(int i4) {
                    String lambda$createView$3;
                    lambda$createView$3 = PasscodeActivity.lambda$createView$3(i4);
                    return lambda$createView$3;
                }
            });
            builder.setView(numberPicker);
            builder.setNegativeButton(LocaleController.getString(R.string.Done), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda18
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i4) {
                    PasscodeActivity.this.lambda$createView$4(numberPicker, i, dialogInterface, i4);
                }
            });
            showDialog(builder.create());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ View lambda$createView$6(Context context) {
        TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText6));
        textView.setGravity(1);
        textView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        textView.setTextSize(1, 15.0f);
        return textView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$7(Context context, View view) {
        AlertsCreator.createForgotPasscodeDialog(context).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(View view, boolean z) {
        this.outlinePasswordView.animateSelection(z ? 1.0f : 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(AtomicBoolean atomicBoolean, View view) {
        atomicBoolean.set(!atomicBoolean.get());
        int selectionStart = this.passwordEditText.getSelectionStart();
        int selectionEnd = this.passwordEditText.getSelectionEnd();
        this.passwordEditText.setInputType((atomicBoolean.get() ? NotificationCenter.messagePlayingProgressDidChanged : 128) | 1);
        this.passwordEditText.setSelection(selectionStart, selectionEnd);
        this.passwordButton.setColorFilter(Theme.getColor(atomicBoolean.get() ? Theme.key_windowBackgroundWhiteInputFieldActivated : Theme.key_windowBackgroundWhiteHintText));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.postedHidePasscodesDoNotMatch = false;
        AndroidUtilities.updateViewVisibilityAnimated(this.passcodesDoNotMatchTextView, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPasscodeError$21() {
        if (!isPinCode()) {
            this.outlinePasswordView.animateError(0.0f);
            return;
        }
        for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
            codeNumberField.animateErrorProgress(0.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPasscodeError$22() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                PasscodeActivity.this.lambda$onPasscodeError$21();
            }
        }, isPinCode() ? 150L : 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$18() {
        this.codeFieldContainer.postDelayed(this.hidePasscodesDoNotMatch, 3000L);
        this.postedHidePasscodesDoNotMatch = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$19(boolean z) {
        getMediaDataController().buildShortcuts();
        if (z) {
            presentFragment(new PasscodeActivity(0), true);
        } else {
            lambda$onBackPressed$321();
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetPasscode, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$20() {
        presentFragment(new PasscodeActivity(0), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCustomKeyboardVisible$13(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.keyboardView.setAlpha(floatValue);
        this.keyboardView.setTranslationY((1.0f - floatValue) * AndroidUtilities.dp(230.0f) * 0.75f);
        this.fragmentView.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setFloatingButtonVisible$14(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.floatingAutoAnimator.setOffsetY(AndroidUtilities.dp(70.0f) * (1.0f - floatValue));
        this.floatingButtonContainer.setAlpha(floatValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateFields$17(boolean z, boolean z2) {
        setFloatingButtonVisible(z, z2);
        AndroidUtilities.cancelRunOnUIThread(this.onShowKeyboardCallback);
    }

    private void onPasscodeError() {
        if (getParentActivity() == null) {
            return;
        }
        try {
            this.fragmentView.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        if (isPinCode()) {
            for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                codeNumberField.animateErrorProgress(1.0f);
            }
        } else {
            this.outlinePasswordView.animateError(1.0f);
        }
        AndroidUtilities.shakeViewSpring(isPinCode() ? this.codeFieldContainer : this.outlinePasswordView, isPinCode() ? 10.0f : 4.0f, new Runnable() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                PasscodeActivity.this.lambda$onPasscodeError$22();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDone() {
        Runnable runnable;
        int i = 0;
        if (isPassword() && this.passwordEditText.getText().length() == 0) {
            onPasscodeError();
            return;
        }
        String code = isPinCode() ? this.codeFieldContainer.getCode() : this.passwordEditText.getText().toString();
        int i2 = this.type;
        if (i2 == 1) {
            if (!this.firstPassword.equals(code)) {
                AndroidUtilities.updateViewVisibilityAnimated(this.passcodesDoNotMatchTextView, true);
                for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                    codeNumberField.setText("");
                }
                if (isPinCode()) {
                    this.codeFieldContainer.codeField[0].requestFocus();
                }
                this.passwordEditText.setText("");
                onPasscodeError();
                this.codeFieldContainer.removeCallbacks(this.hidePasscodesDoNotMatch);
                this.codeFieldContainer.post(new Runnable() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        PasscodeActivity.this.lambda$processDone$18();
                    }
                });
                return;
            }
            final boolean z = SharedConfig.passcodeHash.length() == 0;
            try {
                SharedConfig.passcodeSalt = new byte[16];
                Utilities.random.nextBytes(SharedConfig.passcodeSalt);
                byte[] bytes = this.firstPassword.getBytes("UTF-8");
                int length = bytes.length + 32;
                byte[] bArr = new byte[length];
                System.arraycopy(SharedConfig.passcodeSalt, 0, bArr, 0, 16);
                System.arraycopy(bytes, 0, bArr, 16, bytes.length);
                System.arraycopy(SharedConfig.passcodeSalt, 0, bArr, bytes.length + 16, 16);
                SharedConfig.passcodeHash = Utilities.bytesToHex(Utilities.computeSHA256(bArr, 0, length));
            } catch (Exception e) {
                FileLog.e(e);
            }
            SharedConfig.allowScreenCapture = true;
            SharedConfig.passcodeType = this.currentPasswordType;
            SharedConfig.saveConfig();
            this.passwordEditText.clearFocus();
            AndroidUtilities.hideKeyboard(this.passwordEditText);
            CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
            int length2 = codeNumberFieldArr.length;
            while (i < length2) {
                CodeNumberField codeNumberField2 = codeNumberFieldArr[i];
                codeNumberField2.clearFocus();
                AndroidUtilities.hideKeyboard(codeNumberField2);
                i++;
            }
            this.keyboardView.setEditText(null);
            runnable = new Runnable() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    PasscodeActivity.this.lambda$processDone$19(z);
                }
            };
        } else {
            if (i2 != 2) {
                return;
            }
            long j = SharedConfig.passcodeRetryInMs;
            if (j > 0) {
                double d = j;
                Double.isNaN(d);
                Toast.makeText(getParentActivity(), LocaleController.formatString("TooManyTries", R.string.TooManyTries, LocaleController.formatPluralString("Seconds", Math.max(1, (int) Math.ceil(d / 1000.0d)), new Object[0])), 0).show();
                for (CodeNumberField codeNumberField3 : this.codeFieldContainer.codeField) {
                    codeNumberField3.setText("");
                }
                this.passwordEditText.setText("");
                if (isPinCode()) {
                    this.codeFieldContainer.codeField[0].requestFocus();
                }
                onPasscodeError();
                return;
            }
            if (!SharedConfig.checkPasscode(code)) {
                SharedConfig.increaseBadPasscodeTries();
                this.passwordEditText.setText("");
                for (CodeNumberField codeNumberField4 : this.codeFieldContainer.codeField) {
                    codeNumberField4.setText("");
                }
                if (isPinCode()) {
                    this.codeFieldContainer.codeField[0].requestFocus();
                }
                onPasscodeError();
                return;
            }
            SharedConfig.badPasscodeTries = 0;
            SharedConfig.saveConfig();
            this.passwordEditText.clearFocus();
            AndroidUtilities.hideKeyboard(this.passwordEditText);
            CodeNumberField[] codeNumberFieldArr2 = this.codeFieldContainer.codeField;
            int length3 = codeNumberFieldArr2.length;
            while (i < length3) {
                CodeNumberField codeNumberField5 = codeNumberFieldArr2[i];
                codeNumberField5.clearFocus();
                AndroidUtilities.hideKeyboard(codeNumberField5);
                i++;
            }
            this.keyboardView.setEditText(null);
            runnable = new Runnable() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    PasscodeActivity.this.lambda$processDone$20();
                }
            };
        }
        animateSuccessAnimation(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNext() {
        if ((this.currentPasswordType == 1 && this.passwordEditText.getText().length() == 0) || (this.currentPasswordType == 0 && this.codeFieldContainer.getCode().length() != 4)) {
            onPasscodeError();
            return;
        }
        ActionBarMenuItem actionBarMenuItem = this.otherItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.setVisibility(8);
        }
        this.titleTextView.setText(LocaleController.getString(R.string.ConfirmCreatePasscode));
        this.descriptionTextSwitcher.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.PasscodeReinstallNotice)));
        this.firstPassword = isPinCode() ? this.codeFieldContainer.getCode() : this.passwordEditText.getText().toString();
        this.passwordEditText.setText("");
        this.passwordEditText.setInputType(524417);
        for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
            codeNumberField.setText("");
        }
        showKeyboard();
        this.passcodeSetStep = 1;
    }

    private void setCustomKeyboardVisible(final boolean z, boolean z2) {
        if (z) {
            AndroidUtilities.hideKeyboard(this.fragmentView);
            AndroidUtilities.requestAltFocusable(getParentActivity(), this.classGuid);
        } else {
            AndroidUtilities.removeAltFocusable(getParentActivity(), this.classGuid);
        }
        if (!z2) {
            this.keyboardView.setVisibility(z ? 0 : 8);
            this.keyboardView.setAlpha(z ? 1.0f : 0.0f);
            this.keyboardView.setTranslationY(z ? 0.0f : AndroidUtilities.dp(230.0f));
            this.fragmentView.requestLayout();
            return;
        }
        ValueAnimator duration = ValueAnimator.ofFloat(z ? 0.0f : 1.0f, z ? 1.0f : 0.0f).setDuration(150L);
        duration.setInterpolator(z ? CubicBezierInterpolator.DEFAULT : Easings.easeInOutQuad);
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda10
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PasscodeActivity.this.lambda$setCustomKeyboardVisible$13(valueAnimator);
            }
        });
        duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.PasscodeActivity.11
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (z) {
                    return;
                }
                PasscodeActivity.this.keyboardView.setVisibility(8);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                if (z) {
                    PasscodeActivity.this.keyboardView.setVisibility(0);
                }
            }
        });
        duration.start();
    }

    private void setFloatingButtonVisible(final boolean z, boolean z2) {
        Animator animator = this.floatingButtonAnimator;
        if (animator != null) {
            animator.cancel();
            this.floatingButtonAnimator = null;
        }
        if (!z2) {
            this.floatingAutoAnimator.setOffsetY(z ? 0.0f : AndroidUtilities.dp(70.0f));
            this.floatingButtonContainer.setAlpha(z ? 1.0f : 0.0f);
            this.floatingButtonContainer.setVisibility(z ? 0 : 8);
        } else {
            ValueAnimator duration = ValueAnimator.ofFloat(z ? 0.0f : 1.0f, z ? 1.0f : 0.0f).setDuration(150L);
            duration.setInterpolator(z ? AndroidUtilities.decelerateInterpolator : AndroidUtilities.accelerateInterpolator);
            duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda20
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    PasscodeActivity.this.lambda$setFloatingButtonVisible$14(valueAnimator);
                }
            });
            duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.PasscodeActivity.12
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator2) {
                    if (!z) {
                        PasscodeActivity.this.floatingButtonContainer.setVisibility(8);
                    }
                    if (PasscodeActivity.this.floatingButtonAnimator == animator2) {
                        PasscodeActivity.this.floatingButtonAnimator = null;
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator2) {
                    if (z) {
                        PasscodeActivity.this.floatingButtonContainer.setVisibility(0);
                    }
                }
            });
            duration.start();
            this.floatingButtonAnimator = duration;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showKeyboard() {
        EditTextBoldCursor editTextBoldCursor;
        if (isPinCode()) {
            this.codeFieldContainer.codeField[0].requestFocus();
            if (isCustomKeyboardVisible()) {
                return;
            } else {
                editTextBoldCursor = this.codeFieldContainer.codeField[0];
            }
        } else {
            if (!isPassword()) {
                return;
            }
            this.passwordEditText.requestFocus();
            editTextBoldCursor = this.passwordEditText;
        }
        AndroidUtilities.showKeyboard(editTextBoldCursor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0075  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x005d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateFields() {
        String charSequence;
        int i;
        TextViewSwitcher textViewSwitcher;
        int i2;
        final boolean isPassword;
        if (this.type == 2) {
            i = R.string.EnterYourPasscodeInfo;
        } else {
            if (this.passcodeSetStep != 0) {
                charSequence = this.descriptionTextSwitcher.getCurrentView().getText().toString();
                final boolean z = this.descriptionTextSwitcher.getCurrentView().getText().equals(charSequence) && !TextUtils.isEmpty(this.descriptionTextSwitcher.getCurrentView().getText());
                if (this.type == 2) {
                    if (this.passcodeSetStep == 0) {
                        textViewSwitcher = this.descriptionTextSwitcher;
                        i2 = this.currentPasswordType == 0 ? R.string.CreatePasscodeInfoPIN : R.string.CreatePasscodeInfoPassword;
                    }
                    if (isPinCode()) {
                        AndroidUtilities.updateViewVisibilityAnimated(this.codeFieldContainer, true, 1.0f, z);
                        AndroidUtilities.updateViewVisibilityAnimated(this.outlinePasswordView, false, 1.0f, z);
                    } else if (isPassword()) {
                        AndroidUtilities.updateViewVisibilityAnimated(this.codeFieldContainer, false, 1.0f, z);
                        AndroidUtilities.updateViewVisibilityAnimated(this.outlinePasswordView, true, 1.0f, z);
                    }
                    isPassword = isPassword();
                    if (isPassword) {
                        Runnable runnable = new Runnable() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda19
                            @Override // java.lang.Runnable
                            public final void run() {
                                PasscodeActivity.this.lambda$updateFields$17(isPassword, z);
                            }
                        };
                        this.onShowKeyboardCallback = runnable;
                        AndroidUtilities.runOnUIThread(runnable, 3000L);
                    } else {
                        setFloatingButtonVisible(isPassword, z);
                    }
                    setCustomKeyboardVisible(isCustomKeyboardVisible(), z);
                    showKeyboard();
                }
                textViewSwitcher = this.descriptionTextSwitcher;
                i2 = R.string.EnterYourPasscodeInfo;
                textViewSwitcher.setText(LocaleController.getString(i2), z);
                if (isPinCode()) {
                }
                isPassword = isPassword();
                if (isPassword) {
                }
                setCustomKeyboardVisible(isCustomKeyboardVisible(), z);
                showKeyboard();
            }
            i = this.currentPasswordType == 0 ? R.string.CreatePasscodeInfoPIN : R.string.CreatePasscodeInfoPassword;
        }
        charSequence = LocaleController.getString(i);
        if (this.descriptionTextSwitcher.getCurrentView().getText().equals(charSequence)) {
        }
        if (this.type == 2) {
        }
        textViewSwitcher.setText(LocaleController.getString(i2), z);
        if (isPinCode()) {
        }
        isPassword = isPassword();
        if (isPassword) {
        }
        setCustomKeyboardVisible(isCustomKeyboardVisible(), z);
        showKeyboard();
    }

    private void updateRows() {
        this.fingerprintRow = -1;
        this.utyanRow = 0;
        this.hintRow = 1;
        this.rowCount = 3;
        this.changePasscodeRow = 2;
        try {
            if (Build.VERSION.SDK_INT >= 23 && BiometricManager.from(ApplicationLoader.applicationContext).canAuthenticate(15) == 0 && AndroidUtilities.isKeyguardSecure()) {
                int i = this.rowCount;
                this.rowCount = i + 1;
                this.fingerprintRow = i;
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
        int i2 = this.rowCount;
        this.autoLockRow = i2;
        this.autoLockDetailRow = i2 + 1;
        this.captureHeaderRow = i2 + 2;
        this.captureRow = i2 + 3;
        this.captureDetailRow = i2 + 4;
        this.rowCount = i2 + 6;
        this.disablePasscodeRow = i2 + 5;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x020b  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x02a9  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x02b8  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x03de A[LOOP:0: B:53:0x03dc->B:54:0x03de, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0434  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0448  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x04b8  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x04bf  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0513  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x051a  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x053c  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x051d  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0516  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x04c2  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x04bb  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x02c0  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x02ab  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x020e  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01f3  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0162  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(final Context context) {
        final FrameLayout frameLayout;
        int i;
        TextView textView;
        int i2;
        int i3;
        ActionBarMenuSubItem actionBarMenuSubItem;
        ActionBar actionBar = this.actionBar;
        int i4 = R.drawable.ic_ab_back;
        actionBar.setBackButtonImage(i4);
        boolean z = false;
        this.actionBar.setAllowOverlayTitle(false);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.PasscodeActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i5) {
                if (i5 == -1) {
                    PasscodeActivity.this.lambda$onBackPressed$321();
                }
            }
        });
        FrameLayout frameLayout2 = new FrameLayout(context);
        if (this.type == 0) {
            frameLayout = frameLayout2;
        } else {
            ScrollView scrollView = new ScrollView(context);
            scrollView.addView(frameLayout2, LayoutHelper.createFrame(-1, -2.0f));
            scrollView.setFillViewport(true);
            frameLayout = scrollView;
        }
        SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.PasscodeActivity.2
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z2, int i5, int i6, int i7, int i8) {
                View view;
                int measuredWidth;
                int measuredHeight;
                if (PasscodeActivity.this.keyboardView.getVisibility() == 8 || measureKeyboardHeight() < AndroidUtilities.dp(20.0f)) {
                    if (PasscodeActivity.this.keyboardView.getVisibility() != 8) {
                        view = frameLayout;
                        measuredWidth = getMeasuredWidth();
                        measuredHeight = getMeasuredHeight() - AndroidUtilities.dp(230.0f);
                    }
                    view = frameLayout;
                    measuredWidth = getMeasuredWidth();
                    measuredHeight = getMeasuredHeight();
                } else {
                    if (PasscodeActivity.this.isCustomKeyboardVisible()) {
                        view = frameLayout;
                        measuredWidth = getMeasuredWidth();
                        measuredHeight = (getMeasuredHeight() - AndroidUtilities.dp(230.0f)) + measureKeyboardHeight();
                    }
                    view = frameLayout;
                    measuredWidth = getMeasuredWidth();
                    measuredHeight = getMeasuredHeight();
                }
                view.layout(0, 0, measuredWidth, measuredHeight);
                PasscodeActivity.this.keyboardView.layout(0, measuredHeight, getMeasuredWidth(), AndroidUtilities.dp(230.0f) + measuredHeight);
                notifyHeightChanged();
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i5, int i6) {
                int size = View.MeasureSpec.getSize(i5);
                int size2 = View.MeasureSpec.getSize(i6);
                setMeasuredDimension(size, size2);
                if (PasscodeActivity.this.keyboardView.getVisibility() != 8 && measureKeyboardHeight() < AndroidUtilities.dp(20.0f)) {
                    size2 -= AndroidUtilities.dp(230.0f);
                }
                frameLayout.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                PasscodeActivity.this.keyboardView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(230.0f), 1073741824));
            }
        };
        sizeNotifierFrameLayout.setDelegate(new SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate
            public final void onSizeChanged(int i5, boolean z2) {
                PasscodeActivity.this.lambda$createView$1(i5, z2);
            }
        });
        this.fragmentView = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.addView(frameLayout, LayoutHelper.createLinear(-1, 0, 1.0f));
        CustomPhoneKeyboardView customPhoneKeyboardView = new CustomPhoneKeyboardView(context);
        this.keyboardView = customPhoneKeyboardView;
        customPhoneKeyboardView.setVisibility(isCustomKeyboardVisible() ? 0 : 8);
        sizeNotifierFrameLayout.addView(this.keyboardView, LayoutHelper.createLinear(-1, NotificationCenter.didReceiveCall));
        int i5 = this.type;
        if (i5 == 0) {
            this.actionBar.setTitle(LocaleController.getString(R.string.Passcode));
            int i6 = Theme.key_windowBackgroundGray;
            frameLayout2.setTag(Integer.valueOf(i6));
            frameLayout2.setBackgroundColor(Theme.getColor(i6));
            RecyclerListView recyclerListView = new RecyclerListView(context);
            this.listView = recyclerListView;
            recyclerListView.setLayoutManager(new LinearLayoutManager(context, 1, z) { // from class: org.telegram.ui.PasscodeActivity.3
                @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            });
            this.listView.setVerticalScrollBarEnabled(false);
            this.listView.setItemAnimator(null);
            this.listView.setLayoutAnimation(null);
            frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
            RecyclerListView recyclerListView2 = this.listView;
            ListAdapter listAdapter = new ListAdapter(context);
            this.listAdapter = listAdapter;
            recyclerListView2.setAdapter(listAdapter);
            this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda2
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i7) {
                    PasscodeActivity.this.lambda$createView$5(view, i7);
                }
            });
        } else if (i5 == 1 || i5 == 2) {
            ActionBar actionBar2 = this.actionBar;
            if (actionBar2 != null) {
                actionBar2.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                this.actionBar.setBackButtonImage(i4);
                this.actionBar.setItemsColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText), false);
                this.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarWhiteSelector), false);
                this.actionBar.setCastShadows(false);
                ActionBarMenu createMenu = this.actionBar.createMenu();
                if (this.type == 1) {
                    ActionBarMenuItem addItem = createMenu.addItem(0, R.drawable.ic_ab_other);
                    this.otherItem = addItem;
                    actionBarMenuSubItem = addItem.addSubItem(1, R.drawable.msg_permissions, LocaleController.getString(R.string.PasscodeSwitchToPassword));
                } else {
                    actionBarMenuSubItem = null;
                }
                this.actionBar.setActionBarMenuOnItemClick(new 4(actionBarMenuSubItem));
            }
            FrameLayout frameLayout3 = new FrameLayout(context);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            linearLayout.setGravity(1);
            frameLayout2.addView(linearLayout, LayoutHelper.createFrame(-1, -1.0f));
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.lockImageView = rLottieImageView;
            rLottieImageView.setFocusable(false);
            this.lockImageView.setAnimation(R.raw.tsv_setup_intro, 120, 120);
            this.lockImageView.setAutoRepeat(false);
            this.lockImageView.playAnimation();
            RLottieImageView rLottieImageView2 = this.lockImageView;
            if (!AndroidUtilities.isSmallScreen()) {
                Point point = AndroidUtilities.displaySize;
                if (point.x < point.y) {
                    i = 0;
                    rLottieImageView2.setVisibility(i);
                    linearLayout.addView(this.lockImageView, LayoutHelper.createLinear(120, 120, 1));
                    TextView textView2 = new TextView(context);
                    this.titleTextView = textView2;
                    int i7 = Theme.key_windowBackgroundWhiteBlackText;
                    textView2.setTextColor(Theme.getColor(i7));
                    this.titleTextView.setTypeface(AndroidUtilities.bold());
                    if (this.type == 1) {
                        textView = this.titleTextView;
                        i2 = R.string.EnterYourPasscode;
                    } else if (SharedConfig.passcodeHash.length() != 0) {
                        textView = this.titleTextView;
                        i2 = R.string.EnterNewPasscode;
                    } else {
                        textView = this.titleTextView;
                        i2 = R.string.CreatePasscode;
                    }
                    textView.setText(LocaleController.getString(i2));
                    this.titleTextView.setTextSize(1, 18.0f);
                    this.titleTextView.setGravity(1);
                    linearLayout.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 0, 16, 0, 0));
                    TextViewSwitcher textViewSwitcher = new TextViewSwitcher(context);
                    this.descriptionTextSwitcher = textViewSwitcher;
                    textViewSwitcher.setFactory(new ViewSwitcher.ViewFactory() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda3
                        @Override // android.widget.ViewSwitcher.ViewFactory
                        public final View makeView() {
                            View lambda$createView$6;
                            lambda$createView$6 = PasscodeActivity.lambda$createView$6(context);
                            return lambda$createView$6;
                        }
                    });
                    this.descriptionTextSwitcher.setInAnimation(context, R.anim.alpha_in);
                    this.descriptionTextSwitcher.setOutAnimation(context, R.anim.alpha_out);
                    linearLayout.addView(this.descriptionTextSwitcher, LayoutHelper.createLinear(-2, -2, 1, 20, 8, 20, 0));
                    TextView textView3 = new TextView(context);
                    textView3.setTextSize(1, 14.0f);
                    textView3.setTextColor(Theme.getColor(Theme.key_featuredStickers_addButton));
                    textView3.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
                    textView3.setGravity((!isPassword() ? 3 : 1) | 16);
                    textView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PasscodeActivity.lambda$createView$7(context, view);
                        }
                    });
                    textView3.setVisibility(this.type != 2 ? 0 : 8);
                    textView3.setText(LocaleController.getString(R.string.ForgotPasscode));
                    frameLayout2.addView(textView3, LayoutHelper.createFrame(-1, Build.VERSION.SDK_INT < 21 ? 56.0f : 60.0f, 81, 0.0f, 0.0f, 0.0f, 16.0f));
                    VerticalPositionAutoAnimator.attach(textView3);
                    TextView textView4 = new TextView(context);
                    this.passcodesDoNotMatchTextView = textView4;
                    textView4.setTextSize(1, 14.0f);
                    this.passcodesDoNotMatchTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText6));
                    this.passcodesDoNotMatchTextView.setText(LocaleController.getString(R.string.PasscodesDoNotMatchTryAgain));
                    this.passcodesDoNotMatchTextView.setPadding(0, AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f));
                    AndroidUtilities.updateViewVisibilityAnimated(this.passcodesDoNotMatchTextView, false, 1.0f, false);
                    frameLayout2.addView(this.passcodesDoNotMatchTextView, LayoutHelper.createFrame(-2, -2.0f, 81, 0.0f, 0.0f, 0.0f, 16.0f));
                    OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
                    this.outlinePasswordView = outlineTextContainerView;
                    outlineTextContainerView.setText(LocaleController.getString(R.string.EnterPassword));
                    EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
                    this.passwordEditText = editTextBoldCursor;
                    editTextBoldCursor.setInputType(524417);
                    this.passwordEditText.setTextSize(1, 18.0f);
                    this.passwordEditText.setTextColor(Theme.getColor(i7));
                    this.passwordEditText.setBackground(null);
                    this.passwordEditText.setMaxLines(1);
                    this.passwordEditText.setLines(1);
                    this.passwordEditText.setGravity(!LocaleController.isRTL ? 5 : 3);
                    this.passwordEditText.setSingleLine(true);
                    if (this.type != 1) {
                        this.passcodeSetStep = 0;
                        this.passwordEditText.setImeOptions(5);
                    } else {
                        this.passcodeSetStep = 1;
                        this.passwordEditText.setImeOptions(6);
                    }
                    this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    this.passwordEditText.setTypeface(Typeface.DEFAULT);
                    this.passwordEditText.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated));
                    this.passwordEditText.setCursorSize(AndroidUtilities.dp(20.0f));
                    this.passwordEditText.setCursorWidth(1.5f);
                    int dp = AndroidUtilities.dp(16.0f);
                    this.passwordEditText.setPadding(dp, dp, dp, dp);
                    this.passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda5
                        @Override // android.view.View.OnFocusChangeListener
                        public final void onFocusChange(View view, boolean z2) {
                            PasscodeActivity.this.lambda$createView$8(view, z2);
                        }
                    });
                    LinearLayout linearLayout2 = new LinearLayout(context);
                    linearLayout2.setOrientation(0);
                    linearLayout2.setGravity(16);
                    linearLayout2.addView(this.passwordEditText, LayoutHelper.createLinear(0, -2, 1.0f));
                    ImageView imageView = new ImageView(context);
                    this.passwordButton = imageView;
                    imageView.setImageResource(R.drawable.msg_message);
                    this.passwordButton.setColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
                    this.passwordButton.setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector), 1));
                    AndroidUtilities.updateViewVisibilityAnimated(this.passwordButton, this.type != 1 && this.passcodeSetStep == 0, 0.1f, false);
                    final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                    this.passwordEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PasscodeActivity.5
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            if (PasscodeActivity.this.type == 1 && PasscodeActivity.this.passcodeSetStep == 0) {
                                if (TextUtils.isEmpty(editable) && PasscodeActivity.this.passwordButton.getVisibility() != 8) {
                                    if (atomicBoolean.get()) {
                                        PasscodeActivity.this.passwordButton.callOnClick();
                                    }
                                    AndroidUtilities.updateViewVisibilityAnimated(PasscodeActivity.this.passwordButton, false, 0.1f, true);
                                } else {
                                    if (TextUtils.isEmpty(editable) || PasscodeActivity.this.passwordButton.getVisibility() == 0) {
                                        return;
                                    }
                                    AndroidUtilities.updateViewVisibilityAnimated(PasscodeActivity.this.passwordButton, true, 0.1f, true);
                                }
                            }
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
                        }
                    });
                    this.passwordButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda6
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PasscodeActivity.this.lambda$createView$9(atomicBoolean, view);
                        }
                    });
                    linearLayout2.addView(this.passwordButton, LayoutHelper.createLinearRelatively(24.0f, 24.0f, 0, 0.0f, 0.0f, 14.0f, 0.0f));
                    this.outlinePasswordView.addView(linearLayout2, LayoutHelper.createFrame(-1, -2.0f));
                    frameLayout3.addView(this.outlinePasswordView, LayoutHelper.createLinear(-1, -2, 1, 32, 0, 32, 0));
                    this.passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda7
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView5, int i8, KeyEvent keyEvent) {
                            boolean lambda$createView$10;
                            lambda$createView$10 = PasscodeActivity.this.lambda$createView$10(textView5, i8, keyEvent);
                            return lambda$createView$10;
                        }
                    });
                    this.passwordEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PasscodeActivity.6
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
                            if (PasscodeActivity.this.postedHidePasscodesDoNotMatch) {
                                PasscodeActivity.this.codeFieldContainer.removeCallbacks(PasscodeActivity.this.hidePasscodesDoNotMatch);
                                PasscodeActivity.this.hidePasscodesDoNotMatch.run();
                            }
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
                        }
                    });
                    this.passwordEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: org.telegram.ui.PasscodeActivity.7
                        @Override // android.view.ActionMode.Callback
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            return false;
                        }

                        @Override // android.view.ActionMode.Callback
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            return false;
                        }

                        @Override // android.view.ActionMode.Callback
                        public void onDestroyActionMode(ActionMode actionMode) {
                        }

                        @Override // android.view.ActionMode.Callback
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            return false;
                        }
                    });
                    8 r2 = new 8(context);
                    this.codeFieldContainer = r2;
                    r2.setNumbersCount(4, 10);
                    for (final CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                        codeNumberField.setShowSoftInputOnFocusCompat(!isCustomKeyboardVisible());
                        codeNumberField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        codeNumberField.setTextSize(1, 24.0f);
                        codeNumberField.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PasscodeActivity.9
                            @Override // android.text.TextWatcher
                            public void afterTextChanged(Editable editable) {
                            }

                            @Override // android.text.TextWatcher
                            public void beforeTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
                                if (PasscodeActivity.this.postedHidePasscodesDoNotMatch) {
                                    PasscodeActivity.this.codeFieldContainer.removeCallbacks(PasscodeActivity.this.hidePasscodesDoNotMatch);
                                    PasscodeActivity.this.hidePasscodesDoNotMatch.run();
                                }
                            }

                            @Override // android.text.TextWatcher
                            public void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
                            }
                        });
                        codeNumberField.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda8
                            @Override // android.view.View.OnFocusChangeListener
                            public final void onFocusChange(View view, boolean z2) {
                                PasscodeActivity.this.lambda$createView$11(codeNumberField, view, z2);
                            }
                        });
                    }
                    frameLayout3.addView(this.codeFieldContainer, LayoutHelper.createFrame(-2, -2.0f, 1, 40.0f, 10.0f, 40.0f, 0.0f));
                    linearLayout.addView(frameLayout3, LayoutHelper.createLinear(-1, -2, 1, 0, 32, 0, 72));
                    if (this.type == 1) {
                        frameLayout2.setTag(Integer.valueOf(Theme.key_windowBackgroundWhite));
                    }
                    this.floatingButtonContainer = new FrameLayout(context);
                    i3 = Build.VERSION.SDK_INT;
                    if (i3 >= 21) {
                        StateListAnimator stateListAnimator = new StateListAnimator();
                        stateListAnimator.addState(new int[]{android.R.attr.state_pressed}, ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                        this.floatingButtonContainer.setStateListAnimator(stateListAnimator);
                        this.floatingButtonContainer.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.PasscodeActivity.10
                            @Override // android.view.ViewOutlineProvider
                            public void getOutline(View view, Outline outline) {
                                outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                            }
                        });
                    }
                    this.floatingAutoAnimator = VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
                    frameLayout2.addView(this.floatingButtonContainer, LayoutHelper.createFrame(i3 < 21 ? 56 : 60, i3 < 21 ? 56.0f : 60.0f, 85, 0.0f, 0.0f, 24.0f, 16.0f));
                    this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PasscodeActivity.this.lambda$createView$12(view);
                        }
                    });
                    TransformableLoginButtonView transformableLoginButtonView = new TransformableLoginButtonView(context);
                    this.floatingButtonIcon = transformableLoginButtonView;
                    transformableLoginButtonView.setTransformType(1);
                    this.floatingButtonIcon.setProgress(0.0f);
                    this.floatingButtonIcon.setColor(Theme.getColor(Theme.key_chats_actionIcon));
                    this.floatingButtonIcon.setDrawBackground(false);
                    this.floatingButtonContainer.setContentDescription(LocaleController.getString(R.string.Next));
                    this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i3 < 21 ? 56 : 60, i3 < 21 ? 56.0f : 60.0f));
                    Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
                    if (i3 < 21) {
                        Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
                        mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                        CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
                        combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                        createSimpleSelectorCircleDrawable = combinedDrawable;
                    }
                    this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable);
                    updateFields();
                }
            }
            i = 8;
            rLottieImageView2.setVisibility(i);
            linearLayout.addView(this.lockImageView, LayoutHelper.createLinear(120, 120, 1));
            TextView textView22 = new TextView(context);
            this.titleTextView = textView22;
            int i72 = Theme.key_windowBackgroundWhiteBlackText;
            textView22.setTextColor(Theme.getColor(i72));
            this.titleTextView.setTypeface(AndroidUtilities.bold());
            if (this.type == 1) {
            }
            textView.setText(LocaleController.getString(i2));
            this.titleTextView.setTextSize(1, 18.0f);
            this.titleTextView.setGravity(1);
            linearLayout.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 0, 16, 0, 0));
            TextViewSwitcher textViewSwitcher2 = new TextViewSwitcher(context);
            this.descriptionTextSwitcher = textViewSwitcher2;
            textViewSwitcher2.setFactory(new ViewSwitcher.ViewFactory() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda3
                @Override // android.widget.ViewSwitcher.ViewFactory
                public final View makeView() {
                    View lambda$createView$6;
                    lambda$createView$6 = PasscodeActivity.lambda$createView$6(context);
                    return lambda$createView$6;
                }
            });
            this.descriptionTextSwitcher.setInAnimation(context, R.anim.alpha_in);
            this.descriptionTextSwitcher.setOutAnimation(context, R.anim.alpha_out);
            linearLayout.addView(this.descriptionTextSwitcher, LayoutHelper.createLinear(-2, -2, 1, 20, 8, 20, 0));
            TextView textView32 = new TextView(context);
            textView32.setTextSize(1, 14.0f);
            textView32.setTextColor(Theme.getColor(Theme.key_featuredStickers_addButton));
            textView32.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
            textView32.setGravity((!isPassword() ? 3 : 1) | 16);
            textView32.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PasscodeActivity.lambda$createView$7(context, view);
                }
            });
            textView32.setVisibility(this.type != 2 ? 0 : 8);
            textView32.setText(LocaleController.getString(R.string.ForgotPasscode));
            frameLayout2.addView(textView32, LayoutHelper.createFrame(-1, Build.VERSION.SDK_INT < 21 ? 56.0f : 60.0f, 81, 0.0f, 0.0f, 0.0f, 16.0f));
            VerticalPositionAutoAnimator.attach(textView32);
            TextView textView42 = new TextView(context);
            this.passcodesDoNotMatchTextView = textView42;
            textView42.setTextSize(1, 14.0f);
            this.passcodesDoNotMatchTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText6));
            this.passcodesDoNotMatchTextView.setText(LocaleController.getString(R.string.PasscodesDoNotMatchTryAgain));
            this.passcodesDoNotMatchTextView.setPadding(0, AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f));
            AndroidUtilities.updateViewVisibilityAnimated(this.passcodesDoNotMatchTextView, false, 1.0f, false);
            frameLayout2.addView(this.passcodesDoNotMatchTextView, LayoutHelper.createFrame(-2, -2.0f, 81, 0.0f, 0.0f, 0.0f, 16.0f));
            OutlineTextContainerView outlineTextContainerView2 = new OutlineTextContainerView(context);
            this.outlinePasswordView = outlineTextContainerView2;
            outlineTextContainerView2.setText(LocaleController.getString(R.string.EnterPassword));
            EditTextBoldCursor editTextBoldCursor2 = new EditTextBoldCursor(context);
            this.passwordEditText = editTextBoldCursor2;
            editTextBoldCursor2.setInputType(524417);
            this.passwordEditText.setTextSize(1, 18.0f);
            this.passwordEditText.setTextColor(Theme.getColor(i72));
            this.passwordEditText.setBackground(null);
            this.passwordEditText.setMaxLines(1);
            this.passwordEditText.setLines(1);
            this.passwordEditText.setGravity(!LocaleController.isRTL ? 5 : 3);
            this.passwordEditText.setSingleLine(true);
            if (this.type != 1) {
            }
            this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.passwordEditText.setTypeface(Typeface.DEFAULT);
            this.passwordEditText.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated));
            this.passwordEditText.setCursorSize(AndroidUtilities.dp(20.0f));
            this.passwordEditText.setCursorWidth(1.5f);
            int dp2 = AndroidUtilities.dp(16.0f);
            this.passwordEditText.setPadding(dp2, dp2, dp2, dp2);
            this.passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda5
                @Override // android.view.View.OnFocusChangeListener
                public final void onFocusChange(View view, boolean z2) {
                    PasscodeActivity.this.lambda$createView$8(view, z2);
                }
            });
            LinearLayout linearLayout22 = new LinearLayout(context);
            linearLayout22.setOrientation(0);
            linearLayout22.setGravity(16);
            linearLayout22.addView(this.passwordEditText, LayoutHelper.createLinear(0, -2, 1.0f));
            ImageView imageView2 = new ImageView(context);
            this.passwordButton = imageView2;
            imageView2.setImageResource(R.drawable.msg_message);
            this.passwordButton.setColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
            this.passwordButton.setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector), 1));
            AndroidUtilities.updateViewVisibilityAnimated(this.passwordButton, this.type != 1 && this.passcodeSetStep == 0, 0.1f, false);
            final AtomicBoolean atomicBoolean2 = new AtomicBoolean(false);
            this.passwordEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PasscodeActivity.5
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    if (PasscodeActivity.this.type == 1 && PasscodeActivity.this.passcodeSetStep == 0) {
                        if (TextUtils.isEmpty(editable) && PasscodeActivity.this.passwordButton.getVisibility() != 8) {
                            if (atomicBoolean2.get()) {
                                PasscodeActivity.this.passwordButton.callOnClick();
                            }
                            AndroidUtilities.updateViewVisibilityAnimated(PasscodeActivity.this.passwordButton, false, 0.1f, true);
                        } else {
                            if (TextUtils.isEmpty(editable) || PasscodeActivity.this.passwordButton.getVisibility() == 0) {
                                return;
                            }
                            AndroidUtilities.updateViewVisibilityAnimated(PasscodeActivity.this.passwordButton, true, 0.1f, true);
                        }
                    }
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
                }
            });
            this.passwordButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PasscodeActivity.this.lambda$createView$9(atomicBoolean2, view);
                }
            });
            linearLayout22.addView(this.passwordButton, LayoutHelper.createLinearRelatively(24.0f, 24.0f, 0, 0.0f, 0.0f, 14.0f, 0.0f));
            this.outlinePasswordView.addView(linearLayout22, LayoutHelper.createFrame(-1, -2.0f));
            frameLayout3.addView(this.outlinePasswordView, LayoutHelper.createLinear(-1, -2, 1, 32, 0, 32, 0));
            this.passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda7
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView5, int i8, KeyEvent keyEvent) {
                    boolean lambda$createView$10;
                    lambda$createView$10 = PasscodeActivity.this.lambda$createView$10(textView5, i8, keyEvent);
                    return lambda$createView$10;
                }
            });
            this.passwordEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PasscodeActivity.6
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
                    if (PasscodeActivity.this.postedHidePasscodesDoNotMatch) {
                        PasscodeActivity.this.codeFieldContainer.removeCallbacks(PasscodeActivity.this.hidePasscodesDoNotMatch);
                        PasscodeActivity.this.hidePasscodesDoNotMatch.run();
                    }
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
                }
            });
            this.passwordEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: org.telegram.ui.PasscodeActivity.7
                @Override // android.view.ActionMode.Callback
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    return false;
                }

                @Override // android.view.ActionMode.Callback
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override // android.view.ActionMode.Callback
                public void onDestroyActionMode(ActionMode actionMode) {
                }

                @Override // android.view.ActionMode.Callback
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }
            });
            8 r22 = new 8(context);
            this.codeFieldContainer = r22;
            r22.setNumbersCount(4, 10);
            while (r9 < r5) {
            }
            frameLayout3.addView(this.codeFieldContainer, LayoutHelper.createFrame(-2, -2.0f, 1, 40.0f, 10.0f, 40.0f, 0.0f));
            linearLayout.addView(frameLayout3, LayoutHelper.createLinear(-1, -2, 1, 0, 32, 0, 72));
            if (this.type == 1) {
            }
            this.floatingButtonContainer = new FrameLayout(context);
            i3 = Build.VERSION.SDK_INT;
            if (i3 >= 21) {
            }
            this.floatingAutoAnimator = VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
            frameLayout2.addView(this.floatingButtonContainer, LayoutHelper.createFrame(i3 < 21 ? 56 : 60, i3 < 21 ? 56.0f : 60.0f, 85, 0.0f, 0.0f, 24.0f, 16.0f));
            this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PasscodeActivity.this.lambda$createView$12(view);
                }
            });
            TransformableLoginButtonView transformableLoginButtonView2 = new TransformableLoginButtonView(context);
            this.floatingButtonIcon = transformableLoginButtonView2;
            transformableLoginButtonView2.setTransformType(1);
            this.floatingButtonIcon.setProgress(0.0f);
            this.floatingButtonIcon.setColor(Theme.getColor(Theme.key_chats_actionIcon));
            this.floatingButtonIcon.setDrawBackground(false);
            this.floatingButtonContainer.setContentDescription(LocaleController.getString(R.string.Next));
            this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i3 < 21 ? 56 : 60, i3 < 21 ? 56.0f : 60.0f));
            Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
            if (i3 < 21) {
            }
            this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable2);
            updateFields();
        }
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.didSetPasscode) {
            if ((objArr.length == 0 || ((Boolean) objArr[0]).booleanValue()) && this.type == 0) {
                updateRows();
                ListAdapter listAdapter = this.listAdapter;
                if (listAdapter != null) {
                    listAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        int i = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextCheckCell.class, TextSettingsCell.class}, null, null, null, i));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, i));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i2 = ThemeDescription.FLAG_BACKGROUND;
        int i3 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i2, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, Theme.key_actionBarDefaultSubmenuBackground));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, Theme.key_actionBarDefaultSubmenuItem));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_actionBarDefaultSubmenuItemIcon));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6));
        EditTextBoldCursor editTextBoldCursor = this.passwordEditText;
        int i4 = ThemeDescription.FLAG_TEXTCOLOR;
        int i5 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(editTextBoldCursor, i4, null, null, null, null, i5));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrack));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackChecked));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText7));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean hasForceLightStatusBar() {
        return this.type != 0;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        CodeNumberField[] codeNumberFieldArr;
        int i;
        super.onConfigurationChanged(configuration);
        setCustomKeyboardVisible(isCustomKeyboardVisible(), false);
        RLottieImageView rLottieImageView = this.lockImageView;
        if (rLottieImageView != null) {
            if (!AndroidUtilities.isSmallScreen()) {
                Point point = AndroidUtilities.displaySize;
                if (point.x < point.y) {
                    i = 0;
                    rLottieImageView.setVisibility(i);
                }
            }
            i = 8;
            rLottieImageView.setVisibility(i);
        }
        CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
        if (codeFieldContainer == null || (codeNumberFieldArr = codeFieldContainer.codeField) == null) {
            return;
        }
        for (CodeNumberField codeNumberField : codeNumberFieldArr) {
            codeNumberField.setShowSoftInputOnFocusCompat(!isCustomKeyboardVisible());
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRows();
        if (this.type != 0) {
            return true;
        }
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (this.type == 0) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
        }
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        AndroidUtilities.removeAltFocusable(getParentActivity(), this.classGuid);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        if (this.type != 0 && !isCustomKeyboardVisible()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PasscodeActivity$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    PasscodeActivity.this.showKeyboard();
                }
            }, 200L);
        }
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        if (isCustomKeyboardVisible()) {
            AndroidUtilities.hideKeyboard(this.fragmentView);
            AndroidUtilities.requestAltFocusable(getParentActivity(), this.classGuid);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (!z || this.type == 0) {
            return;
        }
        showKeyboard();
    }
}
