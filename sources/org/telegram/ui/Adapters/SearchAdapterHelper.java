package org.telegram.ui.Adapters;

import android.util.Pair;
import androidx.collection.LongSparseArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Components.ShareAlert;
/* loaded from: classes4.dex */
public class SearchAdapterHelper {
    private boolean allResultsAreGlobal;
    private SearchAdapterHelperDelegate delegate;
    private ArrayList hashtags;
    private HashMap hashtagsByText;
    private String lastFoundChannel;
    private ArrayList localRecentResults;
    private ArrayList localSearchResults;
    private final ArrayList pendingRequestIds = new ArrayList();
    private String lastFoundUsername = null;
    private final ArrayList localServerSearch = new ArrayList();
    private final ArrayList globalSearch = new ArrayList();
    private final LongSparseArray globalSearchMap = new LongSparseArray();
    private final ArrayList groupSearch = new ArrayList();
    private final LongSparseArray groupSearchMap = new LongSparseArray();
    private final LongSparseArray phoneSearchMap = new LongSparseArray();
    private final ArrayList phonesSearch = new ArrayList();
    private int currentAccount = UserConfig.selectedAccount;
    private boolean allowGlobalResults = true;
    private boolean hashtagsLoadedFromDb = false;

    /* loaded from: classes4.dex */
    public static class HashtagObject {
        int date;
        String hashtag;
    }

    /* loaded from: classes4.dex */
    public interface SearchAdapterHelperDelegate {

        /* loaded from: classes4.dex */
        public abstract /* synthetic */ class -CC {
            public static boolean $default$canApplySearchResults(SearchAdapterHelperDelegate searchAdapterHelperDelegate, int i) {
                return true;
            }

            public static LongSparseArray $default$getExcludeCallParticipants(SearchAdapterHelperDelegate searchAdapterHelperDelegate) {
                return null;
            }

            public static LongSparseArray $default$getExcludeUsers(SearchAdapterHelperDelegate searchAdapterHelperDelegate) {
                return null;
            }

            public static void $default$onSetHashtags(SearchAdapterHelperDelegate searchAdapterHelperDelegate, ArrayList arrayList, HashMap hashMap) {
            }
        }

        boolean canApplySearchResults(int i);

        LongSparseArray getExcludeCallParticipants();

        LongSparseArray getExcludeUsers();

        void onDataSetChanged(int i);

        void onSetHashtags(ArrayList arrayList, HashMap hashMap);
    }

    public SearchAdapterHelper(boolean z) {
        this.allResultsAreGlobal = z;
    }

