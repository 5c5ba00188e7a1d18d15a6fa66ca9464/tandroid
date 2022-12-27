package org.telegram.ui.Adapters;

import android.content.Context;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatInvite;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$TL_dialog;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterEmpty;
import org.telegram.tgnet.TLRPC$TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC$TL_messages_search;
import org.telegram.tgnet.TLRPC$TL_messages_searchGlobal;
import org.telegram.tgnet.TLRPC$TL_topPeer;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$messages_Messages;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
import org.telegram.ui.Adapters.FiltersView;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HashtagSearchCell;
import org.telegram.ui.Cells.HintDialogCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TopicSearchCell;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.FilteredSearchView;
/* loaded from: classes3.dex */
public class DialogsSearchAdapter extends RecyclerListView.SelectionAdapter {
    private Runnable cancelShowMoreAnimation;
    private int currentItemCount;
    public DialogsSearchAdapterDelegate delegate;
    private int dialogsType;
    private FilteredSearchView.Delegate filtersDelegate;
    private int folderId;
    private RecyclerListView innerListView;
    private DefaultItemAnimator itemAnimator;
    private int lastForumReqId;
    private int lastGlobalSearchId;
    private int lastLocalSearchId;
    private int lastMessagesSearchId;
    private String lastMessagesSearchString;
    private int lastReqId;
    private int lastSearchId;
    private String lastSearchText;
    private long lastShowMoreUpdate;
    private boolean localMessagesSearchEndReached;
    private boolean localTipArchive;
    private Context mContext;
    private boolean messagesSearchEndReached;
    private int needMessagesSearch;
    private int nextSearchRate;
    private SearchAdapterHelper searchAdapterHelper;
    private Runnable searchRunnable;
    private Runnable searchRunnable2;
    private boolean searchWas;
    private long selfUserId;
    public View showMoreHeader;
    int waitingResponseCount;
    private ArrayList<Object> searchResult = new ArrayList<>();
    private ArrayList<ContactsController.Contact> searchContacts = new ArrayList<>();
    private ArrayList<TLRPC$TL_forumTopic> searchTopics = new ArrayList<>();
    private ArrayList<CharSequence> searchResultNames = new ArrayList<>();
    private ArrayList<MessageObject> searchForumResultMessages = new ArrayList<>();
    private ArrayList<MessageObject> searchResultMessages = new ArrayList<>();
    private ArrayList<String> searchResultHashtags = new ArrayList<>();
    private int reqId = 0;
    private int reqForumId = 0;
    public int localMessagesLoadingRow = -1;
    public boolean showMoreAnimation = false;
    private int currentAccount = UserConfig.selectedAccount;
    private ArrayList<RecentSearchObject> recentSearchObjects = new ArrayList<>();
    private ArrayList<RecentSearchObject> filteredRecentSearchObjects = new ArrayList<>();
    private ArrayList<RecentSearchObject> filtered2RecentSearchObjects = new ArrayList<>();
    private String filteredRecentQuery = null;
    private LongSparseArray<RecentSearchObject> recentSearchObjectsById = new LongSparseArray<>();
    private ArrayList<FiltersView.DateData> localTipDates = new ArrayList<>();
    boolean globalSearchCollapsed = true;
    boolean phoneCollapsed = true;

    /* loaded from: classes3.dex */
    public static class DialogSearchResult {
        public int date;
        public CharSequence name;
        public TLObject object;
    }

    /* loaded from: classes3.dex */
    public interface DialogsSearchAdapterDelegate {
        void didPressedOnSubDialog(long j);

        long getSearchForumDialogId();

        boolean isSelected(long j);

        void needClearList();

        void needRemoveHint(long j);

        void runResultsEnterAnimation();

        void searchStateChanged(boolean z, boolean z2);
    }

    /* loaded from: classes3.dex */
    public interface OnRecentSearchLoaded {
        void setRecentSearch(ArrayList<RecentSearchObject> arrayList, LongSparseArray<RecentSearchObject> longSparseArray);
    }

    /* loaded from: classes3.dex */
    public static class RecentSearchObject {
        public int date;
        public long did;
        public TLObject object;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public boolean isSearching() {
        return this.waitingResponseCount > 0;
    }

    /* loaded from: classes3.dex */
    public static class CategoryAdapterRecycler extends RecyclerListView.SelectionAdapter {
        private final int currentAccount;
        private boolean drawChecked;
        private final Context mContext;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public CategoryAdapterRecycler(Context context, int i, boolean z) {
            this.drawChecked = z;
            this.mContext = context;
            this.currentAccount = i;
        }

