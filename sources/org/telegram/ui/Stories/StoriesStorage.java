package org.telegram.ui.Stories;

import android.text.TextUtils;
import androidx.collection.LongSparseArray;
import com.google.android.exoplayer2.util.Consumer;
import j$.util.Comparator$-CC;
import j$.util.function.ToIntFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLiteException;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.Timer;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$MessageReplyHeader;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messageReplyStoryHeader;
import org.telegram.tgnet.TLRPC$TL_webPageAttributeStory;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.tgnet.TLRPC$WebPageAttribute;
import org.telegram.tgnet.tl.TL_stories$PeerStories;
import org.telegram.tgnet.tl.TL_stories$StoryFwdHeader;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.tgnet.tl.TL_stories$TL_mediaAreaChannelPost;
import org.telegram.tgnet.tl.TL_stories$TL_peerStories;
import org.telegram.tgnet.tl.TL_stories$TL_stories_allStories;
import org.telegram.tgnet.tl.TL_stories$TL_stories_getStoriesByID;
import org.telegram.tgnet.tl.TL_stories$TL_stories_stories;
import org.telegram.tgnet.tl.TL_stories$TL_storyItem;
import org.telegram.tgnet.tl.TL_stories$TL_storyItemDeleted;
import org.telegram.tgnet.tl.TL_stories$TL_storyItemSkipped;
import org.telegram.tgnet.tl.TL_stories$TL_updateStory;
/* loaded from: classes4.dex */
public class StoriesStorage {
    int currentAccount;
    MessagesStorage storage;

    public StoriesStorage(int i) {
        this.currentAccount = i;
        this.storage = MessagesStorage.getInstance(i);
    }

