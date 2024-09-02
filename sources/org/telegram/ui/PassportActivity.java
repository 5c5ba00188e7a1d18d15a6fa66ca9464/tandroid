package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Property;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.core.graphics.ColorUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.crypto.Cipher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MrzRecognizer;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.PushListenerController;
import org.telegram.messenger.R;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.SecureDocument;
import org.telegram.messenger.SecureDocumentKey;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputSecureFile;
import org.telegram.tgnet.TLRPC$PasswordKdfAlgo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$SecureFile;
import org.telegram.tgnet.TLRPC$SecurePasswordKdfAlgo;
import org.telegram.tgnet.TLRPC$SecurePlainData;
import org.telegram.tgnet.TLRPC$SecureRequiredType;
import org.telegram.tgnet.TLRPC$SecureValueError;
import org.telegram.tgnet.TLRPC$SecureValueType;
import org.telegram.tgnet.TLRPC$TL_account_acceptAuthorization;
import org.telegram.tgnet.TLRPC$TL_account_authorizationForm;
import org.telegram.tgnet.TLRPC$TL_account_deleteSecureValue;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_getPasswordSettings;
import org.telegram.tgnet.TLRPC$TL_account_password;
import org.telegram.tgnet.TLRPC$TL_account_passwordInputSettings;
import org.telegram.tgnet.TLRPC$TL_account_passwordSettings;
import org.telegram.tgnet.TLRPC$TL_account_saveSecureValue;
import org.telegram.tgnet.TLRPC$TL_account_sendVerifyEmailCode;
import org.telegram.tgnet.TLRPC$TL_account_sendVerifyPhoneCode;
import org.telegram.tgnet.TLRPC$TL_account_sentEmailCode;
import org.telegram.tgnet.TLRPC$TL_account_updatePasswordSettings;
import org.telegram.tgnet.TLRPC$TL_account_verifyEmail;
import org.telegram.tgnet.TLRPC$TL_account_verifyPhone;
import org.telegram.tgnet.TLRPC$TL_auth_cancelCode;
import org.telegram.tgnet.TLRPC$TL_auth_codeTypeCall;
import org.telegram.tgnet.TLRPC$TL_auth_codeTypeFlashCall;
import org.telegram.tgnet.TLRPC$TL_auth_codeTypeSms;
import org.telegram.tgnet.TLRPC$TL_auth_passwordRecovery;
import org.telegram.tgnet.TLRPC$TL_auth_requestPasswordRecovery;
import org.telegram.tgnet.TLRPC$TL_auth_resendCode;
import org.telegram.tgnet.TLRPC$TL_auth_sentCode;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeCall;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeFlashCall;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeSms;
import org.telegram.tgnet.TLRPC$TL_codeSettings;
import org.telegram.tgnet.TLRPC$TL_emailVerificationCode;
import org.telegram.tgnet.TLRPC$TL_emailVerifyPurposePassport;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_help_getPassportConfig;
import org.telegram.tgnet.TLRPC$TL_help_passportConfig;
import org.telegram.tgnet.TLRPC$TL_inputCheckPasswordSRP;
import org.telegram.tgnet.TLRPC$TL_inputFile;
import org.telegram.tgnet.TLRPC$TL_inputSecureFile;
import org.telegram.tgnet.TLRPC$TL_inputSecureFileUploaded;
import org.telegram.tgnet.TLRPC$TL_inputSecureValue;
import org.telegram.tgnet.TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow;
import org.telegram.tgnet.TLRPC$TL_secureCredentialsEncrypted;
import org.telegram.tgnet.TLRPC$TL_secureData;
import org.telegram.tgnet.TLRPC$TL_secureFile;
import org.telegram.tgnet.TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000;
import org.telegram.tgnet.TLRPC$TL_securePasswordKdfAlgoSHA512;
import org.telegram.tgnet.TLRPC$TL_securePasswordKdfAlgoUnknown;
import org.telegram.tgnet.TLRPC$TL_securePlainEmail;
import org.telegram.tgnet.TLRPC$TL_securePlainPhone;
import org.telegram.tgnet.TLRPC$TL_secureRequiredType;
import org.telegram.tgnet.TLRPC$TL_secureRequiredTypeOneOf;
import org.telegram.tgnet.TLRPC$TL_secureSecretSettings;
import org.telegram.tgnet.TLRPC$TL_secureValue;
import org.telegram.tgnet.TLRPC$TL_secureValueError;
import org.telegram.tgnet.TLRPC$TL_secureValueErrorData;
import org.telegram.tgnet.TLRPC$TL_secureValueErrorFile;
import org.telegram.tgnet.TLRPC$TL_secureValueErrorFiles;
import org.telegram.tgnet.TLRPC$TL_secureValueErrorFrontSide;
import org.telegram.tgnet.TLRPC$TL_secureValueErrorReverseSide;
import org.telegram.tgnet.TLRPC$TL_secureValueErrorSelfie;
import org.telegram.tgnet.TLRPC$TL_secureValueErrorTranslationFile;
import org.telegram.tgnet.TLRPC$TL_secureValueErrorTranslationFiles;
import org.telegram.tgnet.TLRPC$TL_secureValueHash;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeAddress;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeBankStatement;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeDriverLicense;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeEmail;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeIdentityCard;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeInternalPassport;
import org.telegram.tgnet.TLRPC$TL_secureValueTypePassport;
import org.telegram.tgnet.TLRPC$TL_secureValueTypePassportRegistration;
import org.telegram.tgnet.TLRPC$TL_secureValueTypePersonalDetails;
import org.telegram.tgnet.TLRPC$TL_secureValueTypePhone;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeRentalAgreement;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeTemporaryRegistration;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeUtilityBill;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$Vector;
import org.telegram.tgnet.TLRPC$account_Password;
import org.telegram.tgnet.TLRPC$auth_CodeType;
import org.telegram.tgnet.TLRPC$auth_SentCodeType;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.CameraScanActivity;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.HintEditText;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.Premium.boosts.BoostRepository$$ExternalSyntheticLambda31;
import org.telegram.ui.Components.RadialProgress;
import org.telegram.ui.Components.SlideView;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.CountrySelectActivity;
import org.telegram.ui.PassportActivity;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes4.dex */
public class PassportActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private TextView acceptTextView;
    private TextSettingsCell addDocumentCell;
    private ShadowSectionCell addDocumentSectionCell;
    private boolean allowNonLatinName;
    private ArrayList availableDocumentTypes;
    private TextInfoPrivacyCell bottomCell;
    private TextInfoPrivacyCell bottomCellTranslation;
    private FrameLayout bottomLayout;
    private boolean callbackCalled;
    private ChatAttachAlert chatAttachAlert;
    private HashMap codesMap;
    private ArrayList countriesArray;
    private HashMap countriesMap;
    private int currentActivityType;
    private long currentBotId;
    private String currentCallbackUrl;
    private String currentCitizeship;
    private HashMap currentDocumentValues;
    private TLRPC$TL_secureRequiredType currentDocumentsType;
    private TLRPC$TL_secureValue currentDocumentsTypeValue;
    private String currentEmail;
    private int[] currentExpireDate;
    private TLRPC$TL_account_authorizationForm currentForm;
    private String currentGender;
    private String currentNonce;
    private TLRPC$account_Password currentPassword;
    private String currentPayload;
    private TLRPC$TL_auth_sentCode currentPhoneVerification;
    private LinearLayout currentPhotoViewerLayout;
    private String currentPicturePath;
    private String currentPublicKey;
    private String currentResidence;
    private String currentScope;
    private TLRPC$TL_secureRequiredType currentType;
    private TLRPC$TL_secureValue currentTypeValue;
    private HashMap currentValues;
    private int currentViewNum;
    private PassportActivityDelegate delegate;
    private TextSettingsCell deletePassportCell;
    private ArrayList dividers;
    private boolean documentOnly;
    private ArrayList documents;
    private HashMap documentsCells;
    private HashMap documentsErrors;
    private LinearLayout documentsLayout;
    private HashMap documentsToTypesLink;
    private ActionBarMenuItem doneItem;
    private AnimatorSet doneItemAnimation;
    private int emailCodeLength;
    private ImageView emptyImageView;
    private LinearLayout emptyLayout;
    private TextView emptyTextView1;
    private TextView emptyTextView2;
    private TextView emptyTextView3;
    private EmptyTextProgressView emptyView;
    private HashMap errorsMap;
    private HashMap errorsValues;
    private View extraBackgroundView;
    private View extraBackgroundView2;
    private HashMap fieldsErrors;
    private SecureDocument frontDocument;
    private LinearLayout frontLayout;
    private HeaderCell headerCell;
    private boolean ignoreOnFailure;
    private boolean ignoreOnPhoneChange;
    private boolean ignoreOnTextChange;
    private String initialValues;
    private EditTextBoldCursor[] inputExtraFields;
    private ViewGroup[] inputFieldContainers;
    private EditTextBoldCursor[] inputFields;
    private HashMap languageMap;
    private LinearLayout linearLayout2;
    private HashMap mainErrorsMap;
    private TextInfoPrivacyCell nativeInfoCell;
    private boolean needActivityResult;
    private CharSequence noAllDocumentsErrorText;
    private CharSequence noAllTranslationErrorText;
    private ImageView noPasswordImageView;
    private TextView noPasswordSetTextView;
    private TextView noPasswordTextView;
    private boolean[] nonLatinNames;
    private FrameLayout passwordAvatarContainer;
    private TextView passwordForgotButton;
    private TextInfoPrivacyCell passwordInfoRequestTextView;
    private TextInfoPrivacyCell passwordRequestTextView;
    private PassportActivityDelegate pendingDelegate;
    private ErrorRunnable pendingErrorRunnable;
    private Runnable pendingFinishRunnable;
    private String pendingPhone;
    private Dialog permissionsDialog;
    private ArrayList permissionsItems;
    private HashMap phoneFormatMap;
    private TextView plusTextView;
    private PassportActivity presentAfterAnimation;
    private AlertDialog progressDialog;
    private ContextProgressView progressView;
    private ContextProgressView progressViewButton;
    private PhotoViewer.PhotoViewerProvider provider;
    private SecureDocument reverseDocument;
    private LinearLayout reverseLayout;
    private byte[] saltedPassword;
    private byte[] savedPasswordHash;
    private byte[] savedSaltedPassword;
    private TextSettingsCell scanDocumentCell;
    private int scrollHeight;
    private ScrollView scrollView;
    private ShadowSectionCell sectionCell;
    private ShadowSectionCell sectionCell2;
    private byte[] secureSecret;
    private long secureSecretId;
    private SecureDocument selfieDocument;
    private LinearLayout selfieLayout;
    private TextInfoPrivacyCell topErrorCell;
    private ArrayList translationDocuments;
    private LinearLayout translationLayout;
    private HashMap typesValues;
    private HashMap typesViews;
    private TextSettingsCell uploadDocumentCell;
    private TextDetailSettingsCell uploadFrontCell;
    private TextDetailSettingsCell uploadReverseCell;
    private TextDetailSettingsCell uploadSelfieCell;
    private TextSettingsCell uploadTranslationCell;
    private HashMap uploadingDocuments;
    private int uploadingFileType;
    private boolean useCurrentValue;
    private int usingSavedPassword;
    private SlideView[] views;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 1ValueToSend {
        boolean selfie_required;
        boolean translation_required;
        TLRPC$TL_secureValue value;

        public 1ValueToSend(TLRPC$TL_secureValue tLRPC$TL_secureValue, boolean z, boolean z2) {
            this.value = tLRPC$TL_secureValue;
            this.selfie_required = z;
            this.translation_required = z2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 3 extends ActionBar.ActionBarMenuOnItemClick {
        3() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onIdentityDone$0(String str, String str2, String str3, Runnable runnable, ErrorRunnable errorRunnable, DialogInterface dialogInterface, int i) {
            PassportActivity.this.inputFields[0].setText(str);
            PassportActivity.this.inputFields[1].setText(str2);
            PassportActivity.this.inputFields[2].setText(str3);
            PassportActivity.this.showEditDoneProgress(true, true);
            onIdentityDone(runnable, errorRunnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onIdentityDone$1(int i, DialogInterface dialogInterface, int i2) {
            PassportActivity passportActivity = PassportActivity.this;
            passportActivity.onFieldError(passportActivity.inputFields[i]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int lambda$onIdentityDone$2(String str, String str2) {
            int fieldCost = PassportActivity.this.getFieldCost(str);
            int fieldCost2 = PassportActivity.this.getFieldCost(str2);
            if (fieldCost < fieldCost2) {
                return -1;
            }
            return fieldCost > fieldCost2 ? 1 : 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int lambda$onIdentityDone$3(String str, String str2) {
            int fieldCost = PassportActivity.this.getFieldCost(str);
            int fieldCost2 = PassportActivity.this.getFieldCost(str2);
            if (fieldCost < fieldCost2) {
                return -1;
            }
            return fieldCost > fieldCost2 ? 1 : 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$4() {
            PassportActivity.this.finishFragment();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$5(TLRPC$TL_error tLRPC$TL_error, Runnable runnable, ErrorRunnable errorRunnable, TLRPC$TL_account_verifyEmail tLRPC$TL_account_verifyEmail) {
            if (tLRPC$TL_error == null) {
                PassportActivity.this.delegate.saveValue(PassportActivity.this.currentType, (String) PassportActivity.this.currentValues.get("email"), null, null, null, null, null, null, null, null, runnable, errorRunnable);
                return;
            }
            AlertsCreator.processError(((BaseFragment) PassportActivity.this).currentAccount, tLRPC$TL_error, PassportActivity.this, tLRPC$TL_account_verifyEmail, new Object[0]);
            errorRunnable.onError(null, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$6(final Runnable runnable, final ErrorRunnable errorRunnable, final TLRPC$TL_account_verifyEmail tLRPC$TL_account_verifyEmail, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$3$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.3.this.lambda$onItemClick$5(tLRPC$TL_error, runnable, errorRunnable, tLRPC$TL_account_verifyEmail);
                }
            });
        }

        /* JADX WARN: Removed duplicated region for block: B:84:0x035c  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x036d  */
        /* JADX WARN: Removed duplicated region for block: B:90:0x0384  */
        /* JADX WARN: Removed duplicated region for block: B:91:0x038a  */
        /* JADX WARN: Removed duplicated region for block: B:94:0x0393  */
        /* JADX WARN: Removed duplicated region for block: B:95:0x039a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private boolean onIdentityDone(final Runnable runnable, final ErrorRunnable errorRunnable) {
            JSONObject jSONObject;
            JSONObject jSONObject2;
            Editable text;
            String obj;
            String obj2;
            String obj3;
            int i = 3;
            if (!PassportActivity.this.uploadingDocuments.isEmpty() || PassportActivity.this.checkFieldsForError()) {
                return false;
            }
            if (PassportActivity.this.allowNonLatinName) {
                PassportActivity.this.allowNonLatinName = false;
                boolean z = false;
                final int i2 = 0;
                while (i2 < PassportActivity.this.nonLatinNames.length) {
                    if (PassportActivity.this.nonLatinNames[i2]) {
                        PassportActivity.this.inputFields[i2].setErrorText(LocaleController.getString(R.string.PassportUseLatinOnly));
                        if (!z) {
                            if (PassportActivity.this.nonLatinNames[0]) {
                                PassportActivity passportActivity = PassportActivity.this;
                                obj = passportActivity.getTranslitString(passportActivity.inputExtraFields[0].getText().toString());
                            } else {
                                obj = PassportActivity.this.inputFields[0].getText().toString();
                            }
                            final String str = obj;
                            if (PassportActivity.this.nonLatinNames[1]) {
                                PassportActivity passportActivity2 = PassportActivity.this;
                                obj2 = passportActivity2.getTranslitString(passportActivity2.inputExtraFields[1].getText().toString());
                            } else {
                                obj2 = PassportActivity.this.inputFields[1].getText().toString();
                            }
                            final String str2 = obj2;
                            if (PassportActivity.this.nonLatinNames[2]) {
                                PassportActivity passportActivity3 = PassportActivity.this;
                                obj3 = passportActivity3.getTranslitString(passportActivity3.inputExtraFields[2].getText().toString());
                            } else {
                                obj3 = PassportActivity.this.inputFields[2].getText().toString();
                            }
                            final String str3 = obj3;
                            if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
                                PassportActivity passportActivity4 = PassportActivity.this;
                                passportActivity4.onFieldError(passportActivity4.inputFields[i2]);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PassportActivity.this.getParentActivity());
                                int i3 = R.string.PassportNameCheckAlert;
                                Object[] objArr = new Object[i];
                                objArr[0] = str;
                                objArr[1] = str2;
                                objArr[2] = str3;
                                builder.setMessage(LocaleController.formatString("PassportNameCheckAlert", i3, objArr));
                                builder.setTitle(LocaleController.getString(R.string.AppName));
                                builder.setPositiveButton(LocaleController.getString(R.string.Done), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$3$$ExternalSyntheticLambda2
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i4) {
                                        PassportActivity.3.this.lambda$onIdentityDone$0(str, str2, str3, runnable, errorRunnable, dialogInterface, i4);
                                    }
                                });
                                builder.setNegativeButton(LocaleController.getString(R.string.Edit), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$3$$ExternalSyntheticLambda3
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i4) {
                                        PassportActivity.3.this.lambda$onIdentityDone$1(i2, dialogInterface, i4);
                                    }
                                });
                                PassportActivity.this.showDialog(builder.create());
                            }
                            z = true;
                        }
                    }
                    i2++;
                    i = 3;
                }
                if (z) {
                    return false;
                }
            }
            if (PassportActivity.this.isHasNotAnyChanges()) {
                PassportActivity.this.finishFragment();
                return false;
            }
            SecureDocument secureDocument = null;
            try {
                if (PassportActivity.this.documentOnly) {
                    jSONObject = null;
                } else {
                    HashMap hashMap = new HashMap(PassportActivity.this.currentValues);
                    if (PassportActivity.this.currentType.native_names) {
                        if (PassportActivity.this.nativeInfoCell.getVisibility() == 0) {
                            hashMap.put("first_name_native", PassportActivity.this.inputExtraFields[0].getText().toString());
                            hashMap.put("middle_name_native", PassportActivity.this.inputExtraFields[1].getText().toString());
                            text = PassportActivity.this.inputExtraFields[2].getText();
                        } else {
                            hashMap.put("first_name_native", PassportActivity.this.inputFields[0].getText().toString());
                            hashMap.put("middle_name_native", PassportActivity.this.inputFields[1].getText().toString());
                            text = PassportActivity.this.inputFields[2].getText();
                        }
                        hashMap.put("last_name_native", text.toString());
                    }
                    hashMap.put("first_name", PassportActivity.this.inputFields[0].getText().toString());
                    hashMap.put("middle_name", PassportActivity.this.inputFields[1].getText().toString());
                    hashMap.put("last_name", PassportActivity.this.inputFields[2].getText().toString());
                    hashMap.put("birth_date", PassportActivity.this.inputFields[3].getText().toString());
                    hashMap.put("gender", PassportActivity.this.currentGender);
                    hashMap.put("country_code", PassportActivity.this.currentCitizeship);
                    hashMap.put("residence_country_code", PassportActivity.this.currentResidence);
                    jSONObject = new JSONObject();
                    try {
                        ArrayList arrayList = new ArrayList(hashMap.keySet());
                        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.ui.PassportActivity$3$$ExternalSyntheticLambda4
                            @Override // java.util.Comparator
                            public final int compare(Object obj4, Object obj5) {
                                int lambda$onIdentityDone$2;
                                lambda$onIdentityDone$2 = PassportActivity.3.this.lambda$onIdentityDone$2((String) obj4, (String) obj5);
                                return lambda$onIdentityDone$2;
                            }
                        });
                        int size = arrayList.size();
                        for (int i4 = 0; i4 < size; i4++) {
                            String str4 = (String) arrayList.get(i4);
                            jSONObject.put(str4, hashMap.get(str4));
                        }
                    } catch (Exception unused) {
                    }
                }
            } catch (Exception unused2) {
                jSONObject = null;
            }
            if (PassportActivity.this.currentDocumentsType != null) {
                HashMap hashMap2 = new HashMap(PassportActivity.this.currentDocumentValues);
                hashMap2.put("document_no", PassportActivity.this.inputFields[7].getText().toString());
                hashMap2.put("expiry_date", PassportActivity.this.currentExpireDate[0] != 0 ? String.format(Locale.US, "%02d.%02d.%d", Integer.valueOf(PassportActivity.this.currentExpireDate[2]), Integer.valueOf(PassportActivity.this.currentExpireDate[1]), Integer.valueOf(PassportActivity.this.currentExpireDate[0])) : "");
                jSONObject2 = new JSONObject();
                try {
                    ArrayList arrayList2 = new ArrayList(hashMap2.keySet());
                    Collections.sort(arrayList2, new Comparator() { // from class: org.telegram.ui.PassportActivity$3$$ExternalSyntheticLambda5
                        @Override // java.util.Comparator
                        public final int compare(Object obj4, Object obj5) {
                            int lambda$onIdentityDone$3;
                            lambda$onIdentityDone$3 = PassportActivity.3.this.lambda$onIdentityDone$3((String) obj4, (String) obj5);
                            return lambda$onIdentityDone$3;
                        }
                    });
                    int size2 = arrayList2.size();
                    for (int i5 = 0; i5 < size2; i5++) {
                        String str5 = (String) arrayList2.get(i5);
                        jSONObject2.put(str5, hashMap2.get(str5));
                    }
                } catch (Exception unused3) {
                }
                if (PassportActivity.this.fieldsErrors != null) {
                    PassportActivity.this.fieldsErrors.clear();
                }
                if (PassportActivity.this.documentsErrors != null) {
                    PassportActivity.this.documentsErrors.clear();
                }
                PassportActivityDelegate passportActivityDelegate = PassportActivity.this.delegate;
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType = PassportActivity.this.currentType;
                String jSONObject3 = jSONObject == null ? jSONObject.toString() : null;
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2 = PassportActivity.this.currentDocumentsType;
                String jSONObject4 = jSONObject2 == null ? jSONObject2.toString() : null;
                SecureDocument secureDocument2 = PassportActivity.this.selfieDocument;
                ArrayList arrayList3 = PassportActivity.this.translationDocuments;
                SecureDocument secureDocument3 = PassportActivity.this.frontDocument;
                if (PassportActivity.this.reverseLayout != null && PassportActivity.this.reverseLayout.getVisibility() == 0) {
                    secureDocument = PassportActivity.this.reverseDocument;
                }
                passportActivityDelegate.saveValue(tLRPC$TL_secureRequiredType, null, jSONObject3, tLRPC$TL_secureRequiredType2, jSONObject4, null, secureDocument2, arrayList3, secureDocument3, secureDocument, runnable, errorRunnable);
                return true;
            }
            jSONObject2 = null;
            if (PassportActivity.this.fieldsErrors != null) {
            }
            if (PassportActivity.this.documentsErrors != null) {
            }
            PassportActivityDelegate passportActivityDelegate2 = PassportActivity.this.delegate;
            TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3 = PassportActivity.this.currentType;
            if (jSONObject == null) {
            }
            TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType22 = PassportActivity.this.currentDocumentsType;
            if (jSONObject2 == null) {
            }
            SecureDocument secureDocument22 = PassportActivity.this.selfieDocument;
            ArrayList arrayList32 = PassportActivity.this.translationDocuments;
            SecureDocument secureDocument32 = PassportActivity.this.frontDocument;
            if (PassportActivity.this.reverseLayout != null) {
                secureDocument = PassportActivity.this.reverseDocument;
            }
            passportActivityDelegate2.saveValue(tLRPC$TL_secureRequiredType3, null, jSONObject3, tLRPC$TL_secureRequiredType22, jSONObject4, null, secureDocument22, arrayList32, secureDocument32, secureDocument, runnable, errorRunnable);
            return true;
        }

        /* JADX WARN: Removed duplicated region for block: B:74:0x027e  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x028f  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x02a6  */
        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onItemClick(int i) {
            JSONObject jSONObject;
            TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType;
            String jSONObject2;
            TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2;
            ArrayList arrayList;
            SecureDocument secureDocument;
            ArrayList arrayList2;
            SecureDocument secureDocument2;
            SecureDocument secureDocument3;
            String str;
            String str2;
            PassportActivityDelegate passportActivityDelegate;
            String str3;
            if (i == -1) {
                if (PassportActivity.this.checkDiscard()) {
                    return;
                }
                if (PassportActivity.this.currentActivityType == 0 || PassportActivity.this.currentActivityType == 5) {
                    PassportActivity.this.callCallback(false);
                }
                PassportActivity.this.finishFragment();
                return;
            }
            if (i == 1) {
                if (PassportActivity.this.getParentActivity() == null) {
                    return;
                }
                LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(PassportActivity.this.getParentActivity());
                String string = LocaleController.getString(R.string.PassportInfo2);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
                int indexOf = string.indexOf(42);
                int lastIndexOf = string.lastIndexOf(42);
                if (indexOf != -1 && lastIndexOf != -1) {
                    spannableStringBuilder.replace(lastIndexOf, lastIndexOf + 1, (CharSequence) "");
                    spannableStringBuilder.replace(indexOf, indexOf + 1, (CharSequence) "");
                    spannableStringBuilder.setSpan(new URLSpanNoUnderline(LocaleController.getString(R.string.PassportInfoUrl)) { // from class: org.telegram.ui.PassportActivity.3.1
                        @Override // org.telegram.ui.Components.URLSpanNoUnderline, android.text.style.URLSpan, android.text.style.ClickableSpan
                        public void onClick(View view) {
                            PassportActivity.this.dismissCurrentDialog();
                            super.onClick(view);
                        }
                    }, indexOf, lastIndexOf - 1, 33);
                }
                linksTextView.setText(spannableStringBuilder);
                linksTextView.setTextSize(1, 16.0f);
                linksTextView.setLinkTextColor(Theme.getColor(Theme.key_dialogTextLink));
                linksTextView.setHighlightColor(Theme.getColor(Theme.key_dialogLinkSelection));
                linksTextView.setPadding(AndroidUtilities.dp(23.0f), 0, AndroidUtilities.dp(23.0f), 0);
                linksTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                linksTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                AlertDialog.Builder builder = new AlertDialog.Builder(PassportActivity.this.getParentActivity());
                builder.setView(linksTextView);
                builder.setTitle(LocaleController.getString(R.string.PassportInfoTitle));
                builder.setNegativeButton(LocaleController.getString(R.string.Close), null);
                PassportActivity.this.showDialog(builder.create());
            } else if (i == 2) {
                if (PassportActivity.this.currentActivityType == 5) {
                    PassportActivity.this.onPasswordDone(false);
                } else if (PassportActivity.this.currentActivityType == 7) {
                    PassportActivity.this.views[PassportActivity.this.currentViewNum].onNextPressed(null);
                } else {
                    final Runnable runnable = new Runnable() { // from class: org.telegram.ui.PassportActivity$3$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            PassportActivity.3.this.lambda$onItemClick$4();
                        }
                    };
                    final ErrorRunnable errorRunnable = new ErrorRunnable() { // from class: org.telegram.ui.PassportActivity.3.2
                        @Override // org.telegram.ui.PassportActivity.ErrorRunnable
                        public void onError(String str4, String str5) {
                            if (!"PHONE_VERIFICATION_NEEDED".equals(str4)) {
                                PassportActivity.this.showEditDoneProgress(true, false);
                                return;
                            }
                            PassportActivity passportActivity = PassportActivity.this;
                            passportActivity.startPhoneVerification(true, str5, runnable, this, passportActivity.delegate);
                        }
                    };
                    if (PassportActivity.this.currentActivityType == 4) {
                        if (PassportActivity.this.useCurrentValue) {
                            str3 = PassportActivity.this.currentEmail;
                        } else if (PassportActivity.this.checkFieldsForError()) {
                            return;
                        } else {
                            str3 = PassportActivity.this.inputFields[0].getText().toString();
                        }
                    } else if (PassportActivity.this.currentActivityType != 3) {
                        if (PassportActivity.this.currentActivityType != 2) {
                            if (PassportActivity.this.currentActivityType == 1) {
                                if (!onIdentityDone(runnable, errorRunnable)) {
                                    return;
                                }
                            } else if (PassportActivity.this.currentActivityType == 6) {
                                final TLRPC$TL_account_verifyEmail tLRPC$TL_account_verifyEmail = new TLRPC$TL_account_verifyEmail();
                                tLRPC$TL_account_verifyEmail.purpose = new TLRPC$TL_emailVerifyPurposePassport();
                                TLRPC$TL_emailVerificationCode tLRPC$TL_emailVerificationCode = new TLRPC$TL_emailVerificationCode();
                                tLRPC$TL_emailVerificationCode.code = PassportActivity.this.inputFields[0].getText().toString();
                                tLRPC$TL_account_verifyEmail.verification = tLRPC$TL_emailVerificationCode;
                                ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_verifyEmail, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$3$$ExternalSyntheticLambda1
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                        PassportActivity.3.this.lambda$onItemClick$6(runnable, errorRunnable, tLRPC$TL_account_verifyEmail, tLObject, tLRPC$TL_error);
                                    }
                                }), ((BaseFragment) PassportActivity.this).classGuid);
                            }
                            PassportActivity.this.showEditDoneProgress(true, true);
                        } else if (!PassportActivity.this.uploadingDocuments.isEmpty() || PassportActivity.this.checkFieldsForError()) {
                            return;
                        } else {
                            if (PassportActivity.this.isHasNotAnyChanges()) {
                                PassportActivity.this.finishFragment();
                                return;
                            }
                            if (!PassportActivity.this.documentOnly) {
                                jSONObject = new JSONObject();
                                try {
                                    jSONObject.put("street_line1", PassportActivity.this.inputFields[0].getText().toString());
                                    jSONObject.put("street_line2", PassportActivity.this.inputFields[1].getText().toString());
                                    jSONObject.put("post_code", PassportActivity.this.inputFields[2].getText().toString());
                                    jSONObject.put("city", PassportActivity.this.inputFields[3].getText().toString());
                                    jSONObject.put("state", PassportActivity.this.inputFields[4].getText().toString());
                                    jSONObject.put("country_code", PassportActivity.this.currentCitizeship);
                                } catch (Exception unused) {
                                }
                                if (PassportActivity.this.fieldsErrors != null) {
                                    PassportActivity.this.fieldsErrors.clear();
                                }
                                if (PassportActivity.this.documentsErrors != null) {
                                    PassportActivity.this.documentsErrors.clear();
                                }
                                PassportActivityDelegate passportActivityDelegate2 = PassportActivity.this.delegate;
                                tLRPC$TL_secureRequiredType = PassportActivity.this.currentType;
                                jSONObject2 = jSONObject != null ? jSONObject.toString() : null;
                                tLRPC$TL_secureRequiredType2 = PassportActivity.this.currentDocumentsType;
                                arrayList = PassportActivity.this.documents;
                                secureDocument = PassportActivity.this.selfieDocument;
                                arrayList2 = PassportActivity.this.translationDocuments;
                                secureDocument2 = null;
                                secureDocument3 = null;
                                str = null;
                                str2 = null;
                                passportActivityDelegate = passportActivityDelegate2;
                                passportActivityDelegate.saveValue(tLRPC$TL_secureRequiredType, str, jSONObject2, tLRPC$TL_secureRequiredType2, str2, arrayList, secureDocument, arrayList2, secureDocument2, secureDocument3, runnable, errorRunnable);
                                PassportActivity.this.showEditDoneProgress(true, true);
                            }
                            jSONObject = null;
                            if (PassportActivity.this.fieldsErrors != null) {
                            }
                            if (PassportActivity.this.documentsErrors != null) {
                            }
                            PassportActivityDelegate passportActivityDelegate22 = PassportActivity.this.delegate;
                            tLRPC$TL_secureRequiredType = PassportActivity.this.currentType;
                            jSONObject2 = jSONObject != null ? jSONObject.toString() : null;
                            tLRPC$TL_secureRequiredType2 = PassportActivity.this.currentDocumentsType;
                            arrayList = PassportActivity.this.documents;
                            secureDocument = PassportActivity.this.selfieDocument;
                            arrayList2 = PassportActivity.this.translationDocuments;
                            secureDocument2 = null;
                            secureDocument3 = null;
                            str = null;
                            str2 = null;
                            passportActivityDelegate = passportActivityDelegate22;
                            passportActivityDelegate.saveValue(tLRPC$TL_secureRequiredType, str, jSONObject2, tLRPC$TL_secureRequiredType2, str2, arrayList, secureDocument, arrayList2, secureDocument2, secureDocument3, runnable, errorRunnable);
                            PassportActivity.this.showEditDoneProgress(true, true);
                        }
                    } else if (PassportActivity.this.useCurrentValue) {
                        str3 = UserConfig.getInstance(((BaseFragment) PassportActivity.this).currentAccount).getCurrentUser().phone;
                    } else if (PassportActivity.this.checkFieldsForError()) {
                        return;
                    } else {
                        str3 = PassportActivity.this.inputFields[1].getText().toString() + PassportActivity.this.inputFields[2].getText().toString();
                    }
                    str = str3;
                    passportActivityDelegate = PassportActivity.this.delegate;
                    tLRPC$TL_secureRequiredType = PassportActivity.this.currentType;
                    secureDocument2 = null;
                    secureDocument3 = null;
                    jSONObject2 = null;
                    tLRPC$TL_secureRequiredType2 = null;
                    str2 = null;
                    arrayList = null;
                    secureDocument = null;
                    arrayList2 = null;
                    passportActivityDelegate.saveValue(tLRPC$TL_secureRequiredType, str, jSONObject2, tLRPC$TL_secureRequiredType2, str2, arrayList, secureDocument, arrayList2, secureDocument2, secureDocument3, runnable, errorRunnable);
                    PassportActivity.this.showEditDoneProgress(true, true);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 8 implements RequestDelegate {
        final /* synthetic */ TLRPC$TL_account_getPasswordSettings val$req;
        final /* synthetic */ boolean val$saved;
        final /* synthetic */ String val$textPassword;
        final /* synthetic */ byte[] val$x_bytes;

        8(boolean z, byte[] bArr, TLRPC$TL_account_getPasswordSettings tLRPC$TL_account_getPasswordSettings, String str) {
            this.val$saved = z;
            this.val$x_bytes = bArr;
            this.val$req = tLRPC$TL_account_getPasswordSettings;
            this.val$textPassword = str;
        }

        private void generateNewSecret() {
            DispatchQueue dispatchQueue = Utilities.globalQueue;
            final byte[] bArr = this.val$x_bytes;
            final String str = this.val$textPassword;
            dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.8.this.lambda$generateNewSecret$8(bArr, str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$generateNewSecret$4(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            if (tLRPC$TL_error == null) {
                PassportActivity.this.currentPassword = (TLRPC$account_Password) tLObject;
                TwoStepVerificationActivity.initPasswordNewAlgo(PassportActivity.this.currentPassword);
                generateNewSecret();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$generateNewSecret$5(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.8.this.lambda$generateNewSecret$4(tLRPC$TL_error, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$generateNewSecret$6(TLRPC$TL_error tLRPC$TL_error) {
            if (tLRPC$TL_error != null && "SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
                ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda5
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error2) {
                        PassportActivity.8.this.lambda$generateNewSecret$5(tLObject, tLRPC$TL_error2);
                    }
                }, 8);
                return;
            }
            if (PassportActivity.this.currentForm == null) {
                PassportActivity.this.currentForm = new TLRPC$TL_account_authorizationForm();
            }
            openRequestInterface();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$generateNewSecret$7(TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.8.this.lambda$generateNewSecret$6(tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$generateNewSecret$8(byte[] bArr, String str) {
            Utilities.random.setSeed(PassportActivity.this.currentPassword.secure_random);
            TLRPC$TL_account_updatePasswordSettings tLRPC$TL_account_updatePasswordSettings = new TLRPC$TL_account_updatePasswordSettings();
            if (PassportActivity.this.currentPassword.current_algo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
                tLRPC$TL_account_updatePasswordSettings.password = SRPHelper.startCheck(bArr, PassportActivity.this.currentPassword.srp_id, PassportActivity.this.currentPassword.srp_B, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) PassportActivity.this.currentPassword.current_algo);
            }
            tLRPC$TL_account_updatePasswordSettings.new_settings = new TLRPC$TL_account_passwordInputSettings();
            PassportActivity passportActivity = PassportActivity.this;
            passportActivity.secureSecret = passportActivity.getRandomSecret();
            PassportActivity passportActivity2 = PassportActivity.this;
            passportActivity2.secureSecretId = Utilities.bytesToLong(Utilities.computeSHA256(passportActivity2.secureSecret));
            if (PassportActivity.this.currentPassword.new_secure_algo instanceof TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
                TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 = (TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) PassportActivity.this.currentPassword.new_secure_algo;
                PassportActivity.this.saltedPassword = Utilities.computePBKDF2(AndroidUtilities.getStringBytes(str), tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt);
                byte[] bArr2 = new byte[32];
                System.arraycopy(PassportActivity.this.saltedPassword, 0, bArr2, 0, 32);
                byte[] bArr3 = new byte[16];
                System.arraycopy(PassportActivity.this.saltedPassword, 32, bArr3, 0, 16);
                Utilities.aesCbcEncryptionByteArraySafe(PassportActivity.this.secureSecret, bArr2, bArr3, 0, PassportActivity.this.secureSecret.length, 0, 1);
                tLRPC$TL_account_updatePasswordSettings.new_settings.new_secure_settings = new TLRPC$TL_secureSecretSettings();
                TLRPC$TL_secureSecretSettings tLRPC$TL_secureSecretSettings = tLRPC$TL_account_updatePasswordSettings.new_settings.new_secure_settings;
                tLRPC$TL_secureSecretSettings.secure_algo = tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000;
                tLRPC$TL_secureSecretSettings.secure_secret = PassportActivity.this.secureSecret;
                tLRPC$TL_account_updatePasswordSettings.new_settings.new_secure_settings.secure_secret_id = PassportActivity.this.secureSecretId;
                tLRPC$TL_account_updatePasswordSettings.new_settings.flags |= 4;
            }
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_updatePasswordSettings, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    PassportActivity.8.this.lambda$generateNewSecret$7(tLObject, tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resetSecret$0(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            if (tLRPC$TL_error == null) {
                PassportActivity.this.currentPassword = (TLRPC$account_Password) tLObject;
                TwoStepVerificationActivity.initPasswordNewAlgo(PassportActivity.this.currentPassword);
                resetSecret();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resetSecret$1(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.8.this.lambda$resetSecret$0(tLRPC$TL_error, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resetSecret$2(TLRPC$TL_error tLRPC$TL_error) {
            if (tLRPC$TL_error == null || !"SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
                generateNewSecret();
                return;
            }
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda3
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error2) {
                    PassportActivity.8.this.lambda$resetSecret$1(tLObject, tLRPC$TL_error2);
                }
            }, 8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resetSecret$3(TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.8.this.lambda$resetSecret$2(tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$10(final boolean z, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.8.this.lambda$run$9(tLRPC$TL_error, tLObject, z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$11() {
            AlertsCreator.showUpdateAppAlert(PassportActivity.this.getParentActivity(), LocaleController.getString(R.string.UpdateAppAlert), true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$12(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            if (tLObject == null) {
                if ("APP_VERSION_OUTDATED".equals(tLRPC$TL_error.text)) {
                    AlertsCreator.showUpdateAppAlert(PassportActivity.this.getParentActivity(), LocaleController.getString(R.string.UpdateAppAlert), true);
                } else {
                    PassportActivity.this.showAlertWithText(LocaleController.getString(R.string.AppName), tLRPC$TL_error.text);
                }
                PassportActivity.this.showEditDoneProgress(true, false);
                return;
            }
            PassportActivity.this.currentForm = new TLRPC$TL_account_authorizationForm();
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            int size = tLRPC$Vector.objects.size();
            for (int i = 0; i < size; i++) {
                PassportActivity.this.currentForm.values.add((TLRPC$TL_secureValue) tLRPC$Vector.objects.get(i));
            }
            openRequestInterface();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$13(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.8.this.lambda$run$12(tLObject, tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$14(TLRPC$TL_account_passwordSettings tLRPC$TL_account_passwordSettings, boolean z, byte[] bArr) {
            PassportActivity.this.currentEmail = tLRPC$TL_account_passwordSettings.email;
            if (z) {
                PassportActivity passportActivity = PassportActivity.this;
                passportActivity.saltedPassword = passportActivity.savedSaltedPassword;
            }
            PassportActivity passportActivity2 = PassportActivity.this;
            if (PassportActivity.checkSecret(passportActivity2.decryptSecret(passportActivity2.secureSecret, PassportActivity.this.saltedPassword), Long.valueOf(PassportActivity.this.secureSecretId)) && bArr.length != 0 && PassportActivity.this.secureSecretId != 0) {
                if (PassportActivity.this.currentBotId != 0) {
                    openRequestInterface();
                    return;
                }
                ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(new TLObject() { // from class: org.telegram.tgnet.TLRPC$TL_account_getAllSecureValues
                    @Override // org.telegram.tgnet.TLObject
                    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z2) {
                        TLRPC$Vector tLRPC$Vector = new TLRPC$Vector();
                        int readInt32 = abstractSerializedData.readInt32(z2);
                        for (int i2 = 0; i2 < readInt32; i2++) {
                            TLRPC$TL_secureValue TLdeserialize = TLRPC$TL_secureValue.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z2), z2);
                            if (TLdeserialize == null) {
                                return tLRPC$Vector;
                            }
                            tLRPC$Vector.objects.add(TLdeserialize);
                        }
                        return tLRPC$Vector;
                    }

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                        abstractSerializedData.writeInt32(-1299661699);
                    }
                }, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda13
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        PassportActivity.8.this.lambda$run$13(tLObject, tLRPC$TL_error);
                    }
                });
                return;
            }
            PassportActivity passportActivity3 = PassportActivity.this;
            if (z) {
                UserConfig.getInstance(((BaseFragment) passportActivity3).currentAccount).resetSavedPassword();
                PassportActivity.this.usingSavedPassword = 0;
                PassportActivity.this.updatePasswordInterface();
                return;
            }
            if (passportActivity3.currentForm != null) {
                PassportActivity.this.currentForm.values.clear();
                PassportActivity.this.currentForm.errors.clear();
            }
            if (PassportActivity.this.secureSecret == null || PassportActivity.this.secureSecret.length == 0) {
                generateNewSecret();
            } else {
                resetSecret();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$15(TLObject tLObject, String str, final boolean z) {
            final byte[] bArr;
            final TLRPC$TL_account_passwordSettings tLRPC$TL_account_passwordSettings = (TLRPC$TL_account_passwordSettings) tLObject;
            TLRPC$TL_secureSecretSettings tLRPC$TL_secureSecretSettings = tLRPC$TL_account_passwordSettings.secure_settings;
            if (tLRPC$TL_secureSecretSettings != null) {
                PassportActivity.this.secureSecret = tLRPC$TL_secureSecretSettings.secure_secret;
                PassportActivity.this.secureSecretId = tLRPC$TL_account_passwordSettings.secure_settings.secure_secret_id;
                TLRPC$SecurePasswordKdfAlgo tLRPC$SecurePasswordKdfAlgo = tLRPC$TL_account_passwordSettings.secure_settings.secure_algo;
                if (tLRPC$SecurePasswordKdfAlgo instanceof TLRPC$TL_securePasswordKdfAlgoSHA512) {
                    bArr = ((TLRPC$TL_securePasswordKdfAlgoSHA512) tLRPC$SecurePasswordKdfAlgo).salt;
                    PassportActivity.this.saltedPassword = Utilities.computeSHA512(bArr, AndroidUtilities.getStringBytes(str), bArr);
                } else if (tLRPC$SecurePasswordKdfAlgo instanceof TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
                    TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 = (TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) tLRPC$SecurePasswordKdfAlgo;
                    byte[] bArr2 = tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt;
                    PassportActivity.this.saltedPassword = Utilities.computePBKDF2(AndroidUtilities.getStringBytes(str), tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt);
                    bArr = bArr2;
                } else if (tLRPC$SecurePasswordKdfAlgo instanceof TLRPC$TL_securePasswordKdfAlgoUnknown) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            PassportActivity.8.this.lambda$run$11();
                        }
                    });
                    return;
                } else {
                    bArr = new byte[0];
                }
            } else {
                if (PassportActivity.this.currentPassword.new_secure_algo instanceof TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
                    TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter1000002 = (TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) PassportActivity.this.currentPassword.new_secure_algo;
                    byte[] bArr3 = tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter1000002.salt;
                    PassportActivity.this.saltedPassword = Utilities.computePBKDF2(AndroidUtilities.getStringBytes(str), tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter1000002.salt);
                    bArr = bArr3;
                } else {
                    bArr = new byte[0];
                }
                PassportActivity.this.secureSecret = null;
                PassportActivity.this.secureSecretId = 0L;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.8.this.lambda$run$14(tLRPC$TL_account_passwordSettings, z, bArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$16(boolean z, TLRPC$TL_error tLRPC$TL_error) {
            if (z) {
                UserConfig.getInstance(((BaseFragment) PassportActivity.this).currentAccount).resetSavedPassword();
                PassportActivity.this.usingSavedPassword = 0;
                PassportActivity.this.updatePasswordInterface();
                if (PassportActivity.this.inputFieldContainers == null || PassportActivity.this.inputFieldContainers[0].getVisibility() != 0) {
                    return;
                }
                PassportActivity.this.inputFields[0].requestFocus();
                AndroidUtilities.showKeyboard(PassportActivity.this.inputFields[0]);
                return;
            }
            PassportActivity.this.showEditDoneProgress(true, false);
            if (tLRPC$TL_error.text.equals("PASSWORD_HASH_INVALID")) {
                PassportActivity.this.onPasscodeError(true);
            } else if (!tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                PassportActivity.this.showAlertWithText(LocaleController.getString(R.string.AppName), tLRPC$TL_error.text);
            } else {
                int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                PassportActivity.this.showAlertWithText(LocaleController.getString(R.string.AppName), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, intValue < 60 ? LocaleController.formatPluralString("Seconds", intValue, new Object[0]) : LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0])));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$9(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, boolean z) {
            if (tLRPC$TL_error == null) {
                PassportActivity.this.currentPassword = (TLRPC$account_Password) tLObject;
                TwoStepVerificationActivity.initPasswordNewAlgo(PassportActivity.this.currentPassword);
                PassportActivity.this.onPasswordDone(z);
            }
        }

        private void openRequestInterface() {
            if (PassportActivity.this.inputFields == null) {
                return;
            }
            if (!this.val$saved) {
                UserConfig.getInstance(((BaseFragment) PassportActivity.this).currentAccount).savePassword(this.val$x_bytes, PassportActivity.this.saltedPassword);
            }
            AndroidUtilities.hideKeyboard(PassportActivity.this.inputFields[0]);
            PassportActivity.this.ignoreOnFailure = true;
            PassportActivity passportActivity = new PassportActivity(PassportActivity.this.currentBotId == 0 ? 8 : 0, PassportActivity.this.currentBotId, PassportActivity.this.currentScope, PassportActivity.this.currentPublicKey, PassportActivity.this.currentPayload, PassportActivity.this.currentNonce, PassportActivity.this.currentCallbackUrl, PassportActivity.this.currentForm, PassportActivity.this.currentPassword);
            passportActivity.currentEmail = PassportActivity.this.currentEmail;
            ((BaseFragment) passportActivity).currentAccount = ((BaseFragment) PassportActivity.this).currentAccount;
            passportActivity.saltedPassword = PassportActivity.this.saltedPassword;
            passportActivity.secureSecret = PassportActivity.this.secureSecret;
            passportActivity.secureSecretId = PassportActivity.this.secureSecretId;
            passportActivity.needActivityResult = PassportActivity.this.needActivityResult;
            if (((BaseFragment) PassportActivity.this).parentLayout == null || !((BaseFragment) PassportActivity.this).parentLayout.checkTransitionAnimation()) {
                PassportActivity.this.presentFragment(passportActivity, true);
            } else {
                PassportActivity.this.presentAfterAnimation = passportActivity;
            }
        }

        private void resetSecret() {
            TLRPC$TL_account_updatePasswordSettings tLRPC$TL_account_updatePasswordSettings = new TLRPC$TL_account_updatePasswordSettings();
            if (PassportActivity.this.currentPassword.current_algo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
                tLRPC$TL_account_updatePasswordSettings.password = SRPHelper.startCheck(this.val$x_bytes, PassportActivity.this.currentPassword.srp_id, PassportActivity.this.currentPassword.srp_B, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) PassportActivity.this.currentPassword.current_algo);
            }
            TLRPC$TL_account_passwordInputSettings tLRPC$TL_account_passwordInputSettings = new TLRPC$TL_account_passwordInputSettings();
            tLRPC$TL_account_updatePasswordSettings.new_settings = tLRPC$TL_account_passwordInputSettings;
            tLRPC$TL_account_passwordInputSettings.new_secure_settings = new TLRPC$TL_secureSecretSettings();
            TLRPC$TL_secureSecretSettings tLRPC$TL_secureSecretSettings = tLRPC$TL_account_updatePasswordSettings.new_settings.new_secure_settings;
            tLRPC$TL_secureSecretSettings.secure_secret = new byte[0];
            tLRPC$TL_secureSecretSettings.secure_algo = new TLRPC$TL_securePasswordKdfAlgoUnknown();
            TLRPC$TL_account_passwordInputSettings tLRPC$TL_account_passwordInputSettings2 = tLRPC$TL_account_updatePasswordSettings.new_settings;
            tLRPC$TL_account_passwordInputSettings2.new_secure_settings.secure_secret_id = 0L;
            tLRPC$TL_account_passwordInputSettings2.flags |= 4;
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(this.val$req, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda14
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    PassportActivity.8.this.lambda$resetSecret$3(tLObject, tLRPC$TL_error);
                }
            });
        }

        @Override // org.telegram.tgnet.RequestDelegate
        public void run(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            if (tLRPC$TL_error != null && "SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
                TLRPC$TL_account_getPassword tLRPC$TL_account_getPassword = new TLRPC$TL_account_getPassword();
                ConnectionsManager connectionsManager = ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount);
                final boolean z = this.val$saved;
                connectionsManager.sendRequest(tLRPC$TL_account_getPassword, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda7
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                        PassportActivity.8.this.lambda$run$10(z, tLObject2, tLRPC$TL_error2);
                    }
                }, 8);
            } else if (tLRPC$TL_error != null) {
                final boolean z2 = this.val$saved;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        PassportActivity.8.this.lambda$run$16(z2, tLRPC$TL_error);
                    }
                });
            } else {
                DispatchQueue dispatchQueue = Utilities.globalQueue;
                final String str = this.val$textPassword;
                final boolean z3 = this.val$saved;
                dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.PassportActivity$8$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        PassportActivity.8.this.lambda$run$15(tLObject, str, z3);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class EncryptionResult {
        byte[] decrypyedFileSecret;
        byte[] encryptedData;
        byte[] fileHash;
        byte[] fileSecret;
        SecureDocumentKey secureDocumentKey;

        public EncryptionResult(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6) {
            this.encryptedData = bArr;
            this.fileSecret = bArr2;
            this.fileHash = bArr4;
            this.decrypyedFileSecret = bArr3;
            this.secureDocumentKey = new SecureDocumentKey(bArr5, bArr6);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public interface ErrorRunnable {
        void onError(String str, String str2);
    }

    /* loaded from: classes4.dex */
    public class LinkSpan extends ClickableSpan {
        public LinkSpan() {
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(View view) {
            Browser.openUrl(PassportActivity.this.getParentActivity(), PassportActivity.this.currentForm.privacy_policy_url);
        }

        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(true);
            textPaint.setTypeface(AndroidUtilities.bold());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public interface PassportActivityDelegate {
        void deleteValue(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, ArrayList arrayList, boolean z, Runnable runnable, ErrorRunnable errorRunnable);

        SecureDocument saveFile(TLRPC$TL_secureFile tLRPC$TL_secureFile);

        void saveValue(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, String str, String str2, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, String str3, ArrayList arrayList, SecureDocument secureDocument, ArrayList arrayList2, SecureDocument secureDocument2, SecureDocument secureDocument3, Runnable runnable, ErrorRunnable errorRunnable);
    }

    /* loaded from: classes4.dex */
    public class PhoneConfirmationView extends SlideView implements NotificationCenter.NotificationCenterDelegate {
        private ImageView blackImageView;
        private ImageView blueImageView;
        private EditTextBoldCursor[] codeField;
        private LinearLayout codeFieldContainer;
        private int codeTime;
        private Timer codeTimer;
        private TextView confirmTextView;
        private Bundle currentParams;
        private boolean ignoreOnTextChange;
        private double lastCodeTime;
        private double lastCurrentTime;
        private String lastError;
        private int length;
        private boolean nextPressed;
        private int nextType;
        private String pattern;
        private String phone;
        private String phoneHash;
        private TextView problemText;
        private ProgressView progressView;
        private int time;
        private TextView timeText;
        private Timer timeTimer;
        private int timeout;
        private final Object timerSync;
        private TextView titleTextView;
        private int verificationType;
        private boolean waitingForEvent;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public class 4 extends TimerTask {
            4() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0() {
                double currentTimeMillis = System.currentTimeMillis();
                double d = PhoneConfirmationView.this.lastCodeTime;
                Double.isNaN(currentTimeMillis);
                PhoneConfirmationView.this.lastCodeTime = currentTimeMillis;
                PhoneConfirmationView.access$9926(PhoneConfirmationView.this, currentTimeMillis - d);
                if (PhoneConfirmationView.this.codeTime <= 1000) {
                    PhoneConfirmationView.this.problemText.setVisibility(0);
                    PhoneConfirmationView.this.timeText.setVisibility(8);
                    PhoneConfirmationView.this.destroyCodeTimer();
                }
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$4$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PassportActivity.PhoneConfirmationView.4.this.lambda$run$0();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public class 5 extends TimerTask {
            5() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0(TLRPC$TL_error tLRPC$TL_error) {
                PhoneConfirmationView.this.lastError = tLRPC$TL_error.text;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$1(TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
                if (tLRPC$TL_error == null || tLRPC$TL_error.text == null) {
                    return;
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$5$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        PassportActivity.PhoneConfirmationView.5.this.lambda$run$0(tLRPC$TL_error);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* JADX WARN: Removed duplicated region for block: B:16:0x0082  */
            /* JADX WARN: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public /* synthetic */ void lambda$run$2() {
                TextView textView;
                int i;
                NotificationCenter globalInstance;
                PhoneConfirmationView phoneConfirmationView;
                int i2;
                TextView textView2;
                String formatString;
                if (PhoneConfirmationView.this.time < 1000) {
                    if (PhoneConfirmationView.this.progressView != null) {
                        PhoneConfirmationView.this.progressView.setProgress(1.0f);
                    }
                    PhoneConfirmationView.this.destroyTimer();
                    if (PhoneConfirmationView.this.verificationType == 3) {
                        AndroidUtilities.setWaitingForCall(false);
                        globalInstance = NotificationCenter.getGlobalInstance();
                        phoneConfirmationView = PhoneConfirmationView.this;
                        i2 = NotificationCenter.didReceiveCall;
                    } else if (PhoneConfirmationView.this.verificationType != 2 && PhoneConfirmationView.this.verificationType != 4) {
                        return;
                    } else {
                        if (PhoneConfirmationView.this.nextType == 4 || PhoneConfirmationView.this.nextType == 2) {
                            if (PhoneConfirmationView.this.nextType == 4) {
                                textView = PhoneConfirmationView.this.timeText;
                                i = R.string.Calling;
                            } else {
                                textView = PhoneConfirmationView.this.timeText;
                                i = R.string.SendingSms;
                            }
                            textView.setText(LocaleController.getString(i));
                            PhoneConfirmationView.this.createCodeTimer();
                            TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode = new TLRPC$TL_auth_resendCode();
                            tLRPC$TL_auth_resendCode.phone_number = PhoneConfirmationView.this.phone;
                            tLRPC$TL_auth_resendCode.phone_code_hash = PhoneConfirmationView.this.phoneHash;
                            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_resendCode, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$5$$ExternalSyntheticLambda1
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    PassportActivity.PhoneConfirmationView.5.this.lambda$run$1(tLObject, tLRPC$TL_error);
                                }
                            }, 2);
                            return;
                        } else if (PhoneConfirmationView.this.nextType != 3) {
                            return;
                        } else {
                            AndroidUtilities.setWaitingForSms(false);
                            globalInstance = NotificationCenter.getGlobalInstance();
                            phoneConfirmationView = PhoneConfirmationView.this;
                            i2 = NotificationCenter.didReceiveSmsCode;
                        }
                    }
                    globalInstance.removeObserver(phoneConfirmationView, i2);
                    PhoneConfirmationView.this.waitingForEvent = false;
                    PhoneConfirmationView.this.destroyCodeTimer();
                    PhoneConfirmationView.this.resendCode();
                    return;
                }
                int i3 = (PhoneConfirmationView.this.time / 1000) / 60;
                int i4 = (PhoneConfirmationView.this.time / 1000) - (i3 * 60);
                if (PhoneConfirmationView.this.nextType != 4 && PhoneConfirmationView.this.nextType != 3) {
                    if (PhoneConfirmationView.this.nextType == 2) {
                        textView2 = PhoneConfirmationView.this.timeText;
                        formatString = LocaleController.formatString("SmsText", R.string.SmsText, Integer.valueOf(i3), Integer.valueOf(i4));
                    }
                    if (PhoneConfirmationView.this.progressView == null) {
                        PhoneConfirmationView.this.progressView.setProgress(1.0f - (PhoneConfirmationView.this.time / PhoneConfirmationView.this.timeout));
                        return;
                    }
                    return;
                }
                textView2 = PhoneConfirmationView.this.timeText;
                formatString = LocaleController.formatString("CallText", R.string.CallText, Integer.valueOf(i3), Integer.valueOf(i4));
                textView2.setText(formatString);
                if (PhoneConfirmationView.this.progressView == null) {
                }
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (PhoneConfirmationView.this.timeTimer == null) {
                    return;
                }
                double currentTimeMillis = System.currentTimeMillis();
                double d = PhoneConfirmationView.this.lastCurrentTime;
                Double.isNaN(currentTimeMillis);
                PhoneConfirmationView.access$10526(PhoneConfirmationView.this, currentTimeMillis - d);
                PhoneConfirmationView.this.lastCurrentTime = currentTimeMillis;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$5$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PassportActivity.PhoneConfirmationView.5.this.lambda$run$2();
                    }
                });
            }
        }

        public PhoneConfirmationView(Context context, int i) {
            super(context);
            TextView textView;
            int i2;
            View view;
            ViewGroup.LayoutParams createLinear;
            TextView textView2;
            int i3;
            FrameLayout.LayoutParams createFrame;
            View view2;
            this.timerSync = new Object();
            this.time = 60000;
            this.codeTime = 15000;
            this.lastError = "";
            this.pattern = "*";
            this.verificationType = i;
            setOrientation(1);
            TextView textView3 = new TextView(context);
            this.confirmTextView = textView3;
            int i4 = Theme.key_windowBackgroundWhiteGrayText6;
            textView3.setTextColor(Theme.getColor(i4));
            this.confirmTextView.setTextSize(1, 14.0f);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            TextView textView4 = new TextView(context);
            this.titleTextView = textView4;
            int i5 = Theme.key_windowBackgroundWhiteBlackText;
            textView4.setTextColor(Theme.getColor(i5));
            this.titleTextView.setTextSize(1, 18.0f);
            this.titleTextView.setTypeface(AndroidUtilities.bold());
            this.titleTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.titleTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.titleTextView.setGravity(49);
            if (this.verificationType == 3) {
                this.confirmTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                FrameLayout frameLayout = new FrameLayout(context);
                addView(frameLayout, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3));
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.phone_activate);
                boolean z = LocaleController.isRTL;
                if (z) {
                    frameLayout.addView(imageView, LayoutHelper.createFrame(64, 76.0f, 19, 2.0f, 2.0f, 0.0f, 0.0f));
                    View view3 = this.confirmTextView;
                    createFrame = LayoutHelper.createFrame(-1, -2.0f, LocaleController.isRTL ? 5 : 3, 82.0f, 0.0f, 0.0f, 0.0f);
                    view2 = view3;
                } else {
                    frameLayout.addView(this.confirmTextView, LayoutHelper.createFrame(-1, -2.0f, z ? 5 : 3, 0.0f, 0.0f, 82.0f, 0.0f));
                    createFrame = LayoutHelper.createFrame(64, 76.0f, 21, 0.0f, 2.0f, 0.0f, 2.0f);
                    view2 = imageView;
                }
                frameLayout.addView(view2, createFrame);
            } else {
                this.confirmTextView.setGravity(49);
                FrameLayout frameLayout2 = new FrameLayout(context);
                addView(frameLayout2, LayoutHelper.createLinear(-2, -2, 49));
                if (this.verificationType == 1) {
                    ImageView imageView2 = new ImageView(context);
                    this.blackImageView = imageView2;
                    imageView2.setImageResource(R.drawable.sms_devices);
                    ImageView imageView3 = this.blackImageView;
                    int color = Theme.getColor(i5);
                    PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
                    imageView3.setColorFilter(new PorterDuffColorFilter(color, mode));
                    frameLayout2.addView(this.blackImageView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    ImageView imageView4 = new ImageView(context);
                    this.blueImageView = imageView4;
                    imageView4.setImageResource(R.drawable.sms_bubble);
                    this.blueImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionBackground), mode));
                    frameLayout2.addView(this.blueImageView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    textView = this.titleTextView;
                    i2 = R.string.SentAppCodeTitle;
                } else {
                    ImageView imageView5 = new ImageView(context);
                    this.blueImageView = imageView5;
                    imageView5.setImageResource(R.drawable.sms_code);
                    this.blueImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionBackground), PorterDuff.Mode.MULTIPLY));
                    frameLayout2.addView(this.blueImageView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    textView = this.titleTextView;
                    i2 = R.string.SentSmsCodeTitle;
                }
                textView.setText(LocaleController.getString(i2));
                addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 18, 0, 0));
                addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 17, 0, 0));
            }
            LinearLayout linearLayout = new LinearLayout(context);
            this.codeFieldContainer = linearLayout;
            linearLayout.setOrientation(0);
            addView(this.codeFieldContainer, LayoutHelper.createLinear(-2, 36, 1));
            if (this.verificationType == 3) {
                this.codeFieldContainer.setVisibility(8);
            }
            TextView textView5 = new TextView(context) { // from class: org.telegram.ui.PassportActivity.PhoneConfirmationView.1
                @Override // android.widget.TextView, android.view.View
                protected void onMeasure(int i6, int i7) {
                    super.onMeasure(i6, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
                }
            };
            this.timeText = textView5;
            textView5.setTextColor(Theme.getColor(i4));
            this.timeText.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            if (this.verificationType == 3) {
                this.timeText.setTextSize(1, 14.0f);
                addView(this.timeText, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3));
                this.progressView = new ProgressView(context);
                this.timeText.setGravity(LocaleController.isRTL ? 5 : 3);
                view = this.progressView;
                createLinear = LayoutHelper.createLinear(-1, 3, 0.0f, 12.0f, 0.0f, 0.0f);
            } else {
                this.timeText.setPadding(0, AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(10.0f));
                this.timeText.setTextSize(1, 15.0f);
                this.timeText.setGravity(49);
                view = this.timeText;
                createLinear = LayoutHelper.createLinear(-2, -2, 49);
            }
            addView(view, createLinear);
            TextView textView6 = new TextView(context) { // from class: org.telegram.ui.PassportActivity.PhoneConfirmationView.2
                @Override // android.widget.TextView, android.view.View
                protected void onMeasure(int i6, int i7) {
                    super.onMeasure(i6, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
                }
            };
            this.problemText = textView6;
            textView6.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
            this.problemText.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.problemText.setPadding(0, AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(10.0f));
            this.problemText.setTextSize(1, 15.0f);
            this.problemText.setGravity(49);
            if (this.verificationType == 1) {
                textView2 = this.problemText;
                i3 = R.string.DidNotGetTheCodeSms;
            } else {
                textView2 = this.problemText;
                i3 = R.string.DidNotGetTheCode;
            }
            textView2.setText(LocaleController.getString(i3));
            addView(this.problemText, LayoutHelper.createLinear(-2, -2, 49));
            this.problemText.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view4) {
                    PassportActivity.PhoneConfirmationView.this.lambda$new$0(view4);
                }
            });
        }

        static /* synthetic */ int access$10526(PhoneConfirmationView phoneConfirmationView, double d) {
            double d2 = phoneConfirmationView.time;
            Double.isNaN(d2);
            int i = (int) (d2 - d);
            phoneConfirmationView.time = i;
            return i;
        }

        static /* synthetic */ int access$9926(PhoneConfirmationView phoneConfirmationView, double d) {
            double d2 = phoneConfirmationView.codeTime;
            Double.isNaN(d2);
            int i = (int) (d2 - d);
            phoneConfirmationView.codeTime = i;
            return i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void createCodeTimer() {
            if (this.codeTimer != null) {
                return;
            }
            this.codeTime = 15000;
            this.codeTimer = new Timer();
            this.lastCodeTime = System.currentTimeMillis();
            this.codeTimer.schedule(new 4(), 0L, 1000L);
        }

        private void createTimer() {
            if (this.timeTimer != null) {
                return;
            }
            Timer timer = new Timer();
            this.timeTimer = timer;
            timer.schedule(new 5(), 0L, 1000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void destroyCodeTimer() {
            try {
                synchronized (this.timerSync) {
                    Timer timer = this.codeTimer;
                    if (timer != null) {
                        timer.cancel();
                        this.codeTimer = null;
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void destroyTimer() {
            try {
                synchronized (this.timerSync) {
                    Timer timer = this.timeTimer;
                    if (timer != null) {
                        timer.cancel();
                        this.timeTimer = null;
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getCode() {
            if (this.codeField == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (true) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
                if (i >= editTextBoldCursorArr.length) {
                    return sb.toString();
                }
                sb.append(PhoneFormat.stripExceptNumbers(editTextBoldCursorArr[i].getText().toString()));
                i++;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            if (this.nextPressed) {
                return;
            }
            int i = this.nextType;
            if ((i != 4 || this.verificationType != 2) && i != 0) {
                resendCode();
                return;
            }
            try {
                PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                String format = String.format(Locale.US, "%s (%d)", packageInfo.versionName, Integer.valueOf(packageInfo.versionCode));
                Intent intent = new Intent("android.intent.action.SENDTO");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra("android.intent.extra.EMAIL", new String[]{"sms@telegram.org"});
                intent.putExtra("android.intent.extra.SUBJECT", "Android registration/login issue " + format + " " + this.phone);
                intent.putExtra("android.intent.extra.TEXT", "Phone: " + this.phone + "\nApp version: " + format + "\nOS version: SDK " + Build.VERSION.SDK_INT + "\nDevice Name: " + Build.MANUFACTURER + Build.MODEL + "\nLocale: " + Locale.getDefault() + "\nError: " + this.lastError);
                getContext().startActivity(Intent.createChooser(intent, "Send email..."));
            } catch (Exception unused) {
                AlertsCreator.showSimpleAlert(PassportActivity.this, LocaleController.getString(R.string.NoMailInstalled));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBackPressed$8(DialogInterface dialogInterface, int i) {
            onBackPressed(true);
            PassportActivity.this.setPage(0, true, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onBackPressed$9(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0086  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00c7 A[LOOP:0: B:36:0x00c2->B:38:0x00c7, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:41:0x00d1 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$onNextPressed$6(TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_account_verifyPhone tLRPC$TL_account_verifyPhone) {
            int i;
            NotificationCenter globalInstance;
            int i2;
            int i3;
            EditTextBoldCursor[] editTextBoldCursorArr;
            int i4;
            PassportActivity.this.needHideProgress();
            this.nextPressed = false;
            if (tLRPC$TL_error == null) {
                destroyTimer();
                destroyCodeTimer();
                PassportActivityDelegate passportActivityDelegate = PassportActivity.this.delegate;
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType = PassportActivity.this.currentType;
                String str = (String) PassportActivity.this.currentValues.get("phone");
                final PassportActivity passportActivity = PassportActivity.this;
                passportActivityDelegate.saveValue(tLRPC$TL_secureRequiredType, str, null, null, null, null, null, null, null, null, new Runnable() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        PassportActivity.this.finishFragment();
                    }
                }, null);
                return;
            }
            this.lastError = tLRPC$TL_error.text;
            int i5 = this.verificationType;
            if ((i5 == 3 && ((i4 = this.nextType) == 4 || i4 == 2)) || ((i5 == 2 && ((i = this.nextType) == 4 || i == 3)) || (i5 == 4 && this.nextType == 2))) {
                createTimer();
            }
            int i6 = this.verificationType;
            if (i6 == 2) {
                AndroidUtilities.setWaitingForSms(true);
                globalInstance = NotificationCenter.getGlobalInstance();
                i2 = NotificationCenter.didReceiveSmsCode;
            } else {
                if (i6 == 3) {
                    AndroidUtilities.setWaitingForCall(true);
                    globalInstance = NotificationCenter.getGlobalInstance();
                    i2 = NotificationCenter.didReceiveCall;
                }
                this.waitingForEvent = true;
                if (this.verificationType != 3) {
                    AlertsCreator.processError(((BaseFragment) PassportActivity.this).currentAccount, tLRPC$TL_error, PassportActivity.this, tLRPC$TL_account_verifyPhone, new Object[0]);
                }
                PassportActivity.this.showEditDoneProgress(true, false);
                if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") && !tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                    if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                        onBackPressed(true);
                        PassportActivity.this.setPage(0, true, null);
                        return;
                    }
                    return;
                }
                i3 = 0;
                while (true) {
                    editTextBoldCursorArr = this.codeField;
                    if (i3 < editTextBoldCursorArr.length) {
                        editTextBoldCursorArr[0].requestFocus();
                        return;
                    } else {
                        editTextBoldCursorArr[i3].setText("");
                        i3++;
                    }
                }
            }
            globalInstance.addObserver(this, i2);
            this.waitingForEvent = true;
            if (this.verificationType != 3) {
            }
            PassportActivity.this.showEditDoneProgress(true, false);
            if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY")) {
            }
            i3 = 0;
            while (true) {
                editTextBoldCursorArr = this.codeField;
                if (i3 < editTextBoldCursorArr.length) {
                }
                editTextBoldCursorArr[i3].setText("");
                i3++;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$7(final TLRPC$TL_account_verifyPhone tLRPC$TL_account_verifyPhone, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.PhoneConfirmationView.this.lambda$onNextPressed$6(tLRPC$TL_error, tLRPC$TL_account_verifyPhone);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resendCode$1(DialogInterface dialogInterface, int i) {
            onBackPressed(true);
            PassportActivity.this.finishFragment();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resendCode$2(TLRPC$TL_error tLRPC$TL_error, Bundle bundle, TLObject tLObject, TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode) {
            this.nextPressed = false;
            if (tLRPC$TL_error == null) {
                PassportActivity.this.fillNextCodeParams(bundle, (TLRPC$TL_auth_sentCode) tLObject, true);
            } else {
                AlertDialog alertDialog = (AlertDialog) AlertsCreator.processError(((BaseFragment) PassportActivity.this).currentAccount, tLRPC$TL_error, PassportActivity.this, tLRPC$TL_auth_resendCode, new Object[0]);
                if (alertDialog != null && tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                    alertDialog.setPositiveButtonListener(new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda10
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            PassportActivity.PhoneConfirmationView.this.lambda$resendCode$1(dialogInterface, i);
                        }
                    });
                }
            }
            PassportActivity.this.needHideProgress();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resendCode$3(final Bundle bundle, final TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.PhoneConfirmationView.this.lambda$resendCode$2(tLRPC$TL_error, bundle, tLObject, tLRPC$TL_auth_resendCode);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$setParams$4(int i, View view, int i2, KeyEvent keyEvent) {
            if (i2 == 67 && this.codeField[i].length() == 0 && i > 0) {
                int i3 = i - 1;
                EditTextBoldCursor editTextBoldCursor = this.codeField[i3];
                editTextBoldCursor.setSelection(editTextBoldCursor.length());
                this.codeField[i3].requestFocus();
                this.codeField[i3].dispatchKeyEvent(keyEvent);
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$setParams$5(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 5) {
                onNextPressed(null);
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void resendCode() {
            final Bundle bundle = new Bundle();
            bundle.putString("phone", this.phone);
            this.nextPressed = true;
            PassportActivity.this.needShowProgress();
            final TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode = new TLRPC$TL_auth_resendCode();
            tLRPC$TL_auth_resendCode.phone_number = this.phone;
            tLRPC$TL_auth_resendCode.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_resendCode, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda6
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    PassportActivity.PhoneConfirmationView.this.lambda$resendCode$3(bundle, tLRPC$TL_auth_resendCode, tLObject, tLRPC$TL_error);
                }
            }, 2);
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            EditTextBoldCursor[] editTextBoldCursorArr;
            if (!this.waitingForEvent || (editTextBoldCursorArr = this.codeField) == null) {
                return;
            }
            if (i == NotificationCenter.didReceiveSmsCode) {
                editTextBoldCursorArr[0].setText("" + objArr[0]);
            } else if (i != NotificationCenter.didReceiveCall) {
                return;
            } else {
                String str = "" + objArr[0];
                if (!AndroidUtilities.checkPhonePattern(this.pattern, str)) {
                    return;
                }
                this.ignoreOnTextChange = true;
                this.codeField[0].setText(str);
                this.ignoreOnTextChange = false;
            }
            onNextPressed(null);
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean needBackButton() {
            return true;
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean onBackPressed(boolean z) {
            NotificationCenter globalInstance;
            int i;
            if (!z) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PassportActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString(R.string.AppName));
                builder.setMessage(LocaleController.getString(R.string.StopVerification));
                builder.setPositiveButton(LocaleController.getString(R.string.Continue), null);
                builder.setNegativeButton(LocaleController.getString(R.string.Stop), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda2
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        PassportActivity.PhoneConfirmationView.this.lambda$onBackPressed$8(dialogInterface, i2);
                    }
                });
                PassportActivity.this.showDialog(builder.create());
                return false;
            }
            TLRPC$TL_auth_cancelCode tLRPC$TL_auth_cancelCode = new TLRPC$TL_auth_cancelCode();
            tLRPC$TL_auth_cancelCode.phone_number = this.phone;
            tLRPC$TL_auth_cancelCode.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_cancelCode, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda3
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    PassportActivity.PhoneConfirmationView.lambda$onBackPressed$9(tLObject, tLRPC$TL_error);
                }
            }, 2);
            destroyTimer();
            destroyCodeTimer();
            this.currentParams = null;
            int i2 = this.verificationType;
            if (i2 != 2) {
                if (i2 == 3) {
                    AndroidUtilities.setWaitingForCall(false);
                    globalInstance = NotificationCenter.getGlobalInstance();
                    i = NotificationCenter.didReceiveCall;
                }
                this.waitingForEvent = false;
                return true;
            }
            AndroidUtilities.setWaitingForSms(false);
            globalInstance = NotificationCenter.getGlobalInstance();
            i = NotificationCenter.didReceiveSmsCode;
            globalInstance.removeObserver(this, i);
            this.waitingForEvent = false;
            return true;
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onCancelPressed() {
            this.nextPressed = false;
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onDestroyActivity() {
            NotificationCenter globalInstance;
            int i;
            super.onDestroyActivity();
            int i2 = this.verificationType;
            if (i2 != 2) {
                if (i2 == 3) {
                    AndroidUtilities.setWaitingForCall(false);
                    globalInstance = NotificationCenter.getGlobalInstance();
                    i = NotificationCenter.didReceiveCall;
                }
                this.waitingForEvent = false;
                destroyTimer();
                destroyCodeTimer();
            }
            AndroidUtilities.setWaitingForSms(false);
            globalInstance = NotificationCenter.getGlobalInstance();
            i = NotificationCenter.didReceiveSmsCode;
            globalInstance.removeObserver(this, i);
            this.waitingForEvent = false;
            destroyTimer();
            destroyCodeTimer();
        }

        @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int measuredHeight;
            TextView textView;
            int left;
            TextView textView2;
            super.onLayout(z, i, i2, i3, i4);
            if (this.verificationType == 3 || this.blueImageView == null) {
                return;
            }
            int bottom = this.confirmTextView.getBottom();
            int measuredHeight2 = getMeasuredHeight() - bottom;
            if (this.problemText.getVisibility() == 0) {
                measuredHeight = this.problemText.getMeasuredHeight();
                i5 = (measuredHeight2 + bottom) - measuredHeight;
                textView = this.problemText;
                left = textView.getLeft();
                textView2 = this.problemText;
            } else if (this.timeText.getVisibility() != 0) {
                i5 = measuredHeight2 + bottom;
                int measuredHeight3 = this.codeFieldContainer.getMeasuredHeight();
                int i6 = (((i5 - bottom) - measuredHeight3) / 2) + bottom;
                LinearLayout linearLayout = this.codeFieldContainer;
                linearLayout.layout(linearLayout.getLeft(), i6, this.codeFieldContainer.getRight(), measuredHeight3 + i6);
            } else {
                measuredHeight = this.timeText.getMeasuredHeight();
                i5 = (measuredHeight2 + bottom) - measuredHeight;
                textView = this.timeText;
                left = textView.getLeft();
                textView2 = this.timeText;
            }
            textView.layout(left, i5, textView2.getRight(), measuredHeight + i5);
            int measuredHeight32 = this.codeFieldContainer.getMeasuredHeight();
            int i62 = (((i5 - bottom) - measuredHeight32) / 2) + bottom;
            LinearLayout linearLayout2 = this.codeFieldContainer;
            linearLayout2.layout(linearLayout2.getLeft(), i62, this.codeFieldContainer.getRight(), measuredHeight32 + i62);
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            ImageView imageView;
            super.onMeasure(i, i2);
            if (this.verificationType == 3 || (imageView = this.blueImageView) == null) {
                return;
            }
            int measuredHeight = imageView.getMeasuredHeight() + this.titleTextView.getMeasuredHeight() + this.confirmTextView.getMeasuredHeight() + AndroidUtilities.dp(35.0f);
            int dp = AndroidUtilities.dp(80.0f);
            int dp2 = AndroidUtilities.dp(291.0f);
            if (PassportActivity.this.scrollHeight - measuredHeight < dp) {
                setMeasuredDimension(getMeasuredWidth(), measuredHeight + dp);
            } else {
                setMeasuredDimension(getMeasuredWidth(), Math.min(PassportActivity.this.scrollHeight, dp2));
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onNextPressed(String str) {
            NotificationCenter globalInstance;
            int i;
            if (this.nextPressed) {
                return;
            }
            if (str == null) {
                str = getCode();
            }
            if (TextUtils.isEmpty(str)) {
                AndroidUtilities.shakeView(this.codeFieldContainer);
                return;
            }
            this.nextPressed = true;
            int i2 = this.verificationType;
            if (i2 != 2) {
                if (i2 == 3) {
                    AndroidUtilities.setWaitingForCall(false);
                    globalInstance = NotificationCenter.getGlobalInstance();
                    i = NotificationCenter.didReceiveCall;
                }
                this.waitingForEvent = false;
                PassportActivity.this.showEditDoneProgress(true, true);
                final TLRPC$TL_account_verifyPhone tLRPC$TL_account_verifyPhone = new TLRPC$TL_account_verifyPhone();
                tLRPC$TL_account_verifyPhone.phone_number = this.phone;
                tLRPC$TL_account_verifyPhone.phone_code = str;
                tLRPC$TL_account_verifyPhone.phone_code_hash = this.phoneHash;
                destroyTimer();
                PassportActivity.this.needShowProgress();
                ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_verifyPhone, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda0
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        PassportActivity.PhoneConfirmationView.this.lambda$onNextPressed$7(tLRPC$TL_account_verifyPhone, tLObject, tLRPC$TL_error);
                    }
                }, 2);
            }
            AndroidUtilities.setWaitingForSms(false);
            globalInstance = NotificationCenter.getGlobalInstance();
            i = NotificationCenter.didReceiveSmsCode;
            globalInstance.removeObserver(this, i);
            this.waitingForEvent = false;
            PassportActivity.this.showEditDoneProgress(true, true);
            final TLRPC$TL_account_verifyPhone tLRPC$TL_account_verifyPhone2 = new TLRPC$TL_account_verifyPhone();
            tLRPC$TL_account_verifyPhone2.phone_number = this.phone;
            tLRPC$TL_account_verifyPhone2.phone_code = str;
            tLRPC$TL_account_verifyPhone2.phone_code_hash = this.phoneHash;
            destroyTimer();
            PassportActivity.this.needShowProgress();
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_verifyPhone2, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    PassportActivity.PhoneConfirmationView.this.lambda$onNextPressed$7(tLRPC$TL_account_verifyPhone2, tLObject, tLRPC$TL_error);
                }
            }, 2);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onShow() {
            super.onShow();
            LinearLayout linearLayout = this.codeFieldContainer;
            if (linearLayout == null || linearLayout.getVisibility() != 0) {
                return;
            }
            for (int length = this.codeField.length - 1; length >= 0; length--) {
                if (length == 0 || this.codeField[length].length() != 0) {
                    this.codeField[length].requestFocus();
                    EditTextBoldCursor editTextBoldCursor = this.codeField[length];
                    editTextBoldCursor.setSelection(editTextBoldCursor.length());
                    AndroidUtilities.showKeyboard(this.codeField[length]);
                    return;
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:79:0x02a8, code lost:
            if (r18.time < 1000) goto L62;
         */
        /* JADX WARN: Code restructure failed: missing block: B:89:0x02db, code lost:
            if (r18.time < 1000) goto L62;
         */
        /* JADX WARN: Code restructure failed: missing block: B:90:0x02dd, code lost:
            r3 = 8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:91:0x02df, code lost:
            r1.setVisibility(r3);
         */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0060  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0089  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x0189  */
        /* JADX WARN: Removed duplicated region for block: B:44:0x0198 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:45:0x0199  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x01fd  */
        /* JADX WARN: Removed duplicated region for block: B:56:0x020c  */
        @Override // org.telegram.ui.Components.SlideView
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void setParams(Bundle bundle, boolean z) {
            NotificationCenter globalInstance;
            int i;
            int i2;
            EditTextBoldCursor[] editTextBoldCursorArr;
            final int i3;
            ProgressView progressView;
            String formatString;
            int i4;
            TextView textView;
            int i5;
            int i6;
            TextView textView2;
            String formatString2;
            int i7 = 0;
            if (bundle == null) {
                return;
            }
            this.waitingForEvent = true;
            int i8 = this.verificationType;
            if (i8 != 2) {
                if (i8 == 3) {
                    AndroidUtilities.setWaitingForCall(true);
                    globalInstance = NotificationCenter.getGlobalInstance();
                    i = NotificationCenter.didReceiveCall;
                }
                this.currentParams = bundle;
                this.phone = bundle.getString("phone");
                this.phoneHash = bundle.getString("phoneHash");
                int i9 = bundle.getInt("timeout");
                this.time = i9;
                this.timeout = i9;
                this.nextType = bundle.getInt("nextType");
                this.pattern = bundle.getString("pattern");
                i2 = bundle.getInt("length");
                this.length = i2;
                if (i2 == 0) {
                    this.length = 5;
                }
                editTextBoldCursorArr = this.codeField;
                CharSequence charSequence = "";
                if (editTextBoldCursorArr == null && editTextBoldCursorArr.length == this.length) {
                    int i10 = 0;
                    while (true) {
                        EditTextBoldCursor[] editTextBoldCursorArr2 = this.codeField;
                        if (i10 >= editTextBoldCursorArr2.length) {
                            break;
                        }
                        editTextBoldCursorArr2[i10].setText("");
                        i10++;
                    }
                } else {
                    this.codeField = new EditTextBoldCursor[this.length];
                    i3 = 0;
                    while (i3 < this.length) {
                        this.codeField[i3] = new EditTextBoldCursor(getContext());
                        EditTextBoldCursor editTextBoldCursor = this.codeField[i3];
                        int i11 = Theme.key_windowBackgroundWhiteBlackText;
                        editTextBoldCursor.setTextColor(Theme.getColor(i11));
                        this.codeField[i3].setCursorColor(Theme.getColor(i11));
                        this.codeField[i3].setCursorSize(AndroidUtilities.dp(20.0f));
                        this.codeField[i3].setCursorWidth(1.5f);
                        Drawable mutate = getResources().getDrawable(R.drawable.search_dark_activated).mutate();
                        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated), PorterDuff.Mode.MULTIPLY));
                        this.codeField[i3].setBackgroundDrawable(mutate);
                        this.codeField[i3].setImeOptions(268435461);
                        this.codeField[i3].setTextSize(1, 20.0f);
                        this.codeField[i3].setMaxLines(1);
                        this.codeField[i3].setTypeface(AndroidUtilities.bold());
                        this.codeField[i3].setPadding(0, 0, 0, 0);
                        this.codeField[i3].setGravity(49);
                        if (this.verificationType == 3) {
                            this.codeField[i3].setEnabled(false);
                            this.codeField[i3].setInputType(0);
                            this.codeField[i3].setVisibility(8);
                        } else {
                            this.codeField[i3].setInputType(3);
                        }
                        this.codeFieldContainer.addView(this.codeField[i3], LayoutHelper.createLinear(34, 36, 1, 0, 0, i3 != this.length - 1 ? 7 : 0, 0));
                        this.codeField[i3].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PassportActivity.PhoneConfirmationView.3
                            @Override // android.text.TextWatcher
                            public void afterTextChanged(Editable editable) {
                                int length;
                                if (!PhoneConfirmationView.this.ignoreOnTextChange && (length = editable.length()) >= 1) {
                                    if (length > 1) {
                                        String obj = editable.toString();
                                        PhoneConfirmationView.this.ignoreOnTextChange = true;
                                        for (int i12 = 0; i12 < Math.min(PhoneConfirmationView.this.length - i3, length); i12++) {
                                            if (i12 == 0) {
                                                editable.replace(0, length, obj.substring(i12, i12 + 1));
                                            } else {
                                                PhoneConfirmationView.this.codeField[i3 + i12].setText(obj.substring(i12, i12 + 1));
                                            }
                                        }
                                        PhoneConfirmationView.this.ignoreOnTextChange = false;
                                    }
                                    if (i3 != PhoneConfirmationView.this.length - 1) {
                                        PhoneConfirmationView.this.codeField[i3 + 1].setSelection(PhoneConfirmationView.this.codeField[i3 + 1].length());
                                        PhoneConfirmationView.this.codeField[i3 + 1].requestFocus();
                                    }
                                    if ((i3 == PhoneConfirmationView.this.length - 1 || (i3 == PhoneConfirmationView.this.length - 2 && length >= 2)) && PhoneConfirmationView.this.getCode().length() == PhoneConfirmationView.this.length) {
                                        PhoneConfirmationView.this.onNextPressed(null);
                                    }
                                }
                            }

                            @Override // android.text.TextWatcher
                            public void beforeTextChanged(CharSequence charSequence2, int i12, int i13, int i14) {
                            }

                            @Override // android.text.TextWatcher
                            public void onTextChanged(CharSequence charSequence2, int i12, int i13, int i14) {
                            }
                        });
                        this.codeField[i3].setOnKeyListener(new View.OnKeyListener() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda4
                            @Override // android.view.View.OnKeyListener
                            public final boolean onKey(View view, int i12, KeyEvent keyEvent) {
                                boolean lambda$setParams$4;
                                lambda$setParams$4 = PassportActivity.PhoneConfirmationView.this.lambda$setParams$4(i3, view, i12, keyEvent);
                                return lambda$setParams$4;
                            }
                        });
                        this.codeField[i3].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda5
                            @Override // android.widget.TextView.OnEditorActionListener
                            public final boolean onEditorAction(TextView textView3, int i12, KeyEvent keyEvent) {
                                boolean lambda$setParams$5;
                                lambda$setParams$5 = PassportActivity.PhoneConfirmationView.this.lambda$setParams$5(textView3, i12, keyEvent);
                                return lambda$setParams$5;
                            }
                        });
                        i3++;
                    }
                }
                progressView = this.progressView;
                if (progressView != null) {
                    progressView.setVisibility(this.nextType != 0 ? 0 : 8);
                }
                if (this.phone != null) {
                    return;
                }
                PhoneFormat phoneFormat = PhoneFormat.getInstance();
                String format = phoneFormat.format("+" + this.phone);
                int i12 = this.verificationType;
                if (i12 == 2) {
                    formatString = LocaleController.formatString("SentSmsCode", R.string.SentSmsCode, LocaleController.addNbsp(format));
                } else if (i12 != 3) {
                    if (i12 == 4) {
                        formatString = LocaleController.formatString("SentCallOnly", R.string.SentCallOnly, LocaleController.addNbsp(format));
                    }
                    this.confirmTextView.setText(charSequence);
                    if (this.verificationType == 3) {
                        AndroidUtilities.showKeyboard(this.codeField[0]);
                        this.codeField[0].requestFocus();
                    } else {
                        AndroidUtilities.hideKeyboard(this.codeField[0]);
                    }
                    destroyTimer();
                    destroyCodeTimer();
                    this.lastCurrentTime = System.currentTimeMillis();
                    i4 = this.verificationType;
                    if (i4 != 3 && ((i6 = this.nextType) == 4 || i6 == 2)) {
                        this.problemText.setVisibility(8);
                        this.timeText.setVisibility(0);
                        int i13 = this.nextType;
                        if (i13 == 4) {
                            textView2 = this.timeText;
                            formatString2 = LocaleController.formatString("CallText", R.string.CallText, 1, 0);
                        } else if (i13 == 2) {
                            textView2 = this.timeText;
                            formatString2 = LocaleController.formatString("SmsText", R.string.SmsText, 1, 0);
                        }
                        textView2.setText(formatString2);
                    } else if (i4 != 2 && ((i5 = this.nextType) == 4 || i5 == 3)) {
                        this.timeText.setText(LocaleController.formatString("CallText", R.string.CallText, 2, 0));
                        this.problemText.setVisibility(this.time < 1000 ? 0 : 8);
                        textView = this.timeText;
                    } else if (i4 == 4 || this.nextType != 2) {
                        this.timeText.setVisibility(8);
                        this.problemText.setVisibility(8);
                        createCodeTimer();
                        return;
                    } else {
                        this.timeText.setText(LocaleController.formatString("SmsText", R.string.SmsText, 2, 0));
                        this.problemText.setVisibility(this.time < 1000 ? 0 : 8);
                        textView = this.timeText;
                    }
                    createTimer();
                    return;
                } else {
                    formatString = LocaleController.formatString("SentCallCode", R.string.SentCallCode, LocaleController.addNbsp(format));
                }
                charSequence = AndroidUtilities.replaceTags(formatString);
                this.confirmTextView.setText(charSequence);
                if (this.verificationType == 3) {
                }
                destroyTimer();
                destroyCodeTimer();
                this.lastCurrentTime = System.currentTimeMillis();
                i4 = this.verificationType;
                if (i4 != 3) {
                }
                if (i4 != 2) {
                }
                if (i4 == 4) {
                }
                this.timeText.setVisibility(8);
                this.problemText.setVisibility(8);
                createCodeTimer();
                return;
            }
            AndroidUtilities.setWaitingForSms(true);
            globalInstance = NotificationCenter.getGlobalInstance();
            i = NotificationCenter.didReceiveSmsCode;
            globalInstance.addObserver(this, i);
            this.currentParams = bundle;
            this.phone = bundle.getString("phone");
            this.phoneHash = bundle.getString("phoneHash");
            int i92 = bundle.getInt("timeout");
            this.time = i92;
            this.timeout = i92;
            this.nextType = bundle.getInt("nextType");
            this.pattern = bundle.getString("pattern");
            i2 = bundle.getInt("length");
            this.length = i2;
            if (i2 == 0) {
            }
            editTextBoldCursorArr = this.codeField;
            CharSequence charSequence2 = "";
            if (editTextBoldCursorArr == null) {
            }
            this.codeField = new EditTextBoldCursor[this.length];
            i3 = 0;
            while (i3 < this.length) {
            }
            progressView = this.progressView;
            if (progressView != null) {
            }
            if (this.phone != null) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class ProgressView extends View {
        private Paint paint;
        private Paint paint2;
        private float progress;

        public ProgressView(Context context) {
            super(context);
            this.paint = new Paint();
            this.paint2 = new Paint();
            this.paint.setColor(Theme.getColor(Theme.key_login_progressInner));
            this.paint2.setColor(Theme.getColor(Theme.key_login_progressOuter));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float measuredWidth = (int) (getMeasuredWidth() * this.progress);
            canvas.drawRect(0.0f, 0.0f, measuredWidth, getMeasuredHeight(), this.paint2);
            canvas.drawRect(measuredWidth, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.paint);
        }

        public void setProgress(float f) {
            this.progress = f;
            invalidate();
        }
    }

    /* loaded from: classes4.dex */
    public class SecureDocumentCell extends FrameLayout implements DownloadController.FileDownloadProgressListener {
        private int TAG;
        private int buttonState;
        private SecureDocument currentSecureDocument;
        private BackupImageView imageView;
        private RadialProgress radialProgress;
        private TextView textView;
        private TextView valueTextView;

        public SecureDocumentCell(Context context) {
            super(context);
            this.TAG = DownloadController.getInstance(((BaseFragment) PassportActivity.this).currentAccount).generateObserverTag();
            this.radialProgress = new RadialProgress(this);
            BackupImageView backupImageView = new BackupImageView(context);
            this.imageView = backupImageView;
            addView(backupImageView, LayoutHelper.createFrame(48, 48.0f, (LocaleController.isRTL ? 5 : 3) | 48, 21.0f, 8.0f, 21.0f, 0.0f));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            View view = this.textView;
            boolean z = LocaleController.isRTL;
            addView(view, LayoutHelper.createFrame(-2, -2.0f, (z ? 5 : 3) | 48, z ? 21 : 81, 10.0f, z ? 81 : 21, 0.0f));
            TextView textView2 = new TextView(context);
            this.valueTextView = textView2;
            textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2));
            this.valueTextView.setTextSize(1, 13.0f);
            this.valueTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setPadding(0, 0, 0, 0);
            View view2 = this.valueTextView;
            boolean z2 = LocaleController.isRTL;
            addView(view2, LayoutHelper.createFrame(-2, -2.0f, (z2 ? 5 : 3) | 48, z2 ? 21 : 81, 35.0f, z2 ? 81 : 21, 0.0f));
            setWillNotDraw(false);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            boolean drawChild = super.drawChild(canvas, view, j);
            if (view == this.imageView) {
                this.radialProgress.draw(canvas);
            }
            return drawChild;
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public int getObserverTag() {
            return this.TAG;
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            this.textView.invalidate();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onFailedDownload(String str, boolean z) {
            updateButtonState(false);
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            int left = this.imageView.getLeft() + ((this.imageView.getMeasuredWidth() - AndroidUtilities.dp(24.0f)) / 2);
            int top = this.imageView.getTop() + ((this.imageView.getMeasuredHeight() - AndroidUtilities.dp(24.0f)) / 2);
            this.radialProgress.setProgressRect(left, top, AndroidUtilities.dp(24.0f) + left, AndroidUtilities.dp(24.0f) + top);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + 1, 1073741824));
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onProgressDownload(String str, long j, long j2) {
            this.radialProgress.setProgress(Math.min(1.0f, ((float) j) / ((float) j2)), true);
            if (this.buttonState != 1) {
                updateButtonState(false);
            }
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onProgressUpload(String str, long j, long j2, boolean z) {
            this.radialProgress.setProgress(Math.min(1.0f, ((float) j) / ((float) j2)), true);
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onSuccessDownload(String str) {
            this.radialProgress.setProgress(1.0f, true);
            updateButtonState(true);
        }

        public void setTextAndValueAndImage(String str, CharSequence charSequence, SecureDocument secureDocument) {
            this.textView.setText(str);
            this.valueTextView.setText(charSequence);
            this.imageView.setImage(secureDocument, "48_48");
            this.currentSecureDocument = secureDocument;
            updateButtonState(false);
        }

        public void setValue(CharSequence charSequence) {
            this.valueTextView.setText(charSequence);
        }

        public void updateButtonState(boolean z) {
            String attachFileName = FileLoader.getAttachFileName(this.currentSecureDocument);
            boolean exists = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(this.currentSecureDocument).exists();
            if (TextUtils.isEmpty(attachFileName)) {
                this.radialProgress.setBackground(null, false, false);
                return;
            }
            SecureDocument secureDocument = this.currentSecureDocument;
            if (secureDocument.path != null) {
                if (secureDocument.inputFile != null) {
                    DownloadController.getInstance(((BaseFragment) PassportActivity.this).currentAccount).removeLoadingFileObserver(this);
                    this.radialProgress.setBackground(null, false, z);
                    this.buttonState = -1;
                    return;
                }
                DownloadController.getInstance(((BaseFragment) PassportActivity.this).currentAccount).addLoadingFileObserver(this.currentSecureDocument.path, this);
                this.buttonState = 1;
                Float fileProgress = ImageLoader.getInstance().getFileProgress(this.currentSecureDocument.path);
                this.radialProgress.setBackground(getResources().getDrawable(R.drawable.circle), true, z);
                this.radialProgress.setProgress(fileProgress != null ? fileProgress.floatValue() : 0.0f, false);
            } else if (exists) {
                DownloadController.getInstance(((BaseFragment) PassportActivity.this).currentAccount).removeLoadingFileObserver(this);
                this.buttonState = -1;
                this.radialProgress.setBackground(null, false, z);
            } else {
                DownloadController.getInstance(((BaseFragment) PassportActivity.this).currentAccount).addLoadingFileObserver(attachFileName, this);
                this.buttonState = 1;
                Float fileProgress2 = ImageLoader.getInstance().getFileProgress(attachFileName);
                this.radialProgress.setBackground(getResources().getDrawable(R.drawable.circle), true, z);
                this.radialProgress.setProgress(fileProgress2 != null ? fileProgress2.floatValue() : 0.0f, z);
            }
            invalidate();
        }
    }

    /* loaded from: classes4.dex */
    public class TextDetailSecureCell extends FrameLayout {
        private ImageView checkImageView;
        private boolean needDivider;
        private TextView textView;
        private TextView valueTextView;

        public TextDetailSecureCell(Context context) {
            super(context);
            int i = PassportActivity.this.currentActivityType == 8 ? 21 : 51;
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            TextView textView2 = this.textView;
            TextUtils.TruncateAt truncateAt = TextUtils.TruncateAt.END;
            textView2.setEllipsize(truncateAt);
            this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            TextView textView3 = this.textView;
            boolean z = LocaleController.isRTL;
            addView(textView3, LayoutHelper.createFrame(-2, -2.0f, (z ? 5 : 3) | 48, z ? i : 21, 10.0f, z ? 21 : i, 0.0f));
            TextView textView4 = new TextView(context);
            this.valueTextView = textView4;
            textView4.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2));
            this.valueTextView.setTextSize(1, 13.0f);
            this.valueTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setEllipsize(truncateAt);
            this.valueTextView.setPadding(0, 0, 0, 0);
            TextView textView5 = this.valueTextView;
            boolean z2 = LocaleController.isRTL;
            addView(textView5, LayoutHelper.createFrame(-2, -2.0f, (z2 ? 5 : 3) | 48, z2 ? i : 21, 35.0f, z2 ? 21 : i, 0.0f));
            ImageView imageView = new ImageView(context);
            this.checkImageView = imageView;
            imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_featuredStickers_addedIcon), PorterDuff.Mode.MULTIPLY));
            this.checkImageView.setImageResource(R.drawable.sticker_added);
            addView(this.checkImageView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 3 : 5) | 48, 21.0f, 25.0f, 21.0f, 0.0f));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.needDivider) {
                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.needDivider ? 1 : 0), 1073741824));
        }

        public void setChecked(boolean z) {
            this.checkImageView.setVisibility(z ? 0 : 4);
        }

        public void setNeedDivider(boolean z) {
            this.needDivider = z;
            setWillNotDraw(!z);
            invalidate();
        }

        public void setTextAndValue(String str, CharSequence charSequence, boolean z) {
            this.textView.setText(str);
            this.valueTextView.setText(charSequence);
            this.needDivider = z;
            setWillNotDraw(!z);
        }

        public void setValue(CharSequence charSequence) {
            this.valueTextView.setText(charSequence);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0148 A[Catch: Exception -> 0x020f, TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x015a  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x015f A[Catch: Exception -> 0x020f, TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x016d  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x016e A[Catch: Exception -> 0x020f, TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0178 A[Catch: Exception -> 0x020f, TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0182 A[Catch: Exception -> 0x020f, TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x018c A[Catch: Exception -> 0x020f, TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0196 A[Catch: Exception -> 0x020f, TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01a0 A[Catch: Exception -> 0x020f, TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01a9 A[Catch: Exception -> 0x020f, TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01b8 A[Catch: Exception -> 0x020f, TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01c8 A[Catch: Exception -> 0x020f, TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01d1  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x01df A[Catch: Exception -> 0x020f, TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01e8  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01f5 A[Catch: Exception -> 0x020f, PHI: r7 
      PHI: (r7v2 java.lang.String) = (r7v1 java.lang.String), (r7v3 java.lang.String), (r7v4 java.lang.String), (r7v5 java.lang.String) binds: [B:79:0x01b4, B:90:0x01f3, B:86:0x01dc, B:82:0x01c0] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {Exception -> 0x020f, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:46:0x013e, B:48:0x0148, B:52:0x015f, B:54:0x0166, B:55:0x016a, B:79:0x01b4, B:81:0x01b8, B:82:0x01c0, B:83:0x01c8, B:85:0x01d3, B:87:0x01df, B:89:0x01ea, B:91:0x01f5, B:93:0x01fb, B:57:0x016e, B:60:0x0178, B:63:0x0182, B:66:0x018c, B:69:0x0196, B:72:0x01a0, B:75:0x01a9, B:13:0x007b, B:15:0x007f, B:16:0x008f, B:18:0x0093, B:19:0x00a3, B:21:0x00a7, B:22:0x00b7, B:24:0x00bb, B:25:0x00c7, B:27:0x00cb, B:28:0x00db, B:30:0x00df, B:32:0x00ee, B:34:0x00f2, B:35:0x0103, B:37:0x0107, B:38:0x010a, B:40:0x0112, B:42:0x011e, B:44:0x0128), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01f9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public PassportActivity(int i, long j, String str, String str2, String str3, String str4, String str5, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$account_Password tLRPC$account_Password) {
        this(i, tLRPC$TL_account_authorizationForm, tLRPC$account_Password, (TLRPC$TL_secureRequiredType) null, (TLRPC$TL_secureValue) null, (TLRPC$TL_secureRequiredType) null, (TLRPC$TL_secureValue) null, (HashMap) null, (HashMap) null);
        int i2;
        String nameForType;
        byte[] bArr;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11;
        HashMap hashMap;
        char c;
        StringBuilder sb;
        this.currentBotId = j;
        this.currentPayload = str3;
        this.currentNonce = str4;
        this.currentScope = str;
        this.currentPublicKey = str2;
        this.currentCallbackUrl = str5;
        if (i != 0 || tLRPC$TL_account_authorizationForm.errors.isEmpty()) {
            return;
        }
        try {
            Collections.sort(tLRPC$TL_account_authorizationForm.errors, new Comparator() { // from class: org.telegram.ui.PassportActivity.2
                @Override // java.util.Comparator
                public int compare(TLRPC$SecureValueError tLRPC$SecureValueError, TLRPC$SecureValueError tLRPC$SecureValueError2) {
                    int errorValue = getErrorValue(tLRPC$SecureValueError);
                    int errorValue2 = getErrorValue(tLRPC$SecureValueError2);
                    if (errorValue < errorValue2) {
                        return -1;
                    }
                    return errorValue > errorValue2 ? 1 : 0;
                }

                int getErrorValue(TLRPC$SecureValueError tLRPC$SecureValueError) {
                    if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueError) {
                        return 0;
                    }
                    if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorFrontSide) {
                        return 1;
                    }
                    if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorReverseSide) {
                        return 2;
                    }
                    if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorSelfie) {
                        return 3;
                    }
                    if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorTranslationFile) {
                        return 4;
                    }
                    if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorTranslationFiles) {
                        return 5;
                    }
                    if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorFile) {
                        return 6;
                    }
                    if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorFiles) {
                        return 7;
                    }
                    if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorData) {
                        return PassportActivity.this.getFieldCost(((TLRPC$TL_secureValueErrorData) tLRPC$SecureValueError).field);
                    }
                    return 100;
                }
            });
            int size = tLRPC$TL_account_authorizationForm.errors.size();
            int i3 = 0;
            while (i3 < size) {
                TLRPC$SecureValueError tLRPC$SecureValueError = (TLRPC$SecureValueError) tLRPC$TL_account_authorizationForm.errors.get(i3);
                String str12 = null;
                str12 = null;
                str12 = null;
                str12 = null;
                str12 = null;
                byte[] bArr2 = null;
                str12 = null;
                String str13 = "error_all";
                if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorFrontSide) {
                    TLRPC$TL_secureValueErrorFrontSide tLRPC$TL_secureValueErrorFrontSide = (TLRPC$TL_secureValueErrorFrontSide) tLRPC$SecureValueError;
                    nameForType = getNameForType(tLRPC$TL_secureValueErrorFrontSide.type);
                    String str14 = tLRPC$TL_secureValueErrorFrontSide.text;
                    bArr = tLRPC$TL_secureValueErrorFrontSide.file_hash;
                    str6 = str14;
                    str7 = "front";
                } else if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorReverseSide) {
                    TLRPC$TL_secureValueErrorReverseSide tLRPC$TL_secureValueErrorReverseSide = (TLRPC$TL_secureValueErrorReverseSide) tLRPC$SecureValueError;
                    nameForType = getNameForType(tLRPC$TL_secureValueErrorReverseSide.type);
                    String str15 = tLRPC$TL_secureValueErrorReverseSide.text;
                    bArr = tLRPC$TL_secureValueErrorReverseSide.file_hash;
                    str6 = str15;
                    str7 = "reverse";
                } else if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorSelfie) {
                    TLRPC$TL_secureValueErrorSelfie tLRPC$TL_secureValueErrorSelfie = (TLRPC$TL_secureValueErrorSelfie) tLRPC$SecureValueError;
                    nameForType = getNameForType(tLRPC$TL_secureValueErrorSelfie.type);
                    String str16 = tLRPC$TL_secureValueErrorSelfie.text;
                    bArr = tLRPC$TL_secureValueErrorSelfie.file_hash;
                    str6 = str16;
                    str7 = "selfie";
                } else if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorTranslationFile) {
                    TLRPC$TL_secureValueErrorTranslationFile tLRPC$TL_secureValueErrorTranslationFile = (TLRPC$TL_secureValueErrorTranslationFile) tLRPC$SecureValueError;
                    nameForType = getNameForType(tLRPC$TL_secureValueErrorTranslationFile.type);
                    String str17 = tLRPC$TL_secureValueErrorTranslationFile.text;
                    bArr = tLRPC$TL_secureValueErrorTranslationFile.file_hash;
                    str6 = str17;
                    str7 = "translation";
                } else {
                    if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorTranslationFiles) {
                        TLRPC$TL_secureValueErrorTranslationFiles tLRPC$TL_secureValueErrorTranslationFiles = (TLRPC$TL_secureValueErrorTranslationFiles) tLRPC$SecureValueError;
                        nameForType = getNameForType(tLRPC$TL_secureValueErrorTranslationFiles.type);
                        str8 = tLRPC$TL_secureValueErrorTranslationFiles.text;
                        str9 = "translation";
                    } else if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorFile) {
                        TLRPC$TL_secureValueErrorFile tLRPC$TL_secureValueErrorFile = (TLRPC$TL_secureValueErrorFile) tLRPC$SecureValueError;
                        nameForType = getNameForType(tLRPC$TL_secureValueErrorFile.type);
                        String str18 = tLRPC$TL_secureValueErrorFile.text;
                        bArr = tLRPC$TL_secureValueErrorFile.file_hash;
                        str6 = str18;
                        str7 = "files";
                    } else if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorFiles) {
                        TLRPC$TL_secureValueErrorFiles tLRPC$TL_secureValueErrorFiles = (TLRPC$TL_secureValueErrorFiles) tLRPC$SecureValueError;
                        nameForType = getNameForType(tLRPC$TL_secureValueErrorFiles.type);
                        str8 = tLRPC$TL_secureValueErrorFiles.text;
                        str9 = "files";
                    } else if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueError) {
                        TLRPC$TL_secureValueError tLRPC$TL_secureValueError = (TLRPC$TL_secureValueError) tLRPC$SecureValueError;
                        nameForType = getNameForType(tLRPC$TL_secureValueError.type);
                        String str19 = tLRPC$TL_secureValueError.text;
                        bArr = tLRPC$TL_secureValueError.hash;
                        str6 = str19;
                        str7 = "error_all";
                    } else {
                        if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorData) {
                            TLRPC$TL_secureValueErrorData tLRPC$TL_secureValueErrorData = (TLRPC$TL_secureValueErrorData) tLRPC$SecureValueError;
                            int i4 = 0;
                            while (i4 < tLRPC$TL_account_authorizationForm.values.size()) {
                                TLRPC$TL_secureData tLRPC$TL_secureData = ((TLRPC$TL_secureValue) tLRPC$TL_account_authorizationForm.values.get(i4)).data;
                                if (tLRPC$TL_secureData == null || !Arrays.equals(tLRPC$TL_secureData.data_hash, tLRPC$TL_secureValueErrorData.data_hash)) {
                                    i4++;
                                    size = size;
                                } else {
                                    nameForType = getNameForType(tLRPC$TL_secureValueErrorData.type);
                                    String str20 = tLRPC$TL_secureValueErrorData.text;
                                    str12 = tLRPC$TL_secureValueErrorData.field;
                                    bArr = tLRPC$TL_secureValueErrorData.data_hash;
                                    str6 = str20;
                                    str7 = "data";
                                }
                            }
                        }
                        i2 = size;
                        i3++;
                        size = i2;
                    }
                    str10 = str9;
                    str11 = null;
                    hashMap = (HashMap) this.errorsMap.get(nameForType);
                    if (hashMap != null) {
                        hashMap = new HashMap();
                        i2 = size;
                        this.errorsMap.put(nameForType, hashMap);
                        this.mainErrorsMap.put(nameForType, str8);
                    } else {
                        i2 = size;
                    }
                    c = 2;
                    String encodeToString = bArr2 == null ? Base64.encodeToString(bArr2, 2) : "";
                    switch (str10.hashCode()) {
                        case -1840647503:
                            if (str10.equals("translation")) {
                                c = 3;
                                break;
                            }
                            c = 65535;
                            break;
                        case -906020504:
                            if (str10.equals("selfie")) {
                                break;
                            }
                            c = 65535;
                            break;
                        case 3076010:
                            if (str10.equals("data")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case 97434231:
                            if (str10.equals("files")) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 97705513:
                            if (str10.equals("front")) {
                                c = 4;
                                break;
                            }
                            c = 65535;
                            break;
                        case 329856746:
                            if (str10.equals("error_all")) {
                                c = 6;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1099846370:
                            if (str10.equals("reverse")) {
                                c = 5;
                                break;
                            }
                            c = 65535;
                            break;
                        default:
                            c = 65535;
                            break;
                    }
                    switch (c) {
                        case 0:
                            if (str11 == null) {
                                break;
                            } else {
                                hashMap.put(str11, str8);
                                break;
                            }
                        case 1:
                            if (bArr2 != null) {
                                sb = new StringBuilder();
                                sb.append("files");
                                sb.append(encodeToString);
                                str13 = sb.toString();
                                hashMap.put(str13, str8);
                                break;
                            } else {
                                str13 = "files_all";
                                hashMap.put(str13, str8);
                            }
                        case 2:
                            sb = new StringBuilder();
                            sb.append("selfie");
                            sb.append(encodeToString);
                            str13 = sb.toString();
                            hashMap.put(str13, str8);
                            break;
                        case 3:
                            if (bArr2 != null) {
                                sb = new StringBuilder();
                                sb.append("translation");
                                sb.append(encodeToString);
                                str13 = sb.toString();
                                hashMap.put(str13, str8);
                                break;
                            } else {
                                str13 = "translation_all";
                                hashMap.put(str13, str8);
                            }
                        case 4:
                            sb = new StringBuilder();
                            sb.append("front");
                            sb.append(encodeToString);
                            str13 = sb.toString();
                            hashMap.put(str13, str8);
                            break;
                        case 5:
                            sb = new StringBuilder();
                            sb.append("reverse");
                            sb.append(encodeToString);
                            str13 = sb.toString();
                            hashMap.put(str13, str8);
                            break;
                        case 6:
                            hashMap.put(str13, str8);
                            break;
                    }
                    i3++;
                    size = i2;
                }
                String str21 = str12;
                bArr2 = bArr;
                str10 = str7;
                str11 = str21;
                str8 = str6;
                hashMap = (HashMap) this.errorsMap.get(nameForType);
                if (hashMap != null) {
                }
                c = 2;
                if (bArr2 == null) {
                }
                switch (str10.hashCode()) {
                    case -1840647503:
                        break;
                    case -906020504:
                        break;
                    case 3076010:
                        break;
                    case 97434231:
                        break;
                    case 97705513:
                        break;
                    case 329856746:
                        break;
                    case 1099846370:
                        break;
                }
                switch (c) {
                }
                i3++;
                size = i2;
            }
        } catch (Exception unused) {
        }
    }

    public PassportActivity(int i, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$account_Password tLRPC$account_Password, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureValue tLRPC$TL_secureValue, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, TLRPC$TL_secureValue tLRPC$TL_secureValue2, HashMap hashMap, HashMap hashMap2) {
        this.currentCitizeship = "";
        this.currentResidence = "";
        this.currentExpireDate = new int[3];
        this.dividers = new ArrayList();
        this.nonLatinNames = new boolean[3];
        this.allowNonLatinName = true;
        this.countriesArray = new ArrayList();
        this.countriesMap = new HashMap();
        this.codesMap = new HashMap();
        this.phoneFormatMap = new HashMap();
        this.documents = new ArrayList();
        this.translationDocuments = new ArrayList();
        this.documentsCells = new HashMap();
        this.uploadingDocuments = new HashMap();
        this.typesValues = new HashMap();
        this.typesViews = new HashMap();
        this.documentsToTypesLink = new HashMap();
        this.errorsMap = new HashMap();
        this.mainErrorsMap = new HashMap();
        this.errorsValues = new HashMap();
        this.provider = new PhotoViewer.EmptyPhotoViewerProvider() { // from class: org.telegram.ui.PassportActivity.1
            /* JADX WARN: Removed duplicated region for block: B:36:0x00ce  */
            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void deleteImageAtIndex(int i2) {
                ArrayList arrayList;
                SecureDocument secureDocument;
                StringBuilder sb;
                String str;
                if (PassportActivity.this.uploadingFileType == 1) {
                    secureDocument = PassportActivity.this.selfieDocument;
                } else {
                    if (PassportActivity.this.uploadingFileType == 4) {
                        arrayList = PassportActivity.this.translationDocuments;
                    } else if (PassportActivity.this.uploadingFileType == 2) {
                        secureDocument = PassportActivity.this.frontDocument;
                    } else if (PassportActivity.this.uploadingFileType == 3) {
                        secureDocument = PassportActivity.this.reverseDocument;
                    } else {
                        arrayList = PassportActivity.this.documents;
                    }
                    secureDocument = (SecureDocument) arrayList.get(i2);
                }
                SecureDocumentCell secureDocumentCell = (SecureDocumentCell) PassportActivity.this.documentsCells.remove(secureDocument);
                if (secureDocumentCell == null) {
                    return;
                }
                String documentHash = PassportActivity.this.getDocumentHash(secureDocument);
                String str2 = null;
                if (PassportActivity.this.uploadingFileType == 1) {
                    PassportActivity.this.selfieDocument = null;
                    sb = new StringBuilder();
                    str = "selfie";
                } else if (PassportActivity.this.uploadingFileType == 4) {
                    sb = new StringBuilder();
                    str = "translation";
                } else if (PassportActivity.this.uploadingFileType == 2) {
                    PassportActivity.this.frontDocument = null;
                    sb = new StringBuilder();
                    str = "front";
                } else if (PassportActivity.this.uploadingFileType != 3) {
                    if (PassportActivity.this.uploadingFileType == 0) {
                        sb = new StringBuilder();
                        str = "files";
                    }
                    if (str2 != null) {
                        if (PassportActivity.this.documentsErrors != null) {
                            PassportActivity.this.documentsErrors.remove(str2);
                        }
                        if (PassportActivity.this.errorsValues != null) {
                            PassportActivity.this.errorsValues.remove(str2);
                        }
                    }
                    PassportActivity passportActivity = PassportActivity.this;
                    passportActivity.updateUploadText(passportActivity.uploadingFileType);
                    PassportActivity.this.currentPhotoViewerLayout.removeView(secureDocumentCell);
                } else {
                    PassportActivity.this.reverseDocument = null;
                    sb = new StringBuilder();
                    str = "reverse";
                }
                sb.append(str);
                sb.append(documentHash);
                str2 = sb.toString();
                if (str2 != null) {
                }
                PassportActivity passportActivity2 = PassportActivity.this;
                passportActivity2.updateUploadText(passportActivity2.uploadingFileType);
                PassportActivity.this.currentPhotoViewerLayout.removeView(secureDocumentCell);
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public String getDeleteMessageString() {
                return PassportActivity.this.uploadingFileType == 1 ? LocaleController.formatString("PassportDeleteSelfieAlert", R.string.PassportDeleteSelfieAlert, new Object[0]) : LocaleController.formatString("PassportDeleteScanAlert", R.string.PassportDeleteScanAlert, new Object[0]);
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i2, boolean z) {
                if (i2 < 0 || i2 >= PassportActivity.this.currentPhotoViewerLayout.getChildCount()) {
                    return null;
                }
                SecureDocumentCell secureDocumentCell = (SecureDocumentCell) PassportActivity.this.currentPhotoViewerLayout.getChildAt(i2);
                int[] iArr = new int[2];
                secureDocumentCell.imageView.getLocationInWindow(iArr);
                PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                placeProviderObject.viewX = iArr[0];
                placeProviderObject.viewY = iArr[1] - (Build.VERSION.SDK_INT < 21 ? AndroidUtilities.statusBarHeight : 0);
                placeProviderObject.parentView = PassportActivity.this.currentPhotoViewerLayout;
                ImageReceiver imageReceiver = secureDocumentCell.imageView.getImageReceiver();
                placeProviderObject.imageReceiver = imageReceiver;
                placeProviderObject.thumb = imageReceiver.getBitmapSafe();
                return placeProviderObject;
            }
        };
        this.currentActivityType = i;
        this.currentForm = tLRPC$TL_account_authorizationForm;
        this.currentType = tLRPC$TL_secureRequiredType;
        if (tLRPC$TL_secureRequiredType != null) {
            this.allowNonLatinName = tLRPC$TL_secureRequiredType.native_names;
        }
        this.currentTypeValue = tLRPC$TL_secureValue;
        this.currentDocumentsType = tLRPC$TL_secureRequiredType2;
        this.currentDocumentsTypeValue = tLRPC$TL_secureValue2;
        this.currentPassword = tLRPC$account_Password;
        this.currentValues = hashMap;
        this.currentDocumentValues = hashMap2;
        if (i == 3) {
            this.permissionsItems = new ArrayList();
        } else if (i == 7) {
            this.views = new SlideView[3];
        }
        if (this.currentValues == null) {
            this.currentValues = new HashMap();
        }
        if (this.currentDocumentValues == null) {
            this.currentDocumentValues = new HashMap();
        }
        if (i == 5) {
            if (UserConfig.getInstance(this.currentAccount).savedPasswordHash != null && UserConfig.getInstance(this.currentAccount).savedSaltedPassword != null) {
                this.usingSavedPassword = 1;
                this.savedPasswordHash = UserConfig.getInstance(this.currentAccount).savedPasswordHash;
                this.savedSaltedPassword = UserConfig.getInstance(this.currentAccount).savedSaltedPassword;
            }
            TLRPC$account_Password tLRPC$account_Password2 = this.currentPassword;
            if (tLRPC$account_Password2 == null) {
                loadPasswordInfo();
            } else {
                TwoStepVerificationActivity.initPasswordNewAlgo(tLRPC$account_Password2);
                if (this.usingSavedPassword == 1) {
                    onPasswordDone(true);
                }
            }
            if (SharedConfig.isPassportConfigLoaded()) {
                return;
            }
            TLRPC$TL_help_getPassportConfig tLRPC$TL_help_getPassportConfig = new TLRPC$TL_help_getPassportConfig();
            tLRPC$TL_help_getPassportConfig.hash = SharedConfig.passportConfigHash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_help_getPassportConfig, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda2
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    PassportActivity.lambda$new$1(tLObject, tLRPC$TL_error);
                }
            });
        }
    }

    private void addDocumentView(final SecureDocument secureDocument, final int i) {
        String string;
        StringBuilder sb;
        String str;
        String formatDateForBan;
        HashMap hashMap;
        if (i == 1) {
            this.selfieDocument = secureDocument;
            if (this.selfieLayout == null) {
                return;
            }
        } else if (i == 4) {
            this.translationDocuments.add(secureDocument);
            if (this.translationLayout == null) {
                return;
            }
        } else if (i == 2) {
            this.frontDocument = secureDocument;
            if (this.frontLayout == null) {
                return;
            }
        } else if (i == 3) {
            this.reverseDocument = secureDocument;
            if (this.reverseLayout == null) {
                return;
            }
        } else {
            this.documents.add(secureDocument);
            if (this.documentsLayout == null) {
                return;
            }
        }
        if (getParentActivity() == null) {
            return;
        }
        final SecureDocumentCell secureDocumentCell = new SecureDocumentCell(getParentActivity());
        secureDocumentCell.setTag(secureDocument);
        secureDocumentCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.documentsCells.put(secureDocument, secureDocumentCell);
        String documentHash = getDocumentHash(secureDocument);
        if (i == 1) {
            string = LocaleController.getString(R.string.PassportSelfie);
            this.selfieLayout.addView(secureDocumentCell, LayoutHelper.createLinear(-1, -2));
            sb = new StringBuilder();
            str = "selfie";
        } else if (i == 4) {
            string = LocaleController.getString(R.string.AttachPhoto);
            this.translationLayout.addView(secureDocumentCell, LayoutHelper.createLinear(-1, -2));
            sb = new StringBuilder();
            str = "translation";
        } else if (i == 2) {
            TLRPC$SecureValueType tLRPC$SecureValueType = this.currentDocumentsType.type;
            string = LocaleController.getString(((tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport)) ? R.string.PassportMainPage : R.string.PassportFrontSide);
            this.frontLayout.addView(secureDocumentCell, LayoutHelper.createLinear(-1, -2));
            sb = new StringBuilder();
            str = "front";
        } else if (i == 3) {
            string = LocaleController.getString(R.string.PassportReverseSide);
            this.reverseLayout.addView(secureDocumentCell, LayoutHelper.createLinear(-1, -2));
            sb = new StringBuilder();
            str = "reverse";
        } else {
            string = LocaleController.getString(R.string.AttachPhoto);
            this.documentsLayout.addView(secureDocumentCell, LayoutHelper.createLinear(-1, -2));
            sb = new StringBuilder();
            str = "files";
        }
        sb.append(str);
        sb.append(documentHash);
        final String sb2 = sb.toString();
        if (sb2 == null || (hashMap = this.documentsErrors) == null || (formatDateForBan = (String) hashMap.get(sb2)) == null) {
            formatDateForBan = LocaleController.formatDateForBan(secureDocument.secureFile.date);
        } else {
            secureDocumentCell.valueTextView.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
            this.errorsValues.put(sb2, "");
        }
        secureDocumentCell.setTextAndValueAndImage(string, formatDateForBan, secureDocument);
        secureDocumentCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda46
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PassportActivity.this.lambda$addDocumentView$55(i, view);
            }
        });
        secureDocumentCell.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda47
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$addDocumentView$57;
                lambda$addDocumentView$57 = PassportActivity.this.lambda$addDocumentView$57(i, secureDocument, secureDocumentCell, sb2, view);
                return lambda$addDocumentView$57;
            }
        });
    }

    private void addDocumentViewInternal(TLRPC$TL_secureFile tLRPC$TL_secureFile, int i) {
        addDocumentView(new SecureDocument(getSecureDocumentKey(tLRPC$TL_secureFile.secret, tLRPC$TL_secureFile.file_hash), tLRPC$TL_secureFile, null, null, null), i);
    }

    private void addDocumentViews(ArrayList arrayList) {
        this.documents.clear();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLRPC$SecureFile tLRPC$SecureFile = (TLRPC$SecureFile) arrayList.get(i);
            if (tLRPC$SecureFile instanceof TLRPC$TL_secureFile) {
                addDocumentViewInternal((TLRPC$TL_secureFile) tLRPC$SecureFile, 0);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:64:0x014f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TextDetailSecureCell addField(Context context, final TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, final ArrayList arrayList, final boolean z, boolean z2) {
        int i;
        String formatString;
        String str;
        String str2;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2;
        int i2;
        TLRPC$TL_secureValue valueByType;
        LinearLayout linearLayout;
        int size = arrayList != null ? arrayList.size() : 0;
        TextDetailSecureCell textDetailSecureCell = new TextDetailSecureCell(context);
        textDetailSecureCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
            if (arrayList == null || arrayList.isEmpty()) {
                i = R.string.PassportPersonalDetails;
            } else {
                if (!z || arrayList.size() != 1) {
                    if (z && arrayList.size() == 2) {
                        formatString = LocaleController.formatString("PassportTwoDocuments", R.string.PassportTwoDocuments, getTextForType(((TLRPC$TL_secureRequiredType) arrayList.get(0)).type), getTextForType(((TLRPC$TL_secureRequiredType) arrayList.get(1)).type));
                        textDetailSecureCell.setTextAndValue(formatString, "", !z2);
                    } else {
                        i = R.string.PassportIdentityDocument;
                    }
                }
                formatString = getTextForType(((TLRPC$TL_secureRequiredType) arrayList.get(0)).type);
                textDetailSecureCell.setTextAndValue(formatString, "", !z2);
            }
            formatString = LocaleController.getString(i);
            textDetailSecureCell.setTextAndValue(formatString, "", !z2);
        } else {
            if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
                if (arrayList == null || arrayList.isEmpty()) {
                    i = R.string.PassportAddress;
                } else {
                    if (!z || arrayList.size() != 1) {
                        if (z && arrayList.size() == 2) {
                            formatString = LocaleController.formatString("PassportTwoDocuments", R.string.PassportTwoDocuments, getTextForType(((TLRPC$TL_secureRequiredType) arrayList.get(0)).type), getTextForType(((TLRPC$TL_secureRequiredType) arrayList.get(1)).type));
                            textDetailSecureCell.setTextAndValue(formatString, "", !z2);
                        } else {
                            i = R.string.PassportResidentialAddress;
                        }
                    }
                    formatString = getTextForType(((TLRPC$TL_secureRequiredType) arrayList.get(0)).type);
                    textDetailSecureCell.setTextAndValue(formatString, "", !z2);
                }
            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone) {
                i = R.string.PassportPhone;
            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail) {
                i = R.string.PassportEmail;
            }
            formatString = LocaleController.getString(i);
            textDetailSecureCell.setTextAndValue(formatString, "", !z2);
        }
        if (this.currentActivityType == 8) {
            this.linearLayout2.addView(textDetailSecureCell, linearLayout.getChildCount() - 5, LayoutHelper.createLinear(-1, -2));
        } else {
            this.linearLayout2.addView(textDetailSecureCell, LayoutHelper.createLinear(-1, -2));
        }
        textDetailSecureCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda56
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PassportActivity.this.lambda$addField$64(arrayList, tLRPC$TL_secureRequiredType, z, view);
            }
        });
        this.typesViews.put(tLRPC$TL_secureRequiredType, textDetailSecureCell);
        this.typesValues.put(tLRPC$TL_secureRequiredType, new HashMap());
        TLRPC$TL_secureValue valueByType2 = getValueByType(tLRPC$TL_secureRequiredType, false);
        String str3 = null;
        if (valueByType2 != null) {
            TLRPC$SecurePlainData tLRPC$SecurePlainData = valueByType2.plain_data;
            if (tLRPC$SecurePlainData instanceof TLRPC$TL_securePlainEmail) {
                str = ((TLRPC$TL_securePlainEmail) tLRPC$SecurePlainData).email;
            } else if (tLRPC$SecurePlainData instanceof TLRPC$TL_securePlainPhone) {
                str = ((TLRPC$TL_securePlainPhone) tLRPC$SecurePlainData).phone;
            } else {
                TLRPC$TL_secureData tLRPC$TL_secureData = valueByType2.data;
                if (tLRPC$TL_secureData != null) {
                    str2 = decryptData(tLRPC$TL_secureData.data, decryptValueSecret(tLRPC$TL_secureData.secret, tLRPC$TL_secureData.data_hash), valueByType2.data.data_hash);
                    str = null;
                    if (arrayList != null || arrayList.isEmpty()) {
                        tLRPC$TL_secureRequiredType2 = null;
                    } else {
                        int size2 = arrayList.size();
                        String str4 = null;
                        int i3 = 0;
                        boolean z3 = false;
                        while (i3 < size2) {
                            TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3 = (TLRPC$TL_secureRequiredType) arrayList.get(i3);
                            this.typesValues.put(tLRPC$TL_secureRequiredType3, new HashMap());
                            this.documentsToTypesLink.put(tLRPC$TL_secureRequiredType3, tLRPC$TL_secureRequiredType);
                            if (z3 || (valueByType = getValueByType(tLRPC$TL_secureRequiredType3, false)) == null) {
                                i2 = 1;
                            } else {
                                TLRPC$TL_secureData tLRPC$TL_secureData2 = valueByType.data;
                                if (tLRPC$TL_secureData2 != null) {
                                    str4 = decryptData(tLRPC$TL_secureData2.data, decryptValueSecret(tLRPC$TL_secureData2.secret, tLRPC$TL_secureData2.data_hash), valueByType.data.data_hash);
                                }
                                str3 = tLRPC$TL_secureRequiredType3;
                                i2 = 1;
                                z3 = true;
                            }
                            i3 += i2;
                        }
                        tLRPC$TL_secureRequiredType2 = str3 == null ? (TLRPC$TL_secureRequiredType) arrayList.get(0) : str3;
                        str3 = str4;
                    }
                    setTypeValue(tLRPC$TL_secureRequiredType, str, str2, tLRPC$TL_secureRequiredType2, str3, z, size);
                    return textDetailSecureCell;
                }
            }
            str2 = null;
            if (arrayList != null) {
            }
            tLRPC$TL_secureRequiredType2 = null;
            setTypeValue(tLRPC$TL_secureRequiredType, str, str2, tLRPC$TL_secureRequiredType2, str3, z, size);
            return textDetailSecureCell;
        }
        str = null;
        str2 = null;
        if (arrayList != null) {
        }
        tLRPC$TL_secureRequiredType2 = null;
        setTypeValue(tLRPC$TL_secureRequiredType, str, str2, tLRPC$TL_secureRequiredType2, str3, z, size);
        return textDetailSecureCell;
    }

    private void addTranslationDocumentViews(ArrayList arrayList) {
        this.translationDocuments.clear();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLRPC$SecureFile tLRPC$SecureFile = (TLRPC$SecureFile) arrayList.get(i);
            if (tLRPC$SecureFile instanceof TLRPC$TL_secureFile) {
                addDocumentViewInternal((TLRPC$TL_secureFile) tLRPC$SecureFile, 4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callCallback(boolean z) {
        int i;
        int i2;
        Activity parentActivity;
        StringBuilder sb;
        String str;
        if (this.callbackCalled) {
            return;
        }
        if (!TextUtils.isEmpty(this.currentCallbackUrl)) {
            if (z) {
                parentActivity = getParentActivity();
                sb = new StringBuilder();
                sb.append(this.currentCallbackUrl);
                str = "&tg_passport=success";
            } else if (!this.ignoreOnFailure && ((i2 = this.currentActivityType) == 5 || i2 == 0)) {
                parentActivity = getParentActivity();
                sb = new StringBuilder();
                sb.append(this.currentCallbackUrl);
                str = "&tg_passport=cancel";
            }
            sb.append(str);
            Browser.openUrl(parentActivity, Uri.parse(sb.toString()));
        } else if (!this.needActivityResult) {
            return;
        } else {
            if (z || (!this.ignoreOnFailure && ((i = this.currentActivityType) == 5 || i == 0))) {
                getParentActivity().setResult(z ? -1 : 0);
            }
        }
        this.callbackCalled = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkDiscard() {
        if (isHasNotAnyChanges()) {
            return false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString(R.string.PassportDiscard), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda31
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PassportActivity.this.lambda$checkDiscard$69(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setTitle(LocaleController.getString(R.string.DiscardChanges));
        builder.setMessage(LocaleController.getString(R.string.PassportDiscardChanges));
        showDialog(builder.create());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x002c, code lost:
        if (r3 != null) goto L28;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0039  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void checkFieldForError(EditTextBoldCursor editTextBoldCursor, String str, Editable editable, boolean z) {
        String str2;
        HashMap hashMap;
        String str3;
        String str4;
        HashMap hashMap2 = this.errorsValues;
        if (hashMap2 != null && (str3 = (String) hashMap2.get(str)) != null && TextUtils.equals(str3, editable)) {
            HashMap hashMap3 = this.fieldsErrors;
            if (hashMap3 == null || (str4 = (String) hashMap3.get(str)) == null) {
                HashMap hashMap4 = this.documentsErrors;
                if (hashMap4 != null) {
                    str2 = (String) hashMap4.get(str);
                }
            } else {
                editTextBoldCursor.setErrorText(str4);
            }
            String str5 = !z ? "error_document_all" : "error_all";
            hashMap = this.errorsValues;
            if (hashMap == null && hashMap.containsKey(str5)) {
                this.errorsValues.remove(str5);
                checkTopErrorCell(false);
                return;
            }
        }
        str2 = null;
        editTextBoldCursor.setErrorText(str2);
        if (!z) {
        }
        hashMap = this.errorsValues;
        if (hashMap == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x01e8, code lost:
        if (r6 != 5) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0263, code lost:
        if (r8 > 24) goto L164;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x026d, code lost:
        if (r8 < 2) goto L164;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x027c, code lost:
        if (r8 < 2) goto L164;
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x0285, code lost:
        if (r8 > 10) goto L164;
     */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0200  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x028b A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean checkFieldsForError() {
        TextSettingsCell[] textSettingsCellArr;
        boolean z;
        String str;
        TextSettingsCell textSettingsCell;
        TextDetailSettingsCell textDetailSettingsCell;
        HashMap hashMap;
        SecureDocument secureDocument;
        SecureDocument secureDocument2;
        Object obj;
        if (this.currentDocumentsType != null) {
            if (this.errorsValues.containsKey("error_all") || this.errorsValues.containsKey("error_document_all")) {
                textSettingsCell = this.topErrorCell;
            } else {
                if (this.uploadDocumentCell != null) {
                    if (!this.documents.isEmpty()) {
                        int size = this.documents.size();
                        for (int i = 0; i < size; i++) {
                            secureDocument2 = (SecureDocument) this.documents.get(i);
                            String str2 = "files" + getDocumentHash(secureDocument2);
                            if (str2 != null && this.errorsValues.containsKey(str2)) {
                                break;
                            }
                        }
                    } else {
                        textSettingsCell = this.uploadDocumentCell;
                    }
                }
                if (this.errorsValues.containsKey("files_all") || this.errorsValues.containsKey("translation_all")) {
                    textSettingsCell = this.bottomCell;
                } else {
                    TextDetailSettingsCell textDetailSettingsCell2 = this.uploadFrontCell;
                    if (textDetailSettingsCell2 != null) {
                        textSettingsCell = textDetailSettingsCell2;
                        if (this.frontDocument != null) {
                            if (this.errorsValues.containsKey("front" + getDocumentHash(this.frontDocument))) {
                                hashMap = this.documentsCells;
                                secureDocument = this.frontDocument;
                                obj = hashMap.get(secureDocument);
                                textSettingsCell = (View) obj;
                                break;
                            }
                        }
                    }
                    TLRPC$SecureValueType tLRPC$SecureValueType = this.currentDocumentsType.type;
                    if (((tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense)) && (textDetailSettingsCell = this.uploadReverseCell) != null) {
                        textSettingsCell = textDetailSettingsCell;
                        if (this.reverseDocument != null) {
                            if (this.errorsValues.containsKey("reverse" + getDocumentHash(this.reverseDocument))) {
                                hashMap = this.documentsCells;
                                secureDocument = this.reverseDocument;
                                obj = hashMap.get(secureDocument);
                                textSettingsCell = (View) obj;
                                break;
                            }
                        }
                    }
                    TextDetailSettingsCell textDetailSettingsCell3 = this.uploadSelfieCell;
                    if (textDetailSettingsCell3 != null && this.currentBotId != 0) {
                        textSettingsCell = textDetailSettingsCell3;
                        if (this.selfieDocument != null) {
                            if (this.errorsValues.containsKey("selfie" + getDocumentHash(this.selfieDocument))) {
                                hashMap = this.documentsCells;
                                secureDocument = this.selfieDocument;
                                obj = hashMap.get(secureDocument);
                                textSettingsCell = (View) obj;
                                break;
                            }
                        }
                    }
                    if (this.uploadTranslationCell != null && this.currentBotId != 0) {
                        if (this.translationDocuments.isEmpty()) {
                            textSettingsCell = this.uploadTranslationCell;
                        } else {
                            int size2 = this.translationDocuments.size();
                            for (int i2 = 0; i2 < size2; i2++) {
                                secureDocument2 = (SecureDocument) this.translationDocuments.get(i2);
                                if (this.errorsValues.containsKey("translation" + getDocumentHash(secureDocument2))) {
                                    obj = this.documentsCells.get(secureDocument2);
                                    textSettingsCell = (View) obj;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            onFieldError(textSettingsCell);
            return true;
        }
        for (int i3 = 0; i3 < 2; i3++) {
            if (i3 == 0) {
                textSettingsCellArr = this.inputFields;
            } else {
                TextInfoPrivacyCell textInfoPrivacyCell = this.nativeInfoCell;
                textSettingsCellArr = (textInfoPrivacyCell == null || textInfoPrivacyCell.getVisibility() != 0) ? null : this.inputExtraFields;
            }
            if (textSettingsCellArr != null) {
                int i4 = 0;
                while (i4 < textSettingsCellArr.length) {
                    boolean hasErrorText = textSettingsCellArr[i4].hasErrorText();
                    if (!this.errorsValues.isEmpty()) {
                        TLRPC$SecureValueType tLRPC$SecureValueType2 = this.currentType.type;
                        if (!(tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypePersonalDetails)) {
                            if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeAddress) {
                                if (i4 == 0) {
                                    str = "street_line1";
                                } else if (i4 == 1) {
                                    str = "street_line2";
                                } else if (i4 == 2) {
                                    str = "post_code";
                                } else if (i4 == 3) {
                                    str = "city";
                                } else if (i4 == 4) {
                                    str = "state";
                                }
                                if (str != null) {
                                }
                            }
                            str = null;
                            if (str != null) {
                            }
                        } else if (i3 == 0) {
                            switch (i4) {
                                case 0:
                                    str = "first_name";
                                    break;
                                case 1:
                                    str = "middle_name";
                                    break;
                                case 2:
                                    str = "last_name";
                                    break;
                                case 3:
                                    str = "birth_date";
                                    break;
                                case 4:
                                    str = "gender";
                                    break;
                                case 5:
                                    str = "country_code";
                                    break;
                                case 6:
                                    str = "residence_country_code";
                                    break;
                                case 7:
                                    str = "document_no";
                                    break;
                                case 8:
                                    str = "expiry_date";
                                    break;
                                default:
                                    str = null;
                                    break;
                            }
                            if (str != null) {
                                String str3 = (String) this.errorsValues.get(str);
                                if (!TextUtils.isEmpty(str3) && str3.equals(textSettingsCellArr[i4].getText().toString())) {
                                    hasErrorText = true;
                                }
                            }
                        } else {
                            if (i4 == 0) {
                                str = "first_name_native";
                            } else if (i4 != 1) {
                                if (i4 == 2) {
                                    str = "last_name_native";
                                }
                                str = null;
                            } else {
                                str = "middle_name_native";
                            }
                            if (str != null) {
                            }
                        }
                        onFieldError(textSettingsCell);
                        return true;
                    }
                    if (!this.documentOnly || this.currentDocumentsType == null || i4 >= 7) {
                        if (!hasErrorText) {
                            int length = textSettingsCellArr[i4].length();
                            int i5 = this.currentActivityType;
                            if (i5 != 1) {
                                if (i5 == 2) {
                                    if (i4 == 1) {
                                        continue;
                                    } else if (i4 != 3) {
                                        if (i4 == 4) {
                                            if (!"US".equals(this.currentCitizeship)) {
                                                continue;
                                            }
                                        } else if (i4 == 2) {
                                            if (length >= 2) {
                                            }
                                            hasErrorText = true;
                                        }
                                    }
                                }
                                z = false;
                                if (!hasErrorText) {
                                }
                            } else if (i4 != 8) {
                                if ((i3 == 0 && (i4 == 0 || i4 == 2 || i4 == 1)) || (i3 == 1 && (i4 == 0 || i4 == 1 || i4 == 2))) {
                                    if (length > 255) {
                                        hasErrorText = true;
                                    }
                                    if ((i3 == 0 && i4 == 1) || (i3 == 1 && i4 == 1)) {
                                        z = true;
                                        if (!hasErrorText && !z && length == 0) {
                                            hasErrorText = true;
                                        }
                                    }
                                } else if (i4 == 7) {
                                }
                                z = false;
                                if (!hasErrorText) {
                                    hasErrorText = true;
                                }
                            } else {
                                continue;
                            }
                        }
                        if (hasErrorText) {
                            textSettingsCell = textSettingsCellArr[i4];
                            onFieldError(textSettingsCell);
                            return true;
                        }
                    }
                    i4++;
                }
                continue;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkNativeFields(boolean z) {
        ShadowSectionCell shadowSectionCell;
        int i;
        Activity activity;
        EditTextBoldCursor editTextBoldCursor;
        String formatString;
        int i2;
        EditTextBoldCursor[] editTextBoldCursorArr;
        int i3 = 0;
        if (this.inputExtraFields == null) {
            return;
        }
        String str = (String) this.languageMap.get(this.currentResidence);
        String str2 = SharedConfig.getCountryLangs().get(this.currentResidence);
        if (!this.currentType.native_names || TextUtils.isEmpty(this.currentResidence) || "EN".equals(str2)) {
            if (this.nativeInfoCell.getVisibility() != 8) {
                this.nativeInfoCell.setVisibility(8);
                this.headerCell.setVisibility(8);
                this.extraBackgroundView2.setVisibility(8);
                while (true) {
                    EditTextBoldCursor[] editTextBoldCursorArr2 = this.inputExtraFields;
                    if (i3 >= editTextBoldCursorArr2.length) {
                        break;
                    }
                    ((View) editTextBoldCursorArr2[i3].getParent()).setVisibility(8);
                    i3++;
                }
                if (((this.currentBotId == 0 && this.currentDocumentsType != null) || this.currentTypeValue == null || this.documentOnly) && this.currentDocumentsTypeValue == null) {
                    shadowSectionCell = this.sectionCell2;
                    activity = getParentActivity();
                    i = R.drawable.greydivider_bottom;
                } else {
                    shadowSectionCell = this.sectionCell2;
                    activity = getParentActivity();
                    i = R.drawable.greydivider;
                }
                shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(activity, i, Theme.key_windowBackgroundGrayShadow));
                return;
            }
            return;
        }
        if (this.nativeInfoCell.getVisibility() != 0) {
            this.nativeInfoCell.setVisibility(0);
            this.headerCell.setVisibility(0);
            this.extraBackgroundView2.setVisibility(0);
            int i4 = 0;
            while (true) {
                editTextBoldCursorArr = this.inputExtraFields;
                if (i4 >= editTextBoldCursorArr.length) {
                    break;
                }
                ((View) editTextBoldCursorArr[i4].getParent()).setVisibility(0);
                i4++;
            }
            if (editTextBoldCursorArr[0].length() == 0 && this.inputExtraFields[1].length() == 0 && this.inputExtraFields[2].length() == 0) {
                int i5 = 0;
                while (true) {
                    boolean[] zArr = this.nonLatinNames;
                    if (i5 >= zArr.length) {
                        break;
                    } else if (zArr[i5]) {
                        this.inputExtraFields[0].setText(this.inputFields[0].getText());
                        this.inputExtraFields[1].setText(this.inputFields[1].getText());
                        this.inputExtraFields[2].setText(this.inputFields[2].getText());
                        break;
                    } else {
                        i5++;
                    }
                }
            }
            this.sectionCell2.setBackgroundDrawable(Theme.getThemedDrawableByKey(getParentActivity(), R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
        }
        this.nativeInfoCell.setText(LocaleController.formatString("PassportNativeInfo", R.string.PassportNativeInfo, str));
        String serverString = str2 != null ? LocaleController.getServerString("PassportLanguage_" + str2) : null;
        this.headerCell.setText(serverString != null ? LocaleController.formatString("PassportNativeHeaderLang", R.string.PassportNativeHeaderLang, serverString) : LocaleController.getString(R.string.PassportNativeHeader));
        for (int i6 = 0; i6 < 3; i6++) {
            if (i6 == 0) {
                EditTextBoldCursor[] editTextBoldCursorArr3 = this.inputExtraFields;
                if (serverString != null) {
                    editTextBoldCursor = editTextBoldCursorArr3[i6];
                    i2 = R.string.PassportName;
                    formatString = LocaleController.getString(i2);
                    editTextBoldCursor.setHintText(formatString);
                } else {
                    editTextBoldCursor = editTextBoldCursorArr3[i6];
                    formatString = LocaleController.formatString("PassportNameCountry", R.string.PassportNameCountry, str);
                    editTextBoldCursor.setHintText(formatString);
                }
            } else if (i6 == 1) {
                EditTextBoldCursor[] editTextBoldCursorArr4 = this.inputExtraFields;
                if (serverString != null) {
                    editTextBoldCursor = editTextBoldCursorArr4[i6];
                    i2 = R.string.PassportMidname;
                    formatString = LocaleController.getString(i2);
                    editTextBoldCursor.setHintText(formatString);
                } else {
                    editTextBoldCursor = editTextBoldCursorArr4[i6];
                    formatString = LocaleController.formatString("PassportMidnameCountry", R.string.PassportMidnameCountry, str);
                    editTextBoldCursor.setHintText(formatString);
                }
            } else if (i6 == 2) {
                EditTextBoldCursor[] editTextBoldCursorArr5 = this.inputExtraFields;
                if (serverString != null) {
                    editTextBoldCursor = editTextBoldCursorArr5[i6];
                    i2 = R.string.PassportSurname;
                    formatString = LocaleController.getString(i2);
                    editTextBoldCursor.setHintText(formatString);
                } else {
                    editTextBoldCursor = editTextBoldCursorArr5[i6];
                    formatString = LocaleController.formatString("PassportSurnameCountry", R.string.PassportSurnameCountry, str);
                    editTextBoldCursor.setHintText(formatString);
                }
            }
        }
        if (z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda44
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.this.lambda$checkNativeFields$58();
                }
            });
        }
    }

    public static boolean checkSecret(byte[] bArr, Long l) {
        if (bArr == null || bArr.length != 32) {
            return false;
        }
        int i = 0;
        for (byte b : bArr) {
            i += b & 255;
        }
        if (i % NotificationCenter.didClearDatabase != 239) {
            return false;
        }
        return l == null || Utilities.bytesToLong(Utilities.computeSHA256(bArr)) == l.longValue();
    }

    private void checkTopErrorCell(boolean z) {
        SpannableStringBuilder spannableStringBuilder;
        String str;
        String str2;
        if (this.topErrorCell == null) {
            return;
        }
        if (this.fieldsErrors == null || (!(z || this.errorsValues.containsKey("error_all")) || (str2 = (String) this.fieldsErrors.get("error_all")) == null)) {
            spannableStringBuilder = null;
        } else {
            spannableStringBuilder = new SpannableStringBuilder(str2);
            if (z) {
                this.errorsValues.put("error_all", "");
            }
        }
        if (this.documentsErrors != null && ((z || this.errorsValues.containsKey("error_document_all")) && (str = (String) this.documentsErrors.get("error_all")) != null)) {
            if (spannableStringBuilder == null) {
                spannableStringBuilder = new SpannableStringBuilder(str);
            } else {
                spannableStringBuilder.append((CharSequence) "\n\n").append((CharSequence) str);
            }
            if (z) {
                this.errorsValues.put("error_document_all", "");
            }
        }
        if (spannableStringBuilder != null) {
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_text_RedRegular)), 0, spannableStringBuilder.length(), 33);
            this.topErrorCell.setText(spannableStringBuilder);
            this.topErrorCell.setVisibility(0);
        } else if (this.topErrorCell.getVisibility() != 8) {
            this.topErrorCell.setVisibility(8);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0124  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0160  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01a4  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x024f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void createAddressInterface(Context context) {
        final String str;
        int i;
        ActionBar actionBar;
        int i2;
        int i3;
        HashMap hashMap;
        int i4;
        HashMap hashMap2;
        this.languageMap = new HashMap();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("countries.txt")));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String[] split = readLine.split(";");
                this.languageMap.put(split[1], split[2]);
            }
            bufferedReader.close();
        } catch (Exception e) {
            FileLog.e(e);
        }
        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
        this.topErrorCell = textInfoPrivacyCell;
        int i5 = R.drawable.greydivider_top;
        int i6 = Theme.key_windowBackgroundGrayShadow;
        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i5, i6));
        this.topErrorCell.setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
        this.linearLayout2.addView(this.topErrorCell, LayoutHelper.createLinear(-1, -2));
        checkTopErrorCell(true);
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType = this.currentDocumentsType;
        if (tLRPC$TL_secureRequiredType != null) {
            TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
            if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                actionBar = this.actionBar;
                i2 = R.string.ActionBotDocumentRentalAgreement;
            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement) {
                actionBar = this.actionBar;
                i2 = R.string.ActionBotDocumentBankStatement;
            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                actionBar = this.actionBar;
                i2 = R.string.ActionBotDocumentUtilityBill;
            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                actionBar = this.actionBar;
                i2 = R.string.ActionBotDocumentPassportRegistration;
            } else {
                if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                    actionBar = this.actionBar;
                    i2 = R.string.ActionBotDocumentTemporaryRegistration;
                }
                HeaderCell headerCell = new HeaderCell(context);
                this.headerCell = headerCell;
                headerCell.setText(LocaleController.getString(R.string.PassportDocuments));
                HeaderCell headerCell2 = this.headerCell;
                int i7 = Theme.key_windowBackgroundWhite;
                headerCell2.setBackgroundColor(Theme.getColor(i7));
                this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
                LinearLayout linearLayout = new LinearLayout(context);
                this.documentsLayout = linearLayout;
                linearLayout.setOrientation(1);
                this.linearLayout2.addView(this.documentsLayout, LayoutHelper.createLinear(-1, -2));
                TextSettingsCell textSettingsCell = new TextSettingsCell(context);
                this.uploadDocumentCell = textSettingsCell;
                textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.linearLayout2.addView(this.uploadDocumentCell, LayoutHelper.createLinear(-1, -2));
                this.uploadDocumentCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda26
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PassportActivity.this.lambda$createAddressInterface$32(view);
                    }
                });
                TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
                this.bottomCell = textInfoPrivacyCell2;
                int i8 = R.drawable.greydivider;
                textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i8, i6));
                if (this.currentBotId == 0) {
                    i3 = R.string.PassportAddAddressUploadInfo;
                } else {
                    TLRPC$SecureValueType tLRPC$SecureValueType2 = this.currentDocumentsType.type;
                    if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                        i3 = R.string.PassportAddAgreementInfo;
                    } else if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                        i3 = R.string.PassportAddBillInfo;
                    } else if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                        i3 = R.string.PassportAddPassportRegistrationInfo;
                    } else if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                        i3 = R.string.PassportAddTemporaryRegistrationInfo;
                    } else if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeBankStatement) {
                        i3 = R.string.PassportAddBankInfo;
                    } else {
                        this.noAllDocumentsErrorText = "";
                        CharSequence charSequence = this.noAllDocumentsErrorText;
                        hashMap = this.documentsErrors;
                        SpannableStringBuilder spannableStringBuilder = charSequence;
                        if (hashMap != null) {
                            String str2 = (String) hashMap.get("files_all");
                            spannableStringBuilder = charSequence;
                            if (str2 != null) {
                                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(str2);
                                spannableStringBuilder2.append((CharSequence) "\n\n");
                                spannableStringBuilder2.append(this.noAllDocumentsErrorText);
                                spannableStringBuilder2.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_text_RedRegular)), 0, str2.length(), 33);
                                this.errorsValues.put("files_all", "");
                                spannableStringBuilder = spannableStringBuilder2;
                            }
                        }
                        this.bottomCell.setText(spannableStringBuilder);
                        this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
                        if (this.currentDocumentsType.translation_required) {
                            HeaderCell headerCell3 = new HeaderCell(context);
                            this.headerCell = headerCell3;
                            headerCell3.setText(LocaleController.getString(R.string.PassportTranslation));
                            this.headerCell.setBackgroundColor(Theme.getColor(i7));
                            this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
                            LinearLayout linearLayout2 = new LinearLayout(context);
                            this.translationLayout = linearLayout2;
                            linearLayout2.setOrientation(1);
                            this.linearLayout2.addView(this.translationLayout, LayoutHelper.createLinear(-1, -2));
                            TextSettingsCell textSettingsCell2 = new TextSettingsCell(context);
                            this.uploadTranslationCell = textSettingsCell2;
                            textSettingsCell2.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                            this.linearLayout2.addView(this.uploadTranslationCell, LayoutHelper.createLinear(-1, -2));
                            this.uploadTranslationCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda27
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    PassportActivity.this.lambda$createAddressInterface$33(view);
                                }
                            });
                            TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
                            this.bottomCellTranslation = textInfoPrivacyCell3;
                            textInfoPrivacyCell3.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i8, i6));
                            if (this.currentBotId != 0) {
                                i4 = R.string.PassportAddTranslationUploadInfo;
                            } else {
                                TLRPC$SecureValueType tLRPC$SecureValueType3 = this.currentDocumentsType.type;
                                if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                                    i4 = R.string.PassportAddTranslationAgreementInfo;
                                } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                                    i4 = R.string.PassportAddTranslationBillInfo;
                                } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                                    i4 = R.string.PassportAddTranslationPassportRegistrationInfo;
                                } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                                    i4 = R.string.PassportAddTranslationTemporaryRegistrationInfo;
                                } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeBankStatement) {
                                    i4 = R.string.PassportAddTranslationBankInfo;
                                } else {
                                    this.noAllTranslationErrorText = "";
                                    CharSequence charSequence2 = this.noAllTranslationErrorText;
                                    hashMap2 = this.documentsErrors;
                                    SpannableStringBuilder spannableStringBuilder3 = charSequence2;
                                    if (hashMap2 != null) {
                                        String str3 = (String) hashMap2.get("translation_all");
                                        spannableStringBuilder3 = charSequence2;
                                        if (str3 != null) {
                                            SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder(str3);
                                            spannableStringBuilder4.append((CharSequence) "\n\n");
                                            spannableStringBuilder4.append(this.noAllTranslationErrorText);
                                            spannableStringBuilder4.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_text_RedRegular)), 0, str3.length(), 33);
                                            this.errorsValues.put("translation_all", "");
                                            spannableStringBuilder3 = spannableStringBuilder4;
                                        }
                                    }
                                    this.bottomCellTranslation.setText(spannableStringBuilder3);
                                    this.linearLayout2.addView(this.bottomCellTranslation, LayoutHelper.createLinear(-1, -2));
                                }
                            }
                            this.noAllTranslationErrorText = LocaleController.getString(i4);
                            CharSequence charSequence22 = this.noAllTranslationErrorText;
                            hashMap2 = this.documentsErrors;
                            SpannableStringBuilder spannableStringBuilder32 = charSequence22;
                            if (hashMap2 != null) {
                            }
                            this.bottomCellTranslation.setText(spannableStringBuilder32);
                            this.linearLayout2.addView(this.bottomCellTranslation, LayoutHelper.createLinear(-1, -2));
                        }
                    }
                }
                this.noAllDocumentsErrorText = LocaleController.getString(i3);
                CharSequence charSequence3 = this.noAllDocumentsErrorText;
                hashMap = this.documentsErrors;
                SpannableStringBuilder spannableStringBuilder5 = charSequence3;
                if (hashMap != null) {
                }
                this.bottomCell.setText(spannableStringBuilder5);
                this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
                if (this.currentDocumentsType.translation_required) {
                }
            }
            actionBar.setTitle(LocaleController.getString(i2));
            HeaderCell headerCell4 = new HeaderCell(context);
            this.headerCell = headerCell4;
            headerCell4.setText(LocaleController.getString(R.string.PassportDocuments));
            HeaderCell headerCell22 = this.headerCell;
            int i72 = Theme.key_windowBackgroundWhite;
            headerCell22.setBackgroundColor(Theme.getColor(i72));
            this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
            LinearLayout linearLayout3 = new LinearLayout(context);
            this.documentsLayout = linearLayout3;
            linearLayout3.setOrientation(1);
            this.linearLayout2.addView(this.documentsLayout, LayoutHelper.createLinear(-1, -2));
            TextSettingsCell textSettingsCell3 = new TextSettingsCell(context);
            this.uploadDocumentCell = textSettingsCell3;
            textSettingsCell3.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.linearLayout2.addView(this.uploadDocumentCell, LayoutHelper.createLinear(-1, -2));
            this.uploadDocumentCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda26
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PassportActivity.this.lambda$createAddressInterface$32(view);
                }
            });
            TextInfoPrivacyCell textInfoPrivacyCell22 = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell22;
            int i82 = R.drawable.greydivider;
            textInfoPrivacyCell22.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i82, i6));
            if (this.currentBotId == 0) {
            }
            this.noAllDocumentsErrorText = LocaleController.getString(i3);
            CharSequence charSequence32 = this.noAllDocumentsErrorText;
            hashMap = this.documentsErrors;
            SpannableStringBuilder spannableStringBuilder52 = charSequence32;
            if (hashMap != null) {
            }
            this.bottomCell.setText(spannableStringBuilder52);
            this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
            if (this.currentDocumentsType.translation_required) {
            }
        } else {
            this.actionBar.setTitle(LocaleController.getString(R.string.PassportAddress));
        }
        HeaderCell headerCell5 = new HeaderCell(context);
        this.headerCell = headerCell5;
        headerCell5.setText(LocaleController.getString(R.string.PassportAddressHeader));
        this.headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
        this.inputFields = new EditTextBoldCursor[6];
        int i9 = 0;
        while (i9 < 6) {
            final EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
            this.inputFields[i9] = editTextBoldCursor;
            FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.PassportActivity.11
                private StaticLayout errorLayout;
                float offsetX;

                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    if (this.errorLayout != null) {
                        canvas.save();
                        canvas.translate(AndroidUtilities.dp(21.0f) + this.offsetX, editTextBoldCursor.getLineY() + AndroidUtilities.dp(3.0f));
                        this.errorLayout.draw(canvas);
                        canvas.restore();
                    }
                }

                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i10, int i11) {
                    int size = View.MeasureSpec.getSize(i10) - AndroidUtilities.dp(34.0f);
                    StaticLayout errorLayout = editTextBoldCursor.getErrorLayout(size);
                    this.errorLayout = errorLayout;
                    if (errorLayout != null) {
                        int lineCount = errorLayout.getLineCount();
                        int i12 = 0;
                        if (lineCount > 1) {
                            i11 = View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.errorLayout.getLineBottom(lineCount - 1) - this.errorLayout.getLineBottom(0)), 1073741824);
                        }
                        if (LocaleController.isRTL) {
                            float f = 0.0f;
                            while (true) {
                                if (i12 >= lineCount) {
                                    break;
                                } else if (this.errorLayout.getLineLeft(i12) != 0.0f) {
                                    this.offsetX = 0.0f;
                                    break;
                                } else {
                                    f = Math.max(f, this.errorLayout.getLineWidth(i12));
                                    if (i12 == lineCount - 1) {
                                        this.offsetX = size - f;
                                    }
                                    i12++;
                                }
                            }
                        }
                    }
                    super.onMeasure(i10, i11);
                }
            };
            frameLayout.setWillNotDraw(false);
            this.linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-1, -2));
            int i10 = Theme.key_windowBackgroundWhite;
            frameLayout.setBackgroundColor(Theme.getColor(i10));
            if (i9 == 5) {
                View view = new View(context);
                this.extraBackgroundView = view;
                view.setBackgroundColor(Theme.getColor(i10));
                this.linearLayout2.addView(this.extraBackgroundView, LayoutHelper.createLinear(-1, 6));
            }
            if (this.documentOnly && this.currentDocumentsType != null) {
                frameLayout.setVisibility(8);
                View view2 = this.extraBackgroundView;
                if (view2 != null) {
                    view2.setVisibility(8);
                }
            }
            this.inputFields[i9].setTag(Integer.valueOf(i9));
            this.inputFields[i9].setSupportRtlHint(true);
            this.inputFields[i9].setTextSize(1, 16.0f);
            this.inputFields[i9].setHintColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
            this.inputFields[i9].setHeaderHintColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader));
            this.inputFields[i9].setTransformHintToHeader(true);
            EditTextBoldCursor editTextBoldCursor2 = this.inputFields[i9];
            int i11 = Theme.key_windowBackgroundWhiteBlackText;
            editTextBoldCursor2.setTextColor(Theme.getColor(i11));
            this.inputFields[i9].setBackgroundDrawable(null);
            this.inputFields[i9].setCursorColor(Theme.getColor(i11));
            this.inputFields[i9].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[i9].setCursorWidth(1.5f);
            this.inputFields[i9].setLineColors(Theme.getColor(Theme.key_windowBackgroundWhiteInputField), Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated), Theme.getColor(Theme.key_text_RedRegular));
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            if (i9 == 5) {
                editTextBoldCursorArr[i9].setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda28
                    @Override // android.view.View.OnTouchListener
                    public final boolean onTouch(View view3, MotionEvent motionEvent) {
                        boolean lambda$createAddressInterface$35;
                        lambda$createAddressInterface$35 = PassportActivity.this.lambda$createAddressInterface$35(view3, motionEvent);
                        return lambda$createAddressInterface$35;
                    }
                });
                this.inputFields[i9].setInputType(0);
                this.inputFields[i9].setFocusable(false);
            } else {
                editTextBoldCursorArr[i9].setInputType(16385);
                this.inputFields[i9].setImeOptions(268435461);
            }
            if (i9 == 0) {
                this.inputFields[i9].setHintText(LocaleController.getString(R.string.PassportStreet1));
                str = "street_line1";
            } else if (i9 == 1) {
                this.inputFields[i9].setHintText(LocaleController.getString(R.string.PassportStreet2));
                str = "street_line2";
            } else if (i9 == 2) {
                this.inputFields[i9].setHintText(LocaleController.getString(R.string.PassportPostcode));
                str = "post_code";
            } else if (i9 == 3) {
                this.inputFields[i9].setHintText(LocaleController.getString(R.string.PassportCity));
                str = "city";
            } else if (i9 == 4) {
                this.inputFields[i9].setHintText(LocaleController.getString(R.string.PassportState));
                str = "state";
            } else if (i9 != 5) {
                i = 1;
                i9 += i;
            } else {
                this.inputFields[i9].setHintText(LocaleController.getString(R.string.PassportCountry));
                str = "country_code";
            }
            setFieldValues(this.currentValues, this.inputFields[i9], str);
            EditTextBoldCursor[] editTextBoldCursorArr2 = this.inputFields;
            if (i9 == 2) {
                editTextBoldCursorArr2[i9].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PassportActivity.12
                    private boolean ignore;

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        if (this.ignore) {
                            return;
                        }
                        boolean z = true;
                        this.ignore = true;
                        int i12 = 0;
                        while (true) {
                            if (i12 >= editable.length()) {
                                z = false;
                                break;
                            }
                            char charAt = editable.charAt(i12);
                            if ((charAt < 'a' || charAt > 'z') && ((charAt < 'A' || charAt > 'Z') && !((charAt >= '0' && charAt <= '9') || charAt == '-' || charAt == ' '))) {
                                break;
                            }
                            i12++;
                        }
                        this.ignore = false;
                        if (z) {
                            editTextBoldCursor.setErrorText(LocaleController.getString(R.string.PassportUseLatinOnly));
                        } else {
                            PassportActivity.this.checkFieldForError(editTextBoldCursor, str, editable, false);
                        }
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence4, int i12, int i13, int i14) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence4, int i12, int i13, int i14) {
                    }
                });
                this.inputFields[i9].setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            } else {
                editTextBoldCursorArr2[i9].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PassportActivity.13
                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        PassportActivity.this.checkFieldForError(editTextBoldCursor, str, editable, false);
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence4, int i12, int i13, int i14) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence4, int i12, int i13, int i14) {
                    }
                });
            }
            EditTextBoldCursor editTextBoldCursor3 = this.inputFields[i9];
            editTextBoldCursor3.setSelection(editTextBoldCursor3.length());
            this.inputFields[i9].setPadding(0, 0, 0, 0);
            this.inputFields[i9].setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            frameLayout.addView(this.inputFields[i9], LayoutHelper.createFrame(-1, 64.0f, 51, 21.0f, 0.0f, 21.0f, 0.0f));
            this.inputFields[i9].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda29
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView, int i12, KeyEvent keyEvent) {
                    boolean lambda$createAddressInterface$36;
                    lambda$createAddressInterface$36 = PassportActivity.this.lambda$createAddressInterface$36(textView, i12, keyEvent);
                    return lambda$createAddressInterface$36;
                }
            });
            i = 1;
            i9 += i;
        }
        ShadowSectionCell shadowSectionCell = new ShadowSectionCell(context);
        this.sectionCell = shadowSectionCell;
        this.linearLayout2.addView(shadowSectionCell, LayoutHelper.createLinear(-1, -2));
        if (this.documentOnly && this.currentDocumentsType != null) {
            this.headerCell.setVisibility(8);
            this.sectionCell.setVisibility(8);
        }
        if (((this.currentBotId == 0 && this.currentDocumentsType != null) || this.currentTypeValue == null || this.documentOnly) && this.currentDocumentsTypeValue == null) {
            ShadowSectionCell shadowSectionCell2 = this.sectionCell;
            int i12 = R.drawable.greydivider_bottom;
            int i13 = Theme.key_windowBackgroundGrayShadow;
            shadowSectionCell2.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i12, i13));
            if (this.documentOnly && this.currentDocumentsType != null) {
                this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i12, i13));
            }
        } else {
            TLRPC$TL_secureValue tLRPC$TL_secureValue = this.currentDocumentsTypeValue;
            if (tLRPC$TL_secureValue != null) {
                addDocumentViews(tLRPC$TL_secureValue.files);
                addTranslationDocumentViews(this.currentDocumentsTypeValue.translation);
            }
            ShadowSectionCell shadowSectionCell3 = this.sectionCell;
            int i14 = R.drawable.greydivider;
            int i15 = Theme.key_windowBackgroundGrayShadow;
            shadowSectionCell3.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i14, i15));
            TextSettingsCell textSettingsCell4 = new TextSettingsCell(context);
            textSettingsCell4.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
            textSettingsCell4.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            textSettingsCell4.setText(LocaleController.getString(this.currentDocumentsType == null ? R.string.PassportDeleteInfo : R.string.PassportDeleteDocument), false);
            this.linearLayout2.addView(textSettingsCell4, LayoutHelper.createLinear(-1, -2));
            textSettingsCell4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda30
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    PassportActivity.this.lambda$createAddressInterface$37(view3);
                }
            });
            ShadowSectionCell shadowSectionCell4 = new ShadowSectionCell(context);
            this.sectionCell = shadowSectionCell4;
            shadowSectionCell4.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, i15));
            this.linearLayout2.addView(this.sectionCell, LayoutHelper.createLinear(-1, -2));
        }
        updateUploadText(0);
        updateUploadText(4);
    }

    private void createChatAttachView() {
        if (getParentActivity() != null && this.chatAttachAlert == null) {
            ChatAttachAlert chatAttachAlert = new ChatAttachAlert(getParentActivity(), this, false, false);
            this.chatAttachAlert = chatAttachAlert;
            chatAttachAlert.setDelegate(new ChatAttachAlert.ChatAttachViewDelegate() { // from class: org.telegram.ui.PassportActivity.24
                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void didPressedButton(int i, boolean z, boolean z2, int i2, long j, boolean z3, boolean z4) {
                    if (PassportActivity.this.getParentActivity() == null || PassportActivity.this.chatAttachAlert == null) {
                        return;
                    }
                    if (i != 8 && i != 7) {
                        if (PassportActivity.this.chatAttachAlert != null) {
                            PassportActivity.this.chatAttachAlert.dismissWithButtonClick(i);
                        }
                        PassportActivity.this.processSelectedAttach(i);
                        return;
                    }
                    if (i != 8) {
                        PassportActivity.this.chatAttachAlert.dismiss(true);
                    }
                    HashMap<Object, Object> selectedPhotos = PassportActivity.this.chatAttachAlert.getPhotoLayout().getSelectedPhotos();
                    ArrayList<Object> selectedPhotosOrder = PassportActivity.this.chatAttachAlert.getPhotoLayout().getSelectedPhotosOrder();
                    if (selectedPhotos.isEmpty()) {
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    for (int i3 = 0; i3 < selectedPhotosOrder.size(); i3++) {
                        MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) selectedPhotos.get(selectedPhotosOrder.get(i3));
                        SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                        String str = photoEntry.imagePath;
                        if (str == null) {
                            str = photoEntry.path;
                        }
                        sendingMediaInfo.path = str;
                        arrayList.add(sendingMediaInfo);
                        photoEntry.reset();
                    }
                    PassportActivity.this.processSelectedFiles(arrayList);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ void didSelectBot(TLRPC$User tLRPC$User) {
                    ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$didSelectBot(this, tLRPC$User);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ void doOnIdle(Runnable runnable) {
                    ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$doOnIdle(this, runnable);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ boolean needEnterComment() {
                    return ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$needEnterComment(this);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void onCameraOpened() {
                    AndroidUtilities.hideKeyboard(PassportActivity.this.fragmentView.findFocus());
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ void onWallpaperSelected(Object obj) {
                    ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$onWallpaperSelected(this, obj);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ void openAvatarsSearch() {
                    ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$openAvatarsSearch(this);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ boolean selectItemOnClicking() {
                    return ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$selectItemOnClicking(this);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ void sendAudio(ArrayList arrayList, CharSequence charSequence, boolean z, int i, long j, boolean z2) {
                    ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$sendAudio(this, arrayList, charSequence, z, i, j, z2);
                }
            });
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00b4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void createDocumentDeleteAlert() {
        int i;
        final boolean[] zArr = {true};
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda49
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                PassportActivity.this.lambda$createDocumentDeleteAlert$38(zArr, dialogInterface, i2);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setTitle(LocaleController.getString(R.string.AppName));
        boolean z = this.documentOnly;
        builder.setMessage(LocaleController.getString((z && this.currentDocumentsType == null && (this.currentType.type instanceof TLRPC$TL_secureValueTypeAddress)) ? R.string.PassportDeleteAddressAlert : (z && this.currentDocumentsType == null && (this.currentType.type instanceof TLRPC$TL_secureValueTypePersonalDetails)) ? R.string.PassportDeletePersonalAlert : R.string.PassportDeleteDocumentAlert));
        if (!this.documentOnly && this.currentDocumentsType != null) {
            FrameLayout frameLayout = new FrameLayout(getParentActivity());
            CheckBoxCell checkBoxCell = new CheckBoxCell(getParentActivity(), 1);
            checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            TLRPC$SecureValueType tLRPC$SecureValueType = this.currentType.type;
            if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
                i = R.string.PassportDeleteDocumentAddress;
            } else {
                if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                    i = R.string.PassportDeleteDocumentPersonal;
                }
                checkBoxCell.setPadding(!LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, !LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                frameLayout.addView(checkBoxCell, LayoutHelper.createFrame(-1, 48, 51));
                checkBoxCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda50
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PassportActivity.lambda$createDocumentDeleteAlert$39(zArr, view);
                    }
                });
                builder.setView(frameLayout);
            }
            checkBoxCell.setText(LocaleController.getString(i), "", true, false);
            checkBoxCell.setPadding(!LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, !LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
            frameLayout.addView(checkBoxCell, LayoutHelper.createFrame(-1, 48, 51));
            checkBoxCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda50
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PassportActivity.lambda$createDocumentDeleteAlert$39(zArr, view);
                }
            });
            builder.setView(frameLayout);
        }
        showDialog(builder.create());
    }

    private void createEmailInterface(Context context) {
        this.actionBar.setTitle(LocaleController.getString(R.string.PassportEmail));
        if (!TextUtils.isEmpty(this.currentEmail)) {
            TextSettingsCell textSettingsCell = new TextSettingsCell(context);
            textSettingsCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
            textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            textSettingsCell.setText(LocaleController.formatString("PassportPhoneUseSame", R.string.PassportPhoneUseSame, this.currentEmail), false);
            this.linearLayout2.addView(textSettingsCell, LayoutHelper.createLinear(-1, -2));
            textSettingsCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PassportActivity.this.lambda$createEmailInterface$24(view);
                }
            });
            TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell;
            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
            this.bottomCell.setText(LocaleController.getString(R.string.PassportPhoneUseSameEmailInfo));
            this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
        }
        this.inputFields = new EditTextBoldCursor[1];
        FrameLayout frameLayout = new FrameLayout(context);
        this.linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-1, 50));
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.inputFields[0] = new EditTextBoldCursor(context);
        this.inputFields[0].setTag(0);
        this.inputFields[0].setTextSize(1, 16.0f);
        this.inputFields[0].setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        EditTextBoldCursor editTextBoldCursor = this.inputFields[0];
        int i = Theme.key_windowBackgroundWhiteBlackText;
        editTextBoldCursor.setTextColor(Theme.getColor(i));
        this.inputFields[0].setBackgroundDrawable(null);
        this.inputFields[0].setCursorColor(Theme.getColor(i));
        this.inputFields[0].setCursorSize(AndroidUtilities.dp(20.0f));
        this.inputFields[0].setCursorWidth(1.5f);
        this.inputFields[0].setInputType(33);
        this.inputFields[0].setImeOptions(268435462);
        this.inputFields[0].setHint(LocaleController.getString(R.string.PaymentShippingEmailPlaceholder));
        TLRPC$TL_secureValue tLRPC$TL_secureValue = this.currentTypeValue;
        if (tLRPC$TL_secureValue != null) {
            TLRPC$SecurePlainData tLRPC$SecurePlainData = tLRPC$TL_secureValue.plain_data;
            if (tLRPC$SecurePlainData instanceof TLRPC$TL_securePlainEmail) {
                TLRPC$TL_securePlainEmail tLRPC$TL_securePlainEmail = (TLRPC$TL_securePlainEmail) tLRPC$SecurePlainData;
                if (!TextUtils.isEmpty(tLRPC$TL_securePlainEmail.email)) {
                    this.inputFields[0].setText(tLRPC$TL_securePlainEmail.email);
                }
            }
        }
        EditTextBoldCursor editTextBoldCursor2 = this.inputFields[0];
        editTextBoldCursor2.setSelection(editTextBoldCursor2.length());
        this.inputFields[0].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
        this.inputFields[0].setGravity(LocaleController.isRTL ? 5 : 3);
        frameLayout.addView(this.inputFields[0], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
        this.inputFields[0].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda1
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i2, KeyEvent keyEvent) {
                boolean lambda$createEmailInterface$25;
                lambda$createEmailInterface$25 = PassportActivity.this.lambda$createEmailInterface$25(textView, i2, keyEvent);
                return lambda$createEmailInterface$25;
            }
        });
        TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
        this.bottomCell = textInfoPrivacyCell2;
        textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
        this.bottomCell.setText(LocaleController.getString(R.string.PassportEmailUploadInfo));
        this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
    }

    private void createEmailVerificationInterface(Context context) {
        this.actionBar.setTitle(LocaleController.getString(R.string.PassportEmail));
        this.inputFields = new EditTextBoldCursor[1];
        FrameLayout frameLayout = new FrameLayout(context);
        this.linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-1, 50));
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.inputFields[0] = new EditTextBoldCursor(context);
        this.inputFields[0].setTag(0);
        this.inputFields[0].setTextSize(1, 16.0f);
        this.inputFields[0].setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        EditTextBoldCursor editTextBoldCursor = this.inputFields[0];
        int i = Theme.key_windowBackgroundWhiteBlackText;
        editTextBoldCursor.setTextColor(Theme.getColor(i));
        this.inputFields[0].setBackgroundDrawable(null);
        this.inputFields[0].setCursorColor(Theme.getColor(i));
        this.inputFields[0].setCursorSize(AndroidUtilities.dp(20.0f));
        this.inputFields[0].setCursorWidth(1.5f);
        this.inputFields[0].setInputType(3);
        this.inputFields[0].setImeOptions(268435462);
        this.inputFields[0].setHint(LocaleController.getString(R.string.PassportEmailCode));
        EditTextBoldCursor editTextBoldCursor2 = this.inputFields[0];
        editTextBoldCursor2.setSelection(editTextBoldCursor2.length());
        this.inputFields[0].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
        this.inputFields[0].setGravity(LocaleController.isRTL ? 5 : 3);
        frameLayout.addView(this.inputFields[0], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
        this.inputFields[0].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda36
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i2, KeyEvent keyEvent) {
                boolean lambda$createEmailVerificationInterface$5;
                lambda$createEmailVerificationInterface$5 = PassportActivity.this.lambda$createEmailVerificationInterface$5(textView, i2, keyEvent);
                return lambda$createEmailVerificationInterface$5;
            }
        });
        this.inputFields[0].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PassportActivity.6
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (PassportActivity.this.ignoreOnTextChange || PassportActivity.this.emailCodeLength == 0 || PassportActivity.this.inputFields[0].length() != PassportActivity.this.emailCodeLength) {
                    return;
                }
                PassportActivity.this.doneItem.callOnClick();
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }
        });
        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
        this.bottomCell = textInfoPrivacyCell;
        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
        this.bottomCell.setText(LocaleController.formatString("PassportEmailVerifyInfo", R.string.PassportEmailVerifyInfo, this.currentValues.get("email")));
        this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
    }

    /* JADX WARN: Removed duplicated region for block: B:187:0x05b9 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x024d  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x02e9  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x02f3  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0311  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0314  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0324  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void createIdentityInterface(final Context context) {
        LinearLayout linearLayout;
        TextInfoPrivacyCell textInfoPrivacyCell;
        int i;
        int i2;
        boolean z;
        String string;
        HashMap hashMap;
        final String str;
        EditTextBoldCursor editTextBoldCursor;
        int i3;
        final HashMap hashMap2;
        final String str2;
        EditTextBoldCursor editTextBoldCursor2;
        int i4;
        EditTextBoldCursor editTextBoldCursor3;
        int i5;
        boolean z2;
        EditTextBoldCursor editTextBoldCursor4;
        int i6;
        HashMap hashMap3;
        this.languageMap = new HashMap();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("countries.txt")));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String[] split = readLine.split(";");
                this.languageMap.put(split[1], split[2]);
            }
            bufferedReader.close();
        } catch (Exception e) {
            FileLog.e(e);
        }
        TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
        this.topErrorCell = textInfoPrivacyCell2;
        int i7 = R.drawable.greydivider_top;
        int i8 = Theme.key_windowBackgroundGrayShadow;
        textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i7, i8));
        boolean z3 = false;
        this.topErrorCell.setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
        this.linearLayout2.addView(this.topErrorCell, LayoutHelper.createLinear(-1, -2));
        checkTopErrorCell(true);
        if (this.currentDocumentsType != null) {
            HeaderCell headerCell = new HeaderCell(context);
            this.headerCell = headerCell;
            headerCell.setText(LocaleController.getString(this.documentOnly ? R.string.PassportDocuments : R.string.PassportRequiredDocuments));
            HeaderCell headerCell2 = this.headerCell;
            int i9 = Theme.key_windowBackgroundWhite;
            headerCell2.setBackgroundColor(Theme.getColor(i9));
            this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
            LinearLayout linearLayout2 = new LinearLayout(context);
            this.frontLayout = linearLayout2;
            linearLayout2.setOrientation(1);
            this.linearLayout2.addView(this.frontLayout, LayoutHelper.createLinear(-1, -2));
            TextDetailSettingsCell textDetailSettingsCell = new TextDetailSettingsCell(context);
            this.uploadFrontCell = textDetailSettingsCell;
            textDetailSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.linearLayout2.addView(this.uploadFrontCell, LayoutHelper.createLinear(-1, -2));
            this.uploadFrontCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda13
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PassportActivity.this.lambda$createIdentityInterface$40(view);
                }
            });
            LinearLayout linearLayout3 = new LinearLayout(context);
            this.reverseLayout = linearLayout3;
            linearLayout3.setOrientation(1);
            this.linearLayout2.addView(this.reverseLayout, LayoutHelper.createLinear(-1, -2));
            boolean z4 = this.currentDocumentsType.selfie_required;
            TextDetailSettingsCell textDetailSettingsCell2 = new TextDetailSettingsCell(context);
            this.uploadReverseCell = textDetailSettingsCell2;
            textDetailSettingsCell2.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.uploadReverseCell.setTextAndValue(LocaleController.getString(R.string.PassportReverseSide), LocaleController.getString(R.string.PassportReverseSideInfo), z4);
            this.linearLayout2.addView(this.uploadReverseCell, LayoutHelper.createLinear(-1, -2));
            this.uploadReverseCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda15
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PassportActivity.this.lambda$createIdentityInterface$41(view);
                }
            });
            if (this.currentDocumentsType.selfie_required) {
                LinearLayout linearLayout4 = new LinearLayout(context);
                this.selfieLayout = linearLayout4;
                linearLayout4.setOrientation(1);
                this.linearLayout2.addView(this.selfieLayout, LayoutHelper.createLinear(-1, -2));
                TextDetailSettingsCell textDetailSettingsCell3 = new TextDetailSettingsCell(context);
                this.uploadSelfieCell = textDetailSettingsCell3;
                textDetailSettingsCell3.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.uploadSelfieCell.setTextAndValue(LocaleController.getString(R.string.PassportSelfie), LocaleController.getString(R.string.PassportSelfieInfo), this.currentType.translation_required);
                this.linearLayout2.addView(this.uploadSelfieCell, LayoutHelper.createLinear(-1, -2));
                this.uploadSelfieCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda16
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PassportActivity.this.lambda$createIdentityInterface$42(view);
                    }
                });
            }
            TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell3;
            int i10 = R.drawable.greydivider;
            textInfoPrivacyCell3.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i10, i8));
            this.bottomCell.setText(LocaleController.getString(R.string.PassportPersonalUploadInfo));
            this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
            if (this.currentDocumentsType.translation_required) {
                HeaderCell headerCell3 = new HeaderCell(context);
                this.headerCell = headerCell3;
                headerCell3.setText(LocaleController.getString(R.string.PassportTranslation));
                this.headerCell.setBackgroundColor(Theme.getColor(i9));
                this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
                LinearLayout linearLayout5 = new LinearLayout(context);
                this.translationLayout = linearLayout5;
                linearLayout5.setOrientation(1);
                this.linearLayout2.addView(this.translationLayout, LayoutHelper.createLinear(-1, -2));
                TextSettingsCell textSettingsCell = new TextSettingsCell(context);
                this.uploadTranslationCell = textSettingsCell;
                textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.linearLayout2.addView(this.uploadTranslationCell, LayoutHelper.createLinear(-1, -2));
                this.uploadTranslationCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda17
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PassportActivity.this.lambda$createIdentityInterface$43(view);
                    }
                });
                TextInfoPrivacyCell textInfoPrivacyCell4 = new TextInfoPrivacyCell(context);
                this.bottomCellTranslation = textInfoPrivacyCell4;
                textInfoPrivacyCell4.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i10, i8));
                if (this.currentBotId != 0) {
                    i6 = R.string.PassportAddTranslationUploadInfo;
                } else {
                    TLRPC$SecureValueType tLRPC$SecureValueType = this.currentDocumentsType.type;
                    if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) {
                        i6 = R.string.PassportAddPassportInfo;
                    } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                        i6 = R.string.PassportAddInternalPassportInfo;
                    } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                        i6 = R.string.PassportAddIdentityCardInfo;
                    } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                        i6 = R.string.PassportAddDriverLicenceInfo;
                    } else {
                        this.noAllTranslationErrorText = "";
                        CharSequence charSequence = this.noAllTranslationErrorText;
                        hashMap3 = this.documentsErrors;
                        SpannableStringBuilder spannableStringBuilder = charSequence;
                        if (hashMap3 != null) {
                            String str3 = (String) hashMap3.get("translation_all");
                            spannableStringBuilder = charSequence;
                            if (str3 != null) {
                                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(str3);
                                spannableStringBuilder2.append((CharSequence) "\n\n");
                                spannableStringBuilder2.append(this.noAllTranslationErrorText);
                                spannableStringBuilder2.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_text_RedRegular)), 0, str3.length(), 33);
                                this.errorsValues.put("translation_all", "");
                                spannableStringBuilder = spannableStringBuilder2;
                            }
                        }
                        this.bottomCellTranslation.setText(spannableStringBuilder);
                        linearLayout = this.linearLayout2;
                        textInfoPrivacyCell = this.bottomCellTranslation;
                    }
                }
                this.noAllTranslationErrorText = LocaleController.getString(i6);
                CharSequence charSequence2 = this.noAllTranslationErrorText;
                hashMap3 = this.documentsErrors;
                SpannableStringBuilder spannableStringBuilder3 = charSequence2;
                if (hashMap3 != null) {
                }
                this.bottomCellTranslation.setText(spannableStringBuilder3);
                linearLayout = this.linearLayout2;
                textInfoPrivacyCell = this.bottomCellTranslation;
            }
            HeaderCell headerCell4 = new HeaderCell(context);
            this.headerCell = headerCell4;
            headerCell4.setText(LocaleController.getString(!this.documentOnly ? R.string.PassportDocument : R.string.PassportPersonal));
            this.headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
            i = this.currentDocumentsType == null ? 9 : 7;
            this.inputFields = new EditTextBoldCursor[i];
            i2 = 0;
            while (true) {
                int i11 = 64;
                if (i2 < i) {
                    ShadowSectionCell shadowSectionCell = new ShadowSectionCell(context);
                    this.sectionCell2 = shadowSectionCell;
                    this.linearLayout2.addView(shadowSectionCell, LayoutHelper.createLinear(-1, -2));
                    HeaderCell headerCell5 = new HeaderCell(context);
                    this.headerCell = headerCell5;
                    headerCell5.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
                    int i12 = 3;
                    this.inputExtraFields = new EditTextBoldCursor[3];
                    int i13 = 0;
                    while (i13 < i12) {
                        final EditTextBoldCursor editTextBoldCursor5 = new EditTextBoldCursor(context);
                        this.inputExtraFields[i13] = editTextBoldCursor5;
                        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.PassportActivity.18
                            private StaticLayout errorLayout;
                            private float offsetX;

                            @Override // android.view.View
                            protected void onDraw(Canvas canvas) {
                                if (this.errorLayout != null) {
                                    canvas.save();
                                    canvas.translate(AndroidUtilities.dp(21.0f) + this.offsetX, editTextBoldCursor5.getLineY() + AndroidUtilities.dp(3.0f));
                                    this.errorLayout.draw(canvas);
                                    canvas.restore();
                                }
                            }

                            @Override // android.widget.FrameLayout, android.view.View
                            protected void onMeasure(int i14, int i15) {
                                int size = View.MeasureSpec.getSize(i14) - AndroidUtilities.dp(34.0f);
                                StaticLayout errorLayout = editTextBoldCursor5.getErrorLayout(size);
                                this.errorLayout = errorLayout;
                                if (errorLayout != null) {
                                    int lineCount = errorLayout.getLineCount();
                                    int i16 = 0;
                                    if (lineCount > 1) {
                                        i15 = View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.errorLayout.getLineBottom(lineCount - 1) - this.errorLayout.getLineBottom(0)), 1073741824);
                                    }
                                    if (LocaleController.isRTL) {
                                        float f = 0.0f;
                                        while (true) {
                                            if (i16 >= lineCount) {
                                                break;
                                            } else if (this.errorLayout.getLineLeft(i16) != 0.0f) {
                                                this.offsetX = 0.0f;
                                                break;
                                            } else {
                                                f = Math.max(f, this.errorLayout.getLineWidth(i16));
                                                if (i16 == lineCount - 1) {
                                                    this.offsetX = size - f;
                                                }
                                                i16++;
                                            }
                                        }
                                    }
                                }
                                super.onMeasure(i14, i15);
                            }
                        };
                        frameLayout.setWillNotDraw(false);
                        this.linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-1, i11));
                        int i14 = Theme.key_windowBackgroundWhite;
                        frameLayout.setBackgroundColor(Theme.getColor(i14));
                        if (i13 == 2) {
                            View view = new View(context);
                            this.extraBackgroundView2 = view;
                            view.setBackgroundColor(Theme.getColor(i14));
                            this.linearLayout2.addView(this.extraBackgroundView2, LayoutHelper.createLinear(-1, 6));
                        }
                        this.inputExtraFields[i13].setTag(Integer.valueOf(i13));
                        this.inputExtraFields[i13].setSupportRtlHint(true);
                        this.inputExtraFields[i13].setTextSize(1, 16.0f);
                        this.inputExtraFields[i13].setHintColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
                        EditTextBoldCursor editTextBoldCursor6 = this.inputExtraFields[i13];
                        int i15 = Theme.key_windowBackgroundWhiteBlackText;
                        editTextBoldCursor6.setTextColor(Theme.getColor(i15));
                        this.inputExtraFields[i13].setHeaderHintColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader));
                        this.inputExtraFields[i13].setTransformHintToHeader(true);
                        this.inputExtraFields[i13].setBackgroundDrawable(null);
                        this.inputExtraFields[i13].setCursorColor(Theme.getColor(i15));
                        this.inputExtraFields[i13].setCursorSize(AndroidUtilities.dp(20.0f));
                        this.inputExtraFields[i13].setCursorWidth(1.5f);
                        this.inputExtraFields[i13].setLineColors(Theme.getColor(Theme.key_windowBackgroundWhiteInputField), Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated), Theme.getColor(Theme.key_text_RedRegular));
                        this.inputExtraFields[i13].setInputType(16385);
                        this.inputExtraFields[i13].setImeOptions(268435461);
                        if (i13 == 0) {
                            hashMap = this.currentValues;
                            str = "first_name_native";
                        } else if (i13 == 1) {
                            hashMap = this.currentValues;
                            str = "middle_name_native";
                        } else if (i13 != 2) {
                            i13++;
                            i12 = 3;
                            i11 = 64;
                        } else {
                            hashMap = this.currentValues;
                            str = "last_name_native";
                        }
                        setFieldValues(hashMap, this.inputExtraFields[i13], str);
                        EditTextBoldCursor editTextBoldCursor7 = this.inputExtraFields[i13];
                        editTextBoldCursor7.setSelection(editTextBoldCursor7.length());
                        if (i13 == 0 || i13 == 2 || i13 == 1) {
                            this.inputExtraFields[i13].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PassportActivity.19
                                private boolean ignore;

                                @Override // android.text.TextWatcher
                                public void afterTextChanged(Editable editable) {
                                    if (this.ignore) {
                                        return;
                                    }
                                    PassportActivity.this.checkFieldForError(editTextBoldCursor5, str, editable, false);
                                }

                                @Override // android.text.TextWatcher
                                public void beforeTextChanged(CharSequence charSequence3, int i16, int i17, int i18) {
                                }

                                @Override // android.text.TextWatcher
                                public void onTextChanged(CharSequence charSequence3, int i16, int i17, int i18) {
                                }
                            });
                        }
                        this.inputExtraFields[i13].setPadding(0, 0, 0, 0);
                        this.inputExtraFields[i13].setGravity((LocaleController.isRTL ? 5 : 3) | 16);
                        frameLayout.addView(this.inputExtraFields[i13], LayoutHelper.createFrame(-1, -1.0f, 51, 21.0f, 0.0f, 21.0f, 0.0f));
                        this.inputExtraFields[i13].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda23
                            @Override // android.widget.TextView.OnEditorActionListener
                            public final boolean onEditorAction(TextView textView, int i16, KeyEvent keyEvent) {
                                boolean lambda$createIdentityInterface$53;
                                lambda$createIdentityInterface$53 = PassportActivity.this.lambda$createIdentityInterface$53(textView, i16, keyEvent);
                                return lambda$createIdentityInterface$53;
                            }
                        });
                        i13++;
                        i12 = 3;
                        i11 = 64;
                    }
                    TextInfoPrivacyCell textInfoPrivacyCell5 = new TextInfoPrivacyCell(context);
                    this.nativeInfoCell = textInfoPrivacyCell5;
                    this.linearLayout2.addView(textInfoPrivacyCell5, LayoutHelper.createLinear(-1, -2));
                    if (((this.currentBotId == 0 && this.currentDocumentsType != null) || this.currentTypeValue == null || this.documentOnly) && this.currentDocumentsTypeValue == null) {
                        this.nativeInfoCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    } else {
                        TLRPC$TL_secureValue tLRPC$TL_secureValue = this.currentDocumentsTypeValue;
                        if (tLRPC$TL_secureValue != null) {
                            addDocumentViews(tLRPC$TL_secureValue.files);
                            TLRPC$SecureFile tLRPC$SecureFile = this.currentDocumentsTypeValue.front_side;
                            if (tLRPC$SecureFile instanceof TLRPC$TL_secureFile) {
                                addDocumentViewInternal((TLRPC$TL_secureFile) tLRPC$SecureFile, 2);
                            }
                            TLRPC$SecureFile tLRPC$SecureFile2 = this.currentDocumentsTypeValue.reverse_side;
                            if (tLRPC$SecureFile2 instanceof TLRPC$TL_secureFile) {
                                addDocumentViewInternal((TLRPC$TL_secureFile) tLRPC$SecureFile2, 3);
                            }
                            TLRPC$SecureFile tLRPC$SecureFile3 = this.currentDocumentsTypeValue.selfie;
                            if (tLRPC$SecureFile3 instanceof TLRPC$TL_secureFile) {
                                addDocumentViewInternal((TLRPC$TL_secureFile) tLRPC$SecureFile3, 1);
                            }
                            addTranslationDocumentViews(this.currentDocumentsTypeValue.translation);
                        }
                        TextSettingsCell textSettingsCell2 = new TextSettingsCell(context);
                        textSettingsCell2.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
                        textSettingsCell2.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        if (this.currentDocumentsType == null) {
                            string = LocaleController.getString(R.string.PassportDeleteInfo);
                            z = false;
                        } else {
                            z = false;
                            string = LocaleController.getString(R.string.PassportDeleteDocument);
                        }
                        textSettingsCell2.setText(string, z);
                        this.linearLayout2.addView(textSettingsCell2, LayoutHelper.createLinear(-1, -2));
                        textSettingsCell2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda14
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                PassportActivity.this.lambda$createIdentityInterface$54(view2);
                            }
                        });
                        TextInfoPrivacyCell textInfoPrivacyCell6 = this.nativeInfoCell;
                        int i16 = R.drawable.greydivider;
                        int i17 = Theme.key_windowBackgroundGrayShadow;
                        textInfoPrivacyCell6.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i16, i17));
                        ShadowSectionCell shadowSectionCell2 = new ShadowSectionCell(context);
                        this.sectionCell = shadowSectionCell2;
                        shadowSectionCell2.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, i17));
                        this.linearLayout2.addView(this.sectionCell, LayoutHelper.createLinear(-1, -2));
                    }
                    updateInterfaceStringsForDocumentType();
                    checkNativeFields(false);
                    return;
                }
                final EditTextBoldCursor editTextBoldCursor8 = new EditTextBoldCursor(context);
                this.inputFields[i2] = editTextBoldCursor8;
                FrameLayout frameLayout2 = new FrameLayout(context) { // from class: org.telegram.ui.PassportActivity.15
                    private StaticLayout errorLayout;
                    private float offsetX;

                    @Override // android.view.View
                    protected void onDraw(Canvas canvas) {
                        if (this.errorLayout != null) {
                            canvas.save();
                            canvas.translate(AndroidUtilities.dp(21.0f) + this.offsetX, editTextBoldCursor8.getLineY() + AndroidUtilities.dp(3.0f));
                            this.errorLayout.draw(canvas);
                            canvas.restore();
                        }
                    }

                    @Override // android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i18, int i19) {
                        int size = View.MeasureSpec.getSize(i18) - AndroidUtilities.dp(34.0f);
                        StaticLayout errorLayout = editTextBoldCursor8.getErrorLayout(size);
                        this.errorLayout = errorLayout;
                        if (errorLayout != null) {
                            int lineCount = errorLayout.getLineCount();
                            int i20 = 0;
                            if (lineCount > 1) {
                                i19 = View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.errorLayout.getLineBottom(lineCount - 1) - this.errorLayout.getLineBottom(0)), 1073741824);
                            }
                            if (LocaleController.isRTL) {
                                float f = 0.0f;
                                while (true) {
                                    if (i20 >= lineCount) {
                                        break;
                                    } else if (this.errorLayout.getLineLeft(i20) != 0.0f) {
                                        this.offsetX = 0.0f;
                                        break;
                                    } else {
                                        f = Math.max(f, this.errorLayout.getLineWidth(i20));
                                        if (i20 == lineCount - 1) {
                                            this.offsetX = size - f;
                                        }
                                        i20++;
                                    }
                                }
                            }
                        }
                        super.onMeasure(i18, i19);
                    }
                };
                frameLayout2.setWillNotDraw(z3);
                this.linearLayout2.addView(frameLayout2, LayoutHelper.createLinear(-1, 64));
                int i18 = Theme.key_windowBackgroundWhite;
                frameLayout2.setBackgroundColor(Theme.getColor(i18));
                if (i2 == i - 1) {
                    View view2 = new View(context);
                    this.extraBackgroundView = view2;
                    view2.setBackgroundColor(Theme.getColor(i18));
                    this.linearLayout2.addView(this.extraBackgroundView, LayoutHelper.createLinear(-1, 6));
                }
                if (this.documentOnly && this.currentDocumentsType != null && i2 < 7) {
                    frameLayout2.setVisibility(8);
                    View view3 = this.extraBackgroundView;
                    if (view3 != null) {
                        view3.setVisibility(8);
                    }
                }
                this.inputFields[i2].setTag(Integer.valueOf(i2));
                this.inputFields[i2].setSupportRtlHint(true);
                this.inputFields[i2].setTextSize(1, 16.0f);
                this.inputFields[i2].setHintColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
                EditTextBoldCursor editTextBoldCursor9 = this.inputFields[i2];
                int i19 = Theme.key_windowBackgroundWhiteBlackText;
                editTextBoldCursor9.setTextColor(Theme.getColor(i19));
                this.inputFields[i2].setHeaderHintColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader));
                this.inputFields[i2].setTransformHintToHeader(true);
                this.inputFields[i2].setBackgroundDrawable(null);
                this.inputFields[i2].setCursorColor(Theme.getColor(i19));
                this.inputFields[i2].setCursorSize(AndroidUtilities.dp(20.0f));
                this.inputFields[i2].setCursorWidth(1.5f);
                this.inputFields[i2].setLineColors(Theme.getColor(Theme.key_windowBackgroundWhiteInputField), Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated), Theme.getColor(Theme.key_text_RedRegular));
                if (i2 == 5 || i2 == 6) {
                    this.inputFields[i2].setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda21
                        @Override // android.view.View.OnTouchListener
                        public final boolean onTouch(View view4, MotionEvent motionEvent) {
                            boolean lambda$createIdentityInterface$46;
                            lambda$createIdentityInterface$46 = PassportActivity.this.lambda$createIdentityInterface$46(view4, motionEvent);
                            return lambda$createIdentityInterface$46;
                        }
                    });
                    this.inputFields[i2].setInputType(0);
                } else {
                    if (i2 == 3 || i2 == 8) {
                        this.inputFields[i2].setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda20
                            @Override // android.view.View.OnTouchListener
                            public final boolean onTouch(View view4, MotionEvent motionEvent) {
                                boolean lambda$createIdentityInterface$49;
                                lambda$createIdentityInterface$49 = PassportActivity.this.lambda$createIdentityInterface$49(context, view4, motionEvent);
                                return lambda$createIdentityInterface$49;
                            }
                        });
                        z2 = false;
                        this.inputFields[i2].setInputType(0);
                        editTextBoldCursor4 = this.inputFields[i2];
                    } else if (i2 == 4) {
                        this.inputFields[i2].setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda19
                            @Override // android.view.View.OnTouchListener
                            public final boolean onTouch(View view4, MotionEvent motionEvent) {
                                boolean lambda$createIdentityInterface$51;
                                lambda$createIdentityInterface$51 = PassportActivity.this.lambda$createIdentityInterface$51(view4, motionEvent);
                                return lambda$createIdentityInterface$51;
                            }
                        });
                        z2 = false;
                        this.inputFields[i2].setInputType(0);
                        editTextBoldCursor4 = this.inputFields[i2];
                    } else {
                        this.inputFields[i2].setInputType(16385);
                        this.inputFields[i2].setImeOptions(268435461);
                    }
                    editTextBoldCursor4.setFocusable(z2);
                }
                switch (i2) {
                    case 0:
                        if (this.currentType.native_names) {
                            editTextBoldCursor = this.inputFields[i2];
                            i3 = R.string.PassportNameLatin;
                        } else {
                            editTextBoldCursor = this.inputFields[i2];
                            i3 = R.string.PassportName;
                        }
                        editTextBoldCursor.setHintText(LocaleController.getString(i3));
                        hashMap2 = this.currentValues;
                        str2 = "first_name";
                        break;
                    case 1:
                        if (this.currentType.native_names) {
                            editTextBoldCursor2 = this.inputFields[i2];
                            i4 = R.string.PassportMidnameLatin;
                        } else {
                            editTextBoldCursor2 = this.inputFields[i2];
                            i4 = R.string.PassportMidname;
                        }
                        editTextBoldCursor2.setHintText(LocaleController.getString(i4));
                        hashMap2 = this.currentValues;
                        str2 = "middle_name";
                        break;
                    case 2:
                        if (this.currentType.native_names) {
                            editTextBoldCursor3 = this.inputFields[i2];
                            i5 = R.string.PassportSurnameLatin;
                        } else {
                            editTextBoldCursor3 = this.inputFields[i2];
                            i5 = R.string.PassportSurname;
                        }
                        editTextBoldCursor3.setHintText(LocaleController.getString(i5));
                        hashMap2 = this.currentValues;
                        str2 = "last_name";
                        break;
                    case 3:
                        this.inputFields[i2].setHintText(LocaleController.getString(R.string.PassportBirthdate));
                        hashMap2 = this.currentValues;
                        str2 = "birth_date";
                        break;
                    case 4:
                        this.inputFields[i2].setHintText(LocaleController.getString(R.string.PassportGender));
                        hashMap2 = this.currentValues;
                        str2 = "gender";
                        break;
                    case 5:
                        this.inputFields[i2].setHintText(LocaleController.getString(R.string.PassportCitizenship));
                        hashMap2 = this.currentValues;
                        str2 = "country_code";
                        break;
                    case 6:
                        this.inputFields[i2].setHintText(LocaleController.getString(R.string.PassportResidence));
                        hashMap2 = this.currentValues;
                        str2 = "residence_country_code";
                        break;
                    case 7:
                        this.inputFields[i2].setHintText(LocaleController.getString(R.string.PassportDocumentNumber));
                        hashMap2 = this.currentDocumentValues;
                        str2 = "document_no";
                        break;
                    case 8:
                        this.inputFields[i2].setHintText(LocaleController.getString(R.string.PassportExpired));
                        hashMap2 = this.currentDocumentValues;
                        str2 = "expiry_date";
                        break;
                    default:
                        i2++;
                        z3 = false;
                }
                setFieldValues(hashMap2, this.inputFields[i2], str2);
                EditTextBoldCursor editTextBoldCursor10 = this.inputFields[i2];
                editTextBoldCursor10.setSelection(editTextBoldCursor10.length());
                if (i2 == 0 || i2 == 2 || i2 == 1) {
                    this.inputFields[i2].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PassportActivity.16
                        private boolean ignore;

                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            boolean z5;
                            if (this.ignore) {
                                return;
                            }
                            int intValue = ((Integer) editTextBoldCursor8.getTag()).intValue();
                            int i20 = 0;
                            while (true) {
                                if (i20 >= editable.length()) {
                                    z5 = false;
                                    break;
                                }
                                char charAt = editable.charAt(i20);
                                if ((charAt < '0' || charAt > '9') && ((charAt < 'a' || charAt > 'z') && !((charAt >= 'A' && charAt <= 'Z') || charAt == ' ' || charAt == '\'' || charAt == ',' || charAt == '.' || charAt == '&' || charAt == '-' || charAt == '/'))) {
                                    z5 = true;
                                    break;
                                }
                                i20++;
                            }
                            if (z5 && !PassportActivity.this.allowNonLatinName) {
                                editTextBoldCursor8.setErrorText(LocaleController.getString(R.string.PassportUseLatinOnly));
                                return;
                            }
                            PassportActivity.this.nonLatinNames[intValue] = z5;
                            PassportActivity.this.checkFieldForError(editTextBoldCursor8, str2, editable, false);
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence3, int i20, int i21, int i22) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence3, int i20, int i21, int i22) {
                        }
                    });
                } else {
                    this.inputFields[i2].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PassportActivity.17
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            PassportActivity passportActivity = PassportActivity.this;
                            passportActivity.checkFieldForError(editTextBoldCursor8, str2, editable, hashMap2 == passportActivity.currentDocumentValues);
                            int intValue = ((Integer) editTextBoldCursor8.getTag()).intValue();
                            EditTextBoldCursor editTextBoldCursor11 = PassportActivity.this.inputFields[intValue];
                            if (intValue == 6) {
                                PassportActivity.this.checkNativeFields(true);
                            }
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence3, int i20, int i21, int i22) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence3, int i20, int i21, int i22) {
                        }
                    });
                }
                this.inputFields[i2].setPadding(0, 0, 0, 0);
                this.inputFields[i2].setGravity((LocaleController.isRTL ? 5 : 3) | 16);
                frameLayout2.addView(this.inputFields[i2], LayoutHelper.createFrame(-1, -1.0f, 51, 21.0f, 0.0f, 21.0f, 0.0f));
                this.inputFields[i2].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda22
                    @Override // android.widget.TextView.OnEditorActionListener
                    public final boolean onEditorAction(TextView textView, int i20, KeyEvent keyEvent) {
                        boolean lambda$createIdentityInterface$52;
                        lambda$createIdentityInterface$52 = PassportActivity.this.lambda$createIdentityInterface$52(textView, i20, keyEvent);
                        return lambda$createIdentityInterface$52;
                    }
                });
                i2++;
                z3 = false;
            }
        } else {
            TextSettingsCell textSettingsCell3 = new TextSettingsCell(context);
            this.scanDocumentCell = textSettingsCell3;
            textSettingsCell3.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.scanDocumentCell.setText(LocaleController.getString(R.string.PassportScanPassport), false);
            this.linearLayout2.addView(this.scanDocumentCell, LayoutHelper.createLinear(-1, -2));
            this.scanDocumentCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda18
                @Override // android.view.View.OnClickListener
                public final void onClick(View view4) {
                    PassportActivity.this.lambda$createIdentityInterface$44(view4);
                }
            });
            TextInfoPrivacyCell textInfoPrivacyCell7 = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell7;
            textInfoPrivacyCell7.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, i8));
            this.bottomCell.setText(LocaleController.getString(R.string.PassportScanPassportInfo));
            linearLayout = this.linearLayout2;
            textInfoPrivacyCell = this.bottomCell;
        }
        linearLayout.addView(textInfoPrivacyCell, LayoutHelper.createLinear(-1, -2));
        HeaderCell headerCell42 = new HeaderCell(context);
        this.headerCell = headerCell42;
        headerCell42.setText(LocaleController.getString(!this.documentOnly ? R.string.PassportDocument : R.string.PassportPersonal));
        this.headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
        if (this.currentDocumentsType == null) {
        }
        this.inputFields = new EditTextBoldCursor[i];
        i2 = 0;
        while (true) {
            int i112 = 64;
            if (i2 < i) {
            }
            i2++;
            z3 = false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x02db  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x02dd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void createManageInterface(Context context) {
        LinearLayout linearLayout;
        LinearLayout linearLayout2;
        LinearLayout.LayoutParams layoutParams;
        ArrayList arrayList;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType;
        boolean z;
        ArrayList arrayList2;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2;
        TLRPC$SecureValueType tLRPC$TL_secureValueTypeAddress;
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        this.actionBar.setTitle(LocaleController.getString(R.string.TelegramPassport));
        this.actionBar.createMenu().addItem(1, R.drawable.msg_info);
        HeaderCell headerCell = new HeaderCell(context);
        this.headerCell = headerCell;
        headerCell.setText(LocaleController.getString(R.string.PassportProvidedInformation));
        this.headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
        ShadowSectionCell shadowSectionCell = new ShadowSectionCell(context);
        this.sectionCell = shadowSectionCell;
        int i = R.drawable.greydivider;
        int i2 = Theme.key_windowBackgroundGrayShadow;
        shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i, i2));
        this.linearLayout2.addView(this.sectionCell, LayoutHelper.createLinear(-1, -2));
        TextSettingsCell textSettingsCell = new TextSettingsCell(context);
        this.addDocumentCell = textSettingsCell;
        textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        TextSettingsCell textSettingsCell2 = this.addDocumentCell;
        int i3 = R.string.PassportNoDocumentsAdd;
        textSettingsCell2.setText(LocaleController.getString(i3), true);
        this.linearLayout2.addView(this.addDocumentCell, LayoutHelper.createLinear(-1, -2));
        this.addDocumentCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PassportActivity.this.lambda$createManageInterface$17(view);
            }
        });
        TextSettingsCell textSettingsCell3 = new TextSettingsCell(context);
        this.deletePassportCell = textSettingsCell3;
        textSettingsCell3.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
        this.deletePassportCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.deletePassportCell.setText(LocaleController.getString(R.string.TelegramPassportDelete), false);
        this.linearLayout2.addView(this.deletePassportCell, LayoutHelper.createLinear(-1, -2));
        this.deletePassportCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PassportActivity.this.lambda$createManageInterface$21(view);
            }
        });
        ShadowSectionCell shadowSectionCell2 = new ShadowSectionCell(context);
        this.addDocumentSectionCell = shadowSectionCell2;
        int i4 = R.drawable.greydivider_bottom;
        shadowSectionCell2.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i4, i2));
        this.linearLayout2.addView(this.addDocumentSectionCell, LayoutHelper.createLinear(-1, -2));
        LinearLayout linearLayout3 = new LinearLayout(context);
        this.emptyLayout = linearLayout3;
        linearLayout3.setOrientation(1);
        this.emptyLayout.setGravity(17);
        this.emptyLayout.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i4, i2));
        if (AndroidUtilities.isTablet()) {
            linearLayout = this.linearLayout2;
            linearLayout2 = this.emptyLayout;
            layoutParams = new LinearLayout.LayoutParams(-1, AndroidUtilities.dp(528.0f) - ActionBar.getCurrentActionBarHeight());
        } else {
            linearLayout = this.linearLayout2;
            linearLayout2 = this.emptyLayout;
            layoutParams = new LinearLayout.LayoutParams(-1, AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight());
        }
        linearLayout.addView(linearLayout2, layoutParams);
        ImageView imageView = new ImageView(context);
        this.emptyImageView = imageView;
        imageView.setImageResource(R.drawable.no_passport);
        this.emptyImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_sessions_devicesImage), PorterDuff.Mode.MULTIPLY));
        this.emptyLayout.addView(this.emptyImageView, LayoutHelper.createLinear(-2, -2));
        TextView textView = new TextView(context);
        this.emptyTextView1 = textView;
        int i5 = Theme.key_windowBackgroundWhiteGrayText2;
        textView.setTextColor(Theme.getColor(i5));
        this.emptyTextView1.setGravity(17);
        this.emptyTextView1.setTextSize(1, 15.0f);
        this.emptyTextView1.setTypeface(AndroidUtilities.bold());
        this.emptyTextView1.setText(LocaleController.getString(R.string.PassportNoDocuments));
        this.emptyLayout.addView(this.emptyTextView1, LayoutHelper.createLinear(-2, -2, 17, 0, 16, 0, 0));
        TextView textView2 = new TextView(context);
        this.emptyTextView2 = textView2;
        textView2.setTextColor(Theme.getColor(i5));
        this.emptyTextView2.setGravity(17);
        this.emptyTextView2.setTextSize(1, 14.0f);
        this.emptyTextView2.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.emptyTextView2.setText(LocaleController.getString(R.string.PassportNoDocumentsInfo));
        this.emptyLayout.addView(this.emptyTextView2, LayoutHelper.createLinear(-2, -2, 17, 0, 14, 0, 0));
        TextView textView3 = new TextView(context);
        this.emptyTextView3 = textView3;
        int i6 = Theme.key_windowBackgroundWhiteBlueText4;
        textView3.setTextColor(Theme.getColor(i6));
        this.emptyTextView3.setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(4.0f));
        this.emptyTextView3.setBackground(Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(Theme.getColor(i6), 32), 7));
        this.emptyTextView3.setGravity(17);
        this.emptyTextView3.setTextSize(1, 15.0f);
        this.emptyTextView3.setTypeface(AndroidUtilities.bold());
        this.emptyTextView3.setGravity(17);
        this.emptyTextView3.setText(LocaleController.getString(i3).toUpperCase());
        this.emptyLayout.addView(this.emptyTextView3, LayoutHelper.createLinear(-2, 30, 17, 0, 12, 0, 0));
        this.emptyTextView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PassportActivity.this.lambda$createManageInterface$22(view);
            }
        });
        int size = this.currentForm.values.size();
        int i7 = 0;
        while (i7 < size) {
            TLRPC$TL_secureValue tLRPC$TL_secureValue = (TLRPC$TL_secureValue) this.currentForm.values.get(i7);
            if (isPersonalDocument(tLRPC$TL_secureValue.type)) {
                arrayList2 = new ArrayList();
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3 = new TLRPC$TL_secureRequiredType();
                tLRPC$TL_secureRequiredType3.type = tLRPC$TL_secureValue.type;
                tLRPC$TL_secureRequiredType3.selfie_required = true;
                tLRPC$TL_secureRequiredType3.translation_required = true;
                arrayList2.add(tLRPC$TL_secureRequiredType3);
                tLRPC$TL_secureRequiredType2 = new TLRPC$TL_secureRequiredType();
                tLRPC$TL_secureValueTypeAddress = new TLRPC$TL_secureValueTypePersonalDetails();
            } else if (isAddressDocument(tLRPC$TL_secureValue.type)) {
                arrayList2 = new ArrayList();
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType4 = new TLRPC$TL_secureRequiredType();
                tLRPC$TL_secureRequiredType4.type = tLRPC$TL_secureValue.type;
                tLRPC$TL_secureRequiredType4.translation_required = true;
                arrayList2.add(tLRPC$TL_secureRequiredType4);
                tLRPC$TL_secureRequiredType2 = new TLRPC$TL_secureRequiredType();
                tLRPC$TL_secureValueTypeAddress = new TLRPC$TL_secureValueTypeAddress();
            } else {
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType5 = new TLRPC$TL_secureRequiredType();
                tLRPC$TL_secureRequiredType5.type = tLRPC$TL_secureValue.type;
                arrayList = null;
                tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType5;
                z = false;
                addField(context, tLRPC$TL_secureRequiredType, arrayList, z, i7 != size + (-1));
                i7++;
            }
            tLRPC$TL_secureRequiredType2.type = tLRPC$TL_secureValueTypeAddress;
            tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType2;
            arrayList = arrayList2;
            z = true;
            addField(context, tLRPC$TL_secureRequiredType, arrayList, z, i7 != size + (-1));
            i7++;
        }
        updateManageVisibility();
    }

    private void createPasswordInterface(Context context) {
        TLRPC$User currentUser;
        TextInfoPrivacyCell textInfoPrivacyCell;
        CharSequence replaceTags;
        if (this.currentForm != null) {
            int i = 0;
            while (true) {
                if (i >= this.currentForm.users.size()) {
                    currentUser = null;
                    break;
                }
                currentUser = (TLRPC$User) this.currentForm.users.get(i);
                if (currentUser.id == this.currentBotId) {
                    break;
                }
                i++;
            }
        } else {
            currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
        }
        this.actionBar.setTitle(LocaleController.getString(R.string.TelegramPassport));
        EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context);
        this.emptyView = emptyTextProgressView;
        emptyTextProgressView.showProgress();
        ((FrameLayout) this.fragmentView).addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        FrameLayout frameLayout = new FrameLayout(context);
        this.passwordAvatarContainer = frameLayout;
        this.linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-1, 100));
        BackupImageView backupImageView = new BackupImageView(context);
        backupImageView.setRoundRadius(AndroidUtilities.dp(32.0f));
        this.passwordAvatarContainer.addView(backupImageView, LayoutHelper.createFrame(64, 64.0f, 17, 0.0f, 8.0f, 0.0f, 0.0f));
        backupImageView.setForUserOrChat(currentUser, new AvatarDrawable(currentUser));
        TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
        this.passwordRequestTextView = textInfoPrivacyCell2;
        textInfoPrivacyCell2.getTextView().setGravity(1);
        if (this.currentBotId == 0) {
            textInfoPrivacyCell = this.passwordRequestTextView;
            replaceTags = LocaleController.getString(R.string.PassportSelfRequest);
        } else {
            textInfoPrivacyCell = this.passwordRequestTextView;
            replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("PassportRequest", R.string.PassportRequest, UserObject.getFirstName(currentUser)));
        }
        textInfoPrivacyCell.setText(replaceTags);
        ((FrameLayout.LayoutParams) this.passwordRequestTextView.getTextView().getLayoutParams()).gravity = 1;
        this.linearLayout2.addView(this.passwordRequestTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 21.0f, 0.0f, 21.0f, 0.0f));
        ImageView imageView = new ImageView(context);
        this.noPasswordImageView = imageView;
        imageView.setImageResource(R.drawable.no_password);
        this.noPasswordImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_messagePanelIcons), PorterDuff.Mode.MULTIPLY));
        this.linearLayout2.addView(this.noPasswordImageView, LayoutHelper.createLinear(-2, -2, 49, 0, 13, 0, 0));
        TextView textView = new TextView(context);
        this.noPasswordTextView = textView;
        textView.setTextSize(1, 14.0f);
        this.noPasswordTextView.setGravity(1);
        this.noPasswordTextView.setPadding(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(21.0f), AndroidUtilities.dp(17.0f));
        this.noPasswordTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4));
        this.noPasswordTextView.setText(LocaleController.getString(R.string.TelegramPassportCreatePasswordInfo));
        this.linearLayout2.addView(this.noPasswordTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 21.0f, 10.0f, 21.0f, 0.0f));
        TextView textView2 = new TextView(context);
        this.noPasswordSetTextView = textView2;
        textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText5));
        this.noPasswordSetTextView.setGravity(17);
        this.noPasswordSetTextView.setTextSize(1, 16.0f);
        this.noPasswordSetTextView.setTypeface(AndroidUtilities.bold());
        this.noPasswordSetTextView.setText(LocaleController.getString(R.string.TelegramPassportCreatePassword));
        this.linearLayout2.addView(this.noPasswordSetTextView, LayoutHelper.createFrame(-1, 24.0f, (LocaleController.isRTL ? 5 : 3) | 48, 21.0f, 9.0f, 21.0f, 0.0f));
        this.noPasswordSetTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda32
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PassportActivity.this.lambda$createPasswordInterface$6(view);
            }
        });
        this.inputFields = new EditTextBoldCursor[1];
        this.inputFieldContainers = r3;
        ViewGroup[] viewGroupArr = {new FrameLayout(context)};
        this.linearLayout2.addView(this.inputFieldContainers[0], LayoutHelper.createLinear(-1, 50));
        this.inputFieldContainers[0].setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.inputFields[0] = new EditTextBoldCursor(context);
        this.inputFields[0].setTag(0);
        this.inputFields[0].setTextSize(1, 16.0f);
        this.inputFields[0].setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        EditTextBoldCursor editTextBoldCursor = this.inputFields[0];
        int i2 = Theme.key_windowBackgroundWhiteBlackText;
        editTextBoldCursor.setTextColor(Theme.getColor(i2));
        this.inputFields[0].setBackgroundDrawable(null);
        this.inputFields[0].setCursorColor(Theme.getColor(i2));
        this.inputFields[0].setCursorSize(AndroidUtilities.dp(20.0f));
        this.inputFields[0].setCursorWidth(1.5f);
        this.inputFields[0].setInputType(NotificationCenter.walletPendingTransactionsChanged);
        this.inputFields[0].setMaxLines(1);
        this.inputFields[0].setLines(1);
        this.inputFields[0].setSingleLine(true);
        this.inputFields[0].setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.inputFields[0].setTypeface(Typeface.DEFAULT);
        this.inputFields[0].setImeOptions(268435462);
        this.inputFields[0].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
        this.inputFields[0].setGravity(LocaleController.isRTL ? 5 : 3);
        this.inputFieldContainers[0].addView(this.inputFields[0], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
        this.inputFields[0].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda33
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView3, int i3, KeyEvent keyEvent) {
                boolean lambda$createPasswordInterface$7;
                lambda$createPasswordInterface$7 = PassportActivity.this.lambda$createPasswordInterface$7(textView3, i3, keyEvent);
                return lambda$createPasswordInterface$7;
            }
        });
        this.inputFields[0].setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: org.telegram.ui.PassportActivity.7
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
        TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
        this.passwordInfoRequestTextView = textInfoPrivacyCell3;
        textInfoPrivacyCell3.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
        this.passwordInfoRequestTextView.setText(LocaleController.formatString("PassportRequestPasswordInfo", R.string.PassportRequestPasswordInfo, new Object[0]));
        this.linearLayout2.addView(this.passwordInfoRequestTextView, LayoutHelper.createLinear(-1, -2));
        TextView textView3 = new TextView(context);
        this.passwordForgotButton = textView3;
        textView3.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
        this.passwordForgotButton.setTextSize(1, 14.0f);
        this.passwordForgotButton.setText(LocaleController.getString(R.string.ForgotPassword));
        this.passwordForgotButton.setPadding(0, 0, 0, 0);
        this.linearLayout2.addView(this.passwordForgotButton, LayoutHelper.createLinear(-2, 30, (LocaleController.isRTL ? 5 : 3) | 48, 21, 0, 21, 0));
        this.passwordForgotButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda34
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PassportActivity.this.lambda$createPasswordInterface$12(view);
            }
        });
        updatePasswordInterface();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01f9  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0221  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0294  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0316  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0324  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0341 A[SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r12v3, types: [android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r12v6, types: [android.widget.LinearLayout] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void createPhoneInterface(Context context) {
        String str;
        String str2;
        FrameLayout frameLayout;
        EditTextBoldCursor editTextBoldCursor;
        int i;
        EditTextBoldCursor editTextBoldCursor2;
        TextWatcher textWatcher;
        FrameLayout frameLayout2;
        this.actionBar.setTitle(LocaleController.getString(R.string.PassportPhone));
        this.languageMap = new HashMap();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("countries.txt")));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String[] split = readLine.split(";");
                this.countriesArray.add(0, split[2]);
                this.countriesMap.put(split[2], split[0]);
                this.codesMap.put(split[0], split[2]);
                if (split.length > 3) {
                    this.phoneFormatMap.put(split[0], split[3]);
                }
                this.languageMap.put(split[1], split[2]);
            }
            bufferedReader.close();
        } catch (Exception e) {
            FileLog.e(e);
        }
        Collections.sort(this.countriesArray, new BoostRepository$$ExternalSyntheticLambda31());
        String str3 = UserConfig.getInstance(this.currentAccount).getCurrentUser().phone;
        TextSettingsCell textSettingsCell = new TextSettingsCell(context);
        textSettingsCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
        textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        int i2 = R.string.PassportPhoneUseSame;
        PhoneFormat phoneFormat = PhoneFormat.getInstance();
        textSettingsCell.setText(LocaleController.formatString("PassportPhoneUseSame", i2, phoneFormat.format("+" + str3)), false);
        this.linearLayout2.addView(textSettingsCell, LayoutHelper.createLinear(-1, -2));
        textSettingsCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PassportActivity.this.lambda$createPhoneInterface$26(view);
            }
        });
        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
        this.bottomCell = textInfoPrivacyCell;
        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
        this.bottomCell.setText(LocaleController.getString(R.string.PassportPhoneUseSameInfo));
        this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
        HeaderCell headerCell = new HeaderCell(context);
        this.headerCell = headerCell;
        headerCell.setText(LocaleController.getString(R.string.PassportPhoneUseOther));
        this.headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
        this.inputFields = new EditTextBoldCursor[3];
        int i3 = 0;
        while (true) {
            str = null;
            if (i3 < 3) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                if (i3 == 2) {
                    editTextBoldCursorArr[i3] = new HintEditText(context);
                } else {
                    editTextBoldCursorArr[i3] = new EditTextBoldCursor(context);
                }
                if (i3 == 1) {
                    ?? linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(0);
                    frameLayout2 = linearLayout;
                } else if (i3 == 2) {
                    frameLayout = (ViewGroup) this.inputFields[1].getParent();
                    this.inputFields[i3].setTag(Integer.valueOf(i3));
                    this.inputFields[i3].setTextSize(1, 16.0f);
                    this.inputFields[i3].setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
                    EditTextBoldCursor editTextBoldCursor3 = this.inputFields[i3];
                    int i4 = Theme.key_windowBackgroundWhiteBlackText;
                    editTextBoldCursor3.setTextColor(Theme.getColor(i4));
                    this.inputFields[i3].setBackgroundDrawable(null);
                    this.inputFields[i3].setCursorColor(Theme.getColor(i4));
                    this.inputFields[i3].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.inputFields[i3].setCursorWidth(1.5f);
                    EditTextBoldCursor[] editTextBoldCursorArr2 = this.inputFields;
                    if (i3 != 0) {
                        editTextBoldCursorArr2[i3].setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda9
                            @Override // android.view.View.OnTouchListener
                            public final boolean onTouch(View view, MotionEvent motionEvent) {
                                boolean lambda$createPhoneInterface$29;
                                lambda$createPhoneInterface$29 = PassportActivity.this.lambda$createPhoneInterface$29(view, motionEvent);
                                return lambda$createPhoneInterface$29;
                            }
                        });
                        this.inputFields[i3].setText(LocaleController.getString(R.string.ChooseCountry));
                        this.inputFields[i3].setInputType(0);
                        this.inputFields[i3].setFocusable(false);
                    } else {
                        editTextBoldCursorArr2[i3].setInputType(3);
                        EditTextBoldCursor[] editTextBoldCursorArr3 = this.inputFields;
                        if (i3 == 2) {
                            editTextBoldCursor = editTextBoldCursorArr3[i3];
                            i = 268435462;
                        } else {
                            editTextBoldCursor = editTextBoldCursorArr3[i3];
                            i = 268435461;
                        }
                        editTextBoldCursor.setImeOptions(i);
                    }
                    EditTextBoldCursor editTextBoldCursor4 = this.inputFields[i3];
                    editTextBoldCursor4.setSelection(editTextBoldCursor4.length());
                    if (i3 != 1) {
                        TextView textView = new TextView(context);
                        this.plusTextView = textView;
                        textView.setText("+");
                        this.plusTextView.setTextColor(Theme.getColor(i4));
                        this.plusTextView.setTextSize(1, 16.0f);
                        frameLayout.addView(this.plusTextView, LayoutHelper.createLinear(-2, -2, 21.0f, 12.0f, 0.0f, 6.0f));
                        this.inputFields[i3].setPadding(AndroidUtilities.dp(10.0f), 0, 0, 0);
                        this.inputFields[i3].setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                        this.inputFields[i3].setGravity(19);
                        frameLayout.addView(this.inputFields[i3], LayoutHelper.createLinear(55, -2, 0.0f, 12.0f, 16.0f, 6.0f));
                        editTextBoldCursor2 = this.inputFields[i3];
                        textWatcher = new TextWatcher() { // from class: org.telegram.ui.PassportActivity.9
                            @Override // android.text.TextWatcher
                            public void afterTextChanged(Editable editable) {
                                String str4;
                                boolean z;
                                int indexOf;
                                if (PassportActivity.this.ignoreOnTextChange) {
                                    return;
                                }
                                PassportActivity.this.ignoreOnTextChange = true;
                                String stripExceptNumbers = PhoneFormat.stripExceptNumbers(PassportActivity.this.inputFields[1].getText().toString());
                                PassportActivity.this.inputFields[1].setText(stripExceptNumbers);
                                HintEditText hintEditText = (HintEditText) PassportActivity.this.inputFields[2];
                                if (stripExceptNumbers.length() == 0) {
                                    hintEditText.setHintText((String) null);
                                    hintEditText.setHint(LocaleController.getString(R.string.PaymentShippingPhoneNumber));
                                    PassportActivity.this.inputFields[0].setText(LocaleController.getString(R.string.ChooseCountry));
                                } else {
                                    int i5 = 4;
                                    if (stripExceptNumbers.length() > 4) {
                                        while (true) {
                                            if (i5 < 1) {
                                                str4 = null;
                                                z = false;
                                                break;
                                            }
                                            String substring = stripExceptNumbers.substring(0, i5);
                                            if (((String) PassportActivity.this.codesMap.get(substring)) != null) {
                                                String str5 = stripExceptNumbers.substring(i5) + PassportActivity.this.inputFields[2].getText().toString();
                                                PassportActivity.this.inputFields[1].setText(substring);
                                                z = true;
                                                str4 = str5;
                                                stripExceptNumbers = substring;
                                                break;
                                            }
                                            i5--;
                                        }
                                        if (!z) {
                                            str4 = stripExceptNumbers.substring(1) + PassportActivity.this.inputFields[2].getText().toString();
                                            EditTextBoldCursor editTextBoldCursor5 = PassportActivity.this.inputFields[1];
                                            stripExceptNumbers = stripExceptNumbers.substring(0, 1);
                                            editTextBoldCursor5.setText(stripExceptNumbers);
                                        }
                                    } else {
                                        str4 = null;
                                        z = false;
                                    }
                                    String str6 = (String) PassportActivity.this.codesMap.get(stripExceptNumbers);
                                    if (str6 == null || (indexOf = PassportActivity.this.countriesArray.indexOf(str6)) == -1) {
                                        hintEditText.setHintText((String) null);
                                        hintEditText.setHint(LocaleController.getString(R.string.PaymentShippingPhoneNumber));
                                        PassportActivity.this.inputFields[0].setText(LocaleController.getString(R.string.WrongCountry));
                                    } else {
                                        PassportActivity.this.inputFields[0].setText((CharSequence) PassportActivity.this.countriesArray.get(indexOf));
                                        String str7 = (String) PassportActivity.this.phoneFormatMap.get(stripExceptNumbers);
                                        if (str7 != null) {
                                            hintEditText.setHintText(str7.replace('X', (char) 8211));
                                            hintEditText.setHint((CharSequence) null);
                                        }
                                    }
                                    if (!z) {
                                        PassportActivity.this.inputFields[1].setSelection(PassportActivity.this.inputFields[1].getText().length());
                                    }
                                    if (str4 != null) {
                                        hintEditText.requestFocus();
                                        hintEditText.setText(str4);
                                        hintEditText.setSelection(hintEditText.length());
                                    }
                                }
                                PassportActivity.this.ignoreOnTextChange = false;
                            }

                            @Override // android.text.TextWatcher
                            public void beforeTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                            }

                            @Override // android.text.TextWatcher
                            public void onTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                            }
                        };
                    } else if (i3 == 2) {
                        this.inputFields[i3].setPadding(0, 0, 0, 0);
                        this.inputFields[i3].setGravity(19);
                        this.inputFields[i3].setHintText(null);
                        this.inputFields[i3].setHint(LocaleController.getString(R.string.PaymentShippingPhoneNumber));
                        frameLayout.addView(this.inputFields[i3], LayoutHelper.createLinear(-1, -2, 0.0f, 12.0f, 21.0f, 6.0f));
                        editTextBoldCursor2 = this.inputFields[i3];
                        textWatcher = new TextWatcher() { // from class: org.telegram.ui.PassportActivity.10
                            private int actionPosition;
                            private int characterAction = -1;

                            @Override // android.text.TextWatcher
                            public void afterTextChanged(Editable editable) {
                                int i5;
                                int i6;
                                if (PassportActivity.this.ignoreOnPhoneChange) {
                                    return;
                                }
                                HintEditText hintEditText = (HintEditText) PassportActivity.this.inputFields[2];
                                int selectionStart = hintEditText.getSelectionStart();
                                String obj = hintEditText.getText().toString();
                                if (this.characterAction == 3) {
                                    obj = obj.substring(0, this.actionPosition) + obj.substring(this.actionPosition + 1);
                                    selectionStart--;
                                }
                                StringBuilder sb = new StringBuilder(obj.length());
                                int i7 = 0;
                                while (i7 < obj.length()) {
                                    int i8 = i7 + 1;
                                    String substring = obj.substring(i7, i8);
                                    if ("0123456789".contains(substring)) {
                                        sb.append(substring);
                                    }
                                    i7 = i8;
                                }
                                PassportActivity.this.ignoreOnPhoneChange = true;
                                String hintText = hintEditText.getHintText();
                                if (hintText != null) {
                                    int i9 = 0;
                                    while (true) {
                                        if (i9 >= sb.length()) {
                                            break;
                                        } else if (i9 < hintText.length()) {
                                            if (hintText.charAt(i9) == ' ') {
                                                sb.insert(i9, ' ');
                                                i9++;
                                                if (selectionStart == i9 && (i6 = this.characterAction) != 2 && i6 != 3) {
                                                    selectionStart++;
                                                }
                                            }
                                            i9++;
                                        } else {
                                            sb.insert(i9, ' ');
                                            if (selectionStart == i9 + 1 && (i5 = this.characterAction) != 2 && i5 != 3) {
                                                selectionStart++;
                                            }
                                        }
                                    }
                                }
                                hintEditText.setText(sb);
                                if (selectionStart >= 0) {
                                    hintEditText.setSelection(Math.min(selectionStart, hintEditText.length()));
                                }
                                hintEditText.onTextChange();
                                PassportActivity.this.ignoreOnPhoneChange = false;
                            }

                            @Override // android.text.TextWatcher
                            public void beforeTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                                int i8;
                                if (i6 == 0 && i7 == 1) {
                                    this.characterAction = 1;
                                    return;
                                }
                                if (i6 != 1 || i7 != 0) {
                                    i8 = -1;
                                } else if (charSequence.charAt(i5) == ' ' && i5 > 0) {
                                    this.characterAction = 3;
                                    this.actionPosition = i5 - 1;
                                    return;
                                } else {
                                    i8 = 2;
                                }
                                this.characterAction = i8;
                            }

                            @Override // android.text.TextWatcher
                            public void onTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                            }
                        };
                    } else {
                        this.inputFields[i3].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                        this.inputFields[i3].setGravity(LocaleController.isRTL ? 5 : 3);
                        frameLayout.addView(this.inputFields[i3], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                        this.inputFields[i3].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda10
                            @Override // android.widget.TextView.OnEditorActionListener
                            public final boolean onEditorAction(TextView textView2, int i5, KeyEvent keyEvent) {
                                boolean lambda$createPhoneInterface$30;
                                lambda$createPhoneInterface$30 = PassportActivity.this.lambda$createPhoneInterface$30(textView2, i5, keyEvent);
                                return lambda$createPhoneInterface$30;
                            }
                        });
                        if (i3 == 2) {
                            this.inputFields[i3].setOnKeyListener(new View.OnKeyListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda11
                                @Override // android.view.View.OnKeyListener
                                public final boolean onKey(View view, int i5, KeyEvent keyEvent) {
                                    boolean lambda$createPhoneInterface$31;
                                    lambda$createPhoneInterface$31 = PassportActivity.this.lambda$createPhoneInterface$31(view, i5, keyEvent);
                                    return lambda$createPhoneInterface$31;
                                }
                            });
                        }
                        if (i3 == 0) {
                            View view = new View(context);
                            this.dividers.add(view);
                            view.setBackgroundColor(Theme.getColor(Theme.key_divider));
                            frameLayout.addView(view, new FrameLayout.LayoutParams(-1, 1, 83));
                        }
                        i3++;
                    }
                    editTextBoldCursor2.addTextChangedListener(textWatcher);
                    this.inputFields[i3].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda10
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView2, int i5, KeyEvent keyEvent) {
                            boolean lambda$createPhoneInterface$30;
                            lambda$createPhoneInterface$30 = PassportActivity.this.lambda$createPhoneInterface$30(textView2, i5, keyEvent);
                            return lambda$createPhoneInterface$30;
                        }
                    });
                    if (i3 == 2) {
                    }
                    if (i3 == 0) {
                    }
                    i3++;
                } else {
                    frameLayout2 = new FrameLayout(context);
                }
                this.linearLayout2.addView(frameLayout2, LayoutHelper.createLinear(-1, 50));
                frameLayout2.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                frameLayout = frameLayout2;
                this.inputFields[i3].setTag(Integer.valueOf(i3));
                this.inputFields[i3].setTextSize(1, 16.0f);
                this.inputFields[i3].setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
                EditTextBoldCursor editTextBoldCursor32 = this.inputFields[i3];
                int i42 = Theme.key_windowBackgroundWhiteBlackText;
                editTextBoldCursor32.setTextColor(Theme.getColor(i42));
                this.inputFields[i3].setBackgroundDrawable(null);
                this.inputFields[i3].setCursorColor(Theme.getColor(i42));
                this.inputFields[i3].setCursorSize(AndroidUtilities.dp(20.0f));
                this.inputFields[i3].setCursorWidth(1.5f);
                EditTextBoldCursor[] editTextBoldCursorArr22 = this.inputFields;
                if (i3 != 0) {
                }
                EditTextBoldCursor editTextBoldCursor42 = this.inputFields[i3];
                editTextBoldCursor42.setSelection(editTextBoldCursor42.length());
                if (i3 != 1) {
                }
                editTextBoldCursor2.addTextChangedListener(textWatcher);
                this.inputFields[i3].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda10
                    @Override // android.widget.TextView.OnEditorActionListener
                    public final boolean onEditorAction(TextView textView2, int i5, KeyEvent keyEvent) {
                        boolean lambda$createPhoneInterface$30;
                        lambda$createPhoneInterface$30 = PassportActivity.this.lambda$createPhoneInterface$30(textView2, i5, keyEvent);
                        return lambda$createPhoneInterface$30;
                    }
                });
                if (i3 == 2) {
                }
                if (i3 == 0) {
                }
                i3++;
            } else {
                try {
                    break;
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
        }
        TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
        if (telephonyManager != null) {
            str = telephonyManager.getSimCountryIso().toUpperCase();
        }
        if (str != null && (str2 = (String) this.languageMap.get(str)) != null && this.countriesArray.indexOf(str2) != -1) {
            this.inputFields[1].setText((CharSequence) this.countriesMap.get(str2));
        }
        TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
        this.bottomCell = textInfoPrivacyCell2;
        textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
        this.bottomCell.setText(LocaleController.getString(R.string.PassportPhoneUploadInfo));
        this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
    }

    private void createPhoneVerificationInterface(Context context) {
        this.actionBar.setTitle(LocaleController.getString(R.string.PassportPhone));
        FrameLayout frameLayout = new FrameLayout(context);
        this.scrollView.addView(frameLayout, LayoutHelper.createScroll(-1, -2, 51));
        for (int i = 0; i < 3; i++) {
            this.views[i] = new PhoneConfirmationView(context, i + 2);
            this.views[i].setVisibility(8);
            frameLayout.addView(this.views[i], LayoutHelper.createFrame(-1, -1.0f, 51, AndroidUtilities.isTablet() ? 26.0f : 18.0f, 30.0f, AndroidUtilities.isTablet() ? 26.0f : 18.0f, 0.0f));
        }
        Bundle bundle = new Bundle();
        bundle.putString("phone", (String) this.currentValues.get("phone"));
        fillNextCodeParams(bundle, this.currentPhoneVerification, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:137:0x02dd  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x02df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void createRequestInterface(Context context) {
        TLRPC$User tLRPC$User;
        TextInfoPrivacyCell textInfoPrivacyCell;
        SpannableStringBuilder replaceTags;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType;
        TLRPC$SecureValueType tLRPC$TL_secureValueTypeAddress;
        ArrayList arrayList;
        boolean z;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2;
        ArrayList arrayList2;
        ArrayList arrayList3;
        int i;
        ArrayList arrayList4;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3;
        TLRPC$SecureValueType tLRPC$TL_secureValueTypeAddress2;
        ArrayList arrayList5;
        int i2;
        int i3 = 1;
        int i4 = 0;
        if (this.currentForm != null) {
            for (int i5 = 0; i5 < this.currentForm.users.size(); i5++) {
                TLRPC$User tLRPC$User2 = (TLRPC$User) this.currentForm.users.get(i5);
                if (tLRPC$User2.id == this.currentBotId) {
                    tLRPC$User = tLRPC$User2;
                    break;
                }
            }
        }
        tLRPC$User = null;
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        this.actionBar.setTitle(LocaleController.getString(R.string.TelegramPassport));
        this.actionBar.createMenu().addItem(1, R.drawable.msg_info);
        if (tLRPC$User != null) {
            FrameLayout frameLayout2 = new FrameLayout(context);
            this.linearLayout2.addView(frameLayout2, LayoutHelper.createLinear(-1, 100));
            BackupImageView backupImageView = new BackupImageView(context);
            backupImageView.setRoundRadius(AndroidUtilities.dp(32.0f));
            frameLayout2.addView(backupImageView, LayoutHelper.createFrame(64, 64.0f, 17, 0.0f, 8.0f, 0.0f, 0.0f));
            backupImageView.setForUserOrChat(tLRPC$User, new AvatarDrawable(tLRPC$User));
            TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell2;
            textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_top, Theme.key_windowBackgroundGrayShadow));
            this.bottomCell.setText(AndroidUtilities.replaceTags(LocaleController.formatString("PassportRequest", R.string.PassportRequest, UserObject.getFirstName(tLRPC$User))));
            this.bottomCell.getTextView().setGravity(1);
            ((FrameLayout.LayoutParams) this.bottomCell.getTextView().getLayoutParams()).gravity = 1;
            this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
        }
        HeaderCell headerCell = new HeaderCell(context);
        this.headerCell = headerCell;
        headerCell.setText(LocaleController.getString(R.string.PassportRequestedInformation));
        this.headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
        TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm = this.currentForm;
        if (tLRPC$TL_account_authorizationForm != null) {
            int size = tLRPC$TL_account_authorizationForm.required_types.size();
            ArrayList arrayList6 = new ArrayList();
            ArrayList arrayList7 = new ArrayList();
            int i6 = 0;
            boolean z2 = false;
            int i7 = 0;
            int i8 = 0;
            boolean z3 = false;
            while (i6 < size) {
                TLRPC$SecureRequiredType tLRPC$SecureRequiredType = (TLRPC$SecureRequiredType) this.currentForm.required_types.get(i6);
                if (tLRPC$SecureRequiredType instanceof TLRPC$TL_secureRequiredType) {
                    TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType4 = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType;
                    if (isPersonalDocument(tLRPC$TL_secureRequiredType4.type)) {
                        arrayList6.add(tLRPC$TL_secureRequiredType4);
                        i7 += i3;
                        i2 = 1;
                        i6 += i2;
                        i3 = 1;
                        i4 = 0;
                    } else {
                        if (isAddressDocument(tLRPC$TL_secureRequiredType4.type)) {
                            arrayList7.add(tLRPC$TL_secureRequiredType4);
                            i8 += i3;
                        } else {
                            TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_secureRequiredType4.type;
                            if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                                z2 = true;
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
                                i2 = 1;
                                z3 = true;
                                i6 += i2;
                                i3 = 1;
                                i4 = 0;
                            }
                        }
                        i2 = 1;
                        i6 += i2;
                        i3 = 1;
                        i4 = 0;
                    }
                } else {
                    if (tLRPC$SecureRequiredType instanceof TLRPC$TL_secureRequiredTypeOneOf) {
                        TLRPC$TL_secureRequiredTypeOneOf tLRPC$TL_secureRequiredTypeOneOf = (TLRPC$TL_secureRequiredTypeOneOf) tLRPC$SecureRequiredType;
                        if (!tLRPC$TL_secureRequiredTypeOneOf.types.isEmpty()) {
                            TLRPC$SecureRequiredType tLRPC$SecureRequiredType2 = (TLRPC$SecureRequiredType) tLRPC$TL_secureRequiredTypeOneOf.types.get(i4);
                            if (tLRPC$SecureRequiredType2 instanceof TLRPC$TL_secureRequiredType) {
                                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType5 = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType2;
                                if (isPersonalDocument(tLRPC$TL_secureRequiredType5.type)) {
                                    int size2 = tLRPC$TL_secureRequiredTypeOneOf.types.size();
                                    for (int i9 = 0; i9 < size2; i9++) {
                                        TLRPC$SecureRequiredType tLRPC$SecureRequiredType3 = (TLRPC$SecureRequiredType) tLRPC$TL_secureRequiredTypeOneOf.types.get(i9);
                                        if (tLRPC$SecureRequiredType3 instanceof TLRPC$TL_secureRequiredType) {
                                            arrayList6.add((TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType3);
                                        }
                                        i3 = 1;
                                    }
                                    i7 += i3;
                                } else if (isAddressDocument(tLRPC$TL_secureRequiredType5.type)) {
                                    int size3 = tLRPC$TL_secureRequiredTypeOneOf.types.size();
                                    for (int i10 = 0; i10 < size3; i10++) {
                                        TLRPC$SecureRequiredType tLRPC$SecureRequiredType4 = (TLRPC$SecureRequiredType) tLRPC$TL_secureRequiredTypeOneOf.types.get(i10);
                                        if (tLRPC$SecureRequiredType4 instanceof TLRPC$TL_secureRequiredType) {
                                            arrayList7.add((TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType4);
                                        }
                                    }
                                    i2 = 1;
                                    i8++;
                                    i6 += i2;
                                    i3 = 1;
                                    i4 = 0;
                                }
                            }
                        }
                    }
                    i2 = 1;
                    i6 += i2;
                    i3 = 1;
                    i4 = 0;
                }
            }
            boolean z4 = !z2 || i7 > 1;
            boolean z5 = !z3 || i8 > 1;
            int i11 = 0;
            while (i11 < size) {
                TLRPC$SecureRequiredType tLRPC$SecureRequiredType5 = (TLRPC$SecureRequiredType) this.currentForm.required_types.get(i11);
                if (tLRPC$SecureRequiredType5 instanceof TLRPC$TL_secureRequiredType) {
                    TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType6 = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType5;
                    TLRPC$SecureValueType tLRPC$SecureValueType2 = tLRPC$TL_secureRequiredType6.type;
                    if ((tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypePhone) || (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeEmail)) {
                        tLRPC$TL_secureRequiredType2 = tLRPC$TL_secureRequiredType6;
                        arrayList = null;
                    } else {
                        if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                            if (!z4) {
                                arrayList5 = arrayList6;
                                tLRPC$TL_secureRequiredType2 = tLRPC$TL_secureRequiredType6;
                                arrayList = arrayList5;
                            }
                            arrayList5 = null;
                            tLRPC$TL_secureRequiredType2 = tLRPC$TL_secureRequiredType6;
                            arrayList = arrayList5;
                        } else if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeAddress) {
                            if (!z5) {
                                arrayList5 = arrayList7;
                                tLRPC$TL_secureRequiredType2 = tLRPC$TL_secureRequiredType6;
                                arrayList = arrayList5;
                            }
                            arrayList5 = null;
                            tLRPC$TL_secureRequiredType2 = tLRPC$TL_secureRequiredType6;
                            arrayList = arrayList5;
                        } else {
                            if (z4 && isPersonalDocument(tLRPC$SecureValueType2)) {
                                arrayList4 = new ArrayList();
                                arrayList4.add(tLRPC$TL_secureRequiredType6);
                                tLRPC$TL_secureRequiredType3 = new TLRPC$TL_secureRequiredType();
                                tLRPC$TL_secureValueTypeAddress2 = new TLRPC$TL_secureValueTypePersonalDetails();
                            } else {
                                if (z5 && isAddressDocument(tLRPC$TL_secureRequiredType6.type)) {
                                    arrayList4 = new ArrayList();
                                    arrayList4.add(tLRPC$TL_secureRequiredType6);
                                    tLRPC$TL_secureRequiredType3 = new TLRPC$TL_secureRequiredType();
                                    tLRPC$TL_secureValueTypeAddress2 = new TLRPC$TL_secureValueTypeAddress();
                                }
                                arrayList2 = arrayList7;
                                arrayList3 = arrayList6;
                                i = size;
                            }
                            tLRPC$TL_secureRequiredType3.type = tLRPC$TL_secureValueTypeAddress2;
                            tLRPC$TL_secureRequiredType2 = tLRPC$TL_secureRequiredType3;
                            arrayList = arrayList4;
                            z = true;
                        }
                        arrayList2 = arrayList7;
                        ArrayList arrayList8 = arrayList;
                        arrayList3 = arrayList6;
                        boolean z6 = z;
                        i = size;
                        addField(context, tLRPC$TL_secureRequiredType2, arrayList8, z6, i11 == size + (-1));
                    }
                    z = false;
                    arrayList2 = arrayList7;
                    ArrayList arrayList82 = arrayList;
                    arrayList3 = arrayList6;
                    boolean z62 = z;
                    i = size;
                    addField(context, tLRPC$TL_secureRequiredType2, arrayList82, z62, i11 == size + (-1));
                } else {
                    if (tLRPC$SecureRequiredType5 instanceof TLRPC$TL_secureRequiredTypeOneOf) {
                        TLRPC$TL_secureRequiredTypeOneOf tLRPC$TL_secureRequiredTypeOneOf2 = (TLRPC$TL_secureRequiredTypeOneOf) tLRPC$SecureRequiredType5;
                        if (!tLRPC$TL_secureRequiredTypeOneOf2.types.isEmpty()) {
                            TLRPC$SecureRequiredType tLRPC$SecureRequiredType6 = (TLRPC$SecureRequiredType) tLRPC$TL_secureRequiredTypeOneOf2.types.get(0);
                            if (tLRPC$SecureRequiredType6 instanceof TLRPC$TL_secureRequiredType) {
                                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType7 = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType6;
                                if ((z4 && isPersonalDocument(tLRPC$TL_secureRequiredType7.type)) || (z5 && isAddressDocument(tLRPC$TL_secureRequiredType7.type))) {
                                    ArrayList arrayList9 = new ArrayList();
                                    int size4 = tLRPC$TL_secureRequiredTypeOneOf2.types.size();
                                    int i12 = 0;
                                    while (i12 < size4) {
                                        TLRPC$SecureRequiredType tLRPC$SecureRequiredType7 = (TLRPC$SecureRequiredType) tLRPC$TL_secureRequiredTypeOneOf2.types.get(i12);
                                        TLRPC$TL_secureRequiredTypeOneOf tLRPC$TL_secureRequiredTypeOneOf3 = tLRPC$TL_secureRequiredTypeOneOf2;
                                        if (tLRPC$SecureRequiredType7 instanceof TLRPC$TL_secureRequiredType) {
                                            arrayList9.add((TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType7);
                                        }
                                        i12++;
                                        tLRPC$TL_secureRequiredTypeOneOf2 = tLRPC$TL_secureRequiredTypeOneOf3;
                                    }
                                    if (isPersonalDocument(tLRPC$TL_secureRequiredType7.type)) {
                                        tLRPC$TL_secureRequiredType = new TLRPC$TL_secureRequiredType();
                                        tLRPC$TL_secureValueTypeAddress = new TLRPC$TL_secureValueTypePersonalDetails();
                                    } else {
                                        tLRPC$TL_secureRequiredType = new TLRPC$TL_secureRequiredType();
                                        tLRPC$TL_secureValueTypeAddress = new TLRPC$TL_secureValueTypeAddress();
                                    }
                                    tLRPC$TL_secureRequiredType.type = tLRPC$TL_secureValueTypeAddress;
                                    arrayList = arrayList9;
                                    z = true;
                                    tLRPC$TL_secureRequiredType2 = tLRPC$TL_secureRequiredType;
                                    arrayList2 = arrayList7;
                                    ArrayList arrayList822 = arrayList;
                                    arrayList3 = arrayList6;
                                    boolean z622 = z;
                                    i = size;
                                    addField(context, tLRPC$TL_secureRequiredType2, arrayList822, z622, i11 == size + (-1));
                                }
                            }
                        }
                    }
                    arrayList2 = arrayList7;
                    arrayList3 = arrayList6;
                    i = size;
                }
                i11++;
                arrayList6 = arrayList3;
                size = i;
                arrayList7 = arrayList2;
            }
        }
        if (tLRPC$User != null) {
            TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell3;
            textInfoPrivacyCell3.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
            TextInfoPrivacyCell textInfoPrivacyCell4 = this.bottomCell;
            int i13 = Theme.key_windowBackgroundWhiteGrayText4;
            textInfoPrivacyCell4.setLinkTextColorKey(i13);
            if (TextUtils.isEmpty(this.currentForm.privacy_policy_url)) {
                textInfoPrivacyCell = this.bottomCell;
                replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("PassportNoPolicy", R.string.PassportNoPolicy, UserObject.getFirstName(tLRPC$User), tLRPC$User.username));
            } else {
                String formatString = LocaleController.formatString("PassportPolicy", R.string.PassportPolicy, UserObject.getFirstName(tLRPC$User), tLRPC$User.username);
                replaceTags = new SpannableStringBuilder(formatString);
                int indexOf = formatString.indexOf(42);
                int lastIndexOf = formatString.lastIndexOf(42);
                if (indexOf != -1 && lastIndexOf != -1) {
                    this.bottomCell.getTextView().setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                    replaceTags.replace(lastIndexOf, lastIndexOf + 1, (CharSequence) "");
                    replaceTags.replace(indexOf, indexOf + 1, (CharSequence) "");
                    replaceTags.setSpan(new LinkSpan(), indexOf, lastIndexOf - 1, 33);
                }
                textInfoPrivacyCell = this.bottomCell;
            }
            textInfoPrivacyCell.setText(replaceTags);
            this.bottomCell.getTextView().setHighlightColor(Theme.getColor(i13));
            this.bottomCell.getTextView().setGravity(1);
            this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
        }
        FrameLayout frameLayout3 = new FrameLayout(context);
        this.bottomLayout = frameLayout3;
        frameLayout3.setBackgroundDrawable(Theme.createSelectorWithBackgroundDrawable(Theme.getColor(Theme.key_passport_authorizeBackground), Theme.getColor(Theme.key_passport_authorizeBackgroundSelected)));
        frameLayout.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 80));
        this.bottomLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PassportActivity.this.lambda$createRequestInterface$16(view);
            }
        });
        TextView textView = new TextView(context);
        this.acceptTextView = textView;
        textView.setCompoundDrawablePadding(AndroidUtilities.dp(8.0f));
        this.acceptTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.authorize, 0, 0, 0);
        this.acceptTextView.setTextColor(Theme.getColor(Theme.key_passport_authorizeText));
        this.acceptTextView.setText(LocaleController.getString(R.string.PassportAuthorize));
        this.acceptTextView.setTextSize(1, 14.0f);
        this.acceptTextView.setGravity(17);
        this.acceptTextView.setTypeface(AndroidUtilities.bold());
        this.bottomLayout.addView(this.acceptTextView, LayoutHelper.createFrame(-2, -1, 17));
        ContextProgressView contextProgressView = new ContextProgressView(context, 0);
        this.progressViewButton = contextProgressView;
        contextProgressView.setVisibility(4);
        this.bottomLayout.addView(this.progressViewButton, LayoutHelper.createFrame(-1, -1.0f));
        View view = new View(context);
        view.setBackgroundResource(R.drawable.header_shadow_reverse);
        frameLayout.addView(view, LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public EncryptionResult createSecureDocument(String str) {
        RandomAccessFile randomAccessFile;
        byte[] bArr = new byte[(int) new File(str).length()];
        try {
            randomAccessFile = new RandomAccessFile(str, "rws");
            try {
                randomAccessFile.readFully(bArr);
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            randomAccessFile = null;
        }
        EncryptionResult encryptData = encryptData(bArr);
        try {
            randomAccessFile.seek(0L);
            randomAccessFile.write(encryptData.encryptedData);
            randomAccessFile.close();
        } catch (Exception unused3) {
        }
        return encryptData;
    }

    private String decryptData(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        if (bArr == null || bArr2 == null || bArr2.length != 32 || bArr3 == null || bArr3.length != 32) {
            return null;
        }
        byte[] computeSHA512 = Utilities.computeSHA512(bArr2, bArr3);
        byte[] bArr4 = new byte[32];
        System.arraycopy(computeSHA512, 0, bArr4, 0, 32);
        byte[] bArr5 = new byte[16];
        System.arraycopy(computeSHA512, 32, bArr5, 0, 16);
        int length = bArr.length;
        byte[] bArr6 = new byte[length];
        System.arraycopy(bArr, 0, bArr6, 0, bArr.length);
        Utilities.aesCbcEncryptionByteArraySafe(bArr6, bArr4, bArr5, 0, length, 0, 0);
        if (Arrays.equals(Utilities.computeSHA256(bArr6), bArr3)) {
            int i = bArr6[0] & 255;
            return new String(bArr6, i, length - i);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] decryptSecret(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length != 32) {
            return null;
        }
        byte[] bArr3 = new byte[32];
        System.arraycopy(bArr2, 0, bArr3, 0, 32);
        byte[] bArr4 = new byte[16];
        System.arraycopy(bArr2, 32, bArr4, 0, 16);
        byte[] bArr5 = new byte[32];
        System.arraycopy(bArr, 0, bArr5, 0, 32);
        Utilities.aesCbcEncryptionByteArraySafe(bArr5, bArr3, bArr4, 0, 32, 0, 0);
        return bArr5;
    }

    private byte[] decryptValueSecret(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length != 32 || bArr2 == null || bArr2.length != 32) {
            return null;
        }
        byte[] bArr3 = new byte[32];
        System.arraycopy(this.saltedPassword, 0, bArr3, 0, 32);
        byte[] bArr4 = new byte[16];
        System.arraycopy(this.saltedPassword, 32, bArr4, 0, 16);
        byte[] bArr5 = new byte[32];
        System.arraycopy(this.secureSecret, 0, bArr5, 0, 32);
        Utilities.aesCbcEncryptionByteArraySafe(bArr5, bArr3, bArr4, 0, 32, 0, 0);
        if (checkSecret(bArr5, null)) {
            byte[] computeSHA512 = Utilities.computeSHA512(bArr5, bArr2);
            byte[] bArr6 = new byte[32];
            System.arraycopy(computeSHA512, 0, bArr6, 0, 32);
            byte[] bArr7 = new byte[16];
            System.arraycopy(computeSHA512, 32, bArr7, 0, 16);
            byte[] bArr8 = new byte[32];
            System.arraycopy(bArr, 0, bArr8, 0, 32);
            Utilities.aesCbcEncryptionByteArraySafe(bArr8, bArr6, bArr7, 0, 32, 0, 0);
            return bArr8;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0018, code lost:
        if (r15 != null) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void deleteValueInternal(final TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, final TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, final ArrayList arrayList, final boolean z, final Runnable runnable, final ErrorRunnable errorRunnable, final boolean z2) {
        if (tLRPC$TL_secureRequiredType == null) {
            return;
        }
        TLRPC$TL_account_deleteSecureValue tLRPC$TL_account_deleteSecureValue = new TLRPC$TL_account_deleteSecureValue();
        if (!z2 || tLRPC$TL_secureRequiredType2 == null) {
            if (z) {
                tLRPC$TL_account_deleteSecureValue.types.add(tLRPC$TL_secureRequiredType.type);
            }
        }
        tLRPC$TL_account_deleteSecureValue.types.add(tLRPC$TL_secureRequiredType2.type);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_deleteSecureValue, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda61
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PassportActivity.this.lambda$deleteValueInternal$60(errorRunnable, z2, tLRPC$TL_secureRequiredType2, tLRPC$TL_secureRequiredType, z, arrayList, runnable, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public EncryptionResult encryptData(byte[] bArr) {
        byte[] randomSecret = getRandomSecret();
        int nextInt = Utilities.random.nextInt(NotificationCenter.availableEffectsUpdate) + 32;
        while ((bArr.length + nextInt) % 16 != 0) {
            nextInt++;
        }
        byte[] bArr2 = new byte[nextInt];
        Utilities.random.nextBytes(bArr2);
        bArr2[0] = (byte) nextInt;
        int length = nextInt + bArr.length;
        byte[] bArr3 = new byte[length];
        System.arraycopy(bArr2, 0, bArr3, 0, nextInt);
        System.arraycopy(bArr, 0, bArr3, nextInt, bArr.length);
        byte[] computeSHA256 = Utilities.computeSHA256(bArr3);
        byte[] computeSHA512 = Utilities.computeSHA512(randomSecret, computeSHA256);
        byte[] bArr4 = new byte[32];
        System.arraycopy(computeSHA512, 0, bArr4, 0, 32);
        byte[] bArr5 = new byte[16];
        System.arraycopy(computeSHA512, 32, bArr5, 0, 16);
        Utilities.aesCbcEncryptionByteArraySafe(bArr3, bArr4, bArr5, 0, length, 0, 1);
        byte[] bArr6 = new byte[32];
        System.arraycopy(this.saltedPassword, 0, bArr6, 0, 32);
        byte[] bArr7 = new byte[16];
        System.arraycopy(this.saltedPassword, 32, bArr7, 0, 16);
        byte[] bArr8 = new byte[32];
        System.arraycopy(this.secureSecret, 0, bArr8, 0, 32);
        Utilities.aesCbcEncryptionByteArraySafe(bArr8, bArr6, bArr7, 0, 32, 0, 0);
        byte[] computeSHA5122 = Utilities.computeSHA512(bArr8, computeSHA256);
        byte[] bArr9 = new byte[32];
        System.arraycopy(computeSHA5122, 0, bArr9, 0, 32);
        byte[] bArr10 = new byte[16];
        System.arraycopy(computeSHA5122, 32, bArr10, 0, 16);
        byte[] bArr11 = new byte[32];
        System.arraycopy(randomSecret, 0, bArr11, 0, 32);
        Utilities.aesCbcEncryptionByteArraySafe(bArr11, bArr9, bArr10, 0, 32, 0, 1);
        return new EncryptionResult(bArr3, bArr11, randomSecret, computeSHA256, bArr4, bArr5);
    }

    private void fillInitialValues() {
        if (this.initialValues != null) {
            return;
        }
        this.initialValues = getCurrentValues();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fillNextCodeParams(Bundle bundle, TLRPC$TL_auth_sentCode tLRPC$TL_auth_sentCode, boolean z) {
        int i;
        bundle.putString("phoneHash", tLRPC$TL_auth_sentCode.phone_code_hash);
        TLRPC$auth_CodeType tLRPC$auth_CodeType = tLRPC$TL_auth_sentCode.next_type;
        if (tLRPC$auth_CodeType instanceof TLRPC$TL_auth_codeTypeCall) {
            bundle.putInt("nextType", 4);
        } else if (tLRPC$auth_CodeType instanceof TLRPC$TL_auth_codeTypeFlashCall) {
            bundle.putInt("nextType", 3);
        } else if (tLRPC$auth_CodeType instanceof TLRPC$TL_auth_codeTypeSms) {
            bundle.putInt("nextType", 2);
        }
        if (tLRPC$TL_auth_sentCode.timeout == 0) {
            tLRPC$TL_auth_sentCode.timeout = 60;
        }
        bundle.putInt("timeout", tLRPC$TL_auth_sentCode.timeout * 1000);
        TLRPC$auth_SentCodeType tLRPC$auth_SentCodeType = tLRPC$TL_auth_sentCode.type;
        if (tLRPC$auth_SentCodeType instanceof TLRPC$TL_auth_sentCodeTypeCall) {
            bundle.putInt("type", 4);
            bundle.putInt("length", tLRPC$TL_auth_sentCode.type.length);
            setPage(2, z, bundle);
            return;
        }
        if (tLRPC$auth_SentCodeType instanceof TLRPC$TL_auth_sentCodeTypeFlashCall) {
            bundle.putInt("type", 3);
            bundle.putString("pattern", tLRPC$TL_auth_sentCode.type.pattern);
            i = 1;
        } else if (!(tLRPC$auth_SentCodeType instanceof TLRPC$TL_auth_sentCodeTypeSms)) {
            return;
        } else {
            bundle.putInt("type", 2);
            bundle.putInt("length", tLRPC$TL_auth_sentCode.type.length);
            i = 0;
        }
        setPage(i, z, bundle);
    }

    private String getCurrentValues() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (true) {
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            if (i >= editTextBoldCursorArr.length) {
                break;
            }
            sb.append((CharSequence) editTextBoldCursorArr[i].getText());
            sb.append(",");
            i++;
        }
        if (this.inputExtraFields != null) {
            int i2 = 0;
            while (true) {
                EditTextBoldCursor[] editTextBoldCursorArr2 = this.inputExtraFields;
                if (i2 >= editTextBoldCursorArr2.length) {
                    break;
                }
                sb.append((CharSequence) editTextBoldCursorArr2[i2].getText());
                sb.append(",");
                i2++;
            }
        }
        int size = this.documents.size();
        for (int i3 = 0; i3 < size; i3++) {
            sb.append(((SecureDocument) this.documents.get(i3)).secureFile.id);
        }
        SecureDocument secureDocument = this.frontDocument;
        if (secureDocument != null) {
            sb.append(secureDocument.secureFile.id);
        }
        SecureDocument secureDocument2 = this.reverseDocument;
        if (secureDocument2 != null) {
            sb.append(secureDocument2.secureFile.id);
        }
        SecureDocument secureDocument3 = this.selfieDocument;
        if (secureDocument3 != null) {
            sb.append(secureDocument3.secureFile.id);
        }
        int size2 = this.translationDocuments.size();
        for (int i4 = 0; i4 < size2; i4++) {
            sb.append(((SecureDocument) this.translationDocuments.get(i4)).secureFile.id);
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDocumentHash(SecureDocument secureDocument) {
        byte[] bArr;
        if (secureDocument != null) {
            TLRPC$TL_secureFile tLRPC$TL_secureFile = secureDocument.secureFile;
            if (tLRPC$TL_secureFile == null || (bArr = tLRPC$TL_secureFile.file_hash) == null) {
                byte[] bArr2 = secureDocument.fileHash;
                return bArr2 != null ? Base64.encodeToString(bArr2, 2) : "";
            }
            return Base64.encodeToString(bArr, 2);
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getFieldCost(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2006252145:
                if (str.equals("residence_country_code")) {
                    c = 0;
                    break;
                }
                break;
            case -1537298398:
                if (str.equals("last_name_native")) {
                    c = 1;
                    break;
                }
                break;
            case -1249512767:
                if (str.equals("gender")) {
                    c = 2;
                    break;
                }
                break;
            case -796150911:
                if (str.equals("street_line1")) {
                    c = 3;
                    break;
                }
                break;
            case -796150910:
                if (str.equals("street_line2")) {
                    c = 4;
                    break;
                }
                break;
            case -160985414:
                if (str.equals("first_name")) {
                    c = 5;
                    break;
                }
                break;
            case 3053931:
                if (str.equals("city")) {
                    c = 6;
                    break;
                }
                break;
            case 109757585:
                if (str.equals("state")) {
                    c = 7;
                    break;
                }
                break;
            case 421072629:
                if (str.equals("middle_name")) {
                    c = '\b';
                    break;
                }
                break;
            case 451516732:
                if (str.equals("first_name_native")) {
                    c = '\t';
                    break;
                }
                break;
            case 475919162:
                if (str.equals("expiry_date")) {
                    c = '\n';
                    break;
                }
                break;
            case 506677093:
                if (str.equals("document_no")) {
                    c = 11;
                    break;
                }
                break;
            case 1168724782:
                if (str.equals("birth_date")) {
                    c = '\f';
                    break;
                }
                break;
            case 1181577377:
                if (str.equals("middle_name_native")) {
                    c = '\r';
                    break;
                }
                break;
            case 1481071862:
                if (str.equals("country_code")) {
                    c = 14;
                    break;
                }
                break;
            case 2002465324:
                if (str.equals("post_code")) {
                    c = 15;
                    break;
                }
                break;
            case 2013122196:
                if (str.equals("last_name")) {
                    c = 16;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 26;
            case 1:
            case 16:
                return 22;
            case 2:
                return 24;
            case 3:
                return 29;
            case 4:
                return 30;
            case 5:
            case '\t':
                return 20;
            case 6:
                return 32;
            case 7:
                return 33;
            case '\b':
            case '\r':
                return 21;
            case '\n':
                return 28;
            case 11:
                return 27;
            case '\f':
                return 23;
            case 14:
                return 25;
            case 15:
                return 31;
            default:
                return 100;
        }
    }

    private int getMaxSelectedDocuments() {
        ArrayList arrayList;
        int i = this.uploadingFileType;
        if (i == 0) {
            arrayList = this.documents;
        } else if (i != 4) {
            return 1;
        } else {
            arrayList = this.translationDocuments;
        }
        return 20 - arrayList.size();
    }

    private String getNameForType(TLRPC$SecureValueType tLRPC$SecureValueType) {
        return tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails ? "personal_details" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport ? "passport" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport ? "internal_passport" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense ? "driver_license" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard ? "identity_card" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill ? "utility_bill" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress ? "address" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement ? "bank_statement" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement ? "rental_agreement" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration ? "temporary_registration" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration ? "passport_registration" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail ? "email" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone ? "phone" : "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] getRandomSecret() {
        byte[] bArr = new byte[32];
        Utilities.random.nextBytes(bArr);
        int i = 0;
        for (int i2 = 0; i2 < 32; i2++) {
            i += 255 & bArr[i2];
        }
        int i3 = i % NotificationCenter.didClearDatabase;
        if (i3 != 239) {
            int i4 = NotificationCenter.locationPermissionGranted - i3;
            int nextInt = Utilities.random.nextInt(32);
            int i5 = (bArr[nextInt] & 255) + i4;
            if (i5 < 255) {
                i5 += NotificationCenter.didClearDatabase;
            }
            bArr[nextInt] = (byte) (i5 % NotificationCenter.didClearDatabase);
        }
        return bArr;
    }

    private SecureDocumentKey getSecureDocumentKey(byte[] bArr, byte[] bArr2) {
        byte[] computeSHA512 = Utilities.computeSHA512(decryptValueSecret(bArr, bArr2), bArr2);
        byte[] bArr3 = new byte[32];
        System.arraycopy(computeSHA512, 0, bArr3, 0, 32);
        byte[] bArr4 = new byte[16];
        System.arraycopy(computeSHA512, 32, bArr4, 0, 16);
        return new SecureDocumentKey(bArr3, bArr4);
    }

    private String getTextForType(TLRPC$SecureValueType tLRPC$SecureValueType) {
        int i;
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) {
            i = R.string.ActionBotDocumentPassport;
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense) {
            i = R.string.ActionBotDocumentDriverLicence;
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard) {
            i = R.string.ActionBotDocumentIdentityCard;
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill) {
            i = R.string.ActionBotDocumentUtilityBill;
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement) {
            i = R.string.ActionBotDocumentBankStatement;
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
            i = R.string.ActionBotDocumentRentalAgreement;
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport) {
            i = R.string.ActionBotDocumentInternalPassport;
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration) {
            i = R.string.ActionBotDocumentPassportRegistration;
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
            i = R.string.ActionBotDocumentTemporaryRegistration;
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone) {
            i = R.string.ActionBotDocumentPhone;
        } else if (!(tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail)) {
            return "";
        } else {
            i = R.string.ActionBotDocumentEmail;
        }
        return LocaleController.getString(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getTranslitString(String str) {
        return LocaleController.getInstance().getTranslitString(str, true);
    }

    private TLRPC$TL_secureValue getValueByType(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, boolean z) {
        JSONObject jSONObject;
        int i;
        if (tLRPC$TL_secureRequiredType == null) {
            return null;
        }
        int size = this.currentForm.values.size();
        for (int i2 = 0; i2 < size; i2++) {
            TLRPC$TL_secureValue tLRPC$TL_secureValue = (TLRPC$TL_secureValue) this.currentForm.values.get(i2);
            if (tLRPC$TL_secureRequiredType.type.getClass() == tLRPC$TL_secureValue.type.getClass()) {
                if (z) {
                    if (tLRPC$TL_secureRequiredType.selfie_required && !(tLRPC$TL_secureValue.selfie instanceof TLRPC$TL_secureFile)) {
                        return null;
                    }
                    if (tLRPC$TL_secureRequiredType.translation_required && tLRPC$TL_secureValue.translation.isEmpty()) {
                        return null;
                    }
                    if (isAddressDocument(tLRPC$TL_secureRequiredType.type) && tLRPC$TL_secureValue.files.isEmpty()) {
                        return null;
                    }
                    if (isPersonalDocument(tLRPC$TL_secureRequiredType.type) && !(tLRPC$TL_secureValue.front_side instanceof TLRPC$TL_secureFile)) {
                        return null;
                    }
                    TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
                    if (((tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard)) && !(tLRPC$TL_secureValue.reverse_side instanceof TLRPC$TL_secureFile)) {
                        return null;
                    }
                    boolean z2 = tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails;
                    if (z2 || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress)) {
                        String[] strArr = z2 ? tLRPC$TL_secureRequiredType.native_names ? new String[]{"first_name_native", "last_name_native", "birth_date", "gender", "country_code", "residence_country_code"} : new String[]{"first_name", "last_name", "birth_date", "gender", "country_code", "residence_country_code"} : new String[]{"street_line1", "street_line2", "post_code", "city", "state", "country_code"};
                        try {
                            TLRPC$TL_secureData tLRPC$TL_secureData = tLRPC$TL_secureValue.data;
                            jSONObject = new JSONObject(decryptData(tLRPC$TL_secureData.data, decryptValueSecret(tLRPC$TL_secureData.secret, tLRPC$TL_secureData.data_hash), tLRPC$TL_secureValue.data.data_hash));
                        } catch (Throwable unused) {
                        }
                        while (i < strArr.length) {
                            i = (jSONObject.has(strArr[i]) && !TextUtils.isEmpty(jSONObject.getString(strArr[i]))) ? i + 1 : 0;
                            return null;
                        }
                    }
                }
                return tLRPC$TL_secureValue;
            }
        }
        return null;
    }

    private TextDetailSecureCell getViewByType(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType) {
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2;
        TextDetailSecureCell textDetailSecureCell = (TextDetailSecureCell) this.typesViews.get(tLRPC$TL_secureRequiredType);
        return (textDetailSecureCell != null || (tLRPC$TL_secureRequiredType2 = (TLRPC$TL_secureRequiredType) this.documentsToTypesLink.get(tLRPC$TL_secureRequiredType)) == null) ? textDetailSecureCell : (TextDetailSecureCell) this.typesViews.get(tLRPC$TL_secureRequiredType2);
    }

    private boolean hasNotValueForType(Class cls) {
        int size = this.currentForm.values.size();
        for (int i = 0; i < size; i++) {
            if (((TLRPC$TL_secureValue) this.currentForm.values.get(i)).type.getClass() == cls) {
                return false;
            }
        }
        return true;
    }

    private boolean hasUnfilledValues() {
        return hasNotValueForType(TLRPC$TL_secureValueTypePhone.class) || hasNotValueForType(TLRPC$TL_secureValueTypeEmail.class) || hasNotValueForType(TLRPC$TL_secureValueTypePersonalDetails.class) || hasNotValueForType(TLRPC$TL_secureValueTypePassport.class) || hasNotValueForType(TLRPC$TL_secureValueTypeInternalPassport.class) || hasNotValueForType(TLRPC$TL_secureValueTypeIdentityCard.class) || hasNotValueForType(TLRPC$TL_secureValueTypeDriverLicense.class) || hasNotValueForType(TLRPC$TL_secureValueTypeAddress.class) || hasNotValueForType(TLRPC$TL_secureValueTypeUtilityBill.class) || hasNotValueForType(TLRPC$TL_secureValueTypePassportRegistration.class) || hasNotValueForType(TLRPC$TL_secureValueTypeTemporaryRegistration.class) || hasNotValueForType(TLRPC$TL_secureValueTypeBankStatement.class) || hasNotValueForType(TLRPC$TL_secureValueTypeRentalAgreement.class);
    }

    private boolean isAddressDocument(TLRPC$SecureValueType tLRPC$SecureValueType) {
        return (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isHasNotAnyChanges() {
        String str = this.initialValues;
        return str == null || str.equals(getCurrentValues());
    }

    private boolean isPersonalDocument(TLRPC$SecureValueType tLRPC$SecureValueType) {
        return (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addDocumentView$55(int i, View view) {
        PhotoViewer photoViewer;
        ArrayList arrayList;
        ArrayList arrayList2;
        SecureDocument secureDocument;
        this.uploadingFileType = i;
        this.currentPhotoViewerLayout = i == 1 ? this.selfieLayout : i == 4 ? this.translationLayout : i == 2 ? this.frontLayout : i == 3 ? this.reverseLayout : this.documentsLayout;
        SecureDocument secureDocument2 = (SecureDocument) view.getTag();
        PhotoViewer.getInstance().setParentActivity(this);
        if (i == 1) {
            arrayList2 = new ArrayList();
            secureDocument = this.selfieDocument;
        } else if (i == 2) {
            arrayList2 = new ArrayList();
            secureDocument = this.frontDocument;
        } else if (i != 3) {
            if (i == 0) {
                photoViewer = PhotoViewer.getInstance();
                arrayList = this.documents;
            } else {
                photoViewer = PhotoViewer.getInstance();
                arrayList = this.translationDocuments;
            }
            photoViewer.openPhoto(arrayList, arrayList.indexOf(secureDocument2), this.provider);
            return;
        } else {
            arrayList2 = new ArrayList();
            secureDocument = this.reverseDocument;
        }
        arrayList2.add(secureDocument);
        PhotoViewer.getInstance().openPhoto(arrayList2, 0, this.provider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addDocumentView$56(SecureDocument secureDocument, int i, SecureDocumentCell secureDocumentCell, String str, DialogInterface dialogInterface, int i2) {
        LinearLayout linearLayout;
        this.documentsCells.remove(secureDocument);
        if (i == 1) {
            this.selfieDocument = null;
            linearLayout = this.selfieLayout;
        } else if (i == 4) {
            this.translationDocuments.remove(secureDocument);
            linearLayout = this.translationLayout;
        } else if (i == 2) {
            this.frontDocument = null;
            linearLayout = this.frontLayout;
        } else if (i == 3) {
            this.reverseDocument = null;
            linearLayout = this.reverseLayout;
        } else {
            this.documents.remove(secureDocument);
            linearLayout = this.documentsLayout;
        }
        linearLayout.removeView(secureDocumentCell);
        if (str != null) {
            HashMap hashMap = this.documentsErrors;
            if (hashMap != null) {
                hashMap.remove(str);
            }
            HashMap hashMap2 = this.errorsValues;
            if (hashMap2 != null) {
                hashMap2.remove(str);
            }
        }
        updateUploadText(i);
        String str2 = secureDocument.path;
        if (str2 == null || this.uploadingDocuments.remove(str2) == null) {
            return;
        }
        if (this.uploadingDocuments.isEmpty()) {
            this.doneItem.setEnabled(true);
            this.doneItem.setAlpha(1.0f);
        }
        FileLoader.getInstance(this.currentAccount).cancelFileUpload(secureDocument.path, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$addDocumentView$57(final int i, final SecureDocument secureDocument, final SecureDocumentCell secureDocumentCell, final String str, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setMessage(LocaleController.getString(i == 1 ? R.string.PassportDeleteSelfie : R.string.PassportDeleteScan));
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setTitle(LocaleController.getString(R.string.AppName));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda64
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                PassportActivity.this.lambda$addDocumentView$56(secureDocument, i, secureDocumentCell, str, dialogInterface, i2);
            }
        });
        showDialog(builder.create());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addField$61(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, ArrayList arrayList, boolean z, DialogInterface dialogInterface, int i) {
        openTypeActivity(tLRPC$TL_secureRequiredType, (TLRPC$TL_secureRequiredType) arrayList.get(i), arrayList, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addField$62(String str, String str2) {
        needHideProgress();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addField$63(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, boolean z, DialogInterface dialogInterface, int i) {
        needShowProgress();
        deleteValueInternal(tLRPC$TL_secureRequiredType, null, null, true, new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda69
            @Override // java.lang.Runnable
            public final void run() {
                PassportActivity.this.needHideProgress();
            }
        }, new ErrorRunnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda70
            @Override // org.telegram.ui.PassportActivity.ErrorRunnable
            public final void onError(String str, String str2) {
                PassportActivity.this.lambda$addField$62(str, str2);
            }
        }, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00b7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$addField$64(final ArrayList arrayList, final TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, final boolean z, View view) {
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2;
        int i;
        int size;
        int i2;
        AlertDialog create;
        int i3;
        if (arrayList != null) {
            int size2 = arrayList.size();
            for (int i4 = 0; i4 < size2; i4++) {
                tLRPC$TL_secureRequiredType2 = (TLRPC$TL_secureRequiredType) arrayList.get(i4);
                if (getValueByType(tLRPC$TL_secureRequiredType2, false) != null || size2 == 1) {
                    break;
                }
            }
        }
        tLRPC$TL_secureRequiredType2 = null;
        TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
        if (!(tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) && !(tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress)) {
            boolean z2 = tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone;
            if ((z2 || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail)) && getValueByType(tLRPC$TL_secureRequiredType, false) != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setPositiveButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda67
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        PassportActivity.this.lambda$addField$63(tLRPC$TL_secureRequiredType, z, dialogInterface, i5);
                    }
                });
                builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                builder.setTitle(LocaleController.getString(R.string.AppName));
                builder.setMessage(LocaleController.getString(z2 ? R.string.PassportDeletePhoneAlert : R.string.PassportDeleteEmailAlert));
                create = builder.create();
                showDialog(create);
                return;
            }
            openTypeActivity(tLRPC$TL_secureRequiredType, tLRPC$TL_secureRequiredType2, arrayList, z);
        }
        if (tLRPC$TL_secureRequiredType2 == null && arrayList != null && !arrayList.isEmpty()) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
            builder2.setPositiveButton(LocaleController.getString(R.string.Cancel), null);
            TLRPC$SecureValueType tLRPC$SecureValueType2 = tLRPC$TL_secureRequiredType.type;
            if (!(tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypePersonalDetails)) {
                if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeAddress) {
                    i = R.string.PassportAddress;
                }
                ArrayList arrayList2 = new ArrayList();
                size = arrayList.size();
                for (i2 = 0; i2 < size; i2++) {
                    TLRPC$SecureValueType tLRPC$SecureValueType3 = ((TLRPC$TL_secureRequiredType) arrayList.get(i2)).type;
                    if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                        i3 = R.string.PassportAddLicence;
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypePassport) {
                        i3 = R.string.PassportAddPassport;
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                        i3 = R.string.PassportAddInternalPassport;
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                        i3 = R.string.PassportAddCard;
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                        i3 = R.string.PassportAddBill;
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeBankStatement) {
                        i3 = R.string.PassportAddBank;
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                        i3 = R.string.PassportAddAgreement;
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                        i3 = R.string.PassportAddTemporaryRegistration;
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                        i3 = R.string.PassportAddPassportRegistration;
                    }
                    arrayList2.add(LocaleController.getString(i3));
                }
                builder2.setItems((CharSequence[]) arrayList2.toArray(new CharSequence[0]), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda68
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        PassportActivity.this.lambda$addField$61(tLRPC$TL_secureRequiredType, arrayList, z, dialogInterface, i5);
                    }
                });
                create = builder2.create();
                showDialog(create);
                return;
            }
            i = R.string.PassportIdentityDocument;
            builder2.setTitle(LocaleController.getString(i));
            ArrayList arrayList22 = new ArrayList();
            size = arrayList.size();
            while (i2 < size) {
            }
            builder2.setItems((CharSequence[]) arrayList22.toArray(new CharSequence[0]), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda68
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i5) {
                    PassportActivity.this.lambda$addField$61(tLRPC$TL_secureRequiredType, arrayList, z, dialogInterface, i5);
                }
            });
            create = builder2.create();
            showDialog(create);
            return;
        }
        openTypeActivity(tLRPC$TL_secureRequiredType, tLRPC$TL_secureRequiredType2, arrayList, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$69(DialogInterface dialogInterface, int i) {
        finishFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkNativeFields$58() {
        EditTextBoldCursor[] editTextBoldCursorArr = this.inputExtraFields;
        if (editTextBoldCursorArr != null) {
            scrollToField(editTextBoldCursorArr[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createAddressInterface$32(View view) {
        this.uploadingFileType = 0;
        openAttachMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createAddressInterface$33(View view) {
        this.uploadingFileType = 4;
        openAttachMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createAddressInterface$34(CountrySelectActivity.Country country) {
        this.inputFields[5].setText(country.name);
        this.currentCitizeship = country.shortname;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createAddressInterface$35(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(false);
            countrySelectActivity.setCountrySelectActivityDelegate(new CountrySelectActivity.CountrySelectActivityDelegate() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda37
                @Override // org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate
                public final void didSelectCountry(CountrySelectActivity.Country country) {
                    PassportActivity.this.lambda$createAddressInterface$34(country);
                }
            });
            presentFragment(countrySelectActivity);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createAddressInterface$36(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5) {
            int intValue = ((Integer) textView.getTag()).intValue() + 1;
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            if (intValue < editTextBoldCursorArr.length) {
                if (editTextBoldCursorArr[intValue].isFocusable()) {
                    this.inputFields[intValue].requestFocus();
                } else {
                    this.inputFields[intValue].dispatchTouchEvent(MotionEvent.obtain(0L, 0L, 1, 0.0f, 0.0f, 0));
                    textView.clearFocus();
                    AndroidUtilities.hideKeyboard(textView);
                }
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createAddressInterface$37(View view) {
        createDocumentDeleteAlert();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createDocumentDeleteAlert$38(boolean[] zArr, DialogInterface dialogInterface, int i) {
        if (!this.documentOnly) {
            this.currentValues.clear();
        }
        this.currentDocumentValues.clear();
        this.delegate.deleteValue(this.currentType, this.currentDocumentsType, this.availableDocumentTypes, zArr[0], null, null);
        finishFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDocumentDeleteAlert$39(boolean[] zArr, View view) {
        if (view.isEnabled()) {
            boolean z = !zArr[0];
            zArr[0] = z;
            ((CheckBoxCell) view).setChecked(z, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createEmailInterface$24(View view) {
        this.useCurrentValue = true;
        this.doneItem.callOnClick();
        this.useCurrentValue = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createEmailInterface$25(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6 || i == 5) {
            this.doneItem.callOnClick();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createEmailVerificationInterface$5(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6 || i == 5) {
            this.doneItem.callOnClick();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createIdentityInterface$40(View view) {
        this.uploadingFileType = 2;
        openAttachMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createIdentityInterface$41(View view) {
        this.uploadingFileType = 3;
        openAttachMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createIdentityInterface$42(View view) {
        this.uploadingFileType = 1;
        openAttachMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createIdentityInterface$43(View view) {
        this.uploadingFileType = 4;
        openAttachMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createIdentityInterface$44(View view) {
        int checkSelfPermission;
        if (Build.VERSION.SDK_INT >= 23) {
            checkSelfPermission = getParentActivity().checkSelfPermission("android.permission.CAMERA");
            if (checkSelfPermission != 0) {
                getParentActivity().requestPermissions(new String[]{"android.permission.CAMERA"}, 22);
                return;
            }
        }
        CameraScanActivity cameraScanActivity = new CameraScanActivity(0);
        cameraScanActivity.setDelegate(new CameraScanActivity.CameraScanActivityDelegate() { // from class: org.telegram.ui.PassportActivity.14
            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public void didFindMrzInfo(MrzRecognizer.Result result) {
                EditTextBoldCursor editTextBoldCursor;
                int i;
                if (!TextUtils.isEmpty(result.firstName)) {
                    PassportActivity.this.inputFields[0].setText(result.firstName);
                }
                if (!TextUtils.isEmpty(result.middleName)) {
                    PassportActivity.this.inputFields[1].setText(result.middleName);
                }
                if (!TextUtils.isEmpty(result.lastName)) {
                    PassportActivity.this.inputFields[2].setText(result.lastName);
                }
                int i2 = result.gender;
                if (i2 != 0) {
                    if (i2 == 1) {
                        PassportActivity.this.currentGender = "male";
                        editTextBoldCursor = PassportActivity.this.inputFields[4];
                        i = R.string.PassportMale;
                    } else if (i2 == 2) {
                        PassportActivity.this.currentGender = "female";
                        editTextBoldCursor = PassportActivity.this.inputFields[4];
                        i = R.string.PassportFemale;
                    }
                    editTextBoldCursor.setText(LocaleController.getString(i));
                }
                if (!TextUtils.isEmpty(result.nationality)) {
                    PassportActivity.this.currentCitizeship = result.nationality;
                    String str = (String) PassportActivity.this.languageMap.get(PassportActivity.this.currentCitizeship);
                    if (str != null) {
                        PassportActivity.this.inputFields[5].setText(str);
                    }
                }
                if (!TextUtils.isEmpty(result.issuingCountry)) {
                    PassportActivity.this.currentResidence = result.issuingCountry;
                    String str2 = (String) PassportActivity.this.languageMap.get(PassportActivity.this.currentResidence);
                    if (str2 != null) {
                        PassportActivity.this.inputFields[6].setText(str2);
                    }
                }
                if (result.birthDay <= 0 || result.birthMonth <= 0 || result.birthYear <= 0) {
                    return;
                }
                PassportActivity.this.inputFields[3].setText(String.format(Locale.US, "%02d.%02d.%d", Integer.valueOf(result.birthDay), Integer.valueOf(result.birthMonth), Integer.valueOf(result.birthYear)));
            }

            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public /* synthetic */ void didFindQr(String str) {
                CameraScanActivity.CameraScanActivityDelegate.-CC.$default$didFindQr(this, str);
            }

            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public /* synthetic */ String getSubtitleText() {
                return CameraScanActivity.CameraScanActivityDelegate.-CC.$default$getSubtitleText(this);
            }

            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public /* synthetic */ void onDismiss() {
                CameraScanActivity.CameraScanActivityDelegate.-CC.$default$onDismiss(this);
            }

            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public /* synthetic */ boolean processQr(String str, Runnable runnable) {
                return CameraScanActivity.CameraScanActivityDelegate.-CC.$default$processQr(this, str, runnable);
            }
        });
        presentFragment(cameraScanActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createIdentityInterface$45(View view, CountrySelectActivity.Country country) {
        int intValue = ((Integer) view.getTag()).intValue();
        EditTextBoldCursor editTextBoldCursor = this.inputFields[intValue];
        if (intValue == 5) {
            this.currentCitizeship = country.shortname;
        } else {
            this.currentResidence = country.shortname;
        }
        editTextBoldCursor.setText(country.name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createIdentityInterface$46(final View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(false);
            countrySelectActivity.setCountrySelectActivityDelegate(new CountrySelectActivity.CountrySelectActivityDelegate() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda55
                @Override // org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate
                public final void didSelectCountry(CountrySelectActivity.Country country) {
                    PassportActivity.this.lambda$createIdentityInterface$45(view, country);
                }
            });
            presentFragment(countrySelectActivity);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createIdentityInterface$47(int i, EditTextBoldCursor editTextBoldCursor, int i2, int i3, int i4) {
        if (i == 8) {
            int[] iArr = this.currentExpireDate;
            iArr[0] = i2;
            iArr[1] = i3 + 1;
            iArr[2] = i4;
        }
        editTextBoldCursor.setText(String.format(Locale.US, "%02d.%02d.%d", Integer.valueOf(i4), Integer.valueOf(i3 + 1), Integer.valueOf(i2)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createIdentityInterface$48(EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface, int i) {
        int[] iArr = this.currentExpireDate;
        iArr[2] = 0;
        iArr[1] = 0;
        iArr[0] = 0;
        editTextBoldCursor.setText(LocaleController.getString(R.string.PassportNoExpireDate));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createIdentityInterface$49(Context context, View view, MotionEvent motionEvent) {
        String string;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            Calendar calendar = Calendar.getInstance();
            calendar.get(1);
            calendar.get(2);
            calendar.get(5);
            try {
                final EditTextBoldCursor editTextBoldCursor = (EditTextBoldCursor) view;
                final int intValue = ((Integer) editTextBoldCursor.getTag()).intValue();
                if (intValue == 8) {
                    string = LocaleController.getString(R.string.PassportSelectExpiredDate);
                    i = 0;
                    i2 = 20;
                    i3 = 0;
                } else {
                    string = LocaleController.getString(R.string.PassportSelectBithdayDate);
                    i = -120;
                    i2 = 0;
                    i3 = -18;
                }
                String[] split = editTextBoldCursor.getText().toString().split("\\.");
                if (split.length == 3) {
                    int intValue2 = Utilities.parseInt((CharSequence) split[0]).intValue();
                    int intValue3 = Utilities.parseInt((CharSequence) split[1]).intValue();
                    i6 = Utilities.parseInt((CharSequence) split[2]).intValue();
                    i4 = intValue2;
                    i5 = intValue3;
                } else {
                    i4 = -1;
                    i5 = -1;
                    i6 = -1;
                }
                AlertDialog.Builder createDatePickerDialog = AlertsCreator.createDatePickerDialog(context, i, i2, i3, i4, i5, i6, string, intValue == 8, new AlertsCreator.DatePickerDelegate() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda52
                    @Override // org.telegram.ui.Components.AlertsCreator.DatePickerDelegate
                    public final void didSelectDate(int i7, int i8, int i9) {
                        PassportActivity.this.lambda$createIdentityInterface$47(intValue, editTextBoldCursor, i7, i8, i9);
                    }
                });
                if (intValue == 8) {
                    createDatePickerDialog.setNegativeButton(LocaleController.getString(R.string.PassportSelectNotExpire), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda53
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i7) {
                            PassportActivity.this.lambda$createIdentityInterface$48(editTextBoldCursor, dialogInterface, i7);
                        }
                    });
                }
                showDialog(createDatePickerDialog.create());
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createIdentityInterface$50(DialogInterface dialogInterface, int i) {
        EditTextBoldCursor editTextBoldCursor;
        int i2;
        if (i == 0) {
            this.currentGender = "male";
            editTextBoldCursor = this.inputFields[4];
            i2 = R.string.PassportMale;
        } else if (i != 1) {
            return;
        } else {
            this.currentGender = "female";
            editTextBoldCursor = this.inputFields[4];
            i2 = R.string.PassportFemale;
        }
        editTextBoldCursor.setText(LocaleController.getString(i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createIdentityInterface$51(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString(R.string.PassportSelectGender));
            builder.setItems(new CharSequence[]{LocaleController.getString(R.string.PassportMale), LocaleController.getString(R.string.PassportFemale)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda45
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    PassportActivity.this.lambda$createIdentityInterface$50(dialogInterface, i);
                }
            });
            builder.setPositiveButton(LocaleController.getString(R.string.Cancel), null);
            showDialog(builder.create());
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createIdentityInterface$52(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5) {
            int intValue = ((Integer) textView.getTag()).intValue() + 1;
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            if (intValue < editTextBoldCursorArr.length) {
                if (editTextBoldCursorArr[intValue].isFocusable()) {
                    this.inputFields[intValue].requestFocus();
                } else {
                    this.inputFields[intValue].dispatchTouchEvent(MotionEvent.obtain(0L, 0L, 1, 0.0f, 0.0f, 0));
                    textView.clearFocus();
                    AndroidUtilities.hideKeyboard(textView);
                }
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createIdentityInterface$53(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5) {
            int intValue = ((Integer) textView.getTag()).intValue() + 1;
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputExtraFields;
            if (intValue < editTextBoldCursorArr.length) {
                if (editTextBoldCursorArr[intValue].isFocusable()) {
                    this.inputExtraFields[intValue].requestFocus();
                } else {
                    this.inputExtraFields[intValue].dispatchTouchEvent(MotionEvent.obtain(0L, 0L, 1, 0.0f, 0.0f, 0));
                    textView.clearFocus();
                    AndroidUtilities.hideKeyboard(textView);
                }
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createIdentityInterface$54(View view) {
        createDocumentDeleteAlert();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createManageInterface$17(View view) {
        openAddDocumentAlert();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createManageInterface$18() {
        int i = 0;
        while (i < this.linearLayout2.getChildCount()) {
            View childAt = this.linearLayout2.getChildAt(i);
            if (childAt instanceof TextDetailSecureCell) {
                this.linearLayout2.removeView(childAt);
                i--;
            }
            i++;
        }
        needHideProgress();
        this.typesViews.clear();
        this.typesValues.clear();
        this.currentForm.values.clear();
        updateManageVisibility();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createManageInterface$19(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda72
            @Override // java.lang.Runnable
            public final void run() {
                PassportActivity.this.lambda$createManageInterface$18();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createManageInterface$20(DialogInterface dialogInterface, int i) {
        TLRPC$TL_account_deleteSecureValue tLRPC$TL_account_deleteSecureValue = new TLRPC$TL_account_deleteSecureValue();
        for (int i2 = 0; i2 < this.currentForm.values.size(); i2++) {
            tLRPC$TL_account_deleteSecureValue.types.add(((TLRPC$TL_secureValue) this.currentForm.values.get(i2)).type);
        }
        needShowProgress();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_deleteSecureValue, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda63
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PassportActivity.this.lambda$createManageInterface$19(tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createManageInterface$21(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.TelegramPassportDeleteTitle));
        builder.setMessage(LocaleController.getString(R.string.TelegramPassportDeleteAlert));
        builder.setPositiveButton(LocaleController.getString(R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda51
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PassportActivity.this.lambda$createManageInterface$20(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        AlertDialog create = builder.create();
        showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createManageInterface$22(View view) {
        openAddDocumentAlert();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPasswordInterface$10(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda62
            @Override // java.lang.Runnable
            public final void run() {
                PassportActivity.this.lambda$createPasswordInterface$9(tLRPC$TL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPasswordInterface$11(DialogInterface dialogInterface, int i) {
        Activity parentActivity = getParentActivity();
        Browser.openUrl(parentActivity, "https://telegram.org/deactivate?phone=" + UserConfig.getInstance(this.currentAccount).getClientPhone());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPasswordInterface$12(View view) {
        if (this.currentPassword.has_recovery) {
            needShowProgress();
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_auth_requestPasswordRecovery(), new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda41
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    PassportActivity.this.lambda$createPasswordInterface$10(tLObject, tLRPC$TL_error);
                }
            }, 10), this.classGuid);
        } else if (getParentActivity() == null) {
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
            builder.setNegativeButton(LocaleController.getString(R.string.RestorePasswordResetAccount), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda42
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    PassportActivity.this.lambda$createPasswordInterface$11(dialogInterface, i);
                }
            });
            builder.setTitle(LocaleController.getString(R.string.RestorePasswordNoEmailTitle));
            builder.setMessage(LocaleController.getString(R.string.RestorePasswordNoEmailText));
            showDialog(builder.create());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPasswordInterface$6(View view) {
        TwoStepVerificationSetupActivity twoStepVerificationSetupActivity = new TwoStepVerificationSetupActivity(this.currentAccount, 0, this.currentPassword);
        twoStepVerificationSetupActivity.setCloseAfterSet(true);
        presentFragment(twoStepVerificationSetupActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createPasswordInterface$7(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5 || i == 6) {
            this.doneItem.callOnClick();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPasswordInterface$8(TLRPC$TL_auth_passwordRecovery tLRPC$TL_auth_passwordRecovery, DialogInterface dialogInterface, int i) {
        TLRPC$account_Password tLRPC$account_Password = this.currentPassword;
        tLRPC$account_Password.email_unconfirmed_pattern = tLRPC$TL_auth_passwordRecovery.email_pattern;
        presentFragment(new TwoStepVerificationSetupActivity(this.currentAccount, 4, tLRPC$account_Password));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPasswordInterface$9(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        String string;
        String str;
        needHideProgress();
        if (tLRPC$TL_error != null) {
            if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                String formatPluralString = intValue < 60 ? LocaleController.formatPluralString("Seconds", intValue, new Object[0]) : LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                string = LocaleController.getString(R.string.AppName);
                str = LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, formatPluralString);
            } else {
                string = LocaleController.getString(R.string.AppName);
                str = tLRPC$TL_error.text;
            }
            showAlertWithText(string, str);
            return;
        }
        final TLRPC$TL_auth_passwordRecovery tLRPC$TL_auth_passwordRecovery = (TLRPC$TL_auth_passwordRecovery) tLObject;
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setMessage(LocaleController.formatString("RestoreEmailSent", R.string.RestoreEmailSent, tLRPC$TL_auth_passwordRecovery.email_pattern));
        builder.setTitle(LocaleController.getString(R.string.RestoreEmailSentTitle));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda73
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PassportActivity.this.lambda$createPasswordInterface$8(tLRPC$TL_auth_passwordRecovery, dialogInterface, i);
            }
        });
        Dialog showDialog = showDialog(builder.create());
        if (showDialog != null) {
            showDialog.setCanceledOnTouchOutside(false);
            showDialog.setCancelable(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPhoneInterface$26(View view) {
        this.useCurrentValue = true;
        this.doneItem.callOnClick();
        this.useCurrentValue = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPhoneInterface$27() {
        AndroidUtilities.showKeyboard(this.inputFields[2]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPhoneInterface$28(CountrySelectActivity.Country country) {
        this.inputFields[0].setText(country.name);
        if (this.countriesArray.indexOf(country.name) != -1) {
            this.ignoreOnTextChange = true;
            String str = (String) this.countriesMap.get(country.name);
            this.inputFields[1].setText(str);
            String str2 = (String) this.phoneFormatMap.get(str);
            this.inputFields[2].setHintText(str2 != null ? str2.replace('X', (char) 8211) : null);
            this.ignoreOnTextChange = false;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda60
            @Override // java.lang.Runnable
            public final void run() {
                PassportActivity.this.lambda$createPhoneInterface$27();
            }
        }, 300L);
        this.inputFields[2].requestFocus();
        EditTextBoldCursor editTextBoldCursor = this.inputFields[2];
        editTextBoldCursor.setSelection(editTextBoldCursor.length());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createPhoneInterface$29(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(false);
            countrySelectActivity.setCountrySelectActivityDelegate(new CountrySelectActivity.CountrySelectActivityDelegate() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda38
                @Override // org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate
                public final void didSelectCountry(CountrySelectActivity.Country country) {
                    PassportActivity.this.lambda$createPhoneInterface$28(country);
                }
            });
            presentFragment(countrySelectActivity);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createPhoneInterface$30(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5) {
            this.inputFields[2].requestFocus();
            return true;
        } else if (i == 6) {
            this.doneItem.callOnClick();
            return true;
        } else {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createPhoneInterface$31(View view, int i, KeyEvent keyEvent) {
        if (i == 67 && this.inputFields[2].length() == 0) {
            this.inputFields[1].requestFocus();
            EditTextBoldCursor editTextBoldCursor = this.inputFields[1];
            editTextBoldCursor.setSelection(editTextBoldCursor.length());
            this.inputFields[1].dispatchKeyEvent(keyEvent);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createRequestInterface$14(TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            this.ignoreOnFailure = true;
            callCallback(true);
            finishFragment();
            return;
        }
        showEditDoneProgress(false, false);
        if ("APP_VERSION_OUTDATED".equals(tLRPC$TL_error.text)) {
            AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString(R.string.UpdateAppAlert), true);
        } else {
            showAlertWithText(LocaleController.getString(R.string.AppName), tLRPC$TL_error.text);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createRequestInterface$15(TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda66
            @Override // java.lang.Runnable
            public final void run() {
                PassportActivity.this.lambda$createRequestInterface$14(tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createRequestInterface$16(View view) {
        ArrayList arrayList;
        int i;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType;
        ArrayList arrayList2 = new ArrayList();
        int size = this.currentForm.required_types.size();
        for (int i2 = 0; i2 < size; i2++) {
            TLRPC$SecureRequiredType tLRPC$SecureRequiredType = (TLRPC$SecureRequiredType) this.currentForm.required_types.get(i2);
            if (tLRPC$SecureRequiredType instanceof TLRPC$TL_secureRequiredType) {
                tLRPC$TL_secureRequiredType = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType;
            } else {
                if (tLRPC$SecureRequiredType instanceof TLRPC$TL_secureRequiredTypeOneOf) {
                    TLRPC$TL_secureRequiredTypeOneOf tLRPC$TL_secureRequiredTypeOneOf = (TLRPC$TL_secureRequiredTypeOneOf) tLRPC$SecureRequiredType;
                    if (!tLRPC$TL_secureRequiredTypeOneOf.types.isEmpty()) {
                        TLRPC$SecureRequiredType tLRPC$SecureRequiredType2 = (TLRPC$SecureRequiredType) tLRPC$TL_secureRequiredTypeOneOf.types.get(0);
                        if (tLRPC$SecureRequiredType2 instanceof TLRPC$TL_secureRequiredType) {
                            TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2 = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType2;
                            int size2 = tLRPC$TL_secureRequiredTypeOneOf.types.size();
                            int i3 = 0;
                            while (true) {
                                if (i3 >= size2) {
                                    tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType2;
                                    break;
                                }
                                TLRPC$SecureRequiredType tLRPC$SecureRequiredType3 = (TLRPC$SecureRequiredType) tLRPC$TL_secureRequiredTypeOneOf.types.get(i3);
                                if (tLRPC$SecureRequiredType3 instanceof TLRPC$TL_secureRequiredType) {
                                    TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3 = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType3;
                                    if (getValueByType(tLRPC$TL_secureRequiredType3, true) != null) {
                                        tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType3;
                                        break;
                                    }
                                }
                                i3++;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
            TLRPC$TL_secureValue valueByType = getValueByType(tLRPC$TL_secureRequiredType, true);
            if (valueByType == null) {
                Vibrator vibrator = (Vibrator) getParentActivity().getSystemService("vibrator");
                if (vibrator != null) {
                    vibrator.vibrate(200L);
                }
                AndroidUtilities.shakeView(getViewByType(tLRPC$TL_secureRequiredType));
                return;
            }
            HashMap hashMap = (HashMap) this.errorsMap.get(getNameForType(tLRPC$TL_secureRequiredType.type));
            if (hashMap != null && !hashMap.isEmpty()) {
                Vibrator vibrator2 = (Vibrator) getParentActivity().getSystemService("vibrator");
                if (vibrator2 != null) {
                    vibrator2.vibrate(200L);
                }
                AndroidUtilities.shakeView(getViewByType(tLRPC$TL_secureRequiredType));
                return;
            }
            arrayList2.add(new 1ValueToSend(valueByType, tLRPC$TL_secureRequiredType.selfie_required, tLRPC$TL_secureRequiredType.translation_required));
        }
        showEditDoneProgress(false, true);
        TLRPC$TL_account_acceptAuthorization tLRPC$TL_account_acceptAuthorization = new TLRPC$TL_account_acceptAuthorization();
        tLRPC$TL_account_acceptAuthorization.bot_id = this.currentBotId;
        tLRPC$TL_account_acceptAuthorization.scope = this.currentScope;
        tLRPC$TL_account_acceptAuthorization.public_key = this.currentPublicKey;
        JSONObject jSONObject = new JSONObject();
        int size3 = arrayList2.size();
        int i4 = 0;
        while (i4 < size3) {
            1ValueToSend r9 = (1ValueToSend) arrayList2.get(i4);
            TLRPC$TL_secureValue tLRPC$TL_secureValue = r9.value;
            JSONObject jSONObject2 = new JSONObject();
            TLRPC$SecurePlainData tLRPC$SecurePlainData = tLRPC$TL_secureValue.plain_data;
            if (tLRPC$SecurePlainData == null) {
                try {
                    JSONObject jSONObject3 = new JSONObject();
                    TLRPC$TL_secureData tLRPC$TL_secureData = tLRPC$TL_secureValue.data;
                    if (tLRPC$TL_secureData != null) {
                        byte[] decryptValueSecret = decryptValueSecret(tLRPC$TL_secureData.secret, tLRPC$TL_secureData.data_hash);
                        jSONObject2.put("data_hash", Base64.encodeToString(tLRPC$TL_secureValue.data.data_hash, 2));
                        jSONObject2.put("secret", Base64.encodeToString(decryptValueSecret, 2));
                        jSONObject3.put("data", jSONObject2);
                    }
                    if (tLRPC$TL_secureValue.files.isEmpty()) {
                        arrayList = arrayList2;
                        i = size3;
                    } else {
                        JSONArray jSONArray = new JSONArray();
                        int i5 = 0;
                        for (int size4 = tLRPC$TL_secureValue.files.size(); i5 < size4; size4 = size4) {
                            TLRPC$TL_secureFile tLRPC$TL_secureFile = (TLRPC$TL_secureFile) tLRPC$TL_secureValue.files.get(i5);
                            arrayList = arrayList2;
                            try {
                                i = size3;
                                try {
                                    byte[] decryptValueSecret2 = decryptValueSecret(tLRPC$TL_secureFile.secret, tLRPC$TL_secureFile.file_hash);
                                    JSONObject jSONObject4 = new JSONObject();
                                    jSONObject4.put("file_hash", Base64.encodeToString(tLRPC$TL_secureFile.file_hash, 2));
                                    jSONObject4.put("secret", Base64.encodeToString(decryptValueSecret2, 2));
                                    jSONArray.put(jSONObject4);
                                    i5++;
                                    arrayList2 = arrayList;
                                    size3 = i;
                                } catch (Exception unused) {
                                }
                            } catch (Exception unused2) {
                            }
                        }
                        arrayList = arrayList2;
                        i = size3;
                        jSONObject3.put("files", jSONArray);
                    }
                    TLRPC$SecureFile tLRPC$SecureFile = tLRPC$TL_secureValue.front_side;
                    if (tLRPC$SecureFile instanceof TLRPC$TL_secureFile) {
                        TLRPC$TL_secureFile tLRPC$TL_secureFile2 = (TLRPC$TL_secureFile) tLRPC$SecureFile;
                        byte[] decryptValueSecret3 = decryptValueSecret(tLRPC$TL_secureFile2.secret, tLRPC$TL_secureFile2.file_hash);
                        JSONObject jSONObject5 = new JSONObject();
                        jSONObject5.put("file_hash", Base64.encodeToString(tLRPC$TL_secureFile2.file_hash, 2));
                        jSONObject5.put("secret", Base64.encodeToString(decryptValueSecret3, 2));
                        jSONObject3.put("front_side", jSONObject5);
                    }
                    TLRPC$SecureFile tLRPC$SecureFile2 = tLRPC$TL_secureValue.reverse_side;
                    if (tLRPC$SecureFile2 instanceof TLRPC$TL_secureFile) {
                        TLRPC$TL_secureFile tLRPC$TL_secureFile3 = (TLRPC$TL_secureFile) tLRPC$SecureFile2;
                        byte[] decryptValueSecret4 = decryptValueSecret(tLRPC$TL_secureFile3.secret, tLRPC$TL_secureFile3.file_hash);
                        JSONObject jSONObject6 = new JSONObject();
                        jSONObject6.put("file_hash", Base64.encodeToString(tLRPC$TL_secureFile3.file_hash, 2));
                        jSONObject6.put("secret", Base64.encodeToString(decryptValueSecret4, 2));
                        jSONObject3.put("reverse_side", jSONObject6);
                    }
                    if (r9.selfie_required) {
                        TLRPC$SecureFile tLRPC$SecureFile3 = tLRPC$TL_secureValue.selfie;
                        if (tLRPC$SecureFile3 instanceof TLRPC$TL_secureFile) {
                            TLRPC$TL_secureFile tLRPC$TL_secureFile4 = (TLRPC$TL_secureFile) tLRPC$SecureFile3;
                            byte[] decryptValueSecret5 = decryptValueSecret(tLRPC$TL_secureFile4.secret, tLRPC$TL_secureFile4.file_hash);
                            JSONObject jSONObject7 = new JSONObject();
                            jSONObject7.put("file_hash", Base64.encodeToString(tLRPC$TL_secureFile4.file_hash, 2));
                            jSONObject7.put("secret", Base64.encodeToString(decryptValueSecret5, 2));
                            jSONObject3.put("selfie", jSONObject7);
                        }
                    }
                    if (r9.translation_required && !tLRPC$TL_secureValue.translation.isEmpty()) {
                        JSONArray jSONArray2 = new JSONArray();
                        int size5 = tLRPC$TL_secureValue.translation.size();
                        for (int i6 = 0; i6 < size5; i6++) {
                            TLRPC$TL_secureFile tLRPC$TL_secureFile5 = (TLRPC$TL_secureFile) tLRPC$TL_secureValue.translation.get(i6);
                            byte[] decryptValueSecret6 = decryptValueSecret(tLRPC$TL_secureFile5.secret, tLRPC$TL_secureFile5.file_hash);
                            JSONObject jSONObject8 = new JSONObject();
                            jSONObject8.put("file_hash", Base64.encodeToString(tLRPC$TL_secureFile5.file_hash, 2));
                            jSONObject8.put("secret", Base64.encodeToString(decryptValueSecret6, 2));
                            jSONArray2.put(jSONObject8);
                        }
                        jSONObject3.put("translation", jSONArray2);
                    }
                    jSONObject.put(getNameForType(tLRPC$TL_secureValue.type), jSONObject3);
                } catch (Exception unused3) {
                }
                TLRPC$TL_secureValueHash tLRPC$TL_secureValueHash = new TLRPC$TL_secureValueHash();
                tLRPC$TL_secureValueHash.type = tLRPC$TL_secureValue.type;
                tLRPC$TL_secureValueHash.hash = tLRPC$TL_secureValue.hash;
                tLRPC$TL_account_acceptAuthorization.value_hashes.add(tLRPC$TL_secureValueHash);
                i4++;
                arrayList2 = arrayList;
                size3 = i;
            } else if (tLRPC$SecurePlainData instanceof TLRPC$TL_securePlainEmail) {
                TLRPC$TL_securePlainEmail tLRPC$TL_securePlainEmail = (TLRPC$TL_securePlainEmail) tLRPC$SecurePlainData;
            } else if (tLRPC$SecurePlainData instanceof TLRPC$TL_securePlainPhone) {
                TLRPC$TL_securePlainPhone tLRPC$TL_securePlainPhone = (TLRPC$TL_securePlainPhone) tLRPC$SecurePlainData;
            }
            arrayList = arrayList2;
            i = size3;
            TLRPC$TL_secureValueHash tLRPC$TL_secureValueHash2 = new TLRPC$TL_secureValueHash();
            tLRPC$TL_secureValueHash2.type = tLRPC$TL_secureValue.type;
            tLRPC$TL_secureValueHash2.hash = tLRPC$TL_secureValue.hash;
            tLRPC$TL_account_acceptAuthorization.value_hashes.add(tLRPC$TL_secureValueHash2);
            i4++;
            arrayList2 = arrayList;
            size3 = i;
        }
        JSONObject jSONObject9 = new JSONObject();
        try {
            jSONObject9.put("secure_data", jSONObject);
        } catch (Exception unused4) {
        }
        Object obj = this.currentPayload;
        if (obj != null) {
            try {
                jSONObject9.put("payload", obj);
            } catch (Exception unused5) {
            }
        }
        Object obj2 = this.currentNonce;
        if (obj2 != null) {
            try {
                jSONObject9.put("nonce", obj2);
            } catch (Exception unused6) {
            }
        }
        EncryptionResult encryptData = encryptData(AndroidUtilities.getStringBytes(jSONObject9.toString()));
        TLRPC$TL_secureCredentialsEncrypted tLRPC$TL_secureCredentialsEncrypted = new TLRPC$TL_secureCredentialsEncrypted();
        tLRPC$TL_account_acceptAuthorization.credentials = tLRPC$TL_secureCredentialsEncrypted;
        tLRPC$TL_secureCredentialsEncrypted.hash = encryptData.fileHash;
        tLRPC$TL_secureCredentialsEncrypted.data = encryptData.encryptedData;
        try {
            String replace = this.currentPublicKey.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
            Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding");
            cipher.init(1, (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(replace, 0))));
            tLRPC$TL_account_acceptAuthorization.credentials.secret = cipher.doFinal(encryptData.decrypyedFileSecret);
        } catch (Exception e) {
            FileLog.e(e);
        }
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_acceptAuthorization, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda48
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PassportActivity.this.lambda$createRequestInterface$15(tLObject, tLRPC$TL_error);
            }
        }), this.classGuid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00f0  */
    /* JADX WARN: Removed duplicated region for block: B:61:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$deleteValueInternal$59(TLRPC$TL_error tLRPC$TL_error, ErrorRunnable errorRunnable, boolean z, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, boolean z2, ArrayList arrayList, Runnable runnable) {
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3;
        String str;
        int size;
        String str2;
        PassportActivity passportActivity;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType4;
        String str3;
        TLRPC$TL_secureData tLRPC$TL_secureData;
        String str4;
        LinearLayout linearLayout;
        String str5 = null;
        if (tLRPC$TL_error != null) {
            if (errorRunnable != null) {
                errorRunnable.onError(tLRPC$TL_error.text, null);
            }
            showAlertWithText(LocaleController.getString(R.string.AppName), tLRPC$TL_error.text);
            return;
        }
        if (z) {
            if (tLRPC$TL_secureRequiredType == null) {
                removeValue(tLRPC$TL_secureRequiredType2);
                if (this.currentActivityType != 8) {
                    TextDetailSecureCell textDetailSecureCell = (TextDetailSecureCell) this.typesViews.remove(tLRPC$TL_secureRequiredType2);
                    if (textDetailSecureCell != null) {
                        this.linearLayout2.removeView(textDetailSecureCell);
                        View childAt = this.linearLayout2.getChildAt(linearLayout.getChildCount() - 6);
                        if (childAt instanceof TextDetailSecureCell) {
                            ((TextDetailSecureCell) childAt).setNeedDivider(false);
                        }
                    }
                    updateManageVisibility();
                } else {
                    if (tLRPC$TL_secureRequiredType == null || arrayList == null || arrayList.size() <= 1) {
                        tLRPC$TL_secureRequiredType3 = tLRPC$TL_secureRequiredType;
                        str = null;
                    } else {
                        int size2 = arrayList.size();
                        int i = 0;
                        while (true) {
                            if (i >= size2) {
                                tLRPC$TL_secureRequiredType3 = tLRPC$TL_secureRequiredType;
                                break;
                            }
                            tLRPC$TL_secureRequiredType3 = (TLRPC$TL_secureRequiredType) arrayList.get(i);
                            TLRPC$TL_secureValue valueByType = getValueByType(tLRPC$TL_secureRequiredType3, false);
                            if (valueByType != null) {
                                TLRPC$TL_secureData tLRPC$TL_secureData2 = valueByType.data;
                                if (tLRPC$TL_secureData2 != null) {
                                    str4 = decryptData(tLRPC$TL_secureData2.data, decryptValueSecret(tLRPC$TL_secureData2.secret, tLRPC$TL_secureData2.data_hash), valueByType.data.data_hash);
                                }
                            } else {
                                i++;
                            }
                        }
                        str4 = null;
                        if (tLRPC$TL_secureRequiredType3 == null) {
                            str = str4;
                            tLRPC$TL_secureRequiredType3 = (TLRPC$TL_secureRequiredType) arrayList.get(0);
                        } else {
                            str = str4;
                        }
                    }
                    if (z2) {
                        size = arrayList != null ? arrayList.size() : 0;
                        str2 = null;
                        passportActivity = this;
                        tLRPC$TL_secureRequiredType4 = tLRPC$TL_secureRequiredType2;
                        str3 = null;
                    } else {
                        TLRPC$TL_secureValue valueByType2 = getValueByType(tLRPC$TL_secureRequiredType2, false);
                        if (valueByType2 != null && (tLRPC$TL_secureData = valueByType2.data) != null) {
                            str5 = decryptData(tLRPC$TL_secureData.data, decryptValueSecret(tLRPC$TL_secureData.secret, tLRPC$TL_secureData.data_hash), valueByType2.data.data_hash);
                        }
                        size = arrayList != null ? arrayList.size() : 0;
                        str2 = null;
                        passportActivity = this;
                        tLRPC$TL_secureRequiredType4 = tLRPC$TL_secureRequiredType2;
                        str3 = str5;
                    }
                    passportActivity.setTypeValue(tLRPC$TL_secureRequiredType4, str2, str3, tLRPC$TL_secureRequiredType3, str, z, size);
                }
                if (runnable == null) {
                    runnable.run();
                    return;
                }
                return;
            }
        } else if (z2) {
            removeValue(tLRPC$TL_secureRequiredType2);
        }
        removeValue(tLRPC$TL_secureRequiredType);
        if (this.currentActivityType != 8) {
        }
        if (runnable == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteValueInternal$60(final ErrorRunnable errorRunnable, final boolean z, final TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, final TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, final boolean z2, final ArrayList arrayList, final Runnable runnable, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda71
            @Override // java.lang.Runnable
            public final void run() {
                PassportActivity.this.lambda$deleteValueInternal$59(tLRPC$TL_error, errorRunnable, z, tLRPC$TL_secureRequiredType, tLRPC$TL_secureRequiredType2, z2, arrayList, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPasswordInfo$3(TLObject tLObject) {
        if (tLObject != null) {
            TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
            this.currentPassword = tLRPC$account_Password;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(tLRPC$account_Password, false)) {
                AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString(R.string.UpdateAppAlert), true);
                return;
            }
            TwoStepVerificationActivity.initPasswordNewAlgo(this.currentPassword);
            updatePasswordInterface();
            if (this.inputFieldContainers[0].getVisibility() == 0) {
                this.inputFields[0].requestFocus();
                AndroidUtilities.showKeyboard(this.inputFields[0]);
            }
            if (this.usingSavedPassword == 1) {
                onPasswordDone(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPasswordInfo$4(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda59
            @Override // java.lang.Runnable
            public final void run() {
                PassportActivity.this.lambda$loadPasswordInfo$3(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(TLObject tLObject) {
        if (!(tLObject instanceof TLRPC$TL_help_passportConfig)) {
            SharedConfig.getCountryLangs();
            return;
        }
        TLRPC$TL_help_passportConfig tLRPC$TL_help_passportConfig = (TLRPC$TL_help_passportConfig) tLObject;
        SharedConfig.setPassportConfig(tLRPC$TL_help_passportConfig.countries_langs.data, tLRPC$TL_help_passportConfig.hash);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$1(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                PassportActivity.lambda$new$0(TLObject.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x006a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$onPasswordDone$13(boolean z, String str) {
        byte[] bArr;
        byte[] x;
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo;
        TLRPC$TL_account_getPasswordSettings tLRPC$TL_account_getPasswordSettings = new TLRPC$TL_account_getPasswordSettings();
        if (z) {
            x = this.savedPasswordHash;
        } else if (!(this.currentPassword.current_algo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)) {
            bArr = null;
            8 r9 = new 8(z, bArr, tLRPC$TL_account_getPasswordSettings, str);
            TLRPC$account_Password tLRPC$account_Password = this.currentPassword;
            tLRPC$PasswordKdfAlgo = tLRPC$account_Password.current_algo;
            if (tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
                TLRPC$TL_error tLRPC$TL_error = new TLRPC$TL_error();
                tLRPC$TL_error.text = "PASSWORD_HASH_INVALID";
                r9.run(null, tLRPC$TL_error);
                return;
            }
            TLRPC$TL_inputCheckPasswordSRP startCheck = SRPHelper.startCheck(bArr, tLRPC$account_Password.srp_id, tLRPC$account_Password.srp_B, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo);
            tLRPC$TL_account_getPasswordSettings.password = startCheck;
            if (startCheck != null) {
                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getPasswordSettings, r9, 10), this.classGuid);
                return;
            }
            TLRPC$TL_error tLRPC$TL_error2 = new TLRPC$TL_error();
            tLRPC$TL_error2.text = "ALGO_INVALID";
            r9.run(null, tLRPC$TL_error2);
            return;
        } else {
            x = SRPHelper.getX(AndroidUtilities.getStringBytes(str), (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) this.currentPassword.current_algo);
        }
        bArr = x;
        8 r92 = new 8(z, bArr, tLRPC$TL_account_getPasswordSettings, str);
        TLRPC$account_Password tLRPC$account_Password2 = this.currentPassword;
        tLRPC$PasswordKdfAlgo = tLRPC$account_Password2.current_algo;
        if (tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestPermissionsResultFragment$68(DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
            getParentActivity().startActivity(intent);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$2() {
        ViewGroup viewGroup;
        ViewGroup[] viewGroupArr = this.inputFieldContainers;
        if (viewGroupArr == null || (viewGroup = viewGroupArr[0]) == null || viewGroup.getVisibility() != 0) {
            return;
        }
        this.inputFields[0].requestFocus();
        AndroidUtilities.showKeyboard(this.inputFields[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTransitionAnimationEnd$67() {
        presentFragment(this.presentAfterAnimation, true);
        this.presentAfterAnimation = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0050  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$openAddDocumentAlert$23(ArrayList arrayList, DialogInterface dialogInterface, int i) {
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2;
        TLRPC$SecureValueType tLRPC$TL_secureValueTypeAddress;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3 = null;
        try {
            tLRPC$TL_secureRequiredType = new TLRPC$TL_secureRequiredType();
            try {
                tLRPC$TL_secureRequiredType.type = (TLRPC$SecureValueType) ((Class) arrayList.get(i)).newInstance();
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            tLRPC$TL_secureRequiredType = null;
        }
        if (!isPersonalDocument(tLRPC$TL_secureRequiredType.type)) {
            if (isAddressDocument(tLRPC$TL_secureRequiredType.type)) {
                tLRPC$TL_secureRequiredType2 = new TLRPC$TL_secureRequiredType();
                tLRPC$TL_secureValueTypeAddress = new TLRPC$TL_secureValueTypeAddress();
            }
            openTypeActivity(tLRPC$TL_secureRequiredType, tLRPC$TL_secureRequiredType3, new ArrayList(), tLRPC$TL_secureRequiredType3 != null);
        }
        tLRPC$TL_secureRequiredType.selfie_required = true;
        tLRPC$TL_secureRequiredType.translation_required = true;
        tLRPC$TL_secureRequiredType2 = new TLRPC$TL_secureRequiredType();
        tLRPC$TL_secureValueTypeAddress = new TLRPC$TL_secureValueTypePersonalDetails();
        tLRPC$TL_secureRequiredType2.type = tLRPC$TL_secureValueTypeAddress;
        tLRPC$TL_secureRequiredType3 = tLRPC$TL_secureRequiredType;
        tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType2;
        openTypeActivity(tLRPC$TL_secureRequiredType, tLRPC$TL_secureRequiredType3, new ArrayList(), tLRPC$TL_secureRequiredType3 != null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSelectedFiles$70(SecureDocument secureDocument, int i) {
        int i2 = this.uploadingFileType;
        if (i2 == 1) {
            SecureDocument secureDocument2 = this.selfieDocument;
            if (secureDocument2 != null) {
                SecureDocumentCell secureDocumentCell = (SecureDocumentCell) this.documentsCells.remove(secureDocument2);
                if (secureDocumentCell != null) {
                    this.selfieLayout.removeView(secureDocumentCell);
                }
                this.selfieDocument = null;
            }
        } else if (i2 == 4) {
            if (this.translationDocuments.size() >= 20) {
                return;
            }
        } else if (i2 == 2) {
            SecureDocument secureDocument3 = this.frontDocument;
            if (secureDocument3 != null) {
                SecureDocumentCell secureDocumentCell2 = (SecureDocumentCell) this.documentsCells.remove(secureDocument3);
                if (secureDocumentCell2 != null) {
                    this.frontLayout.removeView(secureDocumentCell2);
                }
                this.frontDocument = null;
            }
        } else if (i2 == 3) {
            SecureDocument secureDocument4 = this.reverseDocument;
            if (secureDocument4 != null) {
                SecureDocumentCell secureDocumentCell3 = (SecureDocumentCell) this.documentsCells.remove(secureDocument4);
                if (secureDocumentCell3 != null) {
                    this.reverseLayout.removeView(secureDocumentCell3);
                }
                this.reverseDocument = null;
            }
        } else if (i2 == 0 && this.documents.size() >= 20) {
            return;
        }
        this.uploadingDocuments.put(secureDocument.path, secureDocument);
        this.doneItem.setEnabled(false);
        this.doneItem.setAlpha(0.5f);
        FileLoader.getInstance(this.currentAccount).uploadFile(secureDocument.path, false, true, ConnectionsManager.FileTypePhoto);
        addDocumentView(secureDocument, i);
        updateUploadText(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSelectedFiles$71(MrzRecognizer.Result result) {
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType;
        int i;
        int i2;
        EditTextBoldCursor editTextBoldCursor;
        int i3;
        int i4 = result.type;
        if (i4 == 2) {
            if (!(this.currentDocumentsType.type instanceof TLRPC$TL_secureValueTypeIdentityCard)) {
                int size = this.availableDocumentTypes.size();
                for (int i5 = 0; i5 < size; i5++) {
                    tLRPC$TL_secureRequiredType = (TLRPC$TL_secureRequiredType) this.availableDocumentTypes.get(i5);
                    if (tLRPC$TL_secureRequiredType.type instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                        this.currentDocumentsType = tLRPC$TL_secureRequiredType;
                        updateInterfaceStringsForDocumentType();
                        break;
                    }
                }
            }
        } else if (i4 == 1) {
            if (!(this.currentDocumentsType.type instanceof TLRPC$TL_secureValueTypePassport)) {
                int size2 = this.availableDocumentTypes.size();
                for (int i6 = 0; i6 < size2; i6++) {
                    tLRPC$TL_secureRequiredType = (TLRPC$TL_secureRequiredType) this.availableDocumentTypes.get(i6);
                    if (tLRPC$TL_secureRequiredType.type instanceof TLRPC$TL_secureValueTypePassport) {
                        this.currentDocumentsType = tLRPC$TL_secureRequiredType;
                        updateInterfaceStringsForDocumentType();
                        break;
                    }
                }
            }
        } else if (i4 == 3) {
            if (!(this.currentDocumentsType.type instanceof TLRPC$TL_secureValueTypeInternalPassport)) {
                int size3 = this.availableDocumentTypes.size();
                for (int i7 = 0; i7 < size3; i7++) {
                    tLRPC$TL_secureRequiredType = (TLRPC$TL_secureRequiredType) this.availableDocumentTypes.get(i7);
                    if (tLRPC$TL_secureRequiredType.type instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                        this.currentDocumentsType = tLRPC$TL_secureRequiredType;
                        updateInterfaceStringsForDocumentType();
                        break;
                    }
                }
            }
        } else if (i4 == 4 && !(this.currentDocumentsType.type instanceof TLRPC$TL_secureValueTypeDriverLicense)) {
            int size4 = this.availableDocumentTypes.size();
            for (int i8 = 0; i8 < size4; i8++) {
                tLRPC$TL_secureRequiredType = (TLRPC$TL_secureRequiredType) this.availableDocumentTypes.get(i8);
                if (tLRPC$TL_secureRequiredType.type instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                    this.currentDocumentsType = tLRPC$TL_secureRequiredType;
                    updateInterfaceStringsForDocumentType();
                    break;
                }
            }
        }
        if (!TextUtils.isEmpty(result.firstName)) {
            this.inputFields[0].setText(result.firstName);
        }
        if (!TextUtils.isEmpty(result.middleName)) {
            this.inputFields[1].setText(result.middleName);
        }
        if (!TextUtils.isEmpty(result.lastName)) {
            this.inputFields[2].setText(result.lastName);
        }
        if (!TextUtils.isEmpty(result.number)) {
            this.inputFields[7].setText(result.number);
        }
        int i9 = result.gender;
        if (i9 != 0) {
            if (i9 == 1) {
                this.currentGender = "male";
                editTextBoldCursor = this.inputFields[4];
                i3 = R.string.PassportMale;
            } else if (i9 == 2) {
                this.currentGender = "female";
                editTextBoldCursor = this.inputFields[4];
                i3 = R.string.PassportFemale;
            }
            editTextBoldCursor.setText(LocaleController.getString(i3));
        }
        if (!TextUtils.isEmpty(result.nationality)) {
            String str = result.nationality;
            this.currentCitizeship = str;
            String str2 = (String) this.languageMap.get(str);
            if (str2 != null) {
                this.inputFields[5].setText(str2);
            }
        }
        if (!TextUtils.isEmpty(result.issuingCountry)) {
            String str3 = result.issuingCountry;
            this.currentResidence = str3;
            String str4 = (String) this.languageMap.get(str3);
            if (str4 != null) {
                this.inputFields[6].setText(str4);
            }
        }
        int i10 = result.birthDay;
        if (i10 > 0 && result.birthMonth > 0 && result.birthYear > 0) {
            this.inputFields[3].setText(String.format(Locale.US, "%02d.%02d.%d", Integer.valueOf(i10), Integer.valueOf(result.birthMonth), Integer.valueOf(result.birthYear)));
        }
        int i11 = result.expiryDay;
        if (i11 <= 0 || (i = result.expiryMonth) <= 0 || (i2 = result.expiryYear) <= 0) {
            int[] iArr = this.currentExpireDate;
            iArr[2] = 0;
            iArr[1] = 0;
            iArr[0] = 0;
            this.inputFields[8].setText(LocaleController.getString(R.string.PassportNoExpireDate));
            return;
        }
        int[] iArr2 = this.currentExpireDate;
        iArr2[0] = i2;
        iArr2[1] = i;
        iArr2[2] = i11;
        this.inputFields[8].setText(String.format(Locale.US, "%02d.%02d.%d", Integer.valueOf(i11), Integer.valueOf(result.expiryMonth), Integer.valueOf(result.expiryYear)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSelectedFiles$72(ArrayList arrayList, final int i, boolean z) {
        TLRPC$PhotoSize scaleAndSaveImage;
        int i2 = this.uploadingFileType;
        int min = Math.min((i2 == 0 || i2 == 4) ? 20 : 1, arrayList.size());
        boolean z2 = false;
        for (int i3 = 0; i3 < min; i3++) {
            SendMessagesHelper.SendingMediaInfo sendingMediaInfo = (SendMessagesHelper.SendingMediaInfo) arrayList.get(i3);
            Bitmap loadBitmap = ImageLoader.loadBitmap(sendingMediaInfo.path, sendingMediaInfo.uri, 2048.0f, 2048.0f, false);
            if (loadBitmap != null && (scaleAndSaveImage = ImageLoader.scaleAndSaveImage(loadBitmap, 2048.0f, 2048.0f, 89, false, 320, 320)) != null) {
                TLRPC$TL_secureFile tLRPC$TL_secureFile = new TLRPC$TL_secureFile();
                TLRPC$FileLocation tLRPC$FileLocation = scaleAndSaveImage.location;
                tLRPC$TL_secureFile.dc_id = (int) tLRPC$FileLocation.volume_id;
                tLRPC$TL_secureFile.id = tLRPC$FileLocation.local_id;
                tLRPC$TL_secureFile.date = (int) (System.currentTimeMillis() / 1000);
                final SecureDocument saveFile = this.delegate.saveFile(tLRPC$TL_secureFile);
                saveFile.type = i;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda57
                    @Override // java.lang.Runnable
                    public final void run() {
                        PassportActivity.this.lambda$processSelectedFiles$70(saveFile, i);
                    }
                });
                if (z && !z2) {
                    try {
                        final MrzRecognizer.Result recognize = MrzRecognizer.recognize(loadBitmap, this.currentDocumentsType.type instanceof TLRPC$TL_secureValueTypeDriverLicense);
                        if (recognize != null) {
                            try {
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda58
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        PassportActivity.this.lambda$processSelectedFiles$71(recognize);
                                    }
                                });
                                z2 = true;
                            } catch (Throwable th) {
                                th = th;
                                z2 = true;
                                FileLog.e(th);
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        SharedConfig.saveConfig();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startPhoneVerification$65(TLRPC$TL_error tLRPC$TL_error, String str, PassportActivityDelegate passportActivityDelegate, TLObject tLObject, TLRPC$TL_account_sendVerifyPhoneCode tLRPC$TL_account_sendVerifyPhoneCode) {
        if (tLRPC$TL_error != null) {
            AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, this, tLRPC$TL_account_sendVerifyPhoneCode, str);
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("phone", str);
        PassportActivity passportActivity = new PassportActivity(7, this.currentForm, this.currentPassword, this.currentType, (TLRPC$TL_secureValue) null, (TLRPC$TL_secureRequiredType) null, (TLRPC$TL_secureValue) null, hashMap, (HashMap) null);
        passportActivity.currentAccount = this.currentAccount;
        passportActivity.saltedPassword = this.saltedPassword;
        passportActivity.secureSecret = this.secureSecret;
        passportActivity.delegate = passportActivityDelegate;
        passportActivity.currentPhoneVerification = (TLRPC$TL_auth_sentCode) tLObject;
        presentFragment(passportActivity, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startPhoneVerification$66(final String str, final PassportActivityDelegate passportActivityDelegate, final TLRPC$TL_account_sendVerifyPhoneCode tLRPC$TL_account_sendVerifyPhoneCode, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda65
            @Override // java.lang.Runnable
            public final void run() {
                PassportActivity.this.lambda$startPhoneVerification$65(tLRPC$TL_error, str, passportActivityDelegate, tLObject, tLRPC$TL_account_sendVerifyPhoneCode);
            }
        });
    }

    private void loadPasswordInfo() {
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda7
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PassportActivity.this.lambda$loadPasswordInfo$4(tLObject, tLRPC$TL_error);
            }
        }), this.classGuid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFieldError(View view) {
        if (view == null) {
            return;
        }
        Vibrator vibrator = (Vibrator) getParentActivity().getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(200L);
        }
        AndroidUtilities.shakeView(view);
        scrollToField(view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPasscodeError(boolean z) {
        if (getParentActivity() == null) {
            return;
        }
        Vibrator vibrator = (Vibrator) getParentActivity().getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(200L);
        }
        if (z) {
            this.inputFields[0].setText("");
        }
        AndroidUtilities.shakeView(this.inputFields[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPasswordDone(final boolean z) {
        final String obj;
        if (z) {
            obj = null;
        } else {
            obj = this.inputFields[0].getText().toString();
            if (TextUtils.isEmpty(obj)) {
                onPasscodeError(false);
                return;
            }
            showEditDoneProgress(true, true);
        }
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                PassportActivity.this.lambda$onPasswordDone$13(z, obj);
            }
        });
    }

    private void openAddDocumentAlert() {
        ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        if (hasNotValueForType(TLRPC$TL_secureValueTypePhone.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentPhone));
            arrayList2.add(TLRPC$TL_secureValueTypePhone.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeEmail.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentEmail));
            arrayList2.add(TLRPC$TL_secureValueTypeEmail.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypePersonalDetails.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentIdentity));
            arrayList2.add(TLRPC$TL_secureValueTypePersonalDetails.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypePassport.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentPassport));
            arrayList2.add(TLRPC$TL_secureValueTypePassport.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeInternalPassport.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentInternalPassport));
            arrayList2.add(TLRPC$TL_secureValueTypeInternalPassport.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypePassportRegistration.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentPassportRegistration));
            arrayList2.add(TLRPC$TL_secureValueTypePassportRegistration.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeTemporaryRegistration.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentTemporaryRegistration));
            arrayList2.add(TLRPC$TL_secureValueTypeTemporaryRegistration.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeIdentityCard.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentIdentityCard));
            arrayList2.add(TLRPC$TL_secureValueTypeIdentityCard.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeDriverLicense.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentDriverLicence));
            arrayList2.add(TLRPC$TL_secureValueTypeDriverLicense.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeAddress.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentAddress));
            arrayList2.add(TLRPC$TL_secureValueTypeAddress.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeUtilityBill.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentUtilityBill));
            arrayList2.add(TLRPC$TL_secureValueTypeUtilityBill.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeBankStatement.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentBankStatement));
            arrayList2.add(TLRPC$TL_secureValueTypeBankStatement.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeRentalAgreement.class)) {
            arrayList.add(LocaleController.getString(R.string.ActionBotDocumentRentalAgreement));
            arrayList2.add(TLRPC$TL_secureValueTypeRentalAgreement.class);
        }
        if (getParentActivity() == null || arrayList.isEmpty()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.PassportNoDocumentsAdd));
        builder.setItems((CharSequence[]) arrayList.toArray(new CharSequence[0]), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda40
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PassportActivity.this.lambda$openAddDocumentAlert$23(arrayList2, dialogInterface, i);
            }
        });
        showDialog(builder.create());
    }

    private void openAttachMenu() {
        if (getParentActivity() == null) {
            return;
        }
        if (this.uploadingFileType == 0 && this.documents.size() >= 20) {
            showAlertWithText(LocaleController.getString(R.string.AppName), LocaleController.formatString("PassportUploadMaxReached", R.string.PassportUploadMaxReached, LocaleController.formatPluralString("Files", 20, new Object[0])));
            return;
        }
        createChatAttachView();
        this.chatAttachAlert.setOpenWithFrontFaceCamera(this.uploadingFileType == 1);
        this.chatAttachAlert.setMaxSelectedPhotos(getMaxSelectedDocuments(), false);
        this.chatAttachAlert.getPhotoLayout().loadGalleryPhotos();
        int i = Build.VERSION.SDK_INT;
        if (i == 21 || i == 22) {
            AndroidUtilities.hideKeyboard(this.fragmentView.findFocus());
        }
        this.chatAttachAlert.init();
        showDialog(this.chatAttachAlert);
    }

    private void openTypeActivity(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, ArrayList arrayList, final boolean z) {
        TLRPC$account_Password tLRPC$account_Password;
        HashMap hashMap;
        final int size = arrayList != null ? arrayList.size() : 0;
        final TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
        TLRPC$SecureValueType tLRPC$SecureValueType2 = tLRPC$TL_secureRequiredType2 != null ? tLRPC$TL_secureRequiredType2.type : null;
        int i = tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails ? 1 : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress ? 2 : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone ? 3 : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail ? 4 : -1;
        if (i != -1) {
            HashMap hashMap2 = !z ? (HashMap) this.errorsMap.get(getNameForType(tLRPC$SecureValueType)) : null;
            HashMap hashMap3 = (HashMap) this.errorsMap.get(getNameForType(tLRPC$SecureValueType2));
            TLRPC$TL_secureValue valueByType = getValueByType(tLRPC$TL_secureRequiredType, false);
            TLRPC$TL_secureValue valueByType2 = getValueByType(tLRPC$TL_secureRequiredType2, false);
            TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm = this.currentForm;
            TLRPC$account_Password tLRPC$account_Password2 = this.currentPassword;
            HashMap hashMap4 = (HashMap) this.typesValues.get(tLRPC$TL_secureRequiredType);
            if (tLRPC$TL_secureRequiredType2 != null) {
                tLRPC$account_Password = tLRPC$account_Password2;
                hashMap = (HashMap) this.typesValues.get(tLRPC$TL_secureRequiredType2);
            } else {
                tLRPC$account_Password = tLRPC$account_Password2;
                hashMap = null;
            }
            int i2 = i;
            PassportActivity passportActivity = new PassportActivity(i, tLRPC$TL_account_authorizationForm, tLRPC$account_Password, tLRPC$TL_secureRequiredType, valueByType, tLRPC$TL_secureRequiredType2, valueByType2, hashMap4, hashMap);
            passportActivity.delegate = new PassportActivityDelegate() { // from class: org.telegram.ui.PassportActivity.20

                /* JADX INFO: Access modifiers changed from: package-private */
                /* loaded from: classes4.dex */
                public class 1 implements RequestDelegate {
                    final /* synthetic */ PassportActivityDelegate val$currentDelegate;
                    final /* synthetic */ TLRPC$TL_secureRequiredType val$documentRequiredType;
                    final /* synthetic */ ArrayList val$documents;
                    final /* synthetic */ String val$documentsJson;
                    final /* synthetic */ ErrorRunnable val$errorRunnable;
                    final /* synthetic */ TLRPC$TL_inputSecureValue val$finalFileInputSecureValue;
                    final /* synthetic */ Runnable val$finishRunnable;
                    final /* synthetic */ SecureDocument val$front;
                    final /* synthetic */ String val$json;
                    final /* synthetic */ TLRPC$TL_account_saveSecureValue val$req;
                    final /* synthetic */ TLRPC$TL_secureRequiredType val$requiredType;
                    final /* synthetic */ SecureDocument val$reverse;
                    final /* synthetic */ SecureDocument val$selfie;
                    final /* synthetic */ String val$text;
                    final /* synthetic */ ArrayList val$translationDocuments;

                    1(ErrorRunnable errorRunnable, String str, TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, ArrayList arrayList, SecureDocument secureDocument, SecureDocument secureDocument2, SecureDocument secureDocument3, ArrayList arrayList2, String str2, String str3, Runnable runnable, PassportActivityDelegate passportActivityDelegate, TLRPC$TL_inputSecureValue tLRPC$TL_inputSecureValue) {
                        this.val$errorRunnable = errorRunnable;
                        this.val$text = str;
                        this.val$req = tLRPC$TL_account_saveSecureValue;
                        this.val$documentRequiredType = tLRPC$TL_secureRequiredType;
                        this.val$requiredType = tLRPC$TL_secureRequiredType2;
                        this.val$documents = arrayList;
                        this.val$selfie = secureDocument;
                        this.val$front = secureDocument2;
                        this.val$reverse = secureDocument3;
                        this.val$translationDocuments = arrayList2;
                        this.val$json = str2;
                        this.val$documentsJson = str3;
                        this.val$finishRunnable = runnable;
                        this.val$currentDelegate = passportActivityDelegate;
                        this.val$finalFileInputSecureValue = tLRPC$TL_inputSecureValue;
                    }

                    /* JADX INFO: Access modifiers changed from: private */
                    /* JADX WARN: Removed duplicated region for block: B:14:0x0056  */
                    /* JADX WARN: Removed duplicated region for block: B:16:0x0065  */
                    /* JADX WARN: Removed duplicated region for block: B:22:0x0081  */
                    /* JADX WARN: Removed duplicated region for block: B:40:0x00d9  */
                    /* JADX WARN: Removed duplicated region for block: B:48:0x00f7  */
                    /* JADX WARN: Removed duplicated region for block: B:56:0x0115  */
                    /* JADX WARN: Removed duplicated region for block: B:64:0x0136  */
                    /* JADX WARN: Removed duplicated region for block: B:80:0x018a  */
                    /* JADX WARN: Removed duplicated region for block: B:93:? A[RETURN, SYNTHETIC] */
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                    */
                    public /* synthetic */ void lambda$onResult$0(TLRPC$TL_error tLRPC$TL_error, ErrorRunnable errorRunnable, String str, TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue, boolean z, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, TLRPC$TL_secureValue tLRPC$TL_secureValue, TLRPC$TL_secureValue tLRPC$TL_secureValue2, ArrayList arrayList, SecureDocument secureDocument, SecureDocument secureDocument2, SecureDocument secureDocument3, ArrayList arrayList2, String str2, String str3, int i, Runnable runnable) {
                        int size;
                        int i2;
                        int i3;
                        TLRPC$SecureFile tLRPC$SecureFile;
                        TLRPC$SecureFile tLRPC$SecureFile2;
                        TLRPC$SecureFile tLRPC$SecureFile3;
                        int size2;
                        int i4;
                        int i5;
                        if (tLRPC$TL_error != null) {
                            if (errorRunnable != null) {
                                errorRunnable.onError(tLRPC$TL_error.text, str);
                            }
                            AlertsCreator.processError(((BaseFragment) PassportActivity.this).currentAccount, tLRPC$TL_error, PassportActivity.this, tLRPC$TL_account_saveSecureValue, str);
                            return;
                        }
                        PassportActivity passportActivity = PassportActivity.this;
                        if (!z) {
                            passportActivity.removeValue(tLRPC$TL_secureRequiredType2);
                            passportActivity = PassportActivity.this;
                        } else if (tLRPC$TL_secureRequiredType == null) {
                            passportActivity.removeValue(tLRPC$TL_secureRequiredType2);
                            if (tLRPC$TL_secureValue != null) {
                                PassportActivity.this.currentForm.values.add(tLRPC$TL_secureValue);
                            }
                            if (tLRPC$TL_secureValue2 != null) {
                                PassportActivity.this.currentForm.values.add(tLRPC$TL_secureValue2);
                            }
                            if (arrayList != null && !arrayList.isEmpty()) {
                                size2 = arrayList.size();
                                i4 = 0;
                                while (i4 < size2) {
                                    SecureDocument secureDocument4 = (SecureDocument) arrayList.get(i4);
                                    if (secureDocument4.inputFile != null) {
                                        int size3 = tLRPC$TL_secureValue.files.size();
                                        int i6 = 0;
                                        while (i6 < size3) {
                                            TLRPC$SecureFile tLRPC$SecureFile4 = (TLRPC$SecureFile) tLRPC$TL_secureValue.files.get(i6);
                                            i5 = size2;
                                            if (tLRPC$SecureFile4 instanceof TLRPC$TL_secureFile) {
                                                TLRPC$TL_secureFile tLRPC$TL_secureFile = (TLRPC$TL_secureFile) tLRPC$SecureFile4;
                                                if (Utilities.arraysEquals(secureDocument4.fileSecret, 0, tLRPC$TL_secureFile.secret, 0)) {
                                                    renameFile(secureDocument4, tLRPC$TL_secureFile);
                                                    break;
                                                }
                                            }
                                            i6++;
                                            size2 = i5;
                                        }
                                    }
                                    i5 = size2;
                                    i4++;
                                    size2 = i5;
                                }
                            }
                            if (secureDocument != null && secureDocument.inputFile != null) {
                                tLRPC$SecureFile3 = tLRPC$TL_secureValue.selfie;
                                if (tLRPC$SecureFile3 instanceof TLRPC$TL_secureFile) {
                                    TLRPC$TL_secureFile tLRPC$TL_secureFile2 = (TLRPC$TL_secureFile) tLRPC$SecureFile3;
                                    if (Utilities.arraysEquals(secureDocument.fileSecret, 0, tLRPC$TL_secureFile2.secret, 0)) {
                                        renameFile(secureDocument, tLRPC$TL_secureFile2);
                                    }
                                }
                            }
                            if (secureDocument2 != null && secureDocument2.inputFile != null) {
                                tLRPC$SecureFile2 = tLRPC$TL_secureValue.front_side;
                                if (tLRPC$SecureFile2 instanceof TLRPC$TL_secureFile) {
                                    TLRPC$TL_secureFile tLRPC$TL_secureFile3 = (TLRPC$TL_secureFile) tLRPC$SecureFile2;
                                    if (Utilities.arraysEquals(secureDocument2.fileSecret, 0, tLRPC$TL_secureFile3.secret, 0)) {
                                        renameFile(secureDocument2, tLRPC$TL_secureFile3);
                                    }
                                }
                            }
                            if (secureDocument3 != null && secureDocument3.inputFile != null) {
                                tLRPC$SecureFile = tLRPC$TL_secureValue.reverse_side;
                                if (tLRPC$SecureFile instanceof TLRPC$TL_secureFile) {
                                    TLRPC$TL_secureFile tLRPC$TL_secureFile4 = (TLRPC$TL_secureFile) tLRPC$SecureFile;
                                    if (Utilities.arraysEquals(secureDocument3.fileSecret, 0, tLRPC$TL_secureFile4.secret, 0)) {
                                        renameFile(secureDocument3, tLRPC$TL_secureFile4);
                                    }
                                }
                            }
                            if (arrayList2 != null && !arrayList2.isEmpty()) {
                                size = arrayList2.size();
                                i2 = 0;
                                while (i2 < size) {
                                    SecureDocument secureDocument5 = (SecureDocument) arrayList2.get(i2);
                                    if (secureDocument5.inputFile != null) {
                                        int size4 = tLRPC$TL_secureValue.translation.size();
                                        for (int i7 = 0; i7 < size4; i7++) {
                                            TLRPC$SecureFile tLRPC$SecureFile5 = (TLRPC$SecureFile) tLRPC$TL_secureValue.translation.get(i7);
                                            if (tLRPC$SecureFile5 instanceof TLRPC$TL_secureFile) {
                                                TLRPC$TL_secureFile tLRPC$TL_secureFile5 = (TLRPC$TL_secureFile) tLRPC$SecureFile5;
                                                if (Utilities.arraysEquals(secureDocument5.fileSecret, 0, tLRPC$TL_secureFile5.secret, 0)) {
                                                    renameFile(secureDocument5, tLRPC$TL_secureFile5);
                                                    i3 = 1;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    i3 = 1;
                                    i2 += i3;
                                }
                            }
                            PassportActivity.this.setTypeValue(tLRPC$TL_secureRequiredType2, str, str2, tLRPC$TL_secureRequiredType, str3, z, i);
                            if (runnable == null) {
                                runnable.run();
                                return;
                            }
                            return;
                        }
                        passportActivity.removeValue(tLRPC$TL_secureRequiredType);
                        if (tLRPC$TL_secureValue != null) {
                        }
                        if (tLRPC$TL_secureValue2 != null) {
                        }
                        if (arrayList != null) {
                            size2 = arrayList.size();
                            i4 = 0;
                            while (i4 < size2) {
                            }
                        }
                        if (secureDocument != null) {
                            tLRPC$SecureFile3 = tLRPC$TL_secureValue.selfie;
                            if (tLRPC$SecureFile3 instanceof TLRPC$TL_secureFile) {
                            }
                        }
                        if (secureDocument2 != null) {
                            tLRPC$SecureFile2 = tLRPC$TL_secureValue.front_side;
                            if (tLRPC$SecureFile2 instanceof TLRPC$TL_secureFile) {
                            }
                        }
                        if (secureDocument3 != null) {
                            tLRPC$SecureFile = tLRPC$TL_secureValue.reverse_side;
                            if (tLRPC$SecureFile instanceof TLRPC$TL_secureFile) {
                            }
                        }
                        if (arrayList2 != null) {
                            size = arrayList2.size();
                            i2 = 0;
                            while (i2 < size) {
                            }
                        }
                        PassportActivity.this.setTypeValue(tLRPC$TL_secureRequiredType2, str, str2, tLRPC$TL_secureRequiredType, str3, z, i);
                        if (runnable == null) {
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: private */
                    public /* synthetic */ void lambda$run$1(TLObject tLObject, String str, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, PassportActivityDelegate passportActivityDelegate, TLRPC$TL_error tLRPC$TL_error, ErrorRunnable errorRunnable) {
                        if (tLObject == null) {
                            PassportActivity.this.showAlertWithText(LocaleController.getString(R.string.PassportEmail), tLRPC$TL_error.text);
                            if (errorRunnable != null) {
                                errorRunnable.onError(tLRPC$TL_error.text, str);
                                return;
                            }
                            return;
                        }
                        TLRPC$TL_account_sentEmailCode tLRPC$TL_account_sentEmailCode = (TLRPC$TL_account_sentEmailCode) tLObject;
                        HashMap hashMap = new HashMap();
                        hashMap.put("email", str);
                        hashMap.put("pattern", tLRPC$TL_account_sentEmailCode.email_pattern);
                        PassportActivity passportActivity = new PassportActivity(6, PassportActivity.this.currentForm, PassportActivity.this.currentPassword, tLRPC$TL_secureRequiredType, (TLRPC$TL_secureValue) null, (TLRPC$TL_secureRequiredType) null, (TLRPC$TL_secureValue) null, hashMap, (HashMap) null);
                        ((BaseFragment) passportActivity).currentAccount = ((BaseFragment) PassportActivity.this).currentAccount;
                        passportActivity.emailCodeLength = tLRPC$TL_account_sentEmailCode.length;
                        passportActivity.saltedPassword = PassportActivity.this.saltedPassword;
                        passportActivity.secureSecret = PassportActivity.this.secureSecret;
                        passportActivity.delegate = passportActivityDelegate;
                        PassportActivity.this.presentFragment(passportActivity, true);
                    }

                    /* JADX INFO: Access modifiers changed from: private */
                    public /* synthetic */ void lambda$run$2(final String str, final TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, final PassportActivityDelegate passportActivityDelegate, final ErrorRunnable errorRunnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$20$1$$ExternalSyntheticLambda4
                            @Override // java.lang.Runnable
                            public final void run() {
                                PassportActivity.20.1.this.lambda$run$1(tLObject, str, tLRPC$TL_secureRequiredType, passportActivityDelegate, tLRPC$TL_error, errorRunnable);
                            }
                        });
                    }

                    /* JADX INFO: Access modifiers changed from: private */
                    public static /* synthetic */ void lambda$run$3(ErrorRunnable errorRunnable, TLRPC$TL_error tLRPC$TL_error, String str) {
                        errorRunnable.onError(tLRPC$TL_error.text, str);
                    }

                    /* JADX INFO: Access modifiers changed from: private */
                    /* renamed from: onResult */
                    public void lambda$run$4(final TLRPC$TL_error tLRPC$TL_error, final TLRPC$TL_secureValue tLRPC$TL_secureValue, final TLRPC$TL_secureValue tLRPC$TL_secureValue2) {
                        final ErrorRunnable errorRunnable = this.val$errorRunnable;
                        final String str = this.val$text;
                        final TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue = this.val$req;
                        20 r15 = 20.this;
                        final boolean z = z;
                        final TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType = this.val$documentRequiredType;
                        final TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2 = this.val$requiredType;
                        final ArrayList arrayList = this.val$documents;
                        final SecureDocument secureDocument = this.val$selfie;
                        final SecureDocument secureDocument2 = this.val$front;
                        final SecureDocument secureDocument3 = this.val$reverse;
                        final ArrayList arrayList2 = this.val$translationDocuments;
                        final String str2 = this.val$json;
                        final String str3 = this.val$documentsJson;
                        final int i = size;
                        final Runnable runnable = this.val$finishRunnable;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$20$1$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                PassportActivity.20.1.this.lambda$onResult$0(tLRPC$TL_error, errorRunnable, str, tLRPC$TL_account_saveSecureValue, z, tLRPC$TL_secureRequiredType, tLRPC$TL_secureRequiredType2, tLRPC$TL_secureValue, tLRPC$TL_secureValue2, arrayList, secureDocument, secureDocument2, secureDocument3, arrayList2, str2, str3, i, runnable);
                            }
                        });
                    }

                    @Override // org.telegram.tgnet.RequestDelegate
                    public void run(TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
                        if (tLRPC$TL_error != null) {
                            if (tLRPC$TL_error.text.equals("EMAIL_VERIFICATION_NEEDED")) {
                                TLRPC$TL_account_sendVerifyEmailCode tLRPC$TL_account_sendVerifyEmailCode = new TLRPC$TL_account_sendVerifyEmailCode();
                                tLRPC$TL_account_sendVerifyEmailCode.purpose = new TLRPC$TL_emailVerifyPurposePassport();
                                tLRPC$TL_account_sendVerifyEmailCode.email = this.val$text;
                                ConnectionsManager connectionsManager = ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount);
                                final String str = this.val$text;
                                final TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType = this.val$requiredType;
                                final PassportActivityDelegate passportActivityDelegate = this.val$currentDelegate;
                                final ErrorRunnable errorRunnable = this.val$errorRunnable;
                                connectionsManager.sendRequest(tLRPC$TL_account_sendVerifyEmailCode, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$20$1$$ExternalSyntheticLambda0
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                                        PassportActivity.20.1.this.lambda$run$2(str, tLRPC$TL_secureRequiredType, passportActivityDelegate, errorRunnable, tLObject2, tLRPC$TL_error2);
                                    }
                                });
                                return;
                            } else if (tLRPC$TL_error.text.equals("PHONE_VERIFICATION_NEEDED")) {
                                final ErrorRunnable errorRunnable2 = this.val$errorRunnable;
                                final String str2 = this.val$text;
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$20$1$$ExternalSyntheticLambda1
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        PassportActivity.20.1.lambda$run$3(PassportActivity.ErrorRunnable.this, tLRPC$TL_error, str2);
                                    }
                                });
                                return;
                            }
                        }
                        if (tLRPC$TL_error != null || this.val$finalFileInputSecureValue == null) {
                            lambda$run$4(tLRPC$TL_error, (TLRPC$TL_secureValue) tLObject, null);
                            return;
                        }
                        final TLRPC$TL_secureValue tLRPC$TL_secureValue = (TLRPC$TL_secureValue) tLObject;
                        TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue = new TLRPC$TL_account_saveSecureValue();
                        tLRPC$TL_account_saveSecureValue.value = this.val$finalFileInputSecureValue;
                        tLRPC$TL_account_saveSecureValue.secure_secret_id = PassportActivity.this.secureSecretId;
                        ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_saveSecureValue, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$20$1$$ExternalSyntheticLambda2
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                                PassportActivity.20.1.this.lambda$run$4(tLRPC$TL_secureValue, tLObject2, tLRPC$TL_error2);
                            }
                        });
                    }
                }

                private TLRPC$InputSecureFile getInputSecureFile(SecureDocument secureDocument) {
                    if (secureDocument.inputFile == null) {
                        TLRPC$TL_inputSecureFile tLRPC$TL_inputSecureFile = new TLRPC$TL_inputSecureFile();
                        TLRPC$TL_secureFile tLRPC$TL_secureFile = secureDocument.secureFile;
                        tLRPC$TL_inputSecureFile.id = tLRPC$TL_secureFile.id;
                        tLRPC$TL_inputSecureFile.access_hash = tLRPC$TL_secureFile.access_hash;
                        return tLRPC$TL_inputSecureFile;
                    }
                    TLRPC$TL_inputSecureFileUploaded tLRPC$TL_inputSecureFileUploaded = new TLRPC$TL_inputSecureFileUploaded();
                    TLRPC$TL_inputFile tLRPC$TL_inputFile = secureDocument.inputFile;
                    tLRPC$TL_inputSecureFileUploaded.id = tLRPC$TL_inputFile.id;
                    tLRPC$TL_inputSecureFileUploaded.parts = tLRPC$TL_inputFile.parts;
                    tLRPC$TL_inputSecureFileUploaded.md5_checksum = tLRPC$TL_inputFile.md5_checksum;
                    tLRPC$TL_inputSecureFileUploaded.file_hash = secureDocument.fileHash;
                    tLRPC$TL_inputSecureFileUploaded.secret = secureDocument.fileSecret;
                    return tLRPC$TL_inputSecureFileUploaded;
                }

                /* JADX INFO: Access modifiers changed from: private */
                public void renameFile(SecureDocument secureDocument, TLRPC$TL_secureFile tLRPC$TL_secureFile) {
                    File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(secureDocument);
                    File pathToAttach2 = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$TL_secureFile);
                    pathToAttach.renameTo(pathToAttach2);
                    ImageLoader.getInstance().replaceImageInCache(secureDocument.secureFile.dc_id + "_" + secureDocument.secureFile.id, tLRPC$TL_secureFile.dc_id + "_" + tLRPC$TL_secureFile.id, null, false);
                }

                @Override // org.telegram.ui.PassportActivity.PassportActivityDelegate
                public void deleteValue(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType4, ArrayList arrayList2, boolean z2, Runnable runnable, ErrorRunnable errorRunnable) {
                    PassportActivity.this.deleteValueInternal(tLRPC$TL_secureRequiredType3, tLRPC$TL_secureRequiredType4, arrayList2, z2, runnable, errorRunnable, z);
                }

                @Override // org.telegram.ui.PassportActivity.PassportActivityDelegate
                public SecureDocument saveFile(TLRPC$TL_secureFile tLRPC$TL_secureFile) {
                    String str = FileLoader.getDirectory(4) + "/" + tLRPC$TL_secureFile.dc_id + "_" + tLRPC$TL_secureFile.id + ".jpg";
                    EncryptionResult createSecureDocument = PassportActivity.this.createSecureDocument(str);
                    return new SecureDocument(createSecureDocument.secureDocumentKey, tLRPC$TL_secureFile, str, createSecureDocument.fileHash, createSecureDocument.fileSecret);
                }

                /* JADX WARN: Multi-variable type inference failed */
                @Override // org.telegram.ui.PassportActivity.PassportActivityDelegate
                public void saveValue(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3, String str, String str2, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType4, String str3, ArrayList arrayList2, SecureDocument secureDocument, ArrayList arrayList3, SecureDocument secureDocument2, SecureDocument secureDocument3, Runnable runnable, ErrorRunnable errorRunnable) {
                    TLRPC$TL_inputSecureValue tLRPC$TL_inputSecureValue;
                    TLRPC$TL_securePlainPhone tLRPC$TL_securePlainPhone;
                    TLRPC$TL_inputSecureValue tLRPC$TL_inputSecureValue2;
                    if (!TextUtils.isEmpty(str2)) {
                        tLRPC$TL_inputSecureValue = new TLRPC$TL_inputSecureValue();
                        tLRPC$TL_inputSecureValue.type = tLRPC$TL_secureRequiredType3.type;
                        tLRPC$TL_inputSecureValue.flags |= 1;
                        EncryptionResult encryptData = PassportActivity.this.encryptData(AndroidUtilities.getStringBytes(str2));
                        TLRPC$TL_secureData tLRPC$TL_secureData = new TLRPC$TL_secureData();
                        tLRPC$TL_inputSecureValue.data = tLRPC$TL_secureData;
                        tLRPC$TL_secureData.data = encryptData.encryptedData;
                        tLRPC$TL_secureData.data_hash = encryptData.fileHash;
                        tLRPC$TL_secureData.secret = encryptData.fileSecret;
                    } else if (TextUtils.isEmpty(str)) {
                        tLRPC$TL_inputSecureValue = null;
                    } else {
                        TLRPC$SecureValueType tLRPC$SecureValueType3 = tLRPC$SecureValueType;
                        if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeEmail) {
                            TLRPC$TL_securePlainEmail tLRPC$TL_securePlainEmail = new TLRPC$TL_securePlainEmail();
                            tLRPC$TL_securePlainEmail.email = str;
                            tLRPC$TL_securePlainPhone = tLRPC$TL_securePlainEmail;
                        } else if (!(tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypePhone)) {
                            return;
                        } else {
                            TLRPC$TL_securePlainPhone tLRPC$TL_securePlainPhone2 = new TLRPC$TL_securePlainPhone();
                            tLRPC$TL_securePlainPhone2.phone = str;
                            tLRPC$TL_securePlainPhone = tLRPC$TL_securePlainPhone2;
                        }
                        TLRPC$TL_inputSecureValue tLRPC$TL_inputSecureValue3 = new TLRPC$TL_inputSecureValue();
                        tLRPC$TL_inputSecureValue3.type = tLRPC$TL_secureRequiredType3.type;
                        tLRPC$TL_inputSecureValue3.flags |= 32;
                        tLRPC$TL_inputSecureValue3.plain_data = tLRPC$TL_securePlainPhone;
                        tLRPC$TL_inputSecureValue = tLRPC$TL_inputSecureValue3;
                    }
                    if (!z && tLRPC$TL_inputSecureValue == null) {
                        if (errorRunnable != null) {
                            errorRunnable.onError(null, null);
                            return;
                        }
                        return;
                    }
                    if (tLRPC$TL_secureRequiredType4 != null) {
                        TLRPC$TL_inputSecureValue tLRPC$TL_inputSecureValue4 = new TLRPC$TL_inputSecureValue();
                        tLRPC$TL_inputSecureValue4.type = tLRPC$TL_secureRequiredType4.type;
                        if (!TextUtils.isEmpty(str3)) {
                            tLRPC$TL_inputSecureValue4.flags |= 1;
                            EncryptionResult encryptData2 = PassportActivity.this.encryptData(AndroidUtilities.getStringBytes(str3));
                            TLRPC$TL_secureData tLRPC$TL_secureData2 = new TLRPC$TL_secureData();
                            tLRPC$TL_inputSecureValue4.data = tLRPC$TL_secureData2;
                            tLRPC$TL_secureData2.data = encryptData2.encryptedData;
                            tLRPC$TL_secureData2.data_hash = encryptData2.fileHash;
                            tLRPC$TL_secureData2.secret = encryptData2.fileSecret;
                        }
                        if (secureDocument2 != null) {
                            tLRPC$TL_inputSecureValue4.front_side = getInputSecureFile(secureDocument2);
                            tLRPC$TL_inputSecureValue4.flags |= 2;
                        }
                        if (secureDocument3 != null) {
                            tLRPC$TL_inputSecureValue4.reverse_side = getInputSecureFile(secureDocument3);
                            tLRPC$TL_inputSecureValue4.flags |= 4;
                        }
                        if (secureDocument != null) {
                            tLRPC$TL_inputSecureValue4.selfie = getInputSecureFile(secureDocument);
                            tLRPC$TL_inputSecureValue4.flags |= 8;
                        }
                        if (arrayList3 != null && !arrayList3.isEmpty()) {
                            tLRPC$TL_inputSecureValue4.flags |= 64;
                            int size2 = arrayList3.size();
                            for (int i3 = 0; i3 < size2; i3++) {
                                tLRPC$TL_inputSecureValue4.translation.add(getInputSecureFile((SecureDocument) arrayList3.get(i3)));
                            }
                        }
                        if (arrayList2 != null && !arrayList2.isEmpty()) {
                            tLRPC$TL_inputSecureValue4.flags |= 16;
                            int size3 = arrayList2.size();
                            for (int i4 = 0; i4 < size3; i4++) {
                                tLRPC$TL_inputSecureValue4.files.add(getInputSecureFile((SecureDocument) arrayList2.get(i4)));
                            }
                        }
                        if (!z) {
                            tLRPC$TL_inputSecureValue2 = tLRPC$TL_inputSecureValue4;
                            TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue = new TLRPC$TL_account_saveSecureValue();
                            tLRPC$TL_account_saveSecureValue.value = tLRPC$TL_inputSecureValue;
                            tLRPC$TL_account_saveSecureValue.secure_secret_id = PassportActivity.this.secureSecretId;
                            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_saveSecureValue, new 1(errorRunnable, str, tLRPC$TL_account_saveSecureValue, tLRPC$TL_secureRequiredType4, tLRPC$TL_secureRequiredType3, arrayList2, secureDocument, secureDocument2, secureDocument3, arrayList3, str2, str3, runnable, this, tLRPC$TL_inputSecureValue2));
                        }
                        tLRPC$TL_inputSecureValue = tLRPC$TL_inputSecureValue4;
                    }
                    tLRPC$TL_inputSecureValue2 = null;
                    TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue2 = new TLRPC$TL_account_saveSecureValue();
                    tLRPC$TL_account_saveSecureValue2.value = tLRPC$TL_inputSecureValue;
                    tLRPC$TL_account_saveSecureValue2.secure_secret_id = PassportActivity.this.secureSecretId;
                    ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_saveSecureValue2, new 1(errorRunnable, str, tLRPC$TL_account_saveSecureValue2, tLRPC$TL_secureRequiredType4, tLRPC$TL_secureRequiredType3, arrayList2, secureDocument, secureDocument2, secureDocument3, arrayList3, str2, str3, runnable, this, tLRPC$TL_inputSecureValue2));
                }
            };
            passportActivity.currentAccount = this.currentAccount;
            passportActivity.saltedPassword = this.saltedPassword;
            passportActivity.secureSecret = this.secureSecret;
            passportActivity.currentBotId = this.currentBotId;
            passportActivity.fieldsErrors = hashMap2;
            passportActivity.documentOnly = z;
            passportActivity.documentsErrors = hashMap3;
            passportActivity.availableDocumentTypes = arrayList;
            if (i2 == 4) {
                passportActivity.currentEmail = this.currentEmail;
            }
            presentFragment(passportActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSelectedAttach(int i) {
        int checkSelfPermission;
        if (i == 0) {
            int i2 = Build.VERSION.SDK_INT;
            if (i2 >= 23) {
                checkSelfPermission = getParentActivity().checkSelfPermission("android.permission.CAMERA");
                if (checkSelfPermission != 0) {
                    getParentActivity().requestPermissions(new String[]{"android.permission.CAMERA"}, 19);
                    return;
                }
            }
            try {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File generatePicturePath = AndroidUtilities.generatePicturePath();
                if (generatePicturePath != null) {
                    if (i2 >= 24) {
                        Activity parentActivity = getParentActivity();
                        intent.putExtra("output", FileProvider.getUriForFile(parentActivity, ApplicationLoader.getApplicationId() + ".provider", generatePicturePath));
                        intent.addFlags(2);
                        intent.addFlags(1);
                    } else {
                        intent.putExtra("output", Uri.fromFile(generatePicturePath));
                    }
                    this.currentPicturePath = generatePicturePath.getAbsolutePath();
                }
                startActivityForResult(intent, 0);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSelectedFiles(final ArrayList arrayList) {
        if (arrayList.isEmpty()) {
            return;
        }
        int i = this.uploadingFileType;
        final boolean z = false;
        if (i != 1 && i != 4 && (this.currentType.type instanceof TLRPC$TL_secureValueTypePersonalDetails)) {
            int i2 = 0;
            while (true) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                if (i2 >= editTextBoldCursorArr.length) {
                    z = true;
                    break;
                } else if (i2 != 5 && i2 != 8 && i2 != 4 && i2 != 6 && editTextBoldCursorArr[i2].length() > 0) {
                    break;
                } else {
                    i2++;
                }
            }
        }
        final int i3 = this.uploadingFileType;
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                PassportActivity.this.lambda$processSelectedFiles$72(arrayList, i3, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TLRPC$TL_secureValue removeValue(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType) {
        if (tLRPC$TL_secureRequiredType == null) {
            return null;
        }
        int size = this.currentForm.values.size();
        for (int i = 0; i < size; i++) {
            if (tLRPC$TL_secureRequiredType.type.getClass() == ((TLRPC$TL_secureValue) this.currentForm.values.get(i)).type.getClass()) {
                return (TLRPC$TL_secureValue) this.currentForm.values.remove(i);
            }
        }
        return null;
    }

    private void scrollToField(View view) {
        while (view != null && this.linearLayout2.indexOfChild(view) < 0) {
            view = (View) view.getParent();
        }
        if (view != null) {
            this.scrollView.smoothScrollTo(0, view.getTop() - ((this.scrollView.getMeasuredHeight() - view.getMeasuredHeight()) / 2));
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0052, code lost:
        if (r7 != null) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00c6, code lost:
        if (r7 != null) goto L35;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setFieldValues(HashMap hashMap, EditTextBoldCursor editTextBoldCursor, String str) {
        HashMap hashMap2;
        CharSequence charSequence;
        int i;
        String str2 = (String) hashMap.get(str);
        if (str2 != null) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -2006252145:
                    if (str.equals("residence_country_code")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1249512767:
                    if (str.equals("gender")) {
                        c = 1;
                        break;
                    }
                    break;
                case 475919162:
                    if (str.equals("expiry_date")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1481071862:
                    if (str.equals("country_code")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    this.currentResidence = str2;
                    str2 = (String) this.languageMap.get(str2);
                    break;
                case 1:
                    if ("male".equals(str2)) {
                        this.currentGender = str2;
                        i = R.string.PassportMale;
                    } else if ("female".equals(str2)) {
                        this.currentGender = str2;
                        i = R.string.PassportFemale;
                    }
                    str2 = LocaleController.getString(i);
                    editTextBoldCursor.setText(str2);
                    break;
                case 2:
                    if (!TextUtils.isEmpty(str2)) {
                        String[] split = str2.split("\\.");
                        if (split.length == 3) {
                            this.currentExpireDate[0] = Utilities.parseInt((CharSequence) split[2]).intValue();
                            this.currentExpireDate[1] = Utilities.parseInt((CharSequence) split[1]).intValue();
                            this.currentExpireDate[2] = Utilities.parseInt((CharSequence) split[0]).intValue();
                            editTextBoldCursor.setText(str2);
                            break;
                        }
                    }
                    int[] iArr = this.currentExpireDate;
                    iArr[2] = 0;
                    iArr[1] = 0;
                    iArr[0] = 0;
                    i = R.string.PassportNoExpireDate;
                    str2 = LocaleController.getString(i);
                    editTextBoldCursor.setText(str2);
                case 3:
                    this.currentCitizeship = str2;
                    str2 = (String) this.languageMap.get(str2);
                    break;
                default:
                    editTextBoldCursor.setText(str2);
                    break;
            }
        }
        HashMap hashMap3 = this.fieldsErrors;
        if ((hashMap3 == null || (charSequence = (String) hashMap3.get(str)) == null) && ((hashMap2 = this.documentsErrors) == null || (charSequence = (String) hashMap2.get(str)) == null)) {
            return;
        }
        editTextBoldCursor.setErrorText(charSequence);
        this.errorsValues.put(str, editTextBoldCursor.getText().toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x035c, code lost:
        if (r12 == null) goto L239;
     */
    /* JADX WARN: Removed duplicated region for block: B:111:0x023a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0248 A[Catch: all -> 0x025d, TryCatch #2 {Exception -> 0x0288, blocks: (B:103:0x020d, B:126:0x0275, B:128:0x0278, B:131:0x0282, B:107:0x0226, B:113:0x023e, B:114:0x0242, B:116:0x0248, B:118:0x0254, B:122:0x0267, B:121:0x025f), top: B:312:0x020d }] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0278 A[Catch: Exception -> 0x0288, TryCatch #2 {Exception -> 0x0288, blocks: (B:103:0x020d, B:126:0x0275, B:128:0x0278, B:131:0x0282, B:107:0x0226, B:113:0x023e, B:114:0x0242, B:116:0x0248, B:118:0x0254, B:122:0x0267, B:121:0x025f), top: B:312:0x020d }] */
    /* JADX WARN: Removed duplicated region for block: B:180:0x032d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setTypeValue(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, String str, String str2, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, String str3, boolean z, int i) {
        boolean z2;
        TLRPC$TL_secureValue tLRPC$TL_secureValue;
        StringBuilder sb;
        StringBuilder sb2;
        TLRPC$TL_secureValue tLRPC$TL_secureValue2;
        TextDetailSecureCell textDetailSecureCell;
        Object obj;
        String[] strArr;
        String[] strArr2;
        int i2;
        JSONObject jSONObject;
        HashMap hashMap;
        String[] strArr3;
        String str4;
        String[] strArr4;
        Object obj2;
        Iterator<String> keys;
        int i3;
        String str5;
        String[] strArr5;
        JSONObject jSONObject2;
        Object obj3;
        char c;
        int i4;
        String str6;
        HashMap hashMap2;
        String string;
        int i5;
        String sb3;
        String string2;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3;
        HashMap hashMap3;
        int i6;
        TextDetailSecureCell textDetailSecureCell2;
        boolean z3;
        BufferedReader bufferedReader;
        String str7 = str2;
        String str8 = str3;
        TextDetailSecureCell textDetailSecureCell3 = (TextDetailSecureCell) this.typesViews.get(tLRPC$TL_secureRequiredType);
        if (textDetailSecureCell3 != null) {
            z2 = true;
        } else if (this.currentActivityType != 8) {
            return;
        } else {
            ArrayList arrayList = new ArrayList();
            if (tLRPC$TL_secureRequiredType2 != null) {
                arrayList.add(tLRPC$TL_secureRequiredType2);
            }
            LinearLayout linearLayout = this.linearLayout2;
            View childAt = linearLayout.getChildAt(linearLayout.getChildCount() - 6);
            if (childAt instanceof TextDetailSecureCell) {
                ((TextDetailSecureCell) childAt).setNeedDivider(true);
            }
            z2 = true;
            textDetailSecureCell3 = addField(getParentActivity(), tLRPC$TL_secureRequiredType, arrayList, true, true);
            updateManageVisibility();
        }
        TextDetailSecureCell textDetailSecureCell4 = textDetailSecureCell3;
        HashMap hashMap4 = (HashMap) this.typesValues.get(tLRPC$TL_secureRequiredType);
        HashMap hashMap5 = tLRPC$TL_secureRequiredType2 != null ? (HashMap) this.typesValues.get(tLRPC$TL_secureRequiredType2) : null;
        TLRPC$TL_secureValue valueByType = getValueByType(tLRPC$TL_secureRequiredType, z2);
        TLRPC$TL_secureValue valueByType2 = getValueByType(tLRPC$TL_secureRequiredType2, z2);
        if (str7 == null || this.languageMap != null) {
            tLRPC$TL_secureValue = valueByType;
            sb = null;
            this.languageMap = null;
        } else {
            this.languageMap = new HashMap();
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(ApplicationLoader.applicationContext.getResources().getAssets().open("countries.txt")));
            } catch (Exception e) {
                e = e;
                tLRPC$TL_secureValue = valueByType;
            }
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String[] split = readLine.split(";");
                tLRPC$TL_secureValue = valueByType;
                try {
                    this.languageMap.put(split[1], split[2]);
                    valueByType = tLRPC$TL_secureValue;
                } catch (Exception e2) {
                    e = e2;
                }
                e = e2;
                FileLog.e(e);
                sb = null;
            }
            tLRPC$TL_secureValue = valueByType;
            bufferedReader.close();
            sb = null;
        }
        if (str != null) {
            TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
            if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone) {
                PhoneFormat phoneFormat = PhoneFormat.getInstance();
                sb3 = phoneFormat.format("+" + str);
                textDetailSecureCell = textDetailSecureCell4;
                tLRPC$TL_secureValue2 = valueByType2;
            } else {
                textDetailSecureCell = textDetailSecureCell4;
                tLRPC$TL_secureValue2 = valueByType2;
                if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail) {
                    sb3 = str;
                } else {
                    sb3 = null;
                }
            }
        } else {
            if (this.currentActivityType == 8 || tLRPC$TL_secureRequiredType2 == null || (TextUtils.isEmpty(str3) && valueByType2 == null)) {
                sb2 = sb;
            } else {
                sb2 = new StringBuilder();
                if (i > 1) {
                    string2 = getTextForType(tLRPC$TL_secureRequiredType2.type);
                } else if (TextUtils.isEmpty(str3)) {
                    string2 = LocaleController.getString(R.string.PassportDocuments);
                }
                sb2.append(string2);
            }
            if (str7 == null && str8 == null) {
                textDetailSecureCell = textDetailSecureCell4;
                tLRPC$TL_secureValue2 = valueByType2;
            } else if (hashMap4 == null) {
                return;
            } else {
                hashMap4.clear();
                TLRPC$SecureValueType tLRPC$SecureValueType2 = tLRPC$TL_secureRequiredType.type;
                String str9 = "last_name";
                tLRPC$TL_secureValue2 = valueByType2;
                textDetailSecureCell = textDetailSecureCell4;
                if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                    int i7 = this.currentActivityType;
                    if ((i7 != 0 || z) && !(i7 == 8 && tLRPC$TL_secureRequiredType2 == null)) {
                        obj = "gender";
                        i5 = 8;
                        strArr2 = null;
                    } else {
                        obj = "gender";
                        i5 = 8;
                        strArr2 = new String[]{"first_name", "middle_name", "last_name", "first_name_native", "middle_name_native", "last_name_native", "birth_date", "gender", "country_code", "residence_country_code"};
                    }
                    strArr = (i7 == 0 || (i7 == i5 && tLRPC$TL_secureRequiredType2 != null)) ? new String[]{"document_no", "expiry_date"} : null;
                } else {
                    obj = "gender";
                    if (!(tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeAddress) || (((i2 = this.currentActivityType) != 0 || z) && !(i2 == 8 && tLRPC$TL_secureRequiredType2 == null))) {
                        strArr = null;
                        strArr2 = null;
                    } else {
                        strArr2 = new String[]{"street_line1", "street_line2", "post_code", "city", "state", "country_code"};
                        strArr = null;
                    }
                }
                if (strArr2 != null || strArr != null) {
                    StringBuilder sb4 = sb2;
                    String[] strArr6 = strArr;
                    JSONObject jSONObject3 = null;
                    int i8 = 0;
                    int i9 = 2;
                    String[] strArr7 = null;
                    while (i8 < i9) {
                        if (i8 == 0) {
                            if (str7 != null) {
                                try {
                                    jSONObject = new JSONObject(str7);
                                    hashMap = hashMap5;
                                    strArr3 = strArr2;
                                    if (strArr3 != null && jSONObject != null) {
                                        keys = jSONObject.keys();
                                        while (keys.hasNext()) {
                                            Iterator<String> it = keys;
                                            String next = keys.next();
                                            if (i8 == 0) {
                                                string = jSONObject.getString(next);
                                                hashMap2 = hashMap4;
                                            } else {
                                                hashMap2 = hashMap;
                                                string = jSONObject.getString(next);
                                            }
                                            hashMap2.put(next, string);
                                            keys = it;
                                        }
                                        i3 = 0;
                                        while (i3 < strArr3.length) {
                                            if (jSONObject.has(strArr3[i3])) {
                                                StringBuilder sb5 = sb4 == null ? new StringBuilder() : sb4;
                                                try {
                                                    String string3 = jSONObject.getString(strArr3[i3]);
                                                    if (string3 == null || TextUtils.isEmpty(string3)) {
                                                        str5 = str9;
                                                        strArr5 = strArr3;
                                                        jSONObject2 = jSONObject;
                                                    } else {
                                                        jSONObject2 = jSONObject;
                                                        if ("first_name_native".equals(strArr3[i3]) || "middle_name_native".equals(strArr3[i3]) || "last_name_native".equals(strArr3[i3])) {
                                                            str5 = str9;
                                                            strArr5 = strArr3;
                                                        } else {
                                                            if (sb5.length() > 0) {
                                                                if (!str9.equals(strArr3[i3]) && !"last_name_native".equals(strArr3[i3]) && !"middle_name".equals(strArr3[i3]) && !"middle_name_native".equals(strArr3[i3])) {
                                                                    str6 = ", ";
                                                                    sb5.append(str6);
                                                                }
                                                                str6 = " ";
                                                                sb5.append(str6);
                                                            }
                                                            String str10 = strArr3[i3];
                                                            str5 = str9;
                                                            int hashCode = str10.hashCode();
                                                            strArr5 = strArr3;
                                                            if (hashCode == -2006252145) {
                                                                obj3 = obj;
                                                                if (str10.equals("residence_country_code")) {
                                                                    c = 1;
                                                                    if (c != 0) {
                                                                    }
                                                                    string3 = (String) this.languageMap.get(string3);
                                                                }
                                                                c = 65535;
                                                                if (c != 0) {
                                                                }
                                                                string3 = (String) this.languageMap.get(string3);
                                                            } else if (hashCode == -1249512767) {
                                                                obj3 = obj;
                                                                if (str10.equals(obj3)) {
                                                                    c = 2;
                                                                    if (c != 0) {
                                                                    }
                                                                    string3 = (String) this.languageMap.get(string3);
                                                                }
                                                                c = 65535;
                                                                if (c != 0) {
                                                                }
                                                                string3 = (String) this.languageMap.get(string3);
                                                            } else if (hashCode == 1481071862 && str10.equals("country_code")) {
                                                                obj3 = obj;
                                                                c = 0;
                                                                if (c != 0 || c == 1) {
                                                                    string3 = (String) this.languageMap.get(string3);
                                                                } else if (c != 2) {
                                                                    sb5.append(string3);
                                                                } else {
                                                                    if ("male".equals(string3)) {
                                                                        i4 = R.string.PassportMale;
                                                                    } else if ("female".equals(string3)) {
                                                                        i4 = R.string.PassportFemale;
                                                                    }
                                                                    sb5.append(LocaleController.getString(i4));
                                                                }
                                                            } else {
                                                                obj3 = obj;
                                                                c = 65535;
                                                                if (c != 0) {
                                                                }
                                                                string3 = (String) this.languageMap.get(string3);
                                                            }
                                                            sb4 = sb5;
                                                        }
                                                    }
                                                    obj3 = obj;
                                                    sb4 = sb5;
                                                } catch (Exception unused) {
                                                    sb2 = sb5;
                                                }
                                            } else {
                                                str5 = str9;
                                                strArr5 = strArr3;
                                                jSONObject2 = jSONObject;
                                                obj3 = obj;
                                            }
                                            i3++;
                                            obj = obj3;
                                            jSONObject = jSONObject2;
                                            str9 = str5;
                                            strArr3 = strArr5;
                                        }
                                    }
                                    str4 = str9;
                                    strArr4 = strArr3;
                                    JSONObject jSONObject4 = jSONObject;
                                    i9 = 2;
                                    obj2 = obj;
                                    jSONObject3 = jSONObject4;
                                } catch (Exception unused2) {
                                }
                            }
                            jSONObject = jSONObject3;
                            String[] strArr8 = strArr7;
                            hashMap = hashMap5;
                            strArr3 = strArr8;
                            if (strArr3 != null) {
                                keys = jSONObject.keys();
                                while (keys.hasNext()) {
                                }
                                i3 = 0;
                                while (i3 < strArr3.length) {
                                }
                            }
                            str4 = str9;
                            strArr4 = strArr3;
                            JSONObject jSONObject42 = jSONObject;
                            i9 = 2;
                            obj2 = obj;
                            jSONObject3 = jSONObject42;
                        } else if (hashMap5 == null) {
                            str4 = str9;
                            strArr4 = strArr7;
                            i9 = 2;
                            obj2 = obj;
                            hashMap = hashMap5;
                        } else {
                            if (str8 != null) {
                                jSONObject = new JSONObject(str8);
                                hashMap = hashMap5;
                                strArr3 = strArr6;
                                if (strArr3 != null) {
                                }
                                str4 = str9;
                                strArr4 = strArr3;
                                JSONObject jSONObject422 = jSONObject;
                                i9 = 2;
                                obj2 = obj;
                                jSONObject3 = jSONObject422;
                            }
                            jSONObject = jSONObject3;
                            String[] strArr82 = strArr7;
                            hashMap = hashMap5;
                            strArr3 = strArr82;
                            if (strArr3 != null) {
                            }
                            str4 = str9;
                            strArr4 = strArr3;
                            JSONObject jSONObject4222 = jSONObject;
                            i9 = 2;
                            obj2 = obj;
                            jSONObject3 = jSONObject4222;
                        }
                        i8++;
                        str7 = str2;
                        str8 = str3;
                        obj = obj2;
                        hashMap5 = hashMap;
                        str9 = str4;
                        strArr7 = strArr4;
                    }
                    sb2 = sb4;
                }
            }
            if (sb2 != null) {
                sb3 = sb2.toString();
            }
            sb3 = null;
        }
        if (z) {
            tLRPC$TL_secureRequiredType3 = tLRPC$TL_secureRequiredType;
            hashMap3 = null;
        } else {
            tLRPC$TL_secureRequiredType3 = tLRPC$TL_secureRequiredType;
            hashMap3 = (HashMap) this.errorsMap.get(getNameForType(tLRPC$TL_secureRequiredType3.type));
        }
        HashMap hashMap6 = tLRPC$TL_secureRequiredType2 != null ? (HashMap) this.errorsMap.get(getNameForType(tLRPC$TL_secureRequiredType2.type)) : null;
        if ((hashMap3 == null || hashMap3.size() <= 0) && (hashMap6 == null || hashMap6.size() <= 0)) {
            TLRPC$SecureValueType tLRPC$SecureValueType3 = tLRPC$TL_secureRequiredType3.type;
            if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                if (TextUtils.isEmpty(sb3)) {
                    if (tLRPC$TL_secureRequiredType2 == null) {
                        i6 = R.string.PassportPersonalDetailsInfo;
                    } else {
                        if (this.currentActivityType != 8) {
                            if (i == 1) {
                                TLRPC$SecureValueType tLRPC$SecureValueType4 = tLRPC$TL_secureRequiredType2.type;
                                if (tLRPC$SecureValueType4 instanceof TLRPC$TL_secureValueTypePassport) {
                                    i6 = R.string.PassportIdentityPassport;
                                } else if (tLRPC$SecureValueType4 instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                                    i6 = R.string.PassportIdentityInternalPassport;
                                } else if (tLRPC$SecureValueType4 instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                                    i6 = R.string.PassportIdentityDriverLicence;
                                } else if (tLRPC$SecureValueType4 instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                                    i6 = R.string.PassportIdentityID;
                                }
                            } else {
                                i6 = R.string.PassportIdentityDocumentInfo;
                            }
                        }
                        i6 = R.string.PassportDocuments;
                    }
                    sb3 = LocaleController.getString(i6);
                    textDetailSecureCell2 = textDetailSecureCell;
                    z3 = false;
                }
                textDetailSecureCell2 = textDetailSecureCell;
                z3 = false;
            } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeAddress) {
                if (TextUtils.isEmpty(sb3)) {
                    if (tLRPC$TL_secureRequiredType2 == null) {
                        i6 = R.string.PassportAddressNoUploadInfo;
                        sb3 = LocaleController.getString(i6);
                        textDetailSecureCell2 = textDetailSecureCell;
                        z3 = false;
                    } else {
                        if (this.currentActivityType != 8) {
                            if (i == 1) {
                                TLRPC$SecureValueType tLRPC$SecureValueType5 = tLRPC$TL_secureRequiredType2.type;
                                if (tLRPC$SecureValueType5 instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                                    i6 = R.string.PassportAddAgreementInfo;
                                } else if (tLRPC$SecureValueType5 instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                                    i6 = R.string.PassportAddBillInfo;
                                } else if (tLRPC$SecureValueType5 instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                                    i6 = R.string.PassportAddPassportRegistrationInfo;
                                } else if (tLRPC$SecureValueType5 instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                                    i6 = R.string.PassportAddTemporaryRegistrationInfo;
                                } else {
                                    if (tLRPC$SecureValueType5 instanceof TLRPC$TL_secureValueTypeBankStatement) {
                                        i6 = R.string.PassportAddBankInfo;
                                    }
                                    textDetailSecureCell2 = textDetailSecureCell;
                                    z3 = false;
                                }
                            } else {
                                i6 = R.string.PassportAddressInfo;
                            }
                            sb3 = LocaleController.getString(i6);
                            textDetailSecureCell2 = textDetailSecureCell;
                            z3 = false;
                        }
                        i6 = R.string.PassportDocuments;
                        sb3 = LocaleController.getString(i6);
                        textDetailSecureCell2 = textDetailSecureCell;
                        z3 = false;
                    }
                }
                textDetailSecureCell2 = textDetailSecureCell;
                z3 = false;
            } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypePhone) {
                if (TextUtils.isEmpty(sb3)) {
                    i6 = R.string.PassportPhoneInfo;
                    sb3 = LocaleController.getString(i6);
                }
                textDetailSecureCell2 = textDetailSecureCell;
                z3 = false;
            } else {
                if ((tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeEmail) && TextUtils.isEmpty(sb3)) {
                    i6 = R.string.PassportEmailInfo;
                    sb3 = LocaleController.getString(i6);
                }
                textDetailSecureCell2 = textDetailSecureCell;
                z3 = false;
            }
        } else {
            String str11 = !z ? (String) this.mainErrorsMap.get(getNameForType(tLRPC$TL_secureRequiredType3.type)) : null;
            sb3 = str11 == null ? (String) this.mainErrorsMap.get(getNameForType(tLRPC$TL_secureRequiredType2.type)) : str11;
            textDetailSecureCell2 = textDetailSecureCell;
            z3 = true;
        }
        textDetailSecureCell2.setValue(sb3);
        textDetailSecureCell2.valueTextView.setTextColor(Theme.getColor(z3 ? Theme.key_text_RedRegular : Theme.key_windowBackgroundWhiteGrayText2));
        textDetailSecureCell2.setChecked((z3 || this.currentActivityType == 8 || ((!z || tLRPC$TL_secureRequiredType2 == null) && (z || tLRPC$TL_secureValue == null)) || (tLRPC$TL_secureRequiredType2 != null && tLRPC$TL_secureValue2 == null)) ? false : true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAlertWithText(String str, String str2) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        builder.setTitle(str);
        builder.setMessage(str2);
        showDialog(builder.create());
    }

    private void showAttachmentError() {
        if (getParentActivity() == null) {
            return;
        }
        Toast.makeText(getParentActivity(), LocaleController.getString(R.string.UnsupportedAttachment), 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEditDoneProgress(boolean z, final boolean z2) {
        AnimatorSet animatorSet;
        AnimatorListenerAdapter animatorListenerAdapter;
        AnimatorSet animatorSet2 = this.doneItemAnimation;
        if (animatorSet2 != null) {
            animatorSet2.cancel();
        }
        if (z && this.doneItem != null) {
            this.doneItemAnimation = new AnimatorSet();
            if (z2) {
                this.progressView.setVisibility(0);
                this.doneItem.setEnabled(false);
                AnimatorSet animatorSet3 = this.doneItemAnimation;
                View contentView = this.doneItem.getContentView();
                Property property = View.SCALE_X;
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(contentView, property, 0.1f);
                View contentView2 = this.doneItem.getContentView();
                Property property2 = View.SCALE_Y;
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(contentView2, property2, 0.1f);
                View contentView3 = this.doneItem.getContentView();
                Property property3 = View.ALPHA;
                animatorSet3.playTogether(ofFloat, ofFloat2, ObjectAnimator.ofFloat(contentView3, property3, 0.0f), ObjectAnimator.ofFloat(this.progressView, property, 1.0f), ObjectAnimator.ofFloat(this.progressView, property2, 1.0f), ObjectAnimator.ofFloat(this.progressView, property3, 1.0f));
            } else {
                this.doneItem.getContentView().setVisibility(0);
                this.doneItem.setEnabled(true);
                AnimatorSet animatorSet4 = this.doneItemAnimation;
                ContextProgressView contextProgressView = this.progressView;
                Property property4 = View.SCALE_X;
                ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(contextProgressView, property4, 0.1f);
                ContextProgressView contextProgressView2 = this.progressView;
                Property property5 = View.SCALE_Y;
                ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(contextProgressView2, property5, 0.1f);
                ContextProgressView contextProgressView3 = this.progressView;
                Property property6 = View.ALPHA;
                animatorSet4.playTogether(ofFloat3, ofFloat4, ObjectAnimator.ofFloat(contextProgressView3, property6, 0.0f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), property4, 1.0f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), property5, 1.0f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), property6, 1.0f));
            }
            animatorSet = this.doneItemAnimation;
            animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: org.telegram.ui.PassportActivity.21
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (PassportActivity.this.doneItemAnimation == null || !PassportActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    PassportActivity.this.doneItemAnimation = null;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (PassportActivity.this.doneItemAnimation == null || !PassportActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    if (z2) {
                        PassportActivity.this.doneItem.getContentView().setVisibility(4);
                    } else {
                        PassportActivity.this.progressView.setVisibility(4);
                    }
                }
            };
        } else if (this.acceptTextView == null) {
            return;
        } else {
            this.doneItemAnimation = new AnimatorSet();
            if (z2) {
                this.progressViewButton.setVisibility(0);
                this.bottomLayout.setEnabled(false);
                AnimatorSet animatorSet5 = this.doneItemAnimation;
                TextView textView = this.acceptTextView;
                Property property7 = View.SCALE_X;
                ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(textView, property7, 0.1f);
                TextView textView2 = this.acceptTextView;
                Property property8 = View.SCALE_Y;
                ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(textView2, property8, 0.1f);
                TextView textView3 = this.acceptTextView;
                Property property9 = View.ALPHA;
                animatorSet5.playTogether(ofFloat5, ofFloat6, ObjectAnimator.ofFloat(textView3, property9, 0.0f), ObjectAnimator.ofFloat(this.progressViewButton, property7, 1.0f), ObjectAnimator.ofFloat(this.progressViewButton, property8, 1.0f), ObjectAnimator.ofFloat(this.progressViewButton, property9, 1.0f));
            } else {
                this.acceptTextView.setVisibility(0);
                this.bottomLayout.setEnabled(true);
                AnimatorSet animatorSet6 = this.doneItemAnimation;
                ContextProgressView contextProgressView4 = this.progressViewButton;
                Property property10 = View.SCALE_X;
                ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat(contextProgressView4, property10, 0.1f);
                ContextProgressView contextProgressView5 = this.progressViewButton;
                Property property11 = View.SCALE_Y;
                ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat(contextProgressView5, property11, 0.1f);
                ContextProgressView contextProgressView6 = this.progressViewButton;
                Property property12 = View.ALPHA;
                animatorSet6.playTogether(ofFloat7, ofFloat8, ObjectAnimator.ofFloat(contextProgressView6, property12, 0.0f), ObjectAnimator.ofFloat(this.acceptTextView, property10, 1.0f), ObjectAnimator.ofFloat(this.acceptTextView, property11, 1.0f), ObjectAnimator.ofFloat(this.acceptTextView, property12, 1.0f));
            }
            animatorSet = this.doneItemAnimation;
            animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: org.telegram.ui.PassportActivity.22
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (PassportActivity.this.doneItemAnimation == null || !PassportActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    PassportActivity.this.doneItemAnimation = null;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (PassportActivity.this.doneItemAnimation == null || !PassportActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    if (z2) {
                        PassportActivity.this.acceptTextView.setVisibility(4);
                    } else {
                        PassportActivity.this.progressViewButton.setVisibility(4);
                    }
                }
            };
        }
        animatorSet.addListener(animatorListenerAdapter);
        this.doneItemAnimation.setDuration(150L);
        this.doneItemAnimation.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startPhoneVerification(boolean z, final String str, Runnable runnable, ErrorRunnable errorRunnable, final PassportActivityDelegate passportActivityDelegate) {
        boolean z2;
        int checkSelfPermission;
        boolean shouldShowRequestPermissionRationale;
        TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
        boolean z3 = (telephonyManager.getSimState() == 1 || telephonyManager.getPhoneType() == 0) ? false : true;
        if (getParentActivity() == null || Build.VERSION.SDK_INT < 23 || !z3) {
            z2 = true;
        } else {
            checkSelfPermission = getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE");
            z2 = checkSelfPermission == 0;
            if (z) {
                this.permissionsItems.clear();
                if (!z2) {
                    this.permissionsItems.add("android.permission.READ_PHONE_STATE");
                }
                if (!this.permissionsItems.isEmpty()) {
                    shouldShowRequestPermissionRationale = getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE");
                    if (shouldShowRequestPermissionRationale) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                        builder.setTitle(LocaleController.getString(R.string.AppName));
                        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
                        builder.setMessage(LocaleController.getString(R.string.AllowReadCall));
                        this.permissionsDialog = showDialog(builder.create());
                    } else {
                        getParentActivity().requestPermissions((String[]) this.permissionsItems.toArray(new String[0]), 6);
                    }
                    this.pendingPhone = str;
                    this.pendingErrorRunnable = errorRunnable;
                    this.pendingFinishRunnable = runnable;
                    this.pendingDelegate = passportActivityDelegate;
                    return;
                }
            }
        }
        final TLRPC$TL_account_sendVerifyPhoneCode tLRPC$TL_account_sendVerifyPhoneCode = new TLRPC$TL_account_sendVerifyPhoneCode();
        tLRPC$TL_account_sendVerifyPhoneCode.phone_number = str;
        TLRPC$TL_codeSettings tLRPC$TL_codeSettings = new TLRPC$TL_codeSettings();
        tLRPC$TL_account_sendVerifyPhoneCode.settings = tLRPC$TL_codeSettings;
        tLRPC$TL_codeSettings.allow_flashcall = z3 && z2;
        tLRPC$TL_codeSettings.allow_app_hash = PushListenerController.GooglePushListenerServiceProvider.INSTANCE.hasServices();
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        boolean z4 = tLRPC$TL_account_sendVerifyPhoneCode.settings.allow_app_hash;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        (z4 ? edit.putString("sms_hash", BuildVars.getSmsHash()) : edit.remove("sms_hash")).commit();
        if (tLRPC$TL_account_sendVerifyPhoneCode.settings.allow_flashcall) {
            try {
                String line1Number = telephonyManager.getLine1Number();
                if (TextUtils.isEmpty(line1Number)) {
                    TLRPC$TL_codeSettings tLRPC$TL_codeSettings2 = tLRPC$TL_account_sendVerifyPhoneCode.settings;
                    tLRPC$TL_codeSettings2.unknown_number = true;
                    tLRPC$TL_codeSettings2.current_number = false;
                } else {
                    tLRPC$TL_account_sendVerifyPhoneCode.settings.current_number = PhoneNumberUtils.compare(str, line1Number);
                    TLRPC$TL_codeSettings tLRPC$TL_codeSettings3 = tLRPC$TL_account_sendVerifyPhoneCode.settings;
                    if (!tLRPC$TL_codeSettings3.current_number) {
                        tLRPC$TL_codeSettings3.allow_flashcall = false;
                    }
                }
            } catch (Exception e) {
                tLRPC$TL_account_sendVerifyPhoneCode.settings.allow_flashcall = false;
                FileLog.e(e);
            }
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_sendVerifyPhoneCode, new RequestDelegate() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda54
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PassportActivity.this.lambda$startPhoneVerification$66(str, passportActivityDelegate, tLRPC$TL_account_sendVerifyPhoneCode, tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    private void updateInterfaceStringsForDocumentType() {
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType = this.currentDocumentsType;
        if (tLRPC$TL_secureRequiredType != null) {
            this.actionBar.setTitle(getTextForType(tLRPC$TL_secureRequiredType.type));
        } else {
            this.actionBar.setTitle(LocaleController.getString(R.string.PassportPersonal));
        }
        updateUploadText(2);
        updateUploadText(3);
        updateUploadText(1);
        updateUploadText(4);
    }

    private void updateManageVisibility() {
        View view;
        if (this.currentForm.values.isEmpty()) {
            this.emptyLayout.setVisibility(0);
            this.sectionCell.setVisibility(8);
            this.headerCell.setVisibility(8);
            this.addDocumentCell.setVisibility(8);
            this.deletePassportCell.setVisibility(8);
            view = this.addDocumentSectionCell;
        } else {
            this.emptyLayout.setVisibility(8);
            this.sectionCell.setVisibility(0);
            this.headerCell.setVisibility(0);
            this.deletePassportCell.setVisibility(0);
            this.addDocumentSectionCell.setVisibility(0);
            if (hasUnfilledValues()) {
                this.addDocumentCell.setVisibility(0);
                return;
            }
            view = this.addDocumentCell;
        }
        view.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePasswordInterface() {
        EditTextBoldCursor editTextBoldCursor;
        String string;
        ImageView imageView = this.noPasswordImageView;
        if (imageView == null) {
            return;
        }
        TLRPC$account_Password tLRPC$account_Password = this.currentPassword;
        if (tLRPC$account_Password == null || this.usingSavedPassword != 0) {
            imageView.setVisibility(8);
            this.noPasswordTextView.setVisibility(8);
            this.noPasswordSetTextView.setVisibility(8);
            this.passwordAvatarContainer.setVisibility(8);
            this.inputFieldContainers[0].setVisibility(8);
            this.doneItem.setVisibility(8);
            this.passwordForgotButton.setVisibility(8);
            this.passwordInfoRequestTextView.setVisibility(8);
            this.passwordRequestTextView.setVisibility(8);
            this.emptyView.setVisibility(0);
        } else if (!tLRPC$account_Password.has_password) {
            this.passwordRequestTextView.setVisibility(0);
            this.noPasswordImageView.setVisibility(0);
            this.noPasswordTextView.setVisibility(0);
            this.noPasswordSetTextView.setVisibility(0);
            this.passwordAvatarContainer.setVisibility(8);
            this.inputFieldContainers[0].setVisibility(8);
            this.doneItem.setVisibility(8);
            this.passwordForgotButton.setVisibility(8);
            this.passwordInfoRequestTextView.setVisibility(8);
            this.passwordRequestTextView.setLayoutParams(LayoutHelper.createLinear(-1, -2, 0.0f, 25.0f, 0.0f, 0.0f));
            this.emptyView.setVisibility(8);
        } else {
            this.passwordRequestTextView.setVisibility(0);
            this.noPasswordImageView.setVisibility(8);
            this.noPasswordTextView.setVisibility(8);
            this.noPasswordSetTextView.setVisibility(8);
            this.emptyView.setVisibility(8);
            this.passwordAvatarContainer.setVisibility(0);
            this.inputFieldContainers[0].setVisibility(0);
            this.doneItem.setVisibility(0);
            this.passwordForgotButton.setVisibility(0);
            this.passwordInfoRequestTextView.setVisibility(0);
            this.passwordRequestTextView.setLayoutParams(LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 0.0f));
            if (this.inputFields != null) {
                TLRPC$account_Password tLRPC$account_Password2 = this.currentPassword;
                if (tLRPC$account_Password2 == null || TextUtils.isEmpty(tLRPC$account_Password2.hint)) {
                    editTextBoldCursor = this.inputFields[0];
                    string = LocaleController.getString(R.string.LoginPassword);
                } else {
                    editTextBoldCursor = this.inputFields[0];
                    string = this.currentPassword.hint;
                }
                editTextBoldCursor.setHint(string);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x002e, code lost:
        if (r6.selfieDocument != null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0060, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_secureValueTypeDriverLicense) == false) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x008c, code lost:
        if (r6.frontDocument == null) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00b8, code lost:
        if (r6.reverseDocument != null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00ba, code lost:
        r1 = 8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateUploadText(int i) {
        TextDetailSettingsCell textDetailSettingsCell;
        TLRPC$SecureValueType tLRPC$SecureValueType;
        String string;
        int i2;
        TextSettingsCell textSettingsCell;
        int i3;
        boolean z = true;
        int i4 = 0;
        if (i != 0) {
            if (i == 1) {
                textDetailSettingsCell = this.uploadSelfieCell;
                if (textDetailSettingsCell == null) {
                    return;
                }
            } else if (i == 4) {
                if (this.uploadTranslationCell == null) {
                    return;
                }
                if (this.translationDocuments.size() >= 1) {
                    textSettingsCell = this.uploadTranslationCell;
                    i3 = R.string.PassportUploadAdditinalDocument;
                } else {
                    textSettingsCell = this.uploadTranslationCell;
                    i3 = R.string.PassportUploadDocument;
                }
            } else if (i == 2) {
                TextDetailSettingsCell textDetailSettingsCell2 = this.uploadFrontCell;
                if (textDetailSettingsCell2 == null) {
                    return;
                }
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType = this.currentDocumentsType;
                if (tLRPC$TL_secureRequiredType != null) {
                    if (!tLRPC$TL_secureRequiredType.selfie_required) {
                        TLRPC$SecureValueType tLRPC$SecureValueType2 = tLRPC$TL_secureRequiredType.type;
                        if (!(tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeIdentityCard)) {
                        }
                    }
                    tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
                    if (!(tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport)) {
                        string = LocaleController.getString(R.string.PassportMainPage);
                        i2 = R.string.PassportMainPageInfo;
                    } else {
                        string = LocaleController.getString(R.string.PassportFrontSide);
                        i2 = R.string.PassportFrontSideInfo;
                    }
                    textDetailSettingsCell2.setTextAndValue(string, LocaleController.getString(i2), z);
                    textDetailSettingsCell = this.uploadFrontCell;
                }
                z = false;
                tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
                if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) {
                }
                string = LocaleController.getString(R.string.PassportMainPage);
                i2 = R.string.PassportMainPageInfo;
                textDetailSettingsCell2.setTextAndValue(string, LocaleController.getString(i2), z);
                textDetailSettingsCell = this.uploadFrontCell;
            } else if (i != 3 || this.uploadReverseCell == null) {
                return;
            } else {
                TLRPC$SecureValueType tLRPC$SecureValueType3 = this.currentDocumentsType.type;
                if (!(tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeIdentityCard) && !(tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeDriverLicense)) {
                    this.reverseLayout.setVisibility(8);
                    this.uploadReverseCell.setVisibility(8);
                    return;
                }
                this.reverseLayout.setVisibility(0);
                textDetailSettingsCell = this.uploadReverseCell;
            }
            textDetailSettingsCell.setVisibility(i4);
            return;
        } else if (this.uploadDocumentCell == null) {
            return;
        } else {
            if (this.documents.size() >= 1) {
                textSettingsCell = this.uploadDocumentCell;
                i3 = R.string.PassportUploadAdditinalDocument;
            } else {
                textSettingsCell = this.uploadDocumentCell;
                i3 = R.string.PassportUploadDocument;
            }
        }
        textSettingsCell.setText(LocaleController.getString(i3), false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        ChatAttachAlert chatAttachAlert;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new 3());
        if (this.currentActivityType == 7) {
            ScrollView scrollView = new ScrollView(context) { // from class: org.telegram.ui.PassportActivity.4
                @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i, int i2) {
                    PassportActivity.this.scrollHeight = View.MeasureSpec.getSize(i2) - AndroidUtilities.dp(30.0f);
                    super.onMeasure(i, i2);
                }

                @Override // android.widget.ScrollView, android.view.ViewGroup
                protected boolean onRequestFocusInDescendants(int i, Rect rect) {
                    return false;
                }

                @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
                public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
                    if (PassportActivity.this.currentViewNum == 1 || PassportActivity.this.currentViewNum == 2 || PassportActivity.this.currentViewNum == 4) {
                        rect.bottom += AndroidUtilities.dp(40.0f);
                    }
                    return super.requestChildRectangleOnScreen(view, rect, z);
                }
            };
            this.scrollView = scrollView;
            this.fragmentView = scrollView;
            scrollView.setFillViewport(true);
            AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor(Theme.key_actionBarDefault));
        } else {
            FrameLayout frameLayout = new FrameLayout(context);
            this.fragmentView = frameLayout;
            frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
            ScrollView scrollView2 = new ScrollView(context) { // from class: org.telegram.ui.PassportActivity.5
                @Override // android.widget.ScrollView, android.view.ViewGroup
                protected boolean onRequestFocusInDescendants(int i, Rect rect) {
                    return false;
                }

                @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
                public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
                    rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
                    rect.top += AndroidUtilities.dp(20.0f);
                    rect.bottom += AndroidUtilities.dp(50.0f);
                    return super.requestChildRectangleOnScreen(view, rect, z);
                }
            };
            this.scrollView = scrollView2;
            scrollView2.setFillViewport(true);
            AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor(Theme.key_actionBarDefault));
            frameLayout.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.currentActivityType == 0 ? 48.0f : 0.0f));
            LinearLayout linearLayout = new LinearLayout(context);
            this.linearLayout2 = linearLayout;
            linearLayout.setOrientation(1);
            this.scrollView.addView(this.linearLayout2, new FrameLayout.LayoutParams(-1, -2));
        }
        int i = this.currentActivityType;
        if (i != 0 && i != 8) {
            this.doneItem = this.actionBar.createMenu().addItemWithWidth(2, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f), LocaleController.getString(R.string.Done));
            ContextProgressView contextProgressView = new ContextProgressView(context, 1);
            this.progressView = contextProgressView;
            contextProgressView.setAlpha(0.0f);
            this.progressView.setScaleX(0.1f);
            this.progressView.setScaleY(0.1f);
            this.progressView.setVisibility(4);
            this.doneItem.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0f));
            int i2 = this.currentActivityType;
            if ((i2 == 1 || i2 == 2) && (chatAttachAlert = this.chatAttachAlert) != null) {
                try {
                    if (chatAttachAlert.isShowing()) {
                        this.chatAttachAlert.dismiss();
                    }
                } catch (Exception unused) {
                }
                this.chatAttachAlert.onDestroy();
                this.chatAttachAlert = null;
            }
        }
        int i3 = this.currentActivityType;
        if (i3 == 5) {
            createPasswordInterface(context);
        } else if (i3 == 0) {
            createRequestInterface(context);
        } else {
            if (i3 == 1) {
                createIdentityInterface(context);
            } else if (i3 == 2) {
                createAddressInterface(context);
            } else if (i3 == 3) {
                createPhoneInterface(context);
            } else if (i3 == 4) {
                createEmailInterface(context);
            } else if (i3 == 6) {
                createEmailVerificationInterface(context);
            } else if (i3 == 7) {
                createPhoneVerificationInterface(context);
            } else if (i3 == 8) {
                createManageInterface(context);
            }
            fillInitialValues();
        }
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        EditTextBoldCursor editTextBoldCursor;
        HashMap hashMap;
        String str;
        SecureDocumentCell secureDocumentCell;
        ActionBarMenuItem actionBarMenuItem;
        if (i != NotificationCenter.fileUploaded) {
            if (i != NotificationCenter.fileUploadFailed && i == NotificationCenter.twoStepPasswordChanged) {
                if (objArr == null || objArr.length <= 0) {
                    this.currentPassword = null;
                    loadPasswordInfo();
                } else {
                    Object obj = objArr[7];
                    if (obj != null && (editTextBoldCursor = this.inputFields[0]) != null) {
                        editTextBoldCursor.setText((String) obj);
                    }
                    if (objArr[6] == null) {
                        TLRPC$TL_account_password tLRPC$TL_account_password = new TLRPC$TL_account_password();
                        this.currentPassword = tLRPC$TL_account_password;
                        tLRPC$TL_account_password.current_algo = (TLRPC$PasswordKdfAlgo) objArr[1];
                        tLRPC$TL_account_password.new_secure_algo = (TLRPC$SecurePasswordKdfAlgo) objArr[2];
                        tLRPC$TL_account_password.secure_random = (byte[]) objArr[3];
                        tLRPC$TL_account_password.has_recovery = !TextUtils.isEmpty((String) objArr[4]);
                        TLRPC$account_Password tLRPC$account_Password = this.currentPassword;
                        tLRPC$account_Password.hint = (String) objArr[5];
                        tLRPC$account_Password.srp_id = -1L;
                        byte[] bArr = new byte[256];
                        tLRPC$account_Password.srp_B = bArr;
                        Utilities.random.nextBytes(bArr);
                        EditTextBoldCursor editTextBoldCursor2 = this.inputFields[0];
                        if (editTextBoldCursor2 != null && editTextBoldCursor2.length() > 0) {
                            this.usingSavedPassword = 2;
                        }
                    }
                }
                updatePasswordInterface();
                return;
            }
            return;
        }
        String str2 = (String) objArr[0];
        SecureDocument secureDocument = (SecureDocument) this.uploadingDocuments.get(str2);
        if (secureDocument != null) {
            secureDocument.inputFile = (TLRPC$TL_inputFile) objArr[1];
            this.uploadingDocuments.remove(str2);
            if (this.uploadingDocuments.isEmpty() && (actionBarMenuItem = this.doneItem) != null) {
                actionBarMenuItem.setEnabled(true);
                this.doneItem.setAlpha(1.0f);
            }
            HashMap hashMap2 = this.documentsCells;
            if (hashMap2 != null && (secureDocumentCell = (SecureDocumentCell) hashMap2.get(secureDocument)) != null) {
                secureDocumentCell.updateButtonState(true);
            }
            HashMap hashMap3 = this.errorsValues;
            if (hashMap3 != null && hashMap3.containsKey("error_document_all")) {
                this.errorsValues.remove("error_document_all");
                checkTopErrorCell(false);
            }
            int i3 = secureDocument.type;
            if (i3 == 0) {
                if (this.bottomCell != null && !TextUtils.isEmpty(this.noAllDocumentsErrorText)) {
                    this.bottomCell.setText(this.noAllDocumentsErrorText);
                }
                hashMap = this.errorsValues;
                str = "files_all";
            } else if (i3 != 4) {
                return;
            } else {
                if (this.bottomCellTranslation != null && !TextUtils.isEmpty(this.noAllTranslationErrorText)) {
                    this.bottomCellTranslation.setText(this.noAllTranslationErrorText);
                }
                hashMap = this.errorsValues;
                str = "translation_all";
            }
            hashMap.remove(str);
        }
    }

    public void didSelectFiles(ArrayList arrayList, String str, boolean z, int i, long j, boolean z2) {
        ArrayList arrayList2 = new ArrayList();
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
            sendingMediaInfo.path = (String) arrayList.get(i2);
            arrayList2.add(sendingMediaInfo);
        }
        processSelectedFiles(arrayList2);
    }

    public void didSelectPhotos(ArrayList arrayList, boolean z, int i) {
        processSelectedFiles(arrayList);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void dismissCurrentDialog() {
        ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (chatAttachAlert == null || this.visibleDialog != chatAttachAlert) {
            super.dismissCurrentDialog();
            return;
        }
        chatAttachAlert.getPhotoLayout().closeCamera(false);
        this.chatAttachAlert.dismissInternal();
        this.chatAttachAlert.getPhotoLayout().hideCamera(true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean dismissDialogOnPause(Dialog dialog) {
        return dialog != this.chatAttachAlert && super.dismissDialogOnPause(dialog);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        View view = this.extraBackgroundView;
        int i3 = ThemeDescription.FLAG_BACKGROUND;
        int i4 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(view, i3, null, null, null, null, i4));
        if (this.extraBackgroundView2 != null) {
            arrayList.add(new ThemeDescription(this.extraBackgroundView2, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i4));
        }
        for (int i5 = 0; i5 < this.dividers.size(); i5++) {
            arrayList.add(new ThemeDescription((View) this.dividers.get(i5), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_divider));
        }
        for (Map.Entry entry : this.documentsCells.entrySet()) {
            SecureDocumentCell secureDocumentCell = (SecureDocumentCell) entry.getValue();
            arrayList.add(new ThemeDescription(secureDocumentCell, ThemeDescription.FLAG_SELECTORWHITE, new Class[]{SecureDocumentCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
            arrayList.add(new ThemeDescription(secureDocumentCell, 0, new Class[]{SecureDocumentCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription(secureDocumentCell, 0, new Class[]{SecureDocumentCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        }
        int i6 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_SELECTORWHITE, new Class[]{TextDetailSettingsCell.class}, null, null, null, i6));
        int i7 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        int i8 = Theme.key_windowBackgroundWhiteGrayText2;
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i8));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_SELECTORWHITE, new Class[]{TextSettingsCell.class}, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        int i9 = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_SELECTORWHITE, new Class[]{TextDetailSecureCell.class}, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextDetailSecureCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextDetailSecureCell.class}, null, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextDetailSecureCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i8));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{TextDetailSecureCell.class}, new String[]{"checkImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_featuredStickers_addedIcon));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class}, null, null, null, i6));
        int i10 = Theme.key_windowBackgroundWhiteBlueHeader;
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i10));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        if (this.inputFields != null) {
            for (int i11 = 0; i11 < this.inputFields.length; i11++) {
                arrayList.add(new ThemeDescription((View) this.inputFields[i11].getParent(), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(this.inputFields[i11], ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(this.inputFields[i11], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
                arrayList.add(new ThemeDescription(this.inputFields[i11], ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader));
                arrayList.add(new ThemeDescription(this.inputFields[i11], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
                arrayList.add(new ThemeDescription(this.inputFields[i11], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
                arrayList.add(new ThemeDescription(this.inputFields[i11], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_text_RedRegular));
            }
        } else {
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i7));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, i10));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_text_RedRegular));
        }
        if (this.inputExtraFields != null) {
            for (int i12 = 0; i12 < this.inputExtraFields.length; i12++) {
                arrayList.add(new ThemeDescription((View) this.inputExtraFields[i12].getParent(), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(this.inputExtraFields[i12], ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(this.inputExtraFields[i12], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
                arrayList.add(new ThemeDescription(this.inputExtraFields[i12], ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader));
                arrayList.add(new ThemeDescription(this.inputExtraFields[i12], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
                arrayList.add(new ThemeDescription(this.inputExtraFields[i12], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
                arrayList.add(new ThemeDescription(this.inputExtraFields[i12], ThemeDescription.FLAG_PROGRESSBAR | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_text_RedRegular));
            }
        }
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle));
        arrayList.add(new ThemeDescription(this.noPasswordImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chat_messagePanelIcons));
        arrayList.add(new ThemeDescription(this.noPasswordTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText4));
        arrayList.add(new ThemeDescription(this.noPasswordSetTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText5));
        TextView textView = this.passwordForgotButton;
        int i13 = ThemeDescription.FLAG_TEXTCOLOR;
        int i14 = Theme.key_windowBackgroundWhiteBlueText4;
        arrayList.add(new ThemeDescription(textView, i13, null, null, null, null, i14));
        arrayList.add(new ThemeDescription(this.plusTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.acceptTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_passport_authorizeText));
        arrayList.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_passport_authorizeBackground));
        arrayList.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_passport_authorizeBackgroundSelected));
        ContextProgressView contextProgressView = this.progressView;
        int i15 = Theme.key_contextProgressInner2;
        arrayList.add(new ThemeDescription(contextProgressView, 0, null, null, null, null, i15));
        ContextProgressView contextProgressView2 = this.progressView;
        int i16 = Theme.key_contextProgressOuter2;
        arrayList.add(new ThemeDescription(contextProgressView2, 0, null, null, null, null, i16));
        arrayList.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, i15));
        arrayList.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, i16));
        arrayList.add(new ThemeDescription(this.emptyImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_sessions_devicesImage));
        TextView textView2 = this.emptyTextView1;
        int i17 = ThemeDescription.FLAG_TEXTCOLOR;
        int i18 = Theme.key_windowBackgroundWhiteGrayText2;
        arrayList.add(new ThemeDescription(textView2, i17, null, null, null, null, i18));
        arrayList.add(new ThemeDescription(this.emptyTextView2, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i18));
        arrayList.add(new ThemeDescription(this.emptyTextView3, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i14));
        return arrayList;
    }

    public void needHideProgress() {
        AlertDialog alertDialog = this.progressDialog;
        if (alertDialog == null) {
            return;
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.progressDialog = null;
    }

    public void needShowProgress() {
        if (getParentActivity() == null || getParentActivity().isFinishing() || this.progressDialog != null) {
            return;
        }
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        this.progressDialog = alertDialog;
        alertDialog.setCanCancel(false);
        this.progressDialog.show();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        if (i2 == -1) {
            if (i == 0 || i == 2) {
                createChatAttachView();
                ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
                if (chatAttachAlert != null) {
                    chatAttachAlert.onActivityResultFragment(i, intent, this.currentPicturePath);
                }
                this.currentPicturePath = null;
            } else if (i == 1) {
                if (intent == null || intent.getData() == null) {
                    showAttachmentError();
                    return;
                }
                ArrayList arrayList = new ArrayList();
                SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                sendingMediaInfo.uri = intent.getData();
                arrayList.add(sendingMediaInfo);
                processSelectedFiles(arrayList);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        int i = this.currentActivityType;
        int i2 = 0;
        if (i == 7) {
            this.views[this.currentViewNum].onBackPressed(true);
            while (true) {
                SlideView[] slideViewArr = this.views;
                if (i2 >= slideViewArr.length) {
                    break;
                }
                SlideView slideView = slideViewArr[i2];
                if (slideView != null) {
                    slideView.onDestroyActivity();
                }
                i2++;
            }
        } else if (i == 0 || i == 5) {
            callCallback(false);
        } else if (i == 1 || i == 2) {
            return !checkDiscard();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onDialogDismiss(Dialog dialog) {
        if (this.currentActivityType != 3 || Build.VERSION.SDK_INT < 23 || dialog != this.permissionsDialog || this.permissionsItems.isEmpty()) {
            return;
        }
        getParentActivity().requestPermissions((String[]) this.permissionsItems.toArray(new String[0]), 6);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileUploaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileUploadFailed);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.twoStepPasswordChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didRemoveTwoStepPassword);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileUploaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileUploadFailed);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.twoStepPasswordChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didRemoveTwoStepPassword);
        int i = 0;
        callCallback(false);
        ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (chatAttachAlert != null) {
            chatAttachAlert.dismissInternal();
            this.chatAttachAlert.onDestroy();
        }
        if (this.currentActivityType == 7) {
            while (true) {
                SlideView[] slideViewArr = this.views;
                if (i >= slideViewArr.length) {
                    break;
                }
                SlideView slideView = slideViewArr[i];
                if (slideView != null) {
                    slideView.onDestroyActivity();
                }
                i++;
            }
            AlertDialog alertDialog = this.progressDialog;
            if (alertDialog != null) {
                try {
                    alertDialog.dismiss();
                } catch (Exception e) {
                    FileLog.e(e);
                }
                this.progressDialog = null;
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (chatAttachAlert != null) {
            chatAttachAlert.onPause();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        ChatAttachAlert chatAttachAlert;
        TextSettingsCell textSettingsCell;
        int i2 = this.currentActivityType;
        if ((i2 != 1 && i2 != 2) || (chatAttachAlert = this.chatAttachAlert) == null) {
            if (i2 == 3 && i == 6) {
                startPhoneVerification(false, this.pendingPhone, this.pendingFinishRunnable, this.pendingErrorRunnable, this.pendingDelegate);
            }
        } else if (i == 17) {
            chatAttachAlert.getPhotoLayout().checkCamera(false);
        } else if (i == 21) {
            if (getParentActivity() == null || iArr == null || iArr.length == 0 || iArr[0] == 0) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString(R.string.AppName));
            builder.setMessage(LocaleController.getString(R.string.PermissionNoAudioVideoWithHint));
            builder.setNegativeButton(LocaleController.getString(R.string.PermissionOpenSettings), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda24
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    PassportActivity.this.lambda$onRequestPermissionsResultFragment$68(dialogInterface, i3);
                }
            });
            builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
            builder.show();
        } else if (i == 19 && iArr != null && iArr.length > 0 && iArr[0] == 0) {
            processSelectedAttach(0);
        } else if (i != 22 || iArr == null || iArr.length <= 0 || iArr[0] != 0 || (textSettingsCell = this.scanDocumentCell) == null) {
        } else {
            textSettingsCell.callOnClick();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        ViewGroup[] viewGroupArr;
        ViewGroup viewGroup;
        super.onResume();
        ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (chatAttachAlert != null) {
            chatAttachAlert.onResume();
        }
        if (this.currentActivityType == 5 && (viewGroupArr = this.inputFieldContainers) != null && (viewGroup = viewGroupArr[0]) != null && viewGroup.getVisibility() == 0) {
            this.inputFields[0].requestFocus();
            AndroidUtilities.showKeyboard(this.inputFields[0]);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.this.lambda$onResume$2();
                }
            }, 200L);
        }
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        EditTextBoldCursor editTextBoldCursor;
        if (this.presentAfterAnimation != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PassportActivity$$ExternalSyntheticLambda35
                @Override // java.lang.Runnable
                public final void run() {
                    PassportActivity.this.lambda$onTransitionAnimationEnd$67();
                }
            });
        }
        int i = this.currentActivityType;
        if (i == 5) {
            if (z) {
                if (this.inputFieldContainers[0].getVisibility() == 0) {
                    this.inputFields[0].requestFocus();
                    AndroidUtilities.showKeyboard(this.inputFields[0]);
                }
                if (this.usingSavedPassword == 2) {
                    onPasswordDone(false);
                }
            }
        } else if (i == 7) {
            if (z) {
                this.views[this.currentViewNum].onShow();
            }
        } else {
            if (i == 4) {
                if (!z) {
                    return;
                }
                this.inputFields[0].requestFocus();
                editTextBoldCursor = this.inputFields[0];
            } else if (i != 6) {
                if ((i == 2 || i == 1) && Build.VERSION.SDK_INT >= 21) {
                    createChatAttachView();
                    return;
                }
                return;
            } else if (!z) {
                return;
            } else {
                this.inputFields[0].requestFocus();
                editTextBoldCursor = this.inputFields[0];
            }
            AndroidUtilities.showKeyboard(editTextBoldCursor);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void saveSelfArgs(Bundle bundle) {
        String str = this.currentPicturePath;
        if (str != null) {
            bundle.putString("path", str);
        }
    }

    public void setNeedActivityResult(boolean z) {
        this.needActivityResult = z;
    }

    public void setPage(int i, boolean z, Bundle bundle) {
        if (i == 3) {
            this.doneItem.setVisibility(8);
        }
        SlideView[] slideViewArr = this.views;
        final SlideView slideView = slideViewArr[this.currentViewNum];
        final SlideView slideView2 = slideViewArr[i];
        this.currentViewNum = i;
        slideView2.setParams(bundle, false);
        slideView2.onShow();
        if (!z) {
            slideView2.setTranslationX(0.0f);
            slideView2.setVisibility(0);
            if (slideView != slideView2) {
                slideView.setVisibility(8);
                return;
            }
            return;
        }
        slideView2.setTranslationX(AndroidUtilities.displaySize.x);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(300L);
        animatorSet.playTogether(ObjectAnimator.ofFloat(slideView, "translationX", -AndroidUtilities.displaySize.x), ObjectAnimator.ofFloat(slideView2, "translationX", 0.0f));
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.PassportActivity.23
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                slideView.setVisibility(8);
                slideView.setX(0.0f);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                slideView2.setVisibility(0);
            }
        });
        animatorSet.start();
    }

    public void startDocumentSelectActivity() {
        try {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
            intent.setType("*/*");
            startActivityForResult(intent, 21);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }
}