    public /* synthetic */ void lambda$clearRecentHashtags$8() {
        try {
            MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("DELETE FROM hashtag_recent_v2 WHERE 1").stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static /* synthetic */ int lambda$loadRecentHashtags$4(HashtagObject hashtagObject, HashtagObject hashtagObject2) {
        int i = hashtagObject.date;
        int i2 = hashtagObject2.date;
        if (i < i2) {
            return 1;
        }
        return i > i2 ? -1 : 0;
    }

    public /* synthetic */ void lambda$loadRecentHashtags$6() {
        try {
            SQLiteCursor queryFinalized = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized("SELECT id, date FROM hashtag_recent_v2 WHERE 1", new Object[0]);
            final ArrayList arrayList = new ArrayList();
            final HashMap hashMap = new HashMap();
            while (queryFinalized.next()) {
                HashtagObject hashtagObject = new HashtagObject();
                hashtagObject.hashtag = queryFinalized.stringValue(0);
                hashtagObject.date = queryFinalized.intValue(1);
                arrayList.add(hashtagObject);
                hashMap.put(hashtagObject.hashtag, hashtagObject);
            }
            queryFinalized.dispose();
            Collections.sort(arrayList, new Comparator() { // from class: org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda7
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$loadRecentHashtags$4;
                    lambda$loadRecentHashtags$4 = SearchAdapterHelper.lambda$loadRecentHashtags$4((SearchAdapterHelper.HashtagObject) obj, (SearchAdapterHelper.HashtagObject) obj2);
                    return lambda$loadRecentHashtags$4;
                }
            });
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    SearchAdapterHelper.this.lambda$loadRecentHashtags$5(arrayList, hashMap);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$putRecentHashtags$7(ArrayList arrayList) {
        int i;
        try {
            MessagesStorage.getInstance(this.currentAccount).getDatabase().beginTransaction();
            SQLitePreparedStatement executeFast = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO hashtag_recent_v2 VALUES(?, ?)");
            int i2 = 0;
            while (true) {
                if (i2 >= arrayList.size() || i2 == 100) {
                    break;
                }
                HashtagObject hashtagObject = (HashtagObject) arrayList.get(i2);
                executeFast.requery();
                executeFast.bindString(1, hashtagObject.hashtag);
                executeFast.bindInteger(2, hashtagObject.date);
                executeFast.step();
                i2++;
            }
            executeFast.dispose();
            if (arrayList.size() > 100) {
                SQLitePreparedStatement executeFast2 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("DELETE FROM hashtag_recent_v2 WHERE id = ?");
                for (i = 100; i < arrayList.size(); i++) {
                    executeFast2.requery();
                    executeFast2.bindString(1, ((HashtagObject) arrayList.get(i)).hashtag);
                    executeFast2.step();
                }
                executeFast2.dispose();
            }
            MessagesStorage.getInstance(this.currentAccount).getDatabase().commitTransaction();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$queryServerSearch$0(String str, boolean z, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            TLRPC.TL_channels_channelParticipants tL_channels_channelParticipants = (TLRPC.TL_channels_channelParticipants) tLObject;
            this.lastFoundChannel = str.toLowerCase();
            MessagesController.getInstance(this.currentAccount).putUsers(tL_channels_channelParticipants.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(tL_channels_channelParticipants.chats, false);
            this.groupSearch.clear();
            this.groupSearchMap.clear();
            this.groupSearch.addAll(tL_channels_channelParticipants.participants);
            long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
            int size = tL_channels_channelParticipants.participants.size();
            for (int i = 0; i < size; i++) {
                TLRPC.ChannelParticipant channelParticipant = tL_channels_channelParticipants.participants.get(i);
                long peerId = MessageObject.getPeerId(channelParticipant.peer);
                if (z || peerId != clientUserId) {
                    this.groupSearchMap.put(peerId, channelParticipant);
                } else {
                    this.groupSearch.remove(channelParticipant);
                }
            }
        }
    }

    public /* synthetic */ void lambda$queryServerSearch$1(int i, boolean z, boolean z2, boolean z3, boolean z4, long j, String str, TLObject tLObject, TLRPC.TL_error tL_error) {
        TLRPC.Chat chat;
        TLRPC.User user;
        ArrayList<TLRPC.Peer> arrayList;
        TLRPC.Chat chat2;
        TLRPC.User user2;
        if (this.delegate.canApplySearchResults(i) && tL_error == null) {
            TLRPC.TL_contacts_found tL_contacts_found = (TLRPC.TL_contacts_found) tLObject;
            this.globalSearch.clear();
            this.globalSearchMap.clear();
            this.localServerSearch.clear();
            MessagesController.getInstance(this.currentAccount).putChats(tL_contacts_found.chats, false);
            MessagesController.getInstance(this.currentAccount).putUsers(tL_contacts_found.users, false);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(tL_contacts_found.users, tL_contacts_found.chats, true, true);
            LongSparseArray longSparseArray = new LongSparseArray();
            LongSparseArray longSparseArray2 = new LongSparseArray();
            for (int i2 = 0; i2 < tL_contacts_found.chats.size(); i2++) {
                TLRPC.Chat chat3 = tL_contacts_found.chats.get(i2);
                longSparseArray.put(chat3.id, chat3);
            }
            for (int i3 = 0; i3 < tL_contacts_found.users.size(); i3++) {
                TLRPC.User user3 = tL_contacts_found.users.get(i3);
                longSparseArray2.put(user3.id, user3);
            }
            for (int i4 = 0; i4 < 2; i4++) {
                if (i4 != 0) {
                    arrayList = tL_contacts_found.results;
                } else if (this.allResultsAreGlobal) {
                    arrayList = tL_contacts_found.my_results;
                }
                for (int i5 = 0; i5 < arrayList.size(); i5++) {
                    TLRPC.Peer peer = arrayList.get(i5);
                    long j2 = peer.user_id;
                    if (j2 != 0) {
                        user2 = (TLRPC.User) longSparseArray2.get(j2);
                        chat2 = null;
                    } else {
                        long j3 = peer.chat_id;
                        if (j3 == 0) {
                            j3 = peer.channel_id;
                            if (j3 == 0) {
                                chat2 = null;
                                user2 = null;
                            }
                        }
                        chat2 = (TLRPC.Chat) longSparseArray.get(j3);
                        user2 = null;
                    }
                    if (chat2 != null) {
                        if (z && ((!z2 || ChatObject.canAddBotsToChat(chat2)) && ((this.allowGlobalResults || !ChatObject.isNotInChat(chat2)) && filter(chat2)))) {
                            this.globalSearch.add(chat2);
                            this.globalSearchMap.put(-chat2.id, chat2);
                        }
                    } else if (user2 != null && !z2 && ((z3 || !user2.bot) && ((z4 || !user2.self) && ((this.allowGlobalResults || i4 != 1 || user2.contact) && filter(user2))))) {
                        this.globalSearch.add(user2);
                        this.globalSearchMap.put(user2.id, user2);
                    }
                }
            }
            if (!this.allResultsAreGlobal) {
                for (int i6 = 0; i6 < tL_contacts_found.my_results.size(); i6++) {
                    TLRPC.Peer peer2 = tL_contacts_found.my_results.get(i6);
                    long j4 = peer2.user_id;
                    if (j4 != 0) {
                        user = (TLRPC.User) longSparseArray2.get(j4);
                        chat = null;
                    } else {
                        long j5 = peer2.chat_id;
                        if (j5 == 0) {
                            j5 = peer2.channel_id;
                            if (j5 == 0) {
                                chat = null;
                                user = null;
                            }
                        }
                        chat = (TLRPC.Chat) longSparseArray.get(j5);
                        user = null;
                    }
                    if (chat != null) {
                        if (z && ((!z2 || ChatObject.canAddBotsToChat(chat)) && (-chat.id) != j && filter(chat))) {
                            this.localServerSearch.add(chat);
                            this.globalSearchMap.put(-chat.id, chat);
                        }
                    } else if (user != null && !z2 && ((z3 || !user.bot) && ((z4 || !user.self) && user.id != j && filter(user)))) {
                        this.localServerSearch.add(user);
                        this.globalSearchMap.put(user.id, user);
                    }
                }
            }
            this.lastFoundUsername = str.toLowerCase();
        }
    }

    public /* synthetic */ void lambda$queryServerSearch$2(ArrayList arrayList, int i, TLObject tLObject, TLRPC.TL_error tL_error, AtomicInteger atomicInteger, AtomicInteger atomicInteger2, ArrayList arrayList2, int i2, Runnable runnable) {
        arrayList.set(i, new Pair(tLObject, tL_error));
        Integer valueOf = Integer.valueOf(atomicInteger.get());
        if (this.pendingRequestIds.contains(valueOf)) {
            this.pendingRequestIds.remove(valueOf);
            if (atomicInteger2.incrementAndGet() == arrayList2.size()) {
                for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                    RequestDelegate requestDelegate = (RequestDelegate) ((Pair) arrayList2.get(i3)).second;
                    Pair pair = (Pair) arrayList.get(i3);
                    if (pair != null) {
                        requestDelegate.run((TLObject) pair.first, (TLRPC.TL_error) pair.second);
                    }
                }
                removeGroupSearchFromGlobal();
                ArrayList arrayList3 = this.localSearchResults;
                if (arrayList3 != null) {
                    mergeResults(arrayList3, this.localRecentResults);
                }
                mergeExcludeResults();
                this.delegate.onDataSetChanged(i2);
                if (runnable != null) {
                    runnable.run();
                }
            }
        }
    }

    public /* synthetic */ void lambda$queryServerSearch$3(final ArrayList arrayList, final int i, final AtomicInteger atomicInteger, final AtomicInteger atomicInteger2, final ArrayList arrayList2, final int i2, final Runnable runnable, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                SearchAdapterHelper.this.lambda$queryServerSearch$2(arrayList, i, tLObject, tL_error, atomicInteger, atomicInteger2, arrayList2, i2, runnable);
            }
        });
    }

    private void putRecentHashtags(final ArrayList arrayList) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                SearchAdapterHelper.this.lambda$putRecentHashtags$7(arrayList);
            }
        });
    }

    private void removeGroupSearchFromGlobal() {
        if (this.globalSearchMap.size() == 0) {
            return;
        }
        int size = this.groupSearchMap.size();
        for (int i = 0; i < size; i++) {
            TLRPC.User user = (TLRPC.User) this.globalSearchMap.get(this.groupSearchMap.keyAt(i));
            if (user != null) {
                this.globalSearch.remove(user);
                this.localServerSearch.remove(user);
                this.globalSearchMap.remove(user.id);
            }
        }
    }

    public void addGroupMembers(ArrayList arrayList) {
        LongSparseArray longSparseArray;
        long peerId;
        this.groupSearch.clear();
        this.groupSearch.addAll(arrayList);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLObject tLObject = (TLObject) arrayList.get(i);
            if (tLObject instanceof TLRPC.ChatParticipant) {
                longSparseArray = this.groupSearchMap;
                peerId = ((TLRPC.ChatParticipant) tLObject).user_id;
            } else if (tLObject instanceof TLRPC.ChannelParticipant) {
                longSparseArray = this.groupSearchMap;
                peerId = MessageObject.getPeerId(((TLRPC.ChannelParticipant) tLObject).peer);
            }
            longSparseArray.put(peerId, tLObject);
        }
        removeGroupSearchFromGlobal();
    }

    public void addHashtagsFromMessage(CharSequence charSequence) {
        if (charSequence == null) {
            return;
        }
        Matcher matcher = Pattern.compile("(^|\\s)#[^0-9][\\w@.]+").matcher(charSequence);
        boolean z = false;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (charSequence.charAt(start) != '@' && charSequence.charAt(start) != '#') {
                start++;
            }
            String charSequence2 = charSequence.subSequence(start, end).toString();
            if (this.hashtagsByText == null) {
                this.hashtagsByText = new HashMap();
                this.hashtags = new ArrayList();
            }
            HashtagObject hashtagObject = (HashtagObject) this.hashtagsByText.get(charSequence2);
            if (hashtagObject == null) {
                hashtagObject = new HashtagObject();
                hashtagObject.hashtag = charSequence2;
                this.hashtagsByText.put(charSequence2, hashtagObject);
            } else {
                this.hashtags.remove(hashtagObject);
            }
            hashtagObject.date = (int) (System.currentTimeMillis() / 1000);
            this.hashtags.add(0, hashtagObject);
            z = true;
        }
        if (z) {
            putRecentHashtags(this.hashtags);
        }
    }

    public void clear() {
        this.globalSearch.clear();
        this.globalSearchMap.clear();
        this.localServerSearch.clear();
    }

    public void clearRecentHashtags() {
        this.hashtags = new ArrayList();
        this.hashtagsByText = new HashMap();
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                SearchAdapterHelper.this.lambda$clearRecentHashtags$8();
            }
        });
    }

    protected boolean filter(TLObject tLObject) {
        return true;
    }

    public ArrayList getGlobalSearch() {
        return this.globalSearch;
    }

    public ArrayList getGroupSearch() {
        return this.groupSearch;
    }

    public ArrayList getHashtags() {
        return this.hashtags;
    }

    public String getLastFoundChannel() {
        return this.lastFoundChannel;
    }

    public String getLastFoundUsername() {
        return this.lastFoundUsername;
    }

    public ArrayList getLocalServerSearch() {
        return this.localServerSearch;
    }

    public ArrayList getPhoneSearch() {
        return this.phonesSearch;
    }

    public boolean isSearchInProgress() {
        return this.pendingRequestIds.size() > 0;
    }

    public boolean loadRecentHashtags() {
        if (this.hashtagsLoadedFromDb) {
            return true;
        }
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                SearchAdapterHelper.this.lambda$loadRecentHashtags$6();
            }
        });
        return false;
    }

    public void mergeExcludeResults() {
        SearchAdapterHelperDelegate searchAdapterHelperDelegate = this.delegate;
        if (searchAdapterHelperDelegate == null) {
            return;
        }
        LongSparseArray excludeUsers = searchAdapterHelperDelegate.getExcludeUsers();
        if (excludeUsers != null) {
            int size = excludeUsers.size();
            for (int i = 0; i < size; i++) {
                TLRPC.User user = (TLRPC.User) this.globalSearchMap.get(excludeUsers.keyAt(i));
                if (user != null) {
                    this.globalSearch.remove(user);
                    this.localServerSearch.remove(user);
                    this.globalSearchMap.remove(user.id);
                }
            }
        }
        LongSparseArray excludeCallParticipants = this.delegate.getExcludeCallParticipants();
        if (excludeCallParticipants != null) {
            int size2 = excludeCallParticipants.size();
            for (int i2 = 0; i2 < size2; i2++) {
                TLRPC.User user2 = (TLRPC.User) this.globalSearchMap.get(excludeCallParticipants.keyAt(i2));
                if (user2 != null) {
                    this.globalSearch.remove(user2);
                    this.localServerSearch.remove(user2);
                    this.globalSearchMap.remove(user2.id);
                }
            }
        }
    }

    public void mergeResults(ArrayList arrayList) {
        mergeResults(arrayList, null);
    }

    public void mergeResults(ArrayList arrayList, ArrayList arrayList2) {
        TLRPC.Chat chat;
        LongSparseArray longSparseArray;
        long j;
        this.localSearchResults = arrayList;
        this.localRecentResults = arrayList2;
        if (this.globalSearchMap.size() != 0) {
            if (arrayList == null && arrayList2 == null) {
                return;
            }
            int i = 0;
            int size = arrayList == null ? 0 : arrayList.size();
            int size2 = (arrayList2 == null ? 0 : arrayList2.size()) + size;
            while (i < size2) {
                Object obj = i < size ? arrayList.get(i) : arrayList2.get(i - size);
                if (obj instanceof DialogsSearchAdapter.RecentSearchObject) {
                    obj = ((DialogsSearchAdapter.RecentSearchObject) obj).object;
                }
                if (obj instanceof ShareAlert.DialogSearchResult) {
                    obj = ((ShareAlert.DialogSearchResult) obj).object;
                }
                if (obj instanceof TLRPC.User) {
                    TLRPC.User user = (TLRPC.User) obj;
                    TLRPC.User user2 = (TLRPC.User) this.globalSearchMap.get(user.id);
                    if (user2 != null) {
                        this.globalSearch.remove(user2);
                        this.localServerSearch.remove(user2);
                        this.globalSearchMap.remove(user2.id);
                    }
                    TLObject tLObject = (TLObject) this.groupSearchMap.get(user.id);
                    if (tLObject != null) {
                        this.groupSearch.remove(tLObject);
                        this.groupSearchMap.remove(user.id);
                    }
                    Object obj2 = this.phoneSearchMap.get(user.id);
                    if (obj2 != null) {
                        this.phonesSearch.remove(obj2);
                        longSparseArray = this.phoneSearchMap;
                        j = user.id;
                        longSparseArray.remove(j);
                        i++;
                    } else {
                        i++;
                    }
                } else {
                    if ((obj instanceof TLRPC.Chat) && (chat = (TLRPC.Chat) this.globalSearchMap.get(-((TLRPC.Chat) obj).id)) != null) {
                        this.globalSearch.remove(chat);
                        this.localServerSearch.remove(chat);
                        longSparseArray = this.globalSearchMap;
                        j = -chat.id;
                        longSparseArray.remove(j);
                    }
                    i++;
                }
            }
        }
    }

    public void queryServerSearch(String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, long j, boolean z6, int i, int i2) {
        queryServerSearch(str, z, z2, z3, z4, z5, j, z6, i, i2, 0L, null);
    }

    public void queryServerSearch(String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, long j, boolean z6, int i, int i2, long j2) {
        queryServerSearch(str, z, z2, z3, z4, z5, j, z6, i, i2, j2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:114:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0190  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x019f  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x01bb A[LOOP:2: B:135:0x01b5->B:137:0x01bb, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void queryServerSearch(final String str, boolean z, final boolean z2, final boolean z3, final boolean z4, final boolean z5, long j, boolean z6, int i, final int i2, final long j2, final Runnable runnable) {
        boolean z7;
        boolean z8;
        int i3;
        int size;
        boolean z9;
        int i4;
        String str2;
        Iterator it = this.pendingRequestIds.iterator();
        while (it.hasNext()) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(((Integer) it.next()).intValue(), true);
        }
        this.pendingRequestIds.clear();
        if (str == null) {
            this.groupSearch.clear();
            this.groupSearchMap.clear();
            this.globalSearch.clear();
            this.globalSearchMap.clear();
            this.localServerSearch.clear();
            this.phonesSearch.clear();
            this.phoneSearchMap.clear();
            this.delegate.onDataSetChanged(i2);
            return;
        }
        final ArrayList arrayList = new ArrayList();
        if (str.length() > 0) {
            if (j != 0) {
                TLRPC.TL_channels_getParticipants tL_channels_getParticipants = new TLRPC.TL_channels_getParticipants();
                tL_channels_getParticipants.filter = i == 1 ? new TLRPC.TL_channelParticipantsAdmins() : i == 3 ? new TLRPC.TL_channelParticipantsBanned() : i == 0 ? new TLRPC.TL_channelParticipantsKicked() : new TLRPC.TL_channelParticipantsSearch();
                tL_channels_getParticipants.filter.q = str;
                tL_channels_getParticipants.limit = 50;
                tL_channels_getParticipants.offset = 0;
                tL_channels_getParticipants.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(j);
                arrayList.add(new Pair(tL_channels_getParticipants, new RequestDelegate() { // from class: org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda0
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        SearchAdapterHelper.this.lambda$queryServerSearch$0(str, z4, tLObject, tL_error);
                    }
                }));
            } else {
                this.lastFoundChannel = str.toLowerCase();
            }
            z7 = false;
        } else {
            this.groupSearch.clear();
            this.groupSearchMap.clear();
            z7 = true;
        }
        if (z) {
            if (str.length() <= 0) {
                this.globalSearch.clear();
                this.globalSearchMap.clear();
                this.localServerSearch.clear();
                z8 = false;
                if (!z5 && z6 && str.startsWith("+") && str.length() > 3) {
                    this.phonesSearch.clear();
                    this.phoneSearchMap.clear();
                    String stripExceptNumbers = PhoneFormat.stripExceptNumbers(str);
                    ArrayList<TLRPC.TL_contact> arrayList2 = ContactsController.getInstance(this.currentAccount).contacts;
                    size = arrayList2.size();
                    z9 = false;
                    for (i4 = 0; i4 < size; i4++) {
                        TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(arrayList2.get(i4).user_id));
                        if (user != null && (str2 = user.phone) != null && str2.startsWith(stripExceptNumbers)) {
                            if (!z9) {
                                z9 = user.phone.length() == stripExceptNumbers.length();
                            }
                            this.phonesSearch.add(user);
                            this.phoneSearchMap.put(user.id, user);
                        }
                    }
                    if (!z9) {
                        this.phonesSearch.add("section");
                        this.phonesSearch.add(stripExceptNumbers);
                    }
                    z8 = false;
                }
                if (!z8) {
                    this.delegate.onDataSetChanged(i2);
                }
                final AtomicInteger atomicInteger = new AtomicInteger(0);
                final ArrayList arrayList3 = new ArrayList();
                for (i3 = 0; i3 < arrayList.size(); i3++) {
                    arrayList3.add(null);
                    final AtomicInteger atomicInteger2 = new AtomicInteger();
                    final int i5 = i3;
                    atomicInteger2.set(ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject) ((Pair) arrayList.get(i3)).first, new RequestDelegate() { // from class: org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda2
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            SearchAdapterHelper.this.lambda$queryServerSearch$3(arrayList3, i5, atomicInteger2, atomicInteger, arrayList, i2, runnable, tLObject, tL_error);
                        }
                    }));
                    this.pendingRequestIds.add(Integer.valueOf(atomicInteger2.get()));
                }
            }
            TLRPC.TL_contacts_search tL_contacts_search = new TLRPC.TL_contacts_search();
            tL_contacts_search.q = str;
            tL_contacts_search.limit = 20;
            arrayList.add(new Pair(tL_contacts_search, new RequestDelegate() { // from class: org.telegram.ui.Adapters.SearchAdapterHelper$$ExternalSyntheticLambda1
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    SearchAdapterHelper.this.lambda$queryServerSearch$1(i2, z2, z5, z3, z4, j2, str, tLObject, tL_error);
                }
            }));
        }
        z8 = z7;
        if (!z5) {
            this.phonesSearch.clear();
            this.phoneSearchMap.clear();
            String stripExceptNumbers2 = PhoneFormat.stripExceptNumbers(str);
            ArrayList<TLRPC.TL_contact> arrayList22 = ContactsController.getInstance(this.currentAccount).contacts;
            size = arrayList22.size();
            z9 = false;
            while (i4 < size) {
            }
            if (!z9) {
            }
            z8 = false;
        }
        if (!z8) {
        }
        final AtomicInteger atomicInteger3 = new AtomicInteger(0);
        final ArrayList arrayList32 = new ArrayList();
        while (i3 < arrayList.size()) {
        }
    }

    public void removeUserId(long j) {
        Object obj = this.globalSearchMap.get(j);
        if (obj != null) {
            this.globalSearch.remove(obj);
        }
        Object obj2 = this.groupSearchMap.get(j);
        if (obj2 != null) {
            this.groupSearch.remove(obj2);
        }
    }

    public void setAllowGlobalResults(boolean z) {
        this.allowGlobalResults = z;
    }

    public void setDelegate(SearchAdapterHelperDelegate searchAdapterHelperDelegate) {
        this.delegate = searchAdapterHelperDelegate;
    }

    /* renamed from: setHashtags */
    public void lambda$loadRecentHashtags$5(ArrayList arrayList, HashMap hashMap) {
        this.hashtags = arrayList;
        this.hashtagsByText = hashMap;
        this.hashtagsLoadedFromDb = true;
        this.delegate.onSetHashtags(arrayList, hashMap);
    }

    public void unloadRecentHashtags() {
        this.hashtagsLoadedFromDb = false;
    }
}