        public void setIndex(int i) {
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            HintDialogCell hintDialogCell = new HintDialogCell(this.mContext, this.drawChecked);
            hintDialogCell.setLayoutParams(new RecyclerView.LayoutParams(AndroidUtilities.dp(80.0f), AndroidUtilities.dp(86.0f)));
            return new RecyclerListView.Holder(hintDialogCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC$Chat tLRPC$Chat;
            String str;
            HintDialogCell hintDialogCell = (HintDialogCell) viewHolder.itemView;
            TLRPC$TL_topPeer tLRPC$TL_topPeer = MediaDataController.getInstance(this.currentAccount).hints.get(i);
            new TLRPC$TL_dialog();
            TLRPC$Peer tLRPC$Peer = tLRPC$TL_topPeer.peer;
            long j = tLRPC$Peer.user_id;
            TLRPC$User tLRPC$User = null;
            if (j != 0) {
                tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$TL_topPeer.peer.user_id));
                tLRPC$Chat = null;
            } else {
                long j2 = tLRPC$Peer.channel_id;
                if (j2 != 0) {
                    j = -j2;
                    tLRPC$Chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$TL_topPeer.peer.channel_id));
                } else {
                    long j3 = tLRPC$Peer.chat_id;
                    if (j3 != 0) {
                        j = -j3;
                        tLRPC$Chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$TL_topPeer.peer.chat_id));
                    } else {
                        tLRPC$Chat = null;
                        j = 0;
                    }
                }
            }
            hintDialogCell.setTag(Long.valueOf(j));
            if (tLRPC$User != null) {
                str = UserObject.getFirstName(tLRPC$User);
            } else {
                str = tLRPC$Chat != null ? tLRPC$Chat.title : "";
            }
            hintDialogCell.setDialog(j, true, str);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return MediaDataController.getInstance(this.currentAccount).hints.size();
        }
    }

    public DialogsSearchAdapter(Context context, int i, int i2, DefaultItemAnimator defaultItemAnimator, boolean z) {
        this.itemAnimator = defaultItemAnimator;
        SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(false);
        this.searchAdapterHelper = searchAdapterHelper;
        searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter.1
            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ LongSparseArray getExcludeUsers() {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeUsers(this);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public void onDataSetChanged(int i3) {
                DialogsSearchAdapter dialogsSearchAdapter = DialogsSearchAdapter.this;
                dialogsSearchAdapter.waitingResponseCount--;
                dialogsSearchAdapter.lastGlobalSearchId = i3;
                if (DialogsSearchAdapter.this.lastLocalSearchId != i3) {
                    DialogsSearchAdapter.this.searchResult.clear();
                }
                if (DialogsSearchAdapter.this.lastMessagesSearchId != i3) {
                    DialogsSearchAdapter.this.searchResultMessages.clear();
                }
                DialogsSearchAdapter.this.searchWas = true;
                DialogsSearchAdapter dialogsSearchAdapter2 = DialogsSearchAdapter.this;
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = dialogsSearchAdapter2.delegate;
                if (dialogsSearchAdapterDelegate != null) {
                    dialogsSearchAdapterDelegate.searchStateChanged(dialogsSearchAdapter2.waitingResponseCount > 0, true);
                }
                DialogsSearchAdapter.this.notifyDataSetChanged();
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate2 = DialogsSearchAdapter.this.delegate;
                if (dialogsSearchAdapterDelegate2 != null) {
                    dialogsSearchAdapterDelegate2.runResultsEnterAnimation();
                }
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public void onSetHashtags(ArrayList<SearchAdapterHelper.HashtagObject> arrayList, HashMap<String, SearchAdapterHelper.HashtagObject> hashMap) {
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    DialogsSearchAdapter.this.searchResultHashtags.add(arrayList.get(i3).hashtag);
                }
                DialogsSearchAdapter dialogsSearchAdapter = DialogsSearchAdapter.this;
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = dialogsSearchAdapter.delegate;
                if (dialogsSearchAdapterDelegate != null) {
                    dialogsSearchAdapterDelegate.searchStateChanged(dialogsSearchAdapter.waitingResponseCount > 0, false);
                }
                DialogsSearchAdapter.this.notifyDataSetChanged();
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public boolean canApplySearchResults(int i3) {
                return i3 == DialogsSearchAdapter.this.lastSearchId;
            }
        });
        this.searchAdapterHelper.setAllowGlobalResults(z);
        this.mContext = context;
        this.needMessagesSearch = i;
        this.dialogsType = i2;
        this.selfUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        loadRecentSearch();
        MediaDataController.getInstance(this.currentAccount).loadHints(true);
    }

    public RecyclerListView getInnerListView() {
        return this.innerListView;
    }

    public void setDelegate(DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate) {
        this.delegate = dialogsSearchAdapterDelegate;
    }

    public boolean isMessagesSearchEndReached() {
        return (this.delegate.getSearchForumDialogId() == 0 || this.localMessagesSearchEndReached) && this.messagesSearchEndReached;
    }

    public void loadMoreSearchMessages() {
        if (this.reqForumId == 0 || this.reqId == 0) {
            DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
            if (dialogsSearchAdapterDelegate != null && dialogsSearchAdapterDelegate.getSearchForumDialogId() != 0 && !this.localMessagesSearchEndReached) {
                searchForumMessagesInternal(this.lastMessagesSearchString, this.lastMessagesSearchId);
            } else {
                searchMessagesInternal(this.lastMessagesSearchString, this.lastMessagesSearchId);
            }
        }
    }

    public String getLastSearchString() {
        return this.lastMessagesSearchString;
    }

    private void searchForumMessagesInternal(final String str, final int i) {
        DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
        if (dialogsSearchAdapterDelegate == null || dialogsSearchAdapterDelegate.getSearchForumDialogId() == 0 || this.needMessagesSearch == 0) {
            return;
        }
        if (TextUtils.isEmpty(this.lastMessagesSearchString) && TextUtils.isEmpty(str)) {
            return;
        }
        if (this.reqForumId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.reqForumId, true);
            this.reqForumId = 0;
        }
        if (TextUtils.isEmpty(str)) {
            this.filteredRecentQuery = null;
            this.searchResultMessages.clear();
            this.searchForumResultMessages.clear();
            this.lastForumReqId = 0;
            this.lastMessagesSearchString = null;
            this.searchWas = false;
            notifyDataSetChanged();
            return;
        }
        long searchForumDialogId = this.delegate.getSearchForumDialogId();
        final TLRPC$TL_messages_search tLRPC$TL_messages_search = new TLRPC$TL_messages_search();
        tLRPC$TL_messages_search.limit = 20;
        tLRPC$TL_messages_search.q = str;
        tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterEmpty();
        tLRPC$TL_messages_search.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(searchForumDialogId);
        if (str.equals(this.lastMessagesSearchString) && !this.searchForumResultMessages.isEmpty()) {
            tLRPC$TL_messages_search.add_offset = this.searchForumResultMessages.size();
        }
        this.lastMessagesSearchString = str;
        final int i2 = this.lastForumReqId + 1;
        this.lastForumReqId = i2;
        this.reqForumId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_search, new RequestDelegate() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda22
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                DialogsSearchAdapter.this.lambda$searchForumMessagesInternal$1(str, i2, i, tLRPC$TL_messages_search, tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchForumMessagesInternal$1(final String str, final int i, final int i2, final TLRPC$TL_messages_search tLRPC$TL_messages_search, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        final ArrayList arrayList = new ArrayList();
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            LongSparseArray longSparseArray = new LongSparseArray();
            LongSparseArray longSparseArray2 = new LongSparseArray();
            for (int i3 = 0; i3 < tLRPC$messages_Messages.chats.size(); i3++) {
                TLRPC$Chat tLRPC$Chat = tLRPC$messages_Messages.chats.get(i3);
                longSparseArray.put(tLRPC$Chat.id, tLRPC$Chat);
            }
            for (int i4 = 0; i4 < tLRPC$messages_Messages.users.size(); i4++) {
                TLRPC$User tLRPC$User = tLRPC$messages_Messages.users.get(i4);
                longSparseArray2.put(tLRPC$User.id, tLRPC$User);
            }
            for (int i5 = 0; i5 < tLRPC$messages_Messages.messages.size(); i5++) {
                MessageObject messageObject = new MessageObject(this.currentAccount, tLRPC$messages_Messages.messages.get(i5), (LongSparseArray<TLRPC$User>) longSparseArray2, (LongSparseArray<TLRPC$Chat>) longSparseArray, false, true);
                arrayList.add(messageObject);
                messageObject.setQuery(str);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$searchForumMessagesInternal$0(i, i2, tLRPC$TL_error, str, tLObject, tLRPC$TL_messages_search, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchForumMessagesInternal$0(int i, int i2, TLRPC$TL_error tLRPC$TL_error, String str, TLObject tLObject, TLRPC$TL_messages_search tLRPC$TL_messages_search, ArrayList arrayList) {
        if (i == this.lastForumReqId && (i2 <= 0 || i2 == this.lastSearchId)) {
            this.waitingResponseCount--;
            if (tLRPC$TL_error == null) {
                TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
                MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
                MessagesController.getInstance(this.currentAccount).putUsers(tLRPC$messages_Messages.users, false);
                MessagesController.getInstance(this.currentAccount).putChats(tLRPC$messages_Messages.chats, false);
                if (tLRPC$TL_messages_search.add_offset == 0) {
                    this.searchForumResultMessages.clear();
                }
                this.nextSearchRate = tLRPC$messages_Messages.next_rate;
                for (int i3 = 0; i3 < tLRPC$messages_Messages.messages.size(); i3++) {
                    TLRPC$Message tLRPC$Message = tLRPC$messages_Messages.messages.get(i3);
                    int i4 = MessagesController.getInstance(this.currentAccount).deletedHistory.get(MessageObject.getDialogId(tLRPC$Message));
                    if (i4 == 0 || tLRPC$Message.id > i4) {
                        this.searchForumResultMessages.add((MessageObject) arrayList.get(i3));
                    }
                }
                this.searchWas = true;
                this.localMessagesSearchEndReached = tLRPC$messages_Messages.messages.size() != 20;
                if (i2 > 0) {
                    this.lastMessagesSearchId = i2;
                    if (this.lastLocalSearchId != i2) {
                        this.searchResult.clear();
                    }
                    if (this.lastGlobalSearchId != i2) {
                        this.searchAdapterHelper.clear();
                    }
                }
                this.searchAdapterHelper.mergeResults(this.searchResult, this.filtered2RecentSearchObjects);
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
                if (dialogsSearchAdapterDelegate != null) {
                    dialogsSearchAdapterDelegate.searchStateChanged(this.waitingResponseCount > 0, true);
                    this.delegate.runResultsEnterAnimation();
                }
                notifyDataSetChanged();
            }
        }
        this.reqForumId = 0;
    }

    private void searchTopics(String str) {
        this.searchTopics.clear();
        DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
        if (dialogsSearchAdapterDelegate == null || dialogsSearchAdapterDelegate.getSearchForumDialogId() == 0) {
            return;
        }
        if (!TextUtils.isEmpty(str)) {
            ArrayList<TLRPC$TL_forumTopic> topics = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopics(-this.delegate.getSearchForumDialogId());
            String trim = str.trim();
            for (int i = 0; i < topics.size(); i++) {
                if (topics.get(i) != null && topics.get(i).title.toLowerCase().contains(trim)) {
                    this.searchTopics.add(topics.get(i));
                    topics.get(i).searchQuery = trim;
                }
            }
        }
        notifyDataSetChanged();
    }

    private void searchMessagesInternal(final String str, final int i) {
        if (this.needMessagesSearch != 0) {
            if (TextUtils.isEmpty(this.lastMessagesSearchString) && TextUtils.isEmpty(str)) {
                return;
            }
            if (this.reqId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.reqId, true);
                this.reqId = 0;
            }
            if (TextUtils.isEmpty(str)) {
                this.filteredRecentQuery = null;
                this.searchResultMessages.clear();
                this.searchForumResultMessages.clear();
                this.lastReqId = 0;
                this.lastMessagesSearchString = null;
                this.searchWas = false;
                notifyDataSetChanged();
                return;
            }
            filterRecent(str);
            this.searchAdapterHelper.mergeResults(this.searchResult, this.filtered2RecentSearchObjects);
            final TLRPC$TL_messages_searchGlobal tLRPC$TL_messages_searchGlobal = new TLRPC$TL_messages_searchGlobal();
            tLRPC$TL_messages_searchGlobal.limit = 20;
            tLRPC$TL_messages_searchGlobal.q = str;
            tLRPC$TL_messages_searchGlobal.filter = new TLRPC$TL_inputMessagesFilterEmpty();
            tLRPC$TL_messages_searchGlobal.flags |= 1;
            tLRPC$TL_messages_searchGlobal.folder_id = this.folderId;
            if (str.equals(this.lastMessagesSearchString) && !this.searchResultMessages.isEmpty()) {
                ArrayList<MessageObject> arrayList = this.searchResultMessages;
                MessageObject messageObject = arrayList.get(arrayList.size() - 1);
                tLRPC$TL_messages_searchGlobal.offset_id = messageObject.getId();
                tLRPC$TL_messages_searchGlobal.offset_rate = this.nextSearchRate;
                tLRPC$TL_messages_searchGlobal.offset_peer = MessagesController.getInstance(this.currentAccount).getInputPeer(MessageObject.getPeerId(messageObject.messageOwner.peer_id));
            } else {
                tLRPC$TL_messages_searchGlobal.offset_rate = 0;
                tLRPC$TL_messages_searchGlobal.offset_id = 0;
                tLRPC$TL_messages_searchGlobal.offset_peer = new TLRPC$TL_inputPeerEmpty();
            }
            this.lastMessagesSearchString = str;
            final int i2 = this.lastReqId + 1;
            this.lastReqId = i2;
            this.reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_searchGlobal, new RequestDelegate() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda23
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    DialogsSearchAdapter.this.lambda$searchMessagesInternal$3(str, i2, i, tLRPC$TL_messages_searchGlobal, tLObject, tLRPC$TL_error);
                }
            }, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchMessagesInternal$3(final String str, final int i, final int i2, final TLRPC$TL_messages_searchGlobal tLRPC$TL_messages_searchGlobal, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        final ArrayList arrayList = new ArrayList();
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            LongSparseArray longSparseArray = new LongSparseArray();
            LongSparseArray longSparseArray2 = new LongSparseArray();
            for (int i3 = 0; i3 < tLRPC$messages_Messages.chats.size(); i3++) {
                TLRPC$Chat tLRPC$Chat = tLRPC$messages_Messages.chats.get(i3);
                longSparseArray.put(tLRPC$Chat.id, tLRPC$Chat);
            }
            for (int i4 = 0; i4 < tLRPC$messages_Messages.users.size(); i4++) {
                TLRPC$User tLRPC$User = tLRPC$messages_Messages.users.get(i4);
                longSparseArray2.put(tLRPC$User.id, tLRPC$User);
            }
            for (int i5 = 0; i5 < tLRPC$messages_Messages.messages.size(); i5++) {
                MessageObject messageObject = new MessageObject(this.currentAccount, tLRPC$messages_Messages.messages.get(i5), (LongSparseArray<TLRPC$User>) longSparseArray2, (LongSparseArray<TLRPC$Chat>) longSparseArray, false, true);
                arrayList.add(messageObject);
                messageObject.setQuery(str);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$searchMessagesInternal$2(i, i2, tLRPC$TL_error, str, tLObject, tLRPC$TL_messages_searchGlobal, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchMessagesInternal$2(int i, int i2, TLRPC$TL_error tLRPC$TL_error, String str, TLObject tLObject, TLRPC$TL_messages_searchGlobal tLRPC$TL_messages_searchGlobal, ArrayList arrayList) {
        boolean z;
        if (i == this.lastReqId && (i2 <= 0 || i2 == this.lastSearchId)) {
            this.waitingResponseCount--;
            if (tLRPC$TL_error == null) {
                TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
                MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
                MessagesController.getInstance(this.currentAccount).putUsers(tLRPC$messages_Messages.users, false);
                MessagesController.getInstance(this.currentAccount).putChats(tLRPC$messages_Messages.chats, false);
                if (tLRPC$TL_messages_searchGlobal.offset_id == 0) {
                    this.searchResultMessages.clear();
                }
                this.nextSearchRate = tLRPC$messages_Messages.next_rate;
                for (int i3 = 0; i3 < tLRPC$messages_Messages.messages.size(); i3++) {
                    TLRPC$Message tLRPC$Message = tLRPC$messages_Messages.messages.get(i3);
                    int i4 = MessagesController.getInstance(this.currentAccount).deletedHistory.get(MessageObject.getDialogId(tLRPC$Message));
                    if (i4 == 0 || tLRPC$Message.id > i4) {
                        MessageObject messageObject = (MessageObject) arrayList.get(i3);
                        if (!this.searchForumResultMessages.isEmpty()) {
                            int i5 = 0;
                            while (true) {
                                if (i5 >= this.searchForumResultMessages.size()) {
                                    z = false;
                                    break;
                                }
                                MessageObject messageObject2 = this.searchForumResultMessages.get(i5);
                                if (messageObject2 != null && messageObject != null && messageObject.getId() == messageObject2.getId() && messageObject.getDialogId() == messageObject2.getDialogId()) {
                                    z = true;
                                    break;
                                }
                                i5++;
                            }
                            if (z) {
                            }
                        }
                        this.searchResultMessages.add(messageObject);
                        long dialogId = MessageObject.getDialogId(tLRPC$Message);
                        ConcurrentHashMap<Long, Integer> concurrentHashMap = tLRPC$Message.out ? MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max : MessagesController.getInstance(this.currentAccount).dialogs_read_inbox_max;
                        Integer num = concurrentHashMap.get(Long.valueOf(dialogId));
                        if (num == null) {
                            num = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(tLRPC$Message.out, dialogId));
                            concurrentHashMap.put(Long.valueOf(dialogId), num);
                        }
                        tLRPC$Message.unread = num.intValue() < tLRPC$Message.id;
                    }
                }
                this.searchWas = true;
                this.messagesSearchEndReached = tLRPC$messages_Messages.messages.size() != 20;
                if (i2 > 0) {
                    this.lastMessagesSearchId = i2;
                    if (this.lastLocalSearchId != i2) {
                        this.searchResult.clear();
                    }
                    if (this.lastGlobalSearchId != i2) {
                        this.searchAdapterHelper.clear();
                    }
                }
                this.searchAdapterHelper.mergeResults(this.searchResult, this.filtered2RecentSearchObjects);
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
                if (dialogsSearchAdapterDelegate != null) {
                    dialogsSearchAdapterDelegate.searchStateChanged(this.waitingResponseCount > 0, true);
                    this.delegate.runResultsEnterAnimation();
                }
                this.globalSearchCollapsed = true;
                this.phoneCollapsed = true;
                notifyDataSetChanged();
            }
        }
        this.reqId = 0;
    }

    public boolean hasRecentSearch() {
        return resentSearchAvailable() && getRecentItemsCount() > 0;
    }

    private boolean resentSearchAvailable() {
        int i = this.dialogsType;
        return (i == 2 || i == 4 || i == 5 || i == 6 || i == 11) ? false : true;
    }

    public boolean isSearchWas() {
        return this.searchWas;
    }

    public boolean isRecentSearchDisplayed() {
        return this.needMessagesSearch != 2 && hasRecentSearch();
    }

    public void loadRecentSearch() {
        loadRecentSearch(this.currentAccount, this.dialogsType, new OnRecentSearchLoaded() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda24
            @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.OnRecentSearchLoaded
            public final void setRecentSearch(ArrayList arrayList, LongSparseArray longSparseArray) {
                DialogsSearchAdapter.this.lambda$loadRecentSearch$4(arrayList, longSparseArray);
            }
        });
    }

    public static void loadRecentSearch(final int i, final int i2, final OnRecentSearchLoaded onRecentSearchLoaded) {
        MessagesStorage.getInstance(i).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.lambda$loadRecentSearch$7(i, i2, onRecentSearchLoaded);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadRecentSearch$7(int i, int i2, final OnRecentSearchLoaded onRecentSearchLoaded) {
        boolean z;
        try {
            SQLiteCursor queryFinalized = MessagesStorage.getInstance(i).getDatabase().queryFinalized("SELECT did, date FROM search_recent WHERE 1", new Object[0]);
            ArrayList<Long> arrayList = new ArrayList<>();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            new ArrayList();
            final ArrayList arrayList4 = new ArrayList();
            final LongSparseArray longSparseArray = new LongSparseArray();
            while (queryFinalized.next()) {
                long longValue = queryFinalized.longValue(0);
                if (DialogObject.isEncryptedDialog(longValue)) {
                    if (i2 == 0 || i2 == 3) {
                        int encryptedChatId = DialogObject.getEncryptedChatId(longValue);
                        if (!arrayList3.contains(Integer.valueOf(encryptedChatId))) {
                            arrayList3.add(Integer.valueOf(encryptedChatId));
                            z = true;
                        }
                    }
                    z = false;
                } else if (DialogObject.isUserDialog(longValue)) {
                    if (i2 != 2 && !arrayList.contains(Long.valueOf(longValue))) {
                        arrayList.add(Long.valueOf(longValue));
                        z = true;
                    }
                    z = false;
                } else {
                    long j = -longValue;
                    if (!arrayList2.contains(Long.valueOf(j))) {
                        arrayList2.add(Long.valueOf(j));
                        z = true;
                    }
                    z = false;
                }
                if (z) {
                    RecentSearchObject recentSearchObject = new RecentSearchObject();
                    recentSearchObject.did = longValue;
                    recentSearchObject.date = queryFinalized.intValue(1);
                    arrayList4.add(recentSearchObject);
                    longSparseArray.put(recentSearchObject.did, recentSearchObject);
                }
            }
            queryFinalized.dispose();
            ArrayList<TLRPC$User> arrayList5 = new ArrayList<>();
            if (!arrayList3.isEmpty()) {
                ArrayList<TLRPC$EncryptedChat> arrayList6 = new ArrayList<>();
                MessagesStorage.getInstance(i).getEncryptedChatsInternal(TextUtils.join(",", arrayList3), arrayList6, arrayList);
                for (int i3 = 0; i3 < arrayList6.size(); i3++) {
                    RecentSearchObject recentSearchObject2 = (RecentSearchObject) longSparseArray.get(DialogObject.makeEncryptedDialogId(arrayList6.get(i3).id));
                    if (recentSearchObject2 != null) {
                        recentSearchObject2.object = arrayList6.get(i3);
                    }
                }
            }
            if (!arrayList2.isEmpty()) {
                ArrayList<TLRPC$Chat> arrayList7 = new ArrayList<>();
                MessagesStorage.getInstance(i).getChatsInternal(TextUtils.join(",", arrayList2), arrayList7);
                for (int i4 = 0; i4 < arrayList7.size(); i4++) {
                    TLRPC$Chat tLRPC$Chat = arrayList7.get(i4);
                    long j2 = -tLRPC$Chat.id;
                    if (tLRPC$Chat.migrated_to != null) {
                        RecentSearchObject recentSearchObject3 = (RecentSearchObject) longSparseArray.get(j2);
                        longSparseArray.remove(j2);
                        if (recentSearchObject3 != null) {
                            arrayList4.remove(recentSearchObject3);
                        }
                    } else {
                        RecentSearchObject recentSearchObject4 = (RecentSearchObject) longSparseArray.get(j2);
                        if (recentSearchObject4 != null) {
                            recentSearchObject4.object = tLRPC$Chat;
                        }
                    }
                }
            }
            if (!arrayList.isEmpty()) {
                MessagesStorage.getInstance(i).getUsersInternal(TextUtils.join(",", arrayList), arrayList5);
                for (int i5 = 0; i5 < arrayList5.size(); i5++) {
                    TLRPC$User tLRPC$User = arrayList5.get(i5);
                    RecentSearchObject recentSearchObject5 = (RecentSearchObject) longSparseArray.get(tLRPC$User.id);
                    if (recentSearchObject5 != null) {
                        recentSearchObject5.object = tLRPC$User;
                    }
                }
            }
            Collections.sort(arrayList4, DialogsSearchAdapter$$ExternalSyntheticLambda20.INSTANCE);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsSearchAdapter.OnRecentSearchLoaded.this.setRecentSearch(arrayList4, longSparseArray);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$loadRecentSearch$5(RecentSearchObject recentSearchObject, RecentSearchObject recentSearchObject2) {
        int i = recentSearchObject.date;
        int i2 = recentSearchObject2.date;
        if (i < i2) {
            return 1;
        }
        return i > i2 ? -1 : 0;
    }

    public void putRecentSearch(final long j, TLObject tLObject) {
        RecentSearchObject recentSearchObject = this.recentSearchObjectsById.get(j);
        if (recentSearchObject == null) {
            recentSearchObject = new RecentSearchObject();
            this.recentSearchObjectsById.put(j, recentSearchObject);
        } else {
            this.recentSearchObjects.remove(recentSearchObject);
        }
        this.recentSearchObjects.add(0, recentSearchObject);
        recentSearchObject.did = j;
        recentSearchObject.object = tLObject;
        recentSearchObject.date = (int) (System.currentTimeMillis() / 1000);
        notifyDataSetChanged();
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$putRecentSearch$8(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putRecentSearch$8(long j) {
        try {
            SQLitePreparedStatement executeFast = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO search_recent VALUES(?, ?)");
            executeFast.requery();
            executeFast.bindLong(1, j);
            executeFast.bindInteger(2, (int) (System.currentTimeMillis() / 1000));
            executeFast.step();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void clearRecentSearch() {
        final StringBuilder sb;
        if (this.searchWas) {
            sb = null;
            while (this.filtered2RecentSearchObjects.size() > 0) {
                RecentSearchObject remove = this.filtered2RecentSearchObjects.remove(0);
                this.recentSearchObjects.remove(remove);
                this.filteredRecentSearchObjects.remove(remove);
                this.recentSearchObjectsById.remove(remove.did);
                if (sb == null) {
                    sb = new StringBuilder("did IN (");
                    sb.append(remove.did);
                } else {
                    sb.append(", ");
                    sb.append(remove.did);
                }
            }
            if (sb == null) {
                sb = new StringBuilder("1");
            } else {
                sb.append(")");
            }
        } else {
            this.filtered2RecentSearchObjects.clear();
            this.filteredRecentSearchObjects.clear();
            this.recentSearchObjects.clear();
            this.recentSearchObjectsById.clear();
            sb = new StringBuilder("1");
        }
        notifyDataSetChanged();
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$clearRecentSearch$9(sb);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearRecentSearch$9(StringBuilder sb) {
        try {
            sb.insert(0, "DELETE FROM search_recent WHERE ");
            MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast(sb.toString()).stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void removeRecentSearch(final long j) {
        RecentSearchObject recentSearchObject = this.recentSearchObjectsById.get(j);
        if (recentSearchObject == null) {
            return;
        }
        this.recentSearchObjectsById.remove(j);
        this.recentSearchObjects.remove(recentSearchObject);
        this.filtered2RecentSearchObjects.remove(recentSearchObject);
        this.filteredRecentSearchObjects.remove(recentSearchObject);
        notifyDataSetChanged();
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$removeRecentSearch$10(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeRecentSearch$10(long j) {
        try {
            SQLiteDatabase database = MessagesStorage.getInstance(this.currentAccount).getDatabase();
            database.executeFast("DELETE FROM search_recent WHERE did = " + j).stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void addHashtagsFromMessage(CharSequence charSequence) {
        this.searchAdapterHelper.addHashtagsFromMessage(charSequence);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setRecentSearch */
    public void lambda$loadRecentSearch$4(ArrayList<RecentSearchObject> arrayList, LongSparseArray<RecentSearchObject> longSparseArray) {
        this.recentSearchObjects = arrayList;
        this.recentSearchObjectsById = longSparseArray;
        for (int i = 0; i < this.recentSearchObjects.size(); i++) {
            RecentSearchObject recentSearchObject = this.recentSearchObjects.get(i);
            TLObject tLObject = recentSearchObject.object;
            if (tLObject instanceof TLRPC$User) {
                MessagesController.getInstance(this.currentAccount).putUser((TLRPC$User) recentSearchObject.object, true);
            } else if (tLObject instanceof TLRPC$Chat) {
                MessagesController.getInstance(this.currentAccount).putChat((TLRPC$Chat) recentSearchObject.object, true);
            } else if (tLObject instanceof TLRPC$EncryptedChat) {
                MessagesController.getInstance(this.currentAccount).putEncryptedChat((TLRPC$EncryptedChat) recentSearchObject.object, true);
            }
        }
        filterRecent(null);
        notifyDataSetChanged();
    }

    private void searchDialogsInternal(final String str, final int i) {
        if (this.needMessagesSearch == 2) {
            return;
        }
        final String lowerCase = str.trim().toLowerCase();
        if (lowerCase.length() == 0) {
            this.lastSearchId = 0;
            updateSearchResults(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), this.lastSearchId);
            return;
        }
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$searchDialogsInternal$12(lowerCase, i, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchDialogsInternal$12(String str, int i, String str2) {
        ArrayList<Object> arrayList = new ArrayList<>();
        ArrayList<CharSequence> arrayList2 = new ArrayList<>();
        ArrayList<TLRPC$User> arrayList3 = new ArrayList<>();
        ArrayList<ContactsController.Contact> arrayList4 = new ArrayList<>();
        MessagesStorage.getInstance(this.currentAccount).localSearch(this.dialogsType, str, arrayList, arrayList2, arrayList3, -1);
        updateSearchResults(arrayList, arrayList2, arrayList3, arrayList4, i);
        FiltersView.fillTipDates(str, this.localTipDates);
        this.localTipArchive = false;
        if (str.length() >= 3 && (LocaleController.getString("ArchiveSearchFilter", R.string.ArchiveSearchFilter).toLowerCase().startsWith(str) || "archive".startsWith(str2))) {
            this.localTipArchive = true;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$searchDialogsInternal$11();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchDialogsInternal$11() {
        FilteredSearchView.Delegate delegate = this.filtersDelegate;
        if (delegate != null) {
            delegate.updateFiltersView(false, null, this.localTipDates, this.localTipArchive);
        }
    }

    private void updateSearchResults(final ArrayList<Object> arrayList, final ArrayList<CharSequence> arrayList2, final ArrayList<TLRPC$User> arrayList3, ArrayList<ContactsController.Contact> arrayList4, final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$updateSearchResults$14(i, arrayList, arrayList2, arrayList3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSearchResults$14(int i, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3) {
        final long j;
        this.waitingResponseCount--;
        if (i != this.lastSearchId) {
            return;
        }
        this.lastLocalSearchId = i;
        if (this.lastGlobalSearchId != i) {
            this.searchAdapterHelper.clear();
        }
        if (this.lastMessagesSearchId != i) {
            this.searchResultMessages.clear();
        }
        this.searchWas = true;
        int size = this.filtered2RecentSearchObjects.size();
        int i2 = 0;
        while (i2 < arrayList.size()) {
            final Object obj = arrayList.get(i2);
            if (obj instanceof TLRPC$User) {
                TLRPC$User tLRPC$User = (TLRPC$User) obj;
                MessagesController.getInstance(this.currentAccount).putUser(tLRPC$User, true);
                j = tLRPC$User.id;
            } else if (obj instanceof TLRPC$Chat) {
                TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) obj;
                MessagesController.getInstance(this.currentAccount).putChat(tLRPC$Chat, true);
                j = -tLRPC$Chat.id;
            } else {
                if (obj instanceof TLRPC$EncryptedChat) {
                    MessagesController.getInstance(this.currentAccount).putEncryptedChat((TLRPC$EncryptedChat) obj, true);
                }
                j = 0;
            }
            if (j != 0 && MessagesController.getInstance(this.currentAccount).dialogs_dict.get(j) == null) {
                MessagesStorage.getInstance(this.currentAccount).getDialogFolderId(j, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda21
                    @Override // org.telegram.messenger.MessagesStorage.IntCallback
                    public final void run(int i3) {
                        DialogsSearchAdapter.this.lambda$updateSearchResults$13(j, obj, i3);
                    }
                });
            }
            if (resentSearchAvailable()) {
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
                boolean z = dialogsSearchAdapterDelegate != null && dialogsSearchAdapterDelegate.getSearchForumDialogId() == j;
                for (int i3 = 0; !z && i3 < size; i3++) {
                    RecentSearchObject recentSearchObject = this.filtered2RecentSearchObjects.get(i3);
                    if (recentSearchObject != null && recentSearchObject.did == j) {
                        z = true;
                    }
                }
                if (z) {
                    arrayList.remove(i2);
                    arrayList2.remove(i2);
                    i2--;
                }
            }
            i2++;
        }
        MessagesController.getInstance(this.currentAccount).putUsers(arrayList3, true);
        this.searchResult = arrayList;
        this.searchResultNames = arrayList2;
        this.searchAdapterHelper.mergeResults(arrayList, this.filtered2RecentSearchObjects);
        notifyDataSetChanged();
        DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate2 = this.delegate;
        if (dialogsSearchAdapterDelegate2 != null) {
            dialogsSearchAdapterDelegate2.searchStateChanged(this.waitingResponseCount > 0, true);
            this.delegate.runResultsEnterAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSearchResults$13(long j, Object obj, int i) {
        if (i != -1) {
            TLRPC$TL_dialog tLRPC$TL_dialog = new TLRPC$TL_dialog();
            tLRPC$TL_dialog.id = j;
            if (i != 0) {
                tLRPC$TL_dialog.folder_id = i;
            }
            if (obj instanceof TLRPC$Chat) {
                tLRPC$TL_dialog.flags = ChatObject.isChannel((TLRPC$Chat) obj) ? 1 : 0;
            }
            MessagesController.getInstance(this.currentAccount).dialogs_dict.put(j, tLRPC$TL_dialog);
            MessagesController.getInstance(this.currentAccount).getAllDialogs().add(tLRPC$TL_dialog);
            MessagesController.getInstance(this.currentAccount).sortDialogs(null);
        }
    }

    public boolean isHashtagSearch() {
        return !this.searchResultHashtags.isEmpty();
    }

    public void clearRecentHashtags() {
        this.searchAdapterHelper.clearRecentHashtags();
        this.searchResultHashtags.clear();
        notifyDataSetChanged();
    }

    public void searchDialogs(final String str, int i) {
        if (str != null && str.equals(this.lastSearchText) && (i == this.folderId || TextUtils.isEmpty(str))) {
            return;
        }
        this.lastSearchText = str;
        this.folderId = i;
        if (this.searchRunnable != null) {
            Utilities.searchQueue.cancelRunnable(this.searchRunnable);
            this.searchRunnable = null;
        }
        Runnable runnable = this.searchRunnable2;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.searchRunnable2 = null;
        }
        final String trim = str != null ? str.trim() : null;
        filterRecent(trim);
        if (TextUtils.isEmpty(trim)) {
            this.filteredRecentQuery = null;
            this.searchAdapterHelper.unloadRecentHashtags();
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.searchResultHashtags.clear();
            this.searchAdapterHelper.mergeResults(null, null);
            SearchAdapterHelper searchAdapterHelper = this.searchAdapterHelper;
            int i2 = this.dialogsType;
            boolean z = i2 != 11;
            boolean z2 = i2 != 11;
            boolean z3 = i2 == 2 || i2 == 11;
            boolean z4 = i2 == 0;
            DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
            searchAdapterHelper.queryServerSearch(null, true, true, z, z2, z3, 0L, z4, 0, 0, dialogsSearchAdapterDelegate != null ? dialogsSearchAdapterDelegate.getSearchForumDialogId() : 0L);
            this.searchWas = false;
            this.lastSearchId = 0;
            this.waitingResponseCount = 0;
            this.globalSearchCollapsed = true;
            this.phoneCollapsed = true;
            DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate2 = this.delegate;
            if (dialogsSearchAdapterDelegate2 != null) {
                dialogsSearchAdapterDelegate2.searchStateChanged(false, true);
            }
            searchTopics(null);
            searchMessagesInternal(null, 0);
            searchForumMessagesInternal(null, 0);
            notifyDataSetChanged();
            this.localTipDates.clear();
            this.localTipArchive = false;
            FilteredSearchView.Delegate delegate = this.filtersDelegate;
            if (delegate != null) {
                delegate.updateFiltersView(false, null, this.localTipDates, false);
                return;
            }
            return;
        }
        this.searchAdapterHelper.mergeResults(this.searchResult, this.filtered2RecentSearchObjects);
        if (this.needMessagesSearch != 2 && trim.startsWith("#") && trim.length() == 1) {
            this.messagesSearchEndReached = true;
            if (this.searchAdapterHelper.loadRecentHashtags()) {
                this.searchResultMessages.clear();
                this.searchResultHashtags.clear();
                ArrayList<SearchAdapterHelper.HashtagObject> hashtags = this.searchAdapterHelper.getHashtags();
                for (int i3 = 0; i3 < hashtags.size(); i3++) {
                    this.searchResultHashtags.add(hashtags.get(i3).hashtag);
                }
                this.globalSearchCollapsed = true;
                this.phoneCollapsed = true;
                this.waitingResponseCount = 0;
                notifyDataSetChanged();
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate3 = this.delegate;
                if (dialogsSearchAdapterDelegate3 != null) {
                    dialogsSearchAdapterDelegate3.searchStateChanged(false, false);
                }
            }
        } else {
            this.searchResultHashtags.clear();
        }
        final int i4 = this.lastSearchId + 1;
        this.lastSearchId = i4;
        this.waitingResponseCount = 3;
        this.globalSearchCollapsed = true;
        this.phoneCollapsed = true;
        notifyDataSetChanged();
        DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate4 = this.delegate;
        if (dialogsSearchAdapterDelegate4 != null) {
            dialogsSearchAdapterDelegate4.searchStateChanged(true, false);
        }
        DispatchQueue dispatchQueue = Utilities.searchQueue;
        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$searchDialogs$16(trim, i4, str);
            }
        };
        this.searchRunnable = runnable2;
        dispatchQueue.postRunnable(runnable2, 300L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchDialogs$16(final String str, final int i, final String str2) {
        this.searchRunnable = null;
        searchDialogsInternal(str, i);
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$searchDialogs$15(i, str, str2);
            }
        };
        this.searchRunnable2 = runnable;
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchDialogs$15(int i, String str, String str2) {
        String str3;
        this.searchRunnable2 = null;
        if (i != this.lastSearchId) {
            return;
        }
        if (this.needMessagesSearch != 2) {
            SearchAdapterHelper searchAdapterHelper = this.searchAdapterHelper;
            int i2 = this.dialogsType;
            boolean z = i2 != 4;
            boolean z2 = (i2 == 4 || i2 == 11) ? false : true;
            boolean z3 = i2 == 2 || i2 == 1;
            boolean z4 = i2 == 0;
            DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
            str3 = str2;
            searchAdapterHelper.queryServerSearch(str, true, z, true, z2, z3, 0L, z4, 0, i, dialogsSearchAdapterDelegate != null ? dialogsSearchAdapterDelegate.getSearchForumDialogId() : 0L);
        } else {
            str3 = str2;
            this.waitingResponseCount -= 2;
        }
        if (this.needMessagesSearch == 0) {
            this.waitingResponseCount--;
            return;
        }
        searchTopics(str3);
        String str4 = str3;
        searchMessagesInternal(str4, i);
        searchForumMessagesInternal(str4, i);
    }

    public int getRecentItemsCount() {
        ArrayList<RecentSearchObject> arrayList = this.searchWas ? this.filtered2RecentSearchObjects : this.filteredRecentSearchObjects;
        int i = 1;
        return (!arrayList.isEmpty() ? arrayList.size() + 1 : 0) + ((this.searchWas || MediaDataController.getInstance(this.currentAccount).hints.isEmpty()) ? 0 : 0);
    }

    public int getRecentResultsCount() {
        ArrayList<RecentSearchObject> arrayList = this.searchWas ? this.filtered2RecentSearchObjects : this.filteredRecentSearchObjects;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        int i;
        int i2 = 0;
        int i3 = 3;
        if (this.waitingResponseCount == 3) {
            return 0;
        }
        if (!this.searchResultHashtags.isEmpty()) {
            return this.searchResultHashtags.size() + 1 + 0;
        }
        if (isRecentSearchDisplayed()) {
            i = getRecentItemsCount() + 0;
            if (!this.searchWas) {
                return i;
            }
        } else {
            i = 0;
        }
        if (!this.searchTopics.isEmpty()) {
            i = i + 1 + this.searchTopics.size();
        }
        if (!this.searchContacts.isEmpty()) {
            i += this.searchContacts.size() + 1;
        }
        int size = this.searchResult.size();
        int size2 = this.searchAdapterHelper.getLocalServerSearch().size();
        int i4 = i + size + size2;
        int size3 = this.searchAdapterHelper.getGlobalSearch().size();
        if (size3 > 3 && this.globalSearchCollapsed) {
            size3 = 3;
        }
        int size4 = this.searchAdapterHelper.getPhoneSearch().size();
        if (size4 <= 3 || !this.phoneCollapsed) {
            i3 = size4;
        }
        if (size + size2 > 0 && (getRecentItemsCount() > 0 || !this.searchTopics.isEmpty())) {
            i4++;
        }
        if (size3 != 0) {
            i4 += size3 + 1;
        }
        if (i3 != 0) {
            i4 += i3;
        }
        int size5 = this.searchForumResultMessages.size();
        if (size5 != 0) {
            i4 += size5 + 1 + (!this.localMessagesSearchEndReached ? 1 : 0);
        }
        if (!this.localMessagesSearchEndReached) {
            this.localMessagesLoadingRow = i4;
        }
        int size6 = this.searchResultMessages.size();
        if (this.searchForumResultMessages.isEmpty() || this.localMessagesSearchEndReached) {
            i2 = size6;
        }
        if (i2 != 0) {
            i4 += i2 + 1 + (!this.messagesSearchEndReached ? 1 : 0);
        }
        if (this.localMessagesSearchEndReached) {
            this.localMessagesLoadingRow = i4;
        }
        this.currentItemCount = i4;
        return i4;
    }

    public Object getItem(int i) {
        int i2;
        Object chat;
        if (!this.searchResultHashtags.isEmpty()) {
            if (i > 0) {
                return this.searchResultHashtags.get(i - 1);
            }
            return null;
        }
        int i3 = 0;
        if (isRecentSearchDisplayed()) {
            int i4 = (this.searchWas || MediaDataController.getInstance(this.currentAccount).hints.isEmpty()) ? 0 : 1;
            ArrayList<RecentSearchObject> arrayList = this.searchWas ? this.filtered2RecentSearchObjects : this.filteredRecentSearchObjects;
            if (i > i4 && (i2 = (i - 1) - i4) < arrayList.size()) {
                TLObject tLObject = arrayList.get(i2).object;
                if (tLObject instanceof TLRPC$User) {
                    chat = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(((TLRPC$User) tLObject).id));
                    if (chat == null) {
                        return tLObject;
                    }
                } else if (!(tLObject instanceof TLRPC$Chat) || (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(((TLRPC$Chat) tLObject).id))) == null) {
                    return tLObject;
                }
                return chat;
            }
            i -= getRecentItemsCount();
        }
        if (!this.searchTopics.isEmpty()) {
            if (i > 0 && i <= this.searchTopics.size()) {
                return this.searchTopics.get(i - 1);
            }
            i -= this.searchTopics.size() + 1;
        }
        if (!this.searchContacts.isEmpty()) {
            if (i > 0 && i <= this.searchContacts.size()) {
                return this.searchContacts.get(i - 1);
            }
            i -= this.searchContacts.size() + 1;
        }
        ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
        ArrayList<TLObject> localServerSearch = this.searchAdapterHelper.getLocalServerSearch();
        ArrayList<Object> phoneSearch = this.searchAdapterHelper.getPhoneSearch();
        int size = this.searchResult.size();
        int size2 = localServerSearch.size();
        if (size + size2 > 0 && (getRecentItemsCount() > 0 || !this.searchTopics.isEmpty())) {
            if (i == 0) {
                return null;
            }
            i--;
        }
        int size3 = phoneSearch.size();
        if (size3 > 3 && this.phoneCollapsed) {
            size3 = 3;
        }
        int size4 = globalSearch.isEmpty() ? 0 : globalSearch.size() + 1;
        if (size4 > 4 && this.globalSearchCollapsed) {
            size4 = 4;
        }
        if (i < 0 || i >= size) {
            int i5 = i - size;
            if (i5 < 0 || i5 >= size2) {
                int i6 = i5 - size2;
                if (i6 < 0 || i6 >= size3) {
                    int i7 = i6 - size3;
                    if (i7 > 0 && i7 < size4) {
                        return globalSearch.get(i7 - 1);
                    }
                    int i8 = i7 - size4;
                    int size5 = this.searchForumResultMessages.isEmpty() ? 0 : this.searchForumResultMessages.size() + 1;
                    if (i8 > 0 && i8 <= this.searchForumResultMessages.size()) {
                        return this.searchForumResultMessages.get(i8 - 1);
                    }
                    if (!this.localMessagesSearchEndReached && !this.searchForumResultMessages.isEmpty()) {
                        i3 = 1;
                    }
                    int i9 = i8 - (size5 + i3);
                    if (!this.searchResultMessages.isEmpty()) {
                        this.searchResultMessages.size();
                    }
                    if (i9 <= 0 || i9 > this.searchResultMessages.size()) {
                        return null;
                    }
                    return this.searchResultMessages.get(i9 - 1);
                }
                return phoneSearch.get(i6);
            }
            return localServerSearch.get(i5);
        }
        return this.searchResult.get(i);
    }

    public boolean isGlobalSearch(int i) {
        if (this.searchWas && this.searchResultHashtags.isEmpty()) {
            if (isRecentSearchDisplayed()) {
                int i2 = (this.searchWas || MediaDataController.getInstance(this.currentAccount).hints.isEmpty()) ? 0 : 1;
                ArrayList<RecentSearchObject> arrayList = this.searchWas ? this.filtered2RecentSearchObjects : this.filteredRecentSearchObjects;
                if (i > i2 && (i - 1) - i2 < arrayList.size()) {
                    return false;
                }
                i -= getRecentItemsCount();
            }
            ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
            ArrayList<TLObject> localServerSearch = this.searchAdapterHelper.getLocalServerSearch();
            int size = this.searchResult.size();
            int size2 = localServerSearch.size();
            int size3 = this.searchAdapterHelper.getPhoneSearch().size();
            if (size3 > 3 && this.phoneCollapsed) {
                size3 = 3;
            }
            int size4 = globalSearch.isEmpty() ? 0 : globalSearch.size() + 1;
            if (size4 > 4 && this.globalSearchCollapsed) {
                size4 = 4;
            }
            int size5 = this.searchContacts.size();
            if (i < 0 || i >= size5) {
                int i3 = i - (size5 + 1);
                if (i3 < 0 || i3 >= size) {
                    int i4 = i3 - size;
                    if (i4 < 0 || i4 >= size2) {
                        int i5 = i4 - size2;
                        if (i5 <= 0 || i5 >= size3) {
                            int i6 = i5 - size3;
                            if (i6 <= 0 || i6 >= size4) {
                                int i7 = i6 - size4;
                                int size6 = this.searchForumResultMessages.isEmpty() ? 0 : this.searchForumResultMessages.size() + 1;
                                if ((i7 <= 0 || i7 >= size6) && !this.searchResultMessages.isEmpty()) {
                                    this.searchResultMessages.size();
                                }
                                return false;
                            }
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        int itemViewType = viewHolder.getItemViewType();
        return (itemViewType == 1 || itemViewType == 3) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateViewHolder$17(View view, int i) {
        DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
        if (dialogsSearchAdapterDelegate != null) {
            dialogsSearchAdapterDelegate.didPressedOnSubDialog(((Long) view.getTag()).longValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onCreateViewHolder$18(View view, int i) {
        DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
        if (dialogsSearchAdapterDelegate != null) {
            dialogsSearchAdapterDelegate.needRemoveHint(((Long) view.getTag()).longValue());
            return true;
        }
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        FlickerLoadingView flickerLoadingView;
        RecyclerListView recyclerListView;
        switch (i) {
            case 0:
                flickerLoadingView = new ProfileSearchCell(this.mContext);
                break;
            case 1:
                flickerLoadingView = new GraySectionCell(this.mContext);
                break;
            case 2:
                flickerLoadingView = new DialogCell(this, null, this.mContext, false, true) { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter.2
                    @Override // org.telegram.ui.Cells.DialogCell
                    public boolean isForumCell() {
                        return false;
                    }
                };
                break;
            case 3:
                flickerLoadingView = new TopicSearchCell(this.mContext);
                break;
            case 4:
                FlickerLoadingView flickerLoadingView2 = new FlickerLoadingView(this.mContext);
                flickerLoadingView2.setViewType(1);
                flickerLoadingView2.setIsSingleCell(true);
                flickerLoadingView = flickerLoadingView2;
                break;
            case 5:
                flickerLoadingView = new HashtagSearchCell(this.mContext);
                break;
            case 6:
                RecyclerListView recyclerListView2 = new RecyclerListView(this, this.mContext) { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter.3
                    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                        if (getParent() != null && getParent().getParent() != null) {
                            ViewParent parent = getParent().getParent();
                            boolean z = true;
                            if (!canScrollHorizontally(-1) && !canScrollHorizontally(1)) {
                                z = false;
                            }
                            parent.requestDisallowInterceptTouchEvent(z);
                        }
                        return super.onInterceptTouchEvent(motionEvent);
                    }
                };
                recyclerListView2.setSelectorDrawableColor(Theme.getColor("listSelectorSDK21"));
                recyclerListView2.setTag(9);
                recyclerListView2.setItemAnimator(null);
                recyclerListView2.setLayoutAnimation(null);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, this.mContext) { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter.4
                    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                    public boolean supportsPredictiveItemAnimations() {
                        return false;
                    }
                };
                linearLayoutManager.setOrientation(0);
                recyclerListView2.setLayoutManager(linearLayoutManager);
                recyclerListView2.setAdapter(new CategoryAdapterRecycler(this.mContext, this.currentAccount, false));
                recyclerListView2.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda25
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view, int i2) {
                        DialogsSearchAdapter.this.lambda$onCreateViewHolder$17(view, i2);
                    }
                });
                recyclerListView2.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda26
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
                    public final boolean onItemClick(View view, int i2) {
                        boolean lambda$onCreateViewHolder$18;
                        lambda$onCreateViewHolder$18 = DialogsSearchAdapter.this.lambda$onCreateViewHolder$18(view, i2);
                        return lambda$onCreateViewHolder$18;
                    }
                });
                this.innerListView = recyclerListView2;
                recyclerListView = recyclerListView2;
                flickerLoadingView = recyclerListView;
                break;
            case 7:
            default:
                recyclerListView = new TextCell(this.mContext, 16, false);
                flickerLoadingView = recyclerListView;
                break;
            case 8:
                flickerLoadingView = new ProfileSearchCell(this.mContext);
                break;
        }
        if (i == 5) {
            flickerLoadingView.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(86.0f)));
        } else {
            flickerLoadingView.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        }
        return new RecyclerListView.Holder(flickerLoadingView);
    }

    /* JADX WARN: Code restructure failed: missing block: B:200:0x0480, code lost:
        if (r8.startsWith("@" + r7) != false) goto L190;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:206:0x048a  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x0512  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0528  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0530  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0557  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x0562  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0584  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0586  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x05a1  */
    /* JADX WARN: Type inference failed for: r2v12, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r2v15, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v61 */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        TLRPC$User tLRPC$User;
        TLRPC$Chat tLRPC$Chat;
        String str;
        TLRPC$EncryptedChat tLRPC$EncryptedChat;
        boolean z;
        boolean z2;
        SpannableStringBuilder spannableStringBuilder;
        ?? r2;
        String str2;
        String str3;
        String str4;
        boolean z3;
        boolean z4;
        String str5;
        CharSequence formatPluralStringComma;
        String str6;
        int indexOfIgnoreCase;
        int recentItemsCount;
        String str7;
        int i2;
        String str8;
        final int i3 = i;
        int i4 = 4;
        final Runnable runnable = null;
        switch (viewHolder.getItemViewType()) {
            case 0:
                ProfileSearchCell profileSearchCell = (ProfileSearchCell) viewHolder.itemView;
                profileSearchCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                long dialogId = profileSearchCell.getDialogId();
                Object item = getItem(i3);
                if (item instanceof TLRPC$User) {
                    TLRPC$User tLRPC$User2 = (TLRPC$User) item;
                    str = UserObject.getPublicUsername(tLRPC$User2);
                    tLRPC$Chat = null;
                    tLRPC$EncryptedChat = null;
                    tLRPC$User = tLRPC$User2;
                } else if (item instanceof TLRPC$Chat) {
                    TLRPC$Chat tLRPC$Chat2 = (TLRPC$Chat) item;
                    TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$Chat2.id));
                    if (chat != null) {
                        tLRPC$Chat2 = chat;
                    }
                    tLRPC$Chat = tLRPC$Chat2;
                    str = ChatObject.getPublicUsername(tLRPC$Chat2);
                    tLRPC$User = null;
                    tLRPC$EncryptedChat = null;
                } else if (item instanceof TLRPC$EncryptedChat) {
                    TLRPC$EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(((TLRPC$EncryptedChat) item).id));
                    tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(encryptedChat.user_id));
                    tLRPC$EncryptedChat = encryptedChat;
                    tLRPC$Chat = null;
                    str = null;
                } else {
                    tLRPC$User = null;
                    tLRPC$Chat = null;
                    str = null;
                    tLRPC$EncryptedChat = null;
                }
                if (isRecentSearchDisplayed()) {
                    if (i3 < getRecentItemsCount()) {
                        profileSearchCell.useSeparator = i3 != getRecentItemsCount() - 1;
                        z = true;
                    } else {
                        z = false;
                    }
                    i3 -= getRecentItemsCount();
                } else {
                    z = false;
                }
                if (!this.searchTopics.isEmpty()) {
                    i3 -= this.searchTopics.size() + 1;
                }
                ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
                ArrayList<Object> phoneSearch = this.searchAdapterHelper.getPhoneSearch();
                int size = this.searchResult.size();
                int size2 = this.searchAdapterHelper.getLocalServerSearch().size();
                if (size + size2 > 0 && (getRecentItemsCount() > 0 || !this.searchTopics.isEmpty())) {
                    i3--;
                }
                int size3 = phoneSearch.size();
                if (size3 > 3 && this.phoneCollapsed) {
                    size3 = 3;
                }
                int i5 = (size3 <= 0 || !(phoneSearch.get(size3 + (-1)) instanceof String)) ? size3 : size3 - 2;
                int size4 = globalSearch.isEmpty() ? 0 : globalSearch.size() + 1;
                if (size4 <= 4 || !this.globalSearchCollapsed) {
                    i4 = size4;
                }
                if (!z) {
                    profileSearchCell.useSeparator = (i3 == (getItemCount() - getRecentItemsCount()) - 1 || i3 == ((i5 + size) + size2) - 1 || i3 == (((size + i4) + size3) + size2) - 1) ? false : true;
                }
                if (i3 >= 0 && i3 < this.searchResult.size() && tLRPC$User == null) {
                    CharSequence charSequence = this.searchResultNames.get(i3);
                    String publicUsername = UserObject.getPublicUsername(tLRPC$User);
                    if (charSequence != 0 && tLRPC$User != null && publicUsername != null) {
                        String charSequence2 = charSequence.toString();
                        z2 = charSequence;
                        break;
                    }
                    spannableStringBuilder = charSequence;
                    r2 = 0;
                    if (r2 == 0) {
                        String lastFoundUsername = z ? this.filteredRecentQuery : this.searchAdapterHelper.getLastFoundUsername();
                        if (!TextUtils.isEmpty(lastFoundUsername)) {
                            if (tLRPC$User != null) {
                                str6 = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                            } else {
                                str6 = tLRPC$Chat != null ? tLRPC$Chat.title : null;
                            }
                            if (str6 != null && (indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(str6, lastFoundUsername)) != -1) {
                                spannableStringBuilder = new SpannableStringBuilder(str6);
                                spannableStringBuilder.setSpan(new ForegroundColorSpanThemable("windowBackgroundWhiteBlueText4"), indexOfIgnoreCase, indexOfIgnoreCase + lastFoundUsername.length(), 33);
                            }
                            if (str != null && tLRPC$User == null) {
                                if (lastFoundUsername.startsWith("@")) {
                                    lastFoundUsername = lastFoundUsername.substring(1);
                                }
                                try {
                                    r2 = new SpannableStringBuilder();
                                    r2.append("@");
                                    r2.append(str);
                                    int indexOfIgnoreCase2 = AndroidUtilities.indexOfIgnoreCase(str, lastFoundUsername);
                                    if (indexOfIgnoreCase2 != -1) {
                                        int length = lastFoundUsername.length();
                                        if (indexOfIgnoreCase2 == 0) {
                                            length++;
                                        } else {
                                            indexOfIgnoreCase2++;
                                        }
                                        r2.setSpan(new ForegroundColorSpanThemable("windowBackgroundWhiteBlueText4"), indexOfIgnoreCase2, length + indexOfIgnoreCase2, 33);
                                    }
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                            }
                        }
                    }
                    str = r2;
                    profileSearchCell.setChecked(false, false);
                    if (tLRPC$User == null) {
                        str2 = str;
                        if (tLRPC$User.id == this.selfUserId) {
                            str4 = LocaleController.getString("SavedMessages", R.string.SavedMessages);
                            z3 = true;
                            str3 = null;
                            if (tLRPC$Chat != null && tLRPC$Chat.participants_count != 0) {
                                if (!ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                                    formatPluralStringComma = LocaleController.formatPluralStringComma("Subscribers", tLRPC$Chat.participants_count, ' ');
                                } else {
                                    formatPluralStringComma = LocaleController.formatPluralStringComma("Members", tLRPC$Chat.participants_count, ' ');
                                }
                                if (!(str3 instanceof SpannableStringBuilder)) {
                                    ((SpannableStringBuilder) str3).append((CharSequence) ", ").append(formatPluralStringComma);
                                } else {
                                    if (TextUtils.isEmpty(str3)) {
                                        z4 = false;
                                    } else {
                                        z4 = false;
                                        formatPluralStringComma = TextUtils.concat(str3, ", ", formatPluralStringComma);
                                    }
                                    str5 = formatPluralStringComma;
                                    profileSearchCell.setData(tLRPC$User == null ? tLRPC$User : tLRPC$Chat, tLRPC$EncryptedChat, str4, str5, true, z3);
                                    boolean isSelected = this.delegate.isSelected(profileSearchCell.getDialogId());
                                    if (dialogId == profileSearchCell.getDialogId()) {
                                        z4 = true;
                                    }
                                    profileSearchCell.setChecked(isSelected, z4);
                                    return;
                                }
                            }
                            z4 = false;
                            str5 = str3;
                            profileSearchCell.setData(tLRPC$User == null ? tLRPC$User : tLRPC$Chat, tLRPC$EncryptedChat, str4, str5, true, z3);
                            boolean isSelected2 = this.delegate.isSelected(profileSearchCell.getDialogId());
                            if (dialogId == profileSearchCell.getDialogId()) {
                            }
                            profileSearchCell.setChecked(isSelected2, z4);
                            return;
                        }
                    } else {
                        str2 = str;
                    }
                    str3 = str2;
                    str4 = spannableStringBuilder;
                    z3 = false;
                    if (tLRPC$Chat != null) {
                        if (!ChatObject.isChannel(tLRPC$Chat)) {
                        }
                        formatPluralStringComma = LocaleController.formatPluralStringComma("Members", tLRPC$Chat.participants_count, ' ');
                        if (!(str3 instanceof SpannableStringBuilder)) {
                        }
                    }
                    z4 = false;
                    str5 = str3;
                    profileSearchCell.setData(tLRPC$User == null ? tLRPC$User : tLRPC$Chat, tLRPC$EncryptedChat, str4, str5, true, z3);
                    boolean isSelected22 = this.delegate.isSelected(profileSearchCell.getDialogId());
                    if (dialogId == profileSearchCell.getDialogId()) {
                    }
                    profileSearchCell.setChecked(isSelected22, z4);
                    return;
                }
                z2 = false;
                spannableStringBuilder = null;
                r2 = z2;
                if (r2 == 0) {
                }
                str = r2;
                profileSearchCell.setChecked(false, false);
                if (tLRPC$User == null) {
                }
                str3 = str2;
                str4 = spannableStringBuilder;
                z3 = false;
                if (tLRPC$Chat != null) {
                }
                z4 = false;
                str5 = str3;
                profileSearchCell.setData(tLRPC$User == null ? tLRPC$User : tLRPC$Chat, tLRPC$EncryptedChat, str4, str5, true, z3);
                boolean isSelected222 = this.delegate.isSelected(profileSearchCell.getDialogId());
                if (dialogId == profileSearchCell.getDialogId()) {
                }
                profileSearchCell.setChecked(isSelected222, z4);
                return;
            case 1:
                final GraySectionCell graySectionCell = (GraySectionCell) viewHolder.itemView;
                if (!this.searchResultHashtags.isEmpty()) {
                    graySectionCell.setText(LocaleController.getString("Hashtags", R.string.Hashtags), LocaleController.getString("ClearButton", R.string.ClearButton), new View.OnClickListener() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            DialogsSearchAdapter.this.lambda$onBindViewHolder$19(view);
                        }
                    });
                    return;
                }
                if (!isRecentSearchDisplayed() && this.searchTopics.isEmpty() && this.searchContacts.isEmpty()) {
                    recentItemsCount = i3;
                } else {
                    int i6 = (this.searchWas || MediaDataController.getInstance(this.currentAccount).hints.isEmpty()) ? 0 : 1;
                    if (i3 < i6) {
                        graySectionCell.setText(LocaleController.getString("ChatHints", R.string.ChatHints));
                        return;
                    } else if (i3 == i6 && isRecentSearchDisplayed()) {
                        if (!this.searchWas) {
                            graySectionCell.setText(LocaleController.getString("Recent", R.string.Recent), LocaleController.getString("ClearButton", R.string.ClearButton), new View.OnClickListener() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda2
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    DialogsSearchAdapter.this.lambda$onBindViewHolder$20(view);
                                }
                            });
                            return;
                        } else {
                            graySectionCell.setText(LocaleController.getString("Recent", R.string.Recent), LocaleController.getString("Clear", R.string.Clear), new View.OnClickListener() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda3
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    DialogsSearchAdapter.this.lambda$onBindViewHolder$21(view);
                                }
                            });
                            return;
                        }
                    } else {
                        if (i3 == getRecentItemsCount() + (this.searchTopics.isEmpty() ? 0 : this.searchTopics.size() + 1) + (this.searchContacts.isEmpty() ? 0 : this.searchContacts.size() + 1)) {
                            graySectionCell.setText(LocaleController.getString("SearchAllChatsShort", R.string.SearchAllChatsShort));
                            return;
                        }
                        recentItemsCount = i3 - getRecentItemsCount();
                    }
                }
                final ArrayList<TLObject> globalSearch2 = this.searchAdapterHelper.getGlobalSearch();
                int size5 = this.searchResult.size();
                int size6 = this.searchAdapterHelper.getLocalServerSearch().size();
                int size7 = this.searchAdapterHelper.getPhoneSearch().size();
                if (size7 > 3 && this.phoneCollapsed) {
                    size7 = 3;
                }
                int size8 = globalSearch2.isEmpty() ? 0 : globalSearch2.size() + 1;
                if (size8 <= 4 || !this.globalSearchCollapsed) {
                    i4 = size8;
                }
                int size9 = this.searchForumResultMessages.isEmpty() ? 0 : this.searchForumResultMessages.size() + 1;
                if (!this.searchResultMessages.isEmpty()) {
                    this.searchResultMessages.size();
                }
                if (this.searchTopics.isEmpty()) {
                    str7 = null;
                } else {
                    str7 = recentItemsCount == 0 ? LocaleController.getString("Topics", R.string.Topics) : null;
                    recentItemsCount -= this.searchTopics.size() + 1;
                }
                if (!this.searchContacts.isEmpty()) {
                    if (recentItemsCount == 0) {
                        str7 = LocaleController.getString("InviteToTelegramShort", R.string.InviteToTelegramShort);
                    }
                    recentItemsCount -= this.searchContacts.size() + 1;
                }
                if (str7 == null) {
                    int i7 = recentItemsCount - (size5 + size6);
                    if (i7 < 0 || i7 >= size7) {
                        int i8 = i7 - size7;
                        if (i8 >= 0 && i8 < i4) {
                            str7 = LocaleController.getString("GlobalSearch", R.string.GlobalSearch);
                            if (this.searchAdapterHelper.getGlobalSearch().size() > 3) {
                                r10 = this.globalSearchCollapsed;
                                runnable = new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda18
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        DialogsSearchAdapter.this.lambda$onBindViewHolder$25(globalSearch2, i3, graySectionCell);
                                    }
                                };
                            }
                        } else if (this.delegate != null && size9 > 0 && i8 - i4 <= 1) {
                            TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.delegate.getSearchForumDialogId()));
                            int i9 = R.string.SearchMessagesIn;
                            Object[] objArr = new Object[1];
                            objArr[0] = chat2 == null ? "null" : chat2.title;
                            str7 = LocaleController.formatString("SearchMessagesIn", i9, objArr);
                        } else {
                            str7 = LocaleController.getString("SearchMessages", R.string.SearchMessages);
                        }
                    } else {
                        str7 = LocaleController.getString("PhoneNumberSearch", R.string.PhoneNumberSearch);
                        if (this.searchAdapterHelper.getPhoneSearch().size() > 3) {
                            r10 = this.phoneCollapsed;
                            runnable = new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda19
                                @Override // java.lang.Runnable
                                public final void run() {
                                    DialogsSearchAdapter.this.lambda$onBindViewHolder$22(graySectionCell);
                                }
                            };
                        }
                    }
                }
                if (runnable == null) {
                    graySectionCell.setText(str7);
                    return;
                }
                if (r10) {
                    i2 = R.string.ShowMore;
                    str8 = "ShowMore";
                } else {
                    i2 = R.string.ShowLess;
                    str8 = "ShowLess";
                }
                graySectionCell.setText(str7, LocaleController.getString(str8, i2), new View.OnClickListener() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        runnable.run();
                    }
                });
                return;
            case 2:
                DialogCell dialogCell = (DialogCell) viewHolder.itemView;
                dialogCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                dialogCell.useSeparator = i3 != getItemCount() - 1;
                MessageObject messageObject = (MessageObject) getItem(i3);
                dialogCell.useFromUserAsAvatar = this.searchForumResultMessages.contains(messageObject);
                if (messageObject == null) {
                    dialogCell.setDialog(0L, null, 0, false, false);
                    return;
                } else {
                    dialogCell.setDialog(messageObject.getDialogId(), messageObject, messageObject.messageOwner.date, false, false);
                    return;
                }
            case 3:
                ((TopicSearchCell) viewHolder.itemView).setTopic((TLRPC$TL_forumTopic) getItem(i3));
                return;
            case 4:
            default:
                return;
            case 5:
                HashtagSearchCell hashtagSearchCell = (HashtagSearchCell) viewHolder.itemView;
                hashtagSearchCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                hashtagSearchCell.setText(this.searchResultHashtags.get(i3 - 1));
                hashtagSearchCell.setNeedDivider(i3 != this.searchResultHashtags.size());
                return;
            case 6:
                ((CategoryAdapterRecycler) ((RecyclerListView) viewHolder.itemView).getAdapter()).setIndex(i3 / 2);
                return;
            case 7:
                TextCell textCell = (TextCell) viewHolder.itemView;
                textCell.setColors(null, "windowBackgroundWhiteBlueText2");
                textCell.setText(LocaleController.formatString("AddContactByPhone", R.string.AddContactByPhone, PhoneFormat.getInstance().format("+" + ((String) getItem(i3)))), false);
                return;
            case 8:
                ProfileSearchCell profileSearchCell2 = (ProfileSearchCell) viewHolder.itemView;
                ContactsController.Contact contact = (ContactsController.Contact) getItem(i3);
                profileSearchCell2.setData(contact, null, ContactsController.formatName(contact.first_name, contact.last_name), PhoneFormat.getInstance().format("+" + contact.shortPhones.get(0)), false, false);
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$19(View view) {
        DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
        if (dialogsSearchAdapterDelegate != null) {
            dialogsSearchAdapterDelegate.needClearList();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$20(View view) {
        DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
        if (dialogsSearchAdapterDelegate != null) {
            dialogsSearchAdapterDelegate.needClearList();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$21(View view) {
        DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
        if (dialogsSearchAdapterDelegate != null) {
            dialogsSearchAdapterDelegate.needClearList();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$22(GraySectionCell graySectionCell) {
        int i;
        String str;
        boolean z = !this.phoneCollapsed;
        this.phoneCollapsed = z;
        if (z) {
            i = R.string.ShowMore;
            str = "ShowMore";
        } else {
            i = R.string.ShowLess;
            str = "ShowLess";
        }
        graySectionCell.setRightText(LocaleController.getString(str, i));
        notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$25(ArrayList arrayList, final int i, GraySectionCell graySectionCell) {
        int i2;
        String str;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (elapsedRealtime - this.lastShowMoreUpdate < 300) {
            return;
        }
        this.lastShowMoreUpdate = elapsedRealtime;
        int size = arrayList.isEmpty() ? 0 : arrayList.size();
        boolean z = getItemCount() > (Math.min(size, this.globalSearchCollapsed ? 4 : ConnectionsManager.DEFAULT_DATACENTER_ID) + i) + 1;
        DefaultItemAnimator defaultItemAnimator = this.itemAnimator;
        if (defaultItemAnimator != null) {
            defaultItemAnimator.setAddDuration(z ? 45L : 200L);
            this.itemAnimator.setRemoveDuration(z ? 80L : 200L);
            this.itemAnimator.setRemoveDelay(z ? 270L : 0L);
        }
        boolean z2 = !this.globalSearchCollapsed;
        this.globalSearchCollapsed = z2;
        if (z2) {
            i2 = R.string.ShowMore;
            str = "ShowMore";
        } else {
            i2 = R.string.ShowLess;
            str = "ShowLess";
        }
        graySectionCell.setRightText(LocaleController.getString(str, i2), this.globalSearchCollapsed);
        this.showMoreHeader = null;
        final View view = (View) graySectionCell.getParent();
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            int i3 = !this.globalSearchCollapsed ? i + 4 : i + size + 1;
            int i4 = 0;
            while (true) {
                if (i4 >= recyclerView.getChildCount()) {
                    break;
                }
                View childAt = recyclerView.getChildAt(i4);
                if (recyclerView.getChildAdapterPosition(childAt) == i3) {
                    this.showMoreHeader = childAt;
                    break;
                }
                i4++;
            }
        }
        if (!this.globalSearchCollapsed) {
            notifyItemChanged(i + 3);
            notifyItemRangeInserted(i + 4, size - 3);
        } else {
            notifyItemRangeRemoved(i + 4, size - 3);
            if (z) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsSearchAdapter.this.lambda$onBindViewHolder$23(i);
                    }
                }, 350L);
            } else {
                notifyItemChanged(i + 3);
            }
        }
        Runnable runnable = this.cancelShowMoreAnimation;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        if (z) {
            this.showMoreAnimation = true;
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsSearchAdapter.this.lambda$onBindViewHolder$24(view);
                }
            };
            this.cancelShowMoreAnimation = runnable2;
            AndroidUtilities.runOnUIThread(runnable2, 400L);
            return;
        }
        this.showMoreAnimation = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$23(int i) {
        notifyItemChanged(i + 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$24(View view) {
        this.showMoreAnimation = false;
        this.showMoreHeader = null;
        if (view != null) {
            view.invalidate();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:111:0x0139, code lost:
        if (r10 != 0) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x013b, code lost:
        return 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x013c, code lost:
        if (r10 != r8) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x013e, code lost:
        return 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x013f, code lost:
        return 2;
     */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getItemViewType(int i) {
        if (!this.searchResultHashtags.isEmpty()) {
            return i == 0 ? 1 : 5;
        }
        if (isRecentSearchDisplayed()) {
            int i2 = (this.searchWas || MediaDataController.getInstance(this.currentAccount).hints.isEmpty()) ? 0 : 1;
            if (i < i2) {
                return 6;
            }
            if (i == i2) {
                return 1;
            }
            if (i < getRecentItemsCount()) {
                return 0;
            }
            i -= getRecentItemsCount();
        }
        int i3 = 3;
        if (!this.searchTopics.isEmpty()) {
            if (i == 0) {
                return 1;
            }
            if (i <= this.searchTopics.size()) {
                return 3;
            }
            i -= this.searchTopics.size() + 1;
        }
        if (!this.searchContacts.isEmpty()) {
            if (i == 0) {
                return 1;
            }
            if (i <= this.searchContacts.size()) {
                return 8;
            }
            i -= this.searchContacts.size() + 1;
        }
        ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
        int size = this.searchResult.size();
        int size2 = this.searchAdapterHelper.getLocalServerSearch().size();
        if (size + size2 > 0 && (getRecentItemsCount() > 0 || !this.searchTopics.isEmpty())) {
            if (i == 0) {
                return 1;
            }
            i--;
        }
        int size3 = this.searchAdapterHelper.getPhoneSearch().size();
        if (size3 <= 3 || !this.phoneCollapsed) {
            i3 = size3;
        }
        int size4 = globalSearch.isEmpty() ? 0 : globalSearch.size() + 1;
        if (size4 > 4 && this.globalSearchCollapsed) {
            size4 = 4;
        }
        int size5 = this.searchResultMessages.isEmpty() ? 0 : this.searchResultMessages.size() + 1;
        if (!this.searchForumResultMessages.isEmpty() && !this.localMessagesSearchEndReached) {
            size5 = 0;
        }
        int size6 = this.searchForumResultMessages.isEmpty() ? 0 : this.searchForumResultMessages.size() + 1;
        if (i < 0 || i >= size) {
            int i4 = i - size;
            if (i4 < 0 || i4 >= size2) {
                int i5 = i4 - size2;
                if (i5 >= 0 && i5 < i3) {
                    Object item = getItem(i5);
                    if (item instanceof String) {
                        return "section".equals((String) item) ? 1 : 7;
                    }
                    return 0;
                }
                int i6 = i5 - i3;
                if (i6 >= 0 && i6 < size4) {
                    return i6 == 0 ? 1 : 0;
                }
                int i7 = i6 - size4;
                if (size6 > 0) {
                    if (i7 >= 0) {
                        if (this.localMessagesSearchEndReached) {
                        }
                    }
                    i7 -= size6 + (!this.localMessagesSearchEndReached ? 1 : 0);
                }
                if (i7 < 0 || i7 >= size5) {
                    return 4;
                }
                return i7 == 0 ? 1 : 2;
            }
            return 0;
        }
        return 0;
    }

    public void setFiltersDelegate(FilteredSearchView.Delegate delegate, boolean z) {
        this.filtersDelegate = delegate;
        if (delegate == null || !z) {
            return;
        }
        delegate.updateFiltersView(false, null, this.localTipDates, this.localTipArchive);
    }

    public int getCurrentItemCount() {
        return this.currentItemCount;
    }

    public void filterRecent(String str) {
        DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate;
        String str2;
        this.filteredRecentQuery = str;
        this.filtered2RecentSearchObjects.clear();
        int i = 0;
        if (TextUtils.isEmpty(str)) {
            this.filteredRecentSearchObjects.clear();
            int size = this.recentSearchObjects.size();
            while (i < size) {
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate2 = this.delegate;
                if (dialogsSearchAdapterDelegate2 == null || dialogsSearchAdapterDelegate2.getSearchForumDialogId() != this.recentSearchObjects.get(i).did) {
                    this.filteredRecentSearchObjects.add(this.recentSearchObjects.get(i));
                }
                i++;
            }
            return;
        }
        String lowerCase = str.toLowerCase();
        int size2 = this.recentSearchObjects.size();
        while (i < size2) {
            RecentSearchObject recentSearchObject = this.recentSearchObjects.get(i);
            if (recentSearchObject != null && recentSearchObject.object != null && ((dialogsSearchAdapterDelegate = this.delegate) == null || dialogsSearchAdapterDelegate.getSearchForumDialogId() != recentSearchObject.did)) {
                TLObject tLObject = recentSearchObject.object;
                String str3 = null;
                if (tLObject instanceof TLRPC$Chat) {
                    str3 = ((TLRPC$Chat) tLObject).title;
                    str2 = ((TLRPC$Chat) tLObject).username;
                } else if (tLObject instanceof TLRPC$User) {
                    str3 = UserObject.getUserName((TLRPC$User) tLObject);
                    str2 = ((TLRPC$User) recentSearchObject.object).username;
                } else if (tLObject instanceof TLRPC$ChatInvite) {
                    str3 = ((TLRPC$ChatInvite) tLObject).title;
                    str2 = null;
                } else {
                    str2 = null;
                }
                if ((str3 != null && wordStartsWith(str3.toLowerCase(), lowerCase)) || (str2 != null && wordStartsWith(str2.toLowerCase(), lowerCase))) {
                    this.filtered2RecentSearchObjects.add(recentSearchObject);
                }
                if (this.filtered2RecentSearchObjects.size() >= 5) {
                    return;
                }
            }
            i++;
        }
    }

    private boolean wordStartsWith(String str, String str2) {
        if (str2 == null || str == null) {
            return false;
        }
        String[] split = str.toLowerCase().split(" ");
        for (int i = 0; i < split.length; i++) {
            if (split[i] != null && (split[i].startsWith(str2) || str2.startsWith(split[i]))) {
                return true;
            }
        }
        return false;
    }
}