    public static void applyStory(int i, long j, MessageObject messageObject, TL_stories$StoryItem tL_stories$StoryItem) {
        TLRPC$WebPage tLRPC$WebPage;
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = tLRPC$Message.reply_to;
        if ((tLRPC$MessageReplyHeader instanceof TLRPC$TL_messageReplyStoryHeader) && tLRPC$MessageReplyHeader.story_id == tL_stories$StoryItem.id) {
            tLRPC$Message.replyStory = checkExpiredStateLocal(i, j, tL_stories$StoryItem);
        }
        int i2 = messageObject.type;
        if (i2 == 23 || i2 == 24) {
            MessageMediaStoryFull messageMediaStoryFull = new MessageMediaStoryFull();
            messageMediaStoryFull.user_id = DialogObject.getPeerDialogId(messageObject.messageOwner.media.peer);
            TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
            messageMediaStoryFull.peer = tLRPC$MessageMedia.peer;
            messageMediaStoryFull.id = tLRPC$MessageMedia.id;
            messageMediaStoryFull.storyItem = checkExpiredStateLocal(i, j, tL_stories$StoryItem);
            TLRPC$Message tLRPC$Message2 = messageObject.messageOwner;
            messageMediaStoryFull.via_mention = tLRPC$Message2.media.via_mention;
            tLRPC$Message2.media = messageMediaStoryFull;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject.messageOwner.media;
        if (tLRPC$MessageMedia2 == null || (tLRPC$WebPage = tLRPC$MessageMedia2.webpage) == null || tLRPC$WebPage.attributes == null) {
            return;
        }
        for (int i3 = 0; i3 < messageObject.messageOwner.media.webpage.attributes.size(); i3++) {
            TLRPC$WebPageAttribute tLRPC$WebPageAttribute = (TLRPC$WebPageAttribute) messageObject.messageOwner.media.webpage.attributes.get(i3);
            if (tLRPC$WebPageAttribute instanceof TLRPC$TL_webPageAttributeStory) {
                TLRPC$TL_webPageAttributeStory tLRPC$TL_webPageAttributeStory = (TLRPC$TL_webPageAttributeStory) tLRPC$WebPageAttribute;
                if (tLRPC$TL_webPageAttributeStory.id == tL_stories$StoryItem.id) {
                    tLRPC$WebPageAttribute.flags |= 1;
                    tLRPC$TL_webPageAttributeStory.storyItem = checkExpiredStateLocal(i, j, tL_stories$StoryItem);
                }
            }
        }
    }

    public static TL_stories$StoryItem checkExpiredStateLocal(int i, long j, TL_stories$StoryItem tL_stories$StoryItem) {
        if (tL_stories$StoryItem instanceof TL_stories$TL_storyItemDeleted) {
            return tL_stories$StoryItem;
        }
        int currentTime = ConnectionsManager.getInstance(i).getCurrentTime();
        int i2 = tL_stories$StoryItem.expire_date;
        boolean z = i2 <= 0 ? currentTime - tL_stories$StoryItem.date > 86400 : currentTime > i2;
        if (tL_stories$StoryItem.pinned || !z || j == 0 || j == UserConfig.getInstance(i).clientUserId) {
            return tL_stories$StoryItem;
        }
        TL_stories$TL_storyItemDeleted tL_stories$TL_storyItemDeleted = new TL_stories$TL_storyItemDeleted();
        tL_stories$TL_storyItemDeleted.id = tL_stories$StoryItem.id;
        return tL_stories$TL_storyItemDeleted;
    }

    private void checkExpiredStories(long j, ArrayList arrayList) {
        int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        SQLiteDatabase database = this.storage.getDatabase();
        ArrayList arrayList2 = null;
        ArrayList arrayList3 = null;
        int i = 0;
        while (i < arrayList.size()) {
            TL_stories$StoryItem tL_stories$StoryItem = (TL_stories$StoryItem) arrayList.get(i);
            if (currentTime > ((TL_stories$StoryItem) arrayList.get(i)).expire_date) {
                if (arrayList3 == null) {
                    arrayList3 = new ArrayList();
                    arrayList2 = new ArrayList();
                }
                arrayList3.add(Integer.valueOf(tL_stories$StoryItem.id));
                arrayList2.add(tL_stories$StoryItem);
                arrayList.remove(i);
                i--;
            }
            i++;
        }
        if (arrayList2 != null) {
            try {
                database.executeFast(String.format(Locale.US, "DELETE FROM stories WHERE dialog_id = %d AND story_id IN (%s)", Long.valueOf(j), TextUtils.join(", ", arrayList3))).stepThis().dispose();
            } catch (SQLiteException e) {
                FileLog.e(e);
            }
        }
    }

    private void fillSkippedStories(long j, TL_stories$PeerStories tL_stories$PeerStories) {
        if (tL_stories$PeerStories != null) {
            try {
                ArrayList arrayList = tL_stories$PeerStories.stories;
                for (int i = 0; i < arrayList.size(); i++) {
                    if (((TL_stories$StoryItem) arrayList.get(i)) instanceof TL_stories$TL_storyItemSkipped) {
                        TL_stories$StoryItem storyInternal = getStoryInternal(j, ((TL_stories$StoryItem) arrayList.get(i)).id);
                        if (storyInternal instanceof TL_stories$TL_storyItem) {
                            arrayList.set(i, storyInternal);
                        }
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    private static int getStoryId(MessageObject messageObject) {
        TLRPC$WebPage tLRPC$WebPage;
        int i = messageObject.type;
        if (i == 23 || i == 24) {
            return messageObject.messageOwner.media.id;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
        if (tLRPC$MessageMedia != null && (tLRPC$WebPage = tLRPC$MessageMedia.webpage) != null && tLRPC$WebPage.attributes != null) {
            for (int i2 = 0; i2 < messageObject.messageOwner.media.webpage.attributes.size(); i2++) {
                TLRPC$WebPageAttribute tLRPC$WebPageAttribute = (TLRPC$WebPageAttribute) messageObject.messageOwner.media.webpage.attributes.get(i2);
                if (tLRPC$WebPageAttribute instanceof TLRPC$TL_webPageAttributeStory) {
                    return ((TLRPC$TL_webPageAttributeStory) tLRPC$WebPageAttribute).id;
                }
            }
        }
        return messageObject.messageOwner.reply_to.story_id;
    }

    private TL_stories$StoryItem getStoryInternal(long j, int i) {
        TL_stories$StoryItem tL_stories$StoryItem = null;
        try {
            SQLiteCursor queryFinalized = this.storage.getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, custom_params FROM stories WHERE dialog_id = %d AND story_id = %d", Long.valueOf(j), Integer.valueOf(i)), new Object[0]);
            if (queryFinalized.next()) {
                NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(0);
                NativeByteBuffer byteBufferValue2 = queryFinalized.byteBufferValue(1);
                if (byteBufferValue != null) {
                    tL_stories$StoryItem = TL_stories$StoryItem.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(true), true);
                    tL_stories$StoryItem.dialogId = j;
                    byteBufferValue.reuse();
                }
                if (tL_stories$StoryItem != null) {
                    StoryCustomParamsHelper.readLocalParams(tL_stories$StoryItem, byteBufferValue2);
                }
                if (byteBufferValue2 != null) {
                    byteBufferValue2.reuse();
                }
            }
            queryFinalized.dispose();
        } catch (SQLiteException e) {
            FileLog.e(e);
        }
        return tL_stories$StoryItem;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteAllUserStories$18(long j) {
        try {
            this.storage.getDatabase().executeFast(String.format(Locale.US, "DELETE FROM stories WHERE dialog_id = %d", Long.valueOf(j))).stepThis().dispose();
        } catch (Throwable th) {
            this.storage.checkSQLException(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteStories$12(ArrayList arrayList, long j) {
        SQLiteDatabase database = this.storage.getDatabase();
        try {
            database.executeFast(String.format(Locale.US, "DELETE FROM stories WHERE dialog_id = %d AND story_id IN (%s)", Long.valueOf(j), TextUtils.join(", ", arrayList))).stepThis().dispose();
        } catch (Throwable th) {
            this.storage.checkSQLException(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteStory$11(long j, int i) {
        try {
            this.storage.getDatabase().executeFast(String.format(Locale.US, "DELETE FROM stories WHERE dialog_id = %d AND story_id = %d", Long.valueOf(j), Integer.valueOf(i))).stepThis().dispose();
        } catch (Throwable th) {
            this.storage.checkSQLException(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillMessagesWithStories$14(Timer.Task task, final ArrayList arrayList, long j, boolean z, Timer timer, int[] iArr, Runnable runnable, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        TL_stories$StoryItem tL_stories$TL_storyItemDeleted;
        int i;
        Timer.done(task);
        if (tLObject != null) {
            TL_stories$TL_stories_stories tL_stories$TL_stories_stories = (TL_stories$TL_stories_stories) tLObject;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                MessageObject messageObject = (MessageObject) arrayList.get(i2);
                int i3 = 0;
                while (true) {
                    if (i3 >= tL_stories$TL_stories_stories.stories.size()) {
                        tL_stories$TL_storyItemDeleted = new TL_stories$TL_storyItemDeleted();
                        tL_stories$TL_storyItemDeleted.id = getStoryId(messageObject);
                        i = this.currentAccount;
                        break;
                    } else if (((TL_stories$StoryItem) tL_stories$TL_stories_stories.stories.get(i3)).id == getStoryId(messageObject)) {
                        i = this.currentAccount;
                        tL_stories$TL_storyItemDeleted = (TL_stories$StoryItem) tL_stories$TL_stories_stories.stories.get(i3);
                        break;
                    } else {
                        i3++;
                    }
                }
                applyStory(i, j, messageObject, tL_stories$TL_storyItemDeleted);
                if (z) {
                    this.storage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            StoriesStorage.this.lambda$fillMessagesWithStories$13(arrayList);
                        }
                    });
                }
            }
        } else if (tLRPC$TL_error != null) {
            Timer.log(timer, "fillMessagesWithStories: getStoriesByID error " + tLRPC$TL_error.code + " " + tLRPC$TL_error.text);
        }
        int i4 = iArr[0] - 1;
        iArr[0] = i4;
        if (i4 == 0) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$getAllStories$1(TL_stories$PeerStories tL_stories$PeerStories) {
        ArrayList arrayList = tL_stories$PeerStories.stories;
        return -((TL_stories$StoryItem) arrayList.get(arrayList.size() - 1)).date;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01c1  */
    /* JADX WARN: Type inference failed for: r3v18, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v26 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$getAllStories$3(final Consumer consumer) {
        final Consumer consumer2;
        SQLiteCursor sQLiteCursor;
        ArrayList arrayList;
        SQLiteDatabase sQLiteDatabase;
        int i;
        LongSparseIntArray longSparseIntArray;
        SQLiteDatabase sQLiteDatabase2;
        int i2;
        LongSparseIntArray longSparseIntArray2;
        int i3;
        TLRPC$Peer tLRPC$Peer;
        Long valueOf;
        ArrayList<Long> arrayList2;
        Consumer consumer3 = consumer;
        int i4 = 0;
        int i5 = 1;
        SQLiteDatabase database = this.storage.getDatabase();
        ArrayList arrayList3 = new ArrayList();
        ArrayList<Long> arrayList4 = new ArrayList<>();
        ArrayList<Long> arrayList5 = new ArrayList<>();
        ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        try {
            SQLiteCursor queryFinalized = database.queryFinalized("SELECT dialog_id, max_read FROM stories_counter", new Object[0]);
            try {
                LongSparseIntArray longSparseIntArray3 = new LongSparseIntArray();
                while (queryFinalized.next()) {
                    long longValue = queryFinalized.longValue(0);
                    longSparseIntArray3.put(longValue, queryFinalized.intValue(1));
                    if (longValue > 0) {
                        valueOf = Long.valueOf(longValue);
                        arrayList2 = arrayList4;
                    } else {
                        valueOf = Long.valueOf(longValue);
                        arrayList2 = arrayList5;
                    }
                    arrayList2.add(valueOf);
                }
                queryFinalized.dispose();
                int i6 = 0;
                while (i6 < longSparseIntArray3.size()) {
                    try {
                        long keyAt = longSparseIntArray3.keyAt(i6);
                        int valueAt = longSparseIntArray3.valueAt(i6);
                        Locale locale = Locale.US;
                        Object[] objArr = new Object[i5];
                        objArr[i4] = Long.valueOf(keyAt);
                        sQLiteCursor = database.queryFinalized(String.format(locale, "SELECT data, custom_params FROM stories WHERE dialog_id = %d", objArr), new Object[i4]);
                        try {
                            arrayList = new ArrayList();
                            ?? r3 = i5;
                            while (sQLiteCursor.next()) {
                                NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(i4);
                                NativeByteBuffer byteBufferValue2 = sQLiteCursor.byteBufferValue(r3);
                                if (byteBufferValue != null) {
                                    sQLiteDatabase2 = database;
                                    TL_stories$StoryItem TLdeserialize = TL_stories$StoryItem.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(r3), r3);
                                    TLdeserialize.dialogId = keyAt;
                                    TL_stories$StoryFwdHeader tL_stories$StoryFwdHeader = TLdeserialize.fwd_from;
                                    if (tL_stories$StoryFwdHeader != null && (tLRPC$Peer = tL_stories$StoryFwdHeader.from) != null) {
                                        MessagesStorage.addLoadPeerInfo(tLRPC$Peer, arrayList4, arrayList5);
                                    }
                                    longSparseIntArray2 = longSparseIntArray3;
                                    int i7 = 0;
                                    while (i7 < TLdeserialize.media_areas.size()) {
                                        if (TLdeserialize.media_areas.get(i7) instanceof TL_stories$TL_mediaAreaChannelPost) {
                                            i3 = i6;
                                            long j = ((TL_stories$TL_mediaAreaChannelPost) TLdeserialize.media_areas.get(i7)).channel_id;
                                            if (!arrayList5.contains(Long.valueOf(j))) {
                                                arrayList5.add(Long.valueOf(j));
                                            }
                                        } else {
                                            i3 = i6;
                                        }
                                        i7++;
                                        i6 = i3;
                                    }
                                    i2 = i6;
                                    TLRPC$Peer tLRPC$Peer2 = TLdeserialize.from_id;
                                    if (tLRPC$Peer2 != null) {
                                        MessagesStorage.addLoadPeerInfo(tLRPC$Peer2, arrayList4, arrayList5);
                                    }
                                    StoryCustomParamsHelper.readLocalParams(TLdeserialize, byteBufferValue2);
                                    arrayList.add(TLdeserialize);
                                    byteBufferValue.reuse();
                                } else {
                                    sQLiteDatabase2 = database;
                                    i2 = i6;
                                    longSparseIntArray2 = longSparseIntArray3;
                                }
                                if (byteBufferValue2 != null) {
                                    byteBufferValue2.reuse();
                                }
                                database = sQLiteDatabase2;
                                longSparseIntArray3 = longSparseIntArray2;
                                i6 = i2;
                                i4 = 0;
                                r3 = 1;
                            }
                            sQLiteDatabase = database;
                            i = i6;
                            longSparseIntArray = longSparseIntArray3;
                            sQLiteCursor.dispose();
                        } catch (Throwable th) {
                            th = th;
                        }
                        try {
                            TL_stories$TL_peerStories tL_stories$TL_peerStories = new TL_stories$TL_peerStories();
                            tL_stories$TL_peerStories.stories = arrayList;
                            tL_stories$TL_peerStories.max_read_id = valueAt;
                            tL_stories$TL_peerStories.peer = MessagesController.getInstance(this.currentAccount).getPeer(keyAt);
                            arrayList3.add(tL_stories$TL_peerStories);
                            i6 = i + 1;
                            consumer3 = consumer;
                            database = sQLiteDatabase;
                            longSparseIntArray3 = longSparseIntArray;
                            i4 = 0;
                            i5 = 1;
                        } catch (Throwable th2) {
                            th = th2;
                            sQLiteCursor = null;
                            consumer2 = consumer;
                            try {
                                FileLog.e(th);
                                if (sQLiteCursor != null) {
                                    sQLiteCursor.dispose();
                                }
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda14
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        Consumer.this.accept(null);
                                    }
                                });
                                return;
                            } catch (Throwable th3) {
                                if (sQLiteCursor != null) {
                                    sQLiteCursor.dispose();
                                }
                                throw th3;
                            }
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        consumer2 = consumer;
                        sQLiteCursor = null;
                        FileLog.e(th);
                        if (sQLiteCursor != null) {
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda14
                            @Override // java.lang.Runnable
                            public final void run() {
                                Consumer.this.accept(null);
                            }
                        });
                        return;
                    }
                }
                final TL_stories$TL_stories_allStories tL_stories$TL_stories_allStories = new TL_stories$TL_stories_allStories();
                tL_stories$TL_stories_allStories.peer_stories = arrayList3;
                tL_stories$TL_stories_allStories.users = this.storage.getUsers(arrayList4);
                tL_stories$TL_stories_allStories.chats = this.storage.getChats(arrayList5);
                int i8 = 0;
                while (i8 < tL_stories$TL_stories_allStories.peer_stories.size()) {
                    TL_stories$PeerStories tL_stories$PeerStories = (TL_stories$PeerStories) tL_stories$TL_stories_allStories.peer_stories.get(i8);
                    checkExpiredStories(DialogObject.getPeerDialogId(tL_stories$PeerStories.peer), tL_stories$PeerStories.stories);
                    if (tL_stories$PeerStories.stories.isEmpty()) {
                        tL_stories$TL_stories_allStories.peer_stories.remove(i8);
                        i8--;
                    }
                    Collections.sort(tL_stories$PeerStories.stories, StoriesController.storiesComparator);
                    i8++;
                }
                Collections.sort(tL_stories$TL_stories_allStories.peer_stories, Comparator$-CC.comparingInt(new ToIntFunction() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda12
                    @Override // j$.util.function.ToIntFunction
                    public final int applyAsInt(Object obj) {
                        int lambda$getAllStories$1;
                        lambda$getAllStories$1 = StoriesStorage.lambda$getAllStories$1((TL_stories$PeerStories) obj);
                        return lambda$getAllStories$1;
                    }
                }));
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda13
                    @Override // java.lang.Runnable
                    public final void run() {
                        Consumer.this.accept(tL_stories$TL_stories_allStories);
                    }
                });
            } catch (Throwable th5) {
                th = th5;
                consumer2 = consumer3;
                sQLiteCursor = queryFinalized;
            }
        } catch (Throwable th6) {
            th = th6;
            consumer2 = consumer3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getMaxReadIds$16(final Consumer consumer) {
        SQLiteDatabase database = this.storage.getDatabase();
        final LongSparseIntArray longSparseIntArray = new LongSparseIntArray();
        try {
            SQLiteCursor queryFinalized = database.queryFinalized("SELECT dialog_id, max_read FROM stories_counter", new Object[0]);
            while (queryFinalized.next()) {
                longSparseIntArray.put(queryFinalized.longValue(0), queryFinalized.intValue(1));
            }
        } catch (Exception e) {
            this.storage.checkSQLException(e);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                Consumer.this.accept(longSparseIntArray);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00cf A[Catch: all -> 0x0052, TryCatch #0 {all -> 0x0052, blocks: (B:3:0x0009, B:5:0x0017, B:7:0x0039, B:9:0x0043, B:13:0x0057, B:16:0x005d, B:25:0x00b2, B:27:0x00cf, B:29:0x00d5, B:19:0x0083, B:21:0x0087), top: B:33:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00d4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processUpdate$9(TL_stories$TL_updateStory tL_stories$TL_updateStory) {
        int i;
        boolean z;
        SQLiteDatabase database = this.storage.getDatabase();
        try {
            long peerDialogId = DialogObject.getPeerDialogId(tL_stories$TL_updateStory.peer);
            TL_stories$StoryItem tL_stories$StoryItem = tL_stories$TL_updateStory.story;
            int i2 = tL_stories$StoryItem.id;
            if (!(tL_stories$StoryItem instanceof TL_stories$TL_storyItemDeleted)) {
                if (tL_stories$StoryItem instanceof TL_stories$TL_storyItem) {
                    lambda$updateStoryItem$7(peerDialogId, tL_stories$StoryItem);
                    SQLiteCursor queryFinalized = database.queryFinalized(String.format(Locale.US, "SELECT story_id FROM stories WHERE dialog_id = %d AND story_id = %d", Long.valueOf(peerDialogId), Integer.valueOf(i2)), new Object[0]);
                    boolean next = queryFinalized.next();
                    queryFinalized.dispose();
                    if (!next) {
                        i = 1;
                        SQLiteCursor queryFinalized2 = database.queryFinalized("SELECT count, max_read FROM stories_counter WHERE dialog_id = " + peerDialogId, new Object[0]);
                        if (!queryFinalized2.next()) {
                        }
                        queryFinalized2.dispose();
                        database.executeFast(String.format(Locale.US, "UPDATE stories_counter SET count = %d WHERE dialog_id = %d", Integer.valueOf(r7 + i), Long.valueOf(peerDialogId))).stepThis().dispose();
                    }
                }
                i = 0;
                SQLiteCursor queryFinalized22 = database.queryFinalized("SELECT count, max_read FROM stories_counter WHERE dialog_id = " + peerDialogId, new Object[0]);
                if (!queryFinalized22.next()) {
                }
                queryFinalized22.dispose();
                database.executeFast(String.format(Locale.US, "UPDATE stories_counter SET count = %d WHERE dialog_id = %d", Integer.valueOf(r7 + i), Long.valueOf(peerDialogId))).stepThis().dispose();
            }
            Locale locale = Locale.US;
            SQLiteCursor queryFinalized3 = database.queryFinalized(String.format(locale, "SELECT data, custom_params FROM stories WHERE dialog_id = %d AND story_id = %d", Long.valueOf(peerDialogId), Integer.valueOf(i2)), new Object[0]);
            if (queryFinalized3.next()) {
                NativeByteBuffer byteBufferValue = queryFinalized3.byteBufferValue(0);
                NativeByteBuffer byteBufferValue2 = queryFinalized3.byteBufferValue(1);
                if (byteBufferValue != null) {
                    StoryCustomParamsHelper.readLocalParams(TL_stories$StoryItem.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(true), true), byteBufferValue2);
                    byteBufferValue.reuse();
                }
                if (byteBufferValue2 != null) {
                    byteBufferValue2.reuse();
                }
                z = true;
            } else {
                z = false;
            }
            queryFinalized3.dispose();
            database.executeFast(String.format(locale, "DELETE FROM stories WHERE dialog_id = %d AND story_id = %d", Long.valueOf(peerDialogId), Integer.valueOf(i2))).stepThis().dispose();
            if (z) {
                i = -1;
                SQLiteCursor queryFinalized222 = database.queryFinalized("SELECT count, max_read FROM stories_counter WHERE dialog_id = " + peerDialogId, new Object[0]);
                int intValue = !queryFinalized222.next() ? queryFinalized222.intValue(1) : 0;
                queryFinalized222.dispose();
                database.executeFast(String.format(Locale.US, "UPDATE stories_counter SET count = %d WHERE dialog_id = %d", Integer.valueOf(intValue + i), Long.valueOf(peerDialogId))).stepThis().dispose();
            }
            i = 0;
            SQLiteCursor queryFinalized2222 = database.queryFinalized("SELECT count, max_read FROM stories_counter WHERE dialog_id = " + peerDialogId, new Object[0]);
            if (!queryFinalized2222.next()) {
            }
            queryFinalized2222.dispose();
            database.executeFast(String.format(Locale.US, "UPDATE stories_counter SET count = %d WHERE dialog_id = %d", Integer.valueOf(intValue + i), Long.valueOf(peerDialogId))).stepThis().dispose();
        } catch (Throwable th) {
            this.storage.checkSQLException(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putPeerStories$17(TL_stories$PeerStories tL_stories$PeerStories) {
        putStoriesInternal(DialogObject.getPeerDialogId(tL_stories$PeerStories.peer), tL_stories$PeerStories);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveAllStories$4(ArrayList arrayList, boolean z, boolean z2, Runnable runnable) {
        SQLiteDatabase database = this.storage.getDatabase();
        for (int i = 0; i < arrayList.size(); i++) {
            TL_stories$PeerStories tL_stories$PeerStories = (TL_stories$PeerStories) arrayList.get(i);
            fillSkippedStories(DialogObject.getPeerDialogId(tL_stories$PeerStories.peer), tL_stories$PeerStories);
        }
        if (!z) {
            try {
                SQLiteCursor queryFinalized = database.queryFinalized("SELECT DISTINCT dialog_id FROM stories", new Object[0]);
                ArrayList arrayList2 = new ArrayList();
                while (queryFinalized.next()) {
                    long longValue = queryFinalized.longValue(0);
                    if (longValue > 0) {
                        TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(longValue));
                        if (user == null) {
                            user = MessagesStorage.getInstance(this.currentAccount).getUser(longValue);
                        }
                        if (user != null) {
                            if (user.stories_hidden == z2 && !arrayList2.contains(Long.valueOf(longValue))) {
                            }
                        }
                        arrayList2.add(Long.valueOf(longValue));
                    } else {
                        long j = -longValue;
                        TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
                        if (chat == null) {
                            chat = MessagesStorage.getInstance(this.currentAccount).getChat(j);
                        }
                        if (chat != null) {
                            if (chat.stories_hidden == z2 && !arrayList2.contains(Long.valueOf(longValue))) {
                            }
                        }
                        arrayList2.add(Long.valueOf(longValue));
                    }
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("StoriesStorage delete dialogs " + TextUtils.join(",", arrayList2));
                }
                database.executeFast(String.format(Locale.US, "DELETE FROM stories WHERE dialog_id IN(%s)", TextUtils.join(",", arrayList2))).stepThis().dispose();
            } catch (Throwable th) {
                this.storage.checkSQLException(th);
            }
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            TL_stories$PeerStories tL_stories$PeerStories2 = (TL_stories$PeerStories) arrayList.get(i2);
            putStoriesInternal(DialogObject.getPeerDialogId(tL_stories$PeerStories2.peer), tL_stories$PeerStories2);
        }
        if (runnable != null) {
            AndroidUtilities.runOnUIThread(runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateMaxReadId$8(long j, int i) {
        try {
            this.storage.getDatabase().executeFast(String.format(Locale.US, "REPLACE INTO stories_counter VALUES(%d, 0, %d)", Long.valueOf(j), Integer.valueOf(i))).stepThis().dispose();
        } catch (Throwable th) {
            this.storage.checkSQLException(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateStories$10(TL_stories$PeerStories tL_stories$PeerStories) {
        for (int i = 0; i < tL_stories$PeerStories.stories.size(); i++) {
            lambda$updateStoryItem$7(DialogObject.getPeerDialogId(tL_stories$PeerStories.peer), (TL_stories$StoryItem) tL_stories$PeerStories.stories.get(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updateStoryItemInternal */
    public void lambda$updateStoryItem$7(long j, TL_stories$StoryItem tL_stories$StoryItem) {
        if (j == 0 || tL_stories$StoryItem == null) {
            return;
        }
        if (tL_stories$StoryItem instanceof TL_stories$TL_storyItemDeleted) {
            FileLog.e("StoriesStorage: try write deleted story");
        }
        if (StoriesUtilities.isExpired(this.currentAccount, tL_stories$StoryItem)) {
            FileLog.e("StoriesStorage: try write expired story");
        }
        try {
            SQLitePreparedStatement executeFast = this.storage.getDatabase().executeFast("REPLACE INTO stories VALUES(?, ?, ?, ?)");
            executeFast.requery();
            executeFast.bindLong(1, j);
            executeFast.bindLong(2, tL_stories$StoryItem.id);
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tL_stories$StoryItem.getObjectSize());
            tL_stories$StoryItem.serializeToStream(nativeByteBuffer);
            executeFast.bindByteBuffer(3, nativeByteBuffer);
            NativeByteBuffer writeLocalParams = StoryCustomParamsHelper.writeLocalParams(tL_stories$StoryItem);
            if (writeLocalParams != null) {
                executeFast.bindByteBuffer(4, writeLocalParams);
            } else {
                executeFast.bindNull(4);
            }
            if (writeLocalParams != null) {
                writeLocalParams.reuse();
            }
            executeFast.step();
            nativeByteBuffer.reuse();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void deleteAllUserStories(final long j) {
        this.storage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                StoriesStorage.this.lambda$deleteAllUserStories$18(j);
            }
        });
    }

    public void deleteStories(final long j, final ArrayList arrayList) {
        this.storage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                StoriesStorage.this.lambda$deleteStories$12(arrayList, j);
            }
        });
    }

    public void deleteStory(final long j, final int i) {
        this.storage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                StoriesStorage.this.lambda$deleteStory$11(j, i);
            }
        });
    }

    public void fillMessagesWithStories(LongSparseArray longSparseArray, Runnable runnable, int i, Timer timer) {
        fillMessagesWithStories(longSparseArray, runnable, i, true, timer);
    }

    public void fillMessagesWithStories(LongSparseArray longSparseArray, final Runnable runnable, int i, final boolean z, final Timer timer) {
        LongSparseArray longSparseArray2 = longSparseArray;
        Timer timer2 = timer;
        if (runnable == null) {
            return;
        }
        if (longSparseArray2 == null) {
            runnable.run();
            return;
        }
        ArrayList arrayList = new ArrayList();
        Timer.Task start = Timer.start(timer2, "fillMessagesWithStories: applying stories for existing array");
        int i2 = 0;
        while (i2 < longSparseArray.size()) {
            long keyAt = longSparseArray2.keyAt(i2);
            ArrayList arrayList2 = (ArrayList) longSparseArray2.valueAt(i2);
            int i3 = 0;
            while (i3 < arrayList2.size()) {
                MessageObject messageObject = (MessageObject) arrayList2.get(i3);
                TL_stories$StoryItem storyInternal = getStoryInternal(keyAt, getStoryId(messageObject));
                if (storyInternal != null && !(storyInternal instanceof TL_stories$TL_storyItemSkipped)) {
                    applyStory(this.currentAccount, keyAt, messageObject, storyInternal);
                    arrayList.add(messageObject);
                    arrayList2.remove(i3);
                    i3--;
                    if (arrayList2.isEmpty()) {
                        longSparseArray2.removeAt(i2);
                        i2--;
                    }
                }
                i3++;
            }
            i2++;
        }
        Timer.done(start);
        if (z) {
            lambda$fillMessagesWithStories$13(arrayList);
        }
        if (longSparseArray.isEmpty()) {
            runnable.run();
            return;
        }
        final int[] iArr = {longSparseArray.size()};
        int i4 = 0;
        while (i4 < longSparseArray.size()) {
            final long keyAt2 = longSparseArray2.keyAt(i4);
            final ArrayList arrayList3 = (ArrayList) longSparseArray2.valueAt(i4);
            TL_stories$TL_stories_getStoriesByID tL_stories$TL_stories_getStoriesByID = new TL_stories$TL_stories_getStoriesByID();
            tL_stories$TL_stories_getStoriesByID.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(keyAt2);
            for (int i5 = 0; i5 < arrayList3.size(); i5++) {
                tL_stories$TL_stories_getStoriesByID.id.add(Integer.valueOf(getStoryId((MessageObject) arrayList3.get(i5))));
            }
            final Timer.Task start2 = Timer.start(timer2, "fillMessagesWithStories: getStoriesByID did=" + keyAt2 + " ids=" + TextUtils.join(",", tL_stories$TL_stories_getStoriesByID.id));
            int i6 = i4;
            int sendRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_stories$TL_stories_getStoriesByID, new RequestDelegate() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda2
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    StoriesStorage.this.lambda$fillMessagesWithStories$14(start2, arrayList3, keyAt2, z, timer, iArr, runnable, tLObject, tLRPC$TL_error);
                }
            });
            if (i != 0) {
                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(sendRequest, i);
            }
            i4 = i6 + 1;
            longSparseArray2 = longSparseArray;
            timer2 = timer;
        }
    }

    public void getAllStories(final Consumer consumer) {
        this.storage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                StoriesStorage.this.lambda$getAllStories$3(consumer);
            }
        });
    }

