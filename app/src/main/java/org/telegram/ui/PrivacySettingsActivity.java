package org.telegram.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$PrivacyRule;
import org.telegram.tgnet.TLRPC$TL_accountDaysTTL;
import org.telegram.tgnet.TLRPC$TL_account_authorizationForm;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_password;
import org.telegram.tgnet.TLRPC$TL_account_setAccountTTL;
import org.telegram.tgnet.TLRPC$TL_account_setGlobalPrivacySettings;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_contacts_toggleTopPeers;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_globalPrivacySettings;
import org.telegram.tgnet.TLRPC$TL_payments_clearSavedInfo;
import org.telegram.tgnet.TLRPC$TL_privacyValueAllowAll;
import org.telegram.tgnet.TLRPC$TL_privacyValueAllowChatParticipants;
import org.telegram.tgnet.TLRPC$TL_privacyValueAllowUsers;
import org.telegram.tgnet.TLRPC$TL_privacyValueDisallowAll;
import org.telegram.tgnet.TLRPC$TL_privacyValueDisallowChatParticipants;
import org.telegram.tgnet.TLRPC$TL_privacyValueDisallowUsers;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.RadioColorCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class PrivacySettingsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int advancedSectionRow;
    private boolean archiveChats;
    private int blockedRow;
    private int botsDetailRow;
    private int botsSectionRow;
    private int callsRow;
    private boolean[] clear = new boolean[2];
    private int contactsDeleteRow;
    private int contactsDetailRow;
    private int contactsSectionRow;
    private int contactsSuggestRow;
    private int contactsSyncRow;
    private TLRPC$TL_account_password currentPassword;
    private boolean currentSuggest;
    private boolean currentSync;
    private int deleteAccountDetailRow;
    private int deleteAccountRow;
    private int forwardsRow;
    private int groupsDetailRow;
    private int groupsRow;
    private int lastSeenRow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int newChatsHeaderRow;
    private int newChatsRow;
    private int newChatsSectionRow;
    private boolean newSuggest;
    private boolean newSync;
    private int passcodeRow;
    private int passportRow;
    private int passwordRow;
    private int paymentsClearRow;
    private int phoneNumberRow;
    private int privacySectionRow;
    private int profilePhotoRow;
    private AlertDialog progressDialog;
    private int rowCount;
    private int secretDetailRow;
    private int secretMapRow;
    private int secretSectionRow;
    private int secretWebpageRow;
    private int securitySectionRow;
    private int sessionsDetailRow;
    private int sessionsRow;
    private int webSessionsRow;

    public static /* synthetic */ void lambda$createView$12(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public static /* synthetic */ void lambda$onFragmentDestroy$0(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public static /* synthetic */ void lambda$onFragmentDestroy$1(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        getContactsController().loadPrivacySettings();
        getMessagesController().getBlockedPeers(true);
        boolean z = getUserConfig().syncContacts;
        this.newSync = z;
        this.currentSync = z;
        boolean z2 = getUserConfig().suggestContacts;
        this.newSuggest = z2;
        this.currentSuggest = z2;
        TLRPC$TL_globalPrivacySettings globalPrivacySettings = getContactsController().getGlobalPrivacySettings();
        if (globalPrivacySettings != null) {
            this.archiveChats = globalPrivacySettings.archive_and_mute_new_noncontact_peers;
        }
        updateRows();
        loadPasswordSettings();
        getNotificationCenter().addObserver(this, NotificationCenter.privacyRulesUpdated);
        getNotificationCenter().addObserver(this, NotificationCenter.blockedUsersDidLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.didSetOrRemoveTwoStepPassword);
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onFragmentDestroy() {
        boolean z;
        super.onFragmentDestroy();
        getNotificationCenter().removeObserver(this, NotificationCenter.privacyRulesUpdated);
        getNotificationCenter().removeObserver(this, NotificationCenter.blockedUsersDidLoad);
        getNotificationCenter().removeObserver(this, NotificationCenter.didSetOrRemoveTwoStepPassword);
        boolean z2 = true;
        if (this.currentSync != this.newSync) {
            UserConfig userConfig = getUserConfig();
            boolean z3 = this.newSync;
            userConfig.syncContacts = z3;
            if (z3) {
                getContactsController().forceImportContacts();
                if (getParentActivity() != null) {
                    Toast.makeText(getParentActivity(), LocaleController.getString("SyncContactsAdded", 2131628526), 0).show();
                }
            }
            z = true;
        } else {
            z = false;
        }
        boolean z4 = this.newSuggest;
        if (z4 != this.currentSuggest) {
            if (!z4) {
                getMediaDataController().clearTopPeers();
            }
            getUserConfig().suggestContacts = this.newSuggest;
            TLRPC$TL_contacts_toggleTopPeers tLRPC$TL_contacts_toggleTopPeers = new TLRPC$TL_contacts_toggleTopPeers();
            tLRPC$TL_contacts_toggleTopPeers.enabled = this.newSuggest;
            getConnectionsManager().sendRequest(tLRPC$TL_contacts_toggleTopPeers, PrivacySettingsActivity$$ExternalSyntheticLambda16.INSTANCE);
            z = true;
        }
        TLRPC$TL_globalPrivacySettings globalPrivacySettings = getContactsController().getGlobalPrivacySettings();
        if (globalPrivacySettings != null) {
            boolean z5 = globalPrivacySettings.archive_and_mute_new_noncontact_peers;
            boolean z6 = this.archiveChats;
            if (z5 != z6) {
                globalPrivacySettings.archive_and_mute_new_noncontact_peers = z6;
                TLRPC$TL_account_setGlobalPrivacySettings tLRPC$TL_account_setGlobalPrivacySettings = new TLRPC$TL_account_setGlobalPrivacySettings();
                TLRPC$TL_globalPrivacySettings tLRPC$TL_globalPrivacySettings = new TLRPC$TL_globalPrivacySettings();
                tLRPC$TL_account_setGlobalPrivacySettings.settings = tLRPC$TL_globalPrivacySettings;
                tLRPC$TL_globalPrivacySettings.flags |= 1;
                tLRPC$TL_globalPrivacySettings.archive_and_mute_new_noncontact_peers = this.archiveChats;
                getConnectionsManager().sendRequest(tLRPC$TL_account_setGlobalPrivacySettings, PrivacySettingsActivity$$ExternalSyntheticLambda14.INSTANCE);
                if (z2) {
                    return;
                }
                getUserConfig().saveConfig(false);
                return;
            }
        }
        z2 = z;
        if (z2) {
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("PrivacySettings", 2131627735));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setLayoutManager(new AnonymousClass2(this, context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new PrivacySettingsActivity$$ExternalSyntheticLambda17(this));
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.PrivacySettingsActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            PrivacySettingsActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                PrivacySettingsActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.PrivacySettingsActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends LinearLayoutManager {
        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        AnonymousClass2(PrivacySettingsActivity privacySettingsActivity, Context context, int i, boolean z) {
            super(context, i, z);
        }
    }

    public /* synthetic */ void lambda$createView$15(View view, int i) {
        String str;
        if (!view.isEnabled()) {
            return;
        }
        if (i == this.blockedRow) {
            presentFragment(new PrivacyUsersActivity());
            return;
        }
        boolean z = false;
        if (i == this.sessionsRow) {
            presentFragment(new SessionsActivity(0));
        } else if (i == this.webSessionsRow) {
            presentFragment(new SessionsActivity(1));
        } else {
            int i2 = 6;
            if (i == this.deleteAccountRow) {
                if (getParentActivity() == null) {
                    return;
                }
                int deleteAccountTTL = getContactsController().getDeleteAccountTTL();
                int i3 = deleteAccountTTL <= 31 ? 0 : deleteAccountTTL <= 93 ? 1 : deleteAccountTTL <= 182 ? 2 : 3;
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("DeleteAccountTitle", 2131625372));
                String[] strArr = {LocaleController.formatPluralString("Months", 1, new Object[0]), LocaleController.formatPluralString("Months", 3, new Object[0]), LocaleController.formatPluralString("Months", 6, new Object[0]), LocaleController.formatPluralString("Years", 1, new Object[0])};
                LinearLayout linearLayout = new LinearLayout(getParentActivity());
                linearLayout.setOrientation(1);
                builder.setView(linearLayout);
                int i4 = 0;
                while (i4 < 4) {
                    RadioColorCell radioColorCell = new RadioColorCell(getParentActivity());
                    radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                    radioColorCell.setTag(Integer.valueOf(i4));
                    radioColorCell.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
                    radioColorCell.setTextAndValue(strArr[i4], i3 == i4);
                    linearLayout.addView(radioColorCell);
                    radioColorCell.setOnClickListener(new PrivacySettingsActivity$$ExternalSyntheticLambda5(this, builder));
                    i4++;
                }
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                showDialog(builder.create());
            } else if (i == this.lastSeenRow) {
                presentFragment(new PrivacyControlActivity(0));
            } else if (i == this.phoneNumberRow) {
                presentFragment(new PrivacyControlActivity(6));
            } else if (i == this.groupsRow) {
                presentFragment(new PrivacyControlActivity(1));
            } else if (i == this.callsRow) {
                presentFragment(new PrivacyControlActivity(2));
            } else if (i == this.profilePhotoRow) {
                presentFragment(new PrivacyControlActivity(4));
            } else if (i == this.forwardsRow) {
                presentFragment(new PrivacyControlActivity(5));
            } else if (i == this.passwordRow) {
                TLRPC$TL_account_password tLRPC$TL_account_password = this.currentPassword;
                if (tLRPC$TL_account_password == null) {
                    return;
                }
                if (!TwoStepVerificationActivity.canHandleCurrentPassword(tLRPC$TL_account_password, false)) {
                    AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131628758), true);
                }
                TLRPC$TL_account_password tLRPC$TL_account_password2 = this.currentPassword;
                if (tLRPC$TL_account_password2.has_password) {
                    TwoStepVerificationActivity twoStepVerificationActivity = new TwoStepVerificationActivity();
                    twoStepVerificationActivity.setPassword(this.currentPassword);
                    presentFragment(twoStepVerificationActivity);
                    return;
                }
                if (!TextUtils.isEmpty(tLRPC$TL_account_password2.email_unconfirmed_pattern)) {
                    i2 = 5;
                }
                presentFragment(new TwoStepVerificationSetupActivity(i2, this.currentPassword));
            } else if (i == this.passcodeRow) {
                presentFragment(PasscodeActivity.determineOpenFragment());
            } else if (i == this.secretWebpageRow) {
                if (getMessagesController().secretWebpagePreview == 1) {
                    getMessagesController().secretWebpagePreview = 0;
                } else {
                    getMessagesController().secretWebpagePreview = 1;
                }
                MessagesController.getGlobalMainSettings().edit().putInt("secretWebpage2", getMessagesController().secretWebpagePreview).commit();
                if (!(view instanceof TextCheckCell)) {
                    return;
                }
                TextCheckCell textCheckCell = (TextCheckCell) view;
                if (getMessagesController().secretWebpagePreview == 1) {
                    z = true;
                }
                textCheckCell.setChecked(z);
            } else if (i == this.contactsDeleteRow) {
                if (getParentActivity() == null) {
                    return;
                }
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                builder2.setTitle(LocaleController.getString("SyncContactsDeleteTitle", 2131628529));
                builder2.setMessage(AndroidUtilities.replaceTags(LocaleController.getString("SyncContactsDeleteText", 2131628528)));
                builder2.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                builder2.setPositiveButton(LocaleController.getString("Delete", 2131625368), new PrivacySettingsActivity$$ExternalSyntheticLambda0(this));
                AlertDialog create = builder2.create();
                showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView == null) {
                    return;
                }
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
            } else if (i == this.contactsSuggestRow) {
                TextCheckCell textCheckCell2 = (TextCheckCell) view;
                if (this.newSuggest) {
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(getParentActivity());
                    builder3.setTitle(LocaleController.getString("SuggestContactsTitle", 2131628501));
                    builder3.setMessage(LocaleController.getString("SuggestContactsAlert", 2131628499));
                    builder3.setPositiveButton(LocaleController.getString("MuteDisable", 2131626746), new PrivacySettingsActivity$$ExternalSyntheticLambda3(this, textCheckCell2));
                    builder3.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                    AlertDialog create2 = builder3.create();
                    showDialog(create2);
                    TextView textView2 = (TextView) create2.getButton(-1);
                    if (textView2 == null) {
                        return;
                    }
                    textView2.setTextColor(Theme.getColor("dialogTextRed2"));
                    return;
                }
                this.newSuggest = true;
                textCheckCell2.setChecked(true);
            } else if (i == this.newChatsRow) {
                boolean z2 = !this.archiveChats;
                this.archiveChats = z2;
                ((TextCheckCell) view).setChecked(z2);
            } else if (i == this.contactsSyncRow) {
                boolean z3 = !this.newSync;
                this.newSync = z3;
                if (!(view instanceof TextCheckCell)) {
                    return;
                }
                ((TextCheckCell) view).setChecked(z3);
            } else if (i == this.secretMapRow) {
                AlertsCreator.showSecretLocationAlert(getParentActivity(), this.currentAccount, new PrivacySettingsActivity$$ExternalSyntheticLambda7(this), false, null);
            } else if (i == this.paymentsClearRow) {
                AlertDialog.Builder builder4 = new AlertDialog.Builder(getParentActivity());
                builder4.setTitle(LocaleController.getString("PrivacyPaymentsClearAlertTitle", 2131627717));
                builder4.setMessage(LocaleController.getString("PrivacyPaymentsClearAlertText", 2131627716));
                LinearLayout linearLayout2 = new LinearLayout(getParentActivity());
                linearLayout2.setOrientation(1);
                builder4.setView(linearLayout2);
                int i5 = 0;
                for (int i6 = 2; i5 < i6; i6 = 2) {
                    if (i5 == 0) {
                        str = LocaleController.getString("PrivacyClearShipping", 2131627698);
                    } else {
                        str = LocaleController.getString("PrivacyClearPayment", 2131627697);
                    }
                    this.clear[i5] = true;
                    CheckBoxCell checkBoxCell = new CheckBoxCell(getParentActivity(), 1, 21, null);
                    checkBoxCell.setTag(Integer.valueOf(i5));
                    checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    checkBoxCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                    linearLayout2.addView(checkBoxCell, LayoutHelper.createLinear(-1, 50));
                    checkBoxCell.setText(str, null, true, false);
                    checkBoxCell.setTextColor(Theme.getColor("dialogTextBlack"));
                    checkBoxCell.setOnClickListener(new PrivacySettingsActivity$$ExternalSyntheticLambda4(this));
                    i5++;
                }
                builder4.setPositiveButton(LocaleController.getString("ClearButton", 2131625132), new PrivacySettingsActivity$$ExternalSyntheticLambda2(this));
                builder4.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                showDialog(builder4.create());
                AlertDialog create3 = builder4.create();
                showDialog(create3);
                TextView textView3 = (TextView) create3.getButton(-1);
                if (textView3 == null) {
                    return;
                }
                textView3.setTextColor(Theme.getColor("dialogTextRed2"));
            } else if (i == this.passportRow) {
                presentFragment(new PassportActivity(5, 0L, "", "", (String) null, (String) null, (String) null, (TLRPC$TL_account_authorizationForm) null, (TLRPC$TL_account_password) null));
            }
        }
    }

    public /* synthetic */ void lambda$createView$4(AlertDialog.Builder builder, View view) {
        int i;
        builder.getDismissRunnable().run();
        Integer num = (Integer) view.getTag();
        if (num.intValue() == 0) {
            i = 30;
        } else if (num.intValue() == 1) {
            i = 90;
        } else if (num.intValue() == 2) {
            i = 182;
        } else {
            i = num.intValue() == 3 ? 365 : 0;
        }
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        alertDialog.setCanCancel(false);
        alertDialog.show();
        TLRPC$TL_account_setAccountTTL tLRPC$TL_account_setAccountTTL = new TLRPC$TL_account_setAccountTTL();
        TLRPC$TL_accountDaysTTL tLRPC$TL_accountDaysTTL = new TLRPC$TL_accountDaysTTL();
        tLRPC$TL_account_setAccountTTL.ttl = tLRPC$TL_accountDaysTTL;
        tLRPC$TL_accountDaysTTL.days = i;
        getConnectionsManager().sendRequest(tLRPC$TL_account_setAccountTTL, new PrivacySettingsActivity$$ExternalSyntheticLambda12(this, alertDialog, tLRPC$TL_account_setAccountTTL));
    }

    public /* synthetic */ void lambda$createView$3(AlertDialog alertDialog, TLRPC$TL_account_setAccountTTL tLRPC$TL_account_setAccountTTL, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PrivacySettingsActivity$$ExternalSyntheticLambda9(this, alertDialog, tLObject, tLRPC$TL_account_setAccountTTL));
    }

    public /* synthetic */ void lambda$createView$2(AlertDialog alertDialog, TLObject tLObject, TLRPC$TL_account_setAccountTTL tLRPC$TL_account_setAccountTTL) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            getContactsController().setDeleteAccountTTL(tLRPC$TL_account_setAccountTTL.ttl.days);
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public /* synthetic */ void lambda$createView$6(DialogInterface dialogInterface, int i) {
        AlertDialog show = new AlertDialog.Builder(getParentActivity(), 3, null).show();
        this.progressDialog = show;
        show.setCanCancel(false);
        if (this.currentSync != this.newSync) {
            UserConfig userConfig = getUserConfig();
            boolean z = this.newSync;
            userConfig.syncContacts = z;
            this.currentSync = z;
            getUserConfig().saveConfig(false);
        }
        getContactsController().deleteAllContacts(new PrivacySettingsActivity$$ExternalSyntheticLambda6(this));
    }

    public /* synthetic */ void lambda$createView$5() {
        this.progressDialog.dismiss();
    }

    public /* synthetic */ void lambda$createView$9(TextCheckCell textCheckCell, DialogInterface dialogInterface, int i) {
        TLRPC$TL_payments_clearSavedInfo tLRPC$TL_payments_clearSavedInfo = new TLRPC$TL_payments_clearSavedInfo();
        boolean[] zArr = this.clear;
        tLRPC$TL_payments_clearSavedInfo.credentials = zArr[1];
        tLRPC$TL_payments_clearSavedInfo.info = zArr[0];
        getUserConfig().tmpPassword = null;
        getUserConfig().saveConfig(false);
        getConnectionsManager().sendRequest(tLRPC$TL_payments_clearSavedInfo, new PrivacySettingsActivity$$ExternalSyntheticLambda13(this, textCheckCell));
    }

    public /* synthetic */ void lambda$createView$8(TextCheckCell textCheckCell, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PrivacySettingsActivity$$ExternalSyntheticLambda10(this, textCheckCell));
    }

    public /* synthetic */ void lambda$createView$7(TextCheckCell textCheckCell) {
        boolean z = !this.newSuggest;
        this.newSuggest = z;
        textCheckCell.setChecked(z);
    }

    public /* synthetic */ void lambda$createView$10() {
        this.listAdapter.notifyDataSetChanged();
    }

    public /* synthetic */ void lambda$createView$11(View view) {
        CheckBoxCell checkBoxCell = (CheckBoxCell) view;
        int intValue = ((Integer) checkBoxCell.getTag()).intValue();
        boolean[] zArr = this.clear;
        zArr[intValue] = !zArr[intValue];
        checkBoxCell.setChecked(zArr[intValue], true);
    }

    public /* synthetic */ void lambda$createView$14(DialogInterface dialogInterface, int i) {
        try {
            Dialog dialog = this.visibleDialog;
            if (dialog != null) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("PrivacyPaymentsClearAlertTitle", 2131627717));
        builder.setMessage(LocaleController.getString("PrivacyPaymentsClearAlert", 2131627715));
        builder.setPositiveButton(LocaleController.getString("ClearButton", 2131625132), new PrivacySettingsActivity$$ExternalSyntheticLambda1(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
        showDialog(builder.create());
        AlertDialog create = builder.create();
        showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor("dialogTextRed2"));
        }
    }

    public /* synthetic */ void lambda$createView$13(DialogInterface dialogInterface, int i) {
        String str;
        TLRPC$TL_payments_clearSavedInfo tLRPC$TL_payments_clearSavedInfo = new TLRPC$TL_payments_clearSavedInfo();
        boolean[] zArr = this.clear;
        tLRPC$TL_payments_clearSavedInfo.credentials = zArr[1];
        tLRPC$TL_payments_clearSavedInfo.info = zArr[0];
        getUserConfig().tmpPassword = null;
        getUserConfig().saveConfig(false);
        getConnectionsManager().sendRequest(tLRPC$TL_payments_clearSavedInfo, PrivacySettingsActivity$$ExternalSyntheticLambda15.INSTANCE);
        boolean[] zArr2 = this.clear;
        if (zArr2[0] && zArr2[1]) {
            str = LocaleController.getString("PrivacyPaymentsPaymentShippingCleared", 2131627719);
        } else if (zArr2[0]) {
            str = LocaleController.getString("PrivacyPaymentsShippingInfoCleared", 2131627720);
        } else if (!zArr2[1]) {
            return;
        } else {
            str = LocaleController.getString("PrivacyPaymentsPaymentInfoCleared", 2131627718);
        }
        BulletinFactory.of(this).createSimpleBulletin(2131558424, str).show();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.privacyRulesUpdated) {
            TLRPC$TL_globalPrivacySettings globalPrivacySettings = getContactsController().getGlobalPrivacySettings();
            if (globalPrivacySettings != null) {
                this.archiveChats = globalPrivacySettings.archive_and_mute_new_noncontact_peers;
            }
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter == null) {
                return;
            }
            listAdapter.notifyDataSetChanged();
        } else if (i == NotificationCenter.blockedUsersDidLoad) {
            this.listAdapter.notifyItemChanged(this.blockedRow);
        } else if (i != NotificationCenter.didSetOrRemoveTwoStepPassword) {
        } else {
            if (objArr.length > 0) {
                this.currentPassword = (TLRPC$TL_account_password) objArr[0];
                ListAdapter listAdapter2 = this.listAdapter;
                if (listAdapter2 == null) {
                    return;
                }
                listAdapter2.notifyItemChanged(this.passwordRow);
                return;
            }
            this.currentPassword = null;
            loadPasswordSettings();
            updateRows();
        }
    }

    private void updateRows() {
        this.rowCount = 0;
        int i = 0 + 1;
        this.rowCount = i;
        this.privacySectionRow = 0;
        int i2 = i + 1;
        this.rowCount = i2;
        this.blockedRow = i;
        int i3 = i2 + 1;
        this.rowCount = i3;
        this.phoneNumberRow = i2;
        int i4 = i3 + 1;
        this.rowCount = i4;
        this.lastSeenRow = i3;
        int i5 = i4 + 1;
        this.rowCount = i5;
        this.profilePhotoRow = i4;
        int i6 = i5 + 1;
        this.rowCount = i6;
        this.forwardsRow = i5;
        int i7 = i6 + 1;
        this.rowCount = i7;
        this.callsRow = i6;
        int i8 = i7 + 1;
        this.rowCount = i8;
        this.groupsRow = i7;
        int i9 = i8 + 1;
        this.rowCount = i9;
        this.groupsDetailRow = i8;
        int i10 = i9 + 1;
        this.rowCount = i10;
        this.securitySectionRow = i9;
        int i11 = i10 + 1;
        this.rowCount = i11;
        this.passcodeRow = i10;
        int i12 = i11 + 1;
        this.rowCount = i12;
        this.passwordRow = i11;
        int i13 = i12 + 1;
        this.rowCount = i13;
        this.sessionsRow = i12;
        this.rowCount = i13 + 1;
        this.sessionsDetailRow = i13;
        if (getMessagesController().autoarchiveAvailable || getUserConfig().isPremium()) {
            int i14 = this.rowCount;
            int i15 = i14 + 1;
            this.rowCount = i15;
            this.newChatsHeaderRow = i14;
            int i16 = i15 + 1;
            this.rowCount = i16;
            this.newChatsRow = i15;
            this.rowCount = i16 + 1;
            this.newChatsSectionRow = i16;
        } else {
            this.newChatsHeaderRow = -1;
            this.newChatsRow = -1;
            this.newChatsSectionRow = -1;
        }
        int i17 = this.rowCount;
        int i18 = i17 + 1;
        this.rowCount = i18;
        this.advancedSectionRow = i17;
        int i19 = i18 + 1;
        this.rowCount = i19;
        this.deleteAccountRow = i18;
        int i20 = i19 + 1;
        this.rowCount = i20;
        this.deleteAccountDetailRow = i19;
        this.rowCount = i20 + 1;
        this.botsSectionRow = i20;
        if (getUserConfig().hasSecureData) {
            int i21 = this.rowCount;
            this.rowCount = i21 + 1;
            this.passportRow = i21;
        } else {
            this.passportRow = -1;
        }
        int i22 = this.rowCount;
        int i23 = i22 + 1;
        this.rowCount = i23;
        this.paymentsClearRow = i22;
        int i24 = i23 + 1;
        this.rowCount = i24;
        this.webSessionsRow = i23;
        int i25 = i24 + 1;
        this.rowCount = i25;
        this.botsDetailRow = i24;
        int i26 = i25 + 1;
        this.rowCount = i26;
        this.contactsSectionRow = i25;
        int i27 = i26 + 1;
        this.rowCount = i27;
        this.contactsDeleteRow = i26;
        int i28 = i27 + 1;
        this.rowCount = i28;
        this.contactsSyncRow = i27;
        int i29 = i28 + 1;
        this.rowCount = i29;
        this.contactsSuggestRow = i28;
        int i30 = i29 + 1;
        this.rowCount = i30;
        this.contactsDetailRow = i29;
        int i31 = i30 + 1;
        this.rowCount = i31;
        this.secretSectionRow = i30;
        int i32 = i31 + 1;
        this.rowCount = i32;
        this.secretMapRow = i31;
        int i33 = i32 + 1;
        this.rowCount = i33;
        this.secretWebpageRow = i32;
        this.rowCount = i33 + 1;
        this.secretDetailRow = i33;
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private void loadPasswordSettings() {
        getConnectionsManager().sendRequest(new TLRPC$TL_account_getPassword(), new PrivacySettingsActivity$$ExternalSyntheticLambda11(this), 10);
    }

    public /* synthetic */ void lambda$loadPasswordSettings$17(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            AndroidUtilities.runOnUIThread(new PrivacySettingsActivity$$ExternalSyntheticLambda8(this, (TLRPC$TL_account_password) tLObject));
        }
    }

    public /* synthetic */ void lambda$loadPasswordSettings$16(TLRPC$TL_account_password tLRPC$TL_account_password) {
        this.currentPassword = tLRPC$TL_account_password;
        TwoStepVerificationActivity.initPasswordNewAlgo(tLRPC$TL_account_password);
        if (!getUserConfig().hasSecureData && tLRPC$TL_account_password.has_secure_values) {
            getUserConfig().hasSecureData = true;
            getUserConfig().saveConfig(false);
            updateRows();
            return;
        }
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter == null) {
            return;
        }
        listAdapter.notifyItemChanged(this.passwordRow);
    }

    public static String formatRulesString(AccountInstance accountInstance, int i) {
        ArrayList<TLRPC$PrivacyRule> privacyRules = accountInstance.getContactsController().getPrivacyRules(i);
        if (privacyRules.size() == 0) {
            if (i == 3) {
                return LocaleController.getString("P2PNobody", 2131627132);
            }
            return LocaleController.getString("LastSeenNobody", 2131626380);
        }
        char c = 65535;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < privacyRules.size(); i4++) {
            TLRPC$PrivacyRule tLRPC$PrivacyRule = privacyRules.get(i4);
            if (tLRPC$PrivacyRule instanceof TLRPC$TL_privacyValueAllowChatParticipants) {
                TLRPC$TL_privacyValueAllowChatParticipants tLRPC$TL_privacyValueAllowChatParticipants = (TLRPC$TL_privacyValueAllowChatParticipants) tLRPC$PrivacyRule;
                int size = tLRPC$TL_privacyValueAllowChatParticipants.chats.size();
                for (int i5 = 0; i5 < size; i5++) {
                    TLRPC$Chat chat = accountInstance.getMessagesController().getChat(tLRPC$TL_privacyValueAllowChatParticipants.chats.get(i5));
                    if (chat != null) {
                        i3 += chat.participants_count;
                    }
                }
            } else if (tLRPC$PrivacyRule instanceof TLRPC$TL_privacyValueDisallowChatParticipants) {
                TLRPC$TL_privacyValueDisallowChatParticipants tLRPC$TL_privacyValueDisallowChatParticipants = (TLRPC$TL_privacyValueDisallowChatParticipants) tLRPC$PrivacyRule;
                int size2 = tLRPC$TL_privacyValueDisallowChatParticipants.chats.size();
                for (int i6 = 0; i6 < size2; i6++) {
                    TLRPC$Chat chat2 = accountInstance.getMessagesController().getChat(tLRPC$TL_privacyValueDisallowChatParticipants.chats.get(i6));
                    if (chat2 != null) {
                        i2 += chat2.participants_count;
                    }
                }
            } else if (tLRPC$PrivacyRule instanceof TLRPC$TL_privacyValueAllowUsers) {
                i3 += ((TLRPC$TL_privacyValueAllowUsers) tLRPC$PrivacyRule).users.size();
            } else if (tLRPC$PrivacyRule instanceof TLRPC$TL_privacyValueDisallowUsers) {
                i2 += ((TLRPC$TL_privacyValueDisallowUsers) tLRPC$PrivacyRule).users.size();
            } else if (c == 65535) {
                if (tLRPC$PrivacyRule instanceof TLRPC$TL_privacyValueAllowAll) {
                    c = 0;
                } else {
                    c = tLRPC$PrivacyRule instanceof TLRPC$TL_privacyValueDisallowAll ? (char) 1 : (char) 2;
                }
            }
        }
        if (c == 0 || (c == 65535 && i2 > 0)) {
            return i == 3 ? i2 == 0 ? LocaleController.getString("P2PEverybody", 2131627130) : LocaleController.formatString("P2PEverybodyMinus", 2131627131, Integer.valueOf(i2)) : i2 == 0 ? LocaleController.getString("LastSeenEverybody", 2131626377) : LocaleController.formatString("LastSeenEverybodyMinus", 2131626378, Integer.valueOf(i2));
        } else if (c != 2 && (c != 65535 || i2 <= 0 || i3 <= 0)) {
            return (c == 1 || i3 > 0) ? i == 3 ? i3 == 0 ? LocaleController.getString("P2PNobody", 2131627132) : LocaleController.formatString("P2PNobodyPlus", 2131627133, Integer.valueOf(i3)) : i3 == 0 ? LocaleController.getString("LastSeenNobody", 2131626380) : LocaleController.formatString("LastSeenNobodyPlus", 2131626381, Integer.valueOf(i3)) : "unknown";
        } else if (i == 3) {
            if (i3 == 0 && i2 == 0) {
                return LocaleController.getString("P2PContacts", 2131627125);
            }
            return (i3 == 0 || i2 == 0) ? i2 != 0 ? LocaleController.formatString("P2PContactsMinus", 2131627126, Integer.valueOf(i2)) : LocaleController.formatString("P2PContactsPlus", 2131627128, Integer.valueOf(i3)) : LocaleController.formatString("P2PContactsMinusPlus", 2131627127, Integer.valueOf(i2), Integer.valueOf(i3));
        } else if (i3 == 0 && i2 == 0) {
            return LocaleController.getString("LastSeenContacts", 2131626371);
        } else {
            return (i3 == 0 || i2 == 0) ? i2 != 0 ? LocaleController.formatString("LastSeenContactsMinus", 2131626372, Integer.valueOf(i2)) : LocaleController.formatString("LastSeenContactsPlus", 2131626374, Integer.valueOf(i3)) : LocaleController.formatString("LastSeenContactsMinusPlus", 2131626373, Integer.valueOf(i2), Integer.valueOf(i3));
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            PrivacySettingsActivity.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == PrivacySettingsActivity.this.passcodeRow || adapterPosition == PrivacySettingsActivity.this.passwordRow || adapterPosition == PrivacySettingsActivity.this.blockedRow || adapterPosition == PrivacySettingsActivity.this.sessionsRow || adapterPosition == PrivacySettingsActivity.this.secretWebpageRow || adapterPosition == PrivacySettingsActivity.this.webSessionsRow || (adapterPosition == PrivacySettingsActivity.this.groupsRow && !PrivacySettingsActivity.this.getContactsController().getLoadingPrivicyInfo(1)) || ((adapterPosition == PrivacySettingsActivity.this.lastSeenRow && !PrivacySettingsActivity.this.getContactsController().getLoadingPrivicyInfo(0)) || ((adapterPosition == PrivacySettingsActivity.this.callsRow && !PrivacySettingsActivity.this.getContactsController().getLoadingPrivicyInfo(2)) || ((adapterPosition == PrivacySettingsActivity.this.profilePhotoRow && !PrivacySettingsActivity.this.getContactsController().getLoadingPrivicyInfo(4)) || ((adapterPosition == PrivacySettingsActivity.this.forwardsRow && !PrivacySettingsActivity.this.getContactsController().getLoadingPrivicyInfo(5)) || ((adapterPosition == PrivacySettingsActivity.this.phoneNumberRow && !PrivacySettingsActivity.this.getContactsController().getLoadingPrivicyInfo(6)) || ((adapterPosition == PrivacySettingsActivity.this.deleteAccountRow && !PrivacySettingsActivity.this.getContactsController().getLoadingDeleteInfo()) || ((adapterPosition == PrivacySettingsActivity.this.newChatsRow && !PrivacySettingsActivity.this.getContactsController().getLoadingGlobalSettings()) || adapterPosition == PrivacySettingsActivity.this.paymentsClearRow || adapterPosition == PrivacySettingsActivity.this.secretMapRow || adapterPosition == PrivacySettingsActivity.this.contactsSyncRow || adapterPosition == PrivacySettingsActivity.this.passportRow || adapterPosition == PrivacySettingsActivity.this.contactsDeleteRow || adapterPosition == PrivacySettingsActivity.this.contactsSuggestRow)))))));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return PrivacySettingsActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (i == 0) {
                view = new TextSettingsCell(this.mContext);
                view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else if (i == 1) {
                view = new TextInfoPrivacyCell(this.mContext);
            } else if (i == 2) {
                view = new HeaderCell(this.mContext);
                view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else {
                view = new TextCheckCell(this.mContext);
                view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String str;
            int itemViewType = viewHolder.getItemViewType();
            boolean z = false;
            boolean z2 = true;
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i != PrivacySettingsActivity.this.deleteAccountDetailRow) {
                        if (i != PrivacySettingsActivity.this.groupsDetailRow) {
                            if (i != PrivacySettingsActivity.this.sessionsDetailRow) {
                                if (i != PrivacySettingsActivity.this.secretDetailRow) {
                                    if (i != PrivacySettingsActivity.this.botsDetailRow) {
                                        if (i != PrivacySettingsActivity.this.contactsDetailRow) {
                                            if (i != PrivacySettingsActivity.this.newChatsSectionRow) {
                                                return;
                                            }
                                            textInfoPrivacyCell.setText(LocaleController.getString("ArchiveAndMuteInfo", 2131624394));
                                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                                            return;
                                        }
                                        textInfoPrivacyCell.setText(LocaleController.getString("SuggestContactsInfo", 2131628500));
                                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                                        return;
                                    }
                                    textInfoPrivacyCell.setText(LocaleController.getString("PrivacyBotsInfo", 2131627695));
                                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                                    return;
                                }
                                textInfoPrivacyCell.setText(LocaleController.getString("SecretWebPageInfo", 2131628156));
                                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                                return;
                            }
                            textInfoPrivacyCell.setText(LocaleController.getString("SessionsInfo", 2131628230));
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                            return;
                        }
                        textInfoPrivacyCell.setText(LocaleController.getString("GroupsAndChannelsHelp", 2131626114));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                        return;
                    }
                    textInfoPrivacyCell.setText(LocaleController.getString("DeleteAccountHelp", 2131625369));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                    return;
                } else if (itemViewType != 2) {
                    if (itemViewType != 3) {
                        return;
                    }
                    TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                    if (i != PrivacySettingsActivity.this.secretWebpageRow) {
                        if (i == PrivacySettingsActivity.this.contactsSyncRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("SyncContacts", 2131628525), PrivacySettingsActivity.this.newSync, true);
                            return;
                        } else if (i == PrivacySettingsActivity.this.contactsSuggestRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("SuggestContacts", 2131628498), PrivacySettingsActivity.this.newSuggest, false);
                            return;
                        } else if (i != PrivacySettingsActivity.this.newChatsRow) {
                            return;
                        } else {
                            textCheckCell.setTextAndCheck(LocaleController.getString("ArchiveAndMute", 2131624393), PrivacySettingsActivity.this.archiveChats, false);
                            return;
                        }
                    }
                    String string = LocaleController.getString("SecretWebPage", 2131628155);
                    if (PrivacySettingsActivity.this.getMessagesController().secretWebpagePreview != 1) {
                        z2 = false;
                    }
                    textCheckCell.setTextAndCheck(string, z2, false);
                    return;
                } else {
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i != PrivacySettingsActivity.this.privacySectionRow) {
                        if (i != PrivacySettingsActivity.this.securitySectionRow) {
                            if (i != PrivacySettingsActivity.this.advancedSectionRow) {
                                if (i != PrivacySettingsActivity.this.secretSectionRow) {
                                    if (i != PrivacySettingsActivity.this.botsSectionRow) {
                                        if (i != PrivacySettingsActivity.this.contactsSectionRow) {
                                            if (i != PrivacySettingsActivity.this.newChatsHeaderRow) {
                                                return;
                                            }
                                            headerCell.setText(LocaleController.getString("NewChatsFromNonContacts", 2131626773));
                                            return;
                                        }
                                        headerCell.setText(LocaleController.getString("Contacts", 2131625242));
                                        return;
                                    }
                                    headerCell.setText(LocaleController.getString("PrivacyBots", 2131627694));
                                    return;
                                }
                                headerCell.setText(LocaleController.getString("SecretChat", 2131628144));
                                return;
                            }
                            headerCell.setText(LocaleController.getString("DeleteMyAccount", 2131625426));
                            return;
                        }
                        headerCell.setText(LocaleController.getString("SecurityTitle", 2131628157));
                        return;
                    }
                    headerCell.setText(LocaleController.getString("PrivacyTitle", 2131627737));
                    return;
                }
            }
            String str2 = null;
            int i2 = 16;
            boolean z3 = viewHolder.itemView.getTag() != null && ((Integer) viewHolder.itemView.getTag()).intValue() == i;
            viewHolder.itemView.setTag(Integer.valueOf(i));
            TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
            if (i != PrivacySettingsActivity.this.blockedRow) {
                if (i != PrivacySettingsActivity.this.sessionsRow) {
                    if (i != PrivacySettingsActivity.this.webSessionsRow) {
                        if (i == PrivacySettingsActivity.this.passwordRow) {
                            if (PrivacySettingsActivity.this.currentPassword == null) {
                                z = true;
                            } else if (PrivacySettingsActivity.this.currentPassword.has_password) {
                                str2 = LocaleController.getString("PasswordOn", 2131627362);
                            } else {
                                str2 = LocaleController.getString("PasswordOff", 2131627361);
                            }
                            textSettingsCell.setTextAndValue(LocaleController.getString("TwoStepVerification", 2131628701), str2, true);
                        } else if (i != PrivacySettingsActivity.this.passcodeRow) {
                            if (i == PrivacySettingsActivity.this.phoneNumberRow) {
                                if (PrivacySettingsActivity.this.getContactsController().getLoadingPrivicyInfo(6)) {
                                    z = true;
                                    i2 = 30;
                                } else {
                                    str2 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.getAccountInstance(), 6);
                                }
                                textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyPhone", 2131627721), str2, true);
                            } else if (i == PrivacySettingsActivity.this.lastSeenRow) {
                                if (PrivacySettingsActivity.this.getContactsController().getLoadingPrivicyInfo(0)) {
                                    z = true;
                                    i2 = 30;
                                } else {
                                    str2 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.getAccountInstance(), 0);
                                }
                                textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyLastSeen", 2131627710), str2, true);
                            } else {
                                if (i == PrivacySettingsActivity.this.groupsRow) {
                                    if (PrivacySettingsActivity.this.getContactsController().getLoadingPrivicyInfo(1)) {
                                        i2 = 30;
                                    } else {
                                        str2 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.getAccountInstance(), 1);
                                        z2 = false;
                                    }
                                    textSettingsCell.setTextAndValue(LocaleController.getString("GroupsAndChannels", 2131626113), str2, false);
                                } else if (i == PrivacySettingsActivity.this.callsRow) {
                                    if (PrivacySettingsActivity.this.getContactsController().getLoadingPrivicyInfo(2)) {
                                        z = true;
                                        i2 = 30;
                                    } else {
                                        str2 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.getAccountInstance(), 2);
                                    }
                                    textSettingsCell.setTextAndValue(LocaleController.getString("Calls", 2131624809), str2, true);
                                } else if (i == PrivacySettingsActivity.this.profilePhotoRow) {
                                    if (PrivacySettingsActivity.this.getContactsController().getLoadingPrivicyInfo(4)) {
                                        z = true;
                                        i2 = 30;
                                    } else {
                                        str2 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.getAccountInstance(), 4);
                                    }
                                    textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyProfilePhoto", 2131627731), str2, true);
                                } else if (i == PrivacySettingsActivity.this.forwardsRow) {
                                    if (PrivacySettingsActivity.this.getContactsController().getLoadingPrivicyInfo(5)) {
                                        z = true;
                                        i2 = 30;
                                    } else {
                                        str2 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.getAccountInstance(), 5);
                                    }
                                    textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyForwards", 2131627702), str2, true);
                                } else if (i != PrivacySettingsActivity.this.passportRow) {
                                    if (i == PrivacySettingsActivity.this.deleteAccountRow) {
                                        if (!PrivacySettingsActivity.this.getContactsController().getLoadingDeleteInfo()) {
                                            int deleteAccountTTL = PrivacySettingsActivity.this.getContactsController().getDeleteAccountTTL();
                                            if (deleteAccountTTL <= 182) {
                                                str2 = LocaleController.formatPluralString("Months", deleteAccountTTL / 30, new Object[0]);
                                            } else if (deleteAccountTTL == 365) {
                                                str2 = LocaleController.formatPluralString("Years", deleteAccountTTL / 365, new Object[0]);
                                            } else {
                                                str2 = LocaleController.formatPluralString("Days", deleteAccountTTL, new Object[0]);
                                            }
                                            z2 = false;
                                        }
                                        textSettingsCell.setTextAndValue(LocaleController.getString("DeleteAccountIfAwayFor3", 2131625371), str2, false);
                                    } else if (i != PrivacySettingsActivity.this.paymentsClearRow) {
                                        if (i != PrivacySettingsActivity.this.secretMapRow) {
                                            if (i == PrivacySettingsActivity.this.contactsDeleteRow) {
                                                textSettingsCell.setText(LocaleController.getString("SyncContactsDelete", 2131628527), true);
                                            }
                                        } else {
                                            int i3 = SharedConfig.mapPreviewType;
                                            if (i3 == 0) {
                                                str = LocaleController.getString("MapPreviewProviderTelegram", 2131626537);
                                            } else if (i3 == 1) {
                                                str = LocaleController.getString("MapPreviewProviderGoogle", 2131626535);
                                            } else if (i3 == 2) {
                                                str = LocaleController.getString("MapPreviewProviderNobody", 2131626536);
                                            } else {
                                                str = LocaleController.getString("MapPreviewProviderYandex", 2131626539);
                                            }
                                            textSettingsCell.setTextAndValue(LocaleController.getString("MapPreviewProvider", 2131626534), str, true);
                                        }
                                    } else {
                                        textSettingsCell.setText(LocaleController.getString("PrivacyPaymentsClear", 2131627714), true);
                                    }
                                } else {
                                    textSettingsCell.setText(LocaleController.getString("TelegramPassport", 2131628566), true);
                                }
                                z = z2;
                            }
                        } else {
                            textSettingsCell.setText(LocaleController.getString("Passcode", 2131627162), true);
                        }
                    } else {
                        textSettingsCell.setText(LocaleController.getString("WebSessionsTitle", 2131629225), false);
                    }
                } else {
                    textSettingsCell.setText(LocaleController.getString("SessionsTitle", 2131628233), false);
                }
            } else {
                int i4 = PrivacySettingsActivity.this.getMessagesController().totalBlockedCount;
                if (i4 == 0) {
                    textSettingsCell.setTextAndValue(LocaleController.getString("BlockedUsers", 2131624690), LocaleController.getString("BlockedEmpty", 2131624689), true);
                } else if (i4 > 0) {
                    textSettingsCell.setTextAndValue(LocaleController.getString("BlockedUsers", 2131624690), String.format("%d", Integer.valueOf(i4)), true);
                } else {
                    textSettingsCell.setText(LocaleController.getString("BlockedUsers", 2131624690), true);
                    z = true;
                }
            }
            textSettingsCell.setDrawLoading(z, i2, z3);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == PrivacySettingsActivity.this.passportRow || i == PrivacySettingsActivity.this.lastSeenRow || i == PrivacySettingsActivity.this.phoneNumberRow || i == PrivacySettingsActivity.this.blockedRow || i == PrivacySettingsActivity.this.deleteAccountRow || i == PrivacySettingsActivity.this.sessionsRow || i == PrivacySettingsActivity.this.webSessionsRow || i == PrivacySettingsActivity.this.passwordRow || i == PrivacySettingsActivity.this.passcodeRow || i == PrivacySettingsActivity.this.groupsRow || i == PrivacySettingsActivity.this.paymentsClearRow || i == PrivacySettingsActivity.this.secretMapRow || i == PrivacySettingsActivity.this.contactsDeleteRow) {
                return 0;
            }
            if (i == PrivacySettingsActivity.this.deleteAccountDetailRow || i == PrivacySettingsActivity.this.groupsDetailRow || i == PrivacySettingsActivity.this.sessionsDetailRow || i == PrivacySettingsActivity.this.secretDetailRow || i == PrivacySettingsActivity.this.botsDetailRow || i == PrivacySettingsActivity.this.contactsDetailRow || i == PrivacySettingsActivity.this.newChatsSectionRow) {
                return 1;
            }
            if (i == PrivacySettingsActivity.this.securitySectionRow || i == PrivacySettingsActivity.this.advancedSectionRow || i == PrivacySettingsActivity.this.privacySectionRow || i == PrivacySettingsActivity.this.secretSectionRow || i == PrivacySettingsActivity.this.botsSectionRow || i == PrivacySettingsActivity.this.contactsSectionRow || i == PrivacySettingsActivity.this.newChatsHeaderRow) {
                return 2;
            }
            return (i == PrivacySettingsActivity.this.secretWebpageRow || i == PrivacySettingsActivity.this.contactsSyncRow || i == PrivacySettingsActivity.this.contactsSuggestRow || i == PrivacySettingsActivity.this.newChatsRow) ? 3 : 0;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, TextCheckCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteValueText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        return arrayList;
    }
}
