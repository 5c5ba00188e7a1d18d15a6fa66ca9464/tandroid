package org.telegram.messenger;

import android.os.Bundle;
import android.text.TextUtils;
import androidx.collection.LongSparseArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.SavedMessagesController;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes3.dex */
public class SavedMessagesController {
    private final int currentAccount;
    private int dialogsCount;
    private int dialogsCountHidden;
    public boolean dialogsEndReached;
    private boolean dialogsLoaded;
    private boolean dialogsLoading;
    private boolean loadedCache;
    private boolean loadingCache;
    private boolean loadingCacheOnly;
    private boolean saving;
    public boolean unsupported;
    private ArrayList<SavedDialog> cachedDialogs = new ArrayList<>();
    private ArrayList<SavedDialog> loadedDialogs = new ArrayList<>();
    public ArrayList<SavedDialog> allDialogs = new ArrayList<>();
    private final Runnable saveCacheRunnable = new Runnable() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda16
        @Override // java.lang.Runnable
        public final void run() {
            SavedMessagesController.this.saveCache();
        }
    };
    private final LongSparseArray checkMessagesCallbacks = new LongSparseArray();

    /* loaded from: classes3.dex */
    public static class SavedDialog {
        public long dialogId;
        private int localDate;
        public MessageObject message;
        public int messagesCount;
        public boolean messagesCountLoaded;
        public boolean pinned;
        private int pinnedOrder;
        public int top_message_id;

        public static SavedDialog fromMessage(int i, TLRPC.Message message) {
            SavedDialog savedDialog = new SavedDialog();
            savedDialog.dialogId = MessageObject.getSavedDialogId(UserConfig.getInstance(i).getClientUserId(), message);
            savedDialog.pinned = false;
            savedDialog.top_message_id = message.id;
            savedDialog.message = new MessageObject(i, message, null, null, null, null, null, false, false, 0L, false, false, true);
            return savedDialog;
        }

        public static SavedDialog fromTL(int i, TLRPC.TL_savedDialog tL_savedDialog, ArrayList<TLRPC.Message> arrayList) {
            TLRPC.Message message;
            SavedDialog savedDialog = new SavedDialog();
            savedDialog.dialogId = DialogObject.getPeerDialogId(tL_savedDialog.peer);
            savedDialog.pinned = tL_savedDialog.pinned;
            savedDialog.top_message_id = tL_savedDialog.top_message;
            int i2 = 0;
            while (true) {
                if (i2 >= arrayList.size()) {
                    message = null;
                    break;
                }
                message = arrayList.get(i2);
                if (savedDialog.top_message_id == message.id) {
                    break;
                }
                i2++;
            }
            TLRPC.Message message2 = message;
            if (message2 != null) {
                savedDialog.message = new MessageObject(i, message2, null, null, null, null, null, false, false, 0L, false, false, true);
            }
            return savedDialog;
        }

        public int getDate() {
            TLRPC.Message message;
            MessageObject messageObject = this.message;
            return (messageObject == null || (message = messageObject.messageOwner) == null) ? this.localDate : ((message.flags & 32768) == 0 || message.edit_hide) ? message.date : message.edit_date;
        }

        public boolean isHidden() {
            TLRPC.Message message;
            MessageObject messageObject = this.message;
            return (messageObject == null || (message = messageObject.messageOwner) == null || !(message.action instanceof TLRPC.TL_messageActionHistoryClear)) ? false : true;
        }
    }

    public SavedMessagesController(int i) {
        this.currentAccount = i;
        this.unsupported = MessagesController.getMainSettings(i).getBoolean("savedMessagesUnsupported", true);
    }

    private void deleteCache() {
        if (this.saving) {
            return;
        }
        this.saving = true;
        final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                SavedMessagesController.this.lambda$deleteCache$13(messagesStorage);
            }
        });
    }

    private ArrayList<Long> getCurrentPinnedOrder(ArrayList<SavedDialog> arrayList) {
        ArrayList<Long> arrayList2 = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            SavedDialog savedDialog = arrayList.get(i);
            if (savedDialog.pinned) {
                arrayList2.add(Long.valueOf(savedDialog.dialogId));
            }
        }
        return arrayList2;
    }

    private void invalidate() {
        SavedDialog savedDialog;
        if (this.dialogsLoaded && this.loadedDialogs.isEmpty()) {
            return;
        }
        for (int i = 0; i < this.loadedDialogs.size(); i++) {
            SavedDialog savedDialog2 = this.loadedDialogs.get(i);
            int i2 = 0;
            while (true) {
                if (i2 >= this.cachedDialogs.size()) {
                    savedDialog = null;
                    break;
                }
                savedDialog = this.cachedDialogs.get(i2);
                if (savedDialog.dialogId == savedDialog2.dialogId) {
                    break;
                }
                i2++;
            }
            if (savedDialog == null && !savedDialog2.pinned) {
                this.cachedDialogs.add(savedDialog2);
            }
        }
        this.loadedDialogs.clear();
        this.dialogsLoaded = false;
        this.dialogsCount = 0;
        this.dialogsEndReached = false;
        update();
        loadDialogs(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteCache$12() {
        this.saving = false;
        this.loadedCache = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteCache$13(MessagesStorage messagesStorage) {
        try {
            messagesStorage.getDatabase().executeFast("DELETE FROM saved_dialogs").stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                SavedMessagesController.this.lambda$deleteCache$12();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hasSavedMessages$14(TLObject tLObject, long j) {
        if (tLObject instanceof TLRPC.messages_Messages) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            int size = messages_messages.messages.size();
            if (messages_messages instanceof TLRPC.TL_messages_messagesSlice) {
                size = ((TLRPC.TL_messages_messagesSlice) messages_messages).count;
            }
            MessagesController.getInstance(this.currentAccount).putUsers(messages_messages.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(messages_messages.chats, false);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
            boolean z = size > 0;
            if (size > 0) {
                if (!updatedDialogCount(j, size)) {
                    if (!messages_messages.messages.isEmpty()) {
                        SavedDialog fromMessage = SavedDialog.fromMessage(this.currentAccount, messages_messages.messages.get(0));
                        fromMessage.messagesCount = size;
                        fromMessage.messagesCountLoaded = true;
                        this.cachedDialogs.add(fromMessage);
                    }
                }
                update();
            }
            ArrayList arrayList = (ArrayList) this.checkMessagesCallbacks.get(j);
            this.checkMessagesCallbacks.remove(j);
            if (arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    ((Utilities.Callback) arrayList.get(i)).run(Boolean.valueOf(z));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hasSavedMessages$15(final long j, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SavedMessagesController.this.lambda$hasSavedMessages$14(tLObject, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadCache$6(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, Runnable runnable) {
        this.loadingCache = false;
        this.loadedCache = true;
        MessagesController.getInstance(this.currentAccount).putUsers(arrayList, true);
        MessagesController.getInstance(this.currentAccount).putChats(arrayList2, true);
        AnimatedEmojiDrawable.getDocumentFetcher(this.currentAccount).processDocuments(arrayList3);
        this.cachedDialogs.clear();
        this.cachedDialogs.addAll(arrayList4);
        updateAllDialogs(true);
        if (runnable == null || this.loadingCacheOnly) {
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0156, code lost:
        if (r16 != null) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x016e, code lost:
        if (r16 != null) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0171, code lost:
        r16.dispose();
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0174, code lost:
        org.telegram.messenger.AndroidUtilities.runOnUIThread(new org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda2(r35, r9, r10, r12, r11, r39));
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0184, code lost:
        return;
     */
    /* JADX WARN: Removed duplicated region for block: B:69:0x016b  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0188  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x018d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadCache$7(MessagesStorage messagesStorage, long j, final Runnable runnable) {
        SQLiteCursor sQLiteCursor;
        Long valueOf;
        ArrayList<Long> arrayList;
        int i = 1;
        int i2 = 0;
        SQLiteDatabase database = messagesStorage.getDatabase();
        final ArrayList arrayList2 = new ArrayList();
        ArrayList<Long> arrayList3 = new ArrayList<>();
        ArrayList<Long> arrayList4 = new ArrayList<>();
        ArrayList arrayList5 = new ArrayList();
        final ArrayList<TLRPC.User> arrayList6 = new ArrayList<>();
        final ArrayList<TLRPC.Chat> arrayList7 = new ArrayList<>();
        final ArrayList<TLRPC.Document> arrayList8 = new ArrayList<>();
        SQLiteCursor sQLiteCursor2 = null;
        try {
            SQLiteCursor queryFinalized = database.queryFinalized("SELECT did, date, last_mid, pinned, flags, folder_id, last_mid_group, count FROM saved_dialogs ORDER BY pinned ASC, date DESC", new Object[0]);
            while (queryFinalized.next()) {
                try {
                    SavedDialog savedDialog = new SavedDialog();
                    savedDialog.dialogId = queryFinalized.longValue(i2);
                    savedDialog.localDate = queryFinalized.intValue(i);
                    savedDialog.top_message_id = queryFinalized.intValue(2);
                    savedDialog.pinnedOrder = queryFinalized.intValue(3);
                    savedDialog.messagesCountLoaded = (queryFinalized.intValue(4) & i) != 0;
                    savedDialog.pinned = savedDialog.pinnedOrder != 999;
                    savedDialog.messagesCount = queryFinalized.intValue(7);
                    long j2 = savedDialog.dialogId;
                    if (j2 < 0) {
                        valueOf = Long.valueOf(-j2);
                        arrayList = arrayList4;
                    } else {
                        valueOf = Long.valueOf(j2);
                        arrayList = arrayList3;
                    }
                    arrayList.add(valueOf);
                    SQLiteDatabase sQLiteDatabase = database;
                    sQLiteCursor = sQLiteCursor2;
                    try {
                        sQLiteCursor2 = sQLiteDatabase.queryFinalized("SELECT data FROM messages_topics WHERE uid = ? AND mid = ? AND topic_id = ?", Long.valueOf(j), Integer.valueOf(savedDialog.top_message_id), Long.valueOf(savedDialog.dialogId));
                        if (sQLiteCursor2.next()) {
                            NativeByteBuffer byteBufferValue = sQLiteCursor2.byteBufferValue(0);
                            TLRPC.Message TLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(true), true);
                            MessagesStorage.addUsersAndChatsFromMessage(TLdeserialize, arrayList3, arrayList4, arrayList5);
                            try {
                                savedDialog.message = new MessageObject(this.currentAccount, TLdeserialize, null, null, null, null, null, false, false, 0L, false, false, true);
                            } catch (Exception e) {
                                e = e;
                                sQLiteCursor = sQLiteCursor2;
                                sQLiteCursor2 = queryFinalized;
                                try {
                                    FileLog.e(e);
                                    if (sQLiteCursor2 != null) {
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    if (sQLiteCursor2 != null) {
                                        sQLiteCursor2.dispose();
                                    }
                                    if (sQLiteCursor != null) {
                                        sQLiteCursor.dispose();
                                    }
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                sQLiteCursor = sQLiteCursor2;
                                sQLiteCursor2 = queryFinalized;
                                if (sQLiteCursor2 != null) {
                                }
                                if (sQLiteCursor != null) {
                                }
                                throw th;
                            }
                        }
                        sQLiteCursor2.dispose();
                        arrayList2.add(savedDialog);
                        database = sQLiteDatabase;
                        i = 1;
                        i2 = 0;
                    } catch (Exception e2) {
                        e = e2;
                        sQLiteCursor2 = queryFinalized;
                        FileLog.e(e);
                        if (sQLiteCursor2 != null) {
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        sQLiteCursor2 = queryFinalized;
                        if (sQLiteCursor2 != null) {
                        }
                        if (sQLiteCursor != null) {
                        }
                        throw th;
                    }
                } catch (Exception e3) {
                    e = e3;
                } catch (Throwable th4) {
                    th = th4;
                }
            }
            sQLiteCursor = sQLiteCursor2;
            try {
                if (!arrayList3.isEmpty()) {
                    messagesStorage.getUsersInternal(arrayList3, arrayList6);
                }
                if (!arrayList4.isEmpty()) {
                    messagesStorage.getChatsInternal(TextUtils.join(",", arrayList4), arrayList7);
                }
                if (!arrayList5.isEmpty()) {
                    messagesStorage.getAnimatedEmoji(TextUtils.join(",", arrayList5), arrayList8);
                }
                queryFinalized.dispose();
            } catch (Exception e4) {
                e = e4;
                sQLiteCursor2 = queryFinalized;
                FileLog.e(e);
                if (sQLiteCursor2 != null) {
                    sQLiteCursor2.dispose();
                }
            } catch (Throwable th5) {
                th = th5;
                sQLiteCursor2 = queryFinalized;
                if (sQLiteCursor2 != null) {
                }
                if (sQLiteCursor != null) {
                }
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            sQLiteCursor = null;
        } catch (Throwable th6) {
            th = th6;
            sQLiteCursor = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDialogs$1() {
        loadDialogs(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:79:0x020d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadDialogs$2(TLObject tLObject, ArrayList arrayList, TLRPC.TL_error tL_error) {
        boolean z = this.unsupported;
        if (tLObject instanceof TLRPC.TL_messages_savedDialogs) {
            this.dialogsLoaded = true;
            TLRPC.TL_messages_savedDialogs tL_messages_savedDialogs = (TLRPC.TL_messages_savedDialogs) tLObject;
            MessagesController.getInstance(this.currentAccount).putUsers(tL_messages_savedDialogs.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(tL_messages_savedDialogs.chats, false);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(tL_messages_savedDialogs.users, tL_messages_savedDialogs.chats, true, true);
            MessagesStorage.getInstance(this.currentAccount).putMessages(tL_messages_savedDialogs.messages, false, true, false, 0, false, 3, 0L);
            for (int i = 0; i < tL_messages_savedDialogs.dialogs.size(); i++) {
                SavedDialog fromTL = SavedDialog.fromTL(this.currentAccount, tL_messages_savedDialogs.dialogs.get(i), tL_messages_savedDialogs.messages);
                int i2 = 0;
                while (true) {
                    if (i2 >= this.cachedDialogs.size()) {
                        break;
                    } else if (this.cachedDialogs.get(i2).dialogId == fromTL.dialogId) {
                        fromTL.messagesCount = this.cachedDialogs.get(i2).messagesCount;
                        this.cachedDialogs.get(i2).pinned = fromTL.pinned;
                        break;
                    } else {
                        i2++;
                    }
                }
                int i3 = 0;
                while (true) {
                    if (i3 >= this.loadedDialogs.size()) {
                        this.loadedDialogs.add(fromTL);
                        if (fromTL.isHidden()) {
                            this.dialogsCountHidden++;
                        }
                    } else if (this.loadedDialogs.get(i3).dialogId == fromTL.dialogId) {
                        break;
                    } else {
                        i3++;
                    }
                }
            }
            this.dialogsEndReached = true;
            this.dialogsCount = tL_messages_savedDialogs.dialogs.size();
        } else if (!(tLObject instanceof TLRPC.TL_messages_savedDialogsSlice)) {
            if (!(tLObject instanceof TLRPC.TL_messages_savedDialogsNotModified)) {
                if (tL_error != null) {
                    this.dialogsLoaded = true;
                    if ("SAVED_DIALOGS_UNSUPPORTED".equals(tL_error.text)) {
                        this.unsupported = true;
                    }
                }
                if (this.unsupported != z) {
                }
                this.dialogsLoading = false;
            }
            this.dialogsLoaded = true;
            this.loadedDialogs.addAll(arrayList);
            this.dialogsCount = ((TLRPC.TL_messages_savedDialogsNotModified) tLObject).count;
            this.dialogsCountHidden = 0;
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                if (((SavedDialog) arrayList.get(i4)).isHidden()) {
                    this.dialogsCountHidden++;
                }
            }
            this.dialogsEndReached = this.loadedDialogs.size() >= this.dialogsCount;
            this.unsupported = false;
            if (this.unsupported != z) {
                MessagesController.getMainSettings(this.currentAccount).edit().putBoolean("savedMessagesUnsupported", this.unsupported).apply();
            }
            this.dialogsLoading = false;
        } else {
            this.dialogsLoaded = true;
            TLRPC.TL_messages_savedDialogsSlice tL_messages_savedDialogsSlice = (TLRPC.TL_messages_savedDialogsSlice) tLObject;
            MessagesController.getInstance(this.currentAccount).putUsers(tL_messages_savedDialogsSlice.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(tL_messages_savedDialogsSlice.chats, false);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(tL_messages_savedDialogsSlice.users, tL_messages_savedDialogsSlice.chats, true, true);
            MessagesStorage.getInstance(this.currentAccount).putMessages(tL_messages_savedDialogsSlice.messages, false, true, false, 0, false, 3, 0L);
            for (int i5 = 0; i5 < tL_messages_savedDialogsSlice.dialogs.size(); i5++) {
                SavedDialog fromTL2 = SavedDialog.fromTL(this.currentAccount, tL_messages_savedDialogsSlice.dialogs.get(i5), tL_messages_savedDialogsSlice.messages);
                int i6 = 0;
                while (true) {
                    if (i6 >= this.cachedDialogs.size()) {
                        break;
                    } else if (this.cachedDialogs.get(i6).dialogId == fromTL2.dialogId) {
                        fromTL2.messagesCount = this.cachedDialogs.get(i6).messagesCount;
                        this.cachedDialogs.get(i6).pinned = fromTL2.pinned;
                        break;
                    } else {
                        i6++;
                    }
                }
                int i7 = 0;
                while (true) {
                    if (i7 >= this.loadedDialogs.size()) {
                        this.loadedDialogs.add(fromTL2);
                        if (fromTL2.isHidden()) {
                            this.dialogsCountHidden++;
                        }
                    } else if (this.loadedDialogs.get(i7).dialogId == fromTL2.dialogId) {
                        break;
                    } else {
                        i7++;
                    }
                }
            }
            this.dialogsCount = tL_messages_savedDialogsSlice.count;
            this.dialogsEndReached = getPinnedCount() + this.loadedDialogs.size() >= this.dialogsCount || tL_messages_savedDialogsSlice.dialogs.size() == 0;
        }
        updateAllDialogs(true);
        saveCacheSchedule();
        this.unsupported = false;
        if (this.unsupported != z) {
        }
        this.dialogsLoading = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDialogs$3(final ArrayList arrayList, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                SavedMessagesController.this.lambda$loadDialogs$2(tLObject, arrayList, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveCache$10() {
        this.saving = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0072, code lost:
        if (r0 == null) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$saveCache$11(MessagesStorage messagesStorage, ArrayList arrayList) {
        SQLiteDatabase database = messagesStorage.getDatabase();
        SQLitePreparedStatement sQLitePreparedStatement = null;
        try {
            try {
                database.executeFast("DELETE FROM saved_dialogs").stepThis().dispose();
                sQLitePreparedStatement = database.executeFast("REPLACE INTO saved_dialogs VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                for (int i = 0; i < arrayList.size(); i++) {
                    SavedDialog savedDialog = (SavedDialog) arrayList.get(i);
                    sQLitePreparedStatement.requery();
                    sQLitePreparedStatement.bindLong(1, savedDialog.dialogId);
                    sQLitePreparedStatement.bindInteger(2, savedDialog.getDate());
                    sQLitePreparedStatement.bindInteger(3, savedDialog.top_message_id);
                    sQLitePreparedStatement.bindInteger(4, savedDialog.pinned ? i : 999);
                    sQLitePreparedStatement.bindInteger(5, savedDialog.messagesCountLoaded ? 1 : 0);
                    sQLitePreparedStatement.bindInteger(6, 0);
                    sQLitePreparedStatement.bindInteger(7, 0);
                    sQLitePreparedStatement.bindInteger(8, savedDialog.messagesCount);
                    sQLitePreparedStatement.step();
                }
                sQLitePreparedStatement.dispose();
            } catch (Exception e) {
                FileLog.e(e);
            }
            sQLitePreparedStatement.dispose();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    SavedMessagesController.this.lambda$saveCache$10();
                }
            });
        } catch (Throwable th) {
            if (sQLitePreparedStatement != null) {
                sQLitePreparedStatement.dispose();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$updateAllDialogs$0(SavedDialog savedDialog, SavedDialog savedDialog2) {
        return savedDialog2.getDate() - savedDialog.getDate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsLastMessage$8(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, LongSparseArray longSparseArray) {
        MessagesController.getInstance(this.currentAccount).putUsers(arrayList, true);
        MessagesController.getInstance(this.currentAccount).putChats(arrayList2, true);
        AnimatedEmojiDrawable.getDocumentFetcher(this.currentAccount).processDocuments(arrayList3);
        for (int i = 0; i < arrayList4.size(); i++) {
            removeDialog(((Long) arrayList4.get(i)).longValue());
        }
        for (int i2 = 0; i2 < longSparseArray.size(); i2++) {
            long keyAt = longSparseArray.keyAt(i2);
            MessageObject messageObject = new MessageObject(this.currentAccount, (TLRPC.Message) longSparseArray.valueAt(i2), null, null, null, null, null, false, false, 0L, false, false, true);
            for (int i3 = 0; i3 < this.loadedDialogs.size(); i3++) {
                SavedDialog savedDialog = this.loadedDialogs.get(i3);
                if (savedDialog.dialogId == keyAt) {
                    savedDialog.top_message_id = messageObject.getId();
                    savedDialog.message = messageObject;
                }
            }
            for (int i4 = 0; i4 < this.cachedDialogs.size(); i4++) {
                SavedDialog savedDialog2 = this.cachedDialogs.get(i4);
                if (savedDialog2.dialogId == keyAt) {
                    savedDialog2.top_message_id = messageObject.getId();
                    savedDialog2.message = messageObject;
                }
            }
        }
        update();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00b2, code lost:
        if (r11 != null) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00b8, code lost:
        if (r11 == null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00ba, code lost:
        r11.dispose();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00bd, code lost:
        org.telegram.messenger.AndroidUtilities.runOnUIThread(new org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda3(r18, r6, r7, r10, r8, r9));
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00cb, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$updateDialogsLastMessage$9(MessagesStorage messagesStorage, ArrayList arrayList, long j) {
        SQLiteDatabase database = messagesStorage.getDatabase();
        final ArrayList arrayList2 = new ArrayList();
        final LongSparseArray longSparseArray = new LongSparseArray();
        ArrayList<Long> arrayList3 = new ArrayList<>();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        final ArrayList<TLRPC.User> arrayList6 = new ArrayList<>();
        final ArrayList<TLRPC.Chat> arrayList7 = new ArrayList<>();
        final ArrayList<TLRPC.Document> arrayList8 = new ArrayList<>();
        SQLiteCursor sQLiteCursor = null;
        int i = 0;
        while (i < arrayList.size()) {
            try {
                try {
                    SavedDialog savedDialog = (SavedDialog) arrayList.get(i);
                    int i2 = i;
                    sQLiteCursor = database.queryFinalized("SELECT mid, data FROM messages_topics WHERE uid = ? AND topic_id = ? ORDER BY mid DESC LIMIT 1", Long.valueOf(j), Long.valueOf(savedDialog.dialogId));
                    if (sQLiteCursor.next()) {
                        sQLiteCursor.intValue(0);
                        NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(1);
                        TLRPC.Message TLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(true), true);
                        MessagesStorage.addUsersAndChatsFromMessage(TLdeserialize, arrayList3, arrayList4, arrayList5);
                        longSparseArray.put(savedDialog.dialogId, TLdeserialize);
                    } else {
                        arrayList2.add(Long.valueOf(savedDialog.dialogId));
                    }
                    sQLiteCursor.dispose();
                    i = i2 + 1;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            } catch (Throwable th) {
                if (sQLiteCursor != null) {
                    sQLiteCursor.dispose();
                }
                throw th;
            }
        }
        if (!arrayList3.isEmpty()) {
            messagesStorage.getUsersInternal(arrayList3, arrayList6);
        }
        if (!arrayList4.isEmpty()) {
            messagesStorage.getChatsInternal(TextUtils.join(",", arrayList4), arrayList7);
        }
        if (!arrayList5.isEmpty()) {
            messagesStorage.getAnimatedEmoji(TextUtils.join(",", arrayList5), arrayList8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$updatePinnedOrder$4(SavedDialog savedDialog, SavedDialog savedDialog2) {
        return savedDialog2.getDate() - savedDialog.getDate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$updatePinnedOrder$5(SavedDialog savedDialog, SavedDialog savedDialog2) {
        return savedDialog.pinnedOrder - savedDialog2.pinnedOrder;
    }

    private void loadCache(final Runnable runnable) {
        if (this.loadingCache) {
            return;
        }
        this.loadingCache = true;
        final long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SavedMessagesController.this.lambda$loadCache$7(messagesStorage, clientUserId, runnable);
            }
        });
    }

    public static void openSavedMessages() {
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        if (lastFragment == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(lastFragment.getCurrentAccount()).getClientUserId());
        lastFragment.presentFragment(new ChatActivity(bundle));
    }

    private boolean processUpdateInternal(TLRPC.Update update) {
        if (update instanceof TLRPC.TL_updateSavedDialogPinned) {
            TLRPC.TL_updateSavedDialogPinned tL_updateSavedDialogPinned = (TLRPC.TL_updateSavedDialogPinned) update;
            TLRPC.DialogPeer dialogPeer = tL_updateSavedDialogPinned.peer;
            if (dialogPeer instanceof TLRPC.TL_dialogPeer) {
                long peerDialogId = DialogObject.getPeerDialogId(((TLRPC.TL_dialogPeer) dialogPeer).peer);
                ArrayList<Long> arrayList = new ArrayList<>();
                arrayList.add(Long.valueOf(peerDialogId));
                return updatePinned(arrayList, tL_updateSavedDialogPinned.pinned, false);
            }
            return false;
        } else if (update instanceof TLRPC.TL_updatePinnedSavedDialogs) {
            TLRPC.TL_updatePinnedSavedDialogs tL_updatePinnedSavedDialogs = (TLRPC.TL_updatePinnedSavedDialogs) update;
            ArrayList<Long> arrayList2 = new ArrayList<>(tL_updatePinnedSavedDialogs.order.size());
            for (int i = 0; i < tL_updatePinnedSavedDialogs.order.size(); i++) {
                TLRPC.DialogPeer dialogPeer2 = tL_updatePinnedSavedDialogs.order.get(i);
                if (dialogPeer2 instanceof TLRPC.TL_dialogPeer) {
                    arrayList2.add(Long.valueOf(DialogObject.getPeerDialogId(((TLRPC.TL_dialogPeer) dialogPeer2).peer)));
                }
            }
            return updatePinnedOrder(this.loadedDialogs, arrayList2) || updatePinnedOrder(this.cachedDialogs, arrayList2);
        } else {
            return false;
        }
    }

    private int removeDialog(long j) {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.allDialogs.size()) {
            if (this.allDialogs.get(i2).dialogId == j) {
                this.allDialogs.remove(i2);
                i3++;
                i2--;
            }
            i2++;
        }
        int i4 = 0;
        int i5 = 0;
        while (i4 < this.loadedDialogs.size()) {
            if (this.loadedDialogs.get(i4).dialogId == j) {
                this.loadedDialogs.remove(i4);
                i5++;
                i4--;
            }
            i4++;
        }
        while (i < this.cachedDialogs.size()) {
            if (this.cachedDialogs.get(i).dialogId == j) {
                this.cachedDialogs.remove(i);
                i--;
            }
            i++;
        }
        return Math.max(i3, i5);
    }

    private boolean sameOrder(ArrayList<Long> arrayList, ArrayList<Long> arrayList2) {
        if (arrayList.size() != arrayList2.size()) {
            return false;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (!Objects.equals(arrayList.get(i), arrayList2.get(i))) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveCache() {
        if (this.saving) {
            return;
        }
        this.saving = true;
        final ArrayList arrayList = new ArrayList(this.allDialogs);
        final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                SavedMessagesController.this.lambda$saveCache$11(messagesStorage, arrayList);
            }
        });
    }

    private void saveCacheSchedule() {
        AndroidUtilities.cancelRunOnUIThread(this.saveCacheRunnable);
        AndroidUtilities.runOnUIThread(this.saveCacheRunnable, 450L);
    }

    private void updateAllDialogs(boolean z) {
        this.allDialogs.clear();
        HashSet hashSet = new HashSet();
        for (int i = 0; i < this.cachedDialogs.size(); i++) {
            SavedDialog savedDialog = this.cachedDialogs.get(i);
            if (savedDialog.pinned && !hashSet.contains(Long.valueOf(savedDialog.dialogId)) && !savedDialog.isHidden()) {
                this.allDialogs.add(savedDialog);
                hashSet.add(Long.valueOf(savedDialog.dialogId));
            }
        }
        for (int i2 = 0; i2 < this.loadedDialogs.size(); i2++) {
            SavedDialog savedDialog2 = this.loadedDialogs.get(i2);
            if (savedDialog2.pinned && !hashSet.contains(Long.valueOf(savedDialog2.dialogId)) && !savedDialog2.isHidden()) {
                this.allDialogs.add(savedDialog2);
                hashSet.add(Long.valueOf(savedDialog2.dialogId));
            }
        }
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < this.loadedDialogs.size(); i3++) {
            SavedDialog savedDialog3 = this.loadedDialogs.get(i3);
            if (!hashSet.contains(Long.valueOf(savedDialog3.dialogId)) && !savedDialog3.isHidden()) {
                arrayList.add(savedDialog3);
                hashSet.add(Long.valueOf(savedDialog3.dialogId));
            }
        }
        if (!this.dialogsEndReached) {
            for (int i4 = 0; i4 < this.cachedDialogs.size(); i4++) {
                SavedDialog savedDialog4 = this.cachedDialogs.get(i4);
                if (!hashSet.contains(Long.valueOf(savedDialog4.dialogId)) && !savedDialog4.isHidden()) {
                    arrayList.add(savedDialog4);
                    hashSet.add(Long.valueOf(savedDialog4.dialogId));
                }
            }
        }
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda6
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$updateAllDialogs$0;
                lambda$updateAllDialogs$0 = SavedMessagesController.lambda$updateAllDialogs$0((SavedMessagesController.SavedDialog) obj, (SavedMessagesController.SavedDialog) obj2);
                return lambda$updateAllDialogs$0;
            }
        });
        this.allDialogs.addAll(arrayList);
        if (z) {
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.savedMessagesDialogsUpdate, new Object[0]);
            if (hasDialogs() || !MessagesController.getInstance(this.currentAccount).savedViewAsChats) {
                return;
            }
            MessagesController.getInstance(this.currentAccount).setSavedViewAs(false);
        }
    }

    private void updateDialogsLastMessage(final ArrayList<SavedDialog> arrayList) {
        final long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        messagesStorage.getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                SavedMessagesController.this.lambda$updateDialogsLastMessage$9(messagesStorage, arrayList, clientUserId);
            }
        });
    }

    private boolean updatePinnedOrder(ArrayList<SavedDialog> arrayList, ArrayList<Long> arrayList2) {
        if (sameOrder(arrayList2, getCurrentPinnedOrder(arrayList))) {
            return false;
        }
        ArrayList arrayList3 = new ArrayList();
        int i = 0;
        while (i < arrayList.size()) {
            SavedDialog savedDialog = arrayList.get(i);
            if (savedDialog.pinned) {
                savedDialog.pinned = false;
                arrayList3.add(savedDialog);
                arrayList.remove(i);
                i--;
            }
            i++;
        }
        arrayList.addAll(arrayList3);
        ArrayList arrayList4 = new ArrayList();
        int i2 = 0;
        while (i2 < arrayList.size()) {
            SavedDialog savedDialog2 = arrayList.get(i2);
            int indexOf = arrayList2.indexOf(Long.valueOf(savedDialog2.dialogId));
            if (indexOf >= 0) {
                savedDialog2.pinnedOrder = indexOf;
                savedDialog2.pinned = true;
                arrayList4.add(savedDialog2);
                arrayList.remove(i2);
                i2--;
            }
            i2++;
        }
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda13
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$updatePinnedOrder$4;
                lambda$updatePinnedOrder$4 = SavedMessagesController.lambda$updatePinnedOrder$4((SavedMessagesController.SavedDialog) obj, (SavedMessagesController.SavedDialog) obj2);
                return lambda$updatePinnedOrder$4;
            }
        });
        Collections.sort(arrayList4, new Comparator() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda14
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$updatePinnedOrder$5;
                lambda$updatePinnedOrder$5 = SavedMessagesController.lambda$updatePinnedOrder$5((SavedMessagesController.SavedDialog) obj, (SavedMessagesController.SavedDialog) obj2);
                return lambda$updatePinnedOrder$5;
            }
        });
        arrayList.addAll(0, arrayList4);
        return true;
    }

    private void updatePinnedOrderToServer(ArrayList<Long> arrayList) {
        boolean updatePinnedOrder = updatePinnedOrder(this.loadedDialogs, arrayList);
        boolean updatePinnedOrder2 = updatePinnedOrder(this.cachedDialogs, arrayList);
        if (updatePinnedOrder || updatePinnedOrder2) {
            TLRPC.TL_messages_reorderPinnedSavedDialogs tL_messages_reorderPinnedSavedDialogs = new TLRPC.TL_messages_reorderPinnedSavedDialogs();
            tL_messages_reorderPinnedSavedDialogs.force = true;
            for (int i = 0; i < arrayList.size(); i++) {
                long longValue = arrayList.get(i).longValue();
                TLRPC.TL_inputDialogPeer tL_inputDialogPeer = new TLRPC.TL_inputDialogPeer();
                TLRPC.InputPeer inputPeer = MessagesController.getInstance(this.currentAccount).getInputPeer(longValue);
                tL_inputDialogPeer.peer = inputPeer;
                if (inputPeer != null) {
                    tL_messages_reorderPinnedSavedDialogs.order.add(tL_inputDialogPeer);
                }
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_reorderPinnedSavedDialogs, null);
            update();
        }
    }

    public void checkSavedDialogCount(long j) {
        SavedDialog findSavedDialog = findSavedDialog(j);
        if (findSavedDialog == null || findSavedDialog.messagesCountLoaded) {
            return;
        }
        hasSavedMessages(j, null);
    }

    public void cleanup() {
        this.cachedDialogs.clear();
        this.loadedDialogs.clear();
        this.dialogsLoaded = false;
        this.dialogsCount = 0;
        this.dialogsCountHidden = 0;
        this.dialogsEndReached = false;
        this.loadedCache = true;
        deleteCache();
        this.unsupported = true;
        MessagesController.getMainSettings(this.currentAccount).edit().remove("savedMessagesUnsupported").apply();
    }

    public boolean containsDialog(long j) {
        for (int i = 0; i < this.allDialogs.size(); i++) {
            if (this.allDialogs.get(i).dialogId == j) {
                return true;
            }
        }
        return false;
    }

    public void deleteAllDialogs() {
        this.dialogsCount = 0;
        this.allDialogs.clear();
        this.loadedDialogs.clear();
        this.cachedDialogs.clear();
        update();
    }

    public void deleteDialog(long j) {
        this.dialogsCount -= removeDialog(j);
        update();
    }

    public void deleteDialogs(ArrayList<Long> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            this.dialogsCount -= removeDialog(arrayList.get(i).longValue());
        }
        update();
    }

    public SavedDialog findSavedDialog(long j) {
        return findSavedDialog(this.allDialogs, j);
    }

    public SavedDialog findSavedDialog(ArrayList<SavedDialog> arrayList, long j) {
        for (int i = 0; i < arrayList.size(); i++) {
            SavedDialog savedDialog = arrayList.get(i);
            if (savedDialog.dialogId == j) {
                return savedDialog;
            }
        }
        return null;
    }

    public int getAllCount() {
        return this.dialogsEndReached ? this.allDialogs.size() : this.dialogsLoaded ? this.dialogsCount - this.dialogsCountHidden : this.cachedDialogs.size();
    }

    public int getLoadedCount() {
        return this.loadedDialogs.size();
    }

    public int getMessagesCount(long j) {
        for (int i = 0; i < this.allDialogs.size(); i++) {
            SavedDialog savedDialog = this.allDialogs.get(i);
            if (savedDialog.dialogId == j) {
                return savedDialog.messagesCount;
            }
        }
        return 0;
    }

    public int getPinnedCount() {
        int i = 0;
        for (int i2 = 0; i2 < this.allDialogs.size(); i2++) {
            if (this.allDialogs.get(i2).pinned) {
                i++;
            }
        }
        return i;
    }

    public boolean hasDialogs() {
        if (getAllCount() <= 0) {
            return false;
        }
        return (this.allDialogs.size() == 1 && this.allDialogs.get(0).dialogId == UserConfig.getInstance(this.currentAccount).getClientUserId()) ? false : true;
    }

    public void hasSavedMessages(final long j, Utilities.Callback<Boolean> callback) {
        SavedDialog findSavedDialog = findSavedDialog(j);
        if (findSavedDialog != null && findSavedDialog.messagesCount > 0 && findSavedDialog.messagesCountLoaded) {
            if (callback != null) {
                callback.run(Boolean.TRUE);
                return;
            }
            return;
        }
        ArrayList arrayList = (ArrayList) this.checkMessagesCallbacks.get(j);
        if (arrayList != null) {
            if (callback != null) {
                arrayList.add(callback);
                return;
            }
            return;
        }
        ArrayList arrayList2 = new ArrayList();
        if (callback != null) {
            arrayList2.add(callback);
        }
        this.checkMessagesCallbacks.put(j, arrayList2);
        TLRPC.TL_messages_getSavedHistory tL_messages_getSavedHistory = new TLRPC.TL_messages_getSavedHistory();
        tL_messages_getSavedHistory.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(j);
        tL_messages_getSavedHistory.limit = 1;
        tL_messages_getSavedHistory.hash = 0L;
        tL_messages_getSavedHistory.offset_id = ConnectionsManager.DEFAULT_DATACENTER_ID;
        tL_messages_getSavedHistory.offset_date = ConnectionsManager.DEFAULT_DATACENTER_ID;
        tL_messages_getSavedHistory.add_offset = -1;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getSavedHistory, new RequestDelegate() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda4
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SavedMessagesController.this.lambda$hasSavedMessages$15(j, tLObject, tL_error);
            }
        });
    }

    public void loadDialogs(boolean z) {
        SavedDialog savedDialog;
        TLRPC.InputPeer tL_inputPeerEmpty;
        this.loadingCacheOnly = z;
        if (this.dialogsLoading || this.dialogsEndReached || this.loadingCache) {
            return;
        }
        if (!this.loadedCache) {
            loadCache(new Runnable() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    SavedMessagesController.this.lambda$loadDialogs$1();
                }
            });
        } else if (!z) {
            this.dialogsLoading = true;
            TLRPC.TL_messages_getSavedDialogs tL_messages_getSavedDialogs = new TLRPC.TL_messages_getSavedDialogs();
            if (this.loadedDialogs.isEmpty()) {
                savedDialog = null;
            } else {
                ArrayList<SavedDialog> arrayList = this.loadedDialogs;
                savedDialog = arrayList.get(arrayList.size() - 1);
            }
            if (savedDialog != null) {
                tL_messages_getSavedDialogs.offset_id = savedDialog.top_message_id;
                tL_messages_getSavedDialogs.offset_date = savedDialog.getDate();
                tL_inputPeerEmpty = MessagesController.getInstance(this.currentAccount).getInputPeer(savedDialog.dialogId);
            } else {
                tL_messages_getSavedDialogs.offset_id = ConnectionsManager.DEFAULT_DATACENTER_ID;
                tL_messages_getSavedDialogs.offset_date = 0;
                tL_inputPeerEmpty = new TLRPC.TL_inputPeerEmpty();
            }
            tL_messages_getSavedDialogs.offset_peer = tL_inputPeerEmpty;
            tL_messages_getSavedDialogs.limit = 20;
            final ArrayList arrayList2 = new ArrayList();
            arrayList2.addAll(this.allDialogs.subList(Math.min(this.loadedDialogs.size(), this.allDialogs.size()), Math.min(this.loadedDialogs.size() + tL_messages_getSavedDialogs.limit, this.allDialogs.size())));
            for (int i = 0; i < arrayList2.size(); i++) {
                SavedDialog savedDialog2 = (SavedDialog) arrayList2.get(i);
                long calcHash = MediaDataController.calcHash(tL_messages_getSavedDialogs.hash, savedDialog2.pinned ? 1L : 0L);
                tL_messages_getSavedDialogs.hash = calcHash;
                long calcHash2 = MediaDataController.calcHash(calcHash, Math.abs(savedDialog2.dialogId));
                tL_messages_getSavedDialogs.hash = calcHash2;
                long calcHash3 = MediaDataController.calcHash(calcHash2, savedDialog2.top_message_id);
                tL_messages_getSavedDialogs.hash = calcHash3;
                tL_messages_getSavedDialogs.hash = MediaDataController.calcHash(calcHash3, savedDialog2.getDate());
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getSavedDialogs, new RequestDelegate() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda9
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    SavedMessagesController.this.lambda$loadDialogs$3(arrayList2, tLObject, tL_error);
                }
            });
        }
    }

    public void preloadDialogs(boolean z) {
        if (this.dialogsLoaded) {
            return;
        }
        loadDialogs(z);
    }

    public void processUpdate(TLRPC.Update update) {
        if (processUpdateInternal(update)) {
            update();
        }
    }

    public ArrayList<SavedDialog> searchDialogs(String str) {
        String str2;
        ArrayList<SavedDialog> arrayList = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return arrayList;
        }
        String translitSafe = AndroidUtilities.translitSafe(str.toLowerCase());
        for (int i = 0; i < this.allDialogs.size(); i++) {
            SavedDialog savedDialog = this.allDialogs.get(i);
            long j = savedDialog.dialogId;
            String str3 = null;
            if (j == UserObject.ANONYMOUS) {
                str2 = LocaleController.getString(R.string.AnonymousForward);
            } else if (j == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                str2 = LocaleController.getString(R.string.MyNotes);
                str3 = LocaleController.getString(R.string.SavedMessages);
            } else {
                int i2 = (savedDialog.dialogId > 0L ? 1 : (savedDialog.dialogId == 0L ? 0 : -1));
                MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                long j2 = savedDialog.dialogId;
                if (i2 >= 0) {
                    str2 = UserObject.getUserName(messagesController.getUser(Long.valueOf(j2)));
                } else {
                    TLRPC.Chat chat = messagesController.getChat(Long.valueOf(-j2));
                    str2 = chat != null ? chat.title : "";
                }
            }
            if (str2 != null) {
                String translitSafe2 = AndroidUtilities.translitSafe(str2.toLowerCase());
                if (!translitSafe2.startsWith(translitSafe)) {
                    if (!translitSafe2.contains(" " + translitSafe)) {
                        if (str3 != null) {
                            String translitSafe3 = AndroidUtilities.translitSafe(str3.toLowerCase());
                            if (!translitSafe3.startsWith(translitSafe)) {
                                if (!translitSafe3.contains(" " + translitSafe)) {
                                }
                            }
                        }
                    }
                }
                arrayList.add(savedDialog);
            }
        }
        return arrayList;
    }

    public void update() {
        updateAllDialogs(true);
        saveCacheSchedule();
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0025, code lost:
        if (r0 == false) goto L6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0028, code lost:
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0012, code lost:
        if (r0 == false) goto L6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void update(long j, TLRPC.messages_Messages messages_messages) {
        boolean updateSavedDialogs = updateSavedDialogs(messages_messages.messages);
        if (messages_messages instanceof TLRPC.TL_messages_messagesSlice) {
            if (!updatedDialogCount(j, messages_messages.count)) {
            }
            updateSavedDialogs = true;
        } else if (messages_messages instanceof TLRPC.TL_messages_messages) {
            if (!updatedDialogCount(j, messages_messages.messages.size())) {
            }
            updateSavedDialogs = true;
        }
        if (updateSavedDialogs) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SavedMessagesController$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    SavedMessagesController.this.update();
                }
            });
        }
    }

    public void updateDeleted(LongSparseArray longSparseArray) {
        SavedDialog savedDialog;
        ArrayList<SavedDialog> arrayList = new ArrayList<>();
        boolean z = false;
        for (int i = 0; i < longSparseArray.size(); i++) {
            long keyAt = longSparseArray.keyAt(i);
            ArrayList arrayList2 = (ArrayList) longSparseArray.valueAt(i);
            int i2 = 0;
            for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                i2 = Math.max(i2, ((Integer) arrayList2.get(i3)).intValue());
            }
            int i4 = 0;
            while (true) {
                if (i4 >= this.allDialogs.size()) {
                    savedDialog = null;
                    break;
                } else if (this.allDialogs.get(i4).dialogId == keyAt) {
                    savedDialog = this.allDialogs.get(i4);
                    break;
                } else {
                    i4++;
                }
            }
            if (savedDialog != null) {
                if (savedDialog.messagesCountLoaded) {
                    int max = Math.max(0, savedDialog.messagesCount - arrayList2.size());
                    int i5 = savedDialog.messagesCount;
                    if (max != i5) {
                        savedDialog.messagesCount = Math.max(0, i5 - arrayList2.size());
                        z = true;
                    }
                }
                if (savedDialog.messagesCountLoaded && savedDialog.messagesCount <= 0) {
                    removeDialog(savedDialog.dialogId);
                } else if (savedDialog.top_message_id <= i2) {
                    arrayList.add(savedDialog);
                }
                z = true;
            }
        }
        if (z) {
            if (arrayList.isEmpty()) {
                update();
            } else {
                updateDialogsLastMessage(arrayList);
            }
        }
    }

    public boolean updatePinned(ArrayList<Long> arrayList, boolean z, boolean z2) {
        ArrayList<Long> currentPinnedOrder = getCurrentPinnedOrder(this.allDialogs);
        ArrayList<Long> arrayList2 = new ArrayList<>(currentPinnedOrder);
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            Long l = arrayList.get(size);
            l.longValue();
            if (z && !arrayList2.contains(l)) {
                arrayList2.add(0, l);
            } else if (!z) {
                arrayList2.remove(l);
            }
        }
        if (arrayList2.size() <= (UserConfig.getInstance(this.currentAccount).isPremium() ? MessagesController.getInstance(this.currentAccount).savedDialogsPinnedLimitPremium : MessagesController.getInstance(this.currentAccount).savedDialogsPinnedLimitDefault) && !sameOrder(currentPinnedOrder, arrayList2)) {
            if (!z2) {
                return updatePinnedOrder(this.loadedDialogs, arrayList2) || updatePinnedOrder(this.cachedDialogs, arrayList2);
            }
            updatePinnedOrderToServer(arrayList2);
            return true;
        }
        return false;
    }

    public boolean updatePinnedOrder(ArrayList<Long> arrayList) {
        ArrayList<Long> currentPinnedOrder = getCurrentPinnedOrder(this.allDialogs);
        if (arrayList.size() > (UserConfig.getInstance(this.currentAccount).isPremium() ? MessagesController.getInstance(this.currentAccount).savedDialogsPinnedLimitPremium : MessagesController.getInstance(this.currentAccount).savedDialogsPinnedLimitDefault)) {
            return false;
        }
        if (sameOrder(currentPinnedOrder, arrayList)) {
            return true;
        }
        updatePinnedOrderToServer(arrayList);
        return true;
    }

    public boolean updateSavedDialog(TLRPC.Message message) {
        if (message == null) {
            return false;
        }
        long savedDialogId = MessageObject.getSavedDialogId(UserConfig.getInstance(this.currentAccount).getClientUserId(), message);
        for (int i = 0; i < this.allDialogs.size(); i++) {
            SavedDialog savedDialog = this.allDialogs.get(i);
            if (savedDialog.dialogId == savedDialogId) {
                MessageObject messageObject = new MessageObject(this.currentAccount, message, false, false);
                savedDialog.message = messageObject;
                savedDialog.top_message_id = messageObject.getId();
                return true;
            }
        }
        return false;
    }

    public boolean updateSavedDialogs(ArrayList<TLRPC.Message> arrayList) {
        boolean z;
        boolean z2;
        if (arrayList == null) {
            return false;
        }
        LongSparseArray longSparseArray = new LongSparseArray();
        LongSparseArray longSparseArray2 = new LongSparseArray();
        new HashSet();
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        for (int i = 0; i < arrayList.size(); i++) {
            TLRPC.Message message = arrayList.get(i);
            long savedDialogId = MessageObject.getSavedDialogId(clientUserId, message);
            if (savedDialogId == clientUserId || (message.id >= 0 && (message.send_state == 0 || message.fwd_from == null))) {
                TLRPC.Message message2 = (TLRPC.Message) longSparseArray.get(savedDialogId);
                if (message2 == null || message2.id < message.id) {
                    longSparseArray.put(savedDialogId, message);
                }
                Integer num = (Integer) longSparseArray2.get(savedDialogId);
                longSparseArray2.put(savedDialogId, Integer.valueOf((num == null ? 0 : num.intValue()) + 1));
            }
        }
        boolean z3 = false;
        for (int i2 = 0; i2 < longSparseArray.size(); i2++) {
            long keyAt = longSparseArray.keyAt(i2);
            TLRPC.Message message3 = (TLRPC.Message) longSparseArray.valueAt(i2);
            Integer num2 = (Integer) longSparseArray2.get(keyAt);
            int i3 = 0;
            while (true) {
                if (i3 >= this.cachedDialogs.size()) {
                    z = false;
                    break;
                }
                SavedDialog savedDialog = this.cachedDialogs.get(i3);
                if (savedDialog.dialogId == keyAt) {
                    int i4 = savedDialog.top_message_id;
                    int i5 = message3.id;
                    if (i4 < i5 || (i5 < 0 && message3.date > savedDialog.getDate())) {
                        if (savedDialog.top_message_id < message3.id) {
                            int i6 = 0;
                            for (int i7 = 0; i7 < arrayList.size(); i7++) {
                                if (arrayList.get(i7).id > savedDialog.top_message_id) {
                                    i6++;
                                }
                            }
                            savedDialog.messagesCount += i6;
                        }
                        MessageObject messageObject = new MessageObject(this.currentAccount, message3, false, false);
                        savedDialog.message = messageObject;
                        savedDialog.top_message_id = messageObject.getId();
                        z3 = true;
                    }
                    z = true;
                } else {
                    i3++;
                }
            }
            if (!z) {
                SavedDialog fromMessage = SavedDialog.fromMessage(this.currentAccount, message3);
                if (num2 != null) {
                    fromMessage.messagesCount = num2.intValue();
                }
                this.cachedDialogs.add(fromMessage);
                z3 = true;
            }
            int i8 = 0;
            while (true) {
                if (i8 >= this.loadedDialogs.size()) {
                    z2 = false;
                    break;
                }
                SavedDialog savedDialog2 = this.loadedDialogs.get(i8);
                if (savedDialog2.dialogId == keyAt) {
                    int i9 = savedDialog2.top_message_id;
                    int i10 = message3.id;
                    if (i9 < i10 || (i10 < 0 && message3.date > savedDialog2.getDate())) {
                        if (savedDialog2.top_message_id < message3.id) {
                            int i11 = 0;
                            for (int i12 = 0; i12 < arrayList.size(); i12++) {
                                if (arrayList.get(i12).id > savedDialog2.top_message_id) {
                                    i11++;
                                }
                            }
                            savedDialog2.messagesCount += i11;
                        }
                        MessageObject messageObject2 = new MessageObject(this.currentAccount, message3, false, false);
                        savedDialog2.message = messageObject2;
                        savedDialog2.top_message_id = messageObject2.getId();
                        z3 = true;
                    }
                    z2 = true;
                } else {
                    i8++;
                }
            }
            if (!z2) {
                SavedDialog fromMessage2 = SavedDialog.fromMessage(this.currentAccount, message3);
                if (num2 != null) {
                    fromMessage2.messagesCount = num2.intValue();
                }
                this.loadedDialogs.add(fromMessage2);
                z3 = true;
            }
        }
        return z3;
    }

    public boolean updatedDialogCount(long j, int i) {
        int i2 = 0;
        while (true) {
            if (i2 >= this.allDialogs.size()) {
                break;
            }
            SavedDialog savedDialog = this.allDialogs.get(i2);
            if (savedDialog.dialogId != j) {
                i2++;
            } else if (savedDialog.messagesCount != i) {
                savedDialog.messagesCount = i;
                savedDialog.messagesCountLoaded = true;
                return true;
            }
        }
        return false;
    }
}
