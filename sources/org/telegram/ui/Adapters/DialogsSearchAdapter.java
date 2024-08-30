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
import java.util.Comparator;
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
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$InputPeer;
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
import org.telegram.tgnet.TLRPC$TL_username;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$messages_Messages;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
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
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.FilteredSearchView;
/* loaded from: classes4.dex */
public abstract class DialogsSearchAdapter extends RecyclerListView.SelectionAdapter {
    private Runnable cancelShowMoreAnimation;
    private int currentItemCount;
    private String currentMessagesQuery;
    public DialogsSearchAdapterDelegate delegate;
    private final DialogsActivity dialogsActivity;
    private int dialogsType;
    private ArrayList filterDialogIds;
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
    private final Theme.ResourcesProvider resourcesProvider;
    private SearchAdapterHelper searchAdapterHelper;
    private Runnable searchRunnable;
    private Runnable searchRunnable2;
    private boolean searchWas;
    private long selfUserId;
    public View showMoreHeader;
    int waitingResponseCount;
    public final int VIEW_TYPE_DIALOG_CELL = 2;
    public final int VIEW_TYPE_TOPIC_CELL = 3;
    public final int VIEW_TYPE_LOADING = 4;
    public final int VIEW_TYPE_HASHTAG_CELL = 5;
    public final int VIEW_TYPE_CATEGORY_LIST = 6;
    public final int VIEW_TYPE_ADD_BY_PHONE = 7;
    public final int VIEW_TYPE_INVITE_CONTACT_CELL = 8;
    private ArrayList searchResult = new ArrayList();
    private final ArrayList searchContacts = new ArrayList();
    private final ArrayList searchTopics = new ArrayList();
    private ArrayList searchResultNames = new ArrayList();
    private final ArrayList searchForumResultMessages = new ArrayList();
    private final ArrayList searchResultMessages = new ArrayList();
    private final ArrayList searchResultHashtags = new ArrayList();
    private int reqId = 0;
    private int reqForumId = 0;
    public int localMessagesLoadingRow = -1;
    public boolean showMoreAnimation = false;
    private int currentAccount = UserConfig.selectedAccount;
    private ArrayList recentSearchObjects = new ArrayList();
    private final ArrayList filteredRecentSearchObjects = new ArrayList();
    private final ArrayList filtered2RecentSearchObjects = new ArrayList();
    private String filteredRecentQuery = null;
    private LongSparseArray recentSearchObjectsById = new LongSparseArray();
    private ArrayList localTipDates = new ArrayList();
    boolean globalSearchCollapsed = true;
    boolean phoneCollapsed = true;

    /* loaded from: classes4.dex */
    public static class CategoryAdapterRecycler extends RecyclerListView.SelectionAdapter {
        private final int currentAccount;
        private boolean drawChecked;
        private final Context mContext;
        private Theme.ResourcesProvider resourcesProvider;
        private boolean showPremiumBlock;

