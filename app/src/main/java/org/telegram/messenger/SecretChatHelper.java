package org.telegram.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLClassStore;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$DecryptedMessage;
import org.telegram.tgnet.TLRPC$DecryptedMessageAction;
import org.telegram.tgnet.TLRPC$DecryptedMessageMedia;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$EncryptedFile;
import org.telegram.tgnet.TLRPC$EncryptedMessage;
import org.telegram.tgnet.TLRPC$GeoPoint;
import org.telegram.tgnet.TLRPC$InputEncryptedFile;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_decryptedMessage;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionAbortKey;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionAcceptKey;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionCommitKey;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionDeleteMessages;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionFlushHistory;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionNoop;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionNotifyLayer;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionReadMessages;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionRequestKey;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionResend;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionScreenshotMessages;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionSetMessageTTL;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageLayer;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaAudio;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaContact;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaDocument_layer8;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaEmpty;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaExternalDocument;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaGeoPoint;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaVenue;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaVideo;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageService;
import org.telegram.tgnet.TLRPC$TL_dialog;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_documentEncrypted;
import org.telegram.tgnet.TLRPC$TL_encryptedChat;
import org.telegram.tgnet.TLRPC$TL_encryptedChatDiscarded;
import org.telegram.tgnet.TLRPC$TL_encryptedChatRequested;
import org.telegram.tgnet.TLRPC$TL_encryptedChatWaiting;
import org.telegram.tgnet.TLRPC$TL_encryptedFile;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_fileEncryptedLocation;
import org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC$TL_geoPoint;
import org.telegram.tgnet.TLRPC$TL_inputEncryptedChat;
import org.telegram.tgnet.TLRPC$TL_messageEncryptedAction;
import org.telegram.tgnet.TLRPC$TL_messageMediaContact;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC$TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_messageReplyHeader;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_message_secret;
import org.telegram.tgnet.TLRPC$TL_messages_acceptEncryption;
import org.telegram.tgnet.TLRPC$TL_messages_dhConfig;
import org.telegram.tgnet.TLRPC$TL_messages_discardEncryption;
import org.telegram.tgnet.TLRPC$TL_messages_getDhConfig;
import org.telegram.tgnet.TLRPC$TL_messages_requestEncryption;
import org.telegram.tgnet.TLRPC$TL_messages_sendEncrypted;
import org.telegram.tgnet.TLRPC$TL_messages_sendEncryptedFile;
import org.telegram.tgnet.TLRPC$TL_messages_sendEncryptedMultiMedia;
import org.telegram.tgnet.TLRPC$TL_messages_sendEncryptedService;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.tgnet.TLRPC$TL_photoCachedSize;
import org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC$TL_photoSize_layer127;
import org.telegram.tgnet.TLRPC$TL_updateEncryption;
import org.telegram.tgnet.TLRPC$TL_updateNewEncryptedMessage;
import org.telegram.tgnet.TLRPC$TL_webPageUrlPending;
import org.telegram.tgnet.TLRPC$Update;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$messages_DhConfig;
import org.telegram.tgnet.TLRPC$messages_SentEncryptedMessage;
import org.telegram.ui.ActionBar.AlertDialog;
/* loaded from: classes.dex */
public class SecretChatHelper extends BaseController {
    public static int CURRENT_SECRET_CHAT_LAYER = 101;
    private static volatile SecretChatHelper[] Instance = new SecretChatHelper[4];
    private ArrayList<Integer> sendingNotifyLayer = new ArrayList<>();
    private SparseArray<ArrayList<TL_decryptedMessageHolder>> secretHolesQueue = new SparseArray<>();
    private SparseArray<ArrayList<TLRPC$Update>> pendingSecretMessages = new SparseArray<>();
    private SparseArray<SparseIntArray> requestedHoles = new SparseArray<>();
    private SparseArray<TLRPC$EncryptedChat> acceptingChats = new SparseArray<>();
    public ArrayList<TLRPC$Update> delayedEncryptedChatUpdates = new ArrayList<>();
    private ArrayList<Long> pendingEncMessagesToDelete = new ArrayList<>();
    private boolean startingSecretChat = false;

    /* loaded from: classes.dex */
    public static class TL_decryptedMessageHolder extends TLObject {
        public static int constructor = 1431655929;
        public int date;
        public int decryptedWithVersion;
        public TLRPC$EncryptedFile file;
        public TLRPC$TL_decryptedMessageLayer layer;
        public boolean new_key_used;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
            abstractSerializedData.readInt64(z);
            this.date = abstractSerializedData.readInt32(z);
            this.layer = TLRPC$TL_decryptedMessageLayer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
            if (abstractSerializedData.readBool(z)) {
                this.file = TLRPC$EncryptedFile.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
            }
            this.new_key_used = abstractSerializedData.readBool(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData abstractSerializedData) {
            abstractSerializedData.writeInt32(constructor);
            abstractSerializedData.writeInt64(0L);
            abstractSerializedData.writeInt32(this.date);
            this.layer.serializeToStream(abstractSerializedData);
            abstractSerializedData.writeBool(this.file != null);
            TLRPC$EncryptedFile tLRPC$EncryptedFile = this.file;
            if (tLRPC$EncryptedFile != null) {
                tLRPC$EncryptedFile.serializeToStream(abstractSerializedData);
            }
            abstractSerializedData.writeBool(this.new_key_used);
        }
    }

    public static SecretChatHelper getInstance(int i) {
        SecretChatHelper secretChatHelper = Instance[i];
        if (secretChatHelper == null) {
            synchronized (SecretChatHelper.class) {
                secretChatHelper = Instance[i];
                if (secretChatHelper == null) {
                    SecretChatHelper[] secretChatHelperArr = Instance;
                    SecretChatHelper secretChatHelper2 = new SecretChatHelper(i);
                    secretChatHelperArr[i] = secretChatHelper2;
                    secretChatHelper = secretChatHelper2;
                }
            }
        }
        return secretChatHelper;
    }

    public SecretChatHelper(int i) {
        super(i);
    }

