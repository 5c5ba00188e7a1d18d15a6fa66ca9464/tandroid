package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.RadioCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckBoxCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.UserCell2;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.ChatAvatarContainer;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.NotificationsSettingsActivity;
/* loaded from: classes3.dex */
public class ProfileNotificationsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private ListAdapter adapter;
    private boolean addingException;
    private AnimatorSet animatorSet;
    ChatAvatarContainer avatarContainer;
    private int avatarRow;
    private int avatarSectionRow;
    private int callsRow;
    private int callsVibrateRow;
    private int colorRow;
    private int customResetRow;
    private int customResetShadowRow;
    private ProfileNotificationsActivityDelegate delegate;
    private long dialogId;
    private int enableRow;
    private int generalRow;
    private int ledInfoRow;
    private int ledRow;
    private RecyclerListView listView;
    private boolean needReset;
    private boolean notificationsEnabled;
    private int popupDisabledRow;
    private int popupEnabledRow;
    private int popupInfoRow;
    private int popupRow;
    private int previewRow;
    private int priorityInfoRow;
    private int priorityRow;
    private Theme.ResourcesProvider resourcesProvider;
    private int ringtoneInfoRow;
    private int ringtoneRow;
    private int rowCount;
    private int smartRow;
    private int soundRow;
    private int vibrateRow;

    /* loaded from: classes3.dex */
    public interface ProfileNotificationsActivityDelegate {

        /* renamed from: org.telegram.ui.ProfileNotificationsActivity$ProfileNotificationsActivityDelegate$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
            public static void $default$didRemoveException(ProfileNotificationsActivityDelegate profileNotificationsActivityDelegate, long j) {
            }
        }

        void didCreateNewException(NotificationsSettingsActivity.NotificationException notificationException);

        void didRemoveException(long j);
    }

    public ProfileNotificationsActivity(Bundle bundle) {
        this(bundle, null);
    }

    public ProfileNotificationsActivity(Bundle bundle, Theme.ResourcesProvider resourcesProvider) {
        super(bundle);
        this.resourcesProvider = resourcesProvider;
        this.dialogId = bundle.getLong("dialog_id");
        this.addingException = bundle.getBoolean("exception", false);
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00f0  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0110  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x011d  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0131  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0136  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onFragmentCreate() {
        boolean z;
        this.rowCount = 0;
        boolean z2 = this.addingException;
        if (z2) {
            int i = 0 + 1;
            this.rowCount = i;
            this.avatarRow = 0;
            this.rowCount = i + 1;
            this.avatarSectionRow = i;
        } else {
            this.avatarRow = -1;
            this.avatarSectionRow = -1;
        }
        int i2 = this.rowCount;
        int i3 = i2 + 1;
        this.rowCount = i3;
        this.generalRow = i2;
        if (z2) {
            this.rowCount = i3 + 1;
            this.enableRow = i3;
        } else {
            this.enableRow = -1;
        }
        if (!DialogObject.isEncryptedDialog(this.dialogId)) {
            int i4 = this.rowCount;
            this.rowCount = i4 + 1;
            this.previewRow = i4;
        } else {
            this.previewRow = -1;
        }
        int i5 = this.rowCount;
        int i6 = i5 + 1;
        this.rowCount = i6;
        this.soundRow = i5;
        this.rowCount = i6 + 1;
        this.vibrateRow = i6;
        if (DialogObject.isChatDialog(this.dialogId)) {
            int i7 = this.rowCount;
            this.rowCount = i7 + 1;
            this.smartRow = i7;
        } else {
            this.smartRow = -1;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            int i8 = this.rowCount;
            this.rowCount = i8 + 1;
            this.priorityRow = i8;
        } else {
            this.priorityRow = -1;
        }
        int i9 = this.rowCount;
        this.rowCount = i9 + 1;
        this.priorityInfoRow = i9;
        if (DialogObject.isChatDialog(this.dialogId)) {
            TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                z = true;
                if (DialogObject.isEncryptedDialog(this.dialogId) && !z) {
                    int i10 = this.rowCount;
                    int i11 = i10 + 1;
                    this.rowCount = i11;
                    this.popupRow = i10;
                    int i12 = i11 + 1;
                    this.rowCount = i12;
                    this.popupEnabledRow = i11;
                    int i13 = i12 + 1;
                    this.rowCount = i13;
                    this.popupDisabledRow = i12;
                    this.rowCount = i13 + 1;
                    this.popupInfoRow = i13;
                } else {
                    this.popupRow = -1;
                    this.popupEnabledRow = -1;
                    this.popupDisabledRow = -1;
                    this.popupInfoRow = -1;
                }
                if (!DialogObject.isUserDialog(this.dialogId)) {
                    int i14 = this.rowCount;
                    int i15 = i14 + 1;
                    this.rowCount = i15;
                    this.callsRow = i14;
                    int i16 = i15 + 1;
                    this.rowCount = i16;
                    this.callsVibrateRow = i15;
                    int i17 = i16 + 1;
                    this.rowCount = i17;
                    this.ringtoneRow = i16;
                    this.rowCount = i17 + 1;
                    this.ringtoneInfoRow = i17;
                } else {
                    this.callsRow = -1;
                    this.callsVibrateRow = -1;
                    this.ringtoneRow = -1;
                    this.ringtoneInfoRow = -1;
                }
                int i18 = this.rowCount;
                int i19 = i18 + 1;
                this.rowCount = i19;
                this.ledRow = i18;
                int i20 = i19 + 1;
                this.rowCount = i20;
                this.colorRow = i19;
                int i21 = i20 + 1;
                this.rowCount = i21;
                this.ledInfoRow = i20;
                if (this.addingException) {
                    int i22 = i21 + 1;
                    this.rowCount = i22;
                    this.customResetRow = i21;
                    this.rowCount = i22 + 1;
                    this.customResetShadowRow = i22;
                } else {
                    this.customResetRow = -1;
                    this.customResetShadowRow = -1;
                }
                boolean isGlobalNotificationsEnabled = NotificationsController.getInstance(this.currentAccount).isGlobalNotificationsEnabled(this.dialogId);
                if (!this.addingException) {
                    this.notificationsEnabled = !isGlobalNotificationsEnabled;
                } else {
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                    boolean contains = notificationsSettings.contains("notify2_" + this.dialogId);
                    int i23 = notificationsSettings.getInt("notify2_" + this.dialogId, 0);
                    if (i23 == 0) {
                        if (contains) {
                            this.notificationsEnabled = true;
                        } else {
                            this.notificationsEnabled = NotificationsController.getInstance(this.currentAccount).isGlobalNotificationsEnabled(this.dialogId);
                        }
                    } else if (i23 == 1) {
                        this.notificationsEnabled = true;
                    } else if (i23 == 2) {
                        this.notificationsEnabled = false;
                    } else {
                        this.notificationsEnabled = false;
                    }
                }
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
                return super.onFragmentCreate();
            }
        }
        z = false;
        if (DialogObject.isEncryptedDialog(this.dialogId)) {
        }
        this.popupRow = -1;
        this.popupEnabledRow = -1;
        this.popupDisabledRow = -1;
        this.popupInfoRow = -1;
        if (!DialogObject.isUserDialog(this.dialogId)) {
        }
        int i182 = this.rowCount;
        int i192 = i182 + 1;
        this.rowCount = i192;
        this.ledRow = i182;
        int i202 = i192 + 1;
        this.rowCount = i202;
        this.colorRow = i192;
        int i212 = i202 + 1;
        this.rowCount = i212;
        this.ledInfoRow = i202;
        if (this.addingException) {
        }
        boolean isGlobalNotificationsEnabled2 = NotificationsController.getInstance(this.currentAccount).isGlobalNotificationsEnabled(this.dialogId);
        if (!this.addingException) {
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (!this.needReset) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            edit.putBoolean("custom_" + this.dialogId, true).apply();
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Theme.ResourcesProvider getResourceProvider() {
        return this.resourcesProvider;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setItemsBackgroundColor(Theme.getColor("avatar_actionBarSelectorBlue", this.resourcesProvider), false);
        this.actionBar.setItemsColor(Theme.getColor("actionBarDefaultIcon", this.resourcesProvider), false);
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        ChatAvatarContainer chatAvatarContainer = new ChatAvatarContainer(context, null, false, this.resourcesProvider);
        this.avatarContainer = chatAvatarContainer;
        chatAvatarContainer.setOccupyStatusBar(!AndroidUtilities.isTablet());
        this.actionBar.addView(this.avatarContainer, 0, LayoutHelper.createFrame(-2, -1.0f, 51, !this.inPreviewMode ? 56.0f : 0.0f, 0.0f, 40.0f, 0.0f));
        this.actionBar.setAllowOverlayTitle(false);
        if (this.dialogId < 0) {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
            this.avatarContainer.setChatAvatar(chat);
            this.avatarContainer.setTitle(chat.title);
        } else {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.dialogId));
            if (user != null) {
                this.avatarContainer.setUserAvatar(user);
                this.avatarContainer.setTitle(ContactsController.formatName(user.first_name, user.last_name));
            }
        }
        if (this.addingException) {
            this.avatarContainer.setSubtitle(LocaleController.getString("NotificationsNewException", 2131627048));
            this.actionBar.createMenu().addItem(1, LocaleController.getString("Done", 2131625525).toUpperCase());
        } else {
            this.avatarContainer.setSubtitle(LocaleController.getString("CustomNotifications", 2131625301));
        }
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundGray", this.resourcesProvider));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        frameLayout2.addView(recyclerListView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView2 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.adapter = listAdapter;
        recyclerListView2.setAdapter(listAdapter);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        this.listView.setLayoutManager(new AnonymousClass2(this, context));
        this.listView.setOnItemClickListener(new ProfileNotificationsActivity$$ExternalSyntheticLambda7(this, context));
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.ProfileNotificationsActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            ProfileNotificationsActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (!ProfileNotificationsActivity.this.addingException && ProfileNotificationsActivity.this.notificationsEnabled) {
                    SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).edit();
                    edit.putInt("notify2_" + ProfileNotificationsActivity.this.dialogId, 0).apply();
                }
            } else if (i == 1) {
                SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) ProfileNotificationsActivity.this).currentAccount);
                SharedPreferences.Editor edit2 = notificationsSettings.edit();
                edit2.putBoolean("custom_" + ProfileNotificationsActivity.this.dialogId, true);
                TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).dialogs_dict.get(ProfileNotificationsActivity.this.dialogId);
                if (ProfileNotificationsActivity.this.notificationsEnabled) {
                    edit2.putInt("notify2_" + ProfileNotificationsActivity.this.dialogId, 0);
                    MessagesStorage.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).setDialogFlags(ProfileNotificationsActivity.this.dialogId, 0L);
                    if (tLRPC$Dialog != null) {
                        tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
                    }
                } else {
                    edit2.putInt("notify2_" + ProfileNotificationsActivity.this.dialogId, 2);
                    NotificationsController.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).removeNotificationsForDialog(ProfileNotificationsActivity.this.dialogId);
                    MessagesStorage.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).setDialogFlags(ProfileNotificationsActivity.this.dialogId, 1L);
                    if (tLRPC$Dialog != null) {
                        TLRPC$TL_peerNotifySettings tLRPC$TL_peerNotifySettings = new TLRPC$TL_peerNotifySettings();
                        tLRPC$Dialog.notify_settings = tLRPC$TL_peerNotifySettings;
                        tLRPC$TL_peerNotifySettings.mute_until = Integer.MAX_VALUE;
                    }
                }
                edit2.apply();
                NotificationsController.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).updateServerNotificationsSettings(ProfileNotificationsActivity.this.dialogId);
                if (ProfileNotificationsActivity.this.delegate != null) {
                    NotificationsSettingsActivity.NotificationException notificationException = new NotificationsSettingsActivity.NotificationException();
                    notificationException.did = ProfileNotificationsActivity.this.dialogId;
                    notificationException.hasCustom = true;
                    int i2 = notificationsSettings.getInt("notify2_" + ProfileNotificationsActivity.this.dialogId, 0);
                    notificationException.notify = i2;
                    if (i2 != 0) {
                        notificationException.muteUntil = notificationsSettings.getInt("notifyuntil_" + ProfileNotificationsActivity.this.dialogId, 0);
                    }
                    ProfileNotificationsActivity.this.delegate.didCreateNewException(notificationException);
                }
            }
            ProfileNotificationsActivity.this.finishFragment();
        }
    }

    /* renamed from: org.telegram.ui.ProfileNotificationsActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends LinearLayoutManager {
        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        AnonymousClass2(ProfileNotificationsActivity profileNotificationsActivity, Context context) {
            super(context);
        }
    }

    public /* synthetic */ void lambda$createView$6(Context context, View view, int i) {
        if (!view.isEnabled()) {
            return;
        }
        Parcelable parcelable = null;
        if (i == this.customResetRow) {
            AlertDialog create = new AlertDialog.Builder(context, this.resourcesProvider).setTitle(LocaleController.getString(2131627996)).setMessage(LocaleController.getString(2131627995)).setPositiveButton(LocaleController.getString(2131627980), new ProfileNotificationsActivity$$ExternalSyntheticLambda0(this)).setNegativeButton(LocaleController.getString(2131624819), null).create();
            showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView == null) {
                return;
            }
            textView.setTextColor(Theme.getColor("dialogTextRed2"));
        } else if (i == this.soundRow) {
            Bundle bundle = new Bundle();
            bundle.putLong("dialog_id", this.dialogId);
            presentFragment(new NotificationsSoundActivity(bundle, this.resourcesProvider));
        } else if (i == this.ringtoneRow) {
            try {
                Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
                intent.putExtra("android.intent.extra.ringtone.TYPE", 1);
                intent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                intent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
                intent.putExtra("android.intent.extra.ringtone.DEFAULT_URI", RingtoneManager.getDefaultUri(1));
                SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                Uri uri = Settings.System.DEFAULT_NOTIFICATION_URI;
                String path = uri != null ? uri.getPath() : null;
                String string = notificationsSettings.getString("ringtone_path_" + this.dialogId, path);
                if (string != null && !string.equals("NoSound")) {
                    parcelable = string.equals(path) ? uri : Uri.parse(string);
                }
                intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", parcelable);
                startActivityForResult(intent, 13);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else if (i == this.vibrateRow) {
            showDialog(AlertsCreator.createVibrationSelectDialog(getParentActivity(), this.dialogId, false, false, new ProfileNotificationsActivity$$ExternalSyntheticLambda1(this), this.resourcesProvider));
        } else if (i == this.enableRow) {
            TextCheckCell textCheckCell = (TextCheckCell) view;
            boolean z = !textCheckCell.isChecked();
            this.notificationsEnabled = z;
            textCheckCell.setChecked(z);
            checkRowsEnabled();
        } else if (i == this.previewRow) {
            TextCheckCell textCheckCell2 = (TextCheckCell) view;
            MessagesController.getNotificationsSettings(this.currentAccount).edit().putBoolean("content_preview_" + this.dialogId, !textCheckCell2.isChecked()).apply();
            textCheckCell2.setChecked(textCheckCell2.isChecked() ^ true);
        } else if (i == this.callsVibrateRow) {
            showDialog(AlertsCreator.createVibrationSelectDialog(getParentActivity(), this.dialogId, "calls_vibrate_" + this.dialogId, new ProfileNotificationsActivity$$ExternalSyntheticLambda4(this), this.resourcesProvider));
        } else if (i == this.priorityRow) {
            showDialog(AlertsCreator.createPrioritySelectDialog(getParentActivity(), this.dialogId, -1, new ProfileNotificationsActivity$$ExternalSyntheticLambda3(this), this.resourcesProvider));
        } else {
            int i2 = 2;
            if (i == this.smartRow) {
                if (getParentActivity() == null) {
                    return;
                }
                SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(this.currentAccount);
                int i3 = notificationsSettings2.getInt("smart_max_count_" + this.dialogId, 2);
                int i4 = notificationsSettings2.getInt("smart_delay_" + this.dialogId, 180);
                if (i3 != 0) {
                    i2 = i3;
                }
                AlertsCreator.createSoundFrequencyPickerDialog(getParentActivity(), i2, i4, new ProfileNotificationsActivity$$ExternalSyntheticLambda6(this), this.resourcesProvider);
            } else if (i == this.colorRow) {
                if (getParentActivity() == null) {
                    return;
                }
                showDialog(AlertsCreator.createColorSelectDialog(getParentActivity(), this.dialogId, -1, new ProfileNotificationsActivity$$ExternalSyntheticLambda2(this), this.resourcesProvider));
            } else if (i == this.popupEnabledRow) {
                MessagesController.getNotificationsSettings(this.currentAccount).edit().putInt("popup_" + this.dialogId, 1).apply();
                ((RadioCell) view).setChecked(true, true);
                View findViewWithTag = this.listView.findViewWithTag(2);
                if (findViewWithTag == null) {
                    return;
                }
                ((RadioCell) findViewWithTag).setChecked(false, true);
            } else if (i != this.popupDisabledRow) {
            } else {
                MessagesController.getNotificationsSettings(this.currentAccount).edit().putInt("popup_" + this.dialogId, 2).apply();
                ((RadioCell) view).setChecked(true, true);
                View findViewWithTag2 = this.listView.findViewWithTag(1);
                if (findViewWithTag2 == null) {
                    return;
                }
                ((RadioCell) findViewWithTag2).setChecked(false, true);
            }
        }
    }

    public /* synthetic */ void lambda$createView$0(DialogInterface dialogInterface, int i) {
        this.needReset = true;
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        SharedPreferences.Editor putBoolean = edit.putBoolean("custom_" + this.dialogId, false);
        putBoolean.remove("notify2_" + this.dialogId).apply();
        finishFragment();
        ProfileNotificationsActivityDelegate profileNotificationsActivityDelegate = this.delegate;
        if (profileNotificationsActivityDelegate != null) {
            profileNotificationsActivityDelegate.didRemoveException(this.dialogId);
        }
    }

    public /* synthetic */ void lambda$createView$1() {
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(this.vibrateRow);
        }
    }

    public /* synthetic */ void lambda$createView$2() {
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(this.callsVibrateRow);
        }
    }

    public /* synthetic */ void lambda$createView$3() {
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(this.priorityRow);
        }
    }

    public /* synthetic */ void lambda$createView$4(int i, int i2) {
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        SharedPreferences.Editor putInt = edit.putInt("smart_max_count_" + this.dialogId, i);
        putInt.putInt("smart_delay_" + this.dialogId, i2).apply();
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(this.smartRow);
        }
    }

    public /* synthetic */ void lambda$createView$5() {
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(this.colorRow);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        Ringtone ringtone;
        if (i2 != -1 || intent == null) {
            return;
        }
        Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
        String str = null;
        if (uri != null && (ringtone = RingtoneManager.getRingtone(ApplicationLoader.applicationContext, uri)) != null) {
            if (i == 13) {
                if (uri.equals(Settings.System.DEFAULT_RINGTONE_URI)) {
                    str = LocaleController.getString("DefaultRingtone", 2131625367);
                } else {
                    str = ringtone.getTitle(getParentActivity());
                }
            } else if (uri.equals(Settings.System.DEFAULT_NOTIFICATION_URI)) {
                str = LocaleController.getString("SoundDefault", 2131628387);
            } else {
                str = ringtone.getTitle(getParentActivity());
            }
            ringtone.stop();
        }
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        if (i == 12) {
            if (str != null) {
                edit.putString("sound_" + this.dialogId, str);
                edit.putString("sound_path_" + this.dialogId, uri.toString());
            } else {
                edit.putString("sound_" + this.dialogId, "NoSound");
                edit.putString("sound_path_" + this.dialogId, "NoSound");
            }
            getNotificationsController().deleteNotificationChannel(this.dialogId);
        } else if (i == 13) {
            if (str != null) {
                edit.putString("ringtone_" + this.dialogId, str);
                edit.putString("ringtone_path_" + this.dialogId, uri.toString());
            } else {
                edit.putString("ringtone_" + this.dialogId, "NoSound");
                edit.putString("ringtone_path_" + this.dialogId, "NoSound");
            }
        }
        edit.apply();
        ListAdapter listAdapter = this.adapter;
        if (listAdapter == null) {
            return;
        }
        listAdapter.notifyItemChanged(i == 13 ? this.ringtoneRow : this.soundRow);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    @SuppressLint({"NotifyDataSetChanged"})
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.notificationsSettingsUpdated) {
            try {
                this.adapter.notifyDataSetChanged();
            } catch (Exception unused) {
            }
        }
    }

    public void setDelegate(ProfileNotificationsActivityDelegate profileNotificationsActivityDelegate) {
        this.delegate = profileNotificationsActivityDelegate;
    }

    private void checkRowsEnabled() {
        int childCount = this.listView.getChildCount();
        ArrayList<Animator> arrayList = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.getChildViewHolder(this.listView.getChildAt(i));
            int itemViewType = holder.getItemViewType();
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != this.enableRow && adapterPosition != this.customResetRow) {
                if (itemViewType == 0) {
                    ((HeaderCell) holder.itemView).setEnabled(this.notificationsEnabled, arrayList);
                } else if (itemViewType == 1) {
                    ((TextSettingsCell) holder.itemView).setEnabled(this.notificationsEnabled, arrayList);
                } else if (itemViewType == 2) {
                    ((TextInfoPrivacyCell) holder.itemView).setEnabled(this.notificationsEnabled, arrayList);
                } else if (itemViewType == 3) {
                    ((TextColorCell) holder.itemView).setEnabled(this.notificationsEnabled, arrayList);
                } else if (itemViewType == 4) {
                    ((RadioCell) holder.itemView).setEnabled(this.notificationsEnabled, arrayList);
                } else if (itemViewType == 7 && adapterPosition == this.previewRow) {
                    ((TextCheckCell) holder.itemView).setEnabled(this.notificationsEnabled, arrayList);
                }
            }
        }
        if (!arrayList.isEmpty()) {
            AnimatorSet animatorSet = this.animatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animatorSet = animatorSet2;
            animatorSet2.playTogether(arrayList);
            this.animatorSet.addListener(new AnonymousClass3());
            this.animatorSet.setDuration(150L);
            this.animatorSet.start();
        }
    }

    /* renamed from: org.telegram.ui.ProfileNotificationsActivity$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        AnonymousClass3() {
            ProfileNotificationsActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(ProfileNotificationsActivity.this.animatorSet)) {
                ProfileNotificationsActivity.this.animatorSet = null;
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context context;

        public ListAdapter(Context context) {
            ProfileNotificationsActivity.this = r1;
            this.context = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ProfileNotificationsActivity.this.rowCount;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getAdapterPosition() == ProfileNotificationsActivity.this.previewRow) {
                return ProfileNotificationsActivity.this.notificationsEnabled;
            }
            if (viewHolder.getAdapterPosition() == ProfileNotificationsActivity.this.customResetRow) {
                return true;
            }
            switch (viewHolder.getItemViewType()) {
                case 0:
                case 2:
                case 5:
                case 6:
                    return false;
                case 1:
                case 3:
                case 4:
                    return ProfileNotificationsActivity.this.notificationsEnabled;
                default:
                    return true;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            View view2;
            switch (i) {
                case 0:
                    view2 = new HeaderCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    view2.setBackgroundColor(ProfileNotificationsActivity.this.getThemedColor("windowBackgroundWhite"));
                    view = view2;
                    break;
                case 1:
                    view2 = new TextSettingsCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    view2.setBackgroundColor(ProfileNotificationsActivity.this.getThemedColor("windowBackgroundWhite"));
                    view = view2;
                    break;
                case 2:
                    view = new TextInfoPrivacyCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    break;
                case 3:
                    view2 = new TextColorCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    view2.setBackgroundColor(ProfileNotificationsActivity.this.getThemedColor("windowBackgroundWhite"));
                    view = view2;
                    break;
                case 4:
                    view2 = new RadioCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    view2.setBackgroundColor(ProfileNotificationsActivity.this.getThemedColor("windowBackgroundWhite"));
                    view = view2;
                    break;
                case 5:
                    view2 = new UserCell2(this.context, 4, 0, ProfileNotificationsActivity.this.resourcesProvider);
                    view2.setBackgroundColor(ProfileNotificationsActivity.this.getThemedColor("windowBackgroundWhite"));
                    view = view2;
                    break;
                case 6:
                    view = new ShadowSectionCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    break;
                default:
                    view2 = new TextCheckCell(this.context, ProfileNotificationsActivity.this.resourcesProvider);
                    view2.setBackgroundColor(ProfileNotificationsActivity.this.getThemedColor("windowBackgroundWhite"));
                    view = view2;
                    break;
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i != ProfileNotificationsActivity.this.generalRow) {
                    if (i != ProfileNotificationsActivity.this.popupRow) {
                        if (i != ProfileNotificationsActivity.this.ledRow) {
                            if (i != ProfileNotificationsActivity.this.callsRow) {
                                return;
                            }
                            headerCell.setText(LocaleController.getString("VoipNotificationSettings", 2131629157));
                            return;
                        }
                        headerCell.setText(LocaleController.getString("NotificationsLed", 2131627042));
                        return;
                    }
                    headerCell.setText(LocaleController.getString("ProfilePopupNotification", 2131627741));
                    return;
                }
                headerCell.setText(LocaleController.getString("General", 2131626055));
                return;
            }
            boolean z = false;
            boolean z2 = true;
            if (itemViewType != 1) {
                if (itemViewType == 2) {
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i != ProfileNotificationsActivity.this.popupInfoRow) {
                        if (i != ProfileNotificationsActivity.this.ledInfoRow) {
                            if (i == ProfileNotificationsActivity.this.priorityInfoRow) {
                                if (ProfileNotificationsActivity.this.priorityRow == -1) {
                                    textInfoPrivacyCell.setText("");
                                } else {
                                    textInfoPrivacyCell.setText(LocaleController.getString("PriorityInfo", 2131627692));
                                }
                                textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(this.context, 2131165435, "windowBackgroundGrayShadow"));
                                return;
                            } else if (i != ProfileNotificationsActivity.this.ringtoneInfoRow) {
                                return;
                            } else {
                                textInfoPrivacyCell.setText(LocaleController.getString("VoipRingtoneInfo", 2131629188));
                                textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(this.context, 2131165435, "windowBackgroundGrayShadow"));
                                return;
                            }
                        }
                        textInfoPrivacyCell.setText(LocaleController.getString("NotificationsLedInfo", 2131627044));
                        textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(this.context, 2131165436, "windowBackgroundGrayShadow"));
                        return;
                    }
                    textInfoPrivacyCell.setText(LocaleController.getString("ProfilePopupNotificationInfo", 2131627742));
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(this.context, 2131165435, "windowBackgroundGrayShadow"));
                    return;
                } else if (itemViewType == 3) {
                    TextColorCell textColorCell = (TextColorCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) ProfileNotificationsActivity.this).currentAccount);
                    if (!notificationsSettings.contains("color_" + ProfileNotificationsActivity.this.dialogId)) {
                        if (DialogObject.isChatDialog(ProfileNotificationsActivity.this.dialogId)) {
                            i2 = notificationsSettings.getInt("GroupLed", -16776961);
                        } else {
                            i2 = notificationsSettings.getInt("MessagesLed", -16776961);
                        }
                    } else {
                        i2 = notificationsSettings.getInt("color_" + ProfileNotificationsActivity.this.dialogId, -16776961);
                    }
                    int i3 = 0;
                    while (true) {
                        if (i3 >= 9) {
                            break;
                        } else if (TextColorCell.colorsToSave[i3] == i2) {
                            i2 = TextColorCell.colors[i3];
                            break;
                        } else {
                            i3++;
                        }
                    }
                    textColorCell.setTextAndColor(LocaleController.getString("NotificationsLedColor", 2131627043), i2, false);
                    return;
                } else if (itemViewType != 4) {
                    if (itemViewType == 5) {
                        ((UserCell2) viewHolder.itemView).setData(DialogObject.isUserDialog(ProfileNotificationsActivity.this.dialogId) ? MessagesController.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).getUser(Long.valueOf(ProfileNotificationsActivity.this.dialogId)) : MessagesController.getInstance(((BaseFragment) ProfileNotificationsActivity.this).currentAccount).getChat(Long.valueOf(-ProfileNotificationsActivity.this.dialogId)), null, null, 0);
                        return;
                    } else if (itemViewType != 7) {
                        return;
                    } else {
                        TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                        SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(((BaseFragment) ProfileNotificationsActivity.this).currentAccount);
                        if (i == ProfileNotificationsActivity.this.enableRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("Notifications", 2131627016), ProfileNotificationsActivity.this.notificationsEnabled, true);
                            return;
                        } else if (i != ProfileNotificationsActivity.this.previewRow) {
                            return;
                        } else {
                            String string = LocaleController.getString("MessagePreview", 2131626640);
                            textCheckCell.setTextAndCheck(string, notificationsSettings2.getBoolean("content_preview_" + ProfileNotificationsActivity.this.dialogId, true), true);
                            return;
                        }
                    }
                } else {
                    RadioCell radioCell = (RadioCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(((BaseFragment) ProfileNotificationsActivity.this).currentAccount);
                    int i4 = notificationsSettings3.getInt("popup_" + ProfileNotificationsActivity.this.dialogId, 0);
                    if (i4 == 0) {
                        i4 = notificationsSettings3.getInt(DialogObject.isChatDialog(ProfileNotificationsActivity.this.dialogId) ? "popupGroup" : "popupAll", 0) != 0 ? 1 : 2;
                    }
                    if (i != ProfileNotificationsActivity.this.popupEnabledRow) {
                        if (i != ProfileNotificationsActivity.this.popupDisabledRow) {
                            return;
                        }
                        String string2 = LocaleController.getString("PopupDisabled", 2131627590);
                        if (i4 != 2) {
                            z2 = false;
                        }
                        radioCell.setText(string2, z2, false);
                        radioCell.setTag(2);
                        return;
                    }
                    String string3 = LocaleController.getString("PopupEnabled", 2131627591);
                    if (i4 == 1) {
                        z = true;
                    }
                    radioCell.setText(string3, z, true);
                    radioCell.setTag(1);
                    return;
                }
            }
            TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
            SharedPreferences notificationsSettings4 = MessagesController.getNotificationsSettings(((BaseFragment) ProfileNotificationsActivity.this).currentAccount);
            if (i == ProfileNotificationsActivity.this.customResetRow) {
                textSettingsCell.setText(LocaleController.getString(2131627994), false);
                textSettingsCell.setTextColor(ProfileNotificationsActivity.this.getThemedColor("dialogTextRed"));
                return;
            }
            textSettingsCell.setTextColor(ProfileNotificationsActivity.this.getThemedColor("windowBackgroundWhiteBlackText"));
            if (i != ProfileNotificationsActivity.this.soundRow) {
                if (i != ProfileNotificationsActivity.this.ringtoneRow) {
                    if (i != ProfileNotificationsActivity.this.vibrateRow) {
                        if (i != ProfileNotificationsActivity.this.priorityRow) {
                            if (i != ProfileNotificationsActivity.this.smartRow) {
                                if (i != ProfileNotificationsActivity.this.callsVibrateRow) {
                                    return;
                                }
                                int i5 = notificationsSettings4.getInt("calls_vibrate_" + ProfileNotificationsActivity.this.dialogId, 0);
                                if (i5 == 0 || i5 == 4) {
                                    textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628878), LocaleController.getString("VibrationDefault", 2131628879), true);
                                    return;
                                } else if (i5 == 1) {
                                    textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628878), LocaleController.getString("Short", 2131628327), true);
                                    return;
                                } else if (i5 == 2) {
                                    textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628878), LocaleController.getString("VibrationDisabled", 2131628880), true);
                                    return;
                                } else if (i5 != 3) {
                                    return;
                                } else {
                                    textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628878), LocaleController.getString("Long", 2131626507), true);
                                    return;
                                }
                            }
                            int i6 = notificationsSettings4.getInt("smart_max_count_" + ProfileNotificationsActivity.this.dialogId, 2);
                            int i7 = notificationsSettings4.getInt("smart_delay_" + ProfileNotificationsActivity.this.dialogId, 180);
                            if (i6 == 0) {
                                String string4 = LocaleController.getString("SmartNotifications", 2131628370);
                                String string5 = LocaleController.getString("SmartNotificationsDisabled", 2131628373);
                                if (ProfileNotificationsActivity.this.priorityRow != -1) {
                                    z = true;
                                }
                                textSettingsCell.setTextAndValue(string4, string5, z);
                                return;
                            }
                            String formatPluralString = LocaleController.formatPluralString("Minutes", i7 / 60, new Object[0]);
                            String string6 = LocaleController.getString("SmartNotifications", 2131628370);
                            String formatString = LocaleController.formatString("SmartNotificationsInfo", 2131628374, Integer.valueOf(i6), formatPluralString);
                            if (ProfileNotificationsActivity.this.priorityRow != -1) {
                                z = true;
                            }
                            textSettingsCell.setTextAndValue(string6, formatString, z);
                            return;
                        }
                        int i8 = notificationsSettings4.getInt("priority_" + ProfileNotificationsActivity.this.dialogId, 3);
                        if (i8 == 0) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131627040), LocaleController.getString("NotificationsPriorityHigh", 2131627053), false);
                            return;
                        } else if (i8 == 1 || i8 == 2) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131627040), LocaleController.getString("NotificationsPriorityUrgent", 2131627057), false);
                            return;
                        } else if (i8 == 3) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131627040), LocaleController.getString("NotificationsPrioritySettings", 2131627056), false);
                            return;
                        } else if (i8 == 4) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131627040), LocaleController.getString("NotificationsPriorityLow", 2131627054), false);
                            return;
                        } else if (i8 != 5) {
                            return;
                        } else {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131627040), LocaleController.getString("NotificationsPriorityMedium", 2131627055), false);
                            return;
                        }
                    }
                    int i9 = notificationsSettings4.getInt("vibrate_" + ProfileNotificationsActivity.this.dialogId, 0);
                    if (i9 == 0 || i9 == 4) {
                        String string7 = LocaleController.getString("Vibrate", 2131628878);
                        String string8 = LocaleController.getString("VibrationDefault", 2131628879);
                        if (ProfileNotificationsActivity.this.smartRow != -1 || ProfileNotificationsActivity.this.priorityRow != -1) {
                            z = true;
                        }
                        textSettingsCell.setTextAndValue(string7, string8, z);
                        return;
                    } else if (i9 == 1) {
                        String string9 = LocaleController.getString("Vibrate", 2131628878);
                        String string10 = LocaleController.getString("Short", 2131628327);
                        if (ProfileNotificationsActivity.this.smartRow != -1 || ProfileNotificationsActivity.this.priorityRow != -1) {
                            z = true;
                        }
                        textSettingsCell.setTextAndValue(string9, string10, z);
                        return;
                    } else if (i9 == 2) {
                        String string11 = LocaleController.getString("Vibrate", 2131628878);
                        String string12 = LocaleController.getString("VibrationDisabled", 2131628880);
                        if (ProfileNotificationsActivity.this.smartRow != -1 || ProfileNotificationsActivity.this.priorityRow != -1) {
                            z = true;
                        }
                        textSettingsCell.setTextAndValue(string11, string12, z);
                        return;
                    } else if (i9 != 3) {
                        return;
                    } else {
                        String string13 = LocaleController.getString("Vibrate", 2131628878);
                        String string14 = LocaleController.getString("Long", 2131626507);
                        if (ProfileNotificationsActivity.this.smartRow != -1 || ProfileNotificationsActivity.this.priorityRow != -1) {
                            z = true;
                        }
                        textSettingsCell.setTextAndValue(string13, string14, z);
                        return;
                    }
                }
                String string15 = notificationsSettings4.getString("ringtone_" + ProfileNotificationsActivity.this.dialogId, LocaleController.getString("DefaultRingtone", 2131625367));
                if (string15.equals("NoSound")) {
                    string15 = LocaleController.getString("NoSound", 2131626872);
                }
                textSettingsCell.setTextAndValue(LocaleController.getString("VoipSettingsRingtone", 2131629191), string15, false);
                return;
            }
            String string16 = notificationsSettings4.getString("sound_" + ProfileNotificationsActivity.this.dialogId, LocaleController.getString("SoundDefault", 2131628387));
            long j = notificationsSettings4.getLong("sound_document_id_" + ProfileNotificationsActivity.this.dialogId, 0L);
            if (j != 0) {
                TLRPC$Document document = ProfileNotificationsActivity.this.getMediaDataController().ringtoneDataStore.getDocument(j);
                if (document == null) {
                    string16 = LocaleController.getString("CustomSound", 2131625305);
                } else {
                    string16 = NotificationsSoundActivity.trimTitle(document, document.file_name_fixed);
                }
            } else if (string16.equals("NoSound")) {
                string16 = LocaleController.getString("NoSound", 2131626872);
            } else if (string16.equals("Default")) {
                string16 = LocaleController.getString("SoundDefault", 2131628387);
            }
            textSettingsCell.setTextAndValue(LocaleController.getString("Sound", 2131628384), string16, true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                ((HeaderCell) viewHolder.itemView).setEnabled(ProfileNotificationsActivity.this.notificationsEnabled, null);
            } else if (itemViewType == 1) {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                if (viewHolder.getAdapterPosition() != ProfileNotificationsActivity.this.customResetRow) {
                    textSettingsCell.setEnabled(ProfileNotificationsActivity.this.notificationsEnabled, null);
                } else {
                    textSettingsCell.setEnabled(true, null);
                }
            } else if (itemViewType == 2) {
                ((TextInfoPrivacyCell) viewHolder.itemView).setEnabled(ProfileNotificationsActivity.this.notificationsEnabled, null);
            } else if (itemViewType == 3) {
                ((TextColorCell) viewHolder.itemView).setEnabled(ProfileNotificationsActivity.this.notificationsEnabled, null);
            } else if (itemViewType == 4) {
                ((RadioCell) viewHolder.itemView).setEnabled(ProfileNotificationsActivity.this.notificationsEnabled, null);
            } else if (itemViewType != 7) {
            } else {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                if (viewHolder.getAdapterPosition() == ProfileNotificationsActivity.this.previewRow) {
                    textCheckCell.setEnabled(ProfileNotificationsActivity.this.notificationsEnabled, null);
                } else {
                    textCheckCell.setEnabled(true, null);
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == ProfileNotificationsActivity.this.generalRow || i == ProfileNotificationsActivity.this.popupRow || i == ProfileNotificationsActivity.this.ledRow || i == ProfileNotificationsActivity.this.callsRow) {
                return 0;
            }
            if (i == ProfileNotificationsActivity.this.soundRow || i == ProfileNotificationsActivity.this.vibrateRow || i == ProfileNotificationsActivity.this.priorityRow || i == ProfileNotificationsActivity.this.smartRow || i == ProfileNotificationsActivity.this.ringtoneRow || i == ProfileNotificationsActivity.this.callsVibrateRow || i == ProfileNotificationsActivity.this.customResetRow) {
                return 1;
            }
            if (i == ProfileNotificationsActivity.this.popupInfoRow || i == ProfileNotificationsActivity.this.ledInfoRow || i == ProfileNotificationsActivity.this.priorityInfoRow || i == ProfileNotificationsActivity.this.ringtoneInfoRow) {
                return 2;
            }
            if (i == ProfileNotificationsActivity.this.colorRow) {
                return 3;
            }
            if (i == ProfileNotificationsActivity.this.popupEnabledRow || i == ProfileNotificationsActivity.this.popupDisabledRow) {
                return 4;
            }
            if (i == ProfileNotificationsActivity.this.avatarRow) {
                return 5;
            }
            if (i == ProfileNotificationsActivity.this.avatarSectionRow || i == ProfileNotificationsActivity.this.customResetShadowRow) {
                return 6;
            }
            return (i == ProfileNotificationsActivity.this.enableRow || i == ProfileNotificationsActivity.this.previewRow) ? 7 : 0;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public int getNavigationBarColor() {
        return getThemedColor("windowBackgroundGray");
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        ProfileNotificationsActivity$$ExternalSyntheticLambda5 profileNotificationsActivity$$ExternalSyntheticLambda5 = new ProfileNotificationsActivity$$ExternalSyntheticLambda5(this);
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextSettingsCell.class, TextColorCell.class, RadioCell.class, UserCell2.class, TextCheckCell.class, TextCheckBoxCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteValueText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextColorCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{RadioCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "radioBackground"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "radioBackgroundChecked"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell2.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell2.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, profileNotificationsActivity$$ExternalSyntheticLambda5, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell2.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, profileNotificationsActivity$$ExternalSyntheticLambda5, "windowBackgroundWhiteBlueText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell2.class}, null, Theme.avatarDrawables, null, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileNotificationsActivity$$ExternalSyntheticLambda5, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileNotificationsActivity$$ExternalSyntheticLambda5, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileNotificationsActivity$$ExternalSyntheticLambda5, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileNotificationsActivity$$ExternalSyntheticLambda5, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileNotificationsActivity$$ExternalSyntheticLambda5, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileNotificationsActivity$$ExternalSyntheticLambda5, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, profileNotificationsActivity$$ExternalSyntheticLambda5, "avatar_backgroundPink"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$7() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof UserCell2) {
                    ((UserCell2) childAt).update(0);
                }
            }
        }
    }
}
