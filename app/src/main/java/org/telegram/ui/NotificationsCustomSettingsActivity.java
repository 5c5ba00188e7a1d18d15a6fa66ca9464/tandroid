package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
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
import android.provider.Settings;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatNotificationsPopupWrapper;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.NotificationsSettingsActivity;
import org.telegram.ui.ProfileNotificationsActivity;
/* loaded from: classes3.dex */
public class NotificationsCustomSettingsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private ListAdapter adapter;
    private int alertRow;
    private int alertSection2Row;
    private AnimatorSet animatorSet;
    private int currentType;
    private int deleteAllRow;
    private int deleteAllSectionRow;
    private EmptyTextProgressView emptyView;
    private ArrayList<NotificationsSettingsActivity.NotificationException> exceptions;
    private int exceptionsAddRow;
    private HashMap<Long, NotificationsSettingsActivity.NotificationException> exceptionsDict;
    private int exceptionsEndRow;
    private int exceptionsSection2Row;
    private int exceptionsStartRow;
    private int groupSection2Row;
    private RecyclerListView listView;
    private int messageLedRow;
    private int messagePopupNotificationRow;
    private int messagePriorityRow;
    private int messageSectionRow;
    private int messageSoundRow;
    private int messageVibrateRow;
    private int previewRow;
    private int rowCount;
    private SearchAdapter searchAdapter;
    private boolean searchWas;
    private boolean searching;

    public NotificationsCustomSettingsActivity(int i, ArrayList<NotificationsSettingsActivity.NotificationException> arrayList) {
        this(i, arrayList, false);
    }

    public NotificationsCustomSettingsActivity(int i, ArrayList<NotificationsSettingsActivity.NotificationException> arrayList, boolean z) {
        this.rowCount = 0;
        this.exceptionsDict = new HashMap<>();
        this.currentType = i;
        this.exceptions = arrayList;
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            NotificationsSettingsActivity.NotificationException notificationException = this.exceptions.get(i2);
            this.exceptionsDict.put(Long.valueOf(notificationException.did), notificationException);
        }
        if (z) {
            loadExceptions();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        updateRows(true);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.searching = false;
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        if (this.currentType == -1) {
            this.actionBar.setTitle(LocaleController.getString("NotificationsExceptions", 2131627031));
        } else {
            this.actionBar.setTitle(LocaleController.getString("Notifications", 2131627016));
        }
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList = this.exceptions;
        if (arrayList != null && !arrayList.isEmpty()) {
            this.actionBar.createMenu().addItem(0, 2131165456).setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass2()).setSearchFieldHint(LocaleController.getString("Search", 2131628092));
        }
        this.searchAdapter = new SearchAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context);
        this.emptyView = emptyTextProgressView;
        emptyTextProgressView.setTextSize(18);
        this.emptyView.setText(LocaleController.getString("NoExceptions", 2131626821));
        this.emptyView.showTextView();
        frameLayout2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setEmptyView(this.emptyView);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView2 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.adapter = listAdapter;
        recyclerListView2.setAdapter(listAdapter);
        this.listView.setOnItemClickListener(new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda9(this, context));
        this.listView.setOnScrollListener(new AnonymousClass4());
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.NotificationsCustomSettingsActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            NotificationsCustomSettingsActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                NotificationsCustomSettingsActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.NotificationsCustomSettingsActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        AnonymousClass2() {
            NotificationsCustomSettingsActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchExpand() {
            NotificationsCustomSettingsActivity.this.searching = true;
            NotificationsCustomSettingsActivity.this.emptyView.setShowAtCenter(true);
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchCollapse() {
            NotificationsCustomSettingsActivity.this.searchAdapter.searchDialogs(null);
            NotificationsCustomSettingsActivity.this.searching = false;
            NotificationsCustomSettingsActivity.this.searchWas = false;
            NotificationsCustomSettingsActivity.this.emptyView.setText(LocaleController.getString("NoExceptions", 2131626821));
            NotificationsCustomSettingsActivity.this.listView.setAdapter(NotificationsCustomSettingsActivity.this.adapter);
            NotificationsCustomSettingsActivity.this.adapter.notifyDataSetChanged();
            NotificationsCustomSettingsActivity.this.listView.setFastScrollVisible(true);
            NotificationsCustomSettingsActivity.this.listView.setVerticalScrollBarEnabled(false);
            NotificationsCustomSettingsActivity.this.emptyView.setShowAtCenter(false);
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onTextChanged(EditText editText) {
            if (NotificationsCustomSettingsActivity.this.searchAdapter == null) {
                return;
            }
            String obj = editText.getText().toString();
            if (obj.length() != 0) {
                NotificationsCustomSettingsActivity.this.searchWas = true;
                if (NotificationsCustomSettingsActivity.this.listView != null) {
                    NotificationsCustomSettingsActivity.this.emptyView.setText(LocaleController.getString("NoResult", 2131626858));
                    NotificationsCustomSettingsActivity.this.emptyView.showProgress();
                    NotificationsCustomSettingsActivity.this.listView.setAdapter(NotificationsCustomSettingsActivity.this.searchAdapter);
                    NotificationsCustomSettingsActivity.this.searchAdapter.notifyDataSetChanged();
                    NotificationsCustomSettingsActivity.this.listView.setFastScrollVisible(false);
                    NotificationsCustomSettingsActivity.this.listView.setVerticalScrollBarEnabled(true);
                }
            }
            NotificationsCustomSettingsActivity.this.searchAdapter.searchDialogs(obj);
        }
    }

    public /* synthetic */ void lambda$createView$8(Context context, View view, int i, float f, float f2) {
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList;
        boolean z;
        NotificationsSettingsActivity.NotificationException notificationException;
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList2;
        NotificationsSettingsActivity.NotificationException notificationException2;
        long j;
        boolean z2;
        if (getParentActivity() == null) {
            return;
        }
        boolean z3 = false;
        if (this.listView.getAdapter() == this.searchAdapter || (i >= this.exceptionsStartRow && i < this.exceptionsEndRow)) {
            RecyclerView.Adapter adapter = this.listView.getAdapter();
            SearchAdapter searchAdapter = this.searchAdapter;
            if (adapter == searchAdapter) {
                Object object = searchAdapter.getObject(i);
                if (!(object instanceof NotificationsSettingsActivity.NotificationException)) {
                    boolean z4 = object instanceof TLRPC$User;
                    if (z4) {
                        j = ((TLRPC$User) object).id;
                    } else {
                        j = -((TLRPC$Chat) object).id;
                    }
                    if (this.exceptionsDict.containsKey(Long.valueOf(j))) {
                        notificationException2 = this.exceptionsDict.get(Long.valueOf(j));
                    } else {
                        NotificationsSettingsActivity.NotificationException notificationException3 = new NotificationsSettingsActivity.NotificationException();
                        notificationException3.did = j;
                        if (z4) {
                            notificationException3.did = ((TLRPC$User) object).id;
                        } else {
                            notificationException3.did = -((TLRPC$Chat) object).id;
                        }
                        notificationException2 = notificationException3;
                        z3 = true;
                    }
                    arrayList2 = this.exceptions;
                } else {
                    arrayList2 = this.searchAdapter.searchResult;
                    notificationException2 = (NotificationsSettingsActivity.NotificationException) object;
                }
                notificationException = notificationException2;
                arrayList = arrayList2;
                z = z3;
            } else {
                ArrayList<NotificationsSettingsActivity.NotificationException> arrayList3 = this.exceptions;
                int i2 = i - this.exceptionsStartRow;
                if (i2 < 0 || i2 >= arrayList3.size()) {
                    return;
                }
                arrayList = arrayList3;
                notificationException = arrayList3.get(i2);
                z = false;
            }
            if (notificationException == null) {
                return;
            }
            long j2 = notificationException.did;
            ChatNotificationsPopupWrapper chatNotificationsPopupWrapper = new ChatNotificationsPopupWrapper(context, this.currentAccount, null, true, true, new AnonymousClass3(j2, NotificationsController.getInstance(this.currentAccount).isGlobalNotificationsEnabled(j2), notificationException, z, i, arrayList), getResourceProvider());
            chatNotificationsPopupWrapper.lambda$update$10(j2);
            chatNotificationsPopupWrapper.showAsOptions(this, view, f, f2);
            return;
        }
        if (i == this.exceptionsAddRow) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("onlySelect", true);
            bundle.putBoolean("checkCanWrite", false);
            int i3 = this.currentType;
            if (i3 == 0) {
                bundle.putInt("dialogsType", 6);
            } else if (i3 == 2) {
                bundle.putInt("dialogsType", 5);
            } else {
                bundle.putInt("dialogsType", 4);
            }
            DialogsActivity dialogsActivity = new DialogsActivity(bundle);
            dialogsActivity.setDelegate(new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda10(this));
            presentFragment(dialogsActivity);
        } else if (i == this.deleteAllRow) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("NotificationsDeleteAllExceptionTitle", 2131627029));
            builder.setMessage(LocaleController.getString("NotificationsDeleteAllExceptionAlert", 2131627028));
            builder.setPositiveButton(LocaleController.getString("Delete", 2131625368), new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda0(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
            AlertDialog create = builder.create();
            showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
            }
        } else if (i == this.alertRow) {
            boolean isGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(this.currentType);
            NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(i);
            if (!isGlobalNotificationsEnabled) {
                getNotificationsController().setGlobalNotificationsEnabled(this.currentType, 0);
                notificationsCheckCell.setChecked(true);
                if (findViewHolderForAdapterPosition != null) {
                    this.adapter.onBindViewHolder(findViewHolderForAdapterPosition, i);
                }
                checkRowsEnabled();
            } else {
                AlertsCreator.showCustomNotificationsDialog(this, 0L, this.currentType, this.exceptions, this.currentAccount, new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda7(this, notificationsCheckCell, findViewHolderForAdapterPosition, i));
            }
            z3 = isGlobalNotificationsEnabled;
        } else if (i == this.previewRow) {
            if (!view.isEnabled()) {
                return;
            }
            SharedPreferences notificationsSettings = getNotificationsSettings();
            SharedPreferences.Editor edit = notificationsSettings.edit();
            int i4 = this.currentType;
            if (i4 == 1) {
                z2 = notificationsSettings.getBoolean("EnablePreviewAll", true);
                edit.putBoolean("EnablePreviewAll", !z2);
            } else if (i4 == 0) {
                z2 = notificationsSettings.getBoolean("EnablePreviewGroup", true);
                edit.putBoolean("EnablePreviewGroup", !z2);
            } else {
                z2 = notificationsSettings.getBoolean("EnablePreviewChannel", true);
                edit.putBoolean("EnablePreviewChannel", !z2);
            }
            z3 = z2;
            edit.commit();
            getNotificationsController().updateServerNotificationsSettings(this.currentType);
        } else if (i == this.messageSoundRow) {
            if (!view.isEnabled()) {
                return;
            }
            try {
                Bundle bundle2 = new Bundle();
                bundle2.putInt("type", this.currentType);
                presentFragment(new NotificationsSoundActivity(bundle2, getResourceProvider()));
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else if (i == this.messageLedRow) {
            if (!view.isEnabled()) {
                return;
            }
            showDialog(AlertsCreator.createColorSelectDialog(getParentActivity(), 0L, this.currentType, new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda4(this, i)));
        } else if (i == this.messagePopupNotificationRow) {
            if (!view.isEnabled()) {
                return;
            }
            showDialog(AlertsCreator.createPopupSelectDialog(getParentActivity(), this.currentType, new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda2(this, i)));
        } else if (i == this.messageVibrateRow) {
            if (!view.isEnabled()) {
                return;
            }
            int i5 = this.currentType;
            showDialog(AlertsCreator.createVibrationSelectDialog(getParentActivity(), 0L, i5 == 1 ? "vibrate_messages" : i5 == 0 ? "vibrate_group" : "vibrate_channel", new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda3(this, i)));
        } else if (i == this.messagePriorityRow) {
            if (!view.isEnabled()) {
                return;
            }
            showDialog(AlertsCreator.createPrioritySelectDialog(getParentActivity(), 0L, this.currentType, new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda5(this, i)));
        }
        if (!(view instanceof TextCheckCell)) {
            return;
        }
        ((TextCheckCell) view).setChecked(!z3);
    }

    /* renamed from: org.telegram.ui.NotificationsCustomSettingsActivity$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements ChatNotificationsPopupWrapper.Callback {
        final /* synthetic */ ArrayList val$arrayList;
        final /* synthetic */ boolean val$defaultEnabled;
        final /* synthetic */ long val$did;
        final /* synthetic */ NotificationsSettingsActivity.NotificationException val$exception;
        final /* synthetic */ boolean val$newException;
        final /* synthetic */ int val$position;

        @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
        public /* synthetic */ void dismiss() {
            ChatNotificationsPopupWrapper.Callback.CC.$default$dismiss(this);
        }

        AnonymousClass3(long j, boolean z, NotificationsSettingsActivity.NotificationException notificationException, boolean z2, int i, ArrayList arrayList) {
            NotificationsCustomSettingsActivity.this = r1;
            this.val$did = j;
            this.val$defaultEnabled = z;
            this.val$exception = notificationException;
            this.val$newException = z2;
            this.val$position = i;
            this.val$arrayList = arrayList;
        }

        @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
        public void toggleSound() {
            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) NotificationsCustomSettingsActivity.this).currentAccount);
            boolean z = !notificationsSettings.getBoolean("sound_enabled_" + this.val$did, true);
            notificationsSettings.edit().putBoolean("sound_enabled_" + this.val$did, z).apply();
            if (BulletinFactory.canShowBulletin(NotificationsCustomSettingsActivity.this)) {
                NotificationsCustomSettingsActivity notificationsCustomSettingsActivity = NotificationsCustomSettingsActivity.this;
                BulletinFactory.createSoundEnabledBulletin(notificationsCustomSettingsActivity, !z, notificationsCustomSettingsActivity.getResourceProvider()).show();
            }
        }

        @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
        public void muteFor(int i) {
            if (i != 0) {
                NotificationsCustomSettingsActivity.this.getNotificationsController().muteUntil(this.val$did, i);
                if (BulletinFactory.canShowBulletin(NotificationsCustomSettingsActivity.this)) {
                    NotificationsCustomSettingsActivity notificationsCustomSettingsActivity = NotificationsCustomSettingsActivity.this;
                    BulletinFactory.createMuteBulletin(notificationsCustomSettingsActivity, 5, i, notificationsCustomSettingsActivity.getResourceProvider()).show();
                }
            } else {
                if (NotificationsCustomSettingsActivity.this.getMessagesController().isDialogMuted(this.val$did)) {
                    toggleMute();
                }
                if (BulletinFactory.canShowBulletin(NotificationsCustomSettingsActivity.this)) {
                    NotificationsCustomSettingsActivity notificationsCustomSettingsActivity2 = NotificationsCustomSettingsActivity.this;
                    BulletinFactory.createMuteBulletin(notificationsCustomSettingsActivity2, 4, i, notificationsCustomSettingsActivity2.getResourceProvider()).show();
                }
            }
            update();
        }

        @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
        public void showCustomize() {
            if (this.val$did != 0) {
                Bundle bundle = new Bundle();
                bundle.putLong("dialog_id", this.val$did);
                ProfileNotificationsActivity profileNotificationsActivity = new ProfileNotificationsActivity(bundle);
                profileNotificationsActivity.setDelegate(new AnonymousClass1());
                NotificationsCustomSettingsActivity.this.presentFragment(profileNotificationsActivity);
            }
        }

        /* renamed from: org.telegram.ui.NotificationsCustomSettingsActivity$3$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 implements ProfileNotificationsActivity.ProfileNotificationsActivityDelegate {
            @Override // org.telegram.ui.ProfileNotificationsActivity.ProfileNotificationsActivityDelegate
            public void didCreateNewException(NotificationsSettingsActivity.NotificationException notificationException) {
            }

            AnonymousClass1() {
                AnonymousClass3.this = r1;
            }

            @Override // org.telegram.ui.ProfileNotificationsActivity.ProfileNotificationsActivityDelegate
            public void didRemoveException(long j) {
                AnonymousClass3.this.setDefault();
            }
        }

        @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
        public void toggleMute() {
            NotificationsCustomSettingsActivity.this.getNotificationsController().muteDialog(this.val$did, !NotificationsCustomSettingsActivity.this.getMessagesController().isDialogMuted(this.val$did));
            NotificationsCustomSettingsActivity notificationsCustomSettingsActivity = NotificationsCustomSettingsActivity.this;
            BulletinFactory.createMuteBulletin(notificationsCustomSettingsActivity, notificationsCustomSettingsActivity.getMessagesController().isDialogMuted(this.val$did), null).show();
            update();
        }

        private void update() {
            if (NotificationsCustomSettingsActivity.this.getMessagesController().isDialogMuted(this.val$did) != this.val$defaultEnabled) {
                setDefault();
            } else {
                setNotDefault();
            }
        }

        private void setNotDefault() {
            SharedPreferences notificationsSettings = NotificationsCustomSettingsActivity.this.getNotificationsSettings();
            NotificationsSettingsActivity.NotificationException notificationException = this.val$exception;
            notificationException.hasCustom = notificationsSettings.getBoolean("custom_" + this.val$exception.did, false);
            NotificationsSettingsActivity.NotificationException notificationException2 = this.val$exception;
            notificationException2.notify = notificationsSettings.getInt("notify2_" + this.val$exception.did, 0);
            if (this.val$exception.notify != 0) {
                int i = notificationsSettings.getInt("notifyuntil_" + this.val$exception.did, -1);
                if (i != -1) {
                    this.val$exception.muteUntil = i;
                }
            }
            if (this.val$newException) {
                NotificationsCustomSettingsActivity.this.exceptions.add(this.val$exception);
                NotificationsCustomSettingsActivity.this.exceptionsDict.put(Long.valueOf(this.val$exception.did), this.val$exception);
                NotificationsCustomSettingsActivity.this.updateRows(true);
            } else {
                NotificationsCustomSettingsActivity.this.listView.getAdapter().notifyItemChanged(this.val$position);
            }
            ((BaseFragment) NotificationsCustomSettingsActivity.this).actionBar.closeSearchField();
        }

        public void setDefault() {
            int indexOf;
            if (this.val$newException) {
                return;
            }
            if (this.val$arrayList != NotificationsCustomSettingsActivity.this.exceptions && (indexOf = NotificationsCustomSettingsActivity.this.exceptions.indexOf(this.val$exception)) >= 0) {
                NotificationsCustomSettingsActivity.this.exceptions.remove(indexOf);
                NotificationsCustomSettingsActivity.this.exceptionsDict.remove(Long.valueOf(this.val$exception.did));
            }
            this.val$arrayList.remove(this.val$exception);
            if (this.val$arrayList == NotificationsCustomSettingsActivity.this.exceptions) {
                if (NotificationsCustomSettingsActivity.this.exceptionsAddRow != -1 && this.val$arrayList.isEmpty()) {
                    NotificationsCustomSettingsActivity.this.listView.getAdapter().notifyItemChanged(NotificationsCustomSettingsActivity.this.exceptionsAddRow);
                    NotificationsCustomSettingsActivity.this.listView.getAdapter().notifyItemRemoved(NotificationsCustomSettingsActivity.this.deleteAllRow);
                    NotificationsCustomSettingsActivity.this.listView.getAdapter().notifyItemRemoved(NotificationsCustomSettingsActivity.this.deleteAllSectionRow);
                }
                NotificationsCustomSettingsActivity.this.listView.getAdapter().notifyItemRemoved(this.val$position);
                NotificationsCustomSettingsActivity.this.updateRows(false);
                NotificationsCustomSettingsActivity.this.checkRowsEnabled();
            } else {
                NotificationsCustomSettingsActivity.this.updateRows(true);
                NotificationsCustomSettingsActivity.this.searchAdapter.notifyItemChanged(this.val$position);
            }
            ((BaseFragment) NotificationsCustomSettingsActivity.this).actionBar.closeSearchField();
        }
    }

    public /* synthetic */ void lambda$createView$1(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putLong("dialog_id", ((Long) arrayList.get(0)).longValue());
        bundle.putBoolean("exception", true);
        ProfileNotificationsActivity profileNotificationsActivity = new ProfileNotificationsActivity(bundle, getResourceProvider());
        profileNotificationsActivity.setDelegate(new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda11(this));
        presentFragment(profileNotificationsActivity, true);
    }

    public /* synthetic */ void lambda$createView$0(NotificationsSettingsActivity.NotificationException notificationException) {
        this.exceptions.add(0, notificationException);
        updateRows(true);
    }

    public /* synthetic */ void lambda$createView$2(DialogInterface dialogInterface, int i) {
        SharedPreferences.Editor edit = getNotificationsSettings().edit();
        int size = this.exceptions.size();
        for (int i2 = 0; i2 < size; i2++) {
            NotificationsSettingsActivity.NotificationException notificationException = this.exceptions.get(i2);
            SharedPreferences.Editor remove = edit.remove("notify2_" + notificationException.did);
            remove.remove("custom_" + notificationException.did);
            getMessagesStorage().setDialogFlags(notificationException.did, 0L);
            TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(notificationException.did);
            if (tLRPC$Dialog != null) {
                tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
            }
        }
        edit.commit();
        int size2 = this.exceptions.size();
        for (int i3 = 0; i3 < size2; i3++) {
            getNotificationsController().updateServerNotificationsSettings(this.exceptions.get(i3).did, false);
        }
        this.exceptions.clear();
        this.exceptionsDict.clear();
        updateRows(true);
        getNotificationCenter().postNotificationName(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
    }

    public /* synthetic */ void lambda$createView$3(NotificationsCheckCell notificationsCheckCell, RecyclerView.ViewHolder viewHolder, int i, int i2) {
        int i3;
        SharedPreferences notificationsSettings = getNotificationsSettings();
        int i4 = this.currentType;
        int i5 = 0;
        if (i4 == 1) {
            i3 = notificationsSettings.getInt("EnableAll2", 0);
        } else if (i4 == 0) {
            i3 = notificationsSettings.getInt("EnableGroup2", 0);
        } else {
            i3 = notificationsSettings.getInt("EnableChannel2", 0);
        }
        int currentTime = getConnectionsManager().getCurrentTime();
        if (i3 >= currentTime && i3 - 31536000 < currentTime) {
            i5 = 2;
        }
        notificationsCheckCell.setChecked(getNotificationsController().isGlobalNotificationsEnabled(this.currentType), i5);
        if (viewHolder != null) {
            this.adapter.onBindViewHolder(viewHolder, i);
        }
        checkRowsEnabled();
    }

    public /* synthetic */ void lambda$createView$4(int i) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(i);
        if (findViewHolderForAdapterPosition != null) {
            this.adapter.onBindViewHolder(findViewHolderForAdapterPosition, i);
        }
    }

    public /* synthetic */ void lambda$createView$5(int i) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(i);
        if (findViewHolderForAdapterPosition != null) {
            this.adapter.onBindViewHolder(findViewHolderForAdapterPosition, i);
        }
    }

    public /* synthetic */ void lambda$createView$6(int i) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(i);
        if (findViewHolderForAdapterPosition != null) {
            this.adapter.onBindViewHolder(findViewHolderForAdapterPosition, i);
        }
    }

    public /* synthetic */ void lambda$createView$7(int i) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(i);
        if (findViewHolderForAdapterPosition != null) {
            this.adapter.onBindViewHolder(findViewHolderForAdapterPosition, i);
        }
    }

    /* renamed from: org.telegram.ui.NotificationsCustomSettingsActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends RecyclerView.OnScrollListener {
        AnonymousClass4() {
            NotificationsCustomSettingsActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                AndroidUtilities.hideKeyboard(NotificationsCustomSettingsActivity.this.getParentActivity().getCurrentFocus());
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
        }
    }

    public void checkRowsEnabled() {
        if (!this.exceptions.isEmpty()) {
            return;
        }
        int childCount = this.listView.getChildCount();
        ArrayList<Animator> arrayList = new ArrayList<>();
        boolean isGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(this.currentType);
        for (int i = 0; i < childCount; i++) {
            RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.getChildViewHolder(this.listView.getChildAt(i));
            int itemViewType = holder.getItemViewType();
            if (itemViewType == 0) {
                HeaderCell headerCell = (HeaderCell) holder.itemView;
                if (holder.getAdapterPosition() == this.messageSectionRow) {
                    headerCell.setEnabled(isGlobalNotificationsEnabled, arrayList);
                }
            } else if (itemViewType == 1) {
                ((TextCheckCell) holder.itemView).setEnabled(isGlobalNotificationsEnabled, arrayList);
            } else if (itemViewType == 3) {
                ((TextColorCell) holder.itemView).setEnabled(isGlobalNotificationsEnabled, arrayList);
            } else if (itemViewType == 5) {
                ((TextSettingsCell) holder.itemView).setEnabled(isGlobalNotificationsEnabled, arrayList);
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.animatorSet = animatorSet2;
        animatorSet2.playTogether(arrayList);
        this.animatorSet.addListener(new AnonymousClass5());
        this.animatorSet.setDuration(150L);
        this.animatorSet.start();
    }

    /* renamed from: org.telegram.ui.NotificationsCustomSettingsActivity$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends AnimatorListenerAdapter {
        AnonymousClass5() {
            NotificationsCustomSettingsActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(NotificationsCustomSettingsActivity.this.animatorSet)) {
                NotificationsCustomSettingsActivity.this.animatorSet = null;
            }
        }
    }

    private void loadExceptions() {
        getMessagesStorage().getStorageQueue().postRunnable(new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda1(this));
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0117, code lost:
        if (r4.deleted != false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0141, code lost:
        if (r4.deleted != false) goto L50;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0246  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0260 A[LOOP:3: B:106:0x025e->B:107:0x0260, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0279  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0207  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadExceptions$10() {
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
        ArrayList arrayList5;
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = new ArrayList();
        LongSparseArray longSparseArray = new LongSparseArray();
        ArrayList<Long> arrayList9 = new ArrayList<>();
        ArrayList arrayList10 = new ArrayList();
        ArrayList arrayList11 = new ArrayList();
        ArrayList<TLRPC$User> arrayList12 = new ArrayList<>();
        ArrayList<TLRPC$Chat> arrayList13 = new ArrayList<>();
        ArrayList<TLRPC$EncryptedChat> arrayList14 = new ArrayList<>();
        long j2 = getUserConfig().clientUserId;
        SharedPreferences notificationsSettings = getNotificationsSettings();
        Map<String, ?> all = notificationsSettings.getAll();
        Iterator<Map.Entry<String, ?>> it = all.entrySet().iterator();
        while (true) {
            arrayList = arrayList13;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<String, ?> next = it.next();
            String key = next.getKey();
            Iterator<Map.Entry<String, ?>> it2 = it;
            if (key.startsWith("notify2_")) {
                arrayList4 = arrayList12;
                String replace = key.replace("notify2_", "");
                arrayList5 = arrayList7;
                ArrayList arrayList15 = arrayList8;
                long longValue = Utilities.parseLong(replace).longValue();
                if (longValue == 0 || longValue == j2) {
                    j = j2;
                } else {
                    NotificationsSettingsActivity.NotificationException notificationException = new NotificationsSettingsActivity.NotificationException();
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
                        TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChatId));
                        if (encryptedChat == null) {
                            arrayList11.add(Integer.valueOf(encryptedChatId));
                            longSparseArray.put(longValue, notificationException);
                        } else {
                            TLRPC$User user = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                            if (user == null) {
                                arrayList9.add(Long.valueOf(encryptedChat.user_id));
                                longSparseArray.put(encryptedChat.user_id, notificationException);
                            }
                        }
                        arrayList6.add(notificationException);
                    } else {
                        if (DialogObject.isUserDialog(longValue)) {
                            TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(longValue));
                            if (user2 == null) {
                                arrayList9.add(Long.valueOf(longValue));
                                longSparseArray.put(longValue, notificationException);
                            }
                            arrayList6.add(notificationException);
                        } else {
                            long j3 = -longValue;
                            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(j3));
                            if (chat == null) {
                                arrayList10.add(Long.valueOf(j3));
                                longSparseArray.put(longValue, notificationException);
                            } else if (!chat.left && !chat.kicked && chat.migrated_to == null) {
                                if (ChatObject.isChannel(chat) && !chat.megagroup) {
                                    arrayList8 = arrayList15;
                                    arrayList8.add(notificationException);
                                } else {
                                    arrayList8 = arrayList15;
                                    arrayList5.add(notificationException);
                                }
                            }
                        }
                        arrayList7 = arrayList5;
                        arrayList13 = arrayList;
                        it = it2;
                        arrayList12 = arrayList4;
                        arrayList8 = arrayList15;
                        j2 = j;
                    }
                }
                arrayList8 = arrayList15;
            } else {
                arrayList4 = arrayList12;
                arrayList5 = arrayList7;
                j = j2;
            }
            arrayList7 = arrayList5;
            arrayList13 = arrayList;
            it = it2;
            arrayList12 = arrayList4;
            j2 = j;
        }
        ArrayList<TLRPC$User> arrayList16 = arrayList12;
        ArrayList arrayList17 = arrayList7;
        if (longSparseArray.size() != 0) {
            try {
                if (!arrayList11.isEmpty()) {
                    getMessagesStorage().getEncryptedChatsInternal(TextUtils.join(",", arrayList11), arrayList14, arrayList9);
                }
                if (!arrayList9.isEmpty()) {
                    try {
                        arrayList3 = arrayList16;
                    } catch (Exception e2) {
                        e = e2;
                        arrayList3 = arrayList16;
                        arrayList2 = arrayList;
                        FileLog.e(e);
                        size = arrayList2.size();
                        while (i < size) {
                        }
                        size2 = arrayList3.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList14.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (r11 < size4) {
                        }
                        AndroidUtilities.runOnUIThread(new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda6(this, arrayList3, arrayList2, arrayList14, arrayList6, arrayList17, arrayList8));
                    }
                    try {
                        getMessagesStorage().getUsersInternal(TextUtils.join(",", arrayList9), arrayList3);
                    } catch (Exception e3) {
                        e = e3;
                        arrayList2 = arrayList;
                        FileLog.e(e);
                        size = arrayList2.size();
                        while (i < size) {
                        }
                        size2 = arrayList3.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList14.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (r11 < size4) {
                        }
                        AndroidUtilities.runOnUIThread(new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda6(this, arrayList3, arrayList2, arrayList14, arrayList6, arrayList17, arrayList8));
                    }
                } else {
                    arrayList3 = arrayList16;
                }
                if (!arrayList10.isEmpty()) {
                    arrayList2 = arrayList;
                    try {
                        getMessagesStorage().getChatsInternal(TextUtils.join(",", arrayList10), arrayList2);
                    } catch (Exception e4) {
                        e = e4;
                        FileLog.e(e);
                        size = arrayList2.size();
                        while (i < size) {
                        }
                        size2 = arrayList3.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList14.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (r11 < size4) {
                        }
                        AndroidUtilities.runOnUIThread(new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda6(this, arrayList3, arrayList2, arrayList14, arrayList6, arrayList17, arrayList8));
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
                    NotificationsSettingsActivity.NotificationException notificationException2 = (NotificationsSettingsActivity.NotificationException) longSparseArray.get(-tLRPC$Chat.id);
                    longSparseArray.remove(-tLRPC$Chat.id);
                    if (notificationException2 != null) {
                        if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                            arrayList8.add(notificationException2);
                        } else {
                            arrayList17.add(notificationException2);
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
            size3 = arrayList14.size();
            for (i3 = 0; i3 < size3; i3++) {
                longSparseArray.remove(DialogObject.makeEncryptedDialogId(arrayList14.get(i3).id));
            }
            size4 = longSparseArray.size();
            for (int i4 = 0; i4 < size4; i4++) {
                if (DialogObject.isChatDialog(longSparseArray.keyAt(i4))) {
                    arrayList17.remove(longSparseArray.valueAt(i4));
                    arrayList8.remove(longSparseArray.valueAt(i4));
                } else {
                    arrayList6.remove(longSparseArray.valueAt(i4));
                }
            }
        } else {
            arrayList2 = arrayList;
            arrayList3 = arrayList16;
        }
        AndroidUtilities.runOnUIThread(new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda6(this, arrayList3, arrayList2, arrayList14, arrayList6, arrayList17, arrayList8));
    }

    public /* synthetic */ void lambda$loadExceptions$9(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, ArrayList arrayList5, ArrayList arrayList6) {
        getMessagesController().putUsers(arrayList, true);
        getMessagesController().putChats(arrayList2, true);
        getMessagesController().putEncryptedChats(arrayList3, true);
        int i = this.currentType;
        if (i == 1) {
            this.exceptions = arrayList4;
        } else if (i == 0) {
            this.exceptions = arrayList5;
        } else {
            this.exceptions = arrayList6;
        }
        updateRows(true);
    }

    public void updateRows(boolean z) {
        ListAdapter listAdapter;
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList;
        this.rowCount = 0;
        int i = this.currentType;
        if (i != -1) {
            int i2 = 0 + 1;
            this.rowCount = i2;
            this.alertRow = 0;
            int i3 = i2 + 1;
            this.rowCount = i3;
            this.alertSection2Row = i2;
            int i4 = i3 + 1;
            this.rowCount = i4;
            this.messageSectionRow = i3;
            int i5 = i4 + 1;
            this.rowCount = i5;
            this.previewRow = i4;
            int i6 = i5 + 1;
            this.rowCount = i6;
            this.messageLedRow = i5;
            int i7 = i6 + 1;
            this.rowCount = i7;
            this.messageVibrateRow = i6;
            if (i == 2) {
                this.messagePopupNotificationRow = -1;
            } else {
                this.rowCount = i7 + 1;
                this.messagePopupNotificationRow = i7;
            }
            int i8 = this.rowCount;
            int i9 = i8 + 1;
            this.rowCount = i9;
            this.messageSoundRow = i8;
            if (Build.VERSION.SDK_INT >= 21) {
                this.rowCount = i9 + 1;
                this.messagePriorityRow = i9;
            } else {
                this.messagePriorityRow = -1;
            }
            int i10 = this.rowCount;
            int i11 = i10 + 1;
            this.rowCount = i11;
            this.groupSection2Row = i10;
            this.rowCount = i11 + 1;
            this.exceptionsAddRow = i11;
        } else {
            this.alertRow = -1;
            this.alertSection2Row = -1;
            this.messageSectionRow = -1;
            this.previewRow = -1;
            this.messageLedRow = -1;
            this.messageVibrateRow = -1;
            this.messagePopupNotificationRow = -1;
            this.messageSoundRow = -1;
            this.messagePriorityRow = -1;
            this.groupSection2Row = -1;
            this.exceptionsAddRow = -1;
        }
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList2 = this.exceptions;
        if (arrayList2 != null && !arrayList2.isEmpty()) {
            int i12 = this.rowCount;
            this.exceptionsStartRow = i12;
            int size = i12 + this.exceptions.size();
            this.rowCount = size;
            this.exceptionsEndRow = size;
        } else {
            this.exceptionsStartRow = -1;
            this.exceptionsEndRow = -1;
        }
        if (this.currentType != -1 || ((arrayList = this.exceptions) != null && !arrayList.isEmpty())) {
            int i13 = this.rowCount;
            this.rowCount = i13 + 1;
            this.exceptionsSection2Row = i13;
        } else {
            this.exceptionsSection2Row = -1;
        }
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList3 = this.exceptions;
        if (arrayList3 != null && !arrayList3.isEmpty()) {
            int i14 = this.rowCount;
            int i15 = i14 + 1;
            this.rowCount = i15;
            this.deleteAllRow = i14;
            this.rowCount = i15 + 1;
            this.deleteAllSectionRow = i15;
        } else {
            this.deleteAllRow = -1;
            this.deleteAllSectionRow = -1;
        }
        if (!z || (listAdapter = this.adapter) == null) {
            return;
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        Ringtone ringtone;
        if (i2 == -1) {
            Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            String str = null;
            if (uri != null && (ringtone = RingtoneManager.getRingtone(getParentActivity(), uri)) != null) {
                if (uri.equals(Settings.System.DEFAULT_NOTIFICATION_URI)) {
                    str = LocaleController.getString("SoundDefault", 2131628387);
                } else {
                    str = ringtone.getTitle(getParentActivity());
                }
                ringtone.stop();
            }
            SharedPreferences.Editor edit = getNotificationsSettings().edit();
            int i3 = this.currentType;
            if (i3 == 1) {
                if (str != null && uri != null) {
                    edit.putString("GlobalSound", str);
                    edit.putString("GlobalSoundPath", uri.toString());
                } else {
                    edit.putString("GlobalSound", "NoSound");
                    edit.putString("GlobalSoundPath", "NoSound");
                }
            } else if (i3 == 0) {
                if (str != null && uri != null) {
                    edit.putString("GroupSound", str);
                    edit.putString("GroupSoundPath", uri.toString());
                } else {
                    edit.putString("GroupSound", "NoSound");
                    edit.putString("GroupSoundPath", "NoSound");
                }
            } else if (i3 == 2) {
                if (str != null && uri != null) {
                    edit.putString("ChannelSound", str);
                    edit.putString("ChannelSoundPath", uri.toString());
                } else {
                    edit.putString("ChannelSound", "NoSound");
                    edit.putString("ChannelSoundPath", "NoSound");
                }
            }
            getNotificationsController().deleteNotificationChannelGlobal(this.currentType);
            edit.commit();
            getNotificationsController().updateServerNotificationsSettings(this.currentType);
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(i);
            if (findViewHolderForAdapterPosition == null) {
                return;
            }
            this.adapter.onBindViewHolder(findViewHolderForAdapterPosition, i);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        getNotificationCenter().addObserver(this, NotificationCenter.notificationsSettingsUpdated);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        getNotificationCenter().removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        ListAdapter listAdapter;
        if (i != NotificationCenter.notificationsSettingsUpdated || (listAdapter = this.adapter) == null) {
            return;
        }
        listAdapter.notifyDataSetChanged();
    }

    /* loaded from: classes3.dex */
    public class SearchAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        private SearchAdapterHelper searchAdapterHelper;
        private ArrayList<NotificationsSettingsActivity.NotificationException> searchResult = new ArrayList<>();
        private ArrayList<CharSequence> searchResultNames = new ArrayList<>();
        private Runnable searchRunnable;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public SearchAdapter(Context context) {
            NotificationsCustomSettingsActivity.this = r1;
            this.mContext = context;
            SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(true);
            this.searchAdapterHelper = searchAdapterHelper;
            searchAdapterHelper.setDelegate(new NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda4(this));
        }

        public /* synthetic */ void lambda$new$0(int i) {
            if (this.searchRunnable == null && !this.searchAdapterHelper.isSearchInProgress()) {
                NotificationsCustomSettingsActivity.this.emptyView.showTextView();
            }
            notifyDataSetChanged();
        }

        public void searchDialogs(String str) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (str == null) {
                this.searchResult.clear();
                this.searchResultNames.clear();
                this.searchAdapterHelper.mergeResults(null);
                this.searchAdapterHelper.queryServerSearch(null, true, NotificationsCustomSettingsActivity.this.currentType != 1, true, false, false, 0L, false, 0, 0);
                notifyDataSetChanged();
                return;
            }
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda1 notificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda1 = new NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda1(this, str);
            this.searchRunnable = notificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda1;
            dispatchQueue.postRunnable(notificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda1, 300L);
        }

        /* renamed from: processSearch */
        public void lambda$searchDialogs$1(String str) {
            AndroidUtilities.runOnUIThread(new NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda0(this, str));
        }

        public /* synthetic */ void lambda$processSearch$3(String str) {
            this.searchAdapterHelper.queryServerSearch(str, true, NotificationsCustomSettingsActivity.this.currentType != 1, true, false, false, 0L, false, 0, 0);
            Utilities.searchQueue.postRunnable(new NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda2(this, str, new ArrayList(NotificationsCustomSettingsActivity.this.exceptions)));
        }

        /* JADX WARN: Code restructure failed: missing block: B:60:0x0169, code lost:
            if (r10[r5].contains(" " + r15) == false) goto L62;
         */
        /* JADX WARN: Code restructure failed: missing block: B:66:0x0189, code lost:
            if (r6.contains(" " + r15) != false) goto L67;
         */
        /* JADX WARN: Removed duplicated region for block: B:50:0x0130  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x0140  */
        /* JADX WARN: Removed duplicated region for block: B:81:0x01e0 A[LOOP:1: B:54:0x013e->B:81:0x01e0, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:91:0x01a1 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$processSearch$2(String str, ArrayList arrayList) {
            String[] strArr;
            int i;
            int i2;
            char c;
            TLRPC$User tLRPC$User;
            String translitString;
            int i3;
            int i4;
            char c2;
            char c3;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                return;
            }
            String translitString2 = LocaleController.getInstance().getTranslitString(lowerCase);
            if (lowerCase.equals(translitString2) || translitString2.length() == 0) {
                translitString2 = null;
            }
            int i5 = (translitString2 != null ? 1 : 0) + 1;
            String[] strArr2 = new String[i5];
            strArr2[0] = lowerCase;
            if (translitString2 != null) {
                strArr2[1] = translitString2;
            }
            ArrayList<Object> arrayList2 = new ArrayList<>();
            ArrayList<NotificationsSettingsActivity.NotificationException> arrayList3 = new ArrayList<>();
            ArrayList<CharSequence> arrayList4 = new ArrayList<>();
            String[] strArr3 = new String[2];
            int i6 = 0;
            while (i6 < arrayList.size()) {
                NotificationsSettingsActivity.NotificationException notificationException = (NotificationsSettingsActivity.NotificationException) arrayList.get(i6);
                if (DialogObject.isEncryptedDialog(notificationException.did)) {
                    TLRPC$EncryptedChat encryptedChat = NotificationsCustomSettingsActivity.this.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(notificationException.did)));
                    if (encryptedChat != null) {
                        i2 = i5;
                        TLRPC$User user = NotificationsCustomSettingsActivity.this.getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                        if (user != null) {
                            strArr3[0] = ContactsController.formatName(user.first_name, user.last_name);
                            strArr3[1] = user.username;
                        }
                    } else {
                        i2 = i5;
                    }
                } else {
                    i2 = i5;
                    if (DialogObject.isUserDialog(notificationException.did)) {
                        TLRPC$User user2 = NotificationsCustomSettingsActivity.this.getMessagesController().getUser(Long.valueOf(notificationException.did));
                        if (user2 != null && !user2.deleted) {
                            strArr3[0] = ContactsController.formatName(user2.first_name, user2.last_name);
                            strArr3[1] = user2.username;
                            c = 0;
                            tLRPC$User = user2;
                            String str2 = strArr3[c];
                            strArr3[c] = strArr3[c].toLowerCase();
                            translitString = LocaleController.getInstance().getTranslitString(strArr3[c]);
                            if (strArr3[c] != null && strArr3[c].equals(translitString)) {
                                translitString = null;
                            }
                            i3 = i2;
                            i4 = 0;
                            char c4 = 0;
                            while (i4 < i3) {
                                String str3 = strArr2[i4];
                                strArr = strArr2;
                                if (strArr3[c] != null) {
                                    i = i3;
                                    if (!strArr3[c].startsWith(str3)) {
                                    }
                                    c3 = 1;
                                    c2 = 1;
                                    if (c2 != 0) {
                                        if (c2 == c3) {
                                            arrayList4.add(AndroidUtilities.generateSearchName(str2, null, str3));
                                        } else {
                                            arrayList4.add(AndroidUtilities.generateSearchName("@" + strArr3[c3], null, "@" + str3));
                                        }
                                        arrayList3.add(notificationException);
                                        if (tLRPC$User != null) {
                                            arrayList2.add(tLRPC$User);
                                        }
                                        i6++;
                                        i5 = i;
                                        strArr2 = strArr;
                                    } else {
                                        i4++;
                                        c4 = c2;
                                        i3 = i;
                                        strArr2 = strArr;
                                        c = 0;
                                    }
                                } else {
                                    i = i3;
                                }
                                if (translitString != null) {
                                    if (!translitString.startsWith(str3)) {
                                    }
                                    c3 = 1;
                                    c2 = 1;
                                    if (c2 != 0) {
                                    }
                                }
                                c3 = 1;
                                c2 = (strArr3[1] == null || !strArr3[1].startsWith(str3)) ? c4 : (char) 2;
                                if (c2 != 0) {
                                }
                            }
                            strArr = strArr2;
                            i = i3;
                            i6++;
                            i5 = i;
                            strArr2 = strArr;
                        }
                        strArr = strArr2;
                        i = i2;
                        i6++;
                        i5 = i;
                        strArr2 = strArr;
                    } else {
                        TLRPC$Chat chat = NotificationsCustomSettingsActivity.this.getMessagesController().getChat(Long.valueOf(-notificationException.did));
                        if (chat != null) {
                            if (!chat.left && !chat.kicked && chat.migrated_to == null) {
                                c = 0;
                                strArr3[0] = chat.title;
                                strArr3[1] = chat.username;
                                tLRPC$User = chat;
                                String str22 = strArr3[c];
                                strArr3[c] = strArr3[c].toLowerCase();
                                translitString = LocaleController.getInstance().getTranslitString(strArr3[c]);
                                if (strArr3[c] != null) {
                                    translitString = null;
                                }
                                i3 = i2;
                                i4 = 0;
                                char c42 = 0;
                                while (i4 < i3) {
                                }
                                strArr = strArr2;
                                i = i3;
                                i6++;
                                i5 = i;
                                strArr2 = strArr;
                            }
                            strArr = strArr2;
                            i = i2;
                            i6++;
                            i5 = i;
                            strArr2 = strArr;
                        }
                    }
                }
                c = 0;
                tLRPC$User = null;
                String str222 = strArr3[c];
                strArr3[c] = strArr3[c].toLowerCase();
                translitString = LocaleController.getInstance().getTranslitString(strArr3[c]);
                if (strArr3[c] != null) {
                }
                i3 = i2;
                i4 = 0;
                char c422 = 0;
                while (i4 < i3) {
                }
                strArr = strArr2;
                i = i3;
                i6++;
                i5 = i;
                strArr2 = strArr;
            }
            updateSearchResults(arrayList2, arrayList3, arrayList4);
        }

        private void updateSearchResults(ArrayList<Object> arrayList, ArrayList<NotificationsSettingsActivity.NotificationException> arrayList2, ArrayList<CharSequence> arrayList3) {
            AndroidUtilities.runOnUIThread(new NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda3(this, arrayList2, arrayList3, arrayList));
        }

        public /* synthetic */ void lambda$updateSearchResults$4(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3) {
            if (!NotificationsCustomSettingsActivity.this.searching) {
                return;
            }
            this.searchRunnable = null;
            this.searchResult = arrayList;
            this.searchResultNames = arrayList2;
            this.searchAdapterHelper.mergeResults(arrayList3);
            if (NotificationsCustomSettingsActivity.this.searching && !this.searchAdapterHelper.isSearchInProgress()) {
                NotificationsCustomSettingsActivity.this.emptyView.showTextView();
            }
            notifyDataSetChanged();
        }

        public Object getObject(int i) {
            if (i >= 0 && i < this.searchResult.size()) {
                return this.searchResult.get(i);
            }
            int size = i - (this.searchResult.size() + 1);
            ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
            if (size >= 0 && size < globalSearch.size()) {
                return this.searchAdapterHelper.getGlobalSearch().get(size);
            }
            return null;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int size = this.searchResult.size();
            ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
            return !globalSearch.isEmpty() ? size + globalSearch.size() + 1 : size;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (i == 0) {
                view = new UserCell(this.mContext, 4, 0, false, true);
                view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else {
                view = new GraySectionCell(this.mContext);
            }
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            boolean z = true;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    return;
                }
                ((GraySectionCell) viewHolder.itemView).setText(LocaleController.getString("AddToExceptions", 2131624290));
                return;
            }
            UserCell userCell = (UserCell) viewHolder.itemView;
            if (i < this.searchResult.size()) {
                NotificationsSettingsActivity.NotificationException notificationException = this.searchResult.get(i);
                CharSequence charSequence = this.searchResultNames.get(i);
                if (i == this.searchResult.size() - 1) {
                    z = false;
                }
                userCell.setException(notificationException, charSequence, z);
                userCell.setAddButtonVisible(false);
                return;
            }
            int size = i - (this.searchResult.size() + 1);
            ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
            userCell.setData(globalSearch.get(size), null, LocaleController.getString("NotificationsOn", 2131627051), 0, size != globalSearch.size() - 1);
            userCell.setAddButtonVisible(true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return i == this.searchResult.size() ? 1 : 0;
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            NotificationsCustomSettingsActivity.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return (itemViewType == 0 || itemViewType == 4) ? false : true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return NotificationsCustomSettingsActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            switch (i) {
                case 0:
                    view = new HeaderCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                case 1:
                    view = new TextCheckCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                case 2:
                    view = new UserCell(this.mContext, 6, 0, false);
                    view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                case 3:
                    view = new TextColorCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                case 4:
                    view = new ShadowSectionCell(this.mContext);
                    break;
                case 5:
                    view = new TextSettingsCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                case 6:
                    view = new NotificationsCheckCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                default:
                    view = new TextCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
            }
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            boolean z;
            int i2;
            int i3;
            CharSequence charSequence;
            int i4;
            int i5;
            String str;
            long j;
            String string;
            int i6;
            int i7;
            boolean z2 = false;
            boolean z3 = false;
            int i8 = 0;
            switch (viewHolder.getItemViewType()) {
                case 0:
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i != NotificationsCustomSettingsActivity.this.messageSectionRow) {
                        return;
                    }
                    headerCell.setText(LocaleController.getString("SETTINGS", 2131628057));
                    return;
                case 1:
                    TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings = NotificationsCustomSettingsActivity.this.getNotificationsSettings();
                    if (i != NotificationsCustomSettingsActivity.this.previewRow) {
                        return;
                    }
                    if (NotificationsCustomSettingsActivity.this.currentType != 1) {
                        if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                            z = notificationsSettings.getBoolean("EnablePreviewGroup", true);
                        } else {
                            z = notificationsSettings.getBoolean("EnablePreviewChannel", true);
                        }
                    } else {
                        z = notificationsSettings.getBoolean("EnablePreviewAll", true);
                    }
                    textCheckCell.setTextAndCheck(LocaleController.getString("MessagePreview", 2131626640), z, true);
                    return;
                case 2:
                    UserCell userCell = (UserCell) viewHolder.itemView;
                    NotificationsSettingsActivity.NotificationException notificationException = (NotificationsSettingsActivity.NotificationException) NotificationsCustomSettingsActivity.this.exceptions.get(i - NotificationsCustomSettingsActivity.this.exceptionsStartRow);
                    if (i != NotificationsCustomSettingsActivity.this.exceptionsEndRow - 1) {
                        z2 = true;
                    }
                    userCell.setException(notificationException, null, z2);
                    return;
                case 3:
                    TextColorCell textColorCell = (TextColorCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings2 = NotificationsCustomSettingsActivity.this.getNotificationsSettings();
                    if (NotificationsCustomSettingsActivity.this.currentType != 1) {
                        if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                            i2 = notificationsSettings2.getInt("GroupLed", -16776961);
                        } else {
                            i2 = notificationsSettings2.getInt("ChannelLed", -16776961);
                        }
                    } else {
                        i2 = notificationsSettings2.getInt("MessagesLed", -16776961);
                    }
                    while (true) {
                        if (i8 < 9) {
                            if (TextColorCell.colorsToSave[i8] == i2) {
                                i2 = TextColorCell.colors[i8];
                            } else {
                                i8++;
                            }
                        }
                    }
                    textColorCell.setTextAndColor(LocaleController.getString("LedColor", 2131626396), i2, true);
                    return;
                case 4:
                    if (i == NotificationsCustomSettingsActivity.this.deleteAllSectionRow || ((i == NotificationsCustomSettingsActivity.this.groupSection2Row && NotificationsCustomSettingsActivity.this.exceptionsSection2Row == -1) || (i == NotificationsCustomSettingsActivity.this.exceptionsSection2Row && NotificationsCustomSettingsActivity.this.deleteAllRow == -1))) {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                        return;
                    } else {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                        return;
                    }
                case 5:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                    SharedPreferences notificationsSettings3 = NotificationsCustomSettingsActivity.this.getNotificationsSettings();
                    if (i == NotificationsCustomSettingsActivity.this.messageSoundRow) {
                        if (NotificationsCustomSettingsActivity.this.currentType != 1) {
                            if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                                str = notificationsSettings3.getString("GroupSound", LocaleController.getString("SoundDefault", 2131628387));
                                j = notificationsSettings3.getLong("GroupSoundDocId", 0L);
                            } else {
                                str = notificationsSettings3.getString("ChannelSound", LocaleController.getString("SoundDefault", 2131628387));
                                j = notificationsSettings3.getLong("ChannelDocId", 0L);
                            }
                        } else {
                            str = notificationsSettings3.getString("GlobalSound", LocaleController.getString("SoundDefault", 2131628387));
                            j = notificationsSettings3.getLong("GlobalSoundDocId", 0L);
                        }
                        if (j != 0) {
                            TLRPC$Document document = NotificationsCustomSettingsActivity.this.getMediaDataController().ringtoneDataStore.getDocument(j);
                            if (document == null) {
                                str = LocaleController.getString("CustomSound", 2131625305);
                            } else {
                                str = NotificationsSoundActivity.trimTitle(document, FileLoader.getDocumentFileName(document));
                            }
                        } else if (str.equals("NoSound")) {
                            str = LocaleController.getString("NoSound", 2131626872);
                        } else if (str.equals("Default")) {
                            str = LocaleController.getString("SoundDefault", 2131628387);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("Sound", 2131628384), str, true);
                        return;
                    } else if (i == NotificationsCustomSettingsActivity.this.messageVibrateRow) {
                        if (NotificationsCustomSettingsActivity.this.currentType != 1) {
                            if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                                i5 = notificationsSettings3.getInt("vibrate_group", 0);
                            } else {
                                i5 = notificationsSettings3.getInt("vibrate_channel", 0);
                            }
                        } else {
                            i5 = notificationsSettings3.getInt("vibrate_messages", 0);
                        }
                        if (i5 == 0) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628878), LocaleController.getString("VibrationDefault", 2131628879), true);
                            return;
                        } else if (i5 == 1) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628878), LocaleController.getString("Short", 2131628327), true);
                            return;
                        } else if (i5 == 2) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628878), LocaleController.getString("VibrationDisabled", 2131628880), true);
                            return;
                        } else if (i5 == 3) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628878), LocaleController.getString("Long", 2131626507), true);
                            return;
                        } else if (i5 != 4) {
                            return;
                        } else {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131628878), LocaleController.getString("OnlyIfSilent", 2131627087), true);
                            return;
                        }
                    } else if (i == NotificationsCustomSettingsActivity.this.messagePriorityRow) {
                        if (NotificationsCustomSettingsActivity.this.currentType != 1) {
                            if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                                i4 = notificationsSettings3.getInt("priority_group", 1);
                            } else {
                                i4 = notificationsSettings3.getInt("priority_channel", 1);
                            }
                        } else {
                            i4 = notificationsSettings3.getInt("priority_messages", 1);
                        }
                        if (i4 == 0) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131627040), LocaleController.getString("NotificationsPriorityHigh", 2131627053), false);
                            return;
                        } else if (i4 == 1 || i4 == 2) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131627040), LocaleController.getString("NotificationsPriorityUrgent", 2131627057), false);
                            return;
                        } else if (i4 == 4) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131627040), LocaleController.getString("NotificationsPriorityLow", 2131627054), false);
                            return;
                        } else if (i4 != 5) {
                            return;
                        } else {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131627040), LocaleController.getString("NotificationsPriorityMedium", 2131627055), false);
                            return;
                        }
                    } else if (i != NotificationsCustomSettingsActivity.this.messagePopupNotificationRow) {
                        return;
                    } else {
                        if (NotificationsCustomSettingsActivity.this.currentType != 1) {
                            if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                                i3 = notificationsSettings3.getInt("popupGroup", 0);
                            } else {
                                i3 = notificationsSettings3.getInt("popupChannel", 0);
                            }
                        } else {
                            i3 = notificationsSettings3.getInt("popupAll", 0);
                        }
                        if (i3 == 0) {
                            charSequence = LocaleController.getString("NoPopup", 2131626851);
                        } else if (i3 == 1) {
                            charSequence = LocaleController.getString("OnlyWhenScreenOn", 2131627089);
                        } else if (i3 == 2) {
                            charSequence = LocaleController.getString("OnlyWhenScreenOff", 2131627088);
                        } else {
                            charSequence = LocaleController.getString("AlwaysShowPopup", 2131624336);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("PopupNotification", 2131627592), charSequence, true);
                        return;
                    }
                case 6:
                    NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                    notificationsCheckCell.setDrawLine(false);
                    StringBuilder sb = new StringBuilder();
                    SharedPreferences notificationsSettings4 = NotificationsCustomSettingsActivity.this.getNotificationsSettings();
                    if (NotificationsCustomSettingsActivity.this.currentType != 1) {
                        if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                            string = LocaleController.getString("NotificationsForGroups", 2131627036);
                            i6 = notificationsSettings4.getInt("EnableGroup2", 0);
                        } else {
                            string = LocaleController.getString("NotificationsForChannels", 2131627034);
                            i6 = notificationsSettings4.getInt("EnableChannel2", 0);
                        }
                    } else {
                        string = LocaleController.getString("NotificationsForPrivateChats", 2131627037);
                        i6 = notificationsSettings4.getInt("EnableAll2", 0);
                    }
                    String str2 = string;
                    int currentTime = NotificationsCustomSettingsActivity.this.getConnectionsManager().getCurrentTime();
                    boolean z4 = i6 < currentTime;
                    if (z4) {
                        sb.append(LocaleController.getString("NotificationsOn", 2131627051));
                    } else if (i6 - 31536000 >= currentTime) {
                        sb.append(LocaleController.getString("NotificationsOff", 2131627049));
                    } else {
                        sb.append(LocaleController.formatString("NotificationsOffUntil", 2131627050, LocaleController.stringForMessageListDate(i6)));
                        i7 = 2;
                        notificationsCheckCell.setTextAndValueAndCheck(str2, sb, z4, i7, false);
                        return;
                    }
                    i7 = 0;
                    notificationsCheckCell.setTextAndValueAndCheck(str2, sb, z4, i7, false);
                    return;
                case 7:
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    if (i != NotificationsCustomSettingsActivity.this.exceptionsAddRow) {
                        if (i != NotificationsCustomSettingsActivity.this.deleteAllRow) {
                            return;
                        }
                        textCell.setText(LocaleController.getString("NotificationsDeleteAllException", 2131627027), false);
                        textCell.setColors(null, "windowBackgroundWhiteRedText5");
                        return;
                    }
                    String string2 = LocaleController.getString("NotificationsAddAnException", 2131627017);
                    if (NotificationsCustomSettingsActivity.this.exceptionsStartRow != -1) {
                        z3 = true;
                    }
                    textCell.setTextAndIcon(string2, 2131165690, z3);
                    textCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                    return;
                default:
                    return;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            if (NotificationsCustomSettingsActivity.this.exceptions == null || !NotificationsCustomSettingsActivity.this.exceptions.isEmpty()) {
                return;
            }
            boolean isGlobalNotificationsEnabled = NotificationsCustomSettingsActivity.this.getNotificationsController().isGlobalNotificationsEnabled(NotificationsCustomSettingsActivity.this.currentType);
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (viewHolder.getAdapterPosition() == NotificationsCustomSettingsActivity.this.messageSectionRow) {
                    headerCell.setEnabled(isGlobalNotificationsEnabled, null);
                } else {
                    headerCell.setEnabled(true, null);
                }
            } else if (itemViewType == 1) {
                ((TextCheckCell) viewHolder.itemView).setEnabled(isGlobalNotificationsEnabled, null);
            } else if (itemViewType == 3) {
                ((TextColorCell) viewHolder.itemView).setEnabled(isGlobalNotificationsEnabled, null);
            } else if (itemViewType != 5) {
            } else {
                ((TextSettingsCell) viewHolder.itemView).setEnabled(isGlobalNotificationsEnabled, null);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == NotificationsCustomSettingsActivity.this.messageSectionRow) {
                return 0;
            }
            if (i == NotificationsCustomSettingsActivity.this.previewRow) {
                return 1;
            }
            if (i >= NotificationsCustomSettingsActivity.this.exceptionsStartRow && i < NotificationsCustomSettingsActivity.this.exceptionsEndRow) {
                return 2;
            }
            if (i == NotificationsCustomSettingsActivity.this.messageLedRow) {
                return 3;
            }
            if (i == NotificationsCustomSettingsActivity.this.groupSection2Row || i == NotificationsCustomSettingsActivity.this.alertSection2Row || i == NotificationsCustomSettingsActivity.this.exceptionsSection2Row || i == NotificationsCustomSettingsActivity.this.deleteAllSectionRow) {
                return 4;
            }
            if (i == NotificationsCustomSettingsActivity.this.alertRow) {
                return 6;
            }
            return (i == NotificationsCustomSettingsActivity.this.exceptionsAddRow || i == NotificationsCustomSettingsActivity.this.deleteAllRow) ? 7 : 5;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        NotificationsCustomSettingsActivity$$ExternalSyntheticLambda8 notificationsCustomSettingsActivity$$ExternalSyntheticLambda8 = new NotificationsCustomSettingsActivity$$ExternalSyntheticLambda8(this);
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextCheckCell.class, TextColorCell.class, TextSettingsCell.class, UserCell.class, NotificationsCheckCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, notificationsCustomSettingsActivity$$ExternalSyntheticLambda8, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, notificationsCustomSettingsActivity$$ExternalSyntheticLambda8, "windowBackgroundWhiteBlueText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, notificationsCustomSettingsActivity$$ExternalSyntheticLambda8, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, notificationsCustomSettingsActivity$$ExternalSyntheticLambda8, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, notificationsCustomSettingsActivity$$ExternalSyntheticLambda8, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, notificationsCustomSettingsActivity$$ExternalSyntheticLambda8, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, notificationsCustomSettingsActivity$$ExternalSyntheticLambda8, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, notificationsCustomSettingsActivity$$ExternalSyntheticLambda8, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, notificationsCustomSettingsActivity$$ExternalSyntheticLambda8, "avatar_backgroundPink"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "key_graySectionText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, null, null, null, "graySection"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextColorCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteValueText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueButton"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText5"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueIcon"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$11() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof UserCell) {
                    ((UserCell) childAt).update(0);
                }
            }
        }
    }
}