    public void cleanup() {
        this.sendingNotifyLayer.clear();
        this.acceptingChats.clear();
        this.secretHolesQueue.clear();
        this.pendingSecretMessages.clear();
        this.requestedHoles.clear();
        this.delayedEncryptedChatUpdates.clear();
        this.pendingEncMessagesToDelete.clear();
        this.startingSecretChat = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void processPendingEncMessages() {
        if (!this.pendingEncMessagesToDelete.isEmpty()) {
            final ArrayList arrayList = new ArrayList(this.pendingEncMessagesToDelete);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    SecretChatHelper.this.lambda$processPendingEncMessages$0(arrayList);
                }
            });
            getMessagesStorage().markMessagesAsDeletedByRandoms(new ArrayList<>(this.pendingEncMessagesToDelete));
            this.pendingEncMessagesToDelete.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processPendingEncMessages$0(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = getMessagesController().dialogMessagesByRandomIds.get(((Long) arrayList.get(i)).longValue());
            if (messageObject != null) {
                messageObject.deleted = true;
            }
        }
    }

    private TLRPC$TL_messageService createServiceSecretMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction) {
        TLRPC$TL_messageService tLRPC$TL_messageService = new TLRPC$TL_messageService();
        TLRPC$TL_messageEncryptedAction tLRPC$TL_messageEncryptedAction = new TLRPC$TL_messageEncryptedAction();
        tLRPC$TL_messageService.action = tLRPC$TL_messageEncryptedAction;
        tLRPC$TL_messageEncryptedAction.encryptedAction = tLRPC$DecryptedMessageAction;
        int newMessageId = getUserConfig().getNewMessageId();
        tLRPC$TL_messageService.id = newMessageId;
        tLRPC$TL_messageService.local_id = newMessageId;
        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
        tLRPC$TL_messageService.from_id = tLRPC$TL_peerUser;
        tLRPC$TL_peerUser.user_id = getUserConfig().getClientUserId();
        tLRPC$TL_messageService.unread = true;
        tLRPC$TL_messageService.out = true;
        tLRPC$TL_messageService.flags = 256;
        tLRPC$TL_messageService.dialog_id = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
        tLRPC$TL_messageService.peer_id = new TLRPC$TL_peerUser();
        tLRPC$TL_messageService.send_state = 1;
        if (tLRPC$EncryptedChat.participant_id == getUserConfig().getClientUserId()) {
            tLRPC$TL_messageService.peer_id.user_id = tLRPC$EncryptedChat.admin_id;
        } else {
            tLRPC$TL_messageService.peer_id.user_id = tLRPC$EncryptedChat.participant_id;
        }
        if ((tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionScreenshotMessages) || (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionSetMessageTTL)) {
            tLRPC$TL_messageService.date = getConnectionsManager().getCurrentTime();
        } else {
            tLRPC$TL_messageService.date = 0;
        }
        tLRPC$TL_messageService.random_id = getSendMessagesHelper().getNextRandomId();
        getUserConfig().saveConfig(false);
        ArrayList<TLRPC$Message> arrayList = new ArrayList<>();
        arrayList.add(tLRPC$TL_messageService);
        getMessagesStorage().putMessages(arrayList, false, true, true, 0, false);
        return tLRPC$TL_messageService;
    }

    public void sendMessagesReadMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, ArrayList<Long> arrayList, TLRPC$Message tLRPC$Message) {
        if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat)) {
            return;
        }
        TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = new TLRPC$TL_decryptedMessageService();
        if (tLRPC$Message != null) {
            tLRPC$TL_decryptedMessageService.action = tLRPC$Message.action.encryptedAction;
        } else {
            TLRPC$TL_decryptedMessageActionReadMessages tLRPC$TL_decryptedMessageActionReadMessages = new TLRPC$TL_decryptedMessageActionReadMessages();
            tLRPC$TL_decryptedMessageService.action = tLRPC$TL_decryptedMessageActionReadMessages;
            tLRPC$TL_decryptedMessageActionReadMessages.random_ids = arrayList;
            tLRPC$Message = createServiceSecretMessage(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageActionReadMessages);
        }
        TLRPC$Message tLRPC$Message2 = tLRPC$Message;
        tLRPC$TL_decryptedMessageService.random_id = tLRPC$Message2.random_id;
        performSendEncryptedRequest(tLRPC$TL_decryptedMessageService, tLRPC$Message2, tLRPC$EncryptedChat, null, null, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void processUpdateEncryption(TLRPC$TL_updateEncryption tLRPC$TL_updateEncryption, ConcurrentHashMap<Long, TLRPC$User> concurrentHashMap) {
        byte[] bArr;
        final TLRPC$EncryptedChat tLRPC$EncryptedChat = tLRPC$TL_updateEncryption.chat;
        final long makeEncryptedDialogId = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
        final TLRPC$EncryptedChat encryptedChatDB = getMessagesController().getEncryptedChatDB(tLRPC$EncryptedChat.id, false);
        if ((tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatRequested) && encryptedChatDB == null) {
            long j = tLRPC$EncryptedChat.participant_id;
            if (j == getUserConfig().getClientUserId()) {
                j = tLRPC$EncryptedChat.admin_id;
            }
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(j));
            if (user == null) {
                user = concurrentHashMap.get(Long.valueOf(j));
            }
            tLRPC$EncryptedChat.user_id = j;
            final TLRPC$TL_dialog tLRPC$TL_dialog = new TLRPC$TL_dialog();
            tLRPC$TL_dialog.id = makeEncryptedDialogId;
            tLRPC$TL_dialog.folder_id = tLRPC$EncryptedChat.folder_id;
            tLRPC$TL_dialog.unread_count = 0;
            tLRPC$TL_dialog.top_message = 0;
            tLRPC$TL_dialog.last_message_date = tLRPC$TL_updateEncryption.date;
            getMessagesController().putEncryptedChat(tLRPC$EncryptedChat, false);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    SecretChatHelper.this.lambda$processUpdateEncryption$1(tLRPC$TL_dialog, makeEncryptedDialogId);
                }
            });
            getMessagesStorage().putEncryptedChat(tLRPC$EncryptedChat, user, tLRPC$TL_dialog);
            acceptSecretChat(tLRPC$EncryptedChat);
        } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat) {
            if ((encryptedChatDB instanceof TLRPC$TL_encryptedChatWaiting) && ((bArr = encryptedChatDB.auth_key) == null || bArr.length == 1)) {
                tLRPC$EncryptedChat.a_or_b = encryptedChatDB.a_or_b;
                tLRPC$EncryptedChat.user_id = encryptedChatDB.user_id;
                processAcceptedSecretChat(tLRPC$EncryptedChat);
            } else if (encryptedChatDB == null && this.startingSecretChat) {
                this.delayedEncryptedChatUpdates.add(tLRPC$TL_updateEncryption);
            }
        } else {
            if (encryptedChatDB != null) {
                tLRPC$EncryptedChat.user_id = encryptedChatDB.user_id;
                tLRPC$EncryptedChat.auth_key = encryptedChatDB.auth_key;
                tLRPC$EncryptedChat.key_create_date = encryptedChatDB.key_create_date;
                tLRPC$EncryptedChat.key_use_count_in = encryptedChatDB.key_use_count_in;
                tLRPC$EncryptedChat.key_use_count_out = encryptedChatDB.key_use_count_out;
                tLRPC$EncryptedChat.ttl = encryptedChatDB.ttl;
                tLRPC$EncryptedChat.seq_in = encryptedChatDB.seq_in;
                tLRPC$EncryptedChat.seq_out = encryptedChatDB.seq_out;
                tLRPC$EncryptedChat.admin_id = encryptedChatDB.admin_id;
                tLRPC$EncryptedChat.mtproto_seq = encryptedChatDB.mtproto_seq;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    SecretChatHelper.this.lambda$processUpdateEncryption$2(encryptedChatDB, tLRPC$EncryptedChat);
                }
            });
        }
        if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatDiscarded) || !tLRPC$EncryptedChat.history_deleted) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                SecretChatHelper.this.lambda$processUpdateEncryption$3(makeEncryptedDialogId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processUpdateEncryption$1(TLRPC$Dialog tLRPC$Dialog, long j) {
        if (tLRPC$Dialog.folder_id == 1) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            edit.putBoolean("dialog_bar_archived" + j, true);
            edit.commit();
        }
        getMessagesController().dialogs_dict.put(tLRPC$Dialog.id, tLRPC$Dialog);
        getMessagesController().allDialogs.add(tLRPC$Dialog);
        getMessagesController().sortDialogs(null);
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processUpdateEncryption$2(TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$EncryptedChat tLRPC$EncryptedChat2) {
        if (tLRPC$EncryptedChat != null) {
            getMessagesController().putEncryptedChat(tLRPC$EncryptedChat2, false);
        }
        getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat2);
        getNotificationCenter().postNotificationName(NotificationCenter.encryptedChatUpdated, tLRPC$EncryptedChat2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processUpdateEncryption$3(long j) {
        getMessagesController().deleteDialog(j, 0);
    }

    public void sendMessagesDeleteMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, ArrayList<Long> arrayList, TLRPC$Message tLRPC$Message) {
        if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat)) {
            return;
        }
        TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = new TLRPC$TL_decryptedMessageService();
        if (tLRPC$Message != null) {
            tLRPC$TL_decryptedMessageService.action = tLRPC$Message.action.encryptedAction;
        } else {
            TLRPC$TL_decryptedMessageActionDeleteMessages tLRPC$TL_decryptedMessageActionDeleteMessages = new TLRPC$TL_decryptedMessageActionDeleteMessages();
            tLRPC$TL_decryptedMessageService.action = tLRPC$TL_decryptedMessageActionDeleteMessages;
            tLRPC$TL_decryptedMessageActionDeleteMessages.random_ids = arrayList;
            tLRPC$Message = createServiceSecretMessage(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageActionDeleteMessages);
        }
        TLRPC$Message tLRPC$Message2 = tLRPC$Message;
        tLRPC$TL_decryptedMessageService.random_id = tLRPC$Message2.random_id;
        performSendEncryptedRequest(tLRPC$TL_decryptedMessageService, tLRPC$Message2, tLRPC$EncryptedChat, null, null, null);
    }

    public void sendClearHistoryMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$Message tLRPC$Message) {
        if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat)) {
            return;
        }
        TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = new TLRPC$TL_decryptedMessageService();
        if (tLRPC$Message != null) {
            tLRPC$TL_decryptedMessageService.action = tLRPC$Message.action.encryptedAction;
        } else {
            TLRPC$TL_decryptedMessageActionFlushHistory tLRPC$TL_decryptedMessageActionFlushHistory = new TLRPC$TL_decryptedMessageActionFlushHistory();
            tLRPC$TL_decryptedMessageService.action = tLRPC$TL_decryptedMessageActionFlushHistory;
            tLRPC$Message = createServiceSecretMessage(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageActionFlushHistory);
        }
        TLRPC$Message tLRPC$Message2 = tLRPC$Message;
        tLRPC$TL_decryptedMessageService.random_id = tLRPC$Message2.random_id;
        performSendEncryptedRequest(tLRPC$TL_decryptedMessageService, tLRPC$Message2, tLRPC$EncryptedChat, null, null, null);
    }

    public void sendNotifyLayerMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$Message tLRPC$Message) {
        if ((tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat) && !this.sendingNotifyLayer.contains(Integer.valueOf(tLRPC$EncryptedChat.id))) {
            this.sendingNotifyLayer.add(Integer.valueOf(tLRPC$EncryptedChat.id));
            TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = new TLRPC$TL_decryptedMessageService();
            if (tLRPC$Message != null) {
                tLRPC$TL_decryptedMessageService.action = tLRPC$Message.action.encryptedAction;
            } else {
                TLRPC$TL_decryptedMessageActionNotifyLayer tLRPC$TL_decryptedMessageActionNotifyLayer = new TLRPC$TL_decryptedMessageActionNotifyLayer();
                tLRPC$TL_decryptedMessageService.action = tLRPC$TL_decryptedMessageActionNotifyLayer;
                tLRPC$TL_decryptedMessageActionNotifyLayer.layer = CURRENT_SECRET_CHAT_LAYER;
                tLRPC$Message = createServiceSecretMessage(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageActionNotifyLayer);
            }
            TLRPC$Message tLRPC$Message2 = tLRPC$Message;
            tLRPC$TL_decryptedMessageService.random_id = tLRPC$Message2.random_id;
            performSendEncryptedRequest(tLRPC$TL_decryptedMessageService, tLRPC$Message2, tLRPC$EncryptedChat, null, null, null);
        }
    }

    public void sendRequestKeyMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$Message tLRPC$Message) {
        if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat)) {
            return;
        }
        TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = new TLRPC$TL_decryptedMessageService();
        if (tLRPC$Message != null) {
            tLRPC$TL_decryptedMessageService.action = tLRPC$Message.action.encryptedAction;
        } else {
            TLRPC$TL_decryptedMessageActionRequestKey tLRPC$TL_decryptedMessageActionRequestKey = new TLRPC$TL_decryptedMessageActionRequestKey();
            tLRPC$TL_decryptedMessageService.action = tLRPC$TL_decryptedMessageActionRequestKey;
            tLRPC$TL_decryptedMessageActionRequestKey.exchange_id = tLRPC$EncryptedChat.exchange_id;
            tLRPC$TL_decryptedMessageActionRequestKey.g_a = tLRPC$EncryptedChat.g_a;
            tLRPC$Message = createServiceSecretMessage(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageActionRequestKey);
        }
        TLRPC$Message tLRPC$Message2 = tLRPC$Message;
        tLRPC$TL_decryptedMessageService.random_id = tLRPC$Message2.random_id;
        performSendEncryptedRequest(tLRPC$TL_decryptedMessageService, tLRPC$Message2, tLRPC$EncryptedChat, null, null, null);
    }

    public void sendAcceptKeyMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$Message tLRPC$Message) {
        if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat)) {
            return;
        }
        TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = new TLRPC$TL_decryptedMessageService();
        if (tLRPC$Message != null) {
            tLRPC$TL_decryptedMessageService.action = tLRPC$Message.action.encryptedAction;
        } else {
            TLRPC$TL_decryptedMessageActionAcceptKey tLRPC$TL_decryptedMessageActionAcceptKey = new TLRPC$TL_decryptedMessageActionAcceptKey();
            tLRPC$TL_decryptedMessageService.action = tLRPC$TL_decryptedMessageActionAcceptKey;
            tLRPC$TL_decryptedMessageActionAcceptKey.exchange_id = tLRPC$EncryptedChat.exchange_id;
            tLRPC$TL_decryptedMessageActionAcceptKey.key_fingerprint = tLRPC$EncryptedChat.future_key_fingerprint;
            tLRPC$TL_decryptedMessageActionAcceptKey.g_b = tLRPC$EncryptedChat.g_a_or_b;
            tLRPC$Message = createServiceSecretMessage(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageActionAcceptKey);
        }
        TLRPC$Message tLRPC$Message2 = tLRPC$Message;
        tLRPC$TL_decryptedMessageService.random_id = tLRPC$Message2.random_id;
        performSendEncryptedRequest(tLRPC$TL_decryptedMessageService, tLRPC$Message2, tLRPC$EncryptedChat, null, null, null);
    }

    public void sendCommitKeyMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$Message tLRPC$Message) {
        if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat)) {
            return;
        }
        TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = new TLRPC$TL_decryptedMessageService();
        if (tLRPC$Message != null) {
            tLRPC$TL_decryptedMessageService.action = tLRPC$Message.action.encryptedAction;
        } else {
            TLRPC$TL_decryptedMessageActionCommitKey tLRPC$TL_decryptedMessageActionCommitKey = new TLRPC$TL_decryptedMessageActionCommitKey();
            tLRPC$TL_decryptedMessageService.action = tLRPC$TL_decryptedMessageActionCommitKey;
            tLRPC$TL_decryptedMessageActionCommitKey.exchange_id = tLRPC$EncryptedChat.exchange_id;
            tLRPC$TL_decryptedMessageActionCommitKey.key_fingerprint = tLRPC$EncryptedChat.future_key_fingerprint;
            tLRPC$Message = createServiceSecretMessage(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageActionCommitKey);
        }
        TLRPC$Message tLRPC$Message2 = tLRPC$Message;
        tLRPC$TL_decryptedMessageService.random_id = tLRPC$Message2.random_id;
        performSendEncryptedRequest(tLRPC$TL_decryptedMessageService, tLRPC$Message2, tLRPC$EncryptedChat, null, null, null);
    }

    public void sendAbortKeyMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$Message tLRPC$Message, long j) {
        if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat)) {
            return;
        }
        TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = new TLRPC$TL_decryptedMessageService();
        if (tLRPC$Message != null) {
            tLRPC$TL_decryptedMessageService.action = tLRPC$Message.action.encryptedAction;
        } else {
            TLRPC$TL_decryptedMessageActionAbortKey tLRPC$TL_decryptedMessageActionAbortKey = new TLRPC$TL_decryptedMessageActionAbortKey();
            tLRPC$TL_decryptedMessageService.action = tLRPC$TL_decryptedMessageActionAbortKey;
            tLRPC$TL_decryptedMessageActionAbortKey.exchange_id = j;
            tLRPC$Message = createServiceSecretMessage(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageActionAbortKey);
        }
        TLRPC$Message tLRPC$Message2 = tLRPC$Message;
        tLRPC$TL_decryptedMessageService.random_id = tLRPC$Message2.random_id;
        performSendEncryptedRequest(tLRPC$TL_decryptedMessageService, tLRPC$Message2, tLRPC$EncryptedChat, null, null, null);
    }

    public void sendNoopMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$Message tLRPC$Message) {
        if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat)) {
            return;
        }
        TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = new TLRPC$TL_decryptedMessageService();
        if (tLRPC$Message != null) {
            tLRPC$TL_decryptedMessageService.action = tLRPC$Message.action.encryptedAction;
        } else {
            TLRPC$TL_decryptedMessageActionNoop tLRPC$TL_decryptedMessageActionNoop = new TLRPC$TL_decryptedMessageActionNoop();
            tLRPC$TL_decryptedMessageService.action = tLRPC$TL_decryptedMessageActionNoop;
            tLRPC$Message = createServiceSecretMessage(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageActionNoop);
        }
        TLRPC$Message tLRPC$Message2 = tLRPC$Message;
        tLRPC$TL_decryptedMessageService.random_id = tLRPC$Message2.random_id;
        performSendEncryptedRequest(tLRPC$TL_decryptedMessageService, tLRPC$Message2, tLRPC$EncryptedChat, null, null, null);
    }

    public void sendResendMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, int i, int i2, TLRPC$Message tLRPC$Message) {
        if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat)) {
            return;
        }
        SparseIntArray sparseIntArray = this.requestedHoles.get(tLRPC$EncryptedChat.id);
        if (sparseIntArray != null && sparseIntArray.indexOfKey(i) >= 0) {
            return;
        }
        if (sparseIntArray == null) {
            sparseIntArray = new SparseIntArray();
            this.requestedHoles.put(tLRPC$EncryptedChat.id, sparseIntArray);
        }
        sparseIntArray.put(i, i2);
        TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = new TLRPC$TL_decryptedMessageService();
        if (tLRPC$Message != null) {
            tLRPC$TL_decryptedMessageService.action = tLRPC$Message.action.encryptedAction;
        } else {
            TLRPC$TL_decryptedMessageActionResend tLRPC$TL_decryptedMessageActionResend = new TLRPC$TL_decryptedMessageActionResend();
            tLRPC$TL_decryptedMessageService.action = tLRPC$TL_decryptedMessageActionResend;
            tLRPC$TL_decryptedMessageActionResend.start_seq_no = i;
            tLRPC$TL_decryptedMessageActionResend.end_seq_no = i2;
            tLRPC$Message = createServiceSecretMessage(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageActionResend);
        }
        TLRPC$Message tLRPC$Message2 = tLRPC$Message;
        tLRPC$TL_decryptedMessageService.random_id = tLRPC$Message2.random_id;
        performSendEncryptedRequest(tLRPC$TL_decryptedMessageService, tLRPC$Message2, tLRPC$EncryptedChat, null, null, null);
    }

    public void sendTTLMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$Message tLRPC$Message) {
        if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat)) {
            return;
        }
        TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = new TLRPC$TL_decryptedMessageService();
        if (tLRPC$Message != null) {
            tLRPC$TL_decryptedMessageService.action = tLRPC$Message.action.encryptedAction;
        } else {
            TLRPC$TL_decryptedMessageActionSetMessageTTL tLRPC$TL_decryptedMessageActionSetMessageTTL = new TLRPC$TL_decryptedMessageActionSetMessageTTL();
            tLRPC$TL_decryptedMessageService.action = tLRPC$TL_decryptedMessageActionSetMessageTTL;
            tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds = tLRPC$EncryptedChat.ttl;
            tLRPC$Message = createServiceSecretMessage(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageActionSetMessageTTL);
            MessageObject messageObject = new MessageObject(this.currentAccount, tLRPC$Message, false, false);
            messageObject.messageOwner.send_state = 1;
            messageObject.wasJustSent = true;
            ArrayList<MessageObject> arrayList = new ArrayList<>();
            arrayList.add(messageObject);
            getMessagesController().updateInterfaceWithMessages(tLRPC$Message.dialog_id, arrayList, false);
            getNotificationCenter().postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        }
        TLRPC$Message tLRPC$Message2 = tLRPC$Message;
        tLRPC$TL_decryptedMessageService.random_id = tLRPC$Message2.random_id;
        performSendEncryptedRequest(tLRPC$TL_decryptedMessageService, tLRPC$Message2, tLRPC$EncryptedChat, null, null, null);
    }

    public void sendScreenshotMessage(TLRPC$EncryptedChat tLRPC$EncryptedChat, ArrayList<Long> arrayList, TLRPC$Message tLRPC$Message) {
        if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat)) {
            return;
        }
        TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = new TLRPC$TL_decryptedMessageService();
        if (tLRPC$Message != null) {
            tLRPC$TL_decryptedMessageService.action = tLRPC$Message.action.encryptedAction;
        } else {
            TLRPC$TL_decryptedMessageActionScreenshotMessages tLRPC$TL_decryptedMessageActionScreenshotMessages = new TLRPC$TL_decryptedMessageActionScreenshotMessages();
            tLRPC$TL_decryptedMessageService.action = tLRPC$TL_decryptedMessageActionScreenshotMessages;
            tLRPC$TL_decryptedMessageActionScreenshotMessages.random_ids = arrayList;
            tLRPC$Message = createServiceSecretMessage(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageActionScreenshotMessages);
            MessageObject messageObject = new MessageObject(this.currentAccount, tLRPC$Message, false, false);
            messageObject.messageOwner.send_state = 1;
            messageObject.wasJustSent = true;
            ArrayList<MessageObject> arrayList2 = new ArrayList<>();
            arrayList2.add(messageObject);
            getMessagesController().updateInterfaceWithMessages(tLRPC$Message.dialog_id, arrayList2, false);
            getNotificationCenter().postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        }
        TLRPC$Message tLRPC$Message2 = tLRPC$Message;
        tLRPC$TL_decryptedMessageService.random_id = tLRPC$Message2.random_id;
        performSendEncryptedRequest(tLRPC$TL_decryptedMessageService, tLRPC$Message2, tLRPC$EncryptedChat, null, null, null);
    }

    private void updateMediaPaths(MessageObject messageObject, TLRPC$EncryptedFile tLRPC$EncryptedFile, TLRPC$DecryptedMessage tLRPC$DecryptedMessage, String str) {
        TLRPC$Document tLRPC$Document;
        TLRPC$Photo tLRPC$Photo;
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        if (tLRPC$EncryptedFile != null) {
            TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
            if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$Photo = tLRPC$MessageMedia.photo) != null) {
                ArrayList<TLRPC$PhotoSize> arrayList = tLRPC$Photo.sizes;
                TLRPC$PhotoSize tLRPC$PhotoSize = arrayList.get(arrayList.size() - 1);
                String str2 = tLRPC$PhotoSize.location.volume_id + "_" + tLRPC$PhotoSize.location.local_id;
                TLRPC$TL_fileEncryptedLocation tLRPC$TL_fileEncryptedLocation = new TLRPC$TL_fileEncryptedLocation();
                tLRPC$PhotoSize.location = tLRPC$TL_fileEncryptedLocation;
                TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia = tLRPC$DecryptedMessage.media;
                tLRPC$TL_fileEncryptedLocation.key = tLRPC$DecryptedMessageMedia.key;
                tLRPC$TL_fileEncryptedLocation.iv = tLRPC$DecryptedMessageMedia.iv;
                tLRPC$TL_fileEncryptedLocation.dc_id = tLRPC$EncryptedFile.dc_id;
                tLRPC$TL_fileEncryptedLocation.volume_id = tLRPC$EncryptedFile.id;
                tLRPC$TL_fileEncryptedLocation.secret = tLRPC$EncryptedFile.access_hash;
                tLRPC$TL_fileEncryptedLocation.local_id = tLRPC$EncryptedFile.key_fingerprint;
                new File(FileLoader.getDirectory(4), str2 + ".jpg").renameTo(getFileLoader().getPathToAttach(tLRPC$PhotoSize));
                ImageLoader.getInstance().replaceImageInCache(str2, tLRPC$PhotoSize.location.volume_id + "_" + tLRPC$PhotoSize.location.local_id, ImageLocation.getForPhoto(tLRPC$PhotoSize, tLRPC$Message.media.photo), true);
                ArrayList<TLRPC$Message> arrayList2 = new ArrayList<>();
                arrayList2.add(tLRPC$Message);
                getMessagesStorage().putMessages(arrayList2, false, true, false, 0, false);
            } else if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) || (tLRPC$Document = tLRPC$MessageMedia.document) == null) {
            } else {
                tLRPC$MessageMedia.document = new TLRPC$TL_documentEncrypted();
                TLRPC$Document tLRPC$Document2 = tLRPC$Message.media.document;
                tLRPC$Document2.id = tLRPC$EncryptedFile.id;
                tLRPC$Document2.access_hash = tLRPC$EncryptedFile.access_hash;
                tLRPC$Document2.date = tLRPC$Document.date;
                tLRPC$Document2.attributes = tLRPC$Document.attributes;
                tLRPC$Document2.mime_type = tLRPC$Document.mime_type;
                tLRPC$Document2.size = tLRPC$EncryptedFile.size;
                TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia2 = tLRPC$DecryptedMessage.media;
                tLRPC$Document2.key = tLRPC$DecryptedMessageMedia2.key;
                tLRPC$Document2.iv = tLRPC$DecryptedMessageMedia2.iv;
                ArrayList<TLRPC$PhotoSize> arrayList3 = tLRPC$Document.thumbs;
                tLRPC$Document2.thumbs = arrayList3;
                tLRPC$Document2.dc_id = tLRPC$EncryptedFile.dc_id;
                if (arrayList3.isEmpty()) {
                    TLRPC$TL_photoSizeEmpty tLRPC$TL_photoSizeEmpty = new TLRPC$TL_photoSizeEmpty();
                    tLRPC$TL_photoSizeEmpty.type = "s";
                    tLRPC$Message.media.document.thumbs.add(tLRPC$TL_photoSizeEmpty);
                }
                String str3 = tLRPC$Message.attachPath;
                if (str3 != null && str3.startsWith(FileLoader.getDirectory(4).getAbsolutePath()) && new File(tLRPC$Message.attachPath).renameTo(getFileLoader().getPathToAttach(tLRPC$Message.media.document))) {
                    messageObject.mediaExists = messageObject.attachPathExists;
                    messageObject.attachPathExists = false;
                    tLRPC$Message.attachPath = "";
                }
                ArrayList<TLRPC$Message> arrayList4 = new ArrayList<>();
                arrayList4.add(tLRPC$Message);
                getMessagesStorage().putMessages(arrayList4, false, true, false, 0, false);
            }
        }
    }

    public static boolean isSecretVisibleMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
        if (tLRPC$MessageAction instanceof TLRPC$TL_messageEncryptedAction) {
            TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction = tLRPC$MessageAction.encryptedAction;
            if ((tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionScreenshotMessages) || (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionSetMessageTTL)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSecretInvisibleMessage(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
        if (tLRPC$MessageAction instanceof TLRPC$TL_messageEncryptedAction) {
            TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction = tLRPC$MessageAction.encryptedAction;
            if (!(tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionScreenshotMessages) && !(tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionSetMessageTTL)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void performSendEncryptedRequest(TLRPC$TL_messages_sendEncryptedMultiMedia tLRPC$TL_messages_sendEncryptedMultiMedia, SendMessagesHelper.DelayedMessage delayedMessage) {
        for (int i = 0; i < tLRPC$TL_messages_sendEncryptedMultiMedia.files.size(); i++) {
            performSendEncryptedRequest(tLRPC$TL_messages_sendEncryptedMultiMedia.messages.get(i), delayedMessage.messages.get(i), delayedMessage.encryptedChat, tLRPC$TL_messages_sendEncryptedMultiMedia.files.get(i), delayedMessage.originalPaths.get(i), delayedMessage.messageObjects.get(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void performSendEncryptedRequest(final TLRPC$DecryptedMessage tLRPC$DecryptedMessage, final TLRPC$Message tLRPC$Message, final TLRPC$EncryptedChat tLRPC$EncryptedChat, final TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, final String str, final MessageObject messageObject) {
        if (tLRPC$DecryptedMessage == null || tLRPC$EncryptedChat.auth_key == null || (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatRequested) || (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatWaiting)) {
            return;
        }
        getSendMessagesHelper().putToSendingMessages(tLRPC$Message, false);
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                SecretChatHelper.this.lambda$performSendEncryptedRequest$8(tLRPC$EncryptedChat, tLRPC$DecryptedMessage, tLRPC$Message, tLRPC$InputEncryptedFile, messageObject, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$performSendEncryptedRequest$8(final TLRPC$EncryptedChat tLRPC$EncryptedChat, final TLRPC$DecryptedMessage tLRPC$DecryptedMessage, final TLRPC$Message tLRPC$Message, TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, final MessageObject messageObject, final String str) {
        TLRPC$TL_messages_sendEncryptedFile tLRPC$TL_messages_sendEncryptedFile;
        TLRPC$TL_messages_sendEncryptedFile tLRPC$TL_messages_sendEncryptedFile2;
        try {
            TLRPC$TL_decryptedMessageLayer tLRPC$TL_decryptedMessageLayer = new TLRPC$TL_decryptedMessageLayer();
            tLRPC$TL_decryptedMessageLayer.layer = Math.min(Math.max(46, AndroidUtilities.getMyLayerVersion(tLRPC$EncryptedChat.layer)), Math.max(46, AndroidUtilities.getPeerLayerVersion(tLRPC$EncryptedChat.layer)));
            tLRPC$TL_decryptedMessageLayer.message = tLRPC$DecryptedMessage;
            byte[] bArr = new byte[15];
            tLRPC$TL_decryptedMessageLayer.random_bytes = bArr;
            Utilities.random.nextBytes(bArr);
            boolean z = true;
            if (tLRPC$EncryptedChat.seq_in == 0 && tLRPC$EncryptedChat.seq_out == 0) {
                if (tLRPC$EncryptedChat.admin_id == getUserConfig().getClientUserId()) {
                    tLRPC$EncryptedChat.seq_out = 1;
                    tLRPC$EncryptedChat.seq_in = -2;
                } else {
                    tLRPC$EncryptedChat.seq_in = -1;
                }
            }
            int i = tLRPC$Message.seq_in;
            if (i == 0 && tLRPC$Message.seq_out == 0) {
                int i2 = tLRPC$EncryptedChat.seq_in;
                if (i2 <= 0) {
                    i2 += 2;
                }
                tLRPC$TL_decryptedMessageLayer.in_seq_no = i2;
                int i3 = tLRPC$EncryptedChat.seq_out;
                tLRPC$TL_decryptedMessageLayer.out_seq_no = i3;
                tLRPC$EncryptedChat.seq_out = i3 + 2;
                if (tLRPC$EncryptedChat.key_create_date == 0) {
                    tLRPC$EncryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
                }
                short s = (short) (tLRPC$EncryptedChat.key_use_count_out + 1);
                tLRPC$EncryptedChat.key_use_count_out = s;
                if ((s >= 100 || tLRPC$EncryptedChat.key_create_date < getConnectionsManager().getCurrentTime() - 604800) && tLRPC$EncryptedChat.exchange_id == 0 && tLRPC$EncryptedChat.future_key_fingerprint == 0) {
                    requestNewSecretChatKey(tLRPC$EncryptedChat);
                }
                getMessagesStorage().updateEncryptedChatSeq(tLRPC$EncryptedChat, false);
                tLRPC$Message.seq_in = tLRPC$TL_decryptedMessageLayer.in_seq_no;
                tLRPC$Message.seq_out = tLRPC$TL_decryptedMessageLayer.out_seq_no;
                getMessagesStorage().setMessageSeq(tLRPC$Message.id, tLRPC$Message.seq_in, tLRPC$Message.seq_out);
            } else {
                tLRPC$TL_decryptedMessageLayer.in_seq_no = i;
                tLRPC$TL_decryptedMessageLayer.out_seq_no = tLRPC$Message.seq_out;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d(tLRPC$DecryptedMessage + " send message with in_seq = " + tLRPC$TL_decryptedMessageLayer.in_seq_no + " out_seq = " + tLRPC$TL_decryptedMessageLayer.out_seq_no);
            }
            int objectSize = tLRPC$TL_decryptedMessageLayer.getObjectSize();
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(objectSize + 4);
            nativeByteBuffer.writeInt32(objectSize);
            tLRPC$TL_decryptedMessageLayer.serializeToStream(nativeByteBuffer);
            int length = nativeByteBuffer.length();
            int nextInt = (length % 16 != 0 ? 16 - (length % 16) : 0) + ((Utilities.random.nextInt(3) + 2) * 16);
            NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer(length + nextInt);
            nativeByteBuffer.position(0);
            nativeByteBuffer2.writeBytes(nativeByteBuffer);
            if (nextInt != 0) {
                byte[] bArr2 = new byte[nextInt];
                Utilities.random.nextBytes(bArr2);
                nativeByteBuffer2.writeBytes(bArr2);
            }
            byte[] bArr3 = new byte[16];
            if (tLRPC$EncryptedChat.admin_id == getUserConfig().getClientUserId()) {
                z = false;
            }
            byte[] bArr4 = tLRPC$EncryptedChat.auth_key;
            int i4 = z ? 8 : 0;
            ByteBuffer byteBuffer = nativeByteBuffer2.buffer;
            System.arraycopy(Utilities.computeSHA256(bArr4, i4 + 88, 32, byteBuffer, 0, byteBuffer.limit()), 8, bArr3, 0, 16);
            nativeByteBuffer.reuse();
            MessageKeyData generateMessageKeyData = MessageKeyData.generateMessageKeyData(tLRPC$EncryptedChat.auth_key, bArr3, z, 2);
            Utilities.aesIgeEncryption(nativeByteBuffer2.buffer, generateMessageKeyData.aesKey, generateMessageKeyData.aesIv, true, false, 0, nativeByteBuffer2.limit());
            NativeByteBuffer nativeByteBuffer3 = new NativeByteBuffer(24 + nativeByteBuffer2.length());
            nativeByteBuffer2.position(0);
            nativeByteBuffer3.writeInt64(tLRPC$EncryptedChat.key_fingerprint);
            nativeByteBuffer3.writeBytes(bArr3);
            nativeByteBuffer3.writeBytes(nativeByteBuffer2);
            nativeByteBuffer2.reuse();
            nativeByteBuffer3.position(0);
            if (tLRPC$InputEncryptedFile == null) {
                if (tLRPC$DecryptedMessage instanceof TLRPC$TL_decryptedMessageService) {
                    TLRPC$TL_messages_sendEncryptedService tLRPC$TL_messages_sendEncryptedService = new TLRPC$TL_messages_sendEncryptedService();
                    tLRPC$TL_messages_sendEncryptedService.data = nativeByteBuffer3;
                    tLRPC$TL_messages_sendEncryptedService.random_id = tLRPC$DecryptedMessage.random_id;
                    TLRPC$TL_inputEncryptedChat tLRPC$TL_inputEncryptedChat = new TLRPC$TL_inputEncryptedChat();
                    tLRPC$TL_messages_sendEncryptedService.peer = tLRPC$TL_inputEncryptedChat;
                    tLRPC$TL_inputEncryptedChat.chat_id = tLRPC$EncryptedChat.id;
                    tLRPC$TL_inputEncryptedChat.access_hash = tLRPC$EncryptedChat.access_hash;
                    tLRPC$TL_messages_sendEncryptedFile2 = tLRPC$TL_messages_sendEncryptedService;
                } else {
                    TLRPC$TL_messages_sendEncrypted tLRPC$TL_messages_sendEncrypted = new TLRPC$TL_messages_sendEncrypted();
                    tLRPC$TL_messages_sendEncrypted.silent = tLRPC$Message.silent;
                    tLRPC$TL_messages_sendEncrypted.data = nativeByteBuffer3;
                    tLRPC$TL_messages_sendEncrypted.random_id = tLRPC$DecryptedMessage.random_id;
                    TLRPC$TL_inputEncryptedChat tLRPC$TL_inputEncryptedChat2 = new TLRPC$TL_inputEncryptedChat();
                    tLRPC$TL_messages_sendEncrypted.peer = tLRPC$TL_inputEncryptedChat2;
                    tLRPC$TL_inputEncryptedChat2.chat_id = tLRPC$EncryptedChat.id;
                    tLRPC$TL_inputEncryptedChat2.access_hash = tLRPC$EncryptedChat.access_hash;
                    tLRPC$TL_messages_sendEncryptedFile2 = tLRPC$TL_messages_sendEncrypted;
                }
                tLRPC$TL_messages_sendEncryptedFile = tLRPC$TL_messages_sendEncryptedFile2;
            } else {
                TLRPC$TL_messages_sendEncryptedFile tLRPC$TL_messages_sendEncryptedFile3 = new TLRPC$TL_messages_sendEncryptedFile();
                tLRPC$TL_messages_sendEncryptedFile3.silent = tLRPC$Message.silent;
                tLRPC$TL_messages_sendEncryptedFile3.data = nativeByteBuffer3;
                tLRPC$TL_messages_sendEncryptedFile3.random_id = tLRPC$DecryptedMessage.random_id;
                TLRPC$TL_inputEncryptedChat tLRPC$TL_inputEncryptedChat3 = new TLRPC$TL_inputEncryptedChat();
                tLRPC$TL_messages_sendEncryptedFile3.peer = tLRPC$TL_inputEncryptedChat3;
                tLRPC$TL_inputEncryptedChat3.chat_id = tLRPC$EncryptedChat.id;
                tLRPC$TL_inputEncryptedChat3.access_hash = tLRPC$EncryptedChat.access_hash;
                tLRPC$TL_messages_sendEncryptedFile3.file = tLRPC$InputEncryptedFile;
                tLRPC$TL_messages_sendEncryptedFile = tLRPC$TL_messages_sendEncryptedFile3;
            }
            getConnectionsManager().sendRequest(tLRPC$TL_messages_sendEncryptedFile, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda29
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    SecretChatHelper.this.lambda$performSendEncryptedRequest$7(tLRPC$DecryptedMessage, tLRPC$EncryptedChat, tLRPC$Message, messageObject, str, tLObject, tLRPC$TL_error);
                }
            }, 64);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendEncryptedRequest$7(TLRPC$DecryptedMessage tLRPC$DecryptedMessage, TLRPC$EncryptedChat tLRPC$EncryptedChat, final TLRPC$Message tLRPC$Message, MessageObject messageObject, String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        final int i;
        if (tLRPC$TL_error == null && (tLRPC$DecryptedMessage.action instanceof TLRPC$TL_decryptedMessageActionNotifyLayer)) {
            TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(tLRPC$EncryptedChat.id));
            if (encryptedChat == null) {
                encryptedChat = tLRPC$EncryptedChat;
            }
            if (encryptedChat.key_hash == null) {
                encryptedChat.key_hash = AndroidUtilities.calcAuthKeyHash(encryptedChat.auth_key);
            }
            if (encryptedChat.key_hash.length == 16) {
                try {
                    byte[] bArr = tLRPC$EncryptedChat.auth_key;
                    byte[] computeSHA256 = Utilities.computeSHA256(bArr, 0, bArr.length);
                    byte[] bArr2 = new byte[36];
                    System.arraycopy(tLRPC$EncryptedChat.key_hash, 0, bArr2, 0, 16);
                    System.arraycopy(computeSHA256, 0, bArr2, 16, 20);
                    encryptedChat.key_hash = bArr2;
                    getMessagesStorage().updateEncryptedChat(encryptedChat);
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
            this.sendingNotifyLayer.remove(Integer.valueOf(encryptedChat.id));
            encryptedChat.layer = AndroidUtilities.setMyLayerVersion(encryptedChat.layer, CURRENT_SECRET_CHAT_LAYER);
            getMessagesStorage().updateEncryptedChatLayer(encryptedChat);
        }
        if (tLRPC$TL_error == null) {
            final String str2 = tLRPC$Message.attachPath;
            final TLRPC$messages_SentEncryptedMessage tLRPC$messages_SentEncryptedMessage = (TLRPC$messages_SentEncryptedMessage) tLObject;
            if (isSecretVisibleMessage(tLRPC$Message)) {
                tLRPC$Message.date = tLRPC$messages_SentEncryptedMessage.date;
            }
            if (messageObject != null) {
                TLRPC$EncryptedFile tLRPC$EncryptedFile = tLRPC$messages_SentEncryptedMessage.file;
                if (tLRPC$EncryptedFile instanceof TLRPC$TL_encryptedFile) {
                    updateMediaPaths(messageObject, tLRPC$EncryptedFile, tLRPC$DecryptedMessage, str);
                    i = messageObject.getMediaExistanceFlags();
                    getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda21
                        @Override // java.lang.Runnable
                        public final void run() {
                            SecretChatHelper.this.lambda$performSendEncryptedRequest$5(tLRPC$Message, tLRPC$messages_SentEncryptedMessage, i, str2);
                        }
                    });
                    return;
                }
            }
            i = 0;
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    SecretChatHelper.this.lambda$performSendEncryptedRequest$5(tLRPC$Message, tLRPC$messages_SentEncryptedMessage, i, str2);
                }
            });
            return;
        }
        getMessagesStorage().markMessageAsSendError(tLRPC$Message, false);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                SecretChatHelper.this.lambda$performSendEncryptedRequest$6(tLRPC$Message);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendEncryptedRequest$5(final TLRPC$Message tLRPC$Message, TLRPC$messages_SentEncryptedMessage tLRPC$messages_SentEncryptedMessage, final int i, final String str) {
        if (isSecretInvisibleMessage(tLRPC$Message)) {
            tLRPC$messages_SentEncryptedMessage.date = 0;
        }
        getMessagesStorage().updateMessageStateAndId(tLRPC$Message.random_id, 0L, Integer.valueOf(tLRPC$Message.id), tLRPC$Message.id, tLRPC$messages_SentEncryptedMessage.date, false, 0);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                SecretChatHelper.this.lambda$performSendEncryptedRequest$4(tLRPC$Message, i, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendEncryptedRequest$4(TLRPC$Message tLRPC$Message, int i, String str) {
        tLRPC$Message.send_state = 0;
        getNotificationCenter().postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(tLRPC$Message.id), Integer.valueOf(tLRPC$Message.id), tLRPC$Message, Long.valueOf(tLRPC$Message.dialog_id), 0L, Integer.valueOf(i), Boolean.FALSE);
        getSendMessagesHelper().processSentMessage(tLRPC$Message.id);
        if (MessageObject.isVideoMessage(tLRPC$Message) || MessageObject.isNewGifMessage(tLRPC$Message) || MessageObject.isRoundVideoMessage(tLRPC$Message)) {
            getSendMessagesHelper().stopVideoService(str);
        }
        getSendMessagesHelper().removeFromSendingMessages(tLRPC$Message.id, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendEncryptedRequest$6(TLRPC$Message tLRPC$Message) {
        tLRPC$Message.send_state = 2;
        getNotificationCenter().postNotificationName(NotificationCenter.messageSendError, Integer.valueOf(tLRPC$Message.id));
        getSendMessagesHelper().processSentMessage(tLRPC$Message.id);
        if (MessageObject.isVideoMessage(tLRPC$Message) || MessageObject.isNewGifMessage(tLRPC$Message) || MessageObject.isRoundVideoMessage(tLRPC$Message)) {
            getSendMessagesHelper().stopVideoService(tLRPC$Message.attachPath);
        }
        getSendMessagesHelper().removeFromSendingMessages(tLRPC$Message.id, false);
    }

    private void applyPeerLayer(final TLRPC$EncryptedChat tLRPC$EncryptedChat, int i) {
        int peerLayerVersion = AndroidUtilities.getPeerLayerVersion(tLRPC$EncryptedChat.layer);
        if (i <= peerLayerVersion) {
            return;
        }
        if (tLRPC$EncryptedChat.key_hash.length == 16) {
            try {
                byte[] bArr = tLRPC$EncryptedChat.auth_key;
                byte[] computeSHA256 = Utilities.computeSHA256(bArr, 0, bArr.length);
                byte[] bArr2 = new byte[36];
                System.arraycopy(tLRPC$EncryptedChat.key_hash, 0, bArr2, 0, 16);
                System.arraycopy(computeSHA256, 0, bArr2, 16, 20);
                tLRPC$EncryptedChat.key_hash = bArr2;
                getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        tLRPC$EncryptedChat.layer = AndroidUtilities.setPeerLayerVersion(tLRPC$EncryptedChat.layer, i);
        getMessagesStorage().updateEncryptedChatLayer(tLRPC$EncryptedChat);
        if (peerLayerVersion < CURRENT_SECRET_CHAT_LAYER) {
            sendNotifyLayerMessage(tLRPC$EncryptedChat, null);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                SecretChatHelper.this.lambda$applyPeerLayer$9(tLRPC$EncryptedChat);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyPeerLayer$9(TLRPC$EncryptedChat tLRPC$EncryptedChat) {
        getNotificationCenter().postNotificationName(NotificationCenter.encryptedChatUpdated, tLRPC$EncryptedChat);
    }

    /* JADX WARN: Removed duplicated region for block: B:296:0x07da  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x07ea  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public TLRPC$Message processDecryptedObject(TLRPC$EncryptedChat tLRPC$EncryptedChat, TLRPC$EncryptedFile tLRPC$EncryptedFile, int i, TLObject tLObject, boolean z) {
        int i2;
        int i3;
        int i4;
        int i5;
        byte[] bArr;
        long bytesToLong;
        int i6;
        byte[] bArr2;
        byte[] bArr3;
        byte[] bArr4;
        TLRPC$PhotoSize tLRPC$TL_photoSizeEmpty;
        byte[] bArr5;
        TLRPC$PhotoSize tLRPC$TL_photoSizeEmpty2;
        byte[] bArr6;
        if (tLObject != null) {
            long j = tLRPC$EncryptedChat.admin_id;
            if (j == getUserConfig().getClientUserId()) {
                j = tLRPC$EncryptedChat.participant_id;
            }
            if (tLRPC$EncryptedChat.exchange_id == 0 && tLRPC$EncryptedChat.future_key_fingerprint == 0 && tLRPC$EncryptedChat.key_use_count_in >= 120) {
                requestNewSecretChatKey(tLRPC$EncryptedChat);
            }
            long j2 = tLRPC$EncryptedChat.exchange_id;
            if (j2 == 0 && tLRPC$EncryptedChat.future_key_fingerprint != 0 && !z) {
                tLRPC$EncryptedChat.future_auth_key = new byte[256];
                tLRPC$EncryptedChat.future_key_fingerprint = 0L;
                getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
            } else if (j2 != 0 && z) {
                tLRPC$EncryptedChat.key_fingerprint = tLRPC$EncryptedChat.future_key_fingerprint;
                tLRPC$EncryptedChat.auth_key = tLRPC$EncryptedChat.future_auth_key;
                tLRPC$EncryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
                tLRPC$EncryptedChat.future_auth_key = new byte[256];
                tLRPC$EncryptedChat.future_key_fingerprint = 0L;
                tLRPC$EncryptedChat.key_use_count_in = (short) 0;
                tLRPC$EncryptedChat.key_use_count_out = (short) 0;
                tLRPC$EncryptedChat.exchange_id = 0L;
                getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
            }
            if (tLObject instanceof TLRPC$TL_decryptedMessage) {
                TLRPC$TL_decryptedMessage tLRPC$TL_decryptedMessage = (TLRPC$TL_decryptedMessage) tLObject;
                TLRPC$TL_message_secret tLRPC$TL_message_secret = new TLRPC$TL_message_secret();
                tLRPC$TL_message_secret.ttl = tLRPC$TL_decryptedMessage.ttl;
                tLRPC$TL_message_secret.entities = tLRPC$TL_decryptedMessage.entities;
                tLRPC$TL_message_secret.message = tLRPC$TL_decryptedMessage.message;
                tLRPC$TL_message_secret.date = i;
                int newMessageId = getUserConfig().getNewMessageId();
                tLRPC$TL_message_secret.id = newMessageId;
                tLRPC$TL_message_secret.local_id = newMessageId;
                tLRPC$TL_message_secret.silent = tLRPC$TL_decryptedMessage.silent;
                getUserConfig().saveConfig(false);
                TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                tLRPC$TL_message_secret.from_id = tLRPC$TL_peerUser;
                tLRPC$TL_peerUser.user_id = j;
                TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                tLRPC$TL_message_secret.peer_id = tLRPC$TL_peerUser2;
                tLRPC$TL_peerUser2.user_id = getUserConfig().getClientUserId();
                tLRPC$TL_message_secret.random_id = tLRPC$TL_decryptedMessage.random_id;
                tLRPC$TL_message_secret.unread = true;
                tLRPC$TL_message_secret.flags = 768;
                String str = tLRPC$TL_decryptedMessage.via_bot_name;
                if (str != null && str.length() > 0) {
                    tLRPC$TL_message_secret.via_bot_name = tLRPC$TL_decryptedMessage.via_bot_name;
                    tLRPC$TL_message_secret.flags |= 2048;
                }
                long j3 = tLRPC$TL_decryptedMessage.grouped_id;
                if (j3 != 0) {
                    tLRPC$TL_message_secret.grouped_id = j3;
                    tLRPC$TL_message_secret.flags |= 131072;
                }
                tLRPC$TL_message_secret.dialog_id = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
                if (tLRPC$TL_decryptedMessage.reply_to_random_id != 0) {
                    TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                    tLRPC$TL_message_secret.reply_to = tLRPC$TL_messageReplyHeader;
                    tLRPC$TL_messageReplyHeader.reply_to_random_id = tLRPC$TL_decryptedMessage.reply_to_random_id;
                    tLRPC$TL_message_secret.flags |= 8;
                }
                TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia = tLRPC$TL_decryptedMessage.media;
                if (tLRPC$DecryptedMessageMedia == null || (tLRPC$DecryptedMessageMedia instanceof TLRPC$TL_decryptedMessageMediaEmpty)) {
                    tLRPC$TL_message_secret.media = new TLRPC$TL_messageMediaEmpty();
                } else if (tLRPC$DecryptedMessageMedia instanceof TLRPC$TL_decryptedMessageMediaWebPage) {
                    TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage = new TLRPC$TL_messageMediaWebPage();
                    tLRPC$TL_message_secret.media = tLRPC$TL_messageMediaWebPage;
                    tLRPC$TL_messageMediaWebPage.webpage = new TLRPC$TL_webPageUrlPending();
                    tLRPC$TL_message_secret.media.webpage.url = tLRPC$TL_decryptedMessage.media.url;
                } else {
                    String str2 = "";
                    if (tLRPC$DecryptedMessageMedia instanceof TLRPC$TL_decryptedMessageMediaContact) {
                        TLRPC$TL_messageMediaContact tLRPC$TL_messageMediaContact = new TLRPC$TL_messageMediaContact();
                        tLRPC$TL_message_secret.media = tLRPC$TL_messageMediaContact;
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia2 = tLRPC$TL_decryptedMessage.media;
                        tLRPC$TL_messageMediaContact.last_name = tLRPC$DecryptedMessageMedia2.last_name;
                        tLRPC$TL_messageMediaContact.first_name = tLRPC$DecryptedMessageMedia2.first_name;
                        tLRPC$TL_messageMediaContact.phone_number = tLRPC$DecryptedMessageMedia2.phone_number;
                        tLRPC$TL_messageMediaContact.user_id = tLRPC$DecryptedMessageMedia2.user_id;
                        tLRPC$TL_messageMediaContact.vcard = str2;
                    } else if (tLRPC$DecryptedMessageMedia instanceof TLRPC$TL_decryptedMessageMediaGeoPoint) {
                        TLRPC$TL_messageMediaGeo tLRPC$TL_messageMediaGeo = new TLRPC$TL_messageMediaGeo();
                        tLRPC$TL_message_secret.media = tLRPC$TL_messageMediaGeo;
                        tLRPC$TL_messageMediaGeo.geo = new TLRPC$TL_geoPoint();
                        TLRPC$GeoPoint tLRPC$GeoPoint = tLRPC$TL_message_secret.media.geo;
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia3 = tLRPC$TL_decryptedMessage.media;
                        tLRPC$GeoPoint.lat = tLRPC$DecryptedMessageMedia3.lat;
                        tLRPC$GeoPoint._long = tLRPC$DecryptedMessageMedia3._long;
                    } else if (tLRPC$DecryptedMessageMedia instanceof TLRPC$TL_decryptedMessageMediaPhoto) {
                        byte[] bArr7 = tLRPC$DecryptedMessageMedia.key;
                        if (bArr7 == null || bArr7.length != 32 || (bArr6 = tLRPC$DecryptedMessageMedia.iv) == null || bArr6.length != 32) {
                            return null;
                        }
                        TLRPC$TL_messageMediaPhoto tLRPC$TL_messageMediaPhoto = new TLRPC$TL_messageMediaPhoto();
                        tLRPC$TL_message_secret.media = tLRPC$TL_messageMediaPhoto;
                        tLRPC$TL_messageMediaPhoto.flags |= 3;
                        if (TextUtils.isEmpty(tLRPC$TL_message_secret.message)) {
                            String str3 = tLRPC$TL_decryptedMessage.media.caption;
                            if (str3 != null) {
                                str2 = str3;
                            }
                            tLRPC$TL_message_secret.message = str2;
                        }
                        tLRPC$TL_message_secret.media.photo = new TLRPC$TL_photo();
                        TLRPC$Photo tLRPC$Photo = tLRPC$TL_message_secret.media.photo;
                        tLRPC$Photo.file_reference = new byte[0];
                        tLRPC$Photo.date = tLRPC$TL_message_secret.date;
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia4 = tLRPC$TL_decryptedMessage.media;
                        byte[] bArr8 = ((TLRPC$TL_decryptedMessageMediaPhoto) tLRPC$DecryptedMessageMedia4).thumb;
                        if (bArr8 != null && bArr8.length != 0 && bArr8.length <= 6000 && tLRPC$DecryptedMessageMedia4.thumb_w <= 100 && tLRPC$DecryptedMessageMedia4.thumb_h <= 100) {
                            TLRPC$TL_photoCachedSize tLRPC$TL_photoCachedSize = new TLRPC$TL_photoCachedSize();
                            TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia5 = tLRPC$TL_decryptedMessage.media;
                            tLRPC$TL_photoCachedSize.w = tLRPC$DecryptedMessageMedia5.thumb_w;
                            tLRPC$TL_photoCachedSize.h = tLRPC$DecryptedMessageMedia5.thumb_h;
                            tLRPC$TL_photoCachedSize.bytes = bArr8;
                            tLRPC$TL_photoCachedSize.type = "s";
                            tLRPC$TL_photoCachedSize.location = new TLRPC$TL_fileLocationUnavailable();
                            tLRPC$TL_message_secret.media.photo.sizes.add(tLRPC$TL_photoCachedSize);
                        }
                        int i7 = tLRPC$TL_message_secret.ttl;
                        if (i7 != 0) {
                            TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$TL_message_secret.media;
                            tLRPC$MessageMedia.ttl_seconds = i7;
                            tLRPC$MessageMedia.flags |= 4;
                        }
                        TLRPC$TL_photoSize_layer127 tLRPC$TL_photoSize_layer127 = new TLRPC$TL_photoSize_layer127();
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia6 = tLRPC$TL_decryptedMessage.media;
                        tLRPC$TL_photoSize_layer127.w = tLRPC$DecryptedMessageMedia6.w;
                        tLRPC$TL_photoSize_layer127.h = tLRPC$DecryptedMessageMedia6.h;
                        tLRPC$TL_photoSize_layer127.type = "x";
                        tLRPC$TL_photoSize_layer127.size = (int) tLRPC$EncryptedFile.size;
                        TLRPC$TL_fileEncryptedLocation tLRPC$TL_fileEncryptedLocation = new TLRPC$TL_fileEncryptedLocation();
                        tLRPC$TL_photoSize_layer127.location = tLRPC$TL_fileEncryptedLocation;
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia7 = tLRPC$TL_decryptedMessage.media;
                        tLRPC$TL_fileEncryptedLocation.key = tLRPC$DecryptedMessageMedia7.key;
                        tLRPC$TL_fileEncryptedLocation.iv = tLRPC$DecryptedMessageMedia7.iv;
                        tLRPC$TL_fileEncryptedLocation.dc_id = tLRPC$EncryptedFile.dc_id;
                        tLRPC$TL_fileEncryptedLocation.volume_id = tLRPC$EncryptedFile.id;
                        tLRPC$TL_fileEncryptedLocation.secret = tLRPC$EncryptedFile.access_hash;
                        tLRPC$TL_fileEncryptedLocation.local_id = tLRPC$EncryptedFile.key_fingerprint;
                        tLRPC$TL_message_secret.media.photo.sizes.add(tLRPC$TL_photoSize_layer127);
                    } else if (tLRPC$DecryptedMessageMedia instanceof TLRPC$TL_decryptedMessageMediaVideo) {
                        byte[] bArr9 = tLRPC$DecryptedMessageMedia.key;
                        if (bArr9 == null || bArr9.length != 32 || (bArr5 = tLRPC$DecryptedMessageMedia.iv) == null || bArr5.length != 32) {
                            return null;
                        }
                        TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument = new TLRPC$TL_messageMediaDocument();
                        tLRPC$TL_message_secret.media = tLRPC$TL_messageMediaDocument;
                        tLRPC$TL_messageMediaDocument.flags |= 3;
                        tLRPC$TL_messageMediaDocument.document = new TLRPC$TL_documentEncrypted();
                        TLRPC$Document tLRPC$Document = tLRPC$TL_message_secret.media.document;
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia8 = tLRPC$TL_decryptedMessage.media;
                        tLRPC$Document.key = tLRPC$DecryptedMessageMedia8.key;
                        tLRPC$Document.iv = tLRPC$DecryptedMessageMedia8.iv;
                        tLRPC$Document.dc_id = tLRPC$EncryptedFile.dc_id;
                        if (TextUtils.isEmpty(tLRPC$TL_message_secret.message)) {
                            String str4 = tLRPC$TL_decryptedMessage.media.caption;
                            if (str4 != null) {
                                str2 = str4;
                            }
                            tLRPC$TL_message_secret.message = str2;
                        }
                        TLRPC$Document tLRPC$Document2 = tLRPC$TL_message_secret.media.document;
                        tLRPC$Document2.date = i;
                        tLRPC$Document2.size = tLRPC$EncryptedFile.size;
                        tLRPC$Document2.id = tLRPC$EncryptedFile.id;
                        tLRPC$Document2.access_hash = tLRPC$EncryptedFile.access_hash;
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia9 = tLRPC$TL_decryptedMessage.media;
                        String str5 = tLRPC$DecryptedMessageMedia9.mime_type;
                        tLRPC$Document2.mime_type = str5;
                        if (str5 == null) {
                            tLRPC$Document2.mime_type = "video/mp4";
                        }
                        byte[] bArr10 = ((TLRPC$TL_decryptedMessageMediaVideo) tLRPC$DecryptedMessageMedia9).thumb;
                        if (bArr10 != null && bArr10.length != 0 && bArr10.length <= 6000 && tLRPC$DecryptedMessageMedia9.thumb_w <= 100 && tLRPC$DecryptedMessageMedia9.thumb_h <= 100) {
                            tLRPC$TL_photoSizeEmpty2 = new TLRPC$TL_photoCachedSize();
                            tLRPC$TL_photoSizeEmpty2.bytes = bArr10;
                            TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia10 = tLRPC$TL_decryptedMessage.media;
                            tLRPC$TL_photoSizeEmpty2.w = tLRPC$DecryptedMessageMedia10.thumb_w;
                            tLRPC$TL_photoSizeEmpty2.h = tLRPC$DecryptedMessageMedia10.thumb_h;
                            tLRPC$TL_photoSizeEmpty2.type = "s";
                            tLRPC$TL_photoSizeEmpty2.location = new TLRPC$TL_fileLocationUnavailable();
                        } else {
                            tLRPC$TL_photoSizeEmpty2 = new TLRPC$TL_photoSizeEmpty();
                            tLRPC$TL_photoSizeEmpty2.type = "s";
                        }
                        tLRPC$TL_message_secret.media.document.thumbs.add(tLRPC$TL_photoSizeEmpty2);
                        tLRPC$TL_message_secret.media.document.flags |= 1;
                        TLRPC$TL_documentAttributeVideo tLRPC$TL_documentAttributeVideo = new TLRPC$TL_documentAttributeVideo();
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia11 = tLRPC$TL_decryptedMessage.media;
                        tLRPC$TL_documentAttributeVideo.w = tLRPC$DecryptedMessageMedia11.w;
                        tLRPC$TL_documentAttributeVideo.h = tLRPC$DecryptedMessageMedia11.h;
                        tLRPC$TL_documentAttributeVideo.duration = tLRPC$DecryptedMessageMedia11.duration;
                        tLRPC$TL_documentAttributeVideo.supports_streaming = false;
                        tLRPC$TL_message_secret.media.document.attributes.add(tLRPC$TL_documentAttributeVideo);
                        int i8 = tLRPC$TL_message_secret.ttl;
                        if (i8 != 0) {
                            TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$TL_message_secret.media;
                            tLRPC$MessageMedia2.ttl_seconds = i8;
                            tLRPC$MessageMedia2.flags |= 4;
                        }
                        if (i8 != 0) {
                            tLRPC$TL_message_secret.ttl = Math.max(tLRPC$TL_decryptedMessage.media.duration + 1, i8);
                        }
                    } else if (tLRPC$DecryptedMessageMedia instanceof TLRPC$TL_decryptedMessageMediaDocument) {
                        byte[] bArr11 = tLRPC$DecryptedMessageMedia.key;
                        if (bArr11 == null || bArr11.length != 32 || (bArr4 = tLRPC$DecryptedMessageMedia.iv) == null || bArr4.length != 32) {
                            return null;
                        }
                        TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument2 = new TLRPC$TL_messageMediaDocument();
                        tLRPC$TL_message_secret.media = tLRPC$TL_messageMediaDocument2;
                        tLRPC$TL_messageMediaDocument2.flags |= 3;
                        if (TextUtils.isEmpty(tLRPC$TL_message_secret.message)) {
                            String str6 = tLRPC$TL_decryptedMessage.media.caption;
                            if (str6 == null) {
                                str6 = str2;
                            }
                            tLRPC$TL_message_secret.message = str6;
                        }
                        tLRPC$TL_message_secret.media.document = new TLRPC$TL_documentEncrypted();
                        TLRPC$Document tLRPC$Document3 = tLRPC$TL_message_secret.media.document;
                        tLRPC$Document3.id = tLRPC$EncryptedFile.id;
                        tLRPC$Document3.access_hash = tLRPC$EncryptedFile.access_hash;
                        tLRPC$Document3.date = i;
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia12 = tLRPC$TL_decryptedMessage.media;
                        tLRPC$Document3.mime_type = tLRPC$DecryptedMessageMedia12.mime_type;
                        if (tLRPC$DecryptedMessageMedia12 instanceof TLRPC$TL_decryptedMessageMediaDocument_layer8) {
                            TLRPC$TL_documentAttributeFilename tLRPC$TL_documentAttributeFilename = new TLRPC$TL_documentAttributeFilename();
                            tLRPC$TL_documentAttributeFilename.file_name = tLRPC$TL_decryptedMessage.media.file_name;
                            tLRPC$TL_message_secret.media.document.attributes.add(tLRPC$TL_documentAttributeFilename);
                        } else {
                            tLRPC$Document3.attributes = tLRPC$DecryptedMessageMedia12.attributes;
                        }
                        if (tLRPC$TL_message_secret.ttl > 0) {
                            int size = tLRPC$TL_message_secret.media.document.attributes.size();
                            for (int i9 = 0; i9 < size; i9++) {
                                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$TL_message_secret.media.document.attributes.get(i9);
                                if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo)) {
                                    tLRPC$TL_message_secret.ttl = Math.max(tLRPC$DocumentAttribute.duration + 1, tLRPC$TL_message_secret.ttl);
                                    break;
                                }
                            }
                            tLRPC$TL_message_secret.ttl = Math.max(tLRPC$TL_decryptedMessage.media.duration + 1, tLRPC$TL_message_secret.ttl);
                        }
                        TLRPC$Document tLRPC$Document4 = tLRPC$TL_message_secret.media.document;
                        long j4 = tLRPC$TL_decryptedMessage.media.size;
                        tLRPC$Document4.size = j4 != 0 ? Math.min(j4, tLRPC$EncryptedFile.size) : tLRPC$EncryptedFile.size;
                        TLRPC$Document tLRPC$Document5 = tLRPC$TL_message_secret.media.document;
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia13 = tLRPC$TL_decryptedMessage.media;
                        tLRPC$Document5.key = tLRPC$DecryptedMessageMedia13.key;
                        tLRPC$Document5.iv = tLRPC$DecryptedMessageMedia13.iv;
                        String str7 = tLRPC$Document5.mime_type;
                        if (str7 == null) {
                            tLRPC$Document5.mime_type = str2;
                        } else if ("application/x-tgsticker".equals(str7) || "application/x-tgsdice".equals(tLRPC$TL_message_secret.media.document.mime_type)) {
                            tLRPC$TL_message_secret.media.document.mime_type = "application/x-bad_tgsticker";
                        }
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia14 = tLRPC$TL_decryptedMessage.media;
                        byte[] bArr12 = ((TLRPC$TL_decryptedMessageMediaDocument) tLRPC$DecryptedMessageMedia14).thumb;
                        if (bArr12 != null && bArr12.length != 0 && bArr12.length <= 6000 && tLRPC$DecryptedMessageMedia14.thumb_w <= 100 && tLRPC$DecryptedMessageMedia14.thumb_h <= 100) {
                            tLRPC$TL_photoSizeEmpty = new TLRPC$TL_photoCachedSize();
                            tLRPC$TL_photoSizeEmpty.bytes = bArr12;
                            TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia15 = tLRPC$TL_decryptedMessage.media;
                            tLRPC$TL_photoSizeEmpty.w = tLRPC$DecryptedMessageMedia15.thumb_w;
                            tLRPC$TL_photoSizeEmpty.h = tLRPC$DecryptedMessageMedia15.thumb_h;
                            tLRPC$TL_photoSizeEmpty.type = "s";
                            tLRPC$TL_photoSizeEmpty.location = new TLRPC$TL_fileLocationUnavailable();
                        } else {
                            tLRPC$TL_photoSizeEmpty = new TLRPC$TL_photoSizeEmpty();
                            tLRPC$TL_photoSizeEmpty.type = "s";
                        }
                        tLRPC$TL_message_secret.media.document.thumbs.add(tLRPC$TL_photoSizeEmpty);
                        TLRPC$Document tLRPC$Document6 = tLRPC$TL_message_secret.media.document;
                        tLRPC$Document6.flags |= 1;
                        tLRPC$Document6.dc_id = tLRPC$EncryptedFile.dc_id;
                        if (MessageObject.isVoiceMessage(tLRPC$TL_message_secret) || MessageObject.isRoundVideoMessage(tLRPC$TL_message_secret)) {
                            tLRPC$TL_message_secret.media_unread = true;
                        }
                    } else if (tLRPC$DecryptedMessageMedia instanceof TLRPC$TL_decryptedMessageMediaExternalDocument) {
                        TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument3 = new TLRPC$TL_messageMediaDocument();
                        tLRPC$TL_message_secret.media = tLRPC$TL_messageMediaDocument3;
                        tLRPC$TL_messageMediaDocument3.flags |= 3;
                        tLRPC$TL_message_secret.message = str2;
                        tLRPC$TL_messageMediaDocument3.document = new TLRPC$TL_document();
                        TLRPC$Document tLRPC$Document7 = tLRPC$TL_message_secret.media.document;
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia16 = tLRPC$TL_decryptedMessage.media;
                        tLRPC$Document7.id = tLRPC$DecryptedMessageMedia16.id;
                        tLRPC$Document7.access_hash = tLRPC$DecryptedMessageMedia16.access_hash;
                        tLRPC$Document7.file_reference = new byte[0];
                        tLRPC$Document7.date = tLRPC$DecryptedMessageMedia16.date;
                        tLRPC$Document7.attributes = tLRPC$DecryptedMessageMedia16.attributes;
                        tLRPC$Document7.mime_type = tLRPC$DecryptedMessageMedia16.mime_type;
                        tLRPC$Document7.dc_id = tLRPC$DecryptedMessageMedia16.dc_id;
                        tLRPC$Document7.size = tLRPC$DecryptedMessageMedia16.size;
                        tLRPC$Document7.thumbs.add(((TLRPC$TL_decryptedMessageMediaExternalDocument) tLRPC$DecryptedMessageMedia16).thumb);
                        TLRPC$Document tLRPC$Document8 = tLRPC$TL_message_secret.media.document;
                        tLRPC$Document8.flags |= 1;
                        if (tLRPC$Document8.mime_type == null) {
                            tLRPC$Document8.mime_type = str2;
                        }
                        if (MessageObject.isAnimatedStickerMessage(tLRPC$TL_message_secret)) {
                            tLRPC$TL_message_secret.stickerVerified = 0;
                            getMediaDataController().verifyAnimatedStickerMessage(tLRPC$TL_message_secret, true);
                        }
                    } else if (tLRPC$DecryptedMessageMedia instanceof TLRPC$TL_decryptedMessageMediaAudio) {
                        byte[] bArr13 = tLRPC$DecryptedMessageMedia.key;
                        if (bArr13 == null || bArr13.length != 32 || (bArr3 = tLRPC$DecryptedMessageMedia.iv) == null || bArr3.length != 32) {
                            return null;
                        }
                        TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument4 = new TLRPC$TL_messageMediaDocument();
                        tLRPC$TL_message_secret.media = tLRPC$TL_messageMediaDocument4;
                        tLRPC$TL_messageMediaDocument4.flags |= 3;
                        tLRPC$TL_messageMediaDocument4.document = new TLRPC$TL_documentEncrypted();
                        TLRPC$Document tLRPC$Document9 = tLRPC$TL_message_secret.media.document;
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia17 = tLRPC$TL_decryptedMessage.media;
                        tLRPC$Document9.key = tLRPC$DecryptedMessageMedia17.key;
                        tLRPC$Document9.iv = tLRPC$DecryptedMessageMedia17.iv;
                        tLRPC$Document9.id = tLRPC$EncryptedFile.id;
                        tLRPC$Document9.access_hash = tLRPC$EncryptedFile.access_hash;
                        tLRPC$Document9.date = i;
                        tLRPC$Document9.size = tLRPC$EncryptedFile.size;
                        tLRPC$Document9.dc_id = tLRPC$EncryptedFile.dc_id;
                        tLRPC$Document9.mime_type = tLRPC$DecryptedMessageMedia17.mime_type;
                        if (TextUtils.isEmpty(tLRPC$TL_message_secret.message)) {
                            String str8 = tLRPC$TL_decryptedMessage.media.caption;
                            if (str8 != null) {
                                str2 = str8;
                            }
                            tLRPC$TL_message_secret.message = str2;
                        }
                        TLRPC$Document tLRPC$Document10 = tLRPC$TL_message_secret.media.document;
                        if (tLRPC$Document10.mime_type == null) {
                            tLRPC$Document10.mime_type = "audio/ogg";
                        }
                        TLRPC$TL_documentAttributeAudio tLRPC$TL_documentAttributeAudio = new TLRPC$TL_documentAttributeAudio();
                        tLRPC$TL_documentAttributeAudio.duration = tLRPC$TL_decryptedMessage.media.duration;
                        tLRPC$TL_documentAttributeAudio.voice = true;
                        tLRPC$TL_message_secret.media.document.attributes.add(tLRPC$TL_documentAttributeAudio);
                        int i10 = tLRPC$TL_message_secret.ttl;
                        if (i10 != 0) {
                            tLRPC$TL_message_secret.ttl = Math.max(tLRPC$TL_decryptedMessage.media.duration + 1, i10);
                        }
                        if (tLRPC$TL_message_secret.media.document.thumbs.isEmpty()) {
                            TLRPC$TL_photoSizeEmpty tLRPC$TL_photoSizeEmpty3 = new TLRPC$TL_photoSizeEmpty();
                            tLRPC$TL_photoSizeEmpty3.type = "s";
                            tLRPC$TL_message_secret.media.document.thumbs.add(tLRPC$TL_photoSizeEmpty3);
                        }
                    } else if (!(tLRPC$DecryptedMessageMedia instanceof TLRPC$TL_decryptedMessageMediaVenue)) {
                        return null;
                    } else {
                        TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue = new TLRPC$TL_messageMediaVenue();
                        tLRPC$TL_message_secret.media = tLRPC$TL_messageMediaVenue;
                        tLRPC$TL_messageMediaVenue.geo = new TLRPC$TL_geoPoint();
                        TLRPC$MessageMedia tLRPC$MessageMedia3 = tLRPC$TL_message_secret.media;
                        TLRPC$GeoPoint tLRPC$GeoPoint2 = tLRPC$MessageMedia3.geo;
                        TLRPC$DecryptedMessageMedia tLRPC$DecryptedMessageMedia18 = tLRPC$TL_decryptedMessage.media;
                        tLRPC$GeoPoint2.lat = tLRPC$DecryptedMessageMedia18.lat;
                        tLRPC$GeoPoint2._long = tLRPC$DecryptedMessageMedia18._long;
                        tLRPC$MessageMedia3.title = tLRPC$DecryptedMessageMedia18.title;
                        tLRPC$MessageMedia3.address = tLRPC$DecryptedMessageMedia18.address;
                        tLRPC$MessageMedia3.provider = tLRPC$DecryptedMessageMedia18.provider;
                        tLRPC$MessageMedia3.venue_id = tLRPC$DecryptedMessageMedia18.venue_id;
                        tLRPC$MessageMedia3.venue_type = str2;
                    }
                }
                int i11 = tLRPC$TL_message_secret.ttl;
                if (i11 != 0) {
                    TLRPC$MessageMedia tLRPC$MessageMedia4 = tLRPC$TL_message_secret.media;
                    if (tLRPC$MessageMedia4.ttl_seconds == 0) {
                        tLRPC$MessageMedia4.ttl_seconds = i11;
                        tLRPC$MessageMedia4.flags |= 4;
                    }
                }
                String str9 = tLRPC$TL_message_secret.message;
                if (str9 != null) {
                    tLRPC$TL_message_secret.message = str9.replace((char) 8238, ' ');
                }
                return tLRPC$TL_message_secret;
            } else if (tLObject instanceof TLRPC$TL_decryptedMessageService) {
                TLRPC$TL_decryptedMessageService tLRPC$TL_decryptedMessageService = (TLRPC$TL_decryptedMessageService) tLObject;
                TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction = tLRPC$TL_decryptedMessageService.action;
                if ((tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionSetMessageTTL) || (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionScreenshotMessages)) {
                    TLRPC$TL_messageService tLRPC$TL_messageService = new TLRPC$TL_messageService();
                    if (tLRPC$TL_decryptedMessageService.action instanceof TLRPC$TL_decryptedMessageActionSetMessageTTL) {
                        TLRPC$TL_messageEncryptedAction tLRPC$TL_messageEncryptedAction = new TLRPC$TL_messageEncryptedAction();
                        tLRPC$TL_messageService.action = tLRPC$TL_messageEncryptedAction;
                        TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction2 = tLRPC$TL_decryptedMessageService.action;
                        int i12 = tLRPC$DecryptedMessageAction2.ttl_seconds;
                        if (i12 < 0 || i12 > 31536000) {
                            tLRPC$DecryptedMessageAction2.ttl_seconds = 31536000;
                        }
                        tLRPC$EncryptedChat.ttl = tLRPC$DecryptedMessageAction2.ttl_seconds;
                        tLRPC$TL_messageEncryptedAction.encryptedAction = tLRPC$DecryptedMessageAction2;
                        getMessagesStorage().updateEncryptedChatTTL(tLRPC$EncryptedChat);
                    } else {
                        TLRPC$TL_messageEncryptedAction tLRPC$TL_messageEncryptedAction2 = new TLRPC$TL_messageEncryptedAction();
                        tLRPC$TL_messageService.action = tLRPC$TL_messageEncryptedAction2;
                        tLRPC$TL_messageEncryptedAction2.encryptedAction = tLRPC$TL_decryptedMessageService.action;
                    }
                    int newMessageId2 = getUserConfig().getNewMessageId();
                    tLRPC$TL_messageService.id = newMessageId2;
                    tLRPC$TL_messageService.local_id = newMessageId2;
                    getUserConfig().saveConfig(false);
                    tLRPC$TL_messageService.unread = true;
                    tLRPC$TL_messageService.flags = 256;
                    tLRPC$TL_messageService.date = i;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser3 = new TLRPC$TL_peerUser();
                    tLRPC$TL_messageService.from_id = tLRPC$TL_peerUser3;
                    tLRPC$TL_peerUser3.user_id = j;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser4 = new TLRPC$TL_peerUser();
                    tLRPC$TL_messageService.peer_id = tLRPC$TL_peerUser4;
                    tLRPC$TL_peerUser4.user_id = getUserConfig().getClientUserId();
                    tLRPC$TL_messageService.dialog_id = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
                    return tLRPC$TL_messageService;
                } else if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionFlushHistory) {
                    final long makeEncryptedDialogId = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            SecretChatHelper.this.lambda$processDecryptedObject$12(makeEncryptedDialogId);
                        }
                    });
                    return null;
                } else if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionDeleteMessages) {
                    if (!tLRPC$DecryptedMessageAction.random_ids.isEmpty()) {
                        this.pendingEncMessagesToDelete.addAll(tLRPC$TL_decryptedMessageService.action.random_ids);
                    }
                    return null;
                } else if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionReadMessages) {
                    if (!tLRPC$DecryptedMessageAction.random_ids.isEmpty()) {
                        int currentTime = getConnectionsManager().getCurrentTime();
                        getMessagesStorage().createTaskForSecretChat(tLRPC$EncryptedChat.id, currentTime, currentTime, 1, tLRPC$TL_decryptedMessageService.action.random_ids);
                    }
                } else if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionNotifyLayer) {
                    applyPeerLayer(tLRPC$EncryptedChat, tLRPC$DecryptedMessageAction.layer);
                } else if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionRequestKey) {
                    long j5 = tLRPC$EncryptedChat.exchange_id;
                    if (j5 != 0) {
                        if (j5 > tLRPC$DecryptedMessageAction.exchange_id) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("we already have request key with higher exchange_id");
                            }
                            return null;
                        }
                        sendAbortKeyMessage(tLRPC$EncryptedChat, null, j5);
                    }
                    byte[] bArr14 = new byte[256];
                    Utilities.random.nextBytes(bArr14);
                    BigInteger bigInteger = new BigInteger(1, getMessagesStorage().getSecretPBytes());
                    BigInteger modPow = BigInteger.valueOf(getMessagesStorage().getSecretG()).modPow(new BigInteger(1, bArr14), bigInteger);
                    BigInteger bigInteger2 = new BigInteger(1, tLRPC$TL_decryptedMessageService.action.g_a);
                    if (!Utilities.isGoodGaAndGb(bigInteger2, bigInteger)) {
                        sendAbortKeyMessage(tLRPC$EncryptedChat, null, tLRPC$TL_decryptedMessageService.action.exchange_id);
                        return null;
                    }
                    byte[] byteArray = modPow.toByteArray();
                    if (byteArray.length > 256) {
                        byte[] bArr15 = new byte[256];
                        System.arraycopy(byteArray, 1, bArr15, 0, 256);
                        byteArray = bArr15;
                    }
                    byte[] byteArray2 = bigInteger2.modPow(new BigInteger(1, bArr14), bigInteger).toByteArray();
                    if (byteArray2.length > 256) {
                        bArr2 = new byte[256];
                        i6 = 0;
                        System.arraycopy(byteArray2, byteArray2.length - 256, bArr2, 0, 256);
                    } else {
                        i6 = 0;
                        if (byteArray2.length < 256) {
                            bArr2 = new byte[256];
                            System.arraycopy(byteArray2, 0, bArr2, 256 - byteArray2.length, byteArray2.length);
                            for (int i13 = 0; i13 < 256 - byteArray2.length; i13++) {
                                bArr2[i13] = 0;
                            }
                        }
                        byte[] computeSHA1 = Utilities.computeSHA1(byteArray2);
                        byte[] bArr16 = new byte[8];
                        System.arraycopy(computeSHA1, computeSHA1.length - 8, bArr16, i6, 8);
                        tLRPC$EncryptedChat.exchange_id = tLRPC$TL_decryptedMessageService.action.exchange_id;
                        tLRPC$EncryptedChat.future_auth_key = byteArray2;
                        tLRPC$EncryptedChat.future_key_fingerprint = Utilities.bytesToLong(bArr16);
                        tLRPC$EncryptedChat.g_a_or_b = byteArray;
                        getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
                        sendAcceptKeyMessage(tLRPC$EncryptedChat, null);
                    }
                    byteArray2 = bArr2;
                    byte[] computeSHA12 = Utilities.computeSHA1(byteArray2);
                    byte[] bArr162 = new byte[8];
                    System.arraycopy(computeSHA12, computeSHA12.length - 8, bArr162, i6, 8);
                    tLRPC$EncryptedChat.exchange_id = tLRPC$TL_decryptedMessageService.action.exchange_id;
                    tLRPC$EncryptedChat.future_auth_key = byteArray2;
                    tLRPC$EncryptedChat.future_key_fingerprint = Utilities.bytesToLong(bArr162);
                    tLRPC$EncryptedChat.g_a_or_b = byteArray;
                    getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
                    sendAcceptKeyMessage(tLRPC$EncryptedChat, null);
                } else if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionAcceptKey) {
                    if (tLRPC$EncryptedChat.exchange_id == tLRPC$DecryptedMessageAction.exchange_id) {
                        BigInteger bigInteger3 = new BigInteger(1, getMessagesStorage().getSecretPBytes());
                        BigInteger bigInteger4 = new BigInteger(1, tLRPC$TL_decryptedMessageService.action.g_b);
                        if (!Utilities.isGoodGaAndGb(bigInteger4, bigInteger3)) {
                            tLRPC$EncryptedChat.future_auth_key = new byte[256];
                            tLRPC$EncryptedChat.future_key_fingerprint = 0L;
                            tLRPC$EncryptedChat.exchange_id = 0L;
                            getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
                            sendAbortKeyMessage(tLRPC$EncryptedChat, null, tLRPC$TL_decryptedMessageService.action.exchange_id);
                            return null;
                        }
                        byte[] byteArray3 = bigInteger4.modPow(new BigInteger(1, tLRPC$EncryptedChat.a_or_b), bigInteger3).toByteArray();
                        if (byteArray3.length > 256) {
                            bArr = new byte[256];
                            i5 = 0;
                            System.arraycopy(byteArray3, byteArray3.length - 256, bArr, 0, 256);
                        } else {
                            i5 = 0;
                            if (byteArray3.length < 256) {
                                bArr = new byte[256];
                                System.arraycopy(byteArray3, 0, bArr, 256 - byteArray3.length, byteArray3.length);
                                for (int i14 = 0; i14 < 256 - byteArray3.length; i14++) {
                                    bArr[i14] = 0;
                                }
                            }
                            byte[] computeSHA13 = Utilities.computeSHA1(byteArray3);
                            byte[] bArr17 = new byte[8];
                            System.arraycopy(computeSHA13, computeSHA13.length - 8, bArr17, i5, 8);
                            bytesToLong = Utilities.bytesToLong(bArr17);
                            if (tLRPC$TL_decryptedMessageService.action.key_fingerprint != bytesToLong) {
                                tLRPC$EncryptedChat.future_auth_key = byteArray3;
                                tLRPC$EncryptedChat.future_key_fingerprint = bytesToLong;
                                getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
                                sendCommitKeyMessage(tLRPC$EncryptedChat, null);
                            } else {
                                tLRPC$EncryptedChat.future_auth_key = new byte[256];
                                tLRPC$EncryptedChat.future_key_fingerprint = 0L;
                                tLRPC$EncryptedChat.exchange_id = 0L;
                                getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
                                sendAbortKeyMessage(tLRPC$EncryptedChat, null, tLRPC$TL_decryptedMessageService.action.exchange_id);
                            }
                        }
                        byteArray3 = bArr;
                        byte[] computeSHA132 = Utilities.computeSHA1(byteArray3);
                        byte[] bArr172 = new byte[8];
                        System.arraycopy(computeSHA132, computeSHA132.length - 8, bArr172, i5, 8);
                        bytesToLong = Utilities.bytesToLong(bArr172);
                        if (tLRPC$TL_decryptedMessageService.action.key_fingerprint != bytesToLong) {
                        }
                    } else {
                        tLRPC$EncryptedChat.future_auth_key = new byte[256];
                        tLRPC$EncryptedChat.future_key_fingerprint = 0L;
                        tLRPC$EncryptedChat.exchange_id = 0L;
                        getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
                        sendAbortKeyMessage(tLRPC$EncryptedChat, null, tLRPC$TL_decryptedMessageService.action.exchange_id);
                    }
                } else if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionCommitKey) {
                    if (tLRPC$EncryptedChat.exchange_id == tLRPC$DecryptedMessageAction.exchange_id) {
                        long j6 = tLRPC$EncryptedChat.future_key_fingerprint;
                        if (j6 == tLRPC$DecryptedMessageAction.key_fingerprint) {
                            long j7 = tLRPC$EncryptedChat.key_fingerprint;
                            byte[] bArr18 = tLRPC$EncryptedChat.auth_key;
                            tLRPC$EncryptedChat.key_fingerprint = j6;
                            tLRPC$EncryptedChat.auth_key = tLRPC$EncryptedChat.future_auth_key;
                            tLRPC$EncryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
                            tLRPC$EncryptedChat.future_auth_key = bArr18;
                            tLRPC$EncryptedChat.future_key_fingerprint = j7;
                            tLRPC$EncryptedChat.key_use_count_in = (short) 0;
                            tLRPC$EncryptedChat.key_use_count_out = (short) 0;
                            tLRPC$EncryptedChat.exchange_id = 0L;
                            getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
                            sendNoopMessage(tLRPC$EncryptedChat, null);
                        }
                    }
                    tLRPC$EncryptedChat.future_auth_key = new byte[256];
                    tLRPC$EncryptedChat.future_key_fingerprint = 0L;
                    tLRPC$EncryptedChat.exchange_id = 0L;
                    getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
                    sendAbortKeyMessage(tLRPC$EncryptedChat, null, tLRPC$TL_decryptedMessageService.action.exchange_id);
                } else if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionAbortKey) {
                    if (tLRPC$EncryptedChat.exchange_id == tLRPC$DecryptedMessageAction.exchange_id) {
                        tLRPC$EncryptedChat.future_auth_key = new byte[256];
                        tLRPC$EncryptedChat.future_key_fingerprint = 0L;
                        tLRPC$EncryptedChat.exchange_id = 0L;
                        getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
                    }
                } else if ((tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionNoop) || !(tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionResend) || (i2 = tLRPC$DecryptedMessageAction.end_seq_no) < (i3 = tLRPC$EncryptedChat.in_seq_no) || i2 < (i4 = tLRPC$DecryptedMessageAction.start_seq_no)) {
                    return null;
                } else {
                    if (i4 < i3) {
                        tLRPC$DecryptedMessageAction.start_seq_no = i3;
                    }
                    resendMessages(tLRPC$DecryptedMessageAction.start_seq_no, i2, tLRPC$EncryptedChat);
                }
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.e("unknown message " + tLObject);
            }
        } else if (BuildVars.LOGS_ENABLED) {
            FileLog.e("unknown TLObject");
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDecryptedObject$12(final long j) {
        TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(j);
        if (tLRPC$Dialog != null) {
            tLRPC$Dialog.unread_count = 0;
            getMessagesController().dialogMessage.remove(tLRPC$Dialog.id);
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                SecretChatHelper.this.lambda$processDecryptedObject$11(j);
            }
        });
        getMessagesStorage().deleteDialog(j, 1);
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        getNotificationCenter().postNotificationName(NotificationCenter.removeAllMessagesFromDialog, Long.valueOf(j), Boolean.FALSE, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDecryptedObject$11(final long j) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                SecretChatHelper.this.lambda$processDecryptedObject$10(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDecryptedObject$10(long j) {
        getNotificationsController().processReadMessages(null, j, 0, Integer.MAX_VALUE, false);
        LongSparseIntArray longSparseIntArray = new LongSparseIntArray(1);
        longSparseIntArray.put(j, 0);
        getNotificationsController().processDialogsUpdateRead(longSparseIntArray);
    }

    private TLRPC$Message createDeleteMessage(int i, int i2, int i3, long j, TLRPC$EncryptedChat tLRPC$EncryptedChat) {
        TLRPC$TL_messageService tLRPC$TL_messageService = new TLRPC$TL_messageService();
        TLRPC$TL_messageEncryptedAction tLRPC$TL_messageEncryptedAction = new TLRPC$TL_messageEncryptedAction();
        tLRPC$TL_messageService.action = tLRPC$TL_messageEncryptedAction;
        tLRPC$TL_messageEncryptedAction.encryptedAction = new TLRPC$TL_decryptedMessageActionDeleteMessages();
        tLRPC$TL_messageService.action.encryptedAction.random_ids.add(Long.valueOf(j));
        tLRPC$TL_messageService.id = i;
        tLRPC$TL_messageService.local_id = i;
        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
        tLRPC$TL_messageService.from_id = tLRPC$TL_peerUser;
        tLRPC$TL_peerUser.user_id = getUserConfig().getClientUserId();
        tLRPC$TL_messageService.unread = true;
        tLRPC$TL_messageService.out = true;
        tLRPC$TL_messageService.flags = 256;
        tLRPC$TL_messageService.dialog_id = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
        tLRPC$TL_messageService.send_state = 1;
        tLRPC$TL_messageService.seq_in = i3;
        tLRPC$TL_messageService.seq_out = i2;
        tLRPC$TL_messageService.peer_id = new TLRPC$TL_peerUser();
        if (tLRPC$EncryptedChat.participant_id == getUserConfig().getClientUserId()) {
            tLRPC$TL_messageService.peer_id.user_id = tLRPC$EncryptedChat.admin_id;
        } else {
            tLRPC$TL_messageService.peer_id.user_id = tLRPC$EncryptedChat.participant_id;
        }
        tLRPC$TL_messageService.date = 0;
        tLRPC$TL_messageService.random_id = j;
        return tLRPC$TL_messageService;
    }

    private void resendMessages(final int i, final int i2, final TLRPC$EncryptedChat tLRPC$EncryptedChat) {
        if (tLRPC$EncryptedChat == null || i2 - i < 0) {
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                SecretChatHelper.this.lambda$resendMessages$15(i, tLRPC$EncryptedChat, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v5, types: [java.util.ArrayList] */
    public /* synthetic */ void lambda$resendMessages$15(int i, TLRPC$EncryptedChat tLRPC$EncryptedChat, int i2) {
        int i3;
        long j;
        ArrayList<TLRPC$Message> arrayList;
        TLRPC$Message createDeleteMessage;
        try {
            int i4 = (tLRPC$EncryptedChat.admin_id == getUserConfig().getClientUserId() && i % 2 == 0) ? i + 1 : i;
            int i5 = 5;
            int i6 = 1;
            int i7 = 2;
            int i8 = 3;
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT uid FROM requested_holes WHERE uid = %d AND ((seq_out_start >= %d AND %d <= seq_out_end) OR (seq_out_start >= %d AND %d <= seq_out_end))", Integer.valueOf(tLRPC$EncryptedChat.id), Integer.valueOf(i4), Integer.valueOf(i4), Integer.valueOf(i2), Integer.valueOf(i2)), new Object[0]);
            boolean next = queryFinalized.next();
            queryFinalized.dispose();
            if (next) {
                return;
            }
            long makeEncryptedDialogId = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
            SparseArray sparseArray = new SparseArray();
            ArrayList<TLRPC$Message> arrayList2 = new ArrayList<>();
            for (int i9 = i4; i9 <= i2; i9 += 2) {
                sparseArray.put(i9, null);
            }
            SQLiteCursor queryFinalized2 = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT m.data, r.random_id, s.seq_in, s.seq_out, m.ttl, s.mid FROM messages_seq as s LEFT JOIN randoms_v2 as r ON r.mid = s.mid LEFT JOIN messages_v2 as m ON m.mid = s.mid WHERE m.uid = %d AND m.out = 1 AND s.seq_out >= %d AND s.seq_out <= %d ORDER BY seq_out ASC", Long.valueOf(makeEncryptedDialogId), Integer.valueOf(i4), Integer.valueOf(i2)), new Object[0]);
            while (queryFinalized2.next()) {
                long longValue = queryFinalized2.longValue(i6);
                if (longValue == 0) {
                    longValue = Utilities.random.nextLong();
                }
                long j2 = longValue;
                int intValue = queryFinalized2.intValue(i7);
                int intValue2 = queryFinalized2.intValue(i8);
                int intValue3 = queryFinalized2.intValue(i5);
                NativeByteBuffer byteBufferValue = queryFinalized2.byteBufferValue(0);
                if (byteBufferValue != null) {
                    createDeleteMessage = TLRPC$Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                    i3 = i4;
                    createDeleteMessage.readAttachPath(byteBufferValue, getUserConfig().clientUserId);
                    byteBufferValue.reuse();
                    createDeleteMessage.random_id = j2;
                    createDeleteMessage.dialog_id = makeEncryptedDialogId;
                    createDeleteMessage.seq_in = intValue;
                    createDeleteMessage.seq_out = intValue2;
                    createDeleteMessage.ttl = queryFinalized2.intValue(4);
                    j = makeEncryptedDialogId;
                    arrayList = arrayList2;
                } else {
                    i3 = i4;
                    j = makeEncryptedDialogId;
                    arrayList = arrayList2;
                    createDeleteMessage = createDeleteMessage(intValue3, intValue2, intValue, j2, tLRPC$EncryptedChat);
                }
                arrayList.add(createDeleteMessage);
                sparseArray.remove(intValue2);
                arrayList2 = arrayList;
                i4 = i3;
                makeEncryptedDialogId = j;
                i5 = 5;
                i6 = 1;
                i7 = 2;
                i8 = 3;
            }
            final ArrayList<TLRPC$Message> arrayList3 = arrayList2;
            int i10 = i4;
            queryFinalized2.dispose();
            if (sparseArray.size() != 0) {
                for (int i11 = 0; i11 < sparseArray.size(); i11++) {
                    int keyAt = sparseArray.keyAt(i11);
                    arrayList3.add(createDeleteMessage(getUserConfig().getNewMessageId(), keyAt, keyAt + 1, Utilities.random.nextLong(), tLRPC$EncryptedChat));
                }
                getUserConfig().saveConfig(false);
            }
            Collections.sort(arrayList3, SecretChatHelper$$ExternalSyntheticLambda25.INSTANCE);
            ArrayList<TLRPC$EncryptedChat> arrayList4 = new ArrayList<>();
            arrayList4.add(tLRPC$EncryptedChat);
            try {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        SecretChatHelper.this.lambda$resendMessages$14(arrayList3);
                    }
                });
                getSendMessagesHelper().processUnsentMessages(arrayList3, null, new ArrayList<>(), new ArrayList<>(), arrayList4);
                getMessagesStorage().getDatabase().executeFast(String.format(Locale.US, "REPLACE INTO requested_holes VALUES(%d, %d, %d)", Integer.valueOf(tLRPC$EncryptedChat.id), Integer.valueOf(i10), Integer.valueOf(i2))).stepThis().dispose();
            } catch (Exception e) {
                e = e;
                FileLog.e(e);
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$resendMessages$13(TLRPC$Message tLRPC$Message, TLRPC$Message tLRPC$Message2) {
        return AndroidUtilities.compare(tLRPC$Message.seq_out, tLRPC$Message2.seq_out);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resendMessages$14(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = new MessageObject(this.currentAccount, (TLRPC$Message) arrayList.get(i), false, true);
            messageObject.resendAsIs = true;
            getSendMessagesHelper().retrySendMessage(messageObject, true);
        }
    }

    public void checkSecretHoles(TLRPC$EncryptedChat tLRPC$EncryptedChat, ArrayList<TLRPC$Message> arrayList) {
        TL_decryptedMessageHolder tL_decryptedMessageHolder;
        TLRPC$TL_decryptedMessageLayer tLRPC$TL_decryptedMessageLayer;
        int i;
        int i2;
        ArrayList<TL_decryptedMessageHolder> arrayList2 = this.secretHolesQueue.get(tLRPC$EncryptedChat.id);
        if (arrayList2 == null) {
            return;
        }
        Collections.sort(arrayList2, SecretChatHelper$$ExternalSyntheticLambda24.INSTANCE);
        boolean z = false;
        while (arrayList2.size() > 0 && ((i = (tLRPC$TL_decryptedMessageLayer = (tL_decryptedMessageHolder = arrayList2.get(0)).layer).out_seq_no) == (i2 = tLRPC$EncryptedChat.seq_in) || i2 == i - 2)) {
            applyPeerLayer(tLRPC$EncryptedChat, tLRPC$TL_decryptedMessageLayer.layer);
            TLRPC$TL_decryptedMessageLayer tLRPC$TL_decryptedMessageLayer2 = tL_decryptedMessageHolder.layer;
            tLRPC$EncryptedChat.seq_in = tLRPC$TL_decryptedMessageLayer2.out_seq_no;
            tLRPC$EncryptedChat.in_seq_no = tLRPC$TL_decryptedMessageLayer2.in_seq_no;
            arrayList2.remove(0);
            if (tL_decryptedMessageHolder.decryptedWithVersion == 2) {
                tLRPC$EncryptedChat.mtproto_seq = Math.min(tLRPC$EncryptedChat.mtproto_seq, tLRPC$EncryptedChat.seq_in);
            }
            TLRPC$Message processDecryptedObject = processDecryptedObject(tLRPC$EncryptedChat, tL_decryptedMessageHolder.file, tL_decryptedMessageHolder.date, tL_decryptedMessageHolder.layer.message, tL_decryptedMessageHolder.new_key_used);
            if (processDecryptedObject != null) {
                arrayList.add(processDecryptedObject);
            }
            z = true;
        }
        if (arrayList2.isEmpty()) {
            this.secretHolesQueue.remove(tLRPC$EncryptedChat.id);
        }
        if (!z) {
            return;
        }
        getMessagesStorage().updateEncryptedChatSeq(tLRPC$EncryptedChat, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$checkSecretHoles$16(TL_decryptedMessageHolder tL_decryptedMessageHolder, TL_decryptedMessageHolder tL_decryptedMessageHolder2) {
        int i = tL_decryptedMessageHolder.layer.out_seq_no;
        int i2 = tL_decryptedMessageHolder2.layer.out_seq_no;
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x00e6, code lost:
        if (r0 > 1024) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00eb, code lost:
        r3 = r3 | true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00e9, code lost:
        if (r0 > 15) goto L29;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean decryptWithMtProtoVersion(NativeByteBuffer nativeByteBuffer, byte[] bArr, byte[] bArr2, int i, boolean z, boolean z2) {
        boolean z3 = false;
        boolean z4 = i == 1 ? false : z;
        MessageKeyData generateMessageKeyData = MessageKeyData.generateMessageKeyData(bArr, bArr2, z4, i);
        Utilities.aesIgeEncryption(nativeByteBuffer.buffer, generateMessageKeyData.aesKey, generateMessageKeyData.aesIv, false, false, 24, nativeByteBuffer.limit() - 24);
        int readInt32 = nativeByteBuffer.readInt32(false);
        if (i == 2) {
            int i2 = z4 ? 8 : 0;
            ByteBuffer byteBuffer = nativeByteBuffer.buffer;
            if (!Utilities.arraysEquals(bArr2, 0, Utilities.computeSHA256(bArr, i2 + 88, 32, byteBuffer, 24, byteBuffer.limit()), 8)) {
                if (z2) {
                    Utilities.aesIgeEncryption(nativeByteBuffer.buffer, generateMessageKeyData.aesKey, generateMessageKeyData.aesIv, true, false, 24, nativeByteBuffer.limit() - 24);
                    nativeByteBuffer.position(24);
                }
                z3 = true;
            }
        } else {
            int i3 = readInt32 + 28;
            if (i3 < nativeByteBuffer.buffer.limit() - 15 || i3 > nativeByteBuffer.buffer.limit()) {
                i3 = nativeByteBuffer.buffer.limit();
            }
            byte[] computeSHA1 = Utilities.computeSHA1(nativeByteBuffer.buffer, 24, i3);
            if (!Utilities.arraysEquals(bArr2, 0, computeSHA1, computeSHA1.length - 16)) {
                if (z2) {
                    Utilities.aesIgeEncryption(nativeByteBuffer.buffer, generateMessageKeyData.aesKey, generateMessageKeyData.aesIv, true, false, 24, nativeByteBuffer.limit() - 24);
                    nativeByteBuffer.position(24);
                }
                z3 = true;
            }
        }
        if (readInt32 <= 0) {
            z3 |= true;
        }
        if (readInt32 > nativeByteBuffer.limit() - 28) {
            z3 |= true;
        }
        int limit = (nativeByteBuffer.limit() - 28) - readInt32;
        if (i == 2) {
            if (limit < 12) {
                z3 |= true;
            }
        }
        return true ^ z3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0075 A[Catch: Exception -> 0x0252, TryCatch #0 {Exception -> 0x0252, blocks: (B:6:0x001a, B:8:0x001e, B:10:0x002a, B:11:0x0036, B:14:0x0041, B:16:0x005c, B:19:0x0075, B:22:0x008e, B:25:0x0097, B:28:0x00ac, B:32:0x00c6, B:34:0x00d7, B:35:0x00de, B:37:0x00e2, B:39:0x00e8, B:41:0x00ec, B:43:0x00fa, B:44:0x0101, B:45:0x0104, B:47:0x010b, B:49:0x010f, B:51:0x0115, B:53:0x0119, B:54:0x0155, B:59:0x015f, B:63:0x0166, B:65:0x0169, B:67:0x016d, B:68:0x0172, B:70:0x0187, B:71:0x0193, B:73:0x019a, B:75:0x01d4, B:78:0x01ed, B:79:0x01f5, B:80:0x021d, B:82:0x0230, B:83:0x0233, B:85:0x020e, B:87:0x0212, B:96:0x0238, B:98:0x023f, B:99:0x0060, B:103:0x006c), top: B:5:0x001a }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0238 A[Catch: Exception -> 0x0252, TryCatch #0 {Exception -> 0x0252, blocks: (B:6:0x001a, B:8:0x001e, B:10:0x002a, B:11:0x0036, B:14:0x0041, B:16:0x005c, B:19:0x0075, B:22:0x008e, B:25:0x0097, B:28:0x00ac, B:32:0x00c6, B:34:0x00d7, B:35:0x00de, B:37:0x00e2, B:39:0x00e8, B:41:0x00ec, B:43:0x00fa, B:44:0x0101, B:45:0x0104, B:47:0x010b, B:49:0x010f, B:51:0x0115, B:53:0x0119, B:54:0x0155, B:59:0x015f, B:63:0x0166, B:65:0x0169, B:67:0x016d, B:68:0x0172, B:70:0x0187, B:71:0x0193, B:73:0x019a, B:75:0x01d4, B:78:0x01ed, B:79:0x01f5, B:80:0x021d, B:82:0x0230, B:83:0x0233, B:85:0x020e, B:87:0x0212, B:96:0x0238, B:98:0x023f, B:99:0x0060, B:103:0x006c), top: B:5:0x001a }] */
    /* JADX WARN: Type inference failed for: r1v44, types: [java.util.ArrayList] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ArrayList<TLRPC$Message> decryptMessage(TLRPC$EncryptedMessage tLRPC$EncryptedMessage) {
        byte[] bArr;
        boolean z;
        int i;
        int i2;
        TLRPC$DecryptedMessage tLRPC$DecryptedMessage;
        int i3;
        TLRPC$EncryptedChat encryptedChatDB = getMessagesController().getEncryptedChatDB(tLRPC$EncryptedMessage.chat_id, true);
        if (encryptedChatDB != null && !(encryptedChatDB instanceof TLRPC$TL_encryptedChatDiscarded)) {
            try {
                if (encryptedChatDB instanceof TLRPC$TL_encryptedChatWaiting) {
                    ArrayList<TLRPC$Update> arrayList = this.pendingSecretMessages.get(encryptedChatDB.id);
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                        this.pendingSecretMessages.put(encryptedChatDB.id, arrayList);
                    }
                    TLRPC$TL_updateNewEncryptedMessage tLRPC$TL_updateNewEncryptedMessage = new TLRPC$TL_updateNewEncryptedMessage();
                    tLRPC$TL_updateNewEncryptedMessage.message = tLRPC$EncryptedMessage;
                    arrayList.add(tLRPC$TL_updateNewEncryptedMessage);
                    return null;
                }
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tLRPC$EncryptedMessage.bytes.length);
                nativeByteBuffer.writeBytes(tLRPC$EncryptedMessage.bytes);
                nativeByteBuffer.position(0);
                long readInt64 = nativeByteBuffer.readInt64(false);
                if (encryptedChatDB.key_fingerprint == readInt64) {
                    bArr = encryptedChatDB.auth_key;
                } else {
                    long j = encryptedChatDB.future_key_fingerprint;
                    if (j == 0 || j != readInt64) {
                        bArr = null;
                    } else {
                        bArr = encryptedChatDB.future_auth_key;
                        z = true;
                        if (bArr == null) {
                            byte[] readData = nativeByteBuffer.readData(16, false);
                            boolean z2 = encryptedChatDB.admin_id == getUserConfig().getClientUserId();
                            boolean z3 = encryptedChatDB.mtproto_seq == 0;
                            boolean z4 = z;
                            if (!decryptWithMtProtoVersion(nativeByteBuffer, bArr, readData, 2, z2, z3)) {
                                if (z3) {
                                    i = 2;
                                    if (decryptWithMtProtoVersion(nativeByteBuffer, bArr, readData, 1, z2, false)) {
                                        i2 = 1;
                                    }
                                }
                                return null;
                            }
                            i = 2;
                            i2 = 2;
                            TLObject TLdeserialize = TLClassStore.Instance().TLdeserialize(nativeByteBuffer, nativeByteBuffer.readInt32(false), false);
                            nativeByteBuffer.reuse();
                            if (!z4) {
                                encryptedChatDB.key_use_count_in = (short) (encryptedChatDB.key_use_count_in + 1);
                            }
                            if (TLdeserialize instanceof TLRPC$TL_decryptedMessageLayer) {
                                TLRPC$TL_decryptedMessageLayer tLRPC$TL_decryptedMessageLayer = (TLRPC$TL_decryptedMessageLayer) TLdeserialize;
                                if (encryptedChatDB.seq_in == 0 && encryptedChatDB.seq_out == 0) {
                                    if (encryptedChatDB.admin_id == getUserConfig().getClientUserId()) {
                                        encryptedChatDB.seq_out = 1;
                                        encryptedChatDB.seq_in = -2;
                                    } else {
                                        encryptedChatDB.seq_in = -1;
                                    }
                                }
                                if (tLRPC$TL_decryptedMessageLayer.random_bytes.length < 15) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.e("got random bytes less than needed");
                                    }
                                    return null;
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("current chat in_seq = " + encryptedChatDB.seq_in + " out_seq = " + encryptedChatDB.seq_out);
                                    FileLog.d("got message with in_seq = " + tLRPC$TL_decryptedMessageLayer.in_seq_no + " out_seq = " + tLRPC$TL_decryptedMessageLayer.out_seq_no);
                                }
                                int i4 = tLRPC$TL_decryptedMessageLayer.out_seq_no;
                                int i5 = encryptedChatDB.seq_in;
                                if (i4 <= i5) {
                                    return null;
                                }
                                if (i2 == 1 && (i3 = encryptedChatDB.mtproto_seq) != 0 && i4 >= i3) {
                                    return null;
                                }
                                if (i5 != i4 - i) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.e("got hole");
                                    }
                                    sendResendMessage(encryptedChatDB, encryptedChatDB.seq_in + i, tLRPC$TL_decryptedMessageLayer.out_seq_no - i, null);
                                    ArrayList<TL_decryptedMessageHolder> arrayList2 = this.secretHolesQueue.get(encryptedChatDB.id);
                                    if (arrayList2 == null) {
                                        arrayList2 = new ArrayList<>();
                                        this.secretHolesQueue.put(encryptedChatDB.id, arrayList2);
                                    }
                                    if (arrayList2.size() >= 4) {
                                        this.secretHolesQueue.remove(encryptedChatDB.id);
                                        final TLRPC$TL_encryptedChatDiscarded tLRPC$TL_encryptedChatDiscarded = new TLRPC$TL_encryptedChatDiscarded();
                                        tLRPC$TL_encryptedChatDiscarded.id = encryptedChatDB.id;
                                        tLRPC$TL_encryptedChatDiscarded.user_id = encryptedChatDB.user_id;
                                        tLRPC$TL_encryptedChatDiscarded.auth_key = encryptedChatDB.auth_key;
                                        tLRPC$TL_encryptedChatDiscarded.key_create_date = encryptedChatDB.key_create_date;
                                        tLRPC$TL_encryptedChatDiscarded.key_use_count_in = encryptedChatDB.key_use_count_in;
                                        tLRPC$TL_encryptedChatDiscarded.key_use_count_out = encryptedChatDB.key_use_count_out;
                                        tLRPC$TL_encryptedChatDiscarded.seq_in = encryptedChatDB.seq_in;
                                        tLRPC$TL_encryptedChatDiscarded.seq_out = encryptedChatDB.seq_out;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda22
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                SecretChatHelper.this.lambda$decryptMessage$17(tLRPC$TL_encryptedChatDiscarded);
                                            }
                                        });
                                        declineSecretChat(encryptedChatDB.id, false);
                                        return null;
                                    }
                                    TL_decryptedMessageHolder tL_decryptedMessageHolder = new TL_decryptedMessageHolder();
                                    tL_decryptedMessageHolder.layer = tLRPC$TL_decryptedMessageLayer;
                                    tL_decryptedMessageHolder.file = tLRPC$EncryptedMessage.file;
                                    tL_decryptedMessageHolder.date = tLRPC$EncryptedMessage.date;
                                    tL_decryptedMessageHolder.new_key_used = z4;
                                    tL_decryptedMessageHolder.decryptedWithVersion = i2;
                                    arrayList2.add(tL_decryptedMessageHolder);
                                    return null;
                                }
                                if (i2 == i) {
                                    encryptedChatDB.mtproto_seq = Math.min(encryptedChatDB.mtproto_seq, i5);
                                }
                                applyPeerLayer(encryptedChatDB, tLRPC$TL_decryptedMessageLayer.layer);
                                encryptedChatDB.seq_in = tLRPC$TL_decryptedMessageLayer.out_seq_no;
                                encryptedChatDB.in_seq_no = tLRPC$TL_decryptedMessageLayer.in_seq_no;
                                getMessagesStorage().updateEncryptedChatSeq(encryptedChatDB, true);
                                tLRPC$DecryptedMessage = tLRPC$TL_decryptedMessageLayer.message;
                            } else {
                                if ((TLdeserialize instanceof TLRPC$TL_decryptedMessageService) && (((TLRPC$TL_decryptedMessageService) TLdeserialize).action instanceof TLRPC$TL_decryptedMessageActionNotifyLayer)) {
                                    tLRPC$DecryptedMessage = TLdeserialize;
                                }
                                return null;
                            }
                            ArrayList<TLRPC$Message> arrayList3 = new ArrayList<>();
                            TLRPC$Message processDecryptedObject = processDecryptedObject(encryptedChatDB, tLRPC$EncryptedMessage.file, tLRPC$EncryptedMessage.date, tLRPC$DecryptedMessage, z4);
                            if (processDecryptedObject != null) {
                                arrayList3.add(processDecryptedObject);
                            }
                            checkSecretHoles(encryptedChatDB, arrayList3);
                            return arrayList3;
                        }
                        nativeByteBuffer.reuse();
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.e(String.format("fingerprint mismatch %x", Long.valueOf(readInt64)));
                        }
                    }
                }
                z = false;
                if (bArr == null) {
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$decryptMessage$17(TLRPC$TL_encryptedChatDiscarded tLRPC$TL_encryptedChatDiscarded) {
        getMessagesController().putEncryptedChat(tLRPC$TL_encryptedChatDiscarded, false);
        getMessagesStorage().updateEncryptedChat(tLRPC$TL_encryptedChatDiscarded);
        getNotificationCenter().postNotificationName(NotificationCenter.encryptedChatUpdated, tLRPC$TL_encryptedChatDiscarded);
    }

    public void requestNewSecretChatKey(TLRPC$EncryptedChat tLRPC$EncryptedChat) {
        byte[] bArr = new byte[256];
        Utilities.random.nextBytes(bArr);
        byte[] byteArray = BigInteger.valueOf(getMessagesStorage().getSecretG()).modPow(new BigInteger(1, bArr), new BigInteger(1, getMessagesStorage().getSecretPBytes())).toByteArray();
        if (byteArray.length > 256) {
            byte[] bArr2 = new byte[256];
            System.arraycopy(byteArray, 1, bArr2, 0, 256);
            byteArray = bArr2;
        }
        tLRPC$EncryptedChat.exchange_id = getSendMessagesHelper().getNextRandomId();
        tLRPC$EncryptedChat.a_or_b = bArr;
        tLRPC$EncryptedChat.g_a = byteArray;
        getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
        sendRequestKeyMessage(tLRPC$EncryptedChat, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00b4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void processAcceptedSecretChat(final TLRPC$EncryptedChat tLRPC$EncryptedChat) {
        byte[] bArr;
        byte[] bArr2;
        BigInteger bigInteger = new BigInteger(1, getMessagesStorage().getSecretPBytes());
        BigInteger bigInteger2 = new BigInteger(1, tLRPC$EncryptedChat.g_a_or_b);
        if (!Utilities.isGoodGaAndGb(bigInteger2, bigInteger)) {
            declineSecretChat(tLRPC$EncryptedChat.id, false);
            return;
        }
        byte[] byteArray = bigInteger2.modPow(new BigInteger(1, tLRPC$EncryptedChat.a_or_b), bigInteger).toByteArray();
        if (byteArray.length > 256) {
            bArr = new byte[256];
            System.arraycopy(byteArray, byteArray.length - 256, bArr, 0, 256);
        } else {
            if (byteArray.length < 256) {
                bArr = new byte[256];
                System.arraycopy(byteArray, 0, bArr, 256 - byteArray.length, byteArray.length);
                for (int i = 0; i < 256 - byteArray.length; i++) {
                    bArr[i] = 0;
                }
            }
            byte[] computeSHA1 = Utilities.computeSHA1(byteArray);
            bArr2 = new byte[8];
            System.arraycopy(computeSHA1, computeSHA1.length - 8, bArr2, 0, 8);
            if (tLRPC$EncryptedChat.key_fingerprint != Utilities.bytesToLong(bArr2)) {
                tLRPC$EncryptedChat.auth_key = byteArray;
                tLRPC$EncryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
                tLRPC$EncryptedChat.seq_in = -2;
                tLRPC$EncryptedChat.seq_out = 1;
                getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat);
                getMessagesController().putEncryptedChat(tLRPC$EncryptedChat, false);
                ArrayList<TLRPC$Update> arrayList = this.pendingSecretMessages.get(tLRPC$EncryptedChat.id);
                if (arrayList != null) {
                    getMessagesController().processUpdateArray(arrayList, null, null, false, 0);
                    this.pendingSecretMessages.remove(tLRPC$EncryptedChat.id);
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda15
                    @Override // java.lang.Runnable
                    public final void run() {
                        SecretChatHelper.this.lambda$processAcceptedSecretChat$18(tLRPC$EncryptedChat);
                    }
                });
                return;
            }
            final TLRPC$TL_encryptedChatDiscarded tLRPC$TL_encryptedChatDiscarded = new TLRPC$TL_encryptedChatDiscarded();
            tLRPC$TL_encryptedChatDiscarded.id = tLRPC$EncryptedChat.id;
            tLRPC$TL_encryptedChatDiscarded.user_id = tLRPC$EncryptedChat.user_id;
            tLRPC$TL_encryptedChatDiscarded.auth_key = tLRPC$EncryptedChat.auth_key;
            tLRPC$TL_encryptedChatDiscarded.key_create_date = tLRPC$EncryptedChat.key_create_date;
            tLRPC$TL_encryptedChatDiscarded.key_use_count_in = tLRPC$EncryptedChat.key_use_count_in;
            tLRPC$TL_encryptedChatDiscarded.key_use_count_out = tLRPC$EncryptedChat.key_use_count_out;
            tLRPC$TL_encryptedChatDiscarded.seq_in = tLRPC$EncryptedChat.seq_in;
            tLRPC$TL_encryptedChatDiscarded.seq_out = tLRPC$EncryptedChat.seq_out;
            tLRPC$TL_encryptedChatDiscarded.admin_id = tLRPC$EncryptedChat.admin_id;
            tLRPC$TL_encryptedChatDiscarded.mtproto_seq = tLRPC$EncryptedChat.mtproto_seq;
            getMessagesStorage().updateEncryptedChat(tLRPC$TL_encryptedChatDiscarded);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    SecretChatHelper.this.lambda$processAcceptedSecretChat$19(tLRPC$TL_encryptedChatDiscarded);
                }
            });
            declineSecretChat(tLRPC$EncryptedChat.id, false);
            return;
        }
        byteArray = bArr;
        byte[] computeSHA12 = Utilities.computeSHA1(byteArray);
        bArr2 = new byte[8];
        System.arraycopy(computeSHA12, computeSHA12.length - 8, bArr2, 0, 8);
        if (tLRPC$EncryptedChat.key_fingerprint != Utilities.bytesToLong(bArr2)) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAcceptedSecretChat$18(TLRPC$EncryptedChat tLRPC$EncryptedChat) {
        getNotificationCenter().postNotificationName(NotificationCenter.encryptedChatUpdated, tLRPC$EncryptedChat);
        sendNotifyLayerMessage(tLRPC$EncryptedChat, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAcceptedSecretChat$19(TLRPC$TL_encryptedChatDiscarded tLRPC$TL_encryptedChatDiscarded) {
        getMessagesController().putEncryptedChat(tLRPC$TL_encryptedChatDiscarded, false);
        getNotificationCenter().postNotificationName(NotificationCenter.encryptedChatUpdated, tLRPC$TL_encryptedChatDiscarded);
    }

    public void declineSecretChat(int i, boolean z) {
        declineSecretChat(i, z, 0L);
    }

    public void declineSecretChat(int i, boolean z, final long j) {
        NativeByteBuffer nativeByteBuffer;
        Exception e;
        if (j == 0) {
            try {
                nativeByteBuffer = new NativeByteBuffer(12);
            } catch (Exception e2) {
                nativeByteBuffer = null;
                e = e2;
            }
            try {
                nativeByteBuffer.writeInt32(100);
                nativeByteBuffer.writeInt32(i);
                nativeByteBuffer.writeBool(z);
            } catch (Exception e3) {
                e = e3;
                FileLog.e(e);
                j = getMessagesStorage().createPendingTask(nativeByteBuffer);
                TLRPC$TL_messages_discardEncryption tLRPC$TL_messages_discardEncryption = new TLRPC$TL_messages_discardEncryption();
                tLRPC$TL_messages_discardEncryption.chat_id = i;
                tLRPC$TL_messages_discardEncryption.delete_history = z;
                getConnectionsManager().sendRequest(tLRPC$TL_messages_discardEncryption, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda26
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        SecretChatHelper.this.lambda$declineSecretChat$20(j, tLObject, tLRPC$TL_error);
                    }
                });
            }
            j = getMessagesStorage().createPendingTask(nativeByteBuffer);
        }
        TLRPC$TL_messages_discardEncryption tLRPC$TL_messages_discardEncryption2 = new TLRPC$TL_messages_discardEncryption();
        tLRPC$TL_messages_discardEncryption2.chat_id = i;
        tLRPC$TL_messages_discardEncryption2.delete_history = z;
        getConnectionsManager().sendRequest(tLRPC$TL_messages_discardEncryption2, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda26
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                SecretChatHelper.this.lambda$declineSecretChat$20(j, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$declineSecretChat$20(long j, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (j != 0) {
            getMessagesStorage().removePendingTask(j);
        }
    }

    public void acceptSecretChat(final TLRPC$EncryptedChat tLRPC$EncryptedChat) {
        if (this.acceptingChats.get(tLRPC$EncryptedChat.id) != null) {
            return;
        }
        this.acceptingChats.put(tLRPC$EncryptedChat.id, tLRPC$EncryptedChat);
        TLRPC$TL_messages_getDhConfig tLRPC$TL_messages_getDhConfig = new TLRPC$TL_messages_getDhConfig();
        tLRPC$TL_messages_getDhConfig.random_length = 256;
        tLRPC$TL_messages_getDhConfig.version = getMessagesStorage().getLastSecretVersion();
        getConnectionsManager().sendRequest(tLRPC$TL_messages_getDhConfig, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda30
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                SecretChatHelper.this.lambda$acceptSecretChat$23(tLRPC$EncryptedChat, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acceptSecretChat$23(final TLRPC$EncryptedChat tLRPC$EncryptedChat, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        byte[] bArr;
        if (tLRPC$TL_error == null) {
            TLRPC$messages_DhConfig tLRPC$messages_DhConfig = (TLRPC$messages_DhConfig) tLObject;
            if (tLObject instanceof TLRPC$TL_messages_dhConfig) {
                if (!Utilities.isGoodPrime(tLRPC$messages_DhConfig.p, tLRPC$messages_DhConfig.g)) {
                    this.acceptingChats.remove(tLRPC$EncryptedChat.id);
                    declineSecretChat(tLRPC$EncryptedChat.id, false);
                    return;
                }
                getMessagesStorage().setSecretPBytes(tLRPC$messages_DhConfig.p);
                getMessagesStorage().setSecretG(tLRPC$messages_DhConfig.g);
                getMessagesStorage().setLastSecretVersion(tLRPC$messages_DhConfig.version);
                getMessagesStorage().saveSecretParams(getMessagesStorage().getLastSecretVersion(), getMessagesStorage().getSecretG(), getMessagesStorage().getSecretPBytes());
            }
            byte[] bArr2 = new byte[256];
            for (int i = 0; i < 256; i++) {
                bArr2[i] = (byte) (((byte) (Utilities.random.nextDouble() * 256.0d)) ^ tLRPC$messages_DhConfig.random[i]);
            }
            tLRPC$EncryptedChat.a_or_b = bArr2;
            tLRPC$EncryptedChat.seq_in = -1;
            tLRPC$EncryptedChat.seq_out = 0;
            BigInteger bigInteger = new BigInteger(1, getMessagesStorage().getSecretPBytes());
            BigInteger modPow = BigInteger.valueOf(getMessagesStorage().getSecretG()).modPow(new BigInteger(1, bArr2), bigInteger);
            BigInteger bigInteger2 = new BigInteger(1, tLRPC$EncryptedChat.g_a);
            if (!Utilities.isGoodGaAndGb(bigInteger2, bigInteger)) {
                this.acceptingChats.remove(tLRPC$EncryptedChat.id);
                declineSecretChat(tLRPC$EncryptedChat.id, false);
                return;
            }
            byte[] byteArray = modPow.toByteArray();
            if (byteArray.length > 256) {
                byte[] bArr3 = new byte[256];
                System.arraycopy(byteArray, 1, bArr3, 0, 256);
                byteArray = bArr3;
            }
            byte[] byteArray2 = bigInteger2.modPow(new BigInteger(1, bArr2), bigInteger).toByteArray();
            if (byteArray2.length > 256) {
                bArr = new byte[256];
                System.arraycopy(byteArray2, byteArray2.length - 256, bArr, 0, 256);
            } else {
                if (byteArray2.length < 256) {
                    bArr = new byte[256];
                    System.arraycopy(byteArray2, 0, bArr, 256 - byteArray2.length, byteArray2.length);
                    for (int i2 = 0; i2 < 256 - byteArray2.length; i2++) {
                        bArr[i2] = 0;
                    }
                }
                byte[] computeSHA1 = Utilities.computeSHA1(byteArray2);
                byte[] bArr4 = new byte[8];
                System.arraycopy(computeSHA1, computeSHA1.length - 8, bArr4, 0, 8);
                tLRPC$EncryptedChat.auth_key = byteArray2;
                tLRPC$EncryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
                TLRPC$TL_messages_acceptEncryption tLRPC$TL_messages_acceptEncryption = new TLRPC$TL_messages_acceptEncryption();
                tLRPC$TL_messages_acceptEncryption.g_b = byteArray;
                TLRPC$TL_inputEncryptedChat tLRPC$TL_inputEncryptedChat = new TLRPC$TL_inputEncryptedChat();
                tLRPC$TL_messages_acceptEncryption.peer = tLRPC$TL_inputEncryptedChat;
                tLRPC$TL_inputEncryptedChat.chat_id = tLRPC$EncryptedChat.id;
                tLRPC$TL_inputEncryptedChat.access_hash = tLRPC$EncryptedChat.access_hash;
                tLRPC$TL_messages_acceptEncryption.key_fingerprint = Utilities.bytesToLong(bArr4);
                getConnectionsManager().sendRequest(tLRPC$TL_messages_acceptEncryption, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda31
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                        SecretChatHelper.this.lambda$acceptSecretChat$22(tLRPC$EncryptedChat, tLObject2, tLRPC$TL_error2);
                    }
                }, 64);
                return;
            }
            byteArray2 = bArr;
            byte[] computeSHA12 = Utilities.computeSHA1(byteArray2);
            byte[] bArr42 = new byte[8];
            System.arraycopy(computeSHA12, computeSHA12.length - 8, bArr42, 0, 8);
            tLRPC$EncryptedChat.auth_key = byteArray2;
            tLRPC$EncryptedChat.key_create_date = getConnectionsManager().getCurrentTime();
            TLRPC$TL_messages_acceptEncryption tLRPC$TL_messages_acceptEncryption2 = new TLRPC$TL_messages_acceptEncryption();
            tLRPC$TL_messages_acceptEncryption2.g_b = byteArray;
            TLRPC$TL_inputEncryptedChat tLRPC$TL_inputEncryptedChat2 = new TLRPC$TL_inputEncryptedChat();
            tLRPC$TL_messages_acceptEncryption2.peer = tLRPC$TL_inputEncryptedChat2;
            tLRPC$TL_inputEncryptedChat2.chat_id = tLRPC$EncryptedChat.id;
            tLRPC$TL_inputEncryptedChat2.access_hash = tLRPC$EncryptedChat.access_hash;
            tLRPC$TL_messages_acceptEncryption2.key_fingerprint = Utilities.bytesToLong(bArr42);
            getConnectionsManager().sendRequest(tLRPC$TL_messages_acceptEncryption2, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda31
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                    SecretChatHelper.this.lambda$acceptSecretChat$22(tLRPC$EncryptedChat, tLObject2, tLRPC$TL_error2);
                }
            }, 64);
            return;
        }
        this.acceptingChats.remove(tLRPC$EncryptedChat.id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acceptSecretChat$22(TLRPC$EncryptedChat tLRPC$EncryptedChat, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.acceptingChats.remove(tLRPC$EncryptedChat.id);
        if (tLRPC$TL_error == null) {
            final TLRPC$EncryptedChat tLRPC$EncryptedChat2 = (TLRPC$EncryptedChat) tLObject;
            tLRPC$EncryptedChat2.auth_key = tLRPC$EncryptedChat.auth_key;
            tLRPC$EncryptedChat2.user_id = tLRPC$EncryptedChat.user_id;
            tLRPC$EncryptedChat2.seq_in = tLRPC$EncryptedChat.seq_in;
            tLRPC$EncryptedChat2.seq_out = tLRPC$EncryptedChat.seq_out;
            tLRPC$EncryptedChat2.key_create_date = tLRPC$EncryptedChat.key_create_date;
            tLRPC$EncryptedChat2.key_use_count_in = tLRPC$EncryptedChat.key_use_count_in;
            tLRPC$EncryptedChat2.key_use_count_out = tLRPC$EncryptedChat.key_use_count_out;
            getMessagesStorage().updateEncryptedChat(tLRPC$EncryptedChat2);
            getMessagesController().putEncryptedChat(tLRPC$EncryptedChat2, false);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    SecretChatHelper.this.lambda$acceptSecretChat$21(tLRPC$EncryptedChat2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acceptSecretChat$21(TLRPC$EncryptedChat tLRPC$EncryptedChat) {
        getNotificationCenter().postNotificationName(NotificationCenter.encryptedChatUpdated, tLRPC$EncryptedChat);
        sendNotifyLayerMessage(tLRPC$EncryptedChat, null);
    }

    public void startSecretChat(final Context context, final TLRPC$User tLRPC$User) {
        if (tLRPC$User == null || context == null) {
            return;
        }
        this.startingSecretChat = true;
        final AlertDialog alertDialog = new AlertDialog(context, 3);
        TLRPC$TL_messages_getDhConfig tLRPC$TL_messages_getDhConfig = new TLRPC$TL_messages_getDhConfig();
        tLRPC$TL_messages_getDhConfig.random_length = 256;
        tLRPC$TL_messages_getDhConfig.version = getMessagesStorage().getLastSecretVersion();
        final int sendRequest = getConnectionsManager().sendRequest(tLRPC$TL_messages_getDhConfig, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda27
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                SecretChatHelper.this.lambda$startSecretChat$30(context, alertDialog, tLRPC$User, tLObject, tLRPC$TL_error);
            }
        }, 2);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                SecretChatHelper.this.lambda$startSecretChat$31(sendRequest, dialogInterface);
            }
        });
        try {
            alertDialog.show();
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$30(final Context context, final AlertDialog alertDialog, final TLRPC$User tLRPC$User, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            TLRPC$messages_DhConfig tLRPC$messages_DhConfig = (TLRPC$messages_DhConfig) tLObject;
            if (tLObject instanceof TLRPC$TL_messages_dhConfig) {
                if (!Utilities.isGoodPrime(tLRPC$messages_DhConfig.p, tLRPC$messages_DhConfig.g)) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            SecretChatHelper.lambda$startSecretChat$24(context, alertDialog);
                        }
                    });
                    return;
                }
                getMessagesStorage().setSecretPBytes(tLRPC$messages_DhConfig.p);
                getMessagesStorage().setSecretG(tLRPC$messages_DhConfig.g);
                getMessagesStorage().setLastSecretVersion(tLRPC$messages_DhConfig.version);
                getMessagesStorage().saveSecretParams(getMessagesStorage().getLastSecretVersion(), getMessagesStorage().getSecretG(), getMessagesStorage().getSecretPBytes());
            }
            final byte[] bArr = new byte[256];
            for (int i = 0; i < 256; i++) {
                bArr[i] = (byte) (((byte) (Utilities.random.nextDouble() * 256.0d)) ^ tLRPC$messages_DhConfig.random[i]);
            }
            byte[] byteArray = BigInteger.valueOf(getMessagesStorage().getSecretG()).modPow(new BigInteger(1, bArr), new BigInteger(1, getMessagesStorage().getSecretPBytes())).toByteArray();
            if (byteArray.length > 256) {
                byte[] bArr2 = new byte[256];
                System.arraycopy(byteArray, 1, bArr2, 0, 256);
                byteArray = bArr2;
            }
            TLRPC$TL_messages_requestEncryption tLRPC$TL_messages_requestEncryption = new TLRPC$TL_messages_requestEncryption();
            tLRPC$TL_messages_requestEncryption.g_a = byteArray;
            tLRPC$TL_messages_requestEncryption.user_id = getMessagesController().getInputUser(tLRPC$User);
            tLRPC$TL_messages_requestEncryption.random_id = Utilities.random.nextInt();
            getConnectionsManager().sendRequest(tLRPC$TL_messages_requestEncryption, new RequestDelegate() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda28
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                    SecretChatHelper.this.lambda$startSecretChat$28(context, alertDialog, bArr, tLRPC$User, tLObject2, tLRPC$TL_error2);
                }
            }, 2);
            return;
        }
        this.delayedEncryptedChatUpdates.clear();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                SecretChatHelper.this.lambda$startSecretChat$29(context, alertDialog);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startSecretChat$24(Context context, AlertDialog alertDialog) {
        try {
            if (((Activity) context).isFinishing()) {
                return;
            }
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$28(final Context context, final AlertDialog alertDialog, final byte[] bArr, final TLRPC$User tLRPC$User, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    SecretChatHelper.this.lambda$startSecretChat$26(context, alertDialog, tLObject, bArr, tLRPC$User);
                }
            });
            return;
        }
        this.delayedEncryptedChatUpdates.clear();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                SecretChatHelper.this.lambda$startSecretChat$27(context, alertDialog);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$26(Context context, AlertDialog alertDialog, TLObject tLObject, byte[] bArr, TLRPC$User tLRPC$User) {
        this.startingSecretChat = false;
        if (!((Activity) context).isFinishing()) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        TLRPC$EncryptedChat tLRPC$EncryptedChat = (TLRPC$EncryptedChat) tLObject;
        tLRPC$EncryptedChat.user_id = tLRPC$EncryptedChat.participant_id;
        tLRPC$EncryptedChat.seq_in = -2;
        tLRPC$EncryptedChat.seq_out = 1;
        tLRPC$EncryptedChat.a_or_b = bArr;
        getMessagesController().putEncryptedChat(tLRPC$EncryptedChat, false);
        TLRPC$TL_dialog tLRPC$TL_dialog = new TLRPC$TL_dialog();
        tLRPC$TL_dialog.id = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
        tLRPC$TL_dialog.unread_count = 0;
        tLRPC$TL_dialog.top_message = 0;
        tLRPC$TL_dialog.last_message_date = getConnectionsManager().getCurrentTime();
        getMessagesController().dialogs_dict.put(tLRPC$TL_dialog.id, tLRPC$TL_dialog);
        getMessagesController().allDialogs.add(tLRPC$TL_dialog);
        getMessagesController().sortDialogs(null);
        getMessagesStorage().putEncryptedChat(tLRPC$EncryptedChat, tLRPC$User, tLRPC$TL_dialog);
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        getNotificationCenter().postNotificationName(NotificationCenter.encryptedChatCreated, tLRPC$EncryptedChat);
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SecretChatHelper$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                SecretChatHelper.this.lambda$startSecretChat$25();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$25() {
        if (!this.delayedEncryptedChatUpdates.isEmpty()) {
            getMessagesController().processUpdateArray(this.delayedEncryptedChatUpdates, null, null, false, 0);
            this.delayedEncryptedChatUpdates.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$27(Context context, AlertDialog alertDialog) {
        if (!((Activity) context).isFinishing()) {
            this.startingSecretChat = false;
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(LocaleController.getString("AppName", org.telegram.messenger.beta.R.string.AppName));
            builder.setMessage(LocaleController.getString("CreateEncryptedChatError", org.telegram.messenger.beta.R.string.CreateEncryptedChatError));
            builder.setPositiveButton(LocaleController.getString("OK", org.telegram.messenger.beta.R.string.OK), null);
            builder.show().setCanceledOnTouchOutside(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$29(Context context, AlertDialog alertDialog) {
        this.startingSecretChat = false;
        if (!((Activity) context).isFinishing()) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSecretChat$31(int i, DialogInterface dialogInterface) {
        getConnectionsManager().cancelRequest(i, true);
    }
}
