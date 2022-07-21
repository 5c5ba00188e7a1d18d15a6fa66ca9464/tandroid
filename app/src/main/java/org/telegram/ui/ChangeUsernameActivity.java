package org.telegram.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_account_checkUsername;
import org.telegram.tgnet.TLRPC$TL_account_updateUsername;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
/* loaded from: classes3.dex */
public class ChangeUsernameActivity extends BaseFragment {
    private int checkReqId;
    private Runnable checkRunnable;
    private TextView checkTextView;
    private View doneButton;
    private EditTextBoldCursor firstNameField;
    private TextView helpTextView;
    private boolean ignoreCheck;
    private CharSequence infoText;
    private String lastCheckName;

    public static /* synthetic */ boolean lambda$createView$0(View view, MotionEvent motionEvent) {
        return true;
    }

    /* loaded from: classes3.dex */
    public class LinkSpan extends ClickableSpan {
        private String url;

        public LinkSpan(String str) {
            ChangeUsernameActivity.this = r1;
            this.url = str;
        }

        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(View view) {
            try {
                ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", this.url));
                if (!BulletinFactory.canShowBulletin(ChangeUsernameActivity.this)) {
                    return;
                }
                BulletinFactory.createCopyLinkBulletin(ChangeUsernameActivity.this).show();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* loaded from: classes3.dex */
    private static class LinkMovementMethodMy extends LinkMovementMethod {
        private LinkMovementMethodMy() {
        }

        /* synthetic */ LinkMovementMethodMy(AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // android.text.method.LinkMovementMethod, android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod, android.text.method.MovementMethod
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
            try {
                boolean onTouchEvent = super.onTouchEvent(textView, spannable, motionEvent);
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    Selection.removeSelection(spannable);
                }
                return onTouchEvent;
            } catch (Exception e) {
                FileLog.e(e);
                return false;
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        String str;
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("Username", 2131628916));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.doneButton = this.actionBar.createMenu().addItemWithWidth(1, 2131165450, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", 2131625541));
        TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(UserConfig.getInstance(this.currentAccount).getClientUserId()));
        if (user == null) {
            user = UserConfig.getInstance(this.currentAccount).getCurrentUser();
        }
        LinearLayout linearLayout = new LinearLayout(context);
        this.fragmentView = linearLayout;
        LinearLayout linearLayout2 = linearLayout;
        linearLayout2.setOrientation(1);
        this.fragmentView.setOnTouchListener(ChangeUsernameActivity$$ExternalSyntheticLambda1.INSTANCE);
        EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
        this.firstNameField = editTextBoldCursor;
        editTextBoldCursor.setTextSize(1, 18.0f);
        this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setBackgroundDrawable(null);
        this.firstNameField.setLineColors(getThemedColor("windowBackgroundWhiteInputField"), getThemedColor("windowBackgroundWhiteInputFieldActivated"), getThemedColor("windowBackgroundWhiteRedText3"));
        this.firstNameField.setMaxLines(1);
        this.firstNameField.setLines(1);
        this.firstNameField.setPadding(0, 0, 0, 0);
        this.firstNameField.setSingleLine(true);
        this.firstNameField.setGravity(LocaleController.isRTL ? 5 : 3);
        this.firstNameField.setInputType(180224);
        this.firstNameField.setImeOptions(6);
        this.firstNameField.setHint(LocaleController.getString("UsernamePlaceholder", 2131628928));
        this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.firstNameField.setCursorWidth(1.5f);
        this.firstNameField.setOnEditorActionListener(new ChangeUsernameActivity$$ExternalSyntheticLambda2(this));
        this.firstNameField.addTextChangedListener(new AnonymousClass2());
        linearLayout2.addView(this.firstNameField, LayoutHelper.createLinear(-1, 36, 24.0f, 24.0f, 24.0f, 0.0f));
        TextView textView = new TextView(context);
        this.checkTextView = textView;
        textView.setTextSize(1, 15.0f);
        this.checkTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        linearLayout2.addView(this.checkTextView, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3, 24, 12, 24, 0));
        TextView textView2 = new TextView(context);
        this.helpTextView = textView2;
        textView2.setTextSize(1, 15.0f);
        this.helpTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
        this.helpTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        TextView textView3 = this.helpTextView;
        SpannableStringBuilder replaceTags = AndroidUtilities.replaceTags(LocaleController.getString("UsernameHelp", 2131628921));
        this.infoText = replaceTags;
        textView3.setText(replaceTags);
        this.helpTextView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
        this.helpTextView.setHighlightColor(Theme.getColor("windowBackgroundWhiteLinkSelection"));
        this.helpTextView.setMovementMethod(new LinkMovementMethodMy(null));
        linearLayout2.addView(this.helpTextView, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3, 24, 10, 24, 0));
        this.checkTextView.setVisibility(8);
        if (user != null && (str = user.username) != null && str.length() > 0) {
            this.ignoreCheck = true;
            this.firstNameField.setText(user.username);
            EditTextBoldCursor editTextBoldCursor2 = this.firstNameField;
            editTextBoldCursor2.setSelection(editTextBoldCursor2.length());
            this.ignoreCheck = false;
        }
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.ChangeUsernameActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            ChangeUsernameActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                ChangeUsernameActivity.this.finishFragment();
            } else if (i != 1) {
            } else {
                ChangeUsernameActivity.this.saveName();
            }
        }
    }

    public /* synthetic */ boolean lambda$createView$1(TextView textView, int i, KeyEvent keyEvent) {
        View view;
        if (i != 6 || (view = this.doneButton) == null) {
            return false;
        }
        view.performClick();
        return true;
    }

    /* renamed from: org.telegram.ui.ChangeUsernameActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass2() {
            ChangeUsernameActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (ChangeUsernameActivity.this.ignoreCheck) {
                return;
            }
            ChangeUsernameActivity changeUsernameActivity = ChangeUsernameActivity.this;
            changeUsernameActivity.checkUserName(changeUsernameActivity.firstNameField.getText().toString(), false);
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            String obj = ChangeUsernameActivity.this.firstNameField.getText().toString();
            if (obj.startsWith("@")) {
                obj = obj.substring(1);
            }
            if (obj.length() <= 0) {
                ChangeUsernameActivity.this.helpTextView.setText(ChangeUsernameActivity.this.infoText);
                return;
            }
            String str = "https://" + MessagesController.getInstance(((BaseFragment) ChangeUsernameActivity.this).currentAccount).linkPrefix + "/" + obj;
            String formatString = LocaleController.formatString("UsernameHelpLink", 2131628922, str);
            int indexOf = formatString.indexOf(str);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(formatString);
            if (indexOf >= 0) {
                spannableStringBuilder.setSpan(new LinkSpan(str), indexOf, str.length() + indexOf, 33);
            }
            ChangeUsernameActivity.this.helpTextView.setText(TextUtils.concat(ChangeUsernameActivity.this.infoText, "\n\n", spannableStringBuilder));
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        if (!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            this.firstNameField.requestFocus();
            AndroidUtilities.showKeyboard(this.firstNameField);
        }
    }

    public boolean checkUserName(String str, boolean z) {
        if (str != null && str.startsWith("@")) {
            str = str.substring(1);
        }
        if (!TextUtils.isEmpty(str)) {
            this.checkTextView.setVisibility(0);
        } else {
            this.checkTextView.setVisibility(8);
        }
        if (!z || str.length() != 0) {
            Runnable runnable = this.checkRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.checkRunnable = null;
                this.lastCheckName = null;
                if (this.checkReqId != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.checkReqId, true);
                }
            }
            if (str != null) {
                if (str.startsWith("_") || str.endsWith("_")) {
                    this.checkTextView.setText(LocaleController.getString("UsernameInvalid", 2131628924));
                    this.checkTextView.setTag("windowBackgroundWhiteRedText4");
                    this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                    return false;
                }
                for (int i = 0; i < str.length(); i++) {
                    char charAt = str.charAt(i);
                    if (i == 0 && charAt >= '0' && charAt <= '9') {
                        if (z) {
                            AlertsCreator.showSimpleAlert(this, LocaleController.getString("UsernameInvalidStartNumber", 2131628927));
                        } else {
                            this.checkTextView.setText(LocaleController.getString("UsernameInvalidStartNumber", 2131628927));
                            this.checkTextView.setTag("windowBackgroundWhiteRedText4");
                            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                        }
                        return false;
                    } else if ((charAt < '0' || charAt > '9') && ((charAt < 'a' || charAt > 'z') && ((charAt < 'A' || charAt > 'Z') && charAt != '_'))) {
                        if (z) {
                            AlertsCreator.showSimpleAlert(this, LocaleController.getString("UsernameInvalid", 2131628924));
                        } else {
                            this.checkTextView.setText(LocaleController.getString("UsernameInvalid", 2131628924));
                            this.checkTextView.setTag("windowBackgroundWhiteRedText4");
                            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                        }
                        return false;
                    }
                }
            }
            if (str == null || str.length() < 5) {
                if (z) {
                    AlertsCreator.showSimpleAlert(this, LocaleController.getString("UsernameInvalidShort", 2131628926));
                } else {
                    this.checkTextView.setText(LocaleController.getString("UsernameInvalidShort", 2131628926));
                    this.checkTextView.setTag("windowBackgroundWhiteRedText4");
                    this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                }
                return false;
            } else if (str.length() > 32) {
                if (z) {
                    AlertsCreator.showSimpleAlert(this, LocaleController.getString("UsernameInvalidLong", 2131628925));
                } else {
                    this.checkTextView.setText(LocaleController.getString("UsernameInvalidLong", 2131628925));
                    this.checkTextView.setTag("windowBackgroundWhiteRedText4");
                    this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                }
                return false;
            } else {
                if (!z) {
                    String str2 = UserConfig.getInstance(this.currentAccount).getCurrentUser().username;
                    if (str2 == null) {
                        str2 = "";
                    }
                    if (str.equals(str2)) {
                        this.checkTextView.setText(LocaleController.formatString("UsernameAvailable", 2131628917, str));
                        this.checkTextView.setTag("windowBackgroundWhiteGreenText");
                        this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGreenText"));
                        return true;
                    }
                    this.checkTextView.setText(LocaleController.getString("UsernameChecking", 2131628918));
                    this.checkTextView.setTag("windowBackgroundWhiteGrayText8");
                    this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
                    this.lastCheckName = str;
                    ChangeUsernameActivity$$ExternalSyntheticLambda3 changeUsernameActivity$$ExternalSyntheticLambda3 = new ChangeUsernameActivity$$ExternalSyntheticLambda3(this, str);
                    this.checkRunnable = changeUsernameActivity$$ExternalSyntheticLambda3;
                    AndroidUtilities.runOnUIThread(changeUsernameActivity$$ExternalSyntheticLambda3, 300L);
                }
                return true;
            }
        }
        return true;
    }

    public /* synthetic */ void lambda$checkUserName$4(String str) {
        TLRPC$TL_account_checkUsername tLRPC$TL_account_checkUsername = new TLRPC$TL_account_checkUsername();
        tLRPC$TL_account_checkUsername.username = str;
        this.checkReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_checkUsername, new ChangeUsernameActivity$$ExternalSyntheticLambda7(this, str), 2);
    }

    public /* synthetic */ void lambda$checkUserName$3(String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChangeUsernameActivity$$ExternalSyntheticLambda4(this, str, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$checkUserName$2(String str, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        this.checkReqId = 0;
        String str2 = this.lastCheckName;
        if (str2 == null || !str2.equals(str)) {
            return;
        }
        if (tLRPC$TL_error == null && (tLObject instanceof TLRPC$TL_boolTrue)) {
            this.checkTextView.setText(LocaleController.formatString("UsernameAvailable", 2131628917, str));
            this.checkTextView.setTag("windowBackgroundWhiteGreenText");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGreenText"));
            return;
        }
        this.checkTextView.setText(LocaleController.getString("UsernameInUse", 2131628923));
        this.checkTextView.setTag("windowBackgroundWhiteRedText4");
        this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
    }

    public void saveName() {
        String obj = this.firstNameField.getText().toString();
        if (obj.startsWith("@")) {
            obj = obj.substring(1);
        }
        if (!checkUserName(obj, true)) {
            return;
        }
        TLRPC$User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
        if (getParentActivity() == null || currentUser == null) {
            return;
        }
        String str = currentUser.username;
        if (str == null) {
            str = "";
        }
        if (str.equals(obj)) {
            finishFragment();
            return;
        }
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        TLRPC$TL_account_updateUsername tLRPC$TL_account_updateUsername = new TLRPC$TL_account_updateUsername();
        tLRPC$TL_account_updateUsername.username = obj;
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_NAME));
        int sendRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_updateUsername, new ChangeUsernameActivity$$ExternalSyntheticLambda8(this, alertDialog, tLRPC$TL_account_updateUsername), 2);
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(sendRequest, this.classGuid);
        alertDialog.setOnCancelListener(new ChangeUsernameActivity$$ExternalSyntheticLambda0(this, sendRequest));
        alertDialog.show();
    }

    public /* synthetic */ void lambda$saveName$7(AlertDialog alertDialog, TLRPC$TL_account_updateUsername tLRPC$TL_account_updateUsername, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            AndroidUtilities.runOnUIThread(new ChangeUsernameActivity$$ExternalSyntheticLambda6(this, alertDialog, (TLRPC$User) tLObject));
        } else {
            AndroidUtilities.runOnUIThread(new ChangeUsernameActivity$$ExternalSyntheticLambda5(this, alertDialog, tLRPC$TL_error, tLRPC$TL_account_updateUsername));
        }
    }

    public /* synthetic */ void lambda$saveName$5(AlertDialog alertDialog, TLRPC$User tLRPC$User) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        ArrayList<TLRPC$User> arrayList = new ArrayList<>();
        arrayList.add(tLRPC$User);
        MessagesController.getInstance(this.currentAccount).putUsers(arrayList, false);
        MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(arrayList, null, false, true);
        UserConfig.getInstance(this.currentAccount).saveConfig(true);
        finishFragment();
    }

    public /* synthetic */ void lambda$saveName$6(AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_account_updateUsername tLRPC$TL_account_updateUsername) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, this, tLRPC$TL_account_updateUsername, new Object[0]);
    }

    public /* synthetic */ void lambda$saveName$8(int i, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(i, true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z) {
            this.firstNameField.requestFocus();
            AndroidUtilities.showKeyboard(this.firstNameField);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
        arrayList.add(new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
        arrayList.add(new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
        arrayList.add(new ThemeDescription(this.helpTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText8"));
        arrayList.add(new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteRedText4"));
        arrayList.add(new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhiteGreenText"));
        arrayList.add(new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText8"));
        return arrayList;
    }
}
