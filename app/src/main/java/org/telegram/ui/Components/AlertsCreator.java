package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.util.Consumer;
import java.net.IDN;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.OneUIUtilities;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$ChannelLocation;
import org.telegram.tgnet.TLRPC$ChannelParticipant;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$TL_account_changePhone;
import org.telegram.tgnet.TLRPC$TL_account_confirmPhone;
import org.telegram.tgnet.TLRPC$TL_account_getAuthorizationForm;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_getTmpPassword;
import org.telegram.tgnet.TLRPC$TL_account_reportPeer;
import org.telegram.tgnet.TLRPC$TL_account_saveSecureValue;
import org.telegram.tgnet.TLRPC$TL_account_sendChangePhoneCode;
import org.telegram.tgnet.TLRPC$TL_account_sendConfirmPhoneCode;
import org.telegram.tgnet.TLRPC$TL_account_updateProfile;
import org.telegram.tgnet.TLRPC$TL_account_verifyEmail;
import org.telegram.tgnet.TLRPC$TL_account_verifyPhone;
import org.telegram.tgnet.TLRPC$TL_auth_resendCode;
import org.telegram.tgnet.TLRPC$TL_channelLocation;
import org.telegram.tgnet.TLRPC$TL_channelParticipantAdmin;
import org.telegram.tgnet.TLRPC$TL_channelParticipantCreator;
import org.telegram.tgnet.TLRPC$TL_channels_channelParticipant;
import org.telegram.tgnet.TLRPC$TL_channels_createChannel;
import org.telegram.tgnet.TLRPC$TL_channels_editAdmin;
import org.telegram.tgnet.TLRPC$TL_channels_editBanned;
import org.telegram.tgnet.TLRPC$TL_channels_getParticipant;
import org.telegram.tgnet.TLRPC$TL_channels_inviteToChannel;
import org.telegram.tgnet.TLRPC$TL_channels_joinChannel;
import org.telegram.tgnet.TLRPC$TL_channels_reportSpam;
import org.telegram.tgnet.TLRPC$TL_contacts_blockFromReplies;
import org.telegram.tgnet.TLRPC$TL_contacts_importContacts;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_help_getSupport;
import org.telegram.tgnet.TLRPC$TL_help_support;
import org.telegram.tgnet.TLRPC$TL_inputPeerUser;
import org.telegram.tgnet.TLRPC$TL_inputReportReasonChildAbuse;
import org.telegram.tgnet.TLRPC$TL_inputReportReasonFake;
import org.telegram.tgnet.TLRPC$TL_inputReportReasonIllegalDrugs;
import org.telegram.tgnet.TLRPC$TL_inputReportReasonOther;
import org.telegram.tgnet.TLRPC$TL_inputReportReasonPersonalDetails;
import org.telegram.tgnet.TLRPC$TL_inputReportReasonPornography;
import org.telegram.tgnet.TLRPC$TL_inputReportReasonSpam;
import org.telegram.tgnet.TLRPC$TL_inputReportReasonViolence;
import org.telegram.tgnet.TLRPC$TL_langPackLanguage;
import org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser;
import org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser;
import org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink;
import org.telegram.tgnet.TLRPC$TL_messageActionContactSignUp;
import org.telegram.tgnet.TLRPC$TL_messageActionEmpty;
import org.telegram.tgnet.TLRPC$TL_messageActionGeoProximityReached;
import org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall;
import org.telegram.tgnet.TLRPC$TL_messageActionPinMessage;
import org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme;
import org.telegram.tgnet.TLRPC$TL_messageActionUserJoined;
import org.telegram.tgnet.TLRPC$TL_messages_addChatUser;
import org.telegram.tgnet.TLRPC$TL_messages_checkHistoryImport;
import org.telegram.tgnet.TLRPC$TL_messages_checkHistoryImportPeer;
import org.telegram.tgnet.TLRPC$TL_messages_createChat;
import org.telegram.tgnet.TLRPC$TL_messages_editChatAdmin;
import org.telegram.tgnet.TLRPC$TL_messages_editChatDefaultBannedRights;
import org.telegram.tgnet.TLRPC$TL_messages_editMessage;
import org.telegram.tgnet.TLRPC$TL_messages_forwardMessages;
import org.telegram.tgnet.TLRPC$TL_messages_getAttachedStickers;
import org.telegram.tgnet.TLRPC$TL_messages_importChatInvite;
import org.telegram.tgnet.TLRPC$TL_messages_initHistoryImport;
import org.telegram.tgnet.TLRPC$TL_messages_migrateChat;
import org.telegram.tgnet.TLRPC$TL_messages_report;
import org.telegram.tgnet.TLRPC$TL_messages_sendInlineBotResult;
import org.telegram.tgnet.TLRPC$TL_messages_sendMedia;
import org.telegram.tgnet.TLRPC$TL_messages_sendMessage;
import org.telegram.tgnet.TLRPC$TL_messages_sendMultiMedia;
import org.telegram.tgnet.TLRPC$TL_messages_sendScheduledMessages;
import org.telegram.tgnet.TLRPC$TL_messages_startBot;
import org.telegram.tgnet.TLRPC$TL_messages_startHistoryImport;
import org.telegram.tgnet.TLRPC$TL_payments_sendPaymentForm;
import org.telegram.tgnet.TLRPC$TL_payments_validateRequestedInfo;
import org.telegram.tgnet.TLRPC$TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC$TL_phone_inviteToGroupCall;
import org.telegram.tgnet.TLRPC$TL_updateUserName;
import org.telegram.tgnet.TLRPC$Updates;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.CacheControlActivity;
import org.telegram.ui.Cells.AccountSelectCell;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.RadioColorCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.LanguageSelectActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.LoginActivity;
import org.telegram.ui.NotificationsCustomSettingsActivity;
import org.telegram.ui.NotificationsSettingsActivity;
import org.telegram.ui.ProfileNotificationsActivity;
import org.telegram.ui.ThemePreviewActivity;
import org.telegram.ui.TooManyCommunitiesActivity;
/* loaded from: classes3.dex */
public class AlertsCreator {

    /* loaded from: classes3.dex */
    public interface AccountSelectDelegate {
        void didSelectAccount(int i);
    }

    /* loaded from: classes3.dex */
    public interface BlockDialogCallback {
        void run(boolean z, boolean z2);
    }

    /* loaded from: classes3.dex */
    public interface DatePickerDelegate {
        void didSelectDate(int i, int i2, int i3);
    }

    /* loaded from: classes3.dex */
    public interface ScheduleDatePickerDelegate {
        void didSelectDate(boolean z, int i);
    }

    /* loaded from: classes3.dex */
    public interface SoundFrequencyDelegate {
        void didSelectValues(int i, int i2);
    }

    public static /* synthetic */ boolean lambda$createAutoDeleteDatePickerDialog$63(View view, MotionEvent motionEvent) {
        return true;
    }

    public static /* synthetic */ boolean lambda$createCalendarPickerDialog$76(View view, MotionEvent motionEvent) {
        return true;
    }

