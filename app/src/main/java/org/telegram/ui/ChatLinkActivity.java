package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$TL_channels_getGroupsForDiscussion;
import org.telegram.tgnet.TLRPC$TL_channels_setDiscussionGroup;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputChannelEmpty;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$messages_Chats;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ManageChatTextCell;
import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.JoinToSendSettingsView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LoadingStickerDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.GroupCreateFinalActivity;
/* loaded from: classes3.dex */
public class ChatLinkActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int chatEndRow;
    private int chatStartRow;
    private boolean chatsLoaded;
    private int createChatRow;
    private TLRPC$Chat currentChat;
    private long currentChatId;
    private int detailRow;
    private EmptyTextProgressView emptyView;
    private int helpRow;
    private TLRPC$ChatFull info;
    private boolean isChannel;
    private int joinToSendRow;
    private JoinToSendSettingsView joinToSendSettings;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private boolean loadingChats;
    private int removeChatRow;
    private int rowCount;
    private SearchAdapter searchAdapter;
    private ActionBarMenuItem searchItem;
    private boolean searchWas;
    private boolean searching;
    private TLRPC$Chat waitingForFullChat;
    private AlertDialog waitingForFullChatProgressAlert;
    private ArrayList<TLRPC$Chat> chats = new ArrayList<>();
    private boolean joinToSendProgress = false;
    private boolean joinRequestProgress = false;

    /* loaded from: classes3.dex */
    public static class EmptyView extends LinearLayout implements NotificationCenter.NotificationCenterDelegate {
        private int currentAccount = UserConfig.selectedAccount;
        private LoadingStickerDrawable drawable;
        private BackupImageView stickerView;

        public EmptyView(Context context) {
            super(context);
            setPadding(0, AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f));
            setOrientation(1);
            this.stickerView = new BackupImageView(context);
            LoadingStickerDrawable loadingStickerDrawable = new LoadingStickerDrawable(this.stickerView, "M476.1,397.4c25.8-47.2,0.3-105.9-50.9-120c-2.5-6.9-7.8-12.7-15-16.4l0.4-229.4c0-12.3-10-22.4-22.4-22.4H128.5c-12.3,0-22.4,10-22.4,22.4l-0.4,229.8v0c0,6.7,2.9,12.6,7.6,16.7c-51.6,15.9-79.2,77.2-48.1,116.4c-8.7,11.7-13.4,27.5-14,47.2c-1.7,34.5,21.6,45.8,55.9,45.8c52.3,0,99.1,4.6,105.1-36.2c16.5,0.9,7.1-37.3-6.5-53.3c18.4-22.4,18.3-52.9,4.9-78.2c-0.7-5.3-3.8-9.8-8.1-12.6c-1.5-2-1.6-2-2.1-2.7c0.2-1,1.2-11.8-3.4-20.9h138.5c-4.8,8.8-4.7,17-2.9,22.1c-5.3,4.8-6.8,12.3-5.2,17c-11.4,24.9-10,53.8,4.3,77.5c-6.8,9.7-11.2,21.7-12.6,31.6c-0.2-0.2-0.4-0.3-0.6-0.5c0.8-3.3,0.4-6.4-1.3-7.8c9.3-12.1-4.5-29.2-17-21.7c-3.8-2.8-10.6-3.2-18.1-0.5c-2.4-10.6-21.1-10.6-28.6-1c-1.3,0.3-2.9,0.8-4.5,1.9c-5.2-0.9-10.9,0.1-14.1,4.4c-6.9,3-9.5,10.4-7.8,17c-0.9,1.8-1.1,4-0.8,6.3c-1.6,1.2-2.3,3.1-2,4.9c0.1,0.6,10.4,56.6,11.2,62c0.3,1.8,1.5,3.2,3.1,3.9c8.7,3.4,12,3.8,30.1,9.4c2.7,0.8,2.4,0.8,6.7-0.1c16.4-3.5,30.2-8.9,30.8-9.2c1.6-0.6,2.7-2,3.1-3.7c0.1-0.4,6.8-36.5,10-53.2c0.9,4.2,3.3,7.3,7.4,7.5c1.2,7.8,4.4,14.5,9.5,19.9c16.4,17.3,44.9,15.7,64.9,16.1c38.3,0.8,74.5,1.5,84.4-24.4C488.9,453.5,491.3,421.3,476.1,397.4z", AndroidUtilities.dp(104.0f), AndroidUtilities.dp(104.0f));
            this.drawable = loadingStickerDrawable;
            this.stickerView.setImageDrawable(loadingStickerDrawable);
            addView(this.stickerView, LayoutHelper.createLinear(104, 104, 49, 0, 2, 0, 0));
        }

        private void setSticker() {
            TLRPC$TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByName("tg_placeholders_android");
            if (stickerSetByName == null) {
                stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByEmojiOrName("tg_placeholders_android");
            }
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = stickerSetByName;
            if (tLRPC$TL_messages_stickerSet != null && tLRPC$TL_messages_stickerSet.documents.size() >= 3) {
                this.stickerView.setImage(ImageLocation.getForDocument(tLRPC$TL_messages_stickerSet.documents.get(2)), "104_104", "tgs", this.drawable, tLRPC$TL_messages_stickerSet);
                return;
            }
            MediaDataController.getInstance(this.currentAccount).loadStickersByEmojiOrName("tg_placeholders_android", false, tLRPC$TL_messages_stickerSet == null);
            this.stickerView.setImageDrawable(this.drawable);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            setSticker();
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.diceStickersDidLoad);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.diceStickersDidLoad);
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            if (i != NotificationCenter.diceStickersDidLoad || !"tg_placeholders_android".equals((String) objArr[0])) {
                return;
            }
            setSticker();
        }
    }

    public ChatLinkActivity(long j) {
        boolean z = false;
        this.currentChatId = j;
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(j));
        this.currentChat = chat;
        if (ChatObject.isChannel(chat) && !this.currentChat.megagroup) {
            z = true;
        }
        this.isChannel = z;
    }

    private void updateRows() {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.currentChatId));
        this.currentChat = chat;
        if (chat == null) {
            return;
        }
        int i = 0;
        this.rowCount = 0;
        this.helpRow = -1;
        this.createChatRow = -1;
        this.chatStartRow = -1;
        this.chatEndRow = -1;
        this.removeChatRow = -1;
        this.detailRow = -1;
        this.joinToSendRow = -1;
        int i2 = 0 + 1;
        this.rowCount = i2;
        this.helpRow = 0;
        if (this.isChannel) {
            if (this.info.linked_chat_id == 0) {
                this.rowCount = i2 + 1;
                this.createChatRow = i2;
            }
            int i3 = this.rowCount;
            this.chatStartRow = i3;
            int size = i3 + this.chats.size();
            this.rowCount = size;
            this.chatEndRow = size;
            if (this.info.linked_chat_id != 0) {
                this.rowCount = size + 1;
                this.createChatRow = size;
            }
        } else {
            this.chatStartRow = i2;
            int size2 = i2 + this.chats.size();
            this.rowCount = size2;
            this.chatEndRow = size2;
            this.rowCount = size2 + 1;
            this.createChatRow = size2;
        }
        int i4 = this.rowCount;
        this.rowCount = i4 + 1;
        this.detailRow = i4;
        if (!this.isChannel || (this.chats.size() > 0 && this.info.linked_chat_id != 0)) {
            TLRPC$Chat tLRPC$Chat = this.isChannel ? this.chats.get(0) : this.currentChat;
            if (tLRPC$Chat != null && ((TextUtils.isEmpty(tLRPC$Chat.username) || this.isChannel) && (tLRPC$Chat.creator || ((tLRPC$TL_chatAdminRights = tLRPC$Chat.admin_rights) != null && tLRPC$TL_chatAdminRights.ban_users)))) {
                int i5 = this.rowCount;
                this.rowCount = i5 + 1;
                this.joinToSendRow = i5;
            }
        }
        ListAdapter listAdapter = this.listViewAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        if (actionBarMenuItem == null) {
            return;
        }
        if (this.chats.size() <= 10) {
            i = 8;
        }
        actionBarMenuItem.setVisibility(i);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        getNotificationCenter().addObserver(this, NotificationCenter.chatInfoDidLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.updateInterfaces);
        loadChats();
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        getNotificationCenter().removeObserver(this, NotificationCenter.chatInfoDidLoad);
        getNotificationCenter().removeObserver(this, NotificationCenter.updateInterfaces);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        JoinToSendSettingsView joinToSendSettingsView;
        TLRPC$Chat chat;
        TLRPC$Chat tLRPC$Chat = null;
        if (i == NotificationCenter.chatInfoDidLoad) {
            TLRPC$ChatFull tLRPC$ChatFull = (TLRPC$ChatFull) objArr[0];
            long j = tLRPC$ChatFull.id;
            if (j == this.currentChatId) {
                this.info = tLRPC$ChatFull;
                loadChats();
                updateRows();
                return;
            }
            TLRPC$Chat tLRPC$Chat2 = this.waitingForFullChat;
            if (tLRPC$Chat2 == null || tLRPC$Chat2.id != j) {
                return;
            }
            try {
                this.waitingForFullChatProgressAlert.dismiss();
            } catch (Throwable unused) {
            }
            this.waitingForFullChatProgressAlert = null;
            showLinkAlert(this.waitingForFullChat, false);
            this.waitingForFullChat = null;
        } else if (i != NotificationCenter.updateInterfaces || (((Integer) objArr[0]).intValue() & MessagesController.UPDATE_MASK_CHAT) == 0 || this.currentChat == null) {
        } else {
            TLRPC$Chat chat2 = getMessagesController().getChat(Long.valueOf(this.currentChat.id));
            if (chat2 != null) {
                this.currentChat = chat2;
            }
            if (this.chats.size() > 0 && (chat = getMessagesController().getChat(Long.valueOf(this.chats.get(0).id))) != null) {
                this.chats.set(0, chat);
            }
            if (!this.isChannel) {
                tLRPC$Chat = this.currentChat;
            } else if (this.chats.size() > 0) {
                tLRPC$Chat = this.chats.get(0);
            }
            if (tLRPC$Chat == null || (joinToSendSettingsView = this.joinToSendSettings) == null) {
                return;
            }
            if (!this.joinRequestProgress) {
                joinToSendSettingsView.lambda$new$3(tLRPC$Chat.join_request);
            }
            if (this.joinToSendProgress) {
                return;
            }
            this.joinToSendSettings.setJoinToSend(tLRPC$Chat.join_to_send);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.searching = false;
        this.actionBar.setBackButtonImage(2131165449);
        int i = 1;
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("Discussion", 2131625512));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.createMenu().addItem(0, 2131165456).setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass2());
        this.searchItem = actionBarMenuItemSearchListener;
        actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString("Search", 2131628155));
        this.searchAdapter = new SearchAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        this.fragmentView.setTag("windowBackgroundGray");
        FrameLayout frameLayout2 = (FrameLayout) this.fragmentView;
        EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context);
        this.emptyView = emptyTextProgressView;
        emptyTextProgressView.showProgress();
        this.emptyView.setText(LocaleController.getString("NoResult", 2131626910));
        frameLayout2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setEmptyView(this.emptyView);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        RecyclerListView recyclerListView2 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.listViewAdapter = listAdapter;
        recyclerListView2.setAdapter(listAdapter);
        RecyclerListView recyclerListView3 = this.listView;
        if (!LocaleController.isRTL) {
            i = 2;
        }
        recyclerListView3.setVerticalScrollbarPosition(i);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new ChatLinkActivity$$ExternalSyntheticLambda18(this));
        updateRows();
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.ChatLinkActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            ChatLinkActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                ChatLinkActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.ChatLinkActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        AnonymousClass2() {
            ChatLinkActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchExpand() {
            ChatLinkActivity.this.searching = true;
            ChatLinkActivity.this.emptyView.setShowAtCenter(true);
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchCollapse() {
            ChatLinkActivity.this.searchAdapter.searchDialogs(null);
            ChatLinkActivity.this.searching = false;
            ChatLinkActivity.this.searchWas = false;
            ChatLinkActivity.this.listView.setAdapter(ChatLinkActivity.this.listViewAdapter);
            ChatLinkActivity.this.listViewAdapter.notifyDataSetChanged();
            ChatLinkActivity.this.listView.setFastScrollVisible(true);
            ChatLinkActivity.this.listView.setVerticalScrollBarEnabled(false);
            ChatLinkActivity.this.emptyView.setShowAtCenter(false);
            ((BaseFragment) ChatLinkActivity.this).fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
            ((BaseFragment) ChatLinkActivity.this).fragmentView.setTag("windowBackgroundGray");
            ChatLinkActivity.this.emptyView.showProgress();
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onTextChanged(EditText editText) {
            if (ChatLinkActivity.this.searchAdapter == null) {
                return;
            }
            String obj = editText.getText().toString();
            if (obj.length() != 0) {
                ChatLinkActivity.this.searchWas = true;
                if (ChatLinkActivity.this.listView != null && ChatLinkActivity.this.listView.getAdapter() != ChatLinkActivity.this.searchAdapter) {
                    ChatLinkActivity.this.listView.setAdapter(ChatLinkActivity.this.searchAdapter);
                    ((BaseFragment) ChatLinkActivity.this).fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    ((BaseFragment) ChatLinkActivity.this).fragmentView.setTag("windowBackgroundWhite");
                    ChatLinkActivity.this.searchAdapter.notifyDataSetChanged();
                    ChatLinkActivity.this.listView.setFastScrollVisible(false);
                    ChatLinkActivity.this.listView.setVerticalScrollBarEnabled(true);
                    ChatLinkActivity.this.emptyView.showProgress();
                }
            }
            ChatLinkActivity.this.searchAdapter.searchDialogs(obj);
        }
    }

    public /* synthetic */ void lambda$createView$6(View view, int i) {
        TLRPC$Chat tLRPC$Chat;
        String str;
        String str2;
        if (getParentActivity() == null) {
            return;
        }
        RecyclerView.Adapter adapter = this.listView.getAdapter();
        SearchAdapter searchAdapter = this.searchAdapter;
        if (adapter == searchAdapter) {
            tLRPC$Chat = searchAdapter.getItem(i);
        } else {
            int i2 = this.chatStartRow;
            tLRPC$Chat = (i < i2 || i >= this.chatEndRow) ? null : this.chats.get(i - i2);
        }
        if (tLRPC$Chat != null) {
            if (this.isChannel && this.info.linked_chat_id == 0) {
                showLinkAlert(tLRPC$Chat, true);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", tLRPC$Chat.id);
            presentFragment(new ChatActivity(bundle));
        } else if (i != this.createChatRow) {
        } else {
            if (this.isChannel && this.info.linked_chat_id == 0) {
                Bundle bundle2 = new Bundle();
                bundle2.putLongArray("result", new long[]{getUserConfig().getClientUserId()});
                bundle2.putInt("chatType", 4);
                GroupCreateFinalActivity groupCreateFinalActivity = new GroupCreateFinalActivity(bundle2);
                groupCreateFinalActivity.setDelegate(new AnonymousClass3());
                presentFragment(groupCreateFinalActivity);
            } else if (this.chats.isEmpty()) {
            } else {
                TLRPC$Chat tLRPC$Chat2 = this.chats.get(0);
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                if (this.isChannel) {
                    str2 = LocaleController.getString("DiscussionUnlinkGroup", 2131625529);
                    str = LocaleController.formatString("DiscussionUnlinkChannelAlert", 2131625528, tLRPC$Chat2.title);
                } else {
                    str2 = LocaleController.getString("DiscussionUnlink", 2131625527);
                    str = LocaleController.formatString("DiscussionUnlinkGroupAlert", 2131625530, tLRPC$Chat2.title);
                }
                builder.setTitle(str2);
                builder.setMessage(AndroidUtilities.replaceTags(str));
                builder.setPositiveButton(LocaleController.getString("DiscussionUnlink", 2131625526), new ChatLinkActivity$$ExternalSyntheticLambda3(this));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
                AlertDialog create = builder.create();
                showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView == null) {
                    return;
                }
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
            }
        }
    }

    /* renamed from: org.telegram.ui.ChatLinkActivity$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements GroupCreateFinalActivity.GroupCreateFinalActivityDelegate {
        @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
        public void didFailChatCreation() {
        }

        @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
        public void didStartChatCreation() {
        }

        AnonymousClass3() {
            ChatLinkActivity.this = r1;
        }

        @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
        public void didFinishChatCreation(GroupCreateFinalActivity groupCreateFinalActivity, long j) {
            ChatLinkActivity chatLinkActivity = ChatLinkActivity.this;
            chatLinkActivity.linkChat(chatLinkActivity.getMessagesController().getChat(Long.valueOf(j)), groupCreateFinalActivity);
        }
    }

    public /* synthetic */ void lambda$createView$5(DialogInterface dialogInterface, int i) {
        if (!this.isChannel || this.info.linked_chat_id != 0) {
            AlertDialog[] alertDialogArr = {new AlertDialog(getParentActivity(), 3)};
            TLRPC$TL_channels_setDiscussionGroup tLRPC$TL_channels_setDiscussionGroup = new TLRPC$TL_channels_setDiscussionGroup();
            if (this.isChannel) {
                tLRPC$TL_channels_setDiscussionGroup.broadcast = MessagesController.getInputChannel(this.currentChat);
                tLRPC$TL_channels_setDiscussionGroup.group = new TLRPC$TL_inputChannelEmpty();
            } else {
                tLRPC$TL_channels_setDiscussionGroup.broadcast = new TLRPC$TL_inputChannelEmpty();
                tLRPC$TL_channels_setDiscussionGroup.group = MessagesController.getInputChannel(this.currentChat);
            }
            AndroidUtilities.runOnUIThread(new ChatLinkActivity$$ExternalSyntheticLambda10(this, alertDialogArr, getConnectionsManager().sendRequest(tLRPC$TL_channels_setDiscussionGroup, new ChatLinkActivity$$ExternalSyntheticLambda15(this, alertDialogArr))), 500L);
        }
    }

    public /* synthetic */ void lambda$createView$2(AlertDialog[] alertDialogArr, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChatLinkActivity$$ExternalSyntheticLambda9(this, alertDialogArr));
    }

    public /* synthetic */ void lambda$createView$1(AlertDialog[] alertDialogArr) {
        try {
            alertDialogArr[0].dismiss();
        } catch (Throwable unused) {
        }
        alertDialogArr[0] = null;
        this.info.linked_chat_id = 0L;
        NotificationCenter notificationCenter = NotificationCenter.getInstance(this.currentAccount);
        int i = NotificationCenter.chatInfoDidLoad;
        Boolean bool = Boolean.FALSE;
        notificationCenter.postNotificationName(i, this.info, 0, bool, bool);
        AndroidUtilities.runOnUIThread(new ChatLinkActivity$$ExternalSyntheticLambda6(this), 1000L);
        if (!this.isChannel) {
            finishFragment();
        }
    }

    public /* synthetic */ void lambda$createView$0() {
        getMessagesController().loadFullChat(this.currentChatId, 0, true);
    }

    public /* synthetic */ void lambda$createView$4(AlertDialog[] alertDialogArr, int i) {
        if (alertDialogArr[0] == null) {
            return;
        }
        alertDialogArr[0].setOnCancelListener(new ChatLinkActivity$$ExternalSyntheticLambda2(this, i));
        showDialog(alertDialogArr[0]);
    }

    public /* synthetic */ void lambda$createView$3(int i, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(i, true);
    }

    private void showLinkAlert(TLRPC$Chat tLRPC$Chat, boolean z) {
        String str;
        TLRPC$ChatFull chatFull = getMessagesController().getChatFull(tLRPC$Chat.id);
        if (chatFull == null) {
            if (!z) {
                return;
            }
            getMessagesController().loadFullChat(tLRPC$Chat.id, 0, true);
            this.waitingForFullChat = tLRPC$Chat;
            this.waitingForFullChatProgressAlert = new AlertDialog(getParentActivity(), 3);
            AndroidUtilities.runOnUIThread(new ChatLinkActivity$$ExternalSyntheticLambda5(this), 500L);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        TextView textView = new TextView(getParentActivity());
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        textView.setTextSize(1, 16.0f);
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        if (TextUtils.isEmpty(tLRPC$Chat.username)) {
            str = LocaleController.formatString("DiscussionLinkGroupPublicPrivateAlert", 2131625524, tLRPC$Chat.title, this.currentChat.title);
        } else {
            str = TextUtils.isEmpty(this.currentChat.username) ? LocaleController.formatString("DiscussionLinkGroupPrivateAlert", 2131625522, tLRPC$Chat.title, this.currentChat.title) : LocaleController.formatString("DiscussionLinkGroupPublicAlert", 2131625523, tLRPC$Chat.title, this.currentChat.title);
        }
        if (chatFull.hidden_prehistory) {
            str = str + "\n\n" + LocaleController.getString("DiscussionLinkGroupAlertHistory", 2131625521);
        }
        textView.setText(AndroidUtilities.replaceTags(str));
        FrameLayout frameLayout = new FrameLayout(getParentActivity());
        builder.setView(frameLayout);
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        BackupImageView backupImageView = new BackupImageView(getParentActivity());
        backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
        frameLayout.addView(backupImageView, LayoutHelper.createFrame(40, 40.0f, (LocaleController.isRTL ? 5 : 3) | 48, 22.0f, 5.0f, 22.0f, 0.0f));
        TextView textView2 = new TextView(getParentActivity());
        textView2.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
        textView2.setTextSize(1, 20.0f);
        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView2.setLines(1);
        textView2.setMaxLines(1);
        textView2.setSingleLine(true);
        textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        textView2.setEllipsize(TextUtils.TruncateAt.END);
        textView2.setText(tLRPC$Chat.title);
        boolean z2 = LocaleController.isRTL;
        int i = (z2 ? 5 : 3) | 48;
        int i2 = 21;
        float f = z2 ? 21 : 76;
        if (z2) {
            i2 = 76;
        }
        frameLayout.addView(textView2, LayoutHelper.createFrame(-1, -2.0f, i, f, 11.0f, i2, 0.0f));
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 57.0f, 24.0f, 9.0f));
        avatarDrawable.setInfo(tLRPC$Chat);
        backupImageView.setForUserOrChat(tLRPC$Chat, avatarDrawable);
        builder.setPositiveButton(LocaleController.getString("DiscussionLinkGroup", 2131625520), new ChatLinkActivity$$ExternalSyntheticLambda4(this, chatFull, tLRPC$Chat));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$showLinkAlert$8() {
        AlertDialog alertDialog = this.waitingForFullChatProgressAlert;
        if (alertDialog == null) {
            return;
        }
        alertDialog.setOnCancelListener(new ChatLinkActivity$$ExternalSyntheticLambda0(this));
        showDialog(this.waitingForFullChatProgressAlert);
    }

    public /* synthetic */ void lambda$showLinkAlert$7(DialogInterface dialogInterface) {
        this.waitingForFullChat = null;
    }

    public /* synthetic */ void lambda$showLinkAlert$9(TLRPC$ChatFull tLRPC$ChatFull, TLRPC$Chat tLRPC$Chat, DialogInterface dialogInterface, int i) {
        if (tLRPC$ChatFull.hidden_prehistory) {
            getMessagesController().toggleChannelInvitesHistory(tLRPC$Chat.id, false);
        }
        linkChat(tLRPC$Chat, null);
    }

    public void linkChat(TLRPC$Chat tLRPC$Chat, BaseFragment baseFragment) {
        if (tLRPC$Chat == null) {
            return;
        }
        if (!ChatObject.isChannel(tLRPC$Chat)) {
            getMessagesController().convertToMegaGroup(getParentActivity(), tLRPC$Chat.id, this, new ChatLinkActivity$$ExternalSyntheticLambda13(this, baseFragment));
            return;
        }
        AlertDialog[] alertDialogArr = new AlertDialog[1];
        alertDialogArr[0] = baseFragment != null ? null : new AlertDialog(getParentActivity(), 3);
        TLRPC$TL_channels_setDiscussionGroup tLRPC$TL_channels_setDiscussionGroup = new TLRPC$TL_channels_setDiscussionGroup();
        tLRPC$TL_channels_setDiscussionGroup.broadcast = MessagesController.getInputChannel(this.currentChat);
        tLRPC$TL_channels_setDiscussionGroup.group = MessagesController.getInputChannel(tLRPC$Chat);
        AndroidUtilities.runOnUIThread(new ChatLinkActivity$$ExternalSyntheticLambda11(this, alertDialogArr, getConnectionsManager().sendRequest(tLRPC$TL_channels_setDiscussionGroup, new ChatLinkActivity$$ExternalSyntheticLambda16(this, alertDialogArr, tLRPC$Chat, baseFragment), 64)), 500L);
    }

    public /* synthetic */ void lambda$linkChat$10(BaseFragment baseFragment, long j) {
        if (j != 0) {
            getMessagesController().toggleChannelInvitesHistory(j, false);
            linkChat(getMessagesController().getChat(Long.valueOf(j)), baseFragment);
        }
    }

    public /* synthetic */ void lambda$linkChat$13(AlertDialog[] alertDialogArr, TLRPC$Chat tLRPC$Chat, BaseFragment baseFragment, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChatLinkActivity$$ExternalSyntheticLambda12(this, alertDialogArr, tLRPC$Chat, baseFragment));
    }

    public /* synthetic */ void lambda$linkChat$12(AlertDialog[] alertDialogArr, TLRPC$Chat tLRPC$Chat, BaseFragment baseFragment) {
        if (alertDialogArr[0] != null) {
            try {
                alertDialogArr[0].dismiss();
            } catch (Throwable unused) {
            }
            alertDialogArr[0] = null;
        }
        this.info.linked_chat_id = tLRPC$Chat.id;
        NotificationCenter notificationCenter = NotificationCenter.getInstance(this.currentAccount);
        int i = NotificationCenter.chatInfoDidLoad;
        Boolean bool = Boolean.FALSE;
        notificationCenter.postNotificationName(i, this.info, 0, bool, bool);
        AndroidUtilities.runOnUIThread(new ChatLinkActivity$$ExternalSyntheticLambda7(this), 1000L);
        if (baseFragment != null) {
            removeSelfFromStack();
            baseFragment.finishFragment();
            return;
        }
        finishFragment();
    }

    public /* synthetic */ void lambda$linkChat$11() {
        getMessagesController().loadFullChat(this.currentChatId, 0, true);
    }

    public /* synthetic */ void lambda$linkChat$15(AlertDialog[] alertDialogArr, int i) {
        if (alertDialogArr[0] == null) {
            return;
        }
        alertDialogArr[0].setOnCancelListener(new ChatLinkActivity$$ExternalSyntheticLambda1(this, i));
        showDialog(alertDialogArr[0]);
    }

    public /* synthetic */ void lambda$linkChat$14(int i, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(i, true);
    }

    public void setInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        this.info = tLRPC$ChatFull;
    }

    private void loadChats() {
        if (this.info.linked_chat_id != 0) {
            this.chats.clear();
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.info.linked_chat_id));
            if (chat != null) {
                this.chats.add(chat);
            }
            ActionBarMenuItem actionBarMenuItem = this.searchItem;
            if (actionBarMenuItem != null) {
                actionBarMenuItem.setVisibility(8);
            }
        }
        if (this.loadingChats || !this.isChannel || this.info.linked_chat_id != 0) {
            return;
        }
        this.loadingChats = true;
        getConnectionsManager().sendRequest(new TLRPC$TL_channels_getGroupsForDiscussion(), new ChatLinkActivity$$ExternalSyntheticLambda14(this));
    }

    public /* synthetic */ void lambda$loadChats$17(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ChatLinkActivity$$ExternalSyntheticLambda8(this, tLObject));
    }

    public /* synthetic */ void lambda$loadChats$16(TLObject tLObject) {
        if (tLObject instanceof TLRPC$messages_Chats) {
            TLRPC$messages_Chats tLRPC$messages_Chats = (TLRPC$messages_Chats) tLObject;
            getMessagesController().putChats(tLRPC$messages_Chats.chats, false);
            this.chats = tLRPC$messages_Chats.chats;
        }
        this.loadingChats = false;
        this.chatsLoaded = true;
        updateRows();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listViewAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    /* loaded from: classes3.dex */
    public class HintInnerCell extends FrameLayout {
        private EmptyView emptyView;
        private TextView messageTextView;

        public HintInnerCell(ChatLinkActivity chatLinkActivity, Context context) {
            super(context);
            EmptyView emptyView = new EmptyView(context);
            this.emptyView = emptyView;
            addView(emptyView, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 10.0f, 0.0f, 0.0f));
            TextView textView = new TextView(context);
            this.messageTextView = textView;
            textView.setTextColor(Theme.getColor("chats_message"));
            this.messageTextView.setTextSize(1, 14.0f);
            this.messageTextView.setGravity(17);
            if (chatLinkActivity.isChannel) {
                if (chatLinkActivity.info != null && chatLinkActivity.info.linked_chat_id != 0) {
                    TLRPC$Chat chat = chatLinkActivity.getMessagesController().getChat(Long.valueOf(chatLinkActivity.info.linked_chat_id));
                    if (chat != null) {
                        this.messageTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("DiscussionChannelGroupSetHelp2", 2131625513, chat.title)));
                    }
                } else {
                    this.messageTextView.setText(LocaleController.getString("DiscussionChannelHelp3", 2131625515));
                }
            } else {
                TLRPC$Chat chat2 = chatLinkActivity.getMessagesController().getChat(Long.valueOf(chatLinkActivity.info.linked_chat_id));
                if (chat2 != null) {
                    this.messageTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("DiscussionGroupHelp", 2131625517, chat2.title)));
                }
            }
            addView(this.messageTextView, LayoutHelper.createFrame(-1, -2.0f, 51, 52.0f, 143.0f, 52.0f, 18.0f));
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), i2);
        }
    }

    /* loaded from: classes3.dex */
    public class SearchAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        private ArrayList<TLRPC$Chat> searchResult = new ArrayList<>();
        private ArrayList<CharSequence> searchResultNames = new ArrayList<>();
        private Runnable searchRunnable;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        public SearchAdapter(Context context) {
            ChatLinkActivity.this = r1;
            this.mContext = context;
        }

        public void searchDialogs(String str) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (TextUtils.isEmpty(str)) {
                this.searchResult.clear();
                this.searchResultNames.clear();
                notifyDataSetChanged();
                return;
            }
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            ChatLinkActivity$SearchAdapter$$ExternalSyntheticLambda0 chatLinkActivity$SearchAdapter$$ExternalSyntheticLambda0 = new ChatLinkActivity$SearchAdapter$$ExternalSyntheticLambda0(this, str);
            this.searchRunnable = chatLinkActivity$SearchAdapter$$ExternalSyntheticLambda0;
            dispatchQueue.postRunnable(chatLinkActivity$SearchAdapter$$ExternalSyntheticLambda0, 300L);
        }

        /* renamed from: processSearch */
        public void lambda$searchDialogs$0(String str) {
            AndroidUtilities.runOnUIThread(new ChatLinkActivity$SearchAdapter$$ExternalSyntheticLambda1(this, str));
        }

        public /* synthetic */ void lambda$processSearch$2(String str) {
            this.searchRunnable = null;
            Utilities.searchQueue.postRunnable(new ChatLinkActivity$SearchAdapter$$ExternalSyntheticLambda2(this, str, new ArrayList(ChatLinkActivity.this.chats)));
        }

        /* JADX WARN: Code restructure failed: missing block: B:34:0x00af, code lost:
            if (r12.contains(" " + r15) != false) goto L40;
         */
        /* JADX WARN: Removed duplicated region for block: B:46:0x00fd A[LOOP:1: B:25:0x0073->B:46:0x00fd, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:55:0x00c1 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$processSearch$1(String str, ArrayList arrayList) {
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList<>(), new ArrayList<>());
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                translitString = null;
            }
            int i = (translitString != null ? 1 : 0) + 1;
            String[] strArr = new String[i];
            strArr[0] = lowerCase;
            if (translitString != null) {
                strArr[1] = translitString;
            }
            ArrayList<TLRPC$Chat> arrayList2 = new ArrayList<>();
            ArrayList<CharSequence> arrayList3 = new ArrayList<>();
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) arrayList.get(i2);
                String lowerCase2 = tLRPC$Chat.title.toLowerCase();
                String translitString2 = LocaleController.getInstance().getTranslitString(lowerCase2);
                if (lowerCase2.equals(translitString2)) {
                    translitString2 = null;
                }
                int i3 = 0;
                char c = 0;
                while (true) {
                    if (i3 < i) {
                        String str2 = strArr[i3];
                        if (!lowerCase2.startsWith(str2)) {
                            if (!lowerCase2.contains(" " + str2)) {
                                if (translitString2 != null) {
                                    if (!translitString2.startsWith(str2)) {
                                    }
                                }
                                String str3 = tLRPC$Chat.username;
                                if (str3 != null && str3.startsWith(str2)) {
                                    c = 2;
                                }
                                if (c == 0) {
                                    if (c == 1) {
                                        arrayList3.add(AndroidUtilities.generateSearchName(tLRPC$Chat.title, null, str2));
                                    } else {
                                        arrayList3.add(AndroidUtilities.generateSearchName("@" + tLRPC$Chat.username, null, "@" + str2));
                                    }
                                    arrayList2.add(tLRPC$Chat);
                                } else {
                                    i3++;
                                }
                            }
                        }
                        c = 1;
                        if (c == 0) {
                        }
                    }
                }
            }
            updateSearchResults(arrayList2, arrayList3);
        }

        private void updateSearchResults(ArrayList<TLRPC$Chat> arrayList, ArrayList<CharSequence> arrayList2) {
            AndroidUtilities.runOnUIThread(new ChatLinkActivity$SearchAdapter$$ExternalSyntheticLambda3(this, arrayList, arrayList2));
        }

        public /* synthetic */ void lambda$updateSearchResults$3(ArrayList arrayList, ArrayList arrayList2) {
            if (!ChatLinkActivity.this.searching) {
                return;
            }
            this.searchResult = arrayList;
            this.searchResultNames = arrayList2;
            if (ChatLinkActivity.this.listView.getAdapter() == ChatLinkActivity.this.searchAdapter) {
                ChatLinkActivity.this.emptyView.showTextView();
            }
            notifyDataSetChanged();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.searchResult.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public TLRPC$Chat getItem(int i) {
            return this.searchResult.get(i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            ManageChatUserCell manageChatUserCell = new ManageChatUserCell(this.mContext, 6, 2, false);
            manageChatUserCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            return new RecyclerListView.Holder(manageChatUserCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC$Chat tLRPC$Chat = this.searchResult.get(i);
            String str = tLRPC$Chat.username;
            CharSequence charSequence = this.searchResultNames.get(i);
            CharSequence charSequence2 = null;
            if (charSequence != null && !TextUtils.isEmpty(str)) {
                if (charSequence.toString().startsWith("@" + str)) {
                    charSequence2 = charSequence;
                    charSequence = null;
                }
            }
            ManageChatUserCell manageChatUserCell = (ManageChatUserCell) viewHolder.itemView;
            manageChatUserCell.setTag(Integer.valueOf(i));
            manageChatUserCell.setData(tLRPC$Chat, charSequence, charSequence2, false);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            if (view instanceof ManageChatUserCell) {
                ((ManageChatUserCell) view).recycle();
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            ChatLinkActivity.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 0 || itemViewType == 2;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (!ChatLinkActivity.this.loadingChats || ChatLinkActivity.this.chatsLoaded) {
                return ChatLinkActivity.this.rowCount;
            }
            return 0;
        }

        /* renamed from: org.telegram.ui.ChatLinkActivity$ListAdapter$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends JoinToSendSettingsView {
            final /* synthetic */ TLRPC$Chat val$chat;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, TLRPC$Chat tLRPC$Chat, TLRPC$Chat tLRPC$Chat2) {
                super(context, tLRPC$Chat);
                ListAdapter.this = r1;
                this.val$chat = tLRPC$Chat2;
            }

            private void migrateIfNeeded(Runnable runnable, Runnable runnable2) {
                if (!ChatObject.isChannel(ChatLinkActivity.this.currentChat)) {
                    ChatLinkActivity.this.getMessagesController().convertToMegaGroup(ChatLinkActivity.this.getParentActivity(), this.val$chat.id, ChatLinkActivity.this, new ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda9(this, runnable2), runnable);
                } else {
                    runnable2.run();
                }
            }

            public /* synthetic */ void lambda$migrateIfNeeded$0(Runnable runnable, long j) {
                if (j != 0) {
                    if (ChatLinkActivity.this.isChannel) {
                        ChatLinkActivity.this.chats.set(0, ChatLinkActivity.this.getMessagesController().getChat(Long.valueOf(j)));
                    } else {
                        ChatLinkActivity.this.currentChatId = j;
                        ChatLinkActivity chatLinkActivity = ChatLinkActivity.this;
                        chatLinkActivity.currentChat = chatLinkActivity.getMessagesController().getChat(Long.valueOf(j));
                    }
                    runnable.run();
                }
            }

            @Override // org.telegram.ui.Components.JoinToSendSettingsView
            public boolean onJoinRequestToggle(boolean z, Runnable runnable) {
                if (ChatLinkActivity.this.joinRequestProgress) {
                    return false;
                }
                ChatLinkActivity.this.joinRequestProgress = true;
                migrateIfNeeded(overrideCancel(runnable), new ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda6(this, this.val$chat, z, runnable));
                return true;
            }

            public /* synthetic */ void lambda$onJoinRequestToggle$3(TLRPC$Chat tLRPC$Chat, boolean z, Runnable runnable) {
                tLRPC$Chat.join_request = z;
                ChatLinkActivity.this.getMessagesController().toggleChatJoinRequest(tLRPC$Chat.id, z, new ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda0(this), new ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda2(this, runnable));
            }

            public /* synthetic */ void lambda$onJoinRequestToggle$1() {
                ChatLinkActivity.this.joinRequestProgress = false;
            }

            public /* synthetic */ void lambda$onJoinRequestToggle$2(Runnable runnable) {
                ChatLinkActivity.this.joinRequestProgress = false;
                runnable.run();
            }

            private Runnable overrideCancel(Runnable runnable) {
                return new ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda3(this, runnable);
            }

            public /* synthetic */ void lambda$overrideCancel$4(Runnable runnable) {
                ChatLinkActivity.this.joinToSendProgress = false;
                ChatLinkActivity.this.joinRequestProgress = false;
                runnable.run();
            }

            @Override // org.telegram.ui.Components.JoinToSendSettingsView
            public boolean onJoinToSendToggle(boolean z, Runnable runnable) {
                if (ChatLinkActivity.this.joinToSendProgress) {
                    return false;
                }
                ChatLinkActivity.this.joinToSendProgress = true;
                migrateIfNeeded(overrideCancel(runnable), new ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda7(this, this.val$chat, z, runnable));
                return true;
            }

            public /* synthetic */ void lambda$onJoinToSendToggle$9(TLRPC$Chat tLRPC$Chat, boolean z, Runnable runnable) {
                tLRPC$Chat.join_to_send = z;
                ChatLinkActivity.this.getMessagesController().toggleChatJoinToSend(tLRPC$Chat.id, z, new ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda8(this, z, tLRPC$Chat), new ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda4(this, runnable));
            }

            public /* synthetic */ void lambda$onJoinToSendToggle$7(boolean z, TLRPC$Chat tLRPC$Chat) {
                ChatLinkActivity.this.joinToSendProgress = false;
                if (z || !tLRPC$Chat.join_request) {
                    return;
                }
                tLRPC$Chat.join_request = false;
                ChatLinkActivity.this.joinRequestProgress = true;
                ChatLinkActivity.this.getMessagesController().toggleChatJoinRequest(tLRPC$Chat.id, false, new ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda1(this), new ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda5(this, tLRPC$Chat));
            }

            public /* synthetic */ void lambda$onJoinToSendToggle$5() {
                ChatLinkActivity.this.joinRequestProgress = false;
            }

            public /* synthetic */ void lambda$onJoinToSendToggle$6(TLRPC$Chat tLRPC$Chat) {
                tLRPC$Chat.join_request = true;
                this.isJoinRequest = true;
                this.joinRequestCell.setChecked(true);
            }

            public /* synthetic */ void lambda$onJoinToSendToggle$8(Runnable runnable) {
                ChatLinkActivity.this.joinToSendProgress = false;
                runnable.run();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            View view2;
            if (i == 0) {
                view2 = new ManageChatUserCell(this.mContext, 6, 2, false);
                view2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else {
                if (i == 1) {
                    view = new TextInfoPrivacyCell(this.mContext);
                    view.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                } else if (i == 2) {
                    view2 = new ManageChatTextCell(this.mContext);
                    view2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                } else if (i == 4) {
                    TLRPC$Chat tLRPC$Chat = ChatLinkActivity.this.isChannel ? (TLRPC$Chat) ChatLinkActivity.this.chats.get(0) : ChatLinkActivity.this.currentChat;
                    view = ChatLinkActivity.this.joinToSendSettings = new AnonymousClass1(this.mContext, tLRPC$Chat, tLRPC$Chat);
                } else {
                    view = new HintInnerCell(ChatLinkActivity.this, this.mContext);
                }
                return new RecyclerListView.Holder(view);
            }
            view = view2;
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String str;
            int itemViewType = viewHolder.getItemViewType();
            boolean z = false;
            if (itemViewType == 0) {
                ManageChatUserCell manageChatUserCell = (ManageChatUserCell) viewHolder.itemView;
                manageChatUserCell.setTag(Integer.valueOf(i));
                TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) ChatLinkActivity.this.chats.get(i - ChatLinkActivity.this.chatStartRow);
                if (TextUtils.isEmpty(tLRPC$Chat.username)) {
                    str = null;
                } else {
                    str = "@" + tLRPC$Chat.username;
                }
                if (i != ChatLinkActivity.this.chatEndRow - 1 || ChatLinkActivity.this.info.linked_chat_id != 0) {
                    z = true;
                }
                manageChatUserCell.setData(tLRPC$Chat, null, str, z);
            } else if (itemViewType == 1) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i != ChatLinkActivity.this.detailRow) {
                    return;
                }
                if (ChatLinkActivity.this.isChannel) {
                    textInfoPrivacyCell.setText(LocaleController.getString("DiscussionChannelHelp2", 2131625514));
                } else {
                    textInfoPrivacyCell.setText(LocaleController.getString("DiscussionGroupHelp2", 2131625518));
                }
            } else if (itemViewType != 2) {
            } else {
                ManageChatTextCell manageChatTextCell = (ManageChatTextCell) viewHolder.itemView;
                if (ChatLinkActivity.this.isChannel) {
                    if (ChatLinkActivity.this.info.linked_chat_id != 0) {
                        manageChatTextCell.setColors("windowBackgroundWhiteRedText5", "windowBackgroundWhiteRedText5");
                        manageChatTextCell.setText(LocaleController.getString("DiscussionUnlinkGroup", 2131625529), null, 2131165897, false);
                        return;
                    }
                    manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                    manageChatTextCell.setText(LocaleController.getString("DiscussionCreateGroup", 2131625516), null, 2131165750, true);
                    return;
                }
                manageChatTextCell.setColors("windowBackgroundWhiteRedText5", "windowBackgroundWhiteRedText5");
                manageChatTextCell.setText(LocaleController.getString("DiscussionUnlinkChannel", 2131625527), null, 2131165897, false);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            if (view instanceof ManageChatUserCell) {
                ((ManageChatUserCell) view).recycle();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == ChatLinkActivity.this.helpRow) {
                return 3;
            }
            if (i == ChatLinkActivity.this.createChatRow || i == ChatLinkActivity.this.removeChatRow) {
                return 2;
            }
            if (i >= ChatLinkActivity.this.chatStartRow && i < ChatLinkActivity.this.chatEndRow) {
                return 0;
            }
            return i == ChatLinkActivity.this.joinToSendRow ? 4 : 1;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        ChatLinkActivity$$ExternalSyntheticLambda17 chatLinkActivity$$ExternalSyntheticLambda17 = new ChatLinkActivity$$ExternalSyntheticLambda17(this);
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ManageChatUserCell.class, ManageChatTextCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, chatLinkActivity$$ExternalSyntheticLambda17, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, chatLinkActivity$$ExternalSyntheticLambda17, "windowBackgroundWhiteBlueText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatLinkActivity$$ExternalSyntheticLambda17, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatLinkActivity$$ExternalSyntheticLambda17, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatLinkActivity$$ExternalSyntheticLambda17, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatLinkActivity$$ExternalSyntheticLambda17, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatLinkActivity$$ExternalSyntheticLambda17, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatLinkActivity$$ExternalSyntheticLambda17, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, chatLinkActivity$$ExternalSyntheticLambda17, "avatar_backgroundPink"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HintInnerCell.class}, new String[]{"messageTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_message"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueButton"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueIcon"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$18() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof ManageChatUserCell) {
                    ((ManageChatUserCell) childAt).update(0);
                }
            }
        }
    }
}
