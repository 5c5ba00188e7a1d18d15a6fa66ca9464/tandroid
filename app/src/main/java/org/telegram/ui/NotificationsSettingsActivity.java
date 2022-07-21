package org.telegram.ui;

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
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$TL_account_resetNotifySettings;
import org.telegram.tgnet.TLRPC$TL_account_setContactSignUpNotification;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class NotificationsSettingsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int accountsAllRow;
    private int accountsInfoRow;
    private int accountsSectionRow;
    private ListAdapter adapter;
    private int androidAutoAlertRow;
    private int badgeNumberMessagesRow;
    private int badgeNumberMutedRow;
    private int badgeNumberSection;
    private int badgeNumberSection2Row;
    private int badgeNumberShowRow;
    private int callsRingtoneRow;
    private int callsSection2Row;
    private int callsSectionRow;
    private int callsVibrateRow;
    private int channelsRow;
    private int contactJoinedRow;
    private int eventsSection2Row;
    private int eventsSectionRow;
    private int groupRow;
    private int inappPreviewRow;
    private int inappPriorityRow;
    private int inappSectionRow;
    private int inappSoundRow;
    private int inappVibrateRow;
    private int inchatSoundRow;
    private RecyclerListView listView;
    private int notificationsSection2Row;
    private int notificationsSectionRow;
    private int notificationsServiceConnectionRow;
    private int notificationsServiceRow;
    private int otherSection2Row;
    private int otherSectionRow;
    private int pinnedMessageRow;
    private int privateRow;
    private int repeatRow;
    private int resetNotificationsRow;
    private int resetNotificationsSectionRow;
    private int resetSection2Row;
    private int resetSectionRow;
    private boolean reseting = false;
    private ArrayList<NotificationException> exceptionUsers = null;
    private ArrayList<NotificationException> exceptionChats = null;
    private ArrayList<NotificationException> exceptionChannels = null;
    private int rowCount = 0;

    /* loaded from: classes3.dex */
    public static class NotificationException {
        public long did;
        public boolean hasCustom;
        public int muteUntil;
        public int notify;
    }

    public static /* synthetic */ void lambda$createView$5(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        MessagesController.getInstance(this.currentAccount).loadSignUpNotificationsSettings();
        loadExceptions();
        if (UserConfig.getActivatedAccountsCount() > 1) {
            int i = this.rowCount;
            int i2 = i + 1;
            this.rowCount = i2;
            this.accountsSectionRow = i;
            int i3 = i2 + 1;
            this.rowCount = i3;
            this.accountsAllRow = i2;
            this.rowCount = i3 + 1;
            this.accountsInfoRow = i3;
        } else {
            this.accountsSectionRow = -1;
            this.accountsAllRow = -1;
            this.accountsInfoRow = -1;
        }
        int i4 = this.rowCount;
        int i5 = i4 + 1;
        this.rowCount = i5;
        this.notificationsSectionRow = i4;
        int i6 = i5 + 1;
        this.rowCount = i6;
        this.privateRow = i5;
        int i7 = i6 + 1;
        this.rowCount = i7;
        this.groupRow = i6;
        int i8 = i7 + 1;
        this.rowCount = i8;
        this.channelsRow = i7;
        int i9 = i8 + 1;
        this.rowCount = i9;
        this.notificationsSection2Row = i8;
        int i10 = i9 + 1;
        this.rowCount = i10;
        this.callsSectionRow = i9;
        int i11 = i10 + 1;
        this.rowCount = i11;
        this.callsVibrateRow = i10;
        int i12 = i11 + 1;
        this.rowCount = i12;
        this.callsRingtoneRow = i11;
        int i13 = i12 + 1;
        this.rowCount = i13;
        this.eventsSection2Row = i12;
        int i14 = i13 + 1;
        this.rowCount = i14;
        this.badgeNumberSection = i13;
        int i15 = i14 + 1;
        this.rowCount = i15;
        this.badgeNumberShowRow = i14;
        int i16 = i15 + 1;
        this.rowCount = i16;
        this.badgeNumberMutedRow = i15;
        int i17 = i16 + 1;
        this.rowCount = i17;
        this.badgeNumberMessagesRow = i16;
        int i18 = i17 + 1;
        this.rowCount = i18;
        this.badgeNumberSection2Row = i17;
        int i19 = i18 + 1;
        this.rowCount = i19;
        this.inappSectionRow = i18;
        int i20 = i19 + 1;
        this.rowCount = i20;
        this.inappSoundRow = i19;
        int i21 = i20 + 1;
        this.rowCount = i21;
        this.inappVibrateRow = i20;
        int i22 = i21 + 1;
        this.rowCount = i22;
        this.inappPreviewRow = i21;
        int i23 = i22 + 1;
        this.rowCount = i23;
        this.inchatSoundRow = i22;
        if (Build.VERSION.SDK_INT >= 21) {
            this.rowCount = i23 + 1;
            this.inappPriorityRow = i23;
        } else {
            this.inappPriorityRow = -1;
        }
        int i24 = this.rowCount;
        int i25 = i24 + 1;
        this.rowCount = i25;
        this.callsSection2Row = i24;
        int i26 = i25 + 1;
        this.rowCount = i26;
        this.eventsSectionRow = i25;
        int i27 = i26 + 1;
        this.rowCount = i27;
        this.contactJoinedRow = i26;
        int i28 = i27 + 1;
        this.rowCount = i28;
        this.pinnedMessageRow = i27;
        int i29 = i28 + 1;
        this.rowCount = i29;
        this.otherSection2Row = i28;
        int i30 = i29 + 1;
        this.rowCount = i30;
        this.otherSectionRow = i29;
        int i31 = i30 + 1;
        this.rowCount = i31;
        this.notificationsServiceRow = i30;
        int i32 = i31 + 1;
        this.rowCount = i32;
        this.notificationsServiceConnectionRow = i31;
        this.androidAutoAlertRow = -1;
        int i33 = i32 + 1;
        this.rowCount = i33;
        this.repeatRow = i32;
        int i34 = i33 + 1;
        this.rowCount = i34;
        this.resetSection2Row = i33;
        int i35 = i34 + 1;
        this.rowCount = i35;
        this.resetSectionRow = i34;
        int i36 = i35 + 1;
        this.rowCount = i36;
        this.resetNotificationsRow = i35;
        this.rowCount = i36 + 1;
        this.resetNotificationsSectionRow = i36;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
        return super.onFragmentCreate();
    }

    private void loadExceptions() {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new NotificationsSettingsActivity$$ExternalSyntheticLambda3(this));
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0121, code lost:
        if (r4.deleted != false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x014c, code lost:
        if (r4.deleted != false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x014e, code lost:
        r7 = r15;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0264  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x027e A[LOOP:3: B:108:0x027c->B:109:0x027e, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0297  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0225  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadExceptions$1() {
        ArrayList<TLRPC$Chat> arrayList;
        ArrayList<TLRPC$Chat> arrayList2;
        ArrayList<TLRPC$User> arrayList3;
        int size;
        int i;
        int size2;
        int i2;
        int size3;
        int i3;
        int size4;
        Exception e;
        long j;
        ArrayList<TLRPC$User> arrayList4;
        SharedPreferences sharedPreferences;
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = new ArrayList();
        LongSparseArray longSparseArray = new LongSparseArray();
        ArrayList<Long> arrayList8 = new ArrayList<>();
        ArrayList arrayList9 = new ArrayList();
        ArrayList arrayList10 = new ArrayList();
        ArrayList<TLRPC$User> arrayList11 = new ArrayList<>();
        ArrayList<TLRPC$Chat> arrayList12 = new ArrayList<>();
        ArrayList<TLRPC$EncryptedChat> arrayList13 = new ArrayList<>();
        long j2 = UserConfig.getInstance(this.currentAccount).clientUserId;
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
        Map<String, ?> all = notificationsSettings.getAll();
        Iterator<Map.Entry<String, ?>> it = all.entrySet().iterator();
        while (true) {
            arrayList = arrayList12;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<String, ?> next = it.next();
            String key = next.getKey();
            Iterator<Map.Entry<String, ?>> it2 = it;
            if (key.startsWith("notify2_")) {
                arrayList4 = arrayList11;
                String replace = key.replace("notify2_", "");
                ArrayList arrayList14 = arrayList6;
                ArrayList arrayList15 = arrayList7;
                long longValue = Utilities.parseLong(replace).longValue();
                if (longValue == 0 || longValue == j2) {
                    j = j2;
                } else {
                    NotificationException notificationException = new NotificationException();
                    notificationException.did = longValue;
                    j = j2;
                    notificationException.hasCustom = notificationsSettings.getBoolean("custom_" + longValue, false);
                    int intValue = ((Integer) next.getValue()).intValue();
                    notificationException.notify = intValue;
                    if (intValue != 0) {
                        Integer num = (Integer) all.get("notifyuntil_" + replace);
                        if (num != null) {
                            notificationException.muteUntil = num.intValue();
                        }
                    }
                    if (DialogObject.isEncryptedDialog(longValue)) {
                        int encryptedChatId = DialogObject.getEncryptedChatId(longValue);
                        TLRPC$EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(encryptedChatId));
                        if (encryptedChat == null) {
                            arrayList10.add(Integer.valueOf(encryptedChatId));
                            longSparseArray.put(longValue, notificationException);
                        } else {
                            TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(encryptedChat.user_id));
                            if (user == null) {
                                arrayList8.add(Long.valueOf(encryptedChat.user_id));
                                longSparseArray.put(encryptedChat.user_id, notificationException);
                            }
                        }
                        arrayList5.add(notificationException);
                    } else if (DialogObject.isUserDialog(longValue)) {
                        TLRPC$User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(longValue));
                        if (user2 == null) {
                            arrayList8.add(Long.valueOf(longValue));
                            longSparseArray.put(longValue, notificationException);
                        }
                        arrayList5.add(notificationException);
                    } else {
                        long j3 = -longValue;
                        sharedPreferences = notificationsSettings;
                        TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j3));
                        if (chat == null) {
                            arrayList9.add(Long.valueOf(j3));
                            longSparseArray.put(longValue, notificationException);
                        } else if (!chat.left && !chat.kicked && chat.migrated_to == null) {
                            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                                arrayList7 = arrayList15;
                                arrayList7.add(notificationException);
                                arrayList6 = arrayList14;
                            } else {
                                arrayList7 = arrayList15;
                                arrayList6 = arrayList14;
                                arrayList6.add(notificationException);
                            }
                        }
                        arrayList6 = arrayList14;
                        notificationsSettings = sharedPreferences;
                        arrayList12 = arrayList;
                        it = it2;
                        arrayList11 = arrayList4;
                        arrayList7 = arrayList15;
                    }
                    j2 = j;
                }
                sharedPreferences = notificationsSettings;
                arrayList6 = arrayList14;
                arrayList7 = arrayList15;
            } else {
                arrayList4 = arrayList11;
                j = j2;
                sharedPreferences = notificationsSettings;
            }
            notificationsSettings = sharedPreferences;
            arrayList12 = arrayList;
            it = it2;
            arrayList11 = arrayList4;
            j2 = j;
        }
        ArrayList<TLRPC$User> arrayList16 = arrayList11;
        if (longSparseArray.size() != 0) {
            try {
                if (!arrayList10.isEmpty()) {
                    MessagesStorage.getInstance(this.currentAccount).getEncryptedChatsInternal(TextUtils.join(",", arrayList10), arrayList13, arrayList8);
                }
                if (!arrayList8.isEmpty()) {
                    try {
                        arrayList3 = arrayList16;
                        try {
                            MessagesStorage.getInstance(this.currentAccount).getUsersInternal(TextUtils.join(",", arrayList8), arrayList3);
                        } catch (Exception e2) {
                            e = e2;
                            arrayList2 = arrayList;
                            FileLog.e(e);
                            size = arrayList2.size();
                            while (i < size) {
                            }
                            size2 = arrayList3.size();
                            while (i2 < size2) {
                            }
                            size3 = arrayList13.size();
                            while (i3 < size3) {
                            }
                            size4 = longSparseArray.size();
                            while (r12 < size4) {
                            }
                            AndroidUtilities.runOnUIThread(new NotificationsSettingsActivity$$ExternalSyntheticLambda6(this, arrayList3, arrayList2, arrayList13, arrayList5, arrayList6, arrayList7));
                        }
                    } catch (Exception e3) {
                        e = e3;
                        arrayList3 = arrayList16;
                        arrayList2 = arrayList;
                        FileLog.e(e);
                        size = arrayList2.size();
                        while (i < size) {
                        }
                        size2 = arrayList3.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList13.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (r12 < size4) {
                        }
                        AndroidUtilities.runOnUIThread(new NotificationsSettingsActivity$$ExternalSyntheticLambda6(this, arrayList3, arrayList2, arrayList13, arrayList5, arrayList6, arrayList7));
                    }
                } else {
                    arrayList3 = arrayList16;
                }
                if (!arrayList9.isEmpty()) {
                    arrayList2 = arrayList;
                    try {
                        MessagesStorage.getInstance(this.currentAccount).getChatsInternal(TextUtils.join(",", arrayList9), arrayList2);
                    } catch (Exception e4) {
                        e = e4;
                        FileLog.e(e);
                        size = arrayList2.size();
                        while (i < size) {
                        }
                        size2 = arrayList3.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList13.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (r12 < size4) {
                        }
                        AndroidUtilities.runOnUIThread(new NotificationsSettingsActivity$$ExternalSyntheticLambda6(this, arrayList3, arrayList2, arrayList13, arrayList5, arrayList6, arrayList7));
                    }
                } else {
                    arrayList2 = arrayList;
                }
            } catch (Exception e5) {
                e = e5;
                arrayList2 = arrayList;
                arrayList3 = arrayList16;
            }
            size = arrayList2.size();
            for (i = 0; i < size; i++) {
                TLRPC$Chat tLRPC$Chat = arrayList2.get(i);
                if (!tLRPC$Chat.left && !tLRPC$Chat.kicked && tLRPC$Chat.migrated_to == null) {
                    NotificationException notificationException2 = (NotificationException) longSparseArray.get(-tLRPC$Chat.id);
                    longSparseArray.remove(-tLRPC$Chat.id);
                    if (notificationException2 != null) {
                        if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                            arrayList7.add(notificationException2);
                        } else {
                            arrayList6.add(notificationException2);
                        }
                    }
                }
            }
            size2 = arrayList3.size();
            for (i2 = 0; i2 < size2; i2++) {
                TLRPC$User tLRPC$User = arrayList3.get(i2);
                if (!tLRPC$User.deleted) {
                    longSparseArray.remove(tLRPC$User.id);
                }
            }
            size3 = arrayList13.size();
            for (i3 = 0; i3 < size3; i3++) {
                longSparseArray.remove(DialogObject.makeEncryptedDialogId(arrayList13.get(i3).id));
            }
            size4 = longSparseArray.size();
            for (int i4 = 0; i4 < size4; i4++) {
                if (DialogObject.isChatDialog(longSparseArray.keyAt(i4))) {
                    arrayList6.remove(longSparseArray.valueAt(i4));
                    arrayList7.remove(longSparseArray.valueAt(i4));
                } else {
                    arrayList5.remove(longSparseArray.valueAt(i4));
                }
            }
        } else {
            arrayList2 = arrayList;
            arrayList3 = arrayList16;
        }
        AndroidUtilities.runOnUIThread(new NotificationsSettingsActivity$$ExternalSyntheticLambda6(this, arrayList3, arrayList2, arrayList13, arrayList5, arrayList6, arrayList7));
    }

    public /* synthetic */ void lambda$loadExceptions$0(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, ArrayList arrayList5, ArrayList arrayList6) {
        MessagesController.getInstance(this.currentAccount).putUsers(arrayList, true);
        MessagesController.getInstance(this.currentAccount).putChats(arrayList2, true);
        MessagesController.getInstance(this.currentAccount).putEncryptedChats(arrayList3, true);
        this.exceptionUsers = arrayList4;
        this.exceptionChats = arrayList5;
        this.exceptionChannels = arrayList6;
        this.adapter.notifyItemChanged(this.privateRow);
        this.adapter.notifyItemChanged(this.groupRow);
        this.adapter.notifyItemChanged(this.channelsRow);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("NotificationsAndSounds", 2131627070));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        this.listView.setLayoutManager(new AnonymousClass2(this, context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView2 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.adapter = listAdapter;
        recyclerListView2.setAdapter(listAdapter);
        this.listView.setOnItemClickListener(new NotificationsSettingsActivity$$ExternalSyntheticLambda9(this));
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.NotificationsSettingsActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            NotificationsSettingsActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                NotificationsSettingsActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.NotificationsSettingsActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends LinearLayoutManager {
        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        AnonymousClass2(NotificationsSettingsActivity notificationsSettingsActivity, Context context, int i, boolean z) {
            super(context, i, z);
        }
    }

    public /* synthetic */ void lambda$createView$8(View view, int i, float f, float f2) {
        ArrayList<NotificationException> arrayList;
        if (getParentActivity() == null) {
            return;
        }
        int i2 = this.privateRow;
        int i3 = 2;
        boolean z = false;
        z = false;
        z = false;
        z = false;
        z = false;
        z = false;
        z = false;
        if (i == i2 || i == this.groupRow || i == this.channelsRow) {
            if (i == i2) {
                arrayList = this.exceptionUsers;
                i3 = 1;
            } else if (i == this.groupRow) {
                arrayList = this.exceptionChats;
                i3 = 0;
            } else {
                arrayList = this.exceptionChannels;
            }
            if (arrayList == null) {
                return;
            }
            NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
            boolean isGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(i3);
            if ((LocaleController.isRTL && f <= AndroidUtilities.dp(76.0f)) || (!LocaleController.isRTL && f >= view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) {
                getNotificationsController().setGlobalNotificationsEnabled(i3, !isGlobalNotificationsEnabled ? 0 : Integer.MAX_VALUE);
                showExceptionsAlert(i);
                notificationsCheckCell.setChecked(!isGlobalNotificationsEnabled, 0);
                this.adapter.notifyItemChanged(i);
            } else {
                presentFragment(new NotificationsCustomSettingsActivity(i3, arrayList));
            }
            z = isGlobalNotificationsEnabled;
        } else {
            Parcelable parcelable = null;
            String str = null;
            parcelable = null;
            if (i == this.callsRingtoneRow) {
                try {
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                    Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
                    intent.putExtra("android.intent.extra.ringtone.TYPE", 1);
                    intent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                    intent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
                    intent.putExtra("android.intent.extra.ringtone.DEFAULT_URI", RingtoneManager.getDefaultUri(1));
                    Uri uri = Settings.System.DEFAULT_RINGTONE_URI;
                    String path = uri != null ? uri.getPath() : null;
                    String string = notificationsSettings.getString("CallsRingtonePath", path);
                    if (string != null && !string.equals("NoSound")) {
                        parcelable = string.equals(path) ? uri : Uri.parse(string);
                    }
                    intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", parcelable);
                    startActivityForResult(intent, i);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            } else if (i == this.resetNotificationsRow) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("ResetNotificationsAlertTitle", 2131628065));
                builder.setMessage(LocaleController.getString("ResetNotificationsAlert", 2131628064));
                builder.setPositiveButton(LocaleController.getString("Reset", 2131628042), new NotificationsSettingsActivity$$ExternalSyntheticLambda0(this));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
                AlertDialog create = builder.create();
                showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor("dialogTextRed2"));
                }
            } else if (i == this.inappSoundRow) {
                SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(this.currentAccount);
                SharedPreferences.Editor edit = notificationsSettings2.edit();
                z = notificationsSettings2.getBoolean("EnableInAppSounds", true);
                edit.putBoolean("EnableInAppSounds", !z);
                edit.commit();
            } else if (i == this.inappVibrateRow) {
                SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(this.currentAccount);
                SharedPreferences.Editor edit2 = notificationsSettings3.edit();
                z = notificationsSettings3.getBoolean("EnableInAppVibrate", true);
                edit2.putBoolean("EnableInAppVibrate", !z);
                edit2.commit();
            } else if (i == this.inappPreviewRow) {
                SharedPreferences notificationsSettings4 = MessagesController.getNotificationsSettings(this.currentAccount);
                SharedPreferences.Editor edit3 = notificationsSettings4.edit();
                z = notificationsSettings4.getBoolean("EnableInAppPreview", true);
                edit3.putBoolean("EnableInAppPreview", !z);
                edit3.commit();
            } else if (i == this.inchatSoundRow) {
                SharedPreferences notificationsSettings5 = MessagesController.getNotificationsSettings(this.currentAccount);
                SharedPreferences.Editor edit4 = notificationsSettings5.edit();
                z = notificationsSettings5.getBoolean("EnableInChatSound", true);
                edit4.putBoolean("EnableInChatSound", !z);
                edit4.commit();
                getNotificationsController().setInChatSoundEnabled(!z);
            } else if (i == this.inappPriorityRow) {
                SharedPreferences notificationsSettings6 = MessagesController.getNotificationsSettings(this.currentAccount);
                SharedPreferences.Editor edit5 = notificationsSettings6.edit();
                z = notificationsSettings6.getBoolean("EnableInAppPriority", false);
                edit5.putBoolean("EnableInAppPriority", !z);
                edit5.commit();
            } else if (i == this.contactJoinedRow) {
                SharedPreferences notificationsSettings7 = MessagesController.getNotificationsSettings(this.currentAccount);
                SharedPreferences.Editor edit6 = notificationsSettings7.edit();
                z = notificationsSettings7.getBoolean("EnableContactJoined", true);
                MessagesController.getInstance(this.currentAccount).enableJoined = !z;
                edit6.putBoolean("EnableContactJoined", !z);
                edit6.commit();
                TLRPC$TL_account_setContactSignUpNotification tLRPC$TL_account_setContactSignUpNotification = new TLRPC$TL_account_setContactSignUpNotification();
                tLRPC$TL_account_setContactSignUpNotification.silent = z;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_setContactSignUpNotification, NotificationsSettingsActivity$$ExternalSyntheticLambda8.INSTANCE);
            } else if (i == this.pinnedMessageRow) {
                SharedPreferences notificationsSettings8 = MessagesController.getNotificationsSettings(this.currentAccount);
                SharedPreferences.Editor edit7 = notificationsSettings8.edit();
                z = notificationsSettings8.getBoolean("PinnedMessages", true);
                edit7.putBoolean("PinnedMessages", !z);
                edit7.commit();
            } else if (i == this.androidAutoAlertRow) {
                SharedPreferences notificationsSettings9 = MessagesController.getNotificationsSettings(this.currentAccount);
                SharedPreferences.Editor edit8 = notificationsSettings9.edit();
                z = notificationsSettings9.getBoolean("EnableAutoNotifications", false);
                edit8.putBoolean("EnableAutoNotifications", !z);
                edit8.commit();
            } else if (i == this.badgeNumberShowRow) {
                SharedPreferences.Editor edit9 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                z = getNotificationsController().showBadgeNumber;
                getNotificationsController().showBadgeNumber = !z;
                edit9.putBoolean("badgeNumber", getNotificationsController().showBadgeNumber);
                edit9.commit();
                getNotificationsController().updateBadge();
            } else if (i == this.badgeNumberMutedRow) {
                SharedPreferences.Editor edit10 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                z = getNotificationsController().showBadgeMuted;
                getNotificationsController().showBadgeMuted = !z;
                edit10.putBoolean("badgeNumberMuted", getNotificationsController().showBadgeMuted);
                edit10.commit();
                getNotificationsController().updateBadge();
                getMessagesStorage().updateMutedDialogsFiltersCounters();
            } else if (i == this.badgeNumberMessagesRow) {
                SharedPreferences.Editor edit11 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                z = getNotificationsController().showBadgeMessages;
                getNotificationsController().showBadgeMessages = !z;
                edit11.putBoolean("badgeNumberMessages", getNotificationsController().showBadgeMessages);
                edit11.commit();
                getNotificationsController().updateBadge();
            } else if (i == this.notificationsServiceConnectionRow) {
                SharedPreferences notificationsSettings10 = MessagesController.getNotificationsSettings(this.currentAccount);
                boolean z2 = notificationsSettings10.getBoolean("pushConnection", getMessagesController().backgroundConnection);
                SharedPreferences.Editor edit12 = notificationsSettings10.edit();
                edit12.putBoolean("pushConnection", !z2);
                edit12.commit();
                if (!z2) {
                    ConnectionsManager.getInstance(this.currentAccount).setPushConnectionEnabled(true);
                } else {
                    ConnectionsManager.getInstance(this.currentAccount).setPushConnectionEnabled(false);
                }
                z = z2;
            } else if (i == this.accountsAllRow) {
                SharedPreferences globalNotificationsSettings = MessagesController.getGlobalNotificationsSettings();
                boolean z3 = globalNotificationsSettings.getBoolean("AllAccounts", true);
                SharedPreferences.Editor edit13 = globalNotificationsSettings.edit();
                edit13.putBoolean("AllAccounts", !z3);
                edit13.commit();
                SharedConfig.showNotificationsForAllAccounts = !z3;
                for (int i4 = 0; i4 < 4; i4++) {
                    if (SharedConfig.showNotificationsForAllAccounts) {
                        NotificationsController.getInstance(i4).showNotifications();
                    } else if (i4 == this.currentAccount) {
                        NotificationsController.getInstance(i4).showNotifications();
                    } else {
                        NotificationsController.getInstance(i4).hideNotifications();
                    }
                }
                z = z3;
            } else if (i == this.notificationsServiceRow) {
                SharedPreferences notificationsSettings11 = MessagesController.getNotificationsSettings(this.currentAccount);
                z = notificationsSettings11.getBoolean("pushService", getMessagesController().keepAliveService);
                SharedPreferences.Editor edit14 = notificationsSettings11.edit();
                edit14.putBoolean("pushService", !z);
                edit14.commit();
                ApplicationLoader.startPushService();
            } else if (i == this.callsVibrateRow) {
                if (getParentActivity() == null) {
                    return;
                }
                if (i == this.callsVibrateRow) {
                    str = "vibrate_calls";
                }
                showDialog(AlertsCreator.createVibrationSelectDialog(getParentActivity(), 0L, str, new NotificationsSettingsActivity$$ExternalSyntheticLambda5(this, i)));
            } else if (i == this.repeatRow) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                builder2.setTitle(LocaleController.getString("RepeatNotifications", 2131627977));
                builder2.setItems(new CharSequence[]{LocaleController.getString("RepeatDisabled", 2131627975), LocaleController.formatPluralString("Minutes", 5, new Object[0]), LocaleController.formatPluralString("Minutes", 10, new Object[0]), LocaleController.formatPluralString("Minutes", 30, new Object[0]), LocaleController.formatPluralString("Hours", 1, new Object[0]), LocaleController.formatPluralString("Hours", 2, new Object[0]), LocaleController.formatPluralString("Hours", 4, new Object[0])}, new NotificationsSettingsActivity$$ExternalSyntheticLambda1(this, i));
                builder2.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
                showDialog(builder2.create());
            }
        }
        if (!(view instanceof TextCheckCell)) {
            return;
        }
        ((TextCheckCell) view).setChecked(!z);
    }

    public /* synthetic */ void lambda$createView$4(DialogInterface dialogInterface, int i) {
        if (this.reseting) {
            return;
        }
        this.reseting = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_resetNotifySettings(), new NotificationsSettingsActivity$$ExternalSyntheticLambda7(this));
    }

    public /* synthetic */ void lambda$createView$3(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new NotificationsSettingsActivity$$ExternalSyntheticLambda4(this));
    }

    public /* synthetic */ void lambda$createView$2() {
        getMessagesController().enableJoined = true;
        this.reseting = false;
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        edit.clear();
        edit.commit();
        this.exceptionChats.clear();
        this.exceptionUsers.clear();
        this.adapter.notifyDataSetChanged();
        if (getParentActivity() != null) {
            Toast.makeText(getParentActivity(), LocaleController.getString("ResetNotificationsText", 2131628066), 0).show();
        }
        getMessagesStorage().updateMutedDialogsFiltersCounters();
    }

    public /* synthetic */ void lambda$createView$6(int i) {
        this.adapter.notifyItemChanged(i);
    }

    public /* synthetic */ void lambda$createView$7(int i, DialogInterface dialogInterface, int i2) {
        int i3 = 5;
        if (i2 != 1) {
            i3 = i2 == 2 ? 10 : i2 == 3 ? 30 : i2 == 4 ? 60 : i2 == 5 ? 120 : i2 == 6 ? 240 : 0;
        }
        MessagesController.getNotificationsSettings(this.currentAccount).edit().putInt("repeat_messages", i3).commit();
        this.adapter.notifyItemChanged(i);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        Ringtone ringtone;
        if (i2 == -1) {
            Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            String str = null;
            if (uri != null && (ringtone = RingtoneManager.getRingtone(getParentActivity(), uri)) != null) {
                if (i == this.callsRingtoneRow) {
                    if (uri.equals(Settings.System.DEFAULT_RINGTONE_URI)) {
                        str = LocaleController.getString("DefaultRingtone", 2131625383);
                    } else {
                        str = ringtone.getTitle(getParentActivity());
                    }
                } else if (uri.equals(Settings.System.DEFAULT_NOTIFICATION_URI)) {
                    str = LocaleController.getString("SoundDefault", 2131628451);
                } else {
                    str = ringtone.getTitle(getParentActivity());
                }
                ringtone.stop();
            }
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            if (i == this.callsRingtoneRow) {
                if (str != null && uri != null) {
                    edit.putString("CallsRingtone", str);
                    edit.putString("CallsRingtonePath", uri.toString());
                } else {
                    edit.putString("CallsRingtone", "NoSound");
                    edit.putString("CallsRingtonePath", "NoSound");
                }
            }
            edit.commit();
            this.adapter.notifyItemChanged(i);
        }
    }

    private void showExceptionsAlert(int i) {
        ArrayList<NotificationException> arrayList;
        String str;
        if (i == this.privateRow) {
            arrayList = this.exceptionUsers;
            if (arrayList != null && !arrayList.isEmpty()) {
                str = LocaleController.formatPluralString("ChatsException", arrayList.size(), new Object[0]);
            }
            str = null;
        } else if (i == this.groupRow) {
            arrayList = this.exceptionChats;
            if (arrayList != null && !arrayList.isEmpty()) {
                str = LocaleController.formatPluralString("Groups", arrayList.size(), new Object[0]);
            }
            str = null;
        } else {
            arrayList = this.exceptionChannels;
            if (arrayList != null && !arrayList.isEmpty()) {
                str = LocaleController.formatPluralString("Channels", arrayList.size(), new Object[0]);
            }
            str = null;
        }
        if (str == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        if (arrayList.size() == 1) {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsExceptionsSingleAlert", 2131627085, str)));
        } else {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsExceptionsAlert", 2131627084, str)));
        }
        builder.setTitle(LocaleController.getString("NotificationsExceptions", 2131627083));
        builder.setNeutralButton(LocaleController.getString("ViewExceptions", 2131628989), new NotificationsSettingsActivity$$ExternalSyntheticLambda2(this, arrayList));
        builder.setNegativeButton(LocaleController.getString("OK", 2131627127), null);
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$showExceptionsAlert$9(ArrayList arrayList, DialogInterface dialogInterface, int i) {
        presentFragment(new NotificationsCustomSettingsActivity(-1, arrayList));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.notificationsSettingsUpdated) {
            this.adapter.notifyDataSetChanged();
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            NotificationsSettingsActivity.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return (adapterPosition == NotificationsSettingsActivity.this.notificationsSectionRow || adapterPosition == NotificationsSettingsActivity.this.notificationsSection2Row || adapterPosition == NotificationsSettingsActivity.this.inappSectionRow || adapterPosition == NotificationsSettingsActivity.this.eventsSectionRow || adapterPosition == NotificationsSettingsActivity.this.otherSectionRow || adapterPosition == NotificationsSettingsActivity.this.resetSectionRow || adapterPosition == NotificationsSettingsActivity.this.badgeNumberSection || adapterPosition == NotificationsSettingsActivity.this.otherSection2Row || adapterPosition == NotificationsSettingsActivity.this.resetSection2Row || adapterPosition == NotificationsSettingsActivity.this.callsSection2Row || adapterPosition == NotificationsSettingsActivity.this.callsSectionRow || adapterPosition == NotificationsSettingsActivity.this.badgeNumberSection2Row || adapterPosition == NotificationsSettingsActivity.this.accountsSectionRow || adapterPosition == NotificationsSettingsActivity.this.accountsInfoRow || adapterPosition == NotificationsSettingsActivity.this.resetNotificationsSectionRow) ? false : true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return NotificationsSettingsActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            View view2;
            if (i == 0) {
                view2 = new HeaderCell(this.mContext);
                view2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else if (i == 1) {
                view2 = new TextCheckCell(this.mContext);
                view2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else if (i == 2) {
                view2 = new TextDetailSettingsCell(this.mContext);
                view2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else if (i == 3) {
                view2 = new NotificationsCheckCell(this.mContext);
                view2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else {
                if (i == 4) {
                    view = new ShadowSectionCell(this.mContext);
                } else if (i == 5) {
                    view2 = new TextSettingsCell(this.mContext);
                    view2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                } else {
                    view = new TextInfoPrivacyCell(this.mContext);
                    view.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                }
                return new RecyclerListView.Holder(view);
            }
            view = view2;
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String string;
            ArrayList arrayList;
            int i2;
            String str;
            switch (viewHolder.getItemViewType()) {
                case 0:
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i != NotificationsSettingsActivity.this.notificationsSectionRow) {
                        if (i != NotificationsSettingsActivity.this.inappSectionRow) {
                            if (i != NotificationsSettingsActivity.this.eventsSectionRow) {
                                if (i != NotificationsSettingsActivity.this.otherSectionRow) {
                                    if (i != NotificationsSettingsActivity.this.resetSectionRow) {
                                        if (i != NotificationsSettingsActivity.this.callsSectionRow) {
                                            if (i != NotificationsSettingsActivity.this.badgeNumberSection) {
                                                if (i != NotificationsSettingsActivity.this.accountsSectionRow) {
                                                    return;
                                                }
                                                headerCell.setText(LocaleController.getString("ShowNotificationsFor", 2131628406));
                                                return;
                                            }
                                            headerCell.setText(LocaleController.getString("BadgeNumber", 2131624671));
                                            return;
                                        }
                                        headerCell.setText(LocaleController.getString("VoipNotificationSettings", 2131629232));
                                        return;
                                    }
                                    headerCell.setText(LocaleController.getString("Reset", 2131628042));
                                    return;
                                }
                                headerCell.setText(LocaleController.getString("NotificationsOther", 2131627104));
                                return;
                            }
                            headerCell.setText(LocaleController.getString("Events", 2131625820));
                            return;
                        }
                        headerCell.setText(LocaleController.getString("InAppNotifications", 2131626268));
                        return;
                    }
                    headerCell.setText(LocaleController.getString("NotificationsForChats", 2131627087));
                    return;
                case 1:
                    TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) NotificationsSettingsActivity.this).currentAccount);
                    if (i != NotificationsSettingsActivity.this.inappSoundRow) {
                        if (i != NotificationsSettingsActivity.this.inappVibrateRow) {
                            if (i != NotificationsSettingsActivity.this.inappPreviewRow) {
                                if (i != NotificationsSettingsActivity.this.inappPriorityRow) {
                                    if (i != NotificationsSettingsActivity.this.contactJoinedRow) {
                                        if (i != NotificationsSettingsActivity.this.pinnedMessageRow) {
                                            if (i != NotificationsSettingsActivity.this.androidAutoAlertRow) {
                                                if (i != NotificationsSettingsActivity.this.notificationsServiceRow) {
                                                    if (i != NotificationsSettingsActivity.this.notificationsServiceConnectionRow) {
                                                        if (i == NotificationsSettingsActivity.this.badgeNumberShowRow) {
                                                            textCheckCell.setTextAndCheck(LocaleController.getString("BadgeNumberShow", 2131624673), NotificationsSettingsActivity.this.getNotificationsController().showBadgeNumber, true);
                                                            return;
                                                        } else if (i == NotificationsSettingsActivity.this.badgeNumberMutedRow) {
                                                            textCheckCell.setTextAndCheck(LocaleController.getString("BadgeNumberMutedChats", 2131624672), NotificationsSettingsActivity.this.getNotificationsController().showBadgeMuted, true);
                                                            return;
                                                        } else if (i == NotificationsSettingsActivity.this.badgeNumberMessagesRow) {
                                                            textCheckCell.setTextAndCheck(LocaleController.getString("BadgeNumberUnread", 2131624674), NotificationsSettingsActivity.this.getNotificationsController().showBadgeMessages, false);
                                                            return;
                                                        } else if (i != NotificationsSettingsActivity.this.inchatSoundRow) {
                                                            if (i != NotificationsSettingsActivity.this.callsVibrateRow) {
                                                                if (i != NotificationsSettingsActivity.this.accountsAllRow) {
                                                                    return;
                                                                }
                                                                textCheckCell.setTextAndCheck(LocaleController.getString("AllAccounts", 2131624319), MessagesController.getGlobalNotificationsSettings().getBoolean("AllAccounts", true), false);
                                                                return;
                                                            }
                                                            textCheckCell.setTextAndCheck(LocaleController.getString("Vibrate", 2131628951), notificationsSettings.getBoolean("EnableCallVibrate", true), true);
                                                            return;
                                                        } else {
                                                            textCheckCell.setTextAndCheck(LocaleController.getString("InChatSound", 2131626272), notificationsSettings.getBoolean("EnableInChatSound", true), true);
                                                            return;
                                                        }
                                                    }
                                                    textCheckCell.setTextAndValueAndCheck(LocaleController.getString("NotificationsServiceConnection", 2131627112), LocaleController.getString("NotificationsServiceConnectionInfo", 2131627113), notificationsSettings.getBoolean("pushConnection", NotificationsSettingsActivity.this.getMessagesController().backgroundConnection), true, true);
                                                    return;
                                                }
                                                textCheckCell.setTextAndValueAndCheck(LocaleController.getString("NotificationsService", 2131627111), LocaleController.getString("NotificationsServiceInfo", 2131627114), notificationsSettings.getBoolean("pushService", NotificationsSettingsActivity.this.getMessagesController().keepAliveService), true, true);
                                                return;
                                            }
                                            textCheckCell.setTextAndCheck("Android Auto", notificationsSettings.getBoolean("EnableAutoNotifications", false), true);
                                            return;
                                        }
                                        textCheckCell.setTextAndCheck(LocaleController.getString("PinnedMessages", 2131627600), notificationsSettings.getBoolean("PinnedMessages", true), false);
                                        return;
                                    }
                                    textCheckCell.setTextAndCheck(LocaleController.getString("ContactJoined", 2131625249), notificationsSettings.getBoolean("EnableContactJoined", true), true);
                                    return;
                                }
                                textCheckCell.setTextAndCheck(LocaleController.getString("NotificationsImportance", 2131627092), notificationsSettings.getBoolean("EnableInAppPriority", false), false);
                                return;
                            }
                            textCheckCell.setTextAndCheck(LocaleController.getString("InAppPreview", 2131626269), notificationsSettings.getBoolean("EnableInAppPreview", true), true);
                            return;
                        }
                        textCheckCell.setTextAndCheck(LocaleController.getString("InAppVibrate", 2131626271), notificationsSettings.getBoolean("EnableInAppVibrate", true), true);
                        return;
                    }
                    textCheckCell.setTextAndCheck(LocaleController.getString("InAppSounds", 2131626270), notificationsSettings.getBoolean("EnableInAppSounds", true), true);
                    return;
                case 2:
                    TextDetailSettingsCell textDetailSettingsCell = (TextDetailSettingsCell) viewHolder.itemView;
                    textDetailSettingsCell.setMultilineDetail(true);
                    if (i != NotificationsSettingsActivity.this.resetNotificationsRow) {
                        return;
                    }
                    textDetailSettingsCell.setTextAndValue(LocaleController.getString("ResetAllNotifications", 2131628048), LocaleController.getString("UndoAllCustom", 2131628798), false);
                    return;
                case 3:
                    NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(((BaseFragment) NotificationsSettingsActivity.this).currentAccount);
                    int currentTime = ConnectionsManager.getInstance(((BaseFragment) NotificationsSettingsActivity.this).currentAccount).getCurrentTime();
                    if (i != NotificationsSettingsActivity.this.privateRow) {
                        if (i == NotificationsSettingsActivity.this.groupRow) {
                            string = LocaleController.getString("NotificationsGroups", 2131627091);
                            arrayList = NotificationsSettingsActivity.this.exceptionChats;
                            i2 = notificationsSettings2.getInt("EnableGroup2", 0);
                        } else {
                            string = LocaleController.getString("NotificationsChannels", 2131627071);
                            arrayList = NotificationsSettingsActivity.this.exceptionChannels;
                            i2 = notificationsSettings2.getInt("EnableChannel2", 0);
                        }
                    } else {
                        string = LocaleController.getString("NotificationsPrivateChats", 2131627110);
                        arrayList = NotificationsSettingsActivity.this.exceptionUsers;
                        i2 = notificationsSettings2.getInt("EnableAll2", 0);
                    }
                    String str2 = string;
                    boolean z = i2 < currentTime;
                    int i3 = (!z && i2 - 31536000 < currentTime) ? 2 : 0;
                    StringBuilder sb = new StringBuilder();
                    if (arrayList != null && !arrayList.isEmpty()) {
                        z = i2 < currentTime;
                        if (z) {
                            sb.append(LocaleController.getString("NotificationsOn", 2131627103));
                        } else if (i2 - 31536000 >= currentTime) {
                            sb.append(LocaleController.getString("NotificationsOff", 2131627101));
                        } else {
                            sb.append(LocaleController.formatString("NotificationsOffUntil", 2131627102, LocaleController.stringForMessageListDate(i2)));
                        }
                        if (sb.length() != 0) {
                            sb.append(", ");
                        }
                        sb.append(LocaleController.formatPluralString("Exception", arrayList.size(), new Object[0]));
                    } else {
                        sb.append(LocaleController.getString("TapToChange", 2131628609));
                    }
                    notificationsCheckCell.setTextAndValueAndCheck(str2, sb, z, i3, i != NotificationsSettingsActivity.this.channelsRow);
                    return;
                case 4:
                    if (i == NotificationsSettingsActivity.this.resetNotificationsSectionRow) {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                        return;
                    } else {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                        return;
                    }
                case 5:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(((BaseFragment) NotificationsSettingsActivity.this).currentAccount);
                    if (i != NotificationsSettingsActivity.this.callsRingtoneRow) {
                        if (i != NotificationsSettingsActivity.this.callsVibrateRow) {
                            if (i != NotificationsSettingsActivity.this.repeatRow) {
                                return;
                            }
                            int i4 = notificationsSettings3.getInt("repeat_messages", 60);
                            if (i4 == 0) {
                                str = LocaleController.getString("RepeatNotificationsNever", 2131627978);
                            } else if (i4 < 60) {
                                str = LocaleController.formatPluralString("Minutes", i4, new Object[0]);
                            } else {
                                str = LocaleController.formatPluralString("Hours", i4 / 60, new Object[0]);
                            }
                            textSettingsCell.setTextAndValue(LocaleController.getString("RepeatNotifications", 2131627977), str, false);
                            return;
                        }
                        int i5 = notificationsSettings3.getInt("vibrate_calls", 0);
                        if (i5 == 0) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628951), LocaleController.getString("VibrationDefault", 2131628952), true);
                            return;
                        } else if (i5 == 1) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628951), LocaleController.getString("Short", 2131628390), true);
                            return;
                        } else if (i5 == 2) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628951), LocaleController.getString("VibrationDisabled", 2131628953), true);
                            return;
                        } else if (i5 == 3) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628951), LocaleController.getString("Long", 2131626554), true);
                            return;
                        } else if (i5 != 4) {
                            return;
                        } else {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628951), LocaleController.getString("OnlyIfSilent", 2131627139), true);
                            return;
                        }
                    }
                    String string2 = notificationsSettings3.getString("CallsRingtone", LocaleController.getString("DefaultRingtone", 2131625383));
                    if (string2.equals("NoSound")) {
                        string2 = LocaleController.getString("NoSound", 2131626924);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("VoipSettingsRingtone", 2131629266), string2, false);
                    return;
                case 6:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i != NotificationsSettingsActivity.this.accountsInfoRow) {
                        return;
                    }
                    textInfoPrivacyCell.setText(LocaleController.getString("ShowNotificationsForInfo", 2131628407));
                    return;
                default:
                    return;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == NotificationsSettingsActivity.this.eventsSectionRow || i == NotificationsSettingsActivity.this.otherSectionRow || i == NotificationsSettingsActivity.this.resetSectionRow || i == NotificationsSettingsActivity.this.callsSectionRow || i == NotificationsSettingsActivity.this.badgeNumberSection || i == NotificationsSettingsActivity.this.inappSectionRow || i == NotificationsSettingsActivity.this.notificationsSectionRow || i == NotificationsSettingsActivity.this.accountsSectionRow) {
                return 0;
            }
            if (i == NotificationsSettingsActivity.this.inappSoundRow || i == NotificationsSettingsActivity.this.inappVibrateRow || i == NotificationsSettingsActivity.this.notificationsServiceConnectionRow || i == NotificationsSettingsActivity.this.inappPreviewRow || i == NotificationsSettingsActivity.this.contactJoinedRow || i == NotificationsSettingsActivity.this.pinnedMessageRow || i == NotificationsSettingsActivity.this.notificationsServiceRow || i == NotificationsSettingsActivity.this.badgeNumberMutedRow || i == NotificationsSettingsActivity.this.badgeNumberMessagesRow || i == NotificationsSettingsActivity.this.badgeNumberShowRow || i == NotificationsSettingsActivity.this.inappPriorityRow || i == NotificationsSettingsActivity.this.inchatSoundRow || i == NotificationsSettingsActivity.this.androidAutoAlertRow || i == NotificationsSettingsActivity.this.accountsAllRow) {
                return 1;
            }
            if (i == NotificationsSettingsActivity.this.resetNotificationsRow) {
                return 2;
            }
            if (i == NotificationsSettingsActivity.this.privateRow || i == NotificationsSettingsActivity.this.groupRow || i == NotificationsSettingsActivity.this.channelsRow) {
                return 3;
            }
            if (i == NotificationsSettingsActivity.this.eventsSection2Row || i == NotificationsSettingsActivity.this.notificationsSection2Row || i == NotificationsSettingsActivity.this.otherSection2Row || i == NotificationsSettingsActivity.this.resetSection2Row || i == NotificationsSettingsActivity.this.callsSection2Row || i == NotificationsSettingsActivity.this.badgeNumberSection2Row || i == NotificationsSettingsActivity.this.resetNotificationsSectionRow) {
                return 4;
            }
            return i == NotificationsSettingsActivity.this.accountsInfoRow ? 6 : 5;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextCheckCell.class, TextDetailSettingsCell.class, TextSettingsCell.class, NotificationsCheckCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteValueText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteLinkText"));
        return arrayList;
    }
}