        public CategoryAdapterRecycler(Context context, int i, boolean z, boolean z2, Theme.ResourcesProvider resourcesProvider) {
            this.drawChecked = z;
            this.mContext = context;
            this.currentAccount = i;
            this.showPremiumBlock = z2;
            this.resourcesProvider = resourcesProvider;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return MediaDataController.getInstance(this.currentAccount).hints.size();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC$Chat tLRPC$Chat;
            MessagesController messagesController;
            long j;
            HintDialogCell hintDialogCell = (HintDialogCell) viewHolder.itemView;
            TLRPC$TL_topPeer tLRPC$TL_topPeer = MediaDataController.getInstance(this.currentAccount).hints.get(i);
            new TLRPC$TL_dialog();
            TLRPC$Peer tLRPC$Peer = tLRPC$TL_topPeer.peer;
            long j2 = tLRPC$Peer.user_id;
            TLRPC$User tLRPC$User = null;
            if (j2 != 0) {
                tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$TL_topPeer.peer.user_id));
                tLRPC$Chat = null;
            } else {
                long j3 = tLRPC$Peer.channel_id;
                if (j3 != 0) {
                    j2 = -j3;
                    messagesController = MessagesController.getInstance(this.currentAccount);
                    j = tLRPC$TL_topPeer.peer.channel_id;
                } else {
                    long j4 = tLRPC$Peer.chat_id;
                    if (j4 != 0) {
                        j2 = -j4;
                        messagesController = MessagesController.getInstance(this.currentAccount);
                        j = tLRPC$TL_topPeer.peer.chat_id;
                    } else {
                        tLRPC$Chat = null;
                        j2 = 0;
                    }
                }
                tLRPC$Chat = messagesController.getChat(Long.valueOf(j));
            }
            hintDialogCell.setTag(Long.valueOf(j2));
            hintDialogCell.setDialog(j2, true, tLRPC$User != null ? UserObject.getFirstName(tLRPC$User) : tLRPC$Chat != null ? tLRPC$Chat.title : "");
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            HintDialogCell hintDialogCell = new HintDialogCell(this.mContext, this.drawChecked, this.resourcesProvider);
            if (this.showPremiumBlock) {
                hintDialogCell.showPremiumBlocked();
            }
            hintDialogCell.setLayoutParams(new RecyclerView.LayoutParams(AndroidUtilities.dp(80.0f), AndroidUtilities.dp(86.0f)));
            return new RecyclerListView.Holder(hintDialogCell);
        }

        public void setIndex(int i) {
            notifyDataSetChanged();
        }
    }

    /* loaded from: classes4.dex */
    public static class DialogSearchResult {
        public int date;
        public CharSequence name;
        public TLObject object;
    }

    /* loaded from: classes4.dex */
    public interface DialogsSearchAdapterDelegate {
        void didPressedBlockedDialog(View view, long j);

        void didPressedOnSubDialog(long j);

        long getSearchForumDialogId();

        boolean isSelected(long j);

        void needClearList();

        void needRemoveHint(long j);

        void runResultsEnterAnimation();

        void searchStateChanged(boolean z, boolean z2);
    }

    /* loaded from: classes4.dex */
    public interface OnRecentSearchLoaded {
        void setRecentSearch(ArrayList arrayList, LongSparseArray longSparseArray);
    }

    /* loaded from: classes4.dex */
    public static class RecentSearchObject {
        public int date;
        public long did;
        public TLObject object;
    }

    public DialogsSearchAdapter(Context context, DialogsActivity dialogsActivity, int i, int i2, DefaultItemAnimator defaultItemAnimator, boolean z, Theme.ResourcesProvider resourcesProvider) {
        this.itemAnimator = defaultItemAnimator;
        this.dialogsActivity = dialogsActivity;
        this.resourcesProvider = resourcesProvider;
        SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(false) { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter.1
            @Override // org.telegram.ui.Adapters.SearchAdapterHelper
            protected boolean filter(TLObject tLObject) {
                return DialogsSearchAdapter.this.filter(tLObject);
            }
        };
        this.searchAdapterHelper = searchAdapterHelper;
        searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter.2
            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public boolean canApplySearchResults(int i3) {
                return i3 == DialogsSearchAdapter.this.lastSearchId;
            }

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
            public void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    DialogsSearchAdapter.this.searchResultHashtags.add(((SearchAdapterHelper.HashtagObject) arrayList.get(i3)).hashtag);
                }
                DialogsSearchAdapter dialogsSearchAdapter = DialogsSearchAdapter.this;
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = dialogsSearchAdapter.delegate;
                if (dialogsSearchAdapterDelegate != null) {
                    dialogsSearchAdapterDelegate.searchStateChanged(dialogsSearchAdapter.waitingResponseCount > 0, false);
                }
                DialogsSearchAdapter.this.notifyDataSetChanged();
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

    /* JADX INFO: Access modifiers changed from: private */
    public boolean filter(Object obj) {
        if (this.dialogsType != 14) {
            return true;
        }
        if (obj instanceof TLRPC$User) {
            return ((TLRPC$User) obj).bot ? this.dialogsActivity.allowBots : this.dialogsActivity.allowUsers;
        } else if (obj instanceof TLRPC$Chat) {
            TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) obj;
            if (ChatObject.isChannel(tLRPC$Chat)) {
                return this.dialogsActivity.allowChannels;
            }
            if (ChatObject.isMegagroup(tLRPC$Chat)) {
                DialogsActivity dialogsActivity = this.dialogsActivity;
                return dialogsActivity.allowGroups || dialogsActivity.allowMegagroups;
            }
            DialogsActivity dialogsActivity2 = this.dialogsActivity;
            return dialogsActivity2.allowGroups || dialogsActivity2.allowLegacyGroups;
        } else {
            return false;
        }
    }

    private boolean hasHints() {
        return (this.searchWas || MediaDataController.getInstance(this.currentAccount).hints.isEmpty() || (this.dialogsType == 14 && !this.dialogsActivity.allowUsers)) ? false : true;
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$loadRecentSearch$5(RecentSearchObject recentSearchObject, RecentSearchObject recentSearchObject2) {
        int i = recentSearchObject.date;
        int i2 = recentSearchObject2.date;
        if (i < i2) {
            return 1;
        }
        return i > i2 ? -1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadRecentSearch$7(int i, int i2, final OnRecentSearchLoaded onRecentSearchLoaded) {
        Long valueOf;
        ArrayList<Long> arrayList;
        try {
            SQLiteCursor queryFinalized = MessagesStorage.getInstance(i).getDatabase().queryFinalized("SELECT did, date FROM search_recent WHERE 1", new Object[0]);
            ArrayList<Long> arrayList2 = new ArrayList<>();
            ArrayList<Long> arrayList3 = new ArrayList<>();
            ArrayList arrayList4 = new ArrayList();
            new ArrayList();
            final ArrayList arrayList5 = new ArrayList();
            final LongSparseArray longSparseArray = new LongSparseArray();
            while (queryFinalized.next()) {
                long longValue = queryFinalized.longValue(0);
                if (!DialogObject.isEncryptedDialog(longValue)) {
                    if (!DialogObject.isUserDialog(longValue)) {
                        long j = -longValue;
                        if (!arrayList3.contains(Long.valueOf(j))) {
                            valueOf = Long.valueOf(j);
                            arrayList = arrayList3;
                            arrayList.add(valueOf);
                        }
                    } else if (i2 != 2 && !arrayList2.contains(Long.valueOf(longValue))) {
                        valueOf = Long.valueOf(longValue);
                        arrayList = arrayList2;
                        arrayList.add(valueOf);
                    }
                    RecentSearchObject recentSearchObject = new RecentSearchObject();
                    recentSearchObject.did = longValue;
                    recentSearchObject.date = queryFinalized.intValue(1);
                    arrayList5.add(recentSearchObject);
                    longSparseArray.put(recentSearchObject.did, recentSearchObject);
                } else if (i2 == 0 || i2 == 3) {
                    int encryptedChatId = DialogObject.getEncryptedChatId(longValue);
                    if (!arrayList4.contains(Integer.valueOf(encryptedChatId))) {
                        arrayList4.add(Integer.valueOf(encryptedChatId));
                        RecentSearchObject recentSearchObject2 = new RecentSearchObject();
                        recentSearchObject2.did = longValue;
                        recentSearchObject2.date = queryFinalized.intValue(1);
                        arrayList5.add(recentSearchObject2);
                        longSparseArray.put(recentSearchObject2.did, recentSearchObject2);
                    }
                }
            }
            queryFinalized.dispose();
            ArrayList<TLRPC$User> arrayList6 = new ArrayList<>();
            if (!arrayList4.isEmpty()) {
                ArrayList<TLRPC$EncryptedChat> arrayList7 = new ArrayList<>();
                MessagesStorage.getInstance(i).getEncryptedChatsInternal(TextUtils.join(",", arrayList4), arrayList7, arrayList2);
                for (int i3 = 0; i3 < arrayList7.size(); i3++) {
                    RecentSearchObject recentSearchObject3 = (RecentSearchObject) longSparseArray.get(DialogObject.makeEncryptedDialogId(arrayList7.get(i3).id));
                    if (recentSearchObject3 != null) {
                        recentSearchObject3.object = arrayList7.get(i3);
                    }
                }
            }
            if (!arrayList3.isEmpty()) {
                ArrayList<TLRPC$Chat> arrayList8 = new ArrayList<>();
                MessagesStorage.getInstance(i).getChatsInternal(TextUtils.join(",", arrayList3), arrayList8);
                for (int i4 = 0; i4 < arrayList8.size(); i4++) {
                    TLRPC$Chat tLRPC$Chat = arrayList8.get(i4);
                    long j2 = -tLRPC$Chat.id;
                    if (tLRPC$Chat.migrated_to != null) {
                        RecentSearchObject recentSearchObject4 = (RecentSearchObject) longSparseArray.get(j2);
                        longSparseArray.remove(j2);
                        if (recentSearchObject4 != null) {
                            arrayList5.remove(recentSearchObject4);
                        }
                    } else {
                        RecentSearchObject recentSearchObject5 = (RecentSearchObject) longSparseArray.get(j2);
                        if (recentSearchObject5 != null) {
                            recentSearchObject5.object = tLRPC$Chat;
                        }
                    }
                }
            }
            if (!arrayList2.isEmpty()) {
                MessagesStorage.getInstance(i).getUsersInternal(arrayList2, arrayList6);
                for (int i5 = 0; i5 < arrayList6.size(); i5++) {
                    TLRPC$User tLRPC$User = arrayList6.get(i5);
                    RecentSearchObject recentSearchObject6 = (RecentSearchObject) longSparseArray.get(tLRPC$User.id);
                    if (recentSearchObject6 != null) {
                        recentSearchObject6.object = tLRPC$User;
                    }
                }
            }
            Collections.sort(arrayList5, new Comparator() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda1
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$loadRecentSearch$5;
                    lambda$loadRecentSearch$5 = DialogsSearchAdapter.lambda$loadRecentSearch$5((DialogsSearchAdapter.RecentSearchObject) obj, (DialogsSearchAdapter.RecentSearchObject) obj2);
                    return lambda$loadRecentSearch$5;
                }
            });
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsSearchAdapter.OnRecentSearchLoaded.this.setRecentSearch(arrayList5, longSparseArray);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
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
        boolean z = !this.phoneCollapsed;
        this.phoneCollapsed = z;
        graySectionCell.setRightText(LocaleController.getString(z ? R.string.ShowMore : R.string.ShowLess));
        notifyDataSetChanged();
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$25(ArrayList arrayList, final int i, GraySectionCell graySectionCell) {
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
        graySectionCell.setRightText(LocaleController.getString(z2 ? R.string.ShowMore : R.string.ShowLess), this.globalSearchCollapsed);
        this.showMoreHeader = null;
        final View view = (View) graySectionCell.getParent();
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            int i2 = !this.globalSearchCollapsed ? i + 4 : i + size + 1;
            int i3 = 0;
            while (true) {
                if (i3 >= recyclerView.getChildCount()) {
                    break;
                }
                View childAt = recyclerView.getChildAt(i3);
                if (recyclerView.getChildAdapterPosition(childAt) == i2) {
                    this.showMoreHeader = childAt;
                    break;
                }
                i3++;
            }
        }
        if (this.globalSearchCollapsed) {
            notifyItemRangeRemoved(i + 4, size - 3);
            if (z) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda18
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsSearchAdapter.this.lambda$onBindViewHolder$23(i);
                    }
                }, 350L);
            } else {
                notifyItemChanged(i + 3);
            }
        } else {
            notifyItemChanged(i + 3);
            notifyItemRangeInserted(i + 4, size - 3);
        }
        Runnable runnable = this.cancelShowMoreAnimation;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        if (!z) {
            this.showMoreAnimation = false;
            return;
        }
        this.showMoreAnimation = true;
        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$onBindViewHolder$24(view);
            }
        };
        this.cancelShowMoreAnimation = runnable2;
        AndroidUtilities.runOnUIThread(runnable2, 400L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateViewHolder$17(View view, int i) {
        if (view instanceof HintDialogCell) {
            HintDialogCell hintDialogCell = (HintDialogCell) view;
            if (hintDialogCell.isBlocked()) {
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
                if (dialogsSearchAdapterDelegate != null) {
                    dialogsSearchAdapterDelegate.didPressedBlockedDialog(view, hintDialogCell.getDialogId());
                    return;
                }
                return;
            }
        }
        DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate2 = this.delegate;
        if (dialogsSearchAdapterDelegate2 != null) {
            dialogsSearchAdapterDelegate2.didPressedOnSubDialog(((Long) view.getTag()).longValue());
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeRecentSearch$10(long j) {
        try {
            SQLiteDatabase database = MessagesStorage.getInstance(this.currentAccount).getDatabase();
            database.executeFast("DELETE FROM search_recent WHERE did = " + j).stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchDialogs$15(int i, String str, String str2) {
        String str3;
        int i2;
        this.searchRunnable2 = null;
        if (i != this.lastSearchId) {
            return;
        }
        if (this.needMessagesSearch == 2 || (i2 = this.dialogsType) == 6 || i2 == 5 || this.delegate.getSearchForumDialogId() != 0) {
            str3 = str2;
            this.waitingResponseCount -= 2;
        } else {
            SearchAdapterHelper searchAdapterHelper = this.searchAdapterHelper;
            int i3 = this.dialogsType;
            boolean z = i3 != 4;
            boolean z2 = (i3 == 4 || i3 == 11) ? false : true;
            boolean z3 = i3 == 2 || i3 == 1;
            boolean z4 = i3 == 0;
            DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
            str3 = str2;
            searchAdapterHelper.queryServerSearch(str, true, z, true, z2, z3, 0L, z4, 0, i, dialogsSearchAdapterDelegate != null ? dialogsSearchAdapterDelegate.getSearchForumDialogId() : 0L);
        }
        if (this.needMessagesSearch == 0 || this.dialogsType == 15) {
            this.waitingResponseCount--;
            return;
        }
        searchTopics(str3);
        String str4 = str3;
        searchMessagesInternal(str4, i);
        searchForumMessagesInternal(str4, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchDialogs$16(final String str, final int i, final String str2) {
        this.searchRunnable = null;
        searchDialogsInternal(str, i);
        if (this.dialogsType == 15) {
            this.waitingResponseCount -= 2;
            return;
        }
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$searchDialogs$15(i, str, str2);
            }
        };
        this.searchRunnable2 = runnable;
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchDialogsInternal$11() {
        FilteredSearchView.Delegate delegate = this.filtersDelegate;
        if (delegate != null) {
            delegate.updateFiltersView(false, null, this.localTipDates, this.localTipArchive);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchDialogsInternal$12(String str, int i, String str2) {
        ArrayList<Object> arrayList = new ArrayList<>();
        ArrayList<CharSequence> arrayList2 = new ArrayList<>();
        ArrayList<TLRPC$User> arrayList3 = new ArrayList<>();
        ArrayList arrayList4 = new ArrayList();
        MessagesStorage.getInstance(this.currentAccount).localSearch(this.dialogsType, str, arrayList, arrayList2, arrayList3, this.filterDialogIds, -1);
        updateSearchResults(arrayList, arrayList2, arrayList3, arrayList4, i);
        FiltersView.fillTipDates(str, this.localTipDates);
        this.localTipArchive = false;
        if (str.length() >= 3 && (LocaleController.getString(R.string.ArchiveSearchFilter).toLowerCase().startsWith(str) || "archive".startsWith(str2))) {
            this.localTipArchive = true;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$searchDialogsInternal$11();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchForumMessagesInternal$0(int i, int i2, TLRPC$TL_error tLRPC$TL_error, String str, TLObject tLObject, TLRPC$TL_messages_search tLRPC$TL_messages_search, ArrayList arrayList) {
        if (i == this.lastForumReqId && (i2 <= 0 || i2 == this.lastSearchId)) {
            this.waitingResponseCount--;
            if (tLRPC$TL_error == null) {
                this.currentMessagesQuery = str;
                TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
                MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
                MessagesController.getInstance(this.currentAccount).putUsers(tLRPC$messages_Messages.users, false);
                MessagesController.getInstance(this.currentAccount).putChats(tLRPC$messages_Messages.chats, false);
                if (tLRPC$TL_messages_search.add_offset == 0) {
                    this.searchForumResultMessages.clear();
                }
                this.nextSearchRate = tLRPC$messages_Messages.next_rate;
                for (int i3 = 0; i3 < tLRPC$messages_Messages.messages.size(); i3++) {
                    TLRPC$Message tLRPC$Message = (TLRPC$Message) tLRPC$messages_Messages.messages.get(i3);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchForumMessagesInternal$1(final String str, final int i, final int i2, final TLRPC$TL_messages_search tLRPC$TL_messages_search, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        final ArrayList arrayList = new ArrayList();
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            LongSparseArray longSparseArray = new LongSparseArray();
            LongSparseArray longSparseArray2 = new LongSparseArray();
            for (int i3 = 0; i3 < tLRPC$messages_Messages.chats.size(); i3++) {
                TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) tLRPC$messages_Messages.chats.get(i3);
                longSparseArray.put(tLRPC$Chat.id, tLRPC$Chat);
            }
            for (int i4 = 0; i4 < tLRPC$messages_Messages.users.size(); i4++) {
                TLRPC$User tLRPC$User = (TLRPC$User) tLRPC$messages_Messages.users.get(i4);
                longSparseArray2.put(tLRPC$User.id, tLRPC$User);
            }
            for (int i5 = 0; i5 < tLRPC$messages_Messages.messages.size(); i5++) {
                MessageObject messageObject = new MessageObject(this.currentAccount, (TLRPC$Message) tLRPC$messages_Messages.messages.get(i5), longSparseArray2, longSparseArray, false, true);
                arrayList.add(messageObject);
                messageObject.setQuery(str);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$searchForumMessagesInternal$0(i, i2, tLRPC$TL_error, str, tLObject, tLRPC$TL_messages_search, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchMessagesInternal$2(int i, int i2, TLRPC$TL_error tLRPC$TL_error, String str, TLObject tLObject, TLRPC$TL_messages_searchGlobal tLRPC$TL_messages_searchGlobal, ArrayList arrayList) {
        int i3;
        if (i == this.lastReqId && (i2 <= 0 || i2 == this.lastSearchId)) {
            this.waitingResponseCount--;
            if (tLRPC$TL_error == null) {
                this.currentMessagesQuery = str;
                TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
                MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
                MessagesController.getInstance(this.currentAccount).putUsers(tLRPC$messages_Messages.users, false);
                MessagesController.getInstance(this.currentAccount).putChats(tLRPC$messages_Messages.chats, false);
                if (tLRPC$TL_messages_searchGlobal.offset_id == 0) {
                    this.searchResultMessages.clear();
                }
                this.nextSearchRate = tLRPC$messages_Messages.next_rate;
                for (int i4 = 0; i4 < tLRPC$messages_Messages.messages.size(); i4++) {
                    TLRPC$Message tLRPC$Message = (TLRPC$Message) tLRPC$messages_Messages.messages.get(i4);
                    int i5 = MessagesController.getInstance(this.currentAccount).deletedHistory.get(MessageObject.getDialogId(tLRPC$Message));
                    if (i5 == 0 || tLRPC$Message.id > i5) {
                        MessageObject messageObject = (MessageObject) arrayList.get(i4);
                        if (!this.searchForumResultMessages.isEmpty()) {
                            while (i3 < this.searchForumResultMessages.size()) {
                                MessageObject messageObject2 = (MessageObject) this.searchForumResultMessages.get(i3);
                                i3 = (messageObject2 == null || messageObject == null || messageObject.getId() != messageObject2.getId() || messageObject.getDialogId() != messageObject2.getDialogId()) ? i3 + 1 : 0;
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchMessagesInternal$3(final String str, final int i, final int i2, final TLRPC$TL_messages_searchGlobal tLRPC$TL_messages_searchGlobal, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        final ArrayList arrayList = new ArrayList();
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            LongSparseArray longSparseArray = new LongSparseArray();
            LongSparseArray longSparseArray2 = new LongSparseArray();
            for (int i3 = 0; i3 < tLRPC$messages_Messages.chats.size(); i3++) {
                TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) tLRPC$messages_Messages.chats.get(i3);
                longSparseArray.put(tLRPC$Chat.id, tLRPC$Chat);
            }
            for (int i4 = 0; i4 < tLRPC$messages_Messages.users.size(); i4++) {
                TLRPC$User tLRPC$User = (TLRPC$User) tLRPC$messages_Messages.users.get(i4);
                longSparseArray2.put(tLRPC$User.id, tLRPC$User);
            }
            for (int i5 = 0; i5 < tLRPC$messages_Messages.messages.size(); i5++) {
                MessageObject messageObject = new MessageObject(this.currentAccount, (TLRPC$Message) tLRPC$messages_Messages.messages.get(i5), longSparseArray2, longSparseArray, false, true);
                arrayList.add(messageObject);
                messageObject.setQuery(str);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$searchMessagesInternal$2(i, i2, tLRPC$TL_error, str, tLObject, tLRPC$TL_messages_searchGlobal, arrayList);
            }
        });
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
        int i2 = 0;
        while (i2 < arrayList.size()) {
            if (!filter(arrayList.get(i2))) {
                arrayList.remove(i2);
                i2--;
            }
            i2++;
        }
        int size = this.filtered2RecentSearchObjects.size();
        int i3 = 0;
        while (i3 < arrayList.size()) {
            final Object obj = arrayList.get(i3);
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
            if (j != 0 && ((TLRPC$Dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(j)) == null) {
                MessagesStorage.getInstance(this.currentAccount).getDialogFolderId(j, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda26
                    @Override // org.telegram.messenger.MessagesStorage.IntCallback
                    public final void run(int i4) {
                        DialogsSearchAdapter.this.lambda$updateSearchResults$13(j, obj, i4);
                    }
                });
            }
            if (resentSearchAvailable() && !(obj instanceof TLRPC$EncryptedChat)) {
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
                boolean z = dialogsSearchAdapterDelegate != null && dialogsSearchAdapterDelegate.getSearchForumDialogId() == j;
                for (int i4 = 0; !z && i4 < size; i4++) {
                    RecentSearchObject recentSearchObject = (RecentSearchObject) this.filtered2RecentSearchObjects.get(i4);
                    if (recentSearchObject != null && recentSearchObject.did == j) {
                        z = true;
                    }
                }
                if (z) {
                    arrayList.remove(i3);
                    arrayList2.remove(i3);
                    i3--;
                }
            }
            i3++;
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

    public static void loadRecentSearch(final int i, final int i2, final OnRecentSearchLoaded onRecentSearchLoaded) {
        MessagesStorage.getInstance(i).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.lambda$loadRecentSearch$7(i, i2, onRecentSearchLoaded);
            }
        });
    }

    private boolean resentSearchAvailable() {
        int i = this.dialogsType;
        return (i == 2 || i == 4 || i == 5 || i == 6 || i == 1 || i == 11 || i == 15) ? false : true;
    }

    private void searchDialogsInternal(final String str, final int i) {
        if (this.needMessagesSearch == 2) {
            return;
        }
        final String lowerCase = str.trim().toLowerCase();
        if (lowerCase.length() != 0) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsSearchAdapter.this.lambda$searchDialogsInternal$12(lowerCase, i, str);
                }
            });
            return;
        }
        this.lastSearchId = 0;
        updateSearchResults(new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), this.lastSearchId);
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
        } else if (this.dialogsType == 15) {
        } else {
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
            this.reqForumId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_search, new RequestDelegate() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda15
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    DialogsSearchAdapter.this.lambda$searchForumMessagesInternal$1(str, i2, i, tLRPC$TL_messages_search, tLObject, tLRPC$TL_error);
                }
            }, 2);
        }
    }

    private void searchMessagesInternal(final String str, final int i) {
        TLRPC$InputPeer tLRPC$TL_inputPeerEmpty;
        if (this.needMessagesSearch != 0) {
            if (TextUtils.isEmpty(this.lastMessagesSearchString) && TextUtils.isEmpty(str)) {
                return;
            }
            if (this.reqId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.reqId, true);
                this.reqId = 0;
            }
            if (TextUtils.isEmpty(str) || this.delegate.getSearchForumDialogId() != 0) {
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
            if (this.dialogsType == 15) {
                int i2 = this.waitingResponseCount - 1;
                this.waitingResponseCount = i2;
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
                if (dialogsSearchAdapterDelegate != null) {
                    dialogsSearchAdapterDelegate.searchStateChanged(i2 > 0, true);
                    this.delegate.runResultsEnterAnimation();
                    return;
                }
                return;
            }
            final TLRPC$TL_messages_searchGlobal tLRPC$TL_messages_searchGlobal = new TLRPC$TL_messages_searchGlobal();
            tLRPC$TL_messages_searchGlobal.limit = 20;
            tLRPC$TL_messages_searchGlobal.q = str;
            tLRPC$TL_messages_searchGlobal.filter = new TLRPC$TL_inputMessagesFilterEmpty();
            tLRPC$TL_messages_searchGlobal.flags |= 1;
            tLRPC$TL_messages_searchGlobal.folder_id = this.folderId;
            if (str.equals(this.lastMessagesSearchString) && !this.searchResultMessages.isEmpty() && this.lastMessagesSearchId == this.lastSearchId) {
                ArrayList arrayList = this.searchResultMessages;
                MessageObject messageObject = (MessageObject) arrayList.get(arrayList.size() - 1);
                tLRPC$TL_messages_searchGlobal.offset_id = messageObject.getId();
                tLRPC$TL_messages_searchGlobal.offset_rate = this.nextSearchRate;
                tLRPC$TL_inputPeerEmpty = MessagesController.getInstance(this.currentAccount).getInputPeer(MessageObject.getPeerId(messageObject.messageOwner.peer_id));
            } else {
                tLRPC$TL_messages_searchGlobal.offset_rate = 0;
                tLRPC$TL_messages_searchGlobal.offset_id = 0;
                tLRPC$TL_inputPeerEmpty = new TLRPC$TL_inputPeerEmpty();
            }
            tLRPC$TL_messages_searchGlobal.offset_peer = tLRPC$TL_inputPeerEmpty;
            this.lastMessagesSearchString = str;
            final int i3 = this.lastReqId + 1;
            this.lastReqId = i3;
            this.reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_searchGlobal, new RequestDelegate() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda16
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    DialogsSearchAdapter.this.lambda$searchMessagesInternal$3(str, i3, i, tLRPC$TL_messages_searchGlobal, tLObject, tLRPC$TL_error);
                }
            }, 2);
        }
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

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setRecentSearch */
    public void lambda$loadRecentSearch$4(ArrayList arrayList, LongSparseArray longSparseArray) {
        this.recentSearchObjects = arrayList;
        this.recentSearchObjectsById = longSparseArray;
        for (int i = 0; i < this.recentSearchObjects.size(); i++) {
            RecentSearchObject recentSearchObject = (RecentSearchObject) this.recentSearchObjects.get(i);
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

    private void updateSearchResults(final ArrayList arrayList, final ArrayList arrayList2, final ArrayList arrayList3, ArrayList arrayList4, final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$updateSearchResults$14(i, arrayList, arrayList2, arrayList3);
            }
        });
    }

    private boolean wordStartsWith(String str, String str2) {
        if (str2 == null || str == null) {
            return false;
        }
        String[] split = str.toLowerCase().split(" ");
        for (int i = 0; i < split.length; i++) {
            String str3 = split[i];
            if (str3 != null && (str3.startsWith(str2) || str2.startsWith(split[i]))) {
                return true;
            }
        }
        return false;
    }

    public void addHashtagsFromMessage(CharSequence charSequence) {
        this.searchAdapterHelper.addHashtagsFromMessage(charSequence);
    }

    public void clearRecentHashtags() {
        this.searchAdapterHelper.clearRecentHashtags();
        this.searchResultHashtags.clear();
        notifyDataSetChanged();
    }

    public void clearRecentSearch() {
        final StringBuilder sb;
        if (this.searchWas) {
            sb = null;
            while (this.filtered2RecentSearchObjects.size() > 0) {
                RecentSearchObject recentSearchObject = (RecentSearchObject) this.filtered2RecentSearchObjects.remove(0);
                this.recentSearchObjects.remove(recentSearchObject);
                this.filteredRecentSearchObjects.remove(recentSearchObject);
                this.recentSearchObjectsById.remove(recentSearchObject.did);
                if (sb == null) {
                    sb = new StringBuilder("did IN (");
                } else {
                    sb.append(", ");
                }
                sb.append(recentSearchObject.did);
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
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$clearRecentSearch$9(sb);
            }
        });
    }

    public void filterRecent(String str) {
        DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate;
        String str2;
        String str3;
        this.filteredRecentQuery = str;
        this.filtered2RecentSearchObjects.clear();
        int i = 0;
        if (TextUtils.isEmpty(str)) {
            this.filteredRecentSearchObjects.clear();
            int size = this.recentSearchObjects.size();
            while (i < size) {
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate2 = this.delegate;
                if ((dialogsSearchAdapterDelegate2 == null || dialogsSearchAdapterDelegate2.getSearchForumDialogId() != ((RecentSearchObject) this.recentSearchObjects.get(i)).did) && filter(((RecentSearchObject) this.recentSearchObjects.get(i)).object)) {
                    this.filteredRecentSearchObjects.add((RecentSearchObject) this.recentSearchObjects.get(i));
                }
                i++;
            }
            return;
        }
        String lowerCase = str.toLowerCase();
        int size2 = this.recentSearchObjects.size();
        while (i < size2) {
            RecentSearchObject recentSearchObject = (RecentSearchObject) this.recentSearchObjects.get(i);
            if (recentSearchObject != null && recentSearchObject.object != null && (((dialogsSearchAdapterDelegate = this.delegate) == null || dialogsSearchAdapterDelegate.getSearchForumDialogId() != recentSearchObject.did) && filter(((RecentSearchObject) this.recentSearchObjects.get(i)).object))) {
                TLObject tLObject = recentSearchObject.object;
                if (tLObject instanceof TLRPC$Chat) {
                    TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) tLObject;
                    str3 = tLRPC$Chat.title;
                    str2 = tLRPC$Chat.username;
                } else if (tLObject instanceof TLRPC$User) {
                    str3 = UserObject.getUserName((TLRPC$User) tLObject);
                    str2 = ((TLRPC$User) recentSearchObject.object).username;
                } else if (tLObject instanceof TLRPC$ChatInvite) {
                    str3 = ((TLRPC$ChatInvite) tLObject).title;
                    str2 = null;
                } else {
                    str2 = null;
                    str3 = null;
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

    public int getCurrentItemCount() {
        return this.currentItemCount;
    }

    public RecyclerListView getInnerListView() {
        return this.innerListView;
    }

    /* JADX WARN: Type inference failed for: r0v37, types: [boolean] */
    public Object getItem(int i) {
        ArrayList globalSearch;
        int i2;
        Object chat;
        if (this.searchResultHashtags.isEmpty()) {
            if (isRecentSearchDisplayed()) {
                ?? hasHints = hasHints();
                ArrayList arrayList = this.searchWas ? this.filtered2RecentSearchObjects : this.filteredRecentSearchObjects;
                if (i > hasHints && (i2 = (i - 1) - (hasHints == true ? 1 : 0)) < arrayList.size()) {
                    TLObject tLObject = ((RecentSearchObject) arrayList.get(i2)).object;
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
                if (i <= 0 || i > this.searchTopics.size()) {
                    i -= this.searchTopics.size() + 1;
                } else {
                    globalSearch = this.searchTopics;
                }
            }
            if (!this.searchContacts.isEmpty()) {
                if (i <= 0 || i > this.searchContacts.size()) {
                    i -= this.searchContacts.size() + 1;
                } else {
                    globalSearch = this.searchContacts;
                }
            }
            globalSearch = this.searchAdapterHelper.getGlobalSearch();
            ArrayList localServerSearch = this.searchAdapterHelper.getLocalServerSearch();
            ArrayList phoneSearch = this.searchAdapterHelper.getPhoneSearch();
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
            int i3 = 0;
            int size4 = globalSearch.isEmpty() ? 0 : globalSearch.size() + 1;
            if (size4 > 4 && this.globalSearchCollapsed) {
                size4 = 4;
            }
            if (i >= 0 && i < size) {
                globalSearch = this.searchResult;
                return globalSearch.get(i);
            }
            int i4 = i - size;
            if (i4 >= 0 && i4 < size2) {
                return localServerSearch.get(i4);
            }
            int i5 = i4 - size2;
            if (i5 >= 0 && i5 < size3) {
                return phoneSearch.get(i5);
            }
            i = i5 - size3;
            if (i <= 0 || i >= size4) {
                i -= size4;
                int size5 = this.searchForumResultMessages.isEmpty() ? 0 : this.searchForumResultMessages.size() + 1;
                if (i <= 0 || i > this.searchForumResultMessages.size()) {
                    if (!this.localMessagesSearchEndReached && !this.searchForumResultMessages.isEmpty()) {
                        i3 = 1;
                    }
                    i -= size5 + i3;
                    if (!this.searchResultMessages.isEmpty()) {
                        this.searchResultMessages.size();
                    }
                    if (i <= 0 || i > this.searchResultMessages.size()) {
                        return null;
                    }
                    globalSearch = this.searchResultMessages;
                } else {
                    globalSearch = this.searchForumResultMessages;
                }
            }
        } else if (i <= 0) {
            return null;
        } else {
            globalSearch = this.searchResultHashtags;
        }
        i--;
        return globalSearch.get(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        int i;
        int i2 = 0;
        int i3 = 3;
        if (this.waitingResponseCount == 3) {
            return 0;
        }
        if (this.searchResultHashtags.isEmpty()) {
            if (isRecentSearchDisplayed()) {
                i = getRecentItemsCount();
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
        return this.searchResultHashtags.size() + 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    /* JADX WARN: Code restructure failed: missing block: B:106:0x0128, code lost:
        if (r10 != 0) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x012a, code lost:
        return 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x012b, code lost:
        if (r10 != r8) goto L99;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x012d, code lost:
        return 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x012e, code lost:
        return 2;
     */
    /* JADX WARN: Type inference failed for: r0v28, types: [boolean] */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getItemViewType(int i) {
        if (!this.searchResultHashtags.isEmpty()) {
            return i == 0 ? 1 : 5;
        }
        if (isRecentSearchDisplayed()) {
            ?? hasHints = hasHints();
            if (i < hasHints) {
                return 6;
            }
            if (i == hasHints) {
                return 1;
            }
            if (i < getRecentItemsCount()) {
                return 0;
            }
            i -= getRecentItemsCount();
        }
        int i2 = 3;
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
        ArrayList globalSearch = this.searchAdapterHelper.getGlobalSearch();
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
            i2 = size3;
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
            int i3 = i - size;
            if (i3 < 0 || i3 >= size2) {
                int i4 = i3 - size2;
                if (i4 >= 0 && i4 < i2) {
                    Object item = getItem(i4);
                    if (item instanceof String) {
                        return "section".equals((String) item) ? 1 : 7;
                    }
                    return 0;
                }
                int i5 = i4 - i2;
                if (i5 >= 0 && i5 < size4) {
                    return i5 == 0 ? 1 : 0;
                }
                int i6 = i5 - size4;
                if (size6 > 0) {
                    if (i6 >= 0) {
                        if (this.localMessagesSearchEndReached) {
                        }
                    }
                    i6 -= size6 + (!this.localMessagesSearchEndReached ? 1 : 0);
                }
                if (i6 < 0 || i6 >= size5) {
                    return 4;
                }
                return i6 == 0 ? 1 : 2;
            }
            return 0;
        }
        return 0;
    }

    public String getLastSearchString() {
        return this.lastMessagesSearchString;
    }

    public int getRecentItemsCount() {
        ArrayList arrayList = this.searchWas ? this.filtered2RecentSearchObjects : this.filteredRecentSearchObjects;
        return (!arrayList.isEmpty() ? arrayList.size() + 1 : 0) + (hasHints() ? 1 : 0);
    }

    public int getRecentResultsCount() {
        ArrayList arrayList = this.searchWas ? this.filtered2RecentSearchObjects : this.filteredRecentSearchObjects;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public boolean hasRecentSearch() {
        return resentSearchAvailable() && getRecentItemsCount() > 0;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        int itemViewType = viewHolder.getItemViewType();
        return (itemViewType == 1 || itemViewType == 4) ? false : true;
    }

    /* JADX WARN: Type inference failed for: r0v19, types: [boolean] */
    public boolean isGlobalSearch(int i) {
        if (this.searchWas && this.searchResultHashtags.isEmpty()) {
            if (isRecentSearchDisplayed()) {
                ?? hasHints = hasHints();
                ArrayList arrayList = this.searchWas ? this.filtered2RecentSearchObjects : this.filteredRecentSearchObjects;
                if (i > hasHints && (i - 1) - (hasHints == true ? 1 : 0) < arrayList.size()) {
                    return false;
                }
                i -= getRecentItemsCount();
            }
            ArrayList globalSearch = this.searchAdapterHelper.getGlobalSearch();
            ArrayList localServerSearch = this.searchAdapterHelper.getLocalServerSearch();
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
            if (size5 > 0) {
                if (i >= 0 && i < size5) {
                    return false;
                }
                i -= size5 + 1;
            }
            if (size + size2 > 0 && (getRecentItemsCount() > 0 || !this.searchTopics.isEmpty())) {
                if (i == 0) {
                    return false;
                }
                i--;
            }
            if (i < 0 || i >= size) {
                int i2 = i - size;
                if (i2 < 0 || i2 >= size2) {
                    int i3 = i2 - size2;
                    if (i3 <= 0 || i3 >= size3) {
                        int i4 = i3 - size3;
                        if (i4 <= 0 || i4 >= size4) {
                            int i5 = i4 - size4;
                            int size6 = this.searchForumResultMessages.isEmpty() ? 0 : this.searchForumResultMessages.size() + 1;
                            if ((i5 <= 0 || i5 >= size6) && !this.searchResultMessages.isEmpty()) {
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

    public boolean isHashtagSearch() {
        return !this.searchResultHashtags.isEmpty();
    }

    public boolean isMessagesSearchEndReached() {
        return (this.delegate.getSearchForumDialogId() == 0 || this.localMessagesSearchEndReached) && this.messagesSearchEndReached;
    }

    public boolean isRecentSearchDisplayed() {
        return this.needMessagesSearch != 2 && hasRecentSearch();
    }

    public boolean isSearchWas() {
        return this.searchWas;
    }

    public boolean isSearching() {
        return this.waitingResponseCount > 0;
    }

    public void loadMoreSearchMessages() {
        if ((this.reqForumId == 0 || this.reqId == 0) && this.lastMessagesSearchId == this.lastSearchId) {
            DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
            if (dialogsSearchAdapterDelegate == null || dialogsSearchAdapterDelegate.getSearchForumDialogId() == 0 || this.localMessagesSearchEndReached) {
                searchMessagesInternal(this.lastMessagesSearchString, this.lastMessagesSearchId);
            } else {
                searchForumMessagesInternal(this.lastMessagesSearchString, this.lastMessagesSearchId);
            }
        }
    }

    public void loadRecentSearch() {
        int i = this.dialogsType;
        if (i == 15) {
            return;
        }
        loadRecentSearch(this.currentAccount, i, new OnRecentSearchLoaded() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda4
            @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.OnRecentSearchLoaded
            public final void setRecentSearch(ArrayList arrayList, LongSparseArray longSparseArray) {
                DialogsSearchAdapter.this.lambda$loadRecentSearch$4(arrayList, longSparseArray);
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:219:0x0495, code lost:
        if (r7.startsWith("@" + r6) != false) goto L214;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:118:0x02d1  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x02d6  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x03b1  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x03cc  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x03d5  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x042e  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0431  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x0441  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x04a0  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x052a  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0542  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x0566  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x0573  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x0593  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x0595  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x05ae  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x05b0  */
    /* JADX WARN: Type inference failed for: r3v28, types: [boolean] */
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
        int size;
        int size2;
        int size3;
        int size4;
        String str2;
        SpannableStringBuilder spannableStringBuilder;
        String str3;
        String str4;
        boolean z2;
        String str5;
        int i2;
        String str6;
        int indexOfIgnoreCase;
        int recentItemsCount;
        int i3;
        String string;
        String string2;
        View.OnClickListener onClickListener;
        String str7;
        final Runnable runnable;
        long dialogId;
        int i4;
        boolean z3;
        boolean z4;
        final int i5 = i;
        int i6 = 4;
        switch (viewHolder.getItemViewType()) {
            case 0:
                ProfileSearchCell profileSearchCell = (ProfileSearchCell) viewHolder.itemView;
                profileSearchCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                long dialogId2 = profileSearchCell.getDialogId();
                boolean isGlobalSearch = isGlobalSearch(i5);
                Object item = getItem(i5);
                if (item instanceof TLRPC$User) {
                    tLRPC$User = (TLRPC$User) item;
                    str = UserObject.getPublicUsername(tLRPC$User);
                    if (str != null && this.lastSearchText != null && !str.toLowerCase().contains(this.lastSearchText.toLowerCase()) && tLRPC$User.usernames != null) {
                        for (int i7 = 0; i7 < tLRPC$User.usernames.size(); i7++) {
                            TLRPC$TL_username tLRPC$TL_username = (TLRPC$TL_username) tLRPC$User.usernames.get(i7);
                            if (tLRPC$TL_username != null && tLRPC$TL_username.active && tLRPC$TL_username.username.toLowerCase().contains(this.lastSearchText.toLowerCase())) {
                                str = tLRPC$TL_username.username;
                            }
                        }
                    }
                    tLRPC$Chat = null;
                } else if (item instanceof TLRPC$Chat) {
                    TLRPC$Chat tLRPC$Chat2 = (TLRPC$Chat) item;
                    TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$Chat2.id));
                    if (chat != null) {
                        tLRPC$Chat2 = chat;
                    }
                    str = ChatObject.getPublicUsername(tLRPC$Chat2);
                    tLRPC$Chat = tLRPC$Chat2;
                    tLRPC$User = null;
                } else if (item instanceof TLRPC$EncryptedChat) {
                    TLRPC$EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(((TLRPC$EncryptedChat) item).id));
                    tLRPC$EncryptedChat = encryptedChat;
                    tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(encryptedChat.user_id));
                    tLRPC$Chat = null;
                    str = null;
                    if (isRecentSearchDisplayed()) {
                        z = false;
                    } else {
                        if (i5 < getRecentItemsCount()) {
                            profileSearchCell.useSeparator = i5 != getRecentItemsCount() - 1;
                            z = true;
                        } else {
                            z = false;
                        }
                        i5 -= getRecentItemsCount();
                    }
                    if (!this.searchTopics.isEmpty()) {
                        i5 -= this.searchTopics.size() + 1;
                    }
                    ArrayList globalSearch = this.searchAdapterHelper.getGlobalSearch();
                    ArrayList phoneSearch = this.searchAdapterHelper.getPhoneSearch();
                    size = this.searchResult.size();
                    size2 = this.searchAdapterHelper.getLocalServerSearch().size();
                    if (size + size2 > 0 && (getRecentItemsCount() > 0 || !this.searchTopics.isEmpty())) {
                        i5--;
                    }
                    size3 = phoneSearch.size();
                    if (size3 > 3 && this.phoneCollapsed) {
                        size3 = 3;
                    }
                    int i8 = (size3 > 0 || !(phoneSearch.get(size3 + (-1)) instanceof String)) ? size3 : size3 - 2;
                    size4 = !globalSearch.isEmpty() ? 0 : globalSearch.size() + 1;
                    if (size4 > 4 && this.globalSearchCollapsed) {
                        size4 = 4;
                    }
                    if (!z) {
                        profileSearchCell.useSeparator = (i5 == (getItemCount() - getRecentItemsCount()) - 1 || i5 == ((i8 + size) + size2) - 1 || i5 == (((size + size4) + size3) + size2) - 1) ? false : true;
                    }
                    if (i5 < 0 && i5 < this.searchResult.size() && tLRPC$User == null) {
                        CharSequence charSequence = (CharSequence) this.searchResultNames.get(i5);
                        String publicUsername = UserObject.getPublicUsername(tLRPC$User);
                        if (charSequence != 0 && tLRPC$User != null && publicUsername != null) {
                            String charSequence2 = charSequence.toString();
                            str2 = charSequence;
                            break;
                        }
                        spannableStringBuilder = charSequence;
                        str3 = null;
                        if (str3 == null) {
                            String lastFoundUsername = z ? this.filteredRecentQuery : this.searchAdapterHelper.getLastFoundUsername();
                            if (!TextUtils.isEmpty(lastFoundUsername)) {
                                String formatName = tLRPC$User != null ? ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name) : tLRPC$Chat != null ? tLRPC$Chat.title : null;
                                if (formatName != null && (indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(formatName, lastFoundUsername)) != -1) {
                                    spannableStringBuilder = new SpannableStringBuilder(formatName);
                                    spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_windowBackgroundWhiteBlueText4), indexOfIgnoreCase, lastFoundUsername.length() + indexOfIgnoreCase, 33);
                                }
                                if (str != null && (tLRPC$User == null || isGlobalSearch)) {
                                    if (lastFoundUsername.startsWith("@")) {
                                        lastFoundUsername = lastFoundUsername.substring(1);
                                    }
                                    try {
                                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                                        spannableStringBuilder2.append((CharSequence) "@");
                                        spannableStringBuilder2.append((CharSequence) str);
                                        int indexOfIgnoreCase2 = AndroidUtilities.indexOfIgnoreCase(str, lastFoundUsername);
                                        if (indexOfIgnoreCase2 != -1) {
                                            int length = lastFoundUsername.length();
                                            if (indexOfIgnoreCase2 == 0) {
                                                length++;
                                            } else {
                                                indexOfIgnoreCase2++;
                                            }
                                            spannableStringBuilder2.setSpan(new ForegroundColorSpanThemable(Theme.key_windowBackgroundWhiteBlueText4), indexOfIgnoreCase2, length + indexOfIgnoreCase2, 33);
                                        }
                                        str = spannableStringBuilder2;
                                    } catch (Exception e) {
                                        FileLog.e(e);
                                    }
                                    profileSearchCell.setChecked(false, false);
                                    if (tLRPC$User == null && tLRPC$User.id == this.selfUserId) {
                                        str4 = LocaleController.getString(R.string.SavedMessages);
                                        str = null;
                                        z2 = true;
                                    } else {
                                        str4 = spannableStringBuilder;
                                        z2 = false;
                                    }
                                    if (tLRPC$Chat != null && tLRPC$Chat.participants_count != 0) {
                                        if (ChatObject.isChannel(tLRPC$Chat) || tLRPC$Chat.megagroup) {
                                            i2 = tLRPC$Chat.participants_count;
                                            str6 = "Members";
                                        } else {
                                            i2 = tLRPC$Chat.participants_count;
                                            str6 = "Subscribers";
                                        }
                                        CharSequence formatPluralStringComma = LocaleController.formatPluralStringComma(str6, i2, ' ');
                                        if (str instanceof SpannableStringBuilder) {
                                            if (!TextUtils.isEmpty(str)) {
                                                formatPluralStringComma = TextUtils.concat(str, ", ", formatPluralStringComma);
                                            }
                                            str5 = formatPluralStringComma;
                                            profileSearchCell.setData(tLRPC$User != null ? tLRPC$User : tLRPC$Chat, tLRPC$EncryptedChat, str4, str5, true, z2);
                                            profileSearchCell.setChecked(this.delegate.isSelected(profileSearchCell.getDialogId()), dialogId2 == profileSearchCell.getDialogId());
                                            return;
                                        }
                                        ((SpannableStringBuilder) str).append((CharSequence) ", ").append(formatPluralStringComma);
                                    }
                                    str5 = str;
                                    profileSearchCell.setData(tLRPC$User != null ? tLRPC$User : tLRPC$Chat, tLRPC$EncryptedChat, str4, str5, true, z2);
                                    profileSearchCell.setChecked(this.delegate.isSelected(profileSearchCell.getDialogId()), dialogId2 == profileSearchCell.getDialogId());
                                    return;
                                }
                            }
                        }
                        str = str3;
                        profileSearchCell.setChecked(false, false);
                        if (tLRPC$User == null) {
                        }
                        str4 = spannableStringBuilder;
                        z2 = false;
                        if (tLRPC$Chat != null) {
                            if (ChatObject.isChannel(tLRPC$Chat)) {
                            }
                            i2 = tLRPC$Chat.participants_count;
                            str6 = "Members";
                            CharSequence formatPluralStringComma2 = LocaleController.formatPluralStringComma(str6, i2, ' ');
                            if (str instanceof SpannableStringBuilder) {
                            }
                        }
                        str5 = str;
                        profileSearchCell.setData(tLRPC$User != null ? tLRPC$User : tLRPC$Chat, tLRPC$EncryptedChat, str4, str5, true, z2);
                        profileSearchCell.setChecked(this.delegate.isSelected(profileSearchCell.getDialogId()), dialogId2 == profileSearchCell.getDialogId());
                        return;
                    }
                    str2 = null;
                    spannableStringBuilder = null;
                    str3 = str2;
                    if (str3 == null) {
                    }
                    str = str3;
                    profileSearchCell.setChecked(false, false);
                    if (tLRPC$User == null) {
                    }
                    str4 = spannableStringBuilder;
                    z2 = false;
                    if (tLRPC$Chat != null) {
                    }
                    str5 = str;
                    profileSearchCell.setData(tLRPC$User != null ? tLRPC$User : tLRPC$Chat, tLRPC$EncryptedChat, str4, str5, true, z2);
                    profileSearchCell.setChecked(this.delegate.isSelected(profileSearchCell.getDialogId()), dialogId2 == profileSearchCell.getDialogId());
                    return;
                } else {
                    tLRPC$User = null;
                    tLRPC$Chat = null;
                    str = null;
                }
                tLRPC$EncryptedChat = null;
                if (isRecentSearchDisplayed()) {
                }
                if (!this.searchTopics.isEmpty()) {
                }
                ArrayList globalSearch2 = this.searchAdapterHelper.getGlobalSearch();
                ArrayList phoneSearch2 = this.searchAdapterHelper.getPhoneSearch();
                size = this.searchResult.size();
                size2 = this.searchAdapterHelper.getLocalServerSearch().size();
                if (size + size2 > 0) {
                    i5--;
                }
                size3 = phoneSearch2.size();
                if (size3 > 3) {
                    size3 = 3;
                }
                if (size3 > 0) {
                }
                if (!globalSearch2.isEmpty()) {
                }
                if (size4 > 4) {
                    size4 = 4;
                }
                if (!z) {
                }
                if (i5 < 0) {
                }
                str2 = null;
                spannableStringBuilder = null;
                str3 = str2;
                if (str3 == null) {
                }
                str = str3;
                profileSearchCell.setChecked(false, false);
                if (tLRPC$User == null) {
                }
                str4 = spannableStringBuilder;
                z2 = false;
                if (tLRPC$Chat != null) {
                }
                str5 = str;
                profileSearchCell.setData(tLRPC$User != null ? tLRPC$User : tLRPC$Chat, tLRPC$EncryptedChat, str4, str5, true, z2);
                profileSearchCell.setChecked(this.delegate.isSelected(profileSearchCell.getDialogId()), dialogId2 == profileSearchCell.getDialogId());
                return;
            case 1:
                final GraySectionCell graySectionCell = (GraySectionCell) viewHolder.itemView;
                if (!this.searchResultHashtags.isEmpty()) {
                    graySectionCell.setText(LocaleController.getString(R.string.Hashtags), LocaleController.getString(R.string.ClearButton), new View.OnClickListener() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda8
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            DialogsSearchAdapter.this.lambda$onBindViewHolder$19(view);
                        }
                    });
                    return;
                }
                if (isRecentSearchDisplayed() || !this.searchTopics.isEmpty() || !this.searchContacts.isEmpty()) {
                    ?? hasHints = hasHints();
                    if (i5 < hasHints) {
                        i3 = R.string.ChatHints;
                    } else if (i5 == hasHints && isRecentSearchDisplayed()) {
                        if (this.searchWas) {
                            string = LocaleController.getString(R.string.Recent);
                            string2 = LocaleController.getString(R.string.Clear);
                            onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda10
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    DialogsSearchAdapter.this.lambda$onBindViewHolder$21(view);
                                }
                            };
                        } else {
                            string = LocaleController.getString(R.string.Recent);
                            string2 = LocaleController.getString(R.string.ClearButton);
                            onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda9
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    DialogsSearchAdapter.this.lambda$onBindViewHolder$20(view);
                                }
                            };
                        }
                        graySectionCell.setText(string, string2, onClickListener);
                        return;
                    } else if (i5 == getRecentItemsCount() + (this.searchTopics.isEmpty() ? 0 : this.searchTopics.size() + 1) + (this.searchContacts.isEmpty() ? 0 : this.searchContacts.size() + 1)) {
                        i3 = R.string.SearchAllChatsShort;
                    } else {
                        recentItemsCount = i5 - getRecentItemsCount();
                    }
                    graySectionCell.setText(LocaleController.getString(i3));
                    return;
                }
                recentItemsCount = i5;
                final ArrayList globalSearch3 = this.searchAdapterHelper.getGlobalSearch();
                int size5 = this.searchResult.size();
                int size6 = this.searchAdapterHelper.getLocalServerSearch().size();
                int size7 = this.searchAdapterHelper.getPhoneSearch().size();
                if (size7 > 3 && this.phoneCollapsed) {
                    size7 = 3;
                }
                int size8 = globalSearch3.isEmpty() ? 0 : globalSearch3.size() + 1;
                if (size8 <= 4 || !this.globalSearchCollapsed) {
                    i6 = size8;
                }
                int size9 = this.searchForumResultMessages.isEmpty() ? 0 : this.searchForumResultMessages.size() + 1;
                if (!this.searchResultMessages.isEmpty()) {
                    this.searchResultMessages.size();
                }
                if (this.searchTopics.isEmpty()) {
                    str7 = null;
                } else {
                    str7 = recentItemsCount == 0 ? LocaleController.getString(R.string.Topics) : null;
                    recentItemsCount -= this.searchTopics.size() + 1;
                }
                if (!this.searchContacts.isEmpty()) {
                    if (recentItemsCount == 0) {
                        str7 = LocaleController.getString(R.string.InviteToTelegramShort);
                    }
                    recentItemsCount -= this.searchContacts.size() + 1;
                }
                if (str7 == null) {
                    int i9 = recentItemsCount - (size5 + size6);
                    if (i9 < 0 || i9 >= size7) {
                        int i10 = i9 - size7;
                        if (i10 >= 0 && i10 < i6) {
                            str7 = LocaleController.getString(R.string.GlobalSearch);
                            if (this.searchAdapterHelper.getGlobalSearch().size() > 3) {
                                r10 = this.globalSearchCollapsed;
                                runnable = new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda12
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        DialogsSearchAdapter.this.lambda$onBindViewHolder$25(globalSearch3, i5, graySectionCell);
                                    }
                                };
                            }
                        } else if (this.delegate == null || size9 <= 0 || i10 - i6 > 1) {
                            str7 = LocaleController.getString(R.string.SearchMessages);
                        } else {
                            TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.delegate.getSearchForumDialogId()));
                            str7 = LocaleController.formatString("SearchMessagesIn", R.string.SearchMessagesIn, chat2 == null ? "null" : chat2.title);
                        }
                    } else {
                        str7 = LocaleController.getString(R.string.PhoneNumberSearch);
                        if (this.searchAdapterHelper.getPhoneSearch().size() > 3) {
                            r10 = this.phoneCollapsed;
                            runnable = new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda11
                                @Override // java.lang.Runnable
                                public final void run() {
                                    DialogsSearchAdapter.this.lambda$onBindViewHolder$22(graySectionCell);
                                }
                            };
                        }
                    }
                    if (runnable != null) {
                        graySectionCell.setText(str7);
                        return;
                    } else {
                        graySectionCell.setText(str7, LocaleController.getString(r10 ? R.string.ShowMore : R.string.ShowLess), new View.OnClickListener() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda13
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                runnable.run();
                            }
                        });
                        return;
                    }
                }
                runnable = null;
                if (runnable != null) {
                }
                break;
            case 2:
                DialogCell dialogCell = (DialogCell) viewHolder.itemView;
                dialogCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                dialogCell.useSeparator = i5 != getItemCount() - 1;
                MessageObject messageObject = (MessageObject) getItem(i5);
                dialogCell.useFromUserAsAvatar = this.searchForumResultMessages.contains(messageObject);
                if (messageObject == null) {
                    z3 = false;
                    z4 = false;
                    dialogId = 0;
                    messageObject = null;
                    i4 = 0;
                } else {
                    dialogId = messageObject.getDialogId();
                    i4 = messageObject.messageOwner.date;
                    z3 = false;
                    z4 = false;
                }
                dialogCell.setDialog(dialogId, messageObject, i4, z3, z4);
                return;
            case 3:
                ((TopicSearchCell) viewHolder.itemView).setTopic((TLRPC$TL_forumTopic) getItem(i5));
                return;
            case 4:
            default:
                return;
            case 5:
                HashtagSearchCell hashtagSearchCell = (HashtagSearchCell) viewHolder.itemView;
                hashtagSearchCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                hashtagSearchCell.setText((CharSequence) this.searchResultHashtags.get(i5 - 1));
                hashtagSearchCell.setNeedDivider(i5 != this.searchResultHashtags.size());
                return;
            case 6:
                ((CategoryAdapterRecycler) ((RecyclerListView) viewHolder.itemView).getAdapter()).setIndex(i5 / 2);
                return;
            case 7:
                TextCell textCell = (TextCell) viewHolder.itemView;
                textCell.setColors(-1, Theme.key_windowBackgroundWhiteBlueText2);
                textCell.setText(LocaleController.formatString("AddContactByPhone", R.string.AddContactByPhone, PhoneFormat.getInstance().format("+" + ((String) getItem(i5)))), false);
                return;
            case 8:
                ProfileSearchCell profileSearchCell2 = (ProfileSearchCell) viewHolder.itemView;
                ContactsController.Contact contact = (ContactsController.Contact) getItem(i5);
                profileSearchCell2.setData(contact, null, ContactsController.formatName(contact.first_name, contact.last_name), PhoneFormat.getInstance().format("+" + contact.shortPhones.get(0)), false, false);
                return;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        RecyclerListView recyclerListView;
        switch (i) {
            case 0:
                recyclerListView = new ProfileSearchCell(this.mContext).showPremiumBlock(this.dialogsType == 3);
                break;
            case 1:
                recyclerListView = new GraySectionCell(this.mContext);
                break;
            case 2:
                recyclerListView = new DialogCell(null, this.mContext, false, true) { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter.3
                    @Override // org.telegram.ui.Cells.DialogCell
                    public boolean isForumCell() {
                        return false;
                    }
                };
                break;
            case 3:
                recyclerListView = new TopicSearchCell(this.mContext);
                break;
            case 4:
                FlickerLoadingView flickerLoadingView = new FlickerLoadingView(this.mContext);
                flickerLoadingView.setViewType(1);
                flickerLoadingView.setIsSingleCell(true);
                recyclerListView = flickerLoadingView;
                break;
            case 5:
                recyclerListView = new HashtagSearchCell(this.mContext);
                break;
            case 6:
                RecyclerListView recyclerListView2 = new RecyclerListView(this.mContext) { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter.4
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
                recyclerListView2.setSelectorDrawableColor(Theme.getColor(Theme.key_listSelector));
                recyclerListView2.setTag(9);
                recyclerListView2.setItemAnimator(null);
                recyclerListView2.setLayoutAnimation(null);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.mContext) { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter.5
                    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                    public boolean supportsPredictiveItemAnimations() {
                        return false;
                    }
                };
                linearLayoutManager.setOrientation(0);
                recyclerListView2.setLayoutManager(linearLayoutManager);
                recyclerListView2.setAdapter(new CategoryAdapterRecycler(this.mContext, this.currentAccount, false, this.dialogsType == 3, this.resourcesProvider));
                recyclerListView2.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda5
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view, int i2) {
                        DialogsSearchAdapter.this.lambda$onCreateViewHolder$17(view, i2);
                    }
                });
                recyclerListView2.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda6
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
                    public final boolean onItemClick(View view, int i2) {
                        boolean lambda$onCreateViewHolder$18;
                        lambda$onCreateViewHolder$18 = DialogsSearchAdapter.this.lambda$onCreateViewHolder$18(view, i2);
                        return lambda$onCreateViewHolder$18;
                    }
                });
                this.innerListView = recyclerListView2;
                recyclerListView = recyclerListView2;
                break;
            case 7:
            default:
                recyclerListView = new TextCell(this.mContext, 16, false);
                break;
            case 8:
                recyclerListView = new ProfileSearchCell(this.mContext);
                break;
        }
        recyclerListView.setLayoutParams(i == 5 ? new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(86.0f)) : new RecyclerView.LayoutParams(-1, -2));
        return new RecyclerListView.Holder(recyclerListView);
    }

    public void putRecentSearch(final long j, TLObject tLObject) {
        RecentSearchObject recentSearchObject = (RecentSearchObject) this.recentSearchObjectsById.get(j);
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
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$putRecentSearch$8(j);
            }
        });
    }

    public void removeRecentSearch(final long j) {
        RecentSearchObject recentSearchObject = (RecentSearchObject) this.recentSearchObjectsById.get(j);
        if (recentSearchObject == null) {
            return;
        }
        this.recentSearchObjectsById.remove(j);
        this.recentSearchObjects.remove(recentSearchObject);
        this.filtered2RecentSearchObjects.remove(recentSearchObject);
        this.filteredRecentSearchObjects.remove(recentSearchObject);
        notifyDataSetChanged();
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$removeRecentSearch$10(j);
            }
        });
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
            int i2 = this.dialogsType;
            if (i2 != 15) {
                SearchAdapterHelper searchAdapterHelper = this.searchAdapterHelper;
                boolean z = i2 != 11;
                boolean z2 = i2 != 11;
                boolean z3 = i2 == 2 || i2 == 11;
                boolean z4 = i2 == 0;
                DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate = this.delegate;
                searchAdapterHelper.queryServerSearch(null, true, true, z, z2, z3, 0L, z4, 0, 0, dialogsSearchAdapterDelegate != null ? dialogsSearchAdapterDelegate.getSearchForumDialogId() : 0L);
            }
            this.searchWas = false;
            this.lastSearchId = 0;
            this.waitingResponseCount = 0;
            this.globalSearchCollapsed = true;
            this.phoneCollapsed = true;
            DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate2 = this.delegate;
            if (dialogsSearchAdapterDelegate2 != null) {
                dialogsSearchAdapterDelegate2.searchStateChanged(false, true);
            }
            if (this.dialogsType != 15) {
                searchTopics(null);
                searchMessagesInternal(null, 0);
                searchForumMessagesInternal(null, 0);
            }
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
                ArrayList hashtags = this.searchAdapterHelper.getHashtags();
                for (int i3 = 0; i3 < hashtags.size(); i3++) {
                    this.searchResultHashtags.add(((SearchAdapterHelper.HashtagObject) hashtags.get(i3)).hashtag);
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
        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Adapters.DialogsSearchAdapter$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                DialogsSearchAdapter.this.lambda$searchDialogs$16(trim, i4, str);
            }
        };
        this.searchRunnable = runnable2;
        dispatchQueue.postRunnable(runnable2, 300L);
    }

    public void setDelegate(DialogsSearchAdapterDelegate dialogsSearchAdapterDelegate) {
        this.delegate = dialogsSearchAdapterDelegate;
    }

    public void setFilterDialogIds(ArrayList arrayList) {
        this.filterDialogIds = arrayList;
    }

    public void setFiltersDelegate(FilteredSearchView.Delegate delegate, boolean z) {
        this.filtersDelegate = delegate;
        if (delegate == null || !z) {
            return;
        }
        delegate.updateFiltersView(false, null, this.localTipDates, this.localTipArchive);
    }
}
