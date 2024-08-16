package org.telegram.messenger;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import androidx.collection.LongSparseArray;
import j$.util.function.Consumer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageReplyHeader;
import org.telegram.tgnet.TLRPC$TL_channels_deleteTopicHistory;
import org.telegram.tgnet.TLRPC$TL_channels_editForumTopic;
import org.telegram.tgnet.TLRPC$TL_channels_getForumTopics;
import org.telegram.tgnet.TLRPC$TL_channels_getForumTopicsByID;
import org.telegram.tgnet.TLRPC$TL_channels_reorderPinnedForumTopics;
import org.telegram.tgnet.TLRPC$TL_channels_toggleViewForumAsMessages;
import org.telegram.tgnet.TLRPC$TL_channels_updatePinnedForumTopic;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_forumTopicDeleted;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageActionTopicCreate;
import org.telegram.tgnet.TLRPC$TL_messages_affectedHistory;
import org.telegram.tgnet.TLRPC$TL_messages_forumTopics;
import org.telegram.tgnet.TLRPC$TL_messages_getReplies;
import org.telegram.tgnet.TLRPC$TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC$Updates;
import org.telegram.tgnet.TLRPC$messages_Messages;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.Forum.ForumUtilities;
/* loaded from: classes3.dex */
public class TopicsController extends BaseController {
    public static final int LOAD_TYPE_LOAD_NEXT = 1;
    public static final int LOAD_TYPE_LOAD_UNKNOWN = 2;
    public static final int LOAD_TYPE_PRELOAD = 0;
    private static final int MAX_PRELOAD_COUNT = 20;
    public static final int TOPIC_FLAG_CLOSE = 8;
    public static final int TOPIC_FLAG_HIDE = 32;
    public static final int TOPIC_FLAG_ICON = 2;
    public static final int TOPIC_FLAG_PIN = 4;
    public static final int TOPIC_FLAG_TITLE = 1;
    public static final int TOPIC_FLAG_TOTAL_MESSAGES_COUNT = 16;
    private static final int[] countsTmp = new int[4];
    LongSparseIntArray currentOpenTopicsCounter;
    LongSparseIntArray endIsReached;
    LongSparseArray<TopicsLoadOffset> offsets;
    LongSparseIntArray openedTopicsBuChatId;
    LongSparseArray<ArrayList<TLRPC$TL_forumTopic>> topicsByChatId;
    LongSparseArray<TLRPC$TL_forumTopic> topicsByTopMsgId;
    LongSparseIntArray topicsIsLoading;
    LongSparseArray<LongSparseArray<TLRPC$TL_forumTopic>> topicsMapByChatId;

    /* loaded from: classes3.dex */
    public static class TopicUpdate {
        long dialogId;
        ArrayList<MessageObject> groupedMessages;
        boolean onlyCounters;
        boolean reloadTopic;
        TLRPC$Message topMessage;
        int topMessageId;
        long topicId;
        public int totalMessagesCount = -1;
        int unreadCount;
        int unreadMentions;
    }

    private long messageHash(int i, long j) {
        return j + (i << 12);
    }

    public TopicsController(int i) {
        super(i);
        this.topicsByChatId = new LongSparseArray<>();
        this.topicsMapByChatId = new LongSparseArray<>();
        this.topicsIsLoading = new LongSparseIntArray();
        this.endIsReached = new LongSparseIntArray();
        this.topicsByTopMsgId = new LongSparseArray<>();
        this.currentOpenTopicsCounter = new LongSparseIntArray();
        this.openedTopicsBuChatId = new LongSparseIntArray();
        this.offsets = new LongSparseArray<>();
    }

    public void preloadTopics(long j) {
        loadTopics(j, true, 0);
    }

    public void loadTopics(long j) {
        loadTopics(j, false, 1);
    }

