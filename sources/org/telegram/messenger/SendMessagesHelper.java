package org.telegram.messenger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseArray;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.collection.LongSparseArray;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.json.JSONObject;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.QuickAckDelegate;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Business.QuickRepliesController;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.Reactions.ReactionsUtils;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PaymentFormActivity;
import org.telegram.ui.Stars.StarsController;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.TwoStepVerificationActivity;
import org.telegram.ui.TwoStepVerificationSetupActivity;
import org.telegram.ui.bots.BotWebViewSheet;
import org.webrtc.MediaStreamTrack;

/* loaded from: classes.dex */
public class SendMessagesHelper extends BaseController implements NotificationCenter.NotificationCenterDelegate {
    private static final int ERROR_TYPE_FILE_TOO_LARGE = 2;
    private static final int ERROR_TYPE_UNSUPPORTED = 1;
    private static volatile SendMessagesHelper[] Instance = null;
    public static final int MEDIA_TYPE_DICE = 11;
    public static final int MEDIA_TYPE_STORY = 12;
    private static DispatchQueue mediaSendQueue = new DispatchQueue("mediaSendQueue");
    private static ThreadPoolExecutor mediaSendThreadPool;
    private HashMap<String, ArrayList<DelayedMessage>> delayedMessages;
    private SparseArray<TLRPC.Message> editingMessages;
    private HashMap<String, ImportingHistory> importingHistoryFiles;
    private LongSparseArray importingHistoryMap;
    private HashMap<String, ImportingStickers> importingStickersFiles;
    private HashMap<String, ImportingStickers> importingStickersMap;
    private LocationProvider locationProvider;
    private SparseArray<TLRPC.Message> sendingMessages;
    private LongSparseArray sendingMessagesIdDialogs;
    private SparseArray<MessageObject> unsentMessages;
    private SparseArray<TLRPC.Message> uploadMessages;
    private LongSparseArray uploadingMessagesIdDialogs;
    private LongSparseArray voteSendTime;
    private HashMap<String, Boolean> waitingForCallback;
    private HashMap<String, List<String>> waitingForCallbackMap;
    private HashMap<String, MessageObject> waitingForLocation;
    private HashMap<String, byte[]> waitingForVote;

    /* loaded from: classes3.dex */
    protected class DelayedMessage {
        public TLRPC.EncryptedChat encryptedChat;
        public HashMap<Object, Object> extraHashMap;
        public int finalGroupMessage;
        public long groupId;
        public String httpLocation;
        public ArrayList<String> httpLocations;
        public ArrayList<TLRPC.InputMedia> inputMedias;
        public TLRPC.InputMedia inputUploadMedia;
        public TLObject locationParent;
        public ArrayList<TLRPC.PhotoSize> locations;
        public ArrayList<MessageObject> messageObjects;
        public ArrayList<TLRPC.Message> messages;
        public MessageObject obj;
        public String originalPath;
        public ArrayList<String> originalPaths;
        public boolean paidMedia;
        public Object parentObject;
        public ArrayList<Object> parentObjects;
        public long peer;
        public boolean performMediaUpload;
        public TLRPC.PhotoSize photoSize;
        ArrayList<DelayedMessageSendAfterRequest> requests;
        private boolean retriedToSend;
        public boolean[] retriedToSendArray;
        public boolean scheduled;
        public TLObject sendEncryptedRequest;
        public TLObject sendRequest;
        public int topMessageId;
        public int type;
        public VideoEditedInfo videoEditedInfo;
        public ArrayList<VideoEditedInfo> videoEditedInfos;

        public DelayedMessage(long j) {
            this.peer = j;
        }

        public void addDelayedRequest(TLObject tLObject, ArrayList<MessageObject> arrayList, ArrayList<String> arrayList2, ArrayList<Object> arrayList3, DelayedMessage delayedMessage, boolean z) {
            DelayedMessageSendAfterRequest delayedMessageSendAfterRequest = SendMessagesHelper.this.new DelayedMessageSendAfterRequest();
            delayedMessageSendAfterRequest.request = tLObject;
            delayedMessageSendAfterRequest.msgObjs = arrayList;
            delayedMessageSendAfterRequest.originalPaths = arrayList2;
            delayedMessageSendAfterRequest.delayedMessage = delayedMessage;
            delayedMessageSendAfterRequest.parentObjects = arrayList3;
            delayedMessageSendAfterRequest.scheduled = z;
            if (this.requests == null) {
                this.requests = new ArrayList<>();
            }
            this.requests.add(delayedMessageSendAfterRequest);
        }

        public void addDelayedRequest(TLObject tLObject, MessageObject messageObject, String str, Object obj, DelayedMessage delayedMessage, boolean z) {
            DelayedMessageSendAfterRequest delayedMessageSendAfterRequest = SendMessagesHelper.this.new DelayedMessageSendAfterRequest();
            delayedMessageSendAfterRequest.request = tLObject;
            delayedMessageSendAfterRequest.msgObj = messageObject;
            delayedMessageSendAfterRequest.originalPath = str;
            delayedMessageSendAfterRequest.delayedMessage = delayedMessage;
            delayedMessageSendAfterRequest.parentObject = obj;
            delayedMessageSendAfterRequest.scheduled = z;
            if (this.requests == null) {
                this.requests = new ArrayList<>();
            }
            this.requests.add(delayedMessageSendAfterRequest);
        }

        public boolean getRetriedToSend(int i) {
            boolean[] zArr;
            return (i < 0 || (zArr = this.retriedToSendArray) == null || i >= zArr.length) ? this.retriedToSend : zArr[i];
        }

        public void initForGroup(long j) {
            this.type = 4;
            this.groupId = j;
            this.messageObjects = new ArrayList<>();
            this.messages = new ArrayList<>();
            this.inputMedias = new ArrayList<>();
            this.originalPaths = new ArrayList<>();
            this.parentObjects = new ArrayList<>();
            this.extraHashMap = new HashMap<>();
            this.locations = new ArrayList<>();
            this.httpLocations = new ArrayList<>();
            this.videoEditedInfos = new ArrayList<>();
        }

        public void markAsError() {
            if (this.type == 4) {
                for (int i = 0; i < this.messageObjects.size(); i++) {
                    MessageObject messageObject = this.messageObjects.get(i);
                    SendMessagesHelper.this.getMessagesStorage().markMessageAsSendError(messageObject.messageOwner, messageObject.scheduled ? 1 : 0);
                    messageObject.messageOwner.send_state = 2;
                    SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(messageObject.getId()));
                    SendMessagesHelper.this.processSentMessage(messageObject.getId());
                    SendMessagesHelper.this.removeFromUploadingMessages(messageObject.getId(), this.scheduled);
                }
                SendMessagesHelper.this.delayedMessages.remove("group_" + this.groupId);
            } else {
                MessagesStorage messagesStorage = SendMessagesHelper.this.getMessagesStorage();
                MessageObject messageObject2 = this.obj;
                messagesStorage.markMessageAsSendError(messageObject2.messageOwner, messageObject2.scheduled ? 1 : 0);
                this.obj.messageOwner.send_state = 2;
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(this.obj.getId()));
                SendMessagesHelper.this.processSentMessage(this.obj.getId());
                SendMessagesHelper.this.removeFromUploadingMessages(this.obj.getId(), this.scheduled);
            }
            sendDelayedRequests();
        }

        public void sendDelayedRequests() {
            TLObject tLObject;
            SendMessagesHelper sendMessagesHelper;
            ArrayList<DelayedMessageSendAfterRequest> arrayList = this.requests;
            if (arrayList != null) {
                int i = this.type;
                if (i == 4 || i == 0) {
                    int size = arrayList.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        DelayedMessageSendAfterRequest delayedMessageSendAfterRequest = this.requests.get(i2);
                        TLObject tLObject2 = delayedMessageSendAfterRequest.request;
                        if (tLObject2 instanceof TLRPC.TL_messages_sendEncryptedMultiMedia) {
                            SendMessagesHelper.this.getSecretChatHelper().performSendEncryptedRequest((TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessageSendAfterRequest.request, this);
                        } else {
                            if (tLObject2 instanceof TLRPC.TL_messages_sendMultiMedia) {
                                sendMessagesHelper = SendMessagesHelper.this;
                                tLObject = (TLRPC.TL_messages_sendMultiMedia) tLObject2;
                            } else {
                                if (tLObject2 instanceof TLRPC.TL_messages_sendMedia) {
                                    TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) tLObject2;
                                    if (tL_messages_sendMedia.media instanceof TLRPC.TL_inputMediaPaidMedia) {
                                        sendMessagesHelper = SendMessagesHelper.this;
                                        tLObject = tL_messages_sendMedia;
                                    }
                                }
                                SendMessagesHelper.this.performSendMessageRequest(tLObject2, delayedMessageSendAfterRequest.msgObj, delayedMessageSendAfterRequest.originalPath, delayedMessageSendAfterRequest.delayedMessage, delayedMessageSendAfterRequest.parentObject, null, delayedMessageSendAfterRequest.scheduled);
                            }
                            sendMessagesHelper.performSendMessageRequestMulti(tLObject, delayedMessageSendAfterRequest.msgObjs, delayedMessageSendAfterRequest.originalPaths, delayedMessageSendAfterRequest.parentObjects, delayedMessageSendAfterRequest.delayedMessage, delayedMessageSendAfterRequest.scheduled);
                        }
                    }
                    this.requests = null;
                }
            }
        }

        public void setRetriedToSend(int i, boolean z) {
            if (i < 0) {
                this.retriedToSend = z;
                return;
            }
            if (this.retriedToSendArray == null) {
                this.retriedToSendArray = new boolean[this.messageObjects.size()];
            }
            this.retriedToSendArray[i] = z;
        }
    }

    /* loaded from: classes3.dex */
    protected class DelayedMessageSendAfterRequest {
        public DelayedMessage delayedMessage;
        public MessageObject msgObj;
        public ArrayList<MessageObject> msgObjs;
        public String originalPath;
        public ArrayList<String> originalPaths;
        public Object parentObject;
        public ArrayList<Object> parentObjects;
        public TLObject request;
        public boolean scheduled;

        protected DelayedMessageSendAfterRequest() {
        }
    }

    /* loaded from: classes3.dex */
    public class ImportingHistory {
        public long dialogId;
        public double estimatedUploadSpeed;
        public String historyPath;
        public long importId;
        private long lastUploadSize;
        private long lastUploadTime;
        public TLRPC.InputPeer peer;
        public long totalSize;
        public int uploadProgress;
        public long uploadedSize;
        public ArrayList<Uri> mediaPaths = new ArrayList<>();
        public HashSet<String> uploadSet = new HashSet<>();
        public HashMap<String, Float> uploadProgresses = new HashMap<>();
        public HashMap<String, Long> uploadSize = new HashMap<>();
        public ArrayList<String> uploadMedia = new ArrayList<>();
        public int timeUntilFinish = ConnectionsManager.DEFAULT_DATACENTER_ID;

        class 1 implements RequestDelegate {
            final /* synthetic */ TLRPC.TL_messages_initHistoryImport val$req;

            1(TLRPC.TL_messages_initHistoryImport tL_messages_initHistoryImport) {
                this.val$req = tL_messages_initHistoryImport;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0(TLObject tLObject, TLRPC.TL_messages_initHistoryImport tL_messages_initHistoryImport, TLRPC.TL_error tL_error) {
                if (!(tLObject instanceof TLRPC.TL_messages_historyImport)) {
                    SendMessagesHelper.this.importingHistoryMap.remove(ImportingHistory.this.dialogId);
                    SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId), tL_messages_initHistoryImport, tL_error);
                    return;
                }
                ImportingHistory importingHistory = ImportingHistory.this;
                importingHistory.importId = ((TLRPC.TL_messages_historyImport) tLObject).id;
                importingHistory.uploadSet.remove(importingHistory.historyPath);
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId));
                if (ImportingHistory.this.uploadSet.isEmpty()) {
                    ImportingHistory.this.startImport();
                }
                ImportingHistory.this.lastUploadTime = SystemClock.elapsedRealtime();
                int size = ImportingHistory.this.uploadMedia.size();
                for (int i = 0; i < size; i++) {
                    SendMessagesHelper.this.getFileLoader().uploadFile(ImportingHistory.this.uploadMedia.get(i), false, true, ConnectionsManager.FileTypeFile);
                }
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(final TLObject tLObject, final TLRPC.TL_error tL_error) {
                final TLRPC.TL_messages_initHistoryImport tL_messages_initHistoryImport = this.val$req;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingHistory$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.ImportingHistory.1.this.lambda$run$0(tLObject, tL_messages_initHistoryImport, tL_error);
                    }
                });
            }
        }

        class 2 implements RequestDelegate {
            final /* synthetic */ String val$path;

            2(String str) {
                this.val$path = str;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0(String str) {
                ImportingHistory.this.uploadSet.remove(str);
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId));
                if (ImportingHistory.this.uploadSet.isEmpty()) {
                    ImportingHistory.this.startImport();
                }
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                final String str = this.val$path;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingHistory$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.ImportingHistory.2.this.lambda$run$0(str);
                    }
                });
            }
        }

        class 3 implements RequestDelegate {
            final /* synthetic */ TLRPC.TL_messages_startHistoryImport val$req;

            3(TLRPC.TL_messages_startHistoryImport tL_messages_startHistoryImport) {
                this.val$req = tL_messages_startHistoryImport;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0(TLRPC.TL_error tL_error, TLRPC.TL_messages_startHistoryImport tL_messages_startHistoryImport) {
                SendMessagesHelper.this.importingHistoryMap.remove(ImportingHistory.this.dialogId);
                if (tL_error == null) {
                    SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId));
                } else {
                    SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(ImportingHistory.this.dialogId), tL_messages_startHistoryImport, tL_error);
                }
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject tLObject, final TLRPC.TL_error tL_error) {
                final TLRPC.TL_messages_startHistoryImport tL_messages_startHistoryImport = this.val$req;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingHistory$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.ImportingHistory.3.this.lambda$run$0(tL_error, tL_messages_startHistoryImport);
                    }
                });
            }
        }

        public ImportingHistory() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addUploadProgress(String str, long j, float f) {
            this.uploadProgresses.put(str, Float.valueOf(f));
            this.uploadSize.put(str, Long.valueOf(j));
            this.uploadedSize = 0L;
            Iterator<Map.Entry<String, Long>> it = this.uploadSize.entrySet().iterator();
            while (it.hasNext()) {
                this.uploadedSize += it.next().getValue().longValue();
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (!str.equals(this.historyPath)) {
                long j2 = this.uploadedSize;
                long j3 = this.lastUploadSize;
                if (j2 != j3) {
                    long j4 = this.lastUploadTime;
                    if (elapsedRealtime != j4) {
                        double d = elapsedRealtime - j4;
                        Double.isNaN(d);
                        double d2 = j2 - j3;
                        Double.isNaN(d2);
                        double d3 = d2 / (d / 1000.0d);
                        double d4 = this.estimatedUploadSpeed;
                        if (d4 != 0.0d) {
                            d3 = (d3 * 0.01d) + (d4 * 0.99d);
                        }
                        this.estimatedUploadSpeed = d3;
                        double d5 = (this.totalSize - j2) * 1000;
                        double d6 = this.estimatedUploadSpeed;
                        Double.isNaN(d5);
                        this.timeUntilFinish = (int) (d5 / d6);
                        this.lastUploadSize = j2;
                        this.lastUploadTime = elapsedRealtime;
                    }
                }
            }
            int uploadedCount = (int) ((getUploadedCount() / getTotalCount()) * 100.0f);
            if (this.uploadProgress != uploadedCount) {
                this.uploadProgress = uploadedCount;
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(this.dialogId));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initImport(TLRPC.InputFile inputFile) {
            TLRPC.TL_messages_initHistoryImport tL_messages_initHistoryImport = new TLRPC.TL_messages_initHistoryImport();
            tL_messages_initHistoryImport.file = inputFile;
            tL_messages_initHistoryImport.media_count = this.mediaPaths.size();
            tL_messages_initHistoryImport.peer = this.peer;
            SendMessagesHelper.this.getConnectionsManager().sendRequest(tL_messages_initHistoryImport, new 1(tL_messages_initHistoryImport), 2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onFileFailedToUpload(String str) {
            if (!str.equals(this.historyPath)) {
                this.uploadSet.remove(str);
                return;
            }
            SendMessagesHelper.this.importingHistoryMap.remove(this.dialogId);
            TLRPC.TL_error tL_error = new TLRPC.TL_error();
            tL_error.code = 400;
            tL_error.text = "IMPORT_UPLOAD_FAILED";
            SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(this.dialogId), new TLRPC.TL_messages_initHistoryImport(), tL_error);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onMediaImport(String str, long j, TLRPC.InputFile inputFile) {
            addUploadProgress(str, j, 1.0f);
            TLRPC.TL_messages_uploadImportedMedia tL_messages_uploadImportedMedia = new TLRPC.TL_messages_uploadImportedMedia();
            tL_messages_uploadImportedMedia.peer = this.peer;
            tL_messages_uploadImportedMedia.import_id = this.importId;
            tL_messages_uploadImportedMedia.file_name = new File(str).getName();
            MimeTypeMap singleton = MimeTypeMap.getSingleton();
            int lastIndexOf = tL_messages_uploadImportedMedia.file_name.lastIndexOf(46);
            String lowerCase = lastIndexOf != -1 ? tL_messages_uploadImportedMedia.file_name.substring(lastIndexOf + 1).toLowerCase() : "txt";
            String mimeTypeFromExtension = singleton.getMimeTypeFromExtension(lowerCase);
            if (mimeTypeFromExtension == null) {
                mimeTypeFromExtension = "opus".equals(lowerCase) ? "audio/opus" : "webp".equals(lowerCase) ? "image/webp" : "text/plain";
            }
            if (mimeTypeFromExtension.equals("image/jpg") || mimeTypeFromExtension.equals("image/jpeg")) {
                TLRPC.TL_inputMediaUploadedPhoto tL_inputMediaUploadedPhoto = new TLRPC.TL_inputMediaUploadedPhoto();
                tL_inputMediaUploadedPhoto.file = inputFile;
                tL_messages_uploadImportedMedia.media = tL_inputMediaUploadedPhoto;
            } else {
                TLRPC.TL_inputMediaUploadedDocument tL_inputMediaUploadedDocument = new TLRPC.TL_inputMediaUploadedDocument();
                tL_inputMediaUploadedDocument.file = inputFile;
                tL_inputMediaUploadedDocument.mime_type = mimeTypeFromExtension;
                tL_messages_uploadImportedMedia.media = tL_inputMediaUploadedDocument;
            }
            SendMessagesHelper.this.getConnectionsManager().sendRequest(tL_messages_uploadImportedMedia, new 2(str), 2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startImport() {
            TLRPC.TL_messages_startHistoryImport tL_messages_startHistoryImport = new TLRPC.TL_messages_startHistoryImport();
            tL_messages_startHistoryImport.peer = this.peer;
            tL_messages_startHistoryImport.import_id = this.importId;
            SendMessagesHelper.this.getConnectionsManager().sendRequest(tL_messages_startHistoryImport, new 3(tL_messages_startHistoryImport));
        }

        public long getTotalCount() {
            return this.totalSize;
        }

        public long getUploadedCount() {
            return this.uploadedSize;
        }

        public void setImportProgress(int i) {
            if (i == 100) {
                SendMessagesHelper.this.importingHistoryMap.remove(this.dialogId);
            }
            SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(this.dialogId));
        }
    }

    /* loaded from: classes3.dex */
    public static class ImportingSticker {
        public boolean animated;
        public String emoji;
        public TLRPC.TL_inputStickerSetItem item;
        public String mimeType;
        public String path;
        public boolean validated;
        public VideoEditedInfo videoEditedInfo;

        class 1 implements RequestDelegate {
            final /* synthetic */ Runnable val$onFinish;

            1(Runnable runnable) {
                this.val$onFinish = runnable;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0(TLObject tLObject, Runnable runnable) {
                if (tLObject instanceof TLRPC.TL_messageMediaDocument) {
                    ImportingSticker.this.item = new TLRPC.TL_inputStickerSetItem();
                    ImportingSticker.this.item.document = new TLRPC.TL_inputDocument();
                    ImportingSticker importingSticker = ImportingSticker.this;
                    TLRPC.TL_inputStickerSetItem tL_inputStickerSetItem = importingSticker.item;
                    TLRPC.InputDocument inputDocument = tL_inputStickerSetItem.document;
                    TLRPC.Document document = ((TLRPC.TL_messageMediaDocument) tLObject).document;
                    inputDocument.id = document.id;
                    inputDocument.access_hash = document.access_hash;
                    inputDocument.file_reference = document.file_reference;
                    String str = importingSticker.emoji;
                    if (str == null) {
                        str = "";
                    }
                    tL_inputStickerSetItem.emoji = str;
                    importingSticker.mimeType = document.mime_type;
                } else {
                    ImportingSticker importingSticker2 = ImportingSticker.this;
                    if (importingSticker2.animated) {
                        importingSticker2.mimeType = "application/x-bad-tgsticker";
                    }
                }
                runnable.run();
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(final TLObject tLObject, TLRPC.TL_error tL_error) {
                final Runnable runnable = this.val$onFinish;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingSticker$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.ImportingSticker.1.this.lambda$run$0(tLObject, runnable);
                    }
                });
            }
        }

        public void uploadMedia(int i, TLRPC.InputFile inputFile, Runnable runnable) {
            TLRPC.TL_messages_uploadMedia tL_messages_uploadMedia = new TLRPC.TL_messages_uploadMedia();
            tL_messages_uploadMedia.peer = new TLRPC.TL_inputPeerSelf();
            TLRPC.TL_inputMediaUploadedDocument tL_inputMediaUploadedDocument = new TLRPC.TL_inputMediaUploadedDocument();
            tL_messages_uploadMedia.media = tL_inputMediaUploadedDocument;
            tL_inputMediaUploadedDocument.file = inputFile;
            tL_inputMediaUploadedDocument.mime_type = this.mimeType;
            ConnectionsManager.getInstance(i).sendRequest(tL_messages_uploadMedia, new 1(runnable), 2);
        }
    }

    /* loaded from: classes3.dex */
    public class ImportingStickers {
        public double estimatedUploadSpeed;
        private long lastUploadSize;
        private long lastUploadTime;
        public String shortName;
        public String software;
        public String title;
        public long totalSize;
        public int uploadProgress;
        public long uploadedSize;
        public HashMap<String, ImportingSticker> uploadSet = new HashMap<>();
        public HashMap<String, Float> uploadProgresses = new HashMap<>();
        public HashMap<String, Long> uploadSize = new HashMap<>();
        public ArrayList<ImportingSticker> uploadMedia = new ArrayList<>();
        public int timeUntilFinish = ConnectionsManager.DEFAULT_DATACENTER_ID;

        class 1 implements RequestDelegate {
            final /* synthetic */ TLRPC.TL_stickers_createStickerSet val$req;

            1(TLRPC.TL_stickers_createStickerSet tL_stickers_createStickerSet) {
                this.val$req = tL_stickers_createStickerSet;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0(TLRPC.TL_error tL_error, TLRPC.TL_stickers_createStickerSet tL_stickers_createStickerSet, TLObject tLObject) {
                SendMessagesHelper.this.importingStickersMap.remove(ImportingStickers.this.shortName);
                if (tL_error == null) {
                    SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersImportProgressChanged, ImportingStickers.this.shortName);
                } else {
                    SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersImportProgressChanged, ImportingStickers.this.shortName, tL_stickers_createStickerSet, tL_error);
                }
                if (tLObject instanceof TLRPC.TL_messages_stickerSet) {
                    NotificationCenter notificationCenter = SendMessagesHelper.this.getNotificationCenter();
                    int i = NotificationCenter.stickersImportComplete;
                    if (notificationCenter.hasObservers(i)) {
                        SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(i, tLObject);
                    } else {
                        SendMessagesHelper.this.getMediaDataController().toggleStickerSet(null, tLObject, 2, null, false, false);
                    }
                }
            }

            @Override // org.telegram.tgnet.RequestDelegate
            public void run(final TLObject tLObject, final TLRPC.TL_error tL_error) {
                final TLRPC.TL_stickers_createStickerSet tL_stickers_createStickerSet = this.val$req;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingStickers$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.ImportingStickers.1.this.lambda$run$0(tL_error, tL_stickers_createStickerSet, tLObject);
                    }
                });
            }
        }

        public ImportingStickers() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addUploadProgress(String str, long j, float f) {
            this.uploadProgresses.put(str, Float.valueOf(f));
            this.uploadSize.put(str, Long.valueOf(j));
            this.uploadedSize = 0L;
            Iterator<Map.Entry<String, Long>> it = this.uploadSize.entrySet().iterator();
            while (it.hasNext()) {
                this.uploadedSize += it.next().getValue().longValue();
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j2 = this.uploadedSize;
            long j3 = this.lastUploadSize;
            if (j2 != j3) {
                long j4 = this.lastUploadTime;
                if (elapsedRealtime != j4) {
                    double d = elapsedRealtime - j4;
                    Double.isNaN(d);
                    double d2 = j2 - j3;
                    Double.isNaN(d2);
                    double d3 = d2 / (d / 1000.0d);
                    double d4 = this.estimatedUploadSpeed;
                    if (d4 != 0.0d) {
                        d3 = (d3 * 0.01d) + (d4 * 0.99d);
                    }
                    this.estimatedUploadSpeed = d3;
                    double d5 = (this.totalSize - j2) * 1000;
                    double d6 = this.estimatedUploadSpeed;
                    Double.isNaN(d5);
                    this.timeUntilFinish = (int) (d5 / d6);
                    this.lastUploadSize = j2;
                    this.lastUploadTime = elapsedRealtime;
                }
            }
            int uploadedCount = (int) ((getUploadedCount() / getTotalCount()) * 100.0f);
            if (this.uploadProgress != uploadedCount) {
                this.uploadProgress = uploadedCount;
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersImportProgressChanged, this.shortName);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initImport() {
            SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersImportProgressChanged, this.shortName);
            this.lastUploadTime = SystemClock.elapsedRealtime();
            int size = this.uploadMedia.size();
            for (int i = 0; i < size; i++) {
                SendMessagesHelper.this.getFileLoader().uploadFile(this.uploadMedia.get(i).path, false, true, ConnectionsManager.FileTypeFile);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMediaImport$0(String str) {
            this.uploadSet.remove(str);
            SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersImportProgressChanged, this.shortName);
            if (this.uploadSet.isEmpty()) {
                startImport();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onFileFailedToUpload(String str) {
            ImportingSticker remove = this.uploadSet.remove(str);
            if (remove != null) {
                this.uploadMedia.remove(remove);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onMediaImport(final String str, long j, TLRPC.InputFile inputFile) {
            addUploadProgress(str, j, 1.0f);
            ImportingSticker importingSticker = this.uploadSet.get(str);
            if (importingSticker == null) {
                return;
            }
            importingSticker.uploadMedia(SendMessagesHelper.this.currentAccount, inputFile, new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$ImportingStickers$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.ImportingStickers.this.lambda$onMediaImport$0(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startImport() {
            TLRPC.TL_stickers_createStickerSet tL_stickers_createStickerSet = new TLRPC.TL_stickers_createStickerSet();
            tL_stickers_createStickerSet.user_id = new TLRPC.TL_inputUserSelf();
            tL_stickers_createStickerSet.title = this.title;
            tL_stickers_createStickerSet.short_name = this.shortName;
            String str = this.software;
            if (str != null) {
                tL_stickers_createStickerSet.software = str;
                tL_stickers_createStickerSet.flags |= 8;
            }
            int size = this.uploadMedia.size();
            for (int i = 0; i < size; i++) {
                TLRPC.TL_inputStickerSetItem tL_inputStickerSetItem = this.uploadMedia.get(i).item;
                if (tL_inputStickerSetItem != null) {
                    tL_stickers_createStickerSet.stickers.add(tL_inputStickerSetItem);
                }
            }
            SendMessagesHelper.this.getConnectionsManager().sendRequest(tL_stickers_createStickerSet, new 1(tL_stickers_createStickerSet));
        }

        public long getTotalCount() {
            return this.totalSize;
        }

        public long getUploadedCount() {
            return this.uploadedSize;
        }

        public void setImportProgress(int i) {
            if (i == 100) {
                SendMessagesHelper.this.importingStickersMap.remove(this.shortName);
            }
            SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersImportProgressChanged, this.shortName);
        }
    }

    /* loaded from: classes3.dex */
    public static class LocationProvider {
        private LocationProviderDelegate delegate;
        private GpsLocationListener gpsLocationListener;
        private Location lastKnownLocation;
        private LocationManager locationManager;
        private Runnable locationQueryCancelRunnable;
        private GpsLocationListener networkLocationListener;

        private class GpsLocationListener implements LocationListener {
            private GpsLocationListener() {
            }

            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                if (location == null || LocationProvider.this.locationQueryCancelRunnable == null) {
                    return;
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("found location " + location);
                }
                LocationProvider.this.lastKnownLocation = location;
                if (location.getAccuracy() < 100.0f) {
                    if (LocationProvider.this.delegate != null) {
                        LocationProvider.this.delegate.onLocationAcquired(location);
                    }
                    if (LocationProvider.this.locationQueryCancelRunnable != null) {
                        AndroidUtilities.cancelRunOnUIThread(LocationProvider.this.locationQueryCancelRunnable);
                    }
                    LocationProvider.this.cleanup();
                }
            }

            @Override // android.location.LocationListener
            public void onProviderDisabled(String str) {
            }

            @Override // android.location.LocationListener
            public void onProviderEnabled(String str) {
            }

            @Override // android.location.LocationListener
            public void onStatusChanged(String str, int i, Bundle bundle) {
            }
        }

        public interface LocationProviderDelegate {
            void onLocationAcquired(Location location);

            void onUnableLocationAcquire();
        }

        public LocationProvider() {
            this.gpsLocationListener = new GpsLocationListener();
            this.networkLocationListener = new GpsLocationListener();
        }

        public LocationProvider(LocationProviderDelegate locationProviderDelegate) {
            this.gpsLocationListener = new GpsLocationListener();
            this.networkLocationListener = new GpsLocationListener();
            this.delegate = locationProviderDelegate;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cleanup() {
            this.locationManager.removeUpdates(this.gpsLocationListener);
            this.locationManager.removeUpdates(this.networkLocationListener);
            this.lastKnownLocation = null;
            this.locationQueryCancelRunnable = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$start$0() {
            LocationProviderDelegate locationProviderDelegate = this.delegate;
            if (locationProviderDelegate != null) {
                Location location = this.lastKnownLocation;
                if (location != null) {
                    locationProviderDelegate.onLocationAcquired(location);
                } else {
                    locationProviderDelegate.onUnableLocationAcquire();
                }
            }
            cleanup();
        }

        public void setDelegate(LocationProviderDelegate locationProviderDelegate) {
            this.delegate = locationProviderDelegate;
        }

        public void start() {
            if (this.locationManager == null) {
                this.locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
            }
            try {
                this.locationManager.requestLocationUpdates("gps", 1L, 0.0f, this.gpsLocationListener);
            } catch (Exception e) {
                FileLog.e(e);
            }
            try {
                this.locationManager.requestLocationUpdates("network", 1L, 0.0f, this.networkLocationListener);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            try {
                Location lastKnownLocation = this.locationManager.getLastKnownLocation("gps");
                this.lastKnownLocation = lastKnownLocation;
                if (lastKnownLocation == null) {
                    this.lastKnownLocation = this.locationManager.getLastKnownLocation("network");
                }
            } catch (Exception e3) {
                FileLog.e(e3);
            }
            Runnable runnable = this.locationQueryCancelRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
            Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$LocationProvider$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.LocationProvider.this.lambda$start$0();
                }
            };
            this.locationQueryCancelRunnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2, 5000L);
        }

        public void stop() {
            if (this.locationManager == null) {
                return;
            }
            Runnable runnable = this.locationQueryCancelRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
            cleanup();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    static class MediaSendPrepareWorker {
        public volatile String parentObject;
        public volatile TLRPC.TL_photo photo;
        public CountDownLatch sync;

        private MediaSendPrepareWorker() {
        }
    }

    /* loaded from: classes3.dex */
    public static class SendMessageParams {
        public String caption;
        public TLRPC.TL_document document;
        public long effect_id;
        public ArrayList<TLRPC.MessageEntity> entities;
        public TLRPC.TL_game game;
        public boolean hasMediaSpoilers;
        public boolean invert_media;
        public TLRPC.TL_messageMediaInvoice invoice;
        public TLRPC.MessageMedia location;
        public TLRPC.TL_messageMediaWebPage mediaWebPage;
        public String message;
        public boolean notify;
        public HashMap<String, String> params;
        public Object parentObject;
        public String path;
        public long peer;
        public TLRPC.TL_photo photo;
        public TLRPC.TL_messageMediaPoll poll;
        public String quick_reply_shortcut;
        public int quick_reply_shortcut_id;
        public TLRPC.ReplyMarkup replyMarkup;
        public ChatActivity.ReplyQuote replyQuote;
        public MessageObject replyToMsg;
        public TL_stories.StoryItem replyToStoryItem;
        public MessageObject replyToTopMsg;
        public MessageObject retryMessageObject;
        public int scheduleDate;
        public boolean searchLinks = true;
        public MessageObject.SendAnimationData sendAnimationData;
        public TL_stories.StoryItem sendingStory;
        public long stars;
        public int ttl;
        public boolean updateStickersOrder;
        public TLRPC.User user;
        public VideoEditedInfo videoEditedInfo;
        public TLRPC.WebPage webPage;

        public static SendMessageParams of(String str, long j) {
            return of(str, null, null, null, null, null, null, null, null, null, j, null, null, null, null, true, null, null, null, null, false, 0, 0, null, null, false);
        }

        public static SendMessageParams of(String str, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.WebPage webPage, boolean z, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> hashMap, boolean z2, int i, MessageObject.SendAnimationData sendAnimationData, boolean z3) {
            return of(str, null, null, null, null, null, null, null, null, null, j, null, messageObject, messageObject2, webPage, z, null, arrayList, replyMarkup, hashMap, z2, i, 0, null, sendAnimationData, z3);
        }

        private static SendMessageParams of(String str, String str2, TLRPC.MessageMedia messageMedia, TLRPC.TL_photo tL_photo, VideoEditedInfo videoEditedInfo, TLRPC.User user, TLRPC.TL_document tL_document, TLRPC.TL_game tL_game, TLRPC.TL_messageMediaPoll tL_messageMediaPoll, TLRPC.TL_messageMediaInvoice tL_messageMediaInvoice, long j, String str3, MessageObject messageObject, MessageObject messageObject2, TLRPC.WebPage webPage, boolean z, MessageObject messageObject3, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> hashMap, boolean z2, int i, int i2, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z3) {
            return of(str, str2, messageMedia, tL_photo, videoEditedInfo, user, tL_document, tL_game, tL_messageMediaPoll, tL_messageMediaInvoice, j, str3, messageObject, messageObject2, webPage, z, messageObject3, arrayList, replyMarkup, hashMap, z2, i, i2, obj, sendAnimationData, z3, false);
        }

        public static SendMessageParams of(String str, String str2, TLRPC.MessageMedia messageMedia, TLRPC.TL_photo tL_photo, VideoEditedInfo videoEditedInfo, TLRPC.User user, TLRPC.TL_document tL_document, TLRPC.TL_game tL_game, TLRPC.TL_messageMediaPoll tL_messageMediaPoll, TLRPC.TL_messageMediaInvoice tL_messageMediaInvoice, long j, String str3, MessageObject messageObject, MessageObject messageObject2, TLRPC.WebPage webPage, boolean z, MessageObject messageObject3, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> hashMap, boolean z2, int i, int i2, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z3, boolean z4) {
            SendMessageParams sendMessageParams = new SendMessageParams();
            sendMessageParams.message = str;
            sendMessageParams.caption = str2;
            sendMessageParams.location = messageMedia;
            sendMessageParams.photo = tL_photo;
            sendMessageParams.videoEditedInfo = videoEditedInfo;
            sendMessageParams.user = user;
            sendMessageParams.document = tL_document;
            sendMessageParams.game = tL_game;
            sendMessageParams.poll = tL_messageMediaPoll;
            sendMessageParams.invoice = tL_messageMediaInvoice;
            sendMessageParams.peer = j;
            sendMessageParams.path = str3;
            sendMessageParams.replyToMsg = messageObject;
            sendMessageParams.replyToTopMsg = messageObject2;
            sendMessageParams.webPage = webPage;
            sendMessageParams.searchLinks = z;
            sendMessageParams.retryMessageObject = messageObject3;
            sendMessageParams.entities = arrayList;
            sendMessageParams.replyMarkup = replyMarkup;
            sendMessageParams.params = hashMap;
            sendMessageParams.notify = z2;
            sendMessageParams.scheduleDate = i;
            sendMessageParams.ttl = i2;
            sendMessageParams.parentObject = obj;
            sendMessageParams.sendAnimationData = sendAnimationData;
            sendMessageParams.updateStickersOrder = z3;
            sendMessageParams.hasMediaSpoilers = z4;
            return sendMessageParams;
        }

        public static SendMessageParams of(MessageObject messageObject) {
            long dialogId = messageObject.getDialogId();
            TLRPC.Message message = messageObject.messageOwner;
            SendMessageParams of = of(null, null, null, null, null, null, null, null, null, null, dialogId, message.attachPath, null, null, null, true, messageObject, null, message.reply_markup, message.params, !message.silent, messageObject.scheduled ? message.date : 0, 0, null, null, false);
            TLRPC.Message message2 = messageObject.messageOwner;
            if (message2 != null) {
                TLRPC.InputQuickReplyShortcut inputQuickReplyShortcut = message2.quick_reply_shortcut;
                if (inputQuickReplyShortcut instanceof TLRPC.TL_inputQuickReplyShortcut) {
                    of.quick_reply_shortcut = ((TLRPC.TL_inputQuickReplyShortcut) inputQuickReplyShortcut).shortcut;
                }
                of.quick_reply_shortcut_id = messageObject.getQuickReplyId();
            }
            return of;
        }

        public static SendMessageParams of(TLRPC.MessageMedia messageMedia, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> hashMap, boolean z, int i) {
            return of(null, null, messageMedia, null, null, null, null, null, null, null, j, null, messageObject, messageObject2, null, true, null, null, replyMarkup, hashMap, z, i, 0, null, null, false);
        }

        public static SendMessageParams of(TLRPC.TL_document tL_document, VideoEditedInfo videoEditedInfo, String str, long j, MessageObject messageObject, MessageObject messageObject2, String str2, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> hashMap, boolean z, int i, int i2, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z2) {
            return of(null, str2, null, null, videoEditedInfo, null, tL_document, null, null, null, j, str, messageObject, messageObject2, null, true, null, arrayList, replyMarkup, hashMap, z, i, i2, obj, sendAnimationData, z2);
        }

        public static SendMessageParams of(TLRPC.TL_document tL_document, VideoEditedInfo videoEditedInfo, String str, long j, MessageObject messageObject, MessageObject messageObject2, String str2, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> hashMap, boolean z, int i, int i2, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z2, boolean z3) {
            return of(null, str2, null, null, videoEditedInfo, null, tL_document, null, null, null, j, str, messageObject, messageObject2, null, true, null, arrayList, replyMarkup, hashMap, z, i, i2, obj, sendAnimationData, z2, z3);
        }

        public static SendMessageParams of(TLRPC.TL_game tL_game, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> hashMap, boolean z, int i) {
            return of(null, null, null, null, null, null, null, tL_game, null, null, j, null, messageObject, messageObject2, null, true, null, null, replyMarkup, hashMap, z, i, 0, null, null, false);
        }

        public static SendMessageParams of(TLRPC.TL_messageMediaInvoice tL_messageMediaInvoice, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> hashMap, boolean z, int i) {
            return of(null, null, null, null, null, null, null, null, null, tL_messageMediaInvoice, j, null, messageObject, messageObject2, null, true, null, null, replyMarkup, hashMap, z, i, 0, null, null, false);
        }

        public static SendMessageParams of(TLRPC.TL_messageMediaPoll tL_messageMediaPoll, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> hashMap, boolean z, int i) {
            return of(null, null, null, null, null, null, null, null, tL_messageMediaPoll, null, j, null, messageObject, messageObject2, null, true, null, null, replyMarkup, hashMap, z, i, 0, null, null, false);
        }

        public static SendMessageParams of(TLRPC.TL_photo tL_photo, String str, long j, MessageObject messageObject, MessageObject messageObject2, String str2, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> hashMap, boolean z, int i, int i2, Object obj, boolean z2) {
            return of(null, str2, null, tL_photo, null, null, null, null, null, null, j, str, messageObject, messageObject2, null, true, null, arrayList, replyMarkup, hashMap, z, i, i2, obj, null, z2);
        }

        public static SendMessageParams of(TLRPC.TL_photo tL_photo, String str, long j, MessageObject messageObject, MessageObject messageObject2, String str2, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> hashMap, boolean z, int i, int i2, Object obj, boolean z2, boolean z3) {
            return of(null, str2, null, tL_photo, null, null, null, null, null, null, j, str, messageObject, messageObject2, null, true, null, arrayList, replyMarkup, hashMap, z, i, i2, obj, null, z2, z3);
        }

        public static SendMessageParams of(TLRPC.User user, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> hashMap, boolean z, int i) {
            return of(null, null, null, null, null, user, null, null, null, null, j, null, messageObject, messageObject2, null, true, null, null, replyMarkup, hashMap, z, i, 0, null, null, false);
        }
    }

    /* loaded from: classes3.dex */
    public static class SendingMediaInfo {
        public boolean canDeleteAfter;
        public String caption;
        public TLRPC.VideoSize emojiMarkup;
        public ArrayList<TLRPC.MessageEntity> entities;
        public boolean forceImage;
        public boolean hasMediaSpoilers;
        public TLRPC.BotInlineResult inlineResult;
        public boolean isVideo;
        public ArrayList<TLRPC.InputDocument> masks;
        public String paintPath;
        public HashMap<String, String> params;
        public String path;
        public MediaController.SearchImage searchImage;
        public long stars;
        public String thumbPath;
        public int ttl;
        public boolean updateStickersOrder;
        public Uri uri;
        public VideoEditedInfo videoEditedInfo;
    }

    static {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        mediaSendThreadPool = new ThreadPoolExecutor(availableProcessors, availableProcessors, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        Instance = new SendMessagesHelper[4];
    }

    public SendMessagesHelper(int i) {
        super(i);
        this.delayedMessages = new HashMap<>();
        this.unsentMessages = new SparseArray<>();
        this.sendingMessages = new SparseArray<>();
        this.editingMessages = new SparseArray<>();
        this.uploadMessages = new SparseArray<>();
        this.sendingMessagesIdDialogs = new LongSparseArray();
        this.uploadingMessagesIdDialogs = new LongSparseArray();
        this.waitingForLocation = new HashMap<>();
        this.waitingForCallback = new HashMap<>();
        this.waitingForCallbackMap = new HashMap<>();
        this.waitingForVote = new HashMap<>();
        this.voteSendTime = new LongSparseArray();
        this.importingHistoryFiles = new HashMap<>();
        this.importingHistoryMap = new LongSparseArray();
        this.importingStickersFiles = new HashMap<>();
        this.importingStickersMap = new HashMap<>();
        this.locationProvider = new LocationProvider(new LocationProvider.LocationProviderDelegate() { // from class: org.telegram.messenger.SendMessagesHelper.1
            @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider.LocationProviderDelegate
            public void onLocationAcquired(Location location) {
                SendMessagesHelper.this.sendLocation(location);
                SendMessagesHelper.this.waitingForLocation.clear();
            }

            @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider.LocationProviderDelegate
            public void onUnableLocationAcquire() {
                SendMessagesHelper.this.getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.wasUnableToFindCurrentLocation, new HashMap(SendMessagesHelper.this.waitingForLocation));
                SendMessagesHelper.this.waitingForLocation.clear();
            }
        });
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$new$0();
            }
        });
    }

    public static int canSendMessageToChat(TLRPC.Chat chat, MessageObject messageObject) {
        boolean canSendStickers = ChatObject.canSendStickers(chat);
        boolean canSendPhoto = ChatObject.canSendPhoto(chat);
        boolean canSendVideo = ChatObject.canSendVideo(chat);
        boolean canSendDocument = ChatObject.canSendDocument(chat);
        ChatObject.canSendEmbed(chat);
        boolean canSendPolls = ChatObject.canSendPolls(chat);
        boolean canSendRoundVideo = ChatObject.canSendRoundVideo(chat);
        boolean canSendVoice = ChatObject.canSendVoice(chat);
        boolean canSendMusic = ChatObject.canSendMusic(chat);
        boolean z = messageObject.isSticker() || messageObject.isAnimatedSticker() || messageObject.isGif() || messageObject.isGame();
        if (!canSendStickers && z) {
            return ChatObject.isActionBannedByDefault(chat, 8) ? 4 : 1;
        }
        if (!canSendPhoto && (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) && !messageObject.isVideo() && !z) {
            return ChatObject.isActionBannedByDefault(chat, 16) ? 10 : 12;
        }
        if (!canSendMusic && messageObject.isMusic()) {
            return ChatObject.isActionBannedByDefault(chat, 18) ? 19 : 20;
        }
        if (!canSendVideo && messageObject.isVideo() && !z) {
            return ChatObject.isActionBannedByDefault(chat, 17) ? 9 : 11;
        }
        if (!canSendPolls && (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPoll)) {
            return ChatObject.isActionBannedByDefault(chat, 10) ? 6 : 3;
        }
        if (!canSendVoice && MessageObject.isVoiceMessage(messageObject.messageOwner)) {
            return ChatObject.isActionBannedByDefault(chat, 20) ? 13 : 14;
        }
        if (!canSendRoundVideo && MessageObject.isRoundVideoMessage(messageObject.messageOwner)) {
            return ChatObject.isActionBannedByDefault(chat, 21) ? 15 : 16;
        }
        if (canSendDocument || !(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) || z) {
            return 0;
        }
        return ChatObject.isActionBannedByDefault(chat, 19) ? 17 : 18;
    }

    private static boolean checkFileSize(AccountInstance accountInstance, Uri uri) {
        long j = 0;
        try {
            AssetFileDescriptor openAssetFileDescriptor = ApplicationLoader.applicationContext.getContentResolver().openAssetFileDescriptor(uri, "r", null);
            if (openAssetFileDescriptor != null) {
                openAssetFileDescriptor.getLength();
            }
            Cursor query = ApplicationLoader.applicationContext.getContentResolver().query(uri, new String[]{"_size"}, null, null, null);
            int columnIndex = query.getColumnIndex("_size");
            query.moveToFirst();
            j = query.getLong(columnIndex);
            query.close();
        } catch (Exception e) {
            FileLog.e(e);
        }
        return !FileLoader.checkUploadFileSize(accountInstance.getCurrentAccount(), j);
    }

    public static boolean checkUpdateStickersOrder(CharSequence charSequence) {
        if (charSequence instanceof Spannable) {
            for (AnimatedEmojiSpan animatedEmojiSpan : (AnimatedEmojiSpan[]) ((Spannable) charSequence).getSpans(0, charSequence.length(), AnimatedEmojiSpan.class)) {
                if (animatedEmojiSpan.fromEmojiKeyboard) {
                    return true;
                }
            }
        }
        return false;
    }

    private static VideoEditedInfo createCompressionSettings(String str) {
        int[] iArr = new int[11];
        AnimatedFileDrawable.getVideoInfo(str, iArr);
        if (iArr[0] == 0) {
            if (!BuildVars.LOGS_ENABLED) {
                return null;
            }
            FileLog.d("video hasn't avc1 atom");
            return null;
        }
        long length = new File(str).length();
        int videoBitrate = MediaController.getVideoBitrate(str);
        if (videoBitrate == -1) {
            videoBitrate = iArr[3];
        }
        float f = iArr[4];
        long j = iArr[5];
        int i = iArr[7];
        VideoEditedInfo videoEditedInfo = new VideoEditedInfo();
        videoEditedInfo.startTime = -1L;
        videoEditedInfo.endTime = -1L;
        videoEditedInfo.bitrate = videoBitrate;
        videoEditedInfo.originalPath = str;
        videoEditedInfo.framerate = i;
        videoEditedInfo.estimatedDuration = (long) Math.ceil(f);
        boolean z = true;
        int i2 = iArr[1];
        videoEditedInfo.originalWidth = i2;
        videoEditedInfo.resultWidth = i2;
        int i3 = iArr[2];
        videoEditedInfo.originalHeight = i3;
        videoEditedInfo.resultHeight = i3;
        videoEditedInfo.rotationValue = iArr[8];
        videoEditedInfo.originalDuration = (long) (f * 1000.0f);
        float max = Math.max(i2, i3);
        float f2 = 640.0f;
        int i4 = max > 1280.0f ? 4 : max > 854.0f ? 3 : max > 640.0f ? 2 : 1;
        int round = Math.round(DownloadController.getInstance(UserConfig.selectedAccount).getMaxVideoBitrate() / (100.0f / i4));
        if (round > i4) {
            round = i4;
        }
        if (new File(str).length() < 1048576000) {
            if (round != i4 || Math.max(videoEditedInfo.originalWidth, videoEditedInfo.originalHeight) > 1280) {
                if (round == 1) {
                    f2 = 432.0f;
                } else if (round != 2) {
                    f2 = round != 3 ? 1280.0f : 848.0f;
                }
                int i5 = videoEditedInfo.originalWidth;
                int i6 = videoEditedInfo.originalHeight;
                float f3 = f2 / (i5 > i6 ? i5 : i6);
                videoEditedInfo.resultWidth = Math.round((i5 * f3) / 2.0f) * 2;
                videoEditedInfo.resultHeight = Math.round((videoEditedInfo.originalHeight * f3) / 2.0f) * 2;
            } else {
                z = false;
            }
            videoBitrate = MediaController.makeVideoBitrate(videoEditedInfo.originalHeight, videoEditedInfo.originalWidth, videoBitrate, videoEditedInfo.resultHeight, videoEditedInfo.resultWidth);
        } else {
            z = false;
        }
        if (z) {
            videoEditedInfo.bitrate = videoBitrate;
            videoEditedInfo.estimatedSize = (long) (j + (((f / 1000.0f) * MediaController.extractRealEncoderBitrate(videoEditedInfo.resultWidth, videoEditedInfo.resultHeight, videoBitrate, false)) / 8.0f));
        } else {
            videoEditedInfo.resultWidth = videoEditedInfo.originalWidth;
            videoEditedInfo.resultHeight = videoEditedInfo.originalHeight;
            videoEditedInfo.bitrate = videoBitrate;
            videoEditedInfo.estimatedSize = length;
        }
        if (videoEditedInfo.estimatedSize == 0) {
            videoEditedInfo.estimatedSize = 1L;
        }
        return videoEditedInfo;
    }

    public static Bitmap createVideoThumbnail(String str, int i) {
        float f = i == 2 ? 1920.0f : i == 3 ? 96.0f : 512.0f;
        Bitmap createVideoThumbnailAtTime = createVideoThumbnailAtTime(str, 0L);
        if (createVideoThumbnailAtTime == null) {
            return createVideoThumbnailAtTime;
        }
        int width = createVideoThumbnailAtTime.getWidth();
        int height = createVideoThumbnailAtTime.getHeight();
        float f2 = width;
        if (f2 <= f && height <= f) {
            return createVideoThumbnailAtTime;
        }
        float max = Math.max(width, height) / f;
        return Bitmap.createScaledBitmap(createVideoThumbnailAtTime, (int) (f2 / max), (int) (height / max), true);
    }

    public static Bitmap createVideoThumbnailAtTime(String str, long j) {
        return createVideoThumbnailAtTime(str, j, null, false);
    }

    public static Bitmap createVideoThumbnailAtTime(String str, long j, int[] iArr, boolean z) {
        Bitmap bitmap;
        Bitmap bitmap2;
        if (z) {
            AnimatedFileDrawable animatedFileDrawable = new AnimatedFileDrawable(new File(str), true, 0L, 0, null, null, null, 0L, 0, true, null);
            bitmap2 = animatedFileDrawable.getFrameAtTime(j, z);
            if (iArr != null) {
                iArr[0] = animatedFileDrawable.getOrientation();
            }
            animatedFileDrawable.recycle();
            if (bitmap2 == null) {
                return createVideoThumbnailAtTime(str, j, iArr, false);
            }
        } else {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            try {
                try {
                    mediaMetadataRetriever.setDataSource(str);
                    bitmap = mediaMetadataRetriever.getFrameAtTime(j, 1);
                    if (bitmap == null) {
                        try {
                            bitmap = mediaMetadataRetriever.getFrameAtTime(j, 3);
                        } catch (Exception unused) {
                        }
                    }
                } catch (Exception unused2) {
                    bitmap = null;
                }
                bitmap2 = bitmap;
            } finally {
                try {
                    mediaMetadataRetriever.release();
                } catch (Throwable unused3) {
                }
            }
        }
        return bitmap2;
    }

    public static void ensureMediaThumbExists(AccountInstance accountInstance, boolean z, TLObject tLObject, String str, Uri uri, long j) {
        TLRPC.PhotoSize scaleAndSaveImage;
        TLRPC.PhotoSize scaleAndSaveImage2;
        if (!(tLObject instanceof TLRPC.TL_photo)) {
            if (tLObject instanceof TLRPC.TL_document) {
                TLRPC.TL_document tL_document = (TLRPC.TL_document) tLObject;
                if ((MessageObject.isVideoDocument(tL_document) || MessageObject.isNewGifDocument(tL_document)) && MessageObject.isDocumentHasThumb(tL_document)) {
                    TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_document.thumbs, 320);
                    if ((closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize) || (closestPhotoSizeWithSize instanceof TLRPC.TL_photoPathSize) || FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(closestPhotoSizeWithSize, true).exists()) {
                        return;
                    }
                    Bitmap createVideoThumbnailAtTime = createVideoThumbnailAtTime(str, j);
                    Bitmap createVideoThumbnail = createVideoThumbnailAtTime == null ? createVideoThumbnail(str, 1) : createVideoThumbnailAtTime;
                    int i = z ? 90 : 320;
                    float f = i;
                    tL_document.thumbs.set(0, ImageLoader.scaleAndSaveImage(closestPhotoSizeWithSize, createVideoThumbnail, f, f, i > 90 ? 80 : 55, false, true));
                    return;
                }
                return;
            }
            return;
        }
        TLRPC.TL_photo tL_photo = (TLRPC.TL_photo) tLObject;
        TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tL_photo.sizes, 90);
        boolean exists = ((closestPhotoSizeWithSize2 instanceof TLRPC.TL_photoStrippedSize) || (closestPhotoSizeWithSize2 instanceof TLRPC.TL_photoPathSize)) ? true : FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(closestPhotoSizeWithSize2, true).exists();
        TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(tL_photo.sizes, AndroidUtilities.getPhotoSize());
        boolean exists2 = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(closestPhotoSizeWithSize3, false).exists();
        if (exists && exists2) {
            return;
        }
        Bitmap loadBitmap = ImageLoader.loadBitmap(str, uri, AndroidUtilities.getPhotoSize(), AndroidUtilities.getPhotoSize(), true);
        if (loadBitmap == null) {
            loadBitmap = ImageLoader.loadBitmap(str, uri, 800.0f, 800.0f, true);
        }
        Bitmap bitmap = loadBitmap;
        if (!exists2 && (scaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(closestPhotoSizeWithSize3, bitmap, Bitmap.CompressFormat.JPEG, true, AndroidUtilities.getPhotoSize(), AndroidUtilities.getPhotoSize(), 80, false, 101, 101, false)) != closestPhotoSizeWithSize3) {
            tL_photo.sizes.add(0, scaleAndSaveImage2);
        }
        if (!exists && (scaleAndSaveImage = ImageLoader.scaleAndSaveImage(closestPhotoSizeWithSize2, bitmap, 90.0f, 90.0f, 55, true, false)) != closestPhotoSizeWithSize2) {
            tL_photo.sizes.add(0, scaleAndSaveImage);
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x005e, code lost:
    
        if (r2 == 270(0x10e, float:3.78E-43)) goto L28;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:63:0x006d -> B:29:0x00ae). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void fillVideoAttribute(String str, TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo, VideoEditedInfo videoEditedInfo) {
        MediaMetadataRetriever mediaMetadataRetriever;
        MediaMetadataRetriever mediaMetadataRetriever2 = null;
        MediaMetadataRetriever mediaMetadataRetriever3 = null;
        mediaMetadataRetriever2 = null;
        try {
            try {
                try {
                    mediaMetadataRetriever = new MediaMetadataRetriever();
                } catch (Throwable th) {
                    th = th;
                    mediaMetadataRetriever = mediaMetadataRetriever2;
                }
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            FileLog.e(e2);
            mediaMetadataRetriever2 = mediaMetadataRetriever2;
        }
        try {
            mediaMetadataRetriever.setDataSource(str);
            String extractMetadata = mediaMetadataRetriever.extractMetadata(18);
            if (extractMetadata != null) {
                tL_documentAttributeVideo.w = Integer.parseInt(extractMetadata);
            }
            String extractMetadata2 = mediaMetadataRetriever.extractMetadata(19);
            if (extractMetadata2 != null) {
                tL_documentAttributeVideo.h = Integer.parseInt(extractMetadata2);
            }
            String extractMetadata3 = mediaMetadataRetriever.extractMetadata(9);
            if (extractMetadata3 != null) {
                double parseLong = Long.parseLong(extractMetadata3);
                Double.isNaN(parseLong);
                tL_documentAttributeVideo.duration = parseLong / 1000.0d;
            }
            String extractMetadata4 = mediaMetadataRetriever.extractMetadata(24);
            MediaMetadataRetriever mediaMetadataRetriever4 = extractMetadata4;
            if (extractMetadata4 != null) {
                int intValue = Utilities.parseInt((CharSequence) extractMetadata4).intValue();
                if (videoEditedInfo != null) {
                    videoEditedInfo.rotationValue = intValue;
                    mediaMetadataRetriever4 = intValue;
                } else {
                    if (intValue != 90) {
                        mediaMetadataRetriever4 = intValue;
                    }
                    int i = tL_documentAttributeVideo.w;
                    int i2 = tL_documentAttributeVideo.h;
                    tL_documentAttributeVideo.w = i2;
                    tL_documentAttributeVideo.h = i;
                    mediaMetadataRetriever4 = i2;
                }
            }
            mediaMetadataRetriever.release();
            mediaMetadataRetriever2 = mediaMetadataRetriever4;
        } catch (Exception e3) {
            e = e3;
            mediaMetadataRetriever3 = mediaMetadataRetriever;
            FileLog.e(e);
            if (mediaMetadataRetriever3 != null) {
                try {
                    mediaMetadataRetriever3.release();
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
            }
            Context context = ApplicationLoader.applicationContext;
            File file = new File(str);
            MediaPlayer create = MediaPlayer.create(context, Uri.fromFile(file));
            mediaMetadataRetriever2 = file;
            if (create != null) {
                double duration = create.getDuration();
                Double.isNaN(duration);
                double d = duration / 1000.0d;
                tL_documentAttributeVideo.duration = d;
                tL_documentAttributeVideo.w = create.getVideoWidth();
                tL_documentAttributeVideo.h = create.getVideoHeight();
                create.release();
                mediaMetadataRetriever2 = d;
            }
        } catch (Throwable th2) {
            th = th2;
            if (mediaMetadataRetriever != null) {
                try {
                    mediaMetadataRetriever.release();
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
            }
            throw th;
        }
    }

    private DelayedMessage findMaxDelayedMessageForMessageId(int i, long j) {
        int i2;
        Iterator<Map.Entry<String, ArrayList<DelayedMessage>>> it = this.delayedMessages.entrySet().iterator();
        DelayedMessage delayedMessage = null;
        int i3 = Integer.MIN_VALUE;
        while (it.hasNext()) {
            ArrayList<DelayedMessage> value = it.next().getValue();
            int size = value.size();
            for (int i4 = 0; i4 < size; i4++) {
                DelayedMessage delayedMessage2 = value.get(i4);
                int i5 = delayedMessage2.type;
                if ((i5 == 4 || i5 == 0) && delayedMessage2.peer == j) {
                    MessageObject messageObject = delayedMessage2.obj;
                    if (messageObject == null) {
                        ArrayList<MessageObject> arrayList = delayedMessage2.messageObjects;
                        if (arrayList == null || arrayList.isEmpty()) {
                            i2 = 0;
                            if (i2 != 0 && i2 > i && delayedMessage == null && i3 < i2) {
                                delayedMessage = delayedMessage2;
                                i3 = i2;
                            }
                        } else {
                            messageObject = delayedMessage2.messageObjects.get(r8.size() - 1);
                        }
                    }
                    i2 = messageObject.getId();
                    if (i2 != 0) {
                        delayedMessage = delayedMessage2;
                        i3 = i2;
                    }
                }
            }
        }
        return delayedMessage;
    }

    private static void finishGroup(final AccountInstance accountInstance, final long j, final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$finishGroup$79(AccountInstance.this, j, i);
            }
        });
    }

    public static SendMessagesHelper getInstance(int i) {
        SendMessagesHelper sendMessagesHelper = Instance[i];
        if (sendMessagesHelper == null) {
            synchronized (SendMessagesHelper.class) {
                try {
                    sendMessagesHelper = Instance[i];
                    if (sendMessagesHelper == null) {
                        SendMessagesHelper[] sendMessagesHelperArr = Instance;
                        SendMessagesHelper sendMessagesHelper2 = new SendMessagesHelper(i);
                        sendMessagesHelperArr[i] = sendMessagesHelper2;
                        sendMessagesHelper = sendMessagesHelper2;
                    }
                } finally {
                }
            }
        }
        return sendMessagesHelper;
    }

    public static String getKeyForPhotoSize(AccountInstance accountInstance, TLRPC.PhotoSize photoSize, Bitmap[] bitmapArr, boolean z, boolean z2) {
        if (photoSize == null || photoSize.location == null) {
            return null;
        }
        Point messageSize = ChatMessageCell.getMessageSize(photoSize.w, photoSize.h);
        if (bitmapArr != null) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                File pathToAttach = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(photoSize, z2);
                FileInputStream fileInputStream = new FileInputStream(pathToAttach);
                BitmapFactory.decodeStream(fileInputStream, null, options);
                fileInputStream.close();
                float max = Math.max(options.outWidth / messageSize.x, options.outHeight / messageSize.y);
                if (max < 1.0f) {
                    max = 1.0f;
                }
                options.inJustDecodeBounds = false;
                options.inSampleSize = (int) max;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                if (Build.VERSION.SDK_INT >= 21) {
                    FileInputStream fileInputStream2 = new FileInputStream(pathToAttach);
                    bitmapArr[0] = BitmapFactory.decodeStream(fileInputStream2, null, options);
                    fileInputStream2.close();
                }
            } catch (Throwable unused) {
            }
        }
        return String.format(Locale.US, z ? "%d_%d@%d_%d_b" : "%d_%d@%d_%d", Long.valueOf(photoSize.location.volume_id), Integer.valueOf(photoSize.location.local_id), Integer.valueOf((int) (messageSize.x / AndroidUtilities.density)), Integer.valueOf((int) (messageSize.y / AndroidUtilities.density)));
    }

    private TLRPC.PhotoSize getThumbForSecretChat(ArrayList<TLRPC.PhotoSize> arrayList) {
        if (arrayList != null && !arrayList.isEmpty()) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                TLRPC.PhotoSize photoSize = arrayList.get(i);
                if (photoSize != null && !(photoSize instanceof TLRPC.TL_photoPathSize) && !(photoSize instanceof TLRPC.TL_photoSizeEmpty) && photoSize.location != null) {
                    if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
                        return photoSize;
                    }
                    TLRPC.TL_photoSize_layer127 tL_photoSize_layer127 = new TLRPC.TL_photoSize_layer127();
                    tL_photoSize_layer127.type = photoSize.type;
                    tL_photoSize_layer127.w = photoSize.w;
                    tL_photoSize_layer127.h = photoSize.h;
                    tL_photoSize_layer127.size = photoSize.size;
                    byte[] bArr = photoSize.bytes;
                    tL_photoSize_layer127.bytes = bArr;
                    if (bArr == null) {
                        tL_photoSize_layer127.bytes = new byte[0];
                    }
                    TLRPC.TL_fileLocation_layer82 tL_fileLocation_layer82 = new TLRPC.TL_fileLocation_layer82();
                    tL_photoSize_layer127.location = tL_fileLocation_layer82;
                    TLRPC.FileLocation fileLocation = photoSize.location;
                    tL_fileLocation_layer82.dc_id = fileLocation.dc_id;
                    tL_fileLocation_layer82.volume_id = fileLocation.volume_id;
                    tL_fileLocation_layer82.local_id = fileLocation.local_id;
                    tL_fileLocation_layer82.secret = fileLocation.secret;
                    return tL_photoSize_layer127;
                }
            }
        }
        return null;
    }

    private static String getTrimmedString(String str) {
        String trim = str.trim();
        if (trim.length() == 0) {
            return trim;
        }
        while (str.startsWith("\n")) {
            str = str.substring(1);
        }
        while (str.endsWith("\n")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private static void handleError(final int i, final AccountInstance accountInstance) {
        if (i != 0) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.lambda$handleError$81(i, accountInstance);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$1(TLRPC.TL_photo tL_photo, MessageObject messageObject, File file, DelayedMessage delayedMessage, String str) {
        if (tL_photo == null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("can't load image " + str + " to file " + file.toString());
            }
            delayedMessage.markAsError();
            return;
        }
        TLRPC.Message message = messageObject.messageOwner;
        message.media.photo = tL_photo;
        message.attachPath = file.toString();
        ArrayList<TLRPC.Message> arrayList = new ArrayList<>();
        arrayList.add(messageObject.messageOwner);
        getMessagesStorage().putMessages(arrayList, false, true, false, 0, messageObject.scheduled ? 1 : 0, 0L);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateMessageMedia, messageObject.messageOwner);
        ArrayList<TLRPC.PhotoSize> arrayList2 = tL_photo.sizes;
        delayedMessage.photoSize = arrayList2.get(arrayList2.size() - 1);
        delayedMessage.locationParent = tL_photo;
        delayedMessage.httpLocation = null;
        if (delayedMessage.type != 4) {
            performSendDelayedMessage(delayedMessage);
        } else {
            delayedMessage.performMediaUpload = true;
            performSendDelayedMessage(delayedMessage, delayedMessage.messageObjects.indexOf(messageObject));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$2(final File file, final MessageObject messageObject, final DelayedMessage delayedMessage, final String str) {
        final TLRPC.TL_photo generatePhotoSizes = generatePhotoSizes(file.toString(), null);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda53
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$didReceivedNotification$1(generatePhotoSizes, messageObject, file, delayedMessage, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$3(DelayedMessage delayedMessage, File file, TLRPC.Document document, MessageObject messageObject) {
        delayedMessage.httpLocation = null;
        delayedMessage.obj.messageOwner.attachPath = file.toString();
        if (!document.thumbs.isEmpty()) {
            TLRPC.PhotoSize photoSize = document.thumbs.get(0);
            if (!(photoSize instanceof TLRPC.TL_photoStrippedSize)) {
                delayedMessage.photoSize = photoSize;
                delayedMessage.locationParent = document;
            }
        }
        ArrayList<TLRPC.Message> arrayList = new ArrayList<>();
        arrayList.add(messageObject.messageOwner);
        getMessagesStorage().putMessages(arrayList, false, true, false, 0, messageObject.scheduled ? 1 : 0, 0L);
        delayedMessage.performMediaUpload = true;
        performSendDelayedMessage(delayedMessage);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateMessageMedia, delayedMessage.obj.messageOwner);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$4(final DelayedMessage delayedMessage, final File file, final MessageObject messageObject) {
        final TLRPC.Document document = delayedMessage.obj.getDocument();
        if (document.thumbs.isEmpty() || (document.thumbs.get(0).location instanceof TLRPC.TL_fileLocationUnavailable)) {
            try {
                Bitmap loadBitmap = ImageLoader.loadBitmap(file.getAbsolutePath(), null, 90.0f, 90.0f, true);
                if (loadBitmap != null) {
                    document.thumbs.clear();
                    document.thumbs.add(ImageLoader.scaleAndSaveImage(loadBitmap, 90.0f, 90.0f, 55, delayedMessage.sendEncryptedRequest != null));
                    loadBitmap.recycle();
                }
            } catch (Exception e) {
                document.thumbs.clear();
                FileLog.e(e);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda70
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$didReceivedNotification$3(delayedMessage, file, document, messageObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editMessage$15(TLRPC.TL_error tL_error, BaseFragment baseFragment, TLRPC.TL_messages_editMessage tL_messages_editMessage) {
        AlertsCreator.processError(this.currentAccount, tL_error, baseFragment, tL_messages_editMessage, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editMessage$16(final BaseFragment baseFragment, final TLRPC.TL_messages_editMessage tL_messages_editMessage, TLObject tLObject, final TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda77
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.lambda$editMessage$15(tL_error, baseFragment, tL_messages_editMessage);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$finishGroup$79(AccountInstance accountInstance, long j, int i) {
        SendMessagesHelper sendMessagesHelper = accountInstance.getSendMessagesHelper();
        ArrayList<DelayedMessage> arrayList = sendMessagesHelper.delayedMessages.get("group_" + j);
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        DelayedMessage delayedMessage = arrayList.get(0);
        ArrayList<MessageObject> arrayList2 = delayedMessage.messageObjects;
        MessageObject messageObject = arrayList2.get(arrayList2.size() - 1);
        delayedMessage.finalGroupMessage = messageObject.getId();
        messageObject.messageOwner.params.put("final", "1");
        TLRPC.TL_messages_messages tL_messages_messages = new TLRPC.TL_messages_messages();
        tL_messages_messages.messages.add(messageObject.messageOwner);
        if (!delayedMessage.paidMedia) {
            accountInstance.getMessagesStorage().putMessages((TLRPC.messages_Messages) tL_messages_messages, delayedMessage.peer, -2, 0, false, i != 0 ? 1 : 0, 0L);
        }
        sendMessagesHelper.sendReadyToSendGroup(delayedMessage, true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleError$81(int i, AccountInstance accountInstance) {
        try {
            if (i == 1) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 1, LocaleController.getString(R.string.UnsupportedAttachment));
            } else if (i != 2) {
            } else {
                NotificationCenter.getInstance(accountInstance.getCurrentAccount()).lambda$postNotificationNameOnUIThread$1(NotificationCenter.currentUserShowLimitReachedDialog, 6);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        getNotificationCenter().addObserver(this, NotificationCenter.fileUploaded);
        getNotificationCenter().addObserver(this, NotificationCenter.fileUploadProgressChanged);
        getNotificationCenter().addObserver(this, NotificationCenter.fileUploadFailed);
        getNotificationCenter().addObserver(this, NotificationCenter.filePreparingStarted);
        getNotificationCenter().addObserver(this, NotificationCenter.fileNewChunkAvailable);
        getNotificationCenter().addObserver(this, NotificationCenter.filePreparingFailed);
        getNotificationCenter().addObserver(this, NotificationCenter.httpFileDidFailedLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.httpFileDidLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.fileLoaded);
        getNotificationCenter().addObserver(this, NotificationCenter.fileLoadFailed);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendDelayedMessage$35(TLObject tLObject, DelayedMessage delayedMessage, String str) {
        boolean z;
        if (tLObject != null) {
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject;
            getMediaDataController().storeTempStickerSet(tL_messages_stickerSet);
            TLRPC.TL_documentAttributeSticker_layer55 tL_documentAttributeSticker_layer55 = (TLRPC.TL_documentAttributeSticker_layer55) delayedMessage.locationParent;
            TLRPC.TL_inputStickerSetShortName tL_inputStickerSetShortName = new TLRPC.TL_inputStickerSetShortName();
            tL_documentAttributeSticker_layer55.stickerset = tL_inputStickerSetShortName;
            tL_inputStickerSetShortName.short_name = tL_messages_stickerSet.set.short_name;
            z = true;
        } else {
            z = false;
        }
        ArrayList<DelayedMessage> remove = this.delayedMessages.remove(str);
        if (remove == null || remove.isEmpty()) {
            return;
        }
        if (z) {
            getMessagesStorage().replaceMessageIfExists(remove.get(0).obj.messageOwner, null, null, false);
        }
        SecretChatHelper secretChatHelper = getSecretChatHelper();
        TLRPC.DecryptedMessage decryptedMessage = (TLRPC.DecryptedMessage) delayedMessage.sendEncryptedRequest;
        MessageObject messageObject = delayedMessage.obj;
        secretChatHelper.performSendEncryptedRequest(decryptedMessage, messageObject.messageOwner, delayedMessage.encryptedChat, null, null, messageObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendDelayedMessage$36(final DelayedMessage delayedMessage, final String str, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$performSendDelayedMessage$35(tLObject, delayedMessage, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$49(TLRPC.Message message, boolean z, TLObject tLObject, DelayedMessage delayedMessage) {
        removeFromSendingMessages(message.id, z);
        if (tLObject instanceof TLRPC.TL_messages_sendMedia) {
            TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) tLObject;
            TLRPC.InputMedia inputMedia = tL_messages_sendMedia.media;
            if ((inputMedia instanceof TLRPC.TL_inputMediaPhoto) || (inputMedia instanceof TLRPC.TL_inputMediaDocument)) {
                tL_messages_sendMedia.media = delayedMessage.inputUploadMedia;
            }
        } else if (tLObject instanceof TLRPC.TL_messages_editMessage) {
            TLRPC.TL_messages_editMessage tL_messages_editMessage = (TLRPC.TL_messages_editMessage) tLObject;
            TLRPC.InputMedia inputMedia2 = tL_messages_editMessage.media;
            if ((inputMedia2 instanceof TLRPC.TL_inputMediaPhoto) || (inputMedia2 instanceof TLRPC.TL_inputMediaDocument)) {
                tL_messages_editMessage.media = delayedMessage.inputUploadMedia;
            }
        }
        delayedMessage.performMediaUpload = true;
        performSendDelayedMessage(delayedMessage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$50(TLRPC.Message message, boolean z) {
        processSentMessage(message.id);
        removeFromSendingMessages(message.id, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$51(TLRPC.Updates updates, final TLRPC.Message message, final boolean z) {
        getMessagesController().processUpdates(updates, false);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda76
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$performSendMessageRequest$50(message, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$52(TLRPC.TL_error tL_error, final TLRPC.Message message, TLObject tLObject, MessageObject messageObject, String str, final boolean z, TLObject tLObject2) {
        int i = 0;
        TLRPC.Message message2 = null;
        if (tL_error != null) {
            AlertsCreator.processError(this.currentAccount, tL_error, null, tLObject2, new Object[0]);
            removeFromSendingMessages(message.id, z);
            revertEditingMessageObject(messageObject);
            return;
        }
        String str2 = message.attachPath;
        final TLRPC.Updates updates = (TLRPC.Updates) tLObject;
        ArrayList<TLRPC.Update> arrayList = updates.updates;
        while (true) {
            if (i >= arrayList.size()) {
                break;
            }
            TLRPC.Update update = arrayList.get(i);
            if (update instanceof TLRPC.TL_updateEditMessage) {
                message2 = ((TLRPC.TL_updateEditMessage) update).message;
                break;
            }
            if (update instanceof TLRPC.TL_updateEditChannelMessage) {
                message2 = ((TLRPC.TL_updateEditChannelMessage) update).message;
                break;
            }
            if (update instanceof TLRPC.TL_updateNewScheduledMessage) {
                message2 = ((TLRPC.TL_updateNewScheduledMessage) update).message;
                break;
            } else {
                if (update instanceof TLRPC.TL_updateQuickReplyMessage) {
                    QuickRepliesController.getInstance(this.currentAccount).processUpdate(update, MessageObject.getQuickReplyName(message), MessageObject.getQuickReplyId(message));
                    message2 = ((TLRPC.TL_updateQuickReplyMessage) update).message;
                    break;
                }
                i++;
            }
        }
        TLRPC.Message message3 = message2;
        if (message3 != null) {
            ImageLoader.saveMessageThumbs(message3);
            updateMediaPaths(messageObject, message3, message3.id, str, false);
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda95
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$performSendMessageRequest$51(updates, message, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$53(TLRPC.TL_updateShortSentMessage tL_updateShortSentMessage) {
        getMessagesController().processNewDifferenceParams(-1, tL_updateShortSentMessage.pts, tL_updateShortSentMessage.date, tL_updateShortSentMessage.pts_count);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$54(TLRPC.TL_updateNewMessage tL_updateNewMessage) {
        getMessagesController().processNewDifferenceParams(-1, tL_updateNewMessage.pts, -1, tL_updateNewMessage.pts_count);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$55(TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage) {
        getMessagesController().processNewChannelDifferenceParams(tL_updateNewChannelMessage.pts, tL_updateNewChannelMessage.pts_count, tL_updateNewChannelMessage.message.peer_id.channel_id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$56(TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage, long j) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(Integer.valueOf(tL_updateNewChannelMessage.message.id));
        getMessagesStorage().updatePinnedMessages(-j, arrayList, true, -1, 0, false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$57(TLRPC.Updates updates) {
        getMessagesController().processUpdates(updates, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$58(boolean z, TLRPC.Message message, ArrayList arrayList, boolean z2, ArrayList arrayList2, int i) {
        getMessagesController().deleteMessages(arrayList, null, null, message.dialog_id, false, z2 ? 1 : 0, false, 0L, null, 0, !z2 && z, (!z || message == null) ? 0 : message.id);
        getMessagesController().updateInterfaceWithMessages(message.dialog_id, arrayList2, z ? 1 : 0);
        getMediaDataController().increasePeerRaiting(message.dialog_id);
        processSentMessage(i);
        removeFromSendingMessages(i, z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$59(ArrayList arrayList, final boolean z, final boolean z2, final TLRPC.Message message, final ArrayList arrayList2, final ArrayList arrayList3, final int i) {
        getMessagesStorage().putMessages(arrayList, true, false, false, 0, false, !z ? 1 : 0, 0L);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$performSendMessageRequest$58(z2, message, arrayList2, z, arrayList3, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$60(TLRPC.Message message, int i, int i2, boolean z) {
        getMediaDataController().increasePeerRaiting(message.dialog_id);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer, Integer.valueOf(i), Integer.valueOf(message.id), message, Long.valueOf(message.dialog_id), 0L, Integer.valueOf(i2), Boolean.valueOf(z));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer2, Integer.valueOf(i), Integer.valueOf(message.id), message, Long.valueOf(message.dialog_id), 0L, Integer.valueOf(i2), Boolean.valueOf(z));
        processSentMessage(i);
        removeFromSendingMessages(i, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$61(final boolean z, final TLRPC.Message message, final int i, ArrayList arrayList, final int i2) {
        int i3 = (message.quick_reply_shortcut_id == 0 && message.quick_reply_shortcut == null) ? z ? 1 : 0 : 5;
        getMessagesStorage().updateMessageStateAndId(message.random_id, MessageObject.getPeerId(message.peer_id), Integer.valueOf(i), message.id, 0, false, z ? 1 : 0, message.quick_reply_shortcut_id);
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) arrayList, true, false, false, 0, i3, message.quick_reply_shortcut_id);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$performSendMessageRequest$60(message, i, i2, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r13v41 */
    public /* synthetic */ void lambda$performSendMessageRequest$62(final boolean z, TLRPC.TL_error tL_error, final TLRPC.Message message, TLObject tLObject, MessageObject messageObject, String str, TLObject tLObject2) {
        boolean z2;
        ArrayList arrayList;
        int i;
        final boolean z3;
        int i2;
        boolean z4;
        int i3;
        boolean z5;
        boolean z6;
        TLRPC.Message message2;
        TLRPC.MessageReplyHeader messageReplyHeader;
        SparseArray sparseArray;
        LongSparseArray longSparseArray;
        if (tL_error == null) {
            int i4 = message.id;
            ArrayList arrayList2 = new ArrayList();
            boolean z7 = message.date == 2147483646;
            if (tLObject instanceof TLRPC.TL_updateShortSentMessage) {
                final TLRPC.TL_updateShortSentMessage tL_updateShortSentMessage = (TLRPC.TL_updateShortSentMessage) tLObject;
                arrayList = arrayList2;
                i = i4;
                z4 = false;
                updateMediaPaths(messageObject, null, tL_updateShortSentMessage.id, null, false);
                int mediaExistanceFlags = messageObject.getMediaExistanceFlags();
                int i5 = tL_updateShortSentMessage.id;
                message.id = i5;
                message.local_id = i5;
                message.date = tL_updateShortSentMessage.date;
                message.entities = tL_updateShortSentMessage.entities;
                message.out = tL_updateShortSentMessage.out;
                if ((tL_updateShortSentMessage.flags & ConnectionsManager.FileTypeVideo) != 0) {
                    message.ttl_period = tL_updateShortSentMessage.ttl_period;
                    message.flags |= ConnectionsManager.FileTypeVideo;
                }
                TLRPC.MessageMedia messageMedia = tL_updateShortSentMessage.media;
                if (messageMedia != null) {
                    message.media = messageMedia;
                    message.flags |= 512;
                    ImageLoader.saveMessageThumbs(message);
                }
                TLRPC.MessageMedia messageMedia2 = tL_updateShortSentMessage.media;
                if (((messageMedia2 instanceof TLRPC.TL_messageMediaGame) || (messageMedia2 instanceof TLRPC.TL_messageMediaInvoice)) && !TextUtils.isEmpty(tL_updateShortSentMessage.message)) {
                    message.message = tL_updateShortSentMessage.message;
                }
                if (!message.entities.isEmpty()) {
                    message.flags |= 128;
                }
                Integer num = getMessagesController().dialogs_read_outbox_max.get(Long.valueOf(message.dialog_id));
                if (num == null) {
                    num = Integer.valueOf(getMessagesStorage().getDialogReadMax(message.out, message.dialog_id));
                    getMessagesController().dialogs_read_outbox_max.put(Long.valueOf(message.dialog_id), num);
                }
                message.unread = num.intValue() < message.id;
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda28
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.this.lambda$performSendMessageRequest$53(tL_updateShortSentMessage);
                    }
                });
                arrayList.add(message);
                i2 = mediaExistanceFlags;
                z3 = false;
            } else {
                arrayList = arrayList2;
                i = i4;
                ?? r13 = 1;
                if (tLObject instanceof TLRPC.Updates) {
                    final TLRPC.Updates updates = (TLRPC.Updates) tLObject;
                    ArrayList<TLRPC.Update> arrayList3 = updates.updates;
                    boolean z8 = z ? 1 : 0;
                    int i6 = 0;
                    LongSparseArray longSparseArray2 = null;
                    TLRPC.Message message3 = null;
                    while (i6 < arrayList3.size()) {
                        TLRPC.Update update = arrayList3.get(i6);
                        if (update instanceof TLRPC.TL_updateNewMessage) {
                            final TLRPC.TL_updateNewMessage tL_updateNewMessage = (TLRPC.TL_updateNewMessage) update;
                            message2 = tL_updateNewMessage.message;
                            if (message2.action == null) {
                                arrayList.add(message2);
                                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda29
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.this.lambda$performSendMessageRequest$54(tL_updateNewMessage);
                                    }
                                });
                                arrayList3.remove(i6);
                                i6--;
                                message3 = message2;
                                z6 = true;
                                i6 += z6 ? 1 : 0;
                                r13 = z6;
                            }
                        }
                        if (update instanceof TLRPC.TL_updateNewChannelMessage) {
                            final TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage = (TLRPC.TL_updateNewChannelMessage) update;
                            final long updateChannelId = MessagesController.getUpdateChannelId(tL_updateNewChannelMessage);
                            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(updateChannelId));
                            if ((chat == null || chat.megagroup) && (messageReplyHeader = tL_updateNewChannelMessage.message.reply_to) != null && (messageReplyHeader.reply_to_top_id != 0 || messageReplyHeader.reply_to_msg_id != 0)) {
                                if (longSparseArray2 == null) {
                                    longSparseArray2 = new LongSparseArray();
                                }
                                long dialogId = MessageObject.getDialogId(tL_updateNewChannelMessage.message);
                                SparseArray sparseArray2 = (SparseArray) longSparseArray2.get(dialogId);
                                if (sparseArray2 == null) {
                                    sparseArray = new SparseArray();
                                    longSparseArray2.put(dialogId, sparseArray);
                                } else {
                                    sparseArray = sparseArray2;
                                }
                                TLRPC.MessageReplyHeader messageReplyHeader2 = tL_updateNewChannelMessage.message.reply_to;
                                int i7 = messageReplyHeader2.reply_to_top_id;
                                if (i7 == 0) {
                                    i7 = messageReplyHeader2.reply_to_msg_id;
                                }
                                TLRPC.MessageReplies messageReplies = (TLRPC.MessageReplies) sparseArray.get(i7);
                                if (messageReplies == null) {
                                    messageReplies = new TLRPC.TL_messageReplies();
                                    sparseArray.put(i7, messageReplies);
                                }
                                TLRPC.Peer peer = tL_updateNewChannelMessage.message.from_id;
                                if (peer != null) {
                                    longSparseArray = longSparseArray2;
                                    messageReplies.recent_repliers.add(0, peer);
                                } else {
                                    longSparseArray = longSparseArray2;
                                }
                                messageReplies.replies++;
                                longSparseArray2 = longSparseArray;
                            }
                            TLRPC.Message message4 = tL_updateNewChannelMessage.message;
                            arrayList.add(message4);
                            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda30
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.this.lambda$performSendMessageRequest$55(tL_updateNewChannelMessage);
                                }
                            });
                            arrayList3.remove(i6);
                            i6--;
                            if (tL_updateNewChannelMessage.message.pinned) {
                                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda31
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.this.lambda$performSendMessageRequest$56(tL_updateNewChannelMessage, updateChannelId);
                                    }
                                });
                            }
                            message3 = message4;
                            z8 = false;
                        } else if (update instanceof TLRPC.TL_updateNewScheduledMessage) {
                            TLRPC.TL_updateNewScheduledMessage tL_updateNewScheduledMessage = (TLRPC.TL_updateNewScheduledMessage) update;
                            int i8 = 0;
                            while (true) {
                                if (i8 >= arrayList.size()) {
                                    break;
                                }
                                if (((TLRPC.Message) arrayList.get(i8)).id == tL_updateNewScheduledMessage.message.id) {
                                    arrayList.remove(i8);
                                    break;
                                }
                                i8++;
                            }
                            TLRPC.Message message5 = tL_updateNewScheduledMessage.message;
                            arrayList.add(message5);
                            arrayList3.remove(i6);
                            i6--;
                            message3 = message5;
                            z8 = true;
                        } else if (update instanceof TLRPC.TL_updateQuickReplyMessage) {
                            QuickRepliesController.getInstance(this.currentAccount).processUpdate(update, messageObject.getQuickReplyName(), messageObject.getQuickReplyId());
                            message2 = ((TLRPC.TL_updateQuickReplyMessage) update).message;
                            arrayList.add(message2);
                            arrayList3.remove(i6);
                            i6--;
                            message3 = message2;
                        } else if (update instanceof TLRPC.TL_updateDeleteScheduledMessages) {
                            TLRPC.TL_updateDeleteScheduledMessages tL_updateDeleteScheduledMessages = (TLRPC.TL_updateDeleteScheduledMessages) update;
                            if (messageObject.getDialogId() == DialogObject.getPeerDialogId(tL_updateDeleteScheduledMessages.peer)) {
                                Iterator<Integer> it = tL_updateDeleteScheduledMessages.messages.iterator();
                                while (it.hasNext()) {
                                    int intValue = it.next().intValue();
                                    int i9 = 0;
                                    while (true) {
                                        if (i9 >= arrayList.size()) {
                                            break;
                                        }
                                        if (((TLRPC.Message) arrayList.get(i9)).id == intValue) {
                                            arrayList.remove(i9);
                                            break;
                                        }
                                        i9++;
                                    }
                                }
                                z6 = true;
                                arrayList3.remove(i6);
                                i6--;
                                z8 = false;
                                i6 += z6 ? 1 : 0;
                                r13 = z6;
                            }
                        }
                        z6 = true;
                        i6 += z6 ? 1 : 0;
                        r13 = z6;
                    }
                    if (longSparseArray2 != null) {
                        getMessagesStorage().putChannelViews(null, null, longSparseArray2, r13);
                        NotificationCenter notificationCenter = getNotificationCenter();
                        int i10 = NotificationCenter.didUpdateMessagesViews;
                        Object[] objArr = new Object[4];
                        objArr[0] = null;
                        objArr[r13] = null;
                        objArr[2] = longSparseArray2;
                        objArr[3] = Boolean.TRUE;
                        notificationCenter.lambda$postNotificationNameOnUIThread$1(i10, objArr);
                    }
                    TLRPC.Message message6 = message3;
                    if (message6 != null) {
                        MessageObject.getDialogId(message6);
                        boolean z9 = (!z7 || message6.date == 2147483646) ? z8 : false;
                        ImageLoader.saveMessageThumbs(message6);
                        if (!z9) {
                            Integer num2 = getMessagesController().dialogs_read_outbox_max.get(Long.valueOf(message6.dialog_id));
                            if (num2 == null) {
                                num2 = Integer.valueOf(getMessagesStorage().getDialogReadMax(message6.out, message6.dialog_id));
                                getMessagesController().dialogs_read_outbox_max.put(Long.valueOf(message6.dialog_id), num2);
                            }
                            message6.unread = num2.intValue() < message6.id;
                        }
                        TLRPC.Message message7 = messageObject.messageOwner;
                        message7.post_author = message6.post_author;
                        if ((message6.flags & ConnectionsManager.FileTypeVideo) != 0) {
                            message7.ttl_period = message6.ttl_period;
                            message7.flags |= ConnectionsManager.FileTypeVideo;
                        }
                        message7.entities = message6.entities;
                        int i11 = message6.quick_reply_shortcut_id;
                        message7.quick_reply_shortcut_id = i11;
                        if (i11 != 0) {
                            message7.flags |= 1073741824;
                        }
                        updateMediaPaths(messageObject, message6, message6.id, str, false);
                        i3 = messageObject.getMediaExistanceFlags();
                        message.id = message6.id;
                        z8 = z9;
                        z5 = false;
                    } else {
                        if (BuildVars.LOGS_ENABLED) {
                            StringBuilder sb = new StringBuilder();
                            for (int i12 = 0; i12 < arrayList3.size(); i12++) {
                                sb.append(arrayList3.get(i12).getClass().getSimpleName());
                                sb.append(", ");
                            }
                            FileLog.d("can't find message in updates " + ((Object) sb));
                        }
                        i3 = 0;
                        z5 = true;
                    }
                    Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda32
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.this.lambda$performSendMessageRequest$57(updates);
                        }
                    });
                    i2 = i3;
                    z4 = z5;
                    z3 = z8;
                } else {
                    z3 = z ? 1 : 0;
                    i2 = 0;
                    z4 = false;
                }
            }
            if (MessageObject.isLiveLocationMessage(message) && message.via_bot_id == 0 && TextUtils.isEmpty(message.via_bot_name)) {
                getLocationController().addSharingLocation(message);
            }
            if (!z4) {
                getStatsController().incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, 1);
                message.send_state = 0;
                if (z != z3) {
                    final ArrayList arrayList4 = new ArrayList();
                    arrayList4.add(Integer.valueOf(i));
                    final ArrayList arrayList5 = new ArrayList();
                    arrayList5.add(new MessageObject(messageObject.currentAccount, messageObject.messageOwner, true, true));
                    final ArrayList arrayList6 = arrayList;
                    final int i13 = i;
                    getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda33
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.this.lambda$performSendMessageRequest$59(arrayList6, z, z3, message, arrayList4, arrayList5, i13);
                        }
                    });
                } else {
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer, Integer.valueOf(i), Integer.valueOf(message.id), message, Long.valueOf(message.dialog_id), 0L, Integer.valueOf(i2), Boolean.valueOf(z));
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer2, Integer.valueOf(i), Integer.valueOf(message.id), message, Long.valueOf(message.dialog_id), 0L, Integer.valueOf(i2), Boolean.valueOf(z));
                    final int i14 = i;
                    final ArrayList arrayList7 = arrayList;
                    final int i15 = i2;
                    getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda34
                        @Override // java.lang.Runnable
                        public final void run() {
                            SendMessagesHelper.this.lambda$performSendMessageRequest$61(z, message, i14, arrayList7, i15);
                        }
                    });
                }
            }
            z2 = z4;
        } else {
            AlertsCreator.processError(this.currentAccount, tL_error, null, tLObject2, new Object[0]);
            z2 = true;
        }
        if (z2) {
            getMessagesStorage().markMessageAsSendError(message, z ? 1 : 0);
            message.send_state = 2;
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(message.id));
            processSentMessage(message.id);
            removeFromSendingMessages(message.id, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$63(final TLObject tLObject, Object obj, final MessageObject messageObject, final String str, DelayedMessage delayedMessage, boolean z, final DelayedMessage delayedMessage2, final boolean z2, final TLRPC.Message message, final TLObject tLObject2, final TLRPC.TL_error tL_error) {
        if (tL_error != null && (((tLObject instanceof TLRPC.TL_messages_sendMedia) || (tLObject instanceof TLRPC.TL_messages_editMessage)) && FileRefController.isFileRefError(tL_error.text))) {
            if (obj != null) {
                getFileRefController().requestReference(obj, tLObject, messageObject, str, delayedMessage, Boolean.valueOf(z), delayedMessage2, Boolean.valueOf(z2));
                return;
            } else if (delayedMessage2 != null) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda15
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.this.lambda$performSendMessageRequest$49(message, z2, tLObject, delayedMessage2);
                    }
                });
                return;
            }
        }
        AndroidUtilities.runOnUIThread(tLObject instanceof TLRPC.TL_messages_editMessage ? new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$performSendMessageRequest$52(tL_error, message, tLObject2, messageObject, str, z2, tLObject);
            }
        } : new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$performSendMessageRequest$62(z2, tL_error, message, tLObject2, messageObject, str, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$64(TLRPC.Message message, int i) {
        message.send_state = 0;
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByAck, Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequest$65(final TLRPC.Message message) {
        final int i = message.id;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda93
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$performSendMessageRequest$64(message, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$40(TLObject tLObject, int i, DelayedMessage delayedMessage, ArrayList arrayList, boolean z) {
        boolean z2;
        int i2;
        TLRPC.InputMedia inputMedia;
        int i3;
        TLRPC.TL_inputSingleMedia tL_inputSingleMedia;
        TLRPC.InputMedia inputMedia2;
        if (tLObject instanceof TLRPC.TL_messages_sendMultiMedia) {
            TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) tLObject;
            int size = tL_messages_sendMultiMedia.multi_media.size();
            z2 = false;
            while (i3 < size) {
                if (i >= 0) {
                    i3 = i != i3 ? i3 + 1 : 0;
                    removeFromSendingMessages(((MessageObject) arrayList.get(i3)).getId(), z);
                    tL_inputSingleMedia = tL_messages_sendMultiMedia.multi_media.get(i3);
                    inputMedia2 = tL_inputSingleMedia.media;
                    if (!(inputMedia2 instanceof TLRPC.TL_inputMediaPhoto) || (inputMedia2 instanceof TLRPC.TL_inputMediaDocument)) {
                        tL_inputSingleMedia.media = delayedMessage.inputMedias.get(i3);
                    }
                    delayedMessage.videoEditedInfo = delayedMessage.videoEditedInfos.get(i3);
                    delayedMessage.httpLocation = delayedMessage.httpLocations.get(i3);
                    TLRPC.PhotoSize photoSize = delayedMessage.locations.get(i3);
                    delayedMessage.photoSize = photoSize;
                    delayedMessage.performMediaUpload = true;
                    if (tL_inputSingleMedia.media.file != null || photoSize != null) {
                        z2 = true;
                    }
                    performSendDelayedMessage(delayedMessage, i3);
                } else {
                    if (delayedMessage.parentObjects.get(i3) == null) {
                    }
                    removeFromSendingMessages(((MessageObject) arrayList.get(i3)).getId(), z);
                    tL_inputSingleMedia = tL_messages_sendMultiMedia.multi_media.get(i3);
                    inputMedia2 = tL_inputSingleMedia.media;
                    if (!(inputMedia2 instanceof TLRPC.TL_inputMediaPhoto)) {
                    }
                    tL_inputSingleMedia.media = delayedMessage.inputMedias.get(i3);
                    delayedMessage.videoEditedInfo = delayedMessage.videoEditedInfos.get(i3);
                    delayedMessage.httpLocation = delayedMessage.httpLocations.get(i3);
                    TLRPC.PhotoSize photoSize2 = delayedMessage.locations.get(i3);
                    delayedMessage.photoSize = photoSize2;
                    delayedMessage.performMediaUpload = true;
                    if (tL_inputSingleMedia.media.file != null) {
                    }
                    z2 = true;
                    performSendDelayedMessage(delayedMessage, i3);
                }
            }
        } else if (tLObject instanceof TLRPC.TL_messages_sendMedia) {
            TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) ((TLRPC.TL_messages_sendMedia) tLObject).media;
            int size2 = tL_inputMediaPaidMedia.extended_media.size();
            z2 = false;
            while (i2 < size2) {
                if (i >= 0) {
                    i2 = i != i2 ? i2 + 1 : 0;
                    removeFromSendingMessages(((MessageObject) arrayList.get(i2)).getId(), z);
                    inputMedia = tL_inputMediaPaidMedia.extended_media.get(i2);
                    if (!(inputMedia instanceof TLRPC.TL_inputMediaPhoto) || (inputMedia instanceof TLRPC.TL_inputMediaDocument)) {
                        ArrayList<TLRPC.InputMedia> arrayList2 = tL_inputMediaPaidMedia.extended_media;
                        TLRPC.InputMedia inputMedia3 = delayedMessage.inputMedias.get(i2);
                        arrayList2.set(i2, inputMedia3);
                        inputMedia = inputMedia3;
                    }
                    delayedMessage.videoEditedInfo = delayedMessage.videoEditedInfos.get(i2);
                    delayedMessage.httpLocation = delayedMessage.httpLocations.get(i2);
                    TLRPC.PhotoSize photoSize3 = delayedMessage.locations.get(i2);
                    delayedMessage.photoSize = photoSize3;
                    delayedMessage.performMediaUpload = true;
                    if (inputMedia.file != null || photoSize3 != null) {
                        z2 = true;
                    }
                    performSendDelayedMessage(delayedMessage, i2);
                } else {
                    if (delayedMessage.parentObjects.get(i2) == null) {
                    }
                    removeFromSendingMessages(((MessageObject) arrayList.get(i2)).getId(), z);
                    inputMedia = tL_inputMediaPaidMedia.extended_media.get(i2);
                    if (!(inputMedia instanceof TLRPC.TL_inputMediaPhoto)) {
                    }
                    ArrayList<TLRPC.InputMedia> arrayList22 = tL_inputMediaPaidMedia.extended_media;
                    TLRPC.InputMedia inputMedia32 = delayedMessage.inputMedias.get(i2);
                    arrayList22.set(i2, inputMedia32);
                    inputMedia = inputMedia32;
                    delayedMessage.videoEditedInfo = delayedMessage.videoEditedInfos.get(i2);
                    delayedMessage.httpLocation = delayedMessage.httpLocations.get(i2);
                    TLRPC.PhotoSize photoSize32 = delayedMessage.locations.get(i2);
                    delayedMessage.photoSize = photoSize32;
                    delayedMessage.performMediaUpload = true;
                    if (inputMedia.file != null) {
                    }
                    z2 = true;
                    performSendDelayedMessage(delayedMessage, i2);
                }
            }
        } else {
            z2 = false;
        }
        if (z2) {
            return;
        }
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            TLRPC.Message message = ((MessageObject) arrayList.get(i4)).messageOwner;
            getMessagesStorage().markMessageAsSendError(message, z ? 1 : 0);
            message.send_state = 2;
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(message.id));
            processSentMessage(message.id);
            removeFromSendingMessages(message.id, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$41(TLRPC.TL_updateNewMessage tL_updateNewMessage) {
        getMessagesController().processNewDifferenceParams(-1, tL_updateNewMessage.pts, -1, tL_updateNewMessage.pts_count);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$42(TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage) {
        getMessagesController().processNewChannelDifferenceParams(tL_updateNewChannelMessage.pts, tL_updateNewChannelMessage.pts_count, tL_updateNewChannelMessage.message.peer_id.channel_id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$43(TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage, long j) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(Integer.valueOf(tL_updateNewChannelMessage.message.id));
        getMessagesStorage().updatePinnedMessages(-j, arrayList, true, -1, 0, false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$44(int[] iArr, int[] iArr2, boolean z, boolean z2, MessageObject messageObject, SparseArray sparseArray, ArrayList arrayList, TLRPC.Message message, int i, long j, int i2) {
        char c;
        char c2;
        int i3 = iArr[0] + 1;
        iArr[0] = i3;
        if (i3 != iArr2[0] || z == z2) {
            c = 0;
            c2 = 1;
        } else {
            long dialogId = messageObject.getDialogId();
            int keyAt = (!z2 || sparseArray.size() <= 1) ? 0 : sparseArray.keyAt(0);
            c2 = 1;
            c = 0;
            getMessagesController().deleteMessages(arrayList, null, null, dialogId, false, z ? 1 : 0, false, 0L, null, 0, z2 && !z, keyAt);
        }
        getMediaDataController().increasePeerRaiting(message.dialog_id);
        NotificationCenter notificationCenter = getNotificationCenter();
        int i4 = NotificationCenter.messageReceivedByServer;
        Integer valueOf = Integer.valueOf(i);
        Integer valueOf2 = Integer.valueOf(message.id);
        Long valueOf3 = Long.valueOf(message.dialog_id);
        Long valueOf4 = Long.valueOf(j);
        Integer valueOf5 = Integer.valueOf(i2);
        Boolean valueOf6 = Boolean.valueOf(z2);
        Object[] objArr = new Object[7];
        objArr[c] = valueOf;
        objArr[c2] = valueOf2;
        objArr[2] = message;
        objArr[3] = valueOf3;
        objArr[4] = valueOf4;
        objArr[5] = valueOf5;
        objArr[6] = valueOf6;
        notificationCenter.lambda$postNotificationNameOnUIThread$1(i4, objArr);
        NotificationCenter notificationCenter2 = getNotificationCenter();
        int i5 = NotificationCenter.messageReceivedByServer2;
        Integer valueOf7 = Integer.valueOf(i);
        Integer valueOf8 = Integer.valueOf(message.id);
        Long valueOf9 = Long.valueOf(message.dialog_id);
        Long valueOf10 = Long.valueOf(j);
        Integer valueOf11 = Integer.valueOf(i2);
        Boolean valueOf12 = Boolean.valueOf(z2);
        Object[] objArr2 = new Object[7];
        objArr2[c] = valueOf7;
        objArr2[c2] = valueOf8;
        objArr2[2] = message;
        objArr2[3] = valueOf9;
        objArr2[4] = valueOf10;
        objArr2[5] = valueOf11;
        objArr2[6] = valueOf12;
        notificationCenter2.lambda$postNotificationNameOnUIThread$1(i5, objArr2);
        processSentMessage(i);
        removeFromSendingMessages(i, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$45(final boolean z, final TLRPC.Message message, final int i, ArrayList arrayList, final int[] iArr, final int[] iArr2, final boolean z2, final MessageObject messageObject, final SparseArray sparseArray, final ArrayList arrayList2, final long j, final int i2) {
        int i3 = (message.quick_reply_shortcut_id == 0 && message.quick_reply_shortcut == null) ? z ? 1 : 0 : 5;
        getMessagesStorage().updateMessageStateAndId(message.random_id, MessageObject.getPeerId(message.peer_id), Integer.valueOf(i), message.id, 0, false, i3, message.quick_reply_shortcut_id);
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) arrayList, true, false, false, 0, i3, message.quick_reply_shortcut_id);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda88
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$performSendMessageRequestMulti$44(iArr, iArr2, z2, z, messageObject, sparseArray, arrayList2, message, i, j, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$46(TLRPC.Updates updates) {
        getMessagesController().processUpdates(updates, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$47(TLRPC.TL_error tL_error, TLObject tLObject, final boolean z, ArrayList arrayList, ArrayList arrayList2, TLObject tLObject2) {
        final SendMessagesHelper sendMessagesHelper;
        int i;
        char c;
        boolean z2;
        int i2;
        TLRPC.Updates updates;
        TLRPC.Message message;
        ArrayList arrayList3;
        int i3;
        TLRPC.Message message2;
        TLRPC.Updates updates2;
        LongSparseArray longSparseArray;
        SparseArray sparseArray;
        int i4;
        String quickReplyName;
        int i5;
        TLRPC.MessageReplyHeader messageReplyHeader;
        long j;
        boolean z3 = z ? 1 : 0;
        ArrayList arrayList4 = arrayList;
        TLObject tLObject3 = tLObject2;
        if (tL_error == null) {
            SparseArray sparseArray2 = new SparseArray();
            LongSparseArray longSparseArray2 = new LongSparseArray();
            TLRPC.Updates updates3 = (TLRPC.Updates) tLObject;
            ArrayList<TLRPC.Update> arrayList5 = updates3.updates;
            boolean z4 = z3;
            int i6 = 0;
            LongSparseArray longSparseArray3 = null;
            while (i6 < arrayList5.size()) {
                TLRPC.Update update = arrayList5.get(i6);
                if (update instanceof TLRPC.TL_updateMessageID) {
                    TLRPC.TL_updateMessageID tL_updateMessageID = (TLRPC.TL_updateMessageID) update;
                    longSparseArray2.put(tL_updateMessageID.random_id, Integer.valueOf(tL_updateMessageID.id));
                    arrayList5.remove(i6);
                    i6--;
                } else {
                    if (update instanceof TLRPC.TL_updateNewMessage) {
                        final TLRPC.TL_updateNewMessage tL_updateNewMessage = (TLRPC.TL_updateNewMessage) update;
                        TLRPC.Message message3 = tL_updateNewMessage.message;
                        sparseArray2.put(message3.id, message3);
                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda10
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.this.lambda$performSendMessageRequestMulti$41(tL_updateNewMessage);
                            }
                        });
                    } else if (update instanceof TLRPC.TL_updateNewChannelMessage) {
                        final TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage = (TLRPC.TL_updateNewChannelMessage) update;
                        long updateChannelId = MessagesController.getUpdateChannelId(tL_updateNewChannelMessage);
                        TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(updateChannelId));
                        if (!(chat == null || chat.megagroup) || (messageReplyHeader = tL_updateNewChannelMessage.message.reply_to) == null || (messageReplyHeader.reply_to_top_id == 0 && messageReplyHeader.reply_to_msg_id == 0)) {
                            j = updateChannelId;
                        } else {
                            if (longSparseArray3 == null) {
                                longSparseArray3 = new LongSparseArray();
                            }
                            j = updateChannelId;
                            long dialogId = MessageObject.getDialogId(tL_updateNewChannelMessage.message);
                            SparseArray sparseArray3 = (SparseArray) longSparseArray3.get(dialogId);
                            if (sparseArray3 == null) {
                                sparseArray3 = new SparseArray();
                                longSparseArray3.put(dialogId, sparseArray3);
                            }
                            TLRPC.MessageReplyHeader messageReplyHeader2 = tL_updateNewChannelMessage.message.reply_to;
                            int i7 = messageReplyHeader2.reply_to_top_id;
                            if (i7 == 0) {
                                i7 = messageReplyHeader2.reply_to_msg_id;
                            }
                            TLRPC.MessageReplies messageReplies = (TLRPC.MessageReplies) sparseArray3.get(i7);
                            if (messageReplies == null) {
                                messageReplies = new TLRPC.TL_messageReplies();
                                sparseArray3.put(i7, messageReplies);
                            }
                            TLRPC.Peer peer = tL_updateNewChannelMessage.message.from_id;
                            if (peer != null) {
                                messageReplies.recent_repliers.add(0, peer);
                            }
                            messageReplies.replies++;
                        }
                        TLRPC.Message message4 = tL_updateNewChannelMessage.message;
                        sparseArray2.put(message4.id, message4);
                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda11
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.this.lambda$performSendMessageRequestMulti$42(tL_updateNewChannelMessage);
                            }
                        });
                        arrayList5.remove(i6);
                        i6--;
                        if (tL_updateNewChannelMessage.message.pinned) {
                            final long j2 = j;
                            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda12
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.this.lambda$performSendMessageRequestMulti$43(tL_updateNewChannelMessage, j2);
                                }
                            });
                        }
                    } else if (update instanceof TLRPC.TL_updateNewScheduledMessage) {
                        TLRPC.Message message5 = ((TLRPC.TL_updateNewScheduledMessage) update).message;
                        sparseArray2.put(message5.id, message5);
                        arrayList5.remove(i6);
                        i6--;
                        i5 = 1;
                        z4 = true;
                        i6 += i5;
                    } else if (update instanceof TLRPC.TL_updateQuickReplyMessage) {
                        QuickRepliesController quickRepliesController = QuickRepliesController.getInstance(this.currentAccount);
                        if (arrayList.isEmpty()) {
                            i4 = 0;
                            quickReplyName = null;
                        } else {
                            i4 = 0;
                            quickReplyName = ((MessageObject) arrayList4.get(0)).getQuickReplyName();
                        }
                        quickRepliesController.processUpdate(update, quickReplyName, (arrayList.isEmpty() ? null : Integer.valueOf(((MessageObject) arrayList4.get(i4)).getQuickReplyId())).intValue());
                        TLRPC.Message message6 = ((TLRPC.TL_updateQuickReplyMessage) update).message;
                        sparseArray2.put(message6.id, message6);
                    }
                    arrayList5.remove(i6);
                    i6--;
                    i5 = 1;
                    z4 = false;
                    i6 += i5;
                }
                i5 = 1;
                i6 += i5;
            }
            if (longSparseArray3 != null) {
                getMessagesStorage().putChannelViews(null, null, longSparseArray3, true);
                i2 = 0;
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didUpdateMessagesViews, null, null, longSparseArray3, Boolean.TRUE);
            } else {
                i2 = 0;
            }
            int[] iArr = new int[1];
            iArr[i2] = i2;
            final int[] iArr2 = {i2};
            ArrayList arrayList6 = new ArrayList();
            int i8 = 0;
            while (i8 < arrayList.size()) {
                final MessageObject messageObject = (MessageObject) arrayList4.get(i8);
                String str = (String) arrayList2.get(i8);
                TLRPC.Message message7 = messageObject.messageOwner;
                int i9 = message7.id;
                ArrayList arrayList7 = new ArrayList();
                final int[] iArr3 = iArr;
                Integer num = (Integer) longSparseArray2.get(message7.random_id);
                if (num == null || (message = (TLRPC.Message) sparseArray2.get(num.intValue())) == null) {
                    updates = updates3;
                    z2 = true;
                    break;
                }
                MessageObject.getDialogId(message);
                arrayList7.add(message);
                if ((message.flags & ConnectionsManager.FileTypeVideo) != 0) {
                    TLRPC.Message message8 = messageObject.messageOwner;
                    arrayList3 = arrayList7;
                    message8.ttl_period = message.ttl_period;
                    message8.flags |= ConnectionsManager.FileTypeVideo;
                } else {
                    arrayList3 = arrayList7;
                }
                if (tLObject3 instanceof TLRPC.TL_messages_sendMedia) {
                    i3 = i9;
                    message2 = message7;
                    updates2 = updates3;
                    longSparseArray = longSparseArray2;
                    sparseArray = sparseArray2;
                    updateMediaPaths((MessageObject) arrayList4.get(0), message, message.id, arrayList2, false, -1);
                } else {
                    i3 = i9;
                    message2 = message7;
                    updates2 = updates3;
                    longSparseArray = longSparseArray2;
                    sparseArray = sparseArray2;
                    updateMediaPaths(messageObject, message, message.id, str, false);
                }
                final int mediaExistanceFlags = messageObject.getMediaExistanceFlags();
                message2.id = message.id;
                int i10 = message.quick_reply_shortcut_id;
                message2.quick_reply_shortcut_id = i10;
                if (i10 != 0) {
                    message2.flags |= 1073741824;
                }
                final long j3 = message.grouped_id;
                if (!z3) {
                    Integer num2 = getMessagesController().dialogs_read_outbox_max.get(Long.valueOf(message.dialog_id));
                    if (num2 == null) {
                        num2 = Integer.valueOf(getMessagesStorage().getDialogReadMax(message.out, message.dialog_id));
                        getMessagesController().dialogs_read_outbox_max.put(Long.valueOf(message.dialog_id), num2);
                    }
                    message.unread = num2.intValue() < message.id;
                }
                iArr3[0] = iArr3[0] + 1;
                arrayList6.add(Integer.valueOf(i3));
                getStatsController().incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, 1);
                message2.send_state = 0;
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer, Integer.valueOf(i3), Integer.valueOf(message2.id), message2, Long.valueOf(message2.dialog_id), Long.valueOf(j3), Integer.valueOf(mediaExistanceFlags), Boolean.valueOf(z4));
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer2, Integer.valueOf(i3), Integer.valueOf(message2.id), message2, Long.valueOf(message2.dialog_id), Long.valueOf(j3), Integer.valueOf(mediaExistanceFlags), Boolean.valueOf(z4));
                final boolean z5 = z4;
                final TLRPC.Message message9 = message2;
                final int i11 = i3;
                final ArrayList arrayList8 = arrayList3;
                final ArrayList arrayList9 = arrayList6;
                final SparseArray sparseArray4 = sparseArray;
                getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda13
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.this.lambda$performSendMessageRequestMulti$45(z5, message9, i11, arrayList8, iArr2, iArr3, z, messageObject, sparseArray4, arrayList9, j3, mediaExistanceFlags);
                    }
                });
                i8++;
                z3 = z ? 1 : 0;
                arrayList4 = arrayList;
                tLObject3 = tLObject2;
                arrayList6 = arrayList9;
                iArr = iArr3;
                sparseArray2 = sparseArray;
                longSparseArray2 = longSparseArray;
                updates3 = updates2;
            }
            updates = updates3;
            z2 = false;
            i = 1;
            sendMessagesHelper = this;
            final TLRPC.Updates updates4 = updates;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.lambda$performSendMessageRequestMulti$46(updates4);
                }
            });
            c = 0;
        } else {
            sendMessagesHelper = this;
            i = 1;
            c = 0;
            AlertsCreator.processError(sendMessagesHelper.currentAccount, tL_error, null, tLObject2, new Object[0]);
            z2 = true;
        }
        if (z2) {
            for (int i12 = 0; i12 < arrayList.size(); i12 += i) {
                TLRPC.Message message10 = ((MessageObject) arrayList.get(i12)).messageOwner;
                getMessagesStorage().markMessageAsSendError(message10, z ? 1 : 0);
                message10.send_state = 2;
                NotificationCenter notificationCenter = getNotificationCenter();
                int i13 = NotificationCenter.messageSendError;
                Object[] objArr = new Object[i];
                objArr[c] = Integer.valueOf(message10.id);
                notificationCenter.lambda$postNotificationNameOnUIThread$1(i13, objArr);
                sendMessagesHelper.processSentMessage(message10.id);
                sendMessagesHelper.removeFromSendingMessages(message10.id, z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSendMessageRequestMulti$48(ArrayList arrayList, final TLObject tLObject, final ArrayList arrayList2, final ArrayList arrayList3, final DelayedMessage delayedMessage, final boolean z, final TLObject tLObject2, final TLRPC.TL_error tL_error) {
        if (tL_error != null && FileRefController.isFileRefError(tL_error.text)) {
            final int fileRefErrorIndex = FileRefController.getFileRefErrorIndex(tL_error.text);
            if (arrayList != null) {
                ArrayList arrayList4 = new ArrayList(arrayList);
                if (fileRefErrorIndex >= 0) {
                    int i = 0;
                    while (i < arrayList4.size()) {
                        arrayList4.set(i, fileRefErrorIndex == i ? arrayList4.get(i) : null);
                        i++;
                    }
                }
                getFileRefController().requestReference(arrayList4, tLObject, arrayList2, arrayList3, arrayList4, delayedMessage, Boolean.valueOf(z));
                return;
            }
            if (delayedMessage != null && !delayedMessage.getRetriedToSend(fileRefErrorIndex)) {
                delayedMessage.setRetriedToSend(fileRefErrorIndex, true);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda36
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.this.lambda$performSendMessageRequestMulti$40(tLObject, fileRefErrorIndex, delayedMessage, arrayList2, z);
                    }
                });
                return;
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$performSendMessageRequestMulti$47(tL_error, tLObject2, z, arrayList2, arrayList3, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareImportHistory$67(Uri uri, ArrayList arrayList, MessagesStorage.LongCallback longCallback, long j) {
        if (j != 0) {
            prepareImportHistory(-j, uri, arrayList, longCallback);
        } else {
            longCallback.run(0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareImportHistory$70(MessagesStorage.LongCallback longCallback) {
        Toast.makeText(ApplicationLoader.applicationContext, LocaleController.getString(R.string.ImportFileTooLarge), 0).show();
        longCallback.run(0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareImportHistory$71(HashMap hashMap, long j, ImportingHistory importingHistory, MessagesStorage.LongCallback longCallback) {
        this.importingHistoryFiles.putAll(hashMap);
        this.importingHistoryMap.put(j, importingHistory);
        getFileLoader().uploadFile(importingHistory.historyPath, false, true, 0L, ConnectionsManager.FileTypeFile, true);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, Long.valueOf(j));
        longCallback.run(j);
        try {
            ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, (Class<?>) ImportingService.class));
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0119, code lost:
    
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x011c, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$prepareImportHistory$72(ArrayList arrayList, final long j, Uri uri, final MessagesStorage.LongCallback longCallback) {
        Runnable runnable;
        int i;
        ArrayList arrayList2 = arrayList != null ? arrayList : new ArrayList();
        final ImportingHistory importingHistory = new ImportingHistory();
        importingHistory.mediaPaths = arrayList2;
        importingHistory.dialogId = j;
        importingHistory.peer = getMessagesController().getInputPeer(j);
        final HashMap hashMap = new HashMap();
        int size = arrayList2.size();
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i3 >= size + 1) {
                runnable = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda62
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.this.lambda$prepareImportHistory$71(hashMap, j, importingHistory, longCallback);
                    }
                };
                break;
            }
            Uri uri2 = i3 == 0 ? uri : (Uri) arrayList2.get(i3 - 1);
            if (uri2 == null || AndroidUtilities.isInternalUri(uri2)) {
                i = i3;
                if (i == 0) {
                    runnable = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda61
                        @Override // java.lang.Runnable
                        public final void run() {
                            MessagesStorage.LongCallback.this.run(0L);
                        }
                    };
                    break;
                } else {
                    i3 = i + 1;
                    i2 = 0;
                }
            } else {
                String fixFileName = FileLoader.fixFileName(MediaController.getFileName(uri));
                String str = (fixFileName == null || !fixFileName.endsWith(".zip")) ? "txt" : "zip";
                String copyFileToCache = MediaController.copyFileToCache(uri2, str);
                if ("zip".equals(str)) {
                    File file = new File(copyFileToCache);
                    try {
                        try {
                            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
                            while (true) {
                                try {
                                    ZipEntry nextEntry = zipInputStream.getNextEntry();
                                    if (nextEntry == null) {
                                        break;
                                    }
                                    String name = nextEntry.getName();
                                    if (name != null) {
                                        int lastIndexOf = name.lastIndexOf("/");
                                        if (lastIndexOf >= 0) {
                                            name = name.substring(lastIndexOf + 1);
                                        }
                                        if (name.endsWith(".txt")) {
                                            File createFileInCache = MediaController.createFileInCache(name, "txt");
                                            copyFileToCache = createFileInCache.getAbsolutePath();
                                            FileOutputStream fileOutputStream = new FileOutputStream(createFileInCache);
                                            byte[] bArr = new byte[1024];
                                            while (true) {
                                                int read = zipInputStream.read(bArr);
                                                if (read <= 0) {
                                                    break;
                                                } else {
                                                    fileOutputStream.write(bArr, i2, read);
                                                }
                                            }
                                            fileOutputStream.close();
                                        }
                                    }
                                } catch (Throwable th) {
                                    try {
                                        zipInputStream.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                    throw th;
                                }
                            }
                            zipInputStream.closeEntry();
                            zipInputStream.close();
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    } catch (IOException e2) {
                        FileLog.e(e2);
                    }
                    try {
                        file.delete();
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                }
                if (copyFileToCache == null) {
                    i = i3;
                } else {
                    File file2 = new File(copyFileToCache);
                    if (file2.exists()) {
                        long length = file2.length();
                        if (length != 0) {
                            i = i3;
                            importingHistory.totalSize += length;
                            if (i != 0) {
                                importingHistory.uploadMedia.add(copyFileToCache);
                            } else {
                                if (length > 33554432) {
                                    file2.delete();
                                    runnable = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda59
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SendMessagesHelper.lambda$prepareImportHistory$70(MessagesStorage.LongCallback.this);
                                        }
                                    };
                                    break;
                                }
                                importingHistory.historyPath = copyFileToCache;
                            }
                            importingHistory.uploadSet.add(copyFileToCache);
                            hashMap.put(copyFileToCache, importingHistory);
                        }
                    }
                    i = i3;
                    if (i == 0) {
                        runnable = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda60
                            @Override // java.lang.Runnable
                            public final void run() {
                                MessagesStorage.LongCallback.this.run(0L);
                            }
                        };
                        break;
                    }
                }
                i3 = i + 1;
                i2 = 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareImportStickers$74(ImportingStickers importingStickers, HashMap hashMap, String str, MessagesStorage.StringCallback stringCallback) {
        if (importingStickers.uploadMedia.get(0).item != null) {
            importingStickers.startImport();
        } else {
            this.importingStickersFiles.putAll(hashMap);
            this.importingStickersMap.put(str, importingStickers);
            importingStickers.initImport();
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.historyImportProgressChanged, str);
            stringCallback.run(str);
        }
        try {
            ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, (Class<?>) ImportingService.class));
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareImportStickers$75(String str, final String str2, String str3, ArrayList arrayList, final MessagesStorage.StringCallback stringCallback) {
        Runnable runnable;
        final ImportingStickers importingStickers = new ImportingStickers();
        importingStickers.title = str;
        importingStickers.shortName = str2;
        importingStickers.software = str3;
        final HashMap hashMap = new HashMap();
        int size = arrayList.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                runnable = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda82
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.this.lambda$prepareImportStickers$74(importingStickers, hashMap, str2, stringCallback);
                    }
                };
                break;
            }
            ImportingSticker importingSticker = (ImportingSticker) arrayList.get(i);
            File file = new File(importingSticker.path);
            if (file.exists()) {
                long length = file.length();
                if (length != 0) {
                    importingStickers.totalSize += length;
                    importingStickers.uploadMedia.add(importingSticker);
                    importingStickers.uploadSet.put(importingSticker.path, importingSticker);
                    hashMap.put(importingSticker.path, importingStickers);
                    i++;
                }
            }
            if (i == 0) {
                runnable = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda81
                    @Override // java.lang.Runnable
                    public final void run() {
                        MessagesStorage.StringCallback.this.run(null);
                    }
                };
                break;
            }
            i++;
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareSendingAudioDocuments$77(MessageObject messageObject, AccountInstance accountInstance, TLRPC.TL_document tL_document, MessageObject messageObject2, HashMap hashMap, String str, long j, MessageObject messageObject3, MessageObject messageObject4, String str2, ArrayList arrayList, boolean z, int i, TL_stories.StoryItem storyItem, String str3, int i2, long j2, boolean z2) {
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, null, null, tL_document, messageObject2.messageOwner.attachPath, hashMap, false, false, str);
            return;
        }
        SendMessageParams of = SendMessageParams.of(tL_document, null, messageObject2.messageOwner.attachPath, j, messageObject3, messageObject4, str2, arrayList, null, hashMap, z, i, 0, str, null, false, false);
        of.replyToStoryItem = storyItem;
        of.quick_reply_shortcut = str3;
        of.quick_reply_shortcut_id = i2;
        of.effect_id = j2;
        of.invert_media = z2;
        accountInstance.getSendMessagesHelper().sendMessage(of);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00b3  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00af  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$prepareSendingAudioDocuments$78(ArrayList arrayList, final long j, final AccountInstance accountInstance, CharSequence charSequence, final MessageObject messageObject, final MessageObject messageObject2, final MessageObject messageObject3, final boolean z, final int i, final TL_stories.StoryItem storyItem, final String str, final int i2, final long j2, final boolean z2) {
        final TLRPC.TL_document tL_document;
        String str2;
        final HashMap hashMap;
        int size = arrayList.size();
        long j3 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i3 < size) {
            final MessageObject messageObject4 = (MessageObject) arrayList.get(i3);
            String str3 = messageObject4.messageOwner.attachPath;
            File file = new File(str3);
            boolean isEncryptedDialog = DialogObject.isEncryptedDialog(j);
            if (!isEncryptedDialog && size > 1 && i4 % 10 == 0) {
                j3 = Utilities.random.nextLong();
                i4 = 0;
            }
            if (str3 != null) {
                str3 = str3 + MediaStreamTrack.AUDIO_TRACK_KIND + file.length();
            }
            if (!isEncryptedDialog) {
                Object[] sentFile = accountInstance.getMessagesStorage().getSentFile(str3, !isEncryptedDialog ? 1 : 4);
                if (sentFile != null) {
                    Object obj = sentFile[0];
                    if (obj instanceof TLRPC.TL_document) {
                        tL_document = (TLRPC.TL_document) obj;
                        String str4 = (String) sentFile[1];
                        ensureMediaThumbExists(accountInstance, isEncryptedDialog, tL_document, str3, null, 0L);
                        str2 = str4;
                        if (tL_document == null) {
                            tL_document = (TLRPC.TL_document) messageObject4.messageOwner.media.document;
                        }
                        if (!isEncryptedDialog && accountInstance.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j))) == null) {
                            return;
                        }
                        CharSequence[] charSequenceArr = {charSequence};
                        final ArrayList<TLRPC.MessageEntity> entities = i3 != 0 ? accountInstance.getMediaDataController().getEntities(charSequenceArr, true) : null;
                        final String charSequence2 = i3 == 0 ? charSequenceArr[0].toString() : null;
                        hashMap = new HashMap();
                        if (str3 != null) {
                            hashMap.put("originalPath", str3);
                        }
                        if (str2 != null) {
                            hashMap.put("parentObject", str2);
                        }
                        i4++;
                        hashMap.put("groupId", "" + j3);
                        if (i4 != 10 || i3 == size - 1) {
                            hashMap.put("final", "1");
                        }
                        final String str5 = str2;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda52
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.lambda$prepareSendingAudioDocuments$77(MessageObject.this, accountInstance, tL_document, messageObject4, hashMap, str5, j, messageObject2, messageObject3, charSequence2, entities, z, i, storyItem, str, i2, j2, z2);
                            }
                        });
                        i3++;
                    }
                }
            }
            tL_document = null;
            str2 = null;
            if (tL_document == null) {
            }
            if (!isEncryptedDialog) {
            }
            CharSequence[] charSequenceArr2 = {charSequence};
            if (i3 != 0) {
            }
            final String charSequence22 = i3 == 0 ? charSequenceArr2[0].toString() : null;
            hashMap = new HashMap();
            if (str3 != null) {
            }
            if (str2 != null) {
            }
            i4++;
            hashMap.put("groupId", "" + j3);
            if (i4 != 10) {
            }
            hashMap.put("final", "1");
            final String str52 = str2;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda52
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.lambda$prepareSendingAudioDocuments$77(MessageObject.this, accountInstance, tL_document, messageObject4, hashMap, str52, j, messageObject2, messageObject3, charSequence22, entities, z, i, storyItem, str, i2, j2, z2);
                }
            });
            i3++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareSendingBotContextResult$84(TLRPC.TL_document tL_document, Bitmap[] bitmapArr, String[] strArr, String str, long j, MessageObject messageObject, MessageObject messageObject2, TLRPC.BotInlineResult botInlineResult, HashMap hashMap, boolean z, int i, TLRPC.TL_photo tL_photo, TLRPC.TL_game tL_game, String str2, int i2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, AccountInstance accountInstance) {
        SendMessageParams sendMessageParams;
        if (tL_document != null) {
            if (bitmapArr[0] != null && strArr[0] != null) {
                ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmapArr[0]), strArr[0], false);
            }
            TLRPC.BotInlineMessage botInlineMessage = botInlineResult.send_message;
            sendMessageParams = SendMessageParams.of(tL_document, null, str, j, messageObject, messageObject2, botInlineMessage.message, botInlineMessage.entities, botInlineMessage.reply_markup, hashMap, z, i, 0, botInlineResult, null, false);
        } else {
            sendMessageParams = null;
            if (tL_photo != null) {
                TLRPC.WebDocument webDocument = botInlineResult.content;
                String str3 = webDocument != null ? webDocument.url : null;
                TLRPC.BotInlineMessage botInlineMessage2 = botInlineResult.send_message;
                sendMessageParams = SendMessageParams.of(tL_photo, str3, j, messageObject, messageObject2, botInlineMessage2.message, botInlineMessage2.entities, botInlineMessage2.reply_markup, hashMap, z, i, 0, botInlineResult, false);
            } else if (tL_game != null) {
                sendMessageParams = SendMessageParams.of(tL_game, j, messageObject, messageObject2, botInlineResult.send_message.reply_markup, (HashMap<String, String>) hashMap, z, i);
            }
        }
        if (sendMessageParams != null) {
            sendMessageParams.quick_reply_shortcut = str2;
            sendMessageParams.quick_reply_shortcut_id = i2;
            sendMessageParams.replyToStoryItem = storyItem;
            sendMessageParams.replyQuote = replyQuote;
            accountInstance.getSendMessagesHelper().sendMessage(sendMessageParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x01c6, code lost:
    
        if (r0.equals("voice") == false) goto L81;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0395 A[Catch: all -> 0x034f, TryCatch #1 {all -> 0x034f, blocks: (B:144:0x031d, B:146:0x0323, B:148:0x032a, B:150:0x0330, B:152:0x0338, B:154:0x0346, B:155:0x0360, B:157:0x0351, B:159:0x0395, B:163:0x039f, B:165:0x03a5, B:166:0x03b0, B:168:0x038c, B:172:0x03ed, B:174:0x03f1, B:176:0x042a, B:178:0x0433, B:179:0x043e), top: B:101:0x01fe }] */
    /* JADX WARN: Type inference failed for: r0v121, types: [org.telegram.tgnet.TLRPC$TL_game] */
    /* JADX WARN: Type inference failed for: r15v3, types: [org.telegram.tgnet.TLRPC$TL_game] */
    /* JADX WARN: Type inference failed for: r5v25, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v15 */
    /* JADX WARN: Type inference failed for: r7v4, types: [int] */
    /* JADX WARN: Type inference failed for: r7v5 */
    /* JADX WARN: Type inference failed for: r7v6 */
    /* JADX WARN: Type inference failed for: r7v7, types: [java.lang.String] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$prepareSendingBotContextResult$85(final long j, final TLRPC.BotInlineResult botInlineResult, final AccountInstance accountInstance, final HashMap hashMap, final BaseFragment baseFragment, final MessageObject messageObject, final MessageObject messageObject2, final boolean z, final int i, final String str, final int i2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote) {
        char c;
        TLRPC.TL_document tL_document;
        TLRPC.TL_photo tL_photo;
        final TLRPC.TL_photo tL_photo2;
        Bitmap loadBitmap;
        Bitmap bitmap;
        TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio;
        String str2;
        TLRPC.TL_photo tL_photo3;
        Runnable runnable;
        TLRPC.WebDocument webDocument;
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(j);
        if (!"game".equals(botInlineResult.type)) {
            if (botInlineResult instanceof TLRPC.TL_botInlineMediaResult) {
                TLRPC.Document document = botInlineResult.document;
                if (document == null) {
                    TLRPC.Photo photo = botInlineResult.photo;
                    if (photo != null && (photo instanceof TLRPC.TL_photo)) {
                        tL_photo2 = (TLRPC.TL_photo) photo;
                        tL_document = null;
                        tL_photo = null;
                        tL_photo3 = null;
                    }
                } else if (document instanceof TLRPC.TL_document) {
                    tL_document = (TLRPC.TL_document) document;
                    tL_photo = null;
                }
                tL_document = null;
                tL_photo = null;
            } else {
                TLRPC.WebDocument webDocument2 = botInlineResult.content;
                if (webDocument2 != null) {
                    String httpUrlExtension = ImageLoader.getHttpUrlExtension(webDocument2.url, null);
                    File file = new File(FileLoader.getDirectory(4), Utilities.MD5(botInlineResult.content.url) + (TextUtils.isEmpty(httpUrlExtension) ? FileLoader.getExtensionByMimeType(botInlineResult.content.mime_type) : "." + httpUrlExtension));
                    String absolutePath = file.exists() ? file.getAbsolutePath() : botInlineResult.content.url;
                    String str3 = botInlineResult.type;
                    str3.hashCode();
                    char c2 = 5;
                    switch (str3.hashCode()) {
                        case -1890252483:
                            if (str3.equals("sticker")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case 102340:
                            if (str3.equals("gif")) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 3143036:
                            if (str3.equals("file")) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        case 93166550:
                            if (str3.equals(MediaStreamTrack.AUDIO_TRACK_KIND)) {
                                c = 3;
                                break;
                            }
                            c = 65535;
                            break;
                        case 106642994:
                            if (str3.equals("photo")) {
                                c = 4;
                                break;
                            }
                            c = 65535;
                            break;
                        case 112202875:
                            if (str3.equals(MediaStreamTrack.VIDEO_TRACK_KIND)) {
                                c = 5;
                                break;
                            }
                            c = 65535;
                            break;
                        case 112386354:
                            if (str3.equals("voice")) {
                                c = 6;
                                break;
                            }
                            c = 65535;
                            break;
                        default:
                            c = 65535;
                            break;
                    }
                    switch (c) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 5:
                        case 6:
                            TLRPC.TL_document tL_document2 = new TLRPC.TL_document();
                            tL_document2.id = 0L;
                            tL_document2.size = 0L;
                            tL_document2.dc_id = 0;
                            tL_document2.mime_type = botInlineResult.content.mime_type;
                            tL_document2.file_reference = new byte[0];
                            tL_document2.date = accountInstance.getConnectionsManager().getCurrentTime();
                            TLRPC.TL_documentAttributeFilename tL_documentAttributeFilename = new TLRPC.TL_documentAttributeFilename();
                            tL_document2.attributes.add(tL_documentAttributeFilename);
                            String str4 = botInlineResult.type;
                            str4.hashCode();
                            ?? hashCode = str4.hashCode();
                            switch (hashCode) {
                                case -1890252483:
                                    if (str4.equals("sticker")) {
                                        c2 = 0;
                                        break;
                                    }
                                    c2 = 65535;
                                    break;
                                case 102340:
                                    if (str4.equals("gif")) {
                                        c2 = 1;
                                        break;
                                    }
                                    c2 = 65535;
                                    break;
                                case 3143036:
                                    if (str4.equals("file")) {
                                        c2 = 2;
                                        break;
                                    }
                                    c2 = 65535;
                                    break;
                                case 93166550:
                                    if (str4.equals(MediaStreamTrack.AUDIO_TRACK_KIND)) {
                                        c2 = 3;
                                        break;
                                    }
                                    c2 = 65535;
                                    break;
                                case 112202875:
                                    if (str4.equals(MediaStreamTrack.VIDEO_TRACK_KIND)) {
                                        c2 = 4;
                                        break;
                                    }
                                    c2 = 65535;
                                    break;
                                case 112386354:
                                    break;
                                default:
                                    c2 = 65535;
                                    break;
                            }
                            try {
                                switch (c2) {
                                    case 0:
                                        hashCode = absolutePath;
                                        TLRPC.TL_documentAttributeSticker tL_documentAttributeSticker = new TLRPC.TL_documentAttributeSticker();
                                        tL_documentAttributeSticker.alt = "";
                                        tL_documentAttributeSticker.stickerset = new TLRPC.TL_inputStickerSetEmpty();
                                        tL_document2.attributes.add(tL_documentAttributeSticker);
                                        TLRPC.TL_documentAttributeImageSize tL_documentAttributeImageSize = new TLRPC.TL_documentAttributeImageSize();
                                        int[] inlineResultWidthAndHeight = MessageObject.getInlineResultWidthAndHeight(botInlineResult);
                                        tL_documentAttributeImageSize.w = inlineResultWidthAndHeight[0];
                                        tL_documentAttributeImageSize.h = inlineResultWidthAndHeight[1];
                                        tL_document2.attributes.add(tL_documentAttributeImageSize);
                                        tL_documentAttributeFilename.file_name = "sticker.webp";
                                        if (botInlineResult.thumb != null) {
                                            Bitmap loadBitmap2 = ImageLoader.loadBitmap(new File(FileLoader.getDirectory(4), Utilities.MD5(botInlineResult.thumb.url) + "." + ImageLoader.getHttpUrlExtension(botInlineResult.thumb.url, "webp")).getAbsolutePath(), null, 90.0f, 90.0f, true);
                                            if (loadBitmap2 != null) {
                                                TLRPC.PhotoSize scaleAndSaveImage = ImageLoader.scaleAndSaveImage(loadBitmap2, 90.0f, 90.0f, 55, false);
                                                if (scaleAndSaveImage != null) {
                                                    tL_document2.thumbs.add(scaleAndSaveImage);
                                                    tL_document2.flags |= 1;
                                                }
                                                loadBitmap2.recycle();
                                                break;
                                            }
                                        }
                                        break;
                                    case 1:
                                        tL_documentAttributeFilename.file_name = "animation.gif";
                                        hashCode = absolutePath;
                                        if (hashCode.endsWith("mp4")) {
                                            tL_document2.mime_type = "video/mp4";
                                            tL_document2.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                                        } else {
                                            tL_document2.mime_type = "image/gif";
                                        }
                                        int i3 = isEncryptedDialog ? 90 : 320;
                                        if (hashCode.endsWith("mp4")) {
                                            bitmap = createVideoThumbnail(hashCode, 1);
                                            if (bitmap == null) {
                                                TLRPC.WebDocument webDocument3 = botInlineResult.thumb;
                                                if ((webDocument3 instanceof TLRPC.TL_webDocument) && "video/mp4".equals(webDocument3.mime_type)) {
                                                    String httpUrlExtension2 = ImageLoader.getHttpUrlExtension(botInlineResult.thumb.url, null);
                                                    loadBitmap = createVideoThumbnail(new File(FileLoader.getDirectory(4), Utilities.MD5(botInlineResult.thumb.url) + (TextUtils.isEmpty(httpUrlExtension2) ? FileLoader.getExtensionByMimeType(botInlineResult.thumb.mime_type) : "." + httpUrlExtension2)).getAbsolutePath(), 1);
                                                }
                                            }
                                            if (bitmap != null) {
                                                float f = i3;
                                                TLRPC.PhotoSize scaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(bitmap, f, f, i3 > 90 ? 80 : 55, false);
                                                if (scaleAndSaveImage2 != null) {
                                                    tL_document2.thumbs.add(scaleAndSaveImage2);
                                                    tL_document2.flags |= 1;
                                                }
                                                bitmap.recycle();
                                                break;
                                            }
                                        } else {
                                            float f2 = i3;
                                            loadBitmap = ImageLoader.loadBitmap(hashCode, null, f2, f2, true);
                                        }
                                        bitmap = loadBitmap;
                                        if (bitmap != null) {
                                        }
                                        break;
                                    case 2:
                                        int lastIndexOf = botInlineResult.content.mime_type.lastIndexOf(47);
                                        if (lastIndexOf != -1) {
                                            tL_documentAttributeFilename.file_name = "file." + botInlineResult.content.mime_type.substring(lastIndexOf + 1);
                                        } else {
                                            tL_documentAttributeFilename.file_name = "file";
                                        }
                                        hashCode = absolutePath;
                                        break;
                                    case 3:
                                        tL_documentAttributeAudio = new TLRPC.TL_documentAttributeAudio();
                                        tL_documentAttributeAudio.duration = MessageObject.getInlineResultDuration(botInlineResult);
                                        tL_documentAttributeAudio.title = botInlineResult.title;
                                        int i4 = tL_documentAttributeAudio.flags;
                                        tL_documentAttributeAudio.flags = i4 | 1;
                                        String str5 = botInlineResult.description;
                                        if (str5 != null) {
                                            tL_documentAttributeAudio.performer = str5;
                                            tL_documentAttributeAudio.flags = i4 | 3;
                                        }
                                        str2 = "audio.mp3";
                                        tL_documentAttributeFilename.file_name = str2;
                                        tL_document2.attributes.add(tL_documentAttributeAudio);
                                        hashCode = absolutePath;
                                        break;
                                    case 4:
                                        tL_documentAttributeFilename.file_name = "video.mp4";
                                        TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo();
                                        int[] inlineResultWidthAndHeight2 = MessageObject.getInlineResultWidthAndHeight(botInlineResult);
                                        tL_documentAttributeVideo.w = inlineResultWidthAndHeight2[0];
                                        tL_documentAttributeVideo.h = inlineResultWidthAndHeight2[1];
                                        tL_documentAttributeVideo.duration = MessageObject.getInlineResultDuration(botInlineResult);
                                        tL_documentAttributeVideo.supports_streaming = true;
                                        tL_document2.attributes.add(tL_documentAttributeVideo);
                                        try {
                                            if (botInlineResult.thumb != null) {
                                                Bitmap loadBitmap3 = ImageLoader.loadBitmap(new File(FileLoader.getDirectory(4), Utilities.MD5(botInlineResult.thumb.url) + "." + ImageLoader.getHttpUrlExtension(botInlineResult.thumb.url, "jpg")).getAbsolutePath(), null, 90.0f, 90.0f, true);
                                                if (loadBitmap3 != null) {
                                                    TLRPC.PhotoSize scaleAndSaveImage3 = ImageLoader.scaleAndSaveImage(loadBitmap3, 90.0f, 90.0f, 55, false);
                                                    if (scaleAndSaveImage3 != null) {
                                                        tL_document2.thumbs.add(scaleAndSaveImage3);
                                                        tL_document2.flags |= 1;
                                                    }
                                                    loadBitmap3.recycle();
                                                }
                                            }
                                        } catch (Throwable th) {
                                            FileLog.e(th);
                                        }
                                        hashCode = absolutePath;
                                        break;
                                    case 5:
                                        tL_documentAttributeAudio = new TLRPC.TL_documentAttributeAudio();
                                        tL_documentAttributeAudio.duration = MessageObject.getInlineResultDuration(botInlineResult);
                                        tL_documentAttributeAudio.voice = true;
                                        str2 = "audio.ogg";
                                        tL_documentAttributeFilename.file_name = str2;
                                        tL_document2.attributes.add(tL_documentAttributeAudio);
                                        hashCode = absolutePath;
                                        break;
                                    default:
                                        hashCode = absolutePath;
                                        break;
                                }
                            } catch (Throwable th2) {
                                FileLog.e(th2);
                            }
                            if (tL_documentAttributeFilename.file_name == null) {
                                tL_documentAttributeFilename.file_name = "file";
                            }
                            if (tL_document2.mime_type == null) {
                                tL_document2.mime_type = "application/octet-stream";
                            }
                            if (tL_document2.thumbs.isEmpty()) {
                                TLRPC.TL_photoSize tL_photoSize = new TLRPC.TL_photoSize();
                                int[] inlineResultWidthAndHeight3 = MessageObject.getInlineResultWidthAndHeight(botInlineResult);
                                tL_photoSize.w = inlineResultWidthAndHeight3[0];
                                tL_photoSize.h = inlineResultWidthAndHeight3[1];
                                tL_photoSize.size = 0;
                                tL_photoSize.location = new TLRPC.TL_fileLocationUnavailable();
                                tL_photoSize.type = "x";
                                tL_document2.thumbs.add(tL_photoSize);
                                tL_document2.flags |= 1;
                            }
                            tL_document = tL_document2;
                            tL_photo = hashCode;
                            tL_photo2 = null;
                            tL_photo3 = tL_photo2;
                            break;
                        case 4:
                            TLRPC.TL_photo generatePhotoSizes = file.exists() ? accountInstance.getSendMessagesHelper().generatePhotoSizes(absolutePath, null) : null;
                            if (generatePhotoSizes == null) {
                                generatePhotoSizes = new TLRPC.TL_photo();
                                generatePhotoSizes.date = accountInstance.getConnectionsManager().getCurrentTime();
                                generatePhotoSizes.file_reference = new byte[0];
                                TLRPC.TL_photoSize tL_photoSize2 = new TLRPC.TL_photoSize();
                                int[] inlineResultWidthAndHeight4 = MessageObject.getInlineResultWidthAndHeight(botInlineResult);
                                tL_photoSize2.w = inlineResultWidthAndHeight4[0];
                                tL_photoSize2.h = inlineResultWidthAndHeight4[1];
                                tL_photoSize2.size = 1;
                                tL_photoSize2.location = new TLRPC.TL_fileLocationUnavailable();
                                tL_photoSize2.type = "x";
                                generatePhotoSizes.sizes.add(tL_photoSize2);
                            }
                            tL_photo2 = generatePhotoSizes;
                            tL_photo = absolutePath;
                            tL_document = null;
                            tL_photo3 = null;
                            break;
                        default:
                            tL_photo = absolutePath;
                            tL_document = null;
                            tL_photo2 = null;
                            tL_photo3 = tL_photo2;
                            break;
                    }
                }
                tL_document = null;
                tL_photo = null;
            }
            tL_photo2 = tL_photo;
            tL_photo3 = tL_photo2;
        } else {
            if (isEncryptedDialog) {
                return;
            }
            ?? tL_game = new TLRPC.TL_game();
            tL_game.title = botInlineResult.title;
            tL_game.description = botInlineResult.description;
            tL_game.short_name = botInlineResult.id;
            TLRPC.Photo photo2 = botInlineResult.photo;
            tL_game.photo = photo2;
            if (photo2 == null) {
                tL_game.photo = new TLRPC.TL_photoEmpty();
            }
            TLRPC.Document document2 = botInlineResult.document;
            if (document2 instanceof TLRPC.TL_document) {
                tL_game.document = document2;
                tL_game.flags |= 1;
            }
            tL_photo3 = tL_game;
            tL_document = null;
            tL_photo = null;
            tL_photo2 = null;
        }
        if (hashMap != null && (webDocument = botInlineResult.content) != null) {
            hashMap.put("originalPath", webDocument.url);
        }
        final Bitmap[] bitmapArr = new Bitmap[1];
        final String[] strArr = new String[1];
        if (isEncryptedDialog && tL_document != null) {
            for (int i5 = 0; i5 < tL_document.attributes.size(); i5++) {
                if (tL_document.attributes.get(i5) instanceof TLRPC.TL_documentAttributeVideo) {
                    TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo2 = (TLRPC.TL_documentAttributeVideo) tL_document.attributes.get(i5);
                    TLRPC.TL_documentAttributeVideo_layer159 tL_documentAttributeVideo_layer159 = new TLRPC.TL_documentAttributeVideo_layer159();
                    tL_documentAttributeVideo_layer159.flags = tL_documentAttributeVideo2.flags;
                    tL_documentAttributeVideo_layer159.round_message = tL_documentAttributeVideo2.round_message;
                    tL_documentAttributeVideo_layer159.supports_streaming = tL_documentAttributeVideo2.supports_streaming;
                    tL_documentAttributeVideo_layer159.duration = tL_documentAttributeVideo2.duration;
                    tL_documentAttributeVideo_layer159.w = tL_documentAttributeVideo2.w;
                    tL_documentAttributeVideo_layer159.h = tL_documentAttributeVideo2.h;
                    tL_document.attributes.set(i5, tL_documentAttributeVideo_layer159);
                }
            }
        }
        if (MessageObject.isGifDocument(tL_document)) {
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_document.thumbs, 320);
            File pathToAttach = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(tL_document);
            if (!pathToAttach.exists()) {
                pathToAttach = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(tL_document, true);
            }
            ensureMediaThumbExists(accountInstance, isEncryptedDialog, tL_document, pathToAttach.getAbsolutePath(), null, 0L);
            strArr[0] = getKeyForPhotoSize(accountInstance, closestPhotoSizeWithSize, bitmapArr, true, true);
        }
        TLRPC.InputPeer inputPeer = !DialogObject.isEncryptedDialog(j) ? accountInstance.getMessagesController().getInputPeer(j) : null;
        if (inputPeer == null || inputPeer.user_id == 0 || accountInstance.getMessagesController().getUserFull(inputPeer.user_id) == null || !accountInstance.getMessagesController().getUserFull(inputPeer.user_id).voice_messages_forbidden || tL_document == null) {
            final TLRPC.TL_document tL_document3 = tL_document;
            final ?? r5 = tL_photo;
            final ?? r15 = tL_photo3;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda75
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.lambda$prepareSendingBotContextResult$84(TLRPC.TL_document.this, bitmapArr, strArr, r5, j, messageObject, messageObject2, botInlineResult, hashMap, z, i, tL_photo2, r15, str, i2, storyItem, replyQuote, accountInstance);
                }
            });
            return;
        }
        if (MessageObject.isVoiceDocument(tL_document)) {
            runnable = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda73
                @Override // java.lang.Runnable
                public final void run() {
                    AlertsCreator.showSendMediaAlert(7, BaseFragment.this, null);
                }
            };
        } else if (!MessageObject.isRoundVideoDocument(tL_document)) {
            return;
        } else {
            runnable = new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda74
                @Override // java.lang.Runnable
                public final void run() {
                    AlertsCreator.showSendMediaAlert(8, BaseFragment.this, null);
                }
            };
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareSendingDocumentInternal$76(MessageObject messageObject, AccountInstance accountInstance, TLRPC.TL_document tL_document, String str, HashMap hashMap, String str2, long j, MessageObject messageObject2, MessageObject messageObject3, String str3, ArrayList arrayList, boolean z, int i, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, String str4, int i2, long j2, boolean z2) {
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, null, null, tL_document, str, hashMap, false, false, str2);
            return;
        }
        SendMessageParams of = SendMessageParams.of(tL_document, null, str, j, messageObject2, messageObject3, str3, arrayList, null, hashMap, z, i, 0, str2, null, false);
        of.replyToStoryItem = storyItem;
        of.replyQuote = replyQuote;
        of.quick_reply_shortcut = str4;
        of.quick_reply_shortcut_id = i2;
        of.effect_id = j2;
        of.invert_media = z2;
        accountInstance.getSendMessagesHelper().sendMessage(of);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareSendingDocuments$80(long j, ArrayList arrayList, String str, AccountInstance accountInstance, int i, ArrayList arrayList2, String str2, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, MessageObject messageObject3, boolean z, InputContentInfoCompat inputContentInfoCompat, String str3, int i2, long j2, boolean z2, ArrayList arrayList3) {
        Integer[] numArr;
        long[] jArr;
        ArrayList arrayList4;
        int i3;
        boolean z3;
        AccountInstance accountInstance2;
        AccountInstance accountInstance3 = accountInstance;
        int i4 = i;
        int i5 = 1;
        long[] jArr2 = new long[1];
        Integer[] numArr2 = new Integer[1];
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(j);
        int i6 = 10;
        if (arrayList != null) {
            int size = arrayList.size();
            i3 = 0;
            int i7 = 0;
            boolean z4 = true;
            int i8 = 0;
            while (i8 < size) {
                String str4 = i8 == 0 ? str : null;
                if (!isEncryptedDialog && size > i5 && i7 % 10 == 0) {
                    long j3 = jArr2[0];
                    if (j3 != 0) {
                        finishGroup(accountInstance3, j3, i4);
                    }
                    jArr2[0] = Utilities.random.nextLong();
                    i7 = 0;
                }
                int i9 = i7 + 1;
                long j4 = jArr2[0];
                int i10 = size;
                int i11 = i8;
                Integer[] numArr3 = numArr2;
                long[] jArr3 = jArr2;
                i3 = prepareSendingDocumentInternal(accountInstance, (String) arrayList.get(i8), (String) arrayList2.get(i8), null, str2, j, messageObject, messageObject2, storyItem, replyQuote, null, messageObject3, jArr3, i9 == i6 || i8 == size + (-1), str4, z, i, numArr3, inputContentInfoCompat == null, str3, i2, z4 ? j2 : 0L, z2);
                long j5 = jArr3[0];
                i7 = (j4 != j5 || j5 == -1) ? 1 : i9;
                i8 = i11 + 1;
                accountInstance3 = accountInstance;
                i4 = i;
                size = i10;
                numArr2 = numArr3;
                jArr2 = jArr3;
                z4 = false;
                i6 = 10;
                i5 = 1;
            }
            numArr = numArr2;
            jArr = jArr2;
            arrayList4 = arrayList3;
            z3 = z4;
        } else {
            numArr = numArr2;
            jArr = jArr2;
            arrayList4 = arrayList3;
            i3 = 0;
            z3 = true;
        }
        if (arrayList4 != null) {
            jArr[0] = 0;
            int size2 = arrayList3.size();
            int i12 = 0;
            int i13 = 0;
            while (i13 < arrayList3.size()) {
                String str5 = (i13 == 0 && (arrayList == null || arrayList.size() == 0)) ? str : null;
                if (isEncryptedDialog) {
                    accountInstance2 = accountInstance;
                } else if (size2 <= 1 || i12 % 10 != 0) {
                    accountInstance2 = accountInstance;
                } else {
                    long j6 = jArr[0];
                    accountInstance2 = accountInstance;
                    if (j6 != 0) {
                        finishGroup(accountInstance2, j6, i);
                    }
                    jArr[0] = Utilities.random.nextLong();
                    i12 = 0;
                }
                int i14 = i12 + 1;
                long j7 = jArr[0];
                int i15 = i13;
                int i16 = size2;
                i3 = prepareSendingDocumentInternal(accountInstance, null, null, (Uri) arrayList4.get(i13), str2, j, messageObject, messageObject2, storyItem, replyQuote, null, messageObject3, jArr, i14 == 10 || i13 == size2 + (-1), str5, z, i, numArr, inputContentInfoCompat == null, str3, i2, z3 ? j2 : 0L, z2);
                long j8 = jArr[0];
                i12 = (j7 != j8 || j8 == -1) ? 1 : i14;
                i13 = i15 + 1;
                arrayList4 = arrayList3;
                size2 = i16;
                z3 = false;
            }
        }
        if (inputContentInfoCompat != null) {
            inputContentInfoCompat.releasePermission();
        }
        handleError(i3, accountInstance);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareSendingMedia$89(MediaSendPrepareWorker mediaSendPrepareWorker, AccountInstance accountInstance, SendingMediaInfo sendingMediaInfo, boolean z) {
        mediaSendPrepareWorker.photo = accountInstance.getSendMessagesHelper().generatePhotoSizes(sendingMediaInfo.path, sendingMediaInfo.uri);
        if (z && sendingMediaInfo.canDeleteAfter) {
            new File(sendingMediaInfo.path).delete();
        }
        mediaSendPrepareWorker.sync.countDown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareSendingMedia$90(MessageObject messageObject, AccountInstance accountInstance, TLRPC.TL_document tL_document, String str, HashMap hashMap, SendingMediaInfo sendingMediaInfo, String str2, long j, MessageObject messageObject2, MessageObject messageObject3, boolean z, int i, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, String str3, int i2, boolean z2, long j2, boolean z3) {
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, null, null, tL_document, str, hashMap, false, sendingMediaInfo.hasMediaSpoilers, str2);
            return;
        }
        SendMessageParams of = SendMessageParams.of(tL_document, null, str, j, messageObject2, messageObject3, sendingMediaInfo.caption, sendingMediaInfo.entities, null, hashMap, z, i, 0, str2, null, false, sendingMediaInfo.hasMediaSpoilers);
        of.replyToStoryItem = storyItem;
        of.replyQuote = replyQuote;
        of.quick_reply_shortcut = str3;
        of.quick_reply_shortcut_id = i2;
        if (z2) {
            of.effect_id = j2;
        }
        of.invert_media = z3;
        accountInstance.getSendMessagesHelper().sendMessage(of);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareSendingMedia$91(MessageObject messageObject, AccountInstance accountInstance, TLRPC.TL_photo tL_photo, boolean z, SendingMediaInfo sendingMediaInfo, HashMap hashMap, String str, long j, MessageObject messageObject2, MessageObject messageObject3, boolean z2, int i, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, int i2, String str2, long j2, boolean z3) {
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, tL_photo, null, null, z ? sendingMediaInfo.searchImage.imageUrl : null, hashMap, false, sendingMediaInfo.hasMediaSpoilers, str);
            return;
        }
        SendMessageParams of = SendMessageParams.of(tL_photo, z ? sendingMediaInfo.searchImage.imageUrl : null, j, messageObject2, messageObject3, sendingMediaInfo.caption, sendingMediaInfo.entities, null, hashMap, z2, i, sendingMediaInfo.ttl, str, false, sendingMediaInfo.hasMediaSpoilers);
        of.replyToStoryItem = storyItem;
        of.replyQuote = replyQuote;
        of.quick_reply_shortcut_id = i2;
        of.quick_reply_shortcut = str2;
        of.effect_id = j2;
        of.invert_media = z3;
        accountInstance.getSendMessagesHelper().sendMessage(of);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareSendingMedia$92(Bitmap bitmap, String str, MessageObject messageObject, AccountInstance accountInstance, VideoEditedInfo videoEditedInfo, TLRPC.TL_document tL_document, String str2, HashMap hashMap, SendingMediaInfo sendingMediaInfo, String str3, long j, MessageObject messageObject2, MessageObject messageObject3, boolean z, int i, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, String str4, int i2, long j2, boolean z2) {
        if (bitmap != null && str != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmap), str, false);
        }
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, null, videoEditedInfo, tL_document, str2, hashMap, false, sendingMediaInfo.hasMediaSpoilers, str3);
            return;
        }
        SendMessageParams of = SendMessageParams.of(tL_document, videoEditedInfo, str2, j, messageObject2, messageObject3, sendingMediaInfo.caption, sendingMediaInfo.entities, null, hashMap, z, i, sendingMediaInfo.ttl, str3, null, false, sendingMediaInfo.hasMediaSpoilers);
        of.replyToStoryItem = storyItem;
        of.replyQuote = replyQuote;
        of.quick_reply_shortcut = str4;
        of.quick_reply_shortcut_id = i2;
        of.effect_id = j2;
        of.invert_media = z2;
        of.stars = sendingMediaInfo.stars;
        accountInstance.getSendMessagesHelper().sendMessage(of);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareSendingMedia$93(Bitmap[] bitmapArr, String[] strArr, MessageObject messageObject, AccountInstance accountInstance, TLRPC.TL_photo tL_photo, HashMap hashMap, SendingMediaInfo sendingMediaInfo, String str, long j, MessageObject messageObject2, MessageObject messageObject3, boolean z, int i, boolean z2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, String str2, int i2, long j2, boolean z3) {
        if (bitmapArr[0] != null && strArr[0] != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmapArr[0]), strArr[0], false);
        }
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, tL_photo, null, null, null, hashMap, false, sendingMediaInfo.hasMediaSpoilers, str);
            return;
        }
        SendMessageParams of = SendMessageParams.of(tL_photo, null, j, messageObject2, messageObject3, sendingMediaInfo.caption, sendingMediaInfo.entities, null, hashMap, z, i, sendingMediaInfo.ttl, str, z2, sendingMediaInfo.hasMediaSpoilers);
        of.replyToStoryItem = storyItem;
        of.replyQuote = replyQuote;
        of.quick_reply_shortcut = str2;
        of.quick_reply_shortcut_id = i2;
        of.effect_id = j2;
        of.invert_media = z3;
        of.stars = sendingMediaInfo.stars;
        accountInstance.getSendMessagesHelper().sendMessage(of);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00e9, code lost:
    
        if (shouldSendWebPAsSticker(null, r7.uri) != false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0740, code lost:
    
        if (r5 != null) goto L739;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:144:0x03a8 A[Catch: Exception -> 0x038b, TRY_LEAVE, TryCatch #6 {Exception -> 0x038b, blocks: (B:186:0x0383, B:142:0x0390, B:180:0x039f, B:144:0x03a8), top: B:185:0x0383 }] */
    /* JADX WARN: Removed duplicated region for block: B:150:0x03e4  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0421  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x042e  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0423  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x040e  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x03b5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:177:0x039a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:209:0x051e  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x07e7  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x0846  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x0a1a  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x0a54  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x08e5  */
    /* JADX WARN: Removed duplicated region for block: B:397:0x091a  */
    /* JADX WARN: Removed duplicated region for block: B:428:0x073b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0109  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0b1c  */
    /* JADX WARN: Removed duplicated region for block: B:512:0x0cac  */
    /* JADX WARN: Removed duplicated region for block: B:554:0x0dac  */
    /* JADX WARN: Removed duplicated region for block: B:581:0x0ee1  */
    /* JADX WARN: Removed duplicated region for block: B:585:0x0eef A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:598:0x0f46  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:601:0x0f4d  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0178  */
    /* JADX WARN: Removed duplicated region for block: B:639:0x0e1e  */
    /* JADX WARN: Removed duplicated region for block: B:641:0x0db2  */
    /* JADX WARN: Removed duplicated region for block: B:650:0x0e35  */
    /* JADX WARN: Removed duplicated region for block: B:694:0x1049  */
    /* JADX WARN: Removed duplicated region for block: B:696:0x1065  */
    /* JADX WARN: Removed duplicated region for block: B:697:0x0b1e  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01d9  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01e1  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x012e  */
    /* JADX WARN: Type inference failed for: r11v10 */
    /* JADX WARN: Type inference failed for: r11v11, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r11v34 */
    /* JADX WARN: Type inference failed for: r29v2 */
    /* JADX WARN: Type inference failed for: r29v3 */
    /* JADX WARN: Type inference failed for: r29v5 */
    /* JADX WARN: Type inference failed for: r29v6 */
    /* JADX WARN: Type inference failed for: r29v7 */
    /* JADX WARN: Type inference failed for: r5v4, types: [java.lang.String] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$prepareSendingMedia$94(ArrayList arrayList, final long j, boolean z, boolean z2, final AccountInstance accountInstance, final MessageObject messageObject, final MessageObject messageObject2, final MessageObject messageObject3, final boolean z3, final int i, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final String str, final int i2, final long j2, final boolean z4, InputContentInfoCompat inputContentInfoCompat, final boolean z5) {
        String str2;
        String str3;
        String str4;
        boolean z6;
        String str5;
        String str6;
        String str7;
        String str8;
        ArrayList arrayList2;
        int i3;
        int i4;
        HashMap hashMap;
        int i5;
        int i6;
        int i7;
        int i8;
        ArrayList arrayList3;
        boolean z7;
        int i9;
        long j3;
        MediaController.SearchImage searchImage;
        int i10;
        String str9;
        int i11;
        HashMap hashMap2;
        ArrayList arrayList4;
        boolean z8;
        String str10;
        String str11;
        String str12;
        long j4;
        String str13;
        String str14;
        String str15;
        String str16;
        String str17;
        HashMap hashMap3;
        VideoEditedInfo videoEditedInfo;
        String str18;
        String str19;
        String str20;
        int i12;
        long j5;
        int i13;
        String str21;
        String str22;
        ArrayList arrayList5;
        String str23;
        long j6;
        boolean z9;
        long j7;
        File file;
        Object obj;
        String str24;
        String str25;
        Object obj2;
        ArrayList arrayList6;
        int i14;
        TLRPC.TL_document tL_document;
        String str26;
        Object obj3;
        SendingMediaInfo sendingMediaInfo;
        TLRPC.TL_document tL_document2;
        Bitmap bitmap;
        final String str27;
        int i15;
        TLRPC.PhotoSize photoSize;
        String str28;
        String str29;
        Bitmap bitmap2;
        int i16;
        long j8;
        String str30;
        ArrayList<TLRPC.InputDocument> arrayList7;
        int size;
        int i17;
        TLRPC.PhotoSize photoSize2;
        String str31;
        String str32;
        String str33;
        TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo;
        String str34;
        int i18;
        int i19;
        TLRPC.FileLocation fileLocation;
        MediaController.SearchImage searchImage2;
        String str35;
        String str36;
        String str37;
        String str38;
        String str39;
        String str40;
        String str41;
        ?? r29;
        boolean z10;
        ArrayList arrayList8;
        HashMap hashMap4;
        String str42;
        String str43;
        String str44;
        String str45;
        String str46;
        ArrayList arrayList9;
        String str47;
        Object obj4;
        final TLRPC.TL_photo tL_photo;
        String str48;
        String str49;
        String str50;
        TLRPC.TL_photo tL_photo2;
        String str51;
        TLRPC.TL_photo tL_photo3;
        String str52;
        String str53;
        String str54;
        String str55;
        long j9;
        int i20;
        int i21;
        String str56;
        ArrayList arrayList10;
        ArrayList arrayList11;
        ArrayList arrayList12;
        String str57;
        AccountInstance accountInstance2;
        String str58;
        int i22;
        int i23;
        Uri uri;
        InputStream inputStream;
        FileOutputStream fileOutputStream;
        Uri uri2;
        HashMap hashMap5;
        String str59;
        final TLRPC.TL_photo tL_photo4;
        boolean z11;
        long j10;
        int i24;
        int i25;
        Uri uri3;
        File file2;
        TLRPC.TL_document tL_document3;
        String str60;
        Object obj5;
        String str61;
        File file3;
        int i26;
        Bitmap createVideoThumbnail;
        String str62;
        String str63;
        String str64;
        final boolean z12;
        String str65;
        String str66;
        String str67;
        String str68;
        int i27;
        int i28;
        String str69;
        String str70;
        String str71;
        boolean z13;
        String str72;
        boolean z14;
        SendingMediaInfo sendingMediaInfo2;
        1 r10;
        String str73;
        TLRPC.TL_photo tL_photo5;
        TLRPC.TL_photo tL_photo6;
        String str74;
        Object[] sentFile;
        boolean z15;
        Uri uri4;
        Uri uri5;
        ArrayList arrayList13 = arrayList;
        String str75 = "parentObject";
        String str76 = "masks";
        String str77 = "-2147483648_";
        String str78 = "x";
        String str79 = "video/mp4";
        long currentTimeMillis = System.currentTimeMillis();
        int size2 = arrayList.size();
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(j);
        String str80 = ".webp";
        String str81 = ".gif";
        String str82 = "_";
        String str83 = "webp";
        if (z || !z2) {
            str2 = "_";
            str3 = ".gif";
            str4 = ".webp";
            z6 = isEncryptedDialog;
            str5 = "-2147483648_";
            str6 = "x";
            str7 = "parentObject";
            str8 = str83;
            arrayList2 = null;
            i3 = 1;
            i4 = size2;
            hashMap = null;
        } else {
            HashMap hashMap6 = new HashMap();
            int i29 = 0;
            while (i29 < size2) {
                SendingMediaInfo sendingMediaInfo3 = (SendingMediaInfo) arrayList13.get(i29);
                if (sendingMediaInfo3.searchImage == null && !sendingMediaInfo3.isVideo && sendingMediaInfo3.videoEditedInfo == null) {
                    String str84 = sendingMediaInfo3.path;
                    int i30 = size2;
                    if (str84 != null || (uri5 = sendingMediaInfo3.uri) == null) {
                        str69 = str84;
                    } else {
                        str84 = AndroidUtilities.getPath(uri5);
                        str69 = sendingMediaInfo3.uri.toString();
                    }
                    if (str84 != null) {
                        str65 = str77;
                        if (sendingMediaInfo3.ttl <= 0) {
                            if (str84.endsWith(str81)) {
                                str70 = str81;
                                z13 = false;
                            } else {
                                z13 = str84.endsWith(str80);
                                str70 = str81;
                                if (!z13) {
                                    str71 = str80;
                                    if (!ImageLoader.shouldSendImageAsDocument(sendingMediaInfo3.path, sendingMediaInfo3.uri)) {
                                        if (str84 == null && (uri4 = sendingMediaInfo3.uri) != null && (MediaController.isGif(uri4) || (z13 = MediaController.isWebp(sendingMediaInfo3.uri)))) {
                                            if (arrayList.size() <= 1) {
                                                if (z13) {
                                                }
                                                if (TextUtils.isEmpty(sendingMediaInfo3.caption)) {
                                                    str62 = str82;
                                                    z12 = isEncryptedDialog;
                                                    str66 = str78;
                                                    str68 = str83;
                                                    str63 = str70;
                                                    str64 = str71;
                                                    i27 = 1;
                                                    str67 = str75;
                                                    i28 = i30;
                                                }
                                            }
                                            z15 = true;
                                            sendingMediaInfo3.forceImage = z15;
                                            if (str84 != null) {
                                            }
                                            if (isEncryptedDialog) {
                                            }
                                            str62 = str82;
                                            z14 = isEncryptedDialog;
                                            sendingMediaInfo2 = sendingMediaInfo3;
                                            str68 = str83;
                                            str63 = str70;
                                            str64 = str71;
                                            r10 = null;
                                            str67 = str75;
                                            i28 = i30;
                                            str73 = null;
                                            tL_photo5 = null;
                                            final MediaSendPrepareWorker mediaSendPrepareWorker = new MediaSendPrepareWorker();
                                            final SendingMediaInfo sendingMediaInfo4 = sendingMediaInfo2;
                                            hashMap6.put(sendingMediaInfo4, mediaSendPrepareWorker);
                                            if (tL_photo5 != null) {
                                            }
                                        } else {
                                            if (str84 != null) {
                                                File file4 = new File(str84);
                                                StringBuilder sb = new StringBuilder();
                                                sb.append(str69);
                                                str66 = str78;
                                                sb.append(file4.length());
                                                sb.append(str82);
                                                sb.append(file4.lastModified());
                                                str72 = sb.toString();
                                            } else {
                                                str66 = str78;
                                                str72 = null;
                                            }
                                            if (isEncryptedDialog && sendingMediaInfo3.ttl == 0) {
                                                Object[] sentFile2 = accountInstance.getMessagesStorage().getSentFile(str72, !isEncryptedDialog ? 0 : 3);
                                                if (sentFile2 != null) {
                                                    Object obj6 = sentFile2[0];
                                                    if (obj6 instanceof TLRPC.TL_photo) {
                                                        str74 = (String) sentFile2[1];
                                                        tL_photo6 = (TLRPC.TL_photo) obj6;
                                                        if (tL_photo6 == null && sendingMediaInfo3.uri != null) {
                                                            sentFile = accountInstance.getMessagesStorage().getSentFile(AndroidUtilities.getPath(sendingMediaInfo3.uri), isEncryptedDialog ? 0 : 3);
                                                            if (sentFile != null) {
                                                                Object obj7 = sentFile[0];
                                                                if (obj7 instanceof TLRPC.TL_photo) {
                                                                    tL_photo6 = (TLRPC.TL_photo) obj7;
                                                                    str74 = (String) sentFile[1];
                                                                }
                                                                TLRPC.TL_photo tL_photo7 = tL_photo6;
                                                                String str85 = str74;
                                                                str62 = str82;
                                                                str63 = str70;
                                                                str64 = str71;
                                                                z14 = isEncryptedDialog;
                                                                str67 = str75;
                                                                i28 = i30;
                                                                sendingMediaInfo2 = sendingMediaInfo3;
                                                                str68 = str83;
                                                                r10 = null;
                                                                ensureMediaThumbExists(accountInstance, isEncryptedDialog, tL_photo7, sendingMediaInfo3.path, sendingMediaInfo3.uri, 0L);
                                                                tL_photo5 = tL_photo7;
                                                                str73 = str85;
                                                            }
                                                        }
                                                        TLRPC.TL_photo tL_photo72 = tL_photo6;
                                                        String str852 = str74;
                                                        str62 = str82;
                                                        str63 = str70;
                                                        str64 = str71;
                                                        z14 = isEncryptedDialog;
                                                        str67 = str75;
                                                        i28 = i30;
                                                        sendingMediaInfo2 = sendingMediaInfo3;
                                                        str68 = str83;
                                                        r10 = null;
                                                        ensureMediaThumbExists(accountInstance, isEncryptedDialog, tL_photo72, sendingMediaInfo3.path, sendingMediaInfo3.uri, 0L);
                                                        tL_photo5 = tL_photo72;
                                                        str73 = str852;
                                                    }
                                                }
                                                tL_photo6 = null;
                                                str74 = null;
                                                if (tL_photo6 == null) {
                                                    sentFile = accountInstance.getMessagesStorage().getSentFile(AndroidUtilities.getPath(sendingMediaInfo3.uri), isEncryptedDialog ? 0 : 3);
                                                    if (sentFile != null) {
                                                    }
                                                }
                                                TLRPC.TL_photo tL_photo722 = tL_photo6;
                                                String str8522 = str74;
                                                str62 = str82;
                                                str63 = str70;
                                                str64 = str71;
                                                z14 = isEncryptedDialog;
                                                str67 = str75;
                                                i28 = i30;
                                                sendingMediaInfo2 = sendingMediaInfo3;
                                                str68 = str83;
                                                r10 = null;
                                                ensureMediaThumbExists(accountInstance, isEncryptedDialog, tL_photo722, sendingMediaInfo3.path, sendingMediaInfo3.uri, 0L);
                                                tL_photo5 = tL_photo722;
                                                str73 = str8522;
                                            } else {
                                                str62 = str82;
                                                z14 = isEncryptedDialog;
                                                sendingMediaInfo2 = sendingMediaInfo3;
                                                str68 = str83;
                                                str63 = str70;
                                                str64 = str71;
                                                r10 = null;
                                                str67 = str75;
                                                i28 = i30;
                                                str73 = null;
                                                tL_photo5 = null;
                                            }
                                            final MediaSendPrepareWorker mediaSendPrepareWorker2 = new MediaSendPrepareWorker();
                                            final SendingMediaInfo sendingMediaInfo42 = sendingMediaInfo2;
                                            hashMap6.put(sendingMediaInfo42, mediaSendPrepareWorker2);
                                            if (tL_photo5 != null) {
                                                mediaSendPrepareWorker2.parentObject = str73;
                                                mediaSendPrepareWorker2.photo = tL_photo5;
                                                z12 = z14;
                                                i27 = 1;
                                            } else {
                                                i27 = 1;
                                                mediaSendPrepareWorker2.sync = new CountDownLatch(1);
                                                z12 = z14;
                                                mediaSendThreadPool.execute(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda23
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        SendMessagesHelper.lambda$prepareSendingMedia$89(SendMessagesHelper.MediaSendPrepareWorker.this, accountInstance, sendingMediaInfo42, z12);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                    str62 = str82;
                                    z12 = isEncryptedDialog;
                                    str66 = str78;
                                    str68 = str83;
                                    str63 = str70;
                                    str64 = str71;
                                    i27 = 1;
                                    str67 = str75;
                                    i28 = i30;
                                }
                            }
                            str71 = str80;
                            if (arrayList.size() > 1 || ((z13 && !shouldSendWebPAsSticker(str84, null)) || !TextUtils.isEmpty(sendingMediaInfo3.caption))) {
                                z15 = true;
                                sendingMediaInfo3.forceImage = z15;
                                if (str84 != null) {
                                }
                                if (isEncryptedDialog) {
                                }
                                str62 = str82;
                                z14 = isEncryptedDialog;
                                sendingMediaInfo2 = sendingMediaInfo3;
                                str68 = str83;
                                str63 = str70;
                                str64 = str71;
                                r10 = null;
                                str67 = str75;
                                i28 = i30;
                                str73 = null;
                                tL_photo5 = null;
                                final MediaSendPrepareWorker mediaSendPrepareWorker22 = new MediaSendPrepareWorker();
                                final SendingMediaInfo sendingMediaInfo422 = sendingMediaInfo2;
                                hashMap6.put(sendingMediaInfo422, mediaSendPrepareWorker22);
                                if (tL_photo5 != null) {
                                }
                            }
                            str62 = str82;
                            z12 = isEncryptedDialog;
                            str66 = str78;
                            str68 = str83;
                            str63 = str70;
                            str64 = str71;
                            i27 = 1;
                            str67 = str75;
                            i28 = i30;
                        } else {
                            str70 = str81;
                            str71 = str80;
                        }
                    } else {
                        str70 = str81;
                        str71 = str80;
                        str65 = str77;
                    }
                    z13 = false;
                    if (!ImageLoader.shouldSendImageAsDocument(sendingMediaInfo3.path, sendingMediaInfo3.uri)) {
                    }
                    str62 = str82;
                    z12 = isEncryptedDialog;
                    str66 = str78;
                    str68 = str83;
                    str63 = str70;
                    str64 = str71;
                    i27 = 1;
                    str67 = str75;
                    i28 = i30;
                } else {
                    str62 = str82;
                    str63 = str81;
                    str64 = str80;
                    z12 = isEncryptedDialog;
                    str65 = str77;
                    str66 = str78;
                    str67 = str75;
                    str68 = str83;
                    i27 = 1;
                    i28 = size2;
                }
                i29 += i27;
                isEncryptedDialog = z12;
                size2 = i28;
                str78 = str66;
                str77 = str65;
                str75 = str67;
                str81 = str63;
                str82 = str62;
                str80 = str64;
                str83 = str68;
            }
            str2 = str82;
            str3 = str81;
            str4 = str80;
            z6 = isEncryptedDialog;
            str5 = str77;
            str6 = str78;
            str7 = str75;
            str8 = str83;
            arrayList2 = null;
            i3 = 1;
            i4 = size2;
            hashMap = hashMap6;
        }
        ArrayList arrayList14 = arrayList2;
        ArrayList arrayList15 = arrayList14;
        ArrayList arrayList16 = arrayList15;
        ArrayList arrayList17 = arrayList16;
        ArrayList arrayList18 = arrayList17;
        ArrayList arrayList19 = arrayList18;
        int i31 = 0;
        int i32 = 0;
        long j11 = 0;
        long j12 = 0;
        ArrayList arrayList20 = arrayList18;
        while (i32 < i4) {
            final SendingMediaInfo sendingMediaInfo5 = (SendingMediaInfo) arrayList13.get(i32);
            final boolean z16 = i31 == 0;
            if (z2 && i4 > i3) {
                if (i31 % 10 == 0) {
                    j12 = Utilities.random.nextLong();
                    j3 = j12;
                    i9 = 0;
                    searchImage = sendingMediaInfo5.searchImage;
                    i10 = i32;
                    str9 = str76;
                    ArrayList arrayList21 = arrayList14;
                    i11 = i4;
                    hashMap2 = hashMap;
                    if (searchImage == null && sendingMediaInfo5.videoEditedInfo == null) {
                        ArrayList arrayList22 = arrayList15;
                        if (searchImage.type == 1) {
                            final HashMap hashMap7 = new HashMap();
                            TLRPC.Document document = sendingMediaInfo5.searchImage.document;
                            if (document instanceof TLRPC.TL_document) {
                                tL_document3 = (TLRPC.TL_document) document;
                                file2 = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(tL_document3, true);
                            } else {
                                file2 = new File(FileLoader.getDirectory(4), Utilities.MD5(sendingMediaInfo5.searchImage.imageUrl) + "." + ImageLoader.getHttpUrlExtension(sendingMediaInfo5.searchImage.imageUrl, "jpg"));
                                tL_document3 = null;
                            }
                            if (tL_document3 == null) {
                                TLRPC.TL_document tL_document4 = new TLRPC.TL_document();
                                tL_document4.id = 0L;
                                tL_document4.file_reference = new byte[0];
                                tL_document4.date = accountInstance.getConnectionsManager().getCurrentTime();
                                TLRPC.TL_documentAttributeFilename tL_documentAttributeFilename = new TLRPC.TL_documentAttributeFilename();
                                tL_documentAttributeFilename.file_name = "animation.gif";
                                tL_document4.attributes.add(tL_documentAttributeFilename);
                                tL_document4.size = sendingMediaInfo5.searchImage.size;
                                tL_document4.dc_id = 0;
                                if (z || !file2.toString().endsWith("mp4")) {
                                    tL_document4.mime_type = "image/gif";
                                } else {
                                    tL_document4.mime_type = str79;
                                    tL_document4.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                                }
                                if (file2.exists()) {
                                    file3 = file2;
                                } else {
                                    file2 = null;
                                    file3 = null;
                                }
                                if (file2 == null) {
                                    file2 = new File(FileLoader.getDirectory(4), Utilities.MD5(sendingMediaInfo5.searchImage.thumbUrl) + "." + ImageLoader.getHttpUrlExtension(sendingMediaInfo5.searchImage.thumbUrl, "jpg"));
                                    if (!file2.exists()) {
                                        file2 = null;
                                    }
                                }
                                if (file2 != null) {
                                    if (!z6) {
                                        try {
                                            if (sendingMediaInfo5.ttl == 0) {
                                                i26 = 320;
                                                if (file2.getAbsolutePath().endsWith("mp4")) {
                                                    float f = i26;
                                                    obj5 = null;
                                                    createVideoThumbnail = ImageLoader.loadBitmap(file2.getAbsolutePath(), null, f, f, true);
                                                } else {
                                                    try {
                                                        createVideoThumbnail = createVideoThumbnail(file2.getAbsolutePath(), 1);
                                                        obj5 = null;
                                                    } catch (Exception e) {
                                                        e = e;
                                                        obj5 = null;
                                                        FileLog.e(e);
                                                        if (tL_document4.thumbs.isEmpty()) {
                                                        }
                                                        tL_document3 = tL_document4;
                                                        file2 = file3;
                                                        if (file2 != null) {
                                                        }
                                                        str61 = sendingMediaInfo5.searchImage.imageUrl;
                                                        if (str61 != null) {
                                                        }
                                                        final String str86 = null;
                                                        ArrayList arrayList23 = arrayList17;
                                                        final TLRPC.TL_document tL_document5 = tL_document3;
                                                        z8 = z6;
                                                        str22 = str7;
                                                        hashMap5 = hashMap2;
                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda24
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                SendMessagesHelper.lambda$prepareSendingMedia$90(MessageObject.this, accountInstance, tL_document5, r7, hashMap7, sendingMediaInfo5, str86, j, messageObject2, messageObject3, z3, i, storyItem, replyQuote, str, i2, z16, j2, z4);
                                                            }
                                                        });
                                                        i31 = i9;
                                                        str19 = str2;
                                                        str15 = str4;
                                                        str16 = str8;
                                                        j5 = j3;
                                                        arrayList15 = arrayList22;
                                                        arrayList16 = arrayList16;
                                                        arrayList17 = arrayList23;
                                                        arrayList14 = arrayList21;
                                                        str18 = str79;
                                                        str13 = str60;
                                                        str20 = str5;
                                                        i13 = i10;
                                                        str21 = str9;
                                                        i12 = i11;
                                                        str17 = str3;
                                                        hashMap3 = hashMap5;
                                                        i32 = i13 + 1;
                                                        arrayList13 = arrayList;
                                                        hashMap = hashMap3;
                                                        str6 = str13;
                                                        str3 = str17;
                                                        z6 = z8;
                                                        str76 = str21;
                                                        str79 = str18;
                                                        j11 = j5;
                                                        str5 = str20;
                                                        str7 = str22;
                                                        i4 = i12;
                                                        i3 = 1;
                                                        str8 = str16;
                                                        str4 = str15;
                                                        str2 = str19;
                                                        arrayList20 = arrayList20;
                                                    }
                                                }
                                                if (createVideoThumbnail != null) {
                                                    try {
                                                        float f2 = i26;
                                                        TLRPC.PhotoSize scaleAndSaveImage = ImageLoader.scaleAndSaveImage(createVideoThumbnail, f2, f2, i26 > 90 ? 80 : 55, z6);
                                                        if (scaleAndSaveImage != null) {
                                                            tL_document4.thumbs.add(scaleAndSaveImage);
                                                            tL_document4.flags |= 1;
                                                        }
                                                        createVideoThumbnail.recycle();
                                                    } catch (Exception e2) {
                                                        e = e2;
                                                        FileLog.e(e);
                                                        if (tL_document4.thumbs.isEmpty()) {
                                                        }
                                                        tL_document3 = tL_document4;
                                                        file2 = file3;
                                                        if (file2 != null) {
                                                        }
                                                        str61 = sendingMediaInfo5.searchImage.imageUrl;
                                                        if (str61 != null) {
                                                        }
                                                        final String str862 = null;
                                                        ArrayList arrayList232 = arrayList17;
                                                        final TLRPC.TL_document tL_document52 = tL_document3;
                                                        z8 = z6;
                                                        str22 = str7;
                                                        hashMap5 = hashMap2;
                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda24
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                SendMessagesHelper.lambda$prepareSendingMedia$90(MessageObject.this, accountInstance, tL_document52, r7, hashMap7, sendingMediaInfo5, str862, j, messageObject2, messageObject3, z3, i, storyItem, replyQuote, str, i2, z16, j2, z4);
                                                            }
                                                        });
                                                        i31 = i9;
                                                        str19 = str2;
                                                        str15 = str4;
                                                        str16 = str8;
                                                        j5 = j3;
                                                        arrayList15 = arrayList22;
                                                        arrayList16 = arrayList16;
                                                        arrayList17 = arrayList232;
                                                        arrayList14 = arrayList21;
                                                        str18 = str79;
                                                        str13 = str60;
                                                        str20 = str5;
                                                        i13 = i10;
                                                        str21 = str9;
                                                        i12 = i11;
                                                        str17 = str3;
                                                        hashMap3 = hashMap5;
                                                        i32 = i13 + 1;
                                                        arrayList13 = arrayList;
                                                        hashMap = hashMap3;
                                                        str6 = str13;
                                                        str3 = str17;
                                                        z6 = z8;
                                                        str76 = str21;
                                                        str79 = str18;
                                                        j11 = j5;
                                                        str5 = str20;
                                                        str7 = str22;
                                                        i4 = i12;
                                                        i3 = 1;
                                                        str8 = str16;
                                                        str4 = str15;
                                                        str2 = str19;
                                                        arrayList20 = arrayList20;
                                                    }
                                                }
                                            }
                                        } catch (Exception e3) {
                                            e = e3;
                                            obj5 = null;
                                            FileLog.e(e);
                                            if (tL_document4.thumbs.isEmpty()) {
                                            }
                                            tL_document3 = tL_document4;
                                            file2 = file3;
                                            if (file2 != null) {
                                            }
                                            str61 = sendingMediaInfo5.searchImage.imageUrl;
                                            if (str61 != null) {
                                            }
                                            final String str8622 = null;
                                            ArrayList arrayList2322 = arrayList17;
                                            final TLRPC.TL_document tL_document522 = tL_document3;
                                            z8 = z6;
                                            str22 = str7;
                                            hashMap5 = hashMap2;
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda24
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    SendMessagesHelper.lambda$prepareSendingMedia$90(MessageObject.this, accountInstance, tL_document522, r7, hashMap7, sendingMediaInfo5, str8622, j, messageObject2, messageObject3, z3, i, storyItem, replyQuote, str, i2, z16, j2, z4);
                                                }
                                            });
                                            i31 = i9;
                                            str19 = str2;
                                            str15 = str4;
                                            str16 = str8;
                                            j5 = j3;
                                            arrayList15 = arrayList22;
                                            arrayList16 = arrayList16;
                                            arrayList17 = arrayList2322;
                                            arrayList14 = arrayList21;
                                            str18 = str79;
                                            str13 = str60;
                                            str20 = str5;
                                            i13 = i10;
                                            str21 = str9;
                                            i12 = i11;
                                            str17 = str3;
                                            hashMap3 = hashMap5;
                                            i32 = i13 + 1;
                                            arrayList13 = arrayList;
                                            hashMap = hashMap3;
                                            str6 = str13;
                                            str3 = str17;
                                            z6 = z8;
                                            str76 = str21;
                                            str79 = str18;
                                            j11 = j5;
                                            str5 = str20;
                                            str7 = str22;
                                            i4 = i12;
                                            i3 = 1;
                                            str8 = str16;
                                            str4 = str15;
                                            str2 = str19;
                                            arrayList20 = arrayList20;
                                        }
                                    }
                                    i26 = 90;
                                    if (file2.getAbsolutePath().endsWith("mp4")) {
                                    }
                                    if (createVideoThumbnail != null) {
                                    }
                                } else {
                                    obj5 = null;
                                }
                                if (tL_document4.thumbs.isEmpty()) {
                                    str60 = str6;
                                } else {
                                    TLRPC.TL_photoSize tL_photoSize = new TLRPC.TL_photoSize();
                                    MediaController.SearchImage searchImage3 = sendingMediaInfo5.searchImage;
                                    tL_photoSize.w = searchImage3.width;
                                    tL_photoSize.h = searchImage3.height;
                                    tL_photoSize.size = 0;
                                    tL_photoSize.location = new TLRPC.TL_fileLocationUnavailable();
                                    str60 = str6;
                                    tL_photoSize.type = str60;
                                    tL_document4.thumbs.add(tL_photoSize);
                                    tL_document4.flags |= 1;
                                }
                                tL_document3 = tL_document4;
                                file2 = file3;
                            } else {
                                str60 = str6;
                                obj5 = null;
                            }
                            final String file5 = file2 != null ? sendingMediaInfo5.searchImage.imageUrl : file2.toString();
                            str61 = sendingMediaInfo5.searchImage.imageUrl;
                            if (str61 != null) {
                                hashMap7.put("originalPath", str61);
                            }
                            final String str86222 = null;
                            ArrayList arrayList23222 = arrayList17;
                            final TLRPC.TL_document tL_document5222 = tL_document3;
                            z8 = z6;
                            str22 = str7;
                            hashMap5 = hashMap2;
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda24
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.lambda$prepareSendingMedia$90(MessageObject.this, accountInstance, tL_document5222, file5, hashMap7, sendingMediaInfo5, str86222, j, messageObject2, messageObject3, z3, i, storyItem, replyQuote, str, i2, z16, j2, z4);
                                }
                            });
                            i31 = i9;
                            str19 = str2;
                            str15 = str4;
                            str16 = str8;
                            j5 = j3;
                            arrayList15 = arrayList22;
                            arrayList16 = arrayList16;
                            arrayList17 = arrayList23222;
                            arrayList14 = arrayList21;
                            str18 = str79;
                            str13 = str60;
                            str20 = str5;
                            i13 = i10;
                            str21 = str9;
                            i12 = i11;
                        } else {
                            ArrayList arrayList24 = arrayList16;
                            ArrayList arrayList25 = arrayList17;
                            z8 = z6;
                            String str87 = str79;
                            String str88 = str6;
                            String str89 = str5;
                            str22 = str7;
                            hashMap5 = hashMap2;
                            TLRPC.Photo photo = searchImage.photo;
                            TLRPC.TL_photo tL_photo8 = photo instanceof TLRPC.TL_photo ? (TLRPC.TL_photo) photo : null;
                            if (tL_photo8 == null) {
                                File file6 = new File(FileLoader.getDirectory(4), Utilities.MD5(sendingMediaInfo5.searchImage.imageUrl) + "." + ImageLoader.getHttpUrlExtension(sendingMediaInfo5.searchImage.imageUrl, "jpg"));
                                if (!file6.exists() || file6.length() == 0) {
                                    uri3 = null;
                                } else {
                                    uri3 = null;
                                    tL_photo8 = accountInstance.getSendMessagesHelper().generatePhotoSizes(file6.toString(), null);
                                    if (tL_photo8 != null) {
                                        z11 = false;
                                        if (tL_photo8 == null) {
                                            File file7 = new File(FileLoader.getDirectory(4), Utilities.MD5(sendingMediaInfo5.searchImage.thumbUrl) + "." + ImageLoader.getHttpUrlExtension(sendingMediaInfo5.searchImage.thumbUrl, "jpg"));
                                            if (file7.exists()) {
                                                tL_photo8 = accountInstance.getSendMessagesHelper().generatePhotoSizes(file7.toString(), uri3);
                                            }
                                            if (tL_photo8 == null) {
                                                tL_photo8 = new TLRPC.TL_photo();
                                                tL_photo8.date = accountInstance.getConnectionsManager().getCurrentTime();
                                                tL_photo8.file_reference = new byte[0];
                                                TLRPC.TL_photoSize tL_photoSize2 = new TLRPC.TL_photoSize();
                                                MediaController.SearchImage searchImage4 = sendingMediaInfo5.searchImage;
                                                tL_photoSize2.w = searchImage4.width;
                                                tL_photoSize2.h = searchImage4.height;
                                                tL_photoSize2.size = 0;
                                                tL_photoSize2.location = new TLRPC.TL_fileLocationUnavailable();
                                                str59 = str88;
                                                tL_photoSize2.type = str59;
                                                tL_photo8.sizes.add(tL_photoSize2);
                                                tL_photo4 = tL_photo8;
                                            }
                                        }
                                        str59 = str88;
                                        tL_photo4 = tL_photo8;
                                    }
                                }
                                z11 = true;
                                if (tL_photo8 == null) {
                                }
                                str59 = str88;
                                tL_photo4 = tL_photo8;
                            } else {
                                str59 = str88;
                                tL_photo4 = tL_photo8;
                                z11 = true;
                            }
                            final HashMap hashMap8 = new HashMap();
                            String str90 = sendingMediaInfo5.searchImage.imageUrl;
                            if (str90 != null) {
                                hashMap8.put("originalPath", str90);
                            }
                            if (z2) {
                                int i33 = i9 + 1;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("");
                                j10 = j3;
                                sb2.append(j10);
                                hashMap8.put("groupId", sb2.toString());
                                if (i33 != 10) {
                                    i25 = i11;
                                    i24 = i10;
                                    if (i24 != i25 - 1) {
                                        i9 = i33;
                                    }
                                } else {
                                    i24 = i10;
                                    i25 = i11;
                                }
                                hashMap8.put("final", "1");
                                i9 = i33;
                                j12 = 0;
                            } else {
                                j10 = j3;
                                i24 = i10;
                                i25 = i11;
                            }
                            final String str91 = null;
                            int i34 = i24;
                            str13 = str59;
                            final boolean z17 = z11;
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda25
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.lambda$prepareSendingMedia$91(MessageObject.this, accountInstance, tL_photo4, z17, sendingMediaInfo5, hashMap8, str91, j, messageObject2, messageObject3, z3, i, storyItem, replyQuote, i2, str, j2, z4);
                                }
                            });
                            i31 = i9;
                            str19 = str2;
                            str15 = str4;
                            str16 = str8;
                            arrayList15 = arrayList22;
                            arrayList16 = arrayList24;
                            arrayList17 = arrayList25;
                            arrayList14 = arrayList21;
                            str18 = str87;
                            str20 = str89;
                            str21 = str9;
                            j5 = j10;
                            i12 = i25;
                            i13 = i34;
                        }
                        str17 = str3;
                        hashMap3 = hashMap5;
                    } else {
                        ArrayList arrayList26 = arrayList15;
                        ArrayList arrayList27 = arrayList16;
                        arrayList4 = arrayList17;
                        z8 = z6;
                        str10 = str79;
                        str11 = str5;
                        str12 = str7;
                        j4 = j3;
                        str13 = str6;
                        if (sendingMediaInfo5.isVideo && sendingMediaInfo5.videoEditedInfo == null) {
                            String str92 = sendingMediaInfo5.path;
                            if (str92 != null || (uri2 = sendingMediaInfo5.uri) == null) {
                                str35 = str92;
                                str36 = str35;
                            } else {
                                String path = (Build.VERSION.SDK_INT < 30 || !"content".equals(uri2.getScheme())) ? AndroidUtilities.getPath(sendingMediaInfo5.uri) : null;
                                str35 = sendingMediaInfo5.uri.toString();
                                str36 = path;
                            }
                            if (inputContentInfoCompat == null || sendingMediaInfo5.uri == null || !inputContentInfoCompat.getDescription().hasMimeType("image/png")) {
                                str37 = str4;
                                str38 = str11;
                            } else {
                                try {
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    inputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(sendingMediaInfo5.uri);
                                    try {
                                        Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
                                        StringBuilder sb3 = new StringBuilder();
                                        str38 = str11;
                                        try {
                                            sb3.append(str38);
                                            sb3.append(SharedConfig.getLastLocalId());
                                            str37 = str4;
                                            try {
                                                sb3.append(str37);
                                                File file8 = new File(FileLoader.getDirectory(4), sb3.toString());
                                                fileOutputStream = new FileOutputStream(file8);
                                                try {
                                                    decodeStream.compress(Bitmap.CompressFormat.WEBP, 100, fileOutputStream);
                                                    SharedConfig.saveConfig();
                                                    sendingMediaInfo5.uri = Uri.fromFile(file8);
                                                    if (inputStream != null) {
                                                        try {
                                                            inputStream.close();
                                                        } catch (Exception unused) {
                                                        }
                                                    }
                                                } catch (Throwable th) {
                                                    th = th;
                                                    try {
                                                        FileLog.e(th);
                                                        if (inputStream != null) {
                                                            try {
                                                                inputStream.close();
                                                            } catch (Exception unused2) {
                                                            }
                                                        }
                                                    } finally {
                                                    }
                                                }
                                            } catch (Throwable th2) {
                                                th = th2;
                                                fileOutputStream = null;
                                                FileLog.e(th);
                                                if (inputStream != null) {
                                                }
                                            }
                                        } catch (Throwable th3) {
                                            th = th3;
                                            str37 = str4;
                                        }
                                    } catch (Throwable th4) {
                                        th = th4;
                                        str37 = str4;
                                        str38 = str11;
                                    }
                                } catch (Throwable th5) {
                                    th = th5;
                                    str37 = str4;
                                    str38 = str11;
                                    inputStream = null;
                                }
                                try {
                                    fileOutputStream.close();
                                } catch (Exception unused3) {
                                }
                            }
                            if (z || ImageLoader.shouldSendImageAsDocument(sendingMediaInfo5.path, sendingMediaInfo5.uri)) {
                                str39 = str3;
                                str40 = str8;
                                if (str36 != null) {
                                    r29 = FileLoader.getFileExtension(new File(str36));
                                    str41 = str36;
                                } else {
                                    str41 = str36;
                                    r29 = "";
                                }
                            } else {
                                String str93 = "gif";
                                if (sendingMediaInfo5.forceImage || str36 == null) {
                                    str39 = str3;
                                } else {
                                    str39 = str3;
                                    if ((str36.endsWith(str39) || str36.endsWith(str37)) && sendingMediaInfo5.ttl <= 0) {
                                        if (!str36.endsWith(str39)) {
                                            str93 = str8;
                                        }
                                        str41 = str36;
                                        r29 = str93;
                                        str40 = str8;
                                    }
                                }
                                if (!sendingMediaInfo5.forceImage && str36 == null && (uri = sendingMediaInfo5.uri) != null) {
                                    if (MediaController.isGif(uri)) {
                                        str35 = sendingMediaInfo5.uri.toString();
                                        str36 = MediaController.copyFileToCache(sendingMediaInfo5.uri, "gif");
                                        str41 = str36;
                                        r29 = str93;
                                        str40 = str8;
                                    } else if (MediaController.isWebp(sendingMediaInfo5.uri)) {
                                        str35 = sendingMediaInfo5.uri.toString();
                                        str40 = str8;
                                        str41 = MediaController.copyFileToCache(sendingMediaInfo5.uri, str40);
                                        r29 = str40;
                                    }
                                }
                                str40 = str8;
                                str41 = str36;
                                z10 = false;
                                arrayList20 = arrayList20;
                                arrayList17 = arrayList4;
                                if (z10) {
                                    if (str41 != null) {
                                        File file9 = new File(str41);
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append(str35);
                                        arrayList8 = arrayList17;
                                        sb4.append(file9.length());
                                        sb4.append(str2);
                                        sb4.append(file9.lastModified());
                                        str42 = sb4.toString();
                                        hashMap4 = hashMap2;
                                    } else {
                                        arrayList8 = arrayList17;
                                        hashMap4 = hashMap2;
                                        str42 = null;
                                    }
                                    if (hashMap4 != null) {
                                        MediaSendPrepareWorker mediaSendPrepareWorker3 = (MediaSendPrepareWorker) hashMap4.get(sendingMediaInfo5);
                                        TLRPC.TL_photo tL_photo9 = mediaSendPrepareWorker3.photo;
                                        String str94 = mediaSendPrepareWorker3.parentObject;
                                        if (tL_photo9 == null) {
                                            try {
                                                mediaSendPrepareWorker3.sync.await();
                                            } catch (Exception e4) {
                                                FileLog.e(e4);
                                            }
                                            hashMap3 = hashMap4;
                                            str43 = str42;
                                            str44 = str38;
                                            str45 = str39;
                                            str46 = str2;
                                            arrayList9 = arrayList8;
                                            str47 = str41;
                                            obj4 = "1";
                                            str15 = str37;
                                            tL_photo = mediaSendPrepareWorker3.photo;
                                            str49 = mediaSendPrepareWorker3.parentObject;
                                        } else {
                                            hashMap3 = hashMap4;
                                            str43 = str42;
                                            str44 = str38;
                                            str45 = str39;
                                            str46 = str2;
                                            arrayList9 = arrayList8;
                                            str47 = str41;
                                            obj4 = "1";
                                            str15 = str37;
                                            tL_photo = tL_photo9;
                                            str49 = str94;
                                        }
                                    } else {
                                        if (z8 || sendingMediaInfo5.ttl != 0) {
                                            hashMap3 = hashMap4;
                                            str43 = str42;
                                            str44 = str38;
                                            str45 = str39;
                                            str46 = str2;
                                            arrayList9 = arrayList8;
                                            str47 = str41;
                                            obj4 = "1";
                                            str15 = str37;
                                            tL_photo = null;
                                            str48 = null;
                                        } else {
                                            Object[] sentFile3 = accountInstance.getMessagesStorage().getSentFile(str42, !z8 ? 0 : 3);
                                            if (sentFile3 != null) {
                                                Object obj8 = sentFile3[0];
                                                if (obj8 instanceof TLRPC.TL_photo) {
                                                    tL_photo2 = (TLRPC.TL_photo) obj8;
                                                    str50 = (String) sentFile3[1];
                                                    if (tL_photo2 == null) {
                                                        str51 = str50;
                                                        tL_photo3 = tL_photo2;
                                                    } else if (sendingMediaInfo5.uri != null) {
                                                        str51 = str50;
                                                        tL_photo3 = tL_photo2;
                                                        Object[] sentFile4 = accountInstance.getMessagesStorage().getSentFile(AndroidUtilities.getPath(sendingMediaInfo5.uri), !z8 ? 0 : 3);
                                                        if (sentFile4 != null) {
                                                            Object obj9 = sentFile4[0];
                                                            if (obj9 instanceof TLRPC.TL_photo) {
                                                                str52 = (String) sentFile4[1];
                                                                tL_photo3 = (TLRPC.TL_photo) obj9;
                                                                str48 = str52;
                                                                hashMap3 = hashMap4;
                                                                str43 = str42;
                                                                str46 = str2;
                                                                arrayList9 = arrayList8;
                                                                str44 = str38;
                                                                str47 = str41;
                                                                str15 = str37;
                                                                str45 = str39;
                                                                obj4 = "1";
                                                                ensureMediaThumbExists(accountInstance, z8, tL_photo3, sendingMediaInfo5.path, sendingMediaInfo5.uri, 0L);
                                                                tL_photo = tL_photo3;
                                                            }
                                                        }
                                                    } else {
                                                        str51 = str50;
                                                        tL_photo3 = tL_photo2;
                                                    }
                                                    str52 = str51;
                                                    str48 = str52;
                                                    hashMap3 = hashMap4;
                                                    str43 = str42;
                                                    str46 = str2;
                                                    arrayList9 = arrayList8;
                                                    str44 = str38;
                                                    str47 = str41;
                                                    str15 = str37;
                                                    str45 = str39;
                                                    obj4 = "1";
                                                    ensureMediaThumbExists(accountInstance, z8, tL_photo3, sendingMediaInfo5.path, sendingMediaInfo5.uri, 0L);
                                                    tL_photo = tL_photo3;
                                                }
                                            }
                                            str50 = null;
                                            tL_photo2 = null;
                                            if (tL_photo2 == null) {
                                            }
                                            str52 = str51;
                                            str48 = str52;
                                            hashMap3 = hashMap4;
                                            str43 = str42;
                                            str46 = str2;
                                            arrayList9 = arrayList8;
                                            str44 = str38;
                                            str47 = str41;
                                            str15 = str37;
                                            str45 = str39;
                                            obj4 = "1";
                                            ensureMediaThumbExists(accountInstance, z8, tL_photo3, sendingMediaInfo5.path, sendingMediaInfo5.uri, 0L);
                                            tL_photo = tL_photo3;
                                        }
                                        if (tL_photo == null) {
                                            TLRPC.TL_photo generatePhotoSizes = accountInstance.getSendMessagesHelper().generatePhotoSizes(sendingMediaInfo5.path, sendingMediaInfo5.uri);
                                            if (z8 && sendingMediaInfo5.canDeleteAfter) {
                                                new File(sendingMediaInfo5.path).delete();
                                            }
                                            tL_photo = generatePhotoSizes;
                                        }
                                        str49 = str48;
                                    }
                                    if (tL_photo != null) {
                                        final HashMap hashMap9 = new HashMap();
                                        final Bitmap[] bitmapArr = new Bitmap[1];
                                        final String[] strArr = new String[1];
                                        ArrayList<TLRPC.InputDocument> arrayList28 = sendingMediaInfo5.masks;
                                        boolean z18 = (arrayList28 == null || arrayList28.isEmpty()) ? false : true;
                                        tL_photo.has_stickers = z18;
                                        if (z18) {
                                            SerializedData serializedData = new SerializedData((sendingMediaInfo5.masks.size() * 20) + 4);
                                            serializedData.writeInt32(sendingMediaInfo5.masks.size());
                                            for (int i35 = 0; i35 < sendingMediaInfo5.masks.size(); i35++) {
                                                sendingMediaInfo5.masks.get(i35).serializeToStream(serializedData);
                                            }
                                            str57 = str9;
                                            hashMap9.put(str57, Utilities.bytesToHex(serializedData.toByteArray()));
                                            serializedData.cleanup();
                                        } else {
                                            str57 = str9;
                                        }
                                        String str95 = str43;
                                        if (str95 != null) {
                                            hashMap9.put("originalPath", str95);
                                        }
                                        if (str49 != null) {
                                            hashMap9.put(str12, str49);
                                        }
                                        if (z2) {
                                            try {
                                                if (arrayList.size() == 1) {
                                                }
                                                accountInstance2 = accountInstance;
                                            } catch (Exception e5) {
                                                e = e5;
                                                accountInstance2 = accountInstance;
                                                FileLog.e(e);
                                                if (z2) {
                                                }
                                                final String str96 = str49;
                                                str53 = str44;
                                                i20 = i22;
                                                j9 = j4;
                                                i21 = i23;
                                                str16 = str58;
                                                str54 = str57;
                                                str17 = str45;
                                                str55 = str12;
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda26
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        SendMessagesHelper.lambda$prepareSendingMedia$93(bitmapArr, strArr, messageObject, accountInstance, tL_photo, hashMap9, sendingMediaInfo5, str96, j, messageObject2, messageObject3, z3, i, z5, storyItem, replyQuote, str, i2, j2, z4);
                                                    }
                                                });
                                                i31 = i9;
                                                arrayList15 = arrayList26;
                                                arrayList16 = arrayList27;
                                                arrayList14 = arrayList21;
                                                str18 = str10;
                                                str19 = str46;
                                                arrayList17 = arrayList9;
                                                str20 = str53;
                                                i12 = i20;
                                                j5 = j9;
                                                i13 = i21;
                                                str21 = str54;
                                                str22 = str55;
                                                i32 = i13 + 1;
                                                arrayList13 = arrayList;
                                                hashMap = hashMap3;
                                                str6 = str13;
                                                str3 = str17;
                                                z6 = z8;
                                                str76 = str21;
                                                str79 = str18;
                                                j11 = j5;
                                                str5 = str20;
                                                str7 = str22;
                                                i4 = i12;
                                                i3 = 1;
                                                str8 = str16;
                                                str4 = str15;
                                                str2 = str19;
                                                arrayList20 = arrayList20;
                                            }
                                            if (z2) {
                                                int i36 = i9 + 1;
                                                StringBuilder sb5 = new StringBuilder();
                                                sb5.append("");
                                                str58 = str40;
                                                sb5.append(j4);
                                                hashMap9.put("groupId", sb5.toString());
                                                if (i36 != 10) {
                                                    i22 = i11;
                                                    j4 = j4;
                                                    i23 = i10;
                                                    if (i23 != i22 - 1) {
                                                        i9 = i36;
                                                    }
                                                } else {
                                                    j4 = j4;
                                                    i22 = i11;
                                                    i23 = i10;
                                                }
                                                hashMap9.put("final", obj4);
                                                i9 = i36;
                                                j12 = 0;
                                            } else {
                                                str58 = str40;
                                                i22 = i11;
                                                i23 = i10;
                                            }
                                            final String str962 = str49;
                                            str53 = str44;
                                            i20 = i22;
                                            j9 = j4;
                                            i21 = i23;
                                            str16 = str58;
                                            str54 = str57;
                                            str17 = str45;
                                            str55 = str12;
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda26
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    SendMessagesHelper.lambda$prepareSendingMedia$93(bitmapArr, strArr, messageObject, accountInstance, tL_photo, hashMap9, sendingMediaInfo5, str962, j, messageObject2, messageObject3, z3, i, z5, storyItem, replyQuote, str, i2, j2, z4);
                                                }
                                            });
                                            i31 = i9;
                                            arrayList15 = arrayList26;
                                            arrayList16 = arrayList27;
                                            arrayList14 = arrayList21;
                                            str18 = str10;
                                            str19 = str46;
                                            arrayList17 = arrayList9;
                                        }
                                        TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_photo.sizes, AndroidUtilities.getPhotoSize());
                                        if (closestPhotoSizeWithSize != null) {
                                            accountInstance2 = accountInstance;
                                            try {
                                                strArr[0] = getKeyForPhotoSize(accountInstance2, closestPhotoSizeWithSize, bitmapArr, false, false);
                                            } catch (Exception e6) {
                                                e = e6;
                                                FileLog.e(e);
                                                if (z2) {
                                                }
                                                final String str9622 = str49;
                                                str53 = str44;
                                                i20 = i22;
                                                j9 = j4;
                                                i21 = i23;
                                                str16 = str58;
                                                str54 = str57;
                                                str17 = str45;
                                                str55 = str12;
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda26
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        SendMessagesHelper.lambda$prepareSendingMedia$93(bitmapArr, strArr, messageObject, accountInstance, tL_photo, hashMap9, sendingMediaInfo5, str9622, j, messageObject2, messageObject3, z3, i, z5, storyItem, replyQuote, str, i2, j2, z4);
                                                    }
                                                });
                                                i31 = i9;
                                                arrayList15 = arrayList26;
                                                arrayList16 = arrayList27;
                                                arrayList14 = arrayList21;
                                                str18 = str10;
                                                str19 = str46;
                                                arrayList17 = arrayList9;
                                                str20 = str53;
                                                i12 = i20;
                                                j5 = j9;
                                                i13 = i21;
                                                str21 = str54;
                                                str22 = str55;
                                                i32 = i13 + 1;
                                                arrayList13 = arrayList;
                                                hashMap = hashMap3;
                                                str6 = str13;
                                                str3 = str17;
                                                z6 = z8;
                                                str76 = str21;
                                                str79 = str18;
                                                j11 = j5;
                                                str5 = str20;
                                                str7 = str22;
                                                i4 = i12;
                                                i3 = 1;
                                                str8 = str16;
                                                str4 = str15;
                                                str2 = str19;
                                                arrayList20 = arrayList20;
                                            }
                                            if (z2) {
                                            }
                                            final String str96222 = str49;
                                            str53 = str44;
                                            i20 = i22;
                                            j9 = j4;
                                            i21 = i23;
                                            str16 = str58;
                                            str54 = str57;
                                            str17 = str45;
                                            str55 = str12;
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda26
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    SendMessagesHelper.lambda$prepareSendingMedia$93(bitmapArr, strArr, messageObject, accountInstance, tL_photo, hashMap9, sendingMediaInfo5, str96222, j, messageObject2, messageObject3, z3, i, z5, storyItem, replyQuote, str, i2, j2, z4);
                                                }
                                            });
                                            i31 = i9;
                                            arrayList15 = arrayList26;
                                            arrayList16 = arrayList27;
                                            arrayList14 = arrayList21;
                                            str18 = str10;
                                            str19 = str46;
                                            arrayList17 = arrayList9;
                                        }
                                        accountInstance2 = accountInstance;
                                        if (z2) {
                                        }
                                        final String str962222 = str49;
                                        str53 = str44;
                                        i20 = i22;
                                        j9 = j4;
                                        i21 = i23;
                                        str16 = str58;
                                        str54 = str57;
                                        str17 = str45;
                                        str55 = str12;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda26
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                SendMessagesHelper.lambda$prepareSendingMedia$93(bitmapArr, strArr, messageObject, accountInstance, tL_photo, hashMap9, sendingMediaInfo5, str962222, j, messageObject2, messageObject3, z3, i, z5, storyItem, replyQuote, str, i2, j2, z4);
                                            }
                                        });
                                        i31 = i9;
                                        arrayList15 = arrayList26;
                                        arrayList16 = arrayList27;
                                        arrayList14 = arrayList21;
                                        str18 = str10;
                                        str19 = str46;
                                        arrayList17 = arrayList9;
                                    } else {
                                        str16 = str40;
                                        str17 = str45;
                                        str53 = str44;
                                        str54 = str9;
                                        str55 = str12;
                                        j9 = j4;
                                        i20 = i11;
                                        i21 = i10;
                                        ArrayList arrayList29 = arrayList9;
                                        str35 = str43;
                                        if (arrayList29 == null) {
                                            arrayList17 = new ArrayList();
                                            arrayList16 = new ArrayList();
                                            arrayList11 = new ArrayList();
                                            arrayList12 = new ArrayList();
                                            arrayList15 = new ArrayList();
                                            arrayList10 = arrayList12;
                                            String str97 = str35;
                                            arrayList14 = arrayList11;
                                            str56 = str97;
                                            arrayList17.add(str47);
                                            arrayList16.add(str56);
                                            arrayList15.add(sendingMediaInfo5.uri);
                                            arrayList14.add(sendingMediaInfo5.caption);
                                            arrayList10.add(sendingMediaInfo5.entities);
                                            arrayList19 = arrayList10;
                                            i31 = i9;
                                            str18 = str10;
                                            str19 = str46;
                                        } else {
                                            str56 = str35;
                                            arrayList17 = arrayList29;
                                            arrayList10 = arrayList19;
                                            arrayList15 = arrayList26;
                                            arrayList16 = arrayList27;
                                            arrayList14 = arrayList21;
                                            arrayList17.add(str47);
                                            arrayList16.add(str56);
                                            arrayList15.add(sendingMediaInfo5.uri);
                                            arrayList14.add(sendingMediaInfo5.caption);
                                            arrayList10.add(sendingMediaInfo5.entities);
                                            arrayList19 = arrayList10;
                                            i31 = i9;
                                            str18 = str10;
                                            str19 = str46;
                                        }
                                    }
                                } else if (arrayList17 == null) {
                                    arrayList17 = new ArrayList();
                                    arrayList16 = new ArrayList();
                                    arrayList11 = new ArrayList();
                                    arrayList12 = new ArrayList();
                                    arrayList15 = new ArrayList();
                                    str53 = str38;
                                    str16 = str40;
                                    str17 = str39;
                                    str46 = str2;
                                    str54 = str9;
                                    str55 = str12;
                                    hashMap3 = hashMap2;
                                    j9 = j4;
                                    i20 = i11;
                                    i21 = i10;
                                    str47 = str41;
                                    str15 = str37;
                                    arrayList10 = arrayList12;
                                    String str972 = str35;
                                    arrayList14 = arrayList11;
                                    str56 = str972;
                                    arrayList17.add(str47);
                                    arrayList16.add(str56);
                                    arrayList15.add(sendingMediaInfo5.uri);
                                    arrayList14.add(sendingMediaInfo5.caption);
                                    arrayList10.add(sendingMediaInfo5.entities);
                                    arrayList19 = arrayList10;
                                    i31 = i9;
                                    str18 = str10;
                                    str19 = str46;
                                } else {
                                    str56 = str35;
                                    str53 = str38;
                                    str16 = str40;
                                    str17 = str39;
                                    str46 = str2;
                                    arrayList15 = arrayList26;
                                    arrayList16 = arrayList27;
                                    arrayList14 = arrayList21;
                                    str54 = str9;
                                    str55 = str12;
                                    hashMap3 = hashMap2;
                                    j9 = j4;
                                    i20 = i11;
                                    i21 = i10;
                                    str47 = str41;
                                    str15 = str37;
                                    arrayList10 = arrayList19;
                                    arrayList17.add(str47);
                                    arrayList16.add(str56);
                                    arrayList15.add(sendingMediaInfo5.uri);
                                    arrayList14.add(sendingMediaInfo5.caption);
                                    arrayList10.add(sendingMediaInfo5.entities);
                                    arrayList19 = arrayList10;
                                    i31 = i9;
                                    str18 = str10;
                                    str19 = str46;
                                }
                                str20 = str53;
                                i12 = i20;
                                j5 = j9;
                                i13 = i21;
                                str21 = str54;
                                str22 = str55;
                            }
                            z10 = true;
                            arrayList20 = r29;
                            arrayList17 = arrayList4;
                            if (z10) {
                            }
                            str20 = str53;
                            i12 = i20;
                            j5 = j9;
                            i13 = i21;
                            str21 = str54;
                            str22 = str55;
                        } else {
                            str14 = str2;
                            str15 = str4;
                            str16 = str8;
                            str17 = str3;
                            hashMap3 = hashMap2;
                            if (z) {
                                videoEditedInfo = sendingMediaInfo5.videoEditedInfo;
                                if (videoEditedInfo == null) {
                                    videoEditedInfo = createCompressionSettings(sendingMediaInfo5.path);
                                }
                            } else {
                                videoEditedInfo = null;
                            }
                            if (!z || (videoEditedInfo == null && !sendingMediaInfo5.path.endsWith("mp4"))) {
                                str18 = str10;
                                str19 = str14;
                                str20 = str11;
                                i12 = i11;
                                j5 = j4;
                                i13 = i10;
                                str21 = str9;
                                str22 = str12;
                                if (arrayList4 != null) {
                                    arrayList17 = new ArrayList();
                                    arrayList16 = new ArrayList();
                                    arrayList14 = new ArrayList();
                                    ArrayList arrayList30 = new ArrayList();
                                    arrayList15 = new ArrayList();
                                    arrayList5 = arrayList30;
                                } else {
                                    arrayList17 = arrayList4;
                                    arrayList5 = arrayList19;
                                    arrayList15 = arrayList26;
                                    arrayList16 = arrayList27;
                                    arrayList14 = arrayList21;
                                }
                                arrayList17.add(sendingMediaInfo5.path);
                                arrayList16.add(sendingMediaInfo5.path);
                                arrayList15.add(sendingMediaInfo5.uri);
                                arrayList14.add(sendingMediaInfo5.caption);
                                arrayList5.add(sendingMediaInfo5.entities);
                                arrayList19 = arrayList5;
                                i31 = i9;
                            } else {
                                if (sendingMediaInfo5.path == null && (searchImage2 = sendingMediaInfo5.searchImage) != null) {
                                    sendingMediaInfo5.path = searchImage2.photo instanceof TLRPC.TL_photo ? FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(sendingMediaInfo5.searchImage.photo, true).getAbsolutePath() : new File(FileLoader.getDirectory(4), Utilities.MD5(sendingMediaInfo5.searchImage.imageUrl) + "." + ImageLoader.getHttpUrlExtension(sendingMediaInfo5.searchImage.imageUrl, "jpg")).getAbsolutePath();
                                }
                                String str98 = sendingMediaInfo5.path;
                                File file10 = new File(str98);
                                String str99 = str98 + file10.length() + str14 + file10.lastModified();
                                if (videoEditedInfo != null) {
                                    boolean z19 = videoEditedInfo.muted;
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append(str99);
                                    sb6.append(videoEditedInfo.estimatedDuration);
                                    sb6.append(str14);
                                    sb6.append(videoEditedInfo.startTime);
                                    sb6.append(str14);
                                    sb6.append(videoEditedInfo.endTime);
                                    sb6.append(videoEditedInfo.muted ? "_m" : "");
                                    String sb7 = sb6.toString();
                                    if (videoEditedInfo.resultWidth != videoEditedInfo.originalWidth) {
                                        sb7 = sb7 + str14 + videoEditedInfo.resultWidth;
                                    }
                                    String str100 = sb7;
                                    long j13 = videoEditedInfo.startTime;
                                    if (j13 >= 0) {
                                        z9 = z19;
                                        str23 = str100;
                                        j6 = j13;
                                    } else {
                                        str23 = str100;
                                        z9 = z19;
                                        j6 = 0;
                                    }
                                } else {
                                    str23 = str99;
                                    j6 = 0;
                                    z9 = false;
                                }
                                if (!z8 && sendingMediaInfo5.ttl == 0 && (videoEditedInfo == null || (videoEditedInfo.filterState == null && videoEditedInfo.paintPath == null && videoEditedInfo.mediaEntities == null && videoEditedInfo.cropState == null))) {
                                    Object[] sentFile5 = accountInstance.getMessagesStorage().getSentFile(str23, !z8 ? 2 : 5);
                                    if (sentFile5 != null) {
                                        Object obj10 = sentFile5[0];
                                        if (obj10 instanceof TLRPC.TL_document) {
                                            TLRPC.TL_document tL_document6 = (TLRPC.TL_document) obj10;
                                            String str101 = (String) sentFile5[1];
                                            j7 = j6;
                                            file = file10;
                                            str25 = str98;
                                            str19 = str14;
                                            str24 = "mp4";
                                            obj = "final";
                                            obj2 = "groupId";
                                            arrayList6 = arrayList4;
                                            i14 = 0;
                                            ensureMediaThumbExists(accountInstance, z8, tL_document6, sendingMediaInfo5.path, null, j7);
                                            tL_document = tL_document6;
                                            str26 = str101;
                                            if (tL_document != null) {
                                                String str102 = sendingMediaInfo5.thumbPath;
                                                Bitmap decodeFile = str102 != null ? BitmapFactory.decodeFile(str102) : null;
                                                if (decodeFile == null && (decodeFile = createVideoThumbnailAtTime(sendingMediaInfo5.path, j7)) == null) {
                                                    decodeFile = createVideoThumbnail(sendingMediaInfo5.path, 1);
                                                }
                                                if (decodeFile != null) {
                                                    int max = (z8 || sendingMediaInfo5.ttl != 0) ? 90 : Math.max(decodeFile.getWidth(), decodeFile.getHeight());
                                                    float f3 = max;
                                                    sendingMediaInfo = sendingMediaInfo5;
                                                    photoSize2 = ImageLoader.scaleAndSaveImage(null, decodeFile, (videoEditedInfo == null || !videoEditedInfo.isSticker) ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.WEBP, false, f3, f3, max > 90 ? 80 : 55, z8, 0, 0, false);
                                                    str31 = (photoSize2 == null || photoSize2.location == null) ? null : getKeyForPhotoSize(accountInstance, photoSize2, null, true, i14);
                                                } else {
                                                    sendingMediaInfo = sendingMediaInfo5;
                                                    photoSize2 = null;
                                                    str31 = null;
                                                }
                                                TLRPC.TL_document tL_document7 = new TLRPC.TL_document();
                                                tL_document7.file_reference = new byte[i14];
                                                if (photoSize2 != null) {
                                                    tL_document7.thumbs.add(photoSize2);
                                                    tL_document7.flags |= 1;
                                                }
                                                VideoEditedInfo videoEditedInfo2 = sendingMediaInfo.videoEditedInfo;
                                                if (videoEditedInfo2 == null || !videoEditedInfo2.isSticker) {
                                                    str32 = str10;
                                                    tL_document7.mime_type = str32;
                                                } else {
                                                    tL_document7.mime_type = "video/webm";
                                                    str32 = str10;
                                                }
                                                accountInstance.getUserConfig().saveConfig(i14);
                                                VideoEditedInfo videoEditedInfo3 = sendingMediaInfo.videoEditedInfo;
                                                if (videoEditedInfo3 != null && videoEditedInfo3.isSticker) {
                                                    tL_document7.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                                                    TLRPC.TL_documentAttributeSticker tL_documentAttributeSticker = new TLRPC.TL_documentAttributeSticker();
                                                    tL_documentAttributeSticker.alt = "👍";
                                                    tL_documentAttributeSticker.stickerset = new TLRPC.TL_inputStickerSetEmpty();
                                                    tL_document7.attributes.add(tL_documentAttributeSticker);
                                                    if (photoSize2 != null && (fileLocation = photoSize2.location) != null) {
                                                        obj3 = "1";
                                                        str33 = str32;
                                                        str31 = String.format(Locale.US, "%d_%d@b1", Long.valueOf(fileLocation.volume_id), Integer.valueOf(photoSize2.location.local_id));
                                                        if (z8) {
                                                            tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo();
                                                            tL_documentAttributeVideo.supports_streaming = true;
                                                        } else {
                                                            tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo_layer159();
                                                        }
                                                        tL_document7.attributes.add(tL_documentAttributeVideo);
                                                        if (videoEditedInfo != null || (!videoEditedInfo.needConvert() && sendingMediaInfo.isVideo)) {
                                                            str34 = str31;
                                                            if (file.exists()) {
                                                                tL_document7.size = (int) file.length();
                                                            }
                                                            fillVideoAttribute(sendingMediaInfo.path, tL_documentAttributeVideo, null);
                                                        } else {
                                                            if (sendingMediaInfo.isVideo && videoEditedInfo.muted) {
                                                                fillVideoAttribute(sendingMediaInfo.path, tL_documentAttributeVideo, videoEditedInfo);
                                                                videoEditedInfo.originalWidth = tL_documentAttributeVideo.w;
                                                                videoEditedInfo.originalHeight = tL_documentAttributeVideo.h;
                                                            } else {
                                                                tL_documentAttributeVideo.duration = (int) (videoEditedInfo.estimatedDuration / 1000);
                                                            }
                                                            int i37 = videoEditedInfo.rotationValue;
                                                            MediaController.CropState cropState = videoEditedInfo.cropState;
                                                            if (cropState != null) {
                                                                i18 = cropState.transformWidth;
                                                                i19 = cropState.transformHeight;
                                                            } else {
                                                                i18 = videoEditedInfo.resultWidth;
                                                                i19 = videoEditedInfo.resultHeight;
                                                            }
                                                            str34 = str31;
                                                            if (i37 == 90 || i37 == 270) {
                                                                tL_documentAttributeVideo.w = i19;
                                                                tL_documentAttributeVideo.h = i18;
                                                            } else {
                                                                tL_documentAttributeVideo.w = i18;
                                                                tL_documentAttributeVideo.h = i19;
                                                            }
                                                            tL_document7.size = videoEditedInfo.estimatedSize;
                                                        }
                                                        tL_document2 = tL_document7;
                                                        bitmap = decodeFile;
                                                        str27 = str34;
                                                        str18 = str33;
                                                    }
                                                }
                                                obj3 = "1";
                                                str33 = str32;
                                                if (z8) {
                                                }
                                                tL_document7.attributes.add(tL_documentAttributeVideo);
                                                if (videoEditedInfo != null) {
                                                }
                                                str34 = str31;
                                                if (file.exists()) {
                                                }
                                                fillVideoAttribute(sendingMediaInfo.path, tL_documentAttributeVideo, null);
                                                tL_document2 = tL_document7;
                                                bitmap = decodeFile;
                                                str27 = str34;
                                                str18 = str33;
                                            } else {
                                                obj3 = "1";
                                                sendingMediaInfo = sendingMediaInfo5;
                                                long j14 = j7;
                                                if (tL_document.thumbs.isEmpty()) {
                                                    str18 = str10;
                                                    tL_document2 = tL_document;
                                                    bitmap = null;
                                                    str27 = null;
                                                } else {
                                                    String str103 = sendingMediaInfo.thumbPath;
                                                    Bitmap decodeFile2 = str103 != null ? BitmapFactory.decodeFile(str103) : null;
                                                    if (decodeFile2 == null && (decodeFile2 = createVideoThumbnailAtTime(sendingMediaInfo.path, j14)) == null) {
                                                        decodeFile2 = createVideoThumbnail(sendingMediaInfo.path, 1);
                                                    }
                                                    if (decodeFile2 != null) {
                                                        int max2 = (z8 || sendingMediaInfo.ttl != 0) ? 90 : Math.max(decodeFile2.getWidth(), decodeFile2.getHeight());
                                                        float f4 = max2;
                                                        str18 = str10;
                                                        photoSize = ImageLoader.scaleAndSaveImage(null, decodeFile2, (videoEditedInfo == null || !videoEditedInfo.isSticker) ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.WEBP, false, f4, f4, max2 > 90 ? 80 : 55, z8, 0, 0, false);
                                                        if (photoSize == null || photoSize.location == null) {
                                                            i15 = 1;
                                                            str28 = null;
                                                        } else {
                                                            i15 = 1;
                                                            str28 = getKeyForPhotoSize(accountInstance, photoSize, null, true, false);
                                                        }
                                                    } else {
                                                        str18 = str10;
                                                        i15 = 1;
                                                        photoSize = null;
                                                        str28 = null;
                                                    }
                                                    if (photoSize != null) {
                                                        tL_document.thumbs.add(photoSize);
                                                        tL_document.flags |= i15;
                                                    }
                                                    bitmap = decodeFile2;
                                                    str27 = str28;
                                                    tL_document2 = tL_document;
                                                }
                                            }
                                            if (videoEditedInfo != null && videoEditedInfo.muted) {
                                                size = tL_document2.attributes.size();
                                                i17 = 0;
                                                while (true) {
                                                    if (i17 < size) {
                                                        tL_document2.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                                                        break;
                                                    } else if (tL_document2.attributes.get(i17) instanceof TLRPC.TL_documentAttributeAnimated) {
                                                        break;
                                                    } else {
                                                        i17++;
                                                    }
                                                }
                                            }
                                            if (videoEditedInfo != null || (!videoEditedInfo.needConvert() && sendingMediaInfo.isVideo)) {
                                                str29 = str11;
                                            } else {
                                                String str104 = videoEditedInfo.isSticker ? "webm" : str24;
                                                StringBuilder sb8 = new StringBuilder();
                                                str29 = str11;
                                                sb8.append(str29);
                                                sb8.append(SharedConfig.getLastLocalId());
                                                sb8.append(".");
                                                sb8.append(str104);
                                                File file11 = new File(FileLoader.getDirectory(4), sb8.toString());
                                                SharedConfig.saveConfig();
                                                str25 = file11.getAbsolutePath();
                                            }
                                            final HashMap hashMap10 = new HashMap();
                                            if (str23 != null) {
                                                hashMap10.put("originalPath", str23);
                                            }
                                            if (str26 != null) {
                                                hashMap10.put(str12, str26);
                                            }
                                            if (z9 && z2) {
                                                int i38 = i9 + 1;
                                                StringBuilder sb9 = new StringBuilder();
                                                sb9.append("");
                                                bitmap2 = bitmap;
                                                j8 = j4;
                                                sb9.append(j8);
                                                hashMap10.put(obj2, sb9.toString());
                                                if (i38 != 10) {
                                                    i16 = i11;
                                                    str22 = str12;
                                                    i13 = i10;
                                                    if (i13 != i16 - 1) {
                                                        i9 = i38;
                                                    }
                                                } else {
                                                    str22 = str12;
                                                    i16 = i11;
                                                    i13 = i10;
                                                }
                                                hashMap10.put(obj, obj3);
                                                i9 = i38;
                                                j12 = 0;
                                            } else {
                                                str22 = str12;
                                                bitmap2 = bitmap;
                                                i16 = i11;
                                                j8 = j4;
                                                i13 = i10;
                                            }
                                            if (!z8 || ((videoEditedInfo != null && videoEditedInfo.isSticker) || (arrayList7 = sendingMediaInfo.masks) == null || arrayList7.isEmpty())) {
                                                str30 = str9;
                                            } else {
                                                tL_document2.attributes.add(new TLRPC.TL_documentAttributeHasStickers());
                                                SerializedData serializedData2 = new SerializedData((sendingMediaInfo.masks.size() * 20) + 4);
                                                serializedData2.writeInt32(sendingMediaInfo.masks.size());
                                                for (int i39 = 0; i39 < sendingMediaInfo.masks.size(); i39++) {
                                                    sendingMediaInfo.masks.get(i39).serializeToStream(serializedData2);
                                                }
                                                str30 = str9;
                                                hashMap10.put(str30, Utilities.bytesToHex(serializedData2.toByteArray()));
                                                serializedData2.cleanup();
                                            }
                                            j5 = j8;
                                            final Bitmap bitmap3 = bitmap2;
                                            final SendingMediaInfo sendingMediaInfo6 = sendingMediaInfo;
                                            str20 = str29;
                                            final VideoEditedInfo videoEditedInfo4 = videoEditedInfo;
                                            final String str105 = str26;
                                            final TLRPC.TL_document tL_document8 = tL_document2;
                                            final String str106 = str25;
                                            i12 = i16;
                                            str21 = str30;
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda27
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    SendMessagesHelper.lambda$prepareSendingMedia$92(bitmap3, str27, messageObject, accountInstance, videoEditedInfo4, tL_document8, str106, hashMap10, sendingMediaInfo6, str105, j, messageObject2, messageObject3, z3, i, storyItem, replyQuote, str, i2, j2, z4);
                                                }
                                            });
                                            i31 = i9;
                                            arrayList15 = arrayList26;
                                            arrayList16 = arrayList27;
                                            arrayList14 = arrayList21;
                                            arrayList17 = arrayList6;
                                        }
                                    }
                                }
                                j7 = j6;
                                file = file10;
                                str19 = str14;
                                obj = "final";
                                str24 = "mp4";
                                str25 = str98;
                                obj2 = "groupId";
                                arrayList6 = arrayList4;
                                i14 = 0;
                                tL_document = null;
                                str26 = null;
                                if (tL_document != null) {
                                }
                                if (videoEditedInfo != null) {
                                    size = tL_document2.attributes.size();
                                    i17 = 0;
                                    while (true) {
                                        if (i17 < size) {
                                        }
                                        i17++;
                                    }
                                }
                                if (videoEditedInfo != null) {
                                }
                                str29 = str11;
                                final HashMap hashMap102 = new HashMap();
                                if (str23 != null) {
                                }
                                if (str26 != null) {
                                }
                                if (z9) {
                                }
                                str22 = str12;
                                bitmap2 = bitmap;
                                i16 = i11;
                                j8 = j4;
                                i13 = i10;
                                if (z8) {
                                }
                                str30 = str9;
                                j5 = j8;
                                final Bitmap bitmap32 = bitmap2;
                                final SendingMediaInfo sendingMediaInfo62 = sendingMediaInfo;
                                str20 = str29;
                                final VideoEditedInfo videoEditedInfo42 = videoEditedInfo;
                                final String str1052 = str26;
                                final TLRPC.TL_document tL_document82 = tL_document2;
                                final String str1062 = str25;
                                i12 = i16;
                                str21 = str30;
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda27
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.lambda$prepareSendingMedia$92(bitmap32, str27, messageObject, accountInstance, videoEditedInfo42, tL_document82, str1062, hashMap102, sendingMediaInfo62, str1052, j, messageObject2, messageObject3, z3, i, storyItem, replyQuote, str, i2, j2, z4);
                                    }
                                });
                                i31 = i9;
                                arrayList15 = arrayList26;
                                arrayList16 = arrayList27;
                                arrayList14 = arrayList21;
                                arrayList17 = arrayList6;
                            }
                        }
                    }
                    i32 = i13 + 1;
                    arrayList13 = arrayList;
                    hashMap = hashMap3;
                    str6 = str13;
                    str3 = str17;
                    z6 = z8;
                    str76 = str21;
                    str79 = str18;
                    j11 = j5;
                    str5 = str20;
                    str7 = str22;
                    i4 = i12;
                    i3 = 1;
                    str8 = str16;
                    str4 = str15;
                    str2 = str19;
                    arrayList20 = arrayList20;
                }
            }
            i9 = i31;
            j3 = j11;
            searchImage = sendingMediaInfo5.searchImage;
            i10 = i32;
            str9 = str76;
            ArrayList arrayList212 = arrayList14;
            i11 = i4;
            hashMap2 = hashMap;
            if (searchImage == null) {
            }
            ArrayList arrayList262 = arrayList15;
            ArrayList arrayList272 = arrayList16;
            arrayList4 = arrayList17;
            z8 = z6;
            str10 = str79;
            str11 = str5;
            str12 = str7;
            j4 = j3;
            str13 = str6;
            if (sendingMediaInfo5.isVideo) {
            }
            str14 = str2;
            str15 = str4;
            str16 = str8;
            str17 = str3;
            hashMap3 = hashMap2;
            if (z) {
            }
            if (z) {
            }
            str18 = str10;
            str19 = str14;
            str20 = str11;
            i12 = i11;
            j5 = j4;
            i13 = i10;
            str21 = str9;
            str22 = str12;
            if (arrayList4 != null) {
            }
            arrayList17.add(sendingMediaInfo5.path);
            arrayList16.add(sendingMediaInfo5.path);
            arrayList15.add(sendingMediaInfo5.uri);
            arrayList14.add(sendingMediaInfo5.caption);
            arrayList5.add(sendingMediaInfo5.entities);
            arrayList19 = arrayList5;
            i31 = i9;
            i32 = i13 + 1;
            arrayList13 = arrayList;
            hashMap = hashMap3;
            str6 = str13;
            str3 = str17;
            z6 = z8;
            str76 = str21;
            str79 = str18;
            j11 = j5;
            str5 = str20;
            str7 = str22;
            i4 = i12;
            i3 = 1;
            str8 = str16;
            str4 = str15;
            str2 = str19;
            arrayList20 = arrayList20;
        }
        ArrayList arrayList31 = arrayList14;
        ArrayList arrayList32 = arrayList15;
        ArrayList arrayList33 = arrayList16;
        ArrayList arrayList34 = arrayList17;
        boolean z20 = z6;
        int i40 = i4;
        long j15 = j12;
        if (j15 != 0) {
            finishGroup(accountInstance, j15, i);
        }
        if (inputContentInfoCompat != null) {
            inputContentInfoCompat.releasePermission();
        }
        if (arrayList34 != null && !arrayList34.isEmpty()) {
            int i41 = 1;
            long[] jArr = new long[1];
            int size3 = arrayList34.size();
            int i42 = i31;
            int i43 = 0;
            while (i43 < size3) {
                if (!z || z20) {
                    i5 = i40;
                    i6 = 10;
                } else {
                    i5 = i40;
                    i6 = 10;
                    if (i5 > i41 && i42 % 10 == 0) {
                        jArr[0] = Utilities.random.nextLong();
                        i7 = 1;
                        i42 = 0;
                        i8 = i42 + i7;
                        String str107 = (String) arrayList34.get(i43);
                        ArrayList arrayList35 = arrayList33;
                        String str108 = (String) arrayList35.get(i43);
                        ArrayList arrayList36 = arrayList32;
                        Uri uri6 = (Uri) arrayList36.get(i43);
                        ArrayList arrayList37 = arrayList19;
                        ArrayList arrayList38 = (ArrayList) arrayList37.get(i43);
                        if (i8 != i6 || i43 == size3 - 1) {
                            arrayList3 = arrayList31;
                            z7 = true;
                        } else {
                            arrayList3 = arrayList31;
                            z7 = false;
                        }
                        arrayList19 = arrayList37;
                        long[] jArr2 = jArr;
                        handleError(prepareSendingDocumentInternal(accountInstance, str107, str108, uri6, arrayList20, j, messageObject2, messageObject3, storyItem, replyQuote, arrayList38, messageObject, jArr2, z7, (CharSequence) arrayList3.get(i43), z3, i, null, z, str, i2, j2, z4), accountInstance);
                        i43++;
                        arrayList33 = arrayList35;
                        arrayList32 = arrayList36;
                        i42 = i8;
                        arrayList31 = arrayList3;
                        i40 = i5;
                        size3 = size3;
                        jArr = jArr2;
                        arrayList34 = arrayList34;
                        i41 = 1;
                    }
                }
                i7 = 1;
                i8 = i42 + i7;
                String str1072 = (String) arrayList34.get(i43);
                ArrayList arrayList352 = arrayList33;
                String str1082 = (String) arrayList352.get(i43);
                ArrayList arrayList362 = arrayList32;
                Uri uri62 = (Uri) arrayList362.get(i43);
                ArrayList arrayList372 = arrayList19;
                ArrayList arrayList382 = (ArrayList) arrayList372.get(i43);
                if (i8 != i6) {
                }
                arrayList3 = arrayList31;
                z7 = true;
                arrayList19 = arrayList372;
                long[] jArr22 = jArr;
                handleError(prepareSendingDocumentInternal(accountInstance, str1072, str1082, uri62, arrayList20, j, messageObject2, messageObject3, storyItem, replyQuote, arrayList382, messageObject, jArr22, z7, (CharSequence) arrayList3.get(i43), z3, i, null, z, str, i2, j2, z4), accountInstance);
                i43++;
                arrayList33 = arrayList352;
                arrayList32 = arrayList362;
                i42 = i8;
                arrayList31 = arrayList3;
                i40 = i5;
                size3 = size3;
                jArr = jArr22;
                arrayList34 = arrayList34;
                i41 = 1;
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("total send time = " + (System.currentTimeMillis() - currentTimeMillis));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$prepareSendingText$86(String str, long j, AccountInstance accountInstance, long j2, boolean z, int i, long j3) {
        MessageObject messageObject;
        TLRPC.TL_forumTopic findTopic;
        String trimmedString = getTrimmedString(str);
        if (trimmedString.length() != 0) {
            int ceil = (int) Math.ceil(trimmedString.length() / 4096.0f);
            int i2 = 0;
            if (j != 0 && (findTopic = accountInstance.getMessagesController().getTopicsController().findTopic(-j2, j)) != null && findTopic.topicStartMessage != null) {
                messageObject = new MessageObject(accountInstance.getCurrentAccount(), findTopic.topicStartMessage, false, false);
                messageObject.isTopicMainMessage = true;
                while (i2 < ceil) {
                    int i3 = i2 + 1;
                    SendMessageParams of = SendMessageParams.of(trimmedString.substring(i2 * LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM, Math.min(i3 * LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM, trimmedString.length())), j2, messageObject, messageObject, null, true, null, null, null, z, i, null, false);
                    if (i2 == 0) {
                        of.effect_id = j3;
                    }
                    accountInstance.getSendMessagesHelper().sendMessage(of);
                    i2 = i3;
                }
            }
            messageObject = null;
            while (i2 < ceil) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareSendingText$87(final String str, final long j, final AccountInstance accountInstance, final long j2, final boolean z, final int i, final long j3) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingText$86(str, j, accountInstance, j2, z, i, j3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareSendingText$88(final String str, final long j, final AccountInstance accountInstance, final long j2, final boolean z, final int i, final long j3) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda85
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingText$87(str, j, accountInstance, j2, z, i, j3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareSendingVideo$95(Bitmap bitmap, String str, MessageObject messageObject, AccountInstance accountInstance, VideoEditedInfo videoEditedInfo, TLRPC.TL_document tL_document, String str2, HashMap hashMap, boolean z, String str3, long j, MessageObject messageObject2, MessageObject messageObject3, String str4, ArrayList arrayList, boolean z2, int i, int i2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, int i3, String str5, long j2) {
        if (bitmap != null && str != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmap), str, false);
        }
        if (messageObject != null) {
            accountInstance.getSendMessagesHelper().editMessage(messageObject, null, videoEditedInfo, tL_document, str2, hashMap, false, z, str3);
            return;
        }
        SendMessageParams of = SendMessageParams.of(tL_document, videoEditedInfo, str2, j, messageObject2, messageObject3, str4, arrayList, null, hashMap, z2, i, i2, str3, null, false, z);
        of.replyToStoryItem = storyItem;
        of.replyQuote = replyQuote;
        of.quick_reply_shortcut_id = i3;
        of.quick_reply_shortcut = str5;
        of.effect_id = j2;
        accountInstance.getSendMessagesHelper().sendMessage(of);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:103:0x04bf  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x04cc  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x04d3  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x04db  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x04c7  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0338  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0475  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0486  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$prepareSendingVideo$96(VideoEditedInfo videoEditedInfo, String str, final long j, final int i, final AccountInstance accountInstance, CharSequence charSequence, final MessageObject messageObject, final boolean z, final MessageObject messageObject2, final MessageObject messageObject3, final ArrayList arrayList, final boolean z2, final int i2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final int i3, final String str2, final long j2, boolean z3) {
        String str3;
        long j3;
        long j4;
        String str4;
        String str5;
        final TLRPC.TL_document tL_document;
        String str6;
        final Bitmap bitmap;
        final String str7;
        int i4;
        String str8;
        String str9;
        String str10;
        TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo;
        int i5;
        int i6;
        Object[] sentFile;
        final String str11 = str;
        VideoEditedInfo createCompressionSettings = videoEditedInfo != null ? videoEditedInfo : createCompressionSettings(str);
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(j);
        boolean z4 = createCompressionSettings != null && createCompressionSettings.roundVideo;
        if (createCompressionSettings == null && !str11.endsWith("mp4") && !z4) {
            prepareSendingDocumentInternal(accountInstance, str, str, null, null, j, messageObject2, messageObject3, storyItem, replyQuote, arrayList, messageObject, null, false, charSequence, z2, i2, null, z3, str2, i3, 0L, false);
            return;
        }
        File file = new File(str11);
        String str12 = str11 + file.length() + "_" + file.lastModified();
        if (createCompressionSettings != null) {
            if (!z4) {
                StringBuilder sb = new StringBuilder();
                sb.append(str12);
                sb.append(createCompressionSettings.estimatedDuration);
                sb.append("_");
                sb.append(createCompressionSettings.startTime);
                sb.append("_");
                sb.append(createCompressionSettings.endTime);
                sb.append(createCompressionSettings.muted ? "_m" : "");
                str12 = sb.toString();
                if (createCompressionSettings.resultWidth != createCompressionSettings.originalWidth) {
                    str12 = str12 + "_" + createCompressionSettings.resultWidth;
                }
            }
            j3 = createCompressionSettings.startTime;
            if (j3 >= 0) {
                str3 = str12;
                if (!isEncryptedDialog && i == 0 && (createCompressionSettings == null || (createCompressionSettings.filterState == null && createCompressionSettings.paintPath == null && createCompressionSettings.mediaEntities == null && createCompressionSettings.cropState == null))) {
                    sentFile = accountInstance.getMessagesStorage().getSentFile(str3, isEncryptedDialog ? 2 : 5);
                    if (sentFile != null) {
                        Object obj = sentFile[0];
                        if (obj instanceof TLRPC.TL_document) {
                            TLRPC.TL_document tL_document2 = (TLRPC.TL_document) obj;
                            String str13 = (String) sentFile[1];
                            j4 = j3;
                            str4 = str3;
                            ensureMediaThumbExists(accountInstance, isEncryptedDialog, tL_document2, str, null, j4);
                            tL_document = tL_document2;
                            str5 = str13;
                            if (tL_document == null) {
                                Bitmap bitmap2 = (createCompressionSettings == null || !createCompressionSettings.notReadyYet) ? null : createCompressionSettings.thumb;
                                str6 = str5;
                                if (bitmap2 == null) {
                                    bitmap2 = createVideoThumbnailAtTime(str11, j4);
                                }
                                if (bitmap2 == null) {
                                    bitmap2 = createVideoThumbnail(str11, 1);
                                }
                                int i7 = (isEncryptedDialog || i != 0) ? 90 : 320;
                                float f = i7;
                                TLRPC.PhotoSize scaleAndSaveImage = ImageLoader.scaleAndSaveImage(bitmap2, f, f, i7 > 90 ? 80 : 55, isEncryptedDialog);
                                if (bitmap2 == null || scaleAndSaveImage == null) {
                                    str10 = null;
                                } else if (!z4) {
                                    str10 = null;
                                    bitmap2 = null;
                                } else if (isEncryptedDialog) {
                                    bitmap2 = Bitmap.createScaledBitmap(bitmap2, 90, 90, true);
                                    int i8 = Build.VERSION.SDK_INT;
                                    Utilities.blurBitmap(bitmap2, 7, i8 < 21 ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                    Utilities.blurBitmap(bitmap2, 7, i8 < 21 ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                    Utilities.blurBitmap(bitmap2, 7, i8 < 21 ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                    str10 = String.format(scaleAndSaveImage.location.volume_id + "_" + scaleAndSaveImage.location.local_id + "@%d_%d_b2", Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)), Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)));
                                } else {
                                    Utilities.blurBitmap(bitmap2, 3, Build.VERSION.SDK_INT < 21 ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                    str10 = String.format(scaleAndSaveImage.location.volume_id + "_" + scaleAndSaveImage.location.local_id + "@%d_%d_b", Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)), Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)));
                                }
                                TLRPC.TL_document tL_document3 = new TLRPC.TL_document();
                                if (scaleAndSaveImage != null) {
                                    tL_document3.thumbs.add(scaleAndSaveImage);
                                    tL_document3.flags |= 1;
                                }
                                tL_document3.file_reference = new byte[0];
                                tL_document3.mime_type = "video/mp4";
                                accountInstance.getUserConfig().saveConfig(false);
                                if (!isEncryptedDialog) {
                                    tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo();
                                    tL_documentAttributeVideo.supports_streaming = true;
                                } else if (accountInstance.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j))) == null) {
                                    return;
                                } else {
                                    tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo_layer159();
                                }
                                tL_documentAttributeVideo.round_message = z4;
                                tL_document3.attributes.add(tL_documentAttributeVideo);
                                if (createCompressionSettings != null && createCompressionSettings.notReadyYet) {
                                    tL_documentAttributeVideo.w = createCompressionSettings.resultWidth;
                                    tL_documentAttributeVideo.h = createCompressionSettings.resultHeight;
                                    double d = createCompressionSettings.estimatedDuration;
                                    Double.isNaN(d);
                                    tL_documentAttributeVideo.duration = d / 1000.0d;
                                } else if (createCompressionSettings == null || !createCompressionSettings.needConvert()) {
                                    if (file.exists()) {
                                        tL_document3.size = (int) file.length();
                                    }
                                    fillVideoAttribute(str11, tL_documentAttributeVideo, null);
                                    str7 = str10;
                                    Bitmap bitmap3 = bitmap2;
                                    tL_document = tL_document3;
                                    bitmap = bitmap3;
                                } else {
                                    if (createCompressionSettings.muted) {
                                        tL_document3.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                                        fillVideoAttribute(str11, tL_documentAttributeVideo, createCompressionSettings);
                                        createCompressionSettings.originalWidth = tL_documentAttributeVideo.w;
                                        createCompressionSettings.originalHeight = tL_documentAttributeVideo.h;
                                    } else {
                                        double d2 = createCompressionSettings.estimatedDuration;
                                        Double.isNaN(d2);
                                        tL_documentAttributeVideo.duration = d2 / 1000.0d;
                                    }
                                    int i9 = createCompressionSettings.rotationValue;
                                    MediaController.CropState cropState = createCompressionSettings.cropState;
                                    if (cropState != null) {
                                        i5 = cropState.transformWidth;
                                        i6 = cropState.transformHeight;
                                        i9 += cropState.transformRotation;
                                    } else {
                                        i5 = createCompressionSettings.resultWidth;
                                        i6 = createCompressionSettings.resultHeight;
                                    }
                                    if (i9 == 90 || i9 == 270) {
                                        tL_documentAttributeVideo.w = i6;
                                        tL_documentAttributeVideo.h = i5;
                                    } else {
                                        tL_documentAttributeVideo.w = i5;
                                        tL_documentAttributeVideo.h = i6;
                                    }
                                }
                                tL_document3.size = createCompressionSettings.estimatedSize;
                                str7 = str10;
                                Bitmap bitmap32 = bitmap2;
                                tL_document = tL_document3;
                                bitmap = bitmap32;
                            } else {
                                str6 = str5;
                                long j5 = j4;
                                String str14 = null;
                                if (tL_document.thumbs.isEmpty()) {
                                    Bitmap bitmap4 = (createCompressionSettings == null || !createCompressionSettings.notReadyYet) ? null : createCompressionSettings.thumb;
                                    if (bitmap4 == null) {
                                        bitmap4 = createVideoThumbnailAtTime(str11, j5);
                                    }
                                    Bitmap createVideoThumbnail = bitmap4 == null ? createVideoThumbnail(str11, 1) : bitmap4;
                                    int i10 = (isEncryptedDialog || i != 0) ? 90 : 320;
                                    float f2 = i10;
                                    TLRPC.PhotoSize scaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(createVideoThumbnail, f2, f2, i10 > 90 ? 80 : 55, isEncryptedDialog);
                                    if (createVideoThumbnail != null && scaleAndSaveImage2 != null) {
                                        if (!z4) {
                                            i4 = 1;
                                            createVideoThumbnail = null;
                                        } else if (isEncryptedDialog) {
                                            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(createVideoThumbnail, 90, 90, true);
                                            int i11 = Build.VERSION.SDK_INT;
                                            Utilities.blurBitmap(createScaledBitmap, 7, i11 < 21 ? 0 : 1, createScaledBitmap.getWidth(), createScaledBitmap.getHeight(), createScaledBitmap.getRowBytes());
                                            Utilities.blurBitmap(createScaledBitmap, 7, i11 < 21 ? 0 : 1, createScaledBitmap.getWidth(), createScaledBitmap.getHeight(), createScaledBitmap.getRowBytes());
                                            Utilities.blurBitmap(createScaledBitmap, 7, i11 < 21 ? 0 : 1, createScaledBitmap.getWidth(), createScaledBitmap.getHeight(), createScaledBitmap.getRowBytes());
                                            createVideoThumbnail = createScaledBitmap;
                                            str14 = String.format(scaleAndSaveImage2.location.volume_id + "_" + scaleAndSaveImage2.location.local_id + "@%d_%d_b2", Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)), Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)));
                                        } else {
                                            Utilities.blurBitmap(createVideoThumbnail, 3, Build.VERSION.SDK_INT < 21 ? 0 : 1, createVideoThumbnail.getWidth(), createVideoThumbnail.getHeight(), createVideoThumbnail.getRowBytes());
                                            i4 = 1;
                                            str14 = String.format(scaleAndSaveImage2.location.volume_id + "_" + scaleAndSaveImage2.location.local_id + "@%d_%d_b", Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)), Integer.valueOf((int) (AndroidUtilities.roundMessageSize / AndroidUtilities.density)));
                                        }
                                        if (scaleAndSaveImage2 != null) {
                                            tL_document.thumbs.add(scaleAndSaveImage2);
                                            tL_document.flags |= i4;
                                        }
                                        bitmap = createVideoThumbnail;
                                        str7 = str14;
                                    }
                                    i4 = 1;
                                    if (scaleAndSaveImage2 != null) {
                                    }
                                    bitmap = createVideoThumbnail;
                                    str7 = str14;
                                } else {
                                    bitmap = null;
                                    str7 = null;
                                }
                            }
                            if (createCompressionSettings != null && createCompressionSettings.needConvert()) {
                                File file2 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".mp4");
                                SharedConfig.saveConfig();
                                str11 = file2.getAbsolutePath();
                            }
                            final HashMap hashMap = new HashMap();
                            final String charSequence2 = charSequence != null ? charSequence.toString() : "";
                            str8 = str4;
                            if (str8 != null) {
                                hashMap.put("originalPath", str8);
                            }
                            if (str6 != null) {
                                str9 = str6;
                                hashMap.put("parentObject", str9);
                            } else {
                                str9 = str6;
                            }
                            final String str15 = str9;
                            final VideoEditedInfo videoEditedInfo2 = createCompressionSettings;
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda6
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.lambda$prepareSendingVideo$95(bitmap, str7, messageObject, accountInstance, videoEditedInfo2, tL_document, str11, hashMap, z, str15, j, messageObject2, messageObject3, charSequence2, arrayList, z2, i2, i, storyItem, replyQuote, i3, str2, j2);
                                }
                            });
                        }
                    }
                }
                j4 = j3;
                str4 = str3;
                str5 = null;
                tL_document = null;
                if (tL_document == null) {
                }
                if (createCompressionSettings != null) {
                    File file22 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".mp4");
                    SharedConfig.saveConfig();
                    str11 = file22.getAbsolutePath();
                }
                final HashMap hashMap2 = new HashMap();
                if (charSequence != null) {
                }
                str8 = str4;
                if (str8 != null) {
                }
                if (str6 != null) {
                }
                final String str152 = str9;
                final VideoEditedInfo videoEditedInfo22 = createCompressionSettings;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.lambda$prepareSendingVideo$95(bitmap, str7, messageObject, accountInstance, videoEditedInfo22, tL_document, str11, hashMap2, z, str152, j, messageObject2, messageObject3, charSequence2, arrayList, z2, i2, i, storyItem, replyQuote, i3, str2, j2);
                    }
                });
            }
        }
        str3 = str12;
        j3 = 0;
        if (!isEncryptedDialog) {
            sentFile = accountInstance.getMessagesStorage().getSentFile(str3, isEncryptedDialog ? 2 : 5);
            if (sentFile != null) {
            }
        }
        j4 = j3;
        str4 = str3;
        str5 = null;
        tL_document = null;
        if (tL_document == null) {
        }
        if (createCompressionSettings != null) {
        }
        final HashMap hashMap22 = new HashMap();
        if (charSequence != null) {
        }
        str8 = str4;
        if (str8 != null) {
        }
        if (str6 != null) {
        }
        final String str1522 = str9;
        final VideoEditedInfo videoEditedInfo222 = createCompressionSettings;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingVideo$95(bitmap, str7, messageObject, accountInstance, videoEditedInfo222, tL_document, str11, hashMap22, z, str1522, j, messageObject2, messageObject3, charSequence2, arrayList, z2, i2, i, storyItem, replyQuote, i3, str2, j2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processUnsentMessages$66(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, ArrayList arrayList5) {
        HashMap<String, String> hashMap;
        getMessagesController().putUsers(arrayList, true);
        getMessagesController().putChats(arrayList2, true);
        getMessagesController().putEncryptedChats(arrayList3, true);
        int size = arrayList4.size();
        for (int i = 0; i < size; i++) {
            MessageObject messageObject = new MessageObject(this.currentAccount, (TLRPC.Message) arrayList4.get(i), false, true);
            long groupId = messageObject.getGroupId();
            if (groupId != 0 && (hashMap = messageObject.messageOwner.params) != null && !hashMap.containsKey("final") && (i == size - 1 || ((TLRPC.Message) arrayList4.get(i + 1)).grouped_id != groupId)) {
                messageObject.messageOwner.params.put("final", "1");
            }
            retrySendMessage(messageObject, true);
        }
        if (arrayList5 != null) {
            for (int i2 = 0; i2 < arrayList5.size(); i2++) {
                MessageObject messageObject2 = new MessageObject(this.currentAccount, (TLRPC.Message) arrayList5.get(i2), false, true);
                messageObject2.scheduled = true;
                retrySendMessage(messageObject2, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putToSendingMessages$39(TLRPC.Message message, boolean z) {
        putToSendingMessages(message, z, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$requestUrlAuth$23(TLObject tLObject, ChatActivity chatActivity, TLRPC.TL_messages_requestUrlAuth tL_messages_requestUrlAuth, String str, boolean z) {
        if (tLObject != null) {
            if (tLObject instanceof TLRPC.TL_urlAuthResultRequest) {
                chatActivity.showRequestUrlAlert((TLRPC.TL_urlAuthResultRequest) tLObject, tL_messages_requestUrlAuth, str, z);
                return;
            } else if (tLObject instanceof TLRPC.TL_urlAuthResultAccepted) {
                AlertsCreator.showOpenUrlAlert(chatActivity, ((TLRPC.TL_urlAuthResultAccepted) tLObject).url, false, false);
                return;
            } else if (!(tLObject instanceof TLRPC.TL_urlAuthResultDefault)) {
                return;
            }
        }
        AlertsCreator.showOpenUrlAlert(chatActivity, str, false, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$requestUrlAuth$24(final ChatActivity chatActivity, final TLRPC.TL_messages_requestUrlAuth tL_messages_requestUrlAuth, final String str, final boolean z, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda83
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$requestUrlAuth$23(TLObject.this, chatActivity, tL_messages_requestUrlAuth, str, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendCallback$25(String str, List list) {
        this.waitingForCallback.remove(str);
        list.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendCallback$26(String str) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendCallback$28(final boolean z, final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton, final ChatActivity chatActivity, DialogInterface dialogInterface, int i) {
        final TwoStepVerificationActivity twoStepVerificationActivity = new TwoStepVerificationActivity();
        twoStepVerificationActivity.setDelegate(0, new TwoStepVerificationActivity.TwoStepVerificationActivityDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda43
            @Override // org.telegram.ui.TwoStepVerificationActivity.TwoStepVerificationActivityDelegate
            public final void didEnterPassword(TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP) {
                SendMessagesHelper.this.lambda$sendCallback$27(z, messageObject, keyboardButton, twoStepVerificationActivity, chatActivity, inputCheckPasswordSRP);
            }
        });
        chatActivity.presentFragment(twoStepVerificationActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendCallback$29(ChatActivity chatActivity, DialogInterface dialogInterface, int i) {
        chatActivity.presentFragment(new TwoStepVerificationSetupActivity(6, null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendCallback$30(TLRPC.TL_error tL_error, TLObject tLObject, TwoStepVerificationActivity twoStepVerificationActivity, boolean z, MessageObject messageObject, TLRPC.KeyboardButton keyboardButton, ChatActivity chatActivity) {
        if (tL_error == null) {
            TLRPC.account_Password account_password = (TLRPC.account_Password) tLObject;
            twoStepVerificationActivity.setCurrentPasswordInfo(null, account_password);
            TwoStepVerificationActivity.initPasswordNewAlgo(account_password);
            lambda$sendCallback$27(z, messageObject, keyboardButton, twoStepVerificationActivity.getNewSrpPassword(), twoStepVerificationActivity, chatActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendCallback$31(final TwoStepVerificationActivity twoStepVerificationActivity, final boolean z, final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton, final ChatActivity chatActivity, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$sendCallback$30(tL_error, tLObject, twoStepVerificationActivity, z, messageObject, keyboardButton, chatActivity);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:20:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00a1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$sendCallback$32(final String str, final List list, boolean z, TLObject tLObject, final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton, final ChatActivity chatActivity, final TwoStepVerificationActivity twoStepVerificationActivity, TLObject[] tLObjectArr, TLRPC.TL_error tL_error, TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP, final boolean z2) {
        int i;
        AlertDialog create;
        AlertDialog.Builder builder;
        String str2;
        String str3;
        this.waitingForCallback.remove(str);
        list.remove(str);
        boolean z3 = false;
        if (z && tLObject == null) {
            sendCallback(false, messageObject, keyboardButton, chatActivity);
            return;
        }
        if (tLObject != null) {
            if (twoStepVerificationActivity != null) {
                twoStepVerificationActivity.needHideProgress();
                twoStepVerificationActivity.lambda$onBackPressed$321();
            }
            long fromChatId = messageObject.getFromChatId();
            long j = messageObject.messageOwner.via_bot_id;
            if (j != 0) {
                fromChatId = j;
            }
            if (fromChatId <= 0) {
                TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-fromChatId));
                if (chat != null) {
                    str2 = chat.title;
                    if (str2 == null) {
                    }
                    if (keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth) {
                    }
                    AlertsCreator.showOpenUrlAlert(chatActivity, str3, false, false);
                    return;
                }
                str2 = null;
                if (str2 == null) {
                }
                if (keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth) {
                }
                AlertsCreator.showOpenUrlAlert(chatActivity, str3, false, false);
                return;
            }
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(fromChatId));
            if (user != null) {
                str2 = ContactsController.formatName(user.first_name, user.last_name);
                if (str2 == null) {
                    str2 = "bot";
                }
                if (keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth) {
                    if (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) {
                        if (tLObject instanceof TLRPC.TL_payments_paymentFormStars) {
                            StarsController.getInstance(this.currentAccount).openPaymentForm(messageObject, ((TLRPC.TL_payments_getPaymentForm) tLObjectArr[0]).invoice, (TLRPC.TL_payments_paymentFormStars) tLObject, new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda63
                                @Override // java.lang.Runnable
                                public final void run() {
                                    SendMessagesHelper.this.lambda$sendCallback$25(str, list);
                                }
                            }, new Utilities.Callback() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda64
                                @Override // org.telegram.messenger.Utilities.Callback
                                public final void run(Object obj) {
                                    SendMessagesHelper.lambda$sendCallback$26((String) obj);
                                }
                            });
                            return;
                        }
                        if (tLObject instanceof TLRPC.PaymentForm) {
                            TLRPC.PaymentForm paymentForm = (TLRPC.PaymentForm) tLObject;
                            getMessagesController().putUsers(paymentForm.users, false);
                            chatActivity.presentFragment(new PaymentFormActivity(paymentForm, messageObject, chatActivity));
                            return;
                        } else if (!(tLObject instanceof TLRPC.TL_payments_paymentReceiptStars)) {
                            if (tLObject instanceof TLRPC.PaymentReceipt) {
                                chatActivity.presentFragment(new PaymentFormActivity((TLRPC.PaymentReceipt) tLObject));
                                return;
                            }
                            return;
                        } else {
                            Context context = LaunchActivity.instance;
                            if (context == null) {
                                context = ApplicationLoader.applicationContext;
                            }
                            StarsIntroActivity.showTransactionSheet(context, false, this.currentAccount, (TLRPC.TL_payments_paymentReceiptStars) tLObject, (Theme.ResourcesProvider) null);
                            return;
                        }
                    }
                    TLRPC.TL_messages_botCallbackAnswer tL_messages_botCallbackAnswer = (TLRPC.TL_messages_botCallbackAnswer) tLObject;
                    if (!z && tL_messages_botCallbackAnswer.cache_time != 0 && !keyboardButton.requires_password) {
                        getMessagesStorage().saveBotCache(str, tL_messages_botCallbackAnswer);
                    }
                    String str4 = tL_messages_botCallbackAnswer.message;
                    if (str4 != null) {
                        if (!tL_messages_botCallbackAnswer.alert) {
                            chatActivity.showAlert(str2, str4);
                            return;
                        } else {
                            if (chatActivity.getParentActivity() == null) {
                                return;
                            }
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(chatActivity.getParentActivity());
                            builder2.setTitle(str2);
                            builder2.setPositiveButton(LocaleController.getString(R.string.OK), null);
                            builder2.setMessage(tL_messages_botCallbackAnswer.message);
                            builder = builder2;
                        }
                    } else {
                        if (tL_messages_botCallbackAnswer.url == null || chatActivity.getParentActivity() == null) {
                            return;
                        }
                        TLRPC.User user2 = getMessagesController().getUser(Long.valueOf(fromChatId));
                        boolean z4 = user2 != null && user2.verified;
                        if (keyboardButton instanceof TLRPC.TL_keyboardButtonGame) {
                            TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
                            TLRPC.TL_game tL_game = messageMedia instanceof TLRPC.TL_messageMediaGame ? messageMedia.game : null;
                            if (tL_game == null) {
                                return;
                            }
                            String str5 = tL_messages_botCallbackAnswer.url;
                            if (!z4) {
                                if (MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("askgame_" + fromChatId, true)) {
                                    z3 = true;
                                }
                            }
                            chatActivity.showOpenGameAlert(tL_game, messageObject, str5, z3, fromChatId);
                            return;
                        }
                        str3 = tL_messages_botCallbackAnswer.url;
                    }
                } else if (tLObject instanceof TLRPC.TL_urlAuthResultRequest) {
                    chatActivity.showRequestUrlAlert((TLRPC.TL_urlAuthResultRequest) tLObject, (TLRPC.TL_messages_requestUrlAuth) tLObjectArr[0], keyboardButton.url, false);
                    return;
                } else {
                    if (!(tLObject instanceof TLRPC.TL_urlAuthResultAccepted)) {
                        if (tLObject instanceof TLRPC.TL_urlAuthResultDefault) {
                            AlertsCreator.showOpenUrlAlert(chatActivity, keyboardButton.url, false, true);
                            return;
                        }
                        return;
                    }
                    str3 = ((TLRPC.TL_urlAuthResultAccepted) tLObject).url;
                }
                AlertsCreator.showOpenUrlAlert(chatActivity, str3, false, false);
                return;
            }
            str2 = null;
            if (str2 == null) {
            }
            if (keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth) {
            }
            AlertsCreator.showOpenUrlAlert(chatActivity, str3, false, false);
            return;
            chatActivity.showDialog(create);
        }
        if (tL_error == null || chatActivity.getParentActivity() == null) {
            return;
        }
        if ("PASSWORD_HASH_INVALID".equals(tL_error.text)) {
            if (inputCheckPasswordSRP == null) {
                AlertDialog.Builder builder3 = new AlertDialog.Builder(chatActivity.getParentActivity());
                builder3.setTitle(LocaleController.getString(R.string.BotOwnershipTransfer));
                builder3.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BotOwnershipTransferReadyAlertText", R.string.BotOwnershipTransferReadyAlertText, new Object[0])));
                builder3.setPositiveButton(LocaleController.getString(R.string.BotOwnershipTransferChangeOwner), new DialogInterface.OnClickListener() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda65
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        SendMessagesHelper.this.lambda$sendCallback$28(z2, messageObject, keyboardButton, chatActivity, dialogInterface, i2);
                    }
                });
                builder3.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                create = builder3.create();
                chatActivity.showDialog(create);
            }
            return;
        }
        if (!"PASSWORD_MISSING".equals(tL_error.text) && !tL_error.text.startsWith("PASSWORD_TOO_FRESH_") && !tL_error.text.startsWith("SESSION_TOO_FRESH_")) {
            if ("SRP_ID_INVALID".equals(tL_error.text)) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda66
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC.TL_error tL_error2) {
                        SendMessagesHelper.this.lambda$sendCallback$31(twoStepVerificationActivity, z2, messageObject, keyboardButton, chatActivity, tLObject2, tL_error2);
                    }
                }, 8);
                return;
            } else {
                if (twoStepVerificationActivity != null) {
                    twoStepVerificationActivity.needHideProgress();
                    twoStepVerificationActivity.lambda$onBackPressed$321();
                    return;
                }
                return;
            }
        }
        if (twoStepVerificationActivity != null) {
            twoStepVerificationActivity.needHideProgress();
        }
        AlertDialog.Builder builder4 = new AlertDialog.Builder(chatActivity.getParentActivity());
        builder4.setTitle(LocaleController.getString(R.string.EditAdminTransferAlertTitle));
        LinearLayout linearLayout = new LinearLayout(chatActivity.getParentActivity());
        linearLayout.setPadding(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(24.0f), 0);
        linearLayout.setOrientation(1);
        builder4.setView(linearLayout);
        TextView textView = new TextView(chatActivity.getParentActivity());
        int i2 = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i2));
        textView.setTextSize(1, 16.0f);
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("BotOwnershipTransferAlertText", R.string.BotOwnershipTransferAlertText, new Object[0])));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2));
        LinearLayout linearLayout2 = new LinearLayout(chatActivity.getParentActivity());
        linearLayout2.setOrientation(0);
        linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2, 0.0f, 11.0f, 0.0f, 0.0f));
        ImageView imageView = new ImageView(chatActivity.getParentActivity());
        int i3 = R.drawable.list_circle;
        imageView.setImageResource(i3);
        imageView.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(11.0f) : 0, AndroidUtilities.dp(9.0f), LocaleController.isRTL ? 0 : AndroidUtilities.dp(11.0f), 0);
        int color = Theme.getColor(i2);
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
        imageView.setColorFilter(new PorterDuffColorFilter(color, mode));
        TextView textView2 = new TextView(chatActivity.getParentActivity());
        textView2.setTextColor(Theme.getColor(i2));
        textView2.setTextSize(1, 16.0f);
        textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        textView2.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.EditAdminTransferAlertText1)));
        if (LocaleController.isRTL) {
            linearLayout2.addView(textView2, LayoutHelper.createLinear(-1, -2));
            linearLayout2.addView(imageView, LayoutHelper.createLinear(-2, -2, 5));
        } else {
            linearLayout2.addView(imageView, LayoutHelper.createLinear(-2, -2));
            linearLayout2.addView(textView2, LayoutHelper.createLinear(-1, -2));
        }
        LinearLayout linearLayout3 = new LinearLayout(chatActivity.getParentActivity());
        linearLayout3.setOrientation(0);
        linearLayout.addView(linearLayout3, LayoutHelper.createLinear(-1, -2, 0.0f, 11.0f, 0.0f, 0.0f));
        ImageView imageView2 = new ImageView(chatActivity.getParentActivity());
        imageView2.setImageResource(i3);
        imageView2.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(11.0f) : 0, AndroidUtilities.dp(9.0f), LocaleController.isRTL ? 0 : AndroidUtilities.dp(11.0f), 0);
        imageView2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(i2), mode));
        TextView textView3 = new TextView(chatActivity.getParentActivity());
        textView3.setTextColor(Theme.getColor(i2));
        textView3.setTextSize(1, 16.0f);
        textView3.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        textView3.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.EditAdminTransferAlertText2)));
        if (LocaleController.isRTL) {
            linearLayout3.addView(textView3, LayoutHelper.createLinear(-1, -2));
            linearLayout3.addView(imageView2, LayoutHelper.createLinear(-2, -2, 5));
        } else {
            linearLayout3.addView(imageView2, LayoutHelper.createLinear(-2, -2));
            linearLayout3.addView(textView3, LayoutHelper.createLinear(-1, -2));
        }
        if ("PASSWORD_MISSING".equals(tL_error.text)) {
            builder4.setPositiveButton(LocaleController.getString(R.string.EditAdminTransferSetPassword), new DialogInterface.OnClickListener() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda67
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i4) {
                    SendMessagesHelper.lambda$sendCallback$29(ChatActivity.this, dialogInterface, i4);
                }
            });
            i = R.string.Cancel;
        } else {
            TextView textView4 = new TextView(chatActivity.getParentActivity());
            textView4.setTextColor(Theme.getColor(i2));
            textView4.setTextSize(1, 16.0f);
            textView4.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            textView4.setText(LocaleController.getString(R.string.EditAdminTransferAlertText3));
            linearLayout.addView(textView4, LayoutHelper.createLinear(-1, -2, 0.0f, 11.0f, 0.0f, 0.0f));
            i = R.string.OK;
        }
        builder4.setNegativeButton(LocaleController.getString(i), null);
        builder = builder4;
        create = builder.create();
        chatActivity.showDialog(create);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendCallback$33(final String str, final List list, final boolean z, final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton, final ChatActivity chatActivity, final TwoStepVerificationActivity twoStepVerificationActivity, final TLObject[] tLObjectArr, final TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP, final boolean z2, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$sendCallback$32(str, list, z, tLObject, messageObject, keyboardButton, chatActivity, twoStepVerificationActivity, tLObjectArr, tL_error, inputCheckPasswordSRP, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendGame$34(long j, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        }
        if (j != 0) {
            getMessagesStorage().removePendingTask(j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$10(TLRPC.Message message, long j, int i, TLRPC.Message message2, int i2, int i3) {
        message.send_state = 0;
        getMediaDataController().increasePeerRaiting(j);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer, Integer.valueOf(i), Integer.valueOf(message2.id), message2, Long.valueOf(j), 0L, Integer.valueOf(i2), Boolean.valueOf(i3 != 0));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageReceivedByServer2, Integer.valueOf(i), Integer.valueOf(message2.id), message2, Long.valueOf(j), 0L, Integer.valueOf(i2), Boolean.valueOf(i3 != 0));
        processSentMessage(i);
        removeFromSendingMessages(i, i3 != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$11(final int i, final TLRPC.Message message, final TLRPC.Message message2, TLRPC.Peer peer, final int i2, ArrayList arrayList, final long j, final int i3) {
        int i4 = (message.quick_reply_shortcut_id == 0 && message.quick_reply_shortcut == null) ? i != 0 ? 1 : 0 : 5;
        getMessagesStorage().updateMessageStateAndId(message2.random_id, MessageObject.getPeerId(peer), Integer.valueOf(i2), message2.id, 0, false, i != 0 ? 1 : 0, message.quick_reply_shortcut_id);
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) arrayList, true, false, false, 0, i4, message.quick_reply_shortcut_id);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$sendMessage$10(message2, j, i2, message, i3, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$12(TLRPC.TL_error tL_error, TLRPC.TL_messages_forwardMessages tL_messages_forwardMessages) {
        AlertsCreator.processError(this.currentAccount, tL_error, null, tL_messages_forwardMessages, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$13(TLRPC.Message message, int i) {
        message.send_state = 2;
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(message.id));
        processSentMessage(message.id);
        removeFromSendingMessages(message.id, i != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:39:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0119  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0128  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x013b  */
    /* JADX WARN: Type inference failed for: r9v0 */
    /* JADX WARN: Type inference failed for: r9v4, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r9v7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$sendMessage$14(final long j, final int i, boolean z, boolean z2, LongSparseArray longSparseArray, ArrayList arrayList, ArrayList arrayList2, final MessageObject messageObject, final TLRPC.Peer peer, final TLRPC.TL_messages_forwardMessages tL_messages_forwardMessages, TLObject tLObject, final TLRPC.TL_error tL_error) {
        int i2;
        int i3;
        TLRPC.Message message;
        TLRPC.Message message2;
        int i4;
        int i5;
        long j2;
        TLRPC.Updates updates;
        SparseLongArray sparseLongArray;
        int i6;
        final TLRPC.Message message3;
        int indexOf;
        int i7 = i;
        ArrayList arrayList3 = arrayList2;
        int i8 = -1;
        ?? r9 = 0;
        if (tL_error == null) {
            SparseLongArray sparseLongArray2 = new SparseLongArray();
            TLRPC.Updates updates2 = (TLRPC.Updates) tLObject;
            int i9 = 0;
            while (i9 < updates2.updates.size()) {
                TLRPC.Update update = updates2.updates.get(i9);
                if (update instanceof TLRPC.TL_updateMessageID) {
                    TLRPC.TL_updateMessageID tL_updateMessageID = (TLRPC.TL_updateMessageID) update;
                    sparseLongArray2.put(tL_updateMessageID.id, tL_updateMessageID.random_id);
                    updates2.updates.remove(i9);
                    i9--;
                }
                i9++;
            }
            getNotificationCenter().postNotificationNameOnUIThread(NotificationCenter.savedMessagesForwarded, sparseLongArray2);
            Integer num = getMessagesController().dialogs_read_outbox_max.get(Long.valueOf(j));
            if (num == null) {
                num = Integer.valueOf(getMessagesStorage().getDialogReadMax(true, j));
                getMessagesController().dialogs_read_outbox_max.put(Long.valueOf(j), num);
            }
            Integer num2 = num;
            int i10 = 0;
            int i11 = 0;
            while (i10 < updates2.updates.size()) {
                TLRPC.Update update2 = updates2.updates.get(i10);
                boolean z3 = update2 instanceof TLRPC.TL_updateNewMessage;
                if (z3 || (update2 instanceof TLRPC.TL_updateNewChannelMessage) || (update2 instanceof TLRPC.TL_updateNewScheduledMessage) || (update2 instanceof TLRPC.TL_updateQuickReplyMessage)) {
                    int i12 = i7 != 0 ? 1 : 0;
                    updates2.updates.remove(i10);
                    int i13 = i10 - 1;
                    if (z3) {
                        TLRPC.TL_updateNewMessage tL_updateNewMessage = (TLRPC.TL_updateNewMessage) update2;
                        message2 = tL_updateNewMessage.message;
                        getMessagesController().processNewDifferenceParams(i8, tL_updateNewMessage.pts, i8, tL_updateNewMessage.pts_count);
                    } else if (update2 instanceof TLRPC.TL_updateNewScheduledMessage) {
                        message = ((TLRPC.TL_updateNewScheduledMessage) update2).message;
                        i2 = i12;
                        i3 = i11;
                        i4 = 1;
                        i5 = (z || message.date == 2147483646) ? i4 : 0;
                        ImageLoader.saveMessageThumbs(message);
                        if (i5 == 0) {
                            message.unread = num2.intValue() < message.id;
                        }
                        if (z2) {
                            message.out = true;
                            message.unread = r9;
                            message.media_unread = r9;
                        }
                        j2 = sparseLongArray2.get(message.id);
                        if (j2 != 0 || (message3 = (TLRPC.Message) longSparseArray.get(j2)) == null || (indexOf = arrayList.indexOf(message3)) == -1) {
                            updates = updates2;
                            sparseLongArray = sparseLongArray2;
                            i11 = i3;
                            i10 = i13;
                        } else {
                            MessageObject messageObject2 = (MessageObject) arrayList3.get(indexOf);
                            arrayList.remove(indexOf);
                            arrayList3.remove(indexOf);
                            final int i14 = message3.id;
                            final ArrayList arrayList4 = new ArrayList();
                            arrayList4.add(message);
                            TLRPC.Message message4 = messageObject2.messageOwner;
                            message4.post_author = message.post_author;
                            if ((message.flags & ConnectionsManager.FileTypeVideo) != 0) {
                                message4.ttl_period = message.ttl_period;
                                message4.flags |= ConnectionsManager.FileTypeVideo;
                            }
                            final int i15 = i2;
                            updateMediaPaths(messageObject2, message, message.id, null, true);
                            final int mediaExistanceFlags = messageObject2.getMediaExistanceFlags();
                            message3.id = message.id;
                            int i16 = i3 + 1;
                            if (i15 != i5) {
                                final int i17 = i5;
                                updates = updates2;
                                final TLRPC.Message message5 = message;
                                sparseLongArray = sparseLongArray2;
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda48
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.this.lambda$sendMessage$9(arrayList4, i17, i14, message3, i15, message5, messageObject, i);
                                    }
                                });
                            } else {
                                updates = updates2;
                                sparseLongArray = sparseLongArray2;
                                final TLRPC.Message message6 = message;
                                getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda49
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.this.lambda$sendMessage$11(i, message6, message3, peer, i14, arrayList4, j, mediaExistanceFlags);
                                    }
                                });
                            }
                            i10 = i13;
                            i11 = i16;
                        }
                        i6 = 1;
                    } else if (update2 instanceof TLRPC.TL_updateQuickReplyMessage) {
                        QuickRepliesController.getInstance(this.currentAccount).processUpdate(update2, null, r9);
                        message2 = ((TLRPC.TL_updateQuickReplyMessage) update2).message;
                    } else {
                        TLRPC.TL_updateNewChannelMessage tL_updateNewChannelMessage = (TLRPC.TL_updateNewChannelMessage) update2;
                        TLRPC.Message message7 = tL_updateNewChannelMessage.message;
                        i2 = i12;
                        i3 = i11;
                        getMessagesController().processNewChannelDifferenceParams(tL_updateNewChannelMessage.pts, tL_updateNewChannelMessage.pts_count, message7.peer_id.channel_id);
                        message = message7;
                        i4 = 0;
                        if (z) {
                        }
                        ImageLoader.saveMessageThumbs(message);
                        if (i5 == 0) {
                        }
                        if (z2) {
                        }
                        j2 = sparseLongArray2.get(message.id);
                        if (j2 != 0) {
                        }
                        updates = updates2;
                        sparseLongArray = sparseLongArray2;
                        i11 = i3;
                        i10 = i13;
                        i6 = 1;
                    }
                    message = message2;
                    i2 = i12;
                    i3 = i11;
                    i4 = 0;
                    if (z) {
                    }
                    ImageLoader.saveMessageThumbs(message);
                    if (i5 == 0) {
                    }
                    if (z2) {
                    }
                    j2 = sparseLongArray2.get(message.id);
                    if (j2 != 0) {
                    }
                    updates = updates2;
                    sparseLongArray = sparseLongArray2;
                    i11 = i3;
                    i10 = i13;
                    i6 = 1;
                } else {
                    updates = updates2;
                    sparseLongArray = sparseLongArray2;
                    i6 = 1;
                }
                i10 += i6;
                i7 = i;
                arrayList3 = arrayList2;
                updates2 = updates;
                sparseLongArray2 = sparseLongArray;
                r9 = 0;
                i8 = -1;
            }
            int i18 = i11;
            TLRPC.Updates updates3 = updates2;
            if (!updates3.updates.isEmpty()) {
                getMessagesController().processUpdates(updates3, false);
            }
            getStatsController().incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, i18);
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda50
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.lambda$sendMessage$12(tL_error, tL_messages_forwardMessages);
                }
            });
        }
        for (int i19 = 0; i19 < arrayList.size(); i19++) {
            final TLRPC.Message message8 = (TLRPC.Message) arrayList.get(i19);
            getMessagesStorage().markMessageAsSendError(message8, i != 0 ? 1 : 0);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.lambda$sendMessage$13(message8, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$7(int i, TLRPC.Message message, int i2, int i3, TLRPC.Message message2, MessageObject messageObject, int i4) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(Integer.valueOf(i));
        getMessagesController().deleteMessages(arrayList, null, null, message.dialog_id, false, i2, false, 0L, null, 0, i3 == 1, message2.id);
        ArrayList<MessageObject> arrayList2 = new ArrayList<>();
        arrayList2.add(new MessageObject(messageObject.currentAccount, messageObject.messageOwner, true, true));
        getMessagesController().updateInterfaceWithMessages(message.dialog_id, arrayList2, i3);
        getMediaDataController().increasePeerRaiting(message.dialog_id);
        processSentMessage(i);
        removeFromSendingMessages(i, i4 != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$8(ArrayList arrayList, final int i, final int i2, final TLRPC.Message message, final int i3, final TLRPC.Message message2, final MessageObject messageObject, final int i4) {
        getMessagesStorage().putMessages((ArrayList<TLRPC.Message>) arrayList, true, false, false, 0, i, 0L);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda68
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$sendMessage$7(i2, message, i3, i, message2, messageObject, i4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessage$9(final ArrayList arrayList, final int i, final int i2, final TLRPC.Message message, final int i3, final TLRPC.Message message2, final MessageObject messageObject, final int i4) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda69
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$sendMessage$8(arrayList, i, i2, message, i3, message2, messageObject, i4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendNotificationCallback$17(String str, List list) {
        this.waitingForCallback.remove(str);
        list.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendNotificationCallback$18(final String str, final List list, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$sendNotificationCallback$17(str, list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendNotificationCallback$19(long j, int i, byte[] bArr) {
        TLRPC.Chat chatSync;
        TLRPC.User userSync;
        final String str = j + "_" + i + "_" + Utilities.bytesToHex(bArr) + "_0";
        this.waitingForCallback.put(str, Boolean.TRUE);
        final List<String> list = this.waitingForCallbackMap.get(j + "_" + i);
        if (list == null) {
            ArrayList arrayList = new ArrayList();
            this.waitingForCallbackMap.put(j + "_" + i, arrayList);
            list = arrayList;
        }
        list.add(str);
        if (!DialogObject.isUserDialog(j)) {
            long j2 = -j;
            if (getMessagesController().getChat(Long.valueOf(j2)) == null && (chatSync = getMessagesStorage().getChatSync(j2)) != null) {
                getMessagesController().putChat(chatSync, true);
            }
        } else if (getMessagesController().getUser(Long.valueOf(j)) == null && (userSync = getMessagesStorage().getUserSync(j)) != null) {
            getMessagesController().putUser(userSync, true);
        }
        TLRPC.TL_messages_getBotCallbackAnswer tL_messages_getBotCallbackAnswer = new TLRPC.TL_messages_getBotCallbackAnswer();
        tL_messages_getBotCallbackAnswer.peer = getMessagesController().getInputPeer(j);
        tL_messages_getBotCallbackAnswer.msg_id = i;
        tL_messages_getBotCallbackAnswer.game = false;
        if (bArr != null) {
            tL_messages_getBotCallbackAnswer.flags |= 1;
            tL_messages_getBotCallbackAnswer.data = bArr;
        }
        getConnectionsManager().sendRequest(tL_messages_getBotCallbackAnswer, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda90
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.lambda$sendNotificationCallback$18(str, list, tLObject, tL_error);
            }
        }, 2);
        getMessagesController().markDialogAsRead(j, i, i, 0, false, 0L, 0, true, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendReaction$22(Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject != null) {
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
            if (runnable != null) {
                AndroidUtilities.runOnUIThread(runnable);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSticker$5(Bitmap[] bitmapArr, String[] strArr, TLRPC.Document document, long j, MessageObject messageObject, MessageObject messageObject2, boolean z, int i, Object obj, MessageObject.SendAnimationData sendAnimationData, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, String str, int i2) {
        if (bitmapArr[0] != null && strArr[0] != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmapArr[0]), strArr[0], false);
        }
        SendMessageParams of = SendMessageParams.of((TLRPC.TL_document) document, null, null, j, messageObject, messageObject2, null, null, null, null, z, i, 0, obj, sendAnimationData, false);
        of.replyToStoryItem = storyItem;
        of.replyQuote = replyQuote;
        of.quick_reply_shortcut = str;
        of.quick_reply_shortcut_id = i2;
        sendMessage(of);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSticker$6(final TLRPC.Document document, final long j, final MessageObject messageObject, final MessageObject messageObject2, final boolean z, final int i, final Object obj, final MessageObject.SendAnimationData sendAnimationData, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final String str, final int i2) {
        final Bitmap[] bitmapArr = new Bitmap[1];
        String key = ImageLocation.getForDocument(document).getKey(null, null, false);
        String str2 = "video/mp4".equals(document.mime_type) ? ".mp4" : "video/x-matroska".equals(document.mime_type) ? ".mkv" : "";
        File file = new File(FileLoader.getDirectory(3), key + str2);
        if (!file.exists()) {
            file = new File(FileLoader.getDirectory(2), key + str2);
        }
        ensureMediaThumbExists(getAccountInstance(), false, document, file.getAbsolutePath(), null, 0L);
        final String[] strArr = {getKeyForPhotoSize(getAccountInstance(), FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 320), bitmapArr, true, true)};
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$sendSticker$5(bitmapArr, strArr, document, j, messageObject, messageObject2, z, i, obj, sendAnimationData, storyItem, replyQuote, str, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendVote$20(String str, Runnable runnable) {
        this.waitingForVote.remove(str);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendVote$21(MessageObject messageObject, final String str, final Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            this.voteSendTime.put(messageObject.getPollId(), 0L);
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
            this.voteSendTime.put(messageObject.getPollId(), Long.valueOf(SystemClock.elapsedRealtime()));
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda71
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$sendVote$20(str, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00c8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$uploadMultiMedia$37(TLObject tLObject, TLRPC.InputMedia inputMedia, DelayedMessage delayedMessage) {
        TLRPC.TL_inputMediaDocument tL_inputMediaDocument;
        String str;
        TLRPC.TL_inputMediaDocument tL_inputMediaDocument2;
        if (tLObject != null) {
            TLRPC.MessageMedia messageMedia = (TLRPC.MessageMedia) tLObject;
            if ((inputMedia instanceof TLRPC.TL_inputMediaUploadedPhoto) && (messageMedia instanceof TLRPC.TL_messageMediaPhoto)) {
                TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto = new TLRPC.TL_inputMediaPhoto();
                TLRPC.TL_inputPhoto tL_inputPhoto = new TLRPC.TL_inputPhoto();
                tL_inputMediaPhoto.id = tL_inputPhoto;
                TLRPC.Photo photo = messageMedia.photo;
                tL_inputPhoto.id = photo.id;
                tL_inputPhoto.access_hash = photo.access_hash;
                tL_inputPhoto.file_reference = photo.file_reference;
                tL_inputMediaPhoto.spoiler = inputMedia.spoiler;
                tL_inputMediaDocument = tL_inputMediaPhoto;
                if (BuildVars.DEBUG_VERSION) {
                    str = "set uploaded photo";
                    tL_inputMediaDocument2 = tL_inputMediaPhoto;
                    FileLog.d(str);
                    tL_inputMediaDocument = tL_inputMediaDocument2;
                }
            } else if ((inputMedia instanceof TLRPC.TL_inputMediaUploadedDocument) && (messageMedia instanceof TLRPC.TL_messageMediaDocument)) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument3 = new TLRPC.TL_inputMediaDocument();
                TLRPC.TL_inputDocument tL_inputDocument = new TLRPC.TL_inputDocument();
                tL_inputMediaDocument3.id = tL_inputDocument;
                TLRPC.Document document = messageMedia.document;
                tL_inputDocument.id = document.id;
                tL_inputDocument.access_hash = document.access_hash;
                tL_inputDocument.file_reference = document.file_reference;
                tL_inputMediaDocument3.spoiler = inputMedia.spoiler;
                tL_inputMediaDocument = tL_inputMediaDocument3;
                if (BuildVars.DEBUG_VERSION) {
                    str = "set uploaded document";
                    tL_inputMediaDocument2 = tL_inputMediaDocument3;
                    FileLog.d(str);
                    tL_inputMediaDocument = tL_inputMediaDocument2;
                }
            }
            if (tL_inputMediaDocument != null) {
                delayedMessage.markAsError();
                return;
            }
            int i = inputMedia.ttl_seconds;
            if (i != 0) {
                tL_inputMediaDocument.ttl_seconds = i;
                tL_inputMediaDocument.flags |= 1;
            }
            TLObject tLObject2 = delayedMessage.sendRequest;
            if (tLObject2 instanceof TLRPC.TL_messages_sendMultiMedia) {
                TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) tLObject2;
                int i2 = 0;
                while (true) {
                    if (i2 >= tL_messages_sendMultiMedia.multi_media.size()) {
                        break;
                    }
                    if (tL_messages_sendMultiMedia.multi_media.get(i2).media == inputMedia) {
                        tL_messages_sendMultiMedia.multi_media.get(i2).media = tL_inputMediaDocument;
                        break;
                    }
                    i2++;
                }
            } else if (tLObject2 instanceof TLRPC.TL_messages_sendMedia) {
                TLRPC.InputMedia inputMedia2 = ((TLRPC.TL_messages_sendMedia) tLObject2).media;
                if (inputMedia2 instanceof TLRPC.TL_inputMediaPaidMedia) {
                    TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) inputMedia2;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= tL_inputMediaPaidMedia.extended_media.size()) {
                            break;
                        }
                        if (tL_inputMediaPaidMedia.extended_media.get(i3) == inputMedia) {
                            tL_inputMediaPaidMedia.extended_media.set(i3, tL_inputMediaDocument);
                            break;
                        }
                        i3++;
                    }
                }
            }
            sendReadyToSendGroup(delayedMessage, false, true);
            return;
        }
        tL_inputMediaDocument = null;
        if (tL_inputMediaDocument != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$uploadMultiMedia$38(final TLRPC.InputMedia inputMedia, final DelayedMessage delayedMessage, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$uploadMultiMedia$37(tLObject, inputMedia, delayedMessage);
            }
        });
    }

    private void performSendDelayedMessage(DelayedMessage delayedMessage) {
        performSendDelayedMessage(delayedMessage, -1);
    }

    private void performSendDelayedMessage(final DelayedMessage delayedMessage, int i) {
        boolean z;
        TLRPC.Document document;
        ArrayList arrayList;
        Object obj;
        boolean z2;
        String str;
        FileLoader fileLoader;
        long j;
        int i2;
        boolean z3;
        boolean z4;
        TLRPC.InputMedia inputMedia;
        String str2;
        FileLoader fileLoader2;
        TLRPC.PhotoSize photoSize;
        VideoEditedInfo videoEditedInfo;
        int i3 = delayedMessage.type;
        int i4 = ConnectionsManager.FileTypePhoto;
        if (i3 == 0) {
            String str3 = delayedMessage.httpLocation;
            if (str3 != null) {
                putToDelayedMessages(str3, delayedMessage);
                ImageLoader.getInstance().loadHttpFile(delayedMessage.httpLocation, "file", this.currentAccount);
                return;
            }
            if (delayedMessage.sendRequest == null) {
                String file = FileLoader.getInstance(this.currentAccount).getPathToAttach(delayedMessage.photoSize).toString();
                if (delayedMessage.sendEncryptedRequest != null && delayedMessage.photoSize.location.dc_id != 0) {
                    File file2 = new File(file);
                    if (!file2.exists()) {
                        file = FileLoader.getInstance(this.currentAccount).getPathToAttach(delayedMessage.photoSize, true).toString();
                        file2 = new File(file);
                    }
                    if (!file2.exists()) {
                        putToDelayedMessages(FileLoader.getAttachFileName(delayedMessage.photoSize), delayedMessage);
                        getFileLoader().loadFile(ImageLocation.getForObject(delayedMessage.photoSize, delayedMessage.locationParent), delayedMessage.parentObject, "jpg", 3, 0);
                        return;
                    }
                }
                putToDelayedMessages(file, delayedMessage);
                getFileLoader().uploadFile(file, true, true, ConnectionsManager.FileTypePhoto);
                putToUploadingMessages(delayedMessage.obj);
            }
            str2 = FileLoader.getInstance(this.currentAccount).getPathToAttach(delayedMessage.photoSize).toString();
            putToDelayedMessages(str2, delayedMessage);
            fileLoader2 = getFileLoader();
        } else {
            if (i3 == 1) {
                VideoEditedInfo videoEditedInfo2 = delayedMessage.videoEditedInfo;
                if (videoEditedInfo2 == null || !videoEditedInfo2.needConvert()) {
                    VideoEditedInfo videoEditedInfo3 = delayedMessage.videoEditedInfo;
                    if (videoEditedInfo3 != null) {
                        TLRPC.InputFile inputFile = videoEditedInfo3.file;
                        if (inputFile != null) {
                            TLObject tLObject = delayedMessage.sendRequest;
                            (tLObject instanceof TLRPC.TL_messages_sendMedia ? ((TLRPC.TL_messages_sendMedia) tLObject).media : ((TLRPC.TL_messages_editMessage) tLObject).media).file = inputFile;
                            videoEditedInfo3.file = null;
                        } else if (videoEditedInfo3.encryptedFile != null) {
                            TLRPC.TL_decryptedMessage tL_decryptedMessage = (TLRPC.TL_decryptedMessage) delayedMessage.sendEncryptedRequest;
                            TLRPC.DecryptedMessageMedia decryptedMessageMedia = tL_decryptedMessage.media;
                            decryptedMessageMedia.size = videoEditedInfo3.estimatedSize;
                            decryptedMessageMedia.key = videoEditedInfo3.key;
                            decryptedMessageMedia.iv = videoEditedInfo3.iv;
                            SecretChatHelper secretChatHelper = getSecretChatHelper();
                            MessageObject messageObject = delayedMessage.obj;
                            secretChatHelper.performSendEncryptedRequest(tL_decryptedMessage, messageObject.messageOwner, delayedMessage.encryptedChat, delayedMessage.videoEditedInfo.encryptedFile, delayedMessage.originalPath, messageObject);
                            delayedMessage.videoEditedInfo.encryptedFile = null;
                            return;
                        }
                    }
                    TLObject tLObject2 = delayedMessage.sendRequest;
                    if (tLObject2 != null) {
                        TLRPC.InputMedia inputMedia2 = tLObject2 instanceof TLRPC.TL_messages_sendMedia ? ((TLRPC.TL_messages_sendMedia) tLObject2).media : ((TLRPC.TL_messages_editMessage) tLObject2).media;
                        if (inputMedia2 instanceof TLRPC.TL_inputMediaPaidMedia) {
                            TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) inputMedia2;
                            if (!tL_inputMediaPaidMedia.extended_media.isEmpty()) {
                                inputMedia2 = tL_inputMediaPaidMedia.extended_media.get(0);
                            }
                        }
                        if (inputMedia2.file == null) {
                            MessageObject messageObject2 = delayedMessage.obj;
                            String str4 = messageObject2.messageOwner.attachPath;
                            TLRPC.Document document2 = messageObject2.getDocument();
                            if (str4 == null) {
                                str4 = FileLoader.getDirectory(4) + "/" + document2.id + ".mp4";
                            }
                            String str5 = str4;
                            putToDelayedMessages(str5, delayedMessage);
                            VideoEditedInfo videoEditedInfo4 = delayedMessage.obj.videoEditedInfo;
                            if (videoEditedInfo4 == null || !videoEditedInfo4.notReadyYet) {
                                if (videoEditedInfo4 == null || !videoEditedInfo4.needConvert()) {
                                    getFileLoader().uploadFile(str5, false, false, ConnectionsManager.FileTypeVideo);
                                } else {
                                    getFileLoader().uploadFile(str5, false, false, document2.size, ConnectionsManager.FileTypeVideo, false);
                                }
                            }
                        } else {
                            MessageObject messageObject3 = delayedMessage.obj;
                            str2 = FileLoader.getDirectory(4) + "/" + delayedMessage.photoSize.location.volume_id + "_" + delayedMessage.photoSize.location.local_id + "." + ((messageObject3 == null || (videoEditedInfo = messageObject3.videoEditedInfo) == null || !videoEditedInfo.isSticker) ? "jpg" : "webp");
                            putToDelayedMessages(str2, delayedMessage);
                            fileLoader2 = getFileLoader();
                        }
                    } else {
                        MessageObject messageObject4 = delayedMessage.obj;
                        String str6 = messageObject4.messageOwner.attachPath;
                        TLRPC.Document document3 = messageObject4.getDocument();
                        if (str6 == null) {
                            str6 = FileLoader.getDirectory(4) + "/" + document3.id + ".mp4";
                        }
                        if (delayedMessage.sendEncryptedRequest != null && document3.dc_id != 0) {
                            File file3 = new File(str6);
                            if (!file3.exists() && (file3 = getFileLoader().getPathToMessage(delayedMessage.obj.messageOwner)) != null && file3.exists()) {
                                TLRPC.Message message = delayedMessage.obj.messageOwner;
                                String absolutePath = file3.getAbsolutePath();
                                message.attachPath = absolutePath;
                                delayedMessage.obj.attachPathExists = true;
                                str6 = absolutePath;
                            }
                            if ((file3 == null || (!file3.exists() && delayedMessage.obj.getDocument() != null)) && (file3 = getFileLoader().getPathToAttach(delayedMessage.obj.getDocument(), false)) != null && file3.exists()) {
                                TLRPC.Message message2 = delayedMessage.obj.messageOwner;
                                String absolutePath2 = file3.getAbsolutePath();
                                message2.attachPath = absolutePath2;
                                delayedMessage.obj.attachPathExists = true;
                                str6 = absolutePath2;
                            }
                            if (file3 == null || !file3.exists()) {
                                putToDelayedMessages(FileLoader.getAttachFileName(document3), delayedMessage);
                                getFileLoader().loadFile(document3, delayedMessage.parentObject, 3, 0);
                                return;
                            }
                        }
                        String str7 = str6;
                        putToDelayedMessages(str7, delayedMessage);
                        VideoEditedInfo videoEditedInfo5 = delayedMessage.obj.videoEditedInfo;
                        if (videoEditedInfo5 == null || !videoEditedInfo5.notReadyYet) {
                            if (videoEditedInfo5 == null || !videoEditedInfo5.needConvert()) {
                                getFileLoader().uploadFile(str7, true, false, ConnectionsManager.FileTypeVideo);
                            } else {
                                getFileLoader().uploadFile(str7, true, false, document3.size, ConnectionsManager.FileTypeVideo, false);
                            }
                        }
                    }
                } else {
                    MessageObject messageObject5 = delayedMessage.obj;
                    String str8 = messageObject5.messageOwner.attachPath;
                    TLRPC.Document document4 = messageObject5.getDocument();
                    if (str8 == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(FileLoader.getDirectory(4));
                        sb.append("/");
                        sb.append(document4.id);
                        sb.append(".");
                        sb.append(delayedMessage.videoEditedInfo.isSticker ? "webm" : "mp4");
                        str8 = sb.toString();
                    }
                    putToDelayedMessages(str8, delayedMessage);
                    if (!delayedMessage.videoEditedInfo.alreadyScheduledConverting) {
                        MediaController.getInstance().scheduleVideoConvert(delayedMessage.obj);
                    }
                }
                putToUploadingMessages(delayedMessage.obj);
            }
            if (i3 == 2) {
                String str9 = delayedMessage.httpLocation;
                if (str9 != null) {
                    putToDelayedMessages(str9, delayedMessage);
                    ImageLoader.getInstance().loadHttpFile(delayedMessage.httpLocation, "gif", this.currentAccount);
                    return;
                }
                TLObject tLObject3 = delayedMessage.sendRequest;
                if (tLObject3 != null) {
                    TLRPC.InputMedia inputMedia3 = tLObject3 instanceof TLRPC.TL_messages_sendMedia ? ((TLRPC.TL_messages_sendMedia) tLObject3).media : ((TLRPC.TL_messages_editMessage) tLObject3).media;
                    if (inputMedia3.file == null) {
                        String str10 = delayedMessage.obj.messageOwner.attachPath;
                        putToDelayedMessages(str10, delayedMessage);
                        getFileLoader().uploadFile(str10, delayedMessage.sendRequest == null, false, ConnectionsManager.FileTypeFile);
                    } else {
                        if (inputMedia3.thumb != null || (photoSize = delayedMessage.photoSize) == null || (photoSize instanceof TLRPC.TL_photoStrippedSize)) {
                            return;
                        }
                        str2 = FileLoader.getDirectory(4) + "/" + delayedMessage.photoSize.location.volume_id + "_" + delayedMessage.photoSize.location.local_id + ".jpg";
                        putToDelayedMessages(str2, delayedMessage);
                        fileLoader2 = getFileLoader();
                    }
                } else {
                    MessageObject messageObject6 = delayedMessage.obj;
                    String str11 = messageObject6.messageOwner.attachPath;
                    TLRPC.Document document5 = messageObject6.getDocument();
                    if (delayedMessage.sendEncryptedRequest != null && document5.dc_id != 0) {
                        File file4 = new File(str11);
                        if (!file4.exists() && (file4 = getFileLoader().getPathToMessage(delayedMessage.obj.messageOwner)) != null && file4.exists()) {
                            TLRPC.Message message3 = delayedMessage.obj.messageOwner;
                            String absolutePath3 = file4.getAbsolutePath();
                            message3.attachPath = absolutePath3;
                            delayedMessage.obj.attachPathExists = true;
                            str11 = absolutePath3;
                        }
                        if ((file4 == null || (!file4.exists() && delayedMessage.obj.getDocument() != null)) && (file4 = getFileLoader().getPathToAttach(delayedMessage.obj.getDocument(), false)) != null && file4.exists()) {
                            TLRPC.Message message4 = delayedMessage.obj.messageOwner;
                            String absolutePath4 = file4.getAbsolutePath();
                            message4.attachPath = absolutePath4;
                            delayedMessage.obj.attachPathExists = true;
                            str11 = absolutePath4;
                        }
                        if (file4 == null || !file4.exists()) {
                            putToDelayedMessages(FileLoader.getAttachFileName(document5), delayedMessage);
                            getFileLoader().loadFile(document5, delayedMessage.parentObject, 3, 0);
                            return;
                        }
                    }
                    putToDelayedMessages(str11, delayedMessage);
                    getFileLoader().uploadFile(str11, true, false, ConnectionsManager.FileTypeFile);
                }
                putToUploadingMessages(delayedMessage.obj);
            }
            if (i3 != 3) {
                if (i3 != 4) {
                    if (i3 == 5) {
                        final String str12 = "stickerset_" + delayedMessage.obj.getId();
                        TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
                        tL_messages_getStickerSet.stickerset = (TLRPC.InputStickerSet) delayedMessage.parentObject;
                        getConnectionsManager().sendRequest(tL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda72
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject4, TLRPC.TL_error tL_error) {
                                SendMessagesHelper.this.lambda$performSendDelayedMessage$36(delayedMessage, str12, tLObject4, tL_error);
                            }
                        });
                        putToDelayedMessages(str12, delayedMessage);
                        return;
                    }
                    return;
                }
                boolean z5 = i < 0;
                if (delayedMessage.performMediaUpload) {
                    int size = i < 0 ? delayedMessage.messageObjects.size() - 1 : i;
                    MessageObject messageObject7 = delayedMessage.messageObjects.get(size);
                    TLRPC.Document document6 = messageObject7.getDocument();
                    if (document6 == null && (MessageObject.getMedia(messageObject7) instanceof TLRPC.TL_messageMediaPaidMedia)) {
                        TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) MessageObject.getMedia(messageObject7);
                        TLRPC.MessageExtendedMedia messageExtendedMedia = size >= tL_messageMediaPaidMedia.extended_media.size() ? null : tL_messageMediaPaidMedia.extended_media.get(size);
                        TLRPC.MessageMedia messageMedia = messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia ? ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media : null;
                        document = messageMedia == null ? null : messageMedia.document;
                    } else {
                        document = document6;
                    }
                    if (document != null) {
                        if (delayedMessage.videoEditedInfo != null) {
                            String str13 = messageObject7.messageOwner.attachPath;
                            if (str13 == null) {
                                str13 = FileLoader.getDirectory(4) + "/" + document.id + ".mp4";
                            }
                            putToDelayedMessages(str13, delayedMessage);
                            delayedMessage.extraHashMap.put(messageObject7, str13);
                            delayedMessage.extraHashMap.put(str13 + "_i", messageObject7);
                            TLRPC.PhotoSize photoSize2 = delayedMessage.photoSize;
                            if (photoSize2 != null && photoSize2.location != null) {
                                delayedMessage.extraHashMap.put(str13 + "_t", delayedMessage.photoSize);
                            }
                            if (!delayedMessage.videoEditedInfo.alreadyScheduledConverting) {
                                MediaController.getInstance().scheduleVideoConvert(messageObject7);
                            }
                            delayedMessage.obj = messageObject7;
                        } else {
                            String str14 = messageObject7.messageOwner.attachPath;
                            if (str14 == null) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(FileLoader.getDirectory(4));
                                sb2.append("/");
                                str = "_t";
                                sb2.append(document.id);
                                sb2.append(".mp4");
                                str14 = sb2.toString();
                            } else {
                                str = "_t";
                            }
                            TLObject tLObject4 = delayedMessage.sendRequest;
                            if (tLObject4 != null) {
                                if (tLObject4 instanceof TLRPC.TL_messages_sendMultiMedia) {
                                    inputMedia = ((TLRPC.TL_messages_sendMultiMedia) tLObject4).multi_media.get(size).media;
                                } else {
                                    if (tLObject4 instanceof TLRPC.TL_messages_sendMedia) {
                                        TLRPC.InputMedia inputMedia4 = ((TLRPC.TL_messages_sendMedia) tLObject4).media;
                                        if (inputMedia4 instanceof TLRPC.TL_inputMediaPaidMedia) {
                                            inputMedia = ((TLRPC.TL_inputMediaPaidMedia) inputMedia4).extended_media.get(size);
                                        }
                                    }
                                    inputMedia = null;
                                }
                                if (inputMedia == null || inputMedia.file != null) {
                                    if (delayedMessage.photoSize != null) {
                                        String str15 = FileLoader.getDirectory(4) + "/" + delayedMessage.photoSize.location.volume_id + "_" + delayedMessage.photoSize.location.local_id + ".jpg";
                                        putToDelayedMessages(str15, delayedMessage);
                                        delayedMessage.extraHashMap.put(str15 + "_o", str14);
                                        delayedMessage.extraHashMap.put(messageObject7, str15);
                                        delayedMessage.extraHashMap.put(str15, inputMedia);
                                        getFileLoader().uploadFile(str15, false, true, ConnectionsManager.FileTypePhoto);
                                    }
                                    delayedMessage.videoEditedInfo = null;
                                    delayedMessage.photoSize = null;
                                } else {
                                    putToDelayedMessages(str14, delayedMessage);
                                    delayedMessage.extraHashMap.put(messageObject7, str14);
                                    delayedMessage.extraHashMap.put(str14, inputMedia);
                                    delayedMessage.extraHashMap.put(str14 + "_i", messageObject7);
                                    TLRPC.PhotoSize photoSize3 = delayedMessage.photoSize;
                                    if (photoSize3 != null && photoSize3.location != null) {
                                        delayedMessage.extraHashMap.put(str14 + str, delayedMessage.photoSize);
                                    }
                                    VideoEditedInfo videoEditedInfo6 = messageObject7.videoEditedInfo;
                                    if (videoEditedInfo6 == null || !videoEditedInfo6.needConvert()) {
                                        getFileLoader().uploadFile(str14, false, false, ConnectionsManager.FileTypeVideo);
                                    } else {
                                        fileLoader = getFileLoader();
                                        j = document.size;
                                        i2 = ConnectionsManager.FileTypeVideo;
                                        z3 = false;
                                        z4 = false;
                                        fileLoader.uploadFile(str14, z4, false, j, i2, z3);
                                    }
                                }
                            } else {
                                String str16 = str;
                                TLRPC.TL_messages_sendEncryptedMultiMedia tL_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessage.sendEncryptedRequest;
                                putToDelayedMessages(str14, delayedMessage);
                                delayedMessage.extraHashMap.put(messageObject7, str14);
                                delayedMessage.extraHashMap.put(str14, tL_messages_sendEncryptedMultiMedia.files.get(size));
                                delayedMessage.extraHashMap.put(str14 + "_i", messageObject7);
                                TLRPC.PhotoSize photoSize4 = delayedMessage.photoSize;
                                if (photoSize4 != null && photoSize4.location != null) {
                                    delayedMessage.extraHashMap.put(str14 + str16, delayedMessage.photoSize);
                                }
                                VideoEditedInfo videoEditedInfo7 = messageObject7.videoEditedInfo;
                                if (videoEditedInfo7 == null || !videoEditedInfo7.needConvert()) {
                                    getFileLoader().uploadFile(str14, true, false, ConnectionsManager.FileTypeVideo);
                                } else {
                                    fileLoader = getFileLoader();
                                    j = document.size;
                                    i2 = ConnectionsManager.FileTypeVideo;
                                    z3 = false;
                                    z4 = true;
                                    fileLoader.uploadFile(str14, z4, false, j, i2, z3);
                                }
                            }
                        }
                        putToUploadingMessages(messageObject7);
                        delayedMessage.videoEditedInfo = null;
                        delayedMessage.photoSize = null;
                    } else {
                        String str17 = delayedMessage.httpLocation;
                        if (str17 != null) {
                            putToDelayedMessages(str17, delayedMessage);
                            delayedMessage.extraHashMap.put(messageObject7, delayedMessage.httpLocation);
                            delayedMessage.extraHashMap.put(delayedMessage.httpLocation, messageObject7);
                            ImageLoader.getInstance().loadHttpFile(delayedMessage.httpLocation, "file", this.currentAccount);
                            delayedMessage.httpLocation = null;
                        } else {
                            TLObject tLObject5 = delayedMessage.sendRequest;
                            if (tLObject5 instanceof TLRPC.TL_messages_sendMultiMedia) {
                                obj = ((TLRPC.TL_messages_sendMultiMedia) tLObject5).multi_media.get(size).media;
                            } else {
                                if (tLObject5 instanceof TLRPC.TL_messages_sendMedia) {
                                    TLRPC.InputMedia inputMedia5 = ((TLRPC.TL_messages_sendMedia) tLObject5).media;
                                    if (inputMedia5 instanceof TLRPC.TL_inputMediaPaidMedia) {
                                        arrayList = ((TLRPC.TL_inputMediaPaidMedia) inputMedia5).extended_media;
                                        obj = (TLObject) arrayList.get(size);
                                    }
                                }
                                arrayList = ((TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessage.sendEncryptedRequest).files;
                                obj = (TLObject) arrayList.get(size);
                            }
                            String file5 = FileLoader.getInstance(this.currentAccount).getPathToAttach(delayedMessage.photoSize).toString();
                            putToDelayedMessages(file5, delayedMessage);
                            delayedMessage.extraHashMap.put(file5, obj);
                            delayedMessage.extraHashMap.put(messageObject7, file5);
                            z = true;
                            getFileLoader().uploadFile(file5, delayedMessage.sendEncryptedRequest != null, true, ConnectionsManager.FileTypePhoto);
                            putToUploadingMessages(messageObject7);
                            delayedMessage.photoSize = null;
                            z2 = false;
                            delayedMessage.performMediaUpload = z2;
                        }
                    }
                    z2 = false;
                    z = true;
                    delayedMessage.performMediaUpload = z2;
                } else {
                    z = true;
                    if (!delayedMessage.messageObjects.isEmpty()) {
                        ArrayList<MessageObject> arrayList2 = delayedMessage.messageObjects;
                        putToSendingMessages(arrayList2.get(arrayList2.size() - 1).messageOwner, delayedMessage.finalGroupMessage != 0);
                    }
                }
                sendReadyToSendGroup(delayedMessage, z5, z);
                return;
            }
            str2 = delayedMessage.obj.messageOwner.attachPath;
            putToDelayedMessages(str2, delayedMessage);
            fileLoader2 = getFileLoader();
            r5 = delayedMessage.sendRequest == null;
            i4 = ConnectionsManager.FileTypeAudio;
        }
        fileLoader2.uploadFile(str2, r5, true, i4);
        putToUploadingMessages(delayedMessage.obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performSendMessageRequest(TLObject tLObject, MessageObject messageObject, String str, DelayedMessage delayedMessage, Object obj, HashMap<String, String> hashMap, boolean z) {
        performSendMessageRequest(tLObject, messageObject, str, null, false, delayedMessage, obj, hashMap, z);
    }

    public static void prepareSendingAudioDocuments(final AccountInstance accountInstance, final ArrayList<MessageObject> arrayList, final CharSequence charSequence, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final boolean z, final int i, final MessageObject messageObject3, final String str, final int i2, final long j2, final boolean z2) {
        new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingAudioDocuments$78(arrayList, j, accountInstance, charSequence, messageObject3, messageObject, messageObject2, z, i, storyItem, str, i2, j2, z2);
            }
        }).start();
    }

    public static void prepareSendingBotContextResult(final BaseFragment baseFragment, final AccountInstance accountInstance, final TLRPC.BotInlineResult botInlineResult, final HashMap<String, String> hashMap, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final boolean z, final int i, final String str, final int i2) {
        TLRPC.TL_webPagePending tL_webPagePending;
        SendMessageParams of;
        TLRPC.MessageMedia tL_messageMediaGeoLive;
        TLRPC.BotInlineMessage botInlineMessage;
        TLRPC.TL_webPagePending tL_webPagePending2;
        if (botInlineResult == null) {
            return;
        }
        TLRPC.BotInlineMessage botInlineMessage2 = botInlineResult.send_message;
        if (botInlineMessage2 instanceof TLRPC.TL_botInlineMessageMediaAuto) {
            new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda47
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.lambda$prepareSendingBotContextResult$85(j, botInlineResult, accountInstance, hashMap, baseFragment, messageObject, messageObject2, z, i, str, i2, storyItem, replyQuote);
                }
            }).run();
            return;
        }
        if (!(botInlineMessage2 instanceof TLRPC.TL_botInlineMessageText)) {
            if (botInlineMessage2 instanceof TLRPC.TL_botInlineMessageMediaVenue) {
                TLRPC.TL_messageMediaVenue tL_messageMediaVenue = new TLRPC.TL_messageMediaVenue();
                TLRPC.BotInlineMessage botInlineMessage3 = botInlineResult.send_message;
                tL_messageMediaVenue.geo = botInlineMessage3.geo;
                tL_messageMediaVenue.address = botInlineMessage3.address;
                tL_messageMediaVenue.title = botInlineMessage3.title;
                tL_messageMediaVenue.provider = botInlineMessage3.provider;
                String str2 = botInlineMessage3.venue_type;
                tL_messageMediaVenue.venue_id = str2;
                tL_messageMediaVenue.venue_type = str2;
                if (str2 == null) {
                    tL_messageMediaVenue.venue_type = "";
                }
                of = SendMessageParams.of(tL_messageMediaVenue, j, messageObject, messageObject2, botInlineMessage3.reply_markup, hashMap, z, i);
            } else if (botInlineMessage2 instanceof TLRPC.TL_botInlineMessageMediaGeo) {
                if (botInlineMessage2.period == 0 && botInlineMessage2.proximity_notification_radius == 0) {
                    tL_messageMediaGeoLive = new TLRPC.TL_messageMediaGeo();
                    botInlineMessage = botInlineResult.send_message;
                    tL_messageMediaGeoLive.geo = botInlineMessage.geo;
                    tL_messageMediaGeoLive.heading = botInlineMessage.heading;
                } else {
                    tL_messageMediaGeoLive = new TLRPC.TL_messageMediaGeoLive();
                    botInlineMessage = botInlineResult.send_message;
                    int i3 = botInlineMessage.period;
                    if (i3 == 0) {
                        i3 = 900;
                    }
                    tL_messageMediaGeoLive.period = i3;
                    tL_messageMediaGeoLive.geo = botInlineMessage.geo;
                    tL_messageMediaGeoLive.heading = botInlineMessage.heading;
                    tL_messageMediaGeoLive.proximity_notification_radius = botInlineMessage.proximity_notification_radius;
                }
                of = SendMessageParams.of(tL_messageMediaGeoLive, j, messageObject, messageObject2, botInlineMessage.reply_markup, hashMap, z, i);
            } else if (botInlineMessage2 instanceof TLRPC.TL_botInlineMessageMediaContact) {
                TLRPC.TL_user tL_user = new TLRPC.TL_user();
                TLRPC.BotInlineMessage botInlineMessage4 = botInlineResult.send_message;
                tL_user.phone = botInlineMessage4.phone_number;
                tL_user.first_name = botInlineMessage4.first_name;
                tL_user.last_name = botInlineMessage4.last_name;
                TLRPC.RestrictionReason restrictionReason = new TLRPC.RestrictionReason();
                restrictionReason.text = botInlineResult.send_message.vcard;
                restrictionReason.platform = "";
                restrictionReason.reason = "";
                tL_user.restriction_reason.add(restrictionReason);
                of = SendMessageParams.of(tL_user, j, messageObject, messageObject2, botInlineResult.send_message.reply_markup, hashMap, z, i);
            } else if (botInlineMessage2 instanceof TLRPC.TL_botInlineMessageMediaInvoice) {
                if (DialogObject.isEncryptedDialog(j)) {
                    return;
                }
                TLRPC.TL_botInlineMessageMediaInvoice tL_botInlineMessageMediaInvoice = (TLRPC.TL_botInlineMessageMediaInvoice) botInlineResult.send_message;
                TLRPC.TL_messageMediaInvoice tL_messageMediaInvoice = new TLRPC.TL_messageMediaInvoice();
                tL_messageMediaInvoice.shipping_address_requested = tL_botInlineMessageMediaInvoice.shipping_address_requested;
                tL_messageMediaInvoice.test = tL_botInlineMessageMediaInvoice.test;
                tL_messageMediaInvoice.title = tL_botInlineMessageMediaInvoice.title;
                tL_messageMediaInvoice.description = tL_botInlineMessageMediaInvoice.description;
                TLRPC.WebDocument webDocument = tL_botInlineMessageMediaInvoice.photo;
                if (webDocument != null) {
                    tL_messageMediaInvoice.webPhoto = webDocument;
                    tL_messageMediaInvoice.flags |= 1;
                }
                tL_messageMediaInvoice.currency = tL_botInlineMessageMediaInvoice.currency;
                tL_messageMediaInvoice.total_amount = tL_botInlineMessageMediaInvoice.total_amount;
                tL_messageMediaInvoice.start_param = "";
                of = SendMessageParams.of(tL_messageMediaInvoice, j, messageObject, messageObject2, botInlineResult.send_message.reply_markup, hashMap, z, i);
            } else {
                if (!(botInlineMessage2 instanceof TLRPC.TL_botInlineMessageMediaWebPage)) {
                    return;
                }
                tL_webPagePending = new TLRPC.TL_webPagePending();
                tL_webPagePending.url = ((TLRPC.TL_botInlineMessageMediaWebPage) botInlineMessage2).url;
            }
            of.quick_reply_shortcut = str;
            of.quick_reply_shortcut_id = i2;
            of.replyQuote = replyQuote;
            accountInstance.getSendMessagesHelper().sendMessage(of);
        }
        if (DialogObject.isEncryptedDialog(j)) {
            for (int i4 = 0; i4 < botInlineResult.send_message.entities.size(); i4++) {
                TLRPC.MessageEntity messageEntity = botInlineResult.send_message.entities.get(i4);
                if (messageEntity instanceof TLRPC.TL_messageEntityUrl) {
                    tL_webPagePending2 = new TLRPC.TL_webPagePending();
                    String str3 = botInlineResult.send_message.message;
                    int i5 = messageEntity.offset;
                    tL_webPagePending2.url = str3.substring(i5, messageEntity.length + i5);
                    break;
                }
            }
        }
        tL_webPagePending2 = null;
        tL_webPagePending = tL_webPagePending2;
        TLRPC.BotInlineMessage botInlineMessage5 = botInlineResult.send_message;
        of = SendMessageParams.of(botInlineMessage5.message, j, messageObject, messageObject2, tL_webPagePending, !botInlineMessage5.no_webpage, botInlineMessage5.entities, botInlineMessage5.reply_markup, hashMap, z, i, null, false);
        of.quick_reply_shortcut = str;
        of.quick_reply_shortcut_id = i2;
        of.replyQuote = replyQuote;
        accountInstance.getSendMessagesHelper().sendMessage(of);
    }

    public static void prepareSendingDocument(AccountInstance accountInstance, String str, String str2, Uri uri, String str3, String str4, long j, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, MessageObject messageObject3, boolean z, int i, InputContentInfoCompat inputContentInfoCompat, String str5, int i2, boolean z2) {
        ArrayList arrayList;
        if ((str == null || str2 == null) && uri == null) {
            return;
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        if (uri != null) {
            arrayList = new ArrayList();
            arrayList.add(uri);
        } else {
            arrayList = null;
        }
        if (str != null) {
            arrayList2.add(str);
            arrayList3.add(str2);
        }
        prepareSendingDocuments(accountInstance, arrayList2, arrayList3, arrayList, str3, str4, j, messageObject, messageObject2, storyItem, replyQuote, messageObject3, z, i, inputContentInfoCompat, str5, i2, 0L, z2);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(29:(8:250|251|252|253|(10:255|256|257|258|259|260|261|262|263|264)(1:340)|265|266|(7:268|269|270|271|272|273|274)(1:324))|(4:276|277|278|(29:280|281|(26:283|284|285|286|54|(0)(0)|(0)(0)|246|70|(0)|241|94|(0)(0)|(0)(0)|155|(0)|158|(0)|(0)|164|(0)(0)|194|(0)|185|182|183)|290|284|285|286|54|(0)(0)|(0)(0)|246|70|(0)|241|94|(0)(0)|(0)(0)|155|(0)|158|(0)|(0)|164|(0)(0)|194|(0)|185|182|183))(1:317)|292|290|284|285|286|54|(0)(0)|(0)(0)|246|70|(0)|241|94|(0)(0)|(0)(0)|155|(0)|158|(0)|(0)|164|(0)(0)|194|(0)|185|182|183) */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x03aa, code lost:
    
        if (r3 != null) goto L241;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x018a, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0058, code lost:
    
        if (r3 == false) goto L33;
     */
    /* JADX WARN: Removed duplicated region for block: B:119:0x03ee A[Catch: Exception -> 0x040e, TryCatch #1 {Exception -> 0x040e, blocks: (B:117:0x03e0, B:119:0x03ee, B:121:0x0402, B:122:0x0410), top: B:116:0x03e0 }] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x041a  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x04ab  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x04c7  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x04d5  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x04e0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:163:0x04e9  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x04f5  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0557 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:201:0x0553  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x04cd  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x04bf  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x02b9  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x026b  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x0232  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x01ae A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:309:0x01c5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:316:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:354:0x01d5  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0209  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0239  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0271 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x02b7  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x02c0  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0316  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int prepareSendingDocumentInternal(final AccountInstance accountInstance, String str, String str2, Uri uri, String str3, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final ArrayList<TLRPC.MessageEntity> arrayList, final MessageObject messageObject3, long[] jArr, boolean z, CharSequence charSequence, final boolean z2, final int i, Integer[] numArr, boolean z3, final String str4, final int i2, final long j2, final boolean z4) {
        String str5;
        String str6;
        String str7;
        String lowerCase;
        String str8;
        Object obj;
        String str9;
        AudioInfo audioInfo;
        int i3;
        String str10;
        String str11;
        int i4;
        String str12;
        boolean z5;
        Bitmap cover;
        String str13;
        String str14;
        Object obj2;
        TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio;
        String str15;
        boolean z6;
        Object obj3;
        String str16;
        boolean z7;
        String str17;
        String str18;
        String str19;
        Bitmap bitmap;
        Object obj4;
        Object obj5;
        long j3;
        int i5;
        final String str20;
        TLRPC.TL_document tL_document;
        final String str21;
        String str22;
        TLRPC.TL_document tL_document2;
        final HashMap hashMap;
        char c;
        boolean z8;
        String str23;
        Bitmap bitmap2;
        int i6;
        int i7;
        TLRPC.PhotoSize scaleAndSaveImage;
        Bitmap loadBitmap;
        String str24;
        char c2;
        String str25;
        TLRPC.TL_document tL_document3;
        Object[] sentFile;
        StringBuilder sb;
        MediaMetadataRetriever mediaMetadataRetriever;
        MediaMetadataRetriever mediaMetadataRetriever2;
        String str26;
        Bitmap bitmap3;
        String str27;
        Throwable th;
        boolean z9;
        String str28;
        String str29;
        Bitmap bitmap4;
        boolean z10;
        if ((str == null || str.length() == 0) && uri == null) {
            return 1;
        }
        if (uri != null && AndroidUtilities.isInternalUri(uri)) {
            return 1;
        }
        if (str != null && AndroidUtilities.isInternalUri(Uri.fromFile(new File(str)))) {
            return 1;
        }
        MimeTypeMap singleton = MimeTypeMap.getSingleton();
        if (uri == null || str != null) {
            str5 = str;
        } else {
            if (checkFileSize(accountInstance, uri)) {
                return 2;
            }
            str6 = str3 != null ? singleton.getExtensionFromMimeType(str3) : null;
            if (str6 == null) {
                str6 = "txt";
                z10 = false;
            } else {
                z10 = true;
            }
            String copyFileToCache = MediaController.copyFileToCache(uri, str6);
            if (copyFileToCache == null) {
                return 1;
            }
            str5 = copyFileToCache;
        }
        str6 = null;
        File file = new File(str5);
        if (!file.exists() || file.length() == 0) {
            return 1;
        }
        if (!FileLoader.checkUploadFileSize(accountInstance.getCurrentAccount(), file.length())) {
            return 2;
        }
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(j);
        String name = file.getName();
        if (str6 == null) {
            int lastIndexOf = str5.lastIndexOf(46);
            if (lastIndexOf == -1) {
                str7 = "";
                lowerCase = str7.toLowerCase();
                if (!lowerCase.equals("mp3") || lowerCase.equals("m4a")) {
                    str8 = name;
                    obj = "opus";
                    str9 = lowerCase;
                    audioInfo = AudioInfo.getAudioInfo(file);
                    if (audioInfo != null) {
                        long duration = audioInfo.getDuration();
                        if (duration != 0) {
                            str10 = audioInfo.getArtist();
                            str11 = audioInfo.getTitle();
                            i3 = (int) (duration / 1000);
                        } else {
                            i3 = 0;
                            str10 = null;
                            str11 = null;
                        }
                        i4 = i3;
                        str12 = str10;
                        z5 = false;
                        cover = audioInfo.getCover();
                        str13 = str11;
                        Bitmap bitmap5 = cover;
                        if (i4 == 0) {
                            TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio2 = new TLRPC.TL_documentAttributeAudio();
                            str14 = str9;
                            obj2 = "flac";
                            tL_documentAttributeAudio2.duration = i4;
                            tL_documentAttributeAudio2.title = str13;
                            tL_documentAttributeAudio2.performer = str12;
                            if (str13 == null) {
                                tL_documentAttributeAudio2.title = "";
                            }
                            int i8 = tL_documentAttributeAudio2.flags;
                            tL_documentAttributeAudio2.flags = i8 | 1;
                            if (str12 == null) {
                                tL_documentAttributeAudio2.performer = "";
                            }
                            tL_documentAttributeAudio2.flags = i8 | 3;
                            if (z5) {
                                tL_documentAttributeAudio2.voice = true;
                            }
                            tL_documentAttributeAudio = tL_documentAttributeAudio2;
                        } else {
                            str14 = str9;
                            obj2 = "flac";
                            tL_documentAttributeAudio = null;
                        }
                        if (str2 != null) {
                            str15 = str2;
                        } else {
                            if (str2.endsWith("attheme")) {
                                str15 = str2;
                                z6 = true;
                                if (!z6 || isEncryptedDialog) {
                                    obj3 = "mp3";
                                    str16 = "";
                                    z7 = isEncryptedDialog;
                                    str17 = str15;
                                    str18 = str5;
                                    str19 = str8;
                                    bitmap = bitmap5;
                                    obj4 = obj;
                                    obj5 = obj2;
                                    j3 = 0;
                                    i5 = -1;
                                    str20 = null;
                                    tL_document = null;
                                } else {
                                    Object[] sentFile2 = accountInstance.getMessagesStorage().getSentFile(str15, !isEncryptedDialog ? 1 : 4);
                                    if (sentFile2 != null) {
                                        Object obj6 = sentFile2[0];
                                        if (obj6 instanceof TLRPC.TL_document) {
                                            tL_document3 = (TLRPC.TL_document) obj6;
                                            str25 = (String) sentFile2[1];
                                            if (tL_document3 == null && !str5.equals(str15) && !isEncryptedDialog) {
                                                sentFile = accountInstance.getMessagesStorage().getSentFile(str5 + file.length(), isEncryptedDialog ? 1 : 4);
                                                if (sentFile != null) {
                                                    Object obj7 = sentFile[0];
                                                    if (obj7 instanceof TLRPC.TL_document) {
                                                        str25 = (String) sentFile[1];
                                                        tL_document = (TLRPC.TL_document) obj7;
                                                        i5 = -1;
                                                        str19 = str8;
                                                        bitmap = bitmap5;
                                                        z7 = isEncryptedDialog;
                                                        obj3 = "mp3";
                                                        str16 = "";
                                                        str17 = str15;
                                                        str18 = str5;
                                                        obj4 = obj;
                                                        obj5 = obj2;
                                                        j3 = 0;
                                                        ensureMediaThumbExists(accountInstance, isEncryptedDialog, tL_document, str5, null, 0L);
                                                        str20 = str25;
                                                    }
                                                }
                                            }
                                            tL_document = tL_document3;
                                            i5 = -1;
                                            str19 = str8;
                                            bitmap = bitmap5;
                                            z7 = isEncryptedDialog;
                                            obj3 = "mp3";
                                            str16 = "";
                                            str17 = str15;
                                            str18 = str5;
                                            obj4 = obj;
                                            obj5 = obj2;
                                            j3 = 0;
                                            ensureMediaThumbExists(accountInstance, isEncryptedDialog, tL_document, str5, null, 0L);
                                            str20 = str25;
                                        }
                                    }
                                    str25 = null;
                                    tL_document3 = null;
                                    if (tL_document3 == null) {
                                        sentFile = accountInstance.getMessagesStorage().getSentFile(str5 + file.length(), isEncryptedDialog ? 1 : 4);
                                        if (sentFile != null) {
                                        }
                                    }
                                    tL_document = tL_document3;
                                    i5 = -1;
                                    str19 = str8;
                                    bitmap = bitmap5;
                                    z7 = isEncryptedDialog;
                                    obj3 = "mp3";
                                    str16 = "";
                                    str17 = str15;
                                    str18 = str5;
                                    obj4 = obj;
                                    obj5 = obj2;
                                    j3 = 0;
                                    ensureMediaThumbExists(accountInstance, isEncryptedDialog, tL_document, str5, null, 0L);
                                    str20 = str25;
                                }
                                if (tL_document == null) {
                                    TLRPC.TL_document tL_document4 = new TLRPC.TL_document();
                                    tL_document4.id = j3;
                                    tL_document4.date = accountInstance.getConnectionsManager().getCurrentTime();
                                    TLRPC.TL_documentAttributeFilename tL_documentAttributeFilename = new TLRPC.TL_documentAttributeFilename();
                                    tL_documentAttributeFilename.file_name = str19;
                                    tL_document4.file_reference = new byte[0];
                                    tL_document4.attributes.add(tL_documentAttributeFilename);
                                    tL_document4.size = file.length();
                                    tL_document4.dc_id = 0;
                                    if (tL_documentAttributeAudio != null) {
                                        tL_document4.attributes.add(tL_documentAttributeAudio);
                                    }
                                    if (str7.length() != 0) {
                                        switch (str14.hashCode()) {
                                            case 106458:
                                                str24 = str14;
                                                if (str24.equals("m4a")) {
                                                    c2 = 0;
                                                    break;
                                                }
                                                c2 = 65535;
                                                break;
                                            case 108272:
                                                str24 = str14;
                                                if (str24.equals(obj3)) {
                                                    c2 = 1;
                                                    break;
                                                }
                                                c2 = 65535;
                                                break;
                                            case 109967:
                                                str24 = str14;
                                                if (str24.equals("ogg")) {
                                                    c2 = 2;
                                                    break;
                                                }
                                                c2 = 65535;
                                                break;
                                            case 3145576:
                                                str24 = str14;
                                                if (str24.equals(obj5)) {
                                                    c2 = 3;
                                                    break;
                                                }
                                                c2 = 65535;
                                                break;
                                            case 3418175:
                                                str24 = str14;
                                                if (str24.equals(obj4)) {
                                                    c2 = 4;
                                                    break;
                                                }
                                                c2 = 65535;
                                                break;
                                            case 3645340:
                                                str24 = str14;
                                                if (str24.equals("webp")) {
                                                    c2 = 5;
                                                    break;
                                                }
                                                c2 = 65535;
                                                break;
                                            default:
                                                str24 = str14;
                                                c2 = 65535;
                                                break;
                                        }
                                        switch (c2) {
                                            case 0:
                                                str23 = "audio/m4a";
                                                tL_document4.mime_type = str23;
                                                break;
                                            case 1:
                                                str23 = "audio/mpeg";
                                                tL_document4.mime_type = str23;
                                                break;
                                            case 2:
                                                str23 = "audio/ogg";
                                                tL_document4.mime_type = str23;
                                                break;
                                            case 3:
                                                str23 = "audio/flac";
                                                tL_document4.mime_type = str23;
                                                break;
                                            case 4:
                                                str23 = "audio/opus";
                                                tL_document4.mime_type = str23;
                                                break;
                                            case 5:
                                                tL_document4.mime_type = "image/webp";
                                                break;
                                            default:
                                                str23 = singleton.getMimeTypeFromExtension(str24);
                                                break;
                                        }
                                        if (!z3 && tL_document4.mime_type.equals("image/gif") && (messageObject3 == null || messageObject3.getGroupIdForUse() == j3)) {
                                            try {
                                                loadBitmap = ImageLoader.loadBitmap(file.getAbsolutePath(), null, 90.0f, 90.0f, true);
                                                if (loadBitmap != null) {
                                                    tL_documentAttributeFilename.file_name = "animation.gif";
                                                    tL_document4.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                                                    TLRPC.PhotoSize scaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(loadBitmap, 90.0f, 90.0f, 55, z7);
                                                    if (scaleAndSaveImage2 != null) {
                                                        tL_document4.thumbs.add(scaleAndSaveImage2);
                                                        tL_document4.flags |= 1;
                                                    }
                                                    loadBitmap.recycle();
                                                }
                                            } catch (Exception e) {
                                                FileLog.e(e);
                                            }
                                        }
                                        bitmap2 = bitmap;
                                        if (bitmap2 != null) {
                                            TLRPC.PhotoSize scaleAndSaveImage3 = ImageLoader.scaleAndSaveImage(bitmap2, 132.0f, 132.0f, 55, z7);
                                            if (scaleAndSaveImage3 != null) {
                                                tL_document4.thumbs.add(scaleAndSaveImage3);
                                                tL_document4.flags |= 1;
                                            }
                                            bitmap2.recycle();
                                        }
                                        if (tL_document4.mime_type.equals("image/webp") || messageObject3 != null) {
                                            str21 = str18;
                                        } else {
                                            BitmapFactory.Options options = new BitmapFactory.Options();
                                            try {
                                                options.inJustDecodeBounds = true;
                                                str21 = str18;
                                            } catch (Exception e2) {
                                                e = e2;
                                                str21 = str18;
                                            }
                                            try {
                                                BitmapFactory.decodeFile(str21, options);
                                            } catch (Exception e3) {
                                                e = e3;
                                                FileLog.e(e);
                                                i6 = options.outWidth;
                                                if (i6 != 0) {
                                                    TLRPC.TL_documentAttributeSticker tL_documentAttributeSticker = new TLRPC.TL_documentAttributeSticker();
                                                    str22 = str16;
                                                    tL_documentAttributeSticker.alt = str22;
                                                    tL_documentAttributeSticker.stickerset = new TLRPC.TL_inputStickerSetEmpty();
                                                    tL_document4.attributes.add(tL_documentAttributeSticker);
                                                    TLRPC.TL_documentAttributeImageSize tL_documentAttributeImageSize = new TLRPC.TL_documentAttributeImageSize();
                                                    tL_documentAttributeImageSize.w = options.outWidth;
                                                    tL_documentAttributeImageSize.h = options.outHeight;
                                                    tL_document4.attributes.add(tL_documentAttributeImageSize);
                                                    scaleAndSaveImage = ImageLoader.scaleAndSaveImage(null, ImageLoader.loadBitmap(file.getAbsolutePath(), null, 400.0f, 400.0f, true), Bitmap.CompressFormat.PNG, false, 400.0f, 400.0f, 100, z7, 0, 0, false);
                                                    if (scaleAndSaveImage != null) {
                                                    }
                                                    tL_document2 = tL_document4;
                                                    if (charSequence != null) {
                                                    }
                                                    hashMap = new HashMap();
                                                    if (str17 != null) {
                                                    }
                                                    if (z3) {
                                                    }
                                                    if (str20 != null) {
                                                    }
                                                    int i9 = 0;
                                                    if (numArr != null) {
                                                    }
                                                    z8 = false;
                                                    if (z7) {
                                                    }
                                                    final TLRPC.TL_document tL_document5 = tL_document2;
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document5, str21, hashMap, str20, j, messageObject, messageObject2, r14, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                                        }
                                                    });
                                                    return 0;
                                                }
                                                str22 = str16;
                                                tL_document2 = tL_document4;
                                                if (charSequence != null) {
                                                }
                                                hashMap = new HashMap();
                                                if (str17 != null) {
                                                }
                                                if (z3) {
                                                }
                                                if (str20 != null) {
                                                }
                                                int i92 = 0;
                                                if (numArr != null) {
                                                }
                                                z8 = false;
                                                if (z7) {
                                                }
                                                final TLRPC.TL_document tL_document52 = tL_document2;
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document52, str21, hashMap, str20, j, messageObject, messageObject2, r14, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                                    }
                                                });
                                                return 0;
                                            }
                                            i6 = options.outWidth;
                                            if (i6 != 0 && (i7 = options.outHeight) != 0 && i6 <= 800 && i7 <= 800) {
                                                TLRPC.TL_documentAttributeSticker tL_documentAttributeSticker2 = new TLRPC.TL_documentAttributeSticker();
                                                str22 = str16;
                                                tL_documentAttributeSticker2.alt = str22;
                                                tL_documentAttributeSticker2.stickerset = new TLRPC.TL_inputStickerSetEmpty();
                                                tL_document4.attributes.add(tL_documentAttributeSticker2);
                                                TLRPC.TL_documentAttributeImageSize tL_documentAttributeImageSize2 = new TLRPC.TL_documentAttributeImageSize();
                                                tL_documentAttributeImageSize2.w = options.outWidth;
                                                tL_documentAttributeImageSize2.h = options.outHeight;
                                                tL_document4.attributes.add(tL_documentAttributeImageSize2);
                                                scaleAndSaveImage = ImageLoader.scaleAndSaveImage(null, ImageLoader.loadBitmap(file.getAbsolutePath(), null, 400.0f, 400.0f, true), Bitmap.CompressFormat.PNG, false, 400.0f, 400.0f, 100, z7, 0, 0, false);
                                                if (scaleAndSaveImage != null) {
                                                    tL_document4.thumbs.add(scaleAndSaveImage);
                                                    tL_document4.flags |= 1;
                                                }
                                                tL_document2 = tL_document4;
                                            }
                                        }
                                        str22 = str16;
                                        tL_document2 = tL_document4;
                                    }
                                    str23 = "application/octet-stream";
                                    tL_document4.mime_type = str23;
                                    if (!z3) {
                                        loadBitmap = ImageLoader.loadBitmap(file.getAbsolutePath(), null, 90.0f, 90.0f, true);
                                        if (loadBitmap != null) {
                                        }
                                    }
                                    bitmap2 = bitmap;
                                    if (bitmap2 != null) {
                                    }
                                    if (tL_document4.mime_type.equals("image/webp")) {
                                    }
                                    str21 = str18;
                                    str22 = str16;
                                    tL_document2 = tL_document4;
                                } else {
                                    str21 = str18;
                                    str22 = str16;
                                    tL_document2 = tL_document;
                                }
                                final String charSequence2 = charSequence != null ? charSequence.toString() : str22;
                                hashMap = new HashMap();
                                if (str17 != null) {
                                    hashMap.put("originalPath", str17);
                                }
                                if (z3 && tL_documentAttributeAudio == null) {
                                    hashMap.put("forceDocument", "1");
                                }
                                if (str20 != null) {
                                    hashMap.put("parentObject", str20);
                                }
                                int i922 = 0;
                                if (numArr != null) {
                                    i922 = numArr[0];
                                    String str30 = tL_document2.mime_type;
                                    if (str30 != null && str30.toLowerCase().startsWith("image/webp")) {
                                        numArr[0] = Integer.valueOf(i5);
                                        c = 0;
                                        z8 = true;
                                        if (z7 && jArr != null) {
                                            if (numArr != null && i922 != null && i922 != numArr[c]) {
                                                finishGroup(accountInstance, jArr[c], i);
                                                jArr[c] = Utilities.random.nextLong();
                                            }
                                            if (!z8) {
                                                hashMap.put("groupId", str22 + jArr[c]);
                                                if (z) {
                                                    hashMap.put("final", "1");
                                                }
                                            }
                                        }
                                        final TLRPC.TL_document tL_document522 = tL_document2;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document522, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                            }
                                        });
                                        return 0;
                                    }
                                    String str31 = tL_document2.mime_type;
                                    if ((str31 == null || !(str31.toLowerCase().startsWith("image/") || tL_document2.mime_type.toLowerCase().startsWith("video/mp4"))) && !MessageObject.canPreviewDocument(tL_document2)) {
                                        c = 0;
                                        if (tL_documentAttributeAudio != null) {
                                            numArr[0] = 2;
                                        } else {
                                            numArr[0] = 0;
                                        }
                                    } else {
                                        c = 0;
                                        numArr[0] = 1;
                                    }
                                } else {
                                    c = 0;
                                }
                                z8 = false;
                                if (z7) {
                                }
                                final TLRPC.TL_document tL_document5222 = tL_document2;
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document5222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                    }
                                });
                                return 0;
                            }
                            if (tL_documentAttributeAudio != null) {
                                sb = new StringBuilder();
                                sb.append(str2);
                                sb.append(MediaStreamTrack.AUDIO_TRACK_KIND);
                            } else {
                                sb = new StringBuilder();
                                sb.append(str2);
                                sb.append("");
                            }
                            sb.append(file.length());
                            str15 = sb.toString();
                        }
                        z6 = false;
                        if (z6) {
                        }
                        obj3 = "mp3";
                        str16 = "";
                        z7 = isEncryptedDialog;
                        str17 = str15;
                        str18 = str5;
                        str19 = str8;
                        bitmap = bitmap5;
                        obj4 = obj;
                        obj5 = obj2;
                        j3 = 0;
                        i5 = -1;
                        str20 = null;
                        tL_document = null;
                        if (tL_document == null) {
                        }
                        if (charSequence != null) {
                        }
                        hashMap = new HashMap();
                        if (str17 != null) {
                        }
                        if (z3) {
                            hashMap.put("forceDocument", "1");
                        }
                        if (str20 != null) {
                        }
                        int i9222 = 0;
                        if (numArr != null) {
                        }
                        z8 = false;
                        if (z7) {
                        }
                        final TLRPC.TL_document tL_document52222 = tL_document2;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document52222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                            }
                        });
                        return 0;
                    }
                    str13 = null;
                    str12 = null;
                    cover = null;
                    i4 = 0;
                } else {
                    if (lowerCase.equals("opus") || lowerCase.equals("ogg") || lowerCase.equals("flac")) {
                        try {
                            mediaMetadataRetriever2 = new MediaMetadataRetriever();
                            try {
                                try {
                                    mediaMetadataRetriever2.setDataSource(file.getAbsolutePath());
                                    if (mediaMetadataRetriever2.extractMetadata(9) != null) {
                                        str28 = lowerCase;
                                        try {
                                            i4 = (int) Math.ceil(Long.parseLong(r0) / 1000.0f);
                                        } catch (Exception e4) {
                                            e = e4;
                                            str8 = name;
                                            obj = "opus";
                                            str9 = str28;
                                            i4 = 0;
                                            str26 = null;
                                            bitmap3 = null;
                                            str27 = null;
                                            try {
                                                FileLog.e(e);
                                                if (mediaMetadataRetriever2 != null) {
                                                }
                                                str13 = str26;
                                                cover = bitmap3;
                                                str12 = str27;
                                                z5 = false;
                                                Bitmap bitmap52 = cover;
                                                if (i4 == 0) {
                                                }
                                                if (str2 != null) {
                                                }
                                                z6 = false;
                                                if (z6) {
                                                }
                                                obj3 = "mp3";
                                                str16 = "";
                                                z7 = isEncryptedDialog;
                                                str17 = str15;
                                                str18 = str5;
                                                str19 = str8;
                                                bitmap = bitmap52;
                                                obj4 = obj;
                                                obj5 = obj2;
                                                j3 = 0;
                                                i5 = -1;
                                                str20 = null;
                                                tL_document = null;
                                                if (tL_document == null) {
                                                }
                                                if (charSequence != null) {
                                                }
                                                hashMap = new HashMap();
                                                if (str17 != null) {
                                                }
                                                if (z3) {
                                                }
                                                if (str20 != null) {
                                                }
                                                int i92222 = 0;
                                                if (numArr != null) {
                                                }
                                                z8 = false;
                                                if (z7) {
                                                }
                                                final TLRPC.TL_document tL_document522222 = tL_document2;
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document522222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                                    }
                                                });
                                                return 0;
                                            } catch (Throwable th2) {
                                                th = th2;
                                                mediaMetadataRetriever = mediaMetadataRetriever2;
                                                th = th;
                                                mediaMetadataRetriever2 = mediaMetadataRetriever;
                                                if (mediaMetadataRetriever2 == null) {
                                                }
                                            }
                                        }
                                        try {
                                            str26 = mediaMetadataRetriever2.extractMetadata(7);
                                            str8 = name;
                                        } catch (Exception e5) {
                                            e = e5;
                                            str8 = name;
                                            obj = "opus";
                                            str9 = str28;
                                            str26 = null;
                                            bitmap3 = null;
                                            str27 = null;
                                            FileLog.e(e);
                                            if (mediaMetadataRetriever2 != null) {
                                            }
                                            str13 = str26;
                                            cover = bitmap3;
                                            str12 = str27;
                                            z5 = false;
                                            Bitmap bitmap522 = cover;
                                            if (i4 == 0) {
                                            }
                                            if (str2 != null) {
                                            }
                                            z6 = false;
                                            if (z6) {
                                            }
                                            obj3 = "mp3";
                                            str16 = "";
                                            z7 = isEncryptedDialog;
                                            str17 = str15;
                                            str18 = str5;
                                            str19 = str8;
                                            bitmap = bitmap522;
                                            obj4 = obj;
                                            obj5 = obj2;
                                            j3 = 0;
                                            i5 = -1;
                                            str20 = null;
                                            tL_document = null;
                                            if (tL_document == null) {
                                            }
                                            if (charSequence != null) {
                                            }
                                            hashMap = new HashMap();
                                            if (str17 != null) {
                                            }
                                            if (z3) {
                                            }
                                            if (str20 != null) {
                                            }
                                            int i922222 = 0;
                                            if (numArr != null) {
                                            }
                                            z8 = false;
                                            if (z7) {
                                            }
                                            final TLRPC.TL_document tL_document5222222 = tL_document2;
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document5222222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                                }
                                            });
                                            return 0;
                                        }
                                        try {
                                            str29 = mediaMetadataRetriever2.extractMetadata(2);
                                        } catch (Exception e6) {
                                            e = e6;
                                            obj = "opus";
                                            str9 = str28;
                                            bitmap3 = null;
                                            str27 = null;
                                            FileLog.e(e);
                                            if (mediaMetadataRetriever2 != null) {
                                            }
                                            str13 = str26;
                                            cover = bitmap3;
                                            str12 = str27;
                                            z5 = false;
                                            Bitmap bitmap5222 = cover;
                                            if (i4 == 0) {
                                            }
                                            if (str2 != null) {
                                            }
                                            z6 = false;
                                            if (z6) {
                                            }
                                            obj3 = "mp3";
                                            str16 = "";
                                            z7 = isEncryptedDialog;
                                            str17 = str15;
                                            str18 = str5;
                                            str19 = str8;
                                            bitmap = bitmap5222;
                                            obj4 = obj;
                                            obj5 = obj2;
                                            j3 = 0;
                                            i5 = -1;
                                            str20 = null;
                                            tL_document = null;
                                            if (tL_document == null) {
                                            }
                                            if (charSequence != null) {
                                            }
                                            hashMap = new HashMap();
                                            if (str17 != null) {
                                            }
                                            if (z3) {
                                            }
                                            if (str20 != null) {
                                            }
                                            int i9222222 = 0;
                                            if (numArr != null) {
                                            }
                                            z8 = false;
                                            if (z7) {
                                            }
                                            final TLRPC.TL_document tL_document52222222 = tL_document2;
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document52222222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                                }
                                            });
                                            return 0;
                                        }
                                    } else {
                                        str8 = name;
                                        str28 = lowerCase;
                                        str29 = null;
                                        i4 = 0;
                                        str26 = null;
                                    }
                                    try {
                                        byte[] embeddedPicture = mediaMetadataRetriever2.getEmbeddedPicture();
                                        if (embeddedPicture != null) {
                                            str27 = str29;
                                            try {
                                                obj = "opus";
                                                try {
                                                    bitmap4 = BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.length);
                                                } catch (Exception e7) {
                                                    e = e7;
                                                    str9 = str28;
                                                    bitmap3 = null;
                                                    FileLog.e(e);
                                                    if (mediaMetadataRetriever2 != null) {
                                                        try {
                                                            mediaMetadataRetriever2.release();
                                                        } catch (Exception e8) {
                                                            e = e8;
                                                            z9 = false;
                                                            FileLog.e(e);
                                                            str13 = str26;
                                                            str12 = str27;
                                                            z5 = z9;
                                                            cover = bitmap3;
                                                            Bitmap bitmap52222 = cover;
                                                            if (i4 == 0) {
                                                            }
                                                            if (str2 != null) {
                                                            }
                                                            z6 = false;
                                                            if (z6) {
                                                            }
                                                            obj3 = "mp3";
                                                            str16 = "";
                                                            z7 = isEncryptedDialog;
                                                            str17 = str15;
                                                            str18 = str5;
                                                            str19 = str8;
                                                            bitmap = bitmap52222;
                                                            obj4 = obj;
                                                            obj5 = obj2;
                                                            j3 = 0;
                                                            i5 = -1;
                                                            str20 = null;
                                                            tL_document = null;
                                                            if (tL_document == null) {
                                                            }
                                                            if (charSequence != null) {
                                                            }
                                                            hashMap = new HashMap();
                                                            if (str17 != null) {
                                                            }
                                                            if (z3) {
                                                            }
                                                            if (str20 != null) {
                                                            }
                                                            int i92222222 = 0;
                                                            if (numArr != null) {
                                                            }
                                                            z8 = false;
                                                            if (z7) {
                                                            }
                                                            final TLRPC.TL_document tL_document522222222 = tL_document2;
                                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                                                @Override // java.lang.Runnable
                                                                public final void run() {
                                                                    SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document522222222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                                                }
                                                            });
                                                            return 0;
                                                        }
                                                    }
                                                    str13 = str26;
                                                    cover = bitmap3;
                                                    str12 = str27;
                                                    z5 = false;
                                                    Bitmap bitmap522222 = cover;
                                                    if (i4 == 0) {
                                                    }
                                                    if (str2 != null) {
                                                    }
                                                    z6 = false;
                                                    if (z6) {
                                                    }
                                                    obj3 = "mp3";
                                                    str16 = "";
                                                    z7 = isEncryptedDialog;
                                                    str17 = str15;
                                                    str18 = str5;
                                                    str19 = str8;
                                                    bitmap = bitmap522222;
                                                    obj4 = obj;
                                                    obj5 = obj2;
                                                    j3 = 0;
                                                    i5 = -1;
                                                    str20 = null;
                                                    tL_document = null;
                                                    if (tL_document == null) {
                                                    }
                                                    if (charSequence != null) {
                                                    }
                                                    hashMap = new HashMap();
                                                    if (str17 != null) {
                                                    }
                                                    if (z3) {
                                                    }
                                                    if (str20 != null) {
                                                    }
                                                    int i922222222 = 0;
                                                    if (numArr != null) {
                                                    }
                                                    z8 = false;
                                                    if (z7) {
                                                    }
                                                    final TLRPC.TL_document tL_document5222222222 = tL_document2;
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document5222222222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                                        }
                                                    });
                                                    return 0;
                                                }
                                            } catch (Exception e9) {
                                                e = e9;
                                                obj = "opus";
                                                str9 = str28;
                                                bitmap3 = null;
                                                FileLog.e(e);
                                                if (mediaMetadataRetriever2 != null) {
                                                }
                                                str13 = str26;
                                                cover = bitmap3;
                                                str12 = str27;
                                                z5 = false;
                                                Bitmap bitmap5222222 = cover;
                                                if (i4 == 0) {
                                                }
                                                if (str2 != null) {
                                                }
                                                z6 = false;
                                                if (z6) {
                                                }
                                                obj3 = "mp3";
                                                str16 = "";
                                                z7 = isEncryptedDialog;
                                                str17 = str15;
                                                str18 = str5;
                                                str19 = str8;
                                                bitmap = bitmap5222222;
                                                obj4 = obj;
                                                obj5 = obj2;
                                                j3 = 0;
                                                i5 = -1;
                                                str20 = null;
                                                tL_document = null;
                                                if (tL_document == null) {
                                                }
                                                if (charSequence != null) {
                                                }
                                                hashMap = new HashMap();
                                                if (str17 != null) {
                                                }
                                                if (z3) {
                                                }
                                                if (str20 != null) {
                                                }
                                                int i9222222222 = 0;
                                                if (numArr != null) {
                                                }
                                                z8 = false;
                                                if (z7) {
                                                }
                                                final TLRPC.TL_document tL_document52222222222 = tL_document2;
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document52222222222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                                    }
                                                });
                                                return 0;
                                            }
                                        } else {
                                            str27 = str29;
                                            obj = "opus";
                                            bitmap4 = null;
                                        }
                                    } catch (Exception e10) {
                                        e = e10;
                                        str27 = str29;
                                    }
                                } catch (Exception e11) {
                                    e = e11;
                                    str8 = name;
                                    obj = "opus";
                                    str9 = lowerCase;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                if (mediaMetadataRetriever2 == null) {
                                    throw th;
                                }
                                try {
                                    mediaMetadataRetriever2.release();
                                    throw th;
                                } catch (Exception e12) {
                                    FileLog.e(e12);
                                    throw th;
                                }
                            }
                        } catch (Exception e13) {
                            e = e13;
                            str8 = name;
                            obj = "opus";
                            str9 = lowerCase;
                            mediaMetadataRetriever2 = null;
                        } catch (Throwable th4) {
                            th = th4;
                            mediaMetadataRetriever = null;
                            th = th;
                            mediaMetadataRetriever2 = mediaMetadataRetriever;
                            if (mediaMetadataRetriever2 == null) {
                            }
                        }
                        if (messageObject3 == null) {
                            str9 = str28;
                            try {
                            } catch (Exception e14) {
                                e = e14;
                                bitmap3 = bitmap4;
                                FileLog.e(e);
                                if (mediaMetadataRetriever2 != null) {
                                }
                                str13 = str26;
                                cover = bitmap3;
                                str12 = str27;
                                z5 = false;
                                Bitmap bitmap52222222 = cover;
                                if (i4 == 0) {
                                }
                                if (str2 != null) {
                                }
                                z6 = false;
                                if (z6) {
                                }
                                obj3 = "mp3";
                                str16 = "";
                                z7 = isEncryptedDialog;
                                str17 = str15;
                                str18 = str5;
                                str19 = str8;
                                bitmap = bitmap52222222;
                                obj4 = obj;
                                obj5 = obj2;
                                j3 = 0;
                                i5 = -1;
                                str20 = null;
                                tL_document = null;
                                if (tL_document == null) {
                                }
                                if (charSequence != null) {
                                }
                                hashMap = new HashMap();
                                if (str17 != null) {
                                }
                                if (z3) {
                                }
                                if (str20 != null) {
                                }
                                int i92222222222 = 0;
                                if (numArr != null) {
                                }
                                z8 = false;
                                if (z7) {
                                }
                                final TLRPC.TL_document tL_document522222222222 = tL_document2;
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document522222222222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                    }
                                });
                                return 0;
                            }
                            if (str9.equals("ogg")) {
                                bitmap3 = bitmap4;
                                if (MediaController.isOpusFile(file.getAbsolutePath()) == 1) {
                                    z9 = true;
                                    mediaMetadataRetriever2.release();
                                    str13 = str26;
                                    str12 = str27;
                                    z5 = z9;
                                    cover = bitmap3;
                                    Bitmap bitmap522222222 = cover;
                                    if (i4 == 0) {
                                    }
                                    if (str2 != null) {
                                    }
                                    z6 = false;
                                    if (z6) {
                                    }
                                    obj3 = "mp3";
                                    str16 = "";
                                    z7 = isEncryptedDialog;
                                    str17 = str15;
                                    str18 = str5;
                                    str19 = str8;
                                    bitmap = bitmap522222222;
                                    obj4 = obj;
                                    obj5 = obj2;
                                    j3 = 0;
                                    i5 = -1;
                                    str20 = null;
                                    tL_document = null;
                                    if (tL_document == null) {
                                    }
                                    if (charSequence != null) {
                                    }
                                    hashMap = new HashMap();
                                    if (str17 != null) {
                                    }
                                    if (z3) {
                                    }
                                    if (str20 != null) {
                                    }
                                    int i922222222222 = 0;
                                    if (numArr != null) {
                                    }
                                    z8 = false;
                                    if (z7) {
                                    }
                                    final TLRPC.TL_document tL_document5222222222222 = tL_document2;
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document5222222222222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                        }
                                    });
                                    return 0;
                                }
                                z9 = false;
                                mediaMetadataRetriever2.release();
                                str13 = str26;
                                str12 = str27;
                                z5 = z9;
                                cover = bitmap3;
                                Bitmap bitmap5222222222 = cover;
                                if (i4 == 0) {
                                }
                                if (str2 != null) {
                                }
                                z6 = false;
                                if (z6) {
                                }
                                obj3 = "mp3";
                                str16 = "";
                                z7 = isEncryptedDialog;
                                str17 = str15;
                                str18 = str5;
                                str19 = str8;
                                bitmap = bitmap5222222222;
                                obj4 = obj;
                                obj5 = obj2;
                                j3 = 0;
                                i5 = -1;
                                str20 = null;
                                tL_document = null;
                                if (tL_document == null) {
                                }
                                if (charSequence != null) {
                                }
                                hashMap = new HashMap();
                                if (str17 != null) {
                                }
                                if (z3) {
                                }
                                if (str20 != null) {
                                }
                                int i9222222222222 = 0;
                                if (numArr != null) {
                                }
                                z8 = false;
                                if (z7) {
                                }
                                final TLRPC.TL_document tL_document52222222222222 = tL_document2;
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document52222222222222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                                    }
                                });
                                return 0;
                            }
                        } else {
                            str9 = str28;
                        }
                        bitmap3 = bitmap4;
                        z9 = false;
                        mediaMetadataRetriever2.release();
                        str13 = str26;
                        str12 = str27;
                        z5 = z9;
                        cover = bitmap3;
                        Bitmap bitmap52222222222 = cover;
                        if (i4 == 0) {
                        }
                        if (str2 != null) {
                        }
                        z6 = false;
                        if (z6) {
                        }
                        obj3 = "mp3";
                        str16 = "";
                        z7 = isEncryptedDialog;
                        str17 = str15;
                        str18 = str5;
                        str19 = str8;
                        bitmap = bitmap52222222222;
                        obj4 = obj;
                        obj5 = obj2;
                        j3 = 0;
                        i5 = -1;
                        str20 = null;
                        tL_document = null;
                        if (tL_document == null) {
                        }
                        if (charSequence != null) {
                        }
                        hashMap = new HashMap();
                        if (str17 != null) {
                        }
                        if (z3) {
                        }
                        if (str20 != null) {
                        }
                        int i92222222222222 = 0;
                        if (numArr != null) {
                        }
                        z8 = false;
                        if (z7) {
                        }
                        final TLRPC.TL_document tL_document522222222222222 = tL_document2;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document522222222222222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                            }
                        });
                        return 0;
                    }
                    str8 = name;
                    obj = "opus";
                    str9 = lowerCase;
                    str13 = null;
                    str12 = null;
                    cover = null;
                    i4 = 0;
                }
                z5 = false;
                Bitmap bitmap522222222222 = cover;
                if (i4 == 0) {
                }
                if (str2 != null) {
                }
                z6 = false;
                if (z6) {
                }
                obj3 = "mp3";
                str16 = "";
                z7 = isEncryptedDialog;
                str17 = str15;
                str18 = str5;
                str19 = str8;
                bitmap = bitmap522222222222;
                obj4 = obj;
                obj5 = obj2;
                j3 = 0;
                i5 = -1;
                str20 = null;
                tL_document = null;
                if (tL_document == null) {
                }
                if (charSequence != null) {
                }
                hashMap = new HashMap();
                if (str17 != null) {
                }
                if (z3) {
                }
                if (str20 != null) {
                }
                int i922222222222222 = 0;
                if (numArr != null) {
                }
                z8 = false;
                if (z7) {
                }
                final TLRPC.TL_document tL_document5222222222222222 = tL_document2;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
                    @Override // java.lang.Runnable
                    public final void run() {
                        SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document5222222222222222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
                    }
                });
                return 0;
            }
            str6 = str5.substring(lastIndexOf + 1);
        }
        str7 = str6;
        lowerCase = str7.toLowerCase();
        if (lowerCase.equals("mp3")) {
        }
        str8 = name;
        obj = "opus";
        str9 = lowerCase;
        audioInfo = AudioInfo.getAudioInfo(file);
        if (audioInfo != null) {
        }
        str13 = null;
        str12 = null;
        cover = null;
        i4 = 0;
        z5 = false;
        Bitmap bitmap5222222222222 = cover;
        if (i4 == 0) {
        }
        if (str2 != null) {
        }
        z6 = false;
        if (z6) {
        }
        obj3 = "mp3";
        str16 = "";
        z7 = isEncryptedDialog;
        str17 = str15;
        str18 = str5;
        str19 = str8;
        bitmap = bitmap5222222222222;
        obj4 = obj;
        obj5 = obj2;
        j3 = 0;
        i5 = -1;
        str20 = null;
        tL_document = null;
        if (tL_document == null) {
        }
        if (charSequence != null) {
        }
        hashMap = new HashMap();
        if (str17 != null) {
        }
        if (z3) {
        }
        if (str20 != null) {
        }
        int i9222222222222222 = 0;
        if (numArr != null) {
        }
        z8 = false;
        if (z7) {
        }
        final TLRPC.TL_document tL_document52222222222222222 = tL_document2;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda89
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingDocumentInternal$76(MessageObject.this, accountInstance, tL_document52222222222222222, str21, hashMap, str20, j, messageObject, messageObject2, charSequence2, arrayList, z2, i, storyItem, replyQuote, str4, i2, j2, z4);
            }
        });
        return 0;
    }

    public static void prepareSendingDocuments(final AccountInstance accountInstance, final ArrayList<String> arrayList, final ArrayList<String> arrayList2, final ArrayList<Uri> arrayList3, final String str, final String str2, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final MessageObject messageObject3, final boolean z, final int i, final InputContentInfoCompat inputContentInfoCompat, final String str3, final int i2, final long j2, final boolean z2) {
        if (arrayList == null && arrayList2 == null && arrayList3 == null) {
            return;
        }
        if (arrayList == null || arrayList2 == null || arrayList.size() == arrayList2.size()) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.lambda$prepareSendingDocuments$80(j, arrayList, str, accountInstance, i, arrayList2, str2, messageObject, messageObject2, storyItem, replyQuote, messageObject3, z, inputContentInfoCompat, str3, i2, j2, z2, arrayList3);
                }
            });
        }
    }

    public static void prepareSendingMedia(final AccountInstance accountInstance, final ArrayList<SendingMediaInfo> arrayList, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final boolean z, boolean z2, final MessageObject messageObject3, final boolean z3, final int i, int i2, final boolean z4, final InputContentInfoCompat inputContentInfoCompat, final String str, final int i3, final long j2, final boolean z5) {
        final boolean z6;
        if (arrayList.isEmpty()) {
            return;
        }
        int size = arrayList.size();
        int i4 = 0;
        while (true) {
            if (i4 >= size) {
                z6 = z2;
                break;
            } else {
                if (arrayList.get(i4).ttl > 0) {
                    z6 = false;
                    break;
                }
                i4++;
            }
        }
        mediaSendQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingMedia$94(arrayList, j, z, z6, accountInstance, messageObject3, messageObject, messageObject2, z3, i, storyItem, replyQuote, str, i3, j2, z5, inputContentInfoCompat, z4);
            }
        });
    }

    public static void prepareSendingPhoto(AccountInstance accountInstance, String str, Uri uri, long j, MessageObject messageObject, MessageObject messageObject2, ChatActivity.ReplyQuote replyQuote, CharSequence charSequence, ArrayList<TLRPC.MessageEntity> arrayList, ArrayList<TLRPC.InputDocument> arrayList2, InputContentInfoCompat inputContentInfoCompat, int i, MessageObject messageObject3, boolean z, int i2, int i3, String str2, int i4) {
        prepareSendingPhoto(accountInstance, str, null, uri, j, messageObject, messageObject2, null, null, arrayList, arrayList2, inputContentInfoCompat, i, messageObject3, null, z, i2, i3, false, charSequence, str2, i4, 0L);
    }

    public static void prepareSendingPhoto(AccountInstance accountInstance, String str, String str2, Uri uri, long j, MessageObject messageObject, MessageObject messageObject2, TL_stories.StoryItem storyItem, ChatActivity.ReplyQuote replyQuote, ArrayList<TLRPC.MessageEntity> arrayList, ArrayList<TLRPC.InputDocument> arrayList2, InputContentInfoCompat inputContentInfoCompat, int i, MessageObject messageObject3, VideoEditedInfo videoEditedInfo, boolean z, int i2, int i3, boolean z2, CharSequence charSequence, String str3, int i4, long j2) {
        SendingMediaInfo sendingMediaInfo = new SendingMediaInfo();
        sendingMediaInfo.path = str;
        sendingMediaInfo.thumbPath = str2;
        sendingMediaInfo.uri = uri;
        if (charSequence != null) {
            sendingMediaInfo.caption = charSequence.toString();
        }
        sendingMediaInfo.entities = arrayList;
        sendingMediaInfo.ttl = i;
        if (arrayList2 != null) {
            sendingMediaInfo.masks = new ArrayList<>(arrayList2);
        }
        sendingMediaInfo.videoEditedInfo = videoEditedInfo;
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(sendingMediaInfo);
        prepareSendingMedia(accountInstance, arrayList3, j, messageObject, messageObject2, null, replyQuote, z2, false, messageObject3, z, i2, i3, false, inputContentInfoCompat, str3, i4, j2, false);
    }

    public static void prepareSendingText(final AccountInstance accountInstance, final String str, final long j, final long j2, final boolean z, final int i, final long j3) {
        accountInstance.getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingText$88(str, j2, accountInstance, j, z, i, j3);
            }
        });
    }

    public static void prepareSendingText(AccountInstance accountInstance, String str, long j, boolean z, int i, long j2) {
        prepareSendingText(accountInstance, str, j, 0L, z, i, j2);
    }

    public static void prepareSendingVideo(final AccountInstance accountInstance, final String str, final VideoEditedInfo videoEditedInfo, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final ArrayList<TLRPC.MessageEntity> arrayList, final int i, final MessageObject messageObject3, final boolean z, final int i2, final boolean z2, final boolean z3, final CharSequence charSequence, final String str2, final int i3, final long j2) {
        if (str == null || str.length() == 0) {
            return;
        }
        new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.lambda$prepareSendingVideo$96(VideoEditedInfo.this, str, j, i, accountInstance, charSequence, messageObject3, z3, messageObject, messageObject2, arrayList, z, i2, storyItem, replyQuote, i3, str2, j2, z2);
            }
        }).start();
    }

    private void putToDelayedMessages(String str, DelayedMessage delayedMessage) {
        ArrayList<DelayedMessage> arrayList = this.delayedMessages.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.delayedMessages.put(str, arrayList);
        }
        arrayList.add(delayedMessage);
    }

    private void revertEditingMessageObject(MessageObject messageObject) {
        messageObject.cancelEditing = true;
        TLRPC.Message message = messageObject.messageOwner;
        message.media = messageObject.previousMedia;
        message.message = messageObject.previousMessage;
        ArrayList<TLRPC.MessageEntity> arrayList = messageObject.previousMessageEntities;
        message.entities = arrayList;
        message.attachPath = messageObject.previousAttachPath;
        message.send_state = 0;
        message.flags = arrayList != null ? message.flags | 128 : message.flags & (-129);
        messageObject.previousMedia = null;
        messageObject.previousMessage = null;
        messageObject.previousMessageEntities = null;
        messageObject.previousAttachPath = null;
        messageObject.videoEditedInfo = null;
        messageObject.type = -1;
        messageObject.setType();
        messageObject.caption = null;
        if (messageObject.type != 0) {
            messageObject.generateCaption();
        } else {
            messageObject.resetLayout();
        }
        ArrayList<TLRPC.Message> arrayList2 = new ArrayList<>();
        arrayList2.add(messageObject.messageOwner);
        getMessagesStorage().putMessages(arrayList2, false, true, false, 0, messageObject.scheduled ? 1 : 0, 0L);
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(messageObject);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.replaceMessagesObjects, Long.valueOf(messageObject.getDialogId()), arrayList3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendLocation(Location location) {
        TLRPC.TL_messageMediaGeo tL_messageMediaGeo = new TLRPC.TL_messageMediaGeo();
        TLRPC.TL_geoPoint tL_geoPoint = new TLRPC.TL_geoPoint();
        tL_messageMediaGeo.geo = tL_geoPoint;
        tL_geoPoint.lat = AndroidUtilities.fixLocationCoord(location.getLatitude());
        tL_messageMediaGeo.geo._long = AndroidUtilities.fixLocationCoord(location.getLongitude());
        Iterator<Map.Entry<String, MessageObject>> it = this.waitingForLocation.entrySet().iterator();
        while (it.hasNext()) {
            MessageObject value = it.next().getValue();
            sendMessage(SendMessageParams.of((TLRPC.MessageMedia) tL_messageMediaGeo, value.getDialogId(), value, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, true, 0));
        }
    }

    private void sendReadyToSendGroup(DelayedMessage delayedMessage, boolean z, boolean z2) {
        DelayedMessage findMaxDelayedMessageForMessageId;
        TLObject tLObject;
        DelayedMessage findMaxDelayedMessageForMessageId2;
        ArrayList<MessageObject> arrayList;
        if (delayedMessage.messageObjects.isEmpty()) {
            delayedMessage.markAsError();
            return;
        }
        String str = "group_" + delayedMessage.groupId;
        int i = delayedMessage.finalGroupMessage;
        ArrayList<MessageObject> arrayList2 = delayedMessage.messageObjects;
        int i2 = 1;
        if (i != arrayList2.get(arrayList2.size() - 1).getId()) {
            if (z) {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("final message not added, add");
                }
                putToDelayedMessages(str, delayedMessage);
                return;
            } else {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("final message not added");
                    return;
                }
                return;
            }
        }
        int i3 = 0;
        if (z) {
            this.delayedMessages.remove(str);
            if (!delayedMessage.scheduled) {
                MessageObject messageObject = delayedMessage.obj;
                i2 = ((messageObject == null || !messageObject.isQuickReply()) && ((arrayList = delayedMessage.messageObjects) == null || arrayList.isEmpty() || !delayedMessage.messageObjects.get(0).isQuickReply())) ? 0 : 5;
            }
            if (delayedMessage.paidMedia) {
                ArrayList<MessageObject> arrayList3 = new ArrayList<>();
                arrayList3.add(delayedMessage.messageObjects.get(0));
                ArrayList<TLRPC.Message> arrayList4 = new ArrayList<>();
                arrayList4.add(delayedMessage.messages.get(0));
                getMessagesStorage().putMessages(arrayList4, false, true, false, 0, i2, 0L);
                getMessagesController().updateInterfaceWithMessages(delayedMessage.peer, arrayList3, i2);
            } else {
                getMessagesStorage().putMessages(delayedMessage.messages, false, true, false, 0, i2, 0L);
                getMessagesController().updateInterfaceWithMessages(delayedMessage.peer, delayedMessage.messageObjects, i2);
            }
            if (!delayedMessage.scheduled) {
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
            }
            if (BuildVars.DEBUG_VERSION) {
                FileLog.d("add message");
            }
        }
        TLObject tLObject2 = delayedMessage.sendRequest;
        if (tLObject2 instanceof TLRPC.TL_messages_sendMultiMedia) {
            TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) tLObject2;
            while (i3 < tL_messages_sendMultiMedia.multi_media.size()) {
                TLRPC.InputMedia inputMedia = tL_messages_sendMultiMedia.multi_media.get(i3).media;
                if ((inputMedia instanceof TLRPC.TL_inputMediaUploadedPhoto) || (inputMedia instanceof TLRPC.TL_inputMediaUploadedDocument)) {
                    if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("multi media not ready");
                        return;
                    }
                    return;
                }
                i3++;
            }
            if (z2 && (findMaxDelayedMessageForMessageId2 = findMaxDelayedMessageForMessageId(delayedMessage.finalGroupMessage, delayedMessage.peer)) != null) {
                findMaxDelayedMessageForMessageId2.addDelayedRequest(delayedMessage.sendRequest, delayedMessage.messageObjects, delayedMessage.originalPaths, delayedMessage.parentObjects, delayedMessage, delayedMessage.scheduled);
                ArrayList<DelayedMessageSendAfterRequest> arrayList5 = delayedMessage.requests;
                if (arrayList5 != null) {
                    findMaxDelayedMessageForMessageId2.requests.addAll(arrayList5);
                }
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("has maxDelayedMessage, delay");
                    return;
                }
                return;
            }
        } else {
            if (tLObject2 instanceof TLRPC.TL_messages_sendMedia) {
                TLRPC.InputMedia inputMedia2 = ((TLRPC.TL_messages_sendMedia) tLObject2).media;
                if (inputMedia2 instanceof TLRPC.TL_inputMediaPaidMedia) {
                    TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) inputMedia2;
                    while (i3 < tL_inputMediaPaidMedia.extended_media.size()) {
                        TLRPC.InputMedia inputMedia3 = tL_inputMediaPaidMedia.extended_media.get(i3);
                        if ((inputMedia3 instanceof TLRPC.TL_inputMediaUploadedPhoto) || (inputMedia3 instanceof TLRPC.TL_inputMediaUploadedDocument)) {
                            if (BuildVars.DEBUG_VERSION) {
                                FileLog.d("multi media not ready");
                                return;
                            }
                            return;
                        }
                        i3++;
                    }
                    if (z2 && (findMaxDelayedMessageForMessageId = findMaxDelayedMessageForMessageId(delayedMessage.finalGroupMessage, delayedMessage.peer)) != null) {
                        findMaxDelayedMessageForMessageId.addDelayedRequest(delayedMessage.sendRequest, delayedMessage.messageObjects, delayedMessage.originalPaths, delayedMessage.parentObjects, delayedMessage, delayedMessage.scheduled);
                        ArrayList<DelayedMessageSendAfterRequest> arrayList6 = delayedMessage.requests;
                        if (arrayList6 != null) {
                            findMaxDelayedMessageForMessageId.requests.addAll(arrayList6);
                        }
                        if (BuildVars.DEBUG_VERSION) {
                            FileLog.d("has maxDelayedMessage, delay");
                            return;
                        }
                        return;
                    }
                }
            }
            TLRPC.TL_messages_sendEncryptedMultiMedia tL_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessage.sendEncryptedRequest;
            while (i3 < tL_messages_sendEncryptedMultiMedia.files.size()) {
                if (tL_messages_sendEncryptedMultiMedia.files.get(i3) instanceof TLRPC.TL_inputEncryptedFile) {
                    return;
                } else {
                    i3++;
                }
            }
        }
        TLObject tLObject3 = delayedMessage.sendRequest;
        if (tLObject3 instanceof TLRPC.TL_messages_sendMultiMedia) {
            tLObject = (TLRPC.TL_messages_sendMultiMedia) tLObject3;
        } else {
            if (!(tLObject3 instanceof TLRPC.TL_messages_sendMedia)) {
                getSecretChatHelper().performSendEncryptedRequest((TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessage.sendEncryptedRequest, delayedMessage);
                delayedMessage.sendDelayedRequests();
            }
            tLObject = (TLRPC.TL_messages_sendMedia) tLObject3;
        }
        performSendMessageRequestMulti(tLObject, delayedMessage.messageObjects, delayedMessage.originalPaths, delayedMessage.parentObjects, delayedMessage, delayedMessage.scheduled);
        delayedMessage.sendDelayedRequests();
    }

    public static boolean shouldSendWebPAsSticker(String str, Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (str != null) {
            try {
                BitmapFactory.decodeFile(str, options);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else {
            try {
                InputStream openInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                try {
                    BitmapFactory.decodeStream(openInputStream, null, options);
                    if (openInputStream != null) {
                        openInputStream.close();
                    }
                } finally {
                }
            } catch (Exception unused) {
            }
        }
        return options.outWidth < 800 && options.outHeight < 800;
    }

    private void updateMediaPaths(MessageObject messageObject, TLRPC.Message message, int i, String str, boolean z) {
        updateMediaPaths(messageObject, message, i, Collections.singletonList(str), z, -1);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x01df  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x0791  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x0824  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0827  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0794  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateMediaPaths(MessageObject messageObject, TLRPC.Message message, int i, List<String> list, boolean z, int i2) {
        TLRPC.MessageMedia messageMedia;
        TLRPC.MessageMedia messageMedia2;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia;
        boolean z2;
        TLRPC.PhotoSize photoSize;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia2;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia3;
        String str;
        TLRPC.MessageMedia messageMedia3;
        TLRPC.FileLocation fileLocation;
        byte[] bArr;
        SendMessagesHelper sendMessagesHelper;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia4;
        String str2;
        TLRPC.FileLocation fileLocation2;
        TLRPC.MessageMedia messageMedia4;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia5;
        TLRPC.PhotoSize photoSize2;
        int i3;
        TLRPC.PhotoSize photoSize3;
        TLRPC.Message message2;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia6;
        TLRPC.Message message3;
        String str3;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia7;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia8;
        TLRPC.PhotoSize photoSize4;
        TLRPC.PhotoSize photoSize5;
        TLObject tLObject;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia9;
        TLRPC.PhotoSize photoSize6;
        boolean z3;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia10;
        TLRPC.MessageMedia messageMedia5;
        TLRPC.MessageMedia messageMedia6;
        TLRPC.Document document;
        TLRPC.Photo photo;
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        TLObject tLObject2;
        TLRPC.MessageMedia messageMedia7;
        TLRPC.WebPage webPage;
        TLRPC.Document document2;
        TLRPC.MessageMedia messageMedia8;
        TLRPC.WebPage webPage2;
        TLRPC.Photo photo2;
        TLRPC.MessageMedia messageMedia9;
        TLRPC.Document document3;
        TLRPC.MessageMedia messageMedia10;
        TLRPC.Photo photo3;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia11;
        TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia12;
        boolean z4;
        StringBuilder sb;
        String sb2;
        SendMessagesHelper sendMessagesHelper2 = this;
        TLRPC.Message message4 = messageObject.messageOwner;
        String str4 = (list.isEmpty() || Math.max(0, i2) >= list.size()) ? null : list.get(Math.max(0, i2));
        TLRPC.MessageMedia messageMedia11 = message == null ? null : message.media;
        TLRPC.MessageMedia messageMedia12 = message4 == null ? null : message4.media;
        TLRPC.MessageMedia messageMedia13 = message4.media;
        if (messageMedia13 != null) {
            if (messageMedia13.storyItem != null) {
                message.media = messageMedia13;
            } else {
                if (messageObject.isLiveLocation()) {
                    TLRPC.MessageMedia messageMedia14 = message.media;
                    if (messageMedia14 instanceof TLRPC.TL_messageMediaGeoLive) {
                        message4.media.period = messageMedia14.period;
                    }
                }
                if (messageObject.isDice()) {
                    ((TLRPC.TL_messageMediaDice) message4.media).value = ((TLRPC.TL_messageMediaDice) message.media).value;
                } else {
                    TLRPC.MessageMedia messageMedia15 = message4.media;
                    TLRPC.Photo photo4 = messageMedia15.photo;
                    if (photo4 != null) {
                        photoSize6 = FileLoader.getClosestPhotoSizeWithSize(photo4.sizes, 40);
                        closestPhotoSizeWithSize = (message == null || (messageMedia10 = message.media) == null || (photo3 = messageMedia10.photo) == null) ? photoSize6 : FileLoader.getClosestPhotoSizeWithSize(photo3.sizes, 40);
                        tLObject2 = message4.media.photo;
                    } else {
                        TLRPC.Document document4 = messageMedia15.document;
                        if (document4 != null) {
                            photoSize6 = FileLoader.getClosestPhotoSizeWithSize(document4.thumbs, 40);
                            closestPhotoSizeWithSize = (message == null || (messageMedia9 = message.media) == null || (document3 = messageMedia9.document) == null) ? photoSize6 : FileLoader.getClosestPhotoSizeWithSize(document3.thumbs, 40);
                            tLObject2 = message4.media.document;
                        } else {
                            TLRPC.WebPage webPage3 = messageMedia15.webpage;
                            if (webPage3 != null) {
                                TLRPC.Photo photo5 = webPage3.photo;
                                if (photo5 != null) {
                                    photoSize6 = FileLoader.getClosestPhotoSizeWithSize(photo5.sizes, 40);
                                    closestPhotoSizeWithSize = (message == null || (messageMedia8 = message.media) == null || (webPage2 = messageMedia8.webpage) == null || (photo2 = webPage2.photo) == null) ? photoSize6 : FileLoader.getClosestPhotoSizeWithSize(photo2.sizes, 40);
                                    tLObject2 = message4.media.webpage.photo;
                                } else {
                                    TLRPC.Document document5 = webPage3.document;
                                    if (document5 != null) {
                                        photoSize6 = FileLoader.getClosestPhotoSizeWithSize(document5.thumbs, 40);
                                        closestPhotoSizeWithSize = (message == null || (messageMedia7 = message.media) == null || (webPage = messageMedia7.webpage) == null || (document2 = webPage.document) == null) ? photoSize6 : FileLoader.getClosestPhotoSizeWithSize(document2.thumbs, 40);
                                        tLObject2 = message4.media.webpage.document;
                                    }
                                }
                            } else if ((messageMedia15 instanceof TLRPC.TL_messageMediaPaidMedia) && (messageMedia11 instanceof TLRPC.TL_messageMediaPaidMedia)) {
                                TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageMedia15;
                                TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia2 = (TLRPC.TL_messageMediaPaidMedia) messageMedia11;
                                if (tL_messageMediaPaidMedia.extended_media.isEmpty() || tL_messageMediaPaidMedia2.extended_media.isEmpty()) {
                                    return;
                                }
                                if (i2 == -1) {
                                    for (int i4 = 0; i4 < tL_messageMediaPaidMedia.extended_media.size(); i4++) {
                                        updateMediaPaths(messageObject, message, i, list, z, i4);
                                    }
                                    return;
                                }
                                boolean z5 = tL_messageMediaPaidMedia2.extended_media.size() > 1;
                                if (i2 < 0 || i2 >= tL_messageMediaPaidMedia2.extended_media.size()) {
                                    return;
                                }
                                TLRPC.MessageExtendedMedia messageExtendedMedia = tL_messageMediaPaidMedia2.extended_media.get(i2);
                                if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                                    tL_messageExtendedMedia7 = (TLRPC.TL_messageExtendedMedia) messageExtendedMedia;
                                    messageMedia11 = tL_messageExtendedMedia7.media;
                                } else {
                                    tL_messageExtendedMedia7 = null;
                                }
                                TLRPC.MessageExtendedMedia messageExtendedMedia2 = tL_messageMediaPaidMedia.extended_media.get(i2);
                                if (messageExtendedMedia2 instanceof TLRPC.TL_messageExtendedMedia) {
                                    tL_messageExtendedMedia8 = (TLRPC.TL_messageExtendedMedia) messageExtendedMedia2;
                                    messageMedia12 = tL_messageExtendedMedia8.media;
                                    TLRPC.Photo photo6 = messageMedia12.photo;
                                    if (photo6 != null) {
                                        photoSize5 = FileLoader.getClosestPhotoSizeWithSize(photo6.sizes, 40);
                                        photoSize4 = (messageMedia11 == null || (photo = messageMedia11.photo) == null) ? photoSize5 : FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 40);
                                        tLObject = messageMedia12.photo;
                                    } else {
                                        TLRPC.Document document6 = messageMedia12.document;
                                        if (document6 != null) {
                                            photoSize5 = FileLoader.getClosestPhotoSizeWithSize(document6.thumbs, 40);
                                            photoSize4 = (messageMedia11 == null || (document = messageMedia11.document) == null) ? photoSize5 : FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 40);
                                            tLObject = messageMedia12.document;
                                        }
                                    }
                                    TLRPC.MessageMedia messageMedia16 = messageMedia11;
                                    tL_messageExtendedMedia9 = tL_messageExtendedMedia8;
                                    photoSize6 = photoSize5;
                                    z3 = z5;
                                    tL_messageExtendedMedia10 = tL_messageExtendedMedia7;
                                    messageMedia5 = messageMedia12;
                                    photoSize = photoSize4;
                                    messageMedia6 = messageMedia16;
                                    if ((photoSize instanceof TLRPC.TL_photoStrippedSize) || !(photoSize6 instanceof TLRPC.TL_photoStrippedSize)) {
                                        tL_messageExtendedMedia11 = tL_messageExtendedMedia9;
                                        tL_messageExtendedMedia12 = tL_messageExtendedMedia10;
                                        z4 = z3;
                                    } else {
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("stripped");
                                        tL_messageExtendedMedia11 = tL_messageExtendedMedia9;
                                        sb3.append(FileRefController.getKeyForParentObject(messageObject));
                                        String sb4 = sb3.toString();
                                        if (message != null) {
                                            sb = new StringBuilder();
                                            sb.append("stripped");
                                            sb.append(FileRefController.getKeyForParentObject(message));
                                            sb2 = sb.toString();
                                            tL_messageExtendedMedia12 = tL_messageExtendedMedia10;
                                            z4 = z3;
                                        } else {
                                            sb = new StringBuilder();
                                            sb.append("strippedmessage");
                                            sb.append(i);
                                            sb.append("_");
                                            tL_messageExtendedMedia12 = tL_messageExtendedMedia10;
                                            z4 = z3;
                                            sb.append(messageObject.getChannelId());
                                            sb.append("_");
                                            sb.append(messageObject.scheduled);
                                            sb2 = sb.toString();
                                        }
                                        ImageLoader.getInstance().replaceImageInCache(sb4, sb2, ImageLocation.getForObject(photoSize, tLObject), z);
                                    }
                                    tL_messageExtendedMedia2 = tL_messageExtendedMedia11;
                                    tL_messageExtendedMedia = tL_messageExtendedMedia12;
                                    messageMedia = messageMedia6;
                                    z2 = z4;
                                    messageMedia2 = messageMedia5;
                                } else {
                                    tL_messageExtendedMedia8 = null;
                                }
                                photoSize4 = null;
                                photoSize5 = null;
                                tLObject = null;
                                TLRPC.MessageMedia messageMedia162 = messageMedia11;
                                tL_messageExtendedMedia9 = tL_messageExtendedMedia8;
                                photoSize6 = photoSize5;
                                z3 = z5;
                                tL_messageExtendedMedia10 = tL_messageExtendedMedia7;
                                messageMedia5 = messageMedia12;
                                photoSize = photoSize4;
                                messageMedia6 = messageMedia162;
                                if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
                                }
                                tL_messageExtendedMedia11 = tL_messageExtendedMedia9;
                                tL_messageExtendedMedia12 = tL_messageExtendedMedia10;
                                z4 = z3;
                                tL_messageExtendedMedia2 = tL_messageExtendedMedia11;
                                tL_messageExtendedMedia = tL_messageExtendedMedia12;
                                messageMedia = messageMedia6;
                                z2 = z4;
                                messageMedia2 = messageMedia5;
                            }
                        }
                    }
                    tLObject = tLObject2;
                    tL_messageExtendedMedia10 = null;
                    z3 = false;
                    messageMedia5 = messageMedia12;
                    photoSize = closestPhotoSizeWithSize;
                    messageMedia6 = messageMedia11;
                    tL_messageExtendedMedia9 = null;
                    if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
                    }
                    tL_messageExtendedMedia11 = tL_messageExtendedMedia9;
                    tL_messageExtendedMedia12 = tL_messageExtendedMedia10;
                    z4 = z3;
                    tL_messageExtendedMedia2 = tL_messageExtendedMedia11;
                    tL_messageExtendedMedia = tL_messageExtendedMedia12;
                    messageMedia = messageMedia6;
                    z2 = z4;
                    messageMedia2 = messageMedia5;
                }
            }
            messageMedia6 = messageMedia11;
            messageMedia5 = messageMedia12;
            photoSize6 = null;
            tL_messageExtendedMedia9 = null;
            photoSize = null;
            tL_messageExtendedMedia10 = null;
            z3 = false;
            tLObject = null;
            if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
            }
            tL_messageExtendedMedia11 = tL_messageExtendedMedia9;
            tL_messageExtendedMedia12 = tL_messageExtendedMedia10;
            z4 = z3;
            tL_messageExtendedMedia2 = tL_messageExtendedMedia11;
            tL_messageExtendedMedia = tL_messageExtendedMedia12;
            messageMedia = messageMedia6;
            z2 = z4;
            messageMedia2 = messageMedia5;
        } else {
            messageMedia = messageMedia11;
            messageMedia2 = messageMedia12;
            tL_messageExtendedMedia = null;
            z2 = false;
            photoSize = null;
            tL_messageExtendedMedia2 = null;
        }
        if (message == null) {
            return;
        }
        if ((messageMedia instanceof TLRPC.TL_messageMediaPhoto) && messageMedia.photo != null && (messageMedia2 instanceof TLRPC.TL_messageMediaPhoto) && messageMedia2.photo != null) {
            if (messageMedia.ttl_seconds != 0 || messageObject.scheduled) {
                tL_messageExtendedMedia5 = tL_messageExtendedMedia;
                photoSize2 = photoSize;
                i3 = 0;
            } else {
                MessagesStorage messagesStorage = getMessagesStorage();
                TLRPC.Photo photo7 = messageMedia.photo;
                tL_messageExtendedMedia5 = tL_messageExtendedMedia;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("sent_");
                photoSize2 = photoSize;
                sb5.append(message.peer_id.channel_id);
                sb5.append("_");
                sb5.append(message.id);
                sb5.append("_");
                sb5.append(DialogObject.getPeerDialogId(message.peer_id));
                sb5.append("_");
                sb5.append(1);
                sb5.append("_");
                sb5.append(MessageObject.getMediaSize(messageMedia2));
                i3 = 0;
                messagesStorage.putSentFile(str4, photo7, 0, sb5.toString());
            }
            if (messageMedia2.photo.sizes.size() == 1 && (messageMedia2.photo.sizes.get(i3).location instanceof TLRPC.TL_fileLocationUnavailable)) {
                messageMedia2.photo.sizes = messageMedia.photo.sizes;
            } else {
                int i5 = 0;
                while (i5 < messageMedia2.photo.sizes.size()) {
                    TLRPC.PhotoSize photoSize7 = messageMedia2.photo.sizes.get(i5);
                    if (photoSize7 == null || photoSize7.location == null || photoSize7.type == null) {
                        photoSize3 = photoSize2;
                        message2 = message4;
                        tL_messageExtendedMedia6 = tL_messageExtendedMedia2;
                    } else {
                        int i6 = 0;
                        while (i6 < messageMedia.photo.sizes.size()) {
                            TLRPC.PhotoSize photoSize8 = messageMedia.photo.sizes.get(i6);
                            if (photoSize8 == null || photoSize8.location == null || (photoSize8 instanceof TLRPC.TL_photoSizeEmpty) || (str3 = photoSize8.type) == null) {
                                message3 = message4;
                                tL_messageExtendedMedia6 = tL_messageExtendedMedia2;
                            } else {
                                tL_messageExtendedMedia6 = tL_messageExtendedMedia2;
                                if ((photoSize7.location.volume_id == -2147483648L && str3.equals(photoSize7.type)) || (photoSize8.w == photoSize7.w && photoSize8.h == photoSize7.h)) {
                                    String str5 = photoSize7.location.volume_id + "_" + photoSize7.location.local_id;
                                    String str6 = photoSize8.location.volume_id + "_" + photoSize8.location.local_id;
                                    if (str5.equals(str6)) {
                                        photoSize3 = photoSize2;
                                        message2 = message4;
                                    } else {
                                        message2 = message4;
                                        new File(FileLoader.getDirectory(4), str5 + ".jpg").renameTo((messageMedia.ttl_seconds != 0 || (messageMedia.photo.sizes.size() != 1 && photoSize8.w <= 90 && photoSize8.h <= 90) || z2) ? new File(FileLoader.getDirectory(4), str6 + ".jpg") : FileLoader.getInstance(sendMessagesHelper2.currentAccount).getPathToAttach(photoSize8));
                                        ImageLoader.getInstance().replaceImageInCache(str5, str6, ImageLocation.getForPhoto(photoSize8, messageMedia.photo), z);
                                        photoSize7.location = photoSize8.location;
                                        photoSize7.size = photoSize8.size;
                                        photoSize3 = photoSize2;
                                    }
                                } else {
                                    message3 = message4;
                                }
                            }
                            i6++;
                            sendMessagesHelper2 = this;
                            tL_messageExtendedMedia2 = tL_messageExtendedMedia6;
                            message4 = message3;
                        }
                        message2 = message4;
                        tL_messageExtendedMedia6 = tL_messageExtendedMedia2;
                        String str7 = photoSize7.location.volume_id + "_" + photoSize7.location.local_id;
                        new File(FileLoader.getDirectory(4), str7 + ".jpg").delete();
                        if ("s".equals(photoSize7.type) && photoSize2 != null) {
                            photoSize3 = photoSize2;
                            messageMedia2.photo.sizes.set(i5, photoSize3);
                            ImageLocation forPhoto = ImageLocation.getForPhoto(photoSize3, messageMedia.photo);
                            ImageLoader.getInstance().replaceImageInCache(str7, forPhoto.getKey(message, null, false), forPhoto, z);
                            i5++;
                            sendMessagesHelper2 = this;
                            tL_messageExtendedMedia2 = tL_messageExtendedMedia6;
                            photoSize2 = photoSize3;
                            message4 = message2;
                        }
                        photoSize3 = photoSize2;
                    }
                    i5++;
                    sendMessagesHelper2 = this;
                    tL_messageExtendedMedia2 = tL_messageExtendedMedia6;
                    photoSize2 = photoSize3;
                    message4 = message2;
                }
            }
            TLRPC.Message message5 = message4;
            TLRPC.TL_messageExtendedMedia tL_messageExtendedMedia13 = tL_messageExtendedMedia2;
            if (!z2) {
                message5.message = message.message;
                message.attachPath = message5.attachPath;
            } else if (tL_messageExtendedMedia13 != null && tL_messageExtendedMedia5 != null) {
                tL_messageExtendedMedia13.attachPath = tL_messageExtendedMedia5.attachPath;
            }
            TLRPC.Photo photo8 = messageMedia2.photo;
            TLRPC.Photo photo9 = messageMedia.photo;
            photo8.id = photo9.id;
            photo8.dc_id = photo9.dc_id;
            photo8.access_hash = photo9.access_hash;
            return;
        }
        if (!(messageMedia instanceof TLRPC.TL_messageMediaDocument) || messageMedia.document == null || !(messageMedia2 instanceof TLRPC.TL_messageMediaDocument) || messageMedia2.document == null) {
            TLRPC.MessageMedia messageMedia17 = message.media;
            if ((!(messageMedia17 instanceof TLRPC.TL_messageMediaContact) || !(message4.media instanceof TLRPC.TL_messageMediaContact)) && !(messageMedia17 instanceof TLRPC.TL_messageMediaWebPage)) {
                if (messageMedia17 instanceof TLRPC.TL_messageMediaGeo) {
                    TLRPC.GeoPoint geoPoint = messageMedia17.geo;
                    TLRPC.GeoPoint geoPoint2 = message4.media.geo;
                    geoPoint.lat = geoPoint2.lat;
                    geoPoint._long = geoPoint2._long;
                    return;
                }
                if ((messageMedia17 instanceof TLRPC.TL_messageMediaGame) || (messageMedia17 instanceof TLRPC.TL_messageMediaInvoice)) {
                    message4.media = messageMedia17;
                    if (!TextUtils.isEmpty(message.message)) {
                        message4.entities = message.entities;
                        message4.message = message.message;
                    }
                    TLRPC.ReplyMarkup replyMarkup = message.reply_markup;
                    if (replyMarkup != null) {
                        message4.reply_markup = replyMarkup;
                        message4.flags |= 64;
                        return;
                    }
                    return;
                }
                if (!(messageMedia17 instanceof TLRPC.TL_messageMediaPoll)) {
                    return;
                }
            }
            message4.media = messageMedia17;
            return;
        }
        if (messageMedia.ttl_seconds == 0) {
            VideoEditedInfo videoEditedInfo = messageObject.videoEditedInfo;
            if (videoEditedInfo == null || (videoEditedInfo.mediaEntities == null && TextUtils.isEmpty(videoEditedInfo.paintPath) && messageObject.videoEditedInfo.cropState == null)) {
                boolean isVideoMessage = MessageObject.isVideoMessage(message);
                if ((isVideoMessage || MessageObject.isGifMessage(message)) && MessageObject.isGifDocument(messageMedia.document) == MessageObject.isGifDocument(messageMedia2.document)) {
                    if (messageObject.scheduled) {
                        tL_messageExtendedMedia3 = tL_messageExtendedMedia2;
                        messageMedia4 = messageMedia2;
                        str = ".jpg";
                    } else {
                        MessageObject messageObject2 = new MessageObject(this.currentAccount, message, false, false);
                        MessagesStorage messagesStorage2 = getMessagesStorage();
                        TLRPC.Document document7 = messageMedia.document;
                        tL_messageExtendedMedia3 = tL_messageExtendedMedia2;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("sent_");
                        messageMedia4 = messageMedia2;
                        str = ".jpg";
                        sb6.append(message.peer_id.channel_id);
                        sb6.append("_");
                        sb6.append(message.id);
                        sb6.append("_");
                        sb6.append(DialogObject.getPeerDialogId(message.peer_id));
                        sb6.append("_");
                        sb6.append(messageObject2.type);
                        sb6.append("_");
                        sb6.append(messageObject2.getSize());
                        messagesStorage2.putSentFile(str4, document7, 2, sb6.toString());
                    }
                    if (isVideoMessage) {
                        message.attachPath = message4.attachPath;
                    }
                } else {
                    tL_messageExtendedMedia3 = tL_messageExtendedMedia2;
                    messageMedia4 = messageMedia2;
                    str = ".jpg";
                    if (!MessageObject.isVoiceMessage(message) && !MessageObject.isRoundVideoMessage(message) && !messageObject.scheduled) {
                        MessageObject messageObject3 = new MessageObject(this.currentAccount, message, false, false);
                        getMessagesStorage().putSentFile(str4, messageMedia.document, 1, "sent_" + message.peer_id.channel_id + "_" + message.id + "_" + DialogObject.getPeerDialogId(message.peer_id) + "_" + messageObject3.type + "_" + messageObject3.getSize());
                    }
                }
                messageMedia3 = messageMedia4;
            } else {
                tL_messageExtendedMedia3 = tL_messageExtendedMedia2;
                messageMedia3 = messageMedia2;
                str = ".jpg";
            }
        } else {
            tL_messageExtendedMedia3 = tL_messageExtendedMedia2;
            str = ".jpg";
            messageMedia3 = messageMedia2;
        }
        TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageMedia3.document.thumbs, 320);
        TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageMedia.document.thumbs, 320);
        if (closestPhotoSizeWithSize2 != null && (fileLocation2 = closestPhotoSizeWithSize2.location) != null && fileLocation2.volume_id == -2147483648L && closestPhotoSizeWithSize3 != null && closestPhotoSizeWithSize3.location != null && !(closestPhotoSizeWithSize3 instanceof TLRPC.TL_photoSizeEmpty) && !(closestPhotoSizeWithSize2 instanceof TLRPC.TL_photoSizeEmpty)) {
            String str8 = closestPhotoSizeWithSize2.location.volume_id + "_" + closestPhotoSizeWithSize2.location.local_id;
            String str9 = closestPhotoSizeWithSize3.location.volume_id + "_" + closestPhotoSizeWithSize3.location.local_id;
            if (!str8.equals(str9)) {
                File directory = FileLoader.getDirectory(4);
                StringBuilder sb7 = new StringBuilder();
                sb7.append(str8);
                String str10 = str;
                sb7.append(str10);
                new File(directory, sb7.toString()).renameTo(new File(FileLoader.getDirectory(4), str9 + str10));
                ImageLoader.getInstance().replaceImageInCache(str8, str9, ImageLocation.getForDocument(closestPhotoSizeWithSize3, messageMedia.document), z);
                closestPhotoSizeWithSize2.location = closestPhotoSizeWithSize3.location;
                closestPhotoSizeWithSize2.size = closestPhotoSizeWithSize3.size;
            }
        } else if (closestPhotoSizeWithSize3 != null && closestPhotoSizeWithSize2 != null && MessageObject.isStickerMessage(message) && (fileLocation = closestPhotoSizeWithSize2.location) != null) {
            closestPhotoSizeWithSize3.location = fileLocation;
        } else if (closestPhotoSizeWithSize2 == null || (closestPhotoSizeWithSize2.location instanceof TLRPC.TL_fileLocationUnavailable) || (closestPhotoSizeWithSize2 instanceof TLRPC.TL_photoSizeEmpty)) {
            messageMedia3.document.thumbs = messageMedia.document.thumbs;
        }
        TLRPC.Document document8 = messageMedia3.document;
        TLRPC.Document document9 = messageMedia.document;
        document8.dc_id = document9.dc_id;
        document8.id = document9.id;
        document8.access_hash = document9.access_hash;
        int i7 = 0;
        while (true) {
            if (i7 >= messageMedia3.document.attributes.size()) {
                bArr = null;
                break;
            }
            TLRPC.DocumentAttribute documentAttribute = messageMedia3.document.attributes.get(i7);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                bArr = documentAttribute.waveform;
                break;
            }
            i7++;
        }
        messageMedia3.document.attributes = messageMedia.document.attributes;
        if (bArr != null) {
            for (int i8 = 0; i8 < messageMedia3.document.attributes.size(); i8++) {
                TLRPC.DocumentAttribute documentAttribute2 = messageMedia3.document.attributes.get(i8);
                if (documentAttribute2 instanceof TLRPC.TL_documentAttributeAudio) {
                    documentAttribute2.waveform = bArr;
                    documentAttribute2.flags |= 4;
                }
            }
        }
        TLRPC.Document document10 = messageMedia3.document;
        TLRPC.Document document11 = messageMedia.document;
        document10.size = document11.size;
        document10.mime_type = document11.mime_type;
        if ((message.flags & 4) == 0 && ((MessageObject.isOut(message) || message.dialog_id == getUserConfig().getClientUserId()) && !MessageObject.isQuickReply(message))) {
            if (MessageObject.isNewGifDocument(messageMedia.document)) {
                if (MessageObject.isDocumentHasAttachedStickers(messageMedia.document) ? getMessagesController().saveGifsWithStickers : true) {
                    getMediaDataController().addRecentGif(messageMedia.document, message.date, true);
                }
            } else if (MessageObject.isStickerDocument(messageMedia.document) || MessageObject.isAnimatedStickerDocument(messageMedia.document, true)) {
                sendMessagesHelper = this;
                getMediaDataController().addRecentSticker(0, message, messageMedia.document, message.date, false);
                tL_messageExtendedMedia4 = tL_messageExtendedMedia3;
                str2 = tL_messageExtendedMedia3 == null ? tL_messageExtendedMedia4.attachPath : message4.attachPath;
                if (str2 == null && str2.startsWith(FileLoader.getDirectory(4).getAbsolutePath()) && !MessageObject.isGifDocument(messageMedia.document)) {
                    File file = new File(str2);
                    File pathToAttach = FileLoader.getInstance(sendMessagesHelper.currentAccount).getPathToAttach(messageMedia.document, messageMedia.ttl_seconds != 0);
                    if (file.renameTo(pathToAttach)) {
                        if (MessageObject.isVideoMessage(message)) {
                            messageObject.attachPathExists = true;
                            return;
                        }
                        messageObject.mediaExists = messageObject.attachPathExists;
                        messageObject.attachPathExists = false;
                        if (tL_messageExtendedMedia4 != null) {
                            tL_messageExtendedMedia4.attachPath = "";
                        } else {
                            message4.attachPath = "";
                        }
                        if (str4 == null || !str4.startsWith("http")) {
                            return;
                        }
                        getMessagesStorage().addRecentLocalFile(str4, pathToAttach.toString(), messageMedia3.document);
                        return;
                    }
                    if (file.exists()) {
                        if (tL_messageExtendedMedia4 != null) {
                            tL_messageExtendedMedia4.attachPath = str2;
                        } else {
                            message.attachPath = str2;
                        }
                    } else if (tL_messageExtendedMedia4 == null) {
                        messageObject.attachPathExists = false;
                    }
                    if (tL_messageExtendedMedia4 == null) {
                        messageObject.mediaExists = pathToAttach.exists();
                    }
                } else {
                    if (tL_messageExtendedMedia4 == null) {
                        tL_messageExtendedMedia4.attachPath = str2;
                        return;
                    }
                    message.attachPath = str2;
                }
                message.message = message4.message;
                return;
            }
        }
        sendMessagesHelper = this;
        tL_messageExtendedMedia4 = tL_messageExtendedMedia3;
        if (tL_messageExtendedMedia3 == null) {
        }
        if (str2 == null) {
        }
        if (tL_messageExtendedMedia4 == null) {
        }
    }

    private void uploadMultiMedia(final DelayedMessage delayedMessage, final TLRPC.InputMedia inputMedia, TLRPC.InputEncryptedFile inputEncryptedFile, String str) {
        if (inputMedia == null) {
            if (inputEncryptedFile != null) {
                TLRPC.TL_messages_sendEncryptedMultiMedia tL_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessage.sendEncryptedRequest;
                int i = 0;
                while (true) {
                    if (i >= tL_messages_sendEncryptedMultiMedia.files.size()) {
                        break;
                    }
                    if (tL_messages_sendEncryptedMultiMedia.files.get(i) == inputEncryptedFile) {
                        putToSendingMessages(delayedMessage.messages.get(i), delayedMessage.scheduled);
                        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploadProgressChanged, str, -1L, -1L, Boolean.FALSE);
                        break;
                    }
                    i++;
                }
                sendReadyToSendGroup(delayedMessage, false, true);
                return;
            }
            return;
        }
        TLRPC.TL_messages_uploadMedia tL_messages_uploadMedia = new TLRPC.TL_messages_uploadMedia();
        tL_messages_uploadMedia.media = inputMedia;
        TLObject tLObject = delayedMessage.sendRequest;
        if (tLObject instanceof TLRPC.TL_messages_sendMultiMedia) {
            TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) tLObject;
            tL_messages_uploadMedia.peer = tL_messages_sendMultiMedia.peer;
            int i2 = 0;
            while (true) {
                if (i2 >= tL_messages_sendMultiMedia.multi_media.size()) {
                    break;
                }
                if (tL_messages_sendMultiMedia.multi_media.get(i2).media == inputMedia) {
                    putToSendingMessages(delayedMessage.messages.get(i2), delayedMessage.scheduled);
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploadProgressChanged, str, -1L, -1L, Boolean.FALSE);
                    break;
                }
                i2++;
            }
        } else if (tLObject instanceof TLRPC.TL_messages_sendMedia) {
            TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) tLObject;
            TLRPC.InputMedia inputMedia2 = tL_messages_sendMedia.media;
            if (inputMedia2 instanceof TLRPC.TL_inputMediaPaidMedia) {
                tL_messages_uploadMedia.peer = tL_messages_sendMedia.peer;
                TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) inputMedia2;
                int i3 = 0;
                while (true) {
                    if (i3 >= tL_inputMediaPaidMedia.extended_media.size()) {
                        break;
                    }
                    if (tL_inputMediaPaidMedia.extended_media.get(i3) == inputMedia) {
                        putToSendingMessages(delayedMessage.messages.get(i3), delayedMessage.scheduled);
                        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploadProgressChanged, str, -1L, -1L, Boolean.FALSE);
                        break;
                    }
                    i3++;
                }
            }
        }
        getConnectionsManager().sendRequest(tL_messages_uploadMedia, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda44
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.lambda$uploadMultiMedia$38(inputMedia, delayedMessage, tLObject2, tL_error);
            }
        });
    }

    private void writePreviousMessageData(TLRPC.Message message, SerializedData serializedData) {
        TLRPC.MessageMedia messageMedia = message.media;
        if (messageMedia == null) {
            new TLRPC.TL_messageMediaEmpty().serializeToStream(serializedData);
        } else {
            messageMedia.serializeToStream(serializedData);
        }
        String str = message.message;
        if (str == null) {
            str = "";
        }
        serializedData.writeString(str);
        String str2 = message.attachPath;
        serializedData.writeString(str2 != null ? str2 : "");
        int size = message.entities.size();
        serializedData.writeInt32(size);
        for (int i = 0; i < size; i++) {
            message.entities.get(i).serializeToStream(serializedData);
        }
    }

    public void cancelSendingMessage(ArrayList<MessageObject> arrayList) {
        boolean z;
        ArrayList arrayList2;
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList<Integer> arrayList5 = new ArrayList<>();
        long j = 0;
        int i = 0;
        boolean z2 = false;
        int i2 = 0;
        int i3 = 0;
        while (i < arrayList.size()) {
            MessageObject messageObject = arrayList.get(i);
            if (messageObject.scheduled) {
                i3 = 1;
            }
            long dialogId = messageObject.getDialogId();
            arrayList5.add(Integer.valueOf(messageObject.getId()));
            if (messageObject.isQuickReply()) {
                i2 = messageObject.getQuickReplyId();
            }
            TLRPC.Message removeFromSendingMessages = removeFromSendingMessages(messageObject.getId(), messageObject.scheduled);
            if (removeFromSendingMessages != null) {
                getConnectionsManager().cancelRequest(removeFromSendingMessages.reqId, true);
            }
            for (Map.Entry<String, ArrayList<DelayedMessage>> entry : this.delayedMessages.entrySet()) {
                ArrayList<DelayedMessage> value = entry.getValue();
                int i4 = 0;
                while (true) {
                    if (i4 >= value.size()) {
                        z = z2;
                        break;
                    }
                    DelayedMessage delayedMessage = value.get(i4);
                    z = z2;
                    if (delayedMessage.type == 4) {
                        MessageObject messageObject2 = null;
                        int i5 = 0;
                        while (true) {
                            if (i5 >= delayedMessage.messageObjects.size()) {
                                i5 = -1;
                                break;
                            }
                            messageObject2 = delayedMessage.messageObjects.get(i5);
                            if (messageObject2.getId() == messageObject.getId()) {
                                removeFromUploadingMessages(messageObject.getId(), messageObject.scheduled);
                                break;
                            }
                            i5++;
                        }
                        if (i5 >= 0) {
                            delayedMessage.messageObjects.remove(i5);
                            delayedMessage.messages.remove(i5);
                            delayedMessage.originalPaths.remove(i5);
                            if (!delayedMessage.parentObjects.isEmpty()) {
                                delayedMessage.parentObjects.remove(i5);
                            }
                            TLObject tLObject = delayedMessage.sendRequest;
                            if (tLObject instanceof TLRPC.TL_messages_sendMultiMedia) {
                                arrayList2 = ((TLRPC.TL_messages_sendMultiMedia) tLObject).multi_media;
                            } else {
                                if (tLObject instanceof TLRPC.TL_messages_sendMedia) {
                                    TLRPC.InputMedia inputMedia = ((TLRPC.TL_messages_sendMedia) tLObject).media;
                                    if (inputMedia instanceof TLRPC.TL_inputMediaPaidMedia) {
                                        arrayList2 = ((TLRPC.TL_inputMediaPaidMedia) inputMedia).extended_media;
                                    }
                                }
                                TLRPC.TL_messages_sendEncryptedMultiMedia tL_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia) delayedMessage.sendEncryptedRequest;
                                tL_messages_sendEncryptedMultiMedia.messages.remove(i5);
                                arrayList2 = tL_messages_sendEncryptedMultiMedia.files;
                            }
                            arrayList2.remove(i5);
                            MediaController.getInstance().cancelVideoConvert(messageObject);
                            String str = (String) delayedMessage.extraHashMap.get(messageObject2);
                            if (str != null) {
                                arrayList3.add(str);
                            }
                            if (delayedMessage.messageObjects.isEmpty()) {
                                delayedMessage.sendDelayedRequests();
                            } else {
                                if (delayedMessage.finalGroupMessage == messageObject.getId()) {
                                    ArrayList<MessageObject> arrayList6 = delayedMessage.messageObjects;
                                    MessageObject messageObject3 = arrayList6.get(arrayList6.size() - 1);
                                    delayedMessage.finalGroupMessage = messageObject3.getId();
                                    messageObject3.messageOwner.params.put("final", "1");
                                    TLRPC.TL_messages_messages tL_messages_messages = new TLRPC.TL_messages_messages();
                                    tL_messages_messages.messages.add(messageObject3.messageOwner);
                                    getMessagesStorage().putMessages((TLRPC.messages_Messages) tL_messages_messages, delayedMessage.peer, -2, 0, false, i3, 0L);
                                }
                                if (!arrayList4.contains(delayedMessage)) {
                                    arrayList4.add(delayedMessage);
                                }
                            }
                        }
                    } else if (delayedMessage.obj.getId() == messageObject.getId()) {
                        removeFromUploadingMessages(messageObject.getId(), messageObject.scheduled);
                        value.remove(i4);
                        delayedMessage.sendDelayedRequests();
                        MediaController.getInstance().cancelVideoConvert(delayedMessage.obj);
                        if (value.size() == 0) {
                            arrayList3.add(entry.getKey());
                            if (delayedMessage.sendEncryptedRequest != null) {
                                z2 = true;
                            }
                        }
                    } else {
                        i4++;
                        z2 = z;
                    }
                }
                z2 = z;
            }
            i++;
            j = dialogId;
        }
        for (int i6 = 0; i6 < arrayList3.size(); i6++) {
            String str2 = (String) arrayList3.get(i6);
            if (str2.startsWith("http")) {
                ImageLoader.getInstance().cancelLoadHttpFile(str2);
            } else {
                getFileLoader().cancelFileUpload(str2, z2);
            }
            this.delayedMessages.remove(str2);
        }
        int size = arrayList4.size();
        for (int i7 = 0; i7 < size; i7++) {
            sendReadyToSendGroup((DelayedMessage) arrayList4.get(i7), false, true);
        }
        int i8 = 1;
        if (arrayList.size() == 1 && arrayList.get(0).isEditing() && arrayList.get(0).previousMedia != null) {
            revertEditingMessageObject(arrayList.get(0));
            return;
        }
        if (!arrayList.isEmpty() && arrayList.get(0).isQuickReply()) {
            i8 = 5;
        } else if (i3 == 0) {
            i8 = 0;
        }
        getMessagesController().deleteMessages(arrayList5, null, null, j, i2, false, i8);
    }

    public void cancelSendingMessage(MessageObject messageObject) {
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        arrayList.add(messageObject);
        if (messageObject != null && messageObject.type == 29) {
            Iterator<Map.Entry<String, ArrayList<DelayedMessage>>> it = this.delayedMessages.entrySet().iterator();
            DelayedMessage delayedMessage = null;
            while (it.hasNext()) {
                ArrayList<DelayedMessage> value = it.next().getValue();
                for (int i = 0; i < value.size(); i++) {
                    DelayedMessage delayedMessage2 = value.get(i);
                    if (delayedMessage2.type == 4) {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= delayedMessage2.messageObjects.size()) {
                                break;
                            }
                            if (delayedMessage2.messageObjects.get(i2).getId() == messageObject.getId()) {
                                delayedMessage = delayedMessage2;
                                break;
                            }
                            i2++;
                        }
                    }
                    if (delayedMessage != null) {
                        break;
                    }
                }
            }
            if (delayedMessage != null) {
                arrayList.clear();
                arrayList.addAll(delayedMessage.messageObjects);
            }
        }
        cancelSendingMessage(arrayList);
    }

    public void checkUnsentMessages() {
        getMessagesStorage().getUnsentMessages(1000);
    }

    public void cleanup() {
        this.delayedMessages.clear();
        this.unsentMessages.clear();
        this.sendingMessages.clear();
        this.editingMessages.clear();
        this.sendingMessagesIdDialogs.clear();
        this.uploadMessages.clear();
        this.uploadingMessagesIdDialogs.clear();
        this.waitingForLocation.clear();
        this.waitingForCallback.clear();
        this.waitingForVote.clear();
        this.importingHistoryFiles.clear();
        this.importingHistoryMap.clear();
        this.importingStickersFiles.clear();
        this.importingStickersMap.clear();
        this.locationProvider.stop();
    }

    public TLRPC.InputReplyTo createReplyInput(int i) {
        return createReplyInput(null, i, 0, null);
    }

    public TLRPC.InputReplyTo createReplyInput(TLRPC.InputPeer inputPeer, int i, int i2, ChatActivity.ReplyQuote replyQuote) {
        MessageObject messageObject;
        TLRPC.TL_inputReplyToMessage tL_inputReplyToMessage = new TLRPC.TL_inputReplyToMessage();
        tL_inputReplyToMessage.reply_to_msg_id = i;
        if (i2 != 0) {
            tL_inputReplyToMessage.flags |= 1;
            tL_inputReplyToMessage.top_msg_id = i2;
        }
        if (replyQuote != null) {
            String text = replyQuote.getText();
            tL_inputReplyToMessage.quote_text = text;
            if (!TextUtils.isEmpty(text)) {
                tL_inputReplyToMessage.flags |= 4;
                ArrayList<TLRPC.MessageEntity> entities = replyQuote.getEntities();
                tL_inputReplyToMessage.quote_entities = entities;
                if (entities != null && !entities.isEmpty()) {
                    tL_inputReplyToMessage.quote_entities = new ArrayList<>(tL_inputReplyToMessage.quote_entities);
                    tL_inputReplyToMessage.flags |= 8;
                }
                tL_inputReplyToMessage.flags |= 16;
                tL_inputReplyToMessage.quote_offset = replyQuote.start;
            }
        }
        if (replyQuote != null && (messageObject = replyQuote.message) != null) {
            TLRPC.InputPeer inputPeer2 = getMessagesController().getInputPeer(messageObject.getDialogId());
            if (inputPeer2 != null && !MessageObject.peersEqual(inputPeer2, inputPeer)) {
                tL_inputReplyToMessage.flags |= 2;
                tL_inputReplyToMessage.reply_to_peer_id = inputPeer2;
            }
        }
        return tL_inputReplyToMessage;
    }

    public TLRPC.InputReplyTo createReplyInput(TLRPC.TL_messageReplyHeader tL_messageReplyHeader) {
        TLRPC.TL_inputReplyToMessage tL_inputReplyToMessage = new TLRPC.TL_inputReplyToMessage();
        tL_inputReplyToMessage.reply_to_msg_id = tL_messageReplyHeader.reply_to_msg_id;
        int i = tL_messageReplyHeader.flags;
        if ((i & 2) != 0) {
            tL_inputReplyToMessage.flags |= 1;
            tL_inputReplyToMessage.top_msg_id = tL_messageReplyHeader.reply_to_top_id;
        }
        if ((i & 1) != 0) {
            tL_inputReplyToMessage.flags |= 2;
            tL_inputReplyToMessage.reply_to_peer_id = MessagesController.getInstance(this.currentAccount).getInputPeer(tL_messageReplyHeader.reply_to_peer_id);
        }
        if (tL_messageReplyHeader.quote) {
            int i2 = tL_messageReplyHeader.flags;
            if ((i2 & 64) != 0) {
                tL_inputReplyToMessage.flags |= 4;
                tL_inputReplyToMessage.quote_text = tL_messageReplyHeader.quote_text;
            }
            if ((i2 & 128) != 0) {
                tL_inputReplyToMessage.flags |= 8;
                tL_inputReplyToMessage.quote_entities = tL_messageReplyHeader.quote_entities;
            }
            if ((i2 & 1024) != 0) {
                tL_inputReplyToMessage.flags |= 16;
                tL_inputReplyToMessage.quote_offset = tL_messageReplyHeader.quote_offset;
            }
        }
        return tL_inputReplyToMessage;
    }

    public TLRPC.InputReplyTo createReplyInput(TL_stories.StoryItem storyItem) {
        TLRPC.TL_inputReplyToStory tL_inputReplyToStory = new TLRPC.TL_inputReplyToStory();
        tL_inputReplyToStory.story_id = storyItem.id;
        tL_inputReplyToStory.peer = getMessagesController().getInputPeer(storyItem.dialogId);
        return tL_inputReplyToStory;
    }

    /* JADX WARN: Code restructure failed: missing block: B:61:0x0168, code lost:
    
        if (r0.isSticker == false) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0184, code lost:
    
        if (r0.location != null) goto L77;
     */
    /* JADX WARN: Removed duplicated region for block: B:113:0x02a8  */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        final String str;
        ArrayList<DelayedMessage> arrayList;
        char c;
        final MessageObject messageObject;
        String str2;
        ArrayList<DelayedMessage> arrayList2;
        int i3;
        ArrayList<DelayedMessage> arrayList3;
        int i4;
        int i5;
        int i6;
        int i7;
        TLRPC.InputMedia inputMedia;
        Object obj;
        int i8;
        ArrayList<DelayedMessage> arrayList4;
        TLRPC.InputFile inputFile;
        String str3;
        TLRPC.InputEncryptedFile inputEncryptedFile;
        ArrayList<DelayedMessage> arrayList5;
        int i9;
        Object obj2;
        TLRPC.TL_decryptedMessage tL_decryptedMessage;
        int i10;
        int i11;
        TLRPC.InputEncryptedFile inputEncryptedFile2;
        String str4;
        DelayedMessage delayedMessage;
        TLRPC.InputEncryptedFile inputEncryptedFile3;
        int i12 = 2;
        int i13 = 0;
        if (i == NotificationCenter.fileUploadProgressChanged) {
            String str5 = (String) objArr[0];
            ImportingHistory importingHistory = this.importingHistoryFiles.get(str5);
            if (importingHistory != null) {
                importingHistory.addUploadProgress(str5, ((Long) objArr[1]).longValue(), r2.longValue() / ((Long) objArr[2]).longValue());
            }
            ImportingStickers importingStickers = this.importingStickersFiles.get(str5);
            if (importingStickers != null) {
                importingStickers.addUploadProgress(str5, ((Long) objArr[1]).longValue(), r2.longValue() / ((Long) objArr[2]).longValue());
                return;
            }
            return;
        }
        if (i == NotificationCenter.fileUploaded) {
            String str6 = (String) objArr[0];
            TLRPC.InputFile inputFile2 = (TLRPC.InputFile) objArr[1];
            TLRPC.InputEncryptedFile inputEncryptedFile4 = (TLRPC.InputEncryptedFile) objArr[2];
            ImportingHistory importingHistory2 = this.importingHistoryFiles.get(str6);
            if (importingHistory2 != null) {
                if (str6.equals(importingHistory2.historyPath)) {
                    importingHistory2.initImport(inputFile2);
                } else {
                    importingHistory2.onMediaImport(str6, ((Long) objArr[5]).longValue(), inputFile2);
                }
            }
            ImportingStickers importingStickers2 = this.importingStickersFiles.get(str6);
            if (importingStickers2 != null) {
                importingStickers2.onMediaImport(str6, ((Long) objArr[5]).longValue(), inputFile2);
            }
            ArrayList<DelayedMessage> arrayList6 = this.delayedMessages.get(str6);
            if (arrayList6 == null) {
                return;
            }
            int i14 = 0;
            while (i14 < arrayList6.size()) {
                DelayedMessage delayedMessage2 = arrayList6.get(i14);
                TLObject tLObject = delayedMessage2.sendRequest;
                if (tLObject instanceof TLRPC.TL_messages_sendMedia) {
                    inputMedia = ((TLRPC.TL_messages_sendMedia) tLObject).media;
                    if (inputMedia instanceof TLRPC.TL_inputMediaPaidMedia) {
                        HashMap<Object, Object> hashMap = delayedMessage2.extraHashMap;
                        obj = hashMap == null ? ((TLRPC.TL_inputMediaPaidMedia) inputMedia).extended_media.get(i13) : hashMap.get(str6);
                        inputMedia = (TLRPC.InputMedia) obj;
                    }
                    if (inputFile2 != null || inputMedia == null) {
                        i8 = i14;
                        arrayList4 = arrayList6;
                        inputFile = inputFile2;
                        str3 = str6;
                        inputEncryptedFile = inputEncryptedFile4;
                        if (inputEncryptedFile != null || (obj2 = delayedMessage2.sendEncryptedRequest) == null) {
                            arrayList5 = arrayList4;
                            i9 = i8;
                            i14 = i9 + 1;
                            inputEncryptedFile4 = inputEncryptedFile;
                            str6 = str3;
                            inputFile2 = inputFile;
                            i12 = 2;
                            i13 = 0;
                            arrayList6 = arrayList5;
                        } else {
                            if (delayedMessage2.type == 4) {
                                TLRPC.TL_messages_sendEncryptedMultiMedia tL_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia) obj2;
                                TLRPC.InputEncryptedFile inputEncryptedFile5 = (TLRPC.InputEncryptedFile) delayedMessage2.extraHashMap.get(str3);
                                int indexOf = tL_messages_sendEncryptedMultiMedia.files.indexOf(inputEncryptedFile5);
                                if (indexOf >= 0) {
                                    tL_messages_sendEncryptedMultiMedia.files.set(indexOf, inputEncryptedFile);
                                    if (inputEncryptedFile5.id == 1) {
                                        delayedMessage2.photoSize = (TLRPC.PhotoSize) delayedMessage2.extraHashMap.get(str3 + "_t");
                                    }
                                    obj2 = tL_messages_sendEncryptedMultiMedia.messages.get(indexOf);
                                } else {
                                    tL_decryptedMessage = null;
                                    if (tL_decryptedMessage != null) {
                                        TLRPC.DecryptedMessageMedia decryptedMessageMedia = tL_decryptedMessage.media;
                                        if ((decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaVideo) || (decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaPhoto) || (decryptedMessageMedia instanceof TLRPC.TL_decryptedMessageMediaDocument)) {
                                            tL_decryptedMessage.media.size = ((Long) objArr[5]).longValue();
                                        }
                                        TLRPC.DecryptedMessageMedia decryptedMessageMedia2 = tL_decryptedMessage.media;
                                        decryptedMessageMedia2.key = (byte[]) objArr[3];
                                        decryptedMessageMedia2.iv = (byte[]) objArr[4];
                                        if (delayedMessage2.type == 4) {
                                            uploadMultiMedia(delayedMessage2, null, inputEncryptedFile, str3);
                                        } else {
                                            SecretChatHelper secretChatHelper = getSecretChatHelper();
                                            MessageObject messageObject2 = delayedMessage2.obj;
                                            secretChatHelper.performSendEncryptedRequest(tL_decryptedMessage, messageObject2.messageOwner, delayedMessage2.encryptedChat, inputEncryptedFile, delayedMessage2.originalPath, messageObject2);
                                        }
                                    }
                                    i10 = i8;
                                }
                            }
                            tL_decryptedMessage = (TLRPC.TL_decryptedMessage) obj2;
                            if (tL_decryptedMessage != null) {
                            }
                            i10 = i8;
                        }
                    } else {
                        int i15 = delayedMessage2.type;
                        if (i15 == 0) {
                            inputMedia.file = inputFile2;
                            i11 = i14;
                            inputEncryptedFile2 = inputEncryptedFile4;
                            inputFile = inputFile2;
                            str4 = str6;
                            arrayList4 = arrayList6;
                            performSendMessageRequest(delayedMessage2.sendRequest, delayedMessage2.obj, delayedMessage2.originalPath, delayedMessage2, true, null, delayedMessage2.parentObject, null, delayedMessage2.scheduled);
                        } else {
                            i11 = i14;
                            arrayList4 = arrayList6;
                            inputEncryptedFile2 = inputEncryptedFile4;
                            inputFile = inputFile2;
                            str4 = str6;
                            if (i15 != 1) {
                                delayedMessage = delayedMessage2;
                                if (i15 == i12) {
                                    if (inputMedia.file == null) {
                                        inputMedia.file = inputFile;
                                        if (inputMedia.thumb == null) {
                                            TLRPC.PhotoSize photoSize = delayedMessage.photoSize;
                                            if (photoSize != null) {
                                            }
                                        }
                                        performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject, null, delayedMessage.scheduled);
                                        i10 = i11;
                                        inputEncryptedFile = inputEncryptedFile2;
                                        str3 = str4;
                                        arrayList5 = arrayList4;
                                        arrayList5.remove(i10);
                                        i9 = i10 - 1;
                                        i14 = i9 + 1;
                                        inputEncryptedFile4 = inputEncryptedFile;
                                        str6 = str3;
                                        inputFile2 = inputFile;
                                        i12 = 2;
                                        i13 = 0;
                                        arrayList6 = arrayList5;
                                    }
                                    inputMedia.thumb = inputFile;
                                    inputMedia.flags |= 4;
                                    performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject, null, delayedMessage.scheduled);
                                    i10 = i11;
                                    inputEncryptedFile = inputEncryptedFile2;
                                    str3 = str4;
                                    arrayList5 = arrayList4;
                                    arrayList5.remove(i10);
                                    i9 = i10 - 1;
                                    i14 = i9 + 1;
                                    inputEncryptedFile4 = inputEncryptedFile;
                                    str6 = str3;
                                    inputFile2 = inputFile;
                                    i12 = 2;
                                    i13 = 0;
                                    arrayList6 = arrayList5;
                                } else if (i15 == 3) {
                                    inputMedia.file = inputFile;
                                    performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject, null, delayedMessage.scheduled);
                                    i10 = i11;
                                    inputEncryptedFile = inputEncryptedFile2;
                                    str3 = str4;
                                    arrayList5 = arrayList4;
                                    arrayList5.remove(i10);
                                    i9 = i10 - 1;
                                    i14 = i9 + 1;
                                    inputEncryptedFile4 = inputEncryptedFile;
                                    str6 = str3;
                                    inputFile2 = inputFile;
                                    i12 = 2;
                                    i13 = 0;
                                    arrayList6 = arrayList5;
                                } else {
                                    if (i15 == 4) {
                                        if (!(inputMedia instanceof TLRPC.TL_inputMediaUploadedDocument)) {
                                            str3 = str4;
                                            inputEncryptedFile3 = null;
                                            inputMedia.file = inputFile;
                                        } else if (inputMedia.file == null) {
                                            inputMedia.file = inputFile;
                                            HashMap<Object, Object> hashMap2 = delayedMessage.extraHashMap;
                                            StringBuilder sb = new StringBuilder();
                                            str3 = str4;
                                            sb.append(str3);
                                            sb.append("_i");
                                            int indexOf2 = delayedMessage.messageObjects.indexOf((MessageObject) hashMap2.get(sb.toString()));
                                            TLRPC.PhotoSize photoSize2 = (TLRPC.PhotoSize) delayedMessage.extraHashMap.get(str3 + "_t");
                                            delayedMessage.photoSize = photoSize2;
                                            if (inputMedia.thumb != null || photoSize2 == null || photoSize2.location == null) {
                                                inputEncryptedFile3 = null;
                                            } else {
                                                delayedMessage.performMediaUpload = true;
                                                performSendDelayedMessage(delayedMessage, indexOf2);
                                                i10 = i11;
                                                inputEncryptedFile = inputEncryptedFile2;
                                                arrayList5 = arrayList4;
                                                arrayList5.remove(i10);
                                                i9 = i10 - 1;
                                                i14 = i9 + 1;
                                                inputEncryptedFile4 = inputEncryptedFile;
                                                str6 = str3;
                                                inputFile2 = inputFile;
                                                i12 = 2;
                                                i13 = 0;
                                                arrayList6 = arrayList5;
                                            }
                                        } else {
                                            str3 = str4;
                                            inputMedia.thumb = inputFile;
                                            inputMedia.flags |= 4;
                                            uploadMultiMedia(delayedMessage, inputMedia, null, (String) delayedMessage.extraHashMap.get(str3 + "_o"));
                                        }
                                        uploadMultiMedia(delayedMessage, inputMedia, inputEncryptedFile3, str3);
                                    } else {
                                        str3 = str4;
                                    }
                                    i10 = i11;
                                    inputEncryptedFile = inputEncryptedFile2;
                                }
                            } else if (inputMedia.file == null) {
                                inputMedia.file = inputFile;
                                delayedMessage = delayedMessage2;
                                if (inputMedia.thumb == null) {
                                    TLRPC.PhotoSize photoSize3 = delayedMessage.photoSize;
                                    if (photoSize3 != null) {
                                        if (photoSize3.location != null) {
                                            MessageObject messageObject3 = delayedMessage.obj;
                                            if (messageObject3 != null) {
                                                VideoEditedInfo videoEditedInfo = messageObject3.videoEditedInfo;
                                                if (videoEditedInfo != null) {
                                                }
                                            }
                                            performSendDelayedMessage(delayedMessage);
                                        }
                                    }
                                }
                                performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject, null, delayedMessage.scheduled);
                                i10 = i11;
                                inputEncryptedFile = inputEncryptedFile2;
                                str3 = str4;
                                arrayList5 = arrayList4;
                                arrayList5.remove(i10);
                                i9 = i10 - 1;
                                i14 = i9 + 1;
                                inputEncryptedFile4 = inputEncryptedFile;
                                str6 = str3;
                                inputFile2 = inputFile;
                                i12 = 2;
                                i13 = 0;
                                arrayList6 = arrayList5;
                            } else {
                                delayedMessage = delayedMessage2;
                                inputMedia.thumb = inputFile;
                                inputMedia.flags |= 4;
                                performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject, null, delayedMessage.scheduled);
                                i10 = i11;
                                inputEncryptedFile = inputEncryptedFile2;
                                str3 = str4;
                                arrayList5 = arrayList4;
                                arrayList5.remove(i10);
                                i9 = i10 - 1;
                                i14 = i9 + 1;
                                inputEncryptedFile4 = inputEncryptedFile;
                                str6 = str3;
                                inputFile2 = inputFile;
                                i12 = 2;
                                i13 = 0;
                                arrayList6 = arrayList5;
                            }
                        }
                        i10 = i11;
                        inputEncryptedFile = inputEncryptedFile2;
                        str3 = str4;
                        arrayList5 = arrayList4;
                        arrayList5.remove(i10);
                        i9 = i10 - 1;
                        i14 = i9 + 1;
                        inputEncryptedFile4 = inputEncryptedFile;
                        str6 = str3;
                        inputFile2 = inputFile;
                        i12 = 2;
                        i13 = 0;
                        arrayList6 = arrayList5;
                    }
                    arrayList5 = arrayList4;
                    arrayList5.remove(i10);
                    i9 = i10 - 1;
                    i14 = i9 + 1;
                    inputEncryptedFile4 = inputEncryptedFile;
                    str6 = str3;
                    inputFile2 = inputFile;
                    i12 = 2;
                    i13 = 0;
                    arrayList6 = arrayList5;
                } else {
                    if (tLObject instanceof TLRPC.TL_messages_editMessage) {
                        inputMedia = ((TLRPC.TL_messages_editMessage) tLObject).media;
                    } else if (tLObject instanceof TLRPC.TL_messages_sendMultiMedia) {
                        obj = delayedMessage2.extraHashMap.get(str6);
                        inputMedia = (TLRPC.InputMedia) obj;
                    } else {
                        inputMedia = null;
                    }
                    if (inputFile2 != null) {
                    }
                    i8 = i14;
                    arrayList4 = arrayList6;
                    inputFile = inputFile2;
                    str3 = str6;
                    inputEncryptedFile = inputEncryptedFile4;
                    if (inputEncryptedFile != null) {
                    }
                    arrayList5 = arrayList4;
                    i9 = i8;
                    i14 = i9 + 1;
                    inputEncryptedFile4 = inputEncryptedFile;
                    str6 = str3;
                    inputFile2 = inputFile;
                    i12 = 2;
                    i13 = 0;
                    arrayList6 = arrayList5;
                }
            }
            ArrayList<DelayedMessage> arrayList7 = arrayList6;
            String str7 = str6;
            if (!arrayList7.isEmpty()) {
                return;
            } else {
                str = str7;
            }
        } else if (i == NotificationCenter.fileUploadFailed) {
            str = (String) objArr[0];
            boolean booleanValue = ((Boolean) objArr[1]).booleanValue();
            ImportingHistory importingHistory3 = this.importingHistoryFiles.get(str);
            if (importingHistory3 != null) {
                importingHistory3.onFileFailedToUpload(str);
            }
            ImportingStickers importingStickers3 = this.importingStickersFiles.get(str);
            if (importingStickers3 != null) {
                importingStickers3.onFileFailedToUpload(str);
            }
            ArrayList<DelayedMessage> arrayList8 = this.delayedMessages.get(str);
            if (arrayList8 == null) {
                return;
            }
            int i16 = 0;
            while (i16 < arrayList8.size()) {
                DelayedMessage delayedMessage3 = arrayList8.get(i16);
                if ((booleanValue && delayedMessage3.sendEncryptedRequest != null) || (!booleanValue && delayedMessage3.sendRequest != null)) {
                    delayedMessage3.markAsError();
                    arrayList8.remove(i16);
                    i16--;
                }
                i16++;
            }
            if (!arrayList8.isEmpty()) {
                return;
            }
        } else {
            if (i == NotificationCenter.filePreparingStarted) {
                MessageObject messageObject4 = (MessageObject) objArr[0];
                if (messageObject4.getId() == 0) {
                    return;
                }
                ArrayList<DelayedMessage> arrayList9 = this.delayedMessages.get(messageObject4.messageOwner.attachPath);
                if (arrayList9 != null) {
                    for (int i17 = 0; i17 < arrayList9.size(); i17++) {
                        DelayedMessage delayedMessage4 = arrayList9.get(i17);
                        if (delayedMessage4.type == 4) {
                            int indexOf3 = delayedMessage4.messageObjects.indexOf(messageObject4);
                            delayedMessage4.photoSize = (TLRPC.PhotoSize) delayedMessage4.extraHashMap.get(messageObject4.messageOwner.attachPath + "_t");
                            delayedMessage4.performMediaUpload = true;
                            performSendDelayedMessage(delayedMessage4, indexOf3);
                        } else if (delayedMessage4.obj == messageObject4) {
                            delayedMessage4.videoEditedInfo = null;
                            performSendDelayedMessage(delayedMessage4);
                        }
                        arrayList9.remove(i17);
                    }
                    if (arrayList9.isEmpty()) {
                        this.delayedMessages.remove(messageObject4.messageOwner.attachPath);
                        return;
                    }
                    return;
                }
                return;
            }
            if (i == NotificationCenter.fileNewChunkAvailable) {
                MessageObject messageObject5 = (MessageObject) objArr[0];
                if (messageObject5.getId() == 0) {
                    return;
                }
                String str8 = (String) objArr[1];
                long longValue = ((Long) objArr[2]).longValue();
                long longValue2 = ((Long) objArr[3]).longValue();
                getFileLoader().checkUploadNewDataAvailable(str8, DialogObject.isEncryptedDialog(messageObject5.getDialogId()), longValue, longValue2, (Float) objArr[4]);
                if (longValue2 == 0 || (arrayList3 = this.delayedMessages.get(messageObject5.messageOwner.attachPath)) == null) {
                    return;
                }
                for (int i18 = 0; i18 < arrayList3.size(); i18++) {
                    DelayedMessage delayedMessage5 = arrayList3.get(i18);
                    if (delayedMessage5.type == 4) {
                        int i19 = 0;
                        while (true) {
                            if (i19 >= delayedMessage5.messageObjects.size()) {
                                break;
                            }
                            MessageObject messageObject6 = delayedMessage5.messageObjects.get(i19);
                            if (messageObject6 == messageObject5) {
                                delayedMessage5.obj.shouldRemoveVideoEditedInfo = true;
                                messageObject6.messageOwner.params.remove("ve");
                                TLRPC.Document document = delayedMessage5.obj.getDocument();
                                if (document != null) {
                                    document.size = longValue2;
                                }
                                ArrayList<TLRPC.Message> arrayList10 = new ArrayList<>();
                                arrayList10.add(messageObject6.messageOwner);
                                if (messageObject6.isQuickReply()) {
                                    i6 = messageObject6.getQuickReplyId();
                                    i7 = 5;
                                } else if (messageObject6.scheduled) {
                                    i6 = 0;
                                    i7 = 1;
                                } else {
                                    i6 = 0;
                                    i7 = 0;
                                }
                                if (!delayedMessage5.paidMedia || i19 == 0) {
                                    getMessagesStorage().putMessages(arrayList10, false, true, false, 0, i7, i6);
                                }
                            } else {
                                i19++;
                            }
                        }
                    } else {
                        MessageObject messageObject7 = delayedMessage5.obj;
                        if (messageObject7 == messageObject5) {
                            messageObject7.shouldRemoveVideoEditedInfo = true;
                            messageObject7.messageOwner.params.remove("ve");
                            TLRPC.Document document2 = delayedMessage5.obj.getDocument();
                            if (document2 != null) {
                                document2.size = longValue2;
                            }
                            ArrayList<TLRPC.Message> arrayList11 = new ArrayList<>();
                            arrayList11.add(delayedMessage5.obj.messageOwner);
                            if (delayedMessage5.obj.isQuickReply()) {
                                i4 = delayedMessage5.obj.getQuickReplyId();
                                i5 = 5;
                            } else {
                                i4 = 0;
                                i5 = delayedMessage5.obj.scheduled ? 1 : 0;
                            }
                            getMessagesStorage().putMessages(arrayList11, false, true, false, 0, i5, i4);
                            return;
                        }
                    }
                }
                return;
            }
            if (i == NotificationCenter.filePreparingFailed) {
                MessageObject messageObject8 = (MessageObject) objArr[0];
                if (messageObject8.getId() == 0 || (arrayList2 = this.delayedMessages.get((str2 = (String) objArr[1]))) == null) {
                    return;
                }
                while (i3 < arrayList2.size()) {
                    DelayedMessage delayedMessage6 = arrayList2.get(i3);
                    if (delayedMessage6.type != 4) {
                        i3 = delayedMessage6.obj != messageObject8 ? i3 + 1 : 0;
                        delayedMessage6.markAsError();
                        arrayList2.remove(i3);
                        i3--;
                        break;
                    }
                    for (int i20 = 0; i20 < delayedMessage6.messages.size(); i20++) {
                        if (delayedMessage6.messageObjects.get(i20) == messageObject8) {
                            delayedMessage6.markAsError();
                            arrayList2.remove(i3);
                            i3--;
                            break;
                        }
                    }
                }
                if (arrayList2.isEmpty()) {
                    this.delayedMessages.remove(str2);
                    return;
                }
                return;
            }
            if (i == NotificationCenter.httpFileDidLoad) {
                str = (String) objArr[0];
                ArrayList<DelayedMessage> arrayList12 = this.delayedMessages.get(str);
                if (arrayList12 == null) {
                    return;
                }
                for (int i21 = 0; i21 < arrayList12.size(); i21++) {
                    final DelayedMessage delayedMessage7 = arrayList12.get(i21);
                    int i22 = delayedMessage7.type;
                    if (i22 == 0) {
                        messageObject = delayedMessage7.obj;
                        c = 0;
                    } else {
                        if (i22 == 2) {
                            messageObject = delayedMessage7.obj;
                        } else if (i22 == 4) {
                            MessageObject messageObject9 = (MessageObject) delayedMessage7.extraHashMap.get(str);
                            messageObject = messageObject9;
                            if (messageObject9.getDocument() == null) {
                                c = 0;
                            }
                        } else {
                            c = 65535;
                            messageObject = null;
                        }
                        c = 1;
                    }
                    if (c == 0) {
                        final File file = new File(FileLoader.getDirectory(4), Utilities.MD5(str) + "." + ImageLoader.getHttpUrlExtension(str, "file"));
                        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda56
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.this.lambda$didReceivedNotification$2(file, messageObject, delayedMessage7, str);
                            }
                        });
                    } else if (c == 1) {
                        final File file2 = new File(FileLoader.getDirectory(4), Utilities.MD5(str) + ".gif");
                        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda57
                            @Override // java.lang.Runnable
                            public final void run() {
                                SendMessagesHelper.this.lambda$didReceivedNotification$4(delayedMessage7, file2, messageObject);
                            }
                        });
                    }
                }
            } else if (i == NotificationCenter.fileLoaded) {
                str = (String) objArr[0];
                ArrayList<DelayedMessage> arrayList13 = this.delayedMessages.get(str);
                if (arrayList13 == null) {
                    return;
                }
                for (int i23 = 0; i23 < arrayList13.size(); i23++) {
                    performSendDelayedMessage(arrayList13.get(i23));
                }
            } else {
                if ((i != NotificationCenter.httpFileDidFailedLoad && i != NotificationCenter.fileLoadFailed) || (arrayList = this.delayedMessages.get((str = (String) objArr[0]))) == null) {
                    return;
                }
                for (int i24 = 0; i24 < arrayList.size(); i24++) {
                    arrayList.get(i24).markAsError();
                }
            }
        }
        this.delayedMessages.remove(str);
    }

    public int editMessage(MessageObject messageObject, String str, boolean z, final BaseFragment baseFragment, ArrayList<TLRPC.MessageEntity> arrayList, int i) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return 0;
        }
        final TLRPC.TL_messages_editMessage tL_messages_editMessage = new TLRPC.TL_messages_editMessage();
        tL_messages_editMessage.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
        if (str != null) {
            tL_messages_editMessage.message = str;
            tL_messages_editMessage.flags |= 2048;
            tL_messages_editMessage.no_webpage = !z;
        }
        tL_messages_editMessage.id = messageObject.getId();
        TLRPC.Message message = messageObject.messageOwner;
        if (message != null && (message.flags & 1073741824) != 0) {
            tL_messages_editMessage.quick_reply_shortcut_id = message.quick_reply_shortcut_id;
            tL_messages_editMessage.flags |= 131072;
        }
        if (arrayList != null) {
            tL_messages_editMessage.entities = arrayList;
            tL_messages_editMessage.flags |= 8;
        }
        if (i != 0) {
            tL_messages_editMessage.schedule_date = i;
            tL_messages_editMessage.flags |= 32768;
        }
        return getConnectionsManager().sendRequest(tL_messages_editMessage, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda84
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.lambda$editMessage$16(baseFragment, tL_messages_editMessage, tLObject, tL_error);
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:150:0x05e5, code lost:
    
        if (r7 != false) goto L300;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x03f4, code lost:
    
        if (r9.muted == false) goto L204;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x04ff, code lost:
    
        if (r1 == 19) goto L250;
     */
    /* JADX WARN: Removed duplicated region for block: B:303:0x0154 A[Catch: Exception -> 0x0049, TryCatch #0 {Exception -> 0x0049, blocks: (B:9:0x0022, B:11:0x002c, B:13:0x003e, B:19:0x0053, B:22:0x005d, B:25:0x0062, B:27:0x0066, B:28:0x008c, B:31:0x0092, B:33:0x0098, B:34:0x009f, B:35:0x016d, B:37:0x0171, B:38:0x0175, B:43:0x018f, B:48:0x0198, B:50:0x019c, B:52:0x01ac, B:54:0x01b0, B:55:0x01c1, B:56:0x01c3, B:58:0x01c7, B:61:0x020f, B:62:0x0216, B:64:0x0249, B:66:0x0251, B:69:0x0256, B:70:0x025d, B:71:0x0260, B:73:0x0290, B:75:0x0298, B:81:0x02b0, B:83:0x02b4, B:85:0x02b8, B:88:0x02cf, B:90:0x02d3, B:92:0x04f5, B:94:0x04f9, B:99:0x0502, B:101:0x051f, B:102:0x052f, B:104:0x0533, B:105:0x053f, B:107:0x0546, B:108:0x0551, B:110:0x0555, B:112:0x056b, B:113:0x0591, B:114:0x0593, B:115:0x0570, B:117:0x0584, B:119:0x058a, B:121:0x059a, B:124:0x059f, B:130:0x05e7, B:132:0x05bc, B:138:0x05d4, B:139:0x05eb, B:152:0x0528, B:154:0x052c, B:157:0x02be, B:159:0x02c2, B:162:0x02f2, B:164:0x02fb, B:166:0x0303, B:168:0x0314, B:170:0x0329, B:171:0x0332, B:174:0x0360, B:176:0x0374, B:178:0x037a, B:180:0x0384, B:182:0x0387, B:183:0x033d, B:185:0x0357, B:186:0x035c, B:189:0x03a2, B:191:0x03ab, B:193:0x03b3, B:195:0x03c4, B:197:0x03d5, B:198:0x03de, B:201:0x03f0, B:204:0x03fb, B:206:0x0401, B:207:0x0409, B:210:0x043b, B:212:0x0455, B:214:0x0462, B:215:0x0466, B:216:0x0416, B:218:0x0432, B:219:0x0437, B:224:0x0477, B:227:0x04ba, B:230:0x04d0, B:233:0x04d7, B:235:0x04e4, B:237:0x04e9, B:238:0x0494, B:240:0x04b1, B:241:0x04b6, B:249:0x01b7, B:251:0x01bd, B:252:0x01d3, B:254:0x01d7, B:255:0x020d, B:256:0x01e0, B:258:0x01f4, B:260:0x01fa, B:261:0x0201, B:263:0x0209, B:266:0x0184, B:268:0x018c, B:270:0x006e, B:272:0x0072, B:277:0x0082, B:281:0x00ae, B:283:0x00c0, B:284:0x00c5, B:286:0x00ef, B:288:0x0102, B:290:0x0108, B:293:0x015d, B:294:0x0161, B:295:0x0110, B:297:0x0135, B:303:0x0154), top: B:8:0x0022 }] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0249 A[Catch: Exception -> 0x0049, TryCatch #0 {Exception -> 0x0049, blocks: (B:9:0x0022, B:11:0x002c, B:13:0x003e, B:19:0x0053, B:22:0x005d, B:25:0x0062, B:27:0x0066, B:28:0x008c, B:31:0x0092, B:33:0x0098, B:34:0x009f, B:35:0x016d, B:37:0x0171, B:38:0x0175, B:43:0x018f, B:48:0x0198, B:50:0x019c, B:52:0x01ac, B:54:0x01b0, B:55:0x01c1, B:56:0x01c3, B:58:0x01c7, B:61:0x020f, B:62:0x0216, B:64:0x0249, B:66:0x0251, B:69:0x0256, B:70:0x025d, B:71:0x0260, B:73:0x0290, B:75:0x0298, B:81:0x02b0, B:83:0x02b4, B:85:0x02b8, B:88:0x02cf, B:90:0x02d3, B:92:0x04f5, B:94:0x04f9, B:99:0x0502, B:101:0x051f, B:102:0x052f, B:104:0x0533, B:105:0x053f, B:107:0x0546, B:108:0x0551, B:110:0x0555, B:112:0x056b, B:113:0x0591, B:114:0x0593, B:115:0x0570, B:117:0x0584, B:119:0x058a, B:121:0x059a, B:124:0x059f, B:130:0x05e7, B:132:0x05bc, B:138:0x05d4, B:139:0x05eb, B:152:0x0528, B:154:0x052c, B:157:0x02be, B:159:0x02c2, B:162:0x02f2, B:164:0x02fb, B:166:0x0303, B:168:0x0314, B:170:0x0329, B:171:0x0332, B:174:0x0360, B:176:0x0374, B:178:0x037a, B:180:0x0384, B:182:0x0387, B:183:0x033d, B:185:0x0357, B:186:0x035c, B:189:0x03a2, B:191:0x03ab, B:193:0x03b3, B:195:0x03c4, B:197:0x03d5, B:198:0x03de, B:201:0x03f0, B:204:0x03fb, B:206:0x0401, B:207:0x0409, B:210:0x043b, B:212:0x0455, B:214:0x0462, B:215:0x0466, B:216:0x0416, B:218:0x0432, B:219:0x0437, B:224:0x0477, B:227:0x04ba, B:230:0x04d0, B:233:0x04d7, B:235:0x04e4, B:237:0x04e9, B:238:0x0494, B:240:0x04b1, B:241:0x04b6, B:249:0x01b7, B:251:0x01bd, B:252:0x01d3, B:254:0x01d7, B:255:0x020d, B:256:0x01e0, B:258:0x01f4, B:260:0x01fa, B:261:0x0201, B:263:0x0209, B:266:0x0184, B:268:0x018c, B:270:0x006e, B:272:0x0072, B:277:0x0082, B:281:0x00ae, B:283:0x00c0, B:284:0x00c5, B:286:0x00ef, B:288:0x0102, B:290:0x0108, B:293:0x015d, B:294:0x0161, B:295:0x0110, B:297:0x0135, B:303:0x0154), top: B:8:0x0022 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void editMessage(MessageObject messageObject, TLRPC.TL_photo tL_photo, VideoEditedInfo videoEditedInfo, TLRPC.TL_document tL_document, String str, HashMap<String, String> hashMap, boolean z, boolean z2, Object obj) {
        Object obj2;
        char c;
        Object obj3;
        String str2;
        char c2;
        HashMap<String, String> hashMap2;
        VideoEditedInfo videoEditedInfo2;
        boolean z3;
        String str3;
        VideoEditedInfo videoEditedInfo3;
        String str4;
        String str5;
        HashMap<String, String> hashMap3;
        Object obj4;
        TLRPC.InputMedia inputMedia;
        boolean z4;
        boolean z5;
        DelayedMessage delayedMessage;
        VideoEditedInfo videoEditedInfo4;
        VideoEditedInfo videoEditedInfo5;
        TLRPC.InputMedia inputMedia2;
        boolean z6;
        String str6;
        TLRPC.InputMedia inputMedia3;
        boolean z7;
        String str7;
        TLRPC.InputMedia inputMedia4;
        int i;
        TLRPC.InputMedia inputMedia5;
        int i2;
        int i3;
        TLRPC.Message message;
        TLRPC.EncryptedChat encryptedChat;
        TLRPC.TL_photo tL_photo2 = tL_photo;
        TLRPC.TL_document tL_document2 = tL_document;
        if (messageObject == null) {
            return;
        }
        HashMap<String, String> hashMap4 = hashMap == null ? new HashMap<>() : hashMap;
        TLRPC.Message message2 = messageObject.messageOwner;
        messageObject.cancelEditing = false;
        try {
            long dialogId = messageObject.getDialogId();
            boolean z8 = !DialogObject.isEncryptedDialog(dialogId) || ((encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(dialogId)))) != null && AndroidUtilities.getPeerLayerVersion(encryptedChat.layer) >= 101);
            if (z) {
                TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
                if (!(messageMedia instanceof TLRPC.TL_messageMediaWebPage) && messageMedia != null && !(messageMedia instanceof TLRPC.TL_messageMediaEmpty)) {
                    if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
                        tL_photo2 = (TLRPC.TL_photo) messageMedia.photo;
                        videoEditedInfo2 = videoEditedInfo;
                        c2 = 2;
                    } else if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                        tL_document2 = (TLRPC.TL_document) messageMedia.document;
                        if (!MessageObject.isVideoDocument(tL_document2) && videoEditedInfo == null) {
                            c2 = 7;
                            videoEditedInfo2 = messageObject.videoEditedInfo;
                        }
                        c2 = 3;
                        videoEditedInfo2 = messageObject.videoEditedInfo;
                    } else {
                        videoEditedInfo2 = videoEditedInfo;
                        c2 = 65535;
                    }
                    HashMap<String, String> hashMap5 = message2.params;
                    obj3 = (obj == null || hashMap5 == null || !hashMap5.containsKey("parentObject")) ? obj : hashMap5.get("parentObject");
                    messageObject.editingMessage = message2.message;
                    messageObject.editingMessageEntities = message2.entities;
                    str2 = message2.attachPath;
                    obj2 = "originalPath";
                    hashMap2 = hashMap5;
                }
                videoEditedInfo2 = videoEditedInfo;
                c2 = 1;
                HashMap<String, String> hashMap52 = message2.params;
                if (obj == null) {
                }
                messageObject.editingMessage = message2.message;
                messageObject.editingMessageEntities = message2.entities;
                str2 = message2.attachPath;
                obj2 = "originalPath";
                hashMap2 = hashMap52;
            } else {
                TLRPC.MessageMedia messageMedia2 = message2.media;
                messageObject.previousMedia = messageMedia2;
                messageObject.previousMessage = message2.message;
                messageObject.previousMessageEntities = message2.entities;
                messageObject.previousAttachPath = message2.attachPath;
                if (messageMedia2 == null) {
                    new TLRPC.TL_messageMediaEmpty();
                }
                SerializedData serializedData = new SerializedData(true);
                writePreviousMessageData(message2, serializedData);
                SerializedData serializedData2 = new SerializedData(serializedData.length());
                writePreviousMessageData(message2, serializedData2);
                obj2 = "originalPath";
                hashMap4.put("prevMedia", Base64.encodeToString(serializedData2.toByteArray(), 0));
                serializedData2.cleanup();
                if (tL_photo2 != null) {
                    TLRPC.TL_messageMediaPhoto tL_messageMediaPhoto = new TLRPC.TL_messageMediaPhoto();
                    message2.media = tL_messageMediaPhoto;
                    tL_messageMediaPhoto.flags |= 3;
                    tL_messageMediaPhoto.photo = tL_photo2;
                    tL_messageMediaPhoto.spoiler = z2;
                    if (str == null || str.length() <= 0 || !str.startsWith("http")) {
                        ArrayList<TLRPC.PhotoSize> arrayList = tL_photo2.sizes;
                        message2.attachPath = FileLoader.getInstance(this.currentAccount).getPathToAttach(arrayList.get(arrayList.size() - 1).location, true).toString();
                        c = 2;
                        message2.params = hashMap4;
                        message2.send_state = 3;
                        obj3 = obj;
                        str2 = str;
                        c2 = c;
                        hashMap2 = hashMap4;
                        videoEditedInfo2 = videoEditedInfo;
                    } else {
                        c = 2;
                        message2.attachPath = str;
                        message2.params = hashMap4;
                        message2.send_state = 3;
                        obj3 = obj;
                        str2 = str;
                        c2 = c;
                        hashMap2 = hashMap4;
                        videoEditedInfo2 = videoEditedInfo;
                    }
                } else if (tL_document2 != null) {
                    TLRPC.TL_messageMediaDocument tL_messageMediaDocument = new TLRPC.TL_messageMediaDocument();
                    message2.media = tL_messageMediaDocument;
                    tL_messageMediaDocument.flags |= 3;
                    tL_messageMediaDocument.document = tL_document2;
                    tL_messageMediaDocument.spoiler = z2;
                    if (!MessageObject.isVideoDocument(tL_document) && videoEditedInfo == null) {
                        c = 7;
                        if (videoEditedInfo != null) {
                            hashMap4.put("ve", videoEditedInfo.getString());
                        }
                        message2.attachPath = str;
                        message2.params = hashMap4;
                        message2.send_state = 3;
                        obj3 = obj;
                        str2 = str;
                        c2 = c;
                        hashMap2 = hashMap4;
                        videoEditedInfo2 = videoEditedInfo;
                    }
                    c = 3;
                    if (videoEditedInfo != null) {
                    }
                    message2.attachPath = str;
                    message2.params = hashMap4;
                    message2.send_state = 3;
                    obj3 = obj;
                    str2 = str;
                    c2 = c;
                    hashMap2 = hashMap4;
                    videoEditedInfo2 = videoEditedInfo;
                } else {
                    c = 1;
                    message2.params = hashMap4;
                    message2.send_state = 3;
                    obj3 = obj;
                    str2 = str;
                    c2 = c;
                    hashMap2 = hashMap4;
                    videoEditedInfo2 = videoEditedInfo;
                }
            }
            if (message2.attachPath == null) {
                message2.attachPath = "";
            }
            message2.local_id = 0;
            int i4 = messageObject.type;
            TLRPC.TL_document tL_document3 = tL_document2;
            if ((i4 == 3 || videoEditedInfo2 != null || i4 == 2) && !TextUtils.isEmpty(message2.attachPath)) {
                messageObject.attachPathExists = true;
            }
            VideoEditedInfo videoEditedInfo6 = messageObject.videoEditedInfo;
            if (videoEditedInfo6 != null && videoEditedInfo2 == null) {
                videoEditedInfo2 = videoEditedInfo6;
            }
            if (z) {
                z3 = z8;
                str3 = str2;
                videoEditedInfo3 = videoEditedInfo2;
                str4 = "http";
            } else {
                CharSequence charSequence = messageObject.editingMessage;
                if (charSequence != null) {
                    String str8 = message2.message;
                    String charSequence2 = charSequence.toString();
                    message2.message = charSequence2;
                    videoEditedInfo3 = videoEditedInfo2;
                    messageObject.caption = null;
                    if (c2 == 1) {
                        ArrayList<TLRPC.MessageEntity> arrayList2 = messageObject.editingMessageEntities;
                        if (arrayList2 != null) {
                            message2.entities = arrayList2;
                            i3 = message2.flags | 128;
                        } else {
                            if (!TextUtils.equals(str8, charSequence2)) {
                                i3 = message2.flags & (-129);
                            }
                            message = messageObject.messageOwner;
                            if (message != null && (message.media instanceof TLRPC.TL_messageMediaPaidMedia)) {
                                str4 = "http";
                            }
                        }
                        message2.flags = i3;
                        message = messageObject.messageOwner;
                        if (message != null) {
                            str4 = "http";
                        }
                    } else {
                        ArrayList<TLRPC.MessageEntity> arrayList3 = messageObject.editingMessageEntities;
                        if (arrayList3 != null) {
                            message2.entities = arrayList3;
                            i2 = message2.flags | 128;
                            str4 = "http";
                        } else {
                            str4 = "http";
                            ArrayList<TLRPC.MessageEntity> entities = getMediaDataController().getEntities(new CharSequence[]{messageObject.editingMessage}, z8);
                            if (entities != null && !entities.isEmpty()) {
                                message2.entities = entities;
                                i2 = message2.flags | 128;
                            } else if (!TextUtils.equals(str8, message2.message)) {
                                i2 = message2.flags & (-129);
                            }
                        }
                        message2.flags = i2;
                    }
                    messageObject.generateCaption();
                    ArrayList<TLRPC.Message> arrayList4 = new ArrayList<>();
                    arrayList4.add(message2);
                    getMessagesStorage().putMessages(arrayList4, false, true, false, 0, messageObject.scheduled ? 1 : 0, 0L);
                    getMessagesController().getTopicsController().processEditedMessage(message2);
                    messageObject.type = -1;
                    messageObject.setType();
                    if (c2 == 1) {
                        TLRPC.MessageMedia messageMedia3 = messageObject.messageOwner.media;
                        if (!(messageMedia3 instanceof TLRPC.TL_messageMediaPhoto) && !(messageMedia3 instanceof TLRPC.TL_messageMediaDocument)) {
                            messageObject.resetLayout();
                            messageObject.checkLayout();
                        }
                        messageObject.generateCaption();
                    }
                    messageObject.createMessageSendInfo();
                    ArrayList arrayList5 = new ArrayList();
                    arrayList5.add(messageObject);
                    z3 = z8;
                    str3 = str2;
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.replaceMessagesObjects, Long.valueOf(dialogId), arrayList5);
                } else {
                    videoEditedInfo3 = videoEditedInfo2;
                }
                str4 = "http";
                ArrayList<TLRPC.Message> arrayList42 = new ArrayList<>();
                arrayList42.add(message2);
                getMessagesStorage().putMessages(arrayList42, false, true, false, 0, messageObject.scheduled ? 1 : 0, 0L);
                getMessagesController().getTopicsController().processEditedMessage(message2);
                messageObject.type = -1;
                messageObject.setType();
                if (c2 == 1) {
                }
                messageObject.createMessageSendInfo();
                ArrayList arrayList52 = new ArrayList();
                arrayList52.add(messageObject);
                z3 = z8;
                str3 = str2;
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.replaceMessagesObjects, Long.valueOf(dialogId), arrayList52);
            }
            if (hashMap2 != null) {
                Object obj5 = obj2;
                if (hashMap2.containsKey(obj5)) {
                    str5 = hashMap2.get(obj5);
                    if ((c2 >= 1 || c2 > 3) && (c2 < 5 || c2 > '\b')) {
                        return;
                    }
                    if (c2 == 1) {
                        TLRPC.MessageMedia messageMedia4 = message2.media;
                        if ((messageMedia4 == null || (messageMedia4 instanceof TLRPC.TL_messageMediaEmpty) || (messageMedia4.webpage instanceof TLRPC.TL_webPageEmpty)) && !messageObject.editingMessageSearchWebPage) {
                            inputMedia5 = new TLRPC.TL_inputMediaEmpty();
                        } else {
                            if (messageMedia4 != null && messageMedia4.webpage != null) {
                                TLRPC.TL_inputMediaWebPage tL_inputMediaWebPage = new TLRPC.TL_inputMediaWebPage();
                                TLRPC.MessageMedia messageMedia5 = message2.media;
                                tL_inputMediaWebPage.url = messageMedia5.webpage.url;
                                tL_inputMediaWebPage.force_small_media = messageMedia5.force_small_media;
                                tL_inputMediaWebPage.force_large_media = messageMedia5.force_large_media;
                                inputMedia5 = tL_inputMediaWebPage;
                            }
                            hashMap3 = hashMap2;
                            delayedMessage = null;
                            z5 = false;
                            inputMedia4 = null;
                        }
                        hashMap3 = hashMap2;
                        delayedMessage = null;
                        z5 = false;
                        inputMedia4 = inputMedia5;
                    } else if (c2 == 2) {
                        TLRPC.InputMedia tL_inputMediaUploadedPhoto = new TLRPC.TL_inputMediaUploadedPhoto();
                        tL_inputMediaUploadedPhoto.spoiler = z2;
                        if (hashMap2 != null && (str7 = hashMap2.get("masks")) != null) {
                            SerializedData serializedData3 = new SerializedData(Utilities.hexToBytes(str7));
                            boolean z9 = false;
                            int readInt32 = serializedData3.readInt32(false);
                            int i5 = 0;
                            while (i5 < readInt32) {
                                tL_inputMediaUploadedPhoto.stickers.add(TLRPC.InputDocument.TLdeserialize(serializedData3, serializedData3.readInt32(z9), z9));
                                i5++;
                                readInt32 = readInt32;
                                z9 = false;
                            }
                            tL_inputMediaUploadedPhoto.flags |= 1;
                            serializedData3.cleanup();
                        }
                        if (tL_photo2.access_hash == 0) {
                            inputMedia3 = tL_inputMediaUploadedPhoto;
                            z7 = true;
                        } else {
                            TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto = new TLRPC.TL_inputMediaPhoto();
                            TLRPC.TL_inputPhoto tL_inputPhoto = new TLRPC.TL_inputPhoto();
                            tL_inputMediaPhoto.id = tL_inputPhoto;
                            tL_inputPhoto.id = tL_photo2.id;
                            tL_inputPhoto.access_hash = tL_photo2.access_hash;
                            byte[] bArr = tL_photo2.file_reference;
                            tL_inputPhoto.file_reference = bArr;
                            if (bArr == null) {
                                tL_inputPhoto.file_reference = new byte[0];
                            }
                            tL_inputMediaPhoto.spoiler = z2;
                            inputMedia3 = tL_inputMediaPhoto;
                            z7 = false;
                        }
                        DelayedMessage delayedMessage2 = new DelayedMessage(dialogId);
                        delayedMessage2.type = 0;
                        delayedMessage2.obj = messageObject;
                        delayedMessage2.originalPath = str5;
                        delayedMessage2.parentObject = obj3;
                        delayedMessage2.inputUploadMedia = tL_inputMediaUploadedPhoto;
                        delayedMessage2.performMediaUpload = z7;
                        if (str3 != null && str3.length() > 0) {
                            String str9 = str3;
                            if (str9.startsWith(str4)) {
                                delayedMessage2.httpLocation = str9;
                                delayedMessage = delayedMessage2;
                                hashMap3 = hashMap2;
                                z5 = z7;
                                inputMedia4 = inputMedia3;
                            }
                        }
                        ArrayList<TLRPC.PhotoSize> arrayList6 = tL_photo2.sizes;
                        delayedMessage2.photoSize = arrayList6.get(arrayList6.size() - 1);
                        delayedMessage2.locationParent = tL_photo2;
                        delayedMessage = delayedMessage2;
                        hashMap3 = hashMap2;
                        z5 = z7;
                        inputMedia4 = inputMedia3;
                    } else if (c2 == 3) {
                        TLRPC.InputMedia tL_inputMediaUploadedDocument = new TLRPC.TL_inputMediaUploadedDocument();
                        tL_inputMediaUploadedDocument.spoiler = z2;
                        if (hashMap2 != null && (str6 = hashMap2.get("masks")) != null) {
                            SerializedData serializedData4 = new SerializedData(Utilities.hexToBytes(str6));
                            boolean z10 = false;
                            int readInt322 = serializedData4.readInt32(false);
                            int i6 = 0;
                            while (i6 < readInt322) {
                                tL_inputMediaUploadedDocument.stickers.add(TLRPC.InputDocument.TLdeserialize(serializedData4, serializedData4.readInt32(z10), z10));
                                i6++;
                                z10 = false;
                            }
                            tL_inputMediaUploadedDocument.flags |= 1;
                            serializedData4.cleanup();
                        }
                        tL_inputMediaUploadedDocument.mime_type = tL_document3.mime_type;
                        tL_inputMediaUploadedDocument.attributes = tL_document3.attributes;
                        if (messageObject.isGif()) {
                            videoEditedInfo4 = videoEditedInfo3;
                        } else {
                            if (videoEditedInfo3 != null) {
                                videoEditedInfo4 = videoEditedInfo3;
                            } else {
                                videoEditedInfo4 = videoEditedInfo3;
                            }
                            tL_inputMediaUploadedDocument.nosound_video = true;
                            if (BuildVars.DEBUG_VERSION) {
                                FileLog.d("nosound_video = true");
                            }
                        }
                        if (tL_document3.access_hash == 0) {
                            videoEditedInfo5 = videoEditedInfo4;
                            z6 = true;
                            inputMedia2 = tL_inputMediaUploadedDocument;
                        } else {
                            TLRPC.TL_inputMediaDocument tL_inputMediaDocument = new TLRPC.TL_inputMediaDocument();
                            TLRPC.TL_inputDocument tL_inputDocument = new TLRPC.TL_inputDocument();
                            tL_inputMediaDocument.id = tL_inputDocument;
                            videoEditedInfo5 = videoEditedInfo4;
                            tL_inputDocument.id = tL_document3.id;
                            tL_inputDocument.access_hash = tL_document3.access_hash;
                            byte[] bArr2 = tL_document3.file_reference;
                            tL_inputDocument.file_reference = bArr2;
                            if (bArr2 == null) {
                                tL_inputDocument.file_reference = new byte[0];
                            }
                            tL_inputMediaDocument.spoiler = z2;
                            inputMedia2 = tL_inputMediaDocument;
                            z6 = false;
                        }
                        DelayedMessage delayedMessage3 = new DelayedMessage(dialogId);
                        delayedMessage3.type = 1;
                        delayedMessage3.obj = messageObject;
                        delayedMessage3.originalPath = str5;
                        delayedMessage3.parentObject = obj3;
                        delayedMessage3.inputUploadMedia = tL_inputMediaUploadedDocument;
                        delayedMessage3.performMediaUpload = z6;
                        if (!tL_document3.thumbs.isEmpty()) {
                            TLRPC.PhotoSize photoSize = tL_document3.thumbs.get(0);
                            if (!(photoSize instanceof TLRPC.TL_photoStrippedSize)) {
                                delayedMessage3.photoSize = photoSize;
                                delayedMessage3.locationParent = tL_document3;
                            }
                        }
                        delayedMessage3.videoEditedInfo = videoEditedInfo5;
                        delayedMessage = delayedMessage3;
                        hashMap3 = hashMap2;
                        z5 = z6;
                        inputMedia4 = inputMedia2;
                    } else {
                        VideoEditedInfo videoEditedInfo7 = videoEditedInfo3;
                        if (c2 == 7) {
                            TLRPC.InputMedia tL_inputMediaUploadedDocument2 = new TLRPC.TL_inputMediaUploadedDocument();
                            tL_inputMediaUploadedDocument2.mime_type = tL_document3.mime_type;
                            tL_inputMediaUploadedDocument2.attributes = tL_document3.attributes;
                            tL_inputMediaUploadedDocument2.spoiler = z2;
                            if (tL_document3.access_hash == 0) {
                                inputMedia = tL_inputMediaUploadedDocument2;
                                hashMap3 = hashMap2;
                                obj4 = obj3;
                                z4 = true;
                            } else {
                                TLRPC.TL_inputMediaDocument tL_inputMediaDocument2 = new TLRPC.TL_inputMediaDocument();
                                TLRPC.TL_inputDocument tL_inputDocument2 = new TLRPC.TL_inputDocument();
                                tL_inputMediaDocument2.id = tL_inputDocument2;
                                hashMap3 = hashMap2;
                                obj4 = obj3;
                                tL_inputDocument2.id = tL_document3.id;
                                tL_inputDocument2.access_hash = tL_document3.access_hash;
                                byte[] bArr3 = tL_document3.file_reference;
                                tL_inputDocument2.file_reference = bArr3;
                                if (bArr3 == null) {
                                    tL_inputDocument2.file_reference = new byte[0];
                                }
                                tL_inputMediaDocument2.spoiler = z2;
                                inputMedia = tL_inputMediaDocument2;
                                z4 = false;
                            }
                            DelayedMessage delayedMessage4 = new DelayedMessage(dialogId);
                            delayedMessage4.originalPath = str5;
                            delayedMessage4.type = 2;
                            delayedMessage4.obj = messageObject;
                            if (!tL_document3.thumbs.isEmpty()) {
                                if (videoEditedInfo7 != null) {
                                    if (!videoEditedInfo7.isSticker) {
                                    }
                                }
                                TLRPC.PhotoSize photoSize2 = tL_document3.thumbs.get(0);
                                if (!(photoSize2 instanceof TLRPC.TL_photoStrippedSize)) {
                                    delayedMessage4.photoSize = photoSize2;
                                    delayedMessage4.locationParent = tL_document3;
                                }
                            }
                            obj3 = obj4;
                            delayedMessage4.parentObject = obj3;
                            delayedMessage4.inputUploadMedia = tL_inputMediaUploadedDocument2;
                            delayedMessage4.performMediaUpload = z4;
                            z5 = z4;
                            delayedMessage = delayedMessage4;
                            inputMedia4 = inputMedia;
                        }
                        hashMap3 = hashMap2;
                        delayedMessage = null;
                        z5 = false;
                        inputMedia4 = null;
                    }
                    boolean z11 = inputMedia4 instanceof TLRPC.TL_inputMediaEmpty;
                    TLRPC.InputMedia inputMedia6 = inputMedia4;
                    if (z11) {
                        int i7 = messageObject.type;
                        if (i7 != 0) {
                            inputMedia6 = inputMedia4;
                        }
                        inputMedia6 = null;
                    }
                    TLRPC.TL_messages_editMessage tL_messages_editMessage = new TLRPC.TL_messages_editMessage();
                    tL_messages_editMessage.id = messageObject.getId();
                    tL_messages_editMessage.peer = getMessagesController().getInputPeer(dialogId);
                    TLRPC.Message message3 = messageObject.messageOwner;
                    tL_messages_editMessage.invert_media = message3.invert_media;
                    if (inputMedia6 != null) {
                        tL_messages_editMessage.flags |= LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
                        tL_messages_editMessage.media = inputMedia6;
                    } else if (!messageObject.editingMessageSearchWebPage) {
                        tL_messages_editMessage.no_webpage = true;
                    }
                    if (messageObject.scheduled) {
                        tL_messages_editMessage.schedule_date = message3.date;
                        tL_messages_editMessage.flags |= 32768;
                    }
                    if ((message3.flags & 1073741824) != 0) {
                        tL_messages_editMessage.quick_reply_shortcut_id = message3.quick_reply_shortcut_id;
                        tL_messages_editMessage.flags |= 131072;
                    }
                    CharSequence charSequence3 = messageObject.editingMessage;
                    if (charSequence3 != null) {
                        tL_messages_editMessage.message = charSequence3.toString();
                        int i8 = tL_messages_editMessage.flags;
                        tL_messages_editMessage.flags = i8 | 2048;
                        tL_messages_editMessage.no_webpage = !messageObject.editingMessageSearchWebPage;
                        ArrayList<TLRPC.MessageEntity> arrayList7 = messageObject.editingMessageEntities;
                        if (arrayList7 != null) {
                            tL_messages_editMessage.entities = arrayList7;
                            i = i8 | 2056;
                        } else {
                            ArrayList<TLRPC.MessageEntity> entities2 = getMediaDataController().getEntities(new CharSequence[]{messageObject.editingMessage}, z3);
                            if (entities2 != null && !entities2.isEmpty()) {
                                tL_messages_editMessage.entities = entities2;
                                i = tL_messages_editMessage.flags | 8;
                            }
                            messageObject.editingMessage = null;
                            messageObject.editingMessageEntities = null;
                        }
                        tL_messages_editMessage.flags = i;
                        messageObject.editingMessage = null;
                        messageObject.editingMessageEntities = null;
                    }
                    if (delayedMessage != null) {
                        delayedMessage.sendRequest = tL_messages_editMessage;
                    }
                    if (c2 == 1) {
                        performSendMessageRequest(tL_messages_editMessage, messageObject, null, delayedMessage, obj3, hashMap3, messageObject.scheduled);
                        return;
                    }
                    if (c2 != 2) {
                        HashMap<String, String> hashMap6 = hashMap3;
                        if (c2 == 3) {
                            if (z5) {
                            }
                            performSendMessageRequest(tL_messages_editMessage, messageObject, str5, delayedMessage, obj3, hashMap6, messageObject.scheduled);
                            return;
                        }
                        if (c2 != 6) {
                            if (c2 == 7) {
                                if (z5) {
                                }
                            } else if (c2 != '\b') {
                                return;
                            }
                        }
                        performSendMessageRequest(tL_messages_editMessage, messageObject, str5, delayedMessage, obj3, hashMap6, messageObject.scheduled);
                        return;
                    }
                    if (!z5) {
                        performSendMessageRequest(tL_messages_editMessage, messageObject, str5, null, true, delayedMessage, obj3, hashMap3, messageObject.scheduled);
                        return;
                    }
                    performSendDelayedMessage(delayedMessage);
                    return;
                }
            }
            str5 = null;
            if (c2 >= 1) {
            }
        } catch (Exception e) {
            FileLog.e(e);
            revertEditingMessageObject(messageObject);
        }
    }

    public TLRPC.TL_photo generatePhotoSizes(String str, Uri uri) {
        return generatePhotoSizes(null, str, uri);
    }

    public TLRPC.TL_photo generatePhotoSizes(TLRPC.TL_photo tL_photo, String str, Uri uri) {
        Bitmap loadBitmap = ImageLoader.loadBitmap(str, uri, AndroidUtilities.getPhotoSize(), AndroidUtilities.getPhotoSize(), true);
        if (loadBitmap == null) {
            loadBitmap = ImageLoader.loadBitmap(str, uri, 800.0f, 800.0f, true);
        }
        ArrayList<TLRPC.PhotoSize> arrayList = new ArrayList<>();
        TLRPC.PhotoSize scaleAndSaveImage = ImageLoader.scaleAndSaveImage(loadBitmap, 90.0f, 90.0f, 55, true);
        if (scaleAndSaveImage != null) {
            arrayList.add(scaleAndSaveImage);
        }
        TLRPC.PhotoSize scaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(loadBitmap, AndroidUtilities.getPhotoSize(), AndroidUtilities.getPhotoSize(), true, 80, false, 101, 101);
        if (scaleAndSaveImage2 != null) {
            arrayList.add(scaleAndSaveImage2);
        }
        if (loadBitmap != null) {
            loadBitmap.recycle();
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        getUserConfig().saveConfig(false);
        if (tL_photo == null) {
            tL_photo = new TLRPC.TL_photo();
        }
        tL_photo.date = getConnectionsManager().getCurrentTime();
        tL_photo.sizes = arrayList;
        tL_photo.file_reference = new byte[0];
        return tL_photo;
    }

    protected ArrayList<DelayedMessage> getDelayedMessages(String str) {
        return this.delayedMessages.get(str);
    }

    public ImportingHistory getImportingHistory(long j) {
        return (ImportingHistory) this.importingHistoryMap.get(j);
    }

    public ImportingStickers getImportingStickers(String str) {
        return this.importingStickersMap.get(str);
    }

    public long getNextRandomId() {
        long j = 0;
        while (j == 0) {
            j = Utilities.random.nextLong();
        }
        return j;
    }

    public int getSendingMessageId(long j) {
        for (int i = 0; i < this.sendingMessages.size(); i++) {
            TLRPC.Message valueAt = this.sendingMessages.valueAt(i);
            if (valueAt.dialog_id == j) {
                return valueAt.id;
            }
        }
        for (int i2 = 0; i2 < this.uploadMessages.size(); i2++) {
            TLRPC.Message valueAt2 = this.uploadMessages.valueAt(i2);
            if (valueAt2.dialog_id == j) {
                return valueAt2.id;
            }
        }
        return 0;
    }

    protected long getVoteSendTime(long j) {
        return ((Long) this.voteSendTime.get(j, 0L)).longValue();
    }

    public boolean isImportingHistory() {
        return this.importingHistoryMap.size() != 0;
    }

    public boolean isImportingStickers() {
        return this.importingStickersMap.size() != 0;
    }

    public boolean isSendingCallback(MessageObject messageObject, TLRPC.KeyboardButton keyboardButton) {
        int i = 0;
        if (messageObject == null || keyboardButton == null) {
            return false;
        }
        if (keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth) {
            i = 3;
        } else if (keyboardButton instanceof TLRPC.TL_keyboardButtonGame) {
            i = 1;
        } else if (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) {
            i = 2;
        }
        return this.waitingForCallback.containsKey(messageObject.getDialogId() + "_" + messageObject.getId() + "_" + Utilities.bytesToHex(keyboardButton.data) + "_" + i);
    }

    public boolean isSendingCurrentLocation(MessageObject messageObject, TLRPC.KeyboardButton keyboardButton) {
        if (messageObject == null || keyboardButton == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(messageObject.getDialogId());
        sb.append("_");
        sb.append(messageObject.getId());
        sb.append("_");
        sb.append(Utilities.bytesToHex(keyboardButton.data));
        sb.append("_");
        sb.append(keyboardButton instanceof TLRPC.TL_keyboardButtonGame ? "1" : "0");
        return this.waitingForLocation.containsKey(sb.toString());
    }

    public boolean isSendingMessage(int i) {
        return this.sendingMessages.indexOfKey(i) >= 0 || this.editingMessages.indexOfKey(i) >= 0;
    }

    public boolean isSendingMessageIdDialog(long j) {
        return ((Integer) this.sendingMessagesIdDialogs.get(j, 0)).intValue() > 0;
    }

    public boolean isSendingPaidMessage(int i, int i2) {
        HashMap<String, ArrayList<DelayedMessage>> hashMap = this.delayedMessages;
        DelayedMessage delayedMessage = null;
        if (hashMap != null) {
            for (ArrayList<DelayedMessage> arrayList : hashMap.values()) {
                if (arrayList != null) {
                    Iterator<DelayedMessage> it = arrayList.iterator();
                    while (it.hasNext()) {
                        DelayedMessage next = it.next();
                        ArrayList<TLRPC.Message> arrayList2 = next.messages;
                        if (arrayList2 != null) {
                            Iterator<TLRPC.Message> it2 = arrayList2.iterator();
                            while (true) {
                                if (!it2.hasNext()) {
                                    break;
                                }
                                TLRPC.Message next2 = it2.next();
                                if (next2 != null && next2.id == i) {
                                    delayedMessage = next;
                                    break;
                                }
                            }
                            if (delayedMessage != null) {
                                break;
                            }
                        }
                    }
                    if (delayedMessage != null) {
                        break;
                    }
                }
            }
        }
        if (delayedMessage != null && i2 >= 0 && i2 < delayedMessage.messages.size()) {
            i = delayedMessage.messages.get(i2).id;
        }
        return this.sendingMessages.indexOfKey(i) >= 0 || this.editingMessages.indexOfKey(i) >= 0;
    }

    public byte[] isSendingVote(MessageObject messageObject) {
        if (messageObject == null) {
            return null;
        }
        return this.waitingForVote.get("poll_" + messageObject.getPollId());
    }

    public boolean isUploadingMessageIdDialog(long j) {
        return ((Integer) this.uploadingMessagesIdDialogs.get(j, 0)).intValue() > 0;
    }

    public void onMessageEdited(TLRPC.Message message) {
        if (message == null || message.reply_markup == null) {
            return;
        }
        List<String> remove = this.waitingForCallbackMap.remove(message.dialog_id + "_" + message.id);
        if (remove != null) {
            Iterator<String> it = remove.iterator();
            while (it.hasNext()) {
                this.waitingForCallback.remove(it.next());
            }
        }
    }

    protected void performSendMessageRequest(final TLObject tLObject, final MessageObject messageObject, final String str, final DelayedMessage delayedMessage, final boolean z, final DelayedMessage delayedMessage2, final Object obj, HashMap<String, String> hashMap, final boolean z2) {
        DelayedMessage findMaxDelayedMessageForMessageId;
        ArrayList<DelayedMessageSendAfterRequest> arrayList;
        if (!(tLObject instanceof TLRPC.TL_messages_editMessage) && z && (findMaxDelayedMessageForMessageId = findMaxDelayedMessageForMessageId(messageObject.getId(), messageObject.getDialogId())) != null) {
            findMaxDelayedMessageForMessageId.addDelayedRequest(tLObject, messageObject, str, obj, delayedMessage2, delayedMessage != null ? delayedMessage.scheduled : false);
            if (delayedMessage == null || (arrayList = delayedMessage.requests) == null) {
                return;
            }
            findMaxDelayedMessageForMessageId.requests.addAll(arrayList);
            return;
        }
        final TLRPC.Message message = messageObject.messageOwner;
        putToSendingMessages(message, z2);
        message.reqId = getConnectionsManager().sendRequest(tLObject, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda79
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.lambda$performSendMessageRequest$63(tLObject, obj, messageObject, str, delayedMessage, z, delayedMessage2, z2, message, tLObject2, tL_error);
            }
        }, new QuickAckDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda80
            @Override // org.telegram.tgnet.QuickAckDelegate
            public final void run() {
                SendMessagesHelper.this.lambda$performSendMessageRequest$65(message);
            }
        }, (tLObject instanceof TLRPC.TL_messages_sendMessage ? 128 : 0) | 68);
        if (delayedMessage != null) {
            delayedMessage.sendDelayedRequests();
        }
    }

    protected void performSendMessageRequestMulti(final TLObject tLObject, final ArrayList<MessageObject> arrayList, final ArrayList<String> arrayList2, final ArrayList<Object> arrayList3, final DelayedMessage delayedMessage, final boolean z) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            putToSendingMessages(arrayList.get(i).messageOwner, z);
        }
        getConnectionsManager().sendRequest(tLObject, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda58
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.lambda$performSendMessageRequestMulti$48(arrayList3, tLObject, arrayList, arrayList2, delayedMessage, z, tLObject2, tL_error);
            }
        }, (QuickAckDelegate) null, 68);
    }

    public void prepareImportHistory(final long j, final Uri uri, final ArrayList<Uri> arrayList, final MessagesStorage.LongCallback longCallback) {
        if (this.importingHistoryMap.get(j) != null) {
            longCallback.run(0L);
            return;
        }
        if (DialogObject.isChatDialog(j)) {
            long j2 = -j;
            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(j2));
            if (chat != null && !chat.megagroup) {
                getMessagesController().convertToMegaGroup(null, j2, null, new MessagesStorage.LongCallback() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda40
                    @Override // org.telegram.messenger.MessagesStorage.LongCallback
                    public final void run(long j3) {
                        SendMessagesHelper.this.lambda$prepareImportHistory$67(uri, arrayList, longCallback, j3);
                    }
                });
                return;
            }
        }
        new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$prepareImportHistory$72(arrayList, j, uri, longCallback);
            }
        }).start();
    }

    public void prepareImportStickers(final String str, final String str2, final String str3, final ArrayList<ImportingSticker> arrayList, final MessagesStorage.StringCallback stringCallback) {
        if (this.importingStickersMap.get(str2) != null) {
            stringCallback.run(null);
        } else {
            new Thread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.lambda$prepareImportStickers$75(str, str2, str3, arrayList, stringCallback);
                }
            }).start();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00b9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void processForwardFromMyName(MessageObject messageObject, long j) {
        ArrayList<MessageObject> arrayList;
        SendMessageParams of;
        HashMap hashMap;
        TLRPC.Photo photo;
        if (messageObject == null) {
            return;
        }
        TLRPC.Message message = messageObject.messageOwner;
        TLRPC.MessageMedia messageMedia = message.media;
        ArrayList arrayList2 = null;
        if (messageMedia == null || (messageMedia instanceof TLRPC.TL_messageMediaEmpty) || (messageMedia instanceof TLRPC.TL_messageMediaWebPage) || (messageMedia instanceof TLRPC.TL_messageMediaGame) || (messageMedia instanceof TLRPC.TL_messageMediaInvoice)) {
            if (message.message == null) {
                if (DialogObject.isEncryptedDialog(j)) {
                    arrayList = new ArrayList<>();
                    arrayList.add(messageObject);
                    sendMessage(arrayList, j, true, false, true, 0);
                    return;
                }
                return;
            }
            TLRPC.WebPage webPage = messageMedia instanceof TLRPC.TL_messageMediaWebPage ? messageMedia.webpage : null;
            ArrayList<TLRPC.MessageEntity> arrayList3 = message.entities;
            if (arrayList3 != null && !arrayList3.isEmpty()) {
                arrayList2 = new ArrayList();
                for (int i = 0; i < messageObject.messageOwner.entities.size(); i++) {
                    TLRPC.MessageEntity messageEntity = messageObject.messageOwner.entities.get(i);
                    if ((messageEntity instanceof TLRPC.TL_messageEntityBold) || (messageEntity instanceof TLRPC.TL_messageEntityItalic) || (messageEntity instanceof TLRPC.TL_messageEntityPre) || (messageEntity instanceof TLRPC.TL_messageEntityCode) || (messageEntity instanceof TLRPC.TL_messageEntityTextUrl) || (messageEntity instanceof TLRPC.TL_messageEntitySpoiler) || (messageEntity instanceof TLRPC.TL_messageEntityCustomEmoji)) {
                        arrayList2.add(messageEntity);
                    }
                }
            }
            of = SendMessageParams.of(messageObject.messageOwner.message, j, messageObject.replyMessageObject, null, webPage, true, arrayList2, null, null, true, 0, null, false);
            sendMessage(of);
        }
        if (DialogObject.isEncryptedDialog(j)) {
            TLRPC.Message message2 = messageObject.messageOwner;
            if (message2.peer_id != null) {
                TLRPC.MessageMedia messageMedia2 = message2.media;
                if ((messageMedia2.photo instanceof TLRPC.TL_photo) || (messageMedia2.document instanceof TLRPC.TL_document)) {
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("parentObject", "sent_" + messageObject.messageOwner.peer_id.channel_id + "_" + messageObject.getId() + "_" + messageObject.getDialogId() + "_" + messageObject.type + "_" + messageObject.getSize());
                    hashMap = hashMap2;
                    TLRPC.Message message3 = messageObject.messageOwner;
                    TLRPC.MessageMedia messageMedia3 = message3.media;
                    photo = messageMedia3.photo;
                    if (photo instanceof TLRPC.TL_photo) {
                        TLRPC.Document document = messageMedia3.document;
                        if (document instanceof TLRPC.TL_document) {
                            of = SendMessageParams.of((TLRPC.TL_document) document, null, message3.attachPath, j, messageObject.replyMessageObject, null, message3.message, message3.entities, null, hashMap, true, 0, messageMedia3.ttl_seconds, messageObject, null, false);
                        } else if ((messageMedia3 instanceof TLRPC.TL_messageMediaVenue) || (messageMedia3 instanceof TLRPC.TL_messageMediaGeo)) {
                            of = SendMessageParams.of(messageMedia3, j, messageObject.replyMessageObject, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, true, 0);
                        } else {
                            if (messageMedia3.phone_number == null) {
                                if (DialogObject.isEncryptedDialog(j)) {
                                    return;
                                }
                                arrayList = new ArrayList<>();
                                arrayList.add(messageObject);
                                sendMessage(arrayList, j, true, false, true, 0);
                                return;
                            }
                            TLRPC.TL_userContact_old2 tL_userContact_old2 = new TLRPC.TL_userContact_old2();
                            TLRPC.MessageMedia messageMedia4 = messageObject.messageOwner.media;
                            tL_userContact_old2.phone = messageMedia4.phone_number;
                            tL_userContact_old2.first_name = messageMedia4.first_name;
                            tL_userContact_old2.last_name = messageMedia4.last_name;
                            tL_userContact_old2.id = messageMedia4.user_id;
                            of = SendMessageParams.of((TLRPC.User) tL_userContact_old2, j, messageObject.replyMessageObject, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, true, 0);
                        }
                    } else {
                        of = SendMessageParams.of((TLRPC.TL_photo) photo, null, j, messageObject.replyMessageObject, null, message3.message, message3.entities, null, hashMap, true, 0, messageMedia3.ttl_seconds, messageObject, false);
                    }
                    sendMessage(of);
                }
            }
        }
        hashMap = null;
        TLRPC.Message message32 = messageObject.messageOwner;
        TLRPC.MessageMedia messageMedia32 = message32.media;
        photo = messageMedia32.photo;
        if (photo instanceof TLRPC.TL_photo) {
        }
        sendMessage(of);
    }

    protected void processSentMessage(int i) {
        int size = this.unsentMessages.size();
        this.unsentMessages.remove(i);
        if (size == 0 || this.unsentMessages.size() != 0) {
            return;
        }
        checkUnsentMessages();
    }

    protected void processUnsentMessages(final ArrayList<TLRPC.Message> arrayList, final ArrayList<TLRPC.Message> arrayList2, final ArrayList<TLRPC.User> arrayList3, final ArrayList<TLRPC.Chat> arrayList4, final ArrayList<TLRPC.EncryptedChat> arrayList5) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda78
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$processUnsentMessages$66(arrayList3, arrayList4, arrayList5, arrayList, arrayList2);
            }
        });
    }

    protected void putToSendingMessages(final TLRPC.Message message, final boolean z) {
        if (Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda96
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.lambda$putToSendingMessages$39(message, z);
                }
            });
        } else {
            putToSendingMessages(message, z, true);
        }
    }

    protected void putToSendingMessages(TLRPC.Message message, boolean z, boolean z2) {
        if (message == null) {
            return;
        }
        int i = message.id;
        if (i > 0) {
            this.editingMessages.put(i, message);
            return;
        }
        boolean z3 = this.sendingMessages.indexOfKey(i) >= 0;
        removeFromUploadingMessages(message.id, z);
        this.sendingMessages.put(message.id, message);
        if (z || z3) {
            return;
        }
        long dialogId = MessageObject.getDialogId(message);
        LongSparseArray longSparseArray = this.sendingMessagesIdDialogs;
        longSparseArray.put(dialogId, Integer.valueOf(((Integer) longSparseArray.get(dialogId, 0)).intValue() + 1));
        if (z2) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.sendingMessagesChanged, new Object[0]);
        }
    }

    protected void putToUploadingMessages(MessageObject messageObject) {
        if (messageObject == null || messageObject.getId() > 0 || messageObject.scheduled) {
            return;
        }
        TLRPC.Message message = messageObject.messageOwner;
        boolean z = this.uploadMessages.indexOfKey(message.id) >= 0;
        this.uploadMessages.put(message.id, message);
        if (z) {
            return;
        }
        long dialogId = MessageObject.getDialogId(message);
        LongSparseArray longSparseArray = this.uploadingMessagesIdDialogs;
        longSparseArray.put(dialogId, Integer.valueOf(((Integer) longSparseArray.get(dialogId, 0)).intValue() + 1));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.sendingMessagesChanged, new Object[0]);
    }

    protected TLRPC.Message removeFromSendingMessages(int i, boolean z) {
        if (i > 0) {
            TLRPC.Message message = this.editingMessages.get(i);
            if (message == null) {
                return message;
            }
            this.editingMessages.remove(i);
            return message;
        }
        TLRPC.Message message2 = this.sendingMessages.get(i);
        if (message2 != null) {
            this.sendingMessages.remove(i);
            if (!z) {
                long dialogId = MessageObject.getDialogId(message2);
                Integer num = (Integer) this.sendingMessagesIdDialogs.get(dialogId);
                if (num != null) {
                    int intValue = num.intValue() - 1;
                    if (intValue <= 0) {
                        this.sendingMessagesIdDialogs.remove(dialogId);
                    } else {
                        this.sendingMessagesIdDialogs.put(dialogId, Integer.valueOf(intValue));
                    }
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.sendingMessagesChanged, new Object[0]);
                }
            }
        }
        return message2;
    }

    protected void removeFromUploadingMessages(int i, boolean z) {
        TLRPC.Message message;
        if (i > 0 || z || (message = this.uploadMessages.get(i)) == null) {
            return;
        }
        this.uploadMessages.remove(i);
        long dialogId = MessageObject.getDialogId(message);
        Integer num = (Integer) this.uploadingMessagesIdDialogs.get(dialogId);
        if (num != null) {
            int intValue = num.intValue() - 1;
            if (intValue <= 0) {
                this.uploadingMessagesIdDialogs.remove(dialogId);
            } else {
                this.uploadingMessagesIdDialogs.put(dialogId, Integer.valueOf(intValue));
            }
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.sendingMessagesChanged, new Object[0]);
        }
    }

    public void requestUrlAuth(final String str, final ChatActivity chatActivity, final boolean z) {
        final TLRPC.TL_messages_requestUrlAuth tL_messages_requestUrlAuth = new TLRPC.TL_messages_requestUrlAuth();
        tL_messages_requestUrlAuth.url = str;
        tL_messages_requestUrlAuth.flags |= 4;
        getConnectionsManager().sendRequest(tL_messages_requestUrlAuth, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda86
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.lambda$requestUrlAuth$24(ChatActivity.this, tL_messages_requestUrlAuth, str, z, tLObject, tL_error);
            }
        }, 2);
    }

    public boolean retrySendMessage(MessageObject messageObject, boolean z) {
        if (messageObject.getId() >= 0) {
            if (messageObject.isEditing()) {
                editMessage(messageObject, null, null, null, null, null, true, messageObject.hasMediaSpoilers(), messageObject);
            }
            return false;
        }
        TLRPC.MessageAction messageAction = messageObject.messageOwner.action;
        if (!(messageAction instanceof TLRPC.TL_messageEncryptedAction)) {
            if (messageAction instanceof TLRPC.TL_messageActionScreenshotTaken) {
                sendScreenshotMessage(getMessagesController().getUser(Long.valueOf(messageObject.getDialogId())), messageObject.getReplyMsgId(), messageObject.messageOwner);
            }
            if (z) {
                this.unsentMessages.put(messageObject.getId(), messageObject);
            }
            sendMessage(SendMessageParams.of(messageObject));
            return true;
        }
        TLRPC.EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(messageObject.getDialogId())));
        if (encryptedChat == null) {
            getMessagesStorage().markMessageAsSendError(messageObject.messageOwner, messageObject.scheduled ? 1 : 0);
            messageObject.messageOwner.send_state = 2;
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.messageSendError, Integer.valueOf(messageObject.getId()));
            processSentMessage(messageObject.getId());
            return false;
        }
        TLRPC.Message message = messageObject.messageOwner;
        if (message.random_id == 0) {
            message.random_id = getNextRandomId();
        }
        TLRPC.DecryptedMessageAction decryptedMessageAction = messageObject.messageOwner.action.encryptedAction;
        if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
            getSecretChatHelper().sendTTLMessage(encryptedChat, messageObject.messageOwner);
        } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionDeleteMessages) {
            getSecretChatHelper().sendMessagesDeleteMessage(encryptedChat, null, messageObject.messageOwner);
        } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionFlushHistory) {
            getSecretChatHelper().sendClearHistoryMessage(encryptedChat, messageObject.messageOwner);
        } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionNotifyLayer) {
            getSecretChatHelper().sendNotifyLayerMessage(encryptedChat, messageObject.messageOwner);
        } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionReadMessages) {
            getSecretChatHelper().sendMessagesReadMessage(encryptedChat, null, messageObject.messageOwner);
        } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
            getSecretChatHelper().sendScreenshotMessage(encryptedChat, null, messageObject.messageOwner);
        } else if (!(decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionTyping)) {
            if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionResend) {
                getSecretChatHelper().sendResendMessage(encryptedChat, 0, 0, messageObject.messageOwner);
            } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionCommitKey) {
                getSecretChatHelper().sendCommitKeyMessage(encryptedChat, messageObject.messageOwner);
            } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionAbortKey) {
                getSecretChatHelper().sendAbortKeyMessage(encryptedChat, messageObject.messageOwner, 0L);
            } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionRequestKey) {
                getSecretChatHelper().sendRequestKeyMessage(encryptedChat, messageObject.messageOwner);
            } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionAcceptKey) {
                getSecretChatHelper().sendAcceptKeyMessage(encryptedChat, messageObject.messageOwner);
            } else if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionNoop) {
                getSecretChatHelper().sendNoopMessage(encryptedChat, messageObject.messageOwner);
            }
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00a8  */
    /* renamed from: sendCallback, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void lambda$sendCallback$27(final boolean z, final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton, final TLRPC.InputCheckPasswordSRP inputCheckPasswordSRP, final TwoStepVerificationActivity twoStepVerificationActivity, final ChatActivity chatActivity) {
        boolean z2;
        int i;
        List<String> list;
        final List<String> list2;
        TLRPC.TL_messages_getBotCallbackAnswer tL_messages_getBotCallbackAnswer;
        if (messageObject == null || keyboardButton == null || chatActivity == null) {
            return;
        }
        boolean z3 = keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth;
        if (z3) {
            i = 3;
        } else {
            if (!(keyboardButton instanceof TLRPC.TL_keyboardButtonGame)) {
                z2 = z;
                i = keyboardButton instanceof TLRPC.TL_keyboardButtonBuy ? 2 : 0;
                final String str = messageObject.getDialogId() + "_" + messageObject.getId() + "_" + Utilities.bytesToHex(keyboardButton.data) + "_" + i;
                this.waitingForCallback.put(str, Boolean.TRUE);
                list = this.waitingForCallbackMap.get(messageObject.getDialogId() + "_" + messageObject.getId());
                if (list != null) {
                    HashMap<String, List<String>> hashMap = this.waitingForCallbackMap;
                    String str2 = messageObject.getDialogId() + "_" + messageObject.getId();
                    ArrayList arrayList = new ArrayList();
                    hashMap.put(str2, arrayList);
                    list2 = arrayList;
                } else {
                    list2 = list;
                }
                list2.add(str);
                final TLObject[] tLObjectArr = new TLObject[1];
                final boolean z4 = z2;
                RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda94
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        SendMessagesHelper.this.lambda$sendCallback$33(str, list2, z4, messageObject, keyboardButton, chatActivity, twoStepVerificationActivity, tLObjectArr, inputCheckPasswordSRP, z, tLObject, tL_error);
                    }
                };
                if (!z2) {
                    getMessagesStorage().getBotCache(str, requestDelegate);
                    return;
                }
                if (z3) {
                    TLRPC.TL_messages_requestUrlAuth tL_messages_requestUrlAuth = new TLRPC.TL_messages_requestUrlAuth();
                    tL_messages_requestUrlAuth.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
                    tL_messages_requestUrlAuth.msg_id = messageObject.getId();
                    tL_messages_requestUrlAuth.button_id = keyboardButton.button_id;
                    tL_messages_requestUrlAuth.flags |= 2;
                    tLObjectArr[0] = tL_messages_requestUrlAuth;
                    getConnectionsManager().sendRequest(tL_messages_requestUrlAuth, requestDelegate, 2);
                    return;
                }
                if (!(keyboardButton instanceof TLRPC.TL_keyboardButtonBuy)) {
                    TLRPC.TL_messages_getBotCallbackAnswer tL_messages_getBotCallbackAnswer2 = new TLRPC.TL_messages_getBotCallbackAnswer();
                    tL_messages_getBotCallbackAnswer2.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
                    tL_messages_getBotCallbackAnswer2.msg_id = messageObject.getId();
                    tL_messages_getBotCallbackAnswer2.game = keyboardButton instanceof TLRPC.TL_keyboardButtonGame;
                    if (keyboardButton.requires_password) {
                        tL_messages_getBotCallbackAnswer2.password = inputCheckPasswordSRP != null ? inputCheckPasswordSRP : new TLRPC.TL_inputCheckPasswordEmpty();
                        tL_messages_getBotCallbackAnswer2.flags |= 4;
                    }
                    byte[] bArr = keyboardButton.data;
                    tL_messages_getBotCallbackAnswer = tL_messages_getBotCallbackAnswer2;
                    if (bArr != null) {
                        tL_messages_getBotCallbackAnswer2.flags |= 1;
                        tL_messages_getBotCallbackAnswer2.data = bArr;
                        tL_messages_getBotCallbackAnswer = tL_messages_getBotCallbackAnswer2;
                    }
                } else if ((messageObject.messageOwner.media.flags & 4) == 0) {
                    TLRPC.TL_payments_getPaymentForm tL_payments_getPaymentForm = new TLRPC.TL_payments_getPaymentForm();
                    TLRPC.TL_inputInvoiceMessage tL_inputInvoiceMessage = new TLRPC.TL_inputInvoiceMessage();
                    tL_inputInvoiceMessage.msg_id = messageObject.getId();
                    tL_inputInvoiceMessage.peer = getMessagesController().getInputPeer(messageObject.messageOwner.peer_id);
                    tL_payments_getPaymentForm.invoice = tL_inputInvoiceMessage;
                    JSONObject makeThemeParams = BotWebViewSheet.makeThemeParams(null);
                    if (makeThemeParams != null) {
                        TLRPC.TL_dataJSON tL_dataJSON = new TLRPC.TL_dataJSON();
                        tL_payments_getPaymentForm.theme_params = tL_dataJSON;
                        tL_dataJSON.data = makeThemeParams.toString();
                        tL_payments_getPaymentForm.flags |= 1;
                    }
                    tLObjectArr[0] = tL_payments_getPaymentForm;
                    tL_messages_getBotCallbackAnswer = tL_payments_getPaymentForm;
                } else {
                    TLRPC.TL_payments_getPaymentReceipt tL_payments_getPaymentReceipt = new TLRPC.TL_payments_getPaymentReceipt();
                    tL_payments_getPaymentReceipt.msg_id = messageObject.messageOwner.media.receipt_msg_id;
                    tL_payments_getPaymentReceipt.peer = getMessagesController().getInputPeer(messageObject.messageOwner.peer_id);
                    tLObjectArr[0] = tL_payments_getPaymentReceipt;
                    tL_messages_getBotCallbackAnswer = tL_payments_getPaymentReceipt;
                }
                getConnectionsManager().sendRequest(tL_messages_getBotCallbackAnswer, requestDelegate, 2);
                return;
            }
            i = 1;
        }
        z2 = false;
        final String str3 = messageObject.getDialogId() + "_" + messageObject.getId() + "_" + Utilities.bytesToHex(keyboardButton.data) + "_" + i;
        this.waitingForCallback.put(str3, Boolean.TRUE);
        list = this.waitingForCallbackMap.get(messageObject.getDialogId() + "_" + messageObject.getId());
        if (list != null) {
        }
        list2.add(str3);
        final TLObject[] tLObjectArr2 = new TLObject[1];
        final boolean z42 = z2;
        RequestDelegate requestDelegate2 = new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda94
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.lambda$sendCallback$33(str3, list2, z42, messageObject, keyboardButton, chatActivity, twoStepVerificationActivity, tLObjectArr2, inputCheckPasswordSRP, z, tLObject, tL_error);
            }
        };
        if (!z2) {
        }
    }

    public void sendCallback(boolean z, MessageObject messageObject, TLRPC.KeyboardButton keyboardButton, ChatActivity chatActivity) {
        lambda$sendCallback$27(z, messageObject, keyboardButton, null, null, chatActivity);
    }

    public void sendCurrentLocation(MessageObject messageObject, TLRPC.KeyboardButton keyboardButton) {
        if (messageObject == null || keyboardButton == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(messageObject.getDialogId());
        sb.append("_");
        sb.append(messageObject.getId());
        sb.append("_");
        sb.append(Utilities.bytesToHex(keyboardButton.data));
        sb.append("_");
        sb.append(keyboardButton instanceof TLRPC.TL_keyboardButtonGame ? "1" : "0");
        this.waitingForLocation.put(sb.toString(), messageObject);
        this.locationProvider.start();
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00a5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0061  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void sendGame(TLRPC.InputPeer inputPeer, TLRPC.TL_inputMediaGame tL_inputMediaGame, long j, final long j2) {
        SharedPreferences notificationsSettings;
        StringBuilder sb;
        long j3;
        long j4;
        long sendAsPeerId;
        NativeByteBuffer nativeByteBuffer;
        if (inputPeer == null || tL_inputMediaGame == null) {
            return;
        }
        TLRPC.TL_messages_sendMedia tL_messages_sendMedia = new TLRPC.TL_messages_sendMedia();
        tL_messages_sendMedia.peer = inputPeer;
        if (inputPeer instanceof TLRPC.TL_inputPeerChannel) {
            notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
            sb = new StringBuilder();
            sb.append(NotificationsSettingsFacade.PROPERTY_SILENT);
            j4 = inputPeer.channel_id;
        } else {
            if (!(inputPeer instanceof TLRPC.TL_inputPeerChat)) {
                notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                sb = new StringBuilder();
                sb.append(NotificationsSettingsFacade.PROPERTY_SILENT);
                j3 = inputPeer.user_id;
                sb.append(j3);
                tL_messages_sendMedia.silent = notificationsSettings.getBoolean(sb.toString(), false);
                tL_messages_sendMedia.random_id = j == 0 ? j : getNextRandomId();
                tL_messages_sendMedia.message = "";
                tL_messages_sendMedia.media = tL_inputMediaGame;
                sendAsPeerId = ChatObject.getSendAsPeerId(getMessagesController().getChat(Long.valueOf(inputPeer.chat_id)), getMessagesController().getChatFull(inputPeer.chat_id));
                if (sendAsPeerId != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                    tL_messages_sendMedia.send_as = getMessagesController().getInputPeer(sendAsPeerId);
                }
                if (j2 == 0) {
                    try {
                        nativeByteBuffer = new NativeByteBuffer(inputPeer.getObjectSize() + tL_inputMediaGame.getObjectSize() + 12);
                    } catch (Exception e) {
                        e = e;
                        nativeByteBuffer = null;
                    }
                    try {
                        nativeByteBuffer.writeInt32(3);
                        nativeByteBuffer.writeInt64(j);
                        inputPeer.serializeToStream(nativeByteBuffer);
                        tL_inputMediaGame.serializeToStream(nativeByteBuffer);
                    } catch (Exception e2) {
                        e = e2;
                        FileLog.e(e);
                        j2 = getMessagesStorage().createPendingTask(nativeByteBuffer);
                        getConnectionsManager().sendRequest(tL_messages_sendMedia, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda92
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                SendMessagesHelper.this.lambda$sendGame$34(j2, tLObject, tL_error);
                            }
                        });
                    }
                    j2 = getMessagesStorage().createPendingTask(nativeByteBuffer);
                }
                getConnectionsManager().sendRequest(tL_messages_sendMedia, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda92
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        SendMessagesHelper.this.lambda$sendGame$34(j2, tLObject, tL_error);
                    }
                });
            }
            notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
            sb = new StringBuilder();
            sb.append(NotificationsSettingsFacade.PROPERTY_SILENT);
            j4 = inputPeer.chat_id;
        }
        j3 = -j4;
        sb.append(j3);
        tL_messages_sendMedia.silent = notificationsSettings.getBoolean(sb.toString(), false);
        tL_messages_sendMedia.random_id = j == 0 ? j : getNextRandomId();
        tL_messages_sendMedia.message = "";
        tL_messages_sendMedia.media = tL_inputMediaGame;
        sendAsPeerId = ChatObject.getSendAsPeerId(getMessagesController().getChat(Long.valueOf(inputPeer.chat_id)), getMessagesController().getChatFull(inputPeer.chat_id));
        if (sendAsPeerId != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
        }
        if (j2 == 0) {
        }
        getConnectionsManager().sendRequest(tL_messages_sendMedia, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda92
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.lambda$sendGame$34(j2, tLObject, tL_error);
            }
        });
    }

    public int sendMessage(ArrayList<MessageObject> arrayList, long j, boolean z, boolean z2, boolean z3, int i) {
        return sendMessage(arrayList, j, z, z2, z3, i, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:250:0x0777, code lost:
    
        if (r15.contains(java.lang.Integer.valueOf(r0.reply_to_msg_id)) != false) goto L339;
     */
    /* JADX WARN: Removed duplicated region for block: B:155:0x046c  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x060c  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0616  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x062f  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x06b9  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x06e4  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x076d  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0795  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x07b0  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x07e9  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x07f3  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x0829  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x0859  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x08a9  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x08c3  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x08e5  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x090b  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x0919  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x0929  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x0976  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x0995  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x09f4  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x0998  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x0987 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:319:0x0955  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x08c5  */
    /* JADX WARN: Removed duplicated region for block: B:322:0x08ac  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x07eb  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x0797  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x06e6  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x068a  */
    /* JADX WARN: Removed duplicated region for block: B:344:0x069c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int sendMessage(ArrayList<MessageObject> arrayList, final long j, boolean z, boolean z2, boolean z3, final int i, MessageObject messageObject) {
        TLRPC.UserFull userFull;
        TLRPC.Peer peer;
        long j2;
        boolean z4;
        boolean z5;
        long j3;
        boolean z6;
        String str;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        long j4;
        boolean z11;
        boolean z12;
        boolean canSendEmbed;
        boolean canSendPolls;
        boolean canSendRoundVideo;
        boolean canSendVoice;
        boolean canSendMusic;
        TLRPC.Chat chat;
        String str2;
        LongSparseArray longSparseArray;
        long j5;
        final LongSparseArray longSparseArray2;
        long j6;
        String str3;
        TLRPC.Chat chat2;
        int i2;
        final TLRPC.Peer peer2;
        ArrayList<MessageObject> arrayList2;
        TLRPC.InputPeer inputPeer;
        ArrayList<Integer> arrayList3;
        final ArrayList arrayList4;
        ArrayList<Long> arrayList5;
        int i3;
        boolean z13;
        ArrayList<Long> arrayList6;
        long j7;
        LongSparseArray longSparseArray3;
        String str4;
        TLRPC.Chat chat3;
        LongSparseArray longSparseArray4;
        long j8;
        long j9;
        long j10;
        TLRPC.Peer peer3;
        TLRPC.Chat chat4;
        long sendAsPeerId;
        String str5;
        boolean z14;
        LongSparseArray longSparseArray5;
        TLRPC.Message message;
        long j11;
        ArrayList arrayList7;
        ArrayList<MessageObject> arrayList8;
        ArrayList<Long> arrayList9;
        ArrayList<Integer> arrayList10;
        int i4;
        boolean z15;
        int i5;
        boolean z16;
        TLRPC.Message message2;
        TLRPC.MessageReplyHeader messageReplyHeader;
        TLRPC.Peer peer4;
        TLRPC.MessageReplyHeader messageReplyHeader2;
        TLRPC.TL_keyboardButtonRow tL_keyboardButtonRow;
        TLRPC.User user;
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.Peer peer5;
        int i6;
        ArrayList<MessageObject> arrayList11 = arrayList;
        long j12 = j;
        boolean z17 = z;
        boolean z18 = z2;
        if (arrayList11 == null || arrayList.isEmpty()) {
            return 0;
        }
        long clientUserId = getUserConfig().getClientUserId();
        if (DialogObject.isEncryptedDialog(j)) {
            long j13 = getMessagesController().getEncryptedChat(Integer.valueOf((int) j)).user_id;
            boolean z19 = (!DialogObject.isUserDialog(j13) || getMessagesController().getUser(Long.valueOf(j13)) == null || (userFull = getMessagesController().getUserFull(j13)) == null) ? true : !userFull.voice_messages_forbidden;
            int i7 = 0;
            for (int i8 = 0; i8 < arrayList.size(); i8++) {
                MessageObject messageObject2 = arrayList.get(i8);
                if (z19 || !MessageObject.isVoiceMessage(messageObject2.messageOwner)) {
                    if (!z19 && MessageObject.isRoundVideoMessage(messageObject2.messageOwner) && i7 == 0) {
                        i7 = 8;
                    }
                } else if (i7 == 0) {
                    i7 = 7;
                }
            }
            if (i7 == 0) {
                for (int i9 = 0; i9 < arrayList.size(); i9++) {
                    processForwardFromMyName(arrayList.get(i9), j);
                }
            }
            return i7;
        }
        TLRPC.Peer peer6 = getMessagesController().getPeer(j12);
        if (!DialogObject.isUserDialog(j)) {
            TLRPC.Chat chat5 = getMessagesController().getChat(Long.valueOf(-j12));
            if (ChatObject.isChannel(chat5)) {
                z4 = chat5.signatures;
                z5 = !chat5.megagroup;
                if (z5 && chat5.has_link) {
                    peer = peer6;
                    TLRPC.ChatFull chatFull = getMessagesController().getChatFull(chat5.id);
                    if (chatFull != null) {
                        j2 = chatFull.linked_chat_id;
                    }
                } else {
                    peer = peer6;
                }
                j2 = 0;
            } else {
                peer = peer6;
                j2 = 0;
                z4 = false;
                z5 = false;
            }
            if (chat5 != null) {
                j3 = j2;
                z6 = z4;
                str = getMessagesController().getAdminRank(chat5.id, clientUserId);
            } else {
                j3 = j2;
                z6 = z4;
                str = null;
            }
            boolean canSendStickers = ChatObject.canSendStickers(chat5);
            boolean canSendPhoto = ChatObject.canSendPhoto(chat5);
            boolean canSendVideo = ChatObject.canSendVideo(chat5);
            boolean canSendDocument = ChatObject.canSendDocument(chat5);
            z7 = canSendStickers;
            z8 = z6;
            z9 = z5;
            z10 = canSendPhoto;
            j4 = j3;
            z11 = canSendVideo;
            z12 = canSendDocument;
            canSendEmbed = ChatObject.canSendEmbed(chat5);
            canSendPolls = ChatObject.canSendPolls(chat5);
            canSendRoundVideo = ChatObject.canSendRoundVideo(chat5);
            canSendVoice = ChatObject.canSendVoice(chat5);
            canSendMusic = ChatObject.canSendMusic(chat5);
            chat = chat5;
            str2 = str;
        } else {
            if (getMessagesController().getUser(Long.valueOf(j)) == null) {
                return 0;
            }
            TLRPC.UserFull userFull2 = getMessagesController().getUserFull(j12);
            if (userFull2 != null) {
                canSendVoice = !userFull2.voice_messages_forbidden;
                canSendRoundVideo = canSendVoice;
                peer = peer6;
                j4 = 0;
                str2 = null;
                chat = null;
            } else {
                peer = peer6;
                j4 = 0;
                str2 = null;
                chat = null;
                canSendVoice = true;
                canSendRoundVideo = true;
            }
            z8 = false;
            z7 = true;
            z9 = false;
            z10 = true;
            z11 = true;
            z12 = true;
            canSendEmbed = true;
            canSendPolls = true;
            canSendMusic = true;
        }
        LongSparseArray longSparseArray6 = new LongSparseArray();
        ArrayList<MessageObject> arrayList12 = new ArrayList<>();
        ArrayList arrayList13 = new ArrayList();
        ArrayList<Long> arrayList14 = new ArrayList<>();
        ArrayList<Integer> arrayList15 = new ArrayList<>();
        LongSparseArray longSparseArray7 = new LongSparseArray();
        TLRPC.InputPeer inputPeer2 = getMessagesController().getInputPeer(j12);
        boolean z20 = j12 == clientUserId;
        ArrayList arrayList16 = arrayList13;
        int i10 = 0;
        int i11 = 0;
        ArrayList<MessageObject> arrayList17 = arrayList12;
        ArrayList<Long> arrayList18 = arrayList14;
        TLRPC.InputPeer inputPeer3 = inputPeer2;
        LongSparseArray longSparseArray8 = longSparseArray7;
        while (i10 < arrayList.size()) {
            final MessageObject messageObject3 = arrayList11.get(i10);
            if (messageObject3.getId() <= 0 || messageObject3.needDrawBluredPreview()) {
                longSparseArray = longSparseArray6;
                j5 = j4;
                longSparseArray2 = longSparseArray8;
                j6 = clientUserId;
                str3 = str2;
                chat2 = chat;
                i2 = i10;
                peer2 = peer;
                arrayList2 = arrayList17;
                inputPeer = inputPeer3;
                arrayList3 = arrayList15;
                arrayList4 = arrayList16;
                arrayList5 = arrayList18;
                if (messageObject3.type == 0 && !TextUtils.isEmpty(messageObject3.messageText)) {
                    TLRPC.MessageMedia messageMedia = messageObject3.messageOwner.media;
                    TLRPC.WebPage webPage = messageMedia != null ? messageMedia.webpage : null;
                    SendMessageParams of = SendMessageParams.of(messageObject3.messageText.toString(), j, null, messageObject, webPage, webPage != null, messageObject3.messageOwner.entities, null, null, z3, i, null, false);
                    of.quick_reply_shortcut = messageObject3.getQuickReplyName();
                    of.quick_reply_shortcut_id = messageObject3.getQuickReplyId();
                    sendMessage(of);
                    arrayList18 = arrayList5;
                    arrayList17 = arrayList2;
                    arrayList16 = arrayList4;
                    longSparseArray8 = longSparseArray2;
                    arrayList15 = arrayList3;
                }
                arrayList18 = arrayList5;
                arrayList17 = arrayList2;
                arrayList16 = arrayList4;
                longSparseArray8 = longSparseArray2;
                arrayList15 = arrayList3;
            } else {
                if (messageObject3.isSticker() || messageObject3.isAnimatedSticker() || messageObject3.isGif() || messageObject3.isGame()) {
                    i3 = i10;
                    z13 = true;
                } else {
                    i3 = i10;
                    z13 = false;
                }
                if (!z7 && z13) {
                    if (i11 == 0) {
                        i6 = ChatObject.isActionBannedByDefault(chat, 8) ? 4 : 1;
                        longSparseArray = longSparseArray6;
                        j5 = j4;
                        j6 = clientUserId;
                        str3 = str2;
                        chat2 = chat;
                        i11 = i6;
                        peer2 = peer;
                        inputPeer = inputPeer3;
                        i2 = i3;
                    }
                    longSparseArray = longSparseArray6;
                    j5 = j4;
                    longSparseArray2 = longSparseArray8;
                    j6 = clientUserId;
                    str3 = str2;
                    chat2 = chat;
                    peer2 = peer;
                    arrayList2 = arrayList17;
                    inputPeer = inputPeer3;
                    arrayList3 = arrayList15;
                    i2 = i3;
                    arrayList4 = arrayList16;
                    arrayList5 = arrayList18;
                    arrayList18 = arrayList5;
                    arrayList17 = arrayList2;
                    arrayList16 = arrayList4;
                    longSparseArray8 = longSparseArray2;
                    arrayList15 = arrayList3;
                } else if (!z10 && (messageObject3.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) && !messageObject3.isVideo() && !z13) {
                    if (i11 == 0) {
                        i6 = ChatObject.isActionBannedByDefault(chat, 16) ? 10 : 12;
                        longSparseArray = longSparseArray6;
                        j5 = j4;
                        j6 = clientUserId;
                        str3 = str2;
                        chat2 = chat;
                        i11 = i6;
                        peer2 = peer;
                        inputPeer = inputPeer3;
                        i2 = i3;
                    }
                    longSparseArray = longSparseArray6;
                    j5 = j4;
                    longSparseArray2 = longSparseArray8;
                    j6 = clientUserId;
                    str3 = str2;
                    chat2 = chat;
                    peer2 = peer;
                    arrayList2 = arrayList17;
                    inputPeer = inputPeer3;
                    arrayList3 = arrayList15;
                    i2 = i3;
                    arrayList4 = arrayList16;
                    arrayList5 = arrayList18;
                    arrayList18 = arrayList5;
                    arrayList17 = arrayList2;
                    arrayList16 = arrayList4;
                    longSparseArray8 = longSparseArray2;
                    arrayList15 = arrayList3;
                } else if (!canSendMusic && messageObject3.isMusic()) {
                    if (i11 == 0) {
                        i6 = ChatObject.isActionBannedByDefault(chat, 18) ? 19 : 20;
                        longSparseArray = longSparseArray6;
                        j5 = j4;
                        j6 = clientUserId;
                        str3 = str2;
                        chat2 = chat;
                        i11 = i6;
                        peer2 = peer;
                        inputPeer = inputPeer3;
                        i2 = i3;
                    }
                    longSparseArray = longSparseArray6;
                    j5 = j4;
                    longSparseArray2 = longSparseArray8;
                    j6 = clientUserId;
                    str3 = str2;
                    chat2 = chat;
                    peer2 = peer;
                    arrayList2 = arrayList17;
                    inputPeer = inputPeer3;
                    arrayList3 = arrayList15;
                    i2 = i3;
                    arrayList4 = arrayList16;
                    arrayList5 = arrayList18;
                    arrayList18 = arrayList5;
                    arrayList17 = arrayList2;
                    arrayList16 = arrayList4;
                    longSparseArray8 = longSparseArray2;
                    arrayList15 = arrayList3;
                } else if (!z11 && (messageObject3.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) && messageObject3.isVideo() && !z13) {
                    if (i11 == 0) {
                        i6 = ChatObject.isActionBannedByDefault(chat, 17) ? 9 : 11;
                        longSparseArray = longSparseArray6;
                        j5 = j4;
                        j6 = clientUserId;
                        str3 = str2;
                        chat2 = chat;
                        i11 = i6;
                        peer2 = peer;
                        inputPeer = inputPeer3;
                        i2 = i3;
                    }
                    longSparseArray = longSparseArray6;
                    j5 = j4;
                    longSparseArray2 = longSparseArray8;
                    j6 = clientUserId;
                    str3 = str2;
                    chat2 = chat;
                    peer2 = peer;
                    arrayList2 = arrayList17;
                    inputPeer = inputPeer3;
                    arrayList3 = arrayList15;
                    i2 = i3;
                    arrayList4 = arrayList16;
                    arrayList5 = arrayList18;
                    arrayList18 = arrayList5;
                    arrayList17 = arrayList2;
                    arrayList16 = arrayList4;
                    longSparseArray8 = longSparseArray2;
                    arrayList15 = arrayList3;
                } else if (!canSendPolls && (messageObject3.messageOwner.media instanceof TLRPC.TL_messageMediaPoll)) {
                    if (i11 == 0) {
                        i6 = ChatObject.isActionBannedByDefault(chat, 10) ? 6 : 3;
                        longSparseArray = longSparseArray6;
                        j5 = j4;
                        j6 = clientUserId;
                        str3 = str2;
                        chat2 = chat;
                        i11 = i6;
                        peer2 = peer;
                        inputPeer = inputPeer3;
                        i2 = i3;
                    }
                    longSparseArray = longSparseArray6;
                    j5 = j4;
                    longSparseArray2 = longSparseArray8;
                    j6 = clientUserId;
                    str3 = str2;
                    chat2 = chat;
                    peer2 = peer;
                    arrayList2 = arrayList17;
                    inputPeer = inputPeer3;
                    arrayList3 = arrayList15;
                    i2 = i3;
                    arrayList4 = arrayList16;
                    arrayList5 = arrayList18;
                    arrayList18 = arrayList5;
                    arrayList17 = arrayList2;
                    arrayList16 = arrayList4;
                    longSparseArray8 = longSparseArray2;
                    arrayList15 = arrayList3;
                } else if (canSendVoice || !MessageObject.isVoiceMessage(messageObject3.messageOwner)) {
                    if (canSendRoundVideo || !MessageObject.isRoundVideoMessage(messageObject3.messageOwner)) {
                        if (z12 || !(messageObject3.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) || z13) {
                            TLRPC.TL_message tL_message = new TLRPC.TL_message();
                            if (z17) {
                                arrayList6 = arrayList18;
                                j7 = j4;
                                longSparseArray3 = longSparseArray8;
                                str4 = str2;
                                chat3 = chat;
                                longSparseArray4 = longSparseArray6;
                            } else {
                                boolean z21 = messageObject3.getDialogId() == clientUserId && messageObject3.isFromUser() && messageObject3.messageOwner.from_id.user_id == clientUserId;
                                if (messageObject3.isForwarded()) {
                                    TLRPC.TL_messageFwdHeader tL_messageFwdHeader = new TLRPC.TL_messageFwdHeader();
                                    tL_message.fwd_from = tL_messageFwdHeader;
                                    TLRPC.MessageFwdHeader messageFwdHeader2 = messageObject3.messageOwner.fwd_from;
                                    longSparseArray3 = longSparseArray8;
                                    if ((messageFwdHeader2.flags & 1) != 0) {
                                        tL_messageFwdHeader.flags |= 1;
                                        tL_messageFwdHeader.from_id = messageFwdHeader2.from_id;
                                    }
                                    if ((messageFwdHeader2.flags & 32) != 0) {
                                        tL_messageFwdHeader.flags |= 32;
                                        tL_messageFwdHeader.from_name = messageFwdHeader2.from_name;
                                    }
                                    if ((messageFwdHeader2.flags & 4) != 0) {
                                        tL_messageFwdHeader.flags |= 4;
                                        tL_messageFwdHeader.channel_post = messageFwdHeader2.channel_post;
                                    }
                                    if ((messageFwdHeader2.flags & 8) != 0) {
                                        tL_messageFwdHeader.flags |= 8;
                                        tL_messageFwdHeader.post_author = messageFwdHeader2.post_author;
                                    }
                                    if ((j12 == clientUserId || z9) && (messageFwdHeader2.flags & 16) != 0 && !UserObject.isReplyUser(messageObject3.getDialogId())) {
                                        TLRPC.MessageFwdHeader messageFwdHeader3 = tL_message.fwd_from;
                                        messageFwdHeader3.flags |= 16;
                                        TLRPC.MessageFwdHeader messageFwdHeader4 = messageObject3.messageOwner.fwd_from;
                                        messageFwdHeader3.saved_from_peer = messageFwdHeader4.saved_from_peer;
                                        messageFwdHeader3.saved_from_msg_id = messageFwdHeader4.saved_from_msg_id;
                                    }
                                    tL_message.fwd_from.date = messageObject3.messageOwner.fwd_from.date;
                                    tL_message.flags = 4;
                                } else {
                                    longSparseArray3 = longSparseArray8;
                                    if (!z21) {
                                        long fromChatId = messageObject3.getFromChatId();
                                        TLRPC.TL_messageFwdHeader tL_messageFwdHeader2 = new TLRPC.TL_messageFwdHeader();
                                        tL_message.fwd_from = tL_messageFwdHeader2;
                                        arrayList6 = arrayList18;
                                        tL_messageFwdHeader2.channel_post = messageObject3.getId();
                                        tL_message.fwd_from.flags |= 4;
                                        if (messageObject3.isFromUser()) {
                                            TLRPC.MessageFwdHeader messageFwdHeader5 = tL_message.fwd_from;
                                            messageFwdHeader5.from_id = messageObject3.messageOwner.from_id;
                                            messageFwdHeader5.flags |= 1;
                                            j7 = j4;
                                            str4 = str2;
                                            chat3 = chat;
                                            longSparseArray4 = longSparseArray6;
                                        } else {
                                            tL_message.fwd_from.from_id = new TLRPC.TL_peerChannel();
                                            TLRPC.MessageFwdHeader messageFwdHeader6 = tL_message.fwd_from;
                                            TLRPC.Peer peer7 = messageFwdHeader6.from_id;
                                            str4 = str2;
                                            TLRPC.Message message3 = messageObject3.messageOwner;
                                            chat3 = chat;
                                            TLRPC.Peer peer8 = message3.peer_id;
                                            j7 = j4;
                                            longSparseArray4 = longSparseArray6;
                                            peer7.channel_id = peer8.channel_id;
                                            messageFwdHeader6.flags |= 1;
                                            if (message3.post && fromChatId > 0) {
                                                TLRPC.Peer peer9 = message3.from_id;
                                                if (peer9 != null) {
                                                    peer8 = peer9;
                                                }
                                                messageFwdHeader6.from_id = peer8;
                                            }
                                        }
                                        if (messageObject3.messageOwner.post_author == null && !messageObject3.isOutOwner() && fromChatId > 0 && messageObject3.messageOwner.post && (user = getMessagesController().getUser(Long.valueOf(fromChatId))) != null) {
                                            tL_message.fwd_from.post_author = ContactsController.formatName(user.first_name, user.last_name);
                                            tL_message.fwd_from.flags |= 8;
                                        }
                                        tL_message.date = messageObject3.messageOwner.date;
                                        tL_message.flags = 4;
                                        if (j12 == clientUserId && (messageFwdHeader = tL_message.fwd_from) != null) {
                                            messageFwdHeader.flags |= 16;
                                            messageFwdHeader.saved_from_msg_id = messageObject3.getId();
                                            TLRPC.MessageFwdHeader messageFwdHeader7 = tL_message.fwd_from;
                                            peer5 = messageObject3.messageOwner.peer_id;
                                            messageFwdHeader7.saved_from_peer = peer5;
                                            if (peer5.user_id == clientUserId) {
                                                peer5.user_id = messageObject3.getDialogId();
                                            }
                                        }
                                    }
                                }
                                arrayList6 = arrayList18;
                                j7 = j4;
                                str4 = str2;
                                chat3 = chat;
                                longSparseArray4 = longSparseArray6;
                                if (j12 == clientUserId) {
                                    messageFwdHeader.flags |= 16;
                                    messageFwdHeader.saved_from_msg_id = messageObject3.getId();
                                    TLRPC.MessageFwdHeader messageFwdHeader72 = tL_message.fwd_from;
                                    peer5 = messageObject3.messageOwner.peer_id;
                                    messageFwdHeader72.saved_from_peer = peer5;
                                    if (peer5.user_id == clientUserId) {
                                    }
                                }
                            }
                            HashMap<String, String> hashMap = new HashMap<>();
                            tL_message.params = hashMap;
                            hashMap.put("fwd_id", "" + messageObject3.getId());
                            tL_message.params.put("fwd_peer", "" + messageObject3.getDialogId());
                            if (!messageObject3.messageOwner.restriction_reason.isEmpty()) {
                                tL_message.restriction_reason = messageObject3.messageOwner.restriction_reason;
                                tL_message.flags |= 4194304;
                            }
                            tL_message.media = (canSendEmbed || !(messageObject3.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage)) ? messageObject3.messageOwner.media : new TLRPC.TL_messageMediaEmpty();
                            TLRPC.Message message4 = messageObject3.messageOwner;
                            tL_message.invert_media = message4.invert_media;
                            if (tL_message.media != null) {
                                tL_message.flags |= 512;
                            }
                            long j14 = message4.via_bot_id;
                            if (j14 != 0) {
                                tL_message.via_bot_id = j14;
                                tL_message.flags |= 2048;
                            }
                            if (j7 != 0) {
                                TLRPC.TL_messageReplies tL_messageReplies = new TLRPC.TL_messageReplies();
                                tL_message.replies = tL_messageReplies;
                                tL_messageReplies.comments = true;
                                j8 = j7;
                                tL_messageReplies.channel_id = j8;
                                tL_messageReplies.flags |= 1;
                                tL_message.flags |= 8388608;
                            } else {
                                j8 = j7;
                            }
                            if (!z18 || tL_message.media == null) {
                                tL_message.message = messageObject3.messageOwner.message;
                            }
                            if (tL_message.message == null) {
                                tL_message.message = "";
                            }
                            tL_message.fwd_msg_id = messageObject3.getId();
                            TLRPC.Message message5 = messageObject3.messageOwner;
                            tL_message.attachPath = message5.attachPath;
                            tL_message.entities = message5.entities;
                            if (message5.reply_markup instanceof TLRPC.TL_replyInlineMarkup) {
                                tL_message.reply_markup = new TLRPC.TL_replyInlineMarkup();
                                int size = messageObject3.messageOwner.reply_markup.rows.size();
                                int i12 = 0;
                                boolean z22 = false;
                                while (true) {
                                    if (i12 >= size) {
                                        j9 = j8;
                                        break;
                                    }
                                    TLRPC.TL_keyboardButtonRow tL_keyboardButtonRow2 = messageObject3.messageOwner.reply_markup.rows.get(i12);
                                    int size2 = tL_keyboardButtonRow2.buttons.size();
                                    int i13 = size;
                                    int i14 = 0;
                                    TLRPC.TL_keyboardButtonRow tL_keyboardButtonRow3 = null;
                                    while (true) {
                                        boolean z23 = z22;
                                        if (i14 >= size2) {
                                            j9 = j8;
                                            break;
                                        }
                                        TLRPC.KeyboardButton keyboardButton = tL_keyboardButtonRow2.buttons.get(i14);
                                        j9 = j8;
                                        boolean z24 = keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth;
                                        if (!z24 && !(keyboardButton instanceof TLRPC.TL_keyboardButtonUrl) && !(keyboardButton instanceof TLRPC.TL_keyboardButtonSwitchInline) && !(keyboardButton instanceof TLRPC.TL_keyboardButtonBuy)) {
                                            z22 = true;
                                            break;
                                        }
                                        if (z24) {
                                            TLRPC.TL_keyboardButtonUrlAuth tL_keyboardButtonUrlAuth = new TLRPC.TL_keyboardButtonUrlAuth();
                                            tL_keyboardButtonUrlAuth.flags = keyboardButton.flags;
                                            String str6 = keyboardButton.fwd_text;
                                            if (str6 != null) {
                                                tL_keyboardButtonUrlAuth.fwd_text = str6;
                                            } else {
                                                str6 = keyboardButton.text;
                                            }
                                            tL_keyboardButtonUrlAuth.text = str6;
                                            tL_keyboardButtonUrlAuth.url = keyboardButton.url;
                                            tL_keyboardButtonUrlAuth.button_id = keyboardButton.button_id;
                                            keyboardButton = tL_keyboardButtonUrlAuth;
                                        }
                                        if (tL_keyboardButtonRow3 == null) {
                                            tL_keyboardButtonRow = new TLRPC.TL_keyboardButtonRow();
                                            tL_message.reply_markup.rows.add(tL_keyboardButtonRow);
                                        } else {
                                            tL_keyboardButtonRow = tL_keyboardButtonRow3;
                                        }
                                        tL_keyboardButtonRow.buttons.add(keyboardButton);
                                        i14++;
                                        tL_keyboardButtonRow3 = tL_keyboardButtonRow;
                                        z22 = z23;
                                        j8 = j9;
                                    }
                                    if (z22) {
                                        break;
                                    }
                                    i12++;
                                    size = i13;
                                    j8 = j9;
                                }
                                if (z22) {
                                    messageObject3.messageOwner.reply_markup = null;
                                    tL_message.flags &= -65;
                                    if (!tL_message.entities.isEmpty()) {
                                        tL_message.flags |= 128;
                                    }
                                    if (tL_message.attachPath == null) {
                                        tL_message.attachPath = "";
                                    }
                                    int newMessageId = getUserConfig().getNewMessageId();
                                    tL_message.id = newMessageId;
                                    tL_message.local_id = newMessageId;
                                    tL_message.out = true;
                                    j10 = messageObject3.messageOwner.grouped_id;
                                    if (j10 != 0) {
                                        Long l = (Long) longSparseArray4.get(j10);
                                        if (l == null) {
                                            l = Long.valueOf(Utilities.random.nextLong());
                                            longSparseArray4.put(messageObject3.messageOwner.grouped_id, l);
                                        }
                                        tL_message.grouped_id = l.longValue();
                                        tL_message.flags |= 131072;
                                    }
                                    peer3 = peer;
                                    if (peer3.channel_id == 0 && z9) {
                                        if (z8) {
                                            TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
                                            tL_message.from_id = tL_peerUser;
                                            tL_peerUser.user_id = clientUserId;
                                        } else {
                                            tL_message.from_id = peer3;
                                        }
                                        tL_message.post = true;
                                        str5 = str4;
                                        chat4 = chat3;
                                    } else {
                                        chat4 = chat3;
                                        sendAsPeerId = ChatObject.getSendAsPeerId(chat4, getMessagesController().getChatFull(-j12), true);
                                        if (sendAsPeerId != clientUserId) {
                                            TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
                                            tL_message.from_id = tL_peerUser2;
                                            tL_peerUser2.user_id = clientUserId;
                                            tL_message.flags |= 256;
                                            str5 = str4;
                                        } else {
                                            tL_message.from_id = getMessagesController().getPeer(sendAsPeerId);
                                            str5 = str4;
                                            if (str4 != null) {
                                                tL_message.post_author = str5;
                                                tL_message.flags |= 65536;
                                            }
                                        }
                                    }
                                    if (tL_message.random_id == 0) {
                                        tL_message.random_id = getNextRandomId();
                                    }
                                    ArrayList<Long> arrayList19 = arrayList6;
                                    arrayList19.add(Long.valueOf(tL_message.random_id));
                                    LongSparseArray longSparseArray9 = longSparseArray3;
                                    longSparseArray9.put(tL_message.random_id, tL_message);
                                    ArrayList<Integer> arrayList20 = arrayList15;
                                    arrayList20.add(Integer.valueOf(tL_message.fwd_msg_id));
                                    chat2 = chat4;
                                    int i15 = i3;
                                    tL_message.date = i == 0 ? i : getConnectionsManager().getCurrentTime();
                                    TLRPC.InputPeer inputPeer4 = inputPeer3;
                                    z14 = inputPeer4 instanceof TLRPC.TL_inputPeerChannel;
                                    if (z14 || !z9) {
                                        longSparseArray5 = longSparseArray4;
                                        message = messageObject3.messageOwner;
                                        j11 = clientUserId;
                                        if ((message.flags & 1024) != 0 && i == 0) {
                                            tL_message.views = message.views;
                                            tL_message.flags |= 1024;
                                        }
                                        tL_message.unread = true;
                                    } else {
                                        longSparseArray5 = longSparseArray4;
                                        if (i == 0) {
                                            tL_message.views = 1;
                                            tL_message.flags |= 1024;
                                        }
                                        j11 = clientUserId;
                                    }
                                    tL_message.dialog_id = j12;
                                    tL_message.peer_id = peer3;
                                    if (!MessageObject.isVoiceMessage(tL_message) || MessageObject.isRoundVideoMessage(tL_message)) {
                                        tL_message.media_unread = (z14 || messageObject3.getChannelId() == 0) ? true : messageObject3.isContentUnread();
                                    }
                                    if (messageObject == null && (messageReplyHeader = (message2 = messageObject3.messageOwner).reply_to) != null) {
                                        peer4 = messageReplyHeader.reply_to_peer_id;
                                        if (peer4 != null || MessageObject.peersEqual(peer4, message2.peer_id)) {
                                            messageReplyHeader2 = messageObject3.messageOwner.reply_to;
                                            if ((messageReplyHeader2.flags & 16) != 0) {
                                            }
                                        }
                                        tL_message.flags |= 8;
                                        tL_message.reply_to = messageObject3.messageOwner.reply_to;
                                    }
                                    ArrayList<Integer> arrayList21 = arrayList20;
                                    MessageObject messageObject4 = new MessageObject(this.currentAccount, tL_message, true, true);
                                    messageObject4.scheduled = i == 0;
                                    messageObject4.messageOwner.send_state = 1;
                                    messageObject4.wasJustSent = true;
                                    ArrayList<MessageObject> arrayList22 = arrayList17;
                                    arrayList22.add(messageObject4);
                                    arrayList7 = arrayList16;
                                    arrayList7.add(tL_message);
                                    String str7 = str5;
                                    if (messageObject3.replyMessageObject != null) {
                                        int i16 = 0;
                                        while (i16 < arrayList.size()) {
                                            arrayList8 = arrayList;
                                            arrayList10 = arrayList21;
                                            arrayList9 = arrayList19;
                                            if (arrayList8.get(i16).getId() == messageObject3.replyMessageObject.getId()) {
                                                TLRPC.Message message6 = messageObject4.messageOwner;
                                                MessageObject messageObject5 = messageObject3.replyMessageObject;
                                                message6.replyMessage = messageObject5.messageOwner;
                                                messageObject4.replyMessageObject = messageObject5;
                                                break;
                                            }
                                            i16++;
                                            arrayList21 = arrayList10;
                                            arrayList19 = arrayList9;
                                        }
                                    }
                                    arrayList8 = arrayList;
                                    arrayList9 = arrayList19;
                                    arrayList10 = arrayList21;
                                    putToSendingMessages(tL_message, i == 0);
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("forward message user_id = " + inputPeer4.user_id + " chat_id = " + inputPeer4.chat_id + " channel_id = " + inputPeer4.channel_id + " access_hash = " + inputPeer4.access_hash);
                                    }
                                    if (messageObject != null) {
                                        TLRPC.TL_messageReplyHeader tL_messageReplyHeader = new TLRPC.TL_messageReplyHeader();
                                        tL_message.reply_to = tL_messageReplyHeader;
                                        tL_messageReplyHeader.flags |= 16;
                                        tL_messageReplyHeader.reply_to_msg_id = messageObject.getId();
                                        if (messageObject.isTopicMainMessage) {
                                            TLRPC.MessageReplyHeader messageReplyHeader3 = tL_message.reply_to;
                                            i4 = 1;
                                            messageReplyHeader3.forum_topic = true;
                                            messageReplyHeader3.flags |= 8;
                                            if (arrayList7.size() == 100 && i15 != arrayList.size() - i4 && (i15 == arrayList.size() - i4 || arrayList8.get(i15 + 1).getDialogId() == messageObject3.getDialogId())) {
                                                inputPeer = inputPeer4;
                                                arrayList2 = arrayList22;
                                                arrayList4 = arrayList7;
                                                i2 = i15;
                                                longSparseArray = longSparseArray5;
                                                j6 = j11;
                                                longSparseArray2 = longSparseArray9;
                                                arrayList3 = arrayList10;
                                                arrayList5 = arrayList9;
                                                str3 = str7;
                                                j5 = j9;
                                                peer2 = peer3;
                                                arrayList18 = arrayList5;
                                                arrayList17 = arrayList2;
                                                arrayList16 = arrayList4;
                                                longSparseArray8 = longSparseArray2;
                                                arrayList15 = arrayList3;
                                            } else {
                                                getMessagesStorage().putMessages(new ArrayList<>(arrayList7), false, true, false, 0, i != 0 ? 1 : 0, 0L);
                                                getMessagesController().updateInterfaceWithMessages(j12, arrayList22, i != 0 ? 1 : 0);
                                                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
                                                getUserConfig().saveConfig(false);
                                                final TLRPC.TL_messages_forwardMessages tL_messages_forwardMessages = new TLRPC.TL_messages_forwardMessages();
                                                tL_messages_forwardMessages.to_peer = inputPeer4;
                                                if (z3) {
                                                    if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + j12, false)) {
                                                        z15 = false;
                                                        tL_messages_forwardMessages.silent = z15;
                                                        if (messageObject != null) {
                                                            tL_messages_forwardMessages.top_msg_id = messageObject.getId();
                                                            tL_messages_forwardMessages.flags |= 512;
                                                        }
                                                        if (i != 0) {
                                                            tL_messages_forwardMessages.schedule_date = i;
                                                            tL_messages_forwardMessages.flags |= 1024;
                                                        }
                                                        if (messageObject3.messageOwner.peer_id instanceof TLRPC.TL_peerChannel) {
                                                            i5 = i15;
                                                            tL_messages_forwardMessages.from_peer = new TLRPC.TL_inputPeerEmpty();
                                                        } else {
                                                            TLRPC.Chat chat6 = getMessagesController().getChat(Long.valueOf(messageObject3.messageOwner.peer_id.channel_id));
                                                            TLRPC.TL_inputPeerChannel tL_inputPeerChannel = new TLRPC.TL_inputPeerChannel();
                                                            tL_messages_forwardMessages.from_peer = tL_inputPeerChannel;
                                                            i5 = i15;
                                                            tL_inputPeerChannel.channel_id = messageObject3.messageOwner.peer_id.channel_id;
                                                            if (chat6 != null) {
                                                                tL_inputPeerChannel.access_hash = chat6.access_hash;
                                                            }
                                                        }
                                                        ArrayList<Long> arrayList23 = arrayList9;
                                                        tL_messages_forwardMessages.random_id = arrayList23;
                                                        ArrayList<Integer> arrayList24 = arrayList10;
                                                        tL_messages_forwardMessages.id = arrayList24;
                                                        tL_messages_forwardMessages.drop_author = z;
                                                        tL_messages_forwardMessages.drop_media_captions = z2;
                                                        if (arrayList.size() == 1 && arrayList8.get(0).messageOwner.with_my_score) {
                                                            z16 = true;
                                                            tL_messages_forwardMessages.with_my_score = z16;
                                                            final ArrayList arrayList25 = new ArrayList(arrayList22);
                                                            arrayList5 = arrayList23;
                                                            arrayList2 = arrayList22;
                                                            longSparseArray = longSparseArray5;
                                                            arrayList4 = arrayList7;
                                                            j5 = j9;
                                                            peer2 = peer3;
                                                            inputPeer = inputPeer4;
                                                            longSparseArray2 = longSparseArray9;
                                                            arrayList3 = arrayList24;
                                                            j6 = j11;
                                                            final boolean z25 = i != 2147483646;
                                                            final boolean z26 = z20;
                                                            str3 = str7;
                                                            getConnectionsManager().sendRequest(tL_messages_forwardMessages, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda3
                                                                @Override // org.telegram.tgnet.RequestDelegate
                                                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                                                    SendMessagesHelper.this.lambda$sendMessage$14(j, i, z25, z26, longSparseArray2, arrayList4, arrayList25, messageObject3, peer2, tL_messages_forwardMessages, tLObject, tL_error);
                                                                }
                                                            }, 68);
                                                            i2 = i5;
                                                            if (i2 != arrayList.size() - 1) {
                                                                arrayList17 = new ArrayList<>();
                                                                arrayList16 = new ArrayList();
                                                                arrayList18 = new ArrayList<>();
                                                                arrayList15 = new ArrayList<>();
                                                                longSparseArray8 = new LongSparseArray();
                                                            }
                                                            arrayList18 = arrayList5;
                                                            arrayList17 = arrayList2;
                                                            arrayList16 = arrayList4;
                                                            longSparseArray8 = longSparseArray2;
                                                            arrayList15 = arrayList3;
                                                        }
                                                        z16 = false;
                                                        tL_messages_forwardMessages.with_my_score = z16;
                                                        final ArrayList arrayList252 = new ArrayList(arrayList22);
                                                        arrayList5 = arrayList23;
                                                        arrayList2 = arrayList22;
                                                        longSparseArray = longSparseArray5;
                                                        arrayList4 = arrayList7;
                                                        j5 = j9;
                                                        peer2 = peer3;
                                                        inputPeer = inputPeer4;
                                                        longSparseArray2 = longSparseArray9;
                                                        arrayList3 = arrayList24;
                                                        j6 = j11;
                                                        final boolean z252 = i != 2147483646;
                                                        final boolean z262 = z20;
                                                        str3 = str7;
                                                        getConnectionsManager().sendRequest(tL_messages_forwardMessages, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda3
                                                            @Override // org.telegram.tgnet.RequestDelegate
                                                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                                                SendMessagesHelper.this.lambda$sendMessage$14(j, i, z252, z262, longSparseArray2, arrayList4, arrayList252, messageObject3, peer2, tL_messages_forwardMessages, tLObject, tL_error);
                                                            }
                                                        }, 68);
                                                        i2 = i5;
                                                        if (i2 != arrayList.size() - 1) {
                                                        }
                                                        arrayList18 = arrayList5;
                                                        arrayList17 = arrayList2;
                                                        arrayList16 = arrayList4;
                                                        longSparseArray8 = longSparseArray2;
                                                        arrayList15 = arrayList3;
                                                    }
                                                }
                                                z15 = true;
                                                tL_messages_forwardMessages.silent = z15;
                                                if (messageObject != null) {
                                                }
                                                if (i != 0) {
                                                }
                                                if (messageObject3.messageOwner.peer_id instanceof TLRPC.TL_peerChannel) {
                                                }
                                                ArrayList<Long> arrayList232 = arrayList9;
                                                tL_messages_forwardMessages.random_id = arrayList232;
                                                ArrayList<Integer> arrayList242 = arrayList10;
                                                tL_messages_forwardMessages.id = arrayList242;
                                                tL_messages_forwardMessages.drop_author = z;
                                                tL_messages_forwardMessages.drop_media_captions = z2;
                                                if (arrayList.size() == 1) {
                                                    z16 = true;
                                                    tL_messages_forwardMessages.with_my_score = z16;
                                                    final ArrayList arrayList2522 = new ArrayList(arrayList22);
                                                    arrayList5 = arrayList232;
                                                    arrayList2 = arrayList22;
                                                    longSparseArray = longSparseArray5;
                                                    arrayList4 = arrayList7;
                                                    j5 = j9;
                                                    peer2 = peer3;
                                                    inputPeer = inputPeer4;
                                                    longSparseArray2 = longSparseArray9;
                                                    arrayList3 = arrayList242;
                                                    j6 = j11;
                                                    final boolean z2522 = i != 2147483646;
                                                    final boolean z2622 = z20;
                                                    str3 = str7;
                                                    getConnectionsManager().sendRequest(tL_messages_forwardMessages, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda3
                                                        @Override // org.telegram.tgnet.RequestDelegate
                                                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                                            SendMessagesHelper.this.lambda$sendMessage$14(j, i, z2522, z2622, longSparseArray2, arrayList4, arrayList2522, messageObject3, peer2, tL_messages_forwardMessages, tLObject, tL_error);
                                                        }
                                                    }, 68);
                                                    i2 = i5;
                                                    if (i2 != arrayList.size() - 1) {
                                                    }
                                                    arrayList18 = arrayList5;
                                                    arrayList17 = arrayList2;
                                                    arrayList16 = arrayList4;
                                                    longSparseArray8 = longSparseArray2;
                                                    arrayList15 = arrayList3;
                                                }
                                                z16 = false;
                                                tL_messages_forwardMessages.with_my_score = z16;
                                                final ArrayList arrayList25222 = new ArrayList(arrayList22);
                                                arrayList5 = arrayList232;
                                                arrayList2 = arrayList22;
                                                longSparseArray = longSparseArray5;
                                                arrayList4 = arrayList7;
                                                j5 = j9;
                                                peer2 = peer3;
                                                inputPeer = inputPeer4;
                                                longSparseArray2 = longSparseArray9;
                                                arrayList3 = arrayList242;
                                                j6 = j11;
                                                final boolean z25222 = i != 2147483646;
                                                final boolean z26222 = z20;
                                                str3 = str7;
                                                getConnectionsManager().sendRequest(tL_messages_forwardMessages, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda3
                                                    @Override // org.telegram.tgnet.RequestDelegate
                                                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                                        SendMessagesHelper.this.lambda$sendMessage$14(j, i, z25222, z26222, longSparseArray2, arrayList4, arrayList25222, messageObject3, peer2, tL_messages_forwardMessages, tLObject, tL_error);
                                                    }
                                                }, 68);
                                                i2 = i5;
                                                if (i2 != arrayList.size() - 1) {
                                                }
                                                arrayList18 = arrayList5;
                                                arrayList17 = arrayList2;
                                                arrayList16 = arrayList4;
                                                longSparseArray8 = longSparseArray2;
                                                arrayList15 = arrayList3;
                                            }
                                        }
                                    }
                                    i4 = 1;
                                    if (arrayList7.size() == 100) {
                                    }
                                    getMessagesStorage().putMessages(new ArrayList<>(arrayList7), false, true, false, 0, i != 0 ? 1 : 0, 0L);
                                    getMessagesController().updateInterfaceWithMessages(j12, arrayList22, i != 0 ? 1 : 0);
                                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
                                    getUserConfig().saveConfig(false);
                                    final TLRPC.TL_messages_forwardMessages tL_messages_forwardMessages2 = new TLRPC.TL_messages_forwardMessages();
                                    tL_messages_forwardMessages2.to_peer = inputPeer4;
                                    if (z3) {
                                    }
                                    z15 = true;
                                    tL_messages_forwardMessages2.silent = z15;
                                    if (messageObject != null) {
                                    }
                                    if (i != 0) {
                                    }
                                    if (messageObject3.messageOwner.peer_id instanceof TLRPC.TL_peerChannel) {
                                    }
                                    ArrayList<Long> arrayList2322 = arrayList9;
                                    tL_messages_forwardMessages2.random_id = arrayList2322;
                                    ArrayList<Integer> arrayList2422 = arrayList10;
                                    tL_messages_forwardMessages2.id = arrayList2422;
                                    tL_messages_forwardMessages2.drop_author = z;
                                    tL_messages_forwardMessages2.drop_media_captions = z2;
                                    if (arrayList.size() == 1) {
                                    }
                                    z16 = false;
                                    tL_messages_forwardMessages2.with_my_score = z16;
                                    final ArrayList arrayList252222 = new ArrayList(arrayList22);
                                    arrayList5 = arrayList2322;
                                    arrayList2 = arrayList22;
                                    longSparseArray = longSparseArray5;
                                    arrayList4 = arrayList7;
                                    j5 = j9;
                                    peer2 = peer3;
                                    inputPeer = inputPeer4;
                                    longSparseArray2 = longSparseArray9;
                                    arrayList3 = arrayList2422;
                                    j6 = j11;
                                    final boolean z252222 = i != 2147483646;
                                    final boolean z262222 = z20;
                                    str3 = str7;
                                    getConnectionsManager().sendRequest(tL_messages_forwardMessages2, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda3
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                            SendMessagesHelper.this.lambda$sendMessage$14(j, i, z252222, z262222, longSparseArray2, arrayList4, arrayList252222, messageObject3, peer2, tL_messages_forwardMessages2, tLObject, tL_error);
                                        }
                                    }, 68);
                                    i2 = i5;
                                    if (i2 != arrayList.size() - 1) {
                                    }
                                    arrayList18 = arrayList5;
                                    arrayList17 = arrayList2;
                                    arrayList16 = arrayList4;
                                    longSparseArray8 = longSparseArray2;
                                    arrayList15 = arrayList3;
                                } else {
                                    tL_message.flags |= 64;
                                }
                            } else {
                                j9 = j8;
                            }
                            if (!tL_message.entities.isEmpty()) {
                            }
                            if (tL_message.attachPath == null) {
                            }
                            int newMessageId2 = getUserConfig().getNewMessageId();
                            tL_message.id = newMessageId2;
                            tL_message.local_id = newMessageId2;
                            tL_message.out = true;
                            j10 = messageObject3.messageOwner.grouped_id;
                            if (j10 != 0) {
                            }
                            peer3 = peer;
                            if (peer3.channel_id == 0) {
                            }
                            chat4 = chat3;
                            sendAsPeerId = ChatObject.getSendAsPeerId(chat4, getMessagesController().getChatFull(-j12), true);
                            if (sendAsPeerId != clientUserId) {
                            }
                            if (tL_message.random_id == 0) {
                            }
                            ArrayList<Long> arrayList192 = arrayList6;
                            arrayList192.add(Long.valueOf(tL_message.random_id));
                            LongSparseArray longSparseArray92 = longSparseArray3;
                            longSparseArray92.put(tL_message.random_id, tL_message);
                            ArrayList<Integer> arrayList202 = arrayList15;
                            arrayList202.add(Integer.valueOf(tL_message.fwd_msg_id));
                            chat2 = chat4;
                            int i152 = i3;
                            tL_message.date = i == 0 ? i : getConnectionsManager().getCurrentTime();
                            TLRPC.InputPeer inputPeer42 = inputPeer3;
                            z14 = inputPeer42 instanceof TLRPC.TL_inputPeerChannel;
                            if (z14) {
                            }
                            longSparseArray5 = longSparseArray4;
                            message = messageObject3.messageOwner;
                            j11 = clientUserId;
                            if ((message.flags & 1024) != 0) {
                                tL_message.views = message.views;
                                tL_message.flags |= 1024;
                            }
                            tL_message.unread = true;
                            tL_message.dialog_id = j12;
                            tL_message.peer_id = peer3;
                            if (!MessageObject.isVoiceMessage(tL_message)) {
                            }
                            tL_message.media_unread = (z14 || messageObject3.getChannelId() == 0) ? true : messageObject3.isContentUnread();
                            if (messageObject == null) {
                                peer4 = messageReplyHeader.reply_to_peer_id;
                                if (peer4 != null) {
                                }
                                messageReplyHeader2 = messageObject3.messageOwner.reply_to;
                                if ((messageReplyHeader2.flags & 16) != 0) {
                                }
                            }
                            ArrayList<Integer> arrayList212 = arrayList202;
                            MessageObject messageObject42 = new MessageObject(this.currentAccount, tL_message, true, true);
                            messageObject42.scheduled = i == 0;
                            messageObject42.messageOwner.send_state = 1;
                            messageObject42.wasJustSent = true;
                            ArrayList<MessageObject> arrayList222 = arrayList17;
                            arrayList222.add(messageObject42);
                            arrayList7 = arrayList16;
                            arrayList7.add(tL_message);
                            String str72 = str5;
                            if (messageObject3.replyMessageObject != null) {
                            }
                            arrayList8 = arrayList;
                            arrayList9 = arrayList192;
                            arrayList10 = arrayList212;
                            putToSendingMessages(tL_message, i == 0);
                            if (BuildVars.LOGS_ENABLED) {
                            }
                            if (messageObject != null) {
                            }
                            i4 = 1;
                            if (arrayList7.size() == 100) {
                            }
                            getMessagesStorage().putMessages(new ArrayList<>(arrayList7), false, true, false, 0, i != 0 ? 1 : 0, 0L);
                            getMessagesController().updateInterfaceWithMessages(j12, arrayList222, i != 0 ? 1 : 0);
                            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
                            getUserConfig().saveConfig(false);
                            final TLRPC.TL_messages_forwardMessages tL_messages_forwardMessages22 = new TLRPC.TL_messages_forwardMessages();
                            tL_messages_forwardMessages22.to_peer = inputPeer42;
                            if (z3) {
                            }
                            z15 = true;
                            tL_messages_forwardMessages22.silent = z15;
                            if (messageObject != null) {
                            }
                            if (i != 0) {
                            }
                            if (messageObject3.messageOwner.peer_id instanceof TLRPC.TL_peerChannel) {
                            }
                            ArrayList<Long> arrayList23222 = arrayList9;
                            tL_messages_forwardMessages22.random_id = arrayList23222;
                            ArrayList<Integer> arrayList24222 = arrayList10;
                            tL_messages_forwardMessages22.id = arrayList24222;
                            tL_messages_forwardMessages22.drop_author = z;
                            tL_messages_forwardMessages22.drop_media_captions = z2;
                            if (arrayList.size() == 1) {
                            }
                            z16 = false;
                            tL_messages_forwardMessages22.with_my_score = z16;
                            final ArrayList arrayList2522222 = new ArrayList(arrayList222);
                            arrayList5 = arrayList23222;
                            arrayList2 = arrayList222;
                            longSparseArray = longSparseArray5;
                            arrayList4 = arrayList7;
                            j5 = j9;
                            peer2 = peer3;
                            inputPeer = inputPeer42;
                            longSparseArray2 = longSparseArray92;
                            arrayList3 = arrayList24222;
                            j6 = j11;
                            final boolean z2522222 = i != 2147483646;
                            final boolean z2622222 = z20;
                            str3 = str72;
                            getConnectionsManager().sendRequest(tL_messages_forwardMessages22, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda3
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    SendMessagesHelper.this.lambda$sendMessage$14(j, i, z2522222, z2622222, longSparseArray2, arrayList4, arrayList2522222, messageObject3, peer2, tL_messages_forwardMessages22, tLObject, tL_error);
                                }
                            }, 68);
                            i2 = i5;
                            if (i2 != arrayList.size() - 1) {
                            }
                            arrayList18 = arrayList5;
                            arrayList17 = arrayList2;
                            arrayList16 = arrayList4;
                            longSparseArray8 = longSparseArray2;
                            arrayList15 = arrayList3;
                        } else {
                            if (i11 == 0) {
                                i6 = ChatObject.isActionBannedByDefault(chat, 19) ? 17 : 18;
                                longSparseArray = longSparseArray6;
                                j5 = j4;
                                j6 = clientUserId;
                                str3 = str2;
                                chat2 = chat;
                                i11 = i6;
                                peer2 = peer;
                                inputPeer = inputPeer3;
                                i2 = i3;
                            }
                            longSparseArray = longSparseArray6;
                            j5 = j4;
                            longSparseArray2 = longSparseArray8;
                            j6 = clientUserId;
                            str3 = str2;
                            chat2 = chat;
                            peer2 = peer;
                            arrayList2 = arrayList17;
                            inputPeer = inputPeer3;
                            arrayList3 = arrayList15;
                            i2 = i3;
                            arrayList4 = arrayList16;
                            arrayList5 = arrayList18;
                            arrayList18 = arrayList5;
                            arrayList17 = arrayList2;
                            arrayList16 = arrayList4;
                            longSparseArray8 = longSparseArray2;
                            arrayList15 = arrayList3;
                        }
                    } else if (chat != null) {
                        if (i11 == 0) {
                            i6 = ChatObject.isActionBannedByDefault(chat, 21) ? 15 : 16;
                            longSparseArray = longSparseArray6;
                            j5 = j4;
                            j6 = clientUserId;
                            str3 = str2;
                            chat2 = chat;
                            i11 = i6;
                            peer2 = peer;
                            inputPeer = inputPeer3;
                            i2 = i3;
                        }
                        longSparseArray = longSparseArray6;
                        j5 = j4;
                        longSparseArray2 = longSparseArray8;
                        j6 = clientUserId;
                        str3 = str2;
                        chat2 = chat;
                        peer2 = peer;
                        arrayList2 = arrayList17;
                        inputPeer = inputPeer3;
                        arrayList3 = arrayList15;
                        i2 = i3;
                        arrayList4 = arrayList16;
                        arrayList5 = arrayList18;
                        arrayList18 = arrayList5;
                        arrayList17 = arrayList2;
                        arrayList16 = arrayList4;
                        longSparseArray8 = longSparseArray2;
                        arrayList15 = arrayList3;
                    } else {
                        if (i11 == 0) {
                            i6 = 8;
                            longSparseArray = longSparseArray6;
                            j5 = j4;
                            j6 = clientUserId;
                            str3 = str2;
                            chat2 = chat;
                            i11 = i6;
                            peer2 = peer;
                            inputPeer = inputPeer3;
                            i2 = i3;
                        }
                        longSparseArray = longSparseArray6;
                        j5 = j4;
                        longSparseArray2 = longSparseArray8;
                        j6 = clientUserId;
                        str3 = str2;
                        chat2 = chat;
                        peer2 = peer;
                        arrayList2 = arrayList17;
                        inputPeer = inputPeer3;
                        arrayList3 = arrayList15;
                        i2 = i3;
                        arrayList4 = arrayList16;
                        arrayList5 = arrayList18;
                        arrayList18 = arrayList5;
                        arrayList17 = arrayList2;
                        arrayList16 = arrayList4;
                        longSparseArray8 = longSparseArray2;
                        arrayList15 = arrayList3;
                    }
                } else if (chat != null) {
                    if (i11 == 0) {
                        i6 = ChatObject.isActionBannedByDefault(chat, 20) ? 13 : 14;
                        longSparseArray = longSparseArray6;
                        j5 = j4;
                        j6 = clientUserId;
                        str3 = str2;
                        chat2 = chat;
                        i11 = i6;
                        peer2 = peer;
                        inputPeer = inputPeer3;
                        i2 = i3;
                    }
                    longSparseArray = longSparseArray6;
                    j5 = j4;
                    longSparseArray2 = longSparseArray8;
                    j6 = clientUserId;
                    str3 = str2;
                    chat2 = chat;
                    peer2 = peer;
                    arrayList2 = arrayList17;
                    inputPeer = inputPeer3;
                    arrayList3 = arrayList15;
                    i2 = i3;
                    arrayList4 = arrayList16;
                    arrayList5 = arrayList18;
                    arrayList18 = arrayList5;
                    arrayList17 = arrayList2;
                    arrayList16 = arrayList4;
                    longSparseArray8 = longSparseArray2;
                    arrayList15 = arrayList3;
                } else {
                    if (i11 == 0) {
                        i6 = 7;
                        longSparseArray = longSparseArray6;
                        j5 = j4;
                        j6 = clientUserId;
                        str3 = str2;
                        chat2 = chat;
                        i11 = i6;
                        peer2 = peer;
                        inputPeer = inputPeer3;
                        i2 = i3;
                    }
                    longSparseArray = longSparseArray6;
                    j5 = j4;
                    longSparseArray2 = longSparseArray8;
                    j6 = clientUserId;
                    str3 = str2;
                    chat2 = chat;
                    peer2 = peer;
                    arrayList2 = arrayList17;
                    inputPeer = inputPeer3;
                    arrayList3 = arrayList15;
                    i2 = i3;
                    arrayList4 = arrayList16;
                    arrayList5 = arrayList18;
                    arrayList18 = arrayList5;
                    arrayList17 = arrayList2;
                    arrayList16 = arrayList4;
                    longSparseArray8 = longSparseArray2;
                    arrayList15 = arrayList3;
                }
            }
            i10 = i2 + 1;
            arrayList11 = arrayList;
            j12 = j;
            z17 = z;
            z18 = z2;
            chat = chat2;
            peer = peer2;
            longSparseArray6 = longSparseArray;
            j4 = j5;
            inputPeer3 = inputPeer;
            clientUserId = j6;
            str2 = str3;
        }
        return i11;
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:77)
        */
    public void sendMessage(org.telegram.messenger.SendMessagesHelper.SendMessageParams r103) {
        /*
            Method dump skipped, instructions count: 9170
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SendMessagesHelper.sendMessage(org.telegram.messenger.SendMessagesHelper$SendMessageParams):void");
    }

    public void sendNotificationCallback(final long j, final int i, final byte[] bArr) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                SendMessagesHelper.this.lambda$sendNotificationCallback$19(j, i, bArr);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void sendReaction(MessageObject messageObject, ArrayList<ReactionsLayoutInBubble.VisibleReaction> arrayList, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2, BaseFragment baseFragment, final Runnable runnable) {
        int id;
        TLRPC.TL_reactionEmoji tL_reactionEmoji;
        if (messageObject == null || baseFragment == null) {
            return;
        }
        TLRPC.TL_messages_sendReaction tL_messages_sendReaction = new TLRPC.TL_messages_sendReaction();
        TLRPC.Message message = messageObject.messageOwner;
        if (!message.isThreadMessage || message.fwd_from == null) {
            tL_messages_sendReaction.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
            id = messageObject.getId();
        } else {
            tL_messages_sendReaction.peer = getMessagesController().getInputPeer(messageObject.getFromChatId());
            id = messageObject.messageOwner.fwd_from.saved_from_msg_id;
        }
        tL_messages_sendReaction.msg_id = id;
        tL_messages_sendReaction.add_to_recent = z2;
        if (z2 && visibleReaction != null) {
            MediaDataController.getInstance(this.currentAccount).recentReactions.add(0, ReactionsUtils.toTLReaction(visibleReaction));
        }
        if (arrayList != null && !arrayList.isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                ReactionsLayoutInBubble.VisibleReaction visibleReaction2 = arrayList.get(i);
                if (visibleReaction2.documentId != 0) {
                    TLRPC.TL_reactionCustomEmoji tL_reactionCustomEmoji = new TLRPC.TL_reactionCustomEmoji();
                    tL_reactionCustomEmoji.document_id = visibleReaction2.documentId;
                    tL_reactionEmoji = tL_reactionCustomEmoji;
                } else if (visibleReaction2.emojicon != null) {
                    TLRPC.TL_reactionEmoji tL_reactionEmoji2 = new TLRPC.TL_reactionEmoji();
                    tL_reactionEmoji2.emoticon = visibleReaction2.emojicon;
                    tL_reactionEmoji = tL_reactionEmoji2;
                }
                tL_messages_sendReaction.reaction.add(tL_reactionEmoji);
                tL_messages_sendReaction.flags |= 1;
            }
        }
        if (z) {
            tL_messages_sendReaction.flags |= 2;
            tL_messages_sendReaction.big = true;
        }
        getConnectionsManager().sendRequest(tL_messages_sendReaction, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda91
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.lambda$sendReaction$22(runnable, tLObject, tL_error);
            }
        });
    }

    public void sendScreenshotMessage(TLRPC.User user, int i, TLRPC.Message message) {
        TLRPC.Message message2 = message;
        if (user == null || i == 0 || user.id == getUserConfig().getClientUserId()) {
            return;
        }
        TLRPC.TL_messages_sendScreenshotNotification tL_messages_sendScreenshotNotification = new TLRPC.TL_messages_sendScreenshotNotification();
        TLRPC.TL_inputPeerUser tL_inputPeerUser = new TLRPC.TL_inputPeerUser();
        tL_messages_sendScreenshotNotification.peer = tL_inputPeerUser;
        tL_inputPeerUser.access_hash = user.access_hash;
        tL_inputPeerUser.user_id = user.id;
        if (message2 != null) {
            tL_messages_sendScreenshotNotification.reply_to = createReplyInput(i);
            tL_messages_sendScreenshotNotification.random_id = message2.random_id;
        } else {
            message2 = new TLRPC.TL_messageService();
            message2.random_id = getNextRandomId();
            message2.dialog_id = user.id;
            message2.unread = true;
            message2.out = true;
            int newMessageId = getUserConfig().getNewMessageId();
            message2.id = newMessageId;
            message2.local_id = newMessageId;
            TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
            message2.from_id = tL_peerUser;
            tL_peerUser.user_id = getUserConfig().getClientUserId();
            message2.flags |= NotificationCenter.webRtcMicAmplitudeEvent;
            TLRPC.TL_messageReplyHeader tL_messageReplyHeader = new TLRPC.TL_messageReplyHeader();
            message2.reply_to = tL_messageReplyHeader;
            tL_messageReplyHeader.flags |= 16;
            tL_messageReplyHeader.reply_to_msg_id = i;
            TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
            message2.peer_id = tL_peerUser2;
            tL_peerUser2.user_id = user.id;
            message2.date = getConnectionsManager().getCurrentTime();
            message2.action = new TLRPC.TL_messageActionScreenshotTaken();
            getUserConfig().saveConfig(false);
        }
        tL_messages_sendScreenshotNotification.random_id = message2.random_id;
        MessageObject messageObject = new MessageObject(this.currentAccount, message2, false, true);
        messageObject.messageOwner.send_state = 1;
        messageObject.wasJustSent = true;
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        arrayList.add(messageObject);
        getMessagesController().updateInterfaceWithMessages(message2.dialog_id, arrayList, 0);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
        ArrayList<TLRPC.Message> arrayList2 = new ArrayList<>();
        arrayList2.add(message2);
        getMessagesStorage().putMessages(arrayList2, false, true, false, 0, false, 0, 0L);
        performSendMessageRequest(tL_messages_sendScreenshotNotification, messageObject, null, null, null, null, false);
    }

    public void sendSticker(TLRPC.Document document, String str, final long j, final MessageObject messageObject, final MessageObject messageObject2, final TL_stories.StoryItem storyItem, final ChatActivity.ReplyQuote replyQuote, final MessageObject.SendAnimationData sendAnimationData, final boolean z, final int i, boolean z2, final Object obj, final String str2, final int i2) {
        final TLRPC.Document document2;
        HashMap hashMap;
        TLRPC.PhotoSize photoSize;
        byte[] bArr;
        if (document == null) {
            return;
        }
        if (DialogObject.isEncryptedDialog(j)) {
            if (getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j))) == null) {
                return;
            }
            TLRPC.TL_document_layer82 tL_document_layer82 = new TLRPC.TL_document_layer82();
            tL_document_layer82.id = document.id;
            tL_document_layer82.access_hash = document.access_hash;
            tL_document_layer82.date = document.date;
            tL_document_layer82.mime_type = document.mime_type;
            byte[] bArr2 = document.file_reference;
            tL_document_layer82.file_reference = bArr2;
            if (bArr2 == null) {
                tL_document_layer82.file_reference = new byte[0];
            }
            tL_document_layer82.size = document.size;
            tL_document_layer82.dc_id = document.dc_id;
            tL_document_layer82.attributes = new ArrayList<>();
            for (int i3 = 0; i3 < document.attributes.size(); i3++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i3);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                    TLRPC.TL_documentAttributeVideo_layer159 tL_documentAttributeVideo_layer159 = new TLRPC.TL_documentAttributeVideo_layer159();
                    tL_documentAttributeVideo_layer159.flags = documentAttribute.flags;
                    tL_documentAttributeVideo_layer159.round_message = documentAttribute.round_message;
                    tL_documentAttributeVideo_layer159.supports_streaming = documentAttribute.supports_streaming;
                    tL_documentAttributeVideo_layer159.duration = documentAttribute.duration;
                    tL_documentAttributeVideo_layer159.w = documentAttribute.w;
                    tL_documentAttributeVideo_layer159.h = documentAttribute.h;
                    tL_document_layer82.attributes.add(tL_documentAttributeVideo_layer159);
                } else {
                    tL_document_layer82.attributes.add(documentAttribute);
                }
            }
            if (tL_document_layer82.mime_type == null) {
                tL_document_layer82.mime_type = "";
            }
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 10);
            if ((closestPhotoSizeWithSize instanceof TLRPC.TL_photoSize) || (closestPhotoSizeWithSize instanceof TLRPC.TL_photoSizeProgressive) || (closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize)) {
                File pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, true);
                if ((closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize) || pathToAttach.exists()) {
                    try {
                        if (closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize) {
                            photoSize = new TLRPC.TL_photoStrippedSize();
                            bArr = closestPhotoSizeWithSize.bytes;
                        } else {
                            TLRPC.TL_photoCachedSize tL_photoCachedSize = new TLRPC.TL_photoCachedSize();
                            pathToAttach.length();
                            byte[] bArr3 = new byte[(int) pathToAttach.length()];
                            new RandomAccessFile(pathToAttach, "r").readFully(bArr3);
                            photoSize = tL_photoCachedSize;
                            bArr = bArr3;
                        }
                        TLRPC.TL_fileLocation_layer82 tL_fileLocation_layer82 = new TLRPC.TL_fileLocation_layer82();
                        TLRPC.FileLocation fileLocation = closestPhotoSizeWithSize.location;
                        tL_fileLocation_layer82.dc_id = fileLocation.dc_id;
                        tL_fileLocation_layer82.volume_id = fileLocation.volume_id;
                        tL_fileLocation_layer82.local_id = fileLocation.local_id;
                        tL_fileLocation_layer82.secret = fileLocation.secret;
                        photoSize.location = tL_fileLocation_layer82;
                        photoSize.size = closestPhotoSizeWithSize.size;
                        photoSize.w = closestPhotoSizeWithSize.w;
                        photoSize.h = closestPhotoSizeWithSize.h;
                        photoSize.type = closestPhotoSizeWithSize.type;
                        photoSize.bytes = bArr;
                        tL_document_layer82.thumbs.add(photoSize);
                        tL_document_layer82.flags |= 1;
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            }
            if (tL_document_layer82.thumbs.isEmpty()) {
                TLRPC.TL_photoSizeEmpty tL_photoSizeEmpty = new TLRPC.TL_photoSizeEmpty();
                tL_photoSizeEmpty.type = "s";
                tL_document_layer82.thumbs.add(tL_photoSizeEmpty);
            }
            document2 = tL_document_layer82;
        } else {
            document2 = document;
        }
        if (MessageObject.isGifDocument(document2)) {
            mediaSendQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda87
                @Override // java.lang.Runnable
                public final void run() {
                    SendMessagesHelper.this.lambda$sendSticker$6(document2, j, messageObject, messageObject2, z, i, obj, sendAnimationData, storyItem, replyQuote, str2, i2);
                }
            });
            return;
        }
        if (TextUtils.isEmpty(str)) {
            hashMap = null;
        } else {
            hashMap = new HashMap();
            hashMap.put("query", str);
        }
        SendMessageParams of = SendMessageParams.of((TLRPC.TL_document) document2, null, null, j, messageObject, messageObject2, null, null, null, hashMap, z, i, 0, obj, sendAnimationData, z2);
        of.replyToStoryItem = storyItem;
        of.replyQuote = replyQuote;
        of.quick_reply_shortcut = str2;
        of.quick_reply_shortcut_id = i2;
        sendMessage(of);
    }

    public int sendVote(final MessageObject messageObject, ArrayList<TLRPC.PollAnswer> arrayList, final Runnable runnable) {
        byte[] bArr;
        if (messageObject == null) {
            return 0;
        }
        final String str = "poll_" + messageObject.getPollId();
        if (this.waitingForCallback.containsKey(str)) {
            return 0;
        }
        TLRPC.TL_messages_sendVote tL_messages_sendVote = new TLRPC.TL_messages_sendVote();
        tL_messages_sendVote.msg_id = messageObject.getId();
        tL_messages_sendVote.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
        if (arrayList != null) {
            bArr = new byte[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC.PollAnswer pollAnswer = arrayList.get(i);
                tL_messages_sendVote.options.add(pollAnswer.option);
                bArr[i] = pollAnswer.option[0];
            }
        } else {
            bArr = new byte[0];
        }
        this.waitingForVote.put(str, bArr);
        return getConnectionsManager().sendRequest(tL_messages_sendVote, new RequestDelegate() { // from class: org.telegram.messenger.SendMessagesHelper$$ExternalSyntheticLambda5
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                SendMessagesHelper.this.lambda$sendVote$21(messageObject, str, runnable, tLObject, tL_error);
            }
        });
    }
}