    public void getMaxReadIds(final Consumer consumer) {
        this.storage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                StoriesStorage.this.lambda$getMaxReadIds$16(consumer);
            }
        });
    }

    public void processUpdate(final TL_stories$TL_updateStory tL_stories$TL_updateStory) {
        this.storage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                StoriesStorage.this.lambda$processUpdate$9(tL_stories$TL_updateStory);
            }
        });
    }

    public void putPeerStories(final TL_stories$PeerStories tL_stories$PeerStories) {
        this.storage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                StoriesStorage.this.lambda$putPeerStories$17(tL_stories$PeerStories);
            }
        });
    }

    public void putStoriesInternal(long j, TL_stories$PeerStories tL_stories$PeerStories) {
        SQLiteDatabase database = this.storage.getDatabase();
        if (tL_stories$PeerStories != null) {
            try {
                ArrayList arrayList = tL_stories$PeerStories.stories;
                SQLitePreparedStatement executeFast = database.executeFast("REPLACE INTO stories VALUES(?, ?, ?, ?)");
                for (int i = 0; i < arrayList.size(); i++) {
                    executeFast.requery();
                    TL_stories$StoryItem tL_stories$StoryItem = (TL_stories$StoryItem) arrayList.get(i);
                    if (tL_stories$StoryItem instanceof TL_stories$TL_storyItemDeleted) {
                        FileLog.e("try write deleted story");
                    } else {
                        executeFast.bindLong(1, j);
                        executeFast.bindLong(2, tL_stories$StoryItem.id);
                        NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tL_stories$StoryItem.getObjectSize());
                        tL_stories$StoryItem.serializeToStream(nativeByteBuffer);
                        executeFast.bindByteBuffer(3, nativeByteBuffer);
                        NativeByteBuffer writeLocalParams = StoryCustomParamsHelper.writeLocalParams(tL_stories$StoryItem);
                        if (writeLocalParams != null) {
                            executeFast.bindByteBuffer(4, writeLocalParams);
                        } else {
                            executeFast.bindNull(4);
                        }
                        if (writeLocalParams != null) {
                            writeLocalParams.reuse();
                        }
                        executeFast.step();
                        nativeByteBuffer.reuse();
                    }
                }
                executeFast.dispose();
                database.executeFast(String.format(Locale.US, "REPLACE INTO stories_counter VALUES(%d, %d, %d)", Long.valueOf(j), 0, Integer.valueOf(tL_stories$PeerStories.max_read_id))).stepThis().dispose();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public void putStoryInternal(long j, TL_stories$StoryItem tL_stories$StoryItem) {
        try {
            SQLitePreparedStatement executeFast = this.storage.getDatabase().executeFast("REPLACE INTO stories VALUES(?, ?, ?, ?)");
            if (tL_stories$StoryItem instanceof TL_stories$TL_storyItemDeleted) {
                FileLog.e("putStoryInternal: try write deleted story");
                return;
            }
            executeFast.bindLong(1, j);
            executeFast.bindLong(2, tL_stories$StoryItem.id);
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tL_stories$StoryItem.getObjectSize());
            tL_stories$StoryItem.serializeToStream(nativeByteBuffer);
            executeFast.bindByteBuffer(3, nativeByteBuffer);
            NativeByteBuffer writeLocalParams = StoryCustomParamsHelper.writeLocalParams(tL_stories$StoryItem);
            if (writeLocalParams != null) {
                executeFast.bindByteBuffer(4, writeLocalParams);
            } else {
                executeFast.bindNull(4);
            }
            if (writeLocalParams != null) {
                writeLocalParams.reuse();
            }
            executeFast.step();
            nativeByteBuffer.reuse();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void saveAllStories(final ArrayList arrayList, final boolean z, final boolean z2, final Runnable runnable) {
        this.storage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                StoriesStorage.this.lambda$saveAllStories$4(arrayList, z, z2, runnable);
            }
        });
    }

    public void updateMaxReadId(final long j, final int i) {
        TL_stories$PeerStories tL_stories$PeerStories;
        TL_stories$PeerStories tL_stories$PeerStories2;
        int i2 = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        if (i2 > 0) {
            TLRPC$UserFull userFull = messagesController.getUserFull(j);
            if (userFull != null && (tL_stories$PeerStories2 = userFull.stories) != null) {
                tL_stories$PeerStories2.max_read_id = i;
                this.storage.updateUserInfo(userFull, false);
            }
        } else {
            TLRPC$ChatFull chatFull = messagesController.getChatFull(-j);
            if (chatFull != null && (tL_stories$PeerStories = chatFull.stories) != null) {
                tL_stories$PeerStories.max_read_id = i;
                this.storage.updateChatInfo(chatFull, false);
            }
        }
        this.storage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                StoriesStorage.this.lambda$updateMaxReadId$8(j, i);
            }
        });
    }

    /* renamed from: updateMessagesWithStories */
    public void lambda$fillMessagesWithStories$13(List list) {
        SQLitePreparedStatement sQLitePreparedStatement;
        long dialogId;
        try {
            SQLiteDatabase database = this.storage.getDatabase();
            if (list.isEmpty()) {
                return;
            }
            SQLitePreparedStatement executeFast = database.executeFast("UPDATE messages_v2 SET replydata = ? WHERE mid = ? AND uid = ?");
            SQLitePreparedStatement executeFast2 = database.executeFast("UPDATE messages_topics SET replydata = ? WHERE mid = ? AND uid = ?");
            SQLitePreparedStatement executeFast3 = database.executeFast("UPDATE messages_v2 SET data = ? WHERE mid = ? AND uid = ?");
            SQLitePreparedStatement executeFast4 = database.executeFast("UPDATE messages_topics SET data = ? WHERE mid = ? AND uid = ?");
            for (int i = 0; i < list.size(); i++) {
                MessageObject messageObject = (MessageObject) list.get(i);
                int i2 = 0;
                while (i2 < 2) {
                    if (messageObject.messageOwner.replyStory != null) {
                        sQLitePreparedStatement = i2 == 0 ? executeFast : executeFast2;
                        if (sQLitePreparedStatement != null) {
                            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(messageObject.messageOwner.replyStory.getObjectSize());
                            messageObject.messageOwner.replyStory.serializeToStream(nativeByteBuffer);
                            sQLitePreparedStatement.requery();
                            sQLitePreparedStatement.bindByteBuffer(1, nativeByteBuffer);
                            sQLitePreparedStatement.bindInteger(2, messageObject.getId());
                            dialogId = messageObject.getDialogId();
                            sQLitePreparedStatement.bindLong(3, dialogId);
                            sQLitePreparedStatement.step();
                        }
                    } else {
                        sQLitePreparedStatement = i2 == 0 ? executeFast3 : executeFast4;
                        if (sQLitePreparedStatement != null) {
                            NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer(messageObject.messageOwner.getObjectSize());
                            messageObject.messageOwner.serializeToStream(nativeByteBuffer2);
                            sQLitePreparedStatement.requery();
                            sQLitePreparedStatement.bindByteBuffer(1, nativeByteBuffer2);
                            sQLitePreparedStatement.bindInteger(2, messageObject.getId());
                            dialogId = messageObject.getDialogId();
                            sQLitePreparedStatement.bindLong(3, dialogId);
                            sQLitePreparedStatement.step();
                        }
                    }
                    i2++;
                }
            }
            executeFast.dispose();
            executeFast2.dispose();
            executeFast3.dispose();
            executeFast4.dispose();
        } catch (Throwable th) {
            this.storage.checkSQLException(th);
        }
    }

    public void updateStories(final TL_stories$PeerStories tL_stories$PeerStories) {
        this.storage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                StoriesStorage.this.lambda$updateStories$10(tL_stories$PeerStories);
            }
        });
    }

    public void updateStoryItem(final long j, final TL_stories$StoryItem tL_stories$StoryItem) {
        if (j == 0) {
            return;
        }
        this.storage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.StoriesStorage$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                StoriesStorage.this.lambda$updateStoryItem$7(j, tL_stories$StoryItem);
            }
        });
    }
}