    public void loadTopics(final long j, final boolean z, final int i) {
        if (this.topicsIsLoading.get(j, 0) != 0) {
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("load topics " + j + " fromCache=" + z + " loadType=" + i);
        }
        this.topicsIsLoading.put(j, 1);
        if (z) {
            getMessagesStorage().loadTopics(-j, new Consumer() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda24
                @Override // j$.util.function.Consumer
                public final void accept(Object obj) {
                    TopicsController.this.lambda$loadTopics$1(j, z, i, (ArrayList) obj);
                }

                @Override // j$.util.function.Consumer
                public /* synthetic */ Consumer andThen(Consumer consumer) {
                    return Consumer.-CC.$default$andThen(this, consumer);
                }
            });
            return;
        }
        TLRPC$TL_channels_getForumTopics tLRPC$TL_channels_getForumTopics = new TLRPC$TL_channels_getForumTopics();
        tLRPC$TL_channels_getForumTopics.channel = getMessagesController().getInputChannel(j);
        if (i == 0) {
            tLRPC$TL_channels_getForumTopics.limit = 20;
        } else if (i == 1) {
            tLRPC$TL_channels_getForumTopics.limit = 100;
            TopicsLoadOffset loadOffset = getLoadOffset(j);
            tLRPC$TL_channels_getForumTopics.offset_date = loadOffset.lastMessageDate;
            tLRPC$TL_channels_getForumTopics.offset_id = loadOffset.lastMessageId;
            tLRPC$TL_channels_getForumTopics.offset_topic = loadOffset.lastTopicId;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("offset_date=" + loadOffset.lastMessageDate + " offset_id=" + loadOffset.lastMessageId + " offset_topic=" + loadOffset.lastTopicId);
            }
        }
        getConnectionsManager().sendRequest(tLRPC$TL_channels_getForumTopics, new RequestDelegate() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda25
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                TopicsController.this.lambda$loadTopics$4(j, i, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTopics$1(final long j, final boolean z, final int i, final ArrayList arrayList) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$loadTopics$0(j, arrayList, z, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTopics$0(long j, ArrayList arrayList, boolean z, int i) {
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder sb = new StringBuilder();
            sb.append("loaded from cache ");
            sb.append(j);
            sb.append(" topics_count=");
            sb.append(arrayList == null ? 0 : arrayList.size());
            FileLog.d(sb.toString());
        }
        this.topicsIsLoading.put(j, 0);
        processTopics(j, arrayList, null, z, i, -1);
        sortTopics(j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTopics$4(final long j, final int i, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            final SparseArray sparseArray = new SparseArray();
            final TLRPC$TL_messages_forumTopics tLRPC$TL_messages_forumTopics = (TLRPC$TL_messages_forumTopics) tLObject;
            for (int i2 = 0; i2 < tLRPC$TL_messages_forumTopics.messages.size(); i2++) {
                sparseArray.put(tLRPC$TL_messages_forumTopics.messages.get(i2).id, tLRPC$TL_messages_forumTopics.messages.get(i2));
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TopicsController.this.lambda$loadTopics$2(tLObject, j, tLRPC$TL_messages_forumTopics, sparseArray, i);
                }
            });
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$loadTopics$3(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTopics$2(TLObject tLObject, long j, TLRPC$TL_messages_forumTopics tLRPC$TL_messages_forumTopics, SparseArray sparseArray, int i) {
        TLRPC$TL_messages_forumTopics tLRPC$TL_messages_forumTopics2 = (TLRPC$TL_messages_forumTopics) tLObject;
        getMessagesStorage().putUsersAndChats(tLRPC$TL_messages_forumTopics2.users, tLRPC$TL_messages_forumTopics2.chats, true, true);
        getMessagesController().putUsers(tLRPC$TL_messages_forumTopics2.users, false);
        getMessagesController().putChats(tLRPC$TL_messages_forumTopics2.chats, false);
        this.topicsIsLoading.put(j, 0);
        processTopics(j, tLRPC$TL_messages_forumTopics.topics, sparseArray, false, i, tLRPC$TL_messages_forumTopics2.count);
        sortTopics(j);
        getMessagesStorage().saveTopics(-j, this.topicsByChatId.get(j), true, true, getConnectionsManager().getCurrentTime());
        getMessagesStorage().putMessages(tLRPC$TL_messages_forumTopics.messages, false, true, false, 0, false, 0, 0L);
        if (!tLRPC$TL_messages_forumTopics.topics.isEmpty() && i == 1) {
            ArrayList<TLRPC$TL_forumTopic> arrayList = tLRPC$TL_messages_forumTopics.topics;
            TLRPC$TL_forumTopic tLRPC$TL_forumTopic = arrayList.get(arrayList.size() - 1);
            TLRPC$Message tLRPC$Message = (TLRPC$Message) sparseArray.get(tLRPC$TL_forumTopic.top_message);
            saveLoadOffset(j, tLRPC$TL_forumTopic.top_message, tLRPC$Message == null ? 0 : tLRPC$Message.date, tLRPC$TL_forumTopic.id);
        } else if (getTopics(j) == null || getTopics(j).size() < tLRPC$TL_messages_forumTopics.count) {
            clearLoadingOffset(j);
            loadTopics(j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTopics$3(long j) {
        this.topicsIsLoading.put(j, 0);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.topicsDidLoaded, Long.valueOf(j), Boolean.FALSE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:83:0x01a9, code lost:
        if (r23 == 1) goto L110;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v4, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r4v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void processTopics(final long j, ArrayList<TLRPC$TL_forumTopic> arrayList, SparseArray<TLRPC$Message> sparseArray, boolean z, int i, int i2) {
        ArrayList<Integer> arrayList2;
        ArrayList<TLRPC$TL_forumTopic> arrayList3;
        boolean z2;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic;
        ArrayList<TLRPC$TL_forumTopic> arrayList4 = arrayList;
        SparseArray<TLRPC$Message> sparseArray2 = sparseArray;
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder sb = new StringBuilder();
            sb.append("processTopics=new_topics_size=");
            sb.append(arrayList4 == null ? 0 : arrayList.size());
            sb.append(" fromCache=");
            sb.append(z);
            sb.append(" load_type=");
            sb.append(i);
            sb.append(" totalCount=");
            sb.append(i2);
            FileLog.d(sb.toString());
        }
        ArrayList<TLRPC$TL_forumTopic> arrayList5 = this.topicsByChatId.get(j);
        LongSparseArray<TLRPC$TL_forumTopic> longSparseArray = this.topicsMapByChatId.get(j);
        if (arrayList5 == null) {
            arrayList5 = new ArrayList<>();
            this.topicsByChatId.put(j, arrayList5);
        }
        if (longSparseArray == null) {
            longSparseArray = new LongSparseArray<>();
            this.topicsMapByChatId.put(j, longSparseArray);
        }
        if (arrayList4 != null) {
            int i3 = 0;
            arrayList2 = null;
            arrayList3 = null;
            z2 = false;
            while (i3 < arrayList.size()) {
                TLRPC$TL_forumTopic tLRPC$TL_forumTopic2 = arrayList4.get(i3);
                if (tLRPC$TL_forumTopic2 instanceof TLRPC$TL_forumTopicDeleted) {
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList<>();
                    }
                    arrayList2.add(Integer.valueOf(tLRPC$TL_forumTopic2.id));
                } else {
                    if (!longSparseArray.containsKey(tLRPC$TL_forumTopic2.id)) {
                        if (sparseArray2 != null) {
                            tLRPC$TL_forumTopic2.topMessage = sparseArray2.get(tLRPC$TL_forumTopic2.top_message);
                            tLRPC$TL_forumTopic2.topicStartMessage = sparseArray2.get(tLRPC$TL_forumTopic2.id);
                        }
                        if (tLRPC$TL_forumTopic2.topMessage == null && !tLRPC$TL_forumTopic2.isShort) {
                            if (arrayList3 == null) {
                                arrayList3 = new ArrayList<>();
                            }
                            arrayList3.add(tLRPC$TL_forumTopic2);
                        }
                        if (tLRPC$TL_forumTopic2.topicStartMessage == null) {
                            TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
                            tLRPC$TL_forumTopic2.topicStartMessage = tLRPC$TL_message;
                            tLRPC$TL_message.message = "";
                            tLRPC$TL_message.id = tLRPC$TL_forumTopic2.id;
                            tLRPC$TL_message.peer_id = getMessagesController().getPeer(-j);
                            tLRPC$TL_forumTopic2.topicStartMessage.action = new TLRPC$TL_messageActionTopicCreate();
                            tLRPC$TL_forumTopic2.topicStartMessage.action.title = tLRPC$TL_forumTopic2.title;
                        }
                        arrayList5.add(tLRPC$TL_forumTopic2);
                        longSparseArray.put(tLRPC$TL_forumTopic2.id, tLRPC$TL_forumTopic2);
                        this.topicsByTopMsgId.put(messageHash(tLRPC$TL_forumTopic2.top_message, j), tLRPC$TL_forumTopic2);
                    } else if (!tLRPC$TL_forumTopic2.isShort && (tLRPC$TL_forumTopic = longSparseArray.get(tLRPC$TL_forumTopic2.id)) != null) {
                        boolean z3 = tLRPC$TL_forumTopic.closed;
                        boolean z4 = tLRPC$TL_forumTopic2.closed;
                        if (z3 != z4) {
                            tLRPC$TL_forumTopic.closed = z4;
                            getMessagesStorage().updateTopicData(-j, tLRPC$TL_forumTopic2, 8);
                        }
                    }
                    z2 = true;
                }
                i3++;
                arrayList4 = arrayList;
                sparseArray2 = sparseArray;
            }
        } else {
            arrayList2 = null;
            arrayList3 = null;
            z2 = false;
        }
        int i4 = 0;
        for (int i5 = 0; i5 < arrayList5.size(); i5++) {
            TLRPC$TL_forumTopic tLRPC$TL_forumTopic3 = arrayList5.get(i5);
            if (tLRPC$TL_forumTopic3 != null && tLRPC$TL_forumTopic3.pinned) {
                int i6 = i4 + 1;
                if (tLRPC$TL_forumTopic3.pinnedOrder != i4) {
                    tLRPC$TL_forumTopic3.pinnedOrder = i4;
                    i4 = i6;
                    z2 = true;
                } else {
                    i4 = i6;
                }
            }
        }
        if (arrayList2 != null && i == 2) {
            for (int i7 = 0; i7 < arrayList2.size(); i7++) {
                int i8 = 0;
                while (true) {
                    if (i8 >= arrayList5.size()) {
                        break;
                    } else if (arrayList5.get(i8).id == arrayList2.get(i7).intValue()) {
                        arrayList5.remove(i8);
                        break;
                    } else {
                        i8++;
                    }
                }
            }
            getMessagesStorage().removeTopics(j, arrayList2);
        }
        if (arrayList3 != null && i != 2) {
            reloadTopics(j, arrayList3, null);
        } else {
            ?? r4 = (i != 0 || z) ? 1 : 1;
            if (arrayList5.size() >= i2 && i2 >= 0) {
                this.endIsReached.put(j, r4);
                SharedPreferences.Editor edit = getUserConfig().getPreferences().edit();
                edit.putBoolean("topics_end_reached_" + j, r4).apply();
                z2 = true;
            }
        }
        if (z2) {
            sortTopics(j);
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.topicsDidLoaded, Long.valueOf(j), Boolean.TRUE);
        if ((i == 0 || (i == 0 && !z)) && z && this.topicsByChatId.get(j).isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    TopicsController.this.lambda$processTopics$5(j);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processTopics$5(long j) {
        loadTopics(j, false, 0);
    }

    public ArrayList<TLRPC$TL_forumTopic> getTopics(long j) {
        return this.topicsByChatId.get(j);
    }

    private void sortTopics(long j) {
        sortTopics(j, true);
    }

    public void sortTopics(long j, boolean z) {
        ArrayList<TLRPC$TL_forumTopic> arrayList = this.topicsByChatId.get(j);
        if (arrayList != null) {
            if (this.openedTopicsBuChatId.get(j, 0) > 0) {
                Collections.sort(arrayList, new Comparator() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda18
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int lambda$sortTopics$6;
                        lambda$sortTopics$6 = TopicsController.lambda$sortTopics$6((TLRPC$TL_forumTopic) obj, (TLRPC$TL_forumTopic) obj2);
                        return lambda$sortTopics$6;
                    }
                });
            }
            if (z) {
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.topicsDidLoaded, Long.valueOf(j), Boolean.TRUE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$sortTopics$6(TLRPC$TL_forumTopic tLRPC$TL_forumTopic, TLRPC$TL_forumTopic tLRPC$TL_forumTopic2) {
        boolean z = tLRPC$TL_forumTopic.hidden;
        if (z != tLRPC$TL_forumTopic2.hidden) {
            return z ? -1 : 1;
        }
        boolean z2 = tLRPC$TL_forumTopic.pinned;
        boolean z3 = tLRPC$TL_forumTopic2.pinned;
        if (z2 != z3) {
            return z2 ? -1 : 1;
        } else if (z2 && z3) {
            return tLRPC$TL_forumTopic.pinnedOrder - tLRPC$TL_forumTopic2.pinnedOrder;
        } else {
            TLRPC$Message tLRPC$Message = tLRPC$TL_forumTopic2.topMessage;
            int i = tLRPC$Message != null ? tLRPC$Message.date : 0;
            TLRPC$Message tLRPC$Message2 = tLRPC$TL_forumTopic.topMessage;
            return i - (tLRPC$Message2 != null ? tLRPC$Message2.date : 0);
        }
    }

    public void updateTopicsWithDeletedMessages(final long j, final ArrayList<Integer> arrayList) {
        if (j > 0) {
            return;
        }
        final long j2 = -j;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$updateTopicsWithDeletedMessages$9(j, arrayList, j2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTopicsWithDeletedMessages$9(final long j, final ArrayList arrayList, final long j2) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$updateTopicsWithDeletedMessages$8(j, arrayList, j2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0141  */
    /* JADX WARN: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$updateTopicsWithDeletedMessages$8(long j, ArrayList arrayList, final long j2) {
        final ArrayList<TLRPC$TL_forumTopic> arrayList2;
        NativeByteBuffer byteBufferValue;
        ArrayList<TLRPC$TL_forumTopic> arrayList3 = null;
        try {
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT topic_id, top_message FROM topics WHERE did = %d AND top_message IN (%s)", Long.valueOf(j), TextUtils.join(",", arrayList)), new Object[0]);
            arrayList2 = null;
            while (queryFinalized.next()) {
                try {
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList<>();
                    }
                    TLRPC$TL_forumTopic tLRPC$TL_forumTopic = new TLRPC$TL_forumTopic();
                    tLRPC$TL_forumTopic.id = queryFinalized.intValue(0);
                    tLRPC$TL_forumTopic.top_message = queryFinalized.intValue(1);
                    tLRPC$TL_forumTopic.from_id = getMessagesController().getPeer(getUserConfig().clientUserId);
                    tLRPC$TL_forumTopic.notify_settings = new TLRPC$TL_peerNotifySettings();
                    arrayList2.add(tLRPC$TL_forumTopic);
                } catch (Exception e) {
                    e = e;
                    arrayList3 = arrayList2;
                    e.printStackTrace();
                    arrayList2 = arrayList3;
                    getMessagesStorage().loadGroupedMessagesForTopics(j, arrayList2);
                    if (arrayList2 == null) {
                    }
                }
            }
            queryFinalized.dispose();
            if (arrayList2 != null) {
                for (int i = 0; i < arrayList2.size(); i++) {
                    TLRPC$TL_forumTopic tLRPC$TL_forumTopic2 = arrayList2.get(i);
                    SQLiteCursor queryFinalized2 = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT mid, data FROM messages_topics WHERE uid = %d AND topic_id = %d ORDER BY mid DESC LIMIT 1", Long.valueOf(j), Integer.valueOf(tLRPC$TL_forumTopic2.id)), new Object[0]);
                    if (queryFinalized2.next() && (byteBufferValue = queryFinalized2.byteBufferValue(1)) != null) {
                        TLRPC$Message TLdeserialize = TLRPC$Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                        TLdeserialize.readAttachPath(byteBufferValue, getUserConfig().clientUserId);
                        byteBufferValue.reuse();
                        this.topicsByTopMsgId.remove(messageHash(tLRPC$TL_forumTopic2.top_message, j2));
                        int i2 = TLdeserialize.id;
                        tLRPC$TL_forumTopic2.top_message = i2;
                        tLRPC$TL_forumTopic2.topMessage = TLdeserialize;
                        tLRPC$TL_forumTopic2.groupedMessages = null;
                        this.topicsByTopMsgId.put(messageHash(i2, j2), tLRPC$TL_forumTopic2);
                    }
                    queryFinalized2.dispose();
                }
                for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                    getMessagesStorage().getDatabase().executeFast(String.format(Locale.US, "UPDATE topics SET top_message = %d WHERE did = %d AND topic_id = %d", Integer.valueOf(arrayList2.get(i3).top_message), Long.valueOf(j), Integer.valueOf(arrayList2.get(i3).id))).stepThis().dispose();
                }
            }
        } catch (Exception e2) {
            e = e2;
        }
        getMessagesStorage().loadGroupedMessagesForTopics(j, arrayList2);
        if (arrayList2 == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    TopicsController.this.lambda$updateTopicsWithDeletedMessages$7(arrayList2, j2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTopicsWithDeletedMessages$7(ArrayList arrayList, long j) {
        ArrayList<TLRPC$TL_forumTopic> arrayList2 = null;
        boolean z = false;
        for (int i = 0; i < arrayList.size(); i++) {
            TLRPC$TL_forumTopic tLRPC$TL_forumTopic = (TLRPC$TL_forumTopic) arrayList.get(i);
            LongSparseArray<TLRPC$TL_forumTopic> longSparseArray = this.topicsMapByChatId.get(j);
            if (longSparseArray != null) {
                TLRPC$TL_forumTopic tLRPC$TL_forumTopic2 = longSparseArray.get(tLRPC$TL_forumTopic.id);
                if (tLRPC$TL_forumTopic2 != null && tLRPC$TL_forumTopic.top_message != -1 && tLRPC$TL_forumTopic.topMessage != null) {
                    this.topicsByTopMsgId.remove(messageHash(tLRPC$TL_forumTopic2.top_message, j));
                    TLRPC$Message tLRPC$Message = tLRPC$TL_forumTopic.topMessage;
                    int i2 = tLRPC$Message.id;
                    tLRPC$TL_forumTopic2.top_message = i2;
                    tLRPC$TL_forumTopic2.topMessage = tLRPC$Message;
                    tLRPC$TL_forumTopic2.groupedMessages = tLRPC$TL_forumTopic.groupedMessages;
                    this.topicsByTopMsgId.put(messageHash(i2, j), tLRPC$TL_forumTopic2);
                    z = true;
                } else if (tLRPC$TL_forumTopic.top_message == -1 || tLRPC$TL_forumTopic.topMessage == null) {
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList<>();
                    }
                    arrayList2.add(tLRPC$TL_forumTopic);
                }
            }
        }
        if (z) {
            sortTopics(j);
        }
        if (arrayList2 != null) {
            reloadTopics(j, arrayList2, null);
        }
    }

    public void reloadTopics(final long j, ArrayList<TLRPC$TL_forumTopic> arrayList, final Runnable runnable) {
        TLRPC$TL_channels_getForumTopicsByID tLRPC$TL_channels_getForumTopicsByID = new TLRPC$TL_channels_getForumTopicsByID();
        for (int i = 0; i < arrayList.size(); i++) {
            tLRPC$TL_channels_getForumTopicsByID.topics.add(Integer.valueOf(arrayList.get(i).id));
        }
        tLRPC$TL_channels_getForumTopicsByID.channel = getMessagesController().getInputChannel(j);
        getConnectionsManager().sendRequest(tLRPC$TL_channels_getForumTopicsByID, new RequestDelegate() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda10
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                TopicsController.this.lambda$reloadTopics$12(j, runnable, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reloadTopics$12(final long j, final Runnable runnable, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$reloadTopics$11(tLObject, j, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reloadTopics$11(final TLObject tLObject, final long j, final Runnable runnable) {
        if (tLObject != null) {
            final SparseArray sparseArray = new SparseArray();
            final TLRPC$TL_messages_forumTopics tLRPC$TL_messages_forumTopics = (TLRPC$TL_messages_forumTopics) tLObject;
            for (int i = 0; i < tLRPC$TL_messages_forumTopics.messages.size(); i++) {
                sparseArray.put(tLRPC$TL_messages_forumTopics.messages.get(i).id, tLRPC$TL_messages_forumTopics.messages.get(i));
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda26
                @Override // java.lang.Runnable
                public final void run() {
                    TopicsController.this.lambda$reloadTopics$10(tLObject, j, tLRPC$TL_messages_forumTopics, sparseArray, runnable);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reloadTopics$10(TLObject tLObject, long j, TLRPC$TL_messages_forumTopics tLRPC$TL_messages_forumTopics, SparseArray sparseArray, Runnable runnable) {
        TLRPC$TL_messages_forumTopics tLRPC$TL_messages_forumTopics2 = (TLRPC$TL_messages_forumTopics) tLObject;
        getMessagesController().putUsers(tLRPC$TL_messages_forumTopics2.users, false);
        getMessagesController().putChats(tLRPC$TL_messages_forumTopics2.chats, false);
        processTopics(j, tLRPC$TL_messages_forumTopics.topics, sparseArray, false, 2, -1);
        getMessagesStorage().saveTopics(-j, this.topicsByChatId.get(j), true, true, getConnectionsManager().getCurrentTime());
        getMessagesStorage().putMessages(tLRPC$TL_messages_forumTopics.messages, false, true, false, 0, false, 0, 0L);
        if (runnable != null) {
            runnable.run();
        }
    }

    public void updateMaxReadId(long j, long j2, int i, int i2, int i3) {
        TLRPC$TL_forumTopic findTopic = findTopic(j, j2);
        if (findTopic != null) {
            findTopic.read_inbox_max_id = i;
            findTopic.unread_count = i2;
            if (i3 >= 0) {
                findTopic.unread_mentions_count = i3;
            }
            sortTopics(j);
        }
    }

    public TLRPC$TL_forumTopic findTopic(long j, long j2) {
        LongSparseArray<TLRPC$TL_forumTopic> longSparseArray = this.topicsMapByChatId.get(j);
        if (longSparseArray != null) {
            return longSparseArray.get(j2);
        }
        return null;
    }

    public String getTopicName(TLRPC$Chat tLRPC$Chat, MessageObject messageObject) {
        TLRPC$TL_forumTopic findTopic;
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = messageObject.messageOwner.reply_to;
        if (tLRPC$MessageReplyHeader == null) {
            return null;
        }
        int i = tLRPC$MessageReplyHeader.reply_to_top_id;
        if (i == 0) {
            i = tLRPC$MessageReplyHeader.reply_to_msg_id;
        }
        return (i == 0 || (findTopic = findTopic(tLRPC$Chat.id, (long) i)) == null) ? "" : findTopic.title;
    }

    public CharSequence getTopicIconName(TLRPC$Chat tLRPC$Chat, MessageObject messageObject, TextPaint textPaint) {
        return getTopicIconName(tLRPC$Chat, messageObject, textPaint, null);
    }

    public CharSequence getTopicIconName(TLRPC$Chat tLRPC$Chat, MessageObject messageObject, TextPaint textPaint, Drawable[] drawableArr) {
        TLRPC$TL_forumTopic findTopic;
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = messageObject.messageOwner.reply_to;
        if (tLRPC$MessageReplyHeader == null) {
            return null;
        }
        int i = tLRPC$MessageReplyHeader.reply_to_top_id;
        if (i == 0) {
            i = tLRPC$MessageReplyHeader.reply_to_msg_id;
        }
        if (i == 0 || (findTopic = findTopic(tLRPC$Chat.id, i)) == null) {
            return null;
        }
        return ForumUtilities.getTopicSpannedName(findTopic, textPaint, drawableArr, false);
    }

    public int[] getForumUnreadCount(long j) {
        ArrayList<TLRPC$TL_forumTopic> arrayList = this.topicsByChatId.get(j);
        Arrays.fill(countsTmp, 0);
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC$TL_forumTopic tLRPC$TL_forumTopic = arrayList.get(i);
                int[] iArr = countsTmp;
                iArr[0] = iArr[0] + (tLRPC$TL_forumTopic.unread_count > 0 ? 1 : 0);
                iArr[1] = iArr[1] + (tLRPC$TL_forumTopic.unread_mentions_count > 0 ? 1 : 0);
                iArr[2] = iArr[2] + (tLRPC$TL_forumTopic.unread_reactions_count <= 0 ? 0 : 1);
                if (!getMessagesController().isDialogMuted(-j, tLRPC$TL_forumTopic.id)) {
                    iArr[3] = iArr[3] + tLRPC$TL_forumTopic.unread_count;
                }
            }
        }
        return countsTmp;
    }

    public void onTopicCreated(long j, TLRPC$TL_forumTopic tLRPC$TL_forumTopic, boolean z) {
        long j2 = -j;
        LongSparseArray<TLRPC$TL_forumTopic> longSparseArray = this.topicsMapByChatId.get(j2);
        if (findTopic(j2, tLRPC$TL_forumTopic.id) != null) {
            return;
        }
        if (longSparseArray == null) {
            longSparseArray = new LongSparseArray<>();
            this.topicsMapByChatId.put(j2, longSparseArray);
        }
        ArrayList<TLRPC$TL_forumTopic> arrayList = this.topicsByChatId.get(j2);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.topicsByChatId.put(j2, arrayList);
        }
        longSparseArray.put(tLRPC$TL_forumTopic.id, tLRPC$TL_forumTopic);
        arrayList.add(tLRPC$TL_forumTopic);
        if (z) {
            getMessagesStorage().saveTopics(j, Collections.singletonList(tLRPC$TL_forumTopic), false, true, getConnectionsManager().getCurrentTime());
        }
        sortTopics(j2, true);
    }

    public void onTopicEdited(long j, TLRPC$TL_forumTopic tLRPC$TL_forumTopic) {
        getMessagesStorage().updateTopicData(j, tLRPC$TL_forumTopic, 35);
        sortTopics(-j);
    }

    public void deleteTopics(long j, ArrayList<Integer> arrayList) {
        ArrayList<TLRPC$TL_forumTopic> arrayList2 = this.topicsByChatId.get(j);
        LongSparseArray<TLRPC$TL_forumTopic> longSparseArray = this.topicsMapByChatId.get(j);
        if (longSparseArray != null && arrayList2 != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                long intValue = arrayList.get(i).intValue();
                TLRPC$TL_forumTopic tLRPC$TL_forumTopic = longSparseArray.get(intValue);
                longSparseArray.remove(intValue);
                if (tLRPC$TL_forumTopic != null) {
                    this.topicsByTopMsgId.remove(messageHash(tLRPC$TL_forumTopic.top_message, j));
                    arrayList2.remove(tLRPC$TL_forumTopic);
                }
            }
            sortTopics(j);
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            deleteTopic(j, arrayList.get(i2).intValue(), 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteTopic(final long j, final int i, int i2) {
        TLRPC$TL_channels_deleteTopicHistory tLRPC$TL_channels_deleteTopicHistory = new TLRPC$TL_channels_deleteTopicHistory();
        tLRPC$TL_channels_deleteTopicHistory.channel = getMessagesController().getInputChannel(j);
        tLRPC$TL_channels_deleteTopicHistory.top_msg_id = i;
        if (i2 == 0) {
            getMessagesStorage().removeTopic(-j, i);
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_deleteTopicHistory, new RequestDelegate() { // from class: org.telegram.messenger.TopicsController.1
            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                if (tLRPC$TL_error == null) {
                    TLRPC$TL_messages_affectedHistory tLRPC$TL_messages_affectedHistory = (TLRPC$TL_messages_affectedHistory) tLObject;
                    TopicsController.this.getMessagesController().processNewChannelDifferenceParams(tLRPC$TL_messages_affectedHistory.pts, tLRPC$TL_messages_affectedHistory.pts_count, j);
                    int i3 = tLRPC$TL_messages_affectedHistory.offset;
                    if (i3 > 0) {
                        TopicsController.this.deleteTopic(j, i, i3);
                    }
                }
            }
        });
    }

    public void toggleCloseTopic(long j, int i, boolean z) {
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic;
        TLRPC$TL_channels_editForumTopic tLRPC$TL_channels_editForumTopic = new TLRPC$TL_channels_editForumTopic();
        tLRPC$TL_channels_editForumTopic.channel = getMessagesController().getInputChannel(j);
        tLRPC$TL_channels_editForumTopic.topic_id = i;
        tLRPC$TL_channels_editForumTopic.flags |= 4;
        tLRPC$TL_channels_editForumTopic.closed = z;
        LongSparseArray<TLRPC$TL_forumTopic> longSparseArray = this.topicsMapByChatId.get(j);
        if (longSparseArray != null && (tLRPC$TL_forumTopic = longSparseArray.get(i)) != null) {
            tLRPC$TL_forumTopic.closed = z;
            getMessagesStorage().updateTopicData(-j, tLRPC$TL_forumTopic, 8);
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_editForumTopic, new RequestDelegate() { // from class: org.telegram.messenger.TopicsController.2
            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            }
        });
    }

    public ArrayList<Integer> getCurrentPinnedOrder(long j) {
        ArrayList<TLRPC$TL_forumTopic> topics = getTopics(j);
        ArrayList<Integer> arrayList = new ArrayList<>();
        if (topics != null) {
            for (int i = 0; i < topics.size(); i++) {
                TLRPC$TL_forumTopic tLRPC$TL_forumTopic = topics.get(i);
                if (tLRPC$TL_forumTopic != null && tLRPC$TL_forumTopic.pinned) {
                    arrayList.add(Integer.valueOf(tLRPC$TL_forumTopic.id));
                }
            }
        }
        return arrayList;
    }

    public void applyPinnedOrder(long j, ArrayList<Integer> arrayList) {
        applyPinnedOrder(j, arrayList, true);
    }

    public void applyPinnedOrder(long j, ArrayList<Integer> arrayList, boolean z) {
        if (arrayList == null) {
            return;
        }
        ArrayList<TLRPC$TL_forumTopic> topics = getTopics(j);
        boolean z2 = true;
        if (topics != null) {
            boolean z3 = false;
            for (int i = 0; i < topics.size(); i++) {
                TLRPC$TL_forumTopic tLRPC$TL_forumTopic = topics.get(i);
                if (tLRPC$TL_forumTopic != null) {
                    int indexOf = arrayList.indexOf(Integer.valueOf(tLRPC$TL_forumTopic.id));
                    boolean z4 = indexOf >= 0;
                    if (tLRPC$TL_forumTopic.pinned != z4 || (z4 && tLRPC$TL_forumTopic.pinnedOrder != indexOf)) {
                        tLRPC$TL_forumTopic.pinned = z4;
                        tLRPC$TL_forumTopic.pinnedOrder = indexOf;
                        getMessagesStorage().updateTopicData(j, tLRPC$TL_forumTopic, 4);
                        z3 = true;
                    }
                }
            }
            z2 = z3;
        }
        if (z && z2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    TopicsController.this.lambda$applyPinnedOrder$13();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyPinnedOrder$13() {
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_SELECT_DIALOG));
    }

    public void toggleShowTopic(long j, int i, boolean z) {
        TLRPC$TL_channels_editForumTopic tLRPC$TL_channels_editForumTopic = new TLRPC$TL_channels_editForumTopic();
        tLRPC$TL_channels_editForumTopic.channel = getMessagesController().getInputChannel(j);
        tLRPC$TL_channels_editForumTopic.topic_id = i;
        tLRPC$TL_channels_editForumTopic.flags = 8;
        tLRPC$TL_channels_editForumTopic.hidden = !z;
        TLRPC$TL_forumTopic findTopic = findTopic(j, i);
        if (findTopic != null) {
            boolean z2 = tLRPC$TL_channels_editForumTopic.hidden;
            findTopic.hidden = z2;
            if (z2) {
                findTopic.closed = true;
            }
            long j2 = -j;
            updateTopicInUi(j2, findTopic, 44);
            getMessagesStorage().updateTopicData(j2, findTopic, 44);
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_editForumTopic, null);
    }

    public void toggleViewForumAsMessages(long j, boolean z) {
        TLRPC$TL_channels_toggleViewForumAsMessages tLRPC$TL_channels_toggleViewForumAsMessages = new TLRPC$TL_channels_toggleViewForumAsMessages();
        tLRPC$TL_channels_toggleViewForumAsMessages.channel_id = getMessagesController().getInputChannel(j);
        tLRPC$TL_channels_toggleViewForumAsMessages.enabled = z;
        getConnectionsManager().sendRequest(tLRPC$TL_channels_toggleViewForumAsMessages, new RequestDelegate() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda11
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                TopicsController.this.lambda$toggleViewForumAsMessages$14(tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleViewForumAsMessages$14(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            getMessagesController().processUpdates((TLRPC$Updates) tLObject, false);
        }
    }

    public void pinTopic(final long j, int i, boolean z, final BaseFragment baseFragment) {
        TLRPC$TL_channels_updatePinnedForumTopic tLRPC$TL_channels_updatePinnedForumTopic = new TLRPC$TL_channels_updatePinnedForumTopic();
        tLRPC$TL_channels_updatePinnedForumTopic.channel = getMessagesController().getInputChannel(j);
        tLRPC$TL_channels_updatePinnedForumTopic.topic_id = i;
        tLRPC$TL_channels_updatePinnedForumTopic.pinned = z;
        final ArrayList<Integer> currentPinnedOrder = getCurrentPinnedOrder(j);
        ArrayList<Integer> arrayList = new ArrayList<>(currentPinnedOrder);
        arrayList.remove(Integer.valueOf(i));
        if (z) {
            arrayList.add(0, Integer.valueOf(i));
        }
        applyPinnedOrder(j, arrayList);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_updatePinnedForumTopic, new RequestDelegate() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda21
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                TopicsController.this.lambda$pinTopic$16(baseFragment, j, currentPinnedOrder, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pinTopic$16(final BaseFragment baseFragment, long j, ArrayList arrayList, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error != null) {
            if (!"PINNED_TOO_MUCH".equals(tLRPC$TL_error.text)) {
                if ("PINNED_TOPIC_NOT_MODIFIED".equals(tLRPC$TL_error.text)) {
                    reloadTopics(j, false);
                }
            } else if (baseFragment == null) {
            } else {
                applyPinnedOrder(j, arrayList);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        TopicsController.this.lambda$pinTopic$15(baseFragment);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pinTopic$15(BaseFragment baseFragment) {
        baseFragment.showDialog(new AlertDialog.Builder(baseFragment.getContext()).setTitle(LocaleController.getString("LimitReached", R.string.LimitReached)).setMessage(LocaleController.formatString("LimitReachedPinnedTopics", R.string.LimitReachedPinnedTopics, Integer.valueOf(MessagesController.getInstance(this.currentAccount).topicsPinnedLimit))).setPositiveButton(LocaleController.getString("OK", R.string.OK), null).create());
    }

    public void reorderPinnedTopics(long j, ArrayList<Integer> arrayList) {
        TLRPC$TL_channels_reorderPinnedForumTopics tLRPC$TL_channels_reorderPinnedForumTopics = new TLRPC$TL_channels_reorderPinnedForumTopics();
        tLRPC$TL_channels_reorderPinnedForumTopics.channel = getMessagesController().getInputChannel(j);
        if (arrayList != null) {
            tLRPC$TL_channels_reorderPinnedForumTopics.order.addAll(arrayList);
        }
        tLRPC$TL_channels_reorderPinnedForumTopics.force = true;
        applyPinnedOrder(j, arrayList, false);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_reorderPinnedForumTopics, null);
    }

    public void updateMentionsUnread(final long j, final long j2, final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$updateMentionsUnread$17(j, j2, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateMentionsUnread$17(long j, long j2, int i) {
        long j3 = -j;
        TLRPC$TL_forumTopic findTopic = findTopic(j3, j2);
        if (findTopic != null) {
            findTopic.unread_mentions_count = i;
            sortTopics(j3, true);
        }
    }

    public int updateReactionsUnread(long j, long j2, int i, boolean z) {
        long j3 = -j;
        TLRPC$TL_forumTopic findTopic = findTopic(j3, j2);
        if (findTopic != null) {
            if (z) {
                int i2 = findTopic.unread_reactions_count + i;
                findTopic.unread_reactions_count = i2;
                if (i2 < 0) {
                    findTopic.unread_reactions_count = 0;
                }
            } else {
                findTopic.unread_reactions_count = i;
            }
            int i3 = findTopic.unread_reactions_count;
            sortTopics(j3, true);
            return i3;
        }
        return -1;
    }

    public void markAllReactionsAsRead(long j, long j2) {
        TLRPC$TL_forumTopic findTopic = findTopic(j, j2);
        if (findTopic == null || findTopic.unread_reactions_count <= 0) {
            return;
        }
        findTopic.unread_reactions_count = 0;
        sortTopics(j);
    }

    public TopicsLoadOffset getLoadOffset(long j) {
        TopicsLoadOffset topicsLoadOffset = this.offsets.get(j);
        return topicsLoadOffset != null ? topicsLoadOffset : new TopicsLoadOffset();
    }

    public void saveLoadOffset(long j, int i, int i2, int i3) {
        TopicsLoadOffset topicsLoadOffset = new TopicsLoadOffset();
        topicsLoadOffset.lastMessageId = i;
        topicsLoadOffset.lastMessageDate = i2;
        topicsLoadOffset.lastTopicId = i3;
        this.offsets.put(j, topicsLoadOffset);
    }

    public void clearLoadingOffset(long j) {
        this.offsets.remove(j);
    }

    public boolean endIsReached(long j) {
        return this.endIsReached.get(j, 0) == 1;
    }

    public void processUpdate(final List<TopicUpdate> list) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$processUpdate$18(list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processUpdate$18(List list) {
        HashSet hashSet = new HashSet();
        LongSparseArray longSparseArray = null;
        for (int i = 0; i < list.size(); i++) {
            TopicUpdate topicUpdate = (TopicUpdate) list.get(i);
            if (topicUpdate.reloadTopic) {
                if (longSparseArray == null) {
                    longSparseArray = new LongSparseArray();
                }
                ArrayList arrayList = (ArrayList) longSparseArray.get(topicUpdate.dialogId);
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    longSparseArray.put(topicUpdate.dialogId, arrayList);
                }
                TLRPC$TL_forumTopic tLRPC$TL_forumTopic = new TLRPC$TL_forumTopic();
                tLRPC$TL_forumTopic.id = (int) topicUpdate.topicId;
                arrayList.add(tLRPC$TL_forumTopic);
            } else {
                TLRPC$TL_forumTopic findTopic = findTopic(-topicUpdate.dialogId, topicUpdate.topicId);
                if (findTopic != null) {
                    if (topicUpdate.onlyCounters) {
                        int i2 = topicUpdate.unreadCount;
                        if (i2 >= 0) {
                            findTopic.unread_count = i2;
                        }
                        int i3 = topicUpdate.unreadMentions;
                        if (i3 >= 0) {
                            findTopic.unread_mentions_count = i3;
                        }
                    } else {
                        this.topicsByTopMsgId.remove(messageHash(findTopic.top_message, -topicUpdate.dialogId));
                        findTopic.topMessage = topicUpdate.topMessage;
                        findTopic.groupedMessages = topicUpdate.groupedMessages;
                        int i4 = topicUpdate.topMessageId;
                        findTopic.top_message = i4;
                        findTopic.unread_count = topicUpdate.unreadCount;
                        findTopic.unread_mentions_count = topicUpdate.unreadMentions;
                        this.topicsByTopMsgId.put(messageHash(i4, -topicUpdate.dialogId), findTopic);
                    }
                    int i5 = topicUpdate.totalMessagesCount;
                    if (i5 > 0) {
                        findTopic.totalMessagesCount = i5;
                    }
                    hashSet.add(Long.valueOf(-topicUpdate.dialogId));
                }
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            sortTopics(((Long) it.next()).longValue(), true);
        }
        if (longSparseArray != null) {
            for (int i6 = 0; i6 < longSparseArray.size(); i6++) {
                reloadTopics(-longSparseArray.keyAt(i6), (ArrayList) longSparseArray.valueAt(i6), null);
            }
        }
    }

    public boolean isLoading(long j) {
        if (this.topicsIsLoading.get(j, 0) == 1) {
            return this.topicsByChatId.get(j) == null || this.topicsByChatId.get(j).isEmpty();
        }
        return false;
    }

    public void onTopicsDeletedServerSide(final ArrayList<MessagesStorage.TopicKey> arrayList) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$onTopicsDeletedServerSide$19(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTopicsDeletedServerSide$19(ArrayList arrayList) {
        HashSet hashSet = new HashSet();
        for (int i = 0; i < arrayList.size(); i++) {
            MessagesStorage.TopicKey topicKey = (MessagesStorage.TopicKey) arrayList.get(i);
            long j = -topicKey.dialogId;
            LongSparseArray<TLRPC$TL_forumTopic> longSparseArray = this.topicsMapByChatId.get(j);
            if (longSparseArray != null) {
                longSparseArray.remove(topicKey.topicId);
            }
            ArrayList<TLRPC$TL_forumTopic> arrayList2 = this.topicsByChatId.get(j);
            if (arrayList2 != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= arrayList2.size()) {
                        break;
                    } else if (arrayList2.get(i2).id == topicKey.topicId) {
                        arrayList2.remove(i2);
                        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogDeleted, Long.valueOf(-j), Long.valueOf(topicKey.topicId));
                        hashSet.add(Long.valueOf(j));
                        break;
                    } else {
                        i2++;
                    }
                }
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            sortTopics(((Long) it.next()).longValue(), true);
        }
    }

    public void reloadTopics(long j) {
        reloadTopics(j, true);
    }

    public void reloadTopics(final long j, final boolean z) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$reloadTopics$20(j, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reloadTopics$20(long j, boolean z) {
        SharedPreferences.Editor edit = getUserConfig().getPreferences().edit();
        edit.remove("topics_end_reached_" + j).apply();
        this.topicsByChatId.remove(j);
        this.topicsMapByChatId.remove(j);
        this.endIsReached.delete(j);
        clearLoadingOffset(j);
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(j));
        if (chat != null && chat.forum) {
            loadTopics(j, z, 0);
        }
        sortTopics(j);
    }

    public void databaseCleared() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$databaseCleared$21();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$databaseCleared$21() {
        this.topicsByChatId.clear();
        this.topicsMapByChatId.clear();
        this.endIsReached.clear();
        SharedPreferences.Editor edit = getUserConfig().getPreferences().edit();
        for (String str : getUserConfig().getPreferences().getAll().keySet()) {
            if (str.startsWith("topics_load_offset_message_id_")) {
                edit.remove(str);
            }
            if (str.startsWith("topics_load_offset_date_")) {
                edit.remove(str);
            }
            if (str.startsWith("topics_load_offset_topic_id_")) {
                edit.remove(str);
            }
            if (str.startsWith("topics_end_reached_")) {
                edit.remove(str);
            }
        }
        edit.apply();
    }

    public void updateReadOutbox(final HashMap<MessagesStorage.TopicKey, Integer> hashMap) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$updateReadOutbox$22(hashMap);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateReadOutbox$22(HashMap hashMap) {
        HashSet hashSet = new HashSet();
        for (MessagesStorage.TopicKey topicKey : hashMap.keySet()) {
            int intValue = ((Integer) hashMap.get(topicKey)).intValue();
            TLRPC$TL_forumTopic findTopic = findTopic(-topicKey.dialogId, topicKey.topicId);
            if (findTopic != null) {
                findTopic.read_outbox_max_id = Math.max(findTopic.read_outbox_max_id, intValue);
                hashSet.add(Long.valueOf(-topicKey.dialogId));
                TLRPC$Message tLRPC$Message = findTopic.topMessage;
                if (tLRPC$Message != null && findTopic.read_outbox_max_id >= tLRPC$Message.id) {
                    tLRPC$Message.unread = false;
                }
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.topicsDidLoaded, (Long) it.next(), Boolean.TRUE);
        }
    }

    public void updateTopicInUi(long j, TLRPC$TL_forumTopic tLRPC$TL_forumTopic, int i) {
        long j2 = -j;
        TLRPC$TL_forumTopic findTopic = findTopic(j2, tLRPC$TL_forumTopic.id);
        if (findTopic != null) {
            if ((i & 1) != 0) {
                findTopic.title = tLRPC$TL_forumTopic.title;
            }
            if ((i & 2) != 0) {
                findTopic.icon_emoji_id = tLRPC$TL_forumTopic.icon_emoji_id;
            }
            if ((i & 8) != 0) {
                findTopic.closed = tLRPC$TL_forumTopic.closed;
            }
            if ((i & 4) != 0) {
                findTopic.pinned = tLRPC$TL_forumTopic.pinned;
            }
            if ((i & 32) != 0) {
                findTopic.hidden = tLRPC$TL_forumTopic.hidden;
            }
            sortTopics(j2);
        }
    }

    public void processEditedMessages(LongSparseArray<ArrayList<MessageObject>> longSparseArray) {
        HashSet hashSet = new HashSet();
        for (int i = 0; i < longSparseArray.size(); i++) {
            ArrayList<MessageObject> valueAt = longSparseArray.valueAt(i);
            for (int i2 = 0; i2 < valueAt.size(); i2++) {
                TLRPC$TL_forumTopic tLRPC$TL_forumTopic = this.topicsByTopMsgId.get(messageHash(valueAt.get(i2).getId(), -valueAt.get(i2).getDialogId()));
                if (tLRPC$TL_forumTopic != null) {
                    tLRPC$TL_forumTopic.topMessage = valueAt.get(i2).messageOwner;
                    hashSet.add(Long.valueOf(-valueAt.get(i2).getDialogId()));
                }
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            sortTopics(((Long) it.next()).longValue(), true);
        }
    }

    public void processEditedMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic = this.topicsByTopMsgId.get(messageHash(tLRPC$Message.id, -tLRPC$Message.dialog_id));
        if (tLRPC$TL_forumTopic != null) {
            tLRPC$TL_forumTopic.topMessage = tLRPC$Message;
            sortTopics(-tLRPC$Message.dialog_id, true);
        }
    }

    public void loadTopic(final long j, final long j2, final Runnable runnable) {
        getMessagesStorage().loadTopics(-j, new Consumer() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda3
            @Override // j$.util.function.Consumer
            public final void accept(Object obj) {
                TopicsController.this.lambda$loadTopic$24(j, j2, runnable, (ArrayList) obj);
            }

            @Override // j$.util.function.Consumer
            public /* synthetic */ Consumer andThen(Consumer consumer) {
                return Consumer.-CC.$default$andThen(this, consumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTopic$24(final long j, final long j2, final Runnable runnable, final ArrayList arrayList) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$loadTopic$23(j, arrayList, j2, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTopic$23(long j, ArrayList arrayList, long j2, Runnable runnable) {
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder sb = new StringBuilder();
            sb.append("loaded from cache ");
            sb.append(j);
            sb.append(" topics_count=");
            sb.append(arrayList == null ? 0 : arrayList.size());
            FileLog.d(sb.toString());
        }
        processTopics(j, arrayList, null, true, 0, -1);
        sortTopics(j);
        if (findTopic(j, j2) != null) {
            runnable.run();
            return;
        }
        ArrayList<TLRPC$TL_forumTopic> arrayList2 = new ArrayList<>();
        new TLRPC$TL_forumTopic().id = (int) j2;
        reloadTopics(j, arrayList2, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class TopicsLoadOffset {
        int lastMessageDate;
        int lastMessageId;
        int lastTopicId;

        private TopicsLoadOffset() {
        }
    }

    public void onTopicFragmentResume(long j) {
        this.openedTopicsBuChatId.put(j, this.openedTopicsBuChatId.get(j, 0) + 1);
        sortTopics(j);
    }

    public void onTopicFragmentPause(long j) {
        int i = this.openedTopicsBuChatId.get(j, 0) - 1;
        this.openedTopicsBuChatId.put(j, i >= 0 ? i : 0);
    }

    public void getTopicRepliesCount(final long j, long j2) {
        final TLRPC$TL_forumTopic findTopic = findTopic(-j, j2);
        if (findTopic == null || findTopic.totalMessagesCount != 0) {
            return;
        }
        TLRPC$TL_messages_getReplies tLRPC$TL_messages_getReplies = new TLRPC$TL_messages_getReplies();
        tLRPC$TL_messages_getReplies.peer = getMessagesController().getInputPeer(j);
        tLRPC$TL_messages_getReplies.msg_id = (int) j2;
        tLRPC$TL_messages_getReplies.limit = 1;
        getConnectionsManager().sendRequest(tLRPC$TL_messages_getReplies, new RequestDelegate() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda7
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                TopicsController.this.lambda$getTopicRepliesCount$26(findTopic, j, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getTopicRepliesCount$26(final TLRPC$TL_forumTopic tLRPC$TL_forumTopic, final long j, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TopicsController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                TopicsController.this.lambda$getTopicRepliesCount$25(tLObject, tLRPC$TL_forumTopic, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getTopicRepliesCount$25(TLObject tLObject, TLRPC$TL_forumTopic tLRPC$TL_forumTopic, long j) {
        if (tLObject != null) {
            tLRPC$TL_forumTopic.totalMessagesCount = ((TLRPC$messages_Messages) tLObject).count;
            getMessagesStorage().updateTopicData(j, tLRPC$TL_forumTopic, 16);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.topicsDidLoaded, Long.valueOf(-j), Boolean.TRUE);
        }
    }
}
