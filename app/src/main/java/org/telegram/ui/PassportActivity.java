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
import org.telegram.tgnet.TLRPC$TL_account_getAllSecureValues;
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
import org.telegram.ui.Components.RadialProgress;
import org.telegram.ui.Components.SlideView;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.CountrySelectActivity;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public class PassportActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private TextView acceptTextView;
    private TextSettingsCell addDocumentCell;
    private ShadowSectionCell addDocumentSectionCell;
    private boolean allowNonLatinName;
    private ArrayList<TLRPC$TL_secureRequiredType> availableDocumentTypes;
    private TextInfoPrivacyCell bottomCell;
    private TextInfoPrivacyCell bottomCellTranslation;
    private FrameLayout bottomLayout;
    private boolean callbackCalled;
    private ChatAttachAlert chatAttachAlert;
    private HashMap<String, String> codesMap;
    private ArrayList<String> countriesArray;
    private HashMap<String, String> countriesMap;
    private int currentActivityType;
    private long currentBotId;
    private String currentCallbackUrl;
    private String currentCitizeship;
    private HashMap<String, String> currentDocumentValues;
    private TLRPC$TL_secureRequiredType currentDocumentsType;
    private TLRPC$TL_secureValue currentDocumentsTypeValue;
    private String currentEmail;
    private int[] currentExpireDate;
    private TLRPC$TL_account_authorizationForm currentForm;
    private String currentGender;
    private String currentNonce;
    private TLRPC$TL_account_password currentPassword;
    private String currentPayload;
    private TLRPC$TL_auth_sentCode currentPhoneVerification;
    private LinearLayout currentPhotoViewerLayout;
    private String currentPicturePath;
    private String currentPublicKey;
    private String currentResidence;
    private String currentScope;
    private TLRPC$TL_secureRequiredType currentType;
    private TLRPC$TL_secureValue currentTypeValue;
    private HashMap<String, String> currentValues;
    private int currentViewNum;
    private PassportActivityDelegate delegate;
    private TextSettingsCell deletePassportCell;
    private ArrayList<View> dividers;
    private boolean documentOnly;
    private ArrayList<SecureDocument> documents;
    private HashMap<SecureDocument, SecureDocumentCell> documentsCells;
    private HashMap<String, String> documentsErrors;
    private LinearLayout documentsLayout;
    private HashMap<TLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType> documentsToTypesLink;
    private ActionBarMenuItem doneItem;
    private AnimatorSet doneItemAnimation;
    private int emailCodeLength;
    private ImageView emptyImageView;
    private LinearLayout emptyLayout;
    private TextView emptyTextView1;
    private TextView emptyTextView2;
    private TextView emptyTextView3;
    private EmptyTextProgressView emptyView;
    private HashMap<String, HashMap<String, String>> errorsMap;
    private HashMap<String, String> errorsValues;
    private View extraBackgroundView;
    private View extraBackgroundView2;
    private HashMap<String, String> fieldsErrors;
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
    private HashMap<String, String> languageMap;
    private LinearLayout linearLayout2;
    private HashMap<String, String> mainErrorsMap;
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
    private ArrayList<String> permissionsItems;
    private HashMap<String, String> phoneFormatMap;
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
    private ArrayList<SecureDocument> translationDocuments;
    private LinearLayout translationLayout;
    private HashMap<TLRPC$TL_secureRequiredType, HashMap<String, String>> typesValues;
    private HashMap<TLRPC$TL_secureRequiredType, TextDetailSecureCell> typesViews;
    private TextSettingsCell uploadDocumentCell;
    private TextDetailSettingsCell uploadFrontCell;
    private TextDetailSettingsCell uploadReverseCell;
    private TextDetailSettingsCell uploadSelfieCell;
    private TextSettingsCell uploadTranslationCell;
    private HashMap<String, SecureDocument> uploadingDocuments;
    private int uploadingFileType;
    private boolean useCurrentValue;
    private int usingSavedPassword;
    private SlideView[] views;

    /* loaded from: classes3.dex */
    public interface ErrorRunnable {
        void onError(String str, String str2);
    }

    /* loaded from: classes3.dex */
    public interface PassportActivityDelegate {
        void deleteValue(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, ArrayList<TLRPC$TL_secureRequiredType> arrayList, boolean z, Runnable runnable, ErrorRunnable errorRunnable);

        SecureDocument saveFile(TLRPC$TL_secureFile tLRPC$TL_secureFile);

        void saveValue(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, String str, String str2, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, String str3, ArrayList<SecureDocument> arrayList, SecureDocument secureDocument, ArrayList<SecureDocument> arrayList2, SecureDocument secureDocument2, SecureDocument secureDocument3, Runnable runnable, ErrorRunnable errorRunnable);
    }

    /* renamed from: org.telegram.ui.PassportActivity$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends PhotoViewer.EmptyPhotoViewerProvider {
        AnonymousClass1() {
            PassportActivity.this = r1;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, TLRPC$FileLocation tLRPC$FileLocation, int i, boolean z) {
            if (i < 0 || i >= PassportActivity.this.currentPhotoViewerLayout.getChildCount()) {
                return null;
            }
            SecureDocumentCell secureDocumentCell = (SecureDocumentCell) PassportActivity.this.currentPhotoViewerLayout.getChildAt(i);
            int[] iArr = new int[2];
            secureDocumentCell.imageView.getLocationInWindow(iArr);
            PhotoViewer.PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
            int i2 = 0;
            placeProviderObject.viewX = iArr[0];
            int i3 = iArr[1];
            if (Build.VERSION.SDK_INT < 21) {
                i2 = AndroidUtilities.statusBarHeight;
            }
            placeProviderObject.viewY = i3 - i2;
            placeProviderObject.parentView = PassportActivity.this.currentPhotoViewerLayout;
            ImageReceiver imageReceiver = secureDocumentCell.imageView.getImageReceiver();
            placeProviderObject.imageReceiver = imageReceiver;
            placeProviderObject.thumb = imageReceiver.getBitmapSafe();
            return placeProviderObject;
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public void deleteImageAtIndex(int i) {
            SecureDocument secureDocument = PassportActivity.this.uploadingFileType == 1 ? PassportActivity.this.selfieDocument : PassportActivity.this.uploadingFileType == 4 ? (SecureDocument) PassportActivity.this.translationDocuments.get(i) : PassportActivity.this.uploadingFileType == 2 ? PassportActivity.this.frontDocument : PassportActivity.this.uploadingFileType == 3 ? PassportActivity.this.reverseDocument : (SecureDocument) PassportActivity.this.documents.get(i);
            SecureDocumentCell secureDocumentCell = (SecureDocumentCell) PassportActivity.this.documentsCells.remove(secureDocument);
            if (secureDocumentCell == null) {
                return;
            }
            String documentHash = PassportActivity.this.getDocumentHash(secureDocument);
            String str = null;
            if (PassportActivity.this.uploadingFileType == 1) {
                PassportActivity.this.selfieDocument = null;
                str = "selfie" + documentHash;
            } else if (PassportActivity.this.uploadingFileType != 4) {
                if (PassportActivity.this.uploadingFileType == 2) {
                    PassportActivity.this.frontDocument = null;
                    str = "front" + documentHash;
                } else if (PassportActivity.this.uploadingFileType == 3) {
                    PassportActivity.this.reverseDocument = null;
                    str = "reverse" + documentHash;
                } else if (PassportActivity.this.uploadingFileType == 0) {
                    str = "files" + documentHash;
                }
            } else {
                str = "translation" + documentHash;
            }
            if (str != null) {
                if (PassportActivity.this.documentsErrors != null) {
                    PassportActivity.this.documentsErrors.remove(str);
                }
                if (PassportActivity.this.errorsValues != null) {
                    PassportActivity.this.errorsValues.remove(str);
                }
            }
            PassportActivity passportActivity = PassportActivity.this;
            passportActivity.updateUploadText(passportActivity.uploadingFileType);
            PassportActivity.this.currentPhotoViewerLayout.removeView(secureDocumentCell);
        }

        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
        public String getDeleteMessageString() {
            if (PassportActivity.this.uploadingFileType == 1) {
                return LocaleController.formatString("PassportDeleteSelfieAlert", 2131627271, new Object[0]);
            }
            return LocaleController.formatString("PassportDeleteScanAlert", 2131627269, new Object[0]);
        }
    }

    /* loaded from: classes3.dex */
    public class LinkSpan extends ClickableSpan {
        public LinkSpan() {
            PassportActivity.this = r1;
        }

        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(true);
            textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(View view) {
            Browser.openUrl(PassportActivity.this.getParentActivity(), PassportActivity.this.currentForm.privacy_policy_url);
        }
    }

    /* loaded from: classes3.dex */
    public class TextDetailSecureCell extends FrameLayout {
        private ImageView checkImageView;
        private boolean needDivider;
        private TextView textView;
        private TextView valueTextView;

        public TextDetailSecureCell(PassportActivity passportActivity, Context context) {
            super(context);
            int i = 21;
            int i2 = passportActivity.currentActivityType == 8 ? 21 : 51;
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            int i3 = 5;
            this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            TextView textView2 = this.textView;
            boolean z = LocaleController.isRTL;
            addView(textView2, LayoutHelper.createFrame(-2, -2.0f, (z ? 5 : 3) | 48, z ? i2 : 21, 10.0f, z ? 21 : i2, 0.0f));
            TextView textView3 = new TextView(context);
            this.valueTextView = textView3;
            textView3.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
            this.valueTextView.setTextSize(1, 13.0f);
            this.valueTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setEllipsize(TextUtils.TruncateAt.END);
            this.valueTextView.setPadding(0, 0, 0, 0);
            TextView textView4 = this.valueTextView;
            boolean z2 = LocaleController.isRTL;
            addView(textView4, LayoutHelper.createFrame(-2, -2.0f, (z2 ? 5 : 3) | 48, z2 ? i2 : 21, 35.0f, !z2 ? i2 : i, 0.0f));
            ImageView imageView = new ImageView(context);
            this.checkImageView = imageView;
            imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), PorterDuff.Mode.MULTIPLY));
            this.checkImageView.setImageResource(2131166165);
            addView(this.checkImageView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 3 : i3) | 48, 21.0f, 25.0f, 21.0f, 0.0f));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.needDivider ? 1 : 0), 1073741824));
        }

        public void setTextAndValue(String str, CharSequence charSequence, boolean z) {
            this.textView.setText(str);
            this.valueTextView.setText(charSequence);
            this.needDivider = z;
            setWillNotDraw(!z);
        }

        public void setChecked(boolean z) {
            this.checkImageView.setVisibility(z ? 0 : 4);
        }

        public void setValue(CharSequence charSequence) {
            this.valueTextView.setText(charSequence);
        }

        public void setNeedDivider(boolean z) {
            this.needDivider = z;
            setWillNotDraw(!z);
            invalidate();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.needDivider) {
                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class SecureDocumentCell extends FrameLayout implements DownloadController.FileDownloadProgressListener {
        private int TAG;
        private int buttonState;
        private SecureDocument currentSecureDocument;
        private BackupImageView imageView;
        private RadialProgress radialProgress = new RadialProgress(this);
        private TextView textView;
        private TextView valueTextView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SecureDocumentCell(Context context) {
            super(context);
            PassportActivity.this = r14;
            this.TAG = DownloadController.getInstance(((BaseFragment) r14).currentAccount).generateObserverTag();
            BackupImageView backupImageView = new BackupImageView(context);
            this.imageView = backupImageView;
            int i = 5;
            addView(backupImageView, LayoutHelper.createFrame(48, 48.0f, (LocaleController.isRTL ? 5 : 3) | 48, 21.0f, 8.0f, 21.0f, 0.0f));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            View view = this.textView;
            boolean z = LocaleController.isRTL;
            int i2 = 21;
            addView(view, LayoutHelper.createFrame(-2, -2.0f, (z ? 5 : 3) | 48, z ? 21 : 81, 10.0f, z ? 81 : 21, 0.0f));
            TextView textView2 = new TextView(context);
            this.valueTextView = textView2;
            textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
            this.valueTextView.setTextSize(1, 13.0f);
            this.valueTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setPadding(0, 0, 0, 0);
            View view2 = this.valueTextView;
            boolean z2 = LocaleController.isRTL;
            addView(view2, LayoutHelper.createFrame(-2, -2.0f, (!z2 ? 3 : i) | 48, z2 ? 21 : 81, 35.0f, z2 ? 81 : i2, 0.0f));
            setWillNotDraw(false);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + 1, 1073741824));
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            int left = this.imageView.getLeft() + ((this.imageView.getMeasuredWidth() - AndroidUtilities.dp(24.0f)) / 2);
            int top = this.imageView.getTop() + ((this.imageView.getMeasuredHeight() - AndroidUtilities.dp(24.0f)) / 2);
            this.radialProgress.setProgressRect(left, top, AndroidUtilities.dp(24.0f) + left, AndroidUtilities.dp(24.0f) + top);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            boolean drawChild = super.drawChild(canvas, view, j);
            if (view == this.imageView) {
                this.radialProgress.draw(canvas);
            }
            return drawChild;
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
            float f = 0.0f;
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
                this.radialProgress.setBackground(Theme.chat_photoStatesDrawables[5][0], true, z);
                RadialProgress radialProgress = this.radialProgress;
                if (fileProgress != null) {
                    f = fileProgress.floatValue();
                }
                radialProgress.setProgress(f, false);
                invalidate();
            } else if (exists) {
                DownloadController.getInstance(((BaseFragment) PassportActivity.this).currentAccount).removeLoadingFileObserver(this);
                this.buttonState = -1;
                this.radialProgress.setBackground(null, false, z);
                invalidate();
            } else {
                DownloadController.getInstance(((BaseFragment) PassportActivity.this).currentAccount).addLoadingFileObserver(attachFileName, this);
                this.buttonState = 1;
                Float fileProgress2 = ImageLoader.getInstance().getFileProgress(attachFileName);
                this.radialProgress.setBackground(Theme.chat_photoStatesDrawables[5][0], true, z);
                RadialProgress radialProgress2 = this.radialProgress;
                if (fileProgress2 != null) {
                    f = fileProgress2.floatValue();
                }
                radialProgress2.setProgress(f, z);
                invalidate();
            }
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

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onSuccessDownload(String str) {
            this.radialProgress.setProgress(1.0f, true);
            updateButtonState(true);
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
        public int getObserverTag() {
            return this.TAG;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x0148 A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x015a  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x015f A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0170 A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x017a A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0184 A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x018e A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0198 A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01a2 A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01ab A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01b9  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01bb A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01c0 A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01d3 A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0201 A[Catch: Exception -> 0x023d, TryCatch #0 {Exception -> 0x023d, blocks: (B:6:0x0037, B:8:0x004a, B:11:0x0065, B:13:0x007a, B:15:0x007e, B:16:0x008d, B:18:0x0091, B:19:0x00a0, B:21:0x00a4, B:22:0x00b3, B:24:0x00b7, B:25:0x00c6, B:27:0x00ca, B:28:0x00d9, B:30:0x00dd, B:31:0x00eb, B:33:0x00ef, B:34:0x00ff, B:36:0x0103, B:37:0x0106, B:39:0x010e, B:41:0x011a, B:44:0x0126, B:48:0x012e, B:49:0x013e, B:51:0x0148, B:55:0x015f, B:57:0x0166, B:58:0x016c, B:60:0x0170, B:63:0x017a, B:66:0x0184, B:69:0x018e, B:72:0x0198, B:75:0x01a2, B:78:0x01ab, B:82:0x01b6, B:84:0x01bb, B:85:0x01c0, B:86:0x01d3, B:88:0x01e8, B:89:0x01fb, B:90:0x0201, B:92:0x0216, B:93:0x0229, B:95:0x0231), top: B:100:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0214  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x022f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public PassportActivity(int i, long j, String str, String str2, String str3, String str4, String str5, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$TL_account_password tLRPC$TL_account_password) {
        this(i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_password, (TLRPC$TL_secureRequiredType) null, (TLRPC$TL_secureValue) null, (TLRPC$TL_secureRequiredType) null, (TLRPC$TL_secureValue) null, (HashMap<String, String>) null, (HashMap<String, String>) null);
        int i2;
        String str6;
        String str7;
        String str8;
        String str9;
        HashMap<String, String> hashMap;
        char c;
        boolean z;
        byte[] bArr;
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
            Collections.sort(tLRPC$TL_account_authorizationForm.errors, new AnonymousClass2());
            int size = tLRPC$TL_account_authorizationForm.errors.size();
            int i3 = 0;
            while (i3 < size) {
                TLRPC$SecureValueError tLRPC$SecureValueError = tLRPC$TL_account_authorizationForm.errors.get(i3);
                String str10 = null;
                str10 = null;
                str10 = null;
                str10 = null;
                str10 = null;
                r6 = null;
                str10 = null;
                byte[] bArr2 = null;
                if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorFrontSide) {
                    TLRPC$TL_secureValueErrorFrontSide tLRPC$TL_secureValueErrorFrontSide = (TLRPC$TL_secureValueErrorFrontSide) tLRPC$SecureValueError;
                    str7 = getNameForType(tLRPC$TL_secureValueErrorFrontSide.type);
                    str9 = tLRPC$TL_secureValueErrorFrontSide.text;
                    bArr = tLRPC$TL_secureValueErrorFrontSide.file_hash;
                    str6 = "front";
                } else if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorReverseSide) {
                    TLRPC$TL_secureValueErrorReverseSide tLRPC$TL_secureValueErrorReverseSide = (TLRPC$TL_secureValueErrorReverseSide) tLRPC$SecureValueError;
                    str7 = getNameForType(tLRPC$TL_secureValueErrorReverseSide.type);
                    str9 = tLRPC$TL_secureValueErrorReverseSide.text;
                    bArr = tLRPC$TL_secureValueErrorReverseSide.file_hash;
                    str6 = "reverse";
                } else if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorSelfie) {
                    TLRPC$TL_secureValueErrorSelfie tLRPC$TL_secureValueErrorSelfie = (TLRPC$TL_secureValueErrorSelfie) tLRPC$SecureValueError;
                    str7 = getNameForType(tLRPC$TL_secureValueErrorSelfie.type);
                    str9 = tLRPC$TL_secureValueErrorSelfie.text;
                    bArr = tLRPC$TL_secureValueErrorSelfie.file_hash;
                    str6 = "selfie";
                } else if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorTranslationFile) {
                    TLRPC$TL_secureValueErrorTranslationFile tLRPC$TL_secureValueErrorTranslationFile = (TLRPC$TL_secureValueErrorTranslationFile) tLRPC$SecureValueError;
                    str7 = getNameForType(tLRPC$TL_secureValueErrorTranslationFile.type);
                    str9 = tLRPC$TL_secureValueErrorTranslationFile.text;
                    bArr = tLRPC$TL_secureValueErrorTranslationFile.file_hash;
                    str6 = "translation";
                } else {
                    if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorTranslationFiles) {
                        TLRPC$TL_secureValueErrorTranslationFiles tLRPC$TL_secureValueErrorTranslationFiles = (TLRPC$TL_secureValueErrorTranslationFiles) tLRPC$SecureValueError;
                        str7 = getNameForType(tLRPC$TL_secureValueErrorTranslationFiles.type);
                        str9 = tLRPC$TL_secureValueErrorTranslationFiles.text;
                        str8 = null;
                        str6 = "translation";
                    } else if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorFile) {
                        TLRPC$TL_secureValueErrorFile tLRPC$TL_secureValueErrorFile = (TLRPC$TL_secureValueErrorFile) tLRPC$SecureValueError;
                        str7 = getNameForType(tLRPC$TL_secureValueErrorFile.type);
                        str9 = tLRPC$TL_secureValueErrorFile.text;
                        bArr = tLRPC$TL_secureValueErrorFile.file_hash;
                        str6 = "files";
                    } else if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorFiles) {
                        TLRPC$TL_secureValueErrorFiles tLRPC$TL_secureValueErrorFiles = (TLRPC$TL_secureValueErrorFiles) tLRPC$SecureValueError;
                        str7 = getNameForType(tLRPC$TL_secureValueErrorFiles.type);
                        str9 = tLRPC$TL_secureValueErrorFiles.text;
                        str8 = null;
                        str6 = "files";
                    } else if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueError) {
                        TLRPC$TL_secureValueError tLRPC$TL_secureValueError = (TLRPC$TL_secureValueError) tLRPC$SecureValueError;
                        str7 = getNameForType(tLRPC$TL_secureValueError.type);
                        str9 = tLRPC$TL_secureValueError.text;
                        bArr = tLRPC$TL_secureValueError.hash;
                        str6 = "error_all";
                    } else {
                        if (tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorData) {
                            TLRPC$TL_secureValueErrorData tLRPC$TL_secureValueErrorData = (TLRPC$TL_secureValueErrorData) tLRPC$SecureValueError;
                            int i4 = 0;
                            while (true) {
                                if (i4 < tLRPC$TL_account_authorizationForm.values.size()) {
                                    TLRPC$TL_secureData tLRPC$TL_secureData = tLRPC$TL_account_authorizationForm.values.get(i4).data;
                                    if (tLRPC$TL_secureData == null || !Arrays.equals(tLRPC$TL_secureData.data_hash, tLRPC$TL_secureValueErrorData.data_hash)) {
                                        i4++;
                                    } else {
                                        z = true;
                                    }
                                } else {
                                    z = false;
                                }
                            }
                            if (z) {
                                str7 = getNameForType(tLRPC$TL_secureValueErrorData.type);
                                str9 = tLRPC$TL_secureValueErrorData.text;
                                str10 = tLRPC$TL_secureValueErrorData.field;
                                bArr = tLRPC$TL_secureValueErrorData.data_hash;
                                str6 = "data";
                            }
                        }
                        i2 = size;
                        i3++;
                        size = i2;
                    }
                    hashMap = this.errorsMap.get(str7);
                    if (hashMap != null) {
                        hashMap = new HashMap<>();
                        i2 = size;
                        this.errorsMap.put(str7, hashMap);
                        this.mainErrorsMap.put(str7, str9);
                    } else {
                        i2 = size;
                    }
                    c = 2;
                    String encodeToString = bArr2 == null ? Base64.encodeToString(bArr2, 2) : "";
                    switch (str6.hashCode()) {
                        case -1840647503:
                            if (str6.equals("translation")) {
                                c = 3;
                                break;
                            }
                            c = 65535;
                            break;
                        case -906020504:
                            if (str6.equals("selfie")) {
                                break;
                            }
                            c = 65535;
                            break;
                        case 3076010:
                            if (str6.equals("data")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case 97434231:
                            if (str6.equals("files")) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 97705513:
                            if (str6.equals("front")) {
                                c = 4;
                                break;
                            }
                            c = 65535;
                            break;
                        case 329856746:
                            if (str6.equals("error_all")) {
                                c = 6;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1099846370:
                            if (str6.equals("reverse")) {
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
                            if (str8 != null) {
                                hashMap.put(str8, str9);
                                break;
                            } else {
                                continue;
                                continue;
                            }
                        case 1:
                            if (bArr2 != null) {
                                hashMap.put("files" + encodeToString, str9);
                                continue;
                                continue;
                            } else {
                                hashMap.put("files_all", str9);
                                break;
                            }
                        case 2:
                            hashMap.put("selfie" + encodeToString, str9);
                            continue;
                            continue;
                        case 3:
                            if (bArr2 != null) {
                                hashMap.put("translation" + encodeToString, str9);
                                continue;
                                continue;
                            } else {
                                hashMap.put("translation_all", str9);
                                break;
                            }
                        case 4:
                            hashMap.put("front" + encodeToString, str9);
                            continue;
                            continue;
                        case 5:
                            hashMap.put("reverse" + encodeToString, str9);
                            continue;
                            continue;
                        case 6:
                            hashMap.put("error_all", str9);
                            continue;
                            continue;
                        default:
                            continue;
                            continue;
                    }
                    i3++;
                    size = i2;
                }
                String str11 = str10;
                bArr2 = bArr;
                str8 = str11;
                hashMap = this.errorsMap.get(str7);
                if (hashMap != null) {
                }
                c = 2;
                if (bArr2 == null) {
                }
                switch (str6.hashCode()) {
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

    /* renamed from: org.telegram.ui.PassportActivity$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements Comparator<TLRPC$SecureValueError> {
        AnonymousClass2() {
            PassportActivity.this = r1;
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
            if (!(tLRPC$SecureValueError instanceof TLRPC$TL_secureValueErrorData)) {
                return 100;
            }
            return PassportActivity.this.getFieldCost(((TLRPC$TL_secureValueErrorData) tLRPC$SecureValueError).field);
        }

        public int compare(TLRPC$SecureValueError tLRPC$SecureValueError, TLRPC$SecureValueError tLRPC$SecureValueError2) {
            int errorValue = getErrorValue(tLRPC$SecureValueError);
            int errorValue2 = getErrorValue(tLRPC$SecureValueError2);
            if (errorValue < errorValue2) {
                return -1;
            }
            return errorValue > errorValue2 ? 1 : 0;
        }
    }

    public PassportActivity(int i, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$TL_account_password tLRPC$TL_account_password, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureValue tLRPC$TL_secureValue, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, TLRPC$TL_secureValue tLRPC$TL_secureValue2, HashMap<String, String> hashMap, HashMap<String, String> hashMap2) {
        this.currentCitizeship = "";
        this.currentResidence = "";
        this.currentExpireDate = new int[3];
        this.dividers = new ArrayList<>();
        this.nonLatinNames = new boolean[3];
        this.allowNonLatinName = true;
        this.countriesArray = new ArrayList<>();
        this.countriesMap = new HashMap<>();
        this.codesMap = new HashMap<>();
        this.phoneFormatMap = new HashMap<>();
        this.documents = new ArrayList<>();
        this.translationDocuments = new ArrayList<>();
        this.documentsCells = new HashMap<>();
        this.uploadingDocuments = new HashMap<>();
        this.typesValues = new HashMap<>();
        this.typesViews = new HashMap<>();
        this.documentsToTypesLink = new HashMap<>();
        this.errorsMap = new HashMap<>();
        this.mainErrorsMap = new HashMap<>();
        this.errorsValues = new HashMap<>();
        this.provider = new AnonymousClass1();
        this.currentActivityType = i;
        this.currentForm = tLRPC$TL_account_authorizationForm;
        this.currentType = tLRPC$TL_secureRequiredType;
        if (tLRPC$TL_secureRequiredType != null) {
            this.allowNonLatinName = tLRPC$TL_secureRequiredType.native_names;
        }
        this.currentTypeValue = tLRPC$TL_secureValue;
        this.currentDocumentsType = tLRPC$TL_secureRequiredType2;
        this.currentDocumentsTypeValue = tLRPC$TL_secureValue2;
        this.currentPassword = tLRPC$TL_account_password;
        this.currentValues = hashMap;
        this.currentDocumentValues = hashMap2;
        if (i == 3) {
            this.permissionsItems = new ArrayList<>();
        } else if (i == 7) {
            this.views = new SlideView[3];
        }
        if (this.currentValues == null) {
            this.currentValues = new HashMap<>();
        }
        if (this.currentDocumentValues == null) {
            this.currentDocumentValues = new HashMap<>();
        }
        if (i == 5) {
            if (UserConfig.getInstance(this.currentAccount).savedPasswordHash != null && UserConfig.getInstance(this.currentAccount).savedSaltedPassword != null) {
                this.usingSavedPassword = 1;
                this.savedPasswordHash = UserConfig.getInstance(this.currentAccount).savedPasswordHash;
                this.savedSaltedPassword = UserConfig.getInstance(this.currentAccount).savedSaltedPassword;
            }
            TLRPC$TL_account_password tLRPC$TL_account_password2 = this.currentPassword;
            if (tLRPC$TL_account_password2 == null) {
                loadPasswordInfo();
            } else {
                TwoStepVerificationActivity.initPasswordNewAlgo(tLRPC$TL_account_password2);
                if (this.usingSavedPassword == 1) {
                    onPasswordDone(true);
                }
            }
            if (SharedConfig.isPassportConfigLoaded()) {
                return;
            }
            TLRPC$TL_help_getPassportConfig tLRPC$TL_help_getPassportConfig = new TLRPC$TL_help_getPassportConfig();
            tLRPC$TL_help_getPassportConfig.hash = SharedConfig.passportConfigHash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_help_getPassportConfig, PassportActivity$$ExternalSyntheticLambda68.INSTANCE);
        }
    }

    public static /* synthetic */ void lambda$new$1(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda46(tLObject));
    }

    public static /* synthetic */ void lambda$new$0(TLObject tLObject) {
        if (tLObject instanceof TLRPC$TL_help_passportConfig) {
            TLRPC$TL_help_passportConfig tLRPC$TL_help_passportConfig = (TLRPC$TL_help_passportConfig) tLObject;
            SharedConfig.setPassportConfig(tLRPC$TL_help_passportConfig.countries_langs.data, tLRPC$TL_help_passportConfig.hash);
            return;
        }
        SharedConfig.getCountryLangs();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        ViewGroup[] viewGroupArr;
        super.onResume();
        ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (chatAttachAlert != null) {
            chatAttachAlert.onResume();
        }
        if (this.currentActivityType == 5 && (viewGroupArr = this.inputFieldContainers) != null && viewGroupArr[0] != null && viewGroupArr[0].getVisibility() == 0) {
            this.inputFields[0].requestFocus();
            AndroidUtilities.showKeyboard(this.inputFields[0]);
            AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda50(this), 200L);
        }
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
    }

    public /* synthetic */ void lambda$onResume$2() {
        ViewGroup[] viewGroupArr = this.inputFieldContainers;
        if (viewGroupArr == null || viewGroupArr[0] == null || viewGroupArr[0].getVisibility() != 0) {
            return;
        }
        this.inputFields[0].requestFocus();
        AndroidUtilities.showKeyboard(this.inputFields[0]);
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
                if (slideViewArr[i] != null) {
                    slideViewArr[i].onDestroyActivity();
                }
                i++;
            }
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
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        ChatAttachAlert chatAttachAlert;
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass3());
        if (this.currentActivityType == 7) {
            AnonymousClass4 anonymousClass4 = new AnonymousClass4(context);
            this.scrollView = anonymousClass4;
            this.fragmentView = anonymousClass4;
            anonymousClass4.setFillViewport(true);
            AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("actionBarDefault"));
        } else {
            FrameLayout frameLayout = new FrameLayout(context);
            this.fragmentView = frameLayout;
            FrameLayout frameLayout2 = frameLayout;
            frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
            AnonymousClass5 anonymousClass5 = new AnonymousClass5(this, context);
            this.scrollView = anonymousClass5;
            anonymousClass5.setFillViewport(true);
            AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("actionBarDefault"));
            frameLayout2.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.currentActivityType == 0 ? 48.0f : 0.0f));
            LinearLayout linearLayout = new LinearLayout(context);
            this.linearLayout2 = linearLayout;
            linearLayout.setOrientation(1);
            this.scrollView.addView(this.linearLayout2, new FrameLayout.LayoutParams(-1, -2));
        }
        int i = this.currentActivityType;
        if (i != 0 && i != 8) {
            this.doneItem = this.actionBar.createMenu().addItemWithWidth(2, 2131165450, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", 2131625541));
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
        } else if (i3 == 1) {
            createIdentityInterface(context);
            fillInitialValues();
        } else if (i3 == 2) {
            createAddressInterface(context);
            fillInitialValues();
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
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.PassportActivity$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass3() {
            PassportActivity.this = r1;
        }

        /* JADX WARN: Removed duplicated region for block: B:75:0x0367  */
        /* JADX WARN: Removed duplicated region for block: B:78:0x0378  */
        /* JADX WARN: Removed duplicated region for block: B:81:0x0390  */
        /* JADX WARN: Removed duplicated region for block: B:82:0x0396  */
        /* JADX WARN: Removed duplicated region for block: B:85:0x039f  */
        /* JADX WARN: Removed duplicated region for block: B:86:0x03a6  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private boolean onIdentityDone(Runnable runnable, ErrorRunnable errorRunnable) {
            JSONObject jSONObject;
            JSONObject jSONObject2;
            String str;
            String str2;
            String str3;
            if (!PassportActivity.this.uploadingDocuments.isEmpty() || PassportActivity.this.checkFieldsForError()) {
                return false;
            }
            if (PassportActivity.this.allowNonLatinName) {
                PassportActivity.this.allowNonLatinName = false;
                boolean z = false;
                for (int i = 0; i < PassportActivity.this.nonLatinNames.length; i++) {
                    if (PassportActivity.this.nonLatinNames[i]) {
                        PassportActivity.this.inputFields[i].setErrorText(LocaleController.getString("PassportUseLatinOnly", 2131627403));
                        if (!z) {
                            if (PassportActivity.this.nonLatinNames[0]) {
                                PassportActivity passportActivity = PassportActivity.this;
                                str = passportActivity.getTranslitString(passportActivity.inputExtraFields[0].getText().toString());
                            } else {
                                str = PassportActivity.this.inputFields[0].getText().toString();
                            }
                            String str4 = str;
                            if (PassportActivity.this.nonLatinNames[1]) {
                                PassportActivity passportActivity2 = PassportActivity.this;
                                str2 = passportActivity2.getTranslitString(passportActivity2.inputExtraFields[1].getText().toString());
                            } else {
                                str2 = PassportActivity.this.inputFields[1].getText().toString();
                            }
                            String str5 = str2;
                            if (PassportActivity.this.nonLatinNames[2]) {
                                PassportActivity passportActivity3 = PassportActivity.this;
                                str3 = passportActivity3.getTranslitString(passportActivity3.inputExtraFields[2].getText().toString());
                            } else {
                                str3 = PassportActivity.this.inputFields[2].getText().toString();
                            }
                            String str6 = str3;
                            if (!TextUtils.isEmpty(str4) && !TextUtils.isEmpty(str5) && !TextUtils.isEmpty(str6)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PassportActivity.this.getParentActivity());
                                builder.setMessage(LocaleController.formatString("PassportNameCheckAlert", 2131627349, str4, str5, str6));
                                builder.setTitle(LocaleController.getString("AppName", 2131624384));
                                builder.setPositiveButton(LocaleController.getString("Done", 2131625541), new PassportActivity$3$$ExternalSyntheticLambda1(this, str4, str5, str6, runnable, errorRunnable));
                                builder.setNegativeButton(LocaleController.getString("Edit", 2131625553), new PassportActivity$3$$ExternalSyntheticLambda0(this, i));
                                PassportActivity.this.showDialog(builder.create());
                            } else {
                                PassportActivity passportActivity4 = PassportActivity.this;
                                passportActivity4.onFieldError(passportActivity4.inputFields[i]);
                            }
                            z = true;
                        }
                    }
                }
                if (z) {
                    return false;
                }
            }
            if (!PassportActivity.this.isHasNotAnyChanges()) {
                SecureDocument secureDocument = null;
                try {
                    if (!PassportActivity.this.documentOnly) {
                        HashMap hashMap = new HashMap(PassportActivity.this.currentValues);
                        if (PassportActivity.this.currentType.native_names) {
                            if (PassportActivity.this.nativeInfoCell.getVisibility() == 0) {
                                hashMap.put("first_name_native", PassportActivity.this.inputExtraFields[0].getText().toString());
                                hashMap.put("middle_name_native", PassportActivity.this.inputExtraFields[1].getText().toString());
                                hashMap.put("last_name_native", PassportActivity.this.inputExtraFields[2].getText().toString());
                            } else {
                                hashMap.put("first_name_native", PassportActivity.this.inputFields[0].getText().toString());
                                hashMap.put("middle_name_native", PassportActivity.this.inputFields[1].getText().toString());
                                hashMap.put("last_name_native", PassportActivity.this.inputFields[2].getText().toString());
                            }
                        }
                        hashMap.put("first_name", PassportActivity.this.inputFields[0].getText().toString());
                        hashMap.put("middle_name", PassportActivity.this.inputFields[1].getText().toString());
                        hashMap.put("last_name", PassportActivity.this.inputFields[2].getText().toString());
                        hashMap.put("birth_date", PassportActivity.this.inputFields[3].getText().toString());
                        hashMap.put("gender", PassportActivity.this.currentGender);
                        hashMap.put("country_code", PassportActivity.this.currentCitizeship);
                        hashMap.put("residence_country_code", PassportActivity.this.currentResidence);
                        jSONObject2 = new JSONObject();
                        try {
                            ArrayList arrayList = new ArrayList(hashMap.keySet());
                            Collections.sort(arrayList, new PassportActivity$3$$ExternalSyntheticLambda4(this));
                            int size = arrayList.size();
                            for (int i2 = 0; i2 < size; i2++) {
                                String str7 = (String) arrayList.get(i2);
                                jSONObject2.put(str7, hashMap.get(str7));
                            }
                        } catch (Exception unused) {
                        }
                    } else {
                        jSONObject2 = null;
                    }
                } catch (Exception unused2) {
                    jSONObject2 = null;
                    jSONObject = null;
                }
                if (PassportActivity.this.currentDocumentsType != null) {
                    HashMap hashMap2 = new HashMap(PassportActivity.this.currentDocumentValues);
                    hashMap2.put("document_no", PassportActivity.this.inputFields[7].getText().toString());
                    if (PassportActivity.this.currentExpireDate[0] != 0) {
                        hashMap2.put("expiry_date", String.format(Locale.US, "%02d.%02d.%d", Integer.valueOf(PassportActivity.this.currentExpireDate[2]), Integer.valueOf(PassportActivity.this.currentExpireDate[1]), Integer.valueOf(PassportActivity.this.currentExpireDate[0])));
                    } else {
                        hashMap2.put("expiry_date", "");
                    }
                    jSONObject = new JSONObject();
                    try {
                        ArrayList arrayList2 = new ArrayList(hashMap2.keySet());
                        Collections.sort(arrayList2, new PassportActivity$3$$ExternalSyntheticLambda5(this));
                        int size2 = arrayList2.size();
                        for (int i3 = 0; i3 < size2; i3++) {
                            String str8 = (String) arrayList2.get(i3);
                            jSONObject.put(str8, hashMap2.get(str8));
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
                    String jSONObject3 = jSONObject2 == null ? jSONObject2.toString() : null;
                    TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2 = PassportActivity.this.currentDocumentsType;
                    String jSONObject4 = jSONObject == null ? jSONObject.toString() : null;
                    SecureDocument secureDocument2 = PassportActivity.this.selfieDocument;
                    ArrayList<SecureDocument> arrayList3 = PassportActivity.this.translationDocuments;
                    SecureDocument secureDocument3 = PassportActivity.this.frontDocument;
                    if (PassportActivity.this.reverseLayout != null && PassportActivity.this.reverseLayout.getVisibility() == 0) {
                        secureDocument = PassportActivity.this.reverseDocument;
                    }
                    passportActivityDelegate.saveValue(tLRPC$TL_secureRequiredType, null, jSONObject3, tLRPC$TL_secureRequiredType2, jSONObject4, null, secureDocument2, arrayList3, secureDocument3, secureDocument, runnable, errorRunnable);
                    return true;
                }
                jSONObject = null;
                if (PassportActivity.this.fieldsErrors != null) {
                }
                if (PassportActivity.this.documentsErrors != null) {
                }
                PassportActivityDelegate passportActivityDelegate2 = PassportActivity.this.delegate;
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3 = PassportActivity.this.currentType;
                if (jSONObject2 == null) {
                }
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType22 = PassportActivity.this.currentDocumentsType;
                if (jSONObject == null) {
                }
                SecureDocument secureDocument22 = PassportActivity.this.selfieDocument;
                ArrayList<SecureDocument> arrayList32 = PassportActivity.this.translationDocuments;
                SecureDocument secureDocument32 = PassportActivity.this.frontDocument;
                if (PassportActivity.this.reverseLayout != null) {
                    secureDocument = PassportActivity.this.reverseDocument;
                }
                passportActivityDelegate2.saveValue(tLRPC$TL_secureRequiredType3, null, jSONObject3, tLRPC$TL_secureRequiredType22, jSONObject4, null, secureDocument22, arrayList32, secureDocument32, secureDocument, runnable, errorRunnable);
                return true;
            }
            PassportActivity.this.finishFragment();
            return false;
        }

        public /* synthetic */ void lambda$onIdentityDone$0(String str, String str2, String str3, Runnable runnable, ErrorRunnable errorRunnable, DialogInterface dialogInterface, int i) {
            PassportActivity.this.inputFields[0].setText(str);
            PassportActivity.this.inputFields[1].setText(str2);
            PassportActivity.this.inputFields[2].setText(str3);
            PassportActivity.this.showEditDoneProgress(true, true);
            onIdentityDone(runnable, errorRunnable);
        }

        public /* synthetic */ void lambda$onIdentityDone$1(int i, DialogInterface dialogInterface, int i2) {
            PassportActivity passportActivity = PassportActivity.this;
            passportActivity.onFieldError(passportActivity.inputFields[i]);
        }

        public /* synthetic */ int lambda$onIdentityDone$2(String str, String str2) {
            int fieldCost = PassportActivity.this.getFieldCost(str);
            int fieldCost2 = PassportActivity.this.getFieldCost(str2);
            if (fieldCost < fieldCost2) {
                return -1;
            }
            return fieldCost > fieldCost2 ? 1 : 0;
        }

        public /* synthetic */ int lambda$onIdentityDone$3(String str, String str2) {
            int fieldCost = PassportActivity.this.getFieldCost(str);
            int fieldCost2 = PassportActivity.this.getFieldCost(str2);
            if (fieldCost < fieldCost2) {
                return -1;
            }
            return fieldCost > fieldCost2 ? 1 : 0;
        }

        /* JADX WARN: Removed duplicated region for block: B:72:0x02a8  */
        /* JADX WARN: Removed duplicated region for block: B:75:0x02b9  */
        /* JADX WARN: Removed duplicated region for block: B:78:0x02d1  */
        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onItemClick(int i) {
            JSONObject jSONObject;
            String str;
            String obj;
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
            String str2 = null;
            if (i == 1) {
                if (PassportActivity.this.getParentActivity() == null) {
                    return;
                }
                TextView textView = new TextView(PassportActivity.this.getParentActivity());
                String string = LocaleController.getString("PassportInfo2", 2131627293);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
                int indexOf = string.indexOf(42);
                int lastIndexOf = string.lastIndexOf(42);
                if (indexOf != -1 && lastIndexOf != -1) {
                    spannableStringBuilder.replace(lastIndexOf, lastIndexOf + 1, (CharSequence) "");
                    spannableStringBuilder.replace(indexOf, indexOf + 1, (CharSequence) "");
                    spannableStringBuilder.setSpan(new AnonymousClass1(LocaleController.getString("PassportInfoUrl", 2131627295)), indexOf, lastIndexOf - 1, 33);
                }
                textView.setText(spannableStringBuilder);
                textView.setTextSize(1, 16.0f);
                textView.setLinkTextColor(Theme.getColor("dialogTextLink"));
                textView.setHighlightColor(Theme.getColor("dialogLinkSelection"));
                textView.setPadding(AndroidUtilities.dp(23.0f), 0, AndroidUtilities.dp(23.0f), 0);
                textView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                textView.setTextColor(Theme.getColor("dialogTextBlack"));
                AlertDialog.Builder builder = new AlertDialog.Builder(PassportActivity.this.getParentActivity());
                builder.setView(textView);
                builder.setTitle(LocaleController.getString("PassportInfoTitle", 2131627294));
                builder.setNegativeButton(LocaleController.getString("Close", 2131625183), null);
                PassportActivity.this.showDialog(builder.create());
            } else if (i != 2) {
            } else {
                if (PassportActivity.this.currentActivityType == 5) {
                    PassportActivity.this.onPasswordDone(false);
                } else if (PassportActivity.this.currentActivityType == 7) {
                    PassportActivity.this.views[PassportActivity.this.currentViewNum].onNextPressed(null);
                } else {
                    PassportActivity$3$$ExternalSyntheticLambda2 passportActivity$3$$ExternalSyntheticLambda2 = new PassportActivity$3$$ExternalSyntheticLambda2(this);
                    AnonymousClass2 anonymousClass2 = new AnonymousClass2(passportActivity$3$$ExternalSyntheticLambda2);
                    if (PassportActivity.this.currentActivityType == 4) {
                        if (PassportActivity.this.useCurrentValue) {
                            obj = PassportActivity.this.currentEmail;
                        } else if (PassportActivity.this.checkFieldsForError()) {
                            return;
                        } else {
                            obj = PassportActivity.this.inputFields[0].getText().toString();
                        }
                        PassportActivity.this.delegate.saveValue(PassportActivity.this.currentType, obj, null, null, null, null, null, null, null, null, passportActivity$3$$ExternalSyntheticLambda2, anonymousClass2);
                    } else if (PassportActivity.this.currentActivityType == 3) {
                        if (PassportActivity.this.useCurrentValue) {
                            str = UserConfig.getInstance(((BaseFragment) PassportActivity.this).currentAccount).getCurrentUser().phone;
                        } else if (PassportActivity.this.checkFieldsForError()) {
                            return;
                        } else {
                            str = PassportActivity.this.inputFields[1].getText().toString() + PassportActivity.this.inputFields[2].getText().toString();
                        }
                        PassportActivity.this.delegate.saveValue(PassportActivity.this.currentType, str, null, null, null, null, null, null, null, null, passportActivity$3$$ExternalSyntheticLambda2, anonymousClass2);
                    } else if (PassportActivity.this.currentActivityType == 2) {
                        if (!PassportActivity.this.uploadingDocuments.isEmpty() || PassportActivity.this.checkFieldsForError()) {
                            return;
                        }
                        if (!PassportActivity.this.isHasNotAnyChanges()) {
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
                                PassportActivityDelegate passportActivityDelegate = PassportActivity.this.delegate;
                                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType = PassportActivity.this.currentType;
                                if (jSONObject != null) {
                                    str2 = jSONObject.toString();
                                }
                                passportActivityDelegate.saveValue(tLRPC$TL_secureRequiredType, null, str2, PassportActivity.this.currentDocumentsType, null, PassportActivity.this.documents, PassportActivity.this.selfieDocument, PassportActivity.this.translationDocuments, null, null, passportActivity$3$$ExternalSyntheticLambda2, anonymousClass2);
                            }
                            jSONObject = null;
                            if (PassportActivity.this.fieldsErrors != null) {
                            }
                            if (PassportActivity.this.documentsErrors != null) {
                            }
                            PassportActivityDelegate passportActivityDelegate2 = PassportActivity.this.delegate;
                            TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2 = PassportActivity.this.currentType;
                            if (jSONObject != null) {
                            }
                            passportActivityDelegate2.saveValue(tLRPC$TL_secureRequiredType2, null, str2, PassportActivity.this.currentDocumentsType, null, PassportActivity.this.documents, PassportActivity.this.selfieDocument, PassportActivity.this.translationDocuments, null, null, passportActivity$3$$ExternalSyntheticLambda2, anonymousClass2);
                        } else {
                            PassportActivity.this.finishFragment();
                            return;
                        }
                    } else if (PassportActivity.this.currentActivityType != 1) {
                        if (PassportActivity.this.currentActivityType == 6) {
                            TLRPC$TL_account_verifyEmail tLRPC$TL_account_verifyEmail = new TLRPC$TL_account_verifyEmail();
                            tLRPC$TL_account_verifyEmail.email = (String) PassportActivity.this.currentValues.get("email");
                            tLRPC$TL_account_verifyEmail.code = PassportActivity.this.inputFields[0].getText().toString();
                            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_verifyEmail, new PassportActivity$3$$ExternalSyntheticLambda6(this, passportActivity$3$$ExternalSyntheticLambda2, anonymousClass2, tLRPC$TL_account_verifyEmail)), ((BaseFragment) PassportActivity.this).classGuid);
                        }
                    } else if (!onIdentityDone(passportActivity$3$$ExternalSyntheticLambda2, anonymousClass2)) {
                        return;
                    }
                    PassportActivity.this.showEditDoneProgress(true, true);
                }
            }
        }

        /* renamed from: org.telegram.ui.PassportActivity$3$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends URLSpanNoUnderline {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(String str) {
                super(str);
                AnonymousClass3.this = r1;
            }

            @Override // org.telegram.ui.Components.URLSpanNoUnderline, android.text.style.URLSpan, android.text.style.ClickableSpan
            public void onClick(View view) {
                PassportActivity.this.dismissCurrentDialog();
                super.onClick(view);
            }
        }

        /* renamed from: org.telegram.ui.PassportActivity$3$2 */
        /* loaded from: classes3.dex */
        class AnonymousClass2 implements ErrorRunnable {
            final /* synthetic */ Runnable val$finishRunnable;

            AnonymousClass2(Runnable runnable) {
                AnonymousClass3.this = r1;
                this.val$finishRunnable = runnable;
            }

            @Override // org.telegram.ui.PassportActivity.ErrorRunnable
            public void onError(String str, String str2) {
                if (!"PHONE_VERIFICATION_NEEDED".equals(str)) {
                    PassportActivity.this.showEditDoneProgress(true, false);
                    return;
                }
                PassportActivity passportActivity = PassportActivity.this;
                passportActivity.startPhoneVerification(true, str2, this.val$finishRunnable, this, passportActivity.delegate);
            }
        }

        public /* synthetic */ void lambda$onItemClick$4() {
            PassportActivity.this.finishFragment();
        }

        public /* synthetic */ void lambda$onItemClick$6(Runnable runnable, ErrorRunnable errorRunnable, TLRPC$TL_account_verifyEmail tLRPC$TL_account_verifyEmail, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new PassportActivity$3$$ExternalSyntheticLambda3(this, tLRPC$TL_error, runnable, errorRunnable, tLRPC$TL_account_verifyEmail));
        }

        public /* synthetic */ void lambda$onItemClick$5(TLRPC$TL_error tLRPC$TL_error, Runnable runnable, ErrorRunnable errorRunnable, TLRPC$TL_account_verifyEmail tLRPC$TL_account_verifyEmail) {
            if (tLRPC$TL_error == null) {
                PassportActivity.this.delegate.saveValue(PassportActivity.this.currentType, (String) PassportActivity.this.currentValues.get("email"), null, null, null, null, null, null, null, null, runnable, errorRunnable);
                return;
            }
            AlertsCreator.processError(((BaseFragment) PassportActivity.this).currentAccount, tLRPC$TL_error, PassportActivity.this, tLRPC$TL_account_verifyEmail, new Object[0]);
            errorRunnable.onError(null, null);
        }
    }

    /* renamed from: org.telegram.ui.PassportActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends ScrollView {
        @Override // android.widget.ScrollView, android.view.ViewGroup
        protected boolean onRequestFocusInDescendants(int i, Rect rect) {
            return false;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context) {
            super(context);
            PassportActivity.this = r1;
        }

        @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
        public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
            if (PassportActivity.this.currentViewNum == 1 || PassportActivity.this.currentViewNum == 2 || PassportActivity.this.currentViewNum == 4) {
                rect.bottom += AndroidUtilities.dp(40.0f);
            }
            return super.requestChildRectangleOnScreen(view, rect, z);
        }

        @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            PassportActivity.this.scrollHeight = View.MeasureSpec.getSize(i2) - AndroidUtilities.dp(30.0f);
            super.onMeasure(i, i2);
        }
    }

    /* renamed from: org.telegram.ui.PassportActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends ScrollView {
        @Override // android.widget.ScrollView, android.view.ViewGroup
        protected boolean onRequestFocusInDescendants(int i, Rect rect) {
            return false;
        }

        AnonymousClass5(PassportActivity passportActivity, Context context) {
            super(context);
        }

        @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
        public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
            rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
            rect.top += AndroidUtilities.dp(20.0f);
            rect.bottom += AndroidUtilities.dp(50.0f);
            return super.requestChildRectangleOnScreen(view, rect, z);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean dismissDialogOnPause(Dialog dialog) {
        return dialog != this.chatAttachAlert && super.dismissDialogOnPause(dialog);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void dismissCurrentDialog() {
        ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (chatAttachAlert != null && this.visibleDialog == chatAttachAlert) {
            chatAttachAlert.getPhotoLayout().closeCamera(false);
            this.chatAttachAlert.dismissInternal();
            this.chatAttachAlert.getPhotoLayout().hideCamera(true);
            return;
        }
        super.dismissCurrentDialog();
    }

    public String getTranslitString(String str) {
        return LocaleController.getInstance().getTranslitString(str, true);
    }

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

    private void createPhoneVerificationInterface(Context context) {
        this.actionBar.setTitle(LocaleController.getString("PassportPhone", 2131627364));
        FrameLayout frameLayout = new FrameLayout(context);
        this.scrollView.addView(frameLayout, LayoutHelper.createScroll(-1, -2, 51));
        for (int i = 0; i < 3; i++) {
            this.views[i] = new PhoneConfirmationView(context, i + 2);
            this.views[i].setVisibility(8);
            SlideView slideView = this.views[i];
            float f = 18.0f;
            float f2 = AndroidUtilities.isTablet() ? 26.0f : 18.0f;
            if (AndroidUtilities.isTablet()) {
                f = 26.0f;
            }
            frameLayout.addView(slideView, LayoutHelper.createFrame(-1, -1.0f, 51, f2, 30.0f, f, 0.0f));
        }
        Bundle bundle = new Bundle();
        bundle.putString("phone", this.currentValues.get("phone"));
        fillNextCodeParams(bundle, this.currentPhoneVerification, false);
    }

    private void loadPasswordInfo() {
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new PassportActivity$$ExternalSyntheticLambda63(this)), this.classGuid);
    }

    public /* synthetic */ void lambda$loadPasswordInfo$4(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda56(this, tLObject));
    }

    public /* synthetic */ void lambda$loadPasswordInfo$3(TLObject tLObject) {
        if (tLObject != null) {
            TLRPC$TL_account_password tLRPC$TL_account_password = (TLRPC$TL_account_password) tLObject;
            this.currentPassword = tLRPC$TL_account_password;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(tLRPC$TL_account_password, false)) {
                AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131628831), true);
                return;
            }
            TwoStepVerificationActivity.initPasswordNewAlgo(this.currentPassword);
            updatePasswordInterface();
            if (this.inputFieldContainers[0].getVisibility() == 0) {
                this.inputFields[0].requestFocus();
                AndroidUtilities.showKeyboard(this.inputFields[0]);
            }
            if (this.usingSavedPassword != 1) {
                return;
            }
            onPasswordDone(true);
        }
    }

    private void createEmailVerificationInterface(Context context) {
        this.actionBar.setTitle(LocaleController.getString("PassportEmail", 2131627277));
        this.inputFields = new EditTextBoldCursor[1];
        for (int i = 0; i < 1; i++) {
            FrameLayout frameLayout = new FrameLayout(context);
            this.linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-1, 50));
            frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.inputFields[i] = new EditTextBoldCursor(context);
            this.inputFields[i].setTag(Integer.valueOf(i));
            this.inputFields[i].setTextSize(1, 16.0f);
            this.inputFields[i].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[i].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setBackgroundDrawable(null);
            this.inputFields[i].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[i].setCursorWidth(1.5f);
            int i2 = 3;
            this.inputFields[i].setInputType(3);
            this.inputFields[i].setImeOptions(268435462);
            this.inputFields[i].setHint(LocaleController.getString("PassportEmailCode", 2131627278));
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            editTextBoldCursorArr[i].setSelection(editTextBoldCursorArr[i].length());
            this.inputFields[i].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
            EditTextBoldCursor editTextBoldCursor = this.inputFields[i];
            if (LocaleController.isRTL) {
                i2 = 5;
            }
            editTextBoldCursor.setGravity(i2);
            frameLayout.addView(this.inputFields[i], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
            this.inputFields[i].setOnEditorActionListener(new PassportActivity$$ExternalSyntheticLambda39(this));
            this.inputFields[i].addTextChangedListener(new AnonymousClass6());
        }
        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
        this.bottomCell = textInfoPrivacyCell;
        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
        this.bottomCell.setText(LocaleController.formatString("PassportEmailVerifyInfo", 2131627281, this.currentValues.get("email")));
        this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
    }

    public /* synthetic */ boolean lambda$createEmailVerificationInterface$5(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6 || i == 5) {
            this.doneItem.callOnClick();
            return true;
        }
        return false;
    }

    /* renamed from: org.telegram.ui.PassportActivity$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass6() {
            PassportActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (!PassportActivity.this.ignoreOnTextChange && PassportActivity.this.emailCodeLength != 0 && PassportActivity.this.inputFields[0].length() == PassportActivity.this.emailCodeLength) {
                PassportActivity.this.doneItem.callOnClick();
            }
        }
    }

    private void createPasswordInterface(Context context) {
        TLRPC$User tLRPC$User;
        if (this.currentForm != null) {
            int i = 0;
            while (true) {
                if (i >= this.currentForm.users.size()) {
                    tLRPC$User = null;
                    break;
                }
                tLRPC$User = this.currentForm.users.get(i);
                if (tLRPC$User.id == this.currentBotId) {
                    break;
                }
                i++;
            }
        } else {
            tLRPC$User = UserConfig.getInstance(this.currentAccount).getCurrentUser();
        }
        this.actionBar.setTitle(LocaleController.getString("TelegramPassport", 2131628630));
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
        backupImageView.setForUserOrChat(tLRPC$User, new AvatarDrawable(tLRPC$User));
        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
        this.passwordRequestTextView = textInfoPrivacyCell;
        textInfoPrivacyCell.getTextView().setGravity(1);
        if (this.currentBotId == 0) {
            this.passwordRequestTextView.setText(LocaleController.getString("PassportSelfRequest", 2131627388));
        } else {
            this.passwordRequestTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("PassportRequest", 2131627374, UserObject.getFirstName(tLRPC$User))));
        }
        ((FrameLayout.LayoutParams) this.passwordRequestTextView.getTextView().getLayoutParams()).gravity = 1;
        int i2 = 5;
        this.linearLayout2.addView(this.passwordRequestTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 21.0f, 0.0f, 21.0f, 0.0f));
        ImageView imageView = new ImageView(context);
        this.noPasswordImageView = imageView;
        imageView.setImageResource(2131166005);
        this.noPasswordImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), PorterDuff.Mode.MULTIPLY));
        this.linearLayout2.addView(this.noPasswordImageView, LayoutHelper.createLinear(-2, -2, 49, 0, 13, 0, 0));
        TextView textView = new TextView(context);
        this.noPasswordTextView = textView;
        textView.setTextSize(1, 14.0f);
        this.noPasswordTextView.setGravity(1);
        this.noPasswordTextView.setPadding(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(21.0f), AndroidUtilities.dp(17.0f));
        this.noPasswordTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
        this.noPasswordTextView.setText(LocaleController.getString("TelegramPassportCreatePasswordInfo", 2131628632));
        this.linearLayout2.addView(this.noPasswordTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 21.0f, 10.0f, 21.0f, 0.0f));
        TextView textView2 = new TextView(context);
        this.noPasswordSetTextView = textView2;
        textView2.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText5"));
        this.noPasswordSetTextView.setGravity(17);
        this.noPasswordSetTextView.setTextSize(1, 16.0f);
        this.noPasswordSetTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.noPasswordSetTextView.setText(LocaleController.getString("TelegramPassportCreatePassword", 2131628631));
        this.linearLayout2.addView(this.noPasswordSetTextView, LayoutHelper.createFrame(-1, 24.0f, (LocaleController.isRTL ? 5 : 3) | 48, 21.0f, 9.0f, 21.0f, 0.0f));
        this.noPasswordSetTextView.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda15(this));
        this.inputFields = new EditTextBoldCursor[1];
        this.inputFieldContainers = new ViewGroup[1];
        for (int i3 = 0; i3 < 1; i3++) {
            this.inputFieldContainers[i3] = new FrameLayout(context);
            this.linearLayout2.addView(this.inputFieldContainers[i3], LayoutHelper.createLinear(-1, 50));
            this.inputFieldContainers[i3].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.inputFields[i3] = new EditTextBoldCursor(context);
            this.inputFields[i3].setTag(Integer.valueOf(i3));
            this.inputFields[i3].setTextSize(1, 16.0f);
            this.inputFields[i3].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[i3].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i3].setBackgroundDrawable(null);
            this.inputFields[i3].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i3].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[i3].setCursorWidth(1.5f);
            this.inputFields[i3].setInputType(129);
            this.inputFields[i3].setMaxLines(1);
            this.inputFields[i3].setLines(1);
            this.inputFields[i3].setSingleLine(true);
            this.inputFields[i3].setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.inputFields[i3].setTypeface(Typeface.DEFAULT);
            this.inputFields[i3].setImeOptions(268435462);
            this.inputFields[i3].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
            this.inputFields[i3].setGravity(LocaleController.isRTL ? 5 : 3);
            this.inputFieldContainers[i3].addView(this.inputFields[i3], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
            this.inputFields[i3].setOnEditorActionListener(new PassportActivity$$ExternalSyntheticLambda40(this));
            this.inputFields[i3].setCustomSelectionActionModeCallback(new AnonymousClass7(this));
        }
        TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
        this.passwordInfoRequestTextView = textInfoPrivacyCell2;
        textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
        this.passwordInfoRequestTextView.setText(LocaleController.formatString("PassportRequestPasswordInfo", 2131627375, new Object[0]));
        this.linearLayout2.addView(this.passwordInfoRequestTextView, LayoutHelper.createLinear(-1, -2));
        TextView textView3 = new TextView(context);
        this.passwordForgotButton = textView3;
        textView3.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
        this.passwordForgotButton.setTextSize(1, 14.0f);
        this.passwordForgotButton.setText(LocaleController.getString("ForgotPassword", 2131625980));
        this.passwordForgotButton.setPadding(0, 0, 0, 0);
        LinearLayout linearLayout = this.linearLayout2;
        TextView textView4 = this.passwordForgotButton;
        if (!LocaleController.isRTL) {
            i2 = 3;
        }
        linearLayout.addView(textView4, LayoutHelper.createLinear(-2, 30, i2 | 48, 21, 0, 21, 0));
        this.passwordForgotButton.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda14(this));
        updatePasswordInterface();
    }

    public /* synthetic */ void lambda$createPasswordInterface$6(View view) {
        TwoStepVerificationSetupActivity twoStepVerificationSetupActivity = new TwoStepVerificationSetupActivity(this.currentAccount, 0, this.currentPassword);
        twoStepVerificationSetupActivity.setCloseAfterSet(true);
        presentFragment(twoStepVerificationSetupActivity);
    }

    public /* synthetic */ boolean lambda$createPasswordInterface$7(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5 || i == 6) {
            this.doneItem.callOnClick();
            return true;
        }
        return false;
    }

    /* renamed from: org.telegram.ui.PassportActivity$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 implements ActionMode.Callback {
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

        AnonymousClass7(PassportActivity passportActivity) {
        }
    }

    public /* synthetic */ void lambda$createPasswordInterface$12(View view) {
        if (this.currentPassword.has_recovery) {
            needShowProgress();
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_auth_requestPasswordRecovery(), new PassportActivity$$ExternalSyntheticLambda62(this), 10), this.classGuid);
        } else if (getParentActivity() == null) {
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setPositiveButton(LocaleController.getString("OK", 2131627127), null);
            builder.setNegativeButton(LocaleController.getString("RestorePasswordResetAccount", 2131628084), new PassportActivity$$ExternalSyntheticLambda2(this));
            builder.setTitle(LocaleController.getString("RestorePasswordNoEmailTitle", 2131628083));
            builder.setMessage(LocaleController.getString("RestorePasswordNoEmailText", 2131628081));
            showDialog(builder.create());
        }
    }

    public /* synthetic */ void lambda$createPasswordInterface$10(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda59(this, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$createPasswordInterface$9(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        String str;
        needHideProgress();
        if (tLRPC$TL_error == null) {
            TLRPC$TL_auth_passwordRecovery tLRPC$TL_auth_passwordRecovery = (TLRPC$TL_auth_passwordRecovery) tLObject;
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setMessage(LocaleController.formatString("RestoreEmailSent", 2131628074, tLRPC$TL_auth_passwordRecovery.email_pattern));
            builder.setTitle(LocaleController.getString("RestoreEmailSentTitle", 2131628076));
            builder.setPositiveButton(LocaleController.getString("OK", 2131627127), new PassportActivity$$ExternalSyntheticLambda7(this, tLRPC$TL_auth_passwordRecovery));
            Dialog showDialog = showDialog(builder.create());
            if (showDialog == null) {
                return;
            }
            showDialog.setCanceledOnTouchOutside(false);
            showDialog.setCancelable(false);
        } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
            int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
            if (intValue < 60) {
                str = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
            } else {
                str = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
            }
            showAlertWithText(LocaleController.getString("AppName", 2131624384), LocaleController.formatString("FloodWaitTime", 2131625950, str));
        } else {
            showAlertWithText(LocaleController.getString("AppName", 2131624384), tLRPC$TL_error.text);
        }
    }

    public /* synthetic */ void lambda$createPasswordInterface$8(TLRPC$TL_auth_passwordRecovery tLRPC$TL_auth_passwordRecovery, DialogInterface dialogInterface, int i) {
        TLRPC$TL_account_password tLRPC$TL_account_password = this.currentPassword;
        tLRPC$TL_account_password.email_unconfirmed_pattern = tLRPC$TL_auth_passwordRecovery.email_pattern;
        presentFragment(new TwoStepVerificationSetupActivity(this.currentAccount, 4, tLRPC$TL_account_password));
    }

    public /* synthetic */ void lambda$createPasswordInterface$11(DialogInterface dialogInterface, int i) {
        Activity parentActivity = getParentActivity();
        Browser.openUrl(parentActivity, "https://telegram.org/deactivate?phone=" + UserConfig.getInstance(this.currentAccount).getClientPhone());
    }

    public void onPasswordDone(boolean z) {
        String str;
        if (z) {
            str = null;
        } else {
            str = this.inputFields[0].getText().toString();
            if (TextUtils.isEmpty(str)) {
                onPasscodeError(false);
                return;
            }
            showEditDoneProgress(true, true);
        }
        Utilities.globalQueue.postRunnable(new PassportActivity$$ExternalSyntheticLambda61(this, z, str));
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x006a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$onPasswordDone$13(boolean z, String str) {
        byte[] bArr;
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo;
        byte[] x;
        TLRPC$TL_account_getPasswordSettings tLRPC$TL_account_getPasswordSettings = new TLRPC$TL_account_getPasswordSettings();
        if (z) {
            x = this.savedPasswordHash;
        } else if (this.currentPassword.current_algo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            x = SRPHelper.getX(AndroidUtilities.getStringBytes(str), (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) this.currentPassword.current_algo);
        } else {
            bArr = null;
            AnonymousClass8 anonymousClass8 = new AnonymousClass8(z, bArr, tLRPC$TL_account_getPasswordSettings, str);
            TLRPC$TL_account_password tLRPC$TL_account_password = this.currentPassword;
            tLRPC$PasswordKdfAlgo = tLRPC$TL_account_password.current_algo;
            if (!(tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)) {
                TLRPC$TL_inputCheckPasswordSRP startCheck = SRPHelper.startCheck(bArr, tLRPC$TL_account_password.srp_id, tLRPC$TL_account_password.srp_B, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo);
                tLRPC$TL_account_getPasswordSettings.password = startCheck;
                if (startCheck == null) {
                    TLRPC$TL_error tLRPC$TL_error = new TLRPC$TL_error();
                    tLRPC$TL_error.text = "ALGO_INVALID";
                    anonymousClass8.run(null, tLRPC$TL_error);
                    return;
                }
                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getPasswordSettings, anonymousClass8, 10), this.classGuid);
                return;
            }
            TLRPC$TL_error tLRPC$TL_error2 = new TLRPC$TL_error();
            tLRPC$TL_error2.text = "PASSWORD_HASH_INVALID";
            anonymousClass8.run(null, tLRPC$TL_error2);
            return;
        }
        bArr = x;
        AnonymousClass8 anonymousClass82 = new AnonymousClass8(z, bArr, tLRPC$TL_account_getPasswordSettings, str);
        TLRPC$TL_account_password tLRPC$TL_account_password2 = this.currentPassword;
        tLRPC$PasswordKdfAlgo = tLRPC$TL_account_password2.current_algo;
        if (!(tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)) {
        }
    }

    /* renamed from: org.telegram.ui.PassportActivity$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 implements RequestDelegate {
        final /* synthetic */ TLRPC$TL_account_getPasswordSettings val$req;
        final /* synthetic */ boolean val$saved;
        final /* synthetic */ String val$textPassword;
        final /* synthetic */ byte[] val$x_bytes;

        AnonymousClass8(boolean z, byte[] bArr, TLRPC$TL_account_getPasswordSettings tLRPC$TL_account_getPasswordSettings, String str) {
            PassportActivity.this = r1;
            this.val$saved = z;
            this.val$x_bytes = bArr;
            this.val$req = tLRPC$TL_account_getPasswordSettings;
            this.val$textPassword = str;
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
            if (((BaseFragment) PassportActivity.this).parentLayout != null && ((BaseFragment) PassportActivity.this).parentLayout.checkTransitionAnimation()) {
                PassportActivity.this.presentAfterAnimation = passportActivity;
            } else {
                PassportActivity.this.presentFragment(passportActivity, true);
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
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(this.val$req, new PassportActivity$8$$ExternalSyntheticLambda13(this));
        }

        public /* synthetic */ void lambda$resetSecret$3(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new PassportActivity$8$$ExternalSyntheticLambda4(this, tLRPC$TL_error));
        }

        public /* synthetic */ void lambda$resetSecret$2(TLRPC$TL_error tLRPC$TL_error) {
            if (tLRPC$TL_error != null && "SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
                ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new PassportActivity$8$$ExternalSyntheticLambda12(this), 8);
                return;
            }
            generateNewSecret();
        }

        public /* synthetic */ void lambda$resetSecret$1(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new PassportActivity$8$$ExternalSyntheticLambda7(this, tLRPC$TL_error, tLObject));
        }

        public /* synthetic */ void lambda$resetSecret$0(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            if (tLRPC$TL_error == null) {
                PassportActivity.this.currentPassword = (TLRPC$TL_account_password) tLObject;
                TwoStepVerificationActivity.initPasswordNewAlgo(PassportActivity.this.currentPassword);
                resetSecret();
            }
        }

        private void generateNewSecret() {
            Utilities.globalQueue.postRunnable(new PassportActivity$8$$ExternalSyntheticLambda10(this, this.val$x_bytes, this.val$textPassword));
        }

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
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_updatePasswordSettings, new PassportActivity$8$$ExternalSyntheticLambda11(this));
        }

        public /* synthetic */ void lambda$generateNewSecret$7(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new PassportActivity$8$$ExternalSyntheticLambda5(this, tLRPC$TL_error));
        }

        public /* synthetic */ void lambda$generateNewSecret$6(TLRPC$TL_error tLRPC$TL_error) {
            if (tLRPC$TL_error == null || !"SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
                if (PassportActivity.this.currentForm == null) {
                    PassportActivity.this.currentForm = new TLRPC$TL_account_authorizationForm();
                }
                openRequestInterface();
                return;
            }
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new PassportActivity$8$$ExternalSyntheticLambda15(this), 8);
        }

        public /* synthetic */ void lambda$generateNewSecret$5(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new PassportActivity$8$$ExternalSyntheticLambda6(this, tLRPC$TL_error, tLObject));
        }

        public /* synthetic */ void lambda$generateNewSecret$4(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            if (tLRPC$TL_error == null) {
                PassportActivity.this.currentPassword = (TLRPC$TL_account_password) tLObject;
                TwoStepVerificationActivity.initPasswordNewAlgo(PassportActivity.this.currentPassword);
                generateNewSecret();
            }
        }

        @Override // org.telegram.tgnet.RequestDelegate
        public void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            if (tLRPC$TL_error != null && "SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
                ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new PassportActivity$8$$ExternalSyntheticLambda16(this, this.val$saved), 8);
            } else if (tLRPC$TL_error == null) {
                Utilities.globalQueue.postRunnable(new PassportActivity$8$$ExternalSyntheticLambda1(this, tLObject, this.val$textPassword, this.val$saved));
            } else {
                AndroidUtilities.runOnUIThread(new PassportActivity$8$$ExternalSyntheticLambda9(this, this.val$saved, tLRPC$TL_error));
            }
        }

        public /* synthetic */ void lambda$run$10(boolean z, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new PassportActivity$8$$ExternalSyntheticLambda8(this, tLRPC$TL_error, tLObject, z));
        }

        public /* synthetic */ void lambda$run$9(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, boolean z) {
            if (tLRPC$TL_error == null) {
                PassportActivity.this.currentPassword = (TLRPC$TL_account_password) tLObject;
                TwoStepVerificationActivity.initPasswordNewAlgo(PassportActivity.this.currentPassword);
                PassportActivity.this.onPasswordDone(z);
            }
        }

        public /* synthetic */ void lambda$run$15(TLObject tLObject, String str, boolean z) {
            byte[] bArr;
            TLRPC$TL_account_passwordSettings tLRPC$TL_account_passwordSettings = (TLRPC$TL_account_passwordSettings) tLObject;
            TLRPC$TL_secureSecretSettings tLRPC$TL_secureSecretSettings = tLRPC$TL_account_passwordSettings.secure_settings;
            if (tLRPC$TL_secureSecretSettings == null) {
                if (PassportActivity.this.currentPassword.new_secure_algo instanceof TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
                    TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 = (TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) PassportActivity.this.currentPassword.new_secure_algo;
                    byte[] bArr2 = tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt;
                    PassportActivity.this.saltedPassword = Utilities.computePBKDF2(AndroidUtilities.getStringBytes(str), tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt);
                    bArr = bArr2;
                } else {
                    bArr = new byte[0];
                }
                PassportActivity.this.secureSecret = null;
                PassportActivity.this.secureSecretId = 0L;
            } else {
                PassportActivity.this.secureSecret = tLRPC$TL_secureSecretSettings.secure_secret;
                PassportActivity.this.secureSecretId = tLRPC$TL_account_passwordSettings.secure_settings.secure_secret_id;
                TLRPC$SecurePasswordKdfAlgo tLRPC$SecurePasswordKdfAlgo = tLRPC$TL_account_passwordSettings.secure_settings.secure_algo;
                if (tLRPC$SecurePasswordKdfAlgo instanceof TLRPC$TL_securePasswordKdfAlgoSHA512) {
                    bArr = ((TLRPC$TL_securePasswordKdfAlgoSHA512) tLRPC$SecurePasswordKdfAlgo).salt;
                    PassportActivity.this.saltedPassword = Utilities.computeSHA512(bArr, AndroidUtilities.getStringBytes(str), bArr);
                } else if (tLRPC$SecurePasswordKdfAlgo instanceof TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
                    TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter1000002 = (TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) tLRPC$SecurePasswordKdfAlgo;
                    byte[] bArr3 = tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter1000002.salt;
                    PassportActivity.this.saltedPassword = Utilities.computePBKDF2(AndroidUtilities.getStringBytes(str), tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter1000002.salt);
                    bArr = bArr3;
                } else if (tLRPC$SecurePasswordKdfAlgo instanceof TLRPC$TL_securePasswordKdfAlgoUnknown) {
                    AndroidUtilities.runOnUIThread(new PassportActivity$8$$ExternalSyntheticLambda0(this));
                    return;
                } else {
                    bArr = new byte[0];
                }
            }
            AndroidUtilities.runOnUIThread(new PassportActivity$8$$ExternalSyntheticLambda3(this, tLRPC$TL_account_passwordSettings, z, bArr));
        }

        public /* synthetic */ void lambda$run$11() {
            AlertsCreator.showUpdateAppAlert(PassportActivity.this.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131628831), true);
        }

        public /* synthetic */ void lambda$run$14(TLRPC$TL_account_passwordSettings tLRPC$TL_account_passwordSettings, boolean z, byte[] bArr) {
            PassportActivity.this.currentEmail = tLRPC$TL_account_passwordSettings.email;
            if (z) {
                PassportActivity passportActivity = PassportActivity.this;
                passportActivity.saltedPassword = passportActivity.savedSaltedPassword;
            }
            PassportActivity passportActivity2 = PassportActivity.this;
            if (PassportActivity.checkSecret(passportActivity2.decryptSecret(passportActivity2.secureSecret, PassportActivity.this.saltedPassword), Long.valueOf(PassportActivity.this.secureSecretId)) && bArr.length != 0 && PassportActivity.this.secureSecretId != 0) {
                if (PassportActivity.this.currentBotId == 0) {
                    ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getAllSecureValues(), new PassportActivity$8$$ExternalSyntheticLambda14(this));
                    return;
                }
                openRequestInterface();
            } else if (z) {
                UserConfig.getInstance(((BaseFragment) PassportActivity.this).currentAccount).resetSavedPassword();
                PassportActivity.this.usingSavedPassword = 0;
                PassportActivity.this.updatePasswordInterface();
            } else {
                if (PassportActivity.this.currentForm != null) {
                    PassportActivity.this.currentForm.values.clear();
                    PassportActivity.this.currentForm.errors.clear();
                }
                if (PassportActivity.this.secureSecret == null || PassportActivity.this.secureSecret.length == 0) {
                    generateNewSecret();
                } else {
                    resetSecret();
                }
            }
        }

        public /* synthetic */ void lambda$run$13(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new PassportActivity$8$$ExternalSyntheticLambda2(this, tLObject, tLRPC$TL_error));
        }

        public /* synthetic */ void lambda$run$12(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            if (tLObject != null) {
                PassportActivity.this.currentForm = new TLRPC$TL_account_authorizationForm();
                TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
                int size = tLRPC$Vector.objects.size();
                for (int i = 0; i < size; i++) {
                    PassportActivity.this.currentForm.values.add((TLRPC$TL_secureValue) tLRPC$Vector.objects.get(i));
                }
                openRequestInterface();
                return;
            }
            if (!"APP_VERSION_OUTDATED".equals(tLRPC$TL_error.text)) {
                PassportActivity.this.showAlertWithText(LocaleController.getString("AppName", 2131624384), tLRPC$TL_error.text);
            } else {
                AlertsCreator.showUpdateAppAlert(PassportActivity.this.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131628831), true);
            }
            PassportActivity.this.showEditDoneProgress(true, false);
        }

        public /* synthetic */ void lambda$run$16(boolean z, TLRPC$TL_error tLRPC$TL_error) {
            String str;
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
                PassportActivity.this.showAlertWithText(LocaleController.getString("AppName", 2131624384), tLRPC$TL_error.text);
            } else {
                int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                if (intValue < 60) {
                    str = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
                } else {
                    str = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                }
                PassportActivity.this.showAlertWithText(LocaleController.getString("AppName", 2131624384), LocaleController.formatString("FloodWaitTime", 2131625950, str));
            }
        }
    }

    private boolean isPersonalDocument(TLRPC$SecureValueType tLRPC$SecureValueType) {
        return (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard);
    }

    private boolean isAddressDocument(TLRPC$SecureValueType tLRPC$SecureValueType) {
        return (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement);
    }

    /* JADX WARN: Removed duplicated region for block: B:123:0x02f0  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x02f2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void createRequestInterface(Context context) {
        TLRPC$User tLRPC$User;
        ArrayList<TLRPC$TL_secureRequiredType> arrayList;
        int i;
        boolean z;
        ArrayList<TLRPC$TL_secureRequiredType> arrayList2;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2;
        ArrayList<TLRPC$TL_secureRequiredType> arrayList3;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3;
        ArrayList<TLRPC$TL_secureRequiredType> arrayList4;
        ArrayList<TLRPC$TL_secureRequiredType> arrayList5;
        int i2 = 0;
        if (this.currentForm != null) {
            for (int i3 = 0; i3 < this.currentForm.users.size(); i3++) {
                TLRPC$User tLRPC$User2 = this.currentForm.users.get(i3);
                if (tLRPC$User2.id == this.currentBotId) {
                    tLRPC$User = tLRPC$User2;
                    break;
                }
            }
        }
        tLRPC$User = null;
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        this.actionBar.setTitle(LocaleController.getString("TelegramPassport", 2131628630));
        this.actionBar.createMenu().addItem(1, 2131165764);
        if (tLRPC$User != null) {
            FrameLayout frameLayout2 = new FrameLayout(context);
            this.linearLayout2.addView(frameLayout2, LayoutHelper.createLinear(-1, 100));
            BackupImageView backupImageView = new BackupImageView(context);
            backupImageView.setRoundRadius(AndroidUtilities.dp(32.0f));
            frameLayout2.addView(backupImageView, LayoutHelper.createFrame(64, 64.0f, 17, 0.0f, 8.0f, 0.0f, 0.0f));
            backupImageView.setForUserOrChat(tLRPC$User, new AvatarDrawable(tLRPC$User));
            TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell;
            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165437, "windowBackgroundGrayShadow"));
            this.bottomCell.setText(AndroidUtilities.replaceTags(LocaleController.formatString("PassportRequest", 2131627374, UserObject.getFirstName(tLRPC$User))));
            this.bottomCell.getTextView().setGravity(1);
            ((FrameLayout.LayoutParams) this.bottomCell.getTextView().getLayoutParams()).gravity = 1;
            this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
        }
        HeaderCell headerCell = new HeaderCell(context);
        this.headerCell = headerCell;
        headerCell.setText(LocaleController.getString("PassportRequestedInformation", 2131627376));
        this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
        TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm = this.currentForm;
        if (tLRPC$TL_account_authorizationForm != null) {
            int size = tLRPC$TL_account_authorizationForm.required_types.size();
            ArrayList<TLRPC$TL_secureRequiredType> arrayList6 = new ArrayList<>();
            ArrayList<TLRPC$TL_secureRequiredType> arrayList7 = new ArrayList<>();
            int i4 = 0;
            boolean z2 = false;
            int i5 = 0;
            int i6 = 0;
            boolean z3 = false;
            while (i4 < size) {
                TLRPC$SecureRequiredType tLRPC$SecureRequiredType = this.currentForm.required_types.get(i4);
                if (tLRPC$SecureRequiredType instanceof TLRPC$TL_secureRequiredType) {
                    TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType4 = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType;
                    if (isPersonalDocument(tLRPC$TL_secureRequiredType4.type)) {
                        arrayList6.add(tLRPC$TL_secureRequiredType4);
                        i5++;
                        arrayList5 = arrayList6;
                        i4++;
                        arrayList6 = arrayList5;
                        i2 = 0;
                    } else {
                        if (isAddressDocument(tLRPC$TL_secureRequiredType4.type)) {
                            arrayList7.add(tLRPC$TL_secureRequiredType4);
                            i6++;
                        } else {
                            TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_secureRequiredType4.type;
                            if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                                z2 = true;
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
                                z3 = true;
                            }
                        }
                        arrayList5 = arrayList6;
                        i4++;
                        arrayList6 = arrayList5;
                        i2 = 0;
                    }
                } else {
                    if (tLRPC$SecureRequiredType instanceof TLRPC$TL_secureRequiredTypeOneOf) {
                        TLRPC$TL_secureRequiredTypeOneOf tLRPC$TL_secureRequiredTypeOneOf = (TLRPC$TL_secureRequiredTypeOneOf) tLRPC$SecureRequiredType;
                        if (!tLRPC$TL_secureRequiredTypeOneOf.types.isEmpty()) {
                            TLRPC$SecureRequiredType tLRPC$SecureRequiredType2 = tLRPC$TL_secureRequiredTypeOneOf.types.get(i2);
                            if (tLRPC$SecureRequiredType2 instanceof TLRPC$TL_secureRequiredType) {
                                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType5 = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType2;
                                if (isPersonalDocument(tLRPC$TL_secureRequiredType5.type)) {
                                    int size2 = tLRPC$TL_secureRequiredTypeOneOf.types.size();
                                    int i7 = 0;
                                    while (i7 < size2) {
                                        TLRPC$SecureRequiredType tLRPC$SecureRequiredType3 = tLRPC$TL_secureRequiredTypeOneOf.types.get(i7);
                                        int i8 = size2;
                                        if (tLRPC$SecureRequiredType3 instanceof TLRPC$TL_secureRequiredType) {
                                            arrayList6.add((TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType3);
                                        }
                                        i7++;
                                        size2 = i8;
                                    }
                                    i5++;
                                } else if (isAddressDocument(tLRPC$TL_secureRequiredType5.type)) {
                                    int size3 = tLRPC$TL_secureRequiredTypeOneOf.types.size();
                                    int i9 = 0;
                                    while (i9 < size3) {
                                        TLRPC$SecureRequiredType tLRPC$SecureRequiredType4 = tLRPC$TL_secureRequiredTypeOneOf.types.get(i9);
                                        ArrayList<TLRPC$TL_secureRequiredType> arrayList8 = arrayList6;
                                        if (tLRPC$SecureRequiredType4 instanceof TLRPC$TL_secureRequiredType) {
                                            arrayList7.add((TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType4);
                                        }
                                        i9++;
                                        arrayList6 = arrayList8;
                                    }
                                    arrayList5 = arrayList6;
                                    i6++;
                                    i4++;
                                    arrayList6 = arrayList5;
                                    i2 = 0;
                                }
                            }
                        }
                    }
                    arrayList5 = arrayList6;
                    i4++;
                    arrayList6 = arrayList5;
                    i2 = 0;
                }
            }
            ArrayList<TLRPC$TL_secureRequiredType> arrayList9 = arrayList6;
            boolean z4 = !z2 || i5 > 1;
            boolean z5 = !z3 || i6 > 1;
            int i10 = 0;
            while (i10 < size) {
                TLRPC$SecureRequiredType tLRPC$SecureRequiredType5 = this.currentForm.required_types.get(i10);
                if (tLRPC$SecureRequiredType5 instanceof TLRPC$TL_secureRequiredType) {
                    TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType6 = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType5;
                    TLRPC$SecureValueType tLRPC$SecureValueType2 = tLRPC$TL_secureRequiredType6.type;
                    if ((tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypePhone) || (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeEmail)) {
                        tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType6;
                        arrayList2 = null;
                    } else if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                        if (!z4) {
                            arrayList4 = arrayList9;
                            tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType6;
                            arrayList2 = arrayList4;
                        }
                        arrayList4 = null;
                        tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType6;
                        arrayList2 = arrayList4;
                    } else if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeAddress) {
                        if (!z5) {
                            arrayList4 = arrayList7;
                            tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType6;
                            arrayList2 = arrayList4;
                        }
                        arrayList4 = null;
                        tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType6;
                        arrayList2 = arrayList4;
                    } else {
                        if (z4 && isPersonalDocument(tLRPC$SecureValueType2)) {
                            arrayList3 = new ArrayList<>();
                            arrayList3.add(tLRPC$TL_secureRequiredType6);
                            tLRPC$TL_secureRequiredType3 = new TLRPC$TL_secureRequiredType();
                            tLRPC$TL_secureRequiredType3.type = new TLRPC$TL_secureValueTypePersonalDetails();
                        } else {
                            if (z5 && isAddressDocument(tLRPC$TL_secureRequiredType6.type)) {
                                arrayList3 = new ArrayList<>();
                                arrayList3.add(tLRPC$TL_secureRequiredType6);
                                tLRPC$TL_secureRequiredType3 = new TLRPC$TL_secureRequiredType();
                                tLRPC$TL_secureRequiredType3.type = new TLRPC$TL_secureValueTypeAddress();
                            }
                            arrayList = arrayList7;
                            i = size;
                        }
                        tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType3;
                        arrayList2 = arrayList3;
                        z = true;
                        arrayList = arrayList7;
                        ArrayList<TLRPC$TL_secureRequiredType> arrayList10 = arrayList2;
                        boolean z6 = z;
                        i = size;
                        addField(context, tLRPC$TL_secureRequiredType, arrayList10, z6, i10 == size + (-1));
                    }
                    z = false;
                    arrayList = arrayList7;
                    ArrayList<TLRPC$TL_secureRequiredType> arrayList102 = arrayList2;
                    boolean z62 = z;
                    i = size;
                    addField(context, tLRPC$TL_secureRequiredType, arrayList102, z62, i10 == size + (-1));
                } else {
                    if (tLRPC$SecureRequiredType5 instanceof TLRPC$TL_secureRequiredTypeOneOf) {
                        TLRPC$TL_secureRequiredTypeOneOf tLRPC$TL_secureRequiredTypeOneOf2 = (TLRPC$TL_secureRequiredTypeOneOf) tLRPC$SecureRequiredType5;
                        if (!tLRPC$TL_secureRequiredTypeOneOf2.types.isEmpty()) {
                            TLRPC$SecureRequiredType tLRPC$SecureRequiredType6 = tLRPC$TL_secureRequiredTypeOneOf2.types.get(0);
                            if (tLRPC$SecureRequiredType6 instanceof TLRPC$TL_secureRequiredType) {
                                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType7 = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType6;
                                if ((z4 && isPersonalDocument(tLRPC$TL_secureRequiredType7.type)) || (z5 && isAddressDocument(tLRPC$TL_secureRequiredType7.type))) {
                                    ArrayList<TLRPC$TL_secureRequiredType> arrayList11 = new ArrayList<>();
                                    int size4 = tLRPC$TL_secureRequiredTypeOneOf2.types.size();
                                    int i11 = 0;
                                    while (i11 < size4) {
                                        TLRPC$SecureRequiredType tLRPC$SecureRequiredType7 = tLRPC$TL_secureRequiredTypeOneOf2.types.get(i11);
                                        TLRPC$TL_secureRequiredTypeOneOf tLRPC$TL_secureRequiredTypeOneOf3 = tLRPC$TL_secureRequiredTypeOneOf2;
                                        if (tLRPC$SecureRequiredType7 instanceof TLRPC$TL_secureRequiredType) {
                                            arrayList11.add((TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType7);
                                        }
                                        i11++;
                                        tLRPC$TL_secureRequiredTypeOneOf2 = tLRPC$TL_secureRequiredTypeOneOf3;
                                    }
                                    if (isPersonalDocument(tLRPC$TL_secureRequiredType7.type)) {
                                        tLRPC$TL_secureRequiredType2 = new TLRPC$TL_secureRequiredType();
                                        tLRPC$TL_secureRequiredType2.type = new TLRPC$TL_secureValueTypePersonalDetails();
                                    } else {
                                        tLRPC$TL_secureRequiredType2 = new TLRPC$TL_secureRequiredType();
                                        tLRPC$TL_secureRequiredType2.type = new TLRPC$TL_secureValueTypeAddress();
                                    }
                                    arrayList2 = arrayList11;
                                    z = true;
                                    tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType2;
                                    arrayList = arrayList7;
                                    ArrayList<TLRPC$TL_secureRequiredType> arrayList1022 = arrayList2;
                                    boolean z622 = z;
                                    i = size;
                                    addField(context, tLRPC$TL_secureRequiredType, arrayList1022, z622, i10 == size + (-1));
                                }
                            }
                        }
                    }
                    arrayList = arrayList7;
                    i = size;
                }
                i10++;
                size = i;
                arrayList7 = arrayList;
            }
        }
        if (tLRPC$User != null) {
            TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell2;
            textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
            this.bottomCell.setLinkTextColorKey("windowBackgroundWhiteGrayText4");
            if (!TextUtils.isEmpty(this.currentForm.privacy_policy_url)) {
                String formatString = LocaleController.formatString("PassportPolicy", 2131627371, UserObject.getFirstName(tLRPC$User), tLRPC$User.username);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(formatString);
                int indexOf = formatString.indexOf(42);
                int lastIndexOf = formatString.lastIndexOf(42);
                if (indexOf != -1 && lastIndexOf != -1) {
                    this.bottomCell.getTextView().setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                    spannableStringBuilder.replace(lastIndexOf, lastIndexOf + 1, (CharSequence) "");
                    spannableStringBuilder.replace(indexOf, indexOf + 1, (CharSequence) "");
                    spannableStringBuilder.setSpan(new LinkSpan(), indexOf, lastIndexOf - 1, 33);
                }
                this.bottomCell.setText(spannableStringBuilder);
            } else {
                this.bottomCell.setText(AndroidUtilities.replaceTags(LocaleController.formatString("PassportNoPolicy", 2131627359, UserObject.getFirstName(tLRPC$User), tLRPC$User.username)));
            }
            this.bottomCell.getTextView().setHighlightColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
            this.bottomCell.getTextView().setGravity(1);
            this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
        }
        FrameLayout frameLayout3 = new FrameLayout(context);
        this.bottomLayout = frameLayout3;
        frameLayout3.setBackgroundDrawable(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("passport_authorizeBackground"), Theme.getColor("passport_authorizeBackgroundSelected")));
        frameLayout.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 80));
        this.bottomLayout.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda28(this));
        TextView textView = new TextView(context);
        this.acceptTextView = textView;
        textView.setCompoundDrawablePadding(AndroidUtilities.dp(8.0f));
        this.acceptTextView.setCompoundDrawablesWithIntrinsicBounds(2131165266, 0, 0, 0);
        this.acceptTextView.setTextColor(Theme.getColor("passport_authorizeText"));
        this.acceptTextView.setText(LocaleController.getString("PassportAuthorize", 2131627253));
        this.acceptTextView.setTextSize(1, 14.0f);
        this.acceptTextView.setGravity(17);
        this.acceptTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.bottomLayout.addView(this.acceptTextView, LayoutHelper.createFrame(-2, -1, 17));
        ContextProgressView contextProgressView = new ContextProgressView(context, 0);
        this.progressViewButton = contextProgressView;
        contextProgressView.setVisibility(4);
        this.bottomLayout.addView(this.progressViewButton, LayoutHelper.createFrame(-1, -1.0f));
        View view = new View(context);
        view.setBackgroundResource(2131165447);
        frameLayout.addView(view, LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
    }

    /* renamed from: org.telegram.ui.PassportActivity$1ValueToSend */
    /* loaded from: classes3.dex */
    public class C1ValueToSend {
        boolean selfie_required;
        boolean translation_required;
        TLRPC$TL_secureValue value;

        public C1ValueToSend(PassportActivity passportActivity, TLRPC$TL_secureValue tLRPC$TL_secureValue, boolean z, boolean z2) {
            this.value = tLRPC$TL_secureValue;
            this.selfie_required = z;
            this.translation_required = z2;
        }
    }

    public /* synthetic */ void lambda$createRequestInterface$16(View view) {
        int i;
        ArrayList arrayList;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType;
        ArrayList arrayList2 = new ArrayList();
        int size = this.currentForm.required_types.size();
        for (int i2 = 0; i2 < size; i2++) {
            TLRPC$SecureRequiredType tLRPC$SecureRequiredType = this.currentForm.required_types.get(i2);
            if (tLRPC$SecureRequiredType instanceof TLRPC$TL_secureRequiredType) {
                tLRPC$TL_secureRequiredType = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType;
            } else {
                if (tLRPC$SecureRequiredType instanceof TLRPC$TL_secureRequiredTypeOneOf) {
                    TLRPC$TL_secureRequiredTypeOneOf tLRPC$TL_secureRequiredTypeOneOf = (TLRPC$TL_secureRequiredTypeOneOf) tLRPC$SecureRequiredType;
                    if (!tLRPC$TL_secureRequiredTypeOneOf.types.isEmpty()) {
                        TLRPC$SecureRequiredType tLRPC$SecureRequiredType2 = tLRPC$TL_secureRequiredTypeOneOf.types.get(0);
                        if (tLRPC$SecureRequiredType2 instanceof TLRPC$TL_secureRequiredType) {
                            TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2 = (TLRPC$TL_secureRequiredType) tLRPC$SecureRequiredType2;
                            int size2 = tLRPC$TL_secureRequiredTypeOneOf.types.size();
                            int i3 = 0;
                            while (true) {
                                if (i3 >= size2) {
                                    tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType2;
                                    break;
                                }
                                TLRPC$SecureRequiredType tLRPC$SecureRequiredType3 = tLRPC$TL_secureRequiredTypeOneOf.types.get(i3);
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
                AndroidUtilities.shakeView(getViewByType(tLRPC$TL_secureRequiredType), 2.0f, 0);
                return;
            }
            HashMap<String, String> hashMap = this.errorsMap.get(getNameForType(tLRPC$TL_secureRequiredType.type));
            if (hashMap != null && !hashMap.isEmpty()) {
                Vibrator vibrator2 = (Vibrator) getParentActivity().getSystemService("vibrator");
                if (vibrator2 != null) {
                    vibrator2.vibrate(200L);
                }
                AndroidUtilities.shakeView(getViewByType(tLRPC$TL_secureRequiredType), 2.0f, 0);
                return;
            }
            arrayList2.add(new C1ValueToSend(this, valueByType, tLRPC$TL_secureRequiredType.selfie_required, tLRPC$TL_secureRequiredType.translation_required));
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
            C1ValueToSend c1ValueToSend = (C1ValueToSend) arrayList2.get(i4);
            TLRPC$TL_secureValue tLRPC$TL_secureValue = c1ValueToSend.value;
            JSONObject jSONObject2 = new JSONObject();
            TLRPC$SecurePlainData tLRPC$SecurePlainData = tLRPC$TL_secureValue.plain_data;
            if (tLRPC$SecurePlainData != null) {
                if (tLRPC$SecurePlainData instanceof TLRPC$TL_securePlainEmail) {
                    TLRPC$TL_securePlainEmail tLRPC$TL_securePlainEmail = (TLRPC$TL_securePlainEmail) tLRPC$SecurePlainData;
                } else if (tLRPC$SecurePlainData instanceof TLRPC$TL_securePlainPhone) {
                    TLRPC$TL_securePlainPhone tLRPC$TL_securePlainPhone = (TLRPC$TL_securePlainPhone) tLRPC$SecurePlainData;
                }
            } else {
                try {
                    JSONObject jSONObject3 = new JSONObject();
                    TLRPC$TL_secureData tLRPC$TL_secureData = tLRPC$TL_secureValue.data;
                    if (tLRPC$TL_secureData != null) {
                        byte[] decryptValueSecret = decryptValueSecret(tLRPC$TL_secureData.secret, tLRPC$TL_secureData.data_hash);
                        jSONObject2.put("data_hash", Base64.encodeToString(tLRPC$TL_secureValue.data.data_hash, 2));
                        jSONObject2.put("secret", Base64.encodeToString(decryptValueSecret, 2));
                        jSONObject3.put("data", jSONObject2);
                    }
                    if (!tLRPC$TL_secureValue.files.isEmpty()) {
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
                    } else {
                        arrayList = arrayList2;
                        i = size3;
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
                    if (c1ValueToSend.selfie_required) {
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
                    if (c1ValueToSend.translation_required && !tLRPC$TL_secureValue.translation.isEmpty()) {
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
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_acceptAuthorization, new PassportActivity$$ExternalSyntheticLambda65(this)), this.classGuid);
    }

    public /* synthetic */ void lambda$createRequestInterface$15(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda57(this, tLRPC$TL_error));
    }

    public /* synthetic */ void lambda$createRequestInterface$14(TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            this.ignoreOnFailure = true;
            callCallback(true);
            finishFragment();
            return;
        }
        showEditDoneProgress(false, false);
        if ("APP_VERSION_OUTDATED".equals(tLRPC$TL_error.text)) {
            AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131628831), true);
        } else {
            showAlertWithText(LocaleController.getString("AppName", 2131624384), tLRPC$TL_error.text);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x02cc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x02ce  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void createManageInterface(Context context) {
        boolean z;
        ArrayList<TLRPC$TL_secureRequiredType> arrayList;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType;
        ArrayList<TLRPC$TL_secureRequiredType> arrayList2;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2;
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        this.actionBar.setTitle(LocaleController.getString("TelegramPassport", 2131628630));
        this.actionBar.createMenu().addItem(1, 2131165764);
        HeaderCell headerCell = new HeaderCell(context);
        this.headerCell = headerCell;
        headerCell.setText(LocaleController.getString("PassportProvidedInformation", 2131627373));
        this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
        ShadowSectionCell shadowSectionCell = new ShadowSectionCell(context);
        this.sectionCell = shadowSectionCell;
        shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165435, "windowBackgroundGrayShadow"));
        this.linearLayout2.addView(this.sectionCell, LayoutHelper.createLinear(-1, -2));
        TextSettingsCell textSettingsCell = new TextSettingsCell(context);
        this.addDocumentCell = textSettingsCell;
        textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.addDocumentCell.setText(LocaleController.getString("PassportNoDocumentsAdd", 2131627356), true);
        this.linearLayout2.addView(this.addDocumentCell, LayoutHelper.createLinear(-1, -2));
        this.addDocumentCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda17(this));
        TextSettingsCell textSettingsCell2 = new TextSettingsCell(context);
        this.deletePassportCell = textSettingsCell2;
        textSettingsCell2.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
        this.deletePassportCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.deletePassportCell.setText(LocaleController.getString("TelegramPassportDelete", 2131628633), false);
        this.linearLayout2.addView(this.deletePassportCell, LayoutHelper.createLinear(-1, -2));
        this.deletePassportCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda18(this));
        ShadowSectionCell shadowSectionCell2 = new ShadowSectionCell(context);
        this.addDocumentSectionCell = shadowSectionCell2;
        shadowSectionCell2.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
        this.linearLayout2.addView(this.addDocumentSectionCell, LayoutHelper.createLinear(-1, -2));
        LinearLayout linearLayout = new LinearLayout(context);
        this.emptyLayout = linearLayout;
        linearLayout.setOrientation(1);
        this.emptyLayout.setGravity(17);
        this.emptyLayout.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
        if (AndroidUtilities.isTablet()) {
            this.linearLayout2.addView(this.emptyLayout, new LinearLayout.LayoutParams(-1, AndroidUtilities.dp(528.0f) - ActionBar.getCurrentActionBarHeight()));
        } else {
            this.linearLayout2.addView(this.emptyLayout, new LinearLayout.LayoutParams(-1, AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()));
        }
        ImageView imageView = new ImageView(context);
        this.emptyImageView = imageView;
        imageView.setImageResource(2131166004);
        this.emptyImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("sessions_devicesImage"), PorterDuff.Mode.MULTIPLY));
        this.emptyLayout.addView(this.emptyImageView, LayoutHelper.createLinear(-2, -2));
        TextView textView = new TextView(context);
        this.emptyTextView1 = textView;
        textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.emptyTextView1.setGravity(17);
        this.emptyTextView1.setTextSize(1, 15.0f);
        this.emptyTextView1.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.emptyTextView1.setText(LocaleController.getString("PassportNoDocuments", 2131627355));
        this.emptyLayout.addView(this.emptyTextView1, LayoutHelper.createLinear(-2, -2, 17, 0, 16, 0, 0));
        TextView textView2 = new TextView(context);
        this.emptyTextView2 = textView2;
        textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.emptyTextView2.setGravity(17);
        this.emptyTextView2.setTextSize(1, 14.0f);
        this.emptyTextView2.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.emptyTextView2.setText(LocaleController.getString("PassportNoDocumentsInfo", 2131627357));
        this.emptyLayout.addView(this.emptyTextView2, LayoutHelper.createLinear(-2, -2, 17, 0, 14, 0, 0));
        TextView textView3 = new TextView(context);
        this.emptyTextView3 = textView3;
        textView3.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
        this.emptyTextView3.setGravity(17);
        this.emptyTextView3.setTextSize(1, 15.0f);
        this.emptyTextView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.emptyTextView3.setGravity(17);
        this.emptyTextView3.setText(LocaleController.getString("PassportNoDocumentsAdd", 2131627356).toUpperCase());
        this.emptyLayout.addView(this.emptyTextView3, LayoutHelper.createLinear(-2, 30, 17, 0, 16, 0, 0));
        this.emptyTextView3.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda16(this));
        int size = this.currentForm.values.size();
        int i = 0;
        while (i < size) {
            TLRPC$TL_secureValue tLRPC$TL_secureValue = this.currentForm.values.get(i);
            if (isPersonalDocument(tLRPC$TL_secureValue.type)) {
                arrayList2 = new ArrayList<>();
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3 = new TLRPC$TL_secureRequiredType();
                tLRPC$TL_secureRequiredType3.type = tLRPC$TL_secureValue.type;
                tLRPC$TL_secureRequiredType3.selfie_required = true;
                tLRPC$TL_secureRequiredType3.translation_required = true;
                arrayList2.add(tLRPC$TL_secureRequiredType3);
                tLRPC$TL_secureRequiredType2 = new TLRPC$TL_secureRequiredType();
                tLRPC$TL_secureRequiredType2.type = new TLRPC$TL_secureValueTypePersonalDetails();
            } else if (isAddressDocument(tLRPC$TL_secureValue.type)) {
                arrayList2 = new ArrayList<>();
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType4 = new TLRPC$TL_secureRequiredType();
                tLRPC$TL_secureRequiredType4.type = tLRPC$TL_secureValue.type;
                tLRPC$TL_secureRequiredType4.translation_required = true;
                arrayList2.add(tLRPC$TL_secureRequiredType4);
                tLRPC$TL_secureRequiredType2 = new TLRPC$TL_secureRequiredType();
                tLRPC$TL_secureRequiredType2.type = new TLRPC$TL_secureValueTypeAddress();
            } else {
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType5 = new TLRPC$TL_secureRequiredType();
                tLRPC$TL_secureRequiredType5.type = tLRPC$TL_secureValue.type;
                arrayList = null;
                tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType5;
                z = false;
                addField(context, tLRPC$TL_secureRequiredType, arrayList, z, i != size + (-1));
                i++;
            }
            tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType2;
            arrayList = arrayList2;
            z = true;
            addField(context, tLRPC$TL_secureRequiredType, arrayList, z, i != size + (-1));
            i++;
        }
        updateManageVisibility();
    }

    public /* synthetic */ void lambda$createManageInterface$17(View view) {
        openAddDocumentAlert();
    }

    public /* synthetic */ void lambda$createManageInterface$21(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("TelegramPassportDeleteTitle", 2131628635));
        builder.setMessage(LocaleController.getString("TelegramPassportDeleteAlert", 2131628634));
        builder.setPositiveButton(LocaleController.getString("Delete", 2131625384), new PassportActivity$$ExternalSyntheticLambda0(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        AlertDialog create = builder.create();
        showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor("dialogTextRed2"));
        }
    }

    public /* synthetic */ void lambda$createManageInterface$20(DialogInterface dialogInterface, int i) {
        TLRPC$TL_account_deleteSecureValue tLRPC$TL_account_deleteSecureValue = new TLRPC$TL_account_deleteSecureValue();
        for (int i2 = 0; i2 < this.currentForm.values.size(); i2++) {
            tLRPC$TL_account_deleteSecureValue.types.add(this.currentForm.values.get(i2).type);
        }
        needShowProgress();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_deleteSecureValue, new PassportActivity$$ExternalSyntheticLambda64(this));
    }

    public /* synthetic */ void lambda$createManageInterface$19(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda51(this));
    }

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

    public /* synthetic */ void lambda$createManageInterface$22(View view) {
        openAddDocumentAlert();
    }

    private boolean hasNotValueForType(Class<? extends TLRPC$SecureValueType> cls) {
        int size = this.currentForm.values.size();
        for (int i = 0; i < size; i++) {
            if (this.currentForm.values.get(i).type.getClass() == cls) {
                return false;
            }
        }
        return true;
    }

    private boolean hasUnfilledValues() {
        return hasNotValueForType(TLRPC$TL_secureValueTypePhone.class) || hasNotValueForType(TLRPC$TL_secureValueTypeEmail.class) || hasNotValueForType(TLRPC$TL_secureValueTypePersonalDetails.class) || hasNotValueForType(TLRPC$TL_secureValueTypePassport.class) || hasNotValueForType(TLRPC$TL_secureValueTypeInternalPassport.class) || hasNotValueForType(TLRPC$TL_secureValueTypeIdentityCard.class) || hasNotValueForType(TLRPC$TL_secureValueTypeDriverLicense.class) || hasNotValueForType(TLRPC$TL_secureValueTypeAddress.class) || hasNotValueForType(TLRPC$TL_secureValueTypeUtilityBill.class) || hasNotValueForType(TLRPC$TL_secureValueTypePassportRegistration.class) || hasNotValueForType(TLRPC$TL_secureValueTypeTemporaryRegistration.class) || hasNotValueForType(TLRPC$TL_secureValueTypeBankStatement.class) || hasNotValueForType(TLRPC$TL_secureValueTypeRentalAgreement.class);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r16v3 */
    /* JADX WARN: Type inference failed for: r17v2 */
    private void openAddDocumentAlert() {
        Class<? extends TLRPC$SecureValueType> cls;
        Class<? extends TLRPC$SecureValueType> cls2;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (hasNotValueForType(TLRPC$TL_secureValueTypePhone.class)) {
            cls2 = TLRPC$TL_secureValueTypeRentalAgreement.class;
            cls = TLRPC$TL_secureValueTypeBankStatement.class;
            arrayList.add(LocaleController.getString("ActionBotDocumentPhone", 2131624147));
            arrayList2.add(TLRPC$TL_secureValueTypePhone.class);
        } else {
            cls2 = TLRPC$TL_secureValueTypeRentalAgreement.class;
            cls = TLRPC$TL_secureValueTypeBankStatement.class;
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeEmail.class)) {
            arrayList.add(LocaleController.getString("ActionBotDocumentEmail", 2131624141));
            arrayList2.add(TLRPC$TL_secureValueTypeEmail.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypePersonalDetails.class)) {
            arrayList.add(LocaleController.getString("ActionBotDocumentIdentity", 2131624142));
            arrayList2.add(TLRPC$TL_secureValueTypePersonalDetails.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypePassport.class)) {
            arrayList.add(LocaleController.getString("ActionBotDocumentPassport", 2131624145));
            arrayList2.add(TLRPC$TL_secureValueTypePassport.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeInternalPassport.class)) {
            arrayList.add(LocaleController.getString("ActionBotDocumentInternalPassport", 2131624144));
            arrayList2.add(TLRPC$TL_secureValueTypeInternalPassport.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypePassportRegistration.class)) {
            arrayList.add(LocaleController.getString("ActionBotDocumentPassportRegistration", 2131624146));
            arrayList2.add(TLRPC$TL_secureValueTypePassportRegistration.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeTemporaryRegistration.class)) {
            arrayList.add(LocaleController.getString("ActionBotDocumentTemporaryRegistration", 2131624149));
            arrayList2.add(TLRPC$TL_secureValueTypeTemporaryRegistration.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeIdentityCard.class)) {
            arrayList.add(LocaleController.getString("ActionBotDocumentIdentityCard", 2131624143));
            arrayList2.add(TLRPC$TL_secureValueTypeIdentityCard.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeDriverLicense.class)) {
            arrayList.add(LocaleController.getString("ActionBotDocumentDriverLicence", 2131624140));
            arrayList2.add(TLRPC$TL_secureValueTypeDriverLicense.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeAddress.class)) {
            arrayList.add(LocaleController.getString("ActionBotDocumentAddress", 2131624138));
            arrayList2.add(TLRPC$TL_secureValueTypeAddress.class);
        }
        if (hasNotValueForType(TLRPC$TL_secureValueTypeUtilityBill.class)) {
            arrayList.add(LocaleController.getString("ActionBotDocumentUtilityBill", 2131624150));
            arrayList2.add(TLRPC$TL_secureValueTypeUtilityBill.class);
        }
        Class<? extends TLRPC$SecureValueType> cls3 = cls;
        if (hasNotValueForType(cls3)) {
            arrayList.add(LocaleController.getString("ActionBotDocumentBankStatement", 2131624139));
            arrayList2.add(cls3);
        }
        Class<? extends TLRPC$SecureValueType> cls4 = cls2;
        if (hasNotValueForType(cls4)) {
            arrayList.add(LocaleController.getString("ActionBotDocumentRentalAgreement", 2131624148));
            arrayList2.add(cls4);
        }
        if (getParentActivity() == null || arrayList.isEmpty()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("PassportNoDocumentsAdd", 2131627356));
        builder.setItems((CharSequence[]) arrayList.toArray(new CharSequence[0]), new PassportActivity$$ExternalSyntheticLambda5(this, arrayList2));
        showDialog(builder.create());
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0051  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$openAddDocumentAlert$23(ArrayList arrayList, DialogInterface dialogInterface, int i) {
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2;
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
        boolean z = true;
        if (isPersonalDocument(tLRPC$TL_secureRequiredType.type)) {
            tLRPC$TL_secureRequiredType.selfie_required = true;
            tLRPC$TL_secureRequiredType.translation_required = true;
            tLRPC$TL_secureRequiredType2 = new TLRPC$TL_secureRequiredType();
            tLRPC$TL_secureRequiredType2.type = new TLRPC$TL_secureValueTypePersonalDetails();
        } else {
            if (isAddressDocument(tLRPC$TL_secureRequiredType.type)) {
                tLRPC$TL_secureRequiredType2 = new TLRPC$TL_secureRequiredType();
                tLRPC$TL_secureRequiredType2.type = new TLRPC$TL_secureValueTypeAddress();
            }
            ArrayList<TLRPC$TL_secureRequiredType> arrayList2 = new ArrayList<>();
            if (tLRPC$TL_secureRequiredType3 == null) {
                z = false;
            }
            openTypeActivity(tLRPC$TL_secureRequiredType, tLRPC$TL_secureRequiredType3, arrayList2, z);
        }
        tLRPC$TL_secureRequiredType3 = tLRPC$TL_secureRequiredType;
        tLRPC$TL_secureRequiredType = tLRPC$TL_secureRequiredType2;
        ArrayList<TLRPC$TL_secureRequiredType> arrayList22 = new ArrayList<>();
        if (tLRPC$TL_secureRequiredType3 == null) {
        }
        openTypeActivity(tLRPC$TL_secureRequiredType, tLRPC$TL_secureRequiredType3, arrayList22, z);
    }

    private void updateManageVisibility() {
        if (this.currentForm.values.isEmpty()) {
            this.emptyLayout.setVisibility(0);
            this.sectionCell.setVisibility(8);
            this.headerCell.setVisibility(8);
            this.addDocumentCell.setVisibility(8);
            this.deletePassportCell.setVisibility(8);
            this.addDocumentSectionCell.setVisibility(8);
            return;
        }
        this.emptyLayout.setVisibility(8);
        this.sectionCell.setVisibility(0);
        this.headerCell.setVisibility(0);
        this.deletePassportCell.setVisibility(0);
        this.addDocumentSectionCell.setVisibility(0);
        if (hasUnfilledValues()) {
            this.addDocumentCell.setVisibility(0);
        } else {
            this.addDocumentCell.setVisibility(8);
        }
    }

    public void callCallback(boolean z) {
        int i;
        int i2;
        if (!this.callbackCalled) {
            if (!TextUtils.isEmpty(this.currentCallbackUrl)) {
                if (z) {
                    Activity parentActivity = getParentActivity();
                    Browser.openUrl(parentActivity, Uri.parse(this.currentCallbackUrl + "&tg_passport=success"));
                } else if (!this.ignoreOnFailure && ((i2 = this.currentActivityType) == 5 || i2 == 0)) {
                    Activity parentActivity2 = getParentActivity();
                    Browser.openUrl(parentActivity2, Uri.parse(this.currentCallbackUrl + "&tg_passport=cancel"));
                }
                this.callbackCalled = true;
            } else if (!this.needActivityResult) {
            } else {
                if (z || (!this.ignoreOnFailure && ((i = this.currentActivityType) == 5 || i == 0))) {
                    getParentActivity().setResult(z ? -1 : 0);
                }
                this.callbackCalled = true;
            }
        }
    }

    private void createEmailInterface(Context context) {
        this.actionBar.setTitle(LocaleController.getString("PassportEmail", 2131627277));
        if (!TextUtils.isEmpty(this.currentEmail)) {
            TextSettingsCell textSettingsCell = new TextSettingsCell(context);
            textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            textSettingsCell.setText(LocaleController.formatString("PassportPhoneUseSame", 2131627368, this.currentEmail), false);
            this.linearLayout2.addView(textSettingsCell, LayoutHelper.createLinear(-1, -2));
            textSettingsCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda20(this));
            TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell;
            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
            this.bottomCell.setText(LocaleController.getString("PassportPhoneUseSameEmailInfo", 2131627369));
            this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
        }
        this.inputFields = new EditTextBoldCursor[1];
        for (int i = 0; i < 1; i++) {
            FrameLayout frameLayout = new FrameLayout(context);
            this.linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-1, 50));
            frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.inputFields[i] = new EditTextBoldCursor(context);
            this.inputFields[i].setTag(Integer.valueOf(i));
            this.inputFields[i].setTextSize(1, 16.0f);
            this.inputFields[i].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[i].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setBackgroundDrawable(null);
            this.inputFields[i].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[i].setCursorWidth(1.5f);
            this.inputFields[i].setInputType(33);
            this.inputFields[i].setImeOptions(268435462);
            this.inputFields[i].setHint(LocaleController.getString("PaymentShippingEmailPlaceholder", 2131627465));
            TLRPC$TL_secureValue tLRPC$TL_secureValue = this.currentTypeValue;
            if (tLRPC$TL_secureValue != null) {
                TLRPC$SecurePlainData tLRPC$SecurePlainData = tLRPC$TL_secureValue.plain_data;
                if (tLRPC$SecurePlainData instanceof TLRPC$TL_securePlainEmail) {
                    TLRPC$TL_securePlainEmail tLRPC$TL_securePlainEmail = (TLRPC$TL_securePlainEmail) tLRPC$SecurePlainData;
                    if (!TextUtils.isEmpty(tLRPC$TL_securePlainEmail.email)) {
                        this.inputFields[i].setText(tLRPC$TL_securePlainEmail.email);
                    }
                }
            }
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            editTextBoldCursorArr[i].setSelection(editTextBoldCursorArr[i].length());
            this.inputFields[i].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
            this.inputFields[i].setGravity(LocaleController.isRTL ? 5 : 3);
            frameLayout.addView(this.inputFields[i], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
            this.inputFields[i].setOnEditorActionListener(new PassportActivity$$ExternalSyntheticLambda44(this));
        }
        TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
        this.bottomCell = textInfoPrivacyCell2;
        textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
        this.bottomCell.setText(LocaleController.getString("PassportEmailUploadInfo", 2131627280));
        this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
    }

    public /* synthetic */ void lambda$createEmailInterface$24(View view) {
        this.useCurrentValue = true;
        this.doneItem.callOnClick();
        this.useCurrentValue = false;
    }

    public /* synthetic */ boolean lambda$createEmailInterface$25(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6 || i == 5) {
            this.doneItem.callOnClick();
            return true;
        }
        return false;
    }

    private void createPhoneInterface(Context context) {
        String str;
        String str2;
        TelephonyManager telephonyManager;
        LinearLayout linearLayout;
        this.actionBar.setTitle(LocaleController.getString("PassportPhone", 2131627364));
        this.languageMap = new HashMap<>();
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
        Collections.sort(this.countriesArray, CountrySelectActivity$CountryAdapter$$ExternalSyntheticLambda0.INSTANCE);
        String str3 = UserConfig.getInstance(this.currentAccount).getCurrentUser().phone;
        TextSettingsCell textSettingsCell = new TextSettingsCell(context);
        textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
        textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        textSettingsCell.setText(LocaleController.formatString("PassportPhoneUseSame", 2131627368, PhoneFormat.getInstance().format("+" + str3)), false);
        int i = -1;
        this.linearLayout2.addView(textSettingsCell, LayoutHelper.createLinear(-1, -2));
        textSettingsCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda25(this));
        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
        this.bottomCell = textInfoPrivacyCell;
        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
        this.bottomCell.setText(LocaleController.getString("PassportPhoneUseSameInfo", 2131627370));
        this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
        HeaderCell headerCell = new HeaderCell(context);
        this.headerCell = headerCell;
        headerCell.setText(LocaleController.getString("PassportPhoneUseOther", 2131627367));
        this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
        this.inputFields = new EditTextBoldCursor[3];
        int i2 = 0;
        while (i2 < 3) {
            if (i2 == 2) {
                this.inputFields[i2] = new HintEditText(context);
            } else {
                this.inputFields[i2] = new EditTextBoldCursor(context);
            }
            if (i2 == 1) {
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(0);
                this.linearLayout2.addView(linearLayout2, LayoutHelper.createLinear(i, 50));
                linearLayout2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                linearLayout = linearLayout2;
            } else if (i2 == 2) {
                linearLayout = (ViewGroup) this.inputFields[1].getParent();
            } else {
                FrameLayout frameLayout = new FrameLayout(context);
                this.linearLayout2.addView(frameLayout, LayoutHelper.createLinear(i, 50));
                frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                linearLayout = frameLayout;
            }
            this.inputFields[i2].setTag(Integer.valueOf(i2));
            this.inputFields[i2].setTextSize(1, 16.0f);
            this.inputFields[i2].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[i2].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i2].setBackgroundDrawable(null);
            this.inputFields[i2].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i2].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[i2].setCursorWidth(1.5f);
            if (i2 == 0) {
                this.inputFields[i2].setOnTouchListener(new PassportActivity$$ExternalSyntheticLambda35(this));
                this.inputFields[i2].setText(LocaleController.getString("ChooseCountry", 2131625117));
                this.inputFields[i2].setInputType(0);
                this.inputFields[i2].setFocusable(false);
            } else {
                this.inputFields[i2].setInputType(3);
                if (i2 == 2) {
                    this.inputFields[i2].setImeOptions(268435462);
                } else {
                    this.inputFields[i2].setImeOptions(268435461);
                }
            }
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            editTextBoldCursorArr[i2].setSelection(editTextBoldCursorArr[i2].length());
            int i3 = 5;
            if (i2 == 1) {
                TextView textView = new TextView(context);
                this.plusTextView = textView;
                textView.setText("+");
                this.plusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.plusTextView.setTextSize(1, 16.0f);
                linearLayout.addView(this.plusTextView, LayoutHelper.createLinear(-2, -2, 21.0f, 12.0f, 0.0f, 6.0f));
                this.inputFields[i2].setPadding(AndroidUtilities.dp(10.0f), 0, 0, 0);
                this.inputFields[i2].setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                this.inputFields[i2].setGravity(19);
                linearLayout.addView(this.inputFields[i2], LayoutHelper.createLinear(55, -2, 0.0f, 12.0f, 16.0f, 6.0f));
                this.inputFields[i2].addTextChangedListener(new AnonymousClass9());
            } else if (i2 == 2) {
                this.inputFields[i2].setPadding(0, 0, 0, 0);
                this.inputFields[i2].setGravity(19);
                this.inputFields[i2].setHintText(null);
                this.inputFields[i2].setHint(LocaleController.getString("PaymentShippingPhoneNumber", 2131627469));
                linearLayout.addView(this.inputFields[i2], LayoutHelper.createLinear(-1, -2, 0.0f, 12.0f, 21.0f, 6.0f));
                this.inputFields[i2].addTextChangedListener(new AnonymousClass10());
            } else {
                this.inputFields[i2].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                EditTextBoldCursor editTextBoldCursor = this.inputFields[i2];
                if (!LocaleController.isRTL) {
                    i3 = 3;
                }
                editTextBoldCursor.setGravity(i3);
                linearLayout.addView(this.inputFields[i2], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
            }
            this.inputFields[i2].setOnEditorActionListener(new PassportActivity$$ExternalSyntheticLambda45(this));
            if (i2 == 2) {
                this.inputFields[i2].setOnKeyListener(new PassportActivity$$ExternalSyntheticLambda32(this));
            }
            if (i2 == 0) {
                View view = new View(context);
                this.dividers.add(view);
                view.setBackgroundColor(Theme.getColor("divider"));
                linearLayout.addView(view, new FrameLayout.LayoutParams(-1, 1, 83));
            }
            i2++;
            i = -1;
        }
        try {
            telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        if (telephonyManager != null) {
            str = telephonyManager.getSimCountryIso().toUpperCase();
            if (str != null && (str2 = this.languageMap.get(str)) != null && this.countriesArray.indexOf(str2) != -1) {
                this.inputFields[1].setText(this.countriesMap.get(str2));
            }
            TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell2;
            textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
            this.bottomCell.setText(LocaleController.getString("PassportPhoneUploadInfo", 2131627366));
            this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
        }
        str = null;
        if (str != null) {
            this.inputFields[1].setText(this.countriesMap.get(str2));
        }
        TextInfoPrivacyCell textInfoPrivacyCell22 = new TextInfoPrivacyCell(context);
        this.bottomCell = textInfoPrivacyCell22;
        textInfoPrivacyCell22.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
        this.bottomCell.setText(LocaleController.getString("PassportPhoneUploadInfo", 2131627366));
        this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
    }

    public /* synthetic */ void lambda$createPhoneInterface$26(View view) {
        this.useCurrentValue = true;
        this.doneItem.callOnClick();
        this.useCurrentValue = false;
    }

    public /* synthetic */ boolean lambda$createPhoneInterface$29(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(false);
            countrySelectActivity.setCountrySelectActivityDelegate(new PassportActivity$$ExternalSyntheticLambda71(this));
            presentFragment(countrySelectActivity);
        }
        return true;
    }

    public /* synthetic */ void lambda$createPhoneInterface$28(CountrySelectActivity.Country country) {
        this.inputFields[0].setText(country.name);
        if (this.countriesArray.indexOf(country.name) != -1) {
            this.ignoreOnTextChange = true;
            String str = this.countriesMap.get(country.name);
            this.inputFields[1].setText(str);
            String str2 = this.phoneFormatMap.get(str);
            this.inputFields[2].setHintText(str2 != null ? str2.replace('X', (char) 8211) : null);
            this.ignoreOnTextChange = false;
        }
        AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda48(this), 300L);
        this.inputFields[2].requestFocus();
        EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
        editTextBoldCursorArr[2].setSelection(editTextBoldCursorArr[2].length());
    }

    public /* synthetic */ void lambda$createPhoneInterface$27() {
        AndroidUtilities.showKeyboard(this.inputFields[2]);
    }

    /* renamed from: org.telegram.ui.PassportActivity$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass9() {
            PassportActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            boolean z;
            String str;
            boolean z2;
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
                hintEditText.setHint(LocaleController.getString("PaymentShippingPhoneNumber", 2131627469));
                PassportActivity.this.inputFields[0].setText(LocaleController.getString("ChooseCountry", 2131625117));
            } else {
                int i = 4;
                if (stripExceptNumbers.length() > 4) {
                    while (true) {
                        if (i < 1) {
                            str = null;
                            z = false;
                            break;
                        }
                        String substring = stripExceptNumbers.substring(0, i);
                        if (((String) PassportActivity.this.codesMap.get(substring)) != null) {
                            String str2 = stripExceptNumbers.substring(i) + PassportActivity.this.inputFields[2].getText().toString();
                            PassportActivity.this.inputFields[1].setText(substring);
                            z = true;
                            str = str2;
                            stripExceptNumbers = substring;
                            break;
                        }
                        i--;
                    }
                    if (!z) {
                        str = stripExceptNumbers.substring(1) + PassportActivity.this.inputFields[2].getText().toString();
                        EditTextBoldCursor editTextBoldCursor = PassportActivity.this.inputFields[1];
                        stripExceptNumbers = stripExceptNumbers.substring(0, 1);
                        editTextBoldCursor.setText(stripExceptNumbers);
                    }
                } else {
                    str = null;
                    z = false;
                }
                String str3 = (String) PassportActivity.this.codesMap.get(stripExceptNumbers);
                if (str3 == null || (indexOf = PassportActivity.this.countriesArray.indexOf(str3)) == -1) {
                    z2 = false;
                } else {
                    PassportActivity.this.inputFields[0].setText((CharSequence) PassportActivity.this.countriesArray.get(indexOf));
                    String str4 = (String) PassportActivity.this.phoneFormatMap.get(stripExceptNumbers);
                    if (str4 != null) {
                        hintEditText.setHintText(str4.replace('X', (char) 8211));
                        hintEditText.setHint((CharSequence) null);
                    }
                    z2 = true;
                }
                if (!z2) {
                    hintEditText.setHintText((String) null);
                    hintEditText.setHint(LocaleController.getString("PaymentShippingPhoneNumber", 2131627469));
                    PassportActivity.this.inputFields[0].setText(LocaleController.getString("WrongCountry", 2131629329));
                }
                if (!z) {
                    PassportActivity.this.inputFields[1].setSelection(PassportActivity.this.inputFields[1].getText().length());
                }
                if (str != null) {
                    hintEditText.requestFocus();
                    hintEditText.setText(str);
                    hintEditText.setSelection(hintEditText.length());
                }
            }
            PassportActivity.this.ignoreOnTextChange = false;
        }
    }

    /* renamed from: org.telegram.ui.PassportActivity$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 implements TextWatcher {
        private int actionPosition;
        private int characterAction = -1;

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass10() {
            PassportActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (i2 == 0 && i3 == 1) {
                this.characterAction = 1;
            } else if (i2 == 1 && i3 == 0) {
                if (charSequence.charAt(i) == ' ' && i > 0) {
                    this.characterAction = 3;
                    this.actionPosition = i - 1;
                    return;
                }
                this.characterAction = 2;
            } else {
                this.characterAction = -1;
            }
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            int i;
            int i2;
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
            int i3 = 0;
            while (i3 < obj.length()) {
                int i4 = i3 + 1;
                String substring = obj.substring(i3, i4);
                if ("0123456789".contains(substring)) {
                    sb.append(substring);
                }
                i3 = i4;
            }
            PassportActivity.this.ignoreOnPhoneChange = true;
            String hintText = hintEditText.getHintText();
            if (hintText != null) {
                int i5 = 0;
                while (true) {
                    if (i5 >= sb.length()) {
                        break;
                    } else if (i5 < hintText.length()) {
                        if (hintText.charAt(i5) == ' ') {
                            sb.insert(i5, ' ');
                            i5++;
                            if (selectionStart == i5 && (i2 = this.characterAction) != 2 && i2 != 3) {
                                selectionStart++;
                            }
                        }
                        i5++;
                    } else {
                        sb.insert(i5, ' ');
                        if (selectionStart == i5 + 1 && (i = this.characterAction) != 2 && i != 3) {
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
    }

    public /* synthetic */ boolean lambda$createPhoneInterface$30(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5) {
            this.inputFields[2].requestFocus();
            return true;
        } else if (i != 6) {
            return false;
        } else {
            this.doneItem.callOnClick();
            return true;
        }
    }

    public /* synthetic */ boolean lambda$createPhoneInterface$31(View view, int i, KeyEvent keyEvent) {
        if (i == 67 && this.inputFields[2].length() == 0) {
            this.inputFields[1].requestFocus();
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            editTextBoldCursorArr[1].setSelection(editTextBoldCursorArr[1].length());
            this.inputFields[1].dispatchKeyEvent(keyEvent);
            return true;
        }
        return false;
    }

    private void createAddressInterface(Context context) {
        String str;
        this.languageMap = new HashMap<>();
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
        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165437, "windowBackgroundGrayShadow"));
        this.topErrorCell.setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
        int i = -2;
        this.linearLayout2.addView(this.topErrorCell, LayoutHelper.createLinear(-1, -2));
        checkTopErrorCell(true);
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType = this.currentDocumentsType;
        if (tLRPC$TL_secureRequiredType != null) {
            TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
            if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                this.actionBar.setTitle(LocaleController.getString("ActionBotDocumentRentalAgreement", 2131624148));
            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement) {
                this.actionBar.setTitle(LocaleController.getString("ActionBotDocumentBankStatement", 2131624139));
            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                this.actionBar.setTitle(LocaleController.getString("ActionBotDocumentUtilityBill", 2131624150));
            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                this.actionBar.setTitle(LocaleController.getString("ActionBotDocumentPassportRegistration", 2131624146));
            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                this.actionBar.setTitle(LocaleController.getString("ActionBotDocumentTemporaryRegistration", 2131624149));
            }
            HeaderCell headerCell = new HeaderCell(context);
            this.headerCell = headerCell;
            headerCell.setText(LocaleController.getString("PassportDocuments", 2131627276));
            this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
            LinearLayout linearLayout = new LinearLayout(context);
            this.documentsLayout = linearLayout;
            linearLayout.setOrientation(1);
            this.linearLayout2.addView(this.documentsLayout, LayoutHelper.createLinear(-1, -2));
            TextSettingsCell textSettingsCell = new TextSettingsCell(context);
            this.uploadDocumentCell = textSettingsCell;
            textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.linearLayout2.addView(this.uploadDocumentCell, LayoutHelper.createLinear(-1, -2));
            this.uploadDocumentCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda23(this));
            TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell2;
            textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165435, "windowBackgroundGrayShadow"));
            if (this.currentBotId != 0) {
                this.noAllDocumentsErrorText = LocaleController.getString("PassportAddAddressUploadInfo", 2131627224);
            } else {
                TLRPC$SecureValueType tLRPC$SecureValueType2 = this.currentDocumentsType.type;
                if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                    this.noAllDocumentsErrorText = LocaleController.getString("PassportAddAgreementInfo", 2131627226);
                } else if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                    this.noAllDocumentsErrorText = LocaleController.getString("PassportAddBillInfo", 2131627230);
                } else if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                    this.noAllDocumentsErrorText = LocaleController.getString("PassportAddPassportRegistrationInfo", 2131627240);
                } else if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                    this.noAllDocumentsErrorText = LocaleController.getString("PassportAddTemporaryRegistrationInfo", 2131627242);
                } else if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeBankStatement) {
                    this.noAllDocumentsErrorText = LocaleController.getString("PassportAddBankInfo", 2131627228);
                } else {
                    this.noAllDocumentsErrorText = "";
                }
            }
            CharSequence charSequence = this.noAllDocumentsErrorText;
            HashMap<String, String> hashMap = this.documentsErrors;
            SpannableStringBuilder spannableStringBuilder = charSequence;
            if (hashMap != null) {
                String str2 = hashMap.get("files_all");
                spannableStringBuilder = charSequence;
                if (str2 != null) {
                    SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(str2);
                    spannableStringBuilder2.append((CharSequence) "\n\n");
                    spannableStringBuilder2.append(this.noAllDocumentsErrorText);
                    spannableStringBuilder2.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteRedText3")), 0, str2.length(), 33);
                    this.errorsValues.put("files_all", "");
                    spannableStringBuilder = spannableStringBuilder2;
                }
            }
            this.bottomCell.setText(spannableStringBuilder);
            this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
            if (this.currentDocumentsType.translation_required) {
                HeaderCell headerCell2 = new HeaderCell(context);
                this.headerCell = headerCell2;
                headerCell2.setText(LocaleController.getString("PassportTranslation", 2131627397));
                this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
                LinearLayout linearLayout2 = new LinearLayout(context);
                this.translationLayout = linearLayout2;
                linearLayout2.setOrientation(1);
                this.linearLayout2.addView(this.translationLayout, LayoutHelper.createLinear(-1, -2));
                TextSettingsCell textSettingsCell2 = new TextSettingsCell(context);
                this.uploadTranslationCell = textSettingsCell2;
                textSettingsCell2.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.linearLayout2.addView(this.uploadTranslationCell, LayoutHelper.createLinear(-1, -2));
                this.uploadTranslationCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda27(this));
                TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
                this.bottomCellTranslation = textInfoPrivacyCell3;
                textInfoPrivacyCell3.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165435, "windowBackgroundGrayShadow"));
                if (this.currentBotId != 0) {
                    this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationUploadInfo", 2131627248);
                } else {
                    TLRPC$SecureValueType tLRPC$SecureValueType3 = this.currentDocumentsType.type;
                    if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationAgreementInfo", 2131627243);
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationBillInfo", 2131627245);
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationPassportRegistrationInfo", 2131627246);
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationTemporaryRegistrationInfo", 2131627247);
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeBankStatement) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationBankInfo", 2131627244);
                    } else {
                        this.noAllTranslationErrorText = "";
                    }
                }
                CharSequence charSequence2 = this.noAllTranslationErrorText;
                HashMap<String, String> hashMap2 = this.documentsErrors;
                SpannableStringBuilder spannableStringBuilder3 = charSequence2;
                if (hashMap2 != null) {
                    String str3 = hashMap2.get("translation_all");
                    spannableStringBuilder3 = charSequence2;
                    if (str3 != null) {
                        SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder(str3);
                        spannableStringBuilder4.append((CharSequence) "\n\n");
                        spannableStringBuilder4.append(this.noAllTranslationErrorText);
                        spannableStringBuilder4.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteRedText3")), 0, str3.length(), 33);
                        this.errorsValues.put("translation_all", "");
                        spannableStringBuilder3 = spannableStringBuilder4;
                    }
                }
                this.bottomCellTranslation.setText(spannableStringBuilder3);
                this.linearLayout2.addView(this.bottomCellTranslation, LayoutHelper.createLinear(-1, -2));
            }
        } else {
            this.actionBar.setTitle(LocaleController.getString("PassportAddress", 2131627249));
        }
        HeaderCell headerCell3 = new HeaderCell(context);
        this.headerCell = headerCell3;
        headerCell3.setText(LocaleController.getString("PassportAddressHeader", 2131627250));
        this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
        int i2 = 6;
        this.inputFields = new EditTextBoldCursor[6];
        int i3 = 0;
        while (i3 < i2) {
            EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
            this.inputFields[i3] = editTextBoldCursor;
            AnonymousClass11 anonymousClass11 = new AnonymousClass11(this, context, editTextBoldCursor);
            anonymousClass11.setWillNotDraw(false);
            this.linearLayout2.addView(anonymousClass11, LayoutHelper.createLinear(-1, i));
            anonymousClass11.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            int i4 = 5;
            if (i3 == 5) {
                View view = new View(context);
                this.extraBackgroundView = view;
                view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                this.linearLayout2.addView(this.extraBackgroundView, LayoutHelper.createLinear(-1, i2));
            }
            if (this.documentOnly && this.currentDocumentsType != null) {
                anonymousClass11.setVisibility(8);
                View view2 = this.extraBackgroundView;
                if (view2 != null) {
                    view2.setVisibility(8);
                }
            }
            this.inputFields[i3].setTag(Integer.valueOf(i3));
            this.inputFields[i3].setSupportRtlHint(true);
            this.inputFields[i3].setTextSize(1, 16.0f);
            this.inputFields[i3].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[i3].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
            this.inputFields[i3].setTransformHintToHeader(true);
            this.inputFields[i3].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i3].setBackgroundDrawable(null);
            this.inputFields[i3].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i3].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[i3].setCursorWidth(1.5f);
            this.inputFields[i3].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
            if (i3 == 5) {
                this.inputFields[i3].setOnTouchListener(new PassportActivity$$ExternalSyntheticLambda34(this));
                this.inputFields[i3].setInputType(0);
                this.inputFields[i3].setFocusable(false);
            } else {
                this.inputFields[i3].setInputType(16385);
                this.inputFields[i3].setImeOptions(268435461);
            }
            if (i3 == 0) {
                this.inputFields[i3].setHintText(LocaleController.getString("PassportStreet1", 2131627392));
                str = "street_line1";
            } else if (i3 == 1) {
                this.inputFields[i3].setHintText(LocaleController.getString("PassportStreet2", 2131627393));
                str = "street_line2";
            } else if (i3 == 2) {
                this.inputFields[i3].setHintText(LocaleController.getString("PassportPostcode", 2131627372));
                str = "post_code";
            } else if (i3 == 3) {
                this.inputFields[i3].setHintText(LocaleController.getString("PassportCity", 2131627256));
                str = "city";
            } else if (i3 == 4) {
                this.inputFields[i3].setHintText(LocaleController.getString("PassportState", 2131627391));
                str = "state";
            } else if (i3 != 5) {
                i3++;
                i2 = 6;
                i = -2;
            } else {
                this.inputFields[i3].setHintText(LocaleController.getString("PassportCountry", 2131627258));
                str = "country_code";
            }
            setFieldValues(this.currentValues, this.inputFields[i3], str);
            if (i3 == 2) {
                this.inputFields[i3].addTextChangedListener(new AnonymousClass12(editTextBoldCursor, str));
                this.inputFields[i3].setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            } else {
                this.inputFields[i3].addTextChangedListener(new AnonymousClass13(editTextBoldCursor, str));
            }
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            editTextBoldCursorArr[i3].setSelection(editTextBoldCursorArr[i3].length());
            this.inputFields[i3].setPadding(0, 0, 0, 0);
            EditTextBoldCursor editTextBoldCursor2 = this.inputFields[i3];
            if (!LocaleController.isRTL) {
                i4 = 3;
            }
            editTextBoldCursor2.setGravity(i4 | 16);
            anonymousClass11.addView(this.inputFields[i3], LayoutHelper.createFrame(-1, 64.0f, 51, 21.0f, 0.0f, 21.0f, 0.0f));
            this.inputFields[i3].setOnEditorActionListener(new PassportActivity$$ExternalSyntheticLambda42(this));
            i3++;
            i2 = 6;
            i = -2;
        }
        ShadowSectionCell shadowSectionCell = new ShadowSectionCell(context);
        this.sectionCell = shadowSectionCell;
        this.linearLayout2.addView(shadowSectionCell, LayoutHelper.createLinear(-1, -2));
        if (this.documentOnly && this.currentDocumentsType != null) {
            this.headerCell.setVisibility(8);
            this.sectionCell.setVisibility(8);
        }
        if (((this.currentBotId != 0 || this.currentDocumentsType == null) && this.currentTypeValue != null && !this.documentOnly) || this.currentDocumentsTypeValue != null) {
            TLRPC$TL_secureValue tLRPC$TL_secureValue = this.currentDocumentsTypeValue;
            if (tLRPC$TL_secureValue != null) {
                addDocumentViews(tLRPC$TL_secureValue.files);
                addTranslationDocumentViews(this.currentDocumentsTypeValue.translation);
            }
            this.sectionCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165435, "windowBackgroundGrayShadow"));
            TextSettingsCell textSettingsCell3 = new TextSettingsCell(context);
            textSettingsCell3.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
            textSettingsCell3.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            if (this.currentDocumentsType == null) {
                textSettingsCell3.setText(LocaleController.getString("PassportDeleteInfo", 2131627265), false);
            } else {
                textSettingsCell3.setText(LocaleController.getString("PassportDeleteDocument", 2131627260), false);
            }
            this.linearLayout2.addView(textSettingsCell3, LayoutHelper.createLinear(-1, -2));
            textSettingsCell3.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda13(this));
            ShadowSectionCell shadowSectionCell2 = new ShadowSectionCell(context);
            this.sectionCell = shadowSectionCell2;
            shadowSectionCell2.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
            this.linearLayout2.addView(this.sectionCell, LayoutHelper.createLinear(-1, -2));
        } else {
            this.sectionCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
            if (this.documentOnly && this.currentDocumentsType != null) {
                this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
            }
        }
        updateUploadText(0);
        updateUploadText(4);
    }

    public /* synthetic */ void lambda$createAddressInterface$32(View view) {
        this.uploadingFileType = 0;
        openAttachMenu();
    }

    public /* synthetic */ void lambda$createAddressInterface$33(View view) {
        this.uploadingFileType = 4;
        openAttachMenu();
    }

    /* renamed from: org.telegram.ui.PassportActivity$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 extends FrameLayout {
        private StaticLayout errorLayout;
        float offsetX;
        final /* synthetic */ EditTextBoldCursor val$field;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass11(PassportActivity passportActivity, Context context, EditTextBoldCursor editTextBoldCursor) {
            super(context);
            this.val$field = editTextBoldCursor;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i) - AndroidUtilities.dp(34.0f);
            StaticLayout errorLayout = this.val$field.getErrorLayout(size);
            this.errorLayout = errorLayout;
            if (errorLayout != null) {
                int lineCount = errorLayout.getLineCount();
                int i3 = 0;
                if (lineCount > 1) {
                    i2 = View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.errorLayout.getLineBottom(lineCount - 1) - this.errorLayout.getLineBottom(0)), 1073741824);
                }
                if (LocaleController.isRTL) {
                    float f = 0.0f;
                    while (true) {
                        if (i3 >= lineCount) {
                            break;
                        } else if (this.errorLayout.getLineLeft(i3) != 0.0f) {
                            this.offsetX = 0.0f;
                            break;
                        } else {
                            f = Math.max(f, this.errorLayout.getLineWidth(i3));
                            if (i3 == lineCount - 1) {
                                this.offsetX = size - f;
                            }
                            i3++;
                        }
                    }
                }
            }
            super.onMeasure(i, i2);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.errorLayout != null) {
                canvas.save();
                canvas.translate(AndroidUtilities.dp(21.0f) + this.offsetX, this.val$field.getLineY() + AndroidUtilities.dp(3.0f));
                this.errorLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    public /* synthetic */ boolean lambda$createAddressInterface$35(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(false);
            countrySelectActivity.setCountrySelectActivityDelegate(new PassportActivity$$ExternalSyntheticLambda70(this));
            presentFragment(countrySelectActivity);
        }
        return true;
    }

    public /* synthetic */ void lambda$createAddressInterface$34(CountrySelectActivity.Country country) {
        this.inputFields[5].setText(country.name);
        this.currentCitizeship = country.shortname;
    }

    /* renamed from: org.telegram.ui.PassportActivity$12 */
    /* loaded from: classes3.dex */
    public class AnonymousClass12 implements TextWatcher {
        private boolean ignore;
        final /* synthetic */ EditTextBoldCursor val$field;
        final /* synthetic */ String val$key;

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass12(EditTextBoldCursor editTextBoldCursor, String str) {
            PassportActivity.this = r1;
            this.val$field = editTextBoldCursor;
            this.val$key = str;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (this.ignore) {
                return;
            }
            boolean z = true;
            this.ignore = true;
            int i = 0;
            while (true) {
                if (i >= editable.length()) {
                    z = false;
                    break;
                }
                char charAt = editable.charAt(i);
                if ((charAt < 'a' || charAt > 'z') && ((charAt < 'A' || charAt > 'Z') && !((charAt >= '0' && charAt <= '9') || charAt == '-' || charAt == ' '))) {
                    break;
                }
                i++;
            }
            this.ignore = false;
            if (!z) {
                PassportActivity.this.checkFieldForError(this.val$field, this.val$key, editable, false);
            } else {
                this.val$field.setErrorText(LocaleController.getString("PassportUseLatinOnly", 2131627403));
            }
        }
    }

    /* renamed from: org.telegram.ui.PassportActivity$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 implements TextWatcher {
        final /* synthetic */ EditTextBoldCursor val$field;
        final /* synthetic */ String val$key;

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass13(EditTextBoldCursor editTextBoldCursor, String str) {
            PassportActivity.this = r1;
            this.val$field = editTextBoldCursor;
            this.val$key = str;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            PassportActivity.this.checkFieldForError(this.val$field, this.val$key, editable, false);
        }
    }

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

    public /* synthetic */ void lambda$createAddressInterface$37(View view) {
        createDocumentDeleteAlert();
    }

    private void createDocumentDeleteAlert() {
        boolean[] zArr = {true};
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString("OK", 2131627127), new PassportActivity$$ExternalSyntheticLambda11(this, zArr));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        builder.setTitle(LocaleController.getString("AppName", 2131624384));
        boolean z = this.documentOnly;
        if (z && this.currentDocumentsType == null && (this.currentType.type instanceof TLRPC$TL_secureValueTypeAddress)) {
            builder.setMessage(LocaleController.getString("PassportDeleteAddressAlert", 2131627259));
        } else if (z && this.currentDocumentsType == null && (this.currentType.type instanceof TLRPC$TL_secureValueTypePersonalDetails)) {
            builder.setMessage(LocaleController.getString("PassportDeletePersonalAlert", 2131627266));
        } else {
            builder.setMessage(LocaleController.getString("PassportDeleteDocumentAlert", 2131627262));
        }
        if (!this.documentOnly && this.currentDocumentsType != null) {
            FrameLayout frameLayout = new FrameLayout(getParentActivity());
            CheckBoxCell checkBoxCell = new CheckBoxCell(getParentActivity(), 1);
            checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            TLRPC$SecureValueType tLRPC$SecureValueType = this.currentType.type;
            if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
                checkBoxCell.setText(LocaleController.getString("PassportDeleteDocumentAddress", 2131627261), "", true, false);
            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                checkBoxCell.setText(LocaleController.getString("PassportDeleteDocumentPersonal", 2131627263), "", true, false);
            }
            checkBoxCell.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
            frameLayout.addView(checkBoxCell, LayoutHelper.createFrame(-1, 48, 51));
            checkBoxCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda31(zArr));
            builder.setView(frameLayout);
        }
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$createDocumentDeleteAlert$38(boolean[] zArr, DialogInterface dialogInterface, int i) {
        if (!this.documentOnly) {
            this.currentValues.clear();
        }
        this.currentDocumentValues.clear();
        this.delegate.deleteValue(this.currentType, this.currentDocumentsType, this.availableDocumentTypes, zArr[0], null, null);
        finishFragment();
    }

    public static /* synthetic */ void lambda$createDocumentDeleteAlert$39(boolean[] zArr, View view) {
        if (!view.isEnabled()) {
            return;
        }
        zArr[0] = !zArr[0];
        ((CheckBoxCell) view).setChecked(zArr[0], true);
    }

    public void onFieldError(View view) {
        if (view == null) {
            return;
        }
        Vibrator vibrator = (Vibrator) getParentActivity().getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(200L);
        }
        AndroidUtilities.shakeView(view, 2.0f, 0);
        scrollToField(view);
    }

    private void scrollToField(View view) {
        while (view != null && this.linearLayout2.indexOfChild(view) < 0) {
            view = (View) view.getParent();
        }
        if (view != null) {
            this.scrollView.smoothScrollTo(0, view.getTop() - ((this.scrollView.getMeasuredHeight() - view.getMeasuredHeight()) / 2));
        }
    }

    public String getDocumentHash(SecureDocument secureDocument) {
        byte[] bArr;
        if (secureDocument != null) {
            TLRPC$TL_secureFile tLRPC$TL_secureFile = secureDocument.secureFile;
            if (tLRPC$TL_secureFile != null && (bArr = tLRPC$TL_secureFile.file_hash) != null) {
                return Base64.encodeToString(bArr, 2);
            }
            byte[] bArr2 = secureDocument.fileHash;
            return bArr2 != null ? Base64.encodeToString(bArr2, 2) : "";
        }
        return "";
    }

    public void checkFieldForError(EditTextBoldCursor editTextBoldCursor, String str, Editable editable, boolean z) {
        String str2;
        String str3;
        String str4;
        HashMap<String, String> hashMap = this.errorsValues;
        if (hashMap != null && (str2 = hashMap.get(str)) != null) {
            if (TextUtils.equals(str2, editable)) {
                HashMap<String, String> hashMap2 = this.fieldsErrors;
                if (hashMap2 != null && (str4 = hashMap2.get(str)) != null) {
                    editTextBoldCursor.setErrorText(str4);
                } else {
                    HashMap<String, String> hashMap3 = this.documentsErrors;
                    if (hashMap3 != null && (str3 = hashMap3.get(str)) != null) {
                        editTextBoldCursor.setErrorText(str3);
                    }
                }
            } else {
                editTextBoldCursor.setErrorText(null);
            }
        } else {
            editTextBoldCursor.setErrorText(null);
        }
        String str5 = z ? "error_document_all" : "error_all";
        HashMap<String, String> hashMap4 = this.errorsValues;
        if (hashMap4 == null || !hashMap4.containsKey(str5)) {
            return;
        }
        this.errorsValues.remove(str5);
        checkTopErrorCell(false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:126:0x021a, code lost:
        if (r6 != 5) goto L132;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0290, code lost:
        if (r8 > 24) goto L172;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x029a, code lost:
        if (r8 < 2) goto L172;
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x02aa, code lost:
        if (r8 < 2) goto L172;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x02b3, code lost:
        if (r8 > 10) goto L172;
     */
    /* JADX WARN: Removed duplicated region for block: B:134:0x022f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean checkFieldsForError() {
        View[] viewArr;
        boolean z;
        View view;
        if (this.currentDocumentsType != null) {
            if (this.errorsValues.containsKey("error_all") || this.errorsValues.containsKey("error_document_all")) {
                onFieldError(this.topErrorCell);
                return true;
            }
            if (this.uploadDocumentCell != null) {
                if (this.documents.isEmpty()) {
                    onFieldError(this.uploadDocumentCell);
                    return true;
                }
                int size = this.documents.size();
                for (int i = 0; i < size; i++) {
                    SecureDocument secureDocument = this.documents.get(i);
                    String str = "files" + getDocumentHash(secureDocument);
                    if (str != null && this.errorsValues.containsKey(str)) {
                        onFieldError(this.documentsCells.get(secureDocument));
                        return true;
                    }
                }
            }
            if (this.errorsValues.containsKey("files_all") || this.errorsValues.containsKey("translation_all")) {
                onFieldError(this.bottomCell);
                return true;
            }
            View view2 = this.uploadFrontCell;
            if (view2 != null) {
                if (this.frontDocument == null) {
                    onFieldError(view2);
                    return true;
                }
                if (this.errorsValues.containsKey("front" + getDocumentHash(this.frontDocument))) {
                    onFieldError(this.documentsCells.get(this.frontDocument));
                    return true;
                }
            }
            TLRPC$SecureValueType tLRPC$SecureValueType = this.currentDocumentsType.type;
            if (((tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense)) && (view = this.uploadReverseCell) != null) {
                if (this.reverseDocument == null) {
                    onFieldError(view);
                    return true;
                }
                if (this.errorsValues.containsKey("reverse" + getDocumentHash(this.reverseDocument))) {
                    onFieldError(this.documentsCells.get(this.reverseDocument));
                    return true;
                }
            }
            View view3 = this.uploadSelfieCell;
            if (view3 != null && this.currentBotId != 0) {
                if (this.selfieDocument == null) {
                    onFieldError(view3);
                    return true;
                }
                if (this.errorsValues.containsKey("selfie" + getDocumentHash(this.selfieDocument))) {
                    onFieldError(this.documentsCells.get(this.selfieDocument));
                    return true;
                }
            }
            if (this.uploadTranslationCell != null && this.currentBotId != 0) {
                if (this.translationDocuments.isEmpty()) {
                    onFieldError(this.uploadTranslationCell);
                    return true;
                }
                int size2 = this.translationDocuments.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    SecureDocument secureDocument2 = this.translationDocuments.get(i2);
                    if (this.errorsValues.containsKey("translation" + getDocumentHash(secureDocument2))) {
                        onFieldError(this.documentsCells.get(secureDocument2));
                        return true;
                    }
                }
            }
        }
        for (int i3 = 0; i3 < 2; i3++) {
            if (i3 == 0) {
                viewArr = this.inputFields;
            } else {
                TextInfoPrivacyCell textInfoPrivacyCell = this.nativeInfoCell;
                viewArr = (textInfoPrivacyCell == null || textInfoPrivacyCell.getVisibility() != 0) ? null : this.inputExtraFields;
            }
            if (viewArr != null) {
                int i4 = 0;
                while (i4 < viewArr.length) {
                    boolean hasErrorText = viewArr[i4].hasErrorText();
                    if (!this.errorsValues.isEmpty()) {
                        TLRPC$SecureValueType tLRPC$SecureValueType2 = this.currentType.type;
                        String str2 = "country_code";
                        if (!(tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypePersonalDetails)) {
                            if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeAddress) {
                                if (i4 == 0) {
                                    str2 = "street_line1";
                                } else if (i4 == 1) {
                                    str2 = "street_line2";
                                } else if (i4 == 2) {
                                    str2 = "post_code";
                                } else if (i4 == 3) {
                                    str2 = "city";
                                } else if (i4 == 4) {
                                    str2 = "state";
                                }
                                if (str2 != null) {
                                }
                            }
                            str2 = null;
                            if (str2 != null) {
                            }
                        } else if (i3 == 0) {
                            switch (i4) {
                                case 0:
                                    str2 = "first_name";
                                    break;
                                case 1:
                                    str2 = "middle_name";
                                    break;
                                case 2:
                                    str2 = "last_name";
                                    break;
                                case 3:
                                    str2 = "birth_date";
                                    break;
                                case 4:
                                    str2 = "gender";
                                    break;
                                case 5:
                                    break;
                                case 6:
                                    str2 = "residence_country_code";
                                    break;
                                case 7:
                                    str2 = "document_no";
                                    break;
                                case 8:
                                    str2 = "expiry_date";
                                    break;
                                default:
                                    str2 = null;
                                    break;
                            }
                            if (str2 != null) {
                                String str3 = this.errorsValues.get(str2);
                                if (!TextUtils.isEmpty(str3) && str3.equals(viewArr[i4].getText().toString())) {
                                    hasErrorText = true;
                                }
                            }
                        } else {
                            if (i4 == 0) {
                                str2 = "first_name_native";
                            } else if (i4 != 1) {
                                if (i4 == 2) {
                                    str2 = "last_name_native";
                                }
                                str2 = null;
                            } else {
                                str2 = "middle_name_native";
                            }
                            if (str2 != null) {
                            }
                        }
                    }
                    if (!this.documentOnly || this.currentDocumentsType == null || i4 >= 7) {
                        if (!hasErrorText) {
                            int length = viewArr[i4].length();
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
                            } else if (i4 != 8) {
                                if ((i3 == 0 && (i4 == 0 || i4 == 2 || i4 == 1)) || (i3 == 1 && (i4 == 0 || i4 == 1 || i4 == 2))) {
                                    if (length > 255) {
                                        hasErrorText = true;
                                    }
                                    if ((i3 == 0 && i4 == 1) || (i3 == 1 && i4 == 1)) {
                                        z = true;
                                    }
                                } else if (i4 == 7) {
                                }
                                z = false;
                            } else {
                                continue;
                            }
                            if (!hasErrorText && !z && length == 0) {
                                hasErrorText = true;
                            }
                        }
                        if (hasErrorText) {
                            onFieldError(viewArr[i4]);
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

    private void createIdentityInterface(Context context) {
        String str;
        HashMap<String, String> hashMap;
        String str2;
        HashMap<String, String> hashMap2;
        this.languageMap = new HashMap<>();
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
        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165437, "windowBackgroundGrayShadow"));
        this.topErrorCell.setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
        int i = -1;
        this.linearLayout2.addView(this.topErrorCell, LayoutHelper.createLinear(-1, -2));
        checkTopErrorCell(true);
        if (this.currentDocumentsType != null) {
            HeaderCell headerCell = new HeaderCell(context);
            this.headerCell = headerCell;
            if (this.documentOnly) {
                headerCell.setText(LocaleController.getString("PassportDocuments", 2131627276));
            } else {
                headerCell.setText(LocaleController.getString("PassportRequiredDocuments", 2131627377));
            }
            this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
            LinearLayout linearLayout = new LinearLayout(context);
            this.frontLayout = linearLayout;
            linearLayout.setOrientation(1);
            this.linearLayout2.addView(this.frontLayout, LayoutHelper.createLinear(-1, -2));
            TextDetailSettingsCell textDetailSettingsCell = new TextDetailSettingsCell(context);
            this.uploadFrontCell = textDetailSettingsCell;
            textDetailSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.linearLayout2.addView(this.uploadFrontCell, LayoutHelper.createLinear(-1, -2));
            this.uploadFrontCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda22(this));
            LinearLayout linearLayout2 = new LinearLayout(context);
            this.reverseLayout = linearLayout2;
            linearLayout2.setOrientation(1);
            this.linearLayout2.addView(this.reverseLayout, LayoutHelper.createLinear(-1, -2));
            boolean z = this.currentDocumentsType.selfie_required;
            TextDetailSettingsCell textDetailSettingsCell2 = new TextDetailSettingsCell(context);
            this.uploadReverseCell = textDetailSettingsCell2;
            textDetailSettingsCell2.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.uploadReverseCell.setTextAndValue(LocaleController.getString("PassportReverseSide", 2131627380), LocaleController.getString("PassportReverseSideInfo", 2131627381), z);
            this.linearLayout2.addView(this.uploadReverseCell, LayoutHelper.createLinear(-1, -2));
            this.uploadReverseCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda24(this));
            if (this.currentDocumentsType.selfie_required) {
                LinearLayout linearLayout3 = new LinearLayout(context);
                this.selfieLayout = linearLayout3;
                linearLayout3.setOrientation(1);
                this.linearLayout2.addView(this.selfieLayout, LayoutHelper.createLinear(-1, -2));
                TextDetailSettingsCell textDetailSettingsCell3 = new TextDetailSettingsCell(context);
                this.uploadSelfieCell = textDetailSettingsCell3;
                textDetailSettingsCell3.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.uploadSelfieCell.setTextAndValue(LocaleController.getString("PassportSelfie", 2131627389), LocaleController.getString("PassportSelfieInfo", 2131627390), this.currentType.translation_required);
                this.linearLayout2.addView(this.uploadSelfieCell, LayoutHelper.createLinear(-1, -2));
                this.uploadSelfieCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda21(this));
            }
            TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell2;
            textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165435, "windowBackgroundGrayShadow"));
            this.bottomCell.setText(LocaleController.getString("PassportPersonalUploadInfo", 2131627363));
            this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
            if (this.currentDocumentsType.translation_required) {
                HeaderCell headerCell2 = new HeaderCell(context);
                this.headerCell = headerCell2;
                headerCell2.setText(LocaleController.getString("PassportTranslation", 2131627397));
                this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
                LinearLayout linearLayout4 = new LinearLayout(context);
                this.translationLayout = linearLayout4;
                linearLayout4.setOrientation(1);
                this.linearLayout2.addView(this.translationLayout, LayoutHelper.createLinear(-1, -2));
                TextSettingsCell textSettingsCell = new TextSettingsCell(context);
                this.uploadTranslationCell = textSettingsCell;
                textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.linearLayout2.addView(this.uploadTranslationCell, LayoutHelper.createLinear(-1, -2));
                this.uploadTranslationCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda12(this));
                TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
                this.bottomCellTranslation = textInfoPrivacyCell3;
                textInfoPrivacyCell3.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165435, "windowBackgroundGrayShadow"));
                if (this.currentBotId != 0) {
                    this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationUploadInfo", 2131627248);
                } else {
                    TLRPC$SecureValueType tLRPC$SecureValueType = this.currentDocumentsType.type;
                    if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddPassportInfo", 2131627238);
                    } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddInternalPassportInfo", 2131627235);
                    } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddIdentityCardInfo", 2131627233);
                    } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddDriverLicenceInfo", 2131627232);
                    } else {
                        this.noAllTranslationErrorText = "";
                    }
                }
                CharSequence charSequence = this.noAllTranslationErrorText;
                HashMap<String, String> hashMap3 = this.documentsErrors;
                SpannableStringBuilder spannableStringBuilder = charSequence;
                if (hashMap3 != null) {
                    String str3 = hashMap3.get("translation_all");
                    spannableStringBuilder = charSequence;
                    if (str3 != null) {
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(str3);
                        spannableStringBuilder2.append((CharSequence) "\n\n");
                        spannableStringBuilder2.append(this.noAllTranslationErrorText);
                        spannableStringBuilder2.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteRedText3")), 0, str3.length(), 33);
                        this.errorsValues.put("translation_all", "");
                        spannableStringBuilder = spannableStringBuilder2;
                    }
                }
                this.bottomCellTranslation.setText(spannableStringBuilder);
                this.linearLayout2.addView(this.bottomCellTranslation, LayoutHelper.createLinear(-1, -2));
            }
        } else if (Build.VERSION.SDK_INT >= 18) {
            TextSettingsCell textSettingsCell2 = new TextSettingsCell(context);
            this.scanDocumentCell = textSettingsCell2;
            textSettingsCell2.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.scanDocumentCell.setText(LocaleController.getString("PassportScanPassport", 2131627382), false);
            this.linearLayout2.addView(this.scanDocumentCell, LayoutHelper.createLinear(-1, -2));
            this.scanDocumentCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda19(this));
            TextInfoPrivacyCell textInfoPrivacyCell4 = new TextInfoPrivacyCell(context);
            this.bottomCell = textInfoPrivacyCell4;
            textInfoPrivacyCell4.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165435, "windowBackgroundGrayShadow"));
            this.bottomCell.setText(LocaleController.getString("PassportScanPassportInfo", 2131627383));
            this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
        }
        HeaderCell headerCell3 = new HeaderCell(context);
        this.headerCell = headerCell3;
        if (this.documentOnly) {
            headerCell3.setText(LocaleController.getString("PassportDocument", 2131627274));
        } else {
            headerCell3.setText(LocaleController.getString("PassportPersonal", 2131627360));
        }
        this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
        int i2 = this.currentDocumentsType != null ? 9 : 7;
        this.inputFields = new EditTextBoldCursor[i2];
        int i3 = 0;
        while (true) {
            int i4 = 6;
            if (i3 < i2) {
                EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
                this.inputFields[i3] = editTextBoldCursor;
                AnonymousClass15 anonymousClass15 = new AnonymousClass15(this, context, editTextBoldCursor);
                anonymousClass15.setWillNotDraw(false);
                this.linearLayout2.addView(anonymousClass15, LayoutHelper.createLinear(i, 64));
                anonymousClass15.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                if (i3 == i2 - 1) {
                    View view = new View(context);
                    this.extraBackgroundView = view;
                    view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    this.linearLayout2.addView(this.extraBackgroundView, LayoutHelper.createLinear(i, 6));
                }
                if (this.documentOnly && this.currentDocumentsType != null) {
                    if (i3 < 7) {
                        anonymousClass15.setVisibility(8);
                        View view2 = this.extraBackgroundView;
                        if (view2 != null) {
                            view2.setVisibility(8);
                        }
                    }
                }
                this.inputFields[i3].setTag(Integer.valueOf(i3));
                this.inputFields[i3].setSupportRtlHint(true);
                this.inputFields[i3].setTextSize(1, 16.0f);
                this.inputFields[i3].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
                this.inputFields[i3].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.inputFields[i3].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
                this.inputFields[i3].setTransformHintToHeader(true);
                this.inputFields[i3].setBackgroundDrawable(null);
                this.inputFields[i3].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.inputFields[i3].setCursorSize(AndroidUtilities.dp(20.0f));
                this.inputFields[i3].setCursorWidth(1.5f);
                this.inputFields[i3].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
                if (i3 == 5 || i3 == 6) {
                    this.inputFields[i3].setOnTouchListener(new PassportActivity$$ExternalSyntheticLambda36(this));
                    this.inputFields[i3].setInputType(0);
                } else if (i3 == 3 || i3 == 8) {
                    this.inputFields[i3].setOnTouchListener(new PassportActivity$$ExternalSyntheticLambda38(this, context));
                    this.inputFields[i3].setInputType(0);
                    this.inputFields[i3].setFocusable(false);
                } else if (i3 == 4) {
                    this.inputFields[i3].setOnTouchListener(new PassportActivity$$ExternalSyntheticLambda37(this));
                    this.inputFields[i3].setInputType(0);
                    this.inputFields[i3].setFocusable(false);
                } else {
                    this.inputFields[i3].setInputType(16385);
                    this.inputFields[i3].setImeOptions(268435461);
                }
                switch (i3) {
                    case 0:
                        if (this.currentType.native_names) {
                            this.inputFields[i3].setHintText(LocaleController.getString("PassportNameLatin", 2131627351));
                        } else {
                            this.inputFields[i3].setHintText(LocaleController.getString("PassportName", 2131627348));
                        }
                        hashMap2 = this.currentValues;
                        str2 = "first_name";
                        break;
                    case 1:
                        if (this.currentType.native_names) {
                            this.inputFields[i3].setHintText(LocaleController.getString("PassportMidnameLatin", 2131627347));
                        } else {
                            this.inputFields[i3].setHintText(LocaleController.getString("PassportMidname", 2131627345));
                        }
                        hashMap2 = this.currentValues;
                        str2 = "middle_name";
                        break;
                    case 2:
                        if (this.currentType.native_names) {
                            this.inputFields[i3].setHintText(LocaleController.getString("PassportSurnameLatin", 2131627396));
                        } else {
                            this.inputFields[i3].setHintText(LocaleController.getString("PassportSurname", 2131627394));
                        }
                        hashMap2 = this.currentValues;
                        str2 = "last_name";
                        break;
                    case 3:
                        this.inputFields[i3].setHintText(LocaleController.getString("PassportBirthdate", 2131627254));
                        hashMap2 = this.currentValues;
                        str2 = "birth_date";
                        break;
                    case 4:
                        this.inputFields[i3].setHintText(LocaleController.getString("PassportGender", 2131627286));
                        hashMap2 = this.currentValues;
                        str2 = "gender";
                        break;
                    case 5:
                        this.inputFields[i3].setHintText(LocaleController.getString("PassportCitizenship", 2131627255));
                        hashMap2 = this.currentValues;
                        str2 = "country_code";
                        break;
                    case 6:
                        this.inputFields[i3].setHintText(LocaleController.getString("PassportResidence", 2131627378));
                        hashMap2 = this.currentValues;
                        str2 = "residence_country_code";
                        break;
                    case 7:
                        this.inputFields[i3].setHintText(LocaleController.getString("PassportDocumentNumber", 2131627275));
                        hashMap2 = this.currentDocumentValues;
                        str2 = "document_no";
                        break;
                    case 8:
                        this.inputFields[i3].setHintText(LocaleController.getString("PassportExpired", 2131627282));
                        hashMap2 = this.currentDocumentValues;
                        str2 = "expiry_date";
                        break;
                    default:
                        i3++;
                        i = -1;
                }
                setFieldValues(hashMap2, this.inputFields[i3], str2);
                EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                editTextBoldCursorArr[i3].setSelection(editTextBoldCursorArr[i3].length());
                if (i3 == 0 || i3 == 2 || i3 == 1) {
                    this.inputFields[i3].addTextChangedListener(new AnonymousClass16(editTextBoldCursor, str2));
                } else {
                    this.inputFields[i3].addTextChangedListener(new AnonymousClass17(editTextBoldCursor, str2, hashMap2));
                }
                this.inputFields[i3].setPadding(0, 0, 0, 0);
                this.inputFields[i3].setGravity((LocaleController.isRTL ? 5 : 3) | 16);
                anonymousClass15.addView(this.inputFields[i3], LayoutHelper.createFrame(-1, -1.0f, 51, 21.0f, 0.0f, 21.0f, 0.0f));
                this.inputFields[i3].setOnEditorActionListener(new PassportActivity$$ExternalSyntheticLambda43(this));
                i3++;
                i = -1;
            } else {
                ShadowSectionCell shadowSectionCell = new ShadowSectionCell(context);
                this.sectionCell2 = shadowSectionCell;
                this.linearLayout2.addView(shadowSectionCell, LayoutHelper.createLinear(-1, -2));
                HeaderCell headerCell4 = new HeaderCell(context);
                this.headerCell = headerCell4;
                headerCell4.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
                int i5 = 3;
                this.inputExtraFields = new EditTextBoldCursor[3];
                int i6 = 0;
                while (i6 < i5) {
                    EditTextBoldCursor editTextBoldCursor2 = new EditTextBoldCursor(context);
                    this.inputExtraFields[i6] = editTextBoldCursor2;
                    AnonymousClass18 anonymousClass18 = new AnonymousClass18(this, context, editTextBoldCursor2);
                    anonymousClass18.setWillNotDraw(false);
                    this.linearLayout2.addView(anonymousClass18, LayoutHelper.createLinear(-1, 64));
                    anonymousClass18.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    if (i6 == 2) {
                        View view3 = new View(context);
                        this.extraBackgroundView2 = view3;
                        view3.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        this.linearLayout2.addView(this.extraBackgroundView2, LayoutHelper.createLinear(-1, i4));
                    }
                    this.inputExtraFields[i6].setTag(Integer.valueOf(i6));
                    this.inputExtraFields[i6].setSupportRtlHint(true);
                    this.inputExtraFields[i6].setTextSize(1, 16.0f);
                    this.inputExtraFields[i6].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
                    this.inputExtraFields[i6].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.inputExtraFields[i6].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
                    this.inputExtraFields[i6].setTransformHintToHeader(true);
                    this.inputExtraFields[i6].setBackgroundDrawable(null);
                    this.inputExtraFields[i6].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.inputExtraFields[i6].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.inputExtraFields[i6].setCursorWidth(1.5f);
                    this.inputExtraFields[i6].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
                    this.inputExtraFields[i6].setInputType(16385);
                    this.inputExtraFields[i6].setImeOptions(268435461);
                    if (i6 == 0) {
                        hashMap = this.currentValues;
                        str = "first_name_native";
                    } else if (i6 == 1) {
                        hashMap = this.currentValues;
                        str = "middle_name_native";
                    } else if (i6 != 2) {
                        i6++;
                        i5 = 3;
                        i4 = 6;
                    } else {
                        hashMap = this.currentValues;
                        str = "last_name_native";
                    }
                    setFieldValues(hashMap, this.inputExtraFields[i6], str);
                    EditTextBoldCursor[] editTextBoldCursorArr2 = this.inputExtraFields;
                    editTextBoldCursorArr2[i6].setSelection(editTextBoldCursorArr2[i6].length());
                    if (i6 == 0 || i6 == 2 || i6 == 1) {
                        this.inputExtraFields[i6].addTextChangedListener(new AnonymousClass19(editTextBoldCursor2, str));
                    }
                    this.inputExtraFields[i6].setPadding(0, 0, 0, 0);
                    this.inputExtraFields[i6].setGravity((LocaleController.isRTL ? 5 : 3) | 16);
                    anonymousClass18.addView(this.inputExtraFields[i6], LayoutHelper.createFrame(-1, -1.0f, 51, 21.0f, 0.0f, 21.0f, 0.0f));
                    this.inputExtraFields[i6].setOnEditorActionListener(new PassportActivity$$ExternalSyntheticLambda41(this));
                    i6++;
                    i5 = 3;
                    i4 = 6;
                }
                TextInfoPrivacyCell textInfoPrivacyCell5 = new TextInfoPrivacyCell(context);
                this.nativeInfoCell = textInfoPrivacyCell5;
                this.linearLayout2.addView(textInfoPrivacyCell5, LayoutHelper.createLinear(-1, -2));
                if (((this.currentBotId != 0 || this.currentDocumentsType == null) && this.currentTypeValue != null && !this.documentOnly) || this.currentDocumentsTypeValue != null) {
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
                    TextSettingsCell textSettingsCell3 = new TextSettingsCell(context);
                    textSettingsCell3.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
                    textSettingsCell3.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                    if (this.currentDocumentsType == null) {
                        textSettingsCell3.setText(LocaleController.getString("PassportDeleteInfo", 2131627265), false);
                    } else {
                        textSettingsCell3.setText(LocaleController.getString("PassportDeleteDocument", 2131627260), false);
                    }
                    this.linearLayout2.addView(textSettingsCell3, LayoutHelper.createLinear(-1, -2));
                    textSettingsCell3.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda26(this));
                    this.nativeInfoCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165435, "windowBackgroundGrayShadow"));
                    ShadowSectionCell shadowSectionCell2 = new ShadowSectionCell(context);
                    this.sectionCell = shadowSectionCell2;
                    shadowSectionCell2.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
                    this.linearLayout2.addView(this.sectionCell, LayoutHelper.createLinear(-1, -2));
                } else {
                    this.nativeInfoCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165436, "windowBackgroundGrayShadow"));
                }
                updateInterfaceStringsForDocumentType();
                checkNativeFields(false);
                return;
            }
        }
    }

    public /* synthetic */ void lambda$createIdentityInterface$40(View view) {
        this.uploadingFileType = 2;
        openAttachMenu();
    }

    public /* synthetic */ void lambda$createIdentityInterface$41(View view) {
        this.uploadingFileType = 3;
        openAttachMenu();
    }

    public /* synthetic */ void lambda$createIdentityInterface$42(View view) {
        this.uploadingFileType = 1;
        openAttachMenu();
    }

    public /* synthetic */ void lambda$createIdentityInterface$43(View view) {
        this.uploadingFileType = 4;
        openAttachMenu();
    }

    public /* synthetic */ void lambda$createIdentityInterface$44(View view) {
        if (Build.VERSION.SDK_INT >= 23 && getParentActivity().checkSelfPermission("android.permission.CAMERA") != 0) {
            getParentActivity().requestPermissions(new String[]{"android.permission.CAMERA"}, 22);
            return;
        }
        CameraScanActivity cameraScanActivity = new CameraScanActivity(0);
        cameraScanActivity.setDelegate(new AnonymousClass14());
        presentFragment(cameraScanActivity);
    }

    /* renamed from: org.telegram.ui.PassportActivity$14 */
    /* loaded from: classes3.dex */
    public class AnonymousClass14 implements CameraScanActivity.CameraScanActivityDelegate {
        @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
        public /* synthetic */ void didFindQr(String str) {
            CameraScanActivity.CameraScanActivityDelegate.CC.$default$didFindQr(this, str);
        }

        @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
        public /* synthetic */ boolean processQr(String str, Runnable runnable) {
            return CameraScanActivity.CameraScanActivityDelegate.CC.$default$processQr(this, str, runnable);
        }

        AnonymousClass14() {
            PassportActivity.this = r1;
        }

        @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
        public void didFindMrzInfo(MrzRecognizer.Result result) {
            if (!TextUtils.isEmpty(result.firstName)) {
                PassportActivity.this.inputFields[0].setText(result.firstName);
            }
            if (!TextUtils.isEmpty(result.middleName)) {
                PassportActivity.this.inputFields[1].setText(result.middleName);
            }
            if (!TextUtils.isEmpty(result.lastName)) {
                PassportActivity.this.inputFields[2].setText(result.lastName);
            }
            int i = result.gender;
            if (i != 0) {
                if (i == 1) {
                    PassportActivity.this.currentGender = "male";
                    PassportActivity.this.inputFields[4].setText(LocaleController.getString("PassportMale", 2131627344));
                } else if (i == 2) {
                    PassportActivity.this.currentGender = "female";
                    PassportActivity.this.inputFields[4].setText(LocaleController.getString("PassportFemale", 2131627283));
                }
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
    }

    /* renamed from: org.telegram.ui.PassportActivity$15 */
    /* loaded from: classes3.dex */
    public class AnonymousClass15 extends FrameLayout {
        private StaticLayout errorLayout;
        private float offsetX;
        final /* synthetic */ EditTextBoldCursor val$field;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass15(PassportActivity passportActivity, Context context, EditTextBoldCursor editTextBoldCursor) {
            super(context);
            this.val$field = editTextBoldCursor;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i) - AndroidUtilities.dp(34.0f);
            StaticLayout errorLayout = this.val$field.getErrorLayout(size);
            this.errorLayout = errorLayout;
            if (errorLayout != null) {
                int lineCount = errorLayout.getLineCount();
                int i3 = 0;
                if (lineCount > 1) {
                    i2 = View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.errorLayout.getLineBottom(lineCount - 1) - this.errorLayout.getLineBottom(0)), 1073741824);
                }
                if (LocaleController.isRTL) {
                    float f = 0.0f;
                    while (true) {
                        if (i3 >= lineCount) {
                            break;
                        } else if (this.errorLayout.getLineLeft(i3) != 0.0f) {
                            this.offsetX = 0.0f;
                            break;
                        } else {
                            f = Math.max(f, this.errorLayout.getLineWidth(i3));
                            if (i3 == lineCount - 1) {
                                this.offsetX = size - f;
                            }
                            i3++;
                        }
                    }
                }
            }
            super.onMeasure(i, i2);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.errorLayout != null) {
                canvas.save();
                canvas.translate(AndroidUtilities.dp(21.0f) + this.offsetX, this.val$field.getLineY() + AndroidUtilities.dp(3.0f));
                this.errorLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    public /* synthetic */ boolean lambda$createIdentityInterface$46(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(false);
            countrySelectActivity.setCountrySelectActivityDelegate(new PassportActivity$$ExternalSyntheticLambda72(this, view));
            presentFragment(countrySelectActivity);
        }
        return true;
    }

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

    public /* synthetic */ boolean lambda$createIdentityInterface$49(Context context, View view, MotionEvent motionEvent) {
        String str;
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
                EditTextBoldCursor editTextBoldCursor = (EditTextBoldCursor) view;
                int intValue = ((Integer) editTextBoldCursor.getTag()).intValue();
                if (intValue == 8) {
                    str = LocaleController.getString("PassportSelectExpiredDate", 2131627385);
                    i3 = 0;
                    i2 = 20;
                    i = 0;
                } else {
                    str = LocaleController.getString("PassportSelectBithdayDate", 2131627384);
                    i3 = -120;
                    i2 = 0;
                    i = -18;
                }
                String[] split = editTextBoldCursor.getText().toString().split("\\.");
                if (split.length == 3) {
                    int intValue2 = Utilities.parseInt((CharSequence) split[0]).intValue();
                    int intValue3 = Utilities.parseInt((CharSequence) split[1]).intValue();
                    i4 = Utilities.parseInt((CharSequence) split[2]).intValue();
                    i6 = intValue2;
                    i5 = intValue3;
                } else {
                    i6 = -1;
                    i5 = -1;
                    i4 = -1;
                }
                AlertDialog.Builder createDatePickerDialog = AlertsCreator.createDatePickerDialog(context, i3, i2, i, i6, i5, i4, str, intValue == 8, new PassportActivity$$ExternalSyntheticLambda69(this, intValue, editTextBoldCursor));
                if (intValue == 8) {
                    createDatePickerDialog.setNegativeButton(LocaleController.getString("PassportSelectNotExpire", 2131627387), new PassportActivity$$ExternalSyntheticLambda10(this, editTextBoldCursor));
                }
                showDialog(createDatePickerDialog.create());
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return true;
    }

    public /* synthetic */ void lambda$createIdentityInterface$47(int i, EditTextBoldCursor editTextBoldCursor, int i2, int i3, int i4) {
        if (i == 8) {
            int[] iArr = this.currentExpireDate;
            iArr[0] = i2;
            iArr[1] = i3 + 1;
            iArr[2] = i4;
        }
        editTextBoldCursor.setText(String.format(Locale.US, "%02d.%02d.%d", Integer.valueOf(i4), Integer.valueOf(i3 + 1), Integer.valueOf(i2)));
    }

    public /* synthetic */ void lambda$createIdentityInterface$48(EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface, int i) {
        int[] iArr = this.currentExpireDate;
        iArr[2] = 0;
        iArr[1] = 0;
        iArr[0] = 0;
        editTextBoldCursor.setText(LocaleController.getString("PassportNoExpireDate", 2131627358));
    }

    public /* synthetic */ boolean lambda$createIdentityInterface$51(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("PassportSelectGender", 2131627386));
            builder.setItems(new CharSequence[]{LocaleController.getString("PassportMale", 2131627344), LocaleController.getString("PassportFemale", 2131627283)}, new PassportActivity$$ExternalSyntheticLambda3(this));
            builder.setPositiveButton(LocaleController.getString("Cancel", 2131624832), null);
            showDialog(builder.create());
        }
        return true;
    }

    public /* synthetic */ void lambda$createIdentityInterface$50(DialogInterface dialogInterface, int i) {
        if (i == 0) {
            this.currentGender = "male";
            this.inputFields[4].setText(LocaleController.getString("PassportMale", 2131627344));
        } else if (i != 1) {
        } else {
            this.currentGender = "female";
            this.inputFields[4].setText(LocaleController.getString("PassportFemale", 2131627283));
        }
    }

    /* renamed from: org.telegram.ui.PassportActivity$16 */
    /* loaded from: classes3.dex */
    public class AnonymousClass16 implements TextWatcher {
        private boolean ignore;
        final /* synthetic */ EditTextBoldCursor val$field;
        final /* synthetic */ String val$key;

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass16(EditTextBoldCursor editTextBoldCursor, String str) {
            PassportActivity.this = r1;
            this.val$field = editTextBoldCursor;
            this.val$key = str;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            boolean z;
            if (this.ignore) {
                return;
            }
            int intValue = ((Integer) this.val$field.getTag()).intValue();
            int i = 0;
            while (true) {
                if (i >= editable.length()) {
                    z = false;
                    break;
                }
                char charAt = editable.charAt(i);
                if ((charAt < '0' || charAt > '9') && ((charAt < 'a' || charAt > 'z') && !((charAt >= 'A' && charAt <= 'Z') || charAt == ' ' || charAt == '\'' || charAt == ',' || charAt == '.' || charAt == '&' || charAt == '-' || charAt == '/'))) {
                    z = true;
                    break;
                }
                i++;
            }
            if (!z || PassportActivity.this.allowNonLatinName) {
                PassportActivity.this.nonLatinNames[intValue] = z;
                PassportActivity.this.checkFieldForError(this.val$field, this.val$key, editable, false);
                return;
            }
            this.val$field.setErrorText(LocaleController.getString("PassportUseLatinOnly", 2131627403));
        }
    }

    /* renamed from: org.telegram.ui.PassportActivity$17 */
    /* loaded from: classes3.dex */
    public class AnonymousClass17 implements TextWatcher {
        final /* synthetic */ EditTextBoldCursor val$field;
        final /* synthetic */ String val$key;
        final /* synthetic */ HashMap val$values;

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass17(EditTextBoldCursor editTextBoldCursor, String str, HashMap hashMap) {
            PassportActivity.this = r1;
            this.val$field = editTextBoldCursor;
            this.val$key = str;
            this.val$values = hashMap;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            PassportActivity passportActivity = PassportActivity.this;
            passportActivity.checkFieldForError(this.val$field, this.val$key, editable, this.val$values == passportActivity.currentDocumentValues);
            int intValue = ((Integer) this.val$field.getTag()).intValue();
            EditTextBoldCursor editTextBoldCursor = PassportActivity.this.inputFields[intValue];
            if (intValue == 6) {
                PassportActivity.this.checkNativeFields(true);
            }
        }
    }

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

    /* renamed from: org.telegram.ui.PassportActivity$18 */
    /* loaded from: classes3.dex */
    public class AnonymousClass18 extends FrameLayout {
        private StaticLayout errorLayout;
        private float offsetX;
        final /* synthetic */ EditTextBoldCursor val$field;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass18(PassportActivity passportActivity, Context context, EditTextBoldCursor editTextBoldCursor) {
            super(context);
            this.val$field = editTextBoldCursor;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i) - AndroidUtilities.dp(34.0f);
            StaticLayout errorLayout = this.val$field.getErrorLayout(size);
            this.errorLayout = errorLayout;
            if (errorLayout != null) {
                int lineCount = errorLayout.getLineCount();
                int i3 = 0;
                if (lineCount > 1) {
                    i2 = View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.errorLayout.getLineBottom(lineCount - 1) - this.errorLayout.getLineBottom(0)), 1073741824);
                }
                if (LocaleController.isRTL) {
                    float f = 0.0f;
                    while (true) {
                        if (i3 >= lineCount) {
                            break;
                        } else if (this.errorLayout.getLineLeft(i3) != 0.0f) {
                            this.offsetX = 0.0f;
                            break;
                        } else {
                            f = Math.max(f, this.errorLayout.getLineWidth(i3));
                            if (i3 == lineCount - 1) {
                                this.offsetX = size - f;
                            }
                            i3++;
                        }
                    }
                }
            }
            super.onMeasure(i, i2);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.errorLayout != null) {
                canvas.save();
                canvas.translate(AndroidUtilities.dp(21.0f) + this.offsetX, this.val$field.getLineY() + AndroidUtilities.dp(3.0f));
                this.errorLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    /* renamed from: org.telegram.ui.PassportActivity$19 */
    /* loaded from: classes3.dex */
    public class AnonymousClass19 implements TextWatcher {
        private boolean ignore;
        final /* synthetic */ EditTextBoldCursor val$field;
        final /* synthetic */ String val$key;

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass19(EditTextBoldCursor editTextBoldCursor, String str) {
            PassportActivity.this = r1;
            this.val$field = editTextBoldCursor;
            this.val$key = str;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (this.ignore) {
                return;
            }
            PassportActivity.this.checkFieldForError(this.val$field, this.val$key, editable, false);
        }
    }

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

    public /* synthetic */ void lambda$createIdentityInterface$54(View view) {
        createDocumentDeleteAlert();
    }

    private void updateInterfaceStringsForDocumentType() {
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType = this.currentDocumentsType;
        if (tLRPC$TL_secureRequiredType != null) {
            this.actionBar.setTitle(getTextForType(tLRPC$TL_secureRequiredType.type));
        } else {
            this.actionBar.setTitle(LocaleController.getString("PassportPersonal", 2131627360));
        }
        updateUploadText(2);
        updateUploadText(3);
        updateUploadText(1);
        updateUploadText(4);
    }

    /* JADX WARN: Code restructure failed: missing block: B:41:0x0083, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_secureValueTypeDriverLicense) == false) goto L42;
     */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00c3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateUploadText(int i) {
        TLRPC$SecureValueType tLRPC$SecureValueType;
        boolean z = true;
        int i2 = 0;
        if (i == 0) {
            if (this.uploadDocumentCell == null) {
                return;
            }
            if (this.documents.size() >= 1) {
                this.uploadDocumentCell.setText(LocaleController.getString("PassportUploadAdditinalDocument", 2131627399), false);
            } else {
                this.uploadDocumentCell.setText(LocaleController.getString("PassportUploadDocument", 2131627400), false);
            }
        } else if (i == 1) {
            TextDetailSettingsCell textDetailSettingsCell = this.uploadSelfieCell;
            if (textDetailSettingsCell == null) {
                return;
            }
            if (this.selfieDocument != null) {
                i2 = 8;
            }
            textDetailSettingsCell.setVisibility(i2);
        } else if (i == 4) {
            if (this.uploadTranslationCell == null) {
                return;
            }
            if (this.translationDocuments.size() >= 1) {
                this.uploadTranslationCell.setText(LocaleController.getString("PassportUploadAdditinalDocument", 2131627399), false);
            } else {
                this.uploadTranslationCell.setText(LocaleController.getString("PassportUploadDocument", 2131627400), false);
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
                    textDetailSettingsCell2.setTextAndValue(LocaleController.getString("PassportMainPage", 2131627342), LocaleController.getString("PassportMainPageInfo", 2131627343), z);
                } else {
                    textDetailSettingsCell2.setTextAndValue(LocaleController.getString("PassportFrontSide", 2131627284), LocaleController.getString("PassportFrontSideInfo", 2131627285), z);
                }
                TextDetailSettingsCell textDetailSettingsCell3 = this.uploadFrontCell;
                if (this.frontDocument != null) {
                    i2 = 8;
                }
                textDetailSettingsCell3.setVisibility(i2);
            }
            z = false;
            tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
            if (!(tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport)) {
            }
            textDetailSettingsCell2.setTextAndValue(LocaleController.getString("PassportMainPage", 2131627342), LocaleController.getString("PassportMainPageInfo", 2131627343), z);
            TextDetailSettingsCell textDetailSettingsCell32 = this.uploadFrontCell;
            if (this.frontDocument != null) {
            }
            textDetailSettingsCell32.setVisibility(i2);
        } else if (i != 3 || this.uploadReverseCell == null) {
        } else {
            TLRPC$SecureValueType tLRPC$SecureValueType3 = this.currentDocumentsType.type;
            if ((tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeIdentityCard) || (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeDriverLicense)) {
                this.reverseLayout.setVisibility(0);
                TextDetailSettingsCell textDetailSettingsCell4 = this.uploadReverseCell;
                if (this.reverseDocument != null) {
                    i2 = 8;
                }
                textDetailSettingsCell4.setVisibility(i2);
                return;
            }
            this.reverseLayout.setVisibility(8);
            this.uploadReverseCell.setVisibility(8);
        }
    }

    private void checkTopErrorCell(boolean z) {
        String str;
        String str2;
        if (this.topErrorCell == null) {
            return;
        }
        SpannableStringBuilder spannableStringBuilder = null;
        if (this.fieldsErrors != null && ((z || this.errorsValues.containsKey("error_all")) && (str2 = this.fieldsErrors.get("error_all")) != null)) {
            spannableStringBuilder = new SpannableStringBuilder(str2);
            if (z) {
                this.errorsValues.put("error_all", "");
            }
        }
        if (this.documentsErrors != null && ((z || this.errorsValues.containsKey("error_document_all")) && (str = this.documentsErrors.get("error_all")) != null)) {
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
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteRedText3")), 0, spannableStringBuilder.length(), 33);
            this.topErrorCell.setText(spannableStringBuilder);
            this.topErrorCell.setVisibility(0);
        } else if (this.topErrorCell.getVisibility() == 8) {
        } else {
            this.topErrorCell.setVisibility(8);
        }
    }

    private void addDocumentViewInternal(TLRPC$TL_secureFile tLRPC$TL_secureFile, int i) {
        addDocumentView(new SecureDocument(getSecureDocumentKey(tLRPC$TL_secureFile.secret, tLRPC$TL_secureFile.file_hash), tLRPC$TL_secureFile, null, null, null), i);
    }

    private void addDocumentViews(ArrayList<TLRPC$SecureFile> arrayList) {
        this.documents.clear();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLRPC$SecureFile tLRPC$SecureFile = arrayList.get(i);
            if (tLRPC$SecureFile instanceof TLRPC$TL_secureFile) {
                addDocumentViewInternal((TLRPC$TL_secureFile) tLRPC$SecureFile, 0);
            }
        }
    }

    private void addTranslationDocumentViews(ArrayList<TLRPC$SecureFile> arrayList) {
        this.translationDocuments.clear();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLRPC$SecureFile tLRPC$SecureFile = arrayList.get(i);
            if (tLRPC$SecureFile instanceof TLRPC$TL_secureFile) {
                addDocumentViewInternal((TLRPC$TL_secureFile) tLRPC$SecureFile, 4);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x009d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setFieldValues(HashMap<String, String> hashMap, EditTextBoldCursor editTextBoldCursor, String str) {
        CharSequence charSequence;
        CharSequence charSequence2;
        boolean z;
        String str2 = hashMap.get(str);
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
                    CharSequence charSequence3 = (String) this.languageMap.get(str2);
                    if (charSequence3 != null) {
                        editTextBoldCursor.setText(charSequence3);
                        break;
                    }
                    break;
                case 1:
                    if ("male".equals(str2)) {
                        this.currentGender = str2;
                        editTextBoldCursor.setText(LocaleController.getString("PassportMale", 2131627344));
                        break;
                    } else if ("female".equals(str2)) {
                        this.currentGender = str2;
                        editTextBoldCursor.setText(LocaleController.getString("PassportFemale", 2131627283));
                        break;
                    }
                    break;
                case 2:
                    if (!TextUtils.isEmpty(str2)) {
                        String[] split = str2.split("\\.");
                        if (split.length == 3) {
                            this.currentExpireDate[0] = Utilities.parseInt((CharSequence) split[2]).intValue();
                            this.currentExpireDate[1] = Utilities.parseInt((CharSequence) split[1]).intValue();
                            this.currentExpireDate[2] = Utilities.parseInt((CharSequence) split[0]).intValue();
                            editTextBoldCursor.setText(str2);
                            z = true;
                            if (!z) {
                                int[] iArr = this.currentExpireDate;
                                iArr[2] = 0;
                                iArr[1] = 0;
                                iArr[0] = 0;
                                editTextBoldCursor.setText(LocaleController.getString("PassportNoExpireDate", 2131627358));
                                break;
                            }
                        }
                    }
                    z = false;
                    if (!z) {
                    }
                    break;
                case 3:
                    this.currentCitizeship = str2;
                    CharSequence charSequence4 = (String) this.languageMap.get(str2);
                    if (charSequence4 != null) {
                        editTextBoldCursor.setText(charSequence4);
                        break;
                    }
                    break;
                default:
                    editTextBoldCursor.setText(str2);
                    break;
            }
        }
        HashMap<String, String> hashMap2 = this.fieldsErrors;
        if (hashMap2 != null && (charSequence2 = (String) hashMap2.get(str)) != null) {
            editTextBoldCursor.setErrorText(charSequence2);
            this.errorsValues.put(str, editTextBoldCursor.getText().toString());
            return;
        }
        HashMap<String, String> hashMap3 = this.documentsErrors;
        if (hashMap3 == null || (charSequence = (String) hashMap3.get(str)) == null) {
            return;
        }
        editTextBoldCursor.setErrorText(charSequence);
        this.errorsValues.put(str, editTextBoldCursor.getText().toString());
    }

    private void addDocumentView(SecureDocument secureDocument, int i) {
        String string;
        String str;
        String str2;
        HashMap<String, String> hashMap;
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
        SecureDocumentCell secureDocumentCell = new SecureDocumentCell(getParentActivity());
        secureDocumentCell.setTag(secureDocument);
        secureDocumentCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.documentsCells.put(secureDocument, secureDocumentCell);
        String documentHash = getDocumentHash(secureDocument);
        if (i == 1) {
            string = LocaleController.getString("PassportSelfie", 2131627389);
            this.selfieLayout.addView(secureDocumentCell, LayoutHelper.createLinear(-1, -2));
            str = "selfie" + documentHash;
        } else if (i == 4) {
            string = LocaleController.getString("AttachPhoto", 2131624513);
            this.translationLayout.addView(secureDocumentCell, LayoutHelper.createLinear(-1, -2));
            str = "translation" + documentHash;
        } else if (i == 2) {
            TLRPC$SecureValueType tLRPC$SecureValueType = this.currentDocumentsType.type;
            if ((tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport)) {
                string = LocaleController.getString("PassportMainPage", 2131627342);
            } else {
                string = LocaleController.getString("PassportFrontSide", 2131627284);
            }
            this.frontLayout.addView(secureDocumentCell, LayoutHelper.createLinear(-1, -2));
            str = "front" + documentHash;
        } else if (i == 3) {
            string = LocaleController.getString("PassportReverseSide", 2131627380);
            this.reverseLayout.addView(secureDocumentCell, LayoutHelper.createLinear(-1, -2));
            str = "reverse" + documentHash;
        } else {
            string = LocaleController.getString("AttachPhoto", 2131624513);
            this.documentsLayout.addView(secureDocumentCell, LayoutHelper.createLinear(-1, -2));
            str = "files" + documentHash;
        }
        String str3 = str;
        if (str3 != null && (hashMap = this.documentsErrors) != null && (str2 = hashMap.get(str3)) != null) {
            secureDocumentCell.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
            this.errorsValues.put(str3, "");
        } else {
            str2 = LocaleController.formatDateForBan(secureDocument.secureFile.date);
        }
        secureDocumentCell.setTextAndValueAndImage(string, str2, secureDocument);
        secureDocumentCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda29(this, i));
        secureDocumentCell.setOnLongClickListener(new PassportActivity$$ExternalSyntheticLambda33(this, i, secureDocument, secureDocumentCell, str3));
    }

    public /* synthetic */ void lambda$addDocumentView$55(int i, View view) {
        this.uploadingFileType = i;
        if (i == 1) {
            this.currentPhotoViewerLayout = this.selfieLayout;
        } else if (i == 4) {
            this.currentPhotoViewerLayout = this.translationLayout;
        } else if (i == 2) {
            this.currentPhotoViewerLayout = this.frontLayout;
        } else if (i == 3) {
            this.currentPhotoViewerLayout = this.reverseLayout;
        } else {
            this.currentPhotoViewerLayout = this.documentsLayout;
        }
        SecureDocument secureDocument = (SecureDocument) view.getTag();
        PhotoViewer.getInstance().setParentActivity(getParentActivity());
        if (i == 1) {
            ArrayList<SecureDocument> arrayList = new ArrayList<>();
            arrayList.add(this.selfieDocument);
            PhotoViewer.getInstance().openPhoto(arrayList, 0, this.provider);
        } else if (i == 2) {
            ArrayList<SecureDocument> arrayList2 = new ArrayList<>();
            arrayList2.add(this.frontDocument);
            PhotoViewer.getInstance().openPhoto(arrayList2, 0, this.provider);
        } else if (i == 3) {
            ArrayList<SecureDocument> arrayList3 = new ArrayList<>();
            arrayList3.add(this.reverseDocument);
            PhotoViewer.getInstance().openPhoto(arrayList3, 0, this.provider);
        } else if (i == 0) {
            PhotoViewer photoViewer = PhotoViewer.getInstance();
            ArrayList<SecureDocument> arrayList4 = this.documents;
            photoViewer.openPhoto(arrayList4, arrayList4.indexOf(secureDocument), this.provider);
        } else {
            PhotoViewer photoViewer2 = PhotoViewer.getInstance();
            ArrayList<SecureDocument> arrayList5 = this.translationDocuments;
            photoViewer2.openPhoto(arrayList5, arrayList5.indexOf(secureDocument), this.provider);
        }
    }

    public /* synthetic */ boolean lambda$addDocumentView$57(int i, SecureDocument secureDocument, SecureDocumentCell secureDocumentCell, String str, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        if (i == 1) {
            builder.setMessage(LocaleController.getString("PassportDeleteSelfie", 2131627270));
        } else {
            builder.setMessage(LocaleController.getString("PassportDeleteScan", 2131627268));
        }
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        builder.setTitle(LocaleController.getString("AppName", 2131624384));
        builder.setPositiveButton(LocaleController.getString("OK", 2131627127), new PassportActivity$$ExternalSyntheticLambda6(this, secureDocument, i, secureDocumentCell, str));
        showDialog(builder.create());
        return true;
    }

    public /* synthetic */ void lambda$addDocumentView$56(SecureDocument secureDocument, int i, SecureDocumentCell secureDocumentCell, String str, DialogInterface dialogInterface, int i2) {
        this.documentsCells.remove(secureDocument);
        if (i == 1) {
            this.selfieDocument = null;
            this.selfieLayout.removeView(secureDocumentCell);
        } else if (i == 4) {
            this.translationDocuments.remove(secureDocument);
            this.translationLayout.removeView(secureDocumentCell);
        } else if (i == 2) {
            this.frontDocument = null;
            this.frontLayout.removeView(secureDocumentCell);
        } else if (i == 3) {
            this.reverseDocument = null;
            this.reverseLayout.removeView(secureDocumentCell);
        } else {
            this.documents.remove(secureDocument);
            this.documentsLayout.removeView(secureDocumentCell);
        }
        if (str != null) {
            HashMap<String, String> hashMap = this.documentsErrors;
            if (hashMap != null) {
                hashMap.remove(str);
            }
            HashMap<String, String> hashMap2 = this.errorsValues;
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

    private String getNameForType(TLRPC$SecureValueType tLRPC$SecureValueType) {
        return tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails ? "personal_details" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport ? "passport" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport ? "internal_passport" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense ? "driver_license" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard ? "identity_card" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill ? "utility_bill" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress ? "address" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement ? "bank_statement" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement ? "rental_agreement" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration ? "temporary_registration" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration ? "passport_registration" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail ? "email" : tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone ? "phone" : "";
    }

    private TextDetailSecureCell getViewByType(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType) {
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2;
        TextDetailSecureCell textDetailSecureCell = this.typesViews.get(tLRPC$TL_secureRequiredType);
        return (textDetailSecureCell != null || (tLRPC$TL_secureRequiredType2 = this.documentsToTypesLink.get(tLRPC$TL_secureRequiredType)) == null) ? textDetailSecureCell : this.typesViews.get(tLRPC$TL_secureRequiredType2);
    }

    private String getTextForType(TLRPC$SecureValueType tLRPC$SecureValueType) {
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) {
            return LocaleController.getString("ActionBotDocumentPassport", 2131624145);
        }
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense) {
            return LocaleController.getString("ActionBotDocumentDriverLicence", 2131624140);
        }
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard) {
            return LocaleController.getString("ActionBotDocumentIdentityCard", 2131624143);
        }
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill) {
            return LocaleController.getString("ActionBotDocumentUtilityBill", 2131624150);
        }
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement) {
            return LocaleController.getString("ActionBotDocumentBankStatement", 2131624139);
        }
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
            return LocaleController.getString("ActionBotDocumentRentalAgreement", 2131624148);
        }
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport) {
            return LocaleController.getString("ActionBotDocumentInternalPassport", 2131624144);
        }
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration) {
            return LocaleController.getString("ActionBotDocumentPassportRegistration", 2131624146);
        }
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
            return LocaleController.getString("ActionBotDocumentTemporaryRegistration", 2131624149);
        }
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone) {
            return LocaleController.getString("ActionBotDocumentPhone", 2131624147);
        }
        return tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail ? LocaleController.getString("ActionBotDocumentEmail", 2131624141) : "";
    }

    /* JADX WARN: Removed duplicated region for block: B:107:0x0243 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0290 A[Catch: Exception -> 0x03e3, TryCatch #5 {Exception -> 0x03e3, blocks: (B:99:0x0218, B:103:0x0236, B:122:0x0289, B:124:0x028d, B:126:0x0290, B:129:0x029a), top: B:316:0x0218 }] */
    /* JADX WARN: Removed duplicated region for block: B:174:0x0341  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x0380 A[Catch: Exception -> 0x0395, TRY_LEAVE, TryCatch #3 {Exception -> 0x0395, blocks: (B:132:0x02a6, B:134:0x02ae, B:136:0x02b4, B:138:0x02be, B:140:0x02c6, B:143:0x02d0, B:145:0x02d6, B:147:0x02de, B:149:0x02e6, B:151:0x02ee, B:154:0x02f7, B:155:0x02fd, B:156:0x0302, B:163:0x0320, B:166:0x032a, B:169:0x0334, B:178:0x0347, B:179:0x034b, B:181:0x0353, B:182:0x0360, B:184:0x0368, B:185:0x0375, B:187:0x0380), top: B:312:0x02a6 }] */
    /* JADX WARN: Removed duplicated region for block: B:204:0x03ef  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0400  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0405  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0414  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0417  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x0456  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x04c8  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x057b  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x057e  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x0589  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setTypeValue(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, String str, String str2, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, String str3, boolean z, int i) {
        boolean z2;
        TLRPC$TL_secureValue tLRPC$TL_secureValue;
        StringBuilder sb;
        String str4;
        TextDetailSecureCell textDetailSecureCell;
        TLRPC$TL_secureValue tLRPC$TL_secureValue2;
        String str5;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3;
        HashMap<String, String> hashMap;
        boolean z3;
        TextDetailSecureCell textDetailSecureCell2;
        TLRPC$SecureValueType tLRPC$SecureValueType;
        StringBuilder sb2;
        Object obj;
        String[] strArr;
        String[] strArr2;
        String str6;
        String str7;
        HashMap<String, String> hashMap2;
        String[] strArr3;
        HashMap<String, String> hashMap3;
        String[] strArr4;
        Object obj2;
        JSONObject jSONObject;
        JSONObject jSONObject2;
        int i2;
        String str8;
        String str9;
        JSONObject jSONObject3;
        HashMap<String, String> hashMap4;
        Object obj3;
        StringBuilder sb3;
        char c;
        String str10;
        Throwable th;
        int i3;
        int i4;
        Exception e;
        BufferedReader bufferedReader;
        String str11 = str2;
        String str12 = str3;
        TextDetailSecureCell textDetailSecureCell3 = this.typesViews.get(tLRPC$TL_secureRequiredType);
        if (textDetailSecureCell3 != null) {
            z2 = true;
        } else if (this.currentActivityType != 8) {
            return;
        } else {
            ArrayList<TLRPC$TL_secureRequiredType> arrayList = new ArrayList<>();
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
        HashMap<String, String> hashMap5 = this.typesValues.get(tLRPC$TL_secureRequiredType);
        HashMap<String, String> hashMap6 = tLRPC$TL_secureRequiredType2 != null ? this.typesValues.get(tLRPC$TL_secureRequiredType2) : null;
        TLRPC$TL_secureValue valueByType = getValueByType(tLRPC$TL_secureRequiredType, z2);
        TLRPC$TL_secureValue valueByType2 = getValueByType(tLRPC$TL_secureRequiredType2, z2);
        if (str11 != null && this.languageMap == null) {
            this.languageMap = new HashMap<>();
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(ApplicationLoader.applicationContext.getResources().getAssets().open("countries.txt")));
            } catch (Exception e2) {
                e = e2;
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
                } catch (Exception e3) {
                    e = e3;
                }
                e = e3;
                FileLog.e(e);
                sb = null;
            }
            tLRPC$TL_secureValue = valueByType;
            bufferedReader.close();
            sb = null;
        } else {
            tLRPC$TL_secureValue = valueByType;
            sb = null;
            this.languageMap = null;
        }
        if (str != null) {
            TLRPC$SecureValueType tLRPC$SecureValueType2 = tLRPC$TL_secureRequiredType.type;
            if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypePhone) {
                PhoneFormat phoneFormat = PhoneFormat.getInstance();
                str5 = phoneFormat.format("+" + str);
                textDetailSecureCell = textDetailSecureCell4;
                tLRPC$TL_secureValue2 = valueByType2;
            } else {
                textDetailSecureCell = textDetailSecureCell4;
                tLRPC$TL_secureValue2 = valueByType2;
                if (!(tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeEmail)) {
                    str4 = "PassportDocuments";
                    str5 = null;
                    if (z) {
                        tLRPC$TL_secureRequiredType3 = tLRPC$TL_secureRequiredType;
                        hashMap = this.errorsMap.get(getNameForType(tLRPC$TL_secureRequiredType3.type));
                    } else {
                        tLRPC$TL_secureRequiredType3 = tLRPC$TL_secureRequiredType;
                        hashMap = null;
                    }
                    HashMap<String, String> hashMap7 = tLRPC$TL_secureRequiredType2 == null ? this.errorsMap.get(getNameForType(tLRPC$TL_secureRequiredType2.type)) : null;
                    if ((hashMap == null && hashMap.size() > 0) || (hashMap7 != null && hashMap7.size() > 0)) {
                        String str13 = !z ? this.mainErrorsMap.get(getNameForType(tLRPC$TL_secureRequiredType3.type)) : null;
                        str5 = str13 == null ? this.mainErrorsMap.get(getNameForType(tLRPC$TL_secureRequiredType2.type)) : str13;
                        textDetailSecureCell2 = textDetailSecureCell;
                        z3 = true;
                    } else {
                        tLRPC$SecureValueType = tLRPC$TL_secureRequiredType3.type;
                        if (!(tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails)) {
                            if (TextUtils.isEmpty(str5)) {
                                if (tLRPC$TL_secureRequiredType2 == null) {
                                    str5 = LocaleController.getString("PassportPersonalDetailsInfo", 2131627362);
                                } else if (this.currentActivityType == 8) {
                                    str5 = LocaleController.getString(str4, 2131627276);
                                } else if (i == 1) {
                                    TLRPC$SecureValueType tLRPC$SecureValueType3 = tLRPC$TL_secureRequiredType2.type;
                                    if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypePassport) {
                                        str5 = LocaleController.getString("PassportIdentityPassport", 2131627292);
                                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                                        str5 = LocaleController.getString("PassportIdentityInternalPassport", 2131627291);
                                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                                        str5 = LocaleController.getString("PassportIdentityDriverLicence", 2131627289);
                                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                                        str5 = LocaleController.getString("PassportIdentityID", 2131627290);
                                    }
                                } else {
                                    str5 = LocaleController.getString("PassportIdentityDocumentInfo", 2131627288);
                                }
                                textDetailSecureCell2 = textDetailSecureCell;
                                z3 = false;
                            }
                            textDetailSecureCell2 = textDetailSecureCell;
                            z3 = false;
                        } else {
                            String str14 = str4;
                            if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
                                if (TextUtils.isEmpty(str5)) {
                                    if (tLRPC$TL_secureRequiredType2 == null) {
                                        str5 = LocaleController.getString("PassportAddressNoUploadInfo", 2131627252);
                                    } else if (this.currentActivityType == 8) {
                                        str5 = LocaleController.getString(str14, 2131627276);
                                    } else if (i == 1) {
                                        TLRPC$SecureValueType tLRPC$SecureValueType4 = tLRPC$TL_secureRequiredType2.type;
                                        if (tLRPC$SecureValueType4 instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                                            str5 = LocaleController.getString("PassportAddAgreementInfo", 2131627226);
                                        } else if (tLRPC$SecureValueType4 instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                                            str5 = LocaleController.getString("PassportAddBillInfo", 2131627230);
                                        } else if (tLRPC$SecureValueType4 instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                                            str5 = LocaleController.getString("PassportAddPassportRegistrationInfo", 2131627240);
                                        } else if (tLRPC$SecureValueType4 instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                                            str5 = LocaleController.getString("PassportAddTemporaryRegistrationInfo", 2131627242);
                                        } else if (tLRPC$SecureValueType4 instanceof TLRPC$TL_secureValueTypeBankStatement) {
                                            str5 = LocaleController.getString("PassportAddBankInfo", 2131627228);
                                        }
                                    } else {
                                        str5 = LocaleController.getString("PassportAddressInfo", 2131627251);
                                    }
                                    textDetailSecureCell2 = textDetailSecureCell;
                                    z3 = false;
                                }
                            } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone) {
                                if (TextUtils.isEmpty(str5)) {
                                    str5 = LocaleController.getString("PassportPhoneInfo", 2131627365);
                                }
                            } else if ((tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail) && TextUtils.isEmpty(str5)) {
                                str5 = LocaleController.getString("PassportEmailInfo", 2131627279);
                            }
                            textDetailSecureCell2 = textDetailSecureCell;
                            z3 = false;
                        }
                    }
                    textDetailSecureCell2.setValue(str5);
                    textDetailSecureCell2.valueTextView.setTextColor(Theme.getColor(!z3 ? "windowBackgroundWhiteRedText3" : "windowBackgroundWhiteGrayText2"));
                    textDetailSecureCell2.setChecked(z3 && this.currentActivityType != 8 && ((z && tLRPC$TL_secureRequiredType2 != null) || (!z && tLRPC$TL_secureValue != null)) && (tLRPC$TL_secureRequiredType2 == null || tLRPC$TL_secureValue2 != null));
                }
                str5 = str;
            }
            str4 = "PassportDocuments";
            if (z) {
            }
            if (tLRPC$TL_secureRequiredType2 == null) {
            }
            if (hashMap == null) {
            }
            tLRPC$SecureValueType = tLRPC$TL_secureRequiredType3.type;
            if (!(tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails)) {
            }
            textDetailSecureCell2.setValue(str5);
            textDetailSecureCell2.valueTextView.setTextColor(Theme.getColor(!z3 ? "windowBackgroundWhiteRedText3" : "windowBackgroundWhiteGrayText2"));
            textDetailSecureCell2.setChecked(z3 && this.currentActivityType != 8 && ((z && tLRPC$TL_secureRequiredType2 != null) || (!z && tLRPC$TL_secureValue != null)) && (tLRPC$TL_secureRequiredType2 == null || tLRPC$TL_secureValue2 != null));
        }
        if (this.currentActivityType == 8 || tLRPC$TL_secureRequiredType2 == null || (TextUtils.isEmpty(str3) && valueByType2 == null)) {
            sb2 = sb;
        } else {
            sb2 = new StringBuilder();
            if (i > 1) {
                sb2.append(getTextForType(tLRPC$TL_secureRequiredType2.type));
            } else if (TextUtils.isEmpty(str3)) {
                sb2.append(LocaleController.getString("PassportDocuments", 2131627276));
            }
        }
        if (str11 == null && str12 == null) {
            textDetailSecureCell = textDetailSecureCell4;
            tLRPC$TL_secureValue2 = valueByType2;
            str4 = "PassportDocuments";
        } else if (hashMap5 == null) {
            return;
        } else {
            hashMap5.clear();
            TLRPC$SecureValueType tLRPC$SecureValueType5 = tLRPC$TL_secureRequiredType.type;
            tLRPC$TL_secureValue2 = valueByType2;
            textDetailSecureCell = textDetailSecureCell4;
            String str15 = "last_name";
            str4 = "PassportDocuments";
            HashMap<String, String> hashMap8 = hashMap5;
            String str16 = "middle_name_native";
            if (tLRPC$SecureValueType5 instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                int i5 = this.currentActivityType;
                if ((i5 != 0 || z) && !(i5 == 8 && tLRPC$TL_secureRequiredType2 == null)) {
                    obj = "gender";
                    i4 = 8;
                    strArr2 = null;
                } else {
                    obj = "gender";
                    i4 = 8;
                    strArr2 = new String[]{"first_name", "middle_name", str15, "first_name_native", str16, "last_name_native", "birth_date", "gender", "country_code", "residence_country_code"};
                }
                strArr = (i5 == 0 || (i5 == i4 && tLRPC$TL_secureRequiredType2 != null)) ? new String[]{"document_no", "expiry_date"} : null;
            } else {
                obj = "gender";
                strArr2 = (!(tLRPC$SecureValueType5 instanceof TLRPC$TL_secureValueTypeAddress) || (((i3 = this.currentActivityType) != 0 || z) && !(i3 == 8 && tLRPC$TL_secureRequiredType2 == null))) ? null : new String[]{"street_line1", "street_line2", "post_code", "city", "state", "country_code"};
                strArr = null;
            }
            if (strArr2 != null || strArr != null) {
                StringBuilder sb4 = sb2;
                String[] strArr5 = strArr2;
                JSONObject jSONObject4 = null;
                int i6 = 0;
                int i7 = 2;
                String[] strArr6 = null;
                while (i6 < i7) {
                    if (i6 == 0) {
                        if (str11 != null) {
                            try {
                                jSONObject = new JSONObject(str11);
                                strArr4 = strArr5;
                                if (strArr4 != null || jSONObject == null) {
                                    str7 = str15;
                                    str6 = str16;
                                    hashMap2 = hashMap6;
                                    strArr3 = strArr;
                                    hashMap3 = hashMap8;
                                    obj2 = obj;
                                    jSONObject2 = jSONObject;
                                    i7 = 2;
                                } else {
                                    try {
                                        Iterator<String> keys = jSONObject.keys();
                                        while (keys.hasNext()) {
                                            Iterator<String> it = keys;
                                            String next = keys.next();
                                            if (i6 == 0) {
                                                strArr3 = strArr;
                                                hashMap3 = hashMap8;
                                                try {
                                                    hashMap3.put(next, jSONObject.getString(next));
                                                } catch (Throwable th2) {
                                                    th = th2;
                                                    FileLog.e(th);
                                                    i2 = 0;
                                                    while (i2 < strArr4.length) {
                                                    }
                                                    str7 = str15;
                                                    str6 = str16;
                                                    hashMap2 = hashMap6;
                                                    jSONObject2 = jSONObject;
                                                    i7 = 2;
                                                    obj2 = obj;
                                                    jSONObject4 = jSONObject2;
                                                    i6++;
                                                    str12 = str3;
                                                    obj = obj2;
                                                    hashMap8 = hashMap3;
                                                    strArr = strArr3;
                                                    hashMap6 = hashMap2;
                                                    str15 = str7;
                                                    str16 = str6;
                                                    strArr6 = strArr4;
                                                    str11 = str2;
                                                }
                                            } else {
                                                strArr3 = strArr;
                                                hashMap3 = hashMap8;
                                                hashMap6.put(next, jSONObject.getString(next));
                                            }
                                            hashMap8 = hashMap3;
                                            strArr = strArr3;
                                            keys = it;
                                        }
                                        strArr3 = strArr;
                                        hashMap3 = hashMap8;
                                    } catch (Throwable th3) {
                                        th = th3;
                                        strArr3 = strArr;
                                        hashMap3 = hashMap8;
                                    }
                                    i2 = 0;
                                    while (i2 < strArr4.length) {
                                        if (jSONObject.has(strArr4[i2])) {
                                            if (sb4 == null) {
                                                sb3 = new StringBuilder();
                                                hashMap4 = hashMap6;
                                            } else {
                                                hashMap4 = hashMap6;
                                                sb3 = sb4;
                                            }
                                            try {
                                                String string = jSONObject.getString(strArr4[i2]);
                                                if (string == null || TextUtils.isEmpty(string)) {
                                                    str9 = str15;
                                                    str8 = str16;
                                                    jSONObject3 = jSONObject;
                                                } else {
                                                    jSONObject3 = jSONObject;
                                                    if (!"first_name_native".equals(strArr4[i2]) && !str16.equals(strArr4[i2]) && !"last_name_native".equals(strArr4[i2])) {
                                                        if (sb3.length() > 0) {
                                                            if (!str15.equals(strArr4[i2]) && !"last_name_native".equals(strArr4[i2]) && !"middle_name".equals(strArr4[i2]) && !str16.equals(strArr4[i2])) {
                                                                sb3.append(", ");
                                                            }
                                                            sb3.append(" ");
                                                        }
                                                        String str17 = strArr4[i2];
                                                        str9 = str15;
                                                        int hashCode = str17.hashCode();
                                                        str8 = str16;
                                                        if (hashCode == -2006252145) {
                                                            obj3 = obj;
                                                            if (str17.equals("residence_country_code")) {
                                                                c = 1;
                                                                if (c != 0) {
                                                                }
                                                                str10 = this.languageMap.get(string);
                                                                if (str10 != null) {
                                                                }
                                                            }
                                                            c = 65535;
                                                            if (c != 0) {
                                                            }
                                                            str10 = this.languageMap.get(string);
                                                            if (str10 != null) {
                                                            }
                                                        } else if (hashCode == -1249512767) {
                                                            obj3 = obj;
                                                            if (str17.equals(obj3)) {
                                                                c = 2;
                                                                if (c != 0) {
                                                                }
                                                                str10 = this.languageMap.get(string);
                                                                if (str10 != null) {
                                                                }
                                                            }
                                                            c = 65535;
                                                            if (c != 0) {
                                                            }
                                                            str10 = this.languageMap.get(string);
                                                            if (str10 != null) {
                                                            }
                                                        } else if (hashCode == 1481071862 && str17.equals("country_code")) {
                                                            obj3 = obj;
                                                            c = 0;
                                                            if (c != 0 || c == 1) {
                                                                str10 = this.languageMap.get(string);
                                                                if (str10 != null) {
                                                                    sb3.append(str10);
                                                                }
                                                            } else if (c == 2) {
                                                                if ("male".equals(string)) {
                                                                    sb3.append(LocaleController.getString("PassportMale", 2131627344));
                                                                } else if ("female".equals(string)) {
                                                                    sb3.append(LocaleController.getString("PassportFemale", 2131627283));
                                                                }
                                                            } else {
                                                                sb3.append(string);
                                                            }
                                                        } else {
                                                            obj3 = obj;
                                                            c = 65535;
                                                            if (c != 0) {
                                                            }
                                                            str10 = this.languageMap.get(string);
                                                            if (str10 != null) {
                                                            }
                                                        }
                                                        sb4 = sb3;
                                                    }
                                                    str9 = str15;
                                                    str8 = str16;
                                                }
                                                obj3 = obj;
                                                sb4 = sb3;
                                            } catch (Exception unused) {
                                                sb2 = sb3;
                                            }
                                        } else {
                                            str9 = str15;
                                            str8 = str16;
                                            hashMap4 = hashMap6;
                                            jSONObject3 = jSONObject;
                                            obj3 = obj;
                                        }
                                        i2++;
                                        obj = obj3;
                                        hashMap6 = hashMap4;
                                        jSONObject = jSONObject3;
                                        str15 = str9;
                                        str16 = str8;
                                    }
                                    str7 = str15;
                                    str6 = str16;
                                    hashMap2 = hashMap6;
                                    jSONObject2 = jSONObject;
                                    i7 = 2;
                                    obj2 = obj;
                                }
                                jSONObject4 = jSONObject2;
                            } catch (Exception unused2) {
                            }
                        }
                        jSONObject = jSONObject4;
                        strArr4 = strArr6;
                        if (strArr4 != null) {
                        }
                        str7 = str15;
                        str6 = str16;
                        hashMap2 = hashMap6;
                        strArr3 = strArr;
                        hashMap3 = hashMap8;
                        obj2 = obj;
                        jSONObject2 = jSONObject;
                        i7 = 2;
                        jSONObject4 = jSONObject2;
                    } else if (hashMap6 == null) {
                        str7 = str15;
                        str6 = str16;
                        hashMap2 = hashMap6;
                        strArr4 = strArr6;
                        i7 = 2;
                        obj2 = obj;
                        strArr3 = strArr;
                        hashMap3 = hashMap8;
                    } else {
                        if (str12 != null) {
                            jSONObject = new JSONObject(str12);
                            strArr4 = strArr;
                            if (strArr4 != null) {
                            }
                            str7 = str15;
                            str6 = str16;
                            hashMap2 = hashMap6;
                            strArr3 = strArr;
                            hashMap3 = hashMap8;
                            obj2 = obj;
                            jSONObject2 = jSONObject;
                            i7 = 2;
                            jSONObject4 = jSONObject2;
                        }
                        jSONObject = jSONObject4;
                        strArr4 = strArr6;
                        if (strArr4 != null) {
                        }
                        str7 = str15;
                        str6 = str16;
                        hashMap2 = hashMap6;
                        strArr3 = strArr;
                        hashMap3 = hashMap8;
                        obj2 = obj;
                        jSONObject2 = jSONObject;
                        i7 = 2;
                        jSONObject4 = jSONObject2;
                    }
                    i6++;
                    str12 = str3;
                    obj = obj2;
                    hashMap8 = hashMap3;
                    strArr = strArr3;
                    hashMap6 = hashMap2;
                    str15 = str7;
                    str16 = str6;
                    strArr6 = strArr4;
                    str11 = str2;
                }
                sb2 = sb4;
            }
        }
        if (sb2 != null) {
            str5 = sb2.toString();
            if (z) {
            }
            if (tLRPC$TL_secureRequiredType2 == null) {
            }
            if (hashMap == null) {
            }
            tLRPC$SecureValueType = tLRPC$TL_secureRequiredType3.type;
            if (!(tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails)) {
            }
            textDetailSecureCell2.setValue(str5);
            textDetailSecureCell2.valueTextView.setTextColor(Theme.getColor(!z3 ? "windowBackgroundWhiteRedText3" : "windowBackgroundWhiteGrayText2"));
            textDetailSecureCell2.setChecked(z3 && this.currentActivityType != 8 && ((z && tLRPC$TL_secureRequiredType2 != null) || (!z && tLRPC$TL_secureValue != null)) && (tLRPC$TL_secureRequiredType2 == null || tLRPC$TL_secureValue2 != null));
        }
        str5 = null;
        if (z) {
        }
        if (tLRPC$TL_secureRequiredType2 == null) {
        }
        if (hashMap == null) {
        }
        tLRPC$SecureValueType = tLRPC$TL_secureRequiredType3.type;
        if (!(tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails)) {
        }
        textDetailSecureCell2.setValue(str5);
        textDetailSecureCell2.valueTextView.setTextColor(Theme.getColor(!z3 ? "windowBackgroundWhiteRedText3" : "windowBackgroundWhiteGrayText2"));
        textDetailSecureCell2.setChecked(z3 && this.currentActivityType != 8 && ((z && tLRPC$TL_secureRequiredType2 != null) || (!z && tLRPC$TL_secureValue != null)) && (tLRPC$TL_secureRequiredType2 == null || tLRPC$TL_secureValue2 != null));
    }

    public void checkNativeFields(boolean z) {
        EditTextBoldCursor[] editTextBoldCursorArr;
        if (this.inputExtraFields == null) {
            return;
        }
        String str = this.languageMap.get(this.currentResidence);
        String str2 = SharedConfig.getCountryLangs().get(this.currentResidence);
        int i = 0;
        if (!this.currentType.native_names || TextUtils.isEmpty(this.currentResidence) || "EN".equals(str2)) {
            if (this.nativeInfoCell.getVisibility() == 8) {
                return;
            }
            this.nativeInfoCell.setVisibility(8);
            this.headerCell.setVisibility(8);
            this.extraBackgroundView2.setVisibility(8);
            while (true) {
                EditTextBoldCursor[] editTextBoldCursorArr2 = this.inputExtraFields;
                if (i >= editTextBoldCursorArr2.length) {
                    break;
                }
                ((View) editTextBoldCursorArr2[i].getParent()).setVisibility(8);
                i++;
            }
            if (((this.currentBotId != 0 || this.currentDocumentsType == null) && this.currentTypeValue != null && !this.documentOnly) || this.currentDocumentsTypeValue != null) {
                this.sectionCell2.setBackgroundDrawable(Theme.getThemedDrawable(getParentActivity(), 2131165435, "windowBackgroundGrayShadow"));
                return;
            } else {
                this.sectionCell2.setBackgroundDrawable(Theme.getThemedDrawable(getParentActivity(), 2131165436, "windowBackgroundGrayShadow"));
                return;
            }
        }
        if (this.nativeInfoCell.getVisibility() != 0) {
            this.nativeInfoCell.setVisibility(0);
            this.headerCell.setVisibility(0);
            this.extraBackgroundView2.setVisibility(0);
            int i2 = 0;
            while (true) {
                editTextBoldCursorArr = this.inputExtraFields;
                if (i2 >= editTextBoldCursorArr.length) {
                    break;
                }
                ((View) editTextBoldCursorArr[i2].getParent()).setVisibility(0);
                i2++;
            }
            if (editTextBoldCursorArr[0].length() == 0 && this.inputExtraFields[1].length() == 0 && this.inputExtraFields[2].length() == 0) {
                int i3 = 0;
                while (true) {
                    boolean[] zArr = this.nonLatinNames;
                    if (i3 >= zArr.length) {
                        break;
                    } else if (zArr[i3]) {
                        this.inputExtraFields[0].setText(this.inputFields[0].getText());
                        this.inputExtraFields[1].setText(this.inputFields[1].getText());
                        this.inputExtraFields[2].setText(this.inputFields[2].getText());
                        break;
                    } else {
                        i3++;
                    }
                }
            }
            this.sectionCell2.setBackgroundDrawable(Theme.getThemedDrawable(getParentActivity(), 2131165435, "windowBackgroundGrayShadow"));
        }
        this.nativeInfoCell.setText(LocaleController.formatString("PassportNativeInfo", 2131627354, str));
        String serverString = str2 != null ? LocaleController.getServerString("PassportLanguage_" + str2) : null;
        if (serverString != null) {
            this.headerCell.setText(LocaleController.formatString("PassportNativeHeaderLang", 2131627353, serverString));
        } else {
            this.headerCell.setText(LocaleController.getString("PassportNativeHeader", 2131627352));
        }
        for (int i4 = 0; i4 < 3; i4++) {
            if (i4 != 0) {
                if (i4 != 1) {
                    if (i4 == 2) {
                        if (serverString != null) {
                            this.inputExtraFields[i4].setHintText(LocaleController.getString("PassportSurname", 2131627394));
                        } else {
                            this.inputExtraFields[i4].setHintText(LocaleController.formatString("PassportSurnameCountry", 2131627395, str));
                        }
                    }
                } else if (serverString != null) {
                    this.inputExtraFields[i4].setHintText(LocaleController.getString("PassportMidname", 2131627345));
                } else {
                    this.inputExtraFields[i4].setHintText(LocaleController.formatString("PassportMidnameCountry", 2131627346, str));
                }
            } else if (serverString != null) {
                this.inputExtraFields[i4].setHintText(LocaleController.getString("PassportName", 2131627348));
            } else {
                this.inputExtraFields[i4].setHintText(LocaleController.formatString("PassportNameCountry", 2131627350, str));
            }
        }
        if (!z) {
            return;
        }
        AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda52(this));
    }

    public /* synthetic */ void lambda$checkNativeFields$58() {
        EditTextBoldCursor[] editTextBoldCursorArr = this.inputExtraFields;
        if (editTextBoldCursorArr != null) {
            scrollToField(editTextBoldCursorArr[0]);
        }
    }

    private TLRPC$TL_secureValue getValueByType(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, boolean z) {
        String[] strArr;
        JSONObject jSONObject;
        if (tLRPC$TL_secureRequiredType == null) {
            return null;
        }
        int size = this.currentForm.values.size();
        for (int i = 0; i < size; i++) {
            TLRPC$TL_secureValue tLRPC$TL_secureValue = this.currentForm.values.get(i);
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
                    if ((tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress)) {
                        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                            strArr = tLRPC$TL_secureRequiredType.native_names ? new String[]{"first_name_native", "last_name_native", "birth_date", "gender", "country_code", "residence_country_code"} : new String[]{"first_name", "last_name", "birth_date", "gender", "country_code", "residence_country_code"};
                        } else {
                            strArr = new String[]{"street_line1", "street_line2", "post_code", "city", "state", "country_code"};
                        }
                        try {
                            TLRPC$TL_secureData tLRPC$TL_secureData = tLRPC$TL_secureValue.data;
                            jSONObject = new JSONObject(decryptData(tLRPC$TL_secureData.data, decryptValueSecret(tLRPC$TL_secureData.secret, tLRPC$TL_secureData.data_hash), tLRPC$TL_secureValue.data.data_hash));
                        } catch (Throwable unused) {
                        }
                        for (int i2 = 0; i2 < strArr.length; i2++) {
                            if (jSONObject.has(strArr[i2]) && !TextUtils.isEmpty(jSONObject.getString(strArr[i2]))) {
                            }
                            return null;
                        }
                    }
                }
                return tLRPC$TL_secureValue;
            }
        }
        return null;
    }

    private void openTypeActivity(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, ArrayList<TLRPC$TL_secureRequiredType> arrayList, boolean z) {
        int i;
        TLRPC$TL_account_password tLRPC$TL_account_password;
        HashMap<String, String> hashMap;
        int size = arrayList != null ? arrayList.size() : 0;
        TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
        TLRPC$SecureValueType tLRPC$SecureValueType2 = tLRPC$TL_secureRequiredType2 != null ? tLRPC$TL_secureRequiredType2.type : null;
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
            i = 1;
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
            i = 2;
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone) {
            i = 3;
        } else {
            i = tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail ? 4 : -1;
        }
        if (i != -1) {
            HashMap<String, String> hashMap2 = !z ? this.errorsMap.get(getNameForType(tLRPC$SecureValueType)) : null;
            HashMap<String, String> hashMap3 = this.errorsMap.get(getNameForType(tLRPC$SecureValueType2));
            TLRPC$TL_secureValue valueByType = getValueByType(tLRPC$TL_secureRequiredType, false);
            TLRPC$TL_secureValue valueByType2 = getValueByType(tLRPC$TL_secureRequiredType2, false);
            TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm = this.currentForm;
            TLRPC$TL_account_password tLRPC$TL_account_password2 = this.currentPassword;
            HashMap<String, String> hashMap4 = this.typesValues.get(tLRPC$TL_secureRequiredType);
            if (tLRPC$TL_secureRequiredType2 != null) {
                tLRPC$TL_account_password = tLRPC$TL_account_password2;
                hashMap = this.typesValues.get(tLRPC$TL_secureRequiredType2);
            } else {
                tLRPC$TL_account_password = tLRPC$TL_account_password2;
                hashMap = null;
            }
            int i2 = i;
            PassportActivity passportActivity = new PassportActivity(i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_password, tLRPC$TL_secureRequiredType, valueByType, tLRPC$TL_secureRequiredType2, valueByType2, hashMap4, hashMap);
            passportActivity.delegate = new AnonymousClass20(tLRPC$SecureValueType, z, size);
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

    /* renamed from: org.telegram.ui.PassportActivity$20 */
    /* loaded from: classes3.dex */
    public class AnonymousClass20 implements PassportActivityDelegate {
        final /* synthetic */ int val$availableDocumentTypesCount;
        final /* synthetic */ boolean val$documentOnly;
        final /* synthetic */ TLRPC$SecureValueType val$type;

        AnonymousClass20(TLRPC$SecureValueType tLRPC$SecureValueType, boolean z, int i) {
            PassportActivity.this = r1;
            this.val$type = tLRPC$SecureValueType;
            this.val$documentOnly = z;
            this.val$availableDocumentTypesCount = i;
        }

        private TLRPC$InputSecureFile getInputSecureFile(SecureDocument secureDocument) {
            if (secureDocument.inputFile != null) {
                TLRPC$TL_inputSecureFileUploaded tLRPC$TL_inputSecureFileUploaded = new TLRPC$TL_inputSecureFileUploaded();
                TLRPC$TL_inputFile tLRPC$TL_inputFile = secureDocument.inputFile;
                tLRPC$TL_inputSecureFileUploaded.id = tLRPC$TL_inputFile.id;
                tLRPC$TL_inputSecureFileUploaded.parts = tLRPC$TL_inputFile.parts;
                tLRPC$TL_inputSecureFileUploaded.md5_checksum = tLRPC$TL_inputFile.md5_checksum;
                tLRPC$TL_inputSecureFileUploaded.file_hash = secureDocument.fileHash;
                tLRPC$TL_inputSecureFileUploaded.secret = secureDocument.fileSecret;
                return tLRPC$TL_inputSecureFileUploaded;
            }
            TLRPC$TL_inputSecureFile tLRPC$TL_inputSecureFile = new TLRPC$TL_inputSecureFile();
            TLRPC$TL_secureFile tLRPC$TL_secureFile = secureDocument.secureFile;
            tLRPC$TL_inputSecureFile.id = tLRPC$TL_secureFile.id;
            tLRPC$TL_inputSecureFile.access_hash = tLRPC$TL_secureFile.access_hash;
            return tLRPC$TL_inputSecureFile;
        }

        public void renameFile(SecureDocument secureDocument, TLRPC$TL_secureFile tLRPC$TL_secureFile) {
            File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(secureDocument);
            File pathToAttach2 = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$TL_secureFile);
            pathToAttach.renameTo(pathToAttach2);
            ImageLoader.getInstance().replaceImageInCache(secureDocument.secureFile.dc_id + "_" + secureDocument.secureFile.id, tLRPC$TL_secureFile.dc_id + "_" + tLRPC$TL_secureFile.id, null, false);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.telegram.ui.PassportActivity.PassportActivityDelegate
        public void saveValue(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, String str, String str2, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, String str3, ArrayList<SecureDocument> arrayList, SecureDocument secureDocument, ArrayList<SecureDocument> arrayList2, SecureDocument secureDocument2, SecureDocument secureDocument3, Runnable runnable, ErrorRunnable errorRunnable) {
            TLRPC$TL_inputSecureValue tLRPC$TL_inputSecureValue;
            TLRPC$TL_inputSecureValue tLRPC$TL_inputSecureValue2;
            TLRPC$TL_securePlainPhone tLRPC$TL_securePlainPhone;
            if (!TextUtils.isEmpty(str2)) {
                tLRPC$TL_inputSecureValue = new TLRPC$TL_inputSecureValue();
                tLRPC$TL_inputSecureValue.type = tLRPC$TL_secureRequiredType.type;
                tLRPC$TL_inputSecureValue.flags |= 1;
                EncryptionResult encryptData = PassportActivity.this.encryptData(AndroidUtilities.getStringBytes(str2));
                TLRPC$TL_secureData tLRPC$TL_secureData = new TLRPC$TL_secureData();
                tLRPC$TL_inputSecureValue.data = tLRPC$TL_secureData;
                tLRPC$TL_secureData.data = encryptData.encryptedData;
                tLRPC$TL_secureData.data_hash = encryptData.fileHash;
                tLRPC$TL_secureData.secret = encryptData.fileSecret;
            } else if (!TextUtils.isEmpty(str)) {
                TLRPC$SecureValueType tLRPC$SecureValueType = this.val$type;
                if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail) {
                    TLRPC$TL_securePlainEmail tLRPC$TL_securePlainEmail = new TLRPC$TL_securePlainEmail();
                    tLRPC$TL_securePlainEmail.email = str;
                    tLRPC$TL_securePlainPhone = tLRPC$TL_securePlainEmail;
                } else if (!(tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone)) {
                    return;
                } else {
                    TLRPC$TL_securePlainPhone tLRPC$TL_securePlainPhone2 = new TLRPC$TL_securePlainPhone();
                    tLRPC$TL_securePlainPhone2.phone = str;
                    tLRPC$TL_securePlainPhone = tLRPC$TL_securePlainPhone2;
                }
                TLRPC$TL_inputSecureValue tLRPC$TL_inputSecureValue3 = new TLRPC$TL_inputSecureValue();
                tLRPC$TL_inputSecureValue3.type = tLRPC$TL_secureRequiredType.type;
                tLRPC$TL_inputSecureValue3.flags |= 32;
                tLRPC$TL_inputSecureValue3.plain_data = tLRPC$TL_securePlainPhone;
                tLRPC$TL_inputSecureValue = tLRPC$TL_inputSecureValue3;
            } else {
                tLRPC$TL_inputSecureValue = null;
            }
            if (!this.val$documentOnly && tLRPC$TL_inputSecureValue == null) {
                if (errorRunnable == null) {
                    return;
                }
                errorRunnable.onError(null, null);
                return;
            }
            if (tLRPC$TL_secureRequiredType2 != null) {
                TLRPC$TL_inputSecureValue tLRPC$TL_inputSecureValue4 = new TLRPC$TL_inputSecureValue();
                tLRPC$TL_inputSecureValue4.type = tLRPC$TL_secureRequiredType2.type;
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
                if (arrayList2 != null && !arrayList2.isEmpty()) {
                    tLRPC$TL_inputSecureValue4.flags |= 64;
                    int size = arrayList2.size();
                    for (int i = 0; i < size; i++) {
                        tLRPC$TL_inputSecureValue4.translation.add(getInputSecureFile(arrayList2.get(i)));
                    }
                }
                if (arrayList != null && !arrayList.isEmpty()) {
                    tLRPC$TL_inputSecureValue4.flags |= 16;
                    int size2 = arrayList.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        tLRPC$TL_inputSecureValue4.files.add(getInputSecureFile(arrayList.get(i2)));
                    }
                }
                if (!this.val$documentOnly) {
                    tLRPC$TL_inputSecureValue2 = tLRPC$TL_inputSecureValue4;
                    TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue = new TLRPC$TL_account_saveSecureValue();
                    tLRPC$TL_account_saveSecureValue.value = tLRPC$TL_inputSecureValue;
                    tLRPC$TL_account_saveSecureValue.secure_secret_id = PassportActivity.this.secureSecretId;
                    ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_saveSecureValue, new AnonymousClass1(errorRunnable, str, tLRPC$TL_account_saveSecureValue, tLRPC$TL_secureRequiredType2, tLRPC$TL_secureRequiredType, arrayList, secureDocument, secureDocument2, secureDocument3, arrayList2, str2, str3, runnable, this, tLRPC$TL_inputSecureValue2));
                }
                tLRPC$TL_inputSecureValue = tLRPC$TL_inputSecureValue4;
            }
            tLRPC$TL_inputSecureValue2 = null;
            TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue2 = new TLRPC$TL_account_saveSecureValue();
            tLRPC$TL_account_saveSecureValue2.value = tLRPC$TL_inputSecureValue;
            tLRPC$TL_account_saveSecureValue2.secure_secret_id = PassportActivity.this.secureSecretId;
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_saveSecureValue2, new AnonymousClass1(errorRunnable, str, tLRPC$TL_account_saveSecureValue2, tLRPC$TL_secureRequiredType2, tLRPC$TL_secureRequiredType, arrayList, secureDocument, secureDocument2, secureDocument3, arrayList2, str2, str3, runnable, this, tLRPC$TL_inputSecureValue2));
        }

        /* renamed from: org.telegram.ui.PassportActivity$20$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 implements RequestDelegate {
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

            AnonymousClass1(ErrorRunnable errorRunnable, String str, TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, ArrayList arrayList, SecureDocument secureDocument, SecureDocument secureDocument2, SecureDocument secureDocument3, ArrayList arrayList2, String str2, String str3, Runnable runnable, PassportActivityDelegate passportActivityDelegate, TLRPC$TL_inputSecureValue tLRPC$TL_inputSecureValue) {
                AnonymousClass20.this = r3;
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

            /* renamed from: onResult */
            public void lambda$run$4(TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_secureValue tLRPC$TL_secureValue, TLRPC$TL_secureValue tLRPC$TL_secureValue2) {
                ErrorRunnable errorRunnable = this.val$errorRunnable;
                String str = this.val$text;
                TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue = this.val$req;
                AnonymousClass20 anonymousClass20 = AnonymousClass20.this;
                AndroidUtilities.runOnUIThread(new PassportActivity$20$1$$ExternalSyntheticLambda1(this, tLRPC$TL_error, errorRunnable, str, tLRPC$TL_account_saveSecureValue, anonymousClass20.val$documentOnly, this.val$documentRequiredType, this.val$requiredType, tLRPC$TL_secureValue, tLRPC$TL_secureValue2, this.val$documents, this.val$selfie, this.val$front, this.val$reverse, this.val$translationDocuments, this.val$json, this.val$documentsJson, anonymousClass20.val$availableDocumentTypesCount, this.val$finishRunnable));
            }

            public /* synthetic */ void lambda$onResult$0(TLRPC$TL_error tLRPC$TL_error, ErrorRunnable errorRunnable, String str, TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue, boolean z, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, TLRPC$TL_secureValue tLRPC$TL_secureValue, TLRPC$TL_secureValue tLRPC$TL_secureValue2, ArrayList arrayList, SecureDocument secureDocument, SecureDocument secureDocument2, SecureDocument secureDocument3, ArrayList arrayList2, String str2, String str3, int i, Runnable runnable) {
                int i2;
                if (tLRPC$TL_error != null) {
                    if (errorRunnable != null) {
                        errorRunnable.onError(tLRPC$TL_error.text, str);
                    }
                    AlertsCreator.processError(((BaseFragment) PassportActivity.this).currentAccount, tLRPC$TL_error, PassportActivity.this, tLRPC$TL_account_saveSecureValue, str);
                    return;
                }
                if (!z) {
                    PassportActivity.this.removeValue(tLRPC$TL_secureRequiredType2);
                    PassportActivity.this.removeValue(tLRPC$TL_secureRequiredType);
                } else if (tLRPC$TL_secureRequiredType != null) {
                    PassportActivity.this.removeValue(tLRPC$TL_secureRequiredType);
                } else {
                    PassportActivity.this.removeValue(tLRPC$TL_secureRequiredType2);
                }
                if (tLRPC$TL_secureValue != null) {
                    PassportActivity.this.currentForm.values.add(tLRPC$TL_secureValue);
                }
                if (tLRPC$TL_secureValue2 != null) {
                    PassportActivity.this.currentForm.values.add(tLRPC$TL_secureValue2);
                }
                if (arrayList != null && !arrayList.isEmpty()) {
                    int size = arrayList.size();
                    int i3 = 0;
                    while (i3 < size) {
                        SecureDocument secureDocument4 = (SecureDocument) arrayList.get(i3);
                        if (secureDocument4.inputFile != null) {
                            int size2 = tLRPC$TL_secureValue.files.size();
                            int i4 = 0;
                            while (i4 < size2) {
                                TLRPC$SecureFile tLRPC$SecureFile = tLRPC$TL_secureValue.files.get(i4);
                                i2 = size;
                                if (tLRPC$SecureFile instanceof TLRPC$TL_secureFile) {
                                    TLRPC$TL_secureFile tLRPC$TL_secureFile = (TLRPC$TL_secureFile) tLRPC$SecureFile;
                                    if (Utilities.arraysEquals(secureDocument4.fileSecret, 0, tLRPC$TL_secureFile.secret, 0)) {
                                        AnonymousClass20.this.renameFile(secureDocument4, tLRPC$TL_secureFile);
                                        break;
                                    }
                                }
                                i4++;
                                size = i2;
                            }
                        }
                        i2 = size;
                        i3++;
                        size = i2;
                    }
                }
                if (secureDocument != null && secureDocument.inputFile != null) {
                    TLRPC$SecureFile tLRPC$SecureFile2 = tLRPC$TL_secureValue.selfie;
                    if (tLRPC$SecureFile2 instanceof TLRPC$TL_secureFile) {
                        TLRPC$TL_secureFile tLRPC$TL_secureFile2 = (TLRPC$TL_secureFile) tLRPC$SecureFile2;
                        if (Utilities.arraysEquals(secureDocument.fileSecret, 0, tLRPC$TL_secureFile2.secret, 0)) {
                            AnonymousClass20.this.renameFile(secureDocument, tLRPC$TL_secureFile2);
                        }
                    }
                }
                if (secureDocument2 != null && secureDocument2.inputFile != null) {
                    TLRPC$SecureFile tLRPC$SecureFile3 = tLRPC$TL_secureValue.front_side;
                    if (tLRPC$SecureFile3 instanceof TLRPC$TL_secureFile) {
                        TLRPC$TL_secureFile tLRPC$TL_secureFile3 = (TLRPC$TL_secureFile) tLRPC$SecureFile3;
                        if (Utilities.arraysEquals(secureDocument2.fileSecret, 0, tLRPC$TL_secureFile3.secret, 0)) {
                            AnonymousClass20.this.renameFile(secureDocument2, tLRPC$TL_secureFile3);
                        }
                    }
                }
                if (secureDocument3 != null && secureDocument3.inputFile != null) {
                    TLRPC$SecureFile tLRPC$SecureFile4 = tLRPC$TL_secureValue.reverse_side;
                    if (tLRPC$SecureFile4 instanceof TLRPC$TL_secureFile) {
                        TLRPC$TL_secureFile tLRPC$TL_secureFile4 = (TLRPC$TL_secureFile) tLRPC$SecureFile4;
                        if (Utilities.arraysEquals(secureDocument3.fileSecret, 0, tLRPC$TL_secureFile4.secret, 0)) {
                            AnonymousClass20.this.renameFile(secureDocument3, tLRPC$TL_secureFile4);
                        }
                    }
                }
                if (arrayList2 != null && !arrayList2.isEmpty()) {
                    int size3 = arrayList2.size();
                    for (int i5 = 0; i5 < size3; i5++) {
                        SecureDocument secureDocument5 = (SecureDocument) arrayList2.get(i5);
                        if (secureDocument5.inputFile != null) {
                            int size4 = tLRPC$TL_secureValue.translation.size();
                            for (int i6 = 0; i6 < size4; i6++) {
                                TLRPC$SecureFile tLRPC$SecureFile5 = tLRPC$TL_secureValue.translation.get(i6);
                                if (tLRPC$SecureFile5 instanceof TLRPC$TL_secureFile) {
                                    TLRPC$TL_secureFile tLRPC$TL_secureFile5 = (TLRPC$TL_secureFile) tLRPC$SecureFile5;
                                    if (Utilities.arraysEquals(secureDocument5.fileSecret, 0, tLRPC$TL_secureFile5.secret, 0)) {
                                        AnonymousClass20.this.renameFile(secureDocument5, tLRPC$TL_secureFile5);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                PassportActivity.this.setTypeValue(tLRPC$TL_secureRequiredType2, str, str2, tLRPC$TL_secureRequiredType, str3, z, i);
                if (runnable == null) {
                    return;
                }
                runnable.run();
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                if (tLRPC$TL_error != null) {
                    if (tLRPC$TL_error.text.equals("EMAIL_VERIFICATION_NEEDED")) {
                        TLRPC$TL_account_sendVerifyEmailCode tLRPC$TL_account_sendVerifyEmailCode = new TLRPC$TL_account_sendVerifyEmailCode();
                        tLRPC$TL_account_sendVerifyEmailCode.email = this.val$text;
                        ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_sendVerifyEmailCode, new PassportActivity$20$1$$ExternalSyntheticLambda3(this, this.val$text, this.val$requiredType, this.val$currentDelegate, this.val$errorRunnable));
                        return;
                    } else if (tLRPC$TL_error.text.equals("PHONE_VERIFICATION_NEEDED")) {
                        AndroidUtilities.runOnUIThread(new PassportActivity$20$1$$ExternalSyntheticLambda2(this.val$errorRunnable, tLRPC$TL_error, this.val$text));
                        return;
                    }
                }
                if (tLRPC$TL_error == null && this.val$finalFileInputSecureValue != null) {
                    TLRPC$TL_account_saveSecureValue tLRPC$TL_account_saveSecureValue = new TLRPC$TL_account_saveSecureValue();
                    tLRPC$TL_account_saveSecureValue.value = this.val$finalFileInputSecureValue;
                    tLRPC$TL_account_saveSecureValue.secure_secret_id = PassportActivity.this.secureSecretId;
                    ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_saveSecureValue, new PassportActivity$20$1$$ExternalSyntheticLambda4(this, (TLRPC$TL_secureValue) tLObject));
                    return;
                }
                lambda$run$4(tLRPC$TL_error, (TLRPC$TL_secureValue) tLObject, null);
            }

            public /* synthetic */ void lambda$run$2(String str, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, PassportActivityDelegate passportActivityDelegate, ErrorRunnable errorRunnable, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                AndroidUtilities.runOnUIThread(new PassportActivity$20$1$$ExternalSyntheticLambda0(this, tLObject, str, tLRPC$TL_secureRequiredType, passportActivityDelegate, tLRPC$TL_error, errorRunnable));
            }

            public /* synthetic */ void lambda$run$1(TLObject tLObject, String str, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, PassportActivityDelegate passportActivityDelegate, TLRPC$TL_error tLRPC$TL_error, ErrorRunnable errorRunnable) {
                if (tLObject == null) {
                    PassportActivity.this.showAlertWithText(LocaleController.getString("PassportEmail", 2131627277), tLRPC$TL_error.text);
                    if (errorRunnable == null) {
                        return;
                    }
                    errorRunnable.onError(tLRPC$TL_error.text, str);
                    return;
                }
                TLRPC$TL_account_sentEmailCode tLRPC$TL_account_sentEmailCode = (TLRPC$TL_account_sentEmailCode) tLObject;
                HashMap hashMap = new HashMap();
                hashMap.put("email", str);
                hashMap.put("pattern", tLRPC$TL_account_sentEmailCode.email_pattern);
                PassportActivity passportActivity = new PassportActivity(6, PassportActivity.this.currentForm, PassportActivity.this.currentPassword, tLRPC$TL_secureRequiredType, (TLRPC$TL_secureValue) null, (TLRPC$TL_secureRequiredType) null, (TLRPC$TL_secureValue) null, hashMap, (HashMap<String, String>) null);
                ((BaseFragment) passportActivity).currentAccount = ((BaseFragment) PassportActivity.this).currentAccount;
                passportActivity.emailCodeLength = tLRPC$TL_account_sentEmailCode.length;
                passportActivity.saltedPassword = PassportActivity.this.saltedPassword;
                passportActivity.secureSecret = PassportActivity.this.secureSecret;
                passportActivity.delegate = passportActivityDelegate;
                PassportActivity.this.presentFragment(passportActivity, true);
            }

            public static /* synthetic */ void lambda$run$3(ErrorRunnable errorRunnable, TLRPC$TL_error tLRPC$TL_error, String str) {
                errorRunnable.onError(tLRPC$TL_error.text, str);
            }
        }

        @Override // org.telegram.ui.PassportActivity.PassportActivityDelegate
        public SecureDocument saveFile(TLRPC$TL_secureFile tLRPC$TL_secureFile) {
            String str = FileLoader.getDirectory(4) + "/" + tLRPC$TL_secureFile.dc_id + "_" + tLRPC$TL_secureFile.id + ".jpg";
            EncryptionResult createSecureDocument = PassportActivity.this.createSecureDocument(str);
            return new SecureDocument(createSecureDocument.secureDocumentKey, tLRPC$TL_secureFile, str, createSecureDocument.fileHash, createSecureDocument.fileSecret);
        }

        @Override // org.telegram.ui.PassportActivity.PassportActivityDelegate
        public void deleteValue(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, ArrayList<TLRPC$TL_secureRequiredType> arrayList, boolean z, Runnable runnable, ErrorRunnable errorRunnable) {
            PassportActivity.this.deleteValueInternal(tLRPC$TL_secureRequiredType, tLRPC$TL_secureRequiredType2, arrayList, z, runnable, errorRunnable, this.val$documentOnly);
        }
    }

    public TLRPC$TL_secureValue removeValue(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType) {
        if (tLRPC$TL_secureRequiredType == null) {
            return null;
        }
        int size = this.currentForm.values.size();
        for (int i = 0; i < size; i++) {
            if (tLRPC$TL_secureRequiredType.type.getClass() == this.currentForm.values.get(i).type.getClass()) {
                return this.currentForm.values.remove(i);
            }
        }
        return null;
    }

    public void deleteValueInternal(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, ArrayList<TLRPC$TL_secureRequiredType> arrayList, boolean z, Runnable runnable, ErrorRunnable errorRunnable, boolean z2) {
        if (tLRPC$TL_secureRequiredType == null) {
            return;
        }
        TLRPC$TL_account_deleteSecureValue tLRPC$TL_account_deleteSecureValue = new TLRPC$TL_account_deleteSecureValue();
        if (!z2 || tLRPC$TL_secureRequiredType2 == null) {
            if (z) {
                tLRPC$TL_account_deleteSecureValue.types.add(tLRPC$TL_secureRequiredType.type);
            }
            if (tLRPC$TL_secureRequiredType2 != null) {
                tLRPC$TL_account_deleteSecureValue.types.add(tLRPC$TL_secureRequiredType2.type);
            }
        } else {
            tLRPC$TL_account_deleteSecureValue.types.add(tLRPC$TL_secureRequiredType2.type);
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_deleteSecureValue, new PassportActivity$$ExternalSyntheticLambda67(this, errorRunnable, z2, tLRPC$TL_secureRequiredType2, tLRPC$TL_secureRequiredType, z, arrayList, runnable));
    }

    public /* synthetic */ void lambda$deleteValueInternal$60(ErrorRunnable errorRunnable, boolean z, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, boolean z2, ArrayList arrayList, Runnable runnable, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda60(this, tLRPC$TL_error, errorRunnable, z, tLRPC$TL_secureRequiredType, tLRPC$TL_secureRequiredType2, z2, arrayList, runnable));
    }

    public /* synthetic */ void lambda$deleteValueInternal$59(TLRPC$TL_error tLRPC$TL_error, ErrorRunnable errorRunnable, boolean z, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2, boolean z2, ArrayList arrayList, Runnable runnable) {
        String str;
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3;
        TLRPC$TL_secureData tLRPC$TL_secureData;
        String str2;
        LinearLayout linearLayout;
        String str3 = null;
        if (tLRPC$TL_error != null) {
            if (errorRunnable != null) {
                errorRunnable.onError(tLRPC$TL_error.text, null);
            }
            showAlertWithText(LocaleController.getString("AppName", 2131624384), tLRPC$TL_error.text);
            return;
        }
        if (!z) {
            if (z2) {
                removeValue(tLRPC$TL_secureRequiredType2);
            }
            removeValue(tLRPC$TL_secureRequiredType);
        } else if (tLRPC$TL_secureRequiredType != null) {
            removeValue(tLRPC$TL_secureRequiredType);
        } else {
            removeValue(tLRPC$TL_secureRequiredType2);
        }
        if (this.currentActivityType == 8) {
            TextDetailSecureCell remove = this.typesViews.remove(tLRPC$TL_secureRequiredType2);
            if (remove != null) {
                this.linearLayout2.removeView(remove);
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
                int size = arrayList.size();
                int i = 0;
                while (true) {
                    if (i >= size) {
                        tLRPC$TL_secureRequiredType3 = tLRPC$TL_secureRequiredType;
                        break;
                    }
                    tLRPC$TL_secureRequiredType3 = (TLRPC$TL_secureRequiredType) arrayList.get(i);
                    TLRPC$TL_secureValue valueByType = getValueByType(tLRPC$TL_secureRequiredType3, false);
                    if (valueByType != null) {
                        TLRPC$TL_secureData tLRPC$TL_secureData2 = valueByType.data;
                        if (tLRPC$TL_secureData2 != null) {
                            str2 = decryptData(tLRPC$TL_secureData2.data, decryptValueSecret(tLRPC$TL_secureData2.secret, tLRPC$TL_secureData2.data_hash), valueByType.data.data_hash);
                        }
                    } else {
                        i++;
                    }
                }
                str2 = null;
                if (tLRPC$TL_secureRequiredType3 == null) {
                    str = str2;
                    tLRPC$TL_secureRequiredType3 = (TLRPC$TL_secureRequiredType) arrayList.get(0);
                } else {
                    str = str2;
                }
            }
            if (z2) {
                setTypeValue(tLRPC$TL_secureRequiredType2, null, null, tLRPC$TL_secureRequiredType3, str, z, arrayList != null ? arrayList.size() : 0);
            } else {
                TLRPC$TL_secureValue valueByType2 = getValueByType(tLRPC$TL_secureRequiredType2, false);
                if (valueByType2 != null && (tLRPC$TL_secureData = valueByType2.data) != null) {
                    str3 = decryptData(tLRPC$TL_secureData.data, decryptValueSecret(tLRPC$TL_secureData.secret, tLRPC$TL_secureData.data_hash), valueByType2.data.data_hash);
                }
                setTypeValue(tLRPC$TL_secureRequiredType2, null, str3, tLRPC$TL_secureRequiredType3, str, z, arrayList != null ? arrayList.size() : 0);
            }
        }
        if (runnable == null) {
            return;
        }
        runnable.run();
    }

    /* JADX WARN: Removed duplicated region for block: B:62:0x018e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TextDetailSecureCell addField(Context context, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, ArrayList<TLRPC$TL_secureRequiredType> arrayList, boolean z, boolean z2) {
        String str;
        String str2;
        String str3;
        TLRPC$TL_secureValue valueByType;
        LinearLayout linearLayout;
        String str4;
        String str5;
        int size = arrayList != null ? arrayList.size() : 0;
        TextDetailSecureCell textDetailSecureCell = new TextDetailSecureCell(this, context);
        textDetailSecureCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
            if (arrayList == null || arrayList.isEmpty()) {
                str5 = LocaleController.getString("PassportPersonalDetails", 2131627361);
            } else if (z && arrayList.size() == 1) {
                str5 = getTextForType(arrayList.get(0).type);
            } else {
                str5 = (!z || arrayList.size() != 2) ? LocaleController.getString("PassportIdentityDocument", 2131627287) : LocaleController.formatString("PassportTwoDocuments", 2131627398, getTextForType(arrayList.get(0).type), getTextForType(arrayList.get(1).type));
            }
            textDetailSecureCell.setTextAndValue(str5, "", !z2);
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
            if (arrayList == null || arrayList.isEmpty()) {
                str4 = LocaleController.getString("PassportAddress", 2131627249);
            } else if (z && arrayList.size() == 1) {
                str4 = getTextForType(arrayList.get(0).type);
            } else {
                str4 = (!z || arrayList.size() != 2) ? LocaleController.getString("PassportResidentialAddress", 2131627379) : LocaleController.formatString("PassportTwoDocuments", 2131627398, getTextForType(arrayList.get(0).type), getTextForType(arrayList.get(1).type));
            }
            textDetailSecureCell.setTextAndValue(str4, "", !z2);
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone) {
            textDetailSecureCell.setTextAndValue(LocaleController.getString("PassportPhone", 2131627364), "", !z2);
        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail) {
            textDetailSecureCell.setTextAndValue(LocaleController.getString("PassportEmail", 2131627277), "", !z2);
        }
        if (this.currentActivityType == 8) {
            this.linearLayout2.addView(textDetailSecureCell, linearLayout.getChildCount() - 5, LayoutHelper.createLinear(-1, -2));
        } else {
            this.linearLayout2.addView(textDetailSecureCell, LayoutHelper.createLinear(-1, -2));
        }
        textDetailSecureCell.setOnClickListener(new PassportActivity$$ExternalSyntheticLambda30(this, arrayList, tLRPC$TL_secureRequiredType, z));
        this.typesViews.put(tLRPC$TL_secureRequiredType, textDetailSecureCell);
        this.typesValues.put(tLRPC$TL_secureRequiredType, new HashMap<>());
        TLRPC$TL_secureValue valueByType2 = getValueByType(tLRPC$TL_secureRequiredType, false);
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2 = null;
        if (valueByType2 != null) {
            TLRPC$SecurePlainData tLRPC$SecurePlainData = valueByType2.plain_data;
            if (tLRPC$SecurePlainData instanceof TLRPC$TL_securePlainEmail) {
                str2 = ((TLRPC$TL_securePlainEmail) tLRPC$SecurePlainData).email;
            } else if (tLRPC$SecurePlainData instanceof TLRPC$TL_securePlainPhone) {
                str2 = ((TLRPC$TL_securePlainPhone) tLRPC$SecurePlainData).phone;
            } else {
                TLRPC$TL_secureData tLRPC$TL_secureData = valueByType2.data;
                if (tLRPC$TL_secureData != null) {
                    str = decryptData(tLRPC$TL_secureData.data, decryptValueSecret(tLRPC$TL_secureData.secret, tLRPC$TL_secureData.data_hash), valueByType2.data.data_hash);
                    str2 = null;
                    if (arrayList != null || arrayList.isEmpty()) {
                        str3 = null;
                    } else {
                        int size2 = arrayList.size();
                        str3 = null;
                        boolean z3 = false;
                        for (int i = 0; i < size2; i++) {
                            TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3 = arrayList.get(i);
                            this.typesValues.put(tLRPC$TL_secureRequiredType3, new HashMap<>());
                            this.documentsToTypesLink.put(tLRPC$TL_secureRequiredType3, tLRPC$TL_secureRequiredType);
                            if (!z3 && (valueByType = getValueByType(tLRPC$TL_secureRequiredType3, false)) != null) {
                                TLRPC$TL_secureData tLRPC$TL_secureData2 = valueByType.data;
                                if (tLRPC$TL_secureData2 != null) {
                                    str3 = decryptData(tLRPC$TL_secureData2.data, decryptValueSecret(tLRPC$TL_secureData2.secret, tLRPC$TL_secureData2.data_hash), valueByType.data.data_hash);
                                }
                                tLRPC$TL_secureRequiredType2 = tLRPC$TL_secureRequiredType3;
                                z3 = true;
                            }
                        }
                        if (tLRPC$TL_secureRequiredType2 == null) {
                            tLRPC$TL_secureRequiredType2 = arrayList.get(0);
                        }
                    }
                    setTypeValue(tLRPC$TL_secureRequiredType, str2, str, tLRPC$TL_secureRequiredType2, str3, z, size);
                    return textDetailSecureCell;
                }
            }
            str = null;
            if (arrayList != null) {
            }
            str3 = null;
            setTypeValue(tLRPC$TL_secureRequiredType, str2, str, tLRPC$TL_secureRequiredType2, str3, z, size);
            return textDetailSecureCell;
        }
        str2 = null;
        str = null;
        if (arrayList != null) {
        }
        str3 = null;
        setTypeValue(tLRPC$TL_secureRequiredType, str2, str, tLRPC$TL_secureRequiredType2, str3, z, size);
        return textDetailSecureCell;
    }

    public /* synthetic */ void lambda$addField$64(ArrayList arrayList, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, boolean z, View view) {
        TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2;
        String str;
        int i;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                tLRPC$TL_secureRequiredType2 = (TLRPC$TL_secureRequiredType) arrayList.get(i2);
                if (getValueByType(tLRPC$TL_secureRequiredType2, false) != null || size == 1) {
                    break;
                }
            }
        }
        tLRPC$TL_secureRequiredType2 = null;
        TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_secureRequiredType.type;
        if ((tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress)) {
            if (tLRPC$TL_secureRequiredType2 == null && arrayList != null && !arrayList.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setPositiveButton(LocaleController.getString("Cancel", 2131624832), null);
                TLRPC$SecureValueType tLRPC$SecureValueType2 = tLRPC$TL_secureRequiredType.type;
                if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                    builder.setTitle(LocaleController.getString("PassportIdentityDocument", 2131627287));
                } else if (tLRPC$SecureValueType2 instanceof TLRPC$TL_secureValueTypeAddress) {
                    builder.setTitle(LocaleController.getString("PassportAddress", 2131627249));
                }
                ArrayList arrayList2 = new ArrayList();
                int size2 = arrayList.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    TLRPC$SecureValueType tLRPC$SecureValueType3 = ((TLRPC$TL_secureRequiredType) arrayList.get(i3)).type;
                    if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                        arrayList2.add(LocaleController.getString("PassportAddLicence", 2131627236));
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypePassport) {
                        arrayList2.add(LocaleController.getString("PassportAddPassport", 2131627237));
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                        arrayList2.add(LocaleController.getString("PassportAddInternalPassport", 2131627234));
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                        arrayList2.add(LocaleController.getString("PassportAddCard", 2131627231));
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                        arrayList2.add(LocaleController.getString("PassportAddBill", 2131627229));
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeBankStatement) {
                        arrayList2.add(LocaleController.getString("PassportAddBank", 2131627227));
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                        arrayList2.add(LocaleController.getString("PassportAddAgreement", 2131627225));
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                        arrayList2.add(LocaleController.getString("PassportAddTemporaryRegistration", 2131627241));
                    } else if (tLRPC$SecureValueType3 instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                        arrayList2.add(LocaleController.getString("PassportAddPassportRegistration", 2131627239));
                    }
                }
                builder.setItems((CharSequence[]) arrayList2.toArray(new CharSequence[0]), new PassportActivity$$ExternalSyntheticLambda8(this, tLRPC$TL_secureRequiredType, arrayList, z));
                showDialog(builder.create());
                return;
            }
        } else {
            boolean z2 = tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone;
            if ((z2 || (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail)) && getValueByType(tLRPC$TL_secureRequiredType, false) != null) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                builder2.setPositiveButton(LocaleController.getString("OK", 2131627127), new PassportActivity$$ExternalSyntheticLambda9(this, tLRPC$TL_secureRequiredType, z));
                builder2.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
                builder2.setTitle(LocaleController.getString("AppName", 2131624384));
                if (z2) {
                    i = 2131627267;
                    str = "PassportDeletePhoneAlert";
                } else {
                    i = 2131627264;
                    str = "PassportDeleteEmailAlert";
                }
                builder2.setMessage(LocaleController.getString(str, i));
                showDialog(builder2.create());
                return;
            }
        }
        openTypeActivity(tLRPC$TL_secureRequiredType, tLRPC$TL_secureRequiredType2, arrayList, z);
    }

    public /* synthetic */ void lambda$addField$61(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, ArrayList arrayList, boolean z, DialogInterface dialogInterface, int i) {
        openTypeActivity(tLRPC$TL_secureRequiredType, (TLRPC$TL_secureRequiredType) arrayList.get(i), arrayList, z);
    }

    public /* synthetic */ void lambda$addField$63(TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, boolean z, DialogInterface dialogInterface, int i) {
        needShowProgress();
        deleteValueInternal(tLRPC$TL_secureRequiredType, null, null, true, new PassportActivity$$ExternalSyntheticLambda47(this), new PassportActivity$$ExternalSyntheticLambda73(this), z);
    }

    public /* synthetic */ void lambda$addField$62(String str, String str2) {
        needHideProgress();
    }

    /* loaded from: classes3.dex */
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

    private SecureDocumentKey getSecureDocumentKey(byte[] bArr, byte[] bArr2) {
        byte[] computeSHA512 = Utilities.computeSHA512(decryptValueSecret(bArr, bArr2), bArr2);
        byte[] bArr3 = new byte[32];
        System.arraycopy(computeSHA512, 0, bArr3, 0, 32);
        byte[] bArr4 = new byte[16];
        System.arraycopy(computeSHA512, 32, bArr4, 0, 16);
        return new SecureDocumentKey(bArr3, bArr4);
    }

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
        if (!checkSecret(bArr5, null)) {
            return null;
        }
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

    /* JADX WARN: Can't wrap try/catch for region: R(8:2|(2:14|3)|(2:16|4)|7|12|8|9|(1:(0))) */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public EncryptionResult createSecureDocument(String str) {
        RandomAccessFile randomAccessFile;
        byte[] bArr = new byte[(int) new File(str).length()];
        RandomAccessFile randomAccessFile2 = null;
        try {
            randomAccessFile = new RandomAccessFile(str, "rws");
        } catch (Exception unused) {
        }
        try {
            randomAccessFile.readFully(bArr);
        } catch (Exception unused2) {
            randomAccessFile2 = randomAccessFile;
            randomAccessFile = randomAccessFile2;
            EncryptionResult encryptData = encryptData(bArr);
            randomAccessFile.seek(0L);
            randomAccessFile.write(encryptData.encryptedData);
            randomAccessFile.close();
            return encryptData;
        }
        EncryptionResult encryptData2 = encryptData(bArr);
        randomAccessFile.seek(0L);
        randomAccessFile.write(encryptData2.encryptedData);
        randomAccessFile.close();
        return encryptData2;
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
        if (!Arrays.equals(Utilities.computeSHA256(bArr6), bArr3)) {
            return null;
        }
        int i = bArr6[0] & 255;
        return new String(bArr6, i, length - i);
    }

    public static boolean checkSecret(byte[] bArr, Long l) {
        if (bArr == null || bArr.length != 32) {
            return false;
        }
        int i = 0;
        for (byte b : bArr) {
            i += b & 255;
        }
        if (i % 255 != 239) {
            return false;
        }
        return l == null || Utilities.bytesToLong(Utilities.computeSHA256(bArr)) == l.longValue();
    }

    public byte[] getRandomSecret() {
        byte[] bArr = new byte[32];
        Utilities.random.nextBytes(bArr);
        int i = 0;
        for (int i2 = 0; i2 < 32; i2++) {
            i += 255 & bArr[i2];
        }
        int i3 = i % 255;
        if (i3 != 239) {
            int nextInt = Utilities.random.nextInt(32);
            int i4 = (bArr[nextInt] & 255) + (239 - i3);
            if (i4 < 255) {
                i4 += 255;
            }
            bArr[nextInt] = (byte) (i4 % 255);
        }
        return bArr;
    }

    public EncryptionResult encryptData(byte[] bArr) {
        byte[] randomSecret = getRandomSecret();
        int nextInt = Utilities.random.nextInt(208) + 32;
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

    public void showAlertWithText(String str, String str2) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString("OK", 2131627127), null);
        builder.setTitle(str);
        builder.setMessage(str2);
        showDialog(builder.create());
    }

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
        AndroidUtilities.shakeView(this.inputFields[0], 2.0f, 0);
    }

    public void startPhoneVerification(boolean z, String str, Runnable runnable, ErrorRunnable errorRunnable, PassportActivityDelegate passportActivityDelegate) {
        boolean z2;
        TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
        boolean z3 = true;
        boolean z4 = (telephonyManager.getSimState() == 1 || telephonyManager.getPhoneType() == 0) ? false : true;
        if (getParentActivity() == null || Build.VERSION.SDK_INT < 23 || !z4) {
            z2 = true;
        } else {
            z2 = getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0;
            if (z) {
                this.permissionsItems.clear();
                if (!z2) {
                    this.permissionsItems.add("android.permission.READ_PHONE_STATE");
                }
                if (!this.permissionsItems.isEmpty()) {
                    if (getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                        builder.setTitle(LocaleController.getString("AppName", 2131624384));
                        builder.setPositiveButton(LocaleController.getString("OK", 2131627127), null);
                        builder.setMessage(LocaleController.getString("AllowReadCall", 2131624338));
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
        TLRPC$TL_account_sendVerifyPhoneCode tLRPC$TL_account_sendVerifyPhoneCode = new TLRPC$TL_account_sendVerifyPhoneCode();
        tLRPC$TL_account_sendVerifyPhoneCode.phone_number = str;
        TLRPC$TL_codeSettings tLRPC$TL_codeSettings = new TLRPC$TL_codeSettings();
        tLRPC$TL_account_sendVerifyPhoneCode.settings = tLRPC$TL_codeSettings;
        if (!z4 || !z2) {
            z3 = false;
        }
        tLRPC$TL_codeSettings.allow_flashcall = z3;
        tLRPC$TL_codeSettings.allow_app_hash = ApplicationLoader.hasPlayServices;
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        if (tLRPC$TL_account_sendVerifyPhoneCode.settings.allow_app_hash) {
            sharedPreferences.edit().putString("sms_hash", BuildVars.SMS_HASH).commit();
        } else {
            sharedPreferences.edit().remove("sms_hash").commit();
        }
        if (tLRPC$TL_account_sendVerifyPhoneCode.settings.allow_flashcall) {
            try {
                String line1Number = telephonyManager.getLine1Number();
                if (!TextUtils.isEmpty(line1Number)) {
                    tLRPC$TL_account_sendVerifyPhoneCode.settings.current_number = PhoneNumberUtils.compare(str, line1Number);
                    TLRPC$TL_codeSettings tLRPC$TL_codeSettings2 = tLRPC$TL_account_sendVerifyPhoneCode.settings;
                    if (!tLRPC$TL_codeSettings2.current_number) {
                        tLRPC$TL_codeSettings2.allow_flashcall = false;
                    }
                } else {
                    tLRPC$TL_account_sendVerifyPhoneCode.settings.current_number = false;
                }
            } catch (Exception e) {
                tLRPC$TL_account_sendVerifyPhoneCode.settings.allow_flashcall = false;
                FileLog.e(e);
            }
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_sendVerifyPhoneCode, new PassportActivity$$ExternalSyntheticLambda66(this, str, passportActivityDelegate, tLRPC$TL_account_sendVerifyPhoneCode), 2);
    }

    public /* synthetic */ void lambda$startPhoneVerification$66(String str, PassportActivityDelegate passportActivityDelegate, TLRPC$TL_account_sendVerifyPhoneCode tLRPC$TL_account_sendVerifyPhoneCode, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda58(this, tLRPC$TL_error, str, passportActivityDelegate, tLObject, tLRPC$TL_account_sendVerifyPhoneCode));
    }

    public /* synthetic */ void lambda$startPhoneVerification$65(TLRPC$TL_error tLRPC$TL_error, String str, PassportActivityDelegate passportActivityDelegate, TLObject tLObject, TLRPC$TL_account_sendVerifyPhoneCode tLRPC$TL_account_sendVerifyPhoneCode) {
        if (tLRPC$TL_error == null) {
            HashMap hashMap = new HashMap();
            hashMap.put("phone", str);
            PassportActivity passportActivity = new PassportActivity(7, this.currentForm, this.currentPassword, this.currentType, (TLRPC$TL_secureValue) null, (TLRPC$TL_secureRequiredType) null, (TLRPC$TL_secureValue) null, hashMap, (HashMap<String, String>) null);
            passportActivity.currentAccount = this.currentAccount;
            passportActivity.saltedPassword = this.saltedPassword;
            passportActivity.secureSecret = this.secureSecret;
            passportActivity.delegate = passportActivityDelegate;
            passportActivity.currentPhoneVerification = (TLRPC$TL_auth_sentCode) tLObject;
            presentFragment(passportActivity, true);
            return;
        }
        AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, this, tLRPC$TL_account_sendVerifyPhoneCode, str);
    }

    public void updatePasswordInterface() {
        ImageView imageView = this.noPasswordImageView;
        if (imageView == null) {
            return;
        }
        TLRPC$TL_account_password tLRPC$TL_account_password = this.currentPassword;
        if (tLRPC$TL_account_password == null || this.usingSavedPassword != 0) {
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
        } else if (!tLRPC$TL_account_password.has_password) {
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
            if (this.inputFields == null) {
                return;
            }
            TLRPC$TL_account_password tLRPC$TL_account_password2 = this.currentPassword;
            if (tLRPC$TL_account_password2 != null && !TextUtils.isEmpty(tLRPC$TL_account_password2.hint)) {
                this.inputFields[0].setHint(this.currentPassword.hint);
            } else {
                this.inputFields[0].setHint(LocaleController.getString("LoginPassword", 2131626551));
            }
        }
    }

    public void showEditDoneProgress(boolean z, boolean z2) {
        AnimatorSet animatorSet = this.doneItemAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (z && this.doneItem != null) {
            this.doneItemAnimation = new AnimatorSet();
            if (z2) {
                this.progressView.setVisibility(0);
                this.doneItem.setEnabled(false);
                this.doneItemAnimation.playTogether(ObjectAnimator.ofFloat(this.doneItem.getContentView(), View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.progressView, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.progressView, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.progressView, View.ALPHA, 1.0f));
            } else {
                this.doneItem.getContentView().setVisibility(0);
                this.doneItem.setEnabled(true);
                this.doneItemAnimation.playTogether(ObjectAnimator.ofFloat(this.progressView, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.progressView, View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.progressView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), View.ALPHA, 1.0f));
            }
            this.doneItemAnimation.addListener(new AnonymousClass21(z2));
            this.doneItemAnimation.setDuration(150L);
            this.doneItemAnimation.start();
        } else if (this.acceptTextView == null) {
        } else {
            this.doneItemAnimation = new AnimatorSet();
            if (z2) {
                this.progressViewButton.setVisibility(0);
                this.bottomLayout.setEnabled(false);
                this.doneItemAnimation.playTogether(ObjectAnimator.ofFloat(this.acceptTextView, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.acceptTextView, View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.acceptTextView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.progressViewButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.progressViewButton, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.progressViewButton, View.ALPHA, 1.0f));
            } else {
                this.acceptTextView.setVisibility(0);
                this.bottomLayout.setEnabled(true);
                this.doneItemAnimation.playTogether(ObjectAnimator.ofFloat(this.progressViewButton, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.progressViewButton, View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.progressViewButton, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.acceptTextView, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.acceptTextView, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.acceptTextView, View.ALPHA, 1.0f));
            }
            this.doneItemAnimation.addListener(new AnonymousClass22(z2));
            this.doneItemAnimation.setDuration(150L);
            this.doneItemAnimation.start();
        }
    }

    /* renamed from: org.telegram.ui.PassportActivity$21 */
    /* loaded from: classes3.dex */
    public class AnonymousClass21 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$show;

        AnonymousClass21(boolean z) {
            PassportActivity.this = r1;
            this.val$show = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (PassportActivity.this.doneItemAnimation == null || !PassportActivity.this.doneItemAnimation.equals(animator)) {
                return;
            }
            if (!this.val$show) {
                PassportActivity.this.progressView.setVisibility(4);
            } else {
                PassportActivity.this.doneItem.getContentView().setVisibility(4);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (PassportActivity.this.doneItemAnimation == null || !PassportActivity.this.doneItemAnimation.equals(animator)) {
                return;
            }
            PassportActivity.this.doneItemAnimation = null;
        }
    }

    /* renamed from: org.telegram.ui.PassportActivity$22 */
    /* loaded from: classes3.dex */
    public class AnonymousClass22 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$show;

        AnonymousClass22(boolean z) {
            PassportActivity.this = r1;
            this.val$show = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (PassportActivity.this.doneItemAnimation == null || !PassportActivity.this.doneItemAnimation.equals(animator)) {
                return;
            }
            if (!this.val$show) {
                PassportActivity.this.progressViewButton.setVisibility(4);
            } else {
                PassportActivity.this.acceptTextView.setVisibility(4);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (PassportActivity.this.doneItemAnimation == null || !PassportActivity.this.doneItemAnimation.equals(animator)) {
                return;
            }
            PassportActivity.this.doneItemAnimation = null;
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        SecureDocumentCell secureDocumentCell;
        ActionBarMenuItem actionBarMenuItem;
        if (i == NotificationCenter.fileUploaded) {
            String str = (String) objArr[0];
            SecureDocument secureDocument = this.uploadingDocuments.get(str);
            if (secureDocument == null) {
                return;
            }
            secureDocument.inputFile = (TLRPC$TL_inputFile) objArr[1];
            this.uploadingDocuments.remove(str);
            if (this.uploadingDocuments.isEmpty() && (actionBarMenuItem = this.doneItem) != null) {
                actionBarMenuItem.setEnabled(true);
                this.doneItem.setAlpha(1.0f);
            }
            HashMap<SecureDocument, SecureDocumentCell> hashMap = this.documentsCells;
            if (hashMap != null && (secureDocumentCell = hashMap.get(secureDocument)) != null) {
                secureDocumentCell.updateButtonState(true);
            }
            HashMap<String, String> hashMap2 = this.errorsValues;
            if (hashMap2 != null && hashMap2.containsKey("error_document_all")) {
                this.errorsValues.remove("error_document_all");
                checkTopErrorCell(false);
            }
            int i3 = secureDocument.type;
            if (i3 == 0) {
                if (this.bottomCell != null && !TextUtils.isEmpty(this.noAllDocumentsErrorText)) {
                    this.bottomCell.setText(this.noAllDocumentsErrorText);
                }
                this.errorsValues.remove("files_all");
            } else if (i3 != 4) {
            } else {
                if (this.bottomCellTranslation != null && !TextUtils.isEmpty(this.noAllTranslationErrorText)) {
                    this.bottomCellTranslation.setText(this.noAllTranslationErrorText);
                }
                this.errorsValues.remove("translation_all");
            }
        } else if (i == NotificationCenter.fileUploadFailed) {
        } else {
            if (i == NotificationCenter.twoStepPasswordChanged) {
                if (objArr != null && objArr.length > 0) {
                    if (objArr[7] != null) {
                        EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                        if (editTextBoldCursorArr[0] != null) {
                            editTextBoldCursorArr[0].setText((String) objArr[7]);
                        }
                    }
                    if (objArr[6] == null) {
                        TLRPC$TL_account_password tLRPC$TL_account_password = new TLRPC$TL_account_password();
                        this.currentPassword = tLRPC$TL_account_password;
                        tLRPC$TL_account_password.current_algo = (TLRPC$PasswordKdfAlgo) objArr[1];
                        tLRPC$TL_account_password.new_secure_algo = (TLRPC$SecurePasswordKdfAlgo) objArr[2];
                        tLRPC$TL_account_password.secure_random = (byte[]) objArr[3];
                        tLRPC$TL_account_password.has_recovery = !TextUtils.isEmpty((String) objArr[4]);
                        TLRPC$TL_account_password tLRPC$TL_account_password2 = this.currentPassword;
                        tLRPC$TL_account_password2.hint = (String) objArr[5];
                        tLRPC$TL_account_password2.srp_id = -1L;
                        byte[] bArr = new byte[256];
                        tLRPC$TL_account_password2.srp_B = bArr;
                        Utilities.random.nextBytes(bArr);
                        EditTextBoldCursor[] editTextBoldCursorArr2 = this.inputFields;
                        if (editTextBoldCursorArr2[0] != null && editTextBoldCursorArr2[0].length() > 0) {
                            this.usingSavedPassword = 2;
                        }
                    }
                } else {
                    this.currentPassword = null;
                    loadPasswordInfo();
                }
                updatePasswordInterface();
                return;
            }
            int i4 = NotificationCenter.didRemoveTwoStepPassword;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (this.presentAfterAnimation != null) {
            AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda49(this));
        }
        int i = this.currentActivityType;
        if (i == 5) {
            if (!z) {
                return;
            }
            if (this.inputFieldContainers[0].getVisibility() == 0) {
                this.inputFields[0].requestFocus();
                AndroidUtilities.showKeyboard(this.inputFields[0]);
            }
            if (this.usingSavedPassword != 2) {
                return;
            }
            onPasswordDone(false);
        } else if (i == 7) {
            if (!z) {
                return;
            }
            this.views[this.currentViewNum].onShow();
        } else if (i == 4) {
            if (!z) {
                return;
            }
            this.inputFields[0].requestFocus();
            AndroidUtilities.showKeyboard(this.inputFields[0]);
        } else if (i == 6) {
            if (!z) {
                return;
            }
            this.inputFields[0].requestFocus();
            AndroidUtilities.showKeyboard(this.inputFields[0]);
        } else if ((i != 2 && i != 1) || Build.VERSION.SDK_INT < 21) {
        } else {
            createChatAttachView();
        }
    }

    public /* synthetic */ void lambda$onTransitionAnimationEnd$67() {
        presentFragment(this.presentAfterAnimation, true);
        this.presentAfterAnimation = null;
    }

    private void showAttachmentError() {
        if (getParentActivity() == null) {
            return;
        }
        Toast.makeText(getParentActivity(), LocaleController.getString("UnsupportedAttachment", 2131628826), 0).show();
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
            } else if (i != 1) {
            } else {
                if (intent == null || intent.getData() == null) {
                    showAttachmentError();
                    return;
                }
                ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList = new ArrayList<>();
                SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                sendingMediaInfo.uri = intent.getData();
                arrayList.add(sendingMediaInfo);
                processSelectedFiles(arrayList);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        ChatAttachAlert chatAttachAlert;
        TextSettingsCell textSettingsCell;
        int i2 = this.currentActivityType;
        if ((i2 != 1 && i2 != 2) || (chatAttachAlert = this.chatAttachAlert) == null) {
            if (i2 != 3 || i != 6) {
                return;
            }
            startPhoneVerification(false, this.pendingPhone, this.pendingFinishRunnable, this.pendingErrorRunnable, this.pendingDelegate);
        } else if (i == 17) {
            chatAttachAlert.getPhotoLayout().checkCamera(false);
        } else if (i == 21) {
            if (getParentActivity() == null || iArr == null || iArr.length == 0 || iArr[0] == 0) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", 2131624384));
            builder.setMessage(LocaleController.getString("PermissionNoAudioVideoWithHint", 2131627523));
            builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", 2131627535), new PassportActivity$$ExternalSyntheticLambda4(this));
            builder.setPositiveButton(LocaleController.getString("OK", 2131627127), null);
            builder.show();
        } else if (i == 19 && iArr != null && iArr.length > 0 && iArr[0] == 0) {
            processSelectedAttach(0);
        } else if (i != 22 || iArr == null || iArr.length <= 0 || iArr[0] != 0 || (textSettingsCell = this.scanDocumentCell) == null) {
        } else {
            textSettingsCell.callOnClick();
        }
    }

    public /* synthetic */ void lambda$onRequestPermissionsResultFragment$68(DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
            getParentActivity().startActivity(intent);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void saveSelfArgs(Bundle bundle) {
        String str = this.currentPicturePath;
        if (str != null) {
            bundle.putString("path", str);
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
                if (slideViewArr[i2] != null) {
                    slideViewArr[i2].onDestroyActivity();
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

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onDialogDismiss(Dialog dialog) {
        if (this.currentActivityType != 3 || Build.VERSION.SDK_INT < 23 || dialog != this.permissionsDialog || this.permissionsItems.isEmpty()) {
            return;
        }
        getParentActivity().requestPermissions((String[]) this.permissionsItems.toArray(new String[0]), 6);
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

    public void setPage(int i, boolean z, Bundle bundle) {
        if (i == 3) {
            this.doneItem.setVisibility(8);
        }
        SlideView[] slideViewArr = this.views;
        SlideView slideView = slideViewArr[this.currentViewNum];
        SlideView slideView2 = slideViewArr[i];
        this.currentViewNum = i;
        slideView2.setParams(bundle, false);
        slideView2.onShow();
        if (z) {
            slideView2.setTranslationX(AndroidUtilities.displaySize.x);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.setDuration(300L);
            animatorSet.playTogether(ObjectAnimator.ofFloat(slideView, "translationX", -AndroidUtilities.displaySize.x), ObjectAnimator.ofFloat(slideView2, "translationX", 0.0f));
            animatorSet.addListener(new AnonymousClass23(this, slideView2, slideView));
            animatorSet.start();
            return;
        }
        slideView2.setTranslationX(0.0f);
        slideView2.setVisibility(0);
        if (slideView == slideView2) {
            return;
        }
        slideView.setVisibility(8);
    }

    /* renamed from: org.telegram.ui.PassportActivity$23 */
    /* loaded from: classes3.dex */
    public class AnonymousClass23 extends AnimatorListenerAdapter {
        final /* synthetic */ SlideView val$newView;
        final /* synthetic */ SlideView val$outView;

        AnonymousClass23(PassportActivity passportActivity, SlideView slideView, SlideView slideView2) {
            this.val$newView = slideView;
            this.val$outView = slideView2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.val$newView.setVisibility(0);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.val$outView.setVisibility(8);
            this.val$outView.setX(0.0f);
        }
    }

    public void fillNextCodeParams(Bundle bundle, TLRPC$TL_auth_sentCode tLRPC$TL_auth_sentCode, boolean z) {
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
        } else if (tLRPC$auth_SentCodeType instanceof TLRPC$TL_auth_sentCodeTypeFlashCall) {
            bundle.putInt("type", 3);
            bundle.putString("pattern", tLRPC$TL_auth_sentCode.type.pattern);
            setPage(1, z, bundle);
        } else if (!(tLRPC$auth_SentCodeType instanceof TLRPC$TL_auth_sentCodeTypeSms)) {
        } else {
            bundle.putInt("type", 2);
            bundle.putInt("length", tLRPC$TL_auth_sentCode.type.length);
            setPage(0, z, bundle);
        }
    }

    private void openAttachMenu() {
        if (getParentActivity() == null) {
            return;
        }
        boolean z = true;
        if (this.uploadingFileType == 0 && this.documents.size() >= 20) {
            showAlertWithText(LocaleController.getString("AppName", 2131624384), LocaleController.formatString("PassportUploadMaxReached", 2131627401, LocaleController.formatPluralString("Files", 20, new Object[0])));
            return;
        }
        createChatAttachView();
        ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (this.uploadingFileType != 1) {
            z = false;
        }
        chatAttachAlert.setOpenWithFrontFaceCamera(z);
        this.chatAttachAlert.setMaxSelectedPhotos(getMaxSelectedDocuments(), false);
        this.chatAttachAlert.getPhotoLayout().loadGalleryPhotos();
        int i = Build.VERSION.SDK_INT;
        if (i == 21 || i == 22) {
            AndroidUtilities.hideKeyboard(this.fragmentView.findFocus());
        }
        this.chatAttachAlert.init();
        showDialog(this.chatAttachAlert);
    }

    private void createChatAttachView() {
        if (getParentActivity() != null && this.chatAttachAlert == null) {
            ChatAttachAlert chatAttachAlert = new ChatAttachAlert(getParentActivity(), this, false, false);
            this.chatAttachAlert = chatAttachAlert;
            chatAttachAlert.setDelegate(new AnonymousClass24());
        }
    }

    /* renamed from: org.telegram.ui.PassportActivity$24 */
    /* loaded from: classes3.dex */
    public class AnonymousClass24 implements ChatAttachAlert.ChatAttachViewDelegate {
        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void didSelectBot(TLRPC$User tLRPC$User) {
            ChatAttachAlert.ChatAttachViewDelegate.CC.$default$didSelectBot(this, tLRPC$User);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void doOnIdle(Runnable runnable) {
            runnable.run();
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ boolean needEnterComment() {
            return ChatAttachAlert.ChatAttachViewDelegate.CC.$default$needEnterComment(this);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public /* synthetic */ void openAvatarsSearch() {
            ChatAttachAlert.ChatAttachViewDelegate.CC.$default$openAvatarsSearch(this);
        }

        AnonymousClass24() {
            PassportActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public void didPressedButton(int i, boolean z, boolean z2, int i2, boolean z3) {
            if (PassportActivity.this.getParentActivity() == null || PassportActivity.this.chatAttachAlert == null) {
                return;
            }
            if (i == 8 || i == 7) {
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
                    if (str != null) {
                        sendingMediaInfo.path = str;
                    } else {
                        sendingMediaInfo.path = photoEntry.path;
                    }
                    arrayList.add(sendingMediaInfo);
                    photoEntry.reset();
                }
                PassportActivity.this.processSelectedFiles(arrayList);
                return;
            }
            if (PassportActivity.this.chatAttachAlert != null) {
                PassportActivity.this.chatAttachAlert.dismissWithButtonClick(i);
            }
            PassportActivity.this.processSelectedAttach(i);
        }

        @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
        public void onCameraOpened() {
            AndroidUtilities.hideKeyboard(((BaseFragment) PassportActivity.this).fragmentView.findFocus());
        }
    }

    private int getMaxSelectedDocuments() {
        int size;
        int i = this.uploadingFileType;
        if (i == 0) {
            size = this.documents.size();
        } else if (i != 4) {
            return 1;
        } else {
            size = this.translationDocuments.size();
        }
        return 20 - size;
    }

    public void processSelectedAttach(int i) {
        if (i == 0) {
            int i2 = Build.VERSION.SDK_INT;
            if (i2 >= 23 && getParentActivity().checkSelfPermission("android.permission.CAMERA") != 0) {
                getParentActivity().requestPermissions(new String[]{"android.permission.CAMERA"}, 19);
                return;
            }
            try {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File generatePicturePath = AndroidUtilities.generatePicturePath();
                if (generatePicturePath != null) {
                    if (i2 >= 24) {
                        intent.putExtra("output", FileProvider.getUriForFile(getParentActivity(), "org.telegram.messenger.beta.provider", generatePicturePath));
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

    public void didSelectPhotos(ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList, boolean z, int i) {
        processSelectedFiles(arrayList);
    }

    public void startDocumentSelectActivity() {
        try {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            if (Build.VERSION.SDK_INT >= 18) {
                intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
            }
            intent.setType("*/*");
            startActivityForResult(intent, 21);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void didSelectFiles(ArrayList<String> arrayList, String str, boolean z, int i) {
        ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList2 = new ArrayList<>();
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
            sendingMediaInfo.path = arrayList.get(i2);
            arrayList2.add(sendingMediaInfo);
        }
        processSelectedFiles(arrayList2);
    }

    private void fillInitialValues() {
        if (this.initialValues != null) {
            return;
        }
        this.initialValues = getCurrentValues();
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
            sb.append(this.documents.get(i3).secureFile.id);
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
            sb.append(this.translationDocuments.get(i4).secureFile.id);
        }
        return sb.toString();
    }

    public boolean isHasNotAnyChanges() {
        String str = this.initialValues;
        return str == null || str.equals(getCurrentValues());
    }

    public boolean checkDiscard() {
        if (isHasNotAnyChanges()) {
            return false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString("PassportDiscard", 2131627272), new PassportActivity$$ExternalSyntheticLambda1(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        builder.setTitle(LocaleController.getString("DiscardChanges", 2131625502));
        builder.setMessage(LocaleController.getString("PassportDiscardChanges", 2131627273));
        showDialog(builder.create());
        return true;
    }

    public /* synthetic */ void lambda$checkDiscard$69(DialogInterface dialogInterface, int i) {
        finishFragment();
    }

    public void processSelectedFiles(ArrayList<SendMessagesHelper.SendingMediaInfo> arrayList) {
        if (arrayList.isEmpty()) {
            return;
        }
        int i = this.uploadingFileType;
        boolean z = true;
        boolean z2 = false;
        if (i != 1 && i != 4 && (this.currentType.type instanceof TLRPC$TL_secureValueTypePersonalDetails)) {
            int i2 = 0;
            while (true) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                if (i2 < editTextBoldCursorArr.length) {
                    if (i2 != 5 && i2 != 8 && i2 != 4 && i2 != 6 && editTextBoldCursorArr[i2].length() > 0) {
                        z = false;
                        break;
                    }
                    i2++;
                } else {
                    break;
                }
            }
            z2 = z;
        }
        Utilities.globalQueue.postRunnable(new PassportActivity$$ExternalSyntheticLambda53(this, arrayList, this.uploadingFileType, z2));
    }

    public /* synthetic */ void lambda$processSelectedFiles$72(ArrayList arrayList, int i, boolean z) {
        TLRPC$PhotoSize scaleAndSaveImage;
        Throwable th;
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
                SecureDocument saveFile = this.delegate.saveFile(tLRPC$TL_secureFile);
                saveFile.type = i;
                AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda55(this, saveFile, i));
                if (z && !z2) {
                    try {
                        MrzRecognizer.Result recognize = MrzRecognizer.recognize(loadBitmap, this.currentDocumentsType.type instanceof TLRPC$TL_secureValueTypeDriverLicense);
                        if (recognize != null) {
                            try {
                                AndroidUtilities.runOnUIThread(new PassportActivity$$ExternalSyntheticLambda54(this, recognize));
                                z2 = true;
                            } catch (Throwable th2) {
                                th = th2;
                                z2 = true;
                                FileLog.e(th);
                            }
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                }
            }
        }
        SharedConfig.saveConfig();
    }

    public /* synthetic */ void lambda$processSelectedFiles$70(SecureDocument secureDocument, int i) {
        int i2 = this.uploadingFileType;
        if (i2 == 1) {
            SecureDocument secureDocument2 = this.selfieDocument;
            if (secureDocument2 != null) {
                SecureDocumentCell remove = this.documentsCells.remove(secureDocument2);
                if (remove != null) {
                    this.selfieLayout.removeView(remove);
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
                SecureDocumentCell remove2 = this.documentsCells.remove(secureDocument3);
                if (remove2 != null) {
                    this.frontLayout.removeView(remove2);
                }
                this.frontDocument = null;
            }
        } else if (i2 == 3) {
            SecureDocument secureDocument4 = this.reverseDocument;
            if (secureDocument4 != null) {
                SecureDocumentCell remove3 = this.documentsCells.remove(secureDocument4);
                if (remove3 != null) {
                    this.reverseLayout.removeView(remove3);
                }
                this.reverseDocument = null;
            }
        } else if (i2 == 0 && this.documents.size() >= 20) {
            return;
        }
        this.uploadingDocuments.put(secureDocument.path, secureDocument);
        this.doneItem.setEnabled(false);
        this.doneItem.setAlpha(0.5f);
        FileLoader.getInstance(this.currentAccount).uploadFile(secureDocument.path, false, true, 16777216);
        addDocumentView(secureDocument, i);
        updateUploadText(i);
    }

    public /* synthetic */ void lambda$processSelectedFiles$71(MrzRecognizer.Result result) {
        int i;
        int i2;
        int i3 = result.type;
        if (i3 == 2) {
            if (!(this.currentDocumentsType.type instanceof TLRPC$TL_secureValueTypeIdentityCard)) {
                int size = this.availableDocumentTypes.size();
                int i4 = 0;
                while (true) {
                    if (i4 >= size) {
                        break;
                    }
                    TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType = this.availableDocumentTypes.get(i4);
                    if (tLRPC$TL_secureRequiredType.type instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                        this.currentDocumentsType = tLRPC$TL_secureRequiredType;
                        updateInterfaceStringsForDocumentType();
                        break;
                    }
                    i4++;
                }
            }
        } else if (i3 == 1) {
            if (!(this.currentDocumentsType.type instanceof TLRPC$TL_secureValueTypePassport)) {
                int size2 = this.availableDocumentTypes.size();
                int i5 = 0;
                while (true) {
                    if (i5 >= size2) {
                        break;
                    }
                    TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType2 = this.availableDocumentTypes.get(i5);
                    if (tLRPC$TL_secureRequiredType2.type instanceof TLRPC$TL_secureValueTypePassport) {
                        this.currentDocumentsType = tLRPC$TL_secureRequiredType2;
                        updateInterfaceStringsForDocumentType();
                        break;
                    }
                    i5++;
                }
            }
        } else if (i3 == 3) {
            if (!(this.currentDocumentsType.type instanceof TLRPC$TL_secureValueTypeInternalPassport)) {
                int size3 = this.availableDocumentTypes.size();
                int i6 = 0;
                while (true) {
                    if (i6 >= size3) {
                        break;
                    }
                    TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType3 = this.availableDocumentTypes.get(i6);
                    if (tLRPC$TL_secureRequiredType3.type instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                        this.currentDocumentsType = tLRPC$TL_secureRequiredType3;
                        updateInterfaceStringsForDocumentType();
                        break;
                    }
                    i6++;
                }
            }
        } else if (i3 == 4 && !(this.currentDocumentsType.type instanceof TLRPC$TL_secureValueTypeDriverLicense)) {
            int size4 = this.availableDocumentTypes.size();
            int i7 = 0;
            while (true) {
                if (i7 >= size4) {
                    break;
                }
                TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType4 = this.availableDocumentTypes.get(i7);
                if (tLRPC$TL_secureRequiredType4.type instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                    this.currentDocumentsType = tLRPC$TL_secureRequiredType4;
                    updateInterfaceStringsForDocumentType();
                    break;
                }
                i7++;
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
        int i8 = result.gender;
        if (i8 != 0) {
            if (i8 == 1) {
                this.currentGender = "male";
                this.inputFields[4].setText(LocaleController.getString("PassportMale", 2131627344));
            } else if (i8 == 2) {
                this.currentGender = "female";
                this.inputFields[4].setText(LocaleController.getString("PassportFemale", 2131627283));
            }
        }
        if (!TextUtils.isEmpty(result.nationality)) {
            String str = result.nationality;
            this.currentCitizeship = str;
            String str2 = this.languageMap.get(str);
            if (str2 != null) {
                this.inputFields[5].setText(str2);
            }
        }
        if (!TextUtils.isEmpty(result.issuingCountry)) {
            String str3 = result.issuingCountry;
            this.currentResidence = str3;
            String str4 = this.languageMap.get(str3);
            if (str4 != null) {
                this.inputFields[6].setText(str4);
            }
        }
        int i9 = result.birthDay;
        if (i9 > 0 && result.birthMonth > 0 && result.birthYear > 0) {
            this.inputFields[3].setText(String.format(Locale.US, "%02d.%02d.%d", Integer.valueOf(i9), Integer.valueOf(result.birthMonth), Integer.valueOf(result.birthYear)));
        }
        int i10 = result.expiryDay;
        if (i10 > 0 && (i = result.expiryMonth) > 0 && (i2 = result.expiryYear) > 0) {
            int[] iArr = this.currentExpireDate;
            iArr[0] = i2;
            iArr[1] = i;
            iArr[2] = i10;
            this.inputFields[8].setText(String.format(Locale.US, "%02d.%02d.%d", Integer.valueOf(i10), Integer.valueOf(result.expiryMonth), Integer.valueOf(result.expiryYear)));
            return;
        }
        int[] iArr2 = this.currentExpireDate;
        iArr2[2] = 0;
        iArr2[1] = 0;
        iArr2[0] = 0;
        this.inputFields[8].setText(LocaleController.getString("PassportNoExpireDate", 2131627358));
    }

    public void setNeedActivityResult(boolean z) {
        this.needActivityResult = z;
    }

    /* loaded from: classes3.dex */
    public static class ProgressView extends View {
        private Paint paint = new Paint();
        private Paint paint2 = new Paint();
        private float progress;

        public ProgressView(Context context) {
            super(context);
            this.paint.setColor(Theme.getColor("login_progressInner"));
            this.paint2.setColor(Theme.getColor("login_progressOuter"));
        }

        public void setProgress(float f) {
            this.progress = f;
            invalidate();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float measuredWidth = (int) (getMeasuredWidth() * this.progress);
            canvas.drawRect(0.0f, 0.0f, measuredWidth, getMeasuredHeight(), this.paint2);
            canvas.drawRect(measuredWidth, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.paint);
        }
    }

    /* loaded from: classes3.dex */
    public class PhoneConfirmationView extends SlideView implements NotificationCenter.NotificationCenterDelegate {
        private ImageView blackImageView;
        private ImageView blueImageView;
        private EditTextBoldCursor[] codeField;
        private LinearLayout codeFieldContainer;
        private Timer codeTimer;
        private TextView confirmTextView;
        private boolean ignoreOnTextChange;
        private double lastCodeTime;
        private double lastCurrentTime;
        private int length;
        private boolean nextPressed;
        private int nextType;
        private String phone;
        private String phoneHash;
        private TextView problemText;
        private ProgressView progressView;
        private TextView timeText;
        private Timer timeTimer;
        private int timeout;
        private TextView titleTextView;
        private int verificationType;
        private boolean waitingForEvent;
        private final Object timerSync = new Object();
        private int time = 60000;
        private int codeTime = 15000;
        private String lastError = "";
        private String pattern = "*";

        public static /* synthetic */ void lambda$onBackPressed$9(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean needBackButton() {
            return true;
        }

        static /* synthetic */ int access$10026(PhoneConfirmationView phoneConfirmationView, double d) {
            double d2 = phoneConfirmationView.codeTime;
            Double.isNaN(d2);
            int i = (int) (d2 - d);
            phoneConfirmationView.codeTime = i;
            return i;
        }

        static /* synthetic */ int access$10626(PhoneConfirmationView phoneConfirmationView, double d) {
            double d2 = phoneConfirmationView.time;
            Double.isNaN(d2);
            int i = (int) (d2 - d);
            phoneConfirmationView.time = i;
            return i;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PhoneConfirmationView(Context context, int i) {
            super(context);
            PassportActivity.this = r25;
            this.verificationType = i;
            setOrientation(1);
            TextView textView = new TextView(context);
            this.confirmTextView = textView;
            textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.confirmTextView.setTextSize(1, 14.0f);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            TextView textView2 = new TextView(context);
            this.titleTextView = textView2;
            textView2.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.titleTextView.setTextSize(1, 18.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.titleTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.titleTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.titleTextView.setGravity(49);
            if (this.verificationType == 3) {
                this.confirmTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                FrameLayout frameLayout = new FrameLayout(context);
                addView(frameLayout, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3));
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(2131166044);
                boolean z = LocaleController.isRTL;
                if (z) {
                    frameLayout.addView(imageView, LayoutHelper.createFrame(64, 76.0f, 19, 2.0f, 2.0f, 0.0f, 0.0f));
                    frameLayout.addView(this.confirmTextView, LayoutHelper.createFrame(-1, -2.0f, LocaleController.isRTL ? 5 : 3, 82.0f, 0.0f, 0.0f, 0.0f));
                } else {
                    frameLayout.addView(this.confirmTextView, LayoutHelper.createFrame(-1, -2.0f, z ? 5 : 3, 0.0f, 0.0f, 82.0f, 0.0f));
                    frameLayout.addView(imageView, LayoutHelper.createFrame(64, 76.0f, 21, 0.0f, 2.0f, 0.0f, 2.0f));
                }
            } else {
                this.confirmTextView.setGravity(49);
                FrameLayout frameLayout2 = new FrameLayout(context);
                addView(frameLayout2, LayoutHelper.createLinear(-2, -2, 49));
                if (this.verificationType == 1) {
                    ImageView imageView2 = new ImageView(context);
                    this.blackImageView = imageView2;
                    imageView2.setImageResource(2131166163);
                    this.blackImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"), PorterDuff.Mode.MULTIPLY));
                    frameLayout2.addView(this.blackImageView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    ImageView imageView3 = new ImageView(context);
                    this.blueImageView = imageView3;
                    imageView3.setImageResource(2131166161);
                    this.blueImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionBackground"), PorterDuff.Mode.MULTIPLY));
                    frameLayout2.addView(this.blueImageView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    this.titleTextView.setText(LocaleController.getString("SentAppCodeTitle", 2131628285));
                } else {
                    ImageView imageView4 = new ImageView(context);
                    this.blueImageView = imageView4;
                    imageView4.setImageResource(2131166162);
                    this.blueImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionBackground"), PorterDuff.Mode.MULTIPLY));
                    frameLayout2.addView(this.blueImageView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    this.titleTextView.setText(LocaleController.getString("SentSmsCodeTitle", 2131628290));
                }
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
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, context, r25);
            this.timeText = anonymousClass1;
            anonymousClass1.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.timeText.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            if (this.verificationType == 3) {
                this.timeText.setTextSize(1, 14.0f);
                addView(this.timeText, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3));
                this.progressView = new ProgressView(context);
                this.timeText.setGravity(LocaleController.isRTL ? 5 : 3);
                addView(this.progressView, LayoutHelper.createLinear(-1, 3, 0.0f, 12.0f, 0.0f, 0.0f));
            } else {
                this.timeText.setPadding(0, AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(10.0f));
                this.timeText.setTextSize(1, 15.0f);
                this.timeText.setGravity(49);
                addView(this.timeText, LayoutHelper.createLinear(-2, -2, 49));
            }
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(this, context, r25);
            this.problemText = anonymousClass2;
            anonymousClass2.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.problemText.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.problemText.setPadding(0, AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(10.0f));
            this.problemText.setTextSize(1, 15.0f);
            this.problemText.setGravity(49);
            if (this.verificationType == 1) {
                this.problemText.setText(LocaleController.getString("DidNotGetTheCodeSms", 2131625491));
            } else {
                this.problemText.setText(LocaleController.getString("DidNotGetTheCode", 2131625486));
            }
            addView(this.problemText, LayoutHelper.createLinear(-2, -2, 49));
            this.problemText.setOnClickListener(new PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda2(this));
        }

        /* renamed from: org.telegram.ui.PassportActivity$PhoneConfirmationView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends TextView {
            AnonymousClass1(PhoneConfirmationView phoneConfirmationView, Context context, PassportActivity passportActivity) {
                super(context);
            }

            @Override // android.widget.TextView, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
            }
        }

        /* renamed from: org.telegram.ui.PassportActivity$PhoneConfirmationView$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends TextView {
            AnonymousClass2(PhoneConfirmationView phoneConfirmationView, Context context, PassportActivity passportActivity) {
                super(context);
            }

            @Override // android.widget.TextView, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
            }
        }

        public /* synthetic */ void lambda$new$0(View view) {
            if (this.nextPressed) {
                return;
            }
            int i = this.nextType;
            if (!((i == 4 && this.verificationType == 2) || i == 0)) {
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
                AlertsCreator.showSimpleAlert(PassportActivity.this, LocaleController.getString("NoMailInstalled", 2131626882));
            }
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
            if (PassportActivity.this.scrollHeight - measuredHeight >= dp) {
                setMeasuredDimension(getMeasuredWidth(), Math.min(PassportActivity.this.scrollHeight, dp2));
            } else {
                setMeasuredDimension(getMeasuredWidth(), measuredHeight + dp);
            }
        }

        @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            super.onLayout(z, i, i2, i3, i4);
            if (this.verificationType == 3 || this.blueImageView == null) {
                return;
            }
            int bottom = this.confirmTextView.getBottom();
            int measuredHeight = getMeasuredHeight() - bottom;
            if (this.problemText.getVisibility() == 0) {
                int measuredHeight2 = this.problemText.getMeasuredHeight();
                i5 = (measuredHeight + bottom) - measuredHeight2;
                TextView textView = this.problemText;
                textView.layout(textView.getLeft(), i5, this.problemText.getRight(), measuredHeight2 + i5);
            } else if (this.timeText.getVisibility() == 0) {
                int measuredHeight3 = this.timeText.getMeasuredHeight();
                i5 = (measuredHeight + bottom) - measuredHeight3;
                TextView textView2 = this.timeText;
                textView2.layout(textView2.getLeft(), i5, this.timeText.getRight(), measuredHeight3 + i5);
            } else {
                i5 = measuredHeight + bottom;
            }
            int measuredHeight4 = this.codeFieldContainer.getMeasuredHeight();
            int i6 = (((i5 - bottom) - measuredHeight4) / 2) + bottom;
            LinearLayout linearLayout = this.codeFieldContainer;
            linearLayout.layout(linearLayout.getLeft(), i6, this.codeFieldContainer.getRight(), measuredHeight4 + i6);
        }

        public void resendCode() {
            Bundle bundle = new Bundle();
            bundle.putString("phone", this.phone);
            this.nextPressed = true;
            PassportActivity.this.needShowProgress();
            TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode = new TLRPC$TL_auth_resendCode();
            tLRPC$TL_auth_resendCode.phone_number = this.phone;
            tLRPC$TL_auth_resendCode.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_resendCode, new PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda8(this, bundle, tLRPC$TL_auth_resendCode), 2);
        }

        public /* synthetic */ void lambda$resendCode$3(Bundle bundle, TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda5(this, tLRPC$TL_error, bundle, tLObject, tLRPC$TL_auth_resendCode));
        }

        public /* synthetic */ void lambda$resendCode$2(TLRPC$TL_error tLRPC$TL_error, Bundle bundle, TLObject tLObject, TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode) {
            this.nextPressed = false;
            if (tLRPC$TL_error == null) {
                PassportActivity.this.fillNextCodeParams(bundle, (TLRPC$TL_auth_sentCode) tLObject, true);
            } else {
                AlertDialog alertDialog = (AlertDialog) AlertsCreator.processError(((BaseFragment) PassportActivity.this).currentAccount, tLRPC$TL_error, PassportActivity.this, tLRPC$TL_auth_resendCode, new Object[0]);
                if (alertDialog != null && tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                    alertDialog.setPositiveButtonListener(new PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda0(this));
                }
            }
            PassportActivity.this.needHideProgress();
        }

        public /* synthetic */ void lambda$resendCode$1(DialogInterface dialogInterface, int i) {
            onBackPressed(true);
            PassportActivity.this.finishFragment();
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onCancelPressed() {
            this.nextPressed = false;
        }

        @Override // org.telegram.ui.Components.SlideView
        public void setParams(Bundle bundle, boolean z) {
            int i;
            int i2;
            if (bundle == null) {
                return;
            }
            this.waitingForEvent = true;
            int i3 = this.verificationType;
            if (i3 == 2) {
                AndroidUtilities.setWaitingForSms(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i3 == 3) {
                AndroidUtilities.setWaitingForCall(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }
            this.phone = bundle.getString("phone");
            this.phoneHash = bundle.getString("phoneHash");
            int i4 = bundle.getInt("timeout");
            this.time = i4;
            this.timeout = i4;
            this.nextType = bundle.getInt("nextType");
            this.pattern = bundle.getString("pattern");
            int i5 = bundle.getInt("length");
            this.length = i5;
            if (i5 == 0) {
                this.length = 5;
            }
            EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
            CharSequence charSequence = "";
            int i6 = 8;
            if (editTextBoldCursorArr != null && editTextBoldCursorArr.length == this.length) {
                int i7 = 0;
                while (true) {
                    EditTextBoldCursor[] editTextBoldCursorArr2 = this.codeField;
                    if (i7 >= editTextBoldCursorArr2.length) {
                        break;
                    }
                    editTextBoldCursorArr2[i7].setText(charSequence);
                    i7++;
                }
            } else {
                this.codeField = new EditTextBoldCursor[this.length];
                int i8 = 0;
                while (i8 < this.length) {
                    this.codeField[i8] = new EditTextBoldCursor(getContext());
                    this.codeField[i8].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.codeField[i8].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.codeField[i8].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.codeField[i8].setCursorWidth(1.5f);
                    Drawable mutate = getResources().getDrawable(2131166128).mutate();
                    mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteInputFieldActivated"), PorterDuff.Mode.MULTIPLY));
                    this.codeField[i8].setBackgroundDrawable(mutate);
                    this.codeField[i8].setImeOptions(268435461);
                    this.codeField[i8].setTextSize(1, 20.0f);
                    this.codeField[i8].setMaxLines(1);
                    this.codeField[i8].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    this.codeField[i8].setPadding(0, 0, 0, 0);
                    this.codeField[i8].setGravity(49);
                    if (this.verificationType == 3) {
                        this.codeField[i8].setEnabled(false);
                        this.codeField[i8].setInputType(0);
                        this.codeField[i8].setVisibility(8);
                    } else {
                        this.codeField[i8].setInputType(3);
                    }
                    this.codeFieldContainer.addView(this.codeField[i8], LayoutHelper.createLinear(34, 36, 1, 0, 0, i8 != this.length - 1 ? 7 : 0, 0));
                    this.codeField[i8].addTextChangedListener(new AnonymousClass3(i8));
                    this.codeField[i8].setOnKeyListener(new PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda3(this, i8));
                    this.codeField[i8].setOnEditorActionListener(new PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda4(this));
                    i8++;
                }
            }
            ProgressView progressView = this.progressView;
            if (progressView != null) {
                progressView.setVisibility(this.nextType != 0 ? 0 : 8);
            }
            if (this.phone == null) {
                return;
            }
            PhoneFormat phoneFormat = PhoneFormat.getInstance();
            String format = phoneFormat.format("+" + this.phone);
            int i9 = this.verificationType;
            if (i9 == 2) {
                charSequence = AndroidUtilities.replaceTags(LocaleController.formatString("SentSmsCode", 2131628289, LocaleController.addNbsp(format)));
            } else if (i9 == 3) {
                charSequence = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallCode", 2131628287, LocaleController.addNbsp(format)));
            } else if (i9 == 4) {
                charSequence = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallOnly", 2131628288, LocaleController.addNbsp(format)));
            }
            this.confirmTextView.setText(charSequence);
            if (this.verificationType != 3) {
                AndroidUtilities.showKeyboard(this.codeField[0]);
                this.codeField[0].requestFocus();
            } else {
                AndroidUtilities.hideKeyboard(this.codeField[0]);
            }
            destroyTimer();
            destroyCodeTimer();
            this.lastCurrentTime = System.currentTimeMillis();
            int i10 = this.verificationType;
            if (i10 == 3 && ((i2 = this.nextType) == 4 || i2 == 2)) {
                this.problemText.setVisibility(8);
                this.timeText.setVisibility(0);
                int i11 = this.nextType;
                if (i11 == 4) {
                    this.timeText.setText(LocaleController.formatString("CallText", 2131624816, 1, 0));
                } else if (i11 == 2) {
                    this.timeText.setText(LocaleController.formatString("SmsText", 2131628440, 1, 0));
                }
                createTimer();
            } else if (i10 == 2 && ((i = this.nextType) == 4 || i == 3)) {
                this.timeText.setText(LocaleController.formatString("CallText", 2131624816, 2, 0));
                this.problemText.setVisibility(this.time < 1000 ? 0 : 8);
                TextView textView = this.timeText;
                if (this.time >= 1000) {
                    i6 = 0;
                }
                textView.setVisibility(i6);
                createTimer();
            } else if (i10 == 4 && this.nextType == 2) {
                this.timeText.setText(LocaleController.formatString("SmsText", 2131628440, 2, 0));
                this.problemText.setVisibility(this.time < 1000 ? 0 : 8);
                TextView textView2 = this.timeText;
                if (this.time >= 1000) {
                    i6 = 0;
                }
                textView2.setVisibility(i6);
                createTimer();
            } else {
                this.timeText.setVisibility(8);
                this.problemText.setVisibility(8);
                createCodeTimer();
            }
        }

        /* renamed from: org.telegram.ui.PassportActivity$PhoneConfirmationView$3 */
        /* loaded from: classes3.dex */
        class AnonymousClass3 implements TextWatcher {
            final /* synthetic */ int val$num;

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass3(int i) {
                PhoneConfirmationView.this = r1;
                this.val$num = i;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                int length;
                if (!PhoneConfirmationView.this.ignoreOnTextChange && (length = editable.length()) >= 1) {
                    if (length > 1) {
                        String obj = editable.toString();
                        PhoneConfirmationView.this.ignoreOnTextChange = true;
                        for (int i = 0; i < Math.min(PhoneConfirmationView.this.length - this.val$num, length); i++) {
                            if (i != 0) {
                                PhoneConfirmationView.this.codeField[this.val$num + i].setText(obj.substring(i, i + 1));
                            } else {
                                editable.replace(0, length, obj.substring(i, i + 1));
                            }
                        }
                        PhoneConfirmationView.this.ignoreOnTextChange = false;
                    }
                    if (this.val$num != PhoneConfirmationView.this.length - 1) {
                        PhoneConfirmationView.this.codeField[this.val$num + 1].setSelection(PhoneConfirmationView.this.codeField[this.val$num + 1].length());
                        PhoneConfirmationView.this.codeField[this.val$num + 1].requestFocus();
                    }
                    if ((this.val$num != PhoneConfirmationView.this.length - 1 && (this.val$num != PhoneConfirmationView.this.length - 2 || length < 2)) || PhoneConfirmationView.this.getCode().length() != PhoneConfirmationView.this.length) {
                        return;
                    }
                    PhoneConfirmationView.this.onNextPressed(null);
                }
            }
        }

        public /* synthetic */ boolean lambda$setParams$4(int i, View view, int i2, KeyEvent keyEvent) {
            if (i2 == 67 && this.codeField[i].length() == 0 && i > 0) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
                int i3 = i - 1;
                editTextBoldCursorArr[i3].setSelection(editTextBoldCursorArr[i3].length());
                this.codeField[i3].requestFocus();
                this.codeField[i3].dispatchKeyEvent(keyEvent);
                return true;
            }
            return false;
        }

        public /* synthetic */ boolean lambda$setParams$5(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 5) {
                onNextPressed(null);
                return true;
            }
            return false;
        }

        public void createCodeTimer() {
            if (this.codeTimer != null) {
                return;
            }
            this.codeTime = 15000;
            this.codeTimer = new Timer();
            this.lastCodeTime = System.currentTimeMillis();
            this.codeTimer.schedule(new AnonymousClass4(), 0L, 1000L);
        }

        /* renamed from: org.telegram.ui.PassportActivity$PhoneConfirmationView$4 */
        /* loaded from: classes3.dex */
        public class AnonymousClass4 extends TimerTask {
            AnonymousClass4() {
                PhoneConfirmationView.this = r1;
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                AndroidUtilities.runOnUIThread(new PassportActivity$PhoneConfirmationView$4$$ExternalSyntheticLambda0(this));
            }

            public /* synthetic */ void lambda$run$0() {
                double currentTimeMillis = System.currentTimeMillis();
                double d = PhoneConfirmationView.this.lastCodeTime;
                Double.isNaN(currentTimeMillis);
                PhoneConfirmationView.this.lastCodeTime = currentTimeMillis;
                PhoneConfirmationView.access$10026(PhoneConfirmationView.this, currentTimeMillis - d);
                if (PhoneConfirmationView.this.codeTime <= 1000) {
                    PhoneConfirmationView.this.problemText.setVisibility(0);
                    PhoneConfirmationView.this.timeText.setVisibility(8);
                    PhoneConfirmationView.this.destroyCodeTimer();
                }
            }
        }

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

        private void createTimer() {
            if (this.timeTimer != null) {
                return;
            }
            Timer timer = new Timer();
            this.timeTimer = timer;
            timer.schedule(new AnonymousClass5(), 0L, 1000L);
        }

        /* renamed from: org.telegram.ui.PassportActivity$PhoneConfirmationView$5 */
        /* loaded from: classes3.dex */
        public class AnonymousClass5 extends TimerTask {
            AnonymousClass5() {
                PhoneConfirmationView.this = r1;
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (PhoneConfirmationView.this.timeTimer == null) {
                    return;
                }
                double currentTimeMillis = System.currentTimeMillis();
                double d = PhoneConfirmationView.this.lastCurrentTime;
                Double.isNaN(currentTimeMillis);
                PhoneConfirmationView.access$10626(PhoneConfirmationView.this, currentTimeMillis - d);
                PhoneConfirmationView.this.lastCurrentTime = currentTimeMillis;
                AndroidUtilities.runOnUIThread(new PassportActivity$PhoneConfirmationView$5$$ExternalSyntheticLambda0(this));
            }

            public /* synthetic */ void lambda$run$2() {
                if (PhoneConfirmationView.this.time >= 1000) {
                    int i = (PhoneConfirmationView.this.time / 1000) / 60;
                    int i2 = (PhoneConfirmationView.this.time / 1000) - (i * 60);
                    if (PhoneConfirmationView.this.nextType == 4 || PhoneConfirmationView.this.nextType == 3) {
                        PhoneConfirmationView.this.timeText.setText(LocaleController.formatString("CallText", 2131624816, Integer.valueOf(i), Integer.valueOf(i2)));
                    } else if (PhoneConfirmationView.this.nextType == 2) {
                        PhoneConfirmationView.this.timeText.setText(LocaleController.formatString("SmsText", 2131628440, Integer.valueOf(i), Integer.valueOf(i2)));
                    }
                    if (PhoneConfirmationView.this.progressView == null) {
                        return;
                    }
                    PhoneConfirmationView.this.progressView.setProgress(1.0f - (PhoneConfirmationView.this.time / PhoneConfirmationView.this.timeout));
                    return;
                }
                if (PhoneConfirmationView.this.progressView != null) {
                    PhoneConfirmationView.this.progressView.setProgress(1.0f);
                }
                PhoneConfirmationView.this.destroyTimer();
                if (PhoneConfirmationView.this.verificationType != 3) {
                    if (PhoneConfirmationView.this.verificationType != 2 && PhoneConfirmationView.this.verificationType != 4) {
                        return;
                    }
                    if (PhoneConfirmationView.this.nextType == 4 || PhoneConfirmationView.this.nextType == 2) {
                        if (PhoneConfirmationView.this.nextType == 4) {
                            PhoneConfirmationView.this.timeText.setText(LocaleController.getString("Calling", 2131624821));
                        } else {
                            PhoneConfirmationView.this.timeText.setText(LocaleController.getString("SendingSms", 2131628281));
                        }
                        PhoneConfirmationView.this.createCodeTimer();
                        TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode = new TLRPC$TL_auth_resendCode();
                        tLRPC$TL_auth_resendCode.phone_number = PhoneConfirmationView.this.phone;
                        tLRPC$TL_auth_resendCode.phone_code_hash = PhoneConfirmationView.this.phoneHash;
                        ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_resendCode, new PassportActivity$PhoneConfirmationView$5$$ExternalSyntheticLambda2(this), 2);
                        return;
                    } else if (PhoneConfirmationView.this.nextType != 3) {
                        return;
                    } else {
                        AndroidUtilities.setWaitingForSms(false);
                        NotificationCenter.getGlobalInstance().removeObserver(PhoneConfirmationView.this, NotificationCenter.didReceiveSmsCode);
                        PhoneConfirmationView.this.waitingForEvent = false;
                        PhoneConfirmationView.this.destroyCodeTimer();
                        PhoneConfirmationView.this.resendCode();
                        return;
                    }
                }
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(PhoneConfirmationView.this, NotificationCenter.didReceiveCall);
                PhoneConfirmationView.this.waitingForEvent = false;
                PhoneConfirmationView.this.destroyCodeTimer();
                PhoneConfirmationView.this.resendCode();
            }

            public /* synthetic */ void lambda$run$1(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                if (tLRPC$TL_error == null || tLRPC$TL_error.text == null) {
                    return;
                }
                AndroidUtilities.runOnUIThread(new PassportActivity$PhoneConfirmationView$5$$ExternalSyntheticLambda1(this, tLRPC$TL_error));
            }

            public /* synthetic */ void lambda$run$0(TLRPC$TL_error tLRPC$TL_error) {
                PhoneConfirmationView.this.lastError = tLRPC$TL_error.text;
            }
        }

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

        public String getCode() {
            if (this.codeField == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (true) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
                if (i < editTextBoldCursorArr.length) {
                    sb.append(PhoneFormat.stripExceptNumbers(editTextBoldCursorArr[i].getText().toString()));
                    i++;
                } else {
                    return sb.toString();
                }
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onNextPressed(String str) {
            if (this.nextPressed) {
                return;
            }
            if (str == null) {
                str = getCode();
            }
            if (TextUtils.isEmpty(str)) {
                AndroidUtilities.shakeView(this.codeFieldContainer, 2.0f, 0);
                return;
            }
            this.nextPressed = true;
            int i = this.verificationType;
            if (i == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            PassportActivity.this.showEditDoneProgress(true, true);
            TLRPC$TL_account_verifyPhone tLRPC$TL_account_verifyPhone = new TLRPC$TL_account_verifyPhone();
            tLRPC$TL_account_verifyPhone.phone_number = this.phone;
            tLRPC$TL_account_verifyPhone.phone_code = str;
            tLRPC$TL_account_verifyPhone.phone_code_hash = this.phoneHash;
            destroyTimer();
            PassportActivity.this.needShowProgress();
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_account_verifyPhone, new PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda9(this, tLRPC$TL_account_verifyPhone), 2);
        }

        public /* synthetic */ void lambda$onNextPressed$7(TLRPC$TL_account_verifyPhone tLRPC$TL_account_verifyPhone, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda6(this, tLRPC$TL_error, tLRPC$TL_account_verifyPhone));
        }

        public /* synthetic */ void lambda$onNextPressed$6(TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_account_verifyPhone tLRPC$TL_account_verifyPhone) {
            int i;
            int i2;
            PassportActivity.this.needHideProgress();
            this.nextPressed = false;
            if (tLRPC$TL_error == null) {
                destroyTimer();
                destroyCodeTimer();
                PassportActivity.this.delegate.saveValue(PassportActivity.this.currentType, (String) PassportActivity.this.currentValues.get("phone"), null, null, null, null, null, null, null, null, new PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda7(PassportActivity.this), null);
                return;
            }
            this.lastError = tLRPC$TL_error.text;
            int i3 = this.verificationType;
            if ((i3 == 3 && ((i2 = this.nextType) == 4 || i2 == 2)) || ((i3 == 2 && ((i = this.nextType) == 4 || i == 3)) || (i3 == 4 && this.nextType == 2))) {
                createTimer();
            }
            int i4 = this.verificationType;
            if (i4 == 2) {
                AndroidUtilities.setWaitingForSms(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i4 == 3) {
                AndroidUtilities.setWaitingForCall(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = true;
            if (this.verificationType != 3) {
                AlertsCreator.processError(((BaseFragment) PassportActivity.this).currentAccount, tLRPC$TL_error, PassportActivity.this, tLRPC$TL_account_verifyPhone, new Object[0]);
            }
            PassportActivity.this.showEditDoneProgress(true, false);
            if (!tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") && !tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                if (!tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                    return;
                }
                onBackPressed(true);
                PassportActivity.this.setPage(0, true, null);
                return;
            }
            int i5 = 0;
            while (true) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
                if (i5 < editTextBoldCursorArr.length) {
                    editTextBoldCursorArr[i5].setText("");
                    i5++;
                } else {
                    editTextBoldCursorArr[0].requestFocus();
                    return;
                }
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean onBackPressed(boolean z) {
            if (!z) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PassportActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString("AppName", 2131624384));
                builder.setMessage(LocaleController.getString("StopVerification", 2131628543));
                builder.setPositiveButton(LocaleController.getString("Continue", 2131625262), null);
                builder.setNegativeButton(LocaleController.getString("Stop", 2131628527), new PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda1(this));
                PassportActivity.this.showDialog(builder.create());
                return false;
            }
            TLRPC$TL_auth_cancelCode tLRPC$TL_auth_cancelCode = new TLRPC$TL_auth_cancelCode();
            tLRPC$TL_auth_cancelCode.phone_number = this.phone;
            tLRPC$TL_auth_cancelCode.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(((BaseFragment) PassportActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_cancelCode, PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda10.INSTANCE, 2);
            destroyTimer();
            destroyCodeTimer();
            int i = this.verificationType;
            if (i == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            return true;
        }

        public /* synthetic */ void lambda$onBackPressed$8(DialogInterface dialogInterface, int i) {
            onBackPressed(true);
            PassportActivity.this.setPage(0, true, null);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onDestroyActivity() {
            super.onDestroyActivity();
            int i = this.verificationType;
            if (i == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            destroyTimer();
            destroyCodeTimer();
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
                    EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
                    editTextBoldCursorArr[length].setSelection(editTextBoldCursorArr[length].length());
                    AndroidUtilities.showKeyboard(this.codeField[length]);
                    return;
                }
            }
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            EditTextBoldCursor[] editTextBoldCursorArr;
            if (!this.waitingForEvent || (editTextBoldCursorArr = this.codeField) == null) {
                return;
            }
            if (i == NotificationCenter.didReceiveSmsCode) {
                editTextBoldCursorArr[0].setText("" + objArr[0]);
                onNextPressed(null);
            } else if (i != NotificationCenter.didReceiveCall) {
            } else {
                String str = "" + objArr[0];
                if (!AndroidUtilities.checkPhonePattern(this.pattern, str)) {
                    return;
                }
                this.ignoreOnTextChange = true;
                this.codeField[0].setText(str);
                this.ignoreOnTextChange = false;
                onNextPressed(null);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.extraBackgroundView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        if (this.extraBackgroundView2 != null) {
            arrayList.add(new ThemeDescription(this.extraBackgroundView2, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        }
        for (int i = 0; i < this.dividers.size(); i++) {
            arrayList.add(new ThemeDescription(this.dividers.get(i), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "divider"));
        }
        for (Map.Entry<SecureDocument, SecureDocumentCell> entry : this.documentsCells.entrySet()) {
            SecureDocumentCell value = entry.getValue();
            arrayList.add(new ThemeDescription(value, ThemeDescription.FLAG_SELECTORWHITE, new Class[]{SecureDocumentCell.class}, null, null, null, "windowBackgroundWhite"));
            arrayList.add(new ThemeDescription(value, 0, new Class[]{SecureDocumentCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
            arrayList.add(new ThemeDescription(value, 0, new Class[]{SecureDocumentCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        }
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_SELECTORWHITE, new Class[]{TextDetailSettingsCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_SELECTORWHITE, new Class[]{TextSettingsCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteValueText"));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_SELECTORWHITE, new Class[]{TextDetailSecureCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextDetailSecureCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextDetailSecureCell.class}, null, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextDetailSecureCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{TextDetailSecureCell.class}, new String[]{"checkImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_addedIcon"));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        if (this.inputFields != null) {
            for (int i2 = 0; i2 < this.inputFields.length; i2++) {
                arrayList.add(new ThemeDescription((View) this.inputFields[i2].getParent(), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
                arrayList.add(new ThemeDescription(this.inputFields[i2], ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                arrayList.add(new ThemeDescription(this.inputFields[i2], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
                arrayList.add(new ThemeDescription(this.inputFields[i2], ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "windowBackgroundWhiteBlueHeader"));
                arrayList.add(new ThemeDescription(this.inputFields[i2], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
                arrayList.add(new ThemeDescription(this.inputFields[i2], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                arrayList.add(new ThemeDescription(this.inputFields[i2], ThemeDescription.FLAG_PROGRESSBAR | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteRedText3"));
            }
        } else {
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_PROGRESSBAR | ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueHeader"));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "windowBackgroundWhiteRedText3"));
        }
        if (this.inputExtraFields != null) {
            for (int i3 = 0; i3 < this.inputExtraFields.length; i3++) {
                arrayList.add(new ThemeDescription((View) this.inputExtraFields[i3].getParent(), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
                arrayList.add(new ThemeDescription(this.inputExtraFields[i3], ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                arrayList.add(new ThemeDescription(this.inputExtraFields[i3], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
                arrayList.add(new ThemeDescription(this.inputExtraFields[i3], ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "windowBackgroundWhiteBlueHeader"));
                arrayList.add(new ThemeDescription(this.inputExtraFields[i3], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
                arrayList.add(new ThemeDescription(this.inputExtraFields[i3], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                arrayList.add(new ThemeDescription(this.inputExtraFields[i3], ThemeDescription.FLAG_PROGRESSBAR | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteRedText3"));
            }
        }
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"));
        arrayList.add(new ThemeDescription(this.noPasswordImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chat_messagePanelIcons"));
        arrayList.add(new ThemeDescription(this.noPasswordTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.noPasswordSetTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText5"));
        arrayList.add(new ThemeDescription(this.passwordForgotButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
        arrayList.add(new ThemeDescription(this.plusTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.acceptTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "passport_authorizeText"));
        arrayList.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "passport_authorizeBackground"));
        arrayList.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "passport_authorizeBackgroundSelected"));
        arrayList.add(new ThemeDescription(this.progressView, 0, null, null, null, null, "contextProgressInner2"));
        arrayList.add(new ThemeDescription(this.progressView, 0, null, null, null, null, "contextProgressOuter2"));
        arrayList.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, "contextProgressInner2"));
        arrayList.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, "contextProgressOuter2"));
        arrayList.add(new ThemeDescription(this.emptyImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "sessions_devicesImage"));
        arrayList.add(new ThemeDescription(this.emptyTextView1, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.emptyTextView2, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.emptyTextView3, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
        return arrayList;
    }
}