    public static /* synthetic */ void lambda$createChangeBioAlert$29(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public static /* synthetic */ void lambda$createChangeNameAlert$33(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public static /* synthetic */ boolean lambda$createDatePickerDialog$56(View view, MotionEvent motionEvent) {
        return true;
    }

    public static /* synthetic */ void lambda$createDeleteMessagesAlert$117(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public static /* synthetic */ boolean lambda$createMuteForPickerDialog$73(View view, MotionEvent motionEvent) {
        return true;
    }

    public static /* synthetic */ void lambda$createReportAlert$85(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public static /* synthetic */ boolean lambda$createScheduleDatePickerDialog$47(View view, MotionEvent motionEvent) {
        return true;
    }

    public static /* synthetic */ boolean lambda$createSoundFrequencyPickerDialog$69(View view, MotionEvent motionEvent) {
        return true;
    }

    public static /* synthetic */ void lambda$createThemeCreateDialog$120(DialogInterface dialogInterface, int i) {
    }

    public static /* synthetic */ void lambda$sendReport$83(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public static Dialog createForgotPasscodeDialog(Context context) {
        return new AlertDialog.Builder(context).setTitle(LocaleController.getString(2131625978)).setMessage(LocaleController.getString(2131625979)).setPositiveButton(LocaleController.getString(2131625183), null).create();
    }

    public static Dialog createLocationRequiredDialog(Context context, boolean z) {
        String str;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (z) {
            str = LocaleController.getString("PermissionNoLocationFriends", 2131627530);
        } else {
            str = LocaleController.getString("PermissionNoLocationPeopleNearby", 2131627532);
        }
        return builder.setMessage(AndroidUtilities.replaceTags(str)).setTopAnimation(2131558509, 72, false, Theme.getColor("dialogTopBackground")).setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131627535), new AlertsCreator$$ExternalSyntheticLambda8(context)).setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", 2131625261), null).create();
    }

    public static /* synthetic */ void lambda$createLocationRequiredDialog$0(Context context, DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static Dialog createBackgroundActivityDialog(Context context) {
        return new AlertDialog.Builder(context).setTitle(LocaleController.getString(2131624332)).setMessage(AndroidUtilities.replaceTags(LocaleController.getString(OneUIUtilities.isOneUI() ? Build.VERSION.SDK_INT >= 31 ? 2131624334 : 2131624335 : 2131624333))).setTopAnimation(2131558505, 72, false, Theme.getColor("dialogTopBackground")).setPositiveButton(LocaleController.getString(2131627535), new AlertsCreator$$ExternalSyntheticLambda9(context)).setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", 2131625261), null).create();
    }

    public static /* synthetic */ void lambda$createBackgroundActivityDialog$1(Context context, DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static Dialog createWebViewPermissionsRequestDialog(Context context, Theme.ResourcesProvider resourcesProvider, String[] strArr, int i, String str, String str2, Consumer<Boolean> consumer) {
        boolean z;
        if (strArr != null && (context instanceof Activity) && Build.VERSION.SDK_INT >= 23) {
            Activity activity = (Activity) context;
            for (String str3 : strArr) {
                if (activity.checkSelfPermission(str3) != 0 && activity.shouldShowRequestPermissionRationale(str3)) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        AlertDialog.Builder topAnimation = new AlertDialog.Builder(context, resourcesProvider).setTopAnimation(i, 72, false, Theme.getColor("dialogTopBackground"));
        if (z) {
            str = str2;
        }
        return topAnimation.setMessage(AndroidUtilities.replaceTags(str)).setPositiveButton(LocaleController.getString(z ? 2131627535 : 2131624758), new AlertsCreator$$ExternalSyntheticLambda32(z, context, atomicBoolean, consumer)).setNegativeButton(LocaleController.getString(2131624761), new AlertsCreator$$ExternalSyntheticLambda16(atomicBoolean, consumer)).setOnDismissListener(new AlertsCreator$$ExternalSyntheticLambda45(atomicBoolean, consumer)).create();
    }

    public static /* synthetic */ void lambda$createWebViewPermissionsRequestDialog$2(boolean z, Context context, AtomicBoolean atomicBoolean, Consumer consumer, DialogInterface dialogInterface, int i) {
        if (z) {
            try {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
                context.startActivity(intent);
                return;
            } catch (Exception e) {
                FileLog.e(e);
                return;
            }
        }
        atomicBoolean.set(true);
        consumer.accept(Boolean.TRUE);
    }

    public static /* synthetic */ void lambda$createWebViewPermissionsRequestDialog$3(AtomicBoolean atomicBoolean, Consumer consumer, DialogInterface dialogInterface, int i) {
        atomicBoolean.set(true);
        consumer.accept(Boolean.FALSE);
    }

    public static /* synthetic */ void lambda$createWebViewPermissionsRequestDialog$4(AtomicBoolean atomicBoolean, Consumer consumer, DialogInterface dialogInterface) {
        if (!atomicBoolean.get()) {
            consumer.accept(Boolean.FALSE);
        }
    }

    public static Dialog createApkRestrictedDialog(Context context, Theme.ResourcesProvider resourcesProvider) {
        return new AlertDialog.Builder(context, resourcesProvider).setMessage(LocaleController.getString("ApkRestricted", 2131624374)).setTopAnimation(2131558505, 72, false, Theme.getColor("dialogTopBackground")).setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131627535), new AlertsCreator$$ExternalSyntheticLambda10(context)).setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", 2131625261), null).create();
    }

    public static /* synthetic */ void lambda$createApkRestrictedDialog$5(Context context, DialogInterface dialogInterface, int i) {
        try {
            context.startActivity(new Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES", Uri.parse("package:" + context.getPackageName())));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static Dialog processError(int i, TLRPC$TL_error tLRPC$TL_error, BaseFragment baseFragment, TLObject tLObject, Object... objArr) {
        String str;
        TLRPC$InputPeer tLRPC$InputPeer;
        int i2 = tLRPC$TL_error.code;
        if (i2 == 406 || (str = tLRPC$TL_error.text) == null) {
            return null;
        }
        boolean z = tLObject instanceof TLRPC$TL_messages_initHistoryImport;
        if (z || (tLObject instanceof TLRPC$TL_messages_checkHistoryImportPeer) || (tLObject instanceof TLRPC$TL_messages_checkHistoryImport) || (tLObject instanceof TLRPC$TL_messages_startHistoryImport)) {
            if (z) {
                tLRPC$InputPeer = ((TLRPC$TL_messages_initHistoryImport) tLObject).peer;
            } else {
                tLRPC$InputPeer = tLObject instanceof TLRPC$TL_messages_startHistoryImport ? ((TLRPC$TL_messages_startHistoryImport) tLObject).peer : null;
            }
            if (str.contains("USER_IS_BLOCKED")) {
                showSimpleAlert(baseFragment, LocaleController.getString("ImportErrorTitle", 2131626234), LocaleController.getString("ImportErrorUserBlocked", 2131626235));
            } else if (tLRPC$TL_error.text.contains("USER_NOT_MUTUAL_CONTACT")) {
                showSimpleAlert(baseFragment, LocaleController.getString("ImportErrorTitle", 2131626234), LocaleController.getString("ImportMutualError", 2131626243));
            } else if (tLRPC$TL_error.text.contains("IMPORT_PEER_TYPE_INVALID")) {
                if (tLRPC$InputPeer instanceof TLRPC$TL_inputPeerUser) {
                    showSimpleAlert(baseFragment, LocaleController.getString("ImportErrorTitle", 2131626234), LocaleController.getString("ImportErrorChatInvalidUser", 2131626229));
                } else {
                    showSimpleAlert(baseFragment, LocaleController.getString("ImportErrorTitle", 2131626234), LocaleController.getString("ImportErrorChatInvalidGroup", 2131626228));
                }
            } else if (tLRPC$TL_error.text.contains("CHAT_ADMIN_REQUIRED")) {
                showSimpleAlert(baseFragment, LocaleController.getString("ImportErrorTitle", 2131626234), LocaleController.getString("ImportErrorNotAdmin", 2131626232));
            } else if (tLRPC$TL_error.text.startsWith("IMPORT_FORMAT")) {
                showSimpleAlert(baseFragment, LocaleController.getString("ImportErrorTitle", 2131626234), LocaleController.getString("ImportErrorFileFormatInvalid", 2131626230));
            } else if (tLRPC$TL_error.text.startsWith("PEER_ID_INVALID")) {
                showSimpleAlert(baseFragment, LocaleController.getString("ImportErrorTitle", 2131626234), LocaleController.getString("ImportErrorPeerInvalid", 2131626233));
            } else if (tLRPC$TL_error.text.contains("IMPORT_LANG_NOT_FOUND")) {
                showSimpleAlert(baseFragment, LocaleController.getString("ImportErrorTitle", 2131626234), LocaleController.getString("ImportErrorFileLang", 2131626231));
            } else if (tLRPC$TL_error.text.contains("IMPORT_UPLOAD_FAILED")) {
                showSimpleAlert(baseFragment, LocaleController.getString("ImportErrorTitle", 2131626234), LocaleController.getString("ImportFailedToUpload", 2131626236));
            } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                showFloodWaitAlert(tLRPC$TL_error.text, baseFragment);
            } else {
                String string = LocaleController.getString("ImportErrorTitle", 2131626234);
                showSimpleAlert(baseFragment, string, LocaleController.getString("ErrorOccurred", 2131625695) + "\n" + tLRPC$TL_error.text);
            }
        } else if ((tLObject instanceof TLRPC$TL_account_saveSecureValue) || (tLObject instanceof TLRPC$TL_account_getAuthorizationForm)) {
            if (str.contains("PHONE_NUMBER_INVALID")) {
                showSimpleAlert(baseFragment, LocaleController.getString("InvalidPhoneNumber", 2131626297));
            } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131625949));
            } else if ("APP_VERSION_OUTDATED".equals(tLRPC$TL_error.text)) {
                showUpdateAppAlert(baseFragment.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131628831), true);
            } else {
                showSimpleAlert(baseFragment, LocaleController.getString("ErrorOccurred", 2131625695) + "\n" + tLRPC$TL_error.text);
            }
        } else {
            boolean z2 = tLObject instanceof TLRPC$TL_channels_joinChannel;
            if (z2 || (tLObject instanceof TLRPC$TL_channels_editAdmin) || (tLObject instanceof TLRPC$TL_channels_inviteToChannel) || (tLObject instanceof TLRPC$TL_messages_addChatUser) || (tLObject instanceof TLRPC$TL_messages_startBot) || (tLObject instanceof TLRPC$TL_channels_editBanned) || (tLObject instanceof TLRPC$TL_messages_editChatDefaultBannedRights) || (tLObject instanceof TLRPC$TL_messages_editChatAdmin) || (tLObject instanceof TLRPC$TL_messages_migrateChat) || (tLObject instanceof TLRPC$TL_phone_inviteToGroupCall)) {
                if (baseFragment != null && str.equals("CHANNELS_TOO_MUCH")) {
                    if (baseFragment.getParentActivity() != null) {
                        baseFragment.showDialog(new LimitReachedBottomSheet(baseFragment, baseFragment.getParentActivity(), 5, i));
                        return null;
                    } else if (z2 || (tLObject instanceof TLRPC$TL_channels_inviteToChannel)) {
                        baseFragment.presentFragment(new TooManyCommunitiesActivity(0));
                        return null;
                    } else {
                        baseFragment.presentFragment(new TooManyCommunitiesActivity(1));
                        return null;
                    }
                } else if (baseFragment != null) {
                    showAddUserAlert(tLRPC$TL_error.text, baseFragment, (objArr == null || objArr.length <= 0) ? false : ((Boolean) objArr[0]).booleanValue(), tLObject);
                } else if (tLRPC$TL_error.text.equals("PEER_FLOOD")) {
                    NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.needShowAlert, 1);
                }
            } else if (tLObject instanceof TLRPC$TL_messages_createChat) {
                if (str.equals("CHANNELS_TOO_MUCH")) {
                    if (baseFragment.getParentActivity() != null) {
                        baseFragment.showDialog(new LimitReachedBottomSheet(baseFragment, baseFragment.getParentActivity(), 5, i));
                        return null;
                    }
                    baseFragment.presentFragment(new TooManyCommunitiesActivity(2));
                    return null;
                } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                    showFloodWaitAlert(tLRPC$TL_error.text, baseFragment);
                } else {
                    showAddUserAlert(tLRPC$TL_error.text, baseFragment, false, tLObject);
                }
            } else if (tLObject instanceof TLRPC$TL_channels_createChannel) {
                if (str.equals("CHANNELS_TOO_MUCH")) {
                    if (baseFragment.getParentActivity() != null) {
                        baseFragment.showDialog(new LimitReachedBottomSheet(baseFragment, baseFragment.getParentActivity(), 5, i));
                        return null;
                    }
                    baseFragment.presentFragment(new TooManyCommunitiesActivity(2));
                    return null;
                } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                    showFloodWaitAlert(tLRPC$TL_error.text, baseFragment);
                } else {
                    showAddUserAlert(tLRPC$TL_error.text, baseFragment, false, tLObject);
                }
            } else if (tLObject instanceof TLRPC$TL_messages_editMessage) {
                if (!str.equals("MESSAGE_NOT_MODIFIED")) {
                    if (baseFragment != null) {
                        showSimpleAlert(baseFragment, LocaleController.getString("EditMessageError", 2131625596));
                    } else {
                        showSimpleToast(null, LocaleController.getString("EditMessageError", 2131625596));
                        return null;
                    }
                }
            } else if ((tLObject instanceof TLRPC$TL_messages_sendMessage) || (tLObject instanceof TLRPC$TL_messages_sendMedia) || (tLObject instanceof TLRPC$TL_messages_sendInlineBotResult) || (tLObject instanceof TLRPC$TL_messages_forwardMessages) || (tLObject instanceof TLRPC$TL_messages_sendMultiMedia) || (tLObject instanceof TLRPC$TL_messages_sendScheduledMessages)) {
                str.hashCode();
                char c = 65535;
                switch (str.hashCode()) {
                    case -1809401834:
                        if (str.equals("USER_BANNED_IN_CHANNEL")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -454039871:
                        if (str.equals("PEER_FLOOD")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1169786080:
                        if (str.equals("SCHEDULE_TOO_MUCH")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.needShowAlert, 5);
                        break;
                    case 1:
                        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.needShowAlert, 0);
                        break;
                    case 2:
                        showSimpleToast(baseFragment, LocaleController.getString("MessageScheduledLimitReached", 2131626697));
                        break;
                }
            } else if (tLObject instanceof TLRPC$TL_messages_importChatInvite) {
                if (str.startsWith("FLOOD_WAIT")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131625949));
                } else if (tLRPC$TL_error.text.equals("USERS_TOO_MUCH")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("JoinToGroupErrorFull", 2131626371));
                } else if (tLRPC$TL_error.text.equals("CHANNELS_TOO_MUCH")) {
                    if (baseFragment.getParentActivity() != null) {
                        baseFragment.showDialog(new LimitReachedBottomSheet(baseFragment, baseFragment.getParentActivity(), 5, i));
                    } else {
                        baseFragment.presentFragment(new TooManyCommunitiesActivity(0));
                    }
                } else if (tLRPC$TL_error.text.equals("INVITE_HASH_EXPIRED")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("ExpiredLink", 2131625829), LocaleController.getString("InviteExpired", 2131626307));
                } else {
                    showSimpleAlert(baseFragment, LocaleController.getString("JoinToGroupErrorNotExist", 2131626372));
                }
            } else if (tLObject instanceof TLRPC$TL_messages_getAttachedStickers) {
                if (baseFragment != null && baseFragment.getParentActivity() != null) {
                    Activity parentActivity = baseFragment.getParentActivity();
                    Toast.makeText(parentActivity, LocaleController.getString("ErrorOccurred", 2131625695) + "\n" + tLRPC$TL_error.text, 0).show();
                }
            } else if ((tLObject instanceof TLRPC$TL_account_confirmPhone) || (tLObject instanceof TLRPC$TL_account_verifyPhone) || (tLObject instanceof TLRPC$TL_account_verifyEmail)) {
                if (str.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID") || tLRPC$TL_error.text.contains("CODE_INVALID") || tLRPC$TL_error.text.contains("CODE_EMPTY")) {
                    return showSimpleAlert(baseFragment, LocaleController.getString("InvalidCode", 2131626293));
                }
                if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED") || tLRPC$TL_error.text.contains("EMAIL_VERIFY_EXPIRED")) {
                    return showSimpleAlert(baseFragment, LocaleController.getString("CodeExpired", 2131625187));
                }
                if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                    return showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131625949));
                }
                return showSimpleAlert(baseFragment, tLRPC$TL_error.text);
            } else if (tLObject instanceof TLRPC$TL_auth_resendCode) {
                if (str.contains("PHONE_NUMBER_INVALID")) {
                    return showSimpleAlert(baseFragment, LocaleController.getString("InvalidPhoneNumber", 2131626297));
                }
                if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                    return showSimpleAlert(baseFragment, LocaleController.getString("InvalidCode", 2131626293));
                }
                if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                    return showSimpleAlert(baseFragment, LocaleController.getString("CodeExpired", 2131625187));
                }
                if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                    return showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131625949));
                }
                if (tLRPC$TL_error.code != -1000) {
                    return showSimpleAlert(baseFragment, LocaleController.getString("ErrorOccurred", 2131625695) + "\n" + tLRPC$TL_error.text);
                }
            } else if (tLObject instanceof TLRPC$TL_account_sendConfirmPhoneCode) {
                if (i2 == 400) {
                    return showSimpleAlert(baseFragment, LocaleController.getString("CancelLinkExpired", 2131624842));
                }
                if (str.startsWith("FLOOD_WAIT")) {
                    return showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131625949));
                }
                return showSimpleAlert(baseFragment, LocaleController.getString("ErrorOccurred", 2131625695));
            } else if (tLObject instanceof TLRPC$TL_account_changePhone) {
                if (str.contains("PHONE_NUMBER_INVALID")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("InvalidPhoneNumber", 2131626297));
                } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("InvalidCode", 2131626293));
                } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("CodeExpired", 2131625187));
                } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131625949));
                } else if (tLRPC$TL_error.text.contains("FRESH_CHANGE_PHONE_FORBIDDEN")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("FreshChangePhoneForbidden", 2131626066));
                } else {
                    showSimpleAlert(baseFragment, tLRPC$TL_error.text);
                }
            } else if (tLObject instanceof TLRPC$TL_account_sendChangePhoneCode) {
                if (str.contains("PHONE_NUMBER_INVALID")) {
                    LoginActivity.needShowInvalidAlert(baseFragment, (String) objArr[0], false);
                } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("InvalidCode", 2131626293));
                } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("CodeExpired", 2131625187));
                } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131625949));
                } else if (tLRPC$TL_error.text.startsWith("PHONE_NUMBER_OCCUPIED")) {
                    showSimpleAlert(baseFragment, LocaleController.formatString("ChangePhoneNumberOccupied", 2131624874, objArr[0]));
                } else if (tLRPC$TL_error.text.startsWith("PHONE_NUMBER_BANNED")) {
                    LoginActivity.needShowInvalidAlert(baseFragment, (String) objArr[0], true);
                } else {
                    showSimpleAlert(baseFragment, LocaleController.getString("ErrorOccurred", 2131625695));
                }
            } else if (tLObject instanceof TLRPC$TL_updateUserName) {
                str.hashCode();
                if (str.equals("USERNAME_INVALID")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("UsernameInvalid", 2131628924));
                } else if (str.equals("USERNAME_OCCUPIED")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("UsernameInUse", 2131628923));
                } else {
                    showSimpleAlert(baseFragment, LocaleController.getString("ErrorOccurred", 2131625695));
                }
            } else if (tLObject instanceof TLRPC$TL_contacts_importContacts) {
                if (str.startsWith("FLOOD_WAIT")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131625949));
                } else {
                    showSimpleAlert(baseFragment, LocaleController.getString("ErrorOccurred", 2131625695) + "\n" + tLRPC$TL_error.text);
                }
            } else if ((tLObject instanceof TLRPC$TL_account_getPassword) || (tLObject instanceof TLRPC$TL_account_getTmpPassword)) {
                if (str.startsWith("FLOOD_WAIT")) {
                    showSimpleToast(baseFragment, getFloodWaitString(tLRPC$TL_error.text));
                } else {
                    showSimpleToast(baseFragment, tLRPC$TL_error.text);
                }
            } else if (tLObject instanceof TLRPC$TL_payments_sendPaymentForm) {
                str.hashCode();
                if (str.equals("BOT_PRECHECKOUT_FAILED")) {
                    showSimpleToast(baseFragment, LocaleController.getString("PaymentPrecheckoutFailed", 2131627458));
                } else if (str.equals("PAYMENT_FAILED")) {
                    showSimpleToast(baseFragment, LocaleController.getString("PaymentFailed", 2131627443));
                } else {
                    showSimpleToast(baseFragment, tLRPC$TL_error.text);
                }
            } else if (tLObject instanceof TLRPC$TL_payments_validateRequestedInfo) {
                str.hashCode();
                if (str.equals("SHIPPING_NOT_AVAILABLE")) {
                    showSimpleToast(baseFragment, LocaleController.getString("PaymentNoShippingMethod", 2131627447));
                } else {
                    showSimpleToast(baseFragment, tLRPC$TL_error.text);
                }
            }
        }
        return null;
    }

    public static Toast showSimpleToast(BaseFragment baseFragment, String str) {
        Context context;
        if (str == null) {
            return null;
        }
        if (baseFragment != null && baseFragment.getParentActivity() != null) {
            context = baseFragment.getParentActivity();
        } else {
            context = ApplicationLoader.applicationContext;
        }
        Toast makeText = Toast.makeText(context, str, 1);
        makeText.show();
        return makeText;
    }

    public static AlertDialog showUpdateAppAlert(Context context, String str, boolean z) {
        if (context == null || str == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(LocaleController.getString("AppName", 2131624384));
        builder.setMessage(str);
        builder.setPositiveButton(LocaleController.getString("OK", 2131627127), null);
        if (z) {
            builder.setNegativeButton(LocaleController.getString("UpdateApp", 2131628830), new AlertsCreator$$ExternalSyntheticLambda12(context));
        }
        return builder.show();
    }

    public static /* synthetic */ void lambda$showUpdateAppAlert$6(Context context, DialogInterface dialogInterface, int i) {
        Browser.openUrl(context, BuildVars.PLAYSTORE_APP_URL);
    }

    public static AlertDialog.Builder createLanguageAlert(LaunchActivity launchActivity, TLRPC$TL_langPackLanguage tLRPC$TL_langPackLanguage) {
        String str;
        int i;
        if (tLRPC$TL_langPackLanguage == null) {
            return null;
        }
        tLRPC$TL_langPackLanguage.lang_code = tLRPC$TL_langPackLanguage.lang_code.replace('-', '_').toLowerCase();
        tLRPC$TL_langPackLanguage.plural_code = tLRPC$TL_langPackLanguage.plural_code.replace('-', '_').toLowerCase();
        String str2 = tLRPC$TL_langPackLanguage.base_lang_code;
        if (str2 != null) {
            tLRPC$TL_langPackLanguage.base_lang_code = str2.replace('-', '_').toLowerCase();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(launchActivity);
        if (LocaleController.getInstance().getCurrentLocaleInfo().shortName.equals(tLRPC$TL_langPackLanguage.lang_code)) {
            builder.setTitle(LocaleController.getString("Language", 2131626393));
            str = LocaleController.formatString("LanguageSame", 2131626400, tLRPC$TL_langPackLanguage.name);
            builder.setNegativeButton(LocaleController.getString("OK", 2131627127), null);
            builder.setNeutralButton(LocaleController.getString("SETTINGS", 2131628120), new AlertsCreator$$ExternalSyntheticLambda31(launchActivity));
        } else if (tLRPC$TL_langPackLanguage.strings_count == 0) {
            builder.setTitle(LocaleController.getString("LanguageUnknownTitle", 2131626403));
            str = LocaleController.formatString("LanguageUnknownCustomAlert", 2131626402, tLRPC$TL_langPackLanguage.name);
            builder.setNegativeButton(LocaleController.getString("OK", 2131627127), null);
        } else {
            builder.setTitle(LocaleController.getString("LanguageTitle", 2131626401));
            str = tLRPC$TL_langPackLanguage.official ? LocaleController.formatString("LanguageAlert", 2131626394, tLRPC$TL_langPackLanguage.name, Integer.valueOf((int) Math.ceil((tLRPC$TL_langPackLanguage.translated_count / tLRPC$TL_langPackLanguage.strings_count) * 100.0f))) : LocaleController.formatString("LanguageCustomAlert", 2131626397, tLRPC$TL_langPackLanguage.name, Integer.valueOf((int) Math.ceil((tLRPC$TL_langPackLanguage.translated_count / tLRPC$TL_langPackLanguage.strings_count) * 100.0f)));
            builder.setPositiveButton(LocaleController.getString("Change", 2131624860), new AlertsCreator$$ExternalSyntheticLambda22(tLRPC$TL_langPackLanguage, launchActivity));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceTags(str));
        int indexOf = TextUtils.indexOf((CharSequence) spannableStringBuilder, '[');
        if (indexOf != -1) {
            int i2 = indexOf + 1;
            i = TextUtils.indexOf((CharSequence) spannableStringBuilder, ']', i2);
            if (i != -1) {
                spannableStringBuilder.delete(i, i + 1);
                spannableStringBuilder.delete(indexOf, i2);
            }
        } else {
            i = -1;
        }
        if (indexOf != -1 && i != -1) {
            spannableStringBuilder.setSpan(new AnonymousClass1(tLRPC$TL_langPackLanguage.translations_url, builder), indexOf, i - 1, 33);
        }
        TextView textView = new TextView(launchActivity);
        textView.setText(spannableStringBuilder);
        textView.setTextSize(1, 16.0f);
        textView.setLinkTextColor(Theme.getColor("dialogTextLink"));
        textView.setHighlightColor(Theme.getColor("dialogLinkSelection"));
        textView.setPadding(AndroidUtilities.dp(23.0f), 0, AndroidUtilities.dp(23.0f), 0);
        textView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        builder.setView(textView);
        return builder;
    }

    public static /* synthetic */ void lambda$createLanguageAlert$7(LaunchActivity launchActivity, DialogInterface dialogInterface, int i) {
        launchActivity.lambda$runLinkRequest$59(new LanguageSelectActivity());
    }

    public static /* synthetic */ void lambda$createLanguageAlert$8(TLRPC$TL_langPackLanguage tLRPC$TL_langPackLanguage, LaunchActivity launchActivity, DialogInterface dialogInterface, int i) {
        String str;
        if (tLRPC$TL_langPackLanguage.official) {
            str = "remote_" + tLRPC$TL_langPackLanguage.lang_code;
        } else {
            str = "unofficial_" + tLRPC$TL_langPackLanguage.lang_code;
        }
        LocaleController.LocaleInfo languageFromDict = LocaleController.getInstance().getLanguageFromDict(str);
        if (languageFromDict == null) {
            languageFromDict = new LocaleController.LocaleInfo();
            languageFromDict.name = tLRPC$TL_langPackLanguage.native_name;
            languageFromDict.nameEnglish = tLRPC$TL_langPackLanguage.name;
            languageFromDict.shortName = tLRPC$TL_langPackLanguage.lang_code;
            languageFromDict.baseLangCode = tLRPC$TL_langPackLanguage.base_lang_code;
            languageFromDict.pluralLangCode = tLRPC$TL_langPackLanguage.plural_code;
            languageFromDict.isRtl = tLRPC$TL_langPackLanguage.rtl;
            if (tLRPC$TL_langPackLanguage.official) {
                languageFromDict.pathToFile = "remote";
            } else {
                languageFromDict.pathToFile = "unofficial";
            }
        }
        LocaleController.getInstance().applyLanguage(languageFromDict, true, false, false, true, UserConfig.selectedAccount);
        launchActivity.rebuildAllFragments(true);
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends URLSpanNoUnderline {
        final /* synthetic */ AlertDialog.Builder val$builder;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(String str, AlertDialog.Builder builder) {
            super(str);
            this.val$builder = builder;
        }

        @Override // org.telegram.ui.Components.URLSpanNoUnderline, android.text.style.URLSpan, android.text.style.ClickableSpan
        public void onClick(View view) {
            this.val$builder.getDismissRunnable().run();
            super.onClick(view);
        }
    }

    public static boolean checkSlowMode(Context context, int i, long j, boolean z) {
        TLRPC$Chat chat;
        if (!DialogObject.isChatDialog(j) || (chat = MessagesController.getInstance(i).getChat(Long.valueOf(-j))) == null || !chat.slowmode_enabled || ChatObject.hasAdminRights(chat)) {
            return false;
        }
        if (!z) {
            TLRPC$ChatFull chatFull = MessagesController.getInstance(i).getChatFull(chat.id);
            if (chatFull == null) {
                chatFull = MessagesStorage.getInstance(i).loadChatInfo(chat.id, ChatObject.isChannel(chat), new CountDownLatch(1), false, false);
            }
            if (chatFull != null && chatFull.slowmode_next_send_date >= ConnectionsManager.getInstance(i).getCurrentTime()) {
                z = true;
            }
        }
        if (!z) {
            return false;
        }
        createSimpleAlert(context, chat.title, LocaleController.getString("SlowmodeSendError", 2131628432)).show();
        return true;
    }

    public static AlertDialog.Builder createSimpleAlert(Context context, String str) {
        return createSimpleAlert(context, null, str);
    }

    public static AlertDialog.Builder createSimpleAlert(Context context, String str, String str2) {
        return createSimpleAlert(context, str, str2, null);
    }

    public static AlertDialog.Builder createSimpleAlert(Context context, String str, String str2, Theme.ResourcesProvider resourcesProvider) {
        if (context == null || str2 == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (str == null) {
            str = LocaleController.getString("AppName", 2131624384);
        }
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setPositiveButton(LocaleController.getString("OK", 2131627127), null);
        return builder;
    }

    public static Dialog showSimpleAlert(BaseFragment baseFragment, String str) {
        return showSimpleAlert(baseFragment, null, str);
    }

    public static Dialog showSimpleAlert(BaseFragment baseFragment, String str, String str2) {
        return showSimpleAlert(baseFragment, str, str2, null);
    }

    public static Dialog showSimpleAlert(BaseFragment baseFragment, String str, String str2, Theme.ResourcesProvider resourcesProvider) {
        if (str2 == null || baseFragment == null || baseFragment.getParentActivity() == null) {
            return null;
        }
        AlertDialog create = createSimpleAlert(baseFragment.getParentActivity(), str, str2, resourcesProvider).create();
        baseFragment.showDialog(create);
        return create;
    }

    public static void showBlockReportSpamReplyAlert(ChatActivity chatActivity, MessageObject messageObject, long j, Theme.ResourcesProvider resourcesProvider, Runnable runnable) {
        if (chatActivity == null || chatActivity.getParentActivity() == null || messageObject == null) {
            return;
        }
        AccountInstance accountInstance = chatActivity.getAccountInstance();
        TLRPC$User user = j > 0 ? accountInstance.getMessagesController().getUser(Long.valueOf(j)) : null;
        TLRPC$Chat chat = j < 0 ? accountInstance.getMessagesController().getChat(Long.valueOf(-j)) : null;
        if (user == null && chat == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(chatActivity.getParentActivity(), resourcesProvider);
        builder.setDimEnabled(runnable == null);
        builder.setOnPreDismissListener(new AlertsCreator$$ExternalSyntheticLambda41(runnable));
        builder.setTitle(LocaleController.getString("BlockUser", 2131624690));
        if (user != null) {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BlockUserReplyAlert", 2131624696, UserObject.getFirstName(user))));
        } else {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BlockUserReplyAlert", 2131624696, chat.title)));
        }
        LinearLayout linearLayout = new LinearLayout(chatActivity.getParentActivity());
        linearLayout.setOrientation(1);
        CheckBoxCell[] checkBoxCellArr = {new CheckBoxCell(chatActivity.getParentActivity(), 1, resourcesProvider)};
        checkBoxCellArr[0].setBackgroundDrawable(Theme.getSelectorDrawable(false));
        checkBoxCellArr[0].setTag(0);
        checkBoxCellArr[0].setText(LocaleController.getString("DeleteReportSpam", 2131625445), "", true, false);
        checkBoxCellArr[0].setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
        linearLayout.addView(checkBoxCellArr[0], LayoutHelper.createLinear(-1, -2));
        checkBoxCellArr[0].setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda65(checkBoxCellArr));
        builder.setCustomViewOffset(12);
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("BlockAndDeleteReplies", 2131624688), new AlertsCreator$$ExternalSyntheticLambda23(user, accountInstance, chatActivity, chat, messageObject, checkBoxCellArr, resourcesProvider));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        AlertDialog create = builder.create();
        chatActivity.showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView == null) {
            return;
        }
        textView.setTextColor(Theme.getColor("dialogTextRed2"));
    }

    public static /* synthetic */ void lambda$showBlockReportSpamReplyAlert$9(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public static /* synthetic */ void lambda$showBlockReportSpamReplyAlert$10(CheckBoxCell[] checkBoxCellArr, View view) {
        Integer num = (Integer) view.getTag();
        checkBoxCellArr[num.intValue()].setChecked(!checkBoxCellArr[num.intValue()].isChecked(), true);
    }

    public static /* synthetic */ void lambda$showBlockReportSpamReplyAlert$12(TLRPC$User tLRPC$User, AccountInstance accountInstance, ChatActivity chatActivity, TLRPC$Chat tLRPC$Chat, MessageObject messageObject, CheckBoxCell[] checkBoxCellArr, Theme.ResourcesProvider resourcesProvider, DialogInterface dialogInterface, int i) {
        if (tLRPC$User != null) {
            accountInstance.getMessagesStorage().deleteUserChatHistory(chatActivity.getDialogId(), tLRPC$User.id);
        } else {
            accountInstance.getMessagesStorage().deleteUserChatHistory(chatActivity.getDialogId(), -tLRPC$Chat.id);
        }
        TLRPC$TL_contacts_blockFromReplies tLRPC$TL_contacts_blockFromReplies = new TLRPC$TL_contacts_blockFromReplies();
        tLRPC$TL_contacts_blockFromReplies.msg_id = messageObject.getId();
        tLRPC$TL_contacts_blockFromReplies.delete_message = true;
        tLRPC$TL_contacts_blockFromReplies.delete_history = true;
        if (checkBoxCellArr[0].isChecked()) {
            tLRPC$TL_contacts_blockFromReplies.report_spam = true;
            if (chatActivity.getParentActivity() != null) {
                chatActivity.getUndoView().showWithAction(0L, 74, (Runnable) null);
            }
        }
        accountInstance.getConnectionsManager().sendRequest(tLRPC$TL_contacts_blockFromReplies, new AlertsCreator$$ExternalSyntheticLambda94(accountInstance));
    }

    public static /* synthetic */ void lambda$showBlockReportSpamReplyAlert$11(AccountInstance accountInstance, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject instanceof TLRPC$Updates) {
            accountInstance.getMessagesController().processUpdates((TLRPC$Updates) tLObject, false);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0132  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01dd  */
    /* JADX WARN: Removed duplicated region for block: B:57:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void showBlockReportSpamAlert(BaseFragment baseFragment, long j, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, TLRPC$EncryptedChat tLRPC$EncryptedChat, boolean z, TLRPC$ChatFull tLRPC$ChatFull, MessagesStorage.IntCallback intCallback, Theme.ResourcesProvider resourcesProvider) {
        boolean z2;
        String str;
        CheckBoxCell[] checkBoxCellArr;
        TextView textView;
        String str2;
        Theme.ResourcesProvider resourcesProvider2 = resourcesProvider;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        AccountInstance accountInstance = baseFragment.getAccountInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity(), resourcesProvider2);
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(baseFragment.getCurrentAccount());
        int i = 1;
        if (tLRPC$EncryptedChat == null) {
            if (!notificationsSettings.getBoolean("dialog_bar_report" + j, false)) {
                z2 = false;
                if (tLRPC$User == null) {
                    builder.setTitle(LocaleController.formatString("BlockUserTitle", 2131624697, UserObject.getFirstName(tLRPC$User)));
                    builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BlockUserAlert", 2131624691, UserObject.getFirstName(tLRPC$User))));
                    String string = LocaleController.getString("BlockContact", 2131624689);
                    CheckBoxCell[] checkBoxCellArr2 = new CheckBoxCell[2];
                    LinearLayout linearLayout = new LinearLayout(baseFragment.getParentActivity());
                    linearLayout.setOrientation(1);
                    int i2 = 0;
                    for (int i3 = 2; i2 < i3; i3 = 2) {
                        if (i2 != 0 || z2) {
                            checkBoxCellArr2[i2] = new CheckBoxCell(baseFragment.getParentActivity(), i, resourcesProvider2);
                            checkBoxCellArr2[i2].setBackgroundDrawable(Theme.getSelectorDrawable(false));
                            checkBoxCellArr2[i2].setTag(Integer.valueOf(i2));
                            if (i2 == 0) {
                                str2 = string;
                                checkBoxCellArr2[i2].setText(LocaleController.getString("DeleteReportSpam", 2131625445), "", true, false);
                            } else {
                                str2 = string;
                                checkBoxCellArr2[i2].setText(LocaleController.formatString("DeleteThisChat", 2131625454, new Object[0]), "", true, false);
                            }
                            checkBoxCellArr2[i2].setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                            linearLayout.addView(checkBoxCellArr2[i2], LayoutHelper.createLinear(-1, -2));
                            checkBoxCellArr2[i2].setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda66(checkBoxCellArr2));
                        } else {
                            str2 = string;
                        }
                        i2++;
                        resourcesProvider2 = resourcesProvider;
                        string = str2;
                        i = 1;
                    }
                    builder.setCustomViewOffset(12);
                    builder.setView(linearLayout);
                    checkBoxCellArr = checkBoxCellArr2;
                    str = string;
                } else {
                    if (tLRPC$Chat != null && z) {
                        builder.setTitle(LocaleController.getString("ReportUnrelatedGroup", 2131628023));
                        if (tLRPC$ChatFull != null) {
                            TLRPC$ChannelLocation tLRPC$ChannelLocation = tLRPC$ChatFull.location;
                            if (tLRPC$ChannelLocation instanceof TLRPC$TL_channelLocation) {
                                builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("ReportUnrelatedGroupText", 2131628024, ((TLRPC$TL_channelLocation) tLRPC$ChannelLocation).address)));
                            }
                        }
                        builder.setMessage(LocaleController.getString("ReportUnrelatedGroupTextNoAddress", 2131628025));
                    } else {
                        builder.setTitle(LocaleController.getString("ReportSpamTitle", 2131628016));
                        if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                            builder.setMessage(LocaleController.getString("ReportSpamAlertChannel", 2131628012));
                        } else {
                            builder.setMessage(LocaleController.getString("ReportSpamAlertGroup", 2131628013));
                        }
                    }
                    str = LocaleController.getString("ReportChat", 2131627992);
                    checkBoxCellArr = null;
                }
                builder.setPositiveButton(str, new AlertsCreator$$ExternalSyntheticLambda24(tLRPC$User, accountInstance, checkBoxCellArr, j, tLRPC$Chat, tLRPC$EncryptedChat, z, intCallback));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
                AlertDialog create = builder.create();
                baseFragment.showDialog(create);
                textView = (TextView) create.getButton(-1);
                if (textView != null) {
                    return;
                }
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
                return;
            }
        }
        z2 = true;
        if (tLRPC$User == null) {
        }
        builder.setPositiveButton(str, new AlertsCreator$$ExternalSyntheticLambda24(tLRPC$User, accountInstance, checkBoxCellArr, j, tLRPC$Chat, tLRPC$EncryptedChat, z, intCallback));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        AlertDialog create2 = builder.create();
        baseFragment.showDialog(create2);
        textView = (TextView) create2.getButton(-1);
        if (textView != null) {
        }
    }

    public static /* synthetic */ void lambda$showBlockReportSpamAlert$13(CheckBoxCell[] checkBoxCellArr, View view) {
        Integer num = (Integer) view.getTag();
        checkBoxCellArr[num.intValue()].setChecked(!checkBoxCellArr[num.intValue()].isChecked(), true);
    }

    public static /* synthetic */ void lambda$showBlockReportSpamAlert$14(TLRPC$User tLRPC$User, AccountInstance accountInstance, CheckBoxCell[] checkBoxCellArr, long j, TLRPC$Chat tLRPC$Chat, TLRPC$EncryptedChat tLRPC$EncryptedChat, boolean z, MessagesStorage.IntCallback intCallback, DialogInterface dialogInterface, int i) {
        if (tLRPC$User != null) {
            accountInstance.getMessagesController().blockPeer(tLRPC$User.id);
        }
        if (checkBoxCellArr == null || (checkBoxCellArr[0] != null && checkBoxCellArr[0].isChecked())) {
            accountInstance.getMessagesController().reportSpam(j, tLRPC$User, tLRPC$Chat, tLRPC$EncryptedChat, tLRPC$Chat != null && z);
        }
        if (checkBoxCellArr == null || checkBoxCellArr[1].isChecked()) {
            if (tLRPC$Chat != null) {
                if (ChatObject.isNotInChat(tLRPC$Chat)) {
                    accountInstance.getMessagesController().deleteDialog(j, 0);
                } else {
                    accountInstance.getMessagesController().deleteParticipantFromChat(-j, accountInstance.getMessagesController().getUser(Long.valueOf(accountInstance.getUserConfig().getClientUserId())), null);
                }
            } else {
                accountInstance.getMessagesController().deleteDialog(j, 0);
            }
            intCallback.run(1);
            return;
        }
        intCallback.run(0);
    }

    public static void showCustomNotificationsDialog(BaseFragment baseFragment, long j, int i, ArrayList<NotificationsSettingsActivity.NotificationException> arrayList, int i2, MessagesStorage.IntCallback intCallback) {
        showCustomNotificationsDialog(baseFragment, j, i, arrayList, i2, intCallback, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r8v2 */
    public static void showCustomNotificationsDialog(BaseFragment baseFragment, long j, int i, ArrayList<NotificationsSettingsActivity.NotificationException> arrayList, int i2, MessagesStorage.IntCallback intCallback, MessagesStorage.IntCallback intCallback2) {
        String[] strArr;
        Drawable drawable;
        int[] iArr;
        AlertDialog.Builder builder;
        int i3;
        LinearLayout linearLayout;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        boolean isGlobalNotificationsEnabled = NotificationsController.getInstance(i2).isGlobalNotificationsEnabled(j);
        String[] strArr2 = new String[5];
        strArr2[0] = LocaleController.getString("NotificationsTurnOn", 2131627121);
        ?? r8 = 1;
        strArr2[1] = LocaleController.formatString("MuteFor", 2131626799, LocaleController.formatPluralString("Hours", 1, new Object[0]));
        strArr2[2] = LocaleController.formatString("MuteFor", 2131626799, LocaleController.formatPluralString("Days", 2, new Object[0]));
        Drawable drawable2 = null;
        strArr2[3] = (j != 0 || !(baseFragment instanceof NotificationsCustomSettingsActivity)) ? LocaleController.getString("NotificationsCustomize", 2131627075) : null;
        int i4 = 4;
        strArr2[4] = LocaleController.getString("NotificationsTurnOff", 2131627120);
        int[] iArr2 = {2131166025, 2131166022, 2131166023, 2131166026, 2131166024};
        LinearLayout linearLayout2 = new LinearLayout(baseFragment.getParentActivity());
        linearLayout2.setOrientation(1);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(baseFragment.getParentActivity());
        int i5 = 0;
        LinearLayout linearLayout3 = linearLayout2;
        for (int i6 = 5; i5 < i6; i6 = 5) {
            if (strArr2[i5] == null) {
                i3 = i5;
                builder = builder2;
                linearLayout = linearLayout3;
                iArr = iArr2;
                drawable = drawable2;
                strArr = strArr2;
            } else {
                TextView textView = new TextView(baseFragment.getParentActivity());
                Drawable drawable3 = baseFragment.getParentActivity().getResources().getDrawable(iArr2[i5]);
                if (i5 == i4) {
                    textView.setTextColor(Theme.getColor("dialogTextRed"));
                    drawable3.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogRedIcon"), PorterDuff.Mode.MULTIPLY));
                } else {
                    textView.setTextColor(Theme.getColor("dialogTextBlack"));
                    drawable3.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogIcon"), PorterDuff.Mode.MULTIPLY));
                }
                int i7 = r8 == true ? 1 : 0;
                int i8 = r8 == true ? 1 : 0;
                textView.setTextSize(i7, 16.0f);
                textView.setLines(r8);
                textView.setMaxLines(r8);
                textView.setCompoundDrawablesWithIntrinsicBounds(drawable3, drawable2, drawable2, drawable2);
                textView.setTag(Integer.valueOf(i5));
                textView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                textView.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
                textView.setSingleLine(r8);
                textView.setGravity(19);
                textView.setCompoundDrawablePadding(AndroidUtilities.dp(26.0f));
                textView.setText(strArr2[i5]);
                linearLayout3.addView(textView, LayoutHelper.createLinear(-1, 48, 51));
                i3 = i5;
                builder = builder2;
                linearLayout = linearLayout3;
                iArr = iArr2;
                drawable = drawable2;
                strArr = strArr2;
                textView.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda47(j, i2, isGlobalNotificationsEnabled, intCallback2, i, baseFragment, arrayList, intCallback, builder));
            }
            i5 = i3 + 1;
            linearLayout3 = linearLayout;
            builder2 = builder;
            iArr2 = iArr;
            drawable2 = drawable;
            strArr2 = strArr;
            i4 = 4;
            r8 = 1;
        }
        AlertDialog.Builder builder3 = builder2;
        builder3.setTitle(LocaleController.getString("Notifications", 2131627068));
        builder3.setView(linearLayout3);
        baseFragment.showDialog(builder3.create());
    }

    public static /* synthetic */ void lambda$showCustomNotificationsDialog$15(long j, int i, boolean z, MessagesStorage.IntCallback intCallback, int i2, BaseFragment baseFragment, ArrayList arrayList, MessagesStorage.IntCallback intCallback2, AlertDialog.Builder builder, View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        if (intValue == 0) {
            if (j != 0) {
                SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(i).edit();
                if (z) {
                    edit.remove("notify2_" + j);
                } else {
                    edit.putInt("notify2_" + j, 0);
                }
                MessagesStorage.getInstance(i).setDialogFlags(j, 0L);
                edit.commit();
                TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(i).dialogs_dict.get(j);
                if (tLRPC$Dialog != null) {
                    tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
                }
                NotificationsController.getInstance(i).updateServerNotificationsSettings(j);
                if (intCallback != null) {
                    if (z) {
                        intCallback.run(0);
                    } else {
                        intCallback.run(1);
                    }
                }
            } else {
                NotificationsController.getInstance(i).setGlobalNotificationsEnabled(i2, 0);
            }
        } else if (intValue != 3) {
            int currentTime = ConnectionsManager.getInstance(i).getCurrentTime();
            if (intValue == 1) {
                currentTime += 3600;
            } else if (intValue == 2) {
                currentTime += 172800;
            } else if (intValue == 4) {
                currentTime = Integer.MAX_VALUE;
            }
            NotificationsController.getInstance(i).muteUntil(j, currentTime);
            if (j != 0 && intCallback != null) {
                if (intValue == 4 && !z) {
                    intCallback.run(0);
                } else {
                    intCallback.run(1);
                }
            }
            if (j == 0) {
                NotificationsController.getInstance(i).setGlobalNotificationsEnabled(i2, Integer.MAX_VALUE);
            }
        } else if (j != 0) {
            Bundle bundle = new Bundle();
            bundle.putLong("dialog_id", j);
            baseFragment.presentFragment(new ProfileNotificationsActivity(bundle));
        } else {
            baseFragment.presentFragment(new NotificationsCustomSettingsActivity(i2, arrayList));
        }
        if (intCallback2 != null) {
            intCallback2.run(intValue);
        }
        builder.getDismissRunnable().run();
        int i3 = intValue == 0 ? 4 : intValue == 1 ? 0 : intValue == 2 ? 2 : intValue == 4 ? 3 : -1;
        if (i3 < 0 || !BulletinFactory.canShowBulletin(baseFragment)) {
            return;
        }
        BulletinFactory.createMuteBulletin(baseFragment, i3).show();
    }

    public static AlertDialog showSecretLocationAlert(Context context, int i, Runnable runnable, boolean z, Theme.ResourcesProvider resourcesProvider) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i2 = MessagesController.getInstance(i).availableMapProviders;
        if ((i2 & 1) != 0) {
            arrayList.add(LocaleController.getString("MapPreviewProviderTelegram", 2131626584));
            arrayList2.add(0);
        }
        if ((i2 & 2) != 0) {
            arrayList.add(LocaleController.getString("MapPreviewProviderGoogle", 2131626582));
            arrayList2.add(1);
        }
        if ((i2 & 4) != 0) {
            arrayList.add(LocaleController.getString("MapPreviewProviderYandex", 2131626586));
            arrayList2.add(3);
        }
        arrayList.add(LocaleController.getString("MapPreviewProviderNobody", 2131626583));
        arrayList2.add(2);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, resourcesProvider);
        builder.setTitle(LocaleController.getString("MapPreviewProviderTitle", 2131626585));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        builder.setView(linearLayout);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            RadioColorCell radioColorCell = new RadioColorCell(context, resourcesProvider);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag(Integer.valueOf(i3));
            radioColorCell.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
            radioColorCell.setTextAndValue((String) arrayList.get(i3), SharedConfig.mapPreviewType == ((Integer) arrayList2.get(i3)).intValue());
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda50(arrayList2, runnable, builder));
        }
        if (!z) {
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        }
        AlertDialog show = builder.show();
        if (z) {
            show.setCanceledOnTouchOutside(false);
        }
        return show;
    }

    public static /* synthetic */ void lambda$showSecretLocationAlert$16(ArrayList arrayList, Runnable runnable, AlertDialog.Builder builder, View view) {
        SharedConfig.setSecretMapPreviewType(((Integer) arrayList.get(((Integer) view.getTag()).intValue())).intValue());
        if (runnable != null) {
            runnable.run();
        }
        builder.getDismissRunnable().run();
    }

    public static void updateDayPicker(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2, numberPicker2.getValue());
        calendar.set(1, numberPicker3.getValue());
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(calendar.getActualMaximum(5));
    }

    private static void checkPickerDate(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i = calendar.get(1);
        int i2 = calendar.get(2);
        int i3 = calendar.get(5);
        if (i > numberPicker3.getValue()) {
            numberPicker3.setValue(i);
        }
        if (numberPicker3.getValue() == i) {
            if (i2 > numberPicker2.getValue()) {
                numberPicker2.setValue(i2);
            }
            if (i2 != numberPicker2.getValue() || i3 <= numberPicker.getValue()) {
                return;
            }
            numberPicker.setValue(i3);
        }
    }

    public static void showOpenUrlAlert(BaseFragment baseFragment, String str, boolean z, boolean z2) {
        showOpenUrlAlert(baseFragment, str, z, true, z2, null);
    }

    public static void showOpenUrlAlert(BaseFragment baseFragment, String str, boolean z, boolean z2, Theme.ResourcesProvider resourcesProvider) {
        showOpenUrlAlert(baseFragment, str, z, true, z2, resourcesProvider);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0090  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void showOpenUrlAlert(BaseFragment baseFragment, String str, boolean z, boolean z2, boolean z3, Theme.ResourcesProvider resourcesProvider) {
        Uri parse;
        String str2;
        int indexOf;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        long inlineReturn = baseFragment instanceof ChatActivity ? ((ChatActivity) baseFragment).getInlineReturn() : 0L;
        boolean z4 = true;
        if (Browser.isInternalUrl(str, null) || !z3) {
            Activity parentActivity = baseFragment.getParentActivity();
            if (inlineReturn != 0) {
                z4 = false;
            }
            Browser.openUrl(parentActivity, str, z4, z2);
            return;
        }
        if (z) {
            try {
                str2 = parse.getScheme() + "://" + IDN.toASCII(Uri.parse(str).getHost(), 1) + parse.getPath();
            } catch (Exception e) {
                FileLog.e(e);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity(), resourcesProvider);
            builder.setTitle(LocaleController.getString("OpenUrlTitle", 2131627160));
            String string = LocaleController.getString("OpenUrlAlert2", 2131627157);
            indexOf = string.indexOf("%");
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(String.format(string, str2));
            if (indexOf >= 0) {
                spannableStringBuilder.setSpan(new URLSpan(str2), indexOf, str2.length() + indexOf, 33);
            }
            builder.setMessage(spannableStringBuilder);
            builder.setMessageTextViewClickable(false);
            builder.setPositiveButton(LocaleController.getString("Open", 2131627142), new AlertsCreator$$ExternalSyntheticLambda27(baseFragment, str, inlineReturn, z2));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
            baseFragment.showDialog(builder.create());
        }
        str2 = str;
        AlertDialog.Builder builder2 = new AlertDialog.Builder(baseFragment.getParentActivity(), resourcesProvider);
        builder2.setTitle(LocaleController.getString("OpenUrlTitle", 2131627160));
        String string2 = LocaleController.getString("OpenUrlAlert2", 2131627157);
        indexOf = string2.indexOf("%");
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(String.format(string2, str2));
        if (indexOf >= 0) {
        }
        builder2.setMessage(spannableStringBuilder2);
        builder2.setMessageTextViewClickable(false);
        builder2.setPositiveButton(LocaleController.getString("Open", 2131627142), new AlertsCreator$$ExternalSyntheticLambda27(baseFragment, str, inlineReturn, z2));
        builder2.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        baseFragment.showDialog(builder2.create());
    }

    public static /* synthetic */ void lambda$showOpenUrlAlert$17(BaseFragment baseFragment, String str, long j, boolean z, DialogInterface dialogInterface, int i) {
        Browser.openUrl(baseFragment.getParentActivity(), str, j == 0, z);
    }

    public static AlertDialog createSupportAlert(BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider) {
        URLSpan[] uRLSpanArr;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return null;
        }
        TextView textView = new TextView(baseFragment.getParentActivity());
        SpannableString spannableString = new SpannableString(Html.fromHtml(LocaleController.getString("AskAQuestionInfo", 2131624485).replace("\n", "<br>")));
        for (URLSpan uRLSpan : (URLSpan[]) spannableString.getSpans(0, spannableString.length(), URLSpan.class)) {
            int spanStart = spannableString.getSpanStart(uRLSpan);
            int spanEnd = spannableString.getSpanEnd(uRLSpan);
            spannableString.removeSpan(uRLSpan);
            spannableString.setSpan(new AnonymousClass2(uRLSpan.getURL(), baseFragment), spanStart, spanEnd, 0);
        }
        textView.setText(spannableString);
        textView.setTextSize(1, 16.0f);
        textView.setLinkTextColor(Theme.getColor("dialogTextLink", resourcesProvider));
        textView.setHighlightColor(Theme.getColor("dialogLinkSelection", resourcesProvider));
        textView.setPadding(AndroidUtilities.dp(23.0f), 0, AndroidUtilities.dp(23.0f), 0);
        textView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        textView.setTextColor(Theme.getColor("dialogTextBlack", resourcesProvider));
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity(), resourcesProvider);
        builder.setView(textView);
        builder.setTitle(LocaleController.getString("AskAQuestion", 2131624484));
        builder.setPositiveButton(LocaleController.getString("AskButton", 2131624486), new AlertsCreator$$ExternalSyntheticLambda26(baseFragment));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        return builder.create();
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends URLSpanNoUnderline {
        final /* synthetic */ BaseFragment val$fragment;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(String str, BaseFragment baseFragment) {
            super(str);
            this.val$fragment = baseFragment;
        }

        @Override // org.telegram.ui.Components.URLSpanNoUnderline, android.text.style.URLSpan, android.text.style.ClickableSpan
        public void onClick(View view) {
            this.val$fragment.dismissCurrentDialog();
            super.onClick(view);
        }
    }

    public static void performAskAQuestion(BaseFragment baseFragment) {
        String string;
        int currentAccount = baseFragment.getCurrentAccount();
        SharedPreferences mainSettings = MessagesController.getMainSettings(currentAccount);
        long prefIntOrLong = AndroidUtilities.getPrefIntOrLong(mainSettings, "support_id2", 0L);
        TLRPC$User tLRPC$User = null;
        if (prefIntOrLong != 0) {
            TLRPC$User user = MessagesController.getInstance(currentAccount).getUser(Long.valueOf(prefIntOrLong));
            if (user == null && (string = mainSettings.getString("support_user", null)) != null) {
                try {
                    byte[] decode = Base64.decode(string, 0);
                    if (decode != null) {
                        SerializedData serializedData = new SerializedData(decode);
                        TLRPC$User TLdeserialize = TLRPC$User.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                        if (TLdeserialize != null && TLdeserialize.id == 333000) {
                            TLdeserialize = null;
                        }
                        serializedData.cleanup();
                        tLRPC$User = TLdeserialize;
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            tLRPC$User = user;
        }
        if (tLRPC$User == null) {
            AlertDialog alertDialog = new AlertDialog(baseFragment.getParentActivity(), 3);
            alertDialog.setCanCancel(false);
            alertDialog.show();
            ConnectionsManager.getInstance(currentAccount).sendRequest(new TLRPC$TL_help_getSupport(), new AlertsCreator$$ExternalSyntheticLambda93(mainSettings, alertDialog, currentAccount, baseFragment));
            return;
        }
        MessagesController.getInstance(currentAccount).putUser(tLRPC$User, true);
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", tLRPC$User.id);
        baseFragment.presentFragment(new ChatActivity(bundle));
    }

    public static /* synthetic */ void lambda$performAskAQuestion$21(SharedPreferences sharedPreferences, AlertDialog alertDialog, int i, BaseFragment baseFragment, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            AndroidUtilities.runOnUIThread(new AlertsCreator$$ExternalSyntheticLambda85(sharedPreferences, (TLRPC$TL_help_support) tLObject, alertDialog, i, baseFragment));
        } else {
            AndroidUtilities.runOnUIThread(new AlertsCreator$$ExternalSyntheticLambda86(alertDialog));
        }
    }

    public static /* synthetic */ void lambda$performAskAQuestion$19(SharedPreferences sharedPreferences, TLRPC$TL_help_support tLRPC$TL_help_support, AlertDialog alertDialog, int i, BaseFragment baseFragment) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong("support_id2", tLRPC$TL_help_support.user.id);
        SerializedData serializedData = new SerializedData();
        tLRPC$TL_help_support.user.serializeToStream(serializedData);
        edit.putString("support_user", Base64.encodeToString(serializedData.toByteArray(), 0));
        edit.commit();
        serializedData.cleanup();
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        ArrayList<TLRPC$User> arrayList = new ArrayList<>();
        arrayList.add(tLRPC$TL_help_support.user);
        MessagesStorage.getInstance(i).putUsersAndChats(arrayList, null, true, true);
        MessagesController.getInstance(i).putUser(tLRPC$TL_help_support.user, false);
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", tLRPC$TL_help_support.user.id);
        baseFragment.presentFragment(new ChatActivity(bundle));
    }

    public static /* synthetic */ void lambda$performAskAQuestion$20(AlertDialog alertDialog) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void createImportDialogAlert(BaseFragment baseFragment, String str, String str2, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, Runnable runnable) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        if (tLRPC$Chat == null && tLRPC$User == null) {
            return;
        }
        int currentAccount = baseFragment.getCurrentAccount();
        Activity parentActivity = baseFragment.getParentActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        long clientUserId = UserConfig.getInstance(currentAccount).getClientUserId();
        TextView textView = new TextView(parentActivity);
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        textView.setTextSize(1, 16.0f);
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        FrameLayout frameLayout = new FrameLayout(parentActivity);
        builder.setView(frameLayout);
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        BackupImageView backupImageView = new BackupImageView(parentActivity);
        backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
        frameLayout.addView(backupImageView, LayoutHelper.createFrame(40, 40.0f, (LocaleController.isRTL ? 5 : 3) | 48, 22.0f, 5.0f, 22.0f, 0.0f));
        TextView textView2 = new TextView(parentActivity);
        textView2.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
        textView2.setTextSize(1, 20.0f);
        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView2.setLines(1);
        textView2.setMaxLines(1);
        textView2.setSingleLine(true);
        textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        textView2.setEllipsize(TextUtils.TruncateAt.END);
        textView2.setText(LocaleController.getString("ImportMessages", 2131626242));
        boolean z = LocaleController.isRTL;
        int i = (z ? 5 : 3) | 48;
        int i2 = 21;
        float f = z ? 21 : 76;
        if (z) {
            i2 = 76;
        }
        frameLayout.addView(textView2, LayoutHelper.createFrame(-1, -2.0f, i, f, 11.0f, i2, 0.0f));
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 57.0f, 24.0f, 9.0f));
        if (tLRPC$User != null) {
            if (UserObject.isReplyUser(tLRPC$User)) {
                avatarDrawable.setSmallSize(true);
                avatarDrawable.setAvatarType(12);
                backupImageView.setImage((ImageLocation) null, (String) null, avatarDrawable, tLRPC$User);
            } else if (tLRPC$User.id == clientUserId) {
                avatarDrawable.setSmallSize(true);
                avatarDrawable.setAvatarType(1);
                backupImageView.setImage((ImageLocation) null, (String) null, avatarDrawable, tLRPC$User);
            } else {
                avatarDrawable.setSmallSize(false);
                avatarDrawable.setInfo(tLRPC$User);
                backupImageView.setForUserOrChat(tLRPC$User, avatarDrawable);
            }
        } else {
            avatarDrawable.setInfo(tLRPC$Chat);
            backupImageView.setForUserOrChat(tLRPC$Chat, avatarDrawable);
        }
        textView.setText(AndroidUtilities.replaceTags(str2));
        builder.setPositiveButton(LocaleController.getString("Import", 2131626222), new AlertsCreator$$ExternalSyntheticLambda15(runnable));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        baseFragment.showDialog(builder.create());
    }

    public static /* synthetic */ void lambda$createImportDialogAlert$22(Runnable runnable, DialogInterface dialogInterface, int i) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public static void createClearOrDeleteDialogAlert(BaseFragment baseFragment, boolean z, TLRPC$Chat tLRPC$Chat, TLRPC$User tLRPC$User, boolean z2, boolean z3, MessagesStorage.BooleanCallback booleanCallback) {
        createClearOrDeleteDialogAlert(baseFragment, z, false, false, tLRPC$Chat, tLRPC$User, z2, false, z3, booleanCallback, null);
    }

    public static void createClearOrDeleteDialogAlert(BaseFragment baseFragment, boolean z, TLRPC$Chat tLRPC$Chat, TLRPC$User tLRPC$User, boolean z2, boolean z3, boolean z4, MessagesStorage.BooleanCallback booleanCallback, Theme.ResourcesProvider resourcesProvider) {
        createClearOrDeleteDialogAlert(baseFragment, z, tLRPC$Chat != null && tLRPC$Chat.creator, false, tLRPC$Chat, tLRPC$User, z2, z3, z4, booleanCallback, resourcesProvider);
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x0225 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0238 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0285  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x02c2  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x02c9  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x02d3  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x02d8  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0303  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0307  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0341  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x034e  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x0396  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x053d  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0548  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x05f5  */
    /* JADX WARN: Removed duplicated region for block: B:233:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01fc  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x020d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void createClearOrDeleteDialogAlert(BaseFragment baseFragment, boolean z, boolean z2, boolean z3, TLRPC$Chat tLRPC$Chat, TLRPC$User tLRPC$User, boolean z4, boolean z5, boolean z6, MessagesStorage.BooleanCallback booleanCallback, Theme.ResourcesProvider resourcesProvider) {
        CheckBoxCell[] checkBoxCellArr;
        boolean z7;
        String str;
        boolean z8;
        int i;
        TextView textView;
        MessageObject messageObject;
        boolean z9;
        String str2;
        CharSequence string;
        TextView textView2;
        boolean z10;
        int i2;
        float f;
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageAction tLRPC$MessageAction;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        if (tLRPC$Chat == null && tLRPC$User == null) {
            return;
        }
        int currentAccount = baseFragment.getCurrentAccount();
        Activity parentActivity = baseFragment.getParentActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity, resourcesProvider);
        long clientUserId = UserConfig.getInstance(currentAccount).getClientUserId();
        CheckBoxCell[] checkBoxCellArr2 = new CheckBoxCell[1];
        TextView textView3 = new TextView(parentActivity);
        textView3.setTextColor(Theme.getColor("dialogTextBlack"));
        textView3.setTextSize(1, 16.0f);
        textView3.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        boolean z11 = !z6 && ChatObject.isChannel(tLRPC$Chat) && !TextUtils.isEmpty(tLRPC$Chat.username);
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(parentActivity, checkBoxCellArr2);
        builder.setView(anonymousClass3);
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        BackupImageView backupImageView = new BackupImageView(parentActivity);
        backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
        anonymousClass3.addView(backupImageView, LayoutHelper.createFrame(40, 40.0f, (LocaleController.isRTL ? 5 : 3) | 48, 22.0f, 5.0f, 22.0f, 0.0f));
        TextView textView4 = new TextView(parentActivity);
        textView4.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
        textView4.setTextSize(1, 20.0f);
        textView4.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView4.setLines(1);
        textView4.setMaxLines(1);
        textView4.setSingleLine(true);
        textView4.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        textView4.setEllipsize(TextUtils.TruncateAt.END);
        if (!z) {
            checkBoxCellArr = checkBoxCellArr2;
            z7 = z11;
            if (z2) {
                if (ChatObject.isChannel(tLRPC$Chat)) {
                    if (tLRPC$Chat.megagroup) {
                        textView4.setText(LocaleController.getString("DeleteMegaMenu", 2131625434));
                    } else {
                        textView4.setText(LocaleController.getString("ChannelDeleteMenu", 2131624912));
                    }
                } else {
                    textView4.setText(LocaleController.getString("DeleteMegaMenu", 2131625434));
                }
            } else if (tLRPC$Chat != null) {
                if (ChatObject.isChannel(tLRPC$Chat)) {
                    if (tLRPC$Chat.megagroup) {
                        textView4.setText(LocaleController.getString("LeaveMegaMenu", 2131626442));
                    } else {
                        textView4.setText(LocaleController.getString("LeaveChannelMenu", 2131626434));
                    }
                } else {
                    textView4.setText(LocaleController.getString("LeaveMegaMenu", 2131626442));
                }
            } else {
                textView4.setText(LocaleController.getString("DeleteChatUser", 2131625413));
            }
        } else if (z11) {
            checkBoxCellArr = checkBoxCellArr2;
            z7 = z11;
            textView4.setText(LocaleController.getString("ClearHistoryCache", 2131625155));
        } else {
            checkBoxCellArr = checkBoxCellArr2;
            z7 = z11;
            textView4.setText(LocaleController.getString("ClearHistory", 2131625154));
        }
        boolean z12 = LocaleController.isRTL;
        anonymousClass3.addView(textView4, LayoutHelper.createFrame(-1, -2.0f, (z12 ? 5 : 3) | 48, z12 ? 21 : 76, 11.0f, z12 ? 76 : 21, 0.0f));
        anonymousClass3.addView(textView3, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 57.0f, 24.0f, 9.0f));
        if (tLRPC$User == null || tLRPC$User.bot) {
            str = "DeleteChatUser";
        } else {
            str = "DeleteChatUser";
            if (tLRPC$User.id != clientUserId && MessagesController.getInstance(currentAccount).canRevokePmInbox) {
                z8 = true;
                if (tLRPC$User == null) {
                    i = MessagesController.getInstance(currentAccount).revokeTimePmLimit;
                } else {
                    i = MessagesController.getInstance(currentAccount).revokeTimeLimit;
                }
                boolean z13 = z4 && tLRPC$User != null && z8 && i == Integer.MAX_VALUE;
                boolean[] zArr = new boolean[1];
                if (tLRPC$User == null) {
                    textView = textView3;
                    messageObject = MessagesController.getInstance(currentAccount).dialogMessage.get(tLRPC$User.id);
                } else {
                    textView = textView3;
                    messageObject = null;
                }
                if (messageObject != null && (tLRPC$Message = messageObject.messageOwner) != null) {
                    tLRPC$MessageAction = tLRPC$Message.action;
                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                        z9 = true;
                        if (!z3 || (((!z4 || z) && !z13) || UserObject.isDeleted(tLRPC$User) || z9)) {
                            z10 = !z5 && !z && tLRPC$Chat != null && tLRPC$Chat.creator;
                            if (!z10) {
                                str2 = str;
                                if (tLRPC$User != null) {
                                    if (UserObject.isReplyUser(tLRPC$User)) {
                                        avatarDrawable.setSmallSize(true);
                                        avatarDrawable.setAvatarType(12);
                                        backupImageView.setImage((ImageLocation) null, (String) null, avatarDrawable, tLRPC$User);
                                    } else if (tLRPC$User.id == clientUserId) {
                                        avatarDrawable.setSmallSize(true);
                                        avatarDrawable.setAvatarType(1);
                                        backupImageView.setImage((ImageLocation) null, (String) null, avatarDrawable, tLRPC$User);
                                    } else {
                                        avatarDrawable.setSmallSize(false);
                                        avatarDrawable.setInfo(tLRPC$User);
                                        backupImageView.setForUserOrChat(tLRPC$User, avatarDrawable);
                                    }
                                } else {
                                    avatarDrawable.setInfo(tLRPC$Chat);
                                    backupImageView.setForUserOrChat(tLRPC$Chat, avatarDrawable);
                                }
                                if (!z3) {
                                    TextView textView5 = textView;
                                    if (z) {
                                        if (tLRPC$User != null) {
                                            if (z4) {
                                                textView5.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithSecretUser", 2131624444, UserObject.getUserName(tLRPC$User))));
                                            } else if (tLRPC$User.id == clientUserId) {
                                                textView5.setText(AndroidUtilities.replaceTags(LocaleController.getString("AreYouSureClearHistorySavedMessages", 2131624441)));
                                            } else {
                                                textView5.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithUser", 2131624445, UserObject.getUserName(tLRPC$User))));
                                            }
                                        } else if (!ChatObject.isChannel(tLRPC$Chat) || (tLRPC$Chat.megagroup && TextUtils.isEmpty(tLRPC$Chat.username))) {
                                            textView5.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithChat", 2131624443, tLRPC$Chat.title)));
                                        } else if (tLRPC$Chat.megagroup) {
                                            textView5.setText(LocaleController.getString("AreYouSureClearHistoryGroup", 2131624440));
                                        } else {
                                            textView5.setText(LocaleController.getString("AreYouSureClearHistoryChannel", 2131624438));
                                        }
                                    } else if (z2) {
                                        if (ChatObject.isChannel(tLRPC$Chat)) {
                                            if (tLRPC$Chat.megagroup) {
                                                textView5.setText(LocaleController.getString("AreYouSureDeleteAndExit", 2131624446));
                                            } else {
                                                textView5.setText(LocaleController.getString("AreYouSureDeleteAndExitChannel", 2131624447));
                                            }
                                        } else {
                                            textView5.setText(LocaleController.getString("AreYouSureDeleteAndExit", 2131624446));
                                        }
                                    } else if (tLRPC$User != null) {
                                        if (z4) {
                                            textView5.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureDeleteThisChatWithSecretUser", 2131624465, UserObject.getUserName(tLRPC$User))));
                                        } else if (tLRPC$User.id == clientUserId) {
                                            textView5.setText(AndroidUtilities.replaceTags(LocaleController.getString("AreYouSureDeleteThisChatSavedMessages", 2131624462)));
                                        } else if (!tLRPC$User.bot || tLRPC$User.support) {
                                            textView5.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureDeleteThisChatWithUser", 2131624466, UserObject.getUserName(tLRPC$User))));
                                        } else {
                                            textView5.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureDeleteThisChatWithBot", 2131624463, UserObject.getUserName(tLRPC$User))));
                                        }
                                    } else if (!ChatObject.isChannel(tLRPC$Chat)) {
                                        textView5.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureDeleteAndExitName", 2131624448, tLRPC$Chat.title)));
                                    } else if (tLRPC$Chat.megagroup) {
                                        textView5.setText(AndroidUtilities.replaceTags(LocaleController.formatString("MegaLeaveAlertWithName", 2131626631, tLRPC$Chat.title)));
                                    } else {
                                        textView5.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ChannelLeaveAlertWithName", 2131624926, tLRPC$Chat.title)));
                                    }
                                } else if (UserObject.isUserSelf(tLRPC$User)) {
                                    textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("DeleteAllMessagesSavedAlert", 2131625395)));
                                } else {
                                    TextView textView6 = textView;
                                    if (tLRPC$Chat != null && ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat)) {
                                        textView6.setText(AndroidUtilities.replaceTags(LocaleController.getString("DeleteAllMessagesChannelAlert", 2131625394)));
                                    } else {
                                        textView6.setText(AndroidUtilities.replaceTags(LocaleController.getString("DeleteAllMessagesAlert", 2131625393)));
                                    }
                                }
                                if (z3) {
                                    string = LocaleController.getString("DeleteAll", 2131625389);
                                } else if (z) {
                                    if (z7) {
                                        string = LocaleController.getString("ClearHistoryCache", 2131625155);
                                    } else {
                                        string = LocaleController.getString("ClearForMe", 2131625153);
                                    }
                                } else if (z2) {
                                    if (ChatObject.isChannel(tLRPC$Chat)) {
                                        if (tLRPC$Chat.megagroup) {
                                            string = LocaleController.getString("DeleteMega", 2131625433);
                                        } else {
                                            string = LocaleController.getString("ChannelDelete", 2131624908);
                                        }
                                    } else {
                                        string = LocaleController.getString("DeleteMega", 2131625433);
                                    }
                                } else if (ChatObject.isChannel(tLRPC$Chat)) {
                                    if (tLRPC$Chat.megagroup) {
                                        string = LocaleController.getString("LeaveMegaMenu", 2131626442);
                                    } else {
                                        string = LocaleController.getString("LeaveChannelMenu", 2131626434);
                                    }
                                } else {
                                    string = LocaleController.getString(str2, 2131625413);
                                }
                                builder.setPositiveButton(string, new AlertsCreator$$ExternalSyntheticLambda34(z7, z3, z4, tLRPC$User, baseFragment, z, z2, tLRPC$Chat, z5, z6, booleanCallback, resourcesProvider, zArr));
                                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
                                AlertDialog create = builder.create();
                                baseFragment.showDialog(create);
                                textView2 = (TextView) create.getButton(-1);
                                if (textView2 == null) {
                                    return;
                                }
                                textView2.setTextColor(Theme.getColor("dialogTextRed2"));
                                return;
                            }
                        } else {
                            z10 = false;
                        }
                        checkBoxCellArr[0] = new CheckBoxCell(parentActivity, 1, resourcesProvider);
                        checkBoxCellArr[0].setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        if (!z10) {
                            if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                                checkBoxCellArr[0].setText(LocaleController.getString("DeleteChannelForAll", 2131625410), "", false, false);
                            } else {
                                checkBoxCellArr[0].setText(LocaleController.getString("DeleteGroupForAll", 2131625427), "", false, false);
                            }
                            str2 = str;
                        } else if (z) {
                            str2 = str;
                            checkBoxCellArr[0].setText(LocaleController.formatString("ClearHistoryOptionAlso", 2131625158, UserObject.getFirstName(tLRPC$User)), "", false, false);
                        } else {
                            str2 = str;
                            checkBoxCellArr[0].setText(LocaleController.formatString("DeleteMessagesOptionAlso", 2131625436, UserObject.getFirstName(tLRPC$User)), "", false, false);
                        }
                        CheckBoxCell checkBoxCell = checkBoxCellArr[0];
                        if (!LocaleController.isRTL) {
                            f = 16.0f;
                            i2 = AndroidUtilities.dp(16.0f);
                        } else {
                            f = 16.0f;
                            i2 = AndroidUtilities.dp(8.0f);
                        }
                        checkBoxCell.setPadding(i2, 0, !LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(f), 0);
                        anonymousClass3.addView(checkBoxCellArr[0], LayoutHelper.createFrame(-1, 48.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
                        checkBoxCellArr[0].setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda68(zArr));
                        if (tLRPC$User != null) {
                        }
                        if (!z3) {
                        }
                        if (z3) {
                        }
                        builder.setPositiveButton(string, new AlertsCreator$$ExternalSyntheticLambda34(z7, z3, z4, tLRPC$User, baseFragment, z, z2, tLRPC$Chat, z5, z6, booleanCallback, resourcesProvider, zArr));
                        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
                        AlertDialog create2 = builder.create();
                        baseFragment.showDialog(create2);
                        textView2 = (TextView) create2.getButton(-1);
                        if (textView2 == null) {
                        }
                    }
                }
                z9 = false;
                if (!z3) {
                }
                if (!z5) {
                }
                if (!z10) {
                }
                checkBoxCellArr[0] = new CheckBoxCell(parentActivity, 1, resourcesProvider);
                checkBoxCellArr[0].setBackgroundDrawable(Theme.getSelectorDrawable(false));
                if (!z10) {
                }
                CheckBoxCell checkBoxCell2 = checkBoxCellArr[0];
                if (!LocaleController.isRTL) {
                }
                checkBoxCell2.setPadding(i2, 0, !LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(f), 0);
                anonymousClass3.addView(checkBoxCellArr[0], LayoutHelper.createFrame(-1, 48.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
                checkBoxCellArr[0].setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda68(zArr));
                if (tLRPC$User != null) {
                }
                if (!z3) {
                }
                if (z3) {
                }
                builder.setPositiveButton(string, new AlertsCreator$$ExternalSyntheticLambda34(z7, z3, z4, tLRPC$User, baseFragment, z, z2, tLRPC$Chat, z5, z6, booleanCallback, resourcesProvider, zArr));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
                AlertDialog create22 = builder.create();
                baseFragment.showDialog(create22);
                textView2 = (TextView) create22.getButton(-1);
                if (textView2 == null) {
                }
            }
        }
        z8 = false;
        if (tLRPC$User == null) {
        }
        if (z4) {
        }
        boolean[] zArr2 = new boolean[1];
        if (tLRPC$User == null) {
        }
        if (messageObject != null) {
            tLRPC$MessageAction = tLRPC$Message.action;
            if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined)) {
            }
            z9 = true;
            if (!z3) {
            }
            if (!z5) {
            }
            if (!z10) {
            }
            checkBoxCellArr[0] = new CheckBoxCell(parentActivity, 1, resourcesProvider);
            checkBoxCellArr[0].setBackgroundDrawable(Theme.getSelectorDrawable(false));
            if (!z10) {
            }
            CheckBoxCell checkBoxCell22 = checkBoxCellArr[0];
            if (!LocaleController.isRTL) {
            }
            checkBoxCell22.setPadding(i2, 0, !LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(f), 0);
            anonymousClass3.addView(checkBoxCellArr[0], LayoutHelper.createFrame(-1, 48.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
            checkBoxCellArr[0].setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda68(zArr2));
            if (tLRPC$User != null) {
            }
            if (!z3) {
            }
            if (z3) {
            }
            builder.setPositiveButton(string, new AlertsCreator$$ExternalSyntheticLambda34(z7, z3, z4, tLRPC$User, baseFragment, z, z2, tLRPC$Chat, z5, z6, booleanCallback, resourcesProvider, zArr2));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
            AlertDialog create222 = builder.create();
            baseFragment.showDialog(create222);
            textView2 = (TextView) create222.getButton(-1);
            if (textView2 == null) {
            }
        }
        z9 = false;
        if (!z3) {
        }
        if (!z5) {
        }
        if (!z10) {
        }
        checkBoxCellArr[0] = new CheckBoxCell(parentActivity, 1, resourcesProvider);
        checkBoxCellArr[0].setBackgroundDrawable(Theme.getSelectorDrawable(false));
        if (!z10) {
        }
        CheckBoxCell checkBoxCell222 = checkBoxCellArr[0];
        if (!LocaleController.isRTL) {
        }
        checkBoxCell222.setPadding(i2, 0, !LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(f), 0);
        anonymousClass3.addView(checkBoxCellArr[0], LayoutHelper.createFrame(-1, 48.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
        checkBoxCellArr[0].setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda68(zArr2));
        if (tLRPC$User != null) {
        }
        if (!z3) {
        }
        if (z3) {
        }
        builder.setPositiveButton(string, new AlertsCreator$$ExternalSyntheticLambda34(z7, z3, z4, tLRPC$User, baseFragment, z, z2, tLRPC$Chat, z5, z6, booleanCallback, resourcesProvider, zArr2));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        AlertDialog create2222 = builder.create();
        baseFragment.showDialog(create2222);
        textView2 = (TextView) create2222.getButton(-1);
        if (textView2 == null) {
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends FrameLayout {
        final /* synthetic */ CheckBoxCell[] val$cell;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context, CheckBoxCell[] checkBoxCellArr) {
            super(context);
            this.val$cell = checkBoxCellArr;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            if (this.val$cell[0] != null) {
                setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + this.val$cell[0].getMeasuredHeight() + AndroidUtilities.dp(7.0f));
            }
        }
    }

    public static /* synthetic */ void lambda$createClearOrDeleteDialogAlert$23(boolean[] zArr, View view) {
        zArr[0] = !zArr[0];
        ((CheckBoxCell) view).setChecked(zArr[0], true);
    }

    public static /* synthetic */ void lambda$createClearOrDeleteDialogAlert$25(boolean z, boolean z2, boolean z3, TLRPC$User tLRPC$User, BaseFragment baseFragment, boolean z4, boolean z5, TLRPC$Chat tLRPC$Chat, boolean z6, boolean z7, MessagesStorage.BooleanCallback booleanCallback, Theme.ResourcesProvider resourcesProvider, boolean[] zArr, DialogInterface dialogInterface, int i) {
        boolean z8 = false;
        if (!z && !z2 && !z3) {
            if (UserObject.isUserSelf(tLRPC$User)) {
                createClearOrDeleteDialogAlert(baseFragment, z4, z5, true, tLRPC$Chat, tLRPC$User, false, z6, z7, booleanCallback, resourcesProvider);
                return;
            } else if (tLRPC$User != null && zArr[0]) {
                MessagesStorage.getInstance(baseFragment.getCurrentAccount()).getMessagesCount(tLRPC$User.id, new AlertsCreator$$ExternalSyntheticLambda92(baseFragment, z4, z5, tLRPC$Chat, tLRPC$User, z6, z7, booleanCallback, resourcesProvider, zArr));
                return;
            }
        }
        if (booleanCallback != null) {
            if (z2 || zArr[0]) {
                z8 = true;
            }
            booleanCallback.run(z8);
        }
    }

    public static /* synthetic */ void lambda$createClearOrDeleteDialogAlert$24(BaseFragment baseFragment, boolean z, boolean z2, TLRPC$Chat tLRPC$Chat, TLRPC$User tLRPC$User, boolean z3, boolean z4, MessagesStorage.BooleanCallback booleanCallback, Theme.ResourcesProvider resourcesProvider, boolean[] zArr, int i) {
        if (i >= 50) {
            createClearOrDeleteDialogAlert(baseFragment, z, z2, true, tLRPC$Chat, tLRPC$User, false, z3, z4, booleanCallback, resourcesProvider);
        } else if (booleanCallback == null) {
        } else {
            booleanCallback.run(zArr[0]);
        }
    }

    public static void createClearDaysDialogAlert(BaseFragment baseFragment, int i, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, boolean z, MessagesStorage.BooleanCallback booleanCallback, Theme.ResourcesProvider resourcesProvider) {
        int i2;
        float f;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        if (tLRPC$User == null && tLRPC$Chat == null) {
            return;
        }
        int currentAccount = baseFragment.getCurrentAccount();
        Activity parentActivity = baseFragment.getParentActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity, resourcesProvider);
        long clientUserId = UserConfig.getInstance(currentAccount).getClientUserId();
        CheckBoxCell[] checkBoxCellArr = new CheckBoxCell[1];
        TextView textView = new TextView(parentActivity);
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        textView.setTextSize(1, 16.0f);
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(parentActivity, checkBoxCellArr);
        builder.setView(anonymousClass4);
        TextView textView2 = new TextView(parentActivity);
        textView2.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
        textView2.setTextSize(1, 20.0f);
        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView2.setLines(1);
        textView2.setMaxLines(1);
        textView2.setSingleLine(true);
        textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        textView2.setEllipsize(TextUtils.TruncateAt.END);
        anonymousClass4.addView(textView2, LayoutHelper.createFrame(-1, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 11.0f, 24.0f, 0.0f));
        anonymousClass4.addView(textView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 48.0f, 24.0f, 18.0f));
        if (i == -1) {
            textView2.setText(LocaleController.formatString("ClearHistory", 2131625154, new Object[0]));
            if (tLRPC$User != null) {
                textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithUser", 2131624445, UserObject.getUserName(tLRPC$User))));
            } else if (z) {
                if (ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat)) {
                    textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithChannel", 2131624442, tLRPC$Chat.title)));
                } else {
                    textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithChat", 2131624443, tLRPC$Chat.title)));
                }
            } else if (tLRPC$Chat.megagroup) {
                textView.setText(LocaleController.getString("AreYouSureClearHistoryGroup", 2131624440));
            } else {
                textView.setText(LocaleController.getString("AreYouSureClearHistoryChannel", 2131624438));
            }
        } else {
            textView2.setText(LocaleController.formatPluralString("DeleteDays", i, new Object[0]));
            textView.setText(LocaleController.getString("DeleteHistoryByDaysMessage", 2131625428));
        }
        boolean[] zArr = {false};
        if (tLRPC$Chat != null && z && !TextUtils.isEmpty(tLRPC$Chat.username)) {
            zArr[0] = true;
        }
        if ((tLRPC$User != null && tLRPC$User.id != clientUserId) || (tLRPC$Chat != null && z && TextUtils.isEmpty(tLRPC$Chat.username) && !ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat))) {
            checkBoxCellArr[0] = new CheckBoxCell(parentActivity, 1, resourcesProvider);
            checkBoxCellArr[0].setBackgroundDrawable(Theme.getSelectorDrawable(false));
            if (tLRPC$Chat != null) {
                checkBoxCellArr[0].setText(LocaleController.getString("DeleteMessagesOptionAlsoChat", 2131625437), "", false, false);
            } else {
                checkBoxCellArr[0].setText(LocaleController.formatString("DeleteMessagesOptionAlso", 2131625436, UserObject.getFirstName(tLRPC$User)), "", false, false);
            }
            CheckBoxCell checkBoxCell = checkBoxCellArr[0];
            if (LocaleController.isRTL) {
                f = 16.0f;
                i2 = AndroidUtilities.dp(16.0f);
            } else {
                f = 16.0f;
                i2 = AndroidUtilities.dp(8.0f);
            }
            checkBoxCell.setPadding(i2, 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(f), 0);
            anonymousClass4.addView(checkBoxCellArr[0], LayoutHelper.createFrame(-1, 48.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
            checkBoxCellArr[0].setChecked(false, false);
            checkBoxCellArr[0].setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda70(zArr));
        }
        CharSequence string = LocaleController.getString("Delete", 2131625384);
        if (tLRPC$Chat != null && z && !TextUtils.isEmpty(tLRPC$Chat.username) && !ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat)) {
            string = LocaleController.getString("ClearForAll", 2131625152);
        }
        builder.setPositiveButton(string, new AlertsCreator$$ExternalSyntheticLambda18(booleanCallback, zArr));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        AlertDialog create = builder.create();
        baseFragment.showDialog(create);
        TextView textView3 = (TextView) create.getButton(-1);
        if (textView3 == null) {
            return;
        }
        textView3.setTextColor(Theme.getColor("dialogTextRed2"));
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends FrameLayout {
        final /* synthetic */ CheckBoxCell[] val$cell;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context, CheckBoxCell[] checkBoxCellArr) {
            super(context);
            this.val$cell = checkBoxCellArr;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            if (this.val$cell[0] != null) {
                setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + this.val$cell[0].getMeasuredHeight());
            }
        }
    }

    public static /* synthetic */ void lambda$createClearDaysDialogAlert$26(boolean[] zArr, View view) {
        zArr[0] = !zArr[0];
        ((CheckBoxCell) view).setChecked(zArr[0], true);
    }

    public static /* synthetic */ void lambda$createClearDaysDialogAlert$27(MessagesStorage.BooleanCallback booleanCallback, boolean[] zArr, DialogInterface dialogInterface, int i) {
        booleanCallback.run(zArr[0]);
    }

    public static void createCallDialogAlert(BaseFragment baseFragment, TLRPC$User tLRPC$User, boolean z) {
        String str;
        String str2;
        if (baseFragment == null || baseFragment.getParentActivity() == null || tLRPC$User == null || UserObject.isDeleted(tLRPC$User) || UserConfig.getInstance(baseFragment.getCurrentAccount()).getClientUserId() == tLRPC$User.id) {
            return;
        }
        baseFragment.getCurrentAccount();
        Activity parentActivity = baseFragment.getParentActivity();
        FrameLayout frameLayout = new FrameLayout(parentActivity);
        if (z) {
            str2 = LocaleController.getString("VideoCallAlertTitle", 2131628956);
            str = LocaleController.formatString("VideoCallAlert", 2131628955, UserObject.getUserName(tLRPC$User));
        } else {
            str2 = LocaleController.getString("CallAlertTitle", 2131624795);
            str = LocaleController.formatString("CallAlert", 2131624794, UserObject.getUserName(tLRPC$User));
        }
        TextView textView = new TextView(parentActivity);
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        textView.setTextSize(1, 16.0f);
        int i = 5;
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        textView.setText(AndroidUtilities.replaceTags(str));
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        avatarDrawable.setSmallSize(false);
        avatarDrawable.setInfo(tLRPC$User);
        BackupImageView backupImageView = new BackupImageView(parentActivity);
        backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
        backupImageView.setForUserOrChat(tLRPC$User, avatarDrawable);
        frameLayout.addView(backupImageView, LayoutHelper.createFrame(40, 40.0f, (LocaleController.isRTL ? 5 : 3) | 48, 22.0f, 5.0f, 22.0f, 0.0f));
        TextView textView2 = new TextView(parentActivity);
        textView2.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
        textView2.setTextSize(1, 20.0f);
        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView2.setLines(1);
        textView2.setMaxLines(1);
        textView2.setSingleLine(true);
        textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        textView2.setEllipsize(TextUtils.TruncateAt.END);
        textView2.setText(str2);
        boolean z2 = LocaleController.isRTL;
        int i2 = (z2 ? 5 : 3) | 48;
        int i3 = 21;
        float f = z2 ? 21 : 76;
        if (z2) {
            i3 = 76;
        }
        frameLayout.addView(textView2, LayoutHelper.createFrame(-1, -2.0f, i2, f, 11.0f, i3, 0.0f));
        if (!LocaleController.isRTL) {
            i = 3;
        }
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, i | 48, 24.0f, 57.0f, 24.0f, 9.0f));
        baseFragment.showDialog(new AlertDialog.Builder(parentActivity).setView(frameLayout).setPositiveButton(LocaleController.getString("Call", 2131624792), new AlertsCreator$$ExternalSyntheticLambda28(baseFragment, tLRPC$User, z)).setNegativeButton(LocaleController.getString("Cancel", 2131624832), null).create());
    }

    public static /* synthetic */ void lambda$createCallDialogAlert$28(BaseFragment baseFragment, TLRPC$User tLRPC$User, boolean z, DialogInterface dialogInterface, int i) {
        TLRPC$UserFull userFull = baseFragment.getMessagesController().getUserFull(tLRPC$User.id);
        VoIPHelper.startCall(tLRPC$User, z, userFull != null && userFull.video_calls_available, baseFragment.getParentActivity(), userFull, baseFragment.getAccountInstance());
    }

    public static void createChangeBioAlert(String str, long j, Context context, int i) {
        String str2;
        int i2;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(j > 0 ? LocaleController.getString("UserBio", 2131628875) : LocaleController.getString("DescriptionPlaceholder", 2131625479));
        if (j > 0) {
            i2 = 2131629110;
            str2 = "VoipGroupBioEditAlertText";
        } else {
            i2 = 2131625476;
            str2 = "DescriptionInfo";
        }
        builder.setMessage(LocaleController.getString(str2, i2));
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setClipChildren(false);
        if (j < 0) {
            long j2 = -j;
            if (MessagesController.getInstance(i).getChatFull(j2) == null) {
                MessagesController.getInstance(i).loadFullChat(j2, ConnectionsManager.generateClassGuid(), true);
            }
        }
        NumberTextView numberTextView = new NumberTextView(context);
        EditText editText = new EditText(context);
        editText.setTextColor(Theme.getColor("voipgroup_actionBarItems"));
        editText.setHint(j > 0 ? LocaleController.getString("UserBio", 2131628875) : LocaleController.getString("DescriptionPlaceholder", 2131625479));
        editText.setTextSize(1, 16.0f);
        editText.setBackground(Theme.createEditTextDrawable(context, true));
        editText.setMaxLines(4);
        editText.setRawInputType(147457);
        editText.setImeOptions(6);
        InputFilter[] inputFilterArr = new InputFilter[1];
        int i3 = j > 0 ? 70 : 255;
        inputFilterArr[0] = new AnonymousClass5(i3, context, numberTextView);
        editText.setFilters(inputFilterArr);
        numberTextView.setCenterAlign(true);
        numberTextView.setTextSize(15);
        numberTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
        numberTextView.setImportantForAccessibility(2);
        frameLayout.addView(numberTextView, LayoutHelper.createFrame(20, 20.0f, LocaleController.isRTL ? 3 : 5, 0.0f, 14.0f, 21.0f, 0.0f));
        editText.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(24.0f) : 0, AndroidUtilities.dp(8.0f), LocaleController.isRTL ? 0 : AndroidUtilities.dp(24.0f), AndroidUtilities.dp(8.0f));
        editText.addTextChangedListener(new AnonymousClass6(i3, numberTextView));
        AndroidUtilities.updateViewVisibilityAnimated(numberTextView, false, 0.0f, false);
        editText.setText(str);
        editText.setSelection(editText.getText().toString().length());
        builder.setView(frameLayout);
        DialogInterface.OnClickListener alertsCreator$$ExternalSyntheticLambda1 = new AlertsCreator$$ExternalSyntheticLambda1(j, i, editText);
        builder.setPositiveButton(LocaleController.getString("Save", 2131628123), alertsCreator$$ExternalSyntheticLambda1);
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        builder.setOnPreDismissListener(new AlertsCreator$$ExternalSyntheticLambda39(editText));
        frameLayout.addView(editText, LayoutHelper.createFrame(-1, -2.0f, 0, 23.0f, 12.0f, 23.0f, 21.0f));
        editText.requestFocus();
        AndroidUtilities.showKeyboard(editText);
        AlertDialog create = builder.create();
        editText.setOnEditorActionListener(new AlertsCreator$$ExternalSyntheticLambda82(j, create, alertsCreator$$ExternalSyntheticLambda1));
        create.setBackgroundColor(Theme.getColor("voipgroup_dialogBackground"));
        create.show();
        create.setTextColor(Theme.getColor("voipgroup_actionBarItems"));
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends CodepointsLengthInputFilter {
        final /* synthetic */ NumberTextView val$checkTextView;
        final /* synthetic */ Context val$context;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(int i, Context context, NumberTextView numberTextView) {
            super(i);
            this.val$context = context;
            this.val$checkTextView = numberTextView;
        }

        @Override // org.telegram.ui.Components.CodepointsLengthInputFilter, android.text.InputFilter
        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            CharSequence filter = super.filter(charSequence, i, i2, spanned, i3, i4);
            if (filter != null && charSequence != null && filter.length() != charSequence.length()) {
                Vibrator vibrator = (Vibrator) this.val$context.getSystemService("vibrator");
                if (vibrator != null) {
                    vibrator.vibrate(200L);
                }
                AndroidUtilities.shakeView(this.val$checkTextView, 2.0f, 0);
            }
            return filter;
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 implements TextWatcher {
        final /* synthetic */ NumberTextView val$checkTextView;
        final /* synthetic */ int val$maxSymbolsCount;

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass6(int i, NumberTextView numberTextView) {
            this.val$maxSymbolsCount = i;
            this.val$checkTextView = numberTextView;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            boolean z = false;
            int codePointCount = this.val$maxSymbolsCount - Character.codePointCount(editable, 0, editable.length());
            if (codePointCount < 30) {
                NumberTextView numberTextView = this.val$checkTextView;
                if (numberTextView.getVisibility() == 0) {
                    z = true;
                }
                numberTextView.setNumber(codePointCount, z);
                AndroidUtilities.updateViewVisibilityAnimated(this.val$checkTextView, true);
                return;
            }
            AndroidUtilities.updateViewVisibilityAnimated(this.val$checkTextView, false);
        }
    }

    public static /* synthetic */ void lambda$createChangeBioAlert$30(long j, int i, EditText editText, DialogInterface dialogInterface, int i2) {
        String str = "";
        if (j > 0) {
            TLRPC$UserFull userFull = MessagesController.getInstance(i).getUserFull(UserConfig.getInstance(i).getClientUserId());
            String trim = editText.getText().toString().replace("\n", " ").replaceAll(" +", " ").trim();
            if (userFull != null) {
                String str2 = userFull.about;
                if (str2 != null) {
                    str = str2;
                }
                if (str.equals(trim)) {
                    AndroidUtilities.hideKeyboard(editText);
                    dialogInterface.dismiss();
                    return;
                }
                userFull.about = trim;
                NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.userInfoDidLoad, Long.valueOf(j), userFull);
            }
            TLRPC$TL_account_updateProfile tLRPC$TL_account_updateProfile = new TLRPC$TL_account_updateProfile();
            tLRPC$TL_account_updateProfile.about = trim;
            tLRPC$TL_account_updateProfile.flags = 4 | tLRPC$TL_account_updateProfile.flags;
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.showBulletin, 2, Long.valueOf(j));
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_updateProfile, AlertsCreator$$ExternalSyntheticLambda98.INSTANCE, 2);
        } else {
            long j2 = -j;
            TLRPC$ChatFull chatFull = MessagesController.getInstance(i).getChatFull(j2);
            String obj = editText.getText().toString();
            if (chatFull != null) {
                String str3 = chatFull.about;
                if (str3 != null) {
                    str = str3;
                }
                if (str.equals(obj)) {
                    AndroidUtilities.hideKeyboard(editText);
                    dialogInterface.dismiss();
                    return;
                }
                chatFull.about = obj;
                NotificationCenter notificationCenter = NotificationCenter.getInstance(i);
                int i3 = NotificationCenter.chatInfoDidLoad;
                Boolean bool = Boolean.FALSE;
                notificationCenter.postNotificationName(i3, chatFull, 0, bool, bool);
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.showBulletin, 2, Long.valueOf(j));
            MessagesController.getInstance(i).updateChatAbout(j2, obj, chatFull);
        }
        dialogInterface.dismiss();
    }

    public static /* synthetic */ boolean lambda$createChangeBioAlert$32(long j, AlertDialog alertDialog, DialogInterface.OnClickListener onClickListener, TextView textView, int i, KeyEvent keyEvent) {
        if ((i == 6 || (j > 0 && keyEvent.getKeyCode() == 66)) && alertDialog.isShowing()) {
            onClickListener.onClick(alertDialog, 0);
            return true;
        }
        return false;
    }

    public static void createChangeNameAlert(long j, Context context, int i) {
        String str;
        String str2;
        String str3;
        int i2;
        String str4;
        int i3;
        EditText editText;
        if (DialogObject.isUserDialog(j)) {
            TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
            str = user.first_name;
            str2 = user.last_name;
        } else {
            str = MessagesController.getInstance(i).getChat(Long.valueOf(-j)).title;
            str2 = null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (j > 0) {
            i2 = 2131629092;
            str3 = "VoipEditName";
        } else {
            i2 = 2131629093;
            str3 = "VoipEditTitle";
        }
        builder.setTitle(LocaleController.getString(str3, i2));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        EditText editText2 = new EditText(context);
        editText2.setTextColor(Theme.getColor("voipgroup_actionBarItems"));
        editText2.setTextSize(1, 16.0f);
        editText2.setMaxLines(1);
        editText2.setLines(1);
        editText2.setSingleLine(true);
        editText2.setGravity(LocaleController.isRTL ? 5 : 3);
        editText2.setInputType(49152);
        editText2.setImeOptions(j > 0 ? 5 : 6);
        if (j > 0) {
            i3 = 2131625947;
            str4 = "FirstName";
        } else {
            i3 = 2131629094;
            str4 = "VoipEditTitleHint";
        }
        editText2.setHint(LocaleController.getString(str4, i3));
        editText2.setBackground(Theme.createEditTextDrawable(context, true));
        editText2.setPadding(0, AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f));
        editText2.requestFocus();
        if (j > 0) {
            editText = new EditText(context);
            editText.setTextColor(Theme.getColor("voipgroup_actionBarItems"));
            editText.setTextSize(1, 16.0f);
            editText.setMaxLines(1);
            editText.setLines(1);
            editText.setSingleLine(true);
            editText.setGravity(LocaleController.isRTL ? 5 : 3);
            editText.setInputType(49152);
            editText.setImeOptions(6);
            editText.setHint(LocaleController.getString("LastName", 2131626415));
            editText.setBackground(Theme.createEditTextDrawable(context, true));
            editText.setPadding(0, AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f));
        } else {
            editText = null;
        }
        AndroidUtilities.showKeyboard(editText2);
        linearLayout.addView(editText2, LayoutHelper.createLinear(-1, -2, 0, 23, 12, 23, 21));
        if (editText != null) {
            linearLayout.addView(editText, LayoutHelper.createLinear(-1, -2, 0, 23, 12, 23, 21));
        }
        editText2.setText(str);
        editText2.setSelection(editText2.getText().toString().length());
        if (editText != null) {
            editText.setText(str2);
            editText.setSelection(editText.getText().toString().length());
        }
        builder.setView(linearLayout);
        DialogInterface.OnClickListener alertsCreator$$ExternalSyntheticLambda13 = new AlertsCreator$$ExternalSyntheticLambda13(editText2, j, i, editText);
        builder.setPositiveButton(LocaleController.getString("Save", 2131628123), alertsCreator$$ExternalSyntheticLambda13);
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        builder.setOnPreDismissListener(new AlertsCreator$$ExternalSyntheticLambda40(editText2, editText));
        AlertDialog create = builder.create();
        create.setBackgroundColor(Theme.getColor("voipgroup_dialogBackground"));
        create.show();
        create.setTextColor(Theme.getColor("voipgroup_actionBarItems"));
        AlertsCreator$$ExternalSyntheticLambda83 alertsCreator$$ExternalSyntheticLambda83 = new AlertsCreator$$ExternalSyntheticLambda83(create, alertsCreator$$ExternalSyntheticLambda13);
        if (editText != null) {
            editText.setOnEditorActionListener(alertsCreator$$ExternalSyntheticLambda83);
        } else {
            editText2.setOnEditorActionListener(alertsCreator$$ExternalSyntheticLambda83);
        }
    }

    public static /* synthetic */ void lambda$createChangeNameAlert$34(EditText editText, long j, int i, EditText editText2, DialogInterface dialogInterface, int i2) {
        if (editText.getText() == null) {
            return;
        }
        if (j > 0) {
            TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
            String obj = editText.getText().toString();
            String obj2 = editText2.getText().toString();
            String str = user.first_name;
            String str2 = user.last_name;
            if (str == null) {
                str = "";
            }
            if (str2 == null) {
                str2 = "";
            }
            if (str.equals(obj) && str2.equals(obj2)) {
                dialogInterface.dismiss();
                return;
            }
            TLRPC$TL_account_updateProfile tLRPC$TL_account_updateProfile = new TLRPC$TL_account_updateProfile();
            tLRPC$TL_account_updateProfile.flags = 3;
            tLRPC$TL_account_updateProfile.first_name = obj;
            user.first_name = obj;
            tLRPC$TL_account_updateProfile.last_name = obj2;
            user.last_name = obj2;
            TLRPC$User user2 = MessagesController.getInstance(i).getUser(Long.valueOf(UserConfig.getInstance(i).getClientUserId()));
            if (user2 != null) {
                user2.first_name = tLRPC$TL_account_updateProfile.first_name;
                user2.last_name = tLRPC$TL_account_updateProfile.last_name;
            }
            UserConfig.getInstance(i).saveConfig(true);
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_NAME));
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_updateProfile, AlertsCreator$$ExternalSyntheticLambda100.INSTANCE);
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.showBulletin, 3, Long.valueOf(j));
        } else {
            long j2 = -j;
            TLRPC$Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(j2));
            String obj3 = editText.getText().toString();
            String str3 = chat.title;
            if (str3 != null && str3.equals(obj3)) {
                dialogInterface.dismiss();
                return;
            }
            chat.title = obj3;
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_CHAT_NAME));
            MessagesController.getInstance(i).changeChatTitle(j2, obj3);
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.showBulletin, 3, Long.valueOf(j));
        }
        dialogInterface.dismiss();
    }

    public static /* synthetic */ void lambda$createChangeNameAlert$35(EditText editText, EditText editText2, DialogInterface dialogInterface) {
        AndroidUtilities.hideKeyboard(editText);
        AndroidUtilities.hideKeyboard(editText2);
    }

    public static /* synthetic */ boolean lambda$createChangeNameAlert$36(AlertDialog alertDialog, DialogInterface.OnClickListener onClickListener, TextView textView, int i, KeyEvent keyEvent) {
        if ((i == 6 || keyEvent.getKeyCode() == 66) && alertDialog.isShowing()) {
            onClickListener.onClick(alertDialog, 0);
            return true;
        }
        return false;
    }

    public static void showChatWithAdmin(BaseFragment baseFragment, TLRPC$User tLRPC$User, String str, boolean z, int i) {
        int i2;
        String str2;
        if (baseFragment.getParentActivity() == null) {
            return;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(baseFragment.getParentActivity());
        if (z) {
            i2 = 2131625057;
            str2 = "ChatWithAdminChannelTitle";
        } else {
            i2 = 2131625058;
            str2 = "ChatWithAdminGroupTitle";
        }
        builder.setTitle(LocaleController.getString(str2, i2), true);
        LinearLayout linearLayout = new LinearLayout(baseFragment.getParentActivity());
        linearLayout.setOrientation(1);
        TextView textView = new TextView(baseFragment.getParentActivity());
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, 0, 24, 16, 24, 24));
        textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        textView.setTextSize(1, 16.0f);
        textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ChatWithAdminMessage", 2131625059, str, LocaleController.formatDateAudio(i, false))));
        TextView textView2 = new TextView(baseFragment.getParentActivity());
        textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        textView2.setGravity(17);
        textView2.setTextSize(1, 14.0f);
        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView2.setText(LocaleController.getString("IUnderstand", 2131626216));
        textView2.setTextColor(Theme.getColor("featuredStickers_buttonText"));
        textView2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed")));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, 48, 0, 24, 15, 16, 24));
        builder.setCustomView(linearLayout);
        textView2.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda54(builder.show()));
    }

    public static void createBlockDialogAlert(BaseFragment baseFragment, int i, boolean z, TLRPC$User tLRPC$User, BlockDialogCallback blockDialogCallback) {
        String str;
        String str2;
        int i2;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        if (i == 1 && tLRPC$User == null) {
            return;
        }
        Activity parentActivity = baseFragment.getParentActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        CheckBoxCell[] checkBoxCellArr = new CheckBoxCell[2];
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        builder.setView(linearLayout);
        if (i == 1) {
            String formatName = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
            builder.setTitle(LocaleController.formatString("BlockUserTitle", 2131624697, formatName));
            str = LocaleController.getString("BlockUser", 2131624690);
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BlockUserMessage", 2131624694, formatName)));
        } else {
            builder.setTitle(LocaleController.formatString("BlockUserTitle", 2131624697, LocaleController.formatPluralString("UsersCountTitle", i, new Object[0])));
            str = LocaleController.getString("BlockUsers", 2131624698);
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BlockUsersMessage", 2131624699, LocaleController.formatPluralString("UsersCount", i, new Object[0]))));
        }
        boolean[] zArr = {true, true};
        int i3 = 0;
        for (int i4 = 2; i3 < i4; i4 = 2) {
            if (i3 != 0 || z) {
                checkBoxCellArr[i3] = new CheckBoxCell(parentActivity, 1);
                checkBoxCellArr[i3].setBackgroundDrawable(Theme.getSelectorDrawable(false));
                if (i3 == 0) {
                    checkBoxCellArr[i3].setText(LocaleController.getString("ReportSpamTitle", 2131628016), "", true, false);
                } else {
                    CheckBoxCell checkBoxCell = checkBoxCellArr[i3];
                    if (i == 1) {
                        i2 = 2131625455;
                        str2 = "DeleteThisChatBothSides";
                    } else {
                        i2 = 2131625453;
                        str2 = "DeleteTheseChatsBothSides";
                    }
                    checkBoxCell.setText(LocaleController.getString(str2, i2), "", true, false);
                }
                checkBoxCellArr[i3].setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                linearLayout.addView(checkBoxCellArr[i3], LayoutHelper.createLinear(-1, 48));
                checkBoxCellArr[i3].setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda72(zArr, i3));
            }
            i3++;
        }
        builder.setPositiveButton(str, new AlertsCreator$$ExternalSyntheticLambda29(blockDialogCallback, zArr));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        AlertDialog create = builder.create();
        baseFragment.showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView == null) {
            return;
        }
        textView.setTextColor(Theme.getColor("dialogTextRed2"));
    }

    public static /* synthetic */ void lambda$createBlockDialogAlert$38(boolean[] zArr, int i, View view) {
        zArr[i] = !zArr[i];
        ((CheckBoxCell) view).setChecked(zArr[i], true);
    }

    public static /* synthetic */ void lambda$createBlockDialogAlert$39(BlockDialogCallback blockDialogCallback, boolean[] zArr, DialogInterface dialogInterface, int i) {
        blockDialogCallback.run(zArr[0], zArr[1]);
    }

    public static AlertDialog.Builder createDatePickerDialog(Context context, int i, int i2, int i3, int i4, int i5, int i6, String str, boolean z, DatePickerDelegate datePickerDelegate) {
        if (context == null) {
            return null;
        }
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setWeightSum(1.0f);
        NumberPicker numberPicker = new NumberPicker(context);
        NumberPicker numberPicker2 = new NumberPicker(context);
        NumberPicker numberPicker3 = new NumberPicker(context);
        linearLayout.addView(numberPicker2, LayoutHelper.createLinear(0, -2, 0.3f));
        numberPicker2.setOnScrollListener(new AlertsCreator$$ExternalSyntheticLambda119(z, numberPicker2, numberPicker, numberPicker3));
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(11);
        linearLayout.addView(numberPicker, LayoutHelper.createLinear(0, -2, 0.3f));
        numberPicker.setFormatter(AlertsCreator$$ExternalSyntheticLambda113.INSTANCE);
        numberPicker.setOnValueChangedListener(new AlertsCreator$$ExternalSyntheticLambda128(numberPicker2, numberPicker, numberPicker3));
        numberPicker.setOnScrollListener(new AlertsCreator$$ExternalSyntheticLambda118(z, numberPicker2, numberPicker, numberPicker3));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i7 = calendar.get(1);
        numberPicker3.setMinValue(i7 + i);
        numberPicker3.setMaxValue(i7 + i2);
        numberPicker3.setValue(i7 + i3);
        linearLayout.addView(numberPicker3, LayoutHelper.createLinear(0, -2, 0.4f));
        numberPicker3.setOnValueChangedListener(new AlertsCreator$$ExternalSyntheticLambda127(numberPicker2, numberPicker, numberPicker3));
        numberPicker3.setOnScrollListener(new AlertsCreator$$ExternalSyntheticLambda120(z, numberPicker2, numberPicker, numberPicker3));
        updateDayPicker(numberPicker2, numberPicker, numberPicker3);
        if (z) {
            checkPickerDate(numberPicker2, numberPicker, numberPicker3);
        }
        if (i4 != -1) {
            numberPicker2.setValue(i4);
            numberPicker.setValue(i5);
            numberPicker3.setValue(i6);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Set", 2131628297), new AlertsCreator$$ExternalSyntheticLambda33(z, numberPicker2, numberPicker, numberPicker3, datePickerDelegate));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        return builder;
    }

    public static /* synthetic */ void lambda$createDatePickerDialog$40(boolean z, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, NumberPicker numberPicker4, int i) {
        if (!z || i != 0) {
            return;
        }
        checkPickerDate(numberPicker, numberPicker2, numberPicker3);
    }

    public static /* synthetic */ String lambda$createDatePickerDialog$41(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, 1);
        calendar.set(2, i);
        return calendar.getDisplayName(2, 1, Locale.getDefault());
    }

    public static /* synthetic */ void lambda$createDatePickerDialog$43(boolean z, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, NumberPicker numberPicker4, int i) {
        if (!z || i != 0) {
            return;
        }
        checkPickerDate(numberPicker, numberPicker2, numberPicker3);
    }

    public static /* synthetic */ void lambda$createDatePickerDialog$45(boolean z, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, NumberPicker numberPicker4, int i) {
        if (!z || i != 0) {
            return;
        }
        checkPickerDate(numberPicker, numberPicker2, numberPicker3);
    }

    public static /* synthetic */ void lambda$createDatePickerDialog$46(boolean z, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, DatePickerDelegate datePickerDelegate, DialogInterface dialogInterface, int i) {
        if (z) {
            checkPickerDate(numberPicker, numberPicker2, numberPicker3);
        }
        datePickerDelegate.didSelectDate(numberPicker3.getValue(), numberPicker2.getValue(), numberPicker.getValue());
    }

    public static boolean checkScheduleDate(TextView textView, TextView textView2, int i, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        return checkScheduleDate(textView, textView2, 0L, i, numberPicker, numberPicker2, numberPicker3);
    }

    public static boolean checkScheduleDate(TextView textView, TextView textView2, long j, int i, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        int i2;
        long j2;
        int i3;
        int i4;
        String str;
        int i5;
        int i6;
        int i7;
        int value = numberPicker.getValue();
        int value2 = numberPicker2.getValue();
        int value3 = numberPicker3.getValue();
        Calendar calendar = Calendar.getInstance();
        long currentTimeMillis = System.currentTimeMillis();
        calendar.setTimeInMillis(currentTimeMillis);
        int i8 = calendar.get(1);
        int i9 = calendar.get(6);
        if (j > 0) {
            i2 = i8;
            calendar.setTimeInMillis(currentTimeMillis + (j * 1000));
            calendar.set(11, 23);
            calendar.set(12, 59);
            calendar.set(13, 59);
            j2 = calendar.getTimeInMillis();
        } else {
            i2 = i8;
            j2 = j;
        }
        calendar.setTimeInMillis(System.currentTimeMillis() + (value * 24 * 3600 * 1000));
        calendar.set(11, value2);
        calendar.set(12, value3);
        long timeInMillis = calendar.getTimeInMillis();
        long j3 = currentTimeMillis + 60000;
        if (timeInMillis <= j3) {
            calendar.setTimeInMillis(j3);
            if (i9 != calendar.get(6)) {
                numberPicker.setValue(1);
                i7 = 11;
                i4 = 1;
            } else {
                i4 = value;
                i7 = 11;
            }
            i3 = calendar.get(i7);
            numberPicker2.setValue(i3);
            value3 = calendar.get(12);
            numberPicker3.setValue(value3);
        } else if (j2 <= 0 || timeInMillis <= j2) {
            i4 = value;
            i3 = value2;
        } else {
            calendar.setTimeInMillis(j2);
            i4 = 7;
            numberPicker.setValue(7);
            i3 = calendar.get(11);
            numberPicker2.setValue(i3);
            value3 = calendar.get(12);
            numberPicker3.setValue(value3);
        }
        int i10 = calendar.get(1);
        calendar.setTimeInMillis(System.currentTimeMillis() + (i4 * 24 * 3600 * 1000));
        calendar.set(11, i3);
        calendar.set(12, value3);
        long timeInMillis2 = calendar.getTimeInMillis();
        if (textView != null) {
            if (i4 == 0) {
                i6 = 1;
                i5 = 0;
            } else if (i2 == i10) {
                i6 = 1;
                i5 = 1;
            } else {
                i6 = 1;
                i5 = 2;
            }
            if (i == i6) {
                i5 += 3;
            } else if (i == 2) {
                i5 += 6;
            } else if (i == 3) {
                i5 += 9;
            }
            textView.setText(LocaleController.getInstance().formatterScheduleSend[i5].format(timeInMillis2));
        }
        if (textView2 != null) {
            int i11 = (int) ((timeInMillis2 - currentTimeMillis) / 1000);
            if (i11 > 86400) {
                str = LocaleController.formatPluralString("DaysSchedule", Math.round(i11 / 86400.0f), new Object[0]);
            } else if (i11 >= 3600) {
                str = LocaleController.formatPluralString("HoursSchedule", Math.round(i11 / 3600.0f), new Object[0]);
            } else if (i11 >= 60) {
                str = LocaleController.formatPluralString("MinutesSchedule", Math.round(i11 / 60.0f), new Object[0]);
            } else {
                str = LocaleController.formatPluralString("SecondsSchedule", i11, new Object[0]);
            }
            if (textView2.getTag() != null) {
                textView2.setText(LocaleController.formatString("VoipChannelScheduleInfo", 2131629059, str));
            } else {
                textView2.setText(LocaleController.formatString("VoipGroupScheduleInfo", 2131629169, str));
            }
        }
        return timeInMillis - currentTimeMillis > 60000;
    }

    /* loaded from: classes3.dex */
    public static class ScheduleDatePickerColors {
        public final int backgroundColor;
        public final int buttonBackgroundColor;
        public final int buttonBackgroundPressedColor;
        public final int buttonTextColor;
        public final int iconColor;
        public final int iconSelectorColor;
        public final int subMenuBackgroundColor;
        public final int subMenuSelectorColor;
        public final int subMenuTextColor;
        public final int textColor;

        /* synthetic */ ScheduleDatePickerColors(Theme.ResourcesProvider resourcesProvider, AnonymousClass1 anonymousClass1) {
            this(resourcesProvider);
        }

        /* synthetic */ ScheduleDatePickerColors(AnonymousClass1 anonymousClass1) {
            this();
        }

        private ScheduleDatePickerColors() {
            this((Theme.ResourcesProvider) null);
        }

        private ScheduleDatePickerColors(Theme.ResourcesProvider resourcesProvider) {
            this(resourcesProvider != null ? resourcesProvider.getColorOrDefault("dialogTextBlack") : Theme.getColor("dialogTextBlack"), resourcesProvider != null ? resourcesProvider.getColorOrDefault("dialogBackground") : Theme.getColor("dialogBackground"), resourcesProvider != null ? resourcesProvider.getColorOrDefault("key_sheet_other") : Theme.getColor("key_sheet_other"), resourcesProvider != null ? resourcesProvider.getColorOrDefault("player_actionBarSelector") : Theme.getColor("player_actionBarSelector"), resourcesProvider != null ? resourcesProvider.getColorOrDefault("actionBarDefaultSubmenuItem") : Theme.getColor("actionBarDefaultSubmenuItem"), resourcesProvider != null ? resourcesProvider.getColorOrDefault("actionBarDefaultSubmenuBackground") : Theme.getColor("actionBarDefaultSubmenuBackground"), resourcesProvider != null ? resourcesProvider.getColorOrDefault("listSelectorSDK21") : Theme.getColor("listSelectorSDK21"), resourcesProvider != null ? resourcesProvider.getColorOrDefault("featuredStickers_buttonText") : Theme.getColor("featuredStickers_buttonText"), resourcesProvider != null ? resourcesProvider.getColorOrDefault("featuredStickers_addButton") : Theme.getColor("featuredStickers_addButton"), resourcesProvider != null ? resourcesProvider.getColorOrDefault("featuredStickers_addButtonPressed") : Theme.getColor("featuredStickers_addButtonPressed"));
        }

        public ScheduleDatePickerColors(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
            this(i, i2, i3, i4, i5, i6, i7, Theme.getColor("featuredStickers_buttonText"), Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed"));
        }

        public ScheduleDatePickerColors(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            this.textColor = i;
            this.backgroundColor = i2;
            this.iconColor = i3;
            this.iconSelectorColor = i4;
            this.subMenuTextColor = i5;
            this.subMenuBackgroundColor = i6;
            this.subMenuSelectorColor = i7;
            this.buttonTextColor = i8;
            this.buttonBackgroundColor = i9;
            this.buttonBackgroundPressedColor = i10;
        }
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, ScheduleDatePickerDelegate scheduleDatePickerDelegate) {
        return createScheduleDatePickerDialog(context, j, -1L, scheduleDatePickerDelegate, (Runnable) null);
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, ScheduleDatePickerDelegate scheduleDatePickerDelegate, Theme.ResourcesProvider resourcesProvider) {
        return createScheduleDatePickerDialog(context, j, -1L, scheduleDatePickerDelegate, null, resourcesProvider);
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, ScheduleDatePickerDelegate scheduleDatePickerDelegate, ScheduleDatePickerColors scheduleDatePickerColors) {
        return createScheduleDatePickerDialog(context, j, -1L, scheduleDatePickerDelegate, null, scheduleDatePickerColors, null);
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, ScheduleDatePickerDelegate scheduleDatePickerDelegate, Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        return createScheduleDatePickerDialog(context, j, -1L, scheduleDatePickerDelegate, runnable, resourcesProvider);
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, long j2, ScheduleDatePickerDelegate scheduleDatePickerDelegate, Runnable runnable) {
        return createScheduleDatePickerDialog(context, j, j2, scheduleDatePickerDelegate, runnable, new ScheduleDatePickerColors((AnonymousClass1) null), null);
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, long j2, ScheduleDatePickerDelegate scheduleDatePickerDelegate, Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        return createScheduleDatePickerDialog(context, j, j2, scheduleDatePickerDelegate, runnable, new ScheduleDatePickerColors(resourcesProvider, null), resourcesProvider);
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, long j2, ScheduleDatePickerDelegate scheduleDatePickerDelegate, Runnable runnable, ScheduleDatePickerColors scheduleDatePickerColors, Theme.ResourcesProvider resourcesProvider) {
        LinearLayout linearLayout;
        int i;
        Calendar calendar;
        TLRPC$User user;
        TLRPC$UserStatus tLRPC$UserStatus;
        if (context == null) {
            return null;
        }
        long clientUserId = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        builder.setApplyBottomPadding(false);
        NumberPicker numberPicker = new NumberPicker(context, resourcesProvider);
        numberPicker.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker.setTextOffset(AndroidUtilities.dp(10.0f));
        numberPicker.setItemCount(5);
        AnonymousClass7 anonymousClass7 = new AnonymousClass7(context, resourcesProvider);
        anonymousClass7.setItemCount(5);
        anonymousClass7.setTextColor(scheduleDatePickerColors.textColor);
        anonymousClass7.setTextOffset(-AndroidUtilities.dp(10.0f));
        AnonymousClass8 anonymousClass8 = new AnonymousClass8(context, resourcesProvider);
        anonymousClass8.setItemCount(5);
        anonymousClass8.setTextColor(scheduleDatePickerColors.textColor);
        anonymousClass8.setTextOffset(-AndroidUtilities.dp(34.0f));
        LinearLayout anonymousClass9 = new AnonymousClass9(context, numberPicker, anonymousClass7, anonymousClass8);
        anonymousClass9.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        anonymousClass9.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        if (j == clientUserId) {
            textView.setText(LocaleController.getString("SetReminder", 2131628311));
        } else {
            textView.setText(LocaleController.getString("ScheduleMessage", 2131628145));
        }
        textView.setTextColor(scheduleDatePickerColors.textColor);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(AlertsCreator$$ExternalSyntheticLambda77.INSTANCE);
        if (!DialogObject.isUserDialog(j) || j == clientUserId || (user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(j))) == null || user.bot || (tLRPC$UserStatus = user.status) == null || tLRPC$UserStatus.expires <= 0) {
            linearLayout = anonymousClass9;
            i = 1;
        } else {
            String firstName = UserObject.getFirstName(user);
            if (firstName.length() > 10) {
                firstName = firstName.substring(0, 10) + "…";
            }
            linearLayout = anonymousClass9;
            ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, null, 0, scheduleDatePickerColors.iconColor, false, resourcesProvider);
            actionBarMenuItem.setLongClickEnabled(false);
            actionBarMenuItem.setSubMenuOpenSide(2);
            actionBarMenuItem.setIcon(2131165453);
            i = 1;
            actionBarMenuItem.setBackgroundDrawable(Theme.createSelectorDrawable(scheduleDatePickerColors.iconSelectorColor, 1));
            frameLayout.addView(actionBarMenuItem, LayoutHelper.createFrame(40, 40.0f, 53, 0.0f, 8.0f, 5.0f, 0.0f));
            actionBarMenuItem.addSubItem(1, LocaleController.formatString("ScheduleWhenOnline", 2131628146, firstName));
            actionBarMenuItem.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda51(actionBarMenuItem, scheduleDatePickerColors));
            actionBarMenuItem.setDelegate(new AlertsCreator$$ExternalSyntheticLambda101(scheduleDatePickerDelegate, builder));
            actionBarMenuItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131624003));
        }
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setWeightSum(1.0f);
        LinearLayout linearLayout3 = linearLayout;
        linearLayout3.addView(linearLayout2, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(currentTimeMillis);
        int i2 = calendar2.get(i);
        AnonymousClass10 anonymousClass10 = new AnonymousClass10(context);
        linearLayout2.addView(numberPicker, LayoutHelper.createLinear(0, 270, 0.5f));
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(365);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setFormatter(new AlertsCreator$$ExternalSyntheticLambda102(currentTimeMillis, calendar2, i2));
        AlertsCreator$$ExternalSyntheticLambda125 alertsCreator$$ExternalSyntheticLambda125 = new AlertsCreator$$ExternalSyntheticLambda125(linearLayout3, anonymousClass10, clientUserId, j, numberPicker, anonymousClass7, anonymousClass8);
        numberPicker.setOnValueChangedListener(alertsCreator$$ExternalSyntheticLambda125);
        anonymousClass7.setMinValue(0);
        anonymousClass7.setMaxValue(23);
        linearLayout2.addView(anonymousClass7, LayoutHelper.createLinear(0, 270, 0.2f));
        anonymousClass7.setFormatter(AlertsCreator$$ExternalSyntheticLambda110.INSTANCE);
        anonymousClass7.setOnValueChangedListener(alertsCreator$$ExternalSyntheticLambda125);
        anonymousClass8.setMinValue(0);
        anonymousClass8.setMaxValue(59);
        anonymousClass8.setValue(0);
        anonymousClass8.setFormatter(AlertsCreator$$ExternalSyntheticLambda112.INSTANCE);
        linearLayout2.addView(anonymousClass8, LayoutHelper.createLinear(0, 270, 0.3f));
        anonymousClass8.setOnValueChangedListener(alertsCreator$$ExternalSyntheticLambda125);
        if (j2 <= 0 || j2 == 2147483646) {
            calendar = calendar2;
        } else {
            long j3 = 1000 * j2;
            calendar = calendar2;
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            calendar.set(11, 0);
            int timeInMillis = (int) ((j3 - calendar.getTimeInMillis()) / 86400000);
            calendar.setTimeInMillis(j3);
            if (timeInMillis >= 0) {
                anonymousClass8.setValue(calendar.get(12));
                anonymousClass7.setValue(calendar.get(11));
                numberPicker.setValue(timeInMillis);
            }
        }
        boolean[] zArr = {true};
        checkScheduleDate(anonymousClass10, null, clientUserId == j ? 1 : 0, numberPicker, anonymousClass7, anonymousClass8);
        anonymousClass10.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        anonymousClass10.setGravity(17);
        anonymousClass10.setTextColor(scheduleDatePickerColors.buttonTextColor);
        anonymousClass10.setTextSize(1, 14.0f);
        anonymousClass10.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        anonymousClass10.setBackground(Theme.AdaptiveRipple.filledRect(scheduleDatePickerColors.buttonBackgroundColor, 4.0f));
        linearLayout3.addView(anonymousClass10, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        anonymousClass10.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda73(zArr, clientUserId, j, numberPicker, anonymousClass7, anonymousClass8, calendar, scheduleDatePickerDelegate, builder));
        builder.setCustomView(linearLayout3);
        BottomSheet show = builder.show();
        show.setOnDismissListener(new AlertsCreator$$ExternalSyntheticLambda44(runnable, zArr));
        show.setBackgroundColor(scheduleDatePickerColors.backgroundColor);
        show.fixNavigationBar(scheduleDatePickerColors.backgroundColor);
        return builder;
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends NumberPicker {
        AnonymousClass7(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
        }

        @Override // org.telegram.ui.Components.NumberPicker
        protected CharSequence getContentDescription(int i) {
            return LocaleController.formatPluralString("Hours", i, new Object[0]);
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends NumberPicker {
        AnonymousClass8(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
        }

        @Override // org.telegram.ui.Components.NumberPicker
        protected CharSequence getContentDescription(int i) {
            return LocaleController.formatPluralString("Minutes", i, new Object[0]);
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends LinearLayout {
        boolean ignoreLayout = false;
        final /* synthetic */ NumberPicker val$dayPicker;
        final /* synthetic */ NumberPicker val$hourPicker;
        final /* synthetic */ NumberPicker val$minutePicker;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass9(Context context, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
            super(context);
            this.val$dayPicker = numberPicker;
            this.val$hourPicker = numberPicker2;
            this.val$minutePicker = numberPicker3;
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            this.ignoreLayout = true;
            android.graphics.Point point = AndroidUtilities.displaySize;
            int i3 = point.x > point.y ? 3 : 5;
            this.val$dayPicker.setItemCount(i3);
            this.val$hourPicker.setItemCount(i3);
            this.val$minutePicker.setItemCount(i3);
            this.val$dayPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.val$hourPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.val$minutePicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.ignoreLayout = false;
            super.onMeasure(i, i2);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    public static /* synthetic */ void lambda$createScheduleDatePickerDialog$48(ActionBarMenuItem actionBarMenuItem, ScheduleDatePickerColors scheduleDatePickerColors, View view) {
        actionBarMenuItem.toggleSubMenu();
        actionBarMenuItem.setPopupItemsColor(scheduleDatePickerColors.subMenuTextColor, false);
        actionBarMenuItem.setupPopupRadialSelectors(scheduleDatePickerColors.subMenuSelectorColor);
        actionBarMenuItem.redrawPopup(scheduleDatePickerColors.subMenuBackgroundColor);
    }

    public static /* synthetic */ void lambda$createScheduleDatePickerDialog$49(ScheduleDatePickerDelegate scheduleDatePickerDelegate, BottomSheet.Builder builder, int i) {
        if (i == 1) {
            scheduleDatePickerDelegate.didSelectDate(true, 2147483646);
            builder.getDismissRunnable().run();
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends TextView {
        AnonymousClass10(Context context) {
            super(context);
        }

        @Override // android.widget.TextView, android.view.View
        public CharSequence getAccessibilityClassName() {
            return Button.class.getName();
        }
    }

    public static /* synthetic */ String lambda$createScheduleDatePickerDialog$50(long j, Calendar calendar, int i, int i2) {
        if (i2 == 0) {
            return LocaleController.getString("MessageScheduleToday", 2131626695);
        }
        long j2 = j + (i2 * 86400000);
        calendar.setTimeInMillis(j2);
        if (calendar.get(1) == i) {
            return LocaleController.getInstance().formatterScheduleDay.format(j2);
        }
        return LocaleController.getInstance().formatterScheduleYear.format(j2);
    }

    public static /* synthetic */ void lambda$createScheduleDatePickerDialog$51(LinearLayout linearLayout, TextView textView, long j, long j2, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, NumberPicker numberPicker4, int i, int i2) {
        try {
            linearLayout.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        checkScheduleDate(textView, null, j == j2 ? 1 : 0, numberPicker, numberPicker2, numberPicker3);
    }

    public static /* synthetic */ String lambda$createScheduleDatePickerDialog$52(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    public static /* synthetic */ String lambda$createScheduleDatePickerDialog$53(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    public static /* synthetic */ void lambda$createScheduleDatePickerDialog$54(boolean[] zArr, long j, long j2, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, Calendar calendar, ScheduleDatePickerDelegate scheduleDatePickerDelegate, BottomSheet.Builder builder, View view) {
        zArr[0] = false;
        boolean checkScheduleDate = checkScheduleDate(null, null, j == j2 ? 1 : 0, numberPicker, numberPicker2, numberPicker3);
        calendar.setTimeInMillis(System.currentTimeMillis() + (numberPicker.getValue() * 24 * 3600 * 1000));
        calendar.set(11, numberPicker2.getValue());
        calendar.set(12, numberPicker3.getValue());
        if (checkScheduleDate) {
            calendar.set(13, 0);
        }
        scheduleDatePickerDelegate.didSelectDate(true, (int) (calendar.getTimeInMillis() / 1000));
        builder.getDismissRunnable().run();
    }

    public static /* synthetic */ void lambda$createScheduleDatePickerDialog$55(Runnable runnable, boolean[] zArr, DialogInterface dialogInterface) {
        if (runnable == null || !zArr[0]) {
            return;
        }
        runnable.run();
    }

    public static BottomSheet.Builder createDatePickerDialog(Context context, long j, ScheduleDatePickerDelegate scheduleDatePickerDelegate) {
        LinearLayout linearLayout;
        if (context == null) {
            return null;
        }
        ScheduleDatePickerColors scheduleDatePickerColors = new ScheduleDatePickerColors((AnonymousClass1) null);
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false);
        builder.setApplyBottomPadding(false);
        NumberPicker numberPicker = new NumberPicker(context);
        numberPicker.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker.setTextOffset(AndroidUtilities.dp(10.0f));
        numberPicker.setItemCount(5);
        AnonymousClass11 anonymousClass11 = new AnonymousClass11(context);
        anonymousClass11.setItemCount(5);
        anonymousClass11.setTextColor(scheduleDatePickerColors.textColor);
        anonymousClass11.setTextOffset(-AndroidUtilities.dp(10.0f));
        AnonymousClass12 anonymousClass12 = new AnonymousClass12(context);
        anonymousClass12.setItemCount(5);
        anonymousClass12.setTextColor(scheduleDatePickerColors.textColor);
        anonymousClass12.setTextOffset(-AndroidUtilities.dp(34.0f));
        LinearLayout anonymousClass13 = new AnonymousClass13(context, numberPicker, anonymousClass11, anonymousClass12);
        anonymousClass13.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        anonymousClass13.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(LocaleController.getString("ExpireAfter", 2131625827));
        textView.setTextColor(scheduleDatePickerColors.textColor);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(AlertsCreator$$ExternalSyntheticLambda76.INSTANCE);
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setWeightSum(1.0f);
        anonymousClass13.addView(linearLayout2, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        int i = calendar.get(1);
        AnonymousClass14 anonymousClass14 = new AnonymousClass14(context);
        linearLayout2.addView(numberPicker, LayoutHelper.createLinear(0, 270, 0.5f));
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(365);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setFormatter(new AlertsCreator$$ExternalSyntheticLambda103(currentTimeMillis, calendar, i));
        AlertsCreator$$ExternalSyntheticLambda126 alertsCreator$$ExternalSyntheticLambda126 = new AlertsCreator$$ExternalSyntheticLambda126(anonymousClass13, numberPicker, anonymousClass11, anonymousClass12);
        numberPicker.setOnValueChangedListener(alertsCreator$$ExternalSyntheticLambda126);
        anonymousClass11.setMinValue(0);
        anonymousClass11.setMaxValue(23);
        linearLayout2.addView(anonymousClass11, LayoutHelper.createLinear(0, 270, 0.2f));
        anonymousClass11.setFormatter(AlertsCreator$$ExternalSyntheticLambda111.INSTANCE);
        anonymousClass11.setOnValueChangedListener(alertsCreator$$ExternalSyntheticLambda126);
        anonymousClass12.setMinValue(0);
        anonymousClass12.setMaxValue(59);
        anonymousClass12.setValue(0);
        anonymousClass12.setFormatter(AlertsCreator$$ExternalSyntheticLambda115.INSTANCE);
        linearLayout2.addView(anonymousClass12, LayoutHelper.createLinear(0, 270, 0.3f));
        anonymousClass12.setOnValueChangedListener(alertsCreator$$ExternalSyntheticLambda126);
        if (j <= 0 || j == 2147483646) {
            linearLayout = anonymousClass13;
        } else {
            long j2 = 1000 * j;
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            calendar.set(11, 0);
            linearLayout = anonymousClass13;
            int timeInMillis = (int) ((j2 - calendar.getTimeInMillis()) / 86400000);
            calendar.setTimeInMillis(j2);
            if (timeInMillis >= 0) {
                anonymousClass12.setValue(calendar.get(12));
                anonymousClass11.setValue(calendar.get(11));
                numberPicker.setValue(timeInMillis);
            }
        }
        checkScheduleDate(null, null, 0, numberPicker, anonymousClass11, anonymousClass12);
        anonymousClass14.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        anonymousClass14.setGravity(17);
        anonymousClass14.setTextColor(scheduleDatePickerColors.buttonTextColor);
        anonymousClass14.setTextSize(1, 14.0f);
        anonymousClass14.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        anonymousClass14.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), scheduleDatePickerColors.buttonBackgroundColor, scheduleDatePickerColors.buttonBackgroundPressedColor));
        anonymousClass14.setText(LocaleController.getString("SetTimeLimit", 2131628312));
        LinearLayout linearLayout3 = linearLayout;
        linearLayout3.addView(anonymousClass14, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        anonymousClass14.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda56(numberPicker, anonymousClass11, anonymousClass12, calendar, scheduleDatePickerDelegate, builder));
        builder.setCustomView(linearLayout3);
        BottomSheet show = builder.show();
        show.setBackgroundColor(scheduleDatePickerColors.backgroundColor);
        show.fixNavigationBar(scheduleDatePickerColors.backgroundColor);
        return builder;
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 extends NumberPicker {
        AnonymousClass11(Context context) {
            super(context);
        }

        @Override // org.telegram.ui.Components.NumberPicker
        protected CharSequence getContentDescription(int i) {
            return LocaleController.formatPluralString("Hours", i, new Object[0]);
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$12 */
    /* loaded from: classes3.dex */
    public class AnonymousClass12 extends NumberPicker {
        AnonymousClass12(Context context) {
            super(context);
        }

        @Override // org.telegram.ui.Components.NumberPicker
        protected CharSequence getContentDescription(int i) {
            return LocaleController.formatPluralString("Minutes", i, new Object[0]);
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 extends LinearLayout {
        boolean ignoreLayout = false;
        final /* synthetic */ NumberPicker val$dayPicker;
        final /* synthetic */ NumberPicker val$hourPicker;
        final /* synthetic */ NumberPicker val$minutePicker;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass13(Context context, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
            super(context);
            this.val$dayPicker = numberPicker;
            this.val$hourPicker = numberPicker2;
            this.val$minutePicker = numberPicker3;
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            this.ignoreLayout = true;
            android.graphics.Point point = AndroidUtilities.displaySize;
            int i3 = point.x > point.y ? 3 : 5;
            this.val$dayPicker.setItemCount(i3);
            this.val$hourPicker.setItemCount(i3);
            this.val$minutePicker.setItemCount(i3);
            this.val$dayPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.val$hourPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.val$minutePicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.ignoreLayout = false;
            super.onMeasure(i, i2);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$14 */
    /* loaded from: classes3.dex */
    public class AnonymousClass14 extends TextView {
        AnonymousClass14(Context context) {
            super(context);
        }

        @Override // android.widget.TextView, android.view.View
        public CharSequence getAccessibilityClassName() {
            return Button.class.getName();
        }
    }

    public static /* synthetic */ String lambda$createDatePickerDialog$57(long j, Calendar calendar, int i, int i2) {
        if (i2 == 0) {
            return LocaleController.getString("MessageScheduleToday", 2131626695);
        }
        long j2 = j + (i2 * 86400000);
        calendar.setTimeInMillis(j2);
        if (calendar.get(1) == i) {
            return LocaleController.getInstance().formatterScheduleDay.format(j2);
        }
        return LocaleController.getInstance().formatterScheduleYear.format(j2);
    }

    public static /* synthetic */ void lambda$createDatePickerDialog$58(LinearLayout linearLayout, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, NumberPicker numberPicker4, int i, int i2) {
        try {
            linearLayout.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        checkScheduleDate(null, null, 0, numberPicker, numberPicker2, numberPicker3);
    }

    public static /* synthetic */ String lambda$createDatePickerDialog$59(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    public static /* synthetic */ String lambda$createDatePickerDialog$60(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    public static /* synthetic */ void lambda$createDatePickerDialog$61(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, Calendar calendar, ScheduleDatePickerDelegate scheduleDatePickerDelegate, BottomSheet.Builder builder, View view) {
        boolean checkScheduleDate = checkScheduleDate(null, null, 0, numberPicker, numberPicker2, numberPicker3);
        calendar.setTimeInMillis(System.currentTimeMillis() + (numberPicker.getValue() * 24 * 3600 * 1000));
        calendar.set(11, numberPicker2.getValue());
        calendar.set(12, numberPicker3.getValue());
        if (checkScheduleDate) {
            calendar.set(13, 0);
        }
        scheduleDatePickerDelegate.didSelectDate(true, (int) (calendar.getTimeInMillis() / 1000));
        builder.getDismissRunnable().run();
    }

    public static BottomSheet.Builder createAutoDeleteDatePickerDialog(Context context, Theme.ResourcesProvider resourcesProvider, ScheduleDatePickerDelegate scheduleDatePickerDelegate) {
        if (context == null) {
            return null;
        }
        ScheduleDatePickerColors scheduleDatePickerColors = new ScheduleDatePickerColors(resourcesProvider, null);
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        builder.setApplyBottomPadding(false);
        int[] iArr = {0, 1440, 2880, 4320, 5760, 7200, 8640, 10080, 20160, 30240, 44640, 89280, 133920, 178560, 223200, 267840, 525600};
        AnonymousClass15 anonymousClass15 = new AnonymousClass15(context, resourcesProvider, iArr);
        anonymousClass15.setMinValue(0);
        anonymousClass15.setMaxValue(16);
        anonymousClass15.setTextColor(scheduleDatePickerColors.textColor);
        anonymousClass15.setValue(0);
        anonymousClass15.setFormatter(new AlertsCreator$$ExternalSyntheticLambda104(iArr));
        AnonymousClass16 anonymousClass16 = new AnonymousClass16(context, anonymousClass15);
        anonymousClass16.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        anonymousClass16.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(LocaleController.getString("AutoDeleteAfteTitle", 2131624553));
        textView.setTextColor(scheduleDatePickerColors.textColor);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(AlertsCreator$$ExternalSyntheticLambda79.INSTANCE);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setWeightSum(1.0f);
        anonymousClass16.addView(linearLayout, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        AnonymousClass17 anonymousClass17 = new AnonymousClass17(context);
        linearLayout.addView(anonymousClass15, LayoutHelper.createLinear(0, 270, 1.0f));
        anonymousClass17.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        anonymousClass17.setGravity(17);
        anonymousClass17.setTextColor(scheduleDatePickerColors.buttonTextColor);
        anonymousClass17.setTextSize(1, 14.0f);
        anonymousClass17.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        anonymousClass17.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), scheduleDatePickerColors.buttonBackgroundColor, scheduleDatePickerColors.buttonBackgroundPressedColor));
        anonymousClass17.setText(LocaleController.getString("AutoDeleteConfirm", 2131624559));
        anonymousClass16.addView(anonymousClass17, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        anonymousClass15.setOnValueChangedListener(new AlertsCreator$$ExternalSyntheticLambda121(anonymousClass16));
        anonymousClass17.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda62(iArr, anonymousClass15, scheduleDatePickerDelegate, builder));
        builder.setCustomView(anonymousClass16);
        BottomSheet show = builder.show();
        show.setBackgroundColor(scheduleDatePickerColors.backgroundColor);
        show.fixNavigationBar(scheduleDatePickerColors.backgroundColor);
        return builder;
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$15 */
    /* loaded from: classes3.dex */
    public class AnonymousClass15 extends NumberPicker {
        final /* synthetic */ int[] val$values;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass15(Context context, Theme.ResourcesProvider resourcesProvider, int[] iArr) {
            super(context, resourcesProvider);
            this.val$values = iArr;
        }

        @Override // org.telegram.ui.Components.NumberPicker
        protected CharSequence getContentDescription(int i) {
            int[] iArr = this.val$values;
            if (iArr[i] == 0) {
                return LocaleController.getString("AutoDeleteNever", 2131624567);
            }
            if (iArr[i] < 10080) {
                return LocaleController.formatPluralString("Days", iArr[i] / 1440, new Object[0]);
            }
            if (iArr[i] < 44640) {
                return LocaleController.formatPluralString("Weeks", iArr[i] / 1440, new Object[0]);
            }
            if (iArr[i] < 525600) {
                return LocaleController.formatPluralString("Months", iArr[i] / 10080, new Object[0]);
            }
            return LocaleController.formatPluralString("Years", ((iArr[i] * 5) / 31) * 60 * 24, new Object[0]);
        }
    }

    public static /* synthetic */ String lambda$createAutoDeleteDatePickerDialog$62(int[] iArr, int i) {
        if (iArr[i] == 0) {
            return LocaleController.getString("AutoDeleteNever", 2131624567);
        }
        if (iArr[i] < 10080) {
            return LocaleController.formatPluralString("Days", iArr[i] / 1440, new Object[0]);
        }
        if (iArr[i] < 44640) {
            return LocaleController.formatPluralString("Weeks", iArr[i] / 10080, new Object[0]);
        }
        if (iArr[i] < 525600) {
            return LocaleController.formatPluralString("Months", iArr[i] / 44640, new Object[0]);
        }
        return LocaleController.formatPluralString("Years", iArr[i] / 525600, new Object[0]);
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$16 */
    /* loaded from: classes3.dex */
    public class AnonymousClass16 extends LinearLayout {
        boolean ignoreLayout = false;
        final /* synthetic */ NumberPicker val$numberPicker;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass16(Context context, NumberPicker numberPicker) {
            super(context);
            this.val$numberPicker = numberPicker;
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            this.ignoreLayout = true;
            android.graphics.Point point = AndroidUtilities.displaySize;
            int i3 = point.x > point.y ? 3 : 5;
            this.val$numberPicker.setItemCount(i3);
            this.val$numberPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.ignoreLayout = false;
            super.onMeasure(i, i2);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$17 */
    /* loaded from: classes3.dex */
    public class AnonymousClass17 extends TextView {
        AnonymousClass17(Context context) {
            super(context);
        }

        @Override // android.widget.TextView, android.view.View
        public CharSequence getAccessibilityClassName() {
            return Button.class.getName();
        }
    }

    public static /* synthetic */ void lambda$createAutoDeleteDatePickerDialog$64(LinearLayout linearLayout, NumberPicker numberPicker, int i, int i2) {
        try {
            linearLayout.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
    }

    public static /* synthetic */ void lambda$createAutoDeleteDatePickerDialog$65(int[] iArr, NumberPicker numberPicker, ScheduleDatePickerDelegate scheduleDatePickerDelegate, BottomSheet.Builder builder, View view) {
        scheduleDatePickerDelegate.didSelectDate(true, iArr[numberPicker.getValue()]);
        builder.getDismissRunnable().run();
    }

    public static BottomSheet.Builder createSoundFrequencyPickerDialog(Context context, int i, int i2, SoundFrequencyDelegate soundFrequencyDelegate, Theme.ResourcesProvider resourcesProvider) {
        if (context == null) {
            return null;
        }
        ScheduleDatePickerColors scheduleDatePickerColors = new ScheduleDatePickerColors(resourcesProvider, null);
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        builder.setApplyBottomPadding(false);
        AnonymousClass18 anonymousClass18 = new AnonymousClass18(context, resourcesProvider);
        anonymousClass18.setMinValue(0);
        anonymousClass18.setMaxValue(10);
        anonymousClass18.setTextColor(scheduleDatePickerColors.textColor);
        anonymousClass18.setValue(i - 1);
        anonymousClass18.setWrapSelectorWheel(false);
        anonymousClass18.setFormatter(AlertsCreator$$ExternalSyntheticLambda117.INSTANCE);
        AnonymousClass19 anonymousClass19 = new AnonymousClass19(context, resourcesProvider);
        anonymousClass19.setMinValue(0);
        anonymousClass19.setMaxValue(10);
        anonymousClass19.setTextColor(scheduleDatePickerColors.textColor);
        anonymousClass19.setValue((i2 / 60) - 1);
        anonymousClass19.setWrapSelectorWheel(false);
        anonymousClass19.setFormatter(AlertsCreator$$ExternalSyntheticLambda107.INSTANCE);
        NumberPicker numberPicker = new NumberPicker(context, resourcesProvider);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(0);
        numberPicker.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker.setValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setFormatter(AlertsCreator$$ExternalSyntheticLambda109.INSTANCE);
        AnonymousClass20 anonymousClass20 = new AnonymousClass20(context, anonymousClass18, anonymousClass19, numberPicker);
        anonymousClass20.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        anonymousClass20.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(LocaleController.getString("NotfificationsFrequencyTitle", 2131626938));
        textView.setTextColor(scheduleDatePickerColors.textColor);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(AlertsCreator$$ExternalSyntheticLambda81.INSTANCE);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setWeightSum(1.0f);
        anonymousClass20.addView(linearLayout, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        AnonymousClass21 anonymousClass21 = new AnonymousClass21(context);
        linearLayout.addView(anonymousClass18, LayoutHelper.createLinear(0, 270, 0.4f));
        linearLayout.addView(numberPicker, LayoutHelper.createLinear(0, -2, 0.2f, 16));
        linearLayout.addView(anonymousClass19, LayoutHelper.createLinear(0, 270, 0.4f));
        anonymousClass21.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        anonymousClass21.setGravity(17);
        anonymousClass21.setTextColor(scheduleDatePickerColors.buttonTextColor);
        anonymousClass21.setTextSize(1, 14.0f);
        anonymousClass21.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        anonymousClass21.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), scheduleDatePickerColors.buttonBackgroundColor, scheduleDatePickerColors.buttonBackgroundPressedColor));
        anonymousClass21.setText(LocaleController.getString("AutoDeleteConfirm", 2131624559));
        anonymousClass20.addView(anonymousClass21, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        AlertsCreator$$ExternalSyntheticLambda123 alertsCreator$$ExternalSyntheticLambda123 = new AlertsCreator$$ExternalSyntheticLambda123(anonymousClass20);
        anonymousClass18.setOnValueChangedListener(alertsCreator$$ExternalSyntheticLambda123);
        anonymousClass19.setOnValueChangedListener(alertsCreator$$ExternalSyntheticLambda123);
        anonymousClass21.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda55(anonymousClass18, anonymousClass19, soundFrequencyDelegate, builder));
        builder.setCustomView(anonymousClass20);
        BottomSheet show = builder.show();
        show.setBackgroundColor(scheduleDatePickerColors.backgroundColor);
        show.fixNavigationBar(scheduleDatePickerColors.backgroundColor);
        return builder;
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$18 */
    /* loaded from: classes3.dex */
    public class AnonymousClass18 extends NumberPicker {
        AnonymousClass18(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
        }

        @Override // org.telegram.ui.Components.NumberPicker
        protected CharSequence getContentDescription(int i) {
            return LocaleController.formatPluralString("Times", i + 1, new Object[0]);
        }
    }

    public static /* synthetic */ String lambda$createSoundFrequencyPickerDialog$66(int i) {
        return LocaleController.formatPluralString("Times", i + 1, new Object[0]);
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$19 */
    /* loaded from: classes3.dex */
    public class AnonymousClass19 extends NumberPicker {
        AnonymousClass19(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
        }

        @Override // org.telegram.ui.Components.NumberPicker
        protected CharSequence getContentDescription(int i) {
            return LocaleController.formatPluralString("Times", i + 1, new Object[0]);
        }
    }

    public static /* synthetic */ String lambda$createSoundFrequencyPickerDialog$67(int i) {
        return LocaleController.formatPluralString("Minutes", i + 1, new Object[0]);
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$20 */
    /* loaded from: classes3.dex */
    public class AnonymousClass20 extends LinearLayout {
        boolean ignoreLayout = false;
        final /* synthetic */ NumberPicker val$divider;
        final /* synthetic */ NumberPicker val$minutes;
        final /* synthetic */ NumberPicker val$times;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass20(Context context, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
            super(context);
            this.val$times = numberPicker;
            this.val$minutes = numberPicker2;
            this.val$divider = numberPicker3;
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            this.ignoreLayout = true;
            android.graphics.Point point = AndroidUtilities.displaySize;
            int i3 = point.x > point.y ? 3 : 5;
            this.val$times.setItemCount(i3);
            this.val$times.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.val$minutes.setItemCount(i3);
            this.val$minutes.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.val$divider.setItemCount(i3);
            this.val$divider.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.ignoreLayout = false;
            super.onMeasure(i, i2);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$21 */
    /* loaded from: classes3.dex */
    public class AnonymousClass21 extends TextView {
        AnonymousClass21(Context context) {
            super(context);
        }

        @Override // android.widget.TextView, android.view.View
        public CharSequence getAccessibilityClassName() {
            return Button.class.getName();
        }
    }

    public static /* synthetic */ void lambda$createSoundFrequencyPickerDialog$70(LinearLayout linearLayout, NumberPicker numberPicker, int i, int i2) {
        try {
            linearLayout.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
    }

    public static /* synthetic */ void lambda$createSoundFrequencyPickerDialog$71(NumberPicker numberPicker, NumberPicker numberPicker2, SoundFrequencyDelegate soundFrequencyDelegate, BottomSheet.Builder builder, View view) {
        soundFrequencyDelegate.didSelectValues(numberPicker.getValue() + 1, (numberPicker2.getValue() + 1) * 60);
        builder.getDismissRunnable().run();
    }

    public static BottomSheet.Builder createMuteForPickerDialog(Context context, Theme.ResourcesProvider resourcesProvider, ScheduleDatePickerDelegate scheduleDatePickerDelegate) {
        if (context == null) {
            return null;
        }
        ScheduleDatePickerColors scheduleDatePickerColors = new ScheduleDatePickerColors(resourcesProvider, null);
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        builder.setApplyBottomPadding(false);
        int[] iArr = {30, 60, 120, 180, 480, 1440, 2880, 4320, 5760, 7200, 8640, 10080, 20160, 30240, 44640, 89280, 133920, 178560, 223200, 267840, 525600};
        AnonymousClass22 anonymousClass22 = new AnonymousClass22(context, resourcesProvider, iArr);
        anonymousClass22.setMinValue(0);
        anonymousClass22.setMaxValue(20);
        anonymousClass22.setTextColor(scheduleDatePickerColors.textColor);
        anonymousClass22.setValue(0);
        anonymousClass22.setFormatter(new AlertsCreator$$ExternalSyntheticLambda105(iArr));
        AnonymousClass23 anonymousClass23 = new AnonymousClass23(context, anonymousClass22);
        anonymousClass23.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        anonymousClass23.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(LocaleController.getString("MuteForAlert", 2131626801));
        textView.setTextColor(scheduleDatePickerColors.textColor);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(AlertsCreator$$ExternalSyntheticLambda80.INSTANCE);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setWeightSum(1.0f);
        anonymousClass23.addView(linearLayout, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        AnonymousClass24 anonymousClass24 = new AnonymousClass24(context);
        linearLayout.addView(anonymousClass22, LayoutHelper.createLinear(0, 270, 1.0f));
        anonymousClass22.setOnValueChangedListener(new AlertsCreator$$ExternalSyntheticLambda122(anonymousClass23));
        anonymousClass24.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        anonymousClass24.setGravity(17);
        anonymousClass24.setTextColor(scheduleDatePickerColors.buttonTextColor);
        anonymousClass24.setTextSize(1, 14.0f);
        anonymousClass24.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        anonymousClass24.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), scheduleDatePickerColors.buttonBackgroundColor, scheduleDatePickerColors.buttonBackgroundPressedColor));
        anonymousClass24.setText(LocaleController.getString("AutoDeleteConfirm", 2131624559));
        anonymousClass23.addView(anonymousClass24, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        anonymousClass24.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda63(iArr, anonymousClass22, scheduleDatePickerDelegate, builder));
        builder.setCustomView(anonymousClass23);
        BottomSheet show = builder.show();
        show.setBackgroundColor(scheduleDatePickerColors.backgroundColor);
        show.fixNavigationBar(scheduleDatePickerColors.backgroundColor);
        return builder;
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$22 */
    /* loaded from: classes3.dex */
    public class AnonymousClass22 extends NumberPicker {
        final /* synthetic */ int[] val$values;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass22(Context context, Theme.ResourcesProvider resourcesProvider, int[] iArr) {
            super(context, resourcesProvider);
            this.val$values = iArr;
        }

        @Override // org.telegram.ui.Components.NumberPicker
        protected CharSequence getContentDescription(int i) {
            int[] iArr = this.val$values;
            if (iArr[i] == 0) {
                return LocaleController.getString("MuteNever", 2131626804);
            }
            if (iArr[i] < 60) {
                return LocaleController.formatPluralString("Minutes", iArr[i], new Object[0]);
            }
            if (iArr[i] < 1440) {
                return LocaleController.formatPluralString("Hours", iArr[i] / 60, new Object[0]);
            }
            if (iArr[i] < 10080) {
                return LocaleController.formatPluralString("Days", iArr[i] / 1440, new Object[0]);
            }
            if (iArr[i] < 44640) {
                return LocaleController.formatPluralString("Weeks", iArr[i] / 10080, new Object[0]);
            }
            if (iArr[i] < 525600) {
                return LocaleController.formatPluralString("Months", iArr[i] / 44640, new Object[0]);
            }
            return LocaleController.formatPluralString("Years", iArr[i] / 525600, new Object[0]);
        }
    }

    public static /* synthetic */ String lambda$createMuteForPickerDialog$72(int[] iArr, int i) {
        if (iArr[i] == 0) {
            return LocaleController.getString("MuteNever", 2131626804);
        }
        if (iArr[i] < 60) {
            return LocaleController.formatPluralString("Minutes", iArr[i], new Object[0]);
        }
        if (iArr[i] < 1440) {
            return LocaleController.formatPluralString("Hours", iArr[i] / 60, new Object[0]);
        }
        if (iArr[i] < 10080) {
            return LocaleController.formatPluralString("Days", iArr[i] / 1440, new Object[0]);
        }
        if (iArr[i] < 44640) {
            return LocaleController.formatPluralString("Weeks", iArr[i] / 10080, new Object[0]);
        }
        if (iArr[i] < 525600) {
            return LocaleController.formatPluralString("Months", iArr[i] / 44640, new Object[0]);
        }
        return LocaleController.formatPluralString("Years", iArr[i] / 525600, new Object[0]);
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$23 */
    /* loaded from: classes3.dex */
    public class AnonymousClass23 extends LinearLayout {
        boolean ignoreLayout = false;
        final /* synthetic */ NumberPicker val$numberPicker;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass23(Context context, NumberPicker numberPicker) {
            super(context);
            this.val$numberPicker = numberPicker;
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            this.ignoreLayout = true;
            android.graphics.Point point = AndroidUtilities.displaySize;
            int i3 = point.x > point.y ? 3 : 5;
            this.val$numberPicker.setItemCount(i3);
            this.val$numberPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.ignoreLayout = false;
            super.onMeasure(i, i2);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$24 */
    /* loaded from: classes3.dex */
    public class AnonymousClass24 extends TextView {
        AnonymousClass24(Context context) {
            super(context);
        }

        @Override // android.widget.TextView, android.view.View
        public CharSequence getAccessibilityClassName() {
            return Button.class.getName();
        }
    }

    public static /* synthetic */ void lambda$createMuteForPickerDialog$74(LinearLayout linearLayout, NumberPicker numberPicker, int i, int i2) {
        try {
            linearLayout.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
    }

    public static /* synthetic */ void lambda$createMuteForPickerDialog$75(int[] iArr, NumberPicker numberPicker, ScheduleDatePickerDelegate scheduleDatePickerDelegate, BottomSheet.Builder builder, View view) {
        scheduleDatePickerDelegate.didSelectDate(true, iArr[numberPicker.getValue()] * 60);
        builder.getDismissRunnable().run();
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void checkCalendarDate(long j, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        int actualMaximum;
        int value = numberPicker.getValue();
        int value2 = numberPicker2.getValue();
        int value3 = numberPicker3.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        int i = calendar.get(1);
        int i2 = calendar.get(2);
        int i3 = calendar.get(5);
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i4 = calendar.get(1);
        int i5 = calendar.get(2);
        int i6 = calendar.get(5);
        if (value3 > i4) {
            numberPicker3.setValue(i4);
            value3 = i4;
        }
        if (value3 == i4) {
            if (value2 > i5) {
                numberPicker2.setValue(i5);
                value2 = i5;
            }
            if (value2 == i5 && value > i6) {
                numberPicker.setValue(i6);
                value = i6;
            }
        }
        if (value3 < i) {
            numberPicker3.setValue(i);
            value3 = i;
        }
        if (value3 == i) {
            if (value2 < i2) {
                numberPicker2.setValue(i2);
                value2 = i2;
            }
            if (value2 == i2 && value < i3) {
                numberPicker.setValue(i3);
                calendar.set(1, value3);
                calendar.set(2, value2);
                actualMaximum = calendar.getActualMaximum(5);
                numberPicker.setMaxValue(actualMaximum);
                if (i3 > actualMaximum) {
                    return;
                }
                numberPicker.setValue(actualMaximum);
                return;
            }
        }
        i3 = value;
        calendar.set(1, value3);
        calendar.set(2, value2);
        actualMaximum = calendar.getActualMaximum(5);
        numberPicker.setMaxValue(actualMaximum);
        if (i3 > actualMaximum) {
        }
    }

    public static BottomSheet.Builder createCalendarPickerDialog(Context context, long j, MessagesStorage.IntCallback intCallback, Theme.ResourcesProvider resourcesProvider) {
        if (context == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        builder.setApplyBottomPadding(false);
        NumberPicker numberPicker = new NumberPicker(context, resourcesProvider);
        numberPicker.setTextOffset(AndroidUtilities.dp(10.0f));
        numberPicker.setItemCount(5);
        NumberPicker numberPicker2 = new NumberPicker(context, resourcesProvider);
        numberPicker2.setItemCount(5);
        numberPicker2.setTextOffset(-AndroidUtilities.dp(10.0f));
        NumberPicker numberPicker3 = new NumberPicker(context, resourcesProvider);
        numberPicker3.setItemCount(5);
        numberPicker3.setTextOffset(-AndroidUtilities.dp(24.0f));
        AnonymousClass25 anonymousClass25 = new AnonymousClass25(context, numberPicker, numberPicker2, numberPicker3);
        anonymousClass25.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        anonymousClass25.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(LocaleController.getString("ChooseDate", 2131625119));
        textView.setTextColor(Theme.getColor("dialogTextBlack", resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(AlertsCreator$$ExternalSyntheticLambda78.INSTANCE);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setWeightSum(1.0f);
        anonymousClass25.addView(linearLayout, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        System.currentTimeMillis();
        AnonymousClass26 anonymousClass26 = new AnonymousClass26(context);
        linearLayout.addView(numberPicker, LayoutHelper.createLinear(0, 270, 0.25f));
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(31);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setFormatter(AlertsCreator$$ExternalSyntheticLambda106.INSTANCE);
        AlertsCreator$$ExternalSyntheticLambda124 alertsCreator$$ExternalSyntheticLambda124 = new AlertsCreator$$ExternalSyntheticLambda124(anonymousClass25, j, numberPicker, numberPicker2, numberPicker3);
        numberPicker.setOnValueChangedListener(alertsCreator$$ExternalSyntheticLambda124);
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(11);
        numberPicker2.setWrapSelectorWheel(false);
        linearLayout.addView(numberPicker2, LayoutHelper.createLinear(0, 270, 0.5f));
        numberPicker2.setFormatter(AlertsCreator$$ExternalSyntheticLambda114.INSTANCE);
        numberPicker2.setOnValueChangedListener(alertsCreator$$ExternalSyntheticLambda124);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        int i = calendar.get(1);
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i2 = calendar.get(1);
        numberPicker3.setMinValue(i);
        numberPicker3.setMaxValue(i2);
        numberPicker3.setWrapSelectorWheel(false);
        numberPicker3.setFormatter(AlertsCreator$$ExternalSyntheticLambda108.INSTANCE);
        linearLayout.addView(numberPicker3, LayoutHelper.createLinear(0, 270, 0.25f));
        numberPicker3.setOnValueChangedListener(alertsCreator$$ExternalSyntheticLambda124);
        numberPicker.setValue(31);
        numberPicker2.setValue(12);
        numberPicker3.setValue(i2);
        checkCalendarDate(j, numberPicker, numberPicker2, numberPicker3);
        anonymousClass26.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        anonymousClass26.setGravity(17);
        anonymousClass26.setTextColor(Theme.getColor("featuredStickers_buttonText", resourcesProvider));
        anonymousClass26.setTextSize(1, 14.0f);
        anonymousClass26.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        anonymousClass26.setText(LocaleController.getString("JumpToDate", 2131626382));
        anonymousClass26.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), Theme.getColor("featuredStickers_addButton", resourcesProvider), Theme.getColor("featuredStickers_addButtonPressed", resourcesProvider)));
        anonymousClass25.addView(anonymousClass26, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        anonymousClass26.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda48(j, numberPicker, numberPicker2, numberPicker3, calendar, intCallback, builder));
        builder.setCustomView(anonymousClass25);
        return builder;
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$25 */
    /* loaded from: classes3.dex */
    public class AnonymousClass25 extends LinearLayout {
        boolean ignoreLayout = false;
        final /* synthetic */ NumberPicker val$dayPicker;
        final /* synthetic */ NumberPicker val$monthPicker;
        final /* synthetic */ NumberPicker val$yearPicker;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass25(Context context, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
            super(context);
            this.val$dayPicker = numberPicker;
            this.val$monthPicker = numberPicker2;
            this.val$yearPicker = numberPicker3;
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            this.ignoreLayout = true;
            android.graphics.Point point = AndroidUtilities.displaySize;
            int i3 = point.x > point.y ? 3 : 5;
            this.val$dayPicker.setItemCount(i3);
            this.val$monthPicker.setItemCount(i3);
            this.val$yearPicker.setItemCount(i3);
            this.val$dayPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.val$monthPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.val$yearPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
            this.ignoreLayout = false;
            super.onMeasure(i, i2);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$26 */
    /* loaded from: classes3.dex */
    public class AnonymousClass26 extends TextView {
        AnonymousClass26(Context context) {
            super(context);
        }

        @Override // android.widget.TextView, android.view.View
        public CharSequence getAccessibilityClassName() {
            return Button.class.getName();
        }
    }

    public static /* synthetic */ String lambda$createCalendarPickerDialog$77(int i) {
        return "" + i;
    }

    public static /* synthetic */ void lambda$createCalendarPickerDialog$78(LinearLayout linearLayout, long j, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, NumberPicker numberPicker4, int i, int i2) {
        try {
            linearLayout.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        checkCalendarDate(j, numberPicker, numberPicker2, numberPicker3);
    }

    public static /* synthetic */ String lambda$createCalendarPickerDialog$79(int i) {
        switch (i) {
            case 0:
                return LocaleController.getString("January", 2131626358);
            case 1:
                return LocaleController.getString("February", 2131625846);
            case 2:
                return LocaleController.getString("March", 2131626587);
            case 3:
                return LocaleController.getString("April", 2131624400);
            case 4:
                return LocaleController.getString("May", 2131626603);
            case 5:
                return LocaleController.getString("June", 2131626383);
            case 6:
                return LocaleController.getString("July", 2131626381);
            case 7:
                return LocaleController.getString("August", 2131624534);
            case 8:
                return LocaleController.getString("September", 2131628291);
            case 9:
                return LocaleController.getString("October", 2131627128);
            case 10:
                return LocaleController.getString("November", 2131627124);
            default:
                return LocaleController.getString("December", 2131625378);
        }
    }

    public static /* synthetic */ String lambda$createCalendarPickerDialog$80(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    public static /* synthetic */ void lambda$createCalendarPickerDialog$81(long j, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, Calendar calendar, MessagesStorage.IntCallback intCallback, BottomSheet.Builder builder, View view) {
        checkCalendarDate(j, numberPicker, numberPicker2, numberPicker3);
        calendar.set(1, numberPicker3.getValue());
        calendar.set(2, numberPicker2.getValue());
        calendar.set(5, numberPicker.getValue());
        calendar.set(12, 0);
        calendar.set(11, 0);
        calendar.set(13, 0);
        intCallback.run((int) (calendar.getTimeInMillis() / 1000));
        builder.getDismissRunnable().run();
    }

    public static BottomSheet createMuteAlert(BaseFragment baseFragment, long j, Theme.ResourcesProvider resourcesProvider) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(baseFragment.getParentActivity(), false, resourcesProvider);
        builder.setTitle(LocaleController.getString("Notifications", 2131627068), true);
        builder.setItems(new CharSequence[]{LocaleController.formatString("MuteFor", 2131626799, LocaleController.formatPluralString("Hours", 1, new Object[0])), LocaleController.formatString("MuteFor", 2131626799, LocaleController.formatPluralString("Hours", 8, new Object[0])), LocaleController.formatString("MuteFor", 2131626799, LocaleController.formatPluralString("Days", 2, new Object[0])), LocaleController.getString("MuteDisable", 2131626798)}, new AlertsCreator$$ExternalSyntheticLambda4(j, baseFragment, resourcesProvider));
        return builder.create();
    }

    public static /* synthetic */ void lambda$createMuteAlert$82(long j, BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider, DialogInterface dialogInterface, int i) {
        int i2 = 2;
        if (i == 0) {
            i2 = 0;
        } else if (i == 1) {
            i2 = 1;
        } else if (i != 2) {
            i2 = 3;
        }
        NotificationsController.getInstance(UserConfig.selectedAccount).setDialogNotificationsSettings(j, i2);
        if (BulletinFactory.canShowBulletin(baseFragment)) {
            BulletinFactory.createMuteBulletin(baseFragment, i2, 0, resourcesProvider).show();
        }
    }

    public static void sendReport(TLRPC$InputPeer tLRPC$InputPeer, int i, String str, ArrayList<Integer> arrayList) {
        TLRPC$TL_messages_report tLRPC$TL_messages_report = new TLRPC$TL_messages_report();
        tLRPC$TL_messages_report.peer = tLRPC$InputPeer;
        tLRPC$TL_messages_report.id.addAll(arrayList);
        tLRPC$TL_messages_report.message = str;
        if (i == 0) {
            tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonSpam();
        } else if (i == 6) {
            tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonFake();
        } else if (i == 1) {
            tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonViolence();
        } else if (i == 2) {
            tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonChildAbuse();
        } else if (i == 5) {
            tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonPornography();
        } else if (i == 3) {
            tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonIllegalDrugs();
        } else if (i == 4) {
            tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonPersonalDetails();
        } else if (i == 100) {
            tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonOther();
        }
        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tLRPC$TL_messages_report, AlertsCreator$$ExternalSyntheticLambda97.INSTANCE);
    }

    public static void createReportAlert(Context context, long j, int i, BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider, Runnable runnable) {
        CharSequence[] charSequenceArr;
        int[] iArr;
        int[] iArr2;
        if (context == null || baseFragment == null) {
            return;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(context, true, resourcesProvider);
        builder.setDimBehind(runnable == null);
        builder.setOnPreDismissListener(new AlertsCreator$$ExternalSyntheticLambda42(runnable));
        builder.setTitle(LocaleController.getString("ReportChat", 2131627992), true);
        if (i != 0) {
            iArr = new int[]{2131165685, 2131165908, 2131165652, 2131165904, 2131165907, 2131165909, 2131165906};
            charSequenceArr = new CharSequence[]{LocaleController.getString("ReportChatSpam", 2131628001), LocaleController.getString("ReportChatViolence", 2131628002), LocaleController.getString("ReportChatChild", 2131627993), LocaleController.getString("ReportChatIllegalDrugs", 2131627996), LocaleController.getString("ReportChatPersonalDetails", 2131627998), LocaleController.getString("ReportChatPornography", 2131627999), LocaleController.getString("ReportChatOther", 2131627997)};
            iArr2 = new int[]{0, 1, 2, 3, 4, 5, 100};
        } else {
            iArr2 = new int[]{0, 6, 1, 2, 3, 4, 5, 100};
            iArr = new int[]{2131165685, 2131165905, 2131165908, 2131165652, 2131165904, 2131165907, 2131165909, 2131165906};
            charSequenceArr = new CharSequence[]{LocaleController.getString("ReportChatSpam", 2131628001), LocaleController.getString("ReportChatFakeAccount", 2131627995), LocaleController.getString("ReportChatViolence", 2131628002), LocaleController.getString("ReportChatChild", 2131627993), LocaleController.getString("ReportChatIllegalDrugs", 2131627996), LocaleController.getString("ReportChatPersonalDetails", 2131627998), LocaleController.getString("ReportChatPornography", 2131627999), LocaleController.getString("ReportChatOther", 2131627997)};
        }
        builder.setItems(charSequenceArr, iArr, new AlertsCreator$$ExternalSyntheticLambda36(iArr2, i, baseFragment, context, j, resourcesProvider));
        baseFragment.showDialog(builder.create());
    }

    public static /* synthetic */ void lambda$createReportAlert$84(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void lambda$createReportAlert$86(int[] iArr, int i, BaseFragment baseFragment, Context context, long j, Theme.ResourcesProvider resourcesProvider, DialogInterface dialogInterface, int i2) {
        TLRPC$TL_account_reportPeer tLRPC$TL_account_reportPeer;
        int i3 = iArr[i2];
        if (i == 0 && ((i3 == 0 || i3 == 1 || i3 == 2 || i3 == 5 || i3 == 3 || i3 == 4) && (baseFragment instanceof ChatActivity))) {
            ((ChatActivity) baseFragment).openReportChat(i3);
        } else if ((i == 0 && (i3 == 100 || i3 == 6)) || (i != 0 && i3 == 100)) {
            if (baseFragment instanceof ChatActivity) {
                AndroidUtilities.requestAdjustNothing(baseFragment.getParentActivity(), baseFragment.getClassGuid());
            }
            baseFragment.showDialog(new AnonymousClass27(context, i3, baseFragment, i, j));
        } else {
            TLRPC$InputPeer inputPeer = MessagesController.getInstance(UserConfig.selectedAccount).getInputPeer(j);
            if (i != 0) {
                TLRPC$TL_messages_report tLRPC$TL_messages_report = new TLRPC$TL_messages_report();
                tLRPC$TL_messages_report.peer = inputPeer;
                tLRPC$TL_messages_report.id.add(Integer.valueOf(i));
                tLRPC$TL_messages_report.message = "";
                tLRPC$TL_account_reportPeer = tLRPC$TL_messages_report;
                if (i3 == 0) {
                    tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonSpam();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_messages_report;
                } else if (i3 == 1) {
                    tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonViolence();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_messages_report;
                } else if (i3 == 2) {
                    tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonChildAbuse();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_messages_report;
                } else if (i3 == 5) {
                    tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonPornography();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_messages_report;
                } else if (i3 == 3) {
                    tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonIllegalDrugs();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_messages_report;
                } else if (i3 == 4) {
                    tLRPC$TL_messages_report.reason = new TLRPC$TL_inputReportReasonPersonalDetails();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_messages_report;
                }
            } else {
                TLRPC$TL_account_reportPeer tLRPC$TL_account_reportPeer2 = new TLRPC$TL_account_reportPeer();
                tLRPC$TL_account_reportPeer2.peer = inputPeer;
                tLRPC$TL_account_reportPeer2.message = "";
                tLRPC$TL_account_reportPeer = tLRPC$TL_account_reportPeer2;
                if (i3 == 0) {
                    tLRPC$TL_account_reportPeer2.reason = new TLRPC$TL_inputReportReasonSpam();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_account_reportPeer2;
                } else if (i3 == 6) {
                    tLRPC$TL_account_reportPeer2.reason = new TLRPC$TL_inputReportReasonFake();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_account_reportPeer2;
                } else if (i3 == 1) {
                    tLRPC$TL_account_reportPeer2.reason = new TLRPC$TL_inputReportReasonViolence();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_account_reportPeer2;
                } else if (i3 == 2) {
                    tLRPC$TL_account_reportPeer2.reason = new TLRPC$TL_inputReportReasonChildAbuse();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_account_reportPeer2;
                } else if (i3 == 5) {
                    tLRPC$TL_account_reportPeer2.reason = new TLRPC$TL_inputReportReasonPornography();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_account_reportPeer2;
                } else if (i3 == 3) {
                    tLRPC$TL_account_reportPeer2.reason = new TLRPC$TL_inputReportReasonIllegalDrugs();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_account_reportPeer2;
                } else if (i3 == 4) {
                    tLRPC$TL_account_reportPeer2.reason = new TLRPC$TL_inputReportReasonPersonalDetails();
                    tLRPC$TL_account_reportPeer = tLRPC$TL_account_reportPeer2;
                }
            }
            ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tLRPC$TL_account_reportPeer, AlertsCreator$$ExternalSyntheticLambda96.INSTANCE);
            if (baseFragment instanceof ChatActivity) {
                ((ChatActivity) baseFragment).getUndoView().showWithAction(0L, 74, (Runnable) null);
            } else {
                BulletinFactory.of(baseFragment).createReportSent(resourcesProvider).show();
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$27 */
    /* loaded from: classes3.dex */
    public class AnonymousClass27 extends ReportAlert {
        final /* synthetic */ long val$dialog_id;
        final /* synthetic */ int val$messageId;
        final /* synthetic */ BaseFragment val$parentFragment;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass27(Context context, int i, BaseFragment baseFragment, int i2, long j) {
            super(context, i);
            this.val$parentFragment = baseFragment;
            this.val$messageId = i2;
            this.val$dialog_id = j;
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet
        public void dismissInternal() {
            super.dismissInternal();
            BaseFragment baseFragment = this.val$parentFragment;
            if (baseFragment instanceof ChatActivity) {
                ((ChatActivity) baseFragment).checkAdjustResize();
            }
        }

        @Override // org.telegram.ui.Components.ReportAlert
        protected void onSend(int i, String str) {
            ArrayList arrayList = new ArrayList();
            int i2 = this.val$messageId;
            if (i2 != 0) {
                arrayList.add(Integer.valueOf(i2));
            }
            AlertsCreator.sendReport(MessagesController.getInstance(UserConfig.selectedAccount).getInputPeer(this.val$dialog_id), i, str, arrayList);
            BaseFragment baseFragment = this.val$parentFragment;
            if (baseFragment instanceof ChatActivity) {
                ((ChatActivity) baseFragment).getUndoView().showWithAction(0L, 74, (Runnable) null);
            }
        }
    }

    private static String getFloodWaitString(String str) {
        String str2;
        int intValue = Utilities.parseInt((CharSequence) str).intValue();
        if (intValue < 60) {
            str2 = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
        } else {
            str2 = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
        }
        return LocaleController.formatString("FloodWaitTime", 2131625950, str2);
    }

    public static void showFloodWaitAlert(String str, BaseFragment baseFragment) {
        String str2;
        if (str == null || !str.startsWith("FLOOD_WAIT") || baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        int intValue = Utilities.parseInt((CharSequence) str).intValue();
        if (intValue < 60) {
            str2 = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
        } else {
            str2 = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", 2131624384));
        builder.setMessage(LocaleController.formatString("FloodWaitTime", 2131625950, str2));
        builder.setPositiveButton(LocaleController.getString("OK", 2131627127), null);
        baseFragment.showDialog(builder.create(), true, null);
    }

    public static void showSendMediaAlert(int i, BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider) {
        if (i == 0) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity(), resourcesProvider);
        builder.setTitle(LocaleController.getString("UnableForward", 2131628789));
        if (i == 1) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedStickers", 2131625705));
        } else if (i == 2) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedMedia", 2131625699));
        } else if (i == 3) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedPolls", 2131625701));
        } else if (i == 4) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedStickersAll", 2131625706));
        } else if (i == 5) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedMediaAll", 2131625700));
        } else if (i == 6) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedPollsAll", 2131625702));
        } else if (i == 7) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedPrivacyVoiceMessages", 2131625704));
        } else if (i == 8) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedPrivacyVideoMessages", 2131625703));
        }
        builder.setPositiveButton(LocaleController.getString("OK", 2131627127), null);
        baseFragment.showDialog(builder.create(), true, null);
    }

    public static void showAddUserAlert(String str, BaseFragment baseFragment, boolean z, TLObject tLObject) {
        if (str == null || baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", 2131624384));
        char c = 65535;
        switch (str.hashCode()) {
            case -2120721660:
                if (str.equals("CHANNELS_ADMIN_LOCATED_TOO_MUCH")) {
                    c = 0;
                    break;
                }
                break;
            case -2012133105:
                if (str.equals("CHANNELS_ADMIN_PUBLIC_TOO_MUCH")) {
                    c = 1;
                    break;
                }
                break;
            case -1763467626:
                if (str.equals("USERS_TOO_FEW")) {
                    c = 2;
                    break;
                }
                break;
            case -538116776:
                if (str.equals("USER_BLOCKED")) {
                    c = 3;
                    break;
                }
                break;
            case -512775857:
                if (str.equals("USER_RESTRICTED")) {
                    c = 4;
                    break;
                }
                break;
            case -454039871:
                if (str.equals("PEER_FLOOD")) {
                    c = 5;
                    break;
                }
                break;
            case -420079733:
                if (str.equals("BOTS_TOO_MUCH")) {
                    c = 6;
                    break;
                }
                break;
            case 98635865:
                if (str.equals("USER_KICKED")) {
                    c = 7;
                    break;
                }
                break;
            case 517420851:
                if (str.equals("USER_BOT")) {
                    c = '\b';
                    break;
                }
                break;
            case 845559454:
                if (str.equals("YOU_BLOCKED_USER")) {
                    c = '\t';
                    break;
                }
                break;
            case 916342611:
                if (str.equals("USER_ADMIN_INVALID")) {
                    c = '\n';
                    break;
                }
                break;
            case 1047173446:
                if (str.equals("CHAT_ADMIN_BAN_REQUIRED")) {
                    c = 11;
                    break;
                }
                break;
            case 1167301807:
                if (str.equals("USERS_TOO_MUCH")) {
                    c = '\f';
                    break;
                }
                break;
            case 1227003815:
                if (str.equals("USER_ID_INVALID")) {
                    c = '\r';
                    break;
                }
                break;
            case 1253103379:
                if (str.equals("ADMINS_TOO_MUCH")) {
                    c = 14;
                    break;
                }
                break;
            case 1355367367:
                if (str.equals("CHANNELS_TOO_MUCH")) {
                    c = 15;
                    break;
                }
                break;
            case 1377621075:
                if (str.equals("USER_CHANNELS_TOO_MUCH")) {
                    c = 16;
                    break;
                }
                break;
            case 1623167701:
                if (str.equals("USER_NOT_MUTUAL_CONTACT")) {
                    c = 17;
                    break;
                }
                break;
            case 1754587486:
                if (str.equals("CHAT_ADMIN_INVITE_REQUIRED")) {
                    c = 18;
                    break;
                }
                break;
            case 1916725894:
                if (str.equals("USER_PRIVACY_RESTRICTED")) {
                    c = 19;
                    break;
                }
                break;
            case 1965565720:
                if (str.equals("USER_ALREADY_PARTICIPANT")) {
                    c = 20;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                builder.setMessage(LocaleController.getString("LocatedChannelsTooMuch", 2131626536));
                break;
            case 1:
                builder.setMessage(LocaleController.getString("PublicChannelsTooMuch", 2131627826));
                break;
            case 2:
                builder.setMessage(LocaleController.getString("CreateGroupError", 2131625286));
                break;
            case 3:
            case '\b':
            case '\r':
                if (z) {
                    builder.setMessage(LocaleController.getString("ChannelUserCantAdd", 2131624990));
                    break;
                } else {
                    builder.setMessage(LocaleController.getString("GroupUserCantAdd", 2131626156));
                    break;
                }
            case 4:
                builder.setMessage(LocaleController.getString("UserRestricted", 2131628883));
                break;
            case 5:
                builder.setMessage(LocaleController.getString("NobodyLikesSpam2", 2131626934));
                builder.setNegativeButton(LocaleController.getString("MoreInfo", 2131626789), new AlertsCreator$$ExternalSyntheticLambda25(baseFragment));
                break;
            case 6:
                if (z) {
                    builder.setMessage(LocaleController.getString("ChannelUserCantBot", 2131624992));
                    break;
                } else {
                    builder.setMessage(LocaleController.getString("GroupUserCantBot", 2131626158));
                    break;
                }
            case 7:
            case 11:
                if (tLObject instanceof TLRPC$TL_channels_inviteToChannel) {
                    builder.setMessage(LocaleController.getString("AddUserErrorBlacklisted", 2131624309));
                    break;
                } else {
                    builder.setMessage(LocaleController.getString("AddAdminErrorBlacklisted", 2131624244));
                    break;
                }
            case '\t':
                builder.setMessage(LocaleController.getString("YouBlockedUser", 2131629342));
                break;
            case '\n':
                builder.setMessage(LocaleController.getString("AddBannedErrorAdmin", 2131624254));
                break;
            case '\f':
                if (z) {
                    builder.setMessage(LocaleController.getString("ChannelUserAddLimit", 2131624989));
                    break;
                } else {
                    builder.setMessage(LocaleController.getString("GroupUserAddLimit", 2131626155));
                    break;
                }
            case 14:
                if (z) {
                    builder.setMessage(LocaleController.getString("ChannelUserCantAdmin", 2131624991));
                    break;
                } else {
                    builder.setMessage(LocaleController.getString("GroupUserCantAdmin", 2131626157));
                    break;
                }
            case 15:
                builder.setTitle(LocaleController.getString("ChannelTooMuchTitle", 2131624985));
                if (tLObject instanceof TLRPC$TL_channels_createChannel) {
                    builder.setMessage(LocaleController.getString("ChannelTooMuch", 2131624983));
                    break;
                } else {
                    builder.setMessage(LocaleController.getString("ChannelTooMuchJoin", 2131624984));
                    break;
                }
            case 16:
                builder.setTitle(LocaleController.getString("ChannelTooMuchTitle", 2131624985));
                builder.setMessage(LocaleController.getString("UserChannelTooMuchJoin", 2131628880));
                break;
            case 17:
                if (z) {
                    builder.setMessage(LocaleController.getString("ChannelUserLeftError", 2131624993));
                    break;
                } else {
                    builder.setMessage(LocaleController.getString("GroupUserLeftError", 2131626159));
                    break;
                }
            case 18:
                builder.setMessage(LocaleController.getString("AddAdminErrorNotAMember", 2131624245));
                break;
            case 19:
                if (z) {
                    builder.setMessage(LocaleController.getString("InviteToChannelError", 2131626326));
                    break;
                } else {
                    builder.setMessage(LocaleController.getString("InviteToGroupError", 2131626328));
                    break;
                }
            case 20:
                builder.setTitle(LocaleController.getString("VoipGroupVoiceChat", 2131629205));
                builder.setMessage(LocaleController.getString("VoipGroupInviteAlreadyParticipant", 2131629130));
                break;
            default:
                builder.setMessage(LocaleController.getString("ErrorOccurred", 2131625695) + "\n" + str);
                break;
        }
        builder.setPositiveButton(LocaleController.getString("OK", 2131627127), null);
        baseFragment.showDialog(builder.create(), true, null);
    }

    public static /* synthetic */ void lambda$showAddUserAlert$87(BaseFragment baseFragment, DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(baseFragment.getCurrentAccount()).openByUserName("spambot", baseFragment, 1);
    }

    public static Dialog createColorSelectDialog(Activity activity, long j, int i, Runnable runnable) {
        return createColorSelectDialog(activity, j, i, runnable, null);
    }

    public static Dialog createColorSelectDialog(Activity activity, long j, int i, Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        int i2;
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        if (j != 0) {
            if (notificationsSettings.contains("color_" + j)) {
                i2 = notificationsSettings.getInt("color_" + j, -16776961);
            } else if (DialogObject.isChatDialog(j)) {
                i2 = notificationsSettings.getInt("GroupLed", -16776961);
            } else {
                i2 = notificationsSettings.getInt("MessagesLed", -16776961);
            }
        } else if (i == 1) {
            i2 = notificationsSettings.getInt("MessagesLed", -16776961);
        } else if (i == 0) {
            i2 = notificationsSettings.getInt("GroupLed", -16776961);
        } else {
            i2 = notificationsSettings.getInt("ChannelLed", -16776961);
        }
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        String[] strArr = {LocaleController.getString("ColorRed", 2131625199), LocaleController.getString("ColorOrange", 2131625192), LocaleController.getString("ColorYellow", 2131625207), LocaleController.getString("ColorGreen", 2131625191), LocaleController.getString("ColorCyan", 2131625189), LocaleController.getString("ColorBlue", 2131625188), LocaleController.getString("ColorViolet", 2131625205), LocaleController.getString("ColorPink", 2131625198), LocaleController.getString("ColorWhite", 2131625206)};
        int[] iArr = {i2};
        int i3 = 0;
        for (int i4 = 9; i3 < i4; i4 = 9) {
            RadioColorCell radioColorCell = new RadioColorCell(activity, resourcesProvider);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag(Integer.valueOf(i3));
            int[] iArr2 = TextColorCell.colors;
            radioColorCell.setCheckColor(iArr2[i3], iArr2[i3]);
            radioColorCell.setTextAndValue(strArr[i3], i2 == TextColorCell.colorsToSave[i3]);
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda49(linearLayout, iArr));
            i3++;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, resourcesProvider);
        builder.setTitle(LocaleController.getString("LedColor", 2131626443));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Set", 2131628297), new AlertsCreator$$ExternalSyntheticLambda5(j, iArr, i, runnable));
        builder.setNeutralButton(LocaleController.getString("LedDisabled", 2131626444), new AlertsCreator$$ExternalSyntheticLambda2(j, i, runnable));
        if (j != 0) {
            builder.setNegativeButton(LocaleController.getString("Default", 2131625382), new AlertsCreator$$ExternalSyntheticLambda3(j, runnable));
        }
        return builder.create();
    }

    public static /* synthetic */ void lambda$createColorSelectDialog$88(LinearLayout linearLayout, int[] iArr, View view) {
        int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RadioColorCell radioColorCell = (RadioColorCell) linearLayout.getChildAt(i);
            radioColorCell.setChecked(radioColorCell == view, true);
        }
        iArr[0] = TextColorCell.colorsToSave[((Integer) view.getTag()).intValue()];
    }

    public static /* synthetic */ void lambda$createColorSelectDialog$89(long j, int[] iArr, int i, Runnable runnable, DialogInterface dialogInterface, int i2) {
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
        if (j != 0) {
            edit.putInt("color_" + j, iArr[0]);
            NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannel(j);
        } else {
            if (i == 1) {
                edit.putInt("MessagesLed", iArr[0]);
            } else if (i == 0) {
                edit.putInt("GroupLed", iArr[0]);
            } else {
                edit.putInt("ChannelLed", iArr[0]);
            }
            NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannelGlobal(i);
        }
        edit.commit();
        if (runnable != null) {
            runnable.run();
        }
    }

    public static /* synthetic */ void lambda$createColorSelectDialog$90(long j, int i, Runnable runnable, DialogInterface dialogInterface, int i2) {
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
        if (j != 0) {
            edit.putInt("color_" + j, 0);
        } else if (i == 1) {
            edit.putInt("MessagesLed", 0);
        } else if (i == 0) {
            edit.putInt("GroupLed", 0);
        } else {
            edit.putInt("ChannelLed", 0);
        }
        edit.commit();
        if (runnable != null) {
            runnable.run();
        }
    }

    public static /* synthetic */ void lambda$createColorSelectDialog$91(long j, Runnable runnable, DialogInterface dialogInterface, int i) {
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
        edit.remove("color_" + j);
        edit.commit();
        if (runnable != null) {
            runnable.run();
        }
    }

    public static Dialog createVibrationSelectDialog(Activity activity, long j, boolean z, boolean z2, Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        String str;
        if (j != 0) {
            str = "vibrate_" + j;
        } else {
            str = z ? "vibrate_group" : "vibrate_messages";
        }
        return createVibrationSelectDialog(activity, j, str, runnable, resourcesProvider);
    }

    public static Dialog createVibrationSelectDialog(Activity activity, long j, String str, Runnable runnable) {
        return createVibrationSelectDialog(activity, j, str, runnable, null);
    }

    public static Dialog createVibrationSelectDialog(Activity activity, long j, String str, Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        String[] strArr;
        Activity activity2 = activity;
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        int[] iArr = new int[1];
        int i = 0;
        if (j != 0) {
            iArr[0] = notificationsSettings.getInt(str, 0);
            if (iArr[0] == 3) {
                iArr[0] = 2;
            } else if (iArr[0] == 2) {
                iArr[0] = 3;
            }
            strArr = new String[]{LocaleController.getString("VibrationDefault", 2131628952), LocaleController.getString("Short", 2131628390), LocaleController.getString("Long", 2131626554), LocaleController.getString("VibrationDisabled", 2131628953)};
        } else {
            iArr[0] = notificationsSettings.getInt(str, 0);
            if (iArr[0] == 0) {
                iArr[0] = 1;
            } else if (iArr[0] == 1) {
                iArr[0] = 2;
            } else if (iArr[0] == 2) {
                iArr[0] = 0;
            }
            strArr = new String[]{LocaleController.getString("VibrationDisabled", 2131628953), LocaleController.getString("VibrationDefault", 2131628952), LocaleController.getString("Short", 2131628390), LocaleController.getString("Long", 2131626554), LocaleController.getString("OnlyIfSilent", 2131627139)};
        }
        String[] strArr2 = strArr;
        LinearLayout linearLayout = new LinearLayout(activity2);
        linearLayout.setOrientation(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity2, resourcesProvider);
        int i2 = 0;
        while (i2 < strArr2.length) {
            RadioColorCell radioColorCell = new RadioColorCell(activity2, resourcesProvider);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), i, AndroidUtilities.dp(4.0f), i);
            radioColorCell.setTag(Integer.valueOf(i2));
            radioColorCell.setCheckColor(Theme.getColor("radioBackground", resourcesProvider), Theme.getColor("dialogRadioBackgroundChecked", resourcesProvider));
            radioColorCell.setTextAndValue(strArr2[i2], iArr[i] == i2);
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda59(iArr, j, str, builder, runnable));
            i2++;
            activity2 = activity;
            i = 0;
        }
        builder.setTitle(LocaleController.getString("Vibrate", 2131628951));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", 2131624832), null);
        return builder.create();
    }

    public static /* synthetic */ void lambda$createVibrationSelectDialog$92(int[] iArr, long j, String str, AlertDialog.Builder builder, Runnable runnable, View view) {
        iArr[0] = ((Integer) view.getTag()).intValue();
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
        if (j != 0) {
            if (iArr[0] == 0) {
                edit.putInt(str, 0);
            } else if (iArr[0] == 1) {
                edit.putInt(str, 1);
            } else if (iArr[0] == 2) {
                edit.putInt(str, 3);
            } else if (iArr[0] == 3) {
                edit.putInt(str, 2);
            }
            NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannel(j);
        } else {
            if (iArr[0] == 0) {
                edit.putInt(str, 2);
            } else if (iArr[0] == 1) {
                edit.putInt(str, 0);
            } else if (iArr[0] == 2) {
                edit.putInt(str, 1);
            } else if (iArr[0] == 3) {
                edit.putInt(str, 3);
            } else if (iArr[0] == 4) {
                edit.putInt(str, 4);
            }
            if (str.equals("vibrate_channel")) {
                NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannelGlobal(2);
            } else if (str.equals("vibrate_group")) {
                NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannelGlobal(0);
            } else {
                NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannelGlobal(1);
            }
        }
        edit.commit();
        builder.getDismissRunnable().run();
        if (runnable != null) {
            runnable.run();
        }
    }

    public static Dialog createLocationUpdateDialog(Activity activity, TLRPC$User tLRPC$User, MessagesStorage.IntCallback intCallback, Theme.ResourcesProvider resourcesProvider) {
        int[] iArr = new int[1];
        String[] strArr = {LocaleController.getString("SendLiveLocationFor15m", 2131628254), LocaleController.getString("SendLiveLocationFor1h", 2131628255), LocaleController.getString("SendLiveLocationFor8h", 2131628256)};
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        TextView textView = new TextView(activity);
        if (tLRPC$User != null) {
            textView.setText(LocaleController.formatString("LiveLocationAlertPrivate", 2131626517, UserObject.getFirstName(tLRPC$User)));
        } else {
            textView.setText(LocaleController.getString("LiveLocationAlertGroup", 2131626516));
        }
        textView.setTextColor(resourcesProvider != null ? resourcesProvider.getColorOrDefault("dialogTextBlack") : Theme.getColor("dialogTextBlack"));
        textView.setTextSize(1, 16.0f);
        int i = 5;
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        if (!LocaleController.isRTL) {
            i = 3;
        }
        linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, i | 48, 24, 0, 24, 8));
        int i2 = 0;
        while (i2 < 3) {
            RadioColorCell radioColorCell = new RadioColorCell(activity, resourcesProvider);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag(Integer.valueOf(i2));
            radioColorCell.setCheckColor(resourcesProvider != null ? resourcesProvider.getColorOrDefault("radioBackground") : Theme.getColor("radioBackground"), resourcesProvider != null ? resourcesProvider.getColorOrDefault("dialogRadioBackgroundChecked") : Theme.getColor("dialogRadioBackgroundChecked"));
            radioColorCell.setTextAndValue(strArr[i2], iArr[0] == i2);
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda61(iArr, linearLayout));
            i2++;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, resourcesProvider);
        builder.setTopImage(new ShareLocationDrawable(activity, 0), resourcesProvider != null ? resourcesProvider.getColorOrDefault("dialogTopBackground") : Theme.getColor("dialogTopBackground"));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("ShareFile", 2131628337), new AlertsCreator$$ExternalSyntheticLambda37(iArr, intCallback));
        builder.setNeutralButton(LocaleController.getString("Cancel", 2131624832), null);
        return builder.create();
    }

    public static /* synthetic */ void lambda$createLocationUpdateDialog$93(int[] iArr, LinearLayout linearLayout, View view) {
        iArr[0] = ((Integer) view.getTag()).intValue();
        int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = linearLayout.getChildAt(i);
            if (childAt instanceof RadioColorCell) {
                ((RadioColorCell) childAt).setChecked(childAt == view, true);
            }
        }
    }

    public static /* synthetic */ void lambda$createLocationUpdateDialog$94(int[] iArr, MessagesStorage.IntCallback intCallback, DialogInterface dialogInterface, int i) {
        int i2;
        if (iArr[0] == 0) {
            i2 = 900;
        } else {
            i2 = iArr[0] == 1 ? 3600 : 28800;
        }
        intCallback.run(i2);
    }

    public static AlertDialog.Builder createBackgroundLocationPermissionDialog(Activity activity, TLRPC$User tLRPC$User, Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        if (activity == null || Build.VERSION.SDK_INT < 29) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, resourcesProvider);
        String readRes = RLottieDrawable.readRes(null, Theme.getCurrentTheme().isDark() ? 2131558502 : 2131558501);
        String readRes2 = RLottieDrawable.readRes(null, Theme.getCurrentTheme().isDark() ? 2131558504 : 2131558503);
        FrameLayout frameLayout = new FrameLayout(activity);
        frameLayout.setClipToOutline(true);
        frameLayout.setOutlineProvider(new AnonymousClass28());
        View view = new View(activity);
        view.setBackground(SvgHelper.getDrawable(readRes));
        frameLayout.addView(view, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        View view2 = new View(activity);
        view2.setBackground(SvgHelper.getDrawable(readRes2));
        frameLayout.addView(view2, LayoutHelper.createFrame(60, 82.0f, 17, 0.0f, 0.0f, 0.0f, 0.0f));
        BackupImageView backupImageView = new BackupImageView(activity);
        backupImageView.setRoundRadius(AndroidUtilities.dp(26.0f));
        backupImageView.setForUserOrChat(tLRPC$User, new AvatarDrawable(tLRPC$User));
        frameLayout.addView(backupImageView, LayoutHelper.createFrame(52, 52.0f, 17, 0.0f, 0.0f, 0.0f, 11.0f));
        builder.setTopView(frameLayout);
        builder.setTopViewAspectRatio(0.37820512f);
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString(2131627515)));
        builder.setPositiveButton(LocaleController.getString(2131625262), new AlertsCreator$$ExternalSyntheticLambda7(activity));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), new AlertsCreator$$ExternalSyntheticLambda14(runnable));
        return builder;
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$28 */
    /* loaded from: classes3.dex */
    public class AnonymousClass28 extends ViewOutlineProvider {
        AnonymousClass28() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() + AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
        }
    }

    public static /* synthetic */ void lambda$createBackgroundLocationPermissionDialog$95(Activity activity, DialogInterface dialogInterface, int i) {
        if (activity.checkSelfPermission("android.permission.ACCESS_BACKGROUND_LOCATION") != 0) {
            activity.requestPermissions(new String[]{"android.permission.ACCESS_BACKGROUND_LOCATION"}, 30);
        }
    }

    public static AlertDialog.Builder createGigagroupConvertAlert(Activity activity, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String readRes = RLottieDrawable.readRes(null, 2131558457);
        FrameLayout frameLayout = new FrameLayout(activity);
        if (Build.VERSION.SDK_INT >= 21) {
            frameLayout.setClipToOutline(true);
            frameLayout.setOutlineProvider(new AnonymousClass29());
        }
        View view = new View(activity);
        view.setBackground(new BitmapDrawable(SvgHelper.getBitmap(readRes, AndroidUtilities.dp(320.0f), AndroidUtilities.dp(127.17949f), false)));
        frameLayout.addView(view, LayoutHelper.createFrame(-1, -1.0f, 0, -1.0f, -1.0f, -1.0f, -1.0f));
        builder.setTopView(frameLayout);
        builder.setTopViewAspectRatio(0.3974359f);
        builder.setTitle(LocaleController.getString("GigagroupAlertTitle", 2131626111));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString("GigagroupAlertText", 2131626110)));
        builder.setPositiveButton(LocaleController.getString("GigagroupAlertLearnMore", 2131626109), onClickListener);
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), onClickListener2);
        return builder;
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$29 */
    /* loaded from: classes3.dex */
    public class AnonymousClass29 extends ViewOutlineProvider {
        AnonymousClass29() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() + AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
        }
    }

    public static AlertDialog.Builder createDrawOverlayPermissionDialog(Activity activity, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String readRes = RLottieDrawable.readRes(null, 2131558515);
        FrameLayout frameLayout = new FrameLayout(activity);
        frameLayout.setBackground(new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{-14535089, -14527894}));
        frameLayout.setClipToOutline(true);
        frameLayout.setOutlineProvider(new AnonymousClass30());
        View view = new View(activity);
        view.setBackground(new BitmapDrawable(SvgHelper.getBitmap(readRes, AndroidUtilities.dp(320.0f), AndroidUtilities.dp(161.36752f), false)));
        frameLayout.addView(view, LayoutHelper.createFrame(-1, -1.0f, 0, -1.0f, -1.0f, -1.0f, -1.0f));
        builder.setTopView(frameLayout);
        builder.setTitle(LocaleController.getString("PermissionDrawAboveOtherAppsTitle", 2131627520));
        builder.setMessage(LocaleController.getString("PermissionDrawAboveOtherApps", 2131627517));
        builder.setPositiveButton(LocaleController.getString("Enable", 2131625654), new AlertsCreator$$ExternalSyntheticLambda6(activity));
        builder.notDrawBackgroundOnTopView(true);
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), onClickListener);
        builder.setTopViewAspectRatio(0.50427353f);
        return builder;
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$30 */
    /* loaded from: classes3.dex */
    public class AnonymousClass30 extends ViewOutlineProvider {
        AnonymousClass30() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() + AndroidUtilities.dp(6.0f), AndroidUtilities.dpf2(6.0f));
        }
    }

    public static /* synthetic */ void lambda$createDrawOverlayPermissionDialog$97(Activity activity, DialogInterface dialogInterface, int i) {
        if (activity == null || Build.VERSION.SDK_INT < 23) {
            return;
        }
        try {
            activity.startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + activity.getPackageName())));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static AlertDialog.Builder createDrawOverlayGroupCallPermissionDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String readRes = RLottieDrawable.readRes(null, 2131558516);
        GroupCallPipButton groupCallPipButton = new GroupCallPipButton(context, 0, true);
        groupCallPipButton.setImportantForAccessibility(2);
        AnonymousClass31 anonymousClass31 = new AnonymousClass31(context, groupCallPipButton);
        anonymousClass31.setBackground(new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{-15128003, -15118002}));
        anonymousClass31.setClipToOutline(true);
        anonymousClass31.setOutlineProvider(new AnonymousClass32());
        View view = new View(context);
        view.setBackground(new BitmapDrawable(SvgHelper.getBitmap(readRes, AndroidUtilities.dp(320.0f), AndroidUtilities.dp(184.61539f), false)));
        anonymousClass31.addView(view, LayoutHelper.createFrame(-1, -1.0f, 0, -1.0f, -1.0f, -1.0f, -1.0f));
        anonymousClass31.addView(groupCallPipButton, LayoutHelper.createFrame(117, 117.0f));
        builder.setTopView(anonymousClass31);
        builder.setTitle(LocaleController.getString("PermissionDrawAboveOtherAppsGroupCallTitle", 2131627519));
        builder.setMessage(LocaleController.getString("PermissionDrawAboveOtherAppsGroupCall", 2131627518));
        builder.setPositiveButton(LocaleController.getString("Enable", 2131625654), new AlertsCreator$$ExternalSyntheticLambda11(context));
        builder.notDrawBackgroundOnTopView(true);
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        builder.setTopViewAspectRatio(0.5769231f);
        return builder;
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$31 */
    /* loaded from: classes3.dex */
    public class AnonymousClass31 extends FrameLayout {
        final /* synthetic */ GroupCallPipButton val$button;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass31(Context context, GroupCallPipButton groupCallPipButton) {
            super(context);
            this.val$button = groupCallPipButton;
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            this.val$button.setTranslationY((getMeasuredHeight() * 0.28f) - (this.val$button.getMeasuredWidth() / 2.0f));
            this.val$button.setTranslationX((getMeasuredWidth() * 0.82f) - (this.val$button.getMeasuredWidth() / 2.0f));
        }
    }

    /* renamed from: org.telegram.ui.Components.AlertsCreator$32 */
    /* loaded from: classes3.dex */
    public class AnonymousClass32 extends ViewOutlineProvider {
        AnonymousClass32() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() + AndroidUtilities.dp(6.0f), AndroidUtilities.dpf2(6.0f));
        }
    }

    public static /* synthetic */ void lambda$createDrawOverlayGroupCallPermissionDialog$98(Context context, DialogInterface dialogInterface, int i) {
        if (context != null) {
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + context.getPackageName()));
                    Activity findActivity = AndroidUtilities.findActivity(context);
                    if (findActivity instanceof LaunchActivity) {
                        findActivity.startActivityForResult(intent, 105);
                    } else {
                        context.startActivity(intent);
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static AlertDialog.Builder createContactsPermissionDialog(Activity activity, MessagesStorage.IntCallback intCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTopAnimation(2131558507, 72, false, Theme.getColor("dialogTopBackground"));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString("ContactsPermissionAlert", 2131625259)));
        builder.setPositiveButton(LocaleController.getString("ContactsPermissionAlertContinue", 2131625260), new AlertsCreator$$ExternalSyntheticLambda19(intCallback));
        builder.setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", 2131625261), new AlertsCreator$$ExternalSyntheticLambda20(intCallback));
        return builder;
    }

    public static Dialog createFreeSpaceDialog(LaunchActivity launchActivity) {
        int[] iArr = new int[1];
        int i = SharedConfig.keepMedia;
        int i2 = 3;
        if (i == 2) {
            iArr[0] = 3;
        } else if (i == 0) {
            iArr[0] = 1;
        } else if (i == 1) {
            iArr[0] = 2;
        } else if (i == 3) {
            iArr[0] = 0;
        }
        String[] strArr = {LocaleController.formatPluralString("Days", 3, new Object[0]), LocaleController.formatPluralString("Weeks", 1, new Object[0]), LocaleController.formatPluralString("Months", 1, new Object[0]), LocaleController.getString("LowDiskSpaceNeverRemove", 2131626558)};
        LinearLayout linearLayout = new LinearLayout(launchActivity);
        linearLayout.setOrientation(1);
        TextView textView = new TextView(launchActivity);
        textView.setText(LocaleController.getString("LowDiskSpaceTitle2", 2131626560));
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        textView.setTextSize(1, 16.0f);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        if (LocaleController.isRTL) {
            i2 = 5;
        }
        linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, i2 | 48, 24, 0, 24, 8));
        int i3 = 0;
        while (i3 < 4) {
            RadioColorCell radioColorCell = new RadioColorCell(launchActivity);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag(Integer.valueOf(i3));
            radioColorCell.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
            radioColorCell.setTextAndValue(strArr[i3], iArr[0] == i3);
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda60(iArr, linearLayout));
            i3++;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(launchActivity);
        builder.setTitle(LocaleController.getString("LowDiskSpaceTitle", 2131626559));
        builder.setMessage(LocaleController.getString("LowDiskSpaceMessage", 2131626557));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("OK", 2131627127), new AlertsCreator$$ExternalSyntheticLambda35(iArr));
        builder.setNeutralButton(LocaleController.getString("ClearMediaCache", 2131625160), new AlertsCreator$$ExternalSyntheticLambda30(launchActivity));
        return builder.create();
    }

    public static /* synthetic */ void lambda$createFreeSpaceDialog$101(int[] iArr, LinearLayout linearLayout, View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        if (intValue == 0) {
            iArr[0] = 3;
        } else if (intValue == 1) {
            iArr[0] = 0;
        } else if (intValue == 2) {
            iArr[0] = 1;
        } else if (intValue == 3) {
            iArr[0] = 2;
        }
        int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = linearLayout.getChildAt(i);
            if (childAt instanceof RadioColorCell) {
                ((RadioColorCell) childAt).setChecked(childAt == view, true);
            }
        }
    }

    public static /* synthetic */ void lambda$createFreeSpaceDialog$102(int[] iArr, DialogInterface dialogInterface, int i) {
        SharedConfig.setKeepMedia(iArr[0]);
    }

    public static /* synthetic */ void lambda$createFreeSpaceDialog$103(LaunchActivity launchActivity, DialogInterface dialogInterface, int i) {
        launchActivity.lambda$runLinkRequest$59(new CacheControlActivity());
    }

    public static Dialog createPrioritySelectDialog(Activity activity, long j, int i, Runnable runnable) {
        return createPrioritySelectDialog(activity, j, i, runnable, null);
    }

    public static Dialog createPrioritySelectDialog(Activity activity, long j, int i, Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        int i2;
        String[] strArr;
        char c;
        int i3;
        Activity activity2 = activity;
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        int[] iArr = new int[1];
        int i4 = 0;
        if (j != 0) {
            iArr[0] = notificationsSettings.getInt("priority_" + j, 3);
            if (iArr[0] == 3) {
                iArr[0] = 0;
            } else if (iArr[0] == 4) {
                iArr[0] = 1;
            } else {
                i3 = 5;
                if (iArr[0] == 5) {
                    iArr[0] = 2;
                } else if (iArr[0] == 0) {
                    iArr[0] = 3;
                } else {
                    iArr[0] = 4;
                }
                String[] strArr2 = new String[i3];
                strArr2[0] = LocaleController.getString("NotificationsPrioritySettings", 2131627108);
                i2 = 1;
                strArr2[1] = LocaleController.getString("NotificationsPriorityLow", 2131627106);
                strArr2[2] = LocaleController.getString("NotificationsPriorityMedium", 2131627107);
                strArr2[3] = LocaleController.getString("NotificationsPriorityHigh", 2131627105);
                strArr2[4] = LocaleController.getString("NotificationsPriorityUrgent", 2131627109);
                strArr = strArr2;
            }
            i3 = 5;
            String[] strArr22 = new String[i3];
            strArr22[0] = LocaleController.getString("NotificationsPrioritySettings", 2131627108);
            i2 = 1;
            strArr22[1] = LocaleController.getString("NotificationsPriorityLow", 2131627106);
            strArr22[2] = LocaleController.getString("NotificationsPriorityMedium", 2131627107);
            strArr22[3] = LocaleController.getString("NotificationsPriorityHigh", 2131627105);
            strArr22[4] = LocaleController.getString("NotificationsPriorityUrgent", 2131627109);
            strArr = strArr22;
        } else {
            if (i == 1) {
                iArr[0] = notificationsSettings.getInt("priority_messages", 1);
            } else if (i == 0) {
                iArr[0] = notificationsSettings.getInt("priority_group", 1);
            } else if (i == 2) {
                iArr[0] = notificationsSettings.getInt("priority_channel", 1);
            }
            if (iArr[0] == 4) {
                iArr[0] = 0;
            } else if (iArr[0] == 5) {
                iArr[0] = 1;
            } else {
                if (iArr[0] == 0) {
                    c = 2;
                    iArr[0] = 2;
                } else {
                    c = 2;
                    iArr[0] = 3;
                }
                String[] strArr3 = new String[4];
                strArr3[0] = LocaleController.getString("NotificationsPriorityLow", 2131627106);
                i2 = 1;
                strArr3[1] = LocaleController.getString("NotificationsPriorityMedium", 2131627107);
                strArr3[c] = LocaleController.getString("NotificationsPriorityHigh", 2131627105);
                strArr3[3] = LocaleController.getString("NotificationsPriorityUrgent", 2131627109);
                strArr = strArr3;
            }
            c = 2;
            String[] strArr32 = new String[4];
            strArr32[0] = LocaleController.getString("NotificationsPriorityLow", 2131627106);
            i2 = 1;
            strArr32[1] = LocaleController.getString("NotificationsPriorityMedium", 2131627107);
            strArr32[c] = LocaleController.getString("NotificationsPriorityHigh", 2131627105);
            strArr32[3] = LocaleController.getString("NotificationsPriorityUrgent", 2131627109);
            strArr = strArr32;
        }
        LinearLayout linearLayout = new LinearLayout(activity2);
        linearLayout.setOrientation(i2);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity2, resourcesProvider);
        int i5 = 0;
        while (i5 < strArr.length) {
            RadioColorCell radioColorCell = new RadioColorCell(activity2, resourcesProvider);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), i4, AndroidUtilities.dp(4.0f), i4);
            radioColorCell.setTag(Integer.valueOf(i5));
            radioColorCell.setCheckColor(Theme.getColor("radioBackground", resourcesProvider), Theme.getColor("dialogRadioBackgroundChecked", resourcesProvider));
            radioColorCell.setTextAndValue(strArr[i5], iArr[i4] == i5);
            linearLayout.addView(radioColorCell);
            AlertDialog.Builder builder2 = builder;
            radioColorCell.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda58(iArr, j, i, notificationsSettings, builder2, runnable));
            i5++;
            activity2 = activity;
            linearLayout = linearLayout;
            strArr = strArr;
            builder = builder2;
            i4 = 0;
        }
        AlertDialog.Builder builder3 = builder;
        builder3.setTitle(LocaleController.getString("NotificationsImportance", 2131627092));
        builder3.setView(linearLayout);
        builder3.setPositiveButton(LocaleController.getString("Cancel", 2131624832), null);
        return builder3.create();
    }

    public static /* synthetic */ void lambda$createPrioritySelectDialog$104(int[] iArr, long j, int i, SharedPreferences sharedPreferences, AlertDialog.Builder builder, Runnable runnable, View view) {
        int i2 = 0;
        iArr[0] = ((Integer) view.getTag()).intValue();
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
        int i3 = 5;
        if (j != 0) {
            if (iArr[0] == 0) {
                i2 = 3;
            } else if (iArr[0] == 1) {
                i2 = 4;
            } else if (iArr[0] == 2) {
                i2 = 5;
            } else if (iArr[0] != 3) {
                i2 = 1;
            }
            edit.putInt("priority_" + j, i2);
            NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannel(j);
        } else {
            if (iArr[0] == 0) {
                i3 = 4;
            } else if (iArr[0] != 1) {
                i3 = iArr[0] == 2 ? 0 : 1;
            }
            if (i == 1) {
                edit.putInt("priority_messages", i3);
                iArr[0] = sharedPreferences.getInt("priority_messages", 1);
            } else if (i == 0) {
                edit.putInt("priority_group", i3);
                iArr[0] = sharedPreferences.getInt("priority_group", 1);
            } else if (i == 2) {
                edit.putInt("priority_channel", i3);
                iArr[0] = sharedPreferences.getInt("priority_channel", 1);
            }
            NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannelGlobal(i);
        }
        edit.commit();
        builder.getDismissRunnable().run();
        if (runnable != null) {
            runnable.run();
        }
    }

    public static Dialog createPopupSelectDialog(Activity activity, int i, Runnable runnable) {
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        int[] iArr = new int[1];
        if (i == 1) {
            iArr[0] = notificationsSettings.getInt("popupAll", 0);
        } else if (i == 0) {
            iArr[0] = notificationsSettings.getInt("popupGroup", 0);
        } else {
            iArr[0] = notificationsSettings.getInt("popupChannel", 0);
        }
        String[] strArr = {LocaleController.getString("NoPopup", 2131626903), LocaleController.getString("OnlyWhenScreenOn", 2131627141), LocaleController.getString("OnlyWhenScreenOff", 2131627140), LocaleController.getString("AlwaysShowPopup", 2131624345)};
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        int i2 = 0;
        while (i2 < 4) {
            RadioColorCell radioColorCell = new RadioColorCell(activity);
            radioColorCell.setTag(Integer.valueOf(i2));
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
            radioColorCell.setTextAndValue(strArr[i2], iArr[0] == i2);
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda57(iArr, i, builder, runnable));
            i2++;
        }
        builder.setTitle(LocaleController.getString("PopupNotification", 2131627644));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", 2131624832), null);
        return builder.create();
    }

    public static /* synthetic */ void lambda$createPopupSelectDialog$105(int[] iArr, int i, AlertDialog.Builder builder, Runnable runnable, View view) {
        iArr[0] = ((Integer) view.getTag()).intValue();
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
        if (i == 1) {
            edit.putInt("popupAll", iArr[0]);
        } else if (i == 0) {
            edit.putInt("popupGroup", iArr[0]);
        } else {
            edit.putInt("popupChannel", iArr[0]);
        }
        edit.commit();
        builder.getDismissRunnable().run();
        if (runnable != null) {
            runnable.run();
        }
    }

    public static Dialog createSingleChoiceDialog(Activity activity, String[] strArr, String str, int i, DialogInterface.OnClickListener onClickListener) {
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        int i2 = 0;
        while (i2 < strArr.length) {
            RadioColorCell radioColorCell = new RadioColorCell(activity);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag(Integer.valueOf(i2));
            radioColorCell.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
            radioColorCell.setTextAndValue(strArr[i2], i == i2);
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda52(builder, onClickListener));
            i2++;
        }
        builder.setTitle(str);
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", 2131624832), null);
        return builder.create();
    }

    public static /* synthetic */ void lambda$createSingleChoiceDialog$106(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener, View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        builder.getDismissRunnable().run();
        onClickListener.onClick(null, intValue);
    }

    public static AlertDialog.Builder createTTLAlert(Context context, TLRPC$EncryptedChat tLRPC$EncryptedChat, Theme.ResourcesProvider resourcesProvider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, resourcesProvider);
        builder.setTitle(LocaleController.getString("MessageLifetime", 2131626676));
        NumberPicker numberPicker = new NumberPicker(context);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(20);
        int i = tLRPC$EncryptedChat.ttl;
        if (i > 0 && i < 16) {
            numberPicker.setValue(i);
        } else if (i == 30) {
            numberPicker.setValue(16);
        } else if (i == 60) {
            numberPicker.setValue(17);
        } else if (i == 3600) {
            numberPicker.setValue(18);
        } else if (i == 86400) {
            numberPicker.setValue(19);
        } else if (i == 604800) {
            numberPicker.setValue(20);
        } else if (i == 0) {
            numberPicker.setValue(0);
        }
        numberPicker.setFormatter(AlertsCreator$$ExternalSyntheticLambda116.INSTANCE);
        builder.setView(numberPicker);
        builder.setNegativeButton(LocaleController.getString("Done", 2131625541), new AlertsCreator$$ExternalSyntheticLambda21(tLRPC$EncryptedChat, numberPicker));
        return builder;
    }

    public static /* synthetic */ String lambda$createTTLAlert$107(int i) {
        if (i == 0) {
            return LocaleController.getString("ShortMessageLifetimeForever", 2131628391);
        }
        if (i >= 1 && i < 16) {
            return LocaleController.formatTTLString(i);
        }
        if (i == 16) {
            return LocaleController.formatTTLString(30);
        }
        if (i == 17) {
            return LocaleController.formatTTLString(60);
        }
        if (i == 18) {
            return LocaleController.formatTTLString(3600);
        }
        if (i == 19) {
            return LocaleController.formatTTLString(86400);
        }
        return i == 20 ? LocaleController.formatTTLString(604800) : "";
    }

    public static /* synthetic */ void lambda$createTTLAlert$108(TLRPC$EncryptedChat tLRPC$EncryptedChat, NumberPicker numberPicker, DialogInterface dialogInterface, int i) {
        int i2 = tLRPC$EncryptedChat.ttl;
        int value = numberPicker.getValue();
        if (value >= 0 && value < 16) {
            tLRPC$EncryptedChat.ttl = value;
        } else if (value == 16) {
            tLRPC$EncryptedChat.ttl = 30;
        } else if (value == 17) {
            tLRPC$EncryptedChat.ttl = 60;
        } else if (value == 18) {
            tLRPC$EncryptedChat.ttl = 3600;
        } else if (value == 19) {
            tLRPC$EncryptedChat.ttl = 86400;
        } else if (value == 20) {
            tLRPC$EncryptedChat.ttl = 604800;
        }
        if (i2 != tLRPC$EncryptedChat.ttl) {
            SecretChatHelper.getInstance(UserConfig.selectedAccount).sendTTLMessage(tLRPC$EncryptedChat, null);
            MessagesStorage.getInstance(UserConfig.selectedAccount).updateEncryptedChatTTL(tLRPC$EncryptedChat);
        }
    }

    public static AlertDialog createAccountSelectDialog(Activity activity, AccountSelectDelegate accountSelectDelegate) {
        if (UserConfig.getActivatedAccountsCount() < 2) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        Runnable dismissRunnable = builder.getDismissRunnable();
        AlertDialog[] alertDialogArr = new AlertDialog[1];
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        for (int i = 0; i < 4; i++) {
            if (UserConfig.getInstance(i).getCurrentUser() != null) {
                AccountSelectCell accountSelectCell = new AccountSelectCell(activity, false);
                accountSelectCell.setAccount(i, false);
                accountSelectCell.setPadding(AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(14.0f), 0);
                accountSelectCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                linearLayout.addView(accountSelectCell, LayoutHelper.createLinear(-1, 50));
                accountSelectCell.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda64(alertDialogArr, dismissRunnable, accountSelectDelegate));
            }
        }
        builder.setTitle(LocaleController.getString("SelectAccount", 2131628221));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", 2131624832), null);
        AlertDialog create = builder.create();
        alertDialogArr[0] = create;
        return create;
    }

    public static /* synthetic */ void lambda$createAccountSelectDialog$109(AlertDialog[] alertDialogArr, Runnable runnable, AccountSelectDelegate accountSelectDelegate, View view) {
        if (alertDialogArr[0] != null) {
            alertDialogArr[0].setOnDismissListener(null);
        }
        runnable.run();
        accountSelectDelegate.didSelectAccount(((AccountSelectCell) view).getAccountNumber());
    }

    /* JADX WARN: Removed duplicated region for block: B:181:0x0367  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0374  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0379  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0061  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x0640  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x064e  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x0716  */
    /* JADX WARN: Removed duplicated region for block: B:337:0x071e  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x074e  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:373:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0193  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void createDeleteMessagesAlert(BaseFragment baseFragment, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$ChatFull tLRPC$ChatFull, long j, MessageObject messageObject, SparseArray<MessageObject>[] sparseArrayArr, MessageObject.GroupedMessages groupedMessages, boolean z, int i, Runnable runnable, Runnable runnable2, Theme.ResourcesProvider resourcesProvider) {
        Activity parentActivity;
        int i2;
        long j2;
        boolean z2;
        boolean[] zArr;
        boolean[] zArr2;
        int i3;
        int i4;
        int i5;
        TLRPC$Chat tLRPC$Chat2;
        boolean[] zArr3;
        boolean z3;
        TLRPC$User tLRPC$User2;
        boolean[] zArr4;
        int i6;
        int i7;
        boolean z4;
        AlertDialog.Builder builder;
        int i8;
        int i9;
        TextView textView;
        boolean[] zArr5;
        int i10;
        boolean[] zArr6;
        boolean z5;
        boolean[] zArr7;
        String str;
        TLRPC$Chat tLRPC$Chat3;
        int i11;
        TLRPC$User tLRPC$User3;
        int i12;
        boolean[] zArr8;
        TLRPC$User tLRPC$User4;
        boolean z6;
        String str2;
        TLRPC$Chat tLRPC$Chat4;
        boolean z7;
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$Chat chat;
        int size;
        int i13 = i;
        if (baseFragment != null) {
            if ((tLRPC$User == null && tLRPC$Chat == null && tLRPC$EncryptedChat == null) || (parentActivity = baseFragment.getParentActivity()) == null) {
                return;
            }
            int currentAccount = baseFragment.getCurrentAccount();
            AlertDialog.Builder builder2 = new AlertDialog.Builder(parentActivity, resourcesProvider);
            builder2.setDimAlpha(runnable2 != null ? 0.5f : 0.6f);
            if (groupedMessages != null) {
                size = groupedMessages.messages.size();
            } else if (messageObject == null) {
                size = sparseArrayArr[0].size() + sparseArrayArr[1].size();
            } else {
                i2 = 1;
                if (tLRPC$EncryptedChat == null) {
                    j2 = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
                } else if (tLRPC$User != null) {
                    j2 = tLRPC$User.id;
                } else {
                    j2 = -tLRPC$Chat.id;
                }
                long j3 = j2;
                int currentTime = ConnectionsManager.getInstance(currentAccount).getCurrentTime();
                if (messageObject == null) {
                    z2 = !messageObject.isDice() || Math.abs(currentTime - messageObject.messageOwner.date) > 86400;
                } else {
                    int i14 = 0;
                    boolean z8 = false;
                    for (int i15 = 2; i14 < i15; i15 = 2) {
                        for (int i16 = 0; i16 < sparseArrayArr[i14].size(); i16++) {
                            MessageObject valueAt = sparseArrayArr[i14].valueAt(i16);
                            if (valueAt.isDice() && Math.abs(currentTime - valueAt.messageOwner.date) <= 86400) {
                            }
                            z8 = true;
                        }
                        i14++;
                    }
                    z2 = z8;
                }
                zArr = new boolean[3];
                zArr2 = new boolean[1];
                boolean z9 = tLRPC$User == null && MessagesController.getInstance(currentAccount).canRevokePmInbox;
                if (tLRPC$User == null) {
                    i3 = MessagesController.getInstance(currentAccount).revokeTimePmLimit;
                } else {
                    i3 = MessagesController.getInstance(currentAccount).revokeTimeLimit;
                }
                boolean z10 = tLRPC$EncryptedChat != null && tLRPC$User != null && z9 && i3 == Integer.MAX_VALUE;
                String str3 = "DeleteMessagesOption";
                i4 = i2;
                if (tLRPC$Chat == null && tLRPC$Chat.megagroup && !z) {
                    boolean canBlockUsers = ChatObject.canBlockUsers(tLRPC$Chat);
                    if (messageObject != null) {
                        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
                        TLRPC$MessageAction tLRPC$MessageAction2 = tLRPC$Message.action;
                        zArr7 = zArr2;
                        if (tLRPC$MessageAction2 == null || (tLRPC$MessageAction2 instanceof TLRPC$TL_messageActionEmpty) || (tLRPC$MessageAction2 instanceof TLRPC$TL_messageActionChatDeleteUser) || (tLRPC$MessageAction2 instanceof TLRPC$TL_messageActionChatJoinedByLink) || (tLRPC$MessageAction2 instanceof TLRPC$TL_messageActionChatAddUser)) {
                            TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
                            if (tLRPC$Peer.user_id != 0) {
                                tLRPC$User3 = MessagesController.getInstance(currentAccount).getUser(Long.valueOf(messageObject.messageOwner.from_id.user_id));
                                tLRPC$Chat3 = null;
                                i11 = (!messageObject.isSendError() || messageObject.getDialogId() != j || ((tLRPC$MessageAction = messageObject.messageOwner.action) != null && !(tLRPC$MessageAction instanceof TLRPC$TL_messageActionEmpty)) || !messageObject.isOut() || currentTime - messageObject.messageOwner.date > i3) ? 0 : 1;
                                str = str3;
                            } else {
                                if (tLRPC$Peer.channel_id != 0) {
                                    chat = MessagesController.getInstance(currentAccount).getChat(Long.valueOf(messageObject.messageOwner.from_id.channel_id));
                                } else if (tLRPC$Peer.chat_id != 0) {
                                    chat = MessagesController.getInstance(currentAccount).getChat(Long.valueOf(messageObject.messageOwner.from_id.chat_id));
                                }
                                tLRPC$Chat3 = chat;
                                tLRPC$User3 = null;
                                if (!messageObject.isSendError()) {
                                }
                                str = str3;
                            }
                        }
                        tLRPC$User3 = null;
                        tLRPC$Chat3 = null;
                        if (!messageObject.isSendError()) {
                        }
                        str = str3;
                    } else {
                        zArr7 = zArr2;
                        long j4 = -1;
                        for (int i17 = 1; i17 >= 0; i17--) {
                            for (int i18 = 0; i18 < sparseArrayArr[i17].size(); i18++) {
                                MessageObject valueAt2 = sparseArrayArr[i17].valueAt(i18);
                                if (j4 == -1) {
                                    j4 = valueAt2.getFromChatId();
                                }
                                if (j4 < 0 || j4 != valueAt2.getSenderId()) {
                                    j4 = -2;
                                    break;
                                }
                            }
                            if (j4 == -2) {
                                break;
                            }
                        }
                        int i19 = 0;
                        for (int i20 = 1; i20 >= 0; i20--) {
                            int i21 = 0;
                            while (i21 < sparseArrayArr[i20].size()) {
                                MessageObject valueAt3 = sparseArrayArr[i20].valueAt(i21);
                                String str4 = str3;
                                if (i20 == 1 && valueAt3.isOut()) {
                                    TLRPC$Message tLRPC$Message2 = valueAt3.messageOwner;
                                    if (tLRPC$Message2.action == null && currentTime - tLRPC$Message2.date <= i3) {
                                        i19++;
                                    }
                                }
                                i21++;
                                str3 = str4;
                            }
                        }
                        str = str3;
                        if (j4 != -1) {
                            tLRPC$Chat3 = null;
                            i11 = i19;
                            tLRPC$User3 = MessagesController.getInstance(currentAccount).getUser(Long.valueOf(j4));
                        } else {
                            i11 = i19;
                            tLRPC$User3 = null;
                            tLRPC$Chat3 = null;
                        }
                    }
                    if ((tLRPC$User3 == null || tLRPC$User3.id == UserConfig.getInstance(currentAccount).getClientUserId()) && (tLRPC$Chat3 == null || ChatObject.hasAdminRights(tLRPC$Chat3))) {
                        TLRPC$User tLRPC$User5 = tLRPC$User3;
                        tLRPC$Chat2 = tLRPC$Chat3;
                        i12 = currentAccount;
                        if (i11 <= 0 || !z2) {
                            zArr8 = zArr7;
                            z6 = false;
                            tLRPC$User4 = null;
                        } else {
                            FrameLayout frameLayout = new FrameLayout(parentActivity);
                            CheckBoxCell checkBoxCell = new CheckBoxCell(parentActivity, 1, resourcesProvider);
                            checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                            checkBoxCell.setText(LocaleController.getString(str, 2131625435), "", false, false);
                            checkBoxCell.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                            frameLayout.addView(checkBoxCell, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                            zArr8 = zArr7;
                            checkBoxCell.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda67(zArr8));
                            builder2.setView(frameLayout);
                            builder2.setCustomViewOffset(9);
                            tLRPC$User4 = tLRPC$User5;
                            z6 = true;
                        }
                    } else if (i13 == 1 && !tLRPC$Chat.creator && tLRPC$User3 != null) {
                        AlertDialog[] alertDialogArr = {new AlertDialog(parentActivity, 3)};
                        TLRPC$TL_channels_getParticipant tLRPC$TL_channels_getParticipant = new TLRPC$TL_channels_getParticipant();
                        tLRPC$TL_channels_getParticipant.channel = MessagesController.getInputChannel(tLRPC$Chat);
                        tLRPC$TL_channels_getParticipant.participant = MessagesController.getInputPeer(tLRPC$User3);
                        AndroidUtilities.runOnUIThread(new AlertsCreator$$ExternalSyntheticLambda90(alertDialogArr, currentAccount, ConnectionsManager.getInstance(currentAccount).sendRequest(tLRPC$TL_channels_getParticipant, new AlertsCreator$$ExternalSyntheticLambda95(alertDialogArr, baseFragment, tLRPC$User, tLRPC$Chat, tLRPC$EncryptedChat, tLRPC$ChatFull, j, messageObject, sparseArrayArr, groupedMessages, z, runnable, runnable2, resourcesProvider)), baseFragment), 1000L);
                        return;
                    } else {
                        i12 = currentAccount;
                        FrameLayout frameLayout2 = new FrameLayout(parentActivity);
                        String formatName = tLRPC$User3 != null ? ContactsController.formatName(tLRPC$User3.first_name, tLRPC$User3.last_name) : tLRPC$Chat3.title;
                        TLRPC$User tLRPC$User6 = tLRPC$User3;
                        int i22 = 0;
                        int i23 = 0;
                        for (int i24 = 3; i22 < i24; i24 = 3) {
                            if ((i13 == 2 || !canBlockUsers) && i22 == 0) {
                                z7 = canBlockUsers;
                                tLRPC$Chat4 = tLRPC$Chat3;
                                str2 = formatName;
                            } else {
                                CheckBoxCell checkBoxCell2 = new CheckBoxCell(parentActivity, 1, resourcesProvider);
                                checkBoxCell2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                                checkBoxCell2.setTag(Integer.valueOf(i22));
                                if (i22 == 0) {
                                    z7 = canBlockUsers;
                                    checkBoxCell2.setText(LocaleController.getString("DeleteBanUser", 2131625407), "", false, false);
                                    tLRPC$Chat4 = tLRPC$Chat3;
                                } else {
                                    z7 = canBlockUsers;
                                    if (i22 == 1) {
                                        tLRPC$Chat4 = tLRPC$Chat3;
                                        checkBoxCell2.setText(LocaleController.getString("DeleteReportSpam", 2131625445), "", false, false);
                                    } else {
                                        tLRPC$Chat4 = tLRPC$Chat3;
                                        str2 = formatName;
                                        checkBoxCell2.setText(LocaleController.formatString("DeleteAllFrom", 2131625392, formatName), "", false, false);
                                        checkBoxCell2.setPadding(!LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, !LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                                        frameLayout2.addView(checkBoxCell2, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, i23 * 48, 0.0f, 0.0f));
                                        checkBoxCell2.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda69(zArr));
                                        i23++;
                                    }
                                }
                                str2 = formatName;
                                checkBoxCell2.setPadding(!LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, !LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                                frameLayout2.addView(checkBoxCell2, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, i23 * 48, 0.0f, 0.0f));
                                checkBoxCell2.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda69(zArr));
                                i23++;
                            }
                            i22++;
                            formatName = str2;
                            i13 = i;
                            canBlockUsers = z7;
                            tLRPC$Chat3 = tLRPC$Chat4;
                        }
                        tLRPC$Chat2 = tLRPC$Chat3;
                        builder2.setView(frameLayout2);
                        tLRPC$User4 = tLRPC$User6;
                        zArr8 = zArr7;
                        z6 = false;
                    }
                    z3 = z6;
                    builder = builder2;
                    tLRPC$User2 = tLRPC$User4;
                    i7 = i11;
                    zArr4 = zArr8;
                    i5 = i12;
                    zArr3 = zArr;
                    i6 = i4;
                    z4 = false;
                } else {
                    zArr5 = zArr2;
                    i10 = currentAccount;
                    if (!z || ChatObject.isChannel(tLRPC$Chat) || tLRPC$EncryptedChat != null) {
                        builder = builder2;
                        zArr4 = zArr5;
                        i5 = i10;
                        zArr3 = zArr;
                        i6 = i4;
                        z4 = false;
                        i7 = 0;
                    } else {
                        if ((tLRPC$User == null || tLRPC$User.id == UserConfig.getInstance(i10).getClientUserId() || (tLRPC$User.bot && !tLRPC$User.support)) && tLRPC$Chat == null) {
                            zArr6 = zArr5;
                            i5 = i10;
                            zArr3 = zArr;
                            i7 = 0;
                            z5 = false;
                        } else if (messageObject != null) {
                            if (!messageObject.isSendError()) {
                                TLRPC$MessageAction tLRPC$MessageAction3 = messageObject.messageOwner.action;
                                zArr3 = zArr;
                                if ((tLRPC$MessageAction3 == null || (tLRPC$MessageAction3 instanceof TLRPC$TL_messageActionEmpty) || (tLRPC$MessageAction3 instanceof TLRPC$TL_messageActionPhoneCall) || (tLRPC$MessageAction3 instanceof TLRPC$TL_messageActionPinMessage) || (tLRPC$MessageAction3 instanceof TLRPC$TL_messageActionGeoProximityReached) || (tLRPC$MessageAction3 instanceof TLRPC$TL_messageActionSetChatTheme)) && ((messageObject.isOut() || z9 || ChatObject.hasAdminRights(tLRPC$Chat)) && currentTime - messageObject.messageOwner.date <= i3)) {
                                    i7 = 1;
                                    z5 = !messageObject.isOut();
                                    zArr6 = zArr5;
                                    i5 = i10;
                                }
                            } else {
                                zArr3 = zArr;
                            }
                            i7 = 0;
                            z5 = !messageObject.isOut();
                            zArr6 = zArr5;
                            i5 = i10;
                        } else {
                            zArr3 = zArr;
                            boolean z11 = false;
                            int i25 = 0;
                            for (int i26 = 1; i26 >= 0; i26--) {
                                int i27 = 0;
                                while (i27 < sparseArrayArr[i26].size()) {
                                    MessageObject valueAt4 = sparseArrayArr[i26].valueAt(i27);
                                    int i28 = i10;
                                    TLRPC$MessageAction tLRPC$MessageAction4 = valueAt4.messageOwner.action;
                                    boolean[] zArr9 = zArr5;
                                    if ((tLRPC$MessageAction4 == null || (tLRPC$MessageAction4 instanceof TLRPC$TL_messageActionEmpty) || (tLRPC$MessageAction4 instanceof TLRPC$TL_messageActionPhoneCall) || (tLRPC$MessageAction4 instanceof TLRPC$TL_messageActionPinMessage) || (tLRPC$MessageAction4 instanceof TLRPC$TL_messageActionGeoProximityReached)) && ((valueAt4.isOut() || z9 || (tLRPC$Chat != null && ChatObject.canBlockUsers(tLRPC$Chat))) && currentTime - valueAt4.messageOwner.date <= i3)) {
                                        i25++;
                                        if (!z11 && !valueAt4.isOut()) {
                                            z11 = true;
                                        }
                                    }
                                    i27++;
                                    zArr5 = zArr9;
                                    i10 = i28;
                                }
                            }
                            zArr6 = zArr5;
                            i5 = i10;
                            z5 = z11;
                            i7 = i25;
                        }
                        if (i7 <= 0 || !z2 || (tLRPC$User != null && UserObject.isDeleted(tLRPC$User))) {
                            builder = builder2;
                            i6 = i4;
                            zArr4 = zArr6;
                            z4 = z5;
                        } else {
                            FrameLayout frameLayout3 = new FrameLayout(parentActivity);
                            CheckBoxCell checkBoxCell3 = new CheckBoxCell(parentActivity, 1, resourcesProvider);
                            checkBoxCell3.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                            if (z10) {
                                checkBoxCell3.setText(LocaleController.formatString("DeleteMessagesOptionAlso", 2131625436, UserObject.getFirstName(tLRPC$User)), "", false, false);
                                i6 = i4;
                            } else {
                                i6 = i4;
                                if (tLRPC$Chat != null && (z5 || i7 == i6)) {
                                    checkBoxCell3.setText(LocaleController.getString("DeleteForAll", 2131625422), "", false, false);
                                } else {
                                    checkBoxCell3.setText(LocaleController.getString(str3, 2131625435), "", false, false);
                                }
                            }
                            checkBoxCell3.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                            frameLayout3.addView(checkBoxCell3, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                            zArr4 = zArr6;
                            checkBoxCell3.setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda71(zArr4));
                            builder = builder2;
                            builder.setView(frameLayout3);
                            builder.setCustomViewOffset(9);
                            z4 = z5;
                            tLRPC$User2 = null;
                            z3 = true;
                            tLRPC$Chat2 = null;
                        }
                    }
                    tLRPC$User2 = null;
                    z3 = false;
                    tLRPC$Chat2 = null;
                }
                i8 = i6;
                int i29 = i7;
                boolean z12 = z4;
                builder.setPositiveButton(LocaleController.getString("Delete", 2131625384), new AlertsCreator$$ExternalSyntheticLambda17(messageObject, groupedMessages, tLRPC$EncryptedChat, i5, j3, zArr4, z, sparseArrayArr, tLRPC$User2, tLRPC$Chat2, zArr3, tLRPC$Chat, tLRPC$ChatFull, runnable));
                if (i8 != 1) {
                    builder.setTitle(LocaleController.getString("DeleteSingleMessagesTitle", 2131625447));
                    i9 = 0;
                } else {
                    i9 = 0;
                    builder.setTitle(LocaleController.formatString("DeleteMessagesTitle", 2131625441, LocaleController.formatPluralString("messages", i8, new Object[0])));
                }
                if (tLRPC$Chat != null || !z12) {
                    if (z3 || z10 || i29 == i8) {
                        if (tLRPC$Chat != null || !tLRPC$Chat.megagroup || z) {
                            if (i8 != 1) {
                                builder.setMessage(LocaleController.getString("AreYouSureDeleteSingleMessage", 2131624459));
                            } else {
                                builder.setMessage(LocaleController.getString("AreYouSureDeleteFewMessages", 2131624451));
                            }
                        } else if (i8 == 1) {
                            builder.setMessage(LocaleController.getString("AreYouSureDeleteSingleMessageMega", 2131624460));
                        } else {
                            builder.setMessage(LocaleController.getString("AreYouSureDeleteFewMessagesMega", 2131624452));
                        }
                    } else if (tLRPC$Chat != null) {
                        Object[] objArr = new Object[1];
                        objArr[i9] = LocaleController.formatPluralString("messages", i29, new Object[i9]);
                        builder.setMessage(LocaleController.formatString("DeleteMessagesTextGroup", 2131625439, objArr));
                    } else {
                        Object[] objArr2 = new Object[2];
                        objArr2[i9] = LocaleController.formatPluralString("messages", i29, new Object[i9]);
                        objArr2[1] = UserObject.getFirstName(tLRPC$User);
                        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("DeleteMessagesText", 2131625438, objArr2)));
                    }
                } else if (z3 && i29 != i8) {
                    Object[] objArr3 = new Object[1];
                    objArr3[i9] = LocaleController.formatPluralString("messages", i29, new Object[i9]);
                    builder.setMessage(LocaleController.formatString("DeleteMessagesTextGroupPart", 2131625440, objArr3));
                } else if (i8 == 1) {
                    builder.setMessage(LocaleController.getString("AreYouSureDeleteSingleMessage", 2131624459));
                } else {
                    builder.setMessage(LocaleController.getString("AreYouSureDeleteFewMessages", 2131624451));
                }
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
                builder.setOnPreDismissListener(new AlertsCreator$$ExternalSyntheticLambda43(runnable2));
                AlertDialog create = builder.create();
                baseFragment.showDialog(create);
                textView = (TextView) create.getButton(-1);
                if (textView != null) {
                    return;
                }
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
                return;
            }
            i2 = size;
            if (tLRPC$EncryptedChat == null) {
            }
            long j32 = j2;
            int currentTime2 = ConnectionsManager.getInstance(currentAccount).getCurrentTime();
            if (messageObject == null) {
            }
            zArr = new boolean[3];
            zArr2 = new boolean[1];
            if (tLRPC$User == null) {
            }
            if (tLRPC$User == null) {
            }
            if (tLRPC$EncryptedChat != null) {
            }
            String str32 = "DeleteMessagesOption";
            i4 = i2;
            if (tLRPC$Chat == null) {
            }
            zArr5 = zArr2;
            i10 = currentAccount;
            if (!z) {
            }
            builder = builder2;
            zArr4 = zArr5;
            i5 = i10;
            zArr3 = zArr;
            i6 = i4;
            z4 = false;
            i7 = 0;
            tLRPC$User2 = null;
            z3 = false;
            tLRPC$Chat2 = null;
            i8 = i6;
            int i292 = i7;
            boolean z122 = z4;
            builder.setPositiveButton(LocaleController.getString("Delete", 2131625384), new AlertsCreator$$ExternalSyntheticLambda17(messageObject, groupedMessages, tLRPC$EncryptedChat, i5, j32, zArr4, z, sparseArrayArr, tLRPC$User2, tLRPC$Chat2, zArr3, tLRPC$Chat, tLRPC$ChatFull, runnable));
            if (i8 != 1) {
            }
            if (tLRPC$Chat != null) {
            }
            if (z3) {
            }
            if (tLRPC$Chat != null) {
            }
            if (i8 != 1) {
            }
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
            builder.setOnPreDismissListener(new AlertsCreator$$ExternalSyntheticLambda43(runnable2));
            AlertDialog create2 = builder.create();
            baseFragment.showDialog(create2);
            textView = (TextView) create2.getButton(-1);
            if (textView != null) {
            }
        }
    }

    public static /* synthetic */ void lambda$createDeleteMessagesAlert$111(AlertDialog[] alertDialogArr, BaseFragment baseFragment, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$ChatFull tLRPC$ChatFull, long j, MessageObject messageObject, SparseArray[] sparseArrayArr, MessageObject.GroupedMessages groupedMessages, boolean z, Runnable runnable, Runnable runnable2, Theme.ResourcesProvider resourcesProvider, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new AlertsCreator$$ExternalSyntheticLambda91(alertDialogArr, tLObject, tLRPC$TL_error, baseFragment, tLRPC$User, tLRPC$Chat, tLRPC$EncryptedChat, tLRPC$ChatFull, j, messageObject, sparseArrayArr, groupedMessages, z, runnable, runnable2, resourcesProvider));
    }

    public static /* synthetic */ void lambda$createDeleteMessagesAlert$110(AlertDialog[] alertDialogArr, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error, BaseFragment baseFragment, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$ChatFull tLRPC$ChatFull, long j, MessageObject messageObject, SparseArray[] sparseArrayArr, MessageObject.GroupedMessages groupedMessages, boolean z, Runnable runnable, Runnable runnable2, Theme.ResourcesProvider resourcesProvider) {
        int i;
        int i2 = 0;
        try {
            alertDialogArr[0].dismiss();
        } catch (Throwable unused) {
        }
        alertDialogArr[0] = null;
        if (tLObject != null) {
            TLRPC$ChannelParticipant tLRPC$ChannelParticipant = ((TLRPC$TL_channels_channelParticipant) tLObject).participant;
            if ((tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantAdmin) || (tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator)) {
                i2 = 2;
            }
            i = i2;
        } else {
            i = (tLRPC$TL_error == null || !"USER_NOT_PARTICIPANT".equals(tLRPC$TL_error.text)) ? 2 : 0;
        }
        createDeleteMessagesAlert(baseFragment, tLRPC$User, tLRPC$Chat, tLRPC$EncryptedChat, tLRPC$ChatFull, j, messageObject, sparseArrayArr, groupedMessages, z, i, runnable, runnable2, resourcesProvider);
    }

    public static /* synthetic */ void lambda$createDeleteMessagesAlert$113(AlertDialog[] alertDialogArr, int i, int i2, BaseFragment baseFragment) {
        if (alertDialogArr[0] == null) {
            return;
        }
        alertDialogArr[0].setOnCancelListener(new AlertsCreator$$ExternalSyntheticLambda0(i, i2));
        baseFragment.showDialog(alertDialogArr[0]);
    }

    public static /* synthetic */ void lambda$createDeleteMessagesAlert$112(int i, int i2, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(i2, true);
    }

    public static /* synthetic */ void lambda$createDeleteMessagesAlert$114(boolean[] zArr, View view) {
        if (!view.isEnabled()) {
            return;
        }
        CheckBoxCell checkBoxCell = (CheckBoxCell) view;
        Integer num = (Integer) checkBoxCell.getTag();
        zArr[num.intValue()] = !zArr[num.intValue()];
        checkBoxCell.setChecked(zArr[num.intValue()], true);
    }

    public static /* synthetic */ void lambda$createDeleteMessagesAlert$115(boolean[] zArr, View view) {
        zArr[0] = !zArr[0];
        ((CheckBoxCell) view).setChecked(zArr[0], true);
    }

    public static /* synthetic */ void lambda$createDeleteMessagesAlert$116(boolean[] zArr, View view) {
        zArr[0] = !zArr[0];
        ((CheckBoxCell) view).setChecked(zArr[0], true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void lambda$createDeleteMessagesAlert$118(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages, TLRPC$EncryptedChat tLRPC$EncryptedChat, int i, long j, boolean[] zArr, boolean z, SparseArray[] sparseArrayArr, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, boolean[] zArr2, TLRPC$Chat tLRPC$Chat2, TLRPC$ChatFull tLRPC$ChatFull, Runnable runnable, DialogInterface dialogInterface, int i2) {
        ArrayList<Integer> arrayList;
        int i3;
        ArrayList arrayList2;
        ArrayList<Long> arrayList3;
        int i4 = 10;
        ArrayList<Long> arrayList4 = null;
        int i5 = 0;
        if (messageObject != null) {
            ArrayList<Integer> arrayList5 = new ArrayList<>();
            if (groupedMessages != null) {
                for (int i6 = 0; i6 < groupedMessages.messages.size(); i6++) {
                    MessageObject messageObject2 = groupedMessages.messages.get(i6);
                    arrayList5.add(Integer.valueOf(messageObject2.getId()));
                    if (tLRPC$EncryptedChat != null && messageObject2.messageOwner.random_id != 0 && messageObject2.type != 10) {
                        if (arrayList4 == null) {
                            arrayList4 = new ArrayList<>();
                        }
                        arrayList4.add(Long.valueOf(messageObject2.messageOwner.random_id));
                    }
                }
            } else {
                arrayList5.add(Integer.valueOf(messageObject.getId()));
                if (tLRPC$EncryptedChat != null && messageObject.messageOwner.random_id != 0 && messageObject.type != 10) {
                    ArrayList<Long> arrayList6 = new ArrayList<>();
                    arrayList6.add(Long.valueOf(messageObject.messageOwner.random_id));
                    arrayList3 = arrayList6;
                    arrayList = arrayList5;
                    i3 = 0;
                    MessagesController.getInstance(i).deleteMessages(arrayList5, arrayList3, tLRPC$EncryptedChat, j, zArr[0], z);
                }
            }
            arrayList3 = arrayList4;
            arrayList = arrayList5;
            i3 = 0;
            MessagesController.getInstance(i).deleteMessages(arrayList5, arrayList3, tLRPC$EncryptedChat, j, zArr[0], z);
        } else {
            ArrayList<Integer> arrayList7 = null;
            int i7 = 1;
            while (i7 >= 0) {
                ArrayList<Integer> arrayList8 = new ArrayList<>();
                for (int i8 = 0; i8 < sparseArrayArr[i7].size(); i8++) {
                    arrayList8.add(Integer.valueOf(sparseArrayArr[i7].keyAt(i8)));
                }
                if (!arrayList8.isEmpty()) {
                    long j2 = ((MessageObject) sparseArrayArr[i7].get(arrayList8.get(i5).intValue())).messageOwner.peer_id.channel_id;
                }
                if (tLRPC$EncryptedChat != null) {
                    ArrayList arrayList9 = new ArrayList();
                    for (int i9 = 0; i9 < sparseArrayArr[i7].size(); i9++) {
                        MessageObject messageObject3 = (MessageObject) sparseArrayArr[i7].valueAt(i9);
                        long j3 = messageObject3.messageOwner.random_id;
                        if (j3 != 0 && messageObject3.type != i4) {
                            arrayList9.add(Long.valueOf(j3));
                        }
                    }
                    arrayList2 = arrayList9;
                } else {
                    arrayList2 = null;
                }
                MessagesController.getInstance(i).deleteMessages(arrayList8, arrayList2, tLRPC$EncryptedChat, j, zArr[i5], z);
                sparseArrayArr[i7].clear();
                i7--;
                arrayList7 = arrayList8;
                i5 = 0;
                i4 = 10;
            }
            i3 = 0;
            arrayList = arrayList7;
        }
        if (tLRPC$User != null || tLRPC$Chat != null) {
            if (zArr2[i3]) {
                MessagesController.getInstance(i).deleteParticipantFromChat(tLRPC$Chat2.id, tLRPC$User, tLRPC$Chat, tLRPC$ChatFull, false, false);
            }
            if (zArr2[1]) {
                TLRPC$TL_channels_reportSpam tLRPC$TL_channels_reportSpam = new TLRPC$TL_channels_reportSpam();
                tLRPC$TL_channels_reportSpam.channel = MessagesController.getInputChannel(tLRPC$Chat2);
                if (tLRPC$User != null) {
                    tLRPC$TL_channels_reportSpam.participant = MessagesController.getInputPeer(tLRPC$User);
                } else {
                    tLRPC$TL_channels_reportSpam.participant = MessagesController.getInputPeer(tLRPC$Chat);
                }
                tLRPC$TL_channels_reportSpam.id = arrayList;
                ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_channels_reportSpam, AlertsCreator$$ExternalSyntheticLambda99.INSTANCE);
            }
            if (zArr2[2]) {
                MessagesController.getInstance(i).deleteUserChannelHistory(tLRPC$Chat2, tLRPC$User, tLRPC$Chat, i3);
            }
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    public static /* synthetic */ void lambda$createDeleteMessagesAlert$119(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public static void createThemeCreateDialog(BaseFragment baseFragment, int i, Theme.ThemeInfo themeInfo, Theme.ThemeAccent themeAccent) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        Activity parentActivity = baseFragment.getParentActivity();
        EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(parentActivity);
        editTextBoldCursor.setBackground(null);
        editTextBoldCursor.setLineColors(Theme.getColor("dialogInputField"), Theme.getColor("dialogInputFieldActivated"), Theme.getColor("dialogTextRed2"));
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle(LocaleController.getString("NewTheme", 2131626846));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        builder.setPositiveButton(LocaleController.getString("Create", 2131625284), AlertsCreator$$ExternalSyntheticLambda38.INSTANCE);
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        builder.setView(linearLayout);
        TextView textView = new TextView(parentActivity);
        if (i != 0) {
            textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("EnterThemeNameEdit", 2131625691)));
        } else {
            textView.setText(LocaleController.getString("EnterThemeName", 2131625690));
        }
        textView.setTextSize(1, 16.0f);
        textView.setPadding(AndroidUtilities.dp(23.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(23.0f), AndroidUtilities.dp(6.0f));
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2));
        editTextBoldCursor.setTextSize(1, 16.0f);
        editTextBoldCursor.setTextColor(Theme.getColor("dialogTextBlack"));
        editTextBoldCursor.setMaxLines(1);
        editTextBoldCursor.setLines(1);
        editTextBoldCursor.setInputType(16385);
        editTextBoldCursor.setGravity(51);
        editTextBoldCursor.setSingleLine(true);
        editTextBoldCursor.setImeOptions(6);
        editTextBoldCursor.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        editTextBoldCursor.setCursorSize(AndroidUtilities.dp(20.0f));
        editTextBoldCursor.setCursorWidth(1.5f);
        editTextBoldCursor.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
        linearLayout.addView(editTextBoldCursor, LayoutHelper.createLinear(-1, 36, 51, 24, 6, 24, 0));
        editTextBoldCursor.setOnEditorActionListener(AlertsCreator$$ExternalSyntheticLambda84.INSTANCE);
        editTextBoldCursor.setText(generateThemeName(themeAccent));
        editTextBoldCursor.setSelection(editTextBoldCursor.length());
        AlertDialog create = builder.create();
        create.setOnShowListener(new AlertsCreator$$ExternalSyntheticLambda46(editTextBoldCursor));
        baseFragment.showDialog(create);
        editTextBoldCursor.requestFocus();
        create.getButton(-1).setOnClickListener(new AlertsCreator$$ExternalSyntheticLambda53(baseFragment, editTextBoldCursor, themeAccent, themeInfo, create));
    }

    public static /* synthetic */ void lambda$createThemeCreateDialog$123(EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface) {
        AndroidUtilities.runOnUIThread(new AlertsCreator$$ExternalSyntheticLambda87(editTextBoldCursor));
    }

    public static /* synthetic */ void lambda$createThemeCreateDialog$122(EditTextBoldCursor editTextBoldCursor) {
        editTextBoldCursor.requestFocus();
        AndroidUtilities.showKeyboard(editTextBoldCursor);
    }

    public static /* synthetic */ void lambda$createThemeCreateDialog$126(BaseFragment baseFragment, EditTextBoldCursor editTextBoldCursor, Theme.ThemeAccent themeAccent, Theme.ThemeInfo themeInfo, AlertDialog alertDialog, View view) {
        if (baseFragment.getParentActivity() == null) {
            return;
        }
        if (editTextBoldCursor.length() == 0) {
            Vibrator vibrator = (Vibrator) ApplicationLoader.applicationContext.getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200L);
            }
            AndroidUtilities.shakeView(editTextBoldCursor, 2.0f, 0);
            return;
        }
        if (baseFragment instanceof ThemePreviewActivity) {
            Theme.applyPreviousTheme();
            baseFragment.finishFragment();
        }
        if (themeAccent != null) {
            themeInfo.setCurrentAccentId(themeAccent.id);
            Theme.refreshThemeColors();
            Utilities.searchQueue.postRunnable(new AlertsCreator$$ExternalSyntheticLambda89(editTextBoldCursor, alertDialog, baseFragment));
            return;
        }
        processCreate(editTextBoldCursor, alertDialog, baseFragment);
    }

    public static /* synthetic */ void lambda$createThemeCreateDialog$125(EditTextBoldCursor editTextBoldCursor, AlertDialog alertDialog, BaseFragment baseFragment) {
        AndroidUtilities.runOnUIThread(new AlertsCreator$$ExternalSyntheticLambda88(editTextBoldCursor, alertDialog, baseFragment));
    }

    public static void processCreate(EditTextBoldCursor editTextBoldCursor, AlertDialog alertDialog, BaseFragment baseFragment) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        Theme.ThemeInfo createNewTheme = Theme.createNewTheme(editTextBoldCursor.getText().toString());
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.themeListUpdated, new Object[0]);
        new ThemeEditorView().show(baseFragment.getParentActivity(), createNewTheme);
        alertDialog.dismiss();
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        if (globalMainSettings.getBoolean("themehint", false)) {
            return;
        }
        globalMainSettings.edit().putBoolean("themehint", true).commit();
        try {
            Toast.makeText(baseFragment.getParentActivity(), LocaleController.getString("CreateNewThemeHelp", 2131625297), 1).show();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private static String generateThemeName(Theme.ThemeAccent themeAccent) {
        int i;
        List asList = Arrays.asList("Ancient", "Antique", "Autumn", "Baby", "Barely", "Baroque", "Blazing", "Blushing", "Bohemian", "Bubbly", "Burning", "Buttered", "Classic", "Clear", "Cool", "Cosmic", "Cotton", "Cozy", "Crystal", "Dark", "Daring", "Darling", "Dawn", "Dazzling", "Deep", "Deepest", "Delicate", "Delightful", "Divine", "Double", "Downtown", "Dreamy", "Dusky", "Dusty", "Electric", "Enchanted", "Endless", "Evening", "Fantastic", "Flirty", "Forever", "Frigid", "Frosty", "Frozen", "Gentle", "Heavenly", "Hyper", "Icy", "Infinite", "Innocent", "Instant", "Luscious", "Lunar", "Lustrous", "Magic", "Majestic", "Mambo", "Midnight", "Millenium", "Morning", "Mystic", "Natural", "Neon", "Night", "Opaque", "Paradise", "Perfect", "Perky", "Polished", "Powerful", "Rich", "Royal", "Sheer", "Simply", "Sizzling", "Solar", "Sparkling", "Splendid", "Spicy", "Spring", "Stellar", "Sugared", "Summer", "Sunny", "Super", "Sweet", "Tender", "Tenacious", "Tidal", "Toasted", "Totally", "Tranquil", "Tropical", "True", "Twilight", "Twinkling", "Ultimate", "Ultra", "Velvety", "Vibrant", "Vintage", "Virtual", "Warm", "Warmest", "Whipped", "Wild", "Winsome");
        List asList2 = Arrays.asList("Ambrosia", "Attack", "Avalanche", "Blast", "Bliss", "Blossom", "Blush", "Burst", "Butter", "Candy", "Carnival", "Charm", "Chiffon", "Cloud", "Comet", "Delight", "Dream", "Dust", "Fantasy", "Flame", "Flash", "Fire", "Freeze", "Frost", "Glade", "Glaze", "Gleam", "Glimmer", "Glitter", "Glow", "Grande", "Haze", "Highlight", "Ice", "Illusion", "Intrigue", "Jewel", "Jubilee", "Kiss", "Lights", "Lollypop", "Love", "Luster", "Madness", "Matte", "Mirage", "Mist", "Moon", "Muse", "Myth", "Nectar", "Nova", "Parfait", "Passion", "Pop", "Rain", "Reflection", "Rhapsody", "Romance", "Satin", "Sensation", "Silk", "Shine", "Shadow", "Shimmer", "Sky", "Spice", "Star", "Sugar", "Sunrise", "Sunset", "Sun", "Twist", "Unbound", "Velvet", "Vibrant", "Waters", "Wine", "Wink", "Wonder", "Zone");
        HashMap hashMap = new HashMap();
        hashMap.put(9306112, "Berry");
        hashMap.put(14598550, "Brandy");
        hashMap.put(8391495, "Cherry");
        hashMap.put(16744272, "Coral");
        hashMap.put(14372985, "Cranberry");
        hashMap.put(14423100, "Crimson");
        hashMap.put(14725375, "Mauve");
        hashMap.put(16761035, "Pink");
        hashMap.put(16711680, "Red");
        hashMap.put(16711807, "Rose");
        hashMap.put(8406555, "Russet");
        hashMap.put(16720896, "Scarlet");
        hashMap.put(15856113, "Seashell");
        hashMap.put(16724889, "Strawberry");
        hashMap.put(16760576, "Amber");
        hashMap.put(15438707, "Apricot");
        hashMap.put(16508850, "Banana");
        hashMap.put(10601738, "Citrus");
        hashMap.put(11560192, "Ginger");
        hashMap.put(16766720, "Gold");
        hashMap.put(16640272, "Lemon");
        hashMap.put(16753920, "Orange");
        hashMap.put(16770484, "Peach");
        hashMap.put(16739155, "Persimmon");
        hashMap.put(14996514, "Sunflower");
        hashMap.put(15893760, "Tangerine");
        hashMap.put(16763004, "Topaz");
        hashMap.put(16776960, "Yellow");
        hashMap.put(3688720, "Clover");
        hashMap.put(8628829, "Cucumber");
        hashMap.put(5294200, "Emerald");
        hashMap.put(11907932, "Olive");
        hashMap.put(65280, "Green");
        hashMap.put(43115, "Jade");
        hashMap.put(2730887, "Jungle");
        hashMap.put(12582656, "Lime");
        hashMap.put(776785, "Malachite");
        hashMap.put(10026904, "Mint");
        hashMap.put(11394989, "Moss");
        hashMap.put(3234721, "Azure");
        hashMap.put(255, "Blue");
        hashMap.put(18347, "Cobalt");
        hashMap.put(5204422, "Indigo");
        hashMap.put(96647, "Lagoon");
        hashMap.put(7461346, "Aquamarine");
        hashMap.put(1182351, "Ultramarine");
        hashMap.put(128, "Navy");
        hashMap.put(3101086, "Sapphire");
        hashMap.put(7788522, "Sky");
        hashMap.put(32896, "Teal");
        hashMap.put(4251856, "Turquoise");
        hashMap.put(10053324, "Amethyst");
        hashMap.put(5046581, "Blackberry");
        hashMap.put(6373457, "Eggplant");
        hashMap.put(13148872, "Lilac");
        hashMap.put(11894492, "Lavender");
        hashMap.put(13421823, "Periwinkle");
        hashMap.put(8663417, "Plum");
        hashMap.put(6684825, "Purple");
        hashMap.put(14204888, "Thistle");
        hashMap.put(14315734, "Orchid");
        hashMap.put(2361920, "Violet");
        hashMap.put(4137225, "Bronze");
        hashMap.put(3604994, "Chocolate");
        hashMap.put(8077056, "Cinnamon");
        hashMap.put(3153694, "Cocoa");
        hashMap.put(7365973, "Coffee");
        hashMap.put(7956873, "Rum");
        hashMap.put(5113350, "Mahogany");
        hashMap.put(7875865, "Mocha");
        hashMap.put(12759680, "Sand");
        hashMap.put(8924439, "Sienna");
        hashMap.put(7864585, "Maple");
        hashMap.put(15787660, "Khaki");
        hashMap.put(12088115, "Copper");
        hashMap.put(12144200, "Chestnut");
        hashMap.put(15653316, "Almond");
        hashMap.put(16776656, "Cream");
        hashMap.put(12186367, "Diamond");
        hashMap.put(11109127, "Honey");
        hashMap.put(16777200, "Ivory");
        hashMap.put(15392968, "Pearl");
        hashMap.put(15725299, "Porcelain");
        hashMap.put(13745832, "Vanilla");
        hashMap.put(16777215, "White");
        hashMap.put(8421504, "Gray");
        hashMap.put(0, "Black");
        hashMap.put(15266260, "Chrome");
        hashMap.put(3556687, "Charcoal");
        hashMap.put(789277, "Ebony");
        hashMap.put(12632256, "Silver");
        hashMap.put(16119285, "Smoke");
        hashMap.put(2499381, "Steel");
        hashMap.put(5220413, "Apple");
        hashMap.put(8434628, "Glacier");
        hashMap.put(16693933, "Melon");
        hashMap.put(12929932, "Mulberry");
        hashMap.put(11126466, "Opal");
        hashMap.put(5547512, "Blue");
        Theme.ThemeAccent accent = themeAccent == null ? Theme.getCurrentTheme().getAccent(false) : themeAccent;
        if (accent == null || (i = accent.accentColor) == 0) {
            i = AndroidUtilities.calcDrawableColor(Theme.getCachedWallpaper())[0];
        }
        String str = null;
        int i2 = Integer.MAX_VALUE;
        int red = Color.red(i);
        int green = Color.green(i);
        int blue = Color.blue(i);
        for (Map.Entry entry : hashMap.entrySet()) {
            Integer num = (Integer) entry.getKey();
            int red2 = Color.red(num.intValue());
            int i3 = (red + red2) / 2;
            int i4 = red - red2;
            int green2 = green - Color.green(num.intValue());
            int blue2 = blue - Color.blue(num.intValue());
            int i5 = ((((i3 + 512) * i4) * i4) >> 8) + (green2 * 4 * green2) + ((((767 - i3) * blue2) * blue2) >> 8);
            if (i5 < i2) {
                str = (String) entry.getValue();
                i2 = i5;
            }
        }
        if (Utilities.random.nextInt() % 2 == 0) {
            return ((String) asList.get(Utilities.random.nextInt(asList.size()))) + " " + str;
        }
        return str + " " + ((String) asList2.get(Utilities.random.nextInt(asList2.size())));
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public static ActionBarPopupWindow showPopupMenu(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, View view, int i, int i2) {
        android.graphics.Rect rect = new android.graphics.Rect();
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2);
        if (Build.VERSION.SDK_INT >= 19) {
            actionBarPopupWindow.setAnimationStyle(0);
        } else {
            actionBarPopupWindow.setAnimationStyle(2131689480);
        }
        actionBarPopupWindow.setAnimationEnabled(true);
        actionBarPopupWindow.setOutsideTouchable(true);
        actionBarPopupWindow.setClippingEnabled(true);
        actionBarPopupWindow.setInputMethodMode(2);
        actionBarPopupWindow.setSoftInputMode(0);
        actionBarPopupWindow.setFocusable(true);
        actionBarPopupWindowLayout.setFocusableInTouchMode(true);
        actionBarPopupWindowLayout.setOnKeyListener(new AlertsCreator$$ExternalSyntheticLambda74(actionBarPopupWindow));
        actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x - AndroidUtilities.dp(40.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.y, Integer.MIN_VALUE));
        actionBarPopupWindow.showAsDropDown(view, i, i2);
        actionBarPopupWindowLayout.updateRadialSelectors();
        actionBarPopupWindow.startAnimation();
        actionBarPopupWindowLayout.setOnTouchListener(new AlertsCreator$$ExternalSyntheticLambda75(actionBarPopupWindow, rect));
        return actionBarPopupWindow;
    }

    public static /* synthetic */ boolean lambda$showPopupMenu$127(ActionBarPopupWindow actionBarPopupWindow, View view, int i, KeyEvent keyEvent) {
        if (i == 82 && keyEvent.getRepeatCount() == 0 && keyEvent.getAction() == 1 && actionBarPopupWindow.isShowing()) {
            actionBarPopupWindow.dismiss();
            return true;
        }
        return false;
    }

    public static /* synthetic */ boolean lambda$showPopupMenu$128(ActionBarPopupWindow actionBarPopupWindow, android.graphics.Rect rect, View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0 || actionBarPopupWindow == null || !actionBarPopupWindow.isShowing()) {
            return false;
        }
        view.getHitRect(rect);
        if (rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            return false;
        }
        actionBarPopupWindow.dismiss();
        return false;
    }
}
